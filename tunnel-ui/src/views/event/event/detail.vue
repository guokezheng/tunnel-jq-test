<template>
  <div class="app-container">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="事件详情" name="first">
                  <div  style="height: 65vh;">
                        <el-table  v-loading="loading"  :data="list_detail">
                          <el-table-column label="事件类型" align="center" prop="eventType.eventType" />
                          <el-table-column label="事件标题" align="center" prop="eventTitle" />
                          <el-table-column label="时间" align="center" prop="eventTime" width="180">
                            <template slot-scope="scope">
                              <span>{{ parseTime(scope.row.eventTime, '{y}-{m}-{d} {h}:{m}') }}</span>
                            </template>
                          </el-table-column>
                          <el-table-column label="状态" align="center" prop="eventState" :formatter="eventStateFormat" />
                          <el-table-column label="级别 " align="center" prop="eventGrade" :formatter="eventGradeFormat" />
                          <el-table-column label="位置" align="center" prop="eventLocation" />
                          <el-table-column prop="specs,quantityUnit" label="伤亡">
                                <template slot-scope="scope"> 
                                      死亡：{{scope.row.eventDeath}}<br />
                                      受伤：{{scope.row.eventInjured}} 
                                 </template>
                           </el-table-column>
                          <el-table-column label="事件描述" align="center" prop="eventDescription" />
                        </el-table>
                        <pagination
                          v-show="total_detail>0"
                          :total="total_detail"
                          :page.sync="queryParams_detail.pageNum"
                          :limit.sync="queryParams_detail.pageSize"
                          @pagination="getDetailList"
                        />
                  </div>


        </el-tab-pane>
        <el-tab-pane label="相关预案" name="second">
                    <div  style="height: 75vh;overflow: auto;">
                        <el-table v-loading="loading" :data="list_plan" >
                          <el-table-column label="预案类型" align="center" prop="eventType.eventType" />
                          <el-table-column label="预案描述" align="center" prop="planDescription" />
                          <el-table-column label="预案名称" align="center" prop="planName" />
                          <el-table-column label="相关文档" align="center" class-name="small-padding fixed-width">
                             <template slot-scope="scope">
                              <el-button  v-show="scope.row.planFileId !=null"
                                size="mini"
                                type="text"
                                icon="el-icon-link"
                                @click="openFileDrawer(scope.row)"
                                style="cursor:pointer;"
                              >点击查看</el-button>
                              <div  v-show="scope.row.planFileId == null">
                                无
                              </div>
                            </template>
                          </el-table-column>
                          <el-table-column label="相关策略" align="center"  class-name="small-padding fixed-width">
                            <!-- <template slot-scope="scope">
                               <span v-show="scope.row.strategyId != null">
                                {{scope.row.strategy.strategyInfo}}
                               </span>
                              <div  v-show="scope.row.strategyId == null">
                                无
                              </div>
                            </template> -->
                            <template slot-scope="scope">
                               <span v-show="scope.row.strategyNames != null">
                                    {{scope.row.strategyNames}}
                               </span>
                              <div  v-show="scope.row.strategyNames == null">
                                无
                              </div>
                            </template>
                          </el-table-column>
                          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                            <template slot-scope="scope">
                              <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-guide"
                                @click="chooseStrategyInfo(scope.row)"
                                v-hasPermi="['business:plan:remove']"
                              >执行策略</el-button>
                            </template>
                          </el-table-column>
                        </el-table>
                        <pagination
                          v-show="total_plan>0"
                          :total="total_plan"
                          :page.sync="queryParams_plan.pageNum"
                          :limit.sync="queryParams_plan.pageSize"
                          @pagination="getPlanList"
                        />
                      </div>
        </el-tab-pane>


        <el-tab-pane label="处理记录" name="third">

          <div class="block" style="overflow: auto; height: 80vh;">
             <!-- <div class="radio">
              排序：
              <el-radio-group v-model="reverse">
                <el-radio :label="true">倒序</el-radio>
                <el-radio :label="false">正序</el-radio>
              </el-radio-group>
            </div> -->
            <el-timeline :reverse="reverse">
              <!-- <el-timeline-item v-for="(activity, index) in activities" :key="index" :timestamp="activity.timestamp">
                {{activity.content}}
                <div>12312</div>
              </el-timeline-item> -->
              <el-timeline-item  placement="top">
                <el-button type="primary" @click="showAddRecord">添加处理记录</el-button>
                <div style="margin-top: 1vh;" v-show= "addRecord">
                  <el-input style="width: 30%;"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入内容"
                    :autosize="{ minRows: 5, maxRows: 8}"
                    v-model="textarea">
                  </el-input>
                  <el-button @click="saveEventRecord" type="primary" style="margin-left: 1vw;">保存</el-button>
                  <el-button @click="oncancell" type="">取消</el-button>
                </div>
              </el-timeline-item>

              <el-timeline-item v-for="(activity, index) in activities" :key="index" :timestamp="activity.flowTime" placement="top">
                <span>处理人：{{activity.nickName}}</span>
                <span style="margin-left:2vw ;">{{activity.flowDescription}}</span>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-tab-pane>
      </el-tabs>


      <!-- 文件下载-->
      <el-drawer
        class="zwsj"
        :title="drawerFileTitle"
        :visible.sync="drawerFile"
        :direction="direction"
        :before-close="handleFileClose">
         <el-table v-loading="loading" :data="planFileList">
           <el-table-column label="序号" width="100px" align="center">
                  <template slot-scope="scope">
                      {{scope.$index+1}}
                  </template>
            </el-table-column>
           <el-table-column label="文件名称" align="center" prop="fileName" />
           <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
             <template slot-scope="scope">
               <el-button
                 size="mini"
                 type="text"
                 icon="el-icon-edit"
                 @click="loadFile(scope.row)"
                 v-hasPermi="['business:plan:edit']"
               >下载</el-button>
             </template>
           </el-table-column>
         </el-table>
      </el-drawer>

  <!-- 执行策略选择窗口-->
  <el-dialog  :title="title" :visible.sync="handleStrategyVisible" width="60%" append-to-body>
   <div style="width: 100%;height: 31.25rem; overflow: auto;">
    <el-table ref="multipleTable" :data="handleStrategyList"   @selection-change="handleStrategySelectionChange"
      empty-text="暂无策略" row-key="id">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="隧道名称" align="center" prop="tunnels.tunnelName" width="200" />
        <el-table-column label="策略名称" align="center" prop="strategyName" width="200"/>
       <el-table-column label="策略信息" align="center"  prop="slist" >
         <template slot-scope="scope">
             <div  v-for="(item,index) in scope.row.slist" :key="index" >
               <span style="color: #005CBF;">
                 {{item}}
               </span>
             </div>
         </template>
       </el-table-column>
      </el-table>
    </div>
    <div slot="footer">
      <el-button type="primary" @click="doStrategy">确定执行</el-button>
      <el-button  @click="doStrategyCancel"> 关  闭 </el-button>
    </div>
  </el-dialog>

  </div>
