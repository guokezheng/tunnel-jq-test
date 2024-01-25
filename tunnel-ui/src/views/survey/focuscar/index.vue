<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:focuscar:export']"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入车牌号"
            v-model="queryParams.plate"
            @keyup.enter.native="handleQuery"
            size="small"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="lx_boxShow = !lx_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="lx_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="所属隧道" prop="tunnelId" v-show="manageStatin == '0'">
          <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
            <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
              :value="item.tunnelId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车辆类型" prop="carType">
          <el-select v-model="queryParams.carType" placeholder="请选择车辆类型" clearable size="small">
            <el-option v-for="item in carTypeData" :key="item.carType" :label="item.carTypeName"
              :value="item.carType"></el-option>
          </el-select>
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
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="typeList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      height="62vh"
      class="allTable"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="方向" align="center" prop="direction" />
      <el-table-column label="车牌号" align="center" prop="plate"/>
      <el-table-column label="车辆类型" align="center" prop="carType"/>
      <el-table-column label="平均速度" align="center" prop="speed"/>
      <el-table-column label="进入时间" align="center" prop="inTime"/>
      <el-table-column label="离开时间" align="center" prop="outTime"/>
      </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改车辆类型配置对话框 -->
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
    getFocusVehicleList,focusExportType
  } from "@/api/surveyType/api";

export default {
  name: "Type",
  data() {
    return {
      // 大货车 17  大客车 16  危化车 40
      carTypeData:[
        {carTypeName:'大货车',carType:'17'},
        {carTypeName:'大客车',carType:'16'},
        {carTypeName:'危化车',carType:'40'},
      ],
      dateRange: new Date(),
      userDeptId: null,
      manageStatin: this.$cache.local.get("manageStation"),
      eqTunnelData: [],
      userQueryParams: {
          userName: this.$store.state.user.name,
        },
      options:[],
      lx_boxShow: false,
      iskeyVehicle: "0",
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 车辆类型配置表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carType:null,
        plate: null,
        vehicleTypeName: null,
        iskeyVehicle: null,
        dateType: 0, //当前报表类型 年月日时分
      },
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
      // 表单参数
      form: {}
    };
  },
  watch: {
      "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
        this.getList();
        this.getTunnel(this.userDeptId);
      },
    },
  created() {
    this.getUserDept();
    this.getList();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        // this.queryParams.deptId = response.rows[0].deptId;
        this.getTunnel(this.userDeptId);
      });
    },
    /** 所属隧道 */
    getTunnel(userDeptId) {
      listTunnels(userDeptId).then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
        if (self.lx_boxShow == true) {
          if (this.$refs.main && !this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
            if(self.lx_boxShow == true){
              self.lx_boxShow = false;
            }
          }
        }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询车辆类型配置列表 */
    getList() {
      this.loading = true;
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
      getFocusVehicleList(this.queryParams).then((response) => {
        this.typeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        plate: null,
        vehicleTypeName: null,
        iskeyVehicle: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.lx_boxShow = false
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        searchValue: "",
        dateType: 0,
        plate:"",
      }
      this.dateRange = new Date()
      this.$refs.tableFile.clearSelection();
      this.resetForm("queryForm");
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          checkData(this.form).then((response) => {
            if (response.code == 200) {
              if (this.form.id != null) {
                updateType(this.form).then((response) => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.$refs.tableFile.clearSelection();
                  this.getList();
                });
              } else {
                addType(this.form).then((response) => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }
            } else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
        let confirmInfo = "是否确认导出所有的数据报表数据项？";
        if (this.ids.length > 0) {
          confirmInfo = "是否确认导出所选的数据报表数据项？";
        }
        this.queryParams.ids = this.ids.join();
        const queryParams = this.queryParams;
        this.$modal
          .confirm(confirmInfo)
          .then(() => {
            this.exportLoading = true;
            return focusExportType(queryParams);
          })
          .then((response) => {
            this.$download.name(response.msg);
            this.exportLoading = false;
            this.$refs.tableFile.clearSelection();
            this.queryParams.ids = "";
          })
          .catch(() => {});
      },
  },
};
</script>


