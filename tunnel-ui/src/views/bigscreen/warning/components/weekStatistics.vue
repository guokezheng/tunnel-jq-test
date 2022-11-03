<template>
  <div>
    <div class="contentTitle">
      本周报警统计
      <i>Alarms week</i>
    </div>
    <div class="alarmsStatisticsBox" id="weekCharts"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  data() {
    return {
      todayCharts: null,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.alarmsCharts();
    });
  },
  methods: {
    alarmsCharts() {
      var weekCharts = echarts.init(document.getElementById("weekCharts"));

      var wwc = 8;
      var ywc = 86;
      var proportion = ((wwc / (wwc + ywc)) * 100).toFixed(2);
      var option = {
        // backgroundColor: "#03141c",
        title: {
          text: proportion + "%",
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
            name: "本周报警统计",
            type: "pie",
            radius: ["47%", "80%"],
            center: ["50%", "50%"],
            data: [
              {
                value: wwc,
                name: "未完成",
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                    {
                      offset: 0,
                      color: "#ffaa00",
                    },
                    {
                      offset: 1,
                      color: "#fffb82",
                    },
                  ]),
                },
                label: {
                  color: "rgba(255,255,255,.45)",
                  fontSize: 14,
                  formatter: "未完成\n{a|" + wwc + "}个",
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
                name: "已完成",
                itemStyle: {
                  color: "transparent",
                },
                label: {
                  color: "rgba(255,255,255,.45)",
                  fontSize: 14,
                  formatter: "已完成\n{a|" + ywc + "}个",
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
            radius: ["55%", "75%"],
            center: ["50%", "50%"],
            data: [
              {
                value: wwc,
                name: "",
                itemStyle: {
                  color: "transparent",
                },
              },
              {
                value: ywc,
                name: "rose2",
                //
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                      {
                        //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上

                        offset: 0,
                        color: "#00a2ff",
                      },
                      {
                        offset: 0.5,
                        color: "#00a2ff",
                      },
                      {
                        offset: 1,
                        color: "#00a2ff",
                      },
                    ]),
                  },
                },
                label: {
                  color: "rgba(255,255,255,.45)",
                  fontSize: 14,
                  formatter: "部门总量\n{a|52}个",
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
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      weekCharts.setOption(option);
      // 设置默认突出区域
      // weekCharts.dispatchAction({
      //   type: 'highlight', //突出高亮显示;
      //   seriesIndex: 0,
      //   dataIndex: 1, //默认索引为0，即10%
      // });
    },
  },
};
</script>

<style>
</style>
