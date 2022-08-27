<template>
  <div class="statistics-container">
    <div ref="echartsBox1" id="mainOne"></div>
    <div ref="echartsBox2" id="mainTwo"></div>
    <div ref="echartsBox3" id="mainThree"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from 'element-resize-detector'

export default {
  name: "statistics",
  props: {
    incidentVal: {
      type: Number,
    },
    earlyWarningVal: {
      type: Number,
    },
    malfunctionVal: {
      type: Number,
    },
  },
  data() {
    return {
      optionOne: null,
      optionTwo: null,
      optionThree: null,
    };
  },
  created() {},
  mounted() {
    this.initEchartsOne();
    this.initEchartsTwo()
    this.initEchartsThree()
    this.watchSize()
  },
  watch: {
    incidentVal() {
      this.initEchartsOne()
    },
    earlyWarningVal() {
      this.initEchartsTwo()
    },
    malfunctionVal() {
      this.initEchartsThree()
    }
  },
  methods: {
    watchSize() {
			let that = this;
			let erd = elementResizeDetectorMaker()
			let Dom1 = that.$refs.echartsBox1;//拿dom元素
			let Dom2 = that.$refs.echartsBox2;
			let Dom3 = that.$refs.echartsBox3;
			//监听盒子的变化
			erd.listenTo(Dom1, function (element) {
					let myChart1 = echarts.init(Dom1);
					let myChart2 = echarts.init(Dom2);
					let myChart3 = echarts.init(Dom3);
					myChart1.resize();//echarts自带的方法可以使图表重新加载
					myChart2.resize();
					myChart3.resize();
			})
    },
    initEchartsOne() {
      var myChart = echarts.init(document.getElementById("mainOne"));
      this.optionOne = {
        tooltip: {
          formatter: "{a} <br/>{b} : {c}件",
        },
        series: [
          {
            name: "状况统计",
            type: "gauge",
            startAngle: "180",
            endAngle: "-179",
            radius: "90%",
            max: "100",
            progress: {
              show: true,
              width: 7,
              itemStyle: {
                color: "#04A7D9",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                width: 7,
                color: [[1, '#02255D']],
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            pointer: {
              show: false,
            },
            title: {
              offsetCenter: [0, -6],
              color: "#fff",
              fontSize: 14,
            },
            detail: {
              valueAnimation: true,
              color: "#fff",
              fontSize: 14,
            },
            data: [
              {
                value: this.incidentVal,
                name: "事件",
              },
            ],
          },
        ],
      };
      this.optionOne && myChart.setOption(this.optionOne);
    },
    initEchartsTwo() {
      var myChart = echarts.init(document.getElementById("mainTwo"));
      this.optionTwo = {
        tooltip: {
          formatter: "{a} <br/>{b} : {c}件",
        },
        series: [
          {
            name: "状况统计",
            type: "gauge",
            startAngle: "180",
            endAngle: "-179",
            radius: "90%",
            max: "100",
            progress: {
              show: true,
              width: 7,
              itemStyle: {
                color: "#FA838B",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                width: 7,
                color: [[1, '#02255D']],
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            pointer: {
              show: false,
            },
            title: {
              offsetCenter: [0, -6],
              color: "#fff",
              fontSize: 14,
            },
            detail: {
              valueAnimation: true,
              color: "#fff",
              fontSize: 14,
            },
            data: [
              {
                value: this.earlyWarningVal,
                name: "预警",
              },
            ],
          },
        ],
      };
      myChart.setOption(this.optionTwo);
    },
    initEchartsThree() {
      var myChart = echarts.init(document.getElementById("mainThree"));
      this.optionThree = {
        tooltip: {
          formatter: "{a} <br/>{b} : {c}件",
        },
        series: [
          {
            name: "状况统计",
            type: "gauge",
            startAngle: "180",
            endAngle: "-179",
            radius: "90%",
            max: "100",
            progress: {
              show: true,
              width: 7,
              itemStyle: {
                color: "#03A2D6",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                width: 7,
                color: [[1, '#02255D']],
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            pointer: {
              show: false,
            },
            title: {
              offsetCenter: [0, -6],
              color: "#fff",
              fontSize: 14,
            },
            detail: {
              valueAnimation: true,
              color: "#fff",
              fontSize: 14,
            },
            data: [
              {
                value: this.malfunctionVal,
                name: "故障",
              },
            ],
          },
        ],
      };
      myChart.setOption(this.optionThree);
    },
  },
};
</script>

<style lang="less" scoped>
.statistics-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  #mainOne, #mainTwo, #mainThree {
    width: 33%;
    height: 100%;
  }
}
</style>
