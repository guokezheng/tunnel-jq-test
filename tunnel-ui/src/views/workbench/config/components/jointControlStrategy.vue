<template>
  <el-dialog
    class="timedControl"
    custom-class="no-scroll"
    title="联控策略"
    :close-on-click-modal="false"
    :visible.sync="visibleSync"
    width="80%"
    :destroy-on-close="false"
    append-to-body
    :lock-scroll="true"
    :before-close="closeLogin"
  >
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
    <div class="bottomBox">
      <div class="left-content">
        <div data-v-6425604c="" class="content-header-one">
          <span data-v-6425604c="">策略分组</span>
          <el-tree :data="dataTree" :props="defaultProps"  style="margin-top: 40px;height: 0px" v-model="treeModel"  default-expand-all @node-click="handleNodeClick">
          </el-tree>
        </div>
      </div>
      <div class="content">
        <div class="handControl-container">
          <div data-v-6425604c="" class="content-header">
            <span data-v-6425604c="">策略清单</span>
            <el-button
              style="float: right; margin-right: 20px"
              size="mini"
              type="primary"
              icon="el-icon-plus"
              class="tableBlueButtton"
              @click="openInsertStrategy"
            >新增策略</el-button
            >
          </div>
          <div  v-if="treeModel==0" style=" overflow: auto; width: 100%; height: 88%;">
            <div v-for="item in strategyList" :key="item.id" class="content-centre">
            <span class="diagonal-text-no" v-if="item.strategyState==0">未生效</span>
            <span class="diagonal-text-yes"  v-if="item.strategyState==1">已生效</span>
            <el-row :gutter="24">
              <el-col :span="4" class="elcolName">名称</el-col>
              <el-col :span="20" class="elcolNameOne" v-html="item.strategyName"></el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="4" class="elcolName">隧道</el-col>
              <el-col :span="20" class="elcolNameOne"  v-html="!!item.tunnels ?item.tunnels.tunnelName  +  item.fx:''  +  item.fx"></el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="4" class="elcolName">创建</el-col>
              <el-col :span="20" class="elcolNameOne"  v-html="item.createBy"></el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="4" class="elcolName">时序</el-col>
              <el-col :span="20" class="elcolNameOne">09:30:00开</el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="4" class="elcolName">指令</el-col>
              <el-col :span="20" class="elcolNameOne">含控制指令 3 条</el-col>
            </el-row>
            <!--            <el-row :gutter="24">-->
            <!--              <el-col :span="4" class="elcolName">描述</el-col>-->
            <!--              <el-col :span="7" class="elcolNameOne">ew</el-col>-->
            <!--            </el-row>-->
            <el-row :gutter="24">
              <el-button
                style="float: right ;margin-right: 20px"
                size="mini"
                class="tableDelButtton"
                @click="handleDelete(item)"
              >删除</el-button
              >
              <el-button
                style="float: right ;margin-right: 20px"
                size="mini"
                class="tableBlueButtton"
                @click="handleUpdate(item)"
              >编辑</el-button
              >
            </el-row>
          </div>
          </div>
          <div v-if="treeModel==1"  style=" overflow: auto; width: 100%; height: 88%;" >
            <div v-for="item in lightStrategyList" :key="item.id" class="content-centre-one">
              <span class="diagonal-text-no" v-if="item.isStatus==0">未生效</span>
              <span class="diagonal-text-yes"  v-if="item.isStatus==1">已生效</span>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">隧道</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.tunnelName  +  item.directionName"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">下修</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.beforeLuminance"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">创建</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.createBy"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-button
                  style="float: right ;margin-right: 20px"
                  size="mini"
                  class="tableDelButtton"
                  @click="lightStrategyDelete(item)"
                >删除</el-button
                >
                <el-button
                  style="float: right ;margin-right: 20px"
                  size="mini"
                  class="tableBlueButtton"
                  @click=" lightStrategyUpdate(item)"
                >编辑</el-button
                >
              </el-row>
            </div>
<!--            <el-row :gutter="24" style="clear:both;">-->
<!--              <el-col :span="13">-->
<!--                <div  class="loginChart" ref="loginChart" style="width: 100%; height: 400px; float: left"></div>-->
<!--              </el-col>-->
<!--              <el-col :span="8">-->
<!--                <el-form-->
<!--                  ref="loginQueryForm"-->
<!--                  :model="lightFilesModel"-->
<!--                  :inline="true"-->
<!--                  class="loginQueryFormClass"-->
<!--                  label-width="100px"-->
<!--                  height="300px"-->
<!--                >-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="隧道名称" prop="tunnelId">-->
<!--                        <el-select-->
<!--                          style="width: 100%"-->
<!--                          v-model="lightFilesModel.tunnelId"-->
<!--                          placeholder="请选择隧道"-->
<!--                          clearable-->
<!--                          @change="lightChangeEvent"-->
<!--                        >-->
<!--                          <el-option-->
<!--                            v-for="item in tunnelData"-->
<!--                            :key="item.tunnelId"-->
<!--                            :label="item.tunnelName"-->
<!--                            :value="item.tunnelId"-->
<!--                          />-->
<!--                        </el-select>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="隧道方向" prop="direction">-->
<!--                        <el-select-->
<!--                          clearable-->
<!--                          v-model="lightFilesModel.direction"-->
<!--                          placeholder="请选择隧道方向"-->
<!--                          @change="lightChangeEvent"-->
<!--                          style="width: 100%"-->
<!--                        >-->
<!--                          <el-option-->
<!--                            v-for="dict in lightDirectionOptions"-->
<!--                            :key="dict.value"-->
<!--                            :label="dict.dictLabel"-->
<!--                            :value="dict.dictValue"-->
<!--                          />-->
<!--                        </el-select>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="状态" align="center" prop="schedulerTime">-->
<!--                        <template slot-scope="scope">-->
<!--                          <el-switch-->
<!--                            v-model="lightFilesModel.isStatus"-->
<!--                            active-color="#13ce66"-->
<!--                            inactive-color="#ff4949"-->
<!--                            active-value="0"-->
<!--                            inactive-value="1"-->
<!--                            @change="changeCattate()"-->
<!--                          >-->
<!--                          </el-switch>-->
<!--                        </template>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                  <el-row>-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="下修比例"  >-->
<!--                        <el-input  v-model="lightFilesModel.beforeLuminance"></el-input>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                </el-form>-->
<!--              </el-col>-->
<!--              <el-col :span="3">-->

