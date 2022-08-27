<template>
    <div class="burglarAlarmBox">
        <div class="title">火灾报警器</div>
        <div id="burglarAlarm"></div>
    </div>
</template>

<script>
import *as echarts from 'echarts'   
export default {
    data(){
        return{}
    },
    mounted(){
        this.burglarAlarm()
        var that = this
        TrsUtils.$on('burglarAlarm', function(msg) {
        	that.burglarAlarm(msg)
        })
    },
    methods: {
        burglarAlarm(arr) {
                var burglarAlarm = echarts.init(document.getElementById('burglarAlarm'));
                var normal = 90
                var abnormal = 10
                if(arr){
                    normal = arr[1]
                    abnormal = arr[0]
                }
                
                
                console.log(normal,'正常')
                var option = {
                        // tooltip: {
                        //     trigger: 'item',
                        //     formatter: "{a} <br/>{b}: {c} ({d}%)"
                        // },
                        title: {
                            text: '100%',
                            x: 'center',
                            y: '34%',
                            textStyle: {
                                color:'white'
                            },
                            fontSize:10,
                            fontWeight:"normal"
                        },
                        legend: {
                            // orient: 'vertical',
                            icon: "roundRect",
                            itemHeight: 12,
                            itemWidth: 12,
                            top: '80%',
                            x:"center",
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        calculable : true,
                        series: [
                            {
                                name:'小圈圈',
                                type:'pie',
                                center:['50%','40%'],
                                radius:['28%','45%'],
                                avoidLabelOverlap:false,
                                // hoverAnimation:false ,//禁止鼠标悬停放大区域
                                color:['#32A8FF',' #FEB100'],
                                
                                // roseType : 'area',
                                // max: 40,                // for funnel
                                // sort : 'ascending',     // for funnel
                                label:{
                                    show: true,
                                    formatter: '{d}%',
                                    color:'white'
                                },
                                labelLine:{
                                    normal:{
                                        length:14  ,
                                        length2 :24,
                                        lineStyle: {
                                            color: 'white'
                                        }
                                    }
                                    
                                },
                                data:[
                                    {
                                        value:normal,
                                        name:'正常',
                                        // label: {
                                        //     normal: {
                                        //     textStyle: {
                                        //             color: 'white'  // 改变标示文字的颜色
                                        //     }
                                        //     }
                                        // },
                                    itemStyle:{
                                        normal:{
                                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                                //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                        
                                                offset: 0,
                                                color: '#55B9E9'
                                            }, {
                                                offset: 0.5,
                                                color: '#3375AB'
                                            },{
                                                offset: 1,
                                                color: '#0D2561'
                                            }])
                                        }
                                    }},
                                    {
                                        
                                        value:abnormal,
                                        name:'故障',
                                        // label: {
                                        // normal: {
                                        //     textStyle: {
                                        //         color: 'white'  // 改变标示文字的颜色
                                        //         }
                                        //     }
                                        // },
                                        itemStyle:{
                                        normal:{
                                            color:'#FEB100',
                                        }
                                        },
                                    },
                                    
                                
                                ]
                            },
                            {
                                name:'',
                                type:'pie',
                                radius: ['24%','26%'],
                                center:['50%','40%'],
                                itemStyle: {
                                        normal: {
                                        color: '#043B71',
                                        },
                                },
                                hoverAnimation:false ,//禁止鼠标悬停放大区域
                                color:['#bcdbfa'],
                                data:[{value:'100'}]
                            }
                        ]
                    };
                // 使用刚指定的配置项和数据显示图表。
                burglarAlarm.setOption(option);
                // 设置默认突出区域
                burglarAlarm.dispatchAction({
                  type: 'highlight', //突出高亮显示;
                  seriesIndex: 0,
                  dataIndex: 1, //默认索引为0，即10%
                });
        },

    },
}
</script>

<style scoped="scoped" lang="scss">
    .burglarAlarmBox{
        width: 100%;
        height: 100%;
        overflow: hidden;
        #burglarAlarm{
            width: 100%;
            height: 85%;
        }
    }
    
</style>