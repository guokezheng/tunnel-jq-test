<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:security:add']"
          >新增指数</el-button
        >
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button
            size="mini"
            circle
            icon="el-icon-refresh"
            @click="handleQuery"
          />
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏搜索' : '显示搜索'"
          placement="top"
        >
          <el-button
            size="mini"
            circle
            icon="el-icon-search"
            @click="showSearch = !showSearch"
          />
        </el-tooltip>
      </div>
    </el-row>

    <el-table
      v-loading="loading"
      :data="indexList"
      :header-cell-style="{ 'text-align': 'center' }"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="策略ID" align="center" prop="id" /> -->
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column
        label="安全指数名称"
        align="center"
        prop="securityName"
      />
      <el-table-column
        label="安全指数百分比(%)"
        align="center"
        prop="securityPercentage"
      />
      <el-table-column label="类型" align="left" prop="security">
        <template slot-scope="scope">
          <div style="text-align: center">
            <span
              :style="
                scope.row.security == '0'
                  ? 'color: #00aa00'
                  : 'color: #ff0000'
              "
            >
              {{ scope.row.security == '0' ? '加指标' : '扣指标' }}
            </span>
          </div>
        </template>
      </el-table-column>

      <!-- <el-table-column label="时间表" align="center" prop="schedulerTime" /> -->
      <el-table-column label="描述" align="center" prop="securityDescribe">
        <template v-slot="{row}">
          {{row.securityDescribe !== '' && row.securityDescribe !== null ? (row.securityDescribe == '3' ? '存在,加指标' : row.securityDescribe == '6' ? '存在,扣指标' : '存在,按照个数*指数百分比扣指标') : ''}}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:security:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:security:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加和编辑弹窗 -->
    <el-dialog :title="title" :visible.sync="drawer" width="500px" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="130px"
      >
        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select
            style="width: 80%"
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            clearable
            :disabled="sink == 'edit'"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="安全指数名称" prop="securityName">
          <el-select
            style="width: 80%"
            v-model="form.securityName"
            placeholder="请选择安全指数名称"
            clearable
            :disabled="sink == 'edit'"
          >
            <el-option
              v-for="item in securityNameData"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictLabel"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="安全指数百分比" prop="securityPercentage">
          <el-input
            style="width: 80%"
            v-model="form.securityPercentage"
            placeholder="请输入百分比"
          />%
        </el-form-item>
        <el-form-item label="类型" prop="security">
          <el-select
            v-model="form.security"
            placeholder="请选择指数类型"
            clearable
            :disabled="sink == 'edit'"
          >
            <el-option label="加指标" value="0"></el-option>
            <el-option label="扣指标" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="描述"
          prop="securityDescribe"
          style="height: 100px"
        >
          <el-radio-group v-model="form.securityDescribe" :disabled="sink == 'edit'">
            <el-radio label="3">存在,加指标</el-radio>
            <el-radio label="6">存在,扣指标</el-radio>
            <el-radio label="9">存在,按照个数*指数百分比扣指标</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          style="
            text-align: center;
            text-align: center;
            margin: 0;
            width: 100%;
          "
        >
          <el-button
            style="width: 30%"
            type="primary"
            :loading="submitBtnLoading"
            @click="submitForm"
          >保 存</el-button>
          <el-button style="width: 30%" @click="cancel">
            取 消
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import { listIndex, getIndex, delIndex, addIndex, updateIndex, exportIndex, getTuunelName } from "@/api/business/security.js";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "Index",
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
      // 安全指数表格数据
      indexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      drawer: false,
      // 新增修改弹窗确认按钮loading
      submitBtnLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        tunnelName: null,
        securityName: null,
        securityPercentage: null,
        security: null,
        securityDescribe: null,
      },
      // 表单参数
      form: {
        tunnelId: null,
        tunnelName: null,
        securityName: null,
        securityPercentage: null,
        security: null,
        securityDescribe: null,
      },
      // 表单校验
      rules: {
        tunnelId: [
          { required: true, message: "请选择隧道", trigger: "change" },
        ],
        securityName: [
          { required: true, message: "请输入安全指数名称", trigger: "blur" },
        ],
        securityPercentage: [
          { required: true, message: '请输入百分比', trigger: 'blur' },
          { pattern: /^100$|^(\d|[1-9]\d)$/, message: '请输入 0 ~ 100 之间的整数', trigger: 'blur' },
        ],
        security: [
          { required: true, message: "请选择类型", trigger: "change" }
        ]
      },
      direction: "rtl",
      sink: '', // add添加 edit编辑
      tunnelData: [], // 隧道名列表
      securityNameData: [] // 安全指数名称字典
    };
  },
  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_safety_index_names").then(response => {
      this.securityNameData = response.data;
    });
  },
  methods: {
    /** 查询安全指数列表 */
    getList() {
      this.loading = true;
      listIndex(this.queryParams).then(response => {
        this.indexList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询隧道下拉框 */
    getTunnels() {
      listTunnels().then((response) => {
        this.tunnelData = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.drawer = false;
      this.reset();
    },
    // 监听抽屉的关闭
    handleClose() {
      this.$confirm("确认关闭？").then(()=>{
        this.drawer = false
        this.reset();
      }).catch(()=>{})
    },
    // 表单重置
    reset() {
      this.form = {
        tunnelId: null,
        tunnelName: null,
        securityName: null,
        securityPercentage: null,
        security: null,
        securityDescribe: null,
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
      this.sink = "add";
      this.reset();
      this.drawer = true;
      this.title = "添加安全指数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.sink = "edit";

      getIndex(id).then(response => {
        this.form = response.data;
        this.drawer = true;
        this.title = "修改安全指数";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitBtnLoading) return
      this.submitBtnLoading = true
      this.$refs["form"].validate(async valid => {
        if (valid) {
          if (this.sink == 'edit') {
            await updateIndex(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.drawer = false;
              this.getList();
            });
          } else {
            await addIndex(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.drawer = false;
              this.getList();
            });
          }
        }
        this.submitBtnLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除选中数据项？').then(function() {
        return delIndex(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有安全指数数据项？').then(() => {
        this.exportLoading = true;
        return exportIndex(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>

<style lang="less" scoped>
.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}
label {
  display: block !important;
  margin-top: 10px;
}
</style>
