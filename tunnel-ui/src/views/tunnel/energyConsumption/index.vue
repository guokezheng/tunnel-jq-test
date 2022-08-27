<template>
  <div class="app-container">
      <div style="margin-bottom: 20px;">
          <b style="font-size: 16px;margin-right: 15px;">隧道名称</b>
          <el-button-group class="menu-button-group" v-for="(item, index) in tunnelData" >
              <el-button
                  type="info"
                  size="small"
                  :style="tunnelBtnStyle[index]"
                  @click="setTunnel(item, index)"
              >
                {{ item.tunnelName }}
              </el-button>
          </el-button-group>
      </div>
      <el-row type="flex">
        <el-col :span="12" style="margin:0 20px 20px 20px">
          <el-card class="phoneStatistics">
            <div class="title">
              <div class="preposition"></div>
              <span class="name">今日用电统计</span>
            </div>
            <div class="body">
              <div id="electricToday"></div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12" style="margin:0 20px 20px 20px">
          <el-card class="phoneStatistics">
            <div class="title">
              <div class="preposition"></div>
              <span class="name">本月用电统计</span>
            </div>
            <div class="body">
              <div  id="electricMonth"></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row type="flex">
        <el-col :span="9" style="margin:0 20px 20px 20px">
          <el-card class="phoneStatistics">
            <div class="title">
              <div class="preposition"></div>
              <span class="name">隧道用电排行</span>
            </div>
            <div class="body">
              <div id="electricRanking"></div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" style="margin:0 20px 20px 20px">
          <el-card class="phoneStatistics">
            <div class="title">
              <div class="preposition"></div>
              <span class="name">能耗占比</span>
            </div>
            <div class="body">
              <div id="proportionCharts"></div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="9" style="margin:0 20px 20px 20px">
          <el-card class="phoneStatistics">
            <div class="title">
              <div class="preposition"></div>
              <span class="name">隧道能耗排行</span>
            </div>
            <div class="body">
              <div  id="energyRanking"></div>
            </div>
          </el-card>
        </el-col>
        
      </el-row>
      
  </div>
</template>

