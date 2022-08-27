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
      default: '200px'
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
  //使用watch时有一个特点，就是当值第一次绑定的时候，不会执行监听函数，只有值发生改变才会执行。
  //如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
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
      this.setOptions();
      },
      setOptions() {
       /* var homeAlarmTypeNumEcharts = this.chartData.homeAlarmTypeNumEcharts;
        var xData = [];
        var yData = [];
        for(var i=0;i<homeAlarmTypeNumEcharts.length;i++){
          xData.push(homeAlarmTypeNumEcharts[i].name);
          yData.push(homeAlarmTypeNumEcharts[i].value);
        } */
      this.chart.setOption({
        title: {
                 /* text: '设备类型', */
                 left: 'center',
                 top: 2,
                 textStyle:{
                   color:'#000'
                 }
               },
        tooltip: {
                trigger: 'item' ,
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                bottom: '200',
                right: '10',
                top: '20',
                data: 报警类型

            },
            series: [
                {
                    name: '',
                    type: 'pie',
                    roseType: 'radius',
                    radius: [0, '30%'],
                    data: [],
                    animationEasing: 'cubicInOut',
                    animationDuration: 2600,
                    label: {
                        position: 'inner'
                    },
                    labelLine: {
                        show: false
                    }
                }
            ]
      })
      }
    }
  }
</script>
