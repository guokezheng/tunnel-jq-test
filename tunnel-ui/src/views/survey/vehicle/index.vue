<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <div>
      <el-row class="topFormRow" :gutter="20">
        <el-col :span="6">
          <el-button size="small" :loading="exportLoading" @click="handleExport"
            >导出</el-button
          >
          <el-button size="small" @click="syncButton"
            >同步</el-button
          >
          <el-button size="small" @click="resetQuery" :disabled="resetDisabled"
            >刷新</el-button
          >
        </el-col>
        <el-col :span="6" :offset="12">
          <div ref="main" class="grid-content bg-purple">
            <el-input
              placeholder="请输入车牌，回车搜索"
              v-model="queryParams.plateNumber"
              @keyup.enter.native="handleQuery"
              size="small"
            >
              <el-button
                slot="append"
                icon="icon-gym-Gsearch"
                @click="cl_boxShow = !cl_boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="searchBox" v-show="cl_boxShow" ref="cc">
        <el-form
          ref="queryForm"
          :inline="true"
          :model="queryParams"
          label-width="75px"
        >
          <el-form-item label="机构" prop="orgName">
            <el-cascader
              style="width: 100%"
              popper-class="jigou"
              v-model="queryParams.orgName"
              :show-all-levels="false"
              :options="orgData"
              :props="{ checkStrictly: true }"
              clearable
            ></el-cascader>
          </el-form-item>

          <el-form-item label="车型" prop="vType">
            <el-checkbox
              v-for="dict in vehicleTypeList"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              v-model="result"
              >{{ dict.dictLabel }}</el-checkbox
            >
          </el-form-item>

          <!--        <el-form-item label="车型" prop="vType" style="width: 100%">
          <el-select
            v-model="queryParams.vType"
            clearable
            placeholder="请选择车型"
            size="small"
          >
            <el-option
              v-for="dict in dict.type.sd_emergency_vehicle_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.label"
            />
          </el-select>
        </el-form-item>-->
          <el-form-item label="运行状态" prop="accState" style="width: 100%">
            <el-select
              v-model="queryParams.accState"
              clearable
              placeholder="请选择运行状态"
              size="small"
            >
              <el-option
                v-for="dict in dict.type.sd_vehicle_run_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
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
    </div>

    <!--    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      :rules="rules"
      v-show="showSearch"
    >
      <el-form-item label="机构" prop="orgName">
        &lt;!&ndash; <el-input style="width:200px"  v-model.number="queryParams.orgId" placeholder="请输入机构名称" size="small" /> &ndash;&gt;
        <el-cascader
          popper-class="jigou"
          v-model="queryParams.orgName"
          :show-all-levels="false"
          :options="orgData"
          :props="{ checkStrictly: true }"
          clearable></el-cascader>
      </el-form-item>

      <el-form-item label="车型" prop="tunnelId">
        <el-select
          v-model="queryParams.vType"
          placeholder="请选择车型"
          clearable
          style="width: 100%"
        >
          <el-option
            v-for="dict in dict.type.sd_emergency_vehicle_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车牌:" prop="stagPointName">
        <el-input
          style="width: 200px"
          v-model.number="queryParams.plateNumber"
          placeholder="请输入车牌"
          size="small"
        />
      </el-form-item>
&lt;!&ndash;      <el-form-item label="使用状态" prop="tunnelId">
        <el-select
          v-model="queryParams.useStatus"
          placeholder="请选择使用状态"
          clearable
          style="width: 100%"
        >
          <el-option
            v-for="dict in dict.type.sd_use_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>&ndash;&gt;
      <el-form-item label="运行状态" prop="tunnelId">
        <el-select
          v-model="queryParams.accState"
          placeholder="请选择车型"
          clearable
          style="width: 100%"
        >
          <el-option
            v-for="dict in dict.type.sd_vehicle_run_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
