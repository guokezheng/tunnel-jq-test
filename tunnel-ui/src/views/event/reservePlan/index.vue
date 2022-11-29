<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
    >
      <el-form-item label="所属隧道" prop="tunnelId">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择所属隧道"
          @change="changeSelection"
          clearable
          size="small"
        >
          <el-option
            v-for="(item, index) in eqTunnelData"
            :key="index"
            :label="item.tunnelName"
            :value="item.tunnelId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="预案类别" prop="category">
        <el-select
          v-model="queryParams.category"
          placeholder="请选择预案类别"
          clearable
          size="small"
        >
          <el-option
            v-for="(item, index) in planCategory"
            :key="index"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="事件类型" prop="planTypeId">
        <el-select
          v-model="queryParams.planTypeId"
          clearable
          placeholder="请选择事件类型"
          size="small"
        >
          <el-option
            v-for="(item, index) in planTypeData"
            :key="index"
            :label="item.eventType"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="预案名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          clearable
          placeholder="请输入预案名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button size="mini" type="primary" @click="handleQuery"
          >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          v-hasPermi="['business:plan:add']"
          size="mini"
          type="primary"
          plain
          @click="handleAdd()"
          >新增
        </el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['business:plan:add']"
          icon="el-icon-plus"
          size="mini"
          type="primary"
          plain
          @click="handleAdd()"
          >新增
        </el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" content="刷新" effect="dark" placement="top">
          <el-button
            circle
            icon="el-icon-refresh"
            size="mini"
            @click="handleQuery"
          />
        </el-tooltip>
        <el-tooltip
          :content="showSearch ? '隐藏搜索' : '显示搜索'"
          class="item"
          effect="dark"
          placement="top"
        >
          <el-button
            circle
            icon="el-icon-search"
            size="mini"
            @click="showSearch = !showSearch"
          />
        </el-tooltip>
      </div>
    </el-row> -->

    <el-table
      ref="planTable"
      v-loading="loading"
      :data="planList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      max-height="640"
      :row-class-name="tableRowClassName"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="预案ID" align="center" prop="id" /> -->
      <el-table-column
        align="center"
        label="隧道名称"
        prop="sdTunnels.tunnelName"
        width="130"
      />
      <el-table-column
        align="center"
        label="预案类别"
        prop="category"
        :formatter="categoryFormat"
      />
      <el-table-column align="center" label="预案名称" prop="planName" />
      <el-table-column
        align="center"
        label="分区"
        prop="sdTunnelSubarea.sName"
        width="130"
      />
      <el-table-column
        align="center"
        label="事件类型"
        prop="eventType.eventType"
      />
      <el-table-column
        align="center"
        label="预案描述"
        prop="planDescription"
        width="200"
        :show-overflow-tooltip="true"
      >
        <!-- <el-table-column label="查看工作台" align="left" prop="planDescription" width="200" /> -->
        <!-- <template slot-scope="scope">
          <el-popover
            :content="scope.row.planDescription"
            placement="top-start"
            trigger="hover"
            width="200"
          >
            <div
              slot="reference"
              style="
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                cursor: default;
              "
            >
              {{ scope.row.planDescription }}
            </div>
          </el-popover>
        </template> -->
      </el-table-column>

      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="相关文档"
        width="130"
      >
        <template slot-scope="scope">
          <el-button
            v-show="scope.row.planFileId && scope.row.planFileId != 'null'"
            icon="el-icon-link"
            size="mini"
            style="cursor: pointer; color: #39adff"
            type="text"
            @click="openFileDrawer(scope.row)"
            >点击查看
          </el-button>
          <div v-show="!scope.row.planFileId || scope.row.planFileId == 'null'">
            无
          </div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="相关策略"
        width="200"
      >
        <template slot-scope="scope">
          <!-- <p v-show="scope.row.strategyNames != null"
                  style="overflow: hidden;text-overflow: ellipsis;cursor: default;"
                  @click="showStrategyContent(scope.row)">
                 {{ scope.row.strategyNames }}
            </p> -->
          <el-tag
            v-show="scope.row.strategyNames != null"
            :key="tag"
            v-for="tag in scope.row.strategyNames"
            :disable-transitions="false"
            @close="handleClose(tag)"
            style="display: block"
          >
            {{ tag }}
          </el-tag>
          <div v-show="scope.row.strategyNames == ''">无</div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['business:plan:edit']"
            class="tableBlueButtton"
            size="mini"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['business:plan:remove']"
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
          <el-button
            v-hasPermi="['business:plan:add']"
            size="mini"
            class="tableBlueButtton"
            @click="chooseStrategyInfo(scope.row)"
            >配置策略
          </el-button>
          <el-button
            v-hasPermi="['business:plan:remove']"
            size="mini"
            class="tableBlueButtton"
            @click="openWorkbench(scope.row)"
            >预览
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
    <el-dialog
      :title="drawerFileTitle"
      :visible.sync="drawerFile"
      width="500px"
      append-to-body
      :before-close="handleFileClose"
    >
      <!-- <el-drawer
      :before-close="handleFileClose"
      :direction="direction"
      :title="drawerFileTitle"
      :visible.sync="drawerFile"
      class="zwsj"
    > -->
      <el-table v-loading="loading" :data="planFileList">
        <el-table-column align="center" label="序号" width="100px">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="文件名称" prop="fileName" />
        <el-table-column
          align="center"
          class-name="small-padding fixed-width"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button
              v-hasPermi="['business:plan:edit']"
              :loading="loadFileLoading"
              size="mini"
              type="text"
              @click="loadFile(scope.row)"
              >下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- </el-drawer> -->
    </el-dialog>
    <!-- 配置策略选择窗口-->
    <el-dialog
      :title="title"
      :visible.sync="handleStrategyVisible"
      append-to-body
      width="60%"
    >
      <div style="width: 100%; height: 31.25rem; overflow: auto">
        <el-table
          ref="multipleTable"
          :data="handleStrategyList"
          empty-text="暂无策略"
          row-key="id"
          @selection-change="handleStrategySelectionChange"
          @row-click="multipleTableRowClick"
        >
          <el-table-column align="center" type="selection" width="55" />
          <el-table-column
            align="center"
            label="隧道名称"
            prop="tunnels.tunnelName"
            width="200"
          />
          <el-table-column
            align="center"
            label="策略名称"
            prop="strategyName"
            width="200"
          />
          <el-table-column align="center" label="策略信息" prop="slist">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.slist" :key="index">
                <p style="color: #005cbf">
                  {{ item }}
                </p>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="doStrategy">确定执行</el-button>
        <el-button @click="doStrategyCancel"> 关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 相关策略策略，选择对话框-->
    <el-dialog
      :title="addStrategyTitle"
      :visible.sync="addStrategyVisible"
      append-to-body
      width="60%"
    >
      <div style="width: 100%; height: 31.25rem; overflow: auto">
        <el-table
          ref="addMultipleTable"
          :data="addStrategyList"
          empty-text="暂无策略"
          row-key="id"
          @selection-change="handleSelectionAddSChange"
          @row-click="addMultipleTableRowClick"
        >
          <el-table-column align="center" type="selection" width="55" />
          <el-table-column
            align="center"
            label="隧道名称"
            prop="tunnels.tunnelName"
            width="200"
          />
          <el-table-column
            align="center"
            label="策略名称"
            prop="strategyName"
            width="200"
          />
          <el-table-column align="center" label="策略信息" prop="slist">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.slist" :key="index">
                <span style="color: #005cbf">
                  {{ item }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="addStrategySubmit">确 认</el-button>
        <el-button @click="addStrategyCancel">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="strategyDialog" title="相关策略" width="30%">
      <div
        v-for="(item, index) of str_arr"
        :key="index"
        style="font-size: 16px; line-height: 40px; padding-left: 20px"
      >
        {{ item }}
      </div>
    </el-dialog>

    <!--  预览-->
    <work-bench ref="workBench"></work-bench>
    <!-- <el-dialog
      :before-close="handleClose"
      :visible.sync="workbenchOpen"
      append-to-body
      title="预览"
      width="86.5%"
    >
      <img
        alt=""
        class="chedaoImage"
        src="../../../assets/image/lane/fenghuangshan.png"
      />
      <div
        v-for="(item, index) in selectedIconList"
        :key="index"
        :class="
          item.eqType == 7 || item.eqType == 8 || item.eqType == 9
            ? 'light-' + item.position.left
            : ''
        "
        :style="{
          left: item.position.left - 12 + 'px',
          top: item.position.top + 52 + 'px',
          'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
        }"
        class="icon-box mouseHover"
      >
        <div
          v-show="
            (item.eqType != 7 &&
              item.eqType != 16 &&
              item.eqType != 15 &&
              item.eqType != 8 &&
              item.eqType != 9 &&
              item.eqType != 21 &&
              item.display == true) ||
            ((item.eqType == 7 ||
              item.eqType == 8 ||
              item.eqType == 9 ||
              item.eqType == 21) &&
              item.display == true &&
              lightSwitch == 1)
          "
          :class="{ focus: item.focus }"
        >
          <img
            v-for="(url, indexs) in item.url"
            :key="item.deptId + indexs"
            :height="item.iconHeight"
            :src="url"
            :style="item.eqType || item.eqType == 0 ? 'cursor: pointer;' : ''"
            :width="item.iconWidth"
            style="position: relative"
          />
        </div>
      </div>
      <el-steps :active="active" finish-status="success">
        <el-step
          v-for="item in previewList"
          :key="item.strategyId"
          :title="item.strategyName"
        ></el-step>
      </el-steps>
      <span slot="footer" class="dialog-footer">
        <el-button @click="workbenchOpenEvent">取 消</el-button>
        <el-button type="primary" @click="closeDialogVisible">确 定</el-button>
      </span>
    </el-dialog> -->

    <!-- 新增弹窗 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form
        ref="addform1"
        :model="reservePlanDrawForm"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="reservePlanDrawForm.tunnelId"
            placeholder="请选择所属隧道"
            style="width: 80%"
            @change="changeSelection"
          >
            <el-option
              v-for="(item, index) in eqTunnelData"
              :key="index"
              :label="item.tunnelName"
              :value="item.tunnelId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="隧道分区" prop="sId">
          <el-select
            v-model="reservePlanDrawForm.sId"
            placeholder="请选择所属隧道"
            style="width: 80%"
            @change="changePartitionSelection"
          >
            <el-option
              v-for="(item, index) in eqTunnelDataList"
              :key="index"
              :label="item.sName"
              :value="item.sId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预案类别" prop="category">
          <el-select
            v-model="reservePlanDrawForm.category"
            placeholder="请选择预案类别"
            style="width: 80%"
          >
            <el-option
              v-for="(item, index) in planCategory"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId">
          <el-select
            v-model="reservePlanDrawForm.planTypeId"
            placeholder="请选择事件类型"
            style="width: 80%"
          >
            <el-option
              v-for="(item, index) in planTypeData"
              :key="index"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预案名称" prop="planName">
          <el-input
            v-model="reservePlanDrawForm.planName"
            placeholder="请输入预案名称"
            style="width: 80%"
          />
        </el-form-item>
        <el-form-item label="预案描述" prop="planDescription">
          <el-input
            v-model="reservePlanDrawForm.planDescription"
            maxlength="250"
            placeholder="请输入预案描述"
            style="width: 80%"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="相关文档" prop="eventLocation">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :file-list="fileList"
            :http-request="uploadFile"
            :limit="5"
            :on-change="handleChange"
            :on-exceed="handleExceed"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            action="http://xxx.xxx.xxx/personality/uploadExcel"
            class="upload-demo"
            multiple
            style="width: 80%"
          >
            <el-button slot="trigger" size="small" type="primary"
              >选取文件</el-button
            >
            <!-- <el-button size="small" style="margin-left: 133px;" type="success" @click="submitUpload">上传到服务器
            </el-button> -->
            <span
              slot="tip"
              class="el-upload__tip"
              style="font-style: italic; color: red; padding-left: 5%"
              >{{ text }}</span
            >
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelsubmitUpload">取 消</el-button>
        <el-button type="primary" @click="submitUpload">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 配置策略 -->
    <el-dialog
      :before-close="handleClose"
      :title="addTitle"
      :visible.sync="strategyVisible"
      append-to-body
      width="60%"
    >
      <el-form
        ref="form1"
        :inline="true"
        :model="reserveProcessDrawForm"
        label-width="120px"
        size="medium"
      >
        <el-row :gutter="20">
          <el-col
            v-for="(item, index) in planTypeIdList"
            :key="index"
            :span="24"
            class="colflex"
          >
            <el-form-item label="节点名称" prop="processName">
              <el-input
                v-model="item.processName"
                placeholder="请输入流程节点名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="节点顺序" prop="processSort">
              <el-input-number
                v-model="item.processSort"
                :max="10"
                :min="1"
                label="描述文字"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="相关策略">
              <el-cascader
                v-model="item.handleStrategyList"
                :options="options"
                :props="checkStrictly"
                :show-all-levels="false"
                clearable
                collapse-tags
              ></el-cascader>
              <!-- @change="handleChangeStrategy(item.handleStrategyList)" -->
            </el-form-item>
            <div class="dialog-footer">
              <el-button type="text" @click.native="addStrategy(index)"
                >添加</el-button
              >
              <el-button type="text" @click.native="deleteStrategy(index)"
                >删除</el-button
              >
            </div>
          </el-col>
        </el-row>
      </el-form>
      <el-form-item style="text-align: right; width: 100%"> </el-form-item>
      <div slot="footer" class="dialog-footer">
        <el-button
          style="width: 10%"
          type="primary"
          v-hasPermi="['plan:process:add']"
          @click="submitstrategy"
          >保存</el-button
        >
        <el-button style="width: 10%" @click="closeStrategy">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import workBench from "./workBench";
