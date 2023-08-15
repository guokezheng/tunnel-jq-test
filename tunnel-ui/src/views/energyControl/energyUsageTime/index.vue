<!-- 分时段用能 -->
<template>
  <div
    class="app-container"
    v-loading.fullscreen.lock="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <!-- 顶部 -->
    <el-row class="top_tabs">
      <el-tabs
        v-model="activeName"
        @tab-click="handleClick"
        style="width: 100%"
      >
        <!-- <el-tab-pane label="回路用能" name="first">
          <el-card class="my-card-height left_tree" shadow="never">
            <department-select
              @getTreeFirst="getFirstId"
              @getTree="clickTree"
              @clearTree="clearTree"
              class="top_select"
            ></department-select>
            <circuit-tree
              ref="circuitTree"
              @nodeCheck="handleCheckChange2"
              @defaultCheck="defaultCheckLoop2"
              :show_checkbox="true"
              :default_check_first="true"
              :selectIds="siteIdCircuit"
              height="calc(100vh - 320px)"
            ></circuit-tree>
          </el-card>
        </el-tab-pane> -->
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
      </el-tabs>
      <!-- <div class="top_back" @click="goHome"><i class="el-icon-top"></i></div> -->
    </el-row>
    <el-row :gutter="20" style="width: 83%; float: right">
      <!-- 右侧内容 -->
      <el-col :span="24">
        <div class="my-card-height rightBox">
          <div style="width: 100%; height: 53%">
            <!-- tab选择器 -->
            <div class="right_tabs">
              <p>分时段用能</p>
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
              <div id="chart" style="width: 100%; height: 100%"></div>
            </div>
          </div>
          <!-- 表格 -->
          <div id="tableid" class="table energyTable">
            <el-table
              :data="list"
              style="width: 100%"
              ref="multipleTable"
              max-height="100%"
              class="allTable"
            >
              <el-table-column
                fixed
                width="180"
                :label="this.tableName"
                prop="name"
                align="center"
              >
              </el-table-column>
              <el-table-column align="center" label="尖峰">
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="jValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.jValue == null
                        ? null
                        : scope.row.jValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="jPrice"
                  label="单价/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.jPrice == null
                        ? null
                        : scope.row.jPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="f_EpiJAmount"
                  label="金额/元"
                >
                </el-table-column>
              </el-table-column>
              <el-table-column align="center" label="高峰">
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="fValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.fValue == null
                        ? null
                        : scope.row.fValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="fPrice"
                  label="单价/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.fPrice == null
                        ? null
                        : scope.row.fPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="f_EpiFAmount"
                  label="金额/元"
                >
                </el-table-column>
              </el-table-column>
              <el-table-column align="center" label="平时">
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="pValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.pValue == null
                        ? null
                        : scope.row.pValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="pPrice"
                  label="单价/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.pPrice == null
                        ? null
                        : scope.row.pPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="f_EpiPAmount"
                  label="金额/元"
                >
                </el-table-column>
              </el-table-column>
              <el-table-column align="center" label="低谷">
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="gValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.gValue == null
                        ? null
                        : scope.row.gValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="gPrice"
                  label="单价/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.gPrice == null
                        ? null
                        : scope.row.gPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="f_EpiGAmount"
                  label="金额/元"
                >
                </el-table-column>
              </el-table-column>
              <el-table-column align="center" label="深谷">
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="sValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.sValue == null
                        ? null
                        : scope.row.sValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="sPrice"
                  label="单价/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.sPrice == null
                        ? null
                        : scope.row.sPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="90"
                  align="center"
                  prop="f_EpiSAmount"
                  label="金额/元"
                >
                </el-table-column>
              </el-table-column>
              <el-table-column
                align="center"
                label="合计"
                min-width="200"
                fixed="right"
              >
                <el-table-column
                  min-width="100"
                  align="center"
                  prop="sumValue"
                  label="电量/kW·h"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.sumValue == null
                        ? null
                        : scope.row.sumValue.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  min-width="100"
                  align="center"
                  prop="sumPrice"
                  label="金额/元"
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.sumPrice == null
                        ? null
                        : scope.row.sumPrice.toFixed(2)
                    }}</span>
                  </template>
                </el-table-column>
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
import SiteTree from "@/views/components/siteTree";
import CircuitTree from "@/views/components/circuitTree";
import { getSplitTimeByDept } from '@/api/energy/api'
import { mapState } from "vuex";
import departmentSelect from "@/views/components/department/index2.vue";

