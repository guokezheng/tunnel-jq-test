<template>
  <div class="app-container">
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button size="small" :loading="exportLoading" @click="handleExport">导出
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input placeholder="请点击右侧按钮搜索" v-model="queryParams.searchValue" @keyup.enter.native="handleQuery">
            <el-button slot="append" class="searchTable" @click="boxShow = !boxShow"></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="boxShow" ref="cc">
      <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="75px">
        <el-form-item label="所属隧道" prop="tunnelId" v-show="manageStatin == '0'">
          <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
            <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
              :value="item.tunnelId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车道方向" prop="direction">
          <el-select v-model="queryParams.direction" placeholder="请选择方向" clearable size="small">
            <el-option v-for="dict in directionOptions" :key="dict.dictValue" :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间类型" prop="timeType">
          <el-radio-group v-model="queryParams.dateType" @input="changeTab">
            <el-radio :label="0">分</el-radio>
            <el-radio :label="1">时</el-radio>
            <el-radio :label="2">日</el-radio>
            <el-radio :label="3">月</el-radio>
            <el-radio :label="4">年</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="时间筛选" prop="timeArr">
          <el-date-picker v-if="queryParams.dateType == 2 || queryParams.dateType == 1 || queryParams.dateType == 0"
            v-model="dateRange" type="datetimerange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
            align="center" class="date-picker" :clearable="false" value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%;" popper-class="elDatePicker" size="small" :picker-options="setoptions"
            :default-time="['00:00:00', '23:59:59']" key="1">
          </el-date-picker>
          <el-date-picker v-if="queryParams.dateType == 3" size="small" class="date-picker" v-model="dateRange"
            format="yyyy-MM" align="center" type="monthrange" :clearable="false" :picker-options="setDateRange"
            range-separator="-" start-placeholder="开始月份" end-placeholder="结束月份" style="width: 100%;"
            value-format="yyyy-MM" key="2">
          </el-date-picker>
          <el-date-picker v-if="queryParams.dateType == 4" size="small" class="date-picker" v-model="dateRange"
            align="center" type="year" :clearable="false" :picker-options="setDateRange" style="width: 100%;"
            format="yyyy" value-format="yyyy" key="3"></el-date-picker>
        </el-form-item>

        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery">搜索</el-button>
          <el-button size="small" @click="resetQuery" type="primary" plain>重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="tableTopHr"></div>
    <el-table ref="tableFile" v-loading="loading" :data="tableData"
      @row-click="handleRowClick" class="allTable" :row-key="getRowKey" height="62vh">
      <!-- <el-table-column type="selection" width="55" align="center" reserve-selection /> -->
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="方向" align="center" prop="direction" :formatter="getDirection" />
      <el-table-column label="车流量" align="center" prop="carNum" />
      <el-table-column label="时间" align="center" prop="carTime" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>
