<template>
  <div class="threeCharts">
    <div class="echartsBox">
      <div class="contentTitle">
        今日峰值对比
        <i>n. proportion</i>
      </div>
      <div class="peakMiniBox" id="todayValueCharts"></div>
    </div>
    <div class="echartsBox">
      <div class="contentTitle">
        本月峰值对比
        <i>n. proportion</i>
      </div>
      <div class="peakCenter peakMiniBox" id="monthValueCharts"></div>
    </div>
    <div class="echartsBox">
      <div class="contentTitle">
        今年峰值对比
        <i>n. proportion</i>
      </div>
      <div class="peakRight peakMiniBox" id="yearValueCharts"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  data() {
    return {};
  },
  mounted() {
    this.todayValueCharts();
    this.monthValueCharts();
    this.yearValueCharts();
  },
  methods: {
    todayValueCharts() {
      var todayValueCharts = echarts.init(
        document.getElementById("todayValueCharts")
      );

      var option = {
        title: {
          show: true,
          //   text: "今日峰值对比",
          x: "center",
          y: "0%",
          padding: 5,
          textStyle: {
            fontSize: 14,
            color: "white",
            fontWeight: "normal",
          },
        },
        color: ["#00C8FF"],
        tooltip: {
          trigger: "axis",
          position: [20, 20],
          backgroundColor: "rgba(0,0,0,0.8)", //通过设置rgba调节背景颜色与透明度
          color: "white",
          borderWidth: "1",
          borderColor: "black",
          textStyle: {
            color: "white",
          },
          formatter: function (params) {
            var str = params[0].marker + name;
            str +=
              params[0].axisValue + "  峰值：  " + params[0].value + "<br>";

            return str;
          },
        },
        grid: {
          left: "18%",
          right: "18%",
          bottom: "10%",
          top: "28%",
          containLabel: true,
        },
        xAxis: [
          {
            name: "时",
            nameTextStyle: {
              color: "#fff",
              nameLocation: "start",
              padding: [0, 0, 0, -10],
            },
            type: "category",
            data: ["00:00", "09:00"],
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              lineStyle: {
                color: "#003476",
                width: 1,
              },
            },
            show: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            minInterval: 1,
            splitLine: {
              show: false,
            },
            name: "单位：kwh",
            nameTextStyle: {
              color: "#fff",
              nameLocation: "start",
              padding: [0, 30, 0, 0],
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
                width: 1,
                type: "solid",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        series: [
          {
            type: "bar",
            barWidth: "50%",
            showSymbol: true,
            itemStyle: {
              normal: {
                barBorderRadius: [5, 5, 0, 0],
                //柱体的颜色
                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                color: function (params) {
                  console.log(params);
                  var colorList = [
                    ["#9aaadd", "#007bc2", "#002a5e"],
                    ["#00decc", "#049578", "#013422"],
                  ];
                  var colorItem = colorList[params.dataIndex];
                  return new echarts.graphic.LinearGradient(
                    0,
                    0,
                    0,
                    1,
                    [
                      {
                        offset: 0,
                        color: colorItem[0],
                      },
                      {
                        offset: 0.5,
                        color: colorItem[1],
                      },
                      {
                        offset: 1,
                        color: colorItem[2],
                      },
                    ],
                    false
                  );
                },
              },
            },
            data: [80, 40],
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      todayValueCharts.setOption(option);
    },
    monthValueCharts() {
      var monthValueCharts = echarts.init(
        document.getElementById("monthValueCharts")
      );
      var option = {
        title: {
          show: true,
          //   text: "本月峰值对比1",
          x: "center",
          y: "0%",
          padding: 5,
          textStyle: {
            fontSize: 14,
            color: "white",
            fontWeight: "normal",
          },
        },

        color: ["#00C8FF"],
        tooltip: {
          trigger: "axis",
          position: [20, 20],
          backgroundColor: "rgba(0,0,0,0.8)", //通过设置rgba调节背景颜色与透明度
          color: "white",
          borderWidth: "1",
          borderColor: "black",
          textStyle: {
            color: "white",
          },
          formatter: function (params) {
            var str = params[0].marker + name;
            str += params[0].axisValue + " : " + params[0].value + "<br>";

            return str;
          },
        },
        grid: {
          left: "10%",
          right: "18%",
          bottom: "10%",
          top: "28%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: ["01日", "16日"],
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              lineStyle: {
                color: "#003476",
                width: 1,
              },
            },
            show: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            minInterval: 1,
            splitLine: {
              show: false,
            },
            name: "单位：kwh",
            nameTextStyle: {
              color: "#fff",
              nameLocation: "start",
              padding: [0, 30, 0, 0],
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
                width: 1,
                type: "solid",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        series: [
          {
            type: "bar",
            barWidth: "50%",
            showSymbol: true,
            itemStyle: {
              normal: {
                barBorderRadius: [5, 5, 0, 0],
                //柱体的颜色
                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                color: function (params) {
                  console.log(params);
                  var colorList = [
                    ["#9aaadd", "#007bc2", "#002a5e"],
                    ["#00decc", "#049578", "#013422"],
                  ];
                  var colorItem = colorList[params.dataIndex];
                  return new echarts.graphic.LinearGradient(
                    0,
                    0,
                    0,
                    1,
                    [
                      {
                        offset: 0,
                        color: colorItem[0],
                      },
                      {
                        offset: 0.5,
                        color: colorItem[1],
                      },
                      {
                        offset: 1,
                        color: colorItem[2],
                      },
                    ],
                    false
                  );
                },
              },
            },
            data: [1300, 1440],
          },
        ],
      };
      monthValueCharts.setOption(option);
    },
    yearValueCharts() {
      var yearValueCharts = echarts.init(
        document.getElementById("yearValueCharts")
      );
      var option = {
        title: {
          show: true,
          //   text: "今年峰值对比",
          x: "center",
          y: "0%",
          padding: 5,
          textStyle: {
            fontSize: 14,
            color: "white",
            fontWeight: "normal",
          },
        },
        color: ["#00C8FF"],
        tooltip: {
          trigger: "axis",
          position: [20, 20],
          backgroundColor: "rgba(0,0,0,0.8)", //通过设置rgba调节背景颜色与透明度
          color: "white",
          borderWidth: "1",
          borderColor: "black",
          textStyle: {
            color: "white",
          },
          formatter: function (params) {
            var str = params[0].marker + name;
            str += params[0].axisValue + " : " + params[0].value + "<br>";

            return str;
          },
        },
        grid: {
          left: "10%",
          right: "18%",
          bottom: "10%",
          top: "28%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: ["1月", "3月"],
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              lineStyle: {
                color: "#003476",
                width: 1,
              },
            },
            show: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            minInterval: 1,
            splitLine: {
              show: false,
            },
            name: "单位：kwh",
            nameTextStyle: {
              color: "#fff",
              nameLocation: "start",
              padding: [0, 30, 0, 0],
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
                width: 1,
                type: "solid",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        series: [
          {
            type: "bar",
            barWidth: "50%",
            showSymbol: true,
            itemStyle: {
              normal: {
                barBorderRadius: [5, 5, 0, 0],
                //柱体的颜色
                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                color: function (params) {
                  console.log(params);
                  var colorList = [
                    ["#9aaadd", "#007bc2", "#002a5e"],
                    ["#00decc", "#049578", "#013422"],
                  ];
                  var colorItem = colorList[params.dataIndex];
                  return new echarts.graphic.LinearGradient(
                    0,
                    0,
                    0,
                    1,
                    [
                      {
                        offset: 0,
                        color: colorItem[0],
                      },
                      {
                        offset: 0.5,
                        color: colorItem[1],
                      },
                      {
                        offset: 1,
                        color: colorItem[2],
                      },
                    ],
                    false
                  );
                },
              },
            },
            data: [16000, 15000],
          },
        ],
      };
      yearValueCharts.setOption(option);
    },
  },
};
</script>

<style>
</style>
