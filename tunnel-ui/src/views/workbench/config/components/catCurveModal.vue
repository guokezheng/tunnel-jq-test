<template>
  <el-dialog
    class="timedControl"
    custom-class="no-scroll"
    title="车辆曲线图"
    :close-on-click-modal="false"
    :visible.sync="visibleSync"
    width="80%"
    destroy-on-close
    append-to-body
    :lock-scroll="true"
    :before-close="closeLogin"
  >
    <el-row :gutter="24" style="clear:both;">
        <el-col :span="3" style="float: right">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="submitCatForm"
          >保存</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="catHandleSave"
          >刷新</el-button
          >
        </el-col>
    </el-row>
    <el-row :gutter="24" style="clear:both; margin-top: 5px;">
      <el-col :span="13">

        <el-row :gutter="24" style="clear:both;">
          <el-col :span="24">
            <div  id='trend' class="chartTow" ref="chart1" style="width: 100%; height: 400px; float: left"></div>
          </el-col>

        </el-row>
        <el-row :gutter="24" style="clear:both;">
          <el-col :span="24">
            <div  id='trend' class="chartTow" ref="chart2" style="width: 100%; height: 400px; float: left"></div>
          </el-col>

        </el-row>
      </el-col>
      <el-col :span="11">
        <el-form
          ref="loginQueryForm"
          :model="catFilesModel"
          :inline="true"
          class="loginQueryFormClass"
          label-width="70px"
          height="300px"
        >
          <el-row :gutter="24" style="clear:both;">
            <el-col :span="6">
              <el-form-item label="状态" align="center" prop="schedulerTime"  class="el-form-item-data-type">
                <template slot-scope="scope">
                  <el-switch
                    v-model="catFilesModel.isStatus"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                  >
                  </el-switch>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="隧道名称" prop="tunnelId"  class="el-form-item-data">
                <el-select
                  style="width: 100%"
                  :disabled="tunnelDisabled"
                  v-model="catFilesModel.tunnelId"
                  placeholder="请选择隧道"
                  clearable
                  @change="catChangeEvent"
                >
                  <el-option
                    v-for="item in tunnelData"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="方向" prop="direction"  class="el-form-item-data">
                <el-select
                  clearable
                  :disabled="tunnelDisabled"
                  v-model="catFilesModel.direction"
                  placeholder="请选择隧道方向"
                  @change="catChangeEvent"
                  style="width: 100%"
                >
                  <el-option
                    v-for="dict in catDirectionOptions"
                    :key="dict.value"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24" style="clear:both;overflow: auto; width: 100%; height: 250px;">
            <el-col :span="24">
              <el-form-item label="时间段"  v-for="(item, index) in formItems" :key="index"   class="el-form-item-direction-Item">

                <el-time-picker
                  placeholder="选择开始时间"
                  v-model="item.startTime"
                  style="width: 25%"
                ></el-time-picker>

                <el-time-picker
                  placeholder="选择结束时间"
                  v-model="item.endTime"
                  style="width:25%;margin-left: 5px"
                ></el-time-picker>
                <span style="color: #05AAFD;margin-left: 10px;font-weight: bold;">下修比例</span>
                <el-input style="width: 15%;margin-left: 5px"  v-model="item.beforeLuminance"    @change="beforeLuminanceEventWei"></el-input>
                <div class="buttonBox" style="width: 15% ;float: right;" >
                  <el-button
                    class="delete"
                    @click="deleteHandleUpdate(index)"
                  ></el-button>
                  <el-button
                    class="add"
                    @click="addHandleUpdate(index)"
                  ></el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>

      <el-col :span="11">
        <el-row :gutter="24" style="clear:both;">
          <el-col :span="6" style="float: right">
            <el-button
              size="mini"
              class="tableBlueButtton"
              @click="submitCatFormWei"
            >保存</el-button
            >
            <el-button
              size="mini"
              class="tableBlueButtton"
              @click="catHandleSave"
            >刷新</el-button
            >
          </el-col>
        </el-row>
        <el-form
          ref="loginQueryForm"
          :model="catFilesModelWei"
          :inline="true"
          class="loginQueryFormClass"
          label-width="70px"
          style="margin-top: 5px"
          height="300px"
        >
          <el-row :gutter="24" style="clear:both;">
            <el-col :span="6">
              <el-form-item label="状态" align="center" prop="schedulerTime" class="el-form-item-data-type">
                <template slot-scope="scope">
                  <el-switch
                    v-model="catFilesModelWei.isStatus"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                  >
                  </el-switch>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="隧道名称" prop="tunnelId" class="el-form-item-data">
                <el-select
                  style="width: 100%"
                  :disabled="tunnelDisabled"
                  v-model="catFilesModelWei.tunnelId"
                  placeholder="请选择隧道"
                  clearable
                  @change="catChangeEventWei"
                >
                  <el-option
                    v-for="item in tunnelData"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="方向" prop="direction" class="el-form-item-data">
                <el-select
                  clearable
                  :disabled="tunnelDisabled"
                  v-model="catFilesModelWei.direction"
                  placeholder="请选择隧道方向"
                  @change="catChangeEventWei"
                  style="width: 100%"
                >
                  <el-option
                    v-for="dict in catDirectionOptions"
                    :key="dict.value"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24" style="clear:both;overflow: auto; width: 100%; height: 250px;">
            <el-col :span="24">
              <el-form-item label="时间段"  v-for="(item, index) in formItemsWei" :key="index" class="el-form-item-direction-Item">

                <el-time-picker
                  placeholder="选择开始时间"
                  v-model="item.startTime"
                  style="width: 25%"
                ></el-time-picker>

                <el-time-picker
                  placeholder="选择结束时间"
                  v-model="item.endTime"
                  style="width: 25%;margin-left: 5px"
                ></el-time-picker>
                <span style="color: #05AAFD;margin-left: 10px;font-weight: bold;">下修比例</span>
                <el-input style="width: 15%;margin-left: 5px" placeholder="请输入下修比例" v-model="item.beforeLuminance"    @change="beforeLuminanceEventWei"></el-input>
                <div class="buttonBox" style="width: 15% ;float: right;" >
                  <el-button
                    class="delete"
                    @click="deleteHandleUpdateWei(index)"
                  ></el-button>
                  <el-button
                    class="add"
                    @click="addHandleUpdateWei(index)"
                  ></el-button>
                </div>
              </el-form-item>
            </el-col>

          </el-row>
        </el-form>
      </el-col>
    </el-row>


  </el-dialog>
