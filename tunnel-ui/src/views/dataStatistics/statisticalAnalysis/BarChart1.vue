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
      default: '180px'
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
                  text: '',
                  left: 'center',
                  /* top: 2, */
                  textStyle:{
                    color:'#000'
                  }
                },
        xAxis: {
          /* data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], */
          data: ['0：00', '1：00', '2：00', '3：00', '4：00', '5：00', '6：00','7：00', '8：00', '9：00', '10：00', '11：00', '12：00', '13：00','14：00', '15：00', '16：00', '17：00', '18：00', '19：00', '20：00','21：00', '22：00', '23：00', '24：00'],
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
          bottom: 10,
          /* top: 30, */
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
          }/* ,
          name: 'kw-h', */
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
          data: [0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0,0, 0, 0],
          animationDuration: 180,
          animationEasing: 'cubicInOut'
        }]
      })
    }
  }
}
</script>
