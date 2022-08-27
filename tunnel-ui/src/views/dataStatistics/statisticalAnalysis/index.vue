<template>
  <div class="dashboard-editor-container">
    <panel-group @handleSetLineChartData="handleSetLineChartData" />
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <dealalarm-chart v-bind:chartData="fireAlarmCount" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <line-chart v-bind:chartData="weekFireAlarmCount" />
        </div>
      </el-col>

    </el-row>
    <el-row :gutter="16">
      <el-col :lg="12">
        <div class="chart-wrapper" style="height:315px">
          <template>
            <el-carousel indicator-position="outside">
              <el-carousel-item v-for="item in fireForcesPower" :key="item.num">
                <h3 style="text-align: center;margin-top: 0px;color:#6b6b6b">{{ item.title }}</h3>
                <el-table :data=item.sbtables style="width: 100%">
                  <el-table-column prop="labels1" :label=item.label1 width="200">
                  </el-table-column>
                  <el-table-column prop="labels2" :label=item.label2 width="125">
                  </el-table-column>
                  <el-table-column prop="labels3" :label=item.label3 width="125">
                  </el-table-column>
                  <el-table-column prop="labels4" :label=item.label4 width="125">
                  </el-table-column>
                  <el-table-column prop="labels5" :label=item.label5 width="125">
                  </el-table-column>
                </el-table>
              </el-carousel-item>
            </el-carousel>
          </template>
        </div>
      </el-col>
      <el-col :lg="12">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import PanelGroup from '../../dataStatistics/statisticalAnalysis/PanelGroup'
  import BarChart from '../../dataStatistics/statisticalAnalysis/BarChart'
  import LineChart from '../../dataStatistics/statisticalAnalysis/LineChart'
  import RaddarChart from '../../dataStatistics/statisticalAnalysis/RaddarChart'
  import PieChart from '../../dataStatistics/statisticalAnalysis/PieChart'
  import DealalarmChart from '../../dataStatistics/statisticalAnalysis/DealalarmChart'
  import { listWarningInfo, getWarningInfo, delWarningInfo, addWarningInfo, updateWarningInfo,fireAlarmCount,fireWeekList} from "@/api/event/warningInfo"
  import {
    getToken,
    getExpiresIn,
    setExpiresIn
  } from '@/utils/auth'

  const lineChartData = {
    newVisitis: {
      expectedData: [100, 120, 161, 134, 105, 160, 165],
      actualData: [120, 82, 91, 154, 162, 140, 145]
    },
    messages: {
      expectedData: [200, 192, 120, 144, 160, 130, 140],
      actualData: [180, 160, 151, 106, 145, 150, 130]
    },
    purchases: {
      expectedData: [80, 100, 121, 104, 105, 90, 100],
      actualData: [120, 90, 100, 138, 142, 130, 130]
    },
    shoppings: {
      expectedData: [130, 140, 141, 142, 145, 150, 160],
      actualData: [120, 82, 91, 154, 162, 140, 130]
    }
  }

  export default {
    name: 'Index',
    components: {
      PanelGroup,
      LineChart,
      RaddarChart,
      PieChart,
      BarChart,
      DealalarmChart
    },
    data() {
      return {
        //刷新token锁
        refreshLock: false,
        //刷新token的时间
        refreshTime: '',
        lineChartData: lineChartData.newVisitis,
        fireAlarmCount:[],
        weekFireAlarmCount: [],
        fireForcesPower: [{
          title: "设备运行情况",
          sbtables: [{
            labels1: '照明灯',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '交通信号灯',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '摄像机',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '卷帘门',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '水泵',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '风机',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }, {
            labels1: '情报板',
            labels2: '100',
            labels3: '1',
            labels4: "1%",
            labels5: "3%"
          }],
          label1: "设备名称",
          label2: "数量",
          label3: "故障数量",
          label4: "故障率",
          label5: "离线时长比率",
          num: 1
        }]
      }
    },
    created() {
      this.refreshToken();
      this.dateRange = [];
      var dd = new Date();
      dd.setDate(dd.getDate() - 6);
      this.dateRange = [dd,new Date()];
      this.getList();
      this.getFireList();
    },
    mounted() {
      /* this.timer = setInterval(function(){
        this.getList();
        this.getFireList();
    	}, 1000); */
    },
    methods: {
      handleSetLineChartData(type) {
        this.lineChartData = lineChartData[type]
      },
      getList() {
       // this.loading = true;
        fireAlarmCount().then(response => {
          this.fireAlarmCount = response.data.unProcessed;
        });
      },
      getFireList() {
         fireWeekList().then(response => {
           this.weekFireAlarmCount = response.data.echartsData;
         });
      },
      // 实时检测刷新token
      refreshToken() {
        this.refreshTime = setInterval(() => {
          if (null === getToken()) {
            return;
          }
          const expires_in = getExpiresIn();
          if (expires_in <= 1000 && !this.refreshLock) {
            this.refreshLock = true
            this.$store
              .dispatch('RefreshToken')
              .catch(() => {
                clearInterval(this.refreshTime)
              });
            this.refreshLock = false
          }
          this.$store.commit("SET_EXPIRES_IN", expires_in - 10);
          setExpiresIn(expires_in - 10);
        }, 10000);
      }
    }
  }
</script>

<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
