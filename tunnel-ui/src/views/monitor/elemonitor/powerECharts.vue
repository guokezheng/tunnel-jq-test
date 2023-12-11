<template>
  <div class="echarts" ref="echarts" style="width:100%;height:30vh"></div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'echartsEnvironment',
  data() {
    return {}
  },

  props: ['cityOptionsTable', 'riEChartsList', 'cityOptionsValue'],
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
    this.getInitFn()
  },
  // 侦听器
  watch: {
    cityOptionsTable: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        this.getInitFn()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    riEChartsList: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        this.getInitFn()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    cityOptionsValue: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        this.getInitFn()
      }
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    }
  },
  // 事件函数
  methods: {
    getInitFn() {
      const xItem = this.cityOptionsTable
      let aData = []
      let bData = []
      let cData = []
      let dData = []
      let aValue = []
      let bValue = []
      let cValue = []
      let dValue = []
      let aTime = []
      for (const key in this.riEChartsList) {
        if (this.cityOptionsValue[0] === key) {
          aData = this.riEChartsList[key]
        } else if (this.cityOptionsValue[1] === key) {
          bData = this.riEChartsList[key]
        } else if (this.cityOptionsValue[2] === key) {
          cData = this.riEChartsList[key]
        } else if (this.cityOptionsValue[3] === key) {
          dData = this.riEChartsList[key]
        }
      }
      if(aData.length > 0){
         aValue = aData.map((item)=>{
          return item.value
        })
         aTime = aData.map((item)=>{
          return item.time.slice(11)
        })
      }
      if(bData.length > 0){
         bValue = bData.map((item)=>{
          return item.value
        })
      }
      if(cData.length > 0){
         cValue = cData.map((item)=>{
          return item.value
        })
      }
      if(dData.length > 0){
         dValue = dData.map((item)=>{
          return item.value
        })
      }

      this.echartsInit(xItem, aValue,bValue,cValue,dValue,aTime)
    },
    echartsInit(xItem, aValue,bValue,cValue,dValue,aTime) {
      var chartDom = this.$refs.echarts
      var myChart = echarts.init(chartDom)
      var option
      option = {
        tooltip: {
          show: true,
          trigger: 'axis'
        },
        legend: {
          left: '75%',
          top: '0%',
          textStyle: {
            color: this.sideTheme == "theme-light" ? "#fff" : "#000",
            fontSize: 15
          },
          data: xItem
        },

        grid: {
          top: '15%',
          left: '7%',
          right: '3%',
          bottom: '10%'
          // containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            axisLine: {
              show: false,
              color: this.sideTheme == "theme-light" ? "#fff" : "#000"
            },

            axisLabel: {
              // interval:0, // x轴刻度间距
              color: this.sideTheme == "theme-light" ? "#fff" : "#000",
              width: 100
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#c4f0fc'
              }
            },
            boundaryGap: false,
            // data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'] //this.$moment(data.times).format("HH-mm") ,
            data: aTime //this.$moment(data.times).format("HH-mm") ,
          }
        ],

        yAxis: [
          {
            name: ' V',
            nameTextStyle: {
              color: this.sideTheme == "theme-light" ? "#fff" : "#000",
              fontSize: 15
            },
            type: 'value',
            min: 0,
            splitNumber: 10,
            splitLine: {
              show: true,
              lineStyle: {
                color: '#909399',
                opacity: 0.23
              }
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme == "theme-light" ? "#fff" : "#000"
              }
            },
            axisLabel: {
              show: true,
              margin: 10,
              textStyle: {
                color: this.sideTheme == "theme-light" ? "#fff" : "#000"
              }
            },
            axisTick: {
              show: false,
              lineStyle: {
                color: '#c4f0fc'
              }
            }
          }
        ],
        series: [
          {
            name: xItem[0],
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbol: 'none',
            symbolSize: 5,
            lineStyle: {
              normal: {
                color: 'rgba(243, 210, 135,1)'
              }
            },
            label: {
              show: false,
              position: 'top',
              textStyle: {
                color: 'rgba(243, 210, 135,1)',
                fontSize: 8
              }
            },
            itemStyle: {
              color: 'rgba(243, 210, 135,1)',
              borderColor: 'rgba(243, 210, 135,1)',
              borderWidth: 0
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(243, 210, 135,0.3)'
                    },
                    {
                      offset: 0,
                      color: 'rgba(243, 210, 135,0)'
                    }
                  ],
                  false
                )
              }
            },
            // data: [158.68, 177.84, 182.2, 97.89, 248.98, 176.83, 133.49, 292.09, 186.58, 184.02, 209.62, 203.4, 165.11, 201.31, 5, 65.81, 103.67, 170.34, 146.61, 129.17, 125.97, 205.37, 102.72, 39.04, 93.45, 108.8, 82.65, 131.96, 193.21, 34.54] //data.values
             data: aValue //data.values
          },
          {
            name: xItem[1],
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbol: 'none',
            symbolSize: 5,
            lineStyle: {
              normal: {
                color: 'rgba(74, 188, 149,1)'
              }
            },
            label: {
              show: false,
              position: 'top',
              textStyle: {
                color: 'rgba(74, 188, 149,1)',
                fontSize: 10
              }
            },
            itemStyle: {
              color: 'rgba(74, 188, 149,1)',
              borderColor: 'rgba(74, 188, 149,1)',
              borderWidth: 0
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(74, 188, 149,0.3)'
                    },
                    {
                      offset: 0,
                      color: 'rgba(74, 188, 149,0)'
                    }
                  ],
                  false
                )
              }
            },
            // data: [1004.08, 436.97, 342.37, 335.04, 259.28, 757.08, 66.14, 749.27, 486.26, 0, 0, 470.3, 811.31, 54.86, 819.06, 509.57, 105.31, 34.87, 129.04, 35.33, 0, 31.51, 227.09, 436.76, 488.59, 642.03, 626.7, 433.33, 324.09, 67.43] //data.values
            data: bValue //data.values
          },
          {
            name: xItem[2],
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbol: 'none',
            symbolSize: 5,
            lineStyle: {
              normal: {
                color: 'rgba(251, 118, 140,1)'
              }
            },
            label: {
              show: false,
              position: 'top',
              textStyle: {
                color: 'rgba(251, 118, 140,1)',
                fontSize: 10
              }
            },
            itemStyle: {
              color: 'rgba(251, 118, 140,1)',
              borderColor: 'rgba(251, 118, 140,1)',
              borderWidth: 0
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(251, 118, 140,0.3)'
                    },
                    {
                      offset: 0,
                      color: 'rgba(251, 118, 140,0)'
                    }
                  ],
                  false
                )
              }
            },
            // data: [104.08, 736.97, 842.37, 935.04, 559.28, 357.08, 466.14, 149.27, 286.26, 99, 399, 470.3, 811.31, 54.86, 819.06, 509.57, 105.31, 34.87, 129.04, 35.33, 0, 31.51, 227.09, 436.76, 488.59, 642.03, 626.7, 433.33, 324.09, 67.43] //data.values
            data: cValue //data.values
          },
          {
            name: xItem[3],
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbol: 'none',
            symbolSize: 5,
            lineStyle: {
              normal: {
                color: 'rgba(109, 148, 204,1)'
              }
            },
            label: {
              show: false,
              position: 'top',
              textStyle: {
                color: 'rgba(109, 148, 204,1)',
                fontSize: 10
              }
            },
            itemStyle: {
              color: 'rgba(109, 148, 204,1)',
              borderColor: 'rgba(109, 148, 204,1)',
              borderWidth: 0
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(109, 148, 204,0.3)'
                    },
                    {
                      offset: 0,
                      color: 'rgba(109, 148, 204,0)'
                    }
                  ],
                  false
                )
              }
            },
            // data: [194.08, 236.97, 342.37, 435.04, 859.28, 357.08, 966.14, 449.27, 286.26, 99, 399, 470.3, 811.31, 54.86, 819.06, 509.57, 105.31, 34.87, 129.04, 35.33, 0, 31.51, 227.09, 436.76, 488.59, 642.03, 626.7, 433.33, 324.09, 67.43] //data.values
            data: dValue //data.values
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
