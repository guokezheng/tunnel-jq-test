<template>
    <div class="warning-container">
         <peakToday class="peakToday"></peakToday>
         <peakMonth class="peakMonth"></peakMonth>
         <peakValue class="peakValue"></peakValue>
         <detailsText class="detailsText"></detailsText>
         <proportion class="proportion"></proportion>
         <!-- <energyMap 
         class='amap' 
         :placeDate="placeDate"
         ></energyMap> -->
         <energyMap
         class='miniMap' 
         :placeDate="placeDate"
         ></energyMap>
         <ranking class="ranking"></ranking>
    </div>
</template>

<script>
import peakToday from "./components/peakToday.vue";
import peakValue from "./components/peakValue.vue";
import proportion from "./components/proportion.vue";
import energyMap from "./components/energyMap.vue";
import ranking from "./components/ranking.vue";
import peakMonth from "./components/peakMonth.vue";
import detailsText from "./components/detailsText.vue";
export default {
    components: {
      peakToday,
      peakValue,
      proportion,
      energyMap,
      ranking,
      peakMonth,
      detailsText
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
  mounted(){
      
  },
  methods:{
  }
};
</script>

<style lang="less" scoped>
    .warning-container{
        width: 100%;
        height: 94%;
        position: relative;
        .peakToday{
            height: 23%;
            width: 35%;
            // border: solid 1px white;
            position: absolute;
            overflow: hidden;
            z-index: 1;
            
        }
        .peakMonth{
            height: 23%;
            width: 35%;
            // border: solid 1px white;
            position: absolute;
            top: 24%;
            overflow: hidden;
            z-index: 1;
        }
        .peakValue{
            height: 23%;
            width: 35%;
            // border: solid 1px white;
            position: absolute;
            overflow: hidden;
            top: 48%;
            z-index: 1;
        }
        .proportion{
            height: 34%;
            width: 23.3%;
            // border: solid 1px white;
            position: absolute;
            overflow: hidden;
            top: 72%;
            z-index: 1;
        }
        .amap{
            height: 100% !important;
            width: 100%;
            position: absolute;
            // border: solid 1px white;
            overflow: hidden;
            left: 0;
        }
        .miniMap{
            height: 71% !important;
            width: 63.5% !important;
            border: solid 3px #363F71;
            position: absolute;
            left: 36%;
            
        }
        .ranking{
            position: absolute;
            overflow: hidden;
            top: 72%;
            width: 76%;
            height: 34%;
            left: 23.4%;
            z-index: 1;
            // border: solid 1px white;
        }
        .detailsText{
            width: 17%;
            height: 40%;
            // border: solid 1px white;
            position: absolute;
            left: 36%;
            z-index: 1;
            border-radius: 1vw;
            border-top-left-radius: 0vw;
            background-color: rgba(255,255,255,0.1);
        }
    }
    
</style>
<style lang="less">
    .threeCharts{
        width: 100%;
        height: 100%;
        // border: solid 1px yellow;
        display: flex;
        .peakMiniBox{
            width: 33%;
            height: 100%;
            // border: solid 1px white;
            border-right: solid 3px rgba(255,255,255,0.2);
            border-bottom: solid 3px rgba(255,255,255,0.2);
        }
        .peakCenter{
            margin-left: 1%;
        }
        .peakRight{
            margin-left: 1%;
        }
    }
    .peakBackground{
        // background-color: rgba(255,255,255,0.1) !important;
    }
</style>
