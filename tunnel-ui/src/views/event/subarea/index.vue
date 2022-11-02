<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"

      v-show="showSearch"
      label-width="80px"
    >
      <el-form-item label="分区名称" prop="sName">
        <el-input
          v-model="queryParams.sName"
          placeholder="请输入分区名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择隧道"
          clearable
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelName"
            :label="item.tunnelName"
            :value="item.tunnelId"
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
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['tunnel:subarea:add']"
          >新增</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tunnel:subarea:edit']"
          >修改</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tunnel:subarea:remove']"
          >删除</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['tunnel:subarea:export']"
          >导出</el-button
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
          v-hasPermi="['tunnel:subarea:add']"
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
          v-hasPermi="['tunnel:subarea:edit']"
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
          v-hasPermi="['tunnel:subarea:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['tunnel:subarea:export']"
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
      :data="subareaList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="分区id" align="center" prop="sId" /> -->

      <!-- <el-table-column label="分区id" align="center" prop="sId" /> -->

      <el-table-column label="分区名称" align="center" prop="sName" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column
        label="方向"
        align="center"
        :formatter="eqDirectionFormat"
      />
      <el-table-column label="开始桩号" align="center" prop="startPile" />
      <el-table-column label="结束桩号" align="center" prop="endPile" />
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
            v-hasPermi="['tunnel:subarea:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tunnel:subarea:remove']"
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

    <!-- 添加或修改隧道分区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="530px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" align="left">
        <el-form-item label="分区名称" prop="sName">
          <el-input v-model="form.sName" placeholder="请输入分区名称" />
        </el-form-item>

        <!-- <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item> -->

        <!-- <el-form-item label="隧道id" prop="tunnelId">
          <el-input v-model="form.tunnelId" placeholder="请输入隧道id" />
        </el-form-item> -->
        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            class="tunnelName"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelName"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-select v-model="form.direction" placeholder="请选择方向" class="tunnelName" clearable>
            <el-option
              v-for="dict in dict.type.sd_direction"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始桩号" prop="startPile">
          <el-input v-model="form.startPile" placeholder="请输入开始桩号" @blur="setPileInt('start')"/>
        </el-form-item>
        <el-form-item label="开始桩号(整形)" prop="pileMin">
          <el-input v-model="form.pileMin" disabled="disabled" />
        </el-form-item>
        <el-form-item label="结束桩号" prop="endPile">
          <el-input v-model="form.endPile"  placeholder="请输入结束桩号" @blur="setPileInt('end')"/>
        </el-form-item>
        <el-form-item label="结束桩号(整形)" prop="pileMax">
          <el-input v-model="form.pileMax" disabled="disabled" />
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
// import { listSubarea, getSubarea, delSubarea, addSubarea, updateSubarea, exportSubarea } from "@/api/tunnel/subarea";

import {
  listSubarea,
  getSubarea,
  delSubarea,
  addSubarea,
  updateSubarea,
  exportSubarea,
} from "@/api/event/subarea";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "Subarea",
  dicts: [ 'sd_direction'],
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 隧道分区表格数据
      subareaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sName: null,
        tunnelId: null,
        direction: null,
        pileMin: null,
        pileMax: null,
        startPile: null,
        endPile: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sName: [
          { required: true, message: "请输入分区名称", trigger: "change" },
        ],
        tunnelId: [
          { required: true, message: "请选择隧道名称", trigger: "change" },
        ],
        direction: [
          { required: true, message: "请选择方向", trigger: "change" },
        ],
        startPile: [
          { required: true, message: "请输入开始桩号", trigger: "blur" },
        ],
        endPile: [
          { required: true, message: "请输入结束桩号", trigger: "blur" },
        ]
      },

      tunnelData: [],
    };
  },
  created() {
    this.getList();
    this.getTunnels();
  },

  methods: {
    setPileInt(param){
      if(param=='start'){
        let startPile = this.form.startPile;
        if (startPile == null) {
          return;
        }
        //var reg = startPile.replace(/[\u4e00-\u9fa5]/g, "");
        let pileInt = startPile.replace(/[^\u4e00-\u9fa50-9]/g, '')
        this.form.pileMin = pileInt;
      }else{
        let endPile = this.form.endPile;
        if (endPile == null) {
          return;
        }
        let pileInt = endPile.replace(/[^\u4e00-\u9fa50-9]/g, '')
        this.form.pileMax = pileInt;
      }
    },
    eqDirectionFormat(row, column) {
      return row.direction == 0 ? "上行" : "下行";
    },
    // 隧道名称 下拉框
    getTunnels() {
      listTunnels().then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, " this.tunnelData");
        console.log(this.subareaList, "this.subareaListthis.subareaList");
        // this.tunnelData.forEach((item,index)=>{
        //     this.subareaList.forEach((it,id)=>{
        //         if(item.tunnelId==it.tunnelId){
        //           it.tunnelId=item.tunnelName
        //         }
        //     })
        // })
      });
    },

    /** 查询隧道分区列表 */
    getList() {
      this.loading = true;
      listSubarea(this.queryParams).then((response) => {
        this.subareaList = response.rows;
        this.total = response.total;
        this.loading = false;
        console.log(response, "responseresponse");
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
        sId: null,
        sName: null,
        tunnelId: null,
        createBy: null,
        direction: null,
        pileMin: null,
        createTime: null,
        pileMax: null,
        updateBy: null,
        updateTime: null,
        startPile: null,
        endPile: null
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
      this.ids = selection.map((item) => item.sId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加隧道分区";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const sId = row.sId || this.ids;
      getSubarea(sId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改隧道分区";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if(!new RegExp('^[1-9][0-9]*$').test(this.form.pileMax) || !new RegExp('^[1-9][0-9]*$').test(this.form.pileMin) ){
            this.$modal.msgWarning("桩号格式输入有误！");
            return;
          }
          if (this.form.sId != null) {
            updateSubarea(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              // this.getTunnels()
            });
          } else {
            addSubarea(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              // this.getTunnels()
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const sIds = row.sId || this.ids;
      this.$modal
        .confirm('是否确认删除隧道分区编号为"' + sIds + '"的数据项？')
        .then(function () {
          return delSubarea(sIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
          // this.getTunnels()
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有隧道分区数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportSubarea(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
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
};
</script>


<style scoped>
.tunnelName {
  width: 378px;
}
</style>

