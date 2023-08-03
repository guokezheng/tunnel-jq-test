<template>
  <div class="app-container">
    <!-- <el-row :gutter="20"> -->
      <!--部门数据-->
      <!-- <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            @node-click="handleNodeClick"
            style="height: 100%; overflow: auto"
          />
        </div>
      </el-col> -->
      <!-- <el-col :span="20" :xs="24"> -->
        <!-- <el-row :gutter="20" class="mb8"> -->
          <!-- 全局搜索 -->
          <el-row :gutter="20" class="topFormRow">
            <el-col :span="6">
              <el-button
                v-hasPermi="['business:SdEmergencyPer:add']"
                size="small"
                @click="handleAdd()"
                >新增
              </el-button>
              <el-button size="small" @click="handleExport">导出</el-button>
              <el-button size="small" @click="resetQuery">刷新</el-button>
            </el-col>
            <el-col :span="7" :offset="11">
              <div ref="main" class="grid-content bg-purple">
                <el-input
                  placeholder="请输入姓名、所属管理站，回车搜索"
                  v-model="queryParams.userName"
                  @keyup.enter.native="handleQuery"
                  size="small"
                >
                  <el-button
                    slot="append"
                    class="searchTable"
                    @click="ry_boxShow = !ry_boxShow"
                  ></el-button>
                </el-input>
              </div>
            </el-col>
          </el-row>

          <div
            class="treeSearchBox searchBox"
            v-show="ry_boxShow"
            style="top: 50px !important"
          >
            <el-form
              ref="queryForm"
              :inline="true"
              :model="queryParams"
              label-width="75px"
            >
              <!--          <el-form-item label="隧道名称" prop="tunnelId" >
            <el-select
              v-model="queryParams.tunnelId"
              clearable
              placeholder="请选择隧道名称"
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelName"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>-->
              <el-form-item label="岗位" prop="groupName" style="width: 100%">
                <el-select
                  v-model="queryParams.groupName"
                  clearable
                  placeholder="请选择岗位"
                  size="small"
                  @change="handlechange"
                >
                  <el-option
                    v-for="item in emergencyPostList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
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
            ref="peopleTable"
            :data="SdEmergencyPerList"
            @selection-change="handleSelectionChange"
            @row-click="peopleTableRowClick"
            height="62vh"
            class="allTable"
            :row-key="getRowKey"
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
            <!--      <el-table-column label="隧道" align="center" prop="tunnelName" />-->
            <el-table-column label="所属管理站" align="center" prop="deptName" />
            <el-table-column label="姓名" align="center" prop="userName" />
            <el-table-column label="岗位" align="center" prop="groupName" />
            <el-table-column label="电话" align="center" prop="phone" />
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
                  v-hasPermi="['business:SdEmergencyPer:edit']"
                  >修改</el-button
                >
                <el-button
                  size="mini"
                  class="tableDelButtton"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['business:SdEmergencyPer:remove']"
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
        <!-- </el-row> -->
      <!-- </el-col> -->

      <!-- 添加或修改应急人员信息对话框 -->
      <el-dialog
        :title="title"
        :visible.sync="open"
        width="500px"
        append-to-body
        class="addUserDialog"
        :before-close="cancel"
        :close-on-click-modal="false"
      >
        <div class="dialogStyleBox">
          <div class="dialogLine"></div>
          <div class="dialogCloseButton"></div>
        </div>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="应急人员" prop="userName">
            <el-input v-model="form.userName" placeholder="请输入应急人员" />
          </el-form-item>

          <el-form-item label="管理站" prop="deptId">
            <treeselect
              v-model="form.deptId"
              :options="deptOptions"
              :disable-branch-nodes="true"
              placeholder="请选择管理站"
              noResultsText="暂无数据"
              @input="changeParentDept"
            />
          </el-form-item>
          <el-form-item label="岗位" prop="groupName">
            <el-select
              v-model="form.groupName"
              placeholder="请选择岗位"
              clearable
              size="small"
              style="width: 100%"
            >
              <el-option
                v-for="item in emergencyPostList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入电话" />
          </el-form-item>
          <div  class="dialog-footer">
            <el-button
              class="submitButton"
              :loading="submitBtnLoading"
              @click="submitForm"
              >确 定</el-button
            >
            <el-button class="closeButton" @click="cancel">取 消</el-button>
          </div>
        </el-form>
      </el-dialog>
    <!-- </el-row> -->
  </div>
</template>

