<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>隧道平均速度</div>
      <div>acerage tunnel speed</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <div id="averageVelocity"></div>
  </div>
</template>
  <script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      src: require("@/assets/cloudControl/dialogHeader.png"),
      iconSrc: require("@/assets/icons/energyIcon.png"),
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  methods: {
    initChart(){
      var averageVelocity = echarts.init(document.getElementById("averageVelocity"));
      var safetyArr = [ 50, 50, 60, 80, 80 ];
      var safetyArr2 = [ 50, 60, 70, 80, 70 ];

      var option = {
        // backgroundColor: '#00043A',
        tooltip: {
          trigger: "axis",
          show: true,
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: "{b}<br>平均速度： {c} km/h",
        },
        legend: {
          show: false,
        },
        grid: {
          left: "10%",
          right: "10%",
          bottom: "10%",
          top: "24%",
          containLabel: true,
        },
        xAxis: [
          {
            // name:'日',
            type: "category",
            // boundaryGap : false,
            axisLabel: {
              textStyle: {
                color: "#fff",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#54C1FF",
              },
            },

            axisTick: {
              show: false,
            },
            // splitLine:{
            //   show:true,
            //   lineStyle:{
            //     color:'#195384'
            //   }
            // },
            data: [1,2,3,4,5],
          },
          
        ],
        yAxis: {
          name: "kw-h",
          nameTextStyle: {
            color: "#fff",
            padding: [10, 20, 0, -40],
          },
          splitLine: {
            show: false,
          },
          axisLabel: {
            formatter: "{value}",
            textStyle: {
              color: "#fff",
              fontSize: 10,
            },
          },
          axisLine: {
            show: false,
            // lineStyle:{
            //   color:'#0a88bd'
            // }
          },
           splitLine:{
              show:true,
              lineStyle:{
                color:'#122A4E',
                type:"dashed",

              }
            },
          // offset:20,
        },
        series: [
          {
            name:'乐瞳隧道',
            type: "bar",
            barWidth: 12, //柱图宽度
            itemStyle: {
              normal: {
                // barBorderRadius: [6, 6, 0, 0],
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#00AEBB", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#0061BA", // 100% 处的颜色
                    },
                  ],
                  globalCoord: true, // 缺省为 false
                },
              },
            },
            data: safetyArr,
          },
          {
            name:'凤凰山隧道',
            type: "bar",
            barWidth: 12, //柱图宽度
            itemStyle: {
              normal: {
                // barBorderRadius: [6, 6, 0, 0],
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#B38933", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#B34517", // 100% 处的颜色
                    },
                  ],
                  globalCoord: true, // 缺省为 false
                },
              },
            },
            data: safetyArr2,
          },
        ],
      };
      averageVelocity.setOption(option);
      window.addEventListener("resize", function () {
        averageVelocity.resize();
      });
    }
  },
};
</script>
  <style scoped lang="scss">
.mapCharts {
  height: 25%;
  width: 100%;
  transform: translateX(-18px);
  position: relative;
  .title {
    color: white;
    width: 100%;
    height: 30px;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
    align-items: center;

    > div:nth-of-type(1) {
      margin-left: 10px;
      font-size: 14px;
    }
    > div:nth-of-type(2) {
      text-transform: uppercase;
      margin-left: 10px;
      opacity: 0.35;
      font-size: 10px;
    }
    > img {
      height: 22px;
      margin-left: 8px;
    }
  }
  .titleIcon {
    position: absolute;
    top: 0;
    right: 0;
    height: 30px;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
  #averageVelocity{
    width: 100%;
    height: calc(100% - 30px);
    > div {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
  