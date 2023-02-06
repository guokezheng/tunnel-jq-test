<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发言人" prop="broadcastSpokesman">
        <el-input
          v-model="queryParams.broadcastSpokesman"
          placeholder="请输入发言人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="广播次数" prop="numberOfBroadcasts">
        <el-input
          v-model="queryParams.numberOfBroadcasts"
          placeholder="请输入广播次数"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" plain size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange" class="allTable">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
<!--      <el-table-column label="id" align="center" prop="id" />-->
<!--      <el-table-column label="广播设备" align="center" prop="broadcastEqids" />-->
      <el-table-column label="广播设备" align="center" prop="broadcastEqnames" />
      <el-table-column label="发布结果" align="center" prop="publishResults" />
      <el-table-column label="广播内容" align="center" prop="broadcastContent" />
      <el-table-column label="发言人" align="center" prop="broadcastSpokesman" />
      <el-table-column label="语速" align="center" prop="broadcastSpeed" />
      <el-table-column label="是否保存录音" align="center" prop="isSaveRecording" />
      <el-table-column label="音量(dB)" align="center" prop="volume" />
      <el-table-column label="广播次数" align="center" prop="numberOfBroadcasts" />
      <el-table-column label="录音地址" align="center" prop="recordingAddress" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
<!--      <el-table-column label="备注" align="center" prop="remake" />-->
<!--      <el-table-column label="备注1" align="center" prop="remake1" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['broadcastRecord:record:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['broadcastRecord:record:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改广播记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广播设备" prop="broadcastEqids">
          <el-input v-model="form.broadcastEqids" placeholder="请输入广播设备" />
        </el-form-item>
        <el-form-item label="发布结果" prop="publishResults">
          <el-input v-model="form.publishResults" placeholder="请输入发布结果" />
        </el-form-item>
        <el-form-item label="广播内容">
          <editor v-model="form.broadcastContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="发言人" prop="broadcastSpokesman">
          <el-input v-model="form.broadcastSpokesman" placeholder="请输入发言人" />
        </el-form-item>
        <el-form-item label="语速" prop="broadcastSpeed">
          <el-input v-model="form.broadcastSpeed" placeholder="请输入语速" />
        </el-form-item>
        <el-form-item label="是否保存录音" prop="isSaveRecording">
          <el-input v-model="form.isSaveRecording" placeholder="请输入是否保存录音" />
        </el-form-item>
        <el-form-item label="音量(dB)" prop="volume">
          <el-input v-model="form.volume" placeholder="请输入音量" />
        </el-form-item>
        <el-form-item label="广播次数" prop="numberOfBroadcasts">
          <el-input v-model="form.numberOfBroadcasts" placeholder="请输入广播次数" />
        </el-form-item>
        <el-form-item label="录音地址" prop="recordingAddress">
          <el-input v-model="form.recordingAddress" placeholder="请输入录音地址" />
        </el-form-item>
<!--        <el-form-item label="备注" prop="remake">-->
<!--          <el-input v-model="form.remake" placeholder="请输入备注" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="备注1" prop="remake1">-->
<!--          <el-input v-model="form.remake1" placeholder="请输入备注1" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord, exportRecord } from "@/api/intelligent/trafficBroadcasting/broadcastRecord/record/record";
import Editor from '@/components/Editor';

export default {
  name: "Record",
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
      // 广播记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        broadcastEqids: null,
        publishResults: null,
        broadcastContent: null,
        broadcastSpokesman: null,
        broadcastSpeed: null,
        isSaveRecording: null,
        volume: null,
        numberOfBroadcasts: null,
        recordingAddress: null,
        remake: null,
        remake1: null
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
  },
  methods: {
    /** 查询广播记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
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
        broadcastEqids: null,
        createTime: null,
        publishResults: null,
        broadcastContent: null,
        broadcastSpokesman: null,
        broadcastSpeed: null,
        isSaveRecording: null,
        volume: null,
        numberOfBroadcasts: null,
        recordingAddress: null,
        remake: null,
        remake1: null
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
      this.title = "添加广播记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改广播记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
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
      this.$confirm('是否确认删除广播记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRecord(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有广播记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRecord(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
