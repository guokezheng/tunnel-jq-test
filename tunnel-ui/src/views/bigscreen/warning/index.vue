<template>
  <div class="warning-container">
    <el-row :span="24">
      <el-col :span="5">
        <todayStatistics class="leftCharts"></todayStatistics>
        <weekStatistics class="leftCharts"></weekStatistics>
        <monthStatistics class="leftCharts"></monthStatistics>
      </el-col>
      <el-col :span="14" class="body">
        <warningMap class="warningAmap" :placeDate="placeDate"></warningMap>
        <Cumulative class="footer"></Cumulative>
      </el-col>
      <el-col :span="5">
        <faultList class="leftCharts"></faultList>
        <eventList class="leftCharts"></eventList>
        <earlyWarningList class="leftCharts"></earlyWarningList>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import todayStatistics from "./components/todayStatistics.vue";
import weekStatistics from "./components/weekStatistics.vue";
import monthStatistics from "./components/monthStatistics.vue";
import Cumulative from "./components/Cumulative.vue";
import faultList from "./components/faultList.vue";
import eventList from "./components/eventList.vue";
import earlyWarningList from "./components/earlyWarningList.vue";
import warningMap from "./components/warningMap.vue";
export default {
  components: {
    todayStatistics,
    weekStatistics,
    monthStatistics,
    Cumulative,
    faultList,
    eventList,
    earlyWarningList,
    warningMap,
  },
  data() {
    return {
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
                  data: [5, 7, 8, 7, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 95, // 正常
                  malfunctionVal: 5, // 故障
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
                  data: [6, 7, 5, 7, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 99, // 正常
                  malfunctionVal: 1, // 故障
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
                  data: [7, 7, 6, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 98, // 正常
                  malfunctionVal: 2, // 故障
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
                  data: [7, 8, 6, 8, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 97, // 正常
                  malfunctionVal: 3, // 故障
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
                  data: [8, 7, 7, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 96, // 正常
                  malfunctionVal: 4, // 故障
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
                  data: [6, 7, 6, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 95, // 正常
                  malfunctionVal: 5, // 故障
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
                  data: [6, 8, 6, 8, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 94, // 正常
                  malfunctionVal: 6, // 故障
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
                  data: [7, 8, 6, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 93, // 正常
                  malfunctionVal: 7, // 故障
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
                  data: [7, 8, 7, 7, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 8, // 故障
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
                  data: [7, 8, 6, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 91, // 正常
                  malfunctionVal: 9, // 故障
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
                  data: [7, 7, 5, 6, 7, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 90, // 正常
                  malfunctionVal: 10, // 故障
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
                  data: [7, 8, 5, 6, 8, 6],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 11, // 故障
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
    };
  },
  mounted() {},
  methods: {},
};
</script>

<style lang="less" scoped>
.warning-container {
  width: 100%;
  height: 100%;
  // background-color: #041356;

  .el-row {
    height: 98%;
    width: 100%;
    .el-col {
      height: 100%;
      .leftCharts {
        height: 33%;
        overflow: hidden;
        // border: solid 2px #069cca;
        // border-radius: 1vw;
        margin-bottom: 0.5vw;
        // padding: 0.5vw 0;
      }
    }
  }
  .body {
    .warningAmap {
      // height: calc(66% - 0.4vw)!important;
      // position: absolute;
      // border: solid 1px white;
    }
    .footer {
      width: 58%;
      height: 28%;
      position: absolute;
      top: 72%;
      z-index: 1;
      border: solid 1px #00ade3;
    }
  }
}
ul {
  margin: 0px;
}
</style>
<style lang="less">
ul {
  margin: 0px;
}
// .contentTitle{
//     width: 99%;
//     height: 1.6vw;
//     margin: 0 auto;
//     // background-color: rgba(255,255,255,0.2);
//     // background-color: rgba(4,15,78,0.4);
//     color: white;
//     font-size: 0.9vw;
//     padding-left: 1vw;
//     line-height: 1.6vw;
//     i{
//         color: rgba(255,255,255,0.5);
//         float: right;
//         padding-right: 0.5vw;
//         font-size: 0.7vw;
//         line-height: 1.8vw;
//         text-transform:Uppercase;
//     }

// }
// .alarmsStatisticsBox{
//         width: 99%;
//         height: calc( 100% - 2vw );
//         margin: 0 auto;
//         // border: solid 1px white;
//         // background-color: rgba(255,255,255,0.1);
//         background-color: rgba(4,15,78,0.8);
// }
// .listHeader{
//     font-size: 1.2vw;
//     background-color: #041356;
//     color: #FFFFFF;
//     line-height: 2vw;
//     height: 2vw;
//     // border: solid 1px yellow;
// }
// .listContent{
//     height: calc(100% - 2vw);
//     color: white;
//     font-size: 0.8vw;
//     line-height: 1.8vw;
//     overflow: hidden;
//     .el-row {
//       // min-height: 2vw;
//       width: 100%;
//       // border:solid 1px white;
//       .el-col {
//         display: flex;
//         align-items: center;
//         // border: solid 1px white;
//       }
//     }
// }
.headerIndex {
  width: 7vw;
  /* border: solid 1px red; */
  height: 1.9vw;
  text-align: center;
}
.headerName {
  width: 24vw;
  height: 1.9vw;
  /* border: solid 1px white; */
  padding-left: 1vw;
}
span {
  caret-color: rgba(0, 0, 0, 0);
}
div {
  caret-color: rgba(0, 0, 0, 0);
  cursor: default;
}
</style>