export default {
  name: "Online",
  components: { CircuitTree, SiteTree, departmentSelect },
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
      loading: false, // 遮罩层
      powerList: [],
      power: { powerName: "" }, //当前站点
      list: [],

      //当前数据
      base_date: new Date(),
      tabType: "day", //当前报表类型 日报，月报， 年报

      queryParams: {
        // 站点查询参数
        deptCodeList: undefined,
        baseTime: undefined,
        type: undefined,
      },
      queryParams2: {
        // 回路查询参数
        loopCodeList: undefined,
        baseTime: undefined,
        type: undefined,
      },

      myChart: "",

      activeName: "second", // tab初始页
      siteIdCircuit: null, //站点id

      tableName: "回路名称",
    };
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
      this.loopIds = keys;
      if (this.activeName === "second") this.getData();
    },
    //节点选中状态发生变化时的回调
    handleCheckChange(data, checked) {
      this.loopIds = checked.checkedKeys; //选中回路的id
      this.getData();
    },

    /******回路******/
    //默认选中回路回调
    // defaultCheckLoop2(keys) {
    //   this.loopIds2 = keys;
    //   if (this.activeName === "first") this.getData();
    // },
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

    // 清除siteId
    clear() {
      // if (this.activeName === "first") {
      //   this.siteIdCircuit = null;
      //   this.loopIds2 = [];
      // }
      this.list = [];
      if (this.myChart) {
        this.myChart.clear();
      }
    },
    // tab切换
    handleClick(tab, event) {
      if (this.activeName === "second") {
        this.tableName = "站点名称";
      }
      // else {
      //   this.tableName = "回路名称";
      // }
      this.getData();
    },
    // 日月年切换
    changeTab() {
      //查询逻辑
      this.getData();
    },
    //查询
    async getData() {
      if (this.activeName === "second") {
        // console.log(this.loopIds);
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
        this.loading = true;
        // 参数
        this.queryParams.deptCodeList = this.loopIds
          .filter((e) => e != null)
          .join(",");
        this.queryParams.baseTime = this.parseTime(this.base_date);
        this.queryParams.type = this.tabType;

        // 回路接口请求
          await getSplitTimeByDept(this.queryParams)
            .then(res => {
              if (res.code === 200) {
                this.list = res.data
                this.$nextTick(function () {
                  //清除选中行
                  this.$refs.multipleTable.doLayout()
                })

                // 计算金额
                let that = this
                this.list.forEach(item => {
                  item.f_EpiJAmount = item.jValue == null || item.jPrice == null ? null : that.numberMul(item.jValue, item.jPrice).toFixed(2)
                  item.f_EpiFAmount = item.fValue == null || item.fPrice == null ? null : that.numberMul(item.fValue, item.fPrice).toFixed(2)
                  item.f_EpiPAmount = item.pValue == null || item.pPrice == null ? null : that.numberMul(item.pValue, item.pPrice).toFixed(2)
                  item.f_EpiGAmount = item.gValue == null || item.gPrice == null ? null : that.numberMul(item.gValue, item.gPrice).toFixed(2)
                  item.f_EpiSAmount = item.sValue == null || item.sPrice == null ? null : that.numberMul(item.sValue, item.sPrice).toFixed(2)
                })
                this.openChart()
              }
            })
            .catch(err => {
            })
        this.loading = false;
      }
      // else if (this.activeName === "first") {
      //   // console.log(this.loopIds2);
      //   this.clearData();
      //   if (!this.siteIdCircuit) {
      //     this.$message({
      //       showClose: true,
      //       message: "请选择归属部门",
      //       type: "warning",
      //       duration: 1500,
      //     });
      //     return;
      //   }
      //   //校验参数
      //   if (this.loopIds2.length === 0) {
      //     this.$message({
      //       showClose: true,
      //       message: "至少选择一条回路",
      //       type: "warning",
      //       duration: 1500,
      //     });
      //     return;
      //   }
      //   this.loading = true;
      //   // 参数
      //   this.queryParams2.loopCodeList = this.loopIds2
      //     .filter((e) => e != null)
      //     .join(",");
      //   this.queryParams2.baseTime = this.parseTime(this.base_date);
      //   // this.queryParams2.loopCodeList = "aa";
      //   // this.queryParams2.baseTime = "2022/10/13 15:51:21";
      //   this.queryParams2.type = this.tabType;

      //   // 回路接口请求
      //   //   await getSplitTimeByLoop(this.queryParams2)
      //   //     .then(res => {
      //   //       if (res.code === 200) {
      //   //         this.list = res.data
      //   //         this.$nextTick(function () {
      //   //           //清除选中行
      //   //           this.$refs.multipleTable.doLayout()
      //   //         })

      //   //         // 计算金额
      //   //         let that = this
      //   //         this.list.forEach(item => {
      //   //           item.f_EpiJAmount = item.jValue == null || item.jPrice == null ? null : that.numberMul(item.jValue, item.jPrice).toFixed(2)
      //   //           item.f_EpiFAmount = item.fValue == null || item.fPrice == null ? null : that.numberMul(item.fValue, item.fPrice).toFixed(2)
      //   //           item.f_EpiPAmount = item.pValue == null || item.pPrice == null ? null : that.numberMul(item.pValue, item.pPrice).toFixed(2)
      //   //           item.f_EpiGAmount = item.gValue == null || item.gPrice == null ? null : that.numberMul(item.gValue, item.gPrice).toFixed(2)
      //   //           item.f_EpiSAmount = item.sValue == null || item.sPrice == null ? null : that.numberMul(item.sValue, item.sPrice).toFixed(2)
      //   //         })
      //   //         this.openChart()
      //   //       }
      //   //     })
      //   //     .catch(err => {
      //   //     })
      //   this.loading = false;
      // }
    },
    // 清空数据
    clearData() {
      // 查询之前清空数据
      this.list = [];
      if (this.myChart) {
        this.myChart.clear();
      }
    },
    //导出excel
    export_excel() {
      this.$nextTick(() => {
        let tableDom0 = document.getElementById("tableid");

        var excelBlob = new Blob([tableDom0.innerHTML], {
          type: "application/vnd.ms-excel",
        });
        console.log("excelBlob", excelBlob);
        var oa = document.createElement("a");
        oa.href = URL.createObjectURL(excelBlob);

        let dateStr;
        let y = this.base_date.getFullYear();
        let m = this.base_date.getMonth() + 1;
        let d = this.base_date.getDate();

        dateStr = y + "年";

        if (this.tabType === "month") {
          dateStr += m + "月的分时段统计.xls";
        } else if (this.tabType === "day") {
          dateStr += m + "月" + d + "日的分时段统计.xls";
        } else {
          dateStr += "的分时段统计.xls";
        }

        oa.download = dateStr;
        oa.click();
      });
    },
    //打开图表
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

        //预置数据
        //x轴
        let xLables = [];
        for (let i of this.list) {
          xLables.push(i.name);
        }
        //y轴数据
        let yLables;
        let jData = [],
          fData = [],
          pData = [],
          gData = [],
          sData = [];
        for (let i of this.list) {
          jData.push(i.f_EpiJAmount);
          fData.push(i.f_EpiFAmount);
          pData.push(i.f_EpiPAmount);
          gData.push(i.f_EpiGAmount);
          sData.push(i.f_EpiSAmount);
          // 万
          // jData.push((i.f_EpiJAmount / 10000).toFixed(2));
          // fData.push((i.f_EpiFAmount / 10000).toFixed(2));
          // pData.push((i.f_EpiPAmount / 10000).toFixed(2));
          // gData.push((i.f_EpiGAmount / 10000).toFixed(2));
        }
        yLables = [
          {
            name: "尖峰",
            type: "bar",
            barWidth: 20,
            // stack: 'Ad',
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              color: "#E98867",
            },
            data: jData,
          },
          {
            name: "高峰",
            type: "bar",
            barWidth: 20,
            // stack: 'Ad',
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              color: "#F5B959",
            },
            data: fData,
          },
          {
            name: "平时",
            type: "bar",
            barWidth: 20,
            // stack: 'Ad',
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              color: "#7ACC8A",
            },
            data: pData,
          },
          {
            name: "低谷",
            type: "bar",
            barWidth: 20,
            // stack: 'Ad',
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              color: "#46B6C9",
            },
            data: gData,
          },
          {
            name: "深谷",
            type: "bar",
            barWidth: 20,
            // stack: 'Ad',
            emphasis: {
              focus: "series",
            },
            itemStyle: {
              color: "#4788C8",
            },
            data: sData,
          },
        ];

        let option;

        option = {
          dataZoom: [
            {
              moveOnMouseMove: true,
              type: "slider",
              show: true,
              xAxisIndex: [0],
              start: 0,
              end: 30, //初始化滚动条
              minValueSpan: 6,
              maxValueSpan: 6,
              height: 15,
              bottom: 45,
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
            axisPointer: {
              type: "shadow",
            },
          },
          legend: {
            bottom: "5%",
            textStyle: {
              color: "#fff",
            },
          },
          grid: [
            {
              right: "2%",
              top: "10%",
              left: "1%",
              containLabel: true,
            },
          ],
          xAxis: [
            {
              type: "category",
              axisTick: { show: false },
              axisLabel: {
                color: "#fff",
              },
              data: xLables,
            },
          ],
          yAxis: [
            {
              type: "value",
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
          ],
          series: yLables,
        };
        this.myChart.setOption(option);
      });
    },
    // 返回按钮
    goHome() {
      this.$router.push("/");
    },
  },
};
</script>
  <style lang="scss" scoped>
.app-container {
  // height: calc(100vh - 130px);
  // padding: 5px 20px;
  // background: none;
  // margin: 0px;
}
.el-select {
  width: 100%;
}
::v-deep .el-card__body {
  padding: 6px !important;
  height: 100%;
}
// ::v-deep .el-scrollbar {
//   margin: 0px 10px;
// }
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
  height: calc(100vh - 200px);
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
    // .view {
    //   border-color: #3070ba;
    //   margin-left: 10px;
    //   background: rgba(48, 112, 186, 0.15);
    //   color: rgba(48, 112, 186, 1);
    // }
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
}
::v-deep .el-tabs__content {
  width: 100%;
  position: absolute;
  top: 55px;
  right: 0;
  overflow: visible;
  height: 79vh;
  .el-tab-pane{
    height: 100%;
    position: relative;
    left: 0;
    top: 0px;
    width: 17%;
    border-right: solid 2px #054D83;
    padding-right: 6px;
  }
  .left_tree {
    position: relative;
    left: 0;
    top: 0px;
    width: 100%;
  }
}
</style>
  