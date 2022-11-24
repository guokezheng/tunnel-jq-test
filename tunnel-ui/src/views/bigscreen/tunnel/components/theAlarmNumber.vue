<template>
  <div
    class="theAlarmNumber-container"
    v-loading.lock="fullscreenLoading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="#040f4e"
  >
    <div class="title">
      本月预警事件
      <!-- {{ currentData.name }} -->
      <!-- <span
        style="margin-left: 0.5vw; font-size: 0.8vw; cursor: pointer"
        @click="SwitchingEvents"
        >切换</span
      > -->
    </div>
    <div class="box">
      <div ref="echartsBox" class="box-left" id="theAlarmNumber"></div>
      <div class="box-center UnificationBox">
        <div class="progressBar progress">
          <p>处理中：</p>
          <span>{{ eventProportion[0].percentage }}%</span>
        </div>
        <div class="progressBarNot progress">
          <p>已处理：</p>
          <span>{{ eventProportion[1].percentage }}%</span>
        </div>
        <div class="progressBarNot progress">
          <p>忽略：</p>
          <span>{{ eventProportion[2].percentage }}%</span>
        </div>
        <div class="progressBarNot progress">
          <p>未处理：</p>
          <span>{{ eventProportion[3].percentage }}%</span>
        </div>
      </div>
      <div class="box-right">
        <div class="listHeader">
          <el-row
            type="flex"
            style="font-size: 0.8vw; background-color: rgba(255, 255, 255, 0.2)"
          >
            <el-col style="width: 20vw; padding-left: 0.4vw">隧道</el-col>
            <el-col>发生时间</el-col>
            <el-col>详细信息</el-col>
            <el-col style="width: 20vw">处理情况</el-col>
          </el-row>
        </div>
        <vue-seamless-scroll
          :class-option="defaultOption"
          class="listContent"
          :data="currentData.listData"
        >
          <el-row
            type="flex"
            v-for="(item, index) in currentData.listData"
            :key="index"
            :style="{
              backgroundColor:
                (index + 1) % 2 == 0
                  ? 'rgba(255, 255, 255,0.1)'
                  : 'rgba(255, 255, 255,0)',
            }"
          >
            <el-col style="width: 20vw; padding-left: 0.4vw">{{
              item.tunnelName
            }}</el-col>
            <el-col>{{ item.startTime }}</el-col>
            <el-col style="padding-right: 1vw">{{
              item.eventDescription
            }}</el-col>
            <el-col style="width: 20vw">{{
              item.eventState == 0
                ? "处理中"
                : item.eventState == 1
                ? "已处理"
                : item.eventState == 2
                ? 忽略
                : "未处理"
            }}</el-col>
          </el-row>
        </vue-seamless-scroll>
      </div>
    </div>
  </div>
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
import { getEventWarning } from "@/api/business/new";
import * as echarts from "echarts";
import "echarts-liquidfill";
import elementResizeDetectorMaker from "element-resize-detector";
export default {
  props: {},
  data() {
    return {
      eventProportion: "",
      fullscreenLoading: false,
      currentData: { all: 100 },
      hasDisposal: "",
      notDDisposedOf: "",
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: this.currentData.listData.length, // 开始无缝滚动的数据量 this.dataList.length
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
    this.$nextTick(() => {
      this.setData();
    });
  },
  mounted() {
    // this.setData();
    this.watchSize();
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      console.log(Dom);
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    setData(val) {
      this.fullscreenLoading = true;
      this.currentData = {};
      getEventWarning()
        .then((res) => {
          console.log(res.data);
          this.currentData.listData = res.data.list;
          this.eventProportion = res.data.eventProportion;
          let data = ["处理中", "已处理", "忽略", "未处理"];
          data.forEach((items) => {
            this.eventProportion.forEach((element) => {
              element.name = items;
              element.value = element.percentage;
            });
          });
        })
        .then((res) => {
          this.$nextTick(() => {
            this.initEchart();
          });
        });

      setTimeout(() => {
        this.fullscreenLoading = false;
      }, 500);
    },
    initEchart() {
      // var object = { ...this.currentData };
      var myChart = echarts.init(document.getElementById("theAlarmNumber"));
      // var fontColor = "#fff";
      // let noramlSize = 16;
      var datas = {
        value: this.eventProportion[1].percentage,
        name: "已处理",
        company: "%",
        ringColor: [
          {
            offset: 0,
            color: "#02d6fc", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "#50fc73", // 100% 处的颜色
          },
        ],
      };
      var option = {
        title: {
          text: datas.value + datas.company,
          x: "center",
          y: "center",
          textStyle: {
            fontWeight: "normal",
            color: "#fdfeff",
            fontSize: "24",
          },
        },
        //  color: ['#282c40'],
        legend: {
          show: false,
          data: [],
        },
        color: ["#282c40", "#29aa82", "#f1b144"],
        tooltip: {
          trigger: "item",
          padding: [10, 10, 10, 10],
          formatter: "{b} :<br/> {d}%",
        },
        series: [
          {
            name: "Line 1",
            type: "pie",
            clockWise: true,
            radius: ["45%", "52%"],
            itemStyle: {
              normal: {
                label: {
                  show: false,
                },
                labelLine: {
                  show: false,
                },
              },
            },
            hoverAnimation: false,
            data: [
              {
                value: this.eventProportion[1].percentage,
                name: "已处理",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: datas.ringColor,
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[3].percentage,
                name: "未处理",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: datas.ringColor,
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[2].percentage,
                name: "忽略",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: datas.ringColor,
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[0].percentage,
                name: "处理中",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: datas.ringColor,
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
            ],
          },
          {
            name: "",
            type: "pie",
            radius: ["59%", "69%"],
            center: ["50%", "50%"],
            label: {
              show: false,
              fontSize: 13,
              color: "#333",
            },
            labelLine: {
              show: false,
              // length: 6,
              // length2: 15
            },
            data: [
              {
                value: this.eventProportion[1].percentage,
                name: "已处理",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: "#7afcbc", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#1dcc78", // 100% 处的颜色
                        },
                      ],
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[3].percentage,
                name: "未处理",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: "#f9bf1e", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#f74001", // 100% 处的颜色
                        },
                      ],
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[2].percentage,
                name: "忽略",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: "#12cff6", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#80f7aa", // 100% 处的颜色
                        },
                      ],
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
              {
                value: this.eventProportion[0].percentage,
                name: "处理中",
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: "#8af7ea", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#2acfbe", // 100% 处的颜色
                        },
                      ],
                    },
                    label: {
                      show: false,
                    },
                    labelLine: {
                      show: false,
                    },
                  },
                },
              },
            ],
          },
          // {
          //     type: 'pie',
          //     radius: ['36%', '43%'],
          //     center: ['50%', '50%'],
          //     silent: true,
          //     data: [{
          //         name: '',
          //         value: 1,
          //     }]
          // }
        ],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="less" scoped>
