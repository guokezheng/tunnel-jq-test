<template>
  <div>
    <div class="contentTitle">
      本月报警趋势
      <i>Alarms month</i>
    </div>
    <div class="alarmsStatisticsBox" id="monthCharts"></div>
  </div>
</template>

<script>
import { getSameMonthEventWarning } from "@/api/business/new";
import * as echarts from "echarts";
export default {
  data() {
    return {
      todayCharts: null,
    };
  },
  mounted() {
    this.alarmsCharts();
  },
  methods: {
    alarmsCharts() {
      var monthCharts = echarts.init(document.getElementById("monthCharts"));
      var month = new Date().getMonth() + 1;
      var date = new Date().getDate();
      var dateArr = [];
      getSameMonthEventWarning().then((res) => {
        // let dateValArr1 =
      });
      // 故障
      var dateValArr1 = [
        23, 25, 27, 24, 23, 17, 24, 20, 18, 16, 20, 25, 26, 28, 24, 24, 26, 28,
        25, 29, 27, 26, 25, 26, 25, 24, 28, 26, 24, 20, 21,
      ];
      // 事件
      var dateValArr2 = [
        26, 25, 26, 25, 24, 28, 26, 17, 24, 20, 23, 25, 27, 24, 23, 29, 27, 18,
        16, 20, 25, 26, 28, 24, 24, 26, 28, 25, 24, 20, 21,
      ];
      //预警
      var dateValArr3 = [
        26, 25, 24, 28, 26, 17, 24, 20, 18, 16, 20, 25, 26, 28, 24, 24, 23, 25,
        27, 24, 23, 29, 27, 26, 25, 26, 28, 25, 24, 20, 21,
      ];
      for (var i = 1; i <= date; i++) {
        dateArr.push(month + "-" + i);
      }
      var dateVal1 = dateValArr1.slice(0, date);
      var dateVal2 = dateValArr2.slice(0, date);
      var dateVal3 = dateValArr3.slice(0, date);
      var fontColor = "#30eee9";
      var option = {
        // backgroundColor:'#11183c',
        grid: {
          left: "5%",
          right: "10%",
          top: "25%",
          bottom: "0%",
          containLabel: true,
        },
        tooltip: {
          show: true,
          trigger: "axis",
        },
        legend: {
          show: true,
          x: "center",
          y: "20",
          icon: "stack",
          itemWidth: 10,
          itemHeight: 10,
          textStyle: {
            color: "#1bb4f6",
          },
          data: ["故障", "事件", "预警"],
        },
        xAxis: [
          {
            name: "日",
            type: "category",
            boundaryGap: false,
            axisLabel: {
              color: fontColor,
              rotate: 30,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#397cbc",
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: "#195384",
              },
            },
            data: dateArr,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "件",
            axisLabel: {
              formatter: "{value}",
              textStyle: {
                color: "#2ad1d2",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#27b4c2",
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: "#11366e",
              },
            },
          },
        ],
        series: [
          {
            name: "故障",
            type: "line",
            symbol: "circle",
            symbolSize: 8,
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              normal: {
                color: "#00d4c7",
                lineStyle: {
                  color: "#00d4c7",
                  width: 1,
                },
                // areaStyle: {
                //   //color: '#94C9EC'
                //   color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                //     {
                //       offset: 0,
                //       color: "rgba(7,44,90,0.3)",
                //     },
                //     {
                //       offset: 1,
                //       color: "rgba(0,146,246,0.9)",
                //     },
                //   ]),
                // },
              },
            },
            // markPoint:{
            // 	itemStyle:{
            // 		normal:{
            // 			color:'red'
            // 		}
            // 	}
            // },
            data: dateVal1,
          },
          {
            name: "事件",
            type: "line",
            symbol: "circle",
            symbolSize: 8,
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              normal: {
                color: "#59c5f9",
                lineStyle: {
                  color: "#59c5f9",
                  width: 1,
                },
                // areaStyle: {
                //   //color: '#94C9EC'
                //   color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                //     {
                //       offset: 0,
                //       color: "rgba(7,44,90,0.3)",
                //     },
                //     {
                //       offset: 1,
                //       color: "rgba(0,212,199,0.9)",
                //     },
                //   ]),
                // },
              },
            },
            data: dateVal2,
          },
          {
            name: "预警",
            type: "line",
            symbol: "circle",
            symbolSize: 8,
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              normal: {
                color: "#fbb102",
                lineStyle: {
                  color: "#fbb102",
                  width: 1,
                },
                // areaStyle: {
                //   //color: '#94C9EC'
                //   color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                //     {
                //       offset: 0,
                //       color: "rgba(7,44,90,0.3)",
                //     },
                //     {
                //       offset: 1,
                //       color: "rgba(114,144,89,0.9)",
                //     },
                //   ]),
                // },
              },
            },
            data: dateVal3,
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      monthCharts.setOption(option);
    },
  },
};
</script>

<style>
</style>
