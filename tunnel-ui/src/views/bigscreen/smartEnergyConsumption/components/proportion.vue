<template>
    <div>
        <div class="contentTitle">能耗占比
          <i>Proportion of energy consumption</i>
        </div>
        <div class="alarmsStatisticsBox peakBackground" id="proportionCharts"></div>
    </div>
</template>

<script>
    import *as echarts from 'echarts'   
    export default{
        data() {
          return {
          };
        },
        mounted(){
            this.alarmsCharts()
        },
        methods:{
            alarmsCharts() {
                    var todayCharts = echarts.init(document.getElementById('proportionCharts'));
                    var option = {
                            tooltip: {
                            	trigger: 'item',
                                backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                                color:'white',
                                borderWidth:'1',
                                borderColor:'black',
                                textStyle:{
                                    color:'white',
                                },
                            },
                            
                            // title: {
                            //     text: '100%',
                            //     x: 'center',
                            //     y: '34%',
                            //     textStyle: {
                            //         color:'white'
                            //     },
                            //     fontSize:10,
                            //     fontWeight:"normal"
                            // },
                            legend: {
                                icon: "roundRect",
                                itemHeight: 12,
                                itemWidth: 12,
                                top: '88%',
                                x:"center",
                                textStyle: {
                                    color: '#fff'
                                }
                            },
                            calculable : true,
                            series: [
                                {
                                    name:name,
                                    type:'pie',
                                    startAngle: "30",
                                    center:['50%','46%'],
                                    radius:['14%','75%'],
                                     roseType: 'area',//玫瑰图
                                    // avoidLabelOverlap:false,
                                    // hoverAnimation:false ,//禁止鼠标悬停放大区域
                                    itemStyle: {
                                            borderRadius: 4,
                                           
                                          },
                                    color:['#32A8FF',' #FEB100'],
                                  
                                    label:{
                                        show: true,
                                        formatter: '{c}kwh',
                                        color:'white'
                                    },
                                    labelLine:{
                                        normal:{
                                            length:1,
                                            // length2 :5,
                                            lineStyle: {
                                                color: 'white'
                                            }
                                        }
                                        
                                    },
                                    data:[
                                        {
                                            value:32,
                                            name:'指示器',
                                           
                                            itemStyle:{
                                                normal:{
                                                    color:'#265195',
                                                }
                                            },
                                        },
                                        {
                                            
                                            value:'43',
                                            name:'信号灯',
                                           
                                            itemStyle:{
                                                normal:{
                                                    color:'#FA6F2A',
                                                }
                                            },
                                        },
                                        {
                                            
                                            value:20,
                                            name:'主风机',
                                           
                                            itemStyle:{
                                                normal:{
                                                    color:'#00DAC5',
                                                }
                                            },
                                        },
                                        {
                                            
                                            value:34,
                                            name:'水泵',
                                           
                                            itemStyle:{
                                                normal:{
                                                    color:'#ffff00',
                                                }
                                            },
                                        },
                                        {
                                            
                                            value:76,
                                            name:'摄像机',
                                           
                                            itemStyle:{
                                                normal:{
                                                    color:'#55aaff',
                                                }
                                            },
                                        },
                                        {
                                            
                                            value:53,
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
                    todayCharts.setOption(option);
                    // 设置默认突出区域
                    // alarmsStatistics.dispatchAction({
                    //   type: 'highlight', //突出高亮显示;
                    //   seriesIndex: 0,
                    //   dataIndex: 1, //默认索引为0，即10%
                    // });
            },
        }
    }
</script>

<style lang="less" scoped>
    .contentTitle{
        background-color: rgba(255,255,255,0.2) !important;
        // background-color: rgba(4,15,78,0.4);
        i{
            color: rgba(255,255,255,0.5);
        }
        
    }
</style>
