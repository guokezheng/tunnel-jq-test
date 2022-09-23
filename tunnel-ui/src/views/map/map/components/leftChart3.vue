<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>在途车辆客货比</div>
      <div>vehicles in reansit</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <div class="chartBox">
      <div style="width: 50%; height: 100%">
        <div id="dashboard1"></div>
        <div class="bottomText">
          <div style="background:#0179ff"></div>
          <div>客车:</div>
          <div style="color:#0179ff">73120</div>
          <div>辆</div>
        </div>
      </div>
      <div style="width: 50%; height: 100%;transform: translateX(-18px);">
        <div id="dashboard2"></div>
        <div class="bottomText">
          <div style="background:#3FD7FE"></div>
          <div>货车:</div>
          <div style="color:#3FD7FE">73120</div>
          <div>辆</div>
        </div>
      </div>
    </div>
  </div>
</template>
  <script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      src: require("@/assets/cloudControl/dialogHeader.png"),
      iconSrc: require("@/assets/icons/energyIcon.png"),
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart1();
      this.initChart2();

    });
  },
  methods: {
    initChart1() {
      var dashboard1 = echarts.init(document.getElementById("dashboard1"));
      //要改变的数据
      var getvalue = 50.12;

      var getmax = 100;
      let pre = 500 / 1100;

      var option = {
        grid: {
          top: 0,
          bottom: 0,
        },
        title: {
          show: true,
          text: getvalue + "%",
          textStyle: {
            color: "#fff",
            fontSize: 14,
          },
          x: "center",
          y: "24%",
          padding: 50,
        },
        angleAxis: {
          show: false,
          max: (getmax * 360) / 270, //-45度到225度，二者偏移值是270度除360度
          type: "value",
          startAngle: 225, //极坐标初始角度
          splitLine: {
            show: false,
          },
        },
        radiusAxis: {
          show: false,
          type: "category",
        },

        series: [
          {
            type: "gauge",
            radius: "70%",
            detail: {
              show: false,
              formatter: "",
              color: "#F7B500",
            },
            title: {
              show: true,
              offsetCenter: ["0", "45"], //在途客车的位置
              color: "#fff",
              fontSize: 14,
            },
            pointer: {
              show: false,
            },
            axisLabel: {
              // 刻度标签。
              show: false, // 是否显示标签,默认 true。
            },
            axisTick: {
              // 刻度(线)样式。
              show: false, // 是否显示刻度(线),默认 true。
            },
            splitLine: {
              show: false,
            },
            center: ["center", "60%"], // 仪表位置
            axisLine: {
              show: true,
              lineStyle: {
                width: 10, //数据渐变环宽度
                shadowBlur: 0,
                color: [
                  [
                    pre,
                    {
                      type: "linear",
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [
                        {
                          offset: 0,
                          color: "#0179FF", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#002C48", // 100% 处的颜色
                        },
                      ],
                      global: false, // 缺省为 false
                    },
                  ],
                  [1, "#002C48"],
                ],
              },
            },
            max: 100,
            splitNumber: 20,
            data: [
              {
                value: pre * 100,
                name: "在途客车",
              },
            ],
          },
          {
            name: "内层渐变圆环",
            type: "gauge",
            radius: "52%",
            center: ["center", "60%"], // 圆环位置
            splitNumber: 100,
            axisLine: {
              lineStyle: {
                color: [
                  [
                    100,
                    new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                      {
                        offset: 0,
                        color: "rgba(1,170,253,0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(1,170,253,0.7)",
                      },
                      {
                        offset: 1,
                        color: "rgba(1,170,253,1)",
                      },
                    ]),
                  ],
                  [1, "#f00"],
                ],
                width: 1,
              },
            },
            axisLabel: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            itemStyle: {
              show: false,
            },
            detail: {
              show: false,
            },
            pointer: {
              show: false,
            },
            animationDuration: 4000,
          },

          {
            name: "外部刻度虚线",
            type: "gauge",
            center: ["center", "60%"],
            radius: "90%",
            min: 0, //最小刻度
            max: 100, //最大刻度
            splitNumber: 10, //刻度数量
            startAngle: 225,
            endAngle: -45,
            axisLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: [[1, "rgba(0,0,0,0)"]],
              },
            }, //仪表盘轴线
            axisLabel: {
              show: false,
            }, //刻度标签。
            axisTick: {
              show: true,
              splitNumber: 6, //虚线间隔数量 越大越趋向于实线
              lineStyle: {
                color: "#0179FF", //用颜色渐变函数不起作用
                width: 2, //虚线长度
              },
              length: 1, //虚线高度
            }, //刻度样式
            splitLine: {
              show: false,
            }, //分隔线样式
            detail: {
              show: false,
            },
            pointer: {
              show: false,
            },
          },
          {
            // 底部
            name: "两端线",
            type: "pie",
            radius: ["50%", "76%"],
            center: ["center", "60%"],
            startAngle: 225,
            endAngle: -45,
            labelLine: {
              show: false,
            },
            z: 15,
            silent: true,
            label: {
              show: false,
            },
            data: [
              {
                value: 5,
                itemStyle: {
                  color: "#4698EC",
                },
              },
              {
                value: 744,
                itemStyle: {
                  color: "transparent",
                },
              },
              {
                value: 5,
                itemStyle: {
                  color: "#4698EC",
                },
              },
              {
                value: 250,
                itemStyle: {
                  color: "transparent",
                },
              },
            ],
          },
        ],
      };

      dashboard1.setOption(option);
      window.addEventListener("resize", function () {
        dashboard1.resize();
      });
    },
    initChart2() {
      var dashboard2 = echarts.init(document.getElementById("dashboard2"));
      //要改变的数据
      var getvalue = 30.66;

      var getmax = 100;
      let pre = 500 / 1100;

      var option = {
        grid: {
          top: 0,
          bottom: 0,
        },
        title: {
          show: true,
          text: getvalue + "%",
          textStyle: {
            color: "#fff",
            fontSize: 14,
          },
          x: "center",
          y: "24%",
          padding: 50,
        },
        angleAxis: {
          show: false,
          max: (getmax * 360) / 270, //-45度到225度，二者偏移值是270度除360度
          type: "value",
          startAngle: 225, //极坐标初始角度
          splitLine: {
            show: false,
          },
        },
        radiusAxis: {
          show: false,
          type: "category",
        },

        series: [
          {
            type: "gauge",
            radius: "70%",
            detail: {
              show: false,
              formatter: "",
              color: "#F7B500",
            },
            title: {
              show: true,
              offsetCenter: ["0", "45"], //在途客车的位置
              color: "#fff",
              fontSize: 14,
            },
            pointer: {
              show: false,
            },
            axisLabel: {
              // 刻度标签。
              show: false, // 是否显示标签,默认 true。
            },
            axisTick: {
              // 刻度(线)样式。
              show: false, // 是否显示刻度(线),默认 true。
            },
            splitLine: {
              show: false,
            },
            center: ["center", "60%"], // 仪表位置
            axisLine: {
              show: true,
              lineStyle: {
                width: 10, //数据渐变环宽度
                shadowBlur: 0,
                color: [
                  [
                    pre,
                    {
                      type: "linear",
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [
                        {
                          offset: 0,
                          color: "#3FD7FE", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#002C48", // 100% 处的颜色
                        },
                      ],
                      global: false, // 缺省为 false
                    },
                  ],
                  [1, "#002C48"],
                ],
              },
            },
            max: 100,
            splitNumber: 20,
            data: [
              {
                value: pre * 100,
                name: "在途货车",
              },
            ],
          },
          {
            name: "内层渐变圆环",
            type: "gauge",
            radius: "52%",
            center: ["center", "60%"], // 圆环位置
            splitNumber: 100,
            axisLine: {
              lineStyle: {
                color: [
                  [
                    100,
                    new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                      {
                        offset: 0,
                        color: "rgba(1,170,253,0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(1,170,253,0.7)",
                      },
                      {
                        offset: 1,
                        color: "rgba(1,170,253,1)",
                      },
                    ]),
                  ],
                  [1, "#f00"],
                ],
                width: 1,
              },
            },
            axisLabel: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            itemStyle: {
              show: false,
            },
            detail: {
              show: false,
            },
            pointer: {
              show: false,
            },
            animationDuration: 4000,
          },

          {
            name: "外部刻度虚线",
            type: "gauge",
            center: ["center", "60%"],
            radius: "90%",
            min: 0, //最小刻度
            max: 100, //最大刻度
            splitNumber: 10, //刻度数量
            startAngle: 225,
            endAngle: -45,
            axisLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: [[1, "rgba(0,0,0,0)"]],
              },
            }, //仪表盘轴线
            axisLabel: {
              show: false,
            }, //刻度标签。
            axisTick: {
              show: true,
              splitNumber: 6, //虚线间隔数量 越大越趋向于实线
              lineStyle: {
                color: "#0179FF", //用颜色渐变函数不起作用
                width: 2, //虚线长度
              },
              length: 1, //虚线高度
            }, //刻度样式
            splitLine: {
              show: false,
            }, //分隔线样式
            detail: {
              show: false,
            },
            pointer: {
              show: false,
            },
          },
          {
            // 底部
            name: "两端线",
            type: "pie",
            radius: ["50%", "76%"],
            center: ["center", "60%"],
            startAngle: 225,
            endAngle: -45,
            labelLine: {
              show: false,
            },
            z: 15,
            silent: true,
            label: {
              show: false,
            },
            data: [
              {
                value: 5,
                itemStyle: {
                  color: "#4698EC",
                },
              },
              {
                value: 744,
                itemStyle: {
                  color: "transparent",
                },
              },
              {
                value: 5,
                itemStyle: {
                  color: "#4698EC",
                },
              },
              {
                value: 250,
                itemStyle: {
                  color: "transparent",
                },
              },
            ],
          },
        ],
      };

      dashboard2.setOption(option);
      window.addEventListener("resize", function () {
        dashboard2.resize();
      });
    },
  },
};
</script>
  <style scoped lang="scss">
.mapCharts {
  height: 25%;
  width: 100%;
  margin-left: 18px;

  .title {
    color: white;
    width: 100%;
    height: 30px;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
    align-items: center;

    > div:nth-of-type(1) {
      margin-left: 10px;
      font-size: 14px;
    }
    > div:nth-of-type(2) {
      text-transform: uppercase;
      margin-left: 10px;
      opacity: 0.35;
      font-size: 10px;
    }
    > img {
      height: 22px;
      margin-left: 8px;
    }
  }
  .titleIcon {
    position: absolute;
    top: 0;
    right: 0;
    height: 30px;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
  .chartBox {
    width: 100%;
    height: calc(100% - 30px);
    // border: solid 1px red;
    display: flex;
    #dashboard1, #dashboard2{
      width: 100%;
      height: calc(100% - 40px);
      // border: solid 1px yellow;
      > div {
        width: 100%;
        height: 100%;
      }
    }
    .bottomText {
      height: 30px;
      display: flex;
      color: white;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      > div:nth-of-type(1) {
        width: 10px;
        height: 10px;
        border-radius: 10px;
        margin-right: 5px;
      }
      > div:nth-of-type(3) {
        margin: 0 5px;
      }
    }
  }
}
</style>
  