<!--                <el-button-->
<!--                  size="mini"-->
<!--                  class="tableBlueButtton"-->
<!--                  @click="submitlightForm"-->
<!--                >保存</el-button-->
<!--                >-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  class="tableBlueButtton"-->
<!--                  @click="refreshlightForm"-->
<!--                >刷新</el-button-->
<!--                >-->
<!--              </el-col>-->
<!--            </el-row>-->

          </div>
          <div  v-if="treeModel==2" style=" overflow: auto; width: 100%; height: 88%;" >
            <div v-for="item in catStrategyList" :key="item.id" class="content-centre-tree">
              <span class="diagonal-text-no" v-if="item.isStatus==0">未生效</span>
              <span class="diagonal-text-yes"  v-if="item.isStatus==1">已生效</span>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">隧道</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.tunnelName  +  item.directionName"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">下修</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.beforeLuminance"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">指令</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="'含控制指令'+item.instructNum +' 条'"> </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">创建</el-col>
                <el-col :span="20" class="elcolNameOne"  v-html="item.createBy"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-button
                  style="float: right ;margin-right: 20px"
                  size="mini"
                  class="tableDelButtton"
                  @click="lightStrategyDelete(item)"
                >删除</el-button
                >
                <el-button
                  style="float: right ;margin-right: 20px"
                  size="mini"
                  class="tableBlueButtton"
                  @click=" catStrategyUpdate(item)"
                >编辑</el-button
                >
              </el-row>
            </div>
<!--            <el-row :gutter="24" style="clear:both;">-->
<!--              <el-col :span="13">-->
<!--                <div  id='trend' class="chartTow" ref="chart1" style="width: 100%; height: 400px; float: left"></div>-->
<!--              </el-col>-->
<!--              <el-col :span="8">-->
<!--                <el-form-->
<!--                  ref="loginQueryForm"-->
<!--                  :model="catFilesModel"-->
<!--                  :inline="true"-->
<!--                  class="loginQueryFormClass"-->
<!--                  label-width="70px"-->
<!--                  height="300px"-->
<!--                >-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="隧道名称" prop="tunnelId">-->
<!--                        <el-select-->
<!--                          style="width: 100%"-->
<!--                          v-model="catFilesModel.tunnelId"-->
<!--                          placeholder="请选择隧道"-->
<!--                          clearable-->
<!--                          @change="catChangeEvent"-->
<!--                        >-->
<!--                          <el-option-->
<!--                            v-for="item in tunnelData"-->
<!--                            :key="item.tunnelId"-->
<!--                            :label="item.tunnelName"-->
<!--                            :value="item.tunnelId"-->
<!--                          />-->
<!--                        </el-select>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->

<!--                  </el-row>-->
<!--                  <el-row :gutter="24">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="方向" prop="direction">-->
<!--                        <el-select-->
<!--                          clearable-->
<!--                          v-model="catFilesModel.direction"-->
<!--                          placeholder="请选择隧道方向"-->
<!--                          @change="catChangeEvent"-->
<!--                          style="width: 100%"-->
<!--                        >-->
<!--                          <el-option-->
<!--                            v-for="dict in catDirectionOptions"-->
<!--                            :key="dict.value"-->
<!--                            :label="dict.dictLabel"-->
<!--                            :value="dict.dictValue"-->
<!--                          />-->
<!--                        </el-select>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="状态" align="center" prop="schedulerTime">-->
<!--                        <template slot-scope="scope">-->
<!--                          <el-switch-->
<!--                            v-model="catFilesModel.isStatus"-->
<!--                            active-color="#13ce66"-->
<!--                            inactive-color="#ff4949"-->
<!--                            active-value="0"-->
<!--                            inactive-value="1"-->
<!--                            @change="changeCattate"-->
<!--                          >-->
<!--                          </el-switch>-->
<!--                        </template>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->

<!--                  </el-row>-->
<!--                  <el-row :gutter="24" style="clear:both;">-->
<!--                    <el-col :span="24">-->
<!--                      <el-form-item label="下修比例"  >-->
<!--                        <el-input  v-model="catFilesModel.beforeLuminance"    @change="beforeLuminanceEvent"></el-input>-->
<!--                      </el-form-item>-->
<!--                    </el-col>-->
<!--                  </el-row>-->
<!--                  <el-form-item label="时间段"  v-for="(item, index) in formItems" :key="index" >-->
<!--                    &lt;!&ndash;                <el-time-picker&ndash;&gt;-->
<!--                    &lt;!&ndash;                  style="width: 35%"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  v-model="item.startTime"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  size="small"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  :picker-options="pickerOptions"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  placeholder="选择开始时间">&ndash;&gt;-->
<!--                    &lt;!&ndash;                </el-time-picker>&ndash;&gt;-->
<!--                    <el-time-picker-->
<!--                      placeholder="选择开始时间"-->
<!--                      v-model="item.startTime"-->
<!--                      style="width: 38%"-->
<!--                    ></el-time-picker>-->

