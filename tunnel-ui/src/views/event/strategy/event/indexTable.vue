<template>
  <div>
<!--    <el-tabs v-model="activeName" @tab-click="handleClick">-->
<!--      <el-tab-pane :label="strategyTypeGroup[0]?strategyTypeGroup[0].dictLabel:''" name="one">-->
        <!-- 全局搜索 -->
        <el-row :gutter="20" class="tabTopFormRow">
          <el-col :span="6">
            <el-button
              size="small"
              @click="openInsertStrategy(tableType)"
              v-hasPermi="['system:strategy:add']"
            >新增
            </el-button
            >
            <el-button
              size="small"
              @click="handleExport('1')"
            >导出
            </el-button
            >
            <el-button size="small" @click="resetQuery"
            >刷新
            </el-button
            >
          </el-col>
          <el-col :span="6" :offset="12">
            <div class="grid-content bg-purple" ref="main">
              <el-input
                v-model="queryParams.strategyName"
                placeholder="请输入策略名称，回车搜索"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              >
                <el-button
                  slot="append"
                  class="searchTable"
                  @click="boxShow = !boxShow"
                ></el-button>
              </el-input>
            </div>
          </el-col>
        </el-row>
        <div class="searchBoxTab" v-show="boxShow" ref="cc">
          <el-form
            ref="queryForm"
            :inline="true"
            :model="queryParams"
            label-width="75px"
          >
            <!-- <el-form-item label="隧道名称" prop="tunnelId">
              <el-select
                v-model="queryParams.tunnelId"
                placeholder="请选择隧道"
                clearable
                size="small"
              >
                <el-option
                  v-for="item in tunnelData"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item> -->
            <el-form-item label="方向" prop="direction">
              <el-select
                v-model="queryParams.direction"
                placeholder="请选择方向"
                style="width: 100%"
              >
                <el-option
                  v-for="(item, index) in directionOptions"
                  :key="index"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="策略类型" prop="strategyType">
              <el-select
                v-model="queryParams.strategyType"
                placeholder="请选择策略类型"
                clearable
                size="small"
              >
                <el-option
                  v-for="dict in strategyTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item> -->
            <el-form-item class="bottomBox">
              <el-button size="small" type="primary" @click="handleQuery"
              >搜索
              </el-button
              >
              <el-button size="small" @click="resetQuery" type="primary" plain
              >重置
              </el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <!--开始-->
        <!-- <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="隧道名称" prop="tunnelId">
            <el-select
              v-model="queryParams.tunnelId"
              placeholder="请选择隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
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
          </el-form-item> -->
        <!-- 日常 -->
        <!-- <el-form-item label="策略类型" prop="strategyType">
          <el-select
            v-model="queryParams.strategyType"
            placeholder="请选择策略类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in strategyTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="handleQuery"
          >搜索
          </el-button
          >
          <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置
          </el-button
          >
          <el-button
            type="primary"
            plain
            size="mini"
            @click="openInsertStrategy('richang')"
            v-hasPermi="['system:strategy:add']"
          >新增
          </el-button
          >
        </el-form-item> -->
        <el-table
          :key="tableKey"
          v-loading="loading"
          :data="strategyList"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
          :header-cell-style="{ 'text-align': 'center' }"
          height="62vh"
          class="allTable"
          :row-key="getRowKey"
          ref="tableFile1"
        >
          <el-table-column type="selection" width="55" align="center" reserve-selection />
          <el-table-column
            type="index"
            width="70"
            align="center"
            label="序号">
          </el-table-column>
          <el-table-column
            label="隧道名称"
            align="center"
            prop="tunnels.tunnelName"
          />
          <el-table-column
            label="策略名称"
            align="center"
            prop="strategyName"
            show-overflow-tooltip
          />
          <el-table-column
            label="方向"
            align="center"
            prop="direction"
            :formatter="directionFormat"
          />
          <el-table-column
            label="事件类型"
            align="center"
            prop="eventType"
            :formatter="eventTypeFormatEvent"
            v-if="tableType == 'zidong'"
          />
          <el-table-column
            label="策略类型"
            align="center"
            prop="strategyType"
            :formatter="strategyTypeFormat"
          />
          <el-table-column
            label="策略信息"
            align="left"
            prop="slist"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.slist" :key="index">
                {{ item }}
              </div>
            </template>
          </el-table-column>

          <el-table-column label="状态" align="center" prop="schedulerTime"  v-if="tableType != 'shoudong'" >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.strategyState"
                active-color="#39ADFF"
                inactive-color="#ccc"
                active-value="0"
                inactive-value="1"
                v-hasPermi="['system:strategy:state']"
                @change="changeStrategyState(scope.row,scope.row.index)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:strategy:edit']"
              >编辑
              </el-button
              >
              <el-button
                size="mini"
                class="tableBlueButtton"
                v-if="tableType == 'shoudong'"
                v-hasPermi="['system:strategy:execute']"
                @click="richanghandleUpdate(scope.row)"
              >执行</el-button
              >
              <el-button
                size="mini"
                class="tableDelButtton"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:strategy:remove']"
              >删除
              </el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
        <!--        结束-->
