<template>
    <div>
        <div class="contentTitle">今日用电对比
          <i>Comparison electric today</i>
        </div>
        <div class="alarmsStatisticsBox" id="todayContrastCharts">
            
        </div>
         
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
                var todayContrastCharts = echarts.init(document.getElementById('todayContrastCharts'));
                    
                	var option = {
                		 color: ['#fff000', '#85BDE8','#91C7AE'],
                		
                		tooltip: {
                			trigger: 'axis',
                			backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                			color:'white',
                			borderWidth:'1',
                			borderColor:'black',
                			textStyle:{
                			    color:'white',
                			},
                			formatter: function(params) {
                				var str = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#fff000"></span>' + name + '<br>'
                				str += params[0].axisValue + ' : ' + params[0].value + '<br>'
                				
                				return str;
                			}
                		},
                		legend: {  //示例颜色
                			data: ['今日','同比','环比'],
                			textStyle: { //图例文字的样式
                				color: '#fff',
                				fontSize: 12
                			},
                            x:"30%",
                            y:'10%',
                			icon: 'roundRect',
                            itemHeight: 2,
                            itemWidth: 12,
                		},
                		grid: {
                			left: '10%',
                			right: '12%',
                			bottom: '10%',
                			top:'22%',
                			containLabel: true
                		},
                		
                	
                		xAxis: {
                			type: 'category',
                			boundaryGap: false,
                			axisLine: {
                				show: true,
                				lineStyle: {
                					color: '#00557f'
                				},
                			},
                			axisLabel: {
                				textStyle: {
                					color: '#ffffff',
                					fontSize: 12
                				}
                			},
                			data: ["1","2","3","4","5","6"],
                		},
                		yAxis: {
                			type: 'value',
                			axisLine: {
                				show: true,
                				lineStyle: {
                					color: '#00557f'
                				},
                			},
                			axisLabel: {
                				textStyle: {
                					color: '#ffffff',
                					fontSize: 12
                				},
                                color: '#00557f'
                			},
                            splitLine: {           //分割线
                                show: true,     //控制分割线是否显示
                                lineStyle:{       //分割线的样式
                                    color: ['rgba(43,70,126,1)'],
                                    width: 2,
                                    type: 'solid'
                                }
                　　        },
                		},
                		series: [{
                				name: '今日',
                				type: 'line',
                                symbol: 'circle', // 默认是空心圆（中间是白色的），改成实心圆
                                showAllSymbol: true,
                                symbolSize: 5,
                                smooth: true,
                                showSymbol: true,
                				data: [60,50,70,80,70,60],
                                // lineStyle: {
                                // 	normal: {
                                // 		width: 2,
                                // 		color: "#fff000", // 线条颜色
                                //     }
                                // },
                                axisLabel: {
                                	show: true,
                                	textStyle: {
                                		color: '#FFFFFF',
                                	},
                                },
                                // tooltip: {
                                // 	show: true
                                // },
                                // itemStyle:{
                                //     normal:{
                                //         label:{
                                //             show:true,
                                //             color:'white',
                                //         },
                                        
                                //         color:'#fff000',
                                //         // borderColor:'red',  //拐点边框颜色
                                //     }
                                // },
                              
                			},
                			{
                				name: '同比',
                				type: 'line',
                				data: [80,82,85,79,83,88,83]
                			},
                			{
                				name: '环比',
                				type: 'line',
                				data: [63,66,68,65,66,68],
                                
                			}
                			
                		]
                	}
                	// 使用刚指定的配置项和数据显示图表。
                	todayContrastCharts.setOption(option);
            },
        }
    }
</script>

<style>
</style>
