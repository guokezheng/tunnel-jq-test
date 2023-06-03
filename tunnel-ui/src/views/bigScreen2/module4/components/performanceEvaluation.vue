<template>
  <div class="box">
      <div class="content">
          <div class="left">
              <div>文一路隧道</div>
              <div class="num">-</div>
              <div>评价分值</div>
              <div class="num">95.32</div>
              <div>评分等级</div>
              <div class="num">1 级</div>

          </div>
          <div class="zong_shu">
              <div id="duoChart2" ref="echartsA" style="width: 100%; height: 100%;"></div>
              <div class="zong_shu_item1" v-for="item in chuNenList" :key="item.id" :style="{'left':item.le,'top':item.to}">
              <span>{{ item.name }}</span>
              <!-- <span style="margin-left: 0.3vw;color: #fff;font-size: 14px;"> {{ item.num }}</span>
              <span style="margin-left: 0.3vw;color: #fff;font-size: 12px;">{{ item.unit }}</span> -->
              </div>
          </div>
      </div>
  </div>
</template>
<script>
import * as echarts from "echarts";
export default {
  data(){
      return{
          selectModel:1,
          selectList:[
              {
                  id:1,
                  name:'2020-01-05 全寿命评价'
              }
          ],
          duoChart1: '',
          chuNenList: [
              { id: 1, name: '土建结构1级 93',  le: '7%', to: '46%' },
              { id: 2, name: '附属设备1级 96',  le: '38%', to: '81%' },
              { id: 3, name: '机电系统1级 98.1',  le: '38%', to: '9%' },
              { id: 4, name: '运行服务1级 96.9',  le: '73%', to: '46%' },
          ]
      }
  },
  mounted() {
      this.openChart()
  },
  methods: {
      openChart () {
      this.$nextTick(function () {
          if (this.duoChart1 != null && this.duoChart1 !== '' && this.duoChart1 !== undefined) {
          // 销毁
          this.duoChart1.dispose()
          }
          this.duoChart1 = echarts.init(document.getElementById("duoChart2"));
          let splitColor = "rgba(169, 169, 169, 0.50)";
        let nameList = ["湿度", "充电", "SOC", "放电"]; // 名字
        let colorList = ['rgba(41, 151, 230, 1)','rgba(55, 231, 255, 1)','rgba(62, 192, 153, 1)','rgba(239, 175, 76, 1)']
        let valueList = [98.1,93,96,96.9]; //最大值为100
        // let valueList1 = [55, 50, 75, 55, 39]; // 最大值为100

        let indicatorList = [];
        nameList.map((item, index) => {
          indicatorList.push({
            name: item,
            value: valueList[index],
            max: 120,
            color: colorList[index],
          });
        });

        let option = {
          radar: {
            center: ["50%", "55%"],
            radius: "50%",
            indicator: indicatorList,
            name: {
              textStyle: {
                fontSize: 10,
                color: "rgba(230, 230, 230, 0.50)",
                padding: [0, -5, -10, 0],
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: "rgba(169, 169, 169, .5)", // 设置网格的颜色
              },
            },
            axisLine: {
              //指向外圈文本的分隔线样式
              lineStyle: {
                color: "rgba(169, 169, 169, .5)",
              },
            },
            splitArea: {
              show: false,
            },
          },
          tooltip: {
            type: "item",
            backgroundColor: "rgba(1, 29, 63, .8)", //设置背景颜色
            textStyle: {
              color: "#fff",
              fontSize: 12,
            },
            borderColor: "rgba(1, 29, 63,.8)",
            axisPointer: {
              type: "shadow",
              shadowStyle: {
                fontSize: 12,
                color: "rgba(0, 11, 34, 0)",
              },
            },
          },
          series: [
            {
              name: "文一路隧道",
              type: "radar",
              symbol: "emptyCircle",
              symbolSize: 5,
              color: "rgba(41, 151, 230, 1)",
              label: {
                show: false,
              },
              areaStyle: {
                normal: {
                  color: "rgba(41, 151, 230, 1)",
                  opacity: 0.4,
                },
              },
              lineStyle: {
                width: 1,
                color: "rgba(41, 151, 230, 1)",
              },
              data: [
                {
                  value: valueList,
                },
              ],
            },
          ],
        };
          this.duoChart1.setOption(option)
      })
      }
  },
}
</script>
<style scoped lang="scss">
  .box{
      height: calc(100% - 30px);
      .bigScreenSelect{
          float: right;
          padding-top: 4px;
          ::v-deep .el-input--small .el-input__inner{
              height:28px !important;
              line-height: 28px !important;
              background-color: #051526 !important;
              border: solid 1px #0C4B72 !important;
              color:#9ba0bc !important;
          }
      }
      .content{
          width:100%;
          height:calc(100% - 30px);
          display: flex;
          .left{
              width: 30%;
              height:100%;
              text-align: center;
              padding-top:40px;
              >div{
                  height:11%;
                  color:#9ba0bc;
              }
              .num{
                  font-size:20px;
                  color:#2997E6;
                  height:18% !important;
              }
              
          }
          .zong_shu {
              width: 100%;
              height: 100%;
              position: relative;
              .zong_shu_item1 {
                  width: 3vw;
                  height: 4vh;
                  position: absolute;
                  display: flex;
                  align-items: center;
                  word-wrap: break-word;
                  border: solid 1px #0C4B72;
                  background-color: #051526;
                  border-top-right-radius: 8px;
                  border-bottom-left-radius: 8px;
                  padding:2px;
                  color:#9ba0bc;
                  font-size: 12px;
              }
          }
      }
  }
</style>