<template>
  <div>
    <!--画布区域-->
    <el-image
     class="back-img"
    :src="currentTunnel.lane.url"
    :style="{ width: currentTunnel.lane.width + 'px' }"                  
    ></el-image>
    <div
      class="wrapper"
      id="eq-wrapper"
      @mousedown="dian"
      @mousemove="yi"
      @mouseup="li"
      @mouseover="mouseoversImage"
      @mouseleave="mouseleaveImage"
    >
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
      <!-- 巡检机器人 -->
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
        >
          <img
            src="../../assets/logo/equipment_log/robot_zaixian.png"
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
          left: item.position.left - 12 + 'px',
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
              style="position: relative"
              :style="item.eqType || item.eqType == 0 ? 'cursor: pointer;' : ''"
              :width="item.iconWidth"
              :height="item.iconHeight"
              :key="item.deptId + indexs"
              :src="url"
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
                font-size: 14px;
                position: absolute;
                color: #79e0a9;
                text-decoration: underline;
                padding-left: 5px;
                width: 100px;
                text-align: left;
              "
              v-if="item.eqType == 19"
            >
              {{ item.value }}
              <label v-if="item.eqType == 19" style="font-size: 14px"
                >ppm</label
              >
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
              {{ item.value }}
              <!-- <label v-if="item.eqType == 16" style="font-size:14px;">m/s</label> -->
            </label>
            <!-- 洞内洞外 -->
            <label
              style="
                font-size: 14px;
                position: absolute;
                text-decoration: underline;
                color: #f2a520;
                padding-left: 5px;
                width: 100px;
                text-align: left;
              "
              v-if="item.eqType == 5"
            >
              {{ item.value }}cd/m2
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
          style="color: #055270"
        />
        <div v-else style="width: 80px"></div>
      </div>
    </div>
 <!--状态切换对话框-->
   <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      :visible.sync="stateSwitchVisible"
      width="560px"
      append-to-body
      :class="stateForm.manufacturers?'robot-dialog':''"
    >
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
        style="position: relative;"
      >
        <el-form-item label="设备桩号:">
          {{ stateForm.pile }}
        </el-form-item>
        <el-form-item label="设备类型:">
          {{ stateForm.eqTypeName }}
        </el-form-item>
        <el-form-item label="方向:">
          <div v-if="stateForm.eqDirection == 0">
            {{ rightDirection + "方向" }}
            <!-- <img src="../../../assets/image/workbench/long-arrowhead.png"/> -->
          </div>
          <div v-else-if="stateForm.eqDirection == 1">
            {{ leftDirection + "方向" }}
          <!--  <img
              src="../../../assets/image/workbench/long-arrowhead.png"
              style="transform: rotate(180deg); margin-left: 10px"
            /> -->
          </div>
        </el-form-item>
        <el-form-item label="调光强度:" v-if="stateForm.eqType == 99999999">
          <div style="width: 200px; float: left">
            <slider :min="0" :max="100" v-model="stateForm.lightValue"></slider>
          </div>
          <label style="color: yellow; margin-left: 10px; pointer-events: none">
            {{ stateForm.lightValue }}</label
          >
        </el-form-item>
        <!-- 所有设备 -->
        <el-form-item label="运行状态:" >
          {{ '正常' }}
        </el-form-item>
        <!-- plc -->
        <el-form-item label="配置状态:" v-if="stateForm.eqType == 14">
          {{ '在线'}}
        </el-form-item>
        <!-- 压力表 -->
        <el-form-item label="配置状态:" v-if="stateForm.eqType == 28">
          {{ '正常'}}
        </el-form-item>
        <el-form-item label="配置状态:" v-if="stateForm.eqType == 21">
          {{ '在线'}}
        </el-form-item>
        <!-- 智能手动报警 -->
        <el-form-item label="配置状态:" v-if="stateForm.eqType == 34">
          {{ '在线'}}
        </el-form-item>
          <!-- 消防水泵 -->
        <el-form-item label="水位:" v-if="stateForm.eqType == 13">
          {{ '水位'}}
        </el-form-item>
        
          <!-- ====================微波检查器开始====== -->
          <el-form-item label="配置状态:" v-if="stateForm.value &&stateForm.eqType == 20">
          {{ '正常'}}
        </el-form-item>
            <el-form-item label="车道:" v-if="stateForm.value && stateForm.eqType == 20">
          {{ '这是车道'}}
        </el-form-item>
         <el-form-item label="车流量:" v-if="stateForm.value && stateForm.eqType == 20">
          {{ '10辆/没分钟'}}
        </el-form-item>
         <el-form-item label="平均车速:" v-if="stateForm.value && stateForm.eqType == 20">
          {{ '60公里/每小时'}}
        </el-form-item>
         <el-form-item label="占有率:" v-if="stateForm.value && stateForm.eqType == 20">
          {{ '60%'}}
        </el-form-item>
        <el-form-item label="上传时间:" v-if="stateForm.value && stateForm.eqType == 20">
          {{ '今天'}}
        </el-form-item>
            <!-- ====================微博检查结束============ -->
        <el-form-item label="状态:" v-if="stateForm.value && ![5, 6, 14, 13,15, 16,20].includes(stateForm.eqType)">
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
        </el-form-item>
        <el-form-item
          label="配置状态:"
          v-if="stateForm.eqType != 21 && !stateForm.value && stateForm.eqType != 123 && stateForm.eqType != 28  && stateForm.eqType != 34"
        >
          <!-- <div v-if="spanEqtypeDate" style="white-space: nowrap;">暂未获取</div> -->
          <div class="wrap" v-if="stateForm.eqType != 111">
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
                      v-for="url in item.url"
                      class="equipment-img"
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
                    <img class="equipment-img" :src="item.url[1]"/>
                    <img class="equipment-img" :src="item.url[0]"/>
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
        <!-- 应急照明 -->
       <el-form-item
          label="配置状态:"
          v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 6"
        >
          <!-- <div v-if="spanEqtypeDate" style="white-space: nowrap;">暂未获取</div> -->
          <div class="wrap" v-if="stateForm.eqType != 111">
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
                      v-for="url in item.url"
                      class="equipment-img"
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
                    <img class="equipment-img" :src="item.url[1]"/>
                    <img class="equipment-img" :src="item.url[0]"/>
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
        <!-- 消防水泵 -->
       <el-form-item
          label="配置状态:"
          v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 13"
        >
          <!-- <div v-if="spanEqtypeDate" style="white-space: nowrap;">暂未获取</div> -->
          <div class="wrap" v-if="stateForm.eqType != 111">
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
                      v-for="url in item.url"
                      class="equipment-img"
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
                    <img class="equipment-img" :src="item.url[1]"/>
                    <img class="equipment-img" :src="item.url[0]"/>
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
            <video
              v-if="watchVideo"
              controls
              muted
              autoplay
              loop
              :src="robotVideoUrl"
              style="width: 250px; height: 150px;"
            >
              <source src type="video/mp4"/>
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
            <battery-icon v-if="!(stateForm.electricQuantity == ''|| stateForm.electricQuantity == null)"
                          style="width:auto;" :quantity="stateForm.electricQuantity" rotate="0"/>
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
              <div id="robotEchart"/>
            </el-tab-pane>
          </el-tabs>
        </template>
        <!-- <el-form-item label="模拟量:" v-if="stateForm.value">
          <div id="analogQuantity"></div>
        </el-form-item> -->
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          v-if="stateForm.eqType != 21 && !stateForm.value"
          @click="submitState"
          class="submitButton"
        >确 定
        </el-button>
        <el-button
          type="primary"
          size="mini"
          v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 14"
          @click="submitState"
          class="submitButton"
        >确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

      <!--摄像机对话框-->
      <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table video-dialog"
      :title="title"
      :visible="cameraVisible"
      width="860px"
      append-to-body
      @opened="loadFlv"
      :before-close="handleClosee"
    >
      <el-form
        ref="form"
        :model="stateForm"
        label-width="60px"
        label-position="left"
        size="mini"
      >
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
                <img src="../../assets/image/workbench/long-arrowhead.png"/>
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
        <el-form-item label="实时视频:" label-width="80px">
           <video id="videoBox" controls muted autoplay loop style="width: 80%;"></video>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
              v-for="(items,index) in eqTypeStateList"
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
                    <img class="equipment-img" :src="items.url[1]"/>
                    <img class="equipment-img" :src="items.url[0]"/>
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
                  <img class="equipment-img" :src="items.url[0]"/>
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
        </el-button
        >
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
  import flvjs from "flv.js";
  import {
  listEqTypeState,
  getEqTypeState,
  updateEqTypeState,
  getStateByRun,
  getStateByData
} from "@/api/equipment/eqTypeState/api";
import {
  listTunnels,
  getTunnels,
  updateTunnels,
  getConfigData,
  getStorageData,
  setConfigData,
  pressure,
  sendAnalogCom,
  getHostData,
  setCorLight,
 } from "@/api/equipment/tunnel/api.js";
 import {
  listType,
  listHasType,
  getType,
  groupByBigType,
  hasListByBigType,
  loadPicture,
} from "@/api/equipment/type/api.js";
import {
  icon,
  // eqTypeStateIcon,
  laneImage,
} from "../../utils/configData.js";
import {
  listDevices,
  getDevices,
  updateDevices,
} from "@/api/equipment/eqlist/api";
import {
  getDeviceBase,
  getNewBoardEditInfo,
  templateList,
} from "@/api/workbench/config";
let configData = {}; //配置信息
let wrapperClientX = 0;
let wrapperClientY = 0;
let boxEqList = [];
let mode = "";
export default {
  props: {
    currentTunnel:{
      type:Object,
      default:()=>{}
    },
    //隧道id  不支持异步
    tunnelId:{
      type:String,
      default:''
    },
    eqTypeList:{
      type:Array,
       default:()=>[]          
    },
      
  },
  data() {
    return {
      leftDirection: "潍坊", //左侧方向
      rightDirection: "济南", //右侧方向
      batchFormDirection: 1, //批量对话框默认的设备方向
      title: "", //对话框标题
      stateSwitchVisible: false,//状态切换开关
      cameraVisible:false,
      batchVisible: false,//批量管理对话框
      stateForm: {}, //配置表单
      robotShow:false,//巡检机器人
      //各状态对应图标列表（灵活性差，后期最好在类型状态管理中动态添加）
      eqTypeStateList: [],
      itemEqTypeStateList:[],
      selectedIconList: [], //配置图标
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
      displayNumb: false, //显示编号
      lightSwitch: 0,
      //验证批量管理
      rules: {
        state: [
          {
            required: true,
            message: "请选择设备状态",
            trigger: "change",
          },
        ],
      },
      //批量管理表单
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: ""
      },
      allDirection: [],// 所有方向
      devicesList: [],
      laneUrlList: laneImage,
    };
  },
  methods: {
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
      // console.log(event)
      if (this.px == "" || this.py == "") {
        return;
      }
      let px1 = this.px;
      let px2 = this.py;
      console.log(event.clientX, 10, wrapperClientX, "当前left值");
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
          // this.selectedIconList[i].isfocus = true;
          // console.log(boxEqList.length,'boxEqList.lengthboxEqList.length')
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
          // this.openStateSwitch(boxEqList[0].eqlist[0]);
          // this.$emit('openStateSwitch',boxEqList[0].eqlist[0])
          console.log('打开对话框')
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
            if(this.changeDirection){
              this.devicesList = this.changeDirection(boxEqList[0].eqlist);
            }
           
          }
        }
      }
      this.empty();
    },
                         /* -------------------鼠标拖动结束-------------------*/


    //鼠标划入
    mouseoversImage() {
      //  console.log(this.imageTimer,'清定时器')
      clearInterval(this.imageTimer);
      this.imageTimer = null;
    },
    //鼠标离开
    mouseleaveImage() {
      // console.log('离开了')
      // this.srollAuto();
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
    sensorDisabledTwo(item) {
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return true;
      } else {
        return false;
      }
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
    openTooltip(item, index) {
      this.showTooltipIndex = index
      // this.showTooltip = true;
      // this.sensorDisabled(item)
    },
    closeTooltip(item) {
      this.showTooltipIndex = 999
      // this.showTooltip = false
    },


     //================================================单个配置开始==================================
    /* 打开配置界面*/
    async openStateSwitch(item) {
      console.log(item, '点击的设备');
      let StateTypeId={ 
       StateTypeId: item.eqType
      }
      getStateByRun(StateTypeId).then(res=>{
        //  console.log(res,'uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu')
        this.stateForm.stateName=res.rows[0].stateName
      })
      this.getTunnelData(this.currentTunnel.id);
      // 防止 ‘暂未获取’ 和 配置状态单选同时出现
      this.spanEqtypeDate = true
      let newPromise = new Promise((resolve) => {
        resolve()
      })
      await newPromise.then(() => {
        // console.log(this.eqTypeStateList,'设备状态')
        this.eqTypeStateList.forEach(val => {
          if (item.eqType == val.type && val.control == 1) {
            this.itemEqTypeStateList.push(val)
          }
        })
      })
      if (this.itemEqTypeStateList != []) this.spanEqtypeDate = false

      // 传感器（模拟量）
      let sensorDevice = [1, 2, 3, 4,7, 8, 9, 10, 12, 13, 14, 15, 16,24];
                if (sensorDevice.indexOf(item.eqType) != -1) {
                  // this.stateSwitchVisible = true;
                  // this.stateForm = {
                  //   value: item.value,
                  // };
                  // this.$nextTick(()=>{
                  //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
                  // })
                }
      
      //跳转微波车检
      if (item.eqType == "108") {
        console.log('tiaodaochejianl')
        this.weiboList = eval(item.wbList);
        console.log(this.weiboList)
        this.title = item.eqName;
        this.weiboVisible = true;
        return;
      }
      // 跳转路面状态
      else if (item.eqType == "120") {
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: "", //默认状态
          pile: item.pile,
          eqFeedbackAddress1: item.eqFeedbackAddress1,
        };
        const eqId = {eqId: item.eqId};
        selectByEqDeno(eqId).then(response => {
          this.lumianList = response.data;
        })
        if (item.lmList) {
          this.lmList = JSON.parse(item.lmList)
        }
        console.log(" this.lmList.state == " + item.lmList)
        this.title = item.eqName;
        this.lumianVisible = true;
        return;
      } else if (item.eqType == "10") {
        this.spanEqtypeDate = false
        // 风机
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: item.state, //默认状态
          pile: item.pile,
          value: item.value,
          lightValue: !item.lightValue ? 0 : item.lightValue,
          fjState: item.state,
        };
        this.title = item.eqName;
        this.stateSwitchVisible = true;
      } else if (item.eqType == "8") {
        this.spanEqtypeDate = false
        // 引道照明
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: item.state, //默认状态
          pile: item.pile,
          value: item.value,
          lightValue: !item.lightValue ? 0 : item.lightValue,
          fjState: item.state,
        };
        this.title = item.eqName;
        this.stateSwitchVisible = true;

      }
      //跳转 摄像机
      else if (item.eqType == "24" || item.eqType == "23" ) {
        console.log("点击摄像机")
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: "", //默认状态
          pile: item.pile,
          eqFeedbackAddress1: item.eqFeedbackAddress1,
          eqFeedbackAddress2: item.eqFeedbackAddress2,
        };
        this.title = item.eqName;
        this.cameraVisible = true;
        this.loading = true;
        // this.flvPlayer()
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
          pile: item.pile,
          eqFeedbackAddress1: item.eqFeedbackAddress1,
        };
        if (item.dljb) {
          this.dljbObj = JSON.parse(item.dljb);
        }
        this.title = item.eqName;
        this.daolujiebingVisible = true;
        return;
      }
      //打开情报板
      else if (item.eqType == "100") {
        // getTemplateInfo(item.id).then(res=>{
        //   console.log(res);
        // })
        await getDeviceBase(item.eqId).then((data) => {
          console.log('情报板大小')
          this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
        });
        console.log(item, '情报板信息');
        this.dialogVisible = true;
        this.$refs.vmsContentUpdate.deviceId = item.eqId;
        this.$refs.vmsContentUpdate.isAdd = true;
        // this.$refs.contentBatchEdit.list = item;
        this.$refs.vmsContentUpdate.init();
        // this.dialogVisible = true;
        // this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,item);
      } else if (item.eqType == "111") {
        const eqId = {
          equipmentId: item.eqId,
          tunnelId: this.tunnelId
        }
        this.spanEqtypeDate = false
        var pressureData = {}
        pressure(eqId).then(response => {
          console.log(response, "压力表")
          pressureData = response.data[0]
          this.stateForm = {
            eqType: item.eqType,
            eqTypeName: item.typeName.typeName,
            eqDirection: item.eqDirection,
            pile: item.pile,
            value: item.value,
            equipmentName: pressureData.equipmentName,
            position: pressureData.position,
            tunnelName: pressureData.tunnelName,
            analogQuantity: pressureData.analogQuantity,
            highest: pressureData.highest,
            low: pressureData.low,
            createTime: pressureData.createTime
          };
        });
        this.title = item.eqName;
        this.stateSwitchVisible = true;
      } else if (item.eqType == 112) {
        // 巡检机器人
        this.stateForm = {
          ...item,
          value: item.value,
          state: '正常',
          electricQuantity: '80',
          eqTypeName: item.typeName.typeName,
          eqTunnelName: item.tunnelName.tunnelName,
          manufacturers: '苹果AI',
          dateOfManufacture: '2022/2/23 14:30:45',
        };
        this.robotInformationCollectionData = {}
        // 状态记录
        this.robotRecordList = [
          {robotName: '巡检机器人-001', tunnelName: '马家峪隧道', DateTime: '2022-2-23 15:45:13', state: '离线'},
          {robotName: '巡检机器人-001', tunnelName: '马家峪隧道', DateTime: '2022-2-23 10:23:00', state: '正常'},
        ]
        this.title = item.eqName;
        this.stateSwitchVisible = true;

      } else if (item.eqType == "118") {
        if (!item.eqFeedbackAddress1) {
          this.$modal.msgWarning('没有给设备配置点位地址')
          this.youdaoVisible = false
          return
        }
        this.title = item.eqName
        this.stateForm = {
          eqId: item.eqId,
          eqDirection: item.eqDirection,
          eqFeedbackAddress1: item.eqFeedbackAddress1
        }
        // 诱导灯
        getHostData({hostIP: item.eqFeedbackAddress1}).then(response => {
          this.stateForm = {
            eqId: item.eqId,
            eqDirection: item.eqDirection,
            eqFeedbackAddress1: item.eqFeedbackAddress1,
            corModel: response.corModel,
            Zlane: (response.Zlane == "true" ? true : false),
            Ylane: (response.Ylane == "true" ? true : false),
            whiteLight: parseInt(response.whiteLight),
            yellowLight: parseInt(response.yellowLight),
            twinkleModel: response.twinkleModel,
            twinkleFrequency: response.twinkleFrequency,
            lightTime: response.lightTime
          }
        })
        this.youdaoVisible = true
      }
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
      else if (item.eqType == "123") {
        this.stateForm = {
          eqId: item.eqId,
          eqHostId: item.eqHostId,
          eqName: item.eqName,
          eqType: item.eqType,
          eqTypeName: item.typeName.typeName,
          eqDirection: item.eqDirection,
          state: item.state == '1' ? '已报警' : '未报警',
          pile: item.pile,
        };
        console.log(this.stateForm, "声光报警")
        this.title = item.eqName;
        this.stateSwitchVisible = true;
        return;
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
            // 红绿灯
            this.spanEqtypeDate = false
            this.stateForm = {
              eqId: item.eqId,
              eqHostId: item.eqHostId,
              eqName: item.eqName,
              eqType: item.eqType,
              eqTypeName: item.typeName.typeName,
              eqDirection: item.eqDirection,
              state: item.state, //默认状态
              pile: item.pile,
              value: item.value,
              lightValue: !item.lightValue ? 0 : item.lightValue,
              fjState: item.state,
            };
            this.title = item.eqName;
            // this.stateSwitchVisible = true;
            // console.log('走我了');
            let sensorDevice = [1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 20,24,28,21,34,33];
                if (sensorDevice.indexOf(item.eqType) != -1) {
                  this.stateSwitchVisible = true;
                  // this.stateForm = {
                  //   value: item.value,
                  // };
                  // this.$nextTick(()=>{
                  //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
                  // })
                }
          }
        }
      }
    },
    /* 获取隧道配置信息*/
    getTunnelData(tunnelId) {      
      let that = this;
      that.upList = [];
      that.downList = [];
      const params = {
        tunnelId: tunnelId
      }
      if(tunnelId){
      getTunnels(tunnelId).then((response) => {
        that.loading = false;
        let res = response.data.storeConfigure;

        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          console.log(res,"resdssssssssssss")
          listType("").then((response) => {
            for (let i = 0; i < res.eqList.length; i++) {
              res.eqList[i].focus = false;
              // console.log(response,'responseresponseresponse')
              console.log(res.eqList[i].focus,'item.focus')
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
            console.log(that.selectedIconList, '当前设备所属隧道')  
          }).then(() => {
            // that.initEharts()
            // 切换隧道配置信息时，联动大类查询
            // that.displayControl(that.selectBigType.index.toString(), that.selectBigType.bigType.toString())
          });

          if (res.upList != undefined) {
            that.upList = res.upList;
          }
          if (res.downList != undefined) {
            that.downList = res.downList;
          }
          if (res.leftDirection != undefined && res.leftDirection != '') {
            that.leftDirection = res.leftDirection;
          }
          if (res.rightDirection != undefined && res.leftDirection != '') {
            that.rightDirection = res.rightDirection;
          }

          let id = res.lane;
          console.log(that.laneUrlList,"that.laneUrlList")
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.currentTunnel.lane = that.laneUrlList[i];
              // that.railingList[1].position.left = that.laneUrlList[i].width
            }
          }
        } else {
          console.log('不存在')
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
      });}
    },  
     //打开摄像机对话框
     loadFlv(){
      if (flvjs.isSupported()) {
        var videoElement = document.getElementById("videoBox");
        console.log(videoElement,'-------------------')
        var flvPlayer = flvjs.createPlayer({
            type: 'flv',
            url: 'http://10.166.139.12:8081/live/22456.flv' //你的url地址
        });
        flvPlayer.attachMediaElement(videoElement);
        flvPlayer.load();
        flvPlayer.play();
      }
    },
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
        state: this.stateForm.state,
        tunnelId: this.currentTunnel.id,
      };
      // 发送模拟指令
      this.sendAnalogCommand(param)
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
      sendAnalogCom(param).then(response => {
        if (response.code == 200) {
          this.$modal.msgSuccess('配置成功')
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
      const res = await deviceApi.sendDirective(param)
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
              console.log("记录成功")
            }).catch(err => {
              console.log("记录失败,后台报错：" + err)
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


    
        /* 批量确认按钮*/
    submitBatchState(formName) {
      let that = this;
      if (that.batchForm.eqList.length == 0) {
        return that.$modal.msgWarning('请选择设备')
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
        var arr = IntelligenceBoard[i].eqType
        if (IntelligenceBoard[0].eqType != arr) {
          return that.$modal.msgWarning('设备类型不一致')
        }
      }
      let brr = [];
      that.batchForm.eqList.forEach(item => {
        brr.push(item.eqType);
      })
      if (brr.every(this.everyCheck) == false) {
        console.log('都是情报板设备')
        //如果是情报板设备，则取消验证
        this.rules.state = true;
        // this.$refs.contentBatchEdit.init();
        console.log(that.batchForm.eqList, '选中设备列表');
        // 上面已经判断过是否一致，所以此处不再判断，直接传第一个设备的eqid
        // 打开情报板编辑页面
        getDeviceBase(that.batchForm.eqList[0].eqId).then((data) => {
          this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
        });
        // console.log(item,'情报板信息');
        this.dialogVisible = true;
        let batchFormList = that.batchForm.eqList;
        let deviceList = '';
        batchFormList.forEach(item => {
          deviceList = deviceList.concat(item.eqId)
        })
        console.log(deviceList)
        this.$refs.vmsContentUpdate.deviceId = deviceList;
        console.log(deviceList, '圈选设备id对象')
        this.$refs.vmsContentUpdate.isAdd = false;
        this.$refs.vmsContentUpdate.init();
        return false
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
              })

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
















  /* 查询设备状态图标*/
  async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        isControl: null,
      };
      await getStateByData(queryParams).then((response) => {
        let list = response.rows;
        console.log(response,'qqqqqqqqqqqqqqqqqqqqqqq')
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
      for (var item of that.eqTypeStateList) {
        if (item.type == 18) {
          console.log(item, "引道照明")
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
    //点击摄像机关闭按钮
    handleClosee(){
      this.cameraVisible = false
    },
   
    async flvPlayer(){
      if (flvjs.isSupported()) {
          var video = document.getElementsByClassName("videoElement");
          console.log(video[0],'video');
          if (video) {
              //创建播放器实例
              var player = flvjs.createPlayer({
                  type: 'flv',
                  isLive: true,
                  hasAudio: false,
                  url: `http://10.166.139.12:8081/live/22456.flv`,
              });
              player.attachMediaElement(video);
              try {
                  player.load();
                  player.play()
              } catch (error) {
                  console.log(error)
              }
          }
      }
    },

    /* 批量选择后*/
    handleSelectionChange(val) {
      this.ids = val.map(item => item.id)
      this.single = val.length !== 1
      this.multiple = !val.length
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

    // 点击某一行，将其选中
    handleRowClick(row, i, a) {
          this.$refs.multipleTable.toggleRowSelection(row)
        },

        /* 查询设备*/
    selectEquipment(param) {
      let that = this;
      listDevices(param).then((response) => {
        that.devicesList = that.changeDirection(response.rows);
        // console.log(that.devicesList,'that.devicesList')
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
        this.allLane.forEach(item => {
          switch (Number(item.lane)) {
            case 1:
              item.laneName = '第一车道'
              break;
            case 2:
              item.laneName = '第二车道'
              break;
            case 3:
              item.laneName = '第三车道'
              break;
            case 0:
              item.laneName = '其它'
              break;
          }
        })
      })
    },
     // 查询方向
    selectDirection(param) {
      listDevices(param).then((response) => {
        this.allDirection = this.changeDirection(response.rows);
        for (let i = 0; i < this.allDirection.length; i++) {
          for (let j = i + 1; j < this.allDirection.length; j++) {
            if (this.allDirection[i].eqDirection == this.allDirection[j].eqDirection) {
              this.allDirection.splice(j, 1); //splice会改变原数组
              j--;
            }
          }
        }
      })
    }, 

  },
  watch:{
    w(val) {
      this.move = true;
    },
    // 设备类型
    "batchForm.eqType"(val) {
      if (mode == "buttonSelection") {
        let param = {
          eqTunnelId: this.currentTunnel.id,
          eqType: val,
          lane: this.batchForm.eqlane,
          eqDirection: this.batchForm.eqDirection
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
      }
      this.selectLane(param);
    },
  },
  created(){ 
    // if(this.tunnelId){
    //   console.log(this.tunnelId,'this.tunnelId') 
    //   console.log('子组件的created')
    //   this.getTunnelData(this.tunnelId)
    // }    
    this.getTunnelData(this.tunnelId)
    //获取设备状态图标
    this.getEqTypeStateIcon();
  }

};
</script>

<style lang="scss" scoped>
  .wrapper {
  height: 580px;
  width: 100%;
  /* border: 1px solid #000; */
  border-radius: 10px;
  position: relative;
  z-index: 1;
}
.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  // align-items: center;
  width: 40px !important;
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

.back-img {
  height: 580px;
  // width: 1630px !important;
  position: absolute;
  display: block;
  // top: -22px;
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
  
.flex-row {
  display: flex;
  flex-direction: row;
}

.form-direction {
  width: 60px;
  justify-content: space-around;
}

</style>