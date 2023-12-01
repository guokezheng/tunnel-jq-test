<template>
  <!-- 定时任务-->
  <el-dialog
    class="timedControl"
    custom-class="no-scroll"
    title="定时任务策略"
    :close-on-click-modal="false"
    :visible.sync="visibleSync"
    width="80%"
    :destroy-on-close="false"
    append-to-body
    :lock-scroll="true"
    :before-close="closeLogin"
  >
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
    <div>
      <div style="margin-left: 30%;margin-top: 15px ;font-size: 19px">
        <div style="float: left;">主策略</div>
        <div style="margin-left:30px ;float: left;">当前策略名称：{{titleHistory}}</div>
        <div style="margin-left:30px ;float: left;">时间：{{titleHistory1}}</div>
        <div style="margin-left:30px ;float: left;">下修比例：{{titleHistory2}}</div>
      </div>
      <div class="app-container">



      </div>
      <el-row>
        <el-col :span="21" class="tabs-container">
          <el-tabs  v-model="activeName" @tab-click="handleTabsClick"
                    class="tabsBorder"    size="mini">
            <el-tab-pane v-for="(tab, indextabs) in tabsTimeList" :key="tab.id" :label="tab.label">
              <el-form
                ref="timingControlindextabs"
                v-model="strategyFormList[indextabs]"
                label-width="100px"
              >
                <el-row>
                  <el-col :span="7">
                    <el-form-item label="隧道名称" prop="tunnelId">
                      <el-select
                        style="width: 100%"
                        v-model="strategyFormList[indextabs].tunnelId"
                        placeholder="请选择隧道"
                        clearable
                        @change="changeEvent(indextabs)"
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
                  <el-col :span="7">
                    <el-form-item label="隧道方向" prop="direction">
                      <el-select
                        clearable
                        v-model="strategyFormList[indextabs].direction"
                        placeholder="请选择隧道方向"
                        @change="changeEvent(indextabs)"
                        style="width: 100%"
                      >
                        <el-option
                          v-for="dict in directionOptions"
                          :key="dict.value"
                          :label="dict.dictLabel"
                          :value="dict.dictValue"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
