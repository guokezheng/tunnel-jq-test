<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" style="margin: 10px 0 25px">
      <el-col :span="6">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:vocabulary:add']"
          >新增</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:templateImage:remove']"
          >删除</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:templateImage:export']"
          >导出</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain>刷新</el-button>

      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.pictureName"
            placeholder="请输入图片名称,回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
          <!-- <el-button
              slot="append"
              icon="el-icon-s-fold"
              @click="boxShow = !boxShow"
            ></el-button> -->
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- <div ref="cc" class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
      <el-form-item label="图片名称" prop="pictureName" style="width:100%" >
           <el-input
             v-model="queryParams.pictureName"
             placeholder="请输入图片名称"
             clearable
             size="small"
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
    </div> -->
    <!--    <el-form-->
    <!--      :model="queryParams"-->
    <!--      ref="queryForm"-->
    <!--      :inline="true"-->
    <!--      v-show="showSearch"-->
    <!--      label-width="70px"-->
    <!--    >-->
    <!--      <el-form-item label="图片名称" prop="pictureName" >-->
    <!--        <el-input-->
    <!--          v-model="queryParams.pictureName"-->
    <!--          placeholder="请输入图片名称,回车搜索"-->
    <!--          clearable-->
    <!--          size="small"-->
    <!--          @keyup.enter.native="handleQuery"-->
    <!--        />-->
    <!--      </el-form-item>-->
    <!--      <el-form-item>-->
    <!--&lt;!&ndash;        <el-button&ndash;&gt;-->
    <!--&lt;!&ndash;          type="primary"&ndash;&gt;-->
    <!--&lt;!&ndash;          size="mini"&ndash;&gt;-->
    <!--&lt;!&ndash;          @click="handleQuery"&ndash;&gt;-->
    <!--&lt;!&ndash;          >搜索</el-button&ndash;&gt;-->
    <!--&lt;!&ndash;        >&ndash;&gt;-->
    <!--&lt;!&ndash;        <el-button size="mini" @click="resetQuery" type="primary" plain&ndash;&gt;-->
    <!--&lt;!&ndash;          >重置</el-button&ndash;&gt;-->
    <!--&lt;!&ndash;        >&ndash;&gt;-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['system:templateImage:add']"-->
    <!--          >新增</el-button-->
    <!--        >-->
    <!--&lt;!&ndash;        <el-button&ndash;&gt;-->
    <!--&lt;!&ndash;          type="primary"&ndash;&gt;-->
    <!--&lt;!&ndash;          plain&ndash;&gt;-->
    <!--&lt;!&ndash;          size="mini"&ndash;&gt;-->
    <!--&lt;!&ndash;          :disabled="single"&ndash;&gt;-->
    <!--&lt;!&ndash;          @click="handleUpdate"&ndash;&gt;-->
    <!--&lt;!&ndash;          v-hasPermi="['system:templateImage:edit']"&ndash;&gt;-->
    <!--&lt;!&ndash;          >修改</el-button&ndash;&gt;-->
    <!--&lt;!&ndash;        >&ndash;&gt;-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['system:templateImage:remove']"-->
    <!--          >删除</el-button-->
    <!--        >-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['system:templateImage:export']"-->
    <!--          >导出</el-button-->
    <!--        >-->
    <!--      </el-form-item>-->
    <!--    </el-form>-->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:vocabulary:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:vocabulary:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:vocabulary:remove']"
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
          v-hasPermi="['system:vocabulary:export']"
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
      :data="vocabularyList"
      max-height="640"
      @selection-change="handleSelectionChange"
      :default-sort="{ prop: 'creatTime', order: 'descending' }"
      :row-class-name="tableRowClassName"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" :index="indexMethod" type="index" width="50" align="center">
      </el-table-column>

      <el-table-column label="图片名称" align="center" prop="pictureName" />
      <el-table-column label="图片" align="center">
        <template slot-scope="scope">
          　　　　
          <img
            :src="scope.row.pictureUrl"
            width="35px"
            height="35px"
            class="pictureUrl"
          />
          　　
        </template>
      </el-table-column>
      <el-table-column label="图片宽度" align="center" prop="imageWidth" />
      <el-table-column label="图片高度" align="center" prop="imageHeight" />
      <!-- <el-table-column label="图片类型" align="center" prop=" imageType" /> -->
      <el-table-column label="图片分辨率" align="center" prop="vmsSize" />
      <el-table-column
        label="图片备注"
        align="center"
        prop="imageRemark"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="速度" align="center" prop="speed" />
      <!-- <el-table-column label="是否停用" align="center" prop="deleteflag" /> -->
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
            v-hasPermi="['system:templateImage:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:templateImage:remove']"
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
    <!-- 图片放大缩小 -->
    <el-dialog title="查看图片" :visible="yn" width="500px" @close="openImg">
      <img
        :src="img"
        style="width: 200%; height: 200%; max-width: 100%; max-height: 100%"
      />
    </el-dialog>
    <!-- 添加或修改情报板敏感字管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="图片名称" prop="pictureName">
          <el-input v-model="form.pictureName" placeholder="请输入图片名称" />
        </el-form-item>
        <el-form-item label="图片路径" prop="pictureUrl">
          <!-- <el-input v-model="form.url" placeholder="请输入图片路径" /> -->
          <el-upload
            id="promise"
            :class="{ disabled: eqObj.uploadDisabled }"
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
            accept=".png, .jpg"
            :limit="1"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>
        </el-form-item>
        <el-form-item label="图片宽度" prop="imageWidth">
          <!-- <el-input v-model="form.width" placeholder="请输入图片宽度" /> -->
          <el-input-number
            style="width: 200px"
            controls-position="right"
            placeholder="图标宽度"
            :max="999"
            :min="0"
            :step="1"
            v-model.number="form.imageWidth"
          />
          px
        </el-form-item>
        <el-form-item label="图片高度" prop="imageHeight">
          <!-- <el-input v-model="form.height" placeholder="请输入图片高度" /> -->
          <el-input-number
            style="width: 200px"
            controls-position="right"
            placeholder="图标高度"
            :max="999"
            :min="0"
            :step="1"
            v-model.number="form.imageHeight"
          />
          px
        </el-form-item>

        <!-- <el-form-item label="图片类型" prop="imageType">
          <el-input v-model="form.imageType" placeholder="请输入图片类型" />
        </el-form-item> -->
        <el-form-item label="图片分辨率" prop="vmsSize">
          <el-input v-model="form.vmsSize" placeholder="请输入图片分辨率" />
        </el-form-item>
        <el-form-item label="图片备注" prop="imageRemark">
          <el-input v-model="form.imageRemark" placeholder="请输入图片备注" />
        </el-form-item>
        <el-form-item label="速度" prop="speed">
          <el-input v-model="form.speed" placeholder="请输入速度" />
        </el-form-item>
        <el-form-item label="是否启用" prop="deleteflag">
          <el-switch
            v-model="form.deleteflag"
            active-color="#ff4949"
            inactive-color="#13ce66"
          >
          </el-switch>
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
  getTemplateImageList,
  deleteTemplateImage,
  editTemplateImage,
  addTemplateImage,
  getTemplateImageInfo,
  exportTemplateImage,
} from "@/api/board/templateimage";
import { loadPicture } from "@/api/equipment/type/api.js";
import $ from "jquery";
export default {
  name: "Vocabulary",
  components: {},
  data() {
    var checkWidth = (rule, value, callback) => {
      if (value <= 0 || value >= 1000) {
        callback(new Error("宽度大于0，小于1000"));
      } else {
        console.log(this.ruleForm, "this.ruleForm");
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("imageWidth");
        }
        callback();
      }
    };
    var checkHeight = (rule, value, callback) => {
      if (value <= 0 || value >= 1000) {
        callback(new Error("高度大于0，小于1000"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("imageHeight");
        }
        callback();
      }
    };
    return {
      boxShow:false,
      dialogImageUrl: "",
      dialogVisible: false,
      //需要移除的文件ids
      removeIds: [],
      fileData: "", // 文件上传数据（多文件合一）
      //图片放大缩小
      yn: false,
      //图片路径
      img: "",
      fileList: [],
      eqObj: { uploadDisabled: false },
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
      // 情报板敏感字管理表格数据
      vocabularyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        creatTime: null,
        pictureName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        pictureName: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" },
        ],
        imageWidth: [
          {
            required: true,
            message: "请输入图片宽度",
            trigger: ["blur", "change"],
          },
        ],
        imageHeight: [
          {
            required: true,
            message: "请输入图片高度",
            trigger: ["blur", "change"],
          },
        ],
        vmsSize: [{ required: true, message: "请输入分辨率", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.fileData = new FormData();
  },
  methods: {
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    getList() {
      this.loading = true;
      console.log(this.queryParams, "this.queryParams");
      getTemplateImageList(this.queryParams).then((response) => {
        console.log(response, "情报板列表");
        this.vocabularyList = response.rows;
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
        pictureName: null,
        pictureUrl: null,
        imageRemark: null,
        imageWidth: null,
        imageHeight: null,
        imageType: null,
        vmsSize: null,
        speed: null,
        deleteflag: null,
      };
      this.fileList = [];
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.title = "添加情报板模板图片";

      this.open = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      var that = this;
      that.fileList = [];
      getTemplateImageInfo(id).then((response) => {
        console.log(response, "修改情报板图片");
        this.form = response.data;
        this.title = "修改情报板图片";

        this.open = true;
        // this.planRoadmapUrl(that.form.iFileList);
        that.fileList.push({
          name: this.form.pictureName,
          url: this.form.pictureUrl,
        });
      });
    },
    async planRoadmapUrl(iFileList) {
      var that = this;
      that.fileList = [];
      for (let i = 0; i < iFileList.length; i++) {
        let iconName = iFileList[i].stateIconName;
        // let iconUrl = await that.picture(iFileList[i].url);
        let iconUrl = iFileList[i].url;
        that.fileList.push({
          name: iconName,
          url: iconUrl,
          fId: iFileList[i].id,
        });
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit();
      this.fileData.append("pictureName", this.form.pictureName);
      this.fileData.append("imageType", this.form.imageType);
      this.fileData.append("imageWidth", this.form.imageWidth);
      this.fileData.append("imageHeight", this.form.imageHeight);
      this.fileData.append("vmsSize", this.form.vmsSize);
      this.fileData.append(
        "imageRemark",
        this.form.imageRemark == null ? "" : this.form.imageRemark
      );
      this.fileData.append(
        "speed",
        this.form.speed == null ? "" : this.form.speed
      );
      this.fileData.append(
        "deleteflag",
        this.form.deleteflag == false ? "0" : "1"
      );
      console.log(this.fileData);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (parseInt(this.form.imageWidth) == 0) {
            this.$modal.msgError("图片宽度不能为0");
            return;
          }
          if (parseInt(this.form.imageHeight) == 0) {
            this.$modal.msgError("图片高度不能为0");
            return;
          }
          if (this.form.id != null) {
            this.fileData.append("id", this.form.id);
            this.fileData.append("pictureUrl", this.form.pictureUrl);
            this.fileData.append("removeIds", this.removeIds);
            editTemplateImage(this.fileData).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.upload.clearFiles();
              this.getList();
            });
          } else {
            addTemplateImage(this.fileData).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.$refs.upload.clearFiles();
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return deleteTemplateImage(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      //查看当前ids是否存在,如果存在。则按照当前ids进行导出。
      queryParams.ids = this.ids;
      this.$confirm("是否确认导出情报板模板图片数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportTemplateImage(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          queryParams.ids = null;
        });
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
      if (fileList.length >= 1) {
        this.eqObj.uploadDisabled = true;
        this.$set(this.eqObj, "uploadDisabled", true);
      } else {
        this.eqObj.uploadDisabled = false;
        this.$set(this.eqObj, "uploadDisabled", false);
      }
      this.$forceUpdate();
    },
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
      this.eqObj.uploadDisabled = false;
      this.$forceUpdate();
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    uploadSuccess() {
      console.log("成功了");
      this.$refs.upload.clearFiles();
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning("限制上传图片个数不超过1个");
    },
    /* 请求图片base64地址*/
    picture(fileUrl) {
      return new Promise((resolve, reject) => {
        loadPicture({
          url: fileUrl,
        }).then((response) => {
          if (response.code == 200) {
            let base64 = response.msg;
            resolve(base64); //不可缺少
          }
        });
      });
      return resolve(base64);
    },
    openImg(url) {
      this.img = url;
      this.yn = !this.yn;
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
<style scoped lang="scss">
::v-deep .el-dialog__header {
  padding: 20px;
  padding-bottom: 20px;
}
.searchBox {
  position: absolute;
  top: 8%;
  right: 1%;
  width: 24%;
  z-index: 1996;
  background-color: #00335a;
  padding: 20px;
  box-sizing: border-box;
  ::v-deep .el-form-item__content {
    width: 80%;
    .el-select {
      width: 100%;
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
}
</style>
