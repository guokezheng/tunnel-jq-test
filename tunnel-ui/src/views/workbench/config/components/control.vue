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
        :close-on-click-modal="false"
        :modal="false"
      >
        <div class="dialogStyleBox">
          <div class="dialogLine"></div>
          <div class="dialogCloseButton"></div>
        </div>

        <el-form
          ref="form"
          :model="stateForm"
          label-width="90px"
          label-position="left"
          size="mini"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item label="设备类型:">
                {{ stateForm.typeName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隧道名称:" >
                {{ stateForm.tunnelName }}
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="位置桩号:">
                {{ stateForm.pile }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属方向:" >
                {{ getDirection(stateForm.eqDirection) }}
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="所属机构:">
                {{ stateForm.deptName }}
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12">
              <el-form-item label="设备厂商:" label-width="102px">
                {{ stateForm.supplierName }}
              </el-form-item>
            </el-col> -->
            <el-col :span="12" v-show="this.clickEqType == 45">
              <el-form-item label="设备IP:" >
                {{ stateForm.ip }}
              </el-form-item>
            </el-col>
            <el-col :span="12" v-show="[1,2,3,4,10,12].includes(this.clickEqType) && ipShow">
              <el-form-item label="控制器IP:" >
                {{ stateForm.f_ip }}
              </el-form-item>
            </el-col>
            <el-col :span="12" v-show="[1,2,3,4,10,12].includes(this.clickEqType) && !ipShow">
              <el-form-item label="控制器IP:" >
                {{ stateForm.mca_ip }}
              </el-form-item>
            </el-col>
            <el-col :span="12" v-show="[1,2,3,4,10,12,13,49].includes(this.clickEqType) && !ipShow">
              <el-form-item label="plcIP:" >
                {{ stateForm.f_ip }}
              </el-form-item>
            </el-col>
            <el-col :span="12" v-show="[30,31].includes(this.clickEqType) && !ipShow">
              <el-form-item label="主机IP:" >
                {{ stateForm.f_ip }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="设备状态:"
                :style="{
                  color:
                    stateForm.eqStatus == '1'
                      ? 'yellowgreen'
                      : stateForm.eqStatus == '2'
                      ? 'white'
                      : 'red',
                }"
              >
                {{ geteqType(stateForm.eqStatus) }}
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12" v-if="stateForm.eqType == 13">
              <el-form-item label="消防栓状态:">
                {{ stateForm.xfsStatus }}
              </el-form-item>
            </el-col> -->
          </el-row>
          <div class="lineClass"></div>
          <div style="margin-top: 10px">
            <el-form-item label="配置状态:">
              <div class="wrap">
                <el-radio-group
                  v-for="(item, index) in eqTypeStateList"
                  :key="index"
                  v-model="stateForm.state"
                  style="display: flex; flex-direction: column"
                  @change="$forceUpdate()"
                >
                  <el-radio
                    v-if="stateForm.eqType == item.type && item.control == 1"
                    class="el-radio flex-row"
                    :label="item.state"
                    style="align-items: center"
                    :class="[
                      String(stateForm.state) == String(item.state)
                        ? 'el-radio-selcted'
                        : '',
                    ]"
                  >
                    <el-row
                      class="flex-row"
                      v-if="
                        stateForm.eqDirection == '1' &&
                        (stateForm.eqType == 1 || stateForm.eqType == 2)
                      "
                    >
                      <img
                        :width="iconWidth"
                        :height="iconHeight"
                        :src="item.url[0]"
                      />
                      <img
                        :width="iconWidth"
                        :height="iconHeight"
                        :src="item.url[1]"
                        v-if="item.url[1]"
                      />
                      <div style="margin: 0 0 0 10px; display: inline-block">
                        {{ item.name }}
                      </div>
                    </el-row>
                    <el-row
                      class="flex-row"
                      v-if="
                        stateForm.eqDirection == '2' &&
                        (stateForm.eqType == 1 || stateForm.eqType == 2)
                      "
                    >
                      <img
                        :width="iconWidth"
                        :height="iconHeight"
                        :src="item.url[1]"
                        v-if="item.url.length > 1"
                      />
                      <img
                        :width="iconWidth"
                        :height="iconHeight"
                        :src="item.url[0]"
                      />
                      <div style="margin: 0 0 0 10px; display: inline-block">
                        {{ item.name }}
                      </div>
                    </el-row>
                    <el-row
                      class="flex-row"
                      v-if="stateForm.eqType != 1 && stateForm.eqType != 2"
                    >
                      <img
                        :width="iconWidth"
                        :height="iconHeight"
                        :src="item.url[0]"
                        :style="{width:clickEqType == 31?'110px':'auto'}"
                      />

                      <div style="margin: 0 0 0 10px; display: inline-block">
                        {{ item.name }}
                      </div>
                    </el-row>
                  </el-radio>
                </el-radio-group>
              </div>
            </el-form-item>
            <!-- 加强照明：7  基本照明：9 疏散标志：30 诱导灯：31 警示灯带：45 -->
            <el-row v-show="clickEqType == 30">
              <el-col :span="11">
                <el-form-item v-show="showTipe == true" label-width="10px">
                  <span style="color: red; font-weight: bold"
                    >当前地址为报警点位</span
                  >
                </el-form-item>
              </el-col>
            </el-row>
            <el-row
              style="margin-top: 10px"
              v-show="[7, 9, 30, 31].includes(this.clickEqType)"
            >
              <el-col :span="15">
                <el-form-item label="亮度调整">
                  <el-slider
                    v-model="stateForm.brightness"
                    :max="100"
                    :min="brightnessMin"
                    class="sliderClass"
                    :disabled="!stateForm.brightness"
                  ></el-slider>
                </el-form-item>
              </el-col>
              <el-col :span="9" >
                <span style="padding-left: 10px; line-height: 30px"
                  >{{ stateForm.brightness }} %</span
                >
              </el-col>
            </el-row>
            <el-row v-show="(this.clickEqType == 30 && stateForm.state == 5) || (this.clickEqType == 31)" style="margin-top: 10px" >
              <el-col :span="15">
                <el-form-item label="闪烁频率:">
                  <el-slider
                    v-model="stateForm.frequency"
                    :max="100"
                    :min="frequencyMin"
                    class="sliderClass"
                    :disabled="!stateForm.frequency"
                  ></el-slider>
                </el-form-item>
              </el-col>
              <el-col :span="9" >
                <span style="padding-left: 10px; line-height: 30px"
                  >{{ stateForm.frequency }} m/s</span
                >
              </el-col>
            </el-row>
            <div slot="footer" style="margin-top: 10px" class="dialog-footer">
              <el-button
                @click="handleOK()"
                class="submitButton"
                v-hasPermi="['workbench:dialog:save']"
                >执 行</el-button
              >
              <el-button class="closeButton" @click="handleClosee()"
                >取 消</el-button
              >
            </div>
          </div>
        </el-form>
      </el-dialog>
    </div>
  </template>

    <script>
  import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
  import { getType } from "@/api/equipment/type/api.js"; //查询设备图标宽高
  import { getDevice, setBrightness } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
  import { getStateByData } from "@/api/equipment/eqTypeState/api"; //查询设备状态图标
  import {
    controlDevice,
    controlWarningLightStripDevice,
    setControlDeviceByParam,
    controlEvacuationSignDevice,
    controlGuidanceLampDevice
  } from "@/api/workbench/config.js"; //提交控制信息

  export default {
    data() {
      return {
        title: "",
        stateForm: {
          brightness: 0,
        }, //弹窗表单
        eqTypeStateList: [],
        visible: false,
        titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
        iconWidth: "",
        iconHeight: "",
        clickEqType: "",
        brandList: [],
        eqInfo: {},
        eqTypeDialogList: [],
        directionList: [],
        brightnessMin: 1,
        frequencyMin: 1,
        fireMarkData: [],
        showTipe: false,
        ipShow:false,
      };
    },
    watch: {
      "stateForm.state": function (newVal, oldVal) {
        if([7, 9].includes(this.clickEqType)){
        // 基础照明、加强照明  state == 1 开启  state == 2  关闭
          if(newVal == "1" && this.stateForm.brightness == 0){
            this.stateForm.brightness = 1;
            this.brightnessMin = 1;
            this.frequencyMin = 1;
          }else if(newVal == "2" || newVal == ''){
            this.stateForm.brightness = 0;
            this.brightnessMin = 0;
          }
        }else if([30, 31].includes(this.clickEqType)){
           // 疏散标志 state == 1 关闭 state == 2 常亮 state == 5 报警
           // 诱导灯 state == 1 关闭 state == 2 同步单山 state == 3 逆向流水
           if (newVal == "1" || newVal == '') {
            this.stateForm.brightness = 0;
            this.stateForm.frequency = 0;
            this.brightnessMin = 0;
            this.frequencyMin = 0
          } else if (newVal != "1") {
            if(this.stateForm.brightness == 0){
              this.brightnessMin = 1;
              this.stateForm.brightness = 1;
            }
            if(this.stateForm.frequency == 0){
              this.frequencyMin = 1
              this.stateForm.frequency = 1;
            }
          }
        }
        // else if(this.clickEqType == 45){
        //   // 警示灯带 红黄绿灯
        //   if(newVal == ""){
        //     this.brightnessMin = 0;
        //     this.stateForm.brightness = 0;
        //   }else if(newVal && this.stateForm.brightnes == 0){
        //     this.stateForm.brightness = 1;
        //     this.brightnessMin = 1;
        //   }
        // }
      },
    },
    methods: {
      init(eqInfo, brandList, directionList, eqTypeDialogList) {

        this.eqInfo = eqInfo;
        this.brandList = brandList;
        this.directionList = directionList;
        this.eqTypeDialogList = eqTypeDialogList;
        this.clickEqType = JSON.parse(JSON.stringify(this.eqInfo.clickEqType));
        this.getEqTypeStateIcon();
        this.getMessage();
      },
      // 查设备详情
      async getMessage() {
        if (this.eqInfo.equipmentId) {
          let form = {}
          // 查询单选框弹窗信息 -----------------------
          await getDeviceById(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "查询单选框弹窗信息");
            // debugger
            form = JSON.parse(JSON.stringify(res.data));
            this.title = form.eqName;
            form.frequency = typeof(form.frequency) == "string"?Number(form.frequency):form.frequency;
            form.brightness = typeof(form.brightness) == "string"? Number(form.brightness):form.brightness;
            this.stateForm = form
            console.log( this.stateForm.state, "查询单选框弹窗信息");
            if(res.data.tunnelId == "JQ-JiNan-WenZuBei-MJY" || res.data.tunnelId == 'JQ-WeiFang-JiuLongYu-HSD'){
              this.ipShow = true
            }else{
              this.ipShow = false
            }
            // console.log(this.ipShow,"this.ipShow")
            // 查询设备当前状态 --------------------------------
            // getDevice(this.eqInfo.equipmentId).then((response) => {
            //   console.log(response, "查询设备当前状态");
            //   this.stateForm.state = response.data.state;

            // });
            if (this.eqInfo.clickEqType == 30) {
              this.fireMarkData = [
                {
                  label: "设置为报警点位",
                  value: this.stateForm.query_point_address,
                },
              ];
              console.log(this.stateForm.query_point_address)
              if (this.stateForm.pile == this.stateForm.fireMark) {
                this.fireMarkData.push({ label: "清除报警点位", value: "255" });
                this.showTipe = true;
              } else {
                this.showTipe = false;
              }
            }
          });
        } else {
          this.$modal.msgWarning("没有设备Id");
        }
      },
      /* 查询设备状态图标*/
      async getEqTypeStateIcon() {
        let that = this;
        await getType(this.eqInfo.clickEqType).then((res) => {
          console.log(res, "查询设备图标宽高");
          this.iconWidth = res.data.iconWidth;
          this.iconHeight = res.data.iconHeight;
        });
        let list = [];
        const param = {
          stateTypeId: this.eqInfo.clickEqType,
          isControl: 1,
        };
        await getStateByData(param).then((response) => {
          console.log(response, "查询设备状态图标");
          list = response.rows;
        });

        that.eqTypeStateList = [];
        for (let i = 0; i < list.length; i++) {
          let iconUrl = [];
          if (list[i].iFileList != null) {
            for (let j = 0; j < list[i].iFileList.length; j++) {
              let img = list[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          // debugger
          that.eqTypeStateList.push({
            type: list[i].stateTypeId,
            state: list[i].deviceState,
            name: list[i].stateName,
            control: list[i].isControl,
            url: iconUrl,
          });
        }
        console.log(that.eqTypeStateList, "that.eqTypeStateList");
        this.visible = true;
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
      handleOK() {
        let that = this;

        // 警示灯带
        if (this.eqInfo.clickEqType == 45) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          const param = {
            devId: this.eqInfo.equipmentId,
            state: this.stateForm.state,
            // brightness: this.stateForm.brightness,
          };
          controlWarningLightStripDevice(param).then((res) => {
            console.log("警示灯带控制成功", res);
            if (res.data == 1) {
              this.$modal.msgSuccess("操作成功");
            } else {
              this.$modal.msgError("操作失败");
            }
            loading.close();
          }).catch(()=>{
            loading.close();
          });
          // 消防水泵:13 潜水深井泵:49
        } else if (
          this.eqInfo.clickEqType == 13 ||
          this.eqInfo.clickEqType == 49
        ) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          const param = {
            devId: this.stateForm.eqId, //设备id
            state: this.stateForm.state,
            // comType: "omron",
          };
          setControlDeviceByParam(param).then((res) => {
            console.log("消防栓控制成功", res);
            // let msg = res.msg;
            if (res.data == 1) {
              this.$modal.msgSuccess("控制成功");
            } else {
              this.$modal.msgError("控制失败");
            }
            loading.close();
          }).catch(()=>{
            loading.close();
          });
        } else if (this.stateForm.eqType == 30) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          const param = {
            devId: this.stateForm.eqId, //设备id
            state: this.stateForm.state, //设备状态
            brightness: this.stateForm.brightness, //疏散标志亮度
            frequency: this.stateForm.frequency, //疏散标志频率
            fireMark: // 关闭传0 常亮传255 报警传3
              this.stateForm.state == "1"
                ? "0"
                : this.stateForm.state == "2"
                ? "255"
                : this.stateForm.pile,
          };
          this.$modal.msgSuccess("指令下发中，请稍后。");
          controlEvacuationSignDevice(param).then((response) => {
            console.log(response, "提交控制");
            loading.close();
            this.$modal.msgSuccess("操作成功");
          }).catch(()=>{
            loading.close();
          });
        } else if (this.stateForm.eqType == 31) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          const param = {
            devId: this.stateForm.eqId, //设备id
            state: this.stateForm.state, //设备状态
            brightness: this.stateForm.brightness, //诱导灯亮度
            frequency: this.stateForm.frequency, //诱导灯频率
            eqType: this.stateForm.eqType,

          };
          this.$modal.msgSuccess("指令下发中，请稍后。");
          controlGuidanceLampDevice(param).then((response) => {
            console.log(response, "提交控制");
            loading.close();
            this.$modal.msgSuccess("操作成功");
          }).catch(()=>{
            loading.close();
          });
        } else {
          if (
            this.stateForm.eqType == 9 &&
            this.stateForm.state == 1 &&
            this.stateForm.brightness < 30
          ) {
            this.$modal.msgWarning("基本照明亮度不得低于30");
            return;
          }
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          const param = {
            devId: this.stateForm.eqId, //设备id
            state: this.stateForm.state,
            brightness: this.stateForm.brightness,
            eqType: this.stateForm.eqType,
          };
          controlDevice(param).then((response) => {
            if (response.data == 0) {
              this.$modal.msgError("下发失败");
            } else if (response.data == 1) {
              this.$modal.msgSuccess("下发成功");
            }
            loading.close();
          }).catch(()=>{
            loading.close();

          });
        }
        this.visible = false;
      },
      // 关闭弹窗
      handleClosee() {
        this.visible = false;
      },
    },
  };
  </script>

    <style lang="scss" scoped>
  .videoTabs {
    padding: 0 15px;
  }
  .el-row {
    margin-bottom: -10px;
    display: flex;
    flex-wrap: wrap;
  }
  .el-radio-selcted {
    padding: 5px 300px 5px 20px;
    margin: 2px 0px;
    color: #c0ccda;
    border-radius: 4px;
    background-color: #455d79;
  }

  .el-radio-button--medium .el-radio-button__inner {
    padding: 5px 10px;
  }
  ::v-deep .el-radio__label {
    height: 40px !important;
  }
  .flex-row {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    height: 40px;
    align-items: center;
  }
  .el-radio {
    width: 240px;
    height: 40px;
    line-height: 40px;
  }
  ::v-deep.sliderClass {
    .el-slider__runway {
      width: 100%;
      margin: 12px 0;
    }
    .el-slider__bar {
      background: linear-gradient(90deg, #00aded 0%, #007cdd 100%);
    }
    .el-slider__button {
      width: 10px;
      height: 10px;
      border: solid 1px #fff;
      background-color: #ff9300;
    }
  }
  ::v-deep .el-dialog {
    pointer-events: auto !important;
  }
  </style>
