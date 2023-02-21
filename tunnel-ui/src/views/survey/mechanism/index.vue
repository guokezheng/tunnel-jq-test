<template>
  <div class="app-container">

    <!-- 全局搜索 -->
    <el-row :gutter="20" style="margin: 10px 0 25px">
      <el-col :span="4">
        <el-button
          type="primary"
          plain
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain
            >刷新</el-button
            >
      </el-col>
      <el-col :span="6" :offset="14">
        <div ref="main" class="grid-content bg-purple">
          <el-input
              placeholder="请输入机构、负责人，回车搜索"
              v-model="queryParams.deptName"
              @keyup.enter.native="handleQuery"
            >
              <el-button
                slot="append"
                icon="el-icon-s-fold"
                @click="jg_boxShow = !jg_boxShow"
              ></el-button>
            </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- <div>
      <el-col :span="4">
        <el-button style ="margin: 10px 0px 0px;height: 35px;"
          type="primary"
          plain
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
    </div> -->
    <!-- <div ref="main" style = "margin-left: 75%"> -->
      <!-- <el-row :gutter="20" style="margin: 10px 0 25px">

        <el-col :span="6"  style ="width: 100%;">
          <div class="grid-content bg-purple">
            <el-input
              placeholder="请输入机构、负责人，回车搜索"
              v-model="queryParams.deptName"
              @keyup.enter.native="handleQuery"
            >
              <el-button
                slot="append"
                icon="el-icon-s-fold"
                @click="jg_boxShow = !jg_boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row> -->
      <div class="jg_searchBox" v-show="jg_boxShow">
        <el-form
          ref="queryForm"
          :inline="true"
          :model="queryParams"
          label-width="75px"
        >

          <el-form-item label="机构状态" prop="status" style="width: 100%">
            <el-select
              style="width:335px"
              v-model="queryParams.status"
              clearable
              placeholder="请选择机构状态"
              size="small"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
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
    <!-- </div> -->

<!--    <el-form
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
    </el-form>-->

      <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="mechanismList"
        row-key="deptId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :row-class-name="tableRowClassName"
        class="menuAdministration"
        height="70vh"
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
      jg_boxShow:false,
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
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.jg_boxShow == true){
          self.jg_boxShow = false;
        }
      }
    },
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
      this.queryParams.deptName = "";
      this.$refs.queryForm.resetFields();
      this.queryForm = {
        deptName: null,
        /*leader: null,*/
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
.jg_searchBox {
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
.jg_searchBox {
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


