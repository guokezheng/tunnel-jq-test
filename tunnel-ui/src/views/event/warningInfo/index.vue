<template>
  <div class="app-container">
    <div style="display: flex;font-size: 16px;width: 100%;justify-content: space-between;">
      <div style="line-height: 60px;">设备预警统计:</div>
      <el-card class="card-box">
        今日故障设备统计: {{allmsg}}
      </el-card>
      <el-card class="card-box">
        今日执行故障设备: {{process}}
      </el-card>
      <el-card class="card-box">
        今日故障设备执行率: {{proportion}}
      </el-card>
    </div>

    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <!-- <el-form-item label="预警内容" prop="inforSources">
        <el-input
          v-model="queryParams.inforSources"
          placeholder="请输入预警内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="预警时间" prop="warningTime" >
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          unlink-panels
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="处理状态" prop="processState">
        <el-select
          v-model="queryParams.processState"
          placeholder="请选择处理状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in processStateOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="cyan"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          size="mini"
          @click="seeWarningType()"
          v-hasPermi="['system:warningInfo:add']"
          >查看预警类型</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:warningInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:warningInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:warningInfo:remove']"
        >删除</el-button>
      </el-col> -->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button
            size="mini"
            circle
            icon="el-icon-refresh"
            @click="handleQuery"
          />
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏搜索' : '显示搜索'"
          placement="top"
        >
          <el-button
            size="mini"
            circle
            icon="el-icon-search"
            @click="showSearch = !showSearch"
          />
        </el-tooltip>
      </div>
    </el-row>

    <el-table
      v-loading="loading"
      :data="warningInfoList"
      @selection-change="handleSelectionChange"
      max-height="570"
      :default-sort = "{prop: 'warningTime', order: 'descending'}"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column
        label="预警类型"
        align="center"
        prop="warningType"
        width="150"
      />
      <el-table-column
        label="隧道名称"
        align="center"
        prop="tunnelName"
        width="150"
      />
      <el-table-column
        label="预警名称"
        align="center"
        prop="warningName"
        width="180"
      />
      <el-table-column
        label="预警时间"
        align="center"
        prop="warningTime"
        width="150"
        sortable
      >
        <!-- <template slot-scope="scope">
          <span>{{ parseTime(scope.row.warningTime, '{y}-{m}-{d}') }}</span>
        </template> -->
      </el-table-column>
      <el-table-column label="方向" align="center" prop="holeDirection">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_direction" :value="scope.row.holeDirection"/>
        </template>
      </el-table-column>
      <el-table-column
        label="预警内容"
        align="center"
        prop="inforSources"
        min-width="280"
      />
      <el-table-column
        label="处理状态"
        align="center"
        prop="processState"
        :formatter="processStateFormat"
      />
      <el-table-column label="处理人" align="center" prop="nickName" />
      <el-table-column label="文件（图片）" align="center" prop="picture">
        <template slot-scope="scope">
          <el-popover placement="top-end" title="" trigger="click">
            <img :src="scope.row.picture" style="width: 600px; height: 600px" />
            <img
              slot="reference"
              :src="scope.row.picture"
              style="width: 100px; height: 100px; cursor: pointer"
            />
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" fixed-width />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding "
        min-width="180"
        v-if="processState === '0'"
      >
        <template slot-scope="scope">
          <el-button
            v-show="scope.row.processState === '0'"
            size="mini"
            type="text"
            @click="handleIgnore(scope.row)"
            v-hasPermi="['system:warningInfo:edit']"
            >忽略</el-button
          >
          <el-button
            v-show="scope.row.processState === '0'"
            size="mini"
            type="text"
            @click="handleOk(scope.row)"
            v-hasPermi="['system:warningInfo:remove']"
            >已解决</el-button
          >
          <el-button
            v-show="scope.row.processState === '0'"
            size="mini"
            type="text"
            @click="turnToEventOpen(scope.row)"
            v-hasPermi="['system:warningInfo:remove']"
            >转为事件</el-button
          ><br />
          <el-button
            v-show="scope.row.processState === '0'"
            size="mini"
            type="text"
            @click="turnToStrategyOpen(scope.row)"
            v-hasPermi="['system:warningInfo:remove']"
            >执行相关策略</el-button
          >
          <el-button
            v-show="scope.row.processState === '0'"
            size="mini"
            type="text"
            @click="turnToPlanOpen(scope.row)"
            v-hasPermi="['system:warningInfo:remove']"
            >查看相关预案</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
      class="zwsj"
      title="新增事件"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
    >
      <el-form ref="form1" :model="eventForm" :rules="rules" label-width="80px">
        <el-form-item label="事件标题" prop="eventTitle">
          <el-input
            style="width: 80%"
            v-model="eventForm.eventTitle"
            placeholder="请输入事件标题"
          />
        </el-form-item>
        <el-form-item label="事件类型" prop="eventTypeId">
          <el-select
            v-model="eventForm.eventTypeId"
            placeholder="请选择事件类型"
          >
            <el-option
              v-for="item in eventTypeData"
              :key="item.id"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="eventTime">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="eventForm.eventTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="级别 ">
          <el-radio-group v-model="eventForm.eventGrade">
            <el-radio
              v-for="dict in eventGradeOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
              >{{ dict.dictLabel }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="位置" prop="eventLocation">
          <el-input
            style="width: 80%"
            v-model="eventForm.eventLocation"
            placeholder="请输入位置"
          />
        </el-form-item>
        <el-form-item label="伤亡" prop="eventDeath">
          <el-input-number
            style="width: 35%"
            controls-position="right"
            placeholder="请填写死亡人数"
            :min="0"
            :step="1"
            step-strictly
            v-model.number="eventForm.eventDeath"
          />
          <el-input-number
            style="width: 35%; margin-left: 5%"
            controls-position="right"
            placeholder="请填写受伤人数"
            :min="0"
            :step="1"
            step-strictly
            v-model.number="eventForm.eventInjured"
          />
        </el-form-item>
        <el-form-item label="事件描述" prop="eventDescription">
          <el-input
            style="width: 80%"
            v-model="eventForm.eventDescription"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item
          style="
            text-align: center;
            text-align: center;
            position: absolute;
            bottom: 0;
            width: 100%;
          "
        >
          <el-button
            style="width: 30%"
            type="primary"
            @click="submitEventForm"
            :loading="dloading"
            >{{ dloading ? "提交中 ..." : "保存" }}</el-button
          >
          <el-button style="width: 30%" @click="eventFormClose"
            >取 消</el-button
          >
        </el-form-item>
      </el-form>
    </el-drawer>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改预警信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预警内容" prop="inforSources">
          <el-input v-model="form.inforSources" placeholder="请输入预警内容" />
        </el-form-item>
        <el-form-item label="预警名称" prop="warningName">
          <el-input v-model="form.warningName" placeholder="请输入预警名称" />
        </el-form-item>
        <el-form-item label="预警时间" prop="warningTime">
          <el-date-picker
            clearable
            size="small"
            style="width: 200px"
            v-model="form.warningTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择预警时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理状态" prop="processState">
          <el-select v-model="form.processState" placeholder="请选择处理状态">
            <el-option
              v-for="dict in processStateOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理人" prop="handler">
          <el-input v-model="form.handler" placeholder="请输入处理人" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--查看预警类型对话框-->
    <el-dialog
      v-dialogDrag
      :title="warningTypeTitle"
      :visible.sync="warningTypeVisible"
      :close-on-click-modal="false"
      width="60%"
      append-to-body
    >
      <el-button
        type="success"
        icon="el-icon-plus"
        size="mini"
        @click="handleWarningTypeAdd"
        v-hasPermi="['system:warningInfo:add']"
        >新增</el-button
      >
      <el-table
        ref="multipleTable22"
        :data="warningTypeList"
        tooltip-effect="dark"
        style="width: 100%; margin-top: 10px"
        max-height="520"
        size="mini"
        @selection-change="handleSelectionChange"
        empty-text="暂无预警类型"
      >
        <el-table-column
          label="预警类型"
          align="center"
          prop="typeName"
          width="155"
        />
        <el-table-column label="预警触发值" align="center" width="155">
          <template slot-scope="scope">
            <div style="color: #8423e0">
              {{ scope.row.description }}
              <span v-show="scope.row.judge == 0">大于</span>
              <span v-show="scope.row.judge == 1">小于</span>
              <span v-show="scope.row.judge == 2">等于</span>
              {{ scope.row.threshold }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="相关预案"
          align="center"
          prop="reservePlanInfo"
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              v-show="scope.row.sourceType == 0"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdateWarningCondition(scope.row)"
              v-hasPermi="['business:warningType:edit']"
              >编辑预警条件</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleReservePlan(scope.row)"
              v-hasPermi="['business:warningType:edit']"
              >添加预案</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleDeletePlan(scope.row)"
              v-hasPermi="['business:warningType:delete']"
              >删除预警</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="warningTypeCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增预警类型 -->
    <el-dialog
      :title="warningTypeFormTitle"
      :visible.sync="warningTypeFormVisible"
      width="500px"
      top="65vh"
      append-to-body
    >
      <el-form
        ref="warningTypeForm"
        :model="warningTypeForm"
        :rules="warningTypeFormRules"
        label-width="80px"
      >
        <el-form-item label="预警类型" prop="typeName">
          <el-input
            v-model="warningTypeForm.typeName"
            placeholder="请输入预警类型"
          />
        </el-form-item>
        <el-form-item label="预警条件" prop="judge">
          <el-select
            v-model="warningTypeForm.judge"
            style="width: 100%"
            placeholder="请选择"
          >
            <el-option
              v-for="dict in judgeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警阈值" prop="threshold">
          <el-input
            v-model="warningTypeForm.threshold"
            type="number"
            placeholder="请输入阈值"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="warningTypeForm.description"
            placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="warningTypeBtnLoading" @click="warningTypeFormSubmitForm"
          >确 定</el-button
        >
        <el-button @click="warningTypeFormCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 编辑预警条件对话框 -->
    <el-dialog
      :title="warnConditionTitle"
      :visible.sync="warnConditionVisible"
      width="500px"
      top="65vh"
      append-to-body
    >
      <el-form
        ref="warnConditionForm"
        :model="warnConditionForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="">
          <span style="">{{ warnConditionForm.typeName }}</span>
          <span>{{ warnConditionForm.description }}</span>
          <el-select
            v-model="warnConditionForm.judge"
            style="width: 25%; padding: 2%"
          >
            <el-option
              v-for="dict in judgeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
          <el-input
            v-model="warnConditionForm.threshold"
            placeholder="请输入阈值"
            oninput="value=value.replace(/^\.+|[^\d]/g,'')"
            style="width: 35%; padding: 3%"
          />
        </el-form-item>
        <el-input v-model="warnConditionForm.typeName" v-show="false" />
        <el-input v-model="warnConditionForm.description" v-show="false" />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="warnConditonBtnLoading" @click="warnConditonSubmitForm"
          >确 定</el-button
        >
        <el-button @click="warnConditonCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--查看预警类型对话框-->
    <el-dialog
      v-dialogDrag
      :title="planTitle"
      :visible.sync="planVisible"
      :close-on-click-modal="false"
      width="60%"
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="planList"
        tooltip-effect="dark"
        style="width: 100%"
        max-height="520"
        size="mini"
        @selection-change="handlePlanSelectionChange"
        row-key="id"
        empty-text="暂无预案"
        @row-click="handlePlanRowClick"
      >
        <el-table-column
          type="selection"
          :reserve-selection="true"
          width="55"
          align="center"
        />
        <el-table-column
          label="预案名称"
          align="center"
          prop="planName"
          width="200"
        />
        <el-table-column label="预案描述" align="left" prop="planDescription" />
      </el-table>
      <pagination
        v-show="planTotal > 0"
        :total="planTotal"
        :page.sync="planQueryParams.pageNum"
        :limit.sync="planQueryParams.pageSize"
        @pagination="getPlanList"
      />
      <div slot="footer">
        <el-button type="primary" @click="savePlan">保 存</el-button>
        <el-button @click="planCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      :title="seePlanTitle"
      :visible.sync="seePlanVisible"
      :close-on-click-modal="false"
      width="60%"
      append-to-body
    >
      <div style="width: 100%; height: 31.25rem">
        <el-table v-loading="loading" :data="seePlanList">
          <el-table-column
            label="预案名称"
            align="center"
            prop="planName"
            width="250"
          />
          <!-- <el-table-column label="事件类型" align="center" prop="eventType.eventType" /> -->
          <el-table-column
            label="预案描述"
            align="center"
            prop="planDescription"
            width="350"
          />
          <el-table-column
            label="相关文档"
            align="left"
            prop="pFileList"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.pFileList" :key="index">
                <span
                  @click="loadFile(item)"
                  style="color: #005cbf; cursor: pointer"
                  title="下载"
                >
                  {{ item.fileName }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- <pagination
                   v-show="planTotal>0"
                   :total="planTotal"
                   :page.sync="planQueryParams.pageNum"
                   :limit.sync="planQueryParams.pageSize"
                   @pagination="getPlanList"
                 /> -->
      </div>
      <div slot="footer">
        <el-button @click="seePlanCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 执行相关策略=======================开始=============-->
    <el-dialog
      v-dialogDrag
      :title="seeStrategyTitle"
      :visible.sync="seeStrategyVisible"
      :close-on-click-modal="false"
      width="60%"
      append-to-body
    >
      <div style="width: 100%; height: 31.25rem">
        <el-table
          v-loading="loading"
          :data="seeStrategyList"
          ref="multipleTable"
          @selection-change="handleStrategySelectionChange"
          empty-text="暂无策略"
          row-key="id"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            label="隧道名称"
            align="center"
            prop="tunnels.tunnelName"
            width="200"
          />
          <el-table-column
            label="策略名称"
            align="center"
            prop="strategyName"
            width="200"
          />
          <el-table-column label="策略信息" align="center" prop="strategyInfo">
            <template slot-scope="scope">
              <div style="">{{ scope.row.strategyInfo }}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" :loading="doStrategyBtnLoading" @click="doStrategy">确定执行</el-button>
        <el-button @click="doStrategyCancel"> 关 闭 </el-button>
      </div>
    </el-dialog>
    <!-- 执行相关策略=======================结束=============-->
  </div>
</template>

<script>
import {
  listWarningInfo,
  getWarningInfo,
  delWarningInfo,
  addWarningInfo,
  updateWarningInfo,
  getWarningInfoCount
} from "@/api/event/warningInfo";
import { listEventType } from "@/api/event/eventType";
import { addEvent } from "@/api/event/event";
import {
  listWarningType,
  getWarningType,
  delWarningType,
  addWarningType,
  updateWarningType,
  seePlanListById,
  seeStrategyListById,
} from "@/api/event/warningType";
import {
  listPlan,
  getPlan,
  delPlan,
  addPlan,
  updatePlan,
  addPlanFile,
  loadPlanFile,
  updatePlanFile,
  handleStrategy,
} from "@/api/event/reservePlan";
import { download } from "@/utils/request";
export default {
  name: "WarningInfo",
  //字典值：设备方向，设备品牌，所属车道,使用状态，是否监控，诱导灯控制状态
  dicts: ['sd_direction'],
  watch: {
    drawer(val) {
      if (!val) {
        this.resetEvent();
      }
    },
  },
  data() {
    return {
      dloading: false,
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
      // 预警信息表格数据
      warningInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      warningTypeFormVisible: false,
      // 新增修改预警类型确认按钮loading
      warningTypeBtnLoading: false,
      warningTypeFormTitle: "",
      // 处理状态字典
      processStateOptions: [],
      // 级别 字典
      eventGradeOptions: [],
      processState: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inforSources: null,
        warningName: null,
        warningTime: null,
        processState: null,
        holeDirection: null,
      },
      eventTypeData: {},
      // 表单参数
      form: {},
      warningTypeForm: {},
      eventForm: { eventGrade: "0" },
      // 表单校验
      rules: {
        eventTitle: [
          { required: true, message: "请输入事件标题", trigger: "blur" },
        ],
        eventTypeId: [
          { required: true, message: "请选择事件类型", trigger: "blur" },
        ],
        eventTime: [{ required: true, message: "选择时间", trigger: "blur" }],
        eventLocation: [
          { required: true, message: "请输入位置", trigger: "blur" },
        ],
        eventDeath: [
          { required: true, message: "请填写死亡人数", trigger: "blur" },
        ],
        eventDescription: [
          { required: true, message: "请输入内容", trigger: "blur" },
        ],
      },
      warningTypeFormRules: {
        typeName: [
          { required: true, message: "请输入预警类型", trigger: "blur" },
        ],
        judge: [{ required: true, message: "请选择预警条件", trigger: "blur" }],
        threshold: [{ required: true, message: "请输入阈值", trigger: "blur" }],
        description: [
          { required: true, message: "请输入描述", trigger: "blur" },
        ],
      },
      drawer: false,
      direction: "rtl",
      toEventRowId: null,
      // 日期范围
      dateRange: [],
      //预警类型对话框变量定义
      warningTypeVisible: false,
      warningTypeTitle: "",
      warningTypeList: [],

      /** 设置预警值 */
      //表单参数
      warnConditionForm: {},
      warnConditionVisible: false,
      warnConditonBtnLoading: false,
      warnConditionTitle: "",

      // 判断符 0：大于 1 小于 2 等于字典
      judgeOptions: [],
      /** 预案相关变量 **/
      planTitle: "",
      planVisible: null,
      planList: [],
      planTotal: 0,
      planLoading: false,
      planQueryParams: {
        pageNum: 1,
        pageSize: 20,
      },
      multipleSelection: [], // 存放表格选中项信息
      warnintTypeId: null,

      //查看相关预案
      seePlanTitle: "",
      seePlanVisible: false,
      seePlanList: [],
      // 执行相关策略
      seeStrategyTitle: "",
      seeStrategyVisible: false,
      doStrategyBtnLoading: false,
      seeStrategyList: [],
      handleIds: [],
      allmsg:'',
      process:'',
      proportion:''
    };
  },
  created() {
    this.getList();
    this.getEventType();
    this.getDicts("sd_warning_state").then((response) => {
      this.processStateOptions = response.data;
    });
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeOptions = response.data;
    });
    this.getDicts("sd_warning_type_judge").then((response) => {
      this.judgeOptions = response.data;
    });
    this.getWarningInfo()
  },
  methods: {
    // 事件预警统计
    getWarningInfo(){
      getWarningInfoCount().then((res) =>{
        console.log(res,"事件预警统计")
        this.allmsg = res.data.allmsg
        this.process = res.data.process
        this.proportion = res.data.proportion
      })
    },
    //=========================查看相关预案信息----开始========================================================
    //关闭相关预案
    seePlanCancel() {
      this.seePlanVisible = false;
    },
    // 打开相关预案
    turnToPlanOpen(row) {
      this.seePlanTitle = "查看相关预案";
      this.seePlanVisible = true;
      seePlanListById(row.warningTypeId).then((response) => {
        this.seePlanList = response;
      });
    },
    //下载文件
    loadFile(row) {
      download("system/plan/" + row.id, {}, row.fileName);
    },
    //=========================查看相关预案信息----结束========================================================

    //=========================执行相关策略------开始===========================================================
    //执行策略id勾选事件
    handleStrategySelectionChange(selection) {
      this.handleIds = selection.map((item) => item.id);
    },
    turnToStrategyOpen(row) {
      this.seeStrategyVisible = true;
      this.seeStrategyTitle = "执行相关策略";
      //请求后台数据
      seeStrategyListById(row.warningTypeId).then((response) => {
        this.seeStrategyList = response;
      });
    },

    // 执行 选择的策略
    async doStrategy() {
      this.doStrategyBtnLoading = true
      if (this.handleIds.length > 0) {
        this.$modal.msgSuccess("执行策略中.......");
        for (let i = 0; i < this.handleIds.length; i++) {
          await handleStrategy(handleIds[i]);
        }
        this.seeStrategyVisible = false;
      } else {
        this.$modal.msgError("请先选择需要执行的策略！");
      }
      this.doStrategyBtnLoading = false
    },
    //关闭执行策略对话框
    doStrategyCancel() {
      this.seeStrategyVisible = false;
    },

    //=========================执行相关策略------结束===========================================================
    turnToEventOpen(row) {
      this.drawer = true;
      this.toEventRowId = row.id;
    },
    //// 获取表格选中项信息
    handlePlanSelectionChange(val) {
      this.multipleSelection = val; // 把当前勾选项存放在data属性中的 multipleSelection 数组中
    },
    // 选中当前行
    handlePlanRowClick(row) {
      this.$refs.multipleTable.toggleRowSelection(row)
    },
    // 打开查看预警类型对话框
    seeWarningType() {
      this.warningTypeVisible = true;
      this.warningTypeTitle = "预警类型";
      this.loadWarningTypeList();
    },
    // 关闭查看预警类型对话框
    warningTypeCancel() {
      this.warningTypeVisible = false;
    },
    warningTypeFormCancel() {
      this.warningTypeFormVisible = false;
    },
    /* 查询预警类型*/
    loadWarningTypeList() {
      listWarningType().then((response) => {
        this.warningTypeList = response.rows;
      });
    },
    ///** 修改预警条件按钮操作 */
    handleUpdateWarningCondition(row) {
      this.resetWarnCondition();
      const id = row.id;
      getWarningType(id).then((response) => {
        this.warnConditionForm = response.data;
        this.warnConditionVisible = true;
        this.warnConditionTitle = "设置预警值";
      });
    },
    // 表单重置
    resetWarnCondition() {
      this.warnConditionForm = {
        id: null,
        typeName: null,
        judge: null,
        threshold: null,
        description: null,
        reservePlanIds: null,
        sourceType: null,
      };
      this.resetForm("warnConditionForm");
    },
    // 关闭 设置预警值 对话框
    warnConditonCancel() {
      this.warnConditionVisible = false;
      this.resetWarnCondition();
    },
    //提交 设置预警值 ，关闭对话框
    warnConditonSubmitForm() {
      if(this.warnConditonBtnLoading) return
      this.warnConditonBtnLoading = true
      this.$refs["warnConditionForm"].validate(async (valid) => {
        if (valid) {
          await updateWarningType(this.warnConditionForm).then((response) => {
            if (response.code === 200) {
              this.$modal.msgSuccess("修改成功");
              this.warnConditionVisible = false;
              this.loadWarningTypeList();
            }
          });
        }
        this.warnConditonBtnLoading = false
      });
    },
    //提交 设置预警值 ，关闭对话框
    async warningTypeFormSubmitForm() {
      this.warningTypeBtnLoading = true
      await this.$refs["warningTypeForm"].validate((valid) => {
        if (valid) {
          this.warningTypeForm.sourceType = 0
          addWarningType(this.warningTypeForm).then((response) => {
            if (response.code === 200) {
              this.$modal.msgSuccess("新增成功");
              this.warningTypeFormVisible = false;
              this.loadWarningTypeList();
            }
          });
        }
      });
      this.warningTypeBtnLoading = false
    },
    /////////////////////////////添加预案//////////////////////////////
    // 查询预案列表
    handleReservePlan(row) {
      this.multipleSelection = [];
      let planIds = row.reservePlanIds;
      this.warnintTypeId = row.id;
      this.planTitle = "相关预案";
      this.planVisible = true;
      this.planLoading = true;
      listPlan(this.planQueryParams).then((response) => {
        this.planList = response.rows;
        this.planTotal = response.total;
        this.planLoading = false;
        this.$refs.multipleTable.clearSelection();
        if (planIds != null && planIds.length > 0) {
          this.multipleSelection = planIds.split(",");
          let array = [];
          for (let i = 0; i < this.planList.length; i++) {
            for (let j = 0; j < this.multipleSelection.length; j++) {
              console.log("===" + j);
              if (this.multipleSelection[j] == this.planList[i].id) {
                array.push(this.planList[i]);
              }
            }
          }
          //执行回显勾选
          for (let i = 0; i < array.length; i++) {
            this.$refs.multipleTable.toggleRowSelection(array[i], true); //这是默认选中上的
          }
        }
      });
    },
    /** 查询预案信息列表 */
    getPlanList() {
      this.planLoading = true;
      listPlan(this.planQueryParams)
        .then((response) => {
          this.planList = response.rows;
          this.planTotal = response.total;
          this.planLoading = false;
        })
        .then(() => {
          for (let i = 0; i < this.lists.length; i++) {
            if (this.lists[i].style == this.radio) {
              this.$refs.table.toggleRowSelection(this.lists[i], true);
            }
          }
        });
    },
    // 删除预警类型
    handleDeletePlan(row) {
      delWarningType(row.id).then((response) => {
        if (response.code === 200) {
          this.$modal.msgSuccess("删除成功");
          this.loadWarningTypeList();
        }
      }).catch(() => {
        this.$modal.msgError('删除失败，请稍后重试！')
      });
    },
    //关闭添加预案 对话框
    planCancel() {
      this.planVisible = false;
    },
    // 保存添加预案
    savePlan() {
      let reservePlanIds = "";
      if (this.multipleSelection != null && this.multipleSelection.length > 0) {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          reservePlanIds = reservePlanIds + this.multipleSelection[i].id + ",";
        }
        reservePlanIds = reservePlanIds.substring(0, reservePlanIds.length - 1);
      }
      updateWarningType({
        id: this.warnintTypeId,
        reservePlanIds: reservePlanIds,
      }).then((response) => {
        if (response.code === 200) {
          this.$modal.msgSuccess("操作成功");
          this.planVisible = false;
          this.loadWarningTypeList();
        }
      });
    },

    //关闭drawer
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    /** 查询事件类型列表 */
    getEventType() {
      listEventType().then((response) => {
        this.eventTypeData = response.rows;
      });
    },
    /** 查询预警信息列表 */
    getList() {
      this.loading = true;
      listWarningInfo(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.warningInfoList = response.rows;
          console.log(response,"response.total")
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 处理状态字典翻译
    processStateFormat(row, column) {
      if(row.processState == 0){
				return "未处理"
			}else if(row.processState == 1){
				return "已处理"
			}else if(row.processState == 2){
				return "已忽略"
			}else if(row.processState == 3){
				return "转为事件"
			}
    },
    // 级别 字典翻译
    eventGradeFormat(row, column) {
      return this.selectDictLabel(this.eventGradeOptions, row.eventGrade);
    },
    holeDirectionFormat(row) {
      if (row.holeDirection === "Y") {
        return (this.queryParams.holeDirection = "下行");
      } else if (row.holeDirection === "Z") {
        return (this.queryParams.holeDirection = "上行");
      }
      // else if (row.holeDirection === "N") {
      //   return (this.queryParams.holeDirection = "北口");
      // } else if (row.holeDirection === "S") {
      //   return (this.queryParams.holeDirection = "南口");
      // }
    },
    // 取消按钮
    close() {
      this.open = false;
      this.reset();
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
        inforSources: null,
        warningName: null,
        warningTime: null,
        processState: null,
        handler: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    resetWarningTypeForm() {
      this.warningTypeForm = {};
      this.resetForm("warningTypeForm");
    },
    // 表单重置
    resetEvent() {
      // this.eventForm = {
      //   eventType: null,
      //   eventTime: null,
      //   eventGrade: "0",
      //   eventLocation: null,
      //   eventDeath: null,
      //   eventInjured: null,
      //   eventDescription: null
      // };
      this.$refs.form1.resetFields();
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加预警信息";
    },
    handleWarningTypeAdd() {
      this.resetWarningTypeForm();
      this.warningTypeFormVisible = true;
      this.warningTypeFormTitle = "添加预警类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWarningInfo(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改预警信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateWarningInfo(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addWarningInfo(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除选中数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delWarningInfo(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 转为事件按钮操作 */
    handleToEvent(row) {
      this.title = "转为事件";
    },
    /** 忽略按钮操作 */
    handleIgnore(row) {
      //  const ids = row.id || this.ids;
      this.$confirm("是否确认忽略！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          row.processState = "2";
          return updateWarningInfo(row);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("忽略成功");
        })
        .catch(function () {});
    },
    /** 已解决按钮操作 */
    handleOk(row) {
      // const ids = row.id || this.ids;
      this.$confirm("是否确认此预警已解决！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          row.processState = "1";
          return updateWarningInfo(row);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("解决成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/warningInfo/export",
        {
          ...this.queryParams,
        },
        `system_warningInfo.xlsx`
      );
    },
    /** 提交按钮 */
    submitEventForm() {
      this.dloading = true;
      this.$refs["form1"].validate((valid) => {
        if (valid) {
          addEvent(this.eventForm).then((response) => {
            if (response.code === 200) {
              updateWarningInfo({ id: this.toEventRowId, processState: "3" });
              this.$modal.msgSuccess("新增成功");
              setTimeout(() => {
                this.dloading = false;
                this.drawer = false;
              }, 400);
              this.getList();
            }
          });
        }
      });
    },
    //关闭drawer
    eventFormClose() {
      this.resetEvent();
      this.drawer = false;
    },
  },
};
</script>
<style>
.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}
</style>
<style scoped>
  .card-box{
    width: 30%;
    text-align: center;
    font-weight: bold;
  }
</style>