</template>

<script>
import { listEvent, getEvent, delEvent, addEvent, updateEvent } from "@/api/event/event";
import { listPlan,listStrategyByPlanId } from "@/api/event/reservePlan";
import { listReservePlanFile } from "@/api/event/reservePlanFile";
import { download } from "@/utils/request";
import { listEventFlow, getEventFlow, addEventFlow, updateEventFlow, delEventFlow } from "@/api/event/eventFlow";
import { handleStrategy } from "@/api/event/strategy";
export default {
  name: "Event",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 弹出层标题
      title: "",
      // 状态字典
      eventStateOptions: [],
      // 级别 字典
      eventGradeOptions: [],
      // 表单参数
      eventForm: {
        id:null,
        eventTypeId:null,
        eventTitle:null,
        eventTime:null,
        eventState:null,
        eventGrade:null,
        eventLocation:null,
        eventDeath:null,
        eventInjured:null,
        eventDescription:null,
        flowId:null,
      },
      // 表单校验
      rules: {
      },
      activeName: 'first',
      // 事件详情表格数据
      list_detail: [],
      //总条数
      total_detail:0,
      // 查询参数
      queryParams_detail: {
        pageNum: 1,
        pageSize: 10,
        id:null,
      },
      //预案表格数据
      list_plan:[],
      //总条数
      total_plan:0,
      // 查询参数
      queryParams_plan: {
        pageNum: 1,
        pageSize: 10,
        planTypeId:null
      },
      eventId : null,//当前全局事件ID

      //相关文件drawer标题
      drawerFileTitle: "",
      //相关文件draw开关
      drawerFile:false,
      // 预案文件信息表格数据
      planFileList: [],
      // 遮罩层
      fileLoading: true,
      direction: 'rtl',

      reverse: false,//正序
      activities: [],
      addRecord: true,
      textarea:'',

      // 选择--需要执行的控制策略
      handleStrategyVisible:false,
      handleStrategyList:[],
      title:'',
      handleIds:[],
    };
  },
  created() {
    const eventId = this.$route.params && this.$route.params.id;
    this.eventId = eventId;
    this.getDicts("sd_event_state").then(response => {
      this.eventStateOptions = response.data;
    });
    this.getDicts("sd_event_grade").then(response => {
      this.eventGradeOptions = response.data;
    });
    this.getDetailList();
  },
  methods: {

    //=========================执行相关策略开始=====================
        // 选择将要执行的策略
        chooseStrategyInfo(row){
          this.handleStrategyVisible = true;
          this.title = "执行相关策略";
          listStrategyByPlanId(row.id).then(response => {
            this.handleStrategyList = response.data;
          });
        },
        //执行策略id勾选事件
        handleStrategySelectionChange(selection){
          this.handleIds = selection.map(item => item.id)
        },
        // 关闭 相关策略对话框
        doStrategyCancel(){
          this.handleStrategyVisible = false;
        },
        // 执行 选择的策略
        doStrategy(){
          if(this.handleIds.length>0){
            this.$modal.msgSuccess("执行策略中.......");
            for(let i=0;i < this.handleIds.length;i++){
              handleStrategy(handleIds[i]);
            }
          }else{
            this.$modal.msgError("请先选择需要执行的策略！");
          }
        },
    //=========================执行相关策略结束=====================


    //新增记录-保存操作
    saveEventRecord() {
      this.loading = true;
      addEventFlow({flowDescription:this.textarea,eventId:this.eventForm.flowId}).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("新增成功");
            this.getEventFlow();
            this.oncancell();
            this.loading = false;
           // this.open = false;
           // this.getList();
          }
      });
    },
    //新增记录-取消操作
    oncancell() {
      this.addRecord = false;
      this.textarea = "";
    },
    showAddRecord() {
      this.addRecord = true;
    },

    handleClick(tab, event) {
      console.log(tab, event);
      if(tab.name == "second"){
        this.getPlanList();
      }else if(tab.name == "third"){
        this.getEventFlow();
      }
    },
    /*** 事件详情列表 **/
    getDetailList() {
      this.loading = true;
      this.queryParams_detail.id = this.eventId;
      listEvent(this.queryParams_detail).then(response => {
        this.list_detail = response.rows;
        this.total_detail = response.total;
        this.eventForm = response.rows[0];
        this.loading = false;
      });
    },
    //相关预案列表
    getPlanList() {
      if(this.eventForm.eventTypeId != null){
        this.loading = true;
        this.queryParams_plan.planTypeId = this.eventForm.eventTypeId;
        listPlan(this.queryParams_plan).then(response => {
          this.list_plan = response.rows;
          this.total_plan = response.total;
          this.loading = false;
        });
      }else{
        this.list_plan = [];
        this.total_plan = 0;
      }

    },
    //查看事件流程
    getEventFlow() {
      listEventFlow({eventId : this.eventForm.flowId}).then(response => {
        this.activities = response.rows;
      });
    },
    // 状态字典翻译
    eventStateFormat(row, column) {
      return this.selectDictLabel(this.eventStateOptions, row.eventState);
    },
    // 级别 字典翻译
    eventGradeFormat(row, column) {
      return this.selectDictLabel(this.eventGradeOptions, row.eventGrade);
    },
    //下载文件
    loadFile(row) {
      download("system/plan/"+row.id,{},row.fileName);
    },
    //点击查看文件
    openFileDrawer(row) {
      this.fileLoading = true;
      listReservePlanFile({planFileId : row.planFileId}).then(response => {
        this.drawerFileTitle = "相关文档";
        this.planFileList = response.rows;
        this.fileLoading = false;
        this.drawerFile = true;
      });
    },
    //关闭drawer
    handleFileClose(done) {
         done();
     },
  }
};
</script>
