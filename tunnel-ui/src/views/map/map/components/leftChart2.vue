<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>重点车辆统计</div>
      <div>key vehicle statistics</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <div id="keVehicles"></div>
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
      this.initChart();
    });
  },
  methods: {
    initChart() {
      var keVehicles = echarts.init(document.getElementById("keVehicles"));
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
        //     color: this.echartsColor,
        //     fontSize: 12
        //   }
        // },
        calculable: true,
        grid: {
          top: "26%",
          left: "7%",
          right: "8%",
          bottom: "10%",
          containLabel: true,
        },
        xAxis: [
          {
            // name: "小时",
            type: "category",
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            boundaryGap: true,
            axisLabel: {
              textStyle: {
                color: "#fff",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,

              lineStyle: {
                color: "#54C1FF",
                // type:'dashed',
              },
            },
            data: ["小车", "卡车", "货车", "大巴", "面包车"],
          },
        ],
        yAxis: [
          {
            name: "辆",
            nameTextStyle: {
              color: "#fff",
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
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: "#0F1B28",
                width: 1,
                type: "dashed",
              },
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: "#fff",
                fontSize: 10,
              },
            },
          },
        ],
        series: [
          {
            name: "",
            type: "bar",
            z: 1,
            data: [28, 25, 45, 32, 30],
            barWidth: "8px",
            itemStyle: {
              normal: {
                color: function (params) {
                  var maxIndex = 2;
                  if (params.dataIndex == maxIndex) {
                    return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#FFD17B",
                      },
                      {
                        offset: 1,
                        color: "rgba(255,209,123,0.1)",
                      },
                    ]);
                  } else {
                    return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#1897E7",
                      },
                      {
                        offset: 1,
                        color: "rgba(24,151,231,0.1)",
                      },
                    ]);
                  }
                  // return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  //   { color: colors[0] },
                  //   { color: colors[1] },
                  //   { color: colors[2] },
                  // ]);
                },
              },
            },
          },
          {
            name: "",
            barGap: "-200%",
            type: "bar",
            z: 0,
            data: [50, 50, 50, 50, 50],
            barWidth: "24px",
            itemStyle: {
              normal: {
                color: "rgba(0,149,253,0.15)", // 0% 处的颜色
              },
            },
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
      keVehicles.setOption(option);
      window.addEventListener("resize", function () {
        keVehicles.resize();
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
  #keVehicles {
    width: 100%;
    height: calc(100% - 30px);
    > div {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
  