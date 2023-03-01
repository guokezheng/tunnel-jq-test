<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备id" prop="eqId">
        <el-input
          v-model="queryParams.eqId"
          placeholder="请输入设备id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备类型" prop="eqType">
        <el-select v-model="queryParams.eqType" placeholder="请选择设备类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="寄存器点位" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入寄存器点位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="二进制点" prop="addressIndex">
        <el-input
          v-model="queryParams.addressIndex"
          placeholder="请输入二进制点"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="功能" prop="functionName">
        <el-input
          v-model="queryParams.functionName"
          placeholder="请输入功能"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据类型" prop="dataType">
        <el-select v-model="queryParams.dataType" placeholder="请选择数据类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="信号名称" prop="signalName">
        <el-input
          v-model="queryParams.signalName"
          placeholder="请输入信号名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备状态" prop="stateId">
        <el-input
          v-model="queryParams.stateId"
          placeholder="请输入设备状态"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据状态" prop="dataStatus">
        <el-select v-model="queryParams.dataStatus" placeholder="请选择数据状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="功能描述" prop="functionDescription">
        <el-input
          v-model="queryParams.functionDescription"
          placeholder="请输入功能描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否预留" prop="isReserved">
        <el-input
          v-model="queryParams.isReserved"
          placeholder="请输入是否预留"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:point:add']"
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
          v-hasPermi="['system:point:edit']"
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
          v-hasPermi="['system:point:remove']"
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
          v-hasPermi="['system:point:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pointList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="设备id" align="center" prop="eqId" />
      <el-table-column label="设备类型" align="center" prop="eqType" />
      <el-table-column label="寄存器点位" align="center" prop="address" />
      <el-table-column label="二进制点" align="center" prop="addressIndex" />
      <el-table-column label="功能" align="center" prop="functionName" />
      <el-table-column label="数据类型" align="center" prop="dataType" />
      <el-table-column label="信号名称" align="center" prop="signalName" />
      <el-table-column label="设备状态" align="center" prop="stateId" />
      <el-table-column label="数据状态" align="center" prop="dataStatus" />
      <el-table-column label="功能描述" align="center" prop="functionDescription" />
      <el-table-column label="是否预留" align="center" prop="isReserved" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:point:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:point:remove']"
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

    <!-- 添加或修改设备点位状态详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备id" prop="eqId">
          <el-input v-model="form.eqId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="设备类型" prop="eqType">
          <el-select v-model="form.eqType" placeholder="请选择设备类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="寄存器点位" prop="address">
          <el-input v-model="form.address" placeholder="请输入寄存器点位" />
        </el-form-item>
        <el-form-item label="二进制点" prop="addressIndex">
          <el-input v-model="form.addressIndex" placeholder="请输入二进制点" />
        </el-form-item>
        <el-form-item label="功能" prop="functionName">
          <el-input v-model="form.functionName" placeholder="请输入功能" />
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType">
          <el-select v-model="form.dataType" placeholder="请选择数据类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="信号名称" prop="signalName">
          <el-input v-model="form.signalName" placeholder="请输入信号名称" />
        </el-form-item>
        <el-form-item label="设备状态" prop="stateId">
          <el-input v-model="form.stateId" placeholder="请输入设备状态" />
        </el-form-item>
        <el-form-item label="数据状态">
          <el-radio-group v-model="form.dataStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="功能描述" prop="functionDescription">
          <el-input v-model="form.functionDescription" placeholder="请输入功能描述" />
        </el-form-item>
        <el-form-item label="是否预留" prop="isReserved">
          <el-input v-model="form.isReserved" placeholder="请输入是否预留" />
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
import { listPoint, getPoint, delPoint, addPoint, updatePoint, exportPoint } from "@/api/equipment/protocol/api.js";

export default {
  name: "Point",
  data() {
    return {
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
      // 设备点位状态详情表格数据
      pointList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqId: null,
        eqType: null,
        address: null,
        addressIndex: null,
        functionName: null,
        dataType: null,
        signalName: null,
        stateId: null,
        dataStatus: null,
        functionDescription: null,
        isReserved: null
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
    /** 查询设备点位状态详情列表 */
    getList() {
      this.loading = true;
      listPoint(this.queryParams).then(response => {
        this.pointList = response.rows;
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
        eqId: null,
        eqType: null,
        address: null,
        addressIndex: null,
        functionName: null,
        dataType: null,
        signalName: null,
        stateId: null,
        dataStatus: "0",
        functionDescription: null,
        isReserved: null
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
      this.title = "添加设备点位状态详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPoint(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备点位状态详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePoint(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPoint(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备点位状态详情编号为"' + ids + '"的数据项？').then(function() {
        return delPoint(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有设备点位状态详情数据项？').then(() => {
        this.exportLoading = true;
        return exportPoint(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
