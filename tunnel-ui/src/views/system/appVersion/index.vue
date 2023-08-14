<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          v-hasPermi="['config:config:add']"
          size="small"
          @click="handleAdd()"
        >新增
        </el-button>
<!--        <el-button-->
<!--          size="small"-->
<!--          :loading="exportLoading"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['config:config:export']"-->
<!--        >导出-->
<!--        </el-button>-->
        <el-button size="small" @click="resetQuery">刷新 </el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入AppId、应用名称、版本号、版本名称，回车搜索"
            v-model="queryParams.searchValue"
            @keyup.enter.native="handleQuery"
            size="small"
            style="border-right: solid 1px #00c8ff; border-radius: 3px"
          >
          </el-input>
        </div>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="appVersionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="AppId" align="center" prop="appId" />
      <el-table-column label="应用名称" align="center" prop="appName" />
      <el-table-column label="版本号（从大到小）" align="center" prop="editionNumber" />
      <el-table-column label="版本名称" align="center" prop="editionName" />
      <el-table-column label="系统类型" align="center" prop="sysType" >
        <template slot-scope="scope">
          <span>{{ scope.row.sysType == 1 ? 'android' : 'ios' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="安装包类型" align="center" prop="packageType" >
        <template slot-scope="scope">
          <span>{{ scope.row.packageType == 1 ? 'wgt热更新' : '整包更新' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否发行" align="center" prop="editionIssue" >
        <template slot-scope="scope">
          <span>{{ scope.row.editionIssue == 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="静默更新" align="center" prop="editionSilence" >
        <template slot-scope="scope">
          <span>{{ scope.row.editionSilence == 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="强制更新" align="center" prop="editionForce" >
        <template slot-scope="scope">
          <span>{{ scope.row.editionForce == 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:appVersion:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:appVersion:remove']"
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

    <!-- 添加或修改app版本管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="AppId" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入AppId" />
        </el-form-item>
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="form.appName" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="版本号" prop="editionNumber">
          <el-input v-model="form.editionNumber" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="版本名称" prop="editionName">
          <el-input v-model="form.editionName" placeholder="请输入版本名称" />
        </el-form-item>
        <el-form-item label="系统类型">
          <el-radio-group v-model="form.sysType">
            <el-radio label="1">android</el-radio>
            <el-radio label="2">ios</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="安装包类型">
          <el-radio-group v-model="form.packageType">
            <el-radio label="1">wgt热更新</el-radio>
            <el-radio label="0">整包更新</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否发行">
          <el-radio-group v-model="form.editionIssue">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="静默更新">
          <el-radio-group v-model="form.editionSilence">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>

          </el-radio-group>
        </el-form-item>
        <el-form-item label="强制更新">
          <el-radio-group v-model="form.editionForce">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="下载地址" prop="editionUrl">
          <el-input v-model="form.editionUrl" placeholder="请输入下载地址" />
        </el-form-item>
        <el-form-item label="更新内容" prop="describe">
          <el-input v-model="form.describe" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAppVersion, getAppVersion, delAppVersion, addAppVersion, updateAppVersion, exportAppVersion } from "@/api/system/appVersion";

export default {
  name: "AppVersion",
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
      // app版本管理表格数据
      appVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        appName: null,
        editionNumber: null,
        editionName: null,
      },
      // 表单参数
      form: {

      },
      // 表单校验
      rules: {
        appId: [
          { required: true, message: "AppId不能为空", trigger: "blur" }
        ],
        appName: [
          { required: true, message: "应用名称不能为空", trigger: "blur" }
        ],
        editionNumber: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ],
        editionName: [
          { required: true, message: "版本名称不能为空", trigger: "blur" }
        ],
        sysType: [
          { required: true, message: "系统类型不能为空", trigger: "blur" }
        ],
        packageType: [
          { required: true, message: "安装包类型不能为空", trigger: "blur" }
        ],
        editionIssue: [
          { required: true, message: "是否发行不能为空", trigger: "blur" }
        ],
        editionSilence: [
          { required: true, message: "静默更新不能为空", trigger: "blur" }
        ],
        editionForce: [
          { required: true, message: "强制更新不能为空", trigger: "blur" }
        ],
        editionUrl: [
          { required: true, message: "下载地址不能为空", trigger: "blur" }
        ],
        describe: [
          { required: true, message: "更新内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询app版本管理列表 */
    getList() {
      this.loading = true;
      listAppVersion(this.queryParams).then(response => {
        this.appVersionList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        appId: null,
        appName: null,
        editionNumber: null,
        editionName: null,
        sysType: 0,
        packageType: 0,
        editionIssue: 0,
        editionSilence: 0,
        editionForce: 0,
        editionUrl: null,
        describe: null
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
      this.title = "添加app版本管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAppVersion(id).then(response => {

        var obj = response.data;
        obj.editionForce = String(obj.editionForce);
        obj.editionIssue = String(obj.editionIssue);
        obj.editionSilence = String(obj.editionSilence);
        obj.sysType = String(obj.sysType);
        obj.packageType = String(obj.packageType);
        this.form = obj;
        // this.from.editionForce = this.from.editionForce.toString();
        // this.from.editionForce = this.from.editionForce.toString();
        // this.from.editionForce = this.from.editionForce.toString();
        // this.from.editionForce = this.from.editionForce.toString();
        // this.from.editionForce = this.from.editionForce.toString();
        this.open = true;
        this.title = "修改app版本管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppVersion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppVersion(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除app版本管理编号为"' + ids + '"的数据项？').then(function() {
        return delAppVersion(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有app版本管理数据项？').then(() => {
        this.exportLoading = true;
        return exportAppVersion(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
