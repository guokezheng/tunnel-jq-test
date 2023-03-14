<!-- 分时控制 -->
<template>
  <div>
    <el-form
      ref="timeControl"
      :model="strategyForm"
      :rules="formDataValidator"
      label-width="100px"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="策略名称" prop="strategyName">
            <el-input
              style="width: 100%"
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
        <el-col :span="12">
          <el-form-item label="隧道方向" prop="direction">
            <el-select
              clearable
              v-model="strategyForm.direction"
              placeholder="请选择方向"
              @change="changeEvent()"
              style="width: 100%"
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
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="起始时间" prop="startTime">
            <el-time-picker
              v-model="strategyForm.startTime"
              placeholder="请选择时间"
              value-format="HH:mm:ss"
              style="width: 100%"
            >
            </el-time-picker>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="结束时间" prop="endTime">
            <el-time-picker
              v-model="strategyForm.endTime"
              placeholder="请选择时间"
              value-format="HH:mm:ss"
              style="width: 100%"
              @change="changeEndTime()"
            >
            </el-time-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="clear:both;">
        <el-col :span="24">
          <el-form-item label="执行操作">
            <div class="menu">
              <el-col :span="6">设备类型</el-col>
              <el-col :span="6">指定设备</el-col>
              <el-col :span="8">控制指令</el-col>
              <el-col :span="4">操作</el-col>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="22">
        <el-form-item
          v-for="(dain, index) in strategyForm.autoControl"
          :key="index"
        >
          <el-col :span="6">
            <el-select
              v-model="dain.equipmentTypeId"
              placeholder="请选择设备类型"
              clearable
              @change="changeEquipmentType(index)"
              style="width:100%;"
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
              @change="qbgChange(index, dain.equipments)"
              style="width:100%;"
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
          <div
            v-show="dain.equipmentTypeId != 16 && dain.equipmentTypeId != 36"
          >
            <el-col :span="4">
              <el-select
                v-model="dain.openState"
                placeholder="启动指令"
                style="width: 100%"
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
            <el-col :span="4">
              <el-select
                v-model="dain.closeState"
                placeholder="关闭指令"
                style="width: 100%"
              >
                <el-option
                  v-for="(item, indx) in dain.eqStateListFan"
                  :key="item.deviceState"
                  :label="item.stateName"
                  :value="item.deviceState"
                >
                </el-option>
              </el-select>
            </el-col>
          </div>
          <el-col
            :span="6"
            v-show="dain.equipmentTypeId == 16 || dain.equipmentTypeId == 36"
          >
            <el-cascader
              style="width: 100%"
              :props="checkStrictly"
              v-model="dain.state"
              :options="dain.templatesList"
              :show-all-levels="false"
              clearable
              collapse-tags
              @change="handleChange"
            ></el-cascader>
          </el-col>
          <el-col :span="4" class="buttonBox">
            <el-button
              type=""
              icon="el-icon-delete"
              circle
              @click="removeItem(index)"
            ></el-button>
            <el-button
              type=""
              icon="el-icon-plus"
              circle
              @click="addItem"
            ></el-button>
          </el-col>
        </el-form-item>
      </el-col>
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
  </div>
</template>

