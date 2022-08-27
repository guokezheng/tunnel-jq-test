<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="开启" value="0" />
          <el-option label="关闭" value="1" />
        </el-select>
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
          v-hasPermi="['business:vehicleWhiteList:add']"
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
          v-hasPermi="['business:vehicleWhiteList:edit']"
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
          v-hasPermi="['business:vehicleWhiteList:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:vehicleWhiteList:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleWhiteListList" :default-sort = "{prop: 'lastPassTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
      <el-table-column label="车辆类型" align="center" prop="carModel" :formatter="carModelFormat" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="修改人" align="center" prop="updateBy" />
<!--      <el-table-column label="最近一次通行时间" align="center" prop="lastPassTime" sortable />-->
<!--      <el-table-column label="最近一次通行状态" align="center" prop="lastPassStatus" :formatter="lastPassStatusFormat" />-->
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:vehicleWhiteList:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:vehicleWhiteList:remove']"
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

    <!-- 添加或修改车辆通行白名单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input v-model="form.licensePlateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车辆类型" prop="carModel">
          <el-select v-model="form.carModel" placeholder="请选择车辆类型" style="width: 100%;">
            <el-option v-for="dict in carModelList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" clearable size="small" style="width: 100%;">
            <el-option label="开启" value="0" />
            <el-option label="关闭" value="1" />
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
import { listVehicleWhiteList, getVehicleWhiteList, delVehicleWhiteList, addVehicleWhiteList, updateVehicleWhiteList, exportVehicleWhiteList } from "@/api/business/vehicleWhiteList";

export default {
  name: "VehicleWhiteList",
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
      // 车辆通行白名单表格数据
      vehicleWhiteListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licensePlateNumber: null,
        startTime: null,
        endTime: null,
        lastPassStatus: null,
        status: null
      },
      lastPassStatusOptions: [
        {
          label: '正常', value: 1
        }
      ],
      statusOptions: [
        {
          label: '正常', value: 1
        }, {
          label: '禁用', value: 2
        }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        licensePlateNumber: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        carModel: [
          { required: true, message: "车辆类型不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ]
      },
      carModelList: [],
    };
  },
  created() {
    this.getList();
    this.getDicts("sd_car_model").then((response) => {
      this.carModelList = response.data;
    });
  },
  methods: {
    carModelFormat(row) {
      return this.selectDictLabel(this.carModelList, row.carModel);
    },
    // 最近一次通行状态字段格式化
    lastPassStatusFormat(row, column) {
      let arr = this.lastPassStatusOptions.filter(item => {
        return item.value === row.lastPassStatus
      })
      if(arr.length > 0) {
        return arr[0].label
      } else {
        return ''
      }
    },
    // 状态字段格式化
    statusFormat(row, column) {
      return this.statusOptions.filter(item => {
        return item.value === row.status
      })[0].label
    },
    /** 查询车辆通行白名单列表 */
    getList() {
      this.loading = true;
      listVehicleWhiteList(this.queryParams).then(response => {
        this.vehicleWhiteListList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.vehicleWhiteListList.forEach((e) => {
          if (e.status == 0) {
            e.status = "开启";
          } else if (e.status == 1) {
            e.status = "关闭";
          }
        });
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
        status: null,
        carModel: null,
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
      this.title = "添加车辆通行白名单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVehicleWhiteList(id).then(response => {
		var myData = response.data
		myData.status = response.data.status + ""
        this.form = myData;
        this.open = true;
        this.title = "修改车辆通行白名单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 车牌号
          var carCardId = this.form.licensePlateNumber;
          if (this.form.id != null) {
            updateVehicleWhiteList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            listVehicleWhiteList({
              licensePlateNumber:carCardId
            }).then(responseHead => {
              if(responseHead.rows.length > 0){
                this.$modal.msgError("车牌号已存在,请重新录入车牌号");
              } else {
                addVehicleWhiteList(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }
            })
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除车辆通行白名单数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delVehicleWhiteList(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有车辆通行白名单数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportVehicleWhiteList(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
