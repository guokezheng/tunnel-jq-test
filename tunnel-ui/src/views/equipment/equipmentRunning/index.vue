<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="所属隧道" prop="eqTunnelId">
        <el-select v-model="queryParams.eqTunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="plc主机" prop="eqHostId">
        <el-select v-model="queryParams.eqHostId" placeholder="请选择plc主机" clearable size="small">
          <el-option
            v-for="item in eqHostData"
            :key="item.plcId"
            :label="item.plcName"
            :value="item.plcId"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="设备名称" prop="eqName">
        <el-input
          v-model="queryParams.eqName"
          placeholder="请输入设备名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备类型" prop="eqType">
        <el-select v-model="queryParams.eqType" placeholder="请选择设备类型" clearable size="small">
          <el-option
            v-for="item in eqTypeData"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
      </el-form-item>
     <!-- <el-form-item label="照明灯类型" prop="eqLampType">
        <el-select v-model="queryParams.eqLampType" placeholder="请选择照明灯类型" clearable size="small">
          <el-option
            v-for="dict in eqLampTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="设备状态" prop="deviceState">
        <el-select v-model="queryParams.deviceState" placeholder="请选择设备状态" clearable size="small">
          <el-option
            v-for="dict in deviceStateOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:devices:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:devices:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:devices:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:devices:export']"
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
    </el-row> -->

    <el-table v-loading="loading" :data="devicesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="Id" align="center" prop="eqId" /> -->
      <!-- <el-table-column label="plc主机 id" align="center" prop="eqHostId" :formatter="eqHostIdFormat" /> -->
      <el-table-column label="plc主机" align="center" prop="plcName.plcName"/>
      <!-- <el-table-column label="所属隧道 id" align="center" prop="eqTunnelId" :formatter="eqTunnelIdFormat" /> -->
      <el-table-column label="所属隧道" align="center" prop="tunnelName.tunnelName"/>
      <el-table-column label="设备名称" align="center" prop="eqName" />
      <!-- <el-table-column label="设备方向" align="center" prop="eqDirection" /> -->
     <!-- <el-table-column label="桩号" align="center" prop="stakeMark" /> -->
      <!-- <el-table-column label="设备类型" align="center" prop="eqType" :formatter="eqTypeFormat" /> -->
      <el-table-column label="设备类型" align="center" prop="typeName.typeName"/>
      <!-- <el-table-column label="照明灯类型" align="center" prop="eqLampType" :formatter="eqLampTypeFormat" /> -->
     <!-- <el-table-column label="点位地址1" align="center" prop="eqFeedbackAddress1" />
      <el-table-column label="点位地址2" align="center" prop="eqFeedbackAddress2" />
      <el-table-column label="点位地址3" align="center" prop="eqFeedbackAddress3" />
      <el-table-column label="点位地址4" align="center" prop="eqFeedbackAddress4" />
      <el-table-column label="点位地址5" align="center" prop="eqFeedbackAddress5" /> -->
      <!-- <el-table-column label="控制点位地址" align="center" prop="eqControlPointAddress" /> -->
      <el-table-column label="设备状态" align="center" prop="deviceState" :formatter="deviceStateFormat" />
      <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
      <!-- <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改者" align="center" prop="updateBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:devices:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:devices:remove']"
          >删除</el-button> -->
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

    <!-- 添加或修改设备对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-row>
              <el-col :span="12">
                <el-form-item label="所属隧道" prop="tunnels">
                  <el-select v-model="form.eqTunnelId" @change="getPlcs()" placeholder="请选择所属隧道" >
                    <el-option
                      v-for="item in eqTunnelData"
                      :key="item.tunnelId"
                      :label="item.tunnelName"
                      :value="item.tunnelId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="plc主机" prop="hosts">
                  <el-select v-model="form.eqHostId" placeholder="请选择plc主机" clearable>
                    <el-option
                      v-for="item in eqHostData"
                      :key="item.plcId"
                      :label="item.plcName"
                      :value="item.plcId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备名称" prop="eqName">
                  <el-input v-model="form.eqName" placeholder="请输入设备名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备方向" prop="eqDirection">
                  <el-input v-model="form.eqDirection" placeholder="请输入设备方向" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="桩号" prop="stakeMark">
                  <el-input v-model="form.stakeMark" placeholder="请输入桩号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备类型">
                  <el-select v-model="form.eqType" placeholder="请选择设备类型">
                    <el-option
                      v-for="item in eqTypeData"
                      :key="item.typeId"
                      :label="item.typeName"
                      :value="item.typeId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="照明灯类型">
                  <el-select v-model="form.eqLampType" placeholder="请选择照明灯类型">
                    <el-option
                      v-for="dict in eqLampTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="parseInt(dict.dictValue)"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点位地址1" prop="eqFeedbackAddress1">
                  <el-input v-model="form.eqFeedbackAddress1" placeholder="请输入点位地址1" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点位地址2" prop="eqFeedbackAddress2">
                  <el-input v-model="form.eqFeedbackAddress2" placeholder="请输入点位地址2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点位地址3" prop="eqFeedbackAddress3">
                  <el-input v-model="form.eqFeedbackAddress3" placeholder="请输入点位地址3" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点位地址4" prop="eqFeedbackAddress4">
                  <el-input v-model="form.eqFeedbackAddress4" placeholder="请输入点位地址4" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点位地址5" prop="eqFeedbackAddress5">
                  <el-input v-model="form.eqFeedbackAddress5" placeholder="请输入点位地址5" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
          </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { listEquipmentRunning} from "@/api/equipment/equipmentRunning/api";
