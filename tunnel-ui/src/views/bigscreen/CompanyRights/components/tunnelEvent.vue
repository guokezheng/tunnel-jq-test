<template>
  <div class="tunnelEvent-container">
    <div class="contentTitle">
      近30日隧道预警
      <i>Tunnel event</i>
    </div>
    <div id="echarts-Box" class="echarts-Box"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      listData: [
        {
          id: 0,
          name: "姚家峪隧道",
          time: "2021-10-3 14:30:30",
          content: "发生一起交通拥堵导致的车祸事件，有数人轻伤"
        },
        {
          id: 1,
          name: "毓秀山隧道",
          time: "2021-10-3 14:30:30",
          content: "发生一起闯红灯导致的车祸事件，有1人轻伤"
        },
        {
          id: 2,
          name: "洪山隧道",
          time: "2021-10-3 14:30:30",
          content: "发生一起交通拥堵导致的车祸事件，有数人轻伤"
        },
        {
          id: 3,
          name: "望海石隧道",
          time: "2021-10-3 14:30:30",
          content: "发生一起车辆闯红灯事件"
        }
      ]
    };
  },
  mounted() {
    this.electricRanking()
  },
  methods: {
    initChart() {
      var chartDom = document.getElementById("echarts-Box");
      var myChart = echarts.init(chartDom);
      var option;

      var xData = (function() {
        var data = [];
        for (var i = 1; i < 31; i++) {
          data.push(i + "日");
        }
        return data;
      })();

      option = {
        // backgroundColor: "#1A1835",

        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff"
            }
          },
          confine: true,
        },
        grid: {
          borderWidth: 0,
          top: 60,
          bottom: 95,
          left: 40,
          textStyle: {
            color: "#fff"
          }
        },
        // legend: {
        //   x: "46%",
        //   top: "11%",
        //   textStyle: {
        //     color: "#90979c"
        //   },
        //   data: ["预警量"]
        // },
        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: "rgba(204,187,225,0.5)"
              }
            },
            splitLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            data: xData
          }
        ],

        yAxis: [
          {
            type: "value",
            splitLine: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: "rgba(204,187,225,0.5)"
              }
            },
            nameTextStyle: {
              lineHeight: 15
            }
          }
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#5B3AAE"
            },
            textStyle: {
              color: "rgba(204,187,225,0.5)"
            },
            fillerColor: "rgba(67,55,160,0.4)",
            borderColor: "rgba(204,187,225,0.5)"
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35
          }
        ],
        series: [
          {
            name: "预警量",
            type: "line",
            symbolSize: 10,
            symbol: "circle",
            itemStyle: {
              color: "#6f7de3"
            },
            markPoint: {
              label: {
                normal: {
                  textStyle: {
                    color: "#fff"
                  }
                }
              },
              data: [
                {
                  type: "max",
                  name: "最大值"
                },
                {
                  type: "min",
                  name: "最小值"
                }
              ]
            },
            data: [
              509,
              917,
              2455,
              2610,
              2719,
              3033,
              3044,
              3085,
              2708,
              2809,
              2117,
              2000,
              1455,
              1210,
              719,
              733,
              944,
              2285,
              2208,
              3372,
              3936,
              3693,
              2962,
              2810,
              3519,
              2455,
              2610,
              2719,
              2484,
              2078
            ]
          }
        ]
      };

      option && myChart.setOption(option);
    },
    electricRanking() {
      var electricRanking = echarts.init(
        document.getElementById("echarts-Box")
      );
      var xData = (function() {
        var data = [];
        for (var i = 1; i < 31; i++) {
          data.push(i + "日");
        }
        return data;
      })();

      var option = {
        color: ["#00C8FF"],
        tooltip: {
          trigger: "axis",
          backgroundColor: "rgba(0,0,0,0.8)", //通过设置rgba调节背景颜色与透明度
          color: "white",
          borderWidth: "1",
          borderColor: "black",
          textStyle: {
            color: "white"
          },
          formatter: function(params) {
            var str =
              '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#0B7EC4"></span>' +
              name +
              "<br>";
            str += params[0].axisValue + " : " + params[0].value + "<br>";

            return str;
          }
        },
        grid: {
          left: "10%",
          right: "10%",
          bottom: "10%",
          top: "14%",
          containLabel: true
        },
        xAxis: [
          {
            type: "category",
            data: xData,
            axisTick: {
              alignWithLabel: true
            },
            nameTextStyle: {
              //关键代码
              padding: [30, 0, 0, -10]
            },
            axisLine: {
              lineStyle: {
                color: "#003476",
                width: 1
              }
            },
            show: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF"
              }
            }
          }
        ],
        yAxis: [
          {
            type: "value",
            minInterval: 1,
            splitLine: {
              show: false
            },
            axisTick: {
              //y轴刻度线
              show: false
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
                width: 1,
                type: "solid"
              }
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF"
              }
            }
          }
        ],
        series: [
          {
            type: "bar",
            barWidth: "60%",
            showSymbol: true,
            data: [20, 35, 28, 16, 12, 17, 15, 20, 15, 10, 17, 15, 15, 18, 16, 10, 7, 15, 10, 10, 10, 17, 15, 11, 10, 7, 6, 7, 5, 6],
            itemStyle: {
              //柱形图圆角，鼠标移上去效果，如果只是一个数字则说明四个参数全部设置为那么多
              normal: {
                //柱形图圆角，初始化效果
                barBorderRadius: [5, 5, 0, 0],
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: "#002a5e" // 0% 处的颜色
                    },
                    {
                      offset: 0.6,
                      color: "#007bc2" // 60% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#9aaadd" // 100% 处的颜色
                    }
                  ],
                  false
                )
              }
            }
          }
        ]
      };
      electricRanking.setOption(option);
    }
  }
};
</script>

<style lang="less" scoped>
.tunnelEvent-container {
  width: 100%;
  height: 100%;
  font-size: 0.8vw;
  overflow: hidden;
  #echarts-Box {
    height: 90%;
    width: 100%;
    > div {
      width: 100%;
      height: 100%;
      canvas {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>
