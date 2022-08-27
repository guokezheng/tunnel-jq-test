<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="演习名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入演习名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="演习类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择演习类型"
          clearable
          size="small"
        >
          <el-option v-for="(dict,i) in rehearsalTypeOptions" :key="i" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
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
      <el-form-item label="负责人" prop="person">
        <el-input
          v-model="queryParams.person"
          placeholder="请输入负责人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="联系方式" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系方式"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="演习时间" prop="drillTime">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.drillTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择演习时间"
        >
        </el-date-picker>
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
          v-hasPermi="['business:emeDrill:add']"
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
          v-hasPermi="['business:emeDrill:edit']"
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
          v-hasPermi="['business:emeDrill:remove']"
          >删除</el-button
        >
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['business:emeDrill:export']"-->
<!--          >导出</el-button-->
<!--        >-->
<!--      </el-col>-->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="emeDrillList"
      @selection-change="handleSelectionChange"
      :default-sort = "{prop: 'drillTime', order: 'descending'}"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="ID" align="center" prop="id" /> -->
      <el-table-column label="演习名称" align="center" prop="name" />
      <el-table-column label="演习描述" align="center" prop="content" />
      <el-table-column label="演习类型" align="center" prop="type" :formatter="rehearsalFormat" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="负责人" align="center" prop="person" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column
        label="演习时间"
        align="center"
        prop="drillTime"
        width="180"
        sortable
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.drillTime, "{y}-{m}-{d}") }}</span>
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
            v-hasPermi="['business:emeDrill:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:emeDrill:remove']"
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

    <!-- 添加或修改应急演练对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="演习名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入演习名称" />
        </el-form-item>
        <el-form-item label="演习描述">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="演习类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择演习类型" style="width: 100%">
            <el-option v-for="(item,i) in rehearsalTypeOptions" :key="i" :label="item.dictLabel" :value="item.dictValue" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item> -->
        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="person">
          <el-input v-model="form.person" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="演习时间" prop="drillTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.drillTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择演习时间"
            style="width: 100%"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitBtnLoading" @click="submitForm" v-prevent-click>确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEmeDrill,
  getEmeDrill,
  delEmeDrill,
  addEmeDrill,
  updateEmeDrill,
  exportEmeDrill,
} from "@/api/business/emeDrill";
import Editor from "@/components/Editor";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "EmeDrill",
  components: {
    Editor,
  },
  data() {
    return {
      tunnelData: [],
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
      // 应急演练表格数据
      emeDrillList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 新增修改弹窗确认按钮loading
      submitBtnLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        content: null,
        type: null,
        tunnelId: null,
        person: null,
        phone: null,
        drillTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 演练类型字典
      rehearsalTypeOptions: []
    };
  },
  created() {
    this.getTunnels();
    this.getList();

    this.getDicts("sd_rehearsal_type").then(response => {
      this.rehearsalTypeOptions = response.data;
    });
  },
  mounted() {},
  methods: {
    // 隧道名称 下拉框
    getTunnels() {
      listTunnels().then((response) => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询应急演练列表 */
    getList() {
      this.loading = true;
      listEmeDrill(this.queryParams).then((response) => {
        this.emeDrillList = response.rows;
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
        name: null,
        content: null,
        type: null,
        tunnelId: null,
        person: null,
        phone: null,
        drillTime: null,
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
      this.title = "添加应急演练";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getEmeDrill(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应急演练";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitBtnLoading) return
      this.submitBtnLoading = true
      this.form.content = this.form.content
        .replace("<p>", "")
        .replace("</p>", "");
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.form.id != null) {
            await updateEmeDrill(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addEmeDrill(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
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
      this.$confirm(
        '是否确认删除应急演练?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delEmeDrill(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有应急演练数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportEmeDrill(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
    // 演习类型字典翻译
    rehearsalFormat(row, column) {
      return this.selectDictLabel(this.rehearsalTypeOptions, row.type);
    }
  },
};
</script>
