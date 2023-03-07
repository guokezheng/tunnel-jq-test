<template>
   <div class="app-container">
     <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
       <el-form-item label="隧道名称" prop="tunnelId">
         <el-select
           v-model="queryParams.tunnelId"
           placeholder="请选择隧道"
           clearable
           size="small"
         >
           <el-option
             v-for="item in tunnelData"
             :key="item.tunnelName"
             :label="item.tunnelName"
             :value="item.tunnelId"
           />
         </el-select>
       </el-form-item>
       <el-form-item label="事件类型" prop="evtType">
         <el-select
           v-model="queryParams.evtType"
           placeholder="请选择事件类型"
           clearable
           size="small"
         >
           <el-option
             v-for="item in evtTypeData"
             :key="item.evtTypeName"
             :label="item.evtTypeName"
             :value="item.evtTypeId"
           />
         </el-select>
       </el-form-item>
       <el-form-item label="事件等级" prop="evtGrade">
         <el-select
           v-model="queryParams.evtGrade"
           placeholder="请选择事件等级"
           clearable
           size="small"
         >
           <el-option
             v-for="item in evtGradeData"
             :key="item.evtGradeName"
             :label="item.evtGradeName"
             :value="item.evtGradeId"
           />
         </el-select>
       </el-form-item>
       <el-form-item label="事件名称" prop="evtName">
         <el-input v-model="queryParams.evtName" placeholder="请输入事件名称" />
       </el-form-item>
       <el-form-item label="统计时间" prop="dateRange">
         <el-date-picker
           v-model="queryParams.dateRange"
           size="small"
           style="width: 240px"
           value-format="yyyy-MM-dd"
           type="daterange"
           range-separator="-"
           unlink-panels
           start-placeholder="开始日期"
           end-placeholder="结束日期"
         ></el-date-picker>
       </el-form-item>
       <el-form-item>
         <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
         <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
       </el-form-item>
     </el-form>
     <el-table v-loading="loading" :data="tableData" :default-sort = "{prop: 'evtTime', order: 'descending'}">
       <el-table-column label="所属隧道" align="center" prop="tunnelName" />
       <el-table-column label="事件名称" align="center" prop="evtName" />
       <el-table-column label="事件类型" align="center" prop="evtType" />
       <el-table-column label="事件等级" align="center" prop="evtGrade" >
          <template slot-scope="scope">
            <div style="text-align: center">
              <span
                :style="
                  scope.row.evtGrade == '1'
                    ? 'color: #ff0000'
                    : 'color: #00aa00'
                "
              >
                {{ scope.row.evtGrade }}级
              </span>
            </div>
          </template>
       </el-table-column>
       <el-table-column label="事件措施" align="center" prop="evtMeasures" />
       <el-table-column label="发生时间" align="center" prop="evtTime" sortable />
       <el-table-column label="发生位置" align="center" prop="evtAddress" />
       <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
         <template slot-scope="scope">
           <el-button
             size="mini"
             type="text"
             icon="el-icon-edit"
             @click="handleConfig(scope.row)"
             v-hasPermi="['trafficEmergencies:emergencies:edit']"
           >配置
           </el-button>
           <el-button
             size="mini"
             type="text"
             icon="el-icon-delete"
             @click="handleDelete(scope.row)"
             v-hasPermi="['trafficEmergencies:emergencies:remove']"
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
    <el-dialog :title="title" :visible.sync="openConfig" width="500px" append-to-body >
        <el-form :model="configForm" :inline="true" ref='boardForm'>
           <el-form-item label="隧道名称" prop="tunnelName">
             <el-select
               v-model="configForm.tunnelName"
               placeholder="请选择隧道"
               clearable
               size="small"
             >
               <el-option
                 v-for="item in tunnelData"
                 :key="item.tunnelName"
                 :label="item.tunnelName"
                 :value="item.tunnelId"
               />
             </el-select>
           </el-form-item>
           <el-form-item label="事件名称" prop="evtName">
             <el-input v-model="configForm.evtName" placeholder="请输入事件名称" />
           </el-form-item>
           <el-form-item label="事件类型" prop="evtType">
             <el-select
               v-model="configForm.evtType"
               placeholder="请选择事件类型"
               clearable
               size="small"
             >
               <el-option
                 v-for="item in evtTypeData"
                 :key="item.evtTypeName"
                 :label="item.evtTypeName"
                 :value="item.evtTypeId"
               />
             </el-select>
           </el-form-item>
           <el-form-item label="事件等级" prop="evtGrade">
             <el-select
               v-model="configForm.evtGrade"
               placeholder="请选择事件等级"
               clearable
               size="small"
             >
               <el-option
                 v-for="item in evtGradeData"
                 :key="item.evtGradeName"
                 :label="item.evtGradeName"
                 :value="item.evtGradeId"
               />
             </el-select>
           </el-form-item>
           <el-form-item label="事件措施" prop="evtGrade">
             <el-select
               v-model="configForm.evtGrade"
               placeholder="请选择事件措施"
               clearable
               size="small"
             >
               <el-option
                 v-for="item in evtGradeData"
                 :key="item.evtGradeName"
                 :label="item.evtGradeName"
                 :value="item.evtGradeId"
               />
             </el-select>
           </el-form-item>
           <el-form-item label="发生时间" prop="dateRange">
             <el-date-picker
               v-model="configForm.dateRange"
               size="small"
               style="width: 240px"
               value-format="yyyy-MM-dd"
               type="daterange"
               range-separator="-"
               unlink-panels
               start-placeholder="开始日期"
               end-placeholder="结束日期"
             ></el-date-picker>
           </el-form-item>
           <el-form-item label="发生位置" prop="evtAddress" >
             <el-input v-model="configForm.evtAddress"  />

           </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm('config')">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>
   </div>
