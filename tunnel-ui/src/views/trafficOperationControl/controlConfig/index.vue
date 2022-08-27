<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="管控类别" prop="controlType">
        <el-select v-model="queryParams.controlType" placeholder="请选择管控类别" clearable size="small">
          <el-option
            v-for="dict in dict.type.sd_control_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="管控级别" prop="controlLevel">
        <el-select
          v-model="queryParams.controlLevel"
          placeholder="请选择管控级别"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sd_control_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="管控状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.sd_control_level_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:config:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:config:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:config:remove']"
        >删除</el-button>
      </el-col>
      <!--<el-col :span="1.5">-->
        <!--<el-button-->
          <!--type="warning"-->
          <!--plain-->
          <!--icon="el-icon-download"-->
          <!--size="mini"-->
          <!--:loading="exportLoading"-->
          <!--@click="handleExport"-->
          <!--v-hasPermi="['system:config:export']"-->
        <!--&gt;导出</el-button>-->
      <!--</el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange" row-key="id">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="主键id" align="center" prop="id" />-->
      <el-table-column label="管控类别" align="center" prop="controlType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_control_type" :value="scope.row.controlType"/>
        </template>
      </el-table-column>
      <el-table-column label="管控级别" align="center" prop="controlLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_control_level" :value="scope.row.controlLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_control_level_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleMeasures(scope.row)"
              v-hasPermi="['system:config:edit']"
            >配置措施</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:config:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:config:remove']"
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

    <!-- 添加或修改管控等级配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :before-close="handleClose">
      <el-form ref="form" :model="form"  label-width="80px" :rules="rules">
        <el-form-item label="管控类别" prop="controlType">
          <el-select v-model="form.controlType" placeholder="请选择管控类别">
            <el-option
              v-for="dict in dict.type.sd_control_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="管控级别" prop="controlLevel">
          <el-select v-model="form.controlLevel" placeholder="请选择管控级别" >
            <el-option
              v-for="dict in dict.type.sd_control_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sd_control_level_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel()">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="openMeasures" width="80%"
                append-to-body :before-close="handleClose" >
      <el-card>
        <div slot="header" class="clearfix">
          <span>管控原因</span>
          <div style="float: right;margin-top: -0.6vh;">
            <el-button type="primary" round size="mini" @click="addCauseHandle(1)">+能见度</el-button>
            <el-button type="primary" round size="mini" @click="addCauseHandle(2)">+路面情况</el-button>
            <el-button type="primary" round size="mini" @click="addCauseHandle(3)">+拥挤度</el-button>
            <el-button type="primary" round size="mini" @click="addCauseHandle(4)">+突发事件</el-button>
          </div>
        </div>
        <div class="body">
          <el-form :model="dataForm" ref="dataForm"  v-loading="loading"
                    label-width="90px" size="mini">
            <el-row :gutter="24" style="margin-left: 0px;margin-right: 0px;">
              <el-col :span="2" style="margin-left: 10px;padding:0">
                <span>删除</span>
              </el-col>
              <el-col :span="4" style="padding:0">
                <span>管控原因</span>
              </el-col>
              <el-col :span="10" style="padding-left: -2vh;">
                <span>管控原因阈值</span>
              </el-col>
            </el-row>
            <div style="padding-top: 1vh;">
              <el-row :gutter="24" v-for="(causeItem,index) in dataForm.causeList" :id="causeItem.id"
                      style="cursor:pointer;margin-bottom: 5px;">
                <div v-if="causeItem.causeType==1">
                  <el-col :span="2">
                    <el-button type="primary" icon="el-icon-delete" size="mini" style="font-size: 10px;"
                               @click="deleteCauseHandle(index)">
                    </el-button>
                  </el-col>
                  <el-col :span="3" style="width: 120px;margin-left: -25px;padding:0">
                    <el-input disabled v-model="causeItem.causeType == '1'? '能见度':'路面情况'" size="mini"></el-input>
                  </el-col>
                  <el-col :span="2" style="margin-left: 30px;padding:0">
                    <div style="float:left;line-height:28px;margin-right: 10px;">能见度范围:</div>
                  </el-col>
                  <el-col :span="1" style="margin-left: 0px;padding:0">
                    <el-input style="width: 100%;" v-model="causeItem.visibilityMin" size="mini">
                    </el-input>
                  </el-col>
                  <el-col :span="1" style="padding:0;width: 30px;">
                    <div style="font-weight: bolder;text-align: center;line-height: 30px;">~</div>
                  </el-col>
                  <el-col :span="1" style="padding:0">
                    <el-input style="width: 100%;" v-model="causeItem.visibilityMax" size="mini">
                    </el-input>
                  </el-col>
                  <el-col :span="1" style="padding:5px">
                    <div>米</div>
                  </el-col>
                </div>
                <div v-if="causeItem.causeType==2">
                  <el-col :span="2">
                    <el-button type="primary" icon="el-icon-delete" size="mini" style="font-size: 10px;"
                               @click="deleteCauseHandle(index)">
                    </el-button>
                  </el-col>
                  <el-col :span="3" style="width: 120px;margin-left: -25px;padding:0">
                    <el-input disabled v-model="causeItem.causeType == '1'? '能见度':'路面情况'" size="mini"></el-input>
                  </el-col>
                  <el-col :span="2" style="margin-left: 30px;padding:0">
                    <div style="float:left;line-height:28px;margin-right: 10px;">路面情况:</div>
                  </el-col>
                  <el-col :span="3" style="margin-left: 0px;padding:0">

                    <el-select 
                      v-model="causeItem.roadCondition"
                      placeholder="请选择路面情况"
                      size="small"
                      style="width: 153px;float:left;"
                    >
                      <el-option
                        v-for="dict in dict.type.sd_road_condition"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-col>
                </div>
                <div v-if="causeItem.causeType==3">
                  <el-col :span="2">
                    <el-button type="primary" icon="el-icon-delete" size="mini" style="font-size: 10px;"
                               @click="deleteCauseHandle(index)">
                    </el-button>
                  </el-col>
                  <el-col :span="3" style="width: 120px;margin-left: -25px;padding:0">
                    <el-input disabled value="拥挤度" size="mini"></el-input>
                  </el-col>
                  <el-col :span="2" style="margin-left: 30px;padding:0">
                    <div style="float:left;line-height:28px;margin-right: 10px;">拥挤度:</div>
                  </el-col>
                  <el-col :span="3" style="margin-left: 0px;padding:0">

                    <el-select
                      v-model="causeItem.congestionDegree"
                      placeholder="请选择拥挤度"
                      size="small"
                      style="width: 153px;float:left;"
                    >
                      <el-option
                        v-for="dict in dict.type.sd_congestion_degree"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-col>
                </div>
                <div v-if="causeItem.causeType==4">
                  <el-col :span="2">
                    <el-button type="primary" icon="el-icon-delete" size="mini" style="font-size: 10px;"
                               @click="deleteCauseHandle(index)">
                    </el-button>
                  </el-col>
                  <el-col :span="3" style="width: 120px;margin-left: -25px;padding:0">
                    <el-input disabled value="突发事件" size="mini"></el-input>
                  </el-col>
                  <el-col :span="2" style="margin-left: 30px;padding:0">
                    <div style="float:left;line-height:28px;margin-right: 10px;">事件类型:</div>
                  </el-col>
                  <el-col :span="3" style="margin-left: 0px;padding:0">

                    <el-select
                      v-model="causeItem.incidentType"
                      placeholder="请选择事件类型"
                      size="small"
                      style="width: 153px;float:left;"
                    >
                      <el-option
                        v-for="dict in dict.type.incident_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="2" style="margin-left: 30px;padding:0">
                    <div style="float:left;line-height:28px;margin-right: 10px;">事件级别:</div>
                  </el-col>
                  <el-col :span="3" style="margin-left: 0px;padding:0">
                    <el-select
                      v-model="causeItem.incidentGrade"
                      placeholder="请选择事件级别"
                      size="small"
                      style="width: 153px;float:left;"
                    >
                      <el-option
                        v-for="dict in dict.type.sd_incident_level"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                  </el-col>
                </div>
              </el-row>
            </div>
          </el-form>
        </div>
      </el-card>
      <el-card>
            <div slot="header" class="clearfix">
              <span>管控措施</span>
              <div style="float: right;">
                <el-row>
                  <el-button type="primary" round size="mini" @click="addActionHandle('roadXs')">+道路管制</el-button>
                </el-row>
              </div>

            </div>
            <div class="body">
              <el-row :gutter="24" style="margin-left: 0px;margin-right: 0px;">
                <el-col :span="2" style="margin-left: 10px;padding:0">
                  <span>删除</span>
                </el-col>
                <el-col :span="2" style="padding:0">
                  <span>措施</span>
                </el-col>
                <el-col :span="4" style="margin-left: -10px;padding:0">
                  <span>距离事件点管控范围</span>
                </el-col>
                <el-col :span="16" style="padding-left: 8vh;">
                  <span>措施详情</span>
                </el-col>
              </el-row>
              <el-form :model="dataForm"  ref="dataForm" v-loading="loading" label-width="90px" size="mini">
                <div style="padding-top: 1vh;">
                  <el-row :gutter="24" v-for="(actionItem,index) in dataForm.measureList" :id="actionItem.id"
                          style="cursor:pointer;margin-bottom: 5px;">

                    <div v-if="actionItem.measureType==='roadXs'">
                      <el-col :span="2">
                        <el-button type="primary" icon="el-icon-delete" size="mini" style="font-size: 10px;"
                                   @click="deleteActionHandle(index)">
                        </el-button>
                      </el-col>
                      <el-col :span="2" style="width: 120px;margin-left: -25px;padding:0">
                        <el-input disabled v-model="actionItem.measureType == 'roadXs'? '道路管制':''" size="mini"></el-input>
                      </el-col>
                      <el-col :span="1" style="margin-left: 10px;padding:0">
                        <el-input style="width: 100%;float: left;" v-model="actionItem.controlRangeMin"
                                  size="mini"></el-input>
                      </el-col>
                      <el-col :span="1" style="padding:0;width: 30px;">
                        <div style="font-weight: bolder;text-align: center;line-height: 30px;">~</div>
                      </el-col>
                      <el-col :span="1" style="padding:0">
                        <el-input style="width: 60px;float: left;" v-model="actionItem.controlRangeMax"
                                  size="mini"></el-input>
                      </el-col>
                      <el-col :span="1" style="padding:0">
                        <div style="float:left;line-height:28px;margin-left: 10px;">公里</div>
                      </el-col>
                      <el-col :span="3" style="margin-left: 10px;padding:0">
                        <el-select style="width: 148px;float:left;" placeholder="请选择"
                                    v-model="actionItem.controlMeasure" size="mini"
                                   clearable>
                          <el-option :key="item.value" :label="item.label" :value="item.value"
                                     v-for="item in dict.type.sd_road_control">
                          </el-option>
                        </el-select>
                      </el-col>
                      <el-col :span="4" style="padding:0;display: flex;" v-if="actionItem.controlMeasure == 'limitspeed'">
                         <div style="float:left;line-height:28px;margin-right: 10px;width: 35px;">速度:</div>
                         <el-select style="width: 148px;float:left;" placeholder="请选择"
                                     v-model="actionItem.limitSpeed" size="mini"
                                    clearable>
                           <el-option  v-for="dict in dict.type.sd_limit_speed"
                                      :key="dict.value"
                                      :label="dict.label"
                                      :value="dict.value">
                           </el-option>
                         </el-select>
                         <div style="float:left;line-height:28px;margin-left: 10px;width: 35px;"> km/h</div>
                      </el-col>
                      <el-col :span="9" style="padding:0;display: flex;" v-if="actionItem.controlMeasure == 'limitcar'">
                         <div style="float:left;line-height:28px;margin-right: 10px;width: 65px;">限制类型:</div>
                         <el-select style="width: 148px;float:left;" placeholder="请选择"
                                     v-model="actionItem.limitType" size="mini"
                                    clearable>
                           <el-option  v-for="dict in dict.type.sd_limit_type"
                                      :key="dict.value"
                                      :label="dict.label"
                                      :value="dict.value">
                           </el-option>
                         </el-select>
                         <div style="float:left;line-height:28px;margin-right: 10px;width: 65px;margin-left: 20px;">车辆类型:</div>
                         <el-select style="width: 148px;float:left;" placeholder="请选择"
                                     v-model="actionItem.carType" size="mini"
                                    clearable>
                           <el-option  v-for="dict in dict.type.sd_limit_car_type"
                                      :key="dict.value"
                                      :label="dict.label"
                                      :value="dict.value">
                           </el-option>
                         </el-select>
                      </el-col>
                      <el-col :span="4" style="padding:0;display: flex;" v-if="actionItem.controlMeasure == 'emergencyLine'">
                         <div style="float:left;line-height:28px;margin-right: 10px;width: 35px;">速度:</div>
                         <el-select style="width: 148px;float:left;" placeholder="请选择"
                                     v-model="actionItem.limitSpeed" size="mini"
                                    clearable>
                           <el-option  v-for="dict in dict.type.sd_limit_speed"
                                      :key="dict.value"
                                      :label="dict.label"
                                      :value="dict.value">
                           </el-option>
                         </el-select>
                         <div style="float:left;line-height:28px;margin-left: 10px;width: 35px;"> km/h</div>
                      </el-col>
                    </div>
                  </el-row>
                </div>

              </el-form>
            </div>
          </el-card>
      <div slot="footer">
        <el-button size="small" @click="cancel()">取 消</el-button>
        <el-button size="small" @click="dataFormSubmitHandle()" type="primary" v-loading="loading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { saveControlMeasure, getControlMeasure,listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig } from "@/api/trafficOperationControl/controlConfig/config";

