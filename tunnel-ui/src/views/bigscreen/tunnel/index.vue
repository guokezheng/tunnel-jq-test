<template>
  <div class="tunnel-container">
    <!-- admin -->
    <div v-if="userRight" class="adminClass">
      <div class="adminClass-left">
        <div class="adminClass-left-top">
          <equipment-pm :equipmentData="userRightData.equipmentData" />
        </div>
        <div class="adminClass-left-center">
          <armamentarium />
        </div>
        <div class="adminClass-left-footer">
          <tunnel-event />
        </div>
      </div>
      <div class="adminClass-center">
        <div style="height: 99%">
          <amap
            v-if="isSelect == 'map'"
            ref="amapRef"
            :userRight="userRight"
            :placeDate="placeDate"
            :viewMode="viewMode"
            @updateUserRight="updateUserRight"
            @changeVideo="changeVideo"
          ></amap>
          <div v-else style="height: 100%">
            <iframe
              name="tuniframe"
              id="miframe"
              width="100%"
              style="height: 100%"
              frameborder="0"
              align="center"
              allowfullscreen="true"
              allow="autoplay"
              :src="iframeSrc"
            ></iframe>
          </div>
        </div>
        <div class="float-btn">
          <span
            v-show="isSelect == 'map'"
            style="margin-right: 1vw"
            @click="switchingMap"
            >切换{{ tunnelName == "山东" ? "四川" : "山东" }}</span
          >
          <span
            v-show="isSelect == 'map'"
            style="margin-right: 1vw"
            @click="changeViewMode"
            >{{ viewMode == "2D" ? "3D地图" : "2D地图" }}显示</span
          >
          <span @click="switchingTuunel" style="margin-right: 1vw"
            >{{ isSelect == "map" ? "3D隧道" : "地图" }}显示</span
          >
          <span
            @click="
              changeTuunel('http://10.168.56.206:7090/tunnel-fhs/', 'fhs')
            "
            v-show="isSelect == '3Dtuunel'"
            :class="isActive == 'fhs' ? 'active' : ''"
            style="margin-right: 1vw"
            >凤凰山</span
          >
          <span
            @click="
              changeTuunel('http://10.168.56.206:7090/tunnel-qsg/', 'qsg')
            "
            v-show="isSelect == '3Dtuunel'"
            :class="isActive == 'qsg' ? 'active' : ''"
            style="margin-right: 1vw"
            >青石关</span
          >
          <span
            @click="
              changeTuunel('http://10.168.56.206:7090/tunnel-new/', 'new')
            "
            :class="isActive == 'new' ? 'active' : ''"
            v-show="isSelect == '3Dtuunel'"
            >乐疃</span
          >
        </div>
        <!-- 控制记录浮动窗 -->
        <div v-show="topShow" class="float-top">
          <i class="el-icon-s-fold top-fold-icon" @click="topShow = false"></i>
          <controlRecord />
        </div>
        <i
          v-show="!topShow"
          class="el-icon-s-unfold top-unfold-icon"
          @click="topShow = true"
        ></i>
        <!-- 事件浮动窗 -->
        <div v-show="footerShow" class="float-footer">
          <i
            class="el-icon-s-fold footer-fold-icon"
            @click="footerShow = false"
          ></i>
          <the-alarm-number />
        </div>
        <i
          v-show="!footerShow"
          class="el-icon-s-unfold footer-unfold-icon"
          @click="footerShow = true"
        ></i>
      </div>
      <div class="adminClass-right">
        <div class="adminClass-right-top">
          <tunnel-safety-index />
        </div>
        <div class="adminClass-right-body">
          <weight />
        </div>
        <div class="adminClass-right-footer">
          <tunnel-ranking />
        </div>
      </div>
    </div>
    <div v-if="!userRight" style="width: 100%; height: 100%">
      <div class="body">
        <el-row :gutter="20" style="height: 100%">
          <el-col :span="5" class="body-left">
            <div>
              <div>
                <div>视频1</div>
                <div>左</div>
              </div>
              <div>
                <video
                  controls
                  ref="videoDom1"
                  class="video-box"
                  muted
                  autoplay
                  loop
                >
                  <source src type="video/mp4" />
                </video>
              </div>
            </div>
            <div>
              <div>
                <div>视频2</div>
                <div>右</div>
              </div>
              <div>
                <video
                  controls
                  ref="videoDom2"
                  class="video-box"
                  muted
                  autoplay
                  loop
                >
                  <source src type="video/mp4" />
                </video>
              </div>
            </div>
          </el-col>
          <el-col :span="10" class="body-center">
            <div class="body-center-title">
              <h1>{{ tunnelName }}隧道状况分布</h1>
              <span
                class="switchingMap"
                @click="switchingMap"
                style="margin-right: 1vw"
                >切换</span
              >
              <span
                style="cursor: pointer"
                v-show="!userRight && role"
                @click="userRight = true"
                >返回全视图</span
              >
            </div>
            <div style="width: 99%; height: 100%">
              <amap
                ref="amapRef"
                :userRight="userRight"
                :placeDate="placeDate"
                @updateUserRight="updateUserRight"
                @changeVideo="changeVideo"
              ></amap>
            </div>
            <statistics
              :incidentVal="placeDate.incidentVal"
              :earlyWarningVal="placeDate.earlyWarningVal"
              :malfunctionVal="placeDate.malfunctionVal"
              class="statistics"
            />
          </el-col>
          <el-col :span="9" class="body-right">
            <el-row :gutter="20" style="height: 100%">
              <el-col :span="11" class="body-right-lt">
                <list style="100%"></list>
              </el-col>
              <el-col :span="13" class="body-right-rt">
                <div>
                  <div>
                    <div>视频3</div>
                    <div>左</div>
                  </div>
                  <div>
                    <video
                      controls
                      ref="videoDom3"
                      class="video-box"
                      muted
                      autoplay
                      loop
                      src
                    >
                      <source src type="video/mp4" />
                    </video>
                  </div>
                </div>
                <div>
                  <div>
                    <div>视频4</div>
                    <div>右</div>
                  </div>
                  <div>
                    <video
                      controls
                      ref="videoDom4"
                      class="video-box"
                      muted
                      autoplay
                      loop
                    >
                      <source src type="video/mp4" />
                    </video>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
      <div class="footer">
        <el-row type="flex" justify="space-between">
          <el-col>
            <TrafficFlow
              ref="TrafficFlowRef"
              :trafficData="echartsDate.trafficData"
            ></TrafficFlow>
          </el-col>
          <el-col>
            <windSpeed
              ref="windSpeedRef"
              :windData="echartsDate.windData"
            ></windSpeed>
          </el-col>
          <el-col>
            <COconcentration
              ref="COconcentrationRef"
              :COData="echartsDate.COData"
            ></COconcentration>
          </el-col>
          <!-- <el-col><burglarAlarm></burglarAlarm></el-col> -->
          <el-col>
            <HoleHeight
              ref="HoleHeightRef"
              :luminanceData="echartsDate.luminanceData"
            ></HoleHeight>
          </el-col>
          <el-col>
            <Equipment
              ref="EquipmentRef"
              :equipmentData="echartsDate.equipmentData"
            ></Equipment>
          </el-col>
          <el-col>
            <year
              ref="yearRef"
              :energyConsumption="echartsDate.energyConsumption"
            ></year>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import Amap from "./components/amap.vue";
