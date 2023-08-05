<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog dispatchWorkBench"
      title="工作台"
      width="83%"
      append-to-body
      :visible="visible"
      :before-close="handleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="header">
        <div class="headerLeft">
          <div>{{ tunnelStationName }}</div>
          <div>{{ tunnelName }}</div>
        </div>
        <div class="headerRight">
          <div class="display-box zoomClass" ref="treeBox">
            <el-input
              placeholder="请输入内容"
              v-model="screenEqName"
              class="input-with-select"
              clearable
              @click.native="treeClick()"
              @keyup.enter.native="screenEqNameButton()"
              @clear="treeClear"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="screenEqNameButton()"
              ></el-button>
            </el-input>
            <!-- 搜索栏树状结构 -->
            <div
              class="treeBox"
              ref="treeBox"
              v-show="treeShow"
              :style="dragFlag ? '47%' : '54.5%'"
            >
              <el-tree
                :show-checkbox="false"
                :data="treeData"
                :props="defaultProps"
                @node-click="handleNodeClick"
                :filter-node-method="filterNode"
                accordion
                ref="tree"
                node-key="id"
              ></el-tree>
            </div>
          </div>
          <div class="display-box zoomClass">
            <!-- <p class="zoom-title" style="font-size: 0.75vw">缩放：</p> -->
            <el-input-number
              v-model="zoom"
              :step="10"
              :min="60"
              :max="140"
              step-strictly
              @change="zoomChange"
            >
              {{ zoom + "%" }}
            </el-input-number>
          </div>
          <el-button
            v-if="resetCanvasFlag && currentTunnel.lane.width > 1728"
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-map-location"
            @click="resetCanvas"
          >
            地图复位
          </el-button>
          <el-button
            class="buttons"
            type="primary"
            size="mini"
            @click="batchManage"
            v-if="batchManageType == 1"
          >
            <img src="../../../assets/icons/plcz.png" />
            <span>批量操作</span>
          </el-button>
          <div v-if="batchManageType == 2" class="batchManageButton">
            <div @click="closeBatchManageDialog">取消</div>
            <div @click="implementBatchManage">执行</div>
          </div>
          <el-button
            class="buttons"
            type="primary"
            size="mini"
            @click="iconExplain"
          >
            <img src="../../../assets/icons/tbhy.png" />
            <span>图标含义</span>
          </el-button>
          <div class="buttonsDeawer" @click="isDrawer()">
            <img
              src="../../../assets/icons/deawer.png"
              :style="{
                transform: buttonsDeawer ? 'rotate(180deg)' : '',
              }"
            />
          </div>
        </div>
      </div>
      <div class="footer">
        <div class="eqTypeListClass">
          <div
            type="info"
            size="small"
            v-for="(item, index) in dictList"
            :key="item.raw.dictCode"
            :label="item.label"
            :value="index"
            @click="displayControl(index, item.label)"
            class="leftButtonS"
          >
            <div>{{ item.label }}</div>
          </div>
        </div>
        <div class="vehicleLane">
          <div
            class="content"
            ref="divRoller"
            @wheel.prevent="handleTableWheel"
            @contextmenu.prevent
            @mouseover="mouseoversImage"
            @mouseleave="mouseleaveImage"
          >
            <div class="workbench-content" ref="dragImgDom">
              <!--画布区域-->
              <div>
                <el-row
                  class="config-img-area"
                  id="config-img-id"
                  :style="{ width: currentTunnel.lane.width / 1.2 + 'px' }"
                >
                  <el-image
                    class="back-img"
                    :src="currentTunnel.lane.url"
                    :style="{ width: currentTunnel.lane.width / 1.2 + 'px' }"
                  ></el-image>
                  <div class="wrapper" id="eq-wrapper" ref="eqWrapper">
                    <!-- 设备图标-->
                    <div
                      class="icon-box mouseHover"
                      v-for="(item, index) in selectedIconList"
                      :key="item.eqId"
                      :style="{
                        left: item.position.left / 1.2 + 'px',
                        top: item.position.top / 1.2 + 'px',
                        'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                      }"
                      :class="
                        item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                          ? 'light-' + item.position.left
                          : ''
                      "
                      @click="openStateSwitch(item)"
                      @mousemove="openTooltip(item, index)"
                      @mouseleave="closeTooltip(item)"
                    >
                      <!-- 设备图标上提示文字 -->

                      <div
                        v-if="item.click"
                        class="screenEqNameBox"
                        :style="{
                          top:
                            item.tooltipType == 1 || item.tooltipType == 2
                              ? '35px'
                              : '-50px',
                          left:
                            item.tooltipType == 1 || item.tooltipType == 3
                              ? '0px'
                              : '-100px',
                        }"
                      >
                        {{ item.eqName }}
                      </div>
                      <div
                        v-if="item.textFalse"
                        class="textFalseBox"
                        :style="{
                          top:
                            item.tooltipType1 == 1 || item.tooltipType1 == 2
                              ? '35px'
                              : '-50px',
                          left:
                            item.tooltipType1 == 1 || item.tooltipType1 == 3
                              ? '0px'
                              : '-100px',
                        }"
                      >
                        请选择同种设备
                      </div>
                      <div
                        v-if="item.textKKFalse"
                        class="textFalseBox"
                        :style="{
                          top:
                            item.tooltipType1 == 1 || item.tooltipType1 == 2
                              ? '35px'
                              : '-50px',
                          left:
                            item.tooltipType1 == 1 || item.tooltipType1 == 3
                              ? '0px'
                              : '-100px',
                        }"
                      >
                        请选择可控设备
                      </div>
                      <div
                        class="tooltipBox"
                        v-if="showTooltipIndex == index"
                        :style="{
                          top:
                            item.tooltipType == 1 || item.tooltipType == 2
                              ? '35px'
                              : '-50px',
                          left:
                            item.tooltipType == 1 || item.tooltipType == 3
                              ? '0px'
                              : -(item.eqName.length * 10 + 40) + 'px',
                        }"
                      >
                        <span>名称：{{ item.eqName }}</span
                        ><br />
                        <span>方向：{{ getDirection(item.eqDirection) }}</span>
                      </div>
                      <div
                        v-show="
                          (item.eqType != 7 &&
                            item.eqType != 15 &&
                            item.eqType != 8 &&
                            item.eqType != 9 &&
                            item.display == true) ||
                          ((item.eqType == 7 ||
                            item.eqType == 8 ||
                            item.eqType == 9 ||
                            item.eqType == 21) &&
                            item.display == true &&
                            lightSwitch == 1)
                        "
                        :class="{ focus: item.focus }"
                      >
                        <img
                          v-show="
                            item.eqType != '31' &&
                            item.eqType != '16' &&
                            item.eqType != '36'
                          "
                          v-for="(url, indexs) in item.url"
                          style="position: absolute"
                          :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 2
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth / 1.2"
                          :height="item.iconHeight / 1.2"
                          :key="item.eqId + indexs"
                          :src="url"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        />
                        <img
                          v-show="item.eqType == '31'"
                          style="position: absolute"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.iconWidth / 1.2 + 'px',
                            height: item.iconHeight / 1.2 + 'px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        />
                        <div
                          v-show="item.eqType == 16"
                          class="boardBox1"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'width',
                                  item.eqType
                                ) + 'px'
                              : item.iconWidth + 'px',
                            height: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'height',
                                  item.eqType
                                ) + 'px'
                              : item.iconHeight + 'px',
                            fontSize: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'fontSize',
                                  item.eqType
                                ) + 'px'
                              : '15px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        >
                          <div
                            :style="{
                              animation:
                                'boardBox1 ' +
                                Number(
                                  getBoardStyle(
                                    item.associated_device_id,
                                    'content'
                                  ).length
                                ) *
                                  1.3 +
                                's' +
                                ' linear infinite',
                            }"
                          >
                            <span
                              v-for="itm in getBoardStyle(
                                item.associated_device_id,
                                'array'
                              )"
                              :key="itm.associated_device_id"
                              :style="{
                                color: getColorStyle(itm.COLOR),
                              }"
                              style="padding-top: 10px"
                              >{{ itm.CONTENT }}</span
                            >
                          </div>
                        </div>
                        <div
                          v-show="item.eqType == 36"
                          class="boardBox2"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'width',
                                    item.eqType
                                  ) + 'px'
                                : item.iconWidth + 'px',
                            height:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'height',
                                    item.eqType
                                  ) + 'px'
                                : item.iconHeight + 'px',
                            fontSize:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'fontSize',
                                    item.eqType
                                  ) + 'px'
                                : '15px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        >
                          <div
                            :style="{
                              animation:
                                'boardBox2 ' +
                                Number(
                                  getBoardStyle(
                                    item.associated_device_id,
                                    'content'
                                  ).length
                                ) *
                                  1.3 +
                                's linear infinite',
                            }"
                          >
                            <span
                              v-for="itm in getBoardStyle(
                                item.associated_device_id,
                                'array'
                              )"
                              :key="itm.associated_device_id"
                              :style="{
                                color: getColorStyle(itm.COLOR),
                              }"
                              style="padding-top: 10px"
                              >{{ itm.CONTENT }}</span
                            >
                          </div>
                        </div>
                        <!-- 调光数值 -->
                        <!-- <label
                          style="
                            color: yellow;
                            position: absolute;
                            left: 30px;
                            bottom: 2px;
                            pointer-events: none;
                          "
                          v-if="item.eqType == 21"
                          >{{ item.lightValue }}</label
                        > -->
                        <!-- CO/VI -->
                        <!-- <label
                          style="color: #79e0a9"
                          class="labelClass"
                          v-if="item.eqType == 19"
                        >
                          {{ item.num }}
                        </label> -->
                        <!-- 风速风向 -->
                        <!-- <label
                          style="color: #79e0a9"
                          class="labelClass"
                          v-if="item.eqType == 17"
                        >
                          {{ item.num }}
                        </label> -->
                        <!-- 洞内洞外 -->
                        <!-- <label
                          style="color: #f2a520"
                          class="labelClass"
                          v-if="item.eqType == 5 || item.eqType == 18"
                        >
                          {{ item.num }}
                        </label> -->
                        <!-- 加强照明 -->
                        <!-- <label
                          style="color: #f2a520"
                          class="labelClass labelClass7"
                          v-if="item.eqType == 7"
                        >
                          {{ item.num }}
                        </label>
                        <label
                          style="color: #f2a520"
                          class="labelClass labelClass9"
                          v-if="item.eqType == 9"
                        >
                          {{ item.num }}
                        </label> -->
                      </div>
                    </div>
                  </div>
                </el-row>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-drawer
        title="一键控制"
        :visible.sync="buttonsDeawer"
        :modal="false"
        :append-to-body="true"
        class="drawerBox"
      >
        <el-row>
          <el-col :span="12">
            <div class="drawerItem zoomClass">
              <p
                class="zoom-title"
                style="font-size: 0.7vw; margin-right: 0.5vw"
              >
                {{ srollSwitch ? "底图滚动关" : "底图滚动开" }}
              </p>
              <el-switch
                v-model="srollSwitch"
                class="switchStyle"
                @change="srollSwitchChange"
              ></el-switch>
            </div>
          </el-col>
          <!-- <el-col :span="12">
            <div class="drawerItem zoomClass">
              <p
                class="zoom-title"
                style="font-size: 0.7vw; margin-right: 0.5vw"
              >
                {{ carShow ? "实时车辆关" : "实时车辆开" }}
              </p>
              <el-switch
                v-model="carShow"
                class="switchStyle"
                @change="carShowChange"
              ></el-switch>
            </div>
          </el-col> -->
          <el-col :span="12">
            <div class="drawerItem zoomClass">
              <p
                class="zoom-title"
                style="font-size: 0.7vw; margin-right: 0.5vw"
              >
                {{ zoomSwitch == 0 ? "缩放比例开" : "缩放比例关" }}
              </p>
              <el-switch
                v-model="zoomSwitch"
                class="switchStyle"
                @change="zoomSwitchChange"
              ></el-switch>
            </div>
          </el-col>
        </el-row>
      </el-drawer>
    </el-dialog>
    <!--图标含义对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table icon-dialog"
      :title="title"
      :visible.sync="explainVisible"
      width="1240px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img
        src="@/assets/logo/equipment_log/all.png"
        style="width: 100%; height: auto"
      />
      <div slot="footer" class="dialog-footer">
        <el-button class="closeButton" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <com-video class="comClass" ref="videoRef"></com-video>
    <com-light class="comClass" ref="lightRef"></com-light>
    <com-covi class="comClass" ref="coviRef"></com-covi>
    <com-data class="comClass" ref="dataRef"></com-data>
    <com-wind class="comClass" ref="windRef"></com-wind>
    <com-pressure class="comClass" ref="pressureRef"></com-pressure>
    <com-vehicleDetec class="comClass" ref="vehicleDetecRef"></com-vehicleDetec>
    <com-callPolice class="comClass" ref="callPoliceRef"></com-callPolice>
    <com-xfp class="comClass" ref="xfpRef"></com-xfp>
    <div v-if="robotIframeShow">
      <robot class="comClass robotHtmlBox"></robot>
      <img
        @click="dialogClose"
        src="../../../assets/cloudControl/closeIcon.png"
        class="closeRobot"
      />
    </div>

    <com-bright class="comClass" ref="brightRef"></com-bright>
    <com-youdao class="comClass" ref="youdaoRef"></com-youdao>
    <com-board class="comClass" ref="boardRef"></com-board>
    <com-radio class="comClass" ref="radioRef"></com-radio>
    <com-kzq class="comClass" ref="kzqRef"></com-kzq>
  </div>
