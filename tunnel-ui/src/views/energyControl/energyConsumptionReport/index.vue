<!-- 用能报表 -->
<template>
  <div class="app-container">
    <!-- 顶部 -->
    <el-row class="top_tabs">
      <el-tabs
        v-model="activeName"
        @tab-click="handleClick"
        style="width: 100%"
      >
        <el-tab-pane label="站点用能" name="second">
          <el-card class="my-card-height left_tree" shadow="never">
            <site-tree
              @nodeCheck="handleCheckChange"
              @defaultCheck="defaultCheckLoop"
              :show_checkbox="true"
              :default_check_first="true"
            ></site-tree>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="分项用能" name="third">
          <el-card class="my-card-height left_tree" shadow="never">
            <department-select2
              @getTreeFirst="getFirstId2"
              @getTree="clickTree2"
              @clearTree="clearTree2"
              class="top_select"
            ></department-select2>
            <itemized-tree
              @nodeCheck="handleCheckChange3"
              @defaultCheck="defaultCheckLoop3"
              :show_checkbox="true"
              :default_check_first="true"
              height="calc(100vh - 320px)"
            ></itemized-tree>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="分类用能" name="four">
          <el-card class="my-card-height left_tree" shadow="never">
            <department-select3
              @getTreeFirst="getFirstId3"
              @getTree="clickTree3"
              @clearTree="clearTree3"
              class="top_select"
            ></department-select3>
            <classification-tree
              @nodeCheck="handleCheckChange4"
              @defaultCheck="defaultCheckLoop4"
              :show_checkbox="true"
              :default_check_first="true"
              height="calc(100vh - 320px)"
            ></classification-tree>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-row>
    <el-row
      :gutter="20"
      style="width: 83%; float: right; height: calc(100% - 7vh)"
    >
      <!-- 右侧内容 -->
      <el-col :span="24" style="height: 100%">
        <div class="my-card-height rightBox">
          <div style="width: 100%; height: 53%" class="top">
            <!-- tab选择器 -->
            <div class="right_tabs">
              <p>用能曲线</p>
              <div style="display: flex; align-items: center">
                <el-select
                  v-model="tabType"
                  size="mini"
                  style="width: 80px; margin-right: 10px"
                  @change="changeTab"
                >
                  <el-option label="日报" value="day"></el-option>
                  <el-option label="月报" value="month"></el-option>
                  <el-option label="年报" value="year"></el-option>
                </el-select>
                <el-date-picker
                  v-if="tabType === 'day'"
                  size="mini"
                  class="date-picker"
                  v-model="base_date"
                  align="center"
                  type="date"
                  :clearable="false"
                  :picker-options="setDateRange"
                ></el-date-picker>
                <el-date-picker
                  v-if="tabType === 'month'"
                  size="mini"
                  class="date-picker"
                  v-model="base_date"
                  format="yyyy-MM"
                  align="center"
                  type="month"
                  :clearable="false"
                  :picker-options="setDateRange"
                ></el-date-picker>
                <el-date-picker
                  v-if="tabType === 'year'"
                  size="mini"
                  class="date-picker"
                  v-model="base_date"
                  format="yyyy"
                  align="center"
                  type="year"
                  :clearable="false"
                  :picker-options="setDateRange"
                ></el-date-picker>
                <el-button size="mini" class="search" @click="getData"
                  >搜索</el-button
                >
                <el-button size="mini" class="search" @click="export_excel"
                  >导出</el-button
                >
              </div>
            </div>
            <!-- 图表 -->
            <div class="echart">
              <div
                id="chart"
                style="width: 100%; height: 100%; overflow: hidden"
              ></div>
            </div>
          </div>
          <!-- 表格 -->
          <div class="table" id="tableId">
            <el-table
              v-if="activeName == 'second'"
              :data="tableData"
              border
              ref="multipleTable"
              max-height="100%"
              class="allTable"
              :key="Math.random()"
            >
              <el-table-column
                fixed="left"
                :label="this.tableName"
                min-width="200"
                align="center"
                prop="name"
              >
              </el-table-column>
              <el-table-column
                v-for="item in xData"
                :key="item.id"
                :label="item"
                min-width="120"
                align="center"
                :prop="item"
              >
                <!-- <template slot-scope="scope">
                    <span>{{ scope.row.value }}</span>
                  </template> -->
              </el-table-column>
              <el-table-column
                min-width="120"
                fixed="right"
                label="合计"
                align="center"
                prop="sum"
              >
                <!-- <template slot-scope="scope">
                    <span>{{ row_sum(scope.row) }}</span>
                  </template> -->
              </el-table-column>
            </el-table>
            <el-table
              v-else
              border
              :data="tableData"
              style="width: 100%"
              ref="multipleTable"
              max-height="100%"
              :summary-method="getSummaries"
              show-summary
              class="allTable"
            >
              <el-table-column
                fixed="left"
                :label="this.tableName"
                min-width="200"
                align="center"
                prop="name"
              >
              </el-table-column>
              <el-table-column
                v-for="item in xData"
                :key="item.id"
                :label="item"
                min-width="120"
                align="center"
                :prop="item"
              >
              </el-table-column>
              <el-table-column
                min-width="120"
                fixed="right"
                label="合计"
                align="center"
                prop="sum"
              >
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
  
  <script>