<script>
import {
  listSdEmergencyPer,
  getSdEmergencyPer,
  delSdEmergencyPer,
  addSdEmergencyPer,
  updateSdEmergencyPer,
  exportSdEmergencyPer,
} from "@/api/event/SdEmergencyPer";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { treeSelectYG1 } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "SdEmergencyPer",
  components: { Treeselect },
  data() {
    return {
      manageStatin: this.$cache.local.get("manageStation"),
      defaultProps: {
        children: "children",
        label: "label",
      },

      // 部门树选项
      deptOptions: undefined,
      // 部门名称
      deptName: undefined,
      emergencyPostList: [],
      ry_boxShow: false,
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
      // 应急人员信息表格数据
      SdEmergencyPerList: [],
      tunnelData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 新增修改弹窗确认按钮loading
      submitBtnLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        userName: "",
      },
      paramsData: {
        tunnelId: "",
      },
      // 表单参数
      form: {
        id: null,
        tunnelId: null,
        userName: null,
        phone: null,
        createBy: null,
        updateTime: null,
        createTime: null,
        updateBy: null,
        // userName: null,
        groupName: null,
      },
      // 表单校验
      rules: {
        deptId: {
          required: true,
          message: "请选择管理站",
          trigger: "blur",
        },
        userName: [
          {
            required: true,
            message: "请输入应急人员",
            trigger: "blur",
          },
          {
            min: 1,
            max: 30,
            message: "长度在 1 ~ 30 个字符之间",
            trigger: "blur",
          },
        ],
        groupName: {
          required: true,
          message: "请选择岗位",
          trigger: "change",
        },
        phone: [
          { required: true, message: "请输入电话", trigger: "blur" },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: "电话号码格式不正确!",
            trigger: "blur",
          },
        ],
      },
    };
  },

  created() {
    this.getList();
    this.getTunnels();
    this.getTreeselect();
    this.getDicts("sd_emergency_post").then((response) => {
      this.emergencyPostList = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (self.ry_boxShow == true) {
        if (this.$refs.main && !this.$refs.main.contains(e.target)) {
          if (self.ry_boxShow == true) {
            self.ry_boxShow = false;
          }
        }
      }
    },

    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },

    changeParentDept() {
      this.$refs.form.validateField("deptId");
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeSelectYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },

    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.ids = "";
      this.queryParams.deptId = data.id;
      this.getList();
    },

    getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询应急人员信息列表 */
    getList() {
      this.loading = true;
      /*if(this.manageStatin == '1'){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }*/
      listSdEmergencyPer(this.queryParams).then((response) => {
        this.SdEmergencyPerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.peopleTable.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        tunnelId: null,
        userName: null,
        phone: null,
        createBy: null,
        updateTime: null,
        createTime: null,
        updateBy: null,
      };
      //this.getList();
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.$refs.peopleTable.clearSelection();
      this.getList();
    },
    handlechange(){
      this.$forceUpdate();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.deptName = "";
      this.queryParams.userName = "";
      this.queryParams.deptId = "";
      this.queryParams.ids = "";
      //this.ids = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 单击行切换行的选中状态（表格）
    peopleTableRowClick(row) {
      this.$refs.peopleTable.toggleRowSelection(row);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.submitBtnLoading = false;
      this.title = "添加应急人员信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.submitBtnLoading = false;
      this.reset();
      this.getTreeselect();
      const id = row.id || this.ids;
      getSdEmergencyPer(id).then((response) => {
        console.log(response.data, "response.data");
        this.form = response.data;
        this.form.tunnelName = response.data.tunnelName;
        this.open = true;
        this.title = "修改应急人员信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if (this.submitBtnLoading) return;
      this.submitBtnLoading = true;
      /* if(this.form.deptId==""||this.form.deptId==null){
        this.$message("请选择部门节点");
        this.submitBtnLoading = false
        return;

      }*/
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.form.id != null) {
            await updateSdEmergencyPer(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.peopleTable.clearSelection();
              this.getList();
            });
          } else {
            await addSdEmergencyPer(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        this.submitBtnLoading = false;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this
      const ids = row.id || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delSdEmergencyPer(ids);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {
          that.$refs.peopleTable.clearSelection();
        });;
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的应急人员数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的应急人员数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$confirm(confirmInfo, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportSdEmergencyPer(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.$refs.peopleTable.clearSelection();
          this.queryParams.ids = "";
        });
    },
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.getTunnels();
    },
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
};
</script>

<style scoped lang="scss">
.vue-treeselect__single-value {
  color: #ffffff !important;
}
</style>

