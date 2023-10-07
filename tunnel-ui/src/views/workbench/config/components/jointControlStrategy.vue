<template>
  <el-dialog v-dialogDrag class="timedControl" custom-class="no-scroll" title="智慧调光" :close-on-click-modal="false"
    :visible.sync="visibleSync" width="80%" append-to-body :lock-scroll="true" :before-close="closeLogin">
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
    <div class="bottomBox">
      <div class="left-content">
        <div data-v-6425604c="" class="content-header-one">
          <span data-v-6425604c="">策略分组</span>
          <el-tree :data="dataTree" :props="defaultProps" style="margin-top: 40px; height: 0px" node-key="value"
            :default-expanded-keys="[4]" :default-checked-keys="checkDefault" v-model="treeModel" default-expand-all
            highlight-current @node-click="handleNodeClickLight" ref="tree">
          </el-tree>
        </div>
      </div>
      <div class="content">
        <div class="handControl-container">
          <div data-v-6425604c="" class="content-header">
            <!--            <span data-v-6425604c="">策略清单</span>-->

            <!--            <el-row :gutter="6" class="tabTopFormRow">-->
            <!--              <el-col :span="6" :offset="12">-->
            <div class="grid-content bg-purple" style="width: 37%; float: right" ref="main">
              <el-input v-model="queryParams.strategyName" placeholder="请输入策略名称，回车搜索" clearable size="small"
                @keyup.enter.native="handleQuery">
                <el-button slot="append" class="searchTable" @click="boxShow = !boxShow"></el-button>
              </el-input>
            </div>
            <el-button style="float: left; height: 30px" size="small" type="primary" icon="el-icon-plus"
              class="tableBlueButtton" @click="openInsertStrategy" v-hasPermi="['workbench:light:add']">新增策略</el-button>
            <!--              </el-col>-->
            <!--            </el-row>-->
            <div class="searchBox searchBoxLight" v-show="boxShow" ref="cc">
              <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="75px">
                <el-form-item label="隧道名称" prop="tunnelId">
                  <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
                    <el-option v-for="item in tunnelData" :key="item.tunnelId" :label="item.tunnelName"
                      :value="item.tunnelId" />
                  </el-select>
                </el-form-item>
                <el-form-item label="方向" prop="direction">
                  <el-select v-model="queryParams.direction" placeholder="请选择方向" style="width: 100%">
                    <el-option v-for="(item, index) in directionOptions" :key="index" :label="item.dictLabel"
                      :value="item.dictValue"></el-option>
                  </el-select>
                </el-form-item>
                <!-- <el-form-item label="策略类型" prop="strategyType">
                  <el-select
                    v-model="queryParams.strategyType"
                    placeholder="请选择策略类型"
                    clearable
                    size="small"
                  >
                    <el-option
                      v-for="dict in strategyTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    />
                  </el-select>
                </el-form-item> -->
                <el-form-item class="bottomBox" style="height: 50px">
                  <el-button size="small" type="primary" @click="handleQuery">搜索
                  </el-button>
                  <el-button size="small" @click="resetQuery" type="primary" plain>重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            <!--            <el-button-->
            <!--              style="float: right; margin-right: 20px"-->
            <!--              size="small"-->
            <!--              type="primary"-->
            <!--              class="tableBlueButtton"-->
            <!--              @click="openInsertStrategy"-->
            <!--            >查询</el-button-->
            <!--            >-->
            <!--            <el-select-->
            <!--              v-model="queryParams.direction"-->
            <!--              placeholder="请选择方向"-->
            <!--              size="small"-->
            <!--              class="bottomBoxSelect"-->
            <!--              style="width: 10%;margin-right: 20px;float: right;"-->
            <!--            >-->
            <!--              <el-option-->
            <!--                v-for="(item, index) in directionOptions"-->
            <!--                :key="index"-->
            <!--                :label="item.dictLabel"-->
            <!--                :value="item.dictValue"-->
            <!--              ></el-option>-->
            <!--            </el-select>-->
            <!--            <el-select-->
            <!--              v-model="queryParams.tunnelId"-->
            <!--              placeholder="请选择隧道"-->
            <!--              clearable-->
            <!--              class="bottomBoxSelect"-->
            <!--              style="width: 15%;margin-right: 22px;float: right;"-->
            <!--              size="small"-->
            <!--            >-->
            <!--              <el-option-->
            <!--                v-for="item in tunnelData"-->
            <!--                :key="item.tunnelId"-->
            <!--                :label="item.tunnelName"-->
            <!--                :value="item.tunnelId"-->
            <!--              />-->
            <!--            </el-select>-->
          </div>
          <div v-if="treeModel == '0'" style="overflow-y: auto;overflow-x: hidden; width: 100%; height: 88%">
            <div v-for="item in strategyList" :key="item.id" class="content-centre">
              <!--              <span class="diagonal-text-no" v-if="item.strategyState==1">未生效</span>-->
              <!--              <span class="diagonal-text-yes"  v-if="item.strategyState==0">已生效</span>-->
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">名称</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.strategyName"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">隧道</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="
                    !!item.tunnels
                      ? item.tunnels.tunnelName + item.fx
                      : '' + item.fx
                  "></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">状态</el-col>
                <el-col :span="20" class="elcolNameOne">
                  <el-switch v-model="item.strategyState" active-color="#13ce66" inactive-color="#ff4949"
                    active-value="0" inactive-value="1" v-hasPermi="['workbench:light:switch']"
                    @change="changeStrategyState(item)">
                  </el-switch>
                </el-col>
              </el-row>
              <!--              <el-row :gutter="24">-->
              <!--                <el-col :span="4" class="elcolName">创建</el-col>-->
              <!--                <el-col :span="20" class="elcolNameOne"  v-html="item.createBy"></el-col>-->
              <!--              </el-row>-->
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">时序</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.execTime + '开'"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">指令</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="'含控制指令' + item.slist.length + '条'">
                </el-col>
              </el-row>
              <!--            <el-row :gutter="24">-->
              <!--              <el-col :span="4" class="elcolName">描述</el-col>-->
              <!--              <el-col :span="7" class="elcolNameOne">ew</el-col>-->
              <!--            </el-row>-->
              <el-row :gutter="24">
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableDelButtton"
                  @click="handleDelete(item)" v-hasPermi="['workbench:light:del']">删除</el-button>
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableBlueButtton"
                  @click="handleUpdate(item)">编辑</el-button>
              </el-row>
            </div>
          </div>

          <div v-if="treeModel == '1'" style="overflow-y: auto;overflow-x: hidden; width: 100%; height: 88%">
            <div v-for="item in lightStrategyList" :key="item.id" class="content-centre-one">
              <!--              <span class="diagonal-text-no" v-if="item.isStatus==1">未生效</span>-->
              <!--              <span class="diagonal-text-yes"  v-if="item.isStatus==0">已生效</span>-->
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">隧道</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.tunnelName"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">潍坊</el-col>
                <el-col :span="4" class="elcolNameOne">
                  <el-switch v-model="item.isStatus" active-color="#13ce66" inactive-color="#ff4949"
                    :active-value="parseInt(0)" :inactive-value="parseInt(1)" @change="changeSdWisdomIsStatus(item)"
                    v-hasPermi="['workbench:light:direction']">
                  </el-switch>
                </el-col>
                <el-col :span="4" class="elcolName">济南</el-col>
                <el-col :span="4" class="elcolNameOne">
                  <el-switch v-model="item.isStatusWei" active-color="#13ce66" inactive-color="#ff4949"
                    :active-value="parseInt(0)" :inactive-value="parseInt(1)"
                    @change="changeSdWisdomIsStatus(item, true)" v-hasPermi="['workbench:light:direction']">
                  </el-switch>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">指令</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="'含控制指令' + item.instructNum + ' 条'">
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">创建</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.createBy"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableDelButtton"
                  @click="lightStrategyDelete(item)" v-hasPermi="['workbench:light:del']">删除</el-button>
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableBlueButtton"
                  @click="lightStrategyUpdate(item)">编辑</el-button>
              </el-row>
            </div>
          </div>
          <div v-if="treeModel == '2'" style="overflow-y: auto;overflow-x: hidden; width: 100%; height: 88%">
            <div v-for="item in catStrategyList" :key="item.id" class="content-centre-tree">
              <!--              <span class="diagonal-text-no" v-if="item.isStatus==1">未生效</span>-->
              <!--              <span class="diagonal-text-yes"  v-if="item.isStatus==0">已生效</span>-->
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">隧道</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.tunnelName"></el-col>
              </el-row>

              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">潍坊</el-col>
                <el-col :span="4" class="elcolNameOne">
                  <el-switch v-model="item.isStatus" active-color="#13ce66" inactive-color="#ff4949"
                    :active-value="parseInt(0)" :inactive-value="parseInt(1)" @change="changeSdWisdomIsStatus(item)"
                    v-hasPermi="['workbench:light:direction']">
                  </el-switch>
                </el-col>
                <el-col :span="4" class="elcolName">济南</el-col>
                <el-col :span="4" class="elcolNameOne">
                  <el-switch v-model="item.isStatusWei" active-color="#13ce66" inactive-color="#ff4949"
                    :active-value="parseInt(0)" :inactive-value="parseInt(1)"
                    @change="changeSdWisdomIsStatus(item, true)" v-hasPermi="['workbench:light:direction']">
                  </el-switch>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">指令</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="'含控制指令' + item.instructNum + ' 条'">
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="4" class="elcolName">创建</el-col>
                <el-col :span="20" class="elcolNameOne" v-html="item.createBy"></el-col>
              </el-row>
              <el-row :gutter="24">
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableDelButtton"
                  @click="lightStrategyDelete(item)" v-hasPermi="['workbench:light:del']">删除</el-button>
                <el-button style="float: right; margin-right: 20px" size="mini" class="tableBlueButtton"
                  @click="catStrategyUpdate(item)" >编辑</el-button>
              </el-row>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog :title="title" :visible.sync="dialogVisible" :before-close="handleClose" :append-to-body="true"
      class="celueDialog" width="70%" height="500px" :close-on-click-modal="false">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <!-- 定时控制 -->
      <timingControl v-show="strategyForm.strategyType == '1'" @dialogVisibleClose="closeDialog" @refreshList="getList"
        ref="timingControl"></timingControl>
    </el-dialog>
    <!-- 光照曲线     -->
    <lightCurveModal :show="lightCurveModalShow" @selectLightStrategyList="selectLightStrategyList"
      ref="lightCurveModal"></lightCurveModal>
    <!-- 车辆曲线     -->
    <catCurveModal :show="catCurveModalShow" @selectCatStrategyList="selectCatStrategyList" ref="catCurveModal">
    </catCurveModal>
  </el-dialog>