<script>
  import {
    getUserDeptId
  } from "@/api/system/user";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    trafficVolumeList,trafficVolumeExport
  } from "@/api/monitor/vehicle";
  export default {
    data() {
      return {
        loading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        total: 0,
        tableData: [],
        manageStatin: this.$cache.local.get("manageStation"),
        userQueryParams: {
          userName: this.$store.state.user.name,
        },
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          // ids: "",
          searchValue: "",
          dateType: 0, //当前报表类型 年月日时分

        },
        dateRange: new Date(),
        // base_date: new Date().getFullYear(), //选择的日期 默认当前时间
        exportLoading: false,
        eqTunnelData: [],
        userDeptId: null,
        boxShow: false,
        setDateRange: {
          disabledDate: (time) => {
            // 禁用今天之后的日期【当前天可选】
            return time.getTime() > Date.now();
          },
        },
        setoptions: {
          // 时间不能大于当前时间
          disabledDate(time) {
            let current_time = new Date().format("yyyy-MM-dd") + " 23:59:59"; //时间日期为：‘当前日期 23:59:59’
            let t = new Date(current_time).getTime(); //‘当前日期 23:59:59’的时间戳
            return time.getTime() > t;
          },
          selectableRange: "00:00:00 - 23:59:59",
        },
        directionOptions: []
      }
    },
    created() {
      this.getUserDept();
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data
      });
      this.getList();
    },
    //点击空白区域关闭全局搜索弹窗
    mounted() {
      document.addEventListener("click", this.bodyCloseMenus);
      // this.watchSize();
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    watch: {
      "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
        this.getList();
        this.getTunnel(this.userDeptId);
      },
    },
    methods: {
      /** 查询设备列表 **/
      getList() {
        this.loading = false;
        if (this.manageStatin == "1") {
          this.queryParams.tunnelId = this.$cache.local.get(
            "manageStationSelect"
          );
        }
        console.log(this.dateRange,"this.dateRange")
        if (this.queryParams.dateType != 4) {
          this.queryParams.startDate = this.dateRange[0]
          this.queryParams.endDate = this.dateRange[1]
        } else {
          this.queryParams.startDate = this.dateRange.getFullYear()
          this.queryParams.endDate = this.dateRange.getFullYear()
        }
        trafficVolumeList(this.queryParams).then(
          (response) => {
            this.tableData = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      getUserDept() {
        getUserDeptId(this.userQueryParams).then((response) => {
          this.userDeptId = response.rows[0].deptId;
          // this.queryParams.deptId = response.rows[0].deptId;
          this.getTunnel(this.userDeptId);
        });
      },
      getDirection(row) {
        return this.selectDictLabel(this.directionOptions, row.direction);

      },
      /** 所属隧道 */
      getTunnel(userDeptId) {
        listTunnels(userDeptId).then((response) => {
          this.eqTunnelData = response.rows;
        });
      },
      changeTab() {
        this.$forceUpdate()
        this.dateRange = new Date()
        // this.base_date = new Date().getFullYear()
      },
      bodyCloseMenus(e) {
        let self = this;
        if (self.boxShow == true) {
          if (this.$refs.main && !this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
            if(self.boxShow == true){
              self.boxShow = false;
            }
          }
        }
      },
      handleRowClick(row) {
        this.$refs.tableFile.toggleRowSelection(row);
      },
      //翻页时不刷新序号
      indexMethod(index) {
        return (
          index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
        );
      },
      // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
      getRowKey(row) {
        return row.id;
      },
      // /** 多选框选中数据 */
      // handleSelectionChange(selection) {
      //   this.ids = selection.map((item) => item.id);
      //   this.single = selection.length !== 1;
      //   this.multiple = !selection.length;
      // },
      // 刷新
      resetQuery() {
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          // ids: "",
          searchValue: "",
          dateType: 0,
        }
        this.dateRange = new Date()
        this.$refs.tableFile.clearSelection();
        this.resetForm("queryForm");
        this.handleQuery()
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.boxShow = false
        this.getList();
      },
      /** 数据报表Tab导出按钮操作 */
      handleExport() {
        let confirmInfo = "是否确认导出所有的数据报表数据项？";
        // if (this.ids.length > 0) {
        //   confirmInfo = "是否确认导出所选的数据报表数据项？";
        // }
        // this.queryParams.ids = this.ids.join();
        const queryParams = this.queryParams;
        this.$modal
          .confirm(confirmInfo)
          .then(() => {
            this.exportLoading = true;
            return trafficVolumeExport(queryParams);
          })
          .then((response) => {
            this.$download.name(response.msg);
            this.exportLoading = false;
            this.$refs.tableFile.clearSelection();
            // this.queryParams.ids = "";
          })
          .catch(() => {});
      },

    },
  }

</script>
<style scoped lang="scss">
  .el-radio {
    color: #fff;
  }
::v-deep .el-range-editor--small .el-range-separator{
  line-height: 31px;
}
</style>
