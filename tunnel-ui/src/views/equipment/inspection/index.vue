<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="巡视人员" prop="inspectionPerson">
        <el-input
          v-model="queryParams.inspectionPerson"
          placeholder="请输入巡视人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="巡视位置" prop="inspectionPosition">
        <el-input
          v-model="queryParams.inspectionPosition"
          placeholder="请输入巡视位置"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属隧道" prop="inspectionTunnel">
       <el-select v-model="queryParams.inspectionTunnel" placeholder="请选择所属隧道" clearable size="small">
         <el-option v-for="item in eqTunnelData"
           :key="item.tunnelId"
           :label="item.tunnelName"
           :value="item.tunnelId" />
       </el-select>
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
          v-hasPermi="['system:inspection:add']"
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
          v-hasPermi="['system:inspection:edit']"
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
          v-hasPermi="['system:inspection:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:inspection:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="巡视人员" align="center" prop="inspectionPerson" />
      <el-table-column label="巡视位置" align="center" prop="inspectionPosition" />
      <el-table-column label="巡视时间" align="center" prop="inspectionTime" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="发现问题" align="center" prop="identifyProblem" />
      <el-table-column label="处理方法" align="center" prop="resolveProblem" />
      <el-table-column label="巡视内容" align="center" prop="inspectionContent" />
      <el-table-column label="是否维修" align="center" prop="isRepair" :formatter="repairFormat" />
      <el-table-column label="维修人员" align="center" prop="repairPerson" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column label="维修详情" align="center" prop="repairDetail" />
      <el-table-column label="备注" align="center" prop="inspectionRemark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-link"
            @click="handleShow(scope.row)"
            v-hasPermi="['system:repair:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:inspection:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:inspection:remove']"
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

    <!-- 添加或修改巡视记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body @close="cancel" width="800px" class="controlDialog">
      <el-form ref="form" :model="form" :rules="rules" :inline="true" label-width="120px">
        <el-form-item label="巡视人员" prop="inspectionPerson" >
          <el-input v-model="form.inspectionPerson" placeholder="请输入巡视人员" />
        </el-form-item>
        <el-form-item label="巡视位置" prop="inspectionPosition">
          <el-input v-model="form.inspectionPosition" placeholder="请输入巡视位置" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="inspectionTunnel">
          <el-select v-model="form.inspectionTunnel" placeholder="请选择所属隧道" clearable size="small">
            <el-option v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡视时间" prop="inspectionTime">
          <el-date-picker clearable size="small"
            v-model="form.inspectionTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择巡视时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发现问题" prop="identifyProblem">
          <el-input type="textarea" v-model="form.identifyProblem" placeholder="请输入发现问题" maxlength="250" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
        <el-form-item label="处理方法" prop="resolveProblem">
          <el-input type="textarea" v-model="form.resolveProblem" placeholder="请输入处理方法" maxlength="250" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
        <el-form-item label="巡视内容">
          <el-input type="textarea" v-model="form.inspectionContent" placeholder="请输入巡视内容" maxlength="250" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
        <el-form-item label="是否维修" prop="isRepair">
          <el-select v-model="form.isRepair" placeholder="请选择是否维修">
            <el-option v-for="item in isRepairOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="维修人员" prop="repairPerson" >
          <el-input v-model="form.repairPerson" placeholder="请输入维修人员" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone" >
          <el-input v-model="form.phone" placeholder="请输入维修人员联系方式" />
        </el-form-item>
        <el-form-item label="维修详情" prop="repairDetail">
          <el-input type="textarea" v-model="form.repairDetail" placeholder="请输入维修详情" maxlength="250" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
        <el-form-item label="备注" prop="inspectionRemark">
          <el-input type="textarea" v-model="form.inspectionRemark" placeholder="请输入备注" maxlength="250" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看巡视记录对话框 -->
    <el-dialog :title="title" :visible.sync="openRecord" width="500px" append-to-body @close="cancelRecord">
      <el-form ref="recordForm" :model="form" label-width="100px">
        <el-form-item label="巡视人员: " prop="inspectionPerson">
          {{form.inspectionPerson}}
        </el-form-item>
        <el-form-item label="巡视位置: " prop="inspectionPosition">
          {{form.inspectionPosition}}
        </el-form-item>
        <el-form-item label="所属隧道: " prop="tunnelName" >
          {{form.tunnelName}}
        </el-form-item>
        <el-form-item label="巡视时间: " prop="inspectionTime">
          {{form.inspectionTime}}
        </el-form-item>
        <el-form-item label="发现问题: " prop="identifyProblem">
          {{form.identifyProblem}}
        </el-form-item>
        <el-form-item label="处理方法: " prop="resolveProblem">
          {{form.resolveProblem}}
        </el-form-item>
        <el-form-item label="巡视内容: " prop="inspectionContent">
          {{form.inspectionContent}}
        </el-form-item>
        <el-form-item label="维修人员: " prop="repairPerson">
          {{form.repairPerson}}
        </el-form-item>
        <el-form-item label="联系电话: " prop="phone">
          {{form.phone}}
        </el-form-item>
        <el-form-item label="维修详情: " prop="createTime">
          {{form.createTime}}
        </el-form-item>
        <el-form-item label="备注: " prop="repairDetail">
          {{form.repairDetail}}
        </el-form-item>
        <el-form-item label="创建时间: " prop="createTime">
          {{form.createTime}}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelRecord">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInspection, getInspection, delInspection, addInspection, updateInspection, exportInspection } from "@/api/equipment/inspection/inspection.js";
