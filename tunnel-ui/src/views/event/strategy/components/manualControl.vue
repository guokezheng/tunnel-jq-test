<!-- 手动控制 -->
<template>
  <div>
    <el-form
      ref="manualControl"
      :model="strategyForm"
      :rules="formDataValidator"
      label-width="100px"
    >
      <el-row>
        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select
            style="width: 90%"
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

        <el-form-item label="策略名称" prop="strategyName">
          <el-input
            style="width: 90%"
            v-model="strategyForm.strategyName"
            placeholder="请输入策略名称"
          />
        </el-form-item>
        <el-col :span="8">
          <el-form-item label="设备类型" prop="equipmentTypeId">
            <el-select
              v-model="strategyForm.equipmentTypeId"
              placeholder="请选择设备类型"
              clearable
              title="手动控制"
              @change="changeEvent('1')"
            >
              <el-option
                v-for="item in equipmentTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="方向" prop="direction">
            <el-select
              clearable
              v-model="strategyForm.direction"
              placeholder="请选择设备方向"
              @change="changeEvent"
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
        <el-form-item
          v-for="(items, index) in strategyForm.manualControl"
          :key="items.state + index"
        >
          <el-select
            v-model="items.value"
            multiple
            placeholder="请选择设备"
            style="width: 40%"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in equipmentData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
              :disabled="item.disabled"
            />
          </el-select>

          <el-select
            v-model="items.state"
            placeholder="请选择设备需要执行的操作"
            style="width: 39%; margin-left: 6%"
          >
            <el-option
              v-for="item in manualControlStateList"
              :key="item.deviceState + 1"
              :label="item.stateName"
              :value="item.deviceState"
            >
            </el-option>
          </el-select>
          <el-button
            type=""
            icon="el-icon-delete"
            circle
            @click="removeItem(index)"
            style="margin-left: 2%"
          ></el-button>
        </el-form-item>
        <el-form-item label="" style="">
          <a href="#" @click="addItem" style="color: #1890ff">+添加执行操作</a>
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
  </div>
</template>

<script>
import { listEqTypeStateIsControl } from "@/api/equipment/eqTypeState/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { listType } from "@/api/equipment/type/api";
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
      sink: "", //删除/修改
      id: "", //策略id
      strategyForm: {
        jobRelationId: "", //时间戳
        equipmentTypeId: "", //设备类型
        strategyType: "0", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        manualControl: [{ state: "", value: "" }],
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
        equipmentTypeId: [
          { required: true, message: "请选择设备类型", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    init() {
      if (this.sink == "add") {
        this.resetForm();
        console.log("123");
      }
      console.log("init");
      this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },
    getStrategyData(row) {
      getStrategy(this.id).then((response) => {
        let data = response.data;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        listRl({ strategyId: this.id }).then((response) => {
          console.log(response, "设备数据");
          this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
          if (this.strategyForm.strategyType == "0") {
            this.eqTypeChange();
          }
          listDevices({
            eqType: response.rows[0].eqTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
          }).then((res) => {
            this.equipmentData = res.rows;
          });
          this.strategyForm.manualControl = response.rows;
          this.strategyForm.manualControl.forEach((item) => {
            item.value = "";
          });
          for (var i = 0; i < response.rows.length; i++) {
            var attr = response.rows[i];
            this.strategyForm.manualControl[i].value =
              attr.equipments.split(",");
            console.log(this.strategyForm.manualControl[i].value);
            this.strategyForm.manualControl[i].state = attr.state;
            this.strategyForm.equipmentTypeId = Number(attr.eqTypeId);
          }
        });
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
    //查询设备控制状态和设备列表
    eqTypeChange() {
      if (Number(this.strategyForm.strategyType) == 0) {
        var stateTypeId = this.strategyForm.equipmentTypeId;
        var direction = this.strategyForm.direction;
        let params = {
          stateTypeId: stateTypeId,
          direction: direction,
          isControl: 1,
        };
        this.listEqTypeStateIsControl(params);
        this.listDevices();
      } else {
        if (this.eqForm.equipments.length > 1) {
          this.eqForm.equipments = "";
        }
        this.listDevices();
      }
    },
    /** 提交按钮 */
    async submitStrategyForm() {
      this.$refs["manualControl"].validate((valid) => {
        if (valid) {
          console.log(this.strategyForm, "要提交数据");
          var manualControl = this.strategyForm.manualControl;
          if (
            manualControl[0].value.length == 0 ||
            manualControl[0].state == ""
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
      await getGuid().then((res) => {
        this.strategyForm.jobRelationId = res;
        this.strategyForm.id = this.id;
      });
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
      let params = this.strategyForm;
      addStrategyInfo(params).then((res) => {
        this.resetForm();
        this.$emit("dialogVisibleClose");
        this.$modal.msgSuccess("新增策略成功");
      });
    },
    // 改变设备类型或者方向
    changeEvent(value) {
      console.log(value);
      //给设备名称重新赋值
      let params = {
        eqType: this.strategyForm.equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      listDevices(params).then((res) => {
        this.equipmentData = res.rows;
        console.log(this.equipmentData, "设备列表");
      });
      // 如果改变隧道||设备类型||方向，重置设备和执行状态
      if (
        this.strategyForm.manualControl.length >= 1 ||
        this.strategyForm.manualControl[0].value != ""
      ) {
        this.strategyForm.manualControl = [{ state: "", value: "" }];
      }
      if (value == "1") {
        this.listEqTypeStateIsControl();
      }
    },
    removeItem(index) {
      console.log(index);
      this.strategyForm.manualControl.splice(index, 1);
    },

    // 添加执行操作
    addItem() {
      this.addCf();
      this.strategyForm.manualControl.push({
        value: "",
        state: "",
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
        if (this.strategyForm.manualControl.length > 1) {
          var currentList = this.strategyForm.manualControl;
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
    // 查询设备可控状态
    listEqTypeStateIsControl() {
      var stateTypeId = this.strategyForm.equipmentTypeId;
      var direction = this.strategyForm.direction;
      let params = {
        stateTypeId: stateTypeId,
        direction: direction,
        isControl: 1,
      };
      listEqTypeStateIsControl(params).then((response) => {
        this.manualControlStateList = response.rows;
      });
    },
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then((response) => {
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
      listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
        console.log(this.equipmentTypeData, "设备类型");
      });
    },

    //表单重置方法
    resetForm() {
      this.$refs["manualControl"].resetFields();
      this.strategyForm.manualControl = [{ state: "", value: "" }];
    },
    strategyFormClose() {
      this.$emit("dialogVisibleClose");
    },
  },
};
</script>

<style>
</style>