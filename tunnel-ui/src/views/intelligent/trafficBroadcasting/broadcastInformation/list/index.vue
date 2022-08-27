<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广播名称" prop="broadcastName">
        <el-input
          v-model="queryParams.broadcastName"
          placeholder="请输入广播名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属隧道" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in broadcastInformationListTunnelData" :key="item.tunnelId" :label="item.tunnelName"
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
          v-hasPermi="['broadcastInformation:list:add']"
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
          v-hasPermi="['broadcastInformation:list:edit']"
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
          v-hasPermi="['broadcastInformation:list:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="listList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="广播名称" align="center" prop="broadcastName" />
      <el-table-column label="广播位置" align="center" prop="broadcastLocation" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="厂家" align="center" prop="manufactor" />
      <el-table-column label="最大音量(dB)" align="center" prop="maximumVolume" />
      <el-table-column label="最大广播次数" align="center" prop="maximumNumberOfBroadcasts" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['broadcastInformation:list:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['broadcastInformation:list:remove']"
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

    <!-- 添加或修改广播信息列对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="广播名称" prop="broadcastName">
          <el-input v-model="form.broadcastName" placeholder="请输入广播名称" />
        </el-form-item>
        <el-form-item label="广播位置" prop="broadcastLocation">
          <el-input v-model="form.broadcastLocation" placeholder="请输入广播位置" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="tunnelId">
<!--          <el-input v-model="form.tunnelId" placeholder="请输入所属隧道" />-->
          <el-select v-model="form.tunnelId" placeholder="请选择所属隧道" style="width: 100%;">
            <el-option v-for="item in broadcastInformationListTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="厂家" prop="manufactor">
          <el-input v-model="form.manufactor" placeholder="请输入厂家" />
        </el-form-item>
        <el-form-item label="最大音量(dB)" prop="maximumVolume">
          <el-input v-model="form.maximumVolume" placeholder="请输入最大音量" />
        </el-form-item>
        <el-form-item label="最大广播次数" prop="maximumNumberOfBroadcasts">
          <el-input v-model="form.maximumNumberOfBroadcasts" placeholder="请输入最大广播次数" />
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
import { listList, getList, delList, addList, updateList, exportList } from "@/api/intelligent/trafficBroadcasting/broadcastInformation/list/list";
import {
  listTunnels
} from "@/api/equipment/tunnel/api";

export default {
  name: "List",
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
      // 广播信息列表格数据
      listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      /* 隧道 */
      broadcastInformationListTunnelData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        broadcastName: null,
        broadcastLocation: null,
        tunnelId: null,
        manufactor: null,
        maximumVolume: null,
        maximumNumberOfBroadcasts: null,
        remake: null,
        remake1: null,
        remake2: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        broadcastName: [{
          required: true,
          message: '请输入广播名称',
          trigger: 'blur'
        },{
          min: 1,
          max: 30,
          message: '长度在1~30个字符',
          trigger: 'blur'
        }],
        broadcastLocation: [{
          required: true,
          message: '请输入广播位置',
          trigger: 'blur'
        },{
          min: 1,
          max: 30,
          message: '长度在1~30个字符',
          trigger: 'blur'
        }],
        tunnelId: [{
          required: true,
          message: '请选择所属隧道',
          trigger: 'change'
        }],
        manufactor: [{
          required: true,
          message: '请输入厂家',
          trigger: 'blur'
        },{
          min: 1,
          max: 30,
          message: '长度在1~30个字符',
          trigger: 'blur'
        }],
        maximumVolume: [{
          required: true,
          message: '请输入最大音量',
          trigger: 'blur'
        },{
          pattern: /^(?:[1-9]?\d|100)$/,
          message: '请输入数字(区间为0~100)'
        }],
        maximumNumberOfBroadcasts: [{
          required: true,
          message: '请输入最大广播次数',
          trigger: 'blur'
        },{
          pattern: /^(?:[1-9]?\d|100)$/,
          message: '请输入数字(区间为0~100)'
        }],
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
  },
  methods: {
    /* 所选隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.broadcastInformationListTunnelData = response.rows;
      });
    },
    /** 查询广播信息列列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
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
        broadcastName: null,
        broadcastLocation: null,
        tunnelId: null,
        manufactor: null,
        maximumVolume: null,
        maximumNumberOfBroadcasts: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
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
      this.title = "添加广播信息列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改广播信息列表";
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除广播信息?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delList(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有广播信息?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportList(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
