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
            <el-form-item label="设备状态:"
            :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
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
        <el-row>
          <el-col :span="15">
            <el-form-item label="音量:">
              <el-slider
                v-model="stateForm2.volume"
                :max="100"
                class="sliderClass"
              ></el-slider>
            </el-form-item>
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
      </el-form>
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
      </div>
    </el-dialog>
  </div>
</template>
  <script>
import { getDeviceById, playVoice, getAudioFileList } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  data() {
    return {
      stateForm2: {
        loopCount: 1,
        loop: false,
        volume: 0,
        fileNames: [],
      },
      stateForm:{},
      fileNamesList: [],
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      tunnelId:''
    };
  },
  created() {
    this.getMessage();
    const param ={
      deviceId:this.eqInfo.equipmentId,
    }
    getAudioFileList(param).then((res) =>{
      console.log(res,"文件列表");
      this.fileNamesList = res.data
    })

  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.device = res.data.externalDeviceId
          this.title = this.stateForm.eqName;
          this.tunnelId = res.data.tunnelId
        });
        // await playVoice().then((response) =>{

        // })
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    handleChange(num) {
      this.stateForm.loopCount = num
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
    handleOK(){
      // const items = []

      // const divId = {}
      // divId.deviceId = this.stateForm.externalDeviceId
      // items.push(divId)
      const param ={
        lib:"YeastarHost",
        loop: this.stateForm2.loop,
        loopCount:this.stateForm2.loopCount,
        volume:this.stateForm2.volume,
        fileNames:Array(this.stateForm2.fileNames),
        spkDeviceIds:Array(this.eqInfo.equipmentId),
        controlType: "0",
        tunnelId:this.tunnelId,
        // items :items,
      }
      console.log(param,"param");
      playVoice(param).then((res) =>{
        this.$modal.msgSuccess("控制成功");
      })
        this.$emit("dialogClose");
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
  <style scoped lang="scss">
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
