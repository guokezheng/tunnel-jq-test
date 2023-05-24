<template>
  <div style="width: 100%; height: calc(100% - 30px); padding-top: 2%;">
    <!-- 按钮 -->
    <div style="
        display: flex;
        height: 10%;
        justify-content: space-between;
        padding-right: 5px;">
      <!-- <div style="display: flex">
        <el-button class='bg_btn' size="mini" id="bt1">日报</el-button>
        <el-button class='bg_btn' size="mini" id="bt2" style="background: linear-gradient(#FFCD48, 50%, #FE861E)">月报</el-button>
        <el-button class='bg_btn' size="mini" id="bt3">年报</el-button>
      </div> -->
      <el-radio-group v-model="tabModel" class="tabButton">
        <el-radio-button label="chart1">日报</el-radio-button>
        <el-radio-button label="chart2">月报</el-radio-button>
        <el-radio-button label="chart3">年报</el-radio-button>
      </el-radio-group>
      <div>单位:tCO₂e</div>
    </div>
    <div :class='className' id='chart1' :style='{height:height,width:width}' v-show="tabModel == 'chart1'"></div>
    <div :class='className' id='chart2' :style='{height:height,width:width}' v-show="tabModel == 'chart2'"></div>
    <div :class='className' id='chart3' :style='{height:height,width:width}' v-show="tabModel == 'chart3'"></div>

  </div>
