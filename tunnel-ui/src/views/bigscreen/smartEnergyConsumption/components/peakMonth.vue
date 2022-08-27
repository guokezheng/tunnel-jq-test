<template>
    <div class="threeCharts">
            <div class="peakMiniBox" id="monthDaysCharts"></div>
            <div class="peakCenter peakMiniBox" id="monthYearCharts"></div>
            <div class="peakRight peakMiniBox" id="monthRingCharts"></div>
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
            this.monthDaysCharts()
            this.monthYearCharts()
            this.monthRingCharts()
        },
        methods:{
            monthDaysCharts() {
                var monthDaysCharts = echarts.init(document.getElementById('monthDaysCharts'));
                    var date = new Date().getMonth()+1
                    var dateArr = []
                    var dateValArr = [1400,1600,1300,1500,1600,1300,1530,1480,1300,1600,1450,1550]
                    for(var i=1;i<=date;i++){
                      dateArr.push(i+'月')
                    }
                    var dateVal = dateValArr.slice(0,date)

                	var option = {
                		 // color: ['#fff000', '#85BDE8','#91C7AE'],
                		 title : {
                        show:true, //显示策略，默认值true,可选为：true（显示） | false（隐藏）
                        text: '本月用电', //主标题文本，'\n'指定换行
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
                        }
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
                				str += params[0].name + ' ' + params[0].value + 'kwh<br>'

                				return str;
                			}
                		},
                		grid: {
                			left: '12%',
                			right: '18%',
                			bottom: '10%',
                			top:'28%',
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
                                        padding: [0, 30, 0, 0]
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
                                itemStyle:{
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
                	monthDaysCharts.setOption(option);
            },
            monthYearCharts(){
                var monthYearCharts = echarts.init(document.getElementById('monthYearCharts'))
                var date = new Date().getMonth()+1
                var option = {
                    title : {
                       show:true,
                       text: '本月同比',
                       x:'center',
                       y: '0%',
                       padding: 5,
                       textStyle: {
                           fontSize: 14,
                           color:"white",
                           fontWeight: 'normal',
                       },
                    },
                    graphic:{
                       type:"text",
                       left:"50%",
                       bottom:"5%",
                       style:{
                           text:date+'月',
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
                        name: "单位：kwh",
                        nameTextStyle: {
                                    color: "#fff",
                                    nameLocation: "start",
                                    padding: [0, 30, 0, 0]
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
                                value:1800,
                                name:'本期',

                            },
                            {
                                value:0,
                                name:'去年同期',

                            }],
                	}]
                }
                monthYearCharts.setOption(option)
            },
            monthRingCharts(){
                var monthRingCharts = echarts.init(document.getElementById('monthRingCharts'))
                var date = new Date().getMonth()+1
                var option = {
                    title : {
                       show:true,
                       text: '本月环比',
                       x:'center',
                       y: '0%',
                       padding: 5,
                       textStyle: {
                           fontSize: 14,
                           color:"white",
                           fontWeight: 'normal',
                       },
                    },
                    graphic:{
                       type:"text",
                       left:"50%",
                       bottom:"5%",
                       style:{
                           text:date+"月",
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
                        name: "单位：kwh",
                        nameTextStyle: {
                                    color: "#fff",
                                    nameLocation: "start",
                                    padding: [0, 30, 0, 0]
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
                	series: [{
                		type: 'bar',
                		barWidth: '60%',
                		showSymbol: true,
                        itemStyle:{
                            normal:{
                                barBorderRadius:[5,5,0,0],
                                //柱体的颜色
                                //右，下，左，上（1，0，0，0）表示从正右开始向左渐变
                                color: function(params){
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
                                value:1500,
                                name:'本期',

                            },
                            {
                                value:13000,
                                name:'上月环比',

                            }],
                	}]
                }
                monthRingCharts.setOption(option)
            }
        }
    }
</script>

<style>
</style>
