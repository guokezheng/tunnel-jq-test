<!-- 电费分析 -->
<template>
    <div class="app-container" v-loading.fullscreen.lock="loading">
      <el-row :gutter="20">
        <!-- 左侧下拉 -->
        <el-col :span="4">
          <el-card class="my-card-height" shadow="never">
            <site-tree @nodeCheck="handleCheckChange" @defaultCheck="defaultCheckLoop" :show_checkbox="true" :default_check_first="true" ></site-tree>
          </el-card>
        </el-col>
        <!-- 右侧内容 -->
        <el-col :span="20">
          <div class="my-card-height rightBox">
            <div style="width: 100%;height: 53%;">
              <!-- tab选择器 -->
              <div class="right_tabs">
                <p>电费分析</p>
                <div style="display: flex;align-items: center;">
                  <el-select v-model="tabType" size="mini" style="width:80px;margin-right: 10px;" @change="changeTab">
                    <el-option label="月报" value="month"></el-option>
                    <el-option label="年报" value="year"></el-option>
                  </el-select>
                  <el-date-picker v-if="tabType === 'month'" size="mini" class="date-picker" v-model="base_date" format="yyyy-MM" align="center" type="month" :clearable="false"></el-date-picker>
                  <el-date-picker v-if="tabType === 'year'" size="mini" class="date-picker" v-model="base_date" format="yyyy" align="center" type="year" :clearable="false"></el-date-picker>
                  <el-button size="mini" class="view search" @click="getData">搜索</el-button>
                  <el-button size="mini" class="view" @click="export_excel">导出</el-button>
                </div>
              </div>
              <!-- 图表 -->
              <div class="echart">
                <div id="chart" style="width: 100%; height: 100%"></div>
              </div>
            </div>
            <!-- 表格 -->
            <div class="table" id="tableId">
              <el-table :data="tableData" style="width: 100%" ref="multipleTable" height="100%">
                <el-table-column fixed label="站点名称" min-width="180" align="center" prop="name">
                  <!-- <template slot-scope="scope">
                    <span>{{ scope.row.name }}</span>
                  </template> -->
                </el-table-column>
                <el-table-column v-for="(item) in xData" :key="item.id" :label="item" width="120" align="center" :prop="item">
                  <!-- <template slot-scope="scope">
                    <span>{{ scope.row.value[index] }}</span>
                  </template> -->
                </el-table-column>
                <el-table-column min-width="120" fixed="right" label="合计" align="center" prop="sum">
                  <!-- <template slot-scope="scope">
                    <span>{{ row_sum(scope.row) }}</span>
                  </template> -->
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import * as echarts from 'echarts'
  import SiteTree from '@/views/components/siteTree'
