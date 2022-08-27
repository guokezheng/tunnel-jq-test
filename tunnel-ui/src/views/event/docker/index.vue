<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="显卡编号" prop="deviceID">
        <el-input
          v-model="queryParams.deviceID"
          placeholder="请输入显卡编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="端口号" prop="dockerPort">
        <el-input
          v-model="queryParams.dockerPort"
          placeholder="请输入端口号"
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
          v-hasPermi="['system:docker:add']"
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
          v-hasPermi="['system:docker:edit']"
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
          v-hasPermi="['system:docker:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:docker:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dockerList" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="容器ID" align="center" prop="id" />
      <el-table-column label="显卡编号" align="center" prop="deviceID" />
      <el-table-column label="IP地址" align="center" prop="dockerIp" />
      <el-table-column label="端口号" align="center" prop="dockerPort" />
      <el-table-column label="容器名称" align="center" prop="dockerName" />
      <el-table-column label="cpu核个数" align="center" prop="vcpus" />
      <el-table-column label="内存空间大小" align="center" prop="mems" />
      <!--<el-table-column label="显卡的编号" align="center" prop="nGpuId" />
      <el-table-column label="是否检测人脸" align="center" prop="nFaceEnable" />
      <el-table-column label="是否检测人体" align="center" prop="nBodyEnable" /> -->
     <!-- <el-table-column label="是否检测机动车" align="center" prop="nMotorEnable" >
        <template slot-scope="scope">
            <span style="margin-left: 10px">{{eventCheckFormat(scope.row.nMotorEnable)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否检测非机动车" align="center" prop="nNonMotorEnable" >
        <template slot-scope="scope">
            <span style="margin-left: 10px">{{eventCheckFormat(scope.row.nNonMotorEnable)}}</span>
        </template>
       </el-table-column> -->
      <el-table-column label="是否检测事件" align="center" prop="nEventEnable" >
        <template slot-scope="scope">
            <span style="margin-left: 10px">{{eventCheckFormat(scope.row.nEventEnable)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="事件模式" align="center" prop="nEventMode" />
      <el-table-column label="授权文件路径" align="center" prop="authPath" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="dockerInit(scope.row)"
          >初始化</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:docker:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:docker:remove']"
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

    <!-- 添加或修改容器表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-input v-model="form.nGpuId" type="hidden"/>
        <el-form-item label="显卡编号" prop="deviceID">
          <el-input v-model="form.deviceID" placeholder="请输入显卡编号" />
        </el-form-item>
        <el-form-item label="服务器IP" prop="deviceID">
          <el-input v-model="form.dockerIp" placeholder="请输入服务器IP" />
        </el-form-item>
        <el-form-item label="端口号" prop="dockerPort">
          <el-input v-model="form.dockerPort" placeholder="请输入端口号" />
        </el-form-item>
        <el-form-item label="容器名称" prop="dockerName">
          <el-input v-model="form.dockerName" placeholder="请输入容器名称" />
        </el-form-item>
        <el-form-item label="cpu核个数" prop="vcpus">
          <el-input v-model="form.vcpus" placeholder="请输入容器所占用的cpu核个数" />
        </el-form-item>
        <el-form-item label="内存空间大小" prop="mems">
          <el-input v-model="form.mems" placeholder="请输入容器所占用的内存空间大小" />
        </el-form-item>
<!--        <el-form-item label="显卡的编号" prop="nGpuId">
          <el-input v-model="form.nGpuId" placeholder="请输入显卡或者芯片的编号" />
        </el-form-item> -->
      <!--  <el-form-item label="是否检测人脸" prop="nFaceEnable">
          <el-input v-model="form.nFaceEnable" placeholder="请输入是否检测人脸：1表示检测，0表示不检测" />
        </el-form-item>
        <el-form-item label="是否检测人体" prop="nBodyEnable">
          <el-input v-model="form.nBodyEnable" placeholder="请输入是否检测人体：1表示检测，0表示不检测" />
        </el-form-item> -->
      <!--  <el-form-item label="是否检测机动车" prop="nMotorEnable">
          <el-radio-group v-model="form.nMotorEnable">
            <el-radio
              v-for="dict in eventCheckOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否检测非机动车" prop="nNonMotorEnable">
          <el-radio-group v-model="form.nNonMotorEnable">
            <el-radio
              v-for="dict in eventCheckOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="是否检测事件" prop="nEventEnable">
          <el-radio-group v-model="form.nEventEnable">
            <el-radio
              v-for="dict in eventCheckOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="事件模式" prop="nEventMode">
          <el-input v-model="form.nEventMode" placeholder="监管行为0，交通事件105,安全生产103" />
        </el-form-item>
        <el-form-item label="授权文件路径" prop="authPath">
          <el-input v-model="form.authPath" placeholder="保护授权码，需要/home目录下的子目录下" />
        </el-form-item>
        <el-form-item label="用户名" prop="serverName">
          <el-input v-model="form.serverName" placeholder="服务器用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="serverPwd">
          <el-input v-model="form.serverPwd" type="password" placeholder="服务器密码" />
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
import { listDocker, getDocker, delDocker, addDocker, updateDocker, exportDocker ,dockerInit} from "@/api/event/docker";

export default {
  name: "Docker",
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
      // 容器表表格数据
      dockerList: [],
      eventCheckOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceID: null,
        dockerPort: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceID:[{required: true, message: '显卡编号', trigger: 'blur'}],
        dockerIp:[{required: true, message: '服务器IP', trigger: 'blur'},
                                { pattern: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
        , message: '请输入正确的IP' }],
        dockerPort:[{required: true, message: '端口号', trigger: 'blur'}],
        dockerName:[{required: true, message: '容器名称', trigger: 'blur'}],
        vcpus:[{required: true, message: 'CPU核个数', trigger: 'blur'}],
        mems:[{required: true, message: '内存空间大小', trigger: 'blur'}],
        /* nGpuId:[{required: true, message: '显卡编号', trigger: 'blur'}], */
        nEventEnable:[{required: true, message: '是否检测事件', trigger: 'blur'}],
        nEventMode:[{required: true, message: '事件模式', trigger: 'blur'}],
        authPath:[{required: true, message: '授权文件路径', trigger: 'blur'}],
        serverName:[{required: true, message: '服务器用户名', trigger: 'blur'}],
        serverPwd:[{required: true, message: '服务器密码', trigger: 'blur'}],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sd_event_check").then(response => {
      this.eventCheckOptions = response.data;
    });
  },
  methods: {
    /** 查询容器表列表 */
    getList() {
      this.loading = true;
      listDocker(this.queryParams).then(response => {
        this.dockerList = response.rows;
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
        deviceID: 0,
        dockerIp: null,
        dockerPort: null,
        dockerName: null,
        vcpus: 8,
        mems: 16000,
        nGpuId: 0,
      /*  nFaceEnable: 0,
        nBodyEnable: 0,
        nMotorEnable: "0",
        nNonMotorEnable: "0", */
        nEventEnable: "1",
        nEventMode: 105,
        authPath: '/home/License.dat',
        create_by: null,
        create_time: null,
        update_by: null,
        update_time: null
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
      this.title = "添加容器表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDocker(id).then(response => {
        this.form = response.data;
        this.form.nMotorEnable = response.data.nMotorEnable.toString();
        this.form.nNonMotorEnable = response.data.nNonMotorEnable.toString();
        this.form.nEventEnable = response.data.nEventEnable.toString();
        this.open = true;
        this.title = "修改容器表";
      });
    },
    /** 初始化容器 */
    dockerInit(row) {
      this.reset();
      const id = row.id || this.ids
      dockerInit(id).then(response => {
        if(response.code==200){
           this.$modal.msgSuccess("初始化成功");
        }else{
          this.$modal.msgSuccess("初始化失败");
        }

      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDocker(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDocker(this.form).then(response => {
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
      this.$confirm('是否确认删除选中数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDocker(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有容器表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDocker(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    // 级别 字典翻译
    eventCheckFormat(cell) {
      return this.selectDictLabel(this.eventCheckOptions, cell);
    },
  }
};
</script>
