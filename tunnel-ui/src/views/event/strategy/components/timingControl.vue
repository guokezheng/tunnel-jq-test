<template>
  <div>
    <el-form
      ref="timingControl"
      :model="strategyForm"
      :rules="formDataValidator"
      label-width="100px"
      height="700px"
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
      <el-row >
        <el-col :span="14">
          <el-form-item label="执行日期" prop="execDate">
            <el-date-picker
              v-model="strategyForm.execDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择日期"
              :picker-options="forbiddenTime"
              @change="changeTime"
              style="width: calc(100% - 155px);">
            </el-date-picker>
            <i style="color: red;margin-left: 10px;">不选择日期则每日执行</i>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="执行时间" prop="execTime">
            <el-time-picker
              v-model="strategyForm.execTime"
              placeholder="请选择时间"
              value-format="HH:mm:ss"
              @change="changeTime"
              style="width:100%"
            >
            </el-time-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <!--        <el-col :span="24">
                <el-form-item
                  label="定时频率"
                  prop="schedulerTime"
                  style="width: 100%"
                >
                  <el-input
                    v-model="strategyForm.schedulerTime"
                    placeholder="请输入cron执行表达式"
                  >
                    <template slot="append">
                      <el-button type="primary" @click="handleShowCron">
                        生成表达式
                        <i class="el-icon-time el-icon&#45;&#45;right"></i>
                      </el-button>
                    </template>
                  </el-input>
                </el-form-item>
              </el-col>-->
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
      <el-row :gutter="20" style="clear:both; height: 28vh;
        overflow: auto;     margin-bottom: 0px;">
        <el-col :span="24">
          <el-form-item
            v-for="(items, index) in strategyForm.autoControl"
            :key="index"
          >
            <el-col :span="6" style="padding-left:0">

              <el-cascader
                v-model="items.equipmentTypeId"
                :options="equipmentTypeData"
                :props="equipmentTypeProps"
                :show-all-levels="false"
                @change="changeEquipmentType(index)"
                style="width: 100%"
              ></el-cascader>
<!--              <el-select
                v-model="items.equipmentTypeId"
                placeholder="请选择设备类型"
                clearable
                @change="changeEquipmentType(index)"
                style="width:100%"
              >
                <el-option
                  v-for="item in items.equipmentTypeData"
                  :key="item.typeId"
                  :label="item.typeName"
                  :value="item.typeId"
                />
              </el-select>-->
            </el-col>
            <el-col :span="8">
              <el-select
                v-model="items.equipments"
                multiple
                collapse-tags
                placeholder="请选择设备"
                @change="qbgChange(index, items.equipments,false)"
                style="width:100%"
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
            <el-col :span="8"
              v-show="items.equipmentTypeId != 16 && items.equipmentTypeId != 36 && items.equipmentTypeId != 7 && items.equipmentTypeId != 9"
            >
              <el-select v-model="items.state" placeholder="请选择设备执行操作" style="width:100%">
                <el-option
                  v-for="(item, indx) in items.eqStateList"
                  :key="item.deviceState"
                  :label="item.stateName"
                  :value="item.deviceState"
                >
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8"
              v-show="items.equipmentTypeId == 16 || items.equipmentTypeId == 36"
            >
