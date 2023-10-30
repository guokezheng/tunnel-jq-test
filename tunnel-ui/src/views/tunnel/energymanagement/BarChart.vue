<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../../dashboard/mixins/resize'
export default {
  mixins: [resize],
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
      default: '460px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ expectedData, actualData } = {}) {
      this.chart.setOption({
        xAxis: {
          /* data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], */
          data: ['1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00','8:00', '9:00', '10:00', '11:00', '12:00', '13:00','14:00', '15:00', '16:00', '17:00', '18:00', '19:00','20:00', '21:00', '22:00', '23:00', '24:00'],
          boundaryGap: true,
          axisTick: {
            show: false
          },
          axisLine:{
            lineStyle:{
              color:'#000'
            }
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
         /* height:80, */
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          },
          name: 'kW·h',
        },
        legend: {
          data: ['今日', '昨日'],
          x: 'right', // 'center' | 'left' | {number},
        },
        series: [{
          name: '今日', itemStyle: {
            normal: {
              color: '#16d20c',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          redius:'8%',
          smooth: true,
          type: 'bar',
          /* data: expectedData, */
          data: [100, 120, 161, 134, 105, 160, 165,100, 120, 161, 134, 105, 160, 100, 120, 161, 134, 105, 160, 100, 120, 161],
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '昨日',
          smooth: true,
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          /* data: actualData, */
           data: [120, 82, 91, 154, 162, 140, 145,120, 82, 91, 154, 162, 140, 145,120, 82, 91, 154, 162, 140, 145,120, 82, 91],
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
