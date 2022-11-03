<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="故障位置" prop="faultLocation">
        <el-input
          v-model="queryParams.faultLocation"
          placeholder="请输入故障位置"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="故障类型" prop="faultType">
        <el-select v-model="queryParams.faultType"  placeholder="请选择故障类型" clearable size="small">
          <el-option
            v-for="dict in dict.type.fault_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="故障描述" prop="faultDescription">
        <el-input
          v-model="queryParams.faultDescription"
          placeholder="请输入故障描述"
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
    </el-row>

    <el-table v-loading="loading" :data="listList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="故障编号" align="center" prop="id" />
      <el-table-column label="隧道id" align="center" prop="tunnelId" />
      <el-table-column label="故障位置" align="center" prop="faultLocation" />-->
      <el-table-column label="故障类型" align="center" prop="faultType" />
<!--      <el-table-column label="故障发现源" align="center" prop="faultSource" />-->
      <el-table-column label="发现时间" align="center" prop="faultFxtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.faultFxtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="持续时间" align="center" prop="faultCxtime" />
<!--      <el-table-column label="故障填报人" align="center" prop="faultTbr" />-->
<!--      <el-table-column label="故障填报时间" align="center" prop="faultTbtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.faultTbtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="设备id" align="center" prop="eqId" />
      <el-table-column label="设备状态" align="center" prop="eqStatus" />
<!--      <el-table-column label="故障代码" align="center" prop="faultCode" />-->
      <el-table-column label="故障等级" align="center" prop="faultLevel" />
      <el-table-column label="消除状态" align="center" prop="falltRemoveStatue" />
<!--      <el-table-column label="故障描述" align="center" prop="faultDescription" />-->
      <el-table-column label="状态" align="center" prop="faultStatus" />
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

    <!-- 添加或修改故障清单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="740px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
<!--          <el-col :span="12">-->
          <el-form-item label="所属隧道" prop="tunnelId">
            <el-select v-model="form.eqTunnelId"  placeholder="请选择所属隧道">
              <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                         :value="item.tunnelId"></el-option>
            </el-select>
          </el-form-item>
<!--          </el-col>-->
<!--        <el-form-item label="故障位置" prop="faultLocation">
          <el-input v-model="form.faultLocation" placeholder="请输入故障位置" />
        </el-form-item>-->
<!--          <el-col :span="12">-->
          <el-form-item label="故障类型" prop="faultType">
            <el-select v-model="form.faultType" placeholder="请选择故障类型" >
              <el-option
                v-for="dict in dict.type.fault_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value" />
            </el-select>


          </el-form-item>
<!--          </el-col>-->
        <el-form-item label="故障发现源" prop="faultSource">
          <el-input v-model="form.faultSource" placeholder="请输入故障发现源" />
        </el-form-item>
        <el-form-item label="故障发现时间" prop="faultFxtime">
          <el-date-picker clearable size="small"
            v-model="form.faultFxtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择故障发现时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="故障持续时间" prop="faultCxtime">
          <el-input v-model="form.faultCxtime" placeholder="请输入故障持续时间" />
        </el-form-item>
<!--        <el-form-item label="故障填报人" prop="faultTbr">
          <el-input v-model="form.faultTbr" placeholder="请输入故障填报人" />
        </el-form-item>-->
        <el-form-item label="故障填报时间" prop="faultTbtime">
          <el-date-picker clearable size="small"
            v-model="form.faultTbtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择故障填报时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备名称" prop="eqId">
          <el-select v-model="form.codeDeviceId" placeholder="请选择设备名称">
            <el-option
              v-for="item in eqListData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="故障位置" prop="faultLocation">
          <el-input v-model="form.faultLocation" placeholder="请输入故障位置" />
        </el-form-item>
        <el-form-item label="设备状态">
          <el-radio-group v-model="form.eqStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="故障代码" prop="faultCode">
          <el-input v-model="form.faultCode" placeholder="请输入故障代码" />
        </el-form-item>
        <el-form-item label="故障等级" prop="faultLevel">
          <el-select v-model="form.faultType" placeholder="请选择故障等级" >
            <el-option
              v-for="dict in dict.type.fault_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value" />
          </el-select>
<!--          <el-input v-model="form.faultLevel" placeholder="请输入故障等级" />-->
        </el-form-item>
        <el-form-item label="故障消除状态" prop="falltRemoveStatue">
          <el-select v-model="form.faultType" placeholder="请选择故障等级" >
            <el-option
              v-for="dict in dict.type.fault_remove_statue"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value" />
          </el-select>
<!--          <el-input v-model="form.falltRemoveStatue" placeholder="请输入故障消除状态" />-->
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDescription">
          <el-input v-model="form.faultDescription" placeholder="请输入故障描述" />
        </el-form-item>
<!--        <el-form-item label="状态">
          <el-radio-group v-model="form.faultStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>-->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">仅保存</el-button>
        <el-button type="primary" @click="publishForm">保存并发布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listList, getList, delList, addList, updateList, exportList } from "@/api/electromechanicalPatrol/fault";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {listType} from "@/api/equipment/type/api";
import {listDevices} from "@/api/equipment/eqlist/api";



export default {
  name: "List",
  //字典值：故障类型、故障等级，故障消除状态
  dicts: [ 'fault_type','fault_level','fault_remove_statue'],
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
      // 故障清单表格数据
      listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //故障类型
      faultTypeData: {},
      //所属隧道
      eqTunnelData: {},
      //设备类型
      eqTypeData: {},
      //设备
      eqListData:{},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: null,
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: null
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
    this.getTunnel();
    this.getEqType();
    this.getDevices();
  },
  methods: {
    /** 查询故障清单列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        console.log(response.rows,"所属隧道列表")
        this.eqTunnelData = response.rows;
      });
    },

    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 设备 */
    getDevices() {
      listDevices({fEqId:this.form.codePlcId,eqType:this.form.deviceTypeId}).then(response => {
        this.eqListData = response.rows;
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
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: "0",
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: 0
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
      this.title = "添加故障清单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改故障清单";
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

    publishForm (){
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
              this.$modal.msgSuccess("发布成功");
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
      this.$modal.confirm('是否确认删除故障清单编号为"' + ids + '"的数据项？').then(function() {
        return delList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有故障清单数据项？').then(() => {
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
