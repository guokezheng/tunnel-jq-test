<template>
  <div class="tabBox">
    <div class="lenged" >
      <div>
        <div>
          <div class="green"></div>
          光伏
        </div>
        <div>
          <div class="yellow"></div>
          市电
        </div>
        <div>
          <div class="blue"></div>
          储能
        </div>
      </div>
      <div>单位：Kw</div>
    </div>
    <div id="zong" ></div>
    
  </div>
</template>
<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      myChart1: null,
    };
  },
  created() {},
  mounted() {
    this.initEchats1();

  },
  methods: {
    initEchats1() {
      this.$nextTick(function () {
        if (
          this.myChart1 != null &&
          this.myChart1 != "" &&
          this.myChart1 != undefined
        ) {
          // 销毁
          this.myChart1.dispose();
        }
        this.myChart1 = echarts.init(document.getElementById("zong"));

        const chartData = {
          day7: [
            2.0, 2.4, 2.9, 3.8, 4.0, 4.8, 5.0, 4.3, 4.5, 3.8, 3.9, 3.4, 3.8,
            3.6, 3.7, 3.3, 3.5, 3.0, 3.2, 2.8, 3.1, 2.7, 3.2, 2.6,
          ],
          today: [
            0, 0, 0, 0.5, 0.2, 0.3, 0.3, 0.3, 0.2, 0.5, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
          ],
          lastyear: [
            7, 9, 11, 12, 17, 18, 26, 33, 23, 21, 19, 14, 15, 13, 13, 11, 12,
            14, 15, 13, 13, 11, 13, 14,
          ],
        };
        const option = {
          color: [
            "rgba(84, 181, 157, 1)",
            "rgba(31, 149, 215, 1)",
            "rgba(239, 175, 76, 1)",
            "rgba(55, 231, 255, 1)",
          ],
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
          grid: {
            left: "3%",
            right: "3%",
            bottom: "4%",
            top: "12%",
            containLabel: true,
          },

          xAxis: {
            type: "category",
            data: [
              "00:00",
              "01:00",
              "02:00",
              "03:00",
              "04:00",
              "05:00",
              "06:00",
              "07:00",
              "08:00",
              "09:00",
              "10:00",
              "11:00",
              "12:00",
              "13:00",
              "14:00",
              "15:00",
              "16:00",
              "17:00",
              "18:00",
              "19:00",
              "20:00",
              "21:00",
              "22:00",
              "23:00",
            ],
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
          yAxis: {
            type: "value",
            // min: 0,
            // minInterval: 1,
            // splitArea: {
            //   show: false,
            // },
            axisLine: {
              show: false,
            },
            // axisTick: {
            //   show: false,
            // },
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

          series: [
            {
              type: "line",
              smooth: false, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                color: "rgba(84, 181, 157, 1)",
                width: 1,
              },
              name: "光伏",
              data: chartData.day7,
            },
            ,
            {
              type: "line",
              smooth: false, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                color: "rgba(31, 149, 215, 1)",
                width: 1,
              },
              name: "市电",
              data: chartData.lastyear,
            },
            {
              type: "line",
              smooth: false, // 平滑曲线显示
              showSymbol: false,
              lineStyle: {
                color: "rgba(239, 175, 76, 1)",
                width: 1,
              },
              name: "储能",
              data: chartData.today,
            },
          ],
        };
        this.myChart1.setOption(option);
      });
    },
  },
};
</script>
<style scoped lang="scss">
.tabBox {
  height:calc(100% - 30px);
  .lenged {
    margin-top: 4px;
    width: 100%;
    height: 22px;
    background: linear-gradient(90deg, #014781 0%, rgba(1, 71, 129, 0) 100%);
    display: flex;
    justify-content: space-between;
    font-size: 0.7vw;
    line-height: 22px;
    // color: #9ba0bc;
    >div:first-of-type{
      color: #c5d0e0 ;
    }
    >div:last-of-type{
      color: #9ba0bc;
    }
    > div {
      display: flex;
      > div {
        padding-left: 10px;
        display: flex;
        align-items: center;
        > div {
          width: 8px;
          height: 8px;
          border-radius: 4px;
          margin-right: 4px;
        }
        .green {
          background: #54b59d;
        }
        .yellow {
          background: #efaf4c;
        }
        .blue {
          background: #37e7ff;
        }
      }
    }
  }
  #zong {
    width:100%;
    height: 115px;
    div{
        width:100% !important;
    }
  }
}
</style>