<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:configuration:add']"
          >新增</el-button
        >
        <!--          <el-button-->
        <!--            type="primary"-->
        <!--            plain-->
        <!--            size="mini"-->
        <!--            :disabled="single"-->
        <!--            @click="handleUpdate"-->
        <!--            v-hasPermi="['system:configuration:edit']"-->
        <!--            >修改</el-button-->
        <!--          >-->
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:configuration:remove']"
          >删除</el-button
        >
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:configuration:export']"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.sdName"
            placeholder="请输入名称，回车搜索"
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
        label-width="80px"
      >
        <el-form-item label="环境类型" prop="environmentType">
          <el-select
            v-model="queryParams.environmentType"
            placeholder="请选择环境类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in dict.type.environment"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-select
            v-model="queryParams.direction"
            placeholder="请选择方向"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in dict.type.direction"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <!-- <el-input
              v-model="queryParams.direction"
              placeholder="请输入方向"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            /> -->
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
      :data="configurationList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      height="62vh"
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
      <!-- <el-table-column label="id" align="center" prop="id" /> -->
      <el-table-column label="名称" align="center" prop="sdName" />
      <!-- <el-table-column label="图片" align="center" prop="url">
        <template slot-scope="scope">
          <img
            :src="scope.row.url"
            @click="openImg(scope.row.url)"
            style="
              width: 120px;
              height: 30px;
              max-width: 100%;
              max-height: 100%;
            "
          />
        </template>
      </el-table-column> -->
      <el-table-column label="环境类型" align="center" prop="environmentType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.environment"
            :value="scope.row.environmentType"
          />
        </template>
      </el-table-column>
      <el-table-column label="图片宽度(px)" align="center" prop="width" />
      <el-table-column label="图片高度(px)" align="center" prop="height" />
      <el-table-column label="方向" align="center" prop="direction">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.direction"
            :value="scope.row.direction"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
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
            v-hasPermi="['system:configuration:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:configuration:remove']"
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
    <el-dialog
      title="查看图片"
      :visible="yn"
      width="500px"
      @close="openImg"
      :close-on-click-modal="false"
    >
      <img
        :src="img"
        style="width: 200%; height: 200%; max-width: 100%; max-height: 100%"
      />
    </el-dialog>
    <!-- 添加或修改隧道环境配置对话框 -->
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
        <el-form-item label="名称" prop="sdName">
          <el-input v-model="form.sdName" placeholder="请输入上传名称" />
        </el-form-item>
        <el-form-item label="环境类型" prop="environmentType">
          <el-select
            v-model="form.environmentType"
            placeholder="请选择环境类型"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.environment"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片路径" prop="url">
          <!-- <el-input v-model="form.url" placeholder="请输入图片路径" /> -->
          <el-upload
            id="file"
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
            :limit="1"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog
            :visible.sync="dialogVisible"
            append-to-body
            style="width: 600px !important; margin: 0 auto"
            class="environmentPicDialog"
            :close-on-click-modal="false"
          >
            <div class="dialogStyleBox">
              <div class="dialogLine"></div>
              <div class="dialogCloseButton"></div>
            </div>
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>
        </el-form-item>
        <el-form-item label="图片宽度" prop="width">
          <!-- <el-input v-model="form.width" placeholder="请输入图片宽度" /> -->
          <el-input-number
            style="width: 200px"
            controls-position="right"
            placeholder="图标宽度"
            :max="999"
            :min="0"
            :step="1"
            v-model.number="form.width"
          />
          px
        </el-form-item>
        <el-form-item label="图片高度" prop="height">
          <!-- <el-input v-model="form.height" placeholder="请输入图片高度" /> -->
          <el-input-number
            style="width: 200px"
            controls-position="right"
            placeholder="图标高度"
            :max="999"
            :min="0"
            :step="1"
            v-model.number="form.height"
          />
          px
        </el-form-item>

        <el-form-item label="方向" prop="direction">
          <!-- <el-input v-model="form.direction" placeholder="请输入方向" /> -->
          <el-select
            v-model="form.direction"
            placeholder="请选择方向"
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.direction"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
  listConfiguration,
  getConfiguration,
  delConfiguration,
  addConfiguration,
  updateConfiguration,
  exportConfiguration,
} from "@/api/equipment/configuration/configuration";
import { loadPicture } from "@/api/equipment/type/api.js";
import $ from "jquery";
export default {
  name: "Configuration",
  dicts: ["environment", "direction"],
  data() {
    var checkWidth = (rule, value, callback) => {
      if (value <= 0 || value >= 1000) {
        callback(new Error("宽度大于0，小于1000"));
      } else {
        console.log(this.ruleForm, "this.ruleForm");
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("width");
        }
        callback();
      }
    };
    var checkHeight = (rule, value, callback) => {
      if (value <= 0 || value >= 1000) {
        callback(new Error("高度大于0，小于1000"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("height");
        }
        callback();
      }
    };
    return {
      boxShow: false,
      //图片放大缩小
      yn: false,
      //图片路径
      img: "",
      // 遮罩层
      loading: true,
      fileList: [],
      dialogImageUrl: "",
      fileData: "", // 文件上传数据（多文件合一）
      dialogVisible: false,
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
      // 隧道环境配置表格数据
      configurationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sdName: null,
        environmentType: null,
        direction: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sdName: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" },
        ],
        environmentType: [
          { required: true, message: "请选择环境类型", trigger: "change" },
        ],
        url: [{ required: true, message: "请上传图片", trigger: "change" }],
        width: [
          {
            required: true,
            message: "请输入图片宽度",
            trigger: ["blur", "change"],
          },
          // { validator: this.checkWidth, trigger: 'blur'},
        ],
        height: [
          {
            required: true,
            message: "请输入图片高度",
            trigger: ["blur", "change"],
          },
          // { validator: this.checkHeight, trigger: 'blur'},
        ],
      },
      //需要移除的文件ids
      removeIds: [],
      eqObj: { uploadDisabled: false },
    };
  },
  created() {
    this.getList();
    this.fileData = new FormData();
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
    /** 查询隧道环境配置列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listConfiguration(this.queryParams).then((response) => {
        this.configurationList = response.rows;
        /* this.configurationList.forEach((e) => {
          e.url = this.picture(e.iFileList[0].url);
          e.url.then((a) => {
            e.url = a;
          });
        }); */
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.upload.clearFiles();
      this.$refs.tableFile.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sdName: null,
        url: null,
        environmentType: null,
        width: null,
        height: null,
        direction: null,
        createBy: null,
        remark: null,
        createTime: null,
        updateTime: null,
      };
      this.fileList = [];
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
      this.queryParams.sdName = "";
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
      this.open = true;
      this.form.url = 1;
      this.eqObj.uploadDisabled = false;
      // this.fileList = [];
      // 每次点击新增重置上传图片数组
      this.title = "添加隧道环境配置";
    },
    uploadSuccess() {
      this.$refs.upload.clearFiles();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      var that = this;
      this.eqObj.uploadDisabled = true;
      getConfiguration(id).then((response) => {
        this.form = response.data;
        if (that.form.iFileList.length > 0) {
          that.planRoadmapUrl(that.form.iFileList);
        } else {
          this.eqObj.uploadDisabled = false;
        }
        this.open = true;
        this.title = "修改隧道环境配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      let that = this
      if (this.fileList.length < 1) {
        return this.$modal.msgWarning("请上传图片");
      }
      if (this.fileList[0].hasOwnProperty("size")) {
        const isLt100M = this.fileList.every(
          (file) => file.size / 1024 / 1024 < 1
        );
        if (!isLt100M) {
          this.$message.error("请检查，上传文件大小不能超过1MB!");
        } else {
          this.fileData = new FormData(); // new formData对象
          this.$refs.upload.submit();
          this.fileData.append("sdName", this.form.sdName);
          this.fileData.append("environmentType", this.form.environmentType);
          this.fileData.append("width", this.form.width);
          this.fileData.append("height", this.form.height);
          this.fileData.append("direction", this.form.direction);
          this.fileData.append("remark", this.form.remark);
          this.$refs["form"].validate((valid) => {
            if (valid) {
              if (this.form.id != null) {
                this.fileData.append("id", this.form.id);
                this.fileData.append("url", this.form.url);
                this.fileData.append("removeIds", this.removeIds);
                updateConfiguration(this.fileData).then((response) => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.$refs.tableFile.clearSelection();
                  this.$refs.upload.clearFiles();
                  this.handleQuery();
                });
              } else {
                addConfiguration(this.fileData).then((response) => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.$refs.upload.clearFiles();
                  this.getList();
                });
                this.eqObj.uploadDisabled = false;
              }
            }
          });
        }
      } else {
        this.fileData = new FormData(); // new formData对象
        this.$refs.upload.submit();
        this.fileData.append("sdName", this.form.sdName);
        this.fileData.append("environmentType", this.form.environmentType);
        this.fileData.append("width", this.form.width);
        this.fileData.append("height", this.form.height);
        this.fileData.append("direction", this.form.direction);
        this.fileData.append("remark", this.form.remark);
        this.$refs["form"].validate((valid) => {
          if (valid) {
            if (this.form.id != null) {
              this.fileData.append("id", this.form.id);
              this.fileData.append("url", this.form.url);
              this.fileData.append("removeIds", this.removeIds);
              updateConfiguration(this.fileData).then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.$refs.tableFile.clearSelection();
                this.$refs.upload.clearFiles();
                this.getList();
              });
            } else {
              addConfiguration(this.fileData).then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.$refs.upload.clearFiles();
                this.getList();
              });
              this.eqObj.uploadDisabled = false;
            }
          }
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this;
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delConfiguration(ids);
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
      let confirmInfo = "是否确认导出所有的环境配置数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的环境配置数据项？";
      }
      let that = this
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportConfiguration(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          that.$refs.tableFile.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {});
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
      if (fileList.length >= 1) {
        this.eqObj.uploadDisabled = false;
        //this.eqObj.uploadDisabled = true;
        this.$set(this.eqObj, "uploadDisabled", true);
      } else {
        this.eqObj.uploadDisabled = true;
        //   this.eqObj.uploadDisabled = false;
        this.$set(this.eqObj, "uploadDisabled", false);
      }
      this.$forceUpdate();
      // this.fileData.append("file", file.file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning("限制上传图片个数不超过1个");
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
      this.eqObj.uploadDisabled = false;
      this.$forceUpdate();
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
  },
};
</script>
<style>
.disabled .el-upload.el-upload--picture-card {
  display: none !important;
}
.disabled .el-button--success.is-plain {
  display: none !important;
}
.environmentPicDialog .el-dialog__headerbtn {
  top: 10px !important;
}
</style>

