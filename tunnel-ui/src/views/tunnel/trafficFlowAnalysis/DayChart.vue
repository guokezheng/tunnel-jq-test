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
    dayData: {
      type: null
    }
  },
  watch: {
    dayData: {
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
      var myData = this.dayData;
      // X轴
      var xData = [];
      // Y轴
      var yData = [];
      if(myData.length == 0){
          xData = ['测试数据1','测试数据2','测试数据3','测试数据4']
          yData = [0,0,0,0]
      }else{
        for(var i=0;i<myData.length;i++){
          xData.push(myData[i].date);
          yData.push(Number(myData[i].byVehicelNum) / 2);
        }
      }
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'cross' // 默认为直线，可选为：'line' | 'shadow'
          },
          textStyle: {
            color: '#000'
          },
          backgroundColor: 'rgba(255,255,255,1)',
        },
        grid: {
          top: '30',
          left: '25',
          right: '50',
          bottom: '20',
          containLabel: true
        },
        xAxis: [{
          data: xData,
          boundaryGap: false,
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
        }],
        yAxis: [{
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
          name:"辆"
        }],
        series: [{
          name: '车流量',
          type: 'line',
          data: yData,
          itemStyle:{
            normal: {
              color: '#b30000',
              lineStyle: {
                color: '#b30000'
              },
            }
          },
          areaStyle: {   //添加背景颜色
            type: "default",
            color: "#0055ff",  //背景颜色
            opacity: 0.3,   //透明度
          },
        }]
      })
    }
  }
}
</script>
