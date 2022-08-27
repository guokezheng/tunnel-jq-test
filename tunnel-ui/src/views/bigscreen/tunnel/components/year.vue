<template>
    <div class="yearBox">
        <div class="title yearBoxTitle">年度能耗
            <span style="font-size:14px;">(kwh)</span>
        </div>
        <div ref="echartsBox" id="year"></div>
    </div>
</template>

<script>
    import *as echarts from 'echarts'
    import elementResizeDetectorMaker from 'element-resize-detector'
    export default{
        props: {
            energyConsumption: {
                type: Object,
            }
        },
        data(){
            return{}
        },
        watch: {
            energyConsumption() {
                this.year()
            }
        },
        mounted(){
            this.year()
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
            year() {
            	var year = echarts.init(document.getElementById('year'));
            	var option = {
            		 // color: ['#fff000', '#85BDE8','#91C7AE'],
            		
            		tooltip: {
            			trigger: 'axis',
            			
            		},
                    grid: {
                    	left: '5%',
                    	right: '12%',
                    	bottom: '3%',
                    	top:'40',
                    	containLabel: true
                    },
            		// legend: {
            			// data: [this.energyConsumption.name],
            		// 	textStyle: { //图例文字的样式
            		// 		color: '#fff',
            		// 		fontSize: 12
            		// 	},
                    //     x:"38%",
            		// 	icon: 'roundRect',
                    //     itemHeight: 2,
                    //     itemWidth: 12,
            		// },
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
            			data: ["1月","2月","3月","4月","5月","6月"],
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
            				name: this.energyConsumption.name,
            				type: 'line',
            				stack: '总量',
                            symbol: 'circle', // 默认是空心圆（中间是白色的），改成实心圆
                            showAllSymbol: true,
                            symbolSize: 5,
                            smooth: true,
                            showSymbol: true,
            				data: this.energyConsumption.data,
                            lineStyle: {
                            	normal: {
                            		width: 2,
                            		color: "#fff000", // 线条颜色
                                }
                            },
                            axisLabel: {
                            	show: true,
                            	textStyle: {
                            		color: '#FFFFFF',
                            	},
                            },
                            itemStyle:{
                                normal:{
                                    label:{
                                        show:true,
                                        color:'white',
                                    },
                                    
                                    color:'#fff000',
                                    // borderColor:'red',  //拐点边框颜色
                                }
                            },
                            // areaStyle: { //区域填充样式
                            // 	normal: {
                            // 		//线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                            // 		color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            // 				offset: 0,
                            // 				color: "rgba(251, 255, 5, 0.3)"
                            // 			},
                            // 			{
                            // 				offset: 1,
                            // 				color: "rgba(255, 255, 255, 0.0)"
                            // 			}
                            // 		], false),
                            // 		shadowColor: 'rgba(255, 255, 127, 0.5)', //阴影颜色
                            // 		shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                            // 	}
                            // },
                            // itemStyle: {  
                            //     normal: {  
                            //         color: 'red',  
                            //         lineStyle: {        // 系列级个性化折线样式  
                            //             width: 2,  
                            //             type: 'solid',  
                            //             color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{  
                            //                 offset: 0,  
                            //                 color: '#EFA773'  
                            //             }, {  
                            //                 offset: 0.3,  
                            //                 color: '#D4C649'  
                            //             },{  
                            //                 offset: 0.6,  
                            //                 color: '#EF9B7C'  
                            //             },{  
                            //                 offset: 1,  
                            //                 color: '#EF7797'  
                            //             }]),//线条渐变色  
                            //         }  
                            //     }, 
                           // }
            			},
            			// {
            			// 	name: '红河',
            			// 	type: 'line',
            			// 	stack: '总量',
            			// 	data: [80,82,85,79,83,88,83]
            			// },
            			// {
            			// 	name: '洪山',
            			// 	type: 'line',
            			// 	stack: '总量',
            			// 	data: [63,66,68,65,66,68],
                            
            			// }
            			
            		]
            	}
            	// 使用刚指定的配置项和数据显示图表。
            	year.setOption(option);
            },
        }
    }
</script>

<style scoped="scoped">
    .yearBox{
        width: 100%;
        height: 100%;
        overflow: hidden;
    }
    #year{
        width: 100%;
        height: 90%;
    }
    .yearBoxTitle{
        height: 0px !important;
    }
</style>
