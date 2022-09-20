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
      <div
        style="
          width: 100%;
          height: 30px;
          display: flex;
          justify-content: space-between;
        "
      >
        <div class="dialogLine"></div>
        <img
          :src="titleIcon"
          style="height: 30px; transform: translateY(-30px); cursor: pointer"
          @click="handleClosee"
        />
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="90px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
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
              {{ getBrandName(stateForm.brandName) }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备状态:">
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="开关状态:">
              <!-- {{ stateForm.deptName }} -->
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="诱导灯数量:">
              <!-- {{ getBrandName(stateForm.brandName) }} -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="闪烁频率:">
              <!-- {{ stateForm.deptName }} -->
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="灯光亮度:">
              <!-- {{ getBrandName(stateForm.brandName) }} -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="灯光颜色:">
              <div style="display: flex; align-items: center">
                <div style="width: 40px; height: 18px; background: red"></div>
                <span style="padding-left: 5px">{{ "红色" }}</span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="更新时间:">
              <!-- {{ stateForm.eqStatus }} -->
              <!-- {{ stateForm.eqStatus }} -->
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-show="show1">
        <el-button
          type="primary"
          size="mini"
          @click="handleControl()"
          style="width: 80px"
          >设备管控</el-button
        >
      </div>
      <div v-show="show2">
        <div>
          <el-form
            :model="stateForm2"
            label-width="68px"
            label-position="left"
            size="mini"
            style="padding: 15px; padding-top: 0px"
          >
            <div>设备管控</div>
            <el-switch
              style="display: block; margin: 10px 0 20px 0"
              v-model="stateForm2.switch"
              active-color="#426086"
              inactive-color="#426086"
              active-text="手动开关"
              inactive-text="自动开关"
              @change="handeleSwitch()"
            >
            </el-switch>
            <el-row v-show="!stateForm2.switch">
              <el-col :span="12">
                <el-form-item label="开启时间">
                  <el-select v-model="stateForm2.openTimeValue">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关闭时间">
                  <el-select v-model="stateForm2.closeTimeValue">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-show="!stateForm2.switch">
              <el-col :span="12">
                <el-form-item label="闪烁频率">
                  <el-select v-model="stateForm2.twinkle">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="灯光颜色">
                  <el-select v-model="stateForm2.lightColor">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-show="stateForm2.switch">
              <el-col :span="4">
                <el-form-item label="当前状态">
                  <div class="blueButton" style="width: 80px">开启灯光</div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-show="stateForm2.switch">
              <el-col :span="10">
                <el-form-item label="闪烁频率">
                  <el-select v-model="stateForm2.frequency">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="灯光颜色">
                  <el-select v-model="stateForm2.lightColor">
                    <el-option
                      v-for="item in openTime"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <div class="blueButton">关闭灯光</div>
              </el-col>
            </el-row>
            <div class="lineClass"></div>
            <div style="margin: 10px 0">亮度调整</div>
            <el-form-item label="亮度调整">
              <el-slider
                v-model="stateForm2.brightness"
                :show-tooltip="false"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-form>
        </div>
        <div
          slot="footer"
          style="float: right; margin-right: 15px; margin-bottom: 20px"
        >
          <el-button
            type="primary"
            size="mini"
            @click="handleOK()"
            style="width: 80px"
            class="submitButton"
            >确 定</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="handleClosee()"
            style="width: 80px"
            >取 消</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>
  <script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
import { controlDevice } from "@/api/workbench/config.js"; //提交控制信息

export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
  data() {
    return {
      stateForm: {},
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      show1: true,
      show2: false,
      value2: true,
      stateForm2: {
        openTimeValue: "",
        closeTimeValue: "",
        frequency: "",
        lightColor: "",
        brightness: 50,
      },
      openTime: [
        {
          value: 1,
          label: "10m/s",
        },
      ],
    };
  },
  created() {
    this.getMessage();
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
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    handeleSwitch() {},
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
      const param = {
        devId: this.stateForm.eqId, //设备id
        devType: this.eqInfo.clickEqType,
        brightness: this.stateForm2.brightness, //诱导灯亮度
        frequency: this.stateForm2.frequency,//诱导灯频率
        state: this.stateForm.eqStatus,
        tunnelId: this.stateForm.tunnelId,
      };

      controlDevice(param).then((response) => {
        console.log(response, "提交控制");
        this.$emit("dialogClose");
      });
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
    //  设备管控
    handleControl() {
      console.log(this.stateForm2,"this.stateForm211111111111")
      this.stateForm2 = {}
      console.log(this.stateForm2,"this.stateForm222222222222")

      this.show1 = false;
      this.show2 = true;
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
::v-deep .el-input__inner{
  background: white !important;
  color: #00152B !important;
}
::v-deep .el-input{
  width:86%;
}
</style>
  