<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属单位" prop="zzjgId">
        <el-input
          v-model="queryParams.zzjgId"
          placeholder="请输入所属单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划完成时间" prop="endPlantime">
        <el-date-picker clearable size="small"
          v-model="queryParams.endPlantime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择计划完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="派单人员" prop="dispatcher">
        <el-input
          v-model="queryParams.dispatcher"
          placeholder="请输入派单人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="派单时间" prop="dispatchTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.dispatchTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择派单时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="指派巡查班组" prop="bzId">
        <el-input
          v-model="queryParams.bzId"
          placeholder="请输入指派巡查班组id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务描述" prop="taskDescription">
        <el-input
          v-model="queryParams.taskDescription"
          placeholder="请输入任务描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布状态" prop="publishStatus">
        <el-select v-model="queryParams.publishStatus" placeholder="请选择发布状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择任务状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡查人员" prop="walkerId">
        <el-input
          v-model="queryParams.walkerId"
          placeholder="请输入巡查人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务完成时间" prop="taskEndtime">
        <el-date-picker clearable size="small"
          v-model="queryParams.taskEndtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择任务完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="任务持续时间" prop="taskCxtime">
        <el-date-picker clearable size="small"
          v-model="queryParams.taskCxtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择任务持续时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="现场情况描述" prop="siteDescription">
        <el-input
          v-model="queryParams.siteDescription"
          placeholder="请输入现场情况描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" plain size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
        >新增</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:list:edit']"
        >修改</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:list:remove']"
        >删除</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:list:export']"
        >导出</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:list:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:list:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:list:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="listList" @selection-change="handleSelectionChange" class="allTable">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务编号" align="center" prop="id" />
      <el-table-column label="所属单位" align="center" prop="zzjgId" />
      <el-table-column label="计划完成时间" align="center" prop="endPlantime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endPlantime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="派单人员" align="center" prop="dispatcher" />
      <el-table-column label="派单时间" align="center" prop="dispatchTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dispatchTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="指派巡查班组id" align="center" prop="bzId" />
      <el-table-column label="任务描述" align="center" prop="taskDescription" />
      <el-table-column label="发布状态" align="center" prop="publishStatus" />
      <el-table-column label="任务状态" align="center" prop="taskStatus" />
      <el-table-column label="巡查人员id" align="center" prop="walkerId" />
      <el-table-column label="任务完成时间" align="center" prop="taskEndtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskEndtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务持续时间" align="center" prop="taskCxtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskCxtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现场情况描述" align="center" prop="siteDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:list:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
          >删除</el-button>
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

    <!-- 添加或修改巡查任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属单位" prop="zzjgId">
          <el-input v-model="form.zzjgId" placeholder="请输入所属单位" />
        </el-form-item>
        <el-form-item label="计划完成时间" prop="endPlantime">
          <el-date-picker clearable size="small"
            v-model="form.endPlantime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择计划完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="派单人员" prop="dispatcher">
          <el-input v-model="form.dispatcher" placeholder="请输入派单人员" />
        </el-form-item>
        <el-form-item label="派单时间" prop="dispatchTime">
          <el-date-picker clearable size="small"
            v-model="form.dispatchTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择派单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指派巡查班组id" prop="bzId">
          <el-input v-model="form.bzId" placeholder="请输入指派巡查班组id" />
        </el-form-item>
        <el-form-item label="任务描述" prop="taskDescription">
          <el-input v-model="form.taskDescription" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-radio-group v-model="form.publishStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-radio-group v-model="form.taskStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="巡查人员id" prop="walkerId">
          <el-input v-model="form.walkerId" placeholder="请输入巡查人员id" />
        </el-form-item>
        <el-form-item label="任务完成时间" prop="taskEndtime">
          <el-date-picker clearable size="small"
            v-model="form.taskEndtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择任务完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="任务持续时间" prop="taskCxtime">
          <el-date-picker clearable size="small"
            v-model="form.taskCxtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择任务持续时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="现场情况描述" prop="siteDescription">
          <el-input v-model="form.siteDescription" placeholder="请输入现场情况描述" />
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
import { listList, getList, delList, addList, updateList, exportList } from "@/api/electromechanicalPatrol/taskManage/task";

export default {
  name: "List",
  data() {
    return {
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
      // 巡查任务表格数据
      listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        zzjgId: null,
        endPlantime: null,
        dispatcher: null,
        dispatchTime: null,
        bzId: null,
        taskDescription: null,
        publishStatus: null,
        taskStatus: null,
        walkerId: null,
        taskEndtime: null,
        taskCxtime: null,
        siteDescription: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询巡查任务列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
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
        zzjgId: null,
        endPlantime: null,
        dispatcher: null,
        dispatchTime: null,
        bzId: null,
        taskDescription: null,
        publishStatus: 0,
        taskStatus: 0,
        walkerId: null,
        taskEndtime: null,
        taskCxtime: null,
        siteDescription: null,
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加巡查任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡查任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除巡查任务编号为"' + ids + '"的数据项？').then(function() {
        return delList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有巡查任务数据项？').then(() => {
        this.exportLoading = true;
        return exportList(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
