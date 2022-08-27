<template>
    <div>
        <div class="contentTitle">累计报警分析
          <i>Cumulative alarm analysis</i>
        </div>
        <div class="CumulativeBox">
            <div class='CumulativeContent' style="width: 32%;margin-left: 0.3vw;">
                <div class="cumulativeCharts" id="cumulative"></div>
            </div>
            <div class="CumulativeContent" style="width: 69%;">
                <div class="CumulativeList">
                    <div class="listHeader">
                         <ul style="list-style-type: none;">
                            <li style="font-size: 0.8vw;text-align: left;">
                                <span class="data" style="padding-left: 1.5vw;">序号</span>
                                <span class="date" style="padding-left: 5vw;">报警时间</span>
                                <span class="date" style="padding-left: 8vw;">报警类型</span>
                                <span class="date" style="padding-left: 7vw;">处理情况</span>
                            </li>
                         </ul>
                     </div>
                    <vue-seamless-scroll
                      :class-option="defaultOption"
                      class="listContent"
                      :data="faultList"
                    >
                      <!-- <ul style="list-style-type: none;"> -->
                        <el-row v-for="(item, index) in faultList" :key="index" 
                        :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}">
                            <el-col style="width: 7vw;text-align: center;">{{index+1}}</el-col>
                            <el-col style="width: 11vw;text-align: center;">{{item.time}}</el-col>
                            <el-col style="width: 11vw;text-align: center;">{{item.type}}</el-col>
                            <el-col style="width: 9vw;text-align: center;">{{item.Treatment}}</el-col>
                        </el-row>
                      <!-- </ul> -->
                    </vue-seamless-scroll>
                </div>
            </div>
         </div>
    </div>
</template>

<script>
    import *as echarts from 'echarts'   
    export default{
        data() {
          return {
              faultList:[
                  {
                      time:'2021-12-01 10:20',
                      type:'设备故障',
                      Treatment:'已处置',
                  },
                  {
                      time:'2021-12-01 16:34',
                      type:'行人事件',
                      Treatment:'已处置',
                  },
                  {
                      time:'2021-12-01 18:45',
                      type:'停车事件',
                      Treatment:'已处置',
                  },
                  {
                      time:'2021-12-01 19:20',
                      type:'停车事件',
                      Treatment:'已处置',
                  },
                  {
                      time:'2021-12-01 20:56',
                      type:'抛洒物事件',
                      Treatment:'已处置',
                  },
                  {
                      time:'2021-12-01 22:32',
                      type:'行人事件',
                      Treatment:'已处置',
                  },
                  
                  ]
          };
        },
        mounted(){
            this.alarmsCharts()
        },
        computed: {
        	defaultOption() {
        		return {
        			step: 0.2, // 数值越大速度滚动越快
        			limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        			hoverStop: true, // 是否开启鼠标悬停stop
        			direction: 1, // 0向下 1向上 2向左 3向右
        			openWatch: true, // 开启数据实时监控刷新dom
        			singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        			singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        			waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
        		}
        	}
        },
        methods:{
            alarmsCharts() {
                    var Cumulative = echarts.init(document.getElementById('cumulative'));
                    var option = {
                        legend: {
                          orient: 'vertical',
                          show: true,
                          right: '10%',
                          y: 'center',
                          itemWidth: 3,
                          itemHeight: 30,
                          itemGap: 20,
                          textStyle: {
                            color: '#7a8c9f',
                            fontSize: 14,
                            lineHeight: 20,
                            rich: {
                              percent: {
                                color: '#fff',
                                fontSize: 16,
                              },
                            },
                          },
                          formatter: name => {
                            switch (name) {
                              case '故障':
                                return (
                                  '故障\r\n{percent|40%} ' +
                                  ' ' + 40
                                );
                              case '事件':
                                return (
                                  '事件\r\n{percent|30%} ' +
                                  ' ' + 30
                                );
                              case '预警':
                                return (
                                  '预警\r\n{percent|30%} ' +
                                  ' ' + 30
                                );
                              default:
                                break;
                            }
                          },
                        },
                        tooltip: {
                          show: true,
                        },
                         series: [
                          {
                            type: 'pie',
                            radius: ['55%', '65%'],
                            center: ['35%', '50%'],
                            hoverAnimation: false,
                            z: 10,
                            label: {
                              position: 'center',
                              formatter: () => {
                                return '报警总数\r\n{total|100} 个';
                              },
                              rich: {
                                total: {
                                  fontSize: 30,
                                  color: '#fff',
                                },
                              },
                              color: '#7a8c9f',
                              fontSize: 16,
                              lineHeight: 30,
                            },
                            data: [
                              {
                                value: 40,
                                name: '故障',
                                itemStyle: {
                                  color: '#0286ff',
                                },
                              },
                              {
                                value: 30,
                                name: '事件',
                                itemStyle: {
                                  color: '#ffd302',
                                },
                              },
                              {
                                value: 30,
                                name: '预警',
                                itemStyle: {
                                  color: '#fb5274',
                                },
                              },
                            ],
                            labelLine: {
                              show: false,
                            },
                           },
                          {
                            type: 'pie',
                            radius: ['44%', '54%'],
                            center: ['35%', '50%'],
                            hoverAnimation: false,
                            label: {
                              show: false,
                            },
                            data: [
                              {
                                value: 40,
                                name: '故障',
                                itemStyle: {
                                  color: '#0286ff',
                                  opacity: 0.4,
                                },
                              },
                              {
                                value: 30,
                                name: '事件',
                                itemStyle: {
                                  color: '#ffd302',
                                  opacity: 0.4,
                                },
                              },
                              {
                                value: 40,
                                name: '预警',
                                itemStyle: {
                                  color: '#fb5274',
                                  opacity: 0.4,
                                },
                              },
                            ],
                            labelLine: {
                              show: false,
                            },
                          },
                          {
                            type: 'pie',
                            radius: ['33%', '43%'],
                            center: ['35%', '50%'],
                            hoverAnimation: false,
                            label: {
                              show: false,
                            },
                            data: [
                              {
                                value: 40,
                                name: '故障',
                                itemStyle: {
                                  color: '#0286ff',
                                  opacity: 0.1,
                                },
                              },
                              {
                                value: 30,
                                name: '事件',
                                itemStyle: {
                                  color: '#ffd302',
                                  opacity: 0.1,
                                },
                              },
                              {
                                value: 40,
                                name: '预警',
                                itemStyle: {
                                  color: '#fb5274',
                                  opacity: 0.1,
                                },
                              },
                            ],
                            labelLine: {
                              show: false,
                            },
                          },
                        ],
                      
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    Cumulative.setOption(option);
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
    .cumulativeCharts{
        width: 100%;
        height: 100%;
        // border: solid 1px red;
    }
    .CumulativeBox{
        display: flex;
        width: 100%;
        height: calc(100% - 2vw);
        transform: translateY(-0.2vw);
        // border: solid 1px yellowgreen;
        
    }
    .CumulativeContent{
        height: 100%;
        // border: solid 1px white;
        overflow: hidden;
        background-color: rgba(4,15,78,0.5);
    }
</style>
