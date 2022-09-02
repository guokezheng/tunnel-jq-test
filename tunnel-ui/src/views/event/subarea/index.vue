<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分区名称" prop="sName">
        <el-input
          v-model="queryParams.sName"
          placeholder="请输入分区名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable >
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
          v-hasPermi="['tunnel:subarea:add']"
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
          v-hasPermi="['tunnel:subarea:edit']"
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
          v-hasPermi="['tunnel:subarea:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['tunnel:subarea:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="subareaList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="分区id" align="center" prop="sId" /> -->

      <!-- <el-table-column label="分区id" align="center" prop="sId" /> -->

      <!-- <el-table-column label="分区名称" align="center" prop="sName" /> -->      
      <el-table-column label="隧道名称" align="center" prop="tunnelId" />
      <el-table-column label="桩号下限" align="center" prop="pileMin" />
      <el-table-column label="桩号上限" align="center" prop="pileMax" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tunnel:subarea:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tunnel:subarea:remove']"
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

    <!-- 添加或修改隧道分区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分区名称" prop="sName">
          <el-input v-model="form.sName" placeholder="请输入分区名称" />
        </el-form-item>

        <!-- <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item> -->

        <!-- <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item> -->
        <el-form-item label="隧道名称" prop="tunnelId" >
        <el-select v-model="form.tunnelId" placeholder="请选择隧道" class="tunnelName" >
          <el-option
              v-for="item in tunnelData"
              :key="item.tunnelName"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
        </el-select>
      </el-form-item>

        <el-form-item label="桩号下限" prop="pileMin">
          <el-input v-model="form.pileMin" placeholder="请输入桩号下限" />
        </el-form-item>
        <el-form-item label="桩号上限" prop="pileMax">
          <el-input v-model="form.pileMax" placeholder="请输入桩号上限" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

// import { listSubarea, getSubarea, delSubarea, addSubarea, updateSubarea, exportSubarea } from "@/api/tunnel/subarea";

import { listSubarea, getSubarea, delSubarea, addSubarea, updateSubarea, exportSubarea } from "@/api/event/subarea";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "Subarea",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 隧道分区表格数据
      subareaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sName: null,
        tunnelId: null,
        pileMin: null,
        pileMax: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sName: [
            { required: true, message: '请输入分区名称', trigger: 'change' }
          ],
          tunnelId: [
            { required: true, message: '请选择隧道名称', trigger: 'change' }
          ],
          sName: [
            { required: true, message: '请输入分区名称', trigger: 'change' }
          ],

      },

      tunnelData:[]

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
          console.log( this.tunnelData,' this.tunnelData')
        console.log( this.subareaList,'this.subareaListthis.subareaList')
        this.tunnelData.forEach((item,index)=>{
            this.subareaList.forEach((it,id)=>{
                if(item.tunnelId==it.tunnelId){
                  it.tunnelId=item.tunnelName
                }
            })
        })
        });
      
        
      },

    /** 查询隧道分区列表 */
    getList() {
      this.loading = true;
      listSubarea(this.queryParams).then(response => {
        this.subareaList = response.rows;
        this.total = response.total;
        this.loading = false;
        console.log(response,'responseresponse')
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
        sId: null,
        sName: null,
        tunnelId: null,
        createBy: null,
        pileMin: null,
        createTime: null,
        pileMax: null,
        updateBy: null,
        updateTime: null
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
      this.ids = selection.map(item => item.sId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加隧道分区";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const sId = row.sId || this.ids
      getSubarea(sId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改隧道分区";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.sId != null) {
            updateSubarea(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getTunnels()
            });
          } else {
            addSubarea(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.getTunnels()
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const sIds = row.sId || this.ids;
      this.$modal.confirm('是否确认删除隧道分区编号为"' + sIds + '"的数据项？').then(function() {
        return delSubarea(sIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
        this.getTunnels()
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有隧道分区数据项？').then(() => {
        this.exportLoading = true;
        return exportSubarea(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>


<style scoped>

.tunnelName{
  width: 378px;
}

</style>

