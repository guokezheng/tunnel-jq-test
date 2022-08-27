<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="通道ID" prop="channelId">
        <el-input
          v-model="queryParams.channelId"
          placeholder="请输入通道ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="视频流类型" prop="taskType">
        <el-select v-model="queryParams.taskType" placeholder="请选择视频流类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item> -->
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
          v-hasPermi="['system:eventtask:add']"
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
          v-hasPermi="['system:eventtask:edit']"
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
          v-hasPermi="['system:eventtask:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:eventtask:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="eventtaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="55"/>
      <el-table-column label="容器" align="center" prop="dockerName" />
      <el-table-column label="通道" align="center" prop="channelId" />
      <el-table-column label="视频流类型" align="center" prop="taskType" />
      <el-table-column label="视频流地址" align="center" prop="url" />
      <el-table-column label="结果地址" align="center" prop="results" />
      <el-table-column label="道路类型" align="center" prop="sceneMode" />
      <el-table-column label="上报间隔(秒)" align="center" prop="reportingInterval" />
      <el-table-column label="夜间模式" align="center" prop="nightMode" width="200">
         <template slot-scope="scope">
           <span v-if="scope.row.nightMode==1">
              关闭分析：{{scope.row.nightStart}}点 ~ {{scope.row.nightEnd}}点
           </span>
           <span v-else-if="scope.row.nightMode==0">
              开启分析
           </span>
         </template>
      </el-table-column>
      <el-table-column label="告警视频" align="center" prop="alarmVideo">
        <template slot-scope="scope">
           <span v-if="scope.row.alarmVideo==0">
              开启
           </span>
           <span v-else-if="scope.row.alarmVideo==1">
              关闭
           </span>
         </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120" >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:eventtask:edit']"
          >修改</el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-setting"
            @click="disposeEvent(scope.row)"
          >配置事件</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-caret-right"
            @click="startEventTask(scope.row)"
          >启动</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="stopEventTask(scope.row)"
          >暂停</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="searchEventTask(scope.row)"
          >状态查询</el-button>
         <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:eventtask:remove']"
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

    <!-- 添加或修改事件任务表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="容器" prop="dockerId">
         <el-select v-model="form.dockerId" placeholder="请选择容器" clearable size="small">
           <el-option v-for="item in dockersData" :key="item.id" :label="item.dockerName"
             :value="item.id" />
         </el-select>
        </el-form-item>
        <el-form-item label="通道ID" prop="channelId">
          <el-input v-model="form.channelId" placeholder="请输入通道ID" />
        </el-form-item>
        <el-form-item label="视频流类型" prop="taskType">
          <!-- <el-select v-model="form.taskType" placeholder="请选择视频流类型">
            <el-option label="请选择字典生成" value="" />
          </el-select> -->
           <el-input v-model="form.taskType" placeholder="请输入视频流类型:realVideoStream" />
        </el-form-item>
        <el-form-item label="视频流地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入视频流地址" />
        </el-form-item>
        <el-form-item label="结果地址端口" prop="results">
          <el-input v-model="form.results" placeholder="请输入结果地址端口" />
        </el-form-item>
        <el-form-item label="道路类型" prop="sceneMode">
          <el-input v-model="form.sceneMode" placeholder="请输入道路类型" />
        </el-form-item>
        <el-form-item label="上报间隔" prop="reportingInterval">
          <el-input v-model="form.reportingInterval" placeholder="请输入上报间隔" />
        </el-form-item>
        <el-form-item label="夜间模式" prop="nightMode">
          <el-radio v-model="form.nightMode" label="0">开启分析</el-radio>
          <el-radio v-model="form.nightMode" label="1">关闭分析</el-radio>
        </el-form-item>
        <el-form-item v-if="form.nightMode==1" label="开始点" prop="nightStart">
          <el-input v-model="form.nightStart" placeholder="开始点(24H格式)" />
        </el-form-item>
        <el-form-item v-if="form.nightMode==1" label="结束点" prop="nightEnd">
          <el-input v-model="form.nightEnd" placeholder="结束点(24H格式)" />
        </el-form-item>
        <el-form-item label="告警视频" prop="alarmVideo">
          <el-radio v-model="form.alarmVideo" label="0">开启</el-radio>
          <el-radio v-model="form.alarmVideo" label="1">关闭</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 配置视频坐标，车道信息，事件信息 -->
    <el-dialog :title="eventTitle" :visible.sync="eventopen" width="75%" append-to-body :close-on-click-modal="false">
        <div style="height: 600px;">
            <div class="leftevent">
              <div class="voideDiv" style="z-index:200;">
               <!-- <videoPlayer open="true"  ></videoPlayer> -->
                     <!--class="video-js vjs-default-skin vjs-big-play-centered" -->
                   <!--  <video  :preload="preload" :poster="videoImg" width="960" height="540" :controls="controls"  :autoplay="autoplay">
                       <source :src="videoSrc" type="video/mp4">
                     </video> -->
                      <videoPlayer :id="id" :rtsp="rtsp" :open="eventopen"></videoPlayer>
              </div>
              <div class="voideDiv" style="z-index:300;">
                <svg @mousedown="canvasDown($event,polyline)"  viewBox="0 0 960 540"  width="960" height="540" >
                                   <!--  @mouseup="canvasUp($event)" @mousemove="canvasMove($event)" -->
                               <!--车道坐标-->
                   <polyline  id="coordinates" :points="coordinates" style="fill:none;stroke:blue;stroke-width:3"></polyline>
                   <!--车流方向坐标-->
                   <defs>
                     <marker id="direction" viewBox="0 0 20 20" refX="0" refY="3"
                         markerWidth="10" markerHeight="10" orient="auto">
                       <path d="M 0 0 L 6 3 L 0 6 z" style='fill:green' />
                     </marker>
                   </defs>
                   <polyline  id="direction" :points="direction" stroke-linecap="round" style="fill:none;stroke:green;stroke-width:5" stroke-width="2" marker-end="url(#direction)"></polyline>
                    <!--车道采集区域-->
                   <polyline  id="virtualLoop" :points="virtualLoop" stroke-dasharray="20,10" style="fill:none;stroke:orange;stroke-width:3"></polyline>

                   <polyline  id="coordinates" :points="coordinatesE" style="fill:none;stroke:#0066CC;stroke-width:3"></polyline>
                   <!--车流方向坐标-->
                   <defs>
                     <marker id="directionE" viewBox="0 0 20 20" refX="0" refY="3"
                         markerWidth="10" markerHeight="10" orient="auto">
                       <path d="M 0 0 L 6 3 L 0 6 z" style='fill:#13CE66' />
                     </marker>
                   </defs>
                   <polyline  id="direction" :points="directionE" style="fill:white;stroke:#13CE66;stroke-width:5" stroke-width="2" marker-end="url(#directionE)"></polyline>
                    <!--车道采集区域-->
                   <polyline  id="virtualLoop" :points="virtualLoopE" stroke-dasharray="20,10" style="fill:none;stroke:#FEC171;stroke-width:3"></polyline>
                    <!--交通事件采集区域-->
                   <polyline  id="arCoordinates" :points="arCoordinates" style="fill:none;stroke:crimson;stroke-width:3"></polyline>
                </svg>
              </div>
            </div>
            <div class="rightevent">
              <el-form ref="eventform" :model="eventform" :rules="eventrules" style="height: 100px;">
                 <el-row>
                                <el-col span="12">
                <el-form-item label="主干道路" prop="trunkRoad" style="margin-top:-30px">
                   <el-button  type="primary" size="mini" icon="el-icon-edit" @click="drawPolyline('coordinates')" >绘制车道</el-button>
                   <el-button  type="success" size="mini" icon="el-icon-edit" @click="drawPolyline('direction')" >方向</el-button>
                   <el-button  type="warning" size="mini" icon="el-icon-edit" @click="drawPolyline('virtualLoop')" >采集区域</el-button>
                   <el-button  type="infor" size="mini" icon="el-icon-close" @click="clearPolyline('trunk')" >清空</el-button>
                 </el-form-item>
                 </el-col>
                  <el-col span="12">
                <el-form-item label="应急车道" prop="emergencyRoad" style="margin-top:-30px">
                   <el-button  type="primary" size="mini" icon="el-icon-edit" @click="drawPolyline('coordinatesE')" >绘制车道</el-button>
                   <el-button  type="success" size="mini" icon="el-icon-edit" @click="drawPolyline('directionE')" >方向</el-button>
                   <el-button  type="warning" size="mini" icon="el-icon-edit" @click="drawPolyline('virtualLoopE')" >采集区域</el-button>
                   <el-button  type="infor" size="mini" icon="el-icon-close" @click="clearPolyline('emergency')" >清空</el-button>
                 </el-form-item>
                  </el-col>
                   <el-col span="12">
                 <el-form-item label="事件区域" prop="event" style="margin-top:-20px">
                   <el-button  type="danger" size="mini" icon="el-icon-edit" @click="drawPolyline('arCoordinates')" >采集区域</el-button>
                   <el-button  type="infor" size="mini" icon="el-icon-close" @click="clearPolyline('event')" >清空</el-button>
                 </el-form-item>
                 </el-col>
                  <el-col span="12">
                  <el-form-item label="事件" prop="selectedArray" style="margin-top:-20px">
                    <el-select multiple  v-model='eventform.selectedArray' placeholder='请选择事件类型' style="width: 82%;">
                            <el-option v-for='(item, index) in eventoptions' :key='item.dictValue' :label='item.dictLabel' :value='item.dictValue'></el-option>
                    </el-select>
                  </el-form-item>
                  </el-col>
                   </el-row>
                  <el-input v-model="eventform.coordinates" type="hidden" />
                  <el-input v-model="eventform.direction" type="hidden" />
                  <el-input v-model="eventform.virtualLoop" type="hidden" />
                  <el-input v-model="eventform.coordinatesE" type="hidden" />
                  <el-input v-model="eventform.directionE" type="hidden" />
                  <el-input v-model="eventform.virtualLoopE" type="hidden" />
                  <el-input v-model="eventform.arCoordinates" type="hidden" />
                  <el-input v-model="eventform.taskId" type="hidden" />
                  <el-input v-model="eventform.id" type="hidden" />

              </el-form>
            </div>
        </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEventForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDocker } from "@/api/event/docker";
