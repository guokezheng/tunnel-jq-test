<template>
  <div class="footer">
    <div class="fourBox">
      <div class="footMiniBox" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="carIcon"
              style="width: 0.9vw; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>车辆监测</p>
            <p>Vehicle detection</p>
          </div>
        </div>
        <div id="vehicle"></div>
      </div>
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <!-- <div class="footTriangle"></div> -->
          <div class="footTitleCont">
            <img
              :src="energyIcon"
              style="width: 1vw; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>能耗监测</p>
            <p>Energy monitoring</p>
          </div>
        </div>
        <div id="energyConsumption"></div>
      </div>
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="keyVehiclesIcon"
              style="width: 0.8vw; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>洞内外速度监测</p>
            <p>Opening speed</p>
          </div>
        </div>
        <el-radio-group v-model="tabModel" class="tabButton">
          <el-radio-button label="chartJN">济南方向</el-radio-button>
          <el-radio-button label="chartWF">潍坊方向</el-radio-button>
        </el-radio-group>
        <div id="chartJN" v-show="tabModel == 'chartJN'"></div>
        <div id="chartWF" v-show="tabModel == 'chartWF'"></div>
      </div>
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="keyVehiclesIcon"
              style="width: 0.8vw; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>设备健康监测</p>
            <p>Equipment monitoring</p>
          </div>
        </div>
        <div class="deviceBox">
          <div class="allMsg">
            <div class="item1 item">
              <div>{{ devNum }}</div>
              <div>设备总数</div>
            </div>
            <div class="item2 item">
              <div>{{ faultNum }}</div>
              <div>设备故障数</div>
            </div>
            <div class="item3 item">
              <div>{{ failureRate }}%</div>
              <div>故障率</div>
            </div>
          </div>
          <div id="deviceChart"></div>
        </div>
      </div>

      <div class="footerRight footMiniBox" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 0.8vw; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>预警事件</p>
            <p>Alert event</p>
          </div>
        </div>
        <div
          v-if="trafficList.length == 0"
          style="
            width: 100%;
            text-align: center;
            font-size: 14px;
            margin-top: 80px;
          "
        >
          暂无预警事件
        </div>
        <div class="jumpBox" v-if="trafficList">
          <div class="table_body">
            <el-row
              v-for="(item, index) of trafficList"
              :key="index"
              style="cursor: pointer"
              :style="{ top: tableTop + 'px' }"
              class="table_list"
              :data-index="JSON.stringify(item)"
              :id="item.id"
            >
              <el-col :span="2">
                <div
                  style="
                    width: 30px;
                    height: 30px;
                    display: flex;
                    justify-content: right;
                    align-items: center;
                    transform: scale(0.7);
                  "
                >
                  <img :src="item.eventType.iconUrl" style="width: 100%" />
                </div>
              </el-col>
              <el-col style="display: flex" :span="4">
                <div
                  style="width: 100%"
                  :style="{
                    color:
                      item.eventType.prevControlType == '0'
                        ? 'red'
                        : item.eventType.prevControlType == '1'
                        ? '#0B92FE'
                        : 'yellow',
                  }"
                >
                  {{ item.eventType.simplifyName }}
                </div>
              </el-col>
              <el-col :span="18" style="display: flex">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="
                    tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'
                      ? item.eventTitle
                      : item.frameEventTitle
                  "
                  placement="top"
                >
                  <div
                    style="
                      width: 145px;
                      overflow: hidden;
                      white-space: nowrap;
                      text-overflow: ellipsis;
                      z-index: 10;
                    "
                    @click="jumpYingJi(item.id)"
                  >
                    <span v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'">{{
                      item.eventTitle
                    }}</span>
                    <span v-else>{{ item.frameEventTitle }}</span>
                  </div>
                </el-tooltip>

                <div style="font-size: 12px; float: right; margin-right: 10px">
                  {{ item.startTime }}
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- <el-table
            class="tableBox"
            :style="{
              animationDuration:
                '15s'
               
            }"
            height="165"
            :data="trafficList"
            @row-click="jumpYingJi"
          >
            <el-table-column width="30">
              <template slot-scope="scope">
                <img :src="scope.row.eventType.iconUrl" style="width: 20px" />
              </template>
            </el-table-column>
            <el-table-column width="60">
              <template slot-scope="scope">
                <span
                  :style="{
                    color:
                      scope.row.eventType.prevControlType == '0'
                        ? 'red'
                        : scope.row.eventType.prevControlType == '1'
                        ? '#0B92FE'
                        : 'yellow',
                  }"
                  >{{ scope.row.eventType.simplifyName }}</span
                >
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip width="125">
              <template slot-scope="scope">
                <span v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'">{{
                  scope.row.eventTitle
                }}</span>
                <span v-else>{{ scope.row.frameEventTitle }}</span>
              </template>
            </el-table-column>
            <el-table-column width="80">
              <template slot-scope="scope">
                {{ scope.row.startTime }}
              </template>
            </el-table-column>
          </el-table> -->
        </div>
        <!-- <div v-if="trafficList" @click="jumpYingJi" class="jumpBox">
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="listContent"
            :data="trafficList"
          >
            <ul style="padding-left: 0">
              <li
                v-for="(item, index) of trafficList"
                :key="index"
                style="cursor: pointer; list-style: none"
              >
                <el-row
                  class="listRow"
                  :data-index="JSON.stringify(item)"
                  :id="item.id"
                >
                  <el-col :span="2">
                    <div
                      style="
                        width: 30px;
                        height: 30px;
                        display: flex;
                        justify-content: right;
                        align-items: center;
                        transform: scale(0.7);
                      "
                    >
                      <img :src="item.eventType.iconUrl" style="width: 100%" />
                    </div>
                  </el-col>
                  <el-col style="display: flex" :span="4">
                    <div
                      style="width: 100%"
                      :style="{
                        color:
                          item.eventType.prevControlType == '0'
                            ? 'red'
                            : item.eventType.prevControlType == '1'
                            ? '#0B92FE'
                            : 'yellow',
                      }"
                    >
                      {{ item.eventType.simplifyName }}
                    </div>
                  </el-col>
                  <el-col :span="18" style="display: flex">
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="
                        tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'
                          ? item.eventTitle
                          : item.frameEventTitle
                      "
                      placement="top"
                    >
                      <div
                        style="
                          width: 145px;
                          overflow: hidden;
                          white-space: nowrap;
                          text-overflow: ellipsis;
                          z-index: 10;
                        "
                      >
                        <span v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'">{{
                          item.eventTitle
                        }}</span>
                        <span v-else>{{ item.frameEventTitle }}</span>
                      </div>
                    </el-tooltip>

                    <div
                      style="font-size: 12px; float: right; margin-right: 10px"
                    >
                      {{ item.startTime }}
                    </div>
                  </el-col>
                </el-row>
                <div class="lineBT">
                  <div></div>
                  <div></div>
                  <div></div>
                </div>
              </li>
            </ul>
          </vue-seamless-scroll>
        </div> -->
      </div>
      <div
        class="footMiniBox"
        v-show="footChangeRadio == '视频'"
        :style="{
          width:
            tunnelId == 'JQ-JiNan-WenZuBei-MJY' ? '19.5%' : '24.5% !important',
        }"
      >
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>{{ videoTitle1 }}</p>
            <p>real-time video</p>
          </div>
        </div>
        <videoPlayer
          v-if="liveUrl1 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
          :rtsp="liveUrl1"
          :open="cameraPlayer1"
        ></videoPlayer>
        <video
          v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic1"
          id="h5sVideo2"
          class="h5video_"
          controls
          muted
          loop
          disablePictureInPicture="true"
          style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
        ></video>
        <div class="noPicBox" v-show="videoNoPic1">
          <img src="../../../assets/image/noVideo.png" />
        </div>
      </div>
      <div
        class="footMiniBox footerRight"
        v-show="footChangeRadio == '视频'"
        :style="{
          width:
            tunnelId == 'JQ-JiNan-WenZuBei-MJY' ? '19.5%' : '24.5% !important',
        }"
      >
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>{{ videoTitle2 }}</p>
            <p>real-time video</p>
          </div>
        </div>
        <videoPlayer
          v-if="liveUrl2 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
          :rtsp="liveUrl2"
          :open="cameraPlayer2"
        ></videoPlayer>
        <video
          v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic1"
          id="h5sVideo3"
          class="h5video_"
          controls
          muted
          loop
          autoplay
          webkit-playsinline
          playsinline
          disablePictureInPicture="true"
          controlslist="nodownload noplaybackrate noremoteplayback"
          style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
        ></video>
        <div class="noPicBox" v-show="videoNoPic1">
          <img src="../../../assets/image/noVideo.png" />
        </div>
      </div>
      <div
        class="footMiniBox footerRight"
        v-show="
          footChangeRadio == '视频' && tunnelId == 'JQ-JiNan-WenZuBei-MJY'
        "
        :style="{
          width:
            tunnelId == 'JQ-JiNan-WenZuBei-MJY' ? '19.5%' : '24.5% !important',
        }"
      >
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>{{ videoTitle5 }}</p>
            <p>inspection robot</p>
          </div>
        </div>
        <videoPlayer
          v-if="liveUrl5"
          :rtsp="liveUrl5"
          :open="cameraPlayer5"
        ></videoPlayer>
        <!-- <videoRtmp v-if="liveUrl5" ref="playerObj"></videoRtmp> -->
        <!-- <videoM3u8 v-if="liveUrl5" ref="playerObj"></videoM3u8> -->
        <div class="noPicBox" v-else>
          <img src="../../../assets/image/noVideo.png" />
        </div>
      </div>
      <div
        class="footMiniBox footerRight"
        v-show="footChangeRadio == '视频'"
        :style="{
          width:
            tunnelId == 'JQ-JiNan-WenZuBei-MJY' ? '19.5%' : '24.5% !important',
        }"
      >
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>{{ videoTitle3 }}</p>
            <p>real-time video</p>
          </div>
        </div>
        <videoPlayer
          v-if="liveUrl3 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
          :rtsp="liveUrl3"
          :open="cameraPlayer3"
        ></videoPlayer>
        <video
          v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic2"
          id="h5sVideo4"
          class="h5video_"
          controls
          muted
          loop
          autoplay
          webkit-playsinline
          playsinline
          disablePictureInPicture="true"
          controlslist="nodownload noplaybackrate noremoteplayback"
          style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
        ></video>
        <div class="noPicBox" v-show="videoNoPic2">
          <img src="../../../assets/image/noVideo.png" />
        </div>
      </div>
      <div
        class="footMiniBox footerRight"
        v-show="footChangeRadio == '视频'"
        :style="{
          width:
            tunnelId == 'JQ-JiNan-WenZuBei-MJY' ? '19.5%' : '24.5% !important',
        }"
      >
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-light'"
            />
            <p>{{ videoTitle4 }}</p>
            <p>real-time video</p>
          </div>
        </div>
        <videoPlayer
          v-if="liveUrl4 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
          :rtsp="liveUrl4"
          :open="cameraPlayer4"
        ></videoPlayer>
        <video
          v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic2"
          id="h5sVideo5"
          class="h5video_"
          loop
          controls
          muted
          autoplay
          webkit-playsinline
          playsinline
          disablePictureInPicture="true"
          controlslist="nodownload noplaybackrate noremoteplayback"
          style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
        ></video>
        <div class="noPicBox" v-show="videoNoPic2">
          <img src="../../../assets/image/noVideo.png" />
        </div>
      </div>
    </div>

    <div class="footChangeButton">
      <el-radio-group v-model="footChangeRadio" @change="videoRadioChange">
        <el-radio-button label="图表"></el-radio-button>
        <el-radio-button label="视频"></el-radio-button>
      </el-radio-group>
    </div>
  </div>
