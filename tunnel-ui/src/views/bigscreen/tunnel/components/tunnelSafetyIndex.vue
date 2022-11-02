<template>
  <div class="tunnelSafetyIndex-container">
    <div class="contentTitle">
      隧道安全指数
      <i>safety index</i>
    </div>
    <div ref="echartsBox" class="scroll-Box" id="tunnelSafetyIndex"></div>
  </div>
</template>

<script>
import * as recharts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";
export default {
  data() {
    return {};
  },
  mounted() {
    this.tunnelSafetyIndex();
    this.watchSize();
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = recharts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    tunnelSafetyIndex() {
      var tunnelSafetyIndex = recharts.init(
        document.getElementById("tunnelSafetyIndex")
      );
      var option;
      var yData = [
        "姚家峪隧道",
        "毓秀山隧道",
        "洪河隧道",
        "滨莱高速",
        "望海石隧道",
        "中庄隧道",
        "马公祠隧道",
        "乐疃隧道",
        "樵岭前隧道",
        "佛羊岭隧道",
        "迎春坡隧道",
        "望海石隧道",
      ];
      var data = [
        "2000",
        "1800",
        "1600",
        "1000",
        "800",
        "600",
        "400",
        "300",
        "200",
        "200",
      ];
      data.sort((a, b) => b - a);
      var max = Math.max.apply(null, data);
      var getMax = [];
      for (let i = 0; i < yData.length; i++) {
        getMax.push(max);
      }

      option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "none",
          },
          formatter: function (params) {
            return params[0].name + " : " + params[0].value;
          },
        },
        xAxis: {
          show: false,
          type: "value",
        },
        grid: {
          right: "10%",
          left: "32%",
          top: "4%",
          bottom: "1%",
        },
        yAxis: [
          {
            type: "category",
            inverse: true,
            offset: 120,
            axisLabel: {
              show: true,
              align: "left",
              textStyle: {
                color: "#fff",
                fontSize: "14",
              },
              formatter: function (value, index) {
                var num = "";
                var str = "";
                num = index + 1;
                if (index === 0) {
                  str =
                    "{no1|" + "} {num1|" + num + "} {title1| " + value + "}";
                } else if (index === 1) {
                  str =
                    "{no2|" + "} {num2|" + num + "} {title2| " + value + "}";
                } else if (index === 2) {
                  str =
                    "{no3|" + "} {num3|" + num + "} {title3| " + value + "}";
                } else {
                  str = " {num|" + num + "} {title| " + value + "}";
                }
                return str;
              },
              rich: {
                num: {
                  color: "#387ec1",
                  backgroundColor: "#112b67",
                  width: 10,
                  height: 10,
                  fontSize: 14,
                  padding: [6, 5, 3, 5],
                  align: "center",
                  shadowColor: "#3374ba",
                  borderColor: "#3374ba",
                  borderWidth: 1,
                },
                num1: {
                  color: "#51aff8",
                  backgroundColor: "#112b67",
                  width: 10,
                  height: 10,
                  fontSize: 14,
                  padding: [7, 5, 3, 5],
                  align: "center",
                  shadowColor: "#4db2ff",
                  borderColor: "#4db2ff",
                  borderWidth: 1,
                },
                num2: {
                  color: "#51aff8",
                  backgroundColor: "#112b67",
                  width: 10,
                  height: 10,
                  fontSize: 14,
                  padding: [7, 5, 3, 5],
                  align: "center",
                  shadowColor: "#4db2ff",
                  borderColor: "#4db2ff",
                  borderWidth: 1,
                },
                num3: {
                  color: "#51aff8",
                  backgroundColor: "#112b67",
                  width: 10,
                  height: 10,
                  fontSize: 14,
                  padding: [7, 5, 3, 5],
                  align: "center",
                  shadowColor: "#4db2ff",
                  borderColor: "#4db2ff",
                  borderWidth: 1,
                },
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            data: yData,
          },
        ],
        series: [
          {
            name: "值",
            type: "bar",
            zlevel: 1,
            itemStyle: {
              normal: {
                barBorderRadius: 30,
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 1,
                  y2: 0,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#81d6f3", //  0%  处的颜色
                    },
                    {
                      offset: 1,
                      color: "#5684f6", //  100%  处的颜色
                    },
                  ],
                  global: false, //  缺省为  false
                },
              },
            },
            barWidth: 10,
            data: data,
          },
          {
            name: "背景",
            type: "bar",
            barWidth: 10,
            barGap: "-100%",
            data: getMax,
            itemStyle: {
              color: "#ffffff",
              barBorderRadius: 30,
            },
          },
        ],
      };

      // 使用刚指定的配置项和数据显示图表。
      option && tunnelSafetyIndex.setOption(option);
    },
  },
};
</script>

<style lang="less" scoped>
.tunnelSafetyIndex-container {
  height: 100%;
  padding: 0.1px;
  font-size: 0.8vw;
  .scroll-Box {
    overflow: hidden;
    width: 100%;
    height: calc(100% - 2vw);
    background-color: #00598f;
    .listContent {
      .el-row {
        .el-col {
          display: flex;
          align-items: center;
        }
      }
    }
    .box-id {
      width: 1vw;
      height: 1vw;
      border: 0.1vw solid #fff;
      border-radius: 0.5vw;
      background-color: gainsboro;
      color: black;
      text-align: center;
      font-size: 0.5vw;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .box-id-one {
      background-color: red;
      color: white;
    }
    .box-id-two {
      background-color: orange;
      color: white;
    }
    .box-id-three {
      background-color: blue;
      color: white;
    }

    /deep/ .el-scrollbar {
      height: 100%;
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
      .el-scrollbar__thumb {
        background-color: #027dec;
      }
    }
  }
}
</style>