</template>
<script>
import { laneImage } from "../../../utils/configData.js";
import {
  listTunnels,
  getTunnels,
  updateTunnels,
  getConfigData,
  // getStorageData,
  setConfigData,
  pressure,
  sendAnalogCom,
  getHostData,
  setCorLight,
  updateCarFinger,
  getDeviceDataAndState,
  getJlyTunnel,
  energyConsumptionDetection,
  getBoardContent,
} from "@/api/equipment/tunnel/api.js";
import {
  listType,
  listHasType,
  getType,
  groupByBigType,
  hasListByBigType,
  loadPicture,
} from "@/api/equipment/type/api.js";
import {
  getSystemWarningMsg,
  getTrafficIncident,
  proportionOfEquipment,
  trafficFlowInformation,
  vehicleMonitoringInRecent24Hours,
  special,
  getDeviceData,
  addBoardContent,
  batchControlCarFinger,
  timeSharing,
  updateControlTime,
  timeStrategySwitch,
  specialVehicleMonitoringInRecent24Hours,
  getCategoryDeviceTree,
  batchControlDevice,
} from "@/api/workbench/config.js";
import {
  listEqTypeState,
  getEqTypeState,
  updateEqTypeState,
  getStateByRun,
  getStateByData,
} from "@/api/equipment/eqTypeState/api";
import {
  listDevices,
  getDevices,
  updateDevices,
  getAudioFileList,
  playVoiceGroup,
  videoStreaming,
  getDeviceById,
} from "@/api/equipment/eqlist/api";
import comVideo from "@/views/workbench/config/components/video"; //摄像机弹窗
import comLight from "@/views/workbench/config/components/light"; //各种带单选框的弹窗
import comCovi from "@/views/workbench/config/components/covi"; //covi弹窗
import comBright from "@/views/workbench/config/components/bright"; //亮度检测器等只有基本信息的弹窗
import comWind from "@/views/workbench/config/components/wind"; //风速风向弹窗
import comPressure from "@/views/workbench/config/components/pressure"; //压力表弹窗
import comVehicleDetec from "@/views/workbench/config/components/vehicleDetec"; //微波车检弹窗
import comCallPolice from "@/views/workbench/config/components/callPolice"; //声光报警弹窗
import comRobot from "@/views/workbench/config/components/robot"; //机器人弹窗
import comData from "@/views/workbench/config/components/data"; //只有数据的弹窗
import comYoudao from "@/views/workbench/config/components/youdao"; //诱导灯弹窗
import comBoard from "@/views/workbench/config/components/board"; //情报板弹窗
import comRadio from "@/views/workbench/config/components/radio"; //广播弹窗
import comXfsb from "@/views/workbench/config/components/xfsb"; //消防水泵弹窗
import comSjb from "@/views/workbench/config/components/sjb"; //潜水深水泵
import robot from "@/views/workbench/config/components/robotManagement"; //机器人弹窗
import comKzq from "@/views/workbench/config/components/kzq"; //鸿蒙控制器
import comXfp from "@/views/workbench/config/components/xfp"; //消防炮