.theAlarmNumber-container {
  font-size: 0.8vw;
  height: 100%;
  .title {
    margin-top: 0;
    height: 18%;
    display: flex;
    align-items: flex-end;
    color: #fff;
  }
  .box {
    width: 100%;
    height: 82%;
    display: flex;
    .box-left {
      width: 18%;
      height: 100%;
      padding-top: 1vw;
    }
    .box-center {
      width: 20%;
      height: 100%;
      display: flex;
      // flex-direction: column;
      justify-content: space-between;
      padding-right: 0.5vw;
      align-items: center;
      height: 205px;
      position: relative;
      top: 4%;
      flex-flow: wrap;
      .title {
        margin-bottom: 0.5vw;
      }
      /deep/ .progressBar {
        .el-progress-bar__outer {
          background-color: #040f4e;
        }
        .el-progress-bar__inner {
          background-color: #5470c6;
        }
      }
      /deep/ .progressBarNot {
        .el-progress-bar__outer {
          background-color: #040f4e;
        }
        .el-progress-bar__inner {
          background-color: #91cc75;
        }
      }
    }
    .box-right {
      width: 60%;
      height: 100%;
      padding: 0.5vw;
      overflow: hidden;

      .listContent {
        .el-row {
          height: 100%;
          width: 100%;
          .el-col {
            display: flex;
            align-items: center;
          }
        }
      }
    }
  }
}
.UnificationBox {
  .progress {
    p {
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 16px;
      margin-bottom: 20px;
    }

    span {
      display: block;
      text-align: center;
      width: 100%;
      font-size: 24px;
    }
  }
  .progressBar {
    p::before {
      margin-right: 10px;
      display: block;
      content: "";
      width: 10px;
      height: 10px;
      border-radius: 35px;
      background-color: #00f5fd;
    }
    span {
      color: #00f5fd;
    }
  }
  .progressBarNot {
    p::before {
      margin-right: 10px;
      display: block;
      content: "";
      width: 10px;
      height: 10px;
      border-radius: 35px;
      background-color: #4affb4;
    }
    span {
      color: #4affb4;
    }
  }
}
</style>
