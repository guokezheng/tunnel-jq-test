<template>
  <div>
    <el-form
      ref="timingControl"
      :model="strategyForm"
      :rules="formDataValidator"
      label-width="100px"
    >
      <el-row>
        <el-col>
          <el-form-item label="策略名称" prop="strategyName">
            <el-input
              style="width: 90%"
              v-model="strategyForm.strategyName"
              placeholder="请输入策略名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="隧道名称" prop="tunnelId">
            <el-select
              style="width: 100%"
              v-model="strategyForm.tunnelId"
              placeholder="请选择隧道"
              clearable
              @change="changeEvent()"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="隧道方向" prop="direction">
            <el-select
              clearable
              v-model="strategyForm.direction"
              placeholder="请选择方向"
              @change="changeEvent()"
              style="width: 95%"
            >
              <el-option
                v-for="dict in directionOptions"
                :key="dict.value"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="定时频率"
            prop="schedulerTime"
            style="width: 92%"
          >
            <el-input
              v-model="strategyForm.schedulerTime"
              placeholder="请输入cron执行表达式"
            >
              <template slot="append">
                <el-button type="primary" @click="handleShowCron">
                  生成表达式
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="22">
          <el-form-item label="执行操作">
            <div class="menu">
              <span>设备类型</span>
              <span>指定设备</span>
              <span>控制指令</span>
              <span>操作</span>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item
          v-for="(dain, index) in strategyForm.autoControl"
          :key="index"
        >
          <el-col :span="6">
            <el-select
              v-model="dain.equipmentTypeId"
              placeholder="请选择设备类型"
              clearable
              title="手动控制"
              @change="changeEquipmentType(index)"
            >
              <el-option
                v-for="item in dain.equipmentTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select
              v-model="dain.equipments"
              multiple
              collapse-tags
              placeholder="请选择设备"
              @change="qbgChange(index,dain.value)"
            >
              <el-option
                v-for="item in dain.equipmentData"
                :key="item.eqId"
                :label="item.eqName"
                :value="item.eqId"
                :disabled="item.disabled"
              />
            </el-select>
          </el-col>
          <el-col :span="6" v-show="items.equipmentTypeId != 16 && items.equipmentTypeId != 36">
            <el-select
              v-model="dain.state"
              placeholder="请选择设备执行操作"
            >
              <el-option
                v-for="(item, indx) in dain.eqStateList"
                :key="item.deviceState"
                :label="item.stateName"
                :value="item.deviceState"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="6" v-show="items.equipmentTypeId == 16 || items.equipmentTypeId == 36">
            <el-cascader
              :props="checkStrictly"
              v-model="items.state"
              :options="items.templatesList"
              :show-all-levels="false"
              clearable
              collapse-tags
              :key="isResouceShow"
              @change="handleChange"></el-cascader>
          </el-col>
          <el-col :span="4" class="buttonBox">
            <el-button
              type=""
              icon="el-icon-delete"
              circle
              @click="removeItem(index)"
              style="margin-left: 2%"
            ></el-button>
            <el-button
              type=""
              icon="el-icon-plus"
              circle
              @click="addItem"
              style="margin-left: 2%"
            ></el-button>
          </el-col>
        </el-form-item>
      </el-row>
      <el-form-item class="dialog-footer">
        <el-button style="width: 30%" type="primary" @click="submitStrategyForm"
          >提交</el-button
        >
        <el-button style="width: 30%" @click="strategyFormClose"
          >取 消</el-button
        >
      </el-form-item>
    </el-form>
    <el-dialog
      title="Cron表达式生成器"
      :visible.sync="openCron"
      append-to-body
      destroy-on-close
      class="scrollbar"
    >
      <crontab
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="expression"
      ></crontab>
    </el-dialog>
  </div>
</template>

  <script>
import {
  listJob,
  getJob,
  delJob,
  addJob,
  updateJob,
  exportJob,
  runJob,
  changeJobStatus,
} from "@/api/monitor/job";
import Crontab from "@/components/Crontab";

