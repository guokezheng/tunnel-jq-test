<template>
  <div style="height: calc(100% - 30px)">
    <div style="height: 30px; display: flex; justify-content: space-between">
      <el-radio-group v-model="tabModel" class="tabButton">
        <el-radio-button label="济南方向"></el-radio-button>
        <el-radio-button label="潍坊方向"></el-radio-button>
      </el-radio-group>
      <el-select v-model="selectModel" size="small" class="bigScreenSelect">
        <el-option
          v-for="item in selectList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
    <div id="charts3"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: "countView",
  data() {
    return {
      tabModel: "济南方向",
      myChart: null,
      option: {},
      selectModel: 1,
      selectList: [
        {
          id: 1,
          name: "满庄",
        },
      ],
    };
  },

  // 生命周期函数
  mounted() {
    this.initCharts();
  },

  // 事件函数
  methods: {
    initCharts() {
      this.$nextTick(function () {
        if (
          this.myChart2 != null &&
          this.myChart2 != "" &&
          this.myChart2 != undefined
        ) {
          // 销毁
          this.myChart2.dispose();
        }
        let e = document.getElementById("charts3");
        if(!e){
          return
        }
        this.myChart = echarts.init(e);
        let xData = [
          "8:00",
          "10:00",
          "12:00",
          "2:00",
          "4:00",
          "6:00",
          "8:00",
          "10:00",
        ];
        let eventWarning = [3, 20, 122, 134, 55, 65, 45, 78];
        let eventHandling = [11, 38, 23, 139, 66, 66, 85, 110];
        let legend = ["实际", "预测"];
        this.option = {
          color: ["#333FFF", "#23B899"],
          timeline: {
            show: false,
          },
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "none",
            },
            formatter: "{a}<br/>{b} :\n\n{c} 个",
          },
          legend: {
            top: 10,
            right: "center",
            itemWidth: 16,
            itemHeight: 6,
            itemGap: 20,
            icon: "horizontal",
            textStyle: {
              color: "#9ba0bc",
              fontSize: 12,
            },
            data: legend,
          },
          grid: [
            // 左边
            {
              show: false,
              left: "5%",
              top: "15%",
              bottom: "6%",
              containLabel: true,
              width: "38%",
            },
            // 中间
            {
              show: false,
              left: "52%",
              top: "15%",
              bottom: "6%",
              width: "0%",
            },
            // 右边
            {
              show: false,
              right: "5%",
              top: "15%",
              bottom: "6%",
              containLabel: true,
              width: "38%",
            },
          ],
          xAxis: [
            {
              type: "value",
              inverse: true,
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              axisLabel: {
                show: false,
                verticalAlign: "middle",
                textStyle: {
                  color: "#ADBBD8",
                  fontSize: 12,
                },
              },
              splitLine: {
                show: false,
              },
            },
            {
              gridIndex: 1,
              show: false,
            },
            {
              gridIndex: 2,
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
                verticalAlign: "middle",
                textStyle: {
                  color: "#ADBBD8",
                  fontSize: 12,
                },
              },
              splitLine: {
                show: false,
              },
            },
          ],
          yAxis: [
            {
              type: "category",
              inverse: true,
              position: "right",
              axisLine: {
                show: true,
                lineStyle: {
                  color: "#555555",
                },
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              data: xData,
            },
            {
              gridIndex: 1,
              type: "category",
              inverse: true,
              position: "left",
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: true,
                color: "#9ba0bc",
                fontSize: 12,
                align: "center",
              },
              data: xData.map(function (value) {
                return {
                  value: value,
                  textStyle: {
                    align: "center",
                  },
                };
              }),
            },
            {
              gridIndex: 2,
              type: "category",
              inverse: true,
              position: "left",
              axisLine: {
                show: true,
                lineStyle: {
                  color: "#555555",
                },
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              data: xData,
            },
          ],
          series: [
            {
              name: legend[0],
              type: "bar",
              barWidth: 8,
              stack: "1",
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  {
                    offset: 0,
                    color: "rgba(69, 239, 253, 1)",
                  },
                  {
                    offset: 1,
                    color: "rgba(69, 239, 253, 0)",
                  },
                ]),
              },
              label: {
                show: true,
                position: "left",
                color: "#45EFFD",
              },
              data: eventWarning,
              animationEasing: "elasticOut",
            },
            {
              name: legend[1],
              type: "bar",
              stack: "2",
              barWidth: 8,
              xAxisIndex: 2,
              yAxisIndex: 2,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  {
                    offset: 0,
                    color: "rgba(255, 191, 10, 0)",
                  },
                  {
                    offset: 1,
                    color: "rgba(255, 191, 10, 1)",
                  },
                ]),
              },
              label: {
                show: true,
                position: "right",
                color: "#FFBF0A",
              },
              data: eventHandling,
              animationEasing: "elasticOut",
            },
          ],
        };

        // 绘制图表
        this.option && this.myChart.setOption(this.option);
      });
    },
  },
};
</script>
<style lang='scss' scoped>
.bigScreenSelect {
  float: right;
  padding-top: 4px;
  height: 30px;
  ::v-deep .el-input--small .el-input__inner {
    height: 28px !important;
    width: 100px;
    line-height: 28px !important;
    background-color: rgba(0,0,0,0.5) !important;
    border: solid 1px #0c4b72 !important;
    color: #9ba0bc !important;
  }
}
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background: linear-gradient(180deg, #ffc606, #ff8200) !important;
  border: none;
}
::v-deep .el-radio-group .el-radio-button__inner {
  background: linear-gradient(
    180deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  ) !important;
  border: none;
  color: #fff;
}
.tabButton {
  margin: 4px 0;
  .el-radio-button {
    margin-right: 4px;
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 7px 20px !important;
  }
}
#charts3 {
  width: 100%;
  height: calc(100% - 30px);
  > div {
    height: 100%;
  }
}
</style>
