<template>
  <div id="charts6"></div>
</template>
  <script>
import * as echarts from "echarts";
import { realTimeCarFlow } from "@/api/bigScreen/model1";

export default {
  data() {
    return {
      myChart2: null,
    };
  },
  created() {
    this.getList()
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    getList() {
      realTimeCarFlow().then((res) => {
        // this.initCharts();
      });
    },
    initCharts() {
      this.$nextTick(function () {
        if (
          this.myChart2 != null &&
          this.myChart2 != "" &&
          this.myChart2 != undefined
        ) {
          // 销毁
          this.myChart2.dispose();
        }
        this.myChart2 = echarts.init(document.getElementById("charts6"));
        var option = {
          title: {
            show: false,
            text: "",
            textStyle: {
              fontSize: 20,
              color: "#ffffff",
              width: "300",
              height: 40,
              lineHeight: 40,
              overflow: "truncate",
              ellipsis: "...",
            },
            padding: [10, 15],
            left: "left",
            // 副标题
            subtext: "",
            subtextStyle: {
              show: false,
              fontSize: 16,
              color: "#ffffff",
              width: "300",
              height: 20,
              lineHeight: 20,
              overflow: "truncate",
              ellipsis: "...",
              align: "center",
            },
          },
          legend: {
            show: true,
            data: ["小型车", "中型车", "大型车"],
            textStyle: {
              color: "#9ba0bc",
              fontSize: 12,
            },
            itemStyle: {},
            top: "top",
            left: "center",
            padding: [15, 15, 0, 15],
            icon: "circle",
            orient: "horizontal",
          },
          grid: {
            containLabel: true,
            left: 15,
            right: 15,
            bottom: 10,
            top: 50,
          },
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
          xAxis: {
            // nameTextStyle: {
            //   color: "#9ba0bc",
            //   fontSize: 12,
            // },
            axisLabel: {
              show: true,
              color: "#9ba0bc",
              fontSize: 12,
              rotate: 60,
              fontFamily: "Microsoft YaHei",
            },
            // axisTick: {
            //   show: true,
            //   lineStyle: {
            //     color: "#dadada",
            //     width: 1,
            //   },
            // },
            axisLine: {
              lineStyle: {
                color: "#11395D",
              },
              show: false,
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: "#11395D",
                type: "dashed",
              },
            },
            axisTick: {
              show: false,
            },
            data: [
              "杭山东隧道",
              "马鞍山隧道",
              "金家楼隧道",
              "凤凰山隧道",
              "清风岭隧道",
              "盘顶山隧道",
              "万昌溜隧道",
              "北甲峪隧道",
              "仰长天隧道",
              "双子山隧道",
            ],
            type: "category",
          },
          yAxis: {
            name: "辆",
            nameTextStyle: {
              color: "#9ba0bc",
            },
            axisLabel: {
              show: true,
              color: "#9ba0bc",
              fontSize: 12,
              fontFamily: "Microsoft YaHei",
            },
            axisTick: {
              lineStyle: {
                color: "rgba(255,255,255,0.12)",
                width: 1,
              },
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: "#11395D",
                type: "dashed",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#11395D",
                width: 1,
                // "type": "dashed"
              },
              show: false,
            },
          },
          series: [
            {
              name: "小型车",
              data: [120, 200, 150, 80, 70, 110, 130, 110, 140, 80],
              type: "bar",
              stack: "one",
              barWidth: 10,
              itemStyle: {
                borderWidth: 1,
                borderColor: "#000", //同背景色一样
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  {
                    offset: 0,
                    color: "#1699DB",
                  },
                  {
                    offset: 0.3,
                    color: "#1699DB",
                  },
                  {
                    offset: 1,
                    color: "#0E5B82",
                  },
                ]),
                barBorderRadius: 120,
              },

              emphasis: {
                itemStyle: {
                  borderColor: "rgba(255, 255, 255, 1)",
                },
              },
            },
            {
              name: "中型车",
              data: [120, 30, 150, 80, 70, 110, 130, 70, 50, 30],
              type: "bar",
              barWidth: 10,
              stack: "one", //堆叠
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: "#E1B44B",
                },
                {
                  offset: 0.3,
                  color: "#E1B44B",
                },
                {
                  offset: 1,
                  color: "#4B3607",
                },
              ]),
              itemStyle: {
                borderWidth: 1, //用border设置两个柱形图之间的间距
                borderColor: "#000", //同背景色一样
                barBorderRadius: 120,
              },
              emphasis: {
                itemStyle: {
                  borderColor: "rgba(255, 255, 255, 1)",
                },
              },
            },
            {
              name: "大型车",
              data: [120, 30, 150, 80, 70, 110, 130, 60, 50, 30],
              type: "bar",
              barWidth: 10,
              stack: "one", //堆叠
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: "#32B391",
                },
                {
                  offset: 0.3,
                  color: "#32B391",
                },
                {
                  offset: 1,
                  color: "#093A2D",
                },
              ]),
              itemStyle: {
                borderWidth: 1, //用border设置两个柱形图之间的间距
                borderColor: "#000", //同背景色一样
                barBorderRadius: 120,
              },
              emphasis: {
                itemStyle: {
                  borderColor: "rgba(255, 255, 255, 1)",
                },
              },
            },
          ],
        };

        this.myChart2.setOption(option);
        // window.addEventListener("resize", function () {
        //   this.myChart2.resize();
        // });
      });
    },
  },
};
</script>
  <style scoped lang="scss">
#charts6 {
  width: 100%;
  height: calc(100% - 30px);
  div {
    width: 100% !important;
  }
}
</style>