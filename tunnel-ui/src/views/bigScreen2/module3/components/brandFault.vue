<template>
    <div style="width: 100%; height: calc(100% - 30px); display: flex">
      <!-- 图表 -->
      <div id="itemized" style="width: 60%; height: 100%"></div>
      <!-- 右侧 -->
      <div class="right-text">
        <el-row class="row">
          <span class="row_top_text">湿度</span>
          <span class="row_bottom_text">50%</span>
          <div class="position_color"></div>
        </el-row>
        <el-row class="row">
          <span class="row_top_text" style="color: rgba(55, 231, 255, 1)">充电</span>
          <span class="row_bottom_text" style="color: rgba(55, 231, 255, 1)">
            119kWh
          </span>
          <div
            class="position_color"
            style="background: rgba(55, 231, 255, 1)"
          ></div>
        </el-row>
        <el-row class="row">
          <span class="row_top_text" style="color: rgba(62, 192, 153, 1)">SOC</span>
          <span class="row_bottom_text" style="color: rgba(62, 192, 153, 1)">
            40%
          </span>
          <div
            class="position_color"
            style="background: rgba(62, 192, 153, 1)"
          ></div>
        </el-row>
        <el-row class="row">
          <span class="row_top_text" style="color: rgba(239, 175, 76, 1)">放电</span>
          <span class="row_bottom_text" style="color: rgba(239, 175, 76, 1)">
            119kWh
          </span>
          <div
            class="position_color"
            style="background: rgba(239, 175, 76, 1)"
          ></div>
        </el-row>
        <el-row class="row">
          <span class="row_top_text" style="color: rgba(236, 237, 193, 1)">容量</span>
          <span class="row_bottom_text" style="color: rgba(236, 237, 193, 1)">
            119kWh
          </span>
          <div
            class="position_color"
            style="background: rgba(236, 237, 193, 1)"
          ></div>
        </el-row>
      </div>
    </div>
</template>
  <script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      myChart3: "",
    };
  },
  computed: {},
  watch: {},
  methods: {
    openChart() {
      this.$nextTick(function () {
        if (
          this.myChart3 != null &&
          this.myChart3 != "" &&
          this.myChart3 != undefined
        ) {
          // 销毁
          this.myChart3.dispose();
        }
        this.myChart3 = echarts.init(document.getElementById("itemized"));

        let splitColor = "rgba(169, 169, 169, 0.50)";
        let nameList = ["湿度", "充电", "SOC", "放电", "容量"]; // 名字
        let colorList = ['rgba(41, 151, 230, 1)','rgba(55, 231, 255, 1)','rgba(62, 192, 153, 1)','rgba(239, 175, 76, 1)','rgba(236, 237, 193, 1)']
        let valueList = [85, 80, 60, 76, 47]; //最大值为100
        // let valueList1 = [55, 50, 75, 55, 39]; // 最大值为100

        let indicatorList = [];
        nameList.map((item, index) => {
          indicatorList.push({
            name: item,
            value: valueList[index],
            max: 100,
            color: colorList[index],
          });
        });

        let option = {
          radar: {
            center: ["50%", "50%"],
            radius: "70%",
            indicator: indicatorList,
            name: {
              textStyle: {
                fontSize: 10,
                color: "rgba(230, 230, 230, 0.50)",
                padding: [0, -5, -10, 0],
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: "rgba(169, 169, 169, .5)", // 设置网格的颜色
              },
            },
            axisLine: {
              //指向外圈文本的分隔线样式
              lineStyle: {
                color: "#9ba0bc",
              },
            },
            splitArea: {
              show: false,
            },
          },
          tooltip: {
            type: "item",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
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
          series: [
            {
              name: "储能检测",
              type: "radar",
              symbol: "emptyCircle",
              symbolSize: 5,
              color: "rgba(41, 151, 230, 1)",
              label: {
                show: false,
              },
              areaStyle: {
                normal: {
                  color: "rgba(41, 151, 230, 1)",
                  opacity: 0.4,
                },
              },
              lineStyle: {
                width: 1,
                color: "rgba(41, 151, 230, 1)",
              },
              data: [
                {
                  value: valueList,
                },
              ],
            },
            // {
            //   name: "上月",
            //   type: "radar",
            //   symbol: "emptyCircle",
            //   symbolSize: 5,
            //   color: "rgba(71, 206, 166, 1)",
            //   label: {
            //     show: false,
            //   },
            //   areaStyle: {
            //     normal: {
            //       color: "rgba(71, 206, 166, 1)",
            //       opacity: 0.4,
            //     },
            //   },
            //   lineStyle: {
            //     width: 1,
            //     color: "rgba(71, 206, 166, 1)",
            //   },
            //   data: [
            //     {
            //       value: valueList1,
            //     },
            //   ],
            // },
          ],
        };

        this.myChart3.setOption(option);
      });
    },
  },
  created() {},
  mounted() {
    this.openChart();
  },
};
</script>
  <style scoped lang="less">
/* @import url(); 引入css类 */
.right-text {
  width: 38%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 2%;
  .row {
    width: 100%;
    height: 1.2vh;
    border-left: 3px solid rgba(40, 143, 219, 0.3);
    position: relative;
    padding-left: 0.6vw;
    box-sizing: border-box;
    margin-bottom: 1vh;
    display: flex;
    align-items: center;
    line-height: 1.2vh;
  }
  .position_color {
    width: 3px;
    height: 70%;
    background: rgba(41, 151, 231, 1);
    position: absolute;
    left: -3px;
    top: 0;
  }
  .row_top_text {
    display: inline-block;
    width: 45%;
    color: rgba(41, 151, 231, 1);
    font-size: 12px;
    transform: scale(0.99);
    letter-spacing: 1px;
  }
  .row_bottom_text {
    color: rgba(41, 151, 230, 1);
    font-size: 12px;
    font-weight: normal;
  }
}
.border {
  border-right: 1px solid rgba(7, 140, 255, 0.3);
  border-left: 1px solid rgba(7, 140, 255, 0.3);
  border-bottom: 1px solid rgba(7, 140, 255, 0.3);
}
</style>
    