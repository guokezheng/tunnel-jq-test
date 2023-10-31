<script src="../../../../vue.config.js"></script>
<template>
  <div class="app-container safeWarnStyle">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="tabTopFormRow">
      <el-col :span="6">
        <el-button size="small" @click="resetQuery()">刷新 </el-button>
      </el-col>
      <el-col :span="6" :offset="12">
        <div class="grid-content bg-purple" ref="main1">
          <el-input
            @keyup.enter.native="handleQuery"
            size="small"
            placeholder="请输入事件位置、来源，回车搜索"
            v-model="fuzzySearch1"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="zd_boxShow = !zd_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <!-- 右侧弹窗 -->
    <div class="searchBox searchSafeWarn" v-show="zd_boxShow" ref="cc1">
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        label-width="68px"
        class="formStyle"
      >
        <el-form-item label="事件状态">
          <el-checkbox-group v-model="checkBoxEventState">
            <el-checkbox
              v-for="item in eventStateOptions"
              :key="item.dictValue"
              :label="item.dictValue"
              @change.native="changeCheckBox($event)"
            >
              {{ item.dictLabel }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="防控类型" prop="prevControlType">
          <el-select
            v-model="queryParams.prevControlType"
            placeholder="请选择防控类型"
            clearable
            size="small"
            style="width: 325px"
            @change="handleChangeControl"
          >
            <el-option
              v-for="item in controlTypeOptions"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventTypeId">
          <el-select
            v-model="queryParams.eventTypeId"
            placeholder="请选择事件类型"
            clearable
            size="small"
            style="width: 325px"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="(item, index) in eventTypeDataList"
              :key="index"
              :label="item.simplifyName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="所属隧道"
          prop="tunnelId"
          v-show="manageStation == '0'"
        >
          <el-select
            v-model="queryParams.tunnelId"
            placeholder="请选择所属隧道"
            clearable
            size="small"
            style="width: 325px"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in tunnelList"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="隧道方向" prop="direction">
          <el-select
            v-model="queryParams.direction"
            placeholder="请选择隧道方向"
            clearable
            size="small"
            style="width: 325px"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="item in directionList"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="事件时间" prop="eventTime">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 325px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            unlink-panels
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptionsStart"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" @click="handleQuery">搜索</el-button>
          <el-button size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr"></div>
    <div class="contentListBox container">
      <div
        class="contentBox"
        :style="topNav ? 'width:24.6%' : 'width:24.5%'"
        v-for="(item, index) in eventList"
        :key="index"
      >
        <div class="top" style="">
          <div class="video">
            <div class="videoBox">
              <img
                :src="item.eventImgUrl"
                v-show="item.eventImgUrl"
                style="width: 100%"
                @click="openPicDialog(item)"
              />
              <img
                src="../../../assets/cloudControl/nullImg.png"
                v-show="!item.eventImgUrl || item.eventImgUrl == null"
              />
            </div>
          </div>
          <div class="contentText">
            <div>
              来源 <span>{{ getFrom(item.eventSource) }}</span>
            </div>
            <div style="width: 100%; overflow: hidden; white-space: nowrap">
              位置 <span>{{ item.position }}</span>
            </div>
            <div v-show="item.eventState == '3'">
              时间 <span>{{ item.eventTime }}</span>
            </div>
            <div
              v-show="
                item.eventState == '2' ||
                item.eventState == '4' ||
                item.eventState == '5' ||
                item.eventState == '1'
              "
            >
              时间 <span>{{ item.endTime }}</span>
            </div>
            <div v-show="item.eventState == '0'">
              时间 <span>{{ item.updateTime }}</span>
            </div>

            <div class="stateTab">
              <img :src="safeWarn0" v-show="item.eventState == '0'" />
              <img :src="safeWarn1" v-show="item.eventState == '1'" />
              <img :src="safeWarn2" v-show="item.eventState == '2'" />
              <img :src="safeWarn3" v-show="item.eventState == '3'" />
              <img :src="safeWarn4" v-show="item.eventState == '4'" />
              <img :src="safeWarn5" v-show="item.eventState == '5'" />
            </div>
          </div>
        </div>
        <div class="bottom">
          <div class="eventBox">
            <div class="eventType">
              {{ item.prevControlType == "1" ? "安全预警" : "普通事件" }}
            </div>

            <div>{{ item.simplifyName }}</div>
          </div>
          <div class="contentButton">
            <div @click="detailsOpen(item)">详情</div>
            <div
              v-once
              v-if="item.eventState == '3'"
              @click="detailsButton(item)"
            >
              复核
            </div>
            <div
              v-if="item.eventState == '0'"
              class="chuzhi"
              @click="management(item.id)"
              v-hasPermi="['system:event:disposeOf']"
            >
              处置
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
      v-if="total > 0"
      class="specialPagination"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryParams.pageNum"
      :page-sizes="[16, 32, 48, 64]"
      :page-size="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 查看详情弹窗 -->
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
          <div style="padding: 6px 0">事发时抓图或录像</div>
          <!-- <video :src="eventForm.videoUrl" controls muted loop fluid></video> -->
          <div class="picBox">
            <swiper
              class="swiper gallery-top"
              :options="swiperOptionTop"
              ref="swiperTop"
              v-show="eventFormDetail.iconUrlList.length >= 1"
            >
              <!-- slides -->
              <swiper-slide
                v-for="(item, index) in eventFormDetail.iconUrlList"
                :key="index"
                :class="'slide-' + index"
              >
                <video
                  :src="item.imgUrl"
                  :poster="item.imgUrl"
                  v-if="index == 0"
                  @click="openPicDialog(eventFormDetail)"
                  class="leftVideo"
                  autoplay
                  muted
                  loop
                ></video>
                <img
                  :src="item.imgUrl"
                  style="width: 100%; height: 100%"
                  v-if="index != 0"
                  @click="clickImg(item.imgUrl)"
                />
              </swiper-slide>
              <!-- <div class="swiper-scrollbar"   slot="scrollbar"></div> -->
            </swiper>
            <swiper
              class="swiper gallery-thumbs"
              :options="swiperOptionThumbs"
              ref="swiperThumbs"
              v-show="eventFormDetail.iconUrlList.length >= 1"
            >
              <swiper-slide
                v-for="(item, index) in eventFormDetail.iconUrlList"
                :key="index"
                :class="'slide-' + index"
              >
                <video
                  :src="item.imgUrl"
                  :poster="item.imgUrl"
                  v-if="index == 0"
                  autoplay
                  muted
                  loop
                ></video>
                <img
                  :src="item.imgUrl"
                  style="width: 100%; height: 100%"
                  v-if="index != 0"
                />
              </swiper-slide>
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
            <div
              v-show="eventFormDetail.iconUrlList.length < 1"
              style="width: 100%; height: 300px"
            >
              <el-image
                style="width: 100%; height: 100%"
                :src="noPic"
                :fit="contain"
              >
              </el-image>
            </div>
          </div>
        </div>
        <div class="dialogBg dialogBg2">
          <div style="padding: 6px 0">
            实时视频<span>(事发位置最近的监控视频)</span>
          </div>
          <div class="picBox">
            <el-carousel
              trigger="click"
              :autoplay="false"
              v-if="videoList.length >= 1"
            >
              <el-carousel-item v-for="(item, index) in videoList" :key="index">
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
              :fit="contain"
            >
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
        <el-form
          ref="eventFormDetail"
          :model="eventFormDetail"
          label-width="80px"
          :rules="rules"
        >
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
              <el-form-item label="持续时长" prop="endTime">
                <!-- <el-date-picker
                  @change="changeEndTime"
                  clearable
                  size="small"
                  v-model="eventFormDetail.endTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择预计解除时间"
                  style="width: calc(100% - 10px)"
                >
                </el-date-picker> -->
                <el-input
                  v-model="
                    !!eventFormDetail.continuedTime
                      ? eventFormDetail.continuedTime.split('-').join('')
                      : eventFormDetail.continuedTime
                  "
                  readonly
                  style="width: calc(100% - 10px)"
                ></el-input>
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
              <el-row>
                <el-col :span="15">
                  <el-form-item label="事件起点" prop="stakeNum1">
                    <el-row class="inputNumStyle">
                      <el-col :span="22" style="margin-right: 2px">
                        <el-input
                          v-model="eventFormDetail.stakeNum1"
                          placeholder="Km"
                          oninput="value=value.replace(/[^\d]/g,'')"
                          width="100%"
                        >
                          <template slot="prepend">K</template>
                        </el-input>
                      </el-col>
                      <el-col :span="1"> +</el-col>
                    </el-row>
                  </el-form-item>
                </el-col>
                <el-col :span="9">
                  <el-form-item prop="stakeNum2" label-width="0px">
                    <el-row>
                      <el-col :span="22">
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
              </el-row>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件终点">
                <el-row class="inputNumStyle">
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
              <el-form-item label="影响方向" prop="direction">
                <el-select
                  v-model="eventFormDetail.direction"
                  placeholder="方向"
                  clearable
                  size="small"
                  style="width: calc(100% - 10px)"
                  @change="getReservePlanData"
                >
                  <el-option
                    v-for="item in directionList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="影响车道" prop="laneNo">
                <el-select
                  v-model="eventFormDetail.laneNo"
                  placeholder="车道"
                  clearable
                  size="small"
                  multiple
                  collapse-tags
                  style="width: calc(100% - 10px)"
                >
                  <el-option
                    v-for="(item, index) in chezhiLaneList"
                    :key="index"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件车辆" prop="confidenceList">
                <el-input
                  v-model="eventFormDetail.confidenceList"
                  placeholder=""
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
                  style="width: 100%"
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
                <el-radio-group
                  v-model="eventFormDetail.eventState"
                  @input="eventStateChange"
                >
                  <el-radio :label="4"> 确认(已处理) </el-radio>
                  <el-radio :label="2"> 挂起(稍后处理) </el-radio>
                  <el-radio :label="5"> 误报 </el-radio>
                  <el-radio :label="0"> 突发事件处置 </el-radio>
                </el-radio-group>
                <span style="color: #c59105">(请根据复核判定结果选择)</span>
              </el-form-item>
            </el-col>
            <div style="width: 100%">
              <el-col :span="24" v-show="eventFormDetail.eventState == 4">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group
                    v-model="eventFormDetail.reviewRemark"
                    class="checkBox"
                  >
                    <el-checkbox-button
                      label="已线下处理"
                      value="已线下处理"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="车辆已驶离"
                      value="车辆已驶离"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="施工车辆"
                      value="施工车辆"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="正常施工作业"
                      value="正常施工作业"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="其他"
                      value="其他"
                    ></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventFormDetail.eventState == 2">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group
                    v-model="eventFormDetail.reviewRemark"
                    class="checkBox"
                  >
                    <el-checkbox-button
                      label="稍后处理"
                      value="稍后处理"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="持续跟踪，事态发展情况"
                      value="持续跟踪，事态发展情况"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="已通知高警现场处理"
                      value="已通知高警现场处理"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="已通知路政现场处理"
                      value="已通知路政现场处理"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="其他"
                      value="其他"
                    ></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="24" v-show="eventFormDetail.eventState == 5">
                <el-form-item prop="reviewRemark">
                  <el-checkbox-group
                    v-model="eventFormDetail.reviewRemark"
                    class="checkBox"
                  >
                    <el-checkbox-button
                      label="系统误报"
                      value="系统误报"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="误报或涉事车辆已驶离"
                      value="误报或涉事车辆已驶离"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="无法复核事发情况"
                      value="无法复核事发情况"
                    ></el-checkbox-button>
                    <el-checkbox-button
                      label="其他"
                      value="其他"
                    ></el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col
                :span="24"
                v-if="
                  eventFormDetail.eventState == 0 &&
                  eventFormDetail.prevControlType == 0
                "
              >
                <el-form-item
                  prop="currencyId"
                  :rules="
                    eventFormDetail.eventState == 0 &&
                    eventFormDetail.prevControlType == 0
                      ? rules.currencyId
                      : []
                  "
                >
                  <el-select
                    v-model="eventFormDetail.currencyId"
                    placeholder="请选择预案"
                    style="width: 30%"
                  >
                    <el-option
                      v-for="item in ReservePlanList"
                      :key="item.id"
                      :label="item.planName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <el-button
                    size="small"
                    v-show="eventFormDetail.currencyId"
                    @click="openDoor(eventFormDetail)"
                    >查看</el-button
                  >
                  <span style="color: #c59105"
                    >(事件处置预案根据事件类型、事件等级智能推荐,处置过程中允许升级及更改预案)</span
                  >
                </el-form-item>
              </el-col>
              <el-col
                :span="24"
                v-if="
                  eventFormDetail.eventState == 0 &&
                  eventFormDetail.prevControlType == 1
                "
              >
                <el-form-item
                  prop="currencyId"
                  :rules="
                    eventFormDetail.eventState == 0 &&
                    eventFormDetail.prevControlType == 1
                      ? rules.currencyId
                      : []
                  "
                >
                  <el-select
                    v-model="eventFormDetail.currencyId"
                    placeholder="请选择预案"
                  >
                    <el-option
                      v-for="item in ReservePlanList"
                      :key="item.id"
                      :label="item.planName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <el-button
                    size="small"
                    type="primary"
                    v-show="eventFormDetail.currencyId"
                    @click="openDoor(eventFormDetail)"
                    >查看</el-button
                  >
                  <span style="color: #c59105"
                    >(事件处置预案根据事件类型、事件等级智能推荐,处置过程中允许升级及更改预案)</span
                  >
                </el-form-item>
              </el-col>
              <el-col
                v-show="
                  eventIsShow(
                    eventFormDetail.reviewRemark,
                    eventFormDetail.eventState
                  )
                "
              >
                <el-form-item prop="otherContent">
                  <el-input
                    placeholder="请输入其他原因内容"
                    v-model="eventFormDetail.otherContent"
                  ></el-input>
                </el-form-item>
              </el-col>
            </div>
          </el-row>
        </el-form>
        <div class="dialogFooterButton">
          <div @click="submitDialog" v-hasPermi="['system:event:check']">复核提交</div>
          <!-- <div
            @click="openProcess(1, eventFormDetail.id)"
          >
            处置
          </div> -->
        </div>
      </div>
    </el-dialog>
    <el-dialog
      title="事件详情报告"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false"
      :destroy-on-close="true"
      width="70%"
      class="evtInfo"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-timeline>
        <el-timeline-item timestamp="事件发现" placement="top">
          <el-card>
            <el-form
              ref="eventDiscovery"
              :model="eventDiscovery"
              label-width="100px"
            >
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="告警来源">
                    <el-input
                      v-model="eventDiscovery.eventSource"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="告警时间">
                    <el-input
                      v-model="eventDiscovery.eventTime"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="持续时长">
                    <el-input
                      v-model="eventDiscovery.continuedTime"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事发路段">
                    <el-input
                      v-model="eventDiscovery.tunnelName"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事发位置">
                    <el-input
                      v-model="eventDiscovery.stakeNum"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="所属方向">
                    <el-input
                      v-model="eventDiscovery.direction"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="抓图录像" prop="iconUrlList">
                    <!-- class="detailImg" -->
                    <el-scrollbar wrap-class="scrollbar-wrapper">
                      <div
                        class="scrollbar_li"
                        v-for="(item, index) in eventDiscovery.iconUrlList"
                        :key="index"
                      >
                        <video
                          v-if="item.imgType == '1'"
                          :src="item.imgUrl"
                          muted
                          loop
                          fluid
                          autoplay
                          @click="openPicDialog(item)"
                        ></video>
                        <el-image
                          v-if="item.imgType == '0'"
                          :src="item.imgUrl"
                          @click="clickImg(item.imgUrl)"
                        >
                        </el-image>
                      </div>
                    </el-scrollbar>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="历史时间">
                    <el-date-picker
                      v-model="eventDiscovery.startTime"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="请选择历史时间">
                    </el-date-picker>
                    <el-button size="small" @click="viewVedio" style="margin-left: 7px;">查看</el-button>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="下载时间">
                    <el-date-picker
                      v-model="eventDiscovery.downLoadTime"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      placeholder="请选择下载时间">
                    </el-date-picker>
                    <el-button size="small" @click="downloadVedio" style="margin-left: 7px;">下载</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item label="历史录像" prop="historyUrlList">
                    <el-scrollbar wrap-class="scrollbar-wrapper">
                      <div
                        class="scrollbar_li"
                        v-for="(item, index) in eventDiscovery.historyUrlList"
                        :key="index"
                      >
                        <videoPlayer
                          v-if="item.imgType == '2'"
                          :rtsp="item.imgUrl"
                          :open="vedioPlayer"
                        ></videoPlayer>
                      </div>
                    </el-scrollbar>
                  </el-form-item>
                </el-col>
              </el-row> -->
            </el-form>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          timestamp="人工复核"
          placement="top"
          v-if="eventStateCurrent != '3'"
        >
          <el-form ref="manualReview" :model="manualReview" label-width="100px">
            <el-card>
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item label="当事目标">
                    <el-input
                      v-model="manualReview.confidenceList"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="影响描述">
                    <el-input
                      v-model="manualReview.eventDescription"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="预估类型">
                    <el-input
                      v-model="manualReview.eventTypeName"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="预估等级">
                    <el-input
                      v-model="manualReview.eventGrade"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="复核结果">
                    <el-input
                      v-model="manualReview.eventState"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事发位置">
                    <el-input
                      v-model="manualReview.stakeNum"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="影响方向">
                    <el-input
                      v-model="manualReview.direction"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="影响车道">
                    <el-input v-model="manualReview.laneNo" readonly></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事发路段">
                    <el-input
                      v-model="manualReview.tunnelName"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="8">
                  <el-form-item label="复核人">
                    <el-input
                      v-model="manualReview.updateBy"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="复核时间">
                    <el-input
                      v-model="manualReview.updateTime"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="复核描述">
                    <el-input
                      v-model="manualReview.reviewRemark"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>
          </el-form>
        </el-timeline-item>
        <el-timeline-item
          timestamp="事件处置"
          placement="top"
          v-if="eventStateCurrent == '1' || eventStateCurrent == '0'"
        >
          <el-card>
            <el-col :span="12" v-if="isAuto == 0">
              <div class="IncHand">
                <div class="incHandBox">
                  <el-tabs v-model="historyIndex" @tab-click="handleClick">
                    <el-tab-pane
                      v-for="(item, index) of planDisposal"
                      :label="item.planName"
                      :name="'first' + index"
                      :key="index"
                    >
                      <!-- {{ item.planName }} -->
                    </el-tab-pane>
                  </el-tabs>
                  <div
                    v-for="(item, index) of planDisposal"
                    :key="index"
                    v-if="historyIndex == 'first' + index"
                  >
                    <div
                      v-for="(items, indexs) of item.planList"
                      :key="indexs"
                      class="incHandContent"
                    >
                      <div class="classification">
                        <el-tooltip
                          v-if="items.flowContent"
                          class="item"
                          effect="dark"
                          :content="items.flowContent"
                          placement="right"
                        >
                          <div
                            class="type"
                            :style="{
                              padding: items.flowContent
                                ? items.flowContent.toString().length > 2
                                  ? '8px'
                                  : '15px 5px'
                                : '',
                              marginTop: items.children
                                ? items.flowContent == '设备联控'
                                  ? (items.children.length * 40 +
                                      4 * (items.children.length - 1)) /
                                      2 -
                                    35 +
                                    'px'
                                  : (items.children.length * 40 +
                                      4 * (items.children.length - 1)) /
                                      2 -
                                    25 +
                                    'px'
                                : '',
                            }"
                            v-if="items.flowContent"
                          >
                            {{ items.flowContent }}
                          </div>
                        </el-tooltip>
                        <!-- <div v-show="item.flowId == 7" class="yijian" @click="getYiJian(item)"
                        :style="iconDisabled?'cursor: not-allowed;pointer-events: none;background:#ccc;border:solid 1px #ccc':'cursor: pointer'">一键</div> -->
                      </div>

                      <div
                        class="heng1"
                        v-if="items.children"
                        :style="{
                          marginTop: items.children
                            ? items.children.length == 1
                              ? '20px'
                              : (items.children.length * 40 +
                                  4 * (items.children.length - 1)) /
                                  2 +
                                'px'
                            : '',
                        }"
                      ></div>
                      <div
                        class="shu"
                        v-if="items.children"
                        :style="{
                          height: items.children
                            ? items.children.length > 1
                              ? items.children.length * 44 +
                                4 * items.children.length -
                                40 +
                                'px'
                              : '0px'
                            : '',
                          borderTop:
                            items.children && items.children.length > 1
                              ? 'solid 1px #39adff'
                              : '',
                        }"
                      ></div>
                      <div class="gxp">
                        <div
                          v-for="(itm, inx) of items.children"
                          :key="inx"
                          class="contentList"
                        >
                          <h4 style="float: left; width: 80%">
                            {{ itm.flowContent }}
                          </h4>
                          <div class="yzx" v-show="itm.eventState != '0'">
                            已执行
                          </div>
                          <div
                            class="wzx"
                            v-show="itm.eventState == '0'"
                            type="info"
                          >
                            未执行
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="12" v-if="isAuto == 1">
              <el-timeline
                :reverse="reverse"
                style="
                  overflow-y: auto;
                  height: 420px;
                  padding: 20px 10px 20px 25px;
                "
              >
                <el-table
                  stripe
                  class="phoneTable"
                  :data="tacticsList"
                  :fit="true"
                  height="250"
                  style="width: 100%"
                >
                  <el-table-column
                    prop="eqName"
                    label="设备名称"
                    align="center"
                  >
                  </el-table-column>
                  <el-table-column prop="pile" label="桩号" align="center">
                  </el-table-column>
                  <el-table-column prop="stateName" label="状态" align="center">
                  </el-table-column>
                </el-table>
              </el-timeline>
            </el-col>
            <el-col :span="12" style="padding-bottom: 10px">
              <el-timeline :reverse="reverse" class="disposalRecordBox">
                <el-timeline-item
                  v-for="(activity, index) in disposalRecord"
                  :key="index"
                  :timestamp="activity.flowTime"
                  style="color: #fff"
                  placement="top"
                >
                  <el-card>
                    <h4 class="flow-description">{{ activity.flowDescription }}</h4>
                    <p>用户:{{ activity.nickName }}</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-col>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          timestamp="完结报告"
          placement="top"
          v-if="eventStateCurrent == '1'"
        >
          <el-form ref="endReport" :model="endReport" label-width="100px">
            <el-card>
              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item label="完结时间">
                    <el-input v-model="endReport.endTime" readonly></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="确认人">
                    <el-input v-model="endReport.updateBy" readonly></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="累计耗时">
                    <el-input
                      v-model="endReport.continuedTime"
                      readonly
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="完结原因">
                    <el-input v-model="endReport.remark" readonly></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>
          </el-form>
        </el-timeline-item>
        <el-divider></el-divider>
        <el-button type="success" @click="downFile()">下载报告</el-button>
      </el-timeline>
    </el-dialog>
    <!-- 视频展示 -->
    <el-dialog
      :visible.sync="picUrlDialog"
      :close-on-click-modal="false"
      width="70%"
      title="事件视频"
      class="videoDialog"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="videoDialogClass">
        <video :src="videoUrl" controls muted loop fluid autoplay></video>
      </div>
    </el-dialog>
    <!-- 图片展示 -->
    <el-dialog
      title="抓图详情"
      :visible.sync="dialogVisibleImg"
      width="60%"
      :before-close="handleCloseImg"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img :src="alongImgUrl" style="width: 100%" />
      <!-- <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleImg = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisibleImg = false"
          >确 定</el-button
        >
      </span> -->
    </el-dialog>
    <!-- 复核详情展示 -->
    <el-dialog
      title="设备详情"
      :visible.sync="dialogVisibleDevice"
      width="50%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClickDevice">
          <el-tab-pane
            v-for="(item, index) in DeviceDetail"
            :key="index"
            :label="item.tableName"
            :name="item.tableName"
          ></el-tab-pane>
        </el-tabs>
      </div>
      <div v-for="(item, index) in DeviceDetail" :key="index">
        <el-table
          :data="item.devicesList"
          style="width: 100%"
          height="600"
          v-if="deviceIndexShow == index"
          :key="deviceIndexShow"
        >
          <el-table-column prop="eqName" label="设备名称" align="center">
          </el-table-column>
          <el-table-column prop="pile" label="桩号" align="center">
          </el-table-column>
          <el-table-column prop="stateName" label="修改后状态" align="center">
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="dialogVisibleDevice = false"
          >确 定</el-button
        >
        <el-button @click="closeDetail()" class="closeButton">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import $ from "jquery";
