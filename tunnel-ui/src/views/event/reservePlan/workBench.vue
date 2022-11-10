<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-17 14:42:00
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-10 09:15:47
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
      width="1660px"
      class="workbenchBox"
    >
      <img alt="" class="chedaoImage" :src="currentTunnel" />
      <!-- 设备图标-->
      <div
        v-for="(item, index) in selectedIconList"
        :key="index"
        :class="
          item.eqType == 7 || item.eqType == 8 || item.eqType == 9
            ? 'light-' + item.position.left
            : ''
        "
        :style="{
          left: item.position.left + 'px',
          top: item.position.top + 'px',
          'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
        }"
        class="icon-box mouseHover"
      >
        <div
          v-show="
            (item.eqType != 7 &&
              item.eqType != 16 &&
              item.eqType != 15 &&
              item.eqType != 8 &&
              item.eqType != 9 &&
              item.eqType != 21 &&
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
            :key="item.deptId + indexs"
            :height="item.iconHeight"
            :src="url"
            :style="item.eqType || item.eqType == 0 ? 'cursor: pointer;' : ''"
            :width="item.iconWidth"
            style="position: relative"
          />
        </div>
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
            :data="item.equipmentData"
          >
            <div
              v-for="(items, index) in item.equipmentData"
              :key="index"
              class="listRow"
            >
              <div>
                {{ index + 1 }}:{{ items.eq_name }}状态:
                {{ items.eq_status }}
              </div>
            </div>
          </vue-seamless-scroll>
        </div>
      </div>
      <!-- <el-steps :active="1" simple>
        <el-step
          v-for="item in previewList"
          :key="item.strategyId"
          :title="item.strategyName"
          status="success"
        >
        </el-step>
      </el-steps> -->
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
export default {
  components: {
    vueSeamlessScroll,
  },
  data() {
    return {
      currentTunnel: {},
      //车道列表
      laneUrlList: laneImage,
      tunnelId: "", //隧道id
      id: "", //预案id
      workbenchOpen: false,
      selectedIconList: null, //设备图标渲染数据
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
              this.getPreview();
              // that.getRealTimeData();
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
    getPreview() {
      previewDisplay(this.id).then((res) => {
        this.previewList = res;
        console.log(this.previewList, "this.previewListthis.previewList");
        var deviceList = [];
        for (let i = 0; i < this.previewList.length; i++) {
          var item = this.previewList[i].strategyRl;
          for (let z = 0; z < item.length; z++) {
            var arr = this.previewList[i].iFileList[z];
            if (item[z].equipments.indexOf(",")) {
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
      // console.log(this.selectedIconList);
      for (let i = 0; i < this.selectedIconList.length; i++) {
        for (let x = 0; x < this.deviceList.length; x++) {
          var eqType = this.selectedIconList[i].eqType;
          if ((eqType ?? "") !== "") {
            if (eqType == this.deviceList[x].eqId) {
              var brr = this.deviceList[x].list;
              for (let p = 0; p < brr.length; p++) {
                if (this.selectedIconList[i].eqId == brr[p]) {
                  this.selectedIconList[i].url = [];
                  let url = this.deviceList[x].file;
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

</style>
<style>
  .workbenchBox .el-dialog__body{position: relative;padding: 0px!important;}
</style>
