<template>
  <div class="windSpeedBox">
    <div class="title">
      风速
      <span style="font-size: 14px">(m/s)</span>
    </div>
    <div ref="echartsBox" id="windSpeed"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'
export default {
  props: {
    windData: {
      type: Object,
    },
  },
  data() {
    return {};
  },
  watch: {
    windData() {
      this.windSpeed();
    },
  },
  mounted() {
    this.windSpeed();
		this.watchSize()
  },
  methods: {
		watchSize() {
			let that = this;
			let erd = elementResizeDetectorMaker()
			let Dom = that.$refs.echartsBox;//拿dom元素
			//监听盒子的变化
			erd.listenTo(Dom, function (element) {
					let myChart = echarts.init(Dom);
					myChart.resize();//echarts自带的方法可以使图表重新加载
			})
    },
    windSpeed() {
      var chart = echarts.init(document.getElementById("windSpeed"));

      var option = {
        color: ["#55aa7f"],
        tooltip: {
          trigger: "axis",
        },
        grid: {
          left: "10%",
          right: "10%",
          bottom: "10%",
          top: "14%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: ["1月", "2月", "3月", "4月", "5月", "6月"],
            axisTick: {
              alignWithLabel: true,
            },
            nameTextStyle: {
              //关键代码
              padding: [30, 0, 0, -10],
            },
            axisLine: {
              lineStyle: {
                color: "#047B53",
                width: 1,
              },
            },
            show: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            minInterval: 1,
            splitLine: {
              show: false,
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#047B53",
                width: 1,
                type: "solid",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
          },
        ],
        series: [
          {
            type: "bar",
            barWidth: "60%",
            showSymbol: true,
            data: this.windData.data,
            itemStyle: {
              //柱形图圆角，鼠标移上去效果，如果只是一个数字则说明四个参数全部设置为那么多
              normal: {
                //柱形图圆角，初始化效果
                barBorderRadius: [5, 5, 0, 0],
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: "#013422", // 0% 处的颜色
                    },
                    {
                      offset: 0.6,
                      color: "#049578", // 60% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#00decc", // 100% 处的颜色
                    },
                  ],
                  false
                ),
              },
            },
          },
        ],
      };
      option && chart.setOption(option);
    },
  },
};
</script>

<style scoped="scoped">
.windSpeedBox {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
#windSpeed {
  width: 100%;
  height: 81%;
}
</style>
