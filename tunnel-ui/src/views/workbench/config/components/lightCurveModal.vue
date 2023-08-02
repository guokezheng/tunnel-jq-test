<template>
  <el-dialog
    class="timedControl"
    custom-class="no-scroll"
    title="光照曲线图"
    :close-on-click-modal="false"
    :visible.sync="visibleSync"
    width="80%"
    :destroy-on-close="false"
    append-to-body
    :lock-scroll="true"
    :before-close="closeLogin"
  >
    <el-row>
      <el-col :span="3"  style="float: right">

        <el-button
          size="mini"
          class="tableBlueButtton"
          @click="submitlightForm"
        >保存</el-button
        >
        <el-button
          size="mini"
          class="tableBlueButtton"
          @click="refreshlightForm"
        >刷新</el-button
        >
      </el-col>
    </el-row>
        <el-row :gutter="24" style="clear:both;margin-top: 5px ">
          <el-col :span="17">

            <el-row :gutter="24" style="clear:both;">
              <el-col :span="24">
                <div  class="loginChart" ref="loginChart" style="width: 100%; height: 400px"></div>
              </el-col>
            </el-row>
            <el-row :gutter="24" style="clear:both;">
              <el-col :span="24">
                <div  class="loginChart1" ref="loginChart1" style="width: 100%; height: 400px; float: left"></div>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="6">
            <el-form
              ref="loginQueryForm"
              :model="lightFilesModel"
              :inline="true"
              class="loginQueryFormClass"
              label-width="100px"
              height="300px"
            >
              <el-row :gutter="24" style="clear:both; ">
                <el-col :span="24">
                  <el-form-item label="隧道名称" prop="tunnelId">
                    <el-select
                      style="width: 100%"
                      v-model="lightFilesModel.tunnelId"
                      placeholder="请选择隧道"
                      clearable
                      @change="lightChangeEvent"
                    >
                      <el-option
                        v-for="item in tunnelData"
                        :key="item.tunnelId"
                        :label="item.tunnelName"
                        :value="item.tunnelId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="24">
                  <el-form-item label="隧道方向" prop="direction">
                    <el-select
                      clearable
                      v-model="lightFilesModel.direction"
                      placeholder="请选择隧道方向"
                      @change="lightChangeEvent"
                      style="width: 100%"
                    >
                      <el-option
                        v-for="dict in lightDirectionOptions"
                        :key="dict.value"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="24">
                  <el-form-item label="状态" align="center" prop="schedulerTime">
                    <template slot-scope="scope">
                      <el-switch
                        v-model="lightFilesModel.isStatus"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-value="0"
                        inactive-value="1"
                        @change="changeCattate()"
                      >
                      </el-switch>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="下修比例"  >
                    <el-input  v-model="lightFilesModel.beforeLuminance"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
          <el-col :span="6" style="margin-top: 10%">
            <el-row :gutter="24">
              <el-col :span="10"  style="float: right">

                <el-button
                  size="mini"
                  class="tableBlueButtton"
                  @click="submitlightForm"
                >保存</el-button
                >
                <el-button
                  size="mini"
                  class="tableBlueButtton"
                  @click="refreshlightForm"
                >刷新</el-button
                >
              </el-col>
            </el-row>
            <el-form
              ref="loginQueryForm"
              :model="lightFilesModel"
              :inline="true"
              class="loginQueryFormClass"
              label-width="100px"
              style="margin-top: 5px"
              height="300px"
            >
              <el-row :gutter="24" style="clear:both; ">
                <el-col :span="24">
                  <el-form-item label="隧道名称" prop="tunnelId">
                    <el-select
                      style="width: 100%"
                      v-model="lightFilesModel.tunnelId"
                      placeholder="请选择隧道"
                      clearable
                      @change="lightChangeEvent"
                    >
                      <el-option
                        v-for="item in tunnelData"
                        :key="item.tunnelId"
                        :label="item.tunnelName"
                        :value="item.tunnelId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="24">
                  <el-form-item label="隧道方向" prop="direction">
                    <el-select
                      clearable
                      v-model="lightFilesModel.direction"
                      placeholder="请选择隧道方向"
                      @change="lightChangeEvent"
                      style="width: 100%"
                    >
                      <el-option
                        v-for="dict in lightDirectionOptions"
                        :key="dict.value"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="clear:both;">
                <el-col :span="24">
                  <el-form-item label="状态" align="center" prop="schedulerTime">
                    <template slot-scope="scope">
                      <el-switch
                        v-model="lightFilesModel.isStatus"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-value="0"
                        inactive-value="1"
                        @change="changeCattate()"
                      >
                      </el-switch>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="下修比例"  >
                    <el-input  v-model="lightFilesModel.beforeLuminance"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
        </el-row>

  </el-dialog>
</template>