<!--                    <el-form-item label="策略名称" prop="execTime">-->
<!--                      <el-time-picker-->
<!--                        v-model="strategyFormList[indextabs].execTime"-->
<!--                        placeholder="请选择时间"-->
<!--                        value-format="HH:mm:ss"-->
<!--                        @change="changeTime(indextabs)"-->
<!--                      >-->
<!--                      </el-time-picker>-->
<!--                    </el-form-item>-->
                    <el-form-item label="策略名称" prop="strategyName">
                    <el-input
                      style="width: 100%"
                      v-model="strategyFormList[indextabs].strategyName"
                      placeholder="请输入策略名称"
                    />
                  </el-form-item>
                  </el-col>
                  <el-col :span="3">
                    <el-form-item label="状态" align="center" prop="schedulerTime">
                      <template slot-scope="scope">
                        <el-switch
                          v-model="strategyFormList[indextabs].strategyState"
                          active-color="#13ce66"
                          inactive-color="#ff4949"
                          active-value="0"
                          inactive-value="1"
                          @change="changeStrategyState(strategyFormList[indextabs],indextabs)"
                        >
                        </el-switch>
                      </template>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="7">
                    <el-form-item label="执行时间" prop="execTime">
                      <el-time-picker
                        v-model="strategyFormList[indextabs].execTime"
                        placeholder="请选择时间"
                        value-format="HH:mm:ss"
                        @change="changeTime(indextabs)"
                      >
                      </el-time-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="24" style="clear:both;">
                  <el-col :span="24">
                    <el-form-item label="执行操作">
                      <div class="menu">
                        <el-col :span="6">设备资源类型</el-col>
                        <el-col :span="8">指定设备</el-col>
                        <el-col :span="8">控制指令</el-col>
                        <el-col :span="2">操作</el-col>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20" style="clear:both;" class="form-container">
                  <el-col :span="24">
                    <div class="scrollable-content">
                    <el-form-item
                      v-for="(items, index) in strategyFormList[indextabs].autoControl"
                      :key="index"
                    >
                      <el-col :span="6" style="padding-left:0">
                       <el-select
                          v-model="items.equipmentTypeId"
                          placeholder="请选择设备类型"
                          clearable
                          @change="changeEquipmentType(indextabs,index)"
                          style="width:100%"
                        >
                          <el-option
                            v-for="item in equipmentTypeData"
                            :key="item.value"
                            :label="item.keyName"
                            :value="item.value"
                          />
                        </el-select>
                      </el-col>
                      <el-col :span="8">
                        <el-select
                          v-model="items.equipments"
                          multiple
                          collapse-tags
                          placeholder="请选择设备"
                          @change="qbgChange(indextabs,index, items.equipments,false)"
                          style="width:100%"
                        >
                          <el-option
                            v-for="item in items.equipmentData"
                            :key="item.eqId"
                            :label="item.eqName"
                            :value="item.eqId"
                            :disabled="item.disabled"
                          />
                        </el-select>
                      </el-col>
                      <el-col :span="8"
                              v-show="items.equipmentTypeId != 16 && items.equipmentTypeId != 36 && items.equipmentTypeId != 7 && items.equipmentTypeId != 9"
                      >
                        <el-select v-model="items.state" placeholder="请选择设备执行操作" style="width:100%">
                          <el-option
                            v-for="(item, indx) in items.eqStateList"
                            :key="item.deviceState"
                            :label="item.stateName"
                            :value="item.deviceState"
                          >
                          </el-option>
                        </el-select>
                      </el-col>
                      <el-col :span="8"
                              v-show="items.equipmentTypeId == 16 || items.equipmentTypeId == 36"
                      >
                        <el-input v-model="items.content" placeholder="请选择模板" readonly @click.native="openTemDialog(items)">
                          <el-button slot="append" icon="el-icon-search" @click.stop="templateClick(index, index,items)"></el-button>
                        </el-input>
                      </el-col>
                      <el-col
                        :span="8"
                        v-show="items.equipmentTypeId == 7 ||  items.equipmentTypeId == 9"
                      >
                        <el-select
                          :style="{'width':  items.state == 1 ? '45%' :'100%' }"
                          v-model="items.state"
                          placeholder="请选择执行操作"
                          @change="selectStateVal(indextabs,index)"
                        >
                          <el-option
                            v-for="item in items.eqStateList"
                            :key="item.deviceState + 1"
                            :label="item.stateName"
                            :value="item.deviceState"
                          >
                          </el-option>
                        </el-select>
                        <el-input-number v-if="items.state == 1" v-model="items.stateNum" style="width: 55%"   :min="items.limitMin" :max="100" ></el-input-number>
                      </el-col>
                      <el-col :span="2" class="buttonBox">
                        <el-button
                          class="delete"
                          @click="removeItem(indextabs,index)"
                        ></el-button>
                        <el-button
                          class="add"
                          @click="addItem(indextabs)"
                        ></el-button>
                      </el-col>

                    </el-form-item>
                    </div>
                  </el-col>
                </el-row>

              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col  :span="3" class="add-button">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleSave"
          >新增</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="submitStrategyForm(indextabs)"
          >保存</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(indextabs)"
          >删除</el-button
          >
        </el-col>
      </el-row>
      <el-radio-group v-model="tabRadio" style="margin: 10px 0"  size="small"  @change="handleRadioChange">
        <el-radio-button size="small" label="data">历史洞内亮度</el-radio-button>
        <el-radio-button size="small" label="trend">历史车辆数</el-radio-button>
      </el-radio-group>
      <div v-show="tabRadio == 'data'" style="margin-bottom: 10px">
        <el-row :gutter="24" style="clear:both;">
          <el-col :span="9">
            <div  class="chart" ref="chart" style="width: 100%; height: 400px; float: left"></div>
          </el-col>
          <el-col :span="12">
              <el-form
                ref="loginQueryForm"
                :model="lightFilesModel"
                :inline="true"
                class="loginQueryFormClass"
                label-width="100px"
                height="300px"
              >
                <el-row :gutter="24" style="clear:both;">
                  <el-col :span="12">
                    <el-form-item label="隧道名称" prop="tunnelId">
                      <el-select
                        style="width: 100%"
                        v-model="lightFilesModel.tunnelId"
                        placeholder="请选择隧道"
                        clearable
                        @change="lightChangeEvent"
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
                  <el-col :span="12">
                    <el-form-item label="隧道方向" prop="direction">
                      <el-select
                        clearable
                        v-model="lightFilesModel.direction"
                        placeholder="请选择隧道方向"
                        @change="lightChangeEvent"
                        style="width: 100%"
                      >
                        <el-option
                          v-for="dict in lightDirectionOptions"
                          :key="dict.value"
                          :label="dict.dictLabel"
                          :value="dict.dictValue"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="24" style="clear:both;">
                  <el-col :span="12">
                    <el-form-item label="状态" align="center" prop="schedulerTime">
                      <template slot-scope="scope">
                        <el-switch
                          v-model="lightFilesModel.isStatus"
                          active-color="#13ce66"
                          inactive-color="#ff4949"
                          active-value="0"
                          inactive-value="1"
                          @change="changeCattate()"
                        >
                        </el-switch>
                      </template>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="下修比例"  >
                      <el-input  v-model="lightFilesModel.beforeLuminance"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
          </el-col>
          <el-col :span="3">

            <el-button
              size="mini"
              class="tableBlueButtton"
              @click="submitlightForm"
            >保存</el-button
            >
            <el-button
              size="mini"
              class="tableBlueButtton"
              @click="refreshlightForm"
            >刷新</el-button
            >
          </el-col>
        </el-row>
      </div>

      <div style="margin-bottom: 10px"  v-show="tabRadio == 'trend'">
        <el-row :gutter="24" style="clear:both;">
          <el-col :span="9">
            <div  id='trend' class="chartTow" ref="chart1" style="width: 100%; height: 400px; float: left"></div>
          </el-col>
          <el-col :span="12">
            <el-form
              ref="loginQueryForm"
              :model="catFilesModel"
              :inline="true"
              class="loginQueryFormClass"
              label-width="100px"
              height="300px"
            >
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="12">
                  <el-form-item label="隧道名称" prop="tunnelId">
                    <el-select
                      style="width: 100%"
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
                <el-col :span="12">
                  <el-form-item label="方向" prop="direction">
                    <el-select
                      clearable
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
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="12">
                  <el-form-item label="状态" align="center" prop="schedulerTime">
                    <template slot-scope="scope">
                      <el-switch
                        v-model="catFilesModel.isStatus"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-value="0"
                        inactive-value="1"
                        @change="changeCattate"
                      >
                      </el-switch>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="下修比例"  >
                    <el-input  v-model="catFilesModel.beforeLuminance"    @change="beforeLuminanceEvent"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="时间段"  v-for="(item, index) in formItems" :key="index" >
<!--                <el-time-picker-->
<!--                  style="width: 35%"-->
<!--                  v-model="item.startTime"-->
<!--                  size="small"-->
<!--                  :picker-options="pickerOptions"-->
<!--                  placeholder="选择开始时间">-->
<!--                </el-time-picker>-->
                <el-time-picker
                  placeholder="选择开始时间"
                  v-model="item.startTime"
                  style="width: 38%"
                ></el-time-picker>

<!--                <el-time-picker-->
<!--                  style="width: 35%"-->
<!--                  v-model="item.endTime"-->
<!--                  size="small"-->
<!--                  :picker-options="pickerOptions"-->
<!--                  placeholder="选择结束时间">-->
<!--                </el-time-picker>-->
                <el-time-picker
                  placeholder="选择结束时间"
                  v-model="item.endTime"
                  style="width: 38%"
                ></el-time-picker>
<!--                <el-button-->
<!--                  style="width: 10%"-->
<!--                  size="small"-->
<!--                  class="tableBlueButtton"-->
<!--                  @click="addHandleUpdate(index)"-->
<!--                >+-->
<!--                </el-button-->
<!--                ><el-button-->
<!--                style="width: 10%"-->
<!--                size="small"-->
<!--                class="tableBlueButtton"-->
<!--                @click="deleteHandleUpdate(index)"-->
<!--              >- -->
<!--              </el-button-->
<!--              >-->
<!--                <el-col :span="2" class="buttonBox">-->
                <div class="buttonBox" style="width: 20% ;float: right;" >
                  <el-button
                    class="delete"
                    @click="deleteHandleUpdate(index)"
                  ></el-button>
                  <el-button
                    class="add"
                    @click="addHandleUpdate(index)"
                  ></el-button>
                </div>
