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
        const wwc = res.data[0].eventNumber;
        const ywc = res.data[1].eventNumber;
        const percentage = res.data[1].percentage;
        var proportion = ((wwc / (wwc + ywc)) * 100).toFixed(2);
        var option = {
          // backgroundColor: "#03141c",
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
            formatter: "{a} <br/>{b} : {c}" + "个" + " ({d}%)",
          },
          legend: {
            x: "center",
            y: "bottom",
            data: ["rose3", "rose5", "rose6", "rose7", "rose8"],
          },
          calculable: true,
          series: [
            {
              name: "今日报警统计",
              type: "pie",
              radius: ["50%", "63%"],
              center: ["45%", "50%"],
              data: [
                {
                  value: wwc,
                  name: "已完成",
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                      {
                        offset: 0,
                        color: "#55ff7f",
                      },
                      {
                        offset: 1,
                        color: "#99ffff",
                      },
                    ]),
                  },
                  label: {
                    color: "rgba(255,255,255,.45)",
                    fontSize: 16,
                    formatter: "已完成\n{a|" + wwc + "}个",
                    rich: {
                      a: {
                        color: "#fff",
                        fontSize: 20,
                        lineHeight: 30,
                      },
                    },
                  },
                },
                {
                  value: ywc,
                  name: "未完成",
                  itemStyle: {
                    color: "transparent",
                  },
                  label: {
                    color: "rgba(255,255,255,.45)",
                    fontSize: 16,
                    formatter: "未完成\n{a|" + ywc + "}个",
                    rich: {
                      a: {
                        color: "#fff",
                        fontSize: 20,
                        lineHeight: 30,
                      },
                    },
                  },
                },
              ],
            },
            {
              name: "今日报警统计",
              type: "pie",
              radius: ["50%", "63%"],
              center: ["50%", "55%"],
              data: [
                {
                  value: wwc,
                  name: "已完成",
                  itemStyle: {
                    color: "transparent",
                  },
                },
                {
                  value: ywc,
                  name: "未完成",
                  //
                  itemStyle: {
                    normal: {
                      color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                        {
                          //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                          offset: 0,
                          color: "#0095ea",
                        },
                        {
                          offset: 0.5,
                          color: "#0095ea",
                        },
                        {
                          offset: 1,
                          color: "#0095ea",
                        },
                      ]),
                    },
                  },
                  // label: {
                  //   color: "rgba(255,255,255,.45)",
                  //   fontSize: 14,
                  //   // formatter: "未完成\n{a|52}个",
                  //   rich: {
                  //     a: {
                  //       color: "#fff",
                  //       fontSize: 20,
                  //       lineHeight: 30,
                  //     },
                  //   },
                  // },
                },
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
