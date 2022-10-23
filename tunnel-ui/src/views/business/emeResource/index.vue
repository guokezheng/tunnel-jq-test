<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资源编号" prop="resourceId">
        <el-input
          v-model="queryParams.resourceId"
          placeholder="请输入资源编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源类型" prop="resourceType">
        <el-select v-model="queryParams.resourceType" placeholder="请选择资源类型" clearable size="small">
          <el-option v-for="(dict,i) in resourceTypeOptions" :key="i" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
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
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable>
          <el-option label="启用" value="1"></el-option>
          <el-option label="停用" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
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
          v-hasPermi="['business:emeResource:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:emeResource:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:emeResource:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['business:emeResource:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="Table" v-loading="loading" :data="emeResourceList" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源编号" align="center" prop="resourceId" />
      <el-table-column label="资源名称" align="center" prop="resourceName" />
      <el-table-column label="资源类型" align="center" prop="dictLabel" />
      <el-table-column label="隧道" align="center" prop="tunnelName" />
      <el-table-column label="距离(m)" align="center" prop="distance" />
      <el-table-column label="负责人" align="center" prop="person" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column label="状态" align="center" prop="state" >
        <template slot-scope="scope">
          <div style="text-align: center;">
                <span :style="
                          scope.row.state == '启用'
                            ? 'color: #00aa00'
                            : 'color: #ff0000'
                        ">
                  {{scope.row.state}}
                </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="资源详情" align="center" prop="detail" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:emeResource:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:emeResource:remove']"
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

    <!-- 添加或修改周边资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="资源编号" prop="resourceId">
          <el-input v-model="form.resourceId" placeholder="请输入资源编号" />
        </el-form-item>
        <el-form-item label="资源名称" prop="resourceName">
          <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="form.resourceType" placeholder="请选择资源类型" clearable size="small" style="width: 100%">
            <el-option v-for="(dict,i) in resourceTypeOptions" :key="i" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="隧道" prop="tunnelId">
          <el-select v-model="form.tunnelId" placeholder="请选择隧道" clearable size="small" style="width: 100%">
            <el-option v-for="item in tunnelData" :key="item.tunnelName" :label="item.tunnelName" :value="item.tunnelId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="距离" prop="distance">
          <el-input v-model="form.distance" placeholder="请输入距离" style="width:60%;" /> m
        </el-form-item>
        <el-form-item label="负责人" prop="person">
          <el-input v-model="form.person" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-select v-model="form.state" placeholder="请选择状态" clearable style="width: 100%">
            <el-option label="启用" value="1"></el-option>
            <el-option label="停用" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源详情" prop="detail">
          <el-input type="textarea" v-model="form.detail" placeholder="请输入资源详情" :autosize="{ minRows: 2, maxRows: 4 }" maxlength="250" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm" v-prevent-click>确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEmeResource, getEmeResource, delEmeResource, addEmeResource, updateEmeResource, exportEmeResource } from "@/api/business/emeResource";
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  name: "EmeResource",
  components: {
  },
  data() {
    return {
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
      // 周边资源表格数据
      emeResourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resourceId: null,
        resourceName: null,
        resourceType: null,
        tunnelId: null,
        distance: null,
        person: null,
        phone: null,
        state: null,
        detail: null
      },
      tunnelData:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        resourceId: [{
          required: true,
          message: '请输入资源编号',
          trigger: 'blur'
        },{
          min: 1,
          max: 30,
          message: '长度在1~30个字符之间',
          trigger: 'blur'
        }],
        resourceName: [{
          required: true,
          message: '请输入资源名称',
          trigger: 'blur'
        },{
          min: 1,
          max: 30,
          message: '长度在1~30个字符之间',
          trigger: 'blur'
        }],
        resourceType: [{
          required: true,
          message: '请选择资源类型',
          trigger: 'change'
        }],
        tunnelId: [{
          required: true,
          message: '请选择隧道',
          trigger: 'change'
        }],
        distance: [{
          required: true,
          message: '请输入距离',
          trigger: 'blur'
        }],
        state: [{
          required: true,
          message: '请选择状态',
          trigger: 'change'
        }],
        detail: [{
          required: true,
          message: '请输入资源详情',
          trigger: 'blur'
        }],
      },
      // 演练类型字典
      resourceTypeOptions: []
    };
  },
  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_resource_type").then(response => {
      this.resourceTypeOptions = response.data;
    });
  },
  methods: {
      // 获取所有隧道名称 下拉框
      getTunnels() {
        listTunnels().then(response => {
          this.tunnelData = response.rows;
        });
      },
    /** 查询周边资源列表 */
    getList() {
      this.loading = true;
      listEmeResource(this.queryParams).then(response => {
        this.emeResourceList = response.rows;
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
        resourceId: null,
        resourceName: null,
        resourceType: null,
        tunnelId: null,
        distance: null,
        person: null,
        phone: null,
        state: null,
        createTime: null,
        updateTime: null,
        detail: null
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
    // 点击某一行，将其选中
    handleRowClick(row,i,a) {
      this.$refs.Table.toggleRowSelection(row)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加周边资源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEmeResource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改周边资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      // if(this.submitFormLoading) return
      // this.submitFormLoading = true
      this.$refs["form"].validate(async valid => {
        if (valid) {
          if (this.form.id != null) {
            await updateEmeResource(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addEmeResource(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        // this.submitFormLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除周边资源?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEmeResource(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有周边资源数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEmeResource(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
