<template>
  <div
    style="
      width: 100%;
      height: calc(100% - 38px);
      padding-top: 8px;
      position: relative;
    "
  >
    <div class="top">
      <div class="left">
        <div><span style="background: #61afe0"></span>设备数</div>
        <div><span style="background: #188ac3"></span>故障数</div>
        <div><span style="background: #86cc97"></span>故障率</div>
      </div>
      <div>单位:MWh</div>
    </div>
    <div id="chart6" style="width: 100%; height: 100%;" ref="brandFaultChart"></div>
  </div>
</template>
    <script>
import * as echarts from "echarts";
import { monthFault } from "@/api/bigScreen/model2";
export default {
  data() {
    return {
      brandFaultChart: null,
      monthStrings: [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月",
      ],
      monthArr:[]
    };
  },
  created() {
    this.monthFormat()
    this.getList()
  },
  mounted() {
    this.$nextTick(() => {
      this.openChart();
    });
  },
  methods: {
    getList(){
      monthFault().then((res)=>{
        let devices = res.data.devices;
        let faults = res.data.faults;
        let percent = res.data.percent;

        this.openChart(devices,faults,percent)
        // for(let item of res.data.percent){
        //   item.percent = Number(item.percent) * 100
        // }
      })
    },
    monthFormat() {
      //  到当前时间数组
      let month = new Date().getMonth() + 1;
      let laneArray = JSON.parse(JSON.stringify(this.monthStrings));
      this.monthArr = laneArray.slice(0, month);
    },
    openChart(devices,faults,percent) {
      this.$nextTick(function () {
        if (
          this.brandFaultChart != null &&
          this.brandFaultChart != "" &&
          this.brandFaultChart != undefined
        ) {
          // 销毁
          this.brandFaultChart.dispose();
        }
        let e = this.$refs.brandFaultChart;
        if(!e){
          return
        }
        this.brandFaultChart = echarts.init(e);

        // let chartData = {
        //   bingValue: [200, 160, 120, 110, 100, 110, 90, 80, 70, 60],
        //   ziValue: [30, 26, 38, 25, 30, 27, 30, 26, 38, 25, 30, 27],
        //   guangValue: [45, 33, 43, 31, 39, 34, 45, 33, 43, 31, 39, 34],
        // };
        let xData = this.monthArr;
        let option = {
          color: ["#0C65F6", "#00FFA6", "#F4DF58"],
          tooltip: {
            trigger: "axis",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 10,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "shadow",
              shadowStyle: {
                fontSize: 10,
                color: "rgba(0, 11, 34, 0)",
              },
            },
          },
          dataZoom: [
            {
              moveOnMouseMove: true,
              type: "slider",
              show: false,
              xAxisIndex: [0],
              startValue: 0, // 数据窗口范围的起始数值
              endValue: 6, // 数据窗口范围的结束数值（一页显示多少条数据）
              minValueSpan: 5,
              maxValueSpan: 5,
            },
            {
              type: "inside",
              xAxisIndex: 0,
              zoomOnMouseWheel: false, //滚轮是否触发缩放
              moveOnMouseMove: true, //鼠标滚轮触发滚动
              moveOnMouseWheel: true,
            },
          ],
          grid: {
            left: "5%",
            right: "8%",
            bottom: "10%",
            top: "24%",
            containLabel: true,
          },
          // legend: {
          //   icon: "circle",
          //   orient: "horizontal",
          //   left: "left",
          //   top: 1,
          //   left: 2,
          //   itemWidth: 7,
          //   itemHeight: 7,
          //   textStyle: {
          //     fontSize: 12,
          //     color: "rgba(255, 255, 255, 1)",
          //     height: 8,
          //   },
          //   data: ["光伏", "风电", "发电趋势"],
          // },
          xAxis: {
            type: "category",
            data: xData,
            axisLine: {
              lineStyle: {
                color: "#11395D",
              },
            },
            axisLabel: {
              fontSize: 12,
              color: "#9ba0bc",
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: [
            {
              name: "数量",
              nameTextStyle: {
                color: "#9ba0bc",
              },
              type: "value",
              min: 0,
              minInterval: 1,
              yAxisIndex: 0,
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
              name: "故障率",
              nameTextStyle: {
                color: "#9ba0bc",
              },
              type: "value",
              min: 0,
              minInterval: 1,
              yAxisIndex: 2,
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
            },
          ],

          series: [
            {
              type: "bar",
              barWidth: 6,
              itemStyle: {
                barBorderRadius: [15, 15, 0, 0],
                color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                  {
                    offset: 1,
                    color: "rgba(23, 159, 228, 1)",
                  },
                  {
                    offset: 0,
                    color: "rgba(23, 159, 228,0.2)",
                  },
                ]),
              },
              name: "设备数",
              data: devices,
              yAxisIndex: 0,
            },
            {
              type: "bar",
              barWidth: 6,
              barGap: 1,
              itemStyle: {
                barBorderRadius: [15, 15, 0, 0],
                color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                  {
                    offset: 1,
                    color: "rgba(17,244,242,1)",
                  },
                  {
                    offset: 0,
                    color: "rgba(17,244,242, 0.2)",
                  },
                ]),
              },
              name: "故障数",
              data: faults,
              yAxisIndex: 0,
            },
            {
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
              name: "故障率",
              data: percent,
              yAxisIndex: 1,
            },
          ],
        };
        if (xData.length > 6) {
          let that = this;
          setInterval(function () {
            // 每次向后滚动一个，最后一个从头开始。
            if (option.dataZoom[0].endValue == devices.length) {
              option.dataZoom[0].endValue = 6;
              option.dataZoom[0].startValue = 0;
            } else {
              option.dataZoom[0].endValue = option.dataZoom[0].endValue + 1;
              option.dataZoom[0].startValue = option.dataZoom[0].startValue + 1;
            }
            that.brandFaultChart.setOption(option);
          }, 4000);
        }
        this.brandFaultChart.setOption(option);
        window.addEventListener("resize", function () {
          this.brandFaultChart.resize();
        });
      });
    },
  },
};
</script>
    <style scoped lang="less">
/* @import url(); 引入css类 */
.top {
  width: 100%;
  height: 22px;
  background-image: linear-gradient(
    to right,
    rgba(3, 71, 130, 1),
    rgba(3, 71, 130, 0)
  );
  display: flex;
  //justify-content: space-between;
  justify-content: space-between;
  position: absolute;
  //top: 2px;
  left: 0;
  align-items: center;
  // color: #c5d0e0;
  font-size: 0.7vw;
  padding-right: 5px;
  box-sizing: border-box;
  > div {
    color: #9ba0bc;
  }
  .left {
    display: flex;
    color: #c5d0e0 !important;
    > div:last-of-type {
      span {
        display: inline-block;
        width: 10px;
        height: 2px;
        border-radius: 0;
        margin-left: 10px;
        margin-right: 5px;
        margin-bottom: 3px;
      }
    }
  }
  span {
    display: inline-block;
    width: 7px;
    height: 7px;
    border-radius: 50%;
    margin-left: 10px;
    margin-right: 5px;
  }
}
</style>
  