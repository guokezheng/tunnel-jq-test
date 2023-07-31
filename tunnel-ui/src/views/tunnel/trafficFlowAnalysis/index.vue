<template>
    <div class="dashboard-editor-container">
        <el-row style="margin-bottom: 5px;">
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-col :span="5">
              <el-select @change="getAnalysisData" v-model="queryParams.tunnelId" placeholder="请选择隧道" size="small">
                <el-option
                  v-for="item in tunnelList"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
          </el-col>
          <el-col :span="10">
              <el-radio-group v-model="queryParams.holes" @change="getAnalysisData">
                <el-radio style="margin-right: 5px;" border key="" label="">全部</el-radio>
                <el-radio style="margin-right: 5px;" border key="1" label="1">上行</el-radio>
                <el-radio border key="2" label="2">下行</el-radio>
              </el-radio-group>
          </el-col>
          </el-form>
        </el-row>
        <el-row :gutter="16">
          <el-col :lg="12">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>年度车流量统计</span>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <bar-chart v-bind:yearData="yearData" />
                </div>
              </div>
          </el-col>
          <el-col :lg="12">
            <div class="card-style">
              <div class="card-title">
                <div style="float: left;padding-top: 10px;">
                  <span>季度车流量统计</span>
                </div>
                <div style="float: right;margin-right: 10px;">
                  <el-date-picker type="year" value-format="yyyy-MM-dd HH:mm:ss" v-model="datePickerJd" placeholder="请选择年份" key='year'>
                  </el-date-picker>&nbsp;
                  <el-button type="primary" @click="examTimesClick('quarter')">确定</el-button>
                </div>
              </div>
              <div class="card-line"></div>
              <div class="chart-wrapper" style="height: calc(100% - 50px);">
                <jd-chart v-bind:jdData="jdData" />
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :lg="12">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>月度车流量统计</span>
                  </div>
                  <div style="float: right;margin-right: 10px;">
                    <el-date-picker type="year" value-format="yyyy-MM-dd HH:mm:ss" v-model="datePickerMonth" placeholder="请选择年份">
                    </el-date-picker>&nbsp;
                    <el-button type="primary" @click="examTimesClick('month')">确定</el-button>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <month-chart v-bind:monthData="monthData" :datePickerMonth="datePickerMonth"/>
                </div>
              </div>
          </el-col>
          <el-col :lg="12">
            <div class="card-style">
              <div class="card-title">
                <div style="float: left;padding-top: 10px;">
                  <span>日车流量统计</span>
                </div>
                <div style="float: right;margin-right: 10px;">
                  <el-date-picker type="month" value-format="yyyy-MM-dd HH:mm:ss" v-model="datePickerDay" placeholder="请选择日期">
                  </el-date-picker>&nbsp;
                  <el-button type="primary" @click="examTimesClick('day')">确定</el-button>
                </div>
              </div>
              <div class="card-line"></div>
              <div class="chart-wrapper" style="height: calc(100% - 50px);">
                <day-chart v-bind:dayData="dayData" :datePickerDay="datePickerDay"/>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :lg="12">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>日内车流量统计</span>
                  </div>
                  <div style="float: right;margin-right: 10px;">
                    <el-date-picker v-model="datePickerHour" placeholder="请选择日期" value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>&nbsp;
                    <el-button type="primary" @click="examTimesClick('hour')">确定</el-button>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <oneday-chart v-bind:dayData="onedayData"/>
                </div>
              </div>
          </el-col>
          <el-col :lg="12">
            <div class="card-style">
              <div class="card-title">
                <div style="float: left;padding-top: 10px;">
                  <span>时段车流量统计</span>
                </div>
                <div style="float: right;margin-right: 10px;">
                  <el-date-picker
                    v-model="timePickerDay"
                    size="small"
                    style="width: 240px"
                    value-format="yyyy-MM-dd"
                    type="daterange"
                    range-separator="-"
                    unlink-panels
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                     :picker-options="pickerOptions"
                  ></el-date-picker>
                  <el-button type="primary" @click="examTimesClick('time')">确定</el-button>
                </div>
              </div>
              <div class="card-line"></div>
              <div class="chart-wrapper" style="height: calc(100% - 50px);">
                <time-chart v-bind:dayData="timeData" :timePickerDay="timePickerDay"/>
              </div>
            </div>
          </el-col>
        </el-row>
    </div>
</template>

