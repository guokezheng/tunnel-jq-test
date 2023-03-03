<template>
  <div class="app-container">
    <!--<el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类型名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入类型名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否可控" prop="isControl">
        <el-select v-model="queryParams.isControl" placeholder="是否可控" clearable size="small">
          <el-option
            v-for="dict in dict.type.sd_device_type_control"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" plain type="primary" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['device:bigType:add']"
        >新增
        </el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:bigType:edit']"
        >修改
        </el-button>
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:bigType:remove']"
        >删除
        </el-button>
      </el-form-item>
    </el-form>-->
    <!--全局搜索-->
    <el-row :gutter="20" style="margin: 10px 0 25px">
      <el-col :span="4">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['device:bigType:add']"
        >新增
        </el-button>
        <el-button
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:bigType:edit']"
        >修改
        </el-button>
        <el-button
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:bigType:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="6" :offset="14">
        <div class="grid-content bg-purple">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入设备类型名称,回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery">
          </el-input>
          </div>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange" class="allTable" >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" align="center" :index="indexMethod"></el-table-column>
      <!--<el-table-column label="类型ID" align="center" prop="id" />-->
      <el-table-column label="类型名称" align="center" prop="name"/>
      <el-table-column label="是否可控" align="center" prop="isControl">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_device_type_control" :value="scope.row.isControl"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['device:bigType:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:bigType:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入类型名称"/>
        </el-form-item>
        <el-form-item label="是否可控" prop="isControl">
          <el-select v-model="form.isControl" placeholder="请选择是否可控" style="width: 100%">
            <el-option
              v-for="dict in dict.type.sd_device_type_control"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
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
    listCategory,
    getCategory,
    delCategory,
    addCategory,
    updateCategory,
    exportCategory
  } from "@/api/equipment/bigType/category";

  export default {
    name: "Category",
    dicts: ['sd_device_type_control'],
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
        // 设备类型表格数据
        categoryList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
          isControl: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "设备类型名称不能为空", trigger: "blur"}
          ],
          isControl: [
            {required: true, message: "是否可控：1：是 0：否不能为空", trigger: "change"}
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      //翻页时不刷新序号
      indexMethod(index) {
        return index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      },
      /** 查询设备类型列表 */
      getList() {
        this.loading = true;
        listCategory(this.queryParams).then(response => {
          this.categoryList = response.rows;
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
          name: null,
          isControl: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null
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
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加设备类型";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCategory(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改设备类型";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateCategory(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCategory(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除设备类型编号为"' + ids + '"的数据项？').then(function () {
          return delCategory(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$modal.confirm('是否确认导出设备类型数据项？').then(() => {
          this.exportLoading = true;
          return exportCategory(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        }).catch(() => {
        });
      }
    }
  };
</script>
