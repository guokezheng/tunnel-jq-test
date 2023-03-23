<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
          <el-button
            size="small"
            @click="handleAdd"
            v-hasPermi="['system:warehouse:add']"
          >新增</el-button>
<!--          <el-button-->
<!--            type="primary"-->
<!--            plain-->
<!--            size="mini"-->
<!--            :disabled="single"-->
<!--            @click="handleUpdate"-->
<!--            v-hasPermi="['system:warehouse:edit']"-->
<!--          >修改</el-button>-->
          <el-button
            size="small"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:warehouse:remove']"
          >删除</el-button>
          <el-button
            size="small"
            :loading="exportLoading"
            @click="handleExport"
            v-hasPermi="['system:warehouse:export']"
          >导出</el-button>
          <el-button size="small" @click="resetQuery"
          >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="14">
        <div  ref="main" class="grid-content bg-purple">
            <el-input
              v-model="queryParams.searchValue"
              placeholder="请输入备件名称、品牌、生产厂家,回车搜索"
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
          <el-form-item label="所属隧道" prop="tunnelId"  v-show="manageStatin == '0'">
            <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small">
              <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId" />
            </el-select>
          </el-form-item>

<!--          <el-form-item label="品牌" style="width: 100%" prop="brand">-->
<!--            <el-input-->
<!--              v-model="queryParams.brand"-->
<!--              placeholder="请输入品牌"-->
<!--              clearable-->
<!--              size="small"-->
<!--              @keyup.enter.native="handleQuery"-->
<!--            />-->
<!--          </el-form-item>-->
<!--          <el-form-item label="生产厂家" style="width: 100%" prop="manufacturer">-->
<!--            <el-input-->
<!--              v-model="queryParams.manufacturer"-->
<!--              placeholder="请输入生产厂家"-->
<!--              clearable-->
<!--              size="small"-->
<!--              @keyup.enter.native="handleQuery"-->
<!--            />-->
<!--          </el-form-item>-->
<!--          <el-form-item label="管理员" style="width: 100%" prop="keeper">-->
<!--            <el-input-->
<!--              v-model="queryParams.keeper"-->
<!--              placeholder="请输入管理员"-->
<!--              clearable-->
<!--              size="small"-->
<!--              @keyup.enter.native="handleQuery"-->
<!--            />-->
<!--          </el-form-item>-->
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
    <el-table v-loading="loading" :data="warehouseList" @selection-change="handleSelectionChange"
     height="62vh" class="allTable"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <el-table-column label="所属隧道" align="center" prop="tunnelName" min-width="100" show-overflow-tooltip/>
      <el-table-column label="备件名称" align="center" prop="partName" show-overflow-tooltip/>
      <el-table-column label="品牌" align="center" prop="brand" show-overflow-tooltip/>
      <el-table-column label="型号" align="center" prop="model" show-overflow-tooltip/>
      <el-table-column label="单位" align="center" prop="unit" show-overflow-tooltip/>
      <el-table-column label="生产厂家" align="center" prop="manufacturer" show-overflow-tooltip/>
      <el-table-column label="上次采购时间" align="center" prop="lastPurchaseTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastPurchaseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上次采购数量" align="center" prop="lastPurchaseQuantity" />
      <el-table-column label="上次采购单价" align="center" prop="lastPurchaseUnitPrice" />
      <el-table-column label="当前库存量" align="center" prop="currentInventory" />
      <el-table-column label="管理员" align="center" prop="keeper" />
      <el-table-column label="联系方式" align="center" prop="phone" />
      <el-table-column label="所在位置" align="center" prop="location" show-overflow-tooltip/>
      <el-table-column label="备注" align="center" prop="remake" show-overflow-tooltip/>
