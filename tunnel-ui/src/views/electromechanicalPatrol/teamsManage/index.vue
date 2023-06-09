<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          v-hasPermi="['system:teams:add']"
          size="small"
          @click="handleAdd()"
          >新增
        </el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:teams:export']"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入班组名称"
            v-model="queryParams.deptName"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            style="
              border-right: #00c8ff solid 1px !important;
              border-radius: 3px;
            "
          >
            <!--            <el-button
              slot="append"
              class="searchTable"

            ></el-button>-->
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="teamsList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      height="62vh"
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
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="deptName"
        label="班组名称"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="parentId"
        label="所属部门"
        align="center"
      ></el-table-column>
      <!--      <el-table-column
        prop="orderNum"
        label="排序"
        align="center"
      ></el-table-column>-->
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <!-- <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          /> -->
          <span
            :style="{
              color: scope.row.status == '0' ? '#00FF00' : 'red',
            }"
            >{{ getStatus(scope.row.status) }}</span
          >
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope" v-if="scope.row.roleId !== 1">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            size="mini"
            style="margin-left: 10px"
            class="tableBlueButtton"
            @click="handleAuthUser(scope.row)"
            >包含用户</el-button
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

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :before-close="cancel"
      width="600px"
      append-to-body
      class="addUserDialog"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect
                v-model="form.parentId"
                :disable-branch-nodes="true"
                :options="deptOptions"
                noResultsText="暂无数据"
                placeholder="选择上级部门"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="班组名称" prop="deptName">
              <el-input v-model="form.deptName" plac eholder="请输入班组名称" />
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number
                v-model="form.orderNum"
                controls-position="right"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>-->
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input
                v-model="form.leader"
                placeholder="请输入负责人"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="请输入联系电话"
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
          <el-col :span="12">
            <el-form-item label="部门状态">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 包含用户弹窗对话框 -->
    <el-dialog
      title="包含用户"
      class="batch-table operationDiglog explain-table"
      :visible.sync="teamsUserOpen"
      :before-close="teamsUserCancel"
      width="1000px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-row :gutter="20" style="margin: 10px 0px 6px; display: flex">
        <el-col>
          <el-button size="small" @click="openSelectTeamsUser"
            >添加用户
          </el-button>
          <el-button
            size="small"
            :disabled="multiple"
            @click="cancelAuthUserAll"
            >批量取消</el-button
          >
          <el-button size="small" @click="resetQueryUser">刷新</el-button>
        </el-col>
        <el-col>
          <div class="grid-content bg-purple" ref="main">
            <el-input
              placeholder="请输入用户昵称、手机号码，回车搜索"
              v-model="queryParamsUser.userName"
              @keyup.enter.native="handleQueryUser"
              size="small"
              style="
                border-right: #00c8ff solid 1px !important;
                border-radius: 3px;
              "
            >
            </el-input>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-table
          ref="tables"
          :data="userList"
          v-loading="loadingUser"
          @selection-change="handleUserSelectionChange"
          max-height="430px"
        >
          <el-table-column type="selection" align="center" />
          <el-table-column
            type="index"
            :index="indexMethod"
            label="序号"
            width="68"
            align="center"
          ></el-table-column>
          <el-table-column
            label="用户名称"
            prop="userName"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="用户昵称"
            prop="nickName"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属部门"
            align="center"
            prop="dept.deptName"
            :show-overflow-tooltip="true"
          />
          <!--          <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />-->
          <el-table-column
            label="手机"
            align="center"
            prop="phonenumber"
            :show-overflow-tooltip="true"
          />
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
          <!--          <el-table-column label="创建时间" align="center" prop="createTime" width="220" sortable>
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>-->
          <el-table-column
            label="操作"
            align="center"
            width="180"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="small"
                class="tableDelButtton"
                @click="cancelAuthUser(scope.row)"
                >取消</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="totalUser > 0"
          :total="totalUser"
          :page.sync="queryParamsUser.pageNum"
          :limit.sync="queryParamsUser.pageSize"
          @pagination="getUserList"
        />
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button class="closeButton" @click="teamsUserCancel">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 添加用户弹窗对话框 -->
    <el-dialog
      title="选择用户"
      class="operationDiglog explain-table"
      :visible.sync="teamsUserSelect"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-row class="topFormRow">
        <el-col :span="6">
          <el-button size="small" @click="resetQueryUnUser">刷新</el-button>
        </el-col>
        <el-col :span="10" :offset="8">
          <div class="grid-content bg-purple" ref="main">
            <el-input
              placeholder="请输入用户昵称、手机号码，回车搜索"
              v-model="queryParamsUnuser.userName"
              @keyup.enter.native="handleQueryUnUser"
              size="small"
              style="
                border-right: #00c8ff solid 1px !important;
                border-radius: 3px;
              "
            >
            </el-input>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-table
          ref="tables"
          :data="unUserList"
          @selection-change="handleUnSelectionChange"
          max-height="430px"
        >
          <el-table-column type="selection" align="center" />
          <el-table-column
            type="index"
            :index="indexMethod"
            label="序号"
            width="50"
            align="center"
          ></el-table-column>
          <el-table-column
            label="用户名称"
            prop="userName"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="用户昵称"
            prop="nickName"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属部门"
            prop="dept.deptName"
            align="center"
            width="130"
            :show-overflow-tooltip="true"
          />
          <!--          <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />-->
          <el-table-column
            label="手机"
            prop="phonenumber"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <!-- <dict-tag
                :options="dict.type.sys_normal_disable"
                :value="scope.row.status"
              /> -->
              <span
                :style="{
                  color: scope.row.status == '0' ? '#00FF00' : 'red',
                }"
                >{{ getStatus(scope.row.status) }}</span
              >
            </template>
          </el-table-column>
          <!--          <el-table-column label="创建时间" align="center" prop="createTime" width="220" sortable>
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>-->
        </el-table>
        <pagination
          v-show="totalUnUser > 0"
          :total="totalUnUser"
          :page.sync="queryParamsUnuser.pageNum"
          :limit.sync="queryParamsUnuser.pageSize"
          @pagination="getListUnuser"
        />
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="handleSelectUser"
          >确 定</el-button
        >
        <el-button class="closeButton" @click="teamsUserSelect = false"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { treeSelectYG1 } from "@/api/system/dept";