</template>
<script>
import bus from "@/utils/bus";
import { mapState } from "vuex";
import * as echarts from "echarts";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import videoRtmp from "@/views/event/vedioRecord/videoRtmp";
import videoM3u8 from "@/views/event/vedioRecord/videoM3u8";

import { displayH5sVideoAll } from "@/api/icyH5stream";
import { videoStreaming, getDeviceById } from "@/api/equipment/eqlist/api";
import { getEntranceExitVideo, getVideoUrl } from "@/api/eventDialog/api.js";
import {
  vehicleMonitoringInRecent24Hours,
  getHoursTrafficVolume,
  getStatisticalDevice,
  specialVehicleMonitoringInRecent24Hours,
  getNoDeceleration,
} from "@/api/workbench/config.js";
import { energyConsumptionDetection } from "@/api/equipment/tunnel/api.js";
import { getWarnEvent } from "@/api/event/event";

export default {
  components: {
    videoPlayer,
    videoRtmp,
    videoM3u8
  },
  data() {
    return {
      footChangeRadio: "图表",
      carIcon: require("@/assets/icons/carIcon.png"),
      energyIcon: require("@/assets/icons/energyIcon.png"),
      keyVehiclesIcon: require("@/assets/icons/keyVehiclesIcon.png"),
      warningIcon: require("@/assets/icons/warningIcon.png"),
      trafficList: [],
      videoNoPic1: false,
      videoNoPic2: false,
      videoNoPic3: false,
      videoTitle1: "",
      videoTitle2: "",
      videoTitle3: "",
      videoTitle4: "",
      videoTitle5: "巡检机器人",
      liveUrl1: "",
      liveUrl2: "",
      liveUrl3: "",
      liveUrl4: "",
      liveUrl5: "",
      cameraPlayer1: false,
      cameraPlayer2: false,
      cameraPlayer3: false,
      cameraPlayer4: false,
      cameraPlayer5: false,
      directionList: [{}, {}],
      tunnelId: "",
      devNum: "",
      failureRate: "",
      faultNum: "",
      deviceChart: null,
      option: null,
      nameArr: [],
      myChart: null,
      tabModel: "chartJN",
      // animation: true,
      // timer: null,
      // imageTimer: null, //定时器
      tableTimer: null,
      tableTop: 0, //列表向上移动的像素
      tableList: [], //tableList是列表的数据对象
      showFlag: false,
      componentTimer: null,
      maxCanSee: 6, //maxCanSee代表可视范围内的最大完整数据条数
      tableLineHeight: 29, //tableLineHeight代表列表中一行的高度
    };
  },
  computed: {
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
    }),
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 5, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  watch: {
    // activeFactoryId(val, oldVal) {
    //   clearInterval(this.componentTimer);
    //   this.bsGetOrderProcessList();
    //   this.componentTimerFun();
    // },
    tabModel: function (newValue, oldValue) {
      this.getNoDecelerationChart();
    },
    sdEventList: {
      immediate: true,
      handler: function (event) {
        // console.log(event, "事件弹窗websockt推送");
        this.getWarnList();
      },
    },
    deep: true,
  },
  beforeDestroy() {
    clearInterval(this.componentTimer);
    clearInterval(this.tableTimer);
    // clearInterval(this.imageTimer);
    // this.imageTimer = null;
  },
  created() {
    this.getDicts("sd_strategy_direction").then((data) => {
      this.directionList = data.data;
    });
  },
  methods: {
    init(tunnelId) {
      // console.log(tunnelId, "tunnelId");
      this.tunnelId = tunnelId;
      // setTimeout(() => {
      this.getWarnList();

      // }, 2000);
      this.vehicleEcharts();
      // this.specialVehicleEcharts()
      this.getEnergyConsumption();
      this.getDeviceChart();
      this.getNoDecelerationChart();
    },
    changeActive() {
      this.footChangeRadio = "图表";
      this.cameraPlayer1 = false;
      this.cameraPlayer2 = false;
      this.cameraPlayer3 = false;
      this.cameraPlayer4 = false;
      this.cameraPlayer5 = false;
      window.addEventListener("resize", function () {
        this.deviceChart.resize();
      });
    },
    // 预警事件列表
    getWarnList() {
      // console.log("预警事件列表");
      const param = {
        eventState: "3",
      };
      getWarnEvent(param).then((response) => {
        // console.log(response.data, "预警事件");
        this.trafficList = response.data;
        // setInterval(this.srollAuto, 2000);
        clearInterval(this.componentTimer);
        this.bsGetOrderProcessList();
        this.componentTimerFun();
      });
    },
    // 预警事件点击跳转应急调度
    jumpYingJi(id) {
      // console.log(id);
      // const item = e.target.closest(".listRow");
      // if (item) {
      //   // 是否是滚动组件的某一行/列
      //   const { index } = item.dataset;
      //   let id = JSON.parse(index).id;
      setTimeout(() => {
        bus.$emit("getPicId", id);
      }, 200);
      bus.$emit("openPicDialog");
      // }
    },
    bsGetOrderProcessList() {
      clearInterval(this.tableTimer);
      this.tableTop = 0;
      if (this.trafficList.length > 0) {
        this.actionFun();
      }
    },
    actionFun() {
      if (this.trafficList.length > 6) {
        this.tableTimerFun();
      }
      // else {
      //   this.fillTableList();
      // }
      // this.showFlag = true;
    },
    // fillTableList() {
    //   var addLength = this.maxCanSee - this.trafficList.length;
    //   for (var i = 0; i < addLength; i++) {
    //     this.trafficList.push({
    //       eventType: {
    //         iconUrl: "-",
    //         prevControlType: "-",
    //         simplifyName: "-",
    //       },
    //       eventTitle: "-",
    //       frameEventTitle: "-",
    //       startTime: "-",
    //     });
    //   }
    // },
    tableTimerFun() {
      var count = 0; //每滚动一次，count加1
      if (this.trafficList.length > this.maxCanSee) {
        //tableList是列表的数据对象，maxCanSee代表可视范围内的最大完整数据条数
        this.tableTimer = setInterval(() => {
          if (count < (this.trafficList.length / 2) * this.tableLineHeight) {
            //如果还没滚动到最后一条数据，则列表向上移动以上的高度
            this.tableTop -= 1; //tableLineHeight代表列表中一行的高度
            count++; //每滚动一次，count加1
          } else {
            //如果滚动到最后一条，则恢复初始状态
            count = 0;
            this.tableTop = 0;
          }
        }, 100);
      }
    },
    componentTimerFun() {
      this.componentTimer = setInterval(() => {
        this.bsGetOrderProcessList();
      }, 3600000);
    },
    // mouseEnter(){
    //   this.animation = false
    // },
    // mouseLeave(){
    //   this.animation = true
    // },
    // scroll() {
    //   if (this.animation) {
    //     let el = document.getElementbyClassName(".el-table__body-wrapper");
    //     el.addClassName = "anim";
    //     setTimeout(() => {
    //       // console.log(this.items[0])
    //       this.trafficList.push(this.trafficList[0]); // 将数组的第一个元素添加到数组的
    //       this.trafficList.shift(); //删除数组的第一个元素
    //     }, 2000);
    //   }
    // },
    // 车辆监测数据
    vehicleEcharts() {
      const param = {
        tunnelId: this.tunnelId,
      };
      getHoursTrafficVolume(param).then((res) => {
        // console.log(res, "车辆监测数据");
        var huoArr = [];
        var keArr = [];
        var keyArr = [];
        var timeArr = [];
        for (var item of res.data.huo) {
          huoArr.push(item.count);
          timeArr.push(item.order_hour + ":00");
        }
        for (var item of res.data.ke) {
          keArr.push(item.count);
        }
        for (var item of res.data.key) {
          keyArr.push(item.count);
        }
        this.initeChartsEnd(huoArr, keArr, keyArr, timeArr);
      });
    },
    getDeviceChart() {
      const param = {
        tunnelId: this.tunnelId,
      };
      getStatisticalDevice(param).then((res) => {
        // console.log(res, "设备健康监测");
        this.devNum = res.data.devNum;
        this.failureRate = res.data.failureRate;
        this.faultNum = res.data.faultNum;
        this.nameArr = [];
        let faultArr = [];
        let normalArr = [];
        let allArr = [];
        for (let item of res.data.list) {
          this.nameArr.push(item.type_name);
          faultArr.push(item.faultPercentage);
          normalArr.push(item.normalPercentage);
          allArr.push(100);
        }
        this.initDeviceChart(faultArr, normalArr, allArr);
      });
    },
    // 重点车辆监测数据
    // specialVehicleEcharts() {
    //   // console.log(this.tunnelId,"this.tunnelIdthis.tunnelIdthis.tunnelId")
    //   const param = {
    //     tunnelId: this.tunnelId,
    //   };
    //   specialVehicleMonitoringInRecent24Hours(param).then((res) => {
    //     // console.log(res, "重点车辆监测数据");
    //     var specialVehicleXData = [];
    //     var specialVehicleYData = [];
    //     for (var item of res.data) {
    //       specialVehicleXData.push(item.hour);
    //       specialVehicleYData.push(item.count);
    //     }
    //     this.loadFocusCar(specialVehicleXData, specialVehicleYData);
    //   });
    // },

    // jumpLink(url) {
    //   if (url == "/15/status") {
    //     this.$modal.msgWarning("跳转页面暂未完成");
    //     return;
    //   }
    //   this.$router.push({
    //     path: url,
    //     query: {},
    //   });
    // },
    getNoDecelerationChart() {
      const param = {
        tunnelId: this.tunnelId,
      };
      // getNoDeceleration(param).then((res)=>{
      // console.log(res,"洞口不降速监测")
      // let data = {
      //   oneLane: {
      //     oneListOne: [118, 114, 105, 100],
      //     oneListTwo: [100, 103, 106, 116, 118],
      //   },
      //   twoLane: {
      //     twoListOne: [105, 100, 98, 90],
      //     twoListTwo: [86, 84, 86, 89, 98],
      //   },
      //   threeLane: {
      //     threeListOne: [76, 73, 68, 66],
      //     threeListTwo: [64, 69, 75, 82, 85],
      //   },
      // };
      // console.log(data, "洞口不降速监测");
      // let oneListOne = data.oneLane.oneListOne;
      // let oneListTwo = data.oneLane.oneListTwo;
      // let twoListOne = data.twoLane.twoListOne;
      // let twoListTwo = data.twoLane.twoListTwo;
      // let threeListOne = data.threeLane.threeListOne;
      // let threeListTwo = data.threeLane.threeListTwo;
      // let arr = [
      //   ...oneListOne,
      //   ...oneListTwo,
      //   ...twoListOne,
      //   ...twoListTwo,
      //   ...threeListOne,
      //   ...threeListTwo,
      // ];
      // let max = this.getMax(arr);
      // console.log(max, "洞口不降速监测111");
      // let oneList = [...oneListOne, ...oneListTwo];
      // let twoList = [...twoListOne, ...twoListTwo];
      // let threeList = [...threeListOne, ...threeListTwo];
      let oneList = [];
      let twoList = [];
      let threeList = [];
      if (this.tabModel == "chartJN") {
        oneList = [118, 114, 105, 100, 100, 103, 106, 116, 118];
        twoList = [105, 100, 98, 90, 86, 84, 86, 89, 98];
        threeList = [76, 73, 68, 66, 64, 69, 75, 82, 85];
      } else {
        oneList = [110, 102, 98, 95, 85, 80, 89, 92, 95];
        twoList = [103, 95, 90, 85, 82, 85, 85, 89, 93];
        threeList = [85, 82, 76, 72, 70, 76, 78, 82, 85];
      }
      // console.log(oneList, twoList, threeList, "洞口不降速监测222");
      this.initNoDecelerationChart(oneList, twoList, threeList);
      // })
    },
    // 取数组最大值
    // getMax(arr) {
    //   return Math.max.apply(Math, arr);
    // },

    // 切换图表、视频
    videoRadioChange() {
      if (this.footChangeRadio == "视频" && this.tunnelId) {
        this.getFooterVideo();
      } else {
        this.cameraPlayer1 = false;
        this.cameraPlayer2 = false;
        this.cameraPlayer3 = false;
        this.cameraPlayer4 = false;
        this.liveUrl5 = ''
        // this.$nextTick(()=>{
        //   this.$refs.playerObj.init(this.liveUrl5)
        // })
      }
    },
    getFooterVideo() {
      // 潍坊方向
      getEntranceExitVideo(this.tunnelId, this.directionList[0].dictValue).then(
        (res) => {
          if (res.data.length == 0) {
            this.videoNoPic2 = true;
          } else {
            this.videoTitle3 = res.data[0].inletName;
            this.videoTitle4 = res.data[0].outletName;
            // console.log(res, "后两个视频");
            if (this.tunnelId == "WLJD-JiNan-YanJiuYuan-FHS") {
              getDeviceById(res.data[0].inlet).then((response) => {
                displayH5sVideoAll(response.data.secureKey, "h5sVideo4", 3);
              });
              getDeviceById(res.data[0].outlet).then((response) => {
                displayH5sVideoAll(response.data.secureKey, "h5sVideo5", 4);
              });
            } else {
              videoStreaming(res.data[0].inlet).then((res) => {
                // console.log(res, "入口视频");
                if (res.code == 200 && res.data) {
                  this.liveUrl3 = res.data.liveUrl;
                  this.cameraPlayer3 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                  this.videoNoPic1 = true;
                }
              });
              videoStreaming(res.data[0].outlet).then((res) => {
                if (res.code == 200 && res.data) {
                  this.liveUrl4 = res.data.liveUrl;
                  this.cameraPlayer4 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                  this.videoNoPic1 = true;
                }
              });
            }
          }
        }
      );
      // 济南方向
      getEntranceExitVideo(this.tunnelId, this.directionList[1].dictValue).then(
        (res) => {
          // console.log(res, "前两个视频");
          if (res.data.length == 0) {
            this.videoNoPic1 = true;
          } else {
            this.videoTitle1 = res.data[0].inletName;
            this.videoTitle2 = res.data[0].outletName;
            if (this.tunnelId == "WLJD-JiNan-YanJiuYuan-FHS") {
              getDeviceById(res.data[0].inlet).then((response) => {
                displayH5sVideoAll(response.data.secureKey, "h5sVideo2", 1);
              });
              getDeviceById(res.data[0].outlet).then((response) => {
                displayH5sVideoAll(response.data.secureKey, "h5sVideo3", 2);
              });
            } else {
              videoStreaming(res.data[0].inlet).then((res) => {
                // console.log(res, "入口视频");
                if (res.code == 200 && res.data) {
                  this.liveUrl1 = res.data.liveUrl;
                  this.cameraPlayer1 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                  this.videoNoPic2 = true;
                }
              });
              videoStreaming(res.data[0].outlet).then((res) => {
                if (res.code == 200 && res.data) {
                  this.liveUrl2 = res.data.liveUrl;
                  this.cameraPlayer2 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                  this.videoNoPic2 = true;
                }
              });
            }
          }
        }
      );
     if(this.tunnelId == "JQ-JiNan-WenZuBei-MJY"){
      getVideoUrl()
        .then((res) => {
          console.log(res, "机器人视频");
          this.liveUrl5 = res.data.infrared
          // this.liveUrl5 = 'rtmp://192.168.50.123:1231/live/2'
          // this.liveUrl5 =  'http://10.7.182.85:40080/hls2FAF2DE3/9_10.7.182.85.m3u8'
          // this.$nextTick(()=>{
          //   this.$refs.playerObj.init(this.liveUrl5)
          // })
        })
        .catch(() => {
        });
     }
    },
    initNoDecelerationChart(oneList, twoList, threeList) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        let tab = "";

        //	此dom为echarts图标展示dom
        var chartBJS = echarts.init(document.getElementById(this.tabModel));

        const option = {
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "none",
            },
            formatter: function (param) {
              var tooltip = "";
              if (param.length > 0) {
                tooltip +=
                  param[0].name == "0" ||
                  param[0].name == "1" ||
                  param[0].name == "2" ||
                  param[0].name == "3"
                    ? "洞外" + "<br>"
                    : "洞内" + "<br>";
              }
              for (let item of param) {
                tooltip +=
                  item.marker +
                  item.seriesName +
                  " : " +
                  item.value +
                  "km/h<br>";
              }
              return tooltip;
            },
            //   提示框超出范围时调整位置
            position: function (point, params, dom, rect, size) {
              // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
              // 提示框位置
              let x = 0; // x坐标位置
              let y = 0; // y坐标位置

              // 当前鼠标位置
              let pointX = point[0];
              let pointY = point[1];

              // 外层div大小
              // var viewWidth = size.viewSize[0];
              // var viewHeight = size.viewSize[1];

              // 提示框大小
              let boxWidth = size.contentSize[0];
              let boxHeight = size.contentSize[1];

              // boxWidth > pointX 说明鼠标左边放不下提示框
              if (boxWidth > pointX) {
                x = 5;
              } else {
                // 左边放的下
                x = pointX - boxWidth;
              }

              // boxHeight > pointY 说明鼠标上边放不下提示框
              if (boxHeight > pointY) {
                y = 5;
              } else {
                // 上边放得下
                y = pointY - boxHeight;
              }

              return [x, y];
            },
          },
          legend: {
            show: true,
            data: ["一车道", "二车道", "三车道"],
            textStyle: {
              color: "#AFAFAF",
              fontSize: 10,
            },
            itemWidth: 10,
            itemHeight: 10,
            itemStyle: {},
            top: "top",
            left: "center",
            padding: [10, 15, 0, 15],
            icon: "circle",
            orient: "horizontal",
          },
          grid: {
            left: "6%",
            right: "8%",
            bottom: "5%",
            top: "25%",
            containLabel: true,
          },

          xAxis: {
            type: "category",

            // data: [0,1,2,3,4],
            axisLabel: {
              show: true,
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
              },
              formatter: function (value, index) {
                if (value == "0") {
                  return "洞外";
                } else if (value == "4") {
                  return "洞口";
                } else if (value == "8") {
                  return "洞内";
                } else {
                  return "";
                }
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              },
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: {
            name: "km/h",
            nameTextStyle: {
              color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            type: "value",
            min: 0,
            // max: max,
            minInterval: 1,
            splitArea: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
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
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
          },
          // visualMap: {
          //   type: "piecewise",
          //   show: false,
          //   dimension: 0,
          //   seriesIndex: [0,1,2],
          //   pieces: [
          //     {
          //       gt: 0,
          //       lt: 4,
          //       color: seriesIndex == 0?"red":"green", //大于0小于12为蓝色
          //     },
          //     {
          //       gt: 4,
          //       color: seriesIndex == 0?"yellow":"blue", //大于12区间为红色
          //     },
          //   ],
          // },

          series: [
            {
              type: "line",
              smooth: true,
              color: "#31CEFF",
              lineStyle: {
                normal: {
                  width: 2,
                },
              },
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              markLine: {
                silent: true,
                symbol: ["circle", "circle"],
                lineStyle: {
                  color: "#33b0ee",
                },
                animation: false, //关闭动画
                label: {
                  show: false,
                },
                data: [
                  {
                    xAxis: 4, //在x轴12格处设置一条参考线
                  },
                ],
              },
              name: "一车道",
              data: oneList,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              color: "#FAC858",
              lineStyle: {
                normal: {
                  width: 2,
                },
              },
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              markLine: {
                silent: true,
                symbol: ["circle", "circle"],
                lineStyle: {
                  color: "#AFAFAF",
                },
                animation: false, //关闭动画
                label: {
                  show: false,
                },
                data: [
                  {
                    xAxis: 4, //在x轴12格处设置一条参考线
                  },
                ],
              },
              name: "二车道",
              data: twoList,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              color: "#7BE748",
              lineStyle: {
                normal: {
                  width: 2,
                },
              },
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              markLine: {
                silent: true,
                symbol: ["circle", "circle"],
                lineStyle: {
                  color: "#AFAFAF",
                },
                animation: false, //关闭动画
                label: {
                  show: false,
                },
                data: [
                  {
                    xAxis: 4, //在x轴12格处设置一条参考线
                  },
                ],
              },
              name: "三车道",
              data: threeList,
            },
          ],
        };
        chartBJS.setOption(option);
        window.addEventListener("resize", function () {
          chartBJS.resize();
        });
      });
    },
    initeChartsEnd(huoArr, keArr, keyArr, timeArr) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        //	此dom为echarts图标展示dom
        var vehicle = echarts.init(document.getElementById("vehicle"));

        const option = {
          // color: [
          //   "rgba(84, 181, 157, 1)",
          //   "rgba(31, 149, 215, 1)",
          //   "rgba(239, 175, 76, 1)",
          //   "rgba(55, 231, 255, 1)",
          // ],
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", // 设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "shadow",
              shadowStyle: {
                fontSize: 12,
                color: "rgba(0, 11, 34, 0)",
              },
            },
            //   提示框超出范围时调整位置
            position: function (point, params, dom, rect, size) {
              // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
              // 提示框位置
              let x = 0; // x坐标位置
              let y = 0; // y坐标位置

              // 当前鼠标位置
              let pointX = point[0];
              let pointY = point[1];

              // 外层div大小
              // var viewWidth = size.viewSize[0];
              // var viewHeight = size.viewSize[1];

              // 提示框大小
              let boxWidth = size.contentSize[0];
              let boxHeight = size.contentSize[1];

              // boxWidth > pointX 说明鼠标左边放不下提示框
              if (boxWidth > pointX) {
                x = 5;
              } else {
                // 左边放的下
                x = pointX - boxWidth;
              }

              // boxHeight > pointY 说明鼠标上边放不下提示框
              if (boxHeight > pointY) {
                y = 5;
              } else {
                // 上边放得下
                y = pointY - boxHeight;
              }

              return [x, y];
            },
          },
          legend: {
            show: true,
            data: ["客车", "货车", "重点车辆"],
            textStyle: {
              color: "#AFAFAF",
              fontSize: 10,
            },
            itemWidth: 10,
            itemHeight: 10,
            itemStyle: {},
            top: "top",
            left: "center",
            padding: [6, 15, 0, 15],
            icon: "circle",
            orient: "horizontal",
          },
          grid: {
            left: "6%",
            right: "8%",
            bottom: "2%",
            top: "25%",
            containLabel: true,
          },

          xAxis: {
            name: "时",
            nameTextStyle: {
              color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            type: "category",
            data: timeArr,
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              },
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: {
            name: "辆",
            nameTextStyle: {
              color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            type: "value",
            min: 0,
            minInterval: 1,
            splitArea: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
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
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
          },

          series: [
            {
              type: "line",
              color: "#00c8ff",
              symbol: "none",
              smooth: true,
              stack: "Total",
              areaStyle: {},
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              emphasis: {
                focus: "series",
              },
              //渐变色
              areaStyle: {
                normal: {
                  //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#33b0ee",
                    },
                    {
                      offset: 1,
                      color: "rgba(51,176,238,0.3)",
                    },
                  ]),
                },
              },
              name: "客车",
              data: keArr,
            },
            ,
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              color: "#FAC858",
              lineStyle: {
                width: 1,
              },
              // stack: "Total",
              // areaStyle: {},
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              // emphasis: {
              //   focus: "series",
              // },
              name: "货车",
              data: huoArr,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              color: "#7BE748",
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              name: "重点车辆",
              data: keyArr,
            },
          ],
        };
        vehicle.setOption(option);
        window.addEventListener("resize", function () {
          vehicle.resize();
        });
      });
    },
    getEnergyConsumption() {
      energyConsumptionDetection(this.tunnelId).then((res) => {
        // console.log(res, "能耗监测");
        if (!res.data.year && !res.data.month && !res.data.day) {
          return;
        }
        let xDataN = [];
        let xDataY = [];
        let xDataR = [];

        let xData = [];
        let yData = [];
        yData.push({
          name: "年",
          type: "line",
          color: "#59c5f9",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          // 渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#59c5f9",
                },
                {
                  offset: 1,
                  color: "rgba(89,197,249,0.3)",
                },
              ]),
            },
          },
          data: res.data.year.map((item) => item.value),
        });
        yData.push({
          name: "月",
          type: "line",
          color: "#db72a7",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          stack: "Total",
          emphasis: {
            focus: "series",
          },
          //渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#db72a7",
                },
                {
                  offset: 1,
                  color: "rgba(219,114,167,0.3)",
                },
              ]),
            },
          },
          data: res.data.month.map((item) => item.value),
        });
        yData.push({
          name: "日",
          type: "line",
          color: "#FDB400",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          // 渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#FDB400",
                },
                {
                  offset: 1,
                  color: "rgba(253,180,0,0.3)",
                },
              ]),
            },
          },
          data: res.data.day.map((item) => item.value),
        });
        for (let item of res.data.year) {
          xData.push(item.rt);
          xDataN.push(item.rt);

          // yDataN.push(item.value)
        }
        for (let item of res.data.month) {
          xDataY.push(item.rt);
          // yDataY.push(item.value)
        }
        for (let item of res.data.day) {
          xDataR.push(item.rt);
          // yDataR.push(item.value)
        }
        // let xData = []
        // console.log(xData,yData,xDataN,xDataY,xDataR,"能耗年月日")
        this.$nextTick(() => {
          this.initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR);
        });
      });
    },
    initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR) {
      var energyConsumption = echarts.init(
        document.getElementById("energyConsumption")
      );

      var option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "rgba(1, 29, 63, .8)", // 设置背景颜色
          textStyle: {
            color: "#fff",
            fontSize: 12,
          },
          borderColor: "rgba(1, 29, 63,.8)",
          axisPointer: {
            type: "shadow",
            shadowStyle: {
              fontSize: 12,
              color: "rgba(0, 11, 34, 0)",
            },
          },
        },
        legend: {
          show: true,
          icon: "circle",
          itemWidth: 10,
          itemHeight: 10,
          selectedMode: "single", // 单选
          selected: {
            年: true,
            月: false,
            日: false,
          },
          x: "center",
          data: ["年", "月", "日"],
          textStyle: {
            //图例文字的样式
            color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
            fontSize: 10,
          },
        },
        calculable: true,
        grid: {
          top: "24%",
          bottom: "13%",
          left: "10%",
          right: "10%",
        },
        xAxis: [
          {
            // name: "小时",
            nameTextStyle: {
              fontFamily: "PingFang",
            },
            type: "category",
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            boundaryGap: false,
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
                fontFamily: "PingFang",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "kwh",
            nameTextStyle: {
              color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            type: "value",
            minInterval: 1,
            // min: 0,
            // max: 200,
            axisTick: {
              show: false,
            },
            // max: 200,
            // min: 0,
            splitNumber: 5,
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["#4E6B83"],
                width: 1,
                type: "dashed",
              },
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
              },
            },
          },
        ],
        series: yData,
      };

      energyConsumption.on("legendselectchanged", (obj) => {
        var options = energyConsumption.getOption();
        //这里是选择切换什么样的x轴，那么他会进行对Y值的切换
        if (obj.name == "年") {
          options.xAxis[0].data = xDataN;
        } else if (obj.name == "月") {
          options.xAxis[0].data = xDataY;
        } else if (obj.name == "日") {
          options.xAxis[0].data = xDataR;
        }

        energyConsumption.setOption(options, true);
      });

      // }
      energyConsumption.setOption(option);
      window.addEventListener("resize", function () {
        energyConsumption.resize();
      });
    },
    initDeviceChart(faultArr, normalArr, allArr) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      newPromise.then(() => {
        this.deviceChart = echarts.init(document.getElementById("deviceChart"));
        this.option = {
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "none",
            },
            formatter: function (param) {
              var tooltip = param[0].name + "<br>";
              if (param.length > 1) {
                tooltip +=
                  param[0].marker +
                  param[0].seriesName +
                  " : " +
                  param[0].value +
                  "%<br>";
              }

              if (param.length > 2) {
                tooltip +=
                  param[1].marker +
                  param[1].seriesName +
                  " : " +
                  param[1].value +
                  "%";
              }

              return tooltip;
            },
          },
          dataZoom: [
            {
              moveOnMouseMove: true,
              type: "slider",
              show: false,
              xAxisIndex: [0],
              startValue: 0, // 数据窗口范围的起始数值
              endValue: 8, // 数据窗口范围的结束数值（一页显示多少条数据）
              minValueSpan: 0,
              maxValueSpan: 8,
            },
            {
              type: "inside",
              xAxisIndex: 0,
              zoomOnMouseWheel: false, //滚轮是否触发缩放
              moveOnMouseMove: true, //鼠标滚轮触发滚动
              moveOnMouseWheel: true,
            },
          ],
          legend: {
            icon: "circle",
            orient: "horizontal",
            itemGap: 40,
            itemWidth: 10,
            itemHeight: 10,
            textStyle: {
              fontSize: 10,
              color: "#AFAFAF",
              fontWeight: 400,
              padding: [4, 0, 0, 0],
            },
          },
          // color: ["#F57474", "#ccc016", "#1089E7", "#7bd42b", "#8B78F6"],
          grid: {
            top: "22%",
            left: "6%",
            right: "6%",
            bottom: "8%",
            containLabel: true,
          },
          xAxis: {
            type: "category",
            data: this.nameArr,
            axisTick: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "单位：%",
            // 标题名称颜色
            nameTextStyle: {
              color: "#AFAFAF",
              fontSize: 10,
              fontWeight: 800,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-light" ? "#AFAFAF" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            splitLine: {
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
          },
          series: [
            {
              name: "故障率",
              type: "bar",
              stack: "total",
              label: {
                show: false,
                color: "#FFFFFF",
                formatter: function (e) {
                  // return e.value ? e.value : "";
                  // return e.value ? e.seriesName : "";
                },
              },
              itemStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      // color: '#00FFE3',//渐变1
                      color: "rgba(247,245,26,1)", //渐变1
                    },
                    {
                      offset: 1,
                      // color: '#4693EC',//渐变2
                      color: "rgba(247,178,26,1)", //渐变2
                    },
                  ]),
                },
              },
              emphasis: {
                disabled: true,
                focus: "none",
              },
              barWidth: 14,
              data: faultArr,
            },
            {
              name: "正常率",
              type: "bar",
              stack: "total",
              label: {
                show: false,
                color: "#FFFFFF",
                formatter: "{a}",
              },
              itemStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      // color: '#00FFE3',//渐变1
                      color: "rgba(140,228,255,1)", //渐变1
                    },
                    {
                      offset: 1,
                      // color: '#4693EC',//渐变2
                      color: "rgba(41,204,255,1)", //渐变2
                    },
                  ]),
                },
              },
              emphasis: {
                disabled: true,
                focus: "none",
              },
              barWidth: 14,
              data: normalArr,
            },
            {
              type: "pictorialBar",
              barWidth: 24,
              itemStyle: {
                normal: {
                  color: "#010913", //数据的间隔颜色
                },
              },
              symbolRepeat: "fixed",
              symbolMargin: 2,
              symbol: "rect",
              symbolSize: [14, 4],
              symbolPosition: "end",
              symbolOffset: [0, 0],
              data: allArr,
              z: 1,
              zlevel: 1,
            },
          ],
        };
        if (this.nameArr.length > 0) {
          window.clearInterval(this.myChart);
          this.deviceChart.setOption(this.option);
          this.deviceChart.on("mouseover", () => {
            this.stop();
          });
          this.deviceChart.on("mouseout", () => {
            this.goMove();
          });
          this.autoMove();
        }
        this.deviceChart.setOption(this.option);
        window.addEventListener("resize", function () {
          this.deviceChart.resize();
        });
      });
    },
    autoMove() {
      this.myChart = setInterval(() => {
        if (this.option.dataZoom[0].endValue == this.nameArr.length) {
          this.option.dataZoom[0].endValue = 8;
          this.option.dataZoom[0].startValue = 0;
        } else {
          this.option.dataZoom[0].endValue =
            this.option.dataZoom[0].endValue + 1;
          this.option.dataZoom[0].startValue =
            this.option.dataZoom[0].startValue + 1;
        }

        this.deviceChart.setOption(this.option);
      }, 4000);
    },
    stop() {
      window.clearInterval(this.myChart);
    },
    goMove() {
      this.autoMove();
    },
    // loadFocusCar(specialVehicleXData, specialVehicleYData) {
    //   let newPromise = new Promise((resolve) => {
    //     resolve();
    //   });
    //   newPromise.then(() => {
    //     var focusCar = echarts.init(document.getElementById("focusCar"));
    //     var option = {
    //       tooltip: {
    //         trigger: "axis",
    //       },
    //       toolbox: {
    //         show: true,
    //         feature: {
    //           // magicType: { show: true, type: ['stack', 'tiled'] },
    //           // saveAsImage: { show: true }
    //         },
    //       },
    //       grid: {
    //         top: "20%",
    //         bottom: "18%",
    //         left: "14%",
    //         right: "14%",
    //       },
    //       xAxis: {
    //         type: "category",
    //         boundaryGap: false,
    //         data: specialVehicleXData,
    //         name: "小时",
    //         axisLabel: {
    //           textStyle: {
    //             color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
    //             fontSize: 10,
    //           },
    //         },
    //         axisLine: {
    //           show: true,
    //           lineStyle: {
    //             color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
    //           },
    //         },
    //       },
    //       yAxis: {
    //         type: "value",
    //         name: "总车辆",
    //         nameTextStyle: {
    //           color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
    //           fontSize: 10,
    //           padding: [0, 20, 0, 0],
    //         },
    //         minInterval: 1, //y轴的刻度只显示整数
    //         axisLabel: {
    //           textStyle: {
    //             color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
    //             fontSize: 10,
    //           },
    //         },

    //         axisLine: {
    //           show: false,
    //         },
    //         axisTick: {
    //           show: false,
    //         },
    //         splitLine: {
    //           show: true,
    //           lineStyle: {
    //             //分割线的样式
    //             color: ["#4E6B83"],
    //             width: 1,
    //             type: "dashed",
    //           },
    //         },
    //       },
    //       series: [
    //         {
    //           type: "line",
    //           color: "#00c8ff",
    //           symbol: "none",
    //           smooth: true,
    //           stack: "Total",
    //           areaStyle: {},
    //           symbol: "circle",
    //           symbolSize: [7, 7],
    //           itemStyle: {
    //             normal: {
    //               borderColor: "white",
    //             },
    //           },
    //           emphasis: {
    //             focus: "series",
    //           },
    //           //渐变色
    //           areaStyle: {
    //             normal: {
    //               //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色
    //               color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
    //                 {
    //                   offset: 0,
    //                   color: "#33b0ee",
    //                 },
    //                 {
    //                   offset: 1,
    //                   color: "rgba(51,176,238,0.3)",
    //                 },
    //               ]),
    //             },
    //           },
    //           data: specialVehicleYData,
    //         },
    //       ],
    //     };

    //     focusCar.setOption(option);
    //     window.addEventListener("resize", function () {
    //       focusCar.resize();
    //     });
    //   });
    // },
    // 能耗监测echarts
  },
};
</script>
<style lang="scss" scoped>
.footer {
  width: 100%;
  height: 24%;
  padding: 0px 0px 0px 1vw;
  // margin-top: 10px;
  display: flex;
  padding-bottom: 5px;
  justify-content: space-between;
  // margin-top: 8px;
  position: absolute;
  bottom: 0.5%;
  z-index: 8;
  .fourBox {
    display: flex;
    justify-content: space-between;
    width: calc(100% - 30px);
    margin-right: 20px;
  }
  .footChangeButton {
    width: 1.6vw;
    height: 100%;
    ::v-deep .el-radio-group {
      width: 100%;
      height: 100%;
      .el-radio-button {
        width: 100%;
        height: 50%;
        .el-radio-button__inner {
          padding: 0 !important;
          writing-mode: tb-rl;
          white-space: nowrap;
          text-align: center;
          width: 100%;
          height: 100%;
          line-height: 1.5vw;
          letter-spacing: 16px;
          border-radius: 0;
          font-size: 0.75vw;
        }
      }
    }
    .el-radio-button {
      border: solid 0.1px #0067b2 !important;
      border-radius: 0px !important;
    }
  }
  .footTitle {
    padding: 0 1vw;
    // line-height: 25px;
    font-size: 0.7vw;
    display: flex;
    align-items: center;
    font-family: inherit;
    height: 2.4vh;
    .footTriangle {
      width: 0;
      height: 0;
      border-top: 5px solid transparent;
      border-left: 10px solid #1590ff;
      border-bottom: 5px solid transparent;
    }

    .footTitleCont {
      width: 100%;
      height: 100%;
      // margin-top: -4px;
      font-weight: bold;
      display: flex;
      align-items: center;
      font-family: PingFang;

      p:nth-of-type(2) {
        text-transform: uppercase;
        // font-weight: lighter;
        transform: scale(0.8, 0.8);
        opacity: 0.35;
      }
    }
  }

  .footBigBox {
    width: 52%;
    height: 100%;
  }

  .footMiniBox {
    width: 19.5%;
    height: 100%;
    overflow: hidden;
    // background-image: url(../../../assets/cloudControl/footer_bg.png);
    // background-position: center;
    // background-repeat: no-repeat;
    // background-size: 100% 100%;
    // background-color: rgba($color: #0b1329, $alpha: 0.4);
    // border: solid 1px #183b57;
    // color: white;
    .noPicBox {
      width: 100%;
      height: 200px;
      display: flex;
      justify-content: center;
      img {
        height: 200px;
      }
    }
    video {
      height: 186px;
    }
    .listContent {
      height: 100%;
      font-size: 14px;
      overflow: hidden;
      ul {
        margin: 0;
      }
      > li {
        // margin-bottom: 6px;
        list-style: none;
        padding: 10px;
        padding-bottom: 0px;
      }
    }
  }
  .deviceBox {
    width: 100%;
    height: calc(100% - 2.4vh);
    padding-top: 10px;
    // background: yellow;
    .allMsg {
      width: 100%;
      height: 20%;
      // background: pink;
      display: flex;
      justify-content: space-around;
      .item {
        font-size: 0.7vw;
        font-weight: bold;
        > div {
          display: flex;
          align-items: center;
          height: 50%;
          justify-content: center;
        }
      }
      .item1 {
        color: #fff;
      }
      .item2 {
        color: rgba(238, 49, 139, 1);
      }
      .item3 {
        color: #f7b21a;
      }
    }
  }
  #chartJN,
  #chartWF {
    width: 100%;
    height: calc(100% - 2vw - 22px);
  }
}
#vehicle,
#energyConsumption,
#deviceChart {
  width: 100%;
  height: calc(100% - 2vw);
  margin-top: 5px;
}
#deviceChart {
  height: calc(75% - 4px);
}
.tableBox {
  // animation-name: jumpanimation;
  animation: jumpanimation linear infinite;
}
.tableBox:hover {
  animation-play-state: paused; /* 暂停动画 */
}
.jumpBox {
  height: calc(100% - 2.4vh - 10px);
  overflow: hidden;
  position: relative;
  .table_body {
    width: 100%;
    position: absolute;
    transition: all 0.5s;
    .table_list {
      .el-col {
        display: flex;
        align-items: center;
        height: 29px;
      }
    }
  }

  ::v-deep .el-table .cell {
    padding-left: 4px !important;
    padding-right: 4px !important;
  }
  ::v-deep .el-table__header-wrapper {
    display: none !important;
  }
  ::v-deep .el-table tr {
    background: transparent !important;
  }
  ::v-deep .el-table {
    background: transparent !important;
  }
  ::v-deep .el-table__body-wrapper .el-table__cell {
    border-left: none !important;
  }
}
.tabButton {
  margin: 4px 0 4px 10px;
  .el-radio-button {
    margin-right: 4px;
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 0.2vw 1vh !important;
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    font-size: 0.6vw !important;
  }
  // ::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  //   background: linear-gradient(
  //     180deg,
  //     rgba($color: #00aced, $alpha: 0.8),
  //     rgba($color: #0079db, $alpha: 0.8)
  //   );
  //   border: solid 0.1px #0067b2;
  //   box-shadow: none;
  // }
  // ::v-deep .el-radio-button__inner {
  //   background: transparent;
  //   border: solid 0.1px #0067b2;
  //   color: #fff;
  // }
}

// .anim {
//   animation: mymove 4s linear;
// }

// @keyframes mymove {
//   0% {
//     transform: translateY(0px);
//   }
//   100% {
//     transform: translateY(-20px);
//   }
// }

@keyframes jumpanimation {
  0% {
    transform: translateY(0%);
  }
  100% {
    transform: translateY(-100%);
  }
}
</style>