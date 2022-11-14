<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="事件类型" prop="eventType">
        <el-select
          v-model="queryParams.eventType"
          placeholder="请选择事件类型"
          clearable
          size="small"
          style="width: 180px"
        >
          <el-option
            v-for="item in eventTypeData"
            :key="item.id"
            :label="item.eventType"
            :value="item.eventType"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery"
          >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:eventType:add']"
          >新增</el-button
        >
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:eventType:edit']"
        >修改</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:eventType:remove']"
          >删除</el-button
        >
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:eventType:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:eventType:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:eventType:remove']"
        >删除</el-button>
      </el-col>
     <el-col :span="1.5">
       <el-button
         type="warning"
         icon="el-icon-download"
         size="mini"
         @click="handleExport"
         v-hasPermi="['system:eventType:export']"
       >导出</el-button>
     </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row> -->

    <el-table
      v-loading="loading"
      :data="eventTypeList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="事件类型ID" align="center" prop="id" />
      <el-table-column label="防控类型" align="center"  >
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
            width="50px"
            height="35px"
            class="pictureUrl"
          />
          　　
        </template>
      </el-table-column>
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
            v-hasPermi="['system:eventType:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:eventType:remove']"
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

    <!-- 添加或修改事件类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件类型" prop="eventType">
          <el-input v-model="form.eventType" placeholder="请输入事件类型" />
        </el-form-item>
        <el-form-item label="简称" prop="simplifyName">
          <el-input v-model="form.simplifyName" placeholder="请输入简称" />
        </el-form-item>
        <el-form-item label="防控类型" prop="prevControlType">
          <el-select
            v-model="form.prevControlType"
            placeholder="防控类型"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in prevControlType"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="默认图标" label-width="100px" prop="iconUrl"> 
          <el-upload
            ref="upload"
            action="http://xxx.xxx.xxx/personality/uploadExcel"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :http-request="uploadFile"
            :file-list="fileList"
            :on-exceed="handleExceed"
            :on-change="handleChange"
            :onSuccess="uploadSuccess"
            :limit="1"
            :class="fileList.length >=1 ? 'showUpload':''"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible"  class="modifyEqTypeDialog"
          :append-to-body="true" style="width:600px !important;margin: 0 auto;"
          >
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="dialogOkDisabled">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEventType,
  getEventType,
  delEventType,
  addEventType,
  updateEventType,
} from "@/api/event/eventType";

export default {
  name: "EventType",
  data() {
    return {
      dialogOkDisabled:false,
      from:{},
      dialogImageUrl: "",
      dialogVisible:false,
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
      },
      //事件类型
      eventTypeData: {},
      // 表单参数
      form: {simplifyName:"",prevControlType:""},
      // 表单校验
      rules: {
        eventType: [
          { required: true, message: "请输入事件类型", trigger: "blur" },
        ],
        simplifyName: [
          { required: true, message: "请输入简称", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getDicts("prev_control_type").then(response => {
      this.prevControlType = response.data;
    });
    this.getList();
    this.getEventType();
    this.fileData = new FormData(); // new formData对象
  },
  methods: {
    /** 查询事件类型列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      listEventType(this.queryParams).then((response) => {
        console.log(response.rows,"查询事件类型列表 ")
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
      console.log(file,"file")
      if (file.hasOwnProperty("uid")) {
        this.removeIds.push(file.uid);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(file) {
      console.log(file);
      this.fileData.append("file", file.file); // append增加数据

      console.log(this.fileData,"this.fileData");
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
      };
      this.resetForm("form");
      this.removeIds = [];

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
      this.ids = selection.map((item) => item.id);
      console.log("ids>>>>>", this.ids);

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
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      var that = this;
      that.fileList = [];

      const id = row.id || this.ids[0];
      getEventType(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改事件类型";
        that.fileList.push({
          // name: this.form.pictureName,
          url: this.form.iconUrl,
        });
      });
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
    },
    update(row) {
      this.reset();
      getEventType(row.id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改事件类型";
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
      this.dialogOkDisabled = true

        this.fileData = new FormData(); // new formData对象
        this.$refs.upload.submit(); // 提交调用uploadFile函数
        this.fileData.append("eventType", this.form.eventType); //类型名称
        this.fileData.append("simplifyName", this.form.simplifyName); //类型名称
        // this.fileData.append("uid", this.form.uid); //类型名称

        this.$refs["form"].validate((valid) => {
        if (valid) {
          if(this.fileList.length <= 0) {
            return this.$modal.msgWarning('请选择要上传的图标')
          }
          if (this.form.id != null) {
            this.fileData.append("id", this.form.id);

            this.fileData.append("removeIds", this.removeIds);
            this.fileData.append("iconUrl", this.form.iconUrl);
            updateEventType(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.$refs.upload.clearFiles();
                this.getList();
                this.dialogOkDisabled = false
              }
            });
          } else {
            console.log(this.fileData,"this.fileDatathis.fileData");
            addEventType(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.dialogOkDisabled = false

              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm("是否确认删除选中数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delEventType(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {});
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
