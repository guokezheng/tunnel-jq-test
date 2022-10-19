<template>
  <div class="app-container">
  <el-form :model="queryParams" ref="queryForm" :inline="true" :rules="rules"  v-show="showSearch" >
          <el-form-item label="机构" prop="orgName">
            <!-- <el-input style="width:200px"  v-model.number="queryParams.orgId" placeholder="请输入机构名称" size="small" /> -->
           <el-select
            v-model="queryParams.orgId"
            placeholder="请选择机构"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in orgData"
              :key="item.orgId"
              :label="item.orgName"
              :value="item.orgId"
            />
          </el-select>
          </el-form-item>

        <el-form-item label="车型" prop="tunnelId">
          <el-select
            v-model="queryParams.vType"
            placeholder="请选择车型"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.sd_wj_vehicle_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="车牌:"  prop="stagPointName">
            <el-input style="width:200px;" v-model.number="queryParams.plateNumber" placeholder="请输入车牌" size="small" />
        </el-form-item>
        <el-form-item label="使用状态" prop="tunnelId">
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
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
          <!-- <el-col :span="1.5"> -->

      <!-- </el-col> -->
        </el-form-item>

      </el-form>

      <el-row :gutter="10" class="mb8">
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
      </el-row>

      <el-table v-loading="loading" :data="mechanismList" @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="机构" align="center" prop="orgName" />
        <el-table-column label="车牌" align="center" prop="plateNumber" />
        <el-table-column label="车型" align="center" prop="vType">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sd_wj_vehicle_type" :value="scope.row.vType"/>
          </template>
        </el-table-column>
        <el-table-column label="存放地点" align="center" prop="vPlace" />
        <el-table-column label="使用状态" align="center" prop="useStatus">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sd_use_status" :value="scope.row.useStatus"/>
          </template>
        </el-table-column>
        <el-table-column label="车载终端安装" align="center" prop="terminalInstall" />
        <el-table-column label="技术状态描述" align="center" prop="statusDesc" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateMaterial(scope.row)" v-hasPermi="['system:vehicle:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:vehicle:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
      <!-- 添加/修改应急资源对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :before-close="cancel">
        <el-form ref="form" :model="form" :rules="rules" label-width="106px">
          <el-form-item label="机构" prop="orgId">
            <!-- <el-input v-model="form.orgId" placeholder="请输入机构名称" /> -->
             <el-select
            v-model="form.orgId"
            placeholder="请选择机构"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in orgData"
              :key="item.orgId"
              :label="item.orgName"
              :value="item.orgId"
            />
          </el-select>
          </el-form-item>
          <el-form-item label="车牌" prop=plateNumber>
           <el-input v-model="form.plateNumber" placeholder="请输入车牌" />
          </el-form-item>
          <el-form-item label="车型" prop="vType">
            <el-select
              v-model="form.vType"
              placeholder="请选择车型"
              clearable
              style="width: 100%"
            >
              <el-option
                v-for="dict in dict.type.sd_wj_vehicle_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="存放地点" prop="vPlace">
            <el-input v-model="form.vPlace" placeholder="请输入存放地点" />
          </el-form-item>
          <el-form-item label="使用状态" prop="useStatus">
            <el-select
              v-model="form.useStatus"
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
          </el-form-item>
          <el-form-item label="车载终端安装" prop="terminalInstall">
            <el-input v-model="form.terminalInstall" placeholder="请输入车载终端安装" />
          </el-form-item>
          <el-form-item label="技术状态描述" prop="statusDesc">
            <el-input v-model="form.statusDesc" placeholder="请输入技术状态描述" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" type="primary" @click="submitForm">确 定</el-button>
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
updateForm,
deleteForm,
exportData,
veicleOrgId
} from "@/api/equipment/yingJiGou/emergencyVehicles";
import {
  batchDelete,
} from "@/api/surveyVehicle/api.js";

  export default{
    dicts: ["sd_use_status","sd_wj_vehicle_type"],
    data(){
      return{
        tunnelData:[{tunnelName:1,tunnelId:2}],
        exportLoading:false,
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        total:0,
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 弹出层标题
        title: "",
        queryParams:{
          pageNum: 1,
          pageSize: 10,
        },
        form:{},
        mechanismList:[],
        // 表单校验
        rules: {
           orgId: [
            { required: true, message: '请选择机构', trigger: 'orgId' }
          ],
          plateNumber: [
            { required: true, message: '请输入车牌', trigger: 'plateNumber' }
          ],
          vType: [
            { required: true, message: '请选择车型', trigger: 'vType' }
          ],
          vPlace: [
            { required: true, message: '请输入存放地点', trigger: 'vPlace' }
          ],
          useStatus: [
            { required: true, message: '请选择使用状态', trigger: 'tunnelId' }
          ],
          terminalInstall: [
            { required: true, message: '请输入车载终端安装', trigger: 'terminalInstall' }
          ],
          statusDesc: [
            { required: true, message: '请输入技术状态描述', trigger: 'statusDesc' }
          ]

        },
        open:false,
        orgData:'',
        showSearch:true,
      }
    },
    created(){
      this.getList();
      veicleOrgId().then(res=>{
        console.log(res.data,"机构名称")
        this.orgData=res.data
      })
      this.getDicts("sd_wj_vehicle_type").then((data) => {
      console.log(data, "车型");
      this.vehicleTypeList = data.data;
    });
    },
    methods:{
       /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有数据项？').then(() => {
        this.exportLoading = true;
        return exportData();
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
      /** 查询应急机构列表 */
     getList(queryParams={}) {
      handleQueryList(queryParams).then(res=>{
       if(res.code==200){
        this.mechanismList=res.rows
        this.total = res.total;

       }

      })
     this.loading = false;
      },
      /** 搜索按钮操作 */
      handleQuery() {
       console.log(this.queryParams,'useStatususeStatus');
        // this.queryParams.pageNum = 1;
        this.getList(this.queryParams);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.$refs.queryForm.resetFields()
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
         orgName:null,
         stagPointName:null
        }
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length //非多个禁用
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
          price: undefined
        };
        this.resetForm("form");
      },
      /** 应急机构提交按钮 */
      submitForm() {
      this.$refs["form"].validate(async valid => {
          if (valid) {
            if ( this.title == '修改应急车辆') {
              console.log(this.form,'formfffffff');
              await updateForm(this.form).then(response => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
              console.log('修改应急资源');
            } else {
              console.log(this.form,'22222222222222')
              await addList(this.form).then(response => {
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
      handleUpdateMaterial(scope) {
        // this.reset();
        // const id = row.id || this.ids
        console.log(scope,'scope');
        this.open=true
        this.title='修改应急车辆'
        // console.log(scope,'row.idrow.id');
        editForm(scope.id).then(res => {
           if(res.code==200) {
            this.form=res.data
           }
          console.log(res,'sssssssssssssss');
          // this.form = response.data;
          // this.open = true;
          // this.title = "修改应急资源";
        });
      },
     /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.orgId?[row.orgId]:this.ids;
        var that = this
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          batchDelete(ids).then((res) =>{
            if(res.code == 200){
              that.$modal.msgSuccess("删除成功");
              that.getList();
              that.$forceUpdate()
            }
          })
        })
      },
      // 表格的行样式
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

<style>
</style>
