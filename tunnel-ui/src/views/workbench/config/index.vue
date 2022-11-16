<template>
  <div class="app-container">
    <div
      class="my-back"
      :style="{ height: 'calc(100vh - (' + navigationHeight + 'px))' }"
    >
      <div class="header workbench-header">
        <el-row
          class="menu-b"
          style="display: flex; align-items: center"
          :style="isManagementStation ? 'padding-left:100px;' : ''"
        >
          <el-cascader
            v-model="tunnelQueryParams.deptId"
            :options="siteList"
            :props="siteProps"
            :show-all-levels="false"
            @change="changeSite"
            placeholder="请选择"
            size="mini"
            class="siteClass"
            popper-class="popper-class-site"
            v-show="!isManagementStation"
          />
          <el-button-group
            class="menu-button-group"
            style="margin-left: 3px"
            :style="isManagementStation ? 'display: flex;' : ''"
          >
            <el-tooltip
              class="item"
              popper-class="wb-tip"
              v-for="(item, index) in tunnelList"
              :key="index"
              effect="dark"
              :content="item.tunnelLength"
              placement="top-start"
            >
              <el-button
                type="info"
                size="mini"
                @click="setTunnel(item, index)"
                :class="index == buttonIndex ? 'tunnelBtnStyle' : ''"
                style="
                  display: flex;
                  justify-content: center;
                  height: 32px;
                  line-height: 20px;
                  font-size: 15px;
                "
              >
                <div>{{ item.tunnelName }}</div>
              </el-button>
            </el-tooltip>
          </el-button-group>
        </el-row>
        <div class="flex-row" style="z-index: 8">
          <div class="display-box zoomClass">
            <el-input
              placeholder="请输入内容"
              v-model="screenEqName"
              class="input-with-select"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="screenEqNameButton"
              ></el-button>
            </el-input>
          </div>
          <div class="display-box zoomClass">
            <p class="zoom-title" style="font-size: 14px">缩放：</p>
            <el-input-number
              v-model="zoom"
              :step="10"
              :min="60"
              :max="140"
              step-strictly
              @change="zoomChange"
            >
              {{ zoom + "%" }}
            </el-input-number>
          </div>
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px">
              {{ displayNumb == 0 ? "桩号开" : "桩号关" }}
            </p>
            <el-switch v-model="displayNumb" class="switchStyle"></el-switch>
          </div> -->
          <div class="display-box">
            <p class="zoom-title" style="font-size: 14px">
              {{ zoomSwitch == 0 ? "缩放开" : "缩放关" }}
            </p>
            <el-switch
              v-model="zoomSwitch"
              class="switchStyle"
              @change="zoomSwitchChange"
            ></el-switch>
          </div>
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px;">{{displayThumbnail == 0 ? '缩略图开' : '缩略图关'}}</p>
            <el-switch v-model="displayThumbnail" active-color="#6ea2ec" inactive-color="#8a8c8e" @change="changeThumbnail"></el-switch>
          </div> -->
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px;">统计图表</p>
            <el-switch v-model="displayThumbnail" @change="changeThumbnail"></el-switch>
          </div> -->
          <!-- <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-s-operation"
            @click="strategyPage"
          >
            控制策略
          </el-button> -->
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-files"
            @click="batchManage"
            v-if="batchManageType == 1"
          >
            批量操作
          </el-button>
          <div v-if="batchManageType == 2" class="batchManageButton">
            <div @click="closeBatchManageDialog">取消</div>
            <div @click="implementBatchManage">执行</div>
          </div>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-top"
            @click="iconExplain"
          >
            图标含义
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-tickets"
            @click="operationLogPage"
          >
            操作日志
          </el-button>
        </div>
      </div>
      <div class="vehicleLane">
        <div
          class="content"
          ref="divRoller"
          @wheel.prevent="handleTableWheel"
          @contextmenu.prevent
          style="position: relative; left: 2%"
        >
          <!-- :class="topNav?'contentTopNav':'contentLeftNav'" -->
          <!-- <div class="tunnelBox" :style="{ width: currentTunnel.lane.width + 80 + 'px' }" style="border: solid 1px yellow;"> -->

          <div
            class="workbench-content"
            @mousedown="dragImg"
            ref="dragImgDom"
            @contextmenu.prevent
          >
            <!--画布区域-->
            <div>
              <el-row
                class="config-img-area"
                :style="{ width: currentTunnel.lane.width + 'px' }"
              >
                <el-image
                  class="back-img"
                  :src="currentTunnel.lane.url"
                  :style="{ width: currentTunnel.lane.width + 'px' }"
                ></el-image>
                <div class="carBox" v-show="carShow">
                  <span
                    v-for="item in carList"
                    :key="item.id"
                    :style="{
                      left: item.left,
                      top: item.top,
                      background: item.background,
                    }"
                  ></span>
                </div>
                <div
                  class="wrapper"
                  id="eq-wrapper"
                  @mousemove="mouseoversImage"
                  @mouseleave="mouseleaveImage"
                >
                  <!-- <div
                  class="wrapper"
                  id="eq-wrapper"
                  @mousedown="dian"
                  @mousemove="yi"
                  @mouseup="li"
                  @mouseover="mouseoversImage"
                  @mouseleave="mouseleaveImage"
                > -->
                  <!-- 鼠标移动时产生的蓝框 -->
                  <div
                    id="container"
                    v-if="move == true"
                    :style="{
                      backgroundColor: back,
                      height: h + 'px',
                      width: w + 'px',
                      position: 'absolute',
                      left: left - 48 + 'px',
                      top: top - 30 + 'px',
                      opacity: 0.5,
                      border: len + 'px dashed #176CBF',
                      'z-index': '1000',
                      'pointer-events': 'none',
                    }"
                  ></div>
                  <template>
                    <div
                      style="
                        width: 80%;
                        height: 15px;
                        border-bottom: 4px #ccc solid;
                        position: relative;
                        top: 242px;
                        left: 166px;
                      "
                      v-show="robotShow"
                      @click="clickRobot"
                    >
                      <img
                        src="../../../assets/logo/equipment_log/robot_zaixian.png"
                        class="robotAnimation"
                      />
                    </div>
                  </template>
                  <!-- 设备图标-->
                  <div
                    class="icon-box mouseHover"
                    v-for="(item, index) in selectedIconList"
                    :key="index"
                    :style="{
                      left: item.position.left + 'px',
                      top: item.position.top + 'px',
                      'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                    }"
                    :class="
                      item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                        ? 'light-' + item.position.left
                        : ''
                    "
                    @click="openStateSwitch(item)"
                    @mousemove="openTooltip(item, index)"
                    @mouseleave="closeTooltip(item)"
                  >
                    <!-- 设备图标上提示文字 -->
                    <div
                      v-if="item.eqName == screenEqName"
                      class="screenEqNameBox"
                    >
                      {{ screenEqName }}
                    </div>
                    <div v-if="item.textFalse" class="textFalseBox">
                      请选择同种设备
                    </div>
                    <!-- <div class="tooltip" v-if="showTooltipIndex == index && showTooltip">{{ sensorContent(item) }}</div> -->

                    <el-tooltip
                      effect="dark"
                      :content="sensorContent(item)"
                      placement="right"
                      :title="item.pile"
                      :disabled="sensorDisabledTwo(item)"
                      style="position: relative; top: 0px; left: 0px"
                      popper-class="tipCase"
                    >
                      <!-- 巡检机器人 -->

                      <div
                        v-show="
                          (item.eqType != 7 &&
                            item.eqType != 16 &&
                            item.eqType != 15 &&
                            item.eqType != 8 &&
                            item.eqType != 9 &&
                            item.display == true) ||
                          ((item.eqType == 7 ||
                            item.eqType == 8 ||
                            item.eqType == 9 ||
                            item.eqType == 21) &&
                            item.display == true &&
                            lightSwitch == 1)
                        "
                        :class="{ focus: item.focus }"
                      >
                        <img
                          v-for="(url, indexs) in item.url"
                          style="position: absolute"
                          :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth"
                          :height="item.iconHeight"
                          :key="item.eqId + indexs"
                          :src="url"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        />
                        <!-- 调光数值 -->
                        <label
                          style="
                            color: yellow;
                            position: absolute;
                            left: 30px;
                            bottom: 2px;
                            pointer-events: none;
                          "
                          v-if="item.eqType == 21"
                          >{{ item.lightValue }}</label
                        >
                        <!-- CO/VI -->
                        <label
                          style="
                            font-size: 12px;
                            position: absolute;
                            color: #79e0a9;
                            text-decoration: underline;
                            padding-left: 5px;
                            width: 100px;
                            text-align: left;
                          "
                          v-if="item.eqType == 19"
                        >
                          {{ item.num }}
                          <!-- <label v-if="item.eqType == 19" style="font-size:14px;">ppm</label> -->
                          <!-- <label v-if="item.eqType == 15" style="font-size:14px;">x10-3m<sup>-1</sup></label>-->
                        </label>
                        <!-- 风速风向 -->
                        <label
                          style="
                            font-size: 14px;
                            position: absolute;
                            text-decoration: underline;
                            color: #79e0a9;
                            padding-left: 5px;
                            width: 100px;
                            text-align: left;
                          "
                          v-if="item.eqType == 17"
                        >
                          {{ item.num }}
                          <!-- <label v-if="item.eqType == 16" style="font-size:14px;">m/s</label> -->
                        </label>
                        <!-- 洞内洞外 -->
                        <label
                          style="
                            font-size: 14px;
                            position: absolute;
                            color: #f2a520;
                            padding-left: 5px;
                            width: 100px;
                            text-align: left;
                            transform: translateY(5px);
                          "
                          v-if="item.eqType == 5 || item.eqType == 18"
                        >
                          {{ item.num }}
                        </label>
                      </div>
                    </el-tooltip>
                    <!-- 桩号 -->
                    <input
                      :class="[
                        item.eqType == 7 ||
                        item.eqType == 8 ||
                        item.eqType == 9 ||
                        item.eqType == 21
                          ? 's-config-img-input'
                          : 'config-img-input',
                      ]"
                      v-if="
                        (item.display == true &&
                          displayNumb == true &&
                          item.eqType != 7 &&
                          item.eqType != 8 &&
                          item.eqType != 9 &&
                          item.eqType != 21) ||
                        ((item.eqType == 7 ||
                          item.eqType == 8 ||
                          item.eqType == 9 ||
                          item.eqType == 21) &&
                          item.display == true &&
                          lightSwitch == 1 &&
                          displayNumb == true)
                      "
                      v-show="item.eqType || item.eqType == 0"
                      type="text"
                      v-model="item.pile"
                      disabled="true"
                      style="color: #055270; margin-left: -20px"
                    />
                    <div v-else style="width: 80px"></div>
                  </div>
                </div>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <div class="siblings">
        <div class="eqTypeListClass">
          <div
            type="info"
            size="small"
            v-for="(item, index) in dictList"
            :key="index"
            :label="item.label"
            :value="index"
            @click="displayControl(index, item.label)"
            class="leftButtonS"
            :style="topNav ? 'width:125px' : 'width:100px'"
          >
            <div>{{ item.label }}</div>
          </div>
        </div>

        <!-- 右侧侧窗 -->
        <div
          style="
            position: absolute;
            display: flex;
            justify-content: space-between;
            flex-direction: column;
            height: 100%;
          "
          :class="topNav ? 'topNavRightDeawer' : 'leftNavRightDeawer'"
        >
          <div class="indicatorLight" @click="isDrawerA()">
            <i class="el-icon-caret-left"></i>车道控制模块
          </div>
          <!-- 定时控制模块 -->
          <div class="brightnessControl" @click="isDrawerB()">
            <i class="el-icon-caret-left"></i>分时控制模块
          </div>
          <div class="triggerControl" @click="isDrawerC()">
            <i class="el-icon-caret-left"></i>触发控制模块
          </div>
        </div>

        <!-- 一键车道控制模块 -->
        <el-drawer
          title="车道指示灯-车道控制"
          :visible.sync="drawerA"
          :modal="false"
          :append-to-body="true"
          class="drawerTop"
        >
          <div style="width: 100%; height: 100%; position: relative">
            <div class="jianbianLine"></div>
            <div class="chezhiDrawerDirection">
              {{ directionList[0].dictLabel }}
            </div>
            <div class="chezhiDrawerInfo">
              <div class="chezhiName">车道:</div>
              <el-select
                v-model="chezhiForm0.lane"
                size="small"
                multiple
                collapse-tags
                class="chezhiLaneSelect"
              >
                <el-option
                  v-for="item in chezhiLaneList"
                  :key="item.laneId"
                  :label="item.laneName"
                  :value="item.laneId"
                />
              </el-select>
              <div class="chezhiName">状态:</div>
              <el-select
                v-model="chezhiForm0.state"
                size="small"
                class="chezhiStateSelect"
              >
                <el-option
                  v-for="item in chezhiStateList"
                  :key="item.Id"
                  :value="item.deviceState"
                  :label="item.stateName"
                >
                  <div style="display: flex; align-items: center">
                    <el-image
                      :src="item.url[0]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <el-image
                      :src="item.url[1]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <div style="margin-left: 4px">{{ item.stateName }}</div>
                  </div>
                </el-option>
              </el-select>
              <el-button
                class="chezhiControlButton"
                @click="chezhiControl(0)"
                :disabled="chezhiDisabled"
              >
                控制
              </el-button>
            </div>

            <div class="chezhiDrawerDirection">
              {{ directionList[1].dictLabel }}
            </div>
            <div class="chezhiDrawerInfo">
              <div class="chezhiName">车道:</div>
              <el-select
                v-model="chezhiForm1.lane"
                size="small"
                multiple
                collapse-tags
                class="chezhiLaneSelect"
              >
                <el-option
                  v-for="item in chezhiLaneList"
                  :key="item.laneId"
                  :label="item.laneName"
                  :value="item.laneId"
                />
              </el-select>
              <div class="chezhiName">状态:</div>
              <el-select
                v-model="chezhiForm1.state"
                size="small"
                class="chezhiStateSelect"
              >
                <el-option
                  v-for="item in chezhiStateList"
                  :key="item.Id"
                  :value="item.deviceState"
                  :label="item.stateName"
                >
                  <div style="display: flex; align-items: center">
                    <el-image
                      :src="item.url[0]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <el-image
                      :src="item.url[1]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <div style="margin-left: 4px">{{ item.stateName }}</div>
                  </div>
                </el-option>
              </el-select>
              <el-button
                class="chezhiControlButton"
                @click="chezhiControl(1)"
                :disabled="chezhiDisabled"
              >
                控制
              </el-button>
            </div>
          </div>
        </el-drawer>
        <el-drawer
          title="分时控制模块"
          :visible.sync="drawerB"
          :modal="false"
          :append-to-body="true"
          class="drawerCenter"
        >
          <div
            v-for="(item, index) in timStrategyList"
            :key="index"
            style="width: 100%"
          >
            <div class="ledLighting">
              <span>{{ item.strategy_name }} </span>
              <el-switch
                v-model="item.strategy_state"
                active-value="0"
                inactive-value="1"
                @change="timStrategySwitch(item)"
              >
              </el-switch>
            </div>
            <div class="Time">
              <div class="timeStart">
                <span class="setTime">开启时间：</span>
                <el-time-picker
                  v-model="item.arr[0]"
                  size="mini"
                  :clearable="false"
                  value-format="HH:mm:ss"
                >
                </el-time-picker>
              </div>
              <div class="timeEnd">
                <span class="setTime">关闭时间：</span>
                <el-time-picker
                  v-model="item.arr[1]"
                  size="mini"
                  :clearable="false"
                  value-format="HH:mm:ss"
                >
                </el-time-picker>
                <el-button
                  type="primary"
                  size="mini"
                  class="handleLightClass"
                  @click="timingStrategy(item)"
                  >确定
                </el-button>
              </div>
            </div>
          </div>
        </el-drawer>
        <!-- 触发控制模块 -->
        <el-drawer
          title="触发控制"
          :visible.sync="drawerCVisible"
          :modal="false"
          :append-to-body="true"
          class="drawerBottom"
        >
          <div
            style="
              height: 150px;
              overflowy: auto;
              padding: 5px;
              padding-left: 10px;
            "
          >
            <div
              style="
                border-bottom: 1px solid rgba(224, 231, 237, 0.2);
                color: #00c2ff;
              "
            >
              <span style="padding-left: 5px">预警类型</span>
              <span style="padding-left: 28px; line-height: 40px">触发值</span>
              <span style="padding-left: 28px; line-height: 40px"
                >相关预案</span
              >
            </div>
            <div v-for="(item, index) in isDrawerCList" :key="index">
              <div
                v-for="(itm, inx) in item.slist"
                :key="inx"
                style="
                  display: flex;
                  padding: 4px;
                  line-height: 30px;
                  border-bottom: 1px solid rgba(224, 231, 237, 0.2);
                "
              >
                <div style="width: 80px; margin-right: 5px; padding-left: 5px">
                  {{ item.strategyName }}
                </div>
                <div style="width: 66px; margin-right: 5px; padding-left: 5px">
                  {{ " >200" }}
                </div>
                <div class="reservePlan">{{ itm }}</div>
              </div>
            </div>
          </div>
        </el-drawer>
      </div>

      <!-- <div class="tunnelBox tunnelBoxBottom" ></div> -->
      <!--配置区域-->
      <div class="footer" v-show="displayThumbnail == true">
        <div class="footMiniBox" style="cursor: pointer">
          <div class="footTitle">
            <div class="footTitleCont">
              <img
                :src="carIcon"
                style="width: 18px; margin-right: 5px"
                v-show="sideTheme != 'theme-blue'"
              />
              <p>车辆监测</p>
              <p>Vehicle detection</p>
            </div>
          </div>
          <div id="vehicle"></div>
        </div>
        <div class="footMiniBox footerRight" style="cursor: pointer">
          <div class="footTitle">
            <!-- <div class="footTriangle"></div> -->
            <div class="footTitleCont">
              <img
                :src="energyIcon"
                style="width: 18px; margin-right: 5px"
                v-show="sideTheme != 'theme-blue'"
              />
              <p>能耗监测</p>
              <p>Energy consumption monitoring</p>
            </div>
          </div>
          <div id="energyConsumption"></div>
        </div>

        <div class="footMiniBox footerRight" style="cursor: pointer">
          <div class="footTitle">
            <div class="footTitleCont">
              <img
                :src="keyVehiclesIcon"
                style="width: 17px; margin-right: 5px"
                v-show="sideTheme != 'theme-blue'"
              />
              <p>实时车辆</p>
              <p>Key vehicles</p>
            </div>
          </div>
          <!-- <div id="focusCar"></div> -->
          <div class="realTimeTable">
            <ul>
              <li>
                <div>车牌号</div>
                <div>速度</div>
                <div>车道</div>
                <!--                <div>车型</div>-->
              </li>
            </ul>
            <vue-seamless-scroll
              :class-option="defaultOption"
              class="listContent"
              :data="realTimeList"
            >
              <div
                v-for="(item, index) in realTimeList"
                :key="index"
                class="listRow"
                style="display: flex"
              >
                <div style="text-align: center; width: 15px; margin-left: 25px">
                  {{ index + 1 }}
                </div>
                <div style="width: 95px; text-align: center; margin-left: 10px">
                  {{ item.vehicleLicense }}
                </div>
                <div
                  style="width: 112px; text-align: center; margin-left: 30px"
                >
                  {{ item.speed }}km/h
                </div>
                <div style="width: 86px; text-align: center; margin-left: 35px">
                  {{ item.laneNum }}车道
                </div>
                <!-- <div style="width: 94px; text-align: center" v-if="item.vehicleType">
                  {{ getCheXing(item.vehicleType) }}
                </div> -->
              </div>
            </vue-seamless-scroll>
          </div>
        </div>
        <div class="footerRight footMiniBox" style="cursor: pointer">
          <div class="footTitle">
            <div class="footTitleCont">
              <img
                :src="warningIcon"
                style="width: 16px; margin-right: 5px"
                v-show="sideTheme != 'theme-blue'"
              />
              <p>预警事件</p>
              <p>Alert event</p>
            </div>
          </div>
          <div
            v-if="trafficList.length == 0"
            style="
              width: 100%;
              text-align: center;
              font-size: 14px;
              margin-top: 80px;
            "
          >
            暂无交通事件
          </div>
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="listContent"
            :data="trafficList"
          >
            <el-row
              v-for="(item, index) in trafficList"
              :key="index"
              class="listRow"
            >
              <el-col style="width: 3vw; text-align: center">{{
                index + 1
              }}</el-col>
              <el-col style="width: 18vw" @click.native="jumpYingJi(item.id)"
                >{{ item.startTime }} {{ item.tunnels.tunnelName }}发生{{
                  item.eventType.eventType
                }}事件</el-col
              >
            </el-row>
          </vue-seamless-scroll>
        </div>
      </div>
      <!-- <div class="footer" v-show="displayThumbnail == false"></div> -->
    </div>
    <!-- 批量操作弹窗 -->

    <el-dialog
      class="workbench-dialog batch-table operationDiglog"
      :title="title"
      :visible.sync="batchManageDialog"
      width="560px"
      append-to-body
      v-dialogDrag
    >
      <div
        style="
          width: 100%;
          height: 10px;
          display: flex;
          justify-content: space-between;
        "
      >
        <div class="dialogLine"></div>
        <img
          src="../../../assets/cloudControl/dialogHeader.png"
          style="height: 30px; transform: translateY(-30px)"
          @click="closeBatchManageDialog"
        />
      </div>
      <el-table
        ref="batchManageTable"
        :data="batchManageList"
        tooltip-effect="dark"
        style="width: 100%; margin-bottom: 0px !important"
        max-height="220"
        size="mini"
        @row-click="handleRowClick"
      >
        <el-table-column
          prop="eqName"
          label="设备名称"
          width="220"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="pile"
          label="桩号"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="方向">
          <template slot-scope="scope">
            <span>{{ getDirection(scope.row.eqDirection) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-form
        :model="batchManageForm"
        label-width="90px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-form-item label="配置状态:">
          <div class="wrap">
            <el-radio-group
              v-for="(item, index) in eqTypeStateList"
              :key="index"
              v-model="batchManageForm.state"
              style="display: flex; flex-direction: column"
              @change="$forceUpdate()"
            >
              <el-radio
                v-if="batchManageForm.eqType == item.type && item.control == 1"
                class="el-radio flex-row"
                :label="item.name"
                style="align-items: center"
                :class="[
                  String(batchManageForm.state) == String(item.state)
                    ? 'el-radio-selcted'
                    : '',
                ]"
              >
                <el-row
                  class="flex-row"
                  v-if="
                    batchManageForm.eqDirection == '0' &&
                    batchManageForm.eqType == (1 || 2)
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
                    v-if="item.url.length > 1"
                  />
                  <div style="margin: 0 0 0 10px; display: inline-block">
                    {{ item.name }}
                  </div>
                </el-row>
                <el-row
                  class="flex-row"
                  v-if="
                    batchManageForm.eqDirection == '1' &&
                    batchManageForm.eqType == (1 || 2)
                  "
                >
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[1]"
                  />
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[0]"
                    v-if="item.url.length > 1"
                  />
                  <div style="margin: 0 0 0 10px; display: inline-block">
                    {{ item.name }}
                  </div>
                </el-row>
                <el-row
                  class="flex-row"
                  v-if="batchManageForm.eqType != (1 || 2)"
                >
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
      </el-form>
      <div slot="footer" style="float: right; margin-bottom: 20px">
        <el-button
          type="primary"
          size="mini"
          @click="batchManageOK()"
          style="width: 80px"
          class="submitButton"
          >确 定</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="closeBatchManageDialog()"
          style="width: 80px"
          >取 消</el-button
        >
      </div>
    </el-dialog>
    <!-- 操作日志 弹窗 -->
    <el-dialog
      class="workbench-dialog batch-table operationDiglog"
      :title="title"
      :visible.sync="operationLogDialog"
      width="1000px"
      append-to-body
      v-dialogDrag
    >
      <el-form
        :model="queryParams"
        ref="queryForm"
        label-width="68px"
        size="mini"
      >
        <el-row>
          <el-form-item
            label="设备类型"
            prop="eqTypeId"
            style="display: inline-block"
          >
            <el-select
              v-model="queryParams.eqTypeId"
              placeholder="请选择设备类型"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="隧道名称"
            prop="tunnelId"
            style="display: inline-block; margin-left: 20px"
          >
            <el-select
              v-model="queryParams.tunnelId"
              placeholder="请选择隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="创建时间" style="display: inline-block">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 360px"
              value-format="yyyy-MM-dd HH-mm-ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              class="dateRange"
            ></el-date-picker>
          </el-form-item>
          <el-form-item style="display: inline-block; margin-left: 20px">
            <el-button
              type="cyan"
              icon="el-icon-search"
              size="mini"
              class="submitButton"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
              style="color: white"
              >重置
            </el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <el-table
        v-loading="loading"
        :data="logList"
        min-height="200"
        max-height="400"
        :default-sort="{ prop: 'createTime', order: 'descending' }"
        @selection-change="handleSelectionChange"
        empty-text="暂无操作日志"
      >
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <el-table-column
          label="隧道名称"
          align="center"
          prop="tunnelName.tunnelName"
        />
        <el-table-column
          label="设备类型"
          align="center"
          prop="typeName.typeName"
        />
        <el-table-column label="设备名称" align="center" prop="eqName.eqName" />
        <el-table-column
          label="操作状态"
          align="center"
          prop="stateName.stateName"
        />
        <el-table-column
          label="控制方式"
          align="center"
          prop="controlType"
          :formatter="controlTypeFormat"
        />
        <!-- <el-table-column label="用户名称" align="center" prop="userName" /> -->
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="220"
          sortable
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
        class="paginationWorkbench"
      />
      <!-- <div slot="footer">
        <el-button type="primary" @click="cancel">关 闭</el-button>
      </div> -->
    </el-dialog>
    <!-- 隧道选择对话框-->
    <el-dialog
      class="workbench-dialog"
      :title="title"
      :visible.sync="tunnelVisible"
      width="600px"
      append-to-body
    >
      <el-row>
        <el-checkbox-group v-model="checkboxTunnel">
          <el-checkbox
            class="tunnel-checkbox"
            v-for="(item, index) in tunnelList"
            :key="index"
            :label="item"
            border
          >
            {{ item.tunnelName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-row>
      <div slot="footer">
        <el-button type="primary" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!--状态切换对话框-->
    <!-- <el-dialog v-dialogDrag class="workbench-dialog" :title="title" :visible.sync="stateSwitchVisible" width="450px"
      append-to-body :class="stateForm.manufacturers?'robot-dialog':''">
      <div style="width: 100%;height: 30px;display: flex;justify-content: space-between;">
        <div class="dialogLine"></div>
        <img src="../../../assets/cloudControl/dialogHeader.png" style="height: 30px;transform: translateY(-30px);"/>
      </div>

      <el-form ref="form" :model="stateForm" label-width="80px" label-position="left" size="mini"
                style="position: relative;padding: 20px;padding-top: 0px;">
        <el-row>
         <el-col :span="12">
           <el-form-item label="设备类型:">
             {{ stateForm.eqTypeName }}
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="隧道名称:">
             {{ stateForm.tunnelName }}
           </el-form-item>
         </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属方向:">
              <div v-if="stateForm.eqDirection == 0">
                {{ rightDirection + "方向" }} -->
    <!-- <img src="../../../assets/image/workbench/long-arrowhead.png"/> -->
    <!-- </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }} -->
    <!--  <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                /> -->
    <!-- </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
         <el-col :span="12">
           <el-form-item label="所属机构:">
             {{ stateForm.eqTypeName }}
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="设备厂商:">
             {{ stateForm.tunnelName }}
           </el-form-item>
         </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="运行状态:">
              {{ '正常' }}
            </el-form-item>
          </el-col>
        </el-row>



        <el-form-item label="调光强度:" v-if="stateForm.eqType == 99999999">
          <div style="width: 200px; float: left">
            <slider :min="0" :max="100" v-model="stateForm.lightValue"></slider>
          </div>
          <label style="color: yellow; margin-left: 10px; pointer-events: none">
            {{ stateForm.lightValue }}</label>
        </el-form-item> -->
    <!-- 所有设备 -->

    <!-- plc、紧急电话、手报
        <el-form-item label="配置状态:" v-if="stateForm.eqType == (14 || 21 || 34) ">
          {{ '在线'}}
        </el-form-item> -->
    <!-- 28：压力表  -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 28">
          {{ '正常'}}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 21">

        <el-form-item label="运行状态:">
          {{ '正常' }}
        </el-form-item>
         plc -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 14">
          {{ '在线'}}
        </el-form-item> -->
    <!-- 压力表 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 28">
          {{ '正常'}}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 21">

          {{ '在线'}}
        </el-form-item>  -->

    <!-- 消防水泵 -->
    <!-- <el-form-item label="水位:" v-if="stateForm.eqType == 13">
          {{ '水位'}}
        </el-form-item>

        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="配置状态:" >
              {{ '正常'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上传时间:" >
              {{ '今天'}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="车道:" >
              {{ '这是车道'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车流量:" >
              {{ '10辆/没分钟'}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="平均车速:">
              {{ '60公里/每小时'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="占有率:">
              {{ '60%'}}
            </el-form-item>
          </el-col>
        </el-row>





        <template  v-if="stateForm.value &&stateForm.eqType == 20">
          <el-table
            ref="multipleTable"
            :data="weiboList"
            tooltip-effect="dark"
            style="width: 100%"
            size="mini"
            empty-text="暂无数据"
          >
            <el-table-column label="车道" align="center" prop="byLane">
              <template slot-scope="scope">
                <span>第{{ scope.row.byLane }}车道</span>
              </template>
            </el-table-column>
            <el-table-column
              label="车流量(辆/分钟)"
              align="center"
              prop="fSpaceOccupyRation"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.fSpaceOccupyRation }}辆/分钟</span>
              </template>
            </el-table-column>
            <el-table-column label="平均车速" align="center" prop="bySpeed">
              <template slot-scope="scope">
                <span>{{ scope.row.bySpeed }}km/h</span>
              </template>
            </el-table-column>
            <el-table-column
              label="占有率"
              align="center"
              prop="fSpaceOccupyRation"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.fSpaceOccupyRation }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="上传时间" align="center" prop="createTime">
              <template slot-scope="scope">
                <span>{{
                    parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
                  }}</span>
              </template>
            </el-table-column>
          </el-table>
        </template> -->

    <!-- ====================微博检查结束============ -->
    <!-- <el-form-item label="状态:" v-if="stateForm.value && ![5, 6, 14, 13,15, 16,20].includes(stateForm.eqType)">
          {{ stateForm.value }}
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 999">
          {{ stateForm.value }} ppm
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 15">
          {{ stateForm.value }} x10-3m<sup>-1</sup>
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 16">
          {{ stateForm.value }} m/s
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && [5].includes(stateForm.eqType)">
          {{ stateForm.value }} cd/m²
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 999">
          {{ stateForm.value }} m
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.eqType == 123">
          {{ stateForm.state }}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 33 && stateForm.eqType != 21 && !stateForm.value && stateForm.eqType != 123 && stateForm.eqType != 28  && stateForm.eqType != 34""> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
          <div v-if="stateForm.eqType == 31" class="EqType31">
            <el-form label-width="60px" label-position="left" size="mini">
              <el-form-item class="radioEqType31">
                <el-radio-group v-model="radioEqType31"  >
                   <el-radio :label="1">关灯</el-radio>
                   <el-radio :label="2">同步单闪</el-radio>
                   <el-radio :label="3">流水灯</el-radio>
                 </el-radio-group>
              </el-form-item>

              <el-form-item label="亮度:">
                  <el-slider v-model="brightness"  style="width: 90%;" :min="10" :max="155">
                  </el-slider>
              </el-form-item>
              <el-form-item label="频率:">
                 <el-slider v-model="frequency"  style="width: 90%;" :min="10" :max="155">
                 </el-slider>
              </el-form-item>
            </el-form>
          </div>
          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item> -->
    <!-- 应急照明 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 6 "> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>

          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item> -->
    <!-- 消防水泵 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 13"> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>

          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item>


        <template v-if="stateForm.eqType == 112">
          <div class="robotBox">
            <video v-if="watchVideo" controls muted autoplay loop :src="robotVideoUrl" style="width: 250px; height: 150px;">
              <source src type="video/mp4" />
            </video>
            <el-image v-else :src="robotUrl">
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
            <div class="switchIcon" @click="watchVideo = !watchVideo">
              <i class="el-icon-sort"></i>
            </div>
          </div>
          <el-form-item label="生产厂家:" v-if="stateForm.manufacturers">
            <div v-if="!(stateForm.manufacturers == ''|| stateForm.manufacturers == null)">{{
                stateForm.manufacturers
              }}
            </div>
            <div v-else style="white-space: nowrap;">暂未获取</div>
          </el-form-item>
          <el-form-item label="设备电量:">
            <battery-icon v-if="!(stateForm.electricQuantity == ''|| stateForm.electricQuantity == null)" style="width:auto;"
              :quantity="stateForm.electricQuantity" rotate="0" />
            <div v-else style="white-space: nowrap;">暂未获取</div>
          </el-form-item>
          <el-tabs class="robotTabs" v-model="robotInformationActive" @tab-click="robotTabsClick">
            <el-tab-pane label="车流量情况" name="trafficFlow">
              <el-row>车流量：307</el-row>
              <el-col :span="8">客一：217</el-col>
              <el-col :span="8">客二：2</el-col>
              <el-col :span="8">客三：1</el-col>
              <el-col :span="8">客四：2</el-col>
              <el-col :span="8">货一：35</el-col>
              <el-col :span="8">货二：18</el-col>
              <el-col :span="8">货三：21</el-col>
              <el-col :span="8">货四：4</el-col>
              <el-col :span="8">货五：0</el-col>
              <el-col :span="8">货六：5</el-col>
              <el-col :span="8">其他：2</el-col>
            </el-tab-pane>
            <el-tab-pane label="事件情况" name="eventSituation">
              <el-row>
                <el-col :span="8">事件数：3次</el-col>
                <el-col :span="8">未处置：2次</el-col>
                <el-col :span="8">已处置：1次</el-col>
              </el-row>
              <el-card class="box-card" :span="24">
                <el-col :span="12">事件类型：追尾</el-col>
                <el-col :span="12">事件标题：追尾</el-col>
                <el-col :span="12">事件状态：处理中</el-col>
                <el-col :span="12">时间：2022-2-28 09:30:00</el-col>
                <el-col :span="12">级别：一级</el-col>
                <el-col :span="12">受伤：0; 死亡：0;</el-col>
              </el-card>


            </el-tab-pane>
            <el-tab-pane label="地道的路面情况" name="theRoad"></el-tab-pane>
            <el-tab-pane label="状态记录" name="stateRecords">
              <div id="robotEchart" />
            </el-tab-pane>
          </el-tabs>
        </template>

      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" v-if="stateForm.eqType != 21 && !stateForm.value" @click="submitState"
          class="submitButton">确 定
        </el-button>
        <el-button type="primary" size="mini" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 14"
          @click="submitState" class="submitButton">确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->

    <!--批量管理对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table batch-dialog"
      :title="title"
      :visible.sync="batchVisible"
      width="560px"
      append-to-body
      @close="batchForm.eqDirection = ''"
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
          src="../../../assets/cloudControl/dialogHeader.png"
          style="height: 30px; transform: translateY(-30px)"
        />
      </div>
      <el-form
        ref="batchForm"
        :rules="rules"
        :model="batchForm"
        label-width="80px"
        label-position="left"
        size="mini"
        hide-required-asterisk
        style="padding: 0px 20px 20px"
      >
        <el-form-item label="设备类型:">
          <el-select v-model="batchForm.eqType" size="mini">
            <el-option
              v-for="item in eqTypeList"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            />
            <!--            <el-option
                          v-if="boxTypeList.length > 0"
                          v-for="item in boxTypeList"
                          :key="item.typeId"
                          :label="item.typeName"
                          :value="item.typeId"
                        /> -->
          </el-select>
        </el-form-item>
        <!-- 如果选择项为"普通车道指示器，id = 1,则二级选项显示搜索条件，为车向和车道" -->
        <el-form-item label="方向">
          <el-select v-model="batchForm.eqDirection" size="mini" clearable>
            <el-option
              v-if="allDirection.length > 0"
              v-for="item in allDirection"
              :key="item.eqDirection"
              :label="item.eqDirectionName"
              :value="item.eqDirection"
            />
          </el-select>
        </el-form-item>
        <template v-if="batchForm.eqType == 1">
          <el-form-item label="车道">
            <el-select v-model="batchForm.eqlane" size="mini">
              <el-option
                v-if="allLane.length > 0"
                v-for="item in allLane"
                :key="item.lane"
                :label="item.laneName"
                :value="item.lane"
              />
            </el-select>
          </el-form-item>
        </template>
        <el-table
          ref="multipleTable"
          :data="devicesList"
          tooltip-effect="dark"
          style="width: 100%"
          max-height="220"
          size="mini"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
          empty-text="暂无设备"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            prop="eqName"
            label="设备名称"
            width="220"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="pile"
            label="桩号"
            width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="eqDirectionName" label="方向">
          </el-table-column>
        </el-table>

        <el-form-item label="配置状态:" prop="state" style="margin-top: 20px">
          <div class="wrap">
            <el-radio-group
              v-for="(items, index) in eqTypeStateList"
              :key="index"
              v-model="batchForm.state"
              style="display: flex; flex-direction: column"
            >
              <el-radio
                v-if="batchForm.eqType == items.type && items.control == 1"
                class="el-radio flex-row"
                :label="items.state"
                :class="[
                  batchForm.state == items.state ? 'el-radio-selcted' : '',
                ]"
                style="align-items: center"
              >
                <el-row v-if="items.url.length > 1 && batchFormDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img
                      v-for="(url, indexs) in items.url"
                      class="equipment-img"
                      :key="indexs"
                      :src="url"
                    />
                  </el-row>
                  <el-row
                    class="flex-row align-items-center form-direction"
                    style="margin-top: 5px"
                  >
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row
                  v-else-if="items.url.length > 1 && batchFormDirection == 1"
                >
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="items.url[1]" />
                    <img class="equipment-img" :src="items.url[0]" />
                  </el-row>
                  <el-row
                    class="flex-row align-items-center form-direction"
                    style="margin-top: 5px"
                  >
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="items.url.length == 1" class="flex-row">
                  <img class="equipment-img" :src="items.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ items.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          @click="submitBatchState('batchForm')"
          class="submitButton"
          >确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--微波车检对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="weiboVisible"
      width="45vw"
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="weiboList"
        tooltip-effect="dark"
        style="width: 100%"
        size="mini"
        empty-text="暂无数据"
      >
        <el-table-column label="车道" align="center" prop="byLane" width="200">
          <template slot-scope="scope">
            <span>第{{ scope.row.byLane }}车道</span>
          </template>
        </el-table-column>
        <el-table-column
          label="车流量(辆/分钟)"
          align="center"
          prop="fSpaceOccupyRation"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.byVehicelNum }}辆/分钟</span>
          </template>
        </el-table-column>
        <el-table-column label="平均车速" align="center" prop="bySpeed">
          <template slot-scope="scope">
            <span>{{ scope.row.bySpeed }}km/h</span>
          </template>
        </el-table-column>
        <el-table-column
          label="占有率"
          align="center"
          prop="fSpaceOccupyRation"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.fSpaceOccupyRation }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="上传时间" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
            }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
      v-if="[14, 21, 32, 33, 15, 16, 30, 35].includes(this.eqInfo.clickEqType)"
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
    <com-robot
      class="comClass"
      v-if="this.eqInfo.clickEqType == 29"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-robot>
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
      v-if="this.eqInfo.clickEqType == 31"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-youdao>
    <!--摄像机对话框-->
    <!-- <el-dialog v-dialogDrag class="workbench-dialog batch-table video-dialog" :title="title" :visible="cameraVisible"
      width="860px" append-to-body @opened="loadFlv" :before-close="handleClosee">
      <div style="width: 100%;height: 30px;display: flex;justify-content: space-between;">
        <div class="dialogLine"></div>
        <img src="../../../assets/cloudControl/dialogHeader.png" style="height: 30px;transform: translateY(-30px);"/>
      </div>
      <el-form ref="form" :model="stateForm" label-width="60px" label-position="left" size="mini"
              style="padding: 20px;padding-top: 0px;">
        <el-row>
          <el-col :span="8">
            <el-form-item label="摄像机:" label-width="60px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="30px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img src="../../../assets/image/workbench/long-arrowhead.png" style="transform: rotate(180deg); margin-left: 10px" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实时视频:" label-width="80px">
          <video id="videoBox" controls muted autoplay loop style="width: 80%;"></video>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->

    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="shuibengVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form label-width="80px" label-position="left" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="1#泵状态:">
              {{ shuibengObj.beng1 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="2#泵状态:">
              {{ shuibengObj.beng2 }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="缺水故障:">
              {{ shuibengObj.queshui }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电源故障:">
              {{ shuibengObj.dianyuan }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联动指示:">
              {{ shuibengObj.liandong }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- <el-form-item label="设备状态:">
              {{shuibengObj.devState}}
            </el-form-item> -->
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--道路结冰对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="daolujiebingVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form ref="form" :model="stateForm" label-position="left" size="mini">
        <el-row>
          <el-col :span="8">
            <el-form-item label="所处位置:" label-width="80px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="40px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="表面温度:">
              {{ dljbObj.surfaceTemperature }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面积水:">
              {{ dljbObj.roadWater }}
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="结冰温度:">
              {{ dljbObj.freezeTemperature }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冰厚度:">
              {{ dljbObj.IceThickness }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="盐度值:">
              {{ dljbObj.salinityValue }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="湿滑系数:">
              {{ dljbObj.wetslidingCoefficient }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="路面状况:">
              {{ dljbObj.roadCondition }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备状态:">
              {{ dljbObj.status }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 路面状态 -->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="lumianVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form ref="form" :model="stateForm" label-position="left" size="mini">
        <el-row>
          <el-col :span="8">
            <el-form-item label="所处位置:" label-width="80px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="40px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="路面温度:">
              {{ lumianList.pavementtemp }}℃
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面积水:">
              {{ lumianList.waterthickness }}毫米
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="湿滑系数:">
              {{ lumianList.frictionalcoe }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冰厚度:">
              {{ lumianList.icethickness }}毫米
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="积雪厚度:">
              {{ lumianList.icethickness }}毫米
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面状况:">
              {{ lumianList.pavementstatus }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态:" label-width="80px">
              {{ lmList == 1 ? "正常" : "报警" }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采集时间:">
              <template slot-scope="scope">
                <span>
                  {{
                    parseTime(
                      lumianList.timeCollect,
                      "{yyyy}-{m}-{d} {h}:{i}:{s}"
                    )
                  }}</span
                >
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- <el-dialog v-dialogDrag class="workbench-dialog" :title="title" :visible.sync="coviVisible" width="560px"
      append-to-body>
      <div style="height: 400px;" v-if="stateForm.eqType == 17" id="fengsu"></div>
      <div style="height: 400px;" v-if="stateForm.eqType == 19" id="covi"></div>
    </el-dialog> -->
    <!-- 结束 -->
    <!--图标含义对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table icon-dialog"
      :title="title"
      :visible.sync="explainVisible"
      width="560px"
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="eqIcon"
        tooltip-effect="dark"
        style="width: 100%"
        :max-height="400"
        size="mini"
        @selection-change="handleSelectionChange"
        empty-text="暂无设备"
      >
        <el-table-column label="图标">
          <template slot-scope="scope">
            　
            <img
              :src="scope.row.url"
              style="vertical-align: middle; max-height: 30px; padding: 2px 0"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="type"
          label="所属类型"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column prop="explain" label="含义说明" show-overflow-tooltip>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!--查看控制策略对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table strategyClass eventDiglog"
      :title="title"
      :visible.sync="strategyVisible"
      width="70%"
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="strategyList"
        tooltip-effect="dark"
        style="width: 100%"
        :max-height="400"
        size="mini"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        empty-text="暂无策略"
      >
        <el-table-column
          label="隧道名称"
          align="center"
          prop="tunnels.tunnelName"
          width="200"
        />
        <el-table-column label="策略名称" align="center" prop="strategyName" />
        <el-table-column label="策略信息" align="center" prop="strategyInfo">
          <template slot-scope="scope" v-if="scope.row.slist != []">
            <div v-for="(item, index) in scope.row.slist" :key="index">
              {{ item }}
            </div>
          </template>
          <div v-else>暂无信息</div>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              v-show="scope.row.strategyType == 0"
              size="mini"
              type="text"
              icon="el-icon-thumb"
              @click="handleController(scope.row)"
              >手动控制
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer">
        <el-button type="primary" @click="strategyCancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 情报板编辑弹窗 -->
    <vms-content-update ref="vmsContentUpdate"></vms-content-update>
    <!-- 情报板批量编辑对话窗 -->
    <content-batch-edit ref="contentBatchEdit"></content-batch-edit>
    <!-- 火灾报警 对话框 -->
    <el-dialog
      title="火灾报警提示"
      :visible.sync="HZdialogVisible"
      width="25%"
      :before-close="HZhandleClose"
      class="temperatureDialog"
    >
      <div slot="title">
        <span
          ><i
            class="el-icon-message-solid"
            style="color: #e6a23c; margin-right: 10px"
          ></i
        ></span>
        <span style="color: white">火灾报警提示</span>
      </div>
      <div>
        <div>隧道名称</div>
        <div>{{ tunnelName }}</div>
      </div>
      <div>
        <div>隧道温度</div>
        <div>{{ temperature }}°C</div>
      </div>
      <div class="HZdialogBg"></div>
    </el-dialog>
    <!-- 限高控制 对话框 -->
    <el-dialog
      title="限高杆控制"
      :visible.sync="controlHeightVisible"
      :before-close="controlHeightClose"
      class="workbench-dialog"
      v-dialogDrag
      width="560px"
      append-to-body
    >
      <div>
        <span>隧道名称：</span>
        <span>{{ tunnelNameEarlyWarn }}</span>
      </div>
      <div>
        <span>限高杆高度：</span>
        <span>2米</span>
      </div>
      <div>
        <el-progress
          :text-inside="true"
          :stroke-width="26"
          :percentage="percentage"
          :color="customColors"
          style="width: 60%; display: inline-block"
        ></el-progress>
        <div
          style="
            display: inline-block;
            margin-left: 20px;
            color: white !important;
          "
        >
          <el-button-group>
            <el-button icon="el-icon-minus" @click="decrease"></el-button>
            <el-button icon="el-icon-plus" @click="increase"></el-button>
          </el-button-group>
        </div>
      </div>
    </el-dialog>
    <!--隧道诱导对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table youdaoDialog"
      :title="title"
      :visible.sync="youdaoVisible"
      width="40vw"
      append-to-body
    >
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
      >
        <el-row>
          <!-- <el-col :span="8">
            <el-form-item label="所处位置:">
              {{stateForm.pile}}
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="IP:">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:">
              <div v-if="stateForm.eqDirection == 0">
                <img src="@/assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="@/assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="控制模式:" prop="corModel">
              <el-radio-group v-model="stateForm.corModel" @change="outLight">
                <el-col :span="4">
                  <el-radio
                    key="00"
                    label="00"
                    style="padding: 5px 20px 5px 10px"
                    >闪烁</el-radio
                  >
                </el-col>
                <el-col :span="4">
                  <el-radio
                    key="02"
                    label="02"
                    style="padding: 5px 20px 5px 10px; margin-left: 25px"
                    >常亮</el-radio
                  >
                </el-col>
                <el-col :span="4">
                  <el-radio
                    key="03"
                    label="03"
                    style="padding: 5px 20px 5px 10px; margin-left: 50px"
                    >常灭</el-radio
                  >
                </el-col>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车道:" prop="lane">
              <el-checkbox v-model="stateForm.Zlane" label="D" key="D"
                >左车道</el-checkbox
              >
              <el-checkbox v-model="stateForm.Ylane" label="E" key="E"
                >右车道</el-checkbox
              >
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="白灯亮度:">
              <el-slider
                v-model="stateForm.whiteLight"
                :step="10"
                style="width: 90%"
              >
              </el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="黄灯亮度:">
              <el-slider
                v-model="stateForm.yellowLight"
                :step="10"
                style="width: 90%"
              >
              </el-slider>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.corModel == '00'">
          <el-col :span="12">
            <el-form-item label="闪烁模式:">
              <el-select
                v-model="stateForm.twinkleModel"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in twinkleModelOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="闪烁频率:">
              <el-select
                v-model="stateForm.twinkleFrequency"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in twinkleFrequencyOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              次/分钟
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.corModel == '00'">
          <el-col :span="12">
            <el-form-item label="亮灯时长:">
              <el-select
                v-model="stateForm.lightTime"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in lightTimeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              毫秒
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="submitCorLight"
          >确 定</el-button
        >
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import flvjs from "flv.js";
import { math } from "@/utils/math.js";
import vueSeamlessScroll from "vue-seamless-scroll";
import $ from "jquery";
import "jquery-ui-dist/jquery-ui";
import "jquery-ui-dist/jquery-ui.min.css";
import bus from "@/utils/bus";
import { mapState } from "vuex";

import {
  getLiPowerDevices,
  //initLipowerDevice
} from "@/api/system/lipowerDevice";

import {
  listDevices,
  getDevices,
  updateDevices,
} from "@/api/equipment/eqlist/api";
import {
  listType,
  listHasType,
  getType,
  groupByBigType,
  hasListByBigType,
  loadPicture,
} from "@/api/equipment/type/api.js";
import { getTemplateInfo } from "@/api/board/template.js";
import {
  listTunnels,
  getTunnels,
  updateTunnels,
  getConfigData,
  // getStorageData,
  setConfigData,
  pressure,
  sendAnalogCom,
  getHostData,
  setCorLight,
  updateCarFinger,
  getDeviceDataAndState,
} from "@/api/equipment/tunnel/api.js";
import {
  listEqTypeState,
  getEqTypeState,
  updateEqTypeState,
  getStateByRun,
  getStateByData,
} from "@/api/equipment/eqTypeState/api";
import {
  icon,
  // eqTypeStateIcon,
  laneImage,
} from "../../../utils/configData.js";
import {
  listStrategy,
  getStrategy,
  handleStrategy,
} from "@/api/event/strategy";
import { selectByEqDeno } from "@/api/business/roadState.js";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import vmsContentUpdate from "@/views/workbench/config/vms-content-update"; //单个编辑
import contentBatchEdit from "@/views/workbench/config/content-batch-edit"; //批量编辑
import comVideo from "@/views/workbench/config/components/video"; //摄像机弹窗
import comLight from "@/views/workbench/config/components/light"; //各种带单选框的弹窗
import comCovi from "@/views/workbench/config/components/covi"; //covi弹窗
import comBright from "@/views/workbench/config/components/bright"; //亮度检测器等只有基本信息的弹窗
import comWind from "@/views/workbench/config/components/wind"; //风速风向弹窗
import comPressure from "@/views/workbench/config/components/pressure"; //压力表弹窗
import comVehicleDetec from "@/views/workbench/config/components/vehicleDetec"; //微波车检弹窗
import comCallPolice from "@/views/workbench/config/components/callPolice"; //声光报警弹窗
import comRobot from "@/views/workbench/config/components/robot"; //机器人弹窗
import comData from "@/views/workbench/config/components/data"; //只有数据的弹窗
import comYoudao from "@/views/workbench/config/components/youdao"; //诱导灯弹窗

import { getLocalIP } from "@/api/event/vedioRecord";
import { getHosts } from "@/api/equipment/plc/api";
import * as echarts from "echarts";
import { listUser, getUserDeptId } from "@/api/system/user";
import * as deviceApi from "@/api/equipment/device/api";
import { listLog } from "@/api/system/log";
import {
  listDept,
  listDeptExcludeChild,
  roleDeptTreeselect,
} from "@/api/system/dept";
import bg from "@/assets/cloudControl/right_button.png";
import hoverbg from "@/assets/cloudControl/right_button_hover2.png";
import {
  getSystemWarningMsg,
  getTrafficIncident,
  proportionOfEquipment,
  trafficFlowInformation,
  vehicleMonitoringInRecent24Hours,
  special,
  getDeviceData,
  batchControlCarFinger,
  timeSharing,
  updateControlTime,
  timeStrategySwitch,
} from "@/api/workbench/config.js";
import {
  getDeviceBase,
  getNewBoardEditInfo,
  templateList,
} from "@/api/workbench/config";
import BatteryIcon from "@/components/BatteryIcon";
import { listEvent, getWarnEvent } from "@/api/event/event";

let configData = {}; //配置信息
let wrapperClientX = 0;
let wrapperClientY = 0;
let boxEqList = [];
let mode = "";
export default {
  carShow: false, //车辆实时状态
  name: "Workbench",
  dicts: ["sd_sys_name"],
  components: {
    // DragItDude,
    videoPlayer,
    vueSeamlessScroll,
    vmsContentUpdate,
    BatteryIcon,
    contentBatchEdit,
    comVideo,
    comLight,
    comCovi,
    comBright,
    comWind,
    comPressure,
    comVehicleDetec,
    comCallPolice,
    comRobot,
    comData,
    comYoudao,
  },
  data() {
    return {
      heightRatio: "",
      lane: "",
      carList: [],
      tunnelKm: "", //隧道实际长度
      tunnelLength: "", //隧道px长度
      chezhiDisabled: false, //车指按钮 返回接口结果前禁用
      // 批量选择设备图标
      iconWidth: "",
      iconHeight: "",
      // 多选框选中项表单
      batchManageForm: {
        state: "",
        eqType: "",
        eqDirection: "",
      },
      itemEqId: [], //选中项id数组
      itemEqType: "", //判断多选的设备类型是否相同
      batchManageList: [], //多选设备数组
      addBatchManage: false, //点击多选设备
      batchManageDialog: false, //批量操作弹窗
      batchManageType: 1,
      screenEqName: "", //筛选设备名称
      timStrategyList: [], //定时控制
      BulkData: [],
      realTimeList: [], //websockt推送实时车辆数据
      tunnelLane: "", //当前隧道有几条车道
      eqInfo: {},
      brandList: [],
      directionList: [{}, {}], //设备方向字典
      // coviVisible: false,

      // radioEqType31: 2,
      dictList: [],
      robotShow: false,
      drawerLineList: [
        {
          value: 1,
          label: "",
        },
        {
          value: 2,
          label: "",
        },
        {
          value: 3,
          label: "",
        },
      ],
      checkList1: [],
      checkList2: [],

      //抽屉
      drawerA: false,
      drawerB: false,
      drawerCVisible: false,
      kaiGuan1: false,
      kaiGuan2: false,
      kaiGuan3: false,
      kaiGuan4: false,
      kaiGuan5: false,
      kaiGuan6: false,
      // checked1: false,
      // checked2: false,
      // checked3: false,
      // checked4: false,
      // checked5: false,
      // checked6: false,
      isDrawerCList: [],

      value1: new Date(),
      // wheel:'wheel.prevent',
      buttonIndex: 0,
      sevenDaysBefore: [],
      trafficJamList: [],
      operationLogDialog: false, //操作日志弹窗
      //设备类型
      eqTypeData: {},
      //所属隧道
      eqTunnelData: {},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqTypeId: null,
        tunnelId: null,
        userName: null,
        eqId: null,
        /* eqName: null, */
        code: null,
        cmd: null,
        beforeState: null,
        operationState: null,
        controlType: null,
        state: null,
        description: null,
      },
      // 操作日志表格数据
      logList: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 控制方式   3：手动 1：时间控制 2：光强控制字典
      controlTypeOptions: [],
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      total: 0,
      // 进度条
      customColors: [
        {
          color: "#f56c6c",
          percentage: 20,
        },
        {
          color: "#e6a23c",
          percentage: 40,
        },
        {
          color: "#5cb87a",
          percentage: 60,
        },
        {
          color: "#1989fa",
          percentage: 80,
        },
        {
          color: "#6f7ad3",
          percentage: 100,
        },
      ],
      percentage: 70,
      tunnelNameEarlyWarn: "",
      tunnelId: "",
      displayThumbnail: true,
      SysEarlyWarning: [],
      trafficList: [],
      hour1: "",
      hour2: "",
      hour3: "",

      selectSite: "",
      siteList: null,
      siteProps: {
        value: "deptId",
        label: "deptName",
        children: "children",
      },
      seamless: false, //情报板轮播
      /* ---------火灾报警---------------*/
      alarmBell: false, //是否启用报警铃声
      tunnelName: "",
      temperature: "",
      HZdialogVisible: false,
      controlHeightVisible: false,
      carIcon: require("@/assets/icons/carIcon.png"),
      energyIcon: require("@/assets/icons/energyIcon.png"),
      keyVehiclesIcon: require("@/assets/icons/keyVehiclesIcon.png"),
      warningIcon: require("@/assets/icons/warningIcon.png"),
      railingList: [
        {
          position: {
            top: 325,
            left: 150,
          },
          height: 2,
          image: require("@/assets/logo/equipment_log/railing.png"),
          tunnelName: "刘长山隧道",
        },
        {
          position: {
            top: 85,
            left: 0,
          },
          height: 2,
          image: require("@/assets/logo/equipment_log/railing.png"),
          tunnelName: "凤凰山隧道",
        },
      ],
      /* ---------工作台轮播情报版---------------*/
      newsList: [
        {
          title: "下雨路滑 请谨慎驾驶",
        },
        {
          title: "文明出行 安全第一",
        },
      ],
      /* ---------鼠标右键拖动---------------*/
      mouseLeft: 0,
      mouseTop: 0,
      curX: 0,
      curY: 0,
      dragFlag: false,
      wheelFlag: null,
      oldWidth: 0,
      oldHeight: 0,
      scaleX: 1,
      scaleY: 1,
      newWidth: 0,
      newHeight: 0,
      bgX: 0,
      bgY: 0,
      ww: null,
      wh: null,
      imgw: null,
      imgh: null,
      scaleSize: null,
      testDivDom: null,
      dragImgDom: null,
      wi: null,
      hi: null,
      /* ---------鼠标拖动---------------*/
      tunnelName: "",
      move: false,
      py: "", //开始y轴的位置
      px: "", //开始x轴的位置
      back: "#1E90FF", //框框背景颜色
      h: "", //框框的高度控制
      w: "", //框框的宽度控制
      top: "", //框框的位置控制
      left: "", //框框的位置控制
      len: 0, //框框的边框
      tabLeft: 0,
      tabTop: 0,
      whiteLight: 0,
      yellowLight: 0,
      boxTypeList: [],
      systemState: "良好",
      moveTop: 0.11,
      /* -------------------------*/
      timer: null, //定时器
      windowHeight: document.documentElement.clientHeight, //实时屏幕高度
      displayNumb: false, //显示编号
      zoomSwitch: false, //缩放
      currentTunnel: {
        id: "",
        name: "",
        lane: {},
      }, //选中的隧道
      lightState: "正常",
      dljbObj: {},
      lmList: {},
      lumianList: {},
      loading: true,
      title: "", //对话框标题
      tunnelVisible: false,
      stateSwitchVisible: false,
      batchVisible: false,
      lumianVisible: false,
      weiboVisible: false,
      cameraVisible: false,
      daolujiebingVisible: false,
      lightControlDialog: false,
      youdaoVisible: false,
      cameraErrorInfo: "",
      explainVisible: false,
      eqTypeList: [], //设备类型
      eqBigTypeList: [], //设备类型
      selectBigType: {
        bigType: "全部设备",
        index: 0,
      },
      checkboxTunnel: [], //可点的隧道
      tunnelList: [], //隧道
      selectedIconList: [], //配置图标
      stateForm: {
        state: 1,
      }, //配置表单
      brightness: 50,
      frequency: 69,
      weiboList: [],
      shuibengVisible: false,
      shuibengObj: {},
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: "",
        eqDirection: "",
      },
      lightSwitch: 0,
      //车道按钮样式
      // tunnelBtnStyle: [
      //   {
      //     "border-color": "#ffbf1d",
      //     // "box-shadow": "0px 0px 10px #e2f3fa inset",
      //     "color": "#0a88bd",
      //     "background-color": "#e2f3fa !important",
      //   },
      // ],
      /* playerOptions: {
          playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
          autoplay: false, // 如果为true,浏览器准备好时开始回放。
          muted: false, // 默认情况下将会消除任何音频。
          loop: false, // 是否视频一结束就重新开始。
          preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
          language: 'zh-CN',
          aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
          fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
          sources: [{
            type: "video/mp4", // 类型
            src: require('D:/123.mp4') // url地址
          }],
          poster: '', // 封面地址
          notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
          controlBar: {
            timeDivider: true, // 当前时间和持续时间的分隔符
            durationDisplay: true, // 显示持续时间
            remainingTimeDisplay: false, // 是否显示剩余时间功能
            fullscreenToggle: true // 是否显示全屏按钮
          }
        }, */

      timer: null,

      //车道列表
      laneUrlList: laneImage,
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
      devicesList: [],
      eqIcon: icon,
      //各状态对应图标列表（灵活性差，后期最好在类型状态管理中动态添加）
      eqTypeStateList: [],
      itemEqTypeStateList: [],
      // 策略visible
      strategyVisible: null,
      strategyList: [],
      asd: 50,
      fjState: null,
      hostIP: null,
      userDeptId: null,
      tunnelQueryParams: {
        deptId: this.userDeptId,
      },
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      //验证
      rules: {
        state: [
          {
            required: true,
            message: "请选择设备状态",
            trigger: "change",
          },
        ],
      },
      systemStateClass: {
        良好: "system-state-normal",
        较差: "system-state-abnormal",
      },
      //table样式
      // rowStyle: {
      //   "background-color": "#304156",
      //   color: "#C0CCDA",
      // },
      // explainRowStyle: {
      //   "background-color": "#304156",
      //   color: "#fff",
      // },
      // explainHeaderStyle: {
      //   "background-color": "#304156",
      //   color: "#fff",
      //   "border-color": "#304156",
      // },
      leftDirection: "潍坊", //左侧方向
      rightDirection: "济南", //右侧方向
      batchFormDirection: 1, //批量对话框默认的设备方向
      twinkleModelOptions: [
        {
          value: "01",
          label: "单闪",
        },
        {
          value: "02",
          label: "双闪",
        },
        {
          value: "03",
          label: "三闪",
        },
        {
          value: "04",
          label: "四闪",
        },
        {
          value: "05",
          label: "五闪",
        },
      ],
      twinkleFrequencyOptions: [
        {
          value: "20",
          label: "20",
        },
        {
          value: "30",
          label: "30",
        },
        {
          value: "45",
          label: "45",
        },
        {
          value: "60",
          label: "60",
        },
        {
          value: "90",
          label: "90",
        },
      ],
      lightTimeOptions: [
        {
          value: "100",
          label: "100",
        },
        {
          value: "150",
          label: "150",
        },
        {
          value: "200",
          label: "200",
        },
        {
          value: "250",
          label: "250",
        },
        {
          value: "300",
          label: "300",
        },
        {
          value: "350",
          label: "350",
        },
        {
          value: "400",
          label: "400",
        },
        {
          value: "500",
          label: "500",
        },
      ],
      // 一键车道指示器 车道下拉框
      chezhiLaneList: [],
      chezhiLaneList1: [
        {
          laneId: 1,
          laneName: "一车道",
        },
      ],
      chezhiLaneList2: [
        {
          laneId: 1,
          laneName: "一车道",
        },
        {
          laneId: 2,
          laneName: "二车道",
        },
      ],
      chezhiLaneList3: [
        {
          laneId: 1,
          laneName: "一车道",
        },
        {
          laneId: 2,
          laneName: "二车道",
        },
        {
          laneId: 3,
          laneName: "三车道",
        },
      ],
      // 一键车指状态下拉框
      chezhiStateList: [],
      // 一键车道指示器表单
      chezhiForm0: {
        lane: [],
        state: "",
      },
      chezhiForm1: {
        lane: [],
        state: "",
      },
      lightControForm: {
        index: 0,
        lCDirection: "",
        radioLightControlTop: [],
        radioLightControlBottom: [],
        state: "",
        lane: "",
        name: "",
      },
      lCTop: false,
      lCBottom: false,

      carShow: false,
      carShowTwo: false,
      light:
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAYAAADOvw4PAAAAAXNSR0IArs4c6QAABUBJREFUaEPtWk9oXEUc/r63UA9JerCHQiuIVRDNoYfY6CGBrlrtIftsQ6J4Ezx4EUnxmD3uCl4aPHkTb4Uaa0wDjRR2iyJKVqRgDgGrohgv9rS7gXpIpuz7N/Nm3uzO22xA9O1ld7Pz3pf3m2++3+/3zRDRS6yiggNcAXEOwHj89xzvXQi0QFzlIjZs1xU4RmSOOG4ESADsgtH8zO/b52dtooISrkCUzoEcD6/1wnukPnu9ewKMfgvGBJ+7oNeC511leSfA6d0BYhU1CCznIFT/oUSdC6jqgwqcARE+bNxCMkWT3+/dq/NS15yfjRM1gMshoTQSpb5n/Kb/HhKuztkfqxSfYQ7AzZERLL4R4XNB3rfAcYywU9x6JIg1IlGqWLEy3mO1UdQIns/K/WTexVcn53BQupmhTBrhYlXrEU35HJBKJV+C6VNcRwNE2TEE7sMEmnwdLybpuMBxi50RNzbgsRymJhux1HSmpzVrumvy4q6cn9tnGgDLiYplEiZJiVFqtBAtSavB/9XsKVkbwIRbBHKN6nARxxOSFTiDgxemuw4XDmTcbhxrA5yI6qpslQoIqKY4p3TX4YVfJE7z2QgnJpJRa/UnVlKf6UrndXokE4OffrgRXAzXXlD3FThKEPunO87/I+O2NiZkCrMV3xmkkoW4Nd3x/LbE+WYqxLGTRRLZlhqD67UUCqIgWY71M9yiUWsmt3TH1zpy8jceFTm7O0Vx4u4vW5U405I4381EZM6jYHEqVtXLxCxINkqSOXd3KvFi9ZBqxLn7cvJvnRJmt+fc3UXKoqtL+J0vfC1xWhfSJEtUyVSmRFl15VItjUQVCyXLQTEgpWSrFG52geoluaU7XvxTTv7tMwPSmKoc+dIdpzYlzl0/wrGRSlW4rPSsP6dcPIWSDaSZVB0u7MtJuXEsJJlhVjp3dzYzE3z5nsRpTio12VDdnbVg59kvJM72G9lkVmsso8FQVVjpYpO4hLEoSKaSbEC64+UHclK+HI9IZqa7w5qZPP+TUpA/J0mWFNwO/pRDuuPkNYmz85Z8Hr1TzXL8NSLJBWf6dv9Tkg1nZtJvKwX5CWXlD9fd2cxMzmxJnO9no8I/D7GUFKp3i4oy8elPJM7P72hdrK5SmkWikizoZ9TdhnR6OEqfrMtF6b8doR83ACd/d5e5Xwd2Ofd34ieKzdOKr5TRZQ1vZnb5/B2J88MrbdCbCO0FnWhu3Z3FQ+vyqY8lzm/vtYEejsu2VMTNYOzAV/e/5/ivsgF4oXPtJOmu6a7U5Kt/KA75kw0wwsn0jfJYAaluscmpTYlz93K4I2NgxMqSZR/oqpOxwQ00+cRHEuf398PnyYpZstswkFDmAKLJ4FSEwPoQl/e/RN+DGylOKt35XNiXe3Brj1QguJ4OVsbe3SCHXDcze8/z0j2Jc2eyApTWU9swIzEzSz7Pfi5xtt+sgFxP7ynm6+7kglMWlPB8Pv6hxNldrkCIKG4jZYMf6N2/+3REv3Tn1Tn/wDxNsH68BnjRaQLr3p21uzNUA6yzvG3ifDtdA0ry1EKsNum9uz7bMUa6q3Pymomz83YNHpeThaOSOen47N1dhjrV+dgHJs5f1RqEGOFpHNZ5qlaVhd91+ACWQEwDGBuCy3sQ2IKHFfX0hX4f0Q/HzczcA7gFeCu81LGeHhG3TvoAlwBvGuSYsWWiG4fmUZU9sIdTWuFsy47TKvtAaQngNOiFOIlKZlkOip8U1lkRjrfCZz614/z6rg8hlsBS+DxOpUCqGN8DsQWBFZ6u23F2qz4oliAOwQMN5yErndtqYIc5/QAAAABJRU5ErkJggg==",
      dark: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAMAAAD5Yf49AAAAAXNSR0IArs4c6QAAAUdQTFRFAAAArq6utbW1ubm5v7+/w8PDycnJzc3N09PTsLCwtra2vb29wMDAysrKzc3N1NTUsbGxt7e3wcHBzs7OsLCwtra2uLi4v7+/wcHBy8vLzc3N1dXVrq6usLCwtLS0tbW1t7e3uLi4v7+/wcHBw8PDycnJy8vLzMzMzc3N1NTU1dXVr6+vsLCwtbW1t7e3v7+/wcHBysrKzMzM1dXVr6+vsLCwtLS0uLi4vb29v7+/wsLCyMjIycnJzs7O1NTU1dXVr6+vsLCwtLS0uLi4vb29wsLCycnJzs7O1NTUr6+vsLCwsbGxsrKys7OztLS0tbW1t7e3uLi4ubm5urq6u7u7vLy8vb29vr6+v7+/wcHBwsLCw8PDxMTExcXFxsbGx8fHyMjIycnJysrKzMzMzc3Nzs7Oz8/P0NDQ0dHR0tLS09PT1NTU1dXV1E4hgwAAAEl0Uk5TAExMTExMTExMTU1NTU1NTU5OTk6EhISEhISEhL6+vr6+vr6+vr6+vr6+vvLy8vLy8vLy8vPz8/Pz8/Pz8/Pz8/T09PT09PT09GJ8jKsAAAG6SURBVEjH7dZHU8JAFMDxxQ5SLFgJKAhKsRuxK3aJGkrovffvf/YF4gwLL3jB0QPvyH928hsO+5YQomRYb/ewjIJ0ZkAxnr7DfMB8wvBu05hUVOazQDAYFIRQKBQORyLnlgmpqK2XyWQqlUqnM5lsNpe7sk1JRWu/KVcqlWq1VqvX643GrWNG/Fnv7R89+aFwHEfJeN631C7L/kCAkkWi0ZV2WY0nEpQsl8+vtct6qVSmZM1my0CIwouNkgwsHCLzqaCM+xFZTA1lMo7IChoo00VE1tIRBv0+AyfkixGVbUDZRGUWKFuobBvKDipzEhf6fReckC+7qGwPyj4qO4ByiMqOoByjshPixQdOyBcOlfmg+FFZDEr8W5bulhWgFFFZ65dkAiJL/IGMH8lGMlzW+H+yao8Mv7VY2fuMRe6zjszddZ/Rsov2fYbJrrvuM1p2N5QdwPftAAHZAb2yfM8OoGVOopTdjgMKKhP3pgqViXtTjcrEvalFZbqhvDX4vreGgLw1EBn11qBkBjHMMR7q4x5GKb2bBhTjPSV7MKmkMm9+pGRPllmpLFifKdmLTSOVRfsrJXtzwD/2Betpxj8VdhGZAAAAAElFTkSuQmCC",
      allDirection: [
        {
          eqDirection: "0",
          eqDirectionName: "济南方向",
        },
        {
          eqDirection: "1",
          eqDirectionName: "潍坊方向",
        },
      ], // 所有方向
      allLane: [], // 所有车道
      dialogVisible: false, // 情报板批量编辑弹窗
      aaa: "444",
      zoomList: [70, 80, 90, 100, 110, 120, 130], // 工作台缩放比例列表
      zoom: 100, // 工作台缩放比例
      spanEqtypeDate: true, // 控制设备配置状态不存在时显示“暂未获取”
      // 巡检机器人----------------------------------------------------------------start
      robotUrl: require("@/assets/images/robot.png"),
      watchVideo: false,
      robotVideoUrl: require("@/assets/Example/v1.mp4"),
      // tab页
      robotInformationActive: "trafficFlow",
      robotInformationCollectionData: null,
      // 状态记录
      robotRecordList: null,
      robotChartDom: null,
      robotOption: null,
      robotStateData: null,
      robotXData: null,
      // 巡检机器人-----------------------------------------------------------------end
      isManagementStation: false, // 当前账号的权限是否是管理站
      showTooltipIndex: 999,
      showTooltip: false,
      tooltipShow: false, //是否展示提示内容
      imageTimer: null, //定时器
      isLogin: false,
      handleTableWheelSwithch: false,

      vehicleXData: [],
      vehicleYData: [],
      keyVehiclesXData: [],
      keyVehiclesYData: [],

      weiboList: [
        {
          id: 1,
          byLane: "一",
          fSpaceOccupyRation: "29",
          bySpeed: "74",
          fSpaceOccupyRation: "47",
          createTime: "2022-09-05 17:44:32",
        },
        {
          id: 2,
          byLane: "二",
          fSpaceOccupyRation: "29",
          bySpeed: "74",
          fSpaceOccupyRation: "47",
          createTime: "2022-09-05 18:54:19",
        },
        {
          id: 3,
          byLane: "三",
          fSpaceOccupyRation: "39",
          bySpeed: "54",
          fSpaceOccupyRation: "91",
          createTime: "2022-09-05 19:25:52",
        },
      ], //假数据
      clickEqType: "", //点击设备的eqType
      equipmentId: "",
      // eqInfo: {},
    };
  },

  directives: {
    //注册指令
    drag: function (el, binding) {
      let oDiv = el; //当前元素
      $(oDiv).bind(
        "mousewheel",

        function (e) {
          // 获取鼠标所在位置

          let { clientX, clientY } = e;
          // 获取元素距离屏幕边界左边和上边距离

          let offsetX = $(oDiv).offset().left;

          let offsetY = $(oDiv).offset().top;

          // 获取鼠标距离当前元素边界左边和上边距离

          let mouseToBorderX = clientX - offsetX;

          let mouseToBorderY = clientY - offsetY;

          // 获取元素width,height,left,top;注意元素为relative或absolute定位

          let width = $(oDiv).width();

          let height = $(oDiv).height();

          let left = parseFloat($(oDiv).css("left"));

          let top = parseFloat($(oDiv).css("top"));

          // 设置一下缩放幅度 ,值越大缩放的越快

          let ratio = e.originalEvent.deltaY < 0 ? 0.1 : -0.1;

          // 设置缩放后的宽高

          width = width * (1 + ratio);

          height = height * (1 + ratio);

          // 这里是关键一步

          // 可以想象,当元素宽度增加0.1倍,如果此时元素left值不变化，那么元素是会向右变大的，那么鼠标相

          // 对元素左上角的距离与最开始的距离相比就变了，变化量为mouseToBorderX*ratio，那么让元素left

          //  减去变化量即可保证鼠标相对元素不动

          left = left - mouseToBorderX * ratio;

          top = top - mouseToBorderY * ratio;

          if (width > 100) {
            $(oDiv).css({
              width: width + "px",

              height: height + "px",

              left: left + "px",

              top: top + "px",
            });
          }
        }
      );
    },
  },
  created: function () {
    this.getDicts("sd_direction").then((data) => {
      console.log(data, "方向");
      this.directionList = data.data;
    });
    // this.flvPlayer()
    this.trafficFlowLane();
    this.getEqTypeStateIcon();
    this.getWarnList();
    // this.$store.dispatch('app/toggleSideBar')
    getLocalIP().then((response) => {
      this.hostIP = response;
    });
    this.getUserDept();
    // this.getTunnelList();
    // this.selectEquipmentType()
    // this.initWebSocket()
    this.lightSwitchFunc();
    this.getEqType();
    this.getTunnel();
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    this.getDicts("sd_operation_log_state").then((response) => {
      this.operationStateOptions = response.data;
    });
    this.getDicts("brand").then((data) => {
      console.log(data, "设备厂商");
      this.brandList = data.data;
    });

    this.getDicts("sd_monitor_state").then((data) => {
      console.log(data, "设备类型");
      this.eqTypeDialogList = data.data;
    });
    this.getDicts("sd_wj_vehicle_type").then((data) => {
      console.log(data, "车型列表");
      this.vehicleTypeList = data.data;
    });
    this.getTunnelState();
    //调取滚动条
    this.srollAuto();
  },

  watch: {
    sdEventList(event) {
      // console.log(event, "websockt工作台接收事件弹窗");
      for (var item of event) {
        this.trafficList.unshift(item);
      }
    },
    radarDataList(event) {
      // console.log(event, "websockt工作台接收车辆感知事件数据");
      // 横纬竖经 lng 经度；   lat：纬度
      //       math.add(a+b)//加
      // math.subtract(a-b)//减
      // math.multiply(a*b)//乘
      // math.divide(a/b)//除
      const data = [
        { lng: 117.81632771, lat: 36.46771608 }, //下行入口左侧1
        { lng: 117.81710505, lat: 36.47502258 }, //下行出口左侧2
        { lng: 117.81641244, lat: 36.4677101 }, //下行入口右侧 基点4
        { lng: 117.81718759, lat: 36.47501931 }, //下行出口右侧3
      ];
      // 道路实际高度 / 二维的px数 = 1米所占的px数
      // this.heightRatio = math.divide(7 / 190);
      // let gao = math.subtract(+data[0].lat - +data[2].lat); //隧道宽度差(纬度)
      // console.log(gao, "gaogaogaogao");
      // 结束经度 - 开始经度 = 隧道经度的跨度 = 814 m
      // let chang = math.multiply(+data[3].lng - +data[2].lng); //A
      // 计算比例
      //1个经度代表的米数
      // let changB = math.divide(math.multiply(this.tunnelKm * 1000) / chang);
      // let gaoB = math.divide(+9 / +gao); //一个纬度代表的米数
      // console.log(gaoB, "gaoBgaoBgaoBgaoBgaoB"); //478
      for (let i = 0; i < event.length; i++) {
        //车辆实际经度
        var lng = Number(event[i].longitude);
        //车辆实际纬度
        var lat = event[i].latitude;
        // if (lng <= +data[0].lng || lat < +data[3].lat) {
        //   return;
        // }
        console.log(event[i].laneNum);
        //车辆实际距离入口距离
        var carKm = event[i].distance;
        // var carKm = math.multiply(math.subtract(lng - data[2].lng) * changB); //C
        // 车当前实际的高度
        // var carLat = math.multiply(math.subtract(+lat - +data[0].lat) * gaoB);
        // console.log(carLat, "carLatcarLatcarLat");
        //计算最终经度
        event[i].left =
          math.add(math.multiply(+carKm * this.proportion) + 60) + "px";
        //计算最终纬度
        // event[i].top =
        //   math.add(
        //     math.divide(math.multiply(+carLat * this.heightRatio), 20.3) + 340
        //   ) + "px";
        // console.log(math.multiply(+carLat * this.proportion), "实际left值");
        // console.log(event[i].top, "event[i].topevent[i].top");
        // 根据车道数进行判断
        if (this.lane == 2) {
          console.log(this.lane, "车道");
          if (event[i].laneNum == 1) {
            event[i].top = 360 + "px";
          } else if (event[i].laneNum == 2) {
            event[i].top = 450 + "px";
          }
        }
        if (
          event[i].vehicleType == "3" ||
          event[i].vehicleType == "7" ||
          event[i].vehicleType == "8"
        ) {
          event[i].background = "yellow";
        } else {
          event[i].background = "yellow";
        }
        if (event[i].speed > Number(90) || event[i].speed < Number(60)) {
          event[i].background = "red";
        }
      }
      this.carList = event;
      console.log(this.carList);
    },
    deviceStatus(event) {
      this.deviceStatusList = event;
    },
    // 设备类型
    "batchForm.eqType"(val) {
      console.log(val);
      console.log(mode);
      if (mode == "buttonSelection") {
        let param = {
          eqTunnelId: this.currentTunnel.id,
          eqType: val,
          lane: this.batchForm.eqlane,
          eqDirection: this.batchForm.eqDirection,
        };
        this.selectEquipment(param);
        this.selectDirection(param);
        this.selectLane(param);
      } else {
        for (let i = 0; i < boxEqList.length; i++) {
          if (val == boxEqList[i].typeId) {
            this.devicesList = boxEqList[i].eqlist;
          }
        }
      }
    },
    // 方向
    "batchForm.eqDirection"(val) {
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqType: this.batchForm.eqType,
        eqDirection: val,
        lane: this.batchForm.eqlane,
      };
      this.selectEquipment(param);
    },
    // 车道
    "batchForm.eqlane"(val) {
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqDirection: this.batchForm.eqDirection,
        lane: val,
      };
      this.selectLane(param);
    },
    batchVisible(val) {
      if (val == false) {
        this.cleanUp();
      }
    },
    stateSwitchVisible(val) {
      if (val == false) {
        this.cleanUp();
      }
    },
    w(val) {
      this.move = true;
    },
    // 监听隧道温度
    temperatureList: {
      handler(val) {
        for (var item of val) {
          if (item.temperature >= 50) {
            this.temperature = item.temperature;
            this.tunnelName = item.tunnelName;
            this.HZdialogVisible = true;
            // 当设备异常时
            this.$refs.audio.currentTime = 0; //从头开始播放提示音
            this.$refs.audio.play(); //播放
          } else {
            this.HZdialogVisible = false;
          }
        }
      },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true,
    },
    // 监听声音开关按钮
    alarmBell: {
      handler(val) {
        if ((val = true)) {
          this.$refs.audio.currentTime = 0; //从头开始播放提示音
          this.$refs.audio.play(); //播放
          // 谷歌浏览器禁止一打开就播放声音 所以需要通过开关打开声音
          // 打开后会声音直接播放 所以设置10毫秒 用户就不会听到了
          setTimeout(() => {
            this.$refs.audio.pause();
            this.$refs.audio.load();
          }, 10);
        } else {
          this.$refs.audio.pause();
          this.$refs.audio.load();
        }
      },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true,
    },
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
    // 监听属性 类似于data概念
    optionLeft() {
      return {
        step: 0.4,
        direction: 1, // 0向下 1向上 2向左 3向右
        limitMoveNum: 2,
        waitTime: 1000,
      };
    },
    navigationHeight() {
      this.topNav = this.$store.state.settings.topNav;
      const needTagsView = this.$store.state.settings.tagsView;
      let h = 0;
      h += 72;
      // if (!this.topNav) {
      //   if (needTagsView) h += 22;
      // }

      return h;
    },
    ...mapState({
      deviceStatus: (state) => state.websocket.deviceStatus,
      radarDataList: (state) => state.websocket.radarDataList,
      sdEventList: (state) => state.websocket.sdEventList,
    }),
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
  },
  mounted() {
    this.initEnergyConsumption();
    this.getTimeData();
    // this.vehicleEcharts()
    // this.specialEcharts()
    let that = this;
    window.onresize = () => {
      return (() => {
        this.windowHeight = document.documentElement.clientHeight;
      })();
    };

    // 隧道调取数据两秒一次
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 0);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);

    // 模拟实时隧道温度 调完了再删
    // setInterval(()=>{
    //         this.temperatureList[1].temperature += 2

    // },2000)
    // setInterval(()=>{
    //         this.temperatureList[1].temperature -= 20

    // },22000)
    // console.log(this.temperatureList[1].temperature,"隧道温度")
    // 一进页面判断温度超过50度 弹窗提示
    // for (var item of this.temperatureList) {
    //   if (item.temperature >= 50) {
    //     this.HZdialogVisible = true
    //     this.temperature = item.temperature
    //     this.tunnelName = item.tunnelName
    //   }
    // }
    // this.initeChartsEnd();
    // this.loadFocusCar();
    // this.srollAuto()
  },
  methods: {
    // 批量操作弹窗获取方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 批量操作 弹窗确定
    batchManageOK() {
      const param = {
        eqId: this.itemEqId,
        eqDirection: this.batchManageForm.eqDirection,
        state: this.batchManageForm.state,
      };
      batchManage(param).then((res) => {});
    },
    // 新版批量操作 点击变俩按钮
    batchManage() {
      this.batchManageType = 2;
      this.batchManageList = [];
      this.addBatchManage = true;
    },
    // 批量操作 执行
    implementBatchManage() {
      this.title = "批量操作";
      for (var item of this.selectedIconList) {
        if (item.click) {
          this.batchManageList.push(item);
          this.batchManageDialog = true;
        }
      }
    },
    // 关闭批量操作弹窗 / 批量操作取消
    closeBatchManageDialog() {
      this.batchManageDialog = false;
      this.batchManageType = 1;
      this.itemEqId = [];
      this.itemEqType = "";
      this.addBatchManage = false;
      for (var item of this.selectedIconList) {
        item.click = false;
        this.$forceUpdate();
      }
    },
    // 筛选设备名称
    screenEqNameButton() {
      console.log(this.screenEqName);
      for (var item of this.selectedIconList) {
        if (item.eqName.indexOf(this.screenEqName)) {
          console.log(item, "itemitem");
        }
      }
    },
    // 抽屉车指批量控制 车道下拉框
    getTunnelLane() {
      this.chezhiLaneList = [];
      if (this.tunnelLane == 1) {
        this.chezhiLaneList = this.chezhiLaneList1;
      } else if (this.tunnelLane == 2) {
        this.chezhiLaneList = this.chezhiLaneList2;
      } else if (this.tunnelLane == 3) {
        this.chezhiLaneList = this.chezhiLaneList3;
      }
    },
    // 抽屉车指批量控制 状态下拉框
    getTunnelState() {
      const param = {
        stateTypeId: 1,
        isControl: 1,
      };
      getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
        // this.chezhiStateList = response.rows;
        this.chezhiStateList = [];
        for (let i = 0; i < response.rows.length; i++) {
          let iconUrl = [];
          if (response.rows[i].iFileList != null) {
            for (let j = 0; j < response.rows[i].iFileList.length; j++) {
              let img = response.rows[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          this.chezhiStateList.push({
            deviceState: response.rows[i].deviceState,
            stateName: response.rows[i].stateName,
            url: iconUrl,
          });
        }
      });
    },
    // 控制按钮
    chezhiControl(num) {
      this.chezhiDisabled = true;
      const param = {
        tunnelId: this.tunnelId,
        direction: num,
        state: this["chezhiForm" + num].state,
        lane: this["chezhiForm" + num].lane,
      };
      batchControlCarFinger(param).then((res) => {
        console.log(res);
        if (res.data == 0) {
          this.$modal.msgWarning("控制失败");
        } else if (res.data == 1) {
          this.$modal.msgSuccess("控制成功");
        }
        this.chezhiDisabled = false;
      });
    },
    getDeviceDataAndStateData() {
      getDeviceDataAndState(this.tunnelId).then((result) => {
        console.log(result, "批量控制");
        this.BulkData = result.data;
      });
    },
    // 定时控制
    timingControl() {
      const param = {
        tunnelId: this.tunnelId,
        strategyType: 3,
      };
      timingStrategyList(param).then((res) => {
        console.log(res);
      });
    },
    timingStrategy(item) {
      var time = item.arr.join("-");
      updateControlTime(item.strategy_id, time).then((res) => {
        this.$modal.msgSuccess("修改时间成功");
      });
    },
    timStrategySwitch(item) {
      timeStrategySwitch(item.strategy_id, item.strategy_state).then((res) => {
        if (item.strategy_state == 0) {
          this.$modal.msgSuccess("开启成功");
        } else if (item.strategy_state == 1) {
          this.$modal.msgSuccess("关闭成功");
        }
      });
    },
    // // 抽屉 车指控制
    // controlCheZhi(num) {
    //   console.log(num, "num");
    //   console.log(this.checkList1, "checkList1");
    //   console.log(this.checkList2, "checkList2");
    //   // 上传成功后记得把this.checkList清空
    // },
    // 预警事件点击跳转应急调度
    jumpYingJi(num) {
      bus.$emit("openPicDialog");
      bus.$emit("getPicId", num);

      console.log(num, "num");
    },
    // 车型通过字典表获取值
    getCheXing(num) {
      for (var item of this.vehicleTypeList) {
        if (num == Number(item.dictValue)) {
          return item.dictLabel;
        }
      }
    },
    // 关闭弹窗子组件
    dialogClose() {
      this.eqInfo.clickEqType = 0;
      this.mouseoversImplement = true;
    },
    // 车辆监测数据
    vehicleEcharts() {
      // console.log(this.tunnelId,"this.tunnelIdthis.tunnelIdthis.tunnelId")
      const param = {
        tunnelId: this.tunnelId,
      };
      vehicleMonitoringInRecent24Hours(param).then((res) => {
        console.log(res, "车辆监测数据");
        var vehicleXData = [];
        var vehicleYData = [];
        for (var item of res.data) {
          vehicleXData.push(item.hour);
          vehicleYData.push(item.count);
        }
        this.initeChartsEnd(vehicleXData, vehicleYData);
      });
    },
    // 重点车辆监测数据
    specialEcharts() {
      const param = {
        tunnelId: this.tunnelId,
      };
      special(param).then((res) => {
        console.log(res, "重点车辆监测数据");
        this.keyVehiclesXData = res.data[0];
        this.keyVehiclesYData = res.data[1];
      });
    },
    // 滚动条动画
    srollAuto() {
      if (this.mouseoversImplement == false) {
        return;
      }
      var parent = document.getElementsByClassName("content");
      // console.log(parent,'parentparent')
      clearInterval(this.imageTimer);
      this.imageTimer = setInterval(() => {
        // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
        if (
          Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
          parent[0].scrollWidth
        ) {
          clearInterval(this.imageTimer);
          this.imageTimer = setInterval(() => {
            parent[0].scrollLeft--;
            if (
              Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
              parent[0].clientWidth
            ) {
              this.srollAuto();
              // console.log(123213,'12321')
            }
          }, 20);
        } else {
          parent[0].scrollLeft++;
        }
      }, 20);
    },

    mouseoversImage() {
      //  console.log(this.imageTimer,'清定时器')
      clearInterval(this.imageTimer);
      this.imageTimer = null;
    },
    mouseleaveImage() {
      // console.log('离开了')
      this.srollAuto();
    },
    //抽屉
    isDrawerA() {
      this.drawerA = true;
      this.drawerB = false;
      this.drawerCVisible = false;
    },
    isDrawerB() {
      this.drawerB = true;
      this.drawerA = false;
      this.drawerCVisible = false;
      if (this.tunnelId) {
        timeSharing(this.tunnelId).then((res) => {
          for (var item of res.data) {
            item.arr = item.time.split("-");
            console.log(item, "item");
          }
          this.timStrategyList = res.data;
          console.log(this.timStrategyList, "this.timStrategyList");
        });
      }
    },
    isDrawerC() {
      this.drawerCVisible = true;
      this.drawerA = false;
      this.drawerB = false;

      listStrategy({
        strategyType: 2,
        tunnelId: this.currentTunnel.id,
      }).then((response) => {
        console.log(response, "自动触发抽屉");
        this.isDrawerCList = response.rows;
      });
    },
    zoomSwitchChange(val) {
      console.log(val, "val");
      if (val == false) {
        this.handleTableWheelSwithch = false;
      } else {
        this.handleTableWheelSwithch = true;
      }
    },
    async flvPlayer() {
      if (flvjs.isSupported()) {
        var video = document.getElementsByClassName("videoElement");
        console.log(video[0], "video");
        if (video) {
          //创建播放器实例
          var player = flvjs.createPlayer({
            type: "flv",
            isLive: true,
            hasAudio: false,
            url: `http://10.166.139.12:8081/live/22456.flv`,
          });
          player.attachMediaElement(video);
          try {
            player.load();
            player.play();
          } catch (error) {
            console.log(error);
          }
        }
      }
    },
    // 交通流状况 拥堵状况
    trafficFlowLane() {
      trafficFlowInformation().then((response) => {
        this.trafficJamList = response.data;
        for (var item of response.data) {
          console.log(
            item.left,
            "left",
            item.width,
            "width",
            item.color,
            "color",
            item.tunnelId,
            item.type
          );
        }
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询操作日志列表 */
    getList() {
      this.loading = true;
      listLog(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          console.log(response, "操作日志列表");
          this.logList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 控制方式   3：手动 1：时间控制 2：光强控制字典翻译
    controlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    },
    //操作是否成功 0：成功 1：失败
    stateFormat(row, column) {
      return this.selectDictLabel(this.operationStateOptions, row.state);
    },
    openTooltip(item, index) {
      this.showTooltipIndex = index;
      // this.showTooltip = true;
      this.sensorDisabled(item);
    },
    closeTooltip(item) {
      this.showTooltipIndex = 999;
      // this.showTooltip = false
    },
    increase() {
      this.percentage += 10;
      if (this.percentage > 100) {
        this.percentage = 100;
      }
    },
    decrease() {
      this.percentage -= 10;
      if (this.percentage < 0) {
        this.percentage = 0;
      }
    },
    controlHeight(item) {
      console.log(item, "item");
    },
    getWarnList() {
      const param = {
        eventState: "3",
      };
      // getSystemWarningMsg().then((res) => {
      //   console.log(res, "系统预警")
      //   res.data.forEach(val => {
      //     if (val) {
      //       this.SysEarlyWarning = res.data
      //     }
      //   })
      // })
      // getTrafficIncident().then((response) => {
      //   this.trafficList = response.data
      // })

      getWarnEvent(param).then((response) => {
        // console.log(response.data,"预警事件")
        this.trafficList = response.data;
      });
    },
    /** 查询部门列表 */
    getDeptList() {
      var that = this;
      var id = this.tunnelQueryParams.deptId;
      var iid = "";
      const params = {
        status: 0,
      };
      listDept(params)
        .then((response) => {
          var list = that.handleTree(response.data, "deptId");
          console.log(list, "级联选择器格式");
          function a(list) {
            list.forEach((item) => {
              if (item.deptId == id) {
                that.siteList = item.children || [];
              } else {
                item.children && a(item.children);
              }
            });
          }

          a(list);
        })
        .then(() => {
          that.siteList.filter((item) => {
            if (item.children) {
              item.children.forEach((val) => {
                iid ? "" : (iid = val.deptId);
                that.tunnelQueryParams.deptId == iid
                  ? ""
                  : (that.tunnelQueryParams.deptId = iid);
              });
            } else {
              iid ? "" : (iid = item.deptId);
              that.tunnelQueryParams.deptId == iid
                ? ""
                : (that.tunnelQueryParams.deptId = iid);
            }
          });
        })
        .then(() => {
          // 获取隧道
          that.getTunnelList();
          that.siteList.length == 0 ? (that.isManagementStation = true) : "";
        });
    },
    // 获取最近24小时时间数组
    getTimeData() {
      //获取当前时间-24小时
      let nows = new Date();
      let hourArr = getLastDayHour(nows, 12);

      function getLastDayHour(d, s) {
        let result = [];
        d.setHours(d.getHours());
        for (var i = 0; i < s; i++) {
          d.setHours(d.getHours() - 1);
          var h = d.getHours() + ":00";
          result.push(h);
        }
        return result.reverse();
        console.log();
      }

      return hourArr;
    },
    // 改变站点
    changeSite(index) {
      if (index) {
        this.tunnelQueryParams.deptId = index[index.length - 1];
        this.getTunnelList();
        // this.srollAuto()
      }
    },
    // 机器人tabs页
    robotTabsClick(tab, event) {
      var that = this;
      if (tab.paneName == "stateRecords") {
        this.robotChartDom ||
          (this.robotChartDom = echarts.init(
            document.getElementById("robotEchart")
          ));
        that.$nextTick(() => {
          that.robotEchartInit();
        });
      }
    },
    // 机器人状态记录图表初始化
    robotEchartInit() {
      this.robotStateData || (this.robotStateData = [0, 1, 2]);
      this.robotXData || (this.robotXData = ["8:30", "10:30", "11:30"]);
      this.robotChartDom ||
        (this.robotChartDom = echarts.init(
          document.getElementById("robotEchart")
        ));
      this.robotEchart();
      this.robotChartDom.resize();
    },
    robotEchart() {
      this.robotOption = {
        // title: {
        //   text: '运行状态',
        //   textStyle: {
        //     color: '#fff'
        //   }
        // },
        tooltip: {
          trigger: "axis",
          formatter: function (value) {
            for (var j = 0; j < value.length; j++) {
              if (value[j].value == 0) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 正常"
                );
              } else if (value[j].value == 1) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 离线"
                );
              } else if (value[j].value == 2) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 故障"
                );
              }
            }
          },
          textStyle: {
            color: "powderblue",
          },
          backgroundColor: "rgba(0,0,0,0.5)",
          extraCssText: "box-shadow: 0px 0px 10px rgb(104 181 255);",
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          top: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: this.robotXData,
          axisLine: {
            lineStyle: {
              color: "#fff",
            },
          },
        },
        yAxis: {
          type: "category",
          axisLine: {
            lineStyle: {
              color: "#fff",
            },
          },
          data: [
            {
              value: "正常",
              textStyle: {
                fontSize: 20,
                color: "yellow",
              },
            },
            {
              value: "离线",
              textStyle: {
                fontSize: 20,
                color: "#2c5ea2",
              },
            },
            {
              value: "故障",
              textStyle: {
                fontSize: 20,
                color: "red",
              },
            },
          ],
          axisLine: {
            show: true,
          },
          textStyle: {
            color: "#fff",
          },
        },
        series: [
          {
            name: "运行状态",
            type: "line",
            step: "end",
            data: this.robotStateData,
          },
        ],
      };
      this.robotOption && this.robotChartDom.setOption(this.robotOption);
    },

    // 缩略图开关
    changeThumbnail(val) {
      console.log(val, "val");
      var vehicleLane = document.getElementsByClassName("vehicleLane");
      var footer = document.getElementsByClassName("footer");
      console.log(vehicleLane, "vehicleLane");
      console.log(footer, "footer");
      if (!val) {
        vehicleLane[0].style.height = "87%";
        footer[1].style.height = "0%";
      } else {
        console.log(footer);
        vehicleLane[0].style.removeProperty("height");
        this.initEharts();
      }
    },
    // 初始化echart图
    initEharts() {
      var that = this;
      proportionOfEquipment({
        tunnelId: that.currentTunnel.id,
      }).then((res) => {
        console.log(res, "设备类型占比");
        // that.initechartsB(res.data)
      });
      // that.initeChartsEnd()
      // that.loadFocusCar()
    },
    // 获取最近七天数组
    dateFormat(dateData) {
      // let times = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
      // console.log(times,"times")
      var now = new Date(dateData),
        // y = now.getFullYear(),//年份
        m = now.getMonth() + 1, //月份
        d = now.getDate(); //日期
      return (m < 10 ? "0" + m : m) + "." + (d < 10 ? "0" + d : d);
    },

    // 能耗监测echarts
    initEnergyConsumption(sevenDaysBefore) {
      // 获取最近10天
      var sevenDaysBefore = ["", "", "", "", "", "", "", "", "", ""];
      var now = new Date(); //获取当前时间
      var nowMs = now.getTime(); //获取当前时间的毫秒数
      sevenDaysBefore[9] = this.dateFormat(nowMs - 1000 * 60 * 60); //前几天，n就取几，整数
      sevenDaysBefore[8] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(1)
      ); //前几天，n就取几，整数
      sevenDaysBefore[7] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(3)
      ); //前几天，n就取几，整数
      sevenDaysBefore[6] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(2)
      ); //前几天，n就取几，整数
      sevenDaysBefore[5] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(4)
      ); //前几天，n就取几，整数
      sevenDaysBefore[4] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(5)
      ); //前几天，n就取几，整数
      sevenDaysBefore[3] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(6)
      ); //前几天，n就取几，整数
      sevenDaysBefore[2] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(7)
      ); //前几天，n就取几，整数
      sevenDaysBefore[1] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(8)
      ); //前几天，n就取几，整数
      sevenDaysBefore[0] = this.dateFormat(
        nowMs - 1000 * 60 * 60 * 24 * parseInt(9)
      ); //前几天，n就取几，整数
      var energyConsumption = echarts.init(
        document.getElementById("energyConsumption")
      );
      // var areaList = ['姚家峪隧道', '毓秀山隧道', '洪河隧道', '滨莱高速', '望海石隧道','中庄隧道','马公祠隧道', '乐疃隧道','樵岭前隧道','佛羊岭隧道','迎春坡隧道','龙山寨隧道'];
      var safetyArr = [
        1450, 1650, 1500, 1430, 1580, 1530, 1580, 1460, 1400, 1540,
      ];
      var option = {
        // backgroundColor: '#00043A',
        tooltip: {
          trigger: "axis",
          show: true,
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: "{b}<br>能耗： {c} ",
        },
        legend: {
          show: false,
        },
        grid: {
          left: "10%",
          right: "10%",
          bottom: "8%",
          top: "20%",
          containLabel: true,
        },
        xAxis: [
          {
            // name:'日',
            type: "category",
            // boundaryGap : false,
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },

            axisTick: {
              show: false,
            },
            // splitLine:{
            //   show:true,
            //   lineStyle:{
            //     color:'#195384'
            //   }
            // },
            data: sevenDaysBefore,
          },
        ],
        yAxis: {
          name: "kw-h",
          nameTextStyle: {
            color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
            padding: [10, 20, 0, -40],
          },
          splitLine: {
            show: false,
          },
          axisLabel: {
            formatter: "{value}",
            textStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              fontSize: 10,
            },
          },
          axisLine: {
            show: false,
            // lineStyle:{
            //   color:'#0a88bd'
            // }
          },
          // offset:20,
        },
        series: [
          {
            type: "bar",
            barWidth: 12, //柱图宽度
            itemStyle: {
              normal: {
                barBorderRadius: [6, 6, 0, 0],
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#499eff", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#838eff", // 100% 处的颜色
                    },
                  ],
                  globalCoord: true, // 缺省为 false
                },
              },
            },
            data: safetyArr,
          },
        ],
      };
      energyConsumption.setOption(option);
      window.addEventListener("resize", function () {
        energyConsumption.resize();
      });
    },
    initeChartsEnd(vehicleXData, vehicleYData) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        //	此dom为echarts图标展示dom
        var vehicle = echarts.init(document.getElementById("vehicle"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          // legend: {
          //   show: true,
          //   icon: "rect",
          //   itemWidth: 10,
          //   itemHeight: 10,
          //   x: 'center',
          //   data: ['客车', '货车', '专项车'],
          //   textStyle: { //图例文字的样式
          //     color: this.sideTheme!='theme-blue'?'#fff':'#003a5d',
          //     fontSize: 12
          //   }
          // },
          calculable: true,
          grid: {
            top: "24%",
            bottom: "20%",
            left: "14%",
            right: "14%",
          },
          xAxis: [
            {
              name: "小时",
              type: "category",
              axisTick: {
                show: false,
              },
              splitLine: {
                show: false,
              },
              boundaryGap: false,
              axisLabel: {
                textStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                },
              },
              axisLine: {
                show: true,
                lineStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                },
              },
              data: vehicleXData,
            },
          ],
          yAxis: [
            {
              name: "总车量",
              nameTextStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
                padding: [0, 20, 0, 0],
              },
              type: "value",
              minInterval: 1,
              // min: 0,
              // max: 200,
              axisTick: {
                show: false,
              },
              splitLine: {
                show: true,
                lineStyle: {
                  //分割线的样式
                  color: ["rgba(0,0,0,0.3)"],
                  width: 1,
                  type: "dashed",
                },
              },
              axisLine: {
                show: false,
              },
              axisLabel: {
                textStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                },
              },
            },
          ],
          series: [
            {
              name: "车辆总数",
              type: "line",
              color: "#59c5f9",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              // 渐变色
              areaStyle: {
                normal: {
                  //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#59c5f9",
                    },
                    {
                      offset: 1,
                      color: "rgba(89,197,249,0.3)",
                    },
                  ]),
                },
              },
              data: vehicleYData,
            },
            // {
            //   name: '货车',
            //   type: 'line',
            //   color: '#db72a7',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   stack: 'Total',
            //   areaStyle: {},
            //   emphasis: {
            //     focus: 'series'
            //   },
            //   //渐变色
            //   areaStyle: {
            //     normal: {
            //       //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            //       color: new echarts.graphic.LinearGradient(
            //         0, 0, 0, 1,
            //         [{
            //             offset: 0,
            //             color: '#db72a7'
            //           },
            //           {
            //             offset: 1,
            //             color: 'rgba(219,114,167,0.3)'
            //           }
            //         ]
            //       )
            //     }
            //   },
            //   data: [90, 70, 50, 30, 80, 90, 30, 60, 70, 80, 90, 20]
            // }, {
            //   name: '专项车',
            //   type: 'line',
            //   color: '#ffb600',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   data: [20, 30, 40, 50, 70, 80, 90, 60, 40, 30, 20, 60]
            // },
          ],
        };
        vehicle.setOption(option);
        window.addEventListener("resize", function () {
          vehicle.resize();
        });
      });
    },
    loadFocusCar() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      newPromise.then(() => {
        var focusCar = echarts.init(document.getElementById("focusCar"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          toolbox: {
            show: true,
            feature: {
              // magicType: { show: true, type: ['stack', 'tiled'] },
              // saveAsImage: { show: true }
            },
          },
          grid: {
            top: "20%",
            bottom: "18%",
            left: "14%",
            right: "14%",
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            data: this.keyVehiclesXData,
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "辆",
            nameTextStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            minInterval: 1, //y轴的刻度只显示整数
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["rgba(0,0,0,0.3)"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              type: "line",
              color: "#00c8ff",
              symbol: "none",
              smooth: true,
              stack: "Total",
              areaStyle: {},
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              emphasis: {
                focus: "series",
              },
              //渐变色
              areaStyle: {
                normal: {
                  //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#33b0ee",
                    },
                    {
                      offset: 1,
                      color: "rgba(51,176,238,0.3)",
                    },
                  ]),
                },
              },
              data: this.keyVehiclesYData,
            },
          ],
        };

        focusCar.setOption(option);
        window.addEventListener("resize", function () {
          focusCar.resize();
        });
      });
    },
    // 情报板编辑
    IntelligenceBoardEdit(item) {
      console.log(item);
      this.dialogVisible = true;
      // this.$refs.contentBatchEdit.deviceId = item.id;
      this.$refs.contentBatchEdit.vmsSize = "1024*768"; //item.vmsSize
      // this.$refs.contentBatchEdit.list = item.item;
      // this.$refs.vmsContentUpdate.add = true;
      this.$refs.vmsContentUpdate.init();
    },
    // 火灾报警弹窗
    HZhandleClose() {
      this.HZdialogVisible = false;
    },
    handleClosee() {
      this.cameraVisible = false;
    },
    controlHeightClose() {
      this.controlHeightVisible = false;
    },
    showA() {
      this.carShow = !this.carShow;
      // this.carMapSportCar();
      // this.carMapRight();
    },
    showB() {
      this.carShowTwo = !this.carShowTwo;
      // this.carMapLeft();
    },
    // 跑车
    carMapSportCar() {
      clearInterval(this.timer);
      const car = this.$refs["sportCar"];
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              item.url[0] = this.dark;
            }
          }
          this.carShow = false;
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              if (car.style.left == "100px") {
                this.carShow = false;
              }
              if (parseInt(car.style.left) - item.position["left"] <= 150) {
                item.url[0] = this.light;
              }
              if (item_1.position.top == 156) {
                if (parseInt(car.style.left) - item.position["left"] <= -150) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    // 下行车道
    carMapLeft() {
      clearInterval(this.timer);
      const car = this.$refs["carLeft"];
      let speed = 210;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) >= 1130) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 0) ||
              (item.eqType == 8 && item.eqDirection == 0) ||
              (item.eqType == 9 && item.eqDirection == 0)
            ) {
              item.url[0] = this.dark;
            }
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 0) ||
              (item.eqType == 8 && item.eqDirection == 0) ||
              (item.eqType == 9 && item.eqDirection == 0)
            ) {
              // 到达终点隐藏
              if (car.style.left == "1130px") {
                this.carShow = false;
                return false;
              }
              // 如果车辆当前位置和路灯位置相差小于100，则亮灯
              if (parseInt(car.style.left) - item.position["left"] <= -100) {
                item.url[0] = this.light;
              }
              if (item_1.position.top != 0) {
                // 如果车辆位置 - 当前路灯位置 大于等于 100 则代表车辆已经离开当前路灯区域，则灭灯
                if (parseInt(car.style.left) - item.position["left"] >= 100) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) + 24 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    // 上行车道
    carMapRight() {
      clearInterval(this.timer);
      const car = this.$refs["carRight"];
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              item.url[0] = this.dark;
            }
          }
          this.carShow = false;
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              if (car.style.left == "100px") {
                this.carShow = false;
              }
              if (parseInt(car.style.left) - item.position["left"] <= 150) {
                item.url[0] = this.light;
              }
              if (item_1.position.top == 156) {
                if (parseInt(car.style.left) - item.position["left"] <= -150) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        console.log(response, "管理站级联");
        this.userDeptId = response.rows[0].deptId;
        this.tunnelQueryParams.deptId = response.rows[0].deptId;
        this.getDeptList();
      });
    },
    //右键拖动
    dragImg(e) {
      console.log(e, "e");
      if (e.button == 0) {
        return;
      }
      this.dragFlag = true;
      this.mouseLeft = e.clientX - parseInt(this.$refs.dragImgDom.offsetLeft);
      this.mouseTop = e.clientY - parseInt(this.$refs.dragImgDom.offsetTop);
      document.onmousemove = (e) => {
        if (this.dragFlag) {
          this.curX = e.clientX - this.mouseLeft;
          this.curY = e.clientY - this.mouseTop;
          this.$refs.dragImgDom.style.left = this.curX + "px";
          this.$refs.dragImgDom.style.top = this.curY + "px";
        }
      };
      document.onmouseup = () => {
        this.dragFlag = false;
      };
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        console.log(response, "responseresponseresponse");
        this.lightSwitch = response.msg;
      });
    },

    /* -------------------鼠标拖动-------------------*/
    /* 鼠标点击*/
    dian(event) {
      if (event.button != 0) {
        return;
      }
      // clientX 和 clientY 是起点
      let parentObj = document.getElementById("eq-wrapper");
      wrapperClientX = parentObj.getBoundingClientRect().left;
      wrapperClientY = parentObj.getBoundingClientRect().top;
      this.px =
        event.clientX + event.clientX * this.moveTop - wrapperClientX - 30;
      this.py = event.clientY + event.clientY * this.moveTop - wrapperClientY;
      boxEqList = [];
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
    },
    /*鼠标移动*/
    yi(event) {
      // console.log(event)
      if (this.px == "" || this.py == "") {
        return;
      }
      let px1 = this.px;
      let px2 = this.py;
      this.left = event.clientX + event.clientX * this.moveTop - wrapperClientX;
      this.top = event.clientY + event.clientY * this.moveTop - wrapperClientY;

      this.h = this.top - this.py;
      this.w = this.left - this.px;
      let hc = -this.h;
      let wc = -this.w;
      this.len = 1.5;
      this.back = "#1E90FF";
      if (this.h < 0 && this.w >= 0) {
        this.h = hc;
        this.left = px1;
      } else if (this.h >= 0 && this.w < 0) {
        this.w = wc;
        this.top = px2;
      } else if (this.h < 0 && this.w < 0) {
        this.h = hc;
        this.w = wc;
      } else {
        this.left = this.px;
        this.top = this.py;
      }
      if (this.w < 0) {
        this.w = 0 - this.w;
      }
      if (this.h < 0) {
        this.h = 0 - this.h;
      }
    },
    /* 鼠标离开*/
    li() {
      this.move = false;
      let str = [108, 110, 18, 21, 13, 14, 15, 16, 5, 6, 7, 8, 9];
      let list = this.selectedIconList;
      for (let i = 0; i < list.length; i++) {
        // 传入每个设备并判断是否在范围之内
        let inRange = this.inRange(list[i]);
        if (inRange == true) {
          //判断当前圈选设备的remark字段中是否为null，且是否包含“hjpz”，如果不包含，则返回-1，证明是正常设备，则可以显示圈选效果
          const remark = this.selectedIconList[i].remark;
          if (remark != null && remark.indexOf("hjpz") == -1) {
            this.selectedIconList[i].isfocus = true;
          }
          // debugger
          // this.selectedIconList[i].isfocus = true;
          if (boxEqList.length > 0) {
            let index = -1;
            for (let k = 0; k < boxEqList.length; k++) {
              if (list[i].eqType == boxEqList[k].typeId) {
                index = k;
                break;
              }
            }
            if (index > -1) {
              boxEqList[index].eqlist.push(list[i]);
            } else {
              if (!str.includes(parseInt(list[i].eqType))) {
                boxEqList.push({
                  typeId: list[i].eqType,
                  eqlist: [list[i]],
                });
                this.boxType(list[i].eqType);
              }
            }
          } else {
            if (!str.includes(parseInt(list[i].eqType))) {
              boxEqList.push({
                typeId: list[i].eqType,
                eqlist: [list[i]],
              });
              this.boxType(list[i].eqType);
            }
          }
        } else {
          // console.log('bu在范围内')
        }
      }
      if (boxEqList.length > 0 && this.boxTypeList.length > 0) {
        if (boxEqList.length == 1 && boxEqList[0].eqlist.length == 1) {
          //单个配置
          // console.log(boxEqList[0].eqlist[0])
          this.openStateSwitch(boxEqList[0].eqlist[0]);
        } else {
          //超过1个设备进行批量配置
          this.batchForm.eqType = this.boxTypeList[0].typeId;
          console.log(this.batchForm.eqType, "批量操作设备类型");
          let exist = true; //假设获取到所有信息
          boxEqList = boxEqList.filter((e) => e.typeId != undefined);
          for (let b = 0; b < boxEqList.length; b++) {
            for (let bl = 0; bl < boxEqList[b].eqlist.length; bl++) {
              if (
                boxEqList[b].eqlist[bl].eqDirection == undefined ||
                boxEqList[b].eqlist[bl].eqDirection == null ||
                boxEqList[b].eqlist[bl].typeName == undefined
              ) {
                exist = false; //取不到设备信息，不可配置
                this.$message({
                  message: "未能获取所有设备信息,请重新配置！",
                  type: "warning",
                  customClass: "workbench-msg",
                });
                this.cleanUp();
                this.empty();
                break;
              }
              if (exist == false) {
                break;
              }
            }
          }
          if (exist == true) {
            mode = "boxSelection";
            this.title = "批量操作";
            if (this.$refs["batchForm"]) {
              this.$refs["batchForm"].resetFields();
            }
            this.devicesList = this.changeDirection(boxEqList[0].eqlist);
            let param = {
              eqTunnelId: this.currentTunnel.id,
              eqType: this.devicesList[0].eqType,
              lane: this.batchForm.eqlane,
              eqDirection: this.batchForm.eqDirection,
            };
            this.selectDirection(param);
            this.batchVisible = true;
          }
        }
      }
      this.empty();
    },
    boxType(type) {
      for (let i = 0; i < this.eqTypeList.length; i++) {
        if (type == this.eqTypeList[i].typeId || type == 100) {
          this.boxTypeList.push({
            typeId: type,
            typeName: this.eqTypeList[i].typeName,
          });
        }
      }
    },
    // 获取风机状态
    getFjDisabled(item, state) {
      // 主机
      if (item.eqType == "10") {
        // item.state是当前状态 选择状态
        if (item.fjState == "1" && state == "2") {
          return true;
        }
        //
        else if (item.fjState == "2" && state == "1") {
          return true;
        }
        //
        else {
          return false;
        }
      } else {
        return false;
      }
    },
    /* 是否在范围内*/
    inRange(eqIcon) {
      let inRange = false;
      if (eqIcon.display == true) {
        let maxX = Math.max(this.left + this.w, eqIcon.position.left + 60);
        let maxY = Math.max(this.top + this.h, eqIcon.position.top - 100 + 30);
        let minX = Math.min(this.left, eqIcon.position.left);
        let minY = Math.min(this.top, eqIcon.position.top - 100);
        if (maxX - minX <= this.w + 10 && maxY - minY <= this.h + 20) {
          inRange = true;
        } else {
          inRange = false;
        }
      }
      return inRange;
    },
    empty() {
      this.px = "";
      this.py = "";
      this.h = "";
      this.w = "";
      this.top = "";
      this.left = "";
      this.len = 0;
      this.back = "";
    },
    /* 关闭窗口，清空框选*/
    cleanUp() {
      this.empty();
      this.boxTypeList = [];
      this.move = false;
      mode = "";
      //清除聚焦
      let list = this.selectedIconList;
      for (let i = 0; i < list.length; i++) {
        if (list[i].hasOwnProperty("isfocus")) {
          delete this.selectedIconList[i].isfocus;
        }
      }
    },
    // onmousemove(e) {
    //   let et = e || window.event;
    //   et.preventDefault(); // 阻止默认事件发生
    // },
    /* -------------------鼠标拖动end------------------*/

    /* 查询隧道列表 */
    getTunnelList() {
      listTunnels(this.tunnelQueryParams).then((response) => {
        console.log(response, "查询隧道列表");
        if (!response.rows[0]) {
          this.tunnelList = [];
          return false;
        }
        this.tunnelNameEarlyWarn = response.rows[0].tunnelName;
        this.tunnelId = response.rows[0].tunnelId;
        this.tunnelLane = response.rows[0].lane;
        // this.specialEcharts(this.tunnelId)
        this.vehicleEcharts();
        this.getDeviceDataAndStateData();
        var newDict = this.dict.type.sd_sys_name;
        if (this.tunnelId != "JQ-JiNan-WenZuBei-MJY") {
          // this.robotShow = false;
          this.dictList = newDict.slice(0, 8);
        } else if (this.tunnelId == "JQ-JiNan-WenZuBei-MJY") {
          this.dictList = this.dict.type.sd_sys_name;
          // this.robotShow = true;
        }
        this.tunnelList = [];
        this.checkboxTunnel = [];
        let list = response.rows;
        if (list.length > 0) {
          for (let i = 0; i < list.length; i++) {
            if (list[i].tunnelLength == null || list[i].tunnelLength == "") {
              let num = 0;
              list[i].tunnelLength = num.toString();
            }
          }
          this.tunnelList = list;
          this.checkboxTunnel.push(this.tunnelList[0]);
          this.currentTunnel.id = this.tunnelList[0].tunnelId;
          this.currentTunnel.name = this.tunnelList[0].tunnelName;
          this.selectEquipmentType(this.currentTunnel.id);
          this.getTunnelData(this.currentTunnel.id);
        }
        this.getTunnelLane();
        // this.timingControl()
      });
    },
    /* 查询设备类型*/
    selectEquipmentType(currentTunnelId) {
      this.eqTypeList = [];
      listHasType(currentTunnelId).then((response) => {
        let typeList = response.rows;
        this.eqTypeList = typeList;
        // this.eqTypeList.unshift({
        //   typeId: "00",
        //   typeName: "全部",
        // });

        // this.eqBigTypeList = [];
        groupByBigType().then((response) => {
          var typeList = response.rows;
          // this.eqBigTypeList = typeList;
          // this.eqBigTypeList.unshift({
          //   typeId: "00",
          //   bigType: "全部",
          // });
        });
      });
    },
    // 查询方向
    selectDirection(param) {
      listDevices(param).then((response) => {
        this.allDirection = this.changeDirection(response.rows);
        for (let i = 0; i < this.allDirection.length; i++) {
          for (let j = i + 1; j < this.allDirection.length; j++) {
            if (
              this.allDirection[i].eqDirection ==
              this.allDirection[j].eqDirection
            ) {
              this.allDirection.splice(j, 1); //splice会改变原数组
              j--;
            }
          }
        }
      });
    },
    // 查询车道
    selectLane(param) {
      listDevices(param).then((response) => {
        this.allLane = this.changeDirection(response.rows);
        for (let i = 0; i < this.allLane.length; i++) {
          for (let j = i + 1; j < this.allLane.length; j++) {
            if (this.allLane[i].lane == this.allLane[j].lane) {
              this.allLane.splice(j, 1);
              j--;
            }
          }
        }
        this.allLane.forEach((item) => {
          switch (Number(item.lane)) {
            case 1:
              item.laneName = "第一车道";
              break;
            case 2:
              item.laneName = "第二车道";
              break;
            case 3:
              item.laneName = "第三车道";
              break;
            case 0:
              item.laneName = "其它";
              break;
          }
        });
      });
    },
    /* 查询设备*/
    selectEquipment(param) {
      let that = this;
      listDevices(param).then((response) => {
        that.devicesList = that.changeDirection(response.rows);
      });
    },
    changeDirection(list) {
      for (let i = 0; i < list.length; i++) {
        if (list[i].eqDirection == "1") {
          list[i].eqDirectionName = this.leftDirection + "方向";
        } else if (list[i].eqDirection == "0") {
          list[i].eqDirectionName = this.rightDirection + "方向";
        } else {
          list[i].eqDirectionName = "";
        }
      }
      return list;
    },
    /* 查询设备状态图标*/
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
    async getEqUrl(list) {
      let that = this;
      // that.eqTypeStateList = [];
      that.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let img = await that.picture(list[i].iFileList[j].url);
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        that.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
      }
      console.log(that.eqTypeStateList, "设备图标eqTypeStateList");
      for (var item of that.eqTypeStateList) {
        if (item.type == 18) {
          console.log(item, "引道照明");
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
    /* 视频 */
    changeLoading() {
      this.loading = false;
      this.cameraErrorInfo = "抱歉！视频信号丢失！";
    },
    /* 获取隧道配置信息*/
    getTunnelData(tunnelId) {
      let that = this;
      that.upList = [];
      that.downList = [];
      const params = {
        tunnelId: tunnelId,
      };
      getTunnels(tunnelId).then((response) => {
        that.loading = false;
        let res = response.data.storeConfigure;

        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          listType("")
            .then((response) => {
              for (let i = 0; i < res.eqList.length; i++) {
                res.eqList[i].focus = false;
                for (let j = 0; j < response.rows.length; j++) {
                  if (response.rows[j].typeId == res.eqList[i].eqType) {
                    let iconWidth = Number(response.rows[j].iconWidth);
                    let iconHeight = Number(response.rows[j].iconHeight);
                    res.eqList[i].iconWidth = iconWidth;
                    res.eqList[i].iconHeight = iconHeight;
                    break;
                  }
                }
              }
              that.selectedIconList = res.eqList; //设备zxczczxc
              that.getRealTimeData();
              // that.selectedIconList.forEach((item, indx) => {
              //   // if(item.eqName=='固定摄像机（枪机）'){
              //   if (item.eqType == "23") {
              //     item.position.left = item.position.left + 20;
              //     item.position.top = item.position.top;
              //   } else if (item.eqType == "34") {
              //     // else if(item.eqName=='紧急电话'){
              //     item.position.left = item.position.left + 14;
              //     item.position.top = item.position.top;
              //   } else if (item.eqType == "21") {
              //     // else if(item.eqName=='紧急电话'){
              //     item.position.left = item.position.left + 20;
              //     item.position.top = item.position.top;
              //   } else if (item.eqType == "20") {
              //     // else if(item.eqName=='微波车辆检测器'){
              //     item.position.left = item.position.left + 16;
              //     item.position.top = item.position.top;
              //   }
              //   // else if(item.eqType=='紧急电话'){
              //   // else if(item.eqName=='紧急电话'){
              //   //   item.position.left = item.position.left + 20;
              //   //   item.position.top = item.position.top;
              //   // }
              //   else if (item.eqType == "1") {
              //     // else if(item.eqName=='车道指示器'){
              //     item.position.left = item.position.left + 10;
              //     item.position.top = item.position.top + 16;
              //   } else if (item.eqType == "7") {
              //     // else if(item.eqName=='加强照明'){
              //     item.position.left = item.position.left + 52;
              //     item.position.top = item.position.top - 6;
              //   } else if (item.eqType == "9") {
              //     // else if(item.eqName=='基本照明'){
              //     item.position.left = item.position.left + 18;
              //     item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "19") {
              //     // else if(item.eqName[0]+item.eqName[1]=='CO'){
              //     item.position.left = item.position.left + 20;
              //     item.position.top = item.position.top - 2;
              //   } else if (item.eqType == "24" || item.eqType == "35") {
              //     // else if(item.eqName[0]+item.eqName[1]=='云台'){
              //     item.position.left = item.position.left + 22;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "13" || item.eqType == "18") {
              //     // else if(item.eqName=='水泵'){
              //     item.position.left = item.position.left + 14;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "3") {
              //     // else if(item.eqName=='交通信号灯'){
              //     item.position.left = item.position.left + 26;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "8") {
              //     // else if(item.eqName=='引道照明'){
              //     item.position.left = item.position.left + 20;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "17" || item.eqType == "22") {
              //     // else if(item.eqName.substring(0,7)=='风速风向检测器'){
              //     item.position.left = item.position.left + 22;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "5") {
              //     // else if(item.eqName.substring(0,7)=='亮度检测器'){
              //     item.position.left = item.position.left + 18;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "6") {
              //     // else if(item.eqName.substring(0,7)=='应急照明'){
              //     item.position.left = item.position.left + 24;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "10") {
              //     // else if(item.eqName.substring(0,7)=='风机'){
              //     item.position.left = item.position.left + 18;
              //     // item.position.top = item.position.top - 4;
              //   } else if (item.eqType == "14") {
              //     // else if(item.eqName.substring(0,7)=='PLC主机'){
              //     item.position.left = item.position.left + 16;
              //     // item.position.top = item.position.top - 4;
              //   }
              // });
              console.log(
                that.selectedIconList,
                "所有设备图标selectedIconList"
              );
              for (var item of that.selectedIconList) {
                if (
                  this.tunnelId == "JQ-JiNan-WenZuBei-MJY" &&
                  item.eqType == 29
                ) {
                  console.log(item, "000000000000000000000");
                  // this.dictList = this.dict.type.sd_sys_name;
                  this.robotShow = true;
                } else {
                  this.robotShow = false;
                }
              }
            })
            .then(() => {
              that.initEharts();
              // 切换隧道配置信息时，联动大类查询
              that.displayControl(
                that.selectBigType.index.toString(),
                that.selectBigType.bigType.toString()
              );
            });

          if (res.upList != undefined) {
            that.upList = res.upList;
          }
          if (res.downList != undefined) {
            that.downList = res.downList;
          }
          if (res.leftDirection != undefined && res.leftDirection != "") {
            that.leftDirection = res.leftDirection;
          }
          if (res.rightDirection != undefined && res.leftDirection != "") {
            that.rightDirection = res.rightDirection;
          }

          let id = res.lane;
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.currentTunnel.lane = that.laneUrlList[i];
              // that.railingList[1].position.left = that.laneUrlList[i].width
            }
          }
        } else {
          console.log("不存在");
          //不存在
          that.selectedIconList = [];
          that.initEharts();
          //工作台默认背景图
          // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
    },

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
    /* 获取照明设备数据*/
    getLiPowerDevice() {
      getLiPowerDevices().then((response) => {
        let res = response.data;
        if (res.state == 0) {
          this.lightState = "正常";
        } else if (res.state == -2) {
          this.lightState = "异常";
        }
        // 照明设备
        for (let i = 0; i < res.list.length; i++) {
          // 图标
          for (let j = 0; j < this.selectedIconList.length; j++) {
            // 设备id如果相等
            if (res.list[i].eqId == this.selectedIconList[j].eqId) {
              // 设备状态
              for (let k = 0; k < this.eqTypeStateList.length; k++) {
                // 设备状态相同且设备类型相同
                if (
                  this.selectedIconList[j].eqType ==
                    this.eqTypeStateList[k].type &&
                  res.list[i].switchStatus == this.eqTypeStateList[k].state
                ) {
                  // 改变图标
                  this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                  this.selectedIconList[j].lightValue = res.list[i].lightValue;
                }
              }
            }
          }
        }
      });
    },
    /* 获取实时数据PLC*/
    getRealTimeData() {
      // 真实
      //getConfigData(this.currentTunnel.id)
      // 模拟
      // getStorageData({
      //     tunnelId: this.currentTunnel.id
      //   })
      //   .then((response) => {
      //     for (let i = 0; i < response.length; i++) {
      //       // 实时状态
      //       let type = response[i].devType;
      //       if (type != "" && type != undefined) {
      //         for (let j = 0; j < this.selectedIconList.length; j++) {
      //           if (response[i].devId == this.selectedIconList[j].eqId) {
      //             // 需要换光标的
      //             for (let k = 0; k < this.eqTypeStateList.length; k++) {
      //               if (
      //                 this.selectedIconList[j].eqType ==
      //                 this.eqTypeStateList[k].type &&
      //                 response[i].state == this.eqTypeStateList[k].state
      //               ) {
      //                 let url = this.eqTypeStateList[k].url;
      //                 this.selectedIconList[j].eqDirection =
      //                   response[i].direction;
      //                 if (response[i].direction == "1") {
      //                   //上行车道
      //                   if (url.length > 1) {
      //                     this.selectedIconList[j].url = [url[1], url[0]];
      //                   } else {
      //                     this.selectedIconList[j].url = url;
      //                   }
      //                 } else {
      //                   this.selectedIconList[j].url =
      //                     this.eqTypeStateList[k].url;
      //                 }
      //                 this.selectedIconList[j].state = response[i].state;
      //               }
      //               // 微波车检
      //               else if (
      //                 this.selectedIconList[j].eqType ==
      //                 this.eqTypeStateList[k].type &&
      //                 this.selectedIconList[j].eqType == "108" &&
      //                 type == "108"
      //               ) {
      //                 this.selectedIconList[j].wbList = response[i].state;
      //               }
      //               // 路面状态
      //               else if (
      //                 // this.selectedIconList[j].eqType == this.eqTypeStateList[k].type &&
      //                 this.selectedIconList[j].eqType == "120" && type == "120"
      //               ) {
      //                 this.selectedIconList[j].lmList = response[i].state;
      //               }
      //               // 道路结冰
      //               else if (
      //                 this.selectedIconList[j].eqType == "110" &&
      //                 type == "110"
      //               ) {
      //                 this.selectedIconList[j].dljb = response[i].state;
      //               }
      //               /* // 水泵
      //           else if (this.selectedIconList[j].eqType == '18' && type == '18') {
      //             this.selectedIconList[j].shuibeng = response[i].state
      //             if (response[i].state) {
      //               let devState = JSON.parse(response[i].state).devState
      //               if (this.selectedIconList[j].eqType == this.eqTypeStateList[k].type &&
      //                 devState == this.eqTypeStateList[k].state) {
      //                 this.selectedIconList[j].url = this.eqTypeStateList[k].url
      //               }
      //             }
      //           } */
      //             }
      //             // 不需要换光标的
      //             let paramType = [5, 6, 13, 14, 15, 16, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
      //             if (paramType.includes(parseInt(type))) {
      //               if (response[i].state == "null" || !response[i].state) {
      //                 this.selectedIconList[j].value = "0";
      //               } else {
      //                 this.selectedIconList[j].value = response[i].state;
      //               }
      //             }
      //           }
      //         }
      //       }
      //     }
      //   })
      //   .catch((err) => {
      //     // this.systemState = "较差";
      //   });
      getDeviceData({
        tunnelId: this.currentTunnel.id,
      }).then((response) => {
        // for (let i = 0; i < response.data.length; i++) {
        // debugger;
        // 实时状态
        // let type = response.data[i].eqType;
        // if (type != "" && type != undefined) {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          // console.log(deviceData,'deviceDatadeviceData')
          if (deviceData) {
            // let type = deviceData.eqType;

            // 需要换光标的
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35,
                ];
                if (arr.includes(deviceData.eqType)) {
                  if (
                    // 摄像机之类的只有在线 离线 故障图标
                    this.eqTypeStateList[k].stateType == "1" &&
                    this.eqTypeStateList[k].state == deviceData.eqStatus
                  ) {
                    //取设备监测状态图标
                    this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                    if (deviceData.eqStatus == 1) {
                      if (deviceData.eqType == 19) {
                        this.selectedIconList[j].num =
                          "CO:" +
                          parseFloat(deviceData.CO).toFixed(2) +
                          "/PPM  VI:" +
                          parseFloat(deviceData.VI).toFixed(2) +
                          "KM";
                      } else if (deviceData.eqType == 17) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.FS).toFixed(2) +
                          "m/s " +
                          deviceData.FX;
                      } else if (deviceData.eqType == 5) {
                        if (deviceData.DWLD) {
                          this.selectedIconList[j].num =
                            parseFloat(deviceData.DWLD).toFixed(2) + "lux";
                        }
                      } else if (deviceData.eqType == 18) {
                        if (deviceData.DNLD) {
                          this.selectedIconList[j].num =
                            parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                        }
                      }
                    }
                  }
                } else {
                  //可以控制设备状态的设备类型，比如车指
                  if (deviceData.eqStatus == "1") {
                    // 在线
                    if (
                      // 车指之类的包括正红反绿之类的图标 == 2
                      this.eqTypeStateList[k].stateType == "2"
                    ) {
                      if (this.eqTypeStateList[k].state == deviceData.state) {
                        //取设备运行状态图标
                        let url = this.eqTypeStateList[k].url;
                        this.selectedIconList[j].eqDirection =
                          deviceData.eqDirection;
                        if (deviceData.eqDirection == "1") {
                          //上行车道
                          if (url.length > 1) {
                            this.selectedIconList[j].url = [url[1], url[0]];
                          } else {
                            this.selectedIconList[j].url = url;
                          }
                        } else {
                          this.selectedIconList[j].url =
                            this.eqTypeStateList[k].url;
                        }
                      }
                    }
                  } else {
                    //如果是离线、故障等状态
                    if (
                      this.eqTypeStateList[k].stateType == "1" &&
                      this.eqTypeStateList[k].state == deviceData.eqStatus
                    ) {
                      //取设备监测状态图标
                      this.selectedIconList[j].url =
                        this.eqTypeStateList[k].url;
                    }
                  }
                }

                // let url = this.eqTypeStateList[k].url;
                // this.selectedIconList[j].eqDirection =
                // deviceData.eqDirection;
                // if (deviceData.eqDirection == "1") {
                //   //上行车道
                //   if (url.length > 1) {
                //     this.selectedIconList[j].url = [url[1], url[0]];
                //   } else {
                //     this.selectedIconList[j].url = url;
                //   }
                // } else {
                //   this.selectedIconList[j].url =
                //     this.eqTypeStateList[k].url;
                // }
                // this.selectedIconList[j].state = deviceData.eqStatus;
              }
            }
            // 不需要换光标的
            // let paramType = [5,17, 18, 19, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
            // if (paramType.includes(parseInt(type))) {
            //   if (deviceData.eqStatus == "null" || !deviceData.eqStatus) {
            //     this.selectedIconList[j].value = "0";
            //   } else {
            //     this.selectedIconList[j].value = deviceData.eqStatus;
            //   }
            // }
          }
        }
      });
    },
    /* 选择隧道*/
    switchTunnel() {
      this.title = "切换隧道";
      this.tunnelVisible = true;
    },
    /* 选择隧道*/
    setTunnel(item, index) {
      this.buttonIndex = index;
      this.tunnelNameEarlyWarn = item.tunnelName;
      this.tunnelId = item.tunnelId;
      this.lightControForm.index = index;
      this.lightControForm.name = item.tunnelName;
      // this.tunnelBtnStyle = []
      // this.tunnelBtnStyle[index] = {
      //   "border-color": "#ffbf1d",
      //   // "box-shadow": "0px 0px 10px #68B5FF inset",
      //   "color": "0a88bd",
      //   "background-color": "#e2f3fa !important",
      // };
      (this.currentTunnel.id = item.tunnelId),
        (this.currentTunnel.name = item.tunnelName),
        this.selectEquipmentType(this.currentTunnel.id);
      this.getTunnelData(this.currentTunnel.id);
    },
    onActivated(key) {},
    onDragging(key) {},
    onDropped(key) {},
    monitorWebsocket() {},
    carchange() {
      getTunnels(this.tunnelId).then((res) => {
        const tunnel = res.data;
        // math.add(a+b)//加
        // math.subtract(a-b)//减
        // math.multiply(a*b)//乘
        // math.divide(a/b)//除
        // 计算公里数
        let Mileage = math.divide(
          math.subtract(+tunnel.endPileNum - +tunnel.startPileNum) / 1000
        );
        this.tunnelKm = Mileage;
        if (Mileage <= 1.3) {
          var length = +1300;
        } else if (Mileage < +2.6) {
          var length = +1300 * 1.5;
        } else if (Mileage > +2.6) {
          var length = +1300 * 2;
        }
        this.lane = tunnel.lane;
        this.tunnelLength = length; //px长度
        this.proportion = math.divide(length / (Mileage * 1000)); //计算px和米的比例
      });
      // 首页获取隧道长度，根据隧道长度判断车辆行驶的全部距离
    },
    /*点击设备类型*/
    displayControl(value, lable) {
      if (value == "6") {
        this.carchange();
        this.carShow = true;
      } else {
        this.carShow = false;
      }
      // carShow
      for (var item of this.selectedIconList) {
        if (this.tunnelId == "JQ-JiNan-WenZuBei-MJY" && item.eqType == 29) {
          this.robotShow = true;
        } else {
          this.robotShow = false;
        }
      }
      $(".leftButtonS")
        .eq(value)
        .addClass("leftButton_hover")
        .siblings()
        .removeClass("leftButton_hover");
      this.selectBigType = {
        bigType: lable,
        index: value,
      };
      let data = this.eqBigTypeList;

      var val = value.toString();
      hasListByBigType(val).then((response) => {
        let typelist = response.rows;
        let typeIndex = [];
        if (typelist.length > 0) {
          for (let y = 0; y < typelist.length; y++) {
            for (let i = 0; i < this.selectedIconList.length; i++) {
              if (typelist[y].typeId == this.selectedIconList[i].eqType) {
                typeIndex.push(i);
              }
              this.selectedIconList[i].display = false;

              // 没有eqType属性的图片依旧显示 例如：隧道名称
              if (
                this.selectedIconList[i].eqType == null ||
                this.selectedIconList[i].eqType == undefined
              ) {
                this.selectedIconList[i].display = true;
              }
            }
          }
          for (let i = 0; i < typeIndex.length; i++) {
            this.selectedIconList[typeIndex[i]].display = true;
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            this.selectedIconList[i].display = false;
          }
        }
      });
    },
    /* 传感器设备提示框 */
    sensorContent(item) {
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return "";
      } else {
        let deviceName = "设备名称：" + item.eqName; // 设备名称
        let deviceStake = " 桩号：" + item.pile; // 桩号
        let state = " 检测值：" + (item.value || "无"); // 检测值
        return (deviceName + deviceStake + state).toString();
      }
    },
    sensorDisabled(item) {
      // 需要显示那类设备，直接把设备的eqType值放入就可以
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        this.showTooltip = false;
      } else {
        this.showTooltip = true;
      }
    },
    sensorDisabledTwo(item) {
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return true;
      } else {
        return false;
      }
    },
    clickRobot() {
      this.eqInfo.clickEqType = 29;
      for (var item of this.selectedIconList) {
        if (item.eqType == 29) {
          console.log(item, "机器人");
        }
      }
    },
    //================================================单个配置开始==================================
    /* 打开配置界面*/
    async openStateSwitch(item) {
      console.log(item, "item");
      if (this.addBatchManage == true) {
        // 判断是否有选中项 有的话 判断本次点击和上次点击 设备类型是否一样
        // 要求每次点击选中的设备类型相同
        if (this.itemEqType) {
          // 第二次点击时
          for (var itm of this.selectedIconList) {
            //  多选 选择的设备类型相同时
            if (itm.eqId == item.eqId && this.itemEqType == item.eqType) {
              // 比对id 如果曾点过 取消选框
              const result = this.itemEqId.findIndex(
                (item) => item == itm.eqId
              );
              if (result === -1) {
                itm.click = true;
                this.itemEqId.push(itm.eqId);
                this.$forceUpdate();
              } else {
                this.itemEqId.splice(result, 1);
                itm.click = false;
                this.$forceUpdate();
                if (this.itemEqId.length == 0) {
                  this.itemEqType = "";
                  this.batchManageForm.eqType = "";
                  this.batchManageForm.eqDirection = "";
                  this.addBatchManage = false;
                  this.$forceUpdate();
                }
              }
            }
            // 多选 选择的设备类型不同时 提示红字
            else if (itm.eqId == item.eqId && this.itemEqType != item.eqType) {
              itm.textFalse = true;
              this.$forceUpdate();
            }
          }
        } else {
          // 第一次点击时
          for (let itm of this.selectedIconList) {
            // console.log(itm);
            if (itm.eqId == item.eqId) {
              console.log(itm, "0000000000000000000000000");
              itm.click = true;
              this.itemEqId.push(itm.eqId);
              this.itemEqType = itm.eqType;
              this.batchManageForm.eqType = itm.eqType;
              this.batchManageForm.eqDirection = itm.eqDirection;
              console.log(this.batchManageForm);
              this.$forceUpdate();
              getType(itm.eqType).then((res) => {
                console.log(res, "查询设备图标宽高");
                this.iconWidth = res.data.iconWidth;
                this.iconHeight = res.data.iconHeight;
              });
            }
          }
        }
      } else if (this.addBatchManage == false) {
        this.mouseoversImplement = false;
        console.log(item, "点击的设备");
        this.eqInfo = {
          clickEqType: item.eqType,
          equipmentId: item.eqId,
        };

        let StateTypeId = {
          StateTypeId: item.eqType,
        };
        // getStateByRun(StateTypeId).then(res => {
        //   this.stateForm.stateName = res.rows[0].stateName
        // })
        // this.getTunnelData(this.currentTunnel.id);
        // 防止 ‘暂未获取’ 和 配置状态单选同时出现
        this.spanEqtypeDate = true;
        let newPromise = new Promise((resolve) => {
          resolve();
        });
        await newPromise.then(() => {
          this.eqTypeStateList.forEach((val) => {
            if (item.eqType == val.type && val.control == 1) {
              this.itemEqTypeStateList.push(val);
            }
          });
        });
        if (this.itemEqTypeStateList != []) this.spanEqtypeDate = false;
        // 传感器（模拟量）
        // let sensorDevice = [1, 2, 3, 4, 7, 8, 9, 10, 12, 13, 14, 15, 16];
        // if (sensorDevice.indexOf(item.eqType) != -1) {
        //   // this.stateSwitchVisible = true;
        //   // this.stateForm = {
        //   //   value: item.value,
        //   // };
        //   // this.$nextTick(()=>{
        //   //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
        //   // })
        // }

        //跳转微波车检
        // if (item.eqType == "108") {
        //   this.weiboList = eval(item.wbList);
        //   this.title = item.eqName;
        //   this.weiboVisible = true;
        //   return;
        // }
        // // 跳转路面状态
        // else if (item.eqType == "120") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1,
        //   };
        //   const eqId = {
        //     eqId: item.eqId
        //   };
        //   selectByEqDeno(eqId).then(response => {
        //     this.lumianList = response.data;
        //   })
        //   if (item.lmList) {
        //     this.lmList = JSON.parse(item.lmList)
        //   }
        //   this.title = item.eqName;
        //   this.lumianVisible = true;
        //   return;
        // }
        // else if (item.eqType == "10") {
        //   this.spanEqtypeDate = false
        //   // 风机
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //     tunnelName:item.tunnelName.tunnelName
        //   };
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        // }
        // else if (item.eqType == "8") {
        //   this.spanEqtypeDate = false
        //   // 引道照明
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //   };
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;

        // }
        //跳转 摄像机
        // else if (item.eqType == "24" || item.eqType == "23") {
        //   console.log("点击摄像机")
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1,
        //     eqFeedbackAddress2: item.eqFeedbackAddress2,
        //   };
        //   this.title = item.eqName;
        //   // this.cameraVisible = true;
        //   this.loading = true;
        //   // this.flvPlayer()
        //   setTimeout(this.changeLoading, 2000);
        //   return;
        // }
        // else if(item.eqType == "17"){
        //   this.title = '风速风向检测器'
        //   this.coviVisible = true
        // }
        // else if(item.eqType == "19"){
        //   this.title = 'CO/VI检测器'
        //   this.coviVisible = true
        // }
        // 水泵
        /* else if (item.eqType == "18") {
          if (item.shuibeng && item.shuibeng!='null') {
            this.shuibengObj = JSON.parse(item.shuibeng);
            this.shuibengObj.devState = "正常";
          } else if(!item.shuibeng) {
            this.shuibengObj = {
              "beng1": "关闭", "beng2": "关闭", "dianyuan": "无", "queshui": "无", "liandong": "未联动", "devState": "离线"
            }
          }
          this.title = item.eqName
          this.shuibengVisible = true
          return;
        } */
        //跳转道路结冰
        // else if (item.eqType == "110") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1,
        //   };
        //   if (item.dljb) {
        //     this.dljbObj = JSON.parse(item.dljb);
        //   }
        //   this.title = item.eqName;
        //   this.daolujiebingVisible = true;
        //   return;
        // }
        // //打开情报板
        // else if (item.eqType == "100") {
        //   await getDeviceBase(item.eqId).then((data) => {
        //     console.log('情报板大小')
        //     this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
        //   });
        //   console.log(item, '情报板信息');
        //   this.dialogVisible = true;
        //   this.$refs.vmsContentUpdate.deviceId = item.eqId;
        //   this.$refs.vmsContentUpdate.isAdd = true;
        //   // this.$refs.contentBatchEdit.list = item;
        //   this.$refs.vmsContentUpdate.init();
        //   // this.dialogVisible = true;
        //   // this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,item);
        // } else if (item.eqType == "111") {
        //   const eqId = {
        //     equipmentId: item.eqId,
        //     tunnelId: this.tunnelId
        //   }
        //   this.spanEqtypeDate = false
        //   var pressureData = {}
        //   pressure(eqId).then(response => {
        //     console.log(response, "压力表")
        //     pressureData = response.data[0]
        //     this.stateForm = {
        //       eqType: item.eqType,
        //       eqTypeName: item.typeName.typeName,
        //       eqDirection: item.eqDirection,
        //       pile: item.pile,
        //       value: item.value,
        //       equipmentName: pressureData.equipmentName,
        //       position: pressureData.position,
        //       tunnelName: pressureData.tunnelName,
        //       analogQuantity: pressureData.analogQuantity,
        //       highest: pressureData.highest,
        //       low: pressureData.low,
        //       createTime: pressureData.createTime
        //     };
        //   });
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        // } else if (item.eqType == "112") {
        //   // 巡检机器人
        //   this.stateForm = {
        //     ...item,
        //     value: item.value,
        //     state: '正常',
        //     electricQuantity: '80',
        //     eqTypeName: item.typeName.typeName,
        //     eqTunnelName: item.tunnelName.tunnelName,
        //     manufacturers: '苹果AI',
        //     dateOfManufacture: '2022/2/23 14:30:45',
        //   };
        //   this.robotInformationCollectionData = {}
        //   // 状态记录
        //   this.robotRecordList = [{
        //       robotName: '巡检机器人-001',
        //       tunnelName: '马家峪隧道',
        //       DateTime: '2022-2-23 15:45:13',
        //       state: '离线'
        //     },
        //     {
        //       robotName: '巡检机器人-001',
        //       tunnelName: '马家峪隧道',
        //       DateTime: '2022-2-23 10:23:00',
        //       state: '正常'
        //     },
        //   ]
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;

        // } else if (item.eqType == "118") {
        //   if (!item.eqFeedbackAddress1) {
        //     this.$modal.msgWarning('没有给设备配置点位地址')
        //     this.youdaoVisible = false
        //     return
        //   }
        //   this.title = item.eqName
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqDirection: item.eqDirection,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1
        //   }
        //   // 诱导灯
        //   getHostData({
        //     hostIP: item.eqFeedbackAddress1
        //   }).then(response => {
        //     this.stateForm = {
        //       eqId: item.eqId,
        //       eqDirection: item.eqDirection,
        //       eqFeedbackAddress1: item.eqFeedbackAddress1,
        //       corModel: response.corModel,
        //       Zlane: (response.Zlane == "true" ? true : false),
        //       Ylane: (response.Ylane == "true" ? true : false),
        //       whiteLight: parseInt(response.whiteLight),
        //       yellowLight: parseInt(response.yellowLight),
        //       twinkleModel: response.twinkleModel,
        //       twinkleFrequency: response.twinkleFrequency,
        //       lightTime: response.lightTime
        //     }
        //   })
        //   this.youdaoVisible = true
        // }
        // else if (item.eqType == 1){
        //   this.batchVisible = false;
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //   };
        // }
        // else if (item.eqType == "123") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state == '1' ? '已报警' : '未报警',
        //     pile: item.pile,
        //   };
        //   console.log(this.stateForm, "声光报警")
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        //   return;
        // } else {
        //   //配置
        //   if (item.environmentType == undefined) {
        //     this.batchVisible = false;
        //     if (
        //       item.typeName == undefined ||
        //       item.eqDirection == undefined ||
        //       item.eqDirection == null
        //     ) {
        //       this.$message({
        //         message: "未能获取设备信息,请重新配置！",
        //         type: "warning",
        //         customClass: "workbench-msg",
        //       });
        //       this.cleanUp();
        //       this.empty();
        //     } else {
        //       // 红绿灯
        //       this.spanEqtypeDate = false
        //       this.stateForm = {
        //         eqId: item.eqId,
        //         eqHostId: item.eqHostId,
        //         eqName: item.eqName,
        //         eqType: item.eqType,
        //         eqTypeName: item.typeName.typeName,
        //         eqDirection: item.eqDirection,
        //         state: item.state, //默认状态
        //         pile: item.pile,
        //         value: item.value,
        //         lightValue: !item.lightValue ? 0 : item.lightValue,
        //         fjState: item.state,
        //       };
        //       this.title = item.eqName;
        //       // this.stateSwitchVisible = true;
        //       let sensorDevice = [ 15, 16, 20, 28,  34,31];
        //       if (sensorDevice.indexOf(item.eqType) != -1) {
        //         this.stateSwitchVisible = true;
        //         // this.stateForm = {
        //         //   value: item.value,
        //         // };
        //         // this.$nextTick(()=>{
        //         //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
        //         // })
        //       }
        //     }
        //   }
        // }
      }
    },
    // loadFlv() {
    //   if (flvjs.isSupported()) {
    //     var videoElement = document.getElementById("videoBox");
    //     var flvPlayer = flvjs.createPlayer({
    //       type: 'flv',
    //       url: 'http://10.166.139.12:8081/live/22456.flv' //你的url地址
    //     });
    //     flvPlayer.attachMediaElement(videoElement);
    //     flvPlayer.load();
    //     flvPlayer.play();
    //   }
    // },
    /* 确认配置（单项）*/
    submitState() {
      this.stateSwitchVisible = false;
      // this.stateForm ={}
      let param = {
        // devId: this.stateForm.eqId,
        // devType: this.stateForm.eqType,
        // hostId: this.stateForm.eqHostId,
        // state: this.stateForm.state,
        // tunnelId: this.currentTunnel.id,
        deviceId: this.stateForm.eqId,
        devType: this.stateForm.eqType,
        hostId: this.stateForm.eqHostId,
        state:
          this.stateForm.eqType == 31
            ? this.radioEqType31
            : this.stateForm.state,
        tunnelId: this.currentTunnel.id,
        brightness: this.brightness,
        frequency: this.frequency,
      };
      // 发送模拟指令
      this.sendAnalogCommand(param);
      // 发送正式指令
      // this.sendDirective(param)
      // if (await this.sendDirective(param)) {
      //   // 非照明类（plc）提交
      //   this.setConfigData(param);
      // } else {
      //   this.setWebsocket(param);
      // }
    },
    sendAnalogCommand(param) {
      sendAnalogCom(param).then((response) => {
        if (response.code == 200) {
          this.$modal.msgSuccess("配置成功");
        }
      });
      // const res = await deviceApi.sendAnalogCom(param)
      // console.log(res)
      // return new Promise((resolve, reject) => {
      //   resolve(true);
      // });
    },

    //正式方法
    async sendDirective(param) {
      const res = await deviceApi.sendDirective(param);
      return new Promise((resolve, reject) => {
        resolve(true);
      });
    },
    async getTypeFlag(param) {
      let type = param.devType;
      let hostId = param.hostId;
      const response = await getHosts(hostId);
      let plcControlType = response.data.plcControlType;
      return new Promise((resolve, reject) => {
        if (plcControlType == "CIO") {
          resolve(true);
        } else {
          // 加强、基本、
          if (type == 7 || type == 8 || type == 9 || type == 21) {
            resolve(false);
          } else {
            resolve(true);
          }
        }
      });
    },

    /* 提交配置*/
    setConfigData(param) {
      setConfigData(param)
        .then((response) => {
          console.log("记录成功");
        })
        .catch((err) => {
          console.log("记录失败,后台报错：" + err);
        });
    },
    setWebsocket(param) {
      let actions = "";
      // 发送指令
      if (param.devType == 21) {
        actions = "Light LightValue " + param.devId + " ";
        actions = actions + this.stateForm.lightValue;
      } else {
        let state = "";
        if (param.state == 1) {
          state = 1;
        } else if (param.state == 2) {
          state = 0;
        }
        actions = "Device PowerSet " + param.devId + " " + state;
      }

      this.websocketsend(actions);
    },

    /* 诱导确认配置（单项）*/
    submitCorLight() {
      if (this.stateForm.corModel == null) {
        this.$modal.msgError("请选择控制模式后重试！");
        return;
      }
      let param = {
        hostIP: this.stateForm.eqFeedbackAddress1,
        corModel: this.stateForm.corModel,
        Zlane: this.stateForm.Zlane,
        Ylane: this.stateForm.Ylane,
        whiteLight: this.stateForm.whiteLight,
        yellowLight: this.stateForm.yellowLight,
        twinkleModel: this.stateForm.twinkleModel,
        twinkleFrequency: this.stateForm.twinkleFrequency,
        lightTime: this.stateForm.lightTime,
      };
      this.setCorLight(param)
        .then((response) => {
          this.$modal.msgSuccess("控制成功");
          this.youdaoVisible = false;
        })
        .catch(() => {
          this.$modal.msgError("控制失败，请检查设备或稍后重试！");
        });
    },
    setCorLight(param) {
      setCorLight(param)
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log("配置失败,后台报错：" + err);
        });
    },
    /**诱导弹窗-控制模式改变 */
    outLight() {
      this.stateForm.whiteLight = 0;
      this.stateForm.yellowLight = 0;
    },
    //================================================单个配置结束==================================
    //================================================批量配置开始====================================
    /* 点击批量配置*/
    // ------------------------------------------老版批量操作功能开始----------------------------------
    // batchManage() {
    // this.devicesList = [];
    // this.batchVisible = true;
    // mode = "buttonSelection";
    // this.title = "批量操作";
    // // this.$refs.batchForm.clearValidate();
    // this.batchForm.eqType = this.eqTypeList[1].typeId;
    // let param = {
    //   eqTunnelId: this.currentTunnel.id,
    //   eqType: this.eqTypeList[1].typeId,
    //   eqDirection: "",
    //   lane: "",
    // };
    // this.batchFormDirection = 1;
    // this.selectEquipment(param);
    // },
    // ------------------------------------------老版批量操作功能结束----------------------------------

    /* 批量选择后*/
    handleSelectionChange(val) {
      this.ids = val.map((item) => item.id);
      this.single = val.length !== 1;
      this.multiple = !val.length;
      this.batchForm.eqList = [];
      // this.batchFormDirection = 1
      for (let i = 0; i < val.length; i++) {
        if (val[i + 1] != undefined) {
          if (
            val[i].typeName.typeName.indexOf("车道指示器") != -1 &&
            val[i].eqDirection != val[i + 1].eqDirection
          ) {
            this.$message({
              message: "请选择相同方向的设备进行配置！",
              type: "warning",
              customClass: "workbench-msg",
            });
            this.$refs.multipleTable.clearSelection();
            this.batchForm.eqList = [];
            break;
          } else {
            this.batchFormDirection = val[i].eqDirection;
            this.batchForm.eqList.push(val[i]);
          }
        } else {
          this.batchFormDirection = val[i].eqDirection;
          this.batchForm.eqList.push(val[i]);
        }
      }
    },
    everyCheck(number) {
      return number != 100;
    },
    /* 批量确认按钮*/
    submitBatchState(formName) {
      let that = this;
      if (that.batchForm.eqList.length == 0) {
        return that.$modal.msgWarning("请选择设备");
      }

      // 批量情报板编辑
      // 一 是否有选中情报板
      // 二 选中是否全部为情报板
      // 三 所有选中情报板分辨率是否一致
      // 满足条件后执行批量编辑
      // this.$refs.contentBatchEdit.vmsSize = list[0].vmsSize;
      // this.$refs.contentBatchEdit.list = list;
      // this.$refs.contentBatchEdit.radio1 = this.radio1;
      // this.$refs.contentBatchEdit.init();
      let IntelligenceBoard = that.batchForm.eqList;
      for (let i = 0; i < IntelligenceBoard.length; i++) {
        var arr = IntelligenceBoard[i].eqType;
        if (IntelligenceBoard[0].eqType != arr) {
          return that.$modal.msgWarning("设备类型不一致");
        }
      }
      let brr = [];
      that.batchForm.eqList.forEach((item) => {
        brr.push(item.eqType);
      });
      if (brr.every(this.everyCheck) == false) {
        //如果是情报板设备，则取消验证
        this.rules.state = true;
        // this.$refs.contentBatchEdit.init();
        // 上面已经判断过是否一致，所以此处不再判断，直接传第一个设备的eqid
        // 打开情报板编辑页面
        getDeviceBase(that.batchForm.eqList[0].eqId).then((data) => {
          this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
        });
        // console.log(item,'情报板信息');
        this.dialogVisible = true;
        let batchFormList = that.batchForm.eqList;
        let deviceList = "";
        batchFormList.forEach((item) => {
          deviceList = deviceList.concat(item.eqId);
        });

        this.$refs.vmsContentUpdate.deviceId = deviceList;
        this.$refs.vmsContentUpdate.isAdd = false;
        this.$refs.vmsContentUpdate.init();
        return false;
      }

      // end
      this.$refs[formName].validate((valid) => {
        let eqIdList = [];
        if (valid) {
          for (let i = 0; i < that.batchForm.eqList.length; i++) {
            eqIdList.push(that.batchForm.eqList[i].eqId);
          }
          if (eqIdList.length > 0) {
            for (let i = 0; i < eqIdList.length; i++) {
              let param = {
                devId: eqIdList[i],
                devType: that.batchForm.eqType,
                state: that.batchForm.state,
                tunnelId: that.currentTunnel.id,
              };
              that.setConfigData(param);
              that.sendAnalogCommand({
                deviceId: that.batchForm.eqList[i].eqId,
                devType: that.batchForm.eqList[i].eqType,
                hostId: that.batchForm.eqList[i].eqHostId,
                state: that.batchForm.state,
                tunnelId: that.currentTunnel.id,
              });

              that.$refs["batchForm"].clearValidate();
              that.batchVisible = false;
            }
            // this.dialogVisible = true;
            // this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,eqIdList);
          }
        } else {
          return false;
        }
      });
    },
    // 点击某一行，将其选中
    handleRowClick(row, i, a) {
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    //========================================批量配置结束================================================
    //========================================控制策略开始================================================
    /* 跳至策略页面*/
    strategyPage() {
      //this.$router.push('/strategy/index')
      this.loading = true;
      this.strategyVisible = true;
      this.title = "控制策略";
      if (this.currentTunnel.id) {
        listStrategy({
          strategyType: 0,
          tunnelId: this.currentTunnel.id,
        }).then((response) => {
          this.strategyList = response.rows;
          this.loading = false;
        });
      }
    },
    // 关闭控制策略对话框
    strategyCancel() {
      this.strategyVisible = false;
    },
    // 发送控制指令
    handleController(row) {
      this.$modal.msgSuccess("手动控制中.......");
      handleStrategy(row.id);
    },
    // 查看策略，表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    //========================================控制策略结束================================================
    /* 跳至操作日志页面*/
    operationLogPage() {
      // this.$router.push({
      //   name: "OperationLog",
      // });
      this.title = "操作日志";
      this.operationLogDialog = true;
      this.getList();
    },
    /* 打开图标说明对话框*/
    iconExplain() {
      this.explainVisible = true;
      this.title = "图标含义";
    },
    /* 关闭所有对话框*/
    cancel() {
      this.operationLogDialog = false;
      this.tunnelVisible = false;
      this.stateSwitchVisible = false;
      this.explainVisible = false;
      this.batchVisible = false;
      this.shuibengVisible = false;
      this.lumianVisible = false;
      this.weiboVisible = false;
      this.cameraVisible = false;
      this.daolujiebingVisible = false;
      this.lightControlDialog = false;
      this.youdaoVisible = false;
      this.cameraErrorInfo = "";
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
      this.stateForm = {};
    },
    handleTableWheel(event) {
      console.log(event, "event");
      // let obj = document.getElementsByClassName('content')
      let obj = this.$refs.divRoller;
      if (this.handleTableWheelSwithch == true) {
        this.tableZoom(obj, event);
      }
    },
    tableZoom(obj, event) {
      console.log(obj, event, "obj, event");
      // 一开始默认是100%
      let zoom = parseInt(obj.style.zoom, 10) || 100;
      // 滚轮滚一下wheelDelta的值增加或减少120
      zoom += event.wheelDelta / (Math.abs(event.wheelDelta) / 10);

      // zoom > 130 ? zoom = 100 : ''
      zoom < 60 ? (zoom = 60) : "";
      zoom > 140 ? (zoom = 140) : "";
      this.zoom = zoom;
      this.setMoveTop(zoom);
      if (zoom > 0) {
        obj.style.zoom = zoom + "%";
      }
      this.fnWheel(obj, event, zoom);
      return false;
    },
    setMoveTop(zoom) {
      console.log(zoom, "zoom");
      var ii = 0;
      // if(!zoom){
      //   zoom = 100
      // }
      switch (zoom) {
        case 110:
          ii = 1;
          break;
        case 120:
          ii = 3.5;
          break;
        case 130:
          ii = 7;
          break;
        case 140:
          ii = 11;
          break;
        default:
          ii = 0;
          break;
      }
      if (zoom == 90) {
        ii = 1; //0.11
      } else if (zoom == 80) {
        ii = 5; //0.25
      } else if (zoom == 70) {
        ii = 13; //0.43
      } else if (zoom == 60) {
        ii = 26; //0.66
      } else if (zoom == 50) {
        ii = 50; //1
      } else if (zoom == 40) {
        ii = 90; //1.5
      } else if (zoom == 30) {
        ii = 164; //2.34
      } else if (zoom == 20) {
        ii = 320; //4
      } else if (zoom == 10 || zoom == 0) {
        ii = 800; //9
      }
      this.moveTop = (100 - zoom + ii) / 100;
    },
    fnWheel(obj, e, zoom) {
      // 思路：以鼠标为中心实现滚动的话，那么将会鼠标在背景图片（注意我这里是用背景图片的，不是img的）中滚动的时候的位置比率是不会变的
      if (e.wheelDelta > 0) {
        this.wheelFlag = true;
      } else {
        this.wheelFlag = false;
      }
      console.log(this.$refs.divRoller, "this.$refs.divRoller");
      this.oldWidth = this.$refs.divRoller.offsetWidth;
      this.oldHeight = this.$refs.divRoller.offsetHeight;
      this.mouseLeft = e.clientX - this.$refs.divRoller.offsetLeft;
      this.mouseTop = e.clientY - this.$refs.divRoller.offsetTop;
      // 分别计算出scaleX,scaleY的倍数
      this.scaleX = this.mouseLeft / this.oldWidth;
      this.scaleY = this.mouseTop / this.oldHeight;

      obj.style.zoom = zoom + "%";
      // 鼠标为中心的开始点,如果去掉这个将会以左上角开始滚动图片
      this.newWidth = this.$refs.divCanvas.offsetWidth;
      this.newHeight = this.$refs.divCanvas.offsetHeight;
      obj.style.left = zoom + "%";
      obj.style.top =
        this.$refs.divRoller.offsetTop -
        this.scaleY * (this.newHeight - this.oldHeight) +
        "px";
    },
    // 所有指示器控制 打开弹窗
    // lightControl(){
    //   this.lightControlDialog = true
    //   this.title = '车指批量控制'
    //   //获取当前选择的是哪个隧道
    //   const index = this.lightControForm.index
    //   listTunnels().then(response => {
    //       //获取当前隧道有几个车道
    //       this.lightControForm.lane = response.rows[index].lane
    //       if(!this.lightControForm.name){
    //           //获取当前隧道名字
    //           this.lightControForm.name = response.rows[0].tunnelName
    //       }
    //   });
    // },
    // 所有车指器弹窗确定按钮
    // lightSubmitState(){
    //     // 所有隧道的所有设备
    //     var itemArr = []
    //     listDevices({eqType:1}).then((response) => {
    //             for(var item of response.rows){
    //                 if(this.lightControForm.name == item.tunnelName.tunnelName){
    //                     // 通过隧道名 获取当前隧道的所有设备
    //                     if(this.lightControForm.lCDirection == item.eqDirection){
    //                         // 当前选中的车道 和 设备所属车道对应上 获取该车道里设备的id
    //                         if(this.lightControForm.state == item.lane){
    //                             itemArr = itemArr.concat(item)
    //                         }
    //                     }
    //                 }
    //             }
    //           for(var index of itemArr){
    //               let param = {
    //                 devId: index.eqId,
    //                 devType: index.eqType,
    //                 state: index.lane,
    //                 tunnelId: index.eqTunnelId,
    //                 hostId: index.eqHostId,
    //               }
    //               setConfigData(param).then(response => {
    //                 this.lightControForm.lCDirection = ''
    //                 this.lightControForm.state = ''
    //                 this.lightControForm.radioLightControlTop = ''
    //                 this.lightControForm.radioLightControlBottom = ''

    //               }).catch(err => {
    //               })
    //           }

    //     });

    //     this.lightControlDialog = false
    // },
    // changeLightControlTop(){
    //     if(!this.lCTop){
    //         this.lCTop = true
    //     }else{
    //         this.lCTop = false
    //     }
    // },
    // changeLightControlBottom(){
    //     if(!this.lCBottom){
    //         this.lCBottom = true
    //     }else{
    //         this.lCBottom = false
    //     }
    // },
    // 模拟量曲线图
    alarmsCharts(type) {
      let units = "";
      if (type == 14) units = "ppm"; // CO
      // if(type == 13) units = 'ppm' // 风向
      if (type == 5) units = "cd/m2"; // 光强
      if (type == 15) units = "ppm"; // 能见度
      if (type == 16) units = "m/s"; // 能见度
      var xData = [];
      for (let i = 2; i <= 24; i += 2) {
        xData.push(i + ":00");
      }
      var monthCharts = echarts.init(document.getElementById("analogQuantity"));
      let yData = [0, 1, 1, 2, 0, 1, 1, 1, 0, 1, 1, 0];

      var option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
              fontSize: 12,
            },
          },
          textStyle: {
            fontSize: "100%",
          },
          formatter: (v) => {
            return `
                    <div class='u-p-2'>
                      <div class='fz-10'>时间：${v[0].axisValue}</div>
                      <div class='fz-10 u-mt-2'>模拟量：${v[0].value}${units}</div>
                    </div>
                  `;
          },
        },
        grid: {
          top: "20%",
          left: "10%",
          bottom: "15%",
        },
        xAxis: {
          type: "category",
          boundaryGap: false, // 两边留白
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: 14,
            },
          },
          data: xData,
          axisLine: {
            lineStyle: {
              color: "#000",
              width: 1,
            },
          },
          splitLine: {
            lineStyle: {
              type: "dashed",
              color: "rgba(255,255,255,.2)",
              width: 1,
            },
            show: false,
          },
        },
        yAxis: {
          name: units,
          type: "value",
          scale: true,
          nameTextStyle: {
            color: "#fff",
            align: "left",
            padding: [0, 10, 0, 0],
            fontSize: "100%",
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: "100%",
            },
            margin: 8,
          },
          axisLine: {
            lineStyle: {
              color: "#000",
              width: 1,
            },
          },
          splitLine: {
            lineStyle: {
              type: "solid",
              color: "rgba(255,255,255,.2)",
              width: 1,
            },
            show: false,
          },
        },
        series: {
          name: "",
          type: "line",
          stack: "",
          // 修改的是线下区域的颜色
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: "rgba(236, 99, 123, .5)",
              },
              {
                offset: 1,
                color: "rgba(102, 212, 250,.0)",
              },
            ]),
          },
          // 修改的是线的颜色
          lineStyle: {
            color: {
              type: "linear",
              x: 0,
              y: 1,
              x2: 0,
              y2: 0,
              // 0% 处的颜色                           // 100% 处的颜色
              colorStops: [
                {
                  offset: 0,
                  color: "#3a76f6",
                },
                {
                  offset: 0.25,
                  color: "#66d4fa",
                },
                {
                  offset: 0.75,
                  color: "#f8d470",
                },
                {
                  offset: 1,
                  color: "#ec637b",
                },
              ],
              global: false, // 缺省为 false
            },
            width: 2,
          },
          //图例样式，默认空心圆，设为none是实心，还有'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow', 'none'
          symbol: "none",
          data: yData,
        },
      };
      // 使用刚指定的配置项和数据显示图表。
      monthCharts.setOption(option);
    },
    // 跳转页面
    jumpLink(url) {
      if (url == "/15/status") {
        this.$modal.msgWarning("跳转页面暂未完成");
        return;
      }
      this.$router.push({
        path: url,
        query: {},
      });
    },
    // 监听缩放比例变化
    zoomChange(val) {
      val < 70 ? (val = this.zoom) : "";
      val > 130 ? (val = this.zoom) : "";
      if (val) {
        this.$refs.divRoller.style.zoom = val + "%";
        this.setMoveTop(val);
      }
    },
  },
  //实例销毁前清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
    this.timer = null;
    clearInterval(this.imageTimer);
    this.imageTimer = null;
  },
};
</script>

