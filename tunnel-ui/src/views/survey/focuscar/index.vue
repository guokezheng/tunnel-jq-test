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
        <el-form-item label="重点车辆" prop="iskeyVehicle">
          <el-select
            v-model="queryParams.iskeyVehicle"
            placeholder="请选择重点车辆"
            clearable
            size="small"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
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
      <el-table-column label="隧道id" align="center" prop="tunnelId" />
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
  getFocusVehicleList
} from "@/api/surveyType/api";

export default {
  name: "Type",
  data() {
    return {
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
        plate: null,
        vehicleTypeName: null,
        iskeyVehicle: null,
      },
      // 表单参数
      form: {}
    };
  },
  created() {
    this.getList();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.lx_boxShow == true) {
          self.lx_boxShow = false;
        }
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询车辆类型配置列表 */
    getList() {
      this.loading = true;
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
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.plate = "";
      this.handleQuery();
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
      let confirmInfo = "是否确认导出所有的车辆类型数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的车辆类型数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportType(queryParams);
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


