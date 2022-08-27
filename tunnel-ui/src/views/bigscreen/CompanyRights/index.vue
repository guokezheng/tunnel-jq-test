<template>
  <div class="tunnel-container">
    <div class="adminClass">
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
        <div style="height:100%;">
          <amap
            ref="amapRef"
            :placeDate="placeDate"
          ></amap>
        </div>
        <!-- 上方浮动窗 -->
        <div v-show="topShow" class="float-top">
          <i class="el-icon-s-fold top-fold-icon" @click="topShow = false"></i>
          <controlRecord />
        </div>
        <i v-show="!topShow" class="el-icon-s-unfold top-unfold-icon" @click="topShow = true"></i>
        <!-- 下方浮动窗 -->
        <div v-show="footerShow" class="float-footer">
          <i class="el-icon-s-fold footer-fold-icon" @click="footerShow = false"></i>
          <the-alarm-number />
        </div>
        <i v-show="!footerShow" class="el-icon-s-unfold footer-unfold-icon" @click="footerShow = true"></i>
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
  </div>
</template>

<script>
import Amap from "./components/amap.vue";
import armamentarium from "./components/armamentarium.vue";
import TunnelEvent from "./components/tunnelEvent.vue";
import TunnelSafetyIndex from "./components/tunnelSafetyIndex.vue";
import TunnelRanking from "./components/tunnelRanking.vue";
import TheAlarmNumber from "./components/theAlarmNumber.vue";controlRecord
import weight from "./components/weight.vue";
import controlRecord from "./components/controlRecord.vue";
import EquipmentPm from "./components/EquipmentPm.vue";

