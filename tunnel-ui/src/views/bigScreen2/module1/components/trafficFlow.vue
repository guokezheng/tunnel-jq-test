<template>
  <div class="tabBox">
    <el-radio-group v-model="tabModel" class="tabButton">
      <el-radio-button label="总车流量"></el-radio-button>
      <el-radio-button label="隧道车流量Top10"></el-radio-button>
    </el-radio-group>
    <div class="row2" v-show="tabModel == '总车流量'">
      <div>总车流量</div>
      <div>
        <span class="yellow">{{ thisYearCount }}</span
        ><span>辆</span>
      </div>
      <div>
        <span>同比</span><span class="green">{{ yearPercent }}%</span>
      </div>
    </div>
    <!-- <div class="lenged" v-show="tabModel == '总车流量'"> -->
    <!-- <div>
          <div>
            <div class="green"></div>
            近7日平均
          </div>
          <div>
            <div class="yellow"></div>
            去年同期
          </div>
          <div>
            <div class="blue"></div>
            今日
          </div>
        </div>
        <div>单位：万辆</div> -->
    <!-- </div> -->
    <div id="zong" v-show="tabModel == '总车流量'" ref="zong"></div>
    <div id="top10" v-show="tabModel == '隧道车流量Top10'" ref="top10"></div>
  </div>
</template>
  <script>