<script>
import { analysisData ,analysisDataByTime } from "@/api/system/trafficStatistics/api.js";
import { listTunnels  } from "@/api/equipment/tunnel/api.js";
import LineChart from '../../tunnel/trafficFlowAnalysis/LineChart';
import BarChart from '../../tunnel/trafficFlowAnalysis/BarChart';
import MonthChart from '../../tunnel/trafficFlowAnalysis/MonthChart';
import DayChart from '../../tunnel/trafficFlowAnalysis/DayChart';
import OnedayChart from '../../tunnel/trafficFlowAnalysis/OnedayChart';
import JdChart from '../../tunnel/trafficFlowAnalysis/JdChart';
import TimeChart from '../../tunnel/trafficFlowAnalysis/TimeChart';
import moment from "moment";
export default {
  name: "TrafficFlowAnalysis",
  components: {
      LineChart,
      BarChart,
      MonthChart,
      DayChart,
      JdChart,
      OnedayChart,
      TimeChart
    },
  data() {
    return {
      queryParams:{
        tunnelId:null,
        holes:""
      },
      tunnelList:[],
      yearData:[],
      monthData:[],
      dayData:[],
      jdData:[],
      onedayData:[],
      timeData:[],
      datePickerJd:new Date(),
      datePickerMonth:new Date(),
      datePickerDay:new Date(),
      timePickerDay:[],
      datePickerHour:'',
      pickerOptions:{
        onPick: (obj) => {
          this.pickerMinDate = new Date(obj.minDate).getTime();
        },
        disabledDate:(time)=> {
            if (this.pickerMinDate) {
                const day1 =  366 * 24 * 3600 * 1000
                let maxTime = this.pickerMinDate + day1
                let minTime = this.pickerMinDate - day1
                return time.getTime() > maxTime || time.getTime()<minTime || time.getTime() > Date.now()+0 * 24 * 3600 * 1000
            }else{
                return time.getTime() > Date.now()+0 * 24 * 3600 * 1000
            }
        },
      }
    };
  },
  created() {
    this.getTunnelList();

  },

  methods: {
    // 获取分析数据
    getAnalysisData(){
      this.datePickerJd = new Date()
      this.datePickerMonth= new Date()
      this.datePickerDay = new Date()
      this.timePickerDay = []
      this.datePickerHour = new Date()
      let times = moment(new Date()).format("YYYY-MM-DD")
      let yesTime = Date.parse(new Date())-172800000
      let yesTimes = moment(new Date(yesTime)).format("YYYY-MM-DD")
      this.timePickerDay.push(yesTimes)
      this.timePickerDay.push(times)

      analysisData(this.queryParams.tunnelId,this.queryParams.holes).then(response => {
        if(response.code == 200){
            this.yearData = response.data.yearData;
            this.monthData = response.data.monthData;
            // 1个月的天
            this.dayData = response.data.dayData;
            this.jdData = response.data.quarterData;
            // 1天的时段
            this.onedayData = response.data.hourData;
            // 多个天的时段
            this.timeData = response.data.timeData;
        }
      });
    },
    // 获取隧道
    getTunnelList(){
      listTunnels().then(response => {
        this.tunnelList = response.rows
        this.queryParams.tunnelId = this.tunnelList[0].tunnelId;
        this.getAnalysisData();
      });
    },
    //
    formatDate(time) {
          var date=new Date(parseInt(time));
          var year=date.getFullYear();
          var mon = date.getMonth()+1;
          var day = date.getDate();
          return year+'/'+mon+'/'+day;
    },
    // 日期变更
    examTimesClick(key){
      debugger
      // 季度
      var dateRange = [];
      if(key == 'quarter'){
        dateRange = [this.datePickerJd,new Date()]
      }
      // 月度
      else if(key == 'month'){
        dateRange = [this.datePickerMonth,new Date()]
      }
      // 天
      else if(key == 'day'){
        dateRange = [this.datePickerDay,new Date()]
      }
      // 时段
      else if(key == 'time'){
        dateRange.push(this.timePickerDay[0]+" 00:00:00")
        dateRange.push(this.timePickerDay[1]+" 23:59:59")
        // dateRange = this.timePickerDay
        console.log(dateRange,"dateRange")
      }
      else if(key == 'hour'){
        dateRange = [this.datePickerHour,new Date()]
      }
      var json = {
        eqDirection:key,
        tunnelId:this.queryParams.tunnelId,
        holes:this.queryParams.holes
      }
      analysisDataByTime(this.addDateRange(json, dateRange)).then(response => {
        if(response.code == 200){
            if(key == 'quarter'){
              this.jdData = response.data;
            }
            // 月度
            else if(key == 'month'){
              this.monthData = response.data;
            }
            // 天
            else if(key == 'day'){
              this.dayData = response.data;
            }
            // 时段
            else if(key == 'time'){
              this.timeData = response.data;
            }
            else if(key == 'hour'){
              this.onedayData = response.data;
            }
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 16px;
    padding-top: 5px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 16px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

  .card-style{
    background-color: #fff;
    margin-bottom: 2vh;
    min-height: 200px;
  }

  .card-title{
    padding-left: 20px;
    padding-right: 10px;
    height: 50px;
    padding-top: 5px;
    font-size:17px;
  }

  .card-line{
    border-top: 1px #DDDDDD solid;
    margin-left: 20px;
    margin-right: 20px;
  }

</style>
