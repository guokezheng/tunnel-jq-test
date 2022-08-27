<template>
    <div class="threeCharts">
            <div class="peakMiniBox" id="todayHoursCharts"></div>
            <div class="peakCenter peakMiniBox" id="todayYearCharts"></div>
            <div class="peakRight peakMiniBox" id="todayRingCharts"></div>
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
            this.todayHoursCharts()
            this.todayYearCharts()
            this.todayRingCharts()
        },
        methods:{
            todayHoursCharts() {
                var todayHoursCharts = echarts.init(document.getElementById('todayHoursCharts'));
                    var date = new Date().getHours()
                    console.log(date,"date")
                    var dateArr = []
                    var dateValArr = [38,25,38,26,30,28,30,50,79,40,38,40,60,80,70,40,50,60,80,70,60,50,30,20]
                    for(var i=1;i<=date;i++){
                      dateArr.push(i)
                    }
                    var dateVal = dateValArr.slice(0,date)
                	var option = {
                		 // color: ['#fff000', '#85BDE8','#91C7AE'],
                		 title : {
                		            show:true, //显示策略，默认值true,可选为：true（显示） | false（隐藏）
                		            text: '今日用电', //主标题文本，'\n'指定换行
                		            // link:'', //主标题文本超链接,默认值true
                		            // target: null, //指定窗口打开主标题超链接，支持'self' | 'blank'，不指定等同为'blank'（新窗口）
                		            // subtext: '副标题', //副标题文本，'\n'指定换行
                		            // sublink: '', //副标题文本超链接
                		            // subtarget: null, //指定窗口打开副标题超链接，支持'self' | 'blank'，不指定等同为'blank'（新窗口）
                		            x:'center', //水平安放位置，默认为'left'，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
                		            y: '0%', //垂直安放位置，默认为top，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
                		            // textAlign: null ,//水平对齐方式，默认根据x设置自动调整，可选为： left' | 'right' | 'center
                		            // backgroundColor: 'rgba(0,0,0,0)', //标题背景颜色，默认'rgba(0,0,0,0)'透明
                		            // borderColor: '#ccc', //标题边框颜色,默认'#ccc'
                		            // borderWidth: 0, //标题边框线宽，单位px，默认为0（无边框）
                		            padding: 5, //标题内边距，单位px，默认各方向内边距为5，接受数组分别设定上右下左边距
                		            // itemGap: 10, //主副标题纵向间隔，单位px，默认为10
                		            textStyle: { //主标题文本样式{"fontSize": 18,"fontWeight": "bolder","color": "#333"}
                		                // fontFamily: 'Arial, Verdana, sans...',
                		                fontSize: 14,
                                        color:"white",
                		                // fontStyle: 'normal',
                		                fontWeight: 'normal',
                                        
                		            },
                		            // subtextStyle: {//副标题文本样式{"color": "#aaa"}
                		                // fontFamily: 'Arial, Verdana, sans...',
                		                // fontSize: 12,
                		                // fontStyle: 'normal',
                		                // fontWeight: 'normal',
                		            // },
                		            // zlevel: 0, //一级层叠控制。默认0,每一个不同的zlevel将产生一个独立的canvas，相同zlevel的组件或图标将在同一个canvas上渲染。zlevel越高越靠顶层，canvas对象增多会消耗更多的内存和性能，并不建议设置过多的zlevel，大部分情况可以通过二级层叠控制z实现层叠控制。
                		            // z: 6, //二级层叠控制，默认6,同一个canvas（相同zlevel）上z越高约靠顶层。
                		        },
                		
                		tooltip: {
                			trigger: 'axis',
                      position: [20, 20],
                			backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                			color:'white',
                			borderWidth:'1',
                			borderColor:'black',
                			textStyle:{
                			    color:'white',
                			},
                			formatter: function(params) {
                        var str = params[0].marker+ name
                				str += params[0].axisValue + ':00 ' + params[0].value + 'kwh<br>'
                				
                				return str;
                			}
                		},
                		// legend: {  //示例颜色
                		// 	data: ['今日24小时用电'],
                		// 	textStyle: { //图例文字的样式
                		// 		color: '#fff',
                		// 		fontSize: 12
                		// 	},
                  //           x:"30%",
                  //           y:'10%',
                		// 	icon: 'roundRect',
                  //           itemHeight: 2,
                  //           itemWidth: 12,
                		// },
                		grid: {
                			left: '12%',
                			right: '18%',
                			bottom: '10%',
                			top:'28%',
                			containLabel: true
                		},
                		
                	
                		xAxis: {
                      name: "时",
                      nameTextStyle: {
                          color: "#fff",
                          nameLocation: "start",
                          padding: [0, 0, 0, -10]
                        },
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
                			data: dateArr,
                		},
                		yAxis: {
                			type: 'value',
                			axisLine: {
                				show: true,
                				lineStyle: {
                					color: '#00557f'
                				},
                			},
                      name: "单位：kwh",
                      nameTextStyle: {
                          color: "#fff",
                          nameLocation: "start",
                          padding: [0, 30, 0, 20]
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
                        // showAllSymbol: true,
                        symbolSize: 5,
                        smooth: true,
                        showSymbol: true,
                				data: dateVal,
                        lineStyle: {
                          normal: {
                            width: 2,
                            color: {//线条颜色
                                    colorStops: [{
                                        offset: 0, color: '#5555ff'
                                    }, {
                                        offset: 1, color: '#0f0'
                                    }]
                                }
                            }
                        },
                        axisLabel: {
                          show: true,
                          textStyle: {
                            color: '#FFFFFF',
                          },
                        },
                        tooltip: {
                          show: true
                        },
                        itemStyle:{//每一个折点的数值
                            normal:{
                                label:{
                                    show:false,
                                    color:'white',
                                },
                                
                                color:'#fff000',
                                // borderColor:'red',  //拐点边框颜色
                            }
                        },
                              
                			},
                		]
                	}
                	// 使用刚指定的配置项和数据显示图表。
                	todayHoursCharts.setOption(option);
            },
            todayYearCharts(){
                var todayYearCharts = echarts.init(document.getElementById('todayYearCharts'))
                var date = new Date().getDate()
                var option = {
                    title : {
                       show:true, 
                       text: '今日同比', 
                       x:'center', 
                       y: '0%', 
                       padding: 5, 
                       textStyle: { 
                           fontSize: 14,
                           color:"white",
                           fontWeight: 'normal',
                       },
                    },
                    // 插入文字
                    graphic:{
                       type:"text",
                       left:"50%",
                       bottom:"5%",
                       style:{
                           text:date+"日",
                           textAlign:"center",
                           fill:"#fff",
                           fontSize:12,
                       }
                    },
                	color: ['#00C8FF'],
                	tooltip: {
                		trigger: 'axis',
                    position: [20, 20],
                    backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                    color:'white',
                    borderWidth:'1',
                    borderColor:'black',
                    textStyle:{
                        color:'white',
                    },
                    formatter: function(params) {
                      console.log(params)
                      var str = params[0].marker+ name 
                      str += params[0].name + ' : ' + params[0].value + '<br>'
                      
                      return str;
                    }
                	},
                	grid: {
                		left: '10%',
                		right: '18%',
                		bottom: '10%',
                		top:'28%',
                		containLabel: true
                	},
                    legend: {
                       
                        data:["本期","去年同期"]
                    },
                    
                	xAxis: [{
                    
                		type: 'category',
                		data: ["",""],
                		axisTick: {
                			alignWithLabel: true
                		},
                		
                		axisLine: {
                			lineStyle: {
                				color: '#003476',
                				width: 1,
                			}
                		},
                		show: true,
                        axisLabel: {
                        	show: true,
                        	textStyle: {
                        		color: '#FFFFFF',
                        	},
                        },
                	}],
                	yAxis: [{
                		type: 'value',
                		minInterval: 1,
                        name: "单位：kwh",
                        nameTextStyle: {
                                    color: "#fff",
                                    nameLocation: "start",
                                    padding: [0, 30, 0, 0]
                                  },
                		splitLine: {
                			show: false
                		},
                		"axisTick":{//y轴刻度线
                		    "show":false
                		},
                		axisLine: {
                			show: true,
                			lineStyle: {
                				color: '#003476',
                				width: 1,
                				type: 'solid'
                			}
                		},
                        axisLabel: {
                        	show: true,
                        	textStyle: {
                        		color: '#FFFFFF',
                        	},
                        },
                	}],
                    calculable : true,
                	series: [{
                		type: 'bar',
                		barWidth: '50%',
                		showSymbol: true,
                        itemStyle:{
                            normal:{
                                barBorderRadius:[5,5,0,0],
                                //柱体的颜色
                                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                                color: function(params){
                                    console.log(params);
                                    var colorList = [
                                            ['#9aaadd','#007bc2','#002a5e'],
                                            ['#00decc','#049578','#013422'],
                                           
                                        ];
                                    var colorItem = colorList[params.dataIndex];
                                    return new echarts.graphic.LinearGradient(0,0,0,1,[
                                                {
                                                    offset:0,
                                                    color: colorItem[0]
                                                },
                                                {
                                                    offset:0.5,
                                                    color: colorItem[1]
                                                },
                                                {
                                                    offset:1,
                                                    color: colorItem[2]
                                                }
                                                ],false);
                                    }
                                }
                                
                            },
                        data:[
                            {
                                value:420,
                                name:'本期',
                               
                            },
                            {
                                value:120,
                                name:'去年同期',
                               
                            }]
                        
                    }],
                }
                todayYearCharts.setOption(option)
            },
            todayRingCharts(){
                var todayRingCharts = echarts.init(document.getElementById('todayRingCharts'))
                var date = new Date().getDate()
                var option = {
                    title : {
                       show:true, 
                       text: '今日环比', 
                       x:'center', 
                       y: '0%', 
                       padding: 5, 
                       textStyle: { 
                           fontSize: 14,
                           color:"white",
                           fontWeight: 'normal',
                       },
                    },
                    // 插入文字
                    graphic:{
                       type:"text",
                       left:"50%",
                       bottom:"5%",
                       style:{
                           text:date + "日",
                           textAlign:"center",
                           fill:"#fff",
                           fontSize:12,
                       }
                    },
                	color: ['#00C8FF'],
                	tooltip: {
                		trigger: 'axis',
                    position: [20, 20],
                    backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                    color:'white',
                    borderWidth:'1',
                    borderColor:'black',
                    textStyle:{
                        color:'white',
                    },
                    formatter: function(params) {
                        console.log(params)
                      var str = params[0].marker+ name 
                      str += params[0].name + ' : ' + params[0].value + '<br>'
                      
                      return str;
                    }
                       
                	},
                	grid: {
                		left: '10%',
                		right: '18%',
                		bottom: '10%',
                		top:'28%',
                		containLabel: true
                	},
                	xAxis: [{
                    
                		type: 'category',
                		data: ["",""],
                		axisTick: {
                			alignWithLabel: true
                		},
                		
                		axisLine: {
                			lineStyle: {
                				color: '#003476',
                				width: 1,
                			}
                		},
                		show: true,
                        axisLabel: {
                        	show: true,
                        	textStyle: {
                        		color: '#FFFFFF',
                        	},
                        },
                	}],
                	yAxis: [{
                		type: 'value',
                		minInterval: 1,
                		splitLine: {
                			show: false
                		},
                		"axisTick":{//y轴刻度线
                		    "show":false
                		},
                		axisLine: {
                			show: true,
                			lineStyle: {
                				color: '#003476',
                				width: 1,
                				type: 'solid'
                			}
                		},
                        axisLabel: {
                        	show: true,
                        	textStyle: {
                        		color: '#FFFFFF',
                        	},
                        },
                        name: "单位：kwh",
                                  nameTextStyle: {
                                    color: "#fff",
                                    nameLocation: "start",
                                    padding: [0, 40, 0, 0]
                                  },
                	}],
                	series: [{
                		type: 'bar',
                		barWidth: '50%',
                		showSymbol: true,
                        itemStyle:{
                            normal:{
                                barBorderRadius:[5,5,0,0],
                                //柱体的颜色
                                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                                color: function(params){
                                    console.log(params);
                                    var colorList = [
                                            ['#9aaadd','#007bc2','#002a5e'],
                                            ['#00decc','#049578','#013422'],
                                           
                                        ];
                                    var colorItem = colorList[params.dataIndex];
                                    return new echarts.graphic.LinearGradient(0,0,0,1,[
                                                {
                                                    offset:0,
                                                    color: colorItem[0]
                                                },
                                                {
                                                    offset:0.5,
                                                    color: colorItem[1]
                                                },
                                                {
                                                    offset:1,
                                                    color: colorItem[2]
                                                }
                                                ],false);
                                    }
                                }
                                
                            },
                       data: [{
                                value:480,
                                name:'本期',
                               
                            },
                            {
                                value:1400,
                                name:'上月环比',
                               
                            }],
                	}]
                }
                todayRingCharts.setOption(option)
            }
        }
    }
</script>

<style>
</style>
