<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属隧道" prop="plcTunnelId">
        <el-select v-model="queryParams.plcTunnelId" placeholder="请选择所属隧道" style="width: 200px;margin-right: 10px;">
          <el-option
            v-for="item in plcTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备方向" prop="plcTunnelId">
        <el-select v-model="queryParams.eqDirection" placeholder="请选择设备方向" style="width: 200px;">
          <el-option
            v-for="item in eqDirectionData"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['business:roadState:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['business:roadState:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:roadState:remove']"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:roadState:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="roadStateList"
    height="630" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" />
      <el-table-column label="设备编号" align="center" prop="devno" />
      <el-table-column label="设备名称" align="center" prop="eqName" />
      <el-table-column label="设备方向" align="center" prop="eqDirection"  :formatter="eqDirectionFormat"/>
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="路面温度(℃)" align="center" prop="pavementtemp" />
      <el-table-column label="冰点温度(℃)" align="center" prop="icepointtemp" />
      <el-table-column label="路面状况" align="center" prop="pavementstatus" />
      <el-table-column label="摩擦系数" align="center" prop="frictionalcoe" />
      <el-table-column label="水膜厚度(MM)" align="center" prop="waterfilmheigh" />
      <el-table-column label="含盐量(NaCl)" align="center" prop="saltnessnacl" />
      <el-table-column label="含盐量(MgCl2)" align="center" prop="saltnessmgcl2" />
      <el-table-column label="含盐量(CaCl2)" align="center" prop="saltnesscacl2" />
      <el-table-column label="含冰比例(%)" align="center" prop="icepercent" />
      <el-table-column label="是否有结冰可能" align="center" prop="icepossibiblity" :formatter="icepossibiblityFormat" />
      <el-table-column label="采集时间" align="center" prop="timeCollect">
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="timeUpdate">
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['business:roadState:edit']"-->
<!--          >修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:roadState:remove']"
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

    <!-- 添加或修改路面监测信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="设备编号" prop="devno">
          <el-input v-model="form.devno" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="路面温度 -40~80" prop="pavementtemp">
          <el-input v-model="form.pavementtemp" placeholder="请输入路面温度 -40~80" />
        </el-form-item>
        <el-form-item label="冰点温度 -40~80" prop="icepointtemp">
          <el-input v-model="form.icepointtemp" placeholder="请输入冰点温度 -40~80" />
        </el-form-item>
        <el-form-item label="路面状况">
          <el-input v-model="form.pavementstatus" placeholder="请输入路面状况" />
        </el-form-item>
        <el-form-item label="摩擦系数" prop="frictionalcoe">
          <el-input v-model="form.frictionalcoe" placeholder="请输入摩擦系数" />
        </el-form-item>
        <el-form-item label="水膜厚度(MM)" prop="waterfilmheigh">
          <el-input v-model="form.waterfilmheigh" placeholder="请输入水膜厚度" />
        </el-form-item>
        <el-form-item label="含盐量(NaCl)" prop="saltnessnacl">
          <el-input v-model="form.saltnessnacl" placeholder="请输入含盐量(NaCl)" />
        </el-form-item>
        <el-form-item label="含盐量(MgCl2)" prop="saltnessmgcl2">
          <el-input v-model="form.saltnessmgcl2" placeholder="请输入含盐量(MgCl2)" />
        </el-form-item>
        <el-form-item label="含盐量(CaCl2)" prop="saltnesscacl2">
          <el-input v-model="form.saltnesscacl2" placeholder="请输入含盐量(CaCl2)" />
        </el-form-item>
        <el-form-item label="含冰比例" prop="icepercent">
          <el-input v-model="form.icepercent" placeholder="请输入含冰比例" />
        </el-form-item>
        <el-form-item label="是否有结冰可能" prop="icepossibiblity">
          <el-input v-model="form.icepossibiblity" placeholder="请输入是否有结冰可能" />
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
import { listRoadState, getRoadState, delRoadState, addRoadState, updateRoadState, exportRoadState } from "@/api/business/roadState";
import { listTunnels } from "@/api/equipment/tunnel/api.js";

export default {
  name: "RoadState",
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
      // 路面监测信息表格数据
      roadStateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 0：无结冰可能 1：有结冰可能 2：无法判断是否有结冰可能字典
      icepossibiblityOptions: [],
      plcTunnelData: [],
      eqDirectionData: [
        {value: 0, label: '下行'},
        {value: 1, label: '上行'},
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plcTunnelId: null,
        eqDirection: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        devno: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getDicts("icepossibiblity").then(response => {
      this.icepossibiblityOptions = response.data;
    });
  },
  methods: {
    /** 查询路面监测信息列表 */
    getList() {
      this.loading = true;
      listRoadState(this.queryParams).then(response => {
        this.roadStateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.plcTunnelData = response.rows;
        console.log(this.plcTunnelData)
        console.log('this.plcTunnelData')
      });
    },
    // 0：无结冰可能 1：有结冰可能 2：无法判断是否有结冰可能字典翻译
    icepossibiblityFormat(row, column) {
      return this.selectDictLabel(this.icepossibiblityOptions, row.icepossibiblity);
    },
    eqDirectionFormat(row, column) {
      return row.eqDirection == 1 ? '上行' : '下行'
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
        devno: null,
        pavementtemp: null,
        icepointtemp: null,
        pavementstatus: null,
        frictionalcoe: null,
        waterfilmheigh: null,
        saltnessnacl: null,
        saltnessmgcl2: null,
        saltnesscacl2: null,
        icepercent: null,
        icepossibiblity: null,
        timeCollect: null,
        timeUpdate: null
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
      this.queryParams.plcTunnelId = null;
      this.queryParams.eqDirection = null;
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
      this.title = "添加路面监测信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRoadState(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改路面监测信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRoadState(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRoadState(this.form).then(response => {
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
      this.$confirm('是否确认删除选中的路面监测信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRoadState(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出路面监测信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRoadState(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
