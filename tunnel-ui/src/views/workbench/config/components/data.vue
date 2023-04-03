<template>
    <div style="width: 100%; height: 100%">
      <el-dialog
        v-dialogDrag
        class="workbench-dialog"
        :title="title"
        width="450px"
        append-to-body
        :visible="visible"
        :before-close="handleClosee"
      >
      <div class="dialogCloseButton"></div>
        <el-form
          ref="form"
          :model="stateForm"
          label-width="80px"
          label-position="left"
          size="mini"
        >
          <el-row>
            <el-col :span="13">
              <el-form-item label="设备类型:">
                {{ stateForm.typeName }}
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="隧道名称:">
                {{ stateForm.tunnelName }}
              </el-form-item>
            </el-col>

            <el-col :span="13">
              <el-form-item label="位置桩号:">
                {{ stateForm.pile }}
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="所属方向:">
                {{ getDirection(stateForm.eqDirection) }}
              </el-form-item>
            </el-col>

            <el-col :span="13">
              <el-form-item label="所属机构:">
                {{ stateForm.deptName }}
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="设备厂商:">
                {{ stateForm.supplierName }}
              </el-form-item>
            </el-col>

            <el-col :span="13">
              <el-form-item label="设备状态:"
              :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
                {{ geteqType(stateForm.eqStatus) }}
              </el-form-item>
            </el-col>
          </el-row>
          <!-- <el-row v-if="eqInfo.clickEqType == 45">
            <el-col >
              <div class="lineClass"></div>
              <el-form-item label="亮灯模式:" style="margin-top: 20px;">
                <el-select
                  v-model="stateForm2.lampType"
                  placeholder="请选择亮灯模式"
                  clearable
                  size="mini"
                >
                  <el-option
                    v-for="item in lampTypeList"
                    :key="item.deviceState"
                    :label="item.stateName"
                    :value="item.deviceState"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row> -->
          <div class="lineClass" v-if="eqInfo.clickEqType == 48" style="margin-bottom:10px"></div>
          <el-row v-if="eqInfo.clickEqType == 48">
            <el-col :span="13">
              <el-form-item label="振动速度值:" label-width="90px">
                {{ stateForm2.shakeSpeed }} <span v-show="stateForm2.shakeSpeed">mm/s</span>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="振动幅度值:" label-width="90px">
                {{ stateForm2.amplitude }} <span v-show="stateForm2.amplitude">μm</span>
              </el-form-item>
            </el-col>
            <el-col :span="13">
              <el-form-item label="沉降值:" label-width="90px">
                {{ stateForm2.subside }} <span v-show="stateForm2.subside">mm</span>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="倾斜值:" label-width="90px">
                {{ stateForm2.slope }} <span v-show="stateForm2.slope">°</span>
              </el-form-item>
            </el-col>
            <el-col :span="13">
              <el-form-item label="振动告警:" label-width="90px">
                {{ getshakeAlaram(stateForm2.shakeAlaram) }}
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="沉降倾斜告警:" label-width="100px">
                {{ getsubsideSlopeAlaram(stateForm2.subsideSlopeAlaram) }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <!-- <div slot="footer" style="float: right; margin-bottom: 20px" v-if="eqInfo.clickEqType == 45">
          <el-button
            type="primary"
            size="mini"
            @click="handleOK()"
            style="width: 80px"
            class="submitButton"
            v-hasPermi="['workbench:dialog:save']"
            >确 定</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="handleClosee()"
            style="width: 80px"
            >取 消</el-button
          >
        </div> -->
      </el-dialog>
    </div>
  </template>
  <script>
  import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
  import { controlWarningLightStripDevice, getFanSafeData  } from "@/api/workbench/config.js"; //提交控制信息
  import { getStateByData } from "@/api/equipment/eqTypeState/api.js"; //查询单选框弹窗信息

  export default {
    props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
    data() {
      return {
        stateForm: {},
        title: "",
        visible: true,
        titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
        lampTypeList:[],
        stateForm2:{},
      };
    },
    created() {
      this.getMessage();
      if(this.eqInfo.clickEqType == 45){
        this.getDataType()
      }
    },
    methods: {
      // 查设备详情
      async getMessage() {
        var that = this;
        if (this.eqInfo.equipmentId) {
          var obj = {};
          var state = "";
          // 查询单选框弹窗信息 -----------------------
          await getDeviceById(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "查询单选框弹窗信息");
            this.stateForm = res.data;
            this.title = this.stateForm.eqName;
            // this.stateForm2.lampType = res.data.eqStatus
            console.log(this.stateForm, "stateForm");
          });
          if(this.eqInfo.clickEqType ==48){
            await getFanSafeData(this.eqInfo.equipmentId).then((res) => {
              console.log(res,"风机")
              this.stateForm2 = res.data
            })
          }
        } else {
          this.$modal.msgWarning("没有设备Id");
        }
      },
      getshakeAlaram(type){
        if(type == 0){
          return '正常'
        }else if(type == 1){
          return '报警'
        }else if(type == 2){
          return '危险'
        }
      },
      getsubsideSlopeAlaram(type){
        if(type == 0){
          return '正常'
        }else if(type == 1){
          return '低限位报警'
        }else if(type == 2){
          return '高限位报警'
        }
      },
      getDataType(){
        const param = {
          stateTypeId:45,
          isControl:1
        }
        getStateByData(param).then((res)=>{
          console.log(res,"亮灯模式")
          this.lampTypeList = res.rows
        })
      },
      handleOK(){
        const param = {
          devId:this.eqInfo.equipmentId,
          state:this.stateForm2.lampType
        }
        controlWarningLightStripDevice(param).then((res)=>{
          console.log("警示灯带控制成功",res)
          if(res.data == 1){
            this.$modal.msgSuccess('操作成功');
            this.handleClosee()
          }else{
            this.$modal.msgError("操作失败");
            this.handleClosee()
          }
        })
      },
      getDirection(num) {
        for (var item of this.directionList) {
          if (item.dictValue == num) {
            return item.dictLabel;
          }
        }
      },
      getBrandName(num) {
        // 根据字典表查设备厂商--------------------------
        for (var item of this.brandList) {
          if (Number(item.dictValue) == num) {
            return item.dictLabel;
          }
        }
      },
      geteqType(num) {
      for (var item of this.eqTypeDialogList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
      // 关闭弹窗
      handleClosee() {
        this.$emit("dialogClose");
      },
    },
  };
  </script>
