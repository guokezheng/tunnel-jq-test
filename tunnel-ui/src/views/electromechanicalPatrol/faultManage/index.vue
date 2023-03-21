<template>
  <div class="app-container">
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
          >新增</el-button
        >
        <el-button size="small" :loading="exportLoading" @click="handleExport"
          >导出
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            placeholder="请输入故障位置、故障描述，回车搜索"
            v-model="queryParams.faultDescription"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="icon-gym-Gsearch"
              @click="device_boxShow = !device_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="searchBox" v-show="device_boxShow">
      <el-form
        ref="queryParams"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="故障类型" prop="faultType">
          <el-select
            v-model="queryParams.faultType"
            placeholder="请选择故障类型"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in faultTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr"></div>

    <el-table
      v-loading="loading"
      :data="listList"
      @selection-change="handleSelectionChange"
      height="62vh"
      class="allTable"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="故障编号" align="center" prop="id" />
      <el-table-column label="隧道id" align="center" prop="tunnelId" />-->
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>

      <el-table-column
        label="故障设备"
        align="center"
        prop="eqName"
        width="220"
      />

      <el-table-column label="故障类型" align="center" prop="faultType">
        <template slot-scope="scope">
          <span>{{ getFaultType(scope.row.faultType) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="发现时间"
        align="center"
        prop="faultFxtime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.faultFxtime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="故障位置" align="center" prop="faultLocation" />
      <el-table-column
        label="故障描述"
        align="center"
        prop="faultDescription"
        width="180"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="持续时间" align="center" prop="faultCxtime" /> -->
      <!-- <el-table-column label="设备" align="center" prop="eqName" /> -->
      <el-table-column label="设备状态" align="center" prop="eqStatus">
        <template slot-scope="scope">
          <span>{{ getEqStatus(scope.row.eqStatus) }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="故障代码" align="center" prop="faultCode" />-->
      <el-table-column label="故障等级" align="center" prop="faultLevel">
        <template slot-scope="scope">
          <span>{{ getFaultLevel(scope.row.faultLevel) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="消除状态"
        align="center"
        prop="falltRemoveStatue"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.fault_remove_statue"
            :value="scope.row.falltRemoveStatue"
          />
        </template>
      </el-table-column>
      <!--<el-table-column label="故障描述" align="center" prop="faultDescription" />-->
      <!-- <el-table-column label="状态" align="center" prop="faultStatus" >
        <template slot-scope="scope">
          <dict-tag :options="fault_status_list" :value="scope.row.faultStatus"/>
        </template>
      </el-table-column> -->

      <el-table-column
        label="操作"
        align="center"
        width="300px"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleCheckDetail(scope.row)"
            v-hasPermi="['system:list:edit']"
            >故障详情</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            :style="{ display: scope.row.faultStatus == 1 ? 'none' : '' }"
            @click="recordQuery(scope.row)"
          >
            检修记录
          </el-button>
          <!-- <el-button
            size="mini"
            class="tableBlueButtton"
            @click="exportFaultReport(scope.row)"
            :style="{ display: scope.row.faultStatus==0?'':'none' }"
          >
            检修报告
          </el-button> -->
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            :style="{ display: scope.row.faultStatus == 0 ? 'none' : '' }"
            v-hasPermi="['system:list:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
            :style="{ display: scope.row.faultStatus == 0 ? 'none' : '' }"
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

    <!-- 添加或修改故障清单对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="70%"
      append-to-body
      class="hitchDialog"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row style="display: flex; flex-wrap: wrap">
          <el-card>
            <el-col :span="24">
              <div class="topTxt">故障基本信息</div>
              <div class="tableTopHr"></div>
            </el-col>
            <el-col :span="8" :style="{ display: 'none' }">
              <el-form-item
                label="故障id"
                prop="id"
                :style="{ display: 'none' }"
              >
                <el-input
                  v-model="form.id"
                  placeholder="请输入发现源"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属隧道" prop="tunnelId">
                <el-select
                  v-model="form.tunnelId"
                  :disabled="disstate"
                  placeholder="请选择所属隧道"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in tunnelList"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="故障类型" prop="faultType">
                <el-select
                  v-model="form.faultType"
                  :disabled="disstate"
                  placeholder="请选择故障类型"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in faultTypeOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="故障发现源" prop="faultSource">
                <el-input
                  :disabled="disstate"
                  v-model="form.faultSource"
                  placeholder="请输入发现源"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发现时间" prop="faultFxtime">
                <el-date-picker
                  clearable
                  size="small"
                  :disabled="disstate"
                  v-model="form.faultFxtime"
                  type="datetime"
                  :picker-options="setDateRange"
                  @change="handle"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择发现时间"
                  style="width: 100%"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="持续时间" prop="faultCxtime">
                <el-input
                  :disabled="disstate"
                  v-model="form.faultCxtime"
                  style="width: 100%"
                  placeholder="请按照天/小时/分格式填写"
                />
              </el-form-item>
            </el-col>
<!--            <el-col :span="8">
              <el-form-item label="故障填报时间" prop="faultTbtime">
                <el-date-picker
                  clearable
                  size="small"
                  :disabled="disstate"
                  v-model="form.faultTbtime"
                  :picker-options="setDateRangeTb"
                  @change="handleTb"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择故障填报时间"
                  style="width: 100%"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>-->
          </el-card>
          <el-card>
            <el-col :span="24">
              <div class="topTxt">故障设备情况</div>
              <div class="tableTopHr"></div>
            </el-col>
            <el-col :span="8">
              <el-form-item label="设备名称" prop="eqId">
                <el-select
                  v-model="form.eqId"
                  :disabled="disstate"
                  filterable
                  placeholder="请选择设备名称"
                  @change="eqStatusGet"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in eqListData"
                    :key="item.eqId"
                    :label="item.eqName"
                    :value="item.eqId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="设备状态" prop="eqStatus">
                <el-select
                  v-model="form.eqStatus"
                  :disabled="disstate"
                  placeholder="请选择设备状态"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in eqStatusList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="故障位置" prop="faultLocation">
                <el-input
                  ref="faultLocation"
                  :disabled="disstate"
                  v-model="form.faultLocation"
                  placeholder="请输入故障位置"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="设备运行状态" prop="eqRunStatus">
                <el-input
                  v-model="form.eqRunStatus"
                  :disabled="disstate"
                  placeholder="请输入设备运行状态"
                />
              </el-form-item>
            </el-col>
          </el-card>
          <el-card>
            <el-col :span="24">
              <div class="topTxt">故障描述</div>
              <div class="tableTopHr"></div>
            </el-col>
            <el-col :span="8">
              <el-form-item label="故障代码" prop="faultCode">
                <el-input
                  v-model="form.faultCode"
                  :disabled="disstate"
                  placeholder="请输入故障代码"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="故障等级" prop="faultLevel">
                <el-select
                  v-model="form.faultLevel"
                  :disabled="disstate"
                  placeholder="请选择故障等级"
                  style="width: 100%"
                >
                  <el-option
                    v-for="dict in faultLevelOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8" v-show="removeStata">
              <el-form-item label="故障消除状态" prop="falltRemoveStatue">
                <el-select
                  v-model="form.falltRemoveStatue"
                  :disabled="disstate"
                  placeholder="请选择消除状态"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in faultRemoveStateOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8" v-show="removeStata">
              <el-form-item label="故障消除时间" prop="faultRemoveTime">
                <el-date-picker
                  clearable
                  size="small"
                  :disabled="disstate"
                  v-model="form.faultRemoveTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="暂无消除时间"
                  style="width: 100%"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="故障描述" prop="faultDescription">
                <el-input
                  v-model="form.faultDescription"
                  maxlength="250"
                  :disabled="disstate"
                  placeholder="请输入故障描述"
                  style="width: 100%"
                  type="textarea"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="现场图片">
                <el-upload
                  ref="upload"
                  action="http://xxx.xxx.xxx/personality/uploadExcel"
                  list-type="picture-card"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleRemove"
                  :http-request="uploadFile"
                  :file-list="fileList"
                  :disabled="disstate"
                  :on-exceed="handleExceed"
                  :on-change="handleChange"
                >
                  <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog
                  :visible.sync="dialogVisible"
                  class="modifyEqTypeDialog"
                  :append-to-body="true"
                  style="width: 600px !important; margin: 0 auto"
                >
                  <img width="100%" :src="dialogImageUrl" alt="" />
                </el-dialog>
              </el-form-item>
            </el-col>
          </el-card>
        </el-row>
      </el-form>
      <div class="dialogFooterButton">
        <div v-if="isWritable" @click="submitForm">仅保存</div>
        <div v-if="isWritable" @click="publishForm">保存并发布</div>
        <div @click="cancel">取 消</div>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="record" width="70%">
      <div style="text-align: center; font-size: 18px">故障检修记录</div>
      <div class="card" v-if="news.length > 0" v-for="item in news">
        <div class="card-col" style="font-size: 16px">
          <div>
            巡检时间:
            <span>{{
                parseTime(item.xcTime, "{y}-{m}-{d} {h}:{m}:{s}")
              }}</span>
          </div>
          <div>
            检修班组:
            <span>{{ item.bzName}}</span>
          </div>
          <div>
            检修人:
            <span>{{ item.userName }}</span>
          </div>
        </div>
        <div class="card-col" style="font-size: 16px">
          <div>
            外观情况:
            <span>{{ item.impression }}</span>
          </div>
          <div>
            网络情况:
            <span>{{ item.network }}</span>
          </div>
          <div>
            配电情况:
            <span>{{ item.power }}</span>
          </div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          <div>
            设备运行状态:
            <span style="margin: 6%">设备状态:{{ item.eqStatus }}</span
            ><span> 设备运行状态:{{ item.runStatus }}</span>
          </div>
          <div class="col-test">(检修时检测情况)</div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          <div>
            现场故障情况:
            <span>{{ item.eqFaultDescription }}</span>
          </div>
          <div class="col-test">(检修时检测情况)</div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          现场图片:
          <div v-for="pic in item.iFileList">
            <img :src="pic.imgUrl" :title="pic.imgName" />
          </div>
        </div>
      </div>
      <div v-if="news.length == 0">
        <div style="text-align: center; margin-top: 50px; margin-bottom: 50px">
          暂无记录
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
  getEquipmentInfo,
  getRepairRecordList,
  exportFaultList,
} from "@/api/electromechanicalPatrol/faultManage/fault";
import { listTunnels } from "@/api/equipment/tunnel/api";
import {
  addType,
  listType,
  loadPicture,
  updateType,
} from "@/api/equipment/type/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { editForm } from "@/api/equipment/yingJiGou/emergencyVehicles";
import { listBz } from "@/api/electromechanicalPatrol/taskManage/task";
import { download } from "@/utils/request";
export default {
  name: "List",
  //字典值：故障类型、故障等级，故障消除状态
  dicts: [
    "fault_type",
    "fault_level",
    "fault_remove_statue",
    "sd_monitor_state",
    "fault_status",
    "impression",
    "network",
    "power",
  ],
  data() {
    return {
      removeStata: false,
      device_boxShow: false,

      // fault_status_list:[],
      faultRemoveStateOptions: [],
      eqStatusList: [],
      faultLevelOptions: [],
      faultTypeOptions: [],
      //检修记录弹出窗
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
      // 故障清单表格数据
      listList: [],
      // 弹出层是否可写
      isWritable: true,
      // 是否不可点击
      disstate: false,
      // 弹出层标题
      //巡查班组
      bzData: {},
      title: "",
      // 是否显示弹出层
      open: false,
      //故障类型
      faultTypeData: {},
      //所属隧道
      tunnelList: {},
      //设备类型
      eqTypeData: {},
      //设备
      eqListData: {},
      // 查询参数
      news: {
        xcTime: "",
        bzId: "",
        walkerId: "",
        impression: "",
        network: "",
        power: "",
        eqStatus: "",
        runStatus: "",
        eqFaultDescription: "",
      },
      pics: {
        imgUrl: "",
        imgName: "",
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: null,
        eqRunStatus: null,
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: null,
      },
      // 表单参数
      form: {},
      setDateRange: {
        disabledDate: time => {
          // 禁用今天之后的日期【当前天可选】
          return time.getTime() > Date.now();
        }
      },
      setDateRangeTb: {
        disabledDate: time => {
          // 禁用今天之后的日期【当前天可选】
            return (time.getTime() < new Date(this.form.faultFxtime).getTime()
              ||time.getTime()>Date.now());
        }
      },
      // 表单校验
      rules: {
        faultLevel: [
          { required: true, message: "请选择故障等级", trigger: "blur" },
        ],
        faultLocation: [
          {
            required: true,
            message: "请填写故障位置",
            trigger: "blur",
          },
        ],
        faultType: [
          {
            required: true,
            message: "请选中故障类型",
            trigger: "blur",
          },
        ],
        faultFxtime: [
          { required: true, message: "请选择发现时间", trigger: "blur" },
        ],
       /* faultCxtime: [
          { required: true, message: "请填写持续时间", trigger: "blur" },
        ],*/
        faultDescription: [
          { required: true, message: "请填写故障描述", trigger: "blur" },
        ],
        eqId: [{ required: true, message: "请选择设备", trigger: "blur" }],
        eqStatus: [
          {
            required: true,
            message: "请选中设备状态",
            trigger: "blur",
          },
        ],

        tunnelId: [
          {
            required: true,
            message: "请选择所属隧道",
            trigger: "blur",
          },
        ],
      },
      direction: "",
      dialogImageUrl: "",
      dialogVisible: false,
      fileData: "", // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      //需要移除的文件ids
      removeIds: [],
      //检修记录故障id
      faultId: "",
      //设备状态
      directionList: [],
      impressionOptions: [], //外观情况
      networkOptions: [], //网络情况
      powerOptions: [], //配电情况
    };
  },
  created() {
    this.getBz();
    this.getList();
    this.getTunnel();
    this.getEqType();
    this.getDevices();
    this.fileData = new FormData(); // new formData对象
    //设备状态
    this.getDicts("sd_monitor_state").then((data) => {
      this.eqStatusList = data.data;
    });

    //故障类型
    this.getDicts("fault_type").then((response) => {
      this.faultTypeOptions = response.data;
    });
    //故障等级
    this.getDicts("fault_level").then((response) => {
      this.faultLevelOptions = response.data;
    });
    //故障消除状态
    this.getDicts("fault_remove_statue").then((response) => {
      this.faultRemoveStateOptions = response.data;
    });
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
  },
  mounted() {
    this.$nextTick(() => {
      document.addEventListener("click", this.bodyCloseMenus);
    });
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.device_boxShow == true) {
          self.device_boxShow = false;
        }
      }
    },
    //handle实现插件能选取当前时间的时、分、秒，但是选择完毕之后，只要选择的时、分、秒小于当前时间，会自动填充为当前的时、分、秒
    handle: function() {
      var startAt = new Date(this.form.faultFxtime) * 1000 /1000;
      if(startAt > Date.now()) {
        this.form.faultFxtime = new Date();
      }
    },
    handleTb: function() {
      debugger
      var startAt = new Date(this.form.faultTbtime) * 1000 /1000;
      if(startAt > this.form.faultFxtime) {
        this.form.faultTbtime = new Date(this.form.faultFxtime);
      }
    },

    getFalltRemoveStatue(num) {
      for (let item of this.faultRemoveStateOptions) {
        if (num == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    getFaultLevel(num) {
      for (let item of this.faultLevelOptions) {
        if (num == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    getEqStatus(num) {
      for (let item of this.eqStatusList) {
        if (num == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    getFaultType(num) {
      for (let item of this.faultTypeOptions) {
        if (num == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    eqStatusGet(e) {
      getEquipmentInfo({ eqId: e }).then((response) => {
        this.form.faultLocation = "";
        this.form.eqRunStatus = "";
        this.form.eqStatus = "";
        if (response.data.length != 0) {
          this.form.faultLocation = response.data[0].pile;
          this.form.eqRunStatus = response.data[0].runStatus;
          this.form.eqStatus = response.data[0].eqStatus;
          //this.$refs(this.form, "eqStatus", 1);
        }
        // this.$modal.msgSuccess("修改成功");
        // this.open = false;
        // this.getList();
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
        tunnelId: null,
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        eqTunnelId: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: null,
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: 0,
      };
      this.fileList = [];
      this.removeIds = [];
      this.resetForm("form");
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //删除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    async planRoadmapUrl(iFileList) {
      var that = this;
      that.fileList = [];
      if (iFileList) {
        for (let i = 0; i < iFileList.length; i++) {
          let iconName = iFileList[i].imgName;
          // let iconUrl = await that.picture(iFileList[i].url);
          let iconUrl = iFileList[i].imgUrl;
          that.fileList.push({
            name: iconName,
            url: iconUrl,
            fId: iFileList[i].businessId,
          });
        }
      }
    },
    /* 请求图片base64地址*/
    picture(fileUrl) {
      return new Promise((resolve, reject) => {
        loadPicture({
          url: fileUrl,
        }).then((response) => {
          if (response.code == 200) {
            let base64 = response.msg;
            resolve(base64); //不可缺少
          }
        });
      });
      return resolve(base64);
    },
    openImg(url) {
      this.img = url;
      this.yn = !this.yn;
    },
    /** 查询故障清单列表 */
    getList() {
      this.loading = true;
      if (this.$cache.local.get("manageStation") == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      listList(this.queryParams).then((response) => {
        this.listList = response.rows;
        this.listList.forEach((item) => {
          if (item.faultLocation == "null") {
            item.faultLocation = "";
          }
          if (item.faultCxtime == "null") {
            item.faultCxtime = "";
          }
          if (item.faultCode == "null") {
            item.faultCode = "";
          }
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 巡查班组 */
    getBz() {
      // const response = listBz()
      listBz().then((response) => {
        this.bzData = response.rows;
      });
    },
    /** 所属隧道 */
    getTunnel() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      // const response = listTunnels()
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.tunnelList = response.rows;
      });
    },

    /** 设备类型 */
    getEqType() {
      // const response = listType()
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 设备 */
    getDevices() {
      // const response = listDevices('sdkz')
      listDevices({
        fEqId: this.form.codePlcId,
        eqType: this.form.deviceTypeId,
      }).then((response) => {
        this.eqListData = response.rows;
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
      this.queryParams.faultType = "";
      this.queryParams.faultDescription = "";
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
      this.removeStata = false;
      this.isWritable = true;
      this.disstate = false;
      this.open = true;
      this.title = "添加故障清单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      let that = this;
      this.isWritable = true;
      this.activeName = "2";
      this.getTunnel();
      that.reset();
      const id = row.id || that.ids;
      getList(id).then((response) => {
        this.form = response.data;
        if (this.form.faultSource == "null"||this.form.faultSource == "undefined") {
          this.form.faultSource = "";
        }
        if (this.form.eqRunStatus == "undefined"||this.form.eqRunStatus == "null") {
          this.form.eqRunStatus = "";
        }
        if (this.form.faultCode == "null"||this.form.faultCode == "undefined") {
          this.form.faultCode = "";
        }
        if (this.form.faultDescription == "null"||this.form.faultDescription == "undefined") {
          this.form.faultDescription = "";
        }
        if (this.form.faultCxtime == "null"||this.form.faultCxtime=="undefined") {
          this.form.faultCxtime = "";
        }
        if (this.form.faultLevel == "null"||this.form.faultLevel == "undefined") {
          this.form.faultLevel = "";
        }
        if (this.form.falltRemoveStatue == "null"||this.form.falltRemoveStatue == "undefined") {
          this.form.falltRemoveStatue = "";
        }
        if (this.form.faultLocation == "null"||this.form.faultLocation == "undefined") {
          this.form.faultLocation = "";
        }

        that.planRoadmapUrl(that.form.iFileList);
        this.disstate = false;

        this.open = true;
        this.title = "修改故障清单";
      });
    },
    exportFaultReport(row) {
      let time = parseInt(new Date().getTime() / 1000) + "";
      let fileName = "检修报告" + time;
      download(
        "/fault/list/exportFaultReport",
        { faultId: row.id },
        fileName + ".docx"
      );
    },
    handleCheckDetail(row) {
      var that = this;
      this.removeStata = true;
      this.isWritable = false;
      that.reset();
      const id = row.id || that.ids;
      // const response = getList()
      // console.log(response,"-------------------------------------")
      getList(id).then((response) => {
        this.form = response.data;
        if (this.form.faultSource == "null"||this.form.faultSource == "undefined") {
          this.form.faultSource = "";
        }
        if (this.form.eqRunStatus == "undefined"||this.form.eqRunStatus == "null") {
          this.form.eqRunStatus = "";
        }
        if (this.form.faultCode == "null"||this.form.faultCode == "undefined") {
          this.form.faultCode = "";
        }
        if (this.form.faultDescription == "null"||this.form.faultDescription == "undefined") {
          this.form.faultDescription = "";
        }
        if (this.form.faultCxtime == "undefined"||this.form.faultCxtime == "null") {
          this.form.faultCxtime = "";
        }
        if (this.form.faultLevel == "undefined"||this.form.faultLevel == "null") {
          this.form.faultLevel = "";
        }
        if (this.form.falltRemoveStatue == "undefined"||this.form.falltRemoveStatue == "null") {
          this.form.falltRemoveStatue = "";
        }
        that.planRoadmapUrl(that.form.iFileList);
        this.disstate = true;
        this.open = true;
        this.title = "故障详情";
      });
    },

    // 检修记录按钮操作
    recordQuery(row) {
      this.record = true;
      this.faultId = row.id;
      let that = this;
      getRepairRecordList(this.faultId).then((response) => {
        that.news = response.data;
        if(that.news.length>0){
          for(let i=0;i<that.news.length;i++){
            if(that.news[i].hasOwnProperty("impression")){
              that.impressionOptions.forEach((opt) => {
                if (opt.dictValue == that.news[i].impression) {
                  that.news[i].impression = opt.dictLabel;
                }
              });
            }
          }
          for(let i=0;i<that.news.length;i++){
            if(that.news[i].hasOwnProperty("network")){
              that.networkOptions.forEach((opt) => {
                if (opt.dictValue == that.news[i].network) {
                  that.news[i].network = opt.dictLabel;
                }
              });
            }
          }
          for(let i=0;i<that.news.length;i++){
            if(that.news[i].hasOwnProperty("power")){
              that.powerOptions.forEach((opt) => {
                if (opt.dictValue == that.news[i].power) {
                  that.news[i].power = opt.dictLabel;
                }
              });
            }
          }
        }
      });
    },
    // 关闭弹窗
    close() {
      // 关闭弹出层
      this.record = false;
    },
    /** 提交按钮 */
    submitForm() {
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("id", this.form.id);
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("faultType", this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("eqId", this.form.eqId);
      this.fileData.append("eqStatus", this.form.eqStatus);
      this.fileData.append("faultLocation", this.form.faultLocation);
      this.fileData.append("eqRunStatus", this.form.eqRunStatus);
      this.fileData.append("faultCode", this.form.faultCode);
      this.fileData.append("faultLevel", this.form.faultLevel);
      this.fileData.append("falltRemoveStatue", this.form.falltRemoveStatue);
      this.fileData.append("faultDescription", this.form.faultDescription);
      this.fileData.append("faultStatus", 1);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            this.fileData.append("removeIds", this.removeIds);
            if (this.isClick) {
              updateList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            }
          } else {
            if (this.isClick) {
              addList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        }
      });
      setTimeout(() => {
        this.isClick = true;
      }, 500);
    },

    publishForm() {
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("id", this.form.id);
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("faultType", this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("eqId", this.form.eqId);
      this.fileData.append("eqStatus", this.form.eqStatus);
      this.fileData.append("faultLocation", this.form.faultLocation);
      this.fileData.append("eqRunStatus", this.form.eqRunStatus);
      this.fileData.append("faultCode", this.form.faultCode);
      this.fileData.append("faultLevel", this.form.faultLevel);
      this.fileData.append("falltRemoveStatue", this.form.falltRemoveStatue);
      this.fileData.append("faultDescription", this.form.faultDescription);
      this.fileData.append("faultStatus", 0);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            this.fileData.append("removeIds", this.removeIds);
            updateList(this.fileData).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.fileData).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
      setTimeout(() => {
        this.isClick = true;
      }, 500);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除选中的数据项？')
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
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出设备故障数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportFaultList(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.getTunnel();
    },
  },
};
</script>


<style lang="scss" scoped>
.topTxt {
  font-size: 18px;
  font-weight: 500;
  height: 40px;
  margin-top: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddcccc;
}

.card {
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
    div {
      width: 50%;
    }
    .col-test {
      text-align: right;
      color: #79949c;
    }
    img {
      width: 100px;
      margin-left: 20px;
    }
  }

  .icon {
    position: absolute;
    top: 0;
    right: 30px;
    background-image: url(../../../assets/icons/svg/u954.svg);
    background-size: 100%;
  }
}

/*.trow{
  width:30%;
  font-size: 15px;
}*/

.hitchDialog {
  ::v-deep .el-dialog__body {
    height: 70vh !important;
    overflow: auto !important;
  }
  ::v-deep .el-card {
    margin-bottom: 10px !important;
  }
}
.topTxt {
  margin-left: 7px;
  margin-top: 10px;
  font-size: 16px;
  background-image: url(../../../assets/cloudControl/cardTitle.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  text-align: center;
  width: 139px;
  height: 30px;
  line-height: 30px;
}
.dialogFooterButton {
  width: 100%;
  height: 30px;
  display: flex;
  justify-content: right;
  div {
    margin-right: 20px;
    width: 80px;
    height: 28px;
    border-radius: 14px;
    text-align: center;
    line-height: 28px;
    color: white;
    cursor: pointer;
  }
  div:nth-of-type(1) {
    background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
  }
  div:nth-of-type(2) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
  div:nth-of-type(3) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
}
</style>
