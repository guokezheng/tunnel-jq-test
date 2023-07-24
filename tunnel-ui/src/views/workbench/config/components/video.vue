<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      width="450px"
      append-to-body
      :visible="cameraVisible"
      :before-close="handleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div style="width: 100%; height: 200px">
        <video
          v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'"
          id="h5sVideo1"
          class="h5video_"
          controls
          muted
          autoplay
          disablePictureInPicture="true"
          controlslist="nodownload noplaybackrate noremoteplayback"
          style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
        ></video>
        <videoPlayer
          v-if="videoForm.liveUrl"
          :rtsp="videoForm.liveUrl"
          :open="cameraPlayer"
        ></videoPlayer>
      </div>
      <div
        class="rlModelButton left"
        @click="changeVideo('left')"
        v-if="[21, 32, 39, 40].includes(this.clickEqType)"
      >
        <div @click="changeVideo('left')" ></div>
      </div>
      <div
        class="rlModelButton right"
        v-if="[21, 32, 39, 40].includes(this.clickEqType)"
      >
        <div @click="changeVideo('right')"></div>
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="90px"
        label-position="left"
        size="mini"
      >
        <el-tabs class="videoTabs" v-model="videoActive">
          <el-tab-pane label="详细信息" name="information">
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
                <el-form-item label="所属机构:">
                  {{ stateForm.deptName }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="设备桩号:">
                  {{ stateForm.pile }}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="所属方向:">
                  {{ getDirection(stateForm.eqDirection) }}
                </el-form-item>
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
          </el-tab-pane>
          <el-tab-pane
            label="摄像机参数"
            name="videoParams"
            v-if="[23, 24, 25].includes(this.clickEqType)"
          >
            <el-row>
              <el-col :span="13">
                <el-form-item label="IP:">
                  {{ stateForm.ip }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="端口:">
                  {{ stateForm.port }}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="协议类型:">
                  {{ stateForm.protocol }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="分辨率:">
                  <!-- {{ '1280*720' }} -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="格式:">
                  <!-- {{ 'Video/H.262' }} -->
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="码流:">
                  <!-- {{ '8M' }} -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="厂商:">
                  {{ getBrandName(stateForm.brandName) }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="型号:">
                  {{ stateForm.eqModel }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="videoYunTai()"
          class="submitButton"
          v-show="stateForm.eqType == 24"
          >云台控制</el-button
        >
        <!-- <el-button
          type="primary"
          size="mini"
          @click="videoViewing()"
          style="width: 80px"
          class="submitButton"
          >录像查看</el-button
        > -->
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      width="1000px"
      append-to-body
      :visible="historyVisible"
      :before-close="handleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form
        ref="historyForm"
        :model="queryParams"
        label-width="100px"
        :inline="true"
        size="mini"
        style="margin-top: 10px"
      >
        <el-form-item label="查询事件" prop="eventTypeId">
          <el-select
            v-model="queryParams.eventTypeId"
            placeholder="请选择事件类型"
            clearable
            size="small"
            style="width: 180px"
          >
            <el-option
              v-for="item in eventTypeData"
              :key="item.id"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择时间" prop="eventTime">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            unlink-panels
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          ></el-date-picker>
        </el-form-item>
        <el-button
          type="primary"
          size="mini"
          @click="handleQuery()"
          style="width: 60px"
          >查 询</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="resetQuery()"
          style="width: 60px"
          >重 置</el-button
        >
      </el-form>
      <div
        style="
          width: 100%;
          height: 400px;
          overflow-y: auto;
          margin-bottom: 50px;
        "
      >
        <div
          style="
            width: 19%;
            display: inline-block;
            height: 200px;
            margin-left: 9px;
          "
          v-for="(item, index) in videoList"
          :key="index"
        >
          <div style="width: calc(100% - 20px); height: 50%; margin: 5px auto">
            <el-image
              :src="item.pic"
              style="width: 100%; height: 100%"
            ></el-image>
          </div>
          <div style="padding-left: 10px; line-height: 20px">
            {{ item.time }}
          </div>
          <div style="padding-left: 10px; line-height: 20px">
            {{ item.event }}
          </div>
          <div style="padding-left: 10px; line-height: 20px">
            状态: <span style="color: #ff9900">{{ item.status }}</span>
          </div>
        </div>
      </div>

      <pagination
        v-show="total == 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="handleQuery"
        style="height: 40px; margin-right: 30px"
        class="paginationWorkbench"
      />
    </el-dialog>
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      width="1000px"
      append-to-body
      :visible="yunTaiVisible"
      :before-close="handleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-row class="yuntaiBox">
        <el-col :span="18">
          <div class="yuntaiVideoBox">
            <videoPlayer
              v-if="videoForm.liveUrl"
              :rtsp="videoForm.liveUrl"
              :open="cameraPlayer"
            ></videoPlayer>
          </div>
          <!-- <div class="yuntaiPic"> -->
          <!-- <div @click="clickLeft()"><</div>
            <div>
              <img :src="item.pic" v-for="(item,index) in picList" :key="index"></img>
            </div>
            <div @click="clickRight()">></div> -->
          <!-- <img :src="videoIcon" v-for="(item,index) of 8" :key="index"/> -->
          <!-- </div> -->
        </el-col>
        <el-col :span="6">
          <div class="fangxiang">
            <div class="picBox">
              <img
                :src="yuntai"
                v-for="(item, index) of yuntaiList"
                :key="index"
                @click="changeYunTai(item.cmdType)"
              />
            </div>
            <div class="yuzhiwei">预置位</div>
          </div>
          <div class="jiaJian">
            <div @click="changeYunTai(11)">-</div>
            <div>变倍</div>
            <div @click="changeYunTai(12)">+</div>
          </div>
          <div class="jiaJian">
            <div @click="changeYunTai(13)">-</div>
            <div>聚焦</div>
            <div @click="changeYunTai(14)">+</div>
          </div>
          <div class="jiaJian">
            <div @click="changeYunTai(16)">-</div>
            <div>光圈</div>
            <div @click="changeYunTai(15)">+</div>
          </div>
          <!-- <div class="sliderClass">
            <div>0</div>
            <el-slider v-model="yunTaiForm.slider" :max="255"></el-slider>
            <div>255</div>
          </div> -->
          <div class="switchClass">
            <div>雨刷</div>
            <el-switch
              style="display: block"
              v-model="yunTaiForm.yuShua"
              active-color="#01AAFD"
              inactive-color="#ddd"
              active-text="开"
              inactive-text="关"
              @change="changeYunTai(yunTaiForm.yuShua, 'yushua')"
            >
            </el-switch>
          </div>
          <!-- <div class="switchClass">
            <div>灯光</div>
            <el-switch
              style="display: block"
              v-model="yunTaiForm.dengGuang"
              active-color="#01AAFD"
              inactive-color="#ddd"
              active-text="开"
              inactive-text="关"
            >
            </el-switch>
          </div> -->
          <!-- <div class="switchClass">
            <div>除雪</div>
            <el-switch
              style="display: block"
              v-model="yunTaiForm.chuXue"
              active-color="#01AAFD"
              inactive-color="#ddd"
              active-text="开"
              inactive-text="关"
            >
            </el-switch>
          </div> -->
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { getDeviceById, videoStreaming } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getInfo } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import flvjs from "flv.js";
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
import { PTZContro, getCamera } from "@/api/workbench/config.js"; //提交控制信息

// import { getLocalIP } from "@/api/event/vedioRecord";

export default {
  // props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  components: {
    videoPlayer,
  },
  data() {
    return {
      clickEqType: "",
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      title: "",
      cameraPlayer: false, //摄像机弹窗
      historyVisible: false, //历史记录弹窗
      videoActive: "information", // tab页
      stateForm: {}, //弹窗表单
      eventTypeData: [], //事件类型
      yunTaiVisible: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      }, //历史记录表单
      dateRange: [], //选择时间数组
      total: 0, // 总条数
      src: require("@/assets/images/warningPhoto.png"),
      videoMoNi: "",
      // videoMoNi: require("../../../../assets/Example/d1.mp4"),
      videoForm: {
        liveUrl: "",
      },
      // hostIP: null,
      cameraVisible: false,
      videoIcon: require("@/assets/logo/equipment_log/固定摄像机-正常.png"),
      yuntai: require("@/assets/cloudControl/yuntai.png"),
      yuntaiList: [
        {
          cmdType: 21, //上
        },
        {
          cmdType: 52, //右上
        },
        {
          cmdType: 24, //右
        },
        {
          cmdType: 53, //右下
        },
        {
          cmdType: 22, //下
        },
        {
          cmdType: 51, //左下
        },
        {
          cmdType: 23, //左
        },
        {
          cmdType: 50, //左上
        },
      ],
      videoList: [
        {
          pic: require("@/assets/images/ruoyi-login-background.jpg"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/login-background.jpg"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/Example/pic2.jpg"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },

        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/Example/pic1.jpg"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
        {
          pic: require("@/assets/images/warningPhoto.png"),
          time: "2022-03-11 14:47:13",
          event: "事件: 道路拥堵",
          status: "正在进行",
        },
      ],
      yunTaiForm: {
        slider: 0,
        yuShua: false,
        dengGuang: false,
        chuXue: true,
      },
      picPage: 1,
      player: null,
      tunnelId: "",
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
      pileNum: "",
    };
  },
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.clickEqType = JSON.parse(JSON.stringify(this.eqInfo.clickEqType));
      console.log(this.clickEqType, "this.clickEqType");
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.cameraVisible = true;
      (this.videoActive = "information"), // tab页
        (this.cameraPlayer = false);
      this.getmessage();
    },
    // 点击云台方向
    changeYunTai(cmdType, type) {
      if (type && cmdType == false) {
        cmdType = 48;
      } else if (type && cmdType == true) {
        cmdType = 49;
      }
      PTZContro(this.eqInfo.equipmentId, cmdType).then((res) => {
        console.log(res, "云台控制");
      });
    },
    // 根据设备id 获取弹窗内信息
    async getmessage() {
      if (this.eqInfo.equipmentId) {
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询摄像机弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          this.tunnelId = res.data.tunnelId;
          if (res.data.tunnelId == "WLJD-JiNan-YanJiuYuan-FHS") {
            displayH5sVideoAll(res.data.secureKey, "h5sVideo1");
          } else {
            if ([23, 24, 25].includes(this.clickEqType)) {
              this.getVideo();
            } else {
              this.changeVideo();
            }
          }
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    // 火灾探测器 声光报警器 手报 电话
    // 1.  刚打开 传火灾报警桩号pileNum 隧道id 方向 返回来的是对象 当前摄像机基本信息
    // 2.  往右 传 （1）rlModel：right （2）摄像机桩号 隧道id 方向
    getVideo(videoData) {
      let equipmentId = "";
      if (videoData) {
        equipmentId = videoData;
      } else {
        equipmentId = this.eqInfo.equipmentId;
      }
      videoStreaming(equipmentId)
        .then((response) => {
          console.log(response, "视频流");
          if (response.code == 200 && response.data) {
            this.videoForm = response.data;
            this.cameraPlayer = true;
          } else {
            this.$modal.msgWarning("获取视频失败");
          }
        })
        .catch((e) => {
          this.$modal.msgWarning("获取视频失败");
        });
    },
    changeVideo(rlModel) {
      const params = {
        eqDirection: this.stateForm.eqDirection,
        pileNum: rlModel ? this.pileNum : this.stateForm.pileNum,
        eqTunnelId: this.stateForm.tunnelId,
        rlModel: rlModel,
      };
      getCamera(params).then((res) => {
        console.log(res, "切换左右摄像机信息");
        if (res.data == "") {
          this.$message.warning("当前为最后一个摄像机");
        } else {
          this.pileNum = res.data.pileNum;
          this.cameraPlayer = false;
          this.getVideo(res.data.eqId);
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
      this.cameraVisible = false;
      this.cameraPlayer = false;
      this.yunTaiVisible = false;
    },
    // 录像查看
    videoViewing() {
      // this.cameraVisible = false;
      this.historyVisible = true;
    },
    // 云台控制
    videoYunTai() {
      // this.cameraVisible = false;
      this.yunTaiVisible = true;
    },
    // 历史记录表单查询
    handleQuery() {},
    // 历史记录表单重置
    resetQuery() {
      this.resetForm("queryForm");
    },
    clickLeft() {
      if (this.videoList.length > 4) {
        if (this.picPage == 2) {
          this.picList = this.videoList.slice(0, 4);
          this.picPage = 1;
        } else if (this.picPage == 1) {
          this.picList = this.videoList.slice(4, 8);
          this.picPage = 2;
        }
        this.$forceUpdate();
      }
    },
    clickRight() {
      if (this.videoList.length > 4) {
        if (this.picPage == 2) {
          this.picList = this.videoList.slice(0, 4);
          this.picPage = 1;
        } else if (this.picPage == 1) {
          this.picList = this.videoList.slice(4, 8);
          this.picPage = 2;
        }
        this.$forceUpdate();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.rlModelButton {
  position: absolute;
  top: 53px;
  width: 40px;
  height: 200px;
  opacity: 0;
  display: flex;
  align-items: center;
  > div {
    cursor: pointer;
    width: 40px;
    height: 40px;
    border-radius: 20px;
    background: url(../../../../assets/cloudControl/toRight2.png);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100% 100%;
  }
}
.left {
  left: 15px;
  transform: rotate(180deg);
}
.right {
  right: 15px;
}
.rlModelButton:hover {
  opacity: 0.5;
}
.robotTabs {
  padding: 0 15px;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.theme-dark .pagination-container {
  background: #00152b;
}
.historyMiniBox {
  width: 18%;
  display: inline-block;
  height: 210px;
  margin-left: 17px;
  margin-bottom: 5px;
  border: solid 1px #01aafd;
  background-color: #638ca6;
}
::v-deep .el-pagination__total {
  color: #01aafd !important;
}
.el-pagination.is-background .btn-prev,
.el-pagination.is-background .btn-next,
.el-pagination.is-background .el-pager li {
  background-color: #00152b !important;
}
.el-pagination.is-background .btn-prev:disabled,
.el-pagination.is-background .btn-next:disabled {
  color: "#01AAFD !important";
}
::v-deep .el-tabs__nav-wrap::after {
  background-color: #dfe4ed;
  opacity: 0.4;
}
::v-deep .el-tabs__active-bar {
  background-color: #01aafd;
}
.yuntaiBox {
  width: 100%;
  // height: 600px;
  height: 460px;
  padding: 20px;
  > .el-col:nth-of-type(1) {
    padding-right: 10px;
  }
  .yuntaiVideoBox {
    width: 100%;
    height: 400px;
    .yunTaiVideo {
      width: 100%;
      height: 100%;
      object-fit: cover;
      z-index: -100;
    }
  }
  .yuntaiPic {
    width: 100%;
    height: calc(100% - 440px);
    margin-top: 10px;
    display: flex;
    align-items: center;
    > div:nth-of-type(1),
    > div:nth-of-type(3) {
      width: 30px;
      height: 30px;
      border-radius: 15px;
      text-align: center;
      line-height: 28px;
      font-size: 20px;
    }
    > div:nth-of-type(1) {
      margin-right: 10px;
      cursor: pointer;
    }
    > div:nth-of-type(2) {
      width: calc(100% - 60px);
      height: 100%;
    }
    > div:nth-of-type(3) {
      margin-left: 5px;
      cursor: pointer;
    }
    img {
      width: 30px;
      height: 30px;
      margin-left: 6px;
      cursor: pointer;
      caret-color: rgba(0, 0, 0, 0);
    }
    img:nth-of-type(1) {
      margin-left: 0;
    }
  }
  .fangxiang {
    width: 100%;
    height: 215px;
    margin: 0 auto;
    transform: scale(0.8);
    position: relative;
    .yuzhiwei {
      text-decoration: underline;
      color: #fff;
      font-size: 24px;
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      margin: auto;
      height: 36px;
      width: 74px;
      cursor: pointer;
      caret-color: rgba(0, 0, 0, 0);
    }
    .picBox {
      width: 215px;
      height: 100%;
      margin: 0 auto;
      position: relative;
      transform: rotate(-23deg);
      > div {
        text-decoration: underline;
        color: #fff;
        font-size: 24px;
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        margin: auto;
        height: 36px;
        width: 74px;
        cursor: pointer;
        caret-color: rgba(0, 0, 0, 0);
      }
      > img {
        cursor: pointer;
        caret-color: rgba(0, 0, 0, 0);
      }
      > img:nth-of-type(1) {
        position: absolute;
        top: -1px;
        left: 96px;
        transform: rotate(0deg) scale(0.77);
      }
      > img:nth-of-type(2) {
        position: absolute;
        top: 45px;
        left: 137px;
        transform: rotate(45deg) scale(0.77);
      }
      > img:nth-of-type(3) {
        position: absolute;
        top: 106px;
        left: 133px;
        transform: rotate(90deg) scale(0.77);
      }
      > img:nth-of-type(4) {
        position: absolute;
        top: 147px;
        left: 87px;
        transform: rotate(135deg) scale(0.77);
      }
      > img:nth-of-type(5) {
        position: absolute;
        top: 143px;
        left: 26px;
        transform: rotate(180deg) scale(0.77);
      }
      > img:nth-of-type(6) {
        position: absolute;
        top: 97px;
        left: -14px;
        transform: rotate(225deg) scale(0.77);
      }
      > img:nth-of-type(7) {
        position: absolute;
        top: 36px;
        left: -11px;
        transform: rotate(269deg) scale(0.77);
      }
      > img:nth-of-type(8) {
        position: absolute;
        top: -5px;
        left: 35px;
        transform: rotate(315deg) scale(0.77);
      }
    }
  }
  .jiaJian {
    width: 100%;
    height: 45px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 0 10px;
    transform: translateY(-20px);
    > div:nth-of-type(1),
    > div:nth-of-type(3) {
      width: 23px;
      height: 23px;
      border-radius: 15px;
      text-align: center;
      font-size: 18px;
      cursor: pointer;
      caret-color: rgba(0, 0, 0, 0);
    }
    > div:nth-of-type(1):hover,
    > div:nth-of-type(3):hover {
      background: #01aafd;
      color: #fff;
    }
    > div:nth-of-type(1) {
      line-height: 18px;
    }
    > div:nth-of-type(3) {
      line-height: 22px;
    }
  }
  .sliderClass {
    width: 100%;
    height: 45px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 0 15px;

    > .el-slider {
      width: 50%;
    }
  }
  .switchClass {
    width: 100%;
    height: 45px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 0 22px;
    transform: translateY(-20px);
  }
}
::v-deep .el-switch__label {
  color: #0a6591 !important;
}
::v-deep .el-switch__label.is-active {
  color: #ff9900 !important;
}
::v-deep .el-switch__core {
  height: 14px;
}
::v-deep .el-switch__core:after {
  height: 12px;
  width: 12px;
  top: 0;
}
::v-deep .el-switch.is-checked .el-switch__core::after {
  margin-left: -12px;
}
.paginationWorkbench {
  position: fixed;
  bottom: 350px !important;
  height: 60px;
}

::v-deep .el-dialog__wrapper {
  pointer-events: none !important;
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
