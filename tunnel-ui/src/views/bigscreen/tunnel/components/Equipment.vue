<template>
  <div class="EquipmentBox">
    <div class="title">设备运行</div>
    <div ref="echartsBox" id="Equipment"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";
export default {
  props: {
    equipmentData: {
      type: Object,
    },
  },
  data() {
    return {};
  },
  watch: {
    equipmentData() {
      this.Equipment();
    },
  },
  mounted() {
    this.Equipment();
    this.watchSize();
  },
  methods: {
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    Equipment() {
      var Equipment = echarts.init(document.getElementById("Equipment"));
      var option = {
        tooltip: {
          trigger: "item",
          // formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          // orient: 'vertical',
          icon: "roundRect",
          itemHeight: 12,
          itemWidth: 12,
          top: "84%",
          x: "center",
          textStyle: {
            color: "#fff",
          },
        },
        series: [
          {
            name: "设备状态",
            type: "pie",
            center: ["50%", "44%"],
            radius: "55%",
            avoidLabelOverlap: false,
            color: ["#32A8FF", " #02C800"],
            formatter: "{b}: {d}",
            label: {
              normal: {
                position: "inner",
                show: true, //去掉饼图的导出线和文字
                formatter: "{d}%",
                color: "white",
              },
            },
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
              normal: {
                color: "red",
                lineStyle: {
                  // 系列级个性化折线样式
                  width: 2,
                  type: "solid",
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#00b37d",
                    },
                    {
                      offset: 1,
                      color: "red",
                    },
                  ]), //线条渐变色
                },
              },
            },
            data: [
              {
                value: this.equipmentData.normalVal,
                name: "正常",
                // label: {
                //     normal: {
                //        textStyle: {
                //             color: 'white'  // 改变标示文字的颜色
                //        }
                //     }
                // },
                itemStyle: {
                  normal: {
                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                      {
                        //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上

                        offset: 0,
                        color: "#102E6D",
                      },
                      {
                        offset: 0.5,
                        color: "#007ac6",
                      },
                      {
                        offset: 1,
                        color: "#4FACDD",
                      },
                    ]),
                  },
                },
              },
              {
                value: this.equipmentData.malfunctionVal,
                name: "故障",
                // label: {
                //    normal: {
                //        textStyle: {
                //            color: 'white'  // 改变标示文字的颜色
                //            }
                //        }
                // },
                itemStyle: {
                  normal: {
                    color: "#52B5A8",
                  },
                },
              },
            ],
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      Equipment.setOption(option);
      // 设置默认突出区域
      Equipment.dispatchAction({
        type: "highlight", //突出高亮显示;
        seriesIndex: 0,
        dataIndex: 1, //默认索引为0，即10%
      });
    },
  },
};
</script>

<style scoped="scoped">
.EquipmentBox {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
#Equipment {
  width: 100%;
  height: 81%;
  background-color: #00598f;
}
.title {
  color: #09bdef;
  background-color: #00598f;
}
</style>