<template>
  <div class="box">
    <div style="height: 30px">
      <el-select v-model="selectModel" size="small" class="bigScreenSelect">
        <el-option
          v-for="item in selectList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
    <div id="charts6"></div>
  </div>
</template>
<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      myChart2: null,
      selectModel: 1,
      selectList: [
        {
          id: 1,
          name: "全部隧道",
        },
      ],
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
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
            data: ["缺陷量", "维修量", "平均维修时长"],
            textStyle: {
              color: "#9ba0bc",
              fontSize: 12,
            },
            itemStyle: {},
            itemHeight: 9,
            itemWidth: 20,
            top: "top",
            left: "center",
            padding: [15, 15, 0, 15],
            // icon: "circle",
            orient: "horizontal",
          },
          grid: {
            containLabel: true,
            left: 15,
            right: 20,
            bottom: 15,
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
            nameTextStyle: {
              color: "#667892",
              fontSize: 10,
            },
            axisLabel: {
              show: true,
              color: "#9ba0bc",
              fontSize: 12,
              rotate: 60,
              fontFamily: "Microsoft YaHei",
            },
            axisTick: {
              show: true,
              lineStyle: {
                color: "#dadada",
                width: 1,
              },
            },
            axisLine: {
              lineStyle: {
                color: "rgba(255,255,255,1)",
              },
              show: false,
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: "rgba(255,255,255,0.12)",
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
          yAxis: [{
            name: "条",
            type: "value",
            yAxisIndex:0,
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
          {
            name:'天',
            type: "value",
            min: 0,
            minInterval: 1,
            yAxisIndex:2,
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
              show: false,
              
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
              fontFamily: "Bebas",
            },
          }
        ],
          series: [
            {
              name: "缺陷量",
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
              yAxisIndex:0,
            },
            {
              name: "维修量",
              data: [120, 30, 150, 80, 70, 110, 130, 70, 50, 30],
              type: "bar",
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
              yAxisIndex:0,
            },
            {
              name: "平均维修时长",
              data: [1, 2, 1, 3, 2, 1, 1, 2, 1, 1],
              yAxisIndex:1,
              type: "line",
              smooth: false, // 平滑曲线显示
              symbol: "circle", // 标记的图形为实心圆
              symbolSize: 6, // 标记的大小
              showSymbol: true,
              lineStyle: {
                color: "rgba(134, 206, 118, 1)",
                width: 1,
              },
              emphasis: {
                //悬停
                itemStyle: {
                  borderWidth: 10,
                },
              },
              itemStyle: {
                borderWidth: 0,
                borderColor: "rgba(134, 206, 118,.5)",
                color: "rgba(134, 206, 118, 1)",
              },
            },
            
          ],
        };

        this.myChart2.setOption(option);
        window.addEventListener("resize", function () {
          this.myChart2.resize();
        });
      });
    },
  },
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  .bigScreenSelect {
    float: right;
    padding-top: 4px;
    ::v-deep .el-input--small .el-input__inner {
      height: 28px !important;
      line-height: 28px !important;
      background-color: rgba(0,0,0,0.5) !important;
      border: solid 1px #0c4b72 !important;
      color: #9ba0bc !important;
    }
  }
  #charts6 {
    width: 100%;
    height: calc(100% - 30px);
    div {
      width: 100% !important;
    }
  }
}
</style>