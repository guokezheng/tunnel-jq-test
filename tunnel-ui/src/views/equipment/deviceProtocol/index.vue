<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['device:protocol:add']"
          >新增
        </el-button>
        <!--        <el-button
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:protocol:edit']"
        >修改
        </el-button>-->
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:protocol:remove']"
          >删除
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.protocolName"
            placeholder="请输入协议名称，回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              class="searchTable"
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
        <el-form-item label="协议类型" prop="protocolType">
          <el-select
            v-model="queryParams.protocolType"
            placeholder="请选择协议类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in dict.type.device_protocol_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备品牌" prop="brandId">
          <el-select v-model="queryParams.brandId" placeholder="请选择设备品牌">
            <el-option
              v-for="item in brandList"
              :key="item.supplierId"
              :label="item.shortName"
              :value="item.supplierId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备大类" prop="brandId">
          <el-select
            v-model="queryParams.eqType"
            placeholder="请选择设备大类"
            clearable
            filterable
          >
            <el-option
              v-for="item in eqBigTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
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
    </div>
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="protocolList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>
      <!--      <el-table-column label="ID" align="center" prop="id"/>-->
      <el-table-column label="设备品牌" align="center" prop="brandId">
        <template slot-scope="scope">
          <span>{{ getName(scope.row.brandId, "brand") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备大类" align="center" prop="eqType">
        <template slot-scope="scope">
          <span>{{ getName(scope.row.eqType, "type") }}</span>
        </template>

        <!--<template slot-scope="scope">
          <dict-tag :options="dict.type.eq_category" :value="scope.row.eqType"/>
        </template>-->
      </el-table-column>
      <el-table-column label="协议名称" align="center" prop="protocolName" />
      <el-table-column label="协议类型" align="center" prop="protocolType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.device_protocol_type"
            :value="scope.row.protocolType"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="类名"
        align="center"
        prop="className"
        width="380"
      />
      <el-table-column label="备注" align="center" prop="note" />
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
            v-hasPermi="['device:protocol:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:protocol:remove']"
            >删除
          </el-button>
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

    <!-- 添加或修改设备协议对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
      :before-close="cancel"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备品牌" prop="brandId">
          <el-select
            v-model="form.brandId"
            placeholder="请选择设备品牌"
            style="width: 100%"
          >
            <el-option
              v-for="item in brandList"
              :key="item.supplierId"
              :label="item.shortName"
              :value="item.supplierId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备大类" prop="eqType">
          <el-select
            v-model="form.eqType"
            placeholder="请选择设备大类"
            style="width: 100%"
          >
            <el-option
              v-for="item in eqBigTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <!--<el-select v-model="form.eqType" placeholder="请选择设备类型" clearable style="width: 100%">
            <el-option
              v-for="dict in dict.type.eq_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>-->
        </el-form-item>
        <el-form-item label="协议名称" prop="protocolName">
          <el-input v-model="form.protocolName" placeholder="请输入协议名称" />
        </el-form-item>
        <el-form-item label="协议类型" prop="protocolType">
          <el-select
            v-model="form.protocolType"
            placeholder="请选择协议类型"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.device_protocol_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类名" prop="className">
          <el-input v-model="form.className" placeholder="请输入类名" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listProtocol,
  getProtocol,
  delProtocol,
  addProtocol,
  updateProtocol,
  exportProtocol,
} from "@/api/equipment/deviceProtocol/protocol";
import { getDevBrandList } from "@/api/equipment/eqlist/api";
import { listCategory } from "@/api/equipment/bigType/category";

export default {
  name: "Protocol",
  dicts: ["device_protocol_type"],
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
      // 设备协议表格数据
      protocolList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        brandId: null,
        eqType: null,
        protocolName: null,
        protocolType: null,
        className: null,
        note: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        brandId: [
          { required: true, message: "设备品牌不能为空", trigger: "change" },
        ],
        eqType: [
          { required: true, message: "设备大类不能为空", trigger: "change" },
        ],
        protocolName: [
          { required: true, message: "协议名称不能为空", trigger: "change" },
          { max: 50, message: "最长输入50个字符", trigger: "change" },
        ],
        protocolType: [
          { required: true, message: "协议类型不能为空", trigger: "change" },
        ],
        className: [
          { required: true, message: "类名不能为空", trigger: "change" },
          { max: 50, message: "最长输入50个字符", trigger: "change" },
        ],
        note: [{ max: 50, message: "最长输入50个字符", trigger: "change" }],
      },
      //设备品牌
      brandList: [],
      //设备大类
      eqBigTypeList: {},
    };
  },
  created() {
    this.getList();
    this.getEqBigType();
    this.getDevBrandList();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    bodyCloseMenus(e) {
      let self = this;
      if (
        !this.$refs.main.contains(e.target) &&
        !this.$refs.cc.contains(e.target)
      ) {
        if (self.boxShow == true) {
          self.boxShow = false;
        }
      }
    },
    getName(num, type) {
      if ("brand" == type) {
        for (var item of this.brandList) {
          if (item.supplierId == num) {
            return item.shortName;
          }
        }
      } else if ("type" == type) {
        for (var item of this.eqBigTypeList) {
          if (item.id == num) {
            return item.name;
          }
        }
      }
    },
    getEqBigType() {
      listCategory().then((response) => {
        this.eqBigTypeList = response.rows;
      });
    },
    getDevBrandList() {
      getDevBrandList().then((result) => {
        console.log("brandList:>>>", result.data);
        this.brandList = result.data;
      });
    },
    /** 查询设备协议列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listProtocol(this.queryParams).then((response) => {
        this.protocolList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        brandId: null,
        eqType: null,
        protocolName: null,
        protocolType: null,
        className: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        note: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.eqType = null;
      this.queryParams.protocolName = null;
      this.queryParams.protocolType = null;
      this.queryParams.className = null;
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
      this.title = "添加设备协议";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getProtocol(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备协议";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateProtocol(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addProtocol(this.form).then((response) => {
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
      let that = this;
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delProtocol(ids);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          that.$refs.tableFile.clearSelection();
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的设备协议数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的设备协议数据项？";
      }
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportProtocol(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {});
    },
  },
};
</script>
