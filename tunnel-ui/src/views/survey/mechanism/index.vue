<template>
  <div class="app-container">
  <el-form :model="queryParams" ref="queryForm" :inline="true" :rules="rules"  v-show="showSearch">
          <el-form-item label="物资名称" prop="orgName">
            <el-input style="width:200px"  v-model.number="queryParams.orgName" placeholder="请输入机构名称" size="small" />
          </el-form-item>
        <el-form-item label="驻点名称"  prop="stagPointName">
            <el-input style="width:200px;" v-model.number="queryParams.stagPointName" placeholder="请输入驻点名称" size="small" />
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
           <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['monitor:logininfor:export']"
        >导出</el-button> 
        </el-form-item>        
                
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:material:add']">新增</el-button>
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

      <el-table v-loading="loading" :data="mechanismList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="机构名称" align="center" prop="orgName" />
        <el-table-column label="驻点名称" align="center" prop="stationName" />
        <el-table-column label="机构地址" align="center" prop="orgAddress" />
        <el-table-column label="经度" align="center" prop="longitude" />
        <el-table-column label="纬度" align="center" prop="latitude"  />
        <el-table-column label="救援外协单位" align="center" prop="outsourceUnit" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateMaterial(scope)" v-hasPermi="['system:material:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:material:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
       <pagination
      :total="total"
      :limit.sync="pageSize"
    />

      <!-- 添加/修改应急资源对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :before-close="cancel">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="机构名称" prop="orgName">
            <el-input v-model="form.orgName" placeholder="请输入机构名称" />
          </el-form-item>
          <el-form-item label="驻点名称" prop=stationName>
           <el-input v-model="form.stationName" placeholder="请输入驻点名称" />
          </el-form-item>
          <el-form-item label="机构地址" prop="orgAddress">
            <el-input v-model="form.orgAddress" placeholder="请输入机构地址" />
          </el-form-item>

          <el-form-item label="经度" prop="longitude">
            <el-input v-model="form.longitude" placeholder="请输入经度" />
          </el-form-item>
          <el-form-item label="纬度" prop="latitude">
            <el-input v-model="form.latitude" placeholder="请输入纬度" />
          </el-form-item>
          <el-form-item label="救援外协单位" prop="outsourceUnit">
            <el-input v-model="form.outsourceUnit" placeholder="请输入救援外协单位" />
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
exportData
} from "@/api/equipment/yingJiGou/emergencyOrganization";
  export default{
    data(){
      return{
        //新增弹出
        open:false,
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 弹出层标题
        title: "",
        queryParams:{
          orgName:null,
          stagPointName:null
        },
        form:{},
        mechanismList:[
          // {
          //   materialName:'济南路管分中心',
          //   materialType:'济南驻点',
          //   tunnelName:'济南收费站',
          //   station:'35.3773444',
          //   direction:'117.23742',
          //   number:'-'
          // },
          // {
          //   materialName:'1',
          //   materialType:'2',
          //   tunnelName:'3',
          //   station:'35.3773444',
          //   direction:'117.23742',
          //   number:'-'
          // }
        ],
        // 表单校验
        rules: {
          // longitude: { pattern: /^((([^0][0-9]+|0)\.([0-9]{1,2}))$)|^(([1-9]+)\.([0-9]{1,6})$)/,
          //       message: '经纬度须输入小数，小数点后最多支持六位', trigger: ['blur','change']},
          // latitude: { pattern: /^((([^0][0-9]+|0)\.([0-9]{1,2}))$)|^(([1-9]+)\.([0-9]{1,6})$)/,
          //       message: "经纬度须输入小数，小数点后最多支持六位", trigger: ['blur','change']},
        
           orgName: [
            { required: true, message: '请输入机构', trigger: 'orgName' }
          ],
          stationName: [
            { required: true, message: '请输入驻点名称', trigger: 'stationName' } 
          ],
          orgAddress: [
            { required: true, message: '请输入机构地址', trigger: 'orgAddress' }
          ],
          outsourceUnit: [
            { required: true, message: '请输入救援外协单位', trigger: 'outsourceUnit' }
          ],
          longitude: [
            { required: true, message: '请输入经度', trigger: 'longitude' }
          ],
          latitude: [
            { required: true, message: '请输入纬度', trigger: 'latitude' }
          ]
         
        
        },
        //总条数
        total:0,
        //每页条数
        pageSize:10,
        
        exportLoading:false,
        showSearch:true
      }
    },
    created(){
      this.getList();
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
            this.mechanismList=res.rows
            this.total=res.total
          })
        this.loading = false;
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList(this.queryParams);
      },  
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.$refs.queryForm.resetFields()
        this.queryForm = {
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
        // this.reset();、
        this.open = true;
        this.title = "添加应急资源";
        console.log("xinz");
      },
      // 表单重置
      reset() {
        this.form = {
          // id: undefined,
          // materialId: undefined,
          // materialName: undefined,
          // materialType: undefined,
          // inventoryQuantity: undefined,
          // company: undefined,
          // position: undefined,
          // warningValue: undefined,
          // code: undefined,
          // remark: undefined,
          // state: undefined,
          // createTime: undefined,
          // updateTime: undefined,
          // price: undefined
        };
        this.resetForm("form");
      },
      /** 应急机构提交按钮 */
      submitForm() {
        this.$refs["form"].validate(async valid => {
          if (valid) {
            if ( this.title == '修改应急资源') {
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
                  this.reset()
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
        this.open=true
        this.title='修改应急资源'
        // console.log(scope,'row.idrow.id');
        editForm(scope.row.orgId).then(res => {
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
        // const ids = row.id || this.ids;
        // console.log(row,'rowrorrrrr');
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          // return delMaterial(ids);
        }).then(() => {
          deleteForm(row.orgId).then(res=>{
              conosole.log(res,'res')
              if(res.code==200){
                this.getList()
              }
          })

          
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
      },
    }
  }
</script>

<style>
</style>
