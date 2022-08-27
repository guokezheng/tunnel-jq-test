<template>
  <div class="app-container">
    <!--  列表搜索表单  -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点类型" prop="nodeType">
        <el-select v-model="queryParams.nodeType" placeholder="请选择节点类型" clearable size="small">
          <el-option
            v-for="dict in nodeTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="连网方式" prop="netType">
        <el-select v-model="queryParams.netType" placeholder="请选择连网方式" clearable size="small">
          <el-option
            v-for="dict in netTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--  增删改查导出按钮  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['iot:product:add']"
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
          v-hasPermi="['iot:product:edit']"
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
          v-hasPermi="['iot:product:remove']"
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
          v-hasPermi="['iot:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  产品列表  -->
    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" width="55" type="index" align="center" />
      <el-table-column label="产品名称" align="center" prop="productName" />
      <el-table-column label="所属品类" align="center" prop="categoryKey" >
        <template slot-scope="scope">
          {{ categoryMap.get(scope.row.categoryKey) }}
        </template>
      </el-table-column>
      <el-table-column label="节点类型" align="center" prop="nodeType">
        <template slot-scope="scope">
          <dict-tag :options="nodeTypeOptions" :value="scope.row.nodeType"/>
        </template>
      </el-table-column>
      <el-table-column label="连网方式" align="center" prop="netType">
        <template slot-scope="scope">
          <dict-tag :options="netTypeOptions" :value="scope.row.netType"/>
        </template>
      </el-table-column>
      <el-table-column label="产品描述" align="center" prop="description" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iot:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iot:product:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--  产品列表分页  -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改产品对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="所属品类" prop="categoryName">
          <el-input ref="categoryName" v-model="form.categoryName" @focus="handleCategoryFocus" placeholder="请选择所属品类" >
            <el-button slot="append" :disabled="form.categoryKey==undefined" @click="handleLookFunctionClick">查看功能</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="节点类型" prop="nodeType">
          <el-select v-model="form.nodeType" @change="handleNodeTypeChange" placeholder="请选择节点类型" class="select-width">
            <el-option
              v-for="dict in nodeTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="netTypeShow" label="连网方式" prop="netType">
          <el-select v-model="form.netType" placeholder="请选择连网方式" class="select-width">
            <el-option
              v-for="dict in netTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产品描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 品类抽屉对话框 -->
    <el-drawer
      title="选择品类"
      :visible.sync="drawerCategory.visible"
      :direction="drawerCategory.direction"
      :modal="drawerCategory.modal"
      @open="handleDrawerCategoryOpen"
    >

      <div style="margin-left: 15px; margin-right: 15px">

        <div style="margin-bottom: 15px;">

          <!-- 品类屉搜索 -->
          <el-row>
            <el-col :span="9">
              <el-select
                clearable
                v-model="category.queryParams.field"
                placeholder="全部领域"
                @change="handleCategoryFieldChange"
              >
                <el-option
                  v-for="dict in categoryFieldOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12" :offset="1">
              <el-input placeholder="请输入品类名称" v-model="category.queryParams.categoryName">
                <el-button slot="append" icon="el-icon-search" @click="handleCategoryNameClick"></el-button>
              </el-input>
            </el-col>
          </el-row>

        </div>

        <!-- 品类列表 -->
        <el-table v-loading="loading" :data="category.categoryList" @selection-change="handleSelectionChange">
          <el-table-column label="序号" width="55" align="center" type="index" />
          <el-table-column label="品类名称" align="center" prop="categoryName" >
            <template slot-scope="scope">
              {{scope.row.categoryName}}
              <el-button style="color: #CCCCCCFF" icon="el-icon-info" type="text" @click="handleCheckFunctionClick(scope.row.id)"></el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button :disabled="form.categoryKey===scope.row.id" size="mini" type="text" @click="handleCategoryClick(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 品类列表分页 -->
        <pagination
          v-show="category.total>0"
          :total="category.total"
          :page.sync="category.queryParams.pageNum"
          :limit.sync="category.queryParams.pageSize"
          @pagination="getListCategory"
          layout="prev, pager, next"
        />

      </div>

    </el-drawer>

    <!-- 功能抽屉对话框 -->
    <el-drawer
      title="标准功能定义"
      :visible.sync="drawerFunction.visible"
      :direction="drawerFunction.direction"
      :modal="drawerFunction.modal"
      :wrapper-closable="drawerFunction.wrapperClosable"
      @open="handleDrawerFunctionOpen"
    >

      <div style="margin-left: 15px; margin-right: 15px">

        <!-- 功能列表 -->
        <el-table v-loading="loading" :data="functionList" @selection-change="handleSelectionChange">
          <el-table-column label="序号" width="50" align="center" type="index"/>
          <el-table-column label="功能类型" align="center" prop="functionType">
            <template slot-scope="scope">
              <dict-tag :options="functionTypeOptions" :value="scope.row.functionType"/>
            </template>
          </el-table-column>
          <el-table-column label="功能名称" align="center" prop="name"/>
          <el-table-column label="标识符" align="center" prop="identifier"/>
          <el-table-column label="数据类型" align="center" prop="dataType">
            <template slot-scope="scope">
              <dict-tag :options="dataTypeOptions" :value="scope.row.dataType"/>
            </template>
          </el-table-column>
        </el-table>

      </div>

    </el-drawer>

  </div>
</template>

<script>
import { listProduct, getProduct, delProduct, addProduct, updateProduct, exportProduct } from "@/api/iot/product";
import { listCategory } from "@/api/iot/category";
import { listFunction } from '@/api/iot/function'

