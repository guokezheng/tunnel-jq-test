<template>
    <div class="dashboard-editor-container">
      <div>
        <el-form :model="coForm" :inline="true" label-width="68px">
          <el-row>
            <el-col :span="5">
                <el-select @change="getAnalysisData"  v-model="coForm.tunnelId" placeholder="请选择隧道" size="small">
                  <el-option
                    v-for="item in tunnelList"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
            </el-col>
          </el-row>
        </el-form>

        <el-form :model="viForm" :inline="true" label-width="68px">
        </el-form>

        <el-row :gutter="16" style="margin-top: 10px;">
          <el-col :lg="24">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>Co浓度趋势分析</span>
                  </div>
                  <div style="float: right;margin-right: 5px;">
                    <el-select  v-model="coForm.eqId" placeholder="请选择传感器设备" clearable size="small">
                      <el-option
                        v-for="item in coList"
                        :key="item.eqId"
                        :label="item.eqName"
                        :value="item.eqId"
                      />
                    </el-select>
                    <el-date-picker
                      v-model="coTimePickerDay"
                      size="small"
                      style="width: 380px"
                      type="daterange"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      range-separator="-"
                      unlink-panels
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                    ></el-date-picker>
                    <el-button type="primary" @click="coTimeDuanSubmit()">确定</el-button>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <coline-chart v-bind:dayData="coTimeData"/>
                </div>
              </div>
          </el-col>
        </el-row>
        </div>
        <el-row :gutter="16">
          <el-col :lg="24">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>VI趋势分析</span>
                  </div>
                  <div style="float: right;margin-right: 5px;">
                       <el-select v-model="viForm.eqId" placeholder="请选择传感器设备" clearable size="small">
                         <el-option
                           v-for="item in viList"
                           :key="item.eqId"
                           :label="item.eqName"
                           :value="item.eqId"
                         />
                       </el-select>
                     <el-date-picker
                       v-model="vitime"
                       size="small"
                       style="width: 380px"
                       type="daterange"
                       value-format="yyyy-MM-dd HH:mm:ss"
                       range-separator="-"
                       unlink-panels
                       start-placeholder="开始日期"
                       end-placeholder="结束日期"
                     ></el-date-picker>
                    <el-button type="primary" @click="viTimeDuanSubmit()">确定</el-button>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <viline-chart v-bind:dayData="viTimeData"/>
                </div>
              </div>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :lg="24">
              <div class="card-style">
                <div class="card-title">
                  <div style="float: left;padding-top: 10px;">
                    <span>风速趋势分析</span>
                  </div>
                  <div style="float: right;margin-right: 5px;">
                       <el-select v-model="wsForm.eqId" placeholder="请选择传感器设备" clearable size="small">
                         <el-option
                           v-for="item in wsList"
                           :key="item.eqId"
                           :label="item.eqName"
                           :value="item.eqId"
                         />
                       </el-select>
                     <el-date-picker
                       v-model="wstime"
                       size="small"
                       style="width: 380px"
                       type="daterange"
                       value-format="yyyy-MM-dd HH:mm:ss"
                       range-separator="-"
                       unlink-panels
                       start-placeholder="开始日期"
                       end-placeholder="结束日期"
                     ></el-date-picker>
                    <el-button type="primary" @click="wsTimeDuanSubmit()">确定</el-button>
                  </div>
                </div>
                <div class="card-line"></div>
                <div class="chart-wrapper" style="height: calc(100% - 50px);">
                  <wsline-chart v-bind:dayData="wsTimeData"/>
                </div>
              </div>
          </el-col>
        </el-row>
    </div>
</template>