<script>
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
} from "@/api/equipment/eqTypeState/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { listType, autoEqTypeList } from "@/api/equipment/type/api";
import { listItem } from "@/api/equipment/eqTypeItem/item";
import { listRl, addRl } from "@/api/event/strategyRl";
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
export default {
  data() {
    return {
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      selectIndex: 0,
      paramsData: {
        tunnelId: "",
      },
      submitChooseEqFormLoading: false,
      //是否显示 选择设备弹出层
      chooseEq: false,
      // 二次弹窗表单参数
      eqForm: {
        equipment_type: null, //设备类型ID
        equipments: null, //设备列表
      },
      // 二次表单校验
      rules: {
        equipment_type: [
          { required: true, message: "请选择设备类型22", trigger: "blur" },
        ],
        equipments: [
          { required: true, message: "请选择设备", trigger: "blur" },
        ],
      },
      showCronBox: false,
      strategyForm: {
        strategyGroup: 1,
        strategyType: "3", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        startTime: "",
        endTime: "",
        autoControl: [
          {
            value: "", //设备
            openState: "", //状态
            closeState: "",
            type: "", //设备分类
            equipmentTypeId: "", //设备类型
            equipment: [], //设备列表
            eqStateList: [], //执行开启操作
            eqStateListFan: [], //关闭
          },
        ],
      },
      //设备类型查询参数
      queryEqTypeParams: {
        isControl: 1,
      },
      deviceName: [], //设备名称列表
      dataItem: [], //数据项
      symbol: [], //符号
      eqTypeList: [], //触发器设备类型
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
        getGuid().then((res) => {
          this.strategyForm.jobRelationId = res;
        });
      }
      this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },
    changeEndTime(){
      let startTime = this.strategyForm.startTime;
      let endTime = this.strategyForm.endTime;
      if(startTime)
      console.log(startTime,endTime)
      let d1 = startTime.split(':');
      let d2 = endTime.split(':');
      console.log(d1,d2)
      if(d1.length == 3 && d2.length){
        for(let i = 0;i < d1.length;i++){
          for(let i = 0;i < d2.length;i++){
            if(d1[0] > d2[0] || d1[1] > d2[1] || d1[2] > d2[2]){
              this.strategyForm.endTime = "";
              return this.$modal.msgWarning("开始时间不能大于结束时间");
            }
          }
        }
      }
    },
    /** 修改按钮操作 */
    async getStrategyData(row) {
      //获取设备
      autoEqTypeList().then((res) => {
        this.eqTypeList = res.rows;
      });
      await listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
      });
      this.viewStrategy = false;
      this.currentId = row.id;
      this.sink = "edit";
      const id = row.id || this.ids;
      getStrategy(id).then((response) => {
        let data = response.data;
        this.strategyForm.id = data.id;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        this.$set(this.strategyForm, "startTime", data.timerOpen);
        this.$set(this.strategyForm, "endTime", data.timerClose);
        listRl({ strategyId: id }).then((response) => {
          this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
          listDevices({
            eqType: response.rows[0].eqTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
          }).then((res) => {
            this.equipmentData = res.rows;
          });
          this.strategyForm.autoControl = response.rows;
          for (let i = 0; i < response.rows.length; i++) {
            let autoControl = this.strategyForm.autoControl[i];
            let attr = response.rows[i];
            this.strategyForm.autoControl[i].equipments =
              attr.equipments.split(",");
            this.strategyForm.autoControl[i].openState = attr.state;
            this.strategyForm.autoControl[i].closeState = attr.state;
            this.strategyForm.autoControl[i].type = attr.eqTypeId;
            this.strategyForm.autoControl[i].equipmentTypeId = Number(
              attr.eqTypeId
            );
            this.$set(autoControl, "equipmentTypeData", this.equipmentTypeData);
            // this.strategyForm.autoControl[i].eqStateList = attr.eqStateList;
            // this.strategyForm.autoControl[i].eqStateListFan = attr.eqStateList;
            this.$set(
              this.strategyForm.autoControl[i],
              "eqStateList",
              attr.eqStateList
            );
            this.$set(
              this.strategyForm.autoControl[i],
              "eqStateListFan",
              attr.eqStateList
            );
            listDevices({
              eqType: attr.eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
              eqDirection: this.strategyForm.direction, //方向
            }).then((res) => {
              this.$set(autoControl, "equipmentData", res.rows);
            });
          }
        });
      });
    },
    qbgChange(index, value) {
      console.log(value);
      let data = value;
      if (
        this.strategyForm.autoControl[index].equipmentTypeId == 16 ||
        this.strategyForm.autoControl[index].equipmentTypeId == 36
      ) {
        getVMSTemplatesByDevIdAndCategory(data).then((res) => {
          console.log(res.data, "模板信息");
          // this.templatesList = res.data;
          this.$set(
            this.strategyForm.autoControl[index],
            "templatesList",
            res.data
          );
        });
      }
    },
    handleChange(e) {
      console.log(e);
    },
    /** 提交按钮 */
    async submitStrategyForm() {
      this.$refs["timeControl"].validate((valid) => {
        if (valid) {
          let autoControl = this.strategyForm.autoControl;
          // for(let i = 0;i < autoControl.length;i++){
          //   if (
          //     autoControl[i].equipments.length == 0 ||
          //     autoControl[i].closeState == "" || autoControl[0].openState == ""
          //   ) {
          //     return this.$modal.msgError("请选择设备并添加执行操作");
          //   }
          // }
          if (this.sink == "edit") {
            this.updateStrategyInfoData();
          } else {
            this.addStrategyInfoData();
          }
        }
      });
    },
    // 编辑操作
    updateStrategyInfoData() {
      let data = this.strategyForm.autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
        this.$emit("dialogVisibleClose");
        this.getList();
      });
    },
    // 提交保存方法
    addStrategyInfoData() {
      // for (let i = 0; i < this.strategyForm.autoControl.length; i++) {
      //   this.strategyForm.autoControl[i].controlTime = this.timeChange(
      //     this.strategyForm.autoControl[i].controlTime
      //   );
      // }
      let data = this.strategyForm.autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      addStrategyInfo(params).then((res) => {
        this.resetForm();
        this.$emit("dialogVisibleClose");
        this.$modal.msgSuccess("新增策略成功");
      });
    },
    timeChange(date) {
      var h =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var m =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var s =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return h + m + s;
    },
    // 改变设备类型
    changeEquipmentType(index) {
      this.$set(this.strategyForm.autoControl[index], "openState", "");
      this.$set(this.strategyForm.autoControl[index], "closeState", "");
      this.$set(this.strategyForm.autoControl[index], "equipments", null);
      let params = {
        eqType: this.strategyForm.autoControl[index].equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      listDevices(params).then((res) => {
        this.$set(
          this.strategyForm.autoControl[index],
          "equipmentData",
          res.rows
        );
        // this.equipmentData = res.rows;
        console.log(this.equipmentData, "设备列表");
      });
      this.listEqTypeStateIsControl(index);
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
        this.strategyForm.autoControl[index].eqStateListFan = response.rows;
        console.log(
          this.strategyForm.autoControl[index].eqStateList,
          "00000000000"
        );
      });
    },
    //二次弹窗选择设备提交按钮
    submitChooseEqForm() {
      // 1.赋值 2.比对之前的是否重复   3.根据设备类型查询控制状态
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
      // let params = {
      //   eqType: this.strategyForm.equipmentTypeId, //设备类型
      //   eqTunnelId: this.strategyForm.tunnelId, //隧道
      //   eqDirection: this.strategyForm.direction, //方向
      // };
      // listDevices(params).then((res) => {
      //   this.equipmentData = res.rows;
      //   console.log(this.equipmentData, "设备列表");
      // });
      // // 如果改变隧道||设备类型||方向，重置设备和执行状态
      // if (
      //   this.strategyForm.autoControl.length >= 1 ||
      //   this.strategyForm.autoControl[0].value != ""
      // ) {
      //   this.strategyForm.autoControl = [
      //     {
      //       value: "",
      //       state: "",
      //       type: "",
      //       controlTime: "",
      //     },
      //   ];
      // }
      if (value == "1") {
        this.listEqTypeStateIsControl();
      }
    },
    selectEqName() {
      this.rest();
      // 选择设备名称赋值
      listDevices({
        eqType: this.strategyForm.triggers.deviceTypeId,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
      }).then((res) => {
        this.getListItem();
        this.equipmentData = res.rows;
        this.deviceName = res.rows;
      });
    },
    getListItem() {
      //给设备数据项赋值
      listItem({ deviceTypeId: this.strategyForm.triggers.deviceTypeId }).then(
        (res) => {
          this.dataItem = res.rows;
          console.log(this.dataItem, "数据项");
        }
      );
    },
    //重置方法
    rest() {
      this.strategyForm.triggers.deviceId = null;
      this.strategyForm.triggers.elementId = null;
      this.deviceName = [];
      this.dataItem = [];
    },
    removeItem(index) {
      console.log(index);
      if (this.strategyForm.autoControl.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.strategyForm.autoControl.splice(index, 1);
    },
    // 添加执行操作
    addItem() {
      let list = this.strategyForm.autoControl;
      var flag = list.every(function (item) {
        return item.value != "" || item.state != "" || item.type != "";
      });
      console.log(flag);
      // if (flag == false) {
      //   return this.$modal.msgError("请选择设备并添加执行操作");
      // }
      this.addCf();
      if (this.strategyForm.autoControl.length == 2) {
        return this.$modal.msgError("最多添加2条数据");
      }
      this.strategyForm.autoControl.push({
        controlTime: "",
        value: "",
        state: "",
        type: "",
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
        this.equipmentData = data;
      });
    },
    // 打开选择设备弹窗
    openEqDialog2(event, index) {
      this.selectIndex = index;
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
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        this.getAutoEqTypeList();
        this.getSymbol();
      });
    },
    getSymbol() {
      this.getDicts("sd_trigger_compare_type").then((response) => {
        this.symbol = response.data;
      });
    },
    // 查询触发器设备类型
    getAutoEqTypeList() {
      autoEqTypeList().then((res) => {
        this.eqTypeList = res.rows;
        console.log(this.eqTypeList, "触发器设备类型");
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
      for (let i = 0; i < autoControl.length; i++) {
        listType(this.queryEqTypeParams).then((data) => {
          console.log(data.rows, "设备类型");
          this.$set(autoControl[i], "equipmentTypeData", data.rows);
          this.equipmentTypeData = data.rows;
        });
      }
    },
    changeValue(value) {
      this.changeVal = value;
    },
    //表单重置方法
    resetForm() {
      this.$refs["timeControl"].resetFields();
      this.strategyForm.autoControl = [
        {
          value: "",
          state: "",
          type: "",
          controlTime: "",
        },
      ];
    },
    // 取消按钮
    strategyFormClose() {
      this.$emit("dialogVisibleClose");
    },
  },
};
</script>

<style>
.triggers .box .el-form-item__content {
  display: flex;
  justify-content: center;
  align-items: center;
}
.triggers .box .el-form-item__content .el-form-item {
  margin-left: 15px;
}
</style>
<style lang="scss" scoped>
.menu {
  color: white;
  background-color: #74c5ff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  .el-col{
    text-align: center;
  }
}
.buttonBox {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
</style>