<!--                </el-col>-->
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="3">
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
      </div>
    </div>

  </el-dialog>
</template>

<script>
import * as echarts from "echarts";
import {
  addStrategyInfo, delStrategy,
  getCategoryTree,
  getGuid,
  getSdStrategyAll,
  getStrategy,
  updateState, updateStrategyInfo
} from "@/api/event/strategy";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {dataDevicesLogInfoList, dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";
import {listDevices} from "@/api/equipment/eqlist/api";
import {getVMSTemplatesByDevIdAndCategory, listEqTypeStateIsControl,} from "@/api/equipment/eqTypeState/api";
import {listRl} from "@/api/event/strategyRl";
import {analysisDataByTime} from "@/api/system/trafficStatistics/api";
import {
  addConfig, listConfig, updateConfig
} from "@/api/business/enhancedLighting/app.js";

export default {
  name: "timingTask",
  data() {
    return {
      titleHistory:"杭山东策略",
      titleHistory1:"2023-07-20",
      titleHistory2:"30%",
      visibleSync:false,
      operationLogDialog:false,
      loginModel:{},
      tableData: [{
        id: 1,
        date: '2016-05-02',
        name: '测试定时控制00001',
        address: '加强照明',
        eqId:'出口段',
        controlInstruction:"开启",
        editableDisabled:false
      }, {
        id: 2,
        date: '2016-05-04',
        name: '测试定时控制00002',
        address: '加强照明',
        eqId:'入口段',
        controlInstruction:"开启",
        editableDisabled:false
      }, {
        id: 3,
        date: '2016-05-01',
        name: '测试定时控制00003',
        address: '加强照明',
        controlInstruction:"开启",
        editableDisabled:false,
        children: [{
          id: 31,
          date: '2016-05-01',
          name: '测试定时控制00003',
          address: '加强照明',
          eqId:'出口段',
          controlInstruction:"开启",
          editableDisabled:false
        }, {
          id: 32,
          date: '2016-05-01',
          name: '测试定时控制00003',
          address: '加强照明',
          eqId:'入口段',
          controlInstruction:"开启",
          editableDisabled:false
        }]
      }, {
        id: 4,
        date: '2016-05-03',
        name: '测试定时控制00004',
        address: '加强照明',
        eqId:'入口段',
        controlInstruction:"开启",
        editableDisabled:false
      }],
      chart: null,
      chart1: null,
      startTime: '', // 存储开始时间
      endTime: '', // 存储结束时间
      pickerOptions: {
        selectableRange: '00:00:00 - 23:59:59' // 设置可选的时间范围
      },
      formItems: [
        {
          label: '',
          startTime: '',
          endTime: '',
        }
      ],
      strategyState:false,
      //光强配置文件
      lightFilesModel:{},
      //车辆数配置文件
      catFilesModel:{beforeLuminance:''},
      lightDirectionOptions: [
        {dictLabel:"济南方向",dictValue:"2"},
        {dictLabel:"潍坊方向",dictValue:"1"}
      ], //方向列表
      catDirectionOptions: [
        {dictLabel:"上行",dictValue:"1"},
        {dictLabel:"下行",dictValue:"2"}
      ], //方向列表
      tabRadio:"data",
      mychart1:null,
      editingRow:"",
      tunnelData: [], //隧道列表
      //隧道列表查询
      paramsData: {
        tunnelId: "",
      },
      directionOptions: [], //方向列表
      //设备类型
      equipmentTypeData: [
        {keyName:"加强照明",value:'7'},
        {keyName:"基础照明",value:'9'}
      ],
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      //光查询
      queryParamsLight:{},
      //光 x  光强
      XDataLight:[],
      //光 y  时间
      yDataLight:[],
      //光 y  时间
      yDataLight1:[],
      //光 y  时间
      yDataLight2:[],

      //车辆 x 时间
      XData:[],
      //前天车辆数
      yData1:[],
      //昨天车辆数
      yData2:[],
      //今天车辆数
      yData3:[],
      timingControl:"timingControl",
      tabsTimeList:[
        { label: 'add', content: '内容1' ,id:1},
        { label: 'add', content: '内容1' ,id:2},
      ],
      activeName:'',
      strategyForm:{
        strategyState:1,// 策略状态
        strategyGroup: 1,
        schedulerTime: "", //cron数据
        strategyType: "1", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        execDate: "",
        execTime:"",
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            limitMin:1,
            stateNum: 100,
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          },
        ],
      },
      //定时策略实体
      strategyFormList:[{
        strategyState:1,// 策略状态
        strategyGroup: 1,
        schedulerTime: "", //cron数据
        strategyType: "1", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        execDate: "",
        execTime:"",
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            limitMin:1,
            stateNum: 100,
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          },
        ],
      }],//定时策略实体
      strategyRowList:[],//定时策略策略头集合
      //定时策略限制
      formDataValidator: {
        direction: [{ required: true, message: "请选择隧道方向", trigger: "blur" }],
        tunnelId: [
          { required: true, message: "请选择隧道", trigger: "change" },
        ],
        strategyName: [
          { required: true, message: "请输入策略名称", trigger: "change" },
          { max: 50, message: '最长输入50个字符', trigger: 'change' }
        ],
        /*    schedulerTime:[
              { required: true, message: "请输入定时频率", trigger: "change" }
            ],*/
        execTime: [
          { required: true, message: "请选择执行时间", trigger: "change" },
        ]
      },
      editable: true, // 设置选项卡是否可编辑
      //当前隧道
      tunnelItems:{},
      //全部隧道
      tunnelLists:[],
    }
  },
  mounted() {
    this.initChart();
  },
  created(){
    // debugger

    //查询方向
    this.getDirection()
    // debugger
    //获取所有定时控制数据
    this.getTimingAll()
  },
  methods:{
    //获取所有定时控制数据
    getTimingAll(indextabs){
      // debugger
      let queryParams = {
        pageNum:1,
        pageSize:999,
        strategyGroup:1,
        strategyType:1
      }
      getSdStrategyAll(queryParams).then((response) => {
        let rows = response.rows;

        if(!!rows&&rows.length>0){
          this.tabsTimeList = []
          this.strategyRowList =[]
          this.strategyFormList = []
          for (let i = 0; i < rows.length; i++) {
            let strate = {
              strategyState:1,// 策略状态
              strategyGroup: 1,
              schedulerTime: "", //cron数据
              strategyType: "1", //策略类型
              tunnelId: null, //隧道id
              strategyName: null, //策略名称
              direction: "", //方向
              execDate: "",
              execTime:"",
              autoControl: [
                {
                  value: "", //设备
                  state: "", //状态
                  type: "", //设备分类
                  limitMin:1,
                  stateNum: 100,
                  equipmentTypeId: "", //设备类型
                  equipments: [], //设备列表
                  equipmentTypeData: [],
                  equipmentData: [],
                },
              ],
            }
            //要显示的操作保存的数据集合
            this.strategyFormList.push(strate)
            let tabsTime = {label: rows[i].execTime, content: rows[i].id  ,id: rows[i].id }
            //选项卡的数据集合
            this.tabsTimeList.push(tabsTime)
            //定时策略策略头集合
            this.strategyRowList.push(rows[i])
          }
          if(!!indextabs){
            this.getStrategyData(rows[indextabs],indextabs)
          }else{
            //默认获取首个选项卡详情数据
            this.getStrategyData(rows[0],0)
          }

        }
      });
    },
    //获取定时控制详细信息
    async getStrategyData(row,indextabs) {
      // debugger
      console.log(row, "当前策略数据");
      getStrategy(row.id).then((response) => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
          target:'.strategy-dialog',
        });
        // debugger
        let data = response.data;
        this.strategyFormList[indextabs].id = data.id;
        this.strategyFormList[indextabs].strategyName = data.strategyName;
        this.strategyFormList[indextabs].tunnelId = data.tunnelId;
        this.strategyFormList[indextabs].strategyType = data.strategyType;
        this.strategyFormList[indextabs].direction = data.direction;
        this.strategyFormList[indextabs].strategyState = data.strategyState;
        // this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyFormList[indextabs].jobRelationId = data.jobRelationId;
        this.strategyFormList[indextabs].schedulerTime = data.schedulerTime;
        this.strategyFormList[indextabs].execDate = data.execDate;
        this.strategyFormList[indextabs].execTime = data.execTime;
        listRl({ strategyId: row.id }).then((response) => {
          this.strategyFormList[indextabs].equipmentTypeId = response.rows[0].eqTypeId;
          let params = {
            eqType: response.rows[0].eqTypeId,
            eqTunnelId: this.strategyFormList[indextabs].tunnelId,
            eqDirection: this.strategyFormList[indextabs].direction, //方向
            params:{
              orderBy : 'eqName'
            }
          }
          if(this.strategyFormList[indextabs].direction == 3){
            params.eqDirection = null;
          }
          listDevices(params).then((res) => {
            this.equipmentData = res.rows;
          });
          this.strategyFormList[indextabs].autoControl = response.rows;
          for (var i = 0; i < response.rows.length; i++) {
            let autoControl = this.strategyFormList[indextabs].autoControl[i];
            var attr = response.rows[i];
            // debugger
            this.strategyFormList[indextabs].autoControl[i].eqStateList = attr.eqStateList;
            this.strategyFormList[indextabs].autoControl[i].state = attr.state;
            this.strategyFormList[indextabs].autoControl[i].stateNum = attr.stateNum;
            this.strategyFormList[indextabs].autoControl[i].type = attr.eqTypeId;
            this.strategyFormList[indextabs].autoControl[i].equipmentTypeId = String(
              attr.eqTypeId
            );

            let  equipmentArray = attr.equipments.split(",");
            if (
              this.strategyFormList[indextabs].autoControl[i].equipmentTypeId == 16 ||
              this.strategyFormList[indextabs].autoControl[i].equipmentTypeId == 36
            ) {
              this.strategyFormList[indextabs].autoControl[i].state = +attr.state;
              this.qbgChange(indextabs,i,equipmentArray,true);
            }
            this.$set(autoControl, "equipmentTypeData", this.equipmentTypeData);

            //基本照明限制 最低亮度为 30
            if(this.strategyFormList[indextabs].autoControl[i].equipmentTypeId == 9){
              this.$set(this.strategyFormList[indextabs].autoControl[i], "limitMin", 30);
            }


            let params = {
              eqType: attr.eqTypeId,
              eqTunnelId: this.strategyFormList[indextabs].tunnelId,
              eqDirection: this.strategyFormList[indextabs].direction, //方向
              params:{
                orderBy : 'eqName'
              }
            };
            if(this.strategyFormList[indextabs].direction == 3){
              params.eqDirection = null;
            }
            listDevices(params).then((res) => {
              this.$set(autoControl, "equipmentData", res.rows);
              console.log(autoControl.equipmentData, "设备列表数据1");
              this.$set(autoControl, "equipments", equipmentArray);
              //this.strategyForm.autoControl[i].equipments = equipmentArray;

            });
          }
        });
        setTimeout(() => {
          loading.close();
        }, 1700);
      });


    },
    getdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    getCatdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    //获取光亮的
    getEchartsData(tunnelList ,tunnelItem) {
      if(!!tunnelList){
        this.tunnelLists = tunnelList
      }
      if(!!tunnelItem){
        this.tunnelItems = tunnelItem
      }
      this.XDataLight = []
      this.yDataLight = []
      this.yDataLight1 = []
      this.yDataLight2 = []

      // debugger
      let tunnelItems = null;
      if(!!this.tunnelItems.tunnelId){
        tunnelItems = this.tunnelItems
      }else{
        tunnelItems = this.tunnelLists[0]
      }
      if(!!tunnelItems.tunnelId&&(this.lightFilesModel.tunnelId==null||this.lightFilesModel.tunnelId=="")){//首次
        // this.queryParamsLight.tunnelId = tunnelItems.tunnelId
        this.lightFilesModel.tunnelId= tunnelItems.tunnelId

      }
      // debugger
      let querysParamsTab = {}
      querysParamsTab.tunnelId =   this.lightFilesModel.tunnelId
      querysParamsTab.pageNum =  1
      querysParamsTab.pageSize =  999
      //类型
      querysParamsTab.searchValue =  4
      //根据隧道id获取相应外部测光设备
      dataDevicesLogInfoList(this.addDateRange(querysParamsTab)).then(
        (response) => {
          let listTab = response.rows;
          if(listTab.length>0){
            if(!!tunnelList ){//首次打开页面
              if(listTab[0].direction=="济南方向"){
                this.lightFilesModel.direction = "2"
              }
              if(listTab[0].direction=="潍坊方向"){
                this.lightFilesModel.direction = "1"
              }
              let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.lightFilesModel.tunnelId)
              // debugger
              let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}

              this.lightListConfig(queryParams)
            }
            this.queryParamsLight.direction = this.lightFilesModel.direction
            if(this.lightFilesModel.direction=="2"){//济南
              let rows = listTab.find(item => item.direction=="济南方向")
              this.queryParamsLight.deviceId= rows.eqId
            }
            if(this.lightFilesModel.direction=="1"){//潍坊
              let rows = listTab.find(item => item.direction=="潍坊方向")
              this.queryParamsLight.deviceId= rows.eqId
            }

            this.queryParamsLight.pageNum = 1;
            this.queryParamsLight.pageSize = 10;
            this.queryParamsLight.searchValue = 4;
            // 获取当前日期
            let currentDate = new Date();

            // 获取前天日期
            let twoDaysAgo = new Date();
            twoDaysAgo.setDate(currentDate.getDate() - 2);

            // 获取大前天日期
            let threeDaysAgo = new Date();
            threeDaysAgo.setDate(currentDate.getDate() - 1);
            let ds = this.getdate(currentDate)
            let ds1 = this.getdate(twoDaysAgo)//前天
            let ds2 = this.getdate(threeDaysAgo)//昨天
            // this.XDataLight = this.generateTimeList()

            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds1)
            ).then((response) => {
              // debugger
              let list1 = response.rows;
              for (let i = 0; i < list1.length; i++) {
                //前天
                // this.XDataLight.push(list1[i].createTime)
                this.yDataLight.push(list1[i].data)
              }
              // debugger
              // setTimeout(() => {
              //   this.$nextTick(() => {
              //     this.initChart();
              //   });
              // }, 500);
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds2)
            ).then((response) => {
              // debugger
              let list1 = response.rows;
              //昨天光强
              for (let i = 0; i < list1.length; i++) {
                // this.XDataLight.push(list1[i].createTime)
                this.yDataLight1.push(list1[i].data)
              }
              // debugger
              // setTimeout(() => {
              //   this.$nextTick(() => {
              //     this.initChart();
              //   });
              // }, 500);
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds)
            ).then((response) => {
              // debugger
              let list1 = response.rows;
              //今天
              for (let i = 0; i < list1.length; i++) {
                this.XDataLight.push(list1[i].createTime)
                this.yDataLight2.push(list1[i].data)
              }
              // console.log(this.XDataLight)
              // console.log(this.XDataLight)
              // debugger
              setTimeout(() => {
                this.$nextTick(() => {
                  this.initChart();
                });
              }, 500);
            });
          }
        }
      );
    },
    generateTimeList() {
      let timeList = [];
      let date = new Date();
      date.setHours(0, 0, 0, 0); // 设置时间为凌晨00:00
      console.log(date.getHours() )
      console.log(date.getMinutes() )
      while (date.getHours() < 24 ) {
        timeList.push(this.formatTime(date.getHours(), date.getMinutes()));
        if(this.formatTime(date.getHours(), date.getMinutes())=="23:58"){
          break
        }
        date.setMinutes(date.getMinutes() + 2);
      }
      return timeList;
    },

    formatTime(hours, minutes) {
      let formattedHours = hours.toString().padStart(2, '0');
      let formattedMinutes = minutes.toString().padStart(2, '0');
      return formattedHours + ':' + formattedMinutes;
    },
    /** 查询隧道列表 */
    getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    getDirection() {
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    handleRowEdit(item) {
      // debugger
      this.editingRow = item.row.id;
      this.$forceUpdate()
    },
    handleRowSave(item){
      // debugger
      this.editingRow =""
        this.$forceUpdate()
    },
    // 改变设备类型或者方向
    changeEvent(indextabs) {
      // debugger
      // 重置设备列表
      this.strategyFormList[indextabs].autoControl = [
        { state: "", value: "", equipmentTypeId: "" },
      ];
    },

    equipmenEvent(value){
      // debugger
      let equipment = this.equipmentTypeData.find(item => item.value ==value.equipmentTypeId);
      value.equipmentName = equipment.keyName
    },
    /** 查询设备类型列表 */
    // getEquipmentType() {
    //   let autoControl = this.strategyForm.autoControl;
    //   for (let i = 0; i < autoControl.length; i++) {
    //     getCategoryTree().then((data) => {
    //       this.$set(autoControl[i], "equipmentTypeData", data.data);
    //       this.equipmentName = data.data;
    //     });
    //   }
    // },
    // 获取图表数据信息
    initCatChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart1 = echarts.init(document.getElementById(this.tabRadio));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '历史车辆数',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天车辆数", "昨天车辆数", "今天车辆数"],
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
              name: "前天车辆数",
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
              name: "昨天车辆数",
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
    // 获取图表数据信息
    initChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart = echarts.init(this.$refs.chart);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '历史洞内亮度',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天光强", "昨天光强", "今天光强"],
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
            data: this.XDataLight,
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
            name: "光强",
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
              name: "前天光强",
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
              data: this.yDataLight,
            },
            {
              name: "昨天光强",
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
              data: this.yDataLight1,
            },
            {
              name: "今天光强",
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
              data: this.yDataLight2,
            },
          ],
        };

        this.mychart.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart.resize();
        });
      });
    },
    renderChart() {
      // debugger
      this.chart = echarts.init(this.$refs.chart);
      this.chart.setOption({
        xAxis: {
          type: 'category',
          data: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        },
        yAxis: {
          type: 'value',
        },
        series: [{
          data: [10, 20, 30, 40, 50, 60, 70],
          type: 'line',
        }],
      });
    },
    closeLogin(){
      // debugger
      this.visibleSync = false
      // this.renderChart();
    },
    ProductionSetting(){

    },
    startDecoding(){

    },
    stopDecoding(){

    },

    resetQuery(){
      // debugger
      this.visibleSync = !this.visibleSync
    },
    deleteHandleUpdate(index){
      if (this.formItems.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.formItems.splice(index,1)
      // debugger
    },
    addHandleUpdate(index){
      // debugger
      let form={
        label: '',
        startTime: '',
        endTime: ''
      }
      this.formItems.push(form)
    },
    //新增选项卡
    handleSave(){
      let strate = {
        strategyState:1,// 策略状态
        strategyGroup: 1,
        schedulerTime: "", //cron数据
        strategyType: "1", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        execDate: "",
        execTime:"",
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            limitMin:1,
            stateNum: 100,
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          },
        ],
      }
      this.strategyFormList.push(strate)
      let tabsTime = {label: '新增定时策略', content: '新增'  ,id:this.strategyFormList.length+1}
      this.tabsTimeList.push(tabsTime)
      //定时策略策略头集合
      this.strategyRowList.push(strate)
      this.resetForm(this.tabsTimeList.length-1)
    },
    //选项卡删除
    removeTab(){
      // debugger
    },

    //表单重置方法
    resetForm(indextabs) {
      this.$refs["timingControlindextabs"][this.activeName].resetFields();
      this.strategyFormList[indextabs].schedulerTime = "";
      this.strategyFormList[indextabs].autoControl = [{
        value: "", //设备
        state: "", //状态
        type: "", //设备分类
        equipmentTypeId: "", //设备类型
        equipments: [], //设备列表
        equipmentTypeData: [],
        equipmentData: [],
      }];
    },
    //删除操作
    removeItem(indextabs ,index) {
      if (this.strategyFormList[indextabs].autoControl.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.strategyFormList[indextabs].autoControl.splice(index, 1);
    },
    // 添加执行操作
    addItem(indextabs) {
      // debugger
      // this.$refs["timingControl"].validate((valid) => {
      //   if (valid) {
          // debugger
          this.addCf(indextabs);
          this.strategyFormList[indextabs].autoControl.push({
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          });
          // this.getEquipmentType();
      //   }
      // });
    },
    addCf(indextabs) {

      let params = {
        eqType: this.strategyFormList[indextabs].equipmentTypeId,
        eqTunnelId: this.strategyFormList[indextabs].tunnelId,
        eqDirection: this.strategyFormList[indextabs].direction,
        params:{
          orderBy : 'eqName'
        }
      };
      // 选择双向，则不进行接口过滤条件
      if(this.strategyFormList[indextabs].direction == 3){
        params.eqDirection = null;
      }
      listDevices(params).then((res) => {
        let data = res.rows;
        if (this.strategyFormList[indextabs].autoControl.length > 1) {
          var currentList = this.strategyFormList[indextabs].autoControl;
          let newData = [];
          for (let i = 0; i < currentList.length; i++) {
            newData += currentList[i].value + ",";
          }
          newData = newData.split(",");
          console.log(newData);
          for (let i = 0; i < data.length; i++) {
            for (let z = 0; z < newData.length; z++) {
              if (data[i].eqId == newData[z]) {
                data[i].disabled = true;
              }
            }
          }
          this.equipmentData = data;
          this.$forceUpdate();
        } else {
          this.equipmentData = data;
        }
      });
    },
    // 改变设备资源类型
    changeEquipmentType(indextabs,index) {
      this.$set(this.strategyFormList[indextabs].autoControl[index], "content", null);
      this.$set(this.strategyFormList[indextabs].autoControl[index], "stateNum", null);
      this.$set(this.strategyFormList[indextabs].autoControl[index], "state", "");
      this.$set(this.strategyFormList[indextabs].autoControl[index], "equipments", null);


      if(!!this.strategyFormList[indextabs].tunnelId &&  !!this.strategyFormList[indextabs].direction&& !!this.strategyFormList[indextabs].autoControl[index].equipmentTypeId){
        let params = {
          eqType: this.strategyFormList[indextabs].autoControl[index].equipmentTypeId, //设备类型
          eqTunnelId: this.strategyFormList[indextabs].tunnelId, //隧道
          eqDirection: this.strategyFormList[indextabs].direction, //方向
          params:{
            orderBy : 'eqName'
          }
        };
        if(this.strategyFormList[indextabs].direction == 3){
          params.eqDirection = null;
        }
        listDevices(params).then((res) => {
          this.$set(
            this.strategyFormList[indextabs].autoControl[index],
            "equipmentData",
            res.rows
          );
          console.log(this.equipmentData, "设备列表");
        });
        this.listEqTypeStateIsControl(indextabs,index);
      }
    },
    // 查询设备可控状态
    listEqTypeStateIsControl(indextabs,index) {
      // debugger
      var stateTypeId = this.strategyFormList[indextabs].autoControl[index].equipmentTypeId;
      var direction = this.strategyFormList[indextabs].direction;
      let params = {
        stateTypeId: stateTypeId,
        direction: direction,
        isControl: 1,
      };
      if(this.strategyFormList[indextabs].direction == 3){
        params.eqDirection = null;
      }
      listEqTypeStateIsControl(params).then((response) => {
        // debugger
        this.strategyFormList[indextabs].autoControl[index].eqStateList = response.rows;
      });
    },
    //选中设备
    qbgChange(indextabs,index, value,flag) {
      let data = value;
      if (
        this.strategyFormList[indextabs].autoControl[index].equipmentTypeId == 16 ||
        this.strategyFormList[indextabs].autoControl[index].equipmentTypeId == 36
      ) {
        getVMSTemplatesByDevIdAndCategory(data).then((res) => {
          // this.templatesList = res.data;
          this.$set(
            this.strategyFormList[indextabs].autoControl[index],
            "templatesList",
            res.data
          );
        });
      }
      if(!flag){
        // 设备联控，命令重置
        this.strategyFormList[indextabs].autoControl[index].state = null;
        this.strategyFormList[indextabs].autoControl[index].content = null;
      }
    },
    selectStateVal(indextabs,index){
      if(this.strategyFormList[indextabs].autoControl[index].state == 1){
        this.$set(this.strategyFormList[indextabs].autoControl[index], "stateNum", 100);
        //基本照明限制 最低亮度为 30
        if(this.strategyFormList[indextabs].autoControl[index].equipmentTypeId == 9){
          this.$set(this.strategyFormList[indextabs].autoControl[index], "limitMin", 30);
        }

      }else{
        this.$set(this.strategyFormList[indextabs].autoControl[index], "stateNum", 0);
      }
    },
    //执行时间
    changeTime(index){
      this.tabsTimeList[index].label = this.strategyFormList[index].execTime

      let date =  this.strategyFormList[index].execTime;
      let dateTime = new Date(date).getTime();
      // if(this.strategyForm.execDate && this.strategyForm.execTime && dateTime < new Date()){
      //   this.$modal.msgWarning("执行时间不得早于当前时间");
      //   this.strategyForm.execDate = null;
      //   this.strategyForm.execTime = null;
      //   return false;
      // }
      return  true;
    },
    /** 删除按钮操作 */
    handleDelete(indextabs) {
      let that = this
      let row = that.strategyFormList[that.activeName]
      const ids = row.id || this.ids;
      const rlIds = row.id || this.rlIds;
      const jobRelationId = row.jobRelationId;
      this.$confirm("是否确认删除？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // if (row.jobRelationId != null) {
          //   delJob(jobRelationId).then((response) => {});
          // }
          delStrategy(ids).then((res) => {
            if (res.code == 200) {
              this.tabsTimeList.splice(that.activeName,1)
              this.strategyRowList.splice(that.activeName,1)
              this.strategyFormList.splice(that.activeName,1)
              if(this.activeName>0){
                this.activeName =this.activeName-1+""
              }
              this.getStrategyData(this.strategyRowList[parseInt(this.activeName)],parseInt(this.activeName))
              this.$forceUpdate()
              this.$modal.msgSuccess(res.msg);
            } else {
              this.$modal.msgError(res.msg);
            }
            // this.getList();
          });
        })
        .catch(function () {
        });
    },
    /** 提交按钮 */
    async submitStrategyForm(indextabs) {
      // debugger
      // this.$refs["timingControlindextabs"][this.activeName].validate((valid) => {
      //   if (valid) {
          // debugger
          var autoControl = this.strategyFormList[this.activeName].autoControl;
          let response = JSON.parse(JSON.stringify(autoControl))
          let result = response.every(function (item) {
            return item.equipmentTypeId != "" && item.state != "" && item.equipments != "" &&
              item.equipmentTypeId != null && item.state != null && item.equipments != null
          });
          if(!result){
            return this.$modal.msgError("请填写完整策略信息！");
          }

          // 判断是修改还是删除
          if (!!this.strategyFormList[this.activeName].id) {
            this.updateStrategyInfoData();
          } else {
            this.addStrategyInfoData(this.activeName);
          }
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
        }
      }
      if(this.catFilesModel.beforeLuminance== null || this.catFilesModel.beforeLuminance== ""){
        this.$modal.msgError("下修比例不能为空");
        return;
      }
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
      // debugger
      //模式1 车辆 0光强
      this.catFilesModel.modeType = 1
      if (!!this.catFilesModel.id) {
        updateConfig(this.catFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
          this.open = false;
          this.$refs.tableFile.clearSelection();
          this.getList();
        });
      } else {
        addConfig(this.catFilesModel).then((response) => {
          // debugger
          this.catFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
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
    //光强照明配置查询
    lightListConfig(queryParams){
      // debugger
      listConfig(queryParams).then((response) => {
        // debugger
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.lightFilesModel.id = rows.id
          this.lightFilesModel.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.lightFilesModel.beforeLuminance =  rows.beforeLuminance
          }else{
            this.lightFilesModel.beforeLuminance =''
          }
        }else{
          this.lightFilesModel.id = ''
          this.lightFilesModel.beforeLuminance =''
        }
        this.$forceUpdate();
        // this.total = response.total;
        // this.loading = false;
      })
    },
    //光强 修改隧道名称查看不同隧道 光强照明配置
    lightChangeEvent(indextabs) {
      let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.lightFilesModel.tunnelId)
      // debugger
      let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}

      this.lightListConfig(queryParams)
      this.$forceUpdate()
    },
    //车辆 修改隧道名称查看不同隧道 车来灯亮照明配置
    catChangeEvent(){
      let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.catFilesModel.tunnelId)
      // debugger
      let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.catFilesModel.direction,modeType:1}
      this.catListConfig(queryParams)
    },
    beforeLuminanceEvent(e){
      // debugger
      this.catFilesModel.beforeLuminance  = e
      this.$forceUpdate()
    },
    //车辆状态
    changeCattate(e){
      console.log(e)
      // debugger
    },
    // 编辑操作
    async updateStrategyInfoData() {
      // if (this.sink == "add") {
      //   await getGuid().then((res) => {
      //     this.strategyForm.jobRelationId = res;
      //     this.strategyForm.id = this.id;
      //   });
      // }
      let data = this.strategyFormList[this.activeName].autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyFormList[this.activeName];
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
      });
    },
    // 提交保存方法
    async addStrategyInfoData(indextabs) {
      this.strategyFormList[indextabs].id = null;
      this.strategyFormList[indextabs].strategyState = 1;
      await getGuid().then((res) => {
        this.strategyFormList[indextabs].jobRelationId = res;
      });
      let data = this.strategyFormList[indextabs].autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyFormList[indextabs];
      addStrategyInfo(params).then((res) => {
        // this.resetForm(indextabs);
        let data = true;
        this.$emit("dialogVisibleClose",data);
        this.$modal.msgSuccess("新增策略成功");
        // //获取所有定时控制数据
        this.getTimingAll(indextabs)
      });
    },
    //切换选项卡
    handleTabsClick(tab, event) {
      console.log(tab, event);
      // debugger
      console.log(this.strategyRowList)
      if(!!this.strategyRowList[parseInt(tab.index)].id){
        //默认获取选项卡详情数据
        this.getStrategyData( this.strategyRowList[parseInt(tab.index)],parseInt(tab.index))
      }
    },
    //状态开启关闭
    changeStrategyState(row,index) {
      if(!!row.id){
        let data = {strategyId: row.id, change: row.strategyState};
        updateState(data).then((result) => {

          if(result.code == 200){
            if(row.strategyState == 0){
              this.$modal.msgSuccess("开启成功");
            }else{
              this.$modal.msgSuccess("关闭成功");
            }
          }else{
            this.$modal.msgSuccess(result.msg);
          }
        });
      }
    },
    //车辆光照事件
    handleRadioChange(value){
      console.log('选中的选项值：', value);
      if(value=="data"){

      }else if(value=="trend"){
        //获取车辆数据
        this.getEchartsTrend()

      }
    },
    //获取车辆数据
    getEchartsTrend(){
      this.XData = []
      this.yData3 = []
      this.yData2 = []
      this.yData1 = []

      // 获取当前日期
      let currentDate = new Date();
      // 获取前天日期
      let twoDaysAgo = new Date();
      twoDaysAgo.setDate(currentDate.getDate() - 2);
      // 获取大前天日期
      let threeDaysAgo = new Date();
      threeDaysAgo.setDate(currentDate.getDate() - 1);
      let ds = this.getCatdate(currentDate)//今天
      let ds1 = this.getCatdate(twoDaysAgo)//前天
      let ds2 = this.getCatdate(threeDaysAgo)//昨天

      let tunnelItems = null;
      if(!!this.tunnelItems.tunnelId){
        tunnelItems = this.tunnelItems
      }else{
        tunnelItems = this.tunnelLists[0]
      }
      // debugger
      console.log(this.catFilesModel.tunnelId)
      if(!!tunnelItems.tunnelId&&(this.catFilesModel.tunnelId==null||this.catFilesModel.tunnelId==""||this.catFilesModel.tunnelId=="undefined")){//首次
        // this.queryParamsLight.tunnelId = tunnelItems.tunnelId
        this.catFilesModel.tunnelId= tunnelItems.tunnelId
        this.catFilesModel.direction= "1"
        let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.catFilesModel.tunnelId)
        console.log(tunnel)
        // debugger
        let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.catFilesModel.direction,modeType:1}
        this.catListConfig(queryParams)
        // listConfig(queryParams).then((response) => {
        //   let rows = response.rows[0];
        //   this.catFilesModel.isStatus =  rows.isStatus
        //   this.catFilesModel.beforeLuminance =  rows.beforeLuminance
        //
        //   let jsonArray = JSON.parse(rows.timeSlot);
        //   console.log(jsonArray)
        //   debugger
        //   for (let i = 0; i < jsonArray.length; i++) {
        //
        //   }
        //   this.formItems
        //   // this.total = response.total;
        //   // this.loading = false;
        // });
        // this.$forceUpdate()
      }

      let json = {
        eqDirection:"hour",
        tunnelId: this.catFilesModel.tunnelId,
        holes: this.catFilesModel.direction
      }
      //今天
      analysisDataByTime(this.addDateRange(json, ds)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            const randomNumber = Math.floor(Math.random() * 70);
            this.XData.push(response.data[i].date)
            // this.yData3.push(response.data[i].byVehicelNum)
            this.yData3.push(randomNumber)
          }
        }
      });
      //昨天
      analysisDataByTime(this.addDateRange(json, ds2)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            const randomNumber = Math.floor(Math.random() * 70);
            // this.yData2.push(response.data[i].byVehicelNum)
            this.yData2.push(randomNumber)
          }
        }
      });
      //前天
      analysisDataByTime(this.addDateRange(json, ds1)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            // this.yData1.push(response.data[i].byVehicelNum)
            const randomNumber = Math.floor(Math.random() * 70);
            this.yData1.push(randomNumber)
          }

        }
      });
      setTimeout(() => {
        this.$nextTick(() => {
          //获取车辆数据
          this.initCatChart();
        });
      }, 500);
    },
    //光照下修比例保存
    submitlightForm(){
      // debugger
      console.log(this.lightFilesModel)
      //模式1 车辆 0光强
      this.lightFilesModel.modeType = 0
      if (!!this.lightFilesModel.id) {
        updateConfig(this.lightFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
        });
      } else {
        addConfig(this.lightFilesModel).then((response) => {
          // debugger
          this.lightFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
        });
      }
    },
    //刷新光照图表
    refreshlightForm(){
      console.log(this.lightFilesModel)
      this.getEchartsData()
    },
    //刷新车辆
    catHandleSave(){
      //获取车辆数据
      this.getEchartsTrend()
    }
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
      handler(newValue, oldValue){
        // debugger
        this.visibleSync = !this.visibleSync
        //查询隧道列表
        this.getTunnels()
        //获取所有定时控制数据
        this.getTimingAll()
        //获取光亮的
        this.getEchartsData()
        // this.initChart();

      }
    },
    tabRadio: {
      handler(newValue, oldValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          this.$nextTick(() => {
            // this.initChart1();
          });
        }
      },
    },
    tunnelItem: {
      handler(newValue, oldValue) {
        if (newValue) {
          this.tunnelItems = newValue
        }
      },
    },
    tunnelList: {
      handler(newValue, oldValue) {
          this.tunnelLists = newValue
      },
    },
  }
}
</script>

