<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-17 14:42:00
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-29 16:27:17
 * @FilePath: \tunnel-ui\src\views\event\reservePlan\workBench.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <el-dialog
      :before-close="handleClose"
      :visible.sync="workbenchOpen"
      append-to-body
      title="预览"
      width="1620px"
      class="workbenchBox"
    >
      <img alt="" class="chedaoImage" :src="currentTunnel" />
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
        id="33333"
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
          id="2222"
        >
          <!-- 巡检机器人 -->
          <!-- v-show="
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
            " -->
          <div
            :class="{ focus: item.focus }"
            id="1111"
          >
            <img
            v-show="item.eqType != '31'"
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
              :src='url'
              :class="
                item.eqName == screenEqName
                  ? 'screenEqNameClass'
                  : ''
              "
            />
            <img 
              v-show="item.eqType == '31'"
            style="position: absolute"
              :style="{
                
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
              :src= getTypePic(item)
              :class="
                item.eqName == screenEqName
                  ? 'screenEqNameClass'
                  : ''
              ">
            </img>

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
      <div class="scrollBox">
        <div
          v-for="item in previewList"
          :key="item.strategyId"
          :style="{ width: 100 / previewList.length + '%', height: '80px' }"
        >
          <p class="scrollTitle">
            <el-tag type="success" size="medium">{{
              item.strategyName
            }}</el-tag>
          </p>
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="listContent"
            :data="item.policyInformation"
          >
            <div
              v-for="(items, index) in item.policyInformation"
              :key="index"
              class="listRow"
            >
              <div style="color:red;" v-if="items.indexOf('动态') != -1">
                {{ index + 1 }}:<span>{{ items }}</span>
              </div>
              <div style="color: #000;" v-if="items.indexOf('动态') == -1">
                {{ index + 1 }}:<span>{{ items }}</span>
              </div>
            </div>
          </vue-seamless-scroll>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="workbenchOpenEvent">取 消</el-button>
        <el-button type="primary" @click="workbenchOpenEvent">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
