<template>
  <div class="app-container">
    <div style="height: 100%; display: flex; justify-content: space-between">
      <div style="height: 100%; width: 66%">
        <el-card class="bottomEventForm">
          <div class="tunnelBox">
            <div class="tunnelMap">
              <el-image
                class="back-img"
                :src="laneUrlList[0].url"
                :style="{ width: laneUrlList[0].width + 'px' }"
              ></el-image>
              <div class="maskClass">
                <div
                  v-for="(item, index) in planListEnd"
                  :key="index"
                  class="mousemoveBox"
                  @contextmenu.prevent="rightClick(index)"
                  :style="{
                    width: item.width / 1.34 + 'px',
                    height: item.height / 1.34 + 'px',
                    left: item.left / 1.34 + 'px',
                    top: item.top / 1.34 + 'px',
                  }"
                  @mouseleave="mouseleave(index)"
                >
                  <!--  @mouseleave="mouseleave(index)" -->
                  <div class="partitionBox"></div>
                  <div
                    class="rightClickClass"
                    v-show="fqIndex == index"
                    :style="item.style"
                  >
                    <div class="row1">{{ item.sName }}</div>
                    <div class="recoveryBox">
                      <div
                        class="endButton"
                        v-for="itm in item.reservePlans"
                        :key="itm.sId"
                      >
                        <div class="ButtonBox">
                          <div class="recovery">{{ itm.planName }}</div>
                          <div class="button">
                            <div @click="getPreview(itm)">预览</div>
                            <div @click="eventDo(itm)">执行</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- <div  class="maskClass1" >
                <div class="rightClickClass" v-for="(item,index) in planList1"
                      :style="{width:100/(planList1.length/2)+'%'}"
                      @contextmenu.prevent="rightClick(index)">
                      <div class="rightClickButton">
                        <div class="row1" >{{item.text}}</div>
                        <div class="row2" >
                          <div>执行</div>
                          <div>预览</div>
                        </div>
                      </div>
                </div>
              </div> -->
              <div class="wrapper" id="eq-wrapper">
                <div
                  class="icon-box active"
                  v-for="(item, index) in selectedIconList"
                  :key="index"
                  :style="{
                    left: item.position.left / 1.34 + 'px',
                    top: item.position.top / 1.3 + 'px',
                    'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                  }"
                  :class="
                    item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                      ? 'light-' + item.position.left
                      : ''
                  "
                >
                  <div
                    v-show="
                      (item.eqType != 7 &&
                        item.eqType != 16 &&
                        item.eqType != 15 &&
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
                      v-for="(url, indexs) in item.url"
                      style="position: relative"
                      :style="
                        item.eqType || item.eqType == 0
                          ? 'cursor: pointer;'
                          : ''
                      "
                      :width="item.iconWidth / 1.26"
                      :height="item.iconHeight / 1.26"
                      :key="item.deptId + indexs"
                      :src="url"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-show="changeVideo == 0" class="formBox">
            <div class="formTitle">
              <div style="float: left">事件详情</div>
              <div class="formButton formButton1" @click="eventFormClose">
                <el-image :src="require('@/assets/icons/relieve.png')" />
                <div>结束事件</div>
              </div>
              <div class="formButton formButton2" @click="submitEventForm">
                <el-image :src="require('@/assets/icons/update.png')" />
                <div>更新信息</div>
              </div>
            </div>
            <el-form
              ref="form"
              :model="eventForm"
              label-width="80px"
              style="
                height: calc(100% - 35px);
                overflow-y: auto;
                padding-top: 10px;
              "
            >
              <el-row>
                <el-col :span="8">
                  <el-form-item label="事件类型" prop="eventType">
                    <el-input
                      v-model="eventForm.eventType"
                      placeholder="请输入事件类型"
                      disabled
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <!-- <el-form-item label="事故类型" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" placeholder="请选择事故类型" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item> -->
                </el-col>
                <el-col :span="8">
                  <el-form-item label="所属隧道" prop="tunnelName">
                    <el-select
                      v-model="eventForm.tunnelName"
                      placeholder="请选择所属路段"
                      clearable
                      size="small"
                      disabled
                    >
                      <el-option
                        v-for="item in eqTunnelData"
                        :key="item.tunnelId"
                        :label="item.tunnelName"
                        :value="item.tunnelId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="桩号位置" prop="stakeNum">
                    <!-- <span>K </span> -->
                    <el-input
                      v-model="eventForm.stakeNum"
                      placeholder="请输入桩号位置"
                      style="width: 250px"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker
                      clearable
                      size="small"
                      v-model="eventForm.startTime"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="选择开始时间"
                    >
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker
                      clearable
                      size="small"
                      v-model="eventForm.endTime"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="选择结束时间"
                    >
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <!--  <el-col :span="8">
                  <el-form-item label="影响程度" prop="tunnelId">
                    <el-select v-model="eventForm.tunnelId" placeholder="请选择影响程度" clearable size="small" >
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="压车长度:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  style="width: 100px" />
                    <span> KM</span>
                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item label="事件等级 " prop="eventGrade">
                    <el-radio-group v-model="eventForm.eventGrade">
                      <el-radio
                        v-for="dict in eventGradeOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        >{{ dict.dictLabel }}</el-radio
                      >
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="事件标题" prop="eventTitle">
                    <el-input v-model="eventForm.eventTitle" />
                  </el-form-item>
                </el-col>
                <el-col :span="16">
                  <el-form-item label="事件描述" prop="eventDescription">
                    <el-input
                      v-model="eventForm.eventDescription"
                      style="width: 660px"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="人员伤亡">
                <el-row>
                  <el-col :span="5">
                    <div class="formLable">轻伤</div>
                    <el-input
                      v-model="eventForm.slightInjured"
                      style="width: 100px"
                    />
                    <span> 人</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">重伤</div>
                    <el-input
                      v-model="eventForm.eventInjured"
                      style="width: 100px"
                    />
                    <span> 人</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">死亡</div>
                    <el-input
                      v-model="eventForm.eventDeath"
                      style="width: 100px"
                    />
                    <span> 人</span>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="车辆">
                <el-row>
                  <el-col :span="5">
                    <div class="formLable">小型车</div>
                    <el-input
                      v-model="eventForm.smallCarNum"
                      style="width: 100px"
                    />
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">货车</div>
                    <el-input
                      v-model="eventForm.truckNum"
                      style="width: 100px"
                    />
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">客车</div>
                    <el-input
                      v-model="eventForm.passengerCarNum"
                      style="width: 100px"
                    />
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">罐车</div>
                    <el-input
                      v-model="eventForm.tankerNum"
                      style="width: 100px"
                    />
                    <span> 辆</span>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="车主电话" prop="carOwnerPhone">
                    <el-input v-model="eventForm.carOwnerPhone" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="清障电话" prop="wreckerPhone">
                    <el-input v-model="eventForm.wreckerPhone" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="交警电话" prop="policePhone">
                    <el-input v-model="eventForm.policePhone" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="备注" prop="remark">
                <el-input
                  type="textarea"
                  v-model="eventForm.remark"
                  style="width: 1056px"
                />
              </el-form-item>
              <!-- <el-row>
                <el-col :span="8">
                    <el-form-item label="特情处置" prop="tunnelId">
                      <el-select v-model="eventForm.tunnelId" placeholder="请选择特情处置" clearable size="small" >
                        <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                          :value="item.tunnelId" />
                      </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="洒落物:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="超时原因:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small" >
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row> -->
              <!-- <el-row>
                <el-col :span="8">
                  <el-form-item label="倒货:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="天气:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="拥堵路段:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row> -->
              <!-- <el-row>
                <el-col :span="8">
                  <el-form-item label="施工路段:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="处在弯道:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="在分叉口:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row> -->
            </el-form>
          </div>
          <div v-show="changeVideo == 1" class="formBox">
            <div class="formTitle">
              <div style="float: left">摄像视频</div>
              <div class="formButton formButton2" @click="returnDetails">
                <el-image :src="require('@/assets/icons/change.png')" />
                <div>切换事件详情</div>
              </div>
            </div>
            <div class="cameraBox">
              <video
                controls
                muted
                autoplay
                loop
                :src="item.src"
                v-for="(item, index) in videoList"
                :key="index"
              ></video>
              <el-image
                :src="require('@/assets/icons/outline.png')"
                v-show="videoList.length < 2"
              />
            </div>
          </div>
        </el-card>
      </div>

      <div style="height: 100%; margin-left: 10px; width: 34%">
        <el-row style="height: 100%">
          <el-col :span="11" style="height: 100%">
            <!-- 视频 -->
            <div class="rightBox videoBox">
              <video
                :src="videoUrl"
                controls
                muted
                autoplay
                loop
                class="video1"
              ></video>
              <div class="video">
                <img
                  v-for="(item, index) in urls"
                  :key="index"
                  :src="item.imgUrl"
                  style="width: 32%; border-radius: 6px"
                />
              </div>
            </div>
            <!-- 预案 -->
            <div class="rightBox planBox">
              <div class="title">周边物资</div>
              <el-table :data="materialList" max-height="230">
                <el-table-column
                  label="物资名称"
                  align="center"
                  prop="materialName"
                />
                <el-table-column label="桩号" align="center" prop="station" />
                <el-table-column
                  label="数量"
                  align="center"
                  prop="number"
                  width="60"
                />
              </el-table>
            </div>
            <!-- 电话 -->
            <div class="rightBox phoneBox">
              <div class="title">调度电话</div>
              <el-table :data="personnelList" max-height="210">
                <el-table-column
                  label="姓名"
                  align="center"
                  prop="userName"
                  width="60"
                />
                <el-table-column label="联系方式" align="center" prop="phone" />
                <el-table-column
                  label="操作"
                  align="center"
                  class-name="small-padding"
                  width="60"
                >
                  <template slot-scope="scope">
                    <div class="phoneButton">
                      <el-image :src="require('@/assets/icons/phone.png')" />
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
          <el-col :span="13" style="height: 100%">
            <!-- 事件流程 -->
            <div class="rightBox processBox">
              <el-steps :active="1">
                <el-step title="事件预警" icon="el-icon-edit"></el-step>
                <el-step title="事故确认" icon="el-icon-upload"></el-step>
                <el-step title="处置中" icon="el-icon-picture"></el-step>
                <el-step title="处置结束" icon="el-icon-s-tools"></el-step>
              </el-steps>
              <el-timeline style="height: calc(100% - 150px); overflow: auto">
                <el-timeline-item
                  placement="top"
                  v-for="(item, index) in eventList"
                  :key="index"
                  color="#00A0FF"
                >
                  <div>{{ item.flowTime }}</div>
                  <el-card>
                    <p>{{ item.flowDescription }}</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
              <div class="endButton">
                <div
                  class="ButtonBox"
                  v-for="(item, index) in hfData"
                  :key="index"
                >
                  <div class="recovery">{{ item.planName }}</div>
                  <div class="button">
                    <div @click="getPreview(itm)">预览</div>
                    <div @click="eventDo(item)">执行</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="rightBox implement">
              <div class="title">已执行</div>
              <div style="height: calc(100% - 26px); overflow: auto">
                <div class="implementContent" v-for="item in 6">
                  <el-image
                    class="implementIcon"
                    :src="require('@/assets/icons/implementIcon.png')"
                  ></el-image>
                  <div class="contentBox">
                    <div class="row1">
                      <div>更新车道指示器</div>
                      <div>13:25:45</div>
                    </div>
                    <div class="row2">zk546+11车道指示器46 - 禁行</div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import $ from "jquery";
import { icon, laneImage } from "../../../utils/configData.js";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { listType } from "@/api/equipment/type/api.js";
import { listSdEmergencyPer } from "@/api/event/SdEmergencyPer";
import { listMaterial } from "@/api/system/material";
import {
  listEvent,
  updateEvent,
  getEvent,
  getImplement,
} from "@/api/event/event";
import { image, video } from "@/api/eventDialog/api.js";
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { listEventFlow, getListBySId } from "@/api/event/eventFlow";
import { getDeviceData } from "@/api/workbench/config.js";
import {
  previewDisplay,
  getSubareaByTunnelId,
} from "@/api/event/reserveProcess";
export default {
  name: "dispatch",
  data() {
    return {
      eqTypeStateList: null,
      eventMsg: null,
      fqIndex: null,
      hfData: null, //恢复预案列表
      planListEnd: null,
      previewList: null,
      partitionData: null,
      planList1: [
        {
          text: "火灾报警1区",
        },
        {
          text: "火灾报警2区",
        },
        {
          text: "火灾报警3区",
        },
        {
          text: "火灾报警4区",
        },
      ],
      lightSwitch: 0,
      changeVideo: 0,
      eqTunnelData: [],
      urls: [],
      videoUrl: "",
      videoList: [
        {
          src: require("@/assets/Example/v1.mp4"),
        },
        {
          src: require("@/assets/Example/d1.mp4"),
        },
      ],
      // 策略按钮
      strategyList: [
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
        {
          name: "火灾报警1区",
        },
      ],
      // 人员列表
      personnelList: [],
      //应急物资
      materialList: [],
      coviList: [
        {
          name: "CO",
          phone: "XXX",
          phone2: "0.00",
        },
      ],
      //车道列表
      laneUrlList: laneImage,
      eventForm: {
        // eventType:{},
        // tunnels:{}
      },
      eventGradeOptions: [],
      // 时间轴循环事件数据
      eventList: [],
      moveTop: 0.11,
      py: "", //开始y轴的位置
      px: "", //开始x轴的位置
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: "",
      },
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
      selectedIconList: [], //配置图标
    };
  },
  computed: {
    ...mapState({
      deviceStatusChangeLog: (state) => state.websocket.deviceStatusChangeLog,
    }),
  },
  watch: {
    deviceStatusChangeLog(event) {
      // console.log(event, "websockt工作台接收感知事件数据");
      console.log(event, "已执行");
      var zxc = event;
    },
  },
  mounted() {
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 0);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);
  },
  created() {
    this.getEventData();
    if (this.$route.query.id) {
      const param = {
        id: this.$route.query.id,
      };
      listEvent(param).then((response) => {
        this.eventForm = response.rows[0];
        this.eventForm.eventType = response.rows[0].eventType.eventType;
        this.eventForm.tunnelName = response.rows[0].tunnels.tunnelName;
      });
    }
    this.getDicts("sd_incident_level").then((response) => {
      this.eventGradeOptions = response.data;
    });
    this.accidentInit();
  },
  methods: {
    getRealTimeData() {
      getDeviceData({
        tunnelId: this.currentTunnel.id,
      }).then((response) => {
        // console.log(response,"设备实时数据")
        // for (let i = 0; i < response.data.length; i++) {
        // debugger;
        // 实时状态
        // let type = response.data[i].eqType;
        // if (type != "" && type != undefined) {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          if (deviceData) {
            // console.log(deviceData, "deviceData");
            // let type = deviceData.eqType;

            // 需要换光标的
            console.log();
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 31, 32, 33, 35,
                ];
                if (arr.includes(deviceData.eqType)) {
                  if (
                    // 摄像机之类的只有在线 离线 故障图标
                    this.eqTypeStateList[k].stateType == "1" &&
                    this.eqTypeStateList[k].state == deviceData.eqStatus
                  ) {
                    //取设备监测状态图标
                    this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                    if (deviceData.eqType == 19) {
                      this.selectedIconList[j].num =
                        "CO:" +
                        parseFloat(deviceData.CO).toFixed(2) +
                        "/PPM  VI:" +
                        parseFloat(deviceData.VI).toFixed(2) +
                        "KM";
                    } else if (deviceData.eqType == 17) {
                      this.selectedIconList[j].num =
                        parseFloat(deviceData.FS).toFixed(2) +
                        "m/s " +
                        deviceData.FX;
                    } else if (deviceData.eqType == 5) {
                      if (deviceData.DWLD) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.DWLD).toFixed(2) + "lux";
                      }
                    } else if (deviceData.eqType == 18) {
                      if (deviceData.DNLD) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                      }
                    }
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

                // let url = this.eqTypeStateList[k].url;
                // this.selectedIconList[j].eqDirection =
                // deviceData.eqDirection;
                // if (deviceData.eqDirection == "1") {
                //   //上行车道
                //   if (url.length > 1) {
                //     this.selectedIconList[j].url = [url[1], url[0]];
                //   } else {
                //     this.selectedIconList[j].url = url;
                //   }
                // } else {
                //   this.selectedIconList[j].url =
                //     this.eqTypeStateList[k].url;
                // }
                // this.selectedIconList[j].state = deviceData.eqStatus;
              }
            }
            // 不需要换光标的
            // let paramType = [5,17, 18, 19, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
            // if (paramType.includes(parseInt(type))) {
            //   if (deviceData.eqStatus == "null" || !deviceData.eqStatus) {
            //     this.selectedIconList[j].value = "0";
            //   } else {
            //     this.selectedIconList[j].value = deviceData.eqStatus;
            //   }
            // }
          }
        }
      });
    },
    eventDo(item) {
      console.log(item);
      var data = { eventId: this.eventMsg.id, reserveId: item.id };
      getImplement(data).then((result) => {
        if (result.code == 200) {
          this.$modal.msgSuccess(result.msg);
        }
      });
    },
    async getEventData() {
      await getEvent(this.$route.query.id).then((result) => {
        this.eventMsg = result.data;
      });
      this.getSubareaByTunnel();
      this.getTunnelData();
      this.getmaterialList(); //应急物资
      this.getpersonnelList(); //调度电话
      this.getUrl();
    },
    getListBySIdData(id) {
      var data = { subareaId: id, category: "2" };
      getListBySId(data).then((result) => {
        console.log(result, "===================");
        this.hfData = result.data;
      });
    },
    accidentInit() {
      var eventId = { eventId: this.$route.query.id };
      listEventFlow(eventId).then((result) => {
        this.eventList = result.rows;
      });
    },
    // randomEvent() {
    //   var index = Math.floor(Math.random() * 2 + 2);
    //   this.planList1[index].style =
    //     "border:1px solid red;background-color: rgba(255,0,0,0.6);";
    //   this.rightClick(index);
    // },

    async getSubareaByTunnel() {
      console.log(this.eventMsg);
      const params = this.eventMsg.tunnelId;
      await getSubareaByTunnelId(params).then((result) => {
        this.planList1 = result.data;
      });
    },
    // 预览
    getPreview(row) {
      previewDisplay(row.id).then((res) => {
        this.previewList = res;
        var deviceList = [];
        for (let i = 0; i < this.previewList.length; i++) {
          var item = this.previewList[i].strategyRl;
          for (let z = 0; z < item.length; z++) {
            var arr = this.previewList[i].iFileList[z];
            if (item[z].equipments.indexOf(",")) {
              deviceList.push({
                list: item[z].equipments.split(","),
                state: item[z].state,
                eqId: this.previewList[i].deviceTypeId,
                file: arr,
              });
            } else {
              deviceList.push({
                list: item[z].equipments,
                state: item[z].state,
                eqId: this.previewList[i].deviceTypeId,
                file: arr,
              });
            }
          }
        }
        this.deviceList = deviceList;
        this.ChangeDeviceState();
      });
      this.workbenchOpen = true;
    },
    ChangeDeviceState() {
      for (let i = 0; i < this.selectedIconList.length; i++) {
        for (let x = 0; x < this.deviceList.length; x++) {
          var eqType = this.selectedIconList[i].eqType;
          if ((eqType ?? "") !== "") {
            if (eqType == this.deviceList[x].eqId) {
              var brr = this.deviceList[x].list;
              for (let p = 0; p < brr.length; p++) {
                if (this.selectedIconList[i].eqId == brr[p]) {
                  this.selectedIconList[i].url = [];
                  let url = this.deviceList[x].file;
                  url.forEach((item) => {
                    this.selectedIconList[i].url.push(item.url);
                  });
                }
              }
            }
          }
        }
      }
    },
    /** 查询应急人员信息列表 */
    getpersonnelList() {
      const params = {
        tunnelId: this.eventMsg.tunnelId,
      };
      listSdEmergencyPer(params).then((response) => {
        this.personnelList = response.rows;
      });
    },
    getmaterialList() {
      const params = {
        tunnelId: this.eventMsg.tunnelId,
      };
      listMaterial(params).then((response) => {
        this.materialList = response.rows;
      });
    },

    rightClick(index) {
      this.fqIndex = index;
    },
    mouseleave(index) {
      this.fqIndex = null;
    },

    returnDetails() {
      this.changeVideo = 0;
    },
    submitEventForm() {
      delete this.eventForm.eventType; //删除age元素
      updateEvent(this.eventForm).then((response) => {
        this.$modal.msgSuccess("事件更新成功");
      });
    },
    //
    eventFormClose() {
      const param = {
        id: this.$route.query.id,
        eventState: "1",
      };
      this.$modal
        .confirm("是否确认结束事件")
        .then(function () {
          return updateEvent(param);
        })
        .then(() => {
          this.planListEnd = null;
          this.$modal.msgSuccess("事件处理成功");
        })
        .catch(() => {});
    },
    /* 鼠标点击*/
    dian(event) {
      if (event.button != 0) {
        return;
      }
      let parentObj = document.getElementById("eq-wrapper");
      wrapperClientX = parentObj.getBoundingClientRect().left;
      wrapperClientY = parentObj.getBoundingClientRect().top;
      this.px = event.clientX + event.clientX * this.moveTop - wrapperClientX;
      this.py = event.clientY + event.clientY * this.moveTop - wrapperClientY;
      boxEqList = [];
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
    },
    /* 获取隧道配置信息*/
    getTunnelData() {
      var tunnelId = this.eventMsg.tunnelId; //"JQ-JiNan-WenZuBei-MJY";
      let that = this;
      that.upList = [];
      that.downList = [];

      getTunnels(tunnelId).then((response) => {
        let res = response.data.storeConfigure;
        // console.log(response.data, "12312312");
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          listType()
            .then((response) => {
              var arr = [];
              for (let item1 of response.rows) {
                for (let item of res.eqList) {
                  item.focus = false;
                  if (item1.typeId == item.eqType) {
                    item.iconWidth = Number(item1.iconWidth);
                    item.iconHeight = Number(item1.iconHeight);
                    arr.push(item);
                  }
                }
              }
              this.selectedIconList = arr; //这是最终需要挂载到页面上的值
              for (let p = 0; p < this.selectedIconList.length; p++) {
                for (let i = 0; i < this.planList1.length; i++) {
                  let axx = this.selectedIconList[p];
                  let bxx = this.planList1[i];
                  if (bxx.pileMin == "0") {
                    if (String(axx.pile).trim() == String(bxx.pileMax).trim()) {
                      // 定义获取最大值的left
                      var leftMax = axx.position.left;
                      var leftMin = 0;
                      var deviceWidth = Number(leftMax) - Number(leftMin);
                      var deviceHeight = axx.position.top;
                      bxx.width = deviceWidth;
                      bxx.height = deviceHeight;
                      bxx.top = deviceHeight;
                      bxx.left = leftMin;
                    }
                  } else if (bxx.pileMax == "100") {
                    if (String(axx.pile).trim() == String(bxx.pileMin).trim()) {
                      var leftMax = Number(1640);
                      var leftMin = axx.position.left;
                      var deviceWidth = Number(leftMax) - Number(leftMin);
                      var deviceHeight = axx.position.top;
                      bxx.width = deviceWidth;
                      bxx.height = deviceHeight;
                      bxx.top = deviceHeight;
                      bxx.left = leftMin;
                    }
                  } else {
                    if (String(bxx.pileMin).trim() == String(axx.pile).trim()) {
                      bxx.leftMin = axx.position.left;
                      bxx.deviceHeight = axx.position.top;
                    }
                    if (String(bxx.pileMax).trim() == String(axx.pile).trim()) {
                      bxx.leftMax = axx.position.left;
                    }
                  }
                }
              }
              this.planList1.forEach((item, index) => {
                // item.style = "display:none;";
                // debugger;
                if (item.leftMax != undefined && item.leftMin != undefined) {
                  var deviceWidth = Number(item.leftMax) - Number(item.leftMin);
                  item.width = deviceWidth;
                  item.height = item.deviceHeight;
                  item.top = item.deviceHeight;
                  item.left = item.leftMin;
                }
                console.log(item.direction, "方向");
                if (item.direction == "1") {
                  item.top = 0;
                }
                // 当前事故点的桩号
                var positionCurrent = this.getNumber(this.eventMsg.stakeNum);
                var positionMin = this.getNumber(item.pileMin);
                var positionMax = this.getNumber(item.pileMax);
                console.log(
                  positionCurrent,
                  positionMin,
                  positionMax,
                  "方向=>",
                  this.eventMsg.direction
                );
                if (this.planList1.length == 4) {
                  if (
                    positionCurrent > positionMin &&
                    item.direction == this.eventMsg.direction
                  ) {
                    this.fqIndex = index;
                    item.style =
                      "border:1px solid red;background-color: rgba(255,0,0,0.6);";
                    // this.rightClick(index);
                    item.show = true;
                    this.getListBySIdData(item.id);
                  }
                } else {
                  if (
                    positionCurrent > positionMin &&
                    positionCurrent < positionMax &&
                    item.direction == this.eventMsg.direction
                  ) {
                    console.log(item, "zxczxc");
                    this.fqIndex = index;
                    item.style =
                      "border:1px solid red;background-color: rgba(255,0,0,0.6);";
                    // this.rightClick(index);
                    item.show = true;
                    this.getListBySIdData(item.id);
                  }
                }
              });

              console.log(this.selectedIconList);
              this.planListEnd = this.planList1;
              console.log(this.planListEnd);
            })
            .then(() => {});
        } else {
          //不存在
          that.selectedIconList = [];
          //工作台默认背景图
          // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
    },
    getNumber(number) {
      return number.replace(/[^0-9]/gi, "");
    },
    getUrl() {
      const param3 = {
        businessId: this.$route.query.id,
      };
      const param4 = {
        id: this.$route.query.id,
      };
      image(param3).then((response) => {
        this.urls = response.data;
      });
      video(param4).then((response) => {
        this.videoUrl = response.data;
      });
    },
  },
};
</script>

