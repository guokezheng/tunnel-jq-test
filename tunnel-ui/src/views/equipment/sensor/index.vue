<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item label="传感器类型" prop="sensorTypeId">
        <el-select v-model="queryParams.sensorTypeId" placeholder="请选择传感器类型" clearable size="small">
          <el-option
            v-for="item in sensorTypeData"
           :key="item.id"
           :label="item.sensorType"
           :value="item.id"
           />
        </el-select>
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道名称" clearable size="small">
          <el-option
          v-for="item in eqTunnelData"
          :key="item.tunnelId"
          :label="item.tunnelName"
          :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="真实时间" prop="realTime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.realTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择真实时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:sensor:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:sensor:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:sensor:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:sensor:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="sensorList" :default-sort = "{prop: 'realTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" display:"none" />
      <el-table-column label="传感器类型" align="center" prop="sensorType.sensorType" />
      <!-- <el-table-column label="传感器名称" align="center" prop="sensorName" /> -->
      <el-table-column label="隧道名称" align="center" prop="tunnelName.tunnelName" />
      <el-table-column label="洞内亮度" align="center" prop="brightnessInTunnel" />
      <el-table-column label="洞外亮度" align="center" prop="brightnessOutsideTunnel" />
      <el-table-column label="一氧化碳浓度" align="center" prop="coConcentration" />
      <el-table-column label="可见度" align="center" prop="visibility" />
      <el-table-column label="风速" align="center" prop="windSpeed" />
      <el-table-column label="风向" align="center" prop="windDirection" />
      <el-table-column label="水池液位" align="center" prop="tankLevel" />
      <el-table-column label="真实时间" align="center" prop="realTime" width="120" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.realTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:sensor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:sensor:remove']"
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

    <!-- 添加或修改传感器列对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="传感器类型">
          <el-select v-model="form.sensorTypeId" placeholder="请选择传感器类型">
            <el-option
              v-for="item in sensorTypeData"
              :key="item.id"
              :label="item.sensorType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="传感器名称" prop="sensorName">
          <el-input v-model="form.sensorName" placeholder="请输入传感器名称" />
        </el-form-item> -->
        <el-form-item label="隧道名称">
          <el-select v-model="form.tunnelId" placeholder="请选择隧道名称">
            <el-option
              v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="洞内亮度" prop="brightnessInTunnel">
          <el-input v-model="form.brightnessInTunnel" placeholder="请输入洞内亮度" />
        </el-form-item>
        <el-form-item label="洞外亮度" prop="brightnessOutsideTunnel">
          <el-input v-model="form.brightnessOutsideTunnel" placeholder="请输入洞外亮度" />
        </el-form-item>
        <el-form-item label="一氧化碳浓度" prop="coConcentration">
          <el-input v-model="form.coConcentration" placeholder="请输入一氧化碳浓度" />
        </el-form-item>
        <el-form-item label="可见度" prop="visibility">
          <el-input v-model="form.visibility" placeholder="请输入可见度" />
        </el-form-item>
        <el-form-item label="风速" prop="windSpeed">
          <el-input v-model="form.windSpeed" placeholder="请输入风速" />
        </el-form-item>
        <el-form-item label="风向" prop="windDirection">
          <el-input v-model="form.windDirection" placeholder="请输入风向" />
        </el-form-item>
        <el-form-item label="水池液位" prop="tankLevel">
          <el-input v-model="form.tankLevel" placeholder="请输入水池液位" />
        </el-form-item>
        <el-form-item label="真实时间" prop="realTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.realTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择真实时间">
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
import { listSensor, getSensor, delSensor, addSensor, updateSensor } from "@/api/equipment/sensor/api.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/sensorType/api";
import {exportVehicleWhiteList} from "@/api/business/vehicleWhiteList";
export default {
  name: "Sensor",
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 传感器列表格数据
      sensorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sensorTypeId: null,
        sensorName: null,
        tunnelId: null,
        brightnessInTunnel: null,
        brightnessOutsideTunnel: null,
        coConcentration: null,
        visibility: null,
        windSpeed: null,
        windDirection: null,
        tankLevel: null,
        realTime: null,
      },
      // 表单参数
      form: {},
      //隧道名称
      eqTunnelData:{},
      sensorTypeData:{},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getType();
  },
  methods: {
    /** 查询传感器列列表 */
    getList() {
      this.loading = true;
      listSensor(this.queryParams).then(response => {
        this.sensorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询隧道列表 */
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
      });
    },
    getType(){
      listType().then(response => {
       this.sensorTypeData = response.rows;
      })
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
        sensorTypeId: null,
        sensorName: null,
        tunnelId: null,
        brightnessInTunnel: null,
        brightnessOutsideTunnel: null,
        coConcentration: null,
        visibility: null,
        windSpeed: null,
        windDirection: null,
        tankLevel: null,
        createBy: null,
        createTime: null,
        realTime: null,
        updateBy: null,
        updateTime: null
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
      this.title = "添加传感器列";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSensor(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改传感器列";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSensor(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addSensor(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSensor(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    // handleExport() {
    //   this.download('system/sensor/export', {
    //     ...this.queryParams
    //   }, `system_sensor.xlsx`)
    // }
    // handleExport() {
    //   const queryParams = this.queryParams;
    //   this.$confirm('是否确认导出所有数据项?', "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(function() {
    //     return exportVehicleWhiteList(queryParams);
    //   }).then(response => {
    //     this.$download.name(response.msg);
    //   })
    // }
  }
};
</script>
