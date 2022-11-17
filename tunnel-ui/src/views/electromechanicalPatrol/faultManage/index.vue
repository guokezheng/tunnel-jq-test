<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >

      <!--      <el-form-item label="故障位置" prop="faultLocation">
              <el-input
                v-model="queryParams.faultLocation"
                placeholder="请输入故障位置"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>-->
      <el-form-item label="故障类型" prop="faultType">
        <el-select
          v-model="queryParams.faultType"
          placeholder="请选择故障类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.fault_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="故障描述" prop="faultDescription">
              <el-input
                v-model="queryParams.faultDescription"
                placeholder="请输入故障描述"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>-->

      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery"
        >搜索</el-button
        >
        <el-button type="primary" plain size="mini" @click="resetQuery"
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
        <!--        <el-button
                  type="primary"
                  plain

                  size="mini"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['system:list:edit']"
                  >修改</el-button
                >
                <el-button
                  type="primary"
                  plain
                  size="mini"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['system:list:remove']"
                  >删除</el-button
                >-->
        <!--        <el-button
                  type="primary"
                  plain
                  size="mini"
                  :loading="exportLoading"
                  @click="handleExport"
                  v-hasPermi="['system:list:export']"
                  >导出</el-button
                >-->
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="listList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="故障编号" align="center" prop="id" />
      <el-table-column label="隧道id" align="center" prop="tunnelId" />-->
      <el-table-column label="故障位置" align="center" prop="faultLocation" />
      <el-table-column label="故障类型" align="center" prop="faultType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fault_type" :value="scope.row.faultType"/>
        </template>
      </el-table-column>
      <el-table-column
        label="发现时间"
        align="center"
        prop="faultFxtime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.faultFxtime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="持续时间" align="center" prop="faultCxtime" />
      <el-table-column label="设备" align="center" prop="eqName" />
      <el-table-column label="设备状态" align="center" prop="eqStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_monitor_state" :value="scope.row.eqStatus"/>
        </template>
      </el-table-column>
      <!--<el-table-column label="故障代码" align="center" prop="faultCode" />-->
      <el-table-column label="故障等级" align="center" prop="faultLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fault_level" :value="scope.row.faultLevel"/>
        </template>
      </el-table-column>
      <el-table-column
        label="消除状态"
        align="center"
        prop="falltRemoveStatue"
      >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fault_remove_statue" :value="scope.row.falltRemoveStatue"/>
        </template>
      </el-table-column>
      <!--<el-table-column label="故障描述" align="center" prop="faultDescription" />-->
      <el-table-column label="状态" align="center" prop="faultStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fault_status" :value="scope.row.faultStatus"/>
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
            @click="recordQuery(scope.row)"
          >
            检修记录
          </el-button>
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            :style="{ display: scope.row.faultStatus==0?'none':'' }"
            v-hasPermi="['system:list:edit']"
          >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
            :style="{ display: scope.row.faultStatus==0?'none':'' }"
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
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <div class="topTxt">
              故障基本信息
            </div>
          </el-col>
          <el-col :span="8" :style="{ display: 'none'}">
            <el-form-item label="故障id" prop="id" :style="{ display: 'none'}">
              <el-input
                v-model="form.id"
                placeholder="请输入发现源"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所在路段隧道" prop="tunnelId">
              <el-select v-model="form.eqTunnelId" placeholder="请选择所属隧道">
                <el-option
                  v-for="item in eqTunnelData"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="故障类型" prop="faultType">
              <el-select v-model="form.faultType" placeholder="请选择所属隧道">
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
                v-model="form.faultSource"
                placeholder="请输入发现源"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障发现时间" prop="faultFxtime">
              <el-date-picker
                clearable
                size="small"
                v-model="form.faultFxtime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择故障发现时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障持续时间" prop="faultCxtime">
              <el-input
                v-model="form.faultCxtime"
                placeholder="请输入故障持续时间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="故障填报时间" prop="faultTbtime">
              <el-date-picker
                clearable
                size="small"
                v-model="form.faultTbtime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择故障填报时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <div class="topTxt">
              故障设备情况
            </div>
          </el-col>
          <el-col :span="8">
            <el-form-item label="设备名称" prop="eqId">
              <el-select v-model="form.codeDeviceId" placeholder="请选择设备名称" @change="eqStatusGet">
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
            <el-form-item label="设备填报状态" prop="eqStatus">
              <el-select v-model="form.eqStatus" placeholder="请选择设备填报状态">
                <el-option
                  v-for="item in directionList"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障位置" prop="faultLocation">
              <el-input ref="faultLocation"
                        v-model="form.faultLocation"
                        placeholder="请输入故障位置"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="设备运行状态" prop="eqRunStatus">
              <el-input
                v-model="form.eqRunStatus"
                placeholder=""
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <div class="topTxt">
              故障描述
            </div>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障代码" prop="faultCode">
              <el-input
                v-model="form.faultCode"
                placeholder="请输入故障代码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障等级" prop="faultLevel">
              <el-select v-model="form.faultLevel" placeholder="请选择故障等级">
                <el-option
                  v-for="dict in dict.type.fault_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
