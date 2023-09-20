<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2023-02-14 14:26:29
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-05-16 15:02:51
 * @FilePath: \tunnel-ui\src\views\event\event\dispatch.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="app-container dispatchAss">
    <div class="tunnelBox3">
      <iframe
        name="tuniframe"
        id="miframe"
        class="map3D"
        frameborder="0"
        align="center"
        allowfullscreen="true"
        allow="autoplay"
        src="http://10.7.179.141:9528/tunnel_hushan"
      ></iframe>
    </div>
    <div class="workBenchButton">
      <img
        src="../../../assets/cloudControl/tunnelBox2.png"
        @click="openWorkBench()"
      />
    </div>
    <div class="drawerBox" @click="drawerHandleOpen()" >
      <i class="el-icon-d-arrow-left" v-show="drawer"></i>
      <i class="el-icon-d-arrow-right" v-show="drawer == false"></i>
      处置记录
    </div>
    <el-drawer
      class="drawerLog"
      title="处置记录"
      :visible.sync="drawer"
      direction="ltr"
      :before-close="drawerHandleClose"
      append-to-body
      :modal="false"
      style="left:26%;">
      <el-col :span="24">
        <el-timeline :reverse="reverse">
          <el-timeline-item
            v-for="(activity, index) in disposalRecord"
            :key="index"
            :timestamp="activity.flowTime"
            style="color: #fff;"
            placement="top"
            type="primary"
            >
            <el-card>
              <h4 style="color: #fff;"> {{activity.flowDescription}}</h4>
              <p style="color: #fff;">用户:{{activity.nickName}}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-col>
    </el-drawer>
    <div class="disRightBox">
      <div class="dispatchLeft">
          <div class="video">
            <div class="title">
              <i class="el-icon-video-camera" style="margin-right:15px;"></i>
              实时视频
              <span>VIDEO MONITORING</span>
              <div class="touping" @click="handleTouping">投屏</div>
            </div>
            <div class="videoBox1">
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm1.liveUrl "
                  :rtsp="videoForm1.liveUrl"
                  :open="videoForm1.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm1.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm2.liveUrl "
                  :rtsp="videoForm2.liveUrl"
                  :open="videoForm2.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm2.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm3.liveUrl "
                  :rtsp="videoForm3.liveUrl"
                  :open="videoForm3.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm3.title }}</div>
              </div>
              <div class="videoContent">
                <videoPlayer
                  v-if="videoForm4.liveUrl "
                  :rtsp="videoForm4.liveUrl"
                  :open="videoForm4.cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ videoForm4.title }}</div>
              </div>
              <!-- <div
                v-for="(item, index) of videoList"
                :key="index"
                class="videoContent"
              >
                <videoPlayer
                  v-if="item.liveUrl "
                  :rtsp="item.liveUrl"
                  :open="cameraPlayer"
                  class="video"
                ></videoPlayer>
                <div class="videoListTitle">{{ item.title }}</div>
              </div> -->
            </div>
          </div>
          <div class="evtMessage">
            <div class="title">
              <i class="el-icon-notebook-2" style="margin-right:15px;"></i>
              事件信息
              <span>EVENT INFORMATION</span>
            </div>
            <div class="evtMessBox">
              <div class="evtMessLeft">
                <div>
                  <div>事件来源：</div>
                  <div>{{ getFrom(eventForm.eventSource) }}</div>
                </div>
                <div>
                  <div>事件类型：</div>
                  <div>{{ eventForm.simplifyName }}</div>
                </div>
                <div>
                  <div>所属隧道：</div>
                  <div>{{ eventForm.tunnelName }}</div>
                </div>
                <div>
                  <div>方向：</div>
                  <div>{{ getDirection(eventForm.direction,eventForm) }}</div>
                </div>
                <div>
                  <div style="display: flex;justify-content: left;align-items: center;">影响车道：</div>
                  <div style="width: 45%;">{{ eventForm.laneNoName }}</div>
                </div>
                <div>
                  <div>桩号：</div>
                  <div>{{ eventForm.stakeNum }}</div>
                </div>
              </div>
              <div class="evtMessRight">
                <div class="evtMessVideo">
                  <video
                    :src="eventForm.videoUrl"
                    controls
                    muted
                    loop
                    fluid
                  ></video>
                </div>
                <div class="evtMessImg" v-if="eventForm.iconUrlList && eventForm.iconUrlList.length > 0">
                  <el-image
                    v-for="(item, index) of eventForm.iconUrlList"
                    :key="index"
                    :src="item.imgUrl"
                    :preview-src-list="Array(item.imgUrl)"
                  ></el-image>
                </div>
              </div>
            </div>
          </div>
          <div class="plan">
            <div class="title">
              <i class="el-icon-phone-outline" style="margin-right:15px;"></i>
              调度联络
              <span>DISPATCHING LIAISON</span>
            </div>
            <div>
              <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
                <el-tab-pane v-for="(item,index) in emergencyList" :key="index"
                 :label="item.dictLabel" :name="item.dictValue">
            <el-table
              :data="implementList"
              stripe
              class="phoneTable"
              :fit="true"
              height="180"
            >
              <el-table-column
                label="姓名"
                align="center"
                prop="userName"
                      width="100"
              />
              <el-table-column label="联系方式" align="center" prop="phone" width="150">
                <template slot-scope="scope">
                  <span>{{ scope.row.phone }}</span>
                </template>
              </el-table-column>
                  <el-table-column label="岗位" align="center" prop="groupName" width="150"/>
            </el-table>
                </el-tab-pane>
              </el-tabs>
          </div>
        </div>
    </div>
    </div>
    <div class="disLeftBox">
      <div style="height:100%;">
      <div class="IncHand">
        <div class="title">
          <i class="el-icon-document" style="margin-right:15px;"></i>
          事件处置
          <span class="small">EVENT HANDLING</span>
        </div>
        <div class="incHandBox">
          <div class="GTop">
            <div class="GT_one one_box">
              <p>{{eventInfo.planName}}</p>
              <span>{{eventInfo.eventGrade}}</span>
            </div>
            <div class="GT_one two_box">
              <i class="el-icon-time"></i>
              <p>{{deadline4}}</p>
            </div>
          </div>
          <div class="dieBox" style="height: 75%;overflow-y: auto;">
            <div class="heightBox" style="height:100%;">
              <div
                v-for="(item, index) of incHandList"
                :key="index"
                :class="item.reserveId?'incHandContent incHandContentBox':'incHandContent'"
                :ref="'incHandContent'+index"
              >
                <div :class="item.reserveId?'classification classificationBox':'classification'">
                  <div class="topDashed" v-show="index != 0"
                    :style="{'top':circlePosition}">
                    <p
                      :style="{'height':lineHeight+'px'}">
                    </p>
                    <span class="topCircle"></span>
                  </div>
                  <div class="menuBox">
                    <el-tooltip class="item" effect="dark" :content="item.flowContent" placement="right">
                      <div
                        class="type"
                        :style="{
                          padding: item.flowContent
                            ? item.flowContent.toString().length > 2
                              ? '8px'
                              : '15px 12px'
                            : '',
                        }"
                        v-if="item.flowContent"
                      >
                        {{ item.flowContent }}
                      </div>
                    </el-tooltip>
                    <div
                      v-show="getShow(item,index) == false && index != '0'"
                      class="yijian" @click="getYiJian(item)"
                      style="cursor: pointer">
                      一键
                    </div>

                    <div class="yijian"
                      v-show="getShow(item,index) == true && index != '0'"
                      @click="getYiJian(item)"
                      style="cursor: pointer;">
                      详情
                  </div>
                  </div>
                  <div class="dashed"
                    :style="{top:index == 0?'55px':'76px'}"
                    v-show="index != incHandList.length - 1">
                    <span class="circle"></span>
                    <p :style="{height:index == 0?'50px':lineHeight + 12 + 'px'}"></p>
                  </div>
                </div>

                <div class="heng1" v-if="item.children"></div>
                <div
                  class="shu"
                  v-if="item.children"
                  :style="{
                    height: item.children
                      ? item.children.length > 1
                        ? item.children.length * 50 +
                          4 * item.children.length -
                          50 +
                          'px'
                        : '0px'
                      : '',
                  }"
                ></div>
                <div style="width:100%;">
                  <div
                    v-for="(itm, inx) of item.children"
                    :key="inx"
                    class="contentList"
                    style="width:100%;"
                  >
                    <div style="float: left;padding-right: 20px;line-height: 20px;">{{ itm.flowContent }}</div>
                    <!-- 预警完成按钮 -->
                    <el-button type="success"
                      plain
                      size="mini"
                      style="float: right; "
                      icon="el-icon-check"
                      v-show="itm.eventState != '0' && !itm.processId">
                      完成
                    </el-button>
                    <!-- 预案完成按钮 -->
                    <el-button type="success"
                      plain
                      size="mini"
                      style="float: right; "
                      icon="el-icon-check"
                      v-show="itm.eventState != '0' && itm.processId && itm.eqTypeId != '29'"
                      @click="getManagementDevice(itm)">
                      完成
                    </el-button>
                    <el-button type="success"
                      plain
                      size="mini"
                      style="float: right; "
                      icon="el-icon-check"
                      v-show="itm.eventState != '0' && itm.eqTypeId == '29'"
                      @click="getManagementDevice(itm)">
                      打开
                    </el-button>
                    <!-- <img
                      :src="incHand2"
                      style="float: right; "
                      v-show="itm.eventState != '0'"
                    /> -->
                    <!-- 下发 -->
                    <div class="sendMsg" v-show="itm.eventState == '0'" @click="getManagementDevice(itm)">
                      <el-button type="primary" size="mini" icon="el-icon-pie-chart">处置</el-button>
                      <!-- <img
                        :src="incHand1"
                        style="float: right; "
                        v-show="itm.eventState == '0'"

                        :style="iconDisabled?'cursor: not-allowed;pointer-events: none;':'cursor: pointer'"
                      />
                      <p >处置</p> -->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- <el-divider></el-divider> -->
          <div class="rightButton">
            <el-button round type="primary" @click="over">立即完结</el-button>
            <el-button round type="warning" @click="levelTop">警情升级</el-button>
          </div>
        </div>
      </div>
    </div>
    </div>
    <!-- <el-row style="display: none;">
      <el-col :span="7">
      </el-col>
      <el-col :span="17">
        <div class="dispatchRight" style="display:none;">
          <div class="workBenchBox">
            <div class="tunnelBox1" v-show="activeMap == 1">
              <div style="width:100%;height:100%;overflow:hidden">
                <div class="tunnelMap"
                  @mousedown="dragImg"
                  ref="dragImgDom"
                  @contextmenu.prevent>
                  <el-image
                    class="back-img"
                    :src="backImg"
                    :style="{
                      width: picWidth / 1.23 + 'px',
                  }"
                  ></el-image>
                  <div class="wrapper" id="eq-wrapper">
                    <div
                      class="icon-box active"
                      v-for="(item, index) in selectedIconList"
                      :key="index"
                      @click="openStateSwitch(item)"
                      :style="{
                        left: item.position.left / 1.23 + 'px',
                        top: item.position.top / 1.23 + 'px',
                        'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                      }"
                      :class="
                        item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                          ? 'light-' + item.position.left
                          : ''
                      "
                    >
                      <div :class="{ focus: item.focus }">
                        <img
                          v-show="item.eqType != ('31' || '16' || '36')"
                          v-for="(url, indexs) in item.url"
                          style="position: absolute"
                          :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth / 1.23"
                          :height="item.iconHeight / 1.23"
                          :key="item.eqId + indexs"
                          :src="url"
                        />
                        <img
                          v-show="item.eqType == '31'"
                          style="position: absolute"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth / 1.3"
                          :height="item.iconHeight"
                          :src="getTypePic(item)"
                        />
                        <div
                          v-show="item.eqType == '16' || item.eqType == '36'"
                          style="
                            position: absolute;
                            overflow: hidden;
                            writing-mode: tb-rl;
                            font-size: 12px;
                            color: #ffff07;
                            text-align: center;
                            padding: 2px;
                          "
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border: item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.iconWidth / 1.3 + 'px',
                            height: item.iconHeight / 1.3 + 'px',
                          }"
                          :src="getTypePic(item)"
                        >
                          {{ item.eqName }}
                        </div>

                      </div>
                    </div>
                  </div>
                  <div class="accBox">
                    <div class="accTop">
                      <div class="accPoint"
                      :style="{
                        height:160/tunnelLane + 'px',
                        width:160/tunnelLane + 'px',
                        left:accLeft + '%',
                        top:accTop + '%'
                      }"
                      v-if="eventForm.direction == '2'"
                      >
                    <img :src="assIconUrl"></img>
                    </div>
                    </div>
                    <div class="accBottom">
                      <div class="accPoint"
                      :style="{
                        height:160/tunnelLane + 'px',
                        width:160/tunnelLane + 'px',
                        left:accLeft + '%',
                        top:accTop + '%'
                      }"
                      v-if="eventForm.direction == '1'"
                      >
                      <img :src="assIconUrl"></img>
                    </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>

            <div class="tunnelBox2">
              <div>
                <img
                  src="../../../assets/cloudControl/tunnelBox1.png"
                  style="transform: translate(-9px, -3px)"
                  @click="backSafeWarn()"
                />
              </div>
              <div>
               
                <img
                  src="../../../assets/cloudControl/tunnelBox2.png"
                  @click="changeActiveMap(1)"
                />
                <img
                  src="../../../assets/cloudControl/tunnelBox3.png"
                  @click="changeActiveMap(2)"
                />

              </div>
            </div>
          </div>
          <div class="rightBottom">
            <div class="evtManagement">
              <div class="DisRecords">
                <div class="title">处置记录</div>
                <el-timeline style="height: calc(100% - 40px); overflow: auto">
                  <el-timeline-item
                    placement="top"
                    v-for="(item, index) in eventList"
                    :key="index + item.flowTime"
                  >
                    <div>{{ item.flowDescription }}</div>
                    <div>{{ item.flowTime }}</div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </div>
            <div class="implement1">
              <div class="eqRecord">
                <div class="title">设备执行记录</div>
                <div class="eqRecordBox">
                  <div
                    class="implementContent"
                    v-for="(item, index) in zxList"
                    :key="index"
                  >
                    <el-image
                      class="implementIcon"
                      :src="eqRecordIcon"
                    ></el-image>
                    <div class="contentBox">
                      <div class="row1">
                        <div>{{ item.eqName }}</div>
                        <div style="padding-left: 20px; float: right">
                          {{ item.executeTime }}
                        </div>
                      </div>
                      <div class="row2">
                        <div>
                          {{ getDirection(item.eqDirection) }}
                          {{ getEqType(item.state, item.eqType) }}
                          {{ getExecuteResult(item.executeResult) }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row> -->
    <com-video
      class="comClass"
      v-if="[23, 24, 25].includes(this.eqInfo.clickEqType)"
      @dialogClose="dialogClose"
      :eqInfo="this.eqInfo"
      :eqTypeDialogList="this.eqTypeDialogList"
      :brandList="this.brandList"
      :directionList="this.directionList"
    ></com-video>
    <com-light
      class="comClass"
      v-if="
        [1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13].includes(this.eqInfo.clickEqType)
      "
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
      :eqTypeDialogList="this.eqTypeDialogList"
      :brandList="this.brandList"
      :directionList="this.directionList"
    ></com-light>
    <com-covi
      class="comClass"
      v-if="this.eqInfo.clickEqType == 19"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-covi>
    <com-data
      class="comClass"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      v-if="[14, 21, 32, 33, 15, 35,40,39,48,45].includes(this.eqInfo.clickEqType)"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-data>
    <com-wind
      class="comClass"
      v-if="this.eqInfo.clickEqType == 17"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-wind>
    <com-pressure
      class="comClass"
      v-if="this.eqInfo.clickEqType == 28"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-pressure>
    <com-vehicleDetec
      class="comClass"
      v-if="this.eqInfo.clickEqType == 20"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-vehicleDetec>
    <com-callPolice
      class="comClass"
      v-if="this.eqInfo.clickEqType == 34"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-callPolice>
    <!-- <com-robot
      class="comClass"
      v-if="this.eqInfo.clickEqType == 29"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-robot> -->
    <com-bright
      class="comClass"
      v-if="this.eqInfo.clickEqType == 5 || this.eqInfo.clickEqType == 18"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-bright>
    <com-youdao
      class="comClass"
      v-if="this.eqInfo.clickEqType == 31 || this.eqInfo.clickEqType == 30"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-youdao>
    <com-board
      class="comClass"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      v-if="this.eqInfo.clickEqType == 16 || this.eqInfo.clickEqType == 36"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-board>
    <!-- <work-bench ref="workBench"></work-bench> -->
    <el-dialog
      title="警情升级"
      class="jingqing"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
      :close-on-click-modal="false">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="levelForm" :model="levelForm" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="当前类型" prop="typeName">
              <el-input
                v-model="levelForm.typeName"
                :disabled="true">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前等级" prop="dengji">
              <el-input
                v-model="levelForm.dengji"
                :disabled="true">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="当前预案" prop="yaName">
              <el-input
                v-model="levelForm.yaName"
                :disabled="true">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更改等级" prop="eventGrade">
              <el-select v-model="levelForm.eventGrade" placeholder="请选择等级" @change="levelChange(levelForm.eventGrade)">
                <el-option v-for="dict in eventGradeList"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更改预案" prop="currencyId">
            <el-select v-model="levelForm.currencyId" placeholder="请选择预案">
              <el-option label="区域一"  v-for="(item,index) in ReservePlanList"
                :key="item.id"
                :label="item.planName"
                :value="item.id"
                >
              </el-option>
            </el-select>
          </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="升级原因" prop="remark">
            <el-input
              type="textarea"
              :rows="5"
              placeholder="请输入升级原因"
              v-model="levelForm.remark">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="changeLevel" class="submitButton">确 定</el-button>
        <el-button @click="closeDialog()" class="closeButton">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 执行弹窗 -->
    <el-dialog
      title="批量控制"
      :visible.sync="IssuedDialog"
      width="50%"
      text-align="center"
      class="IssuedDialog"
      append-to-body
      :close-on-click-modal="false"
      >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="GDeviceBox">
        <el-row>
          <el-col :span="24">
            <p style="padding:15px;">设备控制</p>
            <!-- <el-card shadow="always"> -->
              <el-table
                :data="GDeviceData.deviceList"
                stripe
                max-height="400"
                class="phoneTable"
                :fit="true"
                empty-text="该桩号下暂无匹配设备"
              >
                <el-table-column
                  label="设备名称"
                  align="center"
                  prop="eqName"
                />
                <el-table-column
                  label="桩号"
                  align="center"
                  prop="eqPile"
                />
                <el-table-column label="执行状态" align="center">
                  <template>
                    <span>{{ deviceStateName }}</span>
                  </template>
                </el-table-column>
              </el-table>
            <!-- </el-card> -->
          </el-col>
          <el-col :span="24" v-if="GDeviceData.vmsData">
            <p style="padding:15px;">{{boxName}}:</p>
            <el-card shadow="always">
              <div style="display: flex;justify-content: flex-start;align-items: center;">
                <div :style="{
                  'width':GDeviceData.vmsData['width'] + 'px',
                  'height':GDeviceData.vmsData['height'] + 'px',
                  'color':GDeviceData.vmsData['font_color'],
                  'font-size':GDeviceData.vmsData['font_size'] + 'px',
                  'font-family':GDeviceData.vmsData['font_type'],
                  'background-color':'#000',
                  'position':'relative',
                  }">
                  <span :style="{
                    'position':'absolute',
                    'top':GDeviceData.vmsData['top'] + 'px',
                    'left':GDeviceData.vmsData['left'] + 'px',
                  }"
                  style="line-height:1"
                  v-html="GDeviceData.vmsData['content'].replace(
                              /\n|\r\n/g,
                              '<br>'
                            ).replace(/ /g, ' &nbsp')">
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24" v-if="boxName == '下发指令' || boxName == '执行文件' && GDeviceData.deviceList != undefined">
            <p style="padding:15px;">{{boxName}}:</p>
            <el-card v-show="GDeviceData && !GDeviceData.vmsData" shadow="always">
              <div style="display: flex;align-items: center;">
                <img v-for="(items,idx) in GDeviceData.deviceIconUrl" :key="idx"
                  :src="items"
                />
                <p style="padding-left: 15px;">{{ GDeviceData.deviceState }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-show="buttonDisable" @click="changeIncHand" class="submitButton">执 行</el-button>
        <el-button @click="cancelIssuedDialog" class="closeButton">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 一键详情弹窗 -->
    <el-dialog
      class="yjBox"
      title="设备控制详情"
      :visible.sync="oneKeyDialogVisible"
      width="60%"
      :before-close="oneKeyHandleClose"
      :append-to-body="true"
      :close-on-click-modal="false"
      >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="GDeviceBox" style="overflow-y:auto ;max-height:60vh">
        <p class="empty-text" v-show="oneKeyList.length < 1">暂无匹配设备</p>
        <div v-for="(item,index) in oneKeyList" :key="index" class="GDeviceCard">
          <el-card >
            <p style="padding-bottom:15px;">{{item.processName}}</p>
            <el-table
              v-show="oneKeyList.length >= 1"
              :data="item.deviceList"
              style="width: 100%" >
              <el-table-column
                prop="eqName"
                label="设备名称"
                align="center"
                >
              </el-table-column>
              <el-table-column
                prop="eqPile"
                label="设备桩号"
                align="center"
                >
              </el-table-column>
              <el-table-column label="模板内容" align="center" v-if="item.deviceType == '16' || item.deviceType == '36'">
                {{item.vmsData['content']}}
              </el-table-column>
              <el-table-column label="下发指令" align="center" v-if="item.deviceType != '22' && item.deviceType != '16' && item.deviceType != '36'">
                {{item.deviceState}}
              </el-table-column>
              <el-table-column label="执行文件" align="center" v-if="item.deviceType == '22'">
                {{item.lsData}}
              </el-table-column>
              <el-table-column label="执行状态" align="center">
                {{item.handleState == '0' ? '未执行':'已执行'}}
              </el-table-column>
            </el-table>
          </el-card>
          <el-card >
            <!-- 情报板 -->
            <p v-if="item.deviceType == '16' || item.deviceType == '36'" style="padding-bottom:15px;">执行模板:</p>
            <div class="templateImg" v-if="item.deviceType == '16' || item.deviceType == '36'">
              <!-- <p>情报板执行模板</p> -->
              <el-card shadow="always">
                <div style="display: flex;justify-content: flex-start;align-items: center;">
                  <div :style="{
                    'width':item.vmsData['width'] + 'px',
                    'height':item.vmsData['height'] + 'px',
                    'color':item.vmsData['font_color'],
                    'font-size':item.vmsData['font_size'] + 'px',
                    'font-family':item.vmsData['font_type'],
                    'background-color':'#000',
                    'position':'relative',
                    }">
                    <span :style="{
                      'position':'absolute',
                      'top':item.vmsData['top'] + 'px',
                      'left':item.vmsData['left'] + 'px',
                    }"
                    style="line-height:1"
                     v-html="item.vmsData['content'].replace(
                              /\n|\r\n/g,
                              '<br>'
                            ).replace(/ /g, ' &nbsp')">
                    </span>
                  </div>
                </div>
              </el-card>
            </div>
            <!-- 广播 -->
            <p v-if="item.deviceType == '22'" style="padding-bottom:15px;">执行文件:</p>
            <div class="templateImg" v-if="item.deviceType == '22'">
              <el-card shadow="always" class="otherBox">
                <img :src="item.deviceIconUrl" />
                <p>{{item.deviceState}}</p>
              </el-card>
            </div>
            <!-- 其他设备 -->
            <p v-if="item.deviceType != '16' && item.deviceType != '36' && item.deviceType != '22'" style="padding-bottom:15px;">执行状态:</p>
            <div class="templateImg" v-if="item.deviceType != '16' && item.deviceType != '36' && item.deviceType != '22'">
              <el-card shadow="always" class="otherBox">
                <img v-for="(items,idx) in item.deviceIconUrl" :key="idx"
                     :src="items"
                />
                <p>{{item.deviceState}}</p>
              </el-card>
            </div>
          </el-card>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          class="submitButton"
          v-show="yjShow == false"
          @click="oneKeyExecute()">
          执 行
        </el-button>
        <el-button @click="oneKeyDialogVisible = false" class="closeButton">取 消</el-button>
      </span>
    </el-dialog>
      <!-- 巡检机器人 -->
      <el-dialog
      class="robotDialog"
      :visible.sync="robotDialogVisible"
      width="770px"
      :close-on-click-modal="false"
      >
        <robot class="comClass robotHtmlBox" ></robot>
        <img
          @click="dialogClose"
          src="../../../assets/cloudControl/closeIcon.png"
          class="closeRobot"
        />
      </el-dialog>
       
    <jointControl ref = "jointControl" :show="visibleSync" :eqIdList="eqIdList"></jointControl>
    <workBench class="workBenchBox" ref="workBenchRef" :robotPositon="robotPositon"/>

    <!-- </el-dialog> -->
  </div>
