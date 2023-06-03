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
    <div id="charts3"></div>
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
          name: "当月",
        },
      ],
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    initCharts() {
      var myCharts = echarts.init(document.getElementById("charts3"));
      var option = {
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
          textStyle: {
            color: "#fff",
            fontSize: 10,
          },
          borderColor: "rgba(1, 29, 63,.8)",
          axisPointer: {
            type: "shadow",
            shadowStyle: {
              fontSize: 10,
              color: "rgba(0, 11, 34, 0)",
            },
          },
          formatter: function (params) {
            var relVal = params.marker
            relVal +='&nbsp'+ params.name + '&nbsp' + params.value + '%'
            return relVal
          }
        },
        grid: {
          top: "35",
          bottom: "40",
          left: "50",
          right:'30',
        },
        xAxis: {
          data: [
            "马家峪隧道",
            "青风岭隧道",
            "盘顶山隧道",
            "万昌溜隧道",
            "北甲峪隧道",
            "仰长天隧道",
            "双子山隧道",
            "马鞍山隧道",
            "金家楼隧道",
            "杭山东隧道",
          ],
          axisTick: {
            show: false, //隐藏X轴刻度
          },
          axisLine: {
            lineStyle: {
              color: "#11395D",
            },
          },
          axisLabel: {
            fontSize: 12,
            color: "#9ba0bc",
          },
        },
        yAxis: [
          {
            type: "value",
            splitLine: {
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
              fontFamily: "Bebas",
              formatter:'{value} %',
            },
          },
        ],
        series: [
          {
            type: "bar",
            barWidth: 9,
            itemStyle: {
              normal: {
                color: function (params) {
                  var color;
                  if (params.dataIndex == 2) {
                    color = {
                      type: "linear",
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [
                        {
                          offset: 0,
                          color: "#36FFF3", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "rgba(54,255,243,0)", // 100% 处的颜色
                        },
                      ],
                    };
                  } else {
                    color = {
                      type: "linear",
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [
                        {
                          offset: 0,
                          color: "#47B8FF",
                        },
                        {
                          offset: 1,
                          color: "rgba(63,133,238,0)",
                        },
                      ],
                    };
                  }
                  return color;
                },
              },
            },
            data: [60, 80, 100, 70, 50, 90, 60, 80, 70, 90],
            z: 10,
            zlevel: 0,
          },
          {
            // 分隔
            type: "pictorialBar",
            itemStyle: {
              normal: {
                color: "#000000",
              },
            },
            symbolRepeat: "fixed",
            symbolMargin: 3,
            symbol: "rect",
            symbolClip: true,
            symbolSize: [11, 2],
            symbolPosition: "start",
            symbolOffset: [0, 3],
            // symbolBoundingData: this.total,
            data: [60, 80, 100, 70, 50, 90, 60, 80, 70, 90],
            width: 25,
            z: 0,
            zlevel: 1,
          },
        ],
      };

      myCharts.setOption(option);
      window.addEventListener("resize", function () {
        myCharts.resize();
      });
    },
  },
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  .bigScreenSelect {
    width: 100px;
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
}
#charts3 {
  height: 220px;
}
</style>