<!--              <el-cascader
                :props="checkStrictly"
                v-model="items.state"
                :options="items.templatesList"
                :show-all-levels="false"
                clearable
                collapse-tags
                style="width:100%"
                @change="handleChange"
              ></el-cascader>-->
              <el-input v-model="items.content" placeholder="请选择模板" readonly @click.native="openTemDialog(items)">
                <el-button slot="append" icon="el-icon-search" @click.stop="templateClick(index, index,items)"></el-button>
              </el-input>
            </el-col>
            <el-col
              :span="8"
              v-show="items.equipmentTypeId == 7 ||  items.equipmentTypeId == 9"
            >
              <el-select
                :style="{'width':  items.state == 1 ? '45%' :'100%' }"
                v-model="items.state"
                placeholder="请选择执行操作"
                @change="selectStateVal(index)"
              >
                <el-option
                  v-for="item in items.eqStateList"
                  :key="item.deviceState + 1"
                  :label="item.stateName"
                  :value="item.deviceState"
                >
                </el-option>
              </el-select>
              <el-input-number v-if="items.state == 1" v-model="items.stateNum" style="width: 55%"   :min="items.limitMin" :max="100" ></el-input-number>
            </el-col>
            <el-col :span="2" class="buttonBox">
              <el-button
                class="delete"
                @click="removeItem(index)"
              ></el-button>
              <el-button
                class="add"
                @click="addItem"
              ></el-button>
            </el-col>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button class="submitButton" @click="submitStrategyForm"
      >提交</el-button
      >
      <el-button class="closeButton" @click="strategyFormClose"
      >取 消</el-button
      >
    </div>