import * as echarts from "echarts";
import { mapState } from "vuex";

import departmentSelect2 from "@/views/components/department";
import ItemizedTree from "@/views/components/itemizedTree";
import SiteTree from "@/views/components/siteTree";
import departmentSelect3 from "@/views/components/department/index3.vue";
import ClassificationTree from "@/views/components/classificationTree";
import { getElectricityReportList } from "@/api/energy/api";
export default {
  name: "Online",
  components: {
    ItemizedTree,
    SiteTree,
    departmentSelect2,
    departmentSelect3,
    ClassificationTree,
  },
  data() {
    return {
      setDateRange: {
        disabledDate: (time) => {
          // 禁用今天之后的日期【当前天可选】
          return time.getTime() > Date.now();
        },
      },
      powerCode: null,
      loopIds: [], //选中的站点列表id
      loopIds2: [], //选中的回路列表id
      loopIds3: [], //选中的分项列表id
      loopIds4: [], //选中的分类列表id
      loading: false, // 遮罩层
      powerList: [],
      power: { powerName: "" }, //当前站点
      list: [
        [
          {
            name: "",
            value: 0,
          },
        ],
      ],

      //时间查询条件字段
      base_date: new Date(), //选择的日期 默认当前时间

      tabType: "day", //当前报表类型 日报，月报， 年报

      xData: [], //横坐标
      seriesData: [], //折线图数据

      myChart: "",

      //表格数据
      tableData: [],

      queryParams: {
        // 查询参数
        codeList: null,
        baseTime: null,
        type: null,
        tabType: null,
        deptCode: null,
      },
      list1: [],
      lists: [],

      activeName: "second", // tab初始页
      siteIdCircuit: null, //回路站点id
      siteIdItemized: null, //分项站点id
      siteIdClass: null, //分类站点id

      tableName: "站点名称",
    };
  },
  watch:{
    "tableData":{
      handler(){
      console.log(22222)

        this.$nextTick(()=>{
          this.$refs.multipleTable.doLayout()
        });
      },
      deep:true
    }
  },
  computed: {
    ...mapState({
      sideTheme: (state) => state.settings.sideTheme,
    }),
  },
  methods: {
    /******站点******/
    //默认选中回路回调
    defaultCheckLoop(keys) {
      console.log(keys, "keys1111");
      this.loopIds = keys;
      if (this.activeName === "second") this.getData();
    },
    //节点选中状态发生变化时的回调
    handleCheckChange(data, checked) {
      this.loopIds = checked.checkedKeys; //选中回路的id
      console.log(this.loopIds, "this.loopIds11111");
      this.getData();
    },

    /******回路******/
    //默认选中回路回调
    //   defaultCheckLoop2(keys) {
    //     this.loopIds2 = keys
    //     if (this.activeName === 'first') this.getData()
    //   },
    //节点选中状态发生变化时的回调
    handleCheckChange2(data, checked) {
      this.loopIds2 = checked.checkedKeys; //选中回路的id
      this.getData();
    },
    // 获取初始站点id
    getFirstId(ids) {
      this.siteIdCircuit = ids; //选中回路的id
    },
    //节点选中状态发生变化时的回调
    clickTree(ids) {
      this.siteIdCircuit = ids; //选中回路的id
      this.$refs.circuitTree.check_strictly = false;
      this.$refs.circuitTree.default_check_all = false;
    },
    //清除部门所有选项
    clearTree() {
      this.siteIdCircuit = null;
      this.$refs.circuitTree.check_strictly = false;
      this.$refs.circuitTree.default_check_all = false;
      this.$message.warning("请选择归属部门");
      this.clear();
    },

    /******分项******/
    //默认选中回路回调
    defaultCheckLoop3(keys) {
      console.log(keys, "keys");
      this.loopIds3 = keys;
      if (this.activeName === "third") this.getData();
    },
    //节点选中状态发生变化时的回调
    handleCheckChange3(data, checked) {
      this.loopIds3 = checked.checkedKeys; //选中回路的id
      console.log(this.loopIds3, "this.loopIds3");
      this.getData();
    },
    // 获取初始站点id
    getFirstId2(ids) {
      this.siteIdItemized = ids; //选中回路的id
    },
    //节点选中状态发生变化时的回调
    clickTree2(ids) {
      this.siteIdItemized = ids; //选中回路的id
      this.getData();
    },
    //清除部门所有选项
    clearTree2() {
      this.siteIdItemized = null;
      this.$message.warning("请选择归属部门");
      this.clear();
    },

    /******分类******/
    //默认选中回路回调
    defaultCheckLoop4(keys) {
      this.loopIds4 = keys;
      if (this.activeName === "four") this.getData();
    },
    //节点选中状态发生变化时的回调
    handleCheckChange4(data, checked) {
      this.loopIds4 = checked.checkedKeys; //选中回路的id
      this.getData();
    },
    // 获取初始站点id
    getFirstId3(ids) {
      this.siteIdClass = ids; //选中回路的id
    },
    //节点选中状态发生变化时的回调
    clickTree3(ids) {
      this.siteIdClass = ids; //选中回路的id
      this.getData();
    },
    //清除部门所有选项
    clearTree3() {
      this.siteIdClass = null;
      this.$message.warning("请选择归属部门");
      this.clear();
    },

    // 清除siteId
    clear() {
      // if (this.activeName === 'first') {
      //   this.siteIdCircuit = null
      // }
      if (this.activeName === "third") {
        this.siteIdItemized = null;
      }
      // else if (this.activeName === 'four') {
      //   this.siteIdClass = null
      // }
      this.list1 = [];
      this.lists = [];
      this.xData = [];
      this.seriesData = [];
      this.tableData = [];
      if (this.myChart) {
        this.myChart.clear();
      }
    },
    // tab事件
    handleClick(tab, event) {
      if (this.activeName === "second") {
        this.tableName = "站点名称";
      }
      // else if (this.activeName === 'first') {
      //   this.tableName = '回路名称'
      // }
      else if (this.activeName === "third") {
        this.tableName = "分项名称";
      }
      // else if (this.activeName === 'four') {
      //   this.tableName = '分类名称'
      // }
      this.getData();
    },
    // 日月年切换
    changeTab() {
      this.getData();
    },
    //查询
    async getData() {
      if (this.activeName === "second") {
        // console.log(11, this.loopIds)
        this.clearData();
        //校验参数
        if (this.loopIds.length === 0) {
          this.$message({
            showClose: true,
            message: "至少选择一条回路",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        // this.loading = true;
        // 参数
        this.queryParams.codeList = this.loopIds
          .filter((e) => e != null)
          .join(",");
        this.queryParams.baseTime = this.parseTime(this.base_date);
        this.queryParams.type = this.tabType;
        this.queryParams.tabType = 1;
        this.queryParams.deptCode = "null";
        console.log(this.queryParams, "this.queryParams");
        // 接口请求
        const res = await getElectricityReportList(this.queryParams);
        if (res.code === 200) {
          console.log(res, "second");
          this.list1 = res.data;
          this.$nextTick(() => {
            //清除选中行
            this.$refs.multipleTable.doLayout();
          });
          // this.loading = false
        }
        this.openChart();
      }
      // else if (this.activeName === 'first') {
      //   // console.log(this.loopIds2)
      //   this.clearData()
      //   if (!this.siteIdCircuit) {
      //     this.$message({
      //       showClose: true,
      //       message: '请选择归属部门',
      //       type: 'warning',
      //       duration: 1500
      //     })
      //     return
      //   }
      //   //校验参数
      //   if (this.loopIds2.length === 0) {
      //     this.$message({
      //       showClose: true,
      //       message: '至少选择一条回路',
      //       type: 'warning',
      //       duration: 1000
      //     })
      //     return
      //   }
      //   this.loading = true
      //   // 参数
      //   this.queryParams.codeList = this.loopIds2.filter(e => e != null).join(',')
      //   this.queryParams.baseTime = this.parseTime(this.base_date)
      //   this.queryParams.type = this.tabType
      //   this.queryParams.tabType = 2
      //   this.queryParams.deptCode = this.siteIdCircuit

      //   // 接口请求
      // //   const res = await getElectricityReportList(this.queryParams)
      // //   if (res.code === 200) {
      // //     console.log(res)
      // //     this.list1 = res.data
      // //     this.$nextTick(() => {
      // //       //清除选中行
      // //       this.$refs.multipleTable.doLayout()
      // //     })
      // //     this.loading = false
      // //   }
      //   this.openChart()
      // }
      else if (this.activeName === "third") {
        // console.log(this.loopIds3)
        this.clearData();
        if (!this.siteIdItemized) {
          this.$message({
            showClose: true,
            message: "请选择归属部门",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        //校验参数
        if (this.loopIds3.length === 0) {
          this.$message({
            showClose: true,
            message: "至少选择一条回路",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        // this.loading = true;
        // 参数
        this.queryParams.codeList = this.loopIds3
          .filter((e) => e != null)
          .join(",");
        this.queryParams.baseTime = this.parseTime(this.base_date);
        this.queryParams.type = this.tabType;
        this.queryParams.tabType = 3;
        this.queryParams.deptCode = this.siteIdItemized;

        // 接口请求
        const res = await getElectricityReportList(this.queryParams);
        if (res.code === 200) {
          console.log(res);
          this.list1 = res.data;
          this.$nextTick(() => {
            //清除选中行
            this.$refs.multipleTable.doLayout();
          });
          this.loading = false;
        }
        this.openChart();
      } else if (this.activeName === "four") {
        // console.log(this.loopIds4)
        this.clearData();
        if (!this.siteIdClass) {
          this.$message({
            showClose: true,
            message: "请选择归属部门",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        //校验参数
        if (this.loopIds4.length === 0) {
          this.$message({
            showClose: true,
            message: "至少选择一条回路",
            type: "warning",
            duration: 1500,
          });
          return;
        }
        this.loading = true;
        // 参数
        this.queryParams.codeList = this.loopIds4
          .filter((e) => e != null)
          .join(",");
        this.queryParams.baseTime = this.parseTime(this.base_date);
        this.queryParams.type = this.tabType;
        this.queryParams.tabType = 4;
        this.queryParams.deptCode = this.siteIdClass;

        // 接口请求
        const res = await getElectricityReportList(this.queryParams);
        if (res.code === 200) {
          console.log(res);
          this.list1 = res.data;
          this.$nextTick(() => {
            //清除选中行
            this.$refs.multipleTable.doLayout();
          });
          this.loading = false;
        }
        this.openChart();
      }
    },
    // 清空数据
    clearData() {
      // 查询之前清空数据
      this.list1 = [];
      this.lists = [];
      this.xData = [];
      this.seriesData = [];
      this.tableData = [];
      if (this.myChart) {
        this.myChart.clear();
      }
    },
    //图表
    openChart() {
      this.$nextTick(function () {
        if (
          this.myChart != null &&
          this.myChart != "" &&
          this.myChart != undefined
        ) {
          // 销毁
          this.myChart.dispose();
        }
        this.myChart = echarts.init(document.getElementById("chart"));

        // 拆分成单条数据
        let series = {
          name: "",
          value: [],
          rt: [],
          sum: 0,
        };
        for (let item of this.list1) {
          // console.log(item);
          for (let item1 of item) {
            // console.log(item1);
            series.name = item1.name;
            if (item1.rt != null) {
              if (item1.value === null || item1.value === undefined) {
                item1.value = null;
              }
              // series.value.push(item1.value.toFixed(2));
              series.value.push(item1.value);
              series.rt.push(item1.rt);
            }
          }
          // 计算合计
          series.value.forEach((item) => {
            if (item === null) {
              item = 0;
            }
            series.sum += Number(item);
          });
          series.sum = series.sum.toFixed(2);
          this.lists.push(series);
          // 清空单条数据
          series = {
            name: "",
            value: [],
            rt: [],
            sum: 0,
          };
        }

        // 处理图表数据
        for (let n of this.lists) {
          this.seriesData.push({
            name: n.name,
            type: "line",
            smooth: true,
            symbol: "circle",
            showAllSymbol: true,
            symbolSize: 8,
            data: n.value,
            color: "#0090D8",
          });
          this.xData = n.rt;
        }

        this.$nextTick(() => {
          //清除选中行
          this.$refs.multipleTable.doLayout();
        });
        // 处理表格数据
        let obj = {};
        for (let item of this.lists) {
          obj = {};
          for (let i in this.xData) {
            let a = this.xData[i];
            obj[a] = item.value[i];
            obj["sum"] = item.sum;
            obj["name"] = item.name;
          }
          this.tableData.push(obj);
        }
        // console.log(this.tableData)

        var option = {
          dataZoom: [
            {
              moveOnMouseMove: true,
              type: "slider",
              show: true,
              xAxisIndex: [0],
              start: 0,
              end: 30, //初始化滚动条
              minValueSpan: 11,
              height: 15,
              showDetail: false, //滚动条不准缩放
              zoomLock: true,
              brushSelect: false,
            },
            {
              type: "inside",
              xAxisIndex: 0,
              zoomOnMouseWheel: false, //滚轮是否触发缩放
              moveOnMouseMove: true, //鼠标滚轮触发滚动
              moveOnMouseWheel: true,
            },
          ],
          tooltip: {
            trigger: "axis",
          },
          grid: {
            left: "1%",
            right: "2%",
            top: "10%",
            // bottom: '3%',
            bottom: "12%",
            containLabel: true,
          },
          xAxis: {
            type: "category",
            axisTick: {
              alignWithLabel: true,
            },
            axisLabel: {
              color: "#fff",
            },
            data: this.xData,
          },
          yAxis: {
            type: "value",
            name:'kW-h',
            nameTextStyle:{
              color:"#fff"
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: "dashed",
                color: "#919191",
              },
            },
            axisLabel: {
              color: "#fff",
            },
          },
          series: this.seriesData,
        };

        this.myChart.setOption(option);
      });
    },
    //导出excel
    export_excel() {
      this.$nextTick(() => {
        let tableDom = document.getElementById("tableId");
        let fix = document.querySelector(".el-table__fixed");
        let tableDom0 = tableDom.firstElementChild.removeChild(fix);
        tableDom.firstElementChild.appendChild(fix);
        tableDom0 = tableDom0.innerHTML.replace(/border="0"/g,"border='1'")

        var excelBlob = new Blob([tableDom0], {
          type: "application/vnd.ms-excel",
        });
        var oa = document.createElement("a");
        oa.href = URL.createObjectURL(excelBlob);

        let dateStr;
        let y = this.base_date.getFullYear();
        let m = this.base_date.getMonth() + 1;
        let d = this.base_date.getDate();
        dateStr = y + "年";
        if (this.tabType === "month") {
          dateStr += m + "月的用能报表.xls";
        } else if (this.tabType === "day") {
          dateStr += m + "月" + d + "日的用能报表.xls";
        } else {
          dateStr += "的用能报表.xls";
        }

        oa.download = dateStr;
        oa.click();
      });
    },
    // 列合计
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        const values = data.map((item) => {
          if (item[column.property] != null) {
            return Number(item[column.property]);
          } else {
            return null;
          }
        });
        if (!values.every((value) => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            // console.log(prev, curr)
            if (prev == null && curr == null) {
              return null;
            } else {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }
          }, null);
          if (sums[index] != null && sums[index] != 0) {
            sums[index] = sums[index].toFixed(2);
          }
        } else {
          sums[index] = "";
        }
      });
      return sums;
    },
    //行合计
    row_sum(arr) {
      let that = this;
      let _sum = 0,
        isAllNaN = true;
      if (arr.value) {
        arr.value.forEach(function (item, index) {
          if (item === null || item === undefined) {
            return;
          }
          if (!isNaN(item)) {
            isAllNaN = false;
            _sum = that.numberAdd(_sum, item);
          }
        });
      }
      if (isAllNaN) {
        return null;
      }
      return _sum;
    },
    // 返回按钮
    goHome() {
      this.$router.push("/");
    },
  },
};
</script>
  <style lang="scss" scoped>
// .app-container {
//   height: calc(100vh - 130px);
//   padding: 5px 20px;
//   background: none;
//   margin: 0px;
// }
.el-select {
  width: 100%;
}
::v-deep .el-card__body {
  padding: 6px !important;
  height: 100%;
}
//   ::v-deep .el-scrollbar {
//     margin: 0px 10px;
//   }
// 隐藏滚动条
// ::v-deep .el-scrollbar__wrap {
//   margin-right: -20px !important;
// }
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
  box-sizing: border-box;
  ::v-deep .el-table__body-wrapper {
    max-height: 27vh;
  }
  ::v-deep .el-table__fixed,
  .el-table__fixed-right {
    max-height: 35vh;
  }
  ::v-deep .el-table__fixed-body-wrapper {
    max-height: 25.8vh;
  }
  ::v-deep th.el-table__cell {
    background: #004f8b;
  }
  &::before{
    height: 0px;
  }
  .el-table--border::after, .el-table--group::after{
    width: 0px;
    height: 0px;
  }
}
// .el-table {
// overflow-y: auto !important;
// }
.top_tabs {
  position: relative;
  display: flex;
  align-items: center;
  // ::v-deep .el-tabs__header {
  //   background: white;
  // }
  ::v-deep .el-tabs__nav {
    padding-left: 20px;
  }
  ::v-deep .el-tabs__active-bar {
    width: 90px !important;
  }
  .top_back {
    width: 28px;
    height: 28px;
    background: #3070ba;
    border: 1px solid #205798;
    border-radius: 4px;
    position: absolute;
    top: 5px;
    right: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px;
    color: #fff;
  }
}
.rightBox {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.my-card-height {
  height: 100%;
  .right_tabs {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    p {
      font-size: 0.75vw;
      font-weight: 600;
      height: 100%;
      line-height: 3vh;
    }
    .date-picker {
      width: 7vw;
    }
    .search {
      margin-left: 10px;
      background: linear-gradient(180deg, #4cbfff, #259bff) !important;
      border: 1px solid #65bfff !important;
      color: white;
    }
    ::v-deep .el-input__inner {
      border: 1px solid #ececec;
      color: #3070ba;
      background: #f3f8fe;
    }
    ::v-deep .el-input__prefix {
      color: #3070ba;
    }
  }
}
::v-deep .el-card {
  border: none !important;
  border-radius: 2px;
}
.top_select {
  width: 100%;
  // margin-left: 5%;
  // margin-top: 10px;
}
::v-deep .el-tabs__content {
  width: 100%;
  position: absolute;
  top: 55px;
  right: 0;
  overflow: visible;
  height: 79vh;
  // .left_tree {
  //   position: relative;
  //   left: 0;
  //   top: 0px;
  //   width: 17%;
  // }
  .el-tab-pane {
    height: 100%;
    position: relative;
    left: 0;
    top: 0px;
    width: 17%;
    border-right: solid 2px #054d83;
    padding-right: 6px;
  }
}
</style>
  