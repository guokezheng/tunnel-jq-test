<template>
  <div
    class="theAlarmNumber-container"
    v-loading.lock="fullscreenLoading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="#040f4e"
  >
    <div class="title">
      今日{{ currentData.name }}
      <span
        style="margin-left: 0.5vw; font-size: 0.8vw; cursor: pointer"
        @click="SwitchingEvents"
        >切换</span
      >
    </div>
    <div class="box">
      <div ref="echartsBox" class="box-left" id="TheAlarmNumber"></div>
      <div class="box-center">
        <div class="progressBar">
          <div style="font-size: 0.8vw">已处置：</div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="hasDisposal"
          ></el-progress>
        </div>
        <div class="progressBarNot">
          <div style="font-size: 0.8vw">未处置：</div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="notDDisposedOf"
          ></el-progress>
        </div>
      </div>
      <div class="box-right">
        <div class="listHeader">
          <el-row type="flex" style="font-size: 0.8vw;background-color: rgba(255,255,255,0.2);">
            <el-col style="width: 20vw; padding-left: 0.4vw">隧道</el-col>
            <el-col>发生时间</el-col>
            <el-col>详细信息</el-col>
            <el-col style="width: 20vw">处理情况</el-col>
          </el-row>
        </div>
        <vue-seamless-scroll
          :class-option="defaultOption"
          class="listContent"
          :data="currentData.listData"
        >
          <el-row
            type="flex"
            v-for="(item, index) in currentData.listData"
            :key="index"
            :style="{
              backgroundColor:
                (index + 1) % 2 == 0
                  ? 'rgba(255, 255, 255,0.1)'
                  : 'rgba(255, 255, 255,0)',
            }"
          >
            <el-col style="width: 20vw; padding-left: 0.4vw">{{
              item.name
            }}</el-col>
            <el-col>{{ item.time }}</el-col>
            <el-col style="padding-right: 1vw">{{ item.content }}</el-col>
            <el-col style="width: 20vw">{{ item.type }}</el-col>
          </el-row>
        </vue-seamless-scroll>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import "echarts-liquidfill";