<!--      </el-tab-pane>-->
      <!--      <el-tab-pane :label="strategyTypeGroup[1]?strategyTypeGroup[1].dictLabel:''" name="two">
              &lt;!&ndash; 全局搜索 &ndash;&gt;
              <el-row :gutter="20" class="tabTopFormRow">
                <el-col :span="6">
                  <el-button
                    size="small"
                    @click="openInsertStrategy('event')"
                    v-hasPermi="['system:strategy:add']"
                  >新增
                  </el-button
                  >
                  <el-button
                    size="small"
                    @click="handleExport('2')"
                  >导出
                  </el-button
                  >
                  <el-button size="small" @click="resetQuery"
                  >刷新
                  </el-button
                  >
                </el-col>
                <el-col :span="6" :offset="12">
                  <div class="grid-content bg-purple" ref="main1">
                    <el-input
                      v-model="queryParams.strategyName"
                      placeholder="请输入策略名称，回车搜索"
                      clearable
                      size="small"
                      @keyup.enter.native="handleQuery"
                    >
                      <el-button
                        slot="append"
                        class="searchTable"
                        @click="boxShow1 = !boxShow1"
                      ></el-button>
                    </el-input>
                  </div>
                </el-col>
              </el-row>
              <div class="searchBoxTab" v-show="boxShow1">
                <el-form
                  ref="queryForm"
                  :inline="true"
                  :model="queryParams"
                  label-width="75px"
                >
                  <el-form-item label="隧道名称" prop="tunnelId">
                    <el-select
                      v-model="queryParams.tunnelId"
                      placeholder="请选择隧道"
                      clearable
                      size="small"
                    >
                      <el-option
                        v-for="item in tunnelData"
                        :key="item.tunnelId"
                        :label="item.tunnelName"
                        :value="item.tunnelId"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="策略类型" prop="strategyType">
                    <el-select
                      v-model="queryParams.strategyType"
                      placeholder="请选择策略类型"
                      clearable
                      size="small"
                    >
                      <el-option
                        v-for="dict in strategyTypeEvent"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item class="bottomBox">
                    <el-button size="small" type="primary" @click="handleQuery"
                    >搜索
                    </el-button
                    >
                    <el-button size="small" @click="resetQuery" type="primary" plain
                    >重置
                    </el-button
                    >
                  </el-form-item>
                </el-form>
              </div>
              <el-table
                v-loading="loading"
                :data="strategyList"
                @selection-change="handleSelectionChange"
                :header-cell-style="{ 'text-align': 'center' }"
                height="62vh"
                class="allTable"
                :row-key="getRowKey"
                ref="tableFile2"
                :key="Math.random()"
              >
                <el-table-column type="selection" width="55" align="center" reserve-selection/>
                <el-table-column
                  type="index"
                  width="70"
                  align="center"
                  label="序号">
                </el-table-column>
                <el-table-column
                  label="隧道名称"
                  align="center"
                  prop="tunnels.tunnelName"
                />
                <el-table-column
                  label="事件类型"
                  align="center"
                  prop="eventType"
                  :formatter="eventTypeFormatEvent"
                />
                <el-table-column
                  label="策略名称"
                  align="center"
                  prop="strategyName"
                />
                <el-table-column
                  label="方向"
                  align="center"
                  prop="direction"
                  :formatter="directionFormat"
                />
                <el-table-column
                  label="策略类型"
                  align="center"
                  prop="strategyType"
                  :formatter="strategyTypeFormatEvent"
                />
                <el-table-column
                  label="策略信息"
                  align="left"
                  prop="slist"
                  :show-overflow-tooltip="true"
                >
                  <template slot-scope="scope">
                    <div v-for="(item, index) in scope.row.slist" :key="index">
                      {{ item }}
                    </div>
                  </template>
                </el-table-column>

                <el-table-column label="状态" align="center" prop="schedulerTime">
                  <template slot-scope="scope">
                    <el-switch
                      v-model="scope.row.strategyState"
                      active-color="#39ADFF"
                      inactive-color="#ccc"
                      active-value="0"
                      inactive-value="1"
                      @change="changeStrategyState(scope.row)"
                    >
                    </el-switch>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  class-name="small-padding fixed-width"
                >
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      class="tableBlueButtton"
                      @click="handleUpdate(scope.row)"
                      v-hasPermi="['system:strategy:edit']"
                    >编辑
                    </el-button
                    >
                    <el-button
                      size="mini"
                      class="tableDelButtton"
                      @click="handleDelete(scope.row)"
                      v-hasPermi="['system:strategy:remove']"
                    >删除
                    </el-button
                    >
                  </template>
                </el-table-column>
              </el-table>

              <pagination
                v-show="total > 0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
              />
            </el-tab-pane>-->
<!--    </el-tabs>-->

    <!-- 选择策略类型弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :before-close="handleClose"
      :append-to-body="true"
      width="75%"
      class="celueDialog"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="strategyForm" :model="strategyForm" label-width="100px">
