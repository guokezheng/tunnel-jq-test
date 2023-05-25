<template>
  <div id="yanghuCharts" ref="yanghuCharts"></div>
</template>
<script>
import * as echarts from "echarts";
import { yesterdayFlow } from "@/api/bigScreen/model6";
export default {
  data() {
    return {
      duoChart1: null,
      selectModel: 1,
      selectList: [
        {
          id: 1,
          name: "PQ",
        },
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      yesterdayFlow().then((res) => {
        let maxNum = Math.max(...res.data.list);
        let arrMax = [];
        let arrMin = [];

        for (let i = 0; i < res.data.list; i++) {
          arrMax[i] = maxNum;
          arrMin[i] = 0.5;
        }
        setTimeout(()=>{
          this.openChart(res.data, arrMax, arrMin);

        },500)
        // this.$nextTick(() => {
        //   this.openChart(res.data, arrMax, arrMin);
        // });
      });
    },
    openChart(data, arrMax, arrMin) {
      this.$nextTick(function () {
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        //然后异步执行echarts的初始化函数
        newPromise.then(() => {
          let e = document.getElementById("yanghuCharts")
          if(!e){
            return
          }
          let duoChart1 = echarts.init(e);
          var option = {
            tooltip: {
              trigger: "axis",
              backgroundColor: "rgba(1, 29, 63, .8)",
              textStyle: {
                color: "#fff",
                fontSize: 12,
              },
              borderColor: "rgba(1, 29, 63,.8)",
              formatter: function (params) {
                return (
                  params[0].name +
                  "\n" +
                  "<div>" +
                  params[0].marker +
                  "<span style=''> " +
                  params[0].seriesName +
                  ": " +
                  params[0].value +
                  "\n" +
                  " </span>" +
                  "</div>" +
                  params[2].marker +
                  "<span style=''> " +
                  params[2].seriesName +
                  ": " +
                  params[2].value +
                  "%"
                );
              },
            },
            grid: {
              left: "12%",
              right: "12%",
              top: "27%",
              bottom: "28%",
            },
            xAxis: {
              data: data.xData,
              show: true,
              axisTick: {
                show: false,
              },
              axisLine: {
                show: true,
                lineStyle: {
                  type: "solid",
                  color: "#11395D",
                  opacity: 1,
                },
              },
              // lineStyle: {
              //     show: true,
              //     type: 'solid'
              // },
              axisLabel: {
                interval: 0,
                textStyle: {
                  color: "#9ba0bc",
                  fontSize: 12,
                },
                rotate: 60,
                margin: 12, // 刻度标签与轴线之间的距离。
              },
            },
            yAxis: [
              {
                // min: 0,
                // max: 150,
                name: "车次",
                nameTextStyle: {
                  align: "left",
                  color: "#9ba0bc",
                  padding: [0, 0, 0, -20],
                  fontSize: 12,
                },
                splitLine: {
                  lineStyle: {
                    color: "#11395D",
                    type: "dashed", // dotted 虚线
                  },
                },
                axisTick: {
                  show: false,
                },
                axisLine: {
                  show: false,
                  lineStyle: {
                    color: "#11395D",
                  },
                },
                axisLabel: {
                  textStyle: {
                    color: "#9ba0bc",
                    fontSize: 12,
                  },
                },
              },
              {
                show: true,
                name: "%",
                nameTextStyle: {
                  align: "left",
                  color: "#9ba0bc",
                  padding: [0, 0, 0, 10],
                  fontSize: 12,
                },
                splitLine: { show: false },
                axisLine: {
                  show: false,
                  lineStyle: {
                    color: "#123452",
                  },
                },
                axisTick: { show: false },
                axisLabel: {
                  textStyle: { fontSize: 12, color: "#9ba0bc" },
                },
              },
            ],
            legend: {
              top: "5%",
              left: "right",
              itemWidth: 18,
              itemHeight: 10,
              textStyle: {
                color: "#9ba0bc",
                fontSize: 12,
              },

              itemGap: 12, // 设置间距
            },
            series: [
              {
                // 三个最低下的圆片
                name: "",
                type: "pictorialBar",
                symbolSize: [14, 8],
                symbolOffset: [0, 2],
                z: 12,
                itemStyle: {
                  opacity: 1,
                  color: function (params) {
                    var a = params.name.slice(0, 2);
                    return new echarts.graphic.LinearGradient(
                      0,
                      0,
                      0,
                      1,
                      [
                        {
                          offset: 0,
                          color: "#12B9DB", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#007AFF", // 100% 处的颜色
                        },
                      ],
                      false
                    );
                  },
                },
                data: arrMin,
              },

              // 下半截柱状图
              {
                name: "通行车次",
                type: "bar",
                barWidth: 14,
                barGap: "-100%",
                itemStyle: {
                  // lenged文本
                  opacity: 0.7,
                  color: function (params) {
                    var a = params.name.slice(0, 2);
                    return new echarts.graphic.LinearGradient(
                      0,
                      0,
                      0,
                      1,
                      [
                        {
                          offset: 0,
                          color: "#12B9DB", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#007AFF", // 100% 处的颜色
                        },
                      ],
                      false
                    );
                  },
                },

                data: data.list,
              },

              {
                name: "",
                type: "pictorialBar",
                symbolSize: [14, 8],
                symbolOffset: [0, -4],
                z: 5,
                itemStyle: {
                  opacity: 1,
                  color: function (params) {
                    var a = params.name.slice(0, 2);

                    return new echarts.graphic.LinearGradient(
                      0,
                      0,
                      0,
                      1,
                      [
                        {
                          offset: 0,
                          color: "#12B9DB", // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: "#007AFF", // 100% 处的颜色
                        },
                      ],
                      false
                    );
                  },
                },
                symbolPosition: "end",
                data: data.list,
              },
              {
                name: "周环比增长率",
                z: 20,
                yAxisIndex: 1,
                type: "line",
                data: data.qoQList,
                //symbol: '',
                symbolSize: "9",
                lineStyle: {
                  normal: {
                    width: 3,
                    color: "#FF9B61", // 线条颜色
                  },
                  borderColor: "#FF9B61",
                },
              },
              {
                name: "", //头部
                type: "pictorialBar",
                symbolSize: [14, 8],
                symbolOffset: [0, 0],
                z: 12,
                symbolPosition: "end",
                itemStyle: {
                  color: "#1E44B8",
                  opacity: 1,
                },
                data: arrMax,
              },
              {
                name: "",
                type: "bar",
                barWidth: 14,
                barGap: "-100%",
                z: 0,
                itemStyle: {
                  color: "#163F7A",
                  opacity: 0.7,
                },
                data: arrMax,
              },
            ],
          };

          // option && myChart3.setOption(option);
          // if (option && typeof option === "object") {
          //     myChart3.setOption(option, true);
          //     refreshChart(myChart3,option);
          //     var index3 = 0; //播放所在下标
          //     var mTime = setInterval(function () {
          //             myChart3.dispatchAction({
          //                 type: 'showTip',
          //                 seriesIndex: 0,
          //                 dataIndex: index3
          //             });
          //             index3++;
          //             if (index3 >= 6) {
          //                 //console.log('data4', fxdata.length)
          //                 index3 = 0;
          //                 //console.log(fxdata)
          //             }
          //     }, 2600);
          // }
          duoChart1.setOption(option);
        });
      });
    },
  },
};
</script>
<style scoped lang="scss">
#yanghuCharts {
  height: calc(100% - 38px);
  margin-top: 4px;
  > div {
    width: 100%;
    height: 100%;
  }
}
</style>