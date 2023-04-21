<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通行状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择通行状态" clearable size="small">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="通行时间">
        <el-date-picker
          v-model="times"
          type="datetimerange"
          range-separator="至"
          value-format="yyyy-MM-dd HH:mm:ss"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00','23:59:59']"
        >
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:vehicleWhiteListRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:vehicleWhiteListRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="table" v-loading="loading" :data="vehicleWhiteListRecordList" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="通行记录编号" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
      <el-table-column label="通行状态" align="center" prop="status" :formatter="statusFormat"  />
      <el-table-column label="通行时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:vehicleWhiteListRecord:remove']"
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

    <!-- 添加或修改白名单车辆通行记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input v-model="form.licensePlateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="通行状态">
          <el-select v-model="queryParams.status" placeholder="请选择通行状态" clearable size="small">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
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
import { listVehicleWhiteListRecord, getVehicleWhiteListRecord, delVehicleWhiteListRecord, addVehicleWhiteListRecord, updateVehicleWhiteListRecord, exportVehicleWhiteListRecord } from "@/api/business/vehicleWhiteListRecord";

export default {
  name: "VehicleWhiteListRecord",
  components: {
  },
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
      // 白名单车辆通行记录表格数据
      vehicleWhiteListRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询条件 时间范围
      times: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licensePlateNumber: null,
        status: null,
        startTime: null,
        endTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        licensePlateNumber: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "通行状态", trigger: "blur" }
        ]
      },
      statusOptions: [
        {
          label: '正常', value: 1
        },
        {
          label: '禁止', value: 2
        }
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 通行状态格式化
    statusFormat(row, column) {
      let arr = this.statusOptions.filter(item => {
        return item.value === row.status
      })
      if(arr.length > 0) {
        return arr[0].label
      } else {
        return ''
      }
    },
    /** 查询白名单车辆通行记录列表 */
    getList() {
      this.loading = true;
      if(this.times && this.times.length > 0) {
        this.queryParams.startTime = this.times[0]
        this.queryParams.endTime = this.times[1]
      } else {
        this.queryParams.startTime = ''
        this.queryParams.endTime = ''
      }
      listVehicleWhiteListRecord(this.queryParams).then(response => {
        this.vehicleWhiteListRecordList = response.rows;
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
        licensePlateNumber: null,
        createTime: null,
        status: 0
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
      this.times = ''
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 点击行，切换选中状态
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加白名单车辆通行记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVehicleWhiteListRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改白名单车辆通行记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVehicleWhiteListRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVehicleWhiteListRecord(this.form).then(response => {
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
      this.$confirm('是否确认删除选中数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delVehicleWhiteListRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出白名单车辆通行记录数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportVehicleWhiteListRecord(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
      })
    }
  }
};
</script>