&lt;!&ndash;        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:material:add']"
          >新增</el-button
        >&ndash;&gt;
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleUpdateMaterial"
          v-hasPermi="['system:vehicle:edit']"
          >修改</el-button
        >
        &lt;!&ndash; <el-col :span="1.5"> &ndash;&gt;

        &lt;!&ndash; </el-col> &ndash;&gt;
      </el-form-item>
    </el-form>-->

    <!-- <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:material:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:SdEmergencyPer:remove']"
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
          v-hasPermi="['system:vehicle:export']"
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
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="mechanismList"
      class="allTable"
      height="62vh"
      @selection-change="handleSelectionChange"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column type="selection" width="55" align="center" reserve-selection/>
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>
      <!--      <el-table-column label="序号"  align="center">
        <template slot-scope="scope">
          {{scope.$index+1}}
        </template>
      </el-table-column>-->
      <el-table-column label="机构" align="center" prop="orgName" />
      <el-table-column label="车牌" align="center" prop="plateNumber" />
      <el-table-column label="车型" align="center" prop="vType" />
      <el-table-column label="品牌" align="center" prop="vehicleModel" />
      <el-table-column label="存放地点" align="center" prop="vPlace" />
      <el-table-column label="运行状态" align="center" prop="accState">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sd_vehicle_run_type"
            :value="scope.row.accState"
          />
        </template>
      </el-table-column>
<!--      <el-table-column
        label="车载终端安装"
        align="center"
        prop="terminalInstall"
      />-->
<!--      <el-table-column label="技术状态描述" align="center" prop="statusDesc" />-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdateMaterial(scope.row)"
            v-hasPermi="['system:vehicle:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleDetailsMaterial(scope.row)"
            >详情</el-button
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
    <!-- 添加/修改应急资源对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="740px"
      append-to-body
      :before-close="cancel"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="106px">
        <el-col :span="12">
          <el-form-item label="机构" prop="orgName" v-show="model">
            <el-input
              v-model="form.orgName"
              placeholder="请输入机构名称"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车牌" prop="plateNumber" v-show="updateModel">
            <el-input
              v-model="form.plateNumber"
              placeholder="请输入车牌"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车型" prop="vType" v-show="model">
            <el-select
              v-model="form.vType"
              placeholder="请选择车型"
              clearable
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="dict in dict.type.sd_emergency_vehicle_type"
                :key="dict.label"
                :label="dict.label"
                :value="dict.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存放地点" prop="vPlace" v-show="model">
            <el-input
              v-model="form.vPlace"
              placeholder="请输入存放地点"
              type="textarea"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用状态" prop="useStatus" v-show="model">
            <el-select
              v-model="form.useStatus"
              placeholder="请选择使用状态"
              clearable
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="dict in dict.type.sd_use_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="运行状态" prop="accState" v-show="updateModel">
            <el-select
              v-model="form.accState"
              placeholder="请选择运行状态"
              clearable
              style="width: 100%"
              :disabled="upDisabled"
            >
              <el-option
                v-for="dict in dict.type.sd_vehicle_run_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产归属" prop="ownerName" v-show="model">
            <el-input v-model="form.ownerName" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆型号" prop="vehicleModel" v-show="model">
            <el-input v-model="form.vehicleModel" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ETC卡类型" prop="etcTypeDesc" v-show="model">
            <el-input v-model="form.etcTypeDesc" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ETC使用情况" prop="etcStateDesc" v-show="model">
            <el-input v-model="form.etcStateDesc" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车龄" prop="carAge" v-show="model">
            <el-input v-model="form.carAge" :disabled="disabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公里数" prop="mileage" v-show="model">
            <el-input v-model="form.mileage" :disabled="disabled" />
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="primary" @click="submitForm" v-show="upShow"
          >确 定</el-button
        >
        <el-button size="small" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleQueryList,
  addList,
  editForm,
  detailForm,
  updateForm,
  deleteForm,
  exportData,
  veicleOrgId,
  syncVehicle,
} from "@/api/equipment/yingJiGou/emergencyVehicles";
import { batchDelete } from "@/api/surveyVehicle/api.js";

