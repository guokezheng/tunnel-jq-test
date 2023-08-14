<template>
  <div>
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table"
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
      <div class="picVideoBox" v-if="eqInfo.clickEqType == 33">
        <img :src="picUrl" v-if="radio1 == '图像'" />
        <videoPlayer
          v-if="videoForm.liveUrl && radio1 == '视频'"
          :rtsp="videoForm.liveUrl"
          :open="cameraPlayer"
        ></videoPlayer>
        <video
          src="https://media.w3.org/2010/05/sintel/trailer.mp4"
          v-if="radio1 == '演示'"
          controls
          muted
          loop
          autoplay
          fluid
        ></video>
        <img :src="noPicUrl" v-if="radio1 == '视频' && !videoForm.liveUrl" />
      </div>
      <div class="picVideoBox" v-if="eqInfo.clickEqType == 47">
        <img :src="ckUrl" />
      </div>
      <div class="picButton">
        <el-radio-group
          v-model="radio1"
          class="picVideo"
          v-if="eqInfo.clickEqType == 33"
        >
          <el-radio-button label="图像"></el-radio-button>
          <el-radio-button label="演示"></el-radio-button>
          <el-radio-button label="视频"></el-radio-button>
        </el-radio-group>
        <div class="x2all">
          <div class="button" @click="pic2X()">2X</div>
          <div class="button" @click="picFull()">全屏</div>
        </div>
      </div>
      <el-form
        class="form"
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
            <el-form-item label="IP:"> </el-form-item>
          </el-col>
          <el-col :span="11">
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
      </el-form>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      class="workbench-dialog picDialog"
      width="800px"
      append-to-body
      :visible="picVisible"
      :before-close="picHandleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img
        :src="eqInfo.clickEqType == 33 ? picUrl : ckUrl"
        v-if="radio1 == '图像'"
      />
      <videoPlayer
        v-if="videoForm.liveUrl"
        :rtsp="videoForm.liveUrl"
        :open="cameraPlayer"
      ></videoPlayer>
      <video
          src="https://media.w3.org/2010/05/sintel/trailer.mp4"
          v-if="radio1 == '演示'"
          style="height: 400px; object-fit: cover"
          controls
          muted
          loop
          autoplay
          fluid
        ></video>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      class="workbench-dialog picFullDialog"
      width="100%"
      append-to-body
      :visible="picFullVisible"
      :before-close="picHandleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img
        :src="eqInfo.clickEqType == 33 ? picUrl : ckUrl"
        v-if="radio1 == '图像'"
      />
      <videoPlayer
        v-if="videoForm.liveUrl"
        :rtsp="videoForm.liveUrl"
        :open="cameraPlayer"
      ></videoPlayer>
      <!-- src="../../../../assets/Example/v1.mp4" -->

      <video
          src="https://media.w3.org/2010/05/sintel/trailer.mp4"
          v-if="radio1 == '演示'"
          style="height: 100%; object-fit: cover"
          controls
          muted
          loop
          autoplay
          fluid
        ></video>
    </el-dialog>
  </div>
</template>
<script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
import { getRtspStreamAddr } from "@/api/workbench/config.js"; //提交控制信息

export default {
  components: {
    videoPlayer,
  },
  data() {
    return {
      title: "",
      visible: false,
      picVisible: false,
      picFullVisible: false,
      stateForm: {},
      brandList: [],
      eqInfo: {
        clickEqType: "",
        equipmentId: "",
      },
      eqTypeDialogList: [],
      directionList: [],
      radio1: "图像",
      cameraPlayer: false,
      videoForm: {
        liveUrl: "",
      },
      picUrl: require("@/assets/image/xfp.png"),
      noPicUrl: require("@/assets/image/noVideo.png"),
      ckUrl: require("@/assets/image/ck2.png"),
    };
  },
  created() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      console.log(this.eqInfo, "this.eqInfo");
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getMessage();
      this.visible = true;
      this.cameraPlayer = false;
      this.radio1 = "图像";
    },
    async getMessage() {
      // 查询单选框弹窗信息 -----------------------
      await getDeviceById(this.eqInfo.equipmentId).then((res) => {
        console.log(res, "查询单选框弹窗信息");
        this.stateForm = res.data;
        this.title = this.stateForm.eqName;
        if (this.eqInfo.clickEqType == 33) {
          this.getVideo();
        }
      });
    },
    getVideo() {
      // getRtspStreamAddr(this.eqInfo.ip).then((res)=>{
      //     console.log(res,"消防炮视频流")
      // })
      this.$modal.msgWarning("获取视频失败");
    },
    pic2X() {
      this.picVisible = true;
      this.picFullVisible = false;
    },
    picFull() {
      this.picFullVisible = true;
      this.picVisible = false;
    },
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
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
      this.cameraPlayer = false;
      this.picHandleClosee();
    },
    picHandleClosee() {
      this.picVisible = false;
      this.picFullVisible = false;
    },
  },
};
</script>
<style scoped lang="scss">
.picVideoBox {
  width: 100%;
  max-height: 230px;
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    max-height: 230px;
  }
  video{
    max-height: 230px;
    object-fit: cover
  }
}
.picButton {
  width: calc(100% - 30px);
  height: 30px;
  position: absolute;
  top: calc(4vh + 20px);
  // display: flex;
  // justify-content: space-between;
  // align-items: center;
  .picVideo {
    float: left;
    ::v-deep .el-radio-button--medium .el-radio-button__inner {
      padding: 4px 8px;
    }
    ::v-deep .el-radio-button:last-child .el-radio-button__inner {
      border-radius: 0 12px 12px 0;
    }
    ::v-deep .el-radio-button:first-child .el-radio-button__inner {
      border-radius: 12px 0 0 12px;
    }
    ::v-deep .el-radio-button__inner {
      background: #00152b;
      border-color: #39adff;
    }
    ::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
      background: #39adff;
    }
  }
  .x2all {
    float: right;
    display: flex;
    align-items: center;
    .button {
      width: 48px;
      height: 24px;
      line-height: 24px;
      text-align: center;
      border-radius: 12px;
      background: #00152b;
      color: #fff;
      cursor: pointer;
    }
    .button:first-of-type {
      margin-right: 4px;
    }
    .button:hover {
      background: #39adff;
    }
  }
}
.form {
  margin-top: 10px;
}
.picDialog {
  ::v-deep .el-dialog__body {
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      max-height: 400px;
    }
    // video{
    //   height: 400px;
    // }
  }
}
.picFullDialog {
  ::v-deep .el-dialog__body {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 86vh;
    overflow: auto;
    img {
      height: 100%;
    }
  }
}
</style>