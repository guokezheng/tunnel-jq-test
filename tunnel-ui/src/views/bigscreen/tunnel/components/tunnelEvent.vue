<template>
  <div class="tunnelEvent-container">
    <div class="contentTitle">
      近30日隧道预警
      <i>Tunnel event</i>
    </div>
    <div ref="echartsBox" id="echarts-Box" class="echarts-Box"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'

export default {
  data() {
    return {
      warningData: [
        9, 10, 15, 10, 11, 13, 7, 10, 8, 9, 0, 4, 5, 6, 8, 7, 6, 4, 2, 0, 1, 1,
        3, 6, 8, 10, 6, 5, 3, 0,
      ],
    };
  },
  mounted() {
    this.initChart();
    this.watchSize()
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker()
      let Dom = that.$refs.echartsBox;//拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
          let myChart = echarts.init(Dom);
          myChart.resize();//echarts自带的方法可以使图表重新加载
      })
    },
    initChart() {
      var warningData = this.warningData;
      var chartDom = document.getElementById("echarts-Box");
      var myChart = echarts.init(chartDom);
      var option;

      var xData = (function () {
        var data = [];
        for (var i = 30; i >= 1; i--) {
          data.push("近" + i + "日");
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
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          // top: 60,
          // bottom: '10%',
          left: "15%",
          textStyle: {
            color: "#fff",
          },
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
                color: "rgba(204,187,225,0.5)",
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            data: xData,
          },
        ],

        yAxis: [
          {
            type: "value",
            splitLine: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: "rgba(204,187,225,0.5)",
              },
            },
            nameTextStyle: {
              lineHeight: 15,
            },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 22,
            xAxisIndex: [0],
            // bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#5B3AAE",
            },
            textStyle: {
              color: "rgba(204,187,225,0.5)",
            },
            fillerColor: "rgba(67,55,160,0.4)",
            borderColor: "rgba(204,187,225,0.5)",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "预警量",
            type: "line",
            symbolSize: 10,
            symbol: "circle",
            itemStyle: {
              color: "#6f7de3",
            },
            markPoint: {
              label: {
                normal: {
                  textStyle: {
                    color: "#fff",
                  },
                },
              },
              data: [
                {
                  type: "max",
                  name: "最大值",
                },
                {
                  type: "min",
                  name: "最小值",
                },
              ],
            },
            data: warningData,
          },
        ],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="less" scoped>
.tunnelEvent-container {
  width: 100%;
  height: 100%;
  font-size: 0.8vw;
  overflow: hidden;
  #echarts-Box {
    height: calc(100% - 1.7vw);
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
