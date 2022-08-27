<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../../dashboard/mixins/resize'
/* import { listWarningInfo, getWarningInfo, delWarningInfo, addWarningInfo, updateWarningInfo,fireAlarmCount } from "@/api/event/warningInfo"; */
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
      //深度监听，可监听到对象、数组的变化
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
      var processDay = chartData.process;
      var unProcessDay = chartData.unProcess;
      var fourPieData = [
          {
              name: '今日已处理',
              max:10,
              value: processDay
          },{
              name: '今日未处理',
               max:10,
              value: unProcessDay
          },
          {
              name: '今日报警量',
               max:10,
              value: processDay + unProcessDay
          }]
          var titleArr= [], seriesArr=[];
          var colors=[['#57bbf7', '#bbb'],['#ffc969', '#bbb'],['#f38b97', '#bbb'], ['#b1d882', '#f4f4f7'],['#c0acf9', '#f4f4f7']]
          fourPieData.forEach(function(item, index){
              titleArr.push(
                  {
                      text:item.name,
                      left:index * 25 + 25 +'%',
                      top: "2%",
                      textAlign: 'center',
                      textStyle: {
                          fontWeight: 'normal',
                          fontSize: '16',
                          color: colors[index][0],
                          textAlign: 'center',
                      },
                  }
              );
              seriesArr.push(
                  {
                      name: item.name,
                      type: 'pie',
                      clockWise: false,
                      radius: [60, 70],
                      itemStyle:  {
                          normal: {
                              color: colors[index][0],
                              shadowColor: colors[index][0],
                              shadowBlur: 0,
                              label: {
                                  show: false
                              },
                              labelLine: {
                                  show: false
                              },
                          }
                      },
                      hoverAnimation: false,
                      center: [index * 25 + 25 +'%', "50%"],
                      data: [{
                          value: item.value,
                          label: {
                              normal: {
                                  formatter: function(params){
                                      var unit = unit ="次"
                                      return params.value + unit;
                                  },
                                  position: 'center',
                                  show: true,
                                  textStyle: {
                                      fontSize: '20',
                                      fontWeight: 'bold',
                                      color: colors[index][0]  //跟随其他颜色
                                  }
                              }
                          },
                      }, {
                          value: item.max-item.value,
                          name: 'invisible',
                          itemStyle: {
                              normal: {
                                  color: colors[index][1]
                              },
                              emphasis: {
                                  color: colors[index][1]
                              }
                          }
                      }]
                  }
              )
          });
      this.chart.setOption({
        title:titleArr,
        series: seriesArr
      })
    }
  }
}
</script>
