<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:job:remove']"
          >删除</el-button
        >
        <el-button
          size="small"
          @click="handleClean"
          v-hasPermi="['monitor:job:remove']"
          >清空</el-button
        >
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['monitor:job:export']"
          >导出</el-button
        >
        <el-button size="small" @click="handleClose">关闭</el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入任务名称，回车搜索"
            v-model="queryParams.jobName"
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="boxShow = !boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="boxShow" ref="cc">
      <el-form :model="queryParams" ref="queryForm" label-width="68px">
        <el-form-item label="任务分组" prop="jobGroup">
          <el-select
            v-model="queryParams.jobGroup"
            placeholder="请任务组名"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.sys_job_group"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择执行状态"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.sys_common_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 100%"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" @click="handleQuery">搜索</el-button>
          <el-button size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务组名" prop="jobGroup">
        <el-select
          v-model="queryParams.jobGroup"
          placeholder="请任务组名"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_job_group"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择执行状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  size="mini" @click="handleQuery">搜索</el-button>
        <el-button  size="mini" @click="resetQuery" type="primary" plain>重置</el-button>

      </el-form-item>
    </el-form> -->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:job:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['monitor:job:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['monitor:job:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-close"
          size="mini"
          @click="handleClose"
        >关闭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="jobLogList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      height="62vh"
      ref="tableFile"
      :row-key="getRowKey"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="日志编号"
        width="80"
        align="center"
        prop="jobLogId"
      />
      <el-table-column
        label="任务名称"
        align="center"
        prop="jobName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="任务分组"
        align="center"
        prop="jobGroup"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_job_group"
            :value="scope.row.jobGroup"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="调用目标字符串"
        align="center"
        prop="invokeTarget"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="日志信息"
        align="center"
        prop="jobMessage"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="执行状态" align="center" prop="status">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/> -->
          <span
            :style="{
              color: scope.row.status == '0' ? '#00FF00' : 'red',
            }"
            >{{ pollFormat(scope.row.status) }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="执行时间"
        align="center"
        prop="createTime"
        width="180"
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
            class="tableBlueButtton"
            @click="handleView(scope.row)"
            v-hasPermi="['monitor:job:query']"
            >详细</el-button
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

    <!-- 调度日志详细 -->
    <el-dialog
      title="调度日志详细"
      :visible.sync="open"
      width="700px"
      append-to-body
      :before-close="cancel"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ form.jobLogId }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{
              form.createTime
            }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用目标字符串：">{{
              form.invokeTarget
            }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{
              form.jobMessage
            }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="form.status == 0">成功</div>
              <div v-else-if="form.status == 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status == 1">{{
              form.exceptionInfo
            }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="closeButton" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getJob } from "@/api/monitor/job";
import {
  listJobLog,
  delJobLog,
  exportJobLog,
  cleanJobLog,
} from "@/api/monitor/jobLog";

export default {
  name: "JobLog",
  dicts: ["sys_common_status", "sys_job_group"],
  data() {
    return {
      boxShow: false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调度日志表格数据
      jobLogList: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined,
      },
      loginStatusOptions: [], //状态
    };
  },
  created() {
    this.getDicts("sys_common_status").then((response) => {
      this.loginStatusOptions = response.data;
    });
    const jobId = this.$route.query.jobId;
    if (jobId !== undefined && jobId != 0) {
      getJob(jobId).then((response) => {
        this.queryParams.jobName = response.data.jobName;
        this.queryParams.jobGroup = response.data.jobGroup;
        this.getList();
      });
    } else {
      this.getList();
    }
  },
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
    },
    handleRowClick(row){
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.jobId
    },
    pollFormat(row) {
      return this.selectDictLabel(this.loginStatusOptions, row);
    },
    bodyCloseMenus(e) {
      let self = this;
      if (
        !this.$refs.main.contains(e.target) &&
        !this.$refs.cc.contains(e.target)
      ) {
        if (self.boxShow == true) {
          self.boxShow = false;
        }
      }
    },
    /** 查询调度日志列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listJobLog(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          console.log(response.rows, "调度日志列表");
          this.jobLogList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 返回按钮
    handleClose() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/config/job" });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.jobLogId);
      this.multiple = !selection.length;
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this
      const jobLogIds = this.ids;
      this.$modal
        .confirm("是否确认删除选中数据项？")
        .then(function () {
          return delJobLog(jobLogIds);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          that.$refs.tableFile.clearSelection();
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有调度日志数据项？")
        .then(function () {
          return cleanJobLog();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出调度日志数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportJobLog(queryParams);
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
