<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true"  :rules="rules" v-show="showSearch" label-width="80px">
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in tunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="是否启用" prop="poll">
        <el-select v-model="queryParams.poll" placeholder="请选择是否启用" clearable size="small">
          <el-option
            v-for="dict in pollOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
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
          v-hasPermi="['system:tunnels:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tunnels:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tunnels:remove']"
        >删除
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="warning"-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleExport"-->
      <!--          v-hasPermi="['system:tunnels:export']"-->
      <!--        >导出</el-button>-->
      <!--      </el-col>-->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="tunnelsList" @selection-change="handleSelectionChange"
              max-height="610">
      <el-table-column type="selection" width="55" align="center"/>
      <!-- <el-table-column label="隧道ID" align="center" prop="tunnelId" /> -->
      <el-table-column label="隧道ID" align="center" prop="tunnelId"/>
      <el-table-column label="隧道名称" align="center" prop="tunnelName"/>
      <el-table-column label="隧道地址" align="center" prop="tunnelAddress"/>
      <el-table-column label="经度" align="center" prop="longitude"/>
      <el-table-column label="纬度" align="center" prop="latitude"/>
      <!-- <el-table-column label="隧道所ID" align="center" prop="tunnelStationId" />
      <el-table-column label="隧道所名称" align="center" prop="tunnelStationName" /> -->
      <el-table-column label="车道数量" align="center" prop="lane"/>
      <el-table-column label="隧道长度(米)" align="center" prop="tunnelLength" width="200"/>
      <el-table-column label="隧道开始桩号" align="center" prop="startPile" width="180"/>
      <el-table-column label="隧道结束桩号" align="center" prop="endPile" width="180"/>
      <el-table-column label="三维坐标" align="center" prop="coordinates"/>
      <el-table-column label="所属部门" align="center" prop="deptName"/>
      <!--      <el-table-column label="备注" align="center" prop="remake" />-->
      <el-table-column label="是否启用" align="center" prop="poll" :formatter="pollFormat"/>
      <!-- <el-table-column label="存储配置图的html" align="center" prop="storeConfigure" /> -->
      <!-- <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改者" align="center" prop="updateBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="tunnelConfig(scope.row)"
            v-hasPermi="['system:tunnels:edit']"
          >配置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tunnels:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tunnels:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改隧道对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道ID" prop="tunnelId">
              <el-input v-model="form.tunnelId" placeholder="请输入隧道ID" id="aaa" name="aaa" readonly="readonly"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隧道名称" prop="tunnelName">
              <el-input v-model="form.tunnelName" placeholder="请输入隧道名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道地址" prop="tunnelAddress">
              <el-input v-model="form.tunnelAddress" placeholder="请输入隧道地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!--      <el-form-item label="车道数量" prop="lane">
                    <el-input v-model="form.lane"  placeholder="请输入车道数量"  @keyup.native="number"/>
                  </el-form-item> -->
            <el-form-item label="车道数目" prop="lane">
              <el-select v-model="form.lane" placeholder="请选择车道数目">
                <el-option label="1车道" value="1"></el-option>
                <el-option label="2车道" value="2"></el-option>
                <el-option label="3车道" value="3"></el-option>
                <el-option label="4车道" value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道长度" prop="tunnelLength">
              <el-col :span="22">
                <el-input v-model="form.tunnelLength" placeholder="请输入隧道长度"/>
              </el-col>
              <el-col :span="2">
                <!-- <p>米</p> -->
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="三维坐标" prop="coordinates">
              <el-input v-model="form.coordinates" placeholder="请输入三维坐标"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始桩号" prop="startPile">
              <el-col :span="22">
                <el-input v-model="form.startPile" placeholder="请输入开始桩号"/>
              </el-col>
              <el-col :span="2">
                <!-- <p>米</p> -->
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束桩号" prop="endPile">
              <el-input v-model="form.endPile" placeholder="请输入结束桩号"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择所属部门" clearable size="small" style="width: 100%">
                <el-option v-for="item in deptsData" :key="item.deptId" :label="item.deptName"
                           :value="item.deptId"/>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="是否启用" prop="poll">
              <el-select v-model="form.poll" placeholder="请选择是否启用">
                <el-option
                  v-for="dict in pollOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remake">
              <el-input v-model="form.remake" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
          <!--    <el-col :span="12">
                <el-form-item label="存储配置图的html" prop="storeConfigure">
                  <el-input v-model="form.storeConfigure" placeholder="请输入存储配置图的html" />
                </el-form-item>
              </el-col> -->
          <!-- <el-form-item label="隧道所ID" prop="tunnelStationId">
            <el-input v-model="form.tunnelStationId" placeholder="请输入隧道所ID" />
          </el-form-item>
          <el-form-item label="隧道所名称" prop="tunnelStationName">
            <el-input v-model="form.tunnelStationName" placeholder="请输入隧道所名称" />
          </el-form-item> -->
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listTunnels, getTunnels, delTunnels, addTunnels, updateTunnels} from "@/api/equipment/tunnel/api.js";
import {listDept} from "@/api/system/dept";
import {getUserDeptId} from "@/api/system/user";

