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
        <el-row style="margin-top: 10px" v-if="brandOne">
          <el-col :span="14">
            <el-form-item label="播放次数:" prop="loopCount">
              <el-input-number
                v-model.number="stateForm2.loopCount"
                :min="0"
                controls-position="right"
                @change="handleChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="loop" label-width="0px">
              <el-checkbox
                v-model="stateForm2.loop"
                label="循环播放"
                border
              ></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="brandOne">
          <el-col :span="15">
            <el-form-item label="音量:">
              <el-slider
                v-model="stateForm2.volume"
                :max="100"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <span style="padding-left: 10px; line-height: 30px"
              >{{ stateForm2.volume }} %</span
            >
          </el-col>
        </el-row>

        <el-row>
          <el-col>
            <el-form-item label="播放文件:">
              <el-select
                v-model="stateForm2.fileNames"
                placeholder="请选择播放文件"
                clearable
                size="mini"
              >
                <el-option
                  v-for="item in fileNamesList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.fileName"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="brandTwo">
          <el-col>
            <el-form-item label="状态:">
              <el-radio-group
                v-model="stateForm2.loopStatus"
                v-for="item in options"
                :key="item.value"
                @change="$forceUpdate()"
                style="display: flex; flex-direction: column"
              >
                <el-radio
                  :label="item.label"
                  class="el-radio flex-row"
                  style="align-items: center"
                 
                ></el-radio>
              </el-radio-group>
              <!-- <el-select 
                v-model="stateForm2.loopStatus" 
                placeholder="请选择状态"
                clearable
                size="mini"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select> -->
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
import {
  getDeviceById,
  playVoice,
  getAudioFileList,
} from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  data() {
    return {
      options: [
        {
          value: "#PLY#",
          label: "播放",
        },
        {
          value: "#STOP#",
          label: "停止",
        },
      ],
      stateForm2: {
        loopStatus: "",
        loopCount: 1,
        loop: false,
        volume: 0,
        fileNames: [],
      },
      stateForm: {},
      fileNamesList: [],
      title: "",
      visible: false,
      tunnelId: "",
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
      brandOne: false,
      brandTwo: false,
    };
  },
  created() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getAudioFile();
      this.getMessage();
      this.visible = true;
    },
    getAudioFile() {
      const param = {
        deviceId: this.eqInfo.equipmentId,
      };
      getAudioFileList(param).then((res) => {
        console.log(res, "文件列表");
        this.fileNamesList = res.data;
      });
    },
    // 查设备详情
    async getMessage() {
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          if (res.data.brandId != "0060") {
            this.brandOne = false;
            this.brandTwo = true;
          } else {
            this.brandOne = true;
            this.brandTwo = false;
          }
          this.stateForm2.loopStatus = "";
          this.stateForm = res.data;
          this.device = res.data.externalDeviceId;
          this.title = this.stateForm.eqName;
          this.tunnelId = res.data.tunnelId;
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    handleChange(num) {
      this.stateForm.loopCount = num;
    },
    // 关闭弹窗
    handleClosee() {
      this.visible = false;
      this.stateForm2.loopStatus = "";
    },
    handleOK() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      const param = {
        lib: "YeastarHost",
        loop: this.stateForm2.loop,
        loopCount: this.stateForm2.loopCount,
        loopStatus: this.stateForm2.loopStatus,
        volume: this.stateForm2.volume,
        fileNames: Array(this.stateForm2.fileNames),
        spkDeviceIds: Array(this.eqInfo.equipmentId),
        controlType: "0",
        tunnelId: this.tunnelId,
      };
      console.log(param, "param");
      playVoice(param)
        .then((res) => {
          loading.close();
          this.$modal.msgSuccess("控制成功");
        })
        .catch(() => {
          loading.close();
        });
      this.visible = false;
      this.stateForm2.loopStatus = "";
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
  },
};
</script>
  <style scoped lang="scss">
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
</style>