//   import { getSiteElectricity } from '@/api/analysis/energyAnalyze'
  import { mapState } from 'vuex'
  
  export default {
    name: 'Online',
    components: { SiteTree },
    data() {
      return {
        powerCode: null,
        loopIds: [], //选中的站点列表id
        loading: false, // 遮罩层
        powerList: [],
        power: { powerName: '' }, //当前站点
        list: [
          [
            {
              name: '',
              value: 0
            }
          ]
        ],
  
        //时间查询条件字段
        base_date: new Date(), //选择的日期 默认当前时间
  
        //当前数据
        baseDate: null, //基础时间
        tabType: 'month', //当前报表类型 日报，月报， 年报
  
        loopName: [], //查询的参数：左侧选中回路名
  
        xData: [], //横坐标
        seriesData: [], //折线图数据
  
        myChart: '',
  
        queryParams: {
          // 查询参数
          deptCodeList: undefined,
          baseTime: undefined,
          type: undefined
        },
        lists: [], //总数据-表格数据
        tableData: [] //表格数据
      }
    },
    computed: {
      ...mapState({
        sideTheme: state => state.settings.sideTheme
      })
    },
    watch: {
      xData: {
        handler(newValue, oldValue) {
          this.openChart()
        }
      },
      seriesData: {
        handler(newValue, oldValue) {
          this.openChart()
        }
      }
    },
    methods: {
      /******站点******/
      //默认选中回路回调
      defaultCheckLoop(keys) {
        this.loopIds = keys
        this.getData()
      },
      //节点选中状态发生变化时的回调
      handleCheckChange(data, checked) {
        this.loopIds = checked.checkedKeys //选中回路的id
        this.getData()
      },
  
      // 右侧标签页点击操作
      changeTab() {
        this.getData()
      },
      //查询
      async getData() {
        // console.log(this.loopIds);
        this.clearData()
        //校验参数
        if (this.loopIds.length === 0) {
          this.$message({
            showClose: true,
            message: '至少选择一条回路',
            type: 'warning',
            duration: 1500
          })
          return
        }
        // this.loading = true
        // 参数
        this.queryParams.deptCodeList = this.loopIds.filter(e => e != null).join(',')
        this.queryParams.baseTime = this.parseTime(this.base_date)
        // this.queryParams.deptCodeList = "2";
        // this.queryParams.baseTime = "2022/12/13 15:51:21";
        this.queryParams.type = this.tabType
  
        // 接口请求
        // const res = await getSiteElectricity(this.queryParams)
        // if (res.code === 200) {
        //   // console.log(res)
        //   this.list = res.data
        //   this.$nextTick(function () {
        //     //清除选中行
        //     this.$refs.multipleTable.doLayout()
        //   })
        //   this.loading = false
        // }
  
        // 拆分成单条数据
        let series = {
          name: '',
          value: [],
          rt: [],
          sum: null
        }
        for (let item of this.list) {
          // console.log(item);
          for (let item1 of item) {
            series.name = item1.name
            if (item1.rt != null) {
              if (item1.value == 0) {
                item1.value = 0
              } else if (item1.value != null) {
                item1.value = item1.value.toFixed(2)
              }
              series.value.push(item1.value)
              series.rt.push(item1.rt)
            }
          }
          // 计算合计
          series.value.forEach(item => {
            if (item === null) {
              item = 0
            }
            series.sum += Number(item)
          })
          series.sum = series.sum.toFixed(2)
          this.lists.push(series)
          // 清空单条数据
          series = {
            name: '',
            value: [],
            rt: [],
            sum: null
          }
        }
  
        // 折线图数据
        for (let n of this.lists) {
          this.seriesData.push({
            name: n.name,
            type: 'line',
            symbol: 'circle',
            smooth: true,
            showAllSymbol: true,
            symbolSize: 8,
            data: n.value
          })
          this.xData = n.rt
        }
  
        // 处理表格数据
        let obj = {}
        for (let item of this.lists) {
          obj = {}
          for (let i in this.xData) {
            let a = this.xData[i]
            obj[a] = item.value[i]
            obj['sum'] = item.sum
            obj['name'] = item.name
          }
          this.tableData.push(obj)
        }
        // console.log(this.tableData)
  
        this.openChart()
      },
      // 清空数据
      clearData() {
        // 查询之前清空数据
        this.list = []
        this.lists = []
        this.seriesData = []
        this.xData = []
        this.tableData = []
      },
      //图表
      openChart() {
        this.$nextTick(function () {
          if (this.myChart != null && this.myChart != '' && this.myChart != undefined) {
            // 销毁
            this.myChart.dispose()
          }
          this.myChart = echarts.init(document.getElementById('chart'))
  
          let option
          option = {
            dataZoom: [
              {
                moveOnMouseMove: true,
                type: 'slider',
                show: true,
                xAxisIndex: [0],
                start: 0,
                end: 30, //初始化滚动条
                minValueSpan: 11,
                maxValueSpan: 11,
                height: 15,
                showDetail: false, //滚动条不准缩放
                zoomLock: true,
                brushSelect: false
              },
              {
                type: 'inside',
                xAxisIndex: 0,
                zoomOnMouseWheel: false, //滚轮是否触发缩放
                moveOnMouseMove: true, //鼠标滚轮触发滚动
                moveOnMouseWheel: true
              }
            ],
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '1%',
              right: '2%',
              top: '10%',
              bottom: '12%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              axisTick: {
                alignWithLabel: true
              },
              axisLabel: {
                color: '#919191'
              },
              data: this.xData
            },
            yAxis: {
              type: 'value',
              splitLine: {
                show: true,
                lineStyle: {
                  type: 'dashed',
                  color: '#919191'
                }
              },
              axisLabel: {
                color: '#919191'
              }
            },
            series: this.seriesData
          }
          this.myChart.setOption(option)
        })
      },
      //导出excel
      export_excel() {
        this.$nextTick(() => {
          let tableDom = document.getElementById('tableId')
  
          let fix = document.querySelector('.el-table__fixed')
          // console.log('fix', fix)
          let tableDom0 = tableDom.firstElementChild.removeChild(fix)
          tableDom.firstElementChild.appendChild(fix)
  
          // console.log('tableDom0', tableDom0)
          // console.log('tableDom', tableDom0.innerHTML)
          var excelBlob = new Blob([tableDom0.innerHTML], {
            type: 'application/vnd.ms-excel'
          })
          // console.log('excelBlob', excelBlob)
          var oa = document.createElement('a')
          oa.href = URL.createObjectURL(excelBlob)
  
          let dateStr
          let y = this.base_date.getFullYear()
          let m = this.base_date.getMonth() + 1
          let d = this.base_date.getDate()
  
          dateStr = y + '年'
  
          if (this.tabType === 'month') {
            dateStr += m + '月的电费统计.xls'
          } else if (this.tabType === 'day') {
            dateStr += m + '月' + d + '日的电费统计.xls'
          } else {
            dateStr += '的电费统计.xls'
          }
  
          oa.download = dateStr
          oa.click()
        })
      },
      // 列合计
      getSummaries(param) {
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          const values = data.map(item => {
            if (item[column.property] != null) {
              return Number(item[column.property])
            } else {
              return null
            }
          })
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              // console.log(prev, curr)
              if (prev == null && curr == null) {
                return null
              } else {
                const value = Number(curr)
                if (!isNaN(value)) {
                  return prev + curr
                } else {
                  return prev
                }
              }
            }, null)
            if (sums[index] != null && sums[index] != 0) {
              sums[index] = sums[index].toFixed(2)
            }
          } else {
            sums[index] = ''
          }
        })
        return sums
      },
      //行合计
      row_sum(arr) {
        let that = this
        let _sum = 0,
          isAllNaN = true
        if (arr.value) {
          arr.value.forEach(function (item, index) {
            if (item === null || item === undefined) {
              return
            }
            if (!isNaN(item)) {
              isAllNaN = false
              _sum = that.numberAdd(_sum, item)
            }
          })
        }
        if (isAllNaN) {
          return null
        }
        return _sum
      }
    }
  }
  </script>
  <style lang="scss" scoped>
  .app-container {
    height: calc(100vh - 130px);
    padding: 5px 20px;
    background: none;
    margin: 0px;
  }
  .el-row{
    height: 100%;
    .el-col{
        height: 100%;

    }
  }
  .el-select {
    width: 100%;
  }
  ::v-deep .el-card__body {
    padding: 6px !important;
    height: 100%;
    >div{
        height: 100%;
    }
  }
  ::v-deep .el-scrollbar {
    margin: 0px 10px;
    height: calc(100% - 52px);
  }
  // 隐藏滚动条
  ::v-deep .el-scrollbar__wrap {
    margin-right: -20px !important;
  }
  .echart {
    width: 100%;
    height: calc(100% - 56px);
  }
  ::v-deep .el-tabs--border-card > .el-tabs__content {
    padding: 12px;
  }
  .table {
    width: 100%;
    height: 45%;
    padding: 20px;
    background: white;
    // overflow: auto;
    ::v-deep .el-table .el-table__fixed-header-wrapper th{
      background: #E7F5FF;
    }
    ::v-deep .el-table .el-table__header-wrapper th{
      background: #E7F5FF;
    }
    /* 修改滚动条宽度 */
    ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
      // width: 7px;
      // height: 7px;
      background: #F4F4F4;
    }
    /* 修改滚动条颜色 */
    ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background-color: rgba(91, 143, 216, 0.5);
      border-radius: 6px;
    }
  }
  .el-table {
    overflow-y: auto !important;
  }
  .rightBox{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .my-card-height {
    height: calc(100vh - 150px);
    .right_tabs {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 20px;
      p {
        color: #fff;
        font-size: 20px;
        font-weight: 600;
        height: 100%;
        line-height: 36px;
      }
      .date-picker {
        width: 7vw;
      }
      .view {
        border-color: #3070BA;
        margin-left: 10px;
        background: rgba(48, 112, 186, 0.15);
        color: rgba(48, 112, 186, 1);
      }
      .search {
        background: rgba(48, 112, 186);
        color: white;
      }
      ::v-deep .el-input__inner {
        border: 1px solid #ECECEC;
        color: #3070ba;
        background: #F3F8FE;
      }
      ::v-deep .el-input__prefix {
        color: #3070BA;
      }
    }
  }
  ::v-deep .el-card{
    border: none !important;
    border-radius: 2px;
  }
  </style>
  