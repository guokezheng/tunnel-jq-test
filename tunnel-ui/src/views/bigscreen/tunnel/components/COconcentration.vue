<template>
  <div class="COconcentrationBox">
    <div class="title">
      CO浓度
      <span style="font-size: 14px">(ppm)</span>
    </div>
    <div ref="echartsBox" id="COconcentration"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'
export default {
  props: {
    COData: {
      type: Object,
    },
  },
  data() {
    return {
      markPoint: {
        label: {
          show: true,
          position: "top",
          distance: 7,
          offset: [1, 1],
          formatter: "{c}ppm",
          color: "#19A2DE",
        },
        symbol: "circle",
        symbolSize: 5,
        symbolOffset: [0, 0],
        data: [{ type: "max" }],
      },
    };
  },
  watch: {
    COData() {
      this.COconcentration();
    },
  },
  mounted() {
    this.COconcentration();
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
    COconcentration() {
      var COconcentration = echarts.init(
        document.getElementById("COconcentration")
      );
      var option = {
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
            // boundaryGap: false,
            axisLabel: {
              //坐标轴刻度标签的相关设置
              textStyle: {
                color: "#FFFFFF",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
              },
            },
            // axisTick: {
            // 	show: false,
            // },
          },
        ],
        yAxis: [
          {
            // nameTextStyle: {
            // 	color: "#7ec7ff",

            // },
            type: "value",
            minInterval: 1,
            min: 0,
            // splitLine: {
            // 	show: false
            // },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#003476",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
              },
            },
            // axisTick: {
            // 	show: false,
            // },
            splitLine: {
              //分割线
              show: true, //控制分割线是否显示
              lineStyle: {
                //分割线的样式
                color: "#003476",
                width: 2,
                type: "solid",
              },
            },
          },
        ],
        series: [
          {
            type: "line",
            symbol: "circle", // 默认是空心圆（中间是白色的），改成实心圆
            showAllSymbol: true,
            symbolSize: 5,
            smooth: true,
            lineStyle: {
              normal: {
                width: 2,
                color: "rgba(25,163,223,1)", // 线条颜色
              },
            },
            itemStyle: {
              normal: {
                color: "rgba(25,163,223,1)",
                // borderColor:'red',  //拐点边框颜色
              },
            },
            // areaStyle: { //区域填充样式
            // 	normal: {
            // 		//线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
            // 		color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            // 				offset: 0,
            // 				color: "rgba(40, 27, 230, 0.3)"
            // 			},
            // 			{
            // 				offset: 1,
            // 				color: "rgba(255, 255, 255, 0.0)"
            // 			}
            // 		], false),
            // 		shadowColor: 'rgba(255, 255, 127, 0.5)', //阴影颜色
            // 		shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
            // 	}
            // },
            tooltip: {
              show: true,
            },
            // showSymbol: false,
            markPoint: this.markPoint,
            data: this.COData.data,
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      COconcentration.setOption(option);
    },
  },
};
</script>

<style scoped="scoped">
.COconcentrationBox {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
#COconcentration {
  width: 100%;
  height: 81%;
}
</style>
