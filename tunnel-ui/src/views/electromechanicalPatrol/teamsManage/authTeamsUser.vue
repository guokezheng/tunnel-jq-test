<template>
  <div class="app-container" >

    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="openSelectTeamsUser"
        >添加用户
        </el-button>
        <el-button
          size="small"
          :disabled="multiple"
          @click="cancelAuthUserAll"
        >批量取消</el-button>
        <el-button size="small" @click="resetQuery"
        >刷新</el-button
        >
        <el-button
          size="small"
          @click="handleClose"
        >关闭</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入用户昵称、手机号码"
            v-model="queryParams.userName"
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
              icon="icon-gym-Gsearch"

            ></el-button>-->
          </el-input>
        </div>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange" class="allTable" height="62vh">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
      <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
      <el-table-column label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="cancelAuthUser(scope.row)"
          >取消</el-button>
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
    <select-user ref="select" :deptId="queryParams.deptId" @ok="handleQuery" />
  </div>
</template>

<script>
import {
  deleteTeamsUserCancel,
  deleteTeamsUserCancelAll,
  teamsUserList
} from "@/api/electromechanicalPatrol/teamsManage/teams";
import selectUser from "./selectUser";

export default {
  name: "AuthUser",
  dicts: ['sys_normal_disable'],
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
        deptId: undefined,
        userName: undefined,
        phonenumber: undefined
      }
    };
  },
  created() {
    const deptId = this.$route.params&& this.$route.params.deptId;;
    if (deptId) {
      this.queryParams.deptId = deptId;
      this.getList();
    }
  },
  methods: {
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },

    /** 查询授权用户列表 */
    getList() {
      this.loading = true;
      teamsUserList(this.queryParams).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 返回按钮
    handleClose() {
      this.$router.push({ path: "/empatrol/teams" });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.userName ="";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId)
      this.multiple = !selection.length
    },
    /** 打开添加用户表弹窗 */
    openSelectTeamsUser() {
      this.$refs.select.show();
    },
    /** 取消授权按钮操作 */
    cancelAuthUser(row) {
      const deptId = this.queryParams.deptId;
      this.$modal.confirm('确认要取消班组中"' + row.userName + '"用户吗？').then(function() {
        return deleteTeamsUserCancel({ userId: row.userId, deptId: deptId });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      }).catch(() => {});
    },
    /** 批量取消授权按钮操作 */
    cancelAuthUserAll(row) {
      const deptId = this.queryParams.deptId;
      const userIds = this.userIds.join(",");
      this.$modal.confirm('是否取消班组中选中用户吗？').then(function() {
        return deleteTeamsUserCancelAll({ deptId: deptId, userIds: userIds });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      }).catch(() => {});
    }
  }
};
</script>
