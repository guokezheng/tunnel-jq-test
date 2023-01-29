<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="所属单位" prop="zzjgId">
        <el-input
          v-model="queryParams.zzjgId"
          placeholder="请输入所属单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="发布状态" prop="publishStatus">
        <el-select
          v-model="queryParams.publishStatus"
          placeholder="请选择发布状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.publish_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="任务状态" prop="taskStatus">
        <el-select
          v-model="queryParams.taskStatus"
          placeholder="请选择任务状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.task_status"
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
        <el-button size="mini" type="primary" plain @click="resetQuery"
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
          >新增</el-button
        >

      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="listList"
      @selection-change="handleSelectionChange"
      class="allTable"
    >
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="任务编号" align="center" prop="id" />-->
      <el-table-column label="所属单位" align="center" prop="zzjgId" />
      <el-table-column label="派单人员" align="center" prop="dispatcher" />
      <el-table-column
        label="派单时间"
        align="center"
        prop="dispatchTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dispatchTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="承巡班组" align="center" prop="bzId" >
      </el-table-column>
      <!--      <el-table-column label="任务描述" align="center" prop="taskDescription" />-->
      <el-table-column
        label="计划完成时间"
        align="center"
        prop="endPlantime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endPlantime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="publishStatus">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.publish_status"
            :value="scope.row.publishStatus"
          />
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.task_status"
            :value="scope.row.taskStatus"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleRecordy(scope.row)"
          >任务详情</el-button>
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleAbolish(scope.row)"
            :style="{ display: scope.row.publishStatus==2?'':'none' }"
          >废止任务</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="exportTaskReport(scope.row)"
            :style="{ display: scope.row.taskStatus==2?'':'none' }"
          >巡查报告</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:list:edit']"
            :style="{ display: scope.row.publishStatus==2?'none':'' }"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
            :style="{ display: scope.row.publishStatus==2?'none':'' }"
            >删除</el-button
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


    <el-dialog :title="title" :visible.sync="open" width="70%">
      <!--      <h1>新增巡检任务</h1>-->
      <div class="task">
        <div>巡查任务基本信息</div>
        <hr />

        <div class="form-one">
          <div>
            <span prop="dispatcher">派单人员</span>
            <div>
              <el-input
                ref="dispatcher"
                disabled = "disabled"
                v-model="form.dispatcher"
                placeholder="（默认当前登录人）"
              ></el-input>
            </div>
          </div>

          <div>
            <span>派单时间</span>
            <div>
              <el-date-picker
                clearable
                size="small"
                disabled = "disabled"
                v-model="form.dispatchTime"
                type="date"
                style="width: 89%"
                value-format="yyyy-MM-dd"
                placeholder="选择派单时间">
              </el-date-picker>
            </div>
          </div>
          <div>
            <span>指派巡查班组</span>
            <div>
              <el-select v-model="form.bzId" placeholder="请选择班组">
                <el-option
                  v-for="item in bzData"
                  :key="item.deptId"
                  :label="item.deptName"
                  :value="item.deptId"
                ></el-option>
              </el-select>
            </div>
          </div>
        </div>

        <div class="form-two">
          <div>
            <span>需完成日期</span>
            <el-date-picker
              clearable
              size="small"
              v-model="form.endPlantime"
              type="date"
              style="width: 63%"
              value-format="yyyy-MM-dd"
              placeholder="选择完成时间"
            >
            </el-date-picker>
          </div>
          <div>
            <span>所属隧道</span>
               <el-select v-model="form.tunnelId"  placeholder="请选择所属隧道" @change="tunnelSelectGet">
                 <el-option
                   v-for="item in eqTunnelData"
                   :key="item.tunnelId"
                   :label="item.tunnelName"
                   :value="item.tunnelId"
                 ></el-option>
               </el-select>

        </div>
          <div>
            <span>任务名称</span>
            <el-input
              type="text"
              placeholder="请输入内容"
              v-model="form.taskName"
              style="width: 92%;margin-left: 8%;"
            >
            </el-input>

          </div>

        </div>
        <div class="describe">
          <span>任务描述</span>
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="form.taskDescription"
          >
          </el-input>
        </div>
      </div>
      <div class="patrol">
        <div>巡查点信息</div>
        <hr />
        <div class="button-father">
          <el-button type="primary" style="height: 15%" @click="show1"
            >选择巡检点</el-button
          >
          <el-button type="primary" style="height: 15%" @click="show2"
            >选择故障点</el-button
          >
