<template>
  <div style="width: 100%; height: calc(100% - 30px); padding-top: 2%">
    <!-- 按钮 -->
    <div
      style="
        display: flex;
        height: 10%;
        justify-content: space-between;
        padding-right: 5px;
      "
    >
      <el-radio-group v-model="tabModel" class="tabButton">
        <el-radio-button label="chart3">近30天</el-radio-button>
        <el-radio-button label="chart4">近6个月</el-radio-button>
      </el-radio-group>
      <!-- <div style="display: flex">
          <el-button class="btn" size="mini">近30天</el-button>
          <el-button class="btn" size="mini" id="btn">近6个月</el-button>
        </div> -->
      <div>单位:tCO₂e</div>
    </div>
    <div style="width: 100%; height: 86%; display: flex">
      <!-- 图表 -->
      <div
        id="chart3"
        style="width: 50%; height: 100%"
        v-if="tabModel == 'chart3'"
      ></div>
      <div
        id="chart4"
        style="width: 50%; height: 100%"
        v-if="tabModel == 'chart4'"
      ></div>
      <!-- 右侧 -->
      <div class="right-text" >
        <el-row class="row" v-for="(item,index) of tabModel == 'chart3'?monthList:sixMonthsList" :key="index">
          <div class="left-span">
            <span
              class="leftTopSpan"
              :style="{backgroundColor: color1[index]}"
            ></span>
            <span>{{item.typeName}}</span>
          </div>
          <div class="right-span">
            <span :style="{color: color1[index]}">|</span
            ><span 
            style="padding-left: 2px;"
            :style="{color:color1[index]}"
              >{{item.percent}}%</span
            >
          </div>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
import * as echarts from "echarts";
import { faultCategory } from "@/api/bigScreen/model2";

