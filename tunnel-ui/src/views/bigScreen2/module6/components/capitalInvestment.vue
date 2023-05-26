<template>
  <div class="box">
    <div class="row">
      <div class="chartsBox">
        <div class="row1">PCI值</div>
        <div style="height: calc(100% - 36px)">
          <div id="miniCharts1" ref="miniCharts1"></div>
        </div>
        <div class="row3">
          <div>当前值</div>
          <div>94</div>
        </div>
      </div>
      <div class="chartsBox">
        <div class="row1">
          <div>交通流量</div>
          <div>当前小时流量</div>
        </div>
        <div style="height: calc(100% - 54px)">
          <div id="miniCharts2"></div>
        </div>
        <div class="row1">
          <div>南线</div>
          <div>1000辆</div>
        </div>
        <div class="row4">
          <div>北线</div>
          <div>2000辆</div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="chartsBox">
        <div class="row1">
          <div>CO值</div>
          <div>允许值250PPM</div>
        </div>
        <div style="height: calc(100% - 36px)">
          <div id="miniCharts3"></div>
        </div>
        <div class="row3">
          <div>当前值</div>
          <div>160PPM</div>
        </div>
      </div>
      <div class="chartsBox">
        <div class="row1">
          <div>VI能见度</div>
          <div>标准值 1000米</div>
        </div>
        <div style="height: calc(100% - 36px)">
          <div id="miniCharts4"></div>
        </div>
        <div class="row3">
          <div>当前值</div>
          <div>500米</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import * as echarts from "echarts";
