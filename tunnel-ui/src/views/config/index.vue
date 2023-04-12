<template>
  <div class="app-container">

    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          v-hasPermi="['config:config:add']"
          size="small"
          @click="handleAdd()"
        >新增
        </el-button>
        <el-button size="small" :loading="exportLoading"
                   @click="handleExport"
                   v-hasPermi="['config:config:export']"
        >导出
        </el-button>
        <el-button size="small" @click="resetQuery"
        >刷新
        </el-button
        >
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入页面名称、页面标识符"
            v-model="queryParams.searchValue"
            @keyup.enter.native="handleQuery"
            size="small"
            style="border-right: solid 1px #00c8ff; border-radius: 3px"
          >
          </el-input>
        </div>
      </el-col>
    </el-row>



    <el-table v-loading="loading" :data="configList"  class="allTable" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="页面配置ID" align="center" prop="id"/>
      <el-table-column label="所属部门" align="center" prop="deptId"/>
      <el-table-column label="页面名称" align="center" prop="name"/>
      <el-table-column label="页面标识符" align="center" prop="code"/>
      <el-table-column label="页面路径" align="center" prop="url"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['config:config:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['config:config:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改数字孪生页面配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body class="addUserDialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="归属部门" prop="deptId">
          <treeselect
            v-model="form.deptId"
            :options="deptOptions"
            :disable-branch-nodes="true"
            placeholder="请选择归属部门"
            @input="changeParentDept"
          />
        </el-form-item>
        <el-form-item label="页面名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入页面名称"/>
        </el-form-item>
        <el-form-item label="页面标识符" prop="code">
          <el-input v-model="form.code" placeholder="请输入页面标识符"/>
        </el-form-item>
        <el-form-item label="页面路径" prop="url">
          <el-input v-model="form.url" placeholder="请输入页面路径"/>
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
import {listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig} from "@/api/config/config";
import {treeSelectYG1} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Config",
  components: {Treeselect},
  data() {
    return {
      lx_boxShow: false,
      // 部门树选项
      deptOptions: undefined,
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
      // 数字孪生页面配置表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId: null,
        name: null,
        code: null,
        url: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          {required: true, message: "所属部门名称不能为空", trigger: "change"}
        ],
        name: [
          {required: true, message: "页面名称不能为空", trigger: "blur"}
        ],
        code: [
          {required: true, message: "页面标识符不能为空", trigger: "blur"}
        ],
        url: [
          {required: true, message: "页面路径不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.lx_boxShow == true) {
          self.lx_boxShow = false;
        }
      }
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeSelectYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    changeParentDept() {
      this.$refs.form.validateField("deptId");
    },
    /** 查询数字孪生页面配置列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then(response => {
        this.configList = response.rows;
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
        deptId: null,
        name: null,
        code: null,
        url: null,
        createBy: null,
        createTime: null,
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
      this.form.pageNum = 1;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.title = "添加数字孪生页面配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数字孪生页面配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除数字孪生页面配置编号为"' + ids + '"的数据项？').then(function () {
        return delConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有数字孪生页面配置数据项？').then(() => {
        this.exportLoading = true;
        return exportConfig(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
<style scoped lang="scss">
.vue-treeselect__single-value {
  color: #ffffff !important;
}
</style>

