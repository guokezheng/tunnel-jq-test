<template>
  <div class="weight-container">
    <div class="contentTitle">
      风险指标
      <i>risk indicator</i>
    </div>
    <div id="EchartsBox" class="echarts-box" style="width:100%;height:80%;"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data() {
    return {};
  },
  mounted() {
    this.initChart()
  },
  methods: {
    initChart() {
      var chartDom = document.getElementById("EchartsBox");
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        tooltip: {
          trigger: "item",
          confine: true,
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        series: [
          {
            name: "风险指标",
            type: "pie",
            selectedMode: "single",
            radius: [0, "50%"],
            label: {
              position: "inner",
              fontSize: 14
            },
            labelLine: {
              show: false,
              confine: true,
            },
            data: [
              {
                value: 1548,
                name: "正常",
                itemStyle: {
                  color: "#55FF00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#fff" }
              },
              {
                value: 775,
                name: "一般",
                itemStyle: {
                  color: "#FFFF00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#fff" }
              },
              {
                value: 679,
                name: "危险",
                itemStyle: {
                  color: "#FFAA00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#fff" }
              },
              {
                value: 679,
                name: "高危",
                selected: true,
                itemStyle: {
                  color: "#FF0000",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#fff" }
              }
            ]
          },
          {
            name: "风险指标",
            type: "pie",
            radius: ["65%", "80%"],
            labelLine: {
              length: 15
            },
            label: {
              alignTo: "edge",
              formatter: "{name|{b}}\n{d}% {c}",
              minMargin: 5,
              lineHeight: 15,
              edgeDistance: 10,
              rich: {
                time: {
                  fontSize: 10,
                  color: "#fff"
                }
              }
            },
            data: [
              { value: 247, name: "消防维护次数" , label:{color:'#fff'}},
              { value: 351, name: "道路拥堵", label:{color:'#fff'}},
              { value: 351, name: "交通事故" , label:{color:'#fff'}},
              { value: 351, name: "两客一危" , label:{color:'#fff'}},
              { value: 351, name: "雾团", label:{color:'#fff'}},
              { value: 310, name: "火灾报警" , label:{color:'#fff'}},
              { value: 234, name: "消防设备故障", label:{color:'#fff'}},
              { value: 335, name: "道路限速", label:{color:'#fff'}},
              { value: 548, name: "道路封路", label:{color:'#fff'}},
              { value: 351, name: "车流量大", label:{color:'#fff'}},
            ]
          }
        ]
      };

      option && myChart.setOption(option);
    }
  }
};
</script>

<style lang="less" scoped>
.weight-container {
  width: 100%;
  height: 100%;
  padding: 0.1px;
  .echarts-box {
    overflow: hidden;
    width: 100%;
    height: calc(100% - 2vw);
  }
}
</style>