</template>
<script>
import robot from "@/views/workbench/config/components/robotManagement";
import { intervalTime } from "../../../utils/index.js";
import { mapState } from "vuex";
import { getTunnels,getWorkStagingRobot,listTunnels1 } from "@/api/equipment/tunnel/api.js";
import { laneImage,laneImage2 } from "../../../utils/configData.js";
import { listType } from "@/api/equipment/type/api.js";
import { getDeviceData } from "@/api/workbench/config.js";
import { getEventCamera, getEntranceExitVideo } from "@/api/eventDialog/api.js";
import { videoStreaming } from "@/api/equipment/eqlist/api";
import { listSdEmergencyPer } from "@/api/event/SdEmergencyPer";
// import workBench from "@/views/event/reservePlan/workBench";
import comVideo from "@/views/workbench/config/components/video"; //摄像机弹窗
import comLight from "@/views/workbench/config/components/light"; //各种带单选框的弹窗
import comCovi from "@/views/workbench/config/components/covi"; //covi弹窗
import comBright from "@/views/workbench/config/components/bright"; //亮度检测器等只有基本信息的弹窗
import comWind from "@/views/workbench/config/components/wind"; //风速风向弹窗
import comPressure from "@/views/workbench/config/components/pressure"; //压力表弹窗
import comVehicleDetec from "@/views/workbench/config/components/vehicleDetec"; //微波车检弹窗
import comCallPolice from "@/views/workbench/config/components/callPolice"; //声光报警弹窗 //机器人弹窗
import comData from "@/views/workbench/config/components/data"; //只有数据的弹窗
import comYoudao from "@/views/workbench/config/components/youdao"; //诱导灯弹窗
import comBoard from "@/views/workbench/config/components/board"; //诱导灯弹窗
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
import jointControl from "@/views/event/event/projectionScreen/jointControl.vue";
import workBench from "./workBench.vue";

