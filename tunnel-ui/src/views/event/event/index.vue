<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="事件类型" prop="eventTypeId">
        <el-select v-model="queryParams.eventTypeId" placeholder="请选择事件类型" clearable size="small" style="width:180px">
          <el-option
                v-for="item in eventTypeData"
                :key="item.id"
                :label="item.eventType"
                :value="item.id"/>
          </el-select>
      </el-form-item>
      <el-form-item label="管理机构"  prop="stagPointName">
          <el-input style="width:180px;" v-model.number="queryParams.stagPointName"
                    placeholder="请输入机构名称" size="small"/>
      </el-form-item>
      <el-form-item label="路段名称" prop="eventTypeId">
        <el-select v-model="queryParams.eventTypeId" placeholder="请选择路段名称" clearable size="small" style="width:180px">
          <el-option
                v-for="item in eventTypeData"
                :key="item.id"
                :label="item.eventType"
                :value="item.id"/>
          </el-select>
      </el-form-item>

      <el-form-item label="事件状态" prop="eventState">
        <el-select v-model="queryParams.eventState" placeholder="请选择事件状态" clearable size="small" style="width:180px">
          <el-option
            v-for="dict in eventStateOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="eventGrade">
        <el-select v-model="queryParams.eventGrade" placeholder="请选择处理状态" clearable size="small" style="width:180px">
          <el-option
            v-for="dict in eventGradeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="选择日期" prop="eventTime" >
        <el-date-picker
          v-model="dateRange"
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


      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:event:add']"
        >新增事件</el-button>
      </el-col>

      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="eventList" :default-sort = "{prop: 'eventTime', order: 'descending'}">
      <el-table-column label="隧道名称" align="left" prop="tunnels.tunnelName"  />
      <el-table-column label="所属机构" align="left" prop="tunnels.tunnelName" />
      <el-table-column label="方向" align="left" prop="tunnels.tunnelName" />
      <el-table-column label="桩号" align="left" prop="tunnels.tunnelName" />
      <el-table-column label="车型" align="left" prop="tunnels.tunnelName" />
      <el-table-column label="事件类型" align="center" prop="eventType.eventType" />
      <el-table-column label="事件标题" align="center" prop="eventTitle" />
      <el-table-column label="事件描述" align="center" prop="eventDescription"  :show-overflow-tooltip='true'/>
      <el-table-column label="影响程度" align="center" prop="eventType.eventType" />
      <el-table-column label="开始时间" align="center" prop="eventTime"sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.eventTime, '{y}-{m}-{d} {h}:{m}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="eventTime"sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.eventTime, '{y}-{m}-{d} {h}:{m}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="状态" align="center" prop="eventState" :formatter="eventStateFormat" /> -->
      <el-table-column label="事件状态" align="center" prop="eventState" >
        <template slot-scope="scope">
            <span v-show="scope.row.eventState == 0" style="color: #ff0000;"><i class="el-icon-info" style="color: #ff0000;!important"></i>&nbsp;处理中</span>
            <span v-show="scope.row.eventState == 1" style="color: #00aa00;"><i class="el-icon-success" style="color: #00aa00;!important"></i>&nbsp;已处理</span>
         </template>
      </el-table-column>
      <el-table-column label="处置时长" align="center" prop="eventType.eventType" />
      <el-table-column label="发布时间" align="center" prop="eventTime"sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.eventTime, '{y}-{m}-{d} {h}:{m}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="级别 " align="center" prop="eventGrade" :formatter="eventGradeFormat" />
      <el-table-column label="位置" align="center" prop="eventLocation" />
      <el-table-column prop="specs,quantityUnit" label="伤亡" align="left">
            <template slot-scope="scope"> 
                  死亡：{{scope.row.eventDeath}}<br />
                  受伤：{{scope.row.eventInjured}} 
             </template>
       </el-table-column>
 -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-show="scope.row.eventState == 0"
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="handleOk(scope.row)"
            v-hasPermi="['system:event:edit']"
          >已解决</el-button>
          <!-- <router-link :to="'/business/event/' + scope.row.id" class="link-type"> -->
            <el-button
              size="mini"
              type="text"
              icon="el-icon-chat-line-square"
              v-hasPermi="['system:event:remove']"
              @click="handleDetails(scope.row)"
            >查看详情
            </el-button>
          <!-- </router-link> -->
            <el-button
              size="mini"
              type="text"
              icon="el-icon-chat-line-square"
              v-hasPermi="['system:event:remove']"
              @click="handleDispatch(scope.row)"
            >应急调度
            </el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="loadBigScreen(scope.row)"
            v-hasPermi="['system:event:edit']"
          >进入应急调度大屏</el-button> -->
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
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :before-close="cancel" class="addClass">
      <el-form ref="form1" :model="eventForm" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属隧道" prop="tunnelId">
              <el-select v-model="eventForm.tunnelId" placeholder="请选择所属隧道" clearable size="small">
                <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                  :value="item.tunnelId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属机构" prop="eventTitle">
              <el-input  v-model="eventForm.eventTitle" placeholder="请输入所属机构" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="方向" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择方向">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="桩号" prop="eventTitle">
              <el-input  v-model="eventForm.eventTitle" placeholder="请输入桩号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="车型" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择车型">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件类型" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择事件类型">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件标题" prop="eventTitle">
              <el-input  v-model="eventForm.eventTitle" placeholder="请输入事件标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件类型" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择事件类型">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件描述" prop="eventDescription">
              <el-input  v-model="eventForm.eventDescription" placeholder="请输入事件描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响程度" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择影响程度">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="eventTime">
              <el-date-picker clearable size="small" style="width: 200px"
                v-model="eventForm.eventTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完成时间" prop="eventTime">
              <el-date-picker clearable size="small" style="width: 200px"
                v-model="eventForm.eventTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择完成时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件状态" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择事件状态">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处置时长" prop="eventTitle">
              <el-input  v-model="eventForm.eventTitle" placeholder="请输入处置时长" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发布时间" prop="eventTime">
              <el-date-picker clearable size="small" style="width: 200px"
                v-model="eventForm.eventTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择发布时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置" prop="eventLocation">
              <el-input style="width: 80%;" v-model="eventForm.eventLocation" placeholder="请输入位置" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="级别 " prop="eventGrade">
              <el-radio-group v-model="eventForm.eventGrade">
                <el-radio
                  v-for="dict in eventGradeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="伤亡" prop="eventDeath">
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;" v-model="eventForm.eventDeath" placeholder="请填写死亡人数"  />
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;margin-left: 3%;" v-model="eventForm.eventInjured" placeholder="请填写重伤人数" />
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;margin-left: 3%;" v-model="eventForm.eventInjured" placeholder="请填写轻伤人数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item  style="text-align: center;">
          <el-button  size="small"  type="primary" :loading="submitEventFormLoading" @click="submitEventForm">保存</el-button>
          <el-button  size="small"   @click="eventFormClose">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="details" width="800px" append-to-body :before-close="cancel" class="detailsClass">
      <el-form ref="form1" :model="eventForm" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12" style="display: flex;height: 40px;">
            <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
              <div class="circle" style="background-color: #FF5823;"></div>
            </div>
            <div class="detailsText">事件发生时间</div>
            <div style="color: #82B3C2;line-height: 40px;">2022-08-04 09:41</div>
          </el-col>
          <el-col :span="12" style="display: flex;height: 40px;">
            <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
              <div class="circle" style="background-color: #0065F8;"></div>
            </div>
            <div class="detailsText">预计解除时间</div>
            <div style="color: #82B3C2;line-height: 40px;">2022-08-04 09:41</div>
          </el-col>
        </el-row>

      <el-row>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-discover" style="width: 10px;"></div>
          </div>
          <div class="detailsText">行驶方向</div>
          <div style="color: #82B3C2;line-height: 40px;">青岛方向</div>
        </el-col>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-sunny" style="width: 10px;"></div>
          </div>
          <div class="detailsText">当前天气</div>
          <div style="color: #82B3C2;line-height: 40px;">晴</div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-truck" style="width: 10px;"></div>
          </div>
          <div class="detailsText">压车状态</div>
          <div style="color: #82B3C2;line-height: 40px;">无压车</div>
        </el-col>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-connection" style="width: 10px;"></div>
          </div>
          <div class="detailsText">受影响程度</div>
          <div style="color: #82B3C2;line-height: 40px;">正常通行</div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-location-information" style="width: 10px;"></div>
          </div>
          <div class="detailsText">桩号位置</div>
          <div style="color: #82B3C2;line-height: 40px;">K600+000</div>
        </el-col>
        <el-col :span="12" style="display: flex;height: 40px;">
          <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
            <div class="el-icon-money" style="width: 10px;"></div>
          </div>
          <div class="detailsText">抛洒物</div>
          <div style="color: #82B3C2;line-height: 40px;">无</div>
        </el-col>
      </el-row>
      <hr/>
     <el-row class="rowClass">
        <el-col :span="12">
          <div class="eventClass">事件分类：<span style="font-style:oblique;">故障</span></div>
        </el-col>
        <el-col :span="12">
          <div class="eventClass">事件级别：<span style="font-style:oblique;">一般</span></div>
        </el-col>
      </el-row>
      <el-row class="rowClass">
         <el-col :span="6">
           <div class="eventTitleClass">伤亡情况</div>
         </el-col>
         <el-col :span="6">
           <div class="eventClass">死亡人数：<span style="font-style:oblique;">0</span></div>
         </el-col>
         <el-col :span="6">
           <div class="eventClass">重伤人数：<span style="font-style:oblique;">0</span></div>
         </el-col>
         <el-col :span="6">
           <div class="eventClass">轻伤人数：<span style="font-style:oblique;">0</span></div>
         </el-col>
       </el-row>
       <el-row class="rowClass">
          <el-col :span="4">
            <div class="eventTitleClass">车辆损失</div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">小车：<span style="font-style:oblique;">1</span></div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">货车：<span style="font-style:oblique;">0</span></div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">客车：<span style="font-style:oblique;">0</span></div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">罐车：<span style="font-style:oblique;">0</span></div>
          </el-col>
        </el-row>
        <hr/>
        <el-row >
          <el-col :span="24" style="display: flex;height: 40px;">
            <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
              <div class="el-icon-discount" style="width: 10px;"></div>
            </div>
            <div class="detailsText">事件标题</div>
            <div style="color: #82B3C2;line-height: 40px;width: calc(100% - 140px);">马家峪隧道K6+000济南方向，1辆小型车发生故障，占用应急车道。</div>
          </el-col>
        </el-row>
        <el-row style="margin-bottom: 60px;">
            <el-col :span="24" style="display: flex;height: 40px;">
              <div style="width: 40px;height: 40px;text-align: center;line-height: 40px;">
                <div class="el-icon-discount" style="width: 10px;"></div>
              </div>
              <div class="detailsText">事件描述</div>
              <div style="color: #82B3C2;line-height: 40px;width: calc(100% - 140px);">马家峪隧道K6+000济南方向，1辆小型车发生故障，占用应急车道，超车道、行车道正常通行，请过往车辆减速慢行。</div>
            </el-col>

        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listEvent, getEvent, delEvent, addEvent, updateEvent } from "@/api/event/event";
