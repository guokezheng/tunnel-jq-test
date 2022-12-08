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
          @change="resolvingPowerType"
          v-model="queryParams.screenSize"
          placeholder="请选择屏幕尺寸"
          clearable
          size="small"
        >
          <el-option
            v-for="item in screenSizeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
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
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="addOrUpdateHandle"
          v-hasPermi="['system:templateConfig:add']"
          >新增</el-button
        >
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:templateConfig:edit']"-->
<!--          >修改</el-button-->
<!--        >-->
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:templateConfig:remove']"
          >删除</el-button
        >
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:templateConfig:export']"-->
<!--          >导出</el-button-->
<!--        >-->
      </el-form-item>
    </el-form>
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="addOrUpdateHandle"
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
    </el-row> -->
    <!-- 展示表格 -->
    <el-table
      v-loading="loading"
      :data="dataList"
      @selection-change="handleSelectionChange"
    :row-class-name="tableRowClassName"
    max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="序号" align="center" type="index" /> -->
      <el-table-column label="屏幕尺寸" align="center" prop="screenSize"  />
      <el-table-column label="入屏方式" align="center" prop="inScreenMode" :formatter="inScreenModeMatter" />
      <el-table-column label="滚动速度/毫秒" align="center" prop="tcontent.content" />
      <el-table-column label="停留时间/秒" align="center" prop="stopTime" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="addOrUpdateHandle(scope.row.id)"
            v-hasPermi="['system:templateConfig:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:templateConfig:remove']"
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
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update @refreshDataList="getList" ref="addOrUpdate" v-show="addOrUpdateVisible"></add-or-update>
  </div>
