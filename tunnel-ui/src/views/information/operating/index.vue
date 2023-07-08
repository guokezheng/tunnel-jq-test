<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="是否结束" prop="closed">
        <el-select
          v-model="queryParams.closed"
          placeholder="请选择是否结束"
          clearable
          size="small"
        >
          <el-option label="未结束" value="0" />
          <el-option label="已结束" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否有效" prop="available">
        <el-select
          v-model="queryParams.available"
          placeholder="请选择是否有效"
          clearable
          size="small"
        >
          <el-option label="有效" value="0" />
          <el-option label="无效" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否推送" prop="published">
        <el-select
          v-model="queryParams.published"
          placeholder="请选择是否推送"
          clearable
          size="small"
        >
          <el-option label="已推送" value="0" />
          <el-option label="未推送" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="上报类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择上报类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
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
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:operating:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:operating:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:operating:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:operating:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="operatingList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="上报类型" align="center" prop="id" /> -->
      <el-table-column
        label="上报类型"
        align="center"
        prop="type"
        :formatter="typeFormat"
      />
      <el-table-column label="上报人员" align="center" prop="reportUser" />
      <el-table-column label="上报内容" align="center" prop="remark" />
      <el-table-column label="发生时间" align="center" prop="occurTime" />
      <el-table-column label="上报时间" align="center" prop="reportTime" />
      <el-table-column label="是否结束" align="center" prop="closed" />
      <el-table-column label="是否有效" align="center" prop="available" />
      <el-table-column label="是否推送" align="center" prop="published" />
      <!-- <el-table-column label="创建时间" align="center" prop="creatTime" />
      <el-table-column label="修改时间" align="center" prop="updataTime" /> -->

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
            v-hasPermi="['system:operating:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:operating:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改交通事件对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="事件编号" prop="evtId">
          <el-input v-model="form.evtId" placeholder="请输入事件编号" />
        </el-form-item> -->

        <el-form-item label="上报人员" prop="reportUser">
          <el-input v-model="form.reportUser" placeholder="请输入上报人员" />
        </el-form-item>

        <el-form-item label="上报类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择上报类型">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发生时间" prop="occurTime">
          <!-- <el-input v-model="form.occurTime" placeholder="请输入发生时间" /> -->
          <el-date-picker
            clearable
            v-model="form.occurTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请输入发生时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="上报时间" prop="reportTime">
          <!-- <el-input v-model="form.reportTime" placeholder="请输入上报时间" /> -->
          <el-date-picker
            clearable
            v-model="form.reportTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请输入上报时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否结束" prop="closed">
          <el-select v-model="form.closed" placeholder="请选择是否结束">
            <el-option label="未结束" value="0" />
            <el-option label="已结束" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否有效" prop="available">
          <el-select v-model="form.available" placeholder="请选择是否有效">
            <el-option label="有效" value="0" />
            <el-option label="无效" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否推送" prop="published">
          <el-select v-model="form.published" placeholder="请选择是否推送">
            <el-option label="已推送" value="0" />
            <el-option label="未推送" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="上报内容">
          <!-- <editor v-model="form.remark" :min-height="192" /> -->
          <el-input
            v-model="form.remark"
            placeholder="请输入上报内容"
            type="textarea"
          />
        </el-form-item>
        <!-- <el-form-item label="创建时间" prop="creatTime">
          <el-input v-model="form.creatTime" placeholder="请输入创建时间" />
        </el-form-item>
        <el-form-item label="修改时间" prop="updataTime">
          <el-input v-model="form.updataTime" placeholder="请输入修改时间" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listOperating,
  getOperating,
  delOperating,
  addOperating,
  updateOperating,
  exportOperating,
} from "@/api/board/operating";

export default {
  name: "Operating",
  components: {},
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
      // 交通事件表格数据
      operatingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      // 上报类型字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        closed: null,
        available: null,
        published: null,
        type: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
    this.getDicts("eventType").then((response) => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** 查询交通事件列表 */
    getList() {
      this.loading = true;
      listOperating(this.queryParams).then((response) => {
        this.operatingList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.operatingList.forEach((e) => {
          if (e.closed == 0) {
            e.closed = "未结束";
          } else if (e.closed == 1) {
            e.closed = "已结束";
          }
          if (e.available == 0) {
            e.available = "有效";
          } else if (e.available == 1) {
            e.available = "无效";
          }
          if (e.published == 0) {
            e.published = "未推送";
          } else if (e.published == 1) {
            e.published = "已推送";
          }
        });
      });
    },
    // 上报类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
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
        evtId: null,
        occurTime: null,
        reportTime: null,
        reportUser: null,
        closed: null,
        available: null,
        published: null,
        creatTime: null,
        updataTime: null,
        remark: null,
        type: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加交通事件";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getOperating(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交通事件";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitFormLoading) return
      this.submitFormLoading = true
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.form.id != null) {
            await updateOperating(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addOperating(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        this.submitFormLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除选中数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delOperating(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出交通事件数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportOperating(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
  },
};
</script>