<!--                    &lt;!&ndash;                <el-time-picker&ndash;&gt;-->
<!--                    &lt;!&ndash;                  style="width: 35%"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  v-model="item.endTime"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  size="small"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  :picker-options="pickerOptions"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  placeholder="选择结束时间">&ndash;&gt;-->
<!--                    &lt;!&ndash;                </el-time-picker>&ndash;&gt;-->
<!--                    <el-time-picker-->
<!--                      placeholder="选择结束时间"-->
<!--                      v-model="item.endTime"-->
<!--                      style="width: 38%"-->
<!--                    ></el-time-picker>-->
<!--                    &lt;!&ndash;                <el-button&ndash;&gt;-->
<!--                    &lt;!&ndash;                  style="width: 10%"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  size="small"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  class="tableBlueButtton"&ndash;&gt;-->
<!--                    &lt;!&ndash;                  @click="addHandleUpdate(index)"&ndash;&gt;-->
<!--                    &lt;!&ndash;                >+&ndash;&gt;-->
<!--                    &lt;!&ndash;                </el-button&ndash;&gt;-->
<!--                    &lt;!&ndash;                ><el-button&ndash;&gt;-->
<!--                    &lt;!&ndash;                style="width: 10%"&ndash;&gt;-->
<!--                    &lt;!&ndash;                size="small"&ndash;&gt;-->
<!--                    &lt;!&ndash;                class="tableBlueButtton"&ndash;&gt;-->
<!--                    &lt;!&ndash;                @click="deleteHandleUpdate(index)"&ndash;&gt;-->
<!--                    &lt;!&ndash;              >- &ndash;&gt;-->
<!--                    &lt;!&ndash;              </el-button&ndash;&gt;-->
<!--                    &lt;!&ndash;              >&ndash;&gt;-->
<!--                    &lt;!&ndash;                <el-col :span="2" class="buttonBox">&ndash;&gt;-->
<!--                    <div class="buttonBox" style="width: 20% ;float: right;" >-->
<!--                      <el-button-->
<!--                        class="delete"-->
<!--                        @click="deleteHandleUpdate(index)"-->
<!--                      ></el-button>-->
<!--                      <el-button-->
<!--                        class="add"-->
<!--                        @click="addHandleUpdate(index)"-->
<!--                      ></el-button>-->
<!--                    </div>-->
<!--                    &lt;!&ndash;                </el-col>&ndash;&gt;-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--              </el-col>-->
<!--              <el-col :span="3">-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  class="tableBlueButtton"-->
<!--                  @click="submitCatForm"-->
<!--                >保存</el-button-->
<!--                >-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  class="tableBlueButtton"-->
<!--                  @click="catHandleSave"-->
<!--                >刷新</el-button-->
<!--                >-->
<!--              </el-col>-->
<!--            </el-row>-->
          </div>

      </div>
    </div>
    </div>

    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :before-close="handleClose"
      :append-to-body="true"
      class="celueDialog"
      width="70%"
      height="500px"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <!-- 定时控制 -->
      <timingControl
        v-show="strategyForm.strategyType == '1'"
        @dialogVisibleClose="closeDialog"
        @refreshList = "getList"
        ref="timingControl"
      ></timingControl>
    </el-dialog>
    <!-- 光照曲线     -->
    <lightCurveModal
      :show="lightCurveModalShow"
      ref="lightCurveModal"
    ></lightCurveModal>
    <!-- 车辆曲线     -->
    <catCurveModal
      :show="catCurveModalShow"
      ref="catCurveModal"
    ></catCurveModal>
  </el-dialog>

</template>

