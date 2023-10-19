<template>
  <el-dialog class="explain-table  offlinclass" custom-class="no-scroll" title="离线设备" :close-on-click-modal="false"
             :visible.sync="visibleSync" width="80%" destroy-on-close append-to-body :lock-scroll="true"
             :before-close="closeLogin">
    <div >
      <!-- 全局搜索 -->
      <el-row :gutter="24" class="topFormRow" >
        <el-col :span="18">
        </el-col>
        <el-col style="margin-left: 75%;" :span="6" :offset="10">
          <div ref="main" class="grid-content bg-purple">
            <el-input
              v-model="queryParams.searchValue"
              placeholder="请输入设备名称、桩号,回车搜索"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            >
              <el-button
                slot="append"
                class="searchTable"
                @click="boxShow = !boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div ref="cc" class="searchBox" style="margin-top:3.7%" v-show="boxShow">
        <el-form
          ref="queryForm"
          :inline="true"
          :model="queryParams"
          label-width="75px"
        >
          <el-form-item label="设备方向">
            <el-checkbox-group v-model="checkeBox" @change="handleCheckChange">
              <el-checkbox
                v-for="item in dict.type.sd_direction"
                :key="item.value"
                :label="item.value"
                name="check_direction"
              >{{ item.label }}
              </el-checkbox>
              <el-checkbox name="check_direction" key="3" label="3"
              >双向
                <!--            v-for="item in dict.type.sd_direction"-->
                <!--            :key=""-->
                <!--            :label=""-->
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item
            label="所属隧道"
            prop="eqTunnelId"
            v-show="manageStatin == '0'"
          >
            <el-select
              v-model="queryParams.eqTunnelId"
              placeholder="请选择所属隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备类型" prop="eqType">
            <!-- <el-select
              v-model="queryParams.eqType"
              placeholder="请选择设备类型"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select> -->
            <el-cascader
              ref="cc1"
              v-model="queryParams.eqType"
              :options="eqTypeData"
              :props="equipmentTypeProps"
              :show-all-levels="false"
              clearable
              placeholder="请选择设备类型"
              @change="changeEquipmentType"
              style="width: 100%"
              @visible-change="elCascaderOnClick"
              :key="refresh"
              popper-class="cascaderClass"
              :append-to-body="false"
            ></el-cascader>
          </el-form-item>
<!--          <el-form-item label="设备状态" prop="eqStatus">-->
<!--            <el-select-->
<!--              v-model="queryParams.eqStatus"-->
<!--              placeholder="请选择设备状态"-->
<!--              clearable-->
<!--              size="small"-->
<!--            >-->
<!--              <el-option-->
<!--                v-for="item in eqStatusList"-->
<!--                :key="item.dictValue"-->
<!--                :label="item.dictLabel"-->
<!--                :value="item.dictValue"-->
<!--                @change="$forceUpdate()"-->
<!--              />-->
<!--            </el-select>-->
<!--          </el-form-item>-->
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
        :data="devicesList"
        height="45vh"
        :row-key="getRowKey"
        ref="tableFile"
      >

        <el-table-column
          type="index"
          :index="indexMethod"
          label="序号"
          width="55"
          align="center"
        ></el-table-column>

        <el-table-column
          label="所属隧道"
          align="center"
          prop="tunnelName.tunnelName"
          min-width="90"
          show-overflow-tooltip
        />
        <el-table-column
          label="设备名称"
          align="center"
          prop="eqName"
          width="150"
          show-overflow-tooltip
        />
        <el-table-column
          label="设备类型"
          align="center"
          prop="typeName.typeName"
          width="130"
          show-overflow-tooltip
        />

        <el-table-column
          label="设备方向"
          align="center"
          prop="eqDirection"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.sd_direction"
              :value="scope.row.eqDirection"
            />
          </template>
        </el-table-column>

        <el-table-column label="设备IP" align="center" prop="ip" width="120" />
        <el-table-column
          label="设备状态"
          align="center"
          prop="eqStatus"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.eqStatus == '1'">在线</span>
            <span v-else-if="scope.row.eqStatus == '2' || !scope.row.eqStatus"
            >离线</span
            >
            <span v-else-if="scope.row.eqStatus == '3'">故障</span>
            <span v-else>报警</span>
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


    </div>
  </el-dialog>
