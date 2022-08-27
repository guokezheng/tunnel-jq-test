<template>
  <div class="theAlarmNumber-container">
    <div class="title">总事件报警</div>
    <div class="box">
      <div class="box-left" id="TheAlarmNumber"></div>
      <div class="box-center">
        <div class="progressBar">
          <div style="font-size:0.8vw;">已处置：</div>
          <el-progress :text-inside="true" :stroke-width="16" :percentage="hasDisposal"></el-progress>
        </div>
        <div class="progressBarNot">
          <div style="font-size:0.8vw;">未处置：</div>
          <el-progress :text-inside="true" :stroke-width="16" :percentage="notDDisposedOf"></el-progress>
        </div>
      </div>
      <div class="box-right">
        <div class="listHeader">
          <el-row type="flex" style="font-size:0.8vw;">
            <el-col style="width:20vw;padding-left:0.4vw;">隧道</el-col>
            <el-col>发生时间</el-col>
            <el-col>详细信息</el-col>
            <el-col style="width:20vw;">处理情况</el-col>
          </el-row>
        </div>
        <vue-seamless-scroll :class-option="defaultOption" class="listContent" :data="listData">
          <el-row
            type="flex"
            v-for="(item, index) in listData"
            :key="index"
            :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}"
          >
            <el-col style="width:20vw;padding-left:0.4vw;">{{item.name}}</el-col>
            <el-col>{{item.time}}</el-col>
            <el-col style="padding-right:1vw;">{{item.content}}</el-col>
            <el-col style="width:20vw;">{{item.type}}</el-col>
          </el-row>
        </vue-seamless-scroll>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  props: {},
  data() {
    return {
      chartData: {
        all: 132,
        hasDisposal: 112,
        notDDisposedOf: 20
      },
      listData: [
        {
          id: 0,
          name: "姚家峪隧道",
          time: "2021-11-3 14:30:30",
          content: "交通拥堵导致的车祸事件，有1人轻伤",
          type: "未处置"
        },
        {
          id: 1,
          name: "毓秀山隧道",
          time: "2021-11-12 14:30:30",
          content: "行人闯红灯导致的车祸事件，有1人轻伤",
          type: "已处置"
        },
        {
          id: 2,
          name: "洪山隧道",
          time: "2021-11-23 14:30:30",
          content: "交通拥堵导致的车祸事件，无人受伤",
          type: "已处置"
        },
        {
          id: 3,
          name: "望海石隧道",
          time: "2021-11-26 14:30:30",
          content: "行人车辆闯红灯事件",
          type: "未处置"
        }
      ]
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: this.listData.length, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      };
    },
    hasDisposal() {
      var num = (
        (this.chartData.hasDisposal / this.chartData.all) *
        100
      ).toFixed(2) - 0;
      return num;
    },
    notDDisposedOf() {
      var num = (
        (this.chartData.notDDisposedOf / this.chartData.all) *
        100
      ).toFixed(2) - 0;
      return num;
    }
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      var total = this.chartData.all;
      var myChart = echarts.init(document.getElementById("TheAlarmNumber"));
      var Options = {
        title: {
          text: '总数'+'\n'+'\n'+total,
          top: 'center',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 15,
          },
        },
        tooltip: {
          formatter: "{a} <br/>{b} : {c}件",
        },
        series: [
          {
            name: "总事件报警数",
            type: "pie",
            radius: ["50%", "70%"],
            avoidLabelOverlap: false,
            label: {
              show: false
            },
            data: [
              { value: this.chartData.hasDisposal, name: "已处置" },
              { value: this.chartData.notDDisposedOf, name: "未处置" }
            ]
          }
        ]
      };
      Options && myChart.setOption(Options);
    }
  }
};
</script>

<style lang="less" scoped>
.theAlarmNumber-container {
  font-size: 0.8vw;
  height: 100%;
  .title {
    color: #09bdef;
    height: 15%;
    font-size: 1vw;
    padding-top: 0.7vw;
    padding-left: 2.5vw;
  }
  .box {
    width: 100%;
    height: 85%;
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
