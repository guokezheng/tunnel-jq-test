<template>
    <div class="twoRanking">
        <div>
            <div class="contentTitle">隧道用电排行
              <i>Tunnel power consumption ranking</i>
            </div>
            <div class="alarmsStatisticsBox peakBackground" id="electricRanking"></div>
        </div>
        <div>
            <div class="contentTitle">隧道能耗排行
              <i>Tunnel energy consumption ranking</i>
            </div>
            <div class="alarmsStatisticsBox peakBackground" id="energyRanking"></div>
        </div>
         
    </div>
</template>

<script>
    import *as echarts from 'echarts'
    export default{
       
        data(){
            return{}
        },
       
        mounted(){
            this.electricRanking()
            this.energyRanking()
        },
        methods:{
            electricRanking() {
                var dataAxis = ["基本照明","加强照明","指示器","信号灯","主风机","摄像机","压力表",'引道路灯','风速监测','能见度监测'];
                			var data = [88,65,58,56,52,47,45,40,35,30];  
                  			var getOption = function() {
                				var chartOption = { 
                					grid: { 
                						x: 25,
                						x2: 25,
                						y: 25,
                						y2: 35, 
                                         left:'10%',
                                         right:'10%',
                                         bottom:'25%'
                					}, 
                				    xAxis: [{ 
                						 type: 'category',  
                						axisLabel: {
                                textStyle: {
                                    color: '#fff', 
                                },
                                  interval:0,
                                  rotate:40 
                               
                            }, 
                             offset :10,
                						 boundaryGap: false,
                						 splitLine: {
                				            show: false
                				       },
                						data: dataAxis,
                						axisLine:{ 
                						 	show:false,
                						 },
                						 axisTick:{
                						 	show:false,
                						 },
                						 
                					}],
                				    yAxis: {
                              name:'kw-h',
                               nameTextStyle:{
                                  color:'#fff',
                                  padding:[10,20,-10,-40],
                              },
                				    	splitLine: {  
                                           show:false
                                        },  
                				        axisLine: {
                				            show: false
                				        },
                				        axisTick: {
                				            show: false
                				        },
                				        axisLabel: {
                				            show:false
                				        }
                				    },
                				    dataZoom: [
                				        {
                				            type: 'inside'
                				        }
                				    ],
                				    series: [ 
                				        {
                				            type: 'bar',
                				            barWidth :10,//柱图宽度
                				            itemStyle: {
                				                normal: {
                				                	barBorderRadius: 6,
                				                    color: {
                									    type: 'linear',
                									    x: 0,
                									    y: 0,
                									    x2: 0,
                									    y2: 1,
                									    colorStops: [{
                									        offset: 0, color: '#f45c3d' // 0% 处的颜色
                									    }, {
                									        offset: 1, color: '#f9bf6b' // 100% 处的颜色
                									    }],
                									    globalCoord: true // 缺省为 false
                									},
                									label: {
                										show: true,
                										position: 'top',
                										textStyle: {
                										color: '#fff'
                										},
                										formatter:function(params){
                											if(params.value==0){
                												return '';
                											}else{
                												return params.value;
                											}
                										}
                									},
                								}, 
                				            },
                				            data: data
                				        }
                				    ]
                				};
                				return chartOption;
                			};
                			var byId = function(id) {
                				return document.getElementById(id);
                			}; 
                			var electricRanking = echarts.init(byId('electricRanking'));
                			electricRanking.setOption(getOption());
                
            },
            energyRanking(){
                
                let manNumber = [88,65,58,56,52,47,45,40,35,30];
                let xData =  ["基本照明","加强照明","指示器","信号灯","主风机","摄像机","压力表",'引道路灯','风速监测','能见度监测'];
                
                let dom = 800;
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
                                color: '#008ecf',
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
                        left: '10%',
                        right: '10%',
                        top: '20%',
                        bottom: '33%',
                    },
                    //X轴
                    xAxis: {
                        data: xData,
                        type: 'category',
                        axisLine: {
                            show: false,
                            lineStyle: {
                                color: 'rgba(255,255,255,1)',
                                shadowColor: 'rgba(255,255,255,1)',
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
                            interval:0,
                            rotate:40 
                        },
                    },
                    yAxis: {
                        show: true,
                        name:'kw-h',
                         nameTextStyle:{
                            color:'#fff',
                            padding:[-20,20,0,-60],
                        },
                        splitNumber: 4,
                        axisLine: {
                            show: false,
                        },
                        splitLine: {
                            show: true,
                            lineStyle: {
                                type: 'dashed',
                                color: '#075858',
                            },
                        },
                        axisLabel: {
                            color: '#FFFFFF',
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
            }
        }
    }
</script>


<style lang="less" scoped>
    .twoRanking{
        width: 100%;
        height: 100%;
        // border: solid 1px white;
        display: flex;
        >div{
            width: 50%;
            // border: solid 1px white;
        }
    }
    .contentTitle{
        background-color: rgba(255,255,255,0.2) !important;
        // background-color: rgba(4,15,78,0.4);
        i{
            color: rgba(255,255,255,0.5);
        }
        
    }
</style>
