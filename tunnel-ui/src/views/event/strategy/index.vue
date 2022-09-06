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
                <el-button v-if="scope.row.strategyType=='0' || index > 0"
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="viewStrategyInfo(scope.row,index)"
                >{{item}}</el-button>
                <span v-else>
                  {{item}}
                </span>
            </div>
        </template>

      </el-table-column>

      <!-- <el-table-column label="时间表" align="center" prop="schedulerTime" /> -->
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
       <el-select  style="width: 90%;" v-model="strategyForm.tunnelId" placeholder="请选择隧道" clearable @change="changeTunnel">
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
       <el-radio-group v-model="strategyForm.strategyType" >
         <el-radio border
           v-for="dict in insertStrategyTypeOptions"
           :key="dict.dictValue"
           :label="dict.dictValue"
           @click.native="getDeviceType"
         >{{dict.dictLabel}}</el-radio>
       </el-radio-group>
     </el-form-item>

     <template v-if="is_show">
      <el-form-item label="方向" v-if="strategyForm.strategyType === '1' " >
       <el-select v-model="strategyForm.direction" placeholder="请选择设备方向">
         <el-option v-for="(dict,index) in directionOptions" :key="index" :label="dict.dictLabel" :value="dict.dictValue"  @click.native="slectDirectionTime(dict.dictValue)" />
       </el-select>
     </el-form-item>
      <!-- 手动控制 -->
      <el-form-item label="设备类型" v-if="strategyForm.strategyType === '0' && sink=='edit'" >
        <el-select v-model="oneId1" placeholder="请选择设备类型" >
          <el-option  v-for="(item,index) in equipmentTypeData" :key="index" :label="item.typeName" :value="item.typeId" 
                      @click.native="eqTypeChange(item.typeId,item.typeName)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" v-if="strategyForm.strategyType === '0' && sink=='add' " >
        <el-select v-model="strategyForm.equipmentTypeId1" placeholder="请选择设备类型" >
          <el-option  v-for="(item,index) in equipmentTypeData" :key="index" :label="item.typeName" :value="item.typeId" 
                      @click.native="eqTypeChange(item.typeId,item.typeName)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="方向" v-if="strategyForm.strategyType == '0'">
        <el-select v-model="strategyForm.direction" placeholder="请选择设备方向" >
          <el-option v-for="(dict,index) in directionOptions" :key="index" :label="dict.dictLabel" :value="dict.dictValue"   @click.native="slectDirection(dict.dictValue)"/>
        </el-select>
      </el-form-item>
      <!-- 手动控制下面 -->
      <el-form-item v-for="(it, index) in strategyForm.addOperation" :key="index" v-if="strategyForm.strategyType == '0'">
        <el-select v-model="it.equipments" @change='changeSelect' :disabled="viewStrategy"  multiple placeholder="请选择设备" style="width: 40%">
          <el-option label='全选' value='全选' v-show="equipmentData.length > 1" @click.native='selectAll'></el-option>
          <el-option
              v-for="(item) in equipmentData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"/>
          <el-option label='暂无数据' disabled value='' v-show="equipmentData.length < 1" />
        </el-select>
        <el-select v-model="it.eqState" v-show="strategyForm.strategyType === '0' && sink=='edit' " placeholder="请选择设备需要执行的操作"
            style="width: 39%;margin-left:6%;" >
          <el-option
            v-for="item in eqState3"
            :key="item.deviceState+1"
            :label="item.stateName"
            :value="item.deviceState"> 
          </el-option>
        </el-select>       
        <el-select v-model="it.eqState" v-show="strategyForm.strategyType === '0' && sink=='add' " placeholder="请选择设备需要执行的操作" 
            style="width: 39%;margin-left:6%;" >
          <el-option
            v-for="item in eqState1"
            :key="item.deviceState"
            :label="item.stateName"
            :value="item.deviceState"> 
          </el-option>
        </el-select>
        <el-button type="" icon="el-icon-delete" circle  @click="removeItem(it, index)" style="margin-left:2%;" ></el-button>
      </el-form-item>
      
      <!-- 定时控制 -->
      <el-form-item v-show="strategyForm.strategyType === '1'" style="margin-top: -10px; margin-bottom:0px;">
        <cron  v-if="showCronBox" v-model.trim="strategyForm.schedulerTime" ref="cron" @changeValue='changeValue'></cron>
        <span style="color: #E6A23C; font-size: 12px;" >corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份</span>
      </el-form-item>
      <el-form-item v-show="strategyForm.strategyType === '1'" style="width: 90%;">
        <!-- <el-tabs style="width: 400px;" stretch v-model="model">
          <el-tab-pane label="简单模式" name="1">
            <label class="p-left" style="margin-left: 15px;">照明时序选择-每日</label>
            <div style="border: 1px #ccc solid;padding: 5px;">
              <el-row>
                <label class="p-left" style="margin-left: 15px;color: #ccc;">开启时间:</label>
                <el-time-picker type="time" v-model="strategyForm.startTime" value-format="HH:mm:ss" style="float: right;margin-right: 10px;" class="p-right" placeholder="选择开始时间" />
              </el-row>
              <el-row style="margin-top: 5px;">
                <label class="p-left" style="margin-left: 15px;color: #ccc;">关闭时间:</label>
                <el-time-picker type="time" v-model="strategyForm.endTime" value-format="HH:mm:ss" style="float: right;margin-right: 10px;" class="p-right" placeholder="选择关闭时间" />
              </el-row>
            </div>
          </el-tab-pane>
          <el-tab-pane label="指定模式" style="margin-bottom: 10px;" name="2">
            <input-cron type="text" v-model="strategyForm.schedulerTime" :exeStartTime="exeStartTime"
                :hideSecond="false" placeholder="请设定时间策略表达式"  style="width: 80%;" />
          </el-tab-pane>
        </el-tabs> -->
    <!--    <input-cron type="text" v-model="strategyForm.schedulerTime" :exeStartTime="exeStartTime"
            :hideSecond="false" placeholder="请设定时间策略表达式"  style="width: 80%;" /> -->

            <el-input v-model="strategyForm.schedulerTime" auto-complete="off">
                <el-button slot="append" v-if="!showCronBox" icon="el-icon-arrow-up" @click="showCronBox = true" title="打开图形配置" ></el-button>
                <el-button slot="append" v-else icon="el-icon-arrow-down" @click="showCronBox = false" title="关闭图形配置" ></el-button>
            </el-input>
      </el-form-item>

      
      <el-form-item v-for="(it, index) in list" :key="index" v-show="strategyForm.strategyType === '1'">
          <el-input @click.native="openEqDialog2($event,index)" style="width: 35%;" v-model="oneId[index]" placeholder="请点击选择控制类型" />
          <el-select v-model="twoId[index]" placeholder="请选择设备需要执行的操作" @change="saveTwoList($event, index)"
            style="width: 39%;margin-left:6%;" >
            <el-option
              v-for="item in eqState[index]"
              :key="item.deviceState"
              :label="item.stateName"
              :value="item.deviceState">
            </el-option>
          </el-select>
          <el-button type="" icon="el-icon-delete" circle  @click="removeItem(it, index)" style="margin-left:2%;" ></el-button>
      </el-form-item>
      
      <!-- 自动触发 -->
      <el-form-item  label="触发器" v-show="strategyForm.strategyType === '2'"   >
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
      <el-input style="width: 18%;margin-left:2%;" v-model="strategyForm.triggers.compareValue" placeholder="请输入阈值" />
      </el-form-item>
      <el-form-item label="方向" v-if="strategyForm.strategyType === '2'">
        <el-select v-model="strategyForm.direction" placeholder="请选择设备方向">
          <el-option v-for="dict in directionOptions" :key="dict.id" :label="dict.dictLabel" :value="dict.dictValue" @click.native="slectDirectionAuto(dict.dictValue)"/>
        </el-select>
      </el-form-item>
            <!-- 自动触发下边 -->
      <el-form-item v-for="(it, index) in strategyForm.autoAddOperation" :key="index" v-if="strategyForm.strategyType === '2'">  
        <el-select v-model="it.equipments" @change='changeSelect' :disabled="viewStrategy"  multiple placeholder="请选择设备" style="width: 40%">
          <!-- <el-option label='全选' value='全选' v-show="equipmentData.length > 1" @click.native='selectAll'></el-option> -->
          <el-option
              v-for="(item,ix) in equipmentData"
              :key="item.eqId+ix"
              :label="item.eqName"
              :value="item.eqId"/>
          <el-option label='暂无数据' disabled value='' v-show="equipmentData.length < 1" />
        </el-select>
        <el-select v-model="it.eqState" v-if="strategyForm.strategyType === '2' && sink=='edit' " placeholder="请选择设备需要执行的操作" @change="saveTwoList($event, index)"
            style="width: 39%;margin-left:6%;" >
          <el-option
            v-for="item in eqState3"
            :key="item.deviceState+1"
            :label="item.stateName"
            :value="item.deviceState"> 
          </el-option>
        </el-select>       
        <el-select v-model="it.eqState" v-show="strategyForm.strategyType === '2' && sink=='add' " placeholder="请选择设备需要执行的操作" @change="saveTwoList($event, index)"
            style="width: 39%;margin-left:6%;" >
          <el-option
            v-for="item in eqState1"
            :key="item.deviceState"
            :label="item.stateName"
            :value="item.deviceState"> 
          </el-option>
        </el-select>
        <el-button type="" icon="el-icon-delete" circle  @click="removeItem(it, index)" style="margin-left:2%;" ></el-button>
      </el-form-item>
        <!-- <el-form-item label="预警信息" v-show="strategyForm.strategyType === '2'">
          <el-select  style="width: 80%;"  v-model="queryParams.strategyType" placeholder="请选择预警信息" clearable size="small">
            <el-option
              v-for="item in warningOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item> -->
      <el-form-item label="" style="" >
          <a href="#" @click= "addItem" style="color: #1890ff;">+添加执行操作</a>
      </el-form-item>
      
      <el-form-item  style="text-align: center;text-align: center; width: 90%;">
          <el-button style="width: 30%;" type="primary" @click="submitStrategyForm" >保 存</el-button>
          <el-button style="width: 30%;"  @click="strategyFormClose">取 消</el-button>
      </el-form-item>
    </template>
    </el-form>
  </el-dialog>

    <!-- 选择设备-对话框 定时控制里的弹窗-->
    <el-dialog :title="title" :visible.sync="chooseEq"  :close-on-click-modal="false" width="40%" style="height: 100%;" append-to-body>
      <el-form ref="eqForm" :model="eqForm" :rules="rules" label-width="80px">
        <el-form-item label="" v-if="viewStrategy==true">
           隧道名称： {{eqForm.sdName}} <br>
           策略名称： {{eqForm.clName}} <br>
           策略信息： {{eqForm.clInfoName}}
        </el-form-item>
        <el-form-item label="设备类型" v-if="equipmentTypeData.length" :style="{height: viewStrategy? '2.75rem':'13.75rem',overflow: 'auto'}" prop="equipment_type">
          <el-radio-group v-model="eqForm.equipment_type" v-for="item in equipmentTypeData" :key="item.typeId" @click.native="eqTypeChange(item.typeId,item.typeName)" >
              <el-radio-button v-if="eqForm.eqType==item.typeId && viewStrategy!=false" :label="item.typeId" :disabled="viewStrategy" style="margin: 3px;" >{{item.typeName}}</el-radio-button>
              <el-radio-button v-if="viewStrategy==false" :label="item.typeId" :disabled="viewStrategy" style="margin: 3px;" >{{item.typeName}}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备类型" v-else>
          <span v-if="!equipmentTypeData.length">暂无设备类型</span>
        </el-form-item>
        <el-form-item label="选择设备" :style="{height:'300px'}" prop="equipments">
          <el-select v-model="eqForm.equipments" @change='changeSelect' :disabled="viewStrategy"  multiple placeholder="请选择设备" style="width: 90%">
            <!-- <el-option label='全选' value='全选' v-show="equipmentData.length > 1" @click.native='selectAll'></el-option> -->
            <el-option
                v-for="item in equipmentData"
                :key="item.eqId"
                :label="item.eqName"
                :value="item.eqId"/>
            <el-option label='暂无数据' disabled value='' v-show="equipmentData.length < 1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="viewStrategy==false" type="primary" :loading="submitChooseEqFormLoading" @click="submitChooseEqForm">确 定</el-button>
        <el-button @click="cancelChooseEq">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script src="../../../cronStrue/dist/cronstrue.min.js" type="text/javascript"></script>
