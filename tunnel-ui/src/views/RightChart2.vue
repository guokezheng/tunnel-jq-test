<template>
  <div class="right-chart-2">
    <div class="lc1-header"><div class="zuodong-icon" v-on:click="changedic(0)"></div>
    &nbsp;传感数据 - {{direction}}&nbsp;
    <div class="youdong-icon" v-on:click="changedic(1)"></div></div>
    <table class="ctb">
      <tr>
        <td><b>CO浓度</b><br><p id="co">0</p></td>
        <td><b>能见度</b><br><p id="vi">0</p></td>
      </tr>
      <tr>
        <td><b>风速</b><br><p id="ws">0</p></td>
        <td><b>风向</b><br><p id="wd">0</p></td>
      </tr>
      <tr>
        <td><b>洞内亮度</b><br><p id="dn">0</p></td>
        <td><b>洞外亮度</b><br><p id="dw">0</p></td>
      </tr>
    </table>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import { getSensorData } from "@/api/home/home";
  var datav;
  export default {
  data () {
    return {
      direction:'上行',
      leftorright:0
    }
  },
  props: ['test'],
  mounted() {
    this.$nextTick(() => {
      this.getList()
    })
  },
  created() {
              setInterval(this.getList,1000*60);
          },
          methods: {
              getList() {
                  getSensorData(this.test,this.leftorright).then(response => {
                      datav = response.data;
                      this.updateHandler();
                  });
              },
              //更换方向
              changedic(dic){
                this.leftorright=dic;
                getSensorData(this.test,this.leftorright).then(response => {
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
                document.getElementById("co").innerHTML = datav[0];
                document.getElementById("vi").innerHTML = datav[1];
                document.getElementById("ws").innerHTML = datav[2];
                document.getElementById("wd").innerHTML = datav[3];
                document.getElementById("dn").innerHTML = datav[4];
                document.getElementById("dw").innerHTML = datav[5];
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
  .ctb{
    float: left;
    width:80%;
    height: 65%;
    margin-left: 10%;
  }
  .ctb tr{
    text-align: center;
    font-size: 20px;
    color: #48e7ff;
    height: 30%;
  }
  .ctb tr td{
     width: 50%;
     background-color: rgba(21, 48, 81, 0.83);
   }
  .ctb tr td p{
      margin:0 0 0 0 ;
    }
  .cright{
    float: left;
    width:40%;
    height: 100%;
  }
.right-chart-2 {
  width: 100%;
  height: 100%;
  display: flex;
  flex-grow: 0;
  flex-direction: column;}

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



</style>
