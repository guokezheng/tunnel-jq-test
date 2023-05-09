<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="details"
      :before-close="cancel"
      width="60%"
      text-align="left"
      :style="processType ? 'left: 13%' : ''"
      class="detailsDialog"
      :close-on-click-modal="false"
      ref="upload"
      :modal="false"
      append-to-body
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
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
          <div style="padding:15px 0;">
            事发时抓图或录像
          </div>
          <!-- <video :src="eventForm.videoUrl" controls muted loop fluid></video> -->
          <div class="picBox">
            <swiper class="swiper gallery-top" :options="swiperOptionTop" ref="swiperTop"
            v-show="eventFormDetail.iconUrlList.length >= 1">
              <!-- slides -->
              <swiper-slide  v-for="(item, index) in eventFormDetail.iconUrlList" :key="index" :class="'slide-' + index">
                <video :src="item.imgUrl"
                       :poster="item.imgUrl" v-if="index == 0"
                       @click="openPicDialog(eventFormDetail)"
                       class="leftVideo"
                       autoplay muted loop>
                </video>
                <img :src="item.imgUrl" style="width:100%;height:100%;"
                     v-if="index != 0" @click="clickImg(item.imgUrl)" />
              </swiper-slide>
              <!-- <div class="swiper-scrollbar"   slot="scrollbar"></div> -->
            </swiper>
            <swiper class="swiper gallery-thumbs" :options="swiperOptionThumbs"
              ref="swiperThumbs" v-show="eventFormDetail.iconUrlList.length >= 1">
              <swiper-slide v-for="(item, index) in eventFormDetail.iconUrlList"
                :key="index" :class="'slide-' + index">
                <video :src="item.imgUrl" :poster="item.imgUrl"
                  v-if="index == 0" autoplay muted loop>
                </video>
                <img :src="item.imgUrl" style="width:100%;height:100%;" v-if="index != 0">
              </swiper-slide>
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
            <div v-show="eventFormDetail.iconUrlList.length < 1" style="width: 100%; height: 329px;">
              <el-image
                style="width: 100%; height: 100%"
                :src="noPic"
                :fit="contain">
              </el-image>
            </div>
          </div>
        </div>
        <div class="dialogBg dialogBg2">
          <div style="padding:15px 0;">实时视频<span>(事发位置最近的监控视频)</span></div>
          <div class="picBox">
            <el-carousel trigger="click" :autoplay="false" v-if="videoList.length >= 1">
              <el-carousel-item v-for="(item, index) in videoList" :key="index" v-if="item.liveUrl != null">
                <videoPlayer
                  v-show="item.liveUrl != null && item.liveUrl != ''"
                  :rtsp="item.liveUrl"
                  :open="cameraPlayer"
                ></videoPlayer>
              </el-carousel-item>
            </el-carousel>
            <el-image
              v-show="videoList.length < 1"
              :src="noDataUrl"
              :fit="contain">
            </el-image>
          </div>

          <!-- 现场用这个 -->
          <!-- <video
            id="h5sVideo1"
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
        <el-form ref="eventFormDetail" :model="eventFormDetail" label-width="80px">
          <el-row style="display: flex; flex-wrap: wrap">
            <el-col :span="8">
              <el-form-item label="告警来源" prop="eventSource">
                <el-select
                  v-model="eventFormDetail.eventSource"
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
                  v-model="eventFormDetail.eventTime"
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
                  @change="changeEndTime"
                  clearable
                  size="small"
                  v-model="eventFormDetail.endTime"
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
                  v-model="eventFormDetail.tunnelName"
                  placeholder="请选择所属隧道"
                  clearable
                  size="small"
                  disabled
                  style="width: calc(100% - 10px)"
                  @change="getReservePlanData"
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
                      v-model="eventFormDetail.stakeNum1"
                      placeholder="Km"
                      oninput="value=value.replace(/[^\d]/g,'')"
                      width="100%"
                    >
                      <template slot="prepend">K</template>
                    </el-input>
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventFormDetail.stakeNum2"
                      placeholder="m"
                      oninput="value=value.replace(/[^\d]/g,'')"
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
                      v-model="eventFormDetail.stakeEndNum1"
                      placeholder="Km"
                      oninput="value=value.replace(/[^\d]/g,'')"
                      width="100%"
                    >
                      <template slot="prepend">K</template>
                    </el-input>
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventFormDetail.stakeEndNum2"
                      placeholder="m"
                      oninput="value=value.replace(/[^\d]/g,'')"
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
                      v-model="eventFormDetail.direction"
                      placeholder="方向"
                      clearable
                      size="small"
                      style="width: 100%"
                      @change="getReservePlanData"
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
                      v-model="eventFormDetail.laneNo"
                      placeholder="车道"
                      clearable
                      size="small"
                      multiple
                      collapse-tags
                      style="width: 100%; margin-left: 8px"
                    >
                      <el-option
                        v-for="(item, index) in chezhiLaneList"
                        :key="index"
                        :label="item.dictLabel"
                        :value="item.dictValue"
                      />
                    </el-select>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>

            <el-col :span="16">
              <el-form-item label="事件车辆" prop="confidenceList">
                <el-input
                  v-model="eventFormDetail.confidenceList"
                  placeholder=""
                  :disabled="detailsDisabled"
                  style="width: calc(100% - 10px)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="影响描述" prop="eventDescription">
                <el-input
                  v-model="eventFormDetail.eventDescription"
                  placeholder="影响描述"
                  style="width: calc(100% - 10px)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预估类型" prop="eventTypeId">
                <el-select
                  v-model="eventFormDetail.eventTypeId"
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
              <el-form-item label="预估等级" prop="eventGrade">
                <el-select
                  v-model="eventFormDetail.eventGrade"
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
            <el-col :span="24">
              <el-form-item label="复核结果" prop="eventState">
                <el-radio-group v-model="eventFormDetail.eventState"
                                @input="eventStateChange"
                >
                  <el-radio :label="4"> 确认(已处理) </el-radio>
                  <el-radio :label="2"> 挂起(稍后处理) </el-radio>
                  <el-radio :label="5"> 误报 </el-radio>
                  <el-radio :label="0"> 突发事件处置 </el-radio>
                </el-radio-group>
                <span style="color:#c59105;">(请根据复核判定结果选择)</span>
              </el-form-item>
            </el-col>
            <div style="width:100%;">
              <el-col :span="24" v-show="eventFormDetail.eventState == 4">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventFormDetail.reviewRemark" class="checkBox">
                    <el-checkbox-button label="已线下处理" value="已线下处理"></el-checkbox-button>
                    <el-checkbox-button label="车辆已驶离" value="车辆已驶离"></el-checkbox-button>
                    <el-checkbox-button label="施工车辆" value="施工车辆"></el-checkbox-button>
                    <el-checkbox-button label="正常施工作业" value="正常施工作业"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventFormDetail.eventState == 2">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventFormDetail.reviewRemark" class="checkBox">
                    <el-checkbox-button label="稍后处理" value="稍后处理"></el-checkbox-button>
                    <el-checkbox-button label="持续跟踪，事态发展情况" value="持续跟踪，事态发展情况"></el-checkbox-button>
                    <el-checkbox-button label="已通知高警现场处理" value="已通知高警现场处理"></el-checkbox-button>
                    <el-checkbox-button label="已通知路政现场处理" value="已通知路政现场处理"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventFormDetail.eventState == 5">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group v-model="eventFormDetail.reviewRemark" class="checkBox">
                    <el-checkbox-button label="系统误报" value="系统误报"></el-checkbox-button>
                    <el-checkbox-button label="误报或涉事车辆已驶离" value="误报或涉事车辆已驶离"></el-checkbox-button>
                    <el-checkbox-button label="无法复核事发情况" value="无法复核事发情况"></el-checkbox-button>
                    <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="eventFormDetail.eventState == 0 && eventFormDetail.prevControlType == 0">
                <el-form-item prop="currencyId">
                  <el-select v-model="eventFormDetail.currencyId" placeholder="请选择预案" style="width:30%;">
                    <el-option
                      v-for="item in ReservePlanList"
                      :key="item.id"
                      :label="item.planName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <el-button size="small" v-show="eventFormDetail.currencyId" @click="openDoor(eventFormDetail)">查看</el-button>
                  <span style="color:#c59105;">(事件处置预案根据事件类型、事件等级智能推荐,处置过程中允许升级及更改预案)</span>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-if="eventFormDetail.eventState == 0 && eventFormDetail.prevControlType == 1">
                <el-form-item prop="currencyId">
                  <el-select v-model="eventFormDetail.currencyId" placeholder="请选择策略">
                    <el-option
                      v-for="item in strategyList"
                      :key="item.id"
                      :label="item.strategyName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <el-button size="small" type="primary" v-show="eventFormDetail.currencyId" @click="openDoor(eventFormDetail)">查看</el-button>
                  <span style="color:#c59105;">(事件处置预案根据事件类型、事件等级智能推荐,处置过程中允许升级及更改预案)</span>
                </el-form-item>
              </el-col>
              <el-col v-show="eventIsShow(eventFormDetail.reviewRemark, eventFormDetail.eventState)">
                <el-form-item>
                  <el-input placeholder="请输入其他原因内容" v-model="eventFormDetail.otherContent"></el-input>
                </el-form-item>
              </el-col>
            </div>
          </el-row>
        </el-form>
        <div class="dialogFooterButton">
          <div @click="submitDialog">
            复核提交
          </div>
          <!-- <div
            @click="openProcess(1, eventFormDetail.id)"
          >
            处置
          </div> -->
        </div>
      </div>
    </el-dialog>
    <!-- 视频展示 -->
    <el-dialog
      :visible.sync="picUrlDialog"
      width="70%"
      title="事件视频"
      class="videoDialog"
    >
      <div class="videoDialogClass">
        <video :src="videoUrl" controls muted loop fluid autoplay></video>
      </div>
    </el-dialog>
    <!-- 图片展示 -->
    <el-dialog
      title="抓图详情"
      :visible.sync="dialogVisibleImg"
      width="60%"
      :before-close="handleCloseImg">
      <img :src="alongImgUrl" style="width:100%;"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleImg = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisibleImg = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 复核详情展示 -->
    <el-dialog
      title="设备详情"
      :visible.sync="dialogVisibleDevice"
      width="50%"
      :before-close="handleClose">
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClickDevice">
          <el-tab-pane 
          v-for="(item,index) in DeviceDetail" 
          :key="index"
          :label="item.tableName" :name="index"></el-tab-pane>
        </el-tabs>
      </div>
      <div v-for="(item,index) in DeviceDetail" 
        :key="index" 
        v-show="deviceIndexShow == index">
        <el-table
          :data="item.devicesList"
          style="width: 100%">
          <el-table-column
            prop="eqName"
            label="设备名称"
          >
          </el-table-column>
          <el-table-column
            prop="pile"
            label="桩号"
            >
          </el-table-column>
          <el-table-column
            prop="stateName"
            label="修改后状态">
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleDevice = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisibleDevice = false">确 定</el-button>
      </span>
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
import {
  listEventType,
  getTodayEventCount,
  getEventDetail,
  handleStrategy 
} from "@/api/event/eventType";
import {
  getTunnelList,
  getTunnelLane,
  updateEvent,
  listEvent,
  eventFlowList,
  getEvent,
  getReservePlanData, } from "@/api/event/event";