import {
  listPlan,
  getPlan,
  delPlan,
  addPlan,
  updatePlan,
  addPlanFile,
  loadPlanFile,
  updatePlanFile,
  listStrategyByPlanId,
  partitionTunnel,
  tunnelNames,
  getPlanType,
} from "@/api/event/reservePlan";
import { listEventType } from "@/api/event/eventType";
import { listReservePlanFile } from "@/api/event/reservePlanFile";
import { download } from "@/utils/request";
import {
  listStrategy,
  getStrategy,
  handleStrategy,
  getRl,
} from "@/api/event/strategy";
import { listType, getTypeAndStrategy } from "@/api/equipment/type/api.js";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { fastLerp } from "zrender/lib/tool/color";
import {
  addProcess,
  getListByRId,
  previewDisplay,
} from "@/api/event/reserveProcess";

export default {
  name: "Plan",
  components: {
    workBench,
  },
  data() {
    return {
      deviceList: [], //需要操作的设备以及状态数据
      previewList: [], //预览数据
      checkStrictly: {
        multiple: false,
        value: "id",
        label: "name",
        children: "children",
        // emitPath: false,
        checkStrictly: true,
      },
      reserveId: "",
      //新增弹窗
      dialogFormVisible: false,
      visibleAdd: false,
      //配置策略
      strategyVisible: false,
      //策略数组
      planTypeIdList: [
        {
          processName: "",
          processSort: "",
          handleStrategyList: "",
        },
      ],
      title: "",
      addTitle: "",
      options: [],
      props: {
        label: "name",
        value: "id",
        children: "children",
        multiple: true,
        emitPath: false,
      },
      active: 0,
      //工作台
      workbenchOpen: false,
      str_arr: [],
      strategyDialog: false, //相关策略弹窗
      text: "*注*：上传文件不可超过1M且不可超过五个文件",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中关系ID数组
      rlIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预案信息表格数据
      planList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planTypeId: null,
        planName: null,
        planFileId: null,
        strategyId: null,
        tunnelId: null,
        category: null,
      },
      // 表单校验
      rules: {
        planName: [
          { required: true, message: "请输入预案名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" },
        ],
        planTypeId: {
          required: true,
          message: "请选择事件类型",
          trigger: "change",
        },
        tunnelId: {
          required: true,
          message: "请选择隧道类型",
          trigger: "change",
        },
        sId: { required: true, message: "请选择分区隧道", trigger: "change" },
        planDescription: {
          required: true,
          message: "请输入预案描述",
          trigger: "blur",
        },
        category: {
          required: true,
          trigger: "change",
          message: "请选择预案类别",
        },
      },
      //draw开关
      drawer: false,
      // 侧边层标题
      drawerTitle: "",
      //搜索-事件类型
      planTypeData: [],
      //from表单参数
      reservePlanDrawForm: {
        planTypeId: null, //事件类型
        planName: null, //预案名称
        category: null, //预案类别
        planDescription: null, //预案描述
        strategyId: null, //多个策略ID
        strategyNames: null, //多个策略的名称，以：分割
        planFileId: null,
        tunnelId: null, //隧道
        sId: null, //分区隧道
      },
      reserveProcessDrawForm: {
        reserveId: null, //预案id
        deviceTypeId: null, //设备类型id
        strategyId: null, //策略id
        processName: null, //流程节点名称
        processSort: null, //流程节点顺序
        strategyIds: null, //多个策略id
      },
      strategyId: [{}],
      // 遮罩层
      dloading: false,
      //drawer方向
      direction: "rtl",
      //策略数据
      strategyData: [], //策略下来数据信息
      fileData: "", // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      fileDrawForm: {},
      //相关文件drawer标题
      drawerFileTitle: "",
      //相关文件draw开关
      drawerFile: false,
      loadFileLoading: false,
      // 预案文件信息表格数据
      planFileList: [],
      // 遮罩层
      fileLoading: true,
      //需要移除的文件ids
      removeIds: [],
      //添加or编辑标志；add/edit
      planChangeSink: null,
      // 选择--需要执行的控制策略
      handleStrategyVisible: false,
      handleStrategyList: [],
      // title:'',
      handleIds: [],
      //选择---新增控制策略
      addStrategyVisible: false,
      addStrategyList: [],
      addStrategyTitle: "",
      adsIds: [],
      multipleSelectionIds: [], //存放选择的id数组
      adsStrategyNames: [],
      // 非单个禁用
      adsSingle: true,
      // 非多个禁用
      adsMultiple: true,
      tunnelId: "WLJD-JiNan-YanJiuYuan-FHS",
      selectedIconList: [],
      lightSwitch: 0,
      eqTunnelData: [], //隧道下拉
      eqTunnelDataList: [], //分区隧道下拉
      timer: null, //步骤条定时器
      planCategory: [], //预案类别下拉
      strategyRlData: [], //返回策略设备和状态
    };
  },
  created() {
    this.getList();
    this.getPlanType(); //事件类型下拉
    // this.getStrategyInfo();//策略下拉
    this.getTunnelData(this.tunnelId);
    this.lightSwitchFunc();
    this.ceshiTime();
    tunnelNames().then((res) => {
      this.eqTunnelData = res.rows;
      console.log(this.eqTunnelData, "111");
      this.eqTunnelData.forEach((item) => {
        item.sdTunnelSubareas.forEach((item, index) => {
          this.eqTunnelDataList.push(item);
        });
      });
    });
    this.getDicts("sd_reserve_plan_category").then((response) => {
      this.planCategory = response.data;
    });
  },
  methods: {
    categoryFormat(row, column) {
      return this.selectDictLabel(this.planCategory, row.category);
    },
    tunnelIdFormat(row, column) {
      return this.selectDictLabel(this.eqTunnelData, row.tunnelId);
    },
    workbenchOpenEvent() {
      this.getTunnelData(this.tunnelId);
      this.workbenchOpen = false;
    },
    closeDialogVisible() {
      this.getTunnelData(this.tunnelId);
      this.dialogVisible = false;
    },
    deleteStrategy(index) {
      console.log(index);
      if (index == 0) {
        return this.$modal.msgError("至少保留一行");
      }
      this.planTypeIdList.splice(index, 1);
    },
    addStrategy(index) {
      let obj = {
        processName: "",
        processSort: "",
        handleStrategyList: "",
      };
      this.planTypeIdList.splice(index + 1, 0, obj);
    },
    //获得预案类别
    selectPlanType() {
      getPlanType().then((res) => {
        this.planCategory = res.data;
      });
    },
    //点击了取消
    cancelsubmitUpload() {
      this.dialogFormVisible = false;
      this.resetReservePlanDrawForm();
    },
    //删除一行
    updataDeleteForm(index) {
      if (this.planTypeIdList.length <= 1) {
        this.$modal.msgError("请保留一行");
        return;
      }
      this.planTypeIdList.splice(index, 1);
    },
    //添加一行
    addFrom() {
      this.planTypeIdList.push({
        a: [],
      });
      /*this.reserveProcessDrawForm.strategyId.push([{
        id:[]
      }])*/
    },
    ceshiTime() {
      this.timer = setInterval(() => {
        if (this.active++ > 2) this.active = 0;
      }, 1000);
    },
    showStrategyContent(row) {
      this.strategyDialog = true;
      this.str_arr = row.strategyNames.split("；");
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },

    getTunnelData(tunnelId) {
      let that = this;
      // that.upList = [];
      // that.downList = [];
      const params = {
        tunnelId: tunnelId,
      };
      getTunnels(tunnelId).then((response) => {
        let res = "";
        if (response.data) {
          res = response.data.storeConfigure;
          //存在配置内容
          if (res != null && res != "" && res != undefined) {
            res = JSON.parse(res);
            listType({ isControl: 1 })
              .then((response) => {
                var arr = [];
                for (let item1 of response.rows) {
                  for (let item of res.eqList) {
                    item.focus = false;
                    if (item1.typeId == item.eqType) {
                      item.iconWidth = Number(item1.iconWidth);
                      item.iconHeight = Number(item1.iconHeight);
                      arr.push(item);
                    }
                  }
                }
                this.selectedIconList = arr; //这是最终需要挂载到页面上的值
                console.log(this.selectedIconList, "this.selectedIconList");
              })
              .then(() => {});
          } else {
            //不存在
            that.selectedIconList = [];
            //工作台默认背景图
            // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
            that.upList = [];
            that.downList = [];
            that.leftDirection = "";
            that.rightDirection = "";
          }
        }
      });
    },
    //关闭策略弹窗
    closeStrategy() {
      this.getTunnelData(this.tunnelId);
      this.strategyVisible = false;
    },
    // 编辑策略保存方法
    submitstrategy() {
      for (let i = 0; i < this.planTypeIdList.length; i++) {
        console.log(this.planTypeIdList[i].handleStrategyList);
        if (
          this.planTypeIdList[i].handleStrategyList == "" ||
          this.planTypeIdList[i].processName == ""
        ) {
          return this.$modal.msgWarning("请填写完整");
        }
      }
      let data = {
        reserveId: this.reserveId,
        sdReserveProcesses: this.planTypeIdList,
      };
      addProcess(data).then((res) => {
        if (res.code === 200) {
          this.strategyVisible = false;
          this.$modal.msgSuccess(res.msg);
          this.getList();
        }
      });
    },
    //预览按钮
    openWorkbench(row) {
      this.tunnelId = row.tunnelId;
      this.$nextTick(() => {
        this.$refs.workBench.currentClass = "red";
        this.$refs.workBench.id = row.id;
        this.$refs.workBench.tunnelId = this.tunnelId;
        this.$refs.workBench.init();
      });
    },
    //=========================执行相关预案开始=====================
    //执行策略
    /* executionStrategy(row){
       this.$modal.msgSuccess("执行策略中.......");
       handleStrategy(row.strategyId);
     }, */
    // 选择将要执行的策略
    async chooseStrategyInfo(row) {
      this.reserveId = row.id;
      await getTypeAndStrategy({ isControl: 1 }).then((res) => {
        this.options = res.data;
      });
      getListByRId({ reserveId: this.reserveId }).then((res) => {
        this.planTypeIdList = res.data;
        if (this.planTypeIdList.length == 0) {
          this.planTypeIdList = [
            {
              processName: "",
              processSort: "",
              handleStrategyList: "",
            },
          ];
        } else {
          let data = res.data;
          data.forEach((item, index) => {
            // this.planTypeIdList[index].handleStrategyList = item.strategyId;
            this.planTypeIdList[index].processSort = item.processSort;
            this.planTypeIdList[index].processName = item.processName;
          });
        }
      });
      this.handleStrategyList = [];
      this.reserveProcessDrawForm.reserveId = row.id;
      this.strategyVisible = true;
      // })
    },
    //执行策略id勾选事件
    handleStrategySelectionChange(selection) {
      this.handleIds = selection.map((item) => item.id);
    },
    // 关闭 相关策略对话框
    doStrategyCancel() {
      this.handleStrategyVisible = false;
    },
    // 执行 选择的策略
    doStrategy() {
      if (this.handleIds.length > 0) {
        this.$modal.msgSuccess("执行策略中.......");
        for (let i = 0; i < this.handleIds.length; i++) {
          handleStrategy(this.handleIds[i])
            .then((response) => {
              this.$modal.msgSuccess("执行成功");
              this.doStrategyCancel();
            })
            .catch(() => {
              this.$modal.msgError("执行失败");
            });
        }
      } else {
        this.$modal.msgError("请先选择需要执行的策略！");
      }
    },
    //=========================执行相关预案结束=====================

    //=========================选择相关预案开始=====================
    /* 跳至选择相关策略页面*/
    openAddStragegyDialog() {
      this.addStrategyVisible = true;
      this.addStrategyTitle = "相关策略";
      listStrategy({ strategyType: 0 }).then((response) => {
        this.addStrategyList = response.rows;
        this.$refs.addMultipleTable.clearSelection();
        if (
          this.multipleSelectionIds != null &&
          this.multipleSelectionIds.length > 0
        ) {
          let array = [];
          for (let i = 0; i < this.addStrategyList.length; i++) {
            for (let j = 0; j < this.multipleSelectionIds.length; j++) {
              if (this.multipleSelectionIds[j] == this.addStrategyList[i].id) {
                array.push(this.addStrategyList[i]);
              }
            }
          }
          //执行回显勾选
          this.$nextTick(() => {
            for (let k = 0; k < array.length; k++) {
              this.$refs.addMultipleTable.toggleRowSelection(array[k], true); //这是默认选中上的
            }
          });
        }
      });
    },
    // 关闭控制策略对话框
    addStrategyCancel() {
      this.addStrategyVisible = false;
    },
    // 选择,添加控制策略ids
    handleSelectionAddSChange(selection) {
      this.adsIds = selection.map((item) => item.id);
      this.adsStrategyNames = selection.map((item) => item.strategyName);
      this.adsSingle = selection.length !== 1;
      this.adsMultiple = !selection.length;
    },
    // 提交策略，暂存关闭对话框
    addStrategySubmit() {
      this.multipleSelectionIds = this.adsIds; //选中的策略id
      this.reservePlanDrawForm.strategyNames = "";
      this.reservePlanDrawForm.strategyId = "";
      for (let i = 0; i < this.adsStrategyNames.length; i++) {
        this.reservePlanDrawForm.strategyNames =
          this.reservePlanDrawForm.strategyNames +
          this.adsStrategyNames[i] +
          "；";
        this.reservePlanDrawForm.strategyId =
          this.reservePlanDrawForm.strategyId + this.adsIds[i] + "；";
      }
      this.addStrategyVisible = false;
    },
    //===========================选择相关预案结束=========================
    //点击查看文件
    openFileDrawer(row) {
      console.log(row, "row");
      this.fileLoading = true;
      listReservePlanFile({ planFileId: row.planFileId }).then((response) => {
        this.drawerFileTitle = "相关文档";
        this.planFileList = response.rows;
        this.fileLoading = false;
        this.drawerFile = true;
      });
    },
    //下载文件
    async loadFile(row) {
      this.loadFileLoading = true;
      await loadPlanFile(row.id, row.fileName);
      this.loadFileLoading = false;
    },
    downfiles(datas, filename) {
      var data = new Blob([datas]);
      console.log(data);
      var downloadUrl = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.href = downloadUrl;
      // 这里的filename 带有后缀，能决定文件的类型
      anchor.downolad = filename;
      anchor.click();
      window.URL.revokeObjectURL(data);
    },
    //点击文件触发
    handlePreview(file, fileList) {
      ///debugger
    },
    // 上传文件
    uploadFile(file) {
      console.log(file, "上传文件");
      this.fileData.append("file", file.file); // append增加数据
    },
    //form表单置空
    resetReservePlanDrawForm() {
      (this.reservePlanDrawForm = {
        planTypeId: null, //事件类型
        planName: null, //预案名称
        category: null, //预案类别
        planDescription: null, //预案描述
        strategyId: null, //多个策略ID
        strategyNames: null, //多个策略的名称，以：分割
        planFileId: null,
        tunnelId: null, //隧道
        sId: null, //分区隧道
      }),
        // this.reservePlanDrawForm.planTypeId = null;
        // this.reservePlanDrawForm.category = null;
        // this.reservePlanDrawForm.planName = null;
        // this.reservePlanDrawForm.strategyId = "";
        // this.reservePlanDrawForm.strategyNames = null;
        // this.reservePlanDrawForm.planDescription = null;
        // this.reservePlanDrawForm.tunnelId = null;
        // this.reservePlanDrawForm.sId = null;
        (this.fileList = []);
      this.removeIds = [];
      this.planChangeSink = null;
      this.multipleSelectionIds = [];
      this.eqTunnelDataList = [];
      // this.planCategory = [];
    },
    // 上传到服务器
    async submitUpload() {
      console.log(this.reservePlanDrawForm.sId, "this.reservePlanDrawForm");
      // if (!this.reservePlanDrawForm.tunnelId) {
      //   this.$modal.msgError("请选择所属隧道！");
      //   return;
      // }
      // if (!this.reservePlanDrawForm.sId) {
      //   this.$modal.msgError("请输入所属分区！");
      //   return;
      // }
      // if (!this.reservePlanDrawForm.category) {
      //   this.$modal.msgError("请输入预案类别！");
      //   return;
      // }

      // if (!this.reservePlanDrawForm.planTypeId) {
      //   this.$modal.msgError("请选择事件类型！");
      //   return;
      // }
      // if (!this.reservePlanDrawForm.planName) {
      //   this.$modal.msgError("请输入预案名称！");
      //   return;
      // }
      // if (!this.reservePlanDrawForm.planDescription) {
      //   this.$modal.msgError("请输入预案描述！");
      //   return;
      // }
      // if (this.fileList.length === 0) {
      //   this.$message({
      //     message: "请先选择文件",
      //     type: "warning",
      //   });
      // }
      // else {
      this.$refs["addform1"].validate((valid) => {
        if (valid) {
          if (this.loading) return;
          this.dloading = true;
          let currentFileList = [];
          for (var i = 0; i < this.fileList.length; i++) {
            if (!this.fileList[i].hasOwnProperty("fId")) {
              if (!this.fileList[i].size) this.fileList[i].size = 0; // 如果没有size，则给个0
              currentFileList.push(this.fileList[i]);
            }
          }
          const isLt100M = currentFileList.every(
            (file) => file.size / 1024 / 1024 < 1
          );
          if (!isLt100M) {
            this.$message.error("请检查，上传文件大小不能超过1MB!");
          } else {
            this.fileData = new FormData(); // new formData对象
            this.$refs.upload.submit(); // 提交调用uploadFile函数
            this.fileData.append("planName", this.reservePlanDrawForm.planName); // 预案名称
            this.fileData.append(
              "planTypeId",
              this.reservePlanDrawForm.planTypeId
            ); // 事件类型
            this.fileData.append("category", this.reservePlanDrawForm.category); // 预案类型
            this.fileData.append(
              "planDescription",
              this.reservePlanDrawForm.planDescription == null
                ? "#^#"
                : this.reservePlanDrawForm.planDescription
            ); // 预案描述
            // this.fileData.append('strategyId', this.reservePlanDrawForm.strategyId==null? "-1" : this.reservePlanDrawForm.strategyId);  // 策略id
            this.fileData.append("tunnelId", this.reservePlanDrawForm.tunnelId);
            this.fileData.append(
              "subareaId",
              Number(this.reservePlanDrawForm.sId)
            );
            console.log(
              this.fileData,
              "this.fileDatathis.fileDatathis.fileData"
            );
            if (this.planChangeSink == "add") {
              addPlanFile(this.fileData).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("保存成功");
                  // this.drawer = false;//关闭drawer窗体
                  this.dialogFormVisible = false;
                  this.resetReservePlanDrawForm(); //重置表单
                  //this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError("保存失败");
                }
              });
            } else if (this.planChangeSink == "edit") {
              this.fileData.append("id", this.reservePlanDrawForm.id);
              this.fileData.append("removeIds", this.removeIds);
              this.fileData.append(
                "planFileId",
                this.reservePlanDrawForm.planFileId
              );

              updatePlanFile(this.fileData).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.dialogFormVisible = false;
                  // this.resetReservePlanDrawForm(); //重置表单
                  //this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError("修改失败");
                }
              });
            }
            this.multipleSelectionIds = [];
          }
          this.dloading = false;
        }
      });
    },
    /** drawer-form表单，取消操作 **/
    drawerClose() {
      this.$nextTick(() => {
        this.$refs["form1"].clearValidate();
      });
      // this.$refs['form1'].clearValidate();
      this.resetReservePlanDrawForm();
      this.drawer = false;
      this.dloading = false;
      this.addForm = false;
    },
    /** 新增按钮操作 **/
    handleAdd() {
      this.resetReservePlanDrawForm();
      this.title = "新增预案";
      this.planChangeSink = "add";
      this.dialogFormVisible = true;

      this.visibleAdd = true;
      // this.addForm=true
      this.$nextTick(() => {
        this.$refs["form1"].clearValidate();
      });
      //this.$refs["form1"].clearValidate();
    },
    changePartitionSelection(e) {
      this.$forceUpdate();
    },
    changeSelection(e) {
      var that = this;
      this.eqTunnelData.forEach((item) => {
        if (item.tunnelId == e) {
          this.reservePlanDrawForm.sId = null;
          that.eqTunnelDataList = item.sdTunnelSubareas;
          that.$forceUpdate();
        }
      });
      this.getDicts("sd_reserve_plan_category").then((response) => {
        this.planCategory = response.data;
      });
      console.log(
        this.eqTunnelDataList,
        " this.eqTunnelDataList this.eqTunnelDataList"
      );
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row);

      // this.$nextTick(() => {
      //   this.$refs["addForm1"].clearValidate();
      // });
      this.resetForm("addForm1");

      // this.resetReservePlanDrawForm();
      this.planChangeSink = "edit";
      const id = row.id || this.ids;
      tunnelNames().then((res) => {
        this.eqTunnelData = res.rows;
        this.eqTunnelData.forEach((item) => {
          item.sdTunnelSubareas.forEach((item, index) => {
            this.eqTunnelDataList.push(item);
          });
        });
      });
      this.getDicts("sd_reserve_plan_category").then((response) => {
        this.planCategory = response.data;
      });
      getPlan(id).then((response) => {
        this.fileList = [];
        this.reservePlanDrawForm = response.data;
        this.reservePlanDrawForm.tunnelId = response.data.sdTunnels.tunnelId;
        this.reservePlanDrawForm.sId = response.data.sdTunnelSubarea.sId;
        this.reservePlanDrawForm.category = response.data.category;
        if (
          this.reservePlanDrawForm.strategyId != -1 &&
          this.reservePlanDrawForm.strategyId != "-1" &&
          this.reservePlanDrawForm.strategyId != null
        ) {
          this.multipleSelectionIds =
            this.reservePlanDrawForm.strategyId.split(";");
        }

        let fileInfo = response.data.pFileList;
        for (var i = 0; i < fileInfo.length; i++) {
          let fileModel = {};
          fileModel.name = fileInfo[i].fileName;
          fileModel.url = fileInfo[i].url;
          fileModel.fId = fileInfo[i].id;
          this.fileList.push(fileModel);
        }
        //文件回显
      });
      // this.drawer = true;
      // this.$nextTick(() => {
      //   this.$refs["form1"].resetFields();
      // });
      // this.$nextTick(() => {
      //   this.$refs["form1"].clearValidate();
      // });
      this.dialogFormVisible = true;
      this.title = "修改预案信息";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      /* debugger */
      const ids = row.id || this.ids;
      //  const rlIds = row.id || this.rlIds;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delPlan(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    //移除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
      // return this.$confirm(`确定移除 ${ file.name }？`);
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择5个文件`);
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      console.log(file, "filefile");
      console.log(fileList, "fileListfileList");
      let existFile = fileList
        .slice(0, fileList.length - 1)
        .find((f) => f.name === file.name);
      if (existFile) {
        this.$message.error("当前文件已经存在!");
        fileList.pop();
      }
      // let number = [];
      // fileList.forEach((item, index) => {
      //   if (item.name.length > 20) {
      //     number.push({
      //       zxc: index
      //     })
      //     fileList.splice(index, 1);
      //   }
      // })
      // if (number.length > 0) {
      //   this.text = '* 文件名不得超过 20 个字符  !'
      // }
      this.fileList = fileList;
    },
    /** 查询相关策略下拉列表 */
    getStrategyInfo() {
      listStrategy({ strategyType: "0" }).then((response) => {
        this.strategyData = response.rows;
      });
    },
    /** 查询事件类型下拉列表 */
    getPlanType() {
      listEventType().then((response) => {
        console.log(response, "事件类型下拉");
        this.planTypeData = response.rows;
      });
    },
    //关闭drawer
    handleClose(done) {
      // this.$confirm('确认关闭？')
      //   .then(_ => {
      //     done();
      //   })
      //   .catch(_ => {});
      // console.log(done )
      done();
      this.$refs["form1"].resetFields();
    },
    //关闭drawer
    handleFileClose(done) {
      // console.log(done,'donedone')
      done();
    },
    /** 查询预案信息列表 */
    getList() {
      this.loading = true;
      listPlan(this.queryParams).then((response) => {
        this.planList = response.rows;
        console.log(this.planList, "========");
        this.planList.forEach((item) => {
          console.log(item.strategyName);
        });
        this.total = response.total;
        this.loading = false;
      });
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
      this.ids = selection.map((item) => item.id);
      this.rlIds = selection.map((item) => item.planFileId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 点击某一行，将其选中(表格)
    handleRowClick(row, i, a) {
      this.$refs.planTable.toggleRowSelection(row);
    },
    // 点击某一行，将其选中(执行策略弹窗)
    multipleTableRowClick(row) {
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    // 点击某一行，将其选中(相关策略弹窗)
    addMultipleTableRowClick(row) {
      this.$refs.addMultipleTable.toggleRowSelection(row);
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    /** 提交按钮 */
    /* submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlan(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addPlan(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    }, */
  },
};
</script>
<style>
#cascader-menu-45-0 .el-radio {
  display: none !important;
}
</style>
<style lang="scss" scoped>
::v-deep .in-checked-path .el-radio {
  display: none;
}

::v-deep .in-checked-path .el-radio {
  display: none;
}

// .in-checked-path{
//   ::v-deep .el-radio__original{
//     display: none;
//   }
// }
.colflex {
  display: flex;
}

.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}

.workbench {
  background-image: url("../../../assets/image/lane/3duan.png");
}

.chedaoImage {
  // position: relative;
  width: 1620px;
  height: 580px;
  margin-bottom: 20px;
}

.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  /* // align-items: center; */
  width: 40px !important;
}

.el-dialog-div {
  height: 80vh;
  overflow: auto;
}

// .mouseHover {
//   &:hover {
//     z-index: 10;

//     input {
//       cursor: pointer;
//       background-color: #eee;
//       color: #000;
//     }
//   }
// }
.dialogButton {
  width: 50px;
  height: 24px;
  border: solid 1px #ccc;
  border-radius: 4px;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  margin-top: 5px;
  cursor: pointer;
  margin-left: 20px;
  // margin-right: 10px;
}

.flexWrap {
  flex-wrap: nowrap;
}
</style>
