<template>
  <div class="app-container" style="height: 0px !important">
    <!-- 授权用户 -->
    <el-dialog
      title="选择用户"
      class="workbench-dialog batch-table operationDiglog"
      :visible.sync="visible"
      width="1000px"
      append-to-body
      v-dialogDrag
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-row class="topFormRow">
        <el-col :span="10" :offset="14">
          <div class="grid-content bg-purple" ref="main">
            <el-input
              placeholder="请输入用户昵称、手机号码，回车搜索"
              v-model="queryParams.userName"
              @keyup.enter.native="handleQuery"
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
          @selection-change="handleSelectionChange"
          max-height="500px"
          class="allTable"
        >
          <el-table-column type="selection" align="center" />
          <el-table-column
            type="index"
            :index="indexMethod"
            label="序号"
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
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="220"
            sortable
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleSelectUser" class="submitButton">确 定</el-button>
        <el-button @click="visible = false" class="closeButton">取 消</el-button>
      </div>

      <!--    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="userList" @selection-change="handleSelectionChange"
          height="300" class="allTable">
        <el-table-column type="selection" width="55"></el-table-column>
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
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSelectUser">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>-->
    </el-dialog>
  </div>
</template>

<script>
import { unallocatedUserList, authUserSelectAll } from "@/api/system/role";
export default {
  dicts: ["sys_normal_disable"],
  props: {
    // 角色编号
    roleId: {
      type: [Number, String],
    },
  },
  data() {
    return {
      // 遮罩层
      visible: false,
      // 选中数组值
      userIds: [],
      // 总条数
      total: 0,
      // 未授权用户数据
      userList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleId: undefined,
        userName: undefined,
        phonenumber: undefined,
      },
      sysNormalDisableList:[],//状态
    };
  },
  created() {
    //状态
    this.getDicts("sys_normal_disable").then((response) => {
      this.sysNormalDisableList = response.data;
    });
  },
  methods: {
    getStatus(row) {
      return this.selectDictLabel(this.sysNormalDisableList, row);
    },
    // 显示弹框
    show() {
      this.queryParams.roleId = this.roleId;
      this.getList();
      this.visible = true;
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.userIds = selection.map((item) => item.userId);
    },
    // 查询表数据
    getList() {
      unallocatedUserList(this.queryParams).then((res) => {
        this.userList = res.rows;
        this.userList = this.userList.filter(
          (item) => item.userName != "admin"
        );
        this.total = res.total;
      });
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
    /** 选择授权用户操作 */
    handleSelectUser() {
      if (this.userIds.length == 0) {
        return this.$modal.msgWarning("请选择用户");
      }
      const roleId = this.queryParams.roleId;
      const userIds = this.userIds.join(",");
      authUserSelectAll({ roleId: roleId, userIds: userIds }).then((res) => {
        this.$modal.msgSuccess(res.msg);
        if (res.code === 200) {
          this.visible = false;
          this.$emit("ok");
        }
      });
    },
  },
};
</script>
