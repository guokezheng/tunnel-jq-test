<template>
  <div class="armamentarium-container">
    <div class="contentTitle">
      设备占比
      <i>n. proportion</i>
    </div>
    <div id="Armamentarium" class="ArmamentariumClass"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data() {
    return {};
  },
  created() {},
  mounted() {
    this.Armamentarium();
  },
  methods: {
    Armamentarium() {
      var myChart = echarts.init(document.getElementById("Armamentarium"));
      var option;

      option = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
          confine: true,
        },
        // label: {
        //   formatter: "{b|{b}：}\n{hr|}\n {c}  {per|{d}%}  ",
        //   color: "#fff",
        //   borderColor: "#09bdef",
        //   borderWidth: 1,
        //   borderRadius: 5,
        //   rich: {
        //     a: {
        //       color: "#fff",
        //       lineHeight: 22,
        //       align: "center"
        //     },
        //     hr: {
        //       borderColor: "#09bdef",
        //       width: "100%",
        //       borderWidth: 1,
        //       height: 0
        //     },
        //     b: {
        //       color: "#fff",
        //       fontSize: 14,
        //       fontWeight: "bold",
        //       lineHeight: 25,
        //       align: "center"
        //     },
        //     per: {
        //       color: "#fff",
        //       backgroundColor: "#09bdef",
        //       padding: [3, 4],
        //       borderRadius: 4
        //     }
        //   }
        // },
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

  .ArmamentariumClass {
    width: 100%;
    height: calc(100% - 2vw);
    // transform: translateY(-0.2vw);
    overflow: hidden;
  }
}
</style>
