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
        <el-col :span="8">
          <el-form-item label="策略名称" prop="strategyName">
            <el-input
              style="width: 100%"
              v-model="strategyForm.strategyName"
              placeholder="请输入策略名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
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
        <el-col :span="8">
          <el-form-item label="隧道方向" prop="direction">
            <el-select
              clearable
              v-model="strategyForm.direction"
              placeholder="请选择隧道方向"
              @change="changeEvent"
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
        <el-col :span="8">
          <el-form-item label="事件类型" prop="eventType">
            <el-select
              clearable
              v-model="strategyForm.eventType"
              placeholder="请选择事件类型"
              style="width: 100%"
            >
              <el-option
                v-for="dict in eventTypeList"
                :key="dict.id"
                :label="dict.eventType"
                :value="dict.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="有效时间" prop="effectiveTime">
            <el-input
              v-model="strategyForm.effectiveTime"
              placeholder="请输入有效时间(单位：分钟)"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="事件触发" prop="isAutomatic">
            <el-select
              clearable
              v-model="strategyForm.isAutomatic"
              placeholder="请选择事件触发"
              style="width: 100%"
            >
              <el-option
                v-for="dict in triggerList"
                :key="dict.dictCode"
                :label="dict.dictLabel"
                :value="dict.dictCode"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="clear:both;">
        <el-col :span="24">
          <el-form-item label="执行操作">
            <div class="menu">
              <el-col :span="4">处置名称</el-col>
              <el-col :span="6">设备类型</el-col>
              <el-col :span="6">指定设备</el-col>
              <el-col :span="6">控制指令</el-col>
              <el-col :span="2">操作</el-col>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="planBox">
        <el-form-item
          v-for="(items, index) in strategyForm.manualControl"
          :key="index"
        >
          <el-col :span="4">
            <el-form-item prop="disposalName">
              <el-input
                v-model="items.disposalName"
                clearable
                placeholder="处置名称"
                style="width:100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-select
              v-model="items.equipmentTypeId"
              placeholder="请选择设备类型"
              clearable
              @change="changeEquipmentType(index)"
              style="width: 100%"
            >
              <el-option
                v-for="item in items.equipmentTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select
              v-show="strategyForm.equipmentTypeId != 30"
              v-model="items.value"
              multiple
              collapse-tags
              placeholder="请选择设备"
              @change="qbgChange(index, items.value)"
              style="width: 100%"
            >
              <el-option
                v-for="item in items.equipmentData"
                :key="item.eqId"
                :label="item.eqName"
                :value="item.eqId"
                :disabled="item.disabled"
              />
            </el-select>
          </el-col>
          <el-col
            :span="6"
            v-show="items.equipmentTypeId != 16 && items.equipmentTypeId != 36"
          >
            <el-select
              style="width: 100%"
              v-model="items.state"
              placeholder="请选择执行操作"
            >
              <el-option
                v-for="item in items.manualControlStateList"
                :key="item.deviceState + 1"
                :label="item.stateName"
                :value="item.deviceState"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col
            :span="6"
            v-show="items.equipmentTypeId == 16 || items.equipmentTypeId == 36"
          >
            <el-cascader
              :props="checkStrictly"
              v-model="items.state"
              :options="items.templatesList"
              :show-all-levels="false"
              clearable
              collapse-tags
              @change="handleChange"
              style="width:100%"
            ></el-cascader>
          </el-col>
          <el-col :span="2" class="buttonBox">
            <el-button
              class="delete"
              @click="removeItem(index)"
            ></el-button>
              <el-button
                class="add"
                @click="addItem"
                v-show="strategyForm.equipmentTypeId != 30"
              ></el-button>
          </el-col>
        </el-form-item>
      </el-row>
      <el-form-item class="dialog-footer">
        <el-button class="submitButton" @click="submitStrategyForm"
          >提交</el-button
        >
        <el-button class="closeButton" @click="strategyFormClose"
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
import { listType } from "@/api/equipment/type/api";
import { listRl, addRl } from "@/api/event/strategyRl";
import{listEventType}from "@/api/event/eventType";
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
      triggerList: [
        { dictCode: "0", dictLabel: "手动" },
        { dictCode: "1", dictLabel: "自动" },
      ],
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      sink: "", //删除/修改
      id: "", //策略id
      strategyForm: {
        jobRelationId: "", //时间戳
        strategyGroup: 2,
        strategyType: "0", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        eventType: "",
        effectiveTime: "",
        isAutomatic: "", //触发
        manualControl: [
          {
            disposalName: "",
            state: "",
            value: "",
            equipmentTypeId: "",
            equipmentTypeData: [],
            equipmentData: [],
          },
        ],
      },
      //设备类型查询参数
      queryEqTypeParams: {
        isControl: 1,
      },
      manualControlStateList: [],
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
        eventType: [
          { required: true, message: "请选择事件类型", trigger: "change" },
        ],
        effectiveTime: [
          { required: true, message: "请输入有效时间", trigger: "change" },
        ],
        isAutomatic: [
          { required: true, message: "请选择事件触发", trigger: "change" },
        ],
      },
      eventTypeList: [],
    };
  },
  methods: {
    init() {
      if (this.sink == "add") {
        this.resetForm();
      }
      // 事件类型
      let data = {prevControlType:"1"};
      listEventType(data).then(res=>{
        this.eventTypeList = res.rows;
      })
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
        this.strategyForm.isAutomatic = data.isAutomatic;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        this.strategyForm.eventType = Number(data.eventType);
        // this.$set(this.strategyForm, "eventType", data.eventType);

        listRl({ strategyId: this.id }).then((response) => {
          console.log(response, "设备数据");
          this.strategyForm.manualControl = response.rows;
          for (let i = 0; i < response.rows.length; i++) {
            let attr = response.rows[i];
            let manualControl = this.strategyForm.manualControl[i];
            let value = attr.equipments.split(",");
            this.$set(this.strategyForm.manualControl[i],"value",value);
            this.strategyForm.manualControl[i].state = attr.state;
            this.strategyForm.effectiveTime = attr.effectiveTime;
            this.strategyForm.manualControl[i].manualControlStateList =
              attr.eqStateList;
            this.strategyForm.manualControl[i].equipmentTypeId = Number(
              attr.eqTypeId
            );
            if (
              this.strategyForm.manualControl[i].equipmentTypeId == 16 ||
              this.strategyForm.manualControl[i].equipmentTypeId == 36
            ) {
              getVMSTemplatesByDevIdAndCategory(
                this.strategyForm.manualControl[i].value
              ).then((res) => {
                console.log(res.data, "模板信息");
                // this.templatesList = res.data;
                this.$set(
                  this.strategyForm.manualControl[i],
                  "templatesList",
                  res.data
                );
              });
            }
            if (
              this.strategyForm.manualControl[i].equipmentTypeId == 16 ||
              this.strategyForm.manualControl[i].equipmentTypeId == 36
            ) {
              this.strategyForm.manualControl[i].state = +attr.state;
              this.qbgChange(i,this.strategyForm.manualControl[i].value);
            }
            this.$set(
              manualControl,
              "equipmentTypeData",
              this.equipmentTypeData
            );
            listDevices({
              eqType: attr.eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
              eqDirection: this.strategyForm.direction, //方向
            }).then((res) => {
              this.$set(manualControl, "equipmentData", res.rows);
              // this.strategyForm.manualControl[i].equipmentData = res.rows;
              console.log(manualControl.equipmentData, "设备列表数据1");
            });
            // listType(this.queryEqTypeParams).then((data) => {
            //   console.log(data.rows,"设备类型")
            //   this.$set(manualControl,"equipmentTypeData",data.rows)
            //   // this.strategyForm.manualControl[i].equipmentTypeData = data.rows;
            // });
          }
        });
      });
    },
    // 改变设备类型
    changeEquipmentType(index) {
      this.strategyForm.manualControl[index].state = null;
      this.strategyForm.manualControl[index].value = null;
      let params = {
        eqType: this.strategyForm.manualControl[index].equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      listDevices(params).then((res) => {
        this.$set(
          this.strategyForm.manualControl[index],
          "equipmentData",
          res.rows
        );
        // this.strategyForm.manualControl[index].equipmentData = res.rows;
        console.log(res.rows, "设备列表");
      });
      this.listEqTypeStateIsControl(index);
    },
    qbgChange(index, value) {
      console.log(value);
      let data = value;
      if (
        this.strategyForm.manualControl[index].equipmentTypeId == 16 ||
        this.strategyForm.manualControl[index].equipmentTypeId == 36
      ) {
        getVMSTemplatesByDevIdAndCategory(data).then((res) => {
          console.log(res.data, "模板信息");
          // this.templatesList = res.data;
          this.$set(
            this.strategyForm.manualControl[index],
            "templatesList",
            res.data
          );
        });
      }
    },
    handleChange(e) {
      console.log(e);
    },
    // 查询设备可控状态
    listEqTypeStateIsControl(index) {
      var stateTypeId = this.strategyForm.manualControl[index].equipmentTypeId;
      var direction = this.strategyForm.direction;
      let params = {
        stateTypeId: stateTypeId,
        direction: direction,
        isControl: 1,
      };
      listEqTypeStateIsControl(params).then((response) => {
        this.strategyForm.manualControl[index].manualControlStateList =
          response.rows;
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
          var manualControl = JSON.parse(JSON.stringify(this.strategyForm.manualControl));
          //如果不是疏散标志则判断是否填写
          let result = manualControl.every(function (item) {
            console.log(item.state,item.value);
            return item.equipmentTypeId && item.state && item.value && item.disposalName
          });
          console.log(result);
          if(!result){
            return this.$modal.msgError("请填写完整");
          }
          // manualControl.map(item=>{
          //   if (item.equipmentTypeId != 30) {
          //     if (
          //       item.value.length == 0 ||
          //       item.state == ""
          //     ) {
          //       return this.$modal.msgError("请选择设备并添加执行操作");
          //     }
          //   } else {
          //     if (item.state == "") {
          //       return this.$modal.msgError("请选择疏散标志执行操作");
          //     }
          //   }
          // })


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
      let data = this.strategyForm.manualControl;
      data.forEach((item) => {
        item.effectiveTime = this.strategyForm.effectiveTime;
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
        this.$emit("dialogVisibleCloseEvent");
        this.getList();
      });
    },
    // 提交保存方法
    async addStrategyInfoData() {
      await getGuid().then((res) => {
        this.strategyForm.jobRelationId = res;
      });
      let data = this.strategyForm.manualControl;
      data.forEach((item) => {
        item.effectiveTime = this.strategyForm.effectiveTime;
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      addStrategyInfo(params).then((res) => {
        this.resetForm();
        this.$emit("dialogVisibleCloseEvent");
        this.$modal.msgSuccess("新增策略成功");
      });
    },
    // 改变隧道或者方向
    changeEvent(value) {
      //给设备名称重新赋值
      let params = {
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
      };
      // 如果改变隧道||设备类型||方向，重置设备和执行状态
      if (
        this.strategyForm.manualControl.length >= 1 ||
        this.strategyForm.manualControl[0].value != ""
      ) {
        this.strategyForm.manualControl = [
          { state: "", value: "", equipmentTypeId: "" },
        ];
      }
      // 查询设备类型并赋值
      let manualControl = this.strategyForm.manualControl;
      for (let i = 0; i < manualControl.length; i++) {
        listType(this.queryEqTypeParams).then((data) => {
          console.log(data.rows, "设备类型");
          this.$set(manualControl[i], "equipmentTypeData", data.rows);
          // this.strategyForm.manualControl[i].equipmentTypeData = data.rows;
        });
      }
    },
    removeItem(index) {
      console.log(index);
      if (this.strategyForm.manualControl.length == 1) {
        return this.$modal.msgWarning("至少添加一条执行操作");
      }
      this.strategyForm.manualControl.splice(index, 1);
    },
    // 添加执行操作
    addItem() {
      this.addCf();
      this.strategyForm.manualControl.push({
        disposalName: "",
        value: "",
        state: "",
        equipmentTypeId: "",
        equipmentTypeData: [],
        equipmentData: [],
      });
      this.getEquipmentType();
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
      let manualControl = this.strategyForm.manualControl;
      for (let i = 0; i < manualControl.length; i++) {
        listType(this.queryEqTypeParams).then((data) => {
          console.log(data.rows, "设备类型");
          this.$set(manualControl[i], "equipmentTypeData", data.rows);
          this.equipmentTypeData = data.rows;
        });
      }
    },

    //表单重置方法
    resetForm() {
      this.$refs["manualControl"].resetFields();
      this.strategyForm.manualControl = [
        {
          disposalName: "",
          state: "",
          value: "",
          equipmentTypeId: "",
          equipmentData: [],
          equipmentTypeData: [],
        },
      ];
    },
    strategyFormClose() {
      this.$emit("dialogVisibleCloseEvent");
    },
  },
};
</script>

<style lang="scss" scoped>
.menu {
  color: white;
  background-color: #74c5ff;
  height: 40px;
  .el-col{
    text-align: center;
  }
}
.buttonBox {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 36px;
  .delete,.add{
    width:16px;
    height: 16px;
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100%;
    border:none;
    background-color: transparent;
  }
  .delete{
    background-image: url(../../../../assets/icons/delete.png);
  }
  .add{
    background-image: url(../../../../assets/icons/add.png);
  }
}
</style>