import {
  listEventType,
  getDisposalRecord,
  getManagementDevice,
} from "@/api/event/eventType";
import {
  listEqTypeState,
  getStateByData,
} from "@/api/equipment/eqTypeState/api";
import {
  dispatchExecuted,
  listEvent,
  eventFlowList,
  getHandle,
  updateHandle,
  getRelation,
  getReserveId,
  getAccidentPoint,
  implementProcess,
  implementPlan,
  performRecovery,
  getSituationUpgrade,
  getReservePlanData,
  updateEvent,
  updateSituationUpgrade,
  getEventInif,
  getAllManagementDevices,
  roBotDong,
} from "@/api/event/event";
// import { listSdEmergencyPer } from "@/api/event/SdEmergencyPer";

export default {
  components: {
    comVideo,
    comLight,
    comCovi,
    comBright,
    comWind,
    comPressure,
    comVehicleDetec,
    comCallPolice,
    robot,
    comData,
    comYoudao,
    comBoard,
    workBench,
    videoPlayer,
    jointControl,
  },
  data() {
    return {
      robotTimer:null,
      tunnelLang:0,
      robotPositon:99,
      workBenchProp:{},
      robotDialogVisible: false,
      clickEqType: "",
      yjShow: true,
      emergencyList: [],
      activeName: "1",
      buttonDisable: true,
      deviceStateName: "",
      oneKeyList: [],
      oneKeyDialogVisible: false,
      eventInfo: {},
      fontAlign: "", //情报板对齐方式
      boxName: "",
      nowData: Date.now(),
      timeData: "",
      deadline4: "",
      processId: "",
      GDeviceData: {},
      rules: {
        remark: [
          { required: true, message: "请输入升级原因", trigger: "blur" },
        ],
        currencyId: [
          { required: true, message: "请选择预案", trigger: "blur" },
        ],
        eventGrade: [
          { required: true, message: "请选择等级", trigger: "blur" },
        ],
      },
      reverse: true,
      disposalRecord: [], //记录
      drawer: false,
      eventGradeList: null,
      dialogVisible: false,
      // 警情升级
      levelForm: {
        typeName: "",
        currencyId: "",
        remark: "",
        eventGrade: "",
        dengji: "",
        yaName: "",
      },
      iconDisabled: false,
      disabledRadio: false,
      assIconUrl: "",
      IssuedItemContent: "",
      IssuedItem: {},
      title: "",
      IssuedDialog: false,
      accLeft: 0,
      accTop: 0,
      tunnelLane: 0,
      eqTypeDialogList: [],
      brandList: [],
      eqInfo: {},
      relationDisabled: false,
      activeMap: 1,
      eqTypeList: [],
      directionList: [],
      eventForm: {},
      timer: null,
      laneUrlList: this.$cache.local.get("navigationBar") == '0'?laneImage:laneImage2,
      eqTypeStateList: null,
      // selectedIconList: [],
      backImg: "",
      eqRecordIcon: require("@/assets/cloudControl/eqRecord.png"),
      incHand1: require("@/assets/cloudControl/incHand1.png"),
      incHand2: require("@/assets/cloudControl/incHand2.png"),
      phoneIcon: require("@/assets/cloudControl/phone2.png"),
      mesIcon: require("@/assets/cloudControl/phone1.png"),
      videoUrl: require("@/assets/Example/v1.mp4"),
      imgUrl: require("@/assets/Example/pic1.jpg"),
      planType: [],
      videoList: [],
      videoForm1: {},
      videoForm2: {},
      videoForm3: {},
      videoForm4: {},
      //存放设备id
      eqIdList: [],

      fromList: [],
      reservePlan: {
        oneWay: "1",
        twoWay: "1",
        plan: "1",
      },
      eventList: [],
      implementList: [],
      zxList: [],
      incHandList: [],
      picWidth: "",
      dragFlag: false,
      mouseLeft: 0,
      mouseTop: 0,
      curX: 0,
      curY: 0,
      ReservePlanList: null, //预案列表
      reserveId: "",
      lineHeight: 0,
      circlePosition: "",
      deviceIconUrl: [],
      visibleSync: false,
    };
  },
  computed: {
    ...mapState({
      deviceStatus: (state) => state.websocket.deviceStatus,
      deviceStatusChangeLog: (state) => state.websocket.deviceStatusChangeLog,
      eventFlow: (state) => state.websocket.eventFlow,
    }),
  },
  watch: {
    deviceStatus(event) {
      console.log(event, "websockt工作台接收实时设备状态数据");
      this.deviceStatusList = event;
    },
    deviceStatusChangeLog(event) {
      for (let item of event) {
        if (this.$route.query.id == item.eventId) {
          this.zxList.unshift(item);
        }
      }
    },
  },
  async created() {
    const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    await this.getEqTypeStateIcon();
    // await this.getTunnelData();
    await this.getDispatchExecuted();
    this.getListEvent();
    this.stateByData();
    this.getEventList();
    loading.close();
    //当前等级
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeList = response.data;
    });
    // this.getDicts("sd_direction_list").then((response) => {
    //   console.log(response.data, "车道方向");
    //   this.directionList = response.data;
    // });
    this.getDicts("sd_emergency_plan_type").then((response) => {
      this.planType = response.data;
    });
    this.getDicts("brand").then((data) => {
      this.brandList = data.data;
    });
    this.getDicts("sd_direction").then((data) => {
      this.directionList = data.data;
    });
    this.getDicts("sd_monitor_state").then((data) => {
      this.eqTypeDialogList = data.data;
    });
    this.getDicts("sd_event_source").then((data) => {
      this.fromList = data.data;
    });
    // 应急人员岗位
    this.getDicts("sd_emergency_post").then((data) => {
      this.emergencyList = data.data;
    });
  },
  mounted() {
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 100);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);
  },
  beforeDestroy(){
    window.clearInterval(this.robotTimer);
    this.robotTimer = null;
  },
  methods: {
    openWorkBench(){
      this.$refs.workBenchRef.init(this.workBenchProp)
    },
    handleTouping() {
      this.$confirm("即将投屏, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // debugger
          this.visibleSync = !this.visibleSync;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    getShow(item, index) {
      if (item.children != undefined) {
        let isShow = item.children.every((items) => {
          return items.eventState == "1";
        });
        return isShow;
      }
    },
    handleClick(tab, event) {
      this.getImplementList(tab.name);
    },
    //请求应急人员数据
    getImplementList(username) {
      let params = { groupName: username, tunnelId: this.eventForm.tunnelId };
      listSdEmergencyPer(params).then((res) => {
        this.implementList = res.rows;
      });
    },
    oneKeyExecute() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      let planId = this.reserveId;
      let eventId = this.$route.query.id;
      implementPlan(planId, eventId)
        .then((res) => {
          loading.close();
          this.$modal.msgSuccess(res.msg);
          this.oneKeyDialogVisible = false;
          this.evtHandle();
        })
        .catch(() => {
          loading.close();
        });
    },
    // 事件处置 一键
    getYiJian(item) {
      let show = item.children.every((items) => {
        return items.eventState == "1";
      });
      this.yjShow = show;
      var that = this;
      let arr = [];
      for (let itm of item.children) {
        arr.push(itm.id);
      }
      this.reserveId = item.reserveId;
      let data = { reserveId: item.reserveId, eventId: that.$route.query.id };
      getAllManagementDevices(data).then((res) => {
        this.oneKeyList = res.data;
        for (let i = 0; i < this.oneKeyList.length; i++) {
          let item = this.oneKeyList[i];
          if (item.deviceType == 16 || item.deviceType == 36) {
            //情报板
            let zxc = item.vmsData["screen_size"].split("*");
            item.vmsData["width"] = zxc[0];
            item.vmsData["height"] = zxc[1];
            let align = item.vmsData["coordinate"];
            item.vmsData["left"] = align.substr(0, 3);
            item.vmsData["top"] = align.substr(3, 6);
          }
        }
        console.log(this.oneKeyList, "this.oneKeyListthis.oneKeyList");
        this.oneKeyDialogVisible = true;
      });
    },
    oneKeyHandleClose() {
      this.oneKeyDialogVisible = false;
    },
    getEventInfo() {
      let data = { id: this.$route.query.id };
      getEventInif(data).then((res) => {
        this.eventInfo = res.data;
      });
    },
    getManagementDevice(item) {
      console.log(item);
      console.log(this.$route.query.id,"aaaaa")
      if (item.eqTypeId == 29) {
        this.robotDialogVisible = true;
        if(item.eventState == "0"){
          roBotDong(this.$route.query.id,item.id).then((res) => {
            this.evtHandle();
          })
        }
        return;
      }
      if (item.eventState != "0" && item.processId) {
        this.buttonDisable = false;
      } else {
        this.buttonDisable = true;
      }
      console.log(this.buttonDisable);
      this.GDeviceData.deviceIconUrl = null;
      this.processId = item.processId;
      this.IssuedItem.id = item.id;
      let params = { id: item.processId };
      item.eventState == "0"
        ? (this.deviceStateName = "未执行")
        : (this.deviceStateName = "已执行");
      //请求详情
      getManagementDevice(params).then((res) => {
        // console.log(res);
        let data = res.data;
        // 广播
        if (data.deviceType == 22) {
          this.boxName = "执行文件";
          this.GDeviceData.deviceList = data.deviceList;
          this.GDeviceData.deviceState = data.lsData;
        } else if (data.deviceType == 16 || data.deviceType == 36) {
          //情报板
          this.boxName = "执行模板";
          this.GDeviceData.deviceList = data.deviceList;
          this.GDeviceData.vmsData = data.vmsData;
          let zxc = data.vmsData["screen_size"].split("*");
          this.GDeviceData.vmsData["width"] = zxc[0];
          this.GDeviceData.vmsData["height"] = zxc[1];
          let align = data.vmsData["coordinate"];
          this.GDeviceData.vmsData["left"] = align.substr(0, 3);
          this.GDeviceData.vmsData["top"] = align.substr(3, 6);
        } else {
          this.boxName = "下发指令";
          this.GDeviceData = data;
          console.log(this.GDeviceData.deviceList);
          // this.deviceIconUrl = this.GDeviceData.deviceIconUrl;
        }
        this.IssuedDialog = true;
      });
    },
    drawerHandleOpen() {
      let data = { eventId: this.$route.query.id };
      getDisposalRecord(data).then((res) => {
        this.disposalRecord = res.rows;
      });
      this.drawer = !this.drawer;
    },
    // 抽屉
    drawerHandleClose(done) {
      this.drawer = false;
    },
    closeDialog() {
      this.dialogVisible = false;
      this.$refs["levelForm"].resetFields();
    },
    // 警情升级根据等级查询预案
    levelChange(value) {
      this.ReservePlanList = [];
      this.levelForm.currencyId = "";
      const param = {
        tunnelId: this.eventForm.tunnelId,
        planTypeId: this.eventForm.eventTypeId,
        direction: this.eventForm.direction,
        eventGrade: value,
      };
      getReservePlanData(param).then((res) => {
        if (res.data.length == 0) {
          this.$modal.msgWarning("该等级下暂无预案");
        }
        this.ReservePlanList = res.data;
      });
    },
    // 警情升级确定按钮
    changeLevel() {
      this.$refs["levelForm"].validate((valid) => {
        if (valid) {
          this.levelForm.id = this.$route.query.id;
          this.levelForm.eventState = "0";
          this.levelForm.eventTypeId = this.eventForm.eventTypeId;
          const param = this.levelForm;
          updateSituationUpgrade(param).then((response) => {
            //调用事件处置接口，刷新数据
            this.evtHandle();
            this.$refs["levelForm"].resetFields();
            this.dialogVisible = false;
            this.$modal.msgSuccess("修改成功");
            this.getListEvent();
          });
        }
      });
    },
    //完结
    over() {
      this.$confirm("此操作将立即完结该事件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: "Loading",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          const param = {
            id: this.eventForm.id,
            eventState: 1,
          };
          updateEvent(param).then((response) => {
            loading.close();
            this.$message({
              type: "success",
              message: "操作成功!",
            });
            this.backSafeWarn();
            //回跳列表页
          });
        })
        .catch(() => {
          loading.close();
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 警情升级
    levelTop() {
      this.dialogVisible = true;
      const param = {
        id: this.eventForm.id,
      };
      getSituationUpgrade(param).then((res) => {
        let data = res.data;
        this.levelForm.dengji = data.eventGrade;
        this.levelForm.typeName = data.eventTypeName;
        this.levelForm.yaName = data.planName;
      });
    },
    handleClose(done) {
      done();
    },
    //右键拖动
    // dragImg(e) {
    //   // console.log(e, "e");
    //   // console.log(
    //   //   this.$refs.dragImgDom.offsetLeft,
    //   //   "this.$refs.dragImgDom.offsetLeft"
    //   // );
    //   if (e.button == 0) {
    //     return;
    //   }
    //   this.dragFlag = true;
    //   this.mouseLeft =
    //     e.clientX - parseInt(this.$refs.dragImgDom.offsetLeft) + 560;
    //   this.mouseTop = e.clientY - parseInt(this.$refs.dragImgDom.offsetTop);
    //   document.onmousemove = (e) => {
    //     if (this.dragFlag) {
    //       this.curX = e.clientX - this.mouseLeft;
    //       this.curY = e.clientY - this.mouseTop;
    //       this.$refs.dragImgDom.style.left = this.curX + "px";
    //       this.$refs.dragImgDom.style.top = this.curY + "px";
    //     }
    //   };
    //   document.onmouseup = () => {
    //     this.dragFlag = false;
    //   };
    // },
    // 预览按钮
    // getPreview(type) {
    //   // 查预案ID
    //   const params = {
    //     tunnelId: this.eventForm.tunnelId,
    //     category: this.reservePlan.oneWay,
    //     controlDirection: type,
    //     direction: this.eventForm.direction,
    //     eventId: this.eventForm.id,
    //     planTypeId: this.eventForm.eventTypeId,
    //   };
    //   getReserveId(params).then((res) => {
    //     console.log(res, "获取的预案id");
    //     var planId = res.data;
    //     this.$nextTick(() => {
    //       this.$refs.workBench.eventId = this.$route.query.id;
    //       this.$refs.workBench.id = planId; //预案ID
    //       this.$refs.workBench.tunnelId = this.eventForm.tunnelId;
    //       this.$refs.workBench.init();
    //     });
    //   });
    //   // this.workbenchOpen = true;
    // },
    // 事件点图片
    getAccIcon() {
      let id = this.eventForm.eventTypeId;
      listEventType({ id }).then((res) => {
        this.assIconUrl = res.rows[0].iconUrl;
      });
    },
    // 事件点
    async getAcc() {
      const params = {
        stakeNum: this.eventForm.stakeNum,
        laneNo: this.eventForm.laneNo,
        tunnelId: this.eventForm.tunnelId,
      };
      /*await getAccidentPoint(params).then((res) => {
        console.log(res, "事件点");
        this.accLeft = res.data["tunnelLeft"] * 100;
        this.accTop = res.data["tunnelTop"] * 100;
        this.getAccIcon();
      });*/
    },

    // 返回安全预警
    backSafeWarn() {
      this.videoForm1.cameraPlayer = false;
      this.videoForm2.cameraPlayer = false;
      this.videoForm3.cameraPlayer = false;
      this.videoForm4.cameraPlayer = false;
      this.$router.push({
        path: "/emergency/safeWarn",
      });
    },
    // 关闭弹窗子组件
    dialogClose() {
      this.eqInfo.clickEqType = 0;
      this.robotDialogVisible = false;
      this.$forceUpdate();
    },
    // 点设备弹窗
    openStateSwitch(item) {
      this.eqInfo = {
        clickEqType: item.eqType,
        equipmentId: item.eqId,
      };
    },

    // 关联事件
    // relation(type) {
    //   const params = {
    //     tunnelId: this.eventForm.tunnelId,
    //     category: this.reservePlan.oneWay,
    //     controlDirection: type,
    //     direction: this.eventForm.direction,
    //     eventId: this.eventForm.id,
    //     planTypeId: this.eventForm.eventTypeId,
    //   };
    //   getRelation(params).then((res) => {
    //     this.$modal.msgSuccess("关联成功");
    //     this.getListEvent();
    //     this.relationDisabled = true;
    //     this.disabledRadio = true;
    //   });
    // },
    // 打开下发事件弹窗
    // openIssuedDialog(item) {

    //   this.title = "警告";
    //   // this.IssuedItem = item;
    // },
    // 关闭下发事件弹窗
    cancelIssuedDialog() {
      this.IssuedDialog = false;
      this.boxName = "";
      this.GDeviceData.deviceIconUrl = null;
    },
    changeIncHand() {
      var that = this;
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      // if (this.IssuedItem.flowPid == "7") {
      let processId = that.processId;
      let eventId = that.$route.query.id;
      implementProcess(processId, eventId)
        .then((response) => {
          loading.close();
          that.$modal.msgSuccess("执行成功");
          this.IssuedDialog = false;
          this.getDispatchExecuted();
          that.evtHandle();
          that.getEventList();
          this.processId = "";
        })
        .catch(() => {
          loading.close();
        });
      // } else if (this.IssuedItem.flowId == "17") {
      //   let eventId = that.$route.query.id;
      //   let handleId = this.IssuedItem.id;
      //   performRecovery(eventId, handleId).then((res) => {
      //     console.log(res, "解除管控");
      //     this.IssuedDialog = false;
      //     this.IssuedItemContent = "";
      //     that.evtHandle();
      //     that.getEventList();
      //   });
      // } else {
      //   const params = {
      //     id: that.$route.query.id,
      //     ids: that.IssuedItem.id,
      //     // remark: this.IssuedItemContent,
      //   };
      //   updateHandle(params).then((res) => {
      //     console.log(res, "单点改状态");
      //     console.log(that.incHandList, "this.incHandList");
      //     that.$modal.msgSuccess("状态修改成功");
      //     this.IssuedDialog = false;
      //     this.IssuedItemContent = "";
      //     that.evtHandle();
      //     that.getEventList();
      //     if (this.IssuedItem.flowId == "18") {
      //       this.iconDisabled = true;
      //     }
      //   });
      // }
    },
    // 事件处置
    async evtHandle() {
      await getHandle({
        id: this.$route.query.id,
        eventTypeId: this.eventForm.eventTypeId,
      }).then((res) => {
        let list = this.handleTree(res.data, "flowId", "flowPid");
        //  for(let item of list){
        //   console.log(item.flowContent.toString().length,"555555555555555")
        //  }
        for (var i = 0; i < list.length; i++) {
          for (var j = i; j < list.length - 1; j++) {
            if (Number(list[i].flowSort) > Number(list[j + 1].flowSort)) {
              const temp = list[i];
              list[i] = list[j + 1];
              list[j + 1] = temp;
            }
          }
        }
        for (var i = 0; i < list.length; i++) {
          if (list[i].children) {
            for (var a = 0; a < list[i].children.length; a++) {
              for (var b = a; b < list[i].children.length - 1; b++) {
                if (
                  Number(list[i].children[a].flowSort) >
                  Number(list[i].children[b + 1].flowSort)
                ) {
                  const temp = list[i].children[a];
                  // console.log(temp,"temptemptemptemptemptemp")
                  list[i].children[a] = list[i].children[b + 1];
                  list[i].children[b + 1] = temp;
                }
              }
            }
          }
        }
        this.incHandList = list;
        this.incHandList.map((item) => {
          if (item.children) {
            item.children.map((res) => {
              // console.log(res);
              // if (res.eqTypeId == 29) {
              //   this.clickEqType = 29;
              // }
            });
          }
          // console.log(item);
        });
        this.$nextTick(() => {
          const incHandContentBox =
            document.querySelector(".incHandContentBox").offsetHeight;
          const classificationBox =
            document.querySelector(".classificationBox").offsetHeight;
          // 线的高度
          this.lineHeight = (incHandContentBox - classificationBox) / 2;
          this.circlePosition = "-" + (this.lineHeight + 10) + "px";
        });
        for (let item of this.incHandList) {
          for (let itm of item.children) {
            if (itm.flowId == 18 && itm.eventState == "1") {
              this.iconDisabled = true;
              this.relationDisabled = true;
              this.disabledRadio = true;
            }
          }
        }
        this.getEventInfo();
        this.$forceUpdate();
      });
    },
    // 查设备状态
    stateByData() {
      const params = {
        isControl: 1,
      };
      getStateByData(params).then((res) => {
        console.log(res.rows, "查设备状态 正红泛绿...");
        this.eqTypeList = res.rows;
      });
    },
    // 事件详情
    async getListEvent() {
      
      if (this.$route.query.id) {
        const param = {
          id: this.$route.query.id,
        };
        await listEvent(param).then((response) => {
          console.log(response.rows,"事件详情")
          this.workBenchProp = {
            tunnelStationName:response.rows[0].tunnelStationName,
            tunnelName:response.rows[0].tunnelName,
            tunnelId:response.rows[0].tunnelId
          }
          if(response.rows[0].tunnelId == "JQ-JiNan-WenZuBei-MJY"){
            listTunnels1().then((res)=>{
              for(let item of res.rows){
                if(item.tunnelId == "JQ-JiNan-WenZuBei-MJY"){
                this.tunnelLang =
                  Number(item.endPileNum) - Number(item.startPileNum) + 10;
                }
              }
            })
            this.robotTimer = setInterval(() => {
              setTimeout(this.getRobot, 0);
            }, 1000 * 5);
          }
          this.eventForm = response.rows[0];
          this.eventForm.iconUrlList = response.rows[0].iconUrlList.splice(
            0,
            4
          );
          setInterval(() => {
            this.deadline4 = intervalTime(this.eventForm.eventTime);
          }, 1000);
          this.getVideoList();
          this.getpersonnelList();
          this.evtHandle();
          // this.getTunnelData();
          setTimeout(() => {
            this.getAcc();
          }, 500);
        });
      }
    },
    async getRobot(){
      const param = {
        deviceId: 7,
      };
      await getWorkStagingRobot(param).then((res) => {
        console.log(res, "机器人");
        this.robotPositon = (
          (Number(res.data.position) / this.tunnelLang) *
          100
        ).toFixed(2) - 1;
      });
    },
    // 左上角视频
    getVideoList() {
      this.videoForm1.cameraPlayer = false;
      this.videoForm2.cameraPlayer = false;
      this.videoForm3.cameraPlayer = false;
      this.videoForm4.cameraPlayer = false;

      console.log(this.eventForm, "this.eventForm");
      // this.videoList = []
      this.eqIdList = [];
      getEntranceExitVideo(
        this.eventForm.tunnelId,
        this.eventForm.direction
      ).then((response) => {
        this.eqIdList.push(response.data[0].inlet);
        videoStreaming(response.data[0].inlet).then((response) => {
          if (response.data) {
            response.data.title = "";
            if (response.code == 200) {
              this.videoForm1 = response.data;
              this.videoForm1.cameraPlayer = true;
            }
          }
        });
        this.eqIdList.push(response.data[0].outlet);
        videoStreaming(response.data[0].outlet).then((response) => {
          if (response.data) {
            response.data.title = "";
            if (response.code == 200) {
              this.videoForm2 = response.data;
              this.videoForm2.cameraPlayer = true;
            }
          }
        });
      });
      setTimeout(() => {
        getEventCamera(
          this.eventForm.tunnelId,
          this.eventForm.stakeNum,
          this.eventForm.direction
        ).then((res) => {
          this.eqIdList.push(res.data[0].eqId);
          videoStreaming(res.data[0].eqId).then((response) => {
            if (response.data) {
              response.data.title = "现场1";
              if (response.code == 200) {
                this.videoForm3 = response.data;
                this.videoForm3.cameraPlayer = true;
              }
            }
          });
          this.eqIdList.push(res.data[1].eqId);
          videoStreaming(res.data[1].eqId).then((response) => {
            if (response.data) {
              response.data.title = "现场2";
              if (response.code == 200) {
                this.videoForm4 = response.data;
                this.videoForm4.cameraPlayer = true;
              }
            }
          });
        });
      }, 500);
    },
    /** 查询应急人员信息列表 */
    async getpersonnelList() {
      // 高速交警默认字典值
      this.getImplementList(1);
    },
    // 切换工作台和3D隧道
    changeActiveMap(type) {
      if (type == 1) {
        this.activeMap = 1;
      } else {
        this.activeMap = 2;
      }
    },
    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.$route.query.id }).then((res) => {
        this.eventList = res.rows;
      });
    },
    //设备执行记录
    getEqType(state, eqType) {
      for (let i = 0; i < this.eqTypeList.length; i++) {
        let item = this.eqTypeList[i];
        if (eqType == item.stateTypeId && Number(item.deviceState) == state) {
          // console.log(item.stateName);
          return item.stateName;
        }
        //  else {
        //   continue;
        // }
      }
    },
    // 查询方向
    getDirection(num, item) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 转事件来源数据
    getFrom(from) {
      for (let item of this.fromList) {
        if (from == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    // 一进页面获取已执行数据
    getDispatchExecuted() {
      dispatchExecuted(this.$route.query.id).then((res) => {
        this.zxList = res.data;
      });
    },
    /* 获取隧道配置信息*/
    // async getTunnelData() {
    //   let tunnelId = this.eventForm.tunnelId;
    //   // var tunnelId = this.eventMsg.tunnelId; //"WLJD-JiNan-YanJiuYuan-FHS";
    //   let that = this;

    //   await getTunnels(tunnelId).then((response) => {
    //     this.tunnelLane = Number(response.data.lane);
    //     let res = response.data.storeConfigure;
    //     //存在配置内容
    //     if (res != null && res != "" && res != undefined) {
    //       res = JSON.parse(res);
    //       let id = res.lane;
    //       for (let i = 0; i < that.laneUrlList.length; i++) {
    //         if (that.laneUrlList[i].id == id) {
    //           that.backImg = that.laneUrlList[i].url;
    //           that.picWidth = that.laneUrlList[i].width;
    //         }
    //       }
    //       listType().then((response) => {
    //         // var arr = [];
    //         for (let i = 0; i < res.eqList.length; i++) {
    //           res.eqList[i].focus = false;
    //           for (let j = 0; j < response.rows.length; j++) {
    //             if (response.rows[j].typeId == res.eqList[i].eqType) {
    //               let iconWidth = Number(response.rows[j].iconWidth);
    //               let iconHeight = Number(response.rows[j].iconHeight);
    //               res.eqList[i].iconWidth = iconWidth;
    //               res.eqList[i].iconHeight = iconHeight;
    //               break;
    //             }
    //           }
    //         }
    //         this.selectedIconList = res.eqList; //这是最终需要挂载到页面上的值
    //         that.getRealTimeData();
    //       });
    //     } else {
    //       //不存在
    //       that.selectedIconList = [];
    //     }
    //   });
    // },
    // getRealTimeData() {
    //   getDeviceData({
    //     tunnelId: this.eventForm.tunnelId,
    //   }).then((response) => {
    //     for (let j = 0; j < this.selectedIconList.length; j++) {
    //       var eqId = this.selectedIconList[j].eqId;
    //       var deviceData = response.data[eqId];
    //       if (deviceData) {
    //         // let type = deviceData.eqType;

    //         // 需要换光标的
    //         for (let k = 0; k < this.eqTypeStateList.length; k++) {
    //           if (
    //             this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
    //           ) {
    //             //无法控制设备状态的设备类型，比如PLC、摄像机
    //             let arr = [
    //               // 5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 31, 32, 33, 35,
    //               5,
    //               14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35, 22,
    //               40, 39, 48, 45,
    //             ];
    //             if (arr.includes(deviceData.eqType)) {
    //               if (
    //                 // 摄像机之类的只有在线 离线 故障图标
    //                 this.eqTypeStateList[k].stateType == "1" &&
    //                 this.eqTypeStateList[k].state == deviceData.eqStatus
    //               ) {
    //                 //取设备监测状态图标
    //                 this.selectedIconList[j].url = this.eqTypeStateList[k].url;
    //                 if (deviceData.eqType == 19) {
    //                   this.selectedIconList[j].num =
    //                     "CO:" +
    //                     parseFloat(deviceData.CO).toFixed(2) +
    //                     "/PPM  VI:" +
    //                     parseFloat(deviceData.VI).toFixed(2) +
    //                     "M";
    //                 } else if (deviceData.eqType == 17) {
    //                   this.selectedIconList[j].num =
    //                     parseFloat(deviceData.FS).toFixed(2) +
    //                     "m/s " +
    //                     deviceData.FX;
    //                 } else if (deviceData.eqType == 5) {
    //                   if (deviceData.DWLD) {
    //                     this.selectedIconList[j].num =
    //                       parseFloat(deviceData.DWLD).toFixed(2) + "lux";
    //                   }
    //                 } else if (deviceData.eqType == 18) {
    //                   if (deviceData.DNLD) {
    //                     this.selectedIconList[j].num =
    //                       parseFloat(deviceData.DNLD).toFixed(2) + "lux";
    //                   }
    //                 }
    //               }
    //             } else {
    //               //可以控制设备状态的设备类型，比如车指
    //               if (deviceData.eqStatus == "1") {
    //                 // 在线
    //                 if (
    //                   // 车指之类的包括正红反绿之类的图标 == 2
    //                   this.eqTypeStateList[k].stateType == "2"
    //                 ) {
    //                   if (this.eqTypeStateList[k].state == deviceData.state) {
    //                     //取设备运行状态图标
    //                     let url = this.eqTypeStateList[k].url;
    //                     this.selectedIconList[j].eqDirection =
    //                       deviceData.eqDirection;
    //                     if (deviceData.eqDirection == "1") {
    //                       //上行车道
    //                       if (url.length > 1) {
    //                         this.selectedIconList[j].url = [url[1], url[0]];
    //                       } else {
    //                         this.selectedIconList[j].url = url;
    //                       }
    //                     } else {
    //                       this.selectedIconList[j].url =
    //                         this.eqTypeStateList[k].url;
    //                     }
    //                   }
    //                 }
    //               } else {
    //                 //如果是离线、故障等状态
    //                 if (
    //                   this.eqTypeStateList[k].stateType == "1" &&
    //                   this.eqTypeStateList[k].state == deviceData.eqStatus
    //                 ) {
    //                   //取设备监测状态图标
    //                   this.selectedIconList[j].url =
    //                     this.eqTypeStateList[k].url;
    //                 }
    //               }
    //             }
    //           }
    //         }
    //       }
    //     }
    //   });
    // },

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[0];
      for (let i = 0; i < this.laneUrlList.length; i++) {
        if (this.laneUrlList[i].num == num) {
          lane = this.laneUrlList[i];
          break;
        }
      }
      return lane;
    },
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        // isControl: 1,
      };
      await listEqTypeState(queryParams).then((response) => {
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    getEqUrl(list) {
      // let that = this;
      // that.eqTypeStateList = [];
      this.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let img = await that.picture(list[i].iFileList[j].url);
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        this.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
      }
    },
    getExecuteResult(num) {
      if (num == "1") {
        return "执行成功";
      } else {
        return "执行失败";
      }
    },
  },
};
</script>