<script>
import * as echarts from "echarts";
import {addConfig, listConfig, updateConfig} from "@/api/business/enhancedLighting/app";
import {dataDevicesLogInfoList, dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";
import {listTunnels} from "@/api/equipment/tunnel/api";

export default {
  name: "lightCurveModal",
  data() {
    return {
      visibleSync:false,
      //光照曲线ref
      loginChart: null,
      //光强配置文件
      lightFilesModel:{beforeLuminance:''},
      //光照查询
      queryParamsLight:{},
      //光照查询
      queryParamsLight1:{},
      //光 x  光强
      XDataLight:[],
      //光 y  时间
      yDataLight:[],
      //光 y  时间
      yDataLight1:[],
      //光 y  时间
      yDataLight2:[],

      //光 x  光强
      XDataLightOne:[],
      //光 y  时间
      yDataLightOne:[],
      //光 y  时间
      yDataLightOne1:[],
      //光 y  时间
      yDataLightOne2:[],
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      paramsData:{},
      tunnelData:[],
      //隧道方向
      lightDirectionOptions: [
        {dictLabel:"济南方向",dictValue:"2"},
        {dictLabel:"潍坊方向",dictValue:"1"}
      ],//方向列表
      catDirectionOptions: [
        {dictLabel:"上行",dictValue:"1"},
        {dictLabel:"下行",dictValue:"2"}
      ],
      directionOptions:[],
    }
  },
  mounted() {
  },
  created(){
  },
  methods:{
    closeLogin(){
      this.visibleSync = !this.visibleSync
    },
    // 获取济南光照照图表数据信息
    initLoginChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart = echarts.init(this.$refs.loginChart);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '济南方向洞外亮度',
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
            data: ["前天光强", "昨天光强", "今天光强"],
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
            data: this.XDataLight,
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
            name: "光强",
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
              name: "前天光强",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLight,
            },
            {
              name: "昨天光强",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLight1,
            },
            {
              name: "今天光强",
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
              data: this.yDataLight2,
            },
          ],
        };

        this.LoginMychart.setOption(option);
        window.addEventListener("resize", function () {
          this.LoginMychart.resize();
        });
      });
    },
    // 获取潍坊光照照图表数据信息
    initLoginChart1() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart1 = echarts.init(this.$refs.loginChart1);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '潍坊方向洞外亮度',
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
            data: ["前天光强", "昨天光强", "今天光强"],
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
            data: this.XDataLightOne,
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
            name: "光强",
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
              name: "前天光强",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLightOne,
            },
            {
              name: "昨天光强",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLightOne1,
            },
            {
              name: "今天光强",
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
              data: this.yDataLightOne2,
            },
          ],
        };

        this.LoginMychart1.setOption(option);
        window.addEventListener("resize", function () {
          this.LoginMychart1.resize();
        });
      });
    },
    //光强照明配置查询
    lightListConfig(queryParams){
      debugger
      listConfig(queryParams).then((response) => {
        debugger
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.lightFilesModel.id = rows.id
          this.lightFilesModel.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.lightFilesModel.beforeLuminance =  rows.beforeLuminance
          }else{
            this.lightFilesModel.beforeLuminance =''
          }
        }else{
          this.lightFilesModel.id = ''
          this.lightFilesModel.beforeLuminance =''
        }
        this.$forceUpdate();
      })
    },
    //获取光亮的数据
    getEchartsData(row) {
      debugger
      //清空上次数据
      this.XDataLight = []
      this.yDataLight = []
      this.yDataLight1 = []
      this.yDataLight2 = []
      let querysParamsTab = {}
      if(!!row){
        this.lightFilesModel.tunnelId = row.tunnelId
      }
      querysParamsTab.tunnelId =   this.lightFilesModel.tunnelId
      querysParamsTab.pageNum =  1
      querysParamsTab.pageSize =  999
      //类型 外部测光设备
      querysParamsTab.searchValue =  4
      //根据隧道id获取相应外部测光设备
      dataDevicesLogInfoList(this.addDateRange(querysParamsTab)).then(
        (response) => {
          //返回设备
          let listTab = response.rows;
          if(listTab.length>0){
            if(!!row ){//编辑打开

              this.lightFilesModel.direction =row.direction

              debugger
              let queryParams = {tunnelName:row.tunnelId,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}
              //查询出原有配置并且显示
              this.lightListConfig(queryParams)
            }
            this.queryParamsLight.direction = this.lightFilesModel.direction
            this.queryParamsLight1.direction = this.lightFilesModel.direction
            //济南
            let rows = listTab.find(item => item.direction=="济南方向")
            //设备id
            this.queryParamsLight.deviceId= rows.eqId

            //潍坊
            let rows1 = listTab.find(item => item.direction=="潍坊方向")
            //设备id
            this.queryParamsLight1.deviceId= rows1.eqId


            this.queryParamsLight.pageNum = 1;
            this.queryParamsLight.pageSize = 10;
            this.queryParamsLight.searchValue = 4;

            this.queryParamsLight1.pageNum = 1;
            this.queryParamsLight1.pageSize = 10;
            this.queryParamsLight1.searchValue = 4;
            // 获取当前日期
            let currentDate = new Date();

            // 获取前天日期
            let twoDaysAgo = new Date();
            twoDaysAgo.setDate(currentDate.getDate() - 2);

            // 获取大前天日期
            let threeDaysAgo = new Date();
            threeDaysAgo.setDate(currentDate.getDate() - 1);

            let ds = this.getdate(currentDate)//当前
            let ds1 = this.getdate(twoDaysAgo)//前天
            let ds2 = this.getdate(threeDaysAgo)//昨天
            this.XDataLight = this.generateTimeList()
            this.XDataLightOne =  this.XDataLight
            //查询济南光照曲线
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds1)
            ).then((response) => {
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              for (let i = 0; i < list1.length; i++) {
                //前天
                this.yDataLight.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds2)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              //昨天光强
              for (let i = 0; i < list1.length; i++) {
                this.yDataLight1.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              //今天
              for (let i = 0; i < list1.length; i++) {
                // this.XDataLight.push(list1[i].createTime)
                this.yDataLight2.push(list1[i].data)
              }
              setTimeout(() => {
                this.$nextTick(() => {
                  this.initLoginChart();
                });
              }, 500);
            });

            //查询潍坊光照曲线
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight1, ds1)
            ).then((response) => {
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              for (let i = 0; i < list1.length; i++) {
                //前天
                this.yDataLightOne.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight1, ds2)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              //昨天光强
              for (let i = 0; i < list1.length; i++) {
                this.yDataLightOne1.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight1, ds)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              list1 = this.lightDatafiler(list1)
              //今天
              for (let i = 0; i < list1.length; i++) {
                // this.XDataLightOne.push(list1[i].createTime)
                this.yDataLightOne2.push(list1[i].data)
              }
              debugger
              setTimeout(() => {
                this.$nextTick(() => {
                  this.initLoginChart1();
                });
              }, 500);
            });
          }
        }
      );
    },
    generateTimeList() {
      let timeList = [];
      debugger
      let date = new Date();
      date.setHours(0, 0, 0, 0); // 设置时间为凌晨00:00
      console.log(date.getHours() )
      console.log(date.getMinutes() )
      while (date.getHours() < 24 ) {
        timeList.push("2023-08-01" +" "+this.formatTime(date.getHours(), date.getMinutes()));
        if(this.formatTime(date.getHours(), date.getMinutes())=="00:10"){
          break
        }
        date.setMinutes(date.getMinutes() + 50);
      }
      return timeList;
    },
    formatTime(hours, minutes) {
      let formattedHours = hours.toString().padStart(2, '0');
      let formattedMinutes = minutes.toString().padStart(2, '0');
      return formattedHours + ':' + formattedMinutes;
    },
    lightDatafiler(list1){
      let num = 0
      // 过滤后的列表
      const filteredList =list1.filter((entity, index) => {
        // 检查当前实体时间是否满足每隔5分钟的条件
        if (index === 0) {
          // 第一个实体始终保留
          return true;
        } else {
          const prevTime = new Date(list1[index - num].createTime);
          const currTime = new Date(list1[index].createTime);
          console.log(list1[index - num].createTime)
          console.log(list1[index].createTime)
          console.log(currTime - prevTime)
          const diffInMinutes = (currTime - prevTime) / (1000 * 60); // 计算相邻实体的时间差，单位为分钟
          num = num+1
          if(diffInMinutes >= 5){
            num = 0
          }
          return diffInMinutes >= 5; // 判断时间差是否大于等于5分钟
        }
      });
      return filteredList;
    },
    //光强 修改隧道名称查看不同隧道 光强照明配置
    lightChangeEvent(indextabs) {
      let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.lightFilesModel.tunnelId)
      console.log(tunnel)
      debugger
      let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}

      this.lightListConfig(queryParams)
      this.$forceUpdate()
    },
    /** 查询隧道列表 */
    async getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      await listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    async getDirection() {
      await this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    //光照下修比例保存
    submitlightForm(){
      debugger
      console.log(this.lightFilesModel)
      //模式1 车辆 0光强
      this.lightFilesModel.modeType = 0
      if (!!this.lightFilesModel.id) {
        updateConfig(this.lightFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
        });
      } else {
        addConfig(this.lightFilesModel).then((response) => {
          debugger
          this.lightFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
        });
      }
    },
    //刷新光照图表
    refreshlightForm(){
      console.log(this.lightFilesModel)
      this.getEchartsData()
    },
    //生成日期
    getdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    //生成日期
    getCatdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
  },
  props:{
    show:Boolean,
    tunnelItem: {
      type: Object
    },
    tunnelList: {
      type: Array
    }
  },
  watch:{
    show:{
      async handler(newValue, oldValue){
        debugger
        this.visibleSync = !this.visibleSync
        // //查询主策略
        // this.selectListStrategy()
        //查询隧道列表
        await this.getTunnels()
        //查询方向
        await this.getDirection()
        //
        // this.$nextTick(() => {
        //   this.getEchartsData(true)
        // });
      }
    }
  }
}
</script>

<style scoped>

</style>