<!--          <el-button type="primary" style="height: 15%" disabled
            >导入巡查计划</el-button
          >-->
        </div>
        <div class="box-father">
          <div class="box" :key="index" v-for="(item, index) in boxList">
            <div class="contentTextRow">
              <div class="number">{{ index + 1 }}</div>
              <div class="text">
                <div>{{ item.tunnel_name }}</div>
                <div>{{ item.type_name }}</div>
                <div>{{ item.eq_name }}</div>
                <div>{{ item.pile }}</div>
              </div>
            </div>

            <template @slot="scop">
              <div
                :class="index == 0 ? 'disabledClass' : 'top'"
                @click="clickUP(index, item)"
              >
                <i class="el-icon-top"></i>
              </div>
            </template>
            <div
              :class="boxList.length == index + 1 ? 'disabledClass' : 'bottom'"
              @click="clickDown(index, item)"
            >
              <i class="el-icon-bottom"></i>
            </div>
            <div class="delete" @click="clickDelete(index,item)">
              <i class="el-icon-delete-solid"></i>
            </div>
          </div>
        </div>
        <div class="release-father">
          <el-button style="height: 20%" @click="save">暂存</el-button>
          <el-button style="height: 20%;display: none"   type="warning" @click="abolish">废止</el-button>
          <el-button style="height: 20%" type="primary" @click="release"
            >发布</el-button
          >
        </div>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isShow1" width="50%" class="show">
      <div class="show-left">
        <div class="show-title">设备位置</div>
        <el-tree
          class="tree"
          :data="treeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :show-checkbox="show_checkbox"
          :check-strictly="check_strictly"
          :filter-node-method="filterNode"
          ref="tree"
          accordion
          default-expand-all
          @node-click="handleNodeClick"
          node-key="id"
          highlight-current
        />
      </div>
      <div class="show-right">
        <div class="show-title">设备清单</div>
        <div class="right-button">
          <el-select v-model="options1value" @change="getTable">
            <el-option
              v-for="dict in dict.type.eq_system"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <div class="cancel-determine">
            <el-button @click="cancelDetermine1">取消</el-button>
            <el-button type="primary" @click="determine1">确定</el-button>
          </div>
        </div>
        <div class="table-father">
          <el-table
            ref="multipleTable1"
            :data="tableData1"
            tooltip-effect="dark"
            :header-cell-style="{ 'text-align': 'center', padding: '0px' }"
            :cell-style="{ 'text-align': 'center', padding: '0px' }"
            :header-row-style="{
              height: '30px',
              background: '#F2F2F2',
              color: '#606266',
            }"
            :row-style="{
              height: '30px',
              background: '#fff',
              color: '#606266',
            }"
            style="width: 100%; "
            border
            height="358px"
            class="dialogTable allTable"
            @selection-change="onSiteInspectionSelection"
          >
            <el-table-column type="selection" width="39"></el-table-column>
            <el-table-column prop="type_name" label="设备类型">
            </el-table-column>
            <el-table-column prop="eq_name" label="设备名称"> </el-table-column>
            <el-table-column prop="pile" label="安装位置"> </el-table-column>
            <el-table-column prop="dict_label" label="方向"> </el-table-column>
          </el-table>
          <pagination
            v-show="dialogTotal > 0"
            :total="dialogTotal"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            @pagination="getDialogList"
            small
          />
        </div>
      </div>
    </el-dialog>

    <!-- -->


    <el-dialog :visible.sync="isShow2" width="50%" class="show">
      <div class="show-left">
        <div class="show-title">故障位置</div>
        <el-tree
          class="tree"
          :data="treeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :show-checkbox="show_checkbox"
          :check-strictly="check_strictly"
          :filter-node-method="filterNode"
          ref="tree"
          accordion
          default-expand-all
          @node-click="handleNodeClick"
          node-key="id"
          highlight-current
        />
      </div>
      <div class="show-right">
        <div class="show-title">故障清单</div>
        <div class="right-button">
          <el-select v-model="options2value" @change="getGzTable">
            <el-option
              v-for="dict in dict.type.fault_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <div class="cancel-determine">
            <el-button @click="determine2">取消</el-button>
            <el-button type="primary" @click="determine2">确定</el-button>
          </div>
        </div>
        <div class="table-father">
          <el-table
            ref="multipleTable2"
            :data="tableData2"
            tooltip-effect="dark"
            :header-cell-style="{ 'text-align': 'center', padding: '0px' }"
            :cell-style="{ 'text-align': 'center', padding: '0px' }"
            :header-row-style="{
              height: '30px',
              background: '#F2F2F2',
              color: '#606266',
            }"
            :row-style="{
              height: '30px',
              background: '#fff',
              color: '#606266',
            }"
            style="width: 100%; "
            border
            height="358px"
            class="dialogTable allTable"
            @selection-change="onSiteInspectionSelection"
          >
            <el-table-column type="selection" width="39"></el-table-column>
            <el-table-column prop="type_name" label="故障类型">
            </el-table-column>
            <el-table-column prop="eq_name" label="故障设备名称"> </el-table-column>
            <el-table-column prop="pile" label="故障位置"> </el-table-column>
            <el-table-column prop="dict_label" label="故障描述"> </el-table-column>
          </el-table>
          <pagination
            v-show="dialogTotal > 0"
            :total="dialogTotal"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            @pagination="getDialogGzList"
            small
          />
        </div>
      </div>
    </el-dialog>

    <!--巡查任务及执行记录单-->
    <el-dialog :visible.sync="record" width="70%">
      <div style="text-align: center; font-size: 20px">
        巡检任务及执行记录单
      </div>
      <div class="col-1" v-for="(ite, index) in taskNews" :key="index">
        发布状态/执行状态：
        <div class="col-card" v-show="ite.publishStatus">{{ ite.publishStatus }}</div>
        <div class="col-card" v-show="ite.taskStatus">{{ ite.taskStatus }}</div>
        <div v-show="!ite.publishStatus && !ite.taskStatus">暂无状态</div>
      </div>
      <div class="card" v-for="(item, index) in taskNews" :key="index">
        <div class="card-col" style="font-size:16px">
          <div>
            任务编号：
            <span>{{ item.id }}</span>
          </div>
          <div>
            所属单位：
            <span>{{ item.zzjgId }}</span>
          </div>
          <div>
            指派巡查班组：
            <span>{{ item.bzId }}</span>
          </div>
        </div>
        <div class="card-col">
          <div>
            计划完成日期：
            <span>{{ item.endPlantime }}</span>
          </div>
          <div>
            派单人员：
            <span>{{ item.dispatcher }}</span>
          </div>
          <div>
            派单时间：
            <span>{{ item.dispatchTime }}</span>
          </div>
        </div>
        <div class="card-cols">
          <div>
            任务描述：
            <span>{{ item.taskDescription }}</span>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="card-col1" v-for="(pat, index) in patrolNews" :key="index">
          <div class="row">
            <div class="row-card1">设备巡检点</div>
            <div class="row-card2" style="margin-left: 10px">
              {{ pat.tunnelName }}
            </div>
            <div class="row-card2">{{ pat.eqName }}</div>
            <div class="row-card2" style="text-align: right">
              {{ pat.xcTime }}
            </div>
          </div>
          <div style=" padding: 10px">
            <div class="test">
              设备描述：<span>{{ pat.eqFaultDescription }}</span>
            </div>
            <div style="display: flex; margin-top: 10px">
              <div class="test" style="width: 30%">
                外观情况：
                <span>{{ pat.impression }}</span>
              </div>
              <div class="test" style="width: 30%">
                网络情况：
                <span>{{ pat.network }}</span>
              </div>
              <div class="test" style="width: 30%">
                配电情况：
                <span>{{ pat.power }}</span>
              </div>
            </div>
            <div class="card-cols">
              <div style="width: 80%">
                设备运行状态:
                <span
                  >设备状态:{{ pat.eqStatus }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备运行状态:{{
                    pat.runStatus
                  }}</span
                >
              </div>
              <div class="col-test">(检修时检测情况)</div>
            </div>
            <div class="card-cols">
              <div style="width: 80%">
                现场故障情况:
                <span>
                  {{ pat.eqFaultCode }}
                </span>
              </div>
              <div class="col-test">(检修时检测情况)</div>
            </div>
            <div class="card-cols">
              现场照片：
              <div>
                <div v-for="(pic, index) in pat.iFileList" :key="index">
                  <img :src="pic.imgUrl" :title="pic.imgName" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card" v-for="(tas, index) in taskNews" :key="index">
        <div class="card-col">
          <div class="test">
            任务执行状态：
            <span>{{ tas.taskStatus }}</span>
          </div>
          <div class="test">
            执行巡查班组：
            <span>{{ tas.bzId }}</span>
          </div>
          <div class="test">
            执行巡查人：
            <span>{{ tas.dispatcher }}</span>
          </div>
        </div>
        <div class="card-col">
          <div class="test">
            任务完成时间：
            <span>{{ tas.taskEndtime }}</span>
          </div>
          <div class="test">
            任务持续时长：
            <span>{{ tas.taskCxtime }}</span>
            <!-- <div class="chaoshi">{{ tas.ifchaosgu }}</div> -->
            <div >{{ tas.ifchaosgu }}</div>

          </div>
        </div>
        <div class="card-cols">
          <div class="test">
            任务描述：
            <span>{{ tas.taskDescription }}</span>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="table-row" v-show="taskOpt.length>0"  v-for="(item, index) in taskOpt" :key="index">
          <div style="width: 10%">操作记录</div>
          <div style="width: 10%">{{ item.optType }}</div>
          <div style="width: 20%">{{item.tunnelName}} / {{item.optPersonId}}</div>
          <div style="width: 30%">{{item.optTime}}</div>
        </div>
        <div v-show="taskOpt.length==0">
          <div   style="text-align: center;margin-top: 20px;margin-bottom: 20px">
            暂无执行记录
          </div>
        </div>
<!--        <div class="table-row">
          <div style="width: 10%">操作记录</div>
          <div style="width: 10%">派单</div>
          <div style="width: 20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width: 30%">2022/09/18 21:13:35</div>
          <div style="width: 30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
        <div class="table-row">
          <div style="width: 10%">操作记录</div>
          <div style="width: 10%">派单</div>
          <div style="width: 20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width: 30%">2022/09/18 21:13:35</div>
          <div style="width: 30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
        <div class="table-row">
          <div style="width: 10%">操作记录</div>
          <div style="width: 10%">派单</div>
          <div style="width: 20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width: 30%">2022/09/18 21:13:35</div>
          <div style="width: 30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>-->
      </div>
    </el-dialog>
  </div>
</template>

<script>
import $ from "jquery";

import {
  listList,
  getList,
  delList,
  addList,
  updateList,
  exportList,
  getTaskInfoList,
  listBz,
  treeselect,
  getDevicesList,
  abolishList, addTask, getFaultList, updateTask,
} from "@/api/electromechanicalPatrol/taskManage/task";
import {getEquipmentInfo, getRepairRecordList} from "@/api/electromechanicalPatrol/faultManage/fault";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { color } from "echarts";
import {download} from "@/utils/request";

export default {
  name: "List",
  //字典值：任务发布状态,任务状态
  dicts: ["publish_status", "task_status", "network", "power","eq_system","fault_level","opt_type"],
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true,
    },
    //节点是否可被选择
    show_checkbox: {
      type: Boolean,
      default: false,
    },
    //是否级联
    check_strictly: {
      type: Boolean,
      default: false,
    },
    //开启默认全选
    default_check_all: {
      type: Boolean,
      default: false,
    },
    //开启默认选中第一个子节点
    default_check_first: {
      type: Boolean,
      default: false,
    },
    //默认第一个子节点高亮选中
    default_select_first: {
      type: Boolean,
      default: false,
    },
    // powerCode:{
    //   type:String,
    //   default:''
    // },
  },
  data() {
    return {
      isClick:true,
      userName:'',
      currentTime:'',
      deviceType:'',
      faultLevel:'',
      // 获取巡检点 表格选中项
      dialogSelection: [],
      dialogTotal: 0,
      pageNum: 1,
      pageSize: 10,
      taskName:"",
      tunnelId: "",
      defaultProps: {
        value: "id",
        label: "label",
        children: "children",
      },
      treeData: [],
      tableData1: [],
      tableData2: [],
      //所属隧道
      eqTunnelData: {},
      options1value: "", //设备清单绑定
      options2value: "", //故障清单绑定
      boxList: [],//巡检点list
      thatboxList: [],//that巡检点list
      boxTolList: [],//巡检点故障点总list
      boxIds: "",//巡检点ids
      faultList: [],//故障点list
      options1: [], //设备清单
      record: false,
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
      // 巡查任务表格数据
      listList: [],
      //巡查班组
      bzData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 弹框内容是否重置
      openCz: false,
      openGz: false,
      //新增巡查点弹窗
      isShow1: false,
      //新增故障点弹窗
      isShow2: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        zzjgId: null,
        endPlantime: null,
        dispatcher: null,
        dispatchTime: null,
        bzId: null,
        taskDescription: null,
        publishStatus: null,
        taskStatus: null,
        walkerId: null,
        taskEndtime: null,
        taskCxtime: null,
        siteDescription: null,
      },
      // 任务详情参数
      taskNews: {
        id: "",
        zzjgId: "",
        bzId: "",
        endPlantime: "",
        dispatcher: "",
        dispatchTime: "",
        taskDescription: "",
        taskStatus:"",
        publishStatus:"",
        ifchaosgu:"",
      },
      //操作记录
      taskOpt:{
        optType:"",
        optPersonId:"",
        optTime:"",
        optDescription:"",
        tunnelName:""
      },
      //巡查点参数
      patrolNews: {
        tunnelName: "",
        xcTime: "",
        bzId: "",
        walkerId: "",
        impression: 0,
        network: 0,
        power: 0,
        eqStatus: "",
        runStatus: "",
        eqFaultDescription: "",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bzId: [
          { required: true, message: '请选择指派巡查班组', trigger: 'bzId' }
        ],
        taskDescription: [
          { required: true, message: '请填写任务描述', trigger: 'taskDescription' }
        ],
      },
      impressionOptions: [], //外观情况
      networkOptions: [], //网络情况  opt_type
      powerOptions: [], //配电情况
      optTypeOptions: [], //操作类型
    };
  },
  created() {
    this.getBz();
    this.getList();
    this.getTunnel();
    /*this.getTreeSelect();*/
    //外观情况
    this.getDicts("impression").then((response) => {
      this.impressionOptions = response.data;
    });
    //网络情况
    this.getDicts("network").then((response) => {
      this.networkOptions = response.data;
    });
    //外观情况
    this.getDicts("power").then((response) => {
      this.powerOptions = response.data;
    });
    //操作类型
    this.getDicts("opt_type").then((response) => {
      this.optTypeOptions = response.data;
    });
    //获取当前登录人
    this.userName = this.$store.state.user.name;
    this.currentTime = this.getCurrentTime();
  },
  methods: {
    /*获取当前时间*/
    getCurrentTime() {
      //获取当前时间并打印
      var _this = this;
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth()+1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
      let ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
      _this.gettime = yy+'-'+mm+'-'+dd+' '+hh+':'+mf+':'+ss;
      return  _this.gettime;
    },

    tunnelSelectGet(e){
        treeselect(this.form.tunnelId).then((response) => {
          this.treeData = response.data;
          console.log(response.data, "隧道部门树");
        });
    },

    //  上移
    clickUP(i, item) {
      if (item && i) {
        let obj = { ...this.boxList[i - 1] };
        this.boxList.splice(i - 1, 1, item);
        this.boxList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    // 下移
    clickDown(i, item) {
      if (item && typeof i === "number") {
        let obj = { ...this.boxList[i + 1] };
        this.boxList.splice(i + 1, 1, item);
        this.boxList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    unique(arr) {
      const res = new Map();
      return arr.filter((arr) => !res.has(arr.eq_id) && res.set(arr.eq_id, 1));
    },
    arraySort(property) {
      return function (a, b) {
        var value1 = a[property]
        var value2 = b[property]
        return value1 - value2
      }
    },
    clickDelete(i,item) {
      if (item && typeof i === "number") {
        //splice 操作数组的方法
        if (i > -1) {
          this.boxList.splice(i, 1);
        }
        console.log("clickDelete===================="+this.boxList);
      }
    },
    // 弹窗表格翻页
    getDialogList() {
      this.getTable()
    },
    // 弹窗表格翻页
    getDialogGzList() {
      this.getGzTable()
    },
    // 获取巡检点弹窗表格选中项
    onSiteInspectionSelection(selection) {
      console.log(selection,"selectionselectionselectionselection")
      this.dialogSelection = selection;
      console.log(this.dialogSelection, "this.dialogSelection");
    },
    /** 所属隧道 */
    getTunnel() {
      if(this.$cache.local.get("manageStation") == "1"){
        this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.eqTunnelData = response.rows;
      });
    },
    // 获取设备table
    getTable(deviceType) {
      if(deviceType){
        this.deviceType = deviceType
      }
      getDevicesList(this.tunnelId, this.deviceType,this.pageNum,this.pageSize).then((res) => {
        console.log(res, "获取设备table");
        console.log(this.boxList, "boxList");

        this.tableData1 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          console.log(this.boxList[0].eq_type, deviceType, "0000000000");
          // if (this.boxList[0].eq_type == deviceType) {
            this.tableData1.forEach((item) => {
              this.boxList.forEach((row) => {
                if (item.eq_name == row.eq_name) {
                  this.$nextTick(() => {
                    this.$refs.multipleTable1.toggleRowSelection(item, true);
                  });
                }
              });
            });
          // }
        } else {
          this.$refs.multipleTable1.clearSelection();
        }
      });
    },
    // 获取设备table
    getGzTable(deviceType) {
      if(deviceType){
        this.faultLevel = deviceType
      }
      getFaultList(this.tunnelId, this.faultLevel,this.pageNum,this.pageSize).then((res) => {
        console.log(res, "获取故障table");
        console.log("==================getFaultListthis.boxList=="+this.boxList, "boxList");
        this.tableData2 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          console.log(this.boxList[0].eq_type, deviceType, "0000000000");
          // if (this.boxList[0].eq_type == deviceType) {
            this.tableData2.forEach((item) => {
              this.boxList.forEach((row) => {
                if (item.eq_name == row.eq_name) {
                  this.$nextTick(() => {
                    this.$refs.multipleTable2.toggleRowSelection(item, true);
                  });
                }
              });
            });
          // }
        } else {
          this.$refs.multipleTable2.clearSelection();
        }
      });
    },
    //节点单击事件
    handleNodeClick(data) {
      console.log(data, "节点单击事件");
      this.tunnelId = data.id;
    },
    // 筛选节点
    filterNode(value, data) {
      console.log(value, data, "筛选节点");
      if (!value) return true;
      return data.loopName.indexOf(value) !== -1;
    },

    handleRecordy(row) {
      this.record = true;
      this.taskId = row.id;
      getTaskInfoList(this.taskId).then((response) => {
        this.taskNews = response.data.task;
        this.patrolNews = response.data.patrol;
        this.taskOpt = response.data.opt;
        this.impressionOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.impression == opt.dictValue) {
              taskitem.impression = opt.dictLabel;
            }
          });
        });

     /*   this.networkOptions.forEach((opt) => {
          if (opt.dictValue == "0") {
            this.patrolNews.forEach((taskitem) => {
              taskitem.network = opt.dictLabel;
            });
          }
          if (opt.dictValue == "1") {
            this.patrolNews.forEach((taskitem) => {
              taskitem.network = opt.dictLabel;
            });
          }
        });*/

        this.networkOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.network == opt.dictValue) {
              taskitem.network = opt.dictLabel;
            }
          });
        });

     /*  this.powerOptions.forEach((opt) => {
          if (opt.dictValue == "0") {
            this.patrolNews.forEach((taskitem) => {
              taskitem.power = opt.dictLabel;
            });
          }
          if (opt.dictValue == "1") {
            this.patrolNews.forEach((taskitem) => {
              taskitem.power = opt.dictLabel;
            });
          }
        });*/

       this.powerOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.power == opt.dictValue) {
              taskitem.power = opt.dictLabel;
            }
          });
        });


        this.optTypeOptions.forEach((opt) => {
            this.taskOpt.forEach((taskitem) => {
              if (taskitem.optType == opt.dictValue) {
                  taskitem.optType = opt.dictLabel;
              }
           });
        });


          this.taskNews.forEach((taskitem) => {
            if(this.bzData!=""){
              this.bzData.forEach((opt) => {
                if (taskitem.bzId == opt.deptId) {
                  taskitem.bzId = opt.deptName;
                }else{
                  taskitem.bzId = "";
                }
              });
            }else {
              taskitem.bzId = "";
            }

        });

      });
    },
    /** 查询巡查任务列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then((response) => {
        this.listList = response.rows;
        this.total = response.total;
        this.listList.forEach((item) =>{
          if(item.bzId=="null"||item.bzId==NaN){
            item.bzId = "";
          }else{
            if(this.bzData!=""){
              this.bzData.forEach((sitem) =>{
                if(sitem.deptId == parseInt(item.bzId)){
                  item.bzId = sitem.deptName
                }
              })
            }else{
              item.bzId = ""
            }

          }
        })
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 巡查班组 */
    getBz() {
      listBz().then((response) => {
        this.bzData = response.rows;
      });
    },
    /** 隧道部门树 */
    /*getTreeSelect() {
      alert(this.form.tunnelId);
      treeselect().then((response) => {
        this.treeData = response.data;
        console.log(response.data, "隧道部门树");
      });
    },*/

    // 表单重置
    reset() {
      this.form = {
        id: null,
        zzjgId: null,
        endPlantime: null,
        dispatcher: null,
        dispatchTime: null,
        bzId: null,
        taskDescription: null,
        publishStatus: 0,
        taskStatus: 0,
        walkerId: null,
        taskEndtime: null,
        taskCxtime: null,
        taskName:"",
        siteDescription: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.boxList=[]
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
    show1() {
     //this.tableData1 = null
     this.dialogSelection = []
     console.log(this.dialogSelection,"this.dialogSelectionthis.dialogSelectionthis.dialogSelection")
    //  this.$refs.multipleTable1.toggleRowSelection(item, true);
      this.isShow1 = true;
      //点击确定，数据还原
      if(this.openCz){
        this.options1value = "0"
        this.tableData1 = null
        this.dialogTotal = null
        console.log("========="+this.options1value);
        this.getTable(this.options1value)
      }
      this.openCz = false;

    },
    show2() {
      //this.tableData1 = null
      this.isShow2 = true;
      if(this.openGz){
        this.options2value = "0"
        this.tableData2 = null
        this.dialogTotal = null
        console.log("========="+this.options2value);
        this.getGzTable(this.options2value)
      }
      this.openGz = false;
    },
    cancelDetermine1(){
      this.dialogSelection = []
      this.isShow1 = false;

    },
    determine1() {
      this.isShow1 = false;
      this.dialogSelection.forEach((item) =>{
        item.eq_id= item.eq_id+"_1";
      })
      this.boxList = this.unique(this.boxList.concat(this.dialogSelection));
      this.dialogSelection = [];
    },
    determine2() {
      this.isShow2 = false;
      this.dialogSelection.forEach((item) =>{
        item.eq_id= item.eq_id+"_2";
      })
      this.boxList = this.unique(this.boxList.concat(this.dialogSelection));
      this.dialogSelection = [];
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.boxList = "";
      this.boxIds = ""
      this.reset();
      this.open = true;
      this.openGz = true;
      this.openCz = true;
      this.title = "新增巡查任务";
      this.tableData1 = null
      this.tableData2 = null
      this.form.dispatcher = this.userName;
      this.form.dispatchTime = this.currentTime;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      let that = this
      getList(id).then((response) => {
        that.form = response.data.task[0];
        this.boxList = response.data.list;
        console.log("handleUpdate============="+this.boxList);
        // this.tableData1 = response.data.devicesPatrolList;//巡检点
        // this.tableData2 = response.data.faultPatrolList;//故障点
        this.boxList.forEach((item) => {
            this.$nextTick(() => {
              this.$refs.multipleTable2.toggleRowSelection(item, true);
          })
        });
        this.boxList.forEach((item) =>{
          if(item.patrol_type==0){
              item.eq_id= item.eq_id+"_1";
          }else{
              item.eq_id= item.eq_id+"_2";
          }
        })
        if(this.form.taskDescription=="null"){
          this.form.taskDescription=""
        }

        if(this.form.bzId!=null&&this.form.bzId!=""&&this.form.bzId!="null"){
            this.form.bzId = parseInt(this.form.bzId)
        }else{
          this.form.bzId=""
        }
        this.boxList.sort(this.arraySort('xc_sort'))
        this.open = true;
        this.openGz = true;
        this.openCz = true;
        this.title = "修改巡查任务";
      });
    },
    exportTaskReport(row) {
      let time = parseInt(new Date().getTime() / 1000) + '';
      let fileName = '巡查报告'+time;
      download("/task/list/exportPatrolTaskReport", {taskNo:row.id}, fileName+".docx");
    },
    /** 废止按钮操作 */
    handleAbolish(row) {
      const id = row.id || this.ids
      if (id != null) {
        abolishList(id).then(response => {
          this.$modal.msgSuccess("废止成功");
          this.getList();
        });
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateList(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除巡查任务编号为"' + ids + '"的数据项？')
        .then(function () {
          return delList(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有巡查任务数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportList(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    //发布
    release() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription",this.form.taskDescription);
      this.fileData.append("publishStatus","2");
      this.fileData.append("taskStatus","0");
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("taskName", this.form.taskName);
      //判断是否选择点
      if(this.form.bzId==-1||this.form.bzId==""||this.form.bzId==null){
        this.$modal.msgWarning("请指派巡查班组");
        return
      }
      //判断两个字段是否填写
      if (this.form.tunnelId == ""||this.form.tunnelId == -1||this.form.tunnelId==null) {
        return this.$modal.msgWarning('请选择所属隧道')
      }
      if (this.form.taskName == "") {
        return this.$modal.msgWarning('请填写任务名称')
      }
      if(this.boxList==[]||this.boxList==""){
        this.$modal.msgWarning("请选择巡检点或故障点");
        return
      }

      this.boxIds = "";
      this.boxList.forEach((item) =>{
        this.boxIds = this.boxIds+(item.eq_id+",");
      })
      this.fileData.append("devicesList",this.boxIds);
      if (this.form.id != null) {
        if(this.isClick){
          updateTask(this.fileData).then((response) => {
            this.isClick = false;
            this.$modal.msgSuccess("发布成功");
            this.open = false;
            this.getList();
          });
        }
      }else{
        if(this.isClick){
          addTask(this.fileData).then((response) => {
            this.isClick = false;
            this.$modal.msgSuccess("发布成功");
            this.open = false;
            this.getList();
          });
        }

      }
      setTimeout(() => {
        this.isClick = true;
      }, 500)
    },
//废止
    abolish() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription",this.form.taskDescription);
      this.fileData.append("publishStatus","1");
      this.fileData.append("taskStatus","");
      //判断是否选择点
      if(this.boxList==[]||this.boxList==""){
        this.$modal.msgWarning("请选择巡检点或故障点");
        return
      }
      this.boxList.forEach((item) =>{
        this.boxIds = this.boxIds+(item.eq_id+",");
      })
      this.fileData.append("devicesList",this.boxIds);

      if (this.form.id != null) {
        updateTask(this.fileData).then((response) => {
          this.$modal.msgSuccess("废止成功");
          this.open = false;
          this.getList();
        });
      }else{
        addTask(this.fileData).then((response) => {
          this.$modal.msgSuccess("废止成功");
          this.open = false;
          this.getList();
        });
      }
  },
//暂存
    save() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription",this.form.taskDescription);
      this.fileData.append("publishStatus","0");
      this.fileData.append("taskStatus","");
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("taskName", this.form.taskName);
      //判断是否选择点
      if(this.boxList==[]||this.boxList==""){
        this.$modal.msgWarning("请选择巡检点或故障点");
        return
      }
      //判断两个字段是否填写
      if (this.form.tunnelId == ""||this.form.tunnelId == -1||this.form.tunnelId==null) {
        return this.$modal.msgWarning('请选择隧道名称')
      }
      if (this.form.taskName == "") {
        return this.$modal.msgWarning('请填写任务名称')
      }
      this.boxList.forEach((item) =>{
        this.boxIds = this.boxIds+(item.eq_id+",");
      })
      this.fileData.append("devicesList",this.boxIds);

      if (this.form.id != null) {
        if(this.isClick) {
          updateTask(this.fileData).then((response) => {
            this.isClick = false;
            this.$modal.msgSuccess("暂存成功");
            this.open = false;
            this.getList();
          });
        }
      }else{
        if(this.isClick){
          addTask(this.fileData).then((response) => {
            this.isClick = false;
            this.$modal.msgSuccess("暂存成功");
            this.open = false;
            this.getList();
          });
        }

      }
        setTimeout(() => {
          this.isClick = true;
        }, 500)
    },
  },
};
</script>
<style lang="scss">
.el-table tr {
  background-color: transparent;
}
</style>
<style lang="scss" scoped>
.card {
  font-size: 15px;
  position: relative;
  width: 100%;
  padding: 20px;
  margin-top: 20px;
  border-radius: 10px;
  background-color: #f0f0f0;
  .card-col {
    margin-top: 10px;
    display: flex;
    color: #79949c;
    .chaoshi {
      padding: 5px;
      color: #ffd69a;
      display: inline;
      margin-left: 10px;
      border: 1px solid #ffd69a;
    }
    div {
      width: 33%;
      span {
        color: black;
        margin-left: 10px;
      }
    }
  }
  .card-cols {
    margin-top: 10px;
    display: flex;
    .col-test {
      text-align: right;
      color: #79949c;
    }
    img {
      width: 100px;
      margin-left: 20px;
    }
  }
  .card-col1 {
    font-size: 17px;
    .row {
      margin-top: 5px;
      display: flex;
      background-color: white;
      padding: 10px;
      // justify-content: center;
      align-items: center;
      .row-card1 {
        padding: 5px;
        background-color: #f0f0f0;
        margin-left: 5px;
      }
      .row-card2 {
        width: 29%;
      }
    }
  }
}
.col-1 {
  font-size: 20px;
  display: flex;
  justify-content: right;
  align-items: center;
  text-align: right;
  .col-card {
    padding: 10px;
    margin-left: 5px;
    color: #3e9e70;
    background-color: rgba(230, 243, 235, 1);
  }
}
.card-cols {
  margin-top: 10px;
  display: flex;
  .col-test {
    width: 20%;
    text-align: right;
    color: #79949c;
  }
  img {
    width: 100px;
    margin-left: 20px;
  }
}
.table-row {
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  div {
    text-align: center;
    margin-left: 15px;
    border-bottom: 1px solid black;
  }
}
.test {
  color: #79949c;
  span {
    color: black;
    margin-left: 5px;
  }
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}
::v-deep .el-dialog {
  margin-top: 7vh !important;
  .el-dialog__body {
    padding: 15px 20px;
  }
}
.task {
  margin-bottom: 30px;
  .form-one,
  .form-two {
    display: flex;
    justify-content: space-between;
    margin-top: 12px;
    div {
      display: flex;
      align-items: center;
      width: 33%;
      &:nth-child(3) {
        ::v-deep .el-input {
          width: 100%;
        }
      }
      span {
        display: inline-block;
        width: 30%;
      }
      div {
        width: 70%;
        ::v-deep .el-input {
          width: 90%;
        }
        ::v-deep .el-select {
          width: 100%;
        }
      }
    }
  }
  .describe {
    width: 100%;
    display: flex;
    align-items: center;
    margin-top: 12px;
    span {
      display: inline-block;
      width: 11%;
    }
  }
}
.patrol {
  .button-father {
    display: flex;
    justify-content: end;
    .el-button {
      height: 14px;
      display: flex;
      align-items: center;
    }
  }
  .box-father {
    padding: 0 5px;
    margin-top: 20px;
    margin-bottom: 20px;
    .box {
      display: flex;
      margin-top: 5px;
      align-items: center;

      .contentTextRow {
        display: flex;
        width: calc(100% - 100px);
      }
      .number {
        width: 30px;
        height: 32px;
        border: 1px solid rgba(215, 215, 215, 1);
        border-radius: 3px;
        font-weight: 400;
        color: #333;
        /*vertical-align: none;*/
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 10px;
      }
      .text {
        width: calc(100% - 50px);
        height: 32px;
        padding-left: 10px;
        background: inherit;
        border: 1px solid rgba(215, 215, 215, 1);
        border-radius: 3px;
        font-weight: 400;
        font-size: 14px;
        margin-right: 20px;
        display: flex;
        align-items: center;
        > div:nth-of-type(1) {
          width: 120px;
        }
        > div:nth-of-type(2) {
          width: 100px;
          margin-left: 20px;
        }
        > div:nth-of-type(3) {
          margin-left: 20px;
          width: 150px;
        }
        > div:nth-of-type(4) {
          margin-left: 20px;
          width: 150px;
        }
        > div:nth-of-type(5) {
          margin-left: 20px;
          width: 150px;
        }
      }
      .top,
      .bottom,
      .delete {
        width: 24px;
        height: 24px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: skyblue;
        border: 1px solid skyblue;
        border-radius: 50%;
        cursor: pointer;
      }
      .disabledClass {
        width: 24px;
        height: 24px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #ccc;
        border: 1px solid #ccc;
        border-radius: 50%;
        margin-right: 10px;
      }
      .top,
      .bottom {
        margin-right: 10px;
      }
    }
  }
  .release-father {
    display: flex;
    justify-content: center;
    .el-button {
      width: 100px;
      height: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
.show {
  ::v-deep .el-dialog__body {
    display: flex;
    .show-left,
    .show-right {
      height: 478px;
      // border: 1px solid black;
      border-radius: 3px;
      .show-title {
        font-weight: 400;
        font-style: normal;
        background-color: rgba(242, 242, 242, 1);
        color: #5e7f89;
        border-radius: 3px;
        line-height: 30px;
        // border-bottom: 1px solid rgb(204, 204, 204);
        padding-left: 10px;
      }
    }
    .show-left {
      width: 25%;
      margin-right: 10px;
    }
    .show-right {
      width: 75%;
      .right-button {
        background-color: rgba(248, 248, 248, 1);
        padding: 5px 10px;
        display: flex;
        justify-content: space-between;
        .el-input__inner {
          height: 26px;
        }
        .el-input__suffix {
          top: 5px;
        }
        .el-input__icon {
          line-height: inherit;
        }
        .el-input__suffix-inner {
          display: inline-block;
        }
        .cancel-determine {
          display: flex;
          .el-button {
            height: 14px;
            display: flex;
            align-items: center;
          }
        }
      }
      .table-father {
        padding: 10px 10px;
        .el-table {
          font-size: 12px;
        }
        // .el-table--enable-row-hover .el-table__body tr:hover > td {
        //   background: #fff !important;
        // }
        // .pagination-container {
        //   position: static !important;
        //   margin-top: 0px !important;
        //   .el-pagination__total,
        //   .el-pagination__jump {
        //     color: #606266 !important;
        //   }
        //   .el-input__inner {
        //     background: #fff !important;
        //     color: #606266 !important;
        //   }
        //   .el-pagination.is-background .btn-prev,
        //   .el-pagination.is-background .btn-next,
        //   .el-pagination.is-background .el-pager li {
        //     background: #fff !important;
        //     color: #606266 !important;
        //   }
        // }
      }
    }
  }
}
::v-deep .tree {
  height: 445px;
  overflow: auto;
  .el-tree-node:focus > .el-tree-node__content {
    background-color: #89c2f7;
    color: #fff;
  }
  .el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content {
    background-color: #89c2f7;
    color: #fff;
  }
}
</style>
