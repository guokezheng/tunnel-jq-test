<template>
  <el-row>
    <el-col :span="16" >
      <el-row :gutter="24" style="clear:both;">
        <el-col :span="24">
<!--          <el-button type="info" size="small" icon="el-icon-location-outline"@click="ddddd" class="tunnelNameButton">eee</el-button>-->

            <el-select
              style="width: 10%;    float: right;margin-top: 10px;margin-right: 25px;    z-index: 10;"
              v-model="tunnelIdStr"
              placeholder="请选择隧道"
              clearable
              @change="changeEvent()"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          <div  id="energyAnalyse" ref="energyAnalyse" style="width: 99%; height: 400px;border: 8px solid #004375;"></div>
        </el-col>
      </el-row>
      <el-row :gutter="24" style="clear:both;">
        <el-col :span="24">
          <div  class="energyAnalyseTwo" ref="energyAnalyseTwo" style="width: 99%; height: 400px; float: left;border: 8px solid #004375;"></div>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="8">
      <el-row :gutter="24" style="height: 50vh;border: 8px solid #004375;">
        <el-col :span="24">
          <el-col :span="24">
            <el-row>
              <el-col :span="12">
                <div >
                  <h1 style="color:#4675BA  ;font-weight: bold;font-size: 20px;margin-left: 10px">累计数据</h1>
                  <div style="height: 100px;width: 97%; background-color:#1B5480; ">
                    <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                      <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                    </div>

                    <div style="width:69% ;float:left; margin-top: 10%">
                      <span style="color: #FFFFFF;font-size: 15px;">本年能耗 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="yearElectricity"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                    </div>
                    <div style="width:69% ;float:left;">
                      <span style="color: #FFFFFF;font-size: 15px;"  >对比同期 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="yearComparison"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                    </div>
                  </div>

                </div>

              </el-col>
              <el-col :span="12">
                <div style="height: 100px;width: 97%;margin-top:6vh; background-color:#1B5480;">
                  <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                    <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                  </div>

                  <div style="width:69% ;float:left; margin-top: 10%">
                    <span style="color: #FFFFFF;font-size: 15px;">本年电费 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="cityYearElectricity"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;" >MWH</span>
                  </div>
                  <div style="width:69% ;float:left;">
                    <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"v-html="cityYearhComparison"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row style="margin-top: 3px">
              <el-col :span="12">
                <div >

                  <div style="height: 100px;width: 97%; background-color:#1B5480;">
                    <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                      <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                    </div>

                    <div style="width:60% ;float:left; margin-top: 10%">
                      <span style="color: #FFFFFF;font-size: 15px;">本月能耗 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"  v-html="monthElectricity"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;" >MWH</span>
                    </div>
                    <div style="width:60% ;float:left;">
                      <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="monthComparison"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                    </div>
                  </div>

                </div>

              </el-col>
              <el-col :span="12">
                <div style="height: 100px;width: 97%; background-color:#1B5480;">
                  <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                    <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                  </div>

                  <div style="width:60% ;float:left; margin-top: 10%">
                    <span style="color: #FFFFFF;font-size: 15px;">本月电费 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="cityMonthElectricity"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                  <div style="width:60% ;float:left;">
                    <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"v-html="cityMonthComparison"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row style="margin-top: 3px">
              <el-col :span="12">
                <div >

                  <div style="height: 100px;width: 97%; background-color:#1B5480;">
                    <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                      <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                    </div>

                    <div style="width:60% ;float:left; margin-top: 10%">
                      <span style="color: #FFFFFF;font-size: 15px;">照明电量 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="lightMonthElectricity"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;" >MWH</span>
                    </div>
                    <div style="width:60% ;float:left;">
                      <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                      <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;"v-html="lightMonthComparison"> </span>
                      <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                    </div>
                  </div>

                </div>

              </el-col>
              <el-col :span="12">
                <div style="height: 100px;width: 97%; background-color:#1B5480;">
                  <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                    <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                  </div>

                  <div style="width:60% ;float:left; margin-top: 10%">
                    <span style="color: #FFFFFF;font-size: 15px;">风机电量 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="draughtMonthElectricity"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                  <div style="width:60% ;float:left;">
                    <span style="color: #FFFFFF;font-size: 15px;">对比同期 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="draughtMonthComparison"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-col>
      </el-row>
      <el-row :gutter="24" style="height: 35.4vh;clear:both;border: 8px solid #004375;">


        <el-col :span="24">
          <el-row>
            <el-col :span="12">
              <div >
                <h1 style="color:#4675BA  ;font-weight: bold;font-size: 20px;margin-left: 10px">分类数据</h1>
                <div style="height: 100px;width: 97%; background-color:#1B5480; ">
                  <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                    <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                  </div>

                  <div style="width:60% ;float:left; margin-top: 12%">
                    <span style="color: #FFFFFF;font-size: 15px;">市电 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="cityelectricity"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>

                </div>

              </div>

            </el-col>
            <el-col :span="12">
              <div style="height: 100px;width: 97%;margin-top:6vh; background-color:#1B5480;">
                <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                  <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                </div>

                <div style="width:60% ;float:left; margin-top: 12%">
                  <span style="color: #FFFFFF;font-size: 15px;">光伏自用 </span>
                  <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="lightyelectricity"> </span>
                  <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row style="margin-top: 3px">
            <el-col :span="12">
              <div >

                <div style="height: 100px;width: 97%; background-color:#1B5480;">
                  <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                    <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                  </div>

                  <div style="width:60% ;float:left; margin-top: 12%">
                    <span style="color: #FFFFFF;font-size: 15px;">柴发 </span>
                    <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="lfirewoodElectricity"> </span>
                    <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                  </div>
                </div>

              </div>

            </el-col>
            <el-col :span="12">
              <div style="height: 100px;width: 97%; background-color:#1B5480;">
                <div style="height: 100px;width: 20%; display: flex; align-items: center;margin-left: 30px;float:left; ">
                  <img src="../../../assets/cloudControl/energy-yue.png" width="40px" height="40px" alt="Image">
                </div>

                <div style="width:60% ;float:left; margin-top: 12%">
                  <span style="color: #FFFFFF;font-size: 15px;">风电 </span>
                  <span style="margin-left: 1px;color: #9ACDF0;font-size: 15px;" v-html="windElectricity"> </span>
                  <span style="margin-left: 3px;color: #9ACDF0;font-size: 8px;">MWH</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>

    </el-col>
  </el-row>
