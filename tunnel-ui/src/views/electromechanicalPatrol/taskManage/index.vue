<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属单位" prop="zzjgId">
        <el-input
          v-model="queryParams.zzjgId"
          placeholder="请输入所属单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="计划完成时间" prop="endPlantime">
        <el-date-picker clearable size="small"
          v-model="queryParams.endPlantime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择计划完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="派单人员" prop="dispatcher">
        <el-input
          v-model="queryParams.dispatcher"
          placeholder="请输入派单人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="派单时间" prop="dispatchTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.dispatchTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择派单时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="指派巡查班组" prop="bzId">
        <el-input
          v-model="queryParams.bzId"
          placeholder="请输入指派巡查班组id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务描述" prop="taskDescription">
        <el-input
          v-model="queryParams.taskDescription"
          placeholder="请输入任务描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布状态" prop="publishStatus">
        <el-select v-model="queryParams.publishStatus" placeholder="请选择发布状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择任务状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡查人员" prop="walkerId">
        <el-input
          v-model="queryParams.walkerId"
          placeholder="请输入巡查人员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务完成时间" prop="taskEndtime">
        <el-date-picker clearable size="small"
          v-model="queryParams.taskEndtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择任务完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="任务持续时间" prop="taskCxtime">
        <el-date-picker clearable size="small"
          v-model="queryParams.taskCxtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择任务持续时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="现场情况描述" prop="siteDescription">
        <el-input
          v-model="queryParams.siteDescription"
          placeholder="请输入现场情况描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
        >新增</el-button>
        <!--      </el-col>
              <el-col :span="1.5">-->
<!--        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:list:edit']"
        >修改</el-button>-->
        <!--      </el-col>
              <el-col :span="1.5">-->
<!--        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:list:remove']"
        >删除</el-button>-->
        <!--      </el-col>
              <el-col :span="1.5">-->
<!--        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:list:export']"
        >导出</el-button>-->
      </el-form-item>
    </el-form>

<!--    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">-->

<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="listList"
    @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务编号" align="center" prop="id" />
<!--      <el-table-column label="所属单位" align="center" prop="zzjgId" />-->
      <el-table-column label="派单人员" align="center" prop="dispatcher" />
      <el-table-column label="派单时间" align="center" prop="dispatchTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dispatchTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="承巡班组" align="center" prop="bzId" />
<!--      <el-table-column label="任务描述" align="center" prop="taskDescription" />-->
      <el-table-column label="计划完成时间" align="center" prop="endPlantime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endPlantime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="publishStatus" >
      <template slot-scope="scope">
        <dict-tag :options="dict.type.publish_status" :value="scope.row.publishStatus"/>
      </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus" >
      <template slot-scope="scope">
        <dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus"/>
      </template>
      </el-table-column>
<!--      <el-table-column label="巡查人员" align="center" prop="walkerId" />-->
<!--      <el-table-column label="任务完成时间" align="center" prop="taskEndtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskEndtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
<!--      <el-table-column label="任务持续时间" align="center" prop="taskCxtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskCxtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
<!--      <el-table-column label="现场情况描述" align="center" prop="siteDescription" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRecordy(scope.row)"
          >任务详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:list:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
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

    <!-- 添加或修改巡查任务对话框 -->