export default {
  name: "Config",
  dicts: ['sd_control_level', 'sd_control_type','sd_control_level_status','sd_limit_speed','sd_limit_type','sd_limit_car_type','sd_road_condition',"sd_congestion_degree","sd_road_control","incident_type","sd_incident_level"],
  data() {
    return {
        dataForm: {
           id: null,
          causeList: [],
          measureList: []
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
        // roadXsOptions: [{
        //   label: '限行车速',
        //   value: 'limitspeed'
        // }, {
        //   label: '限行车道且限行车辆',
        //   value: 'limitline&limitcar'
        // }, {
        //   label: '限行车辆',
        //   value: 'limitcar'
        // }, {
        //   label: '临时关闭',
        //   value: 'close'
        // }, {
        //   label: '间隔放行',
        //   value: 'interval'
        // }, {
        //   label: '开放应急车道',
        //   value: 'emergencyLine'
        // },],
        rules: {
          controlType: [{
            required: true,
            message: '请选择管控类别',
            trigger: 'blur'
          }],
          controlLevel: [{
            required: true,
            message: '请选择管控级别',
            trigger: 'change'
          }],

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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        controlType: null,
        controlLevel: null,
        status: null,
      },
      // 表单参数
      form: {},

      // 配置措施 弹窗
      openMeasures:false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
    addCauseHandle (causeType) {
        console.log(causeType,"causeType")
      let f = {}
      f.id = this.dataForm.id
      if (causeType == 1) {
        f.controlCauseName = '能见度' //能见度
        f.causeType = 1
        f.visibilityMin = null
        f.visibilityMax = null
        this.dataForm.causeList.push(f)
      }
      if (causeType == 2) {
        f.controlCauseName = '路面情况' //路面情况
        f.causeType = 2

        this.dataForm.causeList.push(f)
      }
      if(causeType === 3){
        f.controlCauseName = "拥挤度";
        f.causeType = 3;
        this.dataForm.causeList.push(f);
      }
      if(causeType === 4){
        f.controlCauseName = "突发事件";
        f.causeType = 4;
        this.dataForm.causeList.push(f);
      }

      setTimeout(() => { //设置延迟执行
        causeType = null
      }, 200)
    },
    //添加管控措施按钮
    addActionHandle (measureType) {
        console.log(measureType,"measureType")
      let m = {}
      m.id = this.dataForm.id
      if (measureType === 'roadXs') {
        m.measureType = 'roadXs'
        m.controlActionName = '道路管制' //道路管制
        m.controlRangeMin = null //管控范围最小值
        m.controlRangeMax = null //管控范围最大值

        this.dataForm.measureList.push(m)
      }
      setTimeout(() => { //设置延迟执行
        measureType = null
      }, 200)
    },
    //删除管控因素按钮
    deleteCauseHandle (index) {
      this.dataForm.causeList.splice(index, 1)
    },
    //删除管控措施按钮
    deleteActionHandle (index) {
      this.dataForm.measureList.splice(index, 1)
    },
    // 管控因素and管控措施 表单提交
    async dataFormSubmitHandle () {
      console.log(this.dataForm,"this.dataForm")
        for( var item of this.dataForm.causeList){
            if(item.causeType == 1){
                if(!item.visibilityMax || !item.visibilityMin){
                    this.$modal.confirm('请输入能见度').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }else if(item.causeType == 2){
                if(!item.roadCondition){
                    this.$modal.confirm('请选择路面状况').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }else if(item.causeType == 3){
                if(!item.congestionDegree){
                    this.$modal.confirm('请选择拥挤度').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }else if(item.causeType == 4){
                if(!item.incidentType){
                    this.$modal.confirm('请选择事件类型').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
                if(!item.incidentGrade){
                    this.$modal.confirm('请选择事件级别').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }
        }
        for(var item of this.dataForm.measureList){
            if(!item.controlRangeMax || !item.controlRangeMin){
                this.$modal.confirm('请输入管控范围度').then(function() {
                }).then(() => {}).catch(() => {});
                return
            }else if(!item.controlMeasure){
                this.$modal.confirm('请选择措施详情').then(function() {
                }).then(() => {}).catch(() => {});
                return
            }else if(item.controlMeasure == 'limitspeed'){
                if(!item.limitSpeed){
                    this.$modal.confirm('请选择车速').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }else if(item.controlMeasure == 'limitcar'){
                if(!item.limitType){
                    this.$modal.confirm('请选择限制类型').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }else if(!item.carType){
                    this.$modal.confirm('请选择车辆类型').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }else if(item.controlMeasure == 'emergencyLine'){
                if(!item.limitSpeed){
                    this.$modal.confirm('请选择车速').then(function() {
                    }).then(() => {}).catch(() => {});
                    return
                }
            }
        }
        console.log(this.dataForm,"this.dataForm2222222")
        saveControlMeasure(this.dataForm).then(response => {
          this.openMeasures = false;
          console.log(response,"response")
          this.$modal.msgSuccess("配置成功");

        });
    },
    // 配置措施 按钮
    handleMeasures(row){
        this.dataForm.id = row.id
        const params = {
            id:row.id
        }
       getControlMeasure(params).then(response => {
         console.log(response,"response")
         this.dataForm.causeList = response.causeList
         this.dataForm.measureList = response.measureList

          this.openMeasures = true;
          this.title = "配置措施";
        });

    },

    // 取消按钮
    cancel() {
            this.$modal.confirm('确认关闭？').then(function() {

            }).then(() => {
              this.open = false;
              this.openMeasures = false

            }).catch(() => {
              return
            });

      // this.reset();
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
    // 关闭弹窗
    handleClose(){
      this.$modal.confirm('确认关闭？').then(function() {
        return
      }).then(() => {
          this.open = false;
          this.openMeasures = false
      }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除该条管控等级配置数据项？').then(function() {
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
