<template>
    <div class="TrafficFlowBox">
        <div class="title">车流量
            <span style="font-size:14px;">(辆)</span>
        </div>
        <div ref="echartsBox" id="TrafficFlow" ></div>
    </div>
</template>

<script>
    import *as echarts from 'echarts'
    import elementResizeDetectorMaker from 'element-resize-detector'
    export default{
        props: {
            trafficData: {
                type: Object,
            }
        },
        data(){
            return{}
        },
        watch: {
            trafficData() {
                this.TrafficFlow()
            }
        },
        mounted(){
            this.TrafficFlow()
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
            TrafficFlow() {
            	var TrafficFlow = echarts.init(document.getElementById('TrafficFlow'))
            	
            	var option = {
            		color: ['#00C8FF'],
            		tooltip: {
            			trigger: 'axis',
                        backgroundColor:'rgba(0,0,0,0.8)',//通过设置rgba调节背景颜色与透明度
                        color:'white',
                        borderWidth:'1',
                        borderColor:'black',
                        textStyle:{
                            color:'white',
                        }
            		},
            		grid: {
            			left: '10%',
            			right: '10%',
            			bottom: '10%',
            			top:'14%',
            			containLabel: true
            		},
            		xAxis: [{
            			type: 'category',
            			data: ["1月","2月","3月","4月","5月","6月"],
            			axisTick: {
            				alignWithLabel: true
            			},
            			nameTextStyle: { //关键代码
            				padding: [30, 0, 0, -10],
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
            		}],
            		series: [{
            			type: 'bar',
            			barWidth: '60%',
            			showSymbol: true,
            			data: this.trafficData.data,
                        itemStyle: {
                              //柱形图圆角，鼠标移上去效果，如果只是一个数字则说明四个参数全部设置为那么多
                              normal: {
                                    //柱形图圆角，初始化效果
                                    barBorderRadius:[5,5,0,0],
                                    color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                                        offset: 0,
                                        color: "#002a5e" // 0% 处的颜色
                                    }, {
                                        offset: 0.6,
                                        color: "#007bc2" // 60% 处的颜色
                                    }, {
                                        offset: 1,
                                        color: "#9aaadd" // 100% 处的颜色
                                    }], false)
                                    
                              }
                        },
                        
            		}]
            	}
            	TrafficFlow.setOption(option)
            },
        }
    }
</script>

<style lang="less" scoped >
    .TrafficFlowBox{
        width: 100%;
        height: 100%;
        overflow: hidden;
    }
    #TrafficFlow{
        width: 100%;
        height: 81%;
        overflow: hidden;
    }
   
</style>
<style>
    .title{
        height: 1vw;
        width: 100%;
        margin-left: 0.2vw;
        margin-top: 0.8vw;
        /* font-weight: bold; */
        font-size: 1vw;
        transform: scale(0.9);
    }
    canvas{
        height: 100% !important;
    }
</style>