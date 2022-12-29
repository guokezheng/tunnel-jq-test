<!-- 全景数据 - 电力数据-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" style="height:100%;">
        <el-card class="box-card">
          <circuit-tree height="calc(100vh - 225px)" @defaultSelect="defaultSelectLoop" @nodeClick="handleClick" :default_check_first="true" :default_select_first="true"></circuit-tree>
        </el-card>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-tabs type="border-card" v-model="activeName" @tab-click="clickzhhuji" class="elemonitorTab">
          <el-tab-pane label="日原始数据" name="first">
            <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
              <el-form-item label="日期" prop="date">
                <el-date-picker v-model="riYuanShiDate" type="date" value-format="yyyy-MM-dd" :clearable="false" placeholder="选择日期" @change="changeTime" size="mini">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="电力类别" prop="powerType">
                <el-select v-model="queryParams.powerType" placeholder="请选择电力类别" filterable size="mini" :clearable="false" style="width: 100%;" @change="clickRightCheck($event)">
                  <el-option v-for="item in powerTypeOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <!-- <el-checkbox-group v-model="cityOptions">
                  <el-checkbox v-for="(item,index) in cityOptionsItem" value="" :label="item" :key="index">{{item}}</el-checkbox>
                </el-checkbox-group> -->
              </el-form-item>
              <el-form-item>
                <div class="div-bottom">
                  <el-button type="primary" plain icon="el-icon-search" @click="handleQuery" size="mini">查询</el-button>
                  <el-button style="margin-left:0.5rem;" @click="dcPower" plain type="primary" icon="el-icon-download" size="mini">导出 </el-button>
                </div>
              </el-form-item>
            </el-form>
            <div>
              <powerECharts :cityOptionsTable="cityOptionsTable" :riEChartsList="riEChartsList" :cityOptionsValue="cityOptionsValue" />
            </div>
            <div style="margin-top:1.5vh;">
              <br />
              <el-table v-loading="loading" :data="tableDtaList" style="overflow-x: auto;overflow-y: auto;" height="35vh">
                <el-table-column label="回路名称" align="center" prop="huiLuName" min-width="200" />
                <el-table-column label="采集时间" align="center" prop="aTime" min-width="200" />
                <el-table-column v-if="labelA !== ''" :label="`${labelA}`" align="center" prop="aValue" min-width="200" />
                <el-table-column v-if="labelB !== ''" :label="`${labelB}`" align="center" prop="bValue" min-width="200" />
                <el-table-column v-if="labelC !== ''" :label="`${labelC}`" align="center" prop="cValue" min-width="200" />
                <el-table-column v-if="labelD !== ''" :label="`${labelD}`" align="center" prop="dValue" min-width="200" />
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="逐日极值数据" name="second">
            <el-form :model="queryParamsDay" ref="queryForm" :inline="true" label-width="68px">
              <el-form-item label="开始日期" prop="date">
                <el-date-picker v-model="startDate" type="date" value-format="yyyy-MM-dd" :clearable="false" placeholder="选择日期" size="mini">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="结束日期" prop="date">
                <el-date-picker v-model="endDate" type="date" value-format="yyyy-MM-dd" :clearable="false" placeholder="选择日期" size="mini">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="电力类别" prop="powerType">
                <el-select v-model="queryParamsDay.powerType" placeholder="请选择电力类别" filterable size="mini" :clearable="false" style="width: 100%;" @change="clickRightCheckFn($event)">
                  <el-option v-for="item in powerTypeOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='0'" @change="changeRightCheckListDay">
                  <el-checkbox value="" label="P">总有功功率</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='1'" @change="changeRightCheckListDay">
                  <el-checkbox value="" label="Ia">A相</el-checkbox>
                  <el-checkbox value="" label="Ib">B相</el-checkbox>
                  <el-checkbox value="" label="Ic">C相</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='2'" @change="changeRightCheckListDay">
                  <el-checkbox value="" label="Ua">A相</el-checkbox>
                  <el-checkbox value="" label="Ub">B相</el-checkbox>
                  <el-checkbox value="" label="Uc">C相</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='3'" @change="changeRightCheckListDay">
                  <el-checkbox value="" label="Uac">Uac</el-checkbox>
                  <el-checkbox value="" label="Ubc">Ubc</el-checkbox>
                  <el-checkbox value="" label="Uca">Uca</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='4'">
                  <el-checkbox value="" label="Fr">Fr</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='5'">
                  <el-checkbox value="" label="cos">总功率因数</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='6'">
                  <el-checkbox value="" label="Q">总无功功率</el-checkbox>
                </el-checkbox-group>
                <el-checkbox-group v-model="rightCheckListDay" v-if="queryParamsDay.powerType=='7'">
                  <el-checkbox value="" label="S">总视在率</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item>
                <div class="div-bottom">
                  <el-button type="primary" icon="el-icon-search" @click="handleQueryDay" size="mini">查询</el-button>
                  <el-button style="margin-left:0.5rem;" @click="dc" type="primary" icon="el-icon-download" size="mini">导出
                  </el-button>
                </div>
              </el-form-item>
            </el-form>
            <div>
              <extremumECharts :zhuRiDataList="zhuRiDataList" />
            </div>
            <div>
              <br />
              <el-table v-loading="loading" :data="zhuRiDataList" style="overflow-x: auto;overflow-y: auto;height:36vh;" class='dianliTable'>
                <el-table-column width="200" label="日期" prop="time" align="center"></el-table-column>
                <el-table-column width="200" :label="tabTitName" align="center">
                  <el-table-column width="200" label="最大值" align="center">
                    <el-table-column align="center" label="数值" prop="maxV" min-width="90"></el-table-column>
                    <el-table-column align="center" label="时间" prop="maxVTime" min-width="90"></el-table-column>
                  </el-table-column>
                  <el-table-column width="200" label="最小值" align="center">
                    <el-table-column align="center" label="数值" prop="minV" min-width="90"></el-table-column>
                    <el-table-column align="center" label="时间" prop="minVTime" min-width="90"></el-table-column>
                  </el-table-column>
                  <el-table-column align="center" label="平均值" prop="avgV" min-width="90"></el-table-column>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import LoopTree from '@/views/components/loopTree'
