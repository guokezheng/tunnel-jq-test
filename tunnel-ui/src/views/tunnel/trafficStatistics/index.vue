<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="隧道 " prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入车辆编号 "
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="微波车检" prop="byVehicelNum">
        <el-input
          v-model="queryParams.byVehicelNum"
          placeholder="请输入区域车辆数 "
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="时间" prop="createTime">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 380px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          unlink-panels
          :picker-options="pickerOptions"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="16" style="margin-top: 30px;">
        <el-col :xs="72" :sm="72" :lg="24">
          <div class="chart-wrapper">
             <line-chart />
          </div>
        </el-col>
    </el-row> -->

    <el-table v-loading="loading" show-summary  :data="statisticsList" :summary-method="getTotal" :default-sort = "{prop: 'createTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="id " align="center" prop="statisticsId" /> -->
      <el-table-column label="设备名称" align="center" prop="deviceName" >
        
      </el-table-column>
      <el-table-column label="车道号 " align="center" prop="byLane" />
      <el-table-column label="过车平均速度 " align="center" prop="bySpeed" />
     <!-- <el-table-column label="到达速度" align="center" prop="wArrivalFlow" />
      <el-table-column label="小型车数量 " align="center" prop="dwLightVehicle" />
      <el-table-column label="中型车数量 " align="center" prop="dwMidVehicle" />
      <el-table-column label="重型车数量" align="center" prop="dwHeavyVehicle" />
      <el-table-column label="车头时距" align="center" prop="dwTimeHeadway" />
      <el-table-column label="车头间距" align="center" prop="dwSpaceHeadway" /> -->
      <el-table-column label="占有率(%)" align="center" prop="fSpaceOccupyRation" />
     <!-- <el-table-column label="时间占有率" align="center" prop="fTimeOccupyRation" />
      <el-table-column label="平均停车次数" align="center" prop="byStoppingTimes" />
      <el-table-column label="堵塞状态下排队长度" align="center" prop="byQueueLen" />
      <el-table-column label="上传标识" align="center" prop="byFlag" /> -->
      <el-table-column label="车流量(辆/分钟)"align="center" prop="byVehicelNum" />
      <!-- <el-table-column label="平均延误 " align="center" prop="wDelay" />
      <el-table-column label="非机动车数量 " align="center" prop="dwNonMotor" /> -->
      <el-table-column label="上传时间"align="center" prop="createTime" sortable />
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
import { listStatistics, getStatistics, delStatistics, addStatistics, updateStatistics } from "@/api/system/trafficStatistics/api.js";
import LineChart from '../../tunnel/trafficStatistics/LineChart'
export default {
  name: "Statistics",
  components: {
      LineChart
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
      // 车流量信息表格数据
      statisticsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        statisticsId: null,
        deviceId: null,
        byLane: null,
        bySpeed: null,
        wArrivalFlow: null,
        dwLightVehicle: null,
        dwMidVehicle: null,
        dwHeavyVehicle: null,
        dwTimeHeadway: null,
        dwSpaceHeadway: null,
        fSpaceOccupyRation: null,
        fTimeOccupyRation: null,
        byStoppingTimes: null,
        byQueueLen: null,
        byFlag: null,
        byVehicelNum: null,
        wDelay: null,
        dwNonMotor: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      dateRange:[],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车流量信息列表 */
    getList() {
      this.loading = true;
      listStatistics(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.statisticsList = response.rows;
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
        statisticsId: null,
        deviceId: null,
        byLane: null,
        bySpeed: null,
        wArrivalFlow: null,
        dwLightVehicle: null,
        dwMidVehicle: null,
        dwHeavyVehicle: null,
        dwTimeHeadway: null,
        dwSpaceHeadway: null,
        fSpaceOccupyRation: null,
        fTimeOccupyRation: null,
        byStoppingTimes: null,
        byQueueLen: null,
        byFlag: null,
        byVehicelNum: null,
        wDelay: null,
        dwNonMotor: null,
        createBy: null,
        createTime: null
      };
      this.resetForm("form");
    },

    getTotal(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
        sums[index] = '合计';
        return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (column.property === 'byVehicelNum') {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
            return prev + curr;
            } else {
            return prev;
            }
          }, 0);
          sums[index];
        } else {
          sums[index] = '--';
        }
      });
      return sums;
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
      this.ids = selection.map(item => item.statisticsId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/statistics/export', {
        ...this.queryParams
      }, `system_statistics.xlsx`)
    }
  }
};
</script>