<!--                <el-option
                  v-for="item in faultLevelOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />-->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障消除状态" prop="falltRemoveStatue">
              <el-select v-model="form.falltRemoveStatue" placeholder="请选择故障等级">
                <el-option
                  v-for="item in faultRemoveStateOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障描述" prop="faultDescription">
              <el-input
                v-model="form.faultDescription"
                maxlength="250"
                placeholder="请输入故障描述"
                style="width: 80%"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="默认图标" label-width="100px">
              <el-upload
                ref="upload"
                action="http://xxx.xxx.xxx/personality/uploadExcel"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :http-request="uploadFile"
                :file-list="fileList"
                :on-exceed="handleExceed"
                :on-change="handleChange"
                :limit="2"
                :class="fileList.length >=2 ? 'showUpload':''"
              >
                <i class="el-icon-plus"></i>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible"  class="modifyEqTypeDialog"
                         :append-to-body="true" style="width:600px !important;margin: 0 auto;"
              >
                <img width="100%" :src="dialogImageUrl" alt="" />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">仅保存</el-button>
        <el-button type="primary" @click="publishForm">保存并发布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :visible.sync="record"
      width="70%" id="jxjlInfo">
      <div style="text-align: center;">故障检修记录</div>
      <div class="card" id="jxjlCard"  v-for="item in news" >
        <div class="card-col" id ="firstRow">
          <div>
            巡检时间:
            <span>{{item.xcTime}}</span>
          </div>
          <div>
            检修班组:
            <span>{{item.bzId}}</span>
          </div>
          <div>
            检修人:
            <span>{{ item.walkerId }}</span>
          </div>
        </div>
        <div class="card-col" id ="secondRow">
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
            <span>{{item.power}}</span>
          </div>
        </div>
        <div class="card-cols">
          <div>
            设备运行状态:
            <span>设备状态:{{item.eqStatus}} 设备运行状态:{{item.runStatus}}</span>
          </div>
          <div class="col-test">
            (抢修时检测情况)
          </div>
        </div>
        <div class="card-cols">
          <div>
            现场故障情况:
            <span>{{item.eqFaultDescription}}</span>
          </div>
          <div class="col-test">
            (抢修时检测情况)
          </div>
        </div>
        <div class="card-cols">
          现场图片:
          <div  v-for="pic in item.iFileList">
            <img :src="pic.imgUrl" :title="pic.imgName">
          </div>
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
  getEquipmentInfo,
  getRepairRecordList,
} from "@/api/electromechanicalPatrol/faultManage/fault";
import { listTunnels } from "@/api/equipment/tunnel/api";
import {addType, listType, updateType} from "@/api/equipment/type/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import {editForm} from "@/api/equipment/yingJiGou/emergencyVehicles";

