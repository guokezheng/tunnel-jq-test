<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="PLC" prop="codePlcId">
        <el-select v-model="queryParams.codePlcId" placeholder="请选择plc" clearable size="small">
          <el-option
            v-for="item in eqHostData"
            :key="item.eqId"
            :label="item.eqName"
            :value="item.eqId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" prop="deviceTypeId">
        <el-select v-model="queryParams.deviceTypeId" placeholder="请选择设备类型" clearable size="small">
          <el-option
            v-for="item in eqTypeData"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="codeDeviceId">
        <el-input v-model="queryParams.codeDeviceId" placeholder="请输入设备名称" clearable size="small"
                  @keyup.enter.native="handleQuery" />
        <!--<el-select v-model="queryParams.codeDeviceId" placeholder="请选择设备名称" clearable size="small">-->
          <!--<el-option-->
            <!--v-for="item in eqListData"-->
            <!--:key="item.eqId"-->
            <!--:label="item.eqName"-->
            <!--:value="item.eqId"-->
          <!--/>-->
        <!--</el-select>-->
      </el-form-item>
      <!-- <el-form-item label="设备状态" prop="codeDeviceState">
        <el-select v-model="queryParams.codeDeviceState" placeholder="请选择设备状态" clearable size="small">
          <el-option
            v-for="item in codeDeviceStateData"
            :key="item.id"
            :label="item.stateName"
            :value="item.id"
          />
        </el-select>
      </el-form-item> -->
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
          v-hasPermi="['system:devCmd:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpddate"
          v-hasPermi="['system:devCmd:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:devCmd:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:devCmd:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="devcmdList"
    height="630" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="PLC名称" align="center" prop="plcName.eqName" />
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
      <el-table-column label="设备名称" align="center" prop="eqName.eqName" />
      <el-table-column label="设备状态" align="center" prop="stateName.stateName"/>
      <el-table-column label="报文" align="center" prop="command" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpddate(scope.row)"
            v-hasPermi="['system:devCmd:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:devCmd:remove']"
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

    <!-- 添加或修改设备指令对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="PLC" prop="codePlcId">
          <el-select v-model="form.codePlcId" @change="plcChange()" placeholder="请选择plc">
            <el-option
              v-for="item in eqHostData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceTypeId">
          <el-select v-model="form.deviceTypeId" @change="changeDeviceType()" placeholder="请选择设备类型" >
            <el-option
              v-for="item in eqTypeData"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备名称" prop="codeDeviceId">
          <el-select v-model="form.codeDeviceId" placeholder="请选择设备名称">
            <el-option
              v-for="item in eqListData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备状态" prop="codeDeviceState">
          <el-select v-model="form.codeDeviceState" placeholder="请选择设备状态" >
            <el-option
              v-for="item in codeDeviceStateData"
              :key="item.id"
              :label="item.stateName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报文" prop="command">
          <el-input v-model="form.command" placeholder="请输入报文" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
<script>
import { listDevcmd, getDevcmd, delDevcmd, addDevcmd, updateDevcmd } from "@/api/equipment/deviceCmd/api.js";
import { listHosts } from "@/api/equipment/plc/api";
import { listType } from "@/api/equipment/type/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { listEqTypeState } from "@/api/equipment/eqTypeState/api";

export default {
  name: "devCmd",
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
      // 设备指令表格数据
      devcmdList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //plc主机
      eqHostData:[],
      //设备类型
      eqTypeData:{},
      //设备
      eqListData:{},
      // 设备状态
      /* codeDeviceStateOptions: [], */
      codeDeviceStateData:{},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        codeDeviceId: null,
        eqHostId:null,
        codePlcId: null,
        deviceTypeId: null,
        codeDeviceState: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        codeDeviceId: [{required: true, message: '请选择设备', trigger: 'change'}],
        command: [
                   { required: true, message: '请输入报文', trigger: 'blur' },
                   { min: 2, max: 300, message: '长度在 2 到 300 个字符', trigger: 'blur' }
                 ],
        deviceTypeId: [{required: true, message: '请选择设备类型', trigger: 'change'}],
        codeDeviceState: [{required: true, message: '请选择设备状态', trigger: 'change'}],
      }
    };
  },
  watch:{
    // 'form.codePlcId': {
    //   handler(newVal, oldVal) {
    //     if(newVal) {
    //       this.getDevices()
    //     } else {
    //       this.eqListData = [];
    //       this.form.codeDeviceId = null;
    //     }
    //   },
    // },
    // 'form.deviceTypeId':{
    //   handler(newVal, oldVal) {
    //     if(newVal) {
    //       this.getDevices()
    //       this.changeDeviceState();
    //     } else {
    //       this.eqListData = [];
    //     }
    //     this.form.codeDeviceId = null;
    //     this.form.codeDeviceState = "";
    //   },
    // }
  },
  created() {
    this.getList();
    this.getPlc();
    this.getEqType();
  },
  methods: {
    /** 查询设备指令列表 */
    getList() {
      this.loading = true;
      listDevcmd(this.queryParams).then(response => {
        this.devcmdList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    //plc主机改变时清空设备名称
    plcChange(){
      this.form.codeDeviceId = null;
      this.getDevices();
    },
    //设备类型改变时清空设备名称、设备状态
    changeDeviceType(){
      this.form.codeDeviceId = null;
      this.form.codeDeviceState = "";
      this.getDevices();
      this.changeDeviceState();
    },

    /** 设备 */
    getDevices() {
      listDevices({fEqId:this.form.codePlcId,eqType:this.form.deviceTypeId}).then(response => {
        this.eqListData = response.rows;
      });
    },
    /** plc主机 */
    getPlc() {
      listDevices({eqType:"0"}).then(response => {
        this.eqHostData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then(response => {
        this.eqTypeData = response.rows;
      });
    },
    //设备类型改变时更新设备状态
    changeDeviceState(){
      // if(this.form.codeDeviceState != '') this.form.codeDeviceState = '';
      this.getDeviceState();
    },
    // 设备状态
    async getDeviceState() {
      //传参：设备类型ID，设备状态可控
      await listEqTypeState({stateTypeId:this.form.deviceTypeId,isControl:1}).then(response => {
        console.log(response.rows,"设备状态")
        this.codeDeviceStateData = response.rows;
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
        codeId: null,
        codeDeviceId: null,
        codePlcId: null,
        command: null,
        deviceTypeId: null,
        codeDeviceState: null,
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
      this.ids = selection.map(item => item.codeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备指令";
    },
    /** 修改按钮操作 */
    async handleUpddate(row) {
      this.reset();
      const codeId = row.codeId || this.ids
      await getDevcmd(codeId).then(response => {
        console.log(response.data,"修改设备指令")
        this.form = response.data;
        this.getDevices();
        this.getDeviceState();
      });

      this.title = "修改设备指令";
      this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.codeId != null) {
            updateDevcmd(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDevcmd(this.form).then(response => {
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
      const codeIds = row.codeId || this.ids;
      this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDevcmd(codeIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/devCmd/export', {
        ...this.queryParams
      }, `system_devcmd.xlsx`)
    }
  }
};
</script>

<!-- <head>
  <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <style>
        .el-scrollbar__wrap{
          overflow-x: hidden;
        }
    </style>
</head> -->