</template>

<script>

import {
  getDevBrandList,
  listDevices,
  getDevices,
  delDevices,
  addDevices,
  createDmcontrolSeat,
  createInstruction,
  updateDevices,
  checkInstruction,
  exportDevices,
  exportDevicesTemplate,
  autoId,
} from "@/api/equipment/eqlist/api";
import { listHosts } from "@/api/equipment/plc/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listType } from "@/api/equipment/type/api";
import {
  listDevcmd,
  getDevcmd,
  delDevcmd,
  addDevcmd,
  updateDevcmd,
} from "@/api/equipment/deviceCmd/api";
import {
  getEqTypeStateByType,
  listEqTypeState,
} from "@/api/equipment/eqTypeState/api";
import { getToken } from "@/utils/auth";
import { listAllSystem } from "@/api/equipment/externalsystem/system";
import { listCategory } from "@/api/equipment/bigType/category";
import { treeSelectYG1 } from "@/api/system/dept";
import { getTeams } from "@/api/electromechanicalPatrol/teamsManage/teams";
import { getCategoryAllTree } from "@/api/event/strategy";
export default {
  name: "offlineDeviceModal",
  //字典值：设备方向，设备品牌，所属车道,使用状态，是否监控，诱导灯控制状态
  dicts: [
    "sd_direction",
    "brand",
    "sd_lane",
    "sd_use_status",
    "sd_is_monitor",
    "inductionlamp_control_type",
  ],
  data() {
    const validatePass = (rule, value, callback) => {
      console.log(rule, value, callback, "rule, value, callback");
      if (this.option) {
        callback(new Error("请选择列表中已有的选项"));
      } else {
        callback();
      }
    };
    return {
      visibleSync: false,
      refresh:0,
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      checkIndex: 1,
      boxShow: false,
      //设备大类
      eqBigTypeList: {},
      //不能选择当前日期
      optionsDisable: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      //巡检状态
      showOrhide: false,
      // plc主机
      showPlc: true,
      //设备品牌
      brandList: [],
      //提交模式 0是修改（隐藏）  1是新增（显示）（控制设备ID字段是否展示）
      submitMode: 1,
      currentDate: new Date(),
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
      // 设备表格数据
      devicesList: [],
      // 控制指令数据
      ctrlCommandList: [],
      // 设备状态数据
      ctrlStateList: [],
      // 弹出层标题
      title: "",
      title2: "修改控制指令",
      instructionTitle: "生成指令",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      instructionDialog: false,
      manageStatin: this.$cache.local.get("manageStation"),
      ctrlcmd: false,
      //plc主机
      eqHostData: {},
      //所属隧道
      eqTunnelData: {},
      // 设备类型字典
      /* eqTypeOptions: [], */
      //设备类型
      eqTypeData: [],
      // 照明灯类型字典
      eqLampTypeOptions: [],
      instructionTypeOptions: [],
      // 设备方向字典
      eqDirections: [],
      checkeBox: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fEqId: null,
        eqTunnelId: null,
        remark: "",
        eqName: null,
        eqType: null,
        deviceState: null,
        searchValue: null,
        exportIds: "",
        eqState: "2",
      },
      queryCmdParams: {
        codeDeviceId: null,
        fEqId: null,
        codePlcId: null,
        deviceTypeId: null,
        codeDeviceState: null,
        command: null,
      },
      queryStateParams: {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        isControl: 1,
      },
      // 表单参数
      form: {},
      instructionForm: {},
      // 表单校验
      rules: {
        eqTunnelId: [
          {
            required: true,
            message: "请选择所属隧道",
            trigger: "blur",
          },
        ],
        eqType: [
          {
            required: true,
            message: "请选择设备类型",
            trigger: "blur",
          },
        ],
        fEqType: [
          {
            required: true,
            message: "请选择设备大类",
            trigger: "blur",
          },
        ],
        eqName: [
          {
            required: true,
            message: "请选择设备名称",
            trigger: "blur",
          },
        ],
        eqId: [
          {
            required: true,
            message: "请填写设备ID",
            trigger: "blur",
          },
          { max: 100, message: "最长输入100个字符", trigger: "blur" },
          /*{
            pattern: /^[0-9a-zA-Z_-]{1,}$/,
            message: "请输入数字字母或1横线",
          },*/
        ],
        pileNum: [
          {
            pattern: /^[1-9]\d*$/,
            message: "只能输入整数",
          },
        ],
        // fEqId: [{
        //   required: true,
        //   message: "请选择plc主机",
        //   trigger: "change",
        // }, ],
        eqDirection: [
          {
            required: true,
            message: "请选择设备方向",
            trigger: "change",
          },
        ], //{ min: 1, max:1,message: '只允许输入1或0，1为左线0为右线' },{ pattern: /^[0-1]{1,}$/, message: '只允许输入1或0，1为左线0为右线' }
        qNumber: [
          {
            pattern: /^[0-9]*$/,
            message: "查询对应点需为数字",
            trigger: "blur",
          },
        ],
      },
      instructionFormRules: {
        instruction: [
          {
            required: true,
            message: "请选择指令模式",
            trigger: "blur",
          },
        ],
        seat: [
          {
            required: true,
            message: "机位不能为空",
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*$/,
            message: "机位需为数字",
            trigger: "blur",
          },
        ],
        qNumber: [
          {
            required: true,
            message: "对应点不能为空",
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*$/,
            message: "对应点需为数字",
            trigger: "blur",
          },
        ],
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken(),
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/devices/importData",
      },
      // zhanshi:false,
      input: "",
      externalSystemList: [],
      eqStatusList: [],
      flag:false,
    };
  },

  created() {

    this.getList();
    this.getTunnel();
    this.getEqType();
    this.getEqBigType();
    this.getStateList();
    this.getDicts("sys_eq_light").then((response) => {
      this.eqLampTypeOptions = response.data;
    });
    this.getDicts("sys_instruction_type").then((response) => {
      this.instructionTypeOptions = response.data;
    });
    /* this.getDicts("sys_eq_state").then(response => {
          this.deviceStateOptions = response.data;
        }); */
    this.getDicts("sd_monitor_state").then((response) => {
      this.eqStatusList = response.data;
      console.log(this.eqStatusList, "设备状态");
    });
    this.getDevBrandList();
    this.getExternalSystemList();
  },

  methods: {
    closeLogin() {
      // debugger
      this.visibleSync = !this.visibleSync
    },
    // 关闭级联选择器时 把打开的二级菜单折叠
    elCascaderOnClick(f){
      if(!f){
        this.refresh++
      }
    },
    changeEquipmentType(index) {
      console.log(index,"index");
    },

    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.eqId;
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    bodyCloseMenus(e) {
      // debugger
      let self = this;
      if (self.boxShow) {
        if (
          !self.$refs.main.contains(e.target) &&
          !self.$refs.cc.contains(e.target)
        ) {
          if (self.boxShow == true) {
            self.boxShow = false;
          }
        }
      }
    },
    getEqBigType() {
      listCategory().then((response) => {
        this.eqBigTypeList = response.rows;
      });
    },
    getDevBrandList() {
      getDevBrandList().then((result) => {
        console.log("brandList:>>>", result.data);
        this.brandList = result.data;
      });
    },
    getExternalSystemList() {
      let params = {
        tunnelId: this.form.eqTunnelId,
      };
      listAllSystem(params).then((result) => {
        console.log("externalSystemList:>>>", result.data);
        this.externalSystemList = result.data;
      });
    },
    getBrand(num) {
      for (var item of this.brandList) {
        if (item.supplierId == num) {
          return item.shortName;
        }
      }
    },

    // 弹窗校验 当没选择隧道时 点击plc 提示先选隧道
    onChangePlc(eqTunnelId) {
      if (eqTunnelId == null) {
        this.$refs.form.validateField("eqTunnelId"); //单独触发校验
      }
    },
    // 选中plc主机时
    changePlc(fEqId) {
      if (fEqId == 0) {
        this.showOrhide = true;
      }
    },
    // 点击弹窗设备类型
    changeEqType(data) {
      console.log(data, "data");
      this.form.fEqId = null;
      if (data == 0) {
        this.showPlc = false;
        this.showOrhide = false;
      } else {
        this.showPlc = true;
      }
    },
    handleInput() {
      this.form.eqId = this.form.eqId.replace(/[\u4e00-\u9fa5]/g, "");
    },
    // 新增弹窗 自动生成id
    automaticGenerationID(form) {
      console.log(form, "form");
      if (!form.eqTunnelId) {
        this.$modal.msgError("请选择所属隧道");
        return;
      }
      if (!form.eqType) {
        this.$modal.msgError("请选择设备类型");
        return;
      }
      // if(!form.eqType){
      //   this.$modal.msgError("请选择设备类型");
      //   return;
      // }
      const params = {
        tunnelId: form.eqTunnelId,
        typeId: form.eqType,
      };
      autoId(params).then((response) => {
        if (response.data) {
          this.form.eqId = response.data;
        } else {
          this.$modal.msgError("未生成设备ID，请检查配置");
        }
      });
    },
    //查询设备状态
    getStateList() {
      var that = this;
      listEqTypeState(this.queryStateParams).then((response) => {
        that.ctrlStateList = response.rows;
      });
    },
    /** 查询设备列表 */
    getList() {
      if (this.manageStatin == "1") {
        this.queryParams.eqTunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      this.loading = true;
      this.boxShow = false;
      this.queryParams.exportIds = "";
      this.queryParams.eqState ='2'
      this.queryParams.eqStatus='2'
      listDevices(this.queryParams).then((response) => {
        console.log(response, "设备列表");
        this.devicesList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** plc主机 */
    getPlcs() {
      listDevices({
        eqTunnelId: this.form.eqTunnelId,
        eqType: "0",
      }).then((response) => {
        console.log(response, "response");
        this.eqHostData = response.rows;
        // this.eqHostData.unshift({
        //   eqId: 0,
        //   eqName: "未关联PLC设备",
        // });
      });
      this.form.externalSystemId = "";
      this.getExternalSystemList();
    },
    /** 所属隧道 */
    getTunnel() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.eqTunnelData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      getCategoryAllTree().then((data) => {
        this.eqTypeData = data.data;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.ctrlcmd = false;
      this.$refs.tableFile.clearSelection();
      this.submitFormLoading = false;
      this.reset();
    },
    cancelInstruction() {
      this.instructionDialog = false;
    },
    // 表单重置
    reset() {
      this.form = {
        brandId: null,
        externalSystemId: null,
        eqId: null,
        fEqId: null,
        eqTunnelId: null,
        eqName: null,
        eqDirection: null,
        stakeMark: null,
        eqType: null,
        fEqType: null,
        queryPointAddress: null,
        controlPointAddress: null,
        deviceState: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        leishiDeviceIp: null,
        lane: null,
        pile: null,
        pileName: null,
        lat: null,
        lng: null,
        ip: null,
        port: null,
        secureKey: null,
        eqUser: null,
        eqPwd: null,
        protocol: null,
        deliveryTime: null,
        warrantyEndTime: null,
        installTime: null,
        useLife: null,
        useStatus: null,
        isMonitor: null,
      };
      this.resetForm("form");
      this.queryCmdParams = {
        codeDeviceId: null,
        fEqId: null,
        codePlcId: null,
        deviceTypeId: null,
        codeDeviceState: null,
        command: null,
      };
      this.eqHostData = [];
    },
    instructionFormReset() {
      this.instructionForm = {
        instruction: null,
        fEqId: null,
        seat: null,
        qNumber: null,
      };
      this.resetForm("instructionForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.queryParams.remark.indexOf("3") != -1) {
        this.queryParams.remark = "1,2";
      }
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.$refs.tableFile.clearSelection();
      /* this.queryParams.eqDirection = 0; */
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.refresh++
      this.eqTypeData = [];
      this.getEqType();
      this.checkeBox = [];
      this.queryParams = {}
      this.queryParams.remark = "";
      this.queryParams.searchValue = "";
      this.queryParams.eqState = "";
      this.queryParams.eqType = null;
      this.queryParams.eqTunnelId = "";
      this.$cache.local.remove("settingPoint");
      this.resetForm("queryForm");
      this.handleQuery();
      this.$forceUpdate()
    },

    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的设备管理数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的设备管理数据项？";
      }
      this.queryParams.exportIds = this.ids.join();
      const queryParams = this.queryParams;
      this.$confirm(confirmInfo, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportDevices(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.$refs.tableFile.clearSelection();
          this.queryParams.exportIds = "";
        });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.submitMode = 1;
      this.title = "添加设备";
      this.form.fEqId = "";
    },
    /** 打开修改控制指令框 */
    OpenCtrlCmd(item, index) {
      this.input = "";
      var myItem = this.ctrlCommandList[index].zhanshi;
      for (let i = 0; i < this.ctrlCommandList.length; i++) {
        this.ctrlCommandList[i].zhanshi = false;
      }
      if (myItem) {
        this.ctrlCommandList[index].zhanshi = false;
      } else {
        this.ctrlCommandList[index].zhanshi = true;
        this.instructionForm.instruction =
          this.instructionTypeOptions[1].dictValue;
        this.instructionForm.fEqId = this.queryCmdParams.fEqId;
        this.instructionForm.seat = this.ctrlCommandList[index].dmcontrolSeat;
        this.instructionDialog = true;
      }
    },
    /** 确认修改 */
    updateCtrlCmd(item, index) {
      this.queryCmdParams.codeDeviceId = item.eqId;
      this.queryCmdParams.codeDeviceState = item.stateId;
      this.queryCmdParams.codePlcId = item.fEqId;
      this.queryCmdParams.deviceTypeId = item.eqType;
      this.queryCmdParams.command = this.input;
      updateDevcmd(this.queryCmdParams).then((response) => {
        this.$modal.msgSuccess("修改成功");
        listDevcmd(this.queryCmdParams).then((response) => {
          this.ctrlCommandList[index].command = response.rows[0].command;
          this.ctrlCommandList[index].zhanshi = false;
        });
      });
    },
    /** 点位设置 **/
    settingPoint(row) {
      const eqId = row.eqId || 0;
      const eqType = row.eqType || 0;
      const protocolId = row.protocolId || 0;

      this.flag = true
      this.$router.push({
        path: "/equipment/eqlist-point/index",
        query: { eqId: eqId, typeId: eqType, protocolId: protocolId },
      });
    },
    /** 修改控制指令操作 */
    updateCmd(row) {
      this.input = "";
      this.reset();
      this.instructionFormReset();
      var that = this;
      that.ctrlCommandList = [];
      that.queryCmdParams.codeDeviceId = row.eqId;
      that.queryCmdParams.fEqId = row.fEqId;
      //筛选出选中设备的设备类型的所有设备状态
      for (var cs in that.ctrlStateList) {
        if (that.ctrlStateList[cs].stateTypeId == row.eqType) {
          var ctrl = {};
          ctrl.stateId = that.ctrlStateList[cs].deviceState;
          ctrl.stateName = that.ctrlStateList[cs].stateName;
          ctrl.command = "";
          ctrl.eqId = row.eqId;
          ctrl.fEqId = row.fEqId;
          ctrl.eqType = row.eqType;
          ctrl.dmcontrolSeat = row.dmcontrolSeat;
          ctrl.zhanshi = false;
          that.ctrlCommandList.push(ctrl);
        }
      }
      if (that.ctrlCommandList.length > 0) {
        this.ctrlcmd = true;
        //查询选中设备的设备指令数据
        listDevcmd(this.queryCmdParams).then((response) => {
          for (var cc in that.ctrlCommandList) {
            for (let i = 0; i < response.rows.length; i++) {
              //设备状态相同
              if (
                that.ctrlCommandList[cc].stateId ==
                response.rows[i].codeDeviceState
              ) {
                that.ctrlCommandList[cc].command = response.rows[i].command;
              }
            }
          }
        });
      } else {
        this.$modal.msgWarning("该设备没有控制状态");
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row, "row");
      this.reset();
      const eqId = row.eqId || this.ids;
      getDevices(eqId).then((response) => {
        console.log(response.data, "修改按钮操作");
        this.form = response.data;
        this.form.eqType = String(response.data.eqType);
        this.open = true;
        this.submitMode = 0;
        this.title = "修改设备";
      });
      this.getExternalSystemList();
    },
    /** 提交按钮 */
    submitForm() {
      if (this.submitFormLoading) return;
      /*this.submitFormLoading = true*/
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.submitMode == 0) {
            await updateDevices(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.$refs.tableFile.clearSelection();
                this.open = false;
                this.getList();
              }
            });
          } else {
            await addDevices(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
        /*this.submitFormLoading = false*/
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this;
      const eqIds = row.eqId || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDevices(eqIds);
        })
        .then(() => {
          this.handleQuery();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {
          that.$refs.tableFile.clearSelection();
        });
    },

    /** 打开导入表弹窗 */
    handleImport() {
      this.upload.title = "设备导入";
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      if (response.msg != null && response.msg != "") {
        this.$alert(response.msg, "导入结果", {
          customClass: "el-message-box_style",
          dangerouslyUseHTMLString: true,
        });
      } else {
        const msg = "恭喜您，数据已全部导入成功！";
        this.$alert(msg, "导入结果", {
          customClass: "el-message-box_style",
          dangerouslyUseHTMLString: true,
        });
      }

      this.$forceUpdate();
      this.getList();
    },
    // 文件上传失败处理
    handleFileError(err, file, fileList) {
      this.$modal.msgError(JSON.parse(err.message).message);
      this.upload.open = false;
    },
    /** 下载模板操作 */
    importTemplate() {
      /* exportDevicesTemplate()*/
      /*.then((response) => {*/
      this.$download.nameXlsx("设备数据.xlsx", false);
      /*});*/
    },
    handleCheckChange(val) {
      this.queryParams.remark = val.toString();
    },
    insertEqControlPointAddress() {
      this.$refs["instructionForm"].validate((valid) => {
        if (valid) {
          createDmcontrolSeat(this.instructionForm).then((response) => {
            this.instructionDialog = false;
            this.form.controlPointAddress = response.instruction;
            this.input = response.instruction;
          });
        }
      });
    },
    createControlPointAddress() {
      if (this.form.fEqId != null && this.form.qNumber != null) {
        createInstruction(this.form).then((response) => {
          this.form.controlPointAddress = response.instructionSeat;
        });
      } else {
        this.$modal.msgError(
          "plc主机,查询+机位(格式：DM_*/CIO_*),查询对应点不能为空!!"
        );
      }
    },
    checkInstruction() {
      checkInstruction(this.queryParams).then((response) => {
        if (response.errorDate.length > 0) {
          alert(
            "共查询" +
            response.sumDate +
            "条数据," +
            response.errorDate.length +
            "数据指令出错。"
          );
          this.queryParams.pageNum = 1;
          this.queryParams.eqDirection = 1;
          this.getList();
        } else {
          this.$modal.msgSuccess("没有数据指令出错");
        }
      });
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    // debugger
    document.addEventListener("click", this.bodyCloseMenus);
  },
  props: {
    show: Boolean,
  },
  watch: {
    show: {
      async handler(newValue, oldValue) {
        // debugger
        this.visibleSync = !this.visibleSync
      }
    },
  },
}
</script>

<style scoped>
.offlinclass{
.el-dialog__body{
  height: 60vh;
}
}
</style>
