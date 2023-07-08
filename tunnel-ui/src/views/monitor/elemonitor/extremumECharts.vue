<template>
  <div class="echarts" ref="echarts" style="width:79vw;height:30vh"></div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'echartsEnvironment',
  data() {
    return {}
  },

  props: ['zhuRiDataList'],
  // 注册组件
  components: {},
  // 计算属性
  computed: {
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
  },
  // 生周期函数
  mounted() {
    this.initEChartsFn()
  },
  // 侦听器
  watch: {
    zhuRiDataList: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        this.initEChartsFn()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    }
  },
  // 事件函数
  methods: {
    initEChartsFn() {
      if (this.zhuRiDataList) {
        let date = this.zhuRiDataList.map(item => {
          return item.time.slice(5)
        })
        let data1 = this.zhuRiDataList.map(item => {
          return item.maxV
        })
        let data2 = this.zhuRiDataList.map(item => {
          return item.minV
        })
        let data3 = this.zhuRiDataList.map(item => {
          return item.avgV
        })
        this.echartsInit(date, data1, data2, data3)
      }
    },
    echartsInit(date, data1, data2, data3) {
      var chartDom = this.$refs.echarts
      var myChart = echarts.init(chartDom)
      var option
      const colorList = ['#1E90FF', '#FFA500', '#FF4500']
      // 日期
      // var date = ['10.1', '10.2', '10.3', '10.4', '10.5', '10.6', '10.7', '10.8', '10.9', '10.10', '10.11', '10.12', '10.13', '10.14', '10.15', '10.16', '10.17', '10.18', '10.19', '10.20', '10.21', '10.22', '10.23', '10.24', '10.25', '10.26', '10.27', '10.28', '10.29', '10.30']
      // var data1 = [7, 10, 0, 4, 6, 10, 25, 28, 24, 200, 25, 22, 20] // 最大值
      // var data2 = [3, 4, 16, 2, 0, 2, 5, 3, 4, 5, 2, 21, 5] // 最小值
      // var data3 = [4, 14, 7, 6, 6, 12, 30, 31, 28, 34, 27, 22, 20] // 平均值

      option = {
        title: {
          text: 'V',
          textStyle: {
            align: 'center',
            fontSize: 16,
            color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
          },
          top: '5%',
          left: '6%',
          show: true
        },
        legend: {
          // x: 'center',
          // y: 'top',
          // show: true,
          left: '85%',
          top: '5%',
          itemWidth: 6,
          itemGap: 20,
          textStyle: {
            color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
          },
          data: ['最大值', '最小值', '平均值']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            label: {
              show: true,
              backgroundColor: '#fff',
              color: '#556677',
              borderColor: 'rgba(0,0,0,0)',
              shadowColor: 'rgba(0,0,0,0)',
              shadowOffsetY: 0
            },
            lineStyle: {
              width: 0
            }
          },
          backgroundColor: '#fff',
          textStyle: {
            color: '#5c6c7c'
          },
          padding: [10, 10],
          extraCssText: 'box-shadow: 1px 0 2px 0 rgba(163,163,163,0.5)'
        },
        grid: {
          top: '15%',
          left: '7%',
          right: '3%',
          bottom: '10%'
        },
        xAxis: [
          {
            type: 'category',
            name: '',
            data: date,
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
              }
            },
            axisTick: {
              show: true
            },
            axisLabel: {
              interval: 0,
              textStyle: {
                color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
              },
              // 默认x轴字体大小
              fontSize: 12,
              // margin:文字到x轴的距离
              margin: 15
            },
            axisPointer: {
              label: {
                padding: [0, 0, 10, 0],
                /* 除了padding[0]建议必须是0之外，其他三项可随意设置
                    和CSSpadding相同，[上，右，下，左]
                    如果需要下边线超出文字，设左右padding即可，注：左右padding最好相同
                    padding[2]的10:
                    10 = 文字距下边线的距离 + 下边线的宽度
                    如：UI图中文字距下边线距离为7 下边线宽度为2
                    则padding: [0, 0, 9, 0]  */
                // 这里的margin和axisLabel的margin要一致!
                margin: 15,
                // 移入时的字体大小
                fontSize: 12,
                backgroundColor: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: '#fff' // 0% 处的颜色
                    },
                    {
                      offset: 0.86,
                      /*  0.86 = （文字 + 文字距下边线的距离）/（文字 + 文字距下边线的距离 + 下边线的宽度）  */
                      color: '#fff' // 0% 处的颜色
                    },
                    {
                      offset: 0.86,
                      color: '#33c0cd' // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: '#33c0cd' // 100% 处的颜色
                    }
                  ],
                  global: false // 缺省为 false
                }
              }
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed'
              }
            },
            boundaryGap: false
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            nameTextStyle: {
              color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
            },
            axisTick: {
              show: true
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
              }
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme == "theme-dark" ? "#fff" : "#000"
              }
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed'
              }
            }
          }
        ],
        series: [
          {
            name: '最大值',
            type: 'line',
            data: data1,
            symbolSize: 1,
            symbol: 'circle',
            smooth: true,
            yAxisIndex: 0,
            showSymbol: false,
            emphasis: {
              focus: 'series'
            },
            lineStyle: {
              width: 2,
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: 'rgba(231, 169, 0,1)'
                },
                {
                  offset: 1,
                  color: 'rgba(231, 169, 0,1)'
                }
              ]),
              shadowColor: 'rgba(231, 169, 0, 0.3)',
              shadowBlur: 10,
              shadowOffsetY: 20
            },
            itemStyle: {
              normal: {
                color: 'rgba(231, 169, 0,1)',
                borderColor: 'rgba(231, 169, 0,1)'
              }
            },
            markPoint: {
              symbol: 'pin', //标记(气泡)的图形
              symbolSize: 50, //标记(气泡)的大小
              itemStyle: {
                color: 'rgba(231, 169, 0,1)', //图形的颜色。
                borderColor: '#000', //图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
                borderWidth: 0, //描边线宽。为 0 时无描边。
                borderType: 'solid' //柱条的描边类型，默认为实线，支持 ‘solid’, ‘dashed’, ‘dotted’。
              },
              data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' }
              ]
            },
            markLine: {
              data: [{ type: 'average', name: '平均值' }]
            }
          },
          {
            name: '最小值',
            type: 'line',
            data: data2,
            symbolSize: 1,
            symbol: 'circle',
            smooth: true,
            yAxisIndex: 0,
            showSymbol: false,
            emphasis: {
              focus: 'series'
            },
            lineStyle: {
              width: 2,
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: 'rgba(18, 187, 93,1)'
                },
                {
                  offset: 1,
                  color: 'rgba(18, 187, 93,1)'
                }
              ]),
              shadowColor: 'rgba(18, 187, 93,0.3)',
              shadowBlur: 10,
              shadowOffsetY: 20
            },
            itemStyle: {
              normal: {
                color: 'rgba(18, 187, 93,1)',
                borderColor: 'rgba(18, 187, 93,1)'
              }
            },
            markPoint: {
              symbol: 'pin', //标记(气泡)的图形
              symbolSize: 50, //标记(气泡)的大小
              itemStyle: {
                color: 'rgba(18, 187, 93,1)', //图形的颜色。
                borderColor: '#000', //图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
                borderWidth: 0, //描边线宽。为 0 时无描边。
                borderType: 'solid' //柱条的描边类型，默认为实线，支持 ‘solid’, ‘dashed’, ‘dotted’。
              },
              data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' }
              ]
            },
            markLine: {
              data: [{ type: 'average', name: '平均值' }]
            }
          },
          {
            name: '平均值',
            type: 'line',
            data: data3,
            symbolSize: 1,
            yAxisIndex: 0,
            symbol: 'circle',
            smooth: true,
            showSymbol: false,
            emphasis: {
              focus: 'series'
            },
            lineStyle: {
              width: 2,
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                {
                  offset: 0,
                  color: 'rgba(255, 73, 73,1)' // 线条开始颜色
                },
                {
                  offset: 1,
                  color: 'rgba(255, 73, 73,1)' // 线条结束颜色
                }
              ]),
              shadowColor: 'rgba(255, 73, 73, 0.3)',
              shadowBlur: 10,
              shadowOffsetY: 20
            },
            itemStyle: {
              normal: {
                color: 'rgba(255, 73, 73,1)',
                borderColor: 'rgba(255, 73, 73,1)'
              }
            },
            markPoint: {
              symbol: 'pin', //标记(气泡)的图形
              symbolSize: 50, //标记(气泡)的大小
              itemStyle: {
                // color: '#4587E7', //图形的颜色。
                borderColor: 'rgba(255, 73, 73,1)', //图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
                borderWidth: 0, //描边线宽。为 0 时无描边。
                borderType: 'solid' //柱条的描边类型，默认为实线，支持 ‘solid’, ‘dashed’, ‘dotted’。
              },
              data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' }
              ]
            },
            markLine: {
              data: [{ type: 'average', name: '平均值' }]
            }
          }
        ]
      }

      option && myChart.setOption(option)
    }
  }
}
</script>
<style lang='scss' scoped>
</style>