export default {
  name: "List",
  //字典值：故障类型、故障等级，故障消除状态
  dicts: ["fault_type", "fault_level", "fault_remove_statue","sd_monitor_state","fault_status"],
  data() {
    return {
      //检修记录弹出窗
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
      // 故障清单表格数据
      listList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //故障类型
      faultTypeData: {},
      //所属隧道
      eqTunnelData: {},
      //设备类型
      eqTypeData: {},
      //设备
      eqListData: {},
      // 查询参数
      news:{
        xcTime:"",
        bzId:"",
        walkerId:"",
        impression:"",
        network:"",
        power:"",
        eqStatus:"",
        runStatus:"",
        eqFaultDescription:"",
      },
      pics:{
        imgUrl:"",
        imgName:""
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
      // 表单校验
      rules: {
        faultLevel: [
          { required: true, message: '请选择', trigger: 'faultLevel' }
        ]
      },
      direction: "",
      dialogImageUrl: "",
      dialogVisible: false,
      fileData: "", // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      //需要移除的文件ids
      removeIds: [],
      //检修记录故障id
      faultId:"",
      //设备填报状态
      directionList:[],
      faultTypeOptions:[],//故障类型
      faultLevelOptions:[],//故障等级
      faultRemoveStateOptions:[],//故障消除状态
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getEqType();
    this.getDevices();
    this.fileData = new FormData(); // new formData对象
    //设备填报状态
    this.getDicts("sd_monitor_state").then((data) => {
      this.directionList = data.data;
    });
    //故障类型
    this.getDicts("fault_type").then(response => {
      this.faultTypeOptions = response.data;
    });
    //故障等级
    this.getDicts("fault_level").then(response => {
      this.faultLevelOptions = response.data;
    });
    //故障消除状态
    this.getDicts("fault_remove_statue").then(response => {
      this.faultRemoveStateOptions = response.data;
    });
  },
  methods: {
    eqStatusGet(e){
      getEquipmentInfo({eqId:e}).then((response) => {
        console.log(response)
        this.$refs.faultLocation.value = response.data[0].pile;
        // this.$modal.msgSuccess("修改成功");
        // this.open = false;
        // this.getList();
      });
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
      for (let i = 0; i < iFileList.length; i++) {
        let iconName = iFileList[i].stateIconName;
        // let iconUrl = await that.picture(iFileList[i].url);
        let iconUrl = iFileList[i].url

        that.fileList.push({
          name: iconName,
          url: iconUrl,
          fId: iFileList[i].id,
        });
        // fileList.push(this.form.iFileList[i].url)
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


    /** 查询故障清单列表 */
    getList() {
      this.loading = true;
      listList(this.queryParams).then((response) => {
        this.listList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.eqTunnelData = response.rows;
      });
    },

    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 设备 */
    getDevices() {
      listDevices({
        fEqId: this.form.codePlcId,
        eqType: this.form.deviceTypeId,
      }).then((response) => {
        this.eqListData = response.rows;
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
        eqTunnelId:null,
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
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加故障清单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      var that = this;
      that.reset();
      const id = row.id|| that.ids;
      getList(id).then((response) => {
        this.form = response.data;
        this.planRoadmapUrl(this.form.iFileList);
        this.open = true;
        this.title = "修改故障清单";
      });
    },

    // 检修记录按钮操作
    recordQuery(row) {
      this.record = true;
      this.faultId = row.id;
      getRepairRecordList(this.faultId).then((response) => {
        this.news = response.data;
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
      this.fileData.append("tunnelId", this.form.eqTunnelId);
      this.fileData.append("faultType",this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("faultTbtime", this.form.faultTbtime);
      this.fileData.append("eqId", this.form.codeDeviceId);
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
          if(this.fileList.length <= 0) {
            return this.$modal.msgWarning('请选择要上传的图片')
          }
          if (this.form.id != null) {
            /* this.fileData.append("typeId", this.form.typeId); //类型id*/
            /* this.fileData.append("iconFileId", this.form.iconFileId); //关联文件id*/
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
      /*this.$refs["form"].validate((valid) => {
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
      });*/
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    publishForm() {
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("tunnelId", this.form.eqTunnelId);
      this.fileData.append("faultType",this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("faultTbtime", this.form.faultTbtime);
      this.fileData.append("eqId", this.form.codeDeviceId);
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
          if(this.fileList.length <= 0) {
            return this.$modal.msgWarning('请选择要上传的图片')
          }
          if (this.form.typeId != null) {
            /* this.fileData.append("typeId", this.form.typeId); //类型id*/
            /* this.fileData.append("iconFileId", this.form.iconFileId); //关联文件id*/
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
      /*this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateList(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then((response) => {
              this.$modal.msgSuccess("发布成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });*/
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除故障清单编号为"' + ids + '"的数据项？')
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
        .confirm("是否确认导出所有故障清单数据项？")
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
  },
};

</script>


<style lang="scss" scoped>
.topTxt{
  font-size: 18px;
  font-weight: 500;
  height: 40px;
  margin-top: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddcccc;
}
::v-deep .el-dialog__body{
  .el-input{
    width: 218px;
  }
}

.card{
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
    div{
      width: 50%;
    }
    .col-test{
      text-align: right;
      color: #79949c;
    }
    img{
      width:100px;
      margin-left: 20px;
    }
  }

  .icon{
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
</style>
