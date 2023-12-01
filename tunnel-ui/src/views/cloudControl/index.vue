<template>
  <div class="app-container">
    <div class="my-back" :style="{height: 'calc(100vh - ('+ navigationHeight + 'px))'}">
      <div class="header workbench-header">
        <div
          class="flex-row"
          style="position: absolute; right: 0px; top: 16px; z-index: 8"
        >

          <el-button
            class="flex-row"
            type="primary"
            size="small"
            icon="el-icon-s-operation"
            @click="strategyPage"
          >
            控制策略
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="small"
            icon="el-icon-files"
            @click="batchManage"
          >
            批量操作
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="small"
            icon="el-icon-top"
            @click="iconExplain"
          >
            图标含义
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="small"
            icon="el-icon-tickets"
            @click="operationLogPage"
            style="margin-right: 20px;"
          >
            操作日志
          </el-button>
        </div>
        <el-row class="menu-b" style="display: flex;">
          <b class="menu-title">站点选择：</b>
          <el-select v-model="getFormSite" placeholder="请选择" @change='changeSite' size="small" >
              <el-option
                v-for="item in siteOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                >
              </el-option>
            </el-select>
          <el-button-group class="menu-button-group">
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
                size="small"
                :style="tunnelBtnStyle[index]"
                @click="setTunnel(item, index)"
              >
                {{ item.tunnelName }}
              </el-button>
            </el-tooltip>
          </el-button-group>
        </el-row>
      </div>
    <!-- <audio id="audio" src="../../../assets/audio/14923.mp3" controls="controls" hidden ref="audio" preload="auto" autoplay/> -->
      <!--菜单-->

      <div style="width: 100%;height: 62%;display: flex;">
          <div
            class="content"
            ref="divRoller"
            @wheel.prevent="handleTableWheel($event)"
            @contextmenu.prevent
            style="position: relative; left: 5px; width: 85%;"
          >
            <div class="tunnelBox" :style="{ width: currentTunnel.lane.width + 80 + 'px' }">
                <div
                  class="workbench-content"
                  @mousedown="dragImg"
                  ref="dragImgDom"
                  style="height: 550px; position: absolute;"
                  @contextmenu.prevent
                >

                  <!--画布区域-->
                  <div >
                    <el-row
                      class="config-img-area"
                      :style="{ width: currentTunnel.lane.width + 80 + 'px' }"
                    >
                      <el-image
                        class="back-img"
                        :src="currentTunnel.lane.url"
                        :style="{ width: currentTunnel.lane.width + 'px' }"
                      ></el-image>



                      <!-- 火灾报警图标 -->
                  <!--    <template>
                        <div class="temperature" v-for="(item,index) in temperatureList" :key='index'
                                :style="{
                                  left: item.position.left + 'px',
                                  top: item.position.top  + 'px',
                                  color:item.temperature>= '50'?'#ff0000' : '#fff'
                                }">
                                <img :src="item.image" />
                          <span v-if="item.temperature >= 50">
                            {{ item.temperature }}°C
                          </span>
                        </div>
                      </template> -->
                      <div
                        class="wrapper"
                        id="eq-wrapper"
                        @mousedown="dian"
                        @mousemove="yi"
                        @mouseup="li"
                      >
                        <div
                          id="container"
                          v-if="move == true"
                          :style="{
                            backgroundColor: back,
                            height: h + 'px',
                            width: w + 'px',
                            position: 'absolute',
                            left: left + 'px',
                            top: top + 'px',
                            opacity: 0.5,
                            border: len + 'px dashed #176CBF',
                            'z-index': '1000',
                          }"
                        ></div>
                        <!-- 设备图标-->
                        <div
                          class="icon-box "
                          v-for="(item, index) in selectedIconList"
                          :key="index"
                          :style="{
                            left: item.position.left - 20 + 'px',
                            top: item.position.top - 100 + 'px',
                          }"
                          :class="item.eqType == 7 || item.eqType == 8 || item.eqType == 9?'light-' + item.position.left:''"
                        >
                          <template v-if="item.eqType == 100">
                            <vue-seamless-scroll :data="newsList" :class-option="optionLeft" class="seamless-warp2" @click.native="IntelligenceBoardEdit(item.eqId)"
                              :style="{width:item.iconWidth+'px',height:item.iconHeight+'px',backgroundImage:'url('+item.url+')'}">
                                <ul class="item" >
                                    <li v-for="(item,index) in newsList" :key="index" v-text="item.title"></li>
                                </ul>
                            </vue-seamless-scroll>
                          </template>

                          <el-tooltip
                            effect="dark"
                            :content="sensorContent(item)"
                            placement="right"
                            :disabled="sensorDisabled(item)"
                            :title="item.eqType"
                          >
                            <div
                              v-show="
                                (item.eqType != 7 &&
                                  item.eqType != 8 &&
                                  item.eqType != 9 &&
                                  item.eqType != 21 &&
                                  item.eqType != 100 &&
                                  item.display == true) ||
                                ((item.eqType == 7 ||
                                  item.eqType == 8 ||
                                  item.eqType == 9 ||
                                  item.eqType == 21) &&
                                  item.display == true &&
                                  lightSwitch == 1)
                              "
                              :class="{ focus: item.isfocus }"
                            >
                              <img

                                v-for="(url, indexs) in item.url"
                                style="position: relative"
                                :width="item.iconWidth"
                                :height="item.iconHeight"
                                :key="indexs"
                                :src="url"
                                @click="openStateSwitch(item)"
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
                              <label style="position: absolute;color: #79e0a9; text-decoration:underline;padding-left: 5px;width: 100px;text-align: left;"   v-if="item.eqType == 14 || item.eqType == 15">
                                {{item.value}}
                              <label v-if="item.eqType == 14">ppm</label>
                                <label v-else="item.eqType == 15">km</label></label>
                                 <!-- 风速风向 -->
                                <label style="position: absolute; text-decoration:underline;color:#79e0a9;padding-left: 5px;width: 100px;text-align: left;"   v-if="item.eqType == 13 || item.eqType == 16 ">
                                {{item.value}}
                               <label v-if="item.eqType == 16">m/s</label></label>
                                 <!-- 洞内洞外 -->
                                <label style="position: absolute;text-decoration:underline;color:#f2a520;padding-left: 5px;width: 100px;text-align: left;"   v-if="item.eqType == 5 || item.eqType == 6 ">
                                {{item.value}}
                                <!-- <label v-if="item.eqType == 5">lux</label>
                                <label v-else="item.eqType == 6">cd/m²</label> --></label>
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
                            type="text"
                            v-model="item.stakeMark"
                            disabled
                          />
                          <div v-else style="width: 80px"></div>
                        </div>
                      </div>
                    </el-row>
                  </div>
                </div>
            </div>

          </div>
          <div style="width: 15%;height: 100%;padding: 0px 130px;padding-right: 0px;">
              <!-- <el-button-group class="menu-button-group"> -->
                <!-- 此处根据需求 改成设备类型大类 ，如需要还原 注释代码如下
                <el-button type="info" size="small" v-for="(item,index) in eqTypeList" :key="index"
                           :style="typeBtnStyle[index]"
                           @click="displayControl(item.typeId,index)">{{ item.typeName }}
                </el-button>-->
                <div
                class="bigTypeButton"
                  type="info"
                  size="small"
                  v-for="(item, index) in eqBigTypeList"
                  :key="index"

                  @click="displayControl(item.bigType, index)"
                  >{{ item.bigType }}
                </div>
              <!-- </el-button-group> -->
          </div>
      </div>
      <!--配置区域-->
      <div  class="footer">
          <div class="footMiniBox">
              <div class="footTitle">系统预警</div>
              <vue-seamless-scroll
                :class-option="defaultOption"
                class="listContent"
                :data="SysEarlyWarning"
              >
                  <el-row v-for="(item, index) in SysEarlyWarning" :key="index"
                  :style="{color:((index+1)%2 == 0) ? '#00fffc' : '#fff'}" class="listRow">
                      <el-col style="width: 3vw;text-align: center;">{{index+1}}</el-col>
                      <el-col style="width: 17vw;">{{item.EarlyWarnContent}}</el-col>

                  </el-row>
              </vue-seamless-scroll>
          </div>
          <div class="footContent">

          </div>
          <div class="footerRight footMiniBox">
              <div class="footTitle">交通事件</div>
              <vue-seamless-scroll
                :class-option="defaultOption"
                class="listContent"
                :data="trafficList"
              >
                  <el-row v-for="(item, index) in trafficList" :key="index"
                  :style="{color:((index+1)%2 == 0) ? '#00fffc' : '#fff'}" class="listRow">
                      <el-col style="width: 3vw;text-align: center;">{{index+1}}</el-col>
                      <el-col style="width: 17vw;">{{item.trafficContent}}</el-col>

                  </el-row>
              </vue-seamless-scroll>
          </div>
      </div>
    </div>
    <!-- <div>111</div> -->
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
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      :visible.sync="stateSwitchVisible"
      width="560px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
      >
        <el-form-item label="设备桩号:">
          {{ stateForm.stakeMark }}
        </el-form-item>
        <el-form-item label="设备类型:">
          {{ stateForm.eqTypeName }}
        </el-form-item>
        <el-form-item label="方向:">
          <div v-if="stateForm.eqDirection == 0">
            <img src="../../assets/image/workbench/long-arrowhead.png" />
            {{ rightDirection + "方向" }}
          </div>
          <div v-else-if="stateForm.eqDirection == 1">
            {{ leftDirection + "方向" }}
            <img
              src="../../assets/image/workbench/long-arrowhead.png"
              style="transform: rotate(180deg); margin-left: 10px"
            />
          </div>
        </el-form-item>
        <el-form-item label="调光强度:" v-if="stateForm.eqType == 21">
          <div style="width: 200px; float: left">
            <slider :min="0" :max="100" v-model="stateForm.lightValue"></slider>
          </div>
          <label style="color: yellow; margin-left: 10px; pointer-events: none">
            {{ stateForm.lightValue }}</label
          >
        </el-form-item>
        <el-form-item label="状态:" v-if="stateForm.value">
          {{ stateForm.value }}
        </el-form-item>
        <el-form-item
          label="配置状态:"
          v-if="stateForm.eqType != 21 && !stateForm.value"
        >
          <div class="wrap">
            <el-radio-group
              v-for="(item, index) in eqTypeStateList"
              :key="index"
              v-model="stateForm.state"
              style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)"
            >
              <el-radio
                v-if="stateForm.eqType == item.type && item.control == 1"
                class="el-radio flex-row"
                :label="item.state"
                style="align-items: center"
                :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]"
              >
                <el-row
                  v-if="item.url.length > 1 && stateForm.eqDirection == 0"
                >
                  <el-row class="flex-row align-items-center">
                    <img
                      v-for="(url, indexs) in item.url"
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
                  v-else-if="item.url.length > 1 && stateForm.eqDirection == 1"
                >
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row
                    class="flex-row align-items-center form-direction"
                    style="margin-top: 5px"
                  >
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img
                    :width="item.width"
                    :height="item.height"
                    :src="item.url[0]"
                  />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
        <!--  <el-form-item label="模拟量:" v-if="stateForm.value">
          <div id="analogQuantity"></div>
        </el-form-item> -->
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          v-if="stateForm.eqType != 21 && !stateForm.value"
          @click="submitState"
          >确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--批量管理对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="batchVisible"
      width="560px"
      append-to-body
    >
      <el-form
        ref="batchForm"
        :rules="rules"
        :model="batchForm"
        label-width="80px"
        label-position="left"
        size="mini"
        hide-required-asterisk
      >
        <el-form-item label="设备类型:">
          <el-select v-model="batchForm.eqType" size="mini">
            <el-option
              v-for="item in eqTypeList"
              v-if="item.typeId != '00' && boxTypeList.length < 1"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            />
            <el-option
              v-if="boxTypeList.length > 0"
              v-for="item in boxTypeList"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            />
          </el-select>
        </el-form-item>
        <!-- 如果选择项为"普通车道指示器，id = 1,则二级选项显示搜索条件，为车向和车道" -->

        <el-form-item label="方向">
          <el-select v-model="batchForm.eqDirection" size="mini">
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
          :row-style="rowStyle"
          :header-cell-style="rowStyle"
          empty-text="暂无设备"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column
            prop="eqName"
            label="设备名称"
            width="220"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="stakeMark"
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
          >确 定</el-button
        >
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
        :row-style="explainRowStyle"
        :header-cell-style="explainHeaderStyle"
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

    <!--摄像机对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="cameraVisible"
      width="45vw"
      style="left: 30%"
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
          <el-col :span="8">
            <el-form-item label="摄像机:">
              {{ stateForm.stakeMark }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实时视频:">
          <videoPlayer
            :id="stateForm.eqId"
            :rtsp="stateForm.queryPointAddress"
            :hostIP="hostIP"
            :open="cameraVisible"
          ></videoPlayer>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
      >
        <el-row>
          <el-col :span="8">
            <el-form-item label="所处位置:">
              {{ stateForm.stakeMark }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../assets/image/workbench/long-arrowhead.png"
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
    <!-- 结束 -->
    <!--图标含义对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table"
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
        :row-style="explainRowStyle"
        :header-cell-style="explainHeaderStyle"
        empty-text="暂无设备"
      >
        <el-table-column label="图标" width="100">
          <template slot-scope="scope">
            　 <img :src="scope.row.url" width="30" height="30" />
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
      class="workbench-dialog explain-table strategyClass"
      :title="title"
      :visible.sync="strategyVisible"
      width="80%"
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
        :row-style="explainRowStyle"
        :header-cell-style="explainHeaderStyle"
        empty-text="暂无策略"
      >
        <el-table-column
          label="隧道名称"
          align="center"
          prop="tunnels.tunnelName"
          width="200"
        />
        <el-table-column
          label="策略名称"
          align="center"
          prop="strategyName"
          width="200"
        />
        <el-table-column label="策略信息" align="center" prop="strategyInfo">
          <template slot-scope="scope">
            <div>{{ scope.row.strategyInfo || "暂无信息" }}</div>
          </template>
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
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="lightControlDialog"
      width="30vw"
      append-to-body
      style="left:37.5%;"
    >
      <el-form label-width="80px" label-position="left" size="mini" :model="lightControForm">
          <el-form-item label="运行方向:">
              <el-radio-group v-model="lightControForm.lCDirection" >
                 <el-radio label="0" @change="changeLightControlTop">上行</el-radio>
                 <el-radio label="1" @change="changeLightControlBottom">下行</el-radio>
               </el-radio-group>
          </el-form-item>
          <el-form-item label="上行车道:" v-if="lCTop">
              <el-checkbox-group v-model="lightControForm.radioLightControlTop" >
                 <el-checkbox label="1" >1车道</el-checkbox>
                 <el-checkbox label="2" v-if="lightControForm.lane >= 2">2车道</el-checkbox>
                 <el-checkbox label="3" v-if="lightControForm.lane >= 3">3车道</el-checkbox>
               </el-checkbox-group>

          </el-form-item>
         <el-form-item label="下行车道:" v-if="lCBottom">
             <el-checkbox-group v-model="lightControForm.radioLightControlBottom" >
                <el-checkbox label="1" ></el-checkbox>
                <el-checkbox label="2" v-if="lightControForm.lane >= 2"></el-checkbox>
                <el-checkbox label="3" v-if="lightControForm.lane >= 3"></el-checkbox>
              </el-checkbox-group>

         </el-form-item>
         <el-form-item label="控制命令:"
                        v-if="lCTop || lCBottom" >
            <div class="wrap">
                <el-radio-group v-for="(item,index) in eqTypeStateList" :key="index" v-model="lightControForm.state"

                                >
                  <el-radio v-if="item.type == 1 && item.control == 1" class="el-radio flex-row"
                            :label="item.state" style="align-items:center;"
                            >
                    <el-row v-if="item.url.length>1 && lightControForm.lCDirection == 0">
                      <el-row class="flex-row align-items-center">
                        <img v-for="(url,indexs) in item.url" class="equipment-img" :key="indexs" :src="url"/>
                      </el-row>
                      <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px;">
                        <el-row>正</el-row>
                        <el-row>反</el-row>
                      </el-row>
                    </el-row>
                    <el-row v-else-if="item.url.length>1 && lightControForm.lCDirection == 1">
                      <el-row class="flex-row align-items-center form-direction">
                        <img class="equipment-img" :src="item.url[1]"/>
                        <img class="equipment-img" :src="item.url[0]"/>
                      </el-row>
                      <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px;">
                        <el-row>反</el-row>
                        <el-row>正</el-row>
                      </el-row>
                    </el-row>
                  </el-radio>

                </el-radio-group>
            </div>
          </el-form-item>
      </el-form>
      <div slot="footer">
          <el-button type="primary" size="mini" @click="lightSubmitState">确 认</el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <intelligence-board ref="intelligenceBoard"></intelligence-board>
    <div class="temperatureDialog">
        <el-dialog
          title="火灾报警提示"
          :visible.sync="HZdialogVisible"
          width="25%"
          :before-close="HZhandleClose"
          >
          <div slot="title" >
              <span ><i class="el-icon-message-solid" style="color: #e6a23c;margin-right:10px"></i></span>
              <span  style="color: white;">火灾报警提示</span>
          </div>
          <div>
              <div>隧道名称</div>
              <div>{{tunnelName}}</div>
          </div>
          <div>
              <div>隧道温度</div>
              <div>{{temperature}}°C</div>
          </div>
          <div class="HZdialogBg"></div>
        </el-dialog>
    </div>
  </div>
</template>

<script>
import vueSeamlessScroll from 'vue-seamless-scroll'
import $ from "jquery";
import "jquery-ui-dist/jquery-ui";
import "jquery-ui-dist/jquery-ui.min.css";
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
import {
  listTunnels,
  getTunnels,
  updateTunnels,
  getConfigData,
  getStorageData,
  setConfigData,
} from "@/api/equipment/tunnel/api.js";
import {
  listEqTypeState,
  getEqTypeState,
  updateEqTypeState,
} from "@/api/equipment/eqTypeState/api";
import {
  icon,
  // eqTypeStateIcon,
  laneImage,
  laneImage2
} from "../../utils/configData.js";
import {
  listStrategy,
  getStrategy,
  handleStrategy,
} from "@/api/event/strategy";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import intelligenceBoard from "@/views/workbench/config/intelligenceBoard";
import { getLocalIP } from "@/api/event/vedioRecord";
import { getHosts } from "@/api/equipment/plc/api";
import * as echarts from "echarts";
import { listUser } from "@/api/system/user";
import * as deviceApi from '@/api/equipment/device/api'
let configData = {}; //配置信息
let wrapperClientX = 0;
let wrapperClientY = 0;
let boxEqList = [];
let mode = "";
export default {
  name: "Workbench",
  components: {
    // DragItDude,
    videoPlayer,
    vueSeamlessScroll,
    intelligenceBoard,
  },
  data() {
    return {
        SysEarlyWarning:[
            {
                EarlyWarnContent:'2022.01.20 13:45:00 凤凰山隧道（K470+000)发现一氧化碳浓度超标',
            },
            {
                EarlyWarnContent:'2022.01.20 15:23:00 刘长山隧道(K520+000)温度检测长期超过50°C'
            },
            {
                EarlyWarnContent:'2022.01.20 17:00:00 姚家峪隧道(K570+000)温度检测长期超过50°C'
            },

        ],
        trafficList:[
            {
                trafficContent:'10:21隧道上行K530+180发生行人事件'
            },
            {
                trafficContent:'10:16隧道上行K350+128发生停车事件'

            },
        ],
        getFormSite:'1',
         siteOptions: [{
                  value: '1',
                  label: '新泰北隧道所'
                }, {
                  value: '2',
                  label: '柏岩隧道所'
                }, {
                  value: '3',
                  label: '山亭隧道所'
                }],
      seamless:false,//情报板轮播
      /* ---------火灾报警---------------*/
      alarmBell:false,//是否启用报警铃声
      tunnelName:'',
      temperature:'',
      HZdialogVisible:false,
      temperatureList:[
        {
          temperature:30,
          position:{
            top:120,
            left:430,
          },
          image:require('@/assets/image/fire_alarm0.png'),
          tunnelName:'刘长山隧道'

        },
        {
          temperature:30,
          position:{
            top:120,
            left:630,
          },
          image:require('@/assets/image/fire_alarm.png'),
          tunnelName:'凤凰山隧道'
        },
      ],
      /* ---------工作台轮播情报版---------------*/
      newsList: [{
          "title": '下雨路滑，请谨慎驾驶',
        },
        {
          'title': '文明出行，安全第一',
      }],
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
      moveTop: 0,
      /* -------------------------*/
      timer: null, //定时器
      windowHeight: document.documentElement.clientHeight, //实时屏幕高度
      displayNumb: false, //显示编号
      currentTunnel: {
        id: "",
        name: "",
        lane: {},
      }, //选中的隧道
      lightState: "正常",
      dljbObj: {},
      loading: true,
      title: "", //对话框标题
      tunnelVisible: false,
      stateSwitchVisible: false,
      batchVisible: false,
      weiboVisible: false,
      cameraVisible: false,
      daolujiebingVisible: false,
      lightControlDialog: false,
      cameraErrorInfo: "",
      explainVisible: false,
      eqTypeList: [], //设备类型
      eqBigTypeList: [
          {bigType:'交通诱导'},
          {bigType:'交通监控'},
          {bigType:'通风照明'},
          {bigType:'消防安全'},
          {bigType:'应急广播'},

      ], //设备类型
      checkboxTunnel: [], //可点的隧道
      tunnelList: [], //隧道
      selectedIconList: [], //配置图标
      stateForm: {}, //配置表单
      weiboList: [],
      shuibengVisible: false,
      shuibengObj: {},
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: ""
      },
      lightSwitch: 0,
      //车道按钮样式
      tunnelBtnStyle: [
        {
          "background-color": "transparent", //4e5e89
          "border-color": "#68B5FF",
           "box-shadow": "0px 0px 10px #68B5FF",
           "margin-left":"20px"
        },
      ],

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

    timer:null,

      //车道列表
      laneUrlList: this.$cache.local.get("navigationBar") == '0'?laneImage:laneImage2,
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
      devicesList: [],
      eqIcon: icon,
      //各状态对应图标列表（灵活性差，后期最好在类型状态管理中动态添加）
      eqTypeStateList: [],
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
      rowStyle: {
        "background-color": "#304156",
        color: "#C0CCDA",
      },
      explainRowStyle: {
        "background-color": "#304156",
        color: "#fff",
      },
      explainHeaderStyle: {
        "background-color": "#304156",
        color: "#fff",
        "border-color": "#304156",
      },
      leftDirection: "", //左侧方向
      rightDirection: "", //右侧方向
      batchFormDirection: 1, //批量对话框默认的设备方向
      lightControForm:{
          index:0,
          lCDirection:'',
          radioLightControlTop:[],
          radioLightControlBottom:[],
          state:'',
          lane:'',
          name:'',
      },
      lCTop:false,
      lCBottom:false,
      lightUrlArr:[],
      eqTypeStateList1:[],
      eqTypeStateList2:[],
      eqTypeStateListAll:[],
      carShow:false,
      carShowTwo:false,
      light:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAYAAADOvw4PAAAAAXNSR0IArs4c6QAABUBJREFUaEPtWk9oXEUc/r63UA9JerCHQiuIVRDNoYfY6CGBrlrtIftsQ6J4Ezx4EUnxmD3uCl4aPHkTb4Uaa0wDjRR2iyJKVqRgDgGrohgv9rS7gXpIpuz7N/Nm3uzO22xA9O1ld7Pz3pf3m2++3+/3zRDRS6yiggNcAXEOwHj89xzvXQi0QFzlIjZs1xU4RmSOOG4ESADsgtH8zO/b52dtooISrkCUzoEcD6/1wnukPnu9ewKMfgvGBJ+7oNeC511leSfA6d0BYhU1CCznIFT/oUSdC6jqgwqcARE+bNxCMkWT3+/dq/NS15yfjRM1gMshoTQSpb5n/Kb/HhKuztkfqxSfYQ7AzZERLL4R4XNB3rfAcYywU9x6JIg1IlGqWLEy3mO1UdQIns/K/WTexVcn53BQupmhTBrhYlXrEU35HJBKJV+C6VNcRwNE2TEE7sMEmnwdLybpuMBxi50RNzbgsRymJhux1HSmpzVrumvy4q6cn9tnGgDLiYplEiZJiVFqtBAtSavB/9XsKVkbwIRbBHKN6nARxxOSFTiDgxemuw4XDmTcbhxrA5yI6qpslQoIqKY4p3TX4YVfJE7z2QgnJpJRa/UnVlKf6UrndXokE4OffrgRXAzXXlD3FThKEPunO87/I+O2NiZkCrMV3xmkkoW4Nd3x/LbE+WYqxLGTRRLZlhqD67UUCqIgWY71M9yiUWsmt3TH1zpy8jceFTm7O0Vx4u4vW5U405I4381EZM6jYHEqVtXLxCxINkqSOXd3KvFi9ZBqxLn7cvJvnRJmt+fc3UXKoqtL+J0vfC1xWhfSJEtUyVSmRFl15VItjUQVCyXLQTEgpWSrFG52geoluaU7XvxTTv7tMwPSmKoc+dIdpzYlzl0/wrGRSlW4rPSsP6dcPIWSDaSZVB0u7MtJuXEsJJlhVjp3dzYzE3z5nsRpTio12VDdnbVg59kvJM72G9lkVmsso8FQVVjpYpO4hLEoSKaSbEC64+UHclK+HI9IZqa7w5qZPP+TUpA/J0mWFNwO/pRDuuPkNYmz85Z8Hr1TzXL8NSLJBWf6dv9Tkg1nZtJvKwX5CWXlD9fd2cxMzmxJnO9no8I/D7GUFKp3i4oy8elPJM7P72hdrK5SmkWikizoZ9TdhnR6OEqfrMtF6b8doR83ACd/d5e5Xwd2Ofd34ieKzdOKr5TRZQ1vZnb5/B2J88MrbdCbCO0FnWhu3Z3FQ+vyqY8lzm/vtYEejsu2VMTNYOzAV/e/5/ivsgF4oXPtJOmu6a7U5Kt/KA75kw0wwsn0jfJYAaluscmpTYlz93K4I2NgxMqSZR/oqpOxwQ00+cRHEuf398PnyYpZstswkFDmAKLJ4FSEwPoQl/e/RN+DGylOKt35XNiXe3Brj1QguJ4OVsbe3SCHXDcze8/z0j2Jc2eyApTWU9swIzEzSz7Pfi5xtt+sgFxP7ynm6+7kglMWlPB8Pv6hxNldrkCIKG4jZYMf6N2/+3REv3Tn1Tn/wDxNsH68BnjRaQLr3p21uzNUA6yzvG3ifDtdA0ry1EKsNum9uz7bMUa6q3Pymomz83YNHpeThaOSOen47N1dhjrV+dgHJs5f1RqEGOFpHNZ5qlaVhd91+ACWQEwDGBuCy3sQ2IKHFfX0hX4f0Q/HzczcA7gFeCu81LGeHhG3TvoAlwBvGuSYsWWiG4fmUZU9sIdTWuFsy47TKvtAaQngNOiFOIlKZlkOip8U1lkRjrfCZz614/z6rg8hlsBS+DxOpUCqGN8DsQWBFZ6u23F2qz4oliAOwQMN5yErndtqYIc5/QAAAABJRU5ErkJggg==",
      dark:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAMAAAD5Yf49AAAAAXNSR0IArs4c6QAAAUdQTFRFAAAArq6utbW1ubm5v7+/w8PDycnJzc3N09PTsLCwtra2vb29wMDAysrKzc3N1NTUsbGxt7e3wcHBzs7OsLCwtra2uLi4v7+/wcHBy8vLzc3N1dXVrq6usLCwtLS0tbW1t7e3uLi4v7+/wcHBw8PDycnJy8vLzMzMzc3N1NTU1dXVr6+vsLCwtbW1t7e3v7+/wcHBysrKzMzM1dXVr6+vsLCwtLS0uLi4vb29v7+/wsLCyMjIycnJzs7O1NTU1dXVr6+vsLCwtLS0uLi4vb29wsLCycnJzs7O1NTUr6+vsLCwsbGxsrKys7OztLS0tbW1t7e3uLi4ubm5urq6u7u7vLy8vb29vr6+v7+/wcHBwsLCw8PDxMTExcXFxsbGx8fHyMjIycnJysrKzMzMzc3Nzs7Oz8/P0NDQ0dHR0tLS09PT1NTU1dXV1E4hgwAAAEl0Uk5TAExMTExMTExMTU1NTU1NTU5OTk6EhISEhISEhL6+vr6+vr6+vr6+vr6+vvLy8vLy8vLy8vPz8/Pz8/Pz8/Pz8/T09PT09PT09GJ8jKsAAAG6SURBVEjH7dZHU8JAFMDxxQ5SLFgJKAhKsRuxK3aJGkrovffvf/YF4gwLL3jB0QPvyH928hsO+5YQomRYb/ewjIJ0ZkAxnr7DfMB8wvBu05hUVOazQDAYFIRQKBQORyLnlgmpqK2XyWQqlUqnM5lsNpe7sk1JRWu/KVcqlWq1VqvX643GrWNG/Fnv7R89+aFwHEfJeN631C7L/kCAkkWi0ZV2WY0nEpQsl8+vtct6qVSmZM1my0CIwouNkgwsHCLzqaCM+xFZTA1lMo7IChoo00VE1tIRBv0+AyfkixGVbUDZRGUWKFuobBvKDipzEhf6fReckC+7qGwPyj4qO4ByiMqOoByjshPixQdOyBcOlfmg+FFZDEr8W5bulhWgFFFZ65dkAiJL/IGMH8lGMlzW+H+yao8Mv7VY2fuMRe6zjszddZ/Rsov2fYbJrrvuM1p2N5QdwPftAAHZAb2yfM8OoGVOopTdjgMKKhP3pgqViXtTjcrEvalFZbqhvDX4vreGgLw1EBn11qBkBjHMMR7q4x5GKb2bBhTjPSV7MKmkMm9+pGRPllmpLFifKdmLTSOVRfsrJXtzwD/2Betpxj8VdhGZAAAAAElFTkSuQmCC",
      allDirection:[],// 所有方向
      allLane:[],// 所有车道
      dialogVisible:false, // 情报板批量编辑弹窗
    }
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
    // this.$store.dispatch('app/toggleSideBar')
    getLocalIP().then((response) => {
      this.hostIP = response;
    });
    this.getUserDept();
    // this.getTunnelList();
    // this.selectEquipmentType()
    this.getEqTypeStateIcon();
    // this.initWebSocket()
    this.lightSwitchFunc();
  },
  watch: {
    // 设备类型
    "batchForm.eqType"(val) {
      if (mode == "buttonSelection") {
        let param = {
          eqTunnelId: this.currentTunnel.id,
          eqType: val,
          lane:this.batchForm.eqlane,
          eqDirection:this.batchForm.eqDirection
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
    "batchForm.eqDirection"(val){
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqDirection:val,
        lane:this.batchForm.eqlane,
      };
      this.selectEquipment(param);
    },
    // 车道
    "batchForm.eqlane"(val){
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqDirection:this.batchForm.eqDirection,
        lane:val,
      }
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
      handler (val) {
        console.log(val,"监听")
        for(var item of val){
            if(item.temperature >= 50){
                this.temperature = item.temperature
                this.tunnelName = item.tunnelName
                this.HZdialogVisible = true
                // 当设备异常时
                this.$refs.audio.currentTime = 0; //从头开始播放提示音
                this.$refs.audio.play(); //播放
                console.log(this.$refs.audio,'this.$refs.audio')
            }else{
                this.HZdialogVisible = false
            }
        }
      },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true
    },
    // 监听声音开关按钮
    alarmBell:{
        handler (val) {
          console.log(val,"监听开关")
          if(val = true){
              this.$refs.audio.currentTime = 0; //从头开始播放提示音
              this.$refs.audio.play(); //播放
              // 谷歌浏览器禁止一打开就播放声音 所以需要通过开关打开声音
              // 打开后会声音直接播放 所以设置10毫秒 用户就不会听到了
              setTimeout(() => {
                this.$refs.audio.pause()
                this.$refs.audio.load()
              }, 10)
          }else{
              this.$refs.audio.pause()
              this.$refs.audio.load()
          }
        },
        // 这里是关键，代表递归监听 demo 的变化
        deep: true
    }
  },
  computed: {

      defaultOption() {
      	return {
      		step: 0.2, // 数值越大速度滚动越快
      		limitMoveNum: 3, // 开始无缝滚动的数据量 this.dataList.length
      		hoverStop: true, // 是否开启鼠标悬停stop
      		direction: 1, // 0向下 1向上 2向左 3向右
      		openWatch: true, // 开启数据实时监控刷新dom
      		singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
      		singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
      		waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      	}
      },
    // 监听属性 类似于data概念
    optionLeft  () {
        return {

          direction: 2, // 0向下 1向上 2向左 3向右
          limitMoveNum: 2,
          waitTime: 2500
        }
    },
    navigationHeight() {
      const topNav = this.$store.state.settings.topNav
      const needTagsView = this.$store.state.settings.tagsView
      let h = 0
      h += 72
      if(!topNav) {
        if(needTagsView) h += 34
      }

      return h
    }
  },
  mounted() {
    let that = this;
    window.onresize = () => {
      return (() => {
        this.windowHeight = document.documentElement.clientHeight;
      })();
    };

	// 隧道调取数据两秒一次
    this.timer = setInterval(() => {
		setTimeout(this.getRealTimeData, 0);
      /* setTimeout(this.getLiPowerDevice, 0) */
    }, 1000 * 2);

    // 模拟实时隧道温度 调完了再删
    // setInterval(()=>{
    //         this.temperatureList[1].temperature += 2

    // },2000)
    // setInterval(()=>{
    //         this.temperatureList[1].temperature -= 20

    // },22000)
    // console.log(this.temperatureList[1].temperature,"隧道温度")
    // 一进页面判断温度超过50度 弹窗提示
    for(var item of this.temperatureList){
        if(item.temperature >=50 ){
            this.HZdialogVisible = true
            this.temperature = item.temperature
            this.tunnelName = item.tunnelName
        }
    }
  },
  methods: {
      changeSite(index){
          console.log(index,"index")
      },
    IntelligenceBoardEdit(id){
      console.log(id);
    },
      // 火灾报警弹窗
    HZhandleClose() {
      this.HZdialogVisible = false
    },
    showA(){
      this.carShow = !this.carShow;
      this.carMapSportCar();
      this.carMapRight();
    },
    showB(){
      this.carShowTwo = !this.carShowTwo;
      this.carMapLeft();
    },
    // 跑车
    carMapSportCar(){
      clearInterval(this.timer)
      const car = this.$refs['sportCar']
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
           clearInterval(this.timer);
           for(let i = 0 ; i < this.selectedIconList.length;i++){
             let item = this.selectedIconList[i];
             if((item.eqType == 7 && item.eqDirection == 1) || (item.eqType == 8 && item.eqDirection == 1) || (item.eqType == 9 && item.eqDirection == 1)){
               item.url[0] = this.dark;
             }
           }
           this.carShow = false;
        } else {
          for(let i = 0 ; i < this.selectedIconList.length;i++){
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i-1];
             // 取到所有的隧道灯
            if((item.eqType == 7 && item.eqDirection == 1) || (item.eqType == 8 && item.eqDirection == 1) || (item.eqType == 9 && item.eqDirection == 1)){
              if(car.style.left == '100px'){
                this.carShow = false;
              }
              if(parseInt(car.style.left) - item.position['left'] <= 150  ){
                  item.url[0] = this.light;
              }
              if(item_1.position.top == 156){
                if(parseInt(car.style.left) - item.position['left'] <= -150  ){
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + 'px'
        }
      },speed)
      this.$forceUpdate();
    },
    // 下行车道
    carMapLeft(){
      clearInterval(this.timer)
      const car = this.$refs['carLeft']
      let speed = 210
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) >= 1130) {
          // 车辆停止运动
           clearInterval(this.timer);
           for(let i = 0 ; i < this.selectedIconList.length;i++){
             let item = this.selectedIconList[i];
             if(item.eqType == 7 && item.eqDirection == 0 || item.eqType == 8 && item.eqDirection == 0 || item.eqType == 9 && item.eqDirection == 0){
               item.url[0] = this.dark;
             }
           }
        } else {
          for(let i = 0 ; i < this.selectedIconList.length;i++){
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i-1];
             // 取到所有的隧道灯
            if(item.eqType == 7 && item.eqDirection == 0 || item.eqType == 8 && item.eqDirection == 0 || item.eqType == 9 && item.eqDirection == 0){
              // 到达终点隐藏
              if(car.style.left == '1130px'){
                this.carShow = false;
                return false
              }
              // 如果车辆当前位置和路灯位置相差小于100，则亮灯
              if(parseInt(car.style.left) - item.position['left'] <= -100  ){
                item.url[0] = this.light;
              }
              if(item_1.position.top != 0){
                // 如果车辆位置 - 当前路灯位置 大于等于 100 则代表车辆已经离开当前路灯区域，则灭灯
                if(parseInt(car.style.left) - item.position['left'] >= 100  ){
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) + 24 + 'px'
        }
      },speed)
      this.$forceUpdate();
    },
    // 上行车道
    carMapRight(){
      clearInterval(this.timer)
      const car = this.$refs['carRight']
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
           clearInterval(this.timer);
           for(let i = 0 ; i < this.selectedIconList.length;i++){
             let item = this.selectedIconList[i];
             if((item.eqType == 7 && item.eqDirection == 1) || (item.eqType == 8 && item.eqDirection == 1) || (item.eqType == 9 && item.eqDirection == 1)){
               item.url[0] = this.dark;
             }
           }
           this.carShow = false;
        } else {
          for(let i = 0 ; i < this.selectedIconList.length;i++){
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i-1];
             // 取到所有的隧道灯
            if((item.eqType == 7 && item.eqDirection == 1) || (item.eqType == 8 && item.eqDirection == 1) || (item.eqType == 9 && item.eqDirection == 1)){
              if(car.style.left == '100px'){
                this.carShow = false;
              }
              if(parseInt(car.style.left) - item.position['left'] <= 150  ){
                  item.url[0] = this.light;
              }
              if(item_1.position.top == 156){
                if(parseInt(car.style.left) - item.position['left'] <= -150  ){
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + 'px'
        }
      },speed)
      this.$forceUpdate();
    },
    getUserDept(){
      listUser(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        this.tunnelQueryParams.deptId = response.rows[0].deptId;
        this.getTunnelList();
      })
    },
    //右键拖动
    dragImg(e) {
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
    initWebSocket() {
      /* initLipowerDevice().then(response => {

      }); */
      // 山亭
      //const wsuri = "ws://10.7.98.20:8239";
      // 白彦
      const wsuri = "ws://10.7.97.20:8239";
      this.websock = new WebSocket(wsuri);
      this.websock.onmessage = this.websocketonmessage;
      this.websock.onopen = this.websocketonopen;
      this.websock.onerror = this.websocketonerror;
      this.websock.onclose = this.websocketclose;
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },
    websocketonopen() {
      let actions = {
        test: "12345",
      };
      this.websocketsend(JSON.stringify(actions));
    },
    websocketonerror() {
      //连接建立失败重连
      this.initWebSocket();
    },
    websocketonmessage(e) {
      //数据接收
      const redata = JSON.parse(e.data);
    },
    websocketsend(Data) {
      //数据发送
      this.websock.send(Data);
    },
    websocketclose(e) {
      //关闭
      console.log("断开连接", e);
    },
    /* -------------------鼠标拖动-------------------*/
    /* 鼠标点击*/
    dian(event) {
      if (event.button != 0) {
        return;
      }
      let parentObj = document.getElementById("eq-wrapper");
      wrapperClientX = parentObj.getBoundingClientRect().left;
      wrapperClientY = parentObj.getBoundingClientRect().top;
      this.px = event.clientX + event.clientX * this.moveTop - wrapperClientX;
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
      let str = [108, 110,18, 21, 13, 14, 15, 16, 5, 6, 7, 8, 9];
      let list = this.selectedIconList;
      for (let i = 0; i < list.length; i++) {
        // 传入每个设备并判断是否在范围之内
        let inRange = this.inRange(list[i]);
        if (inRange == true) {
          this.selectedIconList[i].isfocus = true;
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
        }else{
          console.log('bu在范围内')
        }
      }
      if (boxEqList.length > 0 && this.boxTypeList.length > 0) {
        if (boxEqList.length == 1 && boxEqList[0].eqlist.length == 1) {
          //单个配置
          this.openStateSwitch(boxEqList[0].eqlist[0]);
        } else {
          //超过1个设备进行批量配置
          this.batchForm.eqType = this.boxTypeList[0].typeId;
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
            this.batchVisible = true;
            mode = "boxSelection";
            this.title = "批量操作";
            if (this.$refs["batchForm"]) {
              this.$refs["batchForm"].resetFields();
            }
            this.devicesList = this.changeDirection(boxEqList[0].eqlist);
          }
        }
      }
      this.empty();
    },
    boxType(type) {
      for (let i = 0; i < this.eqTypeList.length; i++) {
        if (type == this.eqTypeList[i].typeId ||type == 100 ) {
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
        // console.log(maxX,maxY,minX,minY);
        if (maxX - minX <= this.w + 60 && maxY - minY <= this.h + 30) {
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
    /* -------------------鼠标拖动end------------------*/

    /* 查询隧道列表 */
    getTunnelList() {
      listTunnels(this.tunnelQueryParams).then((response) => {
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
          console.log(list.slice(0,2),"list")
          this.tunnelList = list.slice(0,4);
          this.checkboxTunnel.push(this.tunnelList[0]);
          this.currentTunnel.id = this.tunnelList[0].tunnelId;
          this.currentTunnel.name = this.tunnelList[0].tunnelName;
          this.selectEquipmentType(this.currentTunnel.id);
          this.getTunnelData(this.currentTunnel.id);
        }
      });
    },
    /* 查询设备类型*/
    selectEquipmentType(currentTunnelId) {
      this.eqTypeList = [];
      listHasType(currentTunnelId).then((response) => {
        let typeList = response.rows;
        this.eqTypeList = typeList;
        this.eqTypeList.unshift({
          typeId: "00",
          typeName: "全部",
        });

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
    selectDirection(param){
      listDevices(param).then((response) => {
        this.allDirection = this.changeDirection(response.rows);
        for(let i=0; i<this.allDirection.length; i++){
          for(let j=i+1; j<this.allDirection.length; j++){
              if(this.allDirection[i].eqDirection == this.allDirection[j].eqDirection){
                  this.allDirection.splice(j,1); //splice会改变原数组
                  j--;
              }
          }
        }
      })
    },
    // 查询车道
    selectLane(param){
      listDevices(param).then((response) => {
        this.allLane = this.changeDirection(response.rows);
        for(let i=0; i<this.allLane.length; i++){
          for(let j=i+1; j<this.allLane.length; j++){
            if(this.allLane[i].lane == this.allLane[j].lane){
                this.allLane.splice(j,1);
                j--;
            }
          }
        }
        this.allLane.forEach(item=>{
          switch(Number(item.lane)){
            case 0:
            item.laneName = '第零车道'
            break;
            case 1:
            item.laneName = '第一车道'
            break;
            case 2:
            item.laneName = '第二车道'
            break;
            case 3:
            item.laneName = '第三车道'
            break;
          }
        })
      })
    },
    /* 查询设备*/
    selectEquipment(param) {
      let that = this;
      listDevices(param).then((response) => {
        that.devicesList = that.changeDirection(response.rows);
      })
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
    getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        isControl: null,
      };
      listEqTypeState(queryParams).then((response) => {
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    async getEqUrl(list) {
      let that = this;
      that.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            let img = await that.picture(list[i].iFileList[j].url);
            iconUrl.push(img);
          }
        }
        that.eqTypeStateList.push({
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
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
      getTunnels(tunnelId).then((response) => {
        that.loading = false;
        let res = response.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);

          listType("").then((response) => {
            // response.rows
            for (let i = 0; i < res.eqList.length; i++) {
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
            that.selectedIconList = res.eqList //设备
          });

          if (res.upList != undefined) {
            that.upList = res.upList;
          }
          if (res.downList != undefined) {
            that.downList = res.downList;
          }
          if (res.leftDirection != undefined) {
            that.leftDirection = res.leftDirection;
          }
          if (res.rightDirection != undefined) {
            that.rightDirection = res.rightDirection;
          }

          let id = res.lane;
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.currentTunnel.lane = that.laneUrlList[i];
            }
          }
        } else {
          //不存在
          that.selectedIconList = [];
          that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
    },

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[8];
      for (let i = 0; i < this.laneUrlList.length; i++) {
        if (this.laneUrlList[i].num == num) {
          lane = this.laneUrlList[i + 2];
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
	  getStorageData(this.currentTunnel.id)
        .then((response) => {
          for (let i = 0; i < response.length; i++) {
            let type = response[i].devType;
            if (type != "" && type != undefined) {
              for (let j = 0; j < this.selectedIconList.length; j++) {
                if (response[i].devId == this.selectedIconList[j].eqId) {
                  // 需要换光标的
                  for (let k = 0; k < this.eqTypeStateList.length; k++) {
                    if (
                      this.selectedIconList[j].eqType ==
                        this.eqTypeStateList[k].type &&
                      response[i].state == this.eqTypeStateList[k].state
                    ) {
                      let url = this.eqTypeStateList[k].url;
                      this.selectedIconList[j].eqDirection =
                        response[i].direction;
                      if (response[i].direction == "1") {
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
                      this.selectedIconList[j].state = response[i].state;
                    }
                    // 微波车检
                    else if (
                      this.selectedIconList[j].eqType ==
                        this.eqTypeStateList[k].type &&
                      this.selectedIconList[j].eqType == "108" &&
                      type == "108"
                    ) {
                      this.selectedIconList[j].wbList = response[i].state;
                    }
                    // 道路结冰
                    else if (
                      this.selectedIconList[j].eqType == "110" &&
                      type == "110"
                    ) {
                      this.selectedIconList[j].dljb = response[i].state;
                    }
                    /* // 水泵
                  else if (this.selectedIconList[j].eqType == '18' && type == '18') {
                    this.selectedIconList[j].shuibeng = response[i].state
                    if (response[i].state) {
                      let devState = JSON.parse(response[i].state).devState
                      if (this.selectedIconList[j].eqType == this.eqTypeStateList[k].type &&
                        devState == this.eqTypeStateList[k].state) {
                        this.selectedIconList[j].url = this.eqTypeStateList[k].url
                      }
                    }
                  } */
                  }
                  // 不需要换光标的
                  let paramType = [5, 6, 13, 14, 15, 16, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
                  if (paramType.includes(parseInt(type))) {
                    if (response[i].state == "null" || !response[i].state) {
                      this.selectedIconList[j].value = "0";
                    } else {
                      this.selectedIconList[j].value = response[i].state;
                    }
                  }
                }
              }
            }
          }
        })
        .catch((err) => {
          // this.systemState = "较差";
        });
    },
    /* 选择隧道*/
    switchTunnel() {
      this.title = "切换隧道";
      this.tunnelVisible = true;
    },
    /* 选择隧道*/
    setTunnel(item, index) {
      this.lightControForm.index = index
      this.lightControForm.name = item.tunnelName
      this.tunnelBtnStyle = []
      this.tunnelBtnStyle[index] = {
        "background-color": "transparent", //4e5e89
        "border-color": "#68B5FF",
         "box-shadow": "0px 0px 10px #68B5FF",
      };
      (this.currentTunnel.id = item.tunnelId),
        (this.currentTunnel.name = item.tunnelName),
        this.selectEquipmentType();
      this.getTunnelData(this.currentTunnel.id);
    },
    onActivated(key) {},
    onDragging(key) {},
    onDropped(key) {},
    /*点击设备类型*/
    displayControl(bigType, index) {
      this.typeBtnStyle = [];
      for (let i = 0; i < this.eqTypeList.length; i++) {
        if (i == index) {
          this.typeBtnStyle[index] = {
            "background-color": "#3A4E86", //4e5e89
            "border-color": "#7f8c98",
          };
        }
      }
      // 改为设备大类，代码注释备份
      // for (let i = 0; i < this.selectedIconList.length; i++) {
      //   if (typeId == this.selectedIconList[i].eqType) {
      //     this.selectedIconList[i].display = true
      //   } else if (typeId == "00") {
      //     this.selectedIconList[i].display = true
      //   } else {
      //     this.selectedIconList[i].display = false
      //   }
      // }
      hasListByBigType(bigType).then((response) => {
        let typelist = response.rows;
        let typeIndex = [];
        if (typelist.length > 0) {
          for (let y = 0; y < typelist.length; y++) {
            for (let i = 0; i < this.selectedIconList.length; i++) {
              if (typelist[y].typeId == this.selectedIconList[i].eqType) {
                typeIndex.push(i);
              }
              this.selectedIconList[i].display = false;
            }
          }
          for (let i = 0; i < typeIndex.length; i++) {
            this.selectedIconList[typeIndex[i]].display = true;
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            this.selectedIconList[i].display = true;
          }
        }
      });
    },
    /* 传感器设备提示框 */
    sensorContent(item) {
      let sensorDevice = [5, 6, 13, 14, 15, 16, 20];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return "";
      } else {
        let deviceName = "设备名称：" + item.eqName; // 设备名称
        let deviceStake = " 桩号：" + item.stakeMark; // 桩号
        let state = " 模拟量：" + (item.value || "无"); // 模拟量
        return (deviceName + deviceStake + state).toString();
      }
    },
    sensorDisabled(item) {
      let sensorDevice = [5, 6, 13, 14, 15, 16, 20];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return true;
      } else {
        return false;
      }
    },
    //================================================单个配置开始==================================
    /* 打开配置界面*/
    openStateSwitch(item) {
      // 传感器（模拟量）
      let sensorDevice = [5, 6, 13, 14, 15, 16, 20];
      if (sensorDevice.indexOf(item.eqType) != -1) {
        this.stateSwitchVisible = true;
        this.stateForm = {
          value: item.value,
        };
        /* this.$nextTick(()=>{
           if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
        }) */
      }

      //跳转微波车检
      if (item.eqType == "108") {
        this.weiboList = eval("(" + item.wbList + ")");
        this.title = item.eqName;
        this.weiboVisible = true;
        return;
      }
      //跳转 摄像机
      else if (item.eqType == "109") {
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: "", //默认状态
          stakeMark: item.stakeMark,
          queryPointAddress: item.queryPointAddress,
        };
        this.title = item.eqName;
        this.cameraVisible = true;
        this.loading = true;
        setTimeout(this.changeLoading, 2000);
        return;
      }
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
      else if (item.eqType == "110") {
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: "", //默认状态
          stakeMark: item.stakeMark,
          eqFeedbackAddress1: item.eqFeedbackAddress1,
        };
        if (item.dljb) {
          this.dljbObj = JSON.parse(item.dljb);
        }
        this.title = item.eqName;
        this.daolujiebingVisible = true;
        return;
      }
      //跳转情报板
      else if (item.eqType == "100") {
        this.$message({
          message: "未能获取设备信息,请重新配置！",
          type: "warning",
          customClass: "workbench-msg",
        });
        this.dialogVisible = true;
        this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,item);
      } else {
        //配置
        if (item.environmentType == undefined) {
          this.batchVisible = false;
          if (
            item.typeName == undefined ||
            item.eqDirection == undefined ||
            item.eqDirection == null
          ) {
            this.$message({
              message: "未能获取设备信息,请重新配置！",
              type: "warning",
              customClass: "workbench-msg",
            });
            this.cleanUp();
            this.empty();
          } else {
            this.stateForm = {
              eqId: item.eqId,
              eqHostId: item.eqHostId,
              eqName: item.eqName,
              eqType: item.eqType,
              eqTypeName: item.typeName.typeName,
              eqDirection: item.eqDirection,
              state: item.state, //默认状态
              stakeMark: item.stakeMark,
              value: item.value,
              lightValue: !item.lightValue ? 0 : item.lightValue,
              fjState: item.state,
            };
            this.title = item.eqName;
            this.stateSwitchVisible = true;
          }
        }
      }
    },

    /* 确认配置（单项）*/
    async submitState() {
      this.stateSwitchVisible = false;
      // this.stateForm ={}
      let param = {
        devId: this.stateForm.eqId,
        devType: this.stateForm.eqType,
        hostId: this.stateForm.eqHostId,
        state: this.stateForm.state,
        tunnelId: this.currentTunnel.id,
      };
      this.sendDirective(param)
      // if (await this.sendDirective(param)) {
      //   // 非照明类（plc）提交
      //   this.setConfigData(param);
      // } else {
      //   this.setWebsocket(param);
      // }
    },
    async sendDirective(param) {
        const res = await deviceApi.sendDirective(param)
        console.log(res)
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
      setConfigData(param).then(response => {
        console.log("配置成功")
      }).catch(err => {
        console.log("配置失败,后台报错：" + err)
      })
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
    //================================================单个配置结束==================================

    //================================================批量配置开始====================================
    /* 点击批量配置*/
    batchManage() {
      this.devicesList = [];
      this.batchVisible = true;
      mode = "buttonSelection";
      this.title = "批量操作";
      // this.$refs.batchForm.clearValidate();
      this.batchForm.eqType = this.eqTypeList[1].typeId;
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqType: this.eqTypeList[1].typeId,
      };
      this.batchFormDirection = 1;
      this.selectEquipment(param);
    },
    /* 批量选择后*/
    handleSelectionChange(val) {
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
    /* 批量确认按钮*/
    submitBatchState(formName) {
      let that = this;
      if(that.batchForm.eqList.length == 0) {
        return that.$modal.msgWarning('请选择设备')
      }
      this.$refs[formName].validate((valid) => {
        let eqIdList = [];
        if (valid) {
          for (let i = 0; i < that.batchForm.eqList.length; i++) {
            eqIdList.push(that.batchForm.eqList[i].eqId);
          }
          // console.log("批量配置的设备id集合:" + eqIdList)
          // console.log("批量配置的设备类型id:" + that.batchForm.eqType)
          // console.log("批量配置的设备状态:" + that.batchForm.state)
          if (eqIdList.length > 0) {
            for (let i = 0; i < eqIdList.length; i++) {
              let param = {
                devId: eqIdList[i],
                devType: that.batchForm.eqType,
                state: that.batchForm.state,
                tunnelId: that.currentTunnel.id,
              };
              // that.setConfigData(param);

              this.$refs["batchForm"].clearValidate();
              that.batchVisible = false;
            }
            this.dialogVisible = true;
            this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,eqIdList);
          }
        } else {
          return false;
        }
      });
    },
    //========================================批量配置结束================================================
    //========================================控制策略开始================================================
    /* 跳至策略页面*/
    strategyPage() {
      //this.$router.push('/strategy/index')
      this.loading = true;
      this.strategyVisible = true;
      this.title = "控制策略";
      listStrategy({
        strategyType: 0,
        tunnelId: this.currentTunnel.id,
      }).then((response) => {
        this.strategyList = response.rows;
        this.loading = false;
      });
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
    //========================================控制策略结束================================================
    /* 跳至操作日志页面*/
    operationLogPage() {
      this.$router.push({
        name: "OperationLog",
      });
    },
    /* 打开图标说明对话框*/
    iconExplain() {
      this.explainVisible = true;
      this.title = "图标含义";
    },
    /* 关闭所有对话框*/
    cancel() {
      this.tunnelVisible = false;
      this.stateSwitchVisible = false;
      this.explainVisible = false;
      this.batchVisible = false;
      this.shuibengVisible = false;
      this.weiboVisible = false;
      this.cameraVisible = false;
      this.daolujiebingVisible = false;
      this.lightControlDialog = false;
      this.cameraErrorInfo = "";
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
      this.stateForm = {};
    },
    handleTableWheel(event) {
      let obj = this.$refs.divRoller;
      return this.tableZoom(obj, event);
    },
    tableZoom(obj, event) {
      // 一开始默认是100%
      let zoom = parseInt(obj.style.zoom, 10) || 100;
      // 滚轮滚一下wheelDelta的值增加或减少120
      zoom += event.wheelDelta / (Math.abs(event.wheelDelta) / 10);
      var ii = 0;
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
      if (zoom > 0) {
        obj.style.zoom = zoom + "%";
      }
      this.fnWheel(obj, event, zoom);
      return false;
    },
    fnWheel(obj, e, zoom) {
      // 思路：以鼠标为中心实现滚动的话，那么将会鼠标在背景图片（注意我这里是用背景图片的，不是img的）中滚动的时候的位置比率是不会变的
      if (e.wheelDelta > 0) {
        this.wheelFlag = true;
      } else {
        this.wheelFlag = false;
      }
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
    lightControl(){
      this.lightControlDialog = true
      this.title = '车指批量控制'
      //获取当前选择的是哪个隧道
      const index = this.lightControForm.index
      listTunnels().then(response => {
          //获取当前隧道有几个车道
          this.lightControForm.lane = response.rows[index].lane
          if(!this.lightControForm.name){
              //获取当前隧道名字
              this.lightControForm.name = response.rows[0].tunnelName
          }
      });
    },
    // 所有车指器弹窗确定按钮
    lightSubmitState(){
        // 所有隧道的所有设备
        var itemArr = []
        listDevices({eqType:1}).then((response) => {
                for(var item of response.rows){
                    if(this.lightControForm.name == item.tunnelName.tunnelName){
                        // 通过隧道名 获取当前隧道的所有设备
                        if(this.lightControForm.lCDirection == item.eqDirection){
                            // 当前选中的车道 和 设备所属车道对应上 获取该车道里设备的id
                            if(this.lightControForm.state == item.lane){
                                itemArr = itemArr.concat(item)
                                console.log(itemArr,"itemArr")
                            }
                        }
                    }
                }
              console.log(itemArr,"itemArr2")
              for(var index of itemArr){
                  let param = {
                    devId: index.eqId,
                    devType: index.eqType,
                    state: index.lane,
                    tunnelId: index.eqTunnelId,
                    hostId: index.eqHostId,
                  }
                  setConfigData(param).then(response => {
                    console.log("配置成功")
                    this.lightControForm.lCDirection = ''
                    this.lightControForm.state = ''
                    this.lightControForm.radioLightControlTop = ''
                    this.lightControForm.radioLightControlBottom = ''

                  }).catch(err => {
                    console.log("配置失败,后台报错：" + err)
                  })
              }

        });

        this.lightControlDialog = false
    },
    changeLightControlTop(){
        if(!this.lCTop){
            this.lCTop = true
        }else{
            this.lCTop = false
        }
    },
    changeLightControlBottom(){
        if(!this.lCBottom){
            this.lCBottom = true
        }else{
            this.lCBottom = false
        }
    },
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
          textStyle: { fontSize: "100%" },
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
          axisLabel: { textStyle: { color: "#fff", fontSize: 14 } },
          data: xData,
          axisLine: { lineStyle: { color: "#000", width: 1 } },
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
            textStyle: { color: "#fff", fontSize: "100%" },
            margin: 8,
          },
          axisLine: { lineStyle: { color: "#000", width: 1 } },
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
                { offset: 0, color: "#3a76f6" },
                { offset: 0.25, color: "#66d4fa" },
                { offset: 0.75, color: "#f8d470" },
                { offset: 1, color: "#ec637b" },
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
  },
  //实例销毁前清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
    this.timer = null;
  },

};
</script>

<style lang="scss" scoped>
    .footer{
        width: 100%;
        height: 24%;
        padding: 0px 30px;
        margin-top: 20px;
        display: flex;

        .footMiniBox{
            width: 22%;
            height: 100%;
            background-image: url(../../assets/cloudControl/footer_bg.png);
            background-position: center;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            color: white;
            .footTitle{
                padding: 5px 20px;
                line-height: 40px;
            }
            .listContent{
                height: 140px;
                font-size: 16px;
                margin-top: 5px;
                overflow: hidden;
            }
            .listRow{
                margin-top: 5px;

            }
        }
        .footContent{
            width: 40%;
            height: 50%;
            margin-top: 40px;
            margin-left: 50px;
            background-image: url(../../assets/cloudControl/thumbnail.png);
            background-position: center;
            background-repeat: no-repeat;
            background-size: 100% 100%;
        }
        .footerRight{
            margin-left: 50px;
        }
    }
    .bigTypeButton{
        color: white;
        width: 200px;
        height: 60px;
        margin-top: 40px;
        line-height: 60px;
        padding-left: 30px;
        color: #baddff;
        background-image: url(../../assets/cloudControl/right_button.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
    }
    .tunnelBox{
        height: 100%;
        overflow: hidden;
        margin: 0 auto;
        background-image: url(../../assets/cloudControl/workscreen.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
    }
    ::v-deep .menu-b .el-select-dropdown{
        background-color: #03192E !important;
        color: white;
        border: solid 1px #e1feff;
       z-index: 100;
    }

    ::v-deep .menu-b .el-input--small .el-input__inner{
        background-color: transparent !important;
        border: solid 1px #68B5FF;
        color: #e1feff;
        box-shadow: 0px 0px 10px #68B5FF;
    }
    ::v-deep .menu-b .el-select .el-input .el-select__caret{
        color: #e1feff;
    }
    ::v-deep .menu-b .el-select > .el-input{
        margin-top: 5px;
    }
    // ::v-deep .menu-b .el-select--small{
    //     transform: translateY(-6px);
    // }
    // 火灾报警弹窗
::v-deep .temperatureDialog .el-dialog{
        margin-top: 15vw !important;
        border: solid 4px #9F0000;
        background-color: rgba($color: #9d0000, $alpha: 0.5);
        border-radius: 4px;

        .el-dialog__body{
            display: flex;
            font-size: 20px;
            text-align: center;
            padding: 0%;
            >div{
                width: 50%;
                color: white;
                margin-bottom: 10%;
                >div:first-of-type{
                    color: #C9C9C9;
                    line-height: 50px;
                }
            }
            >div:nth-of-type(1){
                border-right: solid 1px rgba(255,255,255,0.5);
                z-index: 10;
            }
            >div:nth-of-type(2){
                z-index: 10;
            }
            .HZdialogBg{
                width: 100%;
                height: calc(100% - 54px);
                position: absolute;
                background: url(../../assets/image/fire_bg.png) no-repeat;
                opacity: 0.3;
                background-size: 85% 200%;
                background-position: 130px -20px;
            }

        }

}

// 隧道温度
.temperature{
  font-size: 16px;
  font-weight: bold;
  position: absolute;
  img{
      width: 25px;
      height: 25px;
  }
}
// 字幕滚动
.seamless-warp2 {
    background-image: url("../../assets/image/scroll_bg.png");
    transform: rotate(-90deg);
    // height: 25px;
    // width: 100px;
    overflow: hidden;
    position: absolute;
    top: 32%;
    right: 20%;
    background-size:100% 100%;
    .item{padding-left:0px;width: 400px;
      li{list-style:none;float: left;margin-right:20px;letter-spacing: 2px;color:white;}
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
  color: #ced8e4;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.workbench-header {
  padding-right: 20px;
  height: 45px;
  transform: translateY(-23px);
}

.flex-row {
  display: flex;
  flex-direction: row;
}

.my-back {
  background-image: url("../../assets/image/workbench/background.jpg");
  background-color: #d6edf9;
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
  justify-content: center;
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
.el-button--info {
  // color: #C0CCDA;
  // background-color: #2e354f;
  // border: 1px solid #4F5C69;
  color: #e1feff;
  background-color: transparent !important;
  border: 1px solid #68B5FF;
}

/* 鼠标在按钮上悬浮*/
.el-button:hover {
  background-color: #3a4e86; // #4e5e89
  border: 1px solid #7f8c98;
}

/*自定义primary类型按钮样式*/
.el-button--primary {
  color: #bcbfe2;
  background-color: transparent !important; //#2e354f;
  border: none;
  background-image: url(../../assets/cloudControl/button1.png);
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
}

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
}

.menu-title {
  width: 150px;
  color: #ced8e4;
  font-size: 15px;
  margin-bottom: 0px;
  margin-left: 30px;
  line-height: 40px;
}

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
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* margin: 0 10px 0 10px; */
}

.workbench-content {
  width: 80%;
  height: 100%;
  -webkit-user-select: none;
  user-select: none;
}

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
  align-items: center;
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
  background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 2px 0 2px 0;
  border: 0;
  margin: 0;
  border-radius: 2px;
  color: #a2a2a3;
}

.s-config-img-input {
  width: 80px;
  // background-color: #545559;
  background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 2px 0 2px 0;
  border: 0;
  margin: -7px;
  border-radius: 2px;
  color: #a2a2a3;
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
  display: flex;
  justify-content: center;
  align-items: center;
  height: 500px;
  width: 1442px;
  background-size: cover;
  position: relative;
  // margin-left: 90px;
  top: 23px;
}

.back-img {
  height: 500px;
  width: 1442px;
  position: absolute;
}

.equipment-img {
  height: 30px;
  width: 30px;
}

.s-equipment-img {
  height: 20px;
  width: 22px;
}

.wrapper {
  height: 500px;
  width: 100%;
  /* border: 1px solid #000; */
  border-radius: 10px;
  position: relative;
  z-index: 3;
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

/* 自动换行*/
.wrap {
  word-wrap: break-word;
  word-break: normal;
}

.form-direction {
  width: 60px;
  justify-content: space-around;
}
.el-radio__input.is-checked + .el-radio__label {
  color: #c0ccda !important;
  font-weight: bold;
}
</style>
<style lang="scss">
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

/*单个设备配置框 */
.workbench-dialog {
    // .el-dialog{
    //     left: 30%;
    // }
  .el-form-item__label {
    color: #c0ccda;
  }

  .el-dialog__header {
    background-color: #455d79;
    color: #fff;
  }

  .el-dialog__title {
    color: #fff;
  }

  .el-dialog__body {
    color: #c0ccda;
    background-color: #304156;
    padding-top: 20px;
    padding-bottom: 10px;
  }

  .el-dialog__footer {
    color: #c0ccda;
    background-color: #304156;
    padding-top: 0;
  }

  .el-form-item__content {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-left: 0;
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
    padding: 5px 300px 5px 20px;
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
}

/* 批量管理中的table*/
.batch-table {
  //table为空时
  .el-table__empty-block {
    background-color: #304156;
  }

  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  //鼠标停留在table
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: #455d79;
  }

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

  .el-table__body-wrapper {
    background-color: #304156;
  }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    background-color: #304156;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: #455d79;
    border-radius: 3px;
  }
}

/* 设备含义中的table*/
.explain-table {
  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  /* 鼠标悬浮*/
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: #455d79;
  }

  /* table边框*/
  .el-table--enable-row-transition .el-table__body td {
    border-color: #304156;
    // border-width: 8px;
  }

  .el-table::before {
    height: 0;
  }

  .el-table .cell {
    height: 33px;
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
    background-color: #304156;
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
  .el-table__body {
    .el-table__row {
      .el-table__cell:nth-child(2) {
        text-align: left;
      }
    }
  }
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
.el-checkbox{
    color: #c0ccda;
}
.carContentRight,.carContentLeft{overflow:hidden;width:100%;}
.runingCarRight{position:absolute;transition: 1.6s;}
.runingCarLeft{position:absolute;}
// 跑车
.sportCarBox{width:100%;}
.sportCar{height:80px;position: absolute;transition: 1.6s;}


.item-title{font-size:16px;}
.oldContent,.newContent{
  .el-input__inner{background:#395572;border:unset;text-align:center;}
}
.fromTitle span{font-size:18px;font-weight:bold;}
.offline,.online{
  .el-button{background-color:unset;border:unset;padding:0px;color:white;}
}
.offline{
  .el-button{
    span{
      i{color:red;}
    }
  }
}
.online{
  .el-button{
    span{
      i{color:#1de31d;}
    }
  }
}
.iconList{
  .el-row{
    .el-col-4,.el-col-5{
      .el-button{
        background-color:unset;border:unset;padding:0px;color:white;
      }
    }
    i{color:white;font-size:16px;}
  }
}

</style>
