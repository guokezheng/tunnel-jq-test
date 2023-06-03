<template>
  <div class="barQyEchart">
    <div id="itemizedEnergy"></div>
    <div class="right-text">
      <el-row class="row">
        <div class="left-span">
          <span
            class="leftTopSpan"
            style="background-color: rgb(74, 167, 241)"
          ></span>
          <span>照明系统</span>
        </div>
        <div class="right-span">
          <span class="color1">2026 kwh</span>
          <span style="padding-left: 4px" class="color1">|</span
          ><span style="padding-left: 4px" class="color1">7.69%</span>
        </div>
      </el-row>
      <el-row class="row">
        <div class="left-span">
          <span
            class="leftTopSpan"
            style="background: rgb(94, 211, 250)"
          ></span>
          <span>空调系统</span>
        </div>
        <div class="right-span">
          <span class="color2">2026 kwh</span>
          <span style="padding-left: 4px" class="color2">|</span
          ><span style="padding-left: 4px" class="color2">92.31%</span>
        </div>
      </el-row>
      <el-row class="row">
        <div class="left-span">
          <span
            class="leftTopSpan"
            style="background-color: rgb(227, 186, 115)"
          ></span>
          <span>卫生间用水</span>
        </div>
        <div class="right-span">
          <span class="color3">386 kwh</span>
          <span class="color3" style="padding-left: 4px">|</span
          ><span class="color3" style="padding-left: 4px">7.69%</span>
        </div>
      </el-row>
      <el-row class="row">
        <div class="left-span">
          <span
            class="leftTopSpan"
            style="background-color: rgb(239, 134, 109)"
          ></span>
          <span>办公用电</span>
        </div>
        <div class="right-span">
          <span class="color4">386 kwh</span>
          <span class="color4" style="padding-left: 4px">|</span
          ><span class="color4" style="padding-left: 4px">7.69%</span>
        </div>
      </el-row>
      <el-row class="row">
        <div class="left-span">
          <span
            class="leftTopSpan"
            style="background-color: rgb(189, 131, 242)"
          ></span>
          <span>其他</span>
        </div>
        <div class="right-span">
          <span class="color5">386 kwh</span>
          <span class="color5" style="padding-left: 4px">|</span
          ><span class="color5" style="padding-left: 4px">7.69%</span>
        </div>
      </el-row>
    </div>
  </div>
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
      this.myChart = echarts.init(document.getElementById("itemizedEnergy"));
      const pieData = [
        {
          name: "楼宇负荷",
          value: Math.floor(Math.random() * 100 + 100),
        },
        {
          name: "微网",
          value: Math.floor(Math.random() * 100 + 100),
        },
        {
          name: "储能",
          value: Math.floor(Math.random() * 100 + 100),
        },
        {
          name: "灵活调节电源",
          value: Math.floor(Math.random() * 100 + 100),
        },
        {
          name: "综合能源",
          value: Math.floor(Math.random() * 100 + 100),
        },
      ];
      const total = pieData.reduce((a, b) => {
        return a + b.value;
      }, 0);
      let seriesData = [];
      pieData.forEach((r) => {
        seriesData.push(r);
        seriesData.push({
          name: "",
          value: 15,
          tooltip: { show: false },
          itemStyle: {
            label: {
              show: false,
            },
            labelLine: {
              show: false,
            },
            color: "rgba(0, 0, 0, 0)",
            borderColor: "rgba(0, 0, 0, 0)",
            borderWidth: 0,
          },
        });
      });
      var option = {
        color: ["#96B5FC", "#5F8CEF", "#F18A70", "#F4C594", "#E0E7FA"],
        title: {
          text: `{a|${total}}{b| MW}`,
          textStyle: {
            rich: {
              a: {
                color: "#FFFFFF",
                fontSize: "10",
              },
              b: {
                fontSize: "10",
                color: "#FFFFFF",
              },
            },
          },
          x: "31%",
          y: "45%",
        },
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
          textStyle: {
            color: "#fff",
            fontSize: 12,
          },
          borderColor: "rgba(1, 29, 63,.8)",
          axisPointer: {
            type: "shadow",
            shadowStyle: {
              fontSize: 12,
              color: "rgba(0, 11, 34, 0)",
            },
          },
        //   提示框超出范围时调整位置
          position: function (point, params, dom, rect, size) {
            // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
            // 提示框位置
            let x = 0; // x坐标位置
            let y = 0; // y坐标位置

            // 当前鼠标位置
            let pointX = point[0];
            let pointY = point[1];

            // 外层div大小
            // var viewWidth = size.viewSize[0];
            // var viewHeight = size.viewSize[1];

            // 提示框大小
            let boxWidth = size.contentSize[0];
            let boxHeight = size.contentSize[1];

            // boxWidth > pointX 说明鼠标左边放不下提示框
            if (boxWidth > pointX) {
              x = 5;
            } else {
              // 左边放的下
              x = pointX - boxWidth;
            }

            // boxHeight > pointY 说明鼠标上边放不下提示框
            if (boxHeight > pointY) {
              y = 5;
            } else {
              // 上边放得下
              y = pointY - boxHeight;
            }

            return [x, y];
          },
        },
        // legend: {
        //    icon: "circle",
        //    orient: "horizontal",
        //    right: "10%",
        //    top: "30%",
        //    width: "0",
        //    textStyle: {
        //       color: "#E6F7FF",
        //    },
        //    data: pieData.map(r => r.name),
        // },
        series: [
          {
            name: "交易品种收益",
            type: "pie",
            radius: ["55%", "60%"],
            center: ["50%", "50%"],
            avoidLabelOverlap: false,
            // label: {
            //    position: "outside",
            //    color: "#E6F7FF",
            //    lineHeight: 18,
            //    formatter: params => {
            //       const { data } = params;
            //       return data.name ? `${data.name}\n${data.value}MW` : "";
            //    },
            // },
            itemStyle: {
              normal: {
                label: {
                  show: false,
                },
                labelLine: {
                  show: false,
                },
              },
            },
            // emphasis: {
            //    scaleSize: 10,
            // },
            data: seriesData,
          },
          {
            name: "阴影圈",
            type: "pie",
            radius: ["0", "45%"],
            center: ["50%", "50%"],
            emphasis: {
              scale: false,
            },
            tooltip: {
              show: false,
            },
            itemStyle: {
              color: "rgba(204,225,255, 0.1)",
            },
            zlevel: 4,
            labelLine: {
              show: false,
            },
            data: [100],
          },
          {
            name: "阴影圈",
            type: "pie",
            radius: ["0", "35%"],
            center: ["50%", "50%"],
            emphasis: {
              scale: false,
            },
            tooltip: {
              show: false,
            },
            itemStyle: {
              color: "rgba(204,225,255, 0.1)",
            },
            zlevel: 4,
            labelLine: {
              show: false,
            },
            data: [100],
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
.barQyEchart {
  height: calc(100% - 30px);
  display: flex;
  #itemizedEnergy {
    height: 100%;
    width: 38%;
  }
  .right-text {
    width: 60%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .row {
      width: 100%;
      height: 2vh;
      margin-bottom: 8px;
      display: flex;
      // justify-content: space-between;
      background-image: linear-gradient(
        to right,
        rgba(3, 71, 130, 1),
        rgba(3, 71, 130, 0)
      );
      span {
        color: #c5d0e0;
        font-size: 0.7vw;
      }
      .leftTopSpan {
        display: inline-block;
        width: 7px;
        height: 7px;
        margin: 0 8px;
      }
      .left-span {
        width: 45%;
      }
      .right-span {
        width: 55%;
        text-align: right;
        span {
          width: 50%;
          line-height: 18px;
          font-weight: bold;
        }
        .color1 {
          color: rgb(74, 167, 241);
        }
        .color2 {
          color: rgb(94, 211, 250);
        }
        .color3 {
          color: rgb(227, 186, 115);
        }
        .color4 {
          color: rgb(239, 134, 109);
        }
        .color5 {
          color: rgb(189, 131, 242);
        }
      }
    }
  }
}
</style>
