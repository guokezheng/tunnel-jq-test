<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" >
    
      <el-form-item label="所属隧道">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道"  size="small">
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item >
        <el-radio-group v-model="pickerType" style="margin-right: 15px" @change="changeDate()">
          <el-radio-button label="energyConsumption">能耗统计</el-radio-button>
          <el-radio-button label="carbonEmission">碳排放量</el-radio-button>
          <el-radio-button label="contrast">同比/环比</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item >
        <el-radio-group v-model="pickerTime" style="margin-right: 15px" @change="changeDate()" 
              v-if="pickerType != 'contrast'">
          <el-radio-button label="year">年</el-radio-button>
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="date">日</el-radio-button>
          <el-radio-button label="timeFrame">时间范围</el-radio-button>
          
        </el-radio-group>
      </el-form-item>
      <el-form-item >
            <el-date-picker v-if="pickerTime == 'date'" type="date" size="small" v-model="dateTime"
            	placeholder="选择日期"  @change="changeDate" key='day' :picker-options="pickerOptions"> 
            </el-date-picker>
            <el-date-picker v-if="pickerTime == 'month'" type="month" size="small" v-model="monthTime"
            	placeholder="选择月"  @change="changeDate" key='month'>
            </el-date-picker>
            <el-date-picker v-if="pickerTime == 'year'" type="year" size="small" v-model="yearTime"
            	placeholder="选择年" @change="changeDate" key='year'>
            </el-date-picker>
            <el-date-picker
              v-if="pickerTime == 'timeFrame'"
              v-show="timeFrameShow"
              v-model="forecastTime"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              unlink-panels
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
              @change="changeDate"
              key='time'
            ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row >
      <el-col :span="6" style="margin:0px 20px 20px 40px" v-show="pickerType == 'contrast'">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">同比</span>
          </div>
          <div class="body">
            <div id="comparedSame"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" style="margin:0px 40px 20px 20px" v-show="pickerType == 'contrast'">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">环比</span>
          </div>
          <div class="body">
            <div id="sequential"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="13" style="margin-right:40px" v-show="pickerType == 'energyConsumption'">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">能耗统计</span>
          </div>
          <div class="body">
            <div id="energyConsumptionStatistics"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="13" style="margin-right:40px" v-show="pickerType == 'carbonEmission'">
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">碳排放量</span>
          </div>
          <div class="body">
            <div id="carbonEmission"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10" >
        <el-card class="phoneStatistics">
          <div class="title">
            <div class="preposition"></div>
            <span class="name">排名</span>
          </div>
          <div class="body">
            <div class="ranking">
              <el-table v-loading="loading" :data="rankingList" >
                <el-table-column label="排名" align="center" type="index"/>
                <el-table-column label="配电箱ID" align="center" prop="powerDistributionBox" width="150" />
                <el-table-column label="设备类型" align="center" prop="equipmentType" />
                <el-table-column label="碳排放量" align="center" prop="carbonEmission" sortable>
                  <template slot-scope="scope">
                    <div>{{scope.row.carbonEmission}}千吨</div>
                  </template>
                </el-table-column>
                <el-table-column label="耗电量" align="center" prop="powerConsumption" sortable>
                  <template slot-scope="scope">
                    <div>{{scope.row.powerConsumption}}kW·h</div>
                  </template>
                </el-table-column>
                <el-table-column label="同比" align="center" prop="comparedSame" width="80" sortable>
                  <template slot-scope="scope">
                    <div>{{scope.row.comparedSame}}%</div>
                  </template>
                </el-table-column>
                <el-table-column label="环比" align="center" prop="sequential" width="80" sortable>
                  <template slot-scope="scope">
                    <div>{{scope.row.sequential}}%</div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-card>
      </el-col>
      
    </el-row>
      
  </div>
</template>

