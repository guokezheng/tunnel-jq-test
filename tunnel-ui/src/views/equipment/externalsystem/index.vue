<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:system:add']"
          >新增
        </el-button>
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:system:remove']"
          >删除
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="14">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入系统名称、系统地址，回车搜索"
            v-model="queryParams.searchValue"
            @keyup.enter.native="handleQuery"
            size="small"
            style="border-right: solid 1px #00c8ff; border-radius: 3px"
          >
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="系统名称" prop="systemName">
        <el-input
          v-model="queryParams.systemName"
          placeholder="请输入系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系统地址" prop="systemUrl">
        <el-input
          v-model="queryParams.systemUrl"
          placeholder="请输入系统地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button type="primary" plain size="mini" @click="handleAdd"
                   v-hasPermi="['system:system:add']">新增
        </el-button>
        <el-button type="primary" plain size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPermi="['system:system:edit']">修改
        </el-button>
        <el-button type="primary" plain size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPermi="['system:system:remove']">删除
        </el-button>
      </el-form-item>
    </el-form> -->

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['system:system:add']"-->
    <!--        >新增</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          plain-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['system:system:edit']"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="danger"-->
    <!--          plain-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['system:system:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          plain-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          :loading="exportLoading"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['system:system:export']"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
    <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    <!--    </el-row>-->

    <el-table
      v-loading="loading"
      :data="systemList"
      @selection-change="handleSelectionChange"
      height="62vh"
      class="allTable"
      @row-click="handleRowClick"
      ref="tableFile"
      :row-key="getRowKey"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="系统名称" align="center" prop="systemName" />
      <!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="设备品牌" align="center" prop="brandId">
        <template slot-scope="scope">
          <span>{{ getName(scope.row.brandId) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="隧道管理站" align="center" prop="brandId" >
        <template slot-scope="scope">
          <span>{{ getDeptName(scope.row.deptId) }}</span>
        </template>
      </el-table-column>-->

      <el-table-column label="所属隧道" align="center" prop="tunnelId">
        <template slot-scope="scope">
          <span>{{ getTunnelName(scope.row.tunnelId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否映射方向" align="center" prop="isDirection" />
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="网络状态" align="center" prop="networkStatus" />
      <el-table-column label="系统地址" align="center" prop="systemUrl" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['system:system:edit']"-->
          <!--          >修改</el-button>-->
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-delete"-->
          <!--            @click="handleDelete(scope.row)"-->
          <!--            v-hasPermi="['system:system:remove']"-->
          <!--          >删除</el-button>-->
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:system:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:system:remove']"
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

    <!-- 添加或修改外部系统对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
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

        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择所属隧道"
            style="width: 100%"
          >
            <el-option
              v-for="item in tunnelList"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>

        <!--<el-form-item label="隧道管理站" prop="deptId">
          <treeselect
            v-model="form.deptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择隧道管理站"
          />
        </el-form-item>-->

        <!--        <el-form-item label="是否映射方向" prop="isDirection">-->
        <!--          <el-input v-model="form.isDirection" placeholder="请输入是否映射方向" />-->
        <!--        </el-form-item>-->

        <el-form-item label="是否映射方向:" prop="isDirection">
          <el-select
            v-model="form.isDirection"
            placeholder="请选择是否映射方向"
            style="width: 100%"
          >
            <el-option
              v-for="item in directionMapping"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <!--        <el-form-item label="网络状态">-->
        <!--          <el-radio-group v-model="form.networkStatus">-->
        <!--            <el-radio label="1">请选择字典生成</el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="form.systemName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="系统地址" prop="systemUrl">
          <el-input v-model="form.systemUrl" placeholder="请输入系统地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listSystem,
  getSystem,
  delSystem,
  addSystem,
  updateSystem,
  exportSystem,
} from "@/api/equipment/externalsystem/system";
import { getDevBrandList } from "@/api/equipment/eqlist/api";
import { treeselectExcYG1, listDept } from "@/api/system/dept";
import { listAllTunnels1 } from "@/api/equipment/tunnel/api.js";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "System",
  components: { Treeselect },

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
      // 外部系统表格数据
      systemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        brandId: null,
        isDirection: null,
        username: null,
        password: null,
        networkStatus: null,
        systemName: null,
        systemUrl: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      directionMapping: [
        {
          value: "0",
          label: "是",
        },
        {
          value: "1",
          label: "否",
        },
      ],
      //设备品牌
      brandList: [],
      deptOptions: undefined,
      deptList: [],
      tunnelList: [],
    };
  },
  created() {
    this.getList();
    this.getDevBrandList();
    this.getTreeselect();
    // this.getDeptList();
    this.getTunnelList();
  },
  methods: {
    handleRowClick(row){
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    getTunnelList() {
      listAllTunnels1().then((response) => {
        this.tunnelList = response.data;
        console.log("tunnelList>>>>>>>", this.tunnelList);
      });
    },
    getDeptList() {
      listDept().then((response) => {
        this.deptList = response.data;
        console.log("deptOptions>>>>>>>", this.deptOptions);
      });
    },

    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log("deptOptions>>>>>>>", this.deptOptions);
      });
    },
    getName(num) {
      for (var item of this.brandList) {
        if (item.supplierId == num) {
          return item.shortName;
        }
      }
    },

    getDeptName(num) {
      for (var item of this.deptList) {
        if (item.deptId == num) {
          return item.deptName;
        }
      }
    },
    getTunnelName(num) {
      for (var item of this.tunnelList) {
        if (item.tunnelId == num) {
          return item.tunnelName;
        }
      }
    },

    getDevBrandList() {
      getDevBrandList().then((result) => {
        console.log("brandList:>>>", result.data);
        this.brandList = result.data;
      });
    },
    /** 查询外部系统列表 */
    getList() {
      this.loading = true;
      listSystem(this.queryParams).then((response) => {
        this.systemList = response.rows;
        for (let i = 0; i < this.systemList.length; i++) {
          if (this.systemList[i].isDirection == "0") {
            this.systemList[i].isDirection = "是";
          } else if (this.systemList[i].isDirection == "1") {
            this.systemList[i].isDirection = "否";
          }
        }
        this.total = response.total;
        this.loading = false;
      });
    },
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionMapping, row.isDirection);
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
        tunnelId: null,
        isDirection: null,
        username: null,
        password: null,
        networkStatus: "0",
        systemName: null,
        systemUrl: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
      this.queryParams.searchValue = "";
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
      this.title = "添加外部系统";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getSystem(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改外部系统";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateSystem(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addSystem(this.form).then((response) => {
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
      let that = this
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delSystem(ids);
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
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出外部系统数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportSystem(queryParams);
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
