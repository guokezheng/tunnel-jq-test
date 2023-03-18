<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="details"
      :before-close="cancel"
      class="detailsDialog"
      :close-on-click-modal="false"
      ref="upload"
      :modal="false"
      append-to-body
    >
      <!-- append-to-body -->

      <!--  -->
      <div class="videoDialogBox">
        <div
          style="display: none"
          class="processButton"
          @click="openProcess()"
          :class="processType ? 'el-icon-s-fold' : 'el-icon-s-unfold'"
        >
          预警处置
        </div>
        <div class="dialogBg">
          <div style="padding-bottom:15px;">
            事发时抓图或录像
          </div>
          <!-- <video :src="eventForm.videoUrl" controls muted loop fluid></video> -->
          <div class="picBox" v-if="eventForm.iconUrlList.length >= 1">
            <swiper class="swiper gallery-top" :options="swiperOptionTop" ref="swiperTop">
              <!-- slides -->
              <swiper-slide  v-for="(item,index) in eventForm.iconUrlList" :key="index" :class="'slide-'+index">
                <video :src="item.imgUrl" :poster="item.imgUrl" v-if="index == 0" autoplay muted loop></video>
                <img :src="item.imgUrl" style="width:100%;height:100%;" v-if="index != 0">
              </swiper-slide>
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
              <!-- <div class="swiper-scrollbar"   slot="scrollbar"></div> -->
            </swiper>
            <swiper class="swiper gallery-thumbs" :options="swiperOptionThumbs" ref="swiperThumbs">
              <swiper-slide v-for="(item,index) in eventForm.iconUrlList" :key="index" :class="'slide-'+index">
                <video :src="item.imgUrl" :poster="item.imgUrl" v-if="index == 0" autoplay muted loop></video>
                <img :src="item.imgUrl" style="width:100%;height:100%;" v-if="index != 0">
              </swiper-slide>
            </swiper>
          </div>
          <div v-if="eventForm.iconUrlList.length < 1" style="height: 89%;">
            <el-image
              style="width: 100%; height: 100%"
              :src="noPic"
              :fit="contain">
            </el-image>
          </div>
        </div>
        <div class="dialogBg dialogBg2">
          <div style="padding-bottom:15px;">实时视频<span>(事发位置最近的监控视频)</span></div>
          <el-carousel trigger="click" :autoplay="false" v-if="videoList.length >= 1 && videoList[0] != null">
            <el-carousel-item v-for="(item, index) in videoList" :key="index" >
              <videoPlayer
                v-if="item.liveUrl != null && item.liveUrl != ''"
                :rtsp="item.liveUrl"
                :open="cameraPlayer"
              ></videoPlayer>
            </el-carousel-item>
          </el-carousel>
          <el-image
            v-if="videoList.length < 1 || videoList[0] == null"
            :src="noDataUrl"
            :fit="contain">
          </el-image>
          <!-- <video
            id="h5sVideo2"
            class="h5video_"
            controls
            muted
            loop
            autoplay
            webkit-playsinline
            playsinline
            disablePictureInPicture="true"
            controlslist="nodownload noplaybackrate noremoteplayback"
            style="width: 100%; height: 290px; object-fit: cover; z-index: -100"
          ></video> -->
        </div>
      </div>
      <div class="dialogForm">
        <el-form :model="eventForm" label-width="80px" :rules="rules">
          <el-row style="display: flex; flex-wrap: wrap">
            <el-col :span="8">
              <el-form-item label="告警来源" prop="eventSource">
                <el-select
                  v-model="eventForm.eventSource"
                  disabled
                  placeholder="请选择告警来源"
                  style="width: calc(100% - 10px)"
                >
                  <el-option
                    v-for="item in fromList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="告警时间" prop="eventTime">
                <el-date-picker
                  clearable
                  size="small"
                  disabled
                  v-model="eventForm.eventTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择告警时间"
                  style="width: calc(100% - 10px)"
                  :picker-options="setDisabled"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="预计解除时间"
                prop="endTime"
                label-width="100px"
              >
                <el-date-picker
                  clearable
                  size="small"
                  v-model="eventForm.endTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择预计解除时间"
                  style="width: calc(100% - 10px)"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属隧道" prop="tunnelName">
                <el-select
                  v-model="eventForm.tunnelName"
                  placeholder="请选择所属隧道"
                  clearable
                  size="small"
                  disabled
                  style="width: calc(100% - 10px)"
                >
                  <el-option
                    v-for="item in tunnelList"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件起点">
                <el-row>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeNum1"
                      placeholder="Km"
                      width="100%"
                    >
                      <template slot="prepend">K</template>
                    </el-input>
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeNum2"
                      placeholder="m"
                      width="100%"
                    />
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件终点" label-width="100px">
                <el-row>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeEndNum1"
                      placeholder="Km"
                      width="100%"
                    >
                      <template slot="prepend">K</template>
                    </el-input>
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeEndNum2"
                      placeholder="m"
                      width="100%"
                    />
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="影响车道">
                <el-row>
                  <el-col :span="11">
                    <el-select
                      v-model="eventForm.direction"
                      placeholder="方向"
                      clearable
                      size="small"
                      style="width: 100%"
                    >
                      <el-option
                        v-for="item in directionList"
                        :key="item.dictValue"
                        :label="item.dictLabel"
                        :value="item.dictValue"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="11">
                    <el-select
                      v-model="eventForm.laneNo"
                      placeholder="车道"
                      clearable
                      size="small"
                      style="width: 100%; margin-left: 8px"
                    >
                      <el-option
                        v-for="(item,index) in chezhiLaneList"
                        :key="index"
                        :label="item.laneName"
                        :value="item.laneId"
                      />
                    </el-select>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>

            <el-col :span="16">
              <el-form-item label="事件车辆" prop="confidenceList">
                <el-input
                  v-model="eventForm.confidenceList"
                  :disabled="detailsDisabled"
                  style="width: calc(100% - 10px)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="事件描述" prop="eventDescription">
                <el-input
                  v-model="eventForm.eventDescription"
                  placeholder="事件描述"
                  :disabled="detailsDisabled"
                  style="width: calc(100% - 10px)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="activeName == '1'">
              <el-form-item label="事件类型" prop="eventTypeId">
                <el-select
                  v-model="eventForm.eventTypeId"
                  clearable
                  size="small"
                  style="width: 100%;"
                >
                <el-option
                  v-for="(item, index) in eventTypeData"
                  :key="index"
                  :label="item.simplifyName"
                  :value="item.id"
                />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="activeName == '0'">
              <el-form-item label="事件类型" prop="eventTypeId">
                <el-select
                  v-model="eventForm.eventTypeId"
                  clearable
                  size="small"
                  style="width: 100%;"
                  @change="getReservePlanData"
                >
                <el-option
                  v-for="(item, index) in eventTypeData"
                  :key="index"
                  :label="item.simplifyName"
                  :value="item.id"
                />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事件等级" prop="eventGrade">
                <el-select
                  v-model="eventForm.eventGrade"
                  clearable
                  size="small"
                  style="width: calc(100% - 10px)"
                  @change="getReservePlanData"
                >
                  <el-option
                    v-for="(item, index) in eventGradeList"
                    :key="index"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-show="prevControlType == '0'">
              <el-form-item label="复核结果" prop="eventState">
                <el-radio-group v-model="eventForm.eventState" @input="eventStateChange">
                  <el-radio v-for="item in radioList"
                  :key="item.value"
                  :label="item.value"
                  :value="item.value">
                  {{item.label}}
                  </el-radio>
                </el-radio-group>
                <span style="color:#c59105;">(请根据复核判定结果选择)</span>
              </el-form-item>
            </el-col>
            <div v-show="prevControlType == '0'">
              <el-col :span="24" v-show="eventForm.eventState == 4">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventForm.reviewRemark" @change="reviewRemarkChange">
                    <el-checkbox-button label="已线下处理" value="已线下处理"></el-checkbox-button>
                    <el-checkbox-button label="车辆已驶离" value="车辆已驶离"></el-checkbox-button>
                    <el-checkbox-button label="施工车辆" value="施工车辆"></el-checkbox-button>
                    <el-checkbox-button label="正常施工作业" value="正常施工作业"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventForm.eventState ==2">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventForm.reviewRemark" @change="reviewRemarkChange">
                    <el-checkbox-button label="稍后处理" value="稍后处理"></el-checkbox-button>
                    <el-checkbox-button label="持续跟踪，事态发展情况" value="持续跟踪，事态发展情况"></el-checkbox-button>
                    <el-checkbox-button label="已通知高警现场处理" value="已通知高警现场处理"></el-checkbox-button>
                    <el-checkbox-button label="已通知路政现场处理" value="已通知路政现场处理"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventForm.eventState == 5">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventForm.reviewRemark" @change="reviewRemarkChange">
                    <el-checkbox-button label="系统误报" value="系统误报"></el-checkbox-button>
                    <el-checkbox-button label="误报或涉事车俩已驶离" value="误报或涉事车俩已驶离"></el-checkbox-button>
                    <el-checkbox-button label="无法复核事发情况" value="无法复核事发情况"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventForm.eventState == 0">
                <el-form-item prop="currencyId">
                  <el-select v-model="eventForm.currencyId" placeholder="请选择预案" @change="this.$forceUpdate()">
                    <el-option
                      v-for="item in ReservePlanList"
                      :key="item.id"
                      :label="item.planName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <span style="color:#c59105;">(事件处置预案根据事件类型、事件等级智能推荐,处置过程中允许升级及更改预案)</span>
                </el-form-item>
              </el-col>
              <el-col v-show="eventIsShow(eventForm.reviewRemark,eventForm.eventState)">
                <el-form-item>
                  <el-input placeholder="请输入其他原因内容" v-model="eventForm.otherContent"></el-input>
                </el-form-item>
              </el-col>
            </div>
          </el-row>
        </el-form>
      </div>
      <div class="dialogFooterButton">
        <div @click="submitDialog" v-show="detailsButtonType == 2">
          复核提交
        </div>
        <!-- <div
          v-show="detailsButtonType == 2 && activeName == '0'"
          @click="management(eventForm.id)"
        >
          应急调度
        </div> -->
        <div
          v-show="detailsButtonType == 2 && activeName == '1'"
          @click="openProcess(1, eventForm.id)"
        >
          处置
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import $ from "jquery";
import { mapState } from 'vuex';
import { displayH5sVideoAll } from "@/api/icyH5stream";

