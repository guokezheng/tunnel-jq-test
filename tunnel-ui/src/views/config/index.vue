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
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['config:config:export']"
          >导出
        </el-button>
        <el-button size="small" @click="resetQuery">刷新 </el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入页面名称、页面标识符，回车搜索"
            v-model="queryParams.searchValue"
            @keyup.enter.native="handleQuery"
            size="small"
            class="formNoIcon"
          >
          </el-input>
        </div>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="configList"
      class="allTable"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      ref="tableFile"
      :row-key="getRowKey"
      height="62vh"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="页面配置ID" align="center" prop="id"/>-->
      <el-table-column label="所属部门" align="center" prop="deptId" />
      <el-table-column
        label="所属部门"
        align="center"
        prop="configModule"
        :formatter="configModuleFormat"
      />
      <el-table-column label="隧道名称" align="center" prop="tunnelId" />
      <el-table-column label="页面名称" align="center" prop="name" />
      <el-table-column label="页面标识符" align="center" prop="code" />
      <el-table-column label="页面路径" align="center" prop="url" />
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
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改嵌入页面配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      class="addUserDialog"
      :before-close="cancel"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="归属部门" prop="deptId">
          <treeselect
            @select="getTunnel"
            v-model="form.deptId"
            :options="deptOptions"
            :disable-branch-nodes="true"
            noResultsText="暂无数据"
            placeholder="请选择归属部门"
            @input="changeParentDept"
          />
        </el-form-item>
        <el-form-item label="所属隧道" prop="eqTunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择所属隧道"
            style="width: 100%"
          >
            <el-option
              v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属模块" prop="configModule">
          <el-select v-model="form.configModule" placeholder="请选择所属模块">
            <el-option
              v-for="item in configModuleList"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="页面名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入页面名称" />
        </el-form-item>
        <el-form-item label="页面标识符" prop="code">
          <el-input v-model="form.code" placeholder="请输入页面标识符" />
        </el-form-item>
        <el-form-item label="页面路径" prop="url">
          <el-input v-model="form.url" placeholder="请输入页面路径" />
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
  listConfig,
  getConfig,
  delConfig,
  addConfig,
  updateConfig,
  exportConfig,
} from "@/api/config/config";
import { treeSelectYG1 } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {listTunnels} from "@/api/equipment/tunnel/api";

export default {
  name: "Config",
  components: { Treeselect },
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
      // 嵌入页面配置表格数据
      configList: [],
      // 配置模块列表
      configModuleList: [],
      //所属隧道
      eqTunnelData: {},
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
          {
            required: true,
            message: "所属部门名称不能为空",
            trigger: "change",
          },
        ],
        configModule: [
          { required: true, message: "页面配置模块不能为空", trigger: "blur" },
        ],
        name: [
          { required: true, message: "页面名称不能为空", trigger: "blur" },
          { max: 50, message: "最长输入50个字符", trigger: "blur" },
        ],
        tunnelId: [
          { required: true, message: "所属隧道不能为空", trigger: "blur" },
        ],
        code: [
          { required: true, message: "页面标识符不能为空", trigger: "blur" },
          { max: 50, message: "最长输入50个字符", trigger: "blur" },
        ],
        url: [
          { required: true, message: "页面路径不能为空", trigger: "blur" },
          { max: 300, message: "最长输入300个字符", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sd_config_module").then((response) => {
      this.configModuleList = response.data;
    });
  },
  methods: {
    configModuleFormat(row, column) {
      return this.selectDictLabel(this.configModuleList, row.configModule);
    },
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.lx_boxShow == true) {
          self.lx_boxShow = false;
        }
      }
    },
    /** 所属隧道 */
    getTunnel(obj) {
      this.queryParams.deptId = obj.id;
      console.log("this.queryParams",this.queryParams);
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.eqTunnelData = response.rows;
      });
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
    /** 查询嵌入页面配置列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then((response) => {
        this.configList = response.rows;
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
        id: null,
        deptId: null,
        name: null,
        code: null,
        url: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      // this.form.pageNum = 1;
      this.queryParams.searchValue = "";
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
      this.eqTunnelData = [];
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.title = "添加嵌入页面配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.eqTunnelData = [];
      this.reset();
      const id = row.id || this.ids;
      getConfig(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改嵌入页面配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateConfig(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addConfig(this.form).then((response) => {
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
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除嵌入页面配置编号为"' + ids + '"的数据项？')
        .then(function () {
          return delConfig(ids);
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
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有嵌入页面配置数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportConfig(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
        })
        .catch(() => {});
    },
  },
};
</script>
<style scoped lang="scss">
.vue-treeselect__single-value {
  color: #ffffff !important;
}
</style>