import TrafficFlow from "./components/TrafficFlow.vue";
import windSpeed from "./components/windSpeed.vue";
import COconcentration from "./components/COconcentration.vue";
import burglarAlarm from "./components/burglarAlarm.vue";
import HoleHeight from "./components/HoleHeight.vue";
import Equipment from "./components/Equipment.vue";
import year from "./components/year.vue";
import list from "./components/list.vue";
import statistics from "./components/statistics.vue";
import armamentarium from "./components/armamentarium.vue";
import TunnelEvent from "./components/tunnelEvent.vue";
import TunnelSafetyIndex from "./components/tunnelSafetyIndex.vue";
import TunnelRanking from "./components/tunnelRanking.vue";
import TheAlarmNumber from "./components/theAlarmNumber.vue";
import weight from "./components/weight.vue";
import controlRecord from "./components/controlRecord.vue";
import EquipmentPm from "./components/EquipmentPm.vue";
import { checkPermi, checkRole } from "@/utils/permission.js";

export default {
  components: {
    Amap,
    TrafficFlow,
    windSpeed,
    COconcentration,
    burglarAlarm,
    HoleHeight,
    Equipment,
    year,
    list,
    statistics,
    TunnelSafetyIndex,
    TunnelRanking,
    TheAlarmNumber,
    TunnelEvent,
    armamentarium,
    weight,
    controlRecord,
    EquipmentPm,
  },
  data() {
    return {
      video1: require("@/assets/Example/v1.mp4"),
      video2: require("@/assets/Example/v2.mp4"),
      video3: require("@/assets/Example/v3.mp4"),
      video4: require("@/assets/Example/v4.mp4"),
      video5: require("@/assets/Example/d1.mp4"),
      video6: require("@/assets/Example/d2.mp4"),
      video7: require("@/assets/Example/d3.mp4"),
      video8: require("@/assets/Example/d4.mp4"),
      iframeSrc: "http://10.168.56.206:7090/tunnel-fhs/",
      type: true,
      tunnelName: "山东",
      placeDate: {
        name: "山东",
        type: "province", // province代表省份, city代表城市
        centralPoint: [118.549381, 36.382265], // 中心点坐标
        incidentVal: 13, // 事件
        earlyWarningVal: 23, // 预警
        malfunctionVal: 10, // 故障
        markersList: [
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "姚家峪隧道",
            position: [117.822164, 36.493427],
            extData: {
              name: "姚家峪隧道",
              id: 0,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [240, 230, 250, 260, 255, 245],
                }, // co浓度
                trafficData: {
                  data: [22020, 25050, 23500, 25300, 26030, 24500],
                }, // 车流量
                windData: {
                  data: [6, 7, 8, 6, 8, 7],
                }, // 风速
                luminanceData: {
                  data: [5200, 4800, 5100, 5500, 4500, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "姚家峪隧道",
                  data: [544, 765, 834, 788, 734, 697],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "滨莱高速",
            position: [117.784441, 36.32409],
            extData: {
              name: "滨莱高速",
              id: 1,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [220, 210, 240, 250, 235, 265],
                }, // co浓度
                trafficData: {
                  data: [23020, 20050, 25500, 22300, 22030, 24500],
                }, // 车流量
                windData: {
                  data: [8, 6, 7, 7, 6, 7],
                }, // 风速
                luminanceData: {
                  data: [5500, 4500, 4500, 5500, 4500, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "滨莱高速",
                  data: [632, 765, 508, 722, 766, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 96, // 正常
                  malfunctionVal: 1, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "中庄隧道",
            position: [121.435835, 37.079518],
            extData: {
              name: "中庄隧道",
              id: 2,
              tunnelLength: null,
              affiliation: "荣潍高速",
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [12020, 15050, 13500, 15300, 16030, 14500],
                }, // 车流量
                windData: {
                  data: [7, 7, 8, 6, 7, 7],
                }, // 风速
                luminanceData: {
                  data: [4000, 4500, 4500, 5500, 4500, 5500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "中庄隧道",
                  data: [723, 786, 664, 698, 743, 631],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 4,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "马公祠隧道",
            position: [117.824197, 36.472199],
            extData: {
              name: "马公祠隧道",
              id: 3,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [12420, 15650, 13800, 15500, 15030, 14800],
                }, // 车流量
                windData: {
                  data: [7, 6, 8, 6, 6, 7],
                }, // 风速
                luminanceData: {
                  data: [4800, 5500, 4700, 5200, 4600, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "马公祠隧道",
                  data: [745, 813, 608, 824, 713, 698],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 94, // 正常
                  malfunctionVal: 3, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "乐疃隧道",
            position: [117.821815, 36.457601],
            extData: {
              name: "乐疃隧道",
              id: 4,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [230, 250, 240, 245, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [20320, 25050, 23500, 25800, 25030, 24000],
                }, // 车流量
                windData: {
                  data: [7, 6, 6, 6, 7, 7],
                }, // 风速
                luminanceData: {
                  data: [4600, 5300, 5500, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "乐疃隧道",
                  data: [832, 743, 786, 686, 724, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 93, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "樵岭前隧道",
            position: [117.817382, 36.448935],
            extData: {
              name: "樵岭前隧道",
              id: 5,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [223, 223, 245, 236, 264, 273],
                }, // co浓度
                trafficData: {
                  data: [23340, 25250, 23340, 22300, 26240, 25200],
                }, // 车流量
                windData: {
                  data: [5, 6, 5, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [3200, 4200, 5500, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "樵岭前隧道",
                  data: [634, 786, 608, 624, 736, 618],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "佛羊岭隧道",
            position: [117.800823, 36.374613],
            extData: {
              name: "佛羊岭隧道",
              id: 6,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [252, 235, 262, 246, 226, 273],
                }, // co浓度
                trafficData: {
                  data: [26230, 24220, 24630, 26530, 25240, 25200],
                }, // 车流量
                windData: {
                  data: [5, 6, 7, 6, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [5200, 5200, 5000, 4200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "佛羊岭隧道",
                  data: [642, 824, 646, 822, 796, 625],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 91, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "洪河隧道",
            position: [117.794317, 35.745686],
            extData: {
              name: "洪河隧道",
              id: 7,
              tunnelLength: "515米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [22330, 25220, 22530, 20100, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 6, 7, 7, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [4200, 4800, 5300, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "洪河隧道",
                  data: [756, 835, 676, 612, 798, 634],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 90, // 正常
                  malfunctionVal: 7, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "迎春坡隧道",
            position: [117.653719, 35.329564],
            extData: {
              name: "迎春坡隧道",
              id: 8,
              tunnelLength: "973米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [22430, 23420, 23530, 22300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 8, 7, 6, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [3400, 4800, 5300, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "迎春坡隧道",
                  data: [724, 826, 755, 797, 726, 694],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 8, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "毓秀山隧道",
            position: [117.597113, 35.250696],
            extData: {
              name: "毓秀山隧道",
              id: 9,
              tunnelLength: "400米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [24430, 25420, 23530, 23300, 25570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [4400, 4800, 5300, 5200, 4900, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "毓秀山隧道",
                  data: [735, 836, 635, 635, 735, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 88, // 正常
                  malfunctionVal: 9, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "望海石隧道",
            position: [117.524916, 35.021412],
            extData: {
              name: "望海石隧道",
              id: 10,
              tunnelLength: "2145米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [245, 265, 252, 246, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [23430, 25420, 24530, 24300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [4400, 5100, 5300, 4700, 4900, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "望海石隧道",
                  data: [735, 735, 545, 675, 724, 687],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 87, // 正常
                  malfunctionVal: 10, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "龙山寨隧道",
            position: [117.53021, 35.005675],
            extData: {
              name: "龙山寨隧道",
              id: 11,
              tunnelLength: "515米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [265, 245, 252, 246, 256, 273],
                }, // co浓度
                trafficData: {
                  data: [25430, 25420, 23530, 22300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 6, 6, 7, 5],
                }, // 风速
                luminanceData: {
                  data: [5400, 5100, 5300, 4700, 5100, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "龙山寨隧道",
                  data: [724, 853, 524, 609, 825, 683],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 5,
                }, // 设备
              },
            },
          },
          // {
          //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
          //   title: "照山隧道",
          //   position: [117.53021,35.005675],
          //    extData: {
          //     name: "照山隧道",
          //     id: null,
          //     tunnelLength: '1408米',
          //   },
          // },
          // {
          //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
          //   title: "青龙崮隧道不准确",
          //   position: [122.421867,36.890273], // 不准确
          // },
          // {
          //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
          //   title: "红山隧道",
          //   position: [117.109264,36.645469],
          //    extData: {
          //     name: "红山隧道",
          //     id: null,
          //     tunnelLength: '1434米',
          //   },
          // },
          // {
          //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
          //   title: "长山隧道",
          //   position: [117.597113,35.250696],
          //    extData: {
          //     name: "龙山寨隧道",
          //     id: null,
          //     tunnelLength: '908米',
          //   },
          // },
        ],
      },
      echartsDate: {
        COData: {
          data: [240, 230, 250, 260, 255, 245],
        }, // co浓度
        trafficData: {
          data: [22020, 25050, 23500, 25300, 26030, 24500],
        }, // 车流量
        windData: {
          data: [6, 7, 8, 6, 8, 7],
        }, // 风速
        luminanceData: {
          data: [5200, 4800, 5100, 5500, 4500, 5000],
        }, // 洞口亮度
        energyConsumption: {
          name: "姚家峪隧道",
          data: [542, 743, 834, 793, 738, 655],
        }, // 年度能耗
        equipmentData: {
          normalVal: 95, // 正常
          malfunctionVal: 5, // 故障
          offlineVal: 3,
        }, // 设备
      },
      switchingMapTimeout: null, // 切换按钮定时器
      // 切换全视图
      userRight: true,
      // 角色权限
      role: null,
      userRightData: {
        equipmentData: {
          normalVal: 1088,
          malfunctionVal: 60,
          offlineVal: 52,
        },
      },
      footerShow: true, // 控制事件浮动窗
      topShow: true, // 控制记录浮动窗
      isSelect: "map",
      viewMode: "3D",
      isActive: "map",
    };
  },
  created() {
    // 获取角色权限
    this.role = checkRole(["admin"]);
    this.userRight = this.role;
  },
  mounted() {
    if (!this.userRight) {
      this.$refs.videoDom1.src = this.video1;
      this.$refs.videoDom2.src = this.video2;
      this.$refs.videoDom3.src = this.video3;
      this.$refs.videoDom4.src = this.video4;
    }
  },
  methods: {
    // 点击图标切换视频
    changeVideo(extData) {
      this.$nextTick(() => {
        if (!this.type) {
          this.$refs.videoDom1.src = this.video1;
          this.$refs.videoDom2.src = this.video2;
          this.$refs.videoDom3.src = this.video3;
          this.$refs.videoDom4.src = this.video4;
          this.type = true;
        } else {
          this.$refs.videoDom1.src = this.video5;
          this.$refs.videoDom2.src = this.video6;
          this.$refs.videoDom3.src = this.video7;
          this.$refs.videoDom4.src = this.video8;
          this.type = false;
        }
      });
      this.echartsDate = extData.echartsDate;
      this.initEcharts();
    },
    // 初始化echarts
    initEcharts() {
      this.$nextTick(() => {
        this.$refs.TrafficFlowRef.TrafficFlow();
        this.$refs.windSpeedRef.windSpeed();
        this.$refs.COconcentrationRef.COconcentration();
        this.$refs.HoleHeightRef.HoleHeight();
        this.$refs.EquipmentRef.Equipment();
        this.$refs.yearRef.year();
      });
    },
    // 切换地图显示地区
    switchingMap() {
      if (this.switchingMapTimeout) clearTimeout(this.switchingMapTimeout);
      this.switchingMapTimeout = setTimeout(async () => {
        if (this.tunnelName == "山东") {
          this.placeDate = {
            name: "四川",
            type: "province",
            centralPoint: [103.248235, 30.637441],
            markersList: [
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "四川：长山隧道",
                position: [104.284547, 29.443451],
                extData: {
                  id: 0,
                  name: "四川：长山隧道",
                  tunnelLength: "10174米",
                  affiliation: "隆汉高速",
                  echartsDate: {
                    COData: {
                      data: [235, 245, 232, 246, 256, 243],
                    }, // co浓度
                    trafficData: {
                      data: [22430, 23420, 23530, 25300, 23570, 25200],
                    }, // 车流量
                    windData: {
                      data: [5, 7, 6, 5, 6, 5],
                    }, // 风速
                    luminanceData: {
                      data: [5400, 5100, 5300, 4700, 5100, 4500],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "长山隧道",
                      data: [553, 894, 522, 767, 825, 683],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 88, // 正常
                      malfunctionVal: 7, // 故障
                      offlineVal: 5,
                    }, // 设备
                  },
                },
              },
            ],
            incidentVal: 10, // 事件
            earlyWarningVal: 5, // 预警
            malfunctionVal: 3, // 故障
          };
        } else {
          this.placeDate = {
            name: "山东",
            type: "province", // province代表省份, city代表城市
            centralPoint: [118.549381, 36.382265], // 中心点坐标
            markersList: [
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "姚家峪隧道",
                position: [117.822164, 36.493427],
                extData: {
                  name: "姚家峪隧道",
                  id: 0,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [240, 230, 250, 260, 255, 245],
                    }, // co浓度
                    trafficData: {
                      data: [22020, 25050, 23500, 25300, 26030, 24500],
                    }, // 车流量
                    windData: {
                      data: [6, 7, 8, 6, 8, 7],
                    }, // 风速
                    luminanceData: {
                      data: [5200, 4800, 5100, 5500, 4500, 5000],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "姚家峪隧道",
                      data: [525, 744, 826, 726, 726, 695],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 5, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "滨莱高速",
                position: [117.784441, 36.32409],
                extData: {
                  name: "滨莱高速",
                  id: 1,
                  tunnelLength: null,
                  affiliation: null,
                  echartsDate: {
                    COData: {
                      data: [220, 210, 240, 250, 235, 265],
                    }, // co浓度
                    trafficData: {
                      data: [23020, 20050, 25500, 22300, 22030, 24500],
                    }, // 车流量
                    windData: {
                      data: [8, 6, 7, 7, 6, 7],
                    }, // 风速
                    luminanceData: {
                      data: [5500, 4500, 4500, 5500, 4500, 5000],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "滨莱高速",
                      data: [645, 769, 507, 797, 725, 674],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 96, // 正常
                      malfunctionVal: 1, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "中庄隧道",
                position: [121.435835, 37.079518],
                extData: {
                  name: "中庄隧道",
                  id: 2,
                  tunnelLength: null,
                  affiliation: "荣潍高速",
                  echartsDate: {
                    COData: {
                      data: [240, 240, 250, 250, 265, 255],
                    }, // co浓度
                    trafficData: {
                      data: [12020, 15050, 13500, 15300, 16030, 14500],
                    }, // 车流量
                    windData: {
                      data: [7, 7, 8, 6, 7, 7],
                    }, // 风速
                    luminanceData: {
                      data: [4000, 4500, 4500, 5500, 4500, 5500],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "中庄隧道",
                      data: [725, 768, 636, 625, 724, 679],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 4, // 故障
                      offlineVal: 4,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "马公祠隧道",
                position: [117.824197, 36.472199],
                extData: {
                  name: "马公祠隧道",
                  id: 3,
                  tunnelLength: null,
                  affiliation: null,
                  echartsDate: {
                    COData: {
                      data: [240, 240, 250, 250, 265, 255],
                    }, // co浓度
                    trafficData: {
                      data: [12420, 15650, 13800, 15500, 15030, 14800],
                    }, // 车流量
                    windData: {
                      data: [7, 6, 8, 6, 6, 7],
                    }, // 风速
                    luminanceData: {
                      data: [4800, 5500, 4700, 5200, 4600, 5000],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "马公祠隧道",
                      data: [724, 854, 686, 824, 777, 633],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 94, // 正常
                      malfunctionVal: 3, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "乐疃隧道",
                position: [117.821815, 36.457601],
                extData: {
                  name: "乐疃隧道",
                  id: 4,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [230, 250, 240, 245, 265, 255],
                    }, // co浓度
                    trafficData: {
                      data: [20320, 25050, 23500, 25800, 25030, 24000],
                    }, // 车流量
                    windData: {
                      data: [7, 6, 6, 6, 7, 7],
                    }, // 风速
                    luminanceData: {
                      data: [4600, 5300, 5500, 5200, 4900, 5200],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "乐疃隧道",
                      data: [844, 767, 724, 607, 763, 635],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 93, // 正常
                      malfunctionVal: 4, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "樵岭前隧道",
                position: [117.817382, 36.448935],
                extData: {
                  name: "樵岭前隧道",
                  id: 5,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [223, 223, 245, 236, 264, 273],
                    }, // co浓度
                    trafficData: {
                      data: [23340, 25250, 23340, 22300, 26240, 25200],
                    }, // 车流量
                    windData: {
                      data: [5, 6, 5, 6, 7, 8],
                    }, // 风速
                    luminanceData: {
                      data: [3200, 4200, 5500, 5200, 4900, 5200],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "樵岭前隧道",
                      data: [646, 775, 644, 677, 735, 652],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 5, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "佛羊岭隧道",
                position: [117.800823, 36.374613],
                extData: {
                  name: "佛羊岭隧道",
                  id: 6,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [252, 235, 262, 246, 226, 273],
                    }, // co浓度
                    trafficData: {
                      data: [26230, 24220, 24630, 26530, 25240, 25200],
                    }, // 车流量
                    windData: {
                      data: [5, 6, 7, 6, 6, 8],
                    }, // 风速
                    luminanceData: {
                      data: [5200, 5200, 5000, 4200, 4900, 5200],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "佛羊岭隧道",
                      data: [653, 834, 642, 876, 742, 642],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 91, // 正常
                      malfunctionVal: 6, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "洪河隧道",
                position: [117.794317, 35.745686],
                extData: {
                  name: "洪河隧道",
                  id: 7,
                  tunnelLength: "515米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273],
                    }, // co浓度
                    trafficData: {
                      data: [22330, 25220, 22530, 20100, 23570, 25200],
                    }, // 车流量
                    windData: {
                      data: [6, 6, 7, 7, 6, 8],
                    }, // 风速
                    luminanceData: {
                      data: [4200, 4800, 5300, 5200, 4900, 5200],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "洪河隧道",
                      data: [742, 853, 654, 645, 713, 614],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 90, // 正常
                      malfunctionVal: 7, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "迎春坡隧道",
                position: [117.653719, 35.329564],
                extData: {
                  name: "迎春坡隧道",
                  id: 8,
                  tunnelLength: "973米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273],
                    }, // co浓度
                    trafficData: {
                      data: [22430, 23420, 23530, 22300, 23570, 25200],
                    }, // 车流量
                    windData: {
                      data: [6, 8, 7, 6, 6, 8],
                    }, // 风速
                    luminanceData: {
                      data: [3400, 4800, 5300, 5200, 4900, 5200],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "迎春坡隧道",
                      data: [753, 887, 733, 783, 757, 654],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 89, // 正常
                      malfunctionVal: 8, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "毓秀山隧道",
                position: [117.597113, 35.250696],
                extData: {
                  name: "毓秀山隧道",
                  id: 9,
                  tunnelLength: "400米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273],
                    }, // co浓度
                    trafficData: {
                      data: [24430, 25420, 23530, 23300, 25570, 25200],
                    }, // 车流量
                    windData: {
                      data: [6, 7, 7, 6, 7, 8],
                    }, // 风速
                    luminanceData: {
                      data: [4400, 4800, 5300, 5200, 4900, 4500],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "毓秀山隧道",
                      data: [764, 873, 633, 664, 746, 635],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 88, // 正常
                      malfunctionVal: 9, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "望海石隧道",
                position: [117.524916, 35.021412],
                extData: {
                  name: "望海石隧道",
                  id: 10,
                  tunnelLength: "2145米",
                  affiliation: "新台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 265, 252, 246, 236, 273],
                    }, // co浓度
                    trafficData: {
                      data: [23430, 25420, 24530, 24300, 23570, 25200],
                    }, // 车流量
                    windData: {
                      data: [6, 7, 7, 6, 7, 8],
                    }, // 风速
                    luminanceData: {
                      data: [4400, 5100, 5300, 4700, 4900, 4500],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "望海石隧道",
                      data: [774, 764, 553, 635, 763, 637],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 87, // 正常
                      malfunctionVal: 10, // 故障
                      offlineVal: 3,
                    }, // 设备
                  },
                },
              },
              {
                icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "龙山寨隧道",
                position: [117.53021, 35.005675],
                extData: {
                  name: "龙山寨隧道",
                  id: 11,
                  tunnelLength: "515米",
                  affiliation: "新台高速",
                  echartsDate: {
                    COData: {
                      data: [265, 245, 252, 246, 256, 273],
                    }, // co浓度
                    trafficData: {
                      data: [25430, 25420, 23530, 22300, 23570, 25200],
                    }, // 车流量
                    windData: {
                      data: [6, 7, 6, 6, 7, 5],
                    }, // 风速
                    luminanceData: {
                      data: [5400, 5100, 5300, 4700, 5100, 4500],
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "龙山寨隧道",
                      data: [753, 835, 536, 636, 836, 663],
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 89, // 正常
                      malfunctionVal: 6, // 故障
                      offlineVal: 5,
                    }, // 设备
                  },
                },
              },
              // {
              //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
              //   title: "照山隧道",
              //   position: [117.53021,35.005675],
              //    extData: {
              //     name: "照山隧道",
              //     id: null,
              //     tunnelLength: '1408米',
              //   },
              // },
              // {
              //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
              //   title: "青龙崮隧道不准确",
              //   position: [122.421867,36.890273], // 不准确
              // },
              // {
              //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
              //   title: "红山隧道",
              //   position: [117.109264,36.645469],
              //    extData: {
              //     name: "红山隧道",
              //     id: null,
              //     tunnelLength: '1434米',
              //   },
              // },
              // {
              //   icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
              //   title: "长山隧道",
              //   position: [117.597113,35.250696],
              //    extData: {
              //     name: "龙山寨隧道",
              //     id: null,
              //     tunnelLength: '908米',
              //   },
              // },
            ],
            incidentVal: 13, // 事件
            earlyWarningVal: 23, // 预警
            malfunctionVal: 10, // 故障
          };
        }
        this.echartsDate = this.placeDate.markersList[0].extData.echartsDate;
        this.tunnelName = this.placeDate.name;
        await this.$refs.amapRef.setMarkersList(this.placeDate.markersList);
        this.$refs.amapRef.delLoopIndex();
        this.$refs.amapRef.initNetTick();
      }, 500);
    },
    // 点击marker改变权限
    updateUserRight(marker) {
      this.userRight = false;
    },
    // 地图切换3d隧道
    switchingTuunel() {
      if (this.isSelect == "map") {
        this.isSelect = "3Dtuunel";
        this.isActive = "fhs";
        this.footerShow = false;
        this.topShow = false;
      } else {
        this.isSelect = "map";
      }
    },
    // 2D地图切换3D地图
    changeViewMode() {
      if (this.viewMode == "2D") {
        this.viewMode = "3D";
      } else {
        this.viewMode = "2D";
      }
      this.$refs.amapRef.initNetTick();
    },
    // 3D隧道切换
    changeTuunel(src, val) {
      if (this.iframeSrc != src) {
        this.iframeSrc = src;
        this.isActive = val;
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import "~@/views/bigscreen/styles/index.css";
.tunnel-container {
  width: 100%;
  height: 100%;
  .body {
    height: 65%;
    color: #09bdef;
    .body-left {
      height: 100%;
      > div {
        height: 46%;
        border: 1px solid #09bdef;
        border-radius: 10px;
        margin-bottom: 1vw;
        > div:nth-of-type(1) {
          height: 12%;
          width: 100%;
          padding-left: 1vw;
          // font-weight: bold;
          line-height: 2vw;
          display: flex;
          font-size: 1vw;
          > div:first-of-type {
            width: 90%;
            // height: 30px;
            // font-size: 16px;
          }
          > div:last-of-type {
            width: 1.3vw;
            height: 1.3vw;
            border-radius: 13px;
            margin-top: 0.4vw;
            color: white;
            background: linear-gradient(top, #027dec, #5abce5);
            text-align: center;
            line-height: 1.3vw;
            font-size: 0.8vw;
          }
        }
        > div:nth-of-type(2) {
          height: 83%;
          width: 90%;
          margin: 0 auto;
        }
      }
      // div:first-child {
      //   margin-bottom: 20px;
      // }
    }
    .body-center {
      height: 100%;
      position: relative;
      .body-center-title {
        position: absolute;
        left: 1vw;
        font-size: 0.8vw;
        top: 0;
        z-index: 1;
      }
      .statistics {
        position: absolute;
        right: 1vw;
        bottom: 1.5vw;
        width: 35%;
        height: 4vw;
      }
      .userRightClass {
        height: 65vh !important;
      }
    }

    .body-right {
      height: 100%;
      .body-right-lt {
        height: 95%;
        border: 1px solid #09bdef;
        border-radius: 10px;
      }
      .body-right-rt {
        height: 100%;
        > div {
          height: 46%;
          border: 1px solid #09bdef;
          border-radius: 10px;
          margin-bottom: 20px;
          > div:nth-of-type(1) {
            height: 12%;
            width: 100%;
            padding-left: 20px;
            // font-weight: bold;
            line-height: 2vw;
            display: flex;
            font-size: 1vw;
            > div:first-of-type {
              width: 90%;
              // height: 30px;
              // font-size: 16px;
            }
            > div:last-of-type {
              width: 1.3vw;
              height: 1.3vw;
              border-radius: 13px;
              margin-top: 0.4vw;
              color: white;
              background: linear-gradient(top, #027dec, #5abce5);
              text-align: center;
              line-height: 1.3vw;
              font-size: 0.8vw;
            }
          }
          > div:nth-of-type(2) {
            height: 83%;
            width: 90%;
            margin: 0 auto;
          }
        }
        > div:last-child {
          margin-bottom: 0;
        }
      }
    }

    .video-box {
      width: 100%;
      height: 100%;
    }
  }
  .footer {
    height: 35%;
    width: 100%;
    color: #09bdef;
    .el-row {
      height: 100%;
      .el-col {
        height: 100%;
        width: 16%;
        border: 1px solid #09bdef;
        border-radius: 10px;
        margin-right: 0.2vw;
      }
      .el-col:last-child {
        margin-right: 0;
      }
    }
  }
  .adminClass {
    display: flex;
    height: 100%;
    font-size: 0.8vw;
    justify-content: space-between;
    > div {
      height: 100%;
      // border: 1px solid #fff;
    }
    div {
      box-sizing: border-box;
      background: #004375;
    }
    .adminClass-right,
    .adminClass-left {
      width: 20vw;
    }
    .adminClass-left {
      .adminClass-left-top {
        height: 30%;
        margin-bottom: 0.5vw;
      }
      .adminClass-left-center {
        height: 30%;
        margin-bottom: 0.5vw;
      }
      .adminClass-left-footer {
        height: 37%;
      }
    }
    .adminClass-center {
      position: relative;
      width: 60vw;

      .float-btn {
        position: absolute;
        left: 1vw;
        top: 1vw;
        font-size: 1vw;
        color: #09bdef;
        span {
          cursor: pointer;
        }
        .active {
          color: #01f0ff;
          border-style: solid;
          border-width: thin;
          padding: 0.2vw;
        }
      }

      .float-top {
        position: absolute;
        width: 45%;
        height: 32%;
        right: 0;
        top: 0;
        // border: 1px solid #027dec;
        border-radius: 0 0 1vw 1vw;
        background-color: rgba(4, 15, 77, 0.5);
        .top-fold-icon {
          position: absolute;
          right: 0.8vw;
          top: 0.8vw;
          width: 1.4vw;
          height: 1.4vw;
          font-size: 1.1vw;
          color: #09bdef;
          cursor: pointer;
          z-index: 99;
        }
      }
      .top-unfold-icon {
        position: absolute;
        right: 0.2vw;
        top: 0.2vw;
        width: 1.4vw;
        height: 1.4vw;
        font-size: 1.1vw;
        color: #09bdef;
        cursor: pointer;
        z-index: 99;
      }
      .top-unfold-icon::before,
      .top-fold-icon::before {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%) rotate(90deg);
        -webkit-transform: translate(-50%, -50%) rotate(90deg);
      }

      .float-footer {
        position: absolute;
        width: 100%;
        height: 34%;
        left: 0;
        bottom: 1%;
        // border: 1px solid #027dec;
        // border-radius: 1vw 1vw 0 0;
        // background-color: #00427a;

        .footer-fold-icon {
          position: absolute;
          right: 1.4vw;
          top: 0.4vw;
          width: 1.4vw;
          height: 1.4vw;
          font-size: 1.1vw;
          color: #09bdef;
          cursor: pointer;
          z-index: 99;
        }
      }
      .footer-unfold-icon {
        position: absolute;
        right: 0.2vw;
        bottom: 0.2vw;
        width: 1.4vw;
        height: 1.4vw;
        font-size: 1.1vw;
        color: #09bdef;
        cursor: pointer;
        z-index: 99;
      }
      .footer-unfold-icon::before,
      .footer-fold-icon::before {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%) rotate(-90deg);
        -webkit-transform: translate(-50%, -50%) rotate(-90deg);
      }
    }
    .adminClass-right {
      .adminClass-right-top {
        height: 30%;
        // margin-bottom: 0.5vw;
      }
      .adminClass-right-body {
        height: 30%;
        margin-bottom: 20px;
      }
      .adminClass-right-footer {
        height: 38%;
      }
    }
  }
}
.switchingMap {
  cursor: pointer;
  caret-color: transparent;
}
.theAlarmNumber-container {
  border: 1px solid #00ade3;
  width: 98%;
  margin: auto;
}
</style>
