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
      default: '300px'
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
          data: ['1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00'],
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
          name: '/个',
        },
        legend: {
          data: ['车流量'],
          x: 'right', // 'center' | 'left' | {number},
        },
        series: [{
          name: '车流量', itemStyle: {
            normal: {
              color: '#16d20c',
              lineStyle: {
                color: '#ffff00',
                width: 2
              }
            }
          },
          redius:'8%',
          smooth: true,
          type: 'line',
          /* data: expectedData, */
          data: [8, 12, 11, 14, 10, 10, 16],
          animationDuration: 20,
          animationEasing: 'cubicInOut'
        }]
      })
    }
  }
}
</script>
