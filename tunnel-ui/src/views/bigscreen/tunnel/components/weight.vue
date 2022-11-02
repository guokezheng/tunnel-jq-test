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
import elementResizeDetectorMaker from "element-resize-detector";

export default {
  data() {
    return {};
  },
  mounted() {
    this.initChart();
    this.watchSize();
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    initChart() {
      var myChart = echarts.init(document.getElementById("EchartsBox"));
      var option;
      // 向上方列中拖入字段，可在代码中引用dataSet字段获取二维数组数据
      // img是内圈的4个黄色弧
      option = {
        polar: {
          radius: ["61%", "57%"],
          center: ["50%", "50%"],
        },
        title: {
          zlevel: 0,
          subtext: "风险指标",
          top: "43%",
          left: "50%",
          textAlign: "center",
          subtextStyle: {
            fontSize: 20,
            color: "#fff",
          },
        },
        angleAxis: {
          max: 1,
          show: false,
          startAngle: 0,
        },
        radiusAxis: {
          type: "category",
          show: true,
          axisLabel: { show: false },
          axisLine: { show: false },
          axisTick: { show: false },
        },
        series: [
          // 外层刻度
          {
            hoverAnimation: false,
            type: "pie",
            z: 2,
            data: [],
            radius: ["43%", "50%"],
            zlevel: -2,
            label: {
              normal: {
                position: "inside",
                show: false,
              },
            },
          },
          // 内层饼图
          {
            type: "pie",
            radius: ["56%", "60%"],
            center: ["50%", "50%"],
            data: [],
            labelLine: {
              length: 40,
              borderRadius: 34,
            },
            // 提示线设置
            label: {
              formatter: " {per|{d}%} ",
              backgroundColor: "rgba(0, 0, 0, 0)",
              borderColor: "rgba(0, 0, 0, 0)",
              borderWidth: 1,
              borderRadius: 4,
              rich: {
                a: {
                  color: "#fff",
                  lineHeight: 26,
                  align: "center",
                  fontSize: 16,
                },
                hr: {
                  borderColor: "auto",
                  width: "100%",
                  borderWidth: 1,
                  height: 0,
                },
                per: {
                  color: "#ffffff",
                  backgroundColor: "rgba(0, 0, 0, 0)",
                  padding: [5, 5],
                  borderRadius: 4,
                  fontSize: 16,
                },
              },
            },
          },
        ],
      };
      var chartData = [
        { color: "#3880f5", data: 256, tilte: "车辆慢行" },
        { color: "#f2b557", data: 82, ratio: "0.17", tilte: "行人闯入" },
        { color: "#60c2ce", data: 74, ratio: "0.15", tilte: "临时停车" },
        { color: "#d22c5f", data: 60, ratio: "0.12", tilte: "火灾报警" },
        { color: "#ed8d87", data: 60, ratio: "0.12", tilte: "设备故障" },
        { color: "#00c8ff", data: 60, ratio: "0.12", tilte: "道路拥挤" },
        { color: "#56b0f5", data: 60, ratio: "0.12", tilte: "车辆超速" },
      ];
      var total = 472;
      chartData.map((item) => {
        if (item.data > 0) {
          let num = Math.round((item.data / total) * 100);
          for (let i = 0; i < num; ++i) {
            option.series[0].data.push(
              {
                value: 2, // 有颜色部分的宽度
                name: item.title,
                ratio: item.ratio,
                itemStyle: {
                  normal: { color: "#007fbf" },
                },
              },
              {
                value: 1, // 无颜色部分的宽度
                name: "",
                itemStyle: {
                  normal: {
                    label: { show: false },
                    labelLine: { show: false },
                    color: "rgba(0, 0, 0, 0)",
                    borderColor: "rgba(0, 0, 0, 0)",
                    borderWidth: 0,
                  },
                },
              }
            );
          }
        }
        // 内层饼图数据
        option.series[1].data.push({
          hoverOffset: 1,
          value: item.data,
          itemStyle: { color: item.color },
        });
      });

      option && myChart.setOption(option);
    },
  },
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
    background-color: #00598f;
  }
}
</style>
