<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          type="primary"
          plain
          size="small"
          @click="handleAdd"
          v-hasPermi="['eqType:item:add']"
        >新增</el-button>
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['eqType:item:edit']"-->
<!--        >修改</el-button>-->
        <el-button
          type="primary"
          plain
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['eqType:item:remove']"
        >删除</el-button>
        <el-button
          type="primary"
          plain
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['eqType:item:export']"
        >导出</el-button>
        <el-button size="small" @click="resetQuery" type="primary" plain
          >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.searchValue"
            placeholder="请输入数据项编号、数据项名称，回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="icon-gym-Gsearch"
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
<!--        <el-form-item label="数据项名称" style="width: 100%" prop="itemName">-->
<!--          <el-input-->
<!--            v-model="queryParams.itemName"-->
<!--            placeholder="请输入数据项名称"-->
<!--            clearable-->
<!--            size="small"-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
        <!-- <el-form-item label="设备类型id" prop="deviceTypeId">
          <el-input
            v-model="queryParams.deviceTypeId"
            placeholder="请输入设备类型id"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
        <el-form-item label="设备类型"style="width: 100%" prop="deviceTypeId">
                <el-select v-model="queryParams.deviceTypeId" placeholder="请选择设备类型" clearable>
                  <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId">
                  </el-option>
                </el-select>
              </el-form-item>
<!--        <el-form-item label="单位名称" style="width: 100%" prop="unit">-->
<!--          <el-input-->
<!--            v-model="queryParams.unit"-->
<!--            placeholder="请输入单位名称"-->
<!--            clearable-->
<!--            size="small"-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
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
    <div class="tableTopHr" ></div>
    <el-table v-loading="loading" :data="itemList" @selection-change="handleSelectionChange"
    class="allTable" height="59vh"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <el-table-column label="数据项编号" align="center" prop="itemCode" />
      <el-table-column label="数据项名称" align="center" prop="itemName" />
      <el-table-column label="设备类型" align="center" prop="typeName">
      </el-table-column>

      <el-table-column label="单位名称" align="center" prop="unit" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['eqType:item:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['eqType:item:remove']"
          >删除</el-button>
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

    <!-- 添加或修改设备类型数据项对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="数据项编号" prop="itemCode">
          <el-input v-model="form.itemCode" placeholder="请输入数据项编号" />
        </el-form-item>
        <el-form-item label="数据项名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入数据项名称" />
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceTypeId" >
              <el-select v-model="form.deviceTypeId" placeholder="请选择设备类型" class="deviceTypeId" >
                <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId">
                </el-option>
              </el-select>
            </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listItem, getItem, delItem, addItem, updateItem, exportItem } from "@/api/equipment/eqTypeItem/item";
import { listType } from "@/api/equipment/type/api";
export default {
  name: "Item",
  data() {
    return {
      boxShow: false,
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
      // 设备类型数据项表格数据
      itemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        searchValue: '',
        pageSize: 10,
        itemCode: null,
        itemName: null,
        deviceTypeId: null,
        unit: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
          itemCode: [
          {
              required: true,
              message: "数据项编号不能为空",
              trigger: "blur"
            },
            { validator: this.checkData, trigger: 'blur' }
            // {
            //   pattern: /^[0-9]*$/,
            //   message: "数据项编号需为数字",
            //   trigger: "blur"
            // },
          ],
          itemName: [
            { required: true, message: '请输入数据项名称', trigger: 'change' }
          ],
          deviceTypeId: [
            { required: true, message: '请输入设备类型ID', trigger: 'change' }
          ],
          // unit: [
          //   { required: true, message: '请输入单位', trigger: 'change' }
          // ],
          // remark: [
          //   { required: true, message: '请输入备注', trigger: 'change' }
          // ],
      },
      eqTypeData:[]//设备类型
    };
  },
  created() {
    this.getList();
  },
  mounted(){
    this.getEqType();
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
//数据项编号校验不能输入中文
    checkData(rule, value, callback) {
      if (value) {
        if (/[\一-\龥]/g.test(value)) {
          callback(new Error('不能为中文!'))
        } else {
          callback()
        }
      }
      callback()
    },
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
     /** 设备类型 */
     getEqType() {
        listType().then((response) => {
          this.eqTypeData = response.rows;
          console.log(this.eqTypeData,'eqTypeDataeqTypeDataeqTypeDataeqTypeData')
          // this.itemList.forEach((item,index)=>{
          //   this.eqTypeData.forEach((it,id)=>{
          //       if(item.deviceTypeId == it.typeId){
          //         item.deviceTypeId= it.typeName
          //       }
          //   })
          // })
          console.log(this.itemList,'itemListitemListitemListitemList')
        });
      },
    /** 查询设备类型数据项列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listItem(this.queryParams).then(response => {
        // console.log(response,'responseresponse')
        this.itemList = response.rows;
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
        itemCode: null,
        itemName: null,
        deviceTypeId: null,
        unit: null,
        remark: null,
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
      this.queryParams.searchValue = '';
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备类型数据项";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getItem(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备类型数据项";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getEqType()
            });
          } else {
            addItem(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.getEqType()
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除所选的设备类型数据项？').then(function() {
        return delItem(ids);
      }).then(() => {
        this.getList();
        this.getEqType()
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出设备类型数据项数据项？').then(() => {
        this.exportLoading = true;
        return exportItem(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
  }
};
</script>
<style scoped>
 .deviceTypeId{
  width: 100%;
 }
</style>

