<template>
  <div id="chart2"></div>
</template>
  <script>
import * as echarts from "echarts";
import { eventStat } from "@/api/bigScreen/model1";
export default {
  data() {
    return {
      myChart2: "",
      list: {},
      monthStrings: [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月",
      ],
    };
  },
  created() {
    this.getList();
  },
  mounted() {},
  methods: {
    getList() {
      eventStat().then((res) => {
        console.log(res, "分隧道事件统计");
        let list = res.data;
        // 到当月月份数组
        // let month = new Date().getMonth() + 1
        // let laneArray = JSON.parse(JSON.stringify(this.monthStrings));
        // list.xData = laneArray.slice(0, month);
        this.$nextTick(() => {
          this.openChart(list);
        });
      });
    },
    openChart(chartData) {
      this.$nextTick(function () {
        if (
          this.myChart2 != null &&
          this.myChart2 != "" &&
          this.myChart2 != undefined
        ) {
          // 销毁
          this.myChart2.dispose();
        }
        this.myChart2 = echarts.init(document.getElementById("chart2"));

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
            data: chartData.xData,
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
        this.myChart2.setOption(option);
      });
    },
  },
};
</script>
  <style scoped>
#chart2 {
  height: 26vh;
}
</style>