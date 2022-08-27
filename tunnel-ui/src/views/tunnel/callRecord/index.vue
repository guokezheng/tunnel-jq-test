<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="记录时间" prop="dateRange">
        <el-date-picker v-model="dateRange" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss" align="right" unlink-panels range-separator="至"
          start-placeholder="开始时间" end-placeholder="结束时间" :default-time="['00:00:00', '23:59:59']">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="被叫号码" prop="cdpn">
        <el-input
          v-model="queryParams.cdpn"
          placeholder="请输入被叫号码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row type="flex">
      <el-col :span="24" style="margin:0 20px 20px 20px">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">紧急电话记录统计</span>
          </div>
          <div class="body">
            <line-chart :chartData="recordList" />
          </div>
        </el-card>
      </el-col>
      <!-- <el-col :span="12" style="margin:0 20px 20px 20px">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">紧急电话记录分析</span>
          </div>
          <!-- <div class="body">
            <circle-chart :chartData="recordList" />
          </div> -->
        </el-card>
      </el-col> 
    </el-row>

    <el-table v-loading="loading" :data="recordList" :default-sort = "{prop: 'timeStart', order: 'descending'}">
          <el-table-column label="id" align="center" prop="id" display:"none" />
          <el-table-column label="呼叫编号" align="center" prop="callId" />
          <!-- <el-table-column label="主叫人员" align="center" prop="callName" /> -->
          <!-- <el-table-column label="所属隧道" align="center" prop="pathId" :formatter="getPathName" /> -->
          <el-table-column label="所属隧道" align="center" prop="pathId" />
          <el-table-column label="主叫号码" align="center" prop="cpn" />
          <el-table-column label="被叫号码" align="center" prop="cdpn" />
          <el-table-column label="通话类别" align="center" prop="type" />
          <el-table-column label="开始时间" align="center" prop="timeStart" width="180" sortable>
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.timeStart, '{y}-{m}-{d} {h}:{m}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="timeEnd" width="180" sortable>
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.timeEnd, '{y}-{m}-{d} {h}:{m}') }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="接听人员" align="center" prop="receptionName" /> -->
          <!-- <el-table-column label="电话内容" align="center" prop="telephoneContent" /> -->
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
import { listRecord,positionRecord } from "@/api/system/callRecord/api.js";
import LineChart from '../../tunnel/callRecord/LineChart'
import CircleChart from './CircleChart.vue'
export default {
  name: "Record",
  components: {
      LineChart,
      CircleChart,
    },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 非单个禁用
      single: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 紧急电话记录表格数据
      recordList: [],
      // 紧急电话echarts数据
      echartsData:[],
      dateRange:null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        callId: null,
        callName: null,
        cpn: null,
        position: null,
        timeStart: null,
        timeEnd: null,
        receptionName: null,
        cdpn: null,
        telephoneContent: null,
      },
      // 表单参数
      form: {

      },
      // 表单校验
      rules: {

      },
      pathList:[],
    };
  },
  created() {
    this.getName();
    this.dateRange = [];
    var dd = new Date();
    dd.setDate(dd.getDate() - 6);
    this.dateRange = [this.dateToString(dd), this.dateToString(new Date())];
      console.log(this.dateRange)
      console.log('this.dateRange')
    this.getList();
  },
  methods: {
      dateToString(date,cut) {
          var date = new Date(date);
          var YY = date.getFullYear();
          var MM = date.getMonth() + 1;
          var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
          var hh =
              (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
          var mm =
              (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
              ":";
          var ss = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
          return YY + '-' + Number(MM) + '-' + DD + " " + hh + mm + ss;
      },
    /** 查询紧急电话记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.recordList = response.data.tableData.rows;
        // this.echartsData = response.data.echartsData;
        this.total = response.data.tableData.total;
        this.loading = false;
      });
    },
    getName(){
      positionRecord().then(response => {
        this.pathList = response.data;
      });
    },
    getPathName(){
      var actions = [];
      Object.keys(this.pathList).some((key) => {
        if (this.pathList[key].pathId == ('' + row.pathId)) {
          actions.push(this.pathList[key].name);
          return true;
        }
      })
      return actions.join('');
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if(!this.dateRange){
        this.$message.error("请输入记录日期");
        return;
      }

      if(this.dateRange.length < 2){
        this.$message.error("请输入记录日期");
        return;
      }

      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.dateRange = [];
      var dd = new Date();
      dd.setDate(dd.getDate() - 6);
      this.dateRange = [dd,new Date()];
      this.handleQuery();
    }
  }
};
</script>

<style lang="less" scoped>
.phoneStatistics {
  position: relative;
  .title {
    padding: 0 0 15px 0;
    font-size: 18px;
    font-weight: 700;
    position: relative;
    .preposition {
      background-color: #3e7deb;
      width: 6px;
      height: 25px;
      position: absolute;
      left: -15px;
      top: 0;
    }
  }
  .body {
    display: flex;
    justify-content: center;
  }
}
</style>