export default {
  name: "Product",
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
      // 产品表格数据
      productList: [],
      // 功能表格数据
      functionList: [],
      // 品类数据
      categoryMap: new Map(),
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 节点类型字典
      nodeTypeOptions: [],
      // 连网方式字典
      netTypeOptions: [],
      // 品类领域字典
      categoryFieldOptions: [],
      // 功能类型List
      functionTypeOptions: [],
      // 功能类型List
      dataTypeOptions: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productName: null,
        nodeType: null,
        netType: null,
        description: null,
      },
      // 表单参数
      form: {
        productName: undefined,
        categoryKey: undefined,
        categoryName: undefined,
        nodeType: undefined,
        netType: undefined,
        description: undefined,
      },
      drawerCategory: {
        visible: false,
        direction: 'rtl',
        modal: false
      },
      drawerFunction: {
        visible: false,
        direction: 'rtl',
        modal: false,
        wrapperClosable: true
      },
      netTypeShow: true,
      // 表单校验
      rules: {
        productName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ],
        categoryName: [
          { required: true, message: "所属品类不能为空", trigger: "blur" }
        ],
        nodeType: [
          { required: true, message: "节点类型不能为空", trigger: "change" }
        ],
        netType: [
          { required: true, message: "连网方式不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      },
      // 品类数据
      category: {
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          field: undefined,
          categoryName: undefined
        },
        total: 0,
        // 品类表格数据
        categoryList: []
      }
    };
  },
  async created() {
    this.getDicts("iot_product_node_type").then(response => {
      this.nodeTypeOptions = response.data;
    });
    this.getDicts("iot_product_net_type").then(response => {
      this.netTypeOptions = response.data;
      console.log(this.netTypeOptions,'111111111111');
    });
    this.getDicts("iot_category_field").then(response => {
      this.categoryFieldOptions = response.data;
    });
    // 功能类型字典
    this.getDicts("iot_function_function_type").then(response => {
      this.functionTypeOptions = response.data;
    });
    // 数据类型字典
    this.getDicts("iot_function_data_type").then(response => {
      this.dataTypeOptions = response.data;
    });
    await this.getMapCategory()
    this.getList();
  },
  methods: {
    /** 查询产品列表 */
    getList() {
      this.loading = true;
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
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
        productName: null,
        categoryName: null,
        categoryKey: null,
        nodeType: null,
        netType: null,
        description: null,
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
      this.title = "添加产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProduct(id).then(({ data: {
        id,
        productName,
        categoryKey,
        nodeType,
        netType,
        description
      }}) => {
        this.form.id = id;
        this.form.productName = productName;
        this.form.categoryKey = categoryKey;
        this.form.categoryName = this.categoryMap.get(categoryKey);
        this.form.nodeType = nodeType
        this.form.netType = netType
        this.form.description = description
        this.open = true;
        this.title = "修改产品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(this.form).then(response => {
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
      this.$confirm('是否确认删除？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delProduct(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有产品数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportProduct(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    /** 所属品类在 Input 获得焦点时触发 **/
    handleCategoryFocus() {
      if (!this.drawerCategory.visible) {
        // 去除所属品类焦点
        this.$refs.categoryName.blur();
        // 显示“选择品类”抽屉
        this.drawerCategory.visible = true;
      }
    },
    /** 节点类型 select change 事件**/
    handleNodeTypeChange() {
      // 如果是网关子设备
      if (this.form.nodeType === 2) {
        // 隐藏连网方式下拉框
        this.netTypeShow = false;
        this.form.netType = null;
      }else {
        // 显示连网方式下拉框
        this.netTypeShow = true;
      }
    },
    /** Drawer 打开的回调 **/
    handleDrawerCategoryOpen() {
      this.getListCategory();
    },
    /** Drawer 打开的回调 **/
    handleDrawerFunctionOpen() {
      this.getListCategory();
    },
    /** 获取品类数据 **/
    getListCategory() {
      listCategory(this.category.queryParams).then(response => {
        this.category.categoryList = response.rows;
        this.category.total = response.total;
      });
    },
    /** 获取功能数据 **/
    getListFunction(categoryKey) {
      listFunction({ categoryKey }).then(response => {
        this.functionList = response.rows;
      });
    },
    /** 获取品类数据 **/
    getMapCategory() {
      return listCategory().then(response => {
        const categoryList = response.rows;
        if (!categoryList) return;
        categoryList.forEach(category => {
          this.categoryMap.set(category.id, category.categoryName);
        })
      });
    },
    /** 领域选中值发生变化时触发 **/
    handleCategoryFieldChange() {
      this.category.queryParams.pageNum = 1;
      this.getListCategory();
    },
    /** 品类名称输入框点击搜索 **/
    handleCategoryNameClick() {
      this.category.queryParams.pageNum = 1;;
      this.getListCategory();
    },
    /** 选择品类点击事件 **/
    handleCategoryClick(rowData) {
      // 赋值所属品类
      this.form.categoryName = rowData.categoryName;
      this.form.categoryKey = rowData.id;
    },
    /** 点击事件，查看品类功能 **/
    handleCheckFunctionClick(id) {

      this.getListFunction(id);

      if (!this.drawerFunction.visible) {
        // 显示“标准功能定义”抽屉
        this.drawerFunction.visible = true;
      }
    },
    /** 查看功能点击事件，查看当前输入框品类所有功能 **/
    handleLookFunctionClick() {
      const categoryKey = this.form.categoryKey;
      if (categoryKey) {
        this.handleCheckFunctionClick(categoryKey);
      }
    }
  }
};
</script>

<style scoped>
</style>
