<template>
  <div class="footer">
    <div class="fourBox">
      <div class="footMiniBox" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="carIcon"
              style="width: 0.9vw; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
              v-show="sideTheme != 'theme-blue'"
            />
            <p>能耗监测</p>
            <p>Energy consumption monitoring</p>
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
              v-show="sideTheme != 'theme-blue'"
            />
            <p>重点车辆</p>
            <p>Key vehicles</p>
          </div>
        </div>
        <div id="focusCar"></div>
      </div>
      <div class="footerRight footMiniBox" v-show="footChangeRadio == '图表'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 0.8vw; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
          暂无交通事件
        </div>
        <div v-if="trafficList" @click="jumpYingJi">
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
                  <!-- @click.native="jumpYingJi(item.id)"  -->
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
                    <!-- {{ item.startTime }} {{ item.tunnels.tunnelName }}发生{{
                        item.eventType.eventType
                      }}事件 -->
                    <div
                      style="
                        width: 210px;
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
        </div>
      </div>
      <div class="footMiniBox" v-show="footChangeRadio == '视频'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '视频'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '视频'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
      <div class="footMiniBox footerRight" v-show="footChangeRadio == '视频'">
        <div class="footTitle">
          <div class="footTitleCont">
            <img
              :src="warningIcon"
              style="width: 16px; margin-right: 5px"
              v-show="sideTheme != 'theme-blue'"
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
import * as echarts from "echarts";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import { displayH5sVideoAll } from "@/api/icyH5stream";
import {
  videoStreaming,
  getDeviceById,
} from "@/api/equipment/eqlist/api";
import { getEntranceExitVideo } from "@/api/eventDialog/api.js";
import {
  vehicleMonitoringInRecent24Hours,
  specialVehicleMonitoringInRecent24Hours,
} from "@/api/workbench/config.js";
import {
  energyConsumptionDetection,
} from "@/api/equipment/tunnel/api.js";
import {
  getWarnEvent,
} from "@/api/event/event";
export default {
  components:{
    videoPlayer
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
      videoTitle1: "",
      videoTitle2: "",
      videoTitle3: "",
      videoTitle4: "",
      liveUrl1: "",
      liveUrl2: "",
      liveUrl3: "",
      liveUrl4: "",
      cameraPlayer1: false,
      cameraPlayer2: false,
      cameraPlayer3: false,
      cameraPlayer4: false,
      directionList:[{},{}],
      tunnelId:''
    };
  },
  computed:{
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  created() {
    this.getDicts("sd_strategy_direction").then((data) => {
      this.directionList = data.data;
    });
  },
  methods: {
    init(tunnelId){
        this.tunnelId = tunnelId;
        this.getWarnList();
        this.vehicleEcharts()
        this.specialVehicleEcharts()
    },
    // 车辆监测数据
    vehicleEcharts() {
      const param = {
        tunnelId: this.tunnelId,
      };
      vehicleMonitoringInRecent24Hours(param).then((res) => {
        // console.log(res, "车辆监测数据");
        var vehicleXData = [];
        var vehicleYData = [];
        for (var item of res.data) {
          vehicleXData.push(item.hour);
          vehicleYData.push(item.count);
        }
        this.initeChartsEnd(vehicleXData, vehicleYData);
      });
    },
    // 重点车辆监测数据
    specialVehicleEcharts() {
      // console.log(this.tunnelId,"this.tunnelIdthis.tunnelIdthis.tunnelId")
      const param = {
        tunnelId: this.tunnelId,
      };
      specialVehicleMonitoringInRecent24Hours(param).then((res) => {
        // console.log(res, "重点车辆监测数据");
        var specialVehicleXData = [];
        var specialVehicleYData = [];
        for (var item of res.data) {
          specialVehicleXData.push(item.hour);
          specialVehicleYData.push(item.count);
        }
        this.loadFocusCar(specialVehicleXData, specialVehicleYData);
      });
    },
    getEnergyConsumption() {
      energyConsumptionDetection(this.tunnelId).then((res) => {
        // console.log(res, "能耗监测");
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
          areaStyle: {},
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
    // 预警事件列表
    getWarnList() {
      const param = {
        eventState: "3",
      };
      getWarnEvent(param).then((response) => {
        // console.log(response.data, "预警事件");
        this.trafficList = response.data;
      });
    },
    // 预警事件点击跳转应急调度
    jumpYingJi(e) {
      const item = e.target.closest(".listRow");
      if (item) {
        // 是否是滚动组件的某一行/列
        const { index } = item.dataset;
        let id = JSON.parse(index).id;
        setTimeout(() => {
          bus.$emit("getPicId", id);
        }, 200);
        bus.$emit("openPicDialog");
      }
    },
    // 切换图表、视频
    videoRadioChange() {
      if (this.footChangeRadio == "视频" && this.tunnelId) {
        this.getFooterVideo();
      } else {
        this.cameraPlayer1 = false;
        this.cameraPlayer2 = false;
        this.cameraPlayer3 = false;
        this.cameraPlayer4 = false;
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
                  this.liveUrl1 = res.data.liveUrl;
                  this.cameraPlayer1 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                }
              });
              videoStreaming(res.data[0].outlet).then((res) => {
                if (res.code == 200 && res.data) {
                  this.liveUrl2 = res.data.liveUrl;
                  this.cameraPlayer2 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
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
                  this.liveUrl3 = res.data.liveUrl;
                  this.cameraPlayer3 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                }
              });
              videoStreaming(res.data[0].outlet).then((res) => {
                if (res.code == 200 && res.data) {
                  this.liveUrl4 = res.data.liveUrl;
                  this.cameraPlayer4 = true;
                } else {
                  this.$modal.msgWarning("获取视频失败");
                }
              });
            }
          }
        }
      );
    },
    initeChartsEnd(vehicleXData, vehicleYData) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        //	此dom为echarts图标展示dom
        var vehicle = echarts.init(document.getElementById("vehicle"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          // legend: {
          //   show: true,
          //   icon: "rect",
          //   itemWidth: 10,
          //   itemHeight: 10,
          //   x: 'center',
          //   data: ['客车', '货车', '专项车'],
          //   textStyle: { //图例文字的样式
          //     color: this.sideTheme!='theme-blue'?'#fff':'#003a5d',
          //     fontSize: 12
          //   }
          // },
          calculable: true,
          grid: {
            top: "24%",
            bottom: "20%",
            left: "14%",
            right: "14%",
          },
          xAxis: [
            {
              name: "小时",
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
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                  fontFamily: "PingFang",
                },
              },
              axisLine: {
                show: true,
                lineStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                },
              },
              data: vehicleXData,
            },
          ],
          yAxis: [
            {
              name: "总车量",
              nameTextStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
                padding: [0, 20, 0, 0],
              },
              type: "value",
              minInterval: 1,
              axisTick: {
                show: false,
              },
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
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                },
              },
            },
          ],
          series: [
            {
              name: "车辆总数",
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
              data: vehicleYData,
            },
            // {
            //   name: '货车',
            //   type: 'line',
            //   color: '#db72a7',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   stack: 'Total',
            //   areaStyle: {},
            //   emphasis: {
            //     focus: 'series'
            //   },
            //   //渐变色
            //   areaStyle: {
            //     normal: {
            //       //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            //       color: new echarts.graphic.LinearGradient(
            //         0, 0, 0, 1,
            //         [{
            //             offset: 0,
            //             color: '#db72a7'
            //           },
            //           {
            //             offset: 1,
            //             color: 'rgba(219,114,167,0.3)'
            //           }
            //         ]
            //       )
            //     }
            //   },
            //   data: [90, 70, 50, 30, 80, 90, 30, 60, 70, 80, 90, 20]
            // }, {
            //   name: '专项车',
            //   type: 'line',
            //   color: '#ffb600',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   data: [20, 30, 40, 50, 70, 80, 90, 60, 40, 30, 20, 60]
            // },
          ],
        };
        vehicle.setOption(option);
        window.addEventListener("resize", function () {
          vehicle.resize();
        });
      });
    },
    loadFocusCar(specialVehicleXData, specialVehicleYData) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      newPromise.then(() => {
        var focusCar = echarts.init(document.getElementById("focusCar"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          toolbox: {
            show: true,
            feature: {
              // magicType: { show: true, type: ['stack', 'tiled'] },
              // saveAsImage: { show: true }
            },
          },
          grid: {
            top: "20%",
            bottom: "18%",
            left: "14%",
            right: "14%",
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            data: specialVehicleXData,
            name: "小时",
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "总车辆",
            nameTextStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            minInterval: 1, //y轴的刻度只显示整数
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
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
                color: ["#4E6B83"],
                width: 1,
                type: "dashed",
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
              data: specialVehicleYData,
            },
          ],
        };

        focusCar.setOption(option);
        window.addEventListener("resize", function () {
          focusCar.resize();
        });
      });
    },
    // 能耗监测echarts
    initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR) {
      var energyConsumption = echarts.init(
        document.getElementById("energyConsumption")
      );

      var option = {
        tooltip: {
          trigger: "axis",
        },
        legend: {
          show: true,
          icon: "rect",
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
            color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
            fontSize: 12,
          },
        },
        calculable: true,
        grid: {
          top: "24%",
          bottom: "20%",
          left: "14%",
          right: "14%",
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
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
                fontFamily: "PingFang",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "kwh",
            nameTextStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
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
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
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
  bottom:0.5%;
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
    width: 24.5%;
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
      height: 70%;
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
}
#vehicle,
#energyConsumption,
#focusCar {
  width: 100%;
  height: calc(100% - 2vw);
  margin-top: 5px;
}
</style>