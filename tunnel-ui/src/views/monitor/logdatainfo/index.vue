<template>
  <div class="app-container">
    <!-- <div class="butBox">
      <div :class="searchValue == '1' ? 'xz' : ''" @click="qiehuan('1')">
        系统日志
      </div>
      <div :class="searchValue == '2' ? 'xz' : ''" @click="qiehuan('2')">
        操作日志
      </div>
    </div> -->
    <!-- 全局搜索 -->
    <el-tabs v-model="activeName" @tab-click="handleClick" style="100%">
      <el-tab-pane
        :label="item.title"
        :name="item.name"
        v-for="item in tabList"
        :key="item.name"

      >

      </el-tab-pane>
    </el-tabs>

    <el-row
      :gutter="20"
      class="tabTopFormRow"
      v-show="activeName == '1'"
    >
      <el-col :span="6">
        <el-button
          v-hasPermi="['system:list:add']"
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          >导出
        </el-button>
        <el-button size="small" @click="resetQuery"
            >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            v-model="queryParam.ipaddr"
            placeholder="请输入登录地址、用户名称，回车搜索"
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="xt_boxShow = !xt_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBoxTab" v-show="xt_boxShow" ref="cc">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParam"
        label-width="75px"
      >
        <!--          <el-form-item label="用户名称" prop="userName" style="width: 100%">
            <el-input
              v-model="queryParam.userName"
              placeholder="请输入用户名称"
              clearable
              style="width: 335px;"
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>-->
        <el-form-item label="状态" prop="status" >
          <el-select
            v-model="queryParam.status"
            placeholder="登录状态"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in loginStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="登录时间">
          <el-date-picker
            v-model="dateRangeDl"
            size="small"
            style="width: 100%"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="setoptions"
            :default-time="['00:00:00', '23:59:59']"
            popper-class="elDatePicker"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!--      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model="queryParam.ipaddr"
          placeholder="请输入登录地址"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
    <!--      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParam.userName"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
    <!--      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParam.status"
          placeholder="登录状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in loginStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>-->

    <!--      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
        >导出</el-button>
      </el-form-item>-->
    <el-row
      :gutter="20"
      class="tabTopFormRow"
      v-show="activeName == '2'"
    >
      <el-col :span="6">
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport1"
          >导出
        </el-button>
        <el-button size="small" @click="resetQuery"
            >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple"  ref="main1">
          <el-input
            placeholder="请输入设备名称、桩号、操作地址，回车搜索"
            v-model="queryParams.operIp"
            @keyup.enter.native="handleQuery"
            size="small"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="cz_boxShow = !cz_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBoxTab" v-show="cz_boxShow" ref="cc1">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="设备类型" prop="eqTypeId" style="width: 100%">
          <el-select
            v-model="queryParams.eqTypeId"
            placeholder="请选择设备类型"
            filterable
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
        <el-form-item
          label="隧道名称"
          prop="tunnelId"
          v-show="manageStatin == '0'"
          style="width: 100%"
        >
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
        <el-form-item label="控制方式" prop="controlType" style="width: 100%">
          <el-select
            v-model="queryParams.controlType"
            placeholder="请选择控制方式"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in controlTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" style="width: 100%">
          <el-date-picker
            v-model="dateRangeCz"
            size="small"
            style="width: 100%"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="setoptions"
            :default-time="['00:00:00', '23:59:59']"
            popper-class="elDatePicker"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="tables"
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange1"
      v-show="activeName == '1'"
      :default-sort="{ prop: 'loginTime', order: 'descending' }"
      @sort-change="handleSortChange"
      class="allTable"
      height="62vh"
      :row-key="getRowKey"
    >
      <el-table-column type="selection" width="55" align="center" reserve-selection/>
      <!--      <el-table-column label="访问编号" align="center" prop="infoId" />-->
      <!--      <el-table-column label="序号" align="center" prop="index"  />-->
