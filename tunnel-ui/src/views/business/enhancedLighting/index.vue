<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          v-hasPermi="['system:type:add']"
          size="small"
          @click="handleAdd()"
          >新增
        </el-button>
        <el-button
          size="small"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:type:export']"
          >导出</el-button
        >
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入隧道名称，回车搜索"
            v-model="queryParams.tunnelName"
            @keyup.enter.native="handleQuery"
            size="small"
            style="border-right: solid 1px #00c8ff; border-radius: 3px"
            @click="$forceUpdate()"
          >
            <!-- <el-button
              slot="append"
              class="searchTable"
              @click="lx_boxShow = !lx_boxShow"
            ></el-button> -->
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- <div class="searchBox" v-show="lx_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="queryParams.tunnelId"
            placeholder="请选择所属隧道"
            clearable
            size="small"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
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
    </div> -->

    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="configList"
      @selection-change="handleSelectionChange"
      :row-key="getRowKey"
      @row-click="handleRowClick"
      height="62vh"
      class="allTable"
      ref="tableFile"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="隧道名称"
        align="center"
        prop="tunnelId"
        :formatter="gettunnelName"
      />
      <el-table-column
        label="模式类型"
        align="center"
        prop="modeType"
        :formatter="getModeType"
      />
      <!-- <el-table-column label="时间段配置参数" align="center" prop="timeSlot" /> -->
      <el-table-column
        label="当前亮度值"
        align="center"
        prop="beforeLuminance"
      />
      <el-table-column
        label="最大车流量(辆)"
        align="center"
        prop="maxTrafficFlow"
      />
      <el-table-column label="最小亮度值" align="center" prop="minLuminance" />
      <el-table-column
        label="响应时长(毫秒)"
        align="center"
        prop="respondTime"
      />
      <el-table-column
        label="调光最大区间"
        align="center"
        prop="maxLuminanceRange"
      />
      <el-table-column
        label="调光最小区间"
        align="center"
        prop="minLuminanceRange"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
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

    <!-- 添加或修改加强照明配置信息对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="60%"
      append-to-body
      :close-on-click-modal="false"
      :before-close="cancel"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道名称" prop="tunnelId">
              <el-select
                v-model="form.tunnelId"
                placeholder="请选择隧道"
                style="width: 100%"
              >
                <el-option
                  v-for="item in eqTunnelData"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模式类型" prop="modeType">
              <el-select
                v-model="form.modeType"
                placeholder="请选择模式类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in modeTypeList"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="开启调光模式" prop="isStatus">
              <el-tooltip
                :content="form.isStatus == 1 ? '开启' : '关闭'"
                placement="top"
              >
                <el-switch
                  v-model="form.isStatus"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  :active-value="1"
                  :inactive-value="0"
                >
                </el-switch>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开启车流量模式" prop="isTrafficVolume">
              <el-tooltip
                :content="form.isTrafficVolume == 1 ? '开启' : '关闭'"
                placement="top"
              >
                <el-switch
                  v-model="form.isTrafficVolume"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  :active-value="1"
                  :inactive-value="0"
                >
                </el-switch>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="最小亮度值"
              prop="minLuminance"
              v-if="form.modeType != 0"
            >
              <el-input
                v-model="form.minLuminance"
                placeholder="请输入最小亮度值(0-100)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="当前亮度值"
              prop="beforeLuminance"
              v-if="form.modeType == 1"
            >
              <el-input
                v-model="form.beforeLuminance"
                placeholder="请输入当前亮度值(0-100)"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最大车流量(辆)" prop="maxTrafficFlow">
              <el-input
                v-model="form.maxTrafficFlow"
                placeholder="请输入最大车流量"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="响应时长(毫秒)"
              prop="respondTime"
              v-if="form.modeType != 0"
            >
              <el-input
                v-model="form.respondTime"
                placeholder="请输入响应时长 单位：秒s"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调光最大区间" prop="maxLuminanceRange">
              <el-input
                v-model="form.maxLuminanceRange"
                placeholder="请输入调光最大区间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调光最小区间" prop="minLuminanceRange">
              <el-input
                v-model="form.minLuminanceRange"
                placeholder="请输入调光最小区间"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="form.modeType != 1" style="max-height:40vh;overflow-y: auto;overflow-x: hidden;">
          <el-form-item
            v-for="(item, index) in timeSlotList"
            :key="item.key"
            :label="'定时区间' + (index+1)"
          >
            <el-row :gutter="15">
              <el-col :span="4" style="padding-left:0px">
                <el-select
                  v-model="item.eqIds"
                  placeholder="请选择控制段"
                  width="80px"
                >
                  <el-option
                    v-for="eqInfo in eqIdList"
                    :key="eqInfo.value"
                    :label="eqInfo.name"
                    :value="eqInfo.value"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-select
                  v-model="item.direction"
                  placeholder="请选择路段方向"
                >
                  <el-option
                    v-for="dict in directionOptions"
                    :key="dict.value"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-time-picker
                  placeholder="选择时间"
                  v-model="item.startTime"
                  style="width: 100%"
                ></el-time-picker>
              </el-col>
              <el-col :span="4">
                <el-time-picker
                  placeholder="选择时间"
                  v-model="item.endTime"
                  style="width: 100%"
                ></el-time-picker>
              </el-col>
              <el-col :span="4">
                <el-input placeholder="亮度" v-model="item.value"></el-input>
              </el-col>
              <el-col :span="2">
                <el-button
                  type="primary"
                  @click="addTimeSlot"
                  icon="el-icon-plus"
                  v-if="index != timeSlotList.length - 1 || index == 0"
                  ></el-button
                >
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="delTimeSlot(item)"
                  v-if="index == timeSlotList.length - 1 && index != 0"
                ></el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listConfig,
  getConfig,
  delConfig,
  addConfig,
  updateConfig,
  exportConfig,
} from "@/api/business/enhancedLighting/app.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { newListDevices } from "@/api/equipment/eqlist/api";

