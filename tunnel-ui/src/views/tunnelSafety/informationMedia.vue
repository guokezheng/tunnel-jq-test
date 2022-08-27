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
        <el-form-item label="统计时间" prop="forecastTime">
          <el-date-picker
            v-model="daterange"
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
        <el-form-item label="出入口" prop="status">
          <el-select v-model="queryParams.entExit" placeholder="请选择出入口" clearable size="small">
            <el-option
              v-for="item in entExitList"
              :key="item.entExitValue"
              :label="item.entExitName"
              :value="item.entExitValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <el-button
            size="mini"
            type="info"
            icon="el-icon-edit"
            @click="InformationBoard(scope.row)"
            v-hasPermi="['trafficFlowInformation:information:edit']"
          >提交情报版</el-button>
        </el-form-item>
      </el-form>
      <div class="detailsPicS">
          <div class="detailsBox" v-for="item in 14">
              <div class="imgBox">
                  <img src="../../assets/Example/draught_fan.jpeg"/>
              </div>
              <div class="contBox">
                  <div>隧道：迎春坡隧道</div>
                  <div>车辆类型：轿车</div>
                  <div>车牌号：XXXXX</div>
                  <div>位置：出入口A</div>
                  <div>时间：2022-02-25 10:40:00</div>
              </div>
          </div>
      </div>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      <el-dialog title="情报版信息" :visible.sync="openBoard" width="500px" append-to-body class="boardDialog">
          <el-form :model="boardForm" :inline="true" ref='boardForm'>
              <el-form-item label="事件信息">
                  <el-input type="textarea" v-model="boardForm.evtInformation" ></el-input>
              </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('board')">确 定</el-button>
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
        // 统计时间
        daterange:[],
        // 隧道名称
        tunnelData:[],
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
        // 查询参数
        queryParams: {
          tunnelId:'',
          entExit:1,
          pageNum: 1,
          pageSize: 10,
        },
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
    //添加管控因素按钮
    addCauseHandle (controlCause) {
      let f = {}
      f.controlLevelId = this.dataForm.controlLevelId
      if (controlCause == 1) {
        f.controlCauseName = '能见度' //能见度
        f.controlCause = 1
        f.visibilityMin = null
        f.visibilityMax = null
        this.dataForm.causeList.push(f)
      }
      if (controlCause == 2) {
        f.controlCauseName = '路面情况' //路面情况
        f.controlCause = 2
        
        this.dataForm.causeList.push(f)
      }
      
      setTimeout(() => { //设置延迟执行
        controlCause = null
      }, 200)
    },
    //删除管控因素按钮
    deleteCauseHandle (index) {
      this.dataForm.causeList.splice(index, 1)
    },
    // 管控因素and管控措施 表单提交
    async dataFormSubmitHandle () {
        this.openMeasures = false;
        console.log(this.dataForm,"dataForm")
    },
    //添加管控措施按钮
    addActionHandle (controlAction) {
      let m = {}
      m.controlLevelId = this.dataForm.controlLevelId
      if (controlAction === 'roadXs') {
        m.controlAction = 'roadXs'
        m.controlActionName = '道路管制' //道路管制
        m.controlRangeMin = null //管控范围最小值
        m.controlRangeMax = null //管控范围最大值
    
        this.dataForm.actionList.push(m)
      }
      setTimeout(() => { //设置延迟执行
        controlAction = null
      }, 200)
    },
    //删除管控措施按钮
    deleteActionHandle (index) {
      this.dataForm.actionList.splice(index, 1)
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
    // 配置措施 按钮
    handleMeasures(row){
        this.dataForm.controlLevelId = row.id
        const id = row.id
        // getInformation(id).then(response => {
          // this.form = response.data;
          this.openMeasures = true;
          this.title = "配置措施";
        // });
       
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
<style scoped lang="scss">
    .boardDialog{
        img{
            width: 20px;
            
        }
    }
    .detailsPicS{
        width: 100%;
        height: 650px;
        border: solid 1px yellow;
        overflow: auto;
        // display: flex;
        .detailsBox{
            width: 24%;
            height: 200px;
            border: solid 1px red;
            display: flex;
            margin-right: 18px;
            margin-bottom: 20px;
            float: left;
            .imgBox{
                width: 56%;
                height: 100%;
                border: solid 1px black;
                overflow: hidden;
                img{
                    height: 100%;
                }
            }
            .contBox{
                width: 44%;
                font-size: 14px;
                
                >div{
                   height: 20%;
                   width: 100%;
                   padding-left: 5px;
                   border: solid 1px green;
                   line-height: 40px;
                }
            }
            
        }
    }
</style>
