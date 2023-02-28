<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['device:brand:add']"
          >新增</el-button
        >
        <el-button
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:brand:edit']"
          >修改</el-button
        >
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:brand:remove']"
          >删除</el-button
        >
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['device:brand:export']"
          >导出</el-button>
          <el-button size="small" @click="resetQuery"
          >刷新</el-button
        >
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
            <el-input
              v-model="queryParams.supplierName"
              placeholder="请输入设备厂商名称、简称,回车搜索"
              clearable
              size="small"
              @keyup.enter.native="handleQuery">
              <el-button
                slot="append"
                icon="icon-gym-Gsearch"
                @click="boxShow = !boxShow"
              ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
<!--    <div ref="cc" class="searchBox searchBoxMini" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="100px"
      >
      <el-form-item label="设备厂商名称" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入设备厂商名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="简称" prop="shortName" >
        <el-input
          v-model="queryParams.shortName"
          placeholder="请输入简称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
          >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
          >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>-->
    <div class="tableTopHr" ></div>
    <el-table
      v-loading="loading"
      :data="brandList"
      @selection-change="handleSelectionChange"
      class="allTable"
      height="62vh"
    >
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="序号" type="index" align="center" :index="indexMethod">
        <!--<template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>-->
      </el-table-column>


      <!--<el-table-column label="设备厂商编号" align="center" prop="supplierId" />-->
      <el-table-column
        label="设备厂商名称"
        align="center"
        prop="supplierName"
      />
      <el-table-column label="简称" align="center" prop="shortName" />
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
            v-hasPermi="['device:brand:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:brand:remove']"
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

    <!-- 添加或修改物联设备厂商对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="设备厂商名称" prop="supplierName">
          <el-input
            v-model="form.supplierName"
            placeholder="请输入设备厂商名称"
          />
        </el-form-item>
        <el-form-item label="简称" prop="shortName">
          <el-input v-model="form.shortName" placeholder="请输入简称" />
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
  listBrand,
  getBrand,
  delBrand,
  addBrand,
  updateBrand,
  exportBrand,
} from "@/api/equipment/deviceBrand/brand";
export default {
  name: "Brand",
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
      // 物联设备厂商表格数据
      brandList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        supplierName: null,
        shortName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        supplierName: [
          { required: true, message: "设备厂商名称不能为空", trigger: "blur" },
        ],
        shortName: [
          { required: true, message: "简称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  mounted(){
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    /** 查询物联设备厂商列表 */
    getList() {
      this.loading = true;
      listBrand(this.queryParams).then((response) => {
        this.brandList = response.rows;
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
        supplierId: null,
        supplierName: null,
        shortName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
      this.queryParams.supplierName = '';
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.supplierId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备厂商";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const supplierId = row.supplierId || this.ids;
      getBrand(supplierId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备厂商";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.supplierId != null) {
            updateBrand(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBrand(this.form).then((response) => {
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
      const supplierIds = row.supplierId || this.ids;
      this.$modal
        .confirm(
          '是否确认删除已经选择的物联设备厂商数据项？'
        )
        .then(function () {
          return delBrand(supplierIds);
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
        .confirm("是否确认导出所有设备厂商数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportBrand(queryParams);
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