import Editor from '@/components/Editor';
import { listTunnels} from "@/api/equipment/tunnel/api.js";

export default {
  name: "Inspection",
  components: {
    Editor,
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
      // 巡视记录表格数据
      inspectionList: [],
      eqTunnelData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      changeAddOrEdit: null,
      isRepairOptions: [
        {
          value: 1,
          label: "是",
        },
        {
          value: 0,
          label: "否",
        },
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectionPerson: null,
        inspectionPosition: null,
        inspectionTunnel: null,
        inspectionRemark: null,
        identifyProblem: null,
        resolveProblem: null,
        inspectionContent: null,
        createTime: null,
        createName: null,
        updateName: null,
        updateTime:null,
        tunnelName:null,
        isRepair:null,
        repairPerson:null,
        phone:null,
        repairDetail:null,
      },
      // 表单参数
      form: {
      },
      // 表单校验
      rules: {
        inspectionPerson: [{
            required: true,
            message: '请输入巡视人员',
            trigger: 'blur'
        },{
            message: '长度在1~30个字符之间',
            min: 1,
            max: 30,
            trigger: 'blur'
        }],
        inspectionPosition: [{
            required: true,
            message: '请输入巡视位置',
            trigger: 'blur'
        },{
            message: '长度在1~30个字符之间',
            min: 1,
            max: 30,
            trigger: 'blur'
        }],
        inspectionTunnel: [{
            required: true,
            message: '请选择所属隧道',
            trigger: 'change'
        }],
        isRepair: [{
          required: true,
          message: '请选择是否维修',
          trigger: 'change'
        }],
        repairPerson: [
          { required: false, min: 0, max: 30, message: '长度在0~30个字符之间', trigger: 'blur' },
        ],
        phone: [
          { required: false, min: 0, max: 30, message: '长度在0~30个字符之间', trigger: 'blur' },
        ],
      },
      // 是否维修字典
      isRepairDate: [],
      openRecord: false, // 查看记录对话框
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getDicts("sys_plc_tunnel").then(response => {
      this.eqTunnelIdOptions = response.data;
    });
    this.getDicts("patrol_isRepair").then(response => {
      this.isRepairDate = response.data;
    });
  },
  methods: {
    /** 查询巡视记录列表 */
    getList() {
      this.loading = true;
      listInspection(this.queryParams).then(response => {
        this.inspectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
      });
    },
    // 隧道类型字典翻译
    tunnelFormat(row, column) {
      return this.selectDictLabel(this.eqTunnelIdOptions, row.inspectionTunnel);
    },
    // 是否维修字典翻译
    repairFormat(row, column) {
      return this.selectDictLabel(this.isRepairDate, row.isRepair);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset('form');
      this.$refs.form.resetFields()
    },
    // 查看记录对话框-取消按钮
    cancelRecord() {
      this.openRecord = false;
      this.reset('form');
    },
    // 表单重置
    reset() {
      this.form = {
        inspectionId: null,
        inspectionPerson: null,
        inspectionPosition: null,
        inspectionTunnel: null,
        inspectionRemark: null,
        identifyProblem: null,
        resolveProblem: null,
        inspectionContent: null,
        createTime: null,
        createName: null,
        updateTime: null,
        updateName: null,
        isRepair: null,
        repairPerson: null,
        phone: null,
        repairDetail: null,
      };
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
      this.ids = selection.map(item => item.inspectionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.changeAddOrEdit = 'add'
      this.open = true;
      this.title = "添加巡视记录";
    },
    /** 查看按钮操作 */
    handleShow(row) {
      const inspectionId = row.inspectionId || row.id || this.ids
      getInspection(inspectionId).then(response => {
        this.form = response.data;
        this.openRecord = true;
        this.title = "查看巡视记录";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.changeAddOrEdit = 'edit'
      const inspectionId = row.inspectionId || this.ids
      getInspection(inspectionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡视记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitFormLoading) return
      this.submitFormLoading = true
      this.$refs["form"].validate(async valid => {
        if (valid) {
          if (this.form.inspectionId != null) {
            await updateInspection(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addInspection(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        this.submitFormLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const inspectionIds = row.inspectionId || this.ids;
      this.$confirm('是否确认删除巡视记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInspection(inspectionIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有巡视记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportInspection(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
<style scoped lang="scss">
  
  ::v-deep .el-form-item--medium .el-form-item__content{
      width: 220px;
  }
</style>
