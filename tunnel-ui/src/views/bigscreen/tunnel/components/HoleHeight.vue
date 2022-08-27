<template>
  <div class="HoleHeightBox">
    <div class="title">洞口亮度 cd/m2</div>
    <div ref="echartsBox" id="HoleHeight"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'
export default {
  props: {
    luminanceData: {
      type: Object,
    },
  },
  data() {
    return {};
  },
  watch: {
    luminanceData() {
      this.HoleHeight();
    },
  },
  mounted() {
    this.HoleHeight();
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
    HoleHeight() {
      var HoleHeight = echarts.init(document.getElementById("HoleHeight"));
      var option = {
        tooltip: {
          trigger: "axis",
        },
        grid: {
          left: "10%",
          right: "12%",
          bottom: "10%",
          top: "14%",
          containLabel: true,
        },
        // legend: {
        //     data: ['销售量'],
        //     textStyle: { // 图例文字颜色设置。
        //         color: 'blue'
        //     }
        // },

        xAxis: [
          {
            type: "category",
            data: ["1月", "2月", "3月", "4月", "5月", "6月"],
            boundaryGap: false,
            axisLabel: {
              //坐标轴刻度标签的相关设置
              textStyle: {
                color: "#FFFFFF",
                padding: 5,
                fontSize: 12,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#173164",
              },
            },
            axisTick: {
              show: false,
            },
          },
        ],
        yAxis: [
          {
            minInterval: 1,
            min: 0,
            splitLine: {
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#173164",
              },
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#FFFFFF",
                padding: 5,
              },
              formatter: function (value) {
                if (value === 0) {
                  return value;
                }
                return value;
              },
            },
            axisTick: {
              show: false,
            },
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
            symbolSize: 5, //折线点的大小
            smooth: true,
            lineStyle: {
              normal: {
                width: 2,
                color: "#E6A001", // 线条颜色
              },
            },
            itemStyle: {
              normal: {
                label: {
                  show: true,
                  color: "white",
                },
                color: "#E6A001",
                // borderColor:'red',  //拐点边框颜色
              },
              emphasis: {
                color: "white",
                borderColor: "white",
              },
            },
            tooltip: {
              show: true,
            },
            areaStyle: {
              //区域填充样式
              normal: {
                //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(255, 175, 1, 0.3)",
                    },
                    {
                      offset: 1,
                      color: "rgba(255, 255, 255, 0.0)",
                    },
                  ],
                  false
                ),
                shadowColor: "rgba(255, 255, 127, 0.5)", //阴影颜色
                shadowBlur: 20, //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
              },
            },
            data: this.luminanceData.data,
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      HoleHeight.setOption(option);
    },
  },
};
</script>

<style scoped="scoped">
.HoleHeightBox {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
#HoleHeight {
  width: 100%;
  height: 81%;
}
</style>
