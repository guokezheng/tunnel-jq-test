<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="救援机构" prop="rescuePortal">
        <el-autocomplete
          size="small"
          v-model="queryParams.rescuePortal"
          placeholder="请输入应急救援机构"
          :fetch-suggestions="querySearchAsync"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="隧道" prop="tunnelId">
<!--        <el-input-->
<!--          v-model="queryParams.tunnelId"-->
<!--          placeholder="请输入隧道id"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
        <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in rescueTunnelData" :key="item.tunnelId" :label="item.tunnelName"
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
          v-hasPermi="['emergencyRescue:rescue:add']"
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
          v-hasPermi="['emergencyRescue:rescue:edit']"
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
          v-hasPermi="['emergencyRescue:rescue:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['emergencyRescue:rescue:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="multipleTable" v-loading="loading" :data="rescueList" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="应急救援编号" align="center" prop="id" />-->
      <el-table-column label="应急救援机构" align="center" prop="rescuePortal" />
      <el-table-column label="隧道" align="center" prop="tunnelName" />
      <el-table-column label="位置信息" align="center" prop="rescueLocation" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column label="救援服务信息" align="center" prop="serviceInformation" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['emergencyRescue:rescue:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['emergencyRescue:rescue:remove']"
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

    <!-- 添加或修改应急救援对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="救援机构" prop="rescuePortal">
          <el-input v-model="form.rescuePortal" placeholder="请输入应急救援机构" />
        </el-form-item>
        <el-form-item label="隧道" prop="tunnelId">
<!--          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />-->
          <el-select v-model="form.tunnelId" placeholder="请选择所属隧道" style="width: 100%;">
            <el-option v-for="item in rescueTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置信息" prop="rescueLocation">
          <el-input v-model="form.rescueLocation" placeholder="请输入位置信息" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="救援服务信息" prop="serviceInformation">
          <el-input type="textarea" :maxlength="250" v-model="form.serviceInformation" placeholder="请输入救援服务信息" />
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
import { listRescue, getRescue, delRescue, addRescue, updateRescue, exportRescue } from "@/api/intelligent/emergencyRescue/rescue";
import {
  listTunnels
} from "@/api/equipment/tunnel/api";

export default {
  name: "Rescue",
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
      // 应急救援表格数据
      rescueList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      /* 隧道 */
      rescueTunnelData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rescuePortal: null,
        tunnelId: null,
        rescueLocation: null,
        phone: null,
        serviceInformation: null,
        remake: null,
        remake1: null,
        remake2: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        rescuePortal: [{
          required: true,
          message: '请输入应急救援机构',
          trigger: 'blur'
        }, {
          min: 1,
          max: 30,
          message: '长度在1~30个字符',
          trigger: 'blur'
        }],
        tunnelId: [{
          required: true,
          message: '请选择隧道',
          trigger: 'change'
        }],
        rescueLocation: [{
          required: true,
          message: '请输入位置信息',
          trigger: 'blur'
        }, {
          min: 1,
          max: 30,
          message: '长度在1~30个字符',
          trigger: 'blur'
        }],
        phone: [{
          required: true,
          message: '请输入联系方式',
          trigger: 'blur'
        }, {
          pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
          message: '请输入规范的手机号',
          trigger: 'blur'
        }],
        serviceInformation: [{
          required: true,
          message: '请输入救援服务信息',
          trigger: 'blur'
        }],
      },
      // 模糊匹配-----------------------------------------start
      timeout: null, // 定时器
      restaurants: [], //数据
      // 模糊匹配-----------------------------------------end
    };
  },
  created() {
    this.getList();
    this.getTunnel();
  },
  methods: {
    // 模糊匹配------------------------------------------------------start
    // 获取数据
    loadAll() {
      var arr = []
      listRescue({pageNum: 1, pageSize: 10}).then(response => {
        response.rows.forEach(item => {
          arr.push({value: item.rescuePortal})
        })
      });
      return arr
    },

    // 模糊匹配
    querySearchAsync(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;

      cb(results);
    },
    createStateFilter(queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    // 模糊匹配------------------------------------------------------end
    /* 所选隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.rescueTunnelData = response.rows;
      });
    },
    /** 查询应急救援列表 */
    getList() {
      this.loading = true;
      listRescue(this.queryParams).then(response => {
        this.rescueList = response.rows;
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
        rescuePortal: null,
        tunnelId: null,
        rescueLocation: null,
        phone: null,
        serviceInformation: null,
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
    // 点击行，切换选中状态
    handleRowClick(row) {
      this.$refs.multipleTable.toggleRowSelection(row)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加应急救援信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRescue(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应急救援信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRescue(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRescue(this.form).then(response => {
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
      this.$confirm('是否确认删除应急救援?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRescue(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出应急救援数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRescue(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  },
  mounted() {
    this.restaurants = this.loadAll()
  },
};
</script>
