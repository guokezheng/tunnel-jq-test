<template>
  <div class="weight-container">
    <div class="contentTitle">
      风险指标
      <i>risk indicator</i>
    </div>
    <div ref="echartsBox" id="EchartsBox" class="echarts-box"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'

export default {
  data() {
    return {};
  },
  mounted() {
    this.initChart()
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
      var myChart = echarts.init(document.getElementById("EchartsBox"));
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
              show: false
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
                label: { color: "#00007f" }
              },
              {
                value: 775,
                name: "一般",
                itemStyle: {
                  color: "#FFFF00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#00007f" }
              },
              {
                value: 679,
                name: "危险",
                itemStyle: {
                  color: "#FFAA00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: "#00007f" }
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
                label: { color: "#00007f" }
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
              { value: 247, name: "烟火" , label:{color:'#fff'}},
              { value: 351, name: "道路拥堵", label:{color:'#fff'}},
              { value: 351, name: "停车" , label:{color:'#fff'}},
              { value: 351, name: "两客一危" , label:{color:'#fff'}},
              { value: 351, name: "道路遗撒", label:{color:'#fff'}},
              { value: 810, name: "火灾报警" , label:{color:'#fff'}},
              { value: 234, name: "行人", label:{color:'#fff'}},
              { value: 335, name: "低能见度", label:{color:'#fff'}},
              { value: 548, name: "CO异常", label:{color:'#fff'}},
              { value: 351, name: "设备故障", label:{color:'#fff'}},
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
    height: calc(100% - 1.7vw);
  }
}
</style>
