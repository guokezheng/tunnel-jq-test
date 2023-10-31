<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属隧道">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small" @change="getEquId">
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="设备Id">
          <el-select v-model="queryParams.equipmentId" placeholder="请选择设备" clearable size="small">
            <el-option
              v-for="item in equipmentData"
              :key="item.eqId"
              :label="item.eqId"
              :value="item.eqId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.alarmStatus" placeholder="请选择设备" clearable size="small">
            <el-option
              v-for="item in alarmStatusList"
              :key="item.alarmStatusId"
              :label="item.alarmStatusName"
              :value="item.alarmStatusId"
            />
          </el-select>
        </el-form-item>
       <el-form-item label="统计时间" prop="forecastTime">
         <el-date-picker
           v-model="forecastTime"
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
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    
      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    
      <el-table v-loading="loading" :data="statisticsList" :default-sort = "{prop: 'createTime', order: 'descending'}" @selection-change="handleSelectionChange">
        <el-table-column label="设备ID" align="center" prop="equipmentId" />
        <el-table-column label="所属隧道" align="center" prop="tunnelName" />
        <el-table-column label="耗电量(kW·h)" align="center" prop="powerConsumption" >
          <template slot-scope="scope">
                <div :style="scope.row.powerConsumption >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.powerConsumption }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="碳排放量(千吨)" align="center" prop="carbonEmission" >
          <template slot-scope="scope">
                <div :style="scope.row.carbonEmission >= '5' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.carbonEmission }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="电流(A)" align="center" prop="electricCurrent" >
          <template slot-scope="scope">
                <div :style="scope.row.electricCurrent >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.electricCurrent }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="电压(V)" align="center" prop="voltage" >
          <template slot-scope="scope">
                <div :style="scope.row.voltage >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.voltage }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="功率(P)" align="center" prop="power" >
          <template slot-scope="scope">
                <div :style="scope.row.power >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.power }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="功率因数(W)" align="center" prop="powerFactor" >
          <template slot-scope="scope">
                <div :style="scope.row.powerFactor >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.powerFactor }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="电能(kW·h)" align="center" prop="electricEnergy" >
          <template slot-scope="scope">
                <div :style="scope.row.electricEnergy >= '50' ? 'color: #ff0000'  : 'color: #000'">
                  {{ scope.row.electricEnergy }}
                </div>
          </template>
        </el-table-column>
        <el-table-column label="采集类型" align="center" prop="equipmentType" />
      <el-table-column label="采集时间" align="center" prop="createTime" sortable />
      <el-table-column label="状态" align="center" prop="alarmStatus" />
      
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
  import { listTunnels } from "@/api/equipment/tunnel/api";
  import moment from "moment";
  export default {
    name:'energyConsumptionStatistics',
    data(){
      return{
        // 遮罩层
        loading: false,
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
        // 统计表格
        statisticsList:[
          {
            equipmentId:'JiNanCompany-Huanghe-002-DL-010',
            carbonEmission:'3.27',
            tunnelName:'xxx隧道',
            powerConsumption:'60',
            electricCurrent:'60',
            voltage:'40',
            power:'40',
            powerFactor:'40',
            electricEnergy:'40',
            equipmentType:'照明设备',
            alarmStatus:'报警',
            createTime:'2022-03-08 09:13:00'
          },
          {
            equipmentId:'JiNanCompany-Huanghe-002-DL-010',
            carbonEmission:'5.88',
            tunnelName:'xxx隧道',
            powerConsumption:'40',
            electricCurrent:'40',
            voltage:'40',
            power:'40',
            powerFactor:'40',
            electricEnergy:'40',
            equipmentType:'照明设备',
            alarmStatus:'正常',
            createTime:'2022-03-08 09:13:00'
          },
        ],
        alarmStatusList:[
          {
            alarmStatusId:1,
            alarmStatusName:'报警',
          },
          {
            alarmStatusId:2,
            alarmStatusName:'正常',
          },
        ],
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          tunnelId: null,
          state: null,
          equipmentId:''
        },
        // 统计时间
        forecastTime:[],
        // 设备id 下拉框
        equipmentData:[],
        // 所属隧道
        tunnelData:[],
        // 两天前的日期
        moment:'',
        pickerOptions:{
          onPick: (obj) => {
            this.pickerMinDate = new Date(obj.minDate).getTime();
            if(obj.maxDate && obj.minDate){
              if(Date.parse(obj.minDate) == Date.parse(obj.maxDate)){
                this.$modal.msgWarning('不要选择同一天,请重新选择')
                return
              }
            }
          },
          disabledDate:(time)=> {
              if (this.pickerMinDate) {
                  const day1 =  366 * 24 * 3600 * 1000
                  let maxTime = this.pickerMinDate + day1
                  let minTime = this.pickerMinDate - day1
                  return time.getTime() > maxTime || time.getTime()<minTime || time.getTime() > Date.now()-1 * 24 * 3600 * 1000
              }else{
                  return time.getTime() > Date.now()-1 * 24 * 3600 * 1000
              }
              
          },
        }
      }
    },
    created() {
      // 默认搜索前两天数据
      this.getTime()
      // 获取隧道列表
      this.getTunnel()
    },
    methods:{
      getTime(){
          this.forecastTime = []
          let times = moment(new Date()).format("YYYY-MM-DD")
          let yesTime = Date.parse(new Date())-172800000
          let yesTimes = moment(new Date(yesTime)).format("YYYY-MM-DD")
         
          this.queryParams.params = {};
          this.queryParams.params["beginChangeTime"] = yesTimes
          this.forecastTime.push(yesTimes)
         
          this.queryParams.params["endChangeTime"] = times
          this.forecastTime.push(times)
         
          // this.getList()
      },
      /** 所属隧道 */
      getTunnel() {
        listTunnels().then(response => {
          this.tunnelData = response.rows;
        });
      },
      // 选中隧道后 获取该隧道内的设备id
      getEquId(data){
          this.equipmentData = []
          this.queryParams.equipmentId = ''
          // const params = {
          //     eqType:111,
          //     eqDirection:0,
          //     eqTunnelId: data
          // }
          // listDevices(params).then((response) => {
          //   this.equipmentData = response.rows
          // });
      },
      /** 查询列表 */
      getList() {
        this.queryParams.pageNum = 1;
        // this.loading = true;
        this.queryParams.params = {};
        if (null != this.forecastTime && "" != this.forecastTime) {
          this.queryParams.params["beginChangeTime"] =
            this.forecastTime[0];
          this.queryParams.params["endChangeTime"] = this.forecastTime[1];
        }
        record(this.queryParams).then(response => {
          this.xfpipelineList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      handleMeasures(row){
        
      },
      /** 重置按钮操作 */
      resetQuery() {
          this.queryParams= {
            pageNum: 1,
            pageSize: 10,
            tunnelId: null,
            state:null,
            equipmentId:''
          },
          this.getTime()
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
    }
  }
</script>

<style>
</style>
