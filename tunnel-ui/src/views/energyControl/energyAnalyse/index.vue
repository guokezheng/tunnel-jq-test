<template>
  <el-row>
    <el-col :span="16" >
      <el-row :gutter="24" style="clear:both;">
        <el-col :span="24">
          <div  class="energyAnalyse" ref="energyAnalyse" style="width: 99%; height: 400px;border: 8px solid #E0ECFC;"></div>
        </el-col>
      </el-row>
      <el-row :gutter="24" style="clear:both;">
        <el-col :span="24">
          <div  class="energyAnalyseTwo" ref="energyAnalyseTwo" style="width: 99%; height: 400px; float: left;border: 8px solid #E0ECFC;"></div>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="8">
      <el-row :gutter="24" style="height: 55vh;border: 8px solid #E0ECFC;">
        <el-col :span="12">
          <div >
            <h1 style="color:#4675BA  ;font-weight: bold;font-size: 25px;margin-left: 10px">实时监控</h1>
            <div style="height: 100px;width: 100%;border:5px solid #73C0DE ">
              <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
              </div>

              <div style="width:60% ;float:left; margin-top: 10%">
                <span style="color: #FFFFFF;font-size: 15px;">本月耗能 </span>
                <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"> 213214</span>
                <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
              </div>
              <div style="width:60% ;float:left;">
                <span style="color: #FFFFFF;font-size: 15px;">本月耗能 </span>
                <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"> 213214</span>
                <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
              </div>
            </div>

          </div>

        </el-col>
        <el-col :span="12">
            <div style="height: 100px;width: 100%;border:5px solid #73C0DE ;margin-top:7.3vh">
              <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
              </div>

              <div style="width:60% ;float:left; margin-top: 10%">
                <span style="color: #FFFFFF;font-size: 15px;">本月电费 </span>
                <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"> 213214</span>
                <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
              </div>
              <div style="width:60% ;float:left;">
                <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"> 213214</span>
                <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
              </div>
            </div>
        </el-col>
      </el-row>
      <el-row :gutter="24" style="clear:both;">
        <el-col :span="24">
<!--          ew-->
        </el-col>
      </el-row>
<!--      <el-row :gutter="24" style="clear:both; margin-top: 13px">-->
<!--        <el-col :span="24">-->
<!--          <div style="height: 130px;width: 100%;border:5px solid #73C0DE ">-->
<!--            <div style="height: 130px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">-->
<!--              <img src="../../../assets/cloudControl/energy-yue.png" width="50px" height="50px" alt="Image">-->
<!--            </div>-->

<!--            <div style="width:70% ;float:left; margin-top:  8%">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">本月耗能 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">MWH</span>-->
<!--            </div>-->
<!--            <div style="width:70% ;float:left; margin-top:5px">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">本月耗能 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">MWH</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--      <el-row :gutter="24" style="clear:both;margin-top: 13px ">-->
<!--        <el-col :span="24">-->
<!--          <div style="height: 130px;width: 100%;border:5px solid #73C0DE ">-->
<!--            <div style="height: 130px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">-->
<!--              <img src="../../../assets/cloudControl/yue-dian.png" width="50px" height="50px" alt="Image">-->
<!--            </div>-->

<!--            <div style="width:70% ;float:left; margin-top:  8%">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">本月电费 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">元</span>-->
<!--            </div>-->
<!--            <div style="width:70% ;float:left; margin-top:5px">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">对比同期 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">元</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--      <el-row :gutter="24" style="clear:both;margin-top: 13px">-->
<!--        <el-col :span="24">-->
<!--          <div style="height: 130px;width: 100%;border:5px solid #73C0DE ">-->
<!--            <div style="height: 130px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">-->
<!--              <img src="../../../assets/cloudControl/yue-guang.png" width="50px" height="50px" alt="Image">-->
<!--            </div>-->

<!--            <div style="width:70% ;float:left; margin-top:  8%">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;"> 本月光伏产能</span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">MWH</span>-->
<!--            </div>-->
<!--            <div style="width:70% ;float:left; margin-top:5px">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">对比同期 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">MWH</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--      <el-row :gutter="24" style="clear:both;margin-top: 13px">-->
<!--        <el-col :span="24">-->
<!--          <div style="height: 130px;width: 100%;border:5px solid #73C0DE ">-->
<!--            <div style="height: 130px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">-->
<!--              <img src="../../../assets/cloudControl/tan-fang.png" width="50px" height="50px" alt="Image">-->
<!--            </div>-->

<!--            <div style="width:70% ;float:left; margin-top:  8%">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;"> 本月碳排放</span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">tCO2e</span>-->
<!--            </div>-->
<!--            <div style="width:70% ;float:left; margin-top:5px">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;"> 对比同期</span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">tCO2e</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--      <el-row :gutter="24" style="clear:both;margin-top: 13px">-->
<!--        <el-col :span="24">-->
<!--          <div style="height: 130px;width: 100%;border:5px solid #73C0DE ">-->
<!--            <div style="height: 130px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">-->
<!--              <img src="../../../assets/cloudControl/yue-tan-fang.png" width="50px" height="50px" alt="Image">-->
<!--            </div>-->

<!--            <div style="width:70% ;float:left; margin-top:  8%">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;"> 本月碳减排</span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">tCO2e</span>-->
<!--            </div>-->
<!--            <div style="width:70% ;float:left; margin-top:5px">-->
<!--              <span style="color: #FFFFFF;font-size: 20px;">对比同期 </span>-->
<!--              <span style="margin-left: 1px;color: #9ACDF0;font-size: 20px;"> 213214</span>-->
<!--              <span style="margin-left: 3px;color: #9ACDF0;font-size: 10px;">tCO2e</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
    </el-col>
  </el-row>
