<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            @node-click="handleNodeClick"
            style="height: 690px; overflow: auto"
          />
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-row :gutter="20" class="mb8">
          <!-- 全局搜索 -->
          <el-row :gutter="20" class="topFormRow">
            <el-col :span="6">
              <el-button
                v-hasPermi="['system:user:add']"
                size="small"
                @click="handleAdd()"
                >新增用户
              </el-button>
              <el-button
                size="small"
                @click="handleImport"
                v-hasPermi="['system:user:import']"
                >导入</el-button
              >
              <el-button
                size="small"
                :loading="exportLoading"
                @click="handleExport"
                v-hasPermi="['system:user:export']"
                >导出</el-button
              >
              <el-button size="small" @click="resetQuery">刷新</el-button>
            </el-col>
            <el-col :span="7" :offset="11">
              <div class="grid-content bg-purple" ref="main">
                <el-input
                  placeholder="请输入用户名称、手机号码，回车搜索"
                  v-model="queryParams.userName"
                  @keyup.enter.native="handleQuery"
                >
                  <el-button
                    slot="append"
                    class="searchTable"
                    @click="user_boxShow = !user_boxShow"
                  ></el-button>
                </el-input>
              </div>
            </el-col>
          </el-row>
          <div
            class="treeSearchBox searchBox"
            v-show="user_boxShow"
            ref="cc"
            style="top: 53px !important"
          >
            <el-form
              ref="queryForm"
              :inline="true"
              :model="queryParams"
              label-width="75px"
            >
              <el-form-item label="用户状态" prop="status">
                <el-select
                  v-model="queryParams.status"
                  placeholder="请选择用户状态"
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间">
                <el-date-picker
                  v-model="dateRange"
                  size="small"
                  style="width: 100%"
                  value-format="yyyy-MM-dd"
                  type="daterange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :picker-options="setoptions"
                ></el-date-picker>
              </el-form-item>
              <el-form-item class="bottomBox">
                <el-button size="small" type="primary" @click="handleQuery"
                  >搜索</el-button
                >
                <el-button size="small" @click="resetQuery" type="primary" plain
                  >重置</el-button
                >
                <!--            <el-col :span="1.5">-->

                <!--            </el-col>-->
                <!-- <right-toolbar
              :showSearch.sync="showSearch"
              @queryTable="getList"
              :columns="columns"
            ></right-toolbar> -->
              </el-form-item>
            </el-form>
          </div>
          <div class="tableTopHr"></div>
          <el-table
            v-loading="loading"
            :data="userList"
            height="62vh"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick"
            class="allTable"
            :row-key="getRowKey"
            ref="tableFile"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column
              label="用户编号"
              align="center"
              key="userId"
              prop="userId"
              v-if="columns[0].visible"
            />
            <el-table-column
              label="用户名称"
              align="center"
              key="userName"
              prop="userName"
              v-if="columns[1].visible"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="用户昵称"
              align="center"
              key="nickName"
              prop="nickName"
              v-if="columns[2].visible"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="部门"
              align="center"
              key="deptName"
              prop="dept.deptName"
              v-if="columns[3].visible"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="手机号码"
              align="center"
              key="phonenumber"
              prop="phonenumber"
              v-if="columns[4].visible"
              width="140"
            />
            <el-table-column
              label="状态"
              align="center"
              key="status"
              v-if="columns[5].visible"
            >
              <template slot-scope="scope" v-if="scope.row.userId !== 1">
                <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column
              label="创建时间"
              align="center"
              prop="createTime"
              v-if="columns[6].visible"
              width="200"
              sortable
            >
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="180"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope" v-if="scope.row.userId !== 1">
                <el-button
                  size="mini"
                  class="tableBlueButtton"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:user:edit']"
                  >修改</el-button
                >
                <el-button
                  size="mini"
                  class="tableDelButtton"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['system:user:remove']"
                  >删除</el-button
                >
                <el-dropdown
                  size="mini"
                  @command="(command) => handleCommand(command, scope.row)"
                  v-hasPermi="['system:user:resetPwd', 'system:user:edit']"
                >
                  <!-- <span class="el-dropdown-link">
                <i class="el-icon-d-arrow-right el-icon--right"></i>更多
              </span> -->
                  <el-button
                    size="mini"
                    class="tableBlueButtton"
                    style="margin-left: 10px"
                    >更多</el-button
                  >
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      command="handleResetPwd"
                      icon="el-icon-key"
                      v-hasPermi="['system:user:resetPwd']"
                      >重置密码</el-dropdown-item
                    >
                    <el-dropdown-item
                      command="handleAuthRole"
                      icon="el-icon-circle-check"
                      v-hasPermi="['system:user:edit']"
                      >分配角色</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </el-dropdown>
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
        </el-row>
      </el-col>

      <!-- 添加或修改参数配置对话框 -->
      <el-dialog
        :title="title"
        :visible.sync="open"
        width="600px"
        append-to-body
        class="addUserDialog"
        :before-close="cancel"
        :close-on-click-modal="false"
      >
        <div class="dialogStyleBox">
          <div class="dialogLine"></div>
          <div class="dialogCloseButton"></div>
        </div>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户昵称" prop="nickName">
                <el-input
                  v-model="form.nickName"
                  placeholder="请输入用户昵称"
                  maxlength="30"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="归属部门" prop="deptId">
                <treeselect
                  v-model="form.deptId"
                  :options="deptOptions"
                  :show-count="true"
                  noResultsText="暂无数据"
                  placeholder="请选择归属部门"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="手机号码" prop="phonenumber">
                <el-input
                  v-model="form.phonenumber"
                  placeholder="请输入手机号码"
                  maxlength="11"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input
                  v-model="form.email"
                  placeholder="请输入邮箱"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item
                v-if="form.userId == undefined"
                label="用户名称"
                prop="userName"
              >
                <el-input
                  v-model="form.userName"
                  placeholder="请输入用户名称"
                  maxlength="30"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                v-if="form.userId == undefined"
                label="用户密码"
                prop="password"
              >
                <el-input
                  v-model="form.password"
                  placeholder="请输入用户密码"
                  type="password"
                  maxlength="20"
                  show-password
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户性别">
                <el-select v-model="form.sex" placeholder="请选择">
                  <el-option
                    v-for="dict in dict.type.sys_user_sex"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                    >{{ dict.label }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="岗位">
                <el-select v-model="form.postIds" multiple placeholder="请选择">
                  <el-option
                    v-for="item in postOptions"
                    :key="item.postId"
                    :label="item.postName"
                    :value="item.postId"
                    :disabled="item.status == 1"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色" prop="roleIds">
                <el-select v-model="form.roleIds" multiple placeholder="请选择">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                    :disabled="item.status == 1"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input
                  v-model="form.remark"
                  type="textarea"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button class="submitButton" @click="submitForm">确 定</el-button>
          <el-button class="closeButton" @click="cancel">取 消</el-button>
        </div>
      </el-dialog>

      <!-- 用户导入对话框 -->
      <el-dialog
        :title="upload.title"
        :visible.sync="upload.open"
        width="400px"
        append-to-body
        :close-on-click-modal="false"
      >
        <div class="dialogStyleBox">
          <div class="dialogLine"></div>
          <div class="dialogCloseButton"></div>
        </div>
        <el-upload
          ref="upload"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            <div class="el-upload__tip" slot="tip">
              <el-checkbox v-model="upload.updateSupport" />
              更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >下载模板</el-link
            >
          </div>
        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button class="submitButton" @click="submitFileForm">确 定</el-button>
          <el-button class="closeButton" @click="upload.open = false">取 消</el-button>
        </div>
      </el-dialog>
    </el-row>
  </div>
</template>

<script>
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  exportUser,
  resetUserPwd,
  changeUserStatus,
  importTemplate,
} from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { treeselect, treeselectExcYG1 } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  dicts: ["sys_normal_disable", "sys_user_sex"],
  components: { Treeselect },
  data() {
    return {
      user_boxShow: false,
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData",
      },
      setoptions: {
        // 时间不能大于当前时间
        disabledDate: (time) => {
          return time.getTime() > Date.now();
        },
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true },
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "用户名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        deptId: [{ required: true, message: "部门不能为空", trigger: "blur" }],
        roleIds: [
          { required: true, message: "用户角色不能为空", trigger: "blur" },
        ],
      },
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getConfigKey("sys.user.initPassword").then((response) => {
      this.initPassword = response.msg;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleRowClick(row, i, a) {
      if (i.label != "状态") {
        this.$refs.tableFile.toggleRowSelection(row);
      }
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.userId;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (self.user_boxShow == true) {
        if (
          !this.$refs.main.contains(e.target) &&
          !this.$refs.cc.contains(e.target)
        ) {
          if (self.user_boxShow == true) {
            self.user_boxShow = false;
          }
        }
      }
      
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      this.user_boxShow = false;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm('确认要"' + text + '""' + row.userName + '"用户吗？')
        .then(function () {
          return changeUserStatus(row.userId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: [],
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
      this.dateRange = [];
      this.resetForm("queryForm");
      // Object.keys(this.queryParams).forEach(key => {
      //   this.queryParams[key] = ""
      // })
      this.deptName = ''
      this.queryParams.userName = "";
      this.queryParams.deptId = "";
      this.queryParams.ids = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      getUser().then((response) => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "添加用户";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const userId = row.userId || this.ids;
      getUser(userId).then((response) => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.form.postIds = response.postIds;
        this.form.roleIds = response.roleIds;
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,}/,
        inputErrorMessage: "密码必须由8位及以上大小写字母，数字，特殊字符组成",
      })
        .then(({ value }) => {
          resetUserPwd(row.userId, value).then((response) => {
            this.$modal.msgSuccess("修改成功，新密码是：" + value);
            this.$refs.tableFile.clearSelection();
          });
        })
        .catch(() => {
          this.$refs.tableFile.clearSelection();
        });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addUser(this.form).then((response) => {
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
      const userIds = row.userId || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delUser(userIds);
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
      let confirmInfo = "是否确认导出所有的用户管理数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的用户管理数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportUser(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.$refs.tableFile.clearSelection();
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then((response) => {
        this.$download.name(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 表格样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
  },
};
</script>
