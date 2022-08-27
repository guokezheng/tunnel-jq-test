<template>
  <div class="left-chart-2">
    <div class="rc1-header">设备类型</div>
    <div class="rc1-chart-container">
      <dv-charts class="rc1-right" :option="option" />
    </div>
  </div>
</template>

<script>
    import request from '@/utils/request'
    var datav;
    function getContent(eqTunnelId) {
        return request({
            url: '/devices/bigscreenlist?eqTunnelId='+ eqTunnelId,
            method: 'get'
        })
    }
    export default {
        name: 'RightChart2',
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
                    series: [
                        {
                            type: 'pie',
                            data: [
                                { name: '交通信号灯', value: 93 },
                                { name: '车道指示器', value: 66 },
                                { name: '卷帘门', value: 52 },
                                { name: '风机', value: 34 },
                                { name: '照明', value: 22 },
                                { name: '情报板', value: 34 },
                                { name: '其他', value: 22 }
                            ],
                            radius: ['45%', '65%'],
                            insideLabel: {
                                show: false
                            },
                            outsideLabel: {
                                labelLineEndLength: 10,
                                formatter: '{percent}%\n{name}',
                                style: {
                                    fontSize: 14,
                                    fill: '#fff'
                                }
                            }
                        }
                    ],
                    color: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b', '#4fc8a0', '#a11fbb']
                }
            }
        },
        created() {
/*            setInterval(this.updateHandler,1000); */
            setInterval(this.getList,10000);
            // this.updateHandler();
            // this.getList();
        },
        methods: {
            getList() {
                this.loading = true;
                getContent(this.test).then(response => {
                    datav = response;
                    this.updateHandler();
                });
            },
            // 更新数据的示例方法
            updateHandler () {
                const { option } = this;
                /**
                 * 只是这样做是无效
                 * config指向的内存地址没有发生变化
                 * 组件无法侦知数据变化
                 */
                this.option.series[0].data[0].value = datav[0]; //chezhi
                this.option.series[0].data[1].value = datav[1]; //
                this.option.series[0].data[2].value = datav[2]; //
                this.option.series[0].data[3].value = datav[3]; //
                this.option.series[0].data[4].value = datav[4]; //
                this.option.series[0].data[5].value = datav[5]; //
                this.option.series[0].data[6].value = datav[6]; //
                /**
                 * 使用ES6拓展运算符生成新的props对象
                 * 组件侦知数据变化 自动刷新状态
                 */
                this.option = { ...this.option }
            }
        }
    }
</script>

<style>
  .left-chart-2 {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
  }

  .rc1-header {
    text-align: center;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 25px;
    margin-top: 10px;
    margin-bottom: 5px;
    color:white;
  }

  .rc1-chart-container {
    flex: 1;
    display: flex;
  }

  .rc1-right {
    flex: 1;
    height:22vh;
    padding-bottom: 20px;
    padding-right: 20px;
    box-sizing: border-box;
  }
</style>