export default {
  data() {
    return {
      tabModel: "chart3",
      color0: ["rgba(211,169,70, 0)", "rgba(79,174,203, 0)","rgba(145,204,117,0)","rgba(252,132,82,0)","rgba(234,124,204,0)","rgba(84,112,198,0)","rgba(59,162,114,0)","rgba(115,192,222,0)"],
      color1: ["rgba(211,169,70, 1)", "rgba(79,174,203, 1)","rgba(145,204,117,1)","rgba(252,132,82,1)","rgba(234,124,204,1)","rgba(84,112,198,1)","rgba(59,162,114,1)","rgba(115,192,222,1)"],
      monthList:[],
      sixMonthsList:[]
    };
  },
  computed: {},
  watch: {
    tabModel: function (newVal, oldVal) {
      console.log(newVal, "newVal");
      this.$nextTick(()=>{
        this.openChart()
      })
    },
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      faultCategory().then((res) => {
        this.monthList = res.data.monthList.splice(0, 8);
        this.sixMonthsList = res.data.sixMonthsList.splice(0, 8)
        this.openChart();
      });
    },
    openChart() {
      let list = ''
      if(this.tabModel == "chart3"){
        list = this.monthList
      }else{
        list = this.sixMonthsList
      }
      if (
        this.myChart3 != null &&
        this.myChart3 != "" &&
        this.myChart3 != undefined
      ) {
        // 销毁
        this.myChart3.dispose();
      }
      let e = document.getElementById(this.tabModel)
      if(!e){
        return
      }
      this.myChart3 = echarts.init(e);
      let echartData = []
      list.map((item, index) => {
        echartData.push({
          name: item.typeName,
          value: Number(item.percent),
          color: this.color0[index],
          // itemStyle: {
          //   normal: {
          //     //颜色渐变
          //     color: new echarts.graphic.LinearGradient(1, 0, 0, 1, [
          //       {
          //         offset: 1,
          //         color: this.color0[index],
          //       },
          //       {
          //         offset: 0,
          //         color: this.color1[index],
          //       },
          //     ]),
          //   },
          // },
        });
      });
      var arr = [];
      for (var i = 0; i < echartData.length; i++) {
        arr.push(echartData[i].value);
      }
      let total_datas = this.floatAdd(arr);
      let option = {
        title: {
          text: total_datas,
          subtext: "总数",
          x: "center",
          y: "center",
          top: "37%",
          textStyle: {
            color: "#fff",
            fontSize: 14,
            verticalAlign: "top",
          },
          subtextStyle: {
            color: "#9ba0bc",
            fontSize: 12,
            verticalAlign: "top",
          },
        },
        tooltip: {
          trigger: "item",
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
            type: "pie",
            radius: ["45%", "70%"],
            center: ["50%", "50%"],
            hoverAnimation: true,
            z: 10,
            itemStyle: {
              normal: {
                borderWidth: 4,
                borderColor: "rgba(9,21,42,0.3)",
              },
            },
            label: {
              show: false,
            },
            data: echartData,
          },
          // 总数
          {
            type: "pie",
            name: "总数",
            radius: [0, "38%"],
            center: ["50%", "50%"],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: "rgba(22, 67, 95, 0.7)",
                },
                {
                  offset: 1,
                  color: "rgba(22, 67, 95, 1)",
                },
              ]),
            },
            label: {
              show: false,
            },
            data: Array(Math.round(total_datas)),
          },
          //总数边框
          {
            type: "pie",
            silent: true,
            center: ["50%", "50%"],
            radius: ["38%", "40%"],
            startAngle: 80,
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
            data: [
              {
                value: 40,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "rgba(78, 179, 217, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217,1)",
                      },
                      {
                        offset: 1,
                        color: "rgba(78, 179, 217, 0)",
                      },
                    ]),
                  },
                },
              },
              {
                value: 50,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "rgba(78, 179, 217, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217,1)",
                      },
                      {
                        offset: 1,
                        color: "rgba(78, 179, 217, 0)",
                      },
                    ]),
                  },
                },
              },
            ],
          },
          {
            type: "pie",
            silent: true,
            center: ["50%", "50%"],
            radius: ["34%", "35%"],
            startAngle: 80,
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
            data: [
              {
                value: 40,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0.1,
                        color: "rgba(78, 179, 217,  0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217, 0.72)",
                      },
                      {
                        offset: 0.9,
                        color: "rgba(78, 179, 217,  0)",
                      },
                    ]),
                  },
                },
              },
              {
                value: 50,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0.1,
                        color: "rgba(78, 179, 217,  0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217, 0.72)",
                      },
                      {
                        offset: 0.9,
                        color: "rgba(78, 179, 217, 0)",
                      },
                    ]),
                  },
                },
              },
            ],
          },
          // 外层渐变圆环
          {
            name: " ",
            type: "pie",
            radius: ["83%", "80%"],
            center: ["50%", "50%"],
            avoidLabelOverlap: false, // 是否启用防止标签重叠策略
            startAngle: 60,
            hoverAnimation: false,
            legendHoverLink: false,
            label: {
              normal: {
                show: false,
                position: "center",
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold",
                },
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
            tooltip: {
              show: false,
            },
            data: [
              {
                value: 40,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "rgba(78, 179, 217, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217,1)",
                      },
                      {
                        offset: 0.95,
                        color: "rgba(78, 179, 217, 0)",
                      },
                    ]),
                  },
                },
              },
              {
                value: 50,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0.05,
                        color: "rgba(78, 179, 217, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(78, 179, 217,1)",
                      },
                      {
                        offset: 0.95,
                        color: "rgba(78, 179, 217, 0)",
                      },
                    ]),
                  },
                },
              },
            ],
          },
          {
            name: " ",
            type: "pie",
            radius: ["90%", "88%"],
            center: ["50%", "50%"],
            avoidLabelOverlap: false, // 是否启用防止标签重叠策略
            startAngle: 50,
            hoverAnimation: false,
            legendHoverLink: false,
            label: {
              normal: {
                show: false,
                position: "center",
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold",
                },
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
            tooltip: {
              show: false,
            },
            data: [
              {
                value: 40,
                name: "",
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "rgba(18, 55, 112, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(16, 96, 171, 1)",
                      },
                      {
                        offset: 0.9,
                        color: "rgba(18, 55, 112, 0)",
                      },
                    ]),
                  },
                },
              },
              {
                value: 50,
                name: "",
                itemStyle: {
                  // borderWidth: 0,
                  // color: "rgba(3, 18, 52,0)",
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0.1,
                        color: "rgba(18, 55, 112, 0)",
                      },
                      {
                        offset: 0.5,
                        color: "rgba(16, 96, 171, 1)",
                      },
                      {
                        offset: 0.9,
                        color: "rgba(18, 55, 112, 0)",
                      },
                    ]),
                  },
                },
              },
            ],
          },
          // 指针盘
          {
            name: "",
            type: "gauge",
            center: ["center", "center"],
            radius: "100%",
            detail: {
              show: false,
            },
            min: 0,
            max: 100,
            startAngle: "-120", // 仪表盘起始角度
            endAngle: "120", // 仪表盘结束角度
            axisLabel: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            pointer: {
              show: false, // 不展示指针
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: true,
              splitNumber: 1,
              lineStyle: {
                color: "rgba(255, 170, 52, 0.2)", //用颜色渐变函数不起作用
                width: 1,
              },
              length: 2,
            },
          },
          {
            name: "",
            type: "gauge",
            center: ["center", "center"],
            radius: "100%",
            detail: {
              show: false,
            },
            min: 0,
            max: 100,
            startAngle: "60", // 仪表盘起始角度
            endAngle: "-60", // 仪表盘结束角度
            axisLabel: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            pointer: {
              show: false, // 不展示指针
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: true,
              splitNumber: 1,
              lineStyle: {
                color: "rgba(255, 170, 52, 0.2)", //用颜色渐变函数不起作用
                width: 1,
              },
              length: 2,
            },
          },
        ],
      };

      this.myChart3.setOption(option);
    },
    floatAdd(arr) {
      let maxLength = 0;
      for (var i in arr) {
        try {
          let itemLength = arr[i].toString().split(".")[1]
            ? arr[i].toString().split(".")[1].length
            : 0;
          itemLength >= maxLength ? (maxLength = itemLength) : "";
        } catch (e) {
          maxLength = 0;
        }
      }
      let m = Math.pow(10, maxLength);
      let sum = 0;
      for (var i in arr) {
        sum = sum + arr[i] * m;
      }
      return sum / m;
    },
    pie2() {
      let dataArr = [];
      for (var i = 0; i < 4; i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 25,
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
    },
  },
};
</script>
<style scoped lang="scss">
/* @import url(); 引入css类 */
// .btn {
//   width: 60px;
//   height: 100%;
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   font-size: 12px;
//   background: linear-gradient(#1eace8, 50%, #0074d4);
//   color: white;
//   border: none;
//   border-radius: 1px;
// }
div {
  color: #9ba0bc;
  font-size: 0.7vw;
}
.el-button:focus,
.el-button:hover {
  background: linear-gradient(#1eace8, 50%, #0074d4);
  color: white;
}
.el-button + .el-button,
.el-checkbox.is-bordered + .el-checkbox.is-bordered {
  margin-left: 8px;
}
.right-text {
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: default;
  .row {
    width: 100%;
    height: 2vh;
    margin-bottom: 8px;
    display: flex;
    // justify-content: space-between;
    background-image: linear-gradient(
      to right,
      rgba(3, 71, 130, 1),
      rgba(3, 71, 130, 0)
    );

    span {
      color: #c5d0e0;
      font-size: 12px;
      cursor: default;
    }
    .leftTopSpan {
      display: inline-block;
      width: 7px;
      height: 7px;
      margin: 0 8px;
    }
    .left-span {
      width: 65%;
    }
    .right-span {
      width: 35%;
      text-align: right;
      span {
        width: 50%;
        line-height: 18px;
      }
    }
  }
}
#btn {
  background: linear-gradient(to bottom, #ffcd48, #fe861e);
}
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background: linear-gradient(180deg, #ffc606, #ff8200) !important;
  border: none;
}
::v-deep .el-radio-group .el-radio-button__inner {
  background: linear-gradient(
    180deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  ) !important;
  border: none;
  color: #fff;
}
.tabButton {
  // margin: 4px 0;
  .el-radio-button {
    margin-right: 4px;
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 4px 20px !important;
  }
}
</style>