import comTemperatureHumidity from "@/views/workbench/config/components/temperatureHumidity"; //温湿传感器
import comLiquidLevel from "@/views/workbench/config/components/liquidLevel"; //液位传感器
export default {
  dicts: ["sd_sys_name", "sys_common_status", "sd_control_type"],
  components: {
    // DragItDude,
    // videoPlayer,
    // vueSeamlessScroll,
    // vmsContentUpdate,
    // BatteryIcon,
    // contentBatchEdit,
    comVideo,
    comLight,
    comCovi,
    comBright,
    comWind,
    comPressure,
    comVehicleDetec,
    comCallPolice,
    comRobot,
    comData,
    comYoudao,
    comBoard,
    comRadio,
    comKzq,
    comXfsb,
    comSjb, //深水泵
    robot,
    comTemperatureHumidity, //温湿度传感器
    comLiquidLevel, //液位传感器
    // comDeawer, //抽屉
    // comFooter, //底部echarts
    comXfp,
    // jointControlStrategy
  },
  data() {
    return {
      robotIframeShow: false,
      title: "",
      visible: false,
      tunnelId: "",
      tunnelName: "",
      tunnelStationName: "",
      currentTunnel: {
        id: this.tunnelId,
        name: "",
        lane: {},
      }, //选中的隧道
      screenEqName: "",
      treeShow: false,
      dragFlag: false,
      treeData: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      zoom: 100, // 工作台缩放比例
      //地图复位按钮
      resetCanvasFlag: false,
      batchManageType: 1,
      batchManageList: [],
      batchManageForm: {},
      min: 0,
      selectedIconList: [],
      eqTypeStateList: [],
      addBatchManage: false, //点击多选设备
      batchManageDialog: false, //批量操作弹窗
      itemEqId: "",
      itemEqType: "",
      eqTypeStateList2: [],
      explainVisible: false,
      buttonsDeawer: false,
      srollSwitch: false,
      zoomSwitch: false, //缩放
      imageTimer: null, //定时器
      handleTableWheelSwithch: false,
      moveTop: 0.11,
      move: false,
      laneUrlList: laneImage,
      showTooltipIndex: 9999,
      eqInfo: {},
      brandList: [],
      directionList: [{}, {}], //设备方向字典
      eqTypeDialogList: [],
      // 批量选择设备图标
      iconWidth: "",
      iconHeight: "",
      boardObj: [],
      lightSwitch: 0,
      dictList: [],
      selectBigType: {
        bigType: "全部设备",
        index: 0,
      },
      timer: null, //定时器
    };
  },
  watch: {
    "batchManageForm.state": function (newVal, oldVal) {
      // console.log(newVal, "newVal");
      if ([7, 9].includes(this.itemEqType)) {
        // 基础照明、加强照明  state == 1 开启  state == 2  关闭
        if (newVal == "1" && this.batchManageForm.brightness == 0) {
          this.batchManageForm.brightness = 1;
          this.min = 1;
        } else if (newVal == "2" || newVal == "") {
          this.batchManageForm.brightness = 0;
          this.min = 0;
        }
      } else if (this.itemEqType == 30) {
        // 疏散标志 state == 1 关闭 state == 2 常亮 state == 5 报警
        if (newVal == "1") {
          this.batchManageForm.brightness = 0;
          this.batchManageForm.frequency = 0;
          this.min = 0;
        } else if (newVal != "1" && this.batchManageForm.brightness == 0) {
          this.batchManageForm.brightness = 1;
          this.batchManageForm.frequency = 1;
          this.min = 1;
        }
      }
    },
  },
  created() {},
  mounted() {},
  //实例销毁前清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
    this.timer = null;
    clearInterval(this.imageTimer);
    this.imageTimer = null;
    window.removeEventListener("click", this.otherClose);
  },
  methods: {
    init(workBenchProp) {
      console.log(workBenchProp, "workBenchProp");
      this.getDict();
      this.tunnelId = workBenchProp.tunnelId;
      this.tunnelName = workBenchProp.tunnelName;
      this.tunnelStationName = workBenchProp.tunnelStationName;
      this.visible = true;
      this.getBoardContent();
      this.getEqTypeStateIcon();
      this.lightSwitchFunc();
      this.getDictList();
      console.log(this.dict.type.sd_sys_name, "this.dict.type.sd_sys_name");
      // 隧道调取数据两秒一次
      this.timer = setInterval(() => {
        setTimeout(this.getRealTimeData, 0);
        setTimeout(this.getBoardContent, 0);
      }, 1000 * 5);
      window.addEventListener("click", this.otherClose);
    },
    getDict() {
      this.getDicts("brand").then((data) => {
        this.brandList = data.data;
      });
      this.getDicts("sd_strategy_direction").then((data) => {
        this.directionList = data.data;
      });
      this.getDicts("sd_monitor_state").then((data) => {
        this.eqTypeDialogList = data.data;
      });
    },
    handleClosee() {
      this.visible = false;
    },
    otherClose(e) {
      if (this.treeShow == true) {
        if (!this.$refs.treeBox.contains(e.target)) this.treeShow = false;
      }
    },
    getDictList() {
      var newDict = this.dict.type.sd_sys_name;
      console.log(this.dict.type.sd_sys_name, "this.dict.type.sd_sys_name");
      if (this.tunnelId != "JQ-JiNan-WenZuBei-MJY") {
        this.dictList = newDict.slice(0, 8);
      } else if (this.tunnelId == "JQ-JiNan-WenZuBei-MJY") {
        this.dictList = newDict;
      }
      console.log(this.dictList, "this.dictList");
    },
    /* 查询设备状态图标*/
    async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
      };
      await listEqTypeState(queryParams).then((response) => {
        // console.log(response.rows,"response.rows111")
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    async getEqUrl(list) {
      let that = this;
      that.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        that.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
          id: list[i].id,
        });
      }
      console.log(that.eqTypeStateList, "设备图标eqTypeStateList");
      this.getTunnelData();
    },
    /* 获取隧道配置信息*/
    getTunnelData() {
      let that = this;
      that.upList = [];
      that.downList = [];
      //   const params = {
      //     tunnelId: this.tunnelId,
      //   };
      getTunnels(this.tunnelId).then((res1) => {
        that.loading = false;
        let res = res1.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          console.log(res, "获取隧道配置信息");
          listType("")
            .then((response) => {
              // console.log(response,"response888")
              for (let i = 0; i < res.eqList.length; i++) {
                res.eqList[i].focus = false;
                for (let j = 0; j < response.rows.length; j++) {
                  if (response.rows[j].typeId == res.eqList[i].eqType) {
                    let iconWidth = Number(response.rows[j].iconWidth);
                    let iconHeight = Number(response.rows[j].iconHeight);
                    res.eqList[i].iconWidth = iconWidth;
                    res.eqList[i].iconHeight = iconHeight;
                    res.eqList[i].bigType = response.rows[j].bigType;
                    break;
                  }
                }
              }
              that.selectedIconList = res.eqList; //设备zxczczxc
              // 匹配设备方向
              listDevices().then((data) => {
                console.log(data, "设备表");
                for (let item of that.selectedIconList) {
                  for (let itm of data.rows) {
                    if (item.eqId == itm.eqId) {
                      item.eqDirection = itm.eqDirection;
                    }
                  }
                }
              });
              // 匹配设备是否可控
              listType().then((response) => {
                console.log(response.rows, "设备图标 是否可控");
                for (let item of that.selectedIconList) {
                  for (let itm of response.rows) {
                    if (item.eqType == itm.typeId) {
                      item.isControl = itm.isControl;
                    }
                  }
                }
              });
              that.getRealTimeData();

              console.log(
                that.selectedIconList,
                "所有设备图标selectedIconList"
              );
              //   for (var item of that.selectedIconList) {
              //     if (
              //       this.currentTunnel.id == "JQ-JiNan-WenZuBei-MJY" &&
              //       item.eqType == 29
              //     ) {
              //       this.robotShow = true;
              //     } else {
              //       this.robotShow = false;
              //     }
              //   }
            })
            .then(() => {
              // 切换隧道配置信息时，联动大类查询
              that.displayControl(
                that.selectBigType.index,
                that.selectBigType.bigType
              );
            });
          let id = res.lane;
          console.log(that.laneUrlList, "that.laneUrlList");
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.currentTunnel.lane = that.laneUrlList[i];
            }
          }
          this.$forceUpdate();
        } else {
          console.log("不存在");
          //不存在
          that.selectedIconList = [];
        }
      });
      // 树状搜索
      getCategoryDeviceTree(this.tunnelId).then((res) => {
        console.log(res.data, "res.data");
        this.treeData = res.data;
      });
    },
    /* 打开配置界面*/
    async openStateSwitch(item) {
      console.log(item, "item");
      if (this.addBatchManage == true) {
        // 判断设备是否可控 不可控的不弹批量弹窗
        if (item.isControl == "1" && ![16, 22, 36, 29].includes(item.eqType)) {
          // 判断是否有选中项 有的话 判断本次点击和上次点击 设备类型是否一样
          // 要求每次点击选中的设备类型相同
          if (this.itemEqType) {
            // 第二次点击时
            for (var itm of this.selectedIconList) {
              //  多选 选择的设备类型相同时
              if (itm.eqId == item.eqId && this.itemEqType == item.eqType) {
                // 比对id 如果曾点过 取消选框
                const result = this.itemEqId.findIndex(
                  (item) => item == itm.eqId
                );
                if (result === -1) {
                  itm.click = true;
                  this.itemEqId.push(itm.eqId);
                  this.$forceUpdate();
                } else {
                  this.itemEqId.splice(result, 1);
                  itm.click = false;
                  this.$forceUpdate();
                  if (this.itemEqId.length == 0) {
                    this.itemEqType = "";
                    this.batchManageForm.eqType = "";
                    this.batchManageForm.eqDirection = "";
                    // this.addBatchManage = false;
                    this.$forceUpdate();
                  }
                }
              }
              // 多选 选择的设备类型不同时 提示红字
              else if (
                itm.eqId == item.eqId &&
                this.itemEqType != item.eqType
              ) {
                this.tooltipType1(item);
                itm.textFalse = true;
                setTimeout(() => {
                  item.textFalse = false;
                }, 2000);
                this.$forceUpdate();
              }
            }
          } else {
            // 第一次点击时
            for (let itm of this.selectedIconList) {
              // console.log(itm);
              if (itm.eqId == item.eqId) {
                itm.click = true;
                this.itemEqId.push(itm.eqId);
                this.itemEqType = itm.eqType;
                this.batchManageForm.eqType = itm.eqType;
                this.batchManageForm.eqDirection = itm.eqDirection;
                this.$forceUpdate();
                getType(itm.eqType).then((res) => {
                  console.log(res, "查询设备图标宽高");
                  this.iconWidth = res.data.iconWidth;
                  this.iconHeight = res.data.iconHeight;
                });
              }
            }
          }
        } else if (
          item.isControl == "0" ||
          [16, 22, 29, 33, 36].includes(item.eqType)
        ) {
          // 可控设备里 情报板 消防炮 巡检机器人 广播 也不可批量控制
          console.log(item, "1111111111111111");
          this.tooltipType1(item);

          item.textKKFalse = true;
          setTimeout(() => {
            item.textKKFalse = false;
          }, 2000);
          this.$forceUpdate();
        }
      } else if (this.addBatchManage == false) {
        this.mouseoversImplement = false;
        this.eqInfo = {
          clickEqType: item.eqType,
          equipmentId: item.eqId,
        };

        if (this.dialogEqType && this.dialogEqType != item.eqType) {
          this.dialogEqType = item.eqType;
          this.dialogClose();
        } else {
          this.dialogEqType = item.eqType;
        }
        this.$nextTick(() => {
          if ([21, 23, 24, 25, 32, 39, 40].includes(item.eqType)) {
            this.$refs.videoRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (
            [1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 30, 31, 45, 49].includes(
              item.eqType
            )
          ) {
            this.$refs.lightRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 19) {
            this.$refs.coviRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if ([14, 15, 35, 41, 42, 48].includes(item.eqType)) {
            this.$refs.dataRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 17) {
            this.$refs.windRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 28) {
            this.$refs.pressureRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 20) {
            this.$refs.vehicleDetecRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 34) {
            this.$refs.callPoliceRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 5 || item.eqType == 18) {
            this.$refs.brightRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 16 || item.eqType == 36) {
            this.$refs.boardRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 22) {
            this.$refs.radioRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 1067) {
            // 鸿蒙控制器
            this.$refs.kzqRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          } else if (item.eqType == 29) {
            // 巡检机器人
            this.robotIframeShow = true;
          } else if (item.eqType == 33 || item.eqType == 47) {
            // 智能消防炮
            this.$refs.xfpRef.init(
              this.eqInfo,
              this.brandList,
              this.directionList,
              this.eqTypeDialogList
            );
          }
        });
      }
    },
    tooltipType1(item) {
      if (
        item.position.left / 1.2 <= this.currentTunnel.lane.width / 1.2 - 100 &&
        item.position.top / 1.2 <= 350
      ) {
        return (item.tooltipType1 = 1);
      } else if (
        item.position.left / 1.2 > this.currentTunnel.lane.width / 1.2 - 100 &&
        item.position.top / 1.2 <= 350
      ) {
        return (item.tooltipType1 = 2);
      } else if (
        item.position.left / 1.2 <= this.currentTunnel.lane.width / 1.2 - 100 &&
        item.position.top / 1.2 > 350
      ) {
        return (item.tooltipType1 = 3);
      } else {
        return (item.tooltipType1 = 4);
      }
    },
    /*点击设备类型*/
    displayControl(value, lable) {
      // console.log(value, lable,"value, lable")
      // carShow
      //   for (var item of this.selectedIconList) {
      //     if (
      //       this.currentTunnel.id == "JQ-JiNan-WenZuBei-MJY" &&
      //       item.eqType == 29
      //     ) {
      //       this.robotShow = true;
      //     } else {
      //       this.robotShow = false;
      //     }
      //   }
      $(".leftButtonS")
        .eq(value)
        .addClass("leftButton_hover")
        .siblings()
        .removeClass("leftButton_hover");
      this.selectBigType = {
        bigType: lable,
        index: value,
      };

      var val = value.toString();
      hasListByBigType(val).then((response) => {
        // console.log(response.rows,"response.rows")
        let typelist = response.rows;
        let typeIndex = [];
        if (typelist.length > 0) {
          for (let y = 0; y < typelist.length; y++) {
            for (let i = 0; i < this.selectedIconList.length; i++) {
              if (typelist[y].typeId == this.selectedIconList[i].eqType) {
                typeIndex.push(i);
              }
              this.selectedIconList[i].display = false;

              // 没有eqType属性的图片依旧显示 例如：隧道名称
              if (
                this.selectedIconList[i].eqType == null ||
                this.selectedIconList[i].eqType == undefined
              ) {
                this.selectedIconList[i].display = true;
              }
            }
          }
          for (let i = 0; i < typeIndex.length; i++) {
            this.selectedIconList[typeIndex[i]].display = true;
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            this.selectedIconList[i].display = false;
          }
        }
      });
    },
    // 关闭弹窗子组件
    dialogClose() {
      this.mouseoversImplement = true;
      this.$refs.videoRef.handleClosee();
      this.$refs.lightRef.handleClosee();
      this.$refs.coviRef.handleClosee();
      this.$refs.dataRef.handleClosee();
      this.$refs.windRef.handleClosee();
      this.$refs.pressureRef.handleClosee();
      this.$refs.vehicleDetecRef.handleClosee();
      this.$refs.callPoliceRef.handleClosee();
      this.$refs.brightRef.handleClosee();
      this.$refs.youdaoRef.handleClosee();
      this.$refs.boardRef.handleClosee();
      this.$refs.radioRef.handleClosee();
      this.$refs.kzqRef.handleClosee();
      this.$refs.xfpRef.handleClosee();

      this.robotIframeShow = false;
    },
    async getBoardContent() {
      if (this.tunnelId != null && this.tunnelId != "") {
        await addBoardContent(this.tunnelId).then((res) => {
          //   console.log(res, "情报板显示内容查询");
          this.boardObj = res;
        });
      }
      this.$forceUpdate();
    },
    // 模拟定时
    getRealTimeData() {
      getDeviceData({
        tunnelId: this.tunnelId,
      }).then((response) => {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          if (deviceData) {
            // 需要换光标的
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35, 22,
                  40, 39, 48, 41,
                ];
                if (arr.includes(deviceData.eqType)) {
                  if (
                    // 摄像机之类的只有在线 离线 故障图标
                    this.eqTypeStateList[k].stateType == "1" &&
                    this.eqTypeStateList[k].state == deviceData.eqStatus
                  ) {
                    //取设备监测状态图标
                    this.selectedIconList[j].url = this.eqTypeStateList[k].url;

                    // if (deviceData.eqStatus == 1) {
                    //   if (deviceData.eqType == 19) {
                    //     this.selectedIconList[j].num =
                    //       "CO:" +
                    //       parseFloat(deviceData.CO).toFixed(2) +
                    //       "/PPM  VI:" +
                    //       parseFloat(deviceData.VI).toFixed(2) +
                    //       "M";
                    //   } else if (deviceData.eqType == 17) {
                    //     this.selectedIconList[j].num =
                    //       parseFloat(deviceData.FS).toFixed(2) +
                    //       "m/s " +
                    //       deviceData.FX;
                    //   } else if (deviceData.eqType == 5) {
                    //     if (deviceData.DWLD) {
                    //       this.selectedIconList[j].num =
                    //         parseFloat(deviceData.DWLD).toFixed(2) + "lux";
                    //     }
                    //   } else if (deviceData.eqType == 18) {
                    //     if (deviceData.DNLD) {
                    //       this.selectedIconList[j].num =
                    //         parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                    //     }
                    //   } else if (deviceData.eqType == 7) {
                    //     if (deviceData.DNLD) {
                    //       this.selectedIconList[j].num =
                    //         parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                    //     }
                    //   }
                    // }
                  }
                } else {
                  //可以控制设备状态的设备类型，比如车指
                  if (deviceData.eqStatus == "1") {
                    // 在线
                    if (
                      // 车指之类的包括正红反绿之类的图标 == 2
                      this.eqTypeStateList[k].stateType == "2"
                    ) {
                      if (this.eqTypeStateList[k].state == deviceData.state) {
                        // 照明图标后加数据
                        // if (deviceData.eqType == 7 || deviceData.eqType == 9) {
                        //   //   console.log(deviceData, "deviceData");
                        //   this.selectedIconList[j].num = deviceData.brightness;
                        // }
                        //取设备运行状态图标
                        let url = this.eqTypeStateList[k].url;
                        this.selectedIconList[j].eqDirection =
                          deviceData.eqDirection;
                        if (deviceData.eqDirection == "1") {
                          //上行车道
                          if (url.length > 1) {
                            this.selectedIconList[j].url = [url[1], url[0]];
                          } else {
                            this.selectedIconList[j].url = url;
                          }
                        } else {
                          this.selectedIconList[j].url =
                            this.eqTypeStateList[k].url;
                        }
                      }
                    }
                  } else {
                    //如果是离线、故障等状态
                    if (
                      this.eqTypeStateList[k].stateType == "1" &&
                      this.eqTypeStateList[k].state == deviceData.eqStatus
                    ) {
                      //取设备监测状态图标
                      this.selectedIconList[j].url =
                        this.eqTypeStateList[k].url;
                    }
                  }
                }
              }
            }
          }
        }
      });
    },
    //地图复位
    resetCanvas() {
      setTimeout(() => {
        this.resetCanvasFlag = false;
      }, 50);
      this.$refs.dragImgDom.style.left = "0px";
      this.$refs.dragImgDom.style.top = "0px";
    },
    // 模糊查询
    treeClick() {
      // 点击输入框 折叠之前打开的树形菜单
      const nodes = this.$refs.tree.store._getAllNodes();
      nodes.forEach((item) => {
        item.expanded = false;
      });
      if (this.resetCanvasFlag && this.treeShow) {
        setTimeout(() => {
          this.treeShow = false;
        }, 100);
      } else {
        this.treeShow = !this.treeShow;
      }
    },
    // 筛选设备名称
    screenEqNameButton(treeNodeClick) {
      let that = this;
      if (this.screenEqName) {
        let bigType = "";
        let param = document.getElementsByClassName("content");
        for (var item of this.selectedIconList) {
          if (treeNodeClick) {
            if (item.eqName == this.screenEqName) {
              bigType = item.bigType;
              this.resetCanvasFlag = true;
              if (
                this.currentTunnel.lane.width / 1.2 - item.position.left / 1.2 >
                  720 &&
                item.position.left / 1.2 > 720
              ) {
                this.$refs.dragImgDom.style.left =
                  -item.position.left / 1.2 + 720 + "px";
              } else if (item.position.left / 1.2 < 720) {
                param[0].scrollLeft = 0;
                this.$refs.dragImgDom.style.left = "0px";
              } else if (
                this.currentTunnel.lane.width / 1.2 - item.position.left / 1.2 <
                720
              ) {
                this.$refs.dragImgDom.style.left =
                  1440 - this.currentTunnel.lane.width / 1.2 + "px";
              }
              if (
                item.position.left / 1.2 <=
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 <= 350
              ) {
                item.tooltipType = 1;
              } else if (
                item.position.left / 1.2 >
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 <= 350
              ) {
                item.tooltipType = 2;
              } else if (
                item.position.left / 1.2 <=
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 > 350
              ) {
                item.tooltipType = 3;
              } else {
                item.tooltipType = 4;
              }
              // this.$refs.dragImgDom.style.top = 290 - item.position.top + "px";
              item.click = true;
            } else {
              item.click = false;
            }
          } else {
            if (item.eqName.indexOf(this.screenEqName) > -1) {
              bigType = item.bigType;
              this.resetCanvasFlag = true;
              if (
                this.currentTunnel.lane.width / 1.2 - item.position.left / 1.2 >
                  720 &&
                item.position.left / 1.2 > 720
              ) {
                this.$refs.dragImgDom.style.left =
                  -item.position.left / 1.2 + 720 + "px";
              } else if (item.position.left / 1.2 < 720) {
                param[0].scrollLeft = 0;
                this.$refs.dragImgDom.style.left = "0px";
              } else if (
                this.currentTunnel.lane.width / 1.2 - item.position.left / 1.2 <
                720
              ) {
                this.$refs.dragImgDom.style.left =
                  1440 - this.currentTunnel.lane.width / 1.2 + "px";
              }
              if (
                item.position.left / 1.2 <=
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 <= 350
              ) {
                item.tooltipType = 1;
              } else if (
                item.position.left / 1.2 >
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 <= 350
              ) {
                item.tooltipType = 2;
              } else if (
                item.position.left / 1.2 <=
                  this.currentTunnel.lane.width / 1.2 - 200 &&
                item.position.top / 1.2 > 350
              ) {
                item.tooltipType = 3;
              } else {
                item.tooltipType = 4;
              }
              // this.$refs.dragImgDom.style.top = 290 - item.position.top + "px";
              item.click = true;
            } else {
              item.click = false;
            }
          }
        }
        if (bigType.includes("0")) {
          this.displayControl(0, "全部设备");
        } else {
          for (let itm of this.dictList) {
            if (bigType == itm.value) {
              this.displayControl(bigType, item.label);
            }
          }
        }
      } else {
        for (var item of this.selectedIconList) {
          item.click = false;
        }
      }
      this.$forceUpdate();
    },
    treeClear() {
      // this.resetCanvas();
      for (var item of this.selectedIconList) {
        if (item.eqName.indexOf(this.screenEqName) > -1) {
          item.click = false;
        }
      }
    },
    //点击树状图获取值
    handleNodeClick(data) {
      console.log(data, "data");
      // 如果存在children，则代表是父级
      if (data.children) {
        // 点击父级业务
      } else {
        this.treeShow = false;
        this.screenEqName = data.label;
        this.screenEqNameButton("treeNodeClick");
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 监听缩放比例变化
    zoomChange(val) {
      val < 70 ? (val = this.zoom) : "";
      val > 130 ? (val = this.zoom) : "";
      if (val) {
        this.$refs.divRoller.style.zoom = val + "%";
        // this.setMoveTop(val);
      }
    },
    // 批量操作 弹窗确定
    batchManageOK() {
      if (
        this.batchManageForm.brightness < 30 &&
        this.batchManageForm.state == 1 &&
        this.itemEqType == 9
      ) {
        this.$modal.msgWarning("基本照明亮度不得低于30");
        return;
      } else {
        const loading = this.$loading({
          lock: true,
          text: "Loading",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
        const param = {
          eqId: this.itemEqId.toString(),
          eqDirection: this.batchManageForm.eqDirection,
          state: this.batchManageForm.state,
          brightness: this.batchManageForm.brightness,
          eqType: this.itemEqType,
          frequency: this.batchManageForm.frequency,
        };
        batchControlDevice(param)
          .then((res) => {
            if (res.data == 0) {
              this.$modal.msgError("控制失败");
            } else if (res.data == 1) {
              this.$modal.msgSuccess("控制成功");
            }
            loading.close();
            this.batchManageDialog = false;
            this.closeBatchManageDialog();
          })
          .catch(() => {
            loading.close();
          });
      }
    },
    // 新版批量操作 点击变俩按钮
    batchManage() {
      this.batchManageType = 2;
      this.batchManageList = [];
      this.addBatchManage = true;
      this.screenEqName = "";
    },
    // 关闭批量操作弹窗 / 批量操作取消
    closeBatchManageDialog() {
      this.batchManageDialog = false;
      this.batchManageType = 1;
      this.batchManageForm.state = null;
      this.itemEqId = [];
      if (this.itemEqType == 7 || this.itemEqType == 9) {
        this.batchManageForm.brightness = 0;
        this.min = 0;
      }
      this.itemEqType = "";
      this.addBatchManage = false;
      for (var item of this.selectedIconList) {
        item.click = false;
      }
      this.$forceUpdate();
    },
    // 批量操作 执行
    implementBatchManage() {
      var that = this;
      this.title = "批量操作";
      this.eqTypeStateList2 = [];
      this.batchManageList = [];
      let eqType = "";
      for (var item of this.selectedIconList) {
        if (item.click) {
          this.batchManageList.push(item);
          eqType = item.eqType;
        }
      }
      if (this.batchManageList.length == 0) {
        this.$modal.msgWarning("请选择至少一个设备进行控制！");
        return;
      }
      this.batchManageForm.state = "";
      this.batchManageForm.brightness = 0;
      this.batchManageForm.frequency = 0;
      this.batchManageDialog = true;
      let list = [];
      const param = {
        stateTypeId: eqType,
        isControl: 1,
      };
      getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
        list = response.rows;
        for (let i = 0; i < list.length; i++) {
          let iconUrl = [];
          if (list[i].iFileList != null) {
            for (let j = 0; j < list[i].iFileList.length; j++) {
              let img = list[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          that.eqTypeStateList2.push({
            stateType: list[i].stateType,
            type: list[i].stateTypeId,
            state: list[i].deviceState,
            name: list[i].stateName,
            control: list[i].isControl,
            url: iconUrl,
          });
        }
      });
    },
    openTooltip(item, index) {
      document.addEventListener("mousemove", function (event) {
        let mouseX = event.clientX;
        let mouseY = event.clientY;
        if (mouseX < 1300 && mouseY < 400) {
          item.tooltipType = 1;
        } else if (mouseX > 1300 && mouseY < 400) {
          item.tooltipType = 2;
        } else if (mouseX < 1300 && mouseY > 400) {
          item.tooltipType = 3;
        } else {
          item.tooltipType = 4;
        }
      });
      this.$forceUpdate();
      this.showTooltipIndex = index;
      // this.sensorDisabled(item);
    },
    closeTooltip(item) {
      this.showTooltipIndex = 9999;
    },
    /* 打开图标说明对话框*/
    iconExplain() {
      this.explainVisible = true;
      this.title = "图标含义";
    },
    /* 关闭所有对话框*/
    cancel() {
      this.explainVisible = false;
      this.batchVisible = false;
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
    },
    isDrawer() {
      this.buttonsDeawer = !this.buttonsDeawer;
    },
    srollSwitchChange() {
      if (this.srollSwitch) {
        //调取滚动条
        this.mouseleaveImage();
      } else {
        this.mouseoversImage();
      }
    },
    mouseoversImage() {
      clearInterval(this.imageTimer);
      // this.srollSwitch = false
      this.imageTimer = null;
    },
    mouseleaveImage() {
      if (this.srollSwitch) {
        this.srollAuto();
      }
      // this.srollSwitch = true
    },
    // 滚动条动画
    srollAuto() {
      if (this.mouseoversImplement == false) {
        return;
      }
      var parent = document.getElementsByClassName("content");
      clearInterval(this.imageTimer);
      this.imageTimer = setInterval(() => {
        // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
        if (
          Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
          parent[0].scrollWidth
        ) {
          setTimeout(() => {
            clearInterval(this.imageTimer);
            this.imageTimer = setInterval(() => {
              parent[0].scrollLeft--;
              if (
                Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
                parent[0].clientWidth
              ) {
                this.srollAuto();
              }
            }, 20);
          }, 2000);
        } else {
          setTimeout(() => {
            parent[0].scrollLeft++;
          }, 2000);
        }
      }, 20);
    },
    zoomSwitchChange(val) {
      // console.log(val, "val");
      if (val == false) {
        this.handleTableWheelSwithch = false;
      } else {
        this.handleTableWheelSwithch = true;
      }
    },
    handleTableWheel(event) {
      let obj = this.$refs.divRoller;
      if (this.handleTableWheelSwithch == true) {
        this.tableZoom(obj, event);
      }
    },
    tableZoom(obj, event) {
      // 一开始默认是100%
      let zoom = parseInt(obj.style.zoom, 10) || 100;
      // 滚轮滚一下wheelDelta的值增加或减少120
      zoom += event.wheelDelta / (Math.abs(event.wheelDelta) / 10);
      zoom < 60 ? (zoom = 60) : "";
      zoom > 140 ? (zoom = 140) : "";
      this.zoom = zoom;
      this.setMoveTop(zoom);
      if (zoom > 0) {
        obj.style.zoom = zoom + "%";
      }
      // 按鼠标位置缩放 不好用 先隐藏 以后研究
      // this.fnWheel(obj, event, zoom);
      return false;
    },
    setMoveTop(zoom) {
      var ii = 0;
      switch (zoom) {
        case 110:
          ii = 1;
          break;
        case 120:
          ii = 3.5;
          break;
        case 130:
          ii = 7;
          break;
        case 140:
          ii = 11;
          break;
        default:
          ii = 0;
          break;
      }
      if (zoom == 90) {
        ii = 1; //0.11
      } else if (zoom == 80) {
        ii = 5; //0.25
      } else if (zoom == 70) {
        ii = 13; //0.43
      } else if (zoom == 60) {
        ii = 26; //0.66
      } else if (zoom == 50) {
        ii = 50; //1
      } else if (zoom == 40) {
        ii = 90; //1.5
      } else if (zoom == 30) {
        ii = 164; //2.34
      } else if (zoom == 20) {
        ii = 320; //4
      } else if (zoom == 10 || zoom == 0) {
        ii = 800; //9
      }
      this.moveTop = (100 - zoom + ii) / 100;
    },
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    getBoardStyle(id, type, eqType) {
      if (this.boardObj[id]) {
        if (JSON.parse(this.boardObj[id]).content) {
          let content = JSON.parse(this.boardObj[id]).content;
          let devicePixel = JSON.parse(this.boardObj[id]).devicePixel;
          if (type == "width") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[1] / 2 /1.2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[1] / 4 /1.2;
            }
          } else if (type == "height") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[0] / 2 /1.2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[0] / 4 /1.2;
            }
          }
          let array = [];
          let arr = "";
          let fontS = "";
          for (let i = 0; i < content.length; i++) {
            var itemId = "ITEM" + this.formatNum(i, 3);
            var con = content[i][itemId][0];
            con.CONTENT = con.CONTENT.replace("<br>", " ").replace(
              "&nbsp",
              " "
            );
            array.push(con);
            arr += con.CONTENT.replace("<br>", " ").replace("&nbsp", " ");
            arr += " ";
            fontS = Number(con.FONT_SIZE.substring(0, 2));
          }

          if (type == "content") {
            return arr;
          } else if (type == "fontSize") {
            if (eqType && eqType == 16) {
              return fontS / 2 /1.2;
            } else if (eqType && eqType == 36) {
              return fontS / 4 /1.2;
            }
          } else if (type == "array") {
            return array;
          }
        } else {
          let devicePixel = JSON.parse(this.boardObj[id]).devicePixel;
          if (type == "width") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[1] / 2 /1.2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[1] / 4 /1.2;
            }
          } else if (type == "height") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[0] / 2 /1.2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[0] / 4 /1.2;
            }
          } else if (type == "content") {
            return "山东高速欢迎您";
          } else if (type == "fontSize") {
            return 15 /1.2;
          } else if (type == "array") {
            let array = [{ CONTENT: "山东高速欢迎您", COLOR: "黄色" }];
            return array;
          }
        }
      } else {
        if (type == "width") {
          return 24 /1.2;
        } else if (type == "height") {
          return 72 /1.2;
        } else if (type == "content") {
          return "山东高速欢迎您";
        } else if (type == "fontSize") {
          return 15 /1.2;
        } else if (type == "array") {
          let array = [{ CONTENT: "山东高速欢迎您", COLOR: "黄色" }];
          return array;
        } else if (type == "content") {
          return "山东高速欢迎您";
        }
      }
    },
    // 转颜色
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },
    // 批量操作弹窗获取方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
  },
};
</script>
<style lang="scss">
.boardBox1 {
  position: absolute;
  overflow: hidden;
  writing-mode: tb-rl;
  white-space: nowrap;
  text-align: center;
  padding: 2px;
  border: solid 1.5px #f9b554;
  display: flex;
  align-items: center;
  border-radius: 2px;
  background: black;
  box-shadow: 0px 0px 2px #946f3b inset, 0px 0px 4px #946f3b inset;
}
.boardBox1 span {
  display: inline-block;
  /*inline样式不能使用动画*/
  // animation: boardBox1 10s linear infinite; /*滚动动画*/
}