</template>

<script>
  import * as echarts from "echarts";
  import {
    dataDevicesLogInfoList,
    dataLogInfoLineList,
  } from "@/api/equipment/eqTypeItem/item";
  import {
    addConfig,
    listConfig,
    updateConfig,
    delConfig,
    updateSdWisdomIsStatus,
  } from "@/api/business/wisdomLight/app";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    getUserDeptId
  } from "@/api/system/user";
  import {
    analysisDataByTime
  } from "@/api/system/trafficStatistics/api";
  import {
    delStrategy,
    listStrategy,
    updateState
  } from "@/api/event/strategy";

  import timingControl from "@/views/event/strategy/components/timingControl"; //定时控制
  import lightCurveModal from "./lightCurveModal";
  import catCurveModal from "./catCurveModal.vue";
  export default {
    name: "jointControlStrategy",
    components: {
      timingControl,
      lightCurveModal,
      catCurveModal,
    },
    data() {
      return {
        visibleSync: false,
        dataTree: [{
            label: "日常联控",
            value: 4,
            children: [{
              label: "时序自动控制",
              value: 0,
            }, ],
          },
          {
            label: "节能照明联控",
            value: 5,
            children: [{
                label: "亮度差联控照明",
                value: 1,
              },
              {
                label: "车流量联控照明",
                value: 2,
              },
            ],
          },
        ],
        defaultProps: {
          children: "children",
          label: "label",
          value: "value"
        },
        checkDefault: [0], //存放默认选中节点的id
        //主定时任务列表
        strategyList: [],
        //主定时任务滚动
        isOneLoading: false,
        //光照任务列表
        lightStrategyList: [],
        boxShow: false,
        //车辆任务列表
        catStrategyList: [],
        treeModel: "0",
        //光照曲线ref
        loginChart: null,
        //光强配置文件
        lightFilesModel: {
          beforeLuminance: ""
        },
        //光照查询
        queryParamsLight: {},
        //光 x  光强
        XDataLight: [],
        //光 y  时间
        yDataLight: [],
        //光 y  时间
        yDataLight1: [],
        //光 y  时间
        yDataLight2: [],
        userQueryParams: {
          userName: this.$store.state.user.name,
        },
        //部門id
        userDeptId: "",
        //隧道方向
        lightDirectionOptions: [{
            dictLabel: "济南方向",
            dictValue: "2"
          },
          {
            dictLabel: "潍坊方向",
            dictValue: "1"
          },
        ], //方向列表
        catDirectionOptions: [{
            dictLabel: "上行",
            dictValue: "1"
          },
          {
            dictLabel: "下行",
            dictValue: "2"
          },
        ],
        //车辆数配置文件
        catFilesModel: {
          beforeLuminance: ""
        },
        tunnelItems: {},
        tunnelLists: [],
        //隧道列表
        tunnelData: [],
        directionOptions: [],
        formItems: [{
          label: "",
          startTime: "",
          endTime: "",
        }, ],
        //策略实体
        strategyForm: {},
        //策略标题
        title: "",
        //编辑还是新增
        sink: "",
        // 策略类型字典
        strategyTypeOptions: [],
        // 策略类型字典
        insertStrategyTypeOptions: [],
        // 策略类型字典
        insertStrategyTypeOptionsCopy: [],
        dialogVisible: false,
        lightCurveModalShow: false,
        catCurveModalShow: false,
        queryParams: {},
      };
    },
    mounted() {
      document.addEventListener("click", this.bodyCloseMenus);
    },
    created() {
      this.treeModel = '0'
      this.$refs.tree.setCheckedNodes([{
          value: 0,
          label: '时序自动控制'
        }]);
      // 日常策略
      this.getDicts("sd_strategy_type").then((response) => {
        this.strategyTypeOptions = response.data;
        this.insertStrategyTypeOptions = response.data;
      });
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    methods: {
      bodyCloseMenus(e) {
        let self = this;
        if (self.boxShow == true) {
          self.$nextTick(() => {
            if (
              !self.$refs.main.contains(e.target) &&
              !self.$refs.cc.contains(e.target)
            ) {
              if (self.boxShow == true) {
                self.boxShow = false;
              }
            }
          });
        }
      },
      handleQuery() {
        if (this.treeModel == "0") {
          //时序自动控制
          this.selectListStrategy();
        }
        if (this.treeModel == "1") {
          //亮度差联控照明
          this.selectLightStrategyList();
        }
        if (this.treeModel == "2") {
          //车流量联控照明
          this.selectCatStrategyList();
        }
      },
      resetQuery() {
        if (this.treeModel == "0") {
          //时序自动控制
          this.selectListStrategy("resetQuery");
        }
        if (this.treeModel == "1") {
          //亮度差联控照明
          this.selectLightStrategyList("resetQuery");
        }
        if (this.treeModel == "2") {
          //车流量联控照明
          this.selectCatStrategyList("resetQuery");
        }
      },
      //主定时任务列表
      selectListStrategy(data) {
        // // // debugger
        // const loading = this.$loading({
        //   lock: true,
        //   text: 'Loading',
        //   spinner: 'el-icon-loading',
        //   background: 'rgba(0, 0, 0, 0.7)',
        //   target:'.strategy-dialog',
        // });
        let queryParams = {};
        queryParams.pageNum = 1;
        queryParams.pageSize = 999;
        queryParams.strategyType = 1;
        queryParams.strategyGroup = 1;
        // queryParams.timingType = 0;
        if (data == "resetQuery") {
          this.queryParams = {};
        }
        if (!!this.queryParams.tunnelId) {
          queryParams.tunnelId = this.queryParams.tunnelId;
        }
        if (!!this.queryParams.direction) {
          queryParams.direction = this.queryParams.direction;
        }

        listStrategy(queryParams)
          .then((response) => {
            this.strategyList = response.rows;
            this.isOneLoading = false;
            // loading.close();
          })
          .finally

        // setInterval(function(){
        //   loading.close();
        // },3000)
        ();
      },
      //光照任务列表
      selectLightStrategyList(data) {
        let queryParams = {
          pageSize: 999,
          pageNum: 1,
          modeType: 0
        };
        if (data == "resetQuery") {
          this.queryParams = {};
        }
        if (!!this.queryParams.tunnelId) {
          queryParams.tunnelId = this.queryParams.tunnelId;
        }
        //查询出原有配置并且显示
        this.lightStrategyList = [];
        listConfig(queryParams).then((response) => {
          // // // debugger
          if (response.rows.length > 0) {
            this.lightStrategyList = [];
            for (let i = 0; i < response.rows.length; i++) {
              let tunnelItem = this.lightStrategyList.find(
                (item) => item.tunnelId == response.rows[i].tunnelId
              );

              if (!!tunnelItem) {
                let tunnelItem = this.lightStrategyList.find(
                  (item) => item.tunnelId == response.rows[i].tunnelId
                );
                if (!!response.rows[i].beforeLuminance) {
                  let jsonArray = JSON.parse(response.rows[i].beforeLuminance);
                  tunnelItem.instructNum =
                    parseInt(tunnelItem.instructNum) + parseInt(jsonArray.length);
                } else {
                  tunnelItem.instructNum = 0;
                }
                tunnelItem.isStatusWei = response.rows[i].isStatus;
                tunnelItem.idWei = response.rows[i].id;
              } else {
                if (!!response.rows[i].beforeLuminance) {
                  let jsonArray = JSON.parse(response.rows[i].beforeLuminance);
                  response.rows[i].instructNum = jsonArray.length;
                } else {
                  response.rows[i].instructNum = 0;
                }
                this.lightStrategyList.push(response.rows[i]);
              }
            }
          }
          this.$forceUpdate();
        });
      },
      //车辆任务列表
      selectCatStrategyList(data) {
        let queryParams = {
          pageSize: 999,
          pageNum: 1,
          modeType: 1
        };
        if (data == "resetQuery") {
          this.queryParams = {};
        }
        if (!!this.queryParams.tunnelId) {
          queryParams.tunnelId = this.queryParams.tunnelId;
        }
        //查询出原有配置并且显示
        this.catStrategyList = [];
        listConfig(queryParams).then((response) => {
          // // // debugger
          if (response.rows.length > 0) {
            this.catStrategyList = [];
            for (let i = 0; i < response.rows.length; i++) {
              let tunnelItem = this.catStrategyList.find(
                (item) => item.tunnelId == response.rows[i].tunnelId
              );
              console.log(this.catStrategyList);
              console.log(tunnelItem);
              if (!!tunnelItem) {
                let tunnelItem = this.catStrategyList.find(
                  (item) => item.tunnelId == response.rows[i].tunnelId
                );
                if (!!response.rows[i].timeSlot) {
                  let jsonArray = JSON.parse(response.rows[i].timeSlot);
                  tunnelItem.instructNum =
                    parseInt(tunnelItem.instructNum) + parseInt(jsonArray.length);
                } else {
                  tunnelItem.instructNum = 0;
                }
                tunnelItem.isStatusWei = response.rows[i].isStatus;
                tunnelItem.idWei = response.rows[i].id;
              } else {
                if (!!response.rows[i].timeSlot) {
                  let jsonArray = JSON.parse(response.rows[i].timeSlot);
                  response.rows[i].instructNum = jsonArray.length;
                } else {
                  response.rows[i].instructNum = 0;
                }
                this.catStrategyList.push(response.rows[i]);
              }
            }
            // this.catStrategyList = []
            // for (let i = 0; i < response.rows.length; i++) {
            //   if(response.rows[i].direction==2){
            //     response.rows[i].directionName = "济南方向"
            //   }else if(response.rows[i].direction==1){
            //     response.rows[i].directionName = "潍坊方向"
            //   }else{
            //     response.rows[i].directionName = ""
            //   }
            //   if(!!response.rows[i].timeSlot){
            //     let jsonArray = JSON.parse(response.rows[i].timeSlot);
            //     response.rows[i].instructNum =jsonArray.length
            //   }else{
            //     response.rows[i].instructNum =0
            //   }
            //
            //
            //   this.catStrategyList.push( response.rows[i])
            // }
          }
          this.$forceUpdate();
        });
      },
      closeLogin() {
        // // // debugger
        this.visibleSync = false;
        this.resetQuery()
        this.treeModel = '0'
        document
          .querySelector(".left-content .el-tree-node__children .el-tree-node__content")
          .click();
      },
      handleNodeClickLight(data) {
        this.boxShow = false
        if (data.value == "0") {
          //时序自动控制
          this.treeModel = "0";
          this.selectListStrategy();
        }
        if (data.value == "1") {
          //亮度差联控照明
          this.treeModel = "1";
          this.selectLightStrategyList();
        }
        if (data.value == "2") {
          //车流量联控照明
          this.treeModel = "2";
          this.selectCatStrategyList();
        }
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
      getdate(currentDate) {
        // 生成日期字符串

        let year = currentDate.getFullYear();
        let month = String(currentDate.getMonth() + 1).padStart(2, "0");
        let day = String(currentDate.getDate()).padStart(2, "0");
        let formattedDate = year + "-" + month + "-" + day;

        return [formattedDate + " 00:00:00", formattedDate + " 23:59:59"];
      },
      //生成日期
      getCatdate(currentDate) {
        // 生成日期字符串

        let year = currentDate.getFullYear();
        let month = String(currentDate.getMonth() + 1).padStart(2, "0");
        let day = String(currentDate.getDate()).padStart(2, "0");
        let formattedDate = year + "-" + month + "-" + day;

        return [formattedDate + " 00:00:00", formattedDate + " 23:59:59"];
      },

      //车辆状态
      changeCattate(e) {
        console.log(e);
        // // // debugger
      },

      //车辆 修改隧道名称查看不同隧道 车来灯亮照明配置
      catChangeEvent() {
        let tunnel = this.tunnelData.find(
          (tunnelItem) => tunnelItem.tunnelId == this.catFilesModel.tunnelId
        );
        console.log(tunnel);
        // // // debugger
        let queryParams = {
          tunnelName: tunnel.tunnelName,
          pageSize: 1,
          pageNum: 2,
          direction: this.catFilesModel.direction,
          modeType: 1,
        };
        this.catListConfig(queryParams);
      },
      beforeLuminanceEvent(e) {
        // // // debugger
        console.log(e);
        this.catFilesModel.beforeLuminance = e;
        this.$forceUpdate();
      },

      formatTime(hours, minutes) {
        let formattedHours = hours.toString().padStart(2, "0");
        let formattedMinutes = minutes.toString().padStart(2, "0");
        return formattedHours + ":" + formattedMinutes;
      },
      deleteHandleUpdate(index) {
        if (this.formItems.length == 1) {
          return this.$modal.msgWarning("至少保留一条执行操作");
        }
        this.formItems.splice(index, 1);
        // // // debugger
      },
      addHandleUpdate(index) {
        // // // debugger
        let form = {
          label: "",
          startTime: "",
          endTime: "",
        };
        this.formItems.push(form);
      },

      closeDialog(flag) {
        this.$refs.timingControl.resetForm();
        this.$refs.timingControl.closeBoard();
        this.dialogVisible = false;
        //查询主策略
        this.selectListStrategy();
      },
      getList() {
        //查询主策略
        this.selectListStrategy();
      },
      //新建策略按钮
      openInsertStrategy(type) {
        if (this.treeModel == "0") {
          this.title = "新增策略";
          this.sink = "add";
          this.$set(this.strategyForm, "strategyType", "1");
          this.$nextTick(() => {
            // if (type == "richang") {
            this.dialogVisible = true;
            this.strategyTypeClose();
          });
        } else if (this.treeModel == "1") {
          this.lightCurveModalShow = !this.lightCurveModalShow;
          // console.log(this.$refs.lightCurveModal)
          this.$refs.lightCurveModal.getEchartsData(null, null);
        } else if (this.treeModel == "2") {
          this.catCurveModalShow = !this.catCurveModalShow;
          // console.log(this.$refs.lightCurveModal)
          this.$refs.catCurveModal.getEchartsTrend(null, null);
        }
      },
      //编辑策略
      handleUpdate(row) {
        this.title = "策略编辑";
        // // // debugger
        this.strategyForm.strategyType = row.strategyType;
        this.sink = "edit";

        this.$nextTick(() => {
          this.dialogVisible = true;
          this.strategyTypeClose(row);
        });
      },
      lightStrategyUpdate(row) {
        this.lightCurveModalShow = !this.lightCurveModalShow;
        // console.log(this.$refs.lightCurveModal)
        this.$refs.lightCurveModal.getEchartsData(row);
      },
      catStrategyUpdate(row) {
        this.catCurveModalShow = !this.catCurveModalShow;
        // console.log(this.$refs.lightCurveModal)
        this.$refs.catCurveModal.getEchartsTrend(row);
      },
      //删除策略
      handleDelete(row) {
        // // // debugger
        const ids = row.id;
        const rlIds = row.id;
        const jobRelationId = row.jobRelationId;
        this.$confirm("是否确认删除？", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .then(() => {
            delStrategy(ids).then((res) => {
              if (res.code == 200) {
                //查询主策略
                this.selectListStrategy();
                this.$forceUpdate();
                this.$modal.msgSuccess(res.msg);
              } else {
                this.$modal.msgError(res.msg);
              }
              // this.getList();
            });
          })
          .catch(function () {});
      },
      //删除照明策略
      lightStrategyDelete(row) {
        // // // debugger
        const ids = [];
        ids.push(row.id);
        ids.push(row.idWei);
        this.$confirm("是否确认删除？", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .then(() => {
            // // // debugger
            delConfig(ids).then((res) => {
              if (res.code == 200) {
                //查询照明策略
                this.selectLightStrategyList();
                this.selectCatStrategyList();
                this.$forceUpdate();
                this.$modal.msgSuccess(res.msg);
              } else {
                this.$modal.msgError(res.msg);
              }
              // this.getList();
            });
          })
          .catch(function () {});
      },
      //0:手动 1：定时 2：分时
      strategyTypeClose(row) {
        console.log(this.$refs.manualControl, "获取组件");
        this.$nextTick(() => {
          console.log(this.strategyForm.strategyType);
          switch (this.strategyForm.strategyType) {
            case "0":
              break;

            case "1":
              // // // debugger
              console.log(this.insertStrategyTypeOptions);
              this.insertStrategyTypeOptionsCopy = [];
              let dictLabelStrategy = this.insertStrategyTypeOptions.find(
                (item) => item.dictLabel == "定时控制"
              );
              this.insertStrategyTypeOptionsCopy.push(dictLabelStrategy);
              this.$refs.timingControl.sink = this.sink;
              this.$refs.timingControl.init("0", "调光");
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
        console.log(index, "index");
        switch (index) {
          case "0":
            break;
          case "1":
            this.$refs.timingControl.resetForm();
            this.$refs.timingControl.closeBoard();
            break;
          case "2":
            break;
          case "3":
            break;
        }
        this.strategyForm.strategyType = "";
        this.dialogVisible = false;
        // this.$refs.cron.checkClear();
        done();
      },
      changeStrategyState(row) {
        let data = {
          strategyId: row.id,
          change: row.strategyState
        };
        updateState(data).then((result) => {
          if (result.code == 200) {
            if (row.strategyState == 0) {
              this.$modal.msgSuccess("开启成功");
            } else {
              this.$modal.msgSuccess("关闭成功");
            }
          } else {
            this.$modal.msgSuccess(result.msg);
          }
        });
      },
      changeSdWisdomIsStatus(row, type) {
        // // // debugger
        //type==true 说明是另一个
        let data = {};
        if (type) {
          data = {
            id: row.idWei,
            isStatus: row.isStatusWei
          };
        } else {
          data = {
            id: row.id,
            isStatus: row.isStatus
          };
        }

        updateSdWisdomIsStatus(data).then((result) => {
          if (result.code == 200) {
            if (type) {
              if (row.isStatusWei == 0) {
                this.$modal.msgSuccess("开启成功");
              } else {
                this.$modal.msgSuccess("关闭成功");
              }
              this.$forceUpdate();
            } else {
              if (row.isStatus == 0) {
                this.$modal.msgSuccess("开启成功");
              } else {
                this.$modal.msgSuccess("关闭成功");
              }
            }
          } else {
            this.$modal.msgSuccess(result.msg);
          }
        });
      },
    },
    props: {
      show: Boolean,
    },
    watch: {
      checkDefault: {
        handler(newVal, oldVal) {
          // // // debugger
          if (newVal && this.visibleSync) {
            this.$nextTick(() => {
              document.querySelector(".left-content .el-tree-node__content").click();
              //默认选中第一层级的第一个节点
              document
                .querySelector(".left-content .el-tree-node__children .el-tree-node__content")
                .click();
            });
          }
        },
        immediate: true,
      },
      show: {
        async handler(newValue, oldValue) {
          // // // debugger
          this.treeModel = "0";
          this.$nextTick(() => {
            this.visibleSync = !this.visibleSync;

          })
          //查询主策略
          this.selectListStrategy();
          //查询隧道列表
          await this.getTunnels();
          //查询方向
          await this.getDirection();
          // this.$nextTick(() => {
            // // // debugger
            // document.querySelector(".left-content .el-tree-node__content").click();
            //默认选中第一层级的第一个节点
            document
              .querySelector(".left-content .el-tree-node__children .el-tree-node__content")
              .click();
            // const firstNode = document.querySelector(".left-content .el-tree-node");
            // firstNode.click();
          // });
        },
      },
    },
  };

</script>

<style scoped lang="scss">
  .bottomBox {
    width: 100%;
    height: 85vh;
    box-sizing: border-box;
    display: flex;
  }

  .left-content {
    width: 15%;
    height: 100%;
    // background: #05213E;
  }

  .content {
    // background: #05213E;
    width: 85%;
    margin-left: 10px;
  }

  .handControl-container {
    width: 100%;
    height: 100%;
  }

  .content-header {
    color: #fff;
    height: 5%;
    border-bottom: 1px solid #042d5f;
    margin: 10px 10px 10px 30px;
  }

  .content-header-one {
    font-size: 20px;
    color: #4ba2d2;
    height: 5%;
    border-bottom: 1px solid #042d5f;
    margin: 10px 10px 10px 30px;
  }

  .content-centre {
    height: 27.5%;
    width: 24%;
    position: relative;
    float: left;
    // background-color: #042d5f;
    // border: 1px solid #042d5f;
    margin-left: 10px;
    margin-top: 10px;
  }

  .content-centre-one {
    height: 23%;
    width: 24%;
    position: relative;
    float: left;
    // background-color: #042d5f;
    // border: 1px solid #042d5f;
    margin-left: 10px;
    margin-top: 10px;
  }

  .content-centre-tree {
    height: 23%;
    width: 24%;
    position: relative;
    float: left;
    // background-color: #042d5f;
    // border: 1px solid #042d5f;
    margin-left: 10px;
    margin-top: 10px;
  }

  .content-centre-login {
    margin-left: 30px;
    margin-top: 20px;
  }

  .elcolName {
    color: #b4c0ba;
    margin: 10px 0px 0px 10px;
  }

  .elcolNameOne {
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
    color: #f1d19f;
  }

  .diagonal-text-yes {
    position: absolute;
    top: 28px;
    right: -2px;
    transform: rotate(45deg);
    transform-origin: top right;
    font-size: 15px;
    color: #f1d19f;
  }

  .chartTow {
    flex: 0 0 40%;
    height: 400px;
  }

  .celueDialog {
    .el-dialog__body {
      height: 500px;
    }
  }

  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s;
  }

  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }

  .bottomBoxSelect {
    .el-input__inner {
      height: 33px;
    }
  }

  .searchBox {
    top: 12.4% !important;
    right: 2% !important;
    width: 29.4% !important;
  }

  .searchTable {
    padding: 15px;
    margin: 0px;
    width: 100% !important;
    height: 30px !important;
  }

  .timedControl {
    .el-dialog__body .handControl-container .el-input-group__append .el-button {
      border-top-right-radius: 3px !important;
      border-bottom-right-radius: 3px !important;
      border-top-left-radius: 0 !important;
      border-bottom-left-radius: 0 !important;
    }

    .el-input-group__append {
      background: transparent !important;
      border: transparent !important;
    }
  }

</style>