</template>

<script>
import * as echarts from "echarts";
import {selectEnergyAnalysisElectricityBillList,getEnergySiteList,getAccumulated,getrealTimeData} from "@/api/energy/api";
import {listList} from "@/api/electromechanicalPatrol/faultManage/fault";
import {listDepId, listTunnels} from "@/api/equipment/tunnel/api";

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
      //本月能耗
      monthElectricity:"",
      //对比同期
      monthComparison:"",
      //本年能耗
      yearElectricity:"",
      //对比同期
      yearComparison:"",
      //本月电费
      cityMonthElectricity:"",
      //城市对比同期
      cityMonthComparison:"",
      //本年电费
      cityYearElectricity:"",
      //城市对比同期
      cityYearhComparison:"",
      //照明电费
      lightMonthElectricity:"",
      //照明对比同期
      lightMonthComparison:"",
      //风机电费
      draughtMonthElectricity:"",
      //风机对比同期
      draughtMonthComparison:"",
      //实时数据市电
      cityelectricity:"",
      //光电
      lightyelectricity:"",
      //柴电
      lfirewoodElectricity:"",
      //风电
      windElectricity:'',
      tunnelData:[],
      tunnelIdStr:"JQ-WeiFang-JiuLongYu-HSD",
      paramsData:{}
    }
  },
  mounted() {
  },
  async created(){
    await listDepId(this.paramsData).then((response) => {
      this.tunnelData = response.rows;
      console.log(this.tunnelData, "隧道列表");
    });
    this.getEchartsData()
    this.getEchartsDataTwo()
    //累计数据计算
    this.getAccumulatedData()
    //实时数据
    this.getrealTimeDataMethods()
  },
  methods:{
    changeEvent(){
      //本月能耗
        this.monthElectricity="",
        //对比同期
        this.monthComparison="",
        //本月能耗
        this.yearElectricity="",
        //对比同期
        this.yearComparison="",
        //本月电费
        this.cityMonthElectricity="",
        //城市对比同期
        this.cityMonthComparison="",

        //本月电费
        this.cityYearElectricity="",
        //城市对比同期
        this.cityYearhComparison="",

        //照明电费
        this.lightMonthElectricity="",
        //照明对比同期
        this.lightMonthComparison="",
        //风机电费
        this.draughtMonthElectricity="",
        //风机对比同期
        this.draughtMonthComparison="",
      this.getEchartsData()
      this.getEchartsDataTwo()
      //累计数据计算
      this.getAccumulatedData()
      //实时数据
      this.getrealTimeDataMethods()
    },
    ddddd(){


      this.getEchartsData()
      this.getEchartsDataTwo()
      //累计数据计算
      this.getrealTimeDataMethods()
    },
    //获取当日用电的数据
    async getEchartsData() {
      // debugger
      //清空上次数据
      this.XDataEnergy = []
      this.yDataEnergy = []
      this.yDataEnergy1 = []
      this.yDataEnergy2 = []
      let queryParams = {}
      //时间
      let dateRange = []
      queryParams.statisticsType = 0
      queryParams.tunnelId = this.tunnelIdStr
      console.log(this.getCurrentDateTime())
      // debugger
      // dateRange.push("2023-08-16 00:00:00")
      // dateRange.push("2023-08-16 23:59:59")
      dateRange.push(this.getCurrentDateTime().endTime)
      dateRange.push(this.getCurrentDateTime().startTime)
      await getEnergySiteList(this.addDateRange(queryParams, dateRange)).then(
        (response) => {
          if(response.code==200){

            let endTime = this.getCurrentDateTime().endTime
            const endTimeDatePart = endTime.split(' ')[0];
            let startTime = this.getCurrentDateTime().startTime
            const startTimeDatePart = startTime.split(' ')[0];
            if(!!response.rows && response.rows.length>0){
              for (let i = 0; i < response.rows.length; i++) {
                if( response.rows[i].createTime.includes(startTimeDatePart)){
                // if( response.rows[i].createTime.includes("2023-08-16")){
                  let  createTime = response.rows[i].createTime
                  const createTimeDatePart = createTime.split(' ')[1];
                  this.XDataEnergy.push(createTimeDatePart)
                  this.yDataEnergy.push(response.rows[i].energyValue)
                }

                if( response.rows[i].createTime.includes(endTimeDatePart)){
                // if( response.rows[i].createTime.includes("2023-08-15")){
                  this.yDataEnergy1.push(response.rows[i].energyValue)
                }
              }
            }
          }
        }
      );

      // this.XDataEnergy = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
      // this.yDataEnergy =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      // this.yDataEnergy1 =[21,32,3,43,2,43,75,23,723,72,723,71,23,23,2,23,34,23,23,62,24,32,32,54,136,34]
      // this.yDataEnergy2 =[21,34,56,32,2,76,78,87,723,72,33,71,32,23,2,23,34,23,23,62,24,32,32,54,136,34]

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
        if (
          this.LoginMychart != null &&
          this.LoginMychart != "" &&
          this.LoginMychart != undefined
        ) {
          // 销毁
          this.LoginMychart.dispose();
        }
        this.LoginMychart = echarts.init(document.getElementById("energyAnalyse"));
        var option = {
          tooltip: {
            trigger: "axis",
            formatter: function (params) {
              var str = params[0].marker ;
              str += params[0].value + "</br>";
              str += params[1].marker ;
              str += params[1].value + "</br>";
              str += params[2].marker ;
              str += params[2].value + "</br>";
              return str;
            }
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
            // data: ["今日用电", "昨日用电", "电费"],
            data: ["今日用电", "昨日用电"],
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
    async getEchartsDataTwo() {
      // debugger
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

      let yDataEnergyOne = [];
      let  queryParams ={}
      queryParams.pageNum =1
      queryParams.pageSize =15
      queryParams.statisticsType = 1
      queryParams.deptCode = this.tunnelIdStr
      const  res = await selectEnergyAnalysisElectricityBillList(queryParams);
      if(res.code == 200 ){
        //设置X轴
        let yDataEnergyOneSon =[]
        if(!!res.rows && res.rows.length>0){
          for (let i = 0; i < res.rows.length; i++) {
            if (!this.XDataEnergyOne.includes(res.rows[i].statisticsDate)) {
              this.XDataEnergyOne.push(res.rows[i].statisticsDate)
              if(yDataEnergyOneSon.length>0){
                yDataEnergyOne.push(yDataEnergyOneSon)
                yDataEnergyOneSon =[]
              }
            }
            yDataEnergyOneSon.push(res.rows[i].value)
            if( res.rows.length==i+1){
              yDataEnergyOne.push(yDataEnergyOneSon)
              yDataEnergyOneSon =[]
            }
          }
        }
      }
      // console.log(yDataEnergyOne)
      // yDataEnergyOne.sort((a, b) => b[0] - a[0]);
      // console.log(yDataEnergyOne)
        this.$nextTick(() => {
          this.initLoginChartTwo(yDataEnergyOne);
        });


    },
    // 获取济南光照照图表数据信息
    initLoginChartTwo(yDataEnergyOne) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart = echarts.init(this.$refs.energyAnalyseTwo);

        // 定义电力分析的五个类别
        var categories = ['尖', '峰', '平', '谷', '深'];

        // 定义柱状图的数据
        // let yDataEnergyOne = [
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
            name: "时段用电kw-h",
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
    formatDateTime(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    },
    getCurrentDateTime() {
      const currentDate = new Date();
      const formattedDate = this.formatDateTime(currentDate);

      const startTime = `${formattedDate} 23:59:59`;

      // 获取当前日期的下一天
      const nextDate = new Date(currentDate);
      nextDate.setDate(nextDate.getDate() -1);
      const formattedNextDate =  this.formatDateTime(nextDate);
      const endTime = `${formattedNextDate} 00:00:00`;

      return { startTime, endTime };
    },
    async getAccumulatedData(){
      let queryParams ={}
      queryParams.tunnelId = this.tunnelIdStr
      await getAccumulated(queryParams).then(
        (response) => {
          if(response.code==200){
            let num = 0
            if(!!response.data){
              for (let key in  response.data) {
                if(key=="本月电费"){
                  this.cityMonthElectricity =response.data[key].split(",")[0]
                  this.cityMonthComparison = response.data[key].split(",")[1]
                }else if(key=="本月能耗"){
                  this.monthElectricity =response.data[key].split(",")[0]
                  this.monthComparison = response.data[key].split(",")[1]
                }else if(key=="照明电量"){
                  this.lightMonthElectricity =response.data[key].split(",")[0]
                  this.lightMonthComparison = response.data[key].split(",")[1]
                }else if(key=="风机电量"){
                  this.draughtMonthElectricity =response.data[key].split(",")[0]
                  this.draughtMonthComparison = response.data[key].split(",")[1]
                } else if(key=="本年电费"){
                  this.cityYearElectricity =response.data[key].split(",")[0]
                  this.cityYearhComparison = response.data[key].split(",")[1]
                }else if(key=="本年能耗"){
                  this.yearElectricity =response.data[key].split(",")[0]
                  this.yearComparison = response.data[key].split(",")[1]
                }
                num = num+1
              }
            }
          }
        }
      );
    },
    async getrealTimeDataMethods(){
      let queryParams ={}
      queryParams.tunnelId = this.tunnelIdStr
      await getrealTimeData(queryParams).then(
        (response) => {
          if(response.code==200){
            let num = 0
            if(!!response.data){
              for (let key in  response.data) {
                if(key=="光伏自用"){
                  this.lightyelectricity =response.data[key]
                }else if(key=="市电"){
                  this.cityelectricity =response.data[key]
                }else if(key=="柴发"){
                  this.lfirewoodElectricity =response.data[key]
                }else if(key=="风发"){
                  this.windElectricity =response.data[key]
                }
                num = num+1
              }
            }
          }
        }
      );
    }

  }
}
</script>

<style scoped>

</style>
