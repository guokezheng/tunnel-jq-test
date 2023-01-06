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
      >
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备类型:">
              {{ stateForm.typeName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="隧道名称:" label-width="102px">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
       
          <el-col :span="13">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="所属方向:" label-width="102px">
              {{ getDirection(stateForm.eqDirection) }}
            </el-form-item>
          </el-col>
      
          <el-col :span="13">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="设备厂商:" label-width="102px">
              {{ getBrandName(stateForm.brandName) }}
            </el-form-item>
          </el-col>
       
          <el-col :span="13">
            <el-form-item label="设备状态:">
              <!-- {{ stateForm.eqStatus }} -->
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass" v-if="eqInfo.clickEqType == 10" style="margin-bottom:10px"></div>
        <el-row v-if="eqInfo.clickEqType == 10">
          <el-col :span="13">
            <el-form-item label="振动速度值:">
              {{ stateForm2.shakeSpeed }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="振动幅度值:" label-width="102px">
              {{ stateForm2.amplitude }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="沉降值:">
              {{ stateForm2.subside }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="倾斜值:" label-width="102px">
              {{ stateForm2.slope }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="振动告警:">
              {{ stateForm2.shakeAlaram }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="沉降倾斜告警:" label-width="102px">
              {{ stateForm2.subsideSlopeAlaram }}
            </el-form-item>
          </el-col>
        </el-row>
        
        <div class="lineClass"></div>
        <el-row style="margin-top:10px">
          <el-col :span="15">
            <el-form-item label="亮度调整" v-show="this.eqInfo.clickEqType == 7">
          <el-slider
            v-model="stateForm.brightness"
            :max="100"
            class="sliderClass"
          ></el-slider>
        </el-form-item>
          </el-col>
        </el-row>

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
                      stateForm.eqDirection == '1' && (stateForm.eqType == 1||stateForm.eqType ==2)

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
                       (stateForm.eqType == 1||stateForm.eqType == 2)
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
                  <el-row class="flex-row" v-if="stateForm.eqType != 1 && stateForm.eqType != 2 ">
                    <img
                      :width="iconWidth"
                      :height="iconHeight"
                      :src="item.url[0]"
                    />

                    <div style="margin: 0 0 0 10px; display: inline-block">
                      {{ item.name }}
                    </div>
                  </el-row>
                </el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
          <div slot="footer" style="float: right; margin-bottom: 20px">
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
      </el-form>
    </el-dialog>
  </div>
</template>

  <script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getType } from "@/api/equipment/type/api.js"; //查询设备图标宽高
import { getDevice, setBrightness } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import { getStateByData } from "@/api/equipment/eqTypeState/api"; //查询设备状态图标
import { controlDevice,getFanSafeData } from "@/api/workbench/config.js"; //提交控制信息

export default {
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
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
      stateForm2:{}
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    console.log(this.eqInfo.clickEqType, "clickEqTypeclickEqTypeclickEqType");

    this.getMessage();
    // this.loadFlv();
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
          this.stateForm.brightness = Number(res.data.brightness);
          // 查询设备当前状态 --------------------------------
          getDevice(this.eqInfo.equipmentId).then((response) => {
            console.log(response, "查询设备当前状态");
            this.stateForm.state = response.data.state;
            this.getEqTypeStateIcon();
          });
        });
        if(this.eqInfo.clickEqType == 10){
          await getFanSafeData(this.eqInfo.equipmentId).then((res) => {
            console.log(res,"风机")
            this.stateForm2 = res.data
          })
        }
        
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
        console.log(iconUrl);

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
      console.log(this.stateForm.state, "单选框点击绑定");
      const param = {
        devId: this.stateForm.eqId, //设备id
        state: this.stateForm.state,
      };

      controlDevice(param).then((response) => {
        if (response.data == 0) {
          this.$modal.msgError("控制失败");
        } else if (response.data == 1) {
          const params = {
            bright: this.stateForm.brightness,
            controlType: "0",
            deviceId: this.eqInfo.equipmentId,
          };
          setBrightness(params).then((res) => {
            console.log(res, "亮度");
          this.$modal.msgSuccess("控制成功");

          });
        }

        this.$emit("dialogClose");
      });
     
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
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
    // background-color: #006784;
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
</style>
