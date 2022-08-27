<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择创建时间">
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
          v-hasPermi="['system:icyRoad:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="icyRoadList" max-height="610" :default-sort = "{prop: 'createTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="设备名称" align="center" prop="eqName" />
      <el-table-column label="设备状态" align="center" prop="status" />
      <el-table-column label="表面温度" align="center" prop="surfaceTemperature" />
      <el-table-column label="结冰温度" align="center" prop="freezeTemperature" />
      <el-table-column label="冰厚度" align="center" prop="iceThickness" />
      <el-table-column label="盐度值" align="center" prop="salinityValue" />
      <el-table-column label="湿滑系数" align="center" prop="wetslidingCoefficient" />
      <el-table-column label="路面状况" align="center" prop="roadCondition" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:icyRoad:remove']"
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

    <!-- 添加或修改道路结冰记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="设备状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="表面温度" prop="surfaceTemperature">
          <el-input v-model="form.surfaceTemperature" placeholder="请输入表面温度" />
        </el-form-item>
        <el-form-item label="结冰温度" prop="freezeTemperature">
          <el-input v-model="form.freezeTemperature" placeholder="请输入结冰温度" />
        </el-form-item>
        <el-form-item label="冰厚度" prop="iceThickness">
          <el-input v-model="form.iceThickness" placeholder="请输入冰厚度" />
        </el-form-item>
        <el-form-item label="盐度值" prop="salinityValue">
          <el-input v-model="form.salinityValue" placeholder="请输入盐度值" />
        </el-form-item>
        <el-form-item label="湿滑系数" prop="wetslidingCoefficient">
          <el-input v-model="form.wetslidingCoefficient" placeholder="请输入湿滑系数" />
        </el-form-item>
        <el-form-item label="路面状况" prop="roadCondition">
          <el-input v-model="form.roadCondition" placeholder="请输入路面状况" />
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
import { listIcyRoad, getIcyRoad, delIcyRoad, addIcyRoad, updateIcyRoad } from "@/api/datainfo/icyRoad.js";

export default {
  name: "IcyRoad",
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
      // 道路结冰记录表格数据
      icyRoadList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        createTime: null
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
  },
  methods: {
    /** 查询道路结冰记录列表 */
    getList() {
      this.loading = true;
      listIcyRoad(this.queryParams).then(response => {
        this.icyRoadList = response.rows;
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
        deviceId: null,
        status: "0",
        surfaceTemperature: null,
        freezeTemperature: null,
        iceThickness: null,
        salinityValue: null,
        wetslidingCoefficient: null,
        roadCondition: null,
        createTime: null
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
      this.title = "添加道路结冰记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIcyRoad(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改道路结冰记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateIcyRoad(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIcyRoad(this.form).then(response => {
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
          return delIcyRoad(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/icyRoad/export', {
        ...this.queryParams
      }, `system_icyRoad.xlsx`)
    }
  }
};
</script>