<!--        <el-form-item label="策略类型" prop="strategyType">-->
<!--          <el-radio-group v-model="strategyForm.strategyType">-->
<!--            <el-radio-->
<!--              border-->
<!--              disabled-->
<!--              v-for="dict in insertStrategyTypeOptionsCopy"-->
<!--              :key="dict.dictValue"-->
<!--              :label="dict.dictValue"-->
<!--              @change.native="strategyTypeClose()"-->
<!--            >{{ dict.dictLabel }}-->
<!--            </el-radio-->
<!--            >-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
      </el-form>
      <!--自动触发      -->
      <auto-control-event
        v-show="strategyForm.strategyType == '2'"
        ref="autoControl"
        @dialogVisibleClose="closeDialog"
        @refreshList = "getList"
      ></auto-control-event>
      <!-- 手动控制 -->
      <manual-control
        v-show="strategyForm.strategyType == '0'"
        ref="manualControl"
        @dialogVisibleClose="closeDialog"
        @refreshList = "getList"
      ></manual-control>
      <!-- 定时控制 -->
      <timingControl
        v-show="strategyForm.strategyType == '1'"
        @dialogVisibleClose="closeDialog"
        @refreshList = "getList"
        ref="timingControl"
      ></timingControl>
      <timeControl
        v-show="strategyForm.strategyType == '3'"
        @dialogVisibleClose="closeDialog"
        @refreshList = "getList"
        ref="timeControl"
      ></timeControl>
    </el-dialog>
    <!-- end -->
    <!--    预警事件弹窗-->
    <!--    <el-dialog
          :title="title"
          :visible.sync="dialogVisibleEvent"
          :before-close="handleCloseEvent"
          :append-to-body="true"
          width="75%"
          class="strategyDialog"
        >
          <div class="dialogCloseButton"></div>
          <el-form ref="strategyForm" :model="strategyForm" label-width="100px">
            <el-form-item label="策略类型" prop="strategyType">
              <el-radio-group v-model="strategyForm.strategyType">
                <el-radio
                  border
                  v-for="dict in strategyTypeEvent"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  @change.native="strategyTypeEventClose()"
                >{{ dict.dictLabel }}
                </el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-form>
          &lt;!&ndash; 手动控制 &ndash;&gt;
          <manual-control-event
            v-show="strategyForm.strategyType == '0'"
            ref="manualControlEvent"
            @dialogVisibleCloseEvent="closeDialogEvent"
          ></manual-control-event>
          &lt;!&ndash; 自动触发 &ndash;&gt;
          <autoControl-event
            v-show="strategyForm.strategyType == '2'"
            @dialogVisibleCloseEvent="closeDialogEvent"
            ref="autoControlEvent"
          ></autoControl-event>
        </el-dialog>-->
  </div>
</template>
<script src="../../../../cronStrue/dist/cronstrue.min.js" type="text/javascript"></script>
<script src="../../../../cronStrue/dist/cronstrue-i18n.min.js" type="text/javascript"></script>
<script>
import {
  listStrategy,
  getStrategy,
  delStrategy,
  updateState,
  addStrategy,
  addStrategyInfo,
  updateStrategyInfo,
  getGuid,
  handleStrategy, export1,
  manualControlInfo,
} from "@/api/event/strategy";
import {listEventType} from "@/api/event/eventType";
import {listRl, addRl} from "@/api/event/strategyRl";
import {
  listType,
  listHasType,
  autoEqTypeList,
  getStateTypeId,
  getTriggersByRelateId,
} from "@/api/equipment/type/api";
import {listItem} from "@/api/equipment/eqTypeItem/item";
import {listDevices} from "@/api/equipment/eqlist/api";
import {listTunnels} from "@/api/equipment/tunnel/api";
// import { delStrategy } from "@/api/event/strategy";
import {listEqTypeStateIsControl} from "@/api/equipment/eqTypeState/api";
import {
  addJob,
  updateJob,
  delJob,
  getJobByRelationId,
} from "@/api/monitor/job";
import CronValidator from "../../../../components/easy-cron/validator";
import manualControl from "../components/manualControl"; //手动控制
import timingControl from "../components/timingControl"; //定时控制
import autoControl from "../components/autoControl"; //自动触发
import timeControl from "../components/timeControl"; //分时控制
import cron from "@/components/cron/cron.vue";

import manualControlEvent from "../event/manualControl"; //手动控制
import autoControlEvent from "../event/autoControl.vue";

