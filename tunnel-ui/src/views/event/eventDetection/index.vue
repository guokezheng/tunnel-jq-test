<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="摄像机ID" prop="cameraId">
        <el-input
          v-model="queryParams.cameraId"
          placeholder="请输入摄像机ID"
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

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:detection:add']"
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
          v-hasPermi="['system:detection:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:detection:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:detection:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="摄像机名称" align="center" prop="camName" />
      <el-table-column label="方向" align="center" prop="direction" :formatter="eventDerectionFormat" />
      <el-table-column label="车道号" align="center" prop="road" />
      <el-table-column label="数据类型" align="center" prop="datatype" :formatter="eventDataTypeFormat"/>
      <el-table-column label="事件类型" align="center" prop="eventtype" :formatter="eventTypeFormat"/>
      <el-table-column label="事件录像FTP地址" align="center" prop="eventVideoFtpAddress" />
      <el-table-column label="事件抓图FTP地址" align="center" prop="eventPicFtpAddress" >
        <template slot-scope="scope">
          <!-- <el-image :src="scope.row.eventPicFtpAddress" style="width: 100px;height: 100px;"/> -->
          <el-popover placement="top-end" title="" trigger="click">
            <img :src="scope.row.eventPicFtpAddress" style="width: 600px;height: 600px">
            <img slot="reference" :src="scope.row.eventPicFtpAddress" style="width: 100px;height: 100px;cursor: pointer;">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:detection:edit']"
          >修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:detection:remove']"
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

    <!-- 添加或修改事件监测记录对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="摄像机ID" prop="cameraId">
          <el-input v-model="form.cameraId" placeholder="请输入摄像机ID" />
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-input v-model="form.direction" placeholder="请输入方向" />
        </el-form-item>
        <el-form-item label="车道号" prop="road">
          <el-input v-model="form.road" placeholder="请输入车道号" />
        </el-form-item>
        <el-form-item label="数据类型" prop="datatype">
          <el-select v-model="form.datatype" placeholder="请选择数据类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventtype">
          <el-select v-model="form.eventtype" placeholder="请选择事件类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件录像FTP地址" prop="eventVideoFtpAddress">
          <el-input v-model="form.eventVideoFtpAddress" placeholder="请输入事件录像FTP地址" />
        </el-form-item>
        <el-form-item label="事件抓图FTP地址" prop="eventPicFtpAddress">
          <el-input v-model="form.eventPicFtpAddress" placeholder="请输入事件抓图FTP地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { listDetection, getDetection, delDetection, addDetection, updateDetection, exportDetection } from "@/api/datainfo/detection";

export default {
  name: "Detection",
  components: {
  },
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
      // 事件监测记录表格数据
      detectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //方向字典参数
      eventDirectionOptions: [],
      eventDataTypeOptions: [],
      eventTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelName: null,
        cameraId: null,
        direction: null,
        road: null,
        datatype: null,
        eventtype: null,
        eventVideoFtpAddress: null,
        eventPicFtpAddress: null,
        camName: null,
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
    this.getDicts("sys_event_direction").then(response => {
      this.eventDirectionOptions = response.data;
    });
    this.getDicts("sys_event_dataType").then(response => {
      this.eventDataTypeOptions = response.data;
    });
    this.getDicts("sys_event_type").then(response => {
      this.eventTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询事件监测记录列表 */
    getList() {
      this.loading = true;
      listDetection(this.queryParams).then(response => {
        this.detectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    eventDerectionFormat(row, column) {
      return this.selectDictLabel(this.eventDirectionOptions, row.direction);
    },

    eventDataTypeFormat(row, column) {
      return this.selectDictLabel(this.eventDataTypeOptions, row.datatype);
    },

    eventTypeFormat(row, column) {
      return this.selectDictLabel(this.eventTypeOptions, row.eventtype);
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
        cameraId: null,
        direction: null,
        road: null,
        datatype: null,
        eventtype: null,
        eventVideoFtpAddress: null,
        eventPicFtpAddress: null
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
      this.title = "添加事件监测记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetection(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改事件监测记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDetection(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetection(this.form).then(response => {
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
      this.$confirm('是否确认删除选中数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDetection(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出事件监测记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDetection(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
