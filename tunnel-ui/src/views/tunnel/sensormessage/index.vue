<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="选择隧道" prop="eqTunnelId">
        <el-select @change="getTunnelSelectData" v-model="queryParams.eqTunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="选择设备" prop="eqId">
        <el-select   v-model="queryParams.eqId" placeholder="请选择设备" clearable size="small">
          <el-option
                v-for="item in eqData"
                :key="item.eqId"
                :label="item.eqName"
                :value="item.eqId"/>
          </el-select>
        </el-select>
      </el-form-item>
      <el-form-item label="采集时间">
        <el-date-picker
          v-model="daterangeGettime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"

          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:message:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column label="隧道" align="center" prop="sdTunnel.tunnelName" />
      <el-table-column label="传感设备" align="left" prop="sdDevice.eqName" />
      <!-- <el-table-column label="设备类型" align="center" prop="typeName.typeName" /> -->
      <el-table-column label="现场数据值" align="left" prop="sensorValue" />
      <el-table-column label="采集时间" align="center" prop="gettime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gettime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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
import { listSensorMessage, getDeviceInfo } from '@/api/system/sensor/api.js'
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  name: "Message",
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
      // 传感器采集数据信息表格数据
      messageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 采集数据时间时间范围
      daterangeGettime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqId: null,
        eqType: null,
        eqTunnelId: null,
        gettime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      tunnelData:{},//隧道
      eqData:{}
    };
  },
  created() {
    this.getTunnels();
    this.getEqs();
    this.getList();
  },
  methods: {
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
    },
    getTunnelSelectData(){
      this.getEqs()
    },
    /** 查询设备列表 */
    getEqs() {
      getDeviceInfo(this.queryParams.eqTunnelId).then(response => {
        this.eqData = response.data;
      });
    },
    /** 查询传感器采集数据信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeGettime && '' != this.daterangeGettime) {
        this.queryParams.params["beginGettime"] = this.daterangeGettime[0];
        this.queryParams.params["endGettime"] = this.daterangeGettime[1];
      }
      listSensorMessage(this.queryParams).then(response => {
        this.messageList = response.rows;
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
        eqId: null,
        eqType: null,
        eqTunnelId: null,
        sensorValue: null,
        gettime: null,
        createTime: null,
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
      this.daterangeGettime = [];
      this.resetForm("queryForm");
      this.eqData = {}
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除选中数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMessage(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有传感器采集数据信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportMessage(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
