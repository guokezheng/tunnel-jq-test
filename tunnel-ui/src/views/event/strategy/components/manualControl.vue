<!-- 手动控制 -->
<template>
  <div >
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
        <el-row :gutter="20" style="clear: both">
          <el-col :span="24">
            <el-form-item label="执行操作">
              <div class="menu">
                <el-col :span="6">处置名称</el-col>
                <el-col :span="6">设备资源类型</el-col>
                <el-col :span="6">指定设备</el-col>
                <el-col :span="4">控制指令</el-col>
                <el-col :span="2">操作</el-col>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-row>
      <el-row class="planBox">
        <el-form-item
          v-for="(items, index) in strategyForm.manualControl"
          :key="index"
        >
          <el-col :span="6" style="padding-left: 0">
            <el-form-item prop="disposalName">
              <el-input
                :maxlength="50"
                v-model="items.disposalName"
                placeholder="处置名称"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-cascader
              v-model="items.equipmentTypeId"
              :options="equipmentTypeData"
              :props="equipmentTypeProps"
              :show-all-levels="false"
              @change="changeEquipmentType(index)"
              style="width: 100%"
            ></el-cascader>
<!--            <el-select
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
            </el-select>-->
          </el-col>
          <el-col :span="6">
            <el-select
              v-show="strategyForm.equipmentTypeId != 30"
              v-model="items.value"
              multiple
              collapse-tags
              placeholder="请选择设备"
              @change="qbgChange(index, items.value,false)"
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
            :span="4"
            v-show="items.equipmentTypeId != 16 && items.equipmentTypeId != 36 && items.equipmentTypeId != 7 && items.equipmentTypeId != 9"
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
            :span="4"
            v-show="items.equipmentTypeId == 16 || items.equipmentTypeId == 36"
          >

<!--            <el-input v-model="input" placeholder="请输入内容"></el-input>-->
<!--            <el-cascader
              :props="checkStrictly"
              v-model="items.state"
              :options="items.templatesList"
              :show-all-levels="false"
              clearable
              collapse-tags
              @change="handleChange"
            ></el-cascader>-->
<!--            <el-input v-model="items.content" placeholder="请选择模板" disabled>-->
            <el-input v-model="items.content" placeholder="请选择模板" readonly @click.native="openTemDialog(items)">
              <el-button slot="append" icon="el-icon-search" @click.stop="templateClick(index, index,items)"></el-button>
            </el-input>
          </el-col>
          <el-col
            :span="4"
            v-show="items.equipmentTypeId == 7 ||  items.equipmentTypeId == 9"
          >
            <el-select
              :style="{'width':  items.state == 1 ? '45%' :'100%' }"
              v-model="items.state"
              placeholder="请选择执行操作"
              @change="selectStateVal(index)"
            >
              <el-option
                v-for="item in items.manualControlStateList"
                :key="item.deviceState + 1"
                :label="item.stateName"
                :value="item.deviceState"
              >
              </el-option>
            </el-select>
            <el-input-number @change="$forceUpdate()" v-if="items.state == 1" v-model="items.stateNum" style="width: 55%"   :min="items.limitMin" :max="100" ></el-input-number>
          </el-col>
          <div class="buttonBox">
            <el-button class="delete" @click="removeItem(index)"></el-button>
            <el-button
              v-show="strategyForm.equipmentTypeId != 30"
              class="add"
              @click="addItem"
            ></el-button>
          </div>
        </el-form-item>
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
<!--      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleTem = false">取 消</el-button>
        &lt;!&ndash; <el-button type="primary" @click="dialogVisible = false">确 定</el-button> &ndash;&gt;
      </span>-->
    </el-dialog>
  </div>
</template>