<style scoped lang="scss">
.robotDialog {
  ::v-deep .el-dialog {
    height: 90%;
    border: none;
    background-color: transparent !important;
    .el-dialog__header {
      display: none;
    }
  }
}
.empty-text {
  text-align: center;
  padding: 35px;
}
::v-deep .plan .el-table__body-wrapper {
  width: 100% !important;
}
::v-deep .plan .el-tabs__item {
  padding: 0px 20px !important;
}

::v-deep .yjBox .is-always-shadow .el-card {
  background-color: #012b4e;
}
.dispatchAss .GDeviceBox .el-row .el-col {
  background: #012646;
  margin-bottom: 20px;
}
::v-deep .el-tabs--card > .el-tabs__header .el-tabs__nav {
  border: 0px;
}
::v-deep .el-tabs--card > .el-tabs__header {
  border-bottom: none;
  /* background: rgba(1, 46, 81, 0.7) !important; */
  padding: 10px 0;
  margin: 0 0 5px;
}
.el-tabs--top .el-tabs__item.is-top:nth-child(2),
.el-tabs--top .el-tabs__item.is-bottom:nth-child(2),
.el-tabs--bottom .el-tabs__item.is-top:nth-child(2),
.el-tabs--bottom .el-tabs__item.is-bottom:nth-child(2) {
  padding-left: 0px !important;
}
.theme-dark #app .el-table {
  background-color: transparent;
}
::v-deep .el-table .el-table__header-wrapper th {
  /* background-color: #03659e !important; */
  color: white;
}
.drawerBox:hover {
  cursor: pointer;
}
::v-deep .el-drawer.ltr,
.el-drawer.rtl {
  height: 87%;
  top: 9%;
  bottom: unset;
  border: 1px solid #0661ae;
}
.dieBox::-webkit-scrollbar {
  // display: none;
}
::v-deep .el-table--scrollable-x .el-table__body-wrapper::-webkit-scrollbar {
  // display: none;
}
// ::v-deep .drawerLog .el-drawer.ltr{left: 25%!important;}
::v-deep .el-dialog__title {
  padding-left: 20px;
}
::v-deep .contentList .el-button--success.is-plain {
  background: transparent;
}