</template>

<script>
import { listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig } from "@/api/trafficOperationControl/controlConfig/config";
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  name: "Config",
  dicts: ['sd_control_level', 'sd_control_type','sd_control_level_status'],
  data() {
    return {

        // 隧道名称
        tunnelData:[],
        // 事件类型
        evtTypeData:[
            {
                evtTypeName:'车辆剐蹭',
                evtTypeId:1
            },
            {
                evtTypeName:'车辆追尾',
                evtTypeId:2
            },
        ],
        // 事件等级
        evtGradeData:[
            {
                evtGradeName:'一级',
                evtGradeId:1
            },
            {
                evtGradeName:'二级',
                evtGradeId:2
            },
            {
                evtGradeName:'三级',
                evtGradeId:3
            },
        ],
        // 查询参数
        queryParams: {
          evtId:1,
          tunnelId:'',
          evtType:'',
          evtGrade:'',
          evtName:'',
          dateRange:[],
          pageNum: 1,
          pageSize: 10,
        },
        // 表格
        tableData:[
            {
                tunnelName:'迎春坡隧道',
                evtName:'行车与停止车辆剐蹭',
                evtType:'车辆剐蹭',
                evtGrade:"1",
                evtMeasures:'清障 更改车指器',
                evtTime:'2022-02-15 11:39:00',
                evtAddress:"K550+000",
            },
           {
               tunnelName:'迎春坡隧道',
               evtName:'行车与停止车辆剐蹭',
               evtType:'车辆剐蹭',
               evtGrade:"2",
               evtMeasures:'清障 更改车指器',
               evtTime:'2022-02-15 11:39:00',
               evtAddress:"K550+000",
           },
        ],
        // 配置弹窗
        openConfig:false,
        // 配置页 表单
        configForm:{},
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
        // 管控等级配置表格数据
        configList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层


      // 表单校验
      rules: {
      },

    };
  },
  created() {
    this.getList();
    this.getTunnels()
  },
  methods: {
      // 查隧道的接口
      getTunnels() {
        listTunnels().then((response) => {
           this.tunnelData = response.rows
        });
       },
       // 提交情报版按钮 弹窗
       InformationBoard(){
           this.openBoard = true
       },
    /** 查询管控等级配置列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then(response => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleConfig(row){
        console.log(row,"row")
        const params = {
            id:row.id
        }
        // listConfig(params).then(response => {
          // this.configForm = response.rows;
          this.openConfig = true
          this.title = '配置'
        // });

    },
    // 取消按钮
    cancel() {
      this.openBoard = false;
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        controlType: null,
        controlLevel: null,
        status: "0",
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        delFlag: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      console.log(this.queryParams,'this.queryParams')
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加管控等级配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改管控等级配置";
      });
    },

    /** 提交按钮 */
    submitForm(type) {
        if(type == 'board'){
            this.$refs["boardForm"].validate(valid => {
              // if (valid) {
              //     updateInformation(this.boardForm).then(response => {
                    this.$modal.msgSuccess("发布成功");
                    this.openBoard = false;
              //       this.getList();
              //     });
              // }
            });
        }
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除选中数据项？').then(function() {
        return delConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出管控等级配置数据项？').then(() => {
        this.exportLoading = true;
        return exportConfig(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>

<style scoped lang="scss">
    .inputAddress{

    }
</style>
