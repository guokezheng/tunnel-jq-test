<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件地址" prop="url">
        <el-input
          v-model="queryParams.url"
          placeholder="请输入文件地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件名称" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
              v-for="item in tunnelData"
              :key="item.tunnelName"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:emeDiagram:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:emeDiagram:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:emeDiagram:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:emeDiagram:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="emeDiagramList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道" align="center" prop="tunnelName" />
      <el-table-column label="文件地址" align="center" prop="url" />
      <el-table-column label="文件名称" align="center" prop="fileName" />
      <!-- <el-table-column label="隧道id" align="center" prop="tunnelId" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:emeDiagram:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:emeDiagram:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改作战示意图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="示意图ID">
          <fileUpload v-model="form.fileId" @fileData='fileData'/>
        </el-form-item>
        <el-form-item label="文件地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入文件地址" />
        </el-form-item>
        <el-form-item label="文件名称" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitBtnLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEmeDiagram, getEmeDiagram, delEmeDiagram, addEmeDiagram, updateEmeDiagram, exportEmeDiagram } from "@/api/business/emeDiagram";
import FileUpload from '@/components/FileUpload';
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "EmeDiagram",
  components: {
    FileUpload,
  },
  data() {
    return {
        tunnelData: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 作战示意图表格数据
      emeDiagramList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 新增修改弹窗确认按钮loading
      submitBtnLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        url: null,
        fileName: null,
        tunnelId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getTunnels()
  },
  methods: {
      // 隧道名称 下拉框
      getTunnels() {
        listTunnels().then(response => {
          this.tunnelData = response.rows;
        });
      },
    // 接收子组件传来的上传文件
    fileData(data){
      console.log(data,"上传文件父组件")
      this.form.fileId = data
    },
    /** 查询作战示意图列表 */
    getList() {
      this.loading = true;
      listEmeDiagram(this.queryParams).then(response => {
        this.emeDiagramList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        fileId: null,
        url: null,
        fileName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        tunnelId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加作战示意图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEmeDiagram(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改作战示意图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitBtnLoading) return
      this.submitBtnLoading = true
      this.$refs["form"].validate(async valid => {
        if (valid) {
          if (this.form.id != null) {
            await updateEmeDiagram(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addEmeDiagram(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        this.submitBtnLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除选中数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEmeDiagram(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有作战示意图数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEmeDiagram(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
