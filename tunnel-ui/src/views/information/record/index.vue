<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="发布设备" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入发布设备"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布时间" prop="releaseTime">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.releaseTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择发布时间"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="发布状态" prop="releaseStatus">
        <el-select
          v-model="queryParams.releaseStatus"
          placeholder="请选择发布状态"
          clearable
          size="small"
        >
          <el-option label="成功" value="0" />
          <el-option label="失败" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="发布机构" prop="releaseDeptName">
        <el-input
          v-model="queryParams.releaseDeptName"
          placeholder="请输入发布机构"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布用户" prop="releaseUserName">
        <el-input
          v-model="queryParams.releaseUserName"
          placeholder="请输入发布用户"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery"  type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleExport"
          >导出</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="recordList"
      @selection-change="handleSelectionChange"
      :default-sort = "{prop: 'releaseTime', order: 'descending'}"
    :row-class-name="tableRowClassName"
    max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="发布用户" align="center" prop="id" />-->
      <el-table-column label="发布设备" align="center" prop="deviceId" />
      <el-table-column
        label="发布时间"
        align="center"
        prop="releaseTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.releaseTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="releaseStatus" />
      <el-table-column label="发布内容" align="center" prop="releaseNewContent" />
      <el-table-column label="发布机构" align="center" prop="releaseDeptName" />
      <el-table-column label="发布用户" align="center" prop="releaseUserName" />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改发布记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件发布设备关联表ID" prop="vmsEventId">
          <el-input
            v-model="form.vmsEventId"
            placeholder="请输入事件发布设备关联表ID"
          />
        </el-form-item>
        <el-form-item label="发布设备" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入发布设备" />
        </el-form-item>
        <el-form-item label="发布时间" prop="releaseTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.releaseTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择发布时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发布状态" prop="releaseStatus">
          <el-select v-model="form.releaseStatus" placeholder="请选择发布状态">
            <el-option label="正常" value="0" />
            <el-option label="失败" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布机构" prop="releaseDeptName">
          <el-input
            v-model="form.releaseDeptName"
            placeholder="请输入发布机构"
          />
        </el-form-item>
        <el-form-item label="发布机构" prop="releaseDeptId">
          <el-input v-model="form.releaseDeptId" placeholder="请输入发布机构" />
        </el-form-item>
        <el-form-item label="发布用户" prop="releaseUserName">
          <el-input
            v-model="form.releaseUserName"
            placeholder="请输入发布用户"
          />
        </el-form-item>
        <el-form-item label="发布用户" prop="releaseUserId">
          <el-input v-model="form.releaseUserId" placeholder="请输入发布用户" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRecord,
  exportRecord,
} from "@/api/board/record";

export default {
  name: "Record",
  components: {},
  data() {
    return {
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
      // 发布记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        releaseTime: null,
        releaseStatus: null,
        releaseDeptName: null,
        releaseUserName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询发布记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then((response) => {
        this.recordList = response.rows;
        console.log(this.recordList);
        for (var item of this.recordList) {
          if (item.releaseStatus == "0") {
            item.releaseStatus = "成功";
          } else {
            item.releaseStatus = "失败";
          }
          item.releaseNewContent = item.releaseNewContent.substring(item.releaseNewContent.indexOf("\\f")+7).replaceAll("\\n","");
        }
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
        deviceId: null,
        releaseTime: null,
        releaseStatus: null,
        releaseDeptName: null,
        releaseDeptId: null,
        releaseUserName: null,
        releaseUserId: null,
        releaseNewContent: null,
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
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
    },
    /** 提交按钮 */
    submitForm() {
    },
    /** 删除按钮操作 */
    handleDelete(row) {
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有发布记录数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportRecord(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
    // 表格行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
  },
};
</script>