<script>
import { listTunnels  } from "@/api/equipment/tunnel/api.js";
import {getSensor, listSensor} from '@/api/system/sensor/api.js'
import { sensorListDevices } from "@/api/equipment/eqlist/api";
import ColineChart from '../../tunnel/sensor/ColineChart';
import VilineChart from '../../tunnel/sensor/VilineChart';
import WslineChart from '../../tunnel/sensor/WslineChart';
export default {
  name: "sensor",

  components: {
       ColineChart,VilineChart,WslineChart
    },

  data() {
    return {
      //查询传感器设备的参数
      queryCoParams:{
        eqTunnelId:null,
        eqType: '14',
      },
      queryViParams:{
        eqTunnelId:null,
        eqType: '15',
      },
      queryWsParams:{
        eqTunnelId:null,
        eqType: '16',
      },
      //coform
      coForm:{
        tunnelId:null,
        eqId:'',
        eqname:''
      },
      viForm:{
        tunnelId:null,
        eqId: '',
        eqname:''
      },
      wsForm:{ //风速form
        tunnelId:null,
        eqId: '',
        eqname:''
      },
       tunnelList:[],//隧道下拉
       coList:[],
       viList:[],
       wsList:[],
       coTimeData:[],//时间段co趋势数据
       viTimeData:[],//时间段vi趋势数据
       wsTimeData:[],//时间段风速趋势数据
       coTimePickerDay:[],//co时间控件model对象
       vitime:[],//vi时间控件model对象
       wstime:[],//风速时间控件model对象
    }
  },
  created() {
    this.getTunnelList();
  },
  methods: {
    // 获取隧道
    getTunnelList(){
      listTunnels().then(response => {
        this.tunnelList = response.rows
        this.coForm.tunnelId = this.tunnelList[0].tunnelId
        this.getCoList()
        this.getViList()
        this.getWsList()
      });
    },
    getAnalysisData(){
      this.getCoList()
      this.getViList()
      this.getWsList()
    },
    getCoList(){
      this.queryCoParams.eqTunnelId = this.coForm.tunnelId
      sensorListDevices(this.queryCoParams).then(response => {
        if(response.data!=null){
          this.coList = response.data
          this.coForm.eqname = this.coList[0].eqName
          this.coForm.eqId = this.coList[0].eqId
        }
        /* this.coTimeDuanSubmit(); */
        this.coDefaultTime();
      });
    },
    getViList(){
      this.queryViParams.eqTunnelId = this.coForm.tunnelId
      sensorListDevices(this.queryViParams).then(response => {
        if(response.data!=null){
          this.viList = response.data
          this.viForm.eqname = this.viList[0].eqName
          this.viForm.eqId = this.viList[0].eqId
        }
        /* this.viTimeDuanSubmit(); */
        this.viDefaultTime();
      });
    },
    getWsList(){
      this.queryWsParams.eqTunnelId = this.coForm.tunnelId
      sensorListDevices(this.queryWsParams).then(response => {
        if(response.data!=null){
          this.wsList = response.data
          this.wsForm.eqname = this.wsList[0].eqName
          this.wsForm.eqId = this.wsList[0].eqId
        }
        /* this.wsTimeDuanSubmit(); */
        this.wsDefaultTime();
      });
    },
    coDefaultTime(){
      var time = []
      var date = new Date()
      var endTime = this.format(date)
      var startTime = this.getWeek(date)
      time.push(startTime)
      time.push(endTime)
      var json = {
        eqId:this.coForm.eqId
      }
      getSensor(this.addDateRange(json, time)).then(response => {
        if(response.code == 200){
          this.coTimeData = response.data
        }
      });
    },
    viDefaultTime(){
      var time = []
      var date = new Date()
      var endTime = this.format(date)
      var startTime = this.getWeek(date)
      time.push(startTime)
      time.push(endTime)
      var json = {
        eqId:this.viForm.eqId
      }
      getSensor(this.addDateRange(json, time)).then(response => {
        if(response.code == 200){
          this.viTimeData = response.data
        }
      });
    },
    wsDefaultTime(){
      var time = []
      var date = new Date()
      var endTime = this.format(date)
      var startTime = this.getWeek(date)
      time.push(startTime)
      time.push(endTime)
      var json = {
        eqId:this.wsForm.eqId
      }
      getSensor(this.addDateRange(json, time)).then(response => {
        if(response.code == 200){
          this.wsTimeData = response.data
        }
      });
    },
    //查询co趋势图
    coTimeDuanSubmit(){
      var dateRange = []
      dateRange = this.coTimePickerDay
      var json = {
        eqId:this.coForm.eqId
      }
      getSensor(this.addDateRange(json, dateRange)).then(response => {
        if(response.code == 200){
          this.coTimeData = response.data
        }
      });
    },
    //查询vi趋势图
    viTimeDuanSubmit(){
      var dateRange = []
      dateRange = this.vitime
      var json = {
        eqId:this.viForm.eqId
      }
      getSensor(this.addDateRange(json, dateRange)).then(response => {
        if(response.code == 200){
          this.viTimeData = response.data
        }
      });
    },
    //查询风速趋势图
    wsTimeDuanSubmit(){
      var dateRange = []
      dateRange = this.wstime
      var json = {
        eqId:this.wsForm.eqId
      }
      getSensor(this.addDateRange(json, dateRange)).then(response => {
        if(response.code == 200){
          this.wsTimeData = response.data
        }
      });
    },
    format (date) {
      var month = date.getMonth() + 1
      var strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      var currentDate = date.getFullYear() + '-' + month + '-' + strDate + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds()
      return currentDate
    },
    //获取三天前的日期
    getWeek(date) {
      var targetday_milliseconds = date.getTime() - 1000 * 60 * 60 * 24 * 7;
      date.setTime(targetday_milliseconds);
      var tMonth = date.getMonth();
      var tDate = date.getDate();
      tMonth = this.doHandleMonth(tMonth + 1);
      tDate = this.doHandleMonth(tDate);
      return date.getFullYear() + '-' + tMonth + '-' + tDate + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
    },
    doHandleMonth(month) {
      var m = month;
      if (month.toString().length == 1) {
        m = "0" + month;
      }
      return m;
    },
  },
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
    height: 350px;
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
