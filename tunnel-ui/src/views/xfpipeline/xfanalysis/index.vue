<template>
  <div class="app-container">
	<el-row :gutter="10">
		<el-card class="box-card card-4col">
				<el-row>
					<span>消防压力预警</span>
				</el-row>
				<el-row class="big-number">
					{{earlyWarning}} 次
				</el-row>
				<el-row>
					<div style="width: 100%; height: 20px;"></div>
				</el-row>
			<!-- <el-row class="over-style">
				<el-col :span="12">
					周同比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					2%
				</el-col>
				<el-col :span="12">
					日环比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					3%
				</el-col>
			</el-row> -->
		</el-card>
		<el-card class="box-card card-4col">
				<el-row>
					<span>消防压力表设备总数</span>
				</el-row>
				<el-row class="big-number">
					{{allMessage}} 个
				</el-row>
				<el-row>
					<div style="width: 100%; height: 20px;"></div>
				</el-row>
			<!-- <el-row class="over-style">
				<el-col :span="12">
					周同比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					2%
				</el-col>
				<el-col :span="12">
					日环比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					3%
				</el-col>
			</el-row> -->
		</el-card>
		<el-card class="box-card card-4col">
				<el-row>
					<span>消防压力表离线总数</span>
				</el-row>
				<el-row class="big-number">
					{{offLine}} 个
				</el-row>
				<el-row>
					<div style="width: 100%; height: 20px;"></div>
				</el-row>
			<!-- <el-row class="over-style">
				<el-col :span="12">
					周同比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					2%
				</el-col>
				<el-col :span="12">
					日环比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					3%
				</el-col>
			</el-row> -->
		</el-card>
		<el-card class="box-card card-4col">
				<el-row>
					<span>压力表上报次数</span>
				</el-row>
				<el-row class="big-number">
					{{sumallreport}} 次
				</el-row>
				<el-row>
					<div style="width: 100%; height: 20px;"></div>
				</el-row>
			<!-- <el-row class="over-style">
				<el-col :span="12">
					周同比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					2%
				</el-col>
				<el-col :span="12">
					日环比
					<i class="el-icon-caret-bottom"
						style="color: #00aa00;"></i>
					3%
				</el-col>
			</el-row> -->
		</el-card>
	</el-row>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" style="margin-top: 25px;">
	  <el-form-item label="统计类型" style="margin-right: 20px;">
	    <el-radio-group size="small" v-model="queryParams.statisticalType" @change="typeChangeFun">
	    	<el-radio-button label="1">预警</el-radio-button>
	    	<!-- <el-radio-button label="2">离线</el-radio-button> -->
			<el-radio-button label="3">压力</el-radio-button>
	    </el-radio-group>
	  </el-form-item>
	  <el-form-item label="所属隧道">
	    <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道"  size="small" @change="getEquId">
	      <el-option
	        v-for="item in tunnelData"
	        :key="item.tunnelId"
	        :label="item.tunnelName"
	        :value="item.tunnelId"
	      />
	    </el-select>
	  </el-form-item>
	  <el-form-item label="设备名称" v-show="queryParams.statisticalType == '3'" >
	    <el-select v-model="queryParams.equipmentId" placeholder="请选择设备"  size="small" @change="handleChange">
	      <el-option
	        v-for="item in equipmentData"
	        :key="item.eqId"
	        :label="item.eqName"
	        :value="item.eqId"
	      />
	    </el-select>
	  </el-form-item>
	  <el-form-item label="日期选择" v-show="queryParams.statisticalType == '3'">
          <el-date-picker
            v-model="daterange"
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
	  </el-form-item>
      <el-form-item label="日期选择" v-show="queryParams.statisticalType == '1'">
        <el-radio-group v-model="pickerType" style="margin-right: 15px" @change="changeDate()">
          <el-radio-button label="year">年</el-radio-button>
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="date">日</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-show="queryParams.statisticalType == '1'">
            <el-date-picker v-if="pickerType == 'date'" type="date" size="small" v-model="dateTime"
            	placeholder="选择日期"  @change="changeDate" key='day' :picker-options="pickerOptions"> 
            </el-date-picker>
            <el-date-picker v-if="pickerType == 'month'" type="month" size="small" v-model="monthTime"
            	placeholder="选择月"  @change="changeDate" key='month'>
            </el-date-picker>
            
            <el-date-picker v-if="pickerType == 'year'" type="year" size="small" v-model="yearTime"
            	placeholder="选择年" @change="changeDate" key='year'>
            </el-date-picker>
            
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
	<el-row :gutter="10" >
		<el-col :span="12">
			<div id="alarmChartL" style="width: 100%; height: 250px;"></div>
		</el-col>
		<el-col :span="12">
			<div id="alarmChartR" style="width: 100%; height: 250px;"></div>
		</el-col>
	</el-row>
  <!-- <div id="alarmChart" style="width: 80%; height: 250px;margin: 0 auto;" v-if="queryParams.statisticalType == '3'"></div> -->
	<el-table v-loading="loading" :default-sort = "{prop: 'state', order: 'descending'}" 
            style="margin-top: 10px;"  :data="earlyWarningData" max-height="300"
            v-show="queryParams.statisticalType == '1'">
	  <el-table-column label="所属隧道" align="center" prop="tunnel_name" />
	  <el-table-column label="方向" align="center" prop="direction" >上行</el-table-column>
	  <el-table-column label="报警次数" align="center" prop="num" >
      <template slot-scope="scope">
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'year'">{{scope.row.num}}</div>
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'month'">{{scope.row.sum_flow_num}}</div>
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'date'">{{scope.row.num}}</div>
      </template>
    </el-table-column>
	  <el-table-column label="报警时间" align="center" prop="date" sortable>
      <template slot-scope="scope">
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'year'">{{scope.row.date}}</div>
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'month'">{{scope.row.curr_date}}</div>
        <div v-show="queryParams.statisticalType == '1' && pickerType == 'date'">{{scope.row.order_hour}}:00</div>
      </template>
    </el-table-column>
	</el-table>
   
	<el-table v-loading="loading" :default-sort = "{prop: 'state', order: 'descending'}" 
            style="margin-top: 10px;" v-show="queryParams.statisticalType == '2'">
	  <el-table-column label="所属隧道" align="center" prop="tunnelName" />
	  <el-table-column label="方向" align="center" prop="direction" />
	  <el-table-column label="离线次数" align="center" prop="pressure" />
	  <el-table-column label="报警时间" align="center" prop="state" sortable></el-table-column>
	</el-table>
	<el-table v-loading="loading" :default-sort = "{prop: 'create_time', order: 'descending'}" 
            style="margin-top: 10px;" v-show="queryParams.statisticalType == '3'" :data="pressureData">
	  <el-table-column label="所属隧道" align="center" prop="tunnel_name" />
	  <el-table-column label="方向" align="center" prop="eq_direction" >
          <template slot-scope="scope">
              <div>
                  {{formatDirect(scope.row.eq_direction)}}
              </div>
          </template>
      </el-table-column>
	  <el-table-column label="设备名称" align="center" prop="equipment_name" />
	  <el-table-column label="压力值" align="center" prop="analog_quantity">
          <template slot-scope="scope">
              <div>{{scope.row.analog_quantity}}Mp</div>
          </template>
      </el-table-column>
	  <el-table-column label="采集时间" align="center" prop="create_time" sortable>
          <template slot-scope="scope">
              <div>{{changeTime(scope.row.create_time)}}</div>
          </template>
      </el-table-column>
	</el-table>
  <pagination
    v-show="total>0"
    :total="total"
    :page.sync="queryParams.pageNum"
    :limit.sync="queryParams.pageSize"
    @pagination="handleQuery"
  />
  </div>