<style lang="scss">
/* 设备含义中的table*/
.explain-table {
  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  .el-table::before {
    height: 0;
  }

  .el-table--mini td {
    padding: 2px 0;
  }

  .el-table .cell.el-tooltip {
    padding-top: 5px;
  }

  .el-table__body-wrapper {
    background-color: #304156;
  }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: #455d79;
    border-radius: 3px;
  }
  .el-radio {
    width: 240px;
    height: 40px;
    line-height: 40px;
  }
}
.operationDiglog {
  ::v-deep .el-input-group__append {
    padding: 0;
    width: 60px;
    border-left: none !important;
    .el-button {
      height: 32px;
      border-top-right-radius: 3px !important;
      border-bottom-right-radius: 3px !important;
      border-top-left-radius: 0px !important;
      border-bottom-left-radius: 0px !important;
      // transform: translateX(20px);
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  .el-dialog .el-form {
    // padding: 15px !important;
    .el-form-item__content .el-button {
      width: 88px;
      height: 22px;
      border: none;
    }
    .el-form-item__content .el-button--mini {
      padding: 2px 15px !important;
    }
  }

  .el-dialog__body {

    padding: 0 15px !important;
    .el-col {
      padding: 0 !important;
    }
  }
  .el-table {
    margin-bottom: 20px;
  }
}
.charts-container {
  display: flex;
}

.chart {
  flex: 0 0 40%;
  height: 400px;
  margin-right: 10px;
}

.chartTow {
  flex: 0 0 40%;
  height: 400px;
}
.chartFunction {
  flex: 0 0 50%;
  height: 400px;
  margin-right: 10px;
}
.chartTowFunction {
  flex: 0 0 30%;
  height: 400px;
  margin-right: 10px;
}
::v-deep .el-radio-button--small .el-radio-button__inner {
  padding: 5px 10px !important;
  background: transparent;
  border: 1px solid transparent;
  background-color: red;
}
.tabsBorder{
  width: 100%;

}

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
.form-container {
  height: 180px;
  overflow: auto;
}

.scrollable-content {
  max-height: 180px;
  overflow-y: auto;
  padding: 10px;
}
.no-scroll {
  overflow: hidden;
}
</style>