::v-deep .drawerLog .el-drawer__header {
  background-color: #012039 !important;
  background-image: url(../../../assets/cloudControl/dialogHeader.png);
  background-repeat: no-repeat;
  background-position-x: right;
  font-size: 14px;
}
::v-deep .drawerLog .el-drawer__container {
  left: -10% !important;
}
::v-deep .drawerLog .el-drawer.ltr {
  height: 65%;
  left: 10%;
  top: 9%;
}
::v-deep .el-statistic .number {
  color: white;
}
::v-deep .drawerLog .el-drawer__body {
  background: rgba(1, 46, 81, 0.7) !important;
}
.sendMsg {
  // width: 60px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  // background-color: #007aff;
}
::v-deep .el-table--scrollable-x .el-table__body-wrapper {
  overflow-x: hidden;
}
::v-deep .el-drawer__body::-webkit-scrollbar {
  // display: none;
}
::v-deep .el-table--scrollable-x .el-table__body-wrapper::-webkit-scrollbar {
  // display: none;
}
// ::v-deep .el-table--scrollable-y .el-table__body-wrapper{
//   overflow-y: unset;
//   overflow:unset;
// }
::v-deep .el-drawer__header {
  color: white;
  margin-bottom: 0px;
  background-image: url(../../../assets/cloudControl/distitlebg.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  height: 4vh;
  font-size: 14px;
}
// ::v-deep .el-table{
//   background-color: rgba(1, 46, 81, 0.7)!important;
// }
/* ::v-deep .el-table tr {
  background: rgba(1, 46, 81, 0.7);
} */
::v-deep
  .el-table--striped
  .el-table__body
  tr.el-table__row--striped
  td.el-table__cell {
  background: rgba(1, 46, 81, 0.7);
}
.heightBox {
  padding-bottom: 2%;
}
.disRightBox > div {
  /* background-color: rgba(1, 46, 81, 0.7) !important; */
}
.videoBox1 {
  /* background-color: rgba(1, 46, 81, 0.7) !important; */
}
.dispatchAss {
  .tunnelBox3 {
    width: 100%;
    height: 100vh;
    .map3D {
      width: 100%;
      height: 100%;
    }
  }
  .workBenchButton{
    position: fixed;
    top: calc(9% + 9px);
    right: 34%;

    // right: 22%;
    cursor: pointer;
    >img{
      width: 47px;
      height: 46px;
    }
  }
  .drawerBox {
    position: fixed;
    top: 9.1%;
    left: 24.5%;
    z-index: 619;
    color: white;
    height: 120px;
    width: 35px;
    background-color: rgba(1, 46, 81, 0.7);
    padding: 8px 8px;
    font-size: 14px;
  }
  .disRightBox {
    position: fixed;
    top: 9vh;
    right: 1vw;
    width: 400px;
    height: 87%;
    .dispatchLeft {
      .video {
        height: 33.3%;
        margin-top: 0px !important;
        border-radius: 0px !important;
        .title {
          /* background-image: url(../../../assets/cloudControl/distitlebg.png);
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-color: rgba(1, 46, 81, 0.7); */
          .touping {
            float: right;
            display: inline-block;
            margin-right: 10px;
            border-radius: 3px;
            padding: 2px 12px;
            line-height: 20px;
            margin-top: 8px;
            color: #fff;
            background: #39adff;
            cursor: pointer;
            font-size: 0.68vw;
          }
        }
        .videoBox1 {
          /* background-color: rgba(1, 46, 81, 0.7); */
          width: 100%;
          height: calc(100% - 40px);
          // word-wrap: break-word;
          // word-break: normal;
          padding: 15px;
          box-sizing: border-box;
          display: grid;
          grid-template-rows: repeat(2, 50%);
          grid-template-columns: repeat(2, 48%);
          grid-column-gap: 4%;
          .videoContent {
            background-image: url(../../../assets/image/videoBg.png);
            background-repeat: no-repeat;
            background-size: 100% 100%;
            width: 100%;
            // height: 48%;
            text-align: center;
            display: inline-block;
            justify-content: center;
            margin-top: 4px;
            position: relative;
            display: flex;
            align-items: center;
            .video {
              height: 90px;
              width: 100%;
              object-fit: fill;
              margin: 0 auto;
            }
            .videoListTitle {
              background-color: rgba(107, 115, 123, 0.4);
              width: 100%;
              height: 18px;
              border-radius: 4px;
              font-size: 13px;
              color: #fff;
              position: absolute;
              top: 15px;
              left: 0px;
            }
          }
        }
      }
      .evtMessage {
        height: 33.3%;
        margin-top: 10px;
        .title {
          /* background-image: url(../../../assets/cloudControl/distitlebg.png);
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-color: rgba(1, 46, 81, 0.7); */
        }
        .evtMessBox {
          display: flex;
          width: 100%;
          height: calc(100% - 40px);
          .evtMessRight {
            width: 50%;
            height: 100%;
            .evtMessVideo {
              background-image: url(../../../assets/image/videoBg.png);
              background-repeat: no-repeat;
              background-size: 100% 100%;
              // height: 50%;
              width: auto;
              display: flex;
              justify-content: center;
              margin-top: 10px;
              padding: 10px 0;
              > video {
                height: 100%;
                width: 90%;
                display: block;
                object-fit: fill;
              }
            }
            .evtMessImg {
              width: 90%;
              height: 38px;
              display: flex;
              justify-content: space-between;
              margin: 10px auto;
              > .el-image {
                width: 35%;
                padding: 10px;
                height: 55px;
                background-image: url(../../../assets/image/videoBg.png);
                background-repeat: no-repeat;
                background-size: 100% 100%;
                ::v-deep .el-image__error {
                  font-size: 12px;
                }
              }
            }
            .evtMessTarget {
              font-size: 14px;
              display: flex;
              > div:nth-of-type(1) {
                color: #008aff;
                width: 80px;
                margin-left: 14px;
              }
              > div:nth-of-type(2) {
                // color: #fff;
                height: 40px;
                overflow: auto;
              }
            }
          }
          .evtMessLeft {
            width: 50%;
            height: 100%;
            > div {
              display: flex;
              font-size: 14px;
              margin-top: 13px;

              > div:nth-of-type(1) {
                color: #008aff;
                width: 90px;
                margin-left: 20px;
              }
              > div:nth-of-type(2) {
                color: #fff;
              }
            }
          }
        }
      }
      .plan {
        height: 33.3%;
        margin-top: 10px;
        .title {
          /* background-image: url(../../../assets/cloudControl/distitlebg.png);
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-color: rgba(1, 46, 81, 0.7); */
        }
        ::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
          border-bottom-color: transparent !important;
        }
        ::v-deep .el-tabs--card > .el-tabs__header .el-tabs__item {
          height: 18px;
          line-height: 18px;
        }
        // .planBox1 {
        //   width: 100%;
        //   height: calc(100% - 40px);
        //   display: flex;
        //   .planLeft {
        //     width: 50%;
        //     height: 100%;

        //     .oneWayTraffic,
        //     .twoWayTraffic {
        //       width: 100%;
        //       height: 50%;
        //       font-size: 14px;
        //       // color: #fff;
        //       padding-left: 20px;
        //       padding-top: 5px;
        //       > div:nth-of-type(2) {
        //         div {
        //           margin-right: 10px;
        //         }
        //       }
        //       > div:nth-of-type(3) {
        //         float: right;
        //         width: 160px;
        //         display: flex;
        //         justify-content: space-between;
        //         height: 22px;
        //         color: white;
        //         margin-top: 20px;
        //         margin-right: 20px;
        //         > div {
        //           text-align: center;
        //           padding: 0 15px;
        //           border-radius: 15px;
        //           line-height: 22px;
        //           cursor: pointer;
        //         }
        //         > div:nth-of-type(1) {
        //           background: #d8d8d8
        //             linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        //         }
        //         > div:nth-of-type(2) {
        //           background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
        //         }
        //       }
        //     }
        //     .twoWayTraffic {
        //       border-top: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        //     }
        //   }
        //   .planRight {
        //     width: 50%;
        //     height: 100%;
        //     // border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        //     font-size: 14px;
        //     // color: #fff;
        //     padding: 5px 20px 0px;
        //     > div:nth-of-type(2) {
        //       display: flex;
        //       justify-content: space-between;
        //       width: 100%;
        //       margin: 6px 0;
        //       > div {
        //         color: #008aff;
        //       }
        //       > div:nth-of-type(2n) {
        //         color: #fff;
        //       }
        //     }
        //     > div:nth-of-type(4) {
        //       float: right;
        //       width: 160px;
        //       display: flex;
        //       justify-content: space-between;
        //       height: 22px;
        //       color: white;
        //       margin-top: 20px;
        //       > div {
        //         text-align: center;
        //         padding: 0 15px;
        //         border-radius: 15px;
        //         line-height: 22px;
        //       }
        //       > div:nth-of-type(1) {
        //         background: #d8d8d8
        //           linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        //       }
        //       > div:nth-of-type(2) {
        //         background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
        //       }
        //     }
        //   }
        //   .el-radio--medium.is-bordered {
        //     padding: 0px 10px;
        //     border-radius: 4px;
        //     height: 30px;
        //     width: 106px;
        //     margin-top: 4px;
        //     line-height: 27px;
        //     margin-right: 0px;
        //     // background: linear-gradient(180deg, #002847 0%, #00325e 100%);
        //     border-radius: 2px;
        //     // border: 1px solid #005d89;
        //   }
        //   // .el-radio {
        //   //   color: white;
        //   // }
        // }
      }
    }
  }
  // ::v-deep .yjBox .el-dialog__header{
  //   background-image: url(../../../assets/cloudControl/dialogHeader.png)!important;
  //   background-repeat: no-repeat!important;
  //   background-position-x: right!important;
  // }
  // ::v-deep .IssuedDialog .el-dialog__header{
  //   background-image: url(../../../assets/cloudControl/distitlebg.png)!important;
  //   background-repeat: no-repeat!important;
  //   background-size: 100% 100%!important;
  // }
  // ::v-deep .jingqing{
  // .el-dialog__header{
  //   background-image: url(../../../assets/cloudControl/distitlebg.png);
  //   background-repeat: no-repeat;
  //   background-size: 100% 100%;
  // }
  // }
  // }
  .disLeftBox {
    position: fixed;
    top: 9vh;
    left: 1vw;
    width: 450px;
    height: 87%;

    background-repeat: no-repeat;
    background-size: 100% 100%;
    .IncHand {
      /* background-color: rgba(1, 46, 81, 0.7); */
      height: 100%;
      box-sizing: border-box;
      /* border: 1px solid #0661ae; */
      .title {
        font-size: 16px;
        /* background-image: url(../../../assets/cloudControl/distitlebg.png);
        background-repeat: no-repeat;
        background-size: 100% 100%; */
        span {
          font-size: 13px;
          color: #93a8b9;
          padding-left: 15px;
        }
      }
      .incHandBox {
        height: calc(100% - 40px);
        // overflow: hidden;
        padding: 15px;
        .GTop {
          display: flex;
          justify-content: space-around;
          align-items: center;
          margin-bottom: 20px;
          .GT_one {
            background-image: url(../../../assets/image/leftBg.png);
            // border-top: 4px solid #0175a9;
            // border-bottom: 1px solid #0175a9;
            background-color: #013143;
            color: white;
            width: 41%;
            height: 40px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .one_box {
            display: flex;
            justify-content: space-around;
            align-items: center;
            p {
              text-align: center;
              font-size: 12px;
            }
            span {
              display: block;
              width: 30%;
              font-size: 12px;
              text-align: center;
              background-color: #063f39;
              color: #01c30b;
              padding: 4px 0;
              box-sizing: border-box;
            }
          }
          .two_box {
            display: flex;
            justify-content: space-evenly;
            align-items: center;
            font-size: 12px;
          }
        }
        .incHandContent {
          display: flex;
          // color: white;
          font-size: 12px;
          padding: 0 10px;
          color: white;
          align-items: center;
          margin-bottom: 35px;
          .classification {
            position: relative;
            .menuBox {
              border: 1px solid rgba(57, 173, 255, 0.6);
              display: flex;
              flex-flow: column;
              justify-content: center;
              align-items: center;
            }
            .dashed {
              position: absolute;
              display: flex;
              justify-content: center;
              align-items: center;
              flex-flow: column;
              width: 50px;
              top: 80px;
              p {
                height: 30px;
                border-left: 1px dashed #39adff;
              }
              .circle {
                display: block;
                width: 10px;
                height: 10px;
                border: 1px solid #39adff;
                border-radius: 6px;
              }
            }
            .topDashed {
              position: absolute;
              display: flex;
              justify-content: center;
              align-items: center;
              flex-flow: column;
              width: 50px;
              top: -40px;
              p {
                height: 30px;
                border-left: 1px dashed #39adff;
              }
              .topCircle {
                display: block;
                width: 10px;
                height: 10px;
                border: 1px solid #39adff;
                border-radius: 6px;
              }
            }
            .type {
              width: 65px;
              height: 50px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              text-align: center;
            }
            .yijian {
              color: white;
              width: 40px;
              background-color: #df8600;
              border-radius: 3px;
              // background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
              // border: 1px solid #39adff;
              // padding: 10px;
              text-align: center;
              margin-bottom: 5px;
            }
          }

          .heng1 {
            width: 20px;
            height: 1px;
            border-top: solid 1px #39adff;
          }
          .shu {
            width: 7px;
            position: relative;
            border: 1px solid #39adff;
            border-right: 0px;
            border-top-left-radius: 37px;
            border-bottom-left-radius: 37px;
          }
          .contentList {
            display: block;
            margin-top: 4px;
            line-height: 40px;
            padding: 10px;
            // background: rgba($color: #084e84, $alpha: 0.6);
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
          .contentList:nth-of-type(1) {
            margin-top: 0;
          }
        }
        .rightButton {
          border-top: 2px solid #0083ff;
          display: flex;
          justify-content: space-around;
          margin-top: 5%;
          padding-top: 5%;
        }
      }
    }
  }
  .title {
    width: 100%;
    height: 40px;
    border-bottom: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
    color: white;
    line-height: 40px;
    padding-left: 16px;
    font-size: 16px;
    margin: 0;
    text-align: left;
  }
  .el-row {
    height: 100%;
    .el-col {
      height: 100%;
    }
  }
}
.dispatchLeft,
.dispatchRight {
  height: 100%;
}
.dispatchLeft {
  > div {
    width: 100%;
    /* background-color: transparent; */
  }
}
// .dispatchRight {
//   margin-left: 10px;
//   .workBenchBox {
//     width: 100%;
//     height: 59%;
//     display: flex;
//     .tunnelBox1 {
//       width: 95%;
//       height: 100%;
//       .tunnelMap {
//         height: 100%;
//         width: 100%;
//         position: relative;
//         margin-bottom: 10px;
//         // padding-bottom: 10px;
//         .back-img {
//           // width: 100% !important;
//           height: 502px !important;
//           position: absolute;
//         }
//         .wrapper {
//           height: 100%;
//           width: 100%;
//           position: absolute;
//           z-index: 3;
//           top: 0px;
//           left: 0px;
//           .icon-box {
//             position: absolute;
//             display: flex;
//             flex-direction: column;
//             // align-items: center;
//             width: 30px !important;
//           }
//         }
//         .accBox {
//           width: 100%;
//           height: 100%;
//           position: relative;
//           .accTop {
//             position: absolute;
//             width: calc(100% - 200px);
//             height: 160px;
//             top: 60px;
//             left: 100px;
//           }
//           .accBottom {
//             position: absolute;
//             width: calc(100% - 200px);
//             height: 160px;
//             top: 270px;
//             left: 100px;
//           }
//           .accPoint {
//             border-radius: 50px;
//             position: absolute;
//             // border: solid 1px red;
//             display: flex;
//             justify-content: center;
//             align-items: center;
//           }
//         }
//       }
//     }
//     .tunnelBox2 {
//       width: 5%;
//       position: relative;
//       > div:nth-of-type(1) {
//         position: absolute;
//         top: 0;
//         left: 20px;
//         width: 48px;
//         height: 48px;
//       }
//       > div:nth-of-type(2) {
//         position: absolute;
//         bottom: 0;
//         left: 20px;
//         width: 48px;
//         height: 150px;
//       }
//     }
//   }
//   .rightBottom {
//     width: 100%;
//     height: calc(41% - 10px);
//     margin-top: 10px;
//     display: flex;
//     .evtManagement {
//       width: calc(67% - 10px);
//       height: 100%;
//       display: flex;
//       // background: #012e51;
//       .IncHand {
//         width: 60%;
//         height: 100%;
//       }
//       .DisRecords {
//         width: 40%;
//         height: 100%;
//         // border-left: solid 1px #E0E7ED;
//         overflow: hidden;
//       }
//     }
//     .implement1 {
//       width: 33%;
//       height: 100%;
//       margin-left: 10px;
//       .phone {
//         width: 100%;
//         height: calc(50% - 5px);
//         .phoneTable {
//           background: transparent !important;
//           padding: 10px;

//           img {
//             margin-left: 10px;
//           }
//         }
//       }
//       .eqRecord {
//         width: 100%;
//         height: 100%;
//         .eqRecordBox {
//           height: calc(100% - 40px);
//           overflow: auto;
//           .implementContent {
//             width: 100%;
//             height: 50px;
//             padding-right: 10px;
//             .implementIcon {
//               width: 10px;
//               height: 84%;
//               text-align: center;
//               padding-top: 11px;
//               margin-left: 3%;
//               margin-right: 3%;
//             }
//             .contentBox {
//               display: inline-block;
//               height: 100%;
//               width: 90%;
//               font-size: 12px;
//               // color:#fff;
//               .row1 {
//                 display: flex;
//                 justify-content: space-between;
//                 align-items: center;
//                 height: 30px;
//                 > div:nth-of-type(1) {
//                   // color: #00A0FF;
//                 }
//                 > div:nth-of-type(2) {
//                   // color: #999999;
//                 }
//               }
//               .row2 {
//                 display: flex;
//                 align-items: center;
//                 height: 20px;
//                 // color: #333;
//               }
//             }
//           }
//         }
//       }
//     }
//   }
// }
// 修改封装样式
::v-deep .el-timeline {
  padding: 10px;
  .el-timeline-item {
    padding-bottom: 0px !important;
    // 竖线
    .el-timeline-item__tail {
      transform: translateY(40px);
      border-left: 2px dashed #97a8ae;
    }
    .el-timeline-item__node--normal {
      top: 32px !important;
      background: #3bd1fe;
      border: 1px solid #ffffff;
    }
    .el-timeline-item__wrapper {
      margin-right: 8px;
      padding-left: 20px;
      .el-timeline-item__content {
        padding: 10px;
        // color: white;
        // background: rgba($color: #084e84, $alpha: 0.6);
        font-size: 12px;
        > div:nth-of-type(1) {
          text-align: left;
          width: 100%;
        }
        > div:nth-of-type(2) {
          text-align: right;
          width: 100%;
        }
      }
    }
  }
}
::v-deep .el-table::before {
  height: 0px !important;
}
::v-deep .el-table .el-table__cell {
  height: 35px !important;
  border-bottom: 1px solid #03659e !important;
  /* border-left: 1px solid #03659e !important; */
}
::v-deep ::-webkit-scrollbar {
  // width: 0px;
}
// ::v-deep .phoneTable tr {
//   background: #12578f !important;
// }
// ::v-deep .phoneTable tr:nth-of-type(2n) {
//   background: #165484 !important;
// }
::v-deep .el-radio__inner::after {
  width: 12px;
  height: 12px;
  background: #008aff;
}
::v-deep .el-dialog__header {
  padding: 0 !important;
}
.workbench-dialog {
  .el-dialog__header {
    padding: 0 !important;
  }
}
.GDeviceBox {
  // p{padding:0 15px 15px;}
  .el-row {
    padding: 10px 0px;
  }
  ::v-deep .el-table__row td {
    padding: 10px 0 !important;
    color: white;
    /* border-bottom: 1px solid #03659e !important;
    border-left: 1px solid #03659e !important; */
  }
  ::v-deep .el-table__row td:last-of-type {
    /* border-right: 1px solid #03659e !important; */
  }
  .GDeviceCard {
    background-color: #05213e;
    margin-bottom: 10px;
  }
}
::v-deep .otherBox .el-card__body {
  display: flex;
  align-items: center;
  p {
    padding: 0 15px;
  }
}
.robotHtmlBox {
  width: 770px !important;
  height: 100%;
  position: absolute;
  left: 0% !important;
  top: 0;
  z-index: 96659;
  // background: #071727;
  pointer-events: auto !important;
  // border-left: 1px solid rgba(1, 152, 255, 0.8);
  // border-right: 1px solid rgba(1, 152, 255, 0.8);
  // border-bottom: 2px solid rgba(1, 152, 255, 0.8);
}
.comClass {
  position: absolute;
  top: 1.4%;
  left: 38.8%;
  pointer-events: none;
}
.closeRobot {
  position: absolute;
  top: 23px;
  left: 95%;
  z-index: 96659;
  cursor: pointer;
  width: 13px;
}
</style>
<style lang="scss">
.IssuedDialog {
  .el-dialog {
    margin-top: 15vh !important;
  }
  .el-dialog__body {
    padding-top: 20px !important;
  }
  .IssuedButton1 {
    width: 60px;
    height: 20px;
    border-radius: 15px;
    background: #d8d8d8 linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
    text-align: center;
    line-height: 20px;
    color: #fff;
    margin: 10px;
    cursor: pointer;
  }
  .IssuedButton2 {
    width: 60px;
    height: 20px;
    border-radius: 15px;
    background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
    text-align: center;
    line-height: 20px;
    color: #fff;
    margin: 10px;
    cursor: pointer;
  }
}
.disRightBox {
  .title span {
    font-size: 13px;
    color: #93a8b9;
    padding-left: 15px;
  }
}
</style>
