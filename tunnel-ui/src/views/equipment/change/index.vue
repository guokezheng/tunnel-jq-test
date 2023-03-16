<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row  :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          type="primary"
          plain
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:change:add']"
          >新增</el-button
        >
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:change:edit']"-->
<!--          >修改</el-button-->
<!--        >-->
        <el-button
          type="primary"
          plain
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:change:remove']"
          >删除</el-button
        >
        <el-button
          type="primary"
          plain
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:change:export']"
          >导出</el-button>
          <el-button size="small" @click="resetQuery" type="primary" plain
          >刷新</el-button
          >
        <!--          <el-button-->
        <!--            type="info"-->
        <!--            icon="el-icon-s-help"-->
        <!--            size="mini"-->
        <!--            @click="checkInstruction"-->
        <!--            >校验指令</el-button>-->
      </el-col>
      <el-col :span="6" :offset="12">
        <div  ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.deviceName"
            placeholder="请输入设备名称、设备编号，回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="icon-gym-Gsearch"
              @click="boxShow = !boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div ref="cc" class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >

          <el-form-item style="width: 100%;" label="更换时间">
            <el-date-picker
              v-model="daterangeChangeTime"
              size="small"
              style="width: 320px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        <el-form-item class="bottomBox" align="center">
          <el-button size="small" type="primary" @click="handleQuery"
          >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
          >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr" ></div>
    <el-table
      v-loading="loading"
      :data="changeList"
      @selection-change="handleSelectionChange"
      :default-sort = "{prop: 'changeTime', order: 'descending'}"
      height="62vh"
      class="allTable"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <!-- <el-table-column label="更换时间" align="center" prop="id" /> -->
      <el-table-column label="设备编号" align="center" prop="deviceId" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column
        label="更换时间"
        align="center"
        prop="changeTime"
        width="180"
        sortable
      >
        <!-- <template slot-scope="scope">
          <span>{{ parseTime(scope.row.changeTime, "{y}-{m}-{d}") }}</span>
        </template> -->
      </el-table-column>
      <el-table-column label="桩号" align="center" prop="stakeMark" />
      <el-table-column label="方向" align="center" prop="eqDirection" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:change:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:change:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备变更对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编号" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="更换时间" prop="changeTime">
          <!-- <el-date-picker
            clearable
            size="small"
            v-model="form.changeTime"
            type="date"
            value-format="yyyy-MM-dd hh:mm:ss"
            placeholder="选择更换时间"
          >
          </el-date-picker> -->
          <el-date-picker
            clearable
            v-model="form.changeTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择更换时间"
            style="width:100%"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="桩号" prop="stakeMark">
          <el-input v-model="form.stakeMark" placeholder="请输入桩号" />
        </el-form-item>
        <el-form-item label="方向" prop="eqDirection">
          <el-select v-model="form.eqDirection" placeholder="请选择方向" style="width:100%">
            <el-option label="上行" value="0" />
            <el-option label="下行" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import {
  listChange,
  getChange,
  delChange,
  addChange,
  updateChange,
  exportChange,
} from "@/api/equipment/change/change";

export default {
  name: "Change",
  data() {
    return {
      boxShow: false,
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
      // 设备变更表格数据
      changeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeChangeTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        deviceName: null,
        changeTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceName: [{required: true, message: '请填写设备名称', trigger: 'blur'}],
        deviceId: [{required: true, message: '请填写设备ID', trigger: 'blur'}],
        changeTime: [{required: true, message: '请填写更换时间', trigger: 'blur'}],
        eqDirection: [{required: true, message: '请选择方向', trigger: 'blur'}],
        stakeMark: [{required: true, message: '请填写桩号信息', trigger: 'blur'}],
      },
    };
  },
  created() {
    this.getList();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
    /** 查询设备变更列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      this.queryParams.params = {};
      if (null != this.daterangeChangeTime && "" != this.daterangeChangeTime) {
        this.queryParams.params["beginChangeTime"] =
          this.daterangeChangeTime[0];
        this.queryParams.params["endChangeTime"] = this.daterangeChangeTime[1];
      }
      listChange(this.queryParams).then((response) => {
        console.log(response,"查询设备变更列表")
        this.changeList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.changeList.forEach((e) => {
          if (e.eqDirection == 0) {
            e.eqDirection = "上行";
          } else if (e.eqDirection == 1) {
            e.eqDirection = "下行";
          }
        });
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
        deviceId: null,
        deviceName: null,
        changeTime: null,
        eqDirection: null,
        stakeMark: null,
        remark: null,
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
      this.queryParams.deviceName='';
      this.daterangeChangeTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备变更";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getChange(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备变更";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateChange(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addChange(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除选中数据项？')
        .then(function () {
          return delChange(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出设备变更数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportChange(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>