<!--    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属单位" prop="zzjgId">
          <el-input v-model="form.zzjgId" placeholder="请输入所属单位" />
        </el-form-item>
        <el-form-item label="计划完成时间" prop="endPlantime">
          <el-date-picker clearable size="small"
            v-model="form.endPlantime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择计划完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="派单人员" prop="dispatcher">
          <el-input v-model="form.dispatcher" placeholder="请输入派单人员" />
        </el-form-item>
        <el-form-item label="派单时间" prop="dispatchTime">
          <el-date-picker clearable size="small"
            v-model="form.dispatchTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择派单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指派巡查班组id" prop="bzId">
          <el-input v-model="form.bzId" placeholder="请输入指派巡查班组id" />
        </el-form-item>
        <el-form-item label="任务描述" prop="taskDescription">
          <el-input v-model="form.taskDescription" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-radio-group v-model="form.publishStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-radio-group v-model="form.taskStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="巡查人员id" prop="walkerId">
          <el-input v-model="form.walkerId" placeholder="请输入巡查人员id" />
        </el-form-item>
        <el-form-item label="任务完成时间" prop="taskEndtime">
          <el-date-picker clearable size="small"
            v-model="form.taskEndtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择任务完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="任务持续时间" prop="taskCxtime">
          <el-date-picker clearable size="small"
            v-model="form.taskCxtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择任务持续时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="现场情况描述" prop="siteDescription">
          <el-input v-model="form.siteDescription" placeholder="请输入现场情况描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>-->

    <el-dialog :title="title" :visible.sync="open" width="70%">
