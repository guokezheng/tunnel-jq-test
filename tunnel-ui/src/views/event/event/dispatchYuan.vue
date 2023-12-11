<template>
  <div class="app-container dispatchAss">
    <el-row>
      <el-col :span="7">
        <div class="dispatchLeft">
          <div class="video">
            <div class="title">实时视频</div>
            <div class="videoBox1">
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm1.liveUrl "
                  :rtsp="videoForm1.liveUrl"
                  :open="videoForm1.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm1.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm2.liveUrl "
                  :rtsp="videoForm2.liveUrl"
                  :open="videoForm2.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm2.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm3.liveUrl "
                  :rtsp="videoForm3.liveUrl"
                  :open="videoForm3.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm3.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm4.liveUrl "
                  :rtsp="videoForm4.liveUrl"
                  :open="videoForm4.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm4.title }}</div>
              </div>
              <!-- <div
                v-for="(item, index) of videoList"
                :key="index"
                class="videoContent"
              >
                <videoPlayer
                  v-if="item.liveUrl "
                  :rtsp="item.liveUrl"
                  :open="cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ item.title }}</div>
              </div> -->
            </div>
          </div>
          <div class="evtMessage">
            <div class="title">事件信息</div>
            <div class="evtMessBox">
              <div class="evtMessLeft">
                <div>
                  <div>事件来源：</div>
                  <div>{{ getFrom(eventForm.eventSource) }}</div>
                </div>
                <div>
                  <div>事件类型：</div>
                  <div>{{ eventForm.simplifyName }}</div>
                </div>
                <div>
                  <div>所属隧道：</div>
                  <div>{{ eventForm.tunnelName }}</div>
                </div>
                <div>
                  <div>方向：</div>
                  <div>{{ getDirection(eventForm.direction) }}</div>
                </div>
                <div>
                  <div>影响车道：</div>
                  <div>{{ eventForm.laneNo }} 车道</div>
                </div>
                <div>
                  <div>桩号：</div>
                  <div>{{ eventForm.stakeNum }}</div>
                </div>
              </div>
              <div class="evtMessRight">
                <div class="evtMessVideo">
                  <video
                    :src="eventForm.videoUrl"
                    controls
                    muted
                    loop
                    fluid
                  ></video>
                </div>
                <div class="evtMessImg" v-if="eventForm.iconUrlList && eventForm.iconUrlList.length > 0">
                  <el-image
                    v-for="(item, index) of eventForm.iconUrlList"
                    :key="index"
                    :src="item.imgUrl"
                    :preview-src-list="Array(item.imgUrl)"
                  ></el-image>
                </div>
                <img
                  src="../../../assets/cloudControl/nullImg.png"
                  v-else
                  style="width: 46px; margin: 0 auto; display: flex"
                />
              </div>
            </div>
          </div>
          <div class="plan">
            <div class="title">调度联络</div>
            <el-table
              :data="implementList"
              max-height="110"
              class="phoneTable"
            >
              <el-table-column
                label="姓名"
                align="center"
                prop="userName"
                width="150"
              />
              <el-table-column label="联系方式" align="center" prop="phone">
                <template slot-scope="scope">
                  <span>{{ scope.row.phone }}</span>
                  <img :src="mesIcon" />
                  <img :src="phoneIcon" />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :span="17">
        <div class="dispatchRight">
          <div class="workBenchBox">
            <div class="tunnelBox1" v-show="activeMap == 1">
              <div style="width:100%;height:100%;overflow:hidden">
                <div class="tunnelMap"
                  @mousedown="dragImg"
                  ref="dragImgDom"
                  @contextmenu.prevent>
                  <el-image
                    class="back-img"
                    :src="backImg"
                    :style="{
                      width: picWidth / 1.23 + 'px',
                  }"
                  ></el-image>
                  <div class="wrapper" id="eq-wrapper">
                    <div
                      class="icon-box active"
                      v-for="(item, index) in selectedIconList"
                      :key="index"
                      @click="openStateSwitch(item)"
                      :style="{
                        left: item.position.left / 1.23 + 'px',
                        top: item.position.top / 1.23 + 'px',
                        'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                      }"
                      :class="
                        item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                          ? 'light-' + item.position.left
                          : ''
                      "
                    >
                      <div :class="{ focus: item.focus }">
                        <img
                          v-show="item.eqType != ('31' || '16' || '36')"
                          v-for="(url, indexs) in item.url"
                          style="position: absolute"
                          :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth / 1.23"
                          :height="item.iconHeight / 1.23"
                          :key="item.eqId + indexs"
                          :src="url"
                        />
                        <img
                          v-show="item.eqType == '31'"
                          style="position: absolute"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth / 1.3"
                          :height="item.iconHeight"
                          :src="getTypePic(item)"
                        />
                        <!-- 情报板洞内 -->
                        <div
                          v-show="item.eqType == '16' || item.eqType == '36'"
                          style="
                            position: absolute;
                            overflow: hidden;
                            writing-mode: tb-rl;
                            font-size: 12px;
                            color: #ffff07;
                            text-align: center;
                            padding: 2px;
                          "
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.iconWidth / 1.3 + 'px',
                            height: item.iconHeight / 1.3 + 'px',
                          }"
                          :src="getTypePic(item)"
                        >
                          {{ item.eqName }}
                        </div>

                      </div>
                    </div>
                  </div>
                  <div class="accBox">
                    <div class="accTop">
                      <div class="accPoint"
                      :style="{
                        height:160/tunnelLane + 'px',
                        width:160/tunnelLane + 'px',
                        left:accLeft + '%',
                        top:accTop + '%'
                      }"
                      v-if="eventForm.direction == '2'"
                      >
                    <img :src="assIconUrl"></img>
                    </div>
                    </div>
                    <div class="accBottom">
                      <div class="accPoint"
                      :style="{
                        height:160/tunnelLane + 'px',
                        width:160/tunnelLane + 'px',
                        left:accLeft + '%',
                        top:accTop + '%'
                      }"
                      v-if="eventForm.direction == '1'"
                      >
                      <img :src="assIconUrl"></img>
                    </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
            <div class="tunnelBox3" v-show="activeMap == 2">
              <iframe
                name="tuniframe"
                id="miframe"
                class="map3D"
                frameborder="0"
                align="center"
                allowfullscreen="true"
                allow="autoplay"
                src="http://106.120.201.126:14712/dashboard"
              ></iframe>
            </div>
            <div class="tunnelBox2">
              <div>
                <img
                  src="../../../assets/cloudControl/tunnelBox1.png"
                  style="transform: translate(-9px, -3px)"
                  @click="backSafeWarn()"
                />
              </div>
              <div>
                <img
                  src="../../../assets/cloudControl/tunnelBox2.png"
                  @click="changeActiveMap(1)"
                />
                <img
                  src="../../../assets/cloudControl/tunnelBox3.png"
                  @click="changeActiveMap(2)"
                />

              </div>
            </div>
          </div>
          <div class="rightBottom">
            <div class="evtManagement">
              <div class="IncHand">
                <div class="title">事件处置</div>
                <div class="incHandBox">
                  <div
                    v-for="(item, index) of incHandList"
                    :key="index"
                    class="incHandContent"
                  >
                    <div class="classification">
                      <div
                        class="type"
                        :style="{
                          padding: item.flowContent
                            ? item.flowContent.toString().length > 2
                              ? '8px'
                              : '15px 12px'
                            : '',
                          marginTop: item.children
                            ? item.flowContent == '设备联控'
                              ? (item.children.length * 40 +
                                  4 * (item.children.length - 1)) /
                                  2 -
                                35 +
                                'px'
                              : (item.children.length * 40 +
                                  4 * (item.children.length - 1)) /
                                  2 -
                                25 +
                                'px'
                            : '',
                        }"
                        v-if="item.flowContent"
                      >
                        {{ item.flowContent }}
                      </div>

                      <div v-show="item.flowId == 7" class="yijian" @click="getYiJian(item)"
                      :style="iconDisabled?'cursor: not-allowed;pointer-events: none;background:#ccc;border:solid 1px #ccc':'cursor: pointer'">一键</div>
                    </div>

                    <div
                      class="heng1"
                      v-if="item.children"
                      :style="{
                        marginTop: item.children
                          ? item.children.length == 1
                            ? '20px'
                            : (item.children.length * 40 +
                                4 * (item.children.length - 1)) /
                                2 +
                              'px'
                          : '',
                      }"
                    ></div>
                    <div
                      class="shu"
                      v-if="item.children"
                      :style="{
                        height: item.children
                          ? item.children.length > 1
                            ? item.children.length * 40 +
                              4 * item.children.length -
                              40 +
                              'px'
                            : '0px'
                          : '',
                        borderTop:
                          item.children && item.children.length > 1
                            ? 'solid 1px #39adff'
                            : '',
                      }"
                    ></div>
                    <div>
                      <div
                        v-for="(itm, inx) of item.children"
                        :key="inx"
                        class="contentList"
                      >
                        <div style="float: left">{{ itm.flowContent }}</div>
                        <!-- 绿对号 -->
                        <img
                          :src="incHand2"
                          style="float: right; "
                          v-show="itm.eventState != '0'"
                        />
                        <!-- 下发 -->
                        <img
                          :src="incHand1"
                          style="float: right; "
                          v-show="itm.eventState == '0'"
                          @click="openIssuedDialog(itm)"
                          :style="iconDisabled?'cursor: not-allowed;pointer-events: none;':'cursor: pointer'"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="DisRecords">
                <div class="title">处置记录</div>
                <el-timeline style="height: calc(100% - 40px); overflow: auto">
                  <el-timeline-item
                    placement="top"
                    v-for="(item, index) in eventList"
                    :key="index + item.flowTime"
                  >
                    <div>{{ item.flowDescription }}</div>
                    <div>{{ item.flowTime }}</div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </div>
            <div class="implement1">
              <div class="eqRecord">
                <div class="title">设备执行记录</div>
                <div class="eqRecordBox">
                  <div
                    class="implementContent"
                    v-for="(item, index) in zxList"
                    :key="index"
                  >
                    <el-image
                      class="implementIcon"
                      :src="eqRecordIcon"
                    ></el-image>
                    <div class="contentBox">
                      <div class="row1">
                        <div>{{ item.eqName }}</div>
                        <div style="padding-left: 20px; float: right">
                          {{ item.executeTime }}
                        </div>
                      </div>
                      <div class="row2">
                        <div>
                          {{ getDirection(item.eqDirection) }}
                          {{ getEqType(item.state, item.eqType) }}
                          {{ getExecuteResult(item.executeResult) }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="IssuedDialog"
      width="400px"
      text-align="left"
      class="IssuedDialog"
      append-to-body>
      <div v-if="this.IssuedItem.flowId == 5">是否完成?</div>
      <div v-else-if="this.IssuedItem.flowId == 6">是否上报?</div>
      <div v-else-if="this.IssuedItem.flowPid == 8">{{this.IssuedItem.flowContent}}?</div>
      <div v-else-if="this.IssuedItem.flowPid == 12 || this.IssuedItem.flowPid == 16">是否{{this.IssuedItem.flowContent}}?</div>

      <div v-else>是否确认执行?</div>
      <el-input v-model="IssuedItemContent" v-show="this.IssuedItem.flowPid != 7 && this.IssuedItem.flowId !=17 && this.IssuedItem.flowId !=18"/>
      <div style="display:flex;justify-content:right">
        <div class="IssuedButton1" @click="cancelIssuedDialog">取 消</div>
        <div class="IssuedButton2" @click="changeIncHand">确 认</div>
      </div>
      <!-- item.flowId==5? "是否完成?":item.flowId==6?"是否上报?":item.flowPid == 8?"是否通知？":"是否确认执行?" -->
    </el-dialog>
    <com-video
      class="comClass"
      v-if="[23, 24, 25].includes(this.eqInfo.clickEqType)"
      @dialogClose="dialogClose"
      :eqInfo="this.eqInfo"
      :eqTypeDialogList="this.eqTypeDialogList"
      :brandList="this.brandList"
      :directionList="this.directionList"
    ></com-video>
    <com-light
      class="comClass"
      v-if="
        [1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13].includes(this.eqInfo.clickEqType)
      "
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
      :eqTypeDialogList="this.eqTypeDialogList"
      :brandList="this.brandList"
      :directionList="this.directionList"
    ></com-light>
    <com-covi
      class="comClass"
      v-if="this.eqInfo.clickEqType == 19"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-covi>
    <com-data
      class="comClass"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      v-if="[14, 21, 32, 33, 15, 35,40,39,48,45].includes(this.eqInfo.clickEqType)"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-data>
    <com-wind
      class="comClass"
      v-if="this.eqInfo.clickEqType == 17"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-wind>
    <com-pressure
      class="comClass"
      v-if="this.eqInfo.clickEqType == 28"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-pressure>
    <com-vehicleDetec
      class="comClass"
      v-if="this.eqInfo.clickEqType == 20"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-vehicleDetec>
    <com-callPolice
      class="comClass"
      v-if="this.eqInfo.clickEqType == 34"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-callPolice>
    <com-robot
      class="comClass"
      v-if="this.eqInfo.clickEqType == 29"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-robot>
    <com-bright
      class="comClass"
      v-if="this.eqInfo.clickEqType == 5 || this.eqInfo.clickEqType == 18"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-bright>
    <com-youdao
      class="comClass"
      v-if="this.eqInfo.clickEqType == 31 || this.eqInfo.clickEqType == 30"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-youdao>
    <com-board
      class="comClass"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      v-if="this.eqInfo.clickEqType == 16 || this.eqInfo.clickEqType == 36"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-board>
    <work-bench ref="workBench"></work-bench>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { laneImage,laneImage2 } from "../../../utils/configData.js";
import { listType } from "@/api/equipment/type/api.js";
import { getDeviceData } from "@/api/workbench/config.js";
import { getEventCamera, getEntranceExitVideo} from "@/api/eventDialog/api.js";
import { videoStreaming } from "@/api/equipment/eqlist/api";

import workBench from "@/views/event/reservePlan/workBench";
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
import comBoard from "@/views/workbench/config/components/board"; //诱导灯弹窗
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";

import { listEventType } from "@/api/event/eventType";
import {
  listEqTypeState,
  getStateByData,
} from "@/api/equipment/eqTypeState/api";
import {
  dispatchExecuted,
  listEvent,
  eventFlowList,
  getHandle,
  updateHandle,
  getRelation,
  getReserveId,
  getAccidentPoint,
  implementProcess,
  implementPlan,
  performRecovery,
} from "@/api/event/event";
import { listSdEmergencyPer } from "@/api/event/SdEmergencyPer";

export default {
  components: {
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
    workBench,
    videoPlayer
  },
  data() {
    return {
      iconDisabled: false,
      disabledRadio: false,
      assIconUrl: "",
      IssuedItemContent: "",
      IssuedItem: {},
      title: "",
      IssuedDialog: false,
      accLeft: 0,
      accTop: 0,
      tunnelLane: 0,
      eqTypeDialogList: [],
      brandList: [],
      eqInfo: {},
      relationDisabled: false,
      activeMap: 1,
      eqTypeList: [],
      directionList: [],
      eventForm: {},
      timer: null,
      laneUrlList: this.$cache.local.get("navigationBar") == '0'?laneImage:laneImage2,
      eqTypeStateList: null,
      selectedIconList: [],
      backImg: "",
      eqRecordIcon: require("@/assets/cloudControl/eqRecord.png"),
      incHand1: require("@/assets/cloudControl/incHand1.png"),
      incHand2: require("@/assets/cloudControl/incHand2.png"),
      phoneIcon: require("@/assets/cloudControl/phone2.png"),
      mesIcon: require("@/assets/cloudControl/phone1.png"),
      videoUrl: require("@/assets/Example/v1.mp4"),
      imgUrl: require("@/assets/Example/pic1.jpg"),
      planType: [],
      videoList: [],
      videoForm1:{},
      videoForm2:{},
      videoForm3:{},
      videoForm4:{},

      fromList: [],
      reservePlan: {
        oneWay: "1",
        twoWay: "1",
        plan: "1",
      },
      eventList: [],
      implementList: [],
      zxList: [],
      incHandList: [],
      picWidth: "",
      dragFlag: false,
      mouseLeft: 0,
      mouseTop: 0,
      curX: 0,
      curY: 0,
    };
  },
  computed: {
    ...mapState({
      deviceStatus: (state) => state.websocket.deviceStatus,
      deviceStatusChangeLog: (state) => state.websocket.deviceStatusChangeLog,
      eventFlow: (state) => state.websocket.eventFlow,
    }),
  },
  watch: {
    deviceStatus(event) {
      console.log(event, "websockt工作台接收实时设备状态数据");
      this.deviceStatusList = event;
    },
    deviceStatusChangeLog(event) {
      // console.log(event, "websockt工作台接收感知事件数据");
      console.log(event, "已执行");
      console.log(this.$route.query.id, "this.$route.query.id");
      for (let item of event) {
        if (this.$route.query.id == item.eventId) {
          this.zxList.unshift(item);
        }
      }
    },
  },
  async created() {
    console.log(this.$route.query.id, "this.$route.query.id");

    await this.getEqTypeStateIcon();
    // await this.getTunnelData();
    await this.getDispatchExecuted();
    this.getListEvent();
    this.stateByData();
    this.getEventList();
    // this.evtHandle()
    // this.getpersonnelList()
    this.getDicts("sd_direction_list").then((response) => {
      console.log(response.data, "车道方向");
      this.directionList = response.data;
    });
    this.getDicts("sd_emergency_plan_type").then((response) => {
      console.log(response.data, "预案类型");
      this.planType = response.data;
    });
    this.getDicts("brand").then((data) => {
      console.log(data, "设备厂商");
      this.brandList = data.data;
    });
    this.getDicts("sd_direction").then((data) => {
      console.log(data, "方向");
      this.directionList = data.data;
    });
    this.getDicts("sd_monitor_state").then((data) => {
      console.log(data, "设备类型");
      this.eqTypeDialogList = data.data;
    });
    this.getDicts("sd_event_source").then((data) => {
      console.log(data,"事件来源")
      this.fromList = data.data;
    });
  },
  mounted() {
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 100);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);
  },
  methods: {
    //右键拖动
    dragImg(e) {
      // console.log(e, "e");
      // console.log(
      //   this.$refs.dragImgDom.offsetLeft,
      //   "this.$refs.dragImgDom.offsetLeft"
      // );
      if (e.button == 0) {
        return;
      }
      this.dragFlag = true;
      this.mouseLeft =
        e.clientX - parseInt(this.$refs.dragImgDom.offsetLeft) + 560;
      this.mouseTop = e.clientY - parseInt(this.$refs.dragImgDom.offsetTop);
      document.onmousemove = (e) => {
        if (this.dragFlag) {
          this.curX = e.clientX - this.mouseLeft;
          this.curY = e.clientY - this.mouseTop;
          this.$refs.dragImgDom.style.left = this.curX + "px";
          this.$refs.dragImgDom.style.top = this.curY + "px";
        }
      };
      document.onmouseup = () => {
        this.dragFlag = false;
      };
    },
    // 预览按钮
    getPreview(type) {
      // 查预案ID
      const params = {
        tunnelId: this.eventForm.tunnelId,
        category: this.reservePlan.oneWay,
        controlDirection: type,
        direction: this.eventForm.direction,
        eventId: this.eventForm.id,
        planTypeId: this.eventForm.eventTypeId,
      };
      getReserveId(params).then((res) => {
        console.log(res, "获取的预案id");
        var planId = res.data;
        this.$nextTick(() => {
          this.$refs.workBench.eventId = this.$route.query.id;
          this.$refs.workBench.id = planId; //预案ID
          this.$refs.workBench.tunnelId = this.eventForm.tunnelId;
          this.$refs.workBench.init();
        });
      });
      // this.workbenchOpen = true;
    },
    // 事件点图片
    getAccIcon() {
      let id = this.eventForm.eventTypeId;
      listEventType({ id }).then((res) => {
        this.assIconUrl = res.rows[0].iconUrl;
      });
    },
    // 事件点
    async getAcc() {
      const params = {
        stakeNum: this.eventForm.stakeNum,
        laneNo: this.eventForm.laneNo,
        tunnelId: this.eventForm.tunnelId,
      };
      await getAccidentPoint(params).then((res) => {
        console.log(res, "事件点");
        this.accLeft = res.data["tunnelLeft"] * 100;
        this.accTop = res.data["tunnelTop"] * 100;
        this.getAccIcon();
      });
    },
    // 事件处置 一键
    getYiJian(item) {
      console.log(item, "一键");
      var that = this;
      // let str = ''
      let arr = [];
      for (let itm of item.children) {
        arr.push(itm.id);
      }

      this.$confirm("是否确认执行?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        let planId = item.reserveId;
        let eventId = that.$route.query.id;

        implementPlan(planId, eventId).then((response) => {
          console.log(response, "一键下发成功");
          for (let item of that.incHandList) {
            for (let itm of item.children) {
              for (let it_m of arr) {
                if (itm.id == it_m) {
                  itm.eventState = "1";
                  that.$modal.msgSuccess("一键下发成功");
                }
              }
            }
          }
          this.evtHandle();
          this.getEventList();
        });
      });
    },
    // 返回安全预警
    backSafeWarn() {
      this.$router.push({
        path: "/emergency/safeWarn",
      });
    },
    // 关闭弹窗子组件
    dialogClose() {
      this.eqInfo.clickEqType = 0;
    },
    // 点设备弹窗
    openStateSwitch(item) {
      console.log(item, "点击的设备");
      this.eqInfo = {
        clickEqType: item.eqType,
        equipmentId: item.eqId,
      };
    },

    // 关联事件
    relation(type) {
      const params = {
        tunnelId: this.eventForm.tunnelId,
        category: this.reservePlan.oneWay,
        controlDirection: type,
        direction: this.eventForm.direction,
        eventId: this.eventForm.id,
        planTypeId: this.eventForm.eventTypeId,
      };
      getRelation(params).then((res) => {
        console.log(res, "关联事件");
        this.$modal.msgSuccess("关联成功");

        this.getListEvent();
        this.relationDisabled = true;
        this.disabledRadio = true;
      });
    },
    // 打开下发事件弹窗
    openIssuedDialog(item) {
      this.IssuedDialog = true;
      this.title = "警告";
      this.IssuedItem = item;
    },
    // 关闭下发事件弹窗
    cancelIssuedDialog() {
      this.IssuedDialog = false;
    },
    changeIncHand() {
      var that = this;
      console.log(this.IssuedItem, "this.IssuedItem");
      if (this.IssuedItem.flowPid == "7") {
        let processId = this.IssuedItem.processId;
        let eventId = that.$route.query.id;
        implementProcess(processId, eventId).then((response) => {
          console.log(response, "单点下发");
          that.$modal.msgSuccess("状态修改成功");
          this.IssuedDialog = false;
          this.getDispatchExecuted();
          that.evtHandle();
          that.getEventList();
        });
      } else if (this.IssuedItem.flowId == "17") {
        let eventId = that.$route.query.id;
        let handleId = this.IssuedItem.id;
        performRecovery(eventId, handleId).then((res) => {
          console.log(res, "解除管控");
          this.IssuedDialog = false;
          this.IssuedItemContent = "";
          that.evtHandle();
          that.getEventList();
        });
      } else {
        const params = {
          id: that.$route.query.id,
          ids: this.IssuedItem.id,
          remark: this.IssuedItemContent,
        };
        updateHandle(params).then((res) => {
          console.log(res, "单点改状态");
          console.log(that.incHandList, "this.incHandList");
          that.$modal.msgSuccess("状态修改成功");
          this.IssuedDialog = false;
          this.IssuedItemContent = "";
          that.evtHandle();
          that.getEventList();
          if (this.IssuedItem.flowId == "18") {
            this.iconDisabled = true;
          }
        });
      }
    },
    // 事件处置
    async evtHandle() {
      await getHandle({
        id: this.$route.query.id,
        eventTypeId: this.eventForm.eventTypeId,
      }).then((res) => {
        let list = this.handleTree(res.data, "flowId", "flowPid");
        //  for(let item of list){
        //  }
        for (var i = 0; i < list.length; i++) {
          for (var j = i; j < list.length - 1; j++) {
            if (Number(list[i].flowSort) > Number(list[j + 1].flowSort)) {
              const temp = list[i];
              list[i] = list[j + 1];
              list[j + 1] = temp;
            }
          }
        }
        for (var i = 0; i < list.length; i++) {
          if (list[i].children) {
            for (var a = 0; a < list[i].children.length; a++) {
              for (var b = a; b < list[i].children.length - 1; b++) {
                if (
                  Number(list[i].children[a].flowSort) >
                  Number(list[i].children[b + 1].flowSort)
                ) {
                  const temp = list[i].children[a];
                  // console.log(temp,"temptemptemptemptemptemp")
                  list[i].children[a] = list[i].children[b + 1];
                  list[i].children[b + 1] = temp;
                }
              }
            }
          }
        }
        this.incHandList = list;
        for (let item of this.incHandList) {
          for (let itm of item.children) {
            if (itm.flowId == 18 && itm.eventState == "1") {
              this.iconDisabled = true;
              this.relationDisabled = true;
              this.disabledRadio = true;
            }
          }
        }
        this.$forceUpdate();
      });
    },
    // 查设备状态
    stateByData() {
      const params = {
        isControl: 1,
      };
      getStateByData(params).then((res) => {
        console.log(res.rows, "查设备状态 正红泛绿...");
        this.eqTypeList = res.rows;
      });
    },
    // 事件详情
    async getListEvent() {
      if (this.$route.query.id) {
        const param = {
          id: this.$route.query.id,
        };
        await listEvent(param).then((response) => {
          console.log(response, "事件详情");
          this.eventForm = response.rows[0];
          this.eventForm.iconUrlList = response.rows[0].iconUrlList.splice(0,4)
          this.getVideoList()
          this.getpersonnelList();
          this.evtHandle();
          this.getTunnelData();
          setTimeout(() => {
            this.getAcc();
          }, 500);
        });
      }
    },
    // 左上角视频
    getVideoList(){
      this.videoForm1.cameraPlayer = false
      this.videoForm2.cameraPlayer = false
      this.videoForm3.cameraPlayer = false
      this.videoForm4.cameraPlayer = false

      console.log(this.eventForm,"this.eventForm")
      // this.videoList = []
      getEntranceExitVideo(
        this.eventForm.tunnelId,
        this.eventForm.direction
        ).then((response)=>{
          videoStreaming(response.data[0].inlet).then((response) =>{
            console.log(response,"视频流");
            response.data.title = '';
            if(response.code == 200){
              this.videoForm1 = response.data
              this.videoForm1.cameraPlayer = true
            }
          })
          videoStreaming(response.data[0].outlet).then((response) =>{
            console.log(response,"视频流");
            response.data.title = '';
            if(response.code == 200){
              this.videoForm2 = response.data
              this.videoForm2.cameraPlayer = true
            }
          })
        })
        setTimeout(()=>{
          getEventCamera(
            this.eventForm.tunnelId,
            this.eventForm.stakeNum,
            this.eventForm.direction
            ).then((res)=>{
              videoStreaming(res.data[0].eqId).then((response) =>{
                console.log(response,"视频流");
                response.data.title = '现场1';
                if(response.code == 200){
                  this.videoForm3 = response.data
                  this.videoForm3.cameraPlayer = true
                }
              })
              videoStreaming(res.data[1].eqId).then((response) =>{
                console.log(response,"视频流");
                response.data.title = '现场2';
                if(response.code == 200){
                  this.videoForm4 = response.data
                  this.videoForm4.cameraPlayer = true
                }
              })
          })
        },500)

    },
    /** 查询应急人员信息列表 */
    async getpersonnelList() {
      const params = {
        tunnelId: this.eventForm.tunnelId,
      };
      await listSdEmergencyPer(params).then((response) => {
        this.implementList = response.rows;
      });
    },
    // 切换工作台和3D隧道
    changeActiveMap(type) {
      if (type == 1) {
        this.activeMap = 1;
      } else {
        this.activeMap = 2;
      }
    },
    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.$route.query.id }).then((res) => {
        console.log(res, "处置记录");
        this.eventList = res.rows;
      });
    },
    //设备执行记录
    getEqType(state, eqType) {
      for (let i = 0; i < this.eqTypeList.length; i++) {
        let item = this.eqTypeList[i];
        if (eqType == item.stateTypeId && Number(item.deviceState) == state) {
          // console.log(item.stateName);
          return item.stateName;
        }
        //  else {
        //   continue;
        // }
      }
    },
    // 查询方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 转事件来源数据
    getFrom(from) {
      for (let item of this.fromList) {
        if (from == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    // 一进页面获取已执行数据
    getDispatchExecuted() {
      dispatchExecuted(this.$route.query.id).then((res) => {
        console.log(res, "一进页面获取已执行数据");
        this.zxList = res.data;
      });
    },
    /* 获取隧道配置信息*/
    async getTunnelData() {
      let tunnelId = this.eventForm.tunnelId;
      // var tunnelId = this.eventMsg.tunnelId; //"WLJD-JiNan-YanJiuYuan-FHS";
      let that = this;

      await getTunnels(tunnelId).then((response) => {
        this.tunnelLane = Number(response.data.lane);
        let res = response.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          console.log(res, "获取隧道配置信息");

          let id = res.lane;
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.backImg = that.laneUrlList[i].url;
              that.picWidth = that.laneUrlList[i].width;
            }
          }
          listType().then((response) => {
            // var arr = [];
            for (let i = 0; i < res.eqList.length; i++) {
              res.eqList[i].focus = false;
              for (let j = 0; j < response.rows.length; j++) {
                if (response.rows[j].typeId == res.eqList[i].eqType) {
                  let iconWidth = Number(response.rows[j].iconWidth);
                  let iconHeight = Number(response.rows[j].iconHeight);
                  res.eqList[i].iconWidth = iconWidth;
                  res.eqList[i].iconHeight = iconHeight;
                  break;
                }
              }
            }
            this.selectedIconList = res.eqList; //这是最终需要挂载到页面上的值
            that.getRealTimeData();
          });
        } else {
          //不存在
          that.selectedIconList = [];
        }
      });
    },
    getRealTimeData() {
      getDeviceData({
        tunnelId: this.eventForm.tunnelId,
      }).then((response) => {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          if (deviceData) {
            // let type = deviceData.eqType;

            // 需要换光标的
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  // 5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 31, 32, 33, 35,
                  5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35, 22, 40, 39, 48, 45
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
                        "M";
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
              }
            }
          }
        }
      });
    },

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[0];
      for (let i = 0; i < this.laneUrlList.length; i++) {
        if (this.laneUrlList[i].num == num) {
          lane = this.laneUrlList[i];
          break;
        }
      }
      return lane;
    },
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        // isControl: 1,
      };
      await listEqTypeState(queryParams).then((response) => {
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    getEqUrl(list) {
      // let that = this;
      // that.eqTypeStateList = [];
      this.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let img = await that.picture(list[i].iFileList[j].url);
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        this.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
      }
    },
    getExecuteResult(num) {
      if (num == "1") {
        return "执行成功";
      } else {
        return "执行失败";
      }
    },
  },
};
</script>
<style scoped lang="scss">
.dispatchAss {
  .title {
    width: 100%;
    height: 40px;
    border-bottom: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
    color: white;
    line-height: 40px;
    padding-left: 16px;
    font-size: 16px;
    margin: 0;
    text-align: left;
  }
  .el-row {
    height: 100%;
    .el-col {
      height: 100%;
    }
  }
}
.dispatchLeft,
.dispatchRight {
  height: 100%;
}
.dispatchLeft {
  > div {
    width: 100%;
    // background: #012e51;
  }
  .video {
    height: calc(36% - 20px);
    margin-top: 0px !important;
    border-radius: 0px !important;
    .videoBox1 {
      width: 100%;
      height: calc(100% - 40px);
      word-wrap: break-word;
      word-break: normal;
      .videoContent {
        width: 50%;
        height: 48%;
        text-align: center;
        display: inline-block;
        justify-content: center;
        margin-top: 4px;
        .video {
          height: 90px;
          width: 70%;
          object-fit: fill;
          margin:0 auto;
        }
        .videoListTitle {
          width: 75px;
          height: 18px;
          border-radius: 4px;
          font-size: 13px;
          margin-left: 35%;
          color: #fff;
        }
      }
      .videoContent:nth-of-type(1) .videoListTitle {
        background: #00c8ff;
      }
      .videoContent:nth-of-type(2) .videoListTitle {
        background: #59b94e;
      }
      .videoContent:nth-of-type(3) .videoListTitle {
        background: #c4a23c;
      }
      .videoContent:nth-of-type(4) .videoListTitle {
        background: #c4a23c;
      }
    }
  }
  .evtMessage {
    height: 24%;
    margin-top: 10px;
    .evtMessBox {
      display: flex;
      width: 100%;
      height: calc(100% - 40px);
      .evtMessRight {
        border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        width: 50%;
        height: 100%;
        .evtMessVideo {
          height: 54%;
          width: auto;
          display: flex;
          justify-content: center;
          margin-top: 10px;
          > video {
            height: 100%;
            width: 90%;
            display: block;
            object-fit: fill;
          }
        }
        .evtMessImg {
          width: 90%;
          height: 38px;
          display: flex;
          justify-content: space-between;
          margin: 5px auto;
          > .el-image {
            width: 24%;
          }
        }
        .evtMessTarget {
          font-size: 14px;
          display: flex;
          > div:nth-of-type(1) {
            color: #008aff;
            width: 80px;
            margin-left: 14px;
          }
          > div:nth-of-type(2) {
            // color: #fff;
            height: 40px;
            overflow: auto;
          }
        }
      }
      .evtMessLeft {
        width: 50%;
        height: 100%;
        > div {
          display: flex;
          font-size: 14px;
          margin-top: 13px;

          > div:nth-of-type(1) {
            color: #008aff;
            width: 90px;
            margin-left: 20px;
          }
          > div:nth-of-type(2) {
            color: #fff;
          }
        }
      }
    }
  }
  .plan {
    height: 40%;
    margin-top: 10px;
    .planBox1 {
      width: 100%;
      height: calc(100% - 40px);
      display: flex;
      .planLeft {
        width: 50%;
        height: 100%;

        .oneWayTraffic,
        .twoWayTraffic {
          width: 100%;
          height: 50%;
          font-size: 14px;
          // color: #fff;
          padding-left: 20px;
          padding-top: 5px;
          > div:nth-of-type(2) {
            div {
              margin-right: 10px;
            }
          }
          > div:nth-of-type(3) {
            float: right;
            width: 160px;
            display: flex;
            justify-content: space-between;
            height: 22px;
            color: white;
            margin-top: 20px;
            margin-right: 20px;
            > div {
              text-align: center;
              padding: 0 15px;
              border-radius: 15px;
              line-height: 22px;
              cursor: pointer;
            }
            > div:nth-of-type(1) {
              background: #d8d8d8
                linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
            }
            > div:nth-of-type(2) {
              background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
            }
          }
        }
        .twoWayTraffic {
          border-top: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        }
      }
      .planRight {
        width: 50%;
        height: 100%;
        // border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        font-size: 14px;
        // color: #fff;
        padding: 5px 20px 0px;
        > div:nth-of-type(2) {
          display: flex;
          justify-content: space-between;
          width: 100%;
          margin: 6px 0;
          > div {
            color: #008aff;
          }
          > div:nth-of-type(2n) {
            color: #fff;
          }
        }
        > div:nth-of-type(4) {
          float: right;
          width: 160px;
          display: flex;
          justify-content: space-between;
          height: 22px;
          color: white;
          margin-top: 20px;
          > div {
            text-align: center;
            padding: 0 15px;
            border-radius: 15px;
            line-height: 22px;
          }
          > div:nth-of-type(1) {
            background: #d8d8d8
              linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
          }
          > div:nth-of-type(2) {
            background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
          }
        }
      }
      .el-radio--medium.is-bordered {
        padding: 0px 10px;
        border-radius: 4px;
        height: 30px;
        width: 106px;
        margin-top: 4px;
        line-height: 27px;
        margin-right: 0px;
        // background: linear-gradient(180deg, #002847 0%, #00325e 100%);
        border-radius: 2px;
        // border: 1px solid #005d89;
      }
      // .el-radio {
      //   color: white;
      // }
    }
  }
}
.dispatchRight {
  margin-left: 10px;
  .workBenchBox {
    width: 100%;
    height: 59%;
    display: flex;
    .tunnelBox1 {
      width: 95%;
      height: 100%;
      .tunnelMap {
        height: 100%;
        width: 100%;
        position: relative;
        margin-bottom: 10px;
        // padding-bottom: 10px;
        .back-img {
          // width: 100% !important;
          height: 502px !important;
          position: absolute;
        }
        .wrapper {
          height: 100%;
          width: 100%;
          position: absolute;
          z-index: 3;
          top: 0px;
          left: 0px;
          .icon-box {
            position: absolute;
            display: flex;
            flex-direction: column;
            // align-items: center;
            width: 30px !important;
          }
        }
        .accBox {
          width: 100%;
          height: 100%;
          position: relative;
          .accTop {
            position: absolute;
            width: calc(100% - 200px);
            height: 160px;
            top: 60px;
            left: 100px;
          }
          .accBottom {
            position: absolute;
            width: calc(100% - 200px);
            height: 160px;
            top: 270px;
            left: 100px;
          }
          .accPoint {
            border-radius: 50px;
            position: absolute;
            // border: solid 1px red;
            display: flex;
            justify-content: center;
            align-items: center;
          }
        }
      }
    }
    .tunnelBox2 {
      width: 5%;
      position: relative;
      > div:nth-of-type(1) {
        position: absolute;
        top: 0;
        left: 20px;
        width: 48px;
        height: 48px;
      }
      > div:nth-of-type(2) {
        position: absolute;
        bottom: 0;
        left: 20px;
        width: 48px;
        height: 150px;
      }
    }
    .tunnelBox3 {
      width: 95%;
      height: 100%;
      .map3D {
        width: 100%;
        height: 100%;
      }
    }
  }
  .rightBottom {
    width: 100%;
    height: calc(41% - 10px);
    margin-top: 10px;
    display: flex;
    .evtManagement {
      width: calc(67% - 10px);
      height: 100%;
      display: flex;
      // background: #012e51;
      .IncHand {
        width: 60%;
        height: 100%;
        .incHandBox {
          height: calc(100% - 40px);
          overflow: auto;
          .incHandContent {
            display: flex;
            // color: white;
            font-size: 12px;
            padding: 10px;
            .classification {
              .type {
                width: 50px;
                height: 50px;
                // background: rgba($color: #084e84, $alpha: 0.6);
                // border: 1px solid rgba($color: #39adff, $alpha: 0.6);
                text-align: center;
              }
              .yijian {
                color: white;
                width: 50px;
                background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
                border: 1px solid #39adff;
                // padding: 10px;
                text-align: center;
              }
            }

            .heng1 {
              width: 20px;
              height: 1px;
              border-top: solid 1px #39adff;
            }
            .shu {
              width: 20px;
              border-left: solid 1px #39adff;
              border-bottom: solid 1px #39adff;
              margin-top: 20px;
            }
            .contentList {
              display: block;
              margin-top: 4px;
              line-height: 40px;
              padding: 0 20px;
              // background: rgba($color: #084e84, $alpha: 0.6);
              border-radius: 3px;
              width: 400px;
              display: flex;
              justify-content: space-between;
              align-items: center;
              img {
                width: 18px;
                height: 18px;
              }
            }
            .contentList:nth-of-type(1) {
              margin-top: 0;
            }
          }
        }
      }
      .DisRecords {
        width: 40%;
        height: 100%;
        // border-left: solid 1px #E0E7ED;
        overflow: hidden;
      }
    }
    .implement1 {
      width: 33%;
      height: 100%;
      margin-left: 10px;
      .phone {
        width: 100%;
        height: calc(50% - 5px);
        // background: #012e51;
        .phoneTable {
          background: transparent !important;
          padding: 10px;

          img {
            margin-left: 10px;
          }
        }
      }
      .eqRecord {
        width: 100%;
        height: 100%;
        // margin-top: 10px;
        // background: #012e51;
        .eqRecordBox {
          height: calc(100% - 40px);
          overflow: auto;
          .implementContent {
            width: 100%;
            height: 50px;
            padding-right: 10px;
            .implementIcon {
              width: 10px;
              height: 84%;
              text-align: center;
              padding-top: 11px;
              margin-left: 3%;
              margin-right: 3%;
            }
            .contentBox {
              display: inline-block;
              height: 100%;
              width: 90%;
              font-size: 12px;
              // color:#fff;
              .row1 {
                display: flex;
                justify-content: space-between;
                align-items: center;
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
                align-items: center;
                height: 20px;
                // color: #333;
              }
            }
          }
        }
      }
    }
  }
}
// 修改封装样式
::v-deep .el-timeline {
  padding: 10px;
  .el-timeline-item {
    padding-bottom: 0px !important;
    // 竖线
    .el-timeline-item__tail {
      transform: translateY(40px);
      border-left: 2px dashed #97a8ae;
    }
    .el-timeline-item__node--normal {
      top: 32px !important;
      background: #3bd1fe;
      border: 1px solid #ffffff;
    }
    .el-timeline-item__wrapper {
      margin-right: 8px;
      padding-left: 20px;
      .el-timeline-item__content {
        padding: 10px;
        // color: white;
        // background: rgba($color: #084e84, $alpha: 0.6);
        font-size: 12px;
        > div:nth-of-type(1) {
          text-align: left;
          width: 100%;
        }
        > div:nth-of-type(2) {
          text-align: right;
          width: 100%;
        }
      }
    }
  }
}
::v-deep .el-table .el-table__cell {
  height: 30px !important;
}
::v-deep ::-webkit-scrollbar {
  width: 0px;
}
// ::v-deep .phoneTable tr {
//   background: #12578f !important;
// }
// ::v-deep .phoneTable tr:nth-of-type(2n) {
//   background: #165484 !important;
// }
::v-deep .el-radio__inner::after {
  width: 12px;
  height: 12px;
  background: #008aff;
  // border: solid 2px #012e51;
}
::v-deep .el-dialog__header {
  padding: 0 !important;
}
.workbench-dialog {
  .el-dialog__header {
    padding: 0 !important;
  }
}
</style>
<style lang="scss">
.IssuedDialog {
  .el-dialog {
    margin-top: 28vh !important;
  }
  .el-dialog__body {
    padding-top: 20px !important;
  }
  .IssuedButton1 {
    width: 60px;
    height: 20px;
    border-radius: 15px;
    background: #d8d8d8 linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
    text-align: center;
    line-height: 20px;
    color: #fff;
    margin: 10px;
    cursor: pointer;
  }
  .IssuedButton2 {
    width: 60px;
    height: 20px;
    border-radius: 15px;
    background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
    text-align: center;
    line-height: 20px;
    color: #fff;
    margin: 10px;
    cursor: pointer;
  }
}
</style>
