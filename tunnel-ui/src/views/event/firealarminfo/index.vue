<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="报警名称" prop="alarmName">
        <el-input
          v-model="queryParams.alarmName"
          placeholder="请输入报警名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="隧道id" prop="tunnelId">
        <el-input
          v-model="queryParams.tunnelId"
          placeholder="请输入隧道id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="隧道名称">
      <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
        <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"/>
      </el-select>
      </el-form-item>
      <!-- <el-form-item label="方向" prop="direction">
        <el-input
          v-model="queryParams.direction"
          placeholder="请输入方向"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="车道" prop="vehicleLane">
        <el-input
          v-model="queryParams.vehicleLane"
          placeholder="请输入车道"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="报警方式" prop="alarmMode">
        <el-input
          v-model="queryParams.alarmMode"
          placeholder="请输入报警方式"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="报警类型" prop="alarmType">
        <el-select v-model="queryParams.alarmType" placeholder="请选择报警类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="报警状态" prop="alarmState">
        <el-select v-model="queryParams.alarmState" placeholder="请选择报警状态" clearable size="small">
          <el-option
            v-for="dict in alarmStateOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="报警设备" prop="alarmDevice">
        <el-input
          v-model="queryParams.alarmDevice"
          placeholder="请输入报警设备"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
     <!-- <el-form-item label="报警日期" prop="alarmDate">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.alarmDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择报警日期">
        </el-date-picker>
      </el-form-item> -->
      <!-- <el-form-item label="修改时间" prop="handleTime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.handleTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择修改时间">
        </el-date-picker>
      </el-form-item> -->
      <!-- <el-form-item label="处理人" prop="handler">
        <el-input
          v-model="queryParams.handler"
          placeholder="请输入处理人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:firealarminfo:add']"
        >新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:firealarminfo:edit']"
        >修改</el-button>
      </el-col> -->
     <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:firealarminfo:remove']"
        >删除</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:firealarminfo:export']"
        >导出</el-button>
      </el-col> -->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="firealarminfoList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="处理人" align="center" prop="id" /> -->
      <el-table-column label="报警名称" align="center" prop="alarmName" />
      <el-table-column label="隧道" align="center" prop="tunnels.tunnelName" />
      <el-table-column label="方向" align="center" prop="direction" />
      <el-table-column label="车道" align="center" prop="vehicleLane" />
      <el-table-column label="报警方式" align="center" prop="alarmMode" />
      <el-table-column label="报警类型" align="center" prop="alarmType" />
      <el-table-column label="报警设备" align="center" prop="alarmDevice" />
      <el-table-column label="报警日期" align="center" prop="alarmDate" width="180"/>
      <el-table-column label="报警状态" align="center" prop="alarmState" :formatter="alarmStateFormat" />
      <el-table-column label="处理人" align="center" prop="nickName" />
      <el-table-column label="处理时间" align="center" prop="handleTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding "  width="220">
        <template slot-scope="scope">
          <el-button
            v-show="scope.row.alarmState === 0"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row,1)"
            v-hasPermi="['business:firealarminfo:edit']"
          >已处理</el-button>
          <el-button
            v-show="scope.row.alarmState === 0"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row,2)"
            v-hasPermi="['business:firealarminfo:edit']"
          >已忽略</el-button>
         <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:firealarminfo:remove']"
          >删除</el-button> -->
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

    <!-- 添加或修改火警信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="报警名称" prop="alarmName">
          <el-input v-model="form.alarmName" placeholder="请输入报警名称" />
        </el-form-item>
        <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-input v-model="form.direction" placeholder="请输入方向" />
        </el-form-item>
        <el-form-item label="车道" prop="vehicleLane">
          <el-input v-model="form.vehicleLane" placeholder="请输入车道" />
        </el-form-item>
        <el-form-item label="报警方式///// 智能手动///自动报警" prop="alarmMode">
          <el-input v-model="form.alarmMode" placeholder="请输入报警方式" />
        </el-form-item>
        <el-form-item label="报警类型">
          <el-select v-model="form.alarmType" placeholder="请选择报警类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="报警状态">
          <el-select v-model="form.alarmState" placeholder="请选择报警状态">
            <el-option
              v-for="dict in alarmStateOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报警设备" prop="alarmDevice">
          <el-input v-model="form.alarmDevice" placeholder="请输入报警设备" />
        </el-form-item>
        <el-form-item label="报警日期" prop="alarmDate">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.alarmDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择报警日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="修改时间" prop="handleTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.handleTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择修改时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理人" prop="handler">
          <el-input v-model="form.handler" placeholder="请输入处理人" />
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
import { listFirealarminfo, getFirealarminfo, delFirealarminfo, addFirealarminfo, updateFirealarminfo } from "@/api/event/firealarminfo";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "Firealarminfo",
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
      // 火警信息表格数据
      firealarminfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 报警状态字典
      alarmStateOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmName: null,
        tunnelId: null,
        direction: null,
        vehicleLane: null,
        alarmMode: null,
        alarmType: null,
        alarmState: null,
        alarmDevice: null,
        alarmDate: null,
        handleTime: null,
        handler: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      tunnelData:{},
    };
  },
  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_firealarminfo_state").then(response => {
      this.alarmStateOptions = response.data;
    });
  },
  methods: {
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询火警信息列表 */
    getList() {
      this.loading = true;
      listFirealarminfo(this.queryParams).then(response => {
        this.firealarminfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 报警状态字典翻译
    alarmStateFormat(row, column) {
      return this.selectDictLabel(this.alarmStateOptions, row.alarmState);
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
        alarmName: null,
        tunnelId: null,
        direction: null,
        vehicleLane: null,
        alarmMode: null,
        alarmType: null,
        alarmState: null,
        alarmDevice: null,
        alarmDate: null,
        handleTime: null,
        handler: null
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
      this.title = "添加火警信息";
    },
    /** 已处理/已忽略按钮操作 */
    handleUpdate(row,code) {
       let confirmInfo = '';
       let successInfo = '';
       if(code == 1){
         confirmInfo = '是否确认处理！'
         successInfo = '处理成功'
       }else if(code == 2){
         confirmInfo = '是否确认忽略！'
         successInfo = '忽略成功'
       }
       this.$confirm(confirmInfo, "警告", {
           confirmButtonText: "确定",
           cancelButtonText: "取消",
           type: "warning"
         }).then(function() {
          // row.alarmState = code;
           return updateFirealarminfo({id:row.id,alarmState:code});
         }).then(() => {
           this.getList();
           this.$modal.msgSuccess(successInfo);
         }).catch(function() {});


      /* const id = row.id || this.ids
      getFirealarminfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改火警信息";
      }); */
    },
    /** 修改按钮操作 */
    /* handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFirealarminfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改火警信息";
      });
    }, */
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFirealarminfo(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addFirealarminfo(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
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
          return delFirealarminfo(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/firealarminfo/export', {
        ...this.queryParams
      }, `business_firealarminfo.xlsx`)
    }
  }
};
</script>