export default {
  components: {
    manualControl,
    timingControl,
    autoControl,
    timeControl,
    manualControlEvent,
    autoControlEvent,
    cron,
  },
  data() {
    return {
      manageStatin: this.$cache.local.get("manageStation"),
      tableKey:0,
      allLoading: {
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      },
      boxShow: false,
      boxShow1: false,

      activeName: "one",
      dialogVisible: false,
      dialogVisibleEvent: false,
      index: 0,
      manualControlStateList: [], //当前选择设备状态选项
      strategyForm: {
        strategyType: null, //策略类型
      },
      guid: "",
      is_show: false,
      changeVal: false,
      saveDevices: "",
      //drawer遮罩层
      dloading: false,
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
      chooseScheduleTimeVb: false,
      // 设备方向字典
      directionOptions: [],
      // 策略类型字典
      strategyTypeOptions: [],
      // 策略类型字典
      insertStrategyTypeOptions: [],
      // 策略类型字典
      insertStrategyTypeOptionsCopy: [],
      // 编辑策略选中rlId
      currentId: null,
      // 导出遮罩层
      exportLoading: false,
      // 添加/编辑标志 add添加，edit 编辑
      sink: "",
      checked: false,
      showCronBox: false,
      viewStrategy: false,
      editCronValue: this.cronValue,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        strategyName: null,
        direction:null,
        strategyType: 0,
        strategyInfo: null,
        schedulerTime: null,
        jobTime: null,
        strategyGroup: 1,
      },
      queryEqParams: {
        eqType: null,
      },
      queryEqTypeParams: {
        isControl: 1,
      },
      //查询设备状态params
      queryEqStateParams: {
        stateTypeId: null,
      },
      // 表单参数
      eqForm: {
        equipment_type: null, //设备类型ID
        direction: "", //方向
        index: null, //当前设置的
        equipments: [], //设备名称
      },
      /*      scheduleForm:{
          cronExpression:'',
        }, */
      // 表单校验
      rules: {},
      drawer: false,
      drawerTitle: null, //drawer标题
      direction: "rtl",
      equipmentTypeData: {},
      equipmentData: [],
      tunnelData: {},
      //选中设备类型 名称
      eqTypeName: null,
      array: [],
      oneId: [], //设备类型名称数组
      oneId1: "",
      oneIdEqTypeId: [], //设备类型id数组
      eqState: [], //设备状态数组
      eqState1: [], //新增的状态设备数组
      eqState3: [], //自动控制设备状态数组
      twoId: [], //设备状态存储
      twoId2: [], //手动控制项的
      twoId1: [],
      twoName: [], //设备状态名称
      list: [{oneId: "", twoId: "", twoName: ""}],
      list2: [{twoId: "", twoName: ""}],
      selectedList: [], //存储每次option选中的集合
      warningOptions: [
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
      model: "2", // 复杂模式
      msg: "Cron表达式测试页面",
      exeStartTime: "",
      eqListName: [],
      eqTypeList: [], //设备类型
      deviceName: [], //设备名称
      dataItem: [], //设备数据类型项
      symbol: [], //符号
      strategyTypeGroup: [],
      strategyTypeEvent: [],
      dictCode: "0",
      eventTypeList: [],//主动安全列表
      // automaticEqType:[] //自动触发设备类型数组
      // tableType:'shoudong',
    };
  },
  created() {
    // this.getList(); //查询控制策略
    this.getTunnels(); //获取隧道
    this.getListEventType()
    
    // 预警策略
    this.getDicts("sys_common_event").then((response) => {
      this.strategyTypeEvent = response.data;
      console.log(this.strategyTypeEvent, "this.strategyTypeEvent");
    });
    // 策略组信息
    this.getDicts("sd_strategy_group").then((response) => {
      this.strategyTypeGroup = response.data;
      console.log(this.strategyTypeGroup, "this.strategyTypeGroup");
    });
    // 日常策略
    this.getDicts("sd_strategy_type").then((response) => {
      this.strategyTypeOptions = response.data;
      this.insertStrategyTypeOptions = response.data;
    });
    this.getDicts("sd_trigger_compare_type").then((response) => {
      this.symbol = response.data;
    });
    this.getDicts("sd_strategy_direction").then((response) => {
      response.data.forEach((item) => {
        this.directionOptions.push(item);
      });
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
    // document.addEventListener("click", this.bodyCloseMenus1);
  },
  beforeDestroy(){
    document.removeEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    getListEventType(){
      let data = {prevControlType: "1"};
      listEventType(data).then(res => {
        this.eventTypeList = res.rows;
      })
    },
    // 点击某一行，将其选中(表格)
    handleRowClick(row, i, a) {
      if(i.label != '状态'){
        this.$refs.tableFile1.toggleRowSelection(row);
      }
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id
    },
    bodyCloseMenus(e) {
      let self = this;
      if(self.boxShow == true){
        if (this.$refs.main && !this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
          // if (self.boxShow == true) {
            self.boxShow = false;
          }
        // }
      }
      
    },
    // bodyCloseMenus1(e) {
    //   let self = this;
    //   if (this.$refs.main1 && !this.$refs.main1.contains(e.target)) {
    //     if (self.boxShow1 == true) {
    //       self.boxShow1 = false;
    //     }
    //   }
    // },
    // handleClick(tab, event) {
    //   this.dictCode = tab.index;
    //   this.queryParams.strategyGroup = Number(tab.index) + Number(1);
    //   this.$refs.tableFile1.clearSelection();
    //   //  this.$refs.tableFile2.clearSelection();
    //   this.getList();
    // },
    closeDialogEvent() {
      let index = this.strategyForm.strategyType;
      switch (index) {
        case '0':
          this.$refs.manualControlEvent.resetForm();
          break;
        case '2':
          this.$refs.autoControlEvent.resetForm();
          break;
      }
      console.log(this.strategyForm.strategyType);
      this.strategyForm.strategyType = "";
      this.dialogVisibleEvent = false;
      this.getList();
    },
    // 每次点击取消按钮，策略类型赋空
    closeDialog(flag) {
      let index = this.strategyForm.strategyType;
      switch (index) {
        case '0':
          console.log(this.strategyForm.strategyType);
          this.$refs.manualControl.resetForm();
          this.$refs.manualControl.closeBoard();
          break;
        case '1':
          this.$refs.timingControl.resetForm();
          this.$refs.timingControl.closeBoard();
          break;
        case '2':
          this.$refs.autoControl.resetForm();
          this.$refs.autoControl.closeBoard();
          break;
        case '3':
          this.$refs.timeControl.resetForm();
          this.$refs.timeControl.closeBoard();
          break;
      }
      // console.log(this.strategyForm.strategyType,"0000000000");
      this.strategyForm.strategyType = "";
      this.dialogVisible = false;
      this.$refs.tableFile1.clearSelection();
      if(flag){
        this.getList();
      }

    },
    changeStrategyState(row,index) {
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
    },
    // 改变策略类型
    changeStrategyType() {
      this.resetForm();
    },
    resetForm() {
      this.eqForm = {
        equipment_type: null, //设备类型ID
        direction: "", //方向
        index: null, //当前设置的
      };
    },
    //新建策略按钮
    openInsertStrategy(type) {
      this.title = "新增策略";
      this.sink = "add";
      // 日常策略
      // this.strategyForm.strategyType = 0;
      if(type=="shoudong"){
        this.$set(this.strategyForm, "strategyType", "0");
      }else if(type=="dingshi"){
        this.$set(this.strategyForm, "strategyType", "1");
      }else if(type=="zidong"){
        this.$set(this.strategyForm, "strategyType", "2");
      }

      this.$nextTick(() => {
        // if (type == "richang") {
          this.dialogVisible = true;
          this.strategyTypeClose();
        // } else if (type == "event") {
        //   this.dialogVisibleEvent = true;
        //   this.strategyTypeEventClose();
        // }
      })
    },
    /** 编辑修改按钮操作 */
    handleUpdate(row) {
      console.log(row,"编辑修改按钮操作")
      this.title = "策略编辑";
      this.strategyForm.strategyType = row.strategyType;
      this.sink = "edit";
      console.log(this.queryParams.strategyGroup)
      // debugger
      if (this.queryParams.strategyGroup == 1) {
        this.$nextTick(() => {
          this.dialogVisible = true;
          this.strategyTypeClose(row);
        });
      } else if (this.queryParams.strategyGroup == 2) {
        this.dialogVisibleEvent = true;
        this.strategyTypeEventClose(row);
      }
    },
    //0:手动 1：自动
    strategyTypeEventClose(row) {
      this.$nextTick(() => {
        switch (this.strategyForm.strategyType) {
          case "0":
            this.$refs.manualControlEvent.sink = this.sink;
            this.$refs.manualControlEvent.init();
            if (this.sink == "edit") {
              // this.$refs.manualControl.sink = "edit";
              this.$refs.manualControlEvent.id = row.id;
              this.$refs.manualControlEvent.getStrategyData(row);
            }
            break;
          case "2":
            this.$refs.autoControlEvent.sink = this.sink;
            this.$refs.autoControlEvent.init();
            if (this.sink == "edit") {
              // this.$refs.autoControl.sink = "edit";
              this.$refs.autoControlEvent.id = row.id;
              this.$refs.autoControlEvent.getStrategyData(row);
            }
            break;
        }
      });
    },
    richanghandleUpdate(row) {
      let that = this
      this.$confirm('是否确认执行控制?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          that.richangUpdate(row)
        })
    },
    //手动执行
    async richangUpdate(row) {
      let params = row;
      const loading = this.$loading({
          lock: true,
          text: "Loading",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      await listRl({ strategyId: params.id }).then((response) => {
        // console.log(response, "设备数据");
        params.manualControl = response.rows;
        params.manualControl.equipmentTypeId = params.manualControl.eqTypeId;
        for (let i = 0; i < response.rows.length; i++) {
          let attr = response.rows[i];
          let manualControl = params.manualControl[i];
          if(!!params.manualControl[i].equipments){
            this.$set(params.manualControl[i], "value", params.manualControl[i].equipments.split(","));
          }

          // console.log(params.manualControl[i].value, "选择的设备");
          params.manualControl[i].state = attr.state;
          params.manualControl[i].stateNum = attr.stateNum;

          params.manualControl[i].manualControlStateList = attr.eqStateList;

          params.manualControl[i].equipmentTypeId = String(attr.eqTypeId);
          // 情报板设备
          if (
            params.manualControl[i].equipmentTypeId == 16 ||
            params.manualControl[i].equipmentTypeId == 36
          ) {
            // 改变数据类型
            params.manualControl[i].state = +attr.state;
          }

          //基本照明限制 最低亮度为 30
          if (params.manualControl[i].equipmentTypeId == 9) {
            this.$set(params.manualControl[i], "limitMin", 30);
          }
        }
      });
      await manualControlInfo(params).then((res) => {
        loading.close();
        if (res.code == 200 ) {
          this.$modal.msgSuccess("执行成功");
        }
      }).catch(()=>{
        loading.close();
      });
    },
    // 策略改变触发方法
    // this.strategyForm.strategyType 对应字典表组件索引
    //0:手动 1：定时 2：分时
    strategyTypeClose(row) {
      console.log(this.$refs.manualControl, "获取组件");
      this.$nextTick(() => {
        console.log(this.strategyForm.strategyType)
        switch (this.strategyForm.strategyType) {
          case "0":
            this.insertStrategyTypeOptionsCopy=[]
            let Strategy = this.insertStrategyTypeOptions.find((item) => item.dictLabel =="手动控制")
            this.insertStrategyTypeOptionsCopy.push(Strategy)
            this.$refs.manualControl.sink = this.sink;
            this.$refs.manualControl.init();
            if (this.sink == "edit") {
              console.log(this.$refs, "ppppppppp");
              // this.$refs.manualControl.sink = "edit";
              this.$refs.manualControl.id = row.id;
              this.$refs.manualControl.getStrategyData(row);
            }
            break;

          case "1":
            // debugger
            console.log(this.insertStrategyTypeOptions)
            this.insertStrategyTypeOptionsCopy=[]
            let dictLabelStrategy = this.insertStrategyTypeOptions.find((item) => item.dictLabel =="定时控制")
            this.insertStrategyTypeOptionsCopy.push(dictLabelStrategy)
            this.$refs.timingControl.sink = this.sink;
            this.$refs.timingControl.init();
            if (this.sink == "edit") {
              this.$nextTick(() => {
                // this.$refs.timingControl.sink = "edit";
                this.$refs.timingControl.id = row.id;
                this.$refs.timingControl.getStrategyData(row);
              });
            }
            break;

          case "2":
            this.insertStrategyTypeOptionsCopy=[]
            let Strategys = this.insertStrategyTypeOptions.find((item) => item.dictLabel =="自动触发")
            this.insertStrategyTypeOptionsCopy.push(Strategys)
            this.$refs.autoControl.sink = this.sink;
            this.$refs.autoControl.init();
            if (this.sink == "edit") {
              // this.$refs.autoControl.sink = "edit";
              this.$refs.autoControl.id = row.id;
              this.$refs.autoControl.getStrategyData(row);
            }
            break;

          case "3":
            this.$refs.timeControl.sink = this.sink;
            this.$refs.timeControl.init();
            if (this.sink == "edit") {
              // this.$refs.timeControl.sink = "edit";
              this.$refs.timeControl.id = row.id;
              this.$refs.timeControl.getStrategyData(row);
            }
            break;
        }
      });
    },
    //重置方法
    rest() {
      this.strategyForm.triggers.deviceId = null;
      this.strategyForm.triggers.elementId = null;
      this.deviceName = [];
      this.dataItem = [];
    },
    // 设备名称查询设备数据类型项
    selectDataItem(e) {
      listItem({deviceTypeId: this.strategyForm.triggers.deviceTypeId}).then(
        (res) => {
          this.dataItem = res.rows;
        }
      );
    },
    //选择隧道
    changeTunnel() {
      if (this.strategyForm.strategyType == 2) {
        autoEqTypeList().then((res) => {
          this.eqTypeList = res.rows;
        });
      }
    },
    selectAll() {
      if (this.eqForm.equipments.length < this.equipmentData.length) {
        this.eqForm.equipments = [];
        this.equipmentData.map((item) => {
          this.eqForm.equipments.push(item.eqId);
        });
        // this.eqForm.equipments.remove('全选')
      } else {
        this.eqForm.equipments = [];
      }
    },
    selectDirectionAll(val) {
      this.eqForm.equipments = [];
      this.equipmentData.map((item) => {
        if (val == item.eqDirection) {
          this.eqForm.equipments.push(item.eqId);
        }
      });
    },
    handleController(row) {
      this.$confirm("是否确认执行！", "警告", {
        confirmButtonText: "执行",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$modal.msgSuccess("手动控制中.......");
          return handleStrategy(row.id);
        })
        .then(() => {
          this.$modal.msgSuccess("手动控制成功");
        })
        .catch((_) => {
        });
    },
    //public 查询设备可控状态
    listEqTypeStateIsControl(params) {
      listEqTypeStateIsControl(params).then((response) => {
        this.manualControlStateList = response.rows;
      });
    },
    viewStrategyInfo(row, index) {
      if (row.strategyType == "1") {
        if (index == 0) {
          return;
        } else {
          index--;
        }
      }
      this.eqForm.equipments = [];
      listRl({strategyId: row.id}).then((response) => {
        let strategyRl = response.rows[index];
        let strategyRlArr = strategyRl.equipments.split(",");
        this.eqForm.equipment_type = strategyRl.eqTypeId;
        this.eqForm.eqType = strategyRl.eqTypeId;
        this.eqForm.sdName = row.tunnels.tunnelName;
        this.eqForm.clName = row.strategyName;
        this.eqForm.clInfoName =
          row.slist[row.strategyType == "1" ? (index += 1) : index];
        listDevices({
          eqType: this.eqForm.equipment_type,
          eqTunnelId: row.tunnelId,
        }).then((response) => {
          let a = response.rows;
          if (response.rows != null && response.rows.length > 0) {
            for (let i = 0; i < a.length; i++) {
              if (a[i].remark == null) {
                a[i].remark = "无备注";
              }
            }
          }
          this.equipmentData = a;
        });
        for (var i = 0; i < strategyRlArr.length; i++) {
          this.eqForm.equipments.push(strategyRlArr[i]);
        }
        this.title = "策略信息";
        this.chooseEq = true;
        this.viewStrategy = true;
      });
    },
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then((response) => {
        console.log(response,'隧道列表');
        this.tunnelData = response.rows;
        this.getList()
      });
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
      });
    },
    getEquipmentHasType() {
      listHasType(this.strategyForm.tunnelId).then((response) => {
        this.equipmentTypeData = response.rows;
      });
    },
    /** 根据设备类型查询设备列表 */
    getEquipmentByType(id) {
      listDevices().then((response) => {
        this.equipmentTypeData = response.rows;
      });
    },
    //关闭drawer
    handleClose(done) {
      let index = this.strategyForm.strategyType;
      console.log(index,"index")
      switch (index) {
        case '0':
          this.$refs.manualControl.resetForm();
          this.$refs.manualControl.closeBoard();
          break;
        case '1':
          this.$refs.timingControl.resetForm();
          this.$refs.timingControl.closeBoard();
          break;
        case '2':
          this.$refs.autoControl.resetForm();
          this.$refs.autoControl.closeBoard();
          break;
        case '3':
          this.$refs.timeControl.resetForm();
          // this.$refs.timeControl.closeBoard();
          break;
      }
      console.log(this.strategyForm.strategyType);
      this.strategyForm.strategyType = "";
      this.dialogVisible = false;
      // this.$refs.cron.checkClear();
      done();
    },
    handleCloseEvent(done) {
      let index = this.strategyForm.strategyType;
      switch (index) {
        case '0':
          this.$refs.manualControlEvent.resetForm();
          break;
        case '2':
          this.$refs.autoControlEvent.resetForm();
          break;
      }
      this.strategyForm.strategyType = "";
      this.dialogVisible = false;
      // this.$refs.cron.checkClear();
      done();
    },
    /** 查询控制策略列表 */
    getList() {
      console.log( this.queryParams,this.$cache.local.get(
          "manageStationSelect"
        ),"this.queryParams" )
      this.loading = true;
      this.queryParams.strategyGroup = this.activeName == 'one' ? '1' : '2'
      if (this.manageStatin == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        )
      }
      listStrategy(this.queryParams).then((response) => {
        this.strategyList = response.rows;
        console.log(this.strategyList, 'this.strategyListthis.strategyListthis.strategyListthis.strategyList')
        this.total = response.total;
        this.loading = false;
      });
    },
    //模式中转换
    eventTypeFormatEvent(row, column) {
      // console.log(row)
      for (let i = 0; i < this.eventTypeList.length; i++) {
        if (row.eventType == this.eventTypeList[i].id) {
          return this.eventTypeList[i].eventType;
        }
      }
    },
    //日常预警字典值翻译
    strategyTypeFormat(row, column) {
      return this.selectDictLabel(this.strategyTypeOptions, row.strategyType);
    },
    // 预警字典值翻译
    strategyTypeFormatEvent(row, column) {
      return this.selectDictLabel(this.strategyTypeEvent, row.strategyType);
    },
    // 设备方向字典翻译
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionOptions, row.direction);
    },
    //选择设备提交按钮
    submitChooseEqForm() {
      // 1.赋值 2.比对之前的是否重复   3.根据设备类型查询控制状态  66666
      let index = this.strategyForm.autoControl.length - 1; //求最后一位
      this.strategyForm.autoControl[index].value = this.eqForm.equipments;
      this.strategyForm.autoControl[index].type = this.eqForm.equipment_type;
      this.equipmentTypeData.forEach((item) => {
        if (item.typeId == this.eqForm.equipment_type) {
          this.strategyForm.autoControl[index].typeName = item.typeName;
        }
      });
      this.chooseEq = false; //关闭弹窗
      this.index = 0;
      listEqTypeStateIsControl({
        stateTypeId: this.eqForm.equipment_type,
        isControl: 1,
      }).then((response) => {
        this.strategyForm.autoControl[index].eqStateList = response.rows;
        this.$forceUpdate();
      });
    },
    // 选择设备取消按钮
    cancelChooseEq() {
      this.chooseEq = false;
      this.index = 0;
      this.resetEqForm();
    },
    resetEqForm() {
      this.eqForm = {
        equipments: [],
        equipment_type: null,
      };
      this.resetForm("eqForm");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.$refs.tableFile1.clearSelection();
      //this.$refs.tableFile2.clearSelection();
      this.strategyList = []

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      // debugger
      this.queryParams.pageNum = 1;
      this.queryParams.tunnelId = null
      this.queryParams.strategyName = null
      this.queryParams.strategyInfo = null
      this.queryParams.schedulerTime = null
      this.queryParams.jobTime = null
      console.log( this.queryParams )
      this.handleQuery();
      // this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.rlIds = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /*模式选择*/
    submit1() {
      this.model = "1";
      return this.model;
    },
    submit2() {
      this.model = "2";
      return this.model;
    },
    openScheDialog() {
      this.chooseScheduleTimeVb = true;
      this.title = "设定时间";
      //this.getEquipmentType();
    },
    // 设定时间取消按钮
    cancelChooseTime() {
      this.chooseScheduleTimeVb = false;
    },
    openEqDialog2(event, index) {
      // console.log(index, "当前点击索引");
      // console.log(this.eqForm.equipment_type, "当前设备类型");
      // console.log(this.strategyForm.autoControl[index]);
      if (this.strategyForm.autoControl[index].type == "") {
        this.equipmentData = [];
      }
      this.eqForm.equipments = this.strategyForm.autoControl[index].value;
      this.eqForm.equipment_type = this.strategyForm.autoControl[index].type;
      this.chooseEq = true;
      this.title = "选择设备";
      this.getEquipmentHasType();
    },

    //状态码转换状态名称
    turnToStateName(eqState, index, state) {
      let obj = {};
      obj = eqState.find((item) => {
        return item.deviceState === state;
      });
      this.list[index].twoName = obj.stateName;
    },
    resetStrategyInfo() {
      this.oneId = [];
      this.oneIdEqTypeId = [];
      this.eqState = [];
      this.twoId = [];
      this.list = [{oneId: "", twoId: ""}];
      this.selectedList = []; //存储每次option选中的集合
      this.strategyForm.strategyType = null;
      this.strategyForm.tunnelId = null;
      this.strategyForm.strategyName = null;
      this.strategyForm.equipments = []; //设备名称
      this.strategyForm.equipmentstr = [];
      this.strategyForm.equipmentStates = [];
      this.strategyForm.schedulerTime = null;
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this
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
              this.$refs.tableFile1.clearSelection()
              this.$modal.msgSuccess(res.msg);
            } else {
              this.$modal.msgError(res.msg);
            }
            this.getList();
          });
        })
        .catch(function () {
          that.$refs.tableFile1.clearSelection();
        });
      this.model = "1";
    },
    /** 导出按钮操作 */
    handleExport(flag) {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      let confirmInfo = "";
      if (flag == 1) {

        if(this.tableType=="shoudong"){
          confirmInfo = "是否确认导出所有的手动控制策略数据项？";
        }else if(this.tableType=="dingshi"){
          confirmInfo = "是否确认导出所有的定时控制策略数据项？";
        }else if(this.tableType=="zidong"){
          confirmInfo = "是否确认导出所有的触发控制策略数据项？";
        }
        // confirmInfo = "是否确认导出所有的日常策略数据项？";
        if (this.ids.length > 0) {
          if(this.tableType=="shoudong"){
            confirmInfo = "是否确认导出所选手动控制策略数据项？";
          }else if(this.tableType=="dingshi"){
            confirmInfo = "是否确认导出所选定时控制策略数据项？";
          }else if(this.tableType=="zidong"){
            confirmInfo = "是否确认导出所选触发控制策略数据项？";
          }
        }
      }
      if (flag == 2) {
        confirmInfo = "是否确认导出所有的预警策略数据项？";
        if (this.ids.length > 0) {
          confirmInfo = "是否确认导出所选的预警策略数据项？";
        }
      }
      this.$modal.confirm(confirmInfo).then(() => {
        this.exportLoading = true;
        return export1(queryParams);
      }).then(response => {
        // debugger
        console.log(response.msg)
        this.$download.name(response.msg);
        this.exportLoading = false;
        this.$refs.tableFile1.clearSelection();
        this.$refs.tableFile2.clearSelection();
        this.queryParams.ids = ''
      }).catch(() => {
      });
    },
    /** 添加定时任务 */
    addJobData(guid, str) {
      //  if(this.model == '2'){
      addJob({
        jobName: this.strategyForm.strategyName, //任务名称
        invokeTarget: "strategyTask.strategyParams('" + guid + "')", //调用目标字符串
        cronExpression: this.strategyForm.schedulerTime, //corn表达式
        misfirePolicy: "1", //计划执行错误策略（1立即执行 2执行一次 3放弃执行）
        concurrent: "0", //'是否并发执行（0允许 1禁止）'
        status: "0", //状态（0正常 1暂停）'
        relationId: this.guid,
      }).then((response) => {
        if (response.code === 200) {
          this.$modal.msgSuccess("新增任务成功");
        } else {
          this.$modal.msgError("新增失败，请删除重建控制策略！");
        }
      });
    },
    changeValue(value) {
      this.changeVal = value;
    },

  },
  props:{
    tableType: {
      type: String,
      default: 'shoudong'
    },
  },
  watch:{
    dialogVisible:{
      handler(val){
        this.$refs.tableFile1.clearSelection()
      }
    },
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
    },
    tableType:{
      handler(val){
        console.log(val,"tableTypetableTypetableTypetableTypetableTypetableTypetableTypetableType")
        this.strategyList = []
        this.tableType = val
        if(val=="shoudong"){
          this.queryParams.strategyType = "0";
          this.tableKey = 0
        }else if(val=="dingshi"){
          this.queryParams.strategyType = "1";
          this.tableKey = 1
        }else if(val=="zidong"){
          this.queryParams.strategyType = "2";
          this.tableKey = 2
        }
        //搜索
        this.handleQuery()
      }
    }
  }
};
</script>
<style>
.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}

.el-table .cell {
  white-space: pre-line;
}
</style>
<style scoped lang="scss">
::v-deep .el-dialog .el-dialog__header {
  background-image: url(../../../../assets/cloudControl/dialogHeader.png);
  background-repeat: no-repeat;
  background-position-x: right;
}

::v-deep .el-tabs {
  height: 100%;

  .el-tabs__item {
    height: 4vh;
    font-size: 0.7vw;
  }

  .el-tabs__content {
    height: calc(100% - 5vh);

    .el-tab-pane {
      height: 100%;

      .contentListBox {
        height: 60vh;
        overflow-x: hidden;
        overflow-y: auto;

        .contentBox {
          height: 14vh;
        }
      }
    }
  }
}

::v-deep .el-tabs__header {
  margin: 0 0 8px !important;
}
</style>