import * as echarts from "echarts";
import { carFlow } from "@/api/bigScreen/model1";
export default {
  data() {
    return {
      tabModel: "总车流量",
      myChart1: null,
      myChart2: null,
      rankImg: require("@/assets/Example/bigScreen/rank2.png"),
      energyConsumption: null,
      thisYearCount: "",
      yearPercent: "",
      dayArr: [],
      hourArr: [],
      monthArr: [
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
      hourStrings: ["0:00", "4:00", "8:00", "12:00", "16:00", "20:00"],
    };
  },
  watch: {
    tabModel: function (newValue, oldValue) {
      this.getList();
    },
  },
  created() {
    this.getList();
    this.dateFormat();
    this.hourFormat();
  },
  methods: {
    getList() {
      carFlow().then((res) => {
        this.thisYearCount = res.data.thisYearCount;
        this.yearPercent = res.data.yearPercent;
        let weekList = res.data.weekList;
        let fourHoursList = res.data.fourHoursList;
        let lastYearList = res.data.lastYearList;
        let topList = res.data.topList;
        let xData = [];
        let yData = [];
        for (let i = 0; i < topList.length; i++) {
          xData.push(topList[i].count);
          yData.push("TOP" + (i + 1) + topList[i].tunnelName);
        }
        if(this.tabModel == "总车流量"){
          this.getEnergyConsumption(weekList, lastYearList, fourHoursList);
        }else{
          this.initEchats2(xData, yData);
        }
      });
    },
    // 获取最近七天数组
    dateFormat() {
      const days = 7; // 近7天
      const dateList = Array.from({ length: days }, (v, i) => i).map((day) => {
        const date = new Date();
        date.setDate(date.getDate() - day);
        return date.getMonth() + 1 + "-" + date.getDate();
      });
      this.dayArr = dateList.reverse();
    },
    hourFormat() {
      //  到当前时间数组
      let hour = new Date().getHours();
      let laneArray = JSON.parse(JSON.stringify(this.hourStrings));
      this.hourArr = laneArray.slice(0, 1 + hour / 4);
    },
    initEchats2(xData, yData) {
      // this.$nextTick(function () {
        if (
          this.myChart2 != null &&
          this.myChart2 != "" &&
          this.myChart2 != undefined
        ) {
          // 销毁
          this.myChart2.dispose();
        }
        let myChart2 = this.$refs.top10;
        if(!myChart2){
          return 
        }
        this.myChart2 = echarts.init(myChart2);

        let option = {
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
          dataZoom: [
            {
              type: "slider",
              show: true, //隐藏或显示（true）组件
              backgroundColor: "rgb(19, 63, 100)", // 组件的背景颜色。
              fillerColor: "rgb(16, 171, 198)", // 选中范围的填充颜色。
              borderColor: "rgb(19, 63, 100)", // 边框颜色
              showDetail: false, //是否显示detail，即拖拽时候显示详细数值信息
              startValue: 0, // 数据窗口范围的起始数值
              endValue: 6, // 数据窗口范围的结束数值（一页显示多少条数据）
              yAxisIndex: [0, 1], //控制哪个轴，如果是 number 表示控制一个轴，如果是 Array 表示控制多个轴。此处控制第二根轴
              filterMode: "empty",
              width: 0, //滚动条高度
              height: "80%", //滚动条显示位置
              right: 3, // 距离右边
              handleSize: 0, //控制手柄的尺寸
              zoomLoxk: true, // 是否锁定选择区域（或叫做数据窗口）的大小
              top: "middle",
            },
            {
              //没有下面这块的话，只能拖动滚动条，鼠标滚轮在区域内不能控制外部滚动条
              type: "inside",
              yAxisIndex: [0, 1], //控制哪个轴，如果是 number 表示控制一个轴，如果是 Array 表示控制多个轴。此处控制第二根轴
              zoomOnMouseWheel: false, //滚轮是否触发缩放
              moveOnMouseMove: true, //鼠标移动能否触发平移
              moveOnMouseWheel: true, //鼠标滚轮能否触发平移
            },
          ],
          grid: {
            left: "-24%",
            right: "18%",
            bottom: "6%",
            top: "4%",
            containLabel: true,
          },
          xAxis: {
            type: "value",
            axisLabel: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            splitLine: {
              //网格
              show: false,
            },
          },
          yAxis: {
            type: "category",
            inverse: true,
            axisLabel: {
              //坐标轴刻度标签的相关设置。
              // formatter:'{value}(万元)',
              interval: 0, //横轴信息全部显示
              rotate: 0, //-15度角倾斜显示
              color: "#94BACF",
              fontSize: 12,
              margin: 110, //刻度标签与轴线之间的距离。
              left: 0, //整个echart位置
              textStyle: {
                align: "left",
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              //y轴刻度线
              show: false,
            },
            splitLine: {
              //网格
              show: false,
            },
            data: yData,
          },
          series: [
            {
              type: "bar",
              barWidth: "6px", //柱子宽度
              color: new echarts.graphic.LinearGradient(1, 1, 0, 0, [
                //渐变色
                {
                  offset: 1,
                  color: "rgba(14, 141, 221,0)",
                },
                {
                  offset: 0,
                  color: "rgba(14, 141, 221,1)",
                },
              ]),
              itemStyle: {
                barBorderRadius: [0, 5, 5, 0],
              },
              label: {
                normal: {
                  show: true,
                  position: "right",
                  align: "left",
                  textStyle: {
                    color: "#fff",
                    fontSize: 10,
                  },
                  rich: {
                    c: {
                      color: "#fff",
                      fontSize: 11,
                      align: "center",
                      padding: [5, 5, 2, 5],
                      backgroundColor: {
                        image: this.rankImg,
                      },
                    },
                  },
                  formatter: function (xData) {
                    return ["   {c|" + xData.value + "}"];
                  },
                },
              },
              data: xData,
            },
            {
              name: "",
              type: "scatter",
              hoverAnimation: false,
              data: xData,
              yAxisIndex: 0,
              symbolSize: 11,
              itemStyle: {
                normal: {
                  color: "#FFFFFF",
                  opacity: 1,
                  shadowBlur: 10,
                  shadowColor: "rgba(255,255,255,0.4)",
                  shadowOffsetX: -1,
                  shadowOffsetY: 1,
                },
              },
              zlevel: 0,
              label: {
                show: false,
              },
            },
            {
              name: "",
              type: "scatter",
              hoverAnimation: false,
              data: xData,
              yAxisIndex: 0,
              symbolSize: 11,
              symbol:
                "path://M512 960C264.576 960 64 759.424 64 512S264.576 64 512 64s448 200.576 448 448-200.576 448-448 448z m0-268.8a179.2 179.2 0 1 0 0-358.4 179.2 179.2 0 0 0 0 358.4z",
              itemStyle: {
                color: "rgba(1,29,63,.8)",
                opacity: 1,
                borderColor: "rgba(255,255,255,.5)",
                borderWidth: 1,
                shadowBlur: 10,
                shadowColor: "rgba(255,255,255,0.4)",
                shadowOffsetX: -1,
                shadowOffsetY: 1,
              },
              zlevel: 3,
              label: {
                show: false,
              },
            },
          ],
        };
        let that = this;
        if (xData.length > 0 && yData.length > 0) {
          setInterval(function () {
            // 每次向后滚动一个，最后一个从头开始。
            if (option.dataZoom[0].endValue == yData.length - 1) {
              option.dataZoom[0].endValue = 6;
              option.dataZoom[0].startValue = 0;
            } else {
              option.dataZoom[0].endValue = option.dataZoom[0].endValue + 1;
              option.dataZoom[0].startValue = option.dataZoom[0].startValue + 1;
            }
            that.myChart2.setOption(option);
          }, 4000);
        }
        this.myChart2.setOption(option);
        window.addEventListener("resize", function () {
          this.myChart2.resize();
        });
      // });
    },
    getEnergyConsumption(weekList, lastYearList, fourHoursList) {
      let xDataN = this.dayArr;
      let xDataY = this.monthArr;
      let xDataR = this.hourArr;
      let xData = this.dayArr;
      let yData = [];
      yData.push({
        name: "近7日平均",
        type: "line",
        color: "#59c5f9",
        symbol: "circle",
        symbolSize: [7, 7],
        itemStyle: {
          normal: {
            borderColor: "white",
          },
        },
        smooth: true,
        // 渐变色
        areaStyle: {
          normal: {
            //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: "#59c5f9",
              },
              {
                offset: 1,
                color: "rgba(89,197,249,0.3)",
              },
            ]),
          },
        },
        data: weekList,
      });
      yData.push({
        name: "去年同期",
        type: "line",
        color: "#db72a7",
        symbol: "circle",
        symbolSize: [7, 7],
        itemStyle: {
          normal: {
            borderColor: "white",
          },
        },
        smooth: true,
        stack: "Total",
        areaStyle: {},
        emphasis: {
          focus: "series",
        },
        //渐变色
        areaStyle: {
          normal: {
            //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: "#db72a7",
              },
              {
                offset: 1,
                color: "rgba(219,114,167,0.3)",
              },
            ]),
          },
        },
        data: lastYearList,
      });
      yData.push({
        name: "今日",
        type: "line",
        color: "#FDB400",
        symbol: "circle",
        symbolSize: [7, 7],
        itemStyle: {
          normal: {
            borderColor: "white",
          },
        },
        smooth: true,
        // 渐变色
        areaStyle: {
          normal: {
            //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: "#FDB400",
              },
              {
                offset: 1,
                color: "rgba(253,180,0,0.3)",
              },
            ]),
          },
        },
        data: fourHoursList,
      });

      // this.$nextTick(() => {
        this.initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR);
      // });
      // });
    },
    // 能耗监测echarts
    initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR) {
      this.$nextTick(function () {
        if (
          this.energyConsumption != null &&
          this.energyConsumption != "" &&
          this.energyConsumption != undefined
        ) {
          // 销毁
          this.energyConsumption.dispose();
        }
        let zong = this.$refs.zong;
        if(!zong){
          return 
        }
        this.energyConsumption = echarts.init(zong);

        var option = {
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
          legend: {
            show: true,
            icon: "circle",
            itemWidth: 10,
            itemHeight: 10,
            selectedMode: "single", // 单选
            selected: {
              近7日平均: true,
              去年同期: false,
              今日: false,
              // 停车: false,
              // 逆行: false,
              // 烟火: false,
              // 抛洒物: false,
              // 其他: false,
              // 实线变道: false,
              // 货车走快车道: false,
              // 占用应急车道: false,
            },
            x: "center",
            top: 10,
            data: [
              "近7日平均",
              "去年同期",
              "今日",
              // "停车",
              // "逆行",
              // "烟火",
              // "抛洒物",
              // "其他",
              // "实线变道",
              // "货车走快车道",
              // "占用应急车道",
            ],
            textStyle: {
              //图例文字的样式
              color: "#9ba0bc",
              fontSize: 12,
            },
          },
          calculable: true,
          grid: {
            left: "6%",
            right: "2%",
            bottom: "8%",
            top: "30%",
            containLabel: true,
          },
          xAxis: [
            {
              // name: "小时",
              nameTextStyle: {
                fontFamily: "PingFang",
              },
              type: "category",
              axisTick: {
                show: false,
              },
              splitLine: {
                show: false,
              },
              // boundaryGap: false,
              axisLabel: {
                textStyle: {
                  color: "#9ba0bc",
                  fontSize: 12,
                  fontFamily: "PingFang",
                },
              },
              axisLine: {
                lineStyle: {
                  color: "#11395D",
                },
              },
              data: xData,
            },
          ],
          yAxis: [
            {
              name: "件",
              nameTextStyle: {
                color: "#9ba0bc",
                fontSize: 12,
                padding: [0, 20, 0, 0],
              },
              type: "value",
              minInterval: 1,
              // min: 0,
              // max: 200,
              axisTick: {
                show: false,
              },
              // max: 200,
              // min: 0,
              splitNumber: 5,
              splitLine: {
                show: true,
                lineStyle: {
                  //分割线的样式
                  color: ["#11395D"],
                  width: 1,
                  type: "dashed",
                },
              },
              axisLine: {
                show: false,
              },
              axisLabel: {
                textStyle: {
                  color: "#9ba0bc",
                  fontSize: 12,
                },
              },
            },
          ],
          series: yData,
        };

        this.energyConsumption.on("legendselectchanged", (obj) => {
          var options = this.energyConsumption.getOption();
          //这里是选择切换什么样的x轴，那么他会进行对Y值的切换
          if (obj.name == "近7日平均") {
            options.xAxis[0].data = xDataN;
          } else if (obj.name == "去年同期") {
            options.xAxis[0].data = xDataY;
          } else if (obj.name == "今日") {
            options.xAxis[0].data = xDataR;
          }
          this.energyConsumption.setOption(options, true);
        });
        // this.energyConsumption.setOption(options, true);
        this.energyConsumption.setOption(option);
        window.addEventListener("resize", function () {
          this.energyConsumption.resize();
        });
      });

      // }
    },
  },
};
</script>
  <style scoped lang="scss">
.tabBox {
  height: calc(100% - 30px);
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
    margin: 4px 0;
    .el-radio-button {
      margin-right: 4px;
    }
    ::v-deep .el-radio-button--medium .el-radio-button__inner {
      padding: 4px 20px !important;
    }
  }
  .row2 {
    display: flex;
    > div {
      width: 33.33%;
      height: 36px;
      line-height: 36px;
      border: solid 1px #2c7fc3;
      text-align: center;
    }
    > div:first-of-type {
      background: linear-gradient(
        180deg,
        rgba($color: #00aced, $alpha: 0.8),
        rgba($color: #0079db, $alpha: 0.8)
      );
    }
    .yellow {
      color: #fed37d;
      font-size: 20px;
      font-weight: bold;
      padding-right: 4px;
    }
    .green {
      color: #36c19c;
    }
  }
  #zong {
    width: 100%;
    height: calc(100% - 66px);
    > div {
      height: 100% !important;
    }
  }
  #top10 {
    width: 100%;
    height: calc(100% - 30px);

    > div {
      height: 100% !important;
    }
  }
}
</style>