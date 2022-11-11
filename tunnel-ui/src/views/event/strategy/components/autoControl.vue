<template>
  <div>
    <el-form
      ref="autoControl"
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
        <el-col>
          <el-form-item label="方向" prop="direction">
            <el-select
              clearable
              v-model="strategyForm.direction"
              placeholder="请选择设备方向"
              @change="changeEvent()"
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
        <el-col class="triggers">
          <div class="box">
            <el-form-item
              label="触发器"
              prop="triggers.deviceTypeId"
              label-width="100px"
            >
              <el-select
                v-model="strategyForm.triggers.deviceTypeId"
                placeholder="请选择设备类型"
                style="width: 18%"
                @change="selectEqName()"
              >
                <el-option
                  v-for="item in eqTypeList"
                  :key="item.typeId"
                  :label="item.typeName"
                  :value="item.typeId"
                >
                </el-option>
              </el-select>
              <el-form-item prop="triggers.deviceId" label-width="300">
                <el-select
                  v-model="strategyForm.triggers.deviceId"
                  placeholder="请选择设备名称"
                  multiple
                  @change="selectDataItem()"
                >
                  <el-option
                    v-for="item in deviceName"
                    :key="item.eqId"
                    :label="item.eqName"
                    :value="item.eqId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="triggers.elementId" label-width="0">
                <el-select
                  v-model="strategyForm.triggers.elementId"
                  placeholder="请选择数据项"
                >
                  <el-option
                    v-for="item in dataItem"
                    :key="item.id"
                    :label="item.itemName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="triggers.comparePattern" label-width="0">
                <el-select v-model="strategyForm.triggers.comparePattern">
                  <el-option
                    v-for="item in symbol"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="triggers.compareValue" label-width="0">
                <el-input
                  v-model="strategyForm.triggers.compareValue"
                  placeholder="请输入阈值"
                />
              </el-form-item>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item
          v-for="(dain, index) in strategyForm.autoControl"
          :key="index"
        >
          <div>
            <el-form-item style="width: 100%; margin-top: 20px">
              <el-input
                @click.native="openEqDialog2($event, index)"
                style="width: 40%; margin-top: 20px"
                v-model="dain.typeName"
                placeholder="请点击选择控制类型"
              />
              <el-select
                v-model="dain.state"
                placeholder="请选择设备需要执行的操作"
                style="width: 40%; margin-left: 5%"
              >
                <el-option
                  v-for="(item, indx) in dain.eqStateList"
                  :key="item.deviceState"
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
          </div>
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
    <!-- 选择设备-对话框 定时控制里的弹窗-->
    <el-dialog
      title="选择设备"
      :visible.sync="chooseEq"
      :close-on-click-modal="false"
      width="40%"
      style="height: 100%"
      append-to-body
    >
      <el-form
        ref="eqForm"
        :model="strategyForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="设备类型" prop="equipment_type">
          <el-radio-group
            v-model="eqForm.equipment_type"
            @change="eqTypeChange()"
          >
            <el-radio-button
              :label="item.typeId"
              v-for="item in equipmentTypeData"
              :key="item.typeId"
              style="margin: 3px"
            >
              {{ item.typeName }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="选择设备"
          :style="{ height: '300px' }"
          prop="equipments"
        >
          <el-select
            v-model="eqForm.equipments"
            :disabled="viewStrategy"
            multiple
            placeholder="请选择设备"
            style="width: 90%"
          >
            <el-option
              v-for="item in equipmentData"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
              :disabled="item.disabled"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          :loading="submitChooseEqFormLoading"
          @click.native="submitChooseEqForm"
          >确定</el-button
        >
        <el-button @click="cancelChooseEq">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

    <script>
import { listEqTypeStateIsControl } from "@/api/equipment/eqTypeState/api";
import {
  listType,
  listHasType,
  autoEqTypeList,
  getStateTypeId,
  getTriggersByRelateId,
} from "@/api/equipment/type/api";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listDevices } from "@/api/equipment/eqlist/api";
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
      id: "", //策略id
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
          { required: true, message: "请选择设备类型", trigger: "blur" },
        ],
        equipments: [
          { required: true, message: "请选择设备", trigger: "blur" },
        ],
      },
      viewStrategy:false,
      manualControlStateList:[],
      showCronBox: false,
      strategyForm: {
        strategyType: "2", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
          },
        ],
        triggers: {
          id: "",
          deviceTypeId: "", //设备类型
          deviceId: "",
          elementId: "", //设备数据项
          comparePattern: "", //比较的符号
          compareValue: "", //阈值
        },
      },
      //设备类型查询参数
      queryEqTypeParams: {
        isControl: 1,
      },
      //模拟量设备查询
      queryAnalogEqParams: {
        isAnalog: 1,
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
        triggers: {
          deviceTypeId: [
            { required: true, message: "请选择设备类型", trigger: "change" },
          ],
          deviceId: [
            { required: true, message: "请选择设备名称", trigger: "change" },
          ],
          elementId: [
            { required: true, message: "请选择数据项", trigger: "change" },
          ],
          comparePattern: [
            { required: true, message: "请选择运算符", trigger: "change" },
          ],
          compareValue: [
            { required: true, message: "请输入阈值", trigger: "change" },
          ],
        },
      },
    };
  },
  methods: {
    init() {
      if (this.sink == "add") {
        this.resetForm();
      }
      this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },

    /** 获取当前策略数据 */
    async getStrategyData(row) {
      //获取设备
      autoEqTypeList(this.queryAnalogEqParams).then((res) => {
        this.eqTypeList = res.rows;
      });
      await listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
      });
      getStrategy(this.id).then((response) => {
        let data = response.data;
        this.strategyForm.id = data.id;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        listItem({
          deviceTypeId: this.strategyForm.triggers.deviceTypeId,
        }).then((res) => {
          this.dataItem = res.rows;
        });

        // 获取触发器的数据
        getTriggersByRelateId({ relateId: response.data.id }).then((res) => {
          console.log(res, "触发器数据");
          this.strategyForm.triggers.id = res.data.id;
          this.strategyForm.triggers.comparePattern = res.data.comparePattern;
          this.strategyForm.triggers.compareValue = res.data.compareValue;
          this.strategyForm.triggers.deviceTypeId = res.data.deviceTypeId;
          this.strategyForm.triggers.elementId = res.data.elementId;
          listDevices({
            eqType: this.strategyForm.triggers.deviceTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
            eqDirection: this.strategyForm.direction,
          }).then((data) => {
            this.deviceName = data.rows;
            this.strategyForm.triggers.deviceId = res.data.deviceId.split(",");
          });
          listRl({ strategyId: row.id }).then((response) => {
            this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
            listDevices({
              eqType: response.rows[0].eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
            }).then((list) => {
              this.equipmentData = list.rows;
            });
            this.strategyForm.autoControl = response.rows;
            for (var i = 0; i < response.rows.length; i++) {
              var attr = response.rows[i];
              this.strategyForm.autoControl[i].value =
                attr.equipments.split(",");
              this.strategyForm.autoControl[i].eqStateList = attr.eqStateList;
              this.strategyForm.autoControl[i].state = attr.state;
              this.strategyForm.autoControl[i].type = attr.eqTypeId;
              this.strategyForm.autoControl[i].typeName = attr.eqType.typeName;
            }
          });
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
    /** 提交按钮 */
    async submitStrategyForm() {
      this.$refs["autoControl"].validate((valid) => {
        if (valid) {
          var autoControl = this.strategyForm.autoControl;
          console.log(autoControl.length);
          if (
            !autoControl.length ||
            autoControl[0].value.length == 0 ||
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
      this.strategyForm.triggers.deviceId =
        this.strategyForm.triggers.deviceId.toString();
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
      this.strategyForm.triggers.deviceId =
        this.strategyForm.triggers.deviceId.toString();
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
        this.strategyForm.autoControl.length >= 1 ||
        this.strategyForm.autoControl[0].value != ""
      ) {
        this.strategyForm.autoControl = [{ value: "", state: "", type: "" }];
      }
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
      this.strategyForm.autoControl.splice(index, 1);
    },
    // 添加执行操作
    addItem() {
      let list = this.strategyForm.autoControl;
      var flag = list.every(function (item) {
        return item.value != "" || item.state != "" || item.type != "";
      });
      console.log(flag);
      if (flag == false) {
        return this.$modal.msgError("请选择设备并添加执行操作");
      }
      this.addCf();
      this.strategyForm.autoControl.push({
        value: "",
        state: "",
        type: "",
      });
    },
    //查询设备控制状态和设备列表
    eqTypeChange() {
      console.log(this.eqForm.equipments, "设备数据");
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
    // 选择设备取消按钮
    cancelChooseEq() {
      this.chooseEq = false;
      this.$refs["eqForm"].resetFields();
    },
    /** 查询隧道列表 */
    getTunnels() {
      listTunnels().then((response) => {
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
      autoEqTypeList(this.queryAnalogEqParams).then((res) => {
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
      listType(this.queryEqTypeParams).then((response) => {
        this.equipmentTypeData = response.rows;
        console.log(this.equipmentTypeData, "设备类型");
      });
    },
    changeValue(value) {
      this.changeVal = value;
    },
    //表单重置方法
    resetForm() {
      this.$refs["autoControl"].resetFields();
      this.strategyForm.triggers = {
        deviceTypeId: "", //设备类型
        deviceId: "",
        elementId: "", //设备数据项
        comparePattern: "", //比较的符号
        compareValue: "", //阈值
      };
      this.strategyForm.autoControl = [{ value: "", state: "", type: "" }];
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
