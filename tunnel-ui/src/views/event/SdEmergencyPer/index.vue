<template>
  <div class="app-container">

    <!-- 全局搜索 -->
    <div>
      <el-col :span="4">
      <el-button style ="margin: 10px 0px 25px;height: 35px;"
        v-hasPermi="['business:SdEmergencyPer:add']"
        size="small"
        type="primary"
        plain
        @click="handleAdd()"
      >新增人员
      </el-button>
    </el-col>
    </div>
    <div ref="main" style = "margin-left: 75%">
      <el-row :gutter="20" style="margin: 10px 0 25px">

        <el-col :span="6" style ="width: 100%;">
          <div class="grid-content bg-purple">
            <el-input
              placeholder="请输入人员姓名，回车搜索"
              v-model="queryParams.userName"
              @keyup.enter.native="handleQuery"
            >
              <el-button
                slot="append"
                icon="el-icon-s-fold"
                @click="ry_boxShow = !ry_boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="ry_searchBox" v-show="ry_boxShow">
        <el-form
          ref="queryForm"
          :inline="true"
          :model="queryParams"
          label-width="75px"
        >

          <el-form-item label="隧道名称" prop="tunnelId" style="width: 100%">
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
          </el-form-item>
          <el-form-item label="岗位" prop="groupName" style="width: 100%">
            <el-select
              v-model="queryParams.groupName"
              clearable
              placeholder="请选择岗位"
              size="small"
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
    </div>

<!--    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="隧道名称" prop="tunnelId" v-show="manageStatin == '0'">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelName"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      &lt;!&ndash; <el-form-item label="机构" prop="tunnelId">
        <el-input style="width:200px;" v-model.number="queryParams.groupName" placeholder="请输入机构名称" size="small" />
      </el-form-item> &ndash;&gt;
      <el-form-item label="姓名"  prop="stagPointName">
          <el-input style="width:200px;" v-model.number="queryParams.userName" placeholder="请输入人员姓名" size="small" />
      </el-form-item>
      <el-form-item label="岗位" prop="groupName">
        <el-select
          v-model="queryParams.groupName"
          placeholder="请选择岗位"
          clearable
          size="small"
        >
          <el-option
            v-for="item in emergencyPostList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button  size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:SdEmergencyPer:add']"
          >新增</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:SdEmergencyPer:edit']"
          >修改</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:SdEmergencyPer:remove']"
          >删除</el-button
        >
      </el-form-item>
    </el-form>-->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:SdEmergencyPer:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:SdEmergencyPer:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:SdEmergencyPer:remove']"
          >删除</el-button
        >
      </el-col>
     <el-col :span="1.5">
       <el-button
         type="warning"
         plain
         icon="el-icon-download"
         size="mini"
         @click="handleExport"
         v-hasPermi="['business:SdEmergencyPer:export']/"
         >导出</el-button
       >
     </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row> -->

    <el-table
      v-loading="loading"
      ref="peopleTable"
      :data="SdEmergencyPerList"
      @selection-change="handleSelectionChange"
      @row-click="peopleTableRowClick"
      :row-class-name="tableRowClassName"
      max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号"  align="center">
        <template slot-scope="scope">
          {{scope.$index+1}}
        </template>
      </el-table-column>
      <!-- <el-table-column label="ID" align="center" prop="id" /> -->
      <!--      <el-table-column label="隧道ID" align="center" prop="tunnelId" />  -->
      <el-table-column label="隧道" align="center" prop="tunnelName" />
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

    <!-- 添加或修改应急人员信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="隧道" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="应急人员" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入应急人员" />
        </el-form-item>
        <el-form-item label="岗位" prop="groupName">
          <el-select
          v-model="form.groupName"
          placeholder="请选择岗位"
          clearable
          size="small"
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitBtnLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
export default {
  name: "SdEmergencyPer",
  components: {},
  data() {
    return {
      manageStatin:this.$cache.local.get("manageStation"),

      emergencyPostList:[],
      ry_boxShow:false,
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
      },
      paramsData: {
        tunnelId : ""
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
        tunnelId: {
          required: true, message: '请选择隧道', trigger: 'change'
        },
        userName: [{
          required: true, message: '请输入应急人员', trigger: 'blur'
        },
        { min: 1, max: 30, message: '长度在 1 ~ 30 个字符之间', trigger: 'blur' }],
        groupName: {
          required: true, message: '请选择岗位', trigger: 'change'
        },
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: '电话号码格式不正确!', trigger: 'blur' },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_emergency_post").then((response) => {
      this.emergencyPostList = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.ry_boxShow == true){
          self.ry_boxShow = false;
        }
      }
    },

    getTunnels() {
      if(this.$cache.local.get("manageStation") == "1"){
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询应急人员信息列表 */
    getList() {
      this.loading = true;
      if(this.manageStatin == '1'){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listSdEmergencyPer(this.queryParams).then((response) => {
        console.log(response,"应急人员表格")
        this.SdEmergencyPerList = response.rows;
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
        tunnelId: null,
        userName: null,
        phone: null,
        createBy: null,
        updateTime: null,
        createTime: null,
        updateBy: null,
      };
      this.getList();
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      console.log(this.queryParams)
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams = {
        tunnelId: null,
        groupName: null,
        userName: null,
        // tunnelId: null,
      };
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
      this.$refs.peopleTable.toggleRowSelection(row)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加应急人员信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
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
      if(this.submitBtnLoading) return
      this.submitBtnLoading = true
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.form.id != null) {
            await updateSdEmergencyPer(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
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
        this.submitBtnLoading = false
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除应急人员信息?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delSdEmergencyPer(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有应急人员信息数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportSdEmergencyPer(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.getTunnels();
    }
  }
};
</script>


<style>
.ry_searchBox {
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
.ry_searchBox {
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
