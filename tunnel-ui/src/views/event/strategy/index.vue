<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small" >
          <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="策略名称" prop="strategyName">
        <el-input
          v-model="queryParams.strategyName"
          placeholder="请输入策略名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="策略类型" prop="strategyType">
        <el-select v-model="queryParams.strategyType" placeholder="请选择策略类型" clearable size="small">
          <el-option
            v-for="dict in strategyTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
     <el-col :span="1.5">
       <el-button
         type="primary"
         icon="el-icon-plus"
         size="mini"
         @click="openInsertStrategy"
         v-hasPermi="['system:strategy:add']"
       >新建策略</el-button>
     </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="strategyList" @selection-change="handleSelectionChange"
          :header-cell-style="{'text-align':'center'}" max-height="610">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="策略ID" align="center" prop="id" /> -->
      <el-table-column label="隧道名称" align="center" prop="tunnels.tunnelName" />
      <el-table-column label="策略名称" align="center" prop="strategyName" />
      <el-table-column label="方向" align="center" prop="direction" :formatter="directionFormat" />
      <el-table-column label="策略类型" align="center" prop="strategyType" :formatter="strategyTypeFormat" />
      <el-table-column label="策略信息" align="left" prop="slist">
        <!-- <template slot-scope="scope">
          <div style="color: #8423e0;">{{scope.row.strategyInfo}}</div>
        </template> -->
        <template slot-scope="scope">
            <div  v-for="(item,index) in scope.row.slist" :key="index" >
              {{item}}
              <!-- <template v-if="item != null">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="viewStrategyInfo(scope.row,index)"
                >{{item}}</el-button>
                
              </template> -->
            </div>
        </template>

      </el-table-column>

      <el-table-column label="状态" align="center" prop="schedulerTime">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.strategyState"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="0"
            inactive-value="1"
            active-text="开启"
            inactive-text="关闭"
            @change="changeStrategyState(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-show="scope.row.strategyType == 0"
            size="mini"
            type="text"
            icon="el-icon-thumb"
            @click="handleController(scope.row)"
            v-hasPermi="['system:strategy:edit']"
          >手动控制</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:strategy:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:strategy:remove']"
          >删除</el-button>
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

  <el-dialog
    :title="drawerTitle"
    :visible.sync="drawer"
    :direction="direction"
    :before-close="handleClose" @close="strategyFormClose">
    <el-form ref="form1" :model="strategyForm" :rules="formDataValidator" label-width="100px" >
     <el-form-item label="隧道名称" prop="tunnelId">
       <el-select  style="width: 90%;" v-model="strategyForm.tunnelId" placeholder="请选择隧道" clearable 
       @change="changeTunnel">
         <el-option
             v-for="item in tunnelData"
             :key="item.tunnelId"
             :label="item.tunnelName"
             :value="item.tunnelId"/>
       </el-select>
     </el-form-item>

     <el-form-item label="策略名称" prop="strategyName">
       <el-input style="width: 90%;" v-model="strategyForm.strategyName" placeholder="请输入策略名称" />
     </el-form-item>

     <el-form-item label="策略类型" prop="strategyType">
       <el-radio-group v-model="strategyForm.strategyType">
         <el-radio border
           v-for="dict in insertStrategyTypeOptions"
           :key="dict.dictValue"
           :label="dict.dictValue"
           @change.native="getDeviceType"
         >{{dict.dictLabel}}</el-radio>
       </el-radio-group>
     </el-form-item>

     <template v-if="is_show">
      <!-- 手动控制 -->
      <el-row >
        <template v-if="strategyForm.strategyType == '0'">
          <el-col :span="8">
            <el-form-item label="设备类型">
              <el-select v-model="strategyForm.equipmentTypeId" placeholder="请选择设备类型" title="手动控制">
                <el-option  v-for="(item,index) in equipmentTypeData" :key="index" :label="item.typeName" :value="item.typeId" 
                  @change.native="eqTypeChange" @click.native="resetData" :disabled="item.disabled"/>
              </el-select>
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="8">
          <el-form-item label="方向">
            <el-select v-model="strategyForm.direction" placeholder="请选择设备方向" >
              <el-option v-for="(dict,index) in directionOptions" :key="index" :label="dict.dictLabel" 
              :value="dict.dictValue" @click.native="resetData"   @change.native="eqTypeChange(dict.dictValue)"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 手动控制下面 -->
      <template v-if="strategyForm.strategyType == 0">
        <el-form-item v-for="(items, index) in strategyForm.manualControl" :key="index">
          <el-select v-model="items.value" @change='changeSelect' multiple placeholder="请选择设备" style="width: 40%">
            <el-option
              v-for="(item) in equipmentData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId" :disabled="item.disabled"/>
            <el-option label='暂无数据' disabled value='' v-show="equipmentData.length < 1" />
          </el-select>
          <el-select v-model="items.state" placeholder="请选择设备需要执行的操作"
              style="width: 39%;margin-left:6%;" >
            <el-option
              v-for="item in manualControlStateList"
              :key="item.deviceState+1"
              :label="item.stateName"
              :value="item.deviceState"> 
            </el-option>
          </el-select>
          <el-button type="" icon="el-icon-delete" circle  @click="removeItem(items, index)" style="margin-left:2%;" ></el-button>
        </el-form-item>
      </template>
      <!-- 定时控制 -->
      <el-form-item v-show="strategyForm.strategyType == '1'" style="margin-top: -10px; margin-bottom:0px;">
        <cron  v-if="showCronBox" v-model.trim="strategyForm.schedulerTime" ref="cron" @changeValue='changeValue'></cron>
        <span style="color: #E6A23C; font-size: 12px;" >corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份</span>
      </el-form-item>
      <el-form-item v-show="strategyForm.strategyType == '1'" style="width: 90%;">
            <el-input v-model="strategyForm.schedulerTime" auto-complete="off">
                <el-button slot="append" v-if="!showCronBox" icon="el-icon-arrow-up" @click="showCronBox = true" title="打开图形配置" ></el-button>
                <el-button slot="append" v-else icon="el-icon-arrow-down" @click="showCronBox = false" title="关闭图形配置" ></el-button>
            </el-input>
      </el-form-item>

      <!-- 自动触发 -->
      <template v-if="strategyForm.strategyType == '2'">
        <el-form-item  label="触发器">
          <el-select v-model="strategyForm.triggers.deviceTypeId" placeholder="请选择设备类型" 
              style="width: 18%;" @change="slectEqName()">
            <el-option
              v-for="item in eqTypeList"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId">
            </el-option>
          </el-select>
          <el-select v-model="strategyForm.triggers.deviceId" placeholder="请选择设备名称"
              style="width: 18%;margin-left:2%;"  @change="selectDataItem()">
            <el-option
              v-for="item in deviceName"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId">
            </el-option>
          </el-select>
          <el-select v-model="strategyForm.triggers.elementId" placeholder="请选择数据项"
              style="width: 18%;margin-left:2%;" >
            <el-option
              v-for="item in dataItem"
              :key="item.id"
              :label="item.itemName"
              :value="item.id">
            </el-option>
          </el-select>
          <el-select v-model="strategyForm.triggers.comparePattern" 
              style="width: 12%;margin-left:2%;" >
            <el-option  
              v-for="item in symbol"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue">
            </el-option>
          </el-select>
        <el-input style="width: 18%;margin-left:2%;" v-model="strategyForm.triggers.compareValue" 
          placeholder="请输入阈值" />
        </el-form-item>
      </template>
      <!-- 自动触发下边 -->
      <template v-if="strategyForm.strategyType != 0">
        <el-form-item v-for="(dain, index) in strategyForm.autoControl" :key="index">  
            <el-form-item>
              <el-input @click.native="openEqDialog2($event,index)" style="width: 35%;margin-top:20px;"
                v-model="dain.typeName" placeholder="请点击选择控制类型" />
              <el-select v-model="dain.state" placeholder="请选择设备需要执行的操作"
                style="width: 39%;margin-left:6%;" >
                <el-option
                  v-for="(item,indx) in dain.eqStateList"
                  :key="item.deviceState"
                  :label="item.stateName"
                  :value="item.deviceState">
                </el-option>
              </el-select>
              <el-button type="" icon="el-icon-delete" circle  @click="removeItem(dain, index)" style="margin-left:2%;" ></el-button>
          </el-form-item>
        </el-form-item>
      </template>

      <el-form-item label="" style="" >
          <a href="#" @click="addItem" style="color: #1890ff;">+添加执行操作</a>
      </el-form-item>
      
      <el-form-item  style="text-align: center;text-align: center; width: 90%;">
          <el-button style="width: 30%;" type="primary" @click="submitStrategyForm" >提交</el-button>
          <el-button style="width: 30%;"  @click="strategyFormClose">取 消</el-button>
      </el-form-item>
    </template>
    </el-form>
  </el-dialog>

    <!-- 选择设备-对话框 定时控制里的弹窗-->
    <el-dialog :title="title" :visible.sync="chooseEq"  :close-on-click-modal="false" width="40%" style="height: 100%;" append-to-body>
      <el-form ref="eqForm" :model="strategyForm" :rules="rules" label-width="80px">
        <el-form-item label="设备类型" v-if="equipmentTypeData.length" :style="{height: viewStrategy? '2.75rem':'13.75rem',overflow: 'auto'}" prop="equipment_type">
          <el-radio-group v-model="eqForm.equipment_type" @change.native="eqTypeChange">
              <el-radio-button :label="item.typeId" v-for="item in equipmentTypeData" :key="item.typeId"  style="margin: 3px;">
                {{item.typeName}}
              </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备类型" v-else>
          <span v-if="!equipmentTypeData.length">暂无设备类型</span>
        </el-form-item>
        <el-form-item label="选择设备" :style="{height:'300px'}" prop="equipments">
          <el-select v-model="eqForm.equipments" :disabled="viewStrategy"  
            multiple placeholder="请选择设备" style="width: 90%">
            <el-option
              v-for="item in equipmentData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
              :disabled="item.disabled"/>
            <el-option label='暂无数据' disabled value='' v-show="equipmentData.length < 1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="viewStrategy==false" type="primary" :loading="submitChooseEqFormLoading" 
          @click.native="submitChooseEqForm">确定</el-button>
        <el-button @click="cancelChooseEq">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script src="../../../cronStrue/dist/cronstrue.min.js" type="text/javascript"></script>
