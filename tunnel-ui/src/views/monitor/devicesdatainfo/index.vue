<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForms"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      style="margin-top: 10px"
    >
      <el-form-item label="管理机构" prop="deptId">
        <treeselect
          v-model="queryParams.deptId"
          :options="deptOptions"
          :show-count="true"
          placeholder="请选择归属部门"
          style="width: 360px;"
        />
        <!--<el-select
          v-model="queryParams.deptId"
          placeholder="请选择管理机构"
          clearable
          size="small"
        >
          <el-option
            v-for="item in deptData"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
          />
        </el-select>-->
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId" v-show="manageStatin == '0'">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采集时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery"
          >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <div class="topBox" style="display: flex; justify-content: space-between">
      <div class="butBox">
        <div :class="searchValue == '1' ? 'xz' : ''" @click="qiehuan('1')">
          CO/VI
        </div>
        <div :class="searchValue == '2' ? 'xz' : ''" @click="qiehuan('2')">
          风速风向
        </div>
        <div :class="searchValue == '3' ? 'xz' : ''" @click="qiehuan('3')">
          洞内光强
        </div>
        <div :class="searchValue == '4' ? 'xz' : ''" @click="qiehuan('4')">
          洞外光强
        </div>
      </div>
      <div @click="marketChang()">
        <i
          class="el-icon-s-marketing"
          style="font-size: 36px; color: #39adff"
        ></i>
      </div>
    </div>
    <div v-show="echartShow == false">
      <el-table
        ref="tables"
        v-loading="loading"
        :data="list"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="searchValue == '1'"
        class="tableHeight"
      >
        <!--      <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="序号" align="center" prop="num" />
        <el-table-column label="设备编码" align="center" prop="eqId" />
        <el-table-column label="设备名称" align="center" prop="eqName" />
        <el-table-column label="所属设施" align="center" prop="tunnelName" />
        <el-table-column label="方向" align="center" prop="direction" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column label="CO(ppm)" align="center" prop="CO" />
        <el-table-column label="VI(km)" align="center" prop="VI" />
        <el-table-column label="采集时间" align="center" prop="createTime" />
      </el-table>

      <el-table
        v-loading="loading"
        :data="list"
        class="tableHeight"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="searchValue == '2'"
      >
        <!--      <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="序号" align="center" prop="num" />
        <el-table-column label="设备编码" align="center" prop="eqId" />
        <el-table-column label="设备名称" align="center" prop="eqName" />
        <el-table-column label="所属设施" align="center" prop="tunnelName" />
        <el-table-column label="方向" align="center" prop="direction" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column label="风速(m/s)" align="center" prop="FS" />
        <el-table-column label="风向" align="center" prop="FX" />
        <!--      <el-table-column label="采集时间" align="center" prop="createTime" />-->
        <el-table-column
          label="采集时间"
          align="center"
          prop="createTime"
          width="180"
          sortable
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-table
        ref="tables"
        v-loading="loading"
        :data="list"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="searchValue == '3'"
        class="tableHeight"
      >
        <!--      <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="序号" align="center" prop="num" />
        <el-table-column label="设备编码" align="center" prop="eqId" />
        <el-table-column label="设备名称" align="center" prop="eqName" />
        <el-table-column label="所属设施" align="center" prop="tunnelName" />
        <el-table-column label="方向" align="center" prop="direction" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column label="洞内亮度(lux)" align="center" prop="data" />
        <el-table-column label="采集时间" align="center" prop="createTime" />
      </el-table>
      <el-table
        ref="tables"
        v-loading="loading"
        :data="list"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="searchValue == '4'"
        class="tableHeight"
      >
        <!--      <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="序号" align="center" prop="num" />
        <el-table-column label="设备编码" align="center" prop="eqId" />
        <el-table-column label="设备名称" align="center" prop="eqName" />
        <el-table-column label="所属设施" align="center" prop="tunnelName" />
        <el-table-column label="方向" align="center" prop="direction" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column label="洞外亮度(cd/㎡)" align="center" prop="data" />
        <el-table-column label="采集时间" align="center" prop="createTime" />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <div v-show="echartShow">
      <div ref="echartsBox" id="echarts-Box" class="echarts-Box"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";
