<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button size="small" @click="openSelectUser">添加用户 </el-button>
        <el-button size="small" :disabled="multiple" @click="cancelAuthUserAll"
          >批量取消</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
        <el-button size="small" @click="handleClose">关闭</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入用户昵称、手机号码"
            v-model="queryParams.userName"
            @keyup.enter.native="handleQuery"
            class="formNoIcon"
          >
            <!--            <el-button
                          slot="append"
                          class="searchTable"

                        ></el-button>-->
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!--     <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  size="mini" @click="handleQuery">搜索</el-button>
        <el-button  size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="openSelectUser"
          v-hasPermi="['system:role:add']"
        >添加用户</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="cancelAuthUserAll"
          v-hasPermi="['system:role:remove']"
        >批量取消授权</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleClose"
        >关闭</el-button>
      </el-form-item>
    </el-form>-->

    <el-table
      v-loading="loading"
      :data="userList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      height="62vh"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column type="selection" width="55" align="center" />
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
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="用户昵称"
        prop="nickName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="邮箱"
        prop="email"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="手机"
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
            >{{ pollFormat(scope.row.status) }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        sortable
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
            class="tableDelButtton"
            @click="cancelAuthUser(scope.row)"
            v-hasPermi="['system:role:remove']"
            >取消授权</el-button
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
    <select-user ref="select" :roleId="queryParams.roleId" @ok="handleQuery" />
  </div>
</template>

<script>
import {
  allocatedUserList,
  authUserCancel,
  authUserCancelAll,
} from "@/api/system/role";
import selectUser from "./selectUser";

export default {
  name: "AuthUser",
  dicts: ["sys_normal_disable"],
  components: { selectUser },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中用户组
      userIds: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleId: undefined,
        userName: undefined,
        phonenumber: undefined,
      },
      sysNormalDisableList: [], //状态
    };
  },
  created() {
    const roleId = this.$route.params && this.$route.params.roleId;
    if (roleId) {
      this.queryParams.roleId = roleId;
      this.getList();
    }
    //状态
    this.getDicts("sys_normal_disable").then((response) => {
      this.sysNormalDisableList = response.data;
    });
  },
  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.userId;
    },
    handleRowClick(row){
      this.$refs.tableFile.toggleRowSelection(row);
    },
    pollFormat(row) {
      return this.selectDictLabel(this.sysNormalDisableList, row);
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    /** 查询授权用户列表 */
    getList() {
      this.loading = true;
      allocatedUserList(this.queryParams).then((response) => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 返回按钮
    handleClose() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/system/role" });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.userName = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.userIds = selection.map((item) => item.userId);
      this.multiple = !selection.length;
    },
    /** 打开授权用户表弹窗 */
    openSelectUser() {
      this.$refs.select.show();
    },
    /** 取消授权按钮操作 */
    cancelAuthUser(row) {
      const roleId = this.queryParams.roleId;
      this.$modal
        .confirm('确认要取消该用户"' + row.userName + '"角色吗？')
        .then(function () {
          return authUserCancel({ userId: row.userId, roleId: roleId });
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("取消授权成功");
        })
        .catch(() => {
          this.$refs.tableFile.clearSelection();
        });
    },
    /** 批量取消授权按钮操作 */
    cancelAuthUserAll(row) {
      const roleId = this.queryParams.roleId;
      const userIds = this.userIds.join(",");
      this.$modal
        .confirm("是否取消选中用户授权数据项？")
        .then(function () {
          return authUserCancelAll({ roleId: roleId, userIds: userIds });
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("取消授权成功");
        })
        .catch(() => {});
    },
  },
};
</script>