<script src="../../../cronStrue/dist/cronstrue-i18n.min.js" type="text/javascript"></script>


<script>
import { listStrategy, getStrategy, delStrategy, addStrategy, updateStrategy, addStrategysInfo, updateStrategysInfo, getGuid, handleStrategy } from "@/api/event/strategy";
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
        tunnelId:null,
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
      strategyForm:{
        //策略信息表
        equipmentTypeId1:'',//新增设备id
        equipmentTypeId: [],
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
          }
      ,
      addOperation:[
         {
          equipments:[],//所选设备
          eqState:''
         }
      ],//手动控制添加执行操作数组
      autoAddOperation:[
         {
          equipments:[],//所选设备
          eqState:''
         }
      ],//自动触发控制添加执行操作数组
        eqState1:'',
        //策略关系表
        equipmentstr:[],//动态策略所有选则设备(名称)
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
/*      scheduleForm:{
        cronExpression:'',
      }, */
      // 表单校验
      rules: {
      },
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

      /* formData: {
        url: 'http://www.easysb.cn',
        qq: '34538980',
        cronValue1: '',
      }, */
      formDataValidator: {

        /* schedulerTime: [
          { validator: CronValidator }
        ], */
        tunnelId: [{required: true, message: '请选择隧道', trigger: 'blur'}],
        strategyName: [{required: true, message: '请输入策略名称', trigger: 'blur'}],
        strategyType: [{required: true, trigger: 'blur'}],
        // equipmentTypeId1: [{required: true, message: '请选择设备类型', trigger: 'change'}],
        // oneId1:[{required: true, message: '请选择设备类型', trigger: 'change'}],
        // this.strategyForm.strategyType:[]
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
    this.getList();
    this.getTunnels();
    this.getDicts("sd_strategy_type").then(response => {
      this.strategyTypeOptions = response.data;
      this.insertStrategyTypeOptions = response.data;
      console.log(response,"策略类型")
    });
    this.getDicts("sd_trigger_compare_type").then(response => {
      // this.strategyTypeOptions = response.data;
      // this.insertStrategyTypeOptions = response.data;
      // console.log(response,"dayuhaodeaksjkalkfj")
      this.symbol=response.data
    });
    this.getDicts("sd_direction").then(response => {
      // this.strategyTypeOptions = response.data;
      // this.insertStrategyTypeOptions = response.data;
      console.log(response,"dayuhaodeaksjkalkfj")
      // this.symbol=response.data
      response.data.forEach((item)=>{
        this.directionOptions.push(item)
      })
      
    });
    this.getEquipmentType();
  },
  methods: {
    getDeviceType(){
      console.log(this.strategyForm.tunnelId)
      if(!this.strategyForm.tunnelId){
        this.is_show = false
        return this.$modal.msgError("请首先选择隧道");
      }else{
        this.is_show = true
      }
      autoEqTypeList({isControl:1}).then(res=>{
        this.eqTypeList= res.rows
        console.log(res,'responseresponse')
      })
    },
    //自动触发的方向
    slectDirectionAuto(dictId){
        this.strategyForm.autoAddOperation=[
        {
          equipments:[],//所选设备
          eqState:''
         }
        ]       
          listDevices({eqType:this.strategyForm.triggers.deviceTypeId,eqTunnelId:this.strategyForm.tunnelId,eqDirection:dictId}).then(res=>{
        console.log(res,'oooooooooooooooooo')
        this.equipmentData=res.rows
        this.deviceName=res.rows
            })
    
      getStateTypeId({stateTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
                  console.log(res,'点击了设备类型')
                  if(this.sink=='edit' ){
                    this.eqState3=res.rows
                  }else if(this.sink=='add'){
                    this.eqState1=res.rows
                  }                  
                })
    },
    //选择定时控制方向时
    slectDirectionTime(id){
      this.oneId=[]
      this.twoId=[]
    },
    // 选择方向时
    slectDirection(dictId){
      console.log(dictId,'xuznl fangxiang')
      // this.equipmentData=[]
      // this.deviceName=[]
      this.strategyForm.addOperation=[
        {
          equipments:[],//所选设备
          eqState:''
         }
      ]
      //给设备名称重新赋值
      let params = {
        eqType:this.strategyForm.equipmentTypeId1,
        eqTunnelId:this.strategyForm.tunnelId,
        eqDirection:dictId
      }
      listDevices(params).then(res=>{
        this.equipmentData=res.rows
      }) 

      // //设备操作重新赋值
      // getStateTypeId({stateTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
      //             // console.log(res,'点击了设备类型')
      //             if(this.sink=='edit' ){
      //               this.eqState3=res.rows
      //             }else if(this.sink=='add'){
      //               this.eqState1=res.rows
      //             }                  
      //           })

    },
    //重置方法
    rest(){
      this.strategyForm.triggers.deviceId = null;
      // this.strategyForm.triggers.comparePattern=null
      // this.strategyForm.triggers.compareValue=null
      this.strategyForm.triggers.elementId = null;
      this.deviceName = [];
      this.dataItem = [];
    },
    //点击 自动触发设备类型 
    slectEqName(){
      this.rest();
      //清空设备名称
      //  this.strategyForm.triggers.deviceId=''
      //清空下边两个
      // this.strategyForm.autoAddOperation=[
      //    {
      //     equipments:[],//所选设备
      //     eqState:''
      //    }
      // ]
      // 选择设备名称赋值
      listDevices({eqType:this.strategyForm.triggers.deviceTypeId,eqTunnelId:this.strategyForm.tunnelId,eqDirection:this.strategyForm.direction}).then(res=>{
        console.log(res,'oooooooooooooooooo')
        this.equipmentData=res.rows
        this.deviceName=res.rows
      })  
      //给设备状态赋值
      getStateTypeId({stateTypeId:this.strategyForm.triggers.deviceTypeId}).then(res=>{
                  console.log(res,'点击了设备类型')
                  if(this.sink=='edit' ){
                    this.eqState3=res.rows
                  }else if(this.sink=='add'){
                    this.eqState1=res.rows
                  }                  
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
      this.strategyForm.equipmentstr=e
      // console.log(e,index,'rrrrrrrrrrrrrrrr')
      // console.log(this.strategyForm.equipmentstr)
      
    },
    //选择隧道清空了下数据
    changeTunnel(){
      this.is_show=true;
      this.oneId = [];
      this.oneIdEqTypeId=[];
      this.eqState=[];
      this.twoId=[];
      this.list=[{"oneId":'',"twoId":''}];
      this.list2=[{"twoId":''}];
      this.selectedList=[];//存储每次option选中的集合
      this.strategyForm.equipmentTypeId = [];
      this.strategyForm.strategyType = '0';
      /* this.strategyForm.tunnelId=null; */
      this.strategyForm.strategyName=null;
      this.strategyForm.equipments=null;
      this.strategyForm.equipmentstr=[];
      this.strategyForm.equipmentStates=[];
      this.strategyForm.schedulerTime = null;
     let  currentTunnelId=this.strategyForm.tunnelId
      /* 查询设备类型*/
      // listHasType(currentTunnelId).then((response) => {
      //   let typeList = response.rows;
      //   this.eqTypeList = typeList;
      // console.log(response,'responseresponse')
      // });
      autoEqTypeList({isControl:1}).then(res=>{
        this.eqTypeList= res.rows
        console.log(res,'responseresponse')
      })
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
    changeSelect(val) {
      console.log(val,'valavlalvla')
        // if (!val.includes('全选') && val.length === this.equipmentData.length) {
        // } else if (val.includes('全选') && (val.length - 1) < this.equipmentData.length) {
        //   this.eqForm.equipments = this.eqForm.equipments.filter((item) => {
        //     return item !== '全选'
        //   })
        // }
        this.eqForm.equipments=val
    },
    changeSelectDirection() {
        if(this.viewStrategy){
            return false;
        }
        this.queryEqParams.eqType = this.eqForm.equipment_type;
        this.queryEqParams.eqTunnelId = this.strategyForm.tunnelId;
        this.queryEqParams.eqDirection = this.strategyForm.direction;
        this.eqForm.equipments = null;
        listDevices(this.queryEqParams).then(response => {
            let a = response.rows;
            if(response.rows!=null && response.rows.length>0){
                for(let i=0;i < a.length;i++){
                    if(a[i].remark==null){
                        a[i].remark = "无备注"
                    }
                }
            }
            this.equipmentData = a
            console.log(this.equipmentData,"this.equipmentData")
        });
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
    addItem(){
      let addItemBoolean = true;
      console.log(this.list,"this.list")
      if(this.strategyForm.strategyType == 0){
        console.log(this.strategyForm.strategyType,'this.strategyFormv')
        this.strategyForm.addOperation.push({         
            equipments:[],//所选设备
            eqState1:''        
        })
        // for ( var i = 0; i < this.list2.length; i++){
        //     console.log(this.strategyForm.equipmentTypeId[i]);
        //     if(this.strategyForm.equipments ==null || this.eqState1 ==null){
        //       addItemBoolean = false;
        //     }
        // }
        // if(addItemBoolean){
        //     this.list2.push({"twoId":'', "twoName":''});
        //     console.log(this.eqState,'手动控制')
        // }else{
        //   this.$modal.msgError("请填写完整！");
        // }
      }else if(this.strategyForm.strategyType == 1){
        for ( var i = 0; i < this.list.length; i++){
            console.log(this.strategyForm.equipmentTypeId[i]);
            if(this.strategyForm.equipmentTypeId[i] ==null || this.twoId[i] ==null){
              addItemBoolean = false;
            }
        }
        if(addItemBoolean){
            this.list.push({"oneId": '',"twoId":'', "twoName":''});
            console.log(this.list,'控制')
        }else{
          this.$modal.msgError("请填写完整！");
        }
      }else if(this.strategyForm.strategyType == 2){
        this.strategyForm.autoAddOperation.push({         
            equipments:[],//所选设备
            eqState1:''        
        })
        // for ( var i = 0; i < this.strategyForm.triggers.length; i++){
        //     console.log(this.strategyForm.equipmentTypeId[i]);
        //     if(this.strategyForm.equipmentTypeId[i] ==null || this.twoId[i] ==null){
        //       addItemBoolean = false;
        //     }
        // }
        // if(addItemBoolean){
        //     // this.list3.push({"twoId":'', "twoName":''});
        //     // console.log(this.list,'控制')
        //     this.strategyForm.triggers.push({ 
        //     deviceTypeId:'',//设备类型
        //     elementId:'',//设备数据项
        //     comaparePattern:'',//比较的符号
        //     comapareValue:'',//比较符号
        //   })
        // }else{
        //   this.$modal.msgError("请填写完整！");
        // }
      }
      
    },
    removeItem(it, index){
      //删除时，我们带两个参数，这个it可用可不用，
      //因为我当时只是想看到删除的这个对象的信息，故而带上了；
      //index是list中该对象对应的下标，也是el-form-item的项数
      //根据这个index下标删除list中的该对象
      // this.strategyForm.addOperation.splice(index,1)
      // if(this.list.length > 1){
      //     this.strategyForm.equipmentTypeId.splice(index,1);//设备类型数组
      //     this.oneId.splice(index,1);//设备类型对应的状态数组
      //     this.strategyForm.equipmentstr.splice(index,1);//设备数组
      //     this.eqState.splice(index,1);
      //     this.twoId.splice(index,1);
      //     this.list.splice(index, 1);//去除一行
      // }else{
      //     this.$modal.msgError("至少保留一条数据");
      // }
    },
    saveTwoList(event, index){
      //当每选一个option时，我们需要将这个选中的oneId放入对应的list中即可，
      //最后都选中完后，我们只要获取这个list，即可拿到所有的数据
      this.list[index].twoId = event;
      let obj = {};
      obj = this.eqState[index].find((item)=>{
          return item.deviceState === event;
      });
      this.list[index].twoName = obj.stateName;
    },

    //设备类型查询设备
    eqTypeChange(value,value2) {
        if(this.viewStrategy){
          return false;
        }
        this.eqTypeName = value2;
        this.queryEqParams.eqType = value;
        this.queryEqParams.eqTunnelId = this.strategyForm.tunnelId;
        this.queryEqParams.eqDirection = this.strategyForm.direction;
        this.eqForm.equipments = null;
        listDevices(this.queryEqParams).then(response => {
          let a = response.rows;
          if(response.rows!=null && response.rows.length>0){
            for(let i=0;i < a.length;i++){
              if(a[i].remark==null){
                a[i].remark = "无备注"
              }
            }
          }
          this.equipmentData = a
          // console.log(this.equipmentData,"this.equipmentData")
        });
        //看下这个是设备类型吗
     console.log(value,value2,'value,value2value,value2')
     
     //点击添加的设备类型
     if(this.sink =="add"){
      //给设备状态赋值
         //清空设备名称和设备状态
      this.strategyForm.addOperation=[{
          equipments:[],//所选设备
          eqState:''
         }]
         //给设备状态赋值
      getStateTypeId({stateTypeId:value}).then(res=>{
        // console.log(res,'点击了设备类型')
         this.eqState1=res.rows
      })
        // 给设备名称赋值
        listDevices({eqType:value,eqTunnelId:this.strategyForm.tunnelId,eqDirection:this.strategyForm.direction}).then(res=>{
              // console.log(res,'oooooooooooooooooo')
              this.equipmentData=res.rows 
              // this.deviceName=res.rows
            })

     }

    //  点击了编辑的设备类型
    //  if( this.strategyForm.strategyType === '0' && this.sink=='edit'){
    //   //清空设备名称和设备状态
    //   this.strategyForm.addOperation=[{
    //       equipments:[],//所选设备
    //       eqState:''
    //      }]
    //      //给手动控制的设备名称赋值
    //     listDevices({eqType:value,eqTunnelId:this.strategyForm.tunnelId,eqDirection:this.strategyForm.direction}).then(res=>{
    //           // console.log(res,'oooooooooooooooooo')
    //           this.equipmentData=res.rows 
    //           // this.deviceName=res.rows
    //         })
    //         //给设备状态辅助  (传设备类型)    
    //         getStateTypeId({stateTypeId:value}).then(res=>{
    //               // console.log(res,'点击了设备类型')
    //               this.eqState3=res.rows
    //             })
    //  }
    
     
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
        console.log(response.rows,"查询设备类型列表")
      });
    },
	getEquipmentHasType() {
	  listHasType(this.strategyForm.tunnelId).then(response => {
	    this.equipmentTypeData = response.rows;
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
        console.log(response,'查询控制策略列表')
        this.strategyList = response.rows;
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
    async submitChooseEqForm() {


      //if (this.eqTypeName === null) {
	  if (this.eqForm.equipment_type === null) {
        this.$modal.msgError("未选择设备类型！");
      } else if (this.eqForm.equipments!=null && this.eqForm.equipments.length > 0){

        if(this.submitChooseEqFormLoading) return
        this.submitChooseEqFormLoading = true

		var strategyForm = this.strategyForm

		for(var i=0;i<strategyForm.equipmentstr.length;i++){
			var subArr = strategyForm.equipmentstr[i].split(',')
			for(var j=0;j<subArr.length;j++){
				if(this.eqForm.equipments.indexOf(subArr[j]) > -1){
					if((!this.saveDevices) || this.saveDevices.indexOf(subArr[j]) == -1){
						this.submitChooseEqFormLoading = false
						this.$modal.msgError("不能添加相同设备，请重新选择！");
						return
					}
				}
			}
		}
		this.submitChooseEqFormLoading = false
        this.chooseEq = false;
        this.queryEqStateParams.stateTypeId =this.eqForm.equipment_type;
		// var saveTwo = this.twoId[this.eqForm.index]
        await listEqTypeStateIsControl({...this.queryEqStateParams, eqDirection: this.strategyForm.direction}).then(response => {
          this.eqState[this.eqForm.index] = response.rows;
          console.log(this.eqState[this.eqForm.index],'this.eqState[this.eqForm.index]')
          if(!this.eqTypeName){
            for(var item of this.equipmentTypeData){
              if(item.typeId == this.eqForm.equipment_type){
                this.eqTypeName = item.typeName
              }
            }
          }
          this.strategyForm.equipmentType = this.eqTypeName+"控制";
          this.oneId[this.eqForm.index] = this.eqTypeName+"控制";
          this.eqTypeName = null
          if(this.oneId[this.eqForm.index] !=null){
            this.twoId[this.eqForm.index] = null;
          }
          this.strategyForm.equipmentTypeId[this.eqForm.index] = this.eqForm.equipment_type;
          this.strategyForm.equipmentstr[this.eqForm.index] = this.eqForm.equipments.join(',');
		  //var eqFormEq = this.eqForm.equipments.toString()
          this.resetEqForm();

			/* if(this.saveDevices.toString() == eqFormEq){
				 this.twoId[this.eqForm.index] = saveTwo
			} */
        });
        this.submitChooseEqFormLoading = false
      }else{
        this.$modal.msgError("未选择设备！");
        this.submitChooseEqFormLoading = false
      }
    },
    // 选择设备取消按钮
    cancelChooseEq() {
      this.chooseEq = false;
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
      this.resetForm("queryForm");
      this.handleQuery();
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
	  this.saveDevices = ''
      if(this.strategyForm.tunnelId !=null){
        this.eqForm.index = index;//设定当前策略index
        this.eqForm.equipment_type = this.strategyForm.equipmentTypeId[index];
        listDevices({eqType:this.eqForm.equipment_type,eqTunnelId:this.strategyForm.tunnelId, eqDirection: this.eqForm.direction}).then(response => {
          let a = response.rows;
          if(response.rows!=null && response.rows.length>0){
            for(let i=0;i < a.length;i++){
              if(a[i].remark==null){
                a[i].remark = "无备注"
              }
            }
          }
          this.equipmentData = a
        if(this.strategyForm.equipmentstr[index] !=null){
          this.eqForm.equipments = this.strategyForm.equipmentstr[index].split(',');
		  this.saveDevices = this.eqForm.equipments
        }
        });
        this.chooseEq = true;
        this.title = "选择设备";
        this.getEquipmentHasType();
      }else{
        this.$modal.msgError("请先选择隧道！");
      }
    },
    //打开drawer
    openInsertStrategy() {
      this.drawerTitle = "新增策略";
      this.sink = "add";
      // this.strategyForm.strategyType = "0";
      this.strategyForm = {
        //策略信息表
        equipmentTypeId: [],
        strategyType: null,//策略类型
        tunnelId:null,//隧道id
        strategyName:null,//策略名称
        equipments:null,//所选设备
        schedulerTime:null,
        direction: '',// 方向
        triggers:{ 
            deviceTypeId:'',//设备类型
            deviceId:'',
            elementId:'',//设备数据项
            comaparePattern:'',//比较的符号
            comapareValue:'',//比较符号
          }
      ,
      addOperation:[
         {
          equipments:[],//所选设备
          eqState1:''
         }
      ],
      autoAddOperation:[
         {
          equipments:[],//所选设备
          eqState1:''
         }
      ],
        //策略关系表
        equipmentstr:[],//动态策略所有选则设备
        equipmentStates:[],//动态策略所有选择状态
        jobRelationId:null,//定时任务Uid
        startTime: '',// 简单模式开始时间
        endTime: '',// 简单模式开始时间
      },
      //清空 定时弹窗里的设备名称数组
      this.eqForm.equipments=[]
      this.drawer = true;
     this.viewStrategy=false;
     
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // listHasType(currentTunnelId).then((response) => {
      //   let typeList = response.rows;
      //   this.eqTypeList = typeList;
      // console.log(response,'responseresponse')
      // });    
      autoEqTypeList({isControl:1}).then(res=>{
        console.log(res,'3333333333333333')
        this.eqTypeList = res.rows;
      })     
      listType(this.queryEqTypeParams).then(response => {
        this.equipmentTypeData = response.rows;
        console.log(response.rows,"查询设备类型列表")
      });
      console.log(row,"row")
      this.viewStrategy=false;
      this.currentId = row.id;
      this.sink = "edit";
      const id = row.id || this.ids
      this.strategyForm.strategyType = "0";
      console.log("==="+row.jobRelationId);

      getStrategy(id).then(response => {
        console.log(response.data,'修改按钮操作')
        this.strategyForm.strategyName = response.data.strategyName;
        this.strategyForm.tunnelId = response.data.tunnelId;
        this.strategyForm.strategyType = response.data.strategyType;
        this.strategyForm.direction = response.data.direction;
        
        this.drawerTitle = "编辑策略";
        this.drawer = true;

        this.strategyForm.jobRelationId = response.data.jobRelationId;
        if(this.strategyForm.strategyType ==1 ){
          this.strategyForm.schedulerTime = response.data.schedulerTime;
        }
        listRl({strategyId: row.id}).then(response => {
          console.log(response,"编辑策略")
          // console.log(this.eqState,'this.eqStatethis.eqState')
          
          if(row.strategyType =='2'){
            this.strategyForm.autoAddOperation=[]
          }
          if(row.strategyType =='0'){
            this.strategyForm.addOperation=[]
          }
          listDevices({eqType:response.rows[0].eqTypeId,eqTunnelId: this.strategyForm.tunnelId}).then(res=>{
                console.log(res,'oooooooooooooooooo')
                // this.deviceName=res.rows
                //看看这的值什么东西  是先赋值的吗？
                this.equipmentData=res.rows
              })
              getStateTypeId({stateTypeId:response.rows[0].eqTypeId}).then(res=>{
                  console.log(res,'点击了设备类型')
                  this.eqState3=res.rows
                })
          for ( var i = 0; i < response.rows.length; i++){
            if(i != 0){
              this.list.push({"oneId": '',"twoId":''});
            }

                  // console.log(response.rows,'response.rowsresponse.rows')
                 if(row.strategyType =='0'){
                  this.strategyForm.addOperation.push({
                  equipments:response.rows[i].equipments.split(','),
                  eqState:response.rows[i].state
                })
                 }
                if(row.strategyType =='2'){
                    this.strategyForm.autoAddOperation.push({
                            equipments:response.rows[i].equipments.split(','),
                            eqState:response.rows[i].state
                          })
                }
                
               console.log( row.strategyType,' this.strategyForm.addOperation this.strategyForm.addOperation')
                // this.strategyForm.addOperation[index].equipments.push(response.rows[index].equipments);//设备名称
                // this.strategyForm.addOperation[index].eqState=response.rows[index].state//设备状态
                // this.strategyForm.autoAddOperation[index].equipments.push(response.rows[index].equipments)//自动触发设备名称
                // this.strategyForm.autoAddOperation[index].eqState=response.rows[index].state//自动触发设备状态
            
             
            this.strategyForm.equipmentstr.push(response.rows[i].equipments);
            this.oneIdEqTypeId.push(response.rows[i].eqTypeId);//设备类型id数组
            this.strategyForm.equipmentTypeId.push(response.rows[i].eqTypeId);
            this.oneId.push(response.rows[i].eqType.typeName+"控制");//设备名称数组
            this.oneId1 =Number(response.rows[0].eqTypeId) ;//设备名称数组
            this.eqState[i] = response.rows[i].eqStateList;//设备状态 下拉option
            this.twoId.push(response.rows[i].state);//执行的操作状态码
            this.twoId2.push(response.rows[i].state);//手动控制执行的操作状态码             
            this.strategyForm.equipments.push(response.rows[0].equipments)//设备名称

            // console.log(this.strategyForm.equipments,'this.eqForm.equipmentsthis.eqForm.equipments')
            // this.eqForm.equipments=response.rows[0].equipments
            // this.twoId1.push({eqId:response.rows[i].equipments,eqName:response.rows[i].equipments});//设备名称数组
            // console.log(this.eqState,'this.eqStatethis.eqState')
            // this.equipmentData.push({eqId:response.rows[i].equipments,eqName:response.rows[i].equipments})
            // console.log(this.twoId1,'equipmentDataequipmentDataequipmentData')
            //===============================================================================
            this.turnToStateName(this.eqState[i], i, response.rows[i].state);
          }
        })

        console.log(row.strategyType,'row.strategyTyperow.strategyType')
        if(row.strategyType =='2'){
          console.log('点击了自动触发')
          getTriggersByRelateId({relateId:row.id}).then(res=>{
            // console.log(res,'qqqqqqqqqqqqq')
            // console.log(res.data.comparePattern,'qwwwwwwwwwwwwwwwww')
            //给设备下拉赋值
          listDevices({eqType:res.data.deviceTypeId,eqTunnelId:this.strategyForm.tunnelId}).then(res=>{
             
              this.equipmentData=res.rows
              this.deviceName=res.rows
            })
            listItem({deviceTypeId:res.data.deviceTypeId}).then(res=>{
                this.dataItem=res.rows
                })
           
          this.strategyForm.triggers.deviceTypeId=res.data.deviceTypeId //设备类型
          this.strategyForm.triggers.deviceId=res.data.deviceId  //设备名称
          this.strategyForm.triggers.elementId=res.data.elementId//设备数据项
          this.strategyForm.triggers.comparePattern=res.data.comparePattern //比较的符号
          this.strategyForm.triggers.compareValue=res.data.compareValue//比较值
          this.strategyForm.triggers.id=res.data.id
        //  console.log(this.strategyForm.triggers.comapareValue,'this.strategyForm.triggers.comapareValue')
        })
        }



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
      this.strategyForm.equipmentTypeId = [];
      this.strategyForm.strategyType=null;
      this.strategyForm.tunnelId=null;
      this.strategyForm.strategyName=null;
      this.strategyForm.equipments=[];//设备名称
      this.strategyForm.equipmentstr=[];
      this.strategyForm.equipmentStates=[];
      this.strategyForm.schedulerTime = null;
      this.strategyForm.addOperation=[
      {
          equipments:[],//所选设备
          eqState1:''
         }
      ],
      this.strategyForm.autoAddOperation=[
      {
          equipments:[],//所选设备
          eqState1:''
         }
      ]
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
    submitStrategyForm() {
      var str = ""
      console.log(this.eqForm,'this.eqFormthis.eqFormthis.eqForm')
      if(!this.strategyForm.tunnelId){
        this.$modal.msgError("请选择隧道！");
        return;
      }
      if(!this.strategyForm.strategyType){
        this.$modal.msgError("请选择策略类型！");
        return;
      }
      if(!this.strategyForm.strategyName){
        this.$modal.msgError("请填写策略名称！");
        return;
      }
      console.log(this.strategyForm.addOperation)
      // for ( let i=0; i>this.strategyForm.addOperation.length; i++){
      //   console.log( this.strategyForm.addOperation[i])
      //  if( this.strategyForm.addOperation[i].eqState1 == ''){
      //   this.$modal.msgError("请填写控制策略!!");  
      //   console.log('111')
      //   return
      //  }
      // }
      if(this.strategyForm.strategyName == '0'){
              if(this.strategyForm.addOperation[0].equipments.length<=0){
            this.$modal.msgError("请填写控制策略!!");  
              return
          }
          if(!this.strategyForm.addOperation[0].eqState){
            this.$modal.msgError("请填写控制策略!!");  
              return
          }
    }
     
        
      
      // console.log(this.strategyForm.addOperation,'xinzasdsadsa')
        // this.strategyForm.addOperation.forEach((item,index)=>{
            
        //    if(item.equipmentTypeId ) {
        //     this.$modal.msgError("未设置控制策略！");
        //     return
        //    }  
        // })
      // if(this.strategyForm.equipmentTypeId.length < 1){
      //   this.$modal.msgError("未设置控制策略！");
      //   return;
      // }
      // if(this.twoId.length < 1){
      //   this.$modal.msgError("未设置控制策略！")
      //   return;
      // }

      if(this.strategyForm.strategyType == 1){
        if(this.strategyForm.schedulerTime==null || !this.strategyForm.schedulerTime){
          this.$modal.msgError("未设置定时！")
          return;
        }
        // if(this.model == '2'){

        // }
        /* else if (this.model == '1') {
          var startTime  = this.strategyForm.startTime
          var endTime  = this.strategyForm.endTime
          debugger
          if(startTime == "" || endTime == "" || startTime == null || endTime == null){
            this.$modal.msgError("请选择开始或结束时间！");
            return;
          }
          if(endTime < startTime){
            this.$modal.msgError("开始时间需要小于结束时间！");
            return;
          }
          str += startTime.toString() +'-' + endTime.toString()
        } */
      }
      // let strategyBoolean = true;
      // for ( var i = 0; i < this.list.length; i++){
      //     if(this.strategyForm.equipmentTypeId[i] ==null || this.twoId[i] ==null){
      //       strategyBoolean = false;
      //     }
      // }
      // if(!strategyBoolean){
      //     this.$modal.msgError("控制策略不完整！");
      //     return;
      // }
      this.dloading=true;
      this.$refs["form1"].validate(valid => {
        if (valid) {
          let straInfo = "";//策略信息
          if(this.strategyForm.strategyType == 0){//手动执行

          }else if(this.strategyForm.strategyType == 1){//定时执行
              // 复杂模式
                var cronstrue = require('cronstrue/i18n');
                console.log(this.strategyForm.schedulerTime,"this.strategyForm.schedulerTime")
                straInfo = cronstrue.toString(this.strategyForm.schedulerTime, { locale: "zh_CN" });
                straInfo = straInfo+"：";
                straInfo = "每天" + straInfo + str;
              console.log(straInfo)
          }else if(this.strategyForm.strategyType == 2){//智能执行
              // straInfo = "当******时执行：";
          }
          // 新增
          if(this.sink =="add"){
            if(this.strategyForm.strategyType == 0){//手动执行
              getGuid().then(response => {
                let guid = response;
                this.addStrategysData(guid,straInfo);
              })
            }else if(this.strategyForm.strategyType == 1){//定时执行
                getGuid().then(response => {
                    let guid = response;
                    this.addJobData(guid,str);
                  console.log('定时任务')
                    this.timeAddStrategysData(guid,straInfo);
                })
            }else if(this.strategyForm.strategyType == 2){//智能执行
                // this.addStrategysData(null,straInfo);
                console.log('点击了自动触发')
                getGuid().then(response => {
                let guid = response;
                this.autoaddStrategysData(guid,straInfo);
              })
            }
          }
          // 编辑
          else{
              let flag = false
              if(this.strategyForm.strategyType == 1){//定时执行策略
                console.log(this.strategyForm,"this.strategyForm.jobRelationId");
                getJobByRelationId(this.strategyForm.jobRelationId).then(response => {
                  if(response.code === 200 && response.msg == '定时任务存在') {
                    flag = true
                    return this.$modal.msgWarning(response.msg)
                  }
                  response.data.cronExpression = this.strategyForm.schedulerTime;
                  updateJob(response.data).then(response => {
                    if (response.code === 200) {
                      this.$modal.msgSuccess("任务修改成功");
                    }
                  });

                })
              }
              if(this.strategyForm.strategyType == 2){    
                this.strategyForm.autoAddOperation.forEach((item)=>{
                    item.equipments=item.equipments.join('#')
                    item.equipmentTypeId=this.strategyForm.triggers.deviceTypeId
                  })       
                  console.log(this.strategyForm.triggers,'点击了自动触发')     
                  updateStrategysInfo({
               triggers:this.strategyForm.triggers,
                id : this.currentId,
                tunnelId:this.strategyForm.tunnelId,
                strategyInfo:straInfo,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                equipment:this.strategyForm.autoAddOperation,
                // equipmentTypeId:this.strategyForm.equipmentTypeId.join('#'),
                equipmentState:this.twoId.join('#'),
                // equipments:this.strategyForm.equipmentstr.join('#'),
                schedulerTime:this.strategyForm.schedulerTime,
                triggers:this.strategyForm.triggers
                  }).then(res=>{
                     console.log(res,'自动触发')
                           if (res.code === 200) {
                    this.$modal.msgSuccess("策略修改成功");
                    this.dloading=false;
                    this.drawer=false;
                    // this.currentId = null;
                    this.getList();
                    this.resetStrategyInfo();
                    setTimeout(()=>{
                      this.resetStrategyInfo();
                    }, 400)
                  }
                  })
           
              }
              if(flag) return
              // 执行编辑操作
              if(this.strategyForm.strategyType == 0){
                this.strategyForm.addOperation.forEach((item)=>{
                    item.equipments=item.equipments.join('#')
                    item.equipmentTypeId=this.oneId1           
                  })
                console.log(this.oneId1,'手动控制')
                updateStrategysInfo({
                id : this.currentId,
                tunnelId:this.strategyForm.tunnelId,
                strategyInfo:straInfo,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                equipmentTypeId:this.strategyForm.equipmentTypeId.join('#'),
                equipmentState:this.twoId.join('#'),
                equipments:this.strategyForm.equipmentstr.join('#'),
                equipment:this.strategyForm.addOperation,
                schedulerTime:this.strategyForm.schedulerTime
                }).then(response => {
                  if (response.code === 200) {
                    this.$modal.msgSuccess("策略修改成功");
                    this.dloading=false;
                    this.drawer=false;
                    this.currentId = null;
                    this.getList();
                    setTimeout(()=>{
                      this.resetStrategyInfo();
                    }, 400)
                  }
                });
              }
             
                if(this.strategyForm.strategyType == 1){
                  //执行定时编辑操作
                  console.log('点击了定时的保存')
              updateStrategysInfo({
                id : this.currentId,
                tunnelId:this.strategyForm.tunnelId,
                strategyInfo:straInfo,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                equipmentTypeId:this.strategyForm.equipmentTypeId.join('#'),
                equipmentState:this.twoId.join('#'),
                equipments:this.strategyForm.equipmentstr.join('#'),
                schedulerTime:this.strategyForm.schedulerTime
                }).then(response => {
                  if (response.code === 200) {
                    this.$modal.msgSuccess("策略修改成功");
                    this.dloading=false;
                    this.drawer=false;
                    this.currentRlId = null;
                    this.getList();
                    setTimeout(()=>{
                      this.resetStrategyInfo();
                    }, 400)
                  }
                });
                }

            }
          }
      });
    },

     /** 新增定时触发 */
     timeAddStrategysData(guid,straInfo){
        // this.strategyForm.autoAddOperation.forEach((item)=>{
        //   item.equipments=item.equipments.join('#')
        //   item.equipmentTypeId=this.strategyForm.triggers.deviceTypeId
        // })
        // console.log(this.queryEqParams.eqType,'qqqq')
        // return 
        console.log(this.eqForm.equipments,'this.eqForm.equipments')
        console.log(this.twoId[0],'this.this.twoIdthis.twoIdthis.twoId.')
        addStrategysInfo({tunnelId:this.strategyForm.tunnelId,
                triggers:this.strategyForm.triggers,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                strategyInfo:straInfo,
                equipmentTypeId:this.strategyForm.equipmentTypeId.join('#'),//设备类型id
                equipmentState:this.twoId.join('#'),  //设备状态操作
                equipments:this.strategyForm.equipmentstr.join('#'),
                schedulerTime:this.strategyForm.schedulerTime,
                // equipmentState:this.strategyForm.autoAddOperation.eqState1,
                // equipment:this.strategyForm.autoAddOperation,
                jobRelationId:guid,
                }).then(response => {
                  if (response.code === 200) {
                    if(this.strategyForm.strategyType != 1) {
                     this.$modal.msgSuccess("新增策略成功");
                    }
                    this.dloading=false;
                    this.drawer=false;
                    this.getList();
                    this.resetEqForm()
                    setTimeout(() => {
                      this.resetStrategyInfo();
                    }, 400);
                  }
              });
        },


    /** 新增自动触发 */
    autoaddStrategysData(guid,straInfo){
        this.strategyForm.autoAddOperation.forEach((item)=>{
          item.equipments=item.equipments.join('#')
          item.equipmentTypeId=this.strategyForm.triggers.deviceTypeId
        })
        console.log(this.strategyForm.triggers,'点击了新增的保存')
        addStrategysInfo({tunnelId:this.strategyForm.tunnelId,
                triggers:this.strategyForm.triggers,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                strategyInfo:straInfo,
                // equipmentTypeId:this.strategyForm.equipmentTypeId1,//设备类型id
                equipmentState:this.twoId.join('#'),
                // equipments:this.strategyForm.equipments.join('#'),
                schedulerTime:this.strategyForm.schedulerTime,
                equipmentState:this.strategyForm.autoAddOperation.eqState1,
                equipment:this.strategyForm.autoAddOperation,
                jobRelationId:guid,
                }).then(response => {
                  if (response.code === 200) {
                    if(this.strategyForm.strategyType != 1) {
                     this.$modal.msgSuccess("新增策略成功");
                    }
                    this.dloading=false;
                    this.drawer=false;
                    this.getList();
                    setTimeout(() => {
                      this.resetStrategyInfo();
                    }, 400);
                  }
              });
        },

    
      /** 添加策略 */
      addStrategysData(guid,straInfo){    
        this.strategyForm.addOperation.forEach((item)=>{
          item.equipments=item.equipments.join('#')
          item.equipmentTypeId=this.strategyForm.equipmentTypeId1       
        })
         if(!this.strategyForm.equipmentTypeId1){
          this.$modal.msgError("请填写设备类型!!"); 
          return
         }
        console.log(this.strategyForm.addOperation,'点击了新增的保存')
        addStrategysInfo({tunnelId:this.strategyForm.tunnelId,
                strategyName:this.strategyForm.strategyName,
                direction:this.strategyForm.direction,
                strategyType:this.strategyForm.strategyType,
                strategyInfo:straInfo,
                equipmentTypeId:this.strategyForm.equipmentTypeId1,//设备类型id
                // equipmentState:this.twoId.join('#'),
                // equipments:this.strategyForm.equipments.join('#'),
                schedulerTime:this.strategyForm.schedulerTime,
                equipmentState:this.strategyForm.addOperation.eqState1,
                equipment:this.strategyForm.addOperation,
                jobRelationId:guid,
                }).then(response => {
                  if (response.code === 200) {
                    if(this.strategyForm.strategyType != 1) {
                     this.$modal.msgSuccess("新增策略成功");
                    }
                    this.dloading=false;
                    this.drawer=false;
                    this.getList();
                    setTimeout(() => {
                      this.resetStrategyInfo();
                    }, 400);
                  }
              });
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
            relationId:guid
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