</template>
<script>
import addOrUpdate from './edit'
import {getTemplates} from "@/api/board/template";
import {deleteTemplate} from "@/api/board/template";
export default {
  name: 'vms-tiss-content-template',
  components: {
    addOrUpdate
  },
  data () {
    return {
      screenSizeList:[],//屏幕尺寸列表
      iotTemplateCategoryList:[],
        // 显示搜索条件
        showSearch: true,
        dataForm: {},
        dataList: [],//表格数据
        colorOptions: [
          {
            code: 'red',
            content: '红色',
          },
          {
              code: 'yellow',
              content: '黄色',
          },
          {
              code: 'White',
              content: '白色',
          },
          {
              code: 'GreenYellow',
              content: '绿色',
          },
        ],
        inScreenModeOptions: [
          {
          code: '0',
          name: '清屏（全黑)'
          }, {
          code: '1',
          name: '立即显示'
          }, {
          code: '2',
          name: '上移'
          }, {
          code: '3',
          name: '下移'
          }, {
          code: '4',
          name: '左移'
          }, {
          code: '5',
          name: '右移'
          }, {
          code: '6',
          name: '横百叶窗'
          }, {
          code: '7',
          name: '竖百叶窗'
          }, {
          code: '8',
          name: '上下合拢'
          }, {
          code: '9',
          name: '上下展开'
          }, {
          code: '10',
          name: '左右合拢'
          }, {
          code: '11',
          name: '左右展开'
          }, {
          code: '12',
          name: '中心合拢'
          }, {
          code: '13',
          name: '中心展开'
          }, {
          code: '14',
          name: '向下马赛克'
          }, {
          code: '15',
          name: '向右马赛克'
          }, {
          code: '16',
          name: '淡入'
          }, {
          code: '17',
          name: '淡出'
          }, {
          code: '18',
          name: '字符闪烁（闪后消失）'
          }, {
          code: '19',
          name: '字符闪烁（闪后停留）'
          }, {
          code: '20',
          name: '区域闪烁（闪后复原）'
          }, {
          code: '21',
          name: '区域闪烁（闪后区域为黑）'
          },
        ],
        fontTypeOptions: [
        {
            code: 'KaiTi',
            content: '楷体'
        },
        {
            code: 'SimSun',
            content: '宋体'
        },
        {
            code: 'FangSong',
            content: '仿宋'
        },
        {
            code: 'LiSu',
            content: '隶书'
        },
        ],
        order: '', // 排序，asc／desc
        orderField: '', // 排序，字段
        page: 1, // 当前页码
        limit: 10, // 每页数 limit 不能与字段重复
        total: 0, // 总条数
        dataListLoading: false, // 装载数据
        dataListSelections: [], // 数据列表，多选项
        addOrUpdateVisible: false, // 新增／更新，弹窗addOrUpdateVisible状态
        indexStart: 1,
        // 遮罩层
        loading: true,
         // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 查询参数
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            screenSize: null,
        },
      isCurrencyOptions: [
        {
          code: "0",
          content: "通用",
        },
        {
          code: "1",
          content: "仅为智能推荐模板",
        },
      ],
    }
  },
  created () {
    this.getList();
  },
  mounted(){
    // 屏幕尺寸字典数据
    this.getDicts("screenSize").then((res) => {
      this.screenSizeList = res.data;
      console.log(this.screenSizeList,'this.screenSizeList')
    });
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList,'this.iotTemplateCategoryList')
    });
  },
  methods: {
    init () {

    },
    /* 拆分分辨率大小 */
    resolvingPowerType(data) {
      let a = [];
      a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    getList () {
      this.indexStart = (this.page - 1) * this.limit + 1;
      this.loading = true;
      getTemplates(this.queryParams).then((res) => {
      // listTemplate(this.queryParams).then((res) => {
        this.dataList = res.rows;
        console.log(this.dataList)
        this.total = res.total * 1
        this.loading = false
      }).catch((err) => {
        this.loading = false;
        this.$modal.msgError(err);
      });

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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加情报板模板";
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
          return deleteTemplate(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    // 分页, 每页条数
    pageSizeChangeHandle (val) {
      this.page = 1;
      this.limit = val;
      this.getList()
    },
    // 分页, 当前页
    pageCurrentChangeHandle (val) {
      this.page = val;
      this.getList()
    },
    // checkbox改变
    dataListSelectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 排序
    dataListSortChangeHandle (data) {
      if (!data.order || !data.prop) {
        this.order = '';
        this.orderField = '';
        return false
      }
      this.order = data.order;
      this.orderField = data.prop;
      this.getList()
    },
    //  新增或修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true;
      console.log(id);
      this.$nextTick(() => {
        if(id > 0){
          this.$refs.addOrUpdate.dataForm.id = id;
        }else{
          this.$refs.addOrUpdate.dataForm.id = false;
        }
        console.log(this.$refs.addOrUpdate.dataForm.id)
        // this.$refs.addOrUpdate.isAdd =
        this.$refs.addOrUpdate.init()
      })
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
    /*颜色 */
    fontColorMatter (row, column) {
      var actions = []
      for (var i = 0; i < this.colorOptions.length; i++) {
        if (row.fontColor == this.colorOptions[i].code) {
          actions.push(this.colorOptions[i].content)
          break
        }
      }
      return actions.join('')
    },
    /* 字体类型 */
    fontTypeMatter (row, column) {
      var actions = []
      for (var i = 0; i < this.fontTypeOptions.length; i++) {
        if (row.fontType == this.fontTypeOptions[i].code) {
          actions.push(this.fontTypeOptions[i].content)
          break
        }
      }
      return actions.join('')
    },
    /* 出入屏方式 */
    inScreenModeMatter (row, column) {
      var actions = []
      for (var i = 0; i < this.inScreenModeOptions.length; i++) {
        if (row.inScreenMode == this.inScreenModeOptions[i].code) {
          actions.push(this.inScreenModeOptions[i].name)
          break
        }
      }
      return actions.join('')
    },
    // 模板类型字典翻译
    templateTypeFormat(row) {
      var a = ''
      this.isCurrencyOptions.filter(item => {
        if(item.code == row.templateType) {
          a = item.content
        }
      })
      return a
    },
    // 模板模式字典翻译
    isCurrencyFormat(row) {
      var a = ''
      this.isCurrencyOptions.filter(item => {
        if(item.code == row.isCurrency) {
          a = item.content
        }
      })
      return a
    },
    // 表格行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
  }
}
</script>
<style lang="scss">
</style>
