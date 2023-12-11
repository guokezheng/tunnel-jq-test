<template>
  <!-- 温湿度传感器 -->
  <div class="echarts" ref="echarts" style="width:100%;height:27vh"></div>
</template>

<script>
import * as echarts from 'echarts'
import { getSelectEChartsApi } from '@/api/panoramicData/index.js'
export default {
  name: 'echartsEnvironment',
  data() {
    return {}
  },

  props: ['powerCode', 'WenShiDuDateValue', 'WenShiDuValue'],
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
    this.echartsInit()
  },
  // 侦听器
  watch: {
    powerCode: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // this.echartsInit()
        if (this.WenShiDuValue) {
          this.getInitECharts()
        }
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    WenShiDuDateValue: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // this.echartsInit()
        this.getInitECharts()
      }
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    },
    WenShiDuValue: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // this.echartsInit()
        this.getInitECharts()
      }
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    }
  },
  // 事件函数
  methods: {
    async getInitECharts() {
      if (this.powerCode || this.WenShiDuDateValue || this.WenShiDuValue) {
        let obj = {}
        obj.reportTime = this.WenShiDuDateValue.toLocaleString().slice(0, 10).replace(/\//g, '-').trim()
        obj.powerCode = this.powerCode
        obj.deviceCode = this.WenShiDuValue
        obj.dataItemKey = 'tem-hum'
        console.log(obj)
        const b = await getSelectEChartsApi(obj)
        if (b.data.length > 0) {
          const wenDu = b.data.map(item => {
            return item.tem
          })
          const shiDu = b.data.map(item => {
            return item.hum
          })
          const time = b.data.map(item => {
            return item.time.slice(11)
          })
          this.echartsInit(wenDu, shiDu, time)
        }
      }
    },
    echartsInit(wenDu, shiDu, time) {
      var chartDom = this.$refs.echarts
      var myChart = echarts.init(chartDom)
      var dataDay = time
      var lastMonthDate = shiDu
      var thisMonthDate = wenDu
      var option
      option = {
        color: ['rgba(24, 144, 255, 1)'],
        dataZoom: [
          {
            moveOnMouseMove: true,
            type: 'slider',
            show: false,
            width: 100,
            fillerColor: '#269cdb',
            borderRadius: 5,
            xAxisIndex: [0],
            start: 0,
            end: 100 //初始化滚动条
          },
          {
            type: 'inside',
            xAxisIndex: 0,
            zoomOnMouseWheel: false, //滚轮是否触发缩放
            moveOnMouseMove: true, //鼠标滚轮触发滚动
            moveOnMouseWheel: true
          }
        ],
        // title:{
        //   text:'kw·h',
        //   left:0,
        //   top:1,
        //   textStyle:{
        //     fontSize: 12,
        //     // fontFamily: 'PangMenZhengDao',
        //     color: '#BCCEF8',
        //     fontWeight:200
        //   }
        // },
        grid: {
          top: '18%',
          left: '3%',
          right: '4%',
          bottom: '2%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          show: true,
          x: '2%',
          data: ['温度℃', '湿度%'],
          textStyle: {
            //图例文字的样式
            color: this.sideTheme == 'theme-dark'?'#fff':'#000',
            fontSize: 12
          }
        },
        xAxis: {
          type: 'category',
          splitNumber: 1,
          axisLine: {
            lineStyle: {
              type: 'solid',
              color: this.sideTheme == 'theme-dark'?'#fff':'#000', //x坐标轴线的颜色
              width: '1' //坐标线的宽度
            }
          },
          axisLabel: {
            // interval: 0, // 设置刻度 0表示显示所有
            textStyle: {
              fontsize: '11px',
              color: this.sideTheme == 'theme-dark'?'#fff':'#000',
              fontWeight: 400
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
              fontsize: '13px',
              color: this.sideTheme == 'theme-dark'?'#fff':'#000',
              fontWeight: 400
            }
          },
          splitLine: {
            show: true, //y网格线
            lineStyle: {
              color: '#bbb'
            }
          }
        },
        series: [
          {
            name: '温度℃',
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
                }
                // areaStyle: {
                //   //color: '#94C9EC'
                //   color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                //     {
                //       offset: 0,
                //       color: 'rgba(7,44,90,0.3)'
                //     },
                //     {
                //       offset: 1,
                //       color: '#3BABFE'
                //     }
                //   ])
                // }
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
              symbolSize: 10,
              itemStyle: {
                normal: {
                  color: '#fff',
                  borderColor: '#52B5BE',
                  borderWidth: 2
                }
              },
              symbolOffset: [0, 0],
              data: [{ type: 'max', name: '最大需量' }]
            }
          },
          {
            name: '湿度%',
            data: lastMonthDate,
            type: 'line',
            smooth: true,
            showSymbol: false,
            itemStyle: {
              normal: {
                color: 'rgba(228, 106, 169,1)',
                lineStyle: {
                  color: 'rgba(228, 106, 169,1)',
                  width: 1
                }
                // areaStyle: {
                //   //color: '#94C9EC'
                //   color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
                //     {
                //       offset: 0,
                //       color: 'rgba(7,44,90,0.3)'
                //     },
                //     {
                //       offset: 1,
                //       color: 'rgba(228, 106, 169,1)'
                //     }
                //   ])
                // }
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
                  color: 'rgba(228, 106, 169,1)',
                  borderColor: '#fff',
                  borderWidth: 2
                }
              },
              symbolSize: 10,
              // symbolBackground:'#fff',
              symbolOffset: [0, 0],
              data: [{ type: 'max', name: '最大需量' }]
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