<!--      <el-table-column label="备注1" align="center" prop="remake1" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:warehouse:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:warehouse:remove']"
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

    <!-- 添加或修改备品备件库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" style="max-height:600px;overflow:auto">
        <el-form-item label="所属隧道" prop="eqTunnelId">
          <el-select v-model="form.tunnelId" placeholder="请选择所属隧道" style="width: 100%">
            <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName" :value="item.tunnelId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备件名称" prop="partName">
          <el-input v-model="form.partName" maxlength="100" placeholder="请输入备件名称" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" maxlength="100" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" maxlength="100" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" maxlength="100" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer">
          <el-input v-model="form.manufacturer" maxlength="100" placeholder="请输入生产厂家" />
        </el-form-item>
        <el-form-item label="上次采购时间" prop="lastPurchaseTime">
          <el-date-picker clearable size="small"
            v-model="form.lastPurchaseTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择上次采购时间"
            :picker-options="setoptions"
            style="width: 100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="上次采购数量" prop="lastPurchaseQuantity">
          <el-input v-model="form.lastPurchaseQuantity"  maxlength="100" placeholder="请输入上次采购数量" />
        </el-form-item>
        <el-form-item label="上次采购单价" prop="lastPurchaseUnitPrice">
          <el-input v-model="form.lastPurchaseUnitPrice"  maxlength="100" placeholder="请输入上次采购单价" />
        </el-form-item>
        <el-form-item label="当前库存量" prop="currentInventory">
          <el-input v-model="form.currentInventory" placeholder="请输入当前库存量" />
        </el-form-item>
        <el-form-item label="管理员" prop="keeper">
          <el-input v-model="form.keeper" maxlength="100" placeholder="请输入管理员" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" maxlength="20" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="所在位置" prop="location">
          <el-input v-model="form.location" maxlength="100" placeholder="请输入所在位置" />
        </el-form-item>
        <el-form-item label="备注" prop="remake">
          <el-input v-model="form.remake" maxlength="200" placeholder="请输入备注" />
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
import { listWarehouse, getWarehouse, delWarehouse, addWarehouse, updateWarehouse, exportWarehouse } from "@/api/equipment/warehouse/warehouse";
import {listTunnels} from "@/api/equipment/tunnel/api";
export default {
  name: "Warehouse",
  data() {
    return {
      boxShow: false,
      manageStatin:this.$cache.local.get("manageStation"),

      //所属隧道
      eqTunnelData: {},
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
      // 备品备件库表格数据
      warehouseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      setoptions: {
        // 时间不能大于当前时间
        disabledDate: time => {
          return time.getTime() > Date.now()
        },
        selectableRange: '00:00:00 - 23:59:59'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        partName: null,
        searchValue: '',
        brand: null,
        model: null,
        unit: null,
        manufacturer: null,
        lastPurchaseTime: null,
        lastPurchaseQuantity: null,
        lastPurchaseUnitPrice: null,
        currentInventory: null,
        keeper: null,
        phone: null,
        location: null,
        remake: null,
        remake1: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        partName: [{
          required: true,
          message: "请输入备件名称",
          trigger: "blur"
        }],
        brand: [{
          required: true,
          message: "请输入品牌",
          trigger: "blur"
        }],
        model: [{
          required: true,
          message: "请输入型号",
          trigger: "blur"
        }],
        unit: [{
          required: true,
          message: "请输入单位",
          trigger: "blur"
        }],
        lastPurchaseUnitPrice: [{
          required: true,
          message: "请输入上次采购单价",
          trigger: "blur"
        }],
        currentInventory: [{
          required: true,
          message: "请输入当前库存量",
          trigger: "blur"
        }],
        keeper: [{
          required: true,
          message: "请输入管理员",
          trigger: "blur"
        }],
        phone: [{
          required: true,
          message: "请输入管理员联系方式",
          trigger: "blur"
        },
        { pattern: /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/, message: '请输入正确联系方式', trigger: 'blur' },
      ],
        location: [{
          required: true,
          message: "请输入备品所在位置",
          trigger: "blur"
        }],
      }
    };
  },
  created() {
    this.getList();
    this.getTunnel();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
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
    /** 所属隧道 */
    getTunnel() {
      if(this.$cache.local.get("manageStation") == '1'){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows,"所属隧道列表")
        this.eqTunnelData = response.rows;
      });
    },
    /** 查询备品备件库列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      if(this.manageStatin == '1'){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listWarehouse(this.queryParams).then(response => {
        this.warehouseList = response.rows;
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
        partName: null,
        brand: null,
        model: null,
        unit: null,
        manufacturer: null,
        lastPurchaseTime: null,
        lastPurchaseQuantity: null,
        lastPurchaseUnitPrice: null,
        currentInventory: null,
        keeper: null,
        phone: null,
        location: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remake: null,
        remake1: null
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
      this.queryParams.ids=[];
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
      this.title = "添加备品备件库";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWarehouse(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改备品备件库";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWarehouse(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWarehouse(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() {
        return delWarehouse(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出备品备件数据项？').then(() => {
        this.exportLoading = true;
        return exportWarehouse(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.getTunnel();
    }
  }
};
</script>