export default {
  name: "Type",
  data() {
    return {
      lx_boxShow: false,
      iskeyVehicle: "0",
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
      // 加强照明配置信息表格数据
      configList: [],
      //所属隧道
      eqTunnelData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        tunnelName: "",
        modeType: null,
        timeSlot: null,
        beforeLuminance: null,
        minLuminance: null,
        respondTime: null,
        maxLuminanceRange: null,
        minLuminanceRange: null,
        maxTrafficFlow: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tunnelName: [
          { required: true, message: "类型编码不能为空", trigger: "change" },
          {
            pattern: /^[a-zA-Z0-9]+$/,
            message: "类型编码为数字加字母",
            trigger: "blur",
          },
        ],
      },
      timeSlotList: [],
      nowTimeSlotList: [
        {
          startTime: null,
          endTime: null,
          value: null,
          direction: "3",
          eqIds: null,
        },
      ],
      //获取当前隧道下全部加强照明
      eqIdList: [
        {
          name: "棚洞段加强照明",
          value: "0",
        },
        {
          name: "入口段加强照明",
          value: "1",
        },
        {
          name: "过渡段加强照明",
          value: "2",
        },
        {
          name: "基本段",
          value: "4",
        },
        {
          name: "出口段加强照明",
          value: "5",
        },
      ],
      //根据隧道
      directionEqList: [],
      modeTypeList: [
        {
          name: "定时模式",
          value: 0,
        },
        {
          name: "自动模式",
          value: 1,
        },
        {
          name: "节能模式",
          value: 2,
        },
      ],
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getDirection();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels(this.queryParams).then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    // //获取设备列表信息
    // getListDevices(eqTunnelId,eqType){
    //   let param = {eqTunnelId:eqTunnelId,eqType:eqType};
    //   newListDevices(param).then((response) => {
    //     if (response.code == 200) {
    //       this.eqIdList = response.rows;
    //     }
    //   });
    // },
    // getListDevicesByParam(){
    //   //查询加强照明
    //   this.getListDevices(this.form.tunnelId,7);
    // },
    //发生改变时，清空item.eqIds;
    //查询方向
    //查询方向
    getDirection() {
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    clearEqIds(item) {
      item.eqIds = [];
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.lx_boxShow == true) {
          self.lx_boxShow = false;
        }
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    /** 查询加强照明配置信息列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then((response) => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },
    // 表单重置
    reset() {
      this.timeSlotList = this.nowTimeSlotList;
      this.form = {
        id: null,
        tunnelId: null,
        modeType: null,
        timeSlot: null,
        beforeLuminance: null,
        minLuminance: null,
        respondTime: null,
        maxLuminanceRange: null,
        minLuminanceRange: null,
        maxTrafficFlow: null,
        isStatus: 0,
        isTrafficVolume: 0,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.tunnelName = "";
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
      this.form.iskeyVehicle = "0";
      this.timeSlotList = [
        {
          startTime: null,
          endTime: null,
          value: null,
          direction: "3",
          eqIds: null,
        },
      ];
      this.title = "添加加强照明配置信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getConfig(id).then((response) => {
        this.form = response.data;
        this.timeSlotList = JSON.parse(response.data.timeSlot);
        for (let index = 0; index < this.timeSlotList.length; index++) {
          const param = this.timeSlotList[index];
          param.startTime = new Date(
            ("1970-01-01 " + param.startTime).replace(/-/g, "/")
          );
          param.endTime = new Date(
            ("1970-01-01 " + param.endTime).replace(/-/g, "/")
          );
        }
        this.nowTimeSlotList = this.timeSlotList;
        //获取 HH:mm:ss;
        this.open = true;
        this.title = "修改加强照明配置信息";
        // this.getListDevicesByParam();
      });
    },
    /** 提交按钮 */
    submitForm() {
      if (this.form.modeType != 1) {
        console.log(this.timeSlotList, "this.timeSlotList");
        //timeSlotList 校验
        for (let index = 0; index < this.timeSlotList.length; index++) {
          const element = this.timeSlotList[index];
          if (element.startTime == null || element.startTime == "") {
            this.$modal.msgError("时间区间开始时间不能为空,请填写开始时间");
            return;
          } else if (element.endTime == null || element.endTime == "") {
            this.$modal.msgError("时间区间结束时间不能为空,请填写结束时间");
            return;
          } else if (element.value == null || element.value == "") {
            this.$modal.msgError("时间区间亮度不能为空,请填写亮度值");
            return;
          }
        }
        for (let index = 0; index < this.timeSlotList.length; index++) {
          const param = this.timeSlotList[index];
          param.startTime =
            (param.startTime.getHours() < 10
              ? "0" + param.startTime.getHours()
              : param.startTime.getHours()) +
            ":" +
            (param.startTime.getMinutes() < 10
              ? "0" + param.startTime.getMinutes()
              : param.startTime.getMinutes()) +
            ":" +
            (param.startTime.getSeconds() < 10
              ? "0" + param.startTime.getSeconds()
              : param.startTime.getSeconds());
          param.endTime =
            (param.endTime.getHours() < 10
              ? "0" + param.endTime.getHours()
              : param.endTime.getHours()) +
            ":" +
            (param.endTime.getMinutes() < 10
              ? "0" + param.endTime.getMinutes()
              : param.endTime.getMinutes()) +
            ":" +
            (param.endTime.getSeconds() < 10
              ? "0" + param.endTime.getSeconds()
              : param.endTime.getSeconds());
        }
        this.form.timeSlot = JSON.stringify(this.timeSlotList);
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateConfig(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.tableFile.clearSelection();
              this.getList();
            });
          } else {
            addConfig(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除加强照明配置信息编号为"' + ids + '"的数据项？')
        .then(function () {
          return delConfig(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          this.$refs.tableFile.clearSelection();
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的加强照明配置信息？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的加强照明配置信息？";
      }
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有加强照明配置信息？")
        .then(() => {
          this.exportLoading = true;
          return exportConfig(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
        })
        .catch(() => {});
    },
    getModeType(row) {
      var resultName = "";
      console.log(row.modeType, "row.modeType");
      // 0定时模式  1  自动模式  2 节能模式
      for (let index = 0; index < this.modeTypeList.length; index++) {
        var param = this.modeTypeList[index];
        if (row.modeType == param.value) {
          return param.name;
        }
      }
      return resultName;
    },
    gettunnelName(row) {
      var resultName = "";
      for (let index = 0; index < this.eqTunnelData.length; index++) {
        var param = this.eqTunnelData[index];
        if (row.tunnelId == param.tunnelId) {
          return param.tunnelName;
        }
      }
      return resultName;
    },
    addTimeSlot() {
      this.timeSlotList.push({
        startTime: null,
        endTime: null,
        value: null,
      });
    },
    delTimeSlot(item) {
      var index = this.timeSlotList.indexOf(item);
      if (index !== -1) {
        this.timeSlotList.splice(index, 1);
      }
    },
  },
};
</script>
<style scoped lang="scss">
// ::v-deep .el-dialog__body{
//   max-height: 70vh !important;
// }
</style>


