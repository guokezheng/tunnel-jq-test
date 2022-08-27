<template>
  <div
    :class="className"
    :style="{ height: height, width: width, }"
  />
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
    this.$nextTick(() => {
      this.initChart();
    });
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
          this.tunnelData.forEach((item, index) => {
            this.statisticsList.push({
              tunnelName: item.tunnelName,
              hc: 0,
              hr: 0,
            });
          });
        } else {
          this.statisticsList = [
            "迎春坡隧道",
            "毓秀山隧道",
            "红山隧道",
            "徐庄长山隧道",
            "望海石隧道",
            "龙门隧道",
            "洪门隧道",
            "姚家峪隧道",
            "马公祠隧道",
            "乐疃隧道",
            "樵岭前隧道",
            "佛羊岭隧道",
          ];
        }
      // 呼入
      var hr = null
      // 呼出
      var hc = null
      this.chartData.forEach(item => {
        if(item.type == '呼出') {
          hc ? hc += 1 : hc = 1
        } else {
          hr ? hr += 1 : hr = 1
        }
      })
      // 外圈
      var outerRing = this.statisticsList.map(item => {
        let value = 0
        chartData.forEach(val => {
          let a = 0
          val.pathId == item.tunnelName && (a+=1)
          value = a
        })
        if(!value) return
        return { value: value, name: item.tunnelName, label:{color: color,fontSize: 15}}
      })

      var color = '#000'
      this.chart.setOption({
        tooltip: {
          trigger: "item",
          confine: true,
          formatter: "{a} <br/>{b}: {c} <br/>占比: ({d}%)",
          backgroundColor: "#fff",
          textStyle: {
            color: color,
          },
        },
        series: [
          {
            name: "统计分析",
            type: "pie",
            selectedMode: "single",
            radius: [0, "50%"],
            label: {
              position: "inner",
              fontSize: 14
            },
            labelLine: {
              show: false
            },
            data: [
              {
                value: hr,
                name: "呼入",
                itemStyle: {
                  color: "#55FF00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: color }
              },
              {
                value: hc,
                name: "呼出",
                itemStyle: {
                  color: "#FFFF00",
                  borderColor: "#fff",
                  borderWidth: 2
                },
                label: { color: color }
              }
            ]
          },
          {
            name: "统计分析",
            type: "pie",
            radius: ["65%", "80%"],
            labelLine: {
              // length: 100
            },
            label: {
              alignTo: "edge",
              formatter: "{name|{b}}\n{c}\n{d}%",
              minMargin: 5,
              lineHeight: 15,
              edgeDistance: 10,
              rich: {
                time: {
                  fontSize: 10,
                  color: "#fff"
                }
              }
            },
            data: outerRing
          }
        ]
      });
    },
  },
};
</script>
