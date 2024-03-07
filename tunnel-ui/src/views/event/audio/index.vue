<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          :disabled = "buttonDis"
          size="small"
          @click="updateAdd()"
          >配置提示音
        </el-button>
        <el-button
          :disabled = "buttonDis"
          size="small"
          @click="updateDel()"
          >取消提示音
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入事件类型，回车搜索"
            v-model="queryParams.eventType"
            @keyup.enter.native="handleQuery"
            size="small"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="sj_boxShow = !sj_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="sj_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="防控类型" prop="prevControlType">
          <el-select
            v-model="queryParams.prevControlType"
            placeholder="请选择防控类型"
            clearable
            size="small"
          >
            <el-option
              v-for="item in prevControlType"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否配置" prop="isConfig">
          <el-select
            v-model="queryParams.isConfig"
            placeholder="请选择是否配置"
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
          <!--          <el-button type="primary" plain size="mini" :loading="exportLoading"
                               @click="handleExport"
                               v-hasPermi="['system:type:export']"
                    >导出</el-button>-->
        </el-form-item>
      </el-form>
    </div>

    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="eventTypeList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      height="62vh"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column label="防控类型" align="center">
        <template slot-scope="scope">
          <span>{{ getPrevControlType(scope.row.prevControlType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="简称" align="center" prop="simplifyName" />
      <el-table-column label="事件类型" align="center" prop="eventType" />
      <el-table-column label="图片" align="center">
        <template slot-scope="scope">
          <img
            :src="scope.row.iconUrl"
            width="25px"
            height="25px"
            class="pictureUrl"
          />
        </template>
      </el-table-column>
      <el-table-column label="是否配置" align="center" prop="isConfig">
        <template slot-scope="scope">
          <span>{{ scope.row.isConfig == "0" ? "否" : "是" }}</span>
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
  </div>
</template>

<script>
import {
  listEventType,
  getEventType,
  delEventType,
  addEventType,
  updateEventType,
  updateAudio,
} from "@/api/event/eventType";

export default {
  name: "EventType",
  data() {
    return {
      yxjOptions: [
        {
          value: "0",
          label: "低",
        },
        {
          value: "1",
          label: "中",
        },
        {
          value: "2",
          label: "高",
        },
        {
          value: "3",
          label: "紧急",
        },
      ],
      sj_boxShow: false,
      dialogOkDisabled: false,
      from: {},
      dialogImageUrl: "",
      dialogVisible: false,
      prevControlType: [],
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
      // 事件类型表格数据
      eventTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      fileList: [], // upload多文件数组

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eventType: null,
        prevControlType: null,
        isConfig: null,
      },
      //事件类型
      eventTypeData: {},
      // 表单参数
      form: { simplifyName: "", prevControlType: "", isUsable: "" },
      // 表单校验
      rules: {
        eventType: [
          { required: true, message: "请输入事件类型", trigger: "change" },
          { max: 50, message: "最长输入50个字符", trigger: "change" },
        ],
        prevControlType: [
          { required: true, message: "请选择防控类型", trigger: "change" },
        ],
        isUsable: [
          { required: true, message: "请配置是否可用", trigger: "change" },
        ],
        simplifyName: [
          { required: true, message: "请输入简称", trigger: "change" },
          { max: 15, message: "最长输入15个字符", trigger: "change" },
        ],
        iconUrl: [
          { required: true, message: "请上传默认图标", trigger: "change" },
        ],
      },
      buttonDis: true,
      //是否可用
      options: [
        {
          value: "0",
          label: "否",
        },
        {
          value: "1",
          label: "是",
        },
      ],
    };
  },
  created() {
    this.getDicts("prev_control_type").then((response) => {
      this.prevControlType = response.data;
    });
    this.getList();
    this.getEventType();
    this.fileData = new FormData(); // new formData对象
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
    //优先级
    getPriority(num) {
      for (var item of this.yxjOptions) {
        if (item.value == num) {
          return item.label;
        }
      }
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.sj_boxShow == true) {
          self.sj_boxShow = false;
        }
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询事件类型列表 */
    getList() {
      this.loading = true;
      this.queryParams.isUsable = '1'
      console.log(this.queryParams);
      listEventType(this.queryParams).then((response) => {
        console.log(response.rows, "查询事件类型列表 ");
        this.eventTypeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询事件类型列表 */
    getEventType() {
      listEventType().then((response) => {
        console.log(response, "responseresponse");
        this.eventTypeData = response.rows;
      });
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //删除文件
    handleRemove(file, fileList) {
      console.log(file, "file");
      if (file.hasOwnProperty("uid")) {
        this.removeIds.push(file.uid);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(file) {
      console.log(file);
      this.fileData.append("file", file.file); // append增加数据

      console.log(this.fileData, "this.fileData");
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    uploadSuccess() {
      console.log("成功了");
      this.$refs.upload.clearFiles();
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
        eventType: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        iconUrl: null,
        isUsable: null,
        priority: "",
      };
      this.resetForm("form");
      this.removeIds = [];
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
      this.queryParams.eventType = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      console.log("ids>>>>>", this.ids);
      if(this.ids.length != 0){
        this.buttonDis = false
      }else {
        this.buttonDis = true
      }
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.fileList = [];

      this.open = true;
      this.title = "添加事件类型";
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
    },
    updateAdd() {
      let params = {ids:this.ids.toString(),isConfig:'1'}
      this.$confirm("是否配置提示音?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .then(function () {
            return updateAudio(params)
          })
          .then(() => {
            this.$modal.msgSuccess("配置成功");
            this.$refs.tableFile.clearSelection();
            this.getList();
            this.ids = []
          })
          .catch(function () {
            this.getList();
            this.ids = []
          });
      //this.reset();
      
    },
    updateDel() {
      //this.reset();
      let params = {ids:this.ids.toString(),isConfig:'0'}
      this.$confirm("是否取消提示音?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .then(function () {
            return updateAudio(params)
          })
          .then(() => {
            this.$modal.msgSuccess("取消成功");
            this.$refs.tableFile.clearSelection();
            this.getList();
            this.ids = []
          })
          .catch(function () {
            this.getList();
            this.ids = []
          });
    },
    getPrevControlType(num) {
      for (var item of this.prevControlType) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    /** 提交按钮 */
    submitForm() {
      // this.dialogOkDisabled = true
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("eventType", this.form.eventType); //类型名称
      this.fileData.append("simplifyName", this.form.simplifyName); //类型名称
      this.fileData.append("prevControlType", this.form.prevControlType); //类型名称
      this.fileData.append("isUsable", this.form.isUsable); //是否可用
      this.fileData.append(
        "priority",
        this.form.priority == null ? "" : this.form.priority
      ); //是否可用

      if (this.form.id == null) {
        this.form.iconUrl = -1;
      }

      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.fileList.length <= 0) {
            return this.$modal.msgWarning("请选择要上传的图标");
          }
          if (this.form.id != null) {
            this.fileData.append("id", this.form.id);
            this.fileData.append("removeIds", this.removeIds);
            this.fileData.append("iconUrl", this.form.iconUrl);
            updateEventType(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.$refs.tableFile.clearSelection();
                this.$refs.upload.clearFiles();
                this.getList();
                this.dialogOkDisabled = false;
              }
            });
          } else {
            console.log(this.fileData, "this.fileDatathis.fileData");
            addEventType(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.dialogOkDisabled = false;
              }
            });
          }
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "business/eventType/export",
        {
          ...this.queryParams,
        },
        `system_eventType.xlsx`
      );
    },
  },
};
</script>
<style scoped lang="scss">
::v-deep .showUpload {
  .el-upload {
    display: none !important;
  }
}
::v-deep .el-upload-list__item {
  transition: none !important;
}
</style>
