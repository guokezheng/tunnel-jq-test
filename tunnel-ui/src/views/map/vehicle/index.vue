<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" class="formClass">
      <!-- <el-form-item label="车辆id" prop="vehicleId">
        <el-input
          v-model="queryParams.vehicleId"
          placeholder="请输入车辆id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道名称" clearable size="small">
          <el-option 
           v-for="(item) in eqTunnelData"
           :key="item.tunnelId"
           :label="item.tunnelName"
           :value="item.tunnelId"
           />
        </el-select>
      </el-form-item>
      <el-form-item label="车辆类型" prop="vehicleType">
        <el-select v-model="queryParams.vehicleType" placeholder="请选择车辆类型" clearable size="small">
          <el-option 
           v-for="(item) in vehicleType"
           :key="item.dictValue"
           :label="item.dictLabel"
           :value="item.dictValue"
           />
        </el-select>
      </el-form-item>
      <el-form-item label="车辆颜色" prop="vehicleColor">
        <el-select v-model="queryParams.vehicleColor" placeholder="请选择车辆颜色" clearable size="small">
          <el-option 
           v-for="(item) in vehicleColor"
           :key="item.dictValue"
           :label="item.dictLabel"
           :value="item.dictValue"
           />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="车牌号" prop="vehicleLicense">
        <el-input
          v-model="queryParams.vehicleLicense"
          placeholder="请输入车牌号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="车牌颜色" prop="licenseColor">
        <el-select v-model="queryParams.licenseColor" placeholder="请选择车牌颜色" clearable size="small">
          <el-option 
           v-for="(item) in licenseColor"
           :key="item.dictValue"
           :label="item.dictLabel"
           :value="item.dictValue"
           />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary" 
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['special:vehicle:export']"
        >导出</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['special:vehicle:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['special:vehicle:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['special:vehicle:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary" 
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['special:vehicle:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange" max-height="600" 
              :row-class-name="tableRowClassName"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <!-- <el-table-column label="车辆id" align="center" prop="vehicleId" /> -->
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="车辆类型" align="center" prop="vehicleType"  :formatter="vehicleTypeFormat" />
      <el-table-column label="车辆颜色" align="center" prop="vehicleColor"  :formatter="vehicleColorFormat" />
      <el-table-column label="车牌号" align="center" prop="vehicleLicense" />
      <el-table-column label="车牌颜色" align="center" prop="licenseColor"  :formatter="licenseColorFormat"/>
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['special:vehicle:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['special:vehicle:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改重点车辆对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车辆id" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入车辆id" />
        </el-form-item>
        <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item>
        <el-form-item label="车辆类型" prop="vehicleType">
          <el-select v-model="form.vehicleType" placeholder="请选择车辆类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="车辆颜色" prop="vehicleColor">
          <el-input v-model="form.vehicleColor" placeholder="请输入车辆颜色" />
        </el-form-item>
        <el-form-item label="车牌号" prop="vehicleLicense">
          <el-input v-model="form.vehicleLicense" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车牌颜色" prop="licenseColor">
          <el-input v-model="form.licenseColor" placeholder="请输入车牌颜色" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable size="small"
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable size="small"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVehicle, getVehicle, delVehicle, addVehicle, updateVehicle, exportVehicle } from "@/api/map/special/vehicle";
import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
export default {
  name: "Vehicle",
  data() {
    return {
      licenseColor:[],//车牌颜色
      vehicleColor:[],//车辆颜色
      vehicleType:[],//车辆类型
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
      // 重点车辆表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleId: null,
        tunnelId: null,
        vehicleType: null,
        vehicleColor: null,
        vehicleLicense: null,
        licenseColor: null,
        startTime: null,
        endTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      eqTunnelData:[]

    };
  },
  created() {
    this.getList();
    this.getTunnel();
     //车辆类型
     this.getDicts("sd_wj_vehicle_type").then(response => {
      // console.log(response,'车辆类型')
      this.vehicleType = response.data;
    });
    //车辆颜色
    this.getDicts("sd_wj_vehicle_color").then(response => {
      // console.log(response,'车辆颜色')
      this.vehicleColor = response.data;
    });
     //车牌颜色
     this.getDicts("sd_wj_vehicle_color").then(response => {
      // console.log(response,'车辆颜色')
      this.licenseColor = response.data;
    });
  },
  methods: {
     /** 所属隧道 */
     getTunnel() {
        listTunnels().then((response) => {
          console.log(response.rows,"所属隧道列表")
          this.eqTunnelData = response.rows;
        });
      },
     //车辆类型字典给翻译
     vehicleTypeFormat(row, column) {
      // console.log(row)
      return this.selectDictLabel(this.vehicleType, row.vehicleType);
    },
    //车辆颜色字典翻译
    vehicleColorFormat(row, column){
        // console.log(row)
      return this.selectDictLabel(this.vehicleColor, row.vehicleColor);
    },
    //车牌颜色字典翻译
    licenseColorFormat(row, column){
      console.log(row)
      return this.selectDictLabel(this.licenseColor, row.licenseColor);
    },
    /** 查询重点车辆列表 */
    getList() {
      this.loading = true;
      listVehicle(this.queryParams).then(response => {
        console.log(response,"表格")
        this.vehicleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        vehicleId: null,
        tunnelId: null,
        vehicleType: null,
        vehicleColor: null,
        vehicleLicense: null,
        licenseColor: null,
        startTime: null,
        endTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加重点车辆";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVehicle(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改重点车辆";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
      
        if (valid) {
          if (this.form.id != null) {
            updateVehicle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVehicle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除重点车辆编号为"' + ids + '"的数据项？').then(function() {
        return delVehicle(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有重点车辆数据项？').then(() => {
        this.exportLoading = true;
        return exportVehicle(queryParams);
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
  ::v-deep .el-form-item__content{
    // width:190px !important;
    .el-date-editor{
      width: 100%;
    }
  }
  .formClass{
    .el-select,.el-date-editor{
      width:170px;
    }
  }
</style>