<template>
  <div class="twoRanking">
    <div>
      <div class="contentTitle">
        隧道用电排行
        <i>Tunnel power consumption ranking</i>
      </div>
      <div
        class="alarmsStatisticsBox peakBackground"
        id="electricRanking"
      ></div>
    </div>
    <div>
      <div class="contentTitle">
        隧道能耗排行
        <i>Tunnel energy consumption ranking</i>
      </div>
      <div class="alarmsStatisticsBox peakBackground" id="energyRanking"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  data() {
    return {};
  },

  mounted() {
    this.electricRanking();
    this.energyRanking();
  },
  methods: {
    // 用电
    electricRanking() {
      var dataAxis = [
        "基本照明",
        "加强照明",
        "指示器",
        "信号灯",
        "主风机",
        "摄像机",
        "压力表",
        "引道路灯",
        "风速监测",
        "能见度监测",
      ];
      var data = [88, 65, 58, 56, 52, 47, 45, 40, 35, 30];
      var option = {
        color: ["#3398DB"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          left: "4%",
          right: "4%",
          bottom: "2%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: dataAxis,
            axisLine: {
              lineStyle: {
                type: "dashed",
                color: "#d9d9d9", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            axisLabel: {
              textStyle: {
                //改变刻度字体样式
                color: "#fff",
                fontSize: 14,
              },
              formatter: function (value) {
                return value.split("").join("\n");
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            formatter: "{value} (元)", //  单位
            axisLabel: {
              textStyle: {
                //改变刻度字体样式
                color: "#fff",
              },
            },
            axisLine: {
              lineStyle: {
                type: "solid",
                color: "#d9d9d9", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            // axisLine: {
            //   show: true, //y轴线消失
            // },
            axisTick: {
              show: true, //y轴坐标点消失
            },
          },
        ],
        series: [
          {
            name: "用电",
            type: "bar",
            barWidth: "20",
            data: data,
            itemStyle: {
              //柱形图圆角，鼠标移上去效果
              emphasis: {
                barBorderRadius: [10, 10, 10, 10],
              },
              normal: {
                //柱形图圆角，初始化效果
                barBorderRadius: [10, 10, 10, 10],
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: "#01b9ff", // 100% 处的颜色
                    },
                  ],
                  false
                ),
              },
            },
          },
        ],
      };
      var byId = function (id) {
        return document.getElementById(id);
      };
      var electricRanking = echarts.init(byId("electricRanking"));
      electricRanking.setOption(option);
    },
    // 能耗
    energyRanking() {
      let data = [88, 65, 58, 56, 52, 47, 45, 40, 35, 30];
      let dataAxis = [
        "基本照明",
        "加强照明",
        "指示器",
        "信号灯",
        "主风机",
        "摄像机",
        "压力表",
        "引道路灯",
        "风速监测",
        "能见度监测",
      ];
      var option = {
        color: ["#3398DB"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          left: "4%",
          right: "4%",
          bottom: "2%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: dataAxis,
            axisLine: {
              lineStyle: {
                type: "dashed",
                color: "#d9d9d9", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            axisLabel: {
              textStyle: {
                //改变刻度字体样式
                color: "#fff",
                fontSize: 14,
              },
              formatter: function (value) {
                return value.split("").join("\n");
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            formatter: "{value} (元)", //  单位
            axisLabel: {
              textStyle: {
                //改变刻度字体样式
                color: "#fff",
              },
            },
            axisLine: {
              lineStyle: {
                type: "solid",
                color: "#d9d9d9", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            // axisLine: {
            //   show: true, //y轴线消失
            // },
            axisTick: {
              show: true, //y轴坐标点消失
            },
          },
        ],
        series: [
          {
            name: "用电",
            type: "bar",
            barWidth: "20",
            data: data,
            itemStyle: {
              //柱形图圆角，鼠标移上去效果
              emphasis: {
                barBorderRadius: [10, 10, 10, 10],
              },
              normal: {
                //柱形图圆角，初始化效果
                // barBorderRadius: [10, 10, 10, 10],
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: "#0d8f5e", // 100% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#1cc789", // 100% 处的颜色
                    },
                  ],
                  false
                ),
              },
            },
          },
        ],
      };
      var byId = function (id) {
        return document.getElementById(id);
      };
      var energyRanking = echarts.init(byId("energyRanking"));
      energyRanking.setOption(option);
    },
  },
};
</script>


<style lang="less" scoped>
.twoRanking {
  width: 100%;
  height: 100%;
  // border: solid 1px white;
  display: flex;
  > div {
    width: 50%;
    // border: solid 1px white;
  }
}
.contentTitle {
  background-color: rgba(255, 255, 255, 0.2) !important;
  // background-color: rgba(4,15,78,0.4);
  i {
    color: rgba(255, 255, 255, 0.5);
  }
}
</style>
