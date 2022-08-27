<template>
  <div class="left-chart-1">
    <div class="zuodong-icon"></div>
    <div class="lc1-header">
      <div class="zuodong-icon" v-on:click="changedic(0)"></div>
      &nbsp;今日车流量 - {{direction}}&nbsp;
      <div class="youdong-icon" v-on:click="changedic(1)"></div></div>
    <div class="zuodong-icon"></div>
    <dv-capsule-chart class="lc1-chart" :config="config" />
    <dv-decoration-2 style="height:10px;" />
  </div>
</template>

<script>
 import request from '@/utils/request'
 import { getTrafficVolume } from "@/api/home/home";
 var datav;
export default {
  name: 'LeftChart1',
  mounted() {
    this.$nextTick(() => {
      this.getList()
    })
  },
  data () {
    return {
      config: {
        data: [
          {
            name: '0-6时',
            value: 0
          },
          {
            name: '6-12时',
            value: 0
          },
          {
            name: '12-18时',
            value: 0
          },
          {
            name: '18-24时',
            value: 0
          }
        ],
        colors: ['#00baff', '#3de7c9', '#fff', '#ffc530'],
        unit: '辆',
        showValue: true,
      },
      direction:'上行',
      leftorright:0
    }
  },
  props: ['test'],
  watch: {
    test: {
      deep: true,
      handler(val) {
        this.getList();
      }
    }
  },
  created() {
    setInterval(this.getList,10000);
  },
        methods: {
            getList() {
                getTrafficVolume(this.test,this.leftorright).then(response => {
                    datav = response.data;
                    this.updateHandler();
                });
            },
            //更换方向
            changedic(dic){
              this.leftorright = dic;
              getTrafficVolume(this.test,this.leftorright).then(response => {
                  datav = response.data;
                  this.updateHandler();
                  if(dic == 0){
                    this.direction = "上行";
                  }else if(dic == 1){
                    this.direction = "下行";
                  }
              });
            },
            // 更新数据的示例方法
            updateHandler () {
                const { config } = this;
                this.config.data[0].value=datav[0];
                this.config.data[1].value=datav[1];
                this.config.data[2].value=datav[2];
                this.config.data[3].value=datav[3];
                this.config = { ...this.config }
            }
        }
}
</script>

<style>
  .zuodong-icon{
    background-image: url(../assets/icons/left.png);
    background-size:100% 100%;
    width: 10px;
    height: 10px;
}
  .youdong-icon{
    background-image: url(../assets/icons/right.png);
    background-size:100% 100%;
    width: 10px;
    height: 10px;
  }
.left-chart-1 {
  width: 100%;
  height: 37%;
  display: flex;
  flex-grow: 0;
  flex-direction: column;
 }

  .lc1-header {
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

  .lc1-details {
    height: 50px;
    font-size: 16px;
    display: flex;
    align-items: center;
    text-indent: 20px;}



  .lc1-chart {
    padding-left: 20px;
    width: 95%;
    height: 15vh;
  }
</style>
