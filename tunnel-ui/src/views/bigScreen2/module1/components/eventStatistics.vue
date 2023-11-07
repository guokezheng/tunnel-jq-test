<template>
  <div id="eventchart"></div>
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
        console.log(res.data,"分隧道事件统计")
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
        let e = document.getElementById("eventchart")
        if(!e){
          return
        }
        this.myChart2 = echarts.init(e);

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
        this.myChart2.setOption(option);
      });
    },
  },
};
</script>
  <style scoped>
#eventchart {
  height: calc(100% - 30px);
}
</style>