export default {
  dicts: ["sd_use_status", "sd_emergency_vehicle_type", "sd_vehicle_run_type"],
  data() {
    const validateLongitude = (rule, value, callback) => {
      if (value == "" || (value == null && this.upDisabled == false)) {
        callback(new Error("请选择运行状态！"));
      } else {
        callback();
      }
    };

    return {
      resetDisabled: false,
      testModel: [],
      tunnelData: [{ tunnelName: 1, tunnelId: 2 }],
      exportLoading: false,
      vehicleTypeList: [],
      cl_boxShow: false,
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      total: 0,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      model: false,
      updateModel: true,
      disabled: true,
      result: [], //获取选中后的checkbox的数组值
      upDisabled: false,
      upShow: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vType: [],
        cx: "",
      },
      form: {},
      mechanismList: [],
      // 表单校验
      rules: {
        accState: [
          { validator: validateLongitude, required: false, trigger: "change" },
        ],
      },
      open: false,
      orgData: [],
      showSearch: true,
    };
  },
  created() {
    this.getList();
    veicleOrgId().then((res) => {
      this.orgData = this.handleTree(res, "value");
      console.log(this.orgData, "机构名称");
    });
    this.getDicts("sd_emergency_vehicle_type").then((data) => {
      console.log(data, "车型");
      this.vehicleTypeList = data.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id
    },
    //同步车辆按钮
    syncButton(){
      syncVehicle().then((res) => {
        if(res.code == 200){
          this.$modal.msgSuccess(res.msg);
          this.getList();
        }
      });
    },
    bodyCloseMenus(e) {
      let self = this;
      self.$nextTick(() => {
        if (
          !this.$refs.main.contains(e.target) &&
          !this.$refs.cc.contains(e.target)
        ) {
          if (self.cl_boxShow == true) {
            self.cl_boxShow = false;
          }
        }
      });
    },

    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },

    /** 导出按钮操作 */
    handleExport() {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出应急车辆数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportData(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = ''
        })
        .catch(() => {});
    },
    /** 查询应急机构列表 */
    getList() {
      // console.log(this.queryParams)
      this.cl_boxShow = false;
      if (this.queryParams.orgName) {
        this.queryParams.orgName = this.queryParams.orgName[2]
          ? this.queryParams.orgName[2]
          : this.queryParams.orgName[1]
          ? this.queryParams.orgName[1]
          : this.queryParams.orgName[0]
          ? this.queryParams.orgName[0]
          : "";
      }
      handleQueryList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.mechanismList = res.rows;
          this.total = res.total;
          this.resetDisabled = false;
        }
      });
      this.loading = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.queryParams.cx = this.result.toString();
      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetDisabled = true;
      this.resetForm("queryForm");
      this.$refs.queryForm.resetFields();
      this.queryParams.vType = [];
      this.result = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length; //非多个禁用
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加应急车辆";
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        materialId: undefined,
        materialName: undefined,
        materialType: undefined,
        inventoryQuantity: undefined,
        company: undefined,
        position: undefined,
        warningValue: undefined,
        code: undefined,
        remark: undefined,
        state: undefined,
        createTime: undefined,
        updateTime: undefined,
        price: undefined,
      };
      this.resetForm("form");
    },
    /** 应急机构提交按钮 */
    submitForm() {
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.title == "修改应急车辆") {
            await updateForm(this.form)
              .then((response) => {
                console.log(response.code, "response.code");
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              })
              .catch((e) => {
                this.open = false;
              });
            console.log("修改应急资源");
          } else {
            await addList(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
        // this.submitBtnLoading = false
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 修改按钮操作 */
    handleUpdateMaterial(row) {
      console.log(row, "row");
      // this.reset();
      this.model = false;
      this.upDisabled = false;
      this.upShow = true;
      const id = row.id;
      this.open = true;
      this.title = "修改应急车辆";
      // console.log(scope,'row.idrow.id');
      editForm(id).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
        }
        // this.form = response.data;
        // this.open = true;
        // this.title = "修改应急资源";
      });
    },
    /** 详情按钮操作 */
    handleDetailsMaterial(row) {
      console.log(row, "row");
      // this.reset();
      this.model = true;
      this.upDisabled = true;
      this.open = true;
      this.title = "应急车辆详情";
      // console.log(scope,'row.idrow.id');
      detailForm({ plateNumber: row.plateNumber }).then((res) => {
        if (res.code == 200) {
          this.form = res.data;
        }
        // this.form = response.data;
        // this.open = true;
        // this.title = "修改应急资源";
      });
    },
  },
};
</script>

<style lang="scss">
.jigou .el-scrollbar {
  width: 215px !important;
}
.jigou .el-icon-arrow-right {
  right: 15px;
}
</style>