import { examineDeviceDetail,getStrategyData } from "@/api/event/reservePlan";
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
      processDialog:"false",
      deviceIndexShow:0,
      activeName:'0',
      dialogVisibleDevice:false,
      DeviceDetail:[],
      alongImgUrl:'',
      dialogVisibleImg:false,
      picUrlDialog: false,
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
      // 详情弹窗内
      eventFormDetail:{
        stakeNum1: "",
        stakeNum2: "",
        stakeEndNum1: "",
        stakeEndNum2: "",
        iconUrlList: [],
        reviewRemark:[],
        eventState:"4",
      },
      swiperOptionTop: {
        loop: false,
        loopedSlides: 5, // looped slides should be the same
        spaceBetween: 10,
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        }
      },
      swiperOptionThumbs: {
        loop: false,
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
      chezhiLaneList1: [],
      chezhiLaneList2: [],
      chezhiLaneList3: [],
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
      this.getTunnelLane();
    },
  },
  created() {

  },
  mounted() {
    bus.$on("getPicId", (e) => {
      this.eventId = e;
      this.init(e);
    });
    this.getTunnel();
    this.getEventTypeList();
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
    this.getDicts("sd_lane_one").then((data) => {
      this.chezhiLaneList2 = data.data;
    });
    // 三车道
    this.getDicts("sd_lane_two").then((data) => {
      this.chezhiLaneList3 = data.data;
    });
  },
  beforeCreate() {

  },
  destroy(){
    bus.$off("getPicId");
  },
  methods: {
    getFrom(from) {
      for (let item of this.fromList) {
        if (from == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    init(id) {
      if (id) {
        const param = {
          id: id,
        };
        getEvent(id).then(res=>{
          console.log(res.data,"事件详情数据")
          this.prevControlType = res.data.prevControlType;
          this.detailsButton(res.data);
        });

      }
      this.details = true;
    },
    //详情弹窗
    detailsButton(item) {
      // 获取对应事件
      this.getEventType(item);
      console.log(item, "点击弹窗");
      this.imgUrlList = [];
      this.iconUrlListAll = [];
      this.miniDialog = true;
      this.detailsDisabled = true;
      item.reviewRemark = [];
      this.eventTypeId = item.eventTypeId;
      this.evtId = item.id;
      this.tunnelId = item.tunnelId;

      this.direction = item.direction;
      this.details = true;
      this.eventFormDetail = {...item};
      this.eventFormDetail.eventState = 4;
      if(item.prevControlType == 1){
        this.getStrategyData(item);
      }else{
        this.getReservePlanData();
      }

      this.$nextTick(() => {
        const swiperTop = this.$refs.swiperTop.$el.swiper;
        const swiperThumbs = this.$refs.swiperThumbs.$el.swiper;
        swiperTop.controller.control = swiperThumbs;
        swiperThumbs.controller.control = swiperTop;
      })
      this.getEventList();
      if (item.stakeNum) {
        this.$set(
          this.eventFormDetail,
          "stakeNum1",
          item.stakeNum.split("+")[0].substr(1)
        );
        this.$set(this.eventFormDetail, "stakeNum2", item.stakeNum.split("+")[1]);
      }
      if (item.stakeEndNum) {
        this.$set(
          this.eventFormDetail,
          "stakeEndNum1",
          item.stakeEndNum.split("+")[0].substr(1)
        );
        this.$set(
          this.eventFormDetail,
          "stakeEndNum2",
          item.stakeEndNum.split("+")[1]
        );
      }
      this.title = item.eventTitle;
      // 获取车道
      this.getTunnelLane();
      // 获取实时视频
      this.getVideoUrl(item);
      // 获取实时视频截图
      this.getImgUrl(item);
      // this.getImgUrls(item);
    },
    getStrategyData(item){
      console.log(item);
      let param = {
        "tunnelId":item.tunnelId,
        "direction":item.direction,
        "eventType":item.eventTypeId,
      }
      getStrategyData(param).then(res=>{
        console.log(res.data,"策略列表");
        this.strategyList = res.data
        this.eventFormDetail.currencyId =  res.data[0].id
      })
    },
    // 获取车道数
    getTunnelLane() {
      getTunnelLane(this.tunnelId).then((res) => {
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
      console.log(this.eventFormDetail,'1123123')
      this.$cache.local.set('currencyId',this.eventFormDetail.currencyId);

      if (this.eventFormDetail.stakeNum1 && this.eventFormDetail.stakeNum2) {
        this.eventFormDetail.stakeNum =
          "K" + this.eventFormDetail.stakeNum1 + "+" + this.eventFormDetail.stakeNum2;
      }
      if (this.eventFormDetail.stakeEndNum1 && this.eventFormDetail.stakeEndNum2) {
        this.eventFormDetail.stakeEndNum =
          "K" + this.eventFormDetail.stakeEndNum1 + "+" + this.eventFormDetail.stakeEndNum2;
      }
      if(this.eventFormDetail.reviewRemark.includes('其他')){
        this.eventFormDetail.reviewRemark = this.eventFormDetail.reviewRemark.toString() + ':' + this.eventFormDetail.otherContent
      }else{
        this.eventFormDetail.reviewRemark = this.eventFormDetail.reviewRemark.toString()
      }
      if(this.eventFormDetail.eventState == '0' && this.eventFormDetail.currencyId == ''  || this.eventFormDetail.currencyId == null){
        return this.$modal.msgWarning("请选择事件处置预案");
      }
      const currencyId = this.eventFormDetail.currencyId;
      if(this.eventFormDetail.laneNo){
        this.eventFormDetail.laneNo = this.eventFormDetail.laneNo.toString();
      }

      updateEvent(this.eventFormDetail).then((response) => {
        this.processDialog = false;
        this.closeProcessDialog = false;
        this.processType = false;
        this.details = false;
        this.$modal.msgSuccess("修改成功");
        //主动安全
        //策略不为空
        if(this.eventFormDetail.prevControlType == 1 && currencyId && this.eventFormDetail.eventState == 0){
          let id = currencyId;
          handleStrategy(id).then(res=>{
            console.log(res);
            this.$modal.msgSuccess("下发指令成功");
          })
        }
        // 1.预案不为空
        // 2.当前状态为0
        // 3.普通事件
        if(this.eventFormDetail.prevControlType == 0 && currencyId && this.eventFormDetail.eventState == 0){
          console.log('我跳转了啊~~');
          this.$router.push({
            path: "/emergency/administration/dispatch",
            query: { id: this.eventFormDetail.id },
          });
        }
        this.$cache.local.remove("currencyId")
      });
    },
    // 打开复核内详情
    openDoor(item) {
      // 点击查看按钮重置tab
      this.deviceIndexShow = 0;
      this.activeName = '0';

      let lane = "";
      if (item.laneNo == null || item.laneNo.length == 0) {
        lane = "";
      } else {
        lane = item.laneNo.toString();
      }
      let query = {
        prevControlType: item.prevControlType,
        currencyId: item.currencyId,
        id: item.id,
        laneNo: lane,
      };
      examineDeviceDetail(query).then((res) => {
        console.log(res);
        this.DeviceDetail = res.data;
        this.dialogVisibleDevice = true;
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
    // 复核内图片展示弹窗关闭事件
    handleCloseImg(done) {
      done();
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
    getEventTypeAll(){
      let prevControlType = {
        isUsable: "1",
      };
      listEventType(prevControlType).then((response) => {
        this.eventTypeDataList = [...response.rows];
      });
    },
    /** 查询事件类型列表 */
    getEventType(item) {
      let prevControlType = {
        isUsable: "1",
        prevControlType:item.prevControlType,
      };
      listEventType(prevControlType).then((response) => {
        this.eventTypeData = [...response.rows];
      });
    },
    getEvtType(num) {
      for (var item of this.eventTypeData) {
        if (num == item.id) {
          return item.eventType;
        }
      }
    },

    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.eventForm.id }).then((res) => {
        this.eventWarnList = res.rows;
      });
    },
    getReservePlanData(){
      this.ReservePlanList = [];
      this.eventFormDetail.currencyId = '';
      let data = {
        tunnelId:this.eventFormDetail.tunnelId,
        planTypeId:this.eventFormDetail.eventTypeId,
        direction:this.eventFormDetail.direction,
        eventGrade:this.eventFormDetail.eventGrade,
      }
      getReservePlanData(data).then(res=>{
        this.ReservePlanList = res.data;
        if(this.ReservePlanList.length > 0){
          this.eventFormDetail.currencyId = this.ReservePlanList[0].id
        }else{
          if(this.eventFormDetail.eventState == '0'){
            this.$modal.msgWarning("暂无相关预案");
          }
        }
      })
    },
    handleClickDevice(tab, event) {
      console.log(tab.index);
      this.deviceIndexShow = tab.index;
    },
    getImgUrl(item) {
      this.urlsList = [];
      this.urlsAll = [];
      const param = {
        businessId: item.id,
      };
      image(param).then((response) => {
        this.urls = response.data;
        if (response.data.length > 4) {
          this.arrowRight2 = true;
          for (let i = 0; i < this.urls.length; ) {
            this.urlsAll.push(this.urls.splice(0, 4));
          }
          this.urlsList = this.urlsAll[0];
          this.imgPage2 = 0;
        } else {
          this.arrowRight2 = false;
          this.urlsList = this.urls;
        }
      });
    },
    //关闭drawer
    handleClose(done) {
      done();
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
                  if(response.data != null){
                    this.videoList.push(response.data);
                    this.cameraPlayer = true;
                  }
                }
              });
            }
            console.log(this.videoList, " this.videoList");
          }
          console.log(this.videoList, "this.videoList");
        }
      );
    },
    // 点击缩略图
    clickImg(gImgUrl){
      let imgurl = gImgUrl.substr( -3,3);
      if(imgurl == 'jpg'){
        this.alongImgUrl =  gImgUrl;
        this.dialogVisibleImg = true;
      }
      console.log(imgurl);
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
    changeEndTime(){
      let startTime = new Date(this.eventFormDetail.eventTime).getTime();
      let endTime = new Date(this.eventFormDetail.endTime).getTime();
      console.log(startTime,endTime);
      if(endTime < startTime){
        this.$modal.msgWarning("结束时间必须大于开始时间");
        this.eventFormDetail.endTime = "";
      }
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
    // 打开图片变视频弹窗
    openPicDialog(item) {
      console.log(item);
      if(!item.videoUrl && !item.imgUrl){
        this.$message.warning('暂无视频');
      }else{
        this.videoUrl = item.videoUrl == undefined ? item.imgUrl : item.videoUrl;
        // this.videoUrl = item.videoUrl;
        this.picUrlDialog = true;
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.details = false;
      this.processDialog = false;
      this.processType = false;
      this.reset();
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

<style scoped lang="scss">
  ::v-deep .dialogForm .el-checkbox-button{
    background: #052C4D;
  }
  .theme-light .el-dialog .el-dialog__body .dialogForm .checkBox{
    display: unset!important;
    width: unset!important;
  }
  ::v-deep .el-timeline-item__content{
    background-color: #022443;
  }
  .scrollbar_li{
    width:145px;margin-right:15px;display:inline-block;white-space: nowrap;
    video{width: 100%;}
  }
  ::v-deep .el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell,::v-deep .el-table tr{
    background: unset!important;;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  ::v-deep .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view{
    white-space: nowrap;
  }
  ::v-deep .el-carousel__arrow{background-color: rgba(31, 45, 61, 0.8);}
  ::v-deep .el-carousel__arrow:hover{background-color: rgba(31, 45, 61, 0.8);}
  .gallery-thumbs {
    height: 75px;
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

  .chuzhi{
    background:#05afe3;
  }
  .yzx{
    color: #45d20a;
  }
  .wzx{
    color: #666666;
  }
  .incHandBox {
    height: calc(100% - 40px);
    overflow: auto;
    .incHandContent {
      display: flex;
      // color: white;
      font-size: 12px;
      padding: 10px;
      .classification {
        .type {
          width: 50px;
          height: 50px;
          // background: rgba($color: #084e84, $alpha: 0.6);
          // border: 1px solid rgba($color: #39adff, $alpha: 0.6);
          text-align: center;
        }
        .yijian {
          color: white;
          width: 50px;
          background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
          border: 1px solid #39adff;
          // padding: 10px;
          text-align: center;
        }
      }

      .heng1 {
        width: 20px;
        height: 1px;
        border-top: solid 1px #39adff;
      }
      .shu {
        width: 20px;
        border-left: solid 1px #39adff;
        border-bottom: solid 1px #39adff;
        margin-top: 20px;
      }
      .gxp{
        // margin-left: 20px;
        width:77%;
        .contentList {
          display: block;
          margin-top: 4px;
          line-height: 40px;
          padding: 0 20px;
          border-radius: 3px;
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
          img {
            width: 18px;
            height: 18px;
          }
        }
      }

      .contentList:nth-of-type(1) {
        margin-top: 0;
      }
    }
  }
  .formStyle {
    .el-form-item {
      margin-bottom: 1vh;
    }
  }
  ::v-deep .el-form-item--medium .el-form-item__label {
    font-size: 0.7vw;
  }
  ::v-deep .el-form-item--medium .el-form-item__content {
    font-size: 0.7vw;
  }
  ::v-deep .el-tabs__header {
    margin: 0 0 8px !important;
  }
  .contentListBox {
    width: 100%;
    word-wrap: break-word;
    word-break: normal;
    overflow-y: auto;
    overflow-x: hidden;
    height: 68vh;
    //display: flex;
    .contentBox {
      display: inline-flex;
      margin-right: 0.5vw;
      margin-bottom: 10px;
      position: relative;
      border-radius: 2px;
      // width:24.6%;
      .video {
        width: 40%;
        height: 100%;
        float: left;
        text-align: center;
        font-size: 0.7vw;
        // color: #2aa6ff;
        .eventBox{
          display: flex;
          justify-content: space-between;
          align-items: center;
          .eventType{
            background: rgba(228, 14, 14, 0.4);
            font-size: .675rem;
            font-weight: 600;
            color: #fff;
            // padding:5px 10px;
            width: 60%;
            height: 3vh;
            line-height: 3vh;
          }
          div{
            background: rgba(228, 14, 14, 0.2);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            font-size: .675rem;
            font-weight: 400;
            color: #fff;
            padding: 2px 10px;
            width: 40%;
          }
        }

        video {
          width: 100%;
          height: 100px;
          margin-top: 2px;
          float: left;
        }
        img {
          // width: 100%;
          height: 10vh;
          margin-top: 2px;
        }
      }

      .contentText {
        margin-top: 10px;
        font-size: 0.7vw;
        // color: #0087e7;
        margin-right: 20px;
        width: 60%;
        float: right;
        margin-left: 2px;
        .stateTab {
          position: absolute;
          top: -27px;
          right: -17px;
        }
        div {
          padding: 0.6vh 0;
          span {
            padding-left: 6px;
            font-weight: bold;
          }
        }
        .contentButton {
          display: flex;
          justify-content: space-between;
          width: 150px;
          div {
            width: 65px;
            height: 2vh;
            border-radius: 14px;
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
          }
          div:nth-of-type(1) {
            background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
          }
          div:nth-of-type(2) {
            background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
          }
        }
      }
    }
    .contentBox:nth-of-type(4n) {
      margin-right: 0px;
    }
  }

  .videoDialogBox {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items:center;
    position: relative;
    .swiper-slide{
      video{
        width: 100%;
        height: 100%;
      }
    }
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
      padding: 0px 10px 10px 10px !important;
      margin-left: 10px;
      // ::v-deep .el-carousel__container{
      //   height:378px;
      // }
      .picBox{
        height: calc(400px - 71px);
      }
      ::v-deep .el-image{
        height: calc(400px - 71px);
        width: 100%;
        image{width:100%;height:100%;}
      }
    }
    .dialogBg {
      background: #f7f7f7;
      height: 100%;
      width: 45%;
      color: #0087e7;
      padding: 0px 10px 10px 20px;
      span {
        color: #767676 !important;
        padding-left: 10px;
      }
      .leftVideo {
        width: 100%;
        height: 251px;
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
  .dialogForm {
    width: 100%;
    height: calc(44% - 50px);
    background: #f7f7f7;
    padding: 10px 10px 0;
    overflow-y: auto;
    overflow-x: hidden;
    margin-top:10px;
    .el-input {
      width: 100%;
      .el-input--medium .el-input__inner {
        width: 93px;
      }
    }
    .el-form-item {
      margin-bottom: 10px !important;
    }
    .evtCarStyle {
      width: calc(100% - 10px);
      height: 40px;
      padding: 10px;
      overflow-y: auto;
      padding-bottom: 0;
      border-radius: 4px;
      > div {
        display: flex;
        margin-bottom: 5px;
        .evtNum {
          width: 35px;
          height: 35px;
          // border: solid 1px #ccc;
          text-align: center;
          line-height: 35px;
        }
        div {
          margin-left: 5px;
        }
      }
    }
  }
  .dialogFooterButton {
    width: 100%;
    height: 30px;
    display: flex;
    justify-content: right;
    margin-bottom: 15px;
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
  .el-dialog__headerbtn{
    z-index:3;
  }

  ::v-deep .detailsDialog {
    width: 60%;
    position: absolute;
    left: 20%;
    .el-dialog:not{
      margin-top:0px!important;
    }
  }
  ::v-deep .detailsDialog .el-dialog {
    height: calc(100% - 8vh) !important;
    .el-dialog__body {
      height: calc(100% - 4vh - 30px);
      padding: 0 !important;
    }
  }

  .animationDialog {
    z-index: 2008 !important;
    height: 92%;
    width: 480px;
    // transform: translateX(1330px);
    animation: mymove 0.3s linear;
    position: absolute;
    left: 66%;
  }
  @keyframes mymove {
    0% {
      left: 60%;
    }
    100% {
      left: 69%;
    }
  }
  .el-select-dropdown {
    z-index: 2010 !important;
  }
  .eventTypeButton {
    height: 2.6vh;
    line-height: 2.6vh;
    border-radius: 2px;
    cursor: pointer;
    padding: 0px 10px;
    font-size: 0.7vw;
  }
  // ::v-deep .vue-treeselect__control {
  //   height: 4vh;
  // }
  // ::v-deep .vue-treeselect__placeholder,
  // .vue-treeselect__single-value {
  //   line-height: 4vh;
  // }
  // ::v-deep .el-input--small .el-input__inner {
  //   line-height: 3vh;
  //   height: 4vh;
  //   font-size: 0.7vw;
  // }
  // ::v-deep .el-input--medium .el-input__inner {
  //   line-height: 3vh;
  //   height: 4vh;
  //   font-size: 0.7vw;
  // }
  .butBox {
    width: 280px;
    display: flex;
    padding: 4px 4px;
    background: #9ecced;
    border-radius: 10px;
    margin-bottom: 10px;
    font-size: 14px;
    // justify-content: space-between;
    div {
      padding: 6px 10px;
      color: #fff;
      letter-spacing: 1px;
      cursor: pointer;
    }

    .xz {
      background: #285b8d;
      border-radius: 10px;
    }
  }
  .incHandContent {
    display: flex;
    color: #333333;
    font-size: 12px;
    padding: 10px;
    .classification {
      .type {
        width: 50px;
        height: 50px;
        // background: #f2f8ff;
        border: 1px solid #39adff;
        text-align: center;
        color:#fff;
      }
      .yijian {
        width: 50px;
        background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        border: 1px solid #39adff;
        color: #fff;
        text-align: center;
        transform: translateY(-2px);
        cursor: pointer;
      }
      .hulue {
        width: 50px;
        background: linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
        border: 1px solid #ebab3a;
        color: #fff;
        text-align: center;
        transform: translateY(-2px);
        cursor: pointer;
      }
    }

    .heng1 {
      width: 20px;
      height: 1px;
      border-top: solid 1px #39adff;
    }
    .shu {
      width: 20px;
      border-left: solid 1px #39adff;
      border-bottom: solid 1px #39adff;
      margin-top: 20px;
    }
    .contentList {
      display: block;
      margin-top: 4px;
      line-height: 40px;
      padding: 0 20px;
      // background: #f2f8ff;
      color: #fff;
      border: solid 1px #39adff;
      border-radius: 3px;
      width: 300px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      img {
        width: 18px;
        height: 18px;
      }
    }
    .contentList:nth-of-type(1) {
      margin-top: 0;
    }
  }
  .addClass {
    .el-select {
      width: 250px;
    }

    .el-input {
      width: 250px !important;
    }

    .el-date-editor.el-input,
    .el-date-editor.el-input__inner {
      width: 250px !important;
    }
  }

  .circle {
    width: 10px;
    height: 10px;
    border-radius: 5px;
    display: inline-block;
  }

  .detailsText {
    display: inline-block;
    margin-left: 20px;
    line-height: 40px;
    width: 100px;
  }

  hr {
    border: solid 1px #ddd;
  }

  .rowClass {
    border-top: solid 1px #ddd;
    border-bottom: solid 1px #ddd;
    height: 40px;
    margin-top: 10px;
  }

  .eventClass {
    height: 30px;
    border-right: solid 1px #ddd;
    width: 100%;
    text-align: center;
    margin-top: 5px;
    line-height: 30px;
  }

  .eventTitleClass {
    height: 40px;
    background-color: #eeeeee;
    line-height: 40px;
    text-align: center;
  }

  .video {
    height: 300px;
    border-radius: 0;
    padding: 10px;
    margin-top: 0;
  }

  .image3 {
    padding: 5px;
    height: 49%;
    // border: solid 1px green;
    width: 100%;
  }

  .card-box {
    width: 30%;
    text-align: center;
    font-weight: bold;
  }

  .EquipStatistics {
    width: 200px;
    height: 40px;
    background-image: url(../../assets/cloudControl/shebeiWarning.png);
    color: white;
    text-align: center;
    line-height: 40px;
    font-weight: 400;
    font-size: 16px;
    margin-left: 14px;

    > span {
      font-size: 24px;
      font-weight: 600;
      vertical-align: middle;
    }
  }

  .warningStatistics {
    line-height: 60px;
    font-size: 14px;
    // color: #606266;
    font-weight: 700;
  }

  .eventTitle {
    padding: 15px 0;
    font-size: 18px;
    font-weight: 400;
    color: #303133;
  }

  .card {
    position: relative;
    width: 100%;
    padding: 20px;
    margin-top: 20px;
    border-radius: 10px;
    background-color: #f0f0f0;
    .card-col {
      margin-top: 10px;
      display: flex;
      color: #79949c;
      div {
        width: 33%;
        span {
          color: black;
          margin-left: 10px;
        }
      }
    }
    .card-cols {
      margin-top: 10px;
      display: flex;
      div {
        width: 50%;
      }
      .col-test {
        text-align: right;
        color: #79949c;
      }
      img {
        width: 100px;
        margin-left: 20px;
      }
    }

    .icon {
      position: absolute;
      top: 0;
      right: 30px;
      background-image: url(../../assets/icons/svg/u954.svg);
      background-size: 100%;
    }
  }

  .disabledButton {
    cursor: no-drop;
    pointer-events: none;
  }
  ::-webkit-scrollbar {
    width: 6px;
  }
  // .videoDialog {
  //   height: 92%;
  // }
  .videoDialogClass {
    width: 100%;
    height: 100%;

    video {
      width: 100%;
      height: auto;
    }
  }
  .el-carousel {
    height: 100%;
  }
  ::v-deep .el-carousel__indicators {
    display: none;
  }
  .topTxt {
    margin-left: 7px;
    margin-top: 10px;
    font-size: 16px;
    background-image: url(../../assets/cloudControl/cardTitle.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
    text-align: center;
    width:139px;
    height: 30px;
    line-height: 30px;
  }
  .searchSafeWarn{
    top: 6% !important;
    right: 0.8% !important;
    width: 453px !important;
    .el-checkbox+.el-checkbox{
      margin-left: 0 !important;
    }
  }
  .hitchDialog{
    ::v-deep .el-dialog__body{
      height:70vh !important;
      overflow:auto !important;
    }
    ::v-deep .el-card{
      margin-bottom: 10px !important;
    }
  }
  .evtInfo{
    ::v-deep .el-dialog__body{
      max-height: 70vh;
      overflow: auto;
    }
  }
  .detailsDialog{
    ::v-deep .el-dialog__body{
      max-height: 86vh;
      overflow: auto;
    }
  }
</style>
