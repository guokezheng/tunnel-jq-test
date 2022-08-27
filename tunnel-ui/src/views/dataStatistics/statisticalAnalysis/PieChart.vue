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
        title: {
                 text: '设备能源占比',
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
                right: '30',
                top: '20',
                data: ['照明', '风机', '情报板', '信号灯', '卷帘门', '其他']
            },
            series: [
                {
                    name: '',
                    type: 'pie',
                    selectedMode: 'single',
                    radius: [0, '30%'],
                    label: {
                        position: 'inner'
                    },
                    labelLine: {
                        show: false
                    }
                },
                {
                    name: '',
                    type: 'pie',
                    radius: ['40%', '55%'],
                    label: {
                                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                                    rich: {
                                        a: {
                                            color: '#999',
                                            lineHeight: 22,
                                            align: 'center'
                                        },
                                        hr: {
                                            /* borderColor: '#aaa', */
                                            width: '100%',
                                            borderWidth: 0.5,
                                            height: 0
                                        },
                                        b: {
                                            fontSize: 16,
                                            lineHeight: 33
                                        },
                                        per: {
                                            color: '#eee',
                                            backgroundColor: '#334455',
                                            padding: [2, 4],
                                            borderRadius: 2
                                        }
                                    }
                                },
                    data: [
                        {value: 335, name: '照明', selected: true},
                        {value: 310, name: '风机', selected: true},
                        {value: 234, name: '情报板', selected: true},
                        {value: 135, name: '信号灯', selected: true},
                        {value: 104, name: '卷帘门', selected: true},
                        {value: 102, name: '其他', selected: true}
                    ]
                }
            ]
      })
    }
  }
}
</script>
