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
        label-width="80px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备类型:">
              {{ stateForm.typeName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属方向:">
              {{ getDirection(stateForm.eqDirection) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col> -->
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
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="24">
            <el-form-item label="IP:"> </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="控制模式:">
              <el-radio-group v-model="radioManual">
                <el-radio :label="1">闪烁</el-radio>
                <el-radio :label="2">常亮</el-radio>
                <el-radio :label="3">常灭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="车道:">
              <el-checkbox-group v-model="checkboxLane" class="checkboxLane">
                <el-checkbox label="1">左车道</el-checkbox>
                <el-checkbox label="2">右车道</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="白灯亮度:">
              <el-slider
                v-model="whiteLight"
                :show-tooltip="false"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="黄灯亮度:">
              <el-slider
                v-model="yellowLight"
                :show-tooltip="false"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="handleOK()"
          class="submitButton"
          v-hasPermi="['workbench:dialog:save']"
          >执 行</el-button
        >
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  <script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
import { controlDevice } from "@/api/workbench/config.js"; //提交控制信息

export default {
  data() {
    return {
      stateForm: {},
      title: "",
      visible: false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      radioManual: 1,
      checkboxLane: ["1"],
      whiteLight: 50,
      yellowLight: 50,
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
    };
  },
  created() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getMessage();
      this.visible = true;
    },
    // 查设备详情
    async getMessage() {
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
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
      this.visible = false;
    },
    handleOK() {
      const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      const param = {
        devId: this.stateForm.eqId, //设备id
        devType: this.eqInfo.clickEqType,
        radioManual: this.radioManual, //控制模式
        checkboxLane: this.checkboxLane, //车道
        whiteLight: this.whiteLight, //白灯亮度
        yellowLight: this.yellowLight, //黄灯亮度
      };
      controlDevice(param).then((response) => {
        console.log(response, "提交控制");
        this.visible = false;
        loading.close();
      });
    },
  },
};
</script>
  <style lang="scss" scoped>
::v-deep .el-radio-group {
  display: flex;
}
::v-deep .el-radio {
  padding: 5px 10px 5px 0px !important;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
::v-deep.sliderClass {
  .el-slider__runway {
    width: 50%;
    background-color: #006784;
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
