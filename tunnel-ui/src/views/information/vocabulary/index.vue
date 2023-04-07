<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:vocabulary:add']"
          >新增</el-button
        >
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:vocabulary:remove']"
          >删除</el-button
        >
        <el-button size="small" @click="handleExport">导出</el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.word"
            placeholder="请输入文本内容,回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            style="
              border-right: #00c8ff solid 1px !important;
              border-radius: 3px;
            "
          >
            <!-- <el-button
              slot="append"
              icon="el-icon-s-fold"
              @click="boxShow = !boxShow"
            ></el-button> -->
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- <div ref="cc" class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
      <el-form-item label="文本" prop="word" style="width:100%">
        <el-input
          v-model="queryParams.word"
          placeholder="请输入文本"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
    </div> -->
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:vocabulary:add']"
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
          v-hasPermi="['system:vocabulary:edit']"
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
          v-hasPermi="['system:vocabulary:remove']"
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
          v-hasPermi="['system:vocabulary:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row> -->
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="vocabularyList"
      max-height="62vh"
      @selection-change="handleSelectionChange"
      :default-sort="{ prop: 'creatTime', order: 'descending' }"
      class="allTable"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column
        label="序号"
        align="center"
        :index="indexMethod"
        type="index"
        width="50"
      >
      </el-table-column>
      <el-table-column label="文本" align="center" prop="word" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        v-if="userName != 'jlyadmin'"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:vocabulary:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:vocabulary:remove']"
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

    <!-- 添加或修改情报板敏感字管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文本" prop="word">
          <el-input v-model.trim="form.word" placeholder="请输入文本" />
        </el-form-item>
        <!-- <el-form-item label="创建时间" prop="creatTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.creatTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择创建时间"
          >
          </el-date-picker>
        </el-form-item> -->
      </el-form>
      <div class="dialog-footer">
        <el-button
          class="submitButton"
          :loading="submitFormLoading"
          @click="submitForm"
          >确 定</el-button
        >
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listVocabulary,
  getVocabulary,
  delVocabulary,
  addVocabulary,
  updateVocabulary,
  exportVocabulary,
} from "@/api/board/vocabulary";

export default {
  name: "Vocabulary",
  components: {},
  data() {
    return {
      userName: this.$store.state.user.name,
      //用户
      boxShow: false,
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
      // 情报板敏感字管理表格数据
      vocabularyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        creatTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        word: [
          { required: true, message: "文本不能为空", trigger: "blur" },
          { min: 1, max: 250, message: "长度为1~250个字符", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    console.log(this.userName,"用户")
  },
  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    /** 查询情报板敏感字管理列表 */
    getList() {
      this.loading = true;
      listVocabulary(this.queryParams).then((response) => {
        this.vocabularyList = response.rows;
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
        word: null,
        creatTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryParams");
      this.queryParams.ids=[];
      this.queryParams.word = null;
      this.queryParams.ids = [];
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
      this.title = "添加情报板敏感字管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getVocabulary(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改情报板敏感字管理";
      });
    },
    /** 提交按钮 */
    async submitForm() {
      this.submitFormLoading = true;
      await this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateVocabulary(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVocabulary(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
      this.submitFormLoading = false;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delVocabulary(ids);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo ="是否确认导出所有的敏感字管理数据项？";
      if(this.ids.length>0){
        confirmInfo = "是否确认导出所选的敏感字管理数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      //查看当前ids是否存在,如果存在。则按照当前ids进行导出。
      this.$confirm(confirmInfo, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportVocabulary(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = ''
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.searchBox {
  position: absolute;
  top: 8%;
  right: 1%;
  width: 24%;
  z-index: 1996;
  background-color: #00335a;
  padding: 20px;
  box-sizing: border-box;
  ::v-deep .el-form-item__content {
    width: 80%;
    .el-select {
      width: 100%;
    }
  }
  .bottomBox {
    width: 100%;
    ::v-deep .el-form-item__content {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
    }
  }
}
</style>