import bus from "@/utils/bus";
import { loadPicture } from "@/api/equipment/type/api.js";
import {
  image,
  video,
  userConfirm,
  getEventCamera,
} from "@/api/eventDialog/api.js";
import { listEventType } from "@/api/event/eventType";
import {
  getTunnelList,
  getTunnelLane,
  updateEvent,
  listEvent,
  eventFlowList,
  getEvent,
  getReservePlanData, } from "@/api/event/event";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listDevices, videoStreaming,getDeviceById } from "@/api/equipment/eqlist/api";
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
export default {
  name: "eventDialog",
  // props: ["eventId"],
  components: {
    videoPlayer,
    // Swiper,
    // SwiperSlide,
  },
  data() {
    return {
      queryParams1: {
        pageNum: 1,
        pageSize: 10,
        faultType:null,
        faultDescription:"",
        tunnelId:"",
      },
      noPic:require("@/assets/image/noPic.png"),
      noDataUrl:require("@/assets/image/noVideo.png"),
      contain:"contain",
      noPic:require("@/assets/image/noPic.png"),
      prevControlType:'',
      details: false,
      urls: [],
      videoUrl: "",
      row11: null,
      // event: [{}],
      eventMes: {},
      eventTypeData: [],
      eventId: "",
      video1:'',
      video2:'',
      title:'',
      processType: false,
      swiperOptionTop: {
        loop: true,
        loopedSlides: 5, // looped slides should be the same
        spaceBetween: 10,
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        }
      },
      swiperOptionThumbs: {
        loop: true,
        loopedSlides: 5, // looped slides should be the same
        spaceBetween: 10,
        centeredSlides: true,
        slidesPerView: 'auto',
        touchRatio: 0.2,
        slideToClickedSlide: true
      },
      eventForm: {
        stakeNum1: "",
        stakeNum2: "",
        stakeEndNum1: "",
        stakeEndNum2: "",
        iconUrlList: [],
        reviewRemark:[],
        eventState:"1",
      },
      iconUrlListAll: [],
      ReservePlanList:[],
      detailsButtonType: 1,
      activeName: "0",
      eventGradeList: "", //事件等级
      detailsDisabled: false,
      videoList: [],
      // 表单校验
      rules: {
        /*  tunnelId: [{required: true, message: '请选择隧道名称', trigger: 'blur'}], */
        eventTitle: [
          { required: true, message: "请输入事件标题", trigger: "blur" },
        ],
        eventTypeId: [
          { required: true, message: "请选择事件类型", trigger: "change" },
        ],
        eventGrade: [
          { required: true, message: "请选择事件级别", trigger: "change" },
        ],
        eventLocation: [
          { required: true, message: "请输入位置", trigger: "blur" },
        ],
        eventDescription: [
          { required: true, message: "请输入内容", trigger: "blur" },
        ],
        faultLevel: [
          { required: true, message: "请选择故障等级", trigger: "faultLevel" },
        ],
        faultLocation: [
          {
            required: true,
            message: "请填写故障位置",
            trigger: "faultLocation",
          },
        ],
        faultType: [
          {
            required: true,
            message: "请选中故障类型",
            trigger: "faultType",
          },
        ],
        faultFxtime: [
          { required: true, message: "请填写发现时间", trigger: "faultFxtime" },
        ],
        faultCxtime: [
          { required: true, message: "请填写持续时间", trigger: "faultCxtime" },
        ],
        eqId: [{ required: true, message: "请填写设备名称", trigger: "eqId" }],
        eqStatus: [
          {
            required: true,
            message: "请选中设备填报状态",
            trigger: "eqStatus",
          },
        ],
        falltRemoveStatue: [
          {
            required: true,
            message: "请选中消除状态",
            trigger: "falltRemoveStatue",
          },
        ],
        falltRemoveStatu: [
          {
            required: true,
            message: "请选中消除状态",
            trigger: "falltRemoveStatue",
          },
        ],
        tunnelId: [
          {
            required: true,
            message: "请选中所在路段隧道",
            trigger: "tunnelId",
          },
        ],
      },
      fromList:[],
      setDisabled: {
        disabledDate(time) {
          return time.getTime() > Date.now(); // 可选历史天、可选当前天、不可选未来天
        },
      },
      tunnelList: [],
      directionList:[],
      chezhiLaneList: [],
      chezhiLaneList1: [
        {
          laneId: "1",
          laneName: "一车道",
        },
      ],
      chezhiLaneList2: [
        {
          laneId: "1",
          laneName: "一车道",
        },
        {
          laneId: "2",
          laneName: "二车道",
        },
      ],
      chezhiLaneList3: [
        {
          laneId: "1",
          laneName: "一车道",
        },
        {
          laneId: "2",
          laneName: "二车道",
        },
        {
          laneId: "3",
          laneName: "三车道",
        },
      ],
      radioList:
      [
        {label:"确认(已确认)",value:'4'},
        {label:"挂起(稍后处理)",value:'2'},
        {label:"误报",value:'5'},
        {label:"突发事件处置",value:'0'},
      ],
      manageStation: this.$cache.local.get("manageStation"),
      manageStationSelect: this.$cache.local.get("manageStationSelect"),
    };
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      this.manageStationSelect = newVal;
      this.queryParams.tunnelId = newVal;
      this.queryParams.eventTypeId = "";
      this.getList();
      this.getTunnelLane();
    },
  },
  async created() {
    console.log('createdcreatedcreatedcreated');

  },
  mounted() {
    bus.$on("getPicId", (e) => {
      this.eventId = e;
      this.init(e);
    });
    this.getTunnel();
    this.getEventTypeList();
    this.getTunnelLane();
    // 事件来源
    this.getDicts("sd_event_source").then((data) => {
      this.fromList = data.data;
    });
    this.getDicts("sd_direction").then((response) => {
      this.directionList = response.data;
    });
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeList = response.data;
    });
  },
  beforeCreate() {

  },
  destroy(){
    bus.$off("getPicId");
  },
  methods: {
    init(id) {
      if (id) {
        const param = {
          id: id,
        };
        getEvent(id).then(res=>{
          // 获取事件类型
          // 交通事件 0
          // 主动安全 1
          // 设备故障 2
          this.prevControlType = res.data.prevControlType;
          this.detailsButton(res.data,2);
        });

      }
      this.details = true;
    },
    // 获取车道数
    getTunnelLane() {
      getTunnelLane(this.manageStationSelect).then((res) => {
        this.chezhiLaneList = [];
        if (res.data.lane == 1) {
          this.chezhiLaneList = this.chezhiLaneList1;
        } else if (res.data.lane == 2) {
          this.chezhiLaneList = this.chezhiLaneList2;
        } else if (res.data.lane == 3) {
          this.chezhiLaneList = this.chezhiLaneList3;
        }
      });
    },
    /** 所属隧道 */
    getTunnel() {
      // if (!this.queryParams.deptId) {
        listTunnels().then((response) => {
          this.tunnelList = response.rows;
        });
      // }
    },
    // 查询方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    eventIsShow(value,state){
      if(value != null){
        if(state != '0' && value.includes('其他')){
          return true
        }
      }else{
        return false
      }
    },
    // 复核弹窗内单选改变事件
    eventStateChange(){
      this.eventForm.reviewRemark = [];
    },
    // 复核提交
    submitDialog() {
      if (this.eventForm.stakeNum1 && this.eventForm.stakeNum2) {
        this.eventForm.stakeNum =
          "K" + this.eventForm.stakeNum1 + "+" + this.eventForm.stakeNum2;
      }
      if (this.eventForm.stakeEndNum1 && this.eventForm.stakeEndNum2) {
        this.eventForm.stakeEndNum =
          "K" + this.eventForm.stakeEndNum1 + "+" + this.eventForm.stakeEndNum2;
      }
      // delete this.eventForm['confidenceList'];
      this.eventForm.searchValue = this.activeName;
      if(this.eventForm.reviewRemark.includes('其他')){
        this.eventForm.reviewRemark = this.eventForm.reviewRemark.toString() + ':' + this.eventForm.otherContent
      }else{
        this.eventForm.reviewRemark = this.eventForm.reviewRemark.toString()
      }
      if(this.eventForm.eventState == '0' && this.eventForm.currencyId == ''  || this.eventForm.currencyId == null){
        return this.$modal.msgWarning("请选择事件处置预案");
      }

      if(this.eventForm.eventState == '0' && this.eventForm.currencyId){
        this.$router.push({
          path: "/emergency/administration/dispatch",
          query: { id: this.eventForm.id },
        });
      }
      this.details = false;
      updateEvent(this.eventForm).then((response) => {
        this.processDialog = false;
        this.closeProcessDialog = false;
        this.processType = false;
        // this.details = false;
        this.$modal.msgSuccess("修改成功");
      });
    },
    openProcess(type, id) {
      if (type && id) {
        this.processType = true;
        this.processDialog = true;
        const param = {
          id: id,
          eventState: 0,
        };
        updateEvent(param).then((res) => {
          this.$modal.msgSuccess("正在处理");
          this.evtHandle();
        });
      } else {
        if (this.processType == true) {
          this.processDialog = false;
          // this.details = true;
          // this.closeProcessDialog = false;
          this.processType = false;
        } else {
          this.processType = true;
          this.processDialog = true;
          // this.details = true;
          // this.closeProcessDialog = true;
        }
      }
    },
    reviewRemarkChange(){
      console.log(this.eventForm.reviewRemark);
    },
    /** 查询事件类型列表 */
    getEventTypeList() {
      listEventType().then((response) => {
        this.eventTypeData = response.rows;
      });
    },
    getEvtType(num) {
      for (var item of this.eventTypeData) {
        if (num == item.id) {
          return item.eventType;
        }
      }
    },
    //详情弹窗
    detailsButton(item, type) {
      console.log(item, "点击弹窗");
      this.imgUrlList = [];
      this.iconUrlListAll = [];
      if (type == 1) {
        // this.miniDialog = false;
        // this.detailsDisabled = true;
        // this.detailsButtonType = 1;
      } else {
        // this.eventForm.eventState = "4"
        this.miniDialog = true;
        this.detailsDisabled = true;
        this.detailsButtonType = 2;
        item.reviewRemark = [];
        item.eventState = '4';
      }
      this.eventTypeId = item.eventTypeId;
      this.evtId = item.id;
      this.tunnelId = item.tunnelId;
      this.direction = item.direction;
      this.details = true;
      this.eventForm = item;      console.log(this.eventForm,"this.eventFormthis.eventFormthis.eventForm");
      this.getReservePlanData();
      this.$nextTick(() => {
        const swiperTop = this.$refs.swiperTop.$el.swiper;
        const swiperThumbs = this.$refs.swiperThumbs.$el.swiper;
        swiperTop.controller.control = swiperThumbs;
        swiperThumbs.controller.control = swiperTop;
      })
      this.getEventList();
      if (item.stakeNum) {
        this.$set(
          this.eventForm,
          "stakeNum1",
          item.stakeNum.split("+")[0].substr(1)
        );
        this.$set(this.eventForm, "stakeNum2", item.stakeNum.split("+")[1]);
      }
      if (item.stakeEndNum) {
        this.$set(
          this.eventForm,
          "stakeEndNum1",
          item.stakeEndNum.split("+")[0].substr(1)
        );
        this.$set(
          this.eventForm,
          "stakeEndNum2",
          item.stakeEndNum.split("+")[1]
        );
      }
      this.title = item.eventTitle;
      // 获取实时视频
      this.getVideoUrl(item);
      // 获取实时视频截图
      // this.getImgUrl(item);
      this.getImgUrls(item);
    },
    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.eventForm.id }).then((res) => {
        this.eventWarnList = res.rows;
      });
    },
    getReservePlanData(){
      console.log('触发了');
      this.ReservePlanList = [];
      this.eventForm.currencyId = '';
      let data = {
        tunnelId:this.eventForm.tunnelId,
        planTypeId:this.eventForm.eventTypeId,
        direction:this.eventForm.direction,
        eventGrade:this.eventForm.eventGrade,
      }
      getReservePlanData(data).then(res=>{
        this.ReservePlanList = res.data;
        console.log(this.ReservePlanList);
        if(this.ReservePlanList.length > 0){
          this.eventForm.currencyId = this.ReservePlanList[0].id
        }
      })
    },
    getImgUrls(item){
      this.urlsList = [];
      this.urlsAll = [];
      const param = {
        businessId: item.id,
      };
      image(param).then((response) => {
        this.urls = response.data;
        this.arrowRight2 = false;
        this.urlsList = this.urls;
        console.log(response.data,this.urlsList);
      });
    },
    getVideoUrl(item) {
      this.cameraPlayer = false;
      console.log(item,"itemitem")
      // getEventCamera(item.tunnelId, item.stakeNum, item.direction).then((res)=>{
      //   getDeviceById(res.data[0].eqId).then((response)=>{
      //     console.log(response,"00000000000000000")
      //     displayH5sVideoAll(response.data.secureKey,'h5sVideo2',2);
      //   })
      // })

      this.videoList = [];
      getEventCamera(item.tunnelId, item.stakeNum, item.direction).then(
        (res) => {
          if (res.data) {
            // let videoId = res.data[0].eqId
            let videoId = "";
            for (let item of res.data) {
              videoId = item.eqId;
              videoStreaming(videoId).then((response) => {
                if (response.code == 200) {
                  console.log(response.data,"视频视频视频视频视频");
                  // return false;
                  this.videoList.push(response.data);
                  this.cameraPlayer = true;
                }
              });
            }
            console.log(this.videoList, " this.videoList");
          }
          console.log(this.videoList, "this.videoList");
        }
      );
    },
    getUrl(id) {
      const param3 = {
        businessId: id,
      };
      const param4 = {
        id: id,
      };
      image(param3).then((response) => {
        this.urls = response.data;
      });
      video(param4).then((response) => {
        if(response.data.videoUrl){
          this.videoUrl = response.data.videoUrl;
        }
      });
    },

    // 忽略事件
    handleIgnore(event) {
      if (event) {
        const param = {
          id: event.id,
          eventState: "2",
        };
        updateEvent(param).then((response) => {
          this.$modal.msgSuccess("已成功忽略");
        });
      }
      // this.$emit("closePicDialog");
      bus.$emit("closePicDialog");
      bus.$emit("forceUpdateTable", event.id);

      // bus.$emit("closeTableDialog");

      // this.eventPicDialog = false;
    },
    // 处理 跳转应急调度
    handleDispatch(event) {
      const param = {
        id: event.id,
        eventState: "0",
      };
      updateEvent(param).then(() => {
        this.$modal.msgSuccess("开始处理事件");
      });
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: event.id },
      });
      if (this.eventMes.eventState == "3") {
        userConfirm(event.id).then(() => {});
      }
      // bus.$emit("closePicDialog");
      bus.$emit("closePicDialog");
      bus.$emit("closeDialog");
      // this.eventPicDialog = false;
    },
    // 弹摄像机弹窗
    openVideoDialog(id) {
      setTimeout(()=>{
        bus.$emit("getVideoDialog",id)

      },200)
      bus.$emit("openVideoDialog")

      bus.$emit("closePicDialog")
    },
    closeDialogTable() {
      bus.$emit("closePicDialog");
      // this.eventPicDialog = false;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.details = false;
      this.processDialog = false;
      this.processType = false;

      this.reset();
      if (this.detailsButtonType == 2) {
        this.getList();
      }
    },
        // 表单重置
        reset() {
      this.form = {
        id: null,
        currencyId:'',
        tunnelId: null,
        eventTypeId: null,
        eventTitle: null,
        eventTime: null,
        eventState: null,
        eventGrade: "0",
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
        reservePlanId: null,
        flowId: null,
        warningId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        eqTunnelId: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: null,
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: 0,
      };
      this.fileList = [];
      this.removeIds = [];
      this.resetForm("form");
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-carousel__arrow{background-color: rgba(31, 45, 61, 0.8);}
::v-deep .el-carousel__arrow:hover{background-color: rgba(31, 45, 61, 0.8);}
.gallery-thumbs {
  height: 20% !important;
  box-sizing: border-box;
  padding: 10px 0;
}
.gallery-thumbs {
  height: 20% !important;
  box-sizing: border-box;
  padding: 10px 0;
}
.gallery-thumbs .swiper-slide {
  width: 25%;
  height: 100%;
  opacity: 0.4;
}
.gallery-thumbs .swiper-slide-active {
  opacity: 1;
}
.videoDialogBox {
  width: 100%;
  height: 400px;
  display: flex;
  justify-content: space-between;
  align-item:center;
  position: relative;
  .processButton {
    position: absolute;
    top: 20px;
    right: -15px;
    width: 25px;
    height: 100px;
    cursor: pointer;
    background: #39adff;
    text-align: center;
    line-height: 18px;
    color: #fff;
  }
  .processButton::before {
    font-size: 14px;
    color: #fff;
  }
  .dialogBg2 {
    width: 55% !important;
    padding: 0px 20px 10px 10px !important;
    // .video-box {
    //   height: calc(90%) !important;
    // }
    ::v-deep .el-carousel__container{
      height:315px;
    }
    ::v-deep .el-image{
      height: 80%;
      width: 100%;
      image{width:100%;height:100%;}
    }
  }
  .dialogBg {
    background: #f7f7f7;
    // height: 100%;
    width: 45%;
    color: #0087e7;
    padding: 0px 10px 10px 20px;
    span {
      color: #767676 !important;
      padding-left: 10px;
    }
    video {
      width: 100%;
      height: 73%;
    }
    .picBox {
      width: 100%;
      // height: calc(24% - 25px);
      margin-top: 5px;
      // border: solid 1px red;
      // display: flex;
      // justify-content: center;
      // align-items: center;
      .picList {
        width: 100%;
        height: 100%;
        // display: flex;
        // justify-content: left;
        > div {
          overflow: hidden;
          margin-left: 10px;
          width: 21%;
          height: 100%;
          display: inline-block;
          > .el-image {
            width: auto;
            height: 100%;
            overflow: hidden;
            // border: solid 1px blue;
            margin: 0 auto;
          }
        }
      }
      .turnPages {
        width: 20px !important;
        height: 20px !important;
        border: solid 1px #0087e7;
        border-radius: 10px;
        text-align: center;
        cursor: pointer;
        caret-color: rgba(0, 0, 0, 0);
      }
      .turnPages:hover {
        background: #0087e7;
        color: #fff;
      }
      .noPic {
        border: solid 1px #0087e7;
        display: flex;
        justify-content: center;
        align-items: center;
        img {
          width: 50%;
        }
      }
    }
  }
}
.dialogFooterButton {
  width: 100%;
  height: 30px;
  margin-top: 10px;
  display: flex;
  justify-content: right;
  div {
    margin-right: 20px;
    width: 80px;
    height: 28px;
    border-radius: 14px;
    text-align: center;
    line-height: 28px;
    color: white;
    cursor: pointer;
  }
  div:nth-of-type(1) {
    background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
  }
  div:nth-of-type(2) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
  div:nth-of-type(3) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
}
::v-deep .el-input.is-disabled .el-input__inner {
    background-color: #f5f7fa;
    border-color: #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;
}
.detailsDialog {
  // height: 84%;
  // z-index: 2008 !important;
  width: 60%;
  position: absolute;
  left: 24%;
  top:5%;
}
::v-deep .el-dialog {
  width: 100% !important;
  // height: 100%;
  position: absolute !important;
  left: 0 !important;
  margin: 0;
  box-shadow: none;
  background: transparent;
  .el-dialog__title{
    color: #fff !important;
  }
}
::v-deep .el-dialog:not(.is-fullscreen) {
  margin-top: 0vh !important;
}
// ::v-deep .el-dialog__header {
//   display: none;
// }
::v-deep .el-dialog__body {
  padding: 20px;
  height: calc(100% - 60px);
}
.evenDialogBox {
  width: 52%;
  height: 660px;
  border: solid 1px rgba($color: #0198ff, $alpha: 0.5);
  position: absolute;
  top: 10%;
  left: 25%;
  background-color: #071930;
  z-index: 100;
  .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    justify-content: space-between;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    margin-bottom: 20px;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
}
.eventLeft {
  width: 65%;
  height: 100%;
  .video {
    width: 100%;
    height: 390px;
    margin-top: 0px !important;
  }
  .pic {
    width: 100%;
    height: calc(100% - 400px);
    margin-top: 10px;
    position: relative;
  }
}
::v-deep .el-carousel__mask {
  background-color: transparent;
}

.eventRight {
  width: 35%;
  height: 100%;
  color: white;
  font-size: 16px;
  padding-left: 20px;
  .eventRow {
    display: flex;
    height: 45px;
    > div:nth-of-type(1) {
      width: 140px;
      color: #0198ff;
    }
    > div {
      line-height: 22px;
    }
  }
  .button {
    width: 35%;
    height: 40px;
    margin-top: 15px;
    border-radius: 20px;
    // border: solid 1px #00c8ff;
    text-align: center;
    line-height: 40px;
    margin-left: 35px;
    cursor: pointer;
    color: #fff;
  }
  .handle {
    background: linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
  }
  .handle:hover {
    background-color: #e1aa43;
    color: white;
  }
  .ignore {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
  .ignore:hover {
    background-color: #19b9ea;
    color: white;
  }
  .next {
    color: #fff;
  }
  .next:hover {
    background-color: #ddd;
    color: #005487;
  }
}
.videoDialog {
  width: 70%;
  height: 600px;
  border: solid 10px rgba($color: #5ac4e5, $alpha: 0.3);
  position: absolute;
  top: 15%;
  left: 15%;
  background-color: white;
  border-radius: 10px;
  padding: 10px;
  display: flex;
}
.closeButton {
  width: 40px;
  height: 40px;
  border: solid 5px rgba($color: #5ac4e5, $alpha: 0.3);
  position: absolute;
  top: -20px;
  left: calc(100% - 15px);
  border-radius: 20px;
  background-color: white;
  text-align: center;
  line-height: 35px;
  cursor: pointer;
}
.icon {
  width: 20px;
  height: 22px;
  margin-left: 5px;
}
::v-deep .el-icon-close {
  font-size: 24px !important;
  color: #005487;
  font-weight: bold;
}
// 滚动条
::-webkit-scrollbar-track-piece {
  background-color: rgba($color: #00c2ff, $alpha: 0.1);
  border-left: 1px solid rgba(0, 0, 0, 0);
}
::-webkit-scrollbar {
  width: 0px;
  height: 10px;
}
::-webkit-scrollbar-thumb {
  background-color: rgba($color: #00c2ff, $alpha: 0.6);
  background-clip: padding-box;
  border-radius: 10px;
  min-height: 28px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #00c2ff;
}
::v-deep .el-carousel--horizontal{
  height: 100% !important;
}
</style>