<script>
import * as echarts from "echarts";
import {dataDevicesLogInfoList, dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";
import {addConfig, listConfig, updateConfig} from "@/api/business/enhancedLighting/app";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {getUserDeptId} from "@/api/system/user";
import {analysisDataByTime} from "@/api/system/trafficStatistics/api";
import {delStrategy, listStrategy} from "@/api/event/strategy";
import {
  delConfig,
} from "@/api/business/enhancedLighting/app.js";
import timingControl from "@/views/event/strategy/components/timingControl"; //定时控制
import lightCurveModal from "./lightCurveModal";
import catCurveModal from "./catCurveModal.vue";
export default {
  name: "jointControlStrategy",
  components: {
    timingControl,
    lightCurveModal,
    catCurveModal
  },
  data() {
    return {
      visibleSync:false,
      dataTree: [
        {
          label: '日常联控',
          children: [
            {
            label: '时序自动控制', value:'0',
            }
          ]
        },
        {
          label: '节能照明联控',
          children: [
            {
            label: '亮度差联控照明', value:'1',
            },
            {
              label: '车流量联控照明', value:'2',
            }
          ]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      //主定时任务列表
      strategyList:[],
      //光照任务列表
      lightStrategyList:[],
      //车辆任务列表
      catStrategyList:[],
      treeModel:0,
      //光照曲线ref
      loginChart: null,
      //光强配置文件
      lightFilesModel:{beforeLuminance:''},
      //光照查询
      queryParamsLight:{},
      //光 x  光强
      XDataLight:[],
      //光 y  时间
      yDataLight:[],
      //光 y  时间
      yDataLight1:[],
      //光 y  时间
      yDataLight2:[],
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      //部門id
      userDeptId:'',
      //隧道方向
      lightDirectionOptions: [
        {dictLabel:"济南方向",dictValue:"2"},
        {dictLabel:"潍坊方向",dictValue:"1"}
      ],//方向列表
      catDirectionOptions: [
        {dictLabel:"上行",dictValue:"1"},
        {dictLabel:"下行",dictValue:"2"}
      ],
      //车辆数配置文件
      catFilesModel:{beforeLuminance:''},
      tunnelItems:{},
      tunnelLists:[],
      //隧道列表
      tunnelData:[],
      formItems: [
        {
          label: '',
          startTime: '',
          endTime: '',
        }
      ],
      //策略实体
      strategyForm:{},
      //策略标题
      title:'',
      //编辑还是新增
      sink:'',
      // 策略类型字典
      strategyTypeOptions: [],
      // 策略类型字典
      insertStrategyTypeOptions: [],
      // 策略类型字典
      insertStrategyTypeOptionsCopy: [],
      dialogVisible:false,
      lightCurveModalShow:false,
      catCurveModalShow:false,
    }
  },
  mounted() {
  },
  created(){
    // 日常策略
    this.getDicts("sd_strategy_type").then((response) => {
      this.strategyTypeOptions = response.data;
      this.insertStrategyTypeOptions = response.data;
    });
  },
  methods:{
    //主定时任务列表
    selectListStrategy(){
      let  queryParams = {}
      queryParams.pageNum= 1
      queryParams.pageSize= 999
      queryParams.strategyType =1
      queryParams.strategyGroup =1
      listStrategy(queryParams).then((response) => {
        this.strategyList = response.rows;
        console.log(this.strategyList, 'this.strategyListthis.strategyListthis.strategyListthis.strategyList')
      });
    },
    //光照任务列表
    selectLightStrategyList(){

      let queryParams = {pageSize:999,pageNum:1,modeType:0}
      //查询出原有配置并且显示
      listConfig(queryParams).then((response) => {
        debugger
        if( response.rows.length>0){
          this.lightStrategyList = []
          for (let i = 0; i < response.rows.length; i++) {
            if(response.rows[i].direction==1){
              response.rows[i].directionName = "上行"
            }else if(response.rows[i].direction==2){
              response.rows[i].directionName = "下行"
            }else{
              response.rows[i].directionName = ""
            }

            this.lightStrategyList.push( response.rows[i])
          }
        }
        this.$forceUpdate();
      })
    },
    //车辆任务列表
    selectCatStrategyList(){

      let queryParams = {pageSize:999,pageNum:1,modeType:1}
      //查询出原有配置并且显示
      listConfig(queryParams).then((response) => {
        debugger
        if( response.rows.length>0){
          this.catStrategyList = []
          for (let i = 0; i < response.rows.length; i++) {
            if(response.rows[i].direction==1){
              response.rows[i].directionName = "济南方向"
            }else if(response.rows[i].direction==2){
              response.rows[i].directionName = "潍坊方向"
            }else{
              response.rows[i].directionName = ""
            }
            if(!!response.rows[i].timeSlot){
              let jsonArray = JSON.parse(response.rows[i].timeSlot);
              response.rows[i].instructNum =jsonArray.length
            }else{
              response.rows[i].instructNum =0
            }


            this.catStrategyList.push( response.rows[i])
          }
        }
        this.$forceUpdate();
      })
    },
    closeLogin(){
      debugger
      this.visibleSync = false
    },
    handleNodeClick(data) {
      if(data.value == 0 ){//时序自动控制
        this.treeModel = 0
      }
      if(data.value == 1 ){//亮度差联控照明
        this.treeModel = 1
        this.selectLightStrategyList()
        // //获取光亮的数据
        // this.getEchartsData()
      }
      if(data.value == 2 ){//车流量联控照明
        this.treeModel = 2
        this.selectCatStrategyList()
        //获取车辆数据
        // this.getEchartsTrend()
      }
    },
    //获取光亮的数据
    getEchartsData(tunnelList ,tunnelItem ,firstType) {
      if(!!tunnelList){
        this.tunnelLists = tunnelList
      }
      if(!!tunnelItem){
        this.tunnelItems = tunnelItem
      }
      //清空上次数据
      this.XDataLight = []
      this.yDataLight = []
      this.yDataLight1 = []
      this.yDataLight2 = []
      let querysParamsTab = {}
      querysParamsTab.tunnelId =   this.lightFilesModel.tunnelId
      querysParamsTab.pageNum =  1
      querysParamsTab.pageSize =  999
      //类型 外部测光设备
      querysParamsTab.searchValue =  4
      //根据隧道id获取相应外部测光设备
      dataDevicesLogInfoList(this.addDateRange(querysParamsTab)).then(
        (response) => {
          //返回设备
          let listTab = response.rows;
          if(listTab.length>0){
            if(firstType ){//首次打开页面  要先查询出原有配置并且显示
              if(listTab[0].direction=="济南方向"){
                this.lightFilesModel.direction = "2"
              }
              if(listTab[0].direction=="潍坊方向"){
                this.lightFilesModel.direction = "1"
              }
              let tunnelItems ={}
              if(!!this.tunnelItems.tunnelId){
                tunnelItems = this.tunnelItems
              }else{
                tunnelItems = this.tunnelLists[0]
              }
              this.lightFilesModel.tunnelId = tunnelItems.tunnelId
              // let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.lightFilesModel.tunnelId)
              // console.log(tunnel)
              debugger
              let queryParams = {tunnelName:tunnelItems.tunnelId,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}
              //查询出原有配置并且显示
              this.lightListConfig(queryParams)
            }
            this.queryParamsLight.direction = this.lightFilesModel.direction
            if(this.lightFilesModel.direction=="2"){//济南
              let rows = listTab.find(item => item.direction=="济南方向")
              //设备id
              this.queryParamsLight.deviceId= rows.eqId
            }
            if(this.lightFilesModel.direction=="1"){//潍坊
              let rows = listTab.find(item => item.direction=="潍坊方向")
              //设备id
              this.queryParamsLight.deviceId= rows.eqId
            }

            this.queryParamsLight.pageNum = 1;
            this.queryParamsLight.pageSize = 10;
            this.queryParamsLight.searchValue = 4;
            // 获取当前日期
            let currentDate = new Date();

            // 获取前天日期
            let twoDaysAgo = new Date();
            twoDaysAgo.setDate(currentDate.getDate() - 2);

            // 获取大前天日期
            let threeDaysAgo = new Date();
            threeDaysAgo.setDate(currentDate.getDate() - 1);

            let ds = this.getdate(currentDate)//当前
            let ds1 = this.getdate(twoDaysAgo)//前天
            let ds2 = this.getdate(threeDaysAgo)//昨天
            // this.XDataLight = this.generateTimeList()
            //查询光照曲线
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds1)
            ).then((response) => {
              let list1 = response.rows;
              for (let i = 0; i < list1.length; i++) {
                //前天
                this.yDataLight.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds2)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              //昨天光强
              for (let i = 0; i < list1.length; i++) {
                this.yDataLight1.push(list1[i].data)
              }
            });
            dataLogInfoLineList(
              this.addDateRange(this.queryParamsLight, ds)
            ).then((response) => {
              debugger
              let list1 = response.rows;
              //今天
              for (let i = 0; i < list1.length; i++) {
                this.XDataLight.push(list1[i].createTime)
                this.yDataLight2.push(list1[i].data)
              }
              debugger
              setTimeout(() => {
                this.$nextTick(() => {
                  this.initLoginChart();
                });
              }, 500);
            });
          }
        }
      );
    },
    // 获取光照照图表数据信息
    initLoginChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.LoginMychart = echarts.init(this.$refs.loginChart);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '洞内亮度',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天光强", "昨天光强", "今天光强"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XDataLight,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "光强",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              name: "前天光强",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLight,
            },
            {
              name: "昨天光强",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLight1,
            },
            {
              name: "今天光强",
              type: "line",
              color: "#FFB200",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yDataLight2,
            },
          ],
        };

        this.LoginMychart.setOption(option);
        window.addEventListener("resize", function () {
          this.LoginMychart.resize();
        });
      });
    },
    //光强照明配置查询
    lightListConfig(queryParams){
      debugger
      listConfig(queryParams).then((response) => {
        debugger
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.lightFilesModel.id = rows.id
          this.lightFilesModel.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.lightFilesModel.beforeLuminance =  rows.beforeLuminance
          }else{
            this.lightFilesModel.beforeLuminance =''
          }
        }else{
          this.lightFilesModel.id = ''
          this.lightFilesModel.beforeLuminance =''
        }
        this.$forceUpdate();
      })
    },
    /** 查询隧道列表 */
    async getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      await listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    async getDirection() {
      await this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    //生成日期
    getdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    //生成日期
    getCatdate(currentDate){

      // 生成日期字符串

      let year = currentDate.getFullYear();
      let month = String(currentDate.getMonth() + 1).padStart(2, '0');
      let day = String(currentDate.getDate()).padStart(2, '0');
      let formattedDate = year + '-' + month + '-' + day;

      return [formattedDate + ' 00:00:00', formattedDate + ' 23:59:59'];

    },
    //光强 修改隧道名称查看不同隧道 光强照明配置
    lightChangeEvent(indextabs) {
      let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.lightFilesModel.tunnelId)
      console.log(tunnel)
      debugger
      let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.lightFilesModel.direction,modeType:0}

      this.lightListConfig(queryParams)
      this.$forceUpdate()
    },
    //车辆状态
    changeCattate(e){
      console.log(e)
      debugger
    },
    //光照下修比例保存
    submitlightForm(){
      debugger
      console.log(this.lightFilesModel)
      //模式1 车辆 0光强
      this.lightFilesModel.modeType = 0
      if (!!this.lightFilesModel.id) {
        updateConfig(this.lightFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
        });
      } else {
        addConfig(this.lightFilesModel).then((response) => {
          debugger
          this.lightFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
        });
      }
    },
    //刷新光照图表
    refreshlightForm(){
      console.log(this.lightFilesModel)
      // this.getEchartsData()
    },

    //获取车辆数据
    getEchartsTrend(){
      this.XData = []
      this.yData3 = []
      this.yData2 = []
      this.yData1 = []

      // 获取当前日期
      let currentDate = new Date();
      // 获取前天日期
      let twoDaysAgo = new Date();
      twoDaysAgo.setDate(currentDate.getDate() - 2);
      // 获取大前天日期
      let threeDaysAgo = new Date();
      threeDaysAgo.setDate(currentDate.getDate() - 1);
      let ds = this.getCatdate(currentDate)//今天
      let ds1 = this.getCatdate(twoDaysAgo)//前天
      let ds2 = this.getCatdate(threeDaysAgo)//昨天

      let tunnelItems = null;
      if(!!this.tunnelItems.tunnelId){
        tunnelItems = this.tunnelItems
      }else{
        tunnelItems = this.tunnelLists[0]
      }
      debugger
      console.log(this.catFilesModel.tunnelId)
      if(!!tunnelItems.tunnelId&&(this.catFilesModel.tunnelId==null||this.catFilesModel.tunnelId==""||this.catFilesModel.tunnelId=="undefined")){//首次
        // this.queryParamsLight.tunnelId = tunnelItems.tunnelId
        this.catFilesModel.tunnelId= tunnelItems.tunnelId
        this.catFilesModel.direction= "1"
        let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.catFilesModel.tunnelId)
        console.log(tunnel)
        debugger
        //车辆
        let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.catFilesModel.direction,modeType:1}
        this.catListConfig(queryParams)
        // listConfig(queryParams).then((response) => {
        //   let rows = response.rows[0];
        //   this.catFilesModel.isStatus =  rows.isStatus
        //   this.catFilesModel.beforeLuminance =  rows.beforeLuminance
        //
        //   let jsonArray = JSON.parse(rows.timeSlot);
        //   console.log(jsonArray)
        //   debugger
        //   for (let i = 0; i < jsonArray.length; i++) {
        //
        //   }
        //   this.formItems
        //   // this.total = response.total;
        //   // this.loading = false;
        // });
        // this.$forceUpdate()
      }

      let json = {
        eqDirection:"hour",
        tunnelId: this.catFilesModel.tunnelId,
        holes: this.catFilesModel.direction
      }
      //今天
      analysisDataByTime(this.addDateRange(json, ds)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            const randomNumber = Math.floor(Math.random() * 70);
            this.XData.push(response.data[i].date)
            // this.yData3.push(response.data[i].byVehicelNum)
            this.yData3.push(randomNumber)
          }
        }
      });
      //昨天
      analysisDataByTime(this.addDateRange(json, ds2)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            const randomNumber = Math.floor(Math.random() * 70);
            // this.yData2.push(response.data[i].byVehicelNum)
            this.yData2.push(randomNumber)
          }
        }
      });
      //前天
      analysisDataByTime(this.addDateRange(json, ds1)).then(response => {
        if(response.code == 200){
          for (let i = 0; i < response.data.length; i++) {
            // this.XData.push(response.data[i].date)
            // this.yData1.push(response.data[i].byVehicelNum)
            const randomNumber = Math.floor(Math.random() * 70);
            this.yData1.push(randomNumber)
          }

        }
      });
      setTimeout(() => {
        this.$nextTick(() => {
          debugger
          console.log( this.yData1)
          console.log( this.yData2)
          console.log( this.yData3)
          console.log(  this.XData)
          //获取车辆数据
          this.initCatChart();
        });
      }, 500);
    },
    //车辆 修改隧道名称查看不同隧道 车来灯亮照明配置
    catChangeEvent(){
      let  tunnel = this.tunnelData.find(tunnelItem => tunnelItem.tunnelId ==  this.catFilesModel.tunnelId)
      console.log(tunnel)
      debugger
      let queryParams = {tunnelName:tunnel.tunnelName,pageSize:1,pageNum:2,direction:this.catFilesModel.direction,modeType:1}
      this.catListConfig(queryParams)
    },
    beforeLuminanceEvent(e){
      debugger
      console.log(e)
      this.catFilesModel.beforeLuminance  = e
      this.$forceUpdate()
    },
    //车辆照明配置查询
    catListConfig(queryParams){
      listConfig(queryParams).then((response) => {
        if( response.rows.length>0){
          let rows = response.rows[0];
          this.catFilesModel.id = rows.id
          this.catFilesModel.isStatus =  rows.isStatus.toString()
          if(!!rows.beforeLuminance){
            this.catFilesModel.beforeLuminance =  rows.beforeLuminance
          }else{
            this.catFilesModel.beforeLuminance =''
          }


          let jsonArray = JSON.parse(rows.timeSlot);
          if(!!jsonArray){
            this.formItems =JSON.parse(rows.timeSlot);
            if(this.formItems){
              for (let index = 0; index < this.formItems.length; index++) {
                const param = this.formItems[index];
                param.startTime = new Date(
                  ("1970-01-01 " + param.startTime).replace(/-/g, "/")
                );
                param.endTime = new Date(
                  ("1970-01-01 " + param.endTime).replace(/-/g, "/")
                );
              }
            }
          }else{
            this.formItems =[
              {
                label: '',
                startTime: '',
                endTime: '',
              }
            ]
          }
        }else{
          this.catFilesModel.id = ''
          this.catFilesModel.beforeLuminance =''
          this.formItems =[
            {
              label: '',
              startTime: '',
              endTime: '',
            }
          ]
        }
        this.$forceUpdate();
        // this.total = response.total;
        // this.loading = false;
      })
    },
    generateTimeList() {
      let timeList = [];
      let date = new Date();
      date.setHours(0, 0, 0, 0); // 设置时间为凌晨00:00
      console.log(date.getHours() )
      console.log(date.getMinutes() )
      while (date.getHours() < 24 ) {
        timeList.push(this.formatTime(date.getHours(), date.getMinutes()));
        if(this.formatTime(date.getHours(), date.getMinutes())=="23:58"){
          break
        }
        date.setMinutes(date.getMinutes() + 2);
      }
      return timeList;
    },
    formatTime(hours, minutes) {
      let formattedHours = hours.toString().padStart(2, '0');
      let formattedMinutes = minutes.toString().padStart(2, '0');
      return formattedHours + ':' + formattedMinutes;
    },
    deleteHandleUpdate(index){
      if (this.formItems.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.formItems.splice(index,1)
      debugger
    },
    addHandleUpdate(index){
      debugger
      let form={
        label: '',
        startTime: '',
        endTime: ''
      }
      this.formItems.push(form)
    },
    //车来灯亮配置保存
    submitCatForm(){
      //formItems 校验
      for (let index = 0; index < this.formItems.length; index++) {
        const element = this.formItems[index];
        if (element.startTime == null || element.startTime == "") {
          this.$modal.msgError("时间区间开始时间不能为空,请填写开始时间");
          return;
        } else if (element.endTime == null || element.endTime == "") {
          this.$modal.msgError("时间区间结束时间不能为空,请填写结束时间");
          return;
        }
      }
      if(this.catFilesModel.beforeLuminance== null || this.catFilesModel.beforeLuminance== ""){
        this.$modal.msgError("下修比例不能为空");
        return;
      }
      if(this.catFilesModel.tunnelId== null || this.catFilesModel.tunnelId== ""){
        this.$modal.msgError("隧道名称不能为空");
        return;
      }
      if(this.catFilesModel.direction== null || this.catFilesModel.direction== ""){
        this.$modal.msgError("隧道方向不能为空");
        return;
      }
      //处理时间转json
      let formItemsOne = JSON.parse(JSON.stringify(this.formItems))
      for (let index = 0; index < this.formItems.length; index++) {
        const param = this.formItems[index];
        param.direction = this.catFilesModel.direction
        param.startTime = new Date( param.startTime);
        param.startTime =
          (param.startTime.getHours() < 10
            ? "0" + param.startTime.getHours()
            : param.startTime.getHours()) +
          ":" +
          (param.startTime.getMinutes() < 10
            ? "0" + param.startTime.getMinutes()
            : param.startTime.getMinutes()) +
          ":" +
          (param.startTime.getSeconds() < 10
            ? "0" + param.startTime.getSeconds()
            : param.startTime.getSeconds());
        param.endTime = new Date( param.endTime);
        param.endTime =
          (param.endTime.getHours() < 10
            ? "0" + param.endTime.getHours()
            : param.endTime.getHours()) +
          ":" +
          (param.endTime.getMinutes() < 10
            ? "0" + param.endTime.getMinutes()
            : param.endTime.getMinutes()) +
          ":" +
          (param.endTime.getSeconds() < 10
            ? "0" + param.endTime.getSeconds()
            : param.endTime.getSeconds());
      }
      this.catFilesModel.timeSlot = JSON.stringify(this.formItems);

      this.formItems =  formItemsOne
      console.log(this.formItems)
      console.log(this.catFilesModel)
      debugger
      //模式1 车辆 0光强
      this.catFilesModel.modeType = 1
      if (!!this.catFilesModel.id) {
        updateConfig(this.catFilesModel).then((response) => {
          this.$modal.msgSuccess("修改成功");
          this.open = false;
          this.$refs.tableFile.clearSelection();
          this.getList();
        });
      } else {
        addConfig(this.catFilesModel).then((response) => {
          debugger
          this.catFilesModel.id =response.data.id
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      }
    },
    //刷新车辆
    catHandleSave(){
      //获取车辆数据
      this.getEchartsTrend()
    },
    // 获取图表数据信息
    initCatChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart1 = echarts.init(this.$refs.chart1);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '历史车辆数',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天车辆数", "昨天车辆数", "今天车辆数"],
            textStyle: {
              //图例文字的样式
              color: "#00AAF2",
              fontSize: 12,
            },
            top: "20",
          },
          grid: {
            top: "30%",
            bottom: "18%",
            left: "14%",
            right: "12%",
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            data: this.XData,
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: "#386D88",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "车辆数",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
            axisLabel: {
              textStyle: {
                color: "#00AAF2",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              name: "前天车辆数",
              type: "line",
              color: "#787FFE",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData1,
            },
            {
              name: "昨天车辆数",
              type: "line",
              color: "#00DCA2",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData2,
            },
            {
              name: "今天车辆数",
              type: "line",
              color: "#FFB200",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              data: this.yData3,
            },
          ],
        };

        this.mychart1.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart1.resize();
        });
      });
    },
    closeDialog(flag) {
      this.$refs.timingControl.resetForm();
      this.$refs.timingControl.closeBoard();
      this.dialogVisible = false
      //查询主策略
      this.selectListStrategy()
    },
    getList(){
      //查询主策略
      this.selectListStrategy()
    },
    //新建策略按钮
    openInsertStrategy(type) {
      this.title = "新增策略";
      this.sink = "add";
      // 日常策略
      // this.strategyForm.strategyType = 0;
      // if(type=="shoudong"){
      //   this.$set(this.strategyForm, "strategyType", "0");
      // }else if(type=="dingshi"){
        this.$set(this.strategyForm, "strategyType", "1");
      // }else if(type=="zidong"){
      //   this.$set(this.strategyForm, "strategyType", "2");
      // }

      this.$nextTick(() => {
        // if (type == "richang") {
        this.dialogVisible = true;
        this.strategyTypeClose();
        // } else if (type == "event") {
        //   this.dialogVisibleEvent = true;
        //   this.strategyTypeEventClose();
        // }
      })
    },
    //编辑策略
    handleUpdate(row){
      this.title = "策略编辑";
      debugger
      this.strategyForm.strategyType = row.strategyType;
      this.sink = "edit";

      this.$nextTick(() => {
        this.dialogVisible = true;
        this.strategyTypeClose(row);
      });
    },
    lightStrategyUpdate(row){
      this.lightCurveModalShow = !this.lightCurveModalShow
      // console.log(this.$refs.lightCurveModal)
      this.$refs.lightCurveModal.getEchartsData(row)
    },
    catStrategyUpdate(row){
      this.catCurveModalShow = !this.catCurveModalShow
      // console.log(this.$refs.lightCurveModal)
      this.$refs.catCurveModal.getEchartsTrend(row)
    },
    //删除策略
    handleDelete(row){
      debugger
      const ids = row.id;
      const rlIds = row.id ;
      const jobRelationId = row.jobRelationId;
      this.$confirm("是否确认删除？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        delStrategy(ids).then((res) => {
          if (res.code == 200) {
            //查询主策略
            this.selectListStrategy()
            this.$forceUpdate()
            this.$modal.msgSuccess(res.msg);
          } else {
            this.$modal.msgError(res.msg);
          }
          // this.getList();
        });
      })
        .catch(function () {
        });
    },
    //删除照明策略
    lightStrategyDelete(row){
      debugger
      const ids = row.id;
      const rlIds = row.id ;
      const jobRelationId = row.jobRelationId;
      this.$confirm("是否确认删除？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        delConfig(ids).then((res) => {
          if (res.code == 200) {
            //查询照明策略
            this.selectLightStrategyList()
            this.$forceUpdate()
            this.$modal.msgSuccess(res.msg);
          } else {
            this.$modal.msgError(res.msg);
          }
          // this.getList();
        });
      })
        .catch(function () {
        });
    },
    //0:手动 1：定时 2：分时
    strategyTypeClose(row) {
      console.log(this.$refs.manualControl, "获取组件");
      this.$nextTick(() => {
        console.log(this.strategyForm.strategyType)
        switch (this.strategyForm.strategyType) {
          case "0":

            break;

          case "1":
            debugger
            console.log(this.insertStrategyTypeOptions)
            this.insertStrategyTypeOptionsCopy=[]
            let dictLabelStrategy = this.insertStrategyTypeOptions.find((item) => item.dictLabel =="定时控制")
            this.insertStrategyTypeOptionsCopy.push(dictLabelStrategy)
            this.$refs.timingControl.sink = this.sink;
            this.$refs.timingControl.init();
            if (this.sink == "edit") {
              this.$nextTick(() => {
                // this.$refs.timingControl.sink = "edit";
                this.$refs.timingControl.id = row.id;
                this.$refs.timingControl.getStrategyData(row);
              });
            }
            break;

          case "2":

            break;

          case "3":

            break;
        }
      });
    },
    //关闭drawer
    handleClose(done) {
      let index = this.strategyForm.strategyType;
      console.log(index,"index")
      switch (index) {
        case '0':
          break;
        case '1':
          this.$refs.timingControl.resetForm();
          this.$refs.timingControl.closeBoard();
          break;
        case '2':
          break;
        case '3':
          break;
      }
      this.strategyForm.strategyType = "";
      this.dialogVisible = false;
      // this.$refs.cron.checkClear();
      done();
    },
  },
  props:{
    show:Boolean,

  },
  watch:{
    show:{
      async handler(newValue, oldValue){
        debugger
        this.visibleSync = !this.visibleSync
        //查询主策略
        this.selectListStrategy()
        //查询隧道列表
        await this.getTunnels()
        //查询方向
        await this.getDirection()

        this.$nextTick(() => {
           // this.getEchartsData(true)
        });

      }
    }
  }
}
</script>

