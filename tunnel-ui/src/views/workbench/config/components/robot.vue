<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog robot-dialog"
      :title="title"
      width="770px"
      append-to-body
      :visible="visible"
      :before-close="handleClosee"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="85px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item label="设备类型:">
              {{ stateForm.eqTypeName }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属方向:">
              {{ stateForm.eqDirection }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="设备厂商:">
              <el-tooltip
                effect="dark"
                :content="stateForm.supplierName"
                placement="top-start"
              >
                <div class="overflowText">{{ stateForm.supplierName }}</div>
              </el-tooltip>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="8">
            <el-form-item label="充电状态:">
              {{ stateForm.charge }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="运行时长:">
              {{ stateForm.currentDuration }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="当前电压:">
              {{ stateForm.voltage }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总里程:">
              {{ stateForm.currentMileage }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="当前电流:">
              {{ stateForm.current }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="当前电量:">
              {{ stateForm.electricity }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="电池温度:">
              {{ stateForm.batteryTemp }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="当前温度:"> （无数据） </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="O2浓度:">
              {{ stateForm.oxygenDensity }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="当前湿度:"> （无数据） </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="CO浓度:">
              {{ stateForm.carbonMonoxideDensity }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="投光灯亮度:"> （无数据） </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="设备状态:"
              :style="{
                color:
                  stateForm.eqStatus == '1'
                    ? 'yellowgreen'
                    : stateForm.eqStatus == '2'
                    ? 'white'
                    : 'red',
              }"
            >
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-radio-group
          v-model="tab"
          style="margin-bottom: 10px"
          class="tabRobot"
        >
          <el-radio-button label="trafficFlow">操作项</el-radio-button>
          <!-- <el-radio-button label="event">功能项</el-radio-button> -->
          <el-radio-button label="config">配置项</el-radio-button>
        </el-radio-group>
        <div
          v-show="tab == 'trafficFlow'"
          style="margin-bottom: 10px"
          class="trafficFlowTab"
        >
          <div class="trafficFlowLeft">
            <div>
              <videoPlayer
                v-if="this.stateForm.hd"
                :rtsp="this.stateForm.hd"
                :open="true"
              ></videoPlayer>
            </div>
            <div style="margin-top: 20px">
              <el-radio-group v-model="commend.type">
                <el-radio style="padding: 10px 20px" :label="1">云台</el-radio>
                <el-radio style="padding: 10px 20px" :label="2">相机</el-radio>
                <el-radio style="padding: 10px 20px" :label="3">移动</el-radio>
              </el-radio-group>
            </div>
            <div style="margin-top: 20px">
              <div
                v-if="commend.type == 1 || commend.type == 2"
                class="kongzhiButtons"
              >
                <img
                  src="../../../../assets/cloudControl/robotAmplify.png"
                  @click="execCommend(9)"
                />
                <div class="directionButtons">
                  <el-button
                    class="buttonTop button"
                    @click="execCommend(1)"
                    disabled
                    ><img
                      src="../../../../assets/cloudControl/robotDirection.png"
                  /></el-button>
                  <el-button class="buttonRight button" @click="execCommend(3)"
                    ><img
                      src="../../../../assets/cloudControl/robotDirection.png"
                  /></el-button>
                  <el-button class="buttonBottom button" @click="execCommend(5)"
                    ><img
                      src="../../../../assets/cloudControl/robotDirection.png"
                  /></el-button>
                  <el-button class="buttonLeft button" @click="execCommend(7)"
                    ><img
                      src="../../../../assets/cloudControl/robotDirection.png"
                  /></el-button>
                </div>
                <img
                  src="../../../../assets/cloudControl/robotReduce.png"
                  @click="execCommend(10)"
                />
              </div>
              <div v-if="commend.type == 3" class="kongzhiButtons3">
                <el-button
                  class="buttonForward"
                  @click="commend.controlMoveIndex = 1"
                  >向前</el-button
                >
                <el-button
                  class="buttonForward"
                  @click="commend.controlMoveIndex = 2"
                  >向后</el-button
                >
              </div>
              <div
                style="clear: both; text-align: center; margin-top: 10px"
                class="DistributeButton"
              >
                <el-button @click="stopCommend()" round>停止</el-button>
                <el-button @click="execCommend()" round>执行</el-button>
              </div>
            </div>
          </div>
          <div class="trafficFlowRight">
            <el-form ref="form" label-width="80px">
              <el-row style="margin-bottom:20px">
                <el-col :span="20">
                  <el-slider
                    v-model="commend.position"
                    :max="100"
                    class="sliderClass"
                  ></el-slider>
                </el-col>
                <el-col :span="4">
                  <span style="padding-left: 10px; line-height: 30px">{{ commend.position }}</span>
                </el-col>
              </el-row>
              <div
                style="
                  height: 75px;
                  display: flex;
                  justify-content: space-around;
                "
              >
                <el-radio-group
                  v-model="commend.chargeIndex"
                  style="display: flex; flex-direction: column"
                >
                  <el-radio style="padding: 10px 20px" :label="1"
                    >充电桩A</el-radio
                  >
                  <el-radio
                    style="
                      padding: 10px 20px;
                      margin-left: 0px;
                      margin-top: 5px;
                    "
                    :label="2"
                    >充电桩B</el-radio
                  >
                </el-radio-group>
                <el-radio-group
                  v-model="commend.gotoIndex"
                  style="display: flex; flex-direction: column"
                >
                  <el-radio style="padding: 10px 20px" :label="1"
                    >预置点A</el-radio
                  >
                  <el-radio
                    style="
                      padding: 10px 20px;
                      margin-left: 0px;
                      margin-top: 5px;
                    "
                    :label="2"
                    >预置点B</el-radio
                  >
                </el-radio-group>
              </div>
              <div class="rightButton">
                <div @click="setCharge()" class="button">
                  <img
                    src="../../../../assets/cloudControl/robotChongdian.png"
                  />
                  <span>一键充电</span>
                </div>
                <div @click="gotoPreset()" class="button">
                  <img src="../../../../assets/cloudControl/robotDaohang.png" />
                  <span>一键导航</span>
                </div>
              </div>
              <div style="margin-top: 10px">
                <div style="display: inline">
                  <el-input
                    v-model="commend.videoText"
                    placeholder="输入需要播放语音文字"
                    class="broadcastButton"
                  >
                    <img
                      @click="broadcast()"
                      slot="append"
                      src="../../../../assets/cloudControl/robotBroadcast.png"
                    />
                  </el-input>
                </div>
              </div>
              <div style="margin-top: 10px" class="switchBox">
                <el-row>
                  <el-col :span="13">
                    <div style="margin-top: 10px">
                      <el-form-item label="自动巡航">
                        <el-switch
                          v-model="commend.carmode"
                          @change="changeControl()"
                          active-text="开"
                          inactive-text="关"
                          :active-value="true"
                          :inactive-value="false"
                          class="rightSwitch"
                        >
                        </el-switch>
                      </el-form-item>
                    </div>
                  </el-col>
                  <el-col :span="11">
                    <div style="margin-top: 10px">
                      <el-form-item label="语音对讲">
                        <el-switch
                          v-model="commend.setSpeak"
                          @change="setSpeak(commend.setSpeak)"
                          active-text="开"
                          inactive-text="关"
                          :active-value="1"
                          :inactive-value="0"
                          class="rightSwitch"
                        >
                        </el-switch>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="13">
                    <div style="margin-top: 10px">
                      <el-form-item label="雨刷">
                        <el-switch
                          v-model="commend.windscreen"
                          @change="controlWindscreen()"
                          active-text="开"
                          inactive-text="关"
                          :active-value="1"
                          :inactive-value="0"
                          class="rightSwitch"
                        >
                        </el-switch>
                      </el-form-item>
                    </div>
                  </el-col>
                  <el-col :span="11">
                    <div style="margin-top: 10px">
                      <el-form-item label="投光灯">
                        <el-switch
                          v-model="commend.setLEDLight"
                          @change="setLEDLight(commend.setLEDLight)"
                          active-text="开"
                          inactive-text="关"
                          :active-value="1"
                          :inactive-value="0"
                          class="rightSwitch"
                        >
                        </el-switch>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <!-- <el-row>
                  <el-col :span="13">
                    <div style="margin-top: 10px">
                      <el-form-item label="报警灯">
                        <el-switch
                          style="display: block"
                          v-model="commend.setAlarmLight"
                          active-color="#01AAFD"
                          inactive-color="#ddd"
                          @change="setAlarmLight(commend.setAlarmLight)"
                        >
                        </el-switch>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row> -->
              </div>
            </el-form>
            <div class="initializationRow">
              <el-button @click="initializeRobot()">位置初始化</el-button>
            </div>
          </div>
        </div>

        <div v-show="tab == 'config'" style="margin-bottom: 10px">
          <div style="margin-top: 10px">
            <el-form
              ref="alarmConfigForm"
              :rules="rules"
              :model="alarmConfigForm"
              label-width="140px"
              :inline="true"
              label-position="right"
            >
              <el-row>
<!--                <el-col :span="12">
                  <el-form-item label="主键 id:" prop="id">
                    <el-input
                      v-model="alarmConfigForm.id"
                      placeholder="请输入主键 id"
                      style="width: 100%"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="机器人 id:" prop="deviceId">
                    <el-input
                      v-model="alarmConfigForm.deviceId"
                      placeholder="请输入机器人 id"
                    ></el-input>
                  </el-form-item>
                </el-col>-->
                <el-col :span="12">
                  <el-form-item label="电量阈值配置名称:" prop="groupName">
                    <el-input
                      v-model="alarmConfigForm.groupName"
                      placeholder="请输入电量阈值配置名称"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="电池低电量:" prop="powerLowerLimit">
                    <el-input
                      v-model="alarmConfigForm.powerLowerLimit"
                      placeholder="请输入电池低电量"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="电池高电量:" prop="powerTopLimit">
                    <el-input
                      v-model="alarmConfigForm.powerTopLimit"
                      placeholder="请输入电池高电量"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="生效开始时间:" prop="beginTime">
                    <el-input
                      v-model="alarmConfigForm.beginTime"
                      placeholder="请输入生效开始时间"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="生效结束时间:" prop="endTime">
                    <el-input
                      v-model="alarmConfigForm.endTime"
                      placeholder="请输入生效结束时间"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="电池开启状态:" prop="powerEnable">
                    <el-input
                      v-model="alarmConfigForm.powerEnable"
                      placeholder="请输入电池开启状态"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-button
                    class="yellowButton"
                    @click="setAlarmThresholdConfig()"
                    >保存阈值</el-button
                  >
                </el-col>
              </el-row>
            </el-form>
          </div>
          <div style="margin-top: 10px">
            <el-form-item label=" ">
              <!--              <el-switch
                              v-model="value"
                              active-color="#13ce66"
                              inactive-color="#ff4949">
                            </el-switch>-->
            </el-form-item>
          </div>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import * as echarts from "echarts";

import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
import min from "@/components/Crontab/min.vue";
import {
  broadcast,
  changeControl,
  controlWindscreen,
  findAlarmThresholdConfig,
  gotoPreset,
  initializeRobot,
  move,
  ptz,
  setAlarmLight,
  setAlarmThresholdConfig,
  setCharge,
  setLEDLight,
} from "@/api/workbench/config";
import { updateSituationUpgrade } from "@/api/event/event";
export default {
  computed: {
    min() {
      return min;
    },
  },
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  components: {
    videoPlayer,
  },
  data() {
    return {
      value: true,
      stateForm: {},
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      tab: "trafficFlow",

      commend: {
        type: 1, // 1： 云台   2：相机   3：移动
        chargeIndex: 1, // 充电位置
        gotoIndex: 1, // 预置位置
        setLEDLight: 0, // 闪光灯
        setAlarmLight: 0, // 报警灯
        setSpeak: 0, // 语音
        carmode: true, // 实际切换模式 0 自动巡航 1 手动，  使用switch控件  1=自动巡航  0= 手动
        windscreen: 0, //未定义
        controlPTZIndex: 0, // 云台索引
        controlPhoneIndex: 0, //相机索引
        controlMoveIndex: 0, // 移动方向
        videoText: "", // 语音播放内容
        position: 40, //机器人移动位置进度条
      },
      alarmConfigForm: {
        deviceId: "",
        id: "",
        groupName: "",
        beginTime: "",
        endTime: "",
        powerEnable: "",
        powerLowerLimit: "",
        powerTopLimit: "",
      },
      rules: {
        groupName: [
          {
            required: true,
            message: "请输入电量阈值配置名称",
            trigger: "blur",
          },
        ],
        powerEnable: [
          { required: true, message: "请输入电池开启状态", trigger: "blur" },
        ],
        powerLowerLimit: [
          { required: true, message: "请输入生效开始时间", trigger: "blur" },
        ],
        beginTime: [
          { required: true, message: "请输入生效开始时间", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请输入生效结束时间", trigger: "blur" },
        ],
        powerTopLimit: [
          { required: true, message: "请输入生效结束时间", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getMessage();
    this.findAlarmThresholdConfig();

  },
  mounted() {
    var htmlStr = "<div class='robotButton'/>"
    this.$nextTick(()=>{
      document.getElementsByClassName('el-slider__button')[0].insertAdjacentHTML('afterEnd',htmlStr);
    })
  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      // if (this.eqInfo.equipmentId) {
      var obj = {};
      var state = "";
      // 查询单选框弹窗信息 -----------------------
      console.log("获取设备Id:");
      console.log(this.eqInfo);
      console.log(this.eqInfo.equipmentId);
      await getDeviceById(this.eqInfo.equipmentId).then((res) => {
        console.log(res, "查询单选框弹窗信息");
        obj = res.data;

        this.title = obj.eqName;
        this.stateForm = {
          brandName: that.getBrandName(obj.brandId), //厂商
          eqDirection: that.getDirection(obj.eqDirection),
          isOnline: obj.isOnline,
          pile: obj.pile, //桩号
          eqTypeName: obj.typeName, //设备类型名称
          tunnelName: obj.tunnelName, //隧道名称
          deptName: obj.deptName, //所属机构
          eqType: obj.eqType, //设备类型号
          state: obj.state,
          supplierName: obj.supplierName, //设备厂商
          currentDuration: obj.currentDuration, //运行时长
          currentMileage: obj.currentMileage, //里程数
          electricity: obj.electricity, //当前电量
          charge: obj.charge, //是否在充电
          voltage: obj.voltage, //当前电压
          current: obj.current, //当前电流
          batteryTemp: obj.batteryTemp, //电池温度
          position: obj.position, //轨道位置
          oxygenDensity: obj.oxygenDensity, //氧气浓度
          carbonMonoxideDensity: obj.carbonMonoxideDensity, //一氧化碳浓度
          workModelText: obj.workModelText, //运行状态
          hd: obj.hd, //高清
          infrared: obj.infrared, //红外
          eqStatus: obj.eqStatus,
        };
        console.log(this.stateForm, "stateForm");
      });
      // } else {
      //   this.$modal.msgWarning("没有设备Id");
      // }
    },
    getDirection(num) {
      console.log(this.directionList);

      for (var item of this.directionList) {
        if (item.dictValue == num) {
          console.log(item.dictLabel);
          return item.dictLabel;
        }
      }
    },
    getBrandName(num) {
      // 根据字典表查设备厂商--------------------------
      for (var item of this.brandList) {
        if (Number(item.dictValue) == num) {
          return item.dictLabel;
        }
      }
    },
    geteqType(num) {
      for (var item of this.eqTypeDialogList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },

    // 投光灯
    setLEDLight() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        control: this.commend.setLEDLight ? 1 : 0, // 0 关闭  1打开
      };
      setLEDLight(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },
    //报警灯
    setAlarmLight() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        control: this.commend.setLEDLight ? 1 : 0, // 0 关闭  1打开
      };
      setAlarmLight(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },
    //一键充电
    setCharge() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
      };
      setCharge(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    //语音对讲
    setSpeak() {
      this.$modal.msgError("第三方暂未不支持");
    },
    //设置机器人到达预置点
    gotoPreset() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        presetId: this.commend.gotoIndex, // 预置位置
      };
      gotoPreset(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    // 停止操作
    stopCommend() {
      // 操作类型  1 云盘  2 相机   3 移动
      switch (this.commend.type) {
        case 1:
          this.commend.controlPTZIndex = 0;
          break;
        case 2:
          this.commend.controlPhoneIndex = 0;
          break;
        case 3:
          this.commend.controlMoveIndex = 0;
          break;
      }
      this.execCommend();
    },

    // 执行操作
    execCommend(index) {
      // 操作类型  1 云盘  2 相机   3 移动
      switch (this.commend.type) {
        case 1:
          this.ptz(index);
          break;
        case 2:
          this.$modal.msgError("第三方暂未不支持");
          break;
        case 3:
          this.move();
          break;
      }
    },

    //移动
    move() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        control: this.commend.controlMoveIndex, // 移动方向
      };
      move(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    //云台
    ptz() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        control: this.commend.controlPTZIndex, // 移动方向
      };
      ptz(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    //语音播放
    broadcast() {
      if (this.commend.videoText.length == 0) {
        this.$modal.msgError("请先录入需要播放语音文字");
        return;
      }
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        text: this.commend.videoText, // 移动方向
      };
      broadcast(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },
    //自动巡航
    changeControl() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
        carmode: this.commend.carmode ? 0 : 1, // 切换模式 0 自动巡航 1 手动
      };
      changeControl(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    //机器人雨刷
    controlWindscreen() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
      };
      controlWindscreen(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    //获取电量阈值配置
    findAlarmThresholdConfig() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
      };
      findAlarmThresholdConfig(param).then((res) => {
        if (res.code == 200) {
          this.alarmConfigForm = res.data;
          this.alarmConfigForm.powerEnable = res.data.sendStatus;
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },
    //编辑电量阈值配置
    setAlarmThresholdConfig() {
      this.$refs["alarmConfigForm"].validate((valid) => {
        if (valid) {
          setAlarmThresholdConfig(this.alarmConfigForm).then((res) => {
            if (res.code == 200) {
              this.$modal.msgSuccess("执行成功");
            } else {
              this.$modal.msgError(res.msg);
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    //初始化机器人
    initializeRobot() {
      let param = {
        deviceId: this.eqInfo.equipmentId, // 设备ID
      };
      initializeRobot(param).then((res) => {
        if (res.code == 200) {
          this.$modal.msgSuccess("执行成功");
        } else {
          this.$modal.msgError(res.msg);
        }
      });
    },

    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
    handleOK() {
      this.$emit("dialogClose");
    },
  },
};
</script>
<style lang="scss" scoped>
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}

.tabRobot {
  margin-top: 10px;
}

::v-deep .el-radio-button--medium .el-radio-button__inner {
  padding: 5px 10px !important;
  background: transparent;
  border: 1px solid transparent;
  // color: #fff;
}

::v-deep .el-radio-group > .is-active {
  background: #00aaf2 !important;
  border-radius: 20px !important;
}

::v-deep .el-radio-button {
  margin: 0 5px;
}

#traffic {
  width: 90%;
  height: 150px;
  background: #fff;
  margin-left: 5%;

  div {
    width: 100%;
    height: 150px !important;
  }
}
.trafficFlowTab {
  display: flex;
  justify-content: space-around;
  .trafficFlowLeft {
    .kongzhiButtons {
      width: 100%;
      height: 95px;
      display: flex;
      justify-content: center;
      > img {
        width: 20px;
        height: 20px;
        margin-top: 37px;
        cursor: pointer;
      }
      .directionButtons {
        position: relative;
        width: 120px;
        height: 95px;
        .button {
          width: 35px;
          height: 35px;
          background-color: #0000ff;
          border-radius: 0 50px 0 0 !important;
          position: absolute;
          border: none;
        }
        // .button:hover {
        //   background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
        // }
        .el-button--medium {
          padding: 0;
        }
        .buttonTop {
          transform: rotate(-45deg);
          top: 0;
          left: calc(50% - 18px);
          img {
            transform: rotate(45deg);
            width: 8px;
          }
        }
        .buttonBottom {
          transform: rotate(135deg);
          top: 54px;
          left: calc(50% - 28px);
          img {
            transform: rotate(45deg) translateX(-3px);
            width: 8px;
          }
        }
        .buttonLeft {
          transform: rotate(-135deg);
          top: 27px;
          left: 5px;
          img {
            transform: rotate(45deg);
            width: 8px;
          }
        }
        .buttonRight {
          transform: rotate(45deg);
          right: 16px;
          top: 27px;
          img {
            transform: rotate(45deg);
            width: 8px;
          }
        }
      }
    }
    .kongzhiButtons3 {
      width: 100%;
      justify-content: center;
      display: flex;
      .buttonForward {
        padding: 5px 20px;
        border: none;
        background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
      }
      // .buttonForward:hover {
      //   background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
      // }
    }
    .DistributeButton {
      > .el-button:first-of-type {
        padding: 5px 20px;
        border: none;
        background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
      }
      > .el-button:last-of-type {
        padding: 5px 20px;
        border: none;
        background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
      }
      // > .el-button:hover {
      //   background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
      // }
    }
  }
  .trafficFlowRight {
    .rightButton {
      margin-top: 20px;
      height: 50px;
      display: flex;
      justify-content: space-around;
      .button {
        padding: 0px 20px;
        border: none;
        background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        height: 30px;
        border-radius: 4px !important;
        display: flex;
        align-items: center;
        cursor: pointer;
        img {
          width: 18px;
          height: 18px;
          margin-right: 4px;
        }
      }
    }
    .broadcastButton {
      width: 300px;
      img {
        width: 20px;
        cursor: pointer;
      }
    }
    .initializationRow {
      margin-top: 10px;
      display: flex;
      justify-content: center;
      .el-button {
        padding: 5px 20px;
        border: none;
        background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
      }
    }
  }
}
.overflowText {
  width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.yellowButton {
  padding: 5px 20px;
  border: none;
  // background: #f6b542 linear-gradient(180deg, #e5a535 0%, #ffbd49 100%) !important;
  float: right;
}

</style>
<style lang="scss">
.rightSwitch {
  display: block;
  position: relative;
  transform: translateY(4px);
  .el-switch {
    width: 50px !important;
  }
  .el-switch__label {
    // color:#fff;
    top: 3px;
  }
  .el-switch__label--left {
    position: absolute;
    left: 12px;
    // color: #fff;
  }
  .el-switch__core {
    width: 70px !important;
    position: absolute;
    height: 25px;
    border-radius: 13px;
    // border:solid 1px #01AAFD !important;
    // background: transparent !important;
  }
  .el-switch__core:after {
    width: 34px;
    height: 21px;
    border-radius: 11px;
    // background: #01AAFD;
  }
  .el-switch__label--right {
    position: absolute;
    left: 34px;
    // color: #fff;
  }
  .el-switch__label--right.is-active {
    z-index: 1111;
    // color: #06213E !important;
  }
  .el-switch__label--left.is-active {
    z-index: 1111;
    // color: #06213E !important;
  }
}
.rightSwitch.is-checked .el-switch__core::after {
  left: 73% !important;
}

.trafficFlowRight .sliderClass {
    width: 100% !important;
  .el-slider__runway {
    width: 100%;
    // background-color: #006784;
    margin: 12px 0;
  }
  .el-slider__bar {
    background: linear-gradient(90deg, #00aded 0%, #007cdd 100%);
  }

  .el-slider__button-wrapper{
    top:-6px !important;
    .robotButton{
      width:24px;
      height:24px;
      margin: 2px auto;
      background-image: url(../../../../assets/cloudControl/robot.png);
      background-position: center;
      background-repeat: no-repeat;
      background-size: 100% ;
    }
    .el-slider__button {
      width: 10px;
      height: 10px;
      border: solid 1px #fff;
      background-color: #ff9300;
      // background-image: url(../../../../assets/cloudControl/robot.png);

    }
  }
}
</style>