<!--      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>-->
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <el-table-column
        label="用户名称"
        align="center"
        prop="userName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登录地址"
        align="center"
        prop="ipaddr"
        width="130"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登录地点"
        align="center"
        prop="loginLocation"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="浏览器"
        align="center"
        prop="browser"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="操作系统" align="center" prop="os" />
      <el-table-column
        label="登录状态"
        align="center"
        prop="status"
        :formatter="loginStateFormat"
      />
      <el-table-column label="操作信息" align="center" prop="msg" />
      <el-table-column
        label="登录时间"
        align="center"
        prop="loginTime"
        sortable="custom"
        :sort-orders="['descending', 'ascending']"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-table
      v-loading="loading"
      :data="logList"
      class="allTable"
      :default-sort="{ prop: 'createTime', order: 'descending' }"
      @selection-change="handleSelectionChange2"
      style=" overflow-y: auto;"
      max-height="62vh"
      v-show="activeName == '2'"
      @sort-change="handleSortChange2"
      ref="tableFile"
    >
      <el-table-column type="selection" width="55" align="center" reserve-selection />
      <el-table-column type="index" :index="indexMethod1" label="序号" width="68" align="center"></el-table-column>
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
      <el-table-column label="方向" align="center" prop="direction" >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sd_direction"
            :value="scope.row.direction"
          />
        </template>
      </el-table-column>
      <el-table-column label="桩号" align="center" prop="pile" />
      <el-table-column
        label="操作状态"
        align="center"
        prop="stateName.stateName"
        :formatter="operationStateFormat"
      />
      <el-table-column label="控制方式" align="center" prop="controlType" :formatter="controlTypeFormat" />
      <el-table-column label="操作结果" align="center" prop="state" />
      <el-table-column label="操作地址" align="center" prop="operIp" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        sortable="custom"
        :sort-orders="['descending', 'ascending']"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0 && this.activeName == 1"
      :total="total"
      :page.sync="queryParam.pageNum"
      :limit.sync="queryParam.pageSize"
      @pagination="getList"
    />
    <pagination
      v-show="total > 0 && this.activeName == 2"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  list,
  delLogininfor,
  cleanLogininfor,
  exportLogininfor,
} from "@/api/monitor/logininfor";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";
import { exportLogininfor1, listLog } from "@/api/system/log";

