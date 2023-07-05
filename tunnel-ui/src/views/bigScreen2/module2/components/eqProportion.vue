<template>
  <div class="pieQyEchart">
    <div id="pie" style="height: 100%; width: 50%"></div>
    <div class="list">
      <div class="list_item" v-for="(item, index) in zdPie" :key="item.id">
        <div class="block" :style="{ backgroundColor: colorArr[index] }"></div>
        <span style="width: 65%">{{ item.typeName }}</span>
        <span :style="{ color: colorArr[index] }">|</span>
        <span :style="{ color: colorArr[index] }">{{ item.percent }} %</span>
      </div>
    </div>
  </div>
</template>
  
  <script>
import * as echarts from "echarts";
import { eqPercent } from "@/api/bigScreen/model2";
export default {
  data() {
    return {
      myChart: null,
      zdPie: [],
      wbPie: [],
      allCount:0,
      colorArr: [
        "#4AA7F1",
        "#5ED3FA",
        "#E3BA73",
        "#EF866D",
        "#BD83F2",
        "#FF96DF",
        "#3BA272",
        "#A0FF74",
        "#E3BA73",
      ],
      colorOutArr: [
        "#0D2F50",
        "#5ED3FA",
        "#E3BA73",
        "#EF866D",
        "#BD83F2",
        "#FF96DF",
        "#3BA272",
        "#A0FF74",
        "#E3BA73",
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      eqPercent().then((res) => {
        this.zdPie = res.data.list.splice(0, 9);
        this.zdPie.forEach((value, index) => {
          this.allCount += value.typeCount
          value["color"] = this.colorArr[index];
          value["itemStyle"] = {};
          value.itemStyle.color = this.colorOutArr[index];
          value.name = value.typeName;
          value.value = value.typeCount;
          value.count = value.percent + "%";
        });
        this.wbPie = JSON.parse(JSON.stringify(this.zdPie));
        this.wbPie.forEach((value, index) => {
          value.itemStyle = {};
          if (index == 0) {
            value.itemStyle.color = this.colorArr[0];
          } else {
            value.itemStyle.color = "rgba(255,255,255,0)";
          }
        });
        this.initCharts();
      });
    },
    initCharts() {
      let e = document.getElementById("pie")
      if(!e){
        return
      }
      this.myChart = echarts.init(e);
      var option = {
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
          position(point, params, dom, rect, size) {
            const obj = {};
            // 鼠标的水平位置（point[0]）小于图表容器宽度的一半（size.viewSize[0] / 2）时
            // tooltip显示在右边（right）,反之显示在左边（left）。
            // -100 为位置偏移量，根据具体需求进行调整
            obj[["right", "right"][+(point[0] < size.viewSize[0] / 2)]] = -50;
            // 鼠标的竖直位置（point[1]）小于图表容器高度的一半（size.viewSize[1] / 2）时
            // tooltip显示在底边（bottom）,反之显示在顶边（top）。
            // -58 为位置偏移量，根据具体需求进行调整
            obj[["bottom", "top"][+(point[1] < size.viewSize[1] / 2)]] = -8;
            return obj;
          },
        },
        title: {
          text: this.allCount,
          subtext: "总数",
          textStyle: {
            color: "#f2f2f2",
            fontSize: 16,
          },
          subtextStyle: {
            color: "#9ba0bc",
            fontSize: 12,
          },
          x: "center",
          y: "center",
        },
        series: [
          {
            type: "pie",
            radius: ["55%", "70%"],
            center: ["50%", "53%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
            },
            emphasis: {
              label: {
                show: false,
                fontSize: 18,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.zdPie,
          },
          {
            type: "pie",
            radius: ["70%", "80%"],
            center: ["50%", "53%"],
            avoidLabelOverlap: true,
            label: {
              show: false,
            },
            emphasis: {
              label: {
                show: false,
                fontSize: 18,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.wbPie,
          },
          // 总数
          {
            type: "pie",
            name: "总数",
            radius: [0, "47%"],
            center: ["50%", "53%"],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "rgba(22,67,95,0)",
                },
                {
                  offset: 0.5,
                  color: "#0E3460",
                },
                {
                  offset: 1,
                  color: "rgba(22,67,95,0)",
                },
              ]),
            },
            label: {
              show: false,
            },
            data: ["100%"],
          },
          // 总数边框
          {
            type: "pie",
            silent: true,
            center: ["50%", "53%"],
            radius: ["43%", "44%"],
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
                value: 10,
                name: "1",
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
                value: 20,
                name: "2",
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
        ],
      };
      // 绘制图表
      option && this.myChart.setOption(option);
    },
  },
};
</script>
  
  <style lang="less" scoped>
.pieQyEchart {
  display: flex;
  align-items: center;
  height: calc(100% - 30px);
  .list {
    width: 50%; 
    padding: 0px 10px 0 0px;
    height: 100%;
    overflow-y: auto;
    ::-webkit-scrollbar{
      width:0px !important;
    }
    .list_item {
      font-size: 10px;
      width: 100%;
      background: linear-gradient(90deg, #014781 0%, rgba(1, 71, 129, 0) 100%);
      border-radius: 2px 2px 2px 2px;
      opacity: 1;
      margin: 7px 0;
      color: #c5d0e0;
      display: flex;
      justify-content: space-between;
      height: 2vh;
      cursor: default;
      .block {
        display: inline-block;
        width: 7px;
        height: 7px;
        margin: auto 4px;
      }
    }
  }
}
</style>
  