</template>

<script>
import *as echarts from 'echarts'
import { getPressureGauges, allMessage, earlyWarning, offLine, frequencyAll, listXfpipeline, getXfpipeline, delXfpipeline, addXfpipeline, updateXfpipeline, exportXfpipeline } from "@/api/xfpipeline/xfpipeline";
import { listTunnels } from "@/api/equipment/tunnel/api";
import {listDevices} from "@/api/equipment/eqlist/api";
	
export default {
  name: "Xfanalysis",
  components: {
  },
  data() {
    return {
      total: 0, // 总条数
        // 压力 表格
        pressureData:[],
        // 预警 表格
        earlyWarningData:[],
        // 获取所有压力表一天上报次数
        sumallreport:'',
        // 离线总数
        offLine:'',
        // 预警设备总数
        earlyWarning:'',
        // 所有压力表总数
        allMessage:'',
      // 遮罩层
      loading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        statisticalType:'3',
        tunnelId: null,
        // state: null,
        // pickerDate:"",
        params:{}
      },
      pickerType: "year",
	  // 所属隧道
	  tunnelData:{},
      // 设备名称
      equipmentData:[],
      // 时间选择 数组
      daterange:[],
      dateTime:new Date(),
      monthTime: new Date(),
      yearTime: new Date(),
      dateTimes: new Date(),
      // 限制时间段不能选今天以后的
      pickerOptions:{
        onPick: (obj) => {
          console.log(obj)
            
            // if(obj.maxDate && obj.minDate){
            //   if(Date.parse(obj.minDate) == Date.parse(obj.maxDate)){
            //     this.$modal.msgWarning('不要选择同一天,请重新选择')
            //     return
            //   }
            // }
        },
        disabledDate:(time)=> {
          return time.getTime() >= Date.now()+0 * 24 * 3600 * 1000
          
        },
      },
    }
  },
  created(){
	// 获取表格信息
    // this.getList()
	// 获取隧道列表
	this.getTunnel()
    // 获取所有压力表一天上报次数
    this.getFrequency()
    // 获取所有状态数据
    // this.handleQuery()
  },
  mounted() {
  
  },
  methods: {
	  /** 所属隧道 */
	  getTunnel() {
	    listTunnels().then(response => {
	      this.tunnelData = response.rows;
          this.queryParams.tunnelId = response.rows[0].tunnelId
          this.handleQuery()
          const params = {
              eqType:111,
              eqDirection:0,
              eqTunnelId: this.queryParams.tunnelId
          }
          listDevices(params).then((response) => {
            console.log(response.rows,"设备Id")
            if(response.rows.length>0){
              this.equipmentData = response.rows
              this.queryParams.equipmentId = response.rows[0].eqId
            }
            
          });
         
	    });
	  },
      // 获取压力表当天四个数据
      getFrequency(){
          // 所有压力表
        frequencyAll().then(response => {
            this.sumallreport = response.data[0].sumallreport
	    });
        // 指定压力表
        offLine().then(response =>{
            this.offLine = response.data
        })
        earlyWarning().then(response =>{
            this.earlyWarning = response.data.length
        })
        allMessage().then(response =>{
            this.allMessage = response.data.length
        })
      },
      // 选中隧道后 获取该隧道内的压力表id
      getEquId(data){
          this.equipmentData = []
          this.queryParams.equipmentId = ''
          const params = {
              eqType:111,
              eqDirection:0,
              eqTunnelId: data
          }
          listDevices(params).then((response) => {
            console.log(response.rows,"设备ID")
            this.equipmentData = response.rows
          });
      },
      // 选择时间 年月日
      changeDate(){
              if (this.pickerType == 'date') {
              	this.dateTimes = this.dateTime;
              } else if (this.pickerType == 'month') {
              	this.dateTimes = this.monthTime;
              } else if (this.pickerType == 'year') {
              	this.dateTimes = this.yearTime
              } 
          this.handleQuery()
      },
     
      formatDirect(direction){
          if(direction == "0"){
              return "上行"
          }else{
              return "下行"
          }
      },
      // 手动刷新
      handleChange() {
          this.$forceUpdate()
      },
	  getDuration(type,start,stop){
	      var arr = [];
	      var current = new Date(start);
	      stop  = new Date(stop);
	      while (current <= stop) {
			  var date = new Date (current)
			  
	          arr.push( date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate() );
	          if(type == 'hour'){//小时
	               current.setHours(current.getHours() + 1);
	          }else if(type == 'day'){//天
	              current.setDate(current.getDate() + 1);
	          }else if(type == 'week'){//周
	              current.setDate(current.getDate() + 7);
	          }else if(type == 'month'){//月
	              current.setMonth(current.getMonth() + 1);
	          }else{//默认天
	              current.setDate(current.getDate() + 1);
	          }
	      }
	      return arr;
	  },
	  /* 查询 */
	  handleQuery(){
        this.queryParams.params = {};
        // 预警
        if(this.queryParams.statisticalType == '1'){
            this.queryParams.params[this.pickerType] = this.dateTimes
        }else{
            // 压力
            if (null != this.daterange && "" != this.daterange) {
              if(this.daterange[0] == this.daterange[1]){
                alert(this.daterange[0])
                this.queryParams.params["beginChangeTime"] = this.daterange[0] + " 00:00:00";
                this.queryParams.params["endChangeTime"] = this.daterange[1] + " 23:59:59";
              }else{
                this.queryParams.params["beginChangeTime"] = this.daterange[0] + " 00:00:00";
                this.queryParams.params["endChangeTime"] = this.daterange[1] + " 23:59:59";
              }
            }else if(this.daterange.length == 0){
                let n = 7;
                let d = '';
                d = new Date();
                let year = d.getFullYear();
                let mon = d.getMonth() + 1;
                let day = d.getDate();
                if(day <= n) {
                    if(mon > 1) {
                        mon = mon - 1;
                    } else {
                        year = year - 1;
                        mon = 12;
                    }
                }
                d.setDate(d.getDate() - n);
                year = d.getFullYear();
                mon = d.getMonth() + 1;
                day = d.getDate();
               
                let s = year + "-" + (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day) ;
                this.queryParams.params["beginChangeTime"] = s
                this.daterange.push(s)
                var t = this.appendZero(new Date().getFullYear()) + "-" + this.appendZero((new Date().getMonth() + 1)) + "-" + this.appendZero(new Date().getDate())
                this.queryParams.params["endChangeTime"] = t
                this.daterange.push(t)
            }
        }
		// var a = this.queryParams
        getPressureGauges(this.queryParams).then(response =>{
          console.log(response,"response")
            var params = {
                xData1:[],
                yData1:[],
                eq_direction1:'',
                xData2:[],
                yData2:[],
                eq_direction2:'',
            }
            if(this.queryParams.statisticalType == '3'){
				// 判断当前设备所属上行还是下行
                this.pressureData = response.data
                this.total = response.data.length || 0
                params.eq_direction1 = '上行'
                params.eq_direction2 = '下行'
                if(response.data.length>0){
                  for(var item of response.data){
                    if(item.create_time.indexOf('T')){
                      var items = item.create_time.split("T");
                      params.xData1.push("  "+items[0].slice(5,10)+" "+items[1])
                      params.yData1.push(item.analog_quantity)
                      params.xData2.push("  "+items[0].slice(5,10)+" "+items[1])
                      params.yData2.push(0)
                    }
                      
                  }
                }
                else if(response.data.length == 0){
                // 改为时间范围内的数据
                var arr = this.getDuration('day',this.queryParams.params["beginChangeTime"],this.queryParams.params["endChangeTime"])
                      for(var i = 0;i<arr.length;i++){
                          params.xData1.push(arr[i])
                          params.xData2.push(arr[i])
                          params.yData1.push(0)
                          params.yData2.push(0)
                      }
                    
                  }
                  
              }else{
                  // 处理数据给echarts
                    params.eq_direction1 = '上行'
                    params.eq_direction2 = '下行'
                    if(this.pickerType == 'year'){
                        this.earlyWarningData = response.data[0].year
                        this.total = response.data[0].year.length || 0
                        if(response.data[0].year.length>0){
                          for(var y of response.data[0].year){
                              params.xData1.push(y.date)
                              params.yData1.push(y.num)
                          }
                          if(!response.data[1]){
                            for(var y of response.data[0].year){
                                params.xData2.push(y.date)
                                params.yData2 = [0,0,0,0,0,0,0,0,0,0,0,0]
                            }
                          }
                        }else if(response.data[0].year.length == 0){
                          params.xData1 = [1,2,3,4,5,6,7,8,9,10,11,12]
                          params.yData1 = [0,0,0,0,0,0,0,0,0,0,0,0]
                          params.xData2 = [1,2,3,4,5,6,7,8,9,10,11,12]
                          params.yData2 = [0,0,0,0,0,0,0,0,0,0,0,0]
                        }
                        
                    }else if(this.pickerType == 'month'){
                        this.earlyWarningData = response.data[0].month
                        this.total = response.data[0].month.length || 0
                        for(var n=1;n<=31;n++){
                          params.xData1.push(n)
                          params.xData2.push(n)
                        }
                        params.yData1 = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                        params.yData2 = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                        debugger
                        if(response.data[0] && response.data[0].month.length>0){
                          params.xData1 = []
                          params.yData1 = []
                          for(var m of response.data[0].month){
                            params.xData1.push(m.curr_date.substr(m.curr_date.length-2))
                            params.yData1.push(m.sum_flow_num)
                          }
                        }else if(response.data[1] && response.data[1].month.length>0){
                          for(var m of response.data[1].month){
                            params.xData2 = []
                            params.yData2 = []
                            params.xData2.push(m.curr_date.substr(m.curr_date.length-2))
                            params.yData2.push(m.sum_flow_num)
                          }
                        }
                    }else if(this.pickerType == 'date'){
                        this.earlyWarningData = response.data[0].date
                        this.total = response.data[0].date.length || 0
                        for(var n=0;n<=23;n++){
                          params.xData1.push(n)
                          params.xData2.push(n)
                        }
                        params.yData1 = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                        params.yData2 = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                        if(response.data[0] && response.data[0].date.length>0){
                          params.xData1 = []
                          params.yData1 = []
                          for(var m of response.data[0].date){
                              params.xData1.push(m.order_hour)
                              params.yData1.push(m.num)
                          }
                        }else if(response.data[1] && response.data[1].date.length>0){
                          params.xData2 = []
                          params.yData2 = []
                          for(var m of response.data[1].date){
                              params.xData2.push(m.order_hour)
                              params.yData2.push(m.num)
                          }
                        }
                    }
              }
              console.log(params,"params")
            this.getAlarmEchart(params)
        })
	  },
      // 补0
      appendZero(obj) {
          if (obj < 10) {
              return "0" + obj;
          } else {
              return obj;
          }
      },
      // 修改采集时间格式 去 T
      changeTime(str){
          if(str){
              if(str.indexOf('T') != -1){
                  return str.replace('T',"  ")
              }
          }
      },
	  /* 重置 */
	  resetQuery(){
      this.getTunnel()
		  this.queryParams.statisticalType = '3'
      // this.queryParams.equipmentId =''
      // this.queryParams.tunnelId = this.tunnelData[0].tunnelId
      this.pickerType = "year"
      this.daterange = []
      // this.equipmentData = []
      this.queryParams.params = {};
      this.handleQuery()
	  },
	  /* 类型改变方法 */
	  typeChangeFun(){
          this.queryParams.tunnelId = ''
          this.getTunnel()
          // this.handleQuery()
	  },
	  /* 预警图 */
	  getAlarmEchart(params){
		  this.alarmEchartL = echarts.init(document.getElementById('alarmChartL'));
		  this.alarmEchartR = echarts.init(document.getElementById('alarmChartR'));
		  this.alarmEchartL.clear()
		  this.alarmEchartR.clear()
		  var XName1= params.xData1
      var XName2= params.xData2
      var xAxisName = ''
      if(this.queryParams.statisticalType == 1) {
        xAxisName = this.pickerType == 'year' ? '月份' : this.pickerType == 'month' ? '日期' : '时刻'
      } else {
        xAxisName = '日期'
      }
		  var statisticalType = this.queryParams.statisticalType
		  var data1 = []
      var data2 = []
		  var Line = []
		  if(statisticalType == '1'){
			data1 = params.yData1
      data2 = params.yData2
			Line = ["预警信息数"]
		  }else if(statisticalType == '2'){
			 data1 = [
			             [123,154, 234, 321,120,390, 634]
			         ]
			 Line = ["设备离线"]
		  }else if(statisticalType == '3'){
			  data1 = params.yData1
        data2 = params.yData2
        // data1 = [1.2,1.5,1.4,1.6,1.2,1.7,1.3,1.5,1.3]
        // data2 = [1.1,1.2,1.3,1.5,1.3,1.3,1.0,1.2,1.4]
			  Line = ["压力值"]
		  }
		  var img = [
		              'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABRCAYAAABFTSEIAAAACXBIWXMAAAsSAAALEgHS3X78AAAEp0lEQVR42u3cz4sjRRTA8W9Vd3Vn8mMmjj9WQWSRZQ+CsH+B7MnDIgiCd0E8CYJ/gOAIelo8ehUP/gF6WLw5/gMueFP2sIcF0dHd2Z1kknR11fOQZJJJMtlZd03H7HtQpNOTnpn+8Lrm1etmjIig8e/DKoECKqACKqCGAiqgAiqghgIqoAIqoIYCKqACKqCGAiqgAiqghgIqoAJudKTr+osZMNPvBUQBHwHsPF9fB9R0DeHMOQ6T6WOrhEzXBM4swDOL0M6CrArRVoq3t2dGUIb9fTvatg8ZZup1PDBgzPmy98mey6qfzjLz2WaWjEUZKEvGyi9nWyneMOvGIyFQo2Sbg4MUSChpU9IeTTUpJdsEajPZOJeJG5uBZj7rLLduWS5dGm6XNLEELOFUFj54ACJCaychkpDSASK3bwsXL0YgVpWJKwM0iy9Zy8HdGru7jvt3Pbu7w0wES7drTwAbjTHMGCsQcIAnYTC1/wRx0wEnl27JNgZI8HQ6Kc1mQq83RNzaMjPzXqDbjTQaJRFLxIyyMSxAXEkWrhrQzAAmo5HOjCQf7jflILxOkohL+aUPgV4vEGNJo+E5PAy02+UIMEwBxo0CPDP7Dg5SnEtpt1PA0e87XO25FOoh8IYIH2Y5b45RzGAQBiIltZoHxqMcjbksXAVgdc2EQMYzzzdotyeZWKuleULXJtwT4SODfC2QCWR+IF9KnjuX1Xbo99Op7LVE8iXlz0YBTk5SyLEEjo5OLuccEoFUvHfO+reuUPx4zftXAIcx1hdcF+/TvFab4A0Bs0VwqyhpVnkJT89/Q4DDQ0e77YCMwIUsFMeFZD856699URRvX4nxE4A/jbnxXp7v4Zw3ReGNSDHI8wFQjIafuoyn58L/fB6sth/Ybg9fez2TRC6QZcZYvgHsazF+MP7YCyLXcM7gvSXLDGBqYDg+NhwdmSpPoTrAkub0W+f4FSB1fDucIunMHSLpO8WAH0rSy8u+19MBCHB4OHzd2pI+CEUhpigEiN+l6WcdY252jLn5s7Wf472ImPcN8pUl/tEHoV4XWq1Ke4KrLmPsTA3oODpytFoOyJKSyzHyMSIxteWngMW5cSEdDJQUhTdZVgxOz3/+jFJm4+bA2e5JpNU6WZ4Fw99JwnWMKccwpeddP+B7GZTNUPKqybJy0O+Hs1YfMz9swwvpB8fbGDG0GuGkkK7V0hxSmZQpABI8l2z0v3sJf50qpAMJCd2qCulql3LD1lRGQjm7lEsDz0rkxTQOfiPPxUBcuJTbbhss/Y1eyi3NwsmKInmkZsKk5gtPUzNhvp11507CSy/X6XYStpvFudpZw1ZWIOF4Cq6SdtbKbioJyAhRTu3u9yMJXerN+ugvaQQsjcZ8Q3VnZwxlSDhe1lB9GjrSw5b+1avT8+Jw+979nNaOI6U3KpTrWAosxVQmygK4ld8X0ZtK/7eViExD7O1NQPb3T7fsl4/4sBpwYzPwjFbTo95Yl9l9Vd1YN1X/147HebSjary1AHyc5qc+XLQEQx9ve8Kg6xr6hKoCKqACKqCGAiqgAiqghgIqoAIqoIYCKqACKqCGAiqgAiqghgIq4JrHP8fEWV8FMTmOAAAAAElFTkSuQmCC',
		              'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAE8AAABPCAYAAACqNJiGAAAACXBIWXMAAAsSAAALEgHS3X78AAAGS0lEQVR42u2cz4skSRXHPy8iMrOrq7qnp3dqloEeD0PvHrbxB/TJkwt6EGVBwRHUf0BPXj146JPgosJe/PEX6NoHYUUE8bCC11ZQtw+DLMq2DtPlbM9MVXVVZkbE85DVXdU97e6yi1U9TXwhyaIq4lXmh29ERrxXlKgqSR9OJiFI8BK8BC/BS0rwErwEL8FLSvASvAQvwUvwkhK8BC/BS/CSErwEL8FL8JISvI8udxkvShA5/55y+QrMchmK3hfBej9dBpgLhXcBNIGd9+ix03C7JBAXBm8GnEzBvDV53bvAid3JhW7pDGBdJMC5wzvnNoG7U2B7fWF7G/aPhJdmWu0DL11X9vZge0WnIHd11onzhrgoeDJ1Wk/gRYEjgYHA88LBUNiY6XQAbLQVHih0FK4r3JtAPHWizhueWYzrZsDtdw28Y6BtKJfbVHWbDSzvxg5la413Y4cNLFXdZtxepV4q4B3T9OtJE2fnQz94ngnnzYCTqeO6DbT7Dw1uyZBlHTreM3QBqacgNFPa3jJwjhg85fExt56LMIzQizMOnOscOO9F8tPgyv4ymVi6WExdMbJgbYZ1GSU51mVYmzGyYOqK9ViTiaXsL0PbNHFOHIhcuWF7drhCM8cNhLK/zBCLW7fQcqegqphjNMfRnKuYnwKl5XDrliETgIPJnDmNP6/hO+cdxonrEOgYCipGtcOWjqF3mJal9A6Lxahg7QZB1nB6RKX/pMg8w5FgnUCoKTIPHQNHOnHfU+vAKzJsd+SM6x48NpAb1jKDwVLmjljfJONFRL5CaX8A5tcQ7yHmAS2TIVVGmTsMlrWs6f/gsTnnPrmC8IA3e8L+UbMcydfbPBoaBlhELctqCTJAwwHoZ4BPA6/hydH4I8rwDSqzRaE3ELUMsDwaGvL1NjzfxH2zd7XmvDPzz8vQLH6HgpYekxnEGcZYZAJRnCPG7+L44nf4wgG5dcBfQL4M+hDlVtPeGUxm0NLDsFlUv/zR9suXP6vy94HQdkKx6pHjDBCWW4IPn0D5JF7/+Cn5WPx++OrPWpK/8Pnw8cFr/O7rv4p/fh1nKjL5D84JYSSIF1iuuf9EGHph86rm83bfusAJKyCFgBeCCvBNNB5/y3z2lRb5C80FSudLsv0KRIEolLFpL4XAygf8nmcd3t0tPTeeLQDHwBiAv2H0c2RmNJbqyWzTUuo+mVGi/B5YYzzpd6K8aP/P77lCi2TY7ExvTkeKlorWCkbBRdD4bfP6G//i0S8GjP/Uo/+bn8gf3gCNID8FbqL1pN+oiRVCdSbunLSYTHJYUkLfYzqOlo1UMYJuEilBfgjht1+LP34VcYJ6JWjEmYDYnxO1RiXSMpEQlNhXqqJexG383513dp/ZbTIivq3cuBaJdUR9JEog+vsQIvBLkC2c1kStMeZ7GPsqUe6g9S3iOBAlNP3qyI1rEd+eZFq6c01PzSUxME1D3RX23jZs3zQ8bK+y0oZR7bGFYzzKsLnDeIcYg9QGMoFaUXsLWCaaf+N9j6VWTSg9rczRH8JzwyfsHUa278STHN884M1zzmsyH9sryn5HWW2N6fvINQnEQSBkniLW5FKhsUU0N1G/SZCKyD/I5K/kHBIyTxwErkmg7yOrrTH7nSYuWzrP7dk8ncdZ990RDrAUWLq5AbX01WKwjKxh2U+XHMdOaYVIJLAiASTQqyIlgQ0Ce2/rrOvmNWzNfCx3eiMT992JcF0ZDxoANQ6fC6HwBF9TmIog06MwFcHXhMLjc6GkoCQwHjRxtu/EWddd1XxekzbaBbinbN6OjAeRLDsm9KEeelZXalZCjffTYyXUrK7U1ENP6IMxY8aDyObtCPe0ibdz9Z62F7rv7q6y21U4ijy+3WSEi+Mh3banHkI5dmheUC15qiXPuCyoh0K37SmOh2Tjsul3FNntNvEWUElbZPXs6SLQadVscMEWq6OnVbQLij/zBreQYXt2/ttRmHHhYW9SkxgF9g4jHMbmPArQm3w+cRu7JzWLhdVuL0PRm7NOPMk4n9fJnnXnqWzxwn41oKoLPVDkwmMHg2Im5wvbLPra5TL9u8UHSWBepl9LSfprkGdqnZfgJSV4CV6Cl+AleEkJXoKX4CV4SQlegpfgJXgJXlKCl+AleAleUoKX4CV4V0//BfBm5Ekg9qBkAAAAAElFTkSuQmCC',
		            ];
		  var color =['#ff0000','#55ff00'];
		 
		  var option1 = {
              tooltip: {
              	trigger: 'axis',
              },
              backgroundColor: '#0e2147',
              grid: {
                          left: '5%',
                          top: '25%',
                          bottom: '35%',
                          right: '5%',
                      },
              title: {
                  "text": params.eq_direction1,
                  x: "4%",
                  textStyle: {
                      color: '#fff',
                      fontSize: '18'
                  }
              },
               legend: {
                  type: "scroll",
                  data:Line,
                   itemWidth:18,
                   itemHeight:12,
                   textStyle: {
                      color:"#00ffff",
                       fontSize:14
                   },
               },
              yAxis: [
                  {
                      type: 'value',
                      position: 'right',
                  },
                  {
                      type: 'value',
                      name: statisticalType=='1'?'单位：件':'单位：Mp',
                      position: 'left',
                      nameTextStyle: {
                          color: '#00FFFF'
                      },
                      splitLine: {
                          lineStyle: {
                              type: 'dashed',
                              color: 'rgba(135,140,147,0.8)'
                          }
                      },
                      axisLabel: {
                          formatter: '{value}',
                          color: '#fff',
                          fontSize: 14
                      }
                  },
              ],
              xAxis: [
                  {
                      name: xAxisName,
                      type: 'category',
                      nameTextStyle: {
                        color: '#fff'
                      },
                      data: XName1,
                      axisLabel: {
                          inside: false,
                          textStyle: {
                              color: '#fff',// x轴颜色
                              fontWeight: 'normal',
                              fontSize: '14',
                              lineHeight: 22
                          },
                          show: true,
                         
                      }
                  },
              ],
              dataZoom: [
                {
                  show: true,
                  height: 20,
                  xAxisIndex: [0],
                  bottom: 30,
                  start: 10,
                  end: 80,
                  handleIcon:
                    "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
                  handleSize: "110%",
                  handleStyle: {
                    color: "#5B3AAE"
                  },
                  textStyle: {
                    color: "rgba(204,187,225,0.5)"
                  },
                  fillerColor: "rgba(67,55,160,0.4)",
                  borderColor: "rgba(204,187,225,0.5)"
                },
                {
                  type: "inside",
                  show: true,
                  height: 10,
                  start: 1,
                  end: 35
                }
              ],
              series:  {
                  symbolSize: 150,
                  symbol: img[0],
                  name: statisticalType=='1'?'预警信息数':'压力值',
                  type: "line",
                  yAxisIndex: 1,
                  data: data1 ,
                  itemStyle: {
                      normal: {
                          borderWidth: 5,
                          color: color[0],
                      }
                  }
              }          
		  }
      var option2 = {
          tooltip: {
          	trigger: 'axis',
          },
          backgroundColor: '#0e2147',
          grid: {
              left: '5%',
              top: '25%',
              bottom: '35%',
              right: '5%',
          },
          "title": {
              "text": params.eq_direction2,
              x: "4%",
              textStyle: {
                  color: '#fff',
                  fontSize: '18'
              }
          },
          legend: {
             type: "scroll",
             data:Line,
              itemWidth:18,
              itemHeight:12,
              textStyle: {
                 color:"#00ffff",
                  fontSize:14
              },
          },
          yAxis: [
              {
                  type: 'value',
                  position: 'right',
              },
              {
                  type: 'value',
                  name: statisticalType=='1'?'单位：件':'单位：Mp',
                  position: 'left',
                  nameTextStyle: {
                      color: '#00FFFF'
                  },
                  splitLine: {
                      lineStyle: {
                          type: 'dashed',
                          color: 'rgba(135,140,147,0.8)'
                      }
                  },
                  axisLabel: {
                      formatter: '{value}',
                      color: '#fff',
                      fontSize: 14
                  }
              },
          ],
          xAxis: [
              {
                  type: 'category',
                  name: xAxisName,
                  nameTextStyle: {
                    color: '#fff'
                  },
                  axisLabel: {
                      inside: false,
                      textStyle: {
                          color: '#fff',// x轴颜色
                          fontWeight: 'normal',
                          fontSize: '14',
                          lineHeight: 22
                      },
                      show: true,
                  },
                  data: XName2,
              },
          ],
          dataZoom: [
            {
              show: true,
              height: 20,
              xAxisIndex: [0],
              bottom: 30,
              start: 10,
              end: 80,
              handleIcon:
                "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
              handleSize: "110%",
              handleStyle: {
                color: "#5B3AAE"
              },
              textStyle: {
                color: "rgba(204,187,225,0.5)"
              },
              fillerColor: "rgba(67,55,160,0.4)",
              borderColor: "rgba(204,187,225,0.5)"
            },
            {
              type: "inside",
              show: true,
              height: 10,
              start: 1,
              end: 35
            }
          ],
          series:  {
              symbolSize: 150,
              symbol: img[0],
              name: statisticalType=='1'?'预警信息数':'压力值',
              type: "line",
              yAxisIndex: 1,
              data:  data2,
              itemStyle: {
                  normal: {
                      borderWidth: 5,
                      color: color[0],
                  }
              }
          }
             
      }
		  this.alarmEchartL.setOption(option1)
      this.alarmEchartR.setOption(option2)
	  },
      
	  
  }
};
</script>
<style scoped lang="scss">
	.card-4col {
		width: calc(100%/4 - 10px);
		float: left;
		margin: 0px 5px 10px 5px;
		border: 0px;
		color: #000000;
	}
	.big-number {
		font-size: 22px;
		font-weight: bold;
		padding: 10px 0px 10px 5px;
	}
	::v-deep .el-range-editor--small .el-range__close-icon{
	  display: none !important;
	}
  ::v-deep .el-input__suffix{
    display: none !important;
  }
</style>