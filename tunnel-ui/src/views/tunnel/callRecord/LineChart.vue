<template>
  <div :class="className" :style="{ height: height, width: width, }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "../../dashboard/mixins/resize";
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "400px",
    },
    autoResize: {
      type: Boolean,
      default: true,
    },
    chartData: {
      type: [Object, Array],
      required: true,
    },
  },
  data() {
    return {
      chart: null,
      tunnelData: null,
      statisticsList: [],
    };
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      },
    },
  },
  created() {
    this.getTunnel();
  },
  mounted() {
    this.$nextTick(()=>{
      this.initChart();
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    /** 查询隧道列表 */
    async getTunnel() {
      /* .then((data)=>{ })里的data是指接口成功返回的数据,包含请求头,请求体,等信息; */
      await listTunnels().then((response) => {
        this.tunnelData = response.rows;
        this.setOptions(this.chartData);
      });
    },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");
    },
    setOptions(chartData) {
      if (this.tunnelData) {
        this.statisticsList = []
        this.tunnelData.forEach((item,index) => {
          this.statisticsList.push({tunnelName: item.tunnelName, hc: 0, hr: 0}) 
        });
      } else {
        this.statisticsList = [{tunnelName:'迎春坡隧道'}, {tunnelName:'毓秀山隧道'}, {tunnelName:'红山隧道'}, {tunnelName:'徐庄长山隧道'}, {tunnelName:'望海石隧道'}, {tunnelName:'龙门隧道'}, {tunnelName:'洪门隧道'}, {tunnelName:'姚家峪隧道'}, {tunnelName:'马公祠隧道'}, {tunnelName:'乐疃隧道'}, {tunnelName:'樵岭前隧道'}, {tunnelName:'佛羊岭隧道'}];
      }
      let arr = this.statisticsList
      chartData.forEach((item, i) => {
        arr.forEach((val, i) => {
          if(item.pathId == val.tunnelName) {
            item.type == '呼出' ? val.hc += 1 : val.hr += 1
          }
        })
      });
      // x轴
      let xData = arr.map(item => item.tunnelName);
      // 呼入
      let data1 = []
      // 呼出
      let data2 = []
      // 总数
      let all = []
      for (const k in arr) {
        data1.push(arr[k].hr)
        data2.push(arr[k].hc)
        all.push(arr[k].hr + arr[k].hc)
      }
      
      var color = "#000";
      this.chart.setOption({
        backgroundColor: "#f2f2f2",
        title: {
          // text: "紧急电话记录统计",
          // subtext: "BY Wang Dingding",
          left: "10%",

          textStyle: {
            color: color,
            fontSize: "22",
          },
          subtextStyle: {
            // color: "#90979c",
            fontSize: "16",
          },
        },
        tooltip: {
          trigger: "axis",
          backgroundColor: "#fff",
          textStyle: {
            color: color,
          },
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: color,
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            // color: "#fff",
          },
        },
        legend: {
          show: true,
          left: '10%',
          top: "10%",
          textStyle: {
            color: "#000",
          },
          data: ["呼入", "呼出"],
        },

        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: color,
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            splitLine: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: color,
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              interval: 0,
            },
            splitArea: {
              show: false,
            },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 1,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: color,
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "呼入",
            type: "bar",
            stack: "总量",
            barMaxWidth: 35,
            barGap: "10%",
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "#fff",
                  },
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: data1,
          },

          {
            name: "呼出",
            type: "bar",
            stack: "总量",
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: data2,
          },
          {
            name: "总数",
            type: "line",
            symbolSize: 10,
            symbol: "circle",
            itemStyle: {
              normal: {
                color: "rgba(252,230,48,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "top",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: all,
          },
        ],
      });
    },
  },
};
</script>