<style scoped lang="scss">
.active {
  z-index: 3;
}
.leftContent {
  width: 30%;
  height: 100%;
}
.rightContent {
  width: 70%;
  height: 100%;
  margin-left: 10px;
}
::v-deep .el-card__body {
  height: 100%;
  overflow-y: auto;
  padding: 0;
}
::v-deep .timelineClass {
  height: calc(100% - 50px);
  // .el-card__body{
  //   height: 100%;
  //   overflow-y: auto;
  // }
}
.formLable {
  width: 60px;
  display: inline-block;
}
.steps {
  height: 26%;
}
::v-deep .el-icon-sunrise {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #1d58a9;
}
::v-deep .el-icon-sunrise:before {
  content: "11";
  visibility: hidden;
}
/* 进行中状态：圈线 */
::v-deep .steps .el-step__line {
  color: #1d58a9;
  border-color: #1d58a9;
}
::v-deep .el-icon-eleme {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #00ff7f;
}
::v-deep .el-icon-eleme:before {
  content: "11";
  visibility: hidden;
}
.back-img {
  width: 100% !important;
  height: 100% !important;
  position: absolute;
}
.changeMap {
  width: 30px;
  height: 30px;
  position: absolute;
  left: 0px;
  top: 0px;
  cursor: pointer;
  z-index: 10;
}
::v-deep .bottomEventForm {
  width: 100%;
  height: 100%;
  .el-card__body {
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    .tunnelMap {
      height: 100%;
      width: 100%;
      position: relative;
      margin-bottom: 10px;
      padding-bottom: 10px;
      .maskClass {
        height: 98%;
        width: 100%;
        position: absolute;
        top: 0;
        left: 0;
        .mousemoveBox {
          height: 50%;
          position: absolute;
          display: inline-block;
          z-index: 2;
          .partitionBox {
            width: 100%;
            height: 100%;
            color: white;
            position: relative;
          }
          .partitionBox:hover {
            background-color: rgba($color: #fff, $alpha: 0.3);
          }
          .rightClickClass {
            width: 100%;
            height: 100%;
            border: solid 1px white;
            position: relative;
            top: -100%;
            // left: 15%;
            // display: none;
            color: white;
            background-color: rgba($color: #005e96, $alpha: 0.5);
            z-index: 4;
            border-radius: 10px;
            box-shadow: 0 0 5px white;
            .recoveryBox {
              height: 100%;
              display: flex;
            }
            .row1 {
              width: 100%;
              height: 20%;
              border-bottom: dashed 1px #ccc;
              display: flex;
              justify-content: center;
              align-items: center;
            }
            .row2 {
              width: 100%;
              height: 50%;
              display: flex;
              justify-content: space-around;
              padding: 10px;
              div {
                width: 35%;
                height: 30px;
                border: solid 1px white;
                border-radius: 10px;
                text-align: center;
                font-size: 14px;
                line-height: 29px;
                cursor: pointer;
              }
              div:nth-of-type(1):hover {
                background-color: #e1aa43;
              }
              div:nth-of-type(2):hover {
                background-color: #19b9ea;
              }
            }
          }
        }
      }
      .maskClass1 {
        height: 98%;
        width: 100%;
        position: absolute;
        top: 0;
        left: 0;
        .rightClickClass {
          height: 50%;
          position: relative;
          display: inline-block;
          border: solid 1px white;
          vertical-align: top;
          .rightClickButton {
            height: 70%;
            border: solid 1px white;
            box-shadow: 0 0 5px white;
            background-color: rgba($color: #005e96, $alpha: 0.5);
            width: 70%;
            border-radius: 10px;
            display: none;
            margin: 10% auto;
            position: relative;
            color: white;
            vertical-align: top;
            z-index: 4;
          }
          .row1 {
            width: 100%;
            height: 50%;
            border-bottom: dashed 1px #ccc;
            display: flex;
            justify-content: center;
            align-items: center;
          }
          .row2 {
            width: 100%;
            height: 50%;
            display: flex;
            justify-content: space-around;
            padding: 10px;
            div {
              width: 35%;
              height: 30px;
              border: solid 1px white;
              border-radius: 10px;
              text-align: center;
              font-size: 14px;
              line-height: 29px;
              cursor: pointer;
            }
            div:nth-of-type(1):hover {
              background-color: #e1aa43;
            }
            div:nth-of-type(2):hover {
              background-color: #19b9ea;
            }
          }
        }
      }
    }
  }
}
.tunnelBox {
  height: 57%;
  padding: 15px;
}
.formBox {
  height: 41%;
  padding: 15px;
  margin-top: 16px;
  padding-top: 0;
}
::v-deep .el-input {
  width: 263px;
}
::v-deep ul {
  padding-inline-start: 8px !important;
}
.strategyButton {
  margin-top: 10px;
  width: 138px;
}
::v-deep .el-button {
  margin-left: 10px;
}
// 工作台
.wrapper {
  height: 98%;
  width: 100%;
  position: absolute;
  // z-index: 3;
  top: 0px;
  left: 0px;
}
.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  // align-items: center;
  width: 30px !important;
}
.theme-light .app-container {
  background-color: #010913;
  border: none !important;
}
.theme-dark .app-container {
  background-color: #d6edfb;
}
.app-container {
  // background-color: #D6EDFB;
  padding: 0px 16px;
  height: calc(100% + 60px);
  padding: 20px;
}
.formTitle {
  width: 100%;
  height: 35px;
  line-height: 35px;
  padding-left: 10px;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 6px;
}
.formButton {
  width: 130px;
  height: 35px;
  float: right;
  color: white;
  border-radius: 2px;
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  > .el-image {
    width: 15%;
    height: 50%;
    margin-right: 4px;
  }
}
.formButton2 {
  background-image: linear-gradient(2deg, #4b6ad4, #07a1fb);
}
.formButton1 {
  background: linear-gradient(2deg, #37d8ac, #1ccf7b);
  margin-left: 10px;
}
.cameraBox {
  width: 100%;
  height: calc(100% - 35px);
  display: flex;
  justify-content: space-between;
  padding-top: 10px;
  .el-image {
    width: 49%;
    height: 100%;
    border: solid 1px #e0e7ed;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    ::v-deep .el-image__inner {
      width: 60%;
      height: 65%;
    }
  }
  video {
    width: 49%;
    height: 100%;
    border: solid 1px #e0e7ed;

    object-fit: cover;
  }
}
.video1 {
  width: 100%;
  height: 150px;
  object-fit: cover;
  z-index: -100;
  border-radius: 10px;
}
.rightBox {
  width: 100%;
  padding: 10px;
  > .video {
    width: 100%;
    height: 26%;
    display: flex;
    justify-content: space-between;
  }
}

.videoBox {
  height: 30%;
}
.planBox {
  margin-top: 10px;
  height: 35%;
  > .title {
    font-size: 14px;
    font-weight: bold;
    height: 26px;
    line-height: 20px;
    margin: 0px auto 0px auto;
  }
  // .planMiniBox{
  //   width: 43%;height: 68px;border-radius: 10px;border: solid 1px #00A0FF;display: inline-block;margin: 8px;
  //   >.miniTitle{
  //     width: 100%;text-align: center;font-size: 12px;line-height: 24px;font-weight: bold;border-bottom: dashed 1px #00A0FF;
  //     height: 30px;line-height: 30px;
  //   }
  //   >.planButtonS{
  //     height: 26px;width: 100%;display: flex;justify-content: space-around;padding-top: 4px;color: white;font-size: 12px;
  //     text-align: center;line-height: 23px;
  //     >div{
  //       width: 45%;height: 100%;background: linear-gradient(2deg, #4B6AD4, #07A1FB);border-radius: 2px;cursor: pointer;
  //     }
  //   }
  // }
}
.phoneBox {
  margin-top: 10px;
  height: calc(35% - 20px);
  > .title {
    font-size: 14px;
    font-weight: bold;
    height: 26px;
    line-height: 20px;
    margin: 0px auto 0px auto;
  }

  .phoneButton {
    width: 40px;
    height: 23px;
    border-radius: 12px;
    .el-image {
      width: 14px;
      height: 17px;
      padding-top: 4px;
    }
  }
}
.processBox {
  height: 65%;
  margin-left: 10px;
  ::v-deep .el-step__icon.is-icon {
    // background: linear-gradient(114deg, #96DAFA, #5FA6F5);
    width: 40px;
    height: 40px;
    border-radius: 20px;
  }
  ::v-deep .el-icon-edit {
    background: url(../../../assets/icons/earlyWarning1.png);
    background-position: top;
    background-repeat: no-repeat;
    background-size: 80% 80%;
  }

  ::v-deep .el-icon-edit:before {
    content: "11";
    visibility: hidden;
  }
  ::v-deep .el-icon-upload {
    background: url(../../../assets/icons/confirm1.png);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 80% 80%;
  }
  ::v-deep .el-icon-upload:before {
    content: "11";
    visibility: hidden;
  }
  ::v-deep .el-icon-picture {
    background: url(../../../assets/icons/management1.png);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 80% 80%;
  }
  ::v-deep .el-icon-picture:before {
    content: "11";
    visibility: hidden;
  }
  ::v-deep .el-icon-s-tools {
    background: url(../../../assets/icons/end1.png);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 80% 80%;
  }
  ::v-deep .el-icon-s-tools:before {
    content: "11";
    visibility: hidden;
  }
  ::v-deep .el-step__title {
    font-size: 12px;
  }
  // ::v-deep .el-step__title.is-finish,.el-step__title.is-process{
  //   // color: #333;
  // }
  // ::v-deep .el-step__title.is-process{
  //   font-weight: 400;
  //   // color: #333;
  // }
  ::v-deep .el-step.is-horizontal .el-step__line {
    top: 20px;
    // border-top: dashed 2px #8BCFF9;
    background-color: transparent;
  }

  ::v-deep .el-card {
    // border-radius: 10px;
    margin-top: 10px;
    width: 96%;
  }
  ::v-deep .el-card__body {
    padding: 15px !important;
  }
  ::v-deep .el-timeline-item {
    padding-bottom: 0px !important;
  }
  ::v-deep .el-timeline-item__node--normal {
    transform: translateY(10px);
  }
  ::v-deep .el-timeline-item__tail {
    transform: translateY(10px);
    // border-left: 2px dashed #97A8AE;
  }
  ::v-deep .el-timeline-item__wrapper {
    margin-right: 8px;
    padding-left: 20px;
  }
  .endButton {
    width: 100%;
    height: 65px;
    margin: 10px auto;
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    .ButtonBox {
      width: 32%;
      height: 100%;
      border-radius: 4px;
      .recovery {
        width: 100%;
        height: 45%;
        text-align: center;
        line-height: 2;
      }
      .button {
        height: 55%;
        display: flex;
        justify-content: space-around;
        padding-top: 5%;
        div {
          width: 40%;
          height: 80%;
          text-align: center;
          font-size: 12px;
          line-height: 2;
          border-radius: 2px;
          cursor: pointer;
        }
      }
    }
    // .el-image{
    //   width: 16px;height: 30px;padding-top: 6px;transform: translateX(-5px);
    // }
    // >div{
    //   height: 35px;display: inline-block;transform: translate(6px,-7px);
    // }
  }
}
.implement {
  height: calc(35% - 10px);
  margin-left: 10px;
  margin-top: 10px;
  > .title {
    font-size: 14px;
    font-weight: bold;
    height: 26px;
    line-height: 20px;
    margin: 0px auto 0px auto;
  }
  .implementContent {
    width: 100%;
    height: 50px;
    padding-right: 10px;
    // border-bottom: dashed 1px #E0E7ED;
    .implementIcon {
      width: 2%;
      height: 100%;
      text-align: center;
      padding-top: 11px;
      margin-left: 1%;
      margin-right: 6%;
    }
    .contentBox {
      display: inline-block;
      height: 100%;
      width: 90%;
      font-size: 12px;
      transform: translateY(-20%);
      .row1 {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        height: 30px;
        > div:nth-of-type(1) {
          // color: #00A0FF;
        }
        > div:nth-of-type(2) {
          // color: #999999;
        }
      }
      .row2 {
        display: flex;
        align-items: flex-end;
        height: 20px;
        // color: #333;
      }
    }
  }
}
::-webkit-scrollbar-track-piece {
  background-color: rgba($color: #00c2ff, $alpha: 0.1);
  border-left: 1px solid rgba(0, 0, 0, 0);
}
::-webkit-scrollbar {
  width: 8px;
  height: 10px;
}
::-webkit-scrollbar-thumb {
  background-color: rgba($color: #00c2ff, $alpha: 0.6);
  background-clip: padding-box;
  border-radius: 10px;
  min-height: 28px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #00c2ff;
}
::v-deep .el-icon-picture-outline {
  background: url(../../../assets/icons/outline.png);
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
::v-deep .el-icon-picture-outline:before {
  content: "11";
  visibility: hidden;
}

// ::v-deep .el-table thead{
//   color: #1287B2 !important;
// }

/*table滚动条背景色 */
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  background-color: rgba($color: #00c2ff, $alpha: 0.1);
  width: 8px;
  height: 10px;
}

/* table滚动条的滑块*/
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: rgba($color: #00c2ff, $alpha: 0.6);
  border-radius: 10px;
}
/* 鼠标悬浮*/
::v-deep .el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #c4e8f6;
}
::v-deep .el-table__body {
  width: 264px !important;
}
.rightClickClass .processBox .endButton {
  justify-content: space-around;
}
.mousemoveBox {
  .recoveryBox {
    height: 80% !important;
    display: grid !important;
    grid-template-columns: repeat(2, 50%);
    grid-template-rows: repeat(2, 50%);
    padding: 5px 10px;
    .endButton {
      padding: 5px;
      box-sizing: border-box;
      .ButtonBox {
        border: 1px solid #07a1fb;
        .recovery {
          font-size: 15px;
          text-align: center;
          border-bottom: 1px dashed #07a1fb;
        }
        .button {
          display: flex;
          justify-content: space-around;
          align-items: center;
          div {
            font-size: 14px;
            margin: 5px 0px;
            background: linear-gradient(
              172deg,
              rgba(0, 172, 237, 0.8),
              rgba(0, 121, 219, 0.8)
            );
            padding: 5px;
            border-radius: 4px;
          }
        }
      }
    }
  }
}
.active .rightClickClass {
  display: block;
}
</style>
