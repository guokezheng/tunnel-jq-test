<template>
  <div class="app-container">
    <div class="butBox">
      <div :class="searchValue=='1'?'xz':''" @click="qiehuan('1')">系统日志</div>
      <div :class="searchValue=='2'?'xz':''" @click="qiehuan('2')">操作日志</div>
    </div>
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="searchValue == '1'"
             label-width="68px" style="margin-top: 10px">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model="queryParam.ipaddr"
          placeholder="请输入登录地址"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParam.userName"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParam.status"
          placeholder="登录状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="登录时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH-mm-ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
        >导出</el-button>
      </el-form-item>
    </el-form>

    <el-form :model="queryParams" ref="queryForms" :inline="true" v-show="searchValue == '2'"
               label-width="68px" style="margin-top: 10px">
      <el-form-item label="设备类型" prop="eqTypeId">
        <el-select
          v-model="queryParams.eqTypeId"
          placeholder="请选择设备类型"
          clearable
          size="small"
        >
          <el-option
            v-for="item in eqTypeData"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
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
      <el-form-item label="控制方式" prop="controlType">
        <el-select v-model="queryParams.controlType" placeholder="请选择控制方式" clearable size="small">
          <el-option
            v-for="dict in dict.type.sd_control_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH-mm-ss"
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

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange"
            :row-class-name="tableRowClassName" v-show="searchValue == '1'"
            :default-sort="{prop: 'loginTime', order: 'descending'}" @sort-change="handleSortChange" class="tableHeight"  >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访问编号" align="center" prop="infoId" />
      <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" />
      <el-table-column label="登录地址" align="center" prop="ipaddr" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="登录地点" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
      <el-table-column label="浏览器" align="center" prop="browser" :show-overflow-tooltip="true" />
      <el-table-column label="操作系统" align="center" prop="os" />
      <el-table-column label="登录状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作信息" align="center" prop="msg" />
      <el-table-column label="登录日期" align="center" prop="loginTime" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
<!--    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange"-->
<!--              :row-class-name="tableRowClassName" v-show="searchValue == '2'"-->
<!--              :default-sort="defaultSort" @sort-change="handleSortChange" max-height="640" >-->

      <el-table v-loading="loading" :data="logList" class="tableHeight"  :default-sort="{ prop: 'createTime', order: 'descending' }"
        @selection-change="handleSelectionChange" :row-class-name="tableRowClassName" v-show="searchValue == '2'" >
      <el-table-column label="序号" align="center" prop="id" display="none"/>

      <el-table-column
        label="隧道名称"
        align="center"
        prop="tunnelName.tunnelName"
      />
      <el-table-column
        label="设备类型"
        align="center"
        prop="typeName.typeName"
      />
      <el-table-column label="设备名称" align="center" prop="eqName.eqName" />
      <el-table-column
        label="操作状态"
        align="center"
        prop="stateName.stateName"
      />
      <el-table-column label="控制方式" align="center" prop="controlType" :formatter="controlTypeFormat"/>
      <el-table-column label="操作结果" align="center" prop="state" :formatter="stateFormat"/>
      <el-table-column label="操作地址" align="center" prop="operIp" />
      <el-table-column
        label="创建时间"
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
    <pagination
      v-show="total>0 && this.searchValue==1"
      :total="total"
      :page.sync="queryParam.pageNum"
      :limit.sync="queryParam.pageSize"
      @pagination="getList"
    />
    <pagination
      v-show="total>0 && this.searchValue==2"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { list, delLogininfor, cleanLogininfor, exportLogininfor } from "@/api/monitor/logininfor";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {listType} from "@/api/equipment/type/api";
import {listLog} from "@/api/system/log";

export default {
  name: "Logininfor",
  dicts: ['sys_common_status'],
  data() {
    return {
      manageStatin:this.$cache.local.get("manageStation"),

      searchValue : '1',
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
      //设备类型
      eqTypeData: {},
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      //控制方式
      controlTypeOptions: [],
      // 操作日志表格数据
      logList: [],
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: null,
        userName: null,
        status: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqTypeId: null,
        tunnelId: null,
        userName: null,
        eqId: null,
        code: null,
        cmd: null,
        beforeState: null,
        operationState: null,
        controlType: null,
        state: null,
        description: null,
        searchValue:null,
      }
    };
  },
  created() {
    this.getList('1');
    this.getTunnel();
    this.getEqType();
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    this.getDicts("sd_device_opt_state").then((response) => {
      this.operationStateOptions = response.data;
    });
  },
  methods: {
    // 切换按钮
    qiehuan(inx){
      this.dateRange = [];
      this.resetForm("queryForm");
      this.resetForm("queryForms");
      this.searchValue=inx;
      this.getList(inx);
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    //操作是否成功 0：成功 1：失败
    stateFormat(row, column) {
      return this.selectDictLabel(this.operationStateOptions, row.state);
    },
    controlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    },
    /** 查询登录日志列表 */
    getList(inx) {
      this.loading = true;
      if (inx == null || inx == '1' || this.searchValue == '1') {
        console.log(this.searchValue,"this.searchValue");
        console.log(this.queryParams,"this.queryParams");
        list(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      } else if ((inx != null && inx == '2') || this.searchValue == '2') {
        if(this.manageStatin == '1'){
          this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
        }
        listLog(this.addDateRange(this.queryParams, this.dateRange)).then(
          (response) => {
            console.log(response, "000000")
            this.logList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
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
      this.resetForm("queryForm");
      this.resetForm("queryForms");
      // if (this.searchValue == '1') {
      //   this.$refs.tables.sort('loginTime', 'descending')
      // } else if (this.searchValue == '2') {
      //   this.$refs.tables.sort('createTime', 'descending')
      // }
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.infoId)
      this.multiple = !selection.length
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
      this.$modal.confirm('是否确认删除选中数据项？').then(function() {
        return delLogininfor(infoIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有登录日志数据项？').then(function() {
        return cleanLogininfor();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有操作日志数据项？').then(() => {
        this.exportLoading = true;
        return exportLogininfor(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    // 表格行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
  }
};
</script>
<style scoped lang="scss">
.butBox{
  width: 170px;
  display: flex;
  padding: 4px 4px;
  background: #9ecced;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  // justify-content: space-between;
  div{
    padding: 6px 10px;
    color: #fff;
    letter-spacing: 1px;
    cursor: pointer;
  }
  .xz{
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
  max-height: 52vh !important;
  overflow: auto;
}
</style>

