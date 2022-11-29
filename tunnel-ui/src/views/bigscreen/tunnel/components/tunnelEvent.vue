<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-29 17:11:22
 * @FilePath: \tunnel-ui\src\views\bigscreen\tunnel\components\tunnelEvent.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="tunnelEvent-container">
    <div class="contentTitle">
      近30日隧道预警
      <i>Tunnel event</i>
    </div>
    <div ref="echartsBox" id="echarts-Box" class="echarts-Box"></div>
  </div>
</template>

<script>
import { getWarningnum } from "@/api/business/new";
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";

export default {
  data() {
    return {
      warningData: [
        9, 10, 15, 10, 11, 13, 7, 10, 8, 9, 0, 4, 5, 6, 8, 7, 6, 4, 2, 0, 1, 1,
        3, 6, 8, 10, 6, 5, 3, 0,
      ],
    };
  },
  mounted() {
    this.initChart();
    this.watchSize();
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    initChart() {
      getWarningnum().then((res) => {
        this.warningData = res.data;
      });

      var chartDom = document.getElementById("echarts-Box");
      var myChart = echarts.init(chartDom);
      var option;

      var xData = (function () {
        var data = [];
        for (var i = 30; i >= 1; i--) {
          data.push("近" + i + "日");
        }
        return data;
      })();

      option = {
        legend: {
          lineStyle: {
            type: "dashed",
          },
        },
        xAxis: {
          type: "category",
          data: this.getNearMounth(),

          axisLabel: {
            //y轴文字的配置
            textStyle: {
              color: "#ffffff", //Y轴内容文字颜色
            },
          },
          axisLine: {
            //y轴线的配置
            show: true, //是否展示
            lineStyle: {
              color: "#ffffff", //y轴线的颜色（若只设置了y轴线的颜色，未设置y轴文字的颜色，则y轴文字会默认跟设置的y轴线颜色一致）
              width: 1, //y轴线的宽度
              type: "solid", //y轴线为实线
            },
          },
        },
        yAxis: {
          type: "value",
          axisLabel: {
            //y轴文字的配置
            textStyle: {
              color: "#fff",
              margin: 15,
            },
            // formatter: '{value} %'//y轴的每一个刻度值后面加上‘%’号
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            //y轴线的颜色以及宽度
            show: false,
            lineStyle: {
              color: "#fff",
              width: 1,
              type: "solid",
            },
          },
          splitLine: {
            //分割线配置
            show: true,
            lineStyle: {
              color: "#446984",
              type: "dashed",
            },
          },
        },
        series: [
          {
            lineStyle: {
              color: "#4db6eb",
            },
            data: this.warningData,
            type: "line",
          },
        ],
      };

      option && myChart.setOption(option);
    },
    getNearMounth() {
      var timeArr = [];
      var time = new Date();
      var year = time.getFullYear();
      var lastMonth = time.getMonth();
      var nowMonth = time.getMonth() + 1;

      function getDaysInOneMonth(year, lastMonth) {
        //获取某年某月的天数函数
        lastMonth = parseInt(lastMonth, 10);
        var newTime = new Date(year, lastMonth, 0);
        return newTime.getDate();
      }
      var lastDays = getDaysInOneMonth(year, lastMonth); //上个月天数
      var nowDays = getDaysInOneMonth(year, nowMonth); //本月天数

      var day = time.getDate() + 1;
      for (let i = 0; i < nowDays; i++) {
        day = day - 1;
        if (day <= 0) {
          day = day + lastDays;
        }
        timeArr.push(day);
      }
      timeArr = timeArr.reverse();
      timeArr = timeArr.splice(1, nowDays);
      console.log(timeArr);
      return timeArr;
    },
  },
};
</script>

<style lang="less" scoped>
.tunnelEvent-container {
  width: 100%;
  height: 100%;
  font-size: 0.8vw;
  overflow: hidden;
  #echarts-Box {
    height: calc(100% - 1.7vw);
    width: 100%;
    > div {
      width: 100%;
      height: 100%;
      canvas {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>