import PowerSearch from '@/views/components/powerSearch'
import { powerType } from '../../../utils/powerMonitor/powerType.js'
import powerECharts from './powerECharts.vue'
import extremumECharts from './extremumECharts.vue'
import circuitTree from '@/views/components/circuitTree/index.vue'
import {
  getAllPowerDataInfoApi,
  downloadExcelApi,
  getZhuRiEChartsApi,
  downloadZhuRiExcelApi,
  getTypeListApi
} from '@/api/panoramicData/index.js'
import {
  getToken,
} from '@/api/panoramicData/indexToken.js'
export default {
  name: 'index',
  components: { PowerSearch, LoopTree, powerECharts, extremumECharts, circuitTree },
  data() {
    return {
      token:"",//token(登录能源系统获取)
      tabTitName:'有功功率',// 表格标题
      huiLuName:'',
      zhuRiDataList: [], // 逐日列表数据
      startDate: '', // 逐日开始时间
      endDate: '', // 逐日结束时间
      tableColumnExport: [
        { label: '设备/回路名称', prop: 'name' },
        { label: '采集时间', prop: 'time' }
      ],
      labelA: '',
      labelB: '',
      labelC: '',
      labelD: '',
      tableDtaList: [], // 表格数据
      riEChartsList: [], // 日原始电力echarts数据
      requestObj: {}, // 请求参数
      cityOptions: [], // 电力类别下拉框
      cityOptionsItem: [], // 店里类别下拉框item
      cityOptionsValue: [], // 电力类别下拉框
      cityOptionsTable: [], // 电力类别 关联表格表头
      // 电力类别
      rightCheckListDay: ['P'],
      // 原始 事件日期
      riYuanShiDate: new Date(),
      treeCurV: {}, // 左侧树形组件当前数据
      //供配电单元选项
      powerOptions: [],
      //当前供配电单元编码
      powerCode: null,
      //回路ID
      loopId: null,
      //导出loading
      exportLoading: false,
      tableIteam: [],
      tableIteamDay: [],
      tableColumnIteam: [],
      dateTime: undefined, //日期时间
      unitDW: 'kW',
      resMapData: {},
      resMapDataDay: [],
      resData: [], //图表数据
      activeName: 'first', //选项卡默认
      // activeName: 'second', //选项卡默认
      activeNameLeft: 'second', //选项卡默认
      nowShow: '', //右侧div切换
      rightCheckList: [], //复选框

      showEchartsList: ['Pa', 'Pb', 'Pc', 'P'],
      loading: true, // 遮罩层
      dataName: '',
      echartsName: '',
      echartsNameDay: '',
      optionsName: '',
      optionsNameDay: '总有功功率',
      queryParams: {
        onlyId: '',
        powerType: '0',
        date: '',
        rightCheckList: '',
        powerCode: '',
        deviceCode: ''
      }, // 查询参数
      queryParamsDay: {
        onlyId: '',
        endDate: '',
        startDate: '',
        dataItemCode: '',
        code: '',
        powerType: '0'
      },
      queryParamsDay1: {
        onlyId: '',
        endDate: '',
        startDate: '',
        dataItemCode: '',
        code: '',
        powerType: '0'
      }, // 查询参数
      //设备树
      treeDataDevice: [],
      //被选中的回路节点集合
      //checkedArr: [],
      checkedArrDevice: [],
      //电力类别
      powerTypeOptions: powerType,
      powerTypeOptionsDay: [
        { value: '0', label: '有功功率' },
        { value: '1', label: '电流' },
        { value: '2', label: '相电压' },
        { value: '3', label: '线电压' },
        { value: '4', label: '频率' },
        { value: '5', label: '功率因数' },
        { value: '6', label: '无功功率' },
        { value: '7', label: '视在功率' },
        { value: '8', label: '三相不平衡度' },
        { value: '9', label: '负载率' }
      ],
      //数据项集合
      dataItemList: []
    }
  },
  // 侦听器
  watch: {
    treeCurV: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        this.getData()
        this.getDataDay()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    riYuanShiDate: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        this.getData()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    cityOptionsValue: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        this.getData()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    cityOptionsTable: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        this.getData()
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    activeName: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);
        if (this.activeName === 'first') {
          this.getData()
        } else {
          this.getDataDay()
        }
      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    },
    tabTitName: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        // console.log(newVal);
        // console.log(oldVal);

      },
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    }
  },
  created() {
    this.getDate()
    this.selectFirstType()
    if (this.activeName === 'first') {
      this.getData()
    } else {
      this.getDataDay()
    }
  },
  methods: {
    getDataListFn() {
      if (this.activeName === 'first') {
      }
    },
    //默认高亮选中回路回调
    defaultSelectLoop(key, node) {
      this.treeCurV = node
      this.huiLuName = node.label
    },

    //节点选中状态发生变化时的回调
    handleClick(data) {
      this.treeCurV = data
      this.huiLuName = data.label
    },

    /*切换tab 日原始数据/逐日极值数据*/
    clickzhhuji() {
      console.log(this.activeName)
      // this.getData()
      // this.getDataDay()
    },
    // //默认今日日期
    getDate() {
      var myDate = new Date()
      // this.queryParams.date = myDate.toISOString().slice(0, 10)
      this.endDate = myDate.toISOString().slice(0, 10)
      this.startDate = new Date(myDate - 30 * 24 * 60 * 60 * 1000).toISOString().slice(0, 10)
      // this.dateTime = myDate
      let item = localStorage.getItem("cmts");
      if(item == 'undefined'){
        getToken().then((res) => {
          localStorage.setItem("cmts", res.data);
        })
      }

    },
    /*日期插件 时间变更*/
    changeTime() {
      console.log(this.riYuanShiDate)
    },
    /*日原始数据 查询按钮*/
    handleQuery() {
      this.getData()
    },
    /*逐日极值 查询按钮*/
    handleQueryDay() {
      if (this.cityOptionsValue.length === 0) {
        this.$message({
          showClose: true,
          message: '请选择查询项',
          type: 'error',
          duration: 1500
        })
        return
      }
      if (this.endDate == null || this.startDate == null) {
        this.$message({
          showClose: true,
          message: '请选择起止时间',
          type: 'error',
          duration: 1500
        })
        return
      }

      this.getDataDay()
    },

    //数据项变更（逐日极值）
    changeRightCheckListDay() {
      if (this.rightCheckListDay.length > 1) {
        this.rightCheckListDay.splice(0, 1)
      }
      console.log(this.rightCheckListDay)
    },
    //选中第一个电力类别
    selectFirstType() {
      this.cityOptions = this.powerTypeOptions[0].dataItem.map(m => {
        return m.name
      })
      this.cityOptionsItem = this.powerTypeOptions[0].dataItem.map(m => {
        return m.name
      })
      this.cityOptionsValue = this.powerTypeOptions[0].dataItem.map(m => {
        return m.key
      })
      this.cityOptionsTable = this.powerTypeOptions[0].dataItem.map(m => {
        return m.name
      })
    },
    // 日原始数据   电力类别变更事件
    clickRightCheck(val) {
      this.powerTypeOptions.find(item => {
        if (item.value === val) {
          this.cityOptions = item.dataItem.map(m => {
            return m.name
          })
          this.cityOptionsItem = item.dataItem.map(m => {
            return m.name
          })
          this.cityOptionsValue = item.dataItem.map(m => {
            return m.key
          })
          this.cityOptionsTable = item.dataItem.map(m => {
            return m.name
          })
        }
      })
    },
    // 逐日原始电力类别变更事件
    clickRightCheckFn(val) {
      this.rightCheckListDay = []
      this.powerTypeOptions.find(item => {
        if (item.value === val) {
          this.cityOptions = item.dataItem.map(m => {
            return m.name
          })
          this.cityOptionsItem = item.dataItem.map(m => {
            return m.name
          })
          this.cityOptionsValue = item.dataItem.map(m => {
            return m.key
          })
          this.cityOptionsTable = item.dataItem.map(m => {
            return m.name
          })
        }
      })
      // tabTitName
      let aan =  this.powerTypeOptions.find((item)=>{
        return item.value === this.queryParamsDay.powerType
      })
      this.tabTitName = aan.label
    },
    /*查询逐日极值数据*/
    async getDataDay() {
      this.loading = true
      if (this.startDate || this.endDate) {
        this.requestObj.startDate = this.startDate
        this.requestObj.endDate = this.endDate
      }
      if (!this.activeName === 'second') {
        return
      }
      if (this.activeName === 'second') {
        this.requestObj.dataItemKey = this.rightCheckListDay.join('')
      }
      const res = await getZhuRiEChartsApi(this.requestObj)
      this.zhuRiDataList = res.data

      this.loading = false
    },

    //查询日原始数据
    getData() {
      this.loading = true
      // console.log(this.treeCurV);
      if (this.treeCurV.info) {
        this.requestObj.date = this.riYuanShiDate.toLocaleString().slice(0, 10).replace(/\//g, '-')
        this.requestObj.dataItemKey = this.cityOptionsValue.join('-')
        this.requestObj.deviceCode = this.treeCurV.info.deviceCode
        this.requestObj.powerCode = this.treeCurV.info.powerCode
        this.requestObj.circuitName = this.treeCurV.info.circuitName
        if (!this.activeName === 'first') {
          return
        }

        getAllPowerDataInfoApi(this.requestObj).then(response => {
          this.riEChartsList = response.data
          let aData = []
          let bData = []
          let cData = []
          let dData = []
          let aValue = []
          let bValue = []
          let cValue = []
          let dValue = []
          let aTime = []
          for (const key in this.riEChartsList) {
            if (this.cityOptionsValue[0] === key) {
              aData = this.riEChartsList[key]
            } else if (this.cityOptionsValue[1] === key) {
              bData = this.riEChartsList[key]
            } else if (this.cityOptionsValue[2] === key) {
              cData = this.riEChartsList[key]
            } else if (this.cityOptionsValue[3] === key) {
              dData = this.riEChartsList[key]
            }
          }
          if (aData.length > 0) {
            aValue = aData.map(item => {
              return item.value
            })
            aTime = aData.map((item)=>{
          return item.time
        })
          }
          if (bData.length > 0) {
            bValue = bData.map(item => {
              return item.value
            })
          }
          if (cData.length > 0) {
            cValue = cData.map(item => {
              return item.value
            })
          }
          if (dData.length > 0) {
            dValue = dData.map(item => {
              return item.value
            })
          }
          if (aValue) {
            this.tableDtaList = aValue.map((item, index) => {
              return {
                nameA: this.cityOptionsTable[0],
                nameB: this.cityOptionsTable[1],
                nameC: this.cityOptionsTable[2],
                nameD: this.cityOptionsTable[3],
                aValue: item,
                bValue: bValue[index],
                cValue: cValue[index],
                dValue: dValue[index],
                aTime: aTime[index],
                huiLuName:this.huiLuName
              }
            })
            this.labelA = this.cityOptionsTable[0] ? this.cityOptionsTable[0] : ''
            this.labelB = this.cityOptionsTable[1] ? this.cityOptionsTable[1] : ''
            this.labelC = this.cityOptionsTable[2] ? this.cityOptionsTable[2] : ''
            this.labelD = this.cityOptionsTable[3] ? this.cityOptionsTable[3] : ''
          }
        })
      }
      this.loading = false
    },
    xunhuan(item, one) {
      if (item == '178') {
        let list = [{ label: 'Pa(kw)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '179') {
        let list = [{ label: 'Pb(kw)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '180') {
        let list = [{ label: 'Pc(kw)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '188') {
        let list = [{ label: 'P(kw)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '172') {
        let list = [{ label: 'Ia(A)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '173') {
        let list = [{ label: 'Ib(A)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '174') {
        let list = [{ label: 'Ic(A)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '175') {
        let list = [{ label: 'Ua(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '176') {
        let list = [{ label: 'Ub(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '177') {
        let list = [{ label: 'Uc(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '192') {
        let list = [{ label: 'Uab(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '193') {
        let list = [{ label: 'Ubc(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '194') {
        let list = [{ label: 'Uca(V)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '191') {
        let list = [{ label: 'Fr(Hz)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '181') {
        let list = [{ label: 'Pfa', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '182') {
        let list = [{ label: 'Pfb', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '183') {
        let list = [{ label: 'Pfc', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '189') {
        let list = [{ label: 'Pf', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '184') {
        let list = [{ label: 'Qa(kVar)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '185') {
        let list = [{ label: 'Qb(kVar)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '186') {
        let list = [{ label: 'Qc(kVar)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '190') {
        let list = [{ label: 'Q(kVar)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '214') {
        let list = [{ label: 'Sa(kVA)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '215') {
        let list = [{ label: 'Sb(kVA)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '216') {
        let list = [{ label: 'Sc(kVA)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '213') {
        let list = [{ label: 'S(kVA)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '01') {
        let list = [{ label: 'IUnB(%)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '02') {
        let list = [{ label: 'UUnB(%)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else if (item == '201') {
        let list = [{ label: 'Lf(%)', prop: one }]
        this.tableColumnIteam.push(list[0])
      } else {
        let list = [{ label: '未找到', prop: one }]
        this.tableColumnIteam.push(list[0])
      }
    },

    // 日原始数据导出
    dcPower() {
      // this.tableColumnexport.concat(this.tableColumnIteam)
      if (this.tableDtaList.length === 0) {
        this.$message({
          showClose: true,
          message: '暂无数据，导出失败',
          type: 'error',
          duration: 1500
        })
        return
      }
      var str = '序号'
      for (let i = 0; i < this.tableColumnExport.length; i++) {
        str = str + '-' + this.tableColumnExport[i]['label']
      }
      for (let i = 0; i < this.tableColumnIteam.length; i++) {
        str = str + '-' + this.tableColumnIteam[i]['label']
      }
      console.log(this.requestObj)
      this.$modal.confirm('是否确认导出日原始数据？').then(() => {
        this.requestObj.tableColumnExport = str + '-' + this.cityOptionsValue.join('-')
        downloadExcelApi(this.requestObj)
          .then(response => {
            const blob = new Blob([response])
            console.log(blob)
            const fileName = this.requestObj.date + this.requestObj.circuitName + '日原始数据.xls'
            const elink = document.createElement('a') // 创建a标签
            elink.download = fileName // 为a标签添加download属性
            // a.download = fileName; //命名下载名称
            elink.style.display = 'none'
            elink.href = URL.createObjectURL(blob)
            document.body.appendChild(elink)
            elink.click() // 点击下载
            URL.revokeObjectURL(elink.href) // 释放URL 对象
            document.body.removeChild(elink) // 释放标签
          })
          .catch(err => {
            // console.log(err);
          })
      })
    },
    // 逐日数据导出
    dc() {
      if (this.zhuRiDataList.length === 0) {
        this.$message({
          showClose: true,
          message: '暂无数据，导出失败',
          type: 'error',
          duration: 1500
        })
        return
      }
      this.$modal
        .confirm('是否确认导出逐日极值数据？')
        .then(() => {
          this.exportLoading = true
          return downloadZhuRiExcelApi(this.requestObj)
        })
        .then(response => {
          this.$download.name(response.msg)
          this.exportLoading = false
        })
        .catch(() => {})
    }
  }
}
</script>
