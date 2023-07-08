<template>
  <div id="chart2"></div>
</template>
  <script>
import * as echarts from "echarts";
import { realTimeStat } from "@/api/bigScreen/model2";

export default {
  data() {
    return {
      myChart2: "",
      obj: {},
    };
  },
  // mounted() {
  //   this.$nextTick(() => {
  //     this.openChart();
  //   });
  // },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      realTimeStat().then((res) => {
        let errorList = res.data.errorList;
        let normalList = res.data.normalList;
        let xData = res.data.xData;
        let arrMin = [];
        let arrMax = [];
        let maxNum1 = Math.max(...errorList);
        let maxNum2 = Math.max(...normalList);
        let maxNum = maxNum1 > maxNum2 ? maxNum1 : maxNum2;
        for (var n = 0; n < res.data.xData.length; n++) {
          arrMin[n] = 0;
          arrMax[n] = maxNum;
        }
        this.openChart(errorList, normalList, xData, arrMin, arrMax);
      });
    },
    openChart(errorList, normalList, xData, arrMin, arrMax) {
      // this.$nextTick(function () {
        if (
          this.myChart2 != null &&
          this.myChart2 != "" &&
          this.myChart2 != undefined
        ) {
          // 销毁
          this.myChart2.dispose();
        }
        let e = document.getElementById("chart2")
        if(!e){
          return
        }
        this.myChart2 = echarts.init(e);
        // var newchartName = ["路测设备", "门架设备", "车道设备"],
        //   newchartValue1 = ["91", "51", "29"],
        //   newchartValue2 = ["9", "8", "7"];
        var barWidth = 16;
        var color1 = {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: "linear",
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: "#1C98CD",
            },
            {
              offset: 1,

              color: "rgba(61,187,255,.16)",
            },
          ],
        };
        var color2 = {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          type: "linear",
          global: false,
          colorStops: [
            {
              //第一节下面
              offset: 0,
              color: "#E7AB47",
            },
            {
              offset: 1,
              color: "rgba(255,164,41,.16)",
            },
          ],
        };
        var option = {
          legend: {
            data: ["正常", "异常"],
            icon: "circle",
            top: "7%",
            left: "center",
            itemWidth: 10,
            itemHeight: 10,
            textStyle: {
              color: "#9ba0bc",
              fontWeight: "normal",
              fontSize: 12,
            },
          },
          //提示框
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
            formatter: function (param) {
              var resultTooltip =
                "<div >" +
                "<div style='text-align:center;'>" +
                param[0].name +
                "</div>" +
                "<div style='padding-top:5px;'>" +
                "<span style='display:inline-block;border-radius:4px;width:20px;height:10px;background-color:rgba(61,187,255,.3);border: 2px solid #3eb6f5;'></span>" +
                "<span style=''> " +
                param[0].seriesName +
                ": </span>" +
                "<span style=''>" +
                param[0].value +
                "</span>" +
                "</div>" +
                "<div style='padding-top:5px;'>" +
                "<span style='display:inline-block;border-radius:4px;width:20px;height:10px;background-color:rgba(255,164,41,.3);border: 2px solid #ffc241;'></span>" +
                "<span style=''> " +
                param[1].seriesName +
                ": </span>" +
                "<span style=''>" +
                param[1].value +
                "</span>" +
                "</div>" +
                "</div>";
              return resultTooltip;
            },
          },
          title: [],
          grid: {
            top: "26%",
            left: "8%",
            bottom: "6%",
            right: "5%",
            containLabel: true,
          },
          dataZoom: [
            {
              moveOnMouseMove: true,
              type: "slider",
              show: false,
              xAxisIndex: [0],
              startValue: 0, // 数据窗口范围的起始数值
              endValue: 3, // 数据窗口范围的结束数值（一页显示多少条数据）
              minValueSpan: 0,
              maxValueSpan: 3,
            },
            {
              type: "inside",
              xAxisIndex: 0,
              zoomOnMouseWheel: false, //滚轮是否触发缩放
              moveOnMouseMove: true, //鼠标滚轮触发滚动
              moveOnMouseWheel: true,
            },
          ],
          animation: false,
          xAxis: [
            {
              type: "category",
              axisTick: {
                show: false,
              },
              axisLine: {
                show: true,
                lineStyle: {
                  color: "#11395D",
                },
              },
              axisLabel: {
                inside: false,
                textStyle: {
                  color: "#9ba0bc",
                  fontWeight: "normal",
                  fontSize: 12,
                },
                margin: 20, //刻度标签与轴线之间的距离。
              },
              data: xData,
            },
            {
              type: "category",
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: false,
              },
              splitArea: {
                show: false,
              },
              splitLine: {
                show: false,
              },
              data: xData,
            },
          ],
          yAxis: [
            {
              name: "个",
              nameTextStyle: {
                color: "#9ba0bc",
              },
              show: true,
              type: "value",
              axisLabel: {
                show: false,
                textStyle: {
                  color: "#fff",
                },
              },
              splitLine: {
                lineStyle: {
                  color: "#11395D",
                  type: "dashed", // dotted 虚线
                },
              },
              axisLine: {
                show: false,
              },
            },
          ],
          series: [
            // 头部椭圆
            {
              name: "正常",
              type: "pictorialBar",
              symbolSize: [barWidth, 10],
              symbolOffset: ["-81%", -5],
              symbolPosition: "end",
              z: 15,
              color: "#3eb6f5",
              zlevel: 2,
              data: normalList,
            },
            {
              name: "异常",
              type: "pictorialBar",
              symbolSize: [barWidth, 10],
              symbolOffset: ["81%", -5],
              symbolPosition: "end",
              z: 15,
              color: "#ffc241",
              zlevel: 2,
              data: errorList,
            },
            // 柱子平面
            {
              // name: "正常",
              type: "bar",
              barGap: "0%",
              barWidth: barWidth,
              showBackground: true, //背景色
              backgroundStyle: {
                shadowOffsetY: -15,
                color: "rgba(61,187,255,.16)",
              },
              itemStyle: {
                color: color1,
                borderColor: color1,
                borderWidth: 1,
                borderType: "solid",
              },
              label: {
                show: true,
                formatter: "{c}",
                position: "left",
                color: "rgba(119,167,255,1)",
                fontSize: 10,
                textAlign: "center",
              },
              zlevel: 2,
              data: normalList,
            },
            {
              // name: "异常",
              type: "bar",
              barGap: "60%",
              barWidth: barWidth,
              showBackground: true,
              backgroundStyle: {
                shadowOffsetY: -15,
                color: "rgba(255,164,41,.16)",
              },
              itemStyle: {
                color: color2,
                borderColor: color2,
                borderWidth: 1,
                borderType: "solid",
              },
              label: {
                show: true,
                formatter: "{c}",
                position: "right",
                color: "rgba(255,228,59,1)",
                fontSize: 10,
                textAlign: "center",
              },
              zlevel: 2,
              data: errorList,
            },

            // 底部椭圆
            {
              name: "正常",
              type: "pictorialBar",
              symbolSize: [barWidth, 10],
              symbolOffset: ["-81%", 5],
              z: 12,
              color: "#3eb6f5",
              data: arrMin,
            },
            {
              name: "异常",
              type: "pictorialBar",
              symbolSize: [barWidth, 10],
              symbolOffset: ["81%", 5],
              z: 12,
              color: "#ffc241",
              data: arrMin,
            },
            // 头顶椭圆动画
            {
              name: "正常",
              type: "effectScatter",
              rippleEffect: {
                period: 1,
                scale: 5,
                brushType: "fill",
              },
              z: 20,
              zlevel: 3,
              symbolPosition: "end",
              // symbol: path,
              symbolSize: [6, 3],
              symbolOffset: [-13, 0],
              itemStyle: {
                normal: {
                  shadowColor: "rgba(0, 0, 0, .3)",
                  shadowBlur: 5,
                  shadowOffsetY: 3,
                  shadowOffsetX: 0,
                  color: "rgba(119,167,255,1)",
                },
              },
              data: arrMax,
            },
            {
              name: "异常",
              type: "effectScatter",
              rippleEffect: {
                period: 1,
                scale: 5,
                brushType: "fill",
              },
              z: 20,
              zlevel: 2,
              symbolPosition: "end",
              symbolSize: [7, 4],
              symbolOffset: ["190%", 0],
              itemStyle: {
                normal: {
                  shadowColor: "rgba(0, 0, 0, .3)",
                  shadowBlur: 5,
                  shadowOffsetY: 3,
                  shadowOffsetX: 0,
                  color: "rgba(255,164,41,0.5)",
                },
              },
              data: arrMax,
            },
          ],
        };
        if (xData.length > 0) {
          let that = this;
          setInterval(function () {
            // 每次向后滚动一个，最后一个从头开始。
            if (option.dataZoom[0].endValue == normalList.length) {
              option.dataZoom[0].endValue = 4;
              option.dataZoom[0].startValue = 0;
            } else {
              option.dataZoom[0].endValue = option.dataZoom[0].endValue + 1;
              option.dataZoom[0].startValue = option.dataZoom[0].startValue + 1;
            }
            that.myChart2.setOption(option);
          }, 4000);
        }
        this.myChart2.setOption(option);
      // });
    },
  },
};
</script>
  <style scoped>
#chart2 {
  height: calc(100% - 30px);
}
</style>