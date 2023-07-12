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
              "隧道停电",
              "洞口边坡塌方",
              "隧道衬砌垮塌",
              "隧道渗漏水",
              "电话",
              "火灾",
              "非机动车",
              "行人",
              "事故",
              "停车",
              "遗散",
              "拥堵",
              "逆行",
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
              name: "拥堵",
              data: chartData.yongdu,
              
            },
            ,
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,// 显示折点
              lineStyle: {
                width: 1,
              },
              name: "行人",
              data: chartData.xingren,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "非机动车",
              data: chartData.feijidongche,
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
              name: "洞口边坡塌方",
              data: chartData.dongkoubianpotafang,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "事故",
              data: chartData.shigu,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "隧道衬砌垮塌",
              data: chartData.suidaochenqikuata,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "隧道渗漏水",
              data: chartData.suidaoshenloushui,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "隧道停电",
              data: chartData.suidaotingdian,
            },
            {
              type: "line",
              smooth: true, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                width: 1,
              },
              name: "遗散",
              data: chartData.yisan,
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