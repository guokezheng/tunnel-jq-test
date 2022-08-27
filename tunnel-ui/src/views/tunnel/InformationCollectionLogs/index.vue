<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" prop="eqTypeId">
        <el-select v-model="queryParams.eqTypeId" placeholder="请选择设备类型" clearable size="small">
         <el-option
           v-for="item in eqTypeData"
           :key="item.typeId"
           :label="item.typeName"
           :value="item.typeId"
         />
        </el-select>
      </el-form-item>
      <el-form-item label="采集时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH-DD-SS"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="logList" height="670" :default-sort = "{prop: 'createTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="序号" align="center" prop="id" display="none"/>

      <el-table-column label="隧道名称" align="center" prop="tunnelName.tunnelName" />
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
      <el-table-column label="采集时间" align="center" prop="createTime" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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

  </div>
</template>

<script>
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/system/log";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";

export default {
  name: "InformationCollectionLogs",
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
      // 操作日志表格数据
      logList: [],
      // 弹出层标题
      title: "",
      //所属隧道
      eqTunnelData:{},
      //设备类型
      eqTypeData:{},
      eqNameData:{},
      // 是否显示弹出层
      open: false,
      // 控制方式   3：手动 1：时间控制 2：光强控制字典
      controlTypeOptions: [],
      // 日期范围
      dateRange: [],
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqTypeId: null,
        tunnelId: null,
        userName: null,
        eqId: null,
        /* eqName: null, */
        code: null,
        cmd: null,
        beforeState: null,
        operationState: null,
        controlType: null,
        state: null,
        description: null
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
    this.getTunnel();
    this.getEqType();
    /* this.getDevices(); */
    // this.getDicts("sd_control_type").then(response => {
    //   this.controlTypeOptions = response.data;
    // });
    // this.getDicts("sd_operation_log_state").then(response => {
    //   this.operationStateOptions = response.data;
    // });
  },
  methods: {
    /** 查询信息采集日志列表 */
    getList() {
      this.loading = true;
      listLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.logList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then(response => {
        this.eqTypeData = response.rows;
      });
    },
    // 控制方式   3：手动 1：时间控制 2：光强控制字典翻译
    // controlTypeFormat(row, column) {
    //   return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    // },
    //操作是否成功 0：成功 1：失败
    // stateFormat(row, column) {
    //   return this.selectDictLabel(this.operationStateOptions, row.state);
    // },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        eqType: null,
        tunnelId: null,
        userName: null,
        eqId: null,
       /* eqName: null, */
        code: null,
        cmd: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        beforeState: null,
        operationState: null,
        controlType: null,
        state: null,
        description: null
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
      this.dateRange = [];
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
      this.title = "添加信息采集日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改信息采集日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLog(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addLog(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delLog(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/log/export', {
        ...this.queryParams
      }, `system_log.xlsx`)
    }
  }
};
</script>
