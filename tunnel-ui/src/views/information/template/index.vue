<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="屏幕尺寸" prop="screenSize">
        <el-select
          v-model="queryParams.screenSize"
          placeholder="请选择屏幕尺寸"
          clearable
          size="small"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="适用类型" prop="applyType">
        <el-select
          v-model="queryParams.applyType"
          placeholder="请选择适用类型"
          clearable
          size="small"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="模板类型" prop="templateType">
        <el-select
          v-model="queryParams.templateType"
          placeholder="请选择模板类型"
          clearable
          size="small"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="情报板类型" prop="vmsType">
        <el-select
          v-model="queryParams.vmsType"
          placeholder="请选择情报板类型"
          clearable
          size="small"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:template:add']"
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
          v-hasPermi="['system:template:edit']"
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
          v-hasPermi="['system:template:remove']"
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
          v-hasPermi="['system:template:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      ref="listTable"
      v-loading="loading"
      :data="templateList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="id" />
      <el-table-column label="屏幕尺寸" align="center" prop="screenSize" />
      <el-table-column label="入屏方式" align="center" prop="inScreenMode" />
      <el-table-column label="滚动速度/毫秒" align="center" prop="rollSpeed" />
      <el-table-column label="停留时间/秒" align="center" prop="stopTime" />
      <el-table-column label="适用类型" align="center" prop="applyType" />
      <el-table-column
        label="是否为通用模板"
        align="center"
        prop="isCurrency"
      />
      <el-table-column label="模板类型" align="center" prop="templateType" />
      <el-table-column label="情报板类型" align="center" prop="vmsType" />
      <el-table-column label="创建者" align="center" prop="createby" />
      <el-table-column label="创建时间" align="center" prop="createtime" />
      <el-table-column label="更新者" align="center" prop="updateby" />
      <el-table-column label="更新时间" align="center" prop="updatetime" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:template:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:template:remove']"
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

    <!-- 添加或修改情报板模板对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1050px"
      append-to-body
    >
      <el-form ref="form1" :rules="rules" label-width="80px">
        <fieldset class="field" style="position: relative">
          <legend>显示内容</legend>
          <div
            id="top_contents"
            style="overflow-y: auto; height: 15vh; border: solid 1px #5e96a3"

          >
            <el-row
              v-for="(item, index) in disContentList"
              :class="{ topBg: item.itemId == isactive }"
              :id="item.itemId"
              style="cursor: pointer; margin-bottom: 5px"
            >
              <el-col
                :span="1"
                style="padding-left: 5px"
                @click.native="infosRowClick(item)"
                >{{ index + 1 }}</el-col
              >
              <el-col :span="21" @click.native="infosRowClick(item)">{{
                item.disContent
              }}</el-col>
              <el-col :span="2">
                <el-button type="danger" plain @click="delCurrRow(item, index)"
                  >删除</el-button
                >
              </el-col>
            </el-row>
          </div>
          <el-row type="flex" justify="center" :gutter="20" style="margin-top:5px;">
            <el-button type="primary" plain @click="addCurrRow">添加</el-button>
            <el-button type="info" plain @click="alignment(6)"
              >下对齐</el-button
            >
            <el-button type="info" plain @click="alignment(5)"
              >上下居中</el-button
            >
            <el-button type="info" plain @click="alignment(4)"
              >上对齐</el-button
            >
            <el-button type="info" plain @click="alignment(3)"
              >右对齐</el-button
            >
            <el-button type="info" plain @click="alignment(2)"
              >左右居中</el-button
            >
            <el-button type="info" plain @click="alignment(1)"
              >左对齐</el-button
            >
          </el-row>
        </fieldset>
        <fieldset class="field">
          <legend>内容预览</legend>
          <div style="height: 17vh"></div>
        </fieldset>
        <fieldset class="field clearBootom">
          <legend>亮度设置</legend>
          <el-row type="flex" align="center">
            <el-col :span="8">
              <el-form-item label="调节方式:">
                <el-select
                  id="selectFontFamily"
                  name="selectFontFamily"
                  style="width: 6vw"
                  v-model="lightValue.mode"
                >
                  <el-option value="" label="请选择"></el-option>
                  <el-option value="0" label="自动调节"></el-option>
                  <el-option value="1" label="手动调节"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="亮度值:">
                <el-select style="width: 5.6vw" v-model="lightValue.brightness">
                  <el-option value="" label="请选择"></el-option>
                  <el-option
                    v-for="item in selectLightNumList"
                    :value="item.value"
                    :label="item.name"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-button
                type="primary"
                plain
                :loading="loadingLight"
                @click="readLight"
                >读取亮度
              </el-button>
              <!-- <el-button type="primary" style="margin-top: 5px;" disabled plain @click="setLight">设置亮度</el-button> -->
            </el-col>
          </el-row>
        </fieldset>
        <fieldset class="field">
          <legend>内容编辑</legend>
          <el-row type="flex">
            <el-col :span="3">
              <span style="margin-right: 5px">字体:</span>
              <el-select
                v-model="selectFontFamily"
                @change="boardAttrInfoChange"
                style="width: 80px"
              >
                <el-option
                  value="s"
                  selected="selected"
                  label="宋体"
                ></el-option>
                <el-option value="k" label="楷体"></el-option>
                <el-option value="h" label="黑体"></el-option>
                <el-option value="f" label="仿宋"></el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <span style="margin-right: 5px">大小:</span>
              <el-select
                v-model="selectFontSize"
                @change="boardAttrInfoChange"
                style="width: 80px"
              >
                <el-option
                  value="4848"
                  selected="selected"
                  label="48px"
                ></el-option>
                <el-option value="4040" label="40px"></el-option>
                <el-option value="3232" label="32px"></el-option>
                <el-option value="2424" label="24px"></el-option>
                <el-option value="1616" label="16px"></el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <span style="margin-right: 5px">颜色:</span>
              <el-select
                v-model="selectFontColor"
                @change="boardAttrInfoChange"
                style="width: 80px"
              >
                <el-option
                  value="255255000000"
                  selected="selected"
                  label="黄色"
                ></el-option>
                <el-option value="255255255000" label="白色"></el-option>
                <el-option value="255000000000" label="红色"></el-option>
                <el-option value="000255000000" label="绿色"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <span style="margin-right: 5px">速度:</span>
              <el-input
                disabled="disabled"
                @change="boardAttrInfoChange"
                v-model="selectInOutScreenSpeed"
                value="1"
                style="width: 80px"
              />
            </el-col>
            <el-col :span="4">
              <span style="margin-right: 5px">停留时间:</span>
              <el-input
                v-model="selectResTime"
                @change="boardAttrInfoChange"
                maxlength="4"
                value="500"
                style="width: 80px"
              />
            </el-col>
            <el-col :span="7">
              <span style="margin-right: 5px">出屏方式:</span>
              <el-select
                v-model="selectOutScreenMode"
                @change="boardAttrInfoChange"
                class="boardAttrInfoChange"
                style="width: 200px"
              >
                <el-option
                  value="0"
                  selected="selected"
                  label="清屏(全黑)"
                ></el-option>
                <el-option value="1" label="立即显示"></el-option>
                <el-option value="2" label="上移"></el-option>
                <el-option value="3" label="下移"></el-option>
                <el-option value="4" label="左移"></el-option>
                <el-option value="5" label="右移"></el-option>
                <el-option value="6" label="横百叶窗"></el-option>
                <el-option value="7" label="竖百叶窗"></el-option>
                <el-option value="8" label="上下合拢"></el-option>
                <el-option value="9" label="上下展开"></el-option>
                <el-option value="10" label="左右合拢"></el-option>
                <el-option value="11" label="左右展开"></el-option>
                <el-option value="12" label="中心合拢"></el-option>
                <el-option value="13" label="中心展开"></el-option>
                <el-option value="14" label="向下马赛克"></el-option>
                <el-option value="15" label="向右马赛克"></el-option>
                <el-option value="16" label="淡入"></el-option>
                <el-option value="17" label="淡出"></el-option>
                <el-option value="18" label="字符闪烁(闪后消失)"></el-option>
                <el-option value="19" label="字符闪烁(闪后停留)"></el-option>
                <el-option value="20" label="区域闪烁(闪后复原)"></el-option>
                <el-option
                  value="21"
                  label="区域闪烁(闪后区域为黑)"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col>
              <span style="margin-right: 5px">起始坐标:</span>
              x:
              <el-input
                v-model="startCoordTxt_x"
                maxlength="3"
                @change="boardAttrInfoChange"
                style="width: 60px"
              />
              y:
              <el-input
                v-model="startCoordTxt_y"
                maxlength="3"
                @change="boardAttrInfoChange"
                style="width: 60px"
              />
              <span style="margin: 0px 5px 0px 15px">内容:</span>
              <el-input
                type="textarea"
                v-model="showContentArea"
                @change="boardAttrInfoChange"
                value="欢迎拨打齐鲁交通热线"
                style="width: 670px"
              />
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="20">
              <el-input
                v-model="boardEidtContentArea"
                disabled="disabled"
                style="width: 800px; overflow-y: auto; resize: none"
              ></el-input>
            </el-col>
            <el-col :span="4">
              <el-button
                style="margin-top: 5px"
                type="primary"
                plain
                v-bind:disabled="saveBtnDisabled"
                @click="saveBtnClick()"
                >保存</el-button
              >
            </el-col>
          </el-row>
        </fieldset>
        <el-row type="flex" justify="center" style="width: 100%; margin-top: 5px;">
          <el-button
            id="boardPublishBtn"
            v-bind:disabled="boardPublishBtnDisabled"
            style="width: 30%;"
            type="primary"
            :loading="dloading"
            >保存
          </el-button>
          <el-button style="width: 30%" @click="boardFormClose">关闭</el-button>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTemplate,
  getTemplate,
  delTemplate,
  addTemplate,
  updateTemplate,
  exportTemplate,
} from "@/api/board/template";