</template>

<script>
import * as echarts from "echarts";
import {addConfig, listConfig, updateConfig} from "@/api/business/wisdomLight/app";
import {dataDevicesLogInfoList, dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {analysisDataByTime} from "@/api/system/trafficStatistics/api";

export default {
  name: "catCurveModal",
  data() {
    return {
      visibleSync:false,
      //车辆数配置文件
      catFilesModel:{beforeLuminance:'',isStatus:''},
      //车辆数配置文件
      catFilesModelWei:{beforeLuminance:'',isStatus:''},
      formItems: [
        {
          label: '',
          startTime: '',
          endTime: '',
          beforeLuminance:'',
        }
      ],
      formItemsWei: [
        {
          label: '',
          startTime: '',
          endTime: '',
          beforeLuminance:'',
        }
      ],
      tunnelDisabled:true,
      paramsData:{},
      tunnelData:[],
      //隧道方向
      lightDirectionOptions: [
        {dictLabel:"济南方向",dictValue:"2"},
        {dictLabel:"潍坊方向",dictValue:"1"}
      ],//方向列表
      catDirectionOptions: [
        {dictLabel:"潍坊方向",dictValue:"1"},
        {dictLabel:"济南方向",dictValue:"2"}
      ],
      directionOptions:[],
      //车辆 x 时间
      XData:[],
      //前天车辆数
      yData1:[],
      //昨天车辆数
      yData2:[],
      //今天车辆数
      yData3:[],
      //车辆 x 时间
      XDataOne:[],
      //前天车辆数
      yDataOne1:[],
      //昨天车辆数
      yDataOne2:[],
      //今天车辆数
      yDataOne3:[],
    }
  },
  mounted() {
  },
  created(){
  },
  methods:{
    closeLogin(){
      this.catFilesModel = {}
      this.formItems = [ {
        label: '',
        startTime: '',
        endTime: '',
        beforeLuminance:'',
      }]
      this.catFilesModelWei = {}
      this.formItemsWei = [ {
        label: '',
        startTime: '',
        endTime: '',
        beforeLuminance:'',
      }]
      this.$emit("selectCatStrategyList")
      this.visibleSync = !this.visibleSync
    },
    beforeLuminanceEvent(e){
      debugger
      console.log(e)
      this.catFilesModel.beforeLuminance  = e
      this.$forceUpdate()
    },
    beforeLuminanceEventWei(e){
      debugger
      console.log(e)
      this.catFilesModelWei.beforeLuminance  = e
      this.$forceUpdate()
    },
    //生成日期
    getdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    //生成日期
    getCatdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    deleteHandleUpdate(index){
      if (this.formItems.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.formItems.splice(index,1)
      debugger
    },
    addHandleUpdate(index){
      debugger
      let form={
        label: '',
        startTime: '',
        endTime: ''
      }
      this.formItems.push(form)
    },
    deleteHandleUpdateWei(index){
      if (this.formItemsWei.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.formItemsWei.splice(index,1)
      debugger
    },
    addHandleUpdateWei(index){
      debugger
      let form={
        label: '',
        startTime: '',
        endTime: ''
      }
      this.formItemsWei.push(form)
    },
    //车来灯亮配置保存
    submitCatForm(){
      //formItems 校验
      for (let index = 0; index < this.formItems.length; index++) {
        const element = this.formItems[index];
        if (element.startTime == null || element.startTime == "") {
          this.$modal.msgError("时间区间开始时间不能为空,请填写开始时间");
          return;
        } else if (element.endTime == null || element.endTime == "") {
          this.$modal.msgError("时间区间结束时间不能为空,请填写结束时间");
          return;
        }else if (element.beforeLuminance == null || element.beforeLuminance == "") {
          this.$modal.msgError("下修比例不能为空");
          return;
        }
      }
      // if(this.catFilesModel.beforeLuminance== null || this.catFilesModel.beforeLuminance== ""){
      //   this.$modal.msgError("下修比例不能为空");
      //   return;
      // }
      if(this.catFilesModel.tunnelId== null || this.catFilesModel.tunnelId== ""){
        this.$modal.msgError("隧道名称不能为空");
        return;
      }
      if(this.catFilesModel.direction== null || this.catFilesModel.direction== ""){
        this.$modal.msgError("隧道方向不能为空");
        return;
      }
      //处理时间转json
      let formItemsOne = JSON.parse(JSON.stringify(this.formItems))
      for (let index = 0; index < this.formItems.length; index++) {
        const param = this.formItems[index];
        param.direction = this.catFilesModel.direction
        param.startTime = new Date( param.startTime);
        param.startTime =
          (param.startTime.getHours() < 10
            ? "0" + param.startTime.getHours()
            : param.startTime.getHours()) +
          ":" +
          (param.startTime.getMinutes() < 10
            ? "0" + param.startTime.getMinutes()
            : param.startTime.getMinutes()) +
          ":" +
          (param.startTime.getSeconds() < 10
            ? "0" + param.startTime.getSeconds()
            : param.startTime.getSeconds());
        param.endTime = new Date( param.endTime);
        param.endTime =
          (param.endTime.getHours() < 10
            ? "0" + param.endTime.getHours()
            : param.endTime.getHours()) +
          ":" +
          (param.endTime.getMinutes() < 10
            ? "0" + param.endTime.getMinutes()
            : param.endTime.getMinutes()) +
          ":" +
          (param.endTime.getSeconds() < 10
            ? "0" + param.endTime.getSeconds()
            : param.endTime.getSeconds());
      }
      this.catFilesModel.timeSlot = JSON.stringify(this.formItems);

      this.formItems =  formItemsOne
      console.log(this.formItems)
      console.log(this.catFilesModel)
      debugger
      //模式1 车辆 0光强
      this.catFilesModel.modeType = 1
      if (!!this.catFilesModel.id) {
        updateConfig(this.catFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
        });
      } else {
        addConfig(this.catFilesModel).then((response) => {
          debugger
          this.catFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
        });
      }
    },
    //车来灯亮配置保存
    submitCatFormWei(){
      //formItems 校验
      for (let index = 0; index < this.formItemsWei.length; index++) {
        const element = this.formItemsWei[index];
        if (element.startTime == null || element.startTime == "") {
          this.$modal.msgError("时间区间开始时间不能为空,请填写开始时间");
          return;
        } else if (element.endTime == null || element.endTime == "") {
          this.$modal.msgError("时间区间结束时间不能为空,请填写结束时间");
          return;
        } else if (element.beforeLuminance == null || element.beforeLuminance == "") {
          this.$modal.msgError("下修比例不能为空");
          return;
        }
      }
      // if(this.catFilesModel.beforeLuminance== null || this.catFilesModel.beforeLuminance== ""){
      //   this.$modal.msgError("下修比例不能为空");
      //   return;
      // }
      if(this.catFilesModelWei.tunnelId== null || this.catFilesModelWei.tunnelId== ""){
        this.$modal.msgError("隧道名称不能为空");
        return;
      }
      if(this.catFilesModelWei.direction== null || this.catFilesModelWei.direction== ""){
        this.$modal.msgError("隧道方向不能为空");
        return;
      }
      //处理时间转json
      let formItemsOne = JSON.parse(JSON.stringify(this.formItemsWei))
      for (let index = 0; index < this.formItemsWei.length; index++) {
        const param = this.formItemsWei[index];
        param.direction = this.catFilesModelWei.direction
        param.startTime = new Date( param.startTime);
        param.startTime =
          (param.startTime.getHours() < 10
            ? "0" + param.startTime.getHours()
            : param.startTime.getHours()) +
          ":" +
          (param.startTime.getMinutes() < 10
            ? "0" + param.startTime.getMinutes()
            : param.startTime.getMinutes()) +
          ":" +
          (param.startTime.getSeconds() < 10
            ? "0" + param.startTime.getSeconds()
            : param.startTime.getSeconds());
        param.endTime = new Date( param.endTime);
        param.endTime =
          (param.endTime.getHours() < 10
            ? "0" + param.endTime.getHours()
            : param.endTime.getHours()) +
          ":" +
          (param.endTime.getMinutes() < 10
            ? "0" + param.endTime.getMinutes()
            : param.endTime.getMinutes()) +
          ":" +
          (param.endTime.getSeconds() < 10
            ? "0" + param.endTime.getSeconds()
            : param.endTime.getSeconds());
      }
      this.catFilesModelWei.timeSlot = JSON.stringify(this.formItemsWei);

      this.formItemsWei =  formItemsOne
      console.log(this.formItemsWei)
      console.log(this.catFilesModelWei)
      debugger
      //模式1 车辆 0光强
      this.catFilesModelWei.modeType = 1
      if (!!this.catFilesModelWei.id) {
        updateConfig(this.catFilesModelWei).then((response) => {
          this.$modal.msgSuccess("修改成功");
        });
      } else {
        addConfig(this.catFilesModelWei).then((response) => {
          debugger
          this.catFilesModelWei.id =response.data.id
          this.$modal.msgSuccess("新增成功");
        });
      }
    },
    //刷新车辆
    catHandleSave(){
      //获取车辆数据
      this.getEchartsTrend()
    },

    //获取车辆数据
    async getEchartsTrend(row,type){
      this.XData = []
      this.yData3 = []
      this.yData2 = []
      this.yData1 = []
      this.XDataOne = []
      this.yDataOne3 = []
      this.yDataOne2 = []
      this.yDataOne1 = []

      if(row==null && type ==null){
        this.tunnelDisabled = false
        this.$nextTick(() => {
          this.initCatChart();
          this.initCatChart1();
        });
      }
      // 获取当前日期
      let currentDate = new Date();
      // 获取前天日期
      let twoDaysAgo = new Date();
      twoDaysAgo.setDate(currentDate.getDate() - 2);
      // 获取大前天日期
      let threeDaysAgo = new Date();
      threeDaysAgo.setDate(currentDate.getDate() - 1);
      let ds = this.getCatdate(currentDate)//今天
      let ds1 = this.getCatdate(twoDaysAgo)//历史
      let ds2 = this.getCatdate(threeDaysAgo)//昨天


      debugger
      if(!!row){//首次
        // 济南方向
        this.catFilesModel.tunnelId= row.tunnelId
        this.catFilesModel.direction= "2"
        let  tunnel = this.tunnelData.find(tunnelItem => row.tunnelId ==  this.catFilesModel.tunnelId)
        console.log(tunnel)
        debugger
        //车辆
        let queryParams = {tunnelName:row.tunnelName,pageSize:1,pageNum:2,direction: this.catFilesModel.direction,modeType:1}
        this.catListConfig(queryParams)

        // 潍坊方向
        this.catFilesModelWei.tunnelId= row.tunnelId
        this.catFilesModelWei.direction= "1"
        let  tunnelWei = this.tunnelData.find(tunnelItem => row.tunnelId ==  this.catFilesModelWei.tunnelId)
        console.log(tunnelWei)
        debugger
        //车辆
        let queryParamsWei = {tunnelName:row.tunnelName,pageSize:1,pageNum:2,direction: this.catFilesModelWei.direction,modeType:1}
        this.catListConfigWei(queryParamsWei)
      }

      //济南
      let json = {
        eqDirection:"hour",
        tunnelId: this.catFilesModel.tunnelId,
        holes: 2
      }
      //今天
      await analysisDataByTime(this.addDateRange(json, ds)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            const randomNumber = Math.floor(Math.random() * 70);
            this.XData.push(response.data[i].date)
            // this.yData3.push(response.data[i].byVehicelNum)
            this.yData3.push(response.data[i].byVehicelNum)
          }
        }
      });
      //昨天
      await analysisDataByTime(this.addDateRange(json, ds2)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            const randomNumber = Math.floor(Math.random() * 70);
            // this.yData2.push(response.data[i].byVehicelNum)
            this.yData2.push(response.data[i].byVehicelNum)
          }
        }
      });
      //历史
      await analysisDataByTime(this.addDateRange(json, ds1)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            // this.yData1.push(response.data[i].byVehicelNum)
            const randomNumber = Math.floor(Math.random() * 70);
            this.yData1.push(response.data[i].byVehicelNum)
          }

        }
      });
      await setTimeout(() => {
        this.$nextTick(() => {
          //获取车辆数据
          this.initCatChart();
        });
      }, 10);
      //潍坊方向
      let json1 = {
        eqDirection:"hour",
        tunnelId: this.catFilesModel.tunnelId,
        holes: 1
      }
      //今天
      await analysisDataByTime(this.addDateRange(json1, ds)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            const randomNumber = Math.floor(Math.random() * 70);
            this.XDataOne.push(response.data[i].date)
            // this.yData3.push(response.data[i].byVehicelNum)
            this.yDataOne3.push(response.data[i].byVehicelNum)
          }
        }
      });
      //昨天
      await  analysisDataByTime(this.addDateRange(json1, ds2)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            const randomNumber = Math.floor(Math.random() * 70);
            // this.yData2.push(response.data[i].byVehicelNum)
            this.yDataOne2.push(response.data[i].byVehicelNum)
          }
        }
      });
      //历史
      await analysisDataByTime(this.addDateRange(json1, ds1)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            // this.yData1.push(response.data[i].byVehicelNum)
            const randomNumber = Math.floor(Math.random() * 70);
            this.yDataOne1.push(response.data[i].byVehicelNum)
          }

        }
      });
      await   setTimeout(() => {
        this.$nextTick(() => {
          //获取车辆数据
          this.initCatChart1();
        });
      }, 10);
    },

    /** 查询隧道列表 */
    async getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      await listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    async getDirection() {
      await this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    // 获取图表数据信息
    initCatChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart1 = echarts.init(this.$refs.chart1);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '济南方向历史车辆数',
            textStyle: {
              color: '#05AAFD', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["历史车辆数", "历史车辆数", "今天车辆数"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XData,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "车辆数",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              name: "历史车辆数",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData1,
            },
            {
              name: "历史车辆数",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData2,
            },
            {
              name: "今天车辆数",
              type: "line",
              color: "#FFB200",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData3,
            },
          ],
        };

        this.mychart1.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart1.resize();
        });
      });
    },
    initCatChart1() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart2 = echarts.init(this.$refs.chart2);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '潍坊方向历史车辆数',
            textStyle: {
              color: '#05AAFD', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["历史车辆数", "历史车辆数", "今天车辆数"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XDataOne,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "车辆数",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              name: "历史车辆数",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataOne1,
            },
            {
              name: "历史车辆数",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataOne2,
            },
            {
              name: "今天车辆数",
              type: "line",
              color: "#FFB200",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataOne3,
            },
          ],
        };

        this.mychart2.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart2.resize();
        });
      });
    },
    //车辆 修改隧道名称查看不同隧道 车来灯亮照明配置
    catChangeEvent(){
      if(!!this.catFilesModel.tunnelId&&!!this.catFilesModel.direction){
        let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.catFilesModel.tunnelId)
        console.log(tunnel)
        debugger
        let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.catFilesModel.direction,modeType:1}
        this.catListConfig(queryParams)
      }

    },
    //车辆 修改隧道名称查看不同隧道 车来灯亮照明配置
    catChangeEventWei(){
      if(!!this.catFilesModelWei.tunnelId&&!!this.catFilesModelWei.direction) {
        let tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId == this.catFilesModelWei.tunnelId)
        console.log(tunnel)
        debugger
        let queryParams = {
          tunnelName: tunnel.tunnelName,
          pageSize: 1,
          pageNum: 2,
          direction: this.catFilesModelWei.direction,
          modeType: 1
        }
        this.catListConfigWei(queryParams)
      }
    },
    //车辆照明配置查询
    catListConfig(queryParams){
      listConfig(queryParams).then((response) => {
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.catFilesModel.id = rows.id
          this.catFilesModel.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.catFilesModel.beforeLuminance =  rows.beforeLuminance
          }else{
            this.catFilesModel.beforeLuminance =''
          }


          let jsonArray = JSON.parse(rows.timeSlot);
          if(!!jsonArray){
            this.formItems =JSON.parse(rows.timeSlot);
            if(this.formItems){
              for (let index = 0; index < this.formItems.length; index++) {
                const param = this.formItems[index];
                param.startTime = new Date(
                  ("1970-01-01 " + param.startTime).replace(/-/g, "/")
                );
                param.endTime = new Date(
                  ("1970-01-01 " + param.endTime).replace(/-/g, "/")
                );
              }
            }
          }else{
            this.formItems =[
              {
                label: '',
                startTime: '',
                endTime: '',
              }
            ]
          }
        }else{
          this.catFilesModel.id = ''
          this.catFilesModel.beforeLuminance =''
          this.formItems =[
            {
              label: '',
              startTime: '',
              endTime: '',
            }
          ]
        }
        this.$forceUpdate();
        // this.total = response.total;
        // this.loading = false;
      })
    },
    //车辆照明配置查询
    catListConfigWei(queryParams){
      listConfig(queryParams).then((response) => {
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.catFilesModelWei.id = rows.id
          this.catFilesModelWei.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.catFilesModelWei.beforeLuminance =  rows.beforeLuminance
          }else{
            this.catFilesModelWei.beforeLuminance =''
          }


          let jsonArray = JSON.parse(rows.timeSlot);
          if(!!jsonArray){
            this.formItemsWei =JSON.parse(rows.timeSlot);
            if(this.formItemsWei){
              for (let index = 0; index < this.formItemsWei.length; index++) {
                const param = this.formItemsWei[index];
                param.startTime = new Date(
                  ("1970-01-01 " + param.startTime).replace(/-/g, "/")
                );
                param.endTime = new Date(
                  ("1970-01-01 " + param.endTime).replace(/-/g, "/")
                );
              }
            }
          }else{
            this.formItemsWei =[
              {
                label: '',
                startTime: '',
                endTime: '',
              }
            ]
          }
        }else{
          this.catFilesModelWei.id = ''
          this.catFilesModelWei.beforeLuminance =''
          this.formItemsWei =[
            {
              label: '',
              startTime: '',
              endTime: '',
            }
          ]
        }
        this.$forceUpdate();
        // this.total = response.total;
        // this.loading = false;
      })
    },
  },
  props:{
    show:Boolean,
    tunnelItem: {
      type: Object
    },
    tunnelList: {
      type: Array
    }
  },
  watch:{
    show:{
      async handler(newValue, oldValue){
        debugger
        this.visibleSync = !this.visibleSync
        // //查询主策略
        // this.selectListStrategy()
        //查询隧道列表
        await this.getTunnels()
        //查询方向
        await this.getDirection()
        //
        // this.$nextTick(() => {
        //   this.getEchartsData(true)
        // });
      }
    }
  }
}
</script>

<style scoped>

.buttonBox {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 36px;
.delete,.add{
  width:16px;
  height: 16px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100%;
  border:none;
  background-color: transparent;
}
.delete{
  background-image: url(../../../../assets/icons/delete.png);
}
.add{
  background-image: url(../../../../assets/icons/add.png);
}
}
.el-form-item-data{
  .el-form-item__content{
    width:85%;
  }
}

.el-form-item-data-type{
.el-form-item__content{
  width:30%;
}
.el-form-item__label{
  width:70px !important;
}
}
.el-form-item-data{
.el-form-item__content{
  width:60%;
}
.el-form-item__label{
  width:70px !important;
}
}
.el-form-item-direction-Item {

.el-form-item__content {
  width: 80%;
}

}
</style>
