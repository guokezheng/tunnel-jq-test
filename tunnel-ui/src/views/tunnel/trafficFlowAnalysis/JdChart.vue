<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
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
      default: '280px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    jdData: {
      type: null
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    jdData: {
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
      this.setOptions()
    },
    setOptions() {
      var myData = this.jdData;
      // X轴
      var xData = [];
      // Y轴
      var yData = [];
      if(myData.length == 0){
          xData = ['第一季度','第二季度','第三季度','第四季度']
          yData = [0,0,0,0]
      }else{
        for(var i=0;i<myData.length;i++){
          xData.push(myData[i].date + "季度");
          yData.push(Number(myData[i].byVehicelNum));
        }
      }

      this.chart.setOption({
        xAxis: {
          data: xData,
          boundaryGap: true,
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#838383'
            }
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: '#838383'
            }
          },
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
          textStyle: {
            color: '#000'
          },
          backgroundColor: 'rgba(255,255,255,1)',
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: true
          },
          name: '辆',
          axisLabel: {
            show: true,
            textStyle: {
              color: '#838383'
            }
          },
          axisLine: {
            lineStyle: {
              color: '#838383'
            }
          },
        },
        legend: {
          data: [''],
          x: 'right',
        },
        series: [{
          name: '今日', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          barMaxWidth:30,
          smooth: true,
          type: 'bar',
          data: yData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut',
          itemStyle: {
            normal: {
              label: {
                show: true,  //开启显示
                position: 'top',  //在上方显示
                textStyle: {  //数值样式
                  color: 'black',
                  fontSize: 14
                }
              }
            }
          },
        }]
      })
    }
  }
}
</script>
