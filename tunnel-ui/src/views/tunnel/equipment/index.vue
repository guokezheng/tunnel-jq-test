<template>
  <div :class="className" style="top: 50px;" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
/* import resize from './mixins/resize' */
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
    }
  },
  data() {
    return {
      chart: null
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

      this.chart.setOption({
        /* title: {
                  text: '设备信息统计',
                  right: '500',
                  textStyle:{
                    color:'#000000'
                  }
                }, */
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          /* left: 'right', */
          bottom: '200',
          right: '200',
          top: '20',
          data: ['卷帘门', '风机', '情报板', '水泵', '传感器','照明','信号灯','车指']
        },
        series: [
          {
            name: '设备信息统计',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: 320, name: '卷帘门' },
              { value: 240, name: '风机' },
              { value: 149, name: '情报板' },
              { value: 100, name: '水泵' },
              { value: 59, name: '传感器' },
              { value: 100, name: '照明' },
              { value: 120, name: '信号灯' },
              { value: 69, name: '车指' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    }
  }
}
</script>