import { mapState } from "vuex";
import bus from "@/utils/bus";
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { Loading } from "element-ui";
import {
  listEvent,
  getEvent,
  delEvent,
  addEvent,
  updateEvent,
  toll,
  getTunnelList,
  getTunnelLane,
  getHandle,
  implementProcess,
  implementPlan,
  updateHandle,
  eventFlowList,
  getSafetyHandle,
  implementDisposalStrategy,
  implementDisposalStrategyRl,
  getReservePlanData,
  detailExport,
  closeVedio,
  getVedioData,
  downloadVedio,
} from "@/api/event/event";
import {
  addList,
  delList,
  exportFaultList,
  getEquipmentInfo,
  getList,
  getRepairRecordList,
  listList,
  updateList,
} from "@/api/electromechanicalPatrol/faultManage/fault";
import {
  listEventType,
  getTodayEventCount,
  getEventDetail,
  handleStrategy,
} from "@/api/event/eventType";
import { examineDeviceDetail, getStrategyData } from "@/api/event/reservePlan";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { image, video, getEventCamera } from "@/api/eventDialog/api.js";
import { listEventFlow, getListBySId } from "@/api/event/eventFlow";
import { treeselect, treeselectExcYG1 } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listType, loadPicture } from "@/api/equipment/type/api";
import {
  exportList,
  listBz,
} from "@/api/electromechanicalPatrol/taskManage/task";
import {
  listDevices,
  videoStreaming,
  getDeviceById,
} from "@/api/equipment/eqlist/api";
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";

