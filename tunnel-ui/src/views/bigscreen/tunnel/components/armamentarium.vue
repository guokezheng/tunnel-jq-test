<template>
  <div class="armamentarium-container">
    <div class="contentTitle">
      设备占比
      <i>n. proportion</i>
    </div>
    <div ref="echartsBox" id="Armamentarium" class="ArmamentariumClass"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'

export default {
  data() {
    return {};
  },
  created() {},
  mounted() {
    this.Armamentarium();
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
    Armamentarium() {
      var myChart = echarts.init(document.getElementById("Armamentarium"));
      var option;

      option = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c}<br/>占比：({d}%)",
          confine: true,
        },
        label: {
          alignTo: "edge",
          formatter: "{name|{b}}\n{d}% {c}",
          minMargin: 5,
          lineHeight: 14,
          edgeDistance: 10,
          rich: {
            time: {
              fontSize: 14,
              color: "#fff"
            }
          }
        },
        legend: {
          left: "center",
          top: "bottom",
          data: ["rose1", "rose2", "rose3", "rose4", "rose5"]
        },
        series: [
          {
            name: "设备占比",
            type: "pie",
            radius: ['20%', '70%'],
            center: ["50%", "50%"],
            roseType: "radius",
            itemStyle: {
              borderRadius: 5
            },

            data: [
              { value: 30, name: "隧道灯", label:{color:'#fff'} },
              { value: 28, name: "摄像头", label:{color:'#fff'} },
              { value: 26, name: "COVI检测器", label:{color:'#fff'} },
              { value: 24, name: "风机", label:{color:'#fff'} },
              { value: 22, name: "车道指示器", label:{color:'#fff'} }
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
.armamentarium-container {
  width: 100%;
  height: 100%;
  font-size: 0.8vw;
  overflow: hidden;

  /deep/ .ArmamentariumClass {
    width: 100%;
    height: calc(100% - 2vw);
    // transform: translateY(-0.2vw);
    overflow: hidden;
    >div:first-child {
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
