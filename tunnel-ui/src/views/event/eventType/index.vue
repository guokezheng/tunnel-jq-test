<template>
  <div class="app-container">

    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          v-hasPermi="['system:type:add']"
          size="small"
          @click="handleAdd()"
        >新增类型
        </el-button>
        <el-button size="small" @click="resetQuery"
        >刷新</el-button
        >
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
              icon="icon-gym-Gsearch"
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
        <el-form-item label="防控类型" prop="prevControlType" >
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
        <el-form-item label="是否可用" prop="isUsable" >
          <el-select
            v-model="queryParams.isUsable"
            placeholder="请选择是否可用"
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

    <!--    <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="事件类型" prop="eventType">
            <el-input
              v-model="queryParams.eventType"
              placeholder="请输入事件类型"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
            &lt;!&ndash;        <el-select&ndash;&gt;
            &lt;!&ndash;          v-model="queryParams.eventType"&ndash;&gt;
            &lt;!&ndash;          placeholder="请选择事件类型"&ndash;&gt;
            &lt;!&ndash;          clearable&ndash;&gt;
            &lt;!&ndash;          size="small"&ndash;&gt;
            &lt;!&ndash;          style="width: 180px"&ndash;&gt;
            &lt;!&ndash;        >&ndash;&gt;
            &lt;!&ndash;          <el-option&ndash;&gt;
            &lt;!&ndash;            v-for="item in eventTypeData"&ndash;&gt;
            &lt;!&ndash;            :key="item.id"&ndash;&gt;
            &lt;!&ndash;            :label="item.eventType"&ndash;&gt;
            &lt;!&ndash;            :value="item.eventType"&ndash;&gt;
            &lt;!&ndash;          />&ndash;&gt;
            &lt;!&ndash;        </el-select>&ndash;&gt;
          </el-form-item>
          <el-form-item label="防控类型" prop="prevControlType">
            <el-select
              v-model="queryParams.prevControlType"
              placeholder="请选择防控类型"
              clearable
              size="small"
              style="width: 180px"
            >
              <el-option
                v-for="item in prevControlType"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="是否可用" prop="isUsable">
            <el-select
              v-model="queryParams.isUsable"
              placeholder="请选择是否可用"
              clearable
              size="small"
              style="width: 180px"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
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
        </el-form>-->

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
    <div class="tableTopHr" ></div>
    <el-table
      v-loading="loading"
      :data="eventTypeList"
      @selection-change="handleSelectionChange"
      class="allTable"
      height="62vh"
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
            width="25px"
            height="25px"
            class="pictureUrl"
          />
          　　
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority">
        <template slot-scope="scope">
          <span>{{getPriority(scope.row.priority)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否可用" align="center" prop="isUsable">
        <template slot-scope="scope">
          <span>{{scope.row.isUsable == "0" ? "否" : "是"}}</span>
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
        <el-form-item label="优先级" prop="priority">
          <el-select
            v-model="form.priority"
            placeholder="优先级"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="item in yxjOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否可用" prop="isUsable">
          <el-select
            v-model="form.isUsable"
            clearable
            placeholder="请选择是否可用"
            size="small"
            style="width: 100%">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="默认图标"  prop="iconUrl">
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
            :class="fileList.length ==1 ? 'showUpload':''"
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
      yxjOptions: [{
        value: '0',
        label: '低'
      },{
        value: '1',
        label: '中'
      },{
        value: '2',
        label: '高'
      },{
        value: '3',
        label: '紧急'
      }],
      sj_boxShow:false,
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
        prevControlType: null,
        isUsable: null
      },
      //事件类型
      eventTypeData: {},
      // 表单参数
      form: {simplifyName:"",prevControlType:"",isUsable:""},
      // 表单校验
      rules: {
        eventType: [
          { required: true, message: "请输入事件类型", trigger: "blur" },
        ],
        prevControlType: [
          { required: true, message: "请选择防控类型", trigger: "change" },
        ],
        isUsable: [
          { required: true, message: "请配置是否可用", trigger: "change" },
        ],
        simplifyName: [
          { required: true, message: "请输入简称", trigger: "blur" },
        ],
        iconUrl: [
          { required: true, message: "请上传默认图标", trigger: "blur" },
        ],
      },
      //是否可用
      options: [{
        value: '0',
        label: '否'
      }, {
        value: '1',
        label: '是'
      }],
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
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {

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
        isUsable: null,
        priority: ''
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
      this.queryParams.eventType = "";
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
      this.dialogOkDisabled = false;
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
      // this.dialogOkDisabled = true
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("eventType", this.form.eventType); //类型名称
      this.fileData.append("simplifyName", this.form.simplifyName); //类型名称
      this.fileData.append("prevControlType", this.form.prevControlType); //类型名称
      this.fileData.append("isUsable", this.form.isUsable)//是否可用
      this.fileData.append("priority", this.form.priority)//是否可用
      // this.fileData.append("uid", this.form.uid); //类型名称

      if (this.form.id == null) {
        this.form.iconUrl = -1;
      }

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
  },
};
</script>
<style scoped lang="scss">
::v-deep .showUpload {
    .el-upload {
      display: none !important;
    }
  }
</style>
