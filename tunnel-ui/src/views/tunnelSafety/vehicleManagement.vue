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
      <el-form-item label="方向" prop="tunnelDirection">
        <el-select
          v-model="queryParams.tunnelDirection"
          placeholder="请选择方向道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in directionData"
            :key="item.directionName"
            :label="item.directionName"
            :value="item.directionId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标记" prop="vehicleSign">
        <el-select
          v-model="queryParams.vehicleSign"
          placeholder="是否标记"
          clearable
          size="small"
        >
          <el-option
            v-for="item in signData"
            :key="item.signName"
            :label="item.signName"
            :value="item.signId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车辆类型" prop="vehicleType">
        <el-select
          v-model="queryParams.vehicleType"
          placeholder="请选择车辆类型"
          clearable
          size="small"
        >
          <el-option
            v-for="item in typeData"
            :key="item.typeName"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData">
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="车辆类型" align="center" prop="vehicleType" />
      <el-table-column label="方向" align="center" prop="tunnelDirection" />
      <el-table-column label="标记" align="center" prop="vehicleSign" >
         <template slot-scope="scope">
           <div style="text-align: center">
             <span
               :style="
                 scope.row.vehicleSign == '0'
                   ? 'color: #ff0000'
                   : 'color: #00aa00'
               "
             >
               {{ scope.row.vehicleSign == '0' ? '未标记' : '标记' }}
             </span>
           </div>
         </template>
      </el-table-column>
      <el-table-column label="驾驶员" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlePic(scope.row)"
            v-hasPermi="['trafficEmergencies:emergencies:edit']"
          >点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="车牌号" align="center" prop="plateNum" />
      <el-table-column label="进入通道时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleSign(scope.row)"
            v-hasPermi="['trafficEmergencies:emergencies:edit']"
            
          >{{ scope.row.vehicleSign == '0' ? '标记' : '取消标记' }}
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
    <el-dialog title="事件发布" :visible.sync="openPic" width="500px" append-to-body class="picDialog">
        <!-- <img :src=""/> -->
        
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
        // 方向
        directionData:[
            {
                directionName:'上行',
                directionId:1
            },
            {
                directionName:'下行',
                directionId:2
            }
        ],
        // 标记
        signData:[
            {
                signName:'标记',
                signId:1
            },
            {
                signName:'未标记',
                signId:2
            },
        ],
        // 出入口下拉框
        entExitList:[
            {
                entExitValue:1,
                entExitName:'出入口A',
            },
            {
                entExitValue:2,
                entExitName:'出入口B',
            },
        ],
        // 车辆类型
        typeData:[
            {
                typeName:'小客车',
                typeId:1
            },
            {
                typeName:'大货车',
                typeId:2
            },
            {
                typeName:'专项车',
                typeId:3
            }
        ],
        // 查询参数
        queryParams: {
          tunnelId:'',
          tunnelDirection:'',
          vehicleSign:'',
          vehicleType:'',
          pageNum: 1,
          pageSize: 10,
        },
        // 表格
        tableData:[
            {
                tunnelName:'迎春坡隧道',
                vehicleType:'小型车',
                tunnelDirection:'上行',
                vehicleSign:'0',
                plateNum:'鲁A 88888',
                createTime:'2022-02-15 11:39:00',
                picUrl: require('@/assets/Example/pic1.jpg'),
            },
            {
                tunnelName:'迎春坡隧道',
                vehicleType:'专项车',
                tunnelDirection:'下行',
                vehicleSign:'1',
                plateNum:'鲁A 66666',
                createTime:'2022-02-15 11:39:00',
                picUrl:'',
            }
        ],
        // 点击查看 图片的弹窗
        openPic:false,
        // 弹窗 提交情报版
        openBoard:false,
        // 情报版弹窗 form表单
        boardForm:{
            evtInformation:'一级管控，2022.02.10 17:08:33 K470-K550(龙门隧道附近)，检测有积雪，请货车于该路段限速30km/h',
            // checkList:[],
        },
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
        open: false,
        dataForm: {
          controlLevelId: null,
          causeList: [],
          actionList: []
        },
        conditionList:[
            {
                value:1,
                label:"积雪"
            },
            {
                value:2,
                label:"积水"
            },
        ],
        roadXsOptions: [{
          label: '限行车速',
          value: 'limitspeed'
        }, {
          label: '限行车道且限行车辆',
          value: 'limitline&limitcar'
        }, {
          label: '限行车辆',
          value: 'limitcar'
        }, {
          label: '临时关闭',
          value: 'close'
        }, {
          label: '间隔放行',
          value: 'interval'
        }, {
          label: '开放应急车道',
          value: 'emergencyLine'
        },],
      
      
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 配置措施 弹窗
      openMeasures:false,
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
    // 点击查看 图片
    handlePic(row){
        console.log(row,"row")
        this.openPic = true
    },
    handleSign(row){
        console.log(row,"row")
        if(row.vehicleSign == '0'){
            row.vehicleSign = "1"
        }else{
            row.vehicleSign = "0"
        }
        // 将row传给后台 然后刷新
        
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
      this.$modal.confirm('是否确认导出所有管控等级配置数据项？').then(() => {
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

<style>
</style>
