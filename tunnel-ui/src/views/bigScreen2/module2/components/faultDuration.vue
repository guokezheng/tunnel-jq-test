<template>
  <div class="barQyEchart">
    <div id="faultDurationCharts"></div>
  </div>
</template>
  
  <script>
import * as echarts from "echarts";
import { faultTimeTop } from "@/api/bigScreen/model2";
export default {
  name: "BarQyEchart",
  data() {
    return {
      myChart: null,
      obj: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      faultTimeTop().then((res) => {
        this.obj = res.data;
        this.initCharts();
      });
    },
    initCharts() {
      this.myChart = echarts.init(document.getElementById("faultDurationCharts"));
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
        },
        grid: {
          top: "50",
          bottom: "10",
          left: "10",
          containLabel: true,
        },
        xAxis: {
          data: this.obj.xData,
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
            rotate: 60,
          },
          //x轴换行
          // axisLabel: {
          //   fontSize: 12,
          //   color: "#9ba0bc",
          //   rotate: 60,
          //   interval: 0, //0：全部显示，1：间隔为1显示对应类目
          //   formatter: function (value, index) {
          //     var num = 5; //每行显示字数
          //     if (index == 1) {
          //       num = 4;
          //     }
          //     if (index == 2 || index == 3) {
          //       num = 6;
          //     }
          //     var str = "";
          //     var valLength = value.length; //该项x轴字数
          //     var rowNum = Math.ceil(valLength / num); // 行数
          //     if (rowNum > 1) {
          //       for (var i = 0; i < rowNum; i++) {
          //         var temp = "";
          //         var start = i * num;
          //         var end = start + num;
          //         temp = value.substring(start, end) + "\n";
          //         str += temp;
          //       }
          //       return str;
          //     } else {
          //       return value;
          //     }
          //   },
          // },
        },
        yAxis: [
          {
            name: "小时",
            nameTextStyle: {
              color: "#9ba0bc",
            },
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
                  if (params.dataIndex == 0) {
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
              /*normal: {
                  color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: "#47B8FF"
                  },
                    {
                      offset: 1,
                      color: "rgba(63,133,238,0)"
                    }
                  ])
                }*/
            },
            data: this.obj.list,
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
            data: this.obj.list,
            width: 25,
            z: 0,
            zlevel: 1,
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
  #faultDurationCharts {
    height: 100%;
  }
}
</style>
  