<template>
  <div class="box">
    <div style="height: 30px">
      <el-select v-model="selectModel" size="small" class="bigScreenSelect">
        <el-option
          v-for="item in selectList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
    <div class="content">
      <div class="left">
        <div>隧道整体</div>
        <div class="num">-</div>
        <div>评价分值</div>
        <div class="num">98</div>
        <div>技术状况</div>
        <div class="num">完好</div>
      </div>
      <div class="zong_shu">
        <div
          id="duoChart1"
          ref="echartsA"
          style="width: 100%; height: 100%"
        ></div>
        <div
          class="zong_shu_item1"
          v-for="item in chuNenList"
          :key="item.id"
          :style="{ left: item.le, top: item.to }"
        >
          <span>{{ item.name }}</span>
          <!-- <span style="margin-left: 0.3vw;color: #fff;font-size: 14px;"> {{ item.num }}</span>
                <span style="margin-left: 0.3vw;color: #fff;font-size: 12px;">{{ item.unit }}</span> -->
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
      selectModel: 1,
      selectList: [
        {
          id: 1,
          name: "2020-01-05 全寿命评价",
        },
      ],
      duoChart1: "",
      chuNenList: [
        { id: 1, name: "土建结构1级 完好", le: "7%", to: "41%" },
        { id: 2, name: "附属设备2级 良好", le: "38%", to: "76%" },
        { id: 3, name: "机电系统1级 完好", le: "38%", to: "4%" },
        { id: 4, name: "运行服务1级 完好", le: "73%", to: "41%" },
      ],
    };
  },
  mounted() {
    this.openChart();
  },
  methods: {
    openChart() {
      this.$nextTick(function () {
        if (
          this.duoChart1 != null &&
          this.duoChart1 !== "" &&
          this.duoChart1 !== undefined
        ) {
          // 销毁
          this.duoChart1.dispose();
        }
        this.duoChart1 = echarts.init(document.getElementById("duoChart1"));
        const option = {
          color: ["red"],
          tooltip: {
            show: true,
            textStyle: {
              fontSize: 12,
              color: "#fff",
            },
            backgroundColor: "rgba(1, 29, 63, 0.8)",
            borderColor: "rgba(1, 29, 63,0.8)",
          },
          radar: {
            radius: "50%",
            center: ["50%", "50%"],
            splitNumber: 10,
            nameGap: "10",
            name: {
              show: false,
              textStyle: {
                color: "#646464",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#076CFF",
              },
            },
            splitLine: {
              lineStyle: {
                width: 0,
              },
            },
            splitArea: {
              areaStyle: {
                color: [
                  "#0875bd",
                  "#0875bd",
                  "#0875bd",
                  "#0875bd",
                  "#0875bd",
                  "#0875bd",
                  "#0b6dac",
                  "#0c6198",
                  "#11537e",
                  "#13425d",
                ],
              },
            },
            indicator: [
              {
                name: "放电",
                max: 150,
              },
              {
                name: "温度",
                max: 150,
              },
              {
                name: "充电",
                max: 150,
              },
              {
                name: "SOC",
                max: 150,
              },
            ],
          },
          series: [
            {
              name: "隧道整体",
              type: "radar",
              symbolSize: 5,
              areaStyle: {
                normal: {
                  width: 0,
                  opacity: 0.4,
                },
              },
              lineStyle: {
                normal: {
                  width: 1,
                  type: "dashed",
                },
              },
              data: [
                {
                  itemStyle: {
                    normal: {
                      color: "#fdd07b",
                    },
                    shadowColor: "#35F3FF",
                    shadowBlur: 20,
                  },
                  value: [100, 100, 80, 100],
                },
              ],
            },
          ],
        };
        this.duoChart1.setOption(option);
      });
    },
  },
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  .bigScreenSelect {
    float: right;
    padding-top: 4px;
    ::v-deep .el-input--small .el-input__inner {
      height: 28px !important;
      line-height: 28px !important;
      background-color: rgba(0,0,0,0.5) !important;
      border: solid 1px #0c4b72 !important;
      color: #9ba0bc !important;
    }
  }
  .content {
    width: 100%;
    height: calc(100% - 30px);
    display: flex;
    .left {
      width: 30%;
      height: 100%;
      text-align: center;
      > div {
        height: 11%;
        color: #9ba0bc;
      }
      .num {
        font-size: 20px;
        color: #fbd750;
        height: 18% !important;
      }
    }
    .zong_shu {
      width: 100%;
      height: 100%;
      position: relative;
      .zong_shu_item1 {
        width: 3vw;
        height: 4vh;
        position: absolute;
        display: flex;
        align-items: center;
        word-wrap: break-word;
        border: solid 1px #0c4b72;
        background-color: #051526;
        border-top-right-radius: 8px;
        border-bottom-left-radius: 8px;
        padding: 2px;
        color: #9ba0bc;
        font-size: 12px;
      }
    }
  }
}
</style>