import { listHosts } from "@/api/equipment/plc/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";

export default {
  name: "Devices",
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
      // 设备表格数据
      devicesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // plc主机 id字典
      /* eqHostIdOptions: [], */
      //plc主机
      eqHostData:{},
      // 所属隧道 id字典
      /* eqTunnelIdOptions: [], */
      //所属隧道
      eqTunnelData:{},
      // 设备类型字典
      /* eqTypeOptions: [], */
      //设备类型
      eqTypeData:{},
      // 照明灯类型字典
      eqLampTypeOptions: [],
      // 设备状态字典
      deviceStateOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqHostId: null,
        eqTunnelId: null,
        eqName: null,
        eqType: null,
        eqLampType: null,
        deviceState: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
		  /* tunnels: [{required: true, message: '请选择隧道', trigger: 'blur'}],
		  hosts: [{required: true, message: '请选择PLC主机', trigger: 'blur'}],
		  eqName: [
		             { required: true, message: '请输入设备名称', trigger: 'blur' },
		             { min: 3, max: 5, message: '长度在 2 到 6 个字符', trigger: 'blur' }
		           ] */
      }
    };
  },
  created() {
    this.getList();
    this.getPlc();
    this.getTunnel();
    this.getEqType();
    /* this.getDicts("sys_eq_plc").then(response => {
      this.eqHostIdOptions = response.data;
    }); */
    /* this.getDicts("sys_plc_tunnel").then(response => {
      this.eqTunnelIdOptions = response.data;
    });
    this.getDicts("sys_equipment_type").then(response => {
      this.eqTypeOptions = response.data;
    }); */
    this.getDicts("sys_eq_light").then(response => {
      this.eqLampTypeOptions = response.data;
    });
    this.getDicts("sys_eq_state").then(response => {
      this.deviceStateOptions = response.data;
    });
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listEquipmentRunning(this.queryParams).then(response => {
        this.devicesList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** plc主机 */
    getPlc() {
      listHosts().then(response => {
        this.eqHostData = response.rows;
      });
    },
    /** plc主机 */
    getPlcs() {
      listHosts({plcTunnelId:this.form.eqTunnelId}).then(response => {
        this.eqHostData = response.rows;
      });
    },
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
    // plc主机 id字典翻译
    /* eqHostIdFormat(row, column) {
      return this.selectDictLabel(this.eqHostIdOptions, row.eqHostId);
    }, */
    // 所属隧道 id字典翻译
    /* eqTunnelIdFormat(row, column) {
      return this.selectDictLabel(this.eqTunnelIdOptions, row.eqTunnelId);
    }, */
    // 设备类型字典翻译
    /* eqTypeFormat(row, column) {
      return this.selectDictLabel(this.eqTypeOptions, row.eqType);
    }, */
    // 照明灯类型字典翻译
    eqLampTypeFormat(row, column) {
      return this.selectDictLabel(this.eqLampTypeOptions, row.eqLampType);
    },
    // 设备状态字典翻译
    deviceStateFormat(row, column) {
      return this.selectDictLabel(this.deviceStateOptions, row.deviceState);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        eqId: null,
        eqHostId: null,
        eqTunnelId: null,
        eqName: null,
        eqDirection: null,
        stakeMark: null,
        eqType: null,
        eqLampType: null,
        eqFeedbackAddress1: null,
        eqFeedbackAddress2: null,
        eqFeedbackAddress3: null,
        eqFeedbackAddress4: null,
        eqFeedbackAddress5: null,
        eqControlPointAddress: null,
        deviceState: null,
        remark: null,
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
      this.ids = selection.map(item => item.eqId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
   /* handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备";
    }, */
    /** 修改按钮操作 */
   /* handleUpdate(row) {
      this.reset();
      const eqId = row.eqId || this.ids
      getDevices(eqId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    /** 提交按钮 */
   /* submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.eqId != null) {
            updateDevices(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDevices(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    }, */
    /** 删除按钮操作 */
   /* handleDelete(row) {
      const eqIds = row.eqId || this.ids;
      this.$confirm('是否确认删除设备编号为"' + eqIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDevices(eqIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    }, */
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/devices/export', {
        ...this.queryParams
      }, `system_devices.xlsx`)
    }
  }
};
</script>