<script src="../../../cronStrue/dist/cronstrue-i18n.min.js" type="text/javascript"></script>


<script>
import { listStrategy, getStrategy, delStrategy,updateState, addStrategy, updateStrategy, addStrategysInfo, updateStrategysInfo, getGuid, handleStrategy } from "@/api/event/strategy";
import { listRl, addRl} from "@/api/event/strategyRl";
import { listType,listHasType ,autoEqTypeList,getStateTypeId,getTriggersByRelateId} from "@/api/equipment/type/api";
import { listItem } from "@/api/equipment/eqTypeItem/item";
import { listDevices } from "@/api/equipment/eqlist/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
// import { delStrategy } from "@/api/event/strategy";
import { listEqTypeStateIsControl } from "@/api/equipment/eqTypeState/api";
import { addJob, updateJob, delJob ,getJobByRelationId } from "@/api/monitor/job";
import cron from '../../../components/cron/cron.vue';
import EasyCron from '../../../components/easy-cron/index';
import InputCron from '../../../components/easy-cron/input-cron';
import CronValidator from '../../../components/easy-cron/validator';

export default {
  name: "Strategy",
   model: {
    prop: 'cronValue',
    event: 'change'
  },
  components: {
      cron,
      EasyCron,
      InputCron
    },
  data() {
    return {
      index:0,
      manualControlStateList:[],//当前选择设备状态选项
      strategyForm:{
        manualControl:[
          {value:'',state:''}
        ],//手动控制数组
        autoControl:[{
          value:'',
          state:'',
          type:'',
        }],
        equipmentTypeId: '',
        strategyType: null,//策略类型
        tunnelId:null,//隧道id
        strategyName:null,//策略名称
        equipments:[],//所选设备
        schedulerTime:null,
        direction: '',// 方向
        triggers:{
          deviceTypeId:'',//设备类型
          deviceId:'',
          elementId:'',//设备数据项
          comparePattern:'',//比较的符号
          compareValue:'',//比较符号
        },
        eqState1:'',
        //策略关系表
        equipmentStates:[],//动态策略所有选择状态
        jobRelationId:null,//定时任务Uid
        startTime: '',// 简单模式开始时间
        endTime: '',// 简单模式开始时间
        automaticEqType:'',//自动触发时设备类型
        automaticEqName:'',//自动触发时设备名称
        automaticSymbol:'',//自动触发时符号 >= <= ..
        threshold:'',//自动触发时阈值输入框
        autodataItem:"",//自动触发设备类型数据项
      },
      guid:'',
      is_show:false,
      changeVal:false,
	    saveDevices:'',
      //drawer遮罩层
      dloading:false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组
      rlIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 控制策略表格数据
      strategyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否显示 选择设备弹出层
      chooseEq: false,
      submitChooseEqFormLoading: false,
      //是否显示，选择时间弹出框
      chooseScheduleTimeVb:false,
      // 设备方向字典
      directionOptions: [
      ],
      // 策略类型字典
      strategyTypeOptions: [],
      // 策略类型字典
      insertStrategyTypeOptions: [],
      // 编辑策略选中rlId
      currentId : null,
      // 添加/编辑标志 add添加，edit 编辑
      sink : 'add',
      checked : false,
      showCronBox: false,
      viewStrategy: false,
	    editCronValue: this.cronValue,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        strategyName: null,
        strategyType: null,
        strategyInfo: null,
        schedulerTime: null,
        jobTime:null,
      },
      queryEqParams:{
        eqType:null
      },
      queryEqTypeParams:{
        isControl:1
      },
      //查询设备状态params
      queryEqStateParams:{
        stateTypeId:null
      },
      // 表单参数
      eqForm: {
        equipment_type:null,//设备类型ID
        direction: '',//方向
        index:null,//当前设置的
        equipments:[],//设备名称
      },
/*      scheduleForm:{
        cronExpression:'',
      }, */
      // 表单校验
      rules: {},
      drawer: false,
      drawerTitle:null,//drawer标题
      direction: 'rtl',
      showCronBox: false,
      equipmentTypeData:{},
      equipmentData:[],
      tunnelData:{},
      //选中设备类型 名称
      eqTypeName :null,
      array:[],
      oneId:[],//设备类型名称数组
      oneId1:'',
      oneIdEqTypeId:[],//设备类型id数组
      eqState:[],//设备状态数组
      eqState1:[],//新增的状态设备数组
      eqState3:[],//自动控制设备状态数组
      twoId:[],//设备状态存储
      twoId2:[],//手动控制项的
      twoId1:[],
      twoName:[],//设备状态名称
      list:[{"oneId":'', "twoId":'', "twoName":''}],
      list2:[{"twoId":'', "twoName":''}],
      selectedList:[],//存储每次option选中的集合
      warningOptions :[
        {
          value: "1",
          id: 1,
          label: "CO报警时",
        },
        {
          value: "2",
          id: 2,
          label: "NO报警时",
        },
        {
          value: "3",
          id: 3,
          label: "火灾报警时",
        },
        {
          value: "4",
          id: 4,
          label: "洞内外光线差值超过标准时",
        },
      ],
      model: '2',// 复杂模式
      msg: 'Cron表达式测试页面',
      exeStartTime: '',
      formDataValidator: {
        tunnelId: [{required: true, message: '请选择隧道', trigger: 'blur'}],
        strategyName: [{required: true, message: '请输入策略名称', trigger: 'blur'}],
        strategyType: [{required: true, trigger: 'blur'}],
        direction:[{required: true,message: '请选择方向',trigger: 'blur'}],
      },
      eqListName:[],
      eqTypeList:[],//设备类型
      deviceName:[],//设备名称
      dataItem:[],//设备数据类型项
      symbol:[],//符号
      // automaticEqType:[] //自动触发设备类型数组
    }
  },
  created() {
    this.getList();//查询控制策略
    this.getTunnels();//获取隧道
    this.getDicts("sd_strategy_type").then(response => {
      this.strategyTypeOptions = response.data;
      this.insertStrategyTypeOptions = response.data;
      console.log(this.insertStrategyTypeOptions,'this.insertStrategyTypeOptions');
    });
    this.getDicts("sd_trigger_compare_type").then(response => {
      this.symbol=response.data
    });
    this.getDicts("sd_direction").then(response => {
      response.data.forEach((item)=>{
        this.directionOptions.push(item)
      })
    });
    // this.getEquipmentType();
  },
  methods: {
    changeStrategyState(row){
      let data = {invokeTarget:row.jobRelationId,status:row.strategyState};
      updateState(data).then((result)=>{
        this.$modal.msgError(res.msg);
      })
    },
    // dialogChange(){
    //   console.log('选择了');
    //   this.strategyForm.autoControl[index].state = this.
    // },
    // 改变策略类型
    changeStrategyType(){
      this.resetForm()
    },
    resetForm(){
      this.eqForm =  {
        equipment_type:null,//设备类型ID
        direction: '',//方向
        index:null,//当前设置的
      }
    },
    getDeviceType(){
      if(!this.strategyForm.tunnelId){
        return this.$modal.msgError("请选择隧道");
      }
      if(!this.strategyForm.strategyName){
        return this.$modal.msgError("请填写策略名称");
      }
      if(this.strategyForm.strategyType == '2'){
        autoEqTypeList({isControl: 1}).then(res=>{
          this.eqTypeList = res.rows;
        })
      }
      this.is_show = true;
    },
    //选择定时控制方向时
    slectDirectionTime(id){
      this.oneId=[]
      this.twoId=[]
    },
    // 选择方向时
    slectDirection(dictId){
      if(Number(this.strategyForm.strategyType) ==  0){
        if(!this.strategyForm.equipmentType){
          return this.$modal.msgError("请选择设备类型");
        }
      }
      //给设备名称重新赋值
      let params = {
        eqType:this.strategyForm.equipmentType,
        eqTunnelId:this.strategyForm.tunnelId,
        eqDirection:this.strategyForm.direction,
      }
      listDevices(params).then(res=>{
        this.equipmentData = res.rows
      })
    },
    //重置方法
    rest(){
      this.strategyForm.triggers.deviceId = null;
      this.strategyForm.triggers.elementId = null;
      this.deviceName = [];
      this.dataItem = [];
    },
    //点击 自动触发设备类型 
    slectEqName(){
      this.rest();
      // 选择设备名称赋值
      listDevices({
        eqType:this.strategyForm.triggers.deviceTypeId,
        eqTunnelId:this.strategyForm.tunnelId,
        eqDirection:this.strategyForm.direction
      }).then(res=>{
        this.equipmentData=res.rows
        this.deviceName=res.rows
      })   
     //给设备数据项赋值
      listItem({deviceTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
       this.dataItem=res.rows
      })
    },
    // 设备名称查询设备数据类型项
    selectDataItem(e){
      listItem({deviceTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
       this.dataItem=res.rows
      })
    },
    //选择隧道
    changeTunnel(){
      if(this.strategyForm.strategyType == 2){
        autoEqTypeList().then(res=>{
          this.eqTypeList = res.rows
          console.log(this.eqTypeList,'获取所有设备列表')
        })
      }
    },
    selectAll() {
      if (this.eqForm.equipments.length < this.equipmentData.length) {
        this.eqForm.equipments = []
        this.equipmentData.map((item) => {
          this.eqForm.equipments.push(item.eqId)
        })
        // this.eqForm.equipments.remove('全选')
      } else {
        this.eqForm.equipments = []
      }
    },
    selectDirectionAll(val){
      this.eqForm.equipments = []
      this.equipmentData.map((item) => {
        if(val==item.eqDirection){
            this.eqForm.equipments.push(item.eqId)
        }
      })
    },
    handleController(row){
      this.$confirm('是否确认执行！', "警告", {
        confirmButtonText: "执行",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$modal.msgSuccess("手动控制中.......");
        return handleStrategy(row.id);
      }).then(() => {
        this.$modal.msgSuccess("手动控制成功");
      })
      .catch(_ => {});
    },
    // 添加执行操作
    addItem(){
      this.index = this.index+1;
      console.log(this.index)
      let addItemBoolean = true;
      let strategyType = this.strategyForm.strategyType;
      switch (strategyType) {
        case '0' :
        this.strategyForm.manualControl.push({         
          value:'',
          state:''       
        })
        var currentList = this.strategyForm.manualControl;
        console.log(currentList,'123213')
        let newData = [];
        for(let i = 0;i < currentList.length;i++){
          newData += currentList[i].value + ','
        }
        newData = newData.split(',');
        let data = this.equipmentData;
        console.log(newData)
        for(let i = 0;i < data.length;i++){
          for(let z = 0;z < newData.length;z++){
            if(data[i].eqId == newData[z]){
              data[i].disabled = true;
            }
          }
        }
        this.equipmentData = data;
        console.log(this.equipmentData);
        this.$forceUpdate();
        break;

        case '1' :
        this.strategyForm.autoControl.push({         
          value:'',
          state:'',
          type:'',    
        })
        break;

        case '2' :
        this.strategyForm.autoControl.push({         
          value:'',
          state:'',
          type:'',    
        })
        break;
      }
    },
    removeItem(it, index){
      if(this.strategyForm.strategyType == 0){
        if(this.strategyForm.manualControl.length == 1){
          return this.$modal.msgSuccess('请至少保留一行');
        }
        this.strategyForm.manualControl.splice(index,1)
      }else{
        if(this.strategyForm.autoControl.length == 1){
          return this.$modal.msgSuccess('请至少保留一行');
        }
        this.strategyForm.autoControl.splice(index,1)
      }
    },
    resetData(){
      if(this.sink == 'edit'){
        if(this.strategyForm.strategyType == '0'){
          this.strategyForm.manualControl = [{value:'',state:''}];
        }else{
          this.strategyForm.autoControl = [{value:'',state:'',type:''}];
        }
      }else{
        if(this.strategyForm.strategyType == '0'){
          this.eqTypeChange();
        }
      }
    },
    //查询设备控制状态和设备列表
    eqTypeChange() {
      if(this.chooseEq){
        console.log(this.strategyForm.manualControl);
      }
      if(Number(this.strategyForm.strategyType) == 0){
        console.log(this.sink,'修改');

        var stateTypeId = this.strategyForm.equipmentTypeId
        var direction  =  this.strategyForm.direction
        let params = {'stateTypeId':stateTypeId,'direction':direction,'isControl': 1}
        this.listEqTypeStateIsControl(params)
        this.listDevices();
      }else{
        if(this.eqForm.equipments.length > 1){//二次弹窗内更改设备类型清空设备列表
          this.eqForm.equipments = ''
        }
        console.log(this.strategyForm.strategyType,'this.strategyForm.strategyTypethis.strategyForm.strategyType')
        this.listDevices();
      }
    },
    // public查询设备列表
    listDevices(){
      if(Number(this.strategyForm.strategyType) == 0){
        var eqType = this.strategyForm.equipmentTypeId;
      }else{ 
        var eqType = this.eqForm.equipment_type;
      }
      listDevices({
        eqType:eqType,
        eqTunnelId:this.strategyForm.tunnelId,
        eqDirection:this.strategyForm.direction,
      }).then(res=>{
        let data = res.rows;
        if(this.strategyForm.strategyType != '0'){
          if(this.chooseEq && this.strategyForm.autoControl.length > 1){
            var currentList = this.strategyForm.autoControl;
            let newData = [];
            for(let i = 0;i < currentList.length;i++){
              newData += currentList[i].value + ','
            }
            // console.log(newData);
            newData = newData.split(',');
            for(let i = 0;i < data.length;i++){
              for(let z = 0;z < newData.length;z++){
                if(data[i].eqId == newData[z]){
                  console.log(data[i].eqId);
                  console.log(newData[z])
                  data[i].disabled = true;
                }
              }
            }
            this.equipmentData = data;
            console.log(this.equipmentData);
            this.$forceUpdate();
          }
        }
        this.equipmentData = data;
        this.$forceUpdate();
      })
    },
    //public 查询设备可控状态
    listEqTypeStateIsControl(params){
      listEqTypeStateIsControl(params).then(response => {
        this.manualControlStateList = response.rows
      })
    },
    viewStrategyInfo(row,index){
      if(row.strategyType=="1"){
        if(index==0){
          return;
        }else{
          index--
        }
      }
      this.eqForm.equipments=[]
      listRl({strategyId: row.id}).then(response => {
        let strategyRl=response.rows[index];
        let strategyRlArr=strategyRl.equipments.split(",");
        this.eqForm.equipment_type=strategyRl.eqTypeId
        this.eqForm.eqType=strategyRl.eqTypeId
        this.eqForm.sdName=row.tunnels.tunnelName
        this.eqForm.clName=row.strategyName
        this.eqForm.clInfoName=row.slist[row.strategyType=="1" ? index+=1:index]
        listDevices({eqType:this.eqForm.equipment_type,eqTunnelId:row.tunnelId}).then(response => {
          let a = response.rows;
          if(response.rows!=null && response.rows.length>0){
            for(let i=0;i < a.length;i++){
              if(a[i].remark==null){
                a[i].remark = "无备注"
              }
            }
          }
          this.equipmentData = a
        });
        for (var i = 0; i < strategyRlArr.length; i++) {
          this.eqForm.equipments.push(strategyRlArr[i])
        }
        this.title = "策略信息";
        this.chooseEq=true;
        this.viewStrategy=true
      })
    },
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      listType(this.queryEqTypeParams).then(response => {
        this.equipmentTypeData = response.rows;
      });
    },
    getEquipmentHasType() {
      listHasType(this.strategyForm.tunnelId,).then(response => {
        this.equipmentTypeData = response.rows;
        console.log(this.equipmentTypeData,'==============')
      });
    },
    /** 根据设备类型查询设备列表 */
    getEquipmentByType(id) {
      listDevices().then(response => {
        this.equipmentTypeData = response.rows;
      });
    },
    //关闭drawer
    handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            this.currentId = null;
            this.resetStrategyInfo();
            done();
          })
          .catch(_ => {});
     },
    /** 查询控制策略列表 */
    getList() {
      this.loading = true;
      listStrategy(this.queryParams).then(response => {
        this.strategyList = response.rows;
        console.log(this.strategyList,'strategyList')
        this.total = response.total;
        this.loading = false;
      });
    },
    //模式中转换
    // 策略类型字典翻译
    strategyTypeFormat(row, column) {
      return this.selectDictLabel(this.strategyTypeOptions, row.strategyType);
    },
    // 设备方向字典翻译
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionOptions, row.direction);
    },
    //选择设备提交按钮
    submitChooseEqForm() {
      // 1.赋值 2.比对之前的是否重复   3.根据设备类型查询控制状态  66666
        console.log(this.eqForm.equipments);
        let index = this.strategyForm.autoControl.length - 1;//求最后一位
        this.strategyForm.autoControl[index].value = this.eqForm.equipments;
        this.strategyForm.autoControl[index].type = this.eqForm.equipment_type;
        this.equipmentTypeData.forEach(item=>{
          if(item.typeId == this.eqForm.equipment_type){
            this.strategyForm.autoControl[index].typeName = item.typeName;
          }
        })
        console.log(this.strategyForm.autoControl,'当前选择设备');
        this.chooseEq = false;//关闭弹窗
        this.index = 0;
        listEqTypeStateIsControl({
          stateTypeId:this.eqForm.equipment_type,
          isControl:1,
        }).then(response => {
          console.log(response,'设备状态');
          this.strategyForm.autoControl[index].eqStateList = response.rows
          this.$forceUpdate();
        })
    },
    // 选择设备取消按钮
    cancelChooseEq() {
      this.chooseEq = false;
      this.index = 0;
      this.resetEqForm();
    },
    resetEqForm() {
      this.eqForm = {
        equipments : [],
        equipment_type : null,
      };
      this.resetForm("eqForm");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        strategyName: null,
        strategyType: null,
        strategyInfo: null,
        schedulerTime: null,
        jobTime:null,
      };
      this.handleQuery();
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.rlIds = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /*模式选择*/
    submit1() {
      this.model = '1'
      return this.model
    },
    submit2(){
      this.model = '2'
      return this.model
    },
    openScheDialog(){
      this.chooseScheduleTimeVb = true;
      this.title = "设定时间";
      //this.getEquipmentType();
    },
    // 设定时间取消按钮
    cancelChooseTime() {
      this.chooseScheduleTimeVb = false;
    },
    openEqDialog2(event,index){
      this.chooseEq = true;
      this.title = "选择设备";
      this.getEquipmentHasType();
    },
    //打开drawer
    openInsertStrategy() {
      this.strategyForm = {
        manualControl:[
          {value:'',state:''}
        ],//手动控制数组
        autoControl:[{
          value:'',
          state:'',
          type:'',
        }],
        equipmentTypeId: '',
        strategyType: null,//策略类型
        tunnelId:null,//隧道id
        strategyName:null,//策略名称
        equipments:[],//所选设备
        schedulerTime:null,
        direction: '',// 方向
        triggers:{
          deviceTypeId:'',//设备类型
          deviceId:'',
          elementId:'',//设备数据项
          comparePattern:'',//比较的符号
          compareValue:'',//比较符号
        },
        eqState1:'',
        //策略关系表
        equipmentStates:[],//动态策略所有选择状态
        jobRelationId:null,//定时任务Uid
        startTime: '',// 简单模式开始时间
        endTime: '',// 简单模式开始时间
        automaticEqType:'',//自动触发时设备类型
        automaticEqName:'',//自动触发时设备名称
        automaticSymbol:'',//自动触发时符号 >= <= ..
        threshold:'',//自动触发时阈值输入框
        autodataItem:"",//自动触发设备类型数据项
      },
      this.eqForm = {
        equipment_type:null,//设备类型ID
        direction: '',//方向
        index:null,//当前设置的
        equipments:[],//设备名称
      },
      this.getEquipmentType();
      this.drawerTitle = "新增策略";
      this.sink = "add";
      //清空 定时弹窗里的设备名称数组
      this.eqForm.equipments = []
      this.drawer = true;
      this.viewStrategy=false;
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.is_show = true;
      //获取设备
      autoEqTypeList().then(res=>{
        this.eqTypeList = res.rows;
      })
      await listType(this.queryEqTypeParams).then(response => {
        this.equipmentTypeData = response.rows;
      });
      this.viewStrategy=false;
      this.currentId = row.id;
      this.sink = "edit";
      const id = row.id || this.ids
      // this.strategyForm.strategyType = "0";
      getStrategy(id).then(response => {
        console.log(response.data,'修改按钮操作')
        let data = response.data;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.drawerTitle = "编辑策略";
        this.drawer = true;
        this.strategyForm.jobRelationId = data.jobRelationId;
        listItem({deviceTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
          this.dataItem = res.rows
        })
        // 如果是2.则获取触发器数据
        if(this.strategyForm.strategyType == '2'){
          // 获取触发器的数据
          getTriggersByRelateId({relateId:response.data.id}).then(res=>{
            this.strategyForm.triggers.comparePattern = res.data.comparePattern
            this.strategyForm.triggers.compareValue = res.data.compareValue
            this.strategyForm.triggers.deviceTypeId = res.data.deviceTypeId
            this.strategyForm.triggers.elementId = res.data.elementId
            this.strategyForm.triggers.deviceId = res.data.deviceId
          })
        }
        if(this.strategyForm.strategyType ==1 ){
          this.strategyForm.schedulerTime = data.schedulerTime;
        }
        listRl({strategyId: row.id}).then(response => {
          console.log(response,'asdadas')
          this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
          console.log(this.strategyForm.equipmentTypeId,'this.strategyForm.equipmentTypeId');
          if(this.strategyForm.strategyType == '0'){
            this.eqTypeChange();
          }
          listDevices({eqType:response.rows[0].eqTypeId,eqTunnelId: this.strategyForm.tunnelId}).then(res=>{
            this.equipmentData = res.rows
            console.log(this.equipmentData,'this.equipmentData')
          })
          if(this.strategyForm.strategyType == '0'){
            this.strategyForm.manualControl = response.rows;
          }else{
            this.strategyForm.autoControl = response.rows;
          }
          for ( var i = 0; i < response.rows.length; i++){
            var attr = response.rows[i];
            if(this.strategyForm.strategyType =='0'){
              this.strategyForm.manualControl[i].value = attr.equipments.split(',')
              this.strategyForm.manualControl[i].state = attr.state
              this.strategyForm.equipmentTypeId = Number(attr.eqTypeId)
            }else{
              if(this.strategyForm.autoControl[i].value){
                this.strategyForm.autoControl[i].value = attr.equipments.split(',')
              }else{
                this.strategyForm.autoControl[i].value = attr.equipments.split(',')
                this.strategyForm.autoControl[i].eqStateList = attr.eqStateList
                this.strategyForm.autoControl[i].state = attr.state
                this.strategyForm.autoControl[i].type = attr.eqTypeId
                this.strategyForm.autoControl[i].typeName = attr.eqType.typeName
              }
            }
          }
          console.log(this.strategyForm.autoControl)
        })
      });
    },
    //状态码转换状态名称
    turnToStateName(eqState, index, state){
      let obj = {};
      obj = eqState.find((item)=>{
          return item.deviceState === state;
      });
      this.list[index].twoName = obj.stateName;
    },
    resetStrategyInfo(){
      this.oneId = [];
      this.oneIdEqTypeId=[];
      this.eqState=[];
      this.twoId=[];
      this.list=[{"oneId":'',"twoId":''}];
      this.selectedList=[];//存储每次option选中的集合
      this.strategyForm.strategyType=null;
      this.strategyForm.tunnelId=null;
      this.strategyForm.strategyName=null;
      this.strategyForm.equipments=[];//设备名称
      this.strategyForm.equipmentstr=[];
      this.strategyForm.equipmentStates=[];
      this.strategyForm.schedulerTime = null;
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const rlIds = row.id || this.rlIds;
      const jobRelationId = row.jobRelationId;
      this.$confirm('是否确认删除？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          if(row.jobRelationId !=null){
            console.log(row,'row.jobRelationIdrow.jobRelationId')
            // delJob(jobRelationId).then(response => {
            // });
          }
        delStrategy(ids).then(res=>{
          console.log(res,'resres')
        });
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
        this.model = '1'
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/strategy/export', {
        ...this.queryParams
      }, `system_strategy.xlsx`)
    },
    /** 提交按钮 */
    async submitStrategyForm() {
      console.log(this.strategyForm,'要提交数据');
      // return
      var str = ""
      let type = this.strategyForm.strategyType;
      //手动控制
      // if(type == 0){
      //   console.log(this.strategyForm.addOperation[0].equipments.length)
      //   if(this.strategyForm.addOperation[0].equipments.length == 0){
      //     return this.$modal.msgError("请选择设备并添加执行操作");
      //   }
      // }
      // 自动触发
      if(type == 2){
        if(!this.strategyForm.triggers.deviceTypeId){
          return this.$modal.msgError("请选择设备类型")
        }
        if(!this.strategyForm.triggers.deviceId){
          return this.$modal.msgError("请选择设备名称")
        }
        if(!this.strategyForm.triggers.elementId){
          return this.$modal.msgError("请选择数据项")
        }
        if(!this.strategyForm.triggers.comparePattern){
          return this.$modal.msgError("请选择运算符")
        }
        if(!this.strategyForm.triggers.compareValue){
          return this.$modal.msgError("请输入阈值")
        }
      }
      if(this.strategyForm.strategyType == 1){
        if(this.strategyForm.schedulerTime==null || !this.strategyForm.schedulerTime){
          this.$modal.msgError("未设置定时！")
          return;
        }
      }
      if(this.sink == 'edit'){
        this.updateStrategyInfoData();
      }else{
        this.addStrategyInfoData();
      }
    },
    // 编辑操作
    async updateStrategyInfoData(){
      await getGuid().then(res=>{
        this.strategyForm.jobRelationId = res
        this.strategyForm.id = this.currentId
      });
      let params = this.strategyForm;
      updateStrategy(params).then(res=>{
        this.$modal.msgSuccess("修改策略成功");
        this.dloading=false;
        this.drawer=false;
        this.getList();
      })
    },
    // 提交保存方法
    async addStrategyInfoData(){
      await getGuid().then(res=>{
        this.strategyForm.jobRelationId = res
      });
      let params = this.strategyForm;
      addStrategysInfo(params).then(res=>{
        this.$modal.msgSuccess("新增策略成功");
        this.dloading=false;
        this.drawer=false;
        this.getList();
        setTimeout(() => {
          this.resetStrategyInfo();
        }, 400);
      })
    },
    /** 添加定时任务 */
    addJobData(guid,str){
    //  if(this.model == '2'){
        addJob({jobName:this.strategyForm.strategyName,//任务名称
          invokeTarget:"ryTask.strategyParams('"+guid+"')",//调用目标字符串
          cronExpression:this.strategyForm.schedulerTime,//corn表达式
          misfirePolicy:'1',//计划执行错误策略（1立即执行 2执行一次 3放弃执行）
          concurrent:'0',//'是否并发执行（0允许 1禁止）'
          status:'0',//状态（0正常 1暂停）'
          relationId:this.guid
          }).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("新增任务成功");
          }else{
            this.$modal.msgError("新增失败，请删除重建控制策略！");
          }
        })
        // return
     /* }addJob({jobName:this.strategyForm.strategyName,//任务名称
          invokeTarget:"ryTask.strategyParams('"+guid+"')",//调用目标字符串
          cronExpression:str,//corn表达式
          misfirePolicy:'3',//计划执行错误策略（1立即执行 2执行一次 3放弃执行）
          concurrent:'0',//'是否并发执行（0允许 1禁止）'
          status:'0',//状态（0正常 1暂停）'
          relationId:guid
          }).then(response => {
          if (response.code === 200) {
              this.$modal.msgSuccess("新增任务成功");
           }else{
             this.$modal.msgError("新增失败，请删除重建控制策略！");
           }
      }) */
     },
    //关闭drawer
    strategyFormClose() {
      this.strategyForm = {
        manualControl:[
          {value:'',state:''}
        ],//手动控制数组
        autoControl:[{
          value:'',
          state:'',
          type:'',
        }],
        equipmentTypeId: '',
        strategyType: null,//策略类型
        tunnelId:null,//隧道id
        strategyName:null,//策略名称
        equipments:[],//所选设备
        schedulerTime:null,
        direction: '',// 方向
        triggers:{
          deviceTypeId:'',//设备类型
          deviceId:'',
          elementId:'',//设备数据项
          comparePattern:'',//比较的符号
          compareValue:'',//比较符号
        },
        eqState1:'',
        //策略关系表
        equipmentStates:[],//动态策略所有选择状态
        jobRelationId:null,//定时任务Uid
        startTime: '',// 简单模式开始时间
        endTime: '',// 简单模式开始时间
        automaticEqType:'',//自动触发时设备类型
        automaticEqName:'',//自动触发时设备名称
        automaticSymbol:'',//自动触发时符号 >= <= ..
        threshold:'',//自动触发时阈值输入框
        autodataItem:"",//自动触发设备类型数据项
      },
      this.resetStrategyInfo();
      this.currentId = null;
      this.drawer = false;
      this.model = '1';
      this.visitDate = [];
      this.$refs.form1.resetFields()
      if(this.changeVal == true){
        this.$refs.cron.checkClear()
        this.changeVal = false
      }
    },
    changeValue(value){
      this.changeVal = value
    }
  }
};
</script>
<style>
 .el-drawer__header{
       background: #dcdfe6;
       padding: 0 10px;
       height: 58px;
       font-size: 1.125rem;
  }
  .el-table .cell {
    white-space: pre-line;
  }
/* .el-drawer__title{
   width: 45%!important;
 } */
</style>
