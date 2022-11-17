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
      <el-table-column label="发布状态" align="center" prop="publishStatus" />
      <el-table-column label="任务状态" align="center" prop="taskStatus" />
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
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
    </el-dialog>
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
      <div class="card"  v-for="item in taskNews">
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
        <div class="card-col1">
          <div class="row">
            <div class="row-card1">1</div>
            <div class="row-card1">设备巡检点</div>
            <div class="row-card2" style="margin-left:10px">济潍高速济青线/杭山东隧道</div>
            <div class="row-card2">K120+200  车道指示器(名称车道指示器#2名称)</div>
            <div class="row-card2" style="text-align:right">2022/09/23 12:23:34</div>
          </div>
          <div style="background-color: white;padding: 10px;">
            <div class="test">设备描述：<span>巡检点设定时，巡检点设备或故障描述信息。巡检点设定时，巡检点设备或故障描述信息。</span></div>
            <div style="display:flex;margin-top: 10px;">
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div>
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div>
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%">
                设备运行状态:
                <span>设备状态:在线</span>
              </div>
              <div class="col-test">
                (抢修时检测情况)
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%">
                现场故障情况:
                <span>
                  故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码
                </span>
              </div>
              <div class="col-test">
                (抢修时检测情况)
              </div>
            </div>
            <div class="card-cols">
              现场照片：
               <div>
                <img src="https://image.cn.made-in-china.com/prod/698-23255915.jpg" >
                <img src="https://image.cn.made-in-china.com/prod/698-23255915.jpg" >
                <img src="https://image.cn.made-in-china.com/prod/698-23255915.jpg" >
              </div>
            </div>
          </div>
        </div>
        <div class="card-col1">
          <div class="row">
            <div class="row-card1">2</div>
            <div class="row-card1">设备巡检点</div>
            <div class="row-card2" style="margin-left:10px">济潍高速济青线/杭山东隧道</div>
            <div class="row-card2">K120+200  车道指示器(名称车道指示器#2名称)</div>
            <div class="row-card2" style="text-align:right">2022/09/23 12:23:34</div>
          </div>
          <div style="background-color: white;padding: 10px;">
            <div class="test">设备描述：<span>巡检点设定时，巡检点设备或故障描述信息。巡检点设定时，巡检点设备或故障描述信息。</span></div>
            <div style="display:flex;margin-top: 10px;">
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div >
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div>
              <div class="test" style="width:30%">外观情况：
                <span>外观正常</span>
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%"  class="test">
                设备运行状态:
                <span>设备状态:在线</span>
              </div>
              <div class="col-test">
                (抢修时检测情况)
              </div>
            </div>
            <div class="card-cols">
              <div style="width:80%"  class="test">
                现场故障情况:
                <span>
                  故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码、故障描述故障代码
                </span>
              </div>
              <div class="col-test">
                (抢修时检测情况)
              </div>
            </div>
            <div class="card-cols" >
              现场照片：
              <div style="border: 1px solid #f0f0f0;padding:20px">
              未上传图片
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="card-col">
          <div class="test">任务执行状态：
            <span>已完结</span>
          </div>
          <div class="test">
            执行巡查班组：
            <span>济青中线养护二站</span>
          </div>
          <div class="test">
            执行巡查人：
            <span>郑费腾</span>
          </div>
        </div>
        <div class="card-col">
          <div class="test">
            任务完成时间：
            <span>2022/09/25</span>
          </div>
          <div class="test">
            任务持续时长：
            <span>1天23小时42分</span>
            <div class="chaoshi">超时</div>
          </div>
        </div>
        <div class="card-cols">
          <div class="test">
            任务描述：
            <span>请按照机电设备养护技术规范执行巡检任务，并按时回传巡检记录。如遇巡检设备问题，优先现场记录并处理，无法处理的机电故障需要及时填报故障上报单。巡检点需要如实签到，并记录巡检点现场情况。</span>
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
import { listList, getList, delList, addList, updateList, exportList,getTaskInfoList } from "@/api/electromechanicalPatrol/taskManage/task";
import {getRepairRecordList} from "@/api/electromechanicalPatrol/faultManage/fault";

export default {
  name: "List",
  data() {
    return {
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleRecordy(row) {
      console.log(row);
      this.record = true
      this.taskId = row.id;
      getTaskInfoList(this.taskId).then((response) => {
        debugger
        this.taskNews = response.data.task;
        this.patrolNews =  response.data.patrol;
        console.log(this.taskNews);
console.log(this.patrolNews);
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
      this.title = "添加巡查任务";
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
</style>