export default {
  name: "Template",
  components: {},
  data() {
    return {
      dloading: false,
      boardPublishBtnDisabled: false,
      saveBtnDisabled: "",
      boardEidtContentArea: "",
      showContentArea: null,
      selectLightNumList: [],
      selectFontSize: null,
      selectFontColor: null,
      selectResTime: null,
      selectOutScreenMode: null,
      selectInOutScreenSpeed: null,
      startCoordTxt_x: null,
      startCoordTxt_y: null,
      selectFontFamily: null,
      loadingLight: false,
      selectLightNumList: [],
      // 亮度值
      lightValue: {
        mode: "",
        brightness: "",
        state: "",
      },
      disContentList: [],
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
      // 情报板模板表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        screenSize: null,
        applyType: null,
        templateType: null,
        vmsType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        isCurrency: [
          {
            required: true,
            message: "是否为通用模板不能为空",
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询情报板模板列表 */
    async getList() {
      this.loading = true;
      await listTemplate(this.queryParams).then((response) => {
        this.templateList = response.rows;
        this.total = response.total;
      });
      this.loading = false;
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
        screenSize: null,
        inScreenMode: null,
        rollSpeed: null,
        stopTime: null,
        applyType: null,
        isCurrency: null,
        templateType: null,
        vmsType: null,
        createby: null,
        createtime: null,
        updateby: null,
        updatetime: null,
        remark: null,
      };
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
      this.open = true;
      this.title = "添加情报板模板";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getTemplate(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改情报板模板";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateTemplate(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTemplate(this.form).then((response) => {
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
      const ids = row.id || this.ids;
      let content = '是否确认删除选中数据项?'
      if(ids == null || ids == undefined || ids == [] || ids == '') {
        content = '是否确认删除当前情报板模板?'
      }
      this.$confirm(
        content,
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delTemplate(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有情报板模板数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportTemplate(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
    //情报板添加内容
    addCurrRow() {
      this.boardPublishBtnDisabled = true;
      var newId = "ITEM000";
      var no = 1;
      if (this.disContentList.length > 0) {
        // 获取最后一个
        var lastElemId =
          this.disContentList[this.disContentList.length - 1].itemId;
        var lastId = lastElemId.substring(5, 7);
        newId = "ITEM" + this.formatNum(parseInt(lastId) + 1, 3);
      }
      var defalutContent = "";
      defalutContent = "300,0,0,\\C000000\\fh3232\\c255255000000请修改显示内容";
      this.addItemPropertyMap(
        newId,
        true,
        "300",
        "0",
        "0",
        "000000",
        "黑体",
        "3232",
        "黄色",
        "请修改显示内容"
      );
      this.addContentDisplayInfos(newId, "", defalutContent); //添加一条默认信息
      /* var scrollHeight = this.jquery('#top_contents').prop("scrollHeight");
        this.jquery('#top_contents').scrollTop(scrollHeight, 200); // 滚动条移至最底部 */
      var item = {};
      item.itemId = newId;
      item.disContent = defalutContent;
      this.infosRowClick(item);
    },
    /* 读取亮度 */
    readLight() {
      this.loadingLight = true;
      readBoardLightInfo(this.deviceId).then((response) => {
        this.loadingLight = false;
        if (response.code == 200) {
          this.$modal.msgSuccess("亮度获取成功");
          this.lightValue = response.data;
        } else {
          this.$modal.msgError("亮度获取失败");
        }
      });
    },
    boardAttrInfoChange() {
      if (
        this.showContentArea == "" ||
        this.showContentArea == undefined ||
        this.showContentArea == null
      ) {
        this.$message("发布内容不能为空");
        return;
      }
      var boardAttrInfoUpdate = true;
      if (this.checkCoordinates(this.startCoordTxt_x) == false) {
        boardAttrInfoUpdate = false;
      }
      if (this.checkCoordinates(this.startCoordTxt_y) == false) {
        boardAttrInfoUpdate = false;
      }
      if (boardAttrInfoUpdate == true) {
        this.changeApply(); //属性应用
        this.changePreview(); //属性预览
      }
    },
    //关闭drawer
    boardFormClose() {
      this.open = false;
    },
  },
};
</script>
<style lang="less" scoped>
.clearBootom {
  .el-form-item {
    margin: 0;
  }
}
</style>