</template>
<script>
import * as echarts from 'echarts'

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '88%'
    }
  },
  data () {
    return {
      tabModel: "chart2",
      chart: null,
      dataDay: [],
      dataDayA: [],
      lastMonthDate: [],
      lastMonthDateList: [],
      thisMonthDate: [],
      thisMonthDateList: []
    }
  },
  created () {
  },
  mounted () {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy () {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  watch: {
    dataDay: {
      // 自动触发，只能使用handler
      handler (newVal, oldVal) {
        this.dataDayA = newVal
      },
      // true表示侦听器立即触发（immediate默认为false）
      immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    lastMonthDateList: {
      // 自动触发，只能使用handler
      handler (newVal, oldVal) {
        this.lastMonthDate = newVal
      },
      // true表示侦听器立即触发（immediate默认为false）
      immediate: true,
      // // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    thisMonthDateList: {
      // 自动触发，只能使用handler
      handler (newVal, oldVal) {
        this.thisMonthDate = newVal
      },
      // true表示侦听器立即触发（immediate默认为false）
      immediate: true
      // // true表示深度侦听，侦听对象属性的变化
      // deep:true
    }
  },
  methods: {
    dayData () {
      document.getElementById('bt1').style.background = '#ffb13c'
      document.getElementById('bt2').style.background = '#0e90df'
      document.getElementById('bt3').style.background = '#0e90df'
      const dataDay = ['1日', '2日', '3日', '4日', '5日', '6日', '7日', '8日', '9日', '10日', '11日', '12日', '13日', '14日', '15日', '16日', '17日', '18日', '19日', '20日', '21日', '22日', '23日', '24日', '25日', '26日', '27日', '28日', '29日', '30日']
      const lastMonthDate = [30, 28, 31, 34, 32, 35, 38, 37, 30, 38, 48, 40, 30, 28, 31, 34, 32, 35, 38, 37, 30, 38, 48, 40, 40, 32, 28, 35, 34, 32]
      const thisMonthDate = [28, 25, 28, 34, 33, 38, 48, 40, 30, 32, 38, 37, 28, 25, 28, 34, 33, 38, 48, 40, 30, 32, 38, 37, 30, 32, 38, 35, 28, 25]
      this.setOption(dataDay, thisMonthDate, lastMonthDate)
    },
    monthData () {
      document.getElementById('bt2').style.background = '#ffb13c'
      document.getElementById('bt1').style.background = '#0e90df'
      document.getElementById('bt3').style.background = '#0e90df'
      const dataDay = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      const lastMonthDate = [30, 35, 29, 31, 38, 36, 28, 32, 35, 33, 37, 42]
      const thisMonthDate = [40, 47, 37, 31, 28, 36, 40, 44, 47, 45, 37, 33]
      this.setOption(dataDay, thisMonthDate, lastMonthDate)
    },
    yearData () {
      document.getElementById('bt3').style.background = '#ffb13c'
      document.getElementById('bt2').style.background = '#0e90df'
      document.getElementById('bt1').style.background = '#0e90df'
      const dataDay = ['2015年', '2016年', '2017年', '2018年', '2019年', '2020年', '2021年', '2022年', '2023年', '2024年']
      const lastMonthDate = [4000, 3800, 4800, 3400, 3800, 2800, 3500, 3800, 3700, 3000]
      const thisMonthDate = [3500, 3800, 3700, 3000, 3800, 3200, 4000, 3800, 4800, 4000]
      this.setOption(dataDay, thisMonthDate, lastMonthDate)
    },

    initChart () {
      const dataDay = ['1日', '2日', '3日', '4日', '5日', '6日', '7日', '8日', '9日', '10日', '11日', '12日']
      const lastMonthDate = [30, 35, 29, 31, 38, 36, 28, 32, 35, 33, 37, 42]
      const thisMonthDate = [40, 47, 37, 31, 28, 36, 40, 44, 47, 45, 37, 33]
      this.chart = echarts.init(document.getElementById(this.tabModel))
      this.setOption(dataDay, thisMonthDate, lastMonthDate)
    },
    setOption (dataDay, lastMonthDate, thisMonthDate) {
      this.chart.setOption(
        {
          color: ['rgba(24, 144, 255, 1)'],
          tooltip: {
            trigger: "item",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 10,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "shadow",
              shadowStyle: {
                fontSize: 10,
                color: "rgba(0, 11, 34, 0)",
              },
            }
          },
          // dataZoom: [
          //   {
          //     moveOnMouseMove: true,
          //     type: 'slider',
          //     show: false,
          //     width: 100,
          //     fillerColor: '#269cdb',
          //     borderRadius: 5,
          //     xAxisIndex: [0],
          //     start: 0,
          //     end: 50 // 初始化滚动条
          //   },
          //   {
          //     type: 'inside',
          //     xAxisIndex: 0,
          //     zoomOnMouseWheel: false, // 滚轮是否触发缩放
          //     moveOnMouseMove: true, // 鼠标滚轮触发滚动
          //     moveOnMouseWheel: true
          //   }
          // ],
          title: {
            text: '',
            right: 8,
            top: 2,
            textStyle: {
              fontSize: 10,
              color: '#BCCEF8',
              fontWeight: 200
            }
          },
          grid: {
            top: '18%',
            left: '4%',
            right: '4%',
            bottom: '2%',
            containLabel: true
          },
          legend: {
            show: true,
            x: '55%',
            itemHeight: 9,
            itemWidth: 20,
            data: ['碳排放', '碳减排'],
            textStyle: { // 图例文字的样式
              color: '#9ba0bc',
              fontSize: 12
            }
          },
          xAxis: {
            type: 'category',
            splitNumber: 1,
            axisLine: {
              lineStyle: {
                type: 'solid',
                color: '#11395D', // x坐标轴线的颜色
                width: '1' // 坐标线的宽度
              }
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              // interval: 0, // 设置刻度 0表示显示所有
              textStyle: {
                fontsize: 12,
                color: '#9ba0bc',
              }
              // interval:'auto',
            },
            data: dataDay
          },
          yAxis: {
            type: 'value',
            // interval:'auto',
            axisLabel: {
              textStyle: {
                fontsize: 12,
                color: '#9ba0bc',
              }
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#11395D',
                type: 'dashed'
              }
            }
          },
          series: [
            {
              name: '碳减排',
              data: thisMonthDate,
              type: 'line',
              smooth: true,
              showSymbol: false,
              itemStyle: {
                normal: {
                  color: '#3BABFE',
                  lineStyle: {
                    color: '#3BABFE',
                    width: 1
                  },
                  areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                      offset: 0,
                      color: 'rgba(7,44,90,0.3)'
                    }, {
                      offset: 1,
                      color: '#3BABFE'
                    }])
                  }
                }
              },
              lineStyle: {
                normal: {
                  width: 2
                }
              },
              symbol: 'circle',
              markPoint: {
                label: {
                  show: false
                },
                symbol: 'circle',
                symbolSize: 7,
                itemStyle: {
                  normal: {
                    color: '#52B5BE',
                    borderColor: '#fff',
                    borderWidth: 1
                  }
                },
                symbolOffset: [0, 0],
                data: [{
                  xAxis: '6日',
                  yAxis: '36'
                }]
              }
            },
            {
              name: '碳排放',
              data: lastMonthDate,
              type: 'line',
              smooth: true,
              showSymbol: false,
              symbol: 'circle',
              itemStyle: {
                normal: {
                  color: 'rgba(237,165,69,1)',
                  lineStyle: {
                    color: 'rgba(237,165,69,1)',
                    width: 1
                  },
                  areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                      offset: 0,
                      color: 'rgba(7,44,90,0.3)'
                    }, {
                      offset: 1,
                      color: 'rgba(237,165,69,1)'
                    }])
                  }
                }
              },
              lineStyle: {
                normal: {
                  width: 2
                }
              },
              markPoint: {
                label: {
                  show: false
                },
                symbol: 'circle',
                itemStyle: {
                  normal: {
                    color: 'rgba(237,165,69,1)',
                    borderColor: '#fff',
                    borderWidth: 1
                  }
                },
                symbolSize: 7,
                symbolOffset: [0, 0],
                data: [{
                  xAxis: '4日',
                  yAxis: '31'
                }, {
                  xAxis: '11日',
                  yAxis: '37'
                }]
              }
            }
          ]
        }
      )
    }
  }
}
</script>

<style scoped lang="scss">
.bg_btn {
  width: 60px;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  background: linear-gradient(#1EACE8, 50%, #0074D4);
  color: white;
  border: none;
  border-radius: 1px;
}

.el-button:focus,
.el-button:hover {
  background:  linear-gradient(to bottom,#1eace8, #0074d4);
  color: white;
}
div {
  color: #9ba0bc;
  font-size: 12px;
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
