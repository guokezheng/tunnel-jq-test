<template>
  <div class="app-container" style = "height:0px!important;">
    <!-- 授权用户 -->
    <el-dialog title="选择用户" :visible.sync="visible" width="1000px" append-to-body class="operationDiglog">
        <el-row
          :gutter="20"
          style="margin: 0px 0 6px"
        >
          <el-col :span="10" :offset="14">
            <div class="grid-content bg-purple" ref="main">
              <el-input
                placeholder="请输入用户昵称、手机号码，回车搜索"
                v-model="queryParams.userName"
                @keyup.enter.native="handleQuery"
                size="small"
                clearable
                style="border-right:#00C8FF solid 1px !important;border-radius:3px"
              >

<!--                <el-button
                  slot="append"
                  size="small"
                  icon="icon-gym-Gsearch"
                  style="transform: translateX(20px)"
                ></el-button>-->
              </el-input>
            </div>
          </el-col>
        </el-row>

      <el-row>
<!--        <el-table @row-click="clickRow" ref="table" :data="userList" @selection-change="handleSelectionChange"
            height="260px"  :row-class-name="tableRowClassName">-->
          <el-table
          ref="tables"
          :data="userList"
          @selection-change="handleSelectionChange"
          :row-class-name="tableRowClassName"
          style="color: #ffffff;height:62vh">
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
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {teamsUserSelectAll, unTeamsUserList} from "@/api/electromechanicalPatrol/teamsManage/teams";

export default {
  dicts: ['sys_normal_disable'],
  props: {
    // 角色编号
    deptId: {
      type: [Number, String]
    }
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
        deptId: undefined,
        userName: undefined,
        phonenumber: undefined
      }
    };
  },
  methods: {
    // 显示弹框
    show() {
      this.queryParams.deptId = this.deptId;
      this.getList();
      this.visible = true;
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId);
    },
    // 查询表数据
    getList() {
      unTeamsUserList(this.queryParams).then(res => {
        this.userList = res.rows;
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
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    /** 选择授权用户操作 */
    handleSelectUser() {
      const deptId = this.queryParams.deptId;
      const userIds = this.userIds.join(",");
      teamsUserSelectAll({ deptId: deptId, userIds: userIds }).then(res => {
        this.$modal.msgSuccess(res.msg);
        if (res.code === 200) {
          this.visible = false;
          this.$emit("ok");
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>

.operationDiglog {
::v-deep .el-input-group__append {
  padding: 0;
  width: 60px;
  border-left: none !important;
.el-button {
  height: 32px;
  border-top-right-radius: 3px !important;
  border-bottom-right-radius: 3px !important;
  border-top-left-radius: 0px !important;
  border-bottom-left-radius: 0px !important;
// transform: translateX(20px);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
}
.el-dialog .el-form {
  padding: 15px !important;
.el-form-item__content .el-button {
  width: 88px;
  height: 22px;
  border: none;
}
.el-form-item__content .el-button--mini {
  padding: 2px 15px !important;
}
}
.el-table {
  padding: 0 15px;
  margin-bottom: 60px;
}
.el-tabs {
  padding: 0 15px;
}
}
</style>