import {
  addTeams,
  deleteTeamsUserCancel,
  deleteTeamsUserCancelAll,
  delTeams,
  exportTeams,
  getTeams,
  listTeams,
  teamsUserList,
  teamsUserSelectAll,
  unTeamsUserList,
  updateTeams,
} from "@/api/electromechanicalPatrol/teamsManage/teams";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import authTeamsUser from "./authTeamsUser";
import selectUser from "@/views/electromechanicalPatrol/teamsManage/selectUser";

export default {
  name: "teams",
  components: { Treeselect, authTeamsUser },
  dicts: ["sys_normal_disable"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 遮罩层
      loadingUser: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 选中的用户数组
      userIds: [],
      //未选中的用户数组
      unUserIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 已授权用户总条数
      totalUser: 0,
      // 未授权用户总条数
      totalUnUser: 0,
      // 表格树数据
      teamsList: [],
      // 已包含用户表格数据
      userList: [],
      // 未包含用户表格数据
      unUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示包含用户弹出层
      teamsUserOpen: false,
      // 是否显示选择用户弹出层
      teamsUserSelect: false,

      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 查询参数
      queryParamsUser: {
        pageNum: 1,
        pageSize: 10,
        userName: "",
      },
      queryParamsUnuser: {
        pageNum: 1,
        pageSize: 10,
        userName: "",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parentId: [
          { required: true, message: "上级部门不能为空", trigger: "blur" },
        ],
        deptName: [
          { required: true, message: "班组名称不能为空", trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
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

  methods: {
    teamsUserCancel() {
      this.teamsUserOpen = false;
      this.$refs.tableFile.clearSelection();
    },
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    getStatus(row) {
      return this.selectDictLabel(this.sysNormalDisableList, row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.deptId;
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },

    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询班组列表 */
    getList() {
      this.loading = true;
      listTeams(this.queryParams).then((response) => {
        console.log(response.rows, "查询班组列表");
        this.teamsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 查询授权用户列表 */
    getUserList() {
      this.loadingUser = true;
      teamsUserList(this.queryParamsUser).then((response) => {
        this.userList = response.rows;
        this.totalUser = response.total;
        this.loadingUser = false;
      });
    },
    // 查询未授权表数据
    getListUnuser() {
      unTeamsUserList(this.queryParamsUnuser).then((res) => {
        this.unUserList = res.rows;
        this.totalUnUser = res.total;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQueryUser() {
      this.queryParamsUser.pageNum = 1;
      this.queryParamsUser.pageSize = 10;

      this.getUserList();
    },

    /** 搜索按钮操作 */
    handleQueryUnUser() {
      this.queryParamsUnuser.pageNum = 1;
      this.queryParamsUnuser.pageSize = 10;

      this.getListUnuser();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.deptName = "";
      this.$refs.tableFile.clearSelection();
      this.queryParams.ids = "";
      this.handleQuery();
    },

    /** 重置按钮操作 */
    resetQueryUser() {
      this.queryParamsUser.userName = "";
      this.handleQueryUser();
    },
    /*刷新*/
    resetQueryUnUser() {
      this.queryParamsUnuser.userName = "";
      this.handleQueryUnUser();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.deptId);
      this.single = selection.length != 1;
    },

    handleUnSelectionChange(selection) {
      this.unUserIds = selection.map((item) => item.userId);
      this.multiple = !selection.length;
    },

    // 表单重置
    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0",
      };
      this.resetForm("form");
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加班组";
      treeSelectYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const deptId = row.deptId || this.ids;
      treeSelectYG1().then((response) => {
        this.deptOptions = response.data;
      });
      getTeams(deptId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改班组";
      });
    },

    /** 包含用户操作 */
    handleAuthUser(row) {
      this.queryParamsUser.deptId = row.deptId;
      this.teamsUserOpen = true;
      this.queryParamsUser.userName = "";
      this.getUserList();
    },

    /** 添加用户操作 */
    openSelectTeamsUser() {
      this.teamsUserSelect = true;
      this.queryParamsUnuser.userName = "";
      this.getListUnuser();
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.deptId != undefined) {
            updateTeams(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addTeams(this.form).then((response) => {
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
      const deptIds = row.deptId || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delTeams(deptIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
          this.$refs.tableFile.clearSelection();
        })
        .catch(() => {
          this.$refs.tableFile.clearSelection();
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的班组管理数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的班组管理数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportTeams(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {});
    },

    /** 批量取消授权按钮操作 */
    cancelAuthUserAll() {
      const deptId = this.queryParamsUser.deptId;
      const userIds = this.userIds.join(",");
      this.$modal
        .confirm("是否取消班组中选中用户吗？")
        .then(function () {
          return deleteTeamsUserCancelAll({ deptId: deptId, userIds: userIds });
        })
        .then(() => {
          this.getUserList();
          this.$modal.msgSuccess("取消成功");
        })
        .catch(() => {});
    },

    /** 取消授权按钮操作 */
    cancelAuthUser(row) {
      const deptId = this.queryParams.deptId;
      this.$modal
        .confirm('确认要取消班组中"' + row.userName + '"用户吗？')
        .then(function () {
          return deleteTeamsUserCancel({ userId: row.userId, deptId: deptId });
        })
        .then(() => {
          this.getUserList();
          this.$modal.msgSuccess("取消成功");
        })
        .catch(() => {});
    },
    // 多选框选中数据
    handleUserSelectionChange(selection) {
      this.userIds = selection.map((item) => item.userId);
      this.multiple = !selection.length;
    },
    /** 选择授权用户操作 */
    handleSelectUser() {
      if (this.unUserIds.length == 0) {
        return this.$modal.msgWarning("请选择用户");
      }
      const deptId = this.queryParamsUser.deptId;
      const userIds = this.unUserIds.join(",");
      teamsUserSelectAll({ deptId: deptId, userIds: userIds }).then((res) => {
        this.$modal.msgSuccess(res.msg);
        if (res.code === 200) {
          this.teamsUserSelect = false;
          this.multiple = true;
          this.getUserList();
          this.$emit("ok");
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.operationDiglog {
  .el-dialog__body {
    padding: 0 15px !important;
    .el-col {
      padding: 0 !important;
    }
    ::v-deep .el-table th.gutter{
        display: table-cell !important;
    }
  }
}
</style>


