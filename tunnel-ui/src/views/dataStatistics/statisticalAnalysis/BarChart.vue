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
      type: null
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
         title: {
                  text: '能源使用量',
                  left: 'center',
                  top: 2,
                  textStyle:{
                    color:'#000'
                  }
                },
        xAxis: {
          /* data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], */
          data: ['1号', '2号', '3号', '4号', '5号', '6号', '7号'],
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
            show: true
          },
          name: 'kw-h',
        },
        legend: {
          data: [''],
          x: 'right', // 'center' | 'left' | {number},
        },
        series: [{
          name: '今日', itemStyle: {
            normal: {
              color: '#55aaff',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
           // barWidth : 30,//柱图宽度
          barMaxWidth:30,//最大宽度
          smooth: true,
          type: 'bar',
          /* data: expectedData, */
          data: [10, 12, 16, 13, 15, 16, 16],
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        }]
      })
    }
  }
}
</script>