import {
  addProcess,
  getListByRId,
  previewDisplay,
} from "@/api/event/reserveProcess";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { listType, getTypeAndStrategy } from "@/api/equipment/type/api.js";
import { icon, laneImage } from "../../../utils/configData.js";
import { getDeviceData } from "@/api/workbench/config.js";
import {
  getLiPowerDevices,
  //initLipowerDevice
} from "@/api/system/lipowerDevice";
export default {
  components: {
    vueSeamlessScroll,
  },
  data() {
    return {
      currentClass: "",
      eventId: "", //事件id
      displayNumb: false, //显示编号
      lightSwitch: 0,
      screenEqName: "", //筛选设备名称
      currentTunnel: {},
      //车道列表
      laneUrlList: laneImage,
      tunnelId: "", //隧道id
      id: "", //预案id
      workbenchOpen: false,
      selectedIconList: null, //设备图标渲染数据
      selectedIconList1: null, //设备图标渲染数据
      previewList: null, //预览数据
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
    };
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
  },
  methods: {
    init() {
      console.log(this.tunnelId, "隧道id");
      this.getTunnelData(this.tunnelId);
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
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    /* 获取实时数据PLC*/
    getRealTimeData(tunnelId) {
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
        tunnelId: tunnelId,
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
    /* 获取隧道配置信息*/
    getTunnelData(tunnelId) {
      let that = this;
      that.upList = [];
      that.downList = [];
      getTunnels(tunnelId).then((response) => {
        that.loading = false;
        console.log(response, "===========");
        let res = response.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          listType("")
            .then((response) => {
              for (let i = 0; i < res.eqList.length; i++) {
                res.eqList[i].focus = false;

                for (let j = 0; j < response.rows.length; j++) {
                  // if (
                  //   res.eqList[i].eqType == 7 &&
                  //   response.rows[j].typeId == 7
                  // ) {
                  //   console.log(res.eqList[i]);
                  // }
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
              this.getRealTimeData(tunnelId);
              // this.getLiPowerDevice();
              this.workbenchOpen = true;
              this.getPreview();
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
              // that.initEharts();
              // 切换隧道配置信息时，联动大类查询
              // that.displayControl(
              //   that.selectBigType.index.toString(),
              //   that.selectBigType.bigType.toString()
              // );
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
              that.currentTunnel = that.laneUrlList[i].url;
              // that.railingList[1].position.left = that.laneUrlList[i].width
            }
          }
        } else {
          console.log("不存在");
          //不存在
          that.selectedIconList = [];
          // that.initEharts();
          //工作台默认背景图
          // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
    },
    getPreview() {
      previewDisplay(this.id, this.eventId).then((res) => {
        this.previewList = res;
        console.log(this.previewList, ";;;;;;;;;");
        var deviceList = [];
        for (let i = 0; i < this.previewList.length; i++) {
          var item = this.previewList[i].strategyRl;
          var arr = this.previewList[i].equipmentData;
          for (let z = 0; z < item.length; z++) {
            var arr = this.previewList[i].iFileList[z];
            if (item[z].equipments != "") {
              if (item[z].equipments.indexOf(",")) {
                console.log(item[z].equipments, "////////////");
                deviceList.push({
                  list: item[z].equipments.split(","),
                  state: item[z].state,
                  eqId: this.previewList[i].deviceTypeId,
                  file: arr,
                });
              } else {
                deviceList.push({
                  list: item[z].equipments,
                  state: item[z].state,
                  eqId: this.previewList[i].deviceTypeId,
                  file: arr,
                });
              }
            } else {
              deviceList.push({
                list: item[z].equipments.split(","),
                state: item[z].state,
                eqId: this.previewList[i].deviceTypeId,
                file: arr,
              });
            }
          }
        }
        this.deviceList = deviceList;
        console.log(this.deviceList, "12312312321");
        this.ChangeDeviceState();
      });
      this.workbenchOpen = true;
    },
    // 操作设备，改变设备状态
    ChangeDeviceState() {
      // console.log(this.selectedIconList, "-------------");
      console.log(this.deviceList, "mmmmmmmmmmmmmmmmmmmm");
      for (let i = 0; i < this.selectedIconList.length; i++) {
        for (let x = 0; x < this.deviceList.length; x++) {
          var eqType = this.selectedIconList[i].eqType;
          if ((eqType ?? "") !== "") {
            if (eqType == this.deviceList[x].eqId) {
              var brr = this.deviceList[x].list;
              console.log(brr, "zzzzzzzzzzzzzzzzz");
              for (let p = 0; p < brr.length; p++) {
                if (this.selectedIconList[i].eqId == brr[p]) {
                  this.selectedIconList[i].url = [];
                  let url = this.deviceList[x].file;
                  console.log(url);
                  url.forEach((item) => {
                    this.selectedIconList[i].url.push(item.url);
                  });
                }
              }
            }
          }
        }
      }
    },
    // 关闭弹窗
    workbenchOpenEvent() {
      //   this.getTunnelData(this.tunnelId);
      this.workbenchOpen = false;
    },
    //关闭drawer
    handleClose(done) {
      done();
    },
  },
};
</script>

<style lang="scss" scoped>
.chedaoImage {
  // position: relative;
  width: 1620px;
  height: 580px;
  margin-bottom: 20px;
}
.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  /* // align-items: center; */
  width: 40px !important;
}
.listContent {
  height: 70%;
  font-size: 14px;
  margin-top: 5px;
  overflow: hidden;
}
.scrollBox {
  width: 100%;
  height: 125px;
  display: flex;
  .scrollTitle {
    text-align: center;
    padding: 10px 0;
    font-size: 16px;
    font-weight: bold;
  }
  .listRow {
    div {
      text-align: center;
    }
  }
}
.red {
  color: red;
}
</style>
<style>
.workbenchBox .el-dialog__body {
  position: relative;
  padding: 0px !important;
}
</style>