<!--      <h1>新增巡检任务</h1>-->
      <div class="task">
        <div>巡查任务基本信息</div>
        <hr />

        <div class="form-one">
          <div>
            <span>派单人员</span>
            <div>
              <el-input
                v-model="form.dispatcher"
                placeholder="（默认当前登录人）"
              ></el-input>
            </div>
          </div>
          <div>
            <span>派单时间</span>
            <div>
              <el-date-picker clearable size="small"
                              v-model="form.dispatchTime"
                              type="date"
                              style = "width:89%"
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
            <el-date-picker clearable size="small"
                            v-model="form.endPlantime"
                            type="date"
                            style = "width:63%"
                            value-format="yyyy-MM-dd"
                            placeholder="选择完成时间">
            </el-date-picker>
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
          <el-button type="primary" style = "height:15%" @click="show1">选择巡检点</el-button>
          <el-button type="primary" style = "height:15%" @click="show2">选择故障点</el-button>
          <el-button type="primary" style = "height:15%" disabled>导入巡查计划</el-button>
        </div>
        <div class="box-father">
          <div class="box" :key="index" v-for="(item, index) in boxList">
            <div class="number">{{ item.number }}</div>
            <div class="text">{{ item.text }}</div>
            <template @slot="scop">
              <div class="top" @click="clickUP(index)">
                <i class="el-icon-top"></i>
              </div>
            </template>
            <div class="bottom" @click="clickDown(index)">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="delete" @click="clickDelete(index)">
              <i class="el-icon-delete-solid"></i>
            </div>
          </div>
        </div>
        <div class="release-father">
          <el-button style = "height:20%">暂存</el-button>
          <el-button style = "height:20%" type="warning">废止</el-button>
          <el-button style = "height:20%" type="primary" @click="release">发布</el-button>
        </div>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isShow1" width="50%" class="show">
      <div class="show-left">
        <div class="show-title">设备位置</div>
        <!-- <el-tree
          class="tree"
          :data="treeData"
          :props="defaultProps"
          accordion
          @node-click="handleNodeClick"
          node-key="id"
         /> -->
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
         />
      </div>
      <div class="show-right">
        <div class="show-title">设备清单</div>
        <div class="right-button">
          <el-select v-model="options1value">
            <el-option
              v-for="item in options1"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <div class="cancel-determine">
            <el-button @click="determine1">取消</el-button>
            <el-button type="primary" @click="determine1">确定</el-button>
          </div>
        </div>
        <div class="table-father">
          <el-table
            ref="multipleTable"
            :data="tableData1"
            tooltip-effect="dark"
            :header-cell-style="{ 'text-align': 'center', padding: '0px' }"
            :cell-style="{ 'text-align': 'center', padding: '0px' }"
            :header-row-style="{ height: '30px' }"
            :row-style="{ height: '30px' }"
            style="width: 100%"
            border
          >
            <el-table-column type="selection" width="39"></el-table-column>
            <el-table-column prop="type" label="设备类型" width="90">
            </el-table-column>
            <el-table-column prop="name" label="设备名称" width="135">
            </el-table-column>
            <el-table-column prop="address" label="安装位置"> </el-table-column>
            <el-table-column prop="describe" label="设备描述" width="135">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>


   <!--巡查任务及执行记录单-->
    <el-dialog
      :visible.sync="record"
      width="70%"
    >
      <div style="text-align: center;font-size: 20px;">巡检任务及执行记录单</div>
      <div class="col-1">
        发布状态/执行状态：
        <div class="col-card">已发布</div>
        <div class="col-card">已完结</div>
      </div>
      <div class="card"  v-for="(item,index) in taskNews" :key="index">
        <div class="card-col">
          <div>
            任务编号：
            <span>{{item.id}}</span>
          </div>
          <div>
            所属单位：
            <span>{{item.zzjgId}}</span>
          </div>
          <div>
            指派巡查班组：
            <span>{{item.bzId}}</span>
          </div>
        </div>
        <div class="card-col">
          <div>
            计划完成日期：
            <span>{{item.endPlantime}}</span>
          </div>
          <div>
            派单人员：
            <span>{{item.dispatcher}}</span>
          </div>
          <div>
            派单时间：
            <span>{{item.dispatchTime}}</span>
          </div>
        </div>
        <div class="card-cols">
          <div>
            任务描述：
            <span>{{item.taskDescription}}</span>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="card-col1" v-for="(pat,index) in patrolNews" :key="index">
          <div class="row">
            <div class="row-card1">设备巡检点</div>
            <div class="row-card2" style="margin-left:10px">{{pat.tunnelName}}</div>
            <div class="row-card2">{{pat.eqName}}</div>
            <div class="row-card2" style="text-align:right">{{pat.xcTime}}</div>
          </div>
          <div style="background-color: white;padding: 10px;">
            <div class="test">设备描述：<span>{{pat.eqFaultDescription}}</span></div>
            <div style="display:flex;margin-top: 10px;">
              <div class="test" style="width:30%">外观情况：
                <span>{{pat.impression}}</span>
              </div>
              <div class="test" style="width:30%">网络情况：
                <span>{{pat.network}}</span>
              </div>
              <div class="test" style="width:30%">配电情况：
                <span>{{pat.power}}</span>
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%">
                设备运行状态:
                <span>设备状态:{{pat.eqStatus}}设备运行状态:{{pat.runStatus}}</span>
              </div>
              <div class="col-test">
                (检修时检测情况)
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%">
                现场故障情况:
                <span>
                  {{pat.eqFaultCode}}
                </span>
              </div>
              <div class="col-test">
                (检修时检测情况)
              </div>
            </div>
            <div class="card-cols">
              现场照片：
               <div>
                 <div  v-for="(pic,index) in pat.iFileList" :key="index">
                   <img :src="pic.imgUrl" :title="pic.imgName">
                 </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card"  v-for="(tas,index) in taskNews" :key="index">
        <div class="card-col">
          <div class="test">任务执行状态：
            <span>{{ tas.taskStatus }}</span>
          </div>
          <div class="test">
            执行巡查班组：
            <span>{{tas.bzId}}</span>
          </div>
          <div class="test">
            执行巡查人：
            <span>{{ tas.dispatcher }}</span>
          </div>
        </div>
        <div class="card-col">
          <div class="test">
            任务完成时间：
            <span>{{tas.taskEndtime}}</span>
          </div>
          <div class="test">
            任务持续时长：
            <span>{{ tas.taskCxtime }}</span>
            <div class="chaoshi">超时</div>
          </div>
        </div>
        <div class="card-cols">
          <div class="test">
            任务描述：
            <span>{{tas.taskDescription}}</span>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="table-row">
          <div style="width:10%">操作记录</div>
          <div style="width:10%">派单</div>
          <div style="width:20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width:30%">2022/09/18 21:13:35</div>
          <div style="width:30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
        <div class="table-row">
          <div style="width:10%">操作记录</div>
          <div style="width:10%">派单</div>
          <div style="width:20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width:30%">2022/09/18 21:13:35</div>
          <div style="width:30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
        <div class="table-row">
          <div style="width:10%">操作记录</div>
          <div style="width:10%">派单</div>
          <div style="width:20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width:30%">2022/09/18 21:13:35</div>
          <div style="width:30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
        <div class="table-row">
          <div style="width:10%">操作记录</div>
          <div style="width:10%">派单</div>
          <div style="width:20%">九龙峪管理站 / 监控员 / 郑腾浩</div>
          <div style="width:30%">2022/09/18 21:13:35</div>
          <div style="width:30%">平台制定巡检任务时，派单人员和派单时间。</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listList,
  getList,
  delList,
  addList,
  updateList,
  exportList,
  getTaskInfoList,
  listBz, treeselect,
  getDevicesTypeList,
} from "@/api/electromechanicalPatrol/taskManage/task";
import {getRepairRecordList} from "@/api/electromechanicalPatrol/faultManage/fault";
import {listTunnels} from "@/api/equipment/tunnel/api";

