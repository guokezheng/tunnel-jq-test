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
        v-show="show1"
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
        </el-row>
        <el-row>
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
        </el-row>
        <el-row>
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
        </el-row>
        <el-row>
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
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="当前状态:">
              <el-select v-model="stateForm2.state">
                <el-option
                  v-for="item in openState"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item
              v-show="stateForm2.eqType == 30 && showTipe == true"
              label-width="10px"
            >
              <span style="color: red; font-weight: bold"
                >当前地址为报警点位</span
              >
            </el-form-item></el-col
          >
        </el-row>
        <el-row>
          <el-col :span="15">
            <el-form-item label="闪烁频率:">
              <el-slider
                v-model="stateForm2.frequency"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="9" v-if="stateForm2.frequency">
            <span style="padding-left: 10px; line-height: 30px"
              >{{ stateForm2.frequency }} m/s</span
            >
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="15">
            <el-form-item label="亮度调整">
              <el-slider
                v-model="stateForm2.brightness"
                :max="100"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="9" v-if="stateForm2.brightness">
            <span style="padding-left: 10px; line-height: 30px"
              >{{ stateForm2.brightness }} lux</span
            >
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <!--            <el-form-item label="报警点位:" label-width="130px">-->
            <!--            <el-form-item label="报警点位:" v-show="stateForm2.eqType == 30">-->
            <!--              <el-radio-group v-model="stateForm2.address">-->
            <!--                <el-radio-->
            <!--                  v-for="item in fireMarkData"-->
            <!--                  :key="item.value"-->
            <!--                  :label="item.value"-->
            <!--                >{{item.label}}</el-radio>-->
            <!--              </el-radio-group>-->
            <!--            </el-form-item>-->
            <!-- <el-form-item v-show="stateForm2.eqType == 30 && showTipe == true">
              <span style="color: red; font-weight: bold">当前地址为报警点位</span>
            </el-form-item> -->
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
import {
  controlGuidanceLampDevice,
  controlEvacuationSignDevice,
} from "@/api/workbench/config.js"; //提交控制信息
import { getDevice, fireMarkList } from "@/api/equipment/tunnel/api.js"; //查诱导灯亮度、频率等

export default {
  data() {
    return {
      fireMarkData: [],
      stateForm: {},
      title: "",
      visible: false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      show1: true,
      show2: false,
      value2: true,
      stateForm2: {
        frequency: null,
        brightness: null,
        state: null,
        fireMark: null,
      },
      showTipe: false,
      openState: [
        {
          value: "1",
          label: "关闭",
        },
      ],
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

        await getDevice(this.eqInfo.equipmentId).then((response) => {
          console.log(response, "诱导灯频率、亮度等");
          // this.stateForm2 = response.data
          this.stateForm2 = {
            frequency: Number(response.data.frequency),
            brightness: Number(response.data.brightness),
            state: response.data.state,
            address: this.stateForm.query_point_address,
            eqType: this.stateForm.eqType,
            eqFeedbackAddress1: this.stateForm.query_point_address,
          };
        });
        if (this.eqInfo.clickEqType == 30) {
          this.fireMarkData = [
            {
              label: "设置为报警点位",
              value: this.stateForm.query_point_address,
            },
          ];
          if (this.stateForm.query_point_address == this.stateForm.fireMark) {
            this.fireMarkData.push({ label: "清除报警点位", value: "255" });
            this.showTipe = true;
          } else {
            this.showTipe = false;
          }
          this.openState.push(
            {
              value: "2",
              label: "常亮",
            },
            {
              value: "5",
              label: "报警",
            }
          );
          // fireMarkList(this.eqInfo.equipmentId).then((res) => {
          //   let data = res.data;
          //   this.fireMarkData = data;
          //   //
          //   console.log(this.fireMarkData, "000");
          // });
        } else if (this.eqInfo.clickEqType == 31) {
          this.openState.push(
            {
              value: "2",
              label: "同步单闪",
            },
            {
              value: "3",
              label: "逆向流水",
            }
          );
        }
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

    // 提交修改
    handleOK() {
      if (this.stateForm2.state == "2") {
        this.stateForm2.address = "255";
      } else if (this.stateForm2.state == "1") {
        this.stateForm2.address = "0";
      }
      const param = {
        devId: this.stateForm.eqId, //设备id
        state: this.stateForm2.state, //设备状态
        brightness: this.stateForm2.brightness, //诱导灯亮度
        frequency: this.stateForm2.frequency, //诱导灯频率
        fireMark: this.stateForm2.address,
      };
      this.$modal.msgSuccess("指令下发中，请稍后。");
      if (this.stateForm2.eqType == 30) {
        controlEvacuationSignDevice(param).then((response) => {
          console.log(response, "提交控制");
          this.$modal.msgSuccess("操作成功");
          this.$emit("dialogClose");
        });
      } else if (this.stateForm2.eqType == 31) {
        controlGuidanceLampDevice(param).then((response) => {
          console.log(response, "提交控制");
          this.$modal.msgSuccess("操作成功");
          this.$emit("dialogClose");
        });
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
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
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
.blueButton {
  height: 25px;
  text-align: center;
  line-height: 25px;
  border-radius: 15px;
  font-size: 12px;
  background: linear-gradient(172deg, #00aced, #0079db);
}
::v-deep .el-switch__core {
  width: 200px !important;
  height: 25px;
  border-radius: 15px;
}
::v-deep .el-switch__core:after {
  height: 25px;
  width: 100px;
  border-radius: 15px;
  background: linear-gradient(172deg, #c49326, #d8c960);
  top: -1px;
  left: -1px;
}
::v-deep .el-switch.is-checked .el-switch__core::after {
  left: 115px;
}
::v-deep .el-switch__label--left {
  position: absolute;
  left: 22px;
  top: 3px;
  color: white;
  z-index: 10;
  font-size: 10px;
}
::v-deep .el-switch__label--right {
  position: absolute;
  left: 110px;
  top: 3px;
  color: white;
  z-index: 10;
  font-size: 10px;
}
::v-deep .el-select-dropdown__item.hover,
.el-select-dropdown__item:hover {
  background-color: #1d58a9;
  color: white;
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
