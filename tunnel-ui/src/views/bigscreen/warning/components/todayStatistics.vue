<!-- 大屏今日报警 -->
<template>
  <div>
    <div class="contentTitle">
      今日报警统计
      <i>Alarms today</i>
    </div>
    <div class="alarmsStatisticsBox" id="todayCharts"></div>
  </div>
</template>

<script>
import { getToDayEventWarning } from "@/api/business/new";
import * as echarts from "echarts";
export default {
  data() {
    return {
      todayCharts: null,
    };
  },
  mounted() {
    this.alarmsCharts();
    this.setTimeout(startTimer, 0);
  },
  methods: {
    alarmsCharts() {
      var todayCharts = echarts.init(document.getElementById("todayCharts"));
      getToDayEventWarning().then((res) => {
        console.log(res);
        const wwc = res.data[0].eventNumber;
        const ywc = res.data[1].eventNumber;
        const percentage = res.data[0].percentage;
        var option = {
          title: {
            text: percentage + "%",
            subtext: "未完成",
            x: "center",
            y: "center",
            textStyle: {
              color: "#fff",
              fontSize: 30,
              fontWeight: "normal",
            },
            subtextStyle: {
              color: "rgba(255,255,255,.45)",
              fontSize: 14,
              fontWeight: "normal",
            },
          },
          tooltip: {
            trigger: "item",
          },
          legend: {
            top: "5%",
            left: "center",
            textStyle: {
              color: "#fff",
            },
          },
          series: [
            {
              name: "今日报警统计",
              type: "pie",
              radius: ["40%", "70%"],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: "#fff",
                borderWidth: 1,
              },
              label: {
                show: false,
                position: "center",
              },
              // emphasis: {
              //   label: {
              //     show: true,
              //     fontSize: 24,
              //   },
              // },
              labelLine: {
                show: false,
              },
              data: [
                { value: ywc, name: "已完成" },
                { value: wwc, name: "未完成" },
                // { value: 22, name: "已完成" },
                // { value: 78, name: "未完成" },
              ],
            },
          ],
        };
        // // 使用刚指定的配置项和数据显示图表。
        todayCharts.setOption(option);
      });
    },
  },
};
</script>

<style>
</style>
