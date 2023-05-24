<template>
  <div class="box">
    <div class="topTime">
      <el-date-picker
        v-model="dateRange"
        size="small"
        style="width: 250px; height: 28px; line-height: 28px; margin-top: 4px"
        value-format="yyyy-MM-dd"
        type="daterange"
        range-separator="-"
        unlink-panels
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      ></el-date-picker>
    </div>
    <el-radio-group v-model="tabModel" class="tabButton">
      <el-radio-button label="全部作业"></el-radio-button>
      <el-radio-button label="养护作业"></el-radio-button>
    </el-radio-group>
    <div class="content">
      <div class="left" id="leftChart"></div>
      <div class="right">
        <div class="rightRow1">
          <div>全部</div>
          <div>已完成</div>
        </div>
        <div class="rightRow2">
          <div><span>17</span><span>条</span></div>
          <div><span>1</span><span>条</span></div>
        </div>
        <div class="right-text">
          <el-row class="row">
            <span class="row_top_text"
              >机电维护</span
            >
            <span class="row_bottom_text" style="color: rgba(55, 231, 255, 1)">
              11.76%
            </span>
            <div
              class="position_color"
              style="background: rgba(55, 231, 255, 1)"
            ></div>
          </el-row>
          <el-row class="row">
            <span class="row_top_text" 
              >巡检</span
            >
            <span class="row_bottom_text" style="color: rgba(62, 192, 153, 1)">
              50.59%
            </span>
            <div
              class="position_color"
              style="background: rgba(62, 192, 153, 1)"
            ></div>
          </el-row>
          <el-row class="row">
            <span class="row_top_text" 
              >绿化</span
            >
            <span class="row_bottom_text" style="color: rgba(239, 175, 76, 1)">
              6.00%
            </span>
            <div
              class="position_color"
              style="background: rgba(239, 175, 76, 1)"
            ></div>
          </el-row>
          <el-row class="row">
            <span class="row_top_text" 
              >检测</span
            >
            <span class="row_bottom_text" style="color: rgba(236, 237, 193, 1)">
              14.00%
            </span>
            <div
              class="position_color"
              style="background: rgba(236, 237, 193, 1)"
            ></div>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      myChart1: null,
      // 日期范围
      dateRange: [],
      tabModel: "全部作业",
    };
  },

  created() {},
  mounted() {
    this.initEchats();
  },
  methods: {
    initEchats() {
      this.$nextTick(function () {
        if (
          this.myChart1 != null &&
          this.myChart1 != "" &&
          this.myChart1 != undefined
        ) {
          // 销毁
          this.myChart1.dispose();
        }
        this.myChart1 = echarts.init(document.getElementById("leftChart"));
        // 复制代码
        const chartData = [
          {
            value: 11.76,
            name: "机电维护",
          },
          {
            value: 50.59,
            name: "巡检",
          },
          {
            value: 6,
            name: "绿化",
          },
          {
            value: 14,
            name: "检测",
          },
        ];
        const colorList = [
          "rgba(55, 231, 255, 1)",
          "rgba(62, 192, 153, 1)",
          "rgba(239, 175, 76, 1)",
          "rgba(236, 237, 193, 1)",
          // "#63F2FF",
          // "#9999FE",
        ];
        const sum = chartData.reduce((per, cur) => per + cur.value, 0);
        const gap = (1 * sum) / 100;
        const pieData1 = [];
        const pieData2 = [];
        const gapData = {
          name: "",
          value: gap,
          itemStyle: {
            color: "transparent",
          },
        };

        let total = 0;
        chartData.forEach((item) => {
          total += item.value;
        });

        for (let i = 0; i < chartData.length; i++) {
          // 第一圈数据
          pieData1.push({
            ...chartData[i],
            itemStyle: {
              borderRadius: 10,
            },
          });
          pieData1.push(gapData);

          // 第二圈数据
          pieData2.push({
            ...chartData[i],
            itemStyle: {
              color: colorList[i],
              opacity: 0.21,
            },
          });
          pieData2.push(gapData);
        }

        var option = {
          title: {
            text: "重点人员",
            subtext: "353",
            x: "47%",
            y: "33%",
            itemGap: 15,
            textStyle: {
              color: "#6A93B9",
              fontSize: 12,
              fontWeight: "bold",
            },
            subtextStyle: {
              color: "#6A93B9",
              fontSize: 20,
              fontWeight: "bold",
            },
            textAlign: "center",
          },

          // backgroundColor: "#0F141B",
          tooltip: {
            show: true,
            backgroundColor: "rgba(0, 0, 0,.8)",
            textStyle: {
              color: "#fff",
            },
          },
          // legend: legendData,
          grid: {
            top: 30,
            right: 20,
            bottom: 10,
            left: 10,
          },
          color: colorList,
          series: [
            {
              name: "",
              type: "pie",
              roundCap: true,
              radius: ["66%", "74%"],
              center: ["50%", "45%"],
              label: {
                show: false,
              },
              labelLine: {
                show: false,
              },
              data: pieData1,
            },
            {
              type: "pie",
              radius: ["66%", "55%"],
              center: ["50%", "45%"],
              gap: 1.71,
              label: {
                show: false,
              },
              labelLine: {
                show: false,
              },
              silent: true,
              data: pieData2,
            },
            {
              type: "gauge",
              zlevel: 2,
              splitNumber: 40,
              radius: "74%",
              center: ["50%", "45%"],
              startAngle: 90,
              endAngle: -269.9999,
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              splitLine: {
                show: true,
                length: 3,
                lineStyle: {
                  width: 2,
                  color: "rgba(33,85,130,.7)",
                },
              },
              pointer: {
                show: 0,
              },
              detail: {
                show: 0,
              },
            },
            {
              type: "pie",
              center: ["50%", "45%"],
              radius: [0, "45.6%"],
              label: {
                show: false,
              },
              labelLine: {
                show: false,
              },
              itemStyle: {
                color: "rgba(75, 126, 203,.1)",
              },
              silent: true,
              data: [
                {
                  value: 100,
                  name: "",
                },
              ],
            },
          ],
        };

        this.myChart1.setOption(option);
      });
    },
  },
};
</script>
<style scoped lang="scss">
::v-deep .el-range-editor--small .el-range-input {
  color: #9ba0bc !important;
  background-color: #051526 !important;
}
::v-deep .el-date-editor .el-range__icon {
  color: #9ba0bc !important;
}
::v-deep .el-range-separator {
  color: #9ba0bc !important;
  line-height: 20px !important;
}
.box {
  width: 100%;
  height: calc(100% - 30px);
  .topTime {
    float: right;
    ::v-deep .el-range-editor--small .el-range__icon,
    .el-range-editor--small .el-range__close-icon {
      line-height: 20px !important;
    }
    ::v-deep .el-range-editor.el-input__inner {
      background-color: #051526 !important;
      border: solid 1px #0c4b72 !important;
      color: #9ba0bc !important;
    }
  }
  .tabButton {
    height: 24px;
    margin: 8px 0;
    .el-radio-button {
      margin-right: 4px;
    }
    ::v-deep .el-radio-button--medium .el-radio-button__inner {
      padding: 4px 20px !important;
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
  .content {
    width: 100%;
    height: calc(100% - 64px);
    display: flex;
    .left {
      width: 45%;
      height: 100%;
    }
    .right {
      width: 55%;
      height: 100%;
      .rightRow1,
      .rightRow2 {
        width: 100%;
        height: 30px;
        line-height: 30px;
        display: flex;
        color: #9ba0bc;
        > div {
          width: 50%;
        }
      }
      .rightRow2 {
        > div {
          > span:first-of-type {
            font-size: 20px;
          }
        }
        > div:last-of-type {
          color: #fed37d;
        }
      }
      .right-text {
        width: 100%;
        height: calc(100% - 80px);
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin-left: 2%;
        .row {
          width: 100%;
          height: 2vh;
          // border-left: 3px solid rgba(40, 143, 219, 0.3);
          position: relative;
          padding-left: 0.6vw;
          box-sizing: border-box;
          margin-bottom: 1vh;
          display: flex;
          align-items: center;
          // background-image: linear-gradient(to right, #034782, rgba(3, 71, 130, 0));
        }
        .position_color {
          width: 3px;
          height: 70%;
          background: rgba(41, 151, 231, 1);
          position: absolute;
          left: 4px;
          top: 3px;
        }
        .row_top_text {
          display: inline-block;
          width: 50%;
          color: #c5d0e0;
          font-size: 0.7vw;
          transform: translateX(4px);
          letter-spacing: 1px;
        }
        .row_bottom_text {
          color: rgba(41, 151, 230, 1);
          font-size: 12px;
          font-weight: normal;
        }
      }
    }
  }
}
</style>