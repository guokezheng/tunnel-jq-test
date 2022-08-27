<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广播内容" prop="broadcastContent">
        <el-input
          v-model="queryParams.broadcastContent"
          placeholder="请输入广播内容"
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
          v-hasPermi="['broadcastTemplate:template:add']"
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
          v-hasPermi="['broadcastTemplate:template:edit']"
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
          v-hasPermi="['broadcastTemplate:template:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['broadcastTemplate:template:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="广播内容" align="center" prop="broadcastContent"
                        class="cell el-tooltip" :show-overflow-tooltip="true" />
      <el-table-column label="发言人" align="center" prop="broadcastSpokesman" width="165" />
      <el-table-column label="语速" align="center" prop="broadcastSpeed" width="165" />
      <el-table-column label="是否保存录音" align="center" prop="isSaveRecording" width="165" />
      <el-table-column label="音量(dB)" align="center" prop="volume" width="165" />
      <el-table-column label="广播次数" align="center" prop="numberOfBroadcasts" width="165" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['broadcastTemplate:template:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['broadcastTemplate:template:remove']"
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

    <!-- 添加或修改广播模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="广播内容" prop="broadcastContent">
<!--          <editor v-model="form.broadcastContent" :min-height="192"/>-->
          <el-input type="textarea" :maxlength="250" v-model="form.broadcastContent"></el-input>
        </el-form-item>
        <el-form-item label="发言人" prop="broadcastSpokesman">
<!--          <el-input v-model="form.broadcastSpokesman" placeholder="请输入发言人" />-->
          <el-select v-model="form.broadcastSpokesman" filterable placeholder="请选择发言人" style="width: 100%;">
            <el-option v-for="item in fayanoptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="语速" prop="broadcastSpeed">
<!--          <el-input v-model="form.broadcastSpeed" placeholder="请输入语速" />-->
          <el-select v-model="form.broadcastSpeed" filterable placeholder="请选择语速" style="width: 100%;">
            <el-option v-for="item in yusuoptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否保存录音" prop="isSaveRecording">
<!--          <el-input v-model="form.isSaveRecording" placeholder="请输入是否保存录音" />-->
          <el-select v-model="form.isSaveRecording" filterable placeholder="请选择是否保存录音" style="width: 100%;">
            <el-option v-for="item in luyinoptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="广播次数" prop="numberOfBroadcasts">
          <el-input v-model="form.numberOfBroadcasts" placeholder="请输入广播次数" />
        </el-form-item>
        <el-form-item label="音量(dB)" prop="volume">
          <el-input v-model="form.volume" placeholder="请输入音量" />
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
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate, exportTemplate }
from "@/api/intelligent/trafficBroadcasting/broadcastTemplate/template/template";
import Editor from '@/components/Editor';

export default {
  name: "Template",
  components: {
    Editor,
  },
  data() {
    return {
      fayanoptions: [
        {
          value: "汤姆",
          label: "汤姆",
        },
        {
          value: "大卫",
          label: "大卫",
        },
      ],
      yusuoptions: [
        {
          value: "10",
          label: "10快音速",
        },
        {
          value: "9",
          label: "9快音速",
        },
        {
          value: "8",
          label: "8快音速",
        },
        {
          value: "7",
          label: "7快音速",
        },
        {
          value: "6",
          label: "6快音速",
        },
        {
          value: "5",
          label: "5正常音速",
        },
        {
          value: "4",
          label: "4慢音速",
        },
        {
          value: "3",
          label: "3慢音速",
        },
        {
          value: "2",
          label: "2慢音速",
        },
        {
          value: "1",
          label: "1慢音速",
        },
      ],

      luyinoptions: [
        {
          value: "1",
          label: "不保存",
        },
        {
          value: "0",
          label: "保存",
        },
      ],

      bofangoptions: [
        {
          value: "5",
          label: "5",
        },
        {
          value: "4",
          label: "4",
        },
      ],

      yinliangoptions: [
        {
          value: "0",
          label: "0默认",
        },
        {
          value: "1",
          label: "音量1",
        },
      ],
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
      // 广播模板表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        broadcastContent: null,
        broadcastSpokesman: null,
        broadcastSpeed: null,
        isSaveRecording: null,
        volume: null,
        numberOfBroadcasts: null,
        remake: null,
        remake1: null,
        remake2: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        broadcastContent: [{
          required: true,
          message: '请输入广播内容',
          trigger: 'blur'
        }],
        broadcastSpokesman: [{
          required: true,
          message: '请选择发言人',
          trigger: 'change'
        }],
        broadcastSpeed: [{
          required: true,
          message: '请选择语速',
          trigger: 'change'
        }],
        isSaveRecording: [{
          required: true,
          message: '请选择是否保存录音',
          trigger: 'change'
        }],
        volume: [{
          required: true,
          message: '请输入音量',
          trigger: 'blur'
        },
          {
            pattern: /^(?:[1-9]?\d|100)$/,
            message: '请输入数字(区间为0~100)'
          }],
        numberOfBroadcasts: [{
          required: true,
          message: '请输入广播次数',
          trigger: 'blur'
        },
          {
            pattern: /^(?:[1-9]?\d|100)$/,
            message: '请输入数字(区间为0~100)'
          }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询广播模板列表 */
    getList() {
      this.loading = true;
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows;
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
        broadcastContent: null,
        broadcastSpokesman: null,
        broadcastSpeed: null,
        isSaveRecording: null,
        volume: null,
        numberOfBroadcasts: null,
        createTime: null,
        updateTime: null,
        remake: null,
        remake1: null,
        remake2: null
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
      this.title = "添加广播模板";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTemplate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改广播模板";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTemplate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTemplate(this.form).then(response => {
              this.open = false;
              this.getList();
              this.$modal.msgSuccess("新增成功");
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除广播模板?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTemplate(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有广播模板数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTemplate(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