.boardBox2 {
  position: absolute;
  overflow: hidden;
  writing-mode: tb-rl;
  white-space: nowrap;
  text-align: center;
  padding: 4px;
  border: solid 1.5px #f9b554;
  display: flex;
  align-items: center;
  border-radius: 2px;
  background: black;
  box-shadow: 0px 0px 2px #946f3b inset, 0px 0px 4px #946f3b inset;
}
.boardBox2 span {
  display: inline-block;
  /*inline样式不能使用动画*/
  //   animation: boardBox2 15s linear infinite; /*滚动动画*/
}
@keyframes boardBox1 {
  from {
    transform: translateY(72px); /*div多宽就写多宽*/
  }

  to {
    transform: translateY(-100%);
  }
}
@keyframes boardBox2 {
  from {
    transform: translateY(190px); /*div多宽就写多宽*/
  }

  to {
    transform: translateY(-100%);
  }
}
.vehicle-dialog {
  .el-dialog {
    pointer-events: auto !important;
    .el-radio {
      height: 40px !important;
      width: 240px;
    }
  }
}
.icon-dialog {
  .el-dialog {
    pointer-events: auto !important;
    .el-dialog__body {
      max-height: 70vh;
      overflow-y: auto;
      overflow-x: hidden;
    }
  }
}
</style>
<style scoped lang="scss">
.workbench-dialog {
  .header {
    width: 100%;
    height: 3vh;
    display: flex;
    justify-content: space-between;
    .headerLeft {
      width: 20%;
      display: flex;
      > div {
        padding: 0 1vw;
        border-radius: 2px;
        height: 3vh;
        line-height: 3vh;
      }
      > div:first-of-type {
        background: linear-gradient(90deg, #ffc606, #ff8200) !important;
      }
      > div:last-of-type {
        margin-left: 0.5vw;
        background: linear-gradient(177deg, #00aced, #0079db);
      }
    }
    .headerRight {
      height: 100%;
      display: flex;
      ::v-deep .zoomClass {
        .el-input--medium .el-input__inner {
          border: 1px solid #05afe3 !important;
        }
      }
      /* 显示编号*/
      .display-box {
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        align-items: center;
        padding-right: 0.8vw;
        height: 3vh;
        .switchStyle {
          font-size: 0.75vw;
          line-height: 2vh;
          height: 2vh;
        }
        .el-input-number {
          width: 5.4vw;
          line-height: 25px;
        }
      }
      .el-button + .el-button {
        margin-left: 0.8vw !important;
      }
      .buttons {
        padding: 0;
        width: 110px !important;
        height: 28px !important;
        overflow: hidden;
        span {
          img {
            width: 13px;
            margin-right: 5px;
            display: inline;
            transform: translateY(2px);
          }
          span {
            line-height: 16px;
          }
        }
      }
      .batchManageButton {
        width: 120px;
        display: flex;
        justify-content: space-around;
        padding: 0 5px;
        color: #e1feff;
        background: linear-gradient(2deg, #00070d, #005aa8) !important;
        border: 1px solid #00c8ff;
        font-size: 12px;
        height: 32px;
        align-items: center;
        margin-right: 10px;
        border-radius: 3px;
        color: white;
        text-align: center;
        > div {
          width: 50px;
          height: 20px;
          border-radius: 13px;
          line-height: 20px;
          cursor: pointer;
        }
        > div:nth-of-type(1) {
          background: #c8c8c8;
        }
        > div:nth-of-type(2) {
          background: #00b0ff;
        }
      }
      .buttonsDeawer {
        width: 1.6vw;
        height: 3vh;
        margin-left: 0.8vw;
        background: linear-gradient(
          90deg,
          rgba(0, 172, 237, 0.8),
          rgba(0, 121, 219, 0.8)
        );
        // transform: translateY(-1px);
        display: flex;
        justify-content: center;
        align-items: center;
        // position: absolute;
        // right: 0;
        // top: 10px;
        img {
          width: 1vw;
          height: 1.6vh;
        }
      }
    }
  }
}
.drawerBox {
  width: 100%;
  right: 214px;
  height: 100%;
  top: 110px;
  left: unset;
  ::v-deep .el-drawer.rtl {
    width: 22.6% !important;
    height: 12vh;
    color: #fff;
    .el-drawer__header {
      font-size: 14px;
      margin-bottom: 0px !important;
      height: 36px;
      line-height: 40px;
      color: #fff;
      margin-bottom: 0px;
    }
    .drawerItem {
      display: flex;
      padding-left: 10px;
      line-height: 34px;
      .el-switch {
        height: 34px;
        line-height: 34px;
      }
    }
  }
}
.vehicleLane {
  height: 484px;
  align-items: center;
  width: 1440px;
  display: flex;
  //   position: absolute;
  //   top: 0;
  //   left: 7.35%;
  /* 图片区域*/
  .content {
    clear: both;
    text-align: center;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    overflow-y: hidden;
    zoom: 100%;
    overflow-x: auto;
    display: inline-block;
    margin: 0 auto;
    position: relative;
    .workbench-content {
      height: 484px;
      position: absolute;
      .config-img-area {
        height: 100%;
        width: 100%;
        background-size: cover;
        // 工作台加滚动条 居中后左侧超出部分会溢出 加下面三行
        // position: absolute;
        inset: 0px;
        margin: 0px auto;
        .back-img {
          height: 100%;
          position: absolute;
          display: block;
        }
        .wrapper {
          height: 484px;
          width: 100%;
          border-radius: 10px;
          position: relative;
          z-index: 1;
          .icon-box {
            position: absolute;
            display: flex;
            flex-direction: column;
            // align-items: center;
            width: 0px !important;
            .screenEqNameClass {
              border: solid 2px #09c3fc;
              border-radius: 4px;
            }
          }
          // 鼠标经过盒子
          //   .mouseHover {
          //     &:hover {
          //       z-index: 10;

          //       input {
          //         cursor: pointer;
          //         background-color: #eee;
          //         color: #000;
          //       }
          //     }
          //   }
        }
      }
    }
  }
}
.tooltipBox {
  position: absolute;
  padding: 10px 20px;
  white-space: nowrap;
  border-radius: 4px;
  font-size: 12px;
  background: #cdedfa !important;
  border: solid 1px #1d58a9;
  color: #1d58a9 !important;
  padding: 0 !important;
  z-index: 96659;
  text-align: left;
  // transform:translateX(10px);
  span {
    padding: 10px !important;
    line-height: 24px !important;
  }
}
.textFalseBox {
  width: 120px;
  height: 28px;
  position: absolute;
  line-height: 28px;
  text-align: center;
  font-size: 10px;
  color: #da4a64;
  opacity: 1;
  // animation: fadenum 2s;
  z-index: 100;
  background: #fff;
  border-radius: 10px;
  border: solid 1px #da4a64;
}
.screenEqNameBox {
  width: 120px;
  // height: 40px;
  position: absolute;
  // top: -42px;
  // left: 10px;
  line-height: 1;
  text-align: center;
  padding: 10px;
  // padding-bottom: 22px;
  font-size: 10px;
  // background-image: url(../../../assets/cloudControl/screenEqName.png);
  // background-repeat: no-repeat;
  // background-size: 100% 100%;
  color: #07c3fc;
  z-index: 10;
  background: #fff;
  border-radius: 10px;
  max-height: 42px;
  border: solid 1px #07c3fc;
}
.treeBox {
  position: absolute;
  z-index: 960619;
  top: 14%;
  width: 13.5%;
  height: 40vh;
  overflow-x: hidden;
  overflow-y: auto;
}
.footer {
  width: 100%;
  height: 484px;
  position: relative;
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
  .eqTypeListClass {
    float: left;
    width: 7%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    z-index: 8;

    .leftButtonS {
      position: relative;
      left: 0px;
      font-size: 0.75vw;
      width: 100%;
      height: 5vh;
      line-height: 5vh;
      font-weight: 500;
      caret-color: rgba(0, 0, 0, 0);
      text-align: center;
      //   margin-left: 1vw;
      border-radius: 2px;
      cursor: pointer;
      img {
        width: 20px;
        height: 100%;
        object-fit: contain;
        margin-left: 8px;
        margin-right: 20px;
      }
    }
  }
}
.comClass {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
}
.closeRobot {
  position: absolute;
  top: 12px;
  left: 68%;
  z-index: 96659;
  cursor: pointer;
  width: 13px;
}
</style>