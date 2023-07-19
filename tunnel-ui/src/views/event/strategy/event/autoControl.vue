<template>
  <div>
    <el-form
      ref="autoControl"
      :model="strategyForm"
      :rules="formDataValidator"
      label-width="100px"
    >
      <el-row>
        <el-col>
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
        <el-col :span="24">
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
        <el-col>
          <el-col :span="4">
<!--            <el-form-item-->
<!--              label="定时频率"-->
<!--              prop="schedulerTime"-->
<!--              style="width: 100%"-->
<!--            >-->
<!--              <el-input-->
<!--                v-model="strategyForm.schedulerTime"-->
<!--                placeholder="请输入cron执行表达式"-->
<!--              >-->
<!--                <template slot="append">-->
<!--                  <el-button type="primary" @click="handleShowCron">-->
<!--                    生成表达式-->
<!--                    <i class="el-icon-time el-icon&#45;&#45;right"></i>-->
<!--                  </el-button>-->
<!--                </template>-->
<!--              </el-input>-->
<!--            </el-form-item>-->
            <el-form-item
              label="间隔时间"
              prop="schedulerTime"
              style="width: 100%"
            >
              <el-input
                v-model="strategyForm.schedulerTime"
                placeholder="请输入分钟"
              > <div>dsd</div>
              </el-input>

            </el-form-item>
          </el-col>
          <el-col :span="4" style="margin-top: 6px">
            <span>(分钟)</span>
          </el-col>
        </el-col>
        <el-col class="triggers">
          <div class="box" style="width: 100%">
            <el-form-item
              label="触发器"
              prop="triggers.deviceTypeId"
              label-width="100px"
            >
              <el-select
                v-model="strategyForm.triggers.deviceTypeId"
                placeholder="请选择设备类型"
                style="width: 20%"
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
              <el-form-item prop="triggers.deviceId" style="width: 23%">
                <el-select
                  v-model="strategyForm.triggers.deviceId"
                  placeholder="请选择设备名称"
                  multiple
                  collapse-tags
                  style="width:100%;"
                  @visible-change="selectDataItem"
                  @change="optionDataItem()"
                >
                  <!-- @change="selectDataItem()" -->
                  <el-option
                    v-for="item in deviceName"
                    :key="item.eqId"
                    :label="item.eqName"
                    :value="item.eqId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="triggers.elementId" style="width: 15%">
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
              <!-- 计算符 -->
              <el-form-item prop="triggers.comparePattern" style="width: 15%">
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
              <el-form-item prop="triggers.compareValue" style="width: 12%">
                <el-input-number
                  v-model="strategyForm.triggers.compareValue"
                  placeholder="请输入阈值"
                />
              </el-form-item>
              <el-form-item prop="triggers.warningType" style="width: 15%">
                <el-select v-model="strategyForm.triggers.warningType">
                  <el-option
                    v-for="item in definition"
                    :key="item.warningType"
                    :label="item.name"
                    :value="item.warningType"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form-item>
          </div>
        </el-col>
        <el-row :gutter="20" style="clear:both;">
          <el-col :span="24">
            <el-form-item label="执行操作">
              <div class="menu">
                <el-col :span="6">设备资源类型</el-col>
                <el-col :span="8">指定设备</el-col>
                <el-col :span="8">控制指令</el-col>
                <el-col :span="2">操作</el-col>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-row>
      <div v-show="strategyForm.triggers.warningType == 1">
        <el-row :gutter="20" style="clear:both;">
          <el-col :span="24">
            <el-form-item
              v-for="(dain, index) in strategyForm.autoControl"
              :key="index"
            >
              <el-col :span="6" style="padding-left:0">
                <el-cascader
                  v-model="dain.equipmentTypeId"
                  :options="equipmentTypeData"
                  :props="equipmentTypeProps"
                  :show-all-levels="false"
                  @change="changeEquipmentType(index)"
                  style="width: 100%"
                ></el-cascader>
                <!-- <el-select
                  v-model="dain.equipmentTypeId"
                  placeholder="请选择设备类型"
                  clearable
                  title="手动控制"
                  @change="changeEquipmentType(index)"
                  style="width:100%;"
                >
                  <el-option
                    v-for="item in dain.equipmentTypeData"
                    :key="item.typeId"
                    :label="item.typeName"
                    :value="item.typeId"
                  />
                </el-select> -->
              </el-col>
              <el-col :span="8">
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
              <el-col
                :span="8"
                v-show="dain.equipmentTypeId != 16 && dain.equipmentTypeId != 36 && dain.equipmentTypeId != 7 && dain.equipmentTypeId != 9"
              >
                <el-select v-model="dain.state" placeholder="请选择设备执行操作" style="width:100%;">
                  <el-option
                    v-for="(item, indx) in dain.eqStateList"
                    :key="item.deviceState"
                    :label="item.stateName"
                    :value="item.deviceState"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <el-col
                :span="8"
                v-show="dain.equipmentTypeId == 7 ||  dain.equipmentTypeId == 9"
              >
              <el-select
                :style="{'width':  dain.state == 1 ? '45%' :'100%' }"
                v-model="dain.state"
                placeholder="请选择执行操作"
                @change="selectStateVal(index)"
              >
                <el-option
                  v-for="item in dain.eqStateList"
                  :key="item.deviceState + 1"
                  :label="item.stateName"
                  :value="item.deviceState"
                >
                </el-option>
              </el-select>
                <el-input-number v-if="dain.state == 1" v-model="dain.stateNum" style="width: 55%"  :min="dain.limitMin" :max="100" ></el-input-number>
              </el-col>
              <el-col
                :span="8"
                v-if="dain.equipmentTypeId == 16 || dain.equipmentTypeId == 36"
              >
                <!-- <el-cascader
                  :props="checkStrictly"
                  v-model="dain.state"
                  :options="dain.templatesList"
                  :show-all-levels="false"
                  clearable
                  collapse-tags
                  @change="handleChange"
                  style="width:100%;"
                ></el-cascader> -->
                <el-input v-model="dain.content" placeholder="请选择模板" readonly @click.native="openTemDialog(dain)">
                <el-button slot="append" icon="el-icon-search" @click.stop="templateClick(index, index,dain)"></el-button>
              </el-input>
              </el-col>
              <el-col :span="2" class="buttonBox">
                <el-button
                  class="delete"
                  @click="removeItem(index)"
                ></el-button>
                  <el-button
                    v-show="strategyForm.equipmentTypeId != 30"
                    class="add"
                    @click="addItem"
                  ></el-button>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <el-form-item class="dialog-footer">
        <el-button class="submitButton" @click="submitStrategyForm"
          >提交</el-button
        >
        <el-button class="closeButton" @click="strategyFormClose"
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
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <crontab
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="expression"
      ></crontab>
    </el-dialog>
    <el-dialog
      :title="templateData.processName"
      :visible.sync="dialogVisibleTem"
      append-to-body
      width="45%"
      :before-close="handleClose">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div style="display: flex;justify-content: center;align-items: center;">
        <!-- 'letter-spacing':templateData['font_spacing'] + 'px', -->
        <div :style="{
          'width':templateData['width'] + 'px',
          'height':templateData['height'] + 'px',
          'color':templateData['font_color'],
          'font-size':templateData['font_size'] + 'px',
          'font-family':templateData['font_type'],
          'background-color':'#000',
          'position':'relative',
          }">
          <span :style="{
            'position':'absolute',
            'top':templateData['top'] + 'px',
            'left':templateData['left'] + 'px',
          }"
                style="line-height:1" v-html="templateData['content']">
          </span>
        </div>
      </div>
    </el-dialog>
    <com-board class="comClass" ref="boardRef" @getVmsData="getMsgFormSon"></com-board>

  </div>
