<template>
    <dv-charts :option="option" class="rc-bottom"/>
</template>

<script>
  import request from '@/utils/request'
  var datav;
  var leftorright = 1;
  function getContent(typeId,tunnelId) {
      return request({
          url: '/statistics/bigscreenChartList?tunnelId='+tunnelId+"&typeId=" + typeId,
          method: 'get'
      })
  }
    export default {
        name: 'TopRightCmp',
        props: ['test'],
        watch: {
          test: {
            deep: true,
            handler(val) {
              this.getList();
            }
          }
        },
        mounted() {
          this.$nextTick(() => {
            this.getList()
          })
        },
        data () {
            return {
                option: {
                    xAxis: {
                        data: [
                            '2点', '4点', '6点', '8点', '10点', '12点', '14点', '16点',
                             '18点', '20点', '22点', '24点'
                        ],
                        axisLine: {
                            style: {
                                stroke: '#999'
                            }
                        },
                        axisLabel: {
                            style: {
                                fill: '#999'
                            }
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    grid: {
                        top: '10',
                    },
                    yAxis: {
                        data: 'value',
                        splitLine: {
                            show: false
                        },
                        axisLine: {
                            style: {
                                stroke: '#999'
                            }
                        },
                        axisLabel: {
                            style: {
                                fill: '#999'
                            },
                            formatter: '{value} ppm'
                        },
                        axisTick: {
                            show: false
                        },
                        min: 0
                    },
                    series: [
                        {
                            data: [
                                5, 5, 5, 5, 5, 5, 5
                            ],
                            type: 'line',
                            lineStyle: {
                                stroke: '#1418ff'
                            },
                            linePoint: {
                                radius: 4,
                                style: {
                                    fill: '#1418ff',
                                    stroke: 'transparent'
                                }
                            }
                        }
                    ]
                },
                dwInfo:""
            }
        },
        created() {
                /* setInterval(this.getList,10000); */
                this.getList();
            },
            methods: {
                getList() {
                    this.loading = true;
                    getContent(this.test.type,this.test.tunnelId).then(response => {
                        datav = response.data;
                        this.updateHandler(this.test.type);
                    });
                },
                //更换方向
                changedic(dic){
                  leftorright=dic;
                  getList();
                  upupdateHandler();
                },
                // 更新数据的示例方法
                updateHandler(type) {
                    const { option } = this;
                    this.option.series[0].data=datav.datas;
                    if(type == 14){
                      this.option.yAxis.axisLabel.formatter = '{value}ppm';
                      this.option.series[0].lineStyle.stroke = '#1418ff';
                      this.option.series[0].linePoint.style.fill = '#1418ff';

                    }else if(type == 15){
                      this.option.yAxis.axisLabel.formatter = '{value}m';
                      this.option.series[0].lineStyle.stroke = '#ffaa00';
                      this.option.series[0].linePoint.style.fill = '#ffaa00';
                    }else if(type == 5){
                      this.option.yAxis.axisLabel.formatter = '{value}cd/㎡';
                      this.option.series[0].lineStyle.stroke = '#00aa7f';
                      this.option.series[0].linePoint.style.fill = '#00aa7f';
                    }else if(type == 6){
                      this.option.yAxis.axisLabel.formatter = '{value}cd/㎡';
                      this.option.series[0].lineStyle.stroke = '#ff0000';
                      this.option.series[0].linePoint.style.fill = '#ff0000';
                    }
                    this.option = { ...this.option }
                }
            }
    }
</script>

<style>
  .top-right-cmp {
    position: relative;
    padding: 0 50px;
    box-sizing: border-box;
  }

  .chart-name {
    position: absolute;
    right: 70px;
    text-align: right;
    font-size: 20px;
    top: 10px;
  }
  .rc-bottom{
    height:23vh;
  }
</style>