export default {
  components: {
    Amap,
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
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "姚家峪隧道",
            position: [117.822164, 36.493427],
            extData: {
              name: "姚家峪隧道",
              id: 0,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [240, 230, 250, 260, 255, 245]
                }, // co浓度
                trafficData: {
                  data: [22020, 25050, 23500, 25300, 26030, 24500]
                }, // 车流量
                windData: {
                  data: [6, 7, 8, 6, 8, 7]
                }, // 风速
                luminanceData: {
                  data: [5200, 4800, 5100, 5500, 4500, 5000]
                }, // 洞口亮度
                energyConsumption: {
                  name: "姚家峪隧道",
                  data: [5, 7, 8, 7, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "滨莱高速",
            position: [117.784441, 36.32409],
            extData: {
              name: "滨莱高速",
              id: 1,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [220, 210, 240, 250, 235, 265]
                }, // co浓度
                trafficData: {
                  data: [23020, 20050, 25500, 22300, 22030, 24500]
                }, // 车流量
                windData: {
                  data: [8, 6, 7, 7, 6, 7]
                }, // 风速
                luminanceData: {
                  data: [5500, 4500, 4500, 5500, 4500, 5000]
                }, // 洞口亮度
                energyConsumption: {
                  name: "滨莱高速",
                  data: [6, 7, 5, 7, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 96, // 正常
                  malfunctionVal: 1, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "中庄隧道",
            position: [121.435835, 37.079518],
            extData: {
              name: "中庄隧道",
              id: 2,
              tunnelLength: null,
              affiliation: "荣潍高速",
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255]
                }, // co浓度
                trafficData: {
                  data: [12020, 15050, 13500, 15300, 16030, 14500]
                }, // 车流量
                windData: {
                  data: [7, 7, 8, 6, 7, 7]
                }, // 风速
                luminanceData: {
                  data: [4000, 4500, 4500, 5500, 4500, 5500]
                }, // 洞口亮度
                energyConsumption: {
                  name: "中庄隧道",
                  data: [7, 7, 6, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 4
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "马公祠隧道",
            position: [117.824197, 36.472199],
            extData: {
              name: "马公祠隧道",
              id: 3,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255]
                }, // co浓度
                trafficData: {
                  data: [12420, 15650, 13800, 15500, 15030, 14800]
                }, // 车流量
                windData: {
                  data: [7, 6, 8, 6, 6, 7]
                }, // 风速
                luminanceData: {
                  data: [4800, 5500, 4700, 5200, 4600, 5000]
                }, // 洞口亮度
                energyConsumption: {
                  name: "马公祠隧道",
                  data: [7, 8, 6, 8, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 94, // 正常
                  malfunctionVal: 3, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "乐疃隧道",
            position: [117.821815, 36.457601],
            extData: {
              name: "乐疃隧道",
              id: 4,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [230, 250, 240, 245, 265, 255]
                }, // co浓度
                trafficData: {
                  data: [20320, 25050, 23500, 25800, 25030, 24000]
                }, // 车流量
                windData: {
                  data: [7, 6, 6, 6, 7, 7]
                }, // 风速
                luminanceData: {
                  data: [4600, 5300, 5500, 5200, 4900, 5200]
                }, // 洞口亮度
                energyConsumption: {
                  name: "乐疃隧道",
                  data: [8, 7, 7, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 93, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "樵岭前隧道",
            position: [117.817382, 36.448935],
            extData: {
              name: "樵岭前隧道",
              id: 5,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [223, 223, 245, 236, 264, 273]
                }, // co浓度
                trafficData: {
                  data: [23340, 25250, 23340, 22300, 26240, 25200]
                }, // 车流量
                windData: {
                  data: [5, 6, 5, 6, 7, 8]
                }, // 风速
                luminanceData: {
                  data: [3200, 4200, 5500, 5200, 4900, 5200]
                }, // 洞口亮度
                energyConsumption: {
                  name: "樵岭前隧道",
                  data: [6, 7, 6, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "佛羊岭隧道",
            position: [117.800823, 36.374613],
            extData: {
              name: "佛羊岭隧道",
              id: 6,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [252, 235, 262, 246, 226, 273]
                }, // co浓度
                trafficData: {
                  data: [26230, 24220, 24630, 26530, 25240, 25200]
                }, // 车流量
                windData: {
                  data: [5, 6, 7, 6, 6, 8]
                }, // 风速
                luminanceData: {
                  data: [5200, 5200, 5000, 4200, 4900, 5200]
                }, // 洞口亮度
                energyConsumption: {
                  name: "佛羊岭隧道",
                  data: [6, 8, 6, 8, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 91, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "洪河隧道",
            position: [117.794317, 35.745686],
            extData: {
              name: "洪河隧道",
              id: 7,
              tunnelLength: "515米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273]
                }, // co浓度
                trafficData: {
                  data: [22330, 25220, 22530, 20100, 23570, 25200]
                }, // 车流量
                windData: {
                  data: [6, 6, 7, 7, 6, 8]
                }, // 风速
                luminanceData: {
                  data: [4200, 4800, 5300, 5200, 4900, 5200]
                }, // 洞口亮度
                energyConsumption: {
                  name: "洪河隧道",
                  data: [7, 8, 6, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 90, // 正常
                  malfunctionVal: 7, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "迎春坡隧道",
            position: [117.653719, 35.329564],
            extData: {
              name: "迎春坡隧道",
              id: 8,
              tunnelLength: "973米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273]
                }, // co浓度
                trafficData: {
                  data: [22430, 23420, 23530, 22300, 23570, 25200]
                }, // 车流量
                windData: {
                  data: [6, 8, 7, 6, 6, 8]
                }, // 风速
                luminanceData: {
                  data: [3400, 4800, 5300, 5200, 4900, 5200]
                }, // 洞口亮度
                energyConsumption: {
                  name: "迎春坡隧道",
                  data: [7, 8, 7, 7, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 8, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "毓秀山隧道",
            position: [117.597113, 35.250696],
            extData: {
              name: "毓秀山隧道",
              id: 9,
              tunnelLength: "400米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273]
                }, // co浓度
                trafficData: {
                  data: [24430, 25420, 23530, 23300, 25570, 25200]
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8]
                }, // 风速
                luminanceData: {
                  data: [4400, 4800, 5300, 5200, 4900, 4500]
                }, // 洞口亮度
                energyConsumption: {
                  name: "毓秀山隧道",
                  data: [7, 8, 6, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 88, // 正常
                  malfunctionVal: 9, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "望海石隧道",
            position: [117.524916, 35.021412],
            extData: {
              name: "望海石隧道",
              id: 10,
              tunnelLength: "2145米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [245, 265, 252, 246, 236, 273]
                }, // co浓度
                trafficData: {
                  data: [23430, 25420, 24530, 24300, 23570, 25200]
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8]
                }, // 风速
                luminanceData: {
                  data: [4400, 5100, 5300, 4700, 4900, 4500]
                }, // 洞口亮度
                energyConsumption: {
                  name: "望海石隧道",
                  data: [7, 7, 5, 6, 7, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 87, // 正常
                  malfunctionVal: 10, // 故障
                  offlineVal: 3
                } // 设备
              }
            }
          },
          {
            icon:
              "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "龙山寨隧道",
            position: [117.53021, 35.005675],
            extData: {
              name: "龙山寨隧道",
              id: 11,
              tunnelLength: "515米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [265, 245, 252, 246, 256, 273]
                }, // co浓度
                trafficData: {
                  data: [25430, 25420, 23530, 22300, 23570, 25200]
                }, // 车流量
                windData: {
                  data: [6, 7, 6, 6, 7, 5]
                }, // 风速
                luminanceData: {
                  data: [5400, 5100, 5300, 4700, 5100, 4500]
                }, // 洞口亮度
                energyConsumption: {
                  name: "龙山寨隧道",
                  data: [7, 8, 5, 6, 8, 6]
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 5
                } // 设备
              }
            }
          }
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
        ]
      },
      switchingMapTimeout: null, // 切换按钮定时器
      userRight: "admin",
      userRightData: {
        equipmentData: {
          normalVal: 1088,
          malfunctionVal: 60,
          offlineVal: 52
        }
      },
      footerShow: true, // 控制下方浮动窗
      topShow: true, // 控制上方浮动窗
    };
  },
  created() {},
  mounted() {},
  methods: {
    // 切换地图显示地区
    switchingMap() {
      clearTimeout(this.switchingMapTimeout);
      this.switchingMapTimeout = setTimeout(async () => {
        if (this.tunnelName == "山东") {
          this.placeDate = {
            name: "四川",
            type: "province",
            centralPoint: [103.248235, 30.637441],
            markersList: [
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "四川：长山隧道",
                position: [104.284547, 29.443451],
                extData: {
                  id: 0,
                  name: "四川：长山隧道",
                  tunnelLength: "10174米",
                  affiliation: "隆汉高速",
                  echartsDate: {
                    COData: {
                      data: [235, 245, 232, 246, 256, 243]
                    }, // co浓度
                    trafficData: {
                      data: [22430, 23420, 23530, 25300, 23570, 25200]
                    }, // 车流量
                    windData: {
                      data: [5, 7, 6, 5, 6, 5]
                    }, // 风速
                    luminanceData: {
                      data: [5400, 5100, 5300, 4700, 5100, 4500]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "长山隧道",
                      data: [5, 8, 5, 7, 8, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 88, // 正常
                      malfunctionVal: 7, // 故障
                      offlineVal: 5,
                    } // 设备
                  }
                }
              }
            ],
            incidentVal: 10, // 事件
            earlyWarningVal: 5, // 预警
            malfunctionVal: 3 // 故障
          };
        } else {
          this.placeDate = {
            name: "山东",
            type: "province", // province代表省份, city代表城市
            centralPoint: [118.549381, 36.382265], // 中心点坐标
            markersList: [
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "姚家峪隧道",
                position: [117.822164, 36.493427],
                extData: {
                  name: "姚家峪隧道",
                  id: 0,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [240, 230, 250, 260, 255, 245]
                    }, // co浓度
                    trafficData: {
                      data: [22020, 25050, 23500, 25300, 26030, 24500]
                    }, // 车流量
                    windData: {
                      data: [6, 7, 8, 6, 8, 7]
                    }, // 风速
                    luminanceData: {
                      data: [5200, 4800, 5100, 5500, 4500, 5000]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "姚家峪隧道",
                      data: [5, 7, 8, 7, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 5, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "滨莱高速",
                position: [117.784441, 36.32409],
                extData: {
                  name: "滨莱高速",
                  id: 1,
                  tunnelLength: null,
                  affiliation: null,
                  echartsDate: {
                    COData: {
                      data: [220, 210, 240, 250, 235, 265]
                    }, // co浓度
                    trafficData: {
                      data: [23020, 20050, 25500, 22300, 22030, 24500]
                    }, // 车流量
                    windData: {
                      data: [8, 6, 7, 7, 6, 7]
                    }, // 风速
                    luminanceData: {
                      data: [5500, 4500, 4500, 5500, 4500, 5000]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "滨莱高速",
                      data: [6, 7, 5, 7, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 96, // 正常
                      malfunctionVal: 1, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "中庄隧道",
                position: [121.435835, 37.079518],
                extData: {
                  name: "中庄隧道",
                  id: 2,
                  tunnelLength: null,
                  affiliation: "荣潍高速",
                  echartsDate: {
                    COData: {
                      data: [240, 240, 250, 250, 265, 255]
                    }, // co浓度
                    trafficData: {
                      data: [12020, 15050, 13500, 15300, 16030, 14500]
                    }, // 车流量
                    windData: {
                      data: [7, 7, 8, 6, 7, 7]
                    }, // 风速
                    luminanceData: {
                      data: [4000, 4500, 4500, 5500, 4500, 5500]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "中庄隧道",
                      data: [7, 7, 6, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 4, // 故障
                      offlineVal: 4
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "马公祠隧道",
                position: [117.824197, 36.472199],
                extData: {
                  name: "马公祠隧道",
                  id: 3,
                  tunnelLength: null,
                  affiliation: null,
                  echartsDate: {
                    COData: {
                      data: [240, 240, 250, 250, 265, 255]
                    }, // co浓度
                    trafficData: {
                      data: [12420, 15650, 13800, 15500, 15030, 14800]
                    }, // 车流量
                    windData: {
                      data: [7, 6, 8, 6, 6, 7]
                    }, // 风速
                    luminanceData: {
                      data: [4800, 5500, 4700, 5200, 4600, 5000]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "马公祠隧道",
                      data: [7, 8, 6, 8, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 94, // 正常
                      malfunctionVal: 3, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "乐疃隧道",
                position: [117.821815, 36.457601],
                extData: {
                  name: "乐疃隧道",
                  id: 4,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [230, 250, 240, 245, 265, 255]
                    }, // co浓度
                    trafficData: {
                      data: [20320, 25050, 23500, 25800, 25030, 24000]
                    }, // 车流量
                    windData: {
                      data: [7, 6, 6, 6, 7, 7]
                    }, // 风速
                    luminanceData: {
                      data: [4600, 5300, 5500, 5200, 4900, 5200]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "乐疃隧道",
                      data: [8, 7, 7, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 93, // 正常
                      malfunctionVal: 4, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "樵岭前隧道",
                position: [117.817382, 36.448935],
                extData: {
                  name: "樵岭前隧道",
                  id: 5,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [223, 223, 245, 236, 264, 273]
                    }, // co浓度
                    trafficData: {
                      data: [23340, 25250, 23340, 22300, 26240, 25200]
                    }, // 车流量
                    windData: {
                      data: [5, 6, 5, 6, 7, 8]
                    }, // 风速
                    luminanceData: {
                      data: [3200, 4200, 5500, 5200, 4900, 5200]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "樵岭前隧道",
                      data: [6, 7, 6, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 92, // 正常
                      malfunctionVal: 5, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "佛羊岭隧道",
                position: [117.800823, 36.374613],
                extData: {
                  name: "佛羊岭隧道",
                  id: 6,
                  tunnelLength: null,
                  affiliation: "滨莱高速",
                  echartsDate: {
                    COData: {
                      data: [252, 235, 262, 246, 226, 273]
                    }, // co浓度
                    trafficData: {
                      data: [26230, 24220, 24630, 26530, 25240, 25200]
                    }, // 车流量
                    windData: {
                      data: [5, 6, 7, 6, 6, 8]
                    }, // 风速
                    luminanceData: {
                      data: [5200, 5200, 5000, 4200, 4900, 5200]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "佛羊岭隧道",
                      data: [6, 8, 6, 8, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 91, // 正常
                      malfunctionVal: 6, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "洪河隧道",
                position: [117.794317, 35.745686],
                extData: {
                  name: "洪河隧道",
                  id: 7,
                  tunnelLength: "515米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273]
                    }, // co浓度
                    trafficData: {
                      data: [22330, 25220, 22530, 20100, 23570, 25200]
                    }, // 车流量
                    windData: {
                      data: [6, 6, 7, 7, 6, 8]
                    }, // 风速
                    luminanceData: {
                      data: [4200, 4800, 5300, 5200, 4900, 5200]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "洪河隧道",
                      data: [7, 8, 6, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 90, // 正常
                      malfunctionVal: 7, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "迎春坡隧道",
                position: [117.653719, 35.329564],
                extData: {
                  name: "迎春坡隧道",
                  id: 8,
                  tunnelLength: "973米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273]
                    }, // co浓度
                    trafficData: {
                      data: [22430, 23420, 23530, 22300, 23570, 25200]
                    }, // 车流量
                    windData: {
                      data: [6, 8, 7, 6, 6, 8]
                    }, // 风速
                    luminanceData: {
                      data: [3400, 4800, 5300, 5200, 4900, 5200]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "迎春坡隧道",
                      data: [7, 8, 7, 7, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 89, // 正常
                      malfunctionVal: 8, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "毓秀山隧道",
                position: [117.597113, 35.250696],
                extData: {
                  name: "毓秀山隧道",
                  id: 9,
                  tunnelLength: "400米",
                  affiliation: "滨台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 255, 262, 256, 236, 273]
                    }, // co浓度
                    trafficData: {
                      data: [24430, 25420, 23530, 23300, 25570, 25200]
                    }, // 车流量
                    windData: {
                      data: [6, 7, 7, 6, 7, 8]
                    }, // 风速
                    luminanceData: {
                      data: [4400, 4800, 5300, 5200, 4900, 4500]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "毓秀山隧道",
                      data: [7, 8, 6, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 88, // 正常
                      malfunctionVal: 9, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "望海石隧道",
                position: [117.524916, 35.021412],
                extData: {
                  name: "望海石隧道",
                  id: 10,
                  tunnelLength: "2145米",
                  affiliation: "新台高速",
                  echartsDate: {
                    COData: {
                      data: [245, 265, 252, 246, 236, 273]
                    }, // co浓度
                    trafficData: {
                      data: [23430, 25420, 24530, 24300, 23570, 25200]
                    }, // 车流量
                    windData: {
                      data: [6, 7, 7, 6, 7, 8]
                    }, // 风速
                    luminanceData: {
                      data: [4400, 5100, 5300, 4700, 4900, 4500]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "望海石隧道",
                      data: [7, 7, 5, 6, 7, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 87, // 正常
                      malfunctionVal: 10, // 故障
                      offlineVal: 3
                    } // 设备
                  }
                }
              },
              {
                icon:
                  "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
                title: "龙山寨隧道",
                position: [117.53021, 35.005675],
                extData: {
                  name: "龙山寨隧道",
                  id: 11,
                  tunnelLength: "515米",
                  affiliation: "新台高速",
                  echartsDate: {
                    COData: {
                      data: [265, 245, 252, 246, 256, 273]
                    }, // co浓度
                    trafficData: {
                      data: [25430, 25420, 23530, 22300, 23570, 25200]
                    }, // 车流量
                    windData: {
                      data: [6, 7, 6, 6, 7, 5]
                    }, // 风速
                    luminanceData: {
                      data: [5400, 5100, 5300, 4700, 5100, 4500]
                    }, // 洞口亮度
                    energyConsumption: {
                      name: "龙山寨隧道",
                      data: [7, 8, 5, 6, 8, 6]
                    }, // 年度能耗
                    equipmentData: {
                      normalVal: 89, // 正常
                      malfunctionVal: 6, // 故障
                      offlineVal: 5
                    } // 设备
                  }
                }
              }
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
            malfunctionVal: 10 // 故障
          };
        }
        this.echartsDate = this.placeDate.markersList[0].extData.echartsDate;
        this.tunnelName = this.placeDate.name;
        await this.$refs.amapRef.setMarkersList(this.placeDate.markersList);
        this.$refs.amapRef.initNetTick();
      }, 500);
    },
    // 点击marker改变权限
    updateUserRight() {
      this.userRight = 'admina'
    }
  }
};
</script>

<style lang="less" scoped>
.tunnel-container {
  width: 100%;
  height: 100%;
  
  .adminClass {
    display: flex;
    height: 100%;
    justify-content: space-between;
    color: white !important;
    > div {
      height: 100%;
    }
    div {
      box-sizing: border-box;
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

      .float-top {
        position: absolute;
        width: 40%;
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
      .top-unfold-icon::before, .top-fold-icon::before {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%) rotate(90deg);
        -webkit-transform: translate(-50%, -50%) rotate(90deg);
      }

      .float-footer {
        position: absolute;
        width: 100%;
        height: 30%;
        left: 0;
        bottom: 0;
        // border: 1px solid #027dec;
        border-radius: 1vw 1vw 0 0;
        background-color: rgba(4, 15, 77, 0.5);

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
      .footer-unfold-icon::before, .footer-fold-icon::before {
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
        margin-bottom: 0.5vw;
      }
      .adminClass-right-body {
        height: 30%;
        margin-bottom: 0.5vw;
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
</style>