export default {
  data() {
    return {
      miniCharts1: null,
      miniCharts2: null,
      miniCharts3: null,
      miniCharts4: null,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.openChart1();
      this.openChart2();
      this.openChart3();
      this.openChart4();
    });
  },
  methods: {
    openChart1() {
      this.$nextTick(function () {
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        //然后异步执行echarts的初始化函数
        newPromise.then(() => {
          let e = document.getElementById("miniCharts1")
          if(!e){
            return 
          }
          //	此dom为echarts图标展示dom
          let miniCharts1 = echarts.init(e);
          let xaxisData = ["艾洪蕾", "李明涛", "赵薇", "化工"];
          let yaxisData = [94, 94, 94, 94];

          var option = {
            tooltip: {
              trigger: "axis",
              backgroundColor: "rgba(1, 29, 63, .8)",
              textStyle: {
                color: "#fff",
                fontSize: 12,
              },
              borderColor: "rgba(1, 29, 63,.8)",
              formatter: (params) => {
                const item = params[0];

                return (
                  params[0].marker + "" + item.name + " : " + item.value + " 条"
                );
              },
              //   提示框超出范围时调整位置
              position: function (point, params, dom, rect, size) {
                // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
                // 提示框位置
                let x = 0; // x坐标位置
                let y = 0; // y坐标位置

                // 当前鼠标位置
                let pointX = point[0];
                let pointY = point[1];

                // 外层div大小
                // var viewWidth = size.viewSize[0];
                // var viewHeight = size.viewSize[1];

                // 提示框大小
                let boxWidth = size.contentSize[0];
                let boxHeight = size.contentSize[1];

                // boxWidth > pointX 说明鼠标左边放不下提示框
                if (boxWidth > pointX) {
                  x = 5;
                } else {
                  // 左边放的下
                  x = pointX - boxWidth;
                }

                // boxHeight > pointY 说明鼠标上边放不下提示框
                if (boxHeight > pointY) {
                  y = 5;
                } else {
                  // 上边放得下
                  y = pointY - boxHeight;
                }

                return [x, y];
              },
            },
            grid: {
              left: "1%",
              right: "2%",
              top: "26%",
              bottom: "2%",
              containLabel: true,
            },
            xAxis: [
              {
                type: "category",
                axisLabel: {
                  show: false,
                  interval: 0,
                  color: "#fff",
                  fontSize: 12,
                },
                axisLine: {
                  show: false,
                  lineStyle: {
                    //y轴网格线设置
                    color: "rgba(0, 168, 255, 0.4)",
                    width: 1,
                  },
                },
                axisTick: {
                  show: false,
                },
                data: xaxisData,
              },
            ],
            yAxis: [
              {
                type: "value",
                name: "",
                nameTextStyle: {
                  show: false,
                  color: "#fff",
                  fontWeight: 400,
                  fontSize: 14,
                },
                axisTick: {
                  show: false,
                },
                axisLine: {
                  show: false,
                  lineStyle: {
                    color: "rgba(0, 168, 255, 0.3)",
                    width: 1,
                  },
                },
                splitLine: {
                  show: false,
                },
                axisLabel: {
                  show: false,
                  fontSize: 12,
                  color: "#fff",
                },
              },
            ],
            series: [
              {
                type: "bar",
                barWidth: 16,
                label: {
                  show: true,
                  position: "top",
                  color: "#FF9E02",
                },
                itemStyle: {
                  borderRadius: [8, 8, 0, 0],
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#FF9E02",
                    },
                    {
                      offset: 1,
                      color: "rgba(255,158,2, 0)",
                    },
                  ]),
                },
                data: yaxisData,
              },
            ],
          };
          miniCharts1.setOption(option);
          window.addEventListener("resize", function () {
            miniCharts1.resize();
          });
        });
      });
    },
    openChart2() {
      this.$nextTick(function () {
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        //然后异步执行echarts的初始化函数
        newPromise.then(() => {
          let e = document.getElementById("miniCharts2")
          if(!e){
            return 
          }
          //	此dom为echarts图标展示dom
          let miniCharts2 = echarts.init(e);
         
          var option = {
            title: {
              show: false,
            },
            tooltip: {
              trigger: "axis",
              backgroundColor: "rgba(1, 29, 63, .8)",
              textStyle: {
                color: "#fff",
                fontSize: 12,
              },
              borderColor: "rgba(1, 29, 63,.8)",
            },
            legend: {
              show: false,
              type: "plain",
              top: 0,
              right: 20,
              itemGap: 64,
              itemWidth: 10,
              icon: "circle",
              selectedMode: false,
              textStyle: { padding: [0, 0, 0, 4] },
              data: ["新增登记", "完成服务"],
            },
            grid: {
              left: "5%",
              right: "2%",
              top: "22%",
              bottom: "6%",
              containLabel: true,
            },
            xAxis: {
              type: "category",
              data: [
                "12-26",
                "12-27",
                "12-28",
                "12-29",
                "12-30",
                "12-31",
                "01-01",
                "01-02",
                "01-03",
                "01-04",
                "01-05",
                "01-06",
                "01-07",
                "01-08",
                "01-09",
              ],
              axisLine: { show: false },
              axisTick: { show: false },
              axisLabel: { show: false },
            },
            yAxis: {
              type: "value",
              axisLine: { show: false },
              axisLabel: { show: false },
              splitLine: { show: false },
            },
            series: [
              {
                name: "南线",
                type: "line",
                data: [0, 10, 2, 4, 4, 7, 0, 0, 0, 3, 0, 9, 6, 0, 0],
                smooth: true, // 平滑曲线
                showSymbol: false,
                itemStyle: { color: "#126EFC" },
                lineStyle: { width: 2, color: "#126EFC" },
                areaStyle: { color: "rgba(0, 110, 254, 0.1)" },
              },
              {
                name: "北线",
                type: "line",
                data: [0, 0, 0, 5, 0, 2, 0, 0, 0, 2, 0, 4, 1, 0, 0],
                smooth: true,
                showSymbol: false,
                itemStyle: { color: "#1BB389" },
                lineStyle: { width: 3, color: "#1BB389" },
                areaStyle: { color: "rgba(27, 179, 137, 0.1)" },
              },
            ],
          };
          miniCharts2.setOption(option);
          window.addEventListener("resize", function () {
            miniCharts2.resize();
          });
        });
      });
    },
    openChart3() {
      this.$nextTick(function () {
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        //然后异步执行echarts的初始化函数
        newPromise.then(() => {
          let e = document.getElementById("miniCharts3")
          if(!e){
            return 
          }
          //	此dom为echarts图标展示dom
          let miniCharts3 = echarts.init(e);
         
          var option = {
            title: {
              show: false,
            },
            tooltip: {
              trigger: "axis",
              backgroundColor: "rgba(1, 29, 63, .8)",
              textStyle: {
                color: "#fff",
                fontSize: 12,
              },
              borderColor: "rgba(1, 29, 63,.8)",
              formatter: (params) => {
                const item = params[0];

                return (
                  params[0].marker + "" + item.name + " : " + item.value + " 条"
                );
              },
              //   提示框超出范围时调整位置
              position: function (point, params, dom, rect, size) {
                // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
                // 提示框位置
                let x = 0; // x坐标位置
                let y = 0; // y坐标位置

                // 当前鼠标位置
                let pointX = point[0];
                let pointY = point[1];

                // 外层div大小
                // var viewWidth = size.viewSize[0];
                // var viewHeight = size.viewSize[1];

                // 提示框大小
                let boxWidth = size.contentSize[0];
                let boxHeight = size.contentSize[1];

                // boxWidth > pointX 说明鼠标左边放不下提示框
                if (boxWidth > pointX) {
                  x = 5;
                } else {
                  // 左边放的下
                  x = pointX - boxWidth;
                }

                // boxHeight > pointY 说明鼠标上边放不下提示框
                if (boxHeight > pointY) {
                  y = 5;
                } else {
                  // 上边放得下
                  y = pointY - boxHeight;
                }

                return [x, y];
              },
            },
            legend: {
              show: false,
              type: "plain",
              top: 0,
              right: 20,
              itemGap: 64,
              itemWidth: 10,
              icon: "circle",
              selectedMode: false,
              textStyle: { padding: [0, 0, 0, 4] },
              data: ["新增登记", "完成服务"],
            },
            grid: {
              left: "5%",
              right: "2%",
              top: "22%",
              bottom: "6%",
              containLabel: true,
            },
            xAxis: {
              type: "category",
              data: [
                "12-26",
                "12-27",
                "12-28",
                "12-29",
                "12-30",
                "12-31",
                "01-01",
                "01-02",
                "01-03",
                "01-04",
                "01-05",
                "01-06",
                "01-07",
                "01-08",
                "01-09",
              ],
              axisLine: { show: false },
              axisTick: { show: false },
              axisLabel: { show: false },
            },
            yAxis: {
              type: "value",
              axisLine: { show: false },
              axisLabel: { show: false },
              splitLine: { show: false },
            },
            series: [
              {
                type: "line",
                data: [0, 10, 2, 4, 4, 7, 0, 0, 0, 3, 0, 9, 6, 0, 0],
                smooth: true, // 平滑曲线
                showSymbol: false,
                itemStyle: { color: "#126EFC" },
                lineStyle: { width: 2, color: "#126EFC" },
                areaStyle: { color: "rgba(0, 110, 254, 0.1)" },
              },
            ],
          };
          miniCharts3.setOption(option);
          window.addEventListener("resize", function () {
            miniCharts3.resize();
          });
        });
      });
    },
    openChart4() {
      this.$nextTick(function () {
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        //然后异步执行echarts的初始化函数
        newPromise.then(() => {
          let e = document.getElementById("miniCharts4")
          if(!e){
            return 
          }
          //	此dom为echarts图标展示dom
          let miniCharts4 = echarts.init(e);
          var option = {
            title: {
              show: false,
            },
            tooltip: {
              trigger: "axis",
              backgroundColor: "rgba(1, 29, 63, .8)",
              textStyle: {
                color: "#fff",
                fontSize: 12,
              },
              borderColor: "rgba(1, 29, 63,.8)",
              formatter: (params) => {
                const item = params[0];

                return (
                  params[0].marker + "" + item.name + " : " + item.value + " 条"
                );
              },
            },
            legend: {
              show: false,
              type: "plain",
              top: 0,
              right: 20,
              itemGap: 64,
              itemWidth: 10,
              icon: "circle",
              selectedMode: false,
              textStyle: { padding: [0, 0, 0, 4] },
              data: ["新增登记", "完成服务"],
            },
            grid: {
              left: "5%",
              right: "2%",
              top: "22%",
              bottom: "6%",
              containLabel: true,
            },
            xAxis: {
              type: "category",
              data: [
                "12-26",
                "12-27",
                "12-28",
                "12-29",
                "12-30",
                "12-31",
                "01-01",
                "01-02",
                "01-03",
                "01-04",
                "01-05",
                "01-06",
                "01-07",
                "01-08",
                "01-09",
              ],
              axisLine: { show: false },
              axisTick: { show: false },
              axisLabel: { show: false },
            },
            yAxis: {
              type: "value",
              axisLine: { show: false },
              axisLabel: { show: false },
              splitLine: { show: false },
            },
            series: [
              {
                name: "完成服务",
                type: "line",
                data: [0, 0, 0, 5, 0, 2, 0, 0, 0, 2, 0, 4, 1, 0, 0],
                smooth: true,
                showSymbol: false,
                itemStyle: { color: "#1BB389" },
                lineStyle: { width: 3, color: "#1BB389" },
                areaStyle: { color: "rgba(27, 179, 137, 0.1)" },
              },
            ],
          };
          miniCharts4.setOption(option);
          window.addEventListener("resize", function () {
            miniCharts4.resize();
          });
        });
      });
    },
  },
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  // border:solid 1px red;
  .row {
    width: 95%;
    height: 45%;
    // border: solid 1px yellow;
    margin: 8px;
    display: flex;
    justify-content: space-between;
    .chartsBox {
      width: 48%;
      height: 100%;
      background: rgba($color: #1c568a, $alpha: 0.3);
      font-size: 10px;
      .row1 {
        width: 100%;
        height: 18px;
        color: #72b8f6;
        display: flex;
        justify-content: space-between;
      }
      .row3 {
        width: 100%;
        height: 18px;
        display: flex;
        justify-content: space-between;
        > div:first-of-type {
          color: #72b8f6;
        }
        > div:last-of-type {
          color: #ff9e02;
        }
      }
      .row4 {
        width: 100%;
        height: 18px;
        color: #ff9e02;
        display: flex;
        justify-content: space-between;
      }
    }
  }
}
#miniCharts1,
#miniCharts2,
#miniCharts3,
#miniCharts4 {
  height: 100%;
  > div {
    height: 100%;
  }
}
</style>