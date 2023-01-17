<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="机构名称" prop="deptName">
        <el-input
          style="width: 200px"
          v-model.number="queryParams.deptName"
          placeholder="请输入机构名称"
          size="small"
        />
      </el-form-item>
      <el-form-item label="机构负责人" prop="leader">
        <el-input
          style="width: 200px"
          v-model.number="queryParams.leader"
          placeholder="请输入机构负责人"
          size="small"
        />
      </el-form-item>
      <el-form-item label="机构状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择机构状态" size="small">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button type="primary" plain size="mini" @click="resetQuery"
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-form-item>
    </el-form>

      <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="mechanismList"
        row-key="deptId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :row-class-name="tableRowClassName"
        class="menuAdministration tableClass"
      >
      <el-table-column label="机构名称" prop="deptName" />
      <el-table-column label="机构负责人" align="center" prop="leader" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="机构状态" align="center" prop="status">
        <template slot-scope="scope">
          <span>{{scope.row.status == "0" ? "正常" : "停用"}}</span>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import {
  handleQueryList,
  addList,
  editForm,
  updateForm,
  deleteForm,
  exportData,
} from "@/api/equipment/yingJiGou/emergencyOrganization";
import { batchDelete } from "@/api/surveyMechanism/api.js";
export default {
  data() {
    return {
      //新增弹出
      open: false,
      // 遮罩层
      loading: false,
      // 重新渲染表格状态
      refreshTable: true,
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      queryParams: {
        orgName: null,
        stagPointName: null
      },
      form: {},
      options: [{
          value: '0',
          label: '正常'
        }, {
          value: '1',
          label: '停用'
      }],
      mechanismList: [],
      exportLoading: false,
      showSearch: true,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 查询应急机构列表 */
    getList() {
      this.loading = true;
      handleQueryList(this.queryParams).then((res) => {
        this.mechanismList = this.handleTree(res,"deptId");
      });
      this.loading = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList(this.queryParams);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.$refs.queryForm.resetFields();
      this.queryForm = {
        deptName: null,
        leader: null,
        status: null
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.orgId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length; //非多个禁用
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 应急机构提交按钮 */
    submitForm() {
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.title == "修改应急资源") {
            await updateForm(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            await addList(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.reset();
                this.getList();
              }
            });
          }
        }
        // this.submitBtnLoading = false
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
  },
};
</script>

<style>
</style>