import elementResizeDetectorMaker from 'element-resize-detector'
export default {
  props: {},
  data() {
    return {
      fullscreenLoading: false,
      currentData: {},
      chartData: {
        name: "事件事故",
        all: 22,
        hasDisposal: 21,
        notDDisposedOf: 1,
        listData: [
          {
            id: 0,
            name: "姚家峪隧道",
            time: "2021-11-3 14:30:30",
            content: "交通拥堵导致的车祸事件，有1人轻伤",
            type: "未处置",
          },
          {
            id: 1,
            name: "毓秀山隧道",
            time: "2021-11-12 14:30:30",
            content: "行人闯红灯导致的车祸事件，有1人轻伤",
            type: "已处置",
          },
          {
            id: 2,
            name: "洪山隧道",
            time: "2021-11-23 14:30:30",
            content: "交通拥堵导致的车祸事件，无人受伤",
            type: "已处置",
          },
          {
            id: 3,
            name: "望海石隧道",
            time: "2021-11-26 14:30:30",
            content: "行人车辆闯红灯事件",
            type: "未处置",
          },
        ],
      },
      malfunctionData: {
        name: "故障信息",
        all: 40,
        hasDisposal: 38,
        notDDisposedOf: 2,
        listData: [
          {
            id: 0,
            name: "望海石隧道",
            time: "2021/11/10 12:30:25",
            content: "风机故障",
            type: "未处置",
          },
          {
            id: 1,
            name: "毓秀山隧道",
            time: "2021/11/15 12:30:25",
            content: "COVI仪器故障",
            type: "已处置",
          },
          {
            id: 3,
            name: "中庄",
            time: "2021/11/20 12:30:25",
            content: "监控故障",
            type: "未处置",
          },
        ],
      },
      warningData: {
        name: "预警信息",
        all: 66,
        hasDisposal: 60,
        notDDisposedOf: 6,
        listData: [
          {
            id: 0,
            name: "毓秀山隧道",
            time: "2021/11/13 12:30:25",
            content: "CO浓度超标",
            type: "未处置",
          },
          {
            id: 1,
            name: "姚家峪隧道",
            time: "2021/11/21 17:30:25",
            content: "用电能耗超标",
            type: "已处置",
          },
        ],
      },
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: this.currentData.listData.length, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
    hasDisposal() {
      var num =
        ((this.currentData.hasDisposal / this.currentData.all) * 100).toFixed(
          2
        ) - 0;
      return num;
    },
    notDDisposedOf() {
      var num =
        (
          (this.currentData.notDDisposedOf / this.currentData.all) *
          100
        ).toFixed(2) - 0;
      return num;
    },
  },
  created() {
    this.setData();
  },
  mounted() {
    // this.initChart();
    this.initEchart();
    this.watchSize()
  },
  methods: {
    watchSize() {
			let that = this;
			let erd = elementResizeDetectorMaker()
			let Dom = that.$refs.echartsBox;//拿dom元素
			//监听盒子的变化
			erd.listenTo(Dom, function (element) {
					let myChart = echarts.init(Dom);
					myChart.resize();//echarts自带的方法可以使图表重新加载
			})
    },
    setData(val) {
      this.fullscreenLoading = true;
      this.currentData = {};
      if (val) {
        this.currentData = { ...val };
      } else {
        this.currentData = { ...this.chartData };
      }
      setTimeout(() => {
        this.fullscreenLoading = false;
      }, 500);
    },
    initChart() {
      var object = { ...this.currentData };
      var myChart = echarts.init(document.getElementById("TheAlarmNumber"));
      var Options = {
        title: {
          text: "总数" + "\n" + "\n" + object.all,
          top: "center",
          left: "center",
          textStyle: {
            color: "#fff",
            fontSize: 16,
          },
        },
        tooltip: {
          formatter: "{a} <br/>{b} : {c}件",
        },
        series: [
          {
            name: "总" + object.name + "数",
            type: "pie",
            radius: ["50%", "70%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
            },
            data: [
              { value: object.hasDisposal, name: "已处置" },
              { value: object.notDDisposedOf, name: "未处置" },
            ],
          },
        ],
      };
      Options && myChart.setOption(Options);
    },
    initEchart() {
      var object = { ...this.currentData };
      var myChart = echarts.init(document.getElementById("TheAlarmNumber"));
      function _pie2() {
        let dataArr = [];
        for (var i = 0; i < 8; i++) {
          if (i % 2 === 0) {
            dataArr.push({
              name: (i + 1).toString(),
              value: 25,
              itemStyle: {
                normal: {
                  color: "rgba(88,142,197,0.5)",
                },
              },
            });
          } else {
            dataArr.push({
              name: (i + 1).toString(),
              value: 20,
              itemStyle: {
                normal: {
                  color: "rgba(0,0,0,0)",
                },
              },
            });
          }
        }
        return dataArr;
      }
      var value = object.hasDisposal / object.all;
      var data = [value];
      var option = {
        title: [
          {
            text: object.all + '\n' + '总数',
            // x: "50%",
            // y: "50%",
            top: 'center',
            left: 'center',
            textStyle: {
              fontSize: 20,
              fontWeight: "400",
              color: "#fff",
              lineHeight: 20,
              textAlign: "center",
            },
          },
        ],
        tooltip: {
          formatter: '{a} : '+ object.hasDisposal + '件' + '<br>' + '占比 : ' + (value * 100).toFixed(2) + '%',
        },
        series: [
          {
            type: "liquidFill",
            name: '已处置',
            radius: "57%",
            center: ["50%", "50%"],
            color: [
              {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 1,
                    color: "#6CDEFC",
                  },
                  {
                    offset: 0,
                    color: "#429BF7",
                  },
                ],
                globalCoord: false,
              },
            ],
            data: data,
            backgroundStyle: {
              borderWidth: 1,
              color: "RGBA(51, 66, 127, 0.7)",
            },
            label: {
              normal: {
                show: false,
                // formatter: (value * 100).toFixed(1) + "%",
                // textStyle: {
                //   fontSize: 28,
                //   color: "#fff",
                // },
              },
            },
            outline: {
              show: false,
              itemStyle: {
                borderWidth: 0,
              },
              borderDistance: 0,
            },
          },
          {
            type: "pie",
            zlevel: 1,
            silent: true,
            center: ["50%", "50%"],
            radius: ["65%", "64%"],
            hoverAnimation: false,
            color: "rgba(88,142,197,0.5)",
            label: {
              normal: {
                show: false,
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },

            data: [1],
          },
          {
            type: "pie",
            zlevel: 2,
            silent: true,
            center: ["50%", "50%"],
            radius: ["73%", "70%"],
            startAngle: 50,
            hoverAnimation: false,
            label: {
              normal: {
                show: false,
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
            data: _pie2(),
          },
          {
            type: "pie",
            zlevel: 3,
            silent: true,
            center: ["50%", "50%"],
            radius: ["68%", "69%"],
            label: {
              normal: {
                show: false,
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
            data: _pie2(),
          },
        ],
      };

      option && myChart.setOption(option);
    },
    SwitchingEvents() {
      var name = this.currentData.name;
      if (name == "事件事故") {
        this.setData(this.malfunctionData);
      } else if (name == "故障信息") {
        this.setData(this.warningData);
      } else if (name == "预警信息") {
        this.setData(this.chartData);
      } else {
        this.setData(this.chartData);
      }
      this.initEchart();
    },
  },
};
</script>

<style lang="less" scoped>
.theAlarmNumber-container {
  font-size: 0.8vw;
  height: 100%;
  .title {
    margin-top: 0;
    height: 18%;
    display: flex;
    align-items: flex-end;
    color: #09bdef;
  }
  .box {
    width: 100%;
    height: 82%;
    display: flex;
    .box-left {
      width: 18%;
      height: 100%;
      padding-top: 1vw;
    }
    .box-center {
      width: 20%;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding-right: 0.5vw;
      .title {
        margin-bottom: 0.5vw;
      }
      /deep/ .progressBar {
        .el-progress-bar__outer {
          background-color: #040f4e;
        }
        .el-progress-bar__inner {
          background-color: #5470c6;
        }
      }
      /deep/ .progressBarNot {
        .el-progress-bar__outer {
          background-color: #040f4e;
        }
        .el-progress-bar__inner {
          background-color: #91cc75;
        }
      }
    }
    .box-right {
      width: 60%;
      height: 100%;
      padding: 0.5vw;
      overflow: hidden;

      .listContent {
        .el-row {
          height: 100%;
          width: 100%;
          .el-col {
            display: flex;
            align-items: center;
          }
        }
      }
    }
  }
}
</style>
