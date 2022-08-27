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
    setOptions(chartData) {
      var xData = [];
      var yData = [];
      for(var i=0;i<this.chartData.length;i++){
        xData.push(this.chartData[i].warningTime);
        yData.push(this.chartData[i].warningCount);
      }
      this.chart.setOption({
        title: {
                 text: '最近7天报警情况',
                 left: 'center',
                 top: 2,
                 textStyle:{
                   color:'#000'
                 }
               },
        xAxis: {
          data: xData ,
          boundaryGap: false,
          axisTick: {
            show: false
          },
          axisLabel: {
            interval:0,
            rotate:70
          }
          /* splitNumber: 7 */
        },
        grid: {
          left: 5,
          right: 5,
          bottom: 30,
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
            show: false
          },
          minInterval: 1, //设置成1保证坐标轴分割刻度显示成整数。
        },
        legend: {
          data: ['报警'],
          right: '10',
          top: '10'
        },
        series: [{
          name: '报警', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          /* data: expectedData, */
          data: yData ,
          animationDuration: 28,
          animationEasing: 'cubicInOut'
        }]
      })
    }
  }
}
</script>
