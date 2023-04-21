<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="检修人" prop="repairName">
        <el-input
          v-model="queryParams.repairName"
          placeholder="请输入检修人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属隧道" prop="repairTunnel">
        <el-select v-model="queryParams.repairTunnel" placeholder="请选择所属隧道" clearable size="small">
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
          v-hasPermi="['system:repair:add']"
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
          v-hasPermi="['system:repair:edit']"
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
          v-hasPermi="['system:repair:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:repair:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检修人员" align="center" prop="repairName" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="检修时间" align="center" prop="repairTime" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.repairTime}}</span>
        </template>
      </el-table-column>
      <el-table-column label="检修位置" align="center" prop="repairPosition" />
      <el-table-column label="检修内容" align="center" prop="repairContent" />
      <el-table-column label="检修结果" align="center" prop="repairResult" :formatter="repairResultFomat" />
      <el-table-column label="备注" align="center" prop="repairRemark" />
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
            v-hasPermi="['system:repair:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:repair:remove']"
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

    <!-- 添加或修改设备巡检修对话框 -->
     <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body @close="dialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="检修人" prop="repairName">
          <el-input v-model="form.repairName" placeholder="请输入检修人名称" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="repairTunnel">
          <el-select v-model="form.repairTunnel" placeholder="请选择所属隧道" clearable size="small" style="width: 100%">
            <el-option v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="检修时间" prop="repairTime">
          <el-date-picker clearable size="small"
            v-model="form.repairTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择检修时间"
            style="width: 100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="检修位置" prop="repairPosition">
          <el-input v-model="form.repairPosition" placeholder="请输入检修位置" />
        </el-form-item>
        <el-form-item label="检修内容">
          <el-input type="textarea" v-model="form.repairContent" placeholder="请输入检修内容" maxlength="250"/>
        </el-form-item>
        <el-form-item label="检修结果" prop="repairResult">
          <el-select v-model="form.repairResult" placeholder="请输入检修结果" size="small" style="width: 100%">
            <el-option v-for="dict in repairResultData"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="repairRemark">
          <el-input type="textarea" v-model="form.repairRemark" placeholder="请输入备注" maxlength="250" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
     <!-- 查看巡检修对话框 -->
    <el-dialog :title="title" :visible.sync="lookOpen" width="500px" append-to-body @close="dialogClose">
      <el-form ref="lookForm" :model="form" label-width="90px">
        <el-form-item label="检修人员: " prop="repairName">{{form.repairName}}</el-form-item>
        <el-form-item label="所属隧道: " prop="tunnelName" >
          {{form.tunnelName}}
        </el-form-item>
        <el-form-item label="检修时间: " prop="repairTime">
          {{form.repairTime}}
        </el-form-item>
        <el-form-item label="检测位置: " prop="repairPosition">
          {{form.repairPosition}}
        </el-form-item>
        <el-form-item label="检测内容: " prop="repairContent">
          {{form.repairContent}}
        </el-form-item>
        <el-form-item label="备注: " prop="repairRemark">
          {{form.repairRemark}}
        </el-form-item>
        <el-form-item label="创建时间: " prop="createTime">
          {{form.createTime}}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="lookOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRepair, getRepair, delRepair, addRepair, updateRepair, exportRepair } from "@/api/equipment/repair/repair.js";
import Editor from '@/components/Editor';
import { listTunnels} from "@/api/equipment/tunnel/api.js";

export default {
  name: "Repair",
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
      // 设备巡检修表格数据
      repairList: [],
      changeAddOrEdit: null,
      eqTunnelData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      lookOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        repairName: null,
        repairTunnel: null,
        tunnelName: null,
        repairRemark: null,
        repairTime: null,
        repairPosition: null,
        repairContent: null,
        repairResult: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
          repairName: [
              { required: true, message: "检修人不能为空", trigger: "blur" },
              { min: 1, max: 30, message: '长度在1~30个字符之间', trigger: 'blur' }
          ],
          repairTunnel: [
              { required: true, message: "所属隧道不能为空", trigger: "change" }
          ],
          repairTime: [
              { required: true, message: "检修时间不能为空", trigger: "change" }
          ],
          repairPosition: [
              { required: true, message: "检测位置不能为空", trigger: "blur" },
              { pattern: /^[0-9a-zA-Z+]{1,30}$/, message: '长度在1~30个字符之间，由字母、数字、+组成', trigger: 'blur' }
          ],
          repairResult: [
            { required: true, message: "检测结果不能为空", trigger: "change" },
          ]
      },
      // 检修结果字典
      repairResultData: [],
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getDicts("sys_plc_tunnel").then(response => {
      this.eqTunnelIdOptions = response.data;
    });
    this.getDicts("sys_plc_repairResult").then(response => {
      this.repairResultData = response.data;
    });
  },
  methods: {
    /** 查询设备检修列表 */
    getList() {
      this.loading = true;
      listRepair(this.queryParams).then(response => {
        this.repairList = response.rows;
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
      return this.selectDictLabel(this.eqTunnelIdOptions, row.repairTunnel);
    },
    // 检修结果字典翻译
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
        repairName: null,
        repairTunnel: null,
        repairType: null,
        repairEqName: null,
        repairEqState: null,
        repairRemark: null,
        repairTime: null,
        repairPosition: null,
        repairContent: null,
        repairResult: null
      };
      this.resetForm("form");
    },
    dialogClose() {
      this.$refs.form.resetFields()
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.changeAddOrEdit = 'add'
      this.open = true;
      this.title = "添加设备检修";
    },

    /** 查看按钮操作 */
    handleShow(row) {
      this.changeAddOrEdit = "show";
      const repairId = row.repairId || this.ids
      getRepair(repairId).then(response => {
        this.form = response.data;
        this.lookOpen = true;
        this.title = "查看设备检修";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.changeAddOrEdit = 'edit'
      const repairId = row.repairId || this.ids
      getRepair(repairId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备检修";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.submitFormLoading) return
      this.submitFormLoading = true
      this.$refs["form"].validate(async valid => {
        if (valid) {
          if (this.form.repairId != null) {
            await updateRepair(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            await addRepair(this.form).then(response => {
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
      this.changeAddOrEdit = "delete";
      const repairIds = row.repairId || this.ids;
      this.$confirm('是否确认删除选中的数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRepair(repairIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出设备检修数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRepair(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
    }
  }
};
</script>
