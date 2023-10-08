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
        <div
          class="lineClass"
          v-if="[41, 42, 48].includes(eqInfo.clickEqType)"
        ></div>
        <!-- 48：风机内外震动   -->
        <el-row v-if="eqInfo.clickEqType == 48">
          <el-col :span="13">
            <el-form-item label="振动速度值:" label-width="90px">
              {{ stateForm2.shakeSpeed }}
              <span v-show="stateForm2.shakeSpeed">mm/s</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="振动幅度值:" label-width="90px">
              {{ stateForm2.amplitude }}
              <span v-show="stateForm2.amplitude">μm</span>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="沉降值:" label-width="90px">
              {{ stateForm2.subside }}
              <span v-show="stateForm2.subside">mm</span>
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
        <!-- 41：温湿度传感器 -->
        <el-row v-if="eqInfo.clickEqType == 41">
          <el-col :span="13">
            <el-form-item label="温度:" label-width="90px">
              {{ stateForm2.slope }}
              <span v-show="stateForm2.temperature">°</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="湿度:" label-width="90px">
              {{ stateForm2.slope }} <span v-show="stateForm2.humidity">°</span>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 42:水浸液位传感器 -->
        <el-row v-if="eqInfo.clickEqType == 42">
          <el-col :span="13">
            <el-form-item label="液位:">
              {{ stateForm2.level }} <span style="padding-left: 5px" v-show="stateForm2.level">m³</span>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="13">
            <el-form-item label="电流Ia:">
              {{ stateForm.ia }} <span style="padding-left: 5px">I</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="电流Ib:">
              {{ stateForm.ib }} <span style="padding-left: 5px">I</span>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="电流Ic:">
              {{ stateForm.ic }} <span style="padding-left: 5px">I</span>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="电压Uab:">
              {{ stateForm.va }}<span style="padding-left: 5px">V</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="电压Ubc:">
              {{ stateForm.vb }} <span style="padding-left: 5px">V</span>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="电压Uac:">
              {{ stateForm.vc }} <span style="padding-left: 5px">V</span>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>
  <script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
import {
  controlWarningLightStripDevice,
  getFanSafeData,
  getLevelData
} from "@/api/workbench/config.js"; //提交控制信息
import { getStateByData } from "@/api/equipment/eqTypeState/api.js"; //查询单选框弹窗信息

export default {
  data() {
    return {
      stateForm: {},
      title: "",
      visible: false,
      stateForm2: {},
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
          console.log(res, "查询单选框弹窗信息111");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          // this.stateForm2.lampType = res.data.eqStatus
          console.log(this.stateForm, "stateForm");
        });
        if (this.eqInfo.clickEqType == 48) {
          await getFanSafeData(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "风机内外振动仪");
            this.stateForm2 = res.data;
          });
        }else if(this.eqInfo.clickEqType == 42){
          await getLevelData(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "水浸传感器");
            this.stateForm2 = res.data;
          });
        }
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    getshakeAlaram(type) {
      if (type == 0) {
        return "正常";
      } else if (type == 1) {
        return "报警";
      } else if (type == 2) {
        return "危险";
      }
    },
    getsubsideSlopeAlaram(type) {
      if (type == 0) {
        return "正常";
      } else if (type == 1) {
        return "低限位报警";
      } else if (type == 2) {
        return "高限位报警";
      }
    },
    handleOK() {
      const param = {
        devId: this.eqInfo.equipmentId,
        state: this.stateForm2.lampType,
      };
      controlWarningLightStripDevice(param).then((res) => {
        console.log("警示灯带控制成功", res);
        if (res.data == 1) {
          this.$modal.msgSuccess("操作成功");
          this.handleClosee();
        } else {
          this.$modal.msgError("操作失败");
          this.handleClosee();
        }
      });
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
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
