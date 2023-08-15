<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:type:add']"
          >新增</el-button
        >
        <!--          <el-button-->
        <!--            type="primary"-->
        <!--            plain-->
        <!--            size="mini"-->
        <!--            :disabled="single"-->
        <!--            @click="handleUpdate"-->
        <!--            v-hasPermi="['system:type:edit']"-->
        <!--            >修改</el-button-->
        <!--          >-->
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:type:remove']"
          >删除</el-button
        >
        <el-button size="small" :loading="exportLoading" @click="handleExport"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="14">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            v-model="queryParams.typeName"
            placeholder="请输入设备类型名称、设备类型代号，回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            style="border-right: solid 1px #00c8ff; border-radius: 3px"
          >
            <!-- <el-button
              slot="append"
              class="searchTable"
              @click="boxShow = !boxShow"
            ></el-button> -->
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div ref="cc" class="searchBox searchBoxMini" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="100px"
      >
        <el-form-item label="设备类型名称" prop="typeName">
          <el-input
            v-model="queryParams.typeName"
            placeholder="请输入设备类型名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="设备类型代号" prop="typeAbbr">
          <el-input
            v-model="queryParams.typeAbbr"
            placeholder="请输入设备类型代号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" @click="handleQuery">搜索</el-button>
          <el-button size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="typeList"
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
      <el-table-column label="设备类型名称" align="center" prop="typeName" />
      <el-table-column label="设备类型代号" align="center" prop="typeAbbr" />
      <el-table-column label="是否可控" align="center" prop="isControl">
        <template slot-scope="scope">
          <span>{{ scope.row.isControl == 1 ? "是" : "否" }} </span>
        </template>
      </el-table-column>
      <el-table-column label="图标宽度(px)" align="center" prop="iconWidth">
        <template slot-scope="scope">
          <span>{{ scope.row.iconWidth }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图标高度(px)" align="center" prop="iconHeight">
        <template slot-scope="scope">
          <span>{{ scope.row.iconHeight }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!--          <el-button-->
          <!--            v-show="scope.row.typeId==31"-->
          <!--            size="mini"-->
          <!--            class="tableBlueButtton"-->
          <!--            @click="configData(scope.row)"-->
          <!--            v-hasPermi="['system:type:edit']"-->
          <!--            >配置参数</el-button-->
          <!--          >-->
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:type:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:type:remove']"
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

    <!-- 添加或修改设备类型对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      class="eqTypeDialog"
      :close-on-click-modal="false"
      :before-close="cancel"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="设备类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入设备类型名称" />
        </el-form-item>
        <el-form-item label="设备类型代号" prop="typeAbbr">
          <el-input v-model="form.typeAbbr" placeholder="请输入设备类型代号" />
          <!-- <div style="color: #9c9c9c;font-size: 12px;line-height: 20px;">* 设备类型代号只能输入数字、字母、下划线</div> -->
        </el-form-item>
        <el-form-item label="是否可控" prop="isControl">
          <el-select
            v-model="form.isControl"
            placeholder="请选择是否可控"
            style="width: 100%"
          >
            <el-option
              v-for="dict in isControlOptions"
              :key="dict.dictSort"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图标宽度" prop="iconWidth">
          <el-input
            v-model="form.iconWidth"
            placeholder="图标宽度"
            maxlength="3"
            style="width: 100px"
          />
          px
        </el-form-item>
        <el-form-item label="图标高度" prop="iconHeight">
          <el-input
            v-model="form.iconHeight"
            placeholder="图标高度"
            maxlength="3"
            style="width: 100px"
          />
          px
        </el-form-item>
        <el-form-item label="默认图标" prop="upload">
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
            :limit="2"
            :class="fileList.length >= 2 ? 'showUpload' : ''"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog
            :visible.sync="dialogVisible"
            class="modifyEqTypeDialog"
            :append-to-body="true"
            style="width: 600px !important; margin: 0 auto"
            :close-on-click-modal="false"
          >
            <div class="dialogStyleBox">
              <div class="dialogLine"></div>
              <div class="dialogCloseButton"></div>
            </div>
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>
        </el-form-item>
        <el-form-item label="所属大类" prop="eqCategory">
          <!--<el-select
            v-model="form.eqCategory"
            placeholder="请选择所属大类"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in eqCategoryData"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>-->

          <el-select
            v-model="form.eqCategory"
            placeholder="请选择所属大类"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in eqCategoryData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属系统" prop="eqSystem">
          <el-select
            v-model="form.eqSystem"
            placeholder="请选择所属系统"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in eqSystemData"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="所属模块"
          class="checkboxFormDialog"
          prop="bigType"
        >
          <el-checkbox-group v-model="form.bigType">
            <el-row>
              <el-col
                :span="8"
                v-for="(dict, index) in bigTypeOptions"
                :key="index"
              >
                <el-checkbox :label="dict.dictValue">{{
                  dict.dictLabel
                }}</el-checkbox>
              </el-col>
            </el-row>
          </el-checkbox-group>
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
  listType,
  getType,
  delType,
  addType,
  updateType,
  // groupByBigType,
  eqTypeList,
  loadPicture,
  exportDeviceIcon,
} from "@/api/equipment/type/api.js";
import { listCategory } from "@/api/equipment/bigType/category";
export default {
  name: "Type",
  data() {
    const validateImage = (rule, value, callback) => {
      this.fileData.forEach((value, key) => {
        if (key != "file") {
          callback(new Error("请先上传文件"));
        } else {
          callback();
        }
      });
    };
    return {
      boxShow: false,
      // 导出遮罩层
      exportLoading: false,
      eqSystemData: {},
      // 设备大类
      eqCategoryData: {},
      bigType: "",
      eqCategory: "",
      eqSystem: "",
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
      bigTypeOptions: [],
      // 总条数
      total: 0,
      // 设备类型表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      form: {
        bigType: [],
        eqCategory: "",
        eqSystem: "",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        typeName: null,
      },
      // 表单参数
      form1: {
        bigType: [],
      },

      // 表单校验
      rules: {
        // upload: [
        //   {
        //     trigger: "change",
        //     required: true,
        //     validator: validateImage,
        //   },
        // ],
        typeName: [
          { required: true, message: "设备类型不能为空", trigger: "blur" },
        ],
        typeAbbr: [
          { required: true, message: "设备代号不能为空", trigger: "blur" },
          {
            pattern: /^\w+$/,
            message: "只能输入数字、字母、下划线",
          },
        ],
        iconWidth: [
          { required: true, message: "图标宽度不能为空", trigger: "blur" },
          {
            pattern: /^[1-9]\d*$/,
            message: "请输入0以上正整数",
            trigger: "blur",
          },
        ],
        iconHeight: [
          { required: true, message: "图标高度不能为空", trigger: "blur" },
          {
            pattern: /^[1-9]\d*$/,
            message: "请输入0以上正整数",
            trigger: "blur",
          },
        ],
        bigType: [
          { required: true, message: "请选择所属模块", trigger: "change" },
        ],
        eqSystem: [
          { required: true, message: "请选择所属系统", trigger: "change" },
        ],
        eqCategory: [
          { required: true, message: "请选择所属大类", trigger: "change" },
        ],
        isControl: [
          { required: true, message: "是否可控不能为空", trigger: "change" },
        ],
      },
      //是否区分方向
      direction: "",
      dialogImageUrl: "",
      dialogVisible: false,
      fileData: "", // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      //需要移除的文件ids
      removeIds: [],
      // fIds:[]//需要移除的关联图标iconFileId
      // typeList: [], //所属分类
      isControlOptions: [], //是否可控字典值
    };
  },
  created() {
    this.getDicts("eq_system").then((response) => {
      this.eqSystemData = response.data;
    });
    /*this.getDicts("eq_category").then(response => {
      this.eqCategoryData = response.data;
    })*/ this.getDicts("sd_sys_name").then((response) => {
      console.log(response.data, "所属系统");
      this.bigTypeOptions = response.data;
    });
    this.getEqBigType();
    this.getList();
    // this.getGroupByBigType();
    this.fileData = new FormData(); // new formData对象
    this.getDicts("sys_type_control").then((response) => {
      this.isControlOptions = response.data;
      console.log(this.isControlOptions, "this.isControlOptions");
    });
  },
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.typeId;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (self.boxShow) {
        if (
          !this.$refs.main.contains(e.target) &&
          !this.$refs.cc.contains(e.target)
        ) {
          if (self.boxShow == true) {
            self.boxShow = false;
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
    getEqBigType() {
      listCategory().then((response) => {
        this.eqCategoryData = response.rows;
      });
    },
    configData() {
      this.$router.push({
        path: "/inductionLamp",
      });
    },
    // getGroupByBigType() {
    //   groupByBigType().then((response) => {
    //     // console.log(response.rows,"列表")
    //     this.typeList = response.rows;
    //   });
    // },
    //删除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
    },

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
      this.$refs.form.clearValidate("upload");
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
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
        // fileList.push(this.form.iFileList[i].url)
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

    /** 查询设备类型列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listType(this.queryParams).then((response) => {
        console.log(response.rows, "列表");
        this.typeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        typeId: null,
        typeName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        bigType: [],
      };
      this.fileList = [];
      this.removeIds = [];
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
      this.queryParams.ids = "";
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.typeId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      console.log(selection, "selection");
      console.log(this.ids, "this.ids");
      console.log(this.single, "this.single");
      console.log(this.multiple, "this.multiple");
    },

    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的设备图标数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的设备图标数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportDeviceIcon(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {});
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      var that = this;
      that.reset();
      this.form = {
        bigType: [],
      };
      const typeId = row.typeId || that.ids;
      getType(typeId).then((response) => {
        console.log(response, "xiugai");
        var resultData = response.data;
        if (resultData.bigType != null) {
          resultData.bigType = resultData.bigType.split(",");
        } else {
          resultData.bigType = [];
        }
        this.form = resultData;
        that.planRoadmapUrl(that.form.iFileList);
        this.open = true;
        this.title = "修改设备类型";
      });
    },
    /** 提交按钮 */
    submitForm() {
      let that = this;
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      if ((this.fileList = [])) {
        this.fileData.append("file", null); // append增加数据
      }
      this.fileData.append("typeName", this.form.typeName); //类型名称
      this.fileData.append("typeAbbr", this.form.typeAbbr);
      this.fileData.append("iconWidth", this.form.iconWidth);
      this.fileData.append("iconHeight", this.form.iconHeight);
      this.fileData.append("isControl", this.form.isControl);
      this.fileData.append("bigType", this.form.bigType.join(","));
      this.fileData.append("eqSystem", this.form.eqSystem);
      this.fileData.append("eqCategory", this.form.eqCategory);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.typeId != null) {
            this.fileData.append("typeId", this.form.typeId); //类型id
            this.fileData.append("iconFileId", this.form.iconFileId); //关联文件id
            this.fileData.append("removeIds", this.removeIds);
            updateType(this.fileData).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              that.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addType(this.fileData).then((response) => {
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
      let that = this;
      const typeIds = row.typeId || this.ids;
      // const iconFileIds = row.iconFileId || this.fIds;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delType(typeIds);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
          console.log();
        })
        .catch(function () {
          that.$refs.tableFile.clearSelection();
        });
    },
  },
};
</script>
<style scoped lang="scss">
::v-deep .checkboxFormDialog .el-checkbox {
  width: 80px;
}

::v-deep .showUpload {
  .el-upload {
    display: none;
  }
}
.modifyEqTypeDialog .el-dialog__headerbtn {
  top: 10px !important;
}
::v-deep .el-dialog__headerbtn {
  top: 14px !important;
}
::v-deep .el-dialog__header {
  padding: 20px;
}
</style>
<style lang="scss">
.eqTypeDialog {
  .el-dialog__body {
    max-height: 700px;
    overflow: auto;
  }
  ::-webkit-scrollbar {
    width: 0px;
  }
}
</style>

