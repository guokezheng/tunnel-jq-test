<template>
  <div id="charts4"></div>
</template>
<script>
import * as echarts from "echarts";
export default {
  data() {
    return {
      myChart: null,
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    initCharts() {
      this.myChart = echarts.init(document.getElementById("charts4"));
      let xLabel = ["1月", "2月", "3月", "4月", "5月"];
      var option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
          textStyle: {
            color: "#fff",
            fontSize: 10,
          },
          borderColor: "rgba(1, 29, 63,.8)",
          formatter: function (params) {
            var str =
              "<div style='display:flex;justify-content:space-between;width:100px;margin-bottom:6px'><div><span>" +
              params[0].marker +
              "</span><span>" +
              params[0].seriesName +
              "</span></div><div>" +
              params[0].value +
              "</div></div>";
            str +=
              "<div style='display:flex;justify-content:space-between;width:100px'><div><span>" +
              params[2].marker +
              "</span><span>" +
              params[2].seriesName +
              "</span></div><div>" +
              params[2].value +
              "</div></div>";
            return str;
          },
        },
        animation: false,
        grid: {
          top: "24%",
          bottom: "20%", //也可设置left和right设置距离来控制图表的大小
        },
        legend: {
          show: true,
          icon: "rect",
          right: "5%",
          top: "6%",
          data: ["能耗", "碳排放"],
          itemHeight: 8,
          itemWidth: 16,
          textStyle: {
            color: "#9ba0bc",
            fontSize: 12,
          },
        },

        xAxis: {
          data: xLabel,
          axisLine: {
            lineStyle: {
              color: "#11395D",
            },
          },
          axisLabel: {
            fontSize: 12,
            color: "#9ba0bc",
          },
          axisTick: {
            show: false,
          },
        },
        yAxis: [
          {
            type: "value",
            // gridIndex: 0,
            // min: 0,
            // max: 100,
            // interval: 25,
            // splitNumber: 4,

            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
              lineStyle: {
                color: "#11395D",
              },
            },

            splitLine: {
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
              fontFamily: "Bebas",
            },
          },
        ],
        series: [
          {
            name: "能耗",
            type: "bar",
            barWidth: 14,
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: "#2befc1",
                  },
                  {
                    offset: 1,
                    color: "#11535e",
                  },
                ]),
              },
            },
            data: [60, 55, 46, 34, 23],
            z: 10,
            zlevel: 0,
            label: {
              show: true,
              position: "top",
              distance: 10,
              fontSize: 10,
              color: "#01fff4",
            },
          },

          {
            // 分隔
            type: "pictorialBar",
            itemStyle: {
              normal: {
                color: "#000",
              },
            },
            symbolRepeat: "fixed",
            symbolMargin: 3,
            symbol: "rect",
            symbolClip: true,
            symbolSize: [22, 2],
            symbolPosition: "start",
            symbolOffset: [-12, 0],
            // symbolBoundingData: this.total,
            data: [60, 55, 46, 34, 23],
            width: 25,
            z: 0,
            zlevel: 1,
          },
          {
            name: "碳排放",
            type: "bar",
            barWidth: 14,
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: "#1fc6e4",
                  },
                  {
                    offset: 1,
                    color: "#124b65",
                  },
                ]),
              },
            },
            data: [84, 76, 64, 82, 72],
            z: 10,
            zlevel: 0,
            label: {
              show: true,
              position: "top",
              distance: 10,
              fontSize: 10,
              color: "#01fff4",
            },
          },
          {
            // 分隔
            type: "pictorialBar",
            itemStyle: {
              normal: {
                color: "#000",
              },
            },
            symbolRepeat: "fixed",
            symbolMargin: 3,
            symbol: "rect",
            symbolClip: true,
            symbolSize: [22, 2],
            symbolPosition: "start",
            symbolOffset: [12, 0],
            // symbolBoundingData: this.total,
            data: [84, 76, 64, 82, 72],
            width: 25,
            z: 0,
            zlevel: 1,
          },
        ],
      };
      // 绘制图表
      this.myChart.setOption(option);
    },
  },
};
</script>
<style scoped lang="less">
#charts4 {
  height: calc(100% - 34px);
  > div {
    height: 100%;
  }
}
</style>