import {
  list,
  delLogininfor,
  cleanLogininfor,
  exportLogininfor,
} from "@/api/monitor/logininfor";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";
import { listLog } from "@/api/system/log";
import { listDept,treeselect,treeselectExcYG1 } from "@/api/system/dept";
import { getUserDeptId } from "@/api/system/user";
import { dataLogInfoList } from "@/api/equipment/eqTypeItem/item";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Logininfor",
  dicts: ["sys_common_status"],
  components: { Treeselect },

  data() {
    return {
      manageStatin:this.$cache.local.get("manageStation"),

      searchValue: "1",
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: {},
      //所属隧道
      eqTunnelData: {},
      //管理机构
      deptData: {},
      //设备类型
      eqTypeData: {},
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      //控制方式
      controlTypeOptions: [],
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        deptId: null,
        searchValue: "1",
      },
      echartShow: false,
      CO: [],
      VI: [],
      fsData: [],
      dnData: [],
      dwData: [],
      // 部门树选项
      deptOptions: undefined,
    };
  },
  created() {
    this.getTreeselect();
    this.getList("1");
    this.getDepts();
    this.getUserDept();
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    this.getDicts("sd_device_opt_state").then((response) => {
      this.operationStateOptions = response.data;
    });
  },
  mounted() {
    this.watchSize();
  },
  methods: {
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    marketChang() {
      this.echartShow = !this.echartShow;
      this.initChart();
    },
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    initChart() {
      var chartDom = document.getElementById("echarts-Box");
      console.log(chartDom, "666666666");
      var myChart = echarts.init(chartDom);
      var option;
      if (this.searchValue == 1) {
        var series = [
          {
            name: "CO",
            stack: "Total",
            label: {
              show: true,
              position: "top",
            },
            data: this.CO,
            type: "line",
          },
          {
            name: "VI",
            stack: "Total",
            label: {
              show: true,
              position: "top",
            },
            data: this.VI,
            type: "line",
          },
        ];
      } else if (this.searchValue == 2) {
        //   fsData: [],
        // dnData: [],
        // dwData: [],
        var series = [
          {
            name: "风速风向",
            stack: "Total",
            label: {
              show: true,
              position: "top",
            },
            data: this.fsData,
            type: "line",
          },
        ];
      } else if (this.searchValue == 3) {
        //   fsData: [],
        // dnData: [],
        // dwData: [],
        var series = [
          {
            name: "洞内亮度",
            stack: "Total",
            label: {
              show: true,
              position: "top",
            },
            data: this.dnData,
            type: "line",
          },
        ];
      } else if (this.searchValue == 4) {
        //   fsData: [],
        // dnData: [],
        // dwData: [],
        var series = [
          {
            name: "洞外亮度",
            stack: "Total",
            label: {
              show: true,
              position: "top",
            },
            data: this.dwData,
            type: "line",
          },
        ];
      }
      option = {
        xAxis: {
          type: "category",
          // data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        },
        yAxis: {
          type: "value",
        },
        series,
      };

      option && myChart.setOption(option, true);
    },
    // 切换按钮
    qiehuan(inx) {
      this.dateRange = [];
      this.resetForm("queryForms");
      this.searchValue = inx;
      this.getList(inx);
      this.initChart();
    },
    /** 所属隧道 */
    getTunnel(userDeptId) {
      listTunnels(userDeptId).then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    getDepts() {
      listDept().then((response) => {
        this.deptData = response.data;
      });
    },
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        // this.queryParams.deptId = response.rows[0].deptId;
        this.getTunnel(this.userDeptId);
      });
    },
    //操作是否成功 0：成功 1：失败
    stateFormat(row, column) {
      return this.selectDictLabel(this.operationStateOptions, row.state);
    },
    controlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    },
    /** 查询列表 */
    getList(inx) {
      this.loading = true;
      if(this.manageStatin == '1'){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      if (inx == null || inx == "1" || this.searchValue == "1") {
        this.queryParams.searchValue = "1";
        dataLogInfoList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          this.list = response.rows;
          this.CO = this.list.map((item) => item.CO);
          this.VI = this.list.map((item) => item.VI);
          this.total = response.total;
          this.loading = false;
          this.initChart();
        });
      } else if ((inx != null && inx == "2") || this.searchValue == "2") {
        this.queryParams.searchValue = "2";
        dataLogInfoList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          this.list = response.rows;
          this.fsData = this.list.map((item) => item.FS);
          this.total = response.total;
          this.loading = false;
          this.initChart();
        });
      } else if ((inx != null && inx == "3") || this.searchValue == "3") {
        this.queryParams.searchValue = "3";
        dataLogInfoList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          this.list = response.rows;
          this.dnData = this.list.map((item) => item.data);
          this.total = response.total;
          this.loading = false;
          this.initChart();
        });
      } else if ((inx != null && inx == "4") || this.searchValue == "4") {
        this.queryParams.searchValue = "4";
        dataLogInfoList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          this.list = response.rows;
          this.dwData = this.list.map((item) => item.data);
          this.total = response.total;
          this.loading = false;
          this.initChart();
        });
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList(this.searchValue);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForms");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.infoId);
      this.multiple = !selection.length;
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.infoId || this.ids;
      this.$modal
        .confirm("是否确认删除选中数据项？")
        .then(function () {
          return delLogininfor(infoIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有登录日志数据项？")
        .then(function () {
          return cleanLogininfor();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有操作日志数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportLogininfor(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    // 表格行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
  },
};
</script>
<style scoped lang="scss">
.echarts-Box {
  width: 100vw;
  height: 80vh;
}
.butBox {
  width: 315px;
  display: flex;
  padding: 4px 4px;
  background: #9ecced;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  // justify-content: space-between;
  div {
    padding: 6px 10px;
    color: #fff;
    letter-spacing: 1px;
    cursor: pointer;
  }
  .xz {
    background: #285b8d;
    border-radius: 10px;
  }
}
.addClass {
  .el-select {
    width: 250px;
  }
  .el-input {
    width: 250px !important;
  }
  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 250px !important;
  }
}
.circle {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  display: inline-block;
}
.detailsText {
  display: inline-block;
  margin-left: 20px;
  line-height: 40px;
  width: 100px;
}
hr {
  border: solid 1px #ddd;
}
.rowClass {
  border-top: solid 1px #ddd;
  border-bottom: solid 1px #ddd;
  height: 40px;
  margin-top: 10px;
}
.eventClass {
  height: 30px;
  border-right: solid 1px #ddd;
  width: 100%;
  text-align: center;
  margin-top: 5px;
  line-height: 30px;
}
.eventTitleClass {
  height: 40px;
  background-color: #eeeeee;
  line-height: 40px;
  text-align: center;
}
.video {
  height: 300px;
  border-radius: 0;
  padding: 5px;
  margin-top: 0;
}
.image3 {
  padding: 5px;
  height: 49%;
  // border: solid 1px green;
  width: 100%;
}
.card-box {
  width: 30%;
  text-align: center;
  font-weight: bold;
}

.EquipStatistics {
  width: 200px;
  height: 40px;
  background-image: url(../../../assets/cloudControl/shebeiWarning.png);
  color: white;
  text-align: center;
  line-height: 40px;
  font-weight: 400;
  font-size: 16px;
  margin-left: 14px;
  > span {
    font-size: 24px;
    font-weight: 600;
    vertical-align: middle;
  }
}
.warningStatistics {
  line-height: 60px;
  font-size: 14px;
  // color: #606266;
  font-weight: 700;
}
.eventTitle {
  padding: 15px 0;
  font-size: 18px;
  font-weight: 400;
  color: #303133;
}
.tableHeight{
  max-height: 59vh !important;
  overflow: auto;
}
</style>