<!--    <div class="dialog-footer" slot="footer">-->
<!--      <el-button class="submitButton" @click="submitStrategyForm"-->
<!--      >提交</el-button-->
<!--      >-->
<!--      <el-button class="closeButton" @click="strategyFormClose"-->
<!--      >取 消</el-button-->
<!--      >-->
<!--    </div>-->
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
    <com-board class="comClass" ref="boardRef" @getVmsData="getMsgFormSon"></com-board>
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
import comBoard from "@/views/event/reservePlan/board";
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
} from "@/api/equipment/eqTypeState/api";
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
  handleStrategy, getCategoryTree,
} from "@/api/event/strategy";
import { listRl, addRl } from "@/api/event/strategyRl";
import {selectVmsContent} from "@/api/information/api";
export default {
  model: {
    prop: "cronValue",
    event: "change",
  },
  components: {
    Crontab,
    comBoard,
  },
  dicts: ["sys_job_group", "sys_job_status"],
  data() {
    return {
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      expression: "",
      paramsData: {
        tunnelId: "",
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
        strategyState:1,// 策略状态
        strategyGroup: 1,
        schedulerTime: "", //cron数据
        strategyType: "1", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        execDate: "",
        execTime:"",
        autoControl: [
          {
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            limitMin:1,
            stateNum: 100,
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
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
      forbiddenTime: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7; // 不可选历史天，可选当前天，可选未来天
        }
      },

      formDataValidator: {
        direction: [{ required: true, message: "请选择隧道方向", trigger: "blur" }],
        tunnelId: [
          { required: true, message: "请选择隧道", trigger: "change" },
        ],
        strategyName: [
          { required: true, message: "请输入策略名称", trigger: "change" },
          { max: 50, message: '最长输入50个字符', trigger: 'change' }
        ],
    /*    schedulerTime:[
          { required: true, message: "请输入定时频率", trigger: "change" }
        ],*/
        execTime: [
          { required: true, message: "请选择执行时间", trigger: "change" },
        ]
      },
      templateData:{},
      dialogVisibleTem:false,
    };
  },
  methods: {
    handleClose(){
      this.dialogVisibleTem = false
    },
    //查看情报板信息
    openTemDialog(item){
      let params = {id: item.id,state:item.state,type:'1'};
      console.log(item);
      console.log(item)
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
    getMsgFormSon(data){
      this.$set(this.strategyForm.autoControl[data.index],'content',data.content);
      this.$set(this.strategyForm.autoControl[data.index],'state',data.id);
    },
    // 情报板选择模板点击事件
    templateClick(number, index,item){
      this.$refs.boardRef.init(
        number,
        index,
        item.equipmentTypeId,
      );
    },
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
    changeTime(){
      let date = this.strategyForm.execDate + " " + this.strategyForm.execTime;
      let dateTime = new Date(date).getTime();
      if(this.strategyForm.execDate && this.strategyForm.execTime && dateTime < new Date()){
        this.$modal.msgWarning("执行时间不得早于当前时间");
        this.strategyForm.execDate = null;
        this.strategyForm.execTime = null;
        return false;
      }
      return  true;
    },

    init() {
      if (this.sink == "add") {
        this.resetForm();
        this.$nextTick(() => {
          this.showCronBox = false;
          // this.$refs.cron.checkClear();
        });
      }
  //    this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },
    async getStrategyData(row) {
      debugger
      console.log(row, "当前策略数据");
      await  getCategoryTree().then((data) => {
        let dataNum = 0;
        for (let j = 0; j < data.data.length; j++) {
          if( data.data[j].label=="巡检机器人"){
            dataNum = j
            break;
          }
        }
        data.data.splice(dataNum, 1);
        this.equipmentTypeData = data.data;
      });
      getStrategy(this.id).then((response) => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
          target:'.strategy-dialog',
        });
        let data = response.data;
        this.strategyForm.id = data.id;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.direction = data.direction;
        this.strategyForm.strategyState = data.strategyState;
        // this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;
        this.strategyForm.schedulerTime = data.schedulerTime;
        this.strategyForm.execDate = data.execDate;
        this.strategyForm.execTime = data.execTime;
        listRl({ strategyId: row.id }).then((response) => {
          this.strategyForm.equipmentTypeId = response.rows[0].eqTypeId;
          let params = {
            eqType: response.rows[0].eqTypeId,
            eqTunnelId: this.strategyForm.tunnelId,
            eqDirection: this.strategyForm.direction, //方向
            params:{
              orderBy : 'eqName'
            }
          }
          if(this.strategyForm.direction == 3){
            params.eqDirection = null;
          }
          listDevices(params).then((res) => {
            this.equipmentData = res.rows;
          });
          console.log(response.rows, "设备列表");
          this.strategyForm.autoControl = response.rows;
          for (var i = 0; i < response.rows.length; i++) {
            let autoControl = this.strategyForm.autoControl[i];
            var attr = response.rows[i];
            console.log(attr.eqStateList)
            debugger
            this.strategyForm.autoControl[i].eqStateList = attr.eqStateList;
            this.strategyForm.autoControl[i].state = attr.state;
            this.strategyForm.autoControl[i].stateNum = attr.stateNum;
            this.strategyForm.autoControl[i].type = attr.eqTypeId;
            this.strategyForm.autoControl[i].equipmentTypeId = String(
              attr.eqTypeId
            );

            let  equipmentArray = attr.equipments.split(",");
            if (
              this.strategyForm.autoControl[i].equipmentTypeId == 16 ||
              this.strategyForm.autoControl[i].equipmentTypeId == 36
            ) {
              this.strategyForm.autoControl[i].state = +attr.state;
              this.qbgChange(i,equipmentArray,true);
            }
            this.$set(autoControl, "equipmentTypeData", this.equipmentTypeData);

            //基本照明限制 最低亮度为 30
            if(this.strategyForm.autoControl[i].equipmentTypeId == 9){
              this.$set(this.strategyForm.autoControl[i], "limitMin", 30);
            }


            let params = {
              eqType: attr.eqTypeId,
              eqTunnelId: this.strategyForm.tunnelId,
              eqDirection: this.strategyForm.direction, //方向
              params:{
                orderBy : 'eqName'
              }
            };
            if(this.strategyForm.direction == 3){
              params.eqDirection = null;
            }
            listDevices(params).then((res) => {
              this.$set(autoControl, "equipmentData", res.rows);
              console.log(autoControl.equipmentData, "设备列表数据1");
              this.$set(autoControl, "equipments", equipmentArray);
              //this.strategyForm.autoControl[i].equipments = equipmentArray;

            });
          }
        });
        setTimeout(() => {
          loading.close();
        }, 1700);
      });


    },
    // 改变设备类型
    changeEquipmentType(index) {
      debugger
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
    qbgChange(index, value,flag) {
      console.log(value, "当前选中板子");
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
      if(!flag){
        // 设备联控，命令重置
        this.strategyForm.autoControl[index].state = null;
        this.strategyForm.autoControl[index].content = null;
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
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }
      debugger
      listEqTypeStateIsControl(params).then((response) => {
        debugger
        console.log(response.rows)
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
      let params = {
        eqType: eqType,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
        params:{
          orderBy : 'eqName'
        }
      };
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }
      listDevices(params).then((res) => {
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
          let response = JSON.parse(JSON.stringify(autoControl))
          console.log(response,"response")
          let result = response.every(function (item) {
            return item.equipmentTypeId != "" && item.state != "" && item.equipments != "" &&
            item.equipmentTypeId != null && item.state != null && item.equipments != null
          });
          console.log(result);
          if(!result){
            return this.$modal.msgError("请填写完整策略信息！");
          }

          // 上报数据前，检查执行时期
          if(!this.changeTime()){
            return false;
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
      let data = this.strategyForm.autoControl;
      data.forEach((item) => {
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
/*        this.$emit("dialogVisibleClose");*/
        this.$emit("refreshList");
      });
    },
    // 提交保存方法
    async addStrategyInfoData() {
      this.strategyForm.id = null;
      this.strategyForm.strategyState = 1;
      await getGuid().then((res) => {
        this.strategyForm.jobRelationId = res;
      });
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
    // 改变设备类型或者方向
    changeEvent(value) {

      console.log("当前选中了隧道："+this.strategyForm.tunnelId+"，方向：" + this.strategyForm.direction);

      this.equipmentTypeData = [];

      // 重置设备列表
      this.strategyForm.autoControl = [
        { state: "", value: "", equipmentTypeId: "" },
      ];
      if(this.strategyForm.tunnelId.length !=0 && this.strategyForm.direction.length !=0){
        //this.listEqTypeStateIsControl();
        this.getEquipmentType();
      }

      //给设备名称重新赋值
      /*let params = {
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
      }*/
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
      // let list = this.strategyForm.autoControl;
      // var flag = list.every(function (item) {
      //   return item.value != "" || item.state != "" || item.type != "";
      // });
      // console.log(flag);
      // if (flag == false) {
      //   return this.$modal.msgError("请选择设备并添加执行操作");
      // }
      this.$refs["timingControl"].validate((valid) => {
        if (valid) {
          this.addCf();
         this.strategyForm.autoControl.push({
            value: "", //设备
            state: "", //状态
            type: "", //设备分类
            equipmentTypeId: "", //设备类型
            equipments: [], //设备列表
            equipmentTypeData: [],
            equipmentData: [],
          });
          this.getEquipmentType();
           }
        });
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

      var params ={
        eqType: eqType,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
        params:{
          orderBy : 'eqName'
        }
      }
      // 选择双向，则不进行接口过滤条件
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }
      listDevices(params).then((res) => {
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

      let params = {
        eqType: this.strategyForm.equipmentTypeId,
        eqTunnelId: this.strategyForm.tunnelId,
        eqDirection: this.strategyForm.direction,
        params:{
          orderBy : 'eqName'
        }
      };
      // 选择双向，则不进行接口过滤条件
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }
      listDevices(params).then((res) => {
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
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    getDirection() {
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      let autoControl = this.strategyForm.autoControl;
      for (let i = 0; i < autoControl.length; i++) {
        getCategoryTree().then((data) => {
          let dataNum = 0;
          for (let j = 0; j < data.data.length; j++) {
            if( data.data[j].label=="巡检机器人"){
              dataNum = j
              break;
            }
          }
          data.data.splice(dataNum, 1);
          this.$set(autoControl[i], "equipmentTypeData", data.data);
          this.equipmentTypeData = data.data;
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
      this.strategyForm.autoControl = [{
        value: "", //设备
        state: "", //状态
        type: "", //设备分类
        equipmentTypeId: "", //设备类型
        equipments: [], //设备列表
        equipmentTypeData: [],
        equipmentData: [],
      }];
    },
    strategyFormClose() {
      let data = false;
      this.$emit("dialogVisibleClose",data);
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
