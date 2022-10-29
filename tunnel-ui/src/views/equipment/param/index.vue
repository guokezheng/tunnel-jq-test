<template>
  <div class="app-container deviceIcon">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备id" prop="equipmentId">
        <el-input
          v-model="queryParams.equipmentId"
          placeholder="请输入设备id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['inductionlamp:statusParam:add']"
        >新增</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['inductionlamp:statusParam:edit']"
        >修改</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['inductionlamp:statusParam:remove']"
        >删除</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['inductionlamp:statusParam:add']"
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
          v-hasPermi="['inductionlamp:statusParam:edit']"
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
          v-hasPermi="['inductionlamp:statusParam:remove']"
        >删除</el-button>
      </el-col>
     <el-col :span="1.5">
       <el-button
         type="warning"
         plain
         icon="el-icon-download"
         size="mini"
         :loading="exportLoading"
         @click="handleExport"
         v-hasPermi="['inductionlamp:statusParam:export']"
       >导出</el-button>
     </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="paramList" @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName" max-height="760"
        >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备id" align="center" prop="equipmentId" :show-overflow-tooltip="true"/>
      <el-table-column label="模式名称" align="center" prop="modeName" />
      <el-table-column label="模式编号" align="center" prop="modeCode" />
      <el-table-column label="光照开始值" align="center" prop="illuminationStart" />
      <el-table-column label="光照开始值关系符号" align="center" prop="illuminationStartSymbol" />
      <el-table-column label="光照结束值" align="center" prop="illuminationEnd" />
      <el-table-column label="光照结束值关系符号" align="center" prop="illuminationEndSymbol" />
      <el-table-column label="湿度开始值" align="center" prop="humidityStart" />
      <el-table-column label="湿度开始值关系符号" align="center" prop="humidityStartSymbol" />
      <el-table-column label="湿度结束值" align="center" prop="humidityEnd" />
      <el-table-column label="湿度结束值关系符号" align="center" prop="humidityEndSymbol" />
      <el-table-column label="温度开始值" align="center" prop="temperatureStart" />
      <el-table-column label="温度开始值关系符号" align="center" prop="temperatureStartSymbol" />
      <el-table-column label="温度结束值" align="center" prop="temperatureEnd" />
      <el-table-column label="温度结束值关系符号" align="center" prop="temperatureEndSymbol" />
      <el-table-column label="指令编码" align="center" prop="instructionsCode" />
      <el-table-column label="亮度参数" align="center" prop="brightnessParam" />
      <el-table-column label="次/min" align="center" prop="timeSecond" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleUpdate(scope.row)"
            class="tableBlueButtton"
            v-hasPermi="['inductionlamp:statusParam:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            @click="handleDelete(scope.row)"
            class="tableDelButtton"
            v-hasPermi="['inductionlamp:statusParam:remove']"
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

    <!-- 添加或修改设备控制状态参数对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备id" prop="equipmentId">
          <el-input v-model="form.equipmentId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="模式名称" prop="modeName">
          <el-input v-model="form.modeName" placeholder="请输入模式名称" />
        </el-form-item>
        <el-form-item label="模式编号" prop="modeCode">
          <el-input v-model="form.modeCode" placeholder="请输入模式编号" />
        </el-form-item>
        <el-form-item label="光照开始值" prop="illuminationStart">
          <el-input v-model="form.illuminationStart" placeholder="请输入光照开始值" />
        </el-form-item>
        <el-form-item label="光照开始值关系符号" prop="illuminationStartSymbol">
          <el-input v-model="form.illuminationStartSymbol" placeholder="请输入光照开始值关系符号" />
        </el-form-item>
        <el-form-item label="光照结束值" prop="illuminationEnd">
          <el-input v-model="form.illuminationEnd" placeholder="请输入光照结束值" />
        </el-form-item>
        <el-form-item label="光照结束值关系符号" prop="illuminationEndSymbol">
          <el-input v-model="form.illuminationEndSymbol" placeholder="请输入光照结束值关系符号" />
        </el-form-item>
        <el-form-item label="湿度开始值" prop="humidityStart">
          <el-input v-model="form.humidityStart" placeholder="请输入湿度开始值" />
        </el-form-item>
        <el-form-item label="湿度开始值关系符号" prop="humidityStartSymbol">
          <el-input v-model="form.humidityStartSymbol" placeholder="请输入湿度开始值关系符号" />
        </el-form-item>
        <el-form-item label="湿度结束值" prop="humidityEnd">
          <el-input v-model="form.humidityEnd" placeholder="请输入湿度结束值" />
        </el-form-item>
        <el-form-item label="湿度结束值关系符号" prop="humidityEndSymbol">
          <el-input v-model="form.humidityEndSymbol" placeholder="请输入湿度结束值关系符号" />
        </el-form-item>
        <el-form-item label="温度开始值" prop="temperatureStart">
          <el-input v-model="form.temperatureStart" placeholder="请输入温度开始值" />
        </el-form-item>
        <el-form-item label="温度开始值关系符号" prop="temperatureStartSymbol">
          <el-input v-model="form.temperatureStartSymbol" placeholder="请输入温度开始值关系符号" />
        </el-form-item>
        <el-form-item label="温度结束值" prop="temperatureEnd">
          <el-input v-model="form.temperatureEnd" placeholder="请输入温度结束值" />
        </el-form-item>
        <el-form-item label="温度结束值关系符号" prop="temperatureEndSymbol">
          <el-input v-model="form.temperatureEndSymbol" placeholder="请输入温度结束值关系符号" />
        </el-form-item>
        <el-form-item label="指令编码" prop="instructionsCode">
          <el-input v-model="form.instructionsCode" placeholder="请输入指令编码" />
        </el-form-item>
        <el-form-item label="亮度参数" prop="brightnessParam">
          <el-input v-model="form.brightnessParam" placeholder="请输入亮度参数" />
        </el-form-item>
        <el-form-item label="次/min" prop="timeSecond">
          <el-input v-model="form.timeSecond" placeholder="请输入次/min" />
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
import { listParam, getParam, delParam, addParam, updateParam, exportParam } from "@/api/equipment/param/param";