export default {
  name: "Tunnels",
  data() {
    const validateLongitude = (rule, value, callback) => {
      var longreg = /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/
      if (!longreg.test(value)) {
        callback(new Error('经度整数部分为0-180,小数部分为0到15位!'))
      }
      callback()
    }
    const validateLatitude = (rule, value, callback) => {
      var latreg = /^(\-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/
      if (!latreg.test(value)) {
        callback(new Error('纬度整数部分为0-90,小数部分为0到15位!'))
      }
      callback()
    }
    return {
      oper: "add",//add 添加  edit修改
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
      // 隧道表格数据
      tunnelsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否使用字典
      pollOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        poll: null,
        deptId: this.userDeptId,
        /* storeConfigure: null, */
      },
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      deptsData: {},
      userDeptId: null,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tunnelName: [{required: true, message: '请填写隧道名称', trigger: 'blur'}],
        startPile: [{required: true, message: '请填写隧道开始桩号', trigger: 'blur'}],
        endPile: [{required: true, message: '请填写隧道结束桩号', trigger: 'blur'}],
        tunnelAddress: [{required: true, message: '请填写隧道地址', trigger: 'blur'}],
        lane: [{required: true, message: '请选择车道数目', trigger: 'change'}],
        tunnelId: [{required: true, message: '请填写隧道ID', trigger: 'blur'}],
        deptId: [{required: true, message: '请选择所属部门', trigger: 'change'}],
        longitude: [
          {validator: validateLongitude, trigger: 'blur'},
          {validator: validateLongitude, trigger: 'change'}
        ],
        latitude: [
          {validator: validateLatitude, trigger: 'blur'},
          {validator: validateLatitude, trigger: 'change'}
        ],
        poll: [{required: true, message: '请选择是否可用', trigger: 'change'}],
      },
      // selectedTunnel:{},
      // 隧道列表
      tunnelData: [],
    };
  },
  created() {
    // this.getList();
    this.getTunnel()
    this.getDicts("sys_tunnel_use").then(response => {
      this.pollOptions = response.data;
    });
    this.getDepts();
    this.getUserDept();
  },
  mounted () {
      if (window.history && window.history.pushState) {
          // 向历史记录中插入了当前页
          history.pushState(null, null, document.URL);
          window.addEventListener('popstate', this.goBack, false);
      }
  },
  destroyed () {
      window.removeEventListener('popstate', this.goBack, false);
  },
  methods: {
    getTunnel() {
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
    },
    getDepts() {
      listDept().then((response) => {
        this.deptsData = response.data;
      });
    },
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        this.queryParams.deptId = response.rows[0].deptId;
        this.getList();
      })
    },
    number() {
      this.form.lane = this.form.lane.replace(/[^\.\d]/g, '');
      this.form.lane = this.form.lane.replace('.', '');
    },
    /* 进入配置界面*/
    tunnelConfig(row) {
      const tunnelId = row.tunnelId || this.ids
      const selectedTunnel = {
        id: row.tunnelId,
        name: row.tunnelName,
        lane: row.lane
      }
      this.$router.push({path: '/tunnelConfig', query: {"tunnel": selectedTunnel}})
    },
    /** 查询隧道列表 */
    getList() {
      this.loading = true;
      listTunnels(this.queryParams).then(response => {
        this.tunnelsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 是否使用字典翻译
    pollFormat(row, column) {
      return this.selectDictLabel(this.pollOptions, row.poll);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        tunnelId: null,
        tunnelName: null,
        /*      tunnelCode: null, */
        tunnelAddress: null,
        longitude: null,
        latitude: null,
        tunnelStationId: null,
        tunnelStationName: null,
        tunnelLength: null,
        lane: null,
        coordinates: null,
        remake: null,
        poll: null,
        storeConfigure: null,
        createBy: null,
        createTime: null,
        endPile: null,
        startPile: null,
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
      this.ids = selection.map(item => item.tunnelId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加隧道";
      this.$nextTick(() => {
        document.getElementById("aaa").removeAttribute("readOnly");
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.$nextTick(() => {
        document.getElementById("aaa").setAttribute("readOnly", true);
      });
      this.oper = "edit"
      const tunnelId = row.tunnelId || this.ids
      this.selectedTunnel = {
        id: row.tunnelId,
        name: row.tunnelName
      }
      getTunnels(tunnelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改隧道";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(!new RegExp('^[1-9][0-9]*$').test(this.form.startPile) || !new RegExp('^[1-9][0-9]*$').test(this.form.startPile) ){
            this.$modal.msgWarning("桩号要求输入的格式为整形");
            return;
          }
          if (this.oper == "edit") {
            updateTunnels(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.oper = "add"
                this.getList();
              }
            });
          } else {
            addTunnels(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.oper = "add"
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tunnelIds = row.tunnelId || this.ids;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delTunnels(tunnelIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tunnels/export', {
        ...this.queryParams
      }, `system_tunnels.xlsx`)
    }
  }
};
</script>
<style scoped>
.app-main {
  height: calc(100% - 105px) !important;
}
::v-deep .el-table::before {
  width: 0px !important;
}
</style>