import { listEventtask, getEventtask, delEventtask, addEventtask, updateEventtask, exportEventtask, addTaskEvent,startEventTask,searchEventTask ,stopEventTask} from "@/api/event/eventtask";
import { getEventlaneByTaskId } from "@/api/event/eventlane";
import { getEvId } from "@/api/event/eventanalysis";
 import videoPlayer from "@/views/event/eventtask/myVideo";
/*
import Video from 'video.js'
import 'video.js/dist/video-js.css' */
export default {
  name: "Eventtask",
  components: {
    videoPlayer
  },
  data() {
    return {
      // 绘制车道
      coordinates: "",
      //车道检测区域
      virtualLoop: "",
      //绘制方向
      direction: "",
      coordinatesE: "",
      virtualLoopE: "",
      directionE: "",
      //事件采集区域
      arCoordinates: "",
      id: null,
      rtsp: "",
      // 遮罩层
      loading: true,
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
      // 事件任务表表格数据
      eventtaskList: [],
      dockersData: {},
      coordinatesList: [],
      directionList: [],
      virtualLoopList: [],

      coordinatesEList: [],
      directionEList: [],
      virtualLoopEList: [],
      // 弹出层标题
      title: "",
      eventTitle: "",
      polyline: "",
      // 是否显示弹出层
      open: false,
      eventopen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        channelId: null,
        taskType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
         dockerId:[{required: true, message: '请选择容器', trigger: 'blur'}],
         channelId:[{required: true, message: '请输入通道ID', trigger: 'blur'}],
         taskType:[{required: true, message: '请输入视频流类型', trigger: 'blur'}],
         url:[{required: true, message: '请输入视频流地址', trigger: 'blur'}],
         results:[{required: true, message: '请输入结果地址端口', trigger: 'blur'}],
         sceneMode:[{required: true, message: '请输入道路类型', trigger: 'blur'}],
         reportingInterval:[{required: true, message: '请输入上报间隔时间', trigger: 'blur'}],
      },
   // 表单参数
      eventform: {},
      // 表单校验
      eventrules: {
        selectedArray:[{required: true, message: '请选择事件类型', trigger: 'blur'}],
      },
      selectedArray: [],
      eventoptions: []
    };
  },
  created() {
    this.getList();
    this.getDocker();
    this.getDicts("sys_traffic_event").then(response => {
      this.eventoptions = response.data;
    });
  },
  methods: {
    /** 查询事件任务表列表 */
    getList() {
      this.loading = true;
      listEventtask(this.queryParams).then(response => {
        this.eventtaskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.eventopen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        channelId: null,
        taskType: "realVideoStream",
        url: null,
        results: "8080",
        sceneMode: "MainRoad",
        reportingInterval: 20,
        nightMode: "0",
        alarmVideo: "1",
        nightStart: 16,
        nightEnd: 7,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTtime: null
      };
      this.resetForm("form");

      this.coordinates="";
      this.direction="";
      this.virtualLoop="";
      this.coordinatesE="";
      this.directionE="";
      this.virtualLoopE="";
      this.arCoordinates="";
      this.eventform = {
        taskId: null,
        selectedArray: [],
        coordinates: null,
        direction: null,
        virtualLoop: null,
        arCoordinates: null
      };
      this.resetForm("eventform");
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
      this.title = "添加事件任务表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEventtask(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改事件任务表";
      });
    },
    /** 提交按钮 */
    submitEventForm() {
      this.$refs["eventform"].validate(valid => {
        if (valid) {
          addTaskEvent(this.eventform).then(response => {
            this.$modal.msgSuccess("配置成功");
            this.eventopen = false;
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEventtask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEventtask(this.form).then(response => {
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
      this.$confirm('是否确认删除选中数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEventtask(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有事件任务表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEventtask(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /** 所属容器 */
    getDocker() {
      listDocker().then(response => {
        this.dockersData = response.rows;
      });
    },
    /** 配置事件按钮操作 */
    disposeEvent(row) {
      this.reset();
      const id = row.id || this.ids
      getEventtask(id).then(response => {
        this.eventopen = true;
        this.eventTitle = "配置事件";
        this.id = row.id;
        this.rtsp = row.url;
        this.eventform.taskId=id;
        getEventlaneByTaskId(id).then(responseLane => {
             const da=responseLane.data;
           if(da.length>0){
             da.forEach((item,index)=>{//选中数据
                if(item.laneType==="BusLane"){
                   this.eventform.virtualLoop= item.virtualLoop;
                   this.eventform.coordinates= item.coordinates;
                   this.eventform.direction= item.start +" "+item.end;
                   this.virtualLoop= item.virtualLoop;
                   this.coordinates= item.coordinates;
                   this.direction= item.start +" "+item.end;
                }else if(item.laneType==="EmergencyLane"){
                   this.eventform.virtualLoopE= item.virtualLoop;
                   this.eventform.coordinatesE= item.coordinates;
                   this.eventform.directionE= item.start +" "+item.end;
                   this.virtualLoopE= item.virtualLoop;
                   this.coordinatesE= item.coordinates;
                   this.directionE= item.start +" "+item.end;
                }
             })

           }

        });
        getEvId(id).then(responseA => {
          const daA=responseA.data;
          if(daA.length>0){
            this.eventform.arCoordinates= daA[0].coordinates;
            this.arCoordinates= daA[0].coordinates;
            let peoData=daA[0].incident.split(",");
            peoData.forEach((item,index)=>{//选中数据
              this.eventoptions.forEach((listItem,listIndex)=>{ //配件列表所有数据
                    if(item === listItem.dictValue){
                      console.log(listItem.dictValue)
                      this.eventform.selectedArray.push(listItem.dictValue) ;
                    }
              })
            })
          }
        });
      });
    },
    // 鼠标按下
     canvasDown (e,polyline) {
       console.log(polyline)
       if(polyline=="coordinates"){
           let ev= this.coordinates;
           let list= this.coordinatesList;
           list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
           if(list.length==6){
             this.coordinates=ev+list[0];
             this.eventform.coordinates=ev+list[0];
           }else if(list.length>6){
             this.coordinates="";
             this.eventform.coordinates="";
           }else{
             this.coordinates=ev+" "+e.layerX+","+e.layerY+" ";
             <rect x="20" y="20" width="250" height="250" style="fill:blue;stroke:pink;stroke-width:5; fill-opacity:0.1;stroke-opacity:0.9"/>
           }
       }else if(polyline=="direction"){
         let ev= this.direction;
         let list= this.directionList;
        list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
         if(list.length==1){
           this.direction=ev+" "+e.layerX+","+e.layerY+" ";
           this.eventform.direction=this.direction;
         }else if(list.length>1){
           this.direction="";
           this.eventform.direction="";
         }else{
           this.direction=ev+" "+e.layerX+","+e.layerY+" ";
         }
       }else if(polyline=="virtualLoop"){
         let ev= this.virtualLoop;
         let list= this.virtualLoopList;
         list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
         if(list.length==5){
           this.virtualLoop=ev+list[0];
           this.eventform.virtualLoop=ev+list[0];
         }else if(list.length>5){
           this.virtualLoop="";
           this.eventform.virtualLoop="";
         }else{
           this.virtualLoop=ev+" "+e.layerX+","+e.layerY+" ";
         }
       }else if(polyline=="coordinatesE"){
           let ev= this.coordinatesE;
           let list= this.coordinatesEList;
           list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
           if(list.length==6){
             this.coordinatesE=ev+list[0];
             this.eventform.coordinatesE=ev+list[0];
           }else if(list.length>6){
             this.coordinatesE="";
             this.eventform.coordinatesE="";
           }else{
             this.coordinatesE=ev+" "+e.layerX+","+e.layerY+" ";
           }
       }else if(polyline=="directionE"){
         let ev= this.directionE;
         let list= this.directionEList;
         list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
         if(list.length==1){
           this.directionE=ev+" "+e.layerX+","+e.layerY+" ";
           this.eventform.directionE=this.directionE;
         }else if(list.length>1){
           this.directionE="";
           this.eventform.directionE="";
         }else{
           this.directionE=ev+" "+e.layerX+","+e.layerY+" ";
         }
       }else if(polyline=="virtualLoopE"){
         let ev= this.virtualLoopE;
         let list= this.virtualLoopEList;
         list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
         if(list.length==5){
           this.virtualLoopE=ev+list[0];
           this.eventform.virtualLoopE=ev+list[0];
         }else if(list.length>5){
           this.virtualLoopE="";
           this.eventform.virtualLoopE="";
         }else{
           this.virtualLoopE=ev+" "+e.layerX+","+e.layerY+" ";
         }
       }else if(polyline=="arCoordinates"){
         let ev= this.arCoordinates;
         let list= this.arCoordinatesList;
         list = ev.split(" ").map(el => el.trim()).filter(item => item.trim() != '');
         if(list.length==6){
           this.arCoordinates=ev+list[0];
           this.eventform.arCoordinates=ev+list[0];
         }else if(list.length>6){
           this.arCoordinates="";
           this.eventform.arCoordinates="";
         }else{
           this.arCoordinates=ev+" "+e.layerX+","+e.layerY+" ";
         }
       }
     },
     // 鼠标移动时绘制
     canvasMove (e) {
    // this.aa=this.aa+" "+e.layerX+","+e.layerY+" ";
     },
     // 鼠标抬起
     canvasUp (e) {
     },
     drawPolyline(e){
       this.polyline=e;
     },
     clearPolyline(e){
      if(e==="trunk"){
        this.coordinates="";
        this.direction="";
        this.virtualLoop="";
        this.eventform.coordinates="";
        this.eventform.direction="";
        this.eventform.virtualLoop="";
      }else if(e==="event"){
        this.arCoordinates="";
        this.eventform.arCoordinates="";
      }else{
        this.coordinatesE="";
        this.directionE="";
        this.virtualLoopE="";
        this.eventform.coordinatesE="";
        this.eventform.directionE="";
        this.eventform.virtualLoopE="";
      }
     },
     startEventTask(row){
      this.$confirm('是否启动通道为"' + row.channelId + '"的任务?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return startEventTask(row.id);
        }).then(responseA => {
          this.$modal.msgSuccess(responseA.msg);
        })
     },
     searchEventTask(row){
        searchEventTask(row.id).then(response => {
          this.$modal.msgSuccess(response.msg+" 任务状态："+response.data);
        });
     },
     stopEventTask(row){
        stopEventTask(row.id).then(response => {
          this.$modal.msgSuccess(response.msg);
        });
     },
    }
};
</script>
<style>
    /* 左侧样式 */
    .leftevent {
      width: 960px;
      height: 540px;
      bottom: 0px;
      float: left;
    }
    .leftevent .voideDiv{
       position:absolute;left:20px;right:0;top:50px;bottom:0;width: 960px;height: 540px;
    }
    /* 右侧区域 */
    .rightevent {
      float: left;
      margin-left: 5px;
      bottom: 0px;
      right: 0px;  /* 距离右边0像素 */
      width: 960px;
      /* background-color: red; */
    }
</style>