<script>
  import { listTunnels } from "@/api/equipment/tunnel/api";
  import moment from "moment";
  import * as echarts from "echarts";
  import $ from "jquery";
  export default {
    name:'energyConsumptionAnalysis',
    data(){
      return{
        rankingList:[
          {
            carbonEmission:'5.88',
            powerDistributionBox:'STG01-364-832821',
            equipmentType:'照明设备',
            powerConsumption:'666',
            comparedSame:'4.2',
            sequential:'6.3'
          },
          {
            carbonEmission:'3.27',
            powerDistributionBox:'STG01-364-648294',
            equipmentType:'照明设备',
            powerConsumption:'630',
            comparedSame:'2.2',
            sequential:'6.3'
          },
        ],
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        dateTime:new Date(),
        monthTime: new Date(),
        yearTime: new Date(),
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          tunnelId: '',
          energyConsumptionType: '',
          equipmentType:''
        },
        energyConsumptionTypeList:[
          {
            ecTypeId:1,
            ecTypeName:'水',
          },
          {
            ecTypeId:2,
            ecTypeName:'电',
          },
        ],
        equipmentTypeList:[
          {
            eqTypeId:1,
            eqTypeName:'动力设备',
          },
          {
            eqTypeId:2,
            eqTypeName:'照明设备',
          },
          {
            eqTypeId:3,
            eqTypeName:'监控设备',
          },
          {
            eqTypeId:4,
            eqTypeName:'消防设备',
          },
        ],
        // 时间绑定
        pickerType:'energyConsumption',
        pickerTime:'year',
        // 统计时间
        forecastTime:[],
        // 设备id 下拉框
        equipmentData:[],
        // 所属隧道
        tunnelData:[],
        // 时间范围
        timeFrameShow:false,
        // 两天前的日期
        moment:'',
        pickerOptions:{
          onPick: (obj) => {
            this.pickerMinDate = new Date(obj.minDate).getTime();
            if(obj.maxDate && obj.minDate){
              if(Date.parse(obj.minDate) == Date.parse(obj.maxDate)){
                this.$modal.msgWarning('不要选择同一天,请重新选择')
                return
              }
            }
          },
          disabledDate:(time)=> {
              if (this.pickerMinDate) {
                  const day1 =  366 * 24 * 3600 * 1000
                  let maxTime = this.pickerMinDate + day1
                  let minTime = this.pickerMinDate - day1
                  return time.getTime() > maxTime || time.getTime()<minTime || time.getTime() > Date.now()+0 * 24 * 3600 * 1000
              }else{
                  return time.getTime() > Date.now()+0 * 24 * 3600 * 1000
              }
              
          },
        },
        charts:[
          {
            name:'动力设备',
            arr:[50,60],
          },
          {
            name:'照明设备',
            arr:[70,60]
          },
          {
            name:'监控设备',
            arr:[90,40]
          },
          {
            name:'消防设备',
            arr:[50,80]
          },
        ],
        energyConsumptionStatistics:null,
        sequential:null,
        comparedSame:null,
      }
    },
    created() {
      this.getTunnel()
    },
    mounted() {
      this.comparedSameCharts()
      this.sequentialCharts()
      this.energyConsumptionCharts()
      // this.carbonEmissionCharts()
    },
    methods:{
      /** 所属隧道 */
      getTunnel() {
        listTunnels().then(response => {
          this.tunnelData = response.rows;
          this.queryParams.tunnelId = response.rows[0].tunnelId
        });
      },
      changeDate(){
        if(this.pickerTime == 'timeFrame'){
          this.timeFrame = true
        }
        if(this.pickerType != 'contrast')
        this.energyConsumptionCharts(this.pickerType)
      },
      getTime(){
          this.forecastTime = []
          let times = moment(new Date()).format("YYYY-MM-DD")
          let yesTime = Date.parse(new Date())-172800000
          let yesTimes = moment(new Date(yesTime)).format("YYYY-MM-DD")
         
          this.queryParams.params = {};
          this.queryParams.params["beginChangeTime"] = yesTimes
          this.forecastTime.push(yesTimes)
         
          this.queryParams.params["endChangeTime"] = times
          this.forecastTime.push(times)
         
          // this.getList()
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
      },
      /** 重置按钮操作 */
      resetQuery() {
          this.queryParams = {
            pageNum: 1,
            pageSize: 10,
            tunnelId: '',
            energyConsumptionType: '',
            equipmentType:''
          }
      },
      // 同比
      comparedSameCharts(){
        let newPromise = new Promise((resolve) => {
        		resolve()
        	})
        	//然后异步执行echarts的初始化函数
          newPromise.then(() => {
              this.comparedSame = echarts.init(document.getElementById('comparedSame'));
                var option = {
                  tooltip: {
                    trigger: 'axis',
                    formatter: function (value) {
                      var ratio = 0
                      var marker = ''
                      if(value[2].value != '-'){
                        ratio = ((value[2].value/value[0].value)*100).toFixed(2)
                        marker = value[2].marker
                      }else if(value[3].value != '-'){
                        ratio = -((value[3].value/value[0].value)*100).toFixed(2)
                        marker = value[3].marker
                      }
                      var str = ''
                        str += marker
                      	str += value[0].name + '<br>'
                        str += value[0].seriesName + "：" +value[0].value + '<br>'
                        str += value[1].seriesName + "：" +value[1].value + '<br>'
                        str += '同比：'+ratio +'%'
                      return str;
                    },
                    axisPointer: {
                      type: 'shadow'
                    }
                  },
                  grid: {
                    top: "20%",
                    left: "15%",
                    bottom: "15%",
                  },
                  legend: {
                    data: ['同比上涨','同比下降' ]
                  },
                  xAxis: [
                    {
                      type: 'category',
                      axisTick: { show: false },
                      data: ['动力设备', '照明设备', '监控设备', '消防设备']
                    }
                  ],
                  yAxis: [
                    {
                      type: 'value'
                    }
                  ],
                  series: [
                        {
                          name: '2021年',
                          type: 'bar',
                          stack: 'Total',
                          show: true,
                          itemStyle: {
                            borderColor: 'transparent',
                            color: 'transparent'
                          },
                          emphasis: {
                            itemStyle: {
                              borderColor: 'transparent',
                              color: 'transparent'
                            }
                          },
                          data: [20, 32, 65,87]
                        },
                        {
                          name: '2022年',
                          type: 'bar',
                          stack: 'Total',
                          show: true,
                          itemStyle: {
                            borderColor: 'transparent',
                            color: 'transparent'
                          },
                          emphasis: {
                            itemStyle: {
                              borderColor: 'transparent',
                              color: 'transparent'
                            }
                          },
                          data: [25, 44, 73,82]
                        },
                        {
                          name: '同比上涨',
                          type: 'bar',
                          barWidth: 30,
                          stack: 'Total',
                          label: {
                            show: true,
                            position: 'top'
                          },
                          data: [5, 12, 8, '-',]
                        },
                        {
                          name: '同比下降',
                          type: 'bar',
                          barWidth: 30,
                          stack: 'Total',
                          label: {
                            show: true,
                            position: 'top'
                          },
                          data: ['-', '-', '-', 5]
                        }
                  ]
                };
                
                this.comparedSame.setOption(option)
        })
      },
      // 环比
      sequentialCharts(){
        let newPromise = new Promise((resolve) => {
        		resolve()
        	})
        	//然后异步执行echarts的初始化函数
          newPromise.then(() => {
            this.sequential = echarts.init(document.getElementById('sequential'));
                var option = {
                  tooltip: {
                    trigger: 'axis',
                    formatter: function (value) {
                      console.log(value)
                      var ratio = 0
                      var marker = ''
                      if(value[2].value != '-'){
                        ratio = ((value[2].value/value[0].value)*100).toFixed(2)
                        marker = value[2].marker
                      }else if(value[3].value != '-'){
                        ratio = -((value[3].value/value[0].value)*100).toFixed(2)
                        marker = value[3].marker
                      }
                      var str = ''
                        str += marker
                      	str += value[0].name + '<br>'
                        str += value[0].seriesName + "：" +value[0].value + '<br>'
                        str += value[1].seriesName + "：" +value[1].value + '<br>'
                        str += '环比：'+ratio +'%'
                      return str;
                    },
                    axisPointer: {
                      type: 'shadow'
                    }
                  },
                  grid: {
                    top: "20%",
                    left: "15%",
                    bottom: "15%",
                  },
                  legend: {
                    data: ['环比上涨','环比下降' ]
                  },
                  xAxis: [
                    {
                      type: 'category',
                      axisTick: { show: false },
                      data: ['动力设备', '照明设备', '监控设备', '消防设备']
                    }
                  ],
                  yAxis: [
                    {
                      type: 'value'
                    }
                  ],
                  series: [
                        {
                          name: '2021年',
                          type: 'bar',
                          stack: 'Total',
                          show: true,
                          itemStyle: {
                            borderColor: 'transparent',
                            color: 'transparent'
                          },
                          emphasis: {
                            itemStyle: {
                              borderColor: 'transparent',
                              color: 'transparent'
                            }
                          },
                          data: [20, 32, 65,87]
                        },
                        {
                          name: '2022年',
                          type: 'bar',
                          stack: 'Total',
                          show: true,
                          itemStyle: {
                            borderColor: 'transparent',
                            color: 'transparent'
                          },
                          emphasis: {
                            itemStyle: {
                              borderColor: 'transparent',
                              color: 'transparent'
                            }
                          },
                          data: [25, 44, 73,82]
                        },
                        {
                          name: '环比上涨',
                          type: 'bar',
                          barWidth: 30,
                          stack: 'Total',
                          label: {
                            show: true,
                            position: 'top'
                          },
                          data: [5, 12, 8, '-',]
                        },
                        {
                          name: '环比下降',
                          type: 'bar',
                          barWidth: 30,
                          stack: 'Total',
                          label: {
                            show: true,
                            position: 'top'
                          },
                          data: ['-', '-', '-', 5]
                        }
                  ]
                };
                this.sequential.setOption(option)
          })
      },
      // 能耗统计
      energyConsumptionCharts(pickerType){
        let newPromise = new Promise((resolve) => {
        		resolve()
        	})
        	//然后异步执行echarts的初始化函数
        	newPromise.then(() => {
            var energyConsumptionStatistics = echarts.init(document.getElementById('energyConsumptionStatistics'));
            var carbonEmission = echarts.init(document.getElementById('carbonEmission'));
                var name = ''
                var chartsArr = []
                for(var item of this.charts){
                  name = item.name
                  chartsArr = item.arr
                }
                const posList = [
                  'left',
                  'right',
                  'top',
                  'bottom',
                  'inside',
                  'insideTop',
                  'insideLeft',
                  'insideRight',
                  'insideBottom',
                  'insideTopLeft',
                  'insideTopRight',
                  'insideBottomLeft',
                  'insideBottomRight'
                ];
                app.configParameters = {
                  rotate: {
                    min: -90,
                    max: 90
                  },
                  align: {
                    options: {
                      left: 'left',
                      center: 'center',
                      right: 'right'
                    }
                  },
                  verticalAlign: {
                    options: {
                      top: 'top',
                      middle: 'middle',
                      bottom: 'bottom'
                    }
                  },
                  position: {
                    options: posList.reduce(function (map, pos) {
                      map[pos] = pos;
                      return map;
                    }, {})
                  },
                  distance: {
                    min: 0,
                    max: 100
                  }
                };
                app.config = {
                  rotate: 90,
                  align: 'left',
                  verticalAlign: 'middle',
                  position: 'insideBottom',
                  distance: 15,
                  onChange: function () {
                    const labelOption = {
                      rotate: app.config.rotate,
                      align: app.config.align,
                      verticalAlign: app.config.verticalAlign,
                      position: app.config.position,
                      distance: app.config.distance
                    };
                    energyConsumptionStatistics.setOption({
                      series: [
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        }
                      ]
                    });
                    carbonEmission.setOption({
                      series: [
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        },
                        {
                          label: labelOption
                        }
                      ]
                    });
                  }
                };
                const labelOption = {
                  show: true,
                  position: app.config.position,
                  distance: app.config.distance,
                  align: app.config.align,
                  verticalAlign: app.config.verticalAlign,
                  rotate: app.config.rotate,
                  formatter: '{c}  {name|{a}}',
                  fontSize: 16,
                  rich: {
                    name: {}
                  }
                };
                var option = {
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                      type: 'shadow'
                    }
                  },
                  legend: {
                    data: pickerType == 'carbonEmission'?['动力设备', '照明设备', '监控设备', '消防设备','总排放量']:['动力设备', '照明设备', '监控设备', '消防设备','总耗电量']
                  },
                
                  xAxis: [
                    {
                      type: 'category',
                      axisTick: { show: false },
                      data: ['3.13', '3.14', '3.15', '3.16', '3.17']
                    }
                  ],
                  yAxis: [
                    {
                      name:pickerType == 'carbonEmission'?'排放量（千吨）':'耗电量（kw-h)',
                      
                      type: 'value',
                      minInterval:1,
                      // max: 3000,
                      axisTick: {
                        show: false
                      },
                      splitLine: {
                        show: false
                      },
                      axisLine: {
                          show: true,
                          },
                      axisLabel: {
                        textStyle: {
                          fontSize: 12
                        }
                      },
                    }, {
                      name:pickerType == 'carbonEmission'?'总排放量（千吨）':'总耗电量（kw-h)',
                      type: 'value',
                      minInterval:1,
                      axisTick: {
                        show: false
                      },
                      splitLine: {
                        show: false
                      },
                      axisLine: {
                          show: true,
                          lineStyle: {
                          color: '#aa55ff'
                          }
                          },
                      axisLabel: {
                        textStyle: {
                          color: '#aa55ff',
                          fontSize: 12
                        }
                      },
                    }
                  ],
                  series: [
                    {
                      name: '动力设备',
                      type: 'bar',
                      barGap: 0,
                      label: labelOption,
                      emphasis: {
                        focus: 'series'
                      },
                      data: [620, 232, 101, 153, 290]
                    },
                    {
                      name: '照明设备',
                      type: 'bar',
                      label: labelOption,
                      emphasis: {
                        focus: 'series'
                      },
                      data: [440, 340, 280, 300, 390]
                    },
                    {
                      name: '监控设备',
                      type: 'bar',
                      label: labelOption,
                      emphasis: {
                        focus: 'series'
                      },
                      data: [98, 432, 300, 334, 40]
                    },
                    {
                      name: '消防设备',
                      type: 'bar',
                      label: labelOption,
                      emphasis: {
                        focus: 'series'
                      },
                      data: [150, 182, 302, 234, 190]
                    },
                    {
                      name: pickerType == 'carbonEmission'?'总排放量':'总耗电量',
                      yAxisIndex: 1,
                      type: 'line',
                      symbolSize: 5,
                      symbol: 'circle',
                      itemStyle: {
                        normal: {
                          color: '#aa55ff',
                          barBorderRadius: 0,
                        }
                      },
                      data: [1388,1000,760,800,710]
                    }
                  ]
                };
                energyConsumptionStatistics.setOption(option)
                carbonEmission.setOption(option)
          })
      },
      
    }
  }
</script>

<style scoped lang="scss">
  #comparedSame,#sequential,{
      width: 410px;
      height: 270px;
      >div{
          width: 410px;
          height: 270px;
      }
  }
  #energyConsumptionStatistics,#carbonEmission{
    width: 880px;
    height: 500px;
    >div{
      width: 1000px;
        height: 500px;
    }
  }
  .ranking{
    width: 880px;
    height: 500px;
  }
  .phoneStatistics {
    position: relative;
    .title {
      padding: 0 0 15px 0;
      font-size: 18px;
      font-weight: 700;
      position: relative;
      .preposition {
        background-color: #3e7deb;
        width: 6px;
        height: 25px;
        position: absolute;
        left: -15px;
        top: 0;
      }
    }
    .body {
      display: flex;
      justify-content: center;
    }
  }
</style>
