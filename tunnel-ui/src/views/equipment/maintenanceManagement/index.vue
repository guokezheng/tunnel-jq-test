<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="养护人员" prop="maintenancePerson">
        <el-input
          v-model="queryParams.maintenancePerson"
          placeholder="请输入养护人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属隧道" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in eqTunnelData"
                     :key="item.tunnelId"
                     :label="item.tunnelName"
                     :value="item.tunnelId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:management:add']"
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
          v-hasPermi="['system:management:edit']"
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
          v-hasPermi="['system:management:remove']"
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
          v-hasPermi="['system:management:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="养护人员" align="center" prop="maintenancePerson" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="位置信息" align="center" prop="maintenanceLocation" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column label="养护内容" align="center" prop="maintenanceInformation" />
      <el-table-column label="养护进度" align="center" prop="curingProgress">
        <template slot-scope="scope">
          <div>{{scope.row.curingProgress}}%</div>
        </template>
      </el-table-column>
      <el-table-column label="养护记录" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-data-analysis"
            @click="openRecord(scope.row)"
            v-hasPermi="['trafficFlowInformation:information:edit']"
          >查看详情</el-button>
      
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remake" />
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:management:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:management:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog title="养护记录" :visible.sync="openMaintenanceRecord" width="500px" append-to-body class="inforDialog">
      <div v-if="recordData" v-for="(item,index) in recordData">
       <img :src="item.url" style="width: 100%;"/>
      </div>
      <div v-else>
        无养护照片记录
      </div>
    </el-dialog>
    <!-- 添加或修改养护管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="养护人员" prop="maintenancePerson">
          <el-input v-model="form.maintenancePerson" placeholder="请输入养护人员" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select v-model="form.tunnelId" placeholder="请选择所属隧道" clearable size="small" style="width: 100%">
            <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置信息" prop="maintenanceLocation">
          <el-input v-model="form.maintenanceLocation" placeholder="请输入位置信息" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="养护内容" prop="maintenanceInformation">
          <el-input v-model="form.maintenanceInformation" placeholder="请输入养护内容" />
        </el-form-item>
        <el-form-item label="养护进度" prop="curingProgress">
          <el-input v-model="form.curingProgress" placeholder="请输入养护进度" />
        </el-form-item>
        <el-form-item label="备注" prop="remake">
          <el-input v-model="form.remake" placeholder="请输入备注" />
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
import { listManagement, getManagement, delManagement, addManagement, updateManagement, exportManagement }
  from "@/api/equipment/maintenanceManagement/maintenanceManagement";
import {listTunnels} from "@/api/equipment/tunnel/api";

export default {
  name: "MaintenanceManagement",
  data() {
    return {
      recordData:[],
      openMaintenanceRecord:false,
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
      // 养护管理表格数据
      managementList: [],
      eqTunnelData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        maintenancePerson: null,
        tunnelId: null,
        maintenanceLocation: null,
        phone: null,
        maintenanceInformation: null,
        curingProgress: null,
        remake: null,
        remake1: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        maintenancePerson: [{
          required: true,
          message: '请输入养护人员',
          trigger: 'blur'
        },{
          message: '长度在1~30个字符之间',
          min: 1,
          max: 30,
          trigger: 'blur'
        }],
        tunnelId: [{
          required: true,
          message: '请选择所属隧道',
          trigger: 'change'
        }],
        maintenanceLocation: [{
          required: true,
          message: '请输入位置信息',
          trigger: 'blur'
        },{
          message: '长度在1~30个字符之间',
          min: 1,
          max: 30,
          trigger: 'blur'
        }],
        phone: [{
          required: true,
          message: '请输入联系方式',
          trigger: 'blur'
        }],
        maintenanceInformation: [{
          required: true,
          message: '请输入养护内容',
          trigger: 'blur'
        },{
          message: '长度在1~30个字符之间',
          min: 1,
          max: 30,
          trigger: 'blur'
        }],
        curingProgress: [{
          required: true,
          message: '请输入养护进度',
          trigger: 'blur'
        },{
            pattern: /^(?:[1-9]?\d|100)$/,
            message: '请输入数字(区间为0~100)'
          }],
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
  },
  methods: {
    openRecord(row){
      this.openMaintenanceRecord = true
      getManagement(row.id).then(response => {
        console.log(response.data,"养护管理row")
        this.recordData = response.data.fileLists
      })
    },
    /** 查询养护管理列表 */
    getList() {
      this.loading = true;
      listManagement(this.queryParams).then(response => {
        this.managementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
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
        maintenancePerson: null,
        tunnelId: null,
        maintenanceLocation: null,
        phone: null,
        maintenanceInformation: null,
        curingProgress: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remake: null,
        remake1: null
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
      this.title = "添加养护管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getManagement(id).then(response => {
        console.log(response.data,"养护管理row")
        this.form = response.data;
        this.open = true;
        this.title = "修改养护管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateManagement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addManagement(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除选中数据项？').then(function() {
        return delManagement(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有养护管理数据项？').then(() => {
        this.exportLoading = true;
        return exportManagement(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
