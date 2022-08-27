<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" prop="eqTypeId">
        <el-select v-model="queryParams.eqTypeId" placeholder="请选择设备类型" clearable size="small" @change="eqTypeChange">
         <el-option
           v-for="item in eqTypeData"
           :key="item.typeId"
           :label="item.typeName"
           :value="item.typeId"
         />
        </el-select>
      </el-form-item>
      <el-form-item label="设备位置" prop="eqPosition">
        <el-select v-model="queryParams.eqTypeId" :placeholder="(queryParams.eqTypeId == '' || queryParams.eqTypeId == null) ? '请先选择设备类型':'请选择设备位置'" clearable size="small">
         <el-option
           v-for="item in eqTypeData"
           :key="item.typeId"
           :label="item.typeName"
           :value="item.typeId"
         />
        </el-select>
      </el-form-item>
      <el-form-item label="故障时间">
        <el-date-picker
          v-model="queryParams.timeQuantum"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:index:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:index:edit']"
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
          v-hasPermi="['system:index:remove']"
        >删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:index:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="indexList" :default-sort = "{prop: 'faultTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="设备类型" align="center" prop="eqTypeId" />
      <el-table-column label="设备位置" align="center" prop="eqPosition" />
      <el-table-column label="工作时间" align="center" prop="workingTime" sortable />
      <el-table-column label="故障时间" align="center" prop="faultTime" sortable />
      <el-table-column label="工作状态" align="center" prop="workingType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:index:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:index:remove']"
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

    <!-- 添加或修改信息采集对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select v-model="form.tunnelId" placeholder="请选择隧道" clearable >
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="eqTypeId">
          <el-select v-model="queryParams.eqTypeId" placeholder="请选择设备类型" clearable size="small" @change="eqTypeChange">
         <el-option
           v-for="item in eqTypeData"
           :key="item.typeId"
           :label="item.typeName"
           :value="item.typeId"
         />
        </el-select>
        </el-form-item>
        <el-form-item label="设备位置" prop="eqPosition">
          <el-input v-model="form.eqPosition" placeholder="请选择设备位置" />
        </el-form-item>
        <el-form-item label="工作时间" prop="workingTime">
          <el-date-picker
            v-model="form.workingTime"
            type="datetime"
            placeholder="选择工作时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="故障时间" prop="faultTime">
          <el-date-picker
            v-model="form.faultTime"
            type="datetime"
            placeholder="选择故障时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工作状态" prop="workingType">
          <el-select v-model="form.workingType" placeholder="请选择工作状态" clearable >
            <el-option
              v-for="(item,i) in ['正常','故障']"
              :key="i"
              :label="item"
              :value="item"
            />
          </el-select>
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
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";

export default {
  name: "InformationCollection",
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 安全指数表格数据
      indexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        eqTypeId: null,
        eqPosition: null,
        timeQuantum: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tunnelId: { required: true, message: '请选择隧道', trigger: 'change' },
        eqTypeId: { required: true, message: '请选择设备类型', trigger: 'change' },
        eqPosition: { required: true, message: '请选择设备位置', trigger: 'change' },
        workingTime: { required: true, message: '请选择工作时间', trigger: 'change' },
        faultTime: { required: true, message: '请选择故障时间', trigger: 'change' },
        workingType: { required: true, message: '请选择工作状态', trigger: 'change' },
      },
      // 所属隧道
      eqTunnelData: {},
      // 设备类型
      eqTypeData: {},
    };
  },
  created() {
    this.getTunnel()
    this.getEqType()
    this.getList();
  },
  methods: {
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then(response => {
        this.eqTypeData = response.rows;
      });
    },
    /** 查询信息采集列表 */
    getList() {
      this.indexList = [
        {
          tunnelId: 'S29-LinYiCompany-BaiYanStation-001',
          tunnelName: '毓秀山隧道',
          eqTypeId: 'CO/VI检测器',
          eqPosition: 'k101-11',
          workingTime: '2022-1-18 09:30:00',
          faultTime: '2022-1-18 10:30:00',
          workingType: '正常',
        },
      ]
      return
      this.loading = true;
      listIndex(this.queryParams).then(response => {
        this.indexList = response.rows;
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
        tunnelId: null,
        eqTypeId: null,
        eqPosition: null,
        faultTime: [],
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
      this.title = "添加信息采集";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIndex(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改信息采集";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateIndex(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIndex(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() {
        return delIndex(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出信息采集数据列表？').then(() => {
        this.exportLoading = true;
        return exportIndex(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    eqTypeChange(val) {
      if(val) {
        // 获取设备位置 赋值给查询参数
      }
    }
  }
};
</script>