</template>

    <script>
    import {selectVmsContent} from "@/api/information/api";
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
} from "@/api/equipment/eqTypeState/api";
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
  getCategoryTree
} from "@/api/event/strategy";
import Crontab from "@/components/Crontab";
import{listEventType}from "@/api/event/eventType";
import comBoard from "@/views/event/reservePlan/board";

export default {
  components: {
    Crontab,
    comBoard
  },
  data() {
    return {
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      templateData:{},
      dialogVisibleTem:false,
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      definition: [
        { warningType: "0", name: "仅预警" },
        { warningType: "1", name: "预警联动" },
      ],
      paramsData: {
        tunnelId: "",
      },
      expression: "",
      openCron: false,
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
      viewStrategy: false,
      // manualControlStateList: [],
      eventTypeList: [], //事件类型
      showCronBox: false,
      strategyForm: {
        strategyGroup: 1,
        strategyType: "2", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        eventType: "", //事件类型
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            equipmentTypeId: "", //设备类型
            equipment: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          },
        ],
        triggers: {
          id: "",
          deviceTypeId: "", //设备类型
          deviceId: "",
          elementId: "", //设备数据项
          comparePattern: "", //比较的符号
          compareValue: "", //阈值
          warningType: "",
        },
        schedulerTime: "",
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
        direction: [{ required: true, message: "请选择方向", trigger: "change" }],
        tunnelId: [
          { required: true, message: "请选择隧道", trigger: "change" },
        ],
        strategyName: [
          { required: true, message: "请输入策略名称", trigger: "change" },
          { max: 50, message: '最长输入50个字符', trigger: 'change' }

        ],
        eventType: [
          { required: true, message: "请选择事件类型", trigger: "change" },
        ],
        schedulerTime:[
          { required: true, message: "请选择定时频率", trigger: "change" },
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
    async init() {
      debugger
      if (this.sink == "add") {
        this.resetForm();
      }
      // 获取事件类型
      await this.getListEventType();
      //设备资源类型
      await this.getEquipmentType();
      //请选择隧道
      await this.getTunnels();
      //请选择方向
      await this.getDirection();
      //给设备数据项赋值
      await this.getListItem();
    },
    // 请选择设备 照明
    selectStateVal(index){
      if(this.strategyForm.autoControl[index].state == 1){
        this.$set(this.strategyForm.autoControl[index], "stateNum", 100);
        //基本照明限制 最低亮度为 30
        if(this.strategyForm.autoControl[index].equipmentTypeId == 9){
          this.$set(this.strategyForm.autoControl[index], "limitMin", 30);
        }

      }else{
        this.$set(this.strategyForm.autoControl[index], "stateNum", 0);
      }
    },
    handleClose(){
      this.dialogVisibleTem = false
    },
    // 情报板选择模板点击事件
    templateClick(number, index,item){
      this.$refs.boardRef.init(
        number,
        index,
        item.equipmentTypeId,
      );
    },
    // 情报板弹窗
    getMsgFormSon(data){
      console.log(data,"data")
      this.$set(this.strategyForm.autoControl[data.index],'content',data.content);
      this.$set(this.strategyForm.autoControl[data.index],'state',data.id);
    },
    //查看情报板信息
    openTemDialog(item){
      let params = {id: item.id,state:item.state,type:'1'};

      if(item.state == '' || item.state == null){
        return this.$modal.msgWarning("请选择模板");
      }
      selectVmsContent(params).then((res)=>{
        this.templateData = Object.assign(res.data,item);
        console.log(this.templateData)
        let zxc = this.templateData['screen_size'].split('*');
        this.templateData['width'] = zxc[0];
        this.templateData['height'] = zxc[1];
        let align = this.templateData['coordinate'];
        this.templateData['left'] = align.substr(0,3);
        this.templateData['top'] = align.substr(3,6);
        let content = this.templateData['content'];
        if(content.indexOf('/n') == '-1'){
          this.templateData['content'] = content.replace(/\n|\r\n/g,'<br>').replace(/ /g, ' &nbsp');
        }
        this.dialogVisibleTem = true;
      })
    },
    getListEventType(){
      let data = {prevControlType:"1"};
      listEventType(data).then(res=>{
        this.eventTypeList = res.rows;
        console.log(this.eventTypeList,"事件类型");
      })
    },
    /** 获取当前策略数据 */
    async getStrategyData(row) {
      this.getListEventType();
      //获取设备
      autoEqTypeList(this.queryAnalogEqParams).then((res) => {
        this.eqTypeList = res.rows;
      });
      // await listType(this.queryEqTypeParams).then((response) => {
      //   this.equipmentTypeData = response.rows;
      // });
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
        // listItem({
        //   deviceTypeId: this.strategyForm.triggers.deviceTypeId,
        // }).then((res) => {
        //   this.dataItem = res.rows;
        // });

        // 获取触发器的数据
        getTriggersByRelateId({ relateId: response.data.id }).then((res) => {
          console.log(res, "触发器数据");
          this.strategyForm.triggers.id = res.data.id;
          this.strategyForm.triggers.comparePattern = res.data.comparePattern;
          this.strategyForm.triggers.compareValue = res.data.compareValue;
          this.strategyForm.triggers.deviceTypeId = res.data.deviceTypeId;
          this.strategyForm.triggers.elementId = res.data.elementId;
          // this.strategyForm.triggers.warningType = res.data.warningType;
          this.$set(
            this.strategyForm.triggers,
            "warningType",
            res.data.warningType
          );
          this.$set(this.strategyForm, "eventType", +data.eventType);
          console.log(this.strategyForm.triggers.deviceTypeId ,this.strategyForm.tunnelId,this.strategyForm.direction)
          let params={eqDirection: this.strategyForm.direction}
          if(this.strategyForm.direction == 3){
            params.eqDirection = null;
          }
          listDevices({
            eqType: this.strategyForm.triggers.deviceTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
            eqDirection: params.eqDirection,
          }).then((data) => {
            this.deviceName = data.rows;
            debugger
            this.$nextTick(() => {
              debugger
              this.strategyForm.triggers.deviceId = res.data.deviceId.split(",");
            })

          });
          listRl({ strategyId: row.id }).then((response) => {
            console.log(response,"response")
            this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
            listDevices({
              eqType: response.rows[0].eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
            }).then((list) => {
              this.equipmentData = list.rows;
            });
            this.strategyForm.autoControl = response.rows;
            for (var i = 0; i < response.rows.length; i++) {
              let autoControl = this.strategyForm.autoControl[i];
              var attr = response.rows[i];
              this.strategyForm.autoControl[i].equipments =
                attr.equipments.split(",");
              this.strategyForm.autoControl[i].eqStateList = attr.eqStateList;
              this.strategyForm.autoControl[i].state = attr.state;
              this.strategyForm.autoControl[i].type = attr.eqTypeId;
              this.strategyForm.autoControl[i].equipmentTypeId = String(
                attr.eqTypeId
              );
              if (
                this.strategyForm.autoControl[i].equipmentTypeId == 16 ||
                this.strategyForm.autoControl[i].equipmentTypeId == 36
              ) {
                this.strategyForm.autoControl[i].state = +attr.state;
                this.qbgChange(i,this.strategyForm.autoControl[i].equipments);
              }
              debugger
              this.$set(
                autoControl,
                "equipmentTypeData",
                this.equipmentTypeData
              );
              let params={eqDirection: this.strategyForm.direction}
              if(this.strategyForm.direction == 3){
                params.eqDirection = null;
              }
              listDevices({
                eqType: attr.eqTypeId,
                eqTunnelId: this.strategyForm.tunnelId,
                eqDirection: params.eqDirection, //方向
              }).then((res) => {
                debugger
                this.$set(autoControl, "equipmentData", res.rows);
                console.log(autoControl.equipmentData, "设备列表数据1");
              });
            }
          });
        });
      });
    },
    // 改变设备类型
    changeEquipmentType(index) {
      this.$set(this.strategyForm.autoControl[index], "content", null);
      this.$set(this.strategyForm.autoControl[index], "stateNum", null);
      this.$set(this.strategyForm.autoControl[index], "state", "");
      this.$set(this.strategyForm.autoControl[index], "equipments", null);
      let params = {
        eqType: this.strategyForm.autoControl[index].equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
        params:{
          orderBy : 'eqName'
        }
      };
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }
      debugger
      listDevices(params).then((res) => {
        this.$set(
          this.strategyForm.autoControl[index],
          "equipmentData",
          res.rows
        );
        console.log(this.equipmentData, "设备列表");
      });
      this.listEqTypeStateIsControl(index);
    },
    qbgChange(index, value) {
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
      this.$refs["autoControl"].validate((valid) => {
        if (valid) {
          var autoControl = this.strategyForm.autoControl;
          let response = JSON.parse(JSON.stringify(autoControl))
          // console.log(response,"response");
          // 如果为预警联动则判断是否填写完整
          if(this.strategyForm.triggers.warningType == '1'){
            let result = response.every(function (item) {
                return item.equipmentTypeId && item.state && item.equipments
            });
            // console.log(result);
            if(!result){
              return this.$modal.msgError("请填写完整");
            }
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
      let data = this.strategyForm.autoControl;
      data.forEach((item) => {
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
      this.strategyForm.triggers.deviceId =
        this.strategyForm.triggers.deviceId.toString();
      let data = this.strategyForm.autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      addStrategyInfo(params).then((res) => {
        this.resetForm();
        let data = true;
        this.$emit("dialogVisibleClose",data);
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
    // 改变隧道或者方向
    changeEvent(value) {
      console.log(value,"value")
      // 重置设备列表
      this.equipmentTypeData = [];
      this.deviceName = []
      this.dataItem = []
      this.strategyForm.autoControl = [
        { state: "", value: "", equipmentTypeId: "" },
      ];
      this.strategyForm.triggers =
        { deviceTypeId: "", deviceId: "", elementId: "",comparePattern:"",compareValue:"", warningType:""}
      ;

      if(this.strategyForm.tunnelId.length !=0 && this.strategyForm.direction.length !=0){
        //this.listEqTypeStateIsControl();
        this.getEquipmentType();
      }
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
      //   this.strategyForm.autoControl = [{ value: "", state: "", type: "",equipmentTypeId:"" }];
      // }
      // 查询设备类型并赋值
      let autoControl = this.strategyForm.autoControl;
      // for (let i = 0; i < autoControl.length; i++) {
      //   listType(this.queryEqTypeParams).then((data) => {
      //     this.$set(autoControl[i], "equipmentTypeData", data.rows);
      //     // this.strategyForm.manualControl[i].equipmentTypeData = data.rows;
      //   });
      // }
      if (value == "1") {
        this.listEqTypeStateIsControl();
      }
    },
     /** 查询设备类型列表 */
     getEquipmentType() {
      let autoControl = this.strategyForm.autoControl;
      for (let i = 0; i < autoControl.length; i++) {
        getCategoryTree().then((data) => {
          debugger
          this.$set(autoControl[i], "equipmentTypeData", data.data);
          this.equipmentTypeData = data.data;
        });
      }
    },
    selectEqName() {
      this.rest();
      // 选择设备名称赋值
      console.log(this.strategyForm.tunnelId,this.strategyForm.direction)
      if(this.strategyForm.tunnelId && this.strategyForm.direction && this.strategyForm.triggers.deviceTypeId){
        let params={eqDirection: this.strategyForm.direction}
        if(this.strategyForm.direction == 3){
          params.eqDirection = null;
        }
        console.log(this.strategyForm.triggers.deviceTypeId,this.strategyForm.tunnelId, params.eqDirection)
        listDevices({
          eqType: this.strategyForm.triggers.deviceTypeId,
          eqTunnelId: this.strategyForm.tunnelId,
          eqDirection: params.eqDirection,
        }).then((res) => {
          this.equipmentData = res.rows;
          debugger
          this.deviceName = res.rows;
        });
      }
    },
    selectDataItem(e){
      console.log(this.strategyForm.triggers.deviceId,"111111111")
      if(e == true){
        if(!this.strategyForm.tunnelId){
          this.$modal.msgWarning("请先选择隧道")
        }else if(!this.strategyForm.direction){
          this.$modal.msgWarning("请先选择方向")
        }else{
          this.selectEqName()
        }
      }
    },
    optionDataItem(){
      this.getListItem();
    },
    getListItem() {
      //给设备数据项赋值
      console.log(this.strategyForm.triggers.deviceId,"00000000000")
      if(this.strategyForm.triggers.deviceId){
        listItem({ deviceTypeId: this.strategyForm.triggers.deviceTypeId }).then(
          (res) => {
            debugger
            this.dataItem = res.rows;
            console.log(this.dataItem, "数据项");
          }
        );
      }
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
      this.addCf();
      this.strategyForm.autoControl.push({
        value: "",
        state: "",
        equipmentTypeId: "",
        equipmentTypeData: [],
        equipmentData: [],
      });
      this.getEquipmentType();
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
    // openEqDialog2(event, index) {
    //   if (this.strategyForm.autoControl[index].type == "") {
    //     this.equipmentData = [];
    //   }
    //   this.eqForm.equipments = this.strategyForm.autoControl[index].value;
    //   this.eqForm.equipment_type = this.strategyForm.autoControl[index].type;
    //   this.getEquipmentType();
    //   this.chooseEq = true;
    // },
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
      autoEqTypeList(this.queryAnalogEqParams).then((res) => {
        this.eqTypeList = res.rows;
        console.log(this.eqTypeList, "触发器设备类型");
      });
    },
    //查询方向
    getDirection() {
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
        console.log(this.directionOptions, "方向");
      });
    },
    /** 查询设备类型列表 */
    // getEquipmentType() {
    //   let autoControl = this.strategyForm.autoControl;
    //   for (let i = 0; i < autoControl.length; i++) {
    //     listType(this.queryEqTypeParams).then((data) => {
    //       console.log(data.rows, "设备类型");
    //       this.$set(autoControl[i], "equipmentTypeData", data.rows);
    //       this.equipmentTypeData = data.rows;
    //     });
    //   }
    // },
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
      this.strategyForm.autoControl = [
        {
          value: "",
          state: "",
          type: "",
          equipmentTypeId: "",
          equipment: [],
          equipmentTypeData: [],
        },
      ];
    },
    // 取消按钮
    strategyFormClose() {
      let data = false;
      this.$emit("dialogVisibleClose",data);
      // this.$emit("dialogVisibleCloseEvent");
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
    // 关闭情报板窗口
    closeBoard(){
      this.$refs.boardRef.handleClosee();
    }
  },
};
</script>

<style>
/* .triggers .box .el-form-item__content {
  display: flex;
  justify-content: center;
  align-items: center;
} */
.triggers .box .el-form-item__content .el-form-item {
  margin-left: 4px;
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
