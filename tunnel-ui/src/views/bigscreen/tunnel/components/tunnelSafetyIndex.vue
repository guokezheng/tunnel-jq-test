<template>
  <div class="tunnelSafetyIndex-container">
    <div class="contentTitle">
      隧道安全指数
      <i>safety index</i>
    </div>
    <div ref="echartsBox" class="scroll-Box" id="tunnelSafetyIndex">
      <!-- <div class="listHeader">
        <el-row type="flex" style="font-size:0.8vw;">
          <el-col style="width:13vw;padding-left:1vw;">排名</el-col>
          <el-col>隧道</el-col>
          <el-col>安全系数</el-col>
        </el-row>
      </div>
      <vue-seamless-scroll :class-option="defaultOption" class="listContent" :data="listData">
        <el-row
          type="flex"
          v-for="(item, index) in listData"
          :key="index"
          :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}"
        >
          <el-col style="width:12vw;padding-left:1.3vw;">
            <div
              class="box-id"
              :class="item.id==0?'box-id-one':item.id==1?'box-id-two':item.id==2?'box-id-three':''"
            >
              <span>{{item.id+1}}</span>
            </div>
          </el-col>
          <el-col>{{item.name}}</el-col>
          <el-col>{{item.safetyIndex}}</el-col>
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
          safetyIndex: "90"
        },
        {
          id: 1,
          name: "毓秀山隧道",
          safetyIndex: "88"
        },
        {
          id: 2,
          name: "洪河隧道",
          safetyIndex: "86"
        },
        {
          id: 3,
          name: "滨莱高速",
          safetyIndex: "85"
        },
        {
          id: 4,
          name: "望海石隧道",
          safetyIndex: "83"
        },
        {
          id: 5,
          name: "中庄隧道",
          safetyIndex: "82"
        },
        {
          id: 6,
          name: "马公祠隧道",
          safetyIndex: "80"
        },
        {
          id: 7,
          name: "乐疃隧道",
          safetyIndex: "80"
        },
        {
          id: 8,
          name: "樵岭前隧道",
          safetyIndex: "80"
        },
        {
          id: 9,
          name: "佛羊岭隧道",
          safetyIndex: "78"
        },
        {
          id: 10,
          name: "迎春坡隧道",
          safetyIndex: "77"
        },
        {
          id: 11,
          name: "龙山寨隧道",
          safetyIndex: "75"
        }
      ]
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: this.listData.length, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      };
    }
  },
  mounted() {
      this.tunnelSafetyIndex()
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
      tunnelSafetyIndex(){
          var tunnelSafetyIndex = echarts.init(document.getElementById('tunnelSafetyIndex'));
          
              var areaList = ['姚家峪隧道', '毓秀山隧道', '洪河隧道', '滨莱高速', '望海石隧道','中庄隧道','马公祠隧道', '乐疃隧道','樵岭前隧道','佛羊岭隧道','迎春坡隧道','龙山寨隧道'];
              var safetyArr =  [90,88,86,85,83,82,80,80,80,78,77,75];
              
              var option = {
                  dataZoom:[
                  	{		
                  	        type: 'slider',
                  	        show: true,
                  			realtime : true, //拖动时，是否实时更新系列的视图
                  			showDetail:false, //拖动时 不显示详细数值信息
                  	        yAxisIndex: [0],
                  	        left: '90%',
                  	        start: 1, //数据窗口范围的起始百分比
                  	        end: 60 //初始化时，滑动条宽度结束标度<br> 
                  	    },
                  	    {	
                  	        type: 'inside',
                  	        yAxisIndex: [0],
                  	        start: 1,
                  	        end: 20
                  	    },
                  ],
                  // backgroundColor: '#00043A',
                  tooltip: {
                      trigger: 'axis',
                      show: true,
                      axisPointer: { // 坐标轴指示器，坐标轴触发有效
                          type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                      },
                      formatter: "{b}<br>安全指数： {c} ",
                  },
                  legend: {
                      show:false
                  },
                  grid: {
                      left: '5%',
                      right: '14%',
                      bottom: '0%',
                      top:'5%',
                      containLabel: true
                  },
                  xAxis: [{
                      splitLine: {
                          show: false
                      },
                      type: 'value',
                      show: false,
                  }],
                  yAxis: [{
                      splitLine: {
                          show: false
                      },
                      axisLine: { //y轴
                          show: false
                      },
                      type: 'category',
                      axisTick: {
                          show: false
                      },
                      inverse: true,
                      data: areaList,
                      axisLabel: {
                          color: '#FFFFFF',
                          fontSize: 14,
                      }
                  }],
                  series: [{
                      name: '标准化',
                      type: 'bar',
                      barWidth: 12, // 柱子宽度
                      label: {
                          show: true,
                          position: 'right', // 位置
                          color: '#FFFFFF',
                          fontSize: 14,
                          distance: 15, // 距离
              			formatter: '{c}' // 这里是数据展示的时候显示的数据
                      }, // 柱子上方的数值
                      itemStyle: {
                          barBorderRadius: [0, 20, 20, 0], // 圆角（左上、右上、右下、左下）
                          
                              color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                                  offset: 0,
                                  color: '#51C5FD'
                              }, {
                                  offset: 1,
                                  color: '#005BB1' 
                              }], false), // 渐变
                      },
                      data:safetyArr
                  }, ]
              };
              
          
             // 使用刚指定的配置项和数据显示图表。
             tunnelSafetyIndex.setOption(option);
      }
  }
}
</script>

<style lang="less" scoped>
.tunnelSafetyIndex-container {
  height: 100%;
  padding: 0.1px;
  font-size: 0.8vw;
  .scroll-Box {
    overflow: hidden;
    width: 100%;
    height: calc(100% - 2vw);
    .listContent {
      .el-row {
        .el-col {
          display: flex;
          align-items: center;
        }
      }
    }
    .box-id {
      width: 1vw;
      height: 1vw;
      border: 0.1vw solid #fff;
      border-radius: 0.5vw;
      background-color: gainsboro;
      color: black;
      text-align: center;
      font-size: 0.5vw;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .box-id-one {
      background-color: red;
      color: white;
    }
    .box-id-two {
      background-color: orange;
      color: white;
    }
    .box-id-three {
      background-color: blue;
      color: white;
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
