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
import { getEquipmentStatusList } from "@/api/business/new";
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";

export default {
  data() {
    return {
      list: [],
    };
  },
  created() {},
  mounted() {
    this.Armamentarium();
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
    Armamentarium() {
      var myChart = echarts.init(document.getElementById("Armamentarium"));
      var option;
      const color = ["#83f9f8", "#00d4c7", "#c6bf46", "#0091f6", "#1ac98b"];
      getEquipmentStatusList().then((res) => {
        let data = res.data;
        data.forEach((item) => {
          color.forEach((items) => {
            item.value = item.number;
            item.label = { color: items };
            // item.itemStyle = {
            //   normal: {
            //     borderRadius: ["50", "0", "50", "0"],
            //     //颜色渐变
            //     color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            //       { offset: 0, color: "#0168af" },
            //       { offset: 1, color: "transparent" },
            //     ]),
            //   },
            // };
          });
        });
        this.list = data;
        const echartData = this.list;
        // [
        //   { value: 30, name: "隧道灯" },
        //   { value: 28, name: "摄像头" },
        //   { value: 26, name: "COVI检测器" },
        //   { value: 24, name: "风机" },
        //   { value: 22, name: "车道指示器" },
        // ];
        const rich = {
          yellow: {
            color: "#ffffff",
            fontSize: 15,
            // padding: [21, 0],
            align: "center",
          },
          total: {
            color: "#ffffff",
            fontSize: 20,
            align: "center",
          },

          blue: {
            color: "#00f7f8",
            fontSize: 20,
            align: "center",
          },
          hr: {
            borderColor: "#0b5263",
            width: "100%",
            borderWidth: 1,
            height: 0,
          },
        };
        option = {
          // title: {
          //   text: "设备占比",
          //   textStyle: {
          //     color: "#f2f2f2",
          //     fontSize: 18,
          //     align: "center",
          //   },
          //   x: "center",
          //   y: "center",
          // },
          tooltip: {
            // trigger: "item",
            // formatter: "{b}: {c} ({d}%)",
          },
          legend: {
            left: "center",
            top: "bottom",
            data: ["rose1", "rose2", "rose3", "rose4", "rose5"],
          },
          series: [
            {
              color: color,
              radius: [120, 160],
              selectedMode: "single",
              startAngle: 60,
              // name: "设备占比",
              type: "pie",
              center: ["50%", "50%"],
              // itemStyle: {
              //   normal: {
              //     shadowColor: "rgba(0, 0, 0, 0.2)",
              //     shadowBlur: 10,
              //   },
              // },
              radius: ["50%", "35%"],
              // label: {
              //   position: "inner",
              //   fontSize: 0,
              // },
              labelLine: {
                show: false,
              },
              data: [
                {
                  value: 20,
                  itemStyle: {
                    normal: {
                      borderRadius: ["50", "0", "50", "0"],
                      //颜色渐变
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: "#0168af" },
                        { offset: 1, color: "transparent" },
                      ]),
                    },
                  },
                },
                {
                  value: 20,
                  itemStyle: {
                    normal: {
                      borderRadius: ["50", "0", "50", "0"],
                      //颜色渐变
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: "#3197ba" },
                        { offset: 1, color: "transparent" },
                      ]),
                    },
                  },
                },
                {
                  value: 20,
                  itemStyle: {
                    normal: {
                      borderRadius: ["50", "0", "50", "0"],
                      //颜色渐变
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: "transparent" },
                        { offset: 1, color: "#0168af" },
                      ]),
                    },
                  },
                },
                {
                  value: 20,
                  itemStyle: {
                    normal: {
                      borderRadius: ["50", "0", "50", "0"],
                      //颜色渐变
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: "transparent" },
                        { offset: 1, color: "#0168af" },
                      ]),
                    },
                  },
                },
                {
                  value: 20,
                  itemStyle: {
                    normal: {
                      borderRadius: ["50", "0", "50", "0"],
                      //颜色渐变
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: "#0168af" },
                        { offset: 1, color: "transparent" },
                      ]),
                    },
                  },
                },
              ],
            },
            {
              color: color,
              // name: "Access From",
              type: "pie",
              radius: ["55%", "60%"],
              labelLine: {
                length: 20,
              },
              itemStyle: {
                borderRadius: 55,
              },
              label: {
                // formatter: "{name|{b}}\n{d}% {c} ",
                fontSize: 17,
                borderWidth: 2,
                borderRadius: 10,
                normal: {
                  formatter: function (params, ticket, callback) {
                    var total = 0; //考生总数量
                    var percent = 0; //考生占比
                    echartData.forEach(function (value, index, array) {
                      total += value.value;
                    });
                    percent = ((params.value / total) * 100).toFixed(1);
                    return "{" + params.name + "}\n{blue|" + percent + "%}";
                  },
                  rich: rich,
                },
              },
              data: echartData,
              // [
              //   { value: 30, name: "隧道灯", label: { color: "#83f9f8" } },
              //   { value: 28, name: "摄像头", label: { color: "#00d4c7" } },
              //   { value: 26, name: "COVI检测器", label: { color: "#c6bf46" } },
              //   { value: 24, name: "风机", label: { color: "#0091f6" } },
              //   { value: 22, name: "车道指示器", label: { color: "#1ac98b" } },
              // ],
            },
          ],
        };

        option && myChart.setOption(option);
      });
    },
  },
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
    > div:first-child {
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