export default {
  name: "Param",
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
      // 设备控制状态参数表格数据
      paramList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentId: null,
        modeName: null,
        modeCode: null,
        illuminationStart: null,
        illuminationStartSymbol: null,
        illuminationEnd: null,
        illuminationEndSymbol: null,
        humidityStart: null,
        humidityStartSymbol: null,
        humidityEnd: null,
        humidityEndSymbol: null,
        temperatureStart: null,
        temperatureStartSymbol: null,
        temperatureEnd: null,
        temperatureEndSymbol: null,
        instructionsCode: null,
        brightnessParam: null,
        timeSecond: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        equipmentId: [
          { required: true, message: "设备id不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询设备控制状态参数列表 */
    getList() {
      this.loading = true;
      listParam(this.queryParams).then(response => {
        this.paramList = response.rows;
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
        equipmentId: null,
        modeName: null,
        modeCode: null,
        illuminationStart: null,
        illuminationStartSymbol: null,
        illuminationEnd: null,
        illuminationEndSymbol: null,
        humidityStart: null,
        humidityStartSymbol: null,
        humidityEnd: null,
        humidityEndSymbol: null,
        temperatureStart: null,
        temperatureStartSymbol: null,
        temperatureEnd: null,
        temperatureEndSymbol: null,
        instructionsCode: null,
        brightnessParam: null,
        timeSecond: null,
        createTime: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备控制状态参数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getParam(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备控制状态参数";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateParam(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addParam(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备控制状态参数？').then(function() {
        return delParam(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
    /** 导出按钮操作 */
    // handleExport() {
    //   const queryParams = this.queryParams;
    //   this.$modal.confirm('是否确认导出所有设备控制状态参数数据项？').then(() => {
    //     this.exportLoading = true;
    //     return exportParam(queryParams);
    //   }).then(response => {
    //     this.$download.name(response.msg);
    //     this.exportLoading = false;
    //   }).catch(() => {});
    // }
  }
};
</script>