export default {
  name: "Logininfor",
  dicts: ["sys_common_status, sd_control_type","sd_direction"],
  data() {
    return {
      activeName: "1",
      tabList: [
        {
          title: "系统日志",
          name: "1",
        },
        {
          title: "操作日志",
          name: "2",
        },
      ],
      setoptions: {
        // 时间不能大于当前时间
          disabledDate(time) {
            let current_time = new Date().format('yyyy-MM-dd')+' 23:59:59';  //时间日期为：‘当前日期 23:59:59’
            let t = new Date(current_time).getTime(); //‘当前日期 23:59:59’的时间戳
            return time.getTime() > t;
          },
        selectableRange: '00:00:00 - 23:59:59'
      },
      manageStatin: this.$cache.local.get("manageStation"),
      xt_boxShow: false,
      cz_boxShow: false,
      // searchValue: "1",
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
      dateRangeDl: [],
      dateRangeCz: [],
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
      loginStatusOptions: [],
      // 操作日志表格数据
      logList: [],
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: null,
        userName: null,
        status: null,
        ids:"",
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
        pile:null,
        direction:null,
        // searchValue: null,
        operIp: "",
        ids:"",
      },
    };
  },
  created() {
    this.dateRangeDl = this.getPastTime();
    this.dateRangeCz = this.getPastTime();
    this.getList("1");
    this.getTunnel();
    this.getEqType();
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    this.getDicts("sd_device_opt_state").then((response) => {
      this.operationStateOptions = response.data;
    });
    this.getDicts("sys_common_status").then((response) => {
      this.loginStatusOptions = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
    document.addEventListener("click", this.bodyCloseMenus1);
  },
  methods: {

    // 参数timer是过去的n个小时
    getPastTime() {
      //alert(this.timeFormat(new Date(new Date().setHours(0, 0, 0, 0)).getTime()));
      // 获取过去的时间
      //const lastTime = new Date().getTime() - `${timer * 60 * 60 * 1000}`;
      //获取当天0点时间
      const lastTime = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
      const startTime = this.timeFormat(lastTime);
      // 当天24点时间
      let time = new Date(new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1).getTime();
      const endTime = this.timeFormat(time);
      return [startTime, endTime];

    },

    //时间生成并处理
       timeFormat(time) {
      // 对应的方法
      const timeType = [
        "getFullYear",
        "getMonth",
        "getDate",
        "getHours",
        "getMinutes",
        "getSeconds"
      ];
      // 分隔符
      const separator = {
        getFullYear: "-",
        getMonth: "-",
        getDate: " ",
        getHours: ":",
        getMinutes: ":",
        getSeconds: ""
      };
      let resStr = "";
      for (let i = 0; i < timeType.length; i++) {
        const element = timeType[i];
        let resTime = new Date(time)[element]();
        // 获取月份的要+1
        resTime = element == "getMonth" ? resTime + 1 : resTime;
        // 小于10，前面加0
        resTime = resTime > 9 ? resTime : "0" + resTime;
        resStr = resStr + resTime + separator[element];
      }
      return resStr;
    },

    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.infoId
    },
    bodyCloseMenus(e) {
      let self = this;
      if(self.xt_boxShow == true){
        if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
          if (self.xt_boxShow == true){
            self.xt_boxShow = false;
          }
        }
      }

    },

    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParam.pageNum-1)*this.queryParam.pageSize+1
    },
    //翻页时不刷新序号
    indexMethod1(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    bodyCloseMenus1(e) {
      let self = this;
      // if (this.$refs.main1 && !this.$refs.main1.contains(e.target)) {
      //   if (self.cz_boxShow == true) {
      //     self.cz_boxShow = false;
      //   }
      // }
      if(self.cz_boxShow == true){
        if (!this.$refs.main1.contains(e.target) && !this.$refs.cc1.contains(e.target)) {
          if (self.cz_boxShow == true){
            self.cz_boxShow = false;
          }
        }
      }

    },


    handleClick(e){
      this.dateRangeDl = this.getPastTime();
      this.dateRangeCz = this.getPastTime();
      this.resetForm("queryForm");
      this.resetForm("queryForms");
      this.getList(this.activeName);
      this.$refs.tables.clearSelection();
      this.$refs.tableFile.clearSelection();
    },
    // // 切换按钮
    // qiehuan(inx) {
    //   this.dateRange = [];
    //   this.resetForm("queryForm");
    //   this.resetForm("queryForms");
    //   this.searchValue = inx;
    //   this.getList(inx);
    // },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      const params = {
        isControl: 1, //是否显示/是否可控：1：是 0：否
      };
      listType(params).then((response) => {
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
    loginStateFormat(row, column) {
      return this.selectDictLabel(this.loginStatusOptions, row.status);
    },
    operationStateFormat(row,column){
      //没有状态名称的默认展示原有数据
      return row.stateName.stateName != null ? row.stateName.stateName : row.operationState;
    },
    /** 查询登录日志列表 */
    getList(inx) {
      this.loading = true;
      this.xt_boxShow = false;
      this.cz_boxShow = false;
      console.log(inx,this.activeName,"+++++++++++++")
      if (inx == "1" || this.activeName == "1") {

        console.log(this.activeName, "this.activeName");
        console.log(this.queryParam, "this.queryParam");
        list(this.addDateRange(this.queryParam, this.dateRangeDl)).then(
          (response) => {
            this.list = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      } else if (inx == "2" || this.activeName == "2") {
        if (this.manageStatin == "1") {
          this.queryParams.tunnelId = this.$cache.local.get(
            "manageStationSelect"
          );
        }
        listLog(this.addDateRange(this.queryParams, this.dateRangeCz)).then(
          (response) => {
            console.log(response, "000000");
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
      this.queryParams.pageSize = 10;

      this.queryParam.pageNum = 1;
      this.queryParam.pageSize = 10;

      this.$refs.tables.clearSelection();
      this.$refs.tableFile.clearSelection();
      this.getList(this.activeName);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRangeDl = this.getPastTime();
      this.dateRangeCz = this.getPastTime();
      this.resetForm("queryForm");
      this.resetForm("queryForms");
      this.queryParam.ipaddr = "";
      this.queryParam.status = null;
      this.queryParam.ids = [];
      this.queryParams.operIp = "";
      this.queryParams.ids = [];

      // if (this.searchValue == '1') {
      //   this.$refs.tables.sort('loginTime', 'descending')
      // } else if (this.searchValue == '2') {
      //   this.$refs.tables.sort('createTime', 'descending')
      // }
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange1(selection) {
      this.ids = selection.map((item) => item.infoId);
      this.multiple = !selection.length;
    },

    /** 多选框选中数据 */
    handleSelectionChange2(selection) {
      this.ids = selection.map((item) => item.id);
      this.multiple = !selection.length;
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      console.log(column,"column")
      this.queryParam.orderByColumn = column.prop;
      this.queryParam.isAsc = column.order;
      this.getList();
    },
    /** 排序触发事件 */
    handleSortChange2(column, prop, order) {
      console.log(column,"column")
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      console.log(this.queryParams,"this.queryParamsthis.queryParams")
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
      let confirmInfo ="是否确认导出所有的系统日志数据项？";
      if(this.ids.length>0){
        confirmInfo = "是否确认导出所选的系统日志数据项？";
      }
      let  ids = this.ids.join();
      this.queryParam.ids = ids;
      const queryParams = this.queryParam;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportLogininfor(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tables.clearSelection();
          this.queryParam.ids = ''
        })
        .catch(() => {});
    },

    /** 操作日志导出按钮操作 */
    handleExport1() {
      let startTime;
      let endTime;
      if (this.dateRangeCz.length>0) {
        startTime=new Date(this.dateRangeCz[0].replace(/-/g,"/"));//转换
        endTime =  new Date(this.dateRangeCz[1].replace(/-/g,"/"));//转换
      }else{
        this.$message.warning("请先选择创建时间段");
        return;
      }
      var dateDiff = endTime.getTime() - startTime.getTime();//时间差的毫秒数
      if(dateDiff>(7*24*60*60*1000)){
        this.$message.warning("选中的创建时间段不得大于7天");
        return;
      }else{
        let confirmInfo ="是否确认导出所有的操作日志数据项？";
        if(this.ids.length>0){
          confirmInfo = "是否确认导出所选的操作日志数据项？";
        }
        let  ids = this.ids.join();
        this.queryParams.ids = ids;
        const queryParams = this.queryParams;
        this.$modal
          .confirm(confirmInfo)
          .then(() => {
            this.exportLoading = true;
            return exportLogininfor1(queryParams);
          })
          .then((response) => {
            this.$download.name(response.msg);
            this.exportLoading = false;
            this.$refs.tableFile.clearSelection();
            this.queryParams.ids = ''
          })
          .catch(() => {});
      }

    },

  },
};
</script>
<style>
#cascader-menu-45-0 .el-radio {
  display: none !important;
}
</style>
<style scoped lang="scss">
.butBox {
  width: 170px;
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

::v-deep .el-tabs__header {
  margin: 0 0 6px !important;
}
.searchBoxTab{
  top: 12% !important;
  right: 0.8% !important;
  width: 23.8% !important;
}

</style>