<style lang="scss" scoped>
.batchManageButton {
  width: 120px;
  display: flex;
  justify-content: space-around;
  padding: 0 5px;
  color: #e1feff;
  background: linear-gradient(2deg, #00070d, #005aa8) !important;
  border: 1px solid #00c8ff;
  font-size: 12px;
  height: 32px;
  align-items: center;
  margin-right: 10px;
  border-radius: 3px;
  color: white;
  text-align: center;
  > div {
    width: 50px;
    height: 20px;
    border-radius: 13px;
    line-height: 20px;
    cursor: pointer;
  }
  > div:nth-of-type(1) {
    background: #c8c8c8;
  }
  > div:nth-of-type(2) {
    background: #00b0ff;
  }
}
.screenEqNameClass {
  border: solid 2px #09c3fc;
  border-radius: 4px;
}
.screenEqNameBox {
  width: 120px;
  height: 40px;
  position: absolute;
  top: -40px;
  left: 30px;
  line-height: 28px;
  text-align: center;
  font-size: 10px;
  background-image: url(../../../assets/cloudControl/screenEqName.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  color: #07c3fc;
}
.textFalseBox {
  width: 120px;
  height: 40px;
  position: absolute;
  top: -40px;
  left: 30px;
  line-height: 28px;
  text-align: center;
  font-size: 10px;
  background-image: url(../../../assets/cloudControl/screenEqName.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  color: #da4a64;
  opacity: 0;
  animation: fadenum 2s;
}
@keyframes fadenum {
  0% {
    opacity: 1;
  }
  99% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.siblings {
  position: fixed;
  top: 121px;
  width: 100%;
  height: 61.8%;

  .eqTypeListClass {
    float: left;
    width: 8%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  //车道控制
  .indicatorLight {
    width: 30px;
    height: 33%;
    background: linear-gradient(
      90deg,
      rgba($color: #00aced, $alpha: 0.8),
      rgba($color: #0079db, $alpha: 0.8)
    );
    color: white;
    writing-mode: vertical-lr;
    letter-spacing: 5px;
    font-size: 16px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  //照明控制
  .brightnessControl {
    width: 30px;
    height: 33%;
    background: linear-gradient(
      90deg,
      rgba($color: #00aced, $alpha: 0.8),
      rgba($color: #0079db, $alpha: 0.8)
    );
    color: white;
    //垂直向下
    writing-mode: vertical-lr;
    text-align: center;
    //文字间隔
    letter-spacing: 5px;
    font-size: 16px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .el-icon-close:before {
    content: "\e6d9" !important;
  }
}
// 触发控制模块
.triggerControl {
  width: 30px;
  height: 33%;
  background: linear-gradient(
    90deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  );
  color: white;
  //垂直向下
  writing-mode: vertical-lr;
  text-align: center;
  //文字间隔
  letter-spacing: 5px;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

//抽屉的高度
::v-deep .el-drawer.rtl {
  // height:49%;
  // top:239px;
  width: 21% !important;
  font-size: 14px;

  ::-webkit-scrollbar-track-piece {
    background-color: rgba($color: #00c2ff, $alpha: 0.1);
    border-left: 1px solid rgba(0, 0, 0, 0);
    width: 1000px;
    border-width: 50px;
  }

  ::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba($color: #00c2ff, $alpha: 0.6);
    background-clip: padding-box;

    min-height: 28px;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #00c2ff;
  }
}

.drawerTop {
  height: 62%;
  top: 130px;
  right: 38px;
}
.drawerCenter {
  height: 62%;
  top: 130px;
  right: 38px;

  // top: 33%;
}
.drawerBottom {
  height: 62%;
  top: 130px;
  right: 38px;

  // top: 54%;
}
.drawerBox {
  width: 25%;
  height: 25%;
  align-items: center;
  .drawerDirection {
    width: 100%;
    height: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
.drawerCheckbox {
  width: 100%;
  height: 50%;
  display: flex;
  align-items: center;
}
.control {
  width: 40px !important;
  height: 26px !important;
  text-align: center;
  line-height: 22px;
  padding: 0px 0px !important;
  font-size: 12px;
  // background-color: #00c2ff;
  // border-color: #00c2ff;
}

//小三角位置
// ::v-deep .el-icon-caret-left:before{
//   margin-top:-60px;
// }

//多选框选中样式
::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: white;
  width: 26px;
  height: 26px;
  border: solid 1px #fff;
  // box-shadow: 1px 1px 2px #dcad76;
}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner::after {
  height: 9px;
  width: 6px;
  border: 2px solid #dcad76; // 是改这里的颜色
  border-left: 0;
  border-top: 0;
  top: 6px;
  left: 7px;
}
::v-deep .rtl .el-checkbox__input {
  transform: translateX(28px);
}
::v-deep .rtl .el-checkbox__label {
  transform: translateX(-25px);
  width: 26px;
  height: 26px;
  background: #2fc83a;
  line-height: 26px;
  color: white;
}
::v-deep .rtl {
  .checkbox {
    margin-right: 20px !important;
  }
  .el-checkbox__input.is-checked + .el-checkbox__label {
    background: #bd0a0a;
  }
}
//title
::v-deep .el-drawer__header {
  // background-color: #00C9FE;
  font-size: 14px;
  margin-bottom: 0px !important;
  height: 36px;
  line-height: 40px;
  // padding: 0 10px;
  color: #fff;
  // border-left: 5px solid #F19E39;
}

//开关

::v-deep .el-drawer__body {
  .bingZhou {
    display: flex;
    // width: 56px;
    // height: 28px;
    height: 50%;
    padding: 20px;

    border: solid 1px red;
    font-size: 12px;
    display: flex;
    > .number {
      width: 28px;
      height: 28px;
      // background-color:#00C7FE !important;
      color: white;
      text-align: center;
      line-height: 28px;
      vertical-align: middle;
    }
    > .checkbox {
      width: 28px;
      height: 28px;
    }
  }

  .ledLighting {
    height: 36px;
    // background-color: #4EAACF;
    line-height: 40px;
    padding-left: 14px;
    font-size: 14px;
    // color: #fff;

    // .el-switch__core:after {
    //   background-color: #0f8ab9;
    // }
  }

  .Time {
    display: flex;
    align-items: flex-start;
    height: 50px;
    padding-top: 10px;

    .setTime {
      white-space: nowrap;
      line-height: 32px;
    }

    .timeStart {
      display: flex;
      margin-right: 10px;
      margin-left: 14px;

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .timeEnd {
      display: flex;

      .el-button--mini {
        padding: 0px 0px !important;
      }

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .handleLightClass {
      height: 28px;
      margin-left: 5px;
      width: 40px;
      text-align: center;
      // background-color: #07C2FF !important;
      border: none;
    }
  }
}

::v-deep .el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 86px;
}

// ::v-deep .el-input--prefix .el-input__inner{
//       padding-left: 26px !important;
//       border: solid 1px #1088B9;
//       box-shadow:1px 1px 2px #02C6FA;
// }
// ::v-deep .el-input--suffix .el-input__inner {
//     padding-right: 10px;
//     border: solid 1px #1088B9;
// }
::v-deep .el-input--medium .el-input__inner {
  width: 5.4vw;
}

::v-deep .el-button--medium {
  margin-left: 20px;
}

::v-deep .el-checkbox__inner {
  width: 26px;
  height: 26px;
  border: 1px solid #fff;
  border-radius: 0px;
}

.vehicleLane {
  height: 68%;
  align-items: center;
  width: 100%;
  display: flex;
  position: relative;
  margin-top: 2px;
}
.contentTopNav {
  width: 85%;
  left: 0px;
}
// .openSidebar .contentLeftNav{
//   width:88%;left:3.2%
// }
// .hideSidebar .contentLeftNav{
//   width:88%;left:2%
// }

#vehicle,
#energyConsumption,
#focusCar {
  width: 100%;
  height: calc(100% - 2vw);
  margin-top: 5px;
}

.el-input-number {
  width: 5.4vw;
  line-height: 28px;

  // .el-input-number__decrease, .el-input-number__increase {
  //   color: #e1feff;
  //   background-color: transparent !important;
  //   border: unset;
  //   width: 2vw;
  //   font-size: 10px;
  //   :hover {
  //     font-size: 15px;
  //   }
  // }
  // .el-input__inner {
  //   height: 30px;
  //   padding: 0;
  //   color: #e1feff;
  //   background-color: #0b1a46 !important;
  //   border: solid 1px #0b1a46;
  //   // box-shadow: 0px 0px 10px #68B5FF;
  // }
  // }
}

.footer {
  width: 100%;
  height: 25%;
  padding: 0px 30px;
  // margin-top: 10px;
  display: flex;
  padding-bottom: 5px;
  justify-content: space-between;
  margin-top: 8px;
  .footTitle {
    padding: 5px 20px;
    // line-height: 25px;
    font-size: 14px;
    display: flex;
    align-items: center;
    font-family: inherit;

    .footTriangle {
      width: 0;
      height: 0;
      border-top: 5px solid transparent;
      border-left: 10px solid #1590ff;
      border-bottom: 5px solid transparent;
    }

    .footTitleCont {
      width: 100%;
      height: 24px;
      margin-top: -4px;
      font-weight: bold;
      display: flex;
      align-items: center;
      p:nth-of-type(2) {
        text-transform: uppercase;
        // font-weight: lighter;
        transform: scale(0.8, 0.8);
        opacity: 0.35;
      }
    }
  }

  .footBigBox {
    width: 52%;
    height: 100%;
  }

  .footMiniBox {
    width: 24.5%;
    height: 100%;
    overflow: hidden;
    // background-image: url(../../../assets/cloudControl/footer_bg.png);
    // background-position: center;
    // background-repeat: no-repeat;
    // background-size: 100% 100%;
    // background-color: rgba($color: #0b1329, $alpha: 0.4);
    // border: solid 1px #183b57;
    // color: white;

    .listContent {
      height: 70%;
      font-size: 14px;
      margin-top: 5px;
      overflow: hidden;
    }
  }
}

.rightButtonS {
  margin-top: 32px;
  font-size: 16px;
  // color: #0a88bd;
  width: 120px;
  height: 6vh;
  line-height: 6vh;
  padding-left: 20px;
  // background-image: url(../../../assets/cloudControl/rightButton.png);
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
  caret-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: left;

  img {
    width: 20px;
    height: 100%;
    object-fit: contain;
    margin-left: 20px;
    margin-right: 8px;
  }
}

.leftButtonS {
  position: relative;
  left: 0px;
  font-size: 16px;
  // width: 125px;
  height: 46px;
  line-height: 46px;
  font-weight: 500;
  caret-color: rgba(0, 0, 0, 0);
  text-align: center;
  margin-left: 16px;
  border-radius: 2px;
  cursor: pointer;
  img {
    width: 20px;
    height: 100%;
    object-fit: contain;
    margin-left: 8px;
    margin-right: 20px;
  }
}
.topNavRightDeawer {
  right: 0;
}
.openSidebar .leftNavRightDeawer {
  right: 240px;
}
.hideSidebar .leftNavRightDeawer {
  right: 0px;
}
// .bigTypeButton{
//     color: #baddff;
//     background-image: url(../../../assets/cloudControl/right_button.png);
// }
// .bigTypeButton:hover{
//     color: #fff;
//     background-image: url(../../../assets/cloudControl/right_button_hover2.png);
// }

.tunnelBox {
  overflow: hidden;
  margin-left: 100px;
  width: 85%;
  height: 2%;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.tunnelBoxTop {
  background-image: url(../../../assets/cloudControl/top.png);
  margin-top: 10px;
  z-index: 999;
}

.tunnelBoxBottom {
  background-image: url(../../../assets/cloudControl/bottom.png);
}

::v-deep .menu-b .el-select-dropdown {
  background-color: #03192e !important;
  color: white;
  border: solid 1px #e1feff;
  z-index: 100;
}

// ::v-deep .menu-b .el-input--mini .el-input__inner{
//     background-color: #e2f3fa !important;
//     border: solid 1px #0a88bd;
//     color: #0a88bd;
//     // box-shadow: 0px 0px 10px #68B5FF;
//     width: 170px;
// }
::v-deep .menu-b .el-select .el-input .el-select__caret {
  color: #e1feff;
}

::v-deep .menu-b .el-select > .el-input {
  margin-top: 5px;
}
.menu-b .el-button-group > .el-button + .el-button {
  margin-left: 10px;
}

// ::v-deep .menu-b .el-select--small{
//     transform: translateY(-6px);
// }

// 火灾报警弹窗
::v-deep .temperatureDialog .el-dialog {
  margin-top: 15vw !important;
  border: solid 4px #9f0000;
  background-color: rgba($color: #9d0000, $alpha: 0.5);
  border-radius: 4px;

  .el-dialog__body {
    display: flex;
    font-size: 20px;
    text-align: center;
    padding: 0%;

    > div {
      width: 50%;
      color: white;
      margin-bottom: 10%;

      > div:first-of-type {
        color: #c9c9c9;
        line-height: 50px;
      }
    }

    > div:nth-of-type(1) {
      border-right: solid 1px rgba(255, 255, 255, 0.5);
      z-index: 10;
    }

    > div:nth-of-type(2) {
      z-index: 10;
    }

    .HZdialogBg {
      width: 100%;
      height: calc(100% - 54px);
      position: absolute;
      background: url(../../../assets/image/fire_bg.png) no-repeat;
      opacity: 0.3;
      background-size: 85% 200%;
      background-position: 130px -20px;
    }
  }
}

// 隧道温度
.temperature {
  font-size: 16px;
  font-weight: bold;
  position: absolute;

  img {
    width: 25px;
    height: 25px;
  }
}

// 字幕滚动
.seamless-warp2 {
  background-image: url("../../../assets/image/scroll_bg.png");
  // transform: rotate(-90deg);
  // height: 25px;
  // width: 100px;
  overflow: hidden;
  position: absolute;
  top: 32%;
  right: 20%;
  background-size: 100% 100%;

  .item {
    padding-left: 0px;
    width: 400px;

    li {
      list-style: none;
      float: left;
      margin-right: 20px;
      letter-spacing: 2px;
      color: white;
      width: 30px;
      height: 150px;
    }
  }
}

.item {
  margin: 4px;
  background-color: transparent;
}

.left .el-tooltip__popper,
.right .el-tooltip__popper {
  padding: 8px 10px;
  background-color: #0000ff;
}

.app-container {
  padding: 0px !important;
}

.header {
  // color: #ced8e4;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.workbench-header {
  padding-right: 20px;
  height: 45px;
  margin-top: 2px;
  display: flex;
  justify-content: space-between;
}

.flex-row {
  display: flex;
  // flex-direction: row;
  height: 32px;
  align-items: center;
}

.my-back {
  // background-color: #cdedfa;
  // background-image: url("../../../assets/image/bg.png");
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-size: cover;
  -webkit-background-size: cover;
  /* 兼容Webkit内核浏览器如Chrome和Safari */
  -o-background-size: cover;
  /* 兼容Opera */
  zoom: 1;
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  // justify-content: center;
  position: relative;
}

.menu {
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.workbench-menu {
  padding: 5px 20px 10px 10px;
}

/* 隧道选项*/
.menu-button {
  float: left;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  display: flex;
  flex-direction: row;
  // flex-wrap: wrap;
  align-items: center;
}

/*自定义info类型按钮样式*/
// .el-button--info {
//   // color: #C0CCDA;
//   // background-color: #2e354f;
//   // border: 1px solid #4F5C69;
//   color: #0a88bd;
//   background-color: #112c5b !important;
//   border: 1px solid #3b548c;
// }

/* 鼠标在按钮上悬浮*/
// .el-button:hover {
//   background-color: #3a4e86; // #4e5e89
//   border: 1px solid #7f8c98;
// }

.tunnel-checkbox {
  margin: 10px 0 10px 10px;
  color: #c0ccda;
}

/* 显示编号*/
.display-box {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  padding-right: 10px;
  height: 32px;
}

.menu-title {
  width: 110px;
  color: #ced8e4;
  font-size: 15px;
  margin-bottom: 0px;
  margin-left: 30px;
  line-height: 40px;
}

//  .theme-dark .content::-webkit-scrollbar-thumb{
//       animation: movegundong 60s infinite linear;
//       // background-color:red;
//   }
// @keyframes movegundong {
//        0% {
//           transform: translateX(0px);
//       }
//       50% {
//           transform: translateX(200px);
//       }
//       75% {
//           transform: translateX(800px);
//       }
//       100% {
//           transform: translateX(1000px);
//       }
//    }

.system-state-normal {
  color: #00aa00;
  font-size: 15px;
  margin-bottom: 0px;
}

.system-state-abnormal {
  color: #ff0000;
  font-size: 15px;
  margin-bottom: 0px;
}

.menu-button-group {
  width: 90%;
  display: inline-block;

  // transform: translateY(-6px);
  .el-button {
    margin: 5px;
  }
}

/* 图片区域*/
.content {
  clear: both;
  text-align: center;
  width: 88%;
  height: 100%;
  display: flex;
  align-items: center;
  overflow-y: hidden;
  zoom: 100%;
  overflow-x: auto;
  display: inline-block;
  margin: 0 auto;
}

.workbench-content {
  // width: 90%;
  height: 650px;

  // position: absolute;
  // top: 7%;
  // -webkit-user-select: none;
  // user-select: none;
  // display: flex;
  // justify-content: center;
  // overflow-x: auto;
  > div {
    width: 100%;
  }
}

// .content::-webkit-scrollbar-track-piece {
//     background-color: rgba(0, 0, 0, 0.1);
//     border-left: 1px solid rgba(0, 0, 0, 0);
// }
// .content::-webkit-scrollbar {
//     width: 5px;
//     height: 13px;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
// }
// .content::-webkit-scrollbar-thumb {
//     background-color: rgba($color: #00c2ff, $alpha: 0.6);
//     background-clip: padding-box;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
//     min-height: 28px;
// }
// .content::-webkit-scrollbar-thumb:hover {
//     background-color: #00c2ff;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
// }

.wb-direction-img {
  margin-bottom: 10px;
  padding-right: 20px;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;

  img {
    height: 10px;
    width: 70px;
  }
}

.details-row {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  height: 45px;
  // width: 1442px;
  // position: relative;
  z-index: 3;
  margin-left: 90px;
}

.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  // align-items: center;
  width: 0px !important;
}

// 鼠标经过盒子
.mouseHover {
  &:hover {
    z-index: 10;

    input {
      cursor: pointer;
      background-color: #eee;
      color: #000;
    }
  }
}

/* 被选择后*/
.focus {
  border: 1.3px dashed #1890ff;
  // height: 32px;
  border-radius: 4px;
  margin-bottom: 2px;
}

/* 详情信息*/
.input-box {
  width: 140px;
  height: 45px;
  border-radius: 4px;
  background-color: #192348;
  text-align: center;
  color: rgb(191, 203, 217);
  /*  padding: 10px 10px; */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  display: inline-block;
  position: absolute;
}

/* 风速等输入框*/
.details-input {
  background-color: #192348;
  border-color: #192348;
  color: #6ea2ec;
  width: 45px;
  padding: 0;
  border-radius: 2px;
}

/* 编号等输入框*/
.config-img-input {
  width: 80px;
  // background-color: #545559;
  // background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 0px 0 2px 0;
  border: 0;
  margin: -5px;
  border-radius: 2px;
  // color: #a2a2a3;
}

.s-config-img-input {
  width: 80px;
  // background-color: #545559;
  // background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 0px 0 2px 0;
  border: 0;
  margin: -7px;
  border-radius: 2px;
  // color: #a2a2a3;
}

.el-input--small {
  // margin-top: 6px;
  font-size: 14px;
}

.small-title {
  margin: 0;
  color: #6ea2ec;
}

.param-name {
  width: 140px;
  color: #afafb0;
  font-size: 12px;
  margin-right: 0;
}

/* 车道图片*/
.lane-img {
  margin: 10px 10px 0 10px;
  width: 200px;
  height: 90px;
}

.dialog-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.lane-img-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 10px 0 0 5px;
}

.lane-radio {
  margin-top: 10px;
}

.equipment-img-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
  width: 100px;
  margin: 10px;
}

/* 画布区域*/
.config-img-area {
  // display: flex;
  // justify-content: center;
  // align-items: center;
  height: 580px;
  width: 100%;
  background-size: cover;
  // 工作台加滚动条 居中后左侧超出部分会溢出 加下面三行
  // position: absolute;
  inset: 0px;
  margin: 0px auto;
}

.back-img {
  height: 580px;
  // width: 1630px !important;
  position: absolute;
  display: block;
  // top: -22px;
}

.equipment-img {
  max-height: 150px;
  max-width: 150px;
}

.s-equipment-img {
  height: 20px;
  width: 22px;
}

.wrapper {
  height: 580px;
  width: 100%;
  /* border: 1px solid #000; */
  border-radius: 10px;
  position: relative;
  z-index: 1;
}

input {
  width: 200px;
  padding: 20px 30px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-size: 12px;
  text-align: center;
}

/* 右键菜单 */
.contextmenu {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: absolute;
  list-style-type: none;
  padding: 0 5px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: row;
  align-items: center;

  i {
    margin-right: 5px;
  }

  li {
    margin: 0;
    padding: 5px 5px;
    cursor: pointer;

    &:hover {
      background: #eee;
    }
  }
}

// 自动换行
::v-deep .wrap {
  word-wrap: break-word;
  word-break: normal;
  width: 100%;
  // .el-radio__input.is-checked + .el-radio__label{
  //   color: #c0ccda !important;
  // }
}

.form-direction {
  width: 60px;
  justify-content: space-around;
}

.el-radio__input.is-checked + .el-radio__label {
  color: #c0ccda !important;
  font-weight: bold;
}

/* 级联选择器 */
//  ::v-deep  .siteClass {
//   line-height: unset;
// 	/* 2022.03.25 修改
// 	width: 250px;
// 	margin-left: 100px;
// 	*/
// 	margin-left: 1vw;
//     .el-input__suffix{
//       color: #0a88bd !important;
//     }
//     .el-icon-arrow-down:before{
//       content:"\e6df";
//       border: dashed 1px #746a47;
//       color: #0a88bd;
//       font-size: 13px;
//     }
// }

// 机器人图片盒子
.robotBox {
  position: absolute;
  right: 0;
  top: 0;
  background-color: transparent;

  .el-image {
    width: 200px;
    height: 200px;
    vertical-align: middle;

    .el-image__error {
      background-color: lightslategray;
    }
  }

  .switchIcon {
    position: absolute;
    display: none;
    left: 50%;
    top: 35%;
    font-size: 20px;
    cursor: pointer;
    color: #fff;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    transform: translateX(-50%) rotate(90deg);
    z-index: 1;

    &:hover {
      border-color: rgb(104, 181, 255);
      box-shadow: rgb(104 181 255) 0px 0px 10px;
      color: black;
      background-color: powderblue !important;
    }
  }

  &:hover {
    .switchIcon {
      display: inline-block;
    }
  }
}

.robotTabs {
  ::v-deep .el-tabs__header {
    .el-tabs__nav {
      border: none;

      .el-tabs__item {
        border: none;
        // color: #fff;
      }

      .el-tabs__item.is-active {
        color: cornflowerblue;
      }

      .el-tabs__item:hover {
        color: skyblue;
      }

      .el-tabs__active-bar {
        background-color: blueviolet;
      }
    }
  }

  .el-tab-pane {
    div {
      margin: 5px 0;
    }
  }
}

#robotEchart {
  width: 100%;
  height: 15rem;
}
.eventDiglog,
.operationDiglog {
  .el-dialog .el-form {
    padding: 15px !important;
    .el-form-item__content .el-button {
      width: 88px;
      height: 22px;
      border: none;
    }
    .el-form-item__content .el-button--mini {
      padding: 2px 15px !important;
    }
  }
  .el-table {
    padding: 0 15px;
    margin-bottom: 60px;
  }
}
::v-deep .eventDiglog .el-button--medium {
  height: 22px !important;
  line-height: 22px !important;
  padding: 0px !important;
}
.eventDiglog .el-table {
  padding: 15px;
  padding-top: 0;
  background-color: transparent !important;
  margin-bottom: 65px;
}
.el-table .fixed-width .el-button--mini {
  padding-left: 7px;
  padding-right: 10px;
  width: inherit;
  color: white;
}

// ::v-deep .switchStyle .el-switch__core:after{
//   background: #62708d;
// }
// ::v-deep .is-checked.el-switch__core:after{
//   background: white;
// }
// .el-button-group .el-button--info:last-child{
//   border-left-color:#3B548C !important;
// }
.el-button-group > .el-button:last-child {
  border-top-left-radius: 4px !important;
  border-bottom-left-radius: 4px !important;
}

.el-button-group > .el-button:first-child {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

// .el-button-group .el-button--info:first-child{
//   border-right-color:#3B548C !important;
// }
// .el-button-group .el-button--info:not(:first-child):not(:last-child){
//   border-left-color: #3B548C !important;
//   border-right-color: #3B548C !important;
// }
.el-button-group > .el-button:not(:first-child):not(:last-child) {
  border-radius: 4px !important;
}

::v-deep .app-main {
  height: calc(100vh - 52px) !important;
}
::v-deep .EqType31 .radioEqType31 .el-form-item__content {
  margin-left: 0px !important;
}
::v-deep .EqType31 .radioEqType31 .el-radio {
  padding: 0px 20px !important;
  height: 40px;
  line-height: 40px;
}
.reservePlan {
  cursor: pointer;
  width: 190px;
  border-radius: 5px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.reservePlan:hover {
  background: #00bbf5;
  width: 220px;
}
.paginationWorkbench {
  position: static;
  // bottom: 250px !important;
  height: 60px;
}
</style>
<style lang="scss">
.tipCase.el-tooltip__popper[x-placement^="left"] .popper__arrow:after {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="left"] .popper__arrow {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="right"] .popper__arrow:after {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="right"] .popper__arrow {
  display: none !important;
}

.tipCase {
  background: #cdedfa !important;
  border: solid 1px #1d58a9;
  color: #1d58a9 !important;
}

// .popper-class-site {
//   border-color: rgb(104, 181, 255);
//   box-shadow: rgb(104 181 255) 0px 0px 10px;
//   background-color: rgba(2, 19, 35, 1);

//   .el-cascader-menu {
//     color: #0a88bd;

//     .el-cascader-node {
//       &:hover {
//         color: #0a88bd;
//         background-color: rgba(176,224,230,0.8);
//       }
//     }
//     .el-cascader-node.in-active-path, .el-cascader-node.is-selectable.in-checked-path, .el-cascader-node.is-active {
//       box-shadow: rgb(104 181 255) 0px 0px 10px;
//       color: black;
//       background-color: powderblue;
//     }
//   }
// }
.workbench-msg {
  background-color: #1890ff;
  border-color: #1890ff;

  .el-message__content {
    color: #ffffff;
  }
}

/* 文字提示*/
.wb-tip.el-tooltip__popper {
  background-color: #153051;
  color: #fff;
}

/*左右方向*/
.wb-direction-input {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  padding-right: 10px;

  .el-input {
    width: 100px;

    .el-input__inner {
      background-color: transparent;
      border-color: transparent;
      color: #ced8e4;
      font-size: 15px;
      width: 95px;
      text-align: right;
    }
  }
}
.comClass {
  position: absolute;
  top: 0;
  left: 0;
}
.chezhiDrawerDirection {
  width: 100%;
  height: 30px;
  padding-left: 10px;
  line-height: 30px;
}
.chezhiDrawerInfo {
  width: 100%;
  height: 40px;
  padding: 0 10px 0 5px;
  display: flex;
  align-items: center;
  margin: 10px 0;
  .chezhiName {
    width: 40px;
    margin-left: 5px;
  }
  .chezhiLaneSelect {
    width: 136px;
  }
  .chezhiStateSelect {
    width: 100px;
  }
  .chezhiControlButton {
    width: 50px;
    height: 32px;
    padding: 0;
    // border:solid 1px #A3B7CF;
    border-radius: 2px;
    margin-left: 8px;
    text-align: center;
    line-height: 31px;
    cursor: pointer;
    color: white;
  }
  .chezhiControlButton:hover {
    color: white;
  }
}
/*单个设备配置框 */
.workbench-dialog {
  // .el-dialog{
  //     // left: 30%;
  //     background-image: url(../../../assets/cloudControl/dialog.png);
  //     background-repeat: no-repeat;
  //     background-size: 100% 100%;
  // }
  // .el-form-item__label {
  //   color: #c0ccda;
  // }

  .el-dialog__header {
    // background-color: #455d79;
    // color: #fff;
    height: 30px;
    padding: 0;
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    font-size: 14px;
  }

  .el-dialog__title {
    // color: #fff;
    font-size: 14px;
    line-height: 30px;
  }
  .el-dialog__headerbtn {
    top: 6px !important;
  }
  .el-dialog__body {
    // color: #c0ccda;
    // background-color: #304156;
    padding: 0;
  }

  .el-dialog__footer {
    // color: #c0ccda;
    // background-color: #304156;
    padding-top: 0;
  }

  .el-form-item__content {
    // display: flex;
    // flex-direction: row;
    // align-items: center;
    // margin-left: 0;
  }

  .form-item-img {
    width: 25px;
    height: 25px;
    margin-left: 10px;
    margin-right: 10px;
  }

  .el-radio {
    /* padding: 5px 300px 5px 20px; */
    padding: 5px 150px 5px 20px;
    margin: 2px 0px;
    color: #c0ccda;
    border-radius: 4px;

    &:hover {
      background-color: #455d79;
    }
  }

  .el-radio-selcted {
    padding: 5px 220px 5px 20px;
    margin: 2px 0px;
    color: #c0ccda;
    border-radius: 4px;
    background-color: #455d79;
  }

  /*select选择框的背景颜色*/
  .el-input__inner {
    background-color: #304156;
    color: #c0ccda;
  }

  /* 按钮*/
  .el-button {
    background-color: #455d79;
  }

  .el-icon-close:before {
    content: "\e79d";
    font-size: 20px;
  }
}
.realTimeTable {
  width: 100%;
  height: calc(100% - 50px);
  li {
    display: flex;
    justify-content: space-around;
    font-size: 12px;
    transform: translateX(-20px);
  }
}
/* 批量管理中的table*/
.batch-table {
  //table为空时
  // .el-table__empty-block {
  //   background-color: #fff;
  // }

  // .el-table .el-table__header-wrapper th {
  //   background-color: #fff !important;
  //   color:#0a88bd !important;
  // }

  // //鼠标停留在table
  // .el-table--enable-row-hover .el-table__body tr:hover > td {
  //   background-color: #455d79;
  // }

  //去掉边框
  .el-table__row > td {
    border: none;
  }

  .el-table th.is-leaf {
    border-bottom: none !important;
  }

  .el-table::before {
    height: 0;
  }

  // .el-table__body-wrapper {
  //   background-color: #304156;
  // }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    // background-color: #304156;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    // background-color: #455d79;
    border-radius: 3px;
  }
}

/* 设备含义中的table*/
.explain-table {
  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  // /* 鼠标悬浮*/
  // .el-table--enable-row-hover .el-table__body tr:hover > td {
  //   background-color: #455d79;
  // }

  /* table边框*/
  .el-table--enable-row-transition .el-table__body td {
    // border-color: #304156;
    // border-width: 8px;
  }

  .el-table::before {
    height: 0;
  }

  .el-table .cell {
    // height: 33px;
  }

  .el-table--mini td {
    padding: 2px 0;
  }

  .el-table .cell.el-tooltip {
    padding-top: 5px;
  }

  .el-table__body-wrapper {
    background-color: #304156;
  }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    // background-color: #304156;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: #455d79;
    border-radius: 3px;
  }
}

.movableItem {
  height: 100%;
}

.strategyClass {
  .el-table .cell {
    height: auto;
    white-space: nowrap;
  }

  // .el-table__body {
  //   .el-table__row {
  //     .el-table__cell:nth-child(2) {
  //       text-align: left;
  //       .el-button--text{
  //         color:#fff;
  //       }
  //     }
  //   }
  // }
}

.clickShowDrag {
  width: 40px;
  height: 40px;
  // border: solid 1px red;

  margin-left: 40px;
  margin-top: 100px;
  z-index: 960619;
}

#analogQuantity {
  width: 100%;
  height: 11rem;
}

.el-checkbox {
  color: #c0ccda;
}

.carContentRight,
.carContentLeft {
  overflow: hidden;
  width: 100%;
}

.runingCarRight {
  position: absolute;
  transition: 1.6s;
}

.runingCarLeft {
  position: absolute;
}

// 跑车
.sportCarBox {
  width: 100%;
}

.sportCar {
  height: 80px;
  position: absolute;
  transition: 1.6s;
}

.item-title {
  font-size: 16px;
}

.oldContent,
.newContent {
  .el-input__inner {
    background: #000000;
    border: unset;
    text-align: center;
    height: 80px;
    line-height: 80px;
    font-size: 20px;
  }
}

.fromTitle span {
  font-size: 18px;
  font-weight: bold;
}

.offline,
.online {
  .el-button {
    background-color: unset;
    border: unset;
    padding: 0px;
    color: white;
  }
}

.offline {
  .el-button {
    span {
      i {
        color: red;
      }
    }
  }
}

.online {
  .el-button {
    span {
      i {
        color: #1de31d;
      }
    }
  }
}

.iconList {
  .el-row {
    .el-col-4,
    .el-col-5 {
      .el-button {
        background-color: unset;
        border: unset;
        padding: 0px;
        color: white;
      }
    }

    i {
      color: white;
      font-size: 16px;
    }
  }
}

.youdaoDialog {
  .el-dialog {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
  }
}

.el-table .odd-row {
  background: #304156;
  color: #fff;
}

.el-table .even-row {
  background: slategrey;
  color: #fff;
}

.tooltip {
  position: absolute;
  top: 35px;
  left: -155px;
  padding: 10px 20px;
  white-space: nowrap;
  border-radius: 4px;
  font-size: 16px;
}

.tooltip::before {
  content: "";
  position: relative;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 10px solid #999999;
  top: -39px;
  left: 45%;
}

.robotAnimation {
  width: 20px;
  height: 20px;
  transform: translateY(-12px);
  animation: mymove 60s infinite linear;
  float: left;
  z-index: 10;
  cursor: pointer;
}

@keyframes mymove {
  0% {
    transform: translateX(0px);
  }

  50% {
    transform: translateX(1290px);
  }

  100% {
    transform: translateX(0px);
  }
}

.el-image__error {
  display: none;
}
.carBox {
}
.carBox span {
  display: block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  position: absolute;
  transition: 1.3s;
}
</style>
