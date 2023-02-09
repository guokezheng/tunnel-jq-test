<template>
  <div class="app-container">

    <!-- 全局搜索 -->
    <el-row :gutter="20" style="margin: 10px 0 25px">
      <el-col :span="4">
        <el-button
          v-hasPermi="['system:type:add']"
          size="small"
          type="primary"
          plain
          @click="handleAdd()"
        >新增类型
        </el-button>
      </el-col>
      <el-col :span="6" :offset="14">

        <div class="grid-content bg-purple">
          <el-input
            placeholder="请输入类型编码、类型名称"
            v-model="queryParams.vehicleTypeCode"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="el-icon-s-fold"
              @click="lx_boxShow = !lx_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="lx_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="重点车辆" prop="iskeyVehicle" style="width: 100%">
          <el-select
            v-model="queryParams.iskeyVehicle"
            placeholder="请选择重点车辆"
            clearable
            size="small"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
          >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
          >重置</el-button
          >
          <el-button type="primary" plain size="mini" :loading="exportLoading"
                     @click="handleExport"
                     v-hasPermi="['system:type:export']"
          >导出</el-button>
        </el-form-item>
      </el-form>
    </div>




<!--    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="类型编码" prop="vehicleTypeCode">
        <el-input
          v-model="queryParams.vehicleTypeCode"
          placeholder="请输入类型编码"
          clearable
          size="small"
          style="width: 100%"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型名称" prop="vehicleTypeName">
        <el-input
          v-model="queryParams.vehicleTypeName"
          placeholder="请输入类型名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="重点车辆" prop="iskeyVehicle">
&lt;!&ndash;        <el-input
          v-model="queryParams.iskeyVehicle"
          placeholder="请选择重点车辆"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />&ndash;&gt;
        <el-select v-model="queryParams.iskeyVehicle" clearable placeholder="请选择重点车辆" size="small">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" plain size="mini" @click="resetQuery">重置</el-button>
        <el-button type="primary" plain size="mini" @click="handleAdd" v-hasPermi="['system:type:add']"
        >新增</el-button>
        <el-button type="primary" plain size="mini" :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:type:edit']"
        >修改</el-button>
        <el-button type="primary" plain size="mini" :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:type:remove']"
        >删除</el-button>
        <el-button type="primary" plain size="mini" :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:type:export']"
        >导出</el-button>
      </el-form-item>
    </el-form>-->

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange"
              :row-class-name="tableRowClassName"
              max-height="640">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型编码" align="center" prop="vehicleTypeCode" />
      <el-table-column label="类型名称" align="center" prop="vehicleTypeName" />
      <el-table-column label="重点车辆" align="center" prop="iskeyVehicle">
        <template slot-scope="scope">
          <span>{{scope.row.iskeyVehicle == "0" ? "否" : "是"}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:type:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:type:remove']"
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

    <!-- 添加或修改车辆类型配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型编码" prop="vehicleTypeCode">
          <el-input v-model="form.vehicleTypeCode" placeholder="请输入类型编码" />
        </el-form-item>
        <el-form-item label="类型名称" prop="vehicleTypeName">
          <el-input v-model="form.vehicleTypeName" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="重点车辆" prop="iskeyVehicle">
<!--          <el-input v-model="form.iskeyVehicle" placeholder="请输入是否是重点车辆 0:否 1:是  默认为0" />-->
          <el-radio v-model="form.iskeyVehicle" label="0">否</el-radio>
          <el-radio v-model="form.iskeyVehicle" label="1">是</el-radio>
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
  listType,
  getType,
  delType,
  addType,
  updateType,
  exportType,
  checkData} from "@/api/surveyType/api";

export default {
  name: "Type",
  data() {
    return {
      lx_boxShow:false,
      iskeyVehicle:'0',
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
      // 车辆类型配置表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleTypeCode: null,
        vehicleTypeName: null,
        iskeyVehicle: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehicleTypeCode: [
          { required: true, message: "类型编码不能为空", trigger: "change" },
        ],
        vehicleTypeName: [
          { required: true, message: "类型名称不能为空", trigger: "change" },
        ]
      },
      options: [{
        value: '0',
        label: '否'
      }, {
        value: '1',
        label: '是'
      }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车辆类型配置列表 */
    getList() {
      this.loading = true;
      listType(this.queryParams).then(response => {
        this.typeList = response.rows;
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
        vehicleTypeCode: null,
        vehicleTypeName: null,
        iskeyVehicle: null,
        createBy: null,
        createTime: null,
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
      this.queryParams.vehicleTypeCode = "";
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
      this.form.iskeyVehicle='0';
      this.title = "添加车辆类型配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getType(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改车辆类型配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          checkData(this.form).then(response => {
            if(response.code == 200){
              if (this.form.id != null) {
                updateType(this.form).then(response => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                });
              } else {
                addType(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }
            }else {
              this.$modal.msgError(response.msg)
            }
          })
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除车辆类型配置编号为"' + ids + '"的数据项？').then(function() {
        return delType(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有车辆类型配置数据项？').then(() => {
        this.exportLoading = true;
        return exportType(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
<style>
.searchBox {
  position: absolute;
  top: 8%;
  right: 1%;
  width: 24%;
  z-index: 1996;
  background-color: #00335a;
  padding: 20px;
  box-sizing: border-box;
}
</style>
<style lang="scss" scoped>
.searchBox {
  ::v-deep .el-form-item__content {
    width: 80%;
    .el-select {
      width: 100%;
    }
  }
  .bottomBox {
    .el-form-item__content {
      display: flex;
      justify-content: center;
      align-items: flex-end;
    }
  }
}
.bottomBox {
  width: 100%;
  ::v-deep .el-form-item__content {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
  }
}
</style>

