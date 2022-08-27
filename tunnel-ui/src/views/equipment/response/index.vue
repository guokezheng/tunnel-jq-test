<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="应急处置名称" prop="responseName">
        <el-input
          v-model="queryParams.responseName"
          placeholder="请输入应急处置名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="所属隧道" prop="responseTunnel">
        <el-select v-model="queryParams.responseTunnel" placeholder="请选择所属隧道" clearable size="small">
          <!-- <el-option label="请选择字典生成" value="" /> -->
          <el-option v-for="item in resTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" prop="responseType">
        <el-select v-model="queryParams.responseType" placeholder="请选择设备类型" clearable size="small">
          <!-- <el-option label="请选择字典生成" value="" /> -->
          <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="responseEqName">
        <el-input v-model="queryParams.responseEqName" placeholder="请输入设备名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:response:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:response:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:response:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:response:export']">导出</el-button>
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

    <el-table v-loading="loading" :data="responseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="应急处置id" align="center" prop="responseId" /> -->
      <el-table-column label="应急处置名称" align="center" prop="responseName" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName.tunnelName" />
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
      <el-table-column label="设备名称" align="center" prop="responseEqName" />
      <el-table-column label="设备状态" align="center" prop="responseEqState" :formatter="responseFormat" />
      <el-table-column label="备注" align="center" prop="responseRemark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:response:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:response:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改应急处置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="应急处置名称" prop="responseName">
          <el-input v-model="form.responseName" placeholder="请输入应急处置名称" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="responseTunnel">
          <el-select v-model="form.responseTunnel" placeholder="请选择所属隧道">
            <!-- <el-option label="请选择字典生成" value="" /> -->
            <el-option v-for="item in resTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="responseType">
          <el-select v-model="form.responseType" placeholder="请选择设备类型">
            <!-- <el-option label="请选择字典生成" value="" /> -->
            <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备名称" prop="responseEqName">
          <el-input v-model="form.responseEqName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备状态" prop="responseEqState">
          <el-select v-model="form.responseEqState" placeholder="请选择设备状态">
            <el-option v-for="dict in responseEqStateData" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="responseRemark">
          <el-input v-model="form.responseRemark" placeholder="请输入备注" />
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
  import {
    listResponse,
    getResponse,
    delResponse,
    addResponse,
    updateResponse
  } from "@/api/equipment/response/api.js";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    listType
  } from "@/api/equipment/type/api";

  export default {
    name: "Response",
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
        // 应急处置表格数据
        responseList: [],
        resTunnelData: {},
        eqTypeData: {},
        responseEqStateData: {},
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          responseName: null,
          responseTunnel: null,
          responseType: null,
          responseEqName: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          responseName: [{
            required: true,
            message: '请输入应急处置名称',
            trigger: 'blur'
          }],
          responseTunnel: [{
            required: true,
            message: '请选择所属隧道',
            trigger: 'change'
          }],
          responseType: [{
            required: true,
            message: '请选择设备类型',
            trigger: 'change'
          }],
          responseEqName: [{
            required: true,
            message: '请输入设备名称',
            trigger: 'blur'
          }],
          responseEqState: [{
            required: true,
            message: '请输入设备状态',
            trigger: 'change'
          }],
          responseRemark: [{
            required: true,
            message: '请输入备注',
            trigger: 'blur'
          }],
        }
      };
    },
    created() {
      this.getList();
      this.getTunnel();
      this.getEqType();
      this.getDicts("sd_emergency_response_equipmentType").then(response => {
        this.responseEqStateData = response.data; //获取设备状态字典信息
      });
    },
    methods: {
      /** 查询应急处置列表 */
      getList() {
        this.loading = true;
        listResponse(this.queryParams).then(response => {
          this.responseList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      responseFormat(row, column) {
        return this.selectDictLabel(this.responseEqStateData, row.responseEqState)
      },
      getTunnel() {
        listTunnels().then(response => {
          this.resTunnelData = response.rows;
        });
      },
      getEqType() {
        listType().then(response => {
          this.eqTypeData = response.rows;
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
          responseId: null,
          responseName: null,
          responseTunnel: null,
          responseType: null,
          responseEqName: null,
          responseEqState: null,
          responseRemark: null
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
        this.ids = selection.map(item => item.responseId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加应急处置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const responseId = row.responseId || this.ids
        getResponse(responseId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改应急处置";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.responseId != null) {
              updateResponse(this.form).then(response => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addResponse(this.form).then(response => {
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
        const responseIds = row.responseId || this.ids;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delResponse(responseIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/response/export', {
          ...this.queryParams
        }, `system_response.xlsx`)
      }
    }
  };
</script>
