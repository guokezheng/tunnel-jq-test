<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '../../dashboard/mixins/resize'

const animationDuration = 6000

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
    yearData: {
      type: null
    }
  },
  watch: {
    yearData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
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
      this.chart = echarts.init(this.$el, 'macarons');
      this.setOptions();
    },
    setOptions() {
      var myData = this.yearData;
      console.log(this.yearData,'this.yearData')
      
      // X轴
      var xData = [];
      // Y轴
      var yData = [];
      // 分开
      if(myData.length == 0){
          xData = ['测试数据1','测试数据2','测试数据3','测试数据4']
          yData = [0,0,0,0]
      }else{
        for(var i=0;i<myData.length;i++){
          xData.push(myData[i].date + "年");
          yData.push( Number(myData[i].byVehicelNum) / 2 );
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
