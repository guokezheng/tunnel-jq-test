<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="车辆id" prop="vehicleId">
        <el-input
          v-model="queryParams.vehicleId"
          placeholder="请输入车辆id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="隧道id" prop="tunnelId">
        <el-input
          v-model="queryParams.tunnelId"
          placeholder="请输入隧道id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      <!-- <el-form-item label="经度" prop="longitude">
        <el-input
          v-model="queryParams.longitude"
          placeholder="请输入经度,分辨率1e-7°，东经为正，西经为负"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input
          v-model="queryParams.latitude"
          placeholder="请输入纬度,分辨率1e-7°，北纬为正，南纬为负"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="速度" prop="speed">
        <el-input
          v-model="queryParams.speed"
          placeholder="请输入速度，单位：Km/h"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="车道号" prop="laneNum">
        <el-input
          v-model="queryParams.laneNum"
          placeholder="请输入车道号,沿行车方向从左往右依次为1,2,3,4…"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="航向角" prop="courseAngle">
        <el-input
          v-model="queryParams.courseAngle"
          placeholder="请输入航向角，单位：°，保留1位小数，车头与正北夹角"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="vehicleLicense">
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
      <!-- <el-form-item label="桩号" prop="stakeNum">
        <el-input
          v-model="queryParams.stakeNum"
          placeholder="请输入桩号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="监测时间" prop="detectTime">
        <!-- <el-date-picker clearable size="small"
          v-model="queryParams.detectTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择监测时间">
        </el-date-picker> -->
        <el-date-picker clearable size="small"
            v-model="queryParams.detectTime"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            placeholder="选择监测时间">
      </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['radar:data:add']"
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
          v-hasPermi="['radar:data:edit']"
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
          v-hasPermi="['radar:data:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['radar:data:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <!-- <el-table-column label="车辆id" align="center" prop="vehicleId" /> -->
      <el-table-column label="隧道id" align="center" prop="tunnelId" />
      <el-table-column label="车辆类型" align="center" prop="vehicleType" :formatter="vehicleTypeFormat" />
      <el-table-column label="车辆颜色" align="center" prop="vehicleColor" :formatter="vehicleColorFormat"/>
      <el-table-column label="桩号" align="center" prop="stakeNum" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="速度" align="center" prop="speed" />
      <el-table-column label="车道号" align="center" prop="laneNum" />
      <el-table-column label="航向角" align="center" prop="courseAngle" />
      <el-table-column label="车牌号" align="center" prop="vehicleLicense" />
      <el-table-column label="车牌颜色" align="center" prop="licenseColor"  :formatter="licenseColorFormat"/>
     
      <el-table-column label="监测时间" align="center" prop="detectTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.detectTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['radar:data:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['radar:data:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改雷达监测感知数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="车辆id" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入车辆id" />
        </el-form-item> -->
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
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度,分辨率1e-7°，东经为正，西经为负" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度,分辨率1e-7°，北纬为正，南纬为负" />
        </el-form-item>
        <el-form-item label="速度" prop="speed">
          <el-input v-model="form.speed" placeholder="请输入速度，单位：Km/h" />
        </el-form-item>
        <el-form-item label="车道号" prop="laneNum">
          <el-input v-model="form.laneNum" placeholder="请输入车道号,沿行车方向从左往右依次为1,2,3,4…" />
        </el-form-item>
        <el-form-item label="航向角" prop="courseAngle">
          <el-input v-model="form.courseAngle" placeholder="请输入航向角，单位：°，保留1位小数，车头与正北夹角" />
        </el-form-item>
        <el-form-item label="车牌号" prop="vehicleLicense">
          <el-input v-model="form.vehicleLicense" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车牌颜色" prop="licenseColor">
          <el-input v-model="form.licenseColor" placeholder="请输入车牌颜色" />
        </el-form-item>
        <el-form-item label="桩号" prop="stakeNum">
          <el-input v-model="form.stakeNum" placeholder="请输入桩号" />
        </el-form-item>
        <el-form-item label="监测时间" prop="detectTime">
          <el-date-picker clearable size="small"
            v-model="form.detectTime"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            placeholder="选择监测时间">
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
import { listData, getData, delData, addData, updateData, exportData } from "@/api/map/radar/data";

export default {
  name: "Data",
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
      // 雷达监测感知数据表格数据
      dataList: [],
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
        longitude: null,
        latitude: null,
        speed: null,
        laneNum: null,
        courseAngle: null,
        vehicleLicense: null,
        licenseColor: null,
        stakeNum: null,
        detectTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
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
    
    /** 查询雷达监测感知数据列表 */
    getList() {
      this.loading = true;
      listData(this.queryParams).then(response => {
        this.dataList = response.rows;
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
        longitude: null,
        latitude: null,
        speed: null,
        laneNum: null,
        courseAngle: null,
        vehicleLicense: null,
        licenseColor: null,
        stakeNum: null,
        detectTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // console.log(this.form,'表单值')
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
      this.title = "添加雷达监测感知数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getData(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改雷达监测感知数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addData(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除雷达监测感知数据编号为"' + ids + '"的数据项？').then(function() {
        return delData(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有雷达监测感知数据数据项？').then(() => {
        this.exportLoading = true;
        return exportData(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