import { listEventType } from "@/api/event/eventType";
import { listPlan} from "@/api/event/reservePlan";
import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
export default {
  name: "Event",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      //事件类型
      eventTypeData:{},
      // 总条数
      total: 0,
      // 事件管理表格数据
      eventList: [
        {eventTitle:'11'}
      ],
      // 弹出层标题
      title: "",
      // 状态字典
      eventStateOptions: [],
      // 级别 字典
      eventGradeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eventTypeId: null,
        eventTitle: null,
        eventTime: null,
        eventState: null,
        eventGrade: null,
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
      },
      // 表单参数
      form: {},
      eqTunnelData:{},
      // 表单校验
      rules: {
      /*  tunnelId: [{required: true, message: '请选择隧道名称', trigger: 'blur'}], */
        eventTitle: [{required: true, message: '请输入事件标题', trigger: 'blur'}],
        eventTypeId: [{required: true, message: '请选择事件类型', trigger: 'blur'}],
        eventGrade: [{required: true, trigger:'blur'}],
        eventLocation: [{required: true, message: '请输入位置', trigger:'blur'}],
        eventDescription: [{required: true, message: '请输入内容', trigger:'blur'}],
      },
      // 日期范围
      dateRange: [],
      open: false,
      details:false,
      submitEventFormLoading: false,
      direction: 'rtl',
      eventForm:{eventGrade: "0"},
      // 遮罩层
      dloading: false,

    };
  },
  created() {
    this.getList();
    this.getEventType();
    this.getTunnel();
    this.getDicts("sd_event_state").then(response => {
      this.eventStateOptions = response.data;
    });
    this.getDicts("sd_event_grade").then(response => {
      console.log(response.data,"response.data")
      this.eventGradeOptions = response.data;
    });

  },
  methods: {
    /** 查询事件管理列表 */
    getList() {
      this.loading = true;
      listEvent(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          // this.eventList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 查询事件类型列表 */
    getEventType() {
      listEventType().then(response => {
        this.eventTypeData = response.rows;
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
    /** 已解决按钮操作 */
    handleOk(row) {
     // const ids = row.id || this.ids;
      this.$confirm('是否确认此事件已解决！', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          row.eventState = '1';
          return updateEvent(row);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("解决成功");
        }).catch(function() {});
    },
    /** 进入应急指挥大屏 */
    loadBigScreen(row){
      alert("正在研发中...");
    },
    handleDetails(row){
      this.details = true
      this.title = '事件详情'
    },
    handleDispatch(row){
      this.$router.push({ path: "/emergency/administration/dispatch", query: { row: row } });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        tunnelId: null,
        eventTypeId: null,
        eventTitle: null,
        eventTime: null,
        eventState: null,
        eventGrade: "0",
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
        reservePlanId: null,
        flowId: null,
        warningId: null,
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = '新增事件'
    },
    //关闭drawer
    handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
     },
    /** 提交按钮 */
    submitEventForm() {
      //校验信息
     /* if(!this.eventForm.tunnelId){
        this.$modal.msgError("请选择隧道名称！");
        return;
      } */
      if(!this.eventForm.eventTitle){
        this.$modal.msgError("请输入事件标题！");
        return;
      }
      if(!this.eventForm.eventTypeId){
        this.$modal.msgError("请选择事件类型！");
        return;
      }
      if(!this.eventForm.eventTime){
        this.$modal.msgError("请确定事件发生时间！");
        return;
      }
      if(!this.eventForm.eventGrade){
        this.$modal.msgError("请选择事件等级！");
        return;
      }
      if(!this.eventForm.eventLocation){
        this.$modal.msgError("请输入事件位置！");
        return;
      }
      if(!this.eventForm.eventDeath){
        this.$modal.msgError("请输入死亡人数！");
        return;
      }
      if(!this.eventForm.eventInjured){
        this.$modal.msgError("请输入受伤人数！");
        return;
      }
      this.dloading=true;
      if(this.submitEventFormLoading) return
      this.submitEventFormLoading = true
      this.$refs["form1"].validate(async valid => {
        if (valid) {
          debugger
          alert(123)
            await addEvent(this.eventForm).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                 setTimeout(() => {
                  this.resetEvent();
                  this.dloading=false;
                  this.open = false;
                 }, 400);
                this.getList();
              }
            });
        }
        this.submitEventFormLoading = false
      });
    },
    //关闭弹窗
    eventFormClose() {
      this.resetEvent();
      this.open = false;
    },
    // 表单重置
    resetEvent() {
      this.$refs.form1.resetFields()
      this.eventForm.eventTypeId = null
      this.eventForm.eventInjured = null
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.details = false
      this.reset();
    },
  }
};
</script>
<style scoped lang="scss">
  .addClass{
    .el-select{
      width: 250px;
    }
    .el-input{
      width: 250px !important;
    }
    .el-date-editor.el-input, .el-date-editor.el-input__inner{
      width: 250px !important;
    }
  }
  .circle{
    width: 10px;
    height: 10px;
    border-radius: 5px;
    display: inline-block;
  }
  .detailsText{
    display: inline-block;
    margin-left: 20px;
    line-height: 40px;
    width: 100px;
  }
  hr{
    border: solid 1px #ddd;
  }
  .rowClass{
    border-top: solid 1px #ddd;
    border-bottom: solid 1px #ddd;
    height: 40px;
    margin-top: 10px;
  }
  .eventClass{
    height: 30px;
    border-right: solid 1px #ddd;
    width: 100%;
    text-align: center;
    margin-top: 5px;
    line-height: 30px;
  }
  .eventTitleClass{
    height: 40px;
    background-color: #EEEEEE;
    line-height: 40px;
    text-align: center;
  }
</style>