</template>

<script>
import * as echarts from "echarts";
import {dataDevicesLogInfoList, dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";

export default {
  name: "energyAnalyse",
  data() {
    return {
      energyAnalyse:null,
      XDataEnergy:[],
      yDataEnergy:[],
      yDataEnergy1:[],
      yDataEnergy2:[],
      XDataEnergyOne:[],
      yDataEnergyOne:[],
      yDataEnergyOne1:[],
      yDataEnergyOne2:[],
      energyAnalyseTwo:null,
    }
  },
  mounted() {
  },
  created(){
    this.getEchartsData()
    this.getEchartsDataTwo()
  },
  methods:{
    //获取当日用电的数据
    getEchartsData() {
      debugger
      //清空上次数据
      this.XDataEnergy = []
      this.yDataEnergy = []
      this.yDataEnergy1 = []
      this.yDataEnergy2 = []


      // this.XDataEnergyOne = []
      // // y  时间
      // this.yDataEnergyOne = []
      // // y  时间
      // this.yDataEnergyOne1 = []
      // //y  时间
      // this.yDataEnergyOne2 = []
      //刷新数据
      // if(type=="refresh"){
      //   this.lightChangeEvent()
      //   this.lightChangeEventWei()
      // }

      this.XDataEnergy = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
      this.yDataEnergy =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      this.yDataEnergy1 =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      this.yDataEnergy2 =[21,34,56,32,2,76,78,87,723,72,33,71,32,23,2,23,34,23,23,62,24,32,32,54,136,34]

        setTimeout(() => {
          this.$nextTick(() => {
            this.initLoginChart();
          });
        }, 500);

    },
    // 获取当日用电图表数据信息
    initLoginChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart = echarts.init(this.$refs.energyAnalyse);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["今日用电", "昨日用电", "电费"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XDataEnergy,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "当日用电",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              name: "今日用电",
              type: "bar",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataEnergy,
            },
            {
              name: "昨日用电",
              type: "bar",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataEnergy1,
            },
            {
              name: "电费",
              type: "line",
              color: "#FFB200",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataEnergy2,
            },
          ],
        };

        this.LoginMychart.setOption(option);
        window.addEventListener("resize", function () {
          this.LoginMychart.resize();
        });
      });
    },
    //获取光亮的数据
    getEchartsDataTwo() {
      debugger
      //清空上次数据
      // this.XDataEnergy = []
      // this.yDataEnergy = []
      // this.yDataEnergy1 = []
      // this.yDataEnergy2 = []


      this.XDataEnergyOne = []
      // y  时间
      this.yDataEnergyOne = []
      // y  时间
      this.yDataEnergyOne1 = []
      //y  时间
      this.yDataEnergyOne2 = []
      //刷新数据
      // if(type=="refresh"){
      //   this.lightChangeEvent()
      //   this.lightChangeEventWei()
      // }

      this.XDataEnergyOne = ["8-1","8-2","8-3","8-4","8-5","8-6","8-7","8-8","8-9","8-10","8-11","8-12","8-13","8-14","8-15"]
      // this.yDataEnergyOne =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      // this.yDataEnergyOne1 =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      // this.yDataEnergyOne2 =[21,34,56,32,2,76,78,87,723,72,33,71,32,23,2,23,34,23,23,62,24,32,32,54,136,34]

      // 定义柱状图的数据
      // this.yDataEnergyOne = [
      //   [120, 200, 150, 80, 70],  // 第一个时间点的数据
      //   [100, 180, 140, 90, 60],  // 第二个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [80, 160, 130, 100, 50],  // 第三个时间点的数据
      //   [100, 180, 140, 90, 60],  // 第二个时间点的数据
      //   [100, 180, 140, 90, 60],  // 第二个时间点的数据
      //   [100, 180, 140, 90, 60],  // 第二个时间点的数据
      // ];

      setTimeout(() => {
        this.$nextTick(() => {
          this.initLoginChartTwo();
        });
      }, 500);

    },
    // 获取济南光照照图表数据信息
    initLoginChartTwo() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart = echarts.init(this.$refs.energyAnalyseTwo);

        // 定义电力分析的五个类别
        var categories = ['尖', '峰', '平', '谷', '深'];

        // 定义柱状图的数据
        let yDataEnergyOne = [
          [120, 200, 150, 80, 70],  // 第一个时间点的数据
          [100, 180, 140, 90, 60],  // 第二个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [80, 160, 130, 100, 50],  // 第三个时间点的数据
          [100, 180, 140, 90, 60],  // 第二个时间点的数据
          [100, 180, 140, 90, 60],  // 第二个时间点的数据
          [100, 180, 140, 90, 60],  // 第二个时间点的数据
        ];

        // 创建一个颜色数组，分别表示电力类别的准确颜色
        var colors = ['#498ACA', '#70A8DF', '#46B6C9', '#F5B959', '#7ACC8A'];

        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["尖", "峰", "平", "谷", "深"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XDataEnergyOne,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "当日用电",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series:categories.map(function(category, index) {
            return {
              name: category,
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series'
              },
              itemStyle: {
                color: colors[index],
                normal: {
                  borderColor: "white",
                },
              },
              symbol: "circle",
              symbolSize: [7, 7],
              smooth: true,
              barWidth: 20,
              data: yDataEnergyOne.map(function(item) {
                return item[index];
              })
            };
          })
        };

        this.LoginMychart.setOption(option);
        window.addEventListener("resize", function () {
          this.LoginMychart.resize();
        });
      });
    },
  }
}
</script>

<style scoped>

</style>
