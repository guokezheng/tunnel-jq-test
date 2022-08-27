<template>
  <div class="tunnelRanking-container">
    <div class="contentTitle">
      隧道能耗
      <i>energy consumption</i>
    </div>
    <div ref="echartsBox" class="scroll-Box" id="tunnelRanking">
      <!-- <div class="listHeader">
        <el-row type="flex" style="font-size:0.8vw;">
          <el-col style="width:13vw;padding-left:1vw;">排名</el-col>
          <el-col>隧道</el-col>
          <el-col>能耗<span style="font-size:0.6vw;">(Kwh/年)</span></el-col>
        </el-row>
      </div>
      <vue-seamless-scroll :class-option="defaultOption" class="listContent" :data="listData">
        <el-row
          type="flex"
          v-for="(item, index) in listData"
          :key="index"
          :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}"
        >
          <el-col style="width:12vw;padding-left:1.3vw;">{{item.id+1}}</el-col>
          <el-col>{{item.name}}</el-col>
          <el-col>{{item.energyConsumption}}</el-col>
        </el-row>
      </vue-seamless-scroll> -->
    </div>
  </div>
</template>

<script>
import *as echarts from 'echarts'
import elementResizeDetectorMaker from 'element-resize-detector'
export default {
    
  data() {
    return {
      listData: [
        {
          id: 0,
          name: "姚家峪隧道",
          energyConsumption: 19431.48
        },
        {
          id: 1,
          name: "毓秀山隧道",
          energyConsumption: 18483.24
        },
        {
          id: 2,
          name: "洪河隧道",
          energyConsumption: 18374.28
        },
        {
          id: 3,
          name: "滨莱高速",
          energyConsumption: 17492.42
        },
        {
          id: 4,
          name: "望海石隧道",
          energyConsumption: 16232.12
        },
        {
          id: 5,
          name: "中庄隧道",
          energyConsumption: 15837.83
        },
        {
          id: 6,
          name: "马公祠隧道",
          energyConsumption: 14827.32
        },
        {
          id: 7,
          name: "乐疃隧道",
          energyConsumption: 14758.23
        },
        {
          id: 8,
          name: "樵岭前隧道",
          energyConsumption: 14539.75
        },
        {
          id: 9,
          name: "佛羊岭隧道",
          energyConsumption: 14348.75
        },
        {
          id: 10,
          name: "迎春坡隧道",
          energyConsumption: 14102.32
        },
        {
          id: 11,
          name: "龙山寨隧道",
          energyConsumption: 13975.74
        }
      ]
    };
  },
  // computed: {
  //   defaultOption() {
  //     return {
  //       step: 0.2, // 数值越大速度滚动越快
  //       limitMoveNum: this.listData.length, // 开始无缝滚动的数据量 this.dataList.length
  //       hoverStop: true, // 是否开启鼠标悬停stop
  //       direction: 1, // 0向下 1向上 2向左 3向右
  //       openWatch: true, // 开启数据实时监控刷新dom
  //       singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
  //       singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
  //       waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
  //     };
  //   }
  // },
  mounted(){
    this.tunnelRanking()
    this.watchSize()
  },
  methods:{
    watchSize() {
			let that = this;
			let erd = elementResizeDetectorMaker()
			let Dom = that.$refs.echartsBox;//拿dom元素
			//监听盒子的变化
			erd.listenTo(Dom, function (element) {
					let myChart = echarts.init(Dom);
					myChart.resize();//echarts自带的方法可以使图表重新加载
			})
    },
      tunnelRanking(){
          var tunnelRanking = echarts.init(document.getElementById('tunnelRanking'));
          var option = {
              // backgroundColor: '#000',
              tooltip: {
                  trigger: 'axis',
                  axisPointer: {
                      type: 'shadow'
                  },
                  formatter: function(item) {
                      return item[0].axisValueLabel + '<br />' + item[0].seriesName + ': ' + item[0].data
                  }
              },
              grid: {
                  top: 50,
                  right: 20,
                  left: 50,
                  bottom: 80
              },
              xAxis: [{
                      data: ['姚家峪隧道', '毓秀山隧道', '洪河隧道', '滨莱高速', '望海石隧道', '中庄隧道', '马公祠隧道','乐疃隧道','樵岭前隧道','佛羊岭隧道','迎春坡隧道','龙山寨隧道'],
                      axisLine: {
                          lineStyle: {
                              color: '#FFFFFF'
                          }
                      },
                      axisTick: {
                          show: false
                      },
                      axisLabel: {
                          color: '#FFFFFF',
                          fontSize: 14,
                          interval: 0,
                          formatter: function(value) {
                                        return value.split('').join('\n')
                                      }
                      }
                  },
                  {
                      type: 'category',
                      show: true,
                      axisLine: {
                          show: false
                      },
                      axisTick: {
                          show: false
                      },
                      axisLabel: {
                          interval: 0,
                          margin: 20,
                          color: '#fff',
                          fontSize: '10',
                      },
                      data: [90,88,86,85,83,82,80,80,80,78,77,75],
                  }
              ],
              yAxis: [{
                  //scale:true,
                  // min: function(item){
                  // 	return parseInt(item.min / 10) * 10
                  // },
                  name:'kw-h           ',
                  max: 100,
                  splitLine: {
                      show: false
                  },
                  axisTick: {
                      show: false
                  },
                  axisLine: {
                      show: true,
                      lineStyle: {
                          color: '#FFFFFF'
                      }
                  },
                  axisLabel: {
                      textStyle: {
                          color: '#FFFFFF'
                      },
                      // formatter: function(value){
                      //   return parseInt(value * 100) + '%'
                      // },
                  },
              }],
              series: [{
                  name: '数量',
                  type: 'pictorialBar',
                  symbol: 'rect',
                  itemStyle: {
                      color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                                  offset: 0,
                                  color: '#6bf1fd'
                              }, {
                                  offset: 1,
                                  color: '#0084ff' 
                              }], false), // 渐变
                  },
                  symbolRepeat: true,
                  symbolSize: [10, 2],
                  // symbolBoundingData: 100,
                  symbolMargin: 1,
                  symbolPosition: 'start',
                  z: -20,
                  data: [90,88,86,85,83,82,80,80,80,78,77,75],
                  label: {
                      normal: {
                          show: false,
                          position: 'top',
                          verticalAlign: 'top'
                          // formatter: function(value){
                          //   return value && (value.data * 100).toFixed(1) + '%'
                          // }
                      }
                  }
              }, {
                  name: '',
                  type: 'bar',
                  barWidth: 15,
                  itemStyle: {
                      color: 'rgba(0,0,0,0)',
                      borderWidth: 2,
                      borderColor: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                                  offset: 0,
                                  color: '#51C5FD'
                              }, {
                                  offset: 1,
                                  color: '#005BB1' 
                              }], false), // 渐变
                      padding: 0
                  },
                  label: {
                      show: false
                  },
                  z: -10,
                  data: [100,100,100,100,100,100,100,100,100,100,100,100]
              }, {
                  name: '',
                  type: 'line',
                  // barWidth: 19,
                  symbol: 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAFCAYAAAB1j90SAAABS2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxMzggNzkuMTU5ODI0LCAyMDE2LzA5LzE0LTAxOjA5OjAxICAgICAgICAiPgogPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIi8+CiA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgo8P3hwYWNrZXQgZW5kPSJyIj8+IEmuOgAAADFJREFUGJVjFNj/hwEK/jMQBxgZGBgYWNAFiAUwjcTaBrcEppEk22A2kmobAwMDAwMA74EE3gW8aacAAAAASUVORK5CYII=',
                  symbolSize: [14, 6],
                  symbolOffset: [0, '-70%'],
                  itemStyle: {
                      color: 'rgba(0,0,0,0)',
                      borderWidth: 2,
                      borderColor: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                                  offset: 0,
                                  color: '#51C5FD'
                              }, {
                                  offset: 1,
                                  color: '#005BB1' 
                              }], false), // 渐变
                      padding: 0
                  },
                  hoverAnimation: false,
                  legendHoverLink: false,
                  z: -10,
                  data: [200, 200, 200, 200, 200, 200, 200]
              }]
          };
          // 使用刚指定的配置项和数据显示图表。
          tunnelRanking.setOption(option);
      }
  }
};
</script>

<style lang="less" scoped>
.tunnelRanking-container {
  height: 100%;
  padding: 0.1px;
  font-size: 0.8vw;
  .scroll-Box {
    // padding: 0 1vw 1vw;
    overflow: hidden;
    width: 100%;
    height: calc(100% - 2vw);

    .box-title {
      display: flex;
      justify-content: start;
      padding-left: 1vw;
      > div {
        margin-bottom: 0.6vw;
      }
      > div:first-child {
        width: 24%;
      }
      >div:nth-child(2) {
        width: 40%;
      }
      >div:last-child {
        width: 36%;
      }
    }

    /deep/ .el-scrollbar {
      height: 100%;
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
      .el-scrollbar__thumb {
        background-color: #027dec;
      }
    }
  }
}
</style>
