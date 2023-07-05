<template>
  <div id="charts5"></div>
</template>
  <script>
import * as echarts from "echarts";
// import { realTimeCarFlow } from "@/api/bigScreen/model1";
export default {
  data() {
    return {
      myChart2: null,
    };
  },
  created() {
    // this.getList()
  },

  methods: {
    // getList(){
    //   realTimeCarFlow().then((res)=>{
    //     this.initCharts();
    //   })
    // },
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
        this.myChart2 = echarts.init(document.getElementById("charts5"));
        var option = {
          grid: {
            containLabel: true,
            top: 50,
            right: 15,
            bottom: 10,
            left: 15,
          },
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", // 设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            formatter: "{b}&nbsp&nbsp&nbsp" + this.getVal("{c}")
          },
          xAxis: {
            // 类目轴
            type: "category",
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
            // 轴线设置
            axisLine: {
              lineStyle: {
                color: "#11395D",
              },
            },
            // 坐标轴刻度
            axisTick: {
              show: false,
            },
            // 坐标轴标签
            axisLabel: {
              show: true,
              fontSize: 12,
              // interval: 0,
              color: "#9ba0bc",
              rotate: 60,
              fontFamily: "SourceHanSansCN-Normal, SourceHanSansCN",
            },
          },
          yAxis: {
            name: "等级",
            nameTextStyle: {
              color: "#9ba0bc",
            },
            // 最小间隔
            minInterval: 1,
            // 轴线设置
            axisLine: {
              lineStyle: {
                color: "#11395D",
              },
            },
            // 坐标轴刻度
            axisTick: {
              show: false,
            },
            // 坐标轴标签
            axisLabel: {
              show: true,
              fontSize: 12,
              color: "#9ba0bc",
              fontFamily: "SourceHanSansCN-Normal, SourceHanSansCN",
              // 这里重新定义就可以
              formatter: function (value) {
                var texts = [];
                if (value == 1) {
                  texts.push("畅通");
                } else {
                  texts.push("0");
                }
                return texts;
              },
            },
            // grid区分割线
            splitLine: {
              show: true,
              lineStyle: { color: "#11395D", type: "dashed" },
            },
          },
          series: [
            {
              type: "bar",
              barWidth: "10",
              showBackground: true,
              backgroundStyle: {
                color: "rgba(14,58,99, .5)",
              },
              itemStyle: {
                barBorderRadius: [15, 15, 15, 15],
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: "rgba(22,154,221,1)" },
                  { offset: 1, color: "rgba(22,154,221, 0)" },
                ]),
              },
              emphasis: {
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: "#83bff6" },
                    { offset: 1, color: "#2378f7" },
                  ]),
                },
              },
              data: [1, 1, 1, 1, 1, 1, 1, 0, 0, 0],
            },
          ],
        };
        this.myChart2.setOption(option);
        window.addEventListener("resize", function () {
          this.myChart2.resize();
        });
      });
    },
    getVal(num){
      if(num == 1){
        return "畅通"
      }else{
        return "拥堵"
      }
    },
  },
};
</script>
  <style scoped lang="scss">
#charts5 {
  width: 100%;
  height: calc(100% - 30px);
  div {
    width: 100% !important;
  }
}
</style>