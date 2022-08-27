<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="设备id" prop="eqId">
        <el-input
          v-model="queryParams.eqId"
          placeholder="请输入设备id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="设备类型" prop="eqType">
        <el-select v-model="queryParams.eqType" placeholder="请选择设备类型" clearable size="small">
          <el-option
            v-for="item in typeData"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="隧道名称" prop="eqTunnelId">
        <el-select v-model="queryParams.eqTunnelId" placeholder="请选择隧道名称" clearable size="small">
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采集数据时间" prop="gettime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.gettime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择采集数据时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:message:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:message:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:message:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:message:export']"
        >导出</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" display:"none" />
      <el-table-column label="设备名称" align="center" prop="sdDevice.eqName" />
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
      <el-table-column label="隧道名称" align="center" prop="sdTunnel.tunnelName" display:"none" />
      <el-table-column label="现场数据值" align="center" prop="sensorValue" />
      <el-table-column label="采集数据时间" align="center" prop="gettime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gettime, '{y}-{m}-{d} {h}:{m}') }}</span>
        </template>

      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:message:remove']"
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

    <!-- 添加或修改传感器采集数据信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备id" prop="eqId">
          <el-input v-model="form.eqId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="form.eqType" placeholder="请选择设备类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="隧道id">
          <el-select v-model="form.eqTunnelId" placeholder="请选择隧道id">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="现场数据值" prop="sensorValue">
          <el-input v-model="form.sensorValue" placeholder="请输入现场数据值" />
        </el-form-item>
        <el-form-item label="采集数据时间" prop="gettime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.gettime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择采集数据时间">
          </el-date-picker>
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
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/equipment/sensorMessage/api.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";

export default {
  name: "Message",
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
      // 传感器采集数据信息表格数据
      messageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqId: null,
        eqType: null,
        eqTunnelId: null,
        sensorValue: null,
        gettime: null,
      },
      //隧道对象
      tunnelData:{},
      typeData:{},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getEqtype();
  },
  methods: {
    /** 查询传感器采集数据信息列表 */
    getList() {
      this.loading = true;
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询隧道信息 */
    getTunnel() {
      /* .then((data)=>{ })里的data是指接口成功返回的数据,包含请求头,请求体,等信息; */
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
      },
      getEqtype(){
        listType().then(response =>{
          this.typeData =response.rows;
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
        eqId: null,
        eqType: null,
        eqTunnelId: null,
        sensorValue: null,
        gettime: null,
        createTime: null,
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
      this.title = "添加传感器采集数据信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMessage(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改传感器采集数据信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMessage(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addMessage(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
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
          return delMessage(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/message/export', {
        ...this.queryParams
      }, `system_message.xlsx`)
    }
  }
};
</script>