<script>
  import { listTunnels } from "@/api/equipment/tunnel/api";
  import * as echarts from "echarts";
  export default {
    data(){
      return{
        tunnelData:[
            {
               tunnelName:'体育中心' ,
            },
            {
               tunnelName:'会展中心' ,
            },
        ],
        // 今日用电
        electricToday:null,
        // 本月用电
        electricMonth:null,
        // 隧道Id
        tunnelId:'',
        // 隧道名称
        tunnelName:'体育中心',
        tunnelBtnStyle: [
          {
             "border": "1px solid rgb(217, 220, 227)",
             "box-shadow": "0px 0px 5px #9fa1a6",
             "color":'#000'
          },
        ],
        dayArr:[],
        monthArr:[],
        eTA:[],
        eMA:[],
        etAdd:0,
        eMAdd:0,
      }
    },
    created() {
      // this.getTunnels()
      this.getTime()
    },
    mounted() {
      this.electricTodayCharts()
      this.electricMonthCharts()
      this.electricRankingCharta()
      this.energyRankingCharts()
      this.alarmsCharts()
    },
    methods:{
      // 查隧道的接口
      getTunnels() {
        listTunnels().then((response) => {
           this.tunnelData = response.rows.slice(0,4);
           // if(!this.tunnelId){
           //     this.tunnelId = response.rows[0].tunnelId
           //     this.tunnelName = response.rows[0].tunnelName
           // }
           
        });
       },
       // 选择隧道
      setTunnel(item, index) {
         console.log(item,"item")
         this.tunnelBtnStyle = []
         this.tunnelBtnStyle[index] = {
            "box-shadow": "0px 0px 5px #9fa1a6",
         };
         // this.tunnelId = item.tunnelId
         this.tunnelName = item.tunnelName
         this.getTime()
      },
      getTime(){
        this.dayArr = []
        this.monthArr = []
        var arr = 10
        let nowHour = new Date().getHours()
        let nowDay = new Date().getDate()
         for(var i=0;i<nowHour;i++){
           this.dayArr.push(i+':00')
         }
         for(var i=1;i<=nowDay;i++){
           this.monthArr.push(i)
         }
         var electricTodayArr = []
         var electricMonth = []
         if(this.tunnelName == '体育中心'){
           electricTodayArr = [40,50,55,40,37,48,58,69,83,90,86,67,54,53,42,51,54,59,69,74,65,56,48,42]
           electricMonth = [1300,1400,1500,1450,1550,1600,1540,1480,1380,1330,1450,1560,1480,1550,1580,1490,1600,1530,1480,1460,1520,1450,1480,1520,1560,1580,1480,1520,1540,1600,1540]
         }else{
           electricTodayArr = [90,86,67,54,53,42,40,50,55,42,51,54,59,69,74,65,56,48,40,37,48,58,69,83]
           electricMonth = [1580,1490,1600,1530,1550,1600,1500,1450,1560,1580,1480,1520,1540,1600,1540,1540,1480,1380,1330,1450,1560,1480,1550,1520,1480,1460,1520,1450,1480,1300,1400]
         }
         this.eTA = electricTodayArr.slice(0,nowHour);
         this.etAdd = 0
         this.eMAdd = 0
         for(var item of this.eTA){
           this.etAdd += item
         }
         this.eMA = electricMonth.slice(0,nowDay)
         for(var item of this.eMA){
           this.eMAdd += item
         }
         this.electricTodayCharts()
         this.electricMonthCharts()
         this.electricRankingCharta()
         this.energyRankingCharts()
         this.alarmsCharts()
      },
      // 今日用电排行
      electricTodayCharts(){
          this.electricToday = echarts.init(document.getElementById('electricToday'));
          
              var option = {
                  tooltip: {
                      	trigger: 'axis',
                          formatter: function(params) {
                          	var str = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff000"></span>' + name + '<br>'
                          	str += params[0].axisValue + ':00 ' + params[0].value + 'kwh<br>'
                          	
                          	return str;
                          }
                  },
                  title: {
                      text: this.etAdd + ' kwh',
                      x: 'left',
                      
                      textStyle: {
                          color:'#00458f'
                      },
                      fontSize:10,
                      fontWeight:"normal"
                  },
                  grid: {
                    left: '5%',
                    right: '8%',
                    bottom: '5%',
                    top:'20%',
                    containLabel: true
                  },
                xAxis: {
                  type: 'category',
                  axisLabel: {
                    textStyle: {
                      color: '#00458f',
                      fontSize: 12
                    }
                  },
                  axisLine: {
                    show: true,
                    lineStyle: {
                      color: '#00458f'
                    },
                  },
                  data: this.dayArr,
                  
                },
                yAxis: {
                  type: 'value',
                  name: "单位：kwh",
                  axisLine: {
                    show: true,
                    lineStyle: {
                      color: '#00458f'
                    },
                  },
                  axisLabel: {
                    textStyle: {
                      color: '#00458f',
                      fontSize: 12
                    }
                  },
                },
                series: [
                  {
                    data: this.eTA,
                    type: 'line',
                    smooth: true
                  }
                ]
              };
          this.electricToday.setOption(option)
      },
      // 本月用电排行
      electricMonthCharts(){
          this.electricMonth = echarts.init(document.getElementById('electricMonth'));
          
              var option = {
                  tooltip: {
                      	trigger: 'axis',
                          formatter: function(params) {
                          	var str = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff000"></span>' + name + '<br>'
                          	str += params[0].axisValue + ':00 ' + params[0].value + 'kwh<br>'
                          	
                          	return str;
                          }
                  },
                  title: {
                      text: this.eMAdd + ' kwh',
                      x: 'left',
                      
                      textStyle: {
                          color:'#00458f'
                      },
                      fontSize:10,
                      fontWeight:"normal"
                  },
                  grid: {
                    left: '5%',
                    right: '8%',
                    bottom: '5%',
                    top:'20%',
                    containLabel: true
                  },
                xAxis: {
                  type: 'category',
                  axisLabel: {
                    textStyle: {
                      color: '#00458f',
                      fontSize: 12
                    }
                  },
                  axisLine: {
                    show: true,
                    lineStyle: {
                      color: '#00458f'
                    },
                  },
                  data: this.monthArr,
                  
                },
                yAxis: {
                  type: 'value',
                  name: "单位：kwh",
                  axisLine: {
                    show: true,
                    lineStyle: {
                      color: '#00458f'
                    },
                  },
                  axisLabel: {
                    textStyle: {
                      color: '#00458f',
                      fontSize: 12
                    }
                  },
                },
                series: [
                  {
                    data: this.eMA,
                    type: 'line',
                    smooth: true
                  }
                ]
              };
          this.electricMonth.setOption(option)
        
      },
      // 隧道用电排行
      electricRankingCharta(){
        var manNumber = []
        var xData = []
        if(this.tunnelName == '体育中心'){
          manNumber = [88,65,58,56,52,47,45,40,35,30];
          xData =  ["基本照明","加强照明","指示器","信号灯","主风机","摄像机","压力表",'引道路灯','风速监测','能见度监测'];
        }else{
          manNumber = [84,63,56,56,50,48,46,44,38,36];
          xData =  ["基本照明","加强照明","信号灯","指示器","主风机","摄像机","引道路灯",'压力表','风速监测','能见度监测'];
        }
            let dom = 600;
            let barWidth = dom / 20;
            let manColors = [];
            // let womanColors = [];
            for (let i = 0; i < 10; i++) {
                manColors.push({
                    type: 'linear',
                    x: 0,
                    x2: 1,
                    y: 0,
                    y2: 0,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#00d1ff', // 最左边
                        },
                        {
                            offset: 0.5,
                            color: '#06fbff', // 左边的右边 颜色
                        },
                        {
                            offset: 0.5,
                            color: '#01afff', // 右边的左边 颜色
                        },
                        {
                            offset: 1,
                            color: '#00458f',
                        },
                    ],
                });
                
            }
            var electricRanking = echarts.init(document.getElementById('electricRanking'))
            var option = {
                // backgroundColor: '#010d3a',
                //提示框
                tooltip: {
                    trigger: 'axis',
                    backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                    color:'white',
                    borderWidth:'1',
                    borderColor:'black',
                    textStyle:{
                        color:'white',
                    },
                    formatter: function (p) {
                        return p[0].marker + p[0].axisValue + ': ' +p[0].data
                        console.log(p)
                    
                    },
                    axisPointer: {
                        // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                
                /**区域位置*/
                grid: {
                    left: '14%',
                    right: '10%',
                    top: '10%',
                    bottom: '33%',
                },
                //X轴
                xAxis: {
                    data: xData,
                    type: 'category',
                    axisLine: {
                        show: false,
                        lineStyle: {
                            color: '#00458f',
                            shadowColor: '#00458f',
                            shadowOffsetX: '20',
                        },
                        symbol: ['none', 'arrow'],
                        symbolOffset: [0, 25],
                    },
                    splitLine: {
                        show: false,
                    },
                    axisTick: {
                        show: false,
                    },
                    axisLabel: {
                        margin: 30,
                        fontSize: 14,
                        interval:0,
                        rotate:40 
                    },
                },
                yAxis: {
                    show: true,
                    splitNumber: 4,
                    axisLine: {
                        show: false,
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            type: 'dashed',
                            color: '#00458f',
                        },
                    },
                    axisLabel: {
                        color: '#00458f',
                        margin: 30,
                        fontSize: 15,
                    },
                },
                series: [
                    {
                        name: '男',
                        type: 'bar',
                        barWidth: barWidth,
                        itemStyle: {
                            normal: {
                                color: function (params) {
                                    return manColors[params.dataIndex % 7];
                                },
                            },
                        },
                        data: manNumber,
                    },
                    {
                        z: 2,
                        type: 'pictorialBar',
                        data: manNumber,
                        symbol: 'diamond',
                        symbolOffset: ['0%', '50%'],
                        symbolSize: [barWidth, barWidth * 0.5],
                        itemStyle: {
                            normal: {
                                color: function (params) {
                                    return manColors[params.dataIndex % 7];
                                },
                            },
                        },
                    },
                    {
                        z: 3,
                        type: 'pictorialBar',
                        symbolPosition: 'end',
                        data: manNumber,
                        symbol: 'diamond',
                        symbolOffset: ['0%', '-50%'],
                        symbolSize: [barWidth, barWidth * 0.5],
                        itemStyle: {
                            normal: {
                                borderWidth: 0,
                                color: function (params) {
                                    return manColors[params.dataIndex % 7].colorStops[0].color;
                                },
                            },
                        },
                    },
                    
                ],
            };
            electricRanking.setOption(option)
          
      },
      // 隧道能耗排行
      energyRankingCharts(){
        var manNumber = []
        var xData = []
        if(this.tunnelName == '体育中心'){
          manNumber = [88,65,58,56,52,47,45,40,35,30];
          xData =  ["基本照明","加强照明","指示器","信号灯","主风机","摄像机","压力表",'引道路灯','风速监测','能见度监测'];
        }else{
          manNumber = [84,63,56,56,50,48,46,44,38,36];
          xData =  ["基本照明","加强照明","信号灯","指示器","主风机","摄像机","引道路灯",'压力表','风速监测','能见度监测'];
        }
         
          let dom = 600;
          let barWidth = dom / 20;
          let manColors = [];
          // let womanColors = [];
          for (let i = 0; i < 10; i++) {
              manColors.push({
                  type: 'linear',
                  x: 0,
                  x2: 1,
                  y: 0,
                  y2: 0,
                  colorStops: [
                      {
                          offset: 0,
                          color: '#00d1ff', // 最左边
                      },
                      {
                          offset: 0.5,
                          color: '#06fbff', // 左边的右边 颜色
                      },
                      {
                          offset: 0.5,
                          color: '#01afff', // 右边的左边 颜色
                      },
                      {
                          offset: 1,
                          color: '#00458f',
                      },
                  ],
              });
              
          }
          var energyRanking = echarts.init(document.getElementById('energyRanking'))
          var option = {
              // backgroundColor: '#010d3a',
              //提示框
              tooltip: {
                  trigger: 'axis',
                  backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                  color:'white',
                  borderWidth:'1',
                  borderColor:'black',
                  textStyle:{
                      color:'white',
                  },
                  formatter: function (p) {
                      return p[0].marker + p[0].axisValue + ': ' +p[0].data
                      console.log(p)
                  
                  },
                  axisPointer: {
                      // 坐标轴指示器，坐标轴触发有效
                      type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                  },
              },
              
              /**区域位置*/
              grid: {
                  left: '14%',
                  right: '10%',
                  top: '10%',
                  bottom: '33%',
              },
              //X轴
              xAxis: {
                  data: xData,
                  type: 'category',
                  axisLine: {
                      show: false,
                      lineStyle: {
                          color: '#00458f',
                          shadowColor: '#00458f',
                          shadowOffsetX: '20',
                      },
                      symbol: ['none', 'arrow'],
                      symbolOffset: [0, 25],
                  },
                  splitLine: {
                      show: false,
                  },
                  axisTick: {
                      show: false,
                  },
                  axisLabel: {
                      margin: 30,
                      fontSize: 14,
                      interval:0,
                      rotate:40 
                  },
              },
              yAxis: {
                  show: true,
                  splitNumber: 4,
                  axisLine: {
                      show: false,
                  },
                  splitLine: {
                      show: true,
                      lineStyle: {
                          type: 'dashed',
                          color: '#00458f',
                      },
                  },
                  axisLabel: {
                      color: '#00458f',
                      margin: 30,
                      fontSize: 15,
                  },
              },
              series: [
                  {
                      name: '男',
                      type: 'bar',
                      barWidth: barWidth,
                      itemStyle: {
                          normal: {
                              color: function (params) {
                                  return manColors[params.dataIndex % 7];
                              },
                          },
                      },
                      data: manNumber,
                  },
                  {
                      z: 2,
                      type: 'pictorialBar',
                      data: manNumber,
                      symbol: 'diamond',
                      symbolOffset: ['0%', '50%'],
                      symbolSize: [barWidth, barWidth * 0.5],
                      itemStyle: {
                          normal: {
                              color: function (params) {
                                  return manColors[params.dataIndex % 7];
                              },
                          },
                      },
                  },
                  {
                      z: 3,
                      type: 'pictorialBar',
                      symbolPosition: 'end',
                      data: manNumber,
                      symbol: 'diamond',
                      symbolOffset: ['0%', '-50%'],
                      symbolSize: [barWidth, barWidth * 0.5],
                      itemStyle: {
                          normal: {
                              borderWidth: 0,
                              color: function (params) {
                                  return manColors[params.dataIndex % 7].colorStops[0].color;
                              },
                          },
                      },
                  },
                  
              ],
          };
           energyRanking.setOption(option)
      },
      // 能耗占比
      alarmsCharts() {
              var proportionCharts = echarts.init(document.getElementById('proportionCharts'));
              var val1 = 0
              var val2 = 0
              var val3 = 0
              var val4 = 0
              var val5 = 0
              var val6 = 0
              if(this.tunnelName == '体育中心'){
                val1 = 42,
                val2 = 40,
                val3 = 38,
                val4 = 36,
                val5 = 30,
                val6 = 26
              }else{
                val1 = 66,
                val2 = 68,
                val3 = 40,
                val4 = 48,
                val5 = 43,
                val6 = 38
              }
              var option = {
                      tooltip: {
                        trigger: 'item',
                          backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                          color:'#fff',
                          borderWidth:'1',
                          borderColor:'#00458f',
                          textStyle:{
                              color:'#fff',
                          },
                      },
                      
                      
                      // legend: {
                      //     icon: "roundRect",
                      //     itemHeight: 12,
                      //     itemWidth: 12,
                      //     top: '88%',
                      //     x:"center",
                      //     textStyle: {
                      //         color: '#00458f'
                      //     }
                      // },
                      calculable : true,
                      series: [
                          {
                              name:name,
                              type:'pie',
                              startAngle: "30",
                              center:['50%','46%'],
                              radius:['14%','50%'],
                               roseType: 'area',//玫瑰图
                              // avoidLabelOverlap:false,
                              // hoverAnimation:false ,//禁止鼠标悬停放大区域
                              itemStyle: {
                                      borderRadius: 4,
                                     
                                    },
                              color:['#32A8FF',' #FEB100'],
                            
                              label:{
                                  show: true,
                                  formatter: '{b}\n'+'{c}kwh',
                                  color:'#00458f'
                              },
                              labelLine:{
                                  normal:{
                                      length:1,
                                      // length2 :5,
                                      lineStyle: {
                                          color: '#00458f'
                                      }
                                  }
                                  
                              },
                              data:[
                                  {
                                      value:val1,
                                      name:'指示器',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#265195',
                                          }
                                      },
                                  },
                                  {
                                      
                                      value:val2,
                                      name:'信号灯',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#FA6F2A',
                                          }
                                      },
                                  },
                                  {
                                      
                                      value:val3,
                                      name:'主风机',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#00DAC5',
                                          }
                                      },
                                  },
                                  {
                                      
                                      value:val4,
                                      name:'水泵',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#ffff00',
                                          }
                                      },
                                  },
                                  {
                                      
                                      value:val5,
                                      name:'摄像机',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#55aaff',
                                          }
                                      },
                                  },
                                  {
                                      
                                      value:val6,
                                      name:'压力表',
                                     
                                      itemStyle:{
                                          normal:{
                                              color:'#00ff7f',
                                          }
                                      },
                                  },
                              
                              ]
                          },
                          
                      ]
                  };
              
              // 使用刚指定的配置项和数据显示图表。
              proportionCharts.setOption(option);
              // 设置默认突出区域
              // alarmsStatistics.dispatchAction({
              //   type: 'highlight', //突出高亮显示;
              //   seriesIndex: 0,
              //   dataIndex: 1, //默认索引为0，即10%
              // });
      },
        
    },
    
  }
  
</script>

<style scoped lang="scss">
    #electricToday,#electricMonth,#electricRanking,#energyRanking,#proportionCharts,{
        width: 100%;
        height: 270px;
        >div{
            height: 270px;
        }
    }
    
    .menu-button-group{
      // width: 300px;
      // height: 200px;
      // border: solid 1px red;
        .el-button--info{
            background: transparent;
            color: black;
            margin-right: 4px;
            border: solid 1px rgb(217, 220, 227);
        }
    }
    .phoneStatistics {
      position: relative;
      .title {
        padding: 0 0 15px 0;
        font-size: 18px;
        font-weight: 700;
        position: relative;
        .preposition {
          background-color: #3e7deb;
          width: 6px;
          height: 25px;
          position: absolute;
          left: -15px;
          top: 0;
        }
      }
      .body {
        display: flex;
        justify-content: center;
      }
    }
</style>