export default {
  name: "List",
  //字典值：任务发布状态,任务状态
  dicts: ["publish_status","task_status","network","power"],
  props:{
    //开启过滤
    filter:{
      type:Boolean,
      default:true,
    },
    //节点是否可被选择
    show_checkbox:{
      type:Boolean,
      default:false,
    },
    //是否级联
    check_strictly:{
      type:Boolean,
      default:false,
    },
    //开启默认全选
    default_check_all:{
      type:Boolean,
      default:false,
    },
    //开启默认选中第一个子节点
    default_check_first:{
      type:Boolean,
      default:false,
    },
    //默认第一个子节点高亮选中
    default_select_first:{
      type:Boolean,
      default:false,
    },
    // powerCode:{
    //   type:String,
    //   default:''
    // },
  },
  data() {
    return {
      defaultProps: {
        value:'id',
        label:'label',
        children: 'children'
      },
      treeData:[],
      tableData1:[],
      options1value:'',//设备清单绑定
      boxList:[],
      options1:[],//设备清单
      record:false,
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
      taskNews:{
        id:"",
        zzjgId:"",
        bzId:"",
        endPlantime:"",
        dispatcher:"",
        dispatchTime:"",
        taskDescription:"",
      },
      //巡查点参数
      patrolNews:{
        tunnelName:"",
        xcTime:"",
        bzId:"",
        walkerId:"",
        impression:0,
        network:0,
        power:0,
        eqStatus:"",
        runStatus:"",
        eqFaultDescription:"",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      impressionOptions:[],//外观情况
      networkOptions:[],//网络情况
      powerOptions:[],//配电情况
    };
  },
  created() {
    this.getList();
    this.getBz();
    this.getTreeSelect();
    //外观情况
    this.getDicts("impression").then(response => {
      this.impressionOptions = response.data;
    })
    //网络情况
    this.getDicts("network").then(response => {
      this.networkOptions = response.data;
    });
    //外观情况
    this.getDicts("power").then(response => {
      this.powerOptions = response.data;
    });
  },
  methods: {
    //节点单击事件
    handleNodeClick(data) {
      console.log(data,"节点单击事件")
      const param ={
        tunnelId :data.id
      }
      getDevicesTypeList(param).then((res) =>{
        console.log(res,"获取设备类型下拉");
      })
    },
    // 筛选节点
    filterNode(value, data) {
      console.log(value, data,"筛选节点")
      if (!value) return true;
      return data.loopName.indexOf(value) !== -1;
    },

    handleRecordy(row) {

      this.record = true
      this.taskId = row.id;
      getTaskInfoList(this.taskId).then((response) => {
        this.taskNews = response.data.task;
        this.patrolNews =  response.data.patrol;
        this.impressionOptions.forEach((opt) =>{
          if(opt.dictValue=="0"){
              this.patrolNews.forEach((taskitem) =>{
              taskitem.impression = opt.dictLabel;
            })
          }
          if(opt.dictValue=="1"){
            this.patrolNews.forEach((taskitem) =>{
              taskitem.impression = opt.dictLabel;
            })
          }

        })

        this.networkOptions.forEach((opt) =>{
          if(opt.dictValue=="0"){
            this.patrolNews.forEach((taskitem) =>{
              taskitem.network = opt.dictLabel;
            })
          }
          if(opt.dictValue=="1"){
            this.patrolNews.forEach((taskitem) =>{
              taskitem.network = opt.dictLabel;
            })
          }

        })

        this.powerOptions.forEach((opt) =>{
          if(opt.dictValue=="0"){
            this.patrolNews.forEach((taskitem) =>{
              taskitem.power = opt.dictLabel;
            })
          }
          if(opt.dictValue=="1"){
            this.patrolNews.forEach((taskitem) =>{
              taskitem.power = opt.dictLabel;
            })
          }

        })

      });
    },
    /** 查询巡查任务列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
        this.total = response.total;
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
    getTreeSelect() {
      treeselect().then((response) => {
        this.treeData = response.data
        console.log(response.data,"隧道部门树")
      });
    },

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
        siteDescription: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
    show1() {
      this.isShow1 = true;
    },
    show2() {
      this.isShow2 = true;
    },

    determine1() {
      this.isShow1 = false;
    },
    determine2() {
      this.isShow2 = false;
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
      this.title = "新增巡查任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡查任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除巡查任务编号为"' + ids + '"的数据项？').then(function() {
        return delList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有巡查任务数据项？').then(() => {
        this.exportLoading = true;
        return exportList(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    release(){

    }
  }
};
</script>
<style>
.el-table tr{
  background-color: transparent;
}</style>
<style lang="scss" scoped>
.card{
  font-size: 15px;
  position: relative;
  width: 100%;
  padding: 20px;
  margin-top: 20px;
  border-radius: 10px;
  background-color: #f0f0f0;
  .card-col{
    margin-top: 10px;
    display: flex;
    color: #79949c;
    .chaoshi{
      padding: 5px;
      color: #FFD69A;
      display: inline;
      margin-left: 10px;
      border: 1px solid #FFD69A;
    }
    div{
      width: 33%;
      span{
        color: black;
        margin-left: 10px;
      }
    }
  }
  .card-cols{
    margin-top: 10px;
    display: flex;
    .col-test{
      text-align: right;
      color: #79949c;
    }
    img{
      width:100px;
      margin-left: 20px;
    }
  }
  .card-col1{
    font-size: 17px;
    .row{
      margin-top: 5px;
      display: flex;
      background-color: white;
      padding: 10px;
      // justify-content: center;
      align-items: center;
      .row-card1{
        padding: 5px;
        background-color:#f0f0f0;
        margin-left: 5px;
      }
      .row-card2{
        width: 29%;
      }
    }
  }
}
.col-1{
  font-size: 20px;
  display: flex;
  justify-content: right;
  align-items: center;
  text-align: right;
  .col-card{
    padding: 10px;
    margin-left: 5px;
    color: #3E9E70;
    background-color: rgba(230, 243, 235, 1);
  }
}
  .card-cols{
    margin-top: 10px;
    display: flex;
    .col-test{
      width: 20%;
      text-align: right;
      color: #79949c;
    }
    img{
      width:100px;
      margin-left: 20px;
    }
  }
  .table-row{
    padding: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    div{
      text-align: center;
      margin-left: 15px;
      border-bottom:1px solid black
    }
  }
  .test{
    color: #79949c;
    span{
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
      .number {
        width: 30px;
        height: 25px;
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
        width: 850px;
        height: 25px;
        line-height: 25px;
        padding-left: 10px;
        background: inherit;
        border: 1px solid rgba(215, 215, 215, 1);
        border-radius: 3px;
        font-weight: 400;
        font-size: 14px;
        margin-right: 20px;
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
      border: 1px solid black;
      border-radius: 3px;
      .show-title {
        font-weight: 400;
        font-style: normal;
        background-color: rgba(242, 242, 242, 1);
        color: #5e7f89;
        border-radius: 3px;
        line-height: 30px;
        border-bottom: 1px solid rgb(204, 204, 204);
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
      }
    }
  }
}
.tree{
  height: 445px;
  overflow: auto;
}
</style>
