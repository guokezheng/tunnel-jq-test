<template>
  <div id="perceivedEvent"></div>
</template>
  <script>
import * as echarts from "echarts";
import { findEventStat } from "@/api/bigScreen/model1";
export default {
  data() {
    return {
      dayArr: [],
    };
  },
  created() {
    this.getList();
    this.dateFormat();
  },
  methods: {
    // 获取最近七天数组
    dateFormat() {
      const days = 7; // 近7天
      const dateList = Array.from({ length: days }, (v, i) => i).map((day) => {
        const date = new Date();
        date.setDate(date.getDate() - day);
        return date.getMonth() + 1 + "-" + date.getDate();
      });
      this.dayArr = dateList.reverse();
    },
    getList() {
      findEventStat().then((res) => {
        let list = res.data;
        console.log(list,"list")
        this.$nextTick(() => {
          this.initEchats1(list);
        });
      });
    },
    initEchats1(chartData) {
      this.$nextTick(function () {
        if (
          this.myChart1 != null &&
          this.myChart1 != "" &&
          this.myChart1 != undefined
        ) {
          // 销毁
          this.myChart1.dispose();
        }
        let e = document.getElementById("perceivedEvent")
        if(!e){
          return
        }
        this.myChart1 = echarts.init(e);

        const option = {
          // color: [
          //   "rgba(84, 181, 157, 1)",
          //   "rgba(31, 149, 215, 1)",
          //   "rgba(239, 175, 76, 1)",
          //   "rgba(55, 231, 255, 1)",
          // ],
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", // 设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "shadow",
              shadowStyle: {
                fontSize: 12,
                color: "rgba(0, 11, 34, 0)",
              },
            },
          },
          legend: {
            show: true,
            data: [
              "变道",
              "超速",
              "电话",
              "火灾",
              "慢行",
              "逆行",
              "停车",
              "应急车道",
            ],
            textStyle: {
              color: "#9ba0bc",
              fontSize: 12,
            },
            itemWidth: 10,
            itemHeight: 10,
            itemStyle: {},
            top: "top",
            left: "center",
            padding: [20, 15, 0, 15],
            icon: "circle",
            orient: "horizontal",
          },
          grid: {
            left: "3%",
            right: "3%",
            bottom: "6%",
            top: "40%",
            containLabel: true,
          },

          xAxis: {
            type: "category",
            data: this.dayArr,
            axisLine: {
              lineStyle: {
                color: "#11395D",
              },
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: {
            type: "value",
            min: 0,
            minInterval: 1,
            splitArea: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              lineStyle: {
                color: "#11395D",
                type: "dashed", // dotted 虚线
              },
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
              fontFamily: "Bebas",
            },
          },

          series: [
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "变道",
              data: chartData.biandao,
              
            },
            ,
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,// 显示折点
              lineStyle: {
                width: 1,
              },
              name: "超速",
              data: chartData.chaosu,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "电话",
              data: chartData.dianhua,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "火灾",
              data: chartData.huozai,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "慢行",
              data: chartData.manxing,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "逆行",
              data: chartData.nixing,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "停车",
              data: chartData.tingche,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "应急车道",
              data: chartData.yingjichedao,
            },
          ],
        };
        this.myChart1.setOption(option);
      });
    },
  },
};
</script>
  <style scoped>
#perceivedEvent {
  height: calc(100% - 30px);
}
</style>