<style scoped>
.bottomBox{
  width: 100%;
  height: 85vh;
  box-sizing: border-box;
  display: flex;
}
.left-content{
  width: 15%;
  height: 100%;
  background: #061a32;
}
.content{
  background: #061a32;
  width: 85%;
  margin-left: 10px;
}
.handControl-container{
  width: 100%;
  height: 100%;
}
.content-header{
  color: #fff;
  height: 5%;
  border-bottom: 1px solid #042d5f;
  margin: 10px 10px 10px 30px;
}
.content-header-one{
  font-size: 20px;
  color: #4BA2D2;
  height: 5%;
  border-bottom: 1px solid #042d5f;
  margin: 10px 10px 10px 30px;
}
.content-centre{
  height: 27%;
  width: 30%;
  position: relative;
  float: left;
  background-color: #042D5F;
  border: 1px solid #042d5f;
  margin-left: 30px;
  margin-top: 20px;
}
.content-centre-one{
  height: 20%;
  width: 30%;
  position: relative;
  float: left;
  background-color: #042D5F;
  border: 1px solid #042d5f;
  margin-left: 30px;
  margin-top: 20px;
}
.content-centre-tree{
  height: 23%;
  width: 30%;
  position: relative;
  float: left;
  background-color: #042D5F;
  border: 1px solid #042d5f;
  margin-left: 30px;
  margin-top: 20px;
}
.content-centre-login{
  margin-left: 30px;
  margin-top: 20px;
}
.elcolName{
  color:#B4C0BA;
  margin: 10px 0px 0px 10px;
}
.elcolNameOne{
  margin: 10px 0px 0px -15px;
}
/*.content-centre div{*/
/*  height: 5px;*/
/*  background-color: red;*/
/*  width: 100%;*/
/*}*/
.square {
  width: 200px;
  height: 200px;
  background-color: red;
  position: relative;
}

.diagonal-text-no {
  position: absolute;
  top: 28px;
  right: -2px;
  transform: rotate(45deg);
  transform-origin: top right;
  font-size: 15px;
  color: #F1D19F;
}
.diagonal-text-yes {
  position: absolute;
  top: 28px;
  right: -2px;
  transform: rotate(45deg);
  transform-origin: top right;
  font-size: 15px;
  color: #F1D19F;
}
.chartTow {
  flex: 0 0 40%;
  height: 400px;
}
.celueDialog {
  .el-dialog__body{
    height: 500px;
  }
}
</style>