import { listEqTypeStateIsControl,getVMSTemplatesByDevIdAndCategory } from "@/api/equipment/eqTypeState/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { listType } from "@/api/equipment/type/api";
import {
  listStrategy,
  getStrategy,
  delStrategy,
  updateState,
  addStrategy,
  addStrategyInfo,
  updateStrategyInfo,
  getGuid,
  handleStrategy,
} from "@/api/event/strategy";
import { listRl, addRl } from "@/api/event/strategyRl";
export default {
  model: {
    prop: "cronValue",
    event: "change",
  },
  components: {
    Crontab,
  },
  dicts: ["sys_job_group", "sys_job_status"],
  data() {
    return {
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      expression: "",
      paramsData : {
        tunnelId: ""
      },
      openCron: false,
      id: "", //策略id
      submitChooseEqFormLoading: false,
      // 二次弹窗表单参数
      eqForm: {
        equipment_type: null, //设备类型ID
        equipments: null, //设备列表
      },
      manualControlStateList: [],
      // 二次表单校验
      rules: {
        equipment_type: [
          { required: true, message: "请选择设备类型", trigger: "blur" },
        ],
        equipments: [
          { required: true, message: "请选择设备", trigger: "blur" },
        ],
      },
      viewStrategy: false,
      showCronBox: false,
      strategyForm: {
        strategyGroup:1,
        schedulerTime: "", //cron数据
        strategyType: "1", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            equipmentTypeId:"",//设备类型
            equipment:[],//设备列表
            equipmentTypeData:[],equipmentData:[]
          },
        ],
      },
      //设备类型查询参数
      queryEqTypeParams: {
        isControl: 1,
      },
      tunnelData: [], //隧道列表
      directionOptions: [], //方向列表
      equipmentTypeData: [], //设备类型列表
      equipmentData: [], //设备列表
      formDataValidator: {
        direction: [{ required: true, message: "请选择方向", trigger: "blur" }],
        tunnelId: [
          { required: true, message: "请选择隧道", trigger: "change" },
        ],
        strategyName: [
          { required: true, message: "请输入策略名称", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    init() {
      if (this.sink == "add") {
        this.resetForm();
        this.$nextTick(() => {
          this.showCronBox = false;
          this.$refs.cron.checkClear();
        });
      }
      this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },
    getStrategyData(row) {
      console.log(row, "当前策略数据");
      listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
      });
      getStrategy(this.id).then((response) => {
        let data = response.data;
        this.strategyForm.id = data.id;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        // this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        this.strategyForm.schedulerTime = data.schedulerTime;
        listRl({ strategyId: row.id }).then((response) => {
          this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
          listDevices({
            eqType: response.rows[0].eqTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
          }).then((res) => {
            this.equipmentData = res.rows;
          });
          console.log(response.rows, "设备列表");
          this.strategyForm.autoControl = response.rows;
          for (var i = 0; i < response.rows.length; i++) {
            let autoControl = this.strategyForm.autoControl[i];
            var attr = response.rows[i];
            this.strategyForm.autoControl[i].equipments = attr.equipments.split(",");
            this.strategyForm.autoControl[i].eqStateList = attr.eqStateList;
            this.strategyForm.autoControl[i].state = attr.state;
            this.strategyForm.autoControl[i].type = attr.eqTypeId;
            this.strategyForm.autoControl[i].equipmentTypeId = Number(attr.eqTypeId);
            this.$set(autoControl,"equipmentTypeData",this.equipmentTypeData);
            listDevices({
              eqType: attr.eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
              eqDirection: this.strategyForm.direction, //方向
            }).then((res) => {
              this.$set(autoControl,"equipmentData",res.rows)
              console.log(autoControl.equipmentData,"设备列表数据1")
            });
          }
        });
      });
    },
    // 改变设备类型
    changeEquipmentType(index){
      let params = {
        eqType: this.strategyForm.autoControl[index].equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      listDevices(params).then((res) => {
        this.$set(this.strategyForm.autoControl[index],"equipmentData",res.rows)
        console.log(this.equipmentData, "设备列表");
      });
      this.listEqTypeStateIsControl(index);
    },
    qbgChange(index,value){
      console.log(value);
      let data = value;
      getVMSTemplatesByDevIdAndCategory(data).then(res=>{
        console.log(res.data,"模板信息")
        // this.templatesList = res.data;
        this.$set(this.strategyForm.manualControl[index],"templatesList",res.data)
      })
    },
    handleChange(e){
      console.log(e)
    },
    // 查询设备可控状态
    listEqTypeStateIsControl(index) {
      var stateTypeId = this.strategyForm.autoControl[index].equipmentTypeId;
      var direction = this.strategyForm.direction;
      let params = {
        stateTypeId: stateTypeId,
        direction: direction,
        isControl: 1,
      };
      listEqTypeStateIsControl(params).then((response) => {
        this.strategyForm.autoControl[index].eqStateList = response.rows;
      });
    },
    // public查询设备列表
    listDevices() {
      if (Number(this.strategyForm.strategyType) == 0) {
        var eqType = this.strategyForm.equipmentTypeId;
      } else {
        var eqType = this.eqForm.equipment_type;
      }
      listDevices({
        eqType: eqType,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
      }).then((res) => {
        let data = res.rows;
        if (this.chooseEq && this.strategyForm.autoControl.length > 1) {
          if (this.strategyForm.strategyType != "0") {
            var currentList = this.strategyForm.autoControl;
          }
          let newData = [];
          for (let i = 0; i < currentList.length; i++) {
            newData += currentList[i].value + ",";
          }
          newData = newData.split(",");
          for (let i = 0; i < data.length; i++) {
            for (let z = 0; z < newData.length; z++) {
              if (data[i].eqId == newData[z]) {
                data[i].disabled = true;
              }
            }
          }
          this.equipmentData = data;
          this.$forceUpdate();
        }
        this.equipmentData = data;
        this.$forceUpdate();
      });
    },
    /** 提交按钮 */
    async submitStrategyForm() {
      this.$refs["timingControl"].validate((valid) => {
        if (valid) {
          var autoControl = this.strategyForm.autoControl;

          if (
            autoControl.length < 1 ||
            autoControl[0].equipments.length == 0 ||
            autoControl[0].state == ""
          ) {
            return this.$modal.msgError("请选择设备并添加执行操作");
          }
          // 判断是修改还是删除
          if (this.sink == "edit") {
            this.updateStrategyInfoData();
          } else {
            this.addStrategyInfoData();
          }
        }
      });
    },
    // 编辑操作
    async updateStrategyInfoData() {
      if (this.sink == "add") {
        await getGuid().then((res) => {
          this.strategyForm.jobRelationId = res;
          this.strategyForm.id = this.id;
        });
      }
      let data = this.strategyForm.manualControl;
      data.forEach(item=>{
        item.state = item.state.toString()
      })
      let params = this.strategyForm;
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
        this.$emit("dialogVisibleClose");
        this.getList();
      });
    },
    // 提交保存方法
    async addStrategyInfoData() {
      await getGuid().then((res) => {
        this.strategyForm.jobRelationId = res;
      });
      let data = this.strategyForm.manualControl;
      data.forEach(item=>{
        item.state = item.state.toString()
      })
      let params = this.strategyForm;
      addStrategyInfo(params).then((res) => {
        this.resetForm();
        this.$emit("dialogVisibleClose");
        this.$modal.msgSuccess("新增策略成功");
      });
    },
    //二次弹窗选择设备提交按钮
    submitChooseEqForm() {
      // 1.赋值 2.比对之前的是否重复   3.根据设备类型查询控制状态  66666
      let index = this.strategyForm.autoControl.length - 1; //求最后一位
      this.strategyForm.autoControl[index].value = this.eqForm.equipments;
      this.strategyForm.autoControl[index].type = this.eqForm.equipment_type;
      this.equipmentTypeData.forEach((item) => {
        if (item.typeId == this.eqForm.equipment_type) {
          this.strategyForm.autoControl[index].typeName = item.typeName;
        }
      });
      this.chooseEq = false; //关闭弹窗
      this.index = 0;
      // 如果设备操作状态已选择,则重置状态值
      if (this.strategyForm.autoControl[index].state) {
        this.strategyForm.autoControl[index].state = "";
      }
      listEqTypeStateIsControl({
        stateTypeId: this.eqForm.equipment_type,
        isControl: 1,
      }).then((response) => {
        this.strategyForm.autoControl[index].eqStateList = response.rows;
        this.$forceUpdate();
      });
    },
    // 改变设备类型或者方向
    changeEvent(value) {
      //给设备名称重新赋值
      let params = {
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      // listDevices(params).then((res) => {
      //   this.equipmentData = res.rows;
      //   console.log(this.equipmentData, "设备列表");
      // });
      // // 如果改变隧道||设备类型||方向，重置设备和执行状态
      // if (
      //   this.strategyForm.autoControl.length >= 1 ||
      //   this.strategyForm.autoControl[0].value != ""
      // ) {
      //   this.strategyForm.autoControl = [{ value: "", state: "", type: "" }];
      // }
      if (value == "1") {
        this.listEqTypeStateIsControl();
      }
    },
    removeItem(index) {
      console.log(index);
      this.strategyForm.autoControl.splice(index, 1);
    },
    // 添加执行操作
    addItem() {
      // let list = this.strategyForm.autoControl;
      // var flag = list.every(function (item) {
      //   return item.value != "" || item.state != "" || item.type != "";
      // });
      // console.log(flag);
      // if (flag == false) {
      //   return this.$modal.msgError("请选择设备并添加执行操作");
      // }
      this.addCf();
      this.strategyForm.autoControl.push({
        value: "",
        state: "",
        equipmentTypeId:"",
        equipmentTypeData: [],
        equipmentData: [],
      });
      this.getEquipmentType();
    },
    //查询设备控制状态和设备列表
    eqTypeChange() {
      if (this.eqForm.equipments.length >= 1) {
        this.eqForm.equipments = "";
      }
      this.listDevices();
    },
    listDevices() {
      var eqType = this.eqForm.equipment_type;
      listDevices({
        eqType: eqType,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
      }).then((res) => {
        let data = res.rows;
        if (this.chooseEq && this.strategyForm.autoControl.length > 1) {
          if (this.strategyForm.strategyType != "0") {
            var currentList = this.strategyForm.autoControl;
          }
          let newData = [];
          for (let i = 0; i < currentList.length; i++) {
            newData += currentList[i].value + ",";
          }
          newData = newData.split(",");
          for (let i = 0; i < data.length; i++) {
            for (let z = 0; z < newData.length; z++) {
              if (data[i].eqId == newData[z]) {
                data[i].disabled = true;
              }
            }
          }
          this.equipmentData = data;
          this.$forceUpdate();
        }
        this.equipmentData = data;
        this.$forceUpdate();
      });
    },
    // 去重，已选择设备增加disable
    addCf() {
      listDevices({
        eqType: this.strategyForm.equipmentTypeId,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
      }).then((res) => {
        let data = res.rows;
        if (this.strategyForm.autoControl.length > 1) {
          var currentList = this.strategyForm.autoControl;
          let newData = [];
          for (let i = 0; i < currentList.length; i++) {
            newData += currentList[i].value + ",";
          }
          newData = newData.split(",");
          console.log(newData);
          for (let i = 0; i < data.length; i++) {
            for (let z = 0; z < newData.length; z++) {
              if (data[i].eqId == newData[z]) {
                data[i].disabled = true;
              }
            }
          }
          this.equipmentData = data;
          this.$forceUpdate();
        } else {
          this.equipmentData = data;
        }
      });
    },
    // 打开选择设备弹窗
    openEqDialog2(event, index) {
      if (this.strategyForm.autoControl[index].type == "") {
        this.equipmentData = [];
      }
      this.eqForm.equipments = this.strategyForm.autoControl[index].value;
      this.eqForm.equipment_type = this.strategyForm.autoControl[index].type;
      this.getEquipmentType();
      this.chooseEq = true;
    },
    // 查询设备可控状态
    // listEqTypeStateIsControl() {
    //   var stateTypeId = this.strategyForm.equipmentTypeId;
    //   var direction = this.strategyForm.direction;
    //   let params = {
    //     stateTypeId: stateTypeId,
    //     direction: direction,
    //     isControl: 1,
    //   };
    //   listEqTypeStateIsControl(params).then((response) => {
    //     this.manualControlStateList = response.rows;
    //   });
    // },
    // 选择设备取消按钮
    cancelChooseEq() {
      this.chooseEq = false;
      this.$refs["eqForm"].resetFields();
    },
    /** 查询隧道列表 */
    getTunnels() {
      if(this.$cache.local.get("manageStation") == "1"){
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect")
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    getDirection() {
      this.getDicts("sd_direction").then((response) => {
        this.directionOptions = response.data;
        console.log(this.directionOptions, "方向");
      });
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      let autoControl = this.strategyForm.autoControl;
      for(let i = 0;i < autoControl.length;i++){
        listType(this.queryEqTypeParams).then((data) => {
          console.log(data.rows,"设备类型")
          this.$set(autoControl[i],"equipmentTypeData",data.rows)
          this.equipmentTypeData = data.rows;
        });
      }
    },
    changeValue(value) {
      this.changeVal = value;
    },
    //表单重置方法
    resetForm() {
      this.$refs["timingControl"].resetFields();
      this.strategyForm.schedulerTime = "";
      this.strategyForm.autoControl = [{ value: "", state: "", type: "" }];
    },
    strategyFormClose() {
      this.$emit("dialogVisibleClose");
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.expression = this.strategyForm.schedulerTime;
      this.openCron = true;
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.strategyForm.schedulerTime = value;
    },
  },
};
</script>

<style lang="scss" scoped>
  .menu {color:white;background-color: #74c5ff;display: flex;justify-content: space-around;align-items: center;}
  .buttonBox{display: flex;justify-content: space-around;align-items: center}
</style>