export default {
  name: "Event",
  dicts: [
    "sd_direction",
    "fault_type",
    "fault_level",
    "fault_remove_statue",
    "sd_monitor_state",
    "fault_status",
    "impression",
    "network",
    "power",
  ],
  components: {
    Treeselect,
    videoPlayer,
  },

  data() {
    return {
      evtWebsoktList: [],
      strategyList: [], //策略列表
      controlTypeOptions: [], //防控类型
      pickerOptionsStart: {
        // 时间不能大于当前时间
        disabledDate: (time) => {
          return time.getTime() > Date.now();
        },
      },
      deviceIndexShow: 0,
      activeName: "",
      dialogVisibleDevice: false,
      DeviceDetail: [], //复核弹窗详情
      tableData: [],
      // 区分事件类型
      prevControlType: "",
      historyIndex: "first0",
      alongImgUrl: "",
      dialogVisibleImg: false, //图片和视频容器
      videoShow: false,
      checkBoxEventState: [],
      exportLoading: false,
      contain: "contain",
      fault_boxShow: false,
      zd_boxShow: false,
      boxShow: false,
      swiperOptionTop: {
        loop: true,
        loopedSlides: 5, // looped slides should be the same
        spaceBetween: 10,
      },
      swiperOptionThumbs: {
        loop: false,
        loopedSlides: 5, // looped slides should be the same
        spaceBetween: 10,
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
        centeredSlides: true,
        slidesPerView: "auto",
        touchRatio: 0.2,
        slideToClickedSlide: true,
      },
      reverse: true,
      iconDisabled: false,
      endReport: {}, //完结报告
      disposalRecord: [], //记录
      planDisposal: [], //历史预案
      manualReview: {}, //人工复核
      eventDiscovery: {}, //发现数据
      vedioData: {}, //视频录像数据
      tacticsList: {}, //表单数据
      isAuto: "0", //是否自动执行
      dialogTableVisible: false,
      radioList: [
        { label: "确认(已确认)", value: "4" },
        { label: "挂起(稍后处理)", value: "2" },
        { label: "误报", value: "5" },
        { label: "突发事件处置", value: "0" },
      ],
      ReservePlanList: null,
      planTypeData: [],
      eventGradeList: "", //事件等级
      picUrlDialog: false,
      eventWarnList: [],
      miniDialog: true,
      eventTypeId: "",
      evtId: "",
      noPic: require("@/assets/image/noPic.png"),
      noDataUrl: require("@/assets/image/noVideo.png"),
      incHand1: require("@/assets/cloudControl/incHand1.png"),
      incHand2: require("@/assets/cloudControl/incHand2.png"),
      incHandList: [],
      setDisabled: {
        disabledDate(time) {
          return time.getTime() > Date.now(); // 可选历史天、可选当前天、不可选未来天
        },
      },
      directionList: [],
      direction: "",
      fromList: [],
      closeProcessDialog: false,
      processDialog: false,
      processType: false,
      evtCar: [
        {
          carNum: "鲁A88888",
          speed: "100km/h",
          percentage: "80%",
        },
        {
          carNum: "鲁A88888",
          speed: "100km/h",
          percentage: "80%",
        },
      ],
      detailsDisabled: false,
      laiyuanList: [
        {
          label: "雷视融合",
          value: 1,
        },
      ],
      videoUrl: "",
      // 处理中:0
      // 已处理:1
      // 已挂起:2
      // 待确认:3
      // 已确认:4
      // 误报:5
      safeWarn0: require("@/assets/cloudControl/safeWarn0.png"),
      safeWarn1: require("@/assets/cloudControl/safeWarn1.png"),
      safeWarn2: require("@/assets/cloudControl/safeWarn2.png"),
      safeWarn3: require("@/assets/cloudControl/safeWarn3.png"),
      safeWarn4: require("@/assets/cloudControl/safeWarn4.png"),
      safeWarn5: require("@/assets/cloudControl/safeWarn5.png"),
      // 一键车道指示器 车道下拉框
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
        // {
        //   laneId: "1",
        //   laneName: "一车道",
        // },
        // {
        //   laneId: "2",
        //   laneName: "二车道",
        // },
        // {
        //   laneId: "3",
        //   laneName: "三车道",
        // },
      ],

      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      sClick: true,
      manageStation: this.$cache.local.get("manageStation"),
      manageStationSelect: this.$cache.local.get("manageStationSelect"),
      //检修记录弹出窗
      record: false,
      dialogEventList: [],
      eventMsg: {
        allnum: 0,
        process: 0,
        bl: 0,
      },
      removeStata: false,
      // 管理机构
      mechanism: [],
      // 所属隧道
      tunnelList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      fileList: [],
      removeIds: [],
      // 遮罩层
      loading: true,
      //巡查班组
      fileData: "", // 文件上传数据（多文件合一）
      bzData: {},
      // 弹出层是否可写
      isWritable: true,
      // 是否不可点击
      disstate: false,
      // 显示搜索条件
      showSearch: true,
      //事件类型
      eventTypeData: [],
      // 全部事件类型
      eventTypeDataList: [],
      // 总条数
      total: 0,
      // 事件管理表格数据
      eventList: [],
      //
      searchValue: "1",
      // 弹出层标题
      title: "",
      // 状态字典
      eventStateOptions: [],
      // 级别 字典
      eventGradeOptions: [],
      // 事件查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 16,
        tunnelId: null,
        eventTypeId: null,
        prevControlType: null,
        eventTitle: null,
        eventTime: null,
        eventState: [],
        eventGrade: null,
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
        startTime: null,
        endTime: null,
        deptId: null,
      },
      allmsg: "",
      process: "",
      proportion: "",
      // 表单参数
      form: {},
      //所属隧道
      eqTunnelData: {},
      //设备类型
      eqTypeData: {},
      //设备
      eqListData: {},
      // 查询参数
      news: {
        xcTime: "",
        bzId: "",
        walkerId: "",
        impression: "",
        network: "",
        power: "",
        eqStatus: "",
        runStatus: "",
        eqFaultDescription: "",
      },
      pics: {
        imgUrl: "",
        imgName: "",
      },
      // 日期范围
      dateRange: [],
      open: false,
      details: false,
      submitEventFormLoading: false,
      direction: "rtl",
      eventForm: {
        stakeNum1: "",
        stakeNum2: "",
        stakeEndNum1: "",
        stakeEndNum2: "",
        iconUrlList: [],
        reviewRemark: [],
        eventState: "1",
      },
      // 详情弹窗内
      eventFormDetail: {
        stakeNum1: "",
        stakeNum2: "",
        stakeEndNum1: "",
        stakeEndNum2: "",
        iconUrlList: [],
        reviewRemark: [],
        eventState: "4",
      },
      iconUrlListAll: [],
      imgUrlList: [],
      urls: [],
      urlsList: [],
      urlsAll: [],
      // 翻页
      arrowRight: false,
      arrowLeft: false,
      arrowRight2: false,
      arrowLeft2: false,
      imgPage: 1,
      imgPage2: 1,
      eventStateCurrent: "",
      // 遮罩层
      dloading: false,
      // 部门树选项
      deptOptions: [],
      // 实时视频
      videoForm: {
        liveUrl: "",
      },
      videoList: [],
      cameraVisible: true,
      isState: false,
      showElement: true,
      showFaultElement: false,
      fuzzySearch1: "",
      // 表单校验
      rules: {
        otherContent: [
          { max: 100, message: "最长输入100个字符", trigger: "change" },
        ],
        // eventTime:[
        // {
        //     required: true,
        //     message: "请选择告警时间",
        //     trigger: "change",
        //   },
        // ],
        // tunnelName:[
        // {
        //     required: true,
        //     message: "请选择所属隧道",
        //     trigger: "change",
        //   },
        // ],
        stakeNum1: [
          {
            required: true,
            message: "请输入事件起点",
            trigger: "change",
          },
        ],
        stakeNum2: [
          {
            required: true,
            message: "请输入事件起点",
            trigger: "change",
          },
        ],
        direction: [
          {
            required: true,
            message: "请选择影响车道",
            trigger: "change",
          },
        ],
        eventTypeId: [
          {
            required: true,
            message: "请选择预估类型",
            trigger: "change",
          },
        ],
        eventGrade: [
          {
            required: true,
            message: "请选择预估等级",
            trigger: "change",
          },
        ],
        currencyId: [
          {
            required: true,
            message: "请选择预案",
            trigger: "change",
          },
        ],
        confidenceList: [
          {
            required: false,
            validator: this.confidenceListRules,
            trigger: "blur",
          },
        ],
      },
      // isLoading: false,
      loadingText: "加载中...",
      vedioPlayer: false,
    };
  },
  computed: {
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
    }),
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      this.manageStationSelect = newVal;
      this.queryParams.tunnelId = newVal;
      this.queryParams.eventTypeId = "";
      this.queryParams.prevControlType = "";
      this.getList();
      this.getTunnelLane();
    },
    sdEventList(event) {
      console.log(event, "websockt推送一件事");
      this.evtWebsoktList = event;
    },
  },
  mounted() {},
  async created() {
    this.eventList = [];
    this.eventLists = [];
    this.getTreeselect();
    this.getBz();
    await this.getList();
    await this.getEventMsg();
    this.getEventTypeAll();
    this.getTunnel();
    this.getEqType();
    this.getDevices();
    // 事件来源
    this.getDicts("sd_event_source").then((data) => {
      this.fromList = data.data;
    });
    this.fileData = new FormData(); // new formData对象
    this.getDicts("sd_event_state").then((response) => {
      console.log(response.data, "事件状态");
      this.eventStateOptions = response.data;
    });
    this.getDicts("sd_incident_level").then((response) => {
      this.eventGradeOptions = response.data;
    });
    this.getDicts("sd_direction").then((response) => {
      this.directionList = response.data;
    });
    // 二车道
    this.getDicts("sd_lane_one").then((data) => {
      this.chezhiLaneList2 = data.data;
    });
    // 三车道
    this.getDicts("sd_lane_two").then((data) => {
      this.chezhiLaneList3 = data.data;
    });
    // 事件类型
    // this.getPlanType();
    // 事件等级
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeList = response.data;
    });

    //防控类型
    this.getDicts("prev_control_type").then((response) => {
      if (response.data.length > 0) {
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].dictLabel == "设备故障") {
            response.data.splice(i, 1);
          }
        }
      }
      this.controlTypeOptions = response.data;
    });
    // 管理机构
    toll().then((res) => {
      this.mechanism = res.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus1);
  },
  methods: {
    confidenceListRules(rule, value, callback) {
      console.log(value, "value");
      if (value) {
        const plateNumber =
          /^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$/;
        if (plateNumber.test(value)) {
          callback();
        } else {
          callback(new Error("请输入正确的车牌号，字母大写"));
        }
      }else{
        callback();
      }
    },
    //下载录像
    // downloadVedio(){
    //   console.log(this.eventDiscovery,"22223333")
    //   if(this.eventDiscovery.downLoadTime == null){
    //     this.$modal.msgError("请选择下载时间！");
    //   }else {
    //     if(this.vedioData.camID == undefined || this.vedioData.camID == undefined == null || this.vedioData.camID == ''){
    //       this.$modal.msgError("请先查看视频后下载！");
    //     }
    //     downloadVedio(this.vedioData.camID,this.eventDiscovery.downLoadTime).then((res) => {
    //       console.log(res,"下载下载下载");
    //       window.open(res.data.fileUrl)
    //     })
    //   }

    // },

    //查看录像
    // viewVedio(){
    //   console.log(this.eventDiscovery,"111222")
    //   if(this.eventDiscovery.startTime == null){
    //     this.$modal.msgError("请选择历史时间！");
    //   }else {
    //     //请求前先关闭
    //     console.log(this.vedioData,"hhjjhhjj");
    //     if(this.vedioData != {} && this.vedioData != undefined){
    //       this.closeDialog();
    //     }
    //     let param = {
    //       id: this.eventDiscovery.id,
    //       tunnelId: this.eventDiscovery.tunnelId,
    //       startTime: this.eventDiscovery.startTime
    //     }
    //     getVedioData(param).then((res) => {
    //       console.log(res,"sssssssssssssssss");
    //       this.vedioData = res.data.vedioData;
    //       this.eventDiscovery.historyUrlList = res.data.vedioList
    //       this.vedioPlayer = true;
    //       console.log(this.eventDiscovery,"111222")
    //     })
    //   }

    // },
    // closeDialog(){
    //   console.log("关闭了关闭了")
    //   console.log(this.vedioData)
    //   if(this.vedioData.camID != undefined){
    //     closeVedio(this.vedioData.camID,this.vedioData.playId).then((res) => {
    //       this.vedioPlayer = false;
    //     })
    //   }
    // },
    closeDetail() {
      this.deviceIndexShow = 0;
      // this.activeName = "0";
      this.dialogVisibleDevice = false;
    },
    getStrategyData(item) {
      console.log(item);
      let param = {
        tunnelId: item.tunnelId,
        direction: item.direction,
        eventType: item.eventTypeId,
      };
      getStrategyData(param).then((res) => {
        console.log(res.data, "策略列表");
        //debugger
        this.strategyList = [];
        this.strategyList = res.data;
        this.eventFormDetail.currencyId = res.data[0].id;
      });
    },
    handleClickDevice(tab, event) {
      console.log(tab.index);
      this.deviceIndexShow = tab.index;

    },

    handleChangeControl() {
      this.$forceUpdate();
      this.queryParams.eventTypeId = null;
      //查询事件类型
      let prevControlType = {
        isUsable: "1",
        prevControlType: this.queryParams.prevControlType,
      };
      listEventType(prevControlType).then((response) => {
        this.eventTypeDataList = [...response.rows];
      });
    },
    // 打开复核内详情
    openDoor(item) {
      // 点击查看按钮重置tab
      this.deviceIndexShow = 0;
      // this.activeName = "0";

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
        this.activeName = res.data[0].tableName;
        this.dialogVisibleDevice = true;
      });
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 点击缩略图
    clickImg(gImgUrl) {
      let imgurl = gImgUrl.substr(-3, 3);
      if (imgurl == "jpg") {
        this.alongImgUrl = gImgUrl;
        this.dialogVisibleImg = true;
      }
      console.log(imgurl);
    },
    // 复核内图片展示弹窗关闭事件
    handleCloseImg(done) {
      done();
    },
    changeCheckBox(e) {
      console.log(this.checkBoxEventState, "queryParams.eventState");
    },
    changeInput() {
      this.$forceUpdate();
    },
    bodyCloseMenus1(e) {
      let self = this;
      if (self.zd_boxShow == true) {
        self.$nextTick(() => {
          if (
            !this.$refs.main1.contains(e.target) &&
            !this.$refs.cc1.contains(e.target)
          ) {
            if (self.zd_boxShow == true) {
              self.zd_boxShow = false;
            }
          }
        });
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus1);
    },
    getReservePlanData() {
      this.ReservePlanList = [];
      this.eventFormDetail.currencyId = "";
      if (this.eventFormDetail.eventTypeId == 20) {
        this.eventFormDetail.laneNo = ["1", "2", "3"];
      }
      let data = {
        tunnelId: this.eventFormDetail.tunnelId,
        planTypeId: this.eventFormDetail.eventTypeId,
        direction: this.eventFormDetail.direction,
        eventGrade: this.eventFormDetail.eventGrade,
      };
      getReservePlanData(data).then((res) => {
        this.ReservePlanList = res.data;
        if (this.ReservePlanList.length > 0) {
          this.eventFormDetail.currencyId = this.ReservePlanList[0].id;
        } else {
          if (this.eventFormDetail.eventState == "0") {
            this.$modal.msgWarning("暂无相关预案");
          }
        }
      });
    },
    // 下载事件报告
    downFile() {
      const data = { id: this.eventDiscovery.id };
      detailExport(data).then((res) => {
        let blob = new Blob([res], {
          type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        });
        const createFile = document.createElement("a");
        createFile.href = URL.createObjectURL(blob);
        createFile.download = `事件详情报告.docx`;
        createFile.click();
        document.body.appendChild(createFile);
        document.body.removeChild(createFile); // 下载完成移除元素
        window.URL.revokeObjectURL(createFile.href); // 释放掉blob对象
      });
    },
    // 事件处置 一键
    getYiJian(item) {
      console.log(item, "一键");
      var that = this;
      let arr = [];
      for (let itm of item.children) {
        arr.push(itm.id);
      }

      this.$confirm("是否确认执行?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        let planId = item.reserveId;
        let eventId = that.$route.query.id;

        implementPlan(planId, eventId).then((response) => {
          console.log(response, "一键下发成功");
          for (let item of that.incHandList) {
            for (let itm of item.children) {
              for (let it_m of arr) {
                if (itm.id == it_m) {
                  itm.eventState = "1";
                  that.$modal.msgSuccess("一键下发成功");
                }
              }
            }
          }
          this.evtHandle();
          this.getEventList();
        });
      });
    },
    //打开详情弹窗
    detailsOpen(item) {
      this.prevControlType = item.prevControlType;
      let data = { id: item.id };
      getEventDetail(data).then((res) => {
        console.log(res, "详情数据");
        this.eventDiscovery = res.data.eventDiscovery;
        console.log(res.data.manualReview);
        this.manualReview = !!res.data.manualReview
          ? res.data.manualReview
          : {};
        this.planDisposal = res.data.planDisposal;
        this.disposalRecord = res.data.disposalRecord;
        this.eventStateCurrent = res.data.eventState;
        this.endReport = res.data.endReport;
        this.tacticsList = res.data.tacticsList;
        this.isAuto = res.data.isAuto;
      });
      (this.historyIndex = "first0"), (this.dialogTableVisible = true);
    },
    eventIsShow(value, state) {
      if (value != null) {
        if (state != "0" && (!!value ? value.includes("其他") : false)) {
          return true;
        }
      } else {
        return false;
      }
    },
    isShow(item) {
      if (item.eventState == "3") {
        return true;
      }
    },
    reviewRemarkChange() {
      console.log(this.eventFormDetail.reviewRemark);
    },
    // 复核弹窗内单选改变事件
    eventStateChange() {
      if (this.eventFormDetail.eventState != 0) {
        this.eventFormDetail.currencyId = "";
      } else {
        if (this.ReservePlanList.length > 0) {
          this.eventFormDetail.currencyId = this.ReservePlanList[0].id;
        } else {
          this.eventFormDetail.currencyId = "";
        }
      }
      this.eventFormDetail.reviewRemark = [];
    },
    getPlanType() {
      let data = { prevControlType: 0 };
      listEventType(data).then((response) => {
        this.planTypeData = response.rows;
      });
    },
    // 打开图片变视频弹窗
    openPicDialog(item) {
      console.log(item);
      if (!item.videoUrl && !item.imgUrl) {
        this.$message.warning("暂无视频");
      } else {
        this.videoUrl =
          item.videoUrl == undefined ? item.imgUrl : item.videoUrl;
        // this.videoUrl = item.videoUrl;
        this.picUrlDialog = true;
      }
    },
    // 忽略
    hulue() {
      const param = {
        id: this.eventForm.id,
        eventState: 2,
      };
      updateEvent(param).then((res) => {
        that.$modal.msgSuccess("忽略成功");
      });
    },
    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.eventForm.id }).then((res) => {
        this.eventWarnList = res.rows;
      });
    },
    // 事件处置 一键
    getYiJian(item) {
      var that = this;
      // let str = ''
      let arr = [];
      for (let itm of item.children) {
        arr.push(itm.id);
      }
      // str = arr.join(',')

      this.$confirm("是否确认执行?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        let strategyId = item.reserveId;
        let eventId = that.eventForm.id;

        implementDisposalStrategy(eventId, strategyId).then((response) => {
          for (let item of that.incHandList) {
            for (let itm of item.children) {
              for (let it_m of arr) {
                if (itm.id == it_m) {
                  itm.eventState = "1";
                  that.$modal.msgSuccess("一键下发成功");
                }
              }
            }
          }
        });
      });
    },
    // 处置
    management(id) {
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: id },
      });
      //
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val;
      this.getList();
    },
    // 复核提交
    submitDialog() {
      console.log(this.eventFormDetail, "1123123");
      this.$refs["eventFormDetail"].validate((valid) => {
        if (valid) {
          this.$cache.local.set("currencyId", this.eventFormDetail.currencyId);
          const loading = this.$loading({
            lock: true,
            text: "Loading",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
            target: ".hitchDialog",
          });
          if (
            this.eventFormDetail.stakeNum1 &&
            this.eventFormDetail.stakeNum2
          ) {
            this.eventFormDetail.stakeNum =
              "K" +
              this.eventFormDetail.stakeNum1 +
              "+" +
              this.eventFormDetail.stakeNum2;
          }
          if (
            this.eventFormDetail.stakeEndNum1 &&
            this.eventFormDetail.stakeEndNum2
          ) {
            this.eventFormDetail.stakeEndNum =
              "K" +
              this.eventFormDetail.stakeEndNum1 +
              "+" +
              this.eventFormDetail.stakeEndNum2;
          }
          if (this.eventFormDetail.reviewRemark.includes("其他")) {
            this.eventFormDetail.reviewRemark =
              this.eventFormDetail.reviewRemark.toString() +
              ":" +
              this.eventFormDetail.otherContent;
          } else {
            this.eventFormDetail.reviewRemark =
              this.eventFormDetail.reviewRemark.toString();
          }
          // if (
          //   (this.eventFormDetail.eventState == "0" &&
          //     this.eventFormDetail.currencyId == "") ||
          //   this.eventFormDetail.currencyId == null
          // ) {
          //   return this.$modal.msgWarning("请选择事件处置预案");
          // }
          const currencyId = this.eventFormDetail.currencyId;
          if (this.eventFormDetail.laneNo) {
            this.eventFormDetail.laneNo =
              this.eventFormDetail.laneNo.toString();
          }

          updateEvent(this.eventFormDetail).then((response) => {
            this.processDialog = false;
            this.closeProcessDialog = false;
            this.processType = false;
            this.details = false;
            this.cameraPlayer = false;
            if (response.code == "200") {
              this.$modal.msgSuccess("修改成功");
              if (
                this.evtWebsoktList.length > 0 &&
                this.evtWebsoktList[0].ids == this.eventFormDetail.id
              ) {
                console.log("点复核提交 关弹窗");
                bus.$emit("closeDialog");
              }
            }
            this.getList();
            //主动安全
            //策略不为空
            // if (
            //   this.eventFormDetail.prevControlType == 1 &&
            //   currencyId &&
            //   this.eventFormDetail.eventState == 0
            // ) {
            //   let id = currencyId;
            //   handleStrategy(id).then((res) => {
            //     console.log(res);
            //     this.$modal.msgSuccess("下发指令成功");
            //   });
            // }
            loading.close();
            // 1.预案不为空
            // 2.当前状态为0
            // 3.普通事件
            if (currencyId && this.eventFormDetail.eventState == 0) {
              this.$router.push({
                path: "/emergency/administration/dispatch",
                query: { id: this.eventFormDetail.id },
              });
            }
            this.$cache.local.remove("currencyId");
          });
        }
      });
    },
    // changeEndTime() {
    //   let startTime = new Date(this.eventFormDetail.eventTime).getTime();
    //   let endTime = new Date(this.eventFormDetail.endTime).getTime();
    //   console.log(startTime, endTime);
    //   if (endTime < startTime) {
    //     this.$modal.msgWarning("结束时间必须大于开始时间");
    //     this.eventFormDetail.endTime = "";
    //   }
    // },
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
    getFrom(from) {
      for (let item of this.fromList) {
        if (from == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    // 关闭流程弹窗
    cancelProcessDialog() {
      this.processDialog = false;
      this.processType = false;
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
    // 事件处置
    evtHandle() {
      const param = {
        tunnelId: this.tunnelId,
        id: this.evtId,
        eventTypeId: this.eventTypeId,
        direction: this.direction,
      };
      getSafetyHandle(param).then((res) => {
        let list = this.handleTree(res.data, "flowId", "flowPid");
        this.incHandList = list;
        this.$forceUpdate();
      });
    },
    countLetters(string) {
      var pattern = /[A-Za-z]/g; // 匹配所有字母的正则表达式
      var match = string.match(pattern);
      return match ? match.length : 0;
    },
    //详情弹窗
    detailsButton(item) {
      console.log(item, "000000000");
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
      this.eventFormDetail = { ...item };
      this.eventFormDetail.eventState = 4;
      // if (item.prevControlType == 1) {
      //   this.getStrategyData(item);
      // } else {
      //   this.getReservePlanData();
      // }
      this.getReservePlanData();

      this.$nextTick(() => {
        if (this.$refs.swiperTop) {
          const swiperTop = this.$refs.swiperTop.$el.swiper;
          const swiperThumbs = this.$refs.swiperThumbs.$el.swiper;
          swiperTop.controller.control = swiperThumbs;
          swiperThumbs.controller.control = swiperTop;
          swiperThumbs.activeIndex = 0;
          swiperTop.activeIndex = 0;
        }
      });
      this.getEventList();
      // debugger
      if (item.stakeNum) {
        let stake = "";

        let letterCount = this.countLetters(item.stakeNum.split("+")[0]);
        console.log("字母数量：" + letterCount);
        console.log(item.stakeNum.split("+")[0]);
        if (letterCount >= 2) {
          stake = item.stakeNum.split("+")[0].substr(2);
        } else if (letterCount >= 1) {
          stake = item.stakeNum.split("+")[0].substr(1);
        } else {
          stake = item.stakeNum.split("+")[0];
        }
        this.$set(this.eventFormDetail, "stakeNum1", stake);
        this.$set(
          this.eventFormDetail,
          "stakeNum2",
          item.stakeNum.split("+")[1]
        );
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
    getImgUrls(item) {
      this.urlsList = [];
      this.urlsAll = [];
      const param = {
        businessId: item.id,
      };
      image(param).then((response) => {
        this.urls = response.data;
        this.arrowRight2 = false;
        this.urlsList = this.urls;
      });
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
    getVideoUrl(item) {
      this.cameraPlayer = false;
      console.log(item, "itemitem");
      //现场
      // getEventCamera(item.tunnelId, item.stakeNum, item.direction).then((res)=>{
      //   getDeviceById(res.data[0].eqId).then((response)=>{
      //     console.log(response,"00000000000000000")
      //     displayH5sVideoAll(response.data.secureKey,'h5sVideo1',1);
      //   })
      // })
      // 公司
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
                  if (response.data != null) {
                    this.videoList.push(response.data);
                  }
                  this.cameraPlayer = true;
                }
              });
            }
          }
        }
      );
    },
    turnLeft() {
      this.arrowRight = true;
      this.imgPage--;
      this.imgUrlList = this.iconUrlListAll[this.imgPage];
      if (this.imgPage == 0) {
        this.arrowLeft = false;
      }
    },
    turnRight() {
      this.imgPage++;
      this.imgUrlList = this.iconUrlListAll[this.imgPage];
      this.arrowLeft = true;
      if (this.imgPage == this.iconUrlListAll.length - 1) {
        this.arrowRight = false;
      }
    },
    turnLeft2() {
      this.arrowRight2 = true;
      this.imgPage2--;
      this.urlsList = this.urlsAll[this.imgPage2];
      if (this.imgPage2 == 0) {
        this.arrowLeft2 = false;
      }
    },
    turnRight2() {
      this.imgPage2++;
      this.urlsList = this.urlsAll[this.imgPage2];
      this.arrowLeft2 = true;
      if (this.imgPage2 == this.urlsAll.length - 1) {
        this.arrowRight2 = false;
      }
    },
    //选事件类型
    handleEvtButton(item) {
      this.queryParams.eventTypeId = item;
      this.getList();
    },
    //导出
    handleExport() {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出设备故障数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportFaultList(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    handleSelectionChange(val) {
      this.ids = val.map((item) => item.id);
      this.single = val.length !== 1;
      this.multiple = !val.length;
    },
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
      });
    },
    accidentInit(eventId) {
      var eventId = { eventId: eventId };
      listEventFlow(eventId).then((result) => {
        this.dialogEventList = result.rows;
      });
    },
    /** 巡查班组 */
    getBz() {
      listBz().then((response) => {
        this.bzData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 设备 */
    getDevices() {
      listDevices({
        fEqId: this.form.codePlcId,
        eqType: this.form.deviceTypeId,
      }).then((response) => {
        this.eqListData = response.rows;
      });
    },
    changeState(row, state) {
      this.$confirm("是否确认忽略此事件！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        row.eventState = state;
        return updateEvent(row);
      });
    },
    getEventMsg() {
      // 获取事件预警信息
      getTodayEventCount().then((result) => {
        this.eventMsg = result.data;
        this.$forceUpdate();
      });
    },
    //获取图片视频
    // getUrl(id) {
    //   const param3 = {
    //     businessId: id,
    //   };
    //   const param4 = {
    //     id: id,
    //   };
    //   image(param3).then((response) => {
    //     console.log(response.data.length);
    //     if (response.data.length >= 1) {
    //       this.urls = response.data;
    //     }
    //   });
    //   video(param4).then((response) => {
    //     console.log(response.data, "视频信息");
    //     this.videoUrl = response.data.videoUrl;
    //   });
    // },
    // 查询方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    /** 查询事件管理列表 */
    getList() {
      this.eventForm.currencyId = "";
      this.ReservePlanList = [];
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      // this.isLoading = true;
      // let options ={}
      // let loadingInstances = Loading.service(options);
      this.eventList = [];
      this.eventLists = [];
      if (this.manageStation == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      if (!this.dateRange) {
        this.dateRange = [];
      }
      this.queryParams.startTime = this.dateRange[0];
      this.queryParams.endTime = this.dateRange[1];
      // this.queryParams.searchValue = this.activeName;
      this.queryParams.eventState = this.checkBoxEventState.toString();
      if (this.fuzzySearch1) {
        this.queryParams.fuzzySearch = this.fuzzySearch1.replace(/\s*/g, "");
      }
      listEvent(this.queryParams).then((response) => {
        console.log(response, "responseresponse");
        for (let item of response.rows) {
          if (item.iconUrlList) {
            for (let i = 0; i < item.iconUrlList.length; i++) {
              // console.log(item.iconUrlList[1].imgUrl,"item.iconUrlList[1].imgUrlitem.iconUrlList[1].imgUrlitem.iconUrlList[1].imgUrl")
              if (item.iconUrlList.length == 1) {
                item.picUrl = item.iconUrlList[0].imgUrl;
              } else {
                item.picUrl = item.iconUrlList[1].imgUrl;
              }
            }
          }
        }
        this.eventList = response.rows;
        // this.isLoading = false;
        console.log(this.eventList, "this.eventListthis.eventList");
        this.total = response.total;
        this.loading = false;
        loading.close();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除选中的数据项？")
        .then(function () {
          return delList(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 所属隧道 */
    getTunnel() {
      if (!this.queryParams.deptId) {
        listTunnels().then((response) => {
          this.tunnelList = response.rows;
        });
      }
    },
    getEventTypeAll() {
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
        prevControlType: item.prevControlType,
      };
      listEventType(prevControlType).then((response) => {
        this.eventTypeData = [...response.rows];
      });
    },
    // 状态字典翻译
    eventStateFormat(row, column) {
      return this.selectDictLabel(this.eventStateOptions, row.eventState);
    },
    // 级别 字典翻译
    eventGradeFormat(row, column) {
      return this.selectDictLabel(this.eventGradeOptions, row.eventGrade);
    },
    /** 已解决按钮操作 */
    handleOk(row) {
      // const ids = row.id || this.ids;
      this.$confirm("是否确认此事件已解决！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          row.eventState = "1";
          return updateEvent(row);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("解决成功");
        })
        .catch(function () {});
    },
    /** 进入应急指挥大屏 */
    loadBigScreen(row) {
      alert("正在研发中...");
    },
    // 关闭弹窗
    close() {
      // 关闭弹出层
      this.record = false;
    },
    handleDetails(row) {
      this.details = true;
      this.title = "事件详情";
      this.eventForm = row;
      this.accidentInit(row.id);
      this.getUrl(row.id);
    },
    handleDispatch(row) {
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: {
          id: row.id,
          tunnelId: row.tunnelId,
          stakeNum: row.stakeNum,
          direction: row.direction,
        },
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        currencyId: "",
        tunnelId: null,
        eventTypeId: null,
        prevControlType: null,
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
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //删除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    async planRoadmapUrl(iFileList) {
      let that = this;
      that.fileList = [];
      if (iFileList != null) {
        for (let i = 0; i < iFileList.length; i++) {
          let iconName = iFileList[i].imgName;
          // let iconUrl = await that.picture(iFileList[i].url);
          let iconUrl = iFileList[i].imgUrl;
          that.fileList.push({
            name: iconName,
            url: iconUrl,
            fId: iFileList[i].businessId,
          });
        }
      }
    },
    /* 请求图片base64地址*/
    picture(fileUrl) {
      return new Promise((resolve, reject) => {
        loadPicture({
          url: fileUrl,
        }).then((response) => {
          if (response.code == 200) {
            let base64 = response.msg;
            resolve(base64); //不可缺少
          }
        });
      });
      return resolve(base64);
    },
    openImg(url) {
      this.img = url;
      this.yn = !this.yn;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 16;
      this.zd_boxShow = false;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.isLoading = true;
      // this.queryParams.pageSize bug:857
      this.dateRange = [];
      // this.tunnelList = [];
      this.queryParams.eventTypeId = "";
      this.queryParams.tunnelId = null;
      this.queryParams.prevControlType = null;
      this.queryParams.direction = null;
      this.queryParams.fuzzySearch = "";
      this.fuzzySearch1 = "";
      this.checkBoxEventState = [];
      // this.resetForm("queryForm");
      this.handleQuery();
    },
    //关闭drawer
    handleClose(done) {
      done();
    },
    /** 提交按钮 */
    submitEventForm() {
      this.dloading = true;
      if (this.submitEventFormLoading) return;
      this.submitEventFormLoading = true;
      this.$refs["form1"].validate(async (valid) => {
        if (valid) {
          await addEvent(this.eventForm).then((response) => {
            if (response.code === 200) {
              this.$modal.msgSuccess("新增成功");
              setTimeout(() => {
                this.resetEvent();
                this.dloading = false;
                this.open = false;
              }, 400);
              this.getList();
            }
          });
        }
        this.submitEventFormLoading = false;
      });
    },
    //关闭弹窗
    eventFormClose() {
      this.resetEvent();
      this.open = false;
    },
    // 表单重置
    resetEvent() {
      this.$refs.form1.resetFields();
      this.eventForm.eventTypeId = null;
      this.eventForm.eventInjured = null;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.details = false;
      this.cameraPlayer = false;
      this.processDialog = false;
      this.processType = false;
      this.reset();
      // this.getList();
    },
  },
};
</script>

<style scoped lang="scss">
::v-deep .dialogForm .el-checkbox-button {
  // background: #052c4d;
}
.theme-light .el-dialog .el-dialog__body .dialogForm .checkBox {
  display: unset !important;
  width: unset !important;
}

.scrollbar_li {
  width: 145px;
  margin-right: 15px;
  display: inline-block;
  white-space: nowrap;
  video {
    width: 100%;
  }
}
::v-deep
  .el-table--striped
  .el-table__body
  tr.el-table__row--striped
  td.el-table__cell,
::v-deep .el-table tr {
  background: unset !important;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view {
  white-space: nowrap;
}
::v-deep .el-carousel__arrow {
  background-color: rgba(31, 45, 61, 0.8);
}
::v-deep .el-carousel__arrow:hover {
  background-color: rgba(31, 45, 61, 0.8);
}
.gallery-thumbs {
  height: 56px;
  box-sizing: border-box;
  // padding: 10px 0;
}
.gallery-thumbs .swiper-slide {
  width: 25%;
  height: 100%;
  opacity: 0.4;
}
.gallery-thumbs .swiper-slide-active {
  opacity: 1;
}

.chuzhi {
  background: #05afe3;
}
.yzx {
  color: #45d20a;
}
.wzx {
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
        text-align: center;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
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
      width: 5.5px;
      border-left: solid 1px #39adff;
      border-bottom: solid 1px #39adff;
      margin-top: 20px;
      // border-right: none;
      border-top-left-radius: 37px;
      border-bottom-left-radius: 37px;
    }
    .gxp {
      width: 77%;
      margin-left: 4px;
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
    display: inline-block;
    margin-right: 0.5vw;
    margin-bottom: 10px;
    // position: relative;
    border-radius: 2px;
    overflow: hidden;
    height: 15.4vh;
    .top {
      height: 76%;
      display: flex;
      position: relative;
      .video {
        width: 40%;
        height: 100%;
        float: left;
        text-align: center;
        font-size: 0.7vw;
        // color: #2aa6ff;

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
    }
    .bottom {
      height: 24%;
      display: flex;
      .eventBox {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: calc(40% - 30px);
        margin: 0 10px;
        .eventType {
          background: rgba(228, 14, 14, 0.4);
          font-size: 0.7vw;
          font-weight: 600;
          color: #fff;

          // padding:5px 10px;
          width: 60%;
          // height: 3vh;
          // line-height: 3vh;
        }
        div {
          background: rgba(228, 14, 14, 0.2);
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          font-size: 0.7vw;
          font-weight: 400;
          color: #fff;
          padding: 2px 0px;
          width: 40%;
          text-align: center;
        }
      }
      .contentButton {
        display: flex;
        justify-content: right;
        width: 60%;
        align-items: center;
        div {
          width: 65px;
          height: 23px;
          border-radius: 14px;
          color: white;
          display: flex;
          justify-content: center;
          align-items: center;
          cursor: pointer;
          margin-left: 10px;
          font-size: 0.7vw;
        }
        div:nth-of-type(1) {
          background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
        }
        div:nth-of-type(2) {
          background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        }
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
        padding: 6px 0;
        span {
          padding-left: 6px;
          font-weight: bold;
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
  align-items: center;
  position: relative;
  .swiper-slide {
    video {
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
    width: 59% !important;
    padding: 0px 10px 10px 10px !important;
    margin-left: 10px;
    // ::v-deep .el-carousel__container{
    //   height:378px;
    // }
    .picBox {
      height: 304px;
    }
    ::v-deep .el-image {
      height: 300px;
      width: 100%;
      image {
        width: 100%;
        height: 100%;
      }
    }
  }
  .dialogBg {
    background: #f7f7f7;
    height: 100%;
    width: 41%;
    color: #0087e7;
    padding: 0px 10px 10px 10px;
    span {
      color: #767676 !important;
      padding-left: 10px;
    }
    .leftVideo {
      width: 100%;
      height: 240px;
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
  margin-top: 10px;
  .el-input {
    width: 100%;
    .el-input--medium .el-input__inner {
      width: 93px;
    }
  }
  .el-form-item {
    margin-bottom: 16px !important;
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
// ::v-deep .el-dialog .el-dialog__header{
//     background-image: url(../../../assets/cloudControl/dialogHeader.png);
//     background-repeat: no-repeat;
//     background-position-x: right;
//     background: linear-gradient(270deg, rgba(1,149,251,0) 0%, rgba(1,149,251,0.35) 100%);
// }
.el-dialog__headerbtn {
  z-index: 3;
}

::v-deep .detailsDialog {
  width: 60%;
  position: absolute;
  left: 20%;
  .el-dialog:not {
    margin-top: 0px !important;
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
      color: #fff;
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
    // background: #052c4d;
    color: #fff;
    // border: solid 1px #39adff;
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
  background-image: url(../../../assets/cloudControl/shebeiWarning.png);
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
    background-image: url(../../../assets/icons/svg/u954.svg);
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
  background-image: url(../../../assets/cloudControl/cardTitle.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  text-align: center;
  width: 139px;
  height: 30px;
  line-height: 30px;
}
.searchSafeWarn {
  // top: 6% !important;
  // right: 0.8% !important;
  // width: 453px !important;
  .el-checkbox + .el-checkbox {
    margin-left: 0 !important;
  }
}
.hitchDialog {
  ::v-deep .el-dialog__body {
    height: 70vh !important;
    overflow: auto !important;
  }
  ::v-deep .el-card {
    margin-bottom: 10px !important;
  }
}
.evtInfo {
  ::v-deep .el-dialog__body {
    max-height: 70vh;
    overflow: auto;
  }
}
.detailsDialog {
  ::v-deep .el-dialog__body {
    max-height: 90vh;
    overflow: auto;
  }
}
.el-divider {
  background-color: transparent;
}
.disposalRecordBox {
  overflow-y: auto;
  height: 420px;
  padding: 20px 10px 20px 25px;
  .el-card {
    // background: #052c4d !important;
  }
}
::v-deep .inputNumStyle {
  .el-input-group__prepend {
    padding: 0 0px 0 16px;
  }
  .el-input__inner {
    padding: 0 15px 0 10px;
  }
}
.el-dialog:not(.is-fullscreen) {
  margin-top: 4vh !important;
}
.flow-description {
  white-space: pre-wrap; /* 换行并保留空格 */
}
</style>
<!-- <style lang="scss">
.evtInfo .el-timeline-item__node {
  background: #39adff !important;
}
.evtInfo .el-timeline-item__tail {
  border-left: 1px dashed #39adff !important;
}
</style> -->
