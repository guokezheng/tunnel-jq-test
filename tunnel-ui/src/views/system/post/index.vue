<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
        <el-button
          v-hasPermi="['system:post:add']"
          size="small"
          @click="handleAdd()"
          >新增岗位
        </el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:post:export']"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="14">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入岗位名称、编码，回车搜索"
            v-model="queryParams.postCode"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="post_boxShow = !post_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="post_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="岗位状态" prop="status">
          <el-select
            v-model="queryParams.status"
            clearable
            placeholder="请选择岗位状态"
            size="small"
          >
            <el-option
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <!--    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="岗位编码" prop="postCode">
        <el-input
          v-model="queryParams.postCode"
          placeholder="请输入岗位编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位名称" prop="postName">
        <el-input
          v-model="queryParams.postName"
          placeholder="请输入岗位名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="岗位状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:post:add']"
        >新增</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:post:edit']"
        >修改</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:post:remove']"
        >删除</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:post:export']"
        >导出</el-button>
      </el-form-item>
    </el-form>-->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:post:add']"
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
          v-hasPermi="['system:post:edit']"
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
          v-hasPermi="['system:post:remove']"
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
          v-hasPermi="['system:post:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="postList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      :row-key="getRowKey"
      height="62vh"
      class="allTable"
      ref="tableFile"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="岗位编号" align="center" prop="postId" />
      <el-table-column label="岗位编码" align="center" prop="postCode" />
      <el-table-column label="岗位名称" align="center" prop="postName" />
      <!--      <el-table-column label="岗位排序" align="center" prop="postSort" />-->
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/> -->
          <span
            :style="{
              color: scope.row.status == '0' ? '#00FF00' : 'red',
            }"
            >{{ getStatus(scope.row.status) }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:post:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:post:remove']"
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

    <!-- 添加或修改岗位对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :before-close="cancel"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="form.postCode" placeholder="请输入编码名称" />
        </el-form-item>
        <!--        <el-form-item label="岗位顺序" prop="postSort">
          <el-input-number v-model="form.postSort" controls-position="right" :min="0" />
        </el-form-item>-->
        <el-form-item label="岗位状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitForm" class="submitButton">确 定</el-button>
        <el-button @click="cancel" class="closeButton">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listPost,
  getPost,
  delPost,
  addPost,
  updatePost,
  exportPost,
} from "@/api/system/post";

export default {
  name: "Post",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      post_boxShow: false,
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
      // 岗位表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        postName: [
          { required: true, message: "岗位名称不能为空", trigger: "blur" },
        ],
        postCode: [
          { required: true, message: "岗位编码不能为空", trigger: "blur" },
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]+$/,
            message: "仅限输入数字/字母",
            trigger: "blur",
          },
        ],
        /*postSort: [
          { required: true, message: "岗位顺序不能为空", trigger: "blur" }
        ]*/
      },
      sysNormalDisableList: [], //状态
    };
  },
  created() {
    this.getList();
    //状态
    this.getDicts("sys_normal_disable").then((response) => {
      this.sysNormalDisableList = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    getStatus(row) {
      return this.selectDictLabel(this.sysNormalDisableList, row);
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.post_boxShow == true) {
          self.post_boxShow = false;
        }
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      listPost(this.queryParams).then((response) => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: "0",
        remark: undefined,
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
      this.queryParams.postCode = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.postId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加岗位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const postId = row.postId || this.ids;
      getPost(postId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改岗位";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.postId != undefined) {
            updatePost(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.$refs.tableFile.clearSelection();
              this.open = false;
              this.getList();
            });
          } else {
            addPost(this.form).then((response) => {
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
      let that = this;
      const postIds = row.postId || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delPost(postIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          that.$refs.tableFile.clearSelection();
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的岗位管理数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的岗位管理数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportPost(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.$refs.tableFile.clearSelection();
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>
