<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属隧道" prop="repairTunnelId">
        <el-select v-model="queryParams.repairTunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in repairRecordTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
        </el-select>
      </el-form-item>
      <el-form-item label="维修时间" prop="repairTime">
        <el-date-picker clearable size="small" style="width: 200px" v-model="queryParams.repairTime" type="date"
          value-format="yyyy-MM-dd" placeholder="选择维修时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="设备类型" prop="repairTypeId">
        <el-select v-model="queryParams.repairTypeId" placeholder="请选择设备类型" clearable size="small">
          <!-- <el-option label="请选择字典生成" value="" /> -->
          <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备编号" prop="repairNumber">
        <el-input v-model="queryParams.repairNumber" placeholder="请输入设备编号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:record:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:record:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:record:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:record:export']">导出</el-button>
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

    <el-table v-loading="loading" :data="recordList" :default-sort = "{prop: 'repairTime', order: 'descending'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="设备维修记录ID" align="center" prop="repairId" /> -->
      <el-table-column label="所属隧道" align="center" prop="tunnelName.tunnelName" />
      <el-table-column label="维修时间" align="center" prop="repairTime" width="180" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.repairTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
      <el-table-column label="设备编号" align="center" prop="repairNumber" />
      <el-table-column label="巡检内容" align="center" prop="repairContentState" />
      <el-table-column label="维修内容" align="center" prop="repairContent" />
      <el-table-column label="巡检结果" align="center" prop="repairResult" :formatter="repairResultFomat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:record:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改设备维修记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属隧道" prop="repairTunnel">
          <el-select v-model="form.repairTunnelId" placeholder="请选择所属隧道" style="width: 100%">
            <el-option v-for="item in repairRecordTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修时间" prop="repairTime">
          <el-date-picker clearable size="small" style="width: 100%" v-model="form.repairTime" type="date"
            value-format="yyyy-MM-dd" placeholder="选择维修时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备类型" prop="repairType">
          <el-select v-model="form.repairTypeId" placeholder="请选择设备类型" style="width: 100%">
            <!-- <el-option label="请选择字典生成" value="" /> -->
            <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备编号" prop="repairNumber">
          <el-input v-model="form.repairNumber" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="巡检内容" prop="repairContentState">
          <el-input v-model="form.repairContentState" placeholder="请输入巡检内容" />
        </el-form-item>
        <el-form-item label="维修内容" prop="repairContent">
          <el-input v-model="form.repairContent" placeholder="请输入维修内容" />
        </el-form-item>
<!--        <el-form-item label="巡检结果" prop="repairResult">-->
<!--          <el-input v-model="form.repairResult" placeholder="请输入巡检结果" />-->
<!--        </el-form-item>-->
        <el-form-item label="巡检结果" prop="repairResult">
          <el-select v-model="form.repairResult" placeholder="请输入巡检结果" size="small" style="width: 100%">
            <el-option v-for="dict in repairResultData"
                       :key="dict.dictValue"
                       :label="dict.dictLabel"
                       :value="dict.dictValue" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-prevent-click>确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listRecord,
    getRecord,
    delRecord,
    addRecord,
    updateRecord,
    exportRecord,
    exportRepairRecord
  } from "@/api/equipment/eqRepairRecord/api.js";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    listType
  } from "@/api/equipment/type/api";

  export default {
    name: "Record",
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
        // 设备维修记录表格数据
        recordList: [],
        /* 隧道 */
        repairRecordTunnelData: {},
        //设备类型
        eqTypeData: {},
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          repairTime: null,
          repairTunnelId: null,
          repairTypeId: null,
          repairNumber: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          repairTunnelId: [{
            required: true,
            message: '请输入所属隧道',
            trigger: 'blur'
          }],
          repairTime: [{
            required: true,
            message: '维修时间',
            trigger: 'blur'
          }],
          repairTypeId: [{
            required: true,
            message: '请输入设备类型',
            trigger: 'blur'
          }],
          repairNumber: [{
            required: true,
            message: '请输入设备编号',
            trigger: 'blur'
          }],
          repairContentState: [{
            required: true,
            message: '请输入巡检内容',
            trigger: 'blur'
          }],
          repairContent: [{
            required: true,
            message: '请输入维修内容',
            trigger: 'blur'
          }],
          repairResult: [{
            required: true,
            message: '请输入巡检结果',
            trigger: 'blur'
          }],

        },
        repairResultData: [],
      };
    },
    created() {
      this.getList();
      this.getTunnel();
      this.getEqType();
      this.getDicts("sys_repairRecord_result").then(response => {
        this.repairResultData = response.data;
      });
    },
    methods: {
      /** 查询设备维修记录列表 */
      getList() {
        this.loading = true;
        listRecord(this.queryParams).then(response => {
          this.recordList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /* 所选隧道 */
      getTunnel() {
        listTunnels().then(response => {
          this.repairRecordTunnelData = response.rows;
        });
      },
      /** 设备类型 */
      getEqType() {
        listType().then(response => {
          this.eqTypeData = response.rows;
        });
      },
      repairResultFomat(row) {
        return this.selectDictLabel(this.repairResultData, row.repairResult);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          repairId: null,
          repairTunnelId: null,
          repairTime: null,
          repairTypeId: null,
          repairNumber: null,
          repairContentState: null,
          repairContent: null,
          repairResult: null,
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
        this.ids = selection.map(item => item.repairId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加设备维修记录";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        // console.log(this.repairRecordTunnelData);
        // this.repairRecordTunnelData.forEach(item=>{
        //   if(item.tunnelName == this.form.tunnelName){
        //     console.log(item,'item')
        //     this.form.repairTunnelId = item.tunnelId
        //   }
        // })
        const repairId = row.repairId || this.ids
        getRecord(repairId).then(response => {
          this.form = response.data;
          console.log(this.form,'zxczxc')
          this.open = true;
          this.title = "修改设备维修记录";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.repairId != null) {
              updateRecord(this.form).then(response => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addRecord(this.form).then(response => {
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
        const repairIds = row.repairId || this.ids;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRecord(repairIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出设备维修记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRepairRecord(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
      }
    }
  };
</script>