<script>
import comBoard from "@/views/event/reservePlan/board";
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
} from "@/api/equipment/eqTypeState/api";
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
  handleStrategy, getCategoryTree,
} from "@/api/event/strategy";
import {selectVmsContent} from "@/api/information/api";
export default {
  name: "Plan",
  components: {
    comBoard,
  },
  data() {
    return {
      checkStrictly: {
        multiple: false,
        emitPath: false,
        checkStrictly: true,
      },
      templatesList: [], //模板数据
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      sink: "", //删除/修改
      id: "", //策略id
      strategyForm: {
        strategyState:1,// 策略状态
        jobRelationId: "", //时间戳
        strategyGroup: 1,
        strategyType: "0", //策略类型
        tunnelId: null, //隧道id
        strategyName: null, //策略名称
        direction: "", //方向
        manualControl: [
          {
            state: "",
            stateNum: 100,
            value: "",
            limitMin:1,
            equipmentTypeId: "",
            equipmentTypeData: [],
            equipmentData: [],
            templatesList: [],
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
          { max: 50, message: '最长输入50个字符', trigger: 'change' }

        ],
        equipmentTypeId: [
          { required: true, message: "请选择设备类型", trigger: "change" },
        ],
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
      this.$set(this.strategyForm.manualControl[data.index],'content',data.content);
      this.$set(this.strategyForm.manualControl[data.index],'state',data.id);
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

      if(this.strategyForm.manualControl[index].state == 1){
        this.$set(this.strategyForm.manualControl[index], "stateNum", 100);
        //基本照明限制 最低亮度为 30
        if(this.strategyForm.manualControl[index].equipmentTypeId == 9){
          this.$set(this.strategyForm.manualControl[index], "limitMin", 30);
        }

      }else{
        this.$set(this.strategyForm.manualControl[index], "stateNum", 0);
      }
    },
    init() {
      if (this.sink == "add") {
        this.resetForm();
        console.log("123");
      }
      console.log("init");
    //  this.getEquipmentType();
      this.getTunnels();
      this.getDirection();
    },
    // 组件调用方法;回显数据;
    async getStrategyData(row) {
      debugger
      console.log(row, "当前策略数据");
      await  getCategoryTree().then((data) => {
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
        // this.strategyForm = data;
        this.strategyForm.strategyName = data.strategyName;
        this.strategyForm.tunnelId = data.tunnelId;
        this.strategyForm.strategyType = data.strategyType;
        this.strategyForm.strategyState = data.strategyState;
        this.strategyForm.direction = data.direction;
        this.strategyForm.equipmentTypeId = data.equipmentTypeId;
        this.strategyForm.jobRelationId = data.jobRelationId;

        listRl({ strategyId: this.id }).then((response) => {
          console.log(response, "设备数据");
          this.strategyForm.manualControl = response.rows;
          for (let i = 0; i < response.rows.length; i++) {
            let attr = response.rows[i];
            let manualControl = this.strategyForm.manualControl[i];


            console.log(this.strategyForm.manualControl[i].value, "选择的设备");
            this.strategyForm.manualControl[i].state = attr.state;
            this.strategyForm.manualControl[i].stateNum = attr.stateNum;

            this.strategyForm.manualControl[i].manualControlStateList =
              attr.eqStateList;

            this.strategyForm.manualControl[i].equipmentTypeId = String(
              attr.eqTypeId
            );
            // 情报板设备
            if (
              this.strategyForm.manualControl[i].equipmentTypeId == 16 ||
              this.strategyForm.manualControl[i].equipmentTypeId == 36
            ) {
              // 改变数据类型
              this.strategyForm.manualControl[i].state = +attr.state;
              this.qbgChange(i, attr.equipments.split(","),true);
            }
            this.$set(
              manualControl,
              "equipmentTypeData",
              this.equipmentTypeData
            );

            //基本照明限制 最低亮度为 30
            if(this.strategyForm.manualControl[i].equipmentTypeId == 9){
              this.$set(this.strategyForm.manualControl[i], "limitMin", 30);
            }

            let params = {
              eqType: attr.eqTypeId, //设备类型
              eqTunnelId: this.strategyForm.tunnelId, //隧道
              eqDirection: this.strategyForm.direction, //方向
              params:{
                orderBy : 'eqName'
              }
            };
            // 选择双向，则不进行接口过滤条件
            if(this.strategyForm.direction == 3){
              params.eqDirection = null;
            }
            listDevices(params).then((res) => {
              this.$set(manualControl, "equipmentData", res.rows);
              console.log(manualControl.equipmentData, "设备列表数据1");
              console.log(attr.equipments.split(","))
              this.strategyForm.manualControl[i].value = attr.equipments.split(",");
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


      this.strategyForm.manualControl[index].stateNum = null;
      this.strategyForm.manualControl[index].state = "";
      this.strategyForm.manualControl[index].value = null;
      this.strategyForm.manualControl[index].content = null;
      let params = {
        eqType: this.strategyForm.manualControl[index].equipmentTypeId, //设备类型
        eqTunnelId: this.strategyForm.tunnelId, //隧道
        eqDirection: this.strategyForm.direction, //方向
        params:{
          orderBy : 'eqName'
        }
      };
      // 选择双向，则不进行接口过滤条件
      if(this.strategyForm.direction == 3){
        params.eqDirection = null;
      }

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
      if (
        this.strategyForm.manualControl[index].equipmentTypeId == 16 ||
        this.strategyForm.manualControl[index].equipmentTypeId == 36
      ) {
      }
    },
    //情报板设备改变事件
    qbgChange(index, value,flag) {
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
      if(!flag){
        this.strategyForm.manualControl[index].state = null;
        this.strategyForm.manualControl[index].content = null;
      }
      this.$forceUpdate();
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

      let params = {
        eqType: eqType,
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
          var manualControl = JSON.parse(
            JSON.stringify(this.strategyForm.manualControl)
          );
          let result = manualControl.every(function (item) {
            return (
              item.disposalName != "" && item.equipmentTypeId != "" && item.state != "" && item.value != "" &&
              item.disposalName != null && item.equipmentTypeId != null && item.state != null && item.value != null
            );
          });
          console.log(result);
          if (!result) {
            return this.$modal.msgError("请填写完整策略信息！");
          }
          //如果不是疏散标志则判断是否填写
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
        item.state = item.state.toString();
      });
      let params = this.strategyForm;
      updateStrategyInfo(params).then((res) => {
        this.$modal.msgSuccess("修改策略成功");
 /*       let data = true;
        this.$emit("dialogVisibleClose",data);*/
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
      let data = this.strategyForm.manualControl;
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
    // 改变隧道或者方向
    changeEvent(value) {
      console.log("当前选中了隧道："+this.strategyForm.tunnelId+"，方向：" + this.strategyForm.direction);

      this.equipmentTypeData = [];
      // 重置设备列表
      this.strategyForm.manualControl = [
        { state: "", value: "", equipmentTypeId: "" },
      ];

      if(this.strategyForm.tunnelId.length !=0 && this.strategyForm.direction.length !=0){
        this.getEquipmentType();
      }
      /* //给设备名称重新赋值
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
       }*/
    },
    removeItem(index) {
      console.log(index);
      if (this.strategyForm.manualControl.length == 1) {
        return this.$modal.msgWarning("至少保留一条执行操作");
      }
      this.strategyForm.manualControl.splice(index, 1);
    },

    // 添加执行操作
    addItem() {
      this.$refs["manualControl"].validate((valid) => {
        if (valid) {
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
        }
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
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      let manualControl = this.strategyForm.manualControl;
      for (let i = 0; i < manualControl.length; i++) {
    /*    listType(this.queryEqTypeParams).then((data) => {
          console.log(data.rows, "设备类型");
          this.$set(manualControl[i], "equipmentTypeData", data.rows);
          this.equipmentTypeData = data.rows;
        });*/
        getCategoryTree().then((data) => {
          console.log(data.rows, "设备类型");
          this.$set(manualControl[i], "equipmentTypeData", data.data);
          this.equipmentTypeData = data.data;
        });
      }
    },

    //表单重置方法
    resetForm() {
      this.$refs["manualControl"].resetFields();
      this.strategyForm.manualControl = [
        { state: "", value: "", equipmentTypeId: "", disposalName: "" },
      ];
    },
    strategyFormClose() {
      let data = false;
      this.$emit("dialogVisibleClose",data);
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
  height: 40px;

  .el-col {
    text-align: center;
  }
}
.buttonBox {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 36px;
  .delete,
  .add {
    width: 16px;
    height: 16px;
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100%;
    border: none;
    background-color: transparent;
  }
  .delete {
    background-image: url(../../../../assets/icons/delete.png);
  }
  .add {
    background-image: url(../../../../assets/icons/add.png);
  }
}
</style>
