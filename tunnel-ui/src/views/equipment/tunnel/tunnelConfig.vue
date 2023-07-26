<template>
  <div class="app-container configurePage" v-loading="saveLoading" style="position: relative;background-color: #D3EBFF;"
       element-loading-background="rgba(0, 0, 0, 0.8)">
    <div class="header config-header">
    </div>
    <div class="menu config-menu">
      <el-row class="menu-button">
        <b class="menu-title">隧道名称：</b>
        <el-button type="info" size="small" icon="el-icon-location-outline" class="tunnelNameButton">
          {{selectedTunnel.name}}</el-button>
        <!-- color:#1890ff-->
        <el-button type="info" size="small" icon="el-icon-sort" class="tunnelNameButton" @click="openLaneDialog">更换车道
        </el-button>
        <el-button type="info" size="small" icon="el-icon-check" class="tunnelNameButton" @click="preserve">保存配置
        </el-button>
        <el-button type="info" size="small" icon="el-icon-refresh-left" class="tunnelNameButton" @click="handleBack">返回</el-button>
        <el-popconfirm popper-class="reset-confirm" confirm-button-text='确定' cancel-button-text='取消' icon="el-icon-info"
                       icon-color="red" title="重置后该隧道的配置信息将被清空,确定清空吗？" @confirm="reset">
          <el-button type="info" size="small" slot="reference" icon="el-icon-refresh" class="tunnelNameButton"
                     style="margin-left: 10px;">重置</el-button>
        </el-popconfirm>

        <el-select
          class="tunnelNameSelect"
          v-model="deviceEqType"
          placeholder="请选择设备类型"
          style="width: 10%; "
          size="small"
          @change="selectEqType"
        >
          <el-option
            v-for="item in deviceEqTypeList"
            :key="item.raw.dictCode"
            :label="item.label"
            :value="item.raw.dictValue"
          >
          </el-option>
        </el-select>

        <div class="display-box">
          <p class="menu-title title-wrap">显示设备编号：</p>
          <el-switch class="title-wrap" v-model="displayNumb" active-color="#46a6ff" inactive-color="#ccc">
          </el-switch>
        </div>
      </el-row>
      <el-row class="eqType">
        <b class="menu-title title-wrap">设备类型：</b>
        <el-button-group class="menu-button-group">
          <el-button type="info" size="mini" v-for="(item,index) in eqTypeList" :key="index"
                     @click="openEquipmentDialog(item.typeId,index,'type')">{{item.typeName}}</el-button>
        </el-button-group>

      </el-row>
      <el-row class="configuration">
        <b class="menu-title title-wrap">环境配置：</b>
        <el-button-group class="menu-button-group">
          <el-button type="info" size="mini" v-for="(item,index) in  dict.type.environment" :key="index"
                     @click="openEquipmentDialog(item.value,index,'configuration')">{{item.label}}</el-button>
        </el-button-group>
      </el-row>
    </div>
    <!--配置区域-->
    <div class="content config-back">
      <div class="config-content">
        <!--画布区域-->
        <el-row class="config-img-box"  id ='svgRow' v-loading="loading" >
          <el-image class="config-img" id="imageId" :src="selectedTunnel.lane.url" :style="{width:selectedTunnel.lane.width + 'px'}" lazy></el-image>
          <svg id="svg" class="tunnelSvg" height="580" :style="{width:selectedTunnel.lane.width + 'px'}" style="position: relative;z-index: 3;" ></svg>
          <!-- 辅助线 -->
          <div id="guide-h" class="guide"></div>
          <div id="guide-v" class="guide"></div>
          <div id="guide-h1" class="guide"></div>
          <div id="guide-v1" class="guide"></div>
        </el-row>
      </div>
    </div>
    <!--车道 选择对话框-->
    <el-dialog v-dialogDrag class="equipment-dialog" :title="title" :visible.sync="laneVisible" width="723px"
               append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="dialog-content">
        <el-tabs type="border-card" class="laneTabs" v-model="activeTab">
          <el-tab-pane label="车道逆向" name="direction0">
            <div class="lane-img-box" v-for="(item,index) in laneUrlList" :key="index" v-if="item.direction == 0">
              <el-image class="lane-img" :src="item.url" :preview-src-list="previewLaneUrlList"></el-image>
              <el-radio class="lane-radio" v-model="laneRadio" :label="index">{{item.name}}</el-radio>
            </div>
          </el-tab-pane>
          <el-tab-pane label="车道同向" name="direction1">
            <div class="lane-img-box" v-for="(item,index) in laneUrlList" :key="index" v-if="item.direction == 1">
              <el-image class="lane-img" :src="item.url" :preview-src-list="previewLaneUrlList"></el-image>
              <el-radio class="lane-radio" v-model="laneRadio" :label="index">{{item.name}}</el-radio>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary"class="submitButton"  @click="submitLane">确 定</el-button>
        <el-button @click="cancel" class="closeButton">取 消</el-button>
      </div>
    </el-dialog>
    <!--设备 选择对话框-->
    <el-dialog v-dialogDrag class="equipment-dialog" :title="title" :visible.sync="equipmentVisible" width="560px"
               append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="dialog-content">
        <div v-for="item in equipmentList" :key="item.id">
          <div class="equipment-img-box">
            <el-row v-if="eqTypeList[equipmentTypeflag].url.length>0">
              <!-- 此处将el-image更换为img、动态更换图片尺寸 -->
              <img :width="eqTypeList[equipmentTypeflag].iconWidth" :height="eqTypeList[equipmentTypeflag].iconHeight>80?'82px':eqTypeList[equipmentTypeflag].iconHeight"
                   :class="[item.exist == true ? 'eq-exist' : '']" v-for="(url,index) in eqTypeList[equipmentTypeflag].url"
                   :key="index" :src="url" @click="getEquipment(item,eqTypeList[equipmentTypeflag])">
            </el-row>
            <el-row v-else>
              <el-image>
                <div slot="error" class="image-slot" @click="tips">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </el-row>
            <el-row v-if="item.pile">
              {{item.pile}}
            </el-row>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" class="closeButton">关 闭</el-button>
      </div>
    </el-dialog>
    <!--环境配置 选择对话框-->
    <el-dialog v-dialogDrag class="equipment-dialog" title="环境配置" :visible.sync="EnvironmentVisible" width="560px"
               append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="dialog-content">
        <div v-for="(items,index) in EnvironmentList[Environmentflag]" :key="index">
          <div class="equipment-img-box">
            <el-row v-if="items.url.length>0">
              <!-- 此处将el-image更换为img、动态更换图片尺寸 -->
              <img :width="items.iconWidth" :height="items.iconHeight" v-for="(url,index) in items.url" :key="index"
                   :src="url" @click="getEquipment(items,items)">
              </img>
            </el-row>
            <el-row v-else>
              <el-image>
                <div slot="error" class="image-slot" @click="tips">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </el-row>
            <!-- <el-row >
              {{items.sdName}}
            </el-row> -->
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" class="closeButton">关 闭</el-button>
      </div>
    </el-dialog>
    <!--风向等设备选择对话框-->
    <el-dialog v-dialogDrag class="equipment-dialog batch-table" :title="title" :visible.sync="paramVisible"
               width="560px" append-to-body>
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="paramFrom" :rules="rules" :model="paramFrom" label-width="80px" label-position="left" size="mini"
               hide-required-asterisk>
        <el-table ref="multipleTable" :data="equipmentList" tooltip-effect="dark" style="width: 100%" max-height="400"
                  size="mini" @selection-change="equipmentSelectionChange" :row-style="rowStyle" :header-cell-style="rowStyle"
                  empty-text="暂无设备">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="eqName" label="名称" width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="pile" label="桩号" width="150" show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="eqDirection" label="方向" width="145">
          </el-table-column>
        </el-table>
        <el-form-item label="设备方向:" prop="direction" style="margin-top:20px;">
          <div class="wrap">
            <el-radio-group v-model="paramFrom.direction">
              <el-radio class="el-radio" label="1"> 上行</el-radio>
              <el-radio class="el-radio" label="2"> 下行</el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="submitParam('paramFrom')">确 定</el-button>
        <el-button type="primary" size="mini"  class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <ul v-show="deleteVisible" :style="{left:left - svgScrollLeft +'px',top:top + 40 +'px'}" class="contextmenu">
      <li @click="deleteImage"><i class="el-icon-delete"></i>删除</li>
    </ul>

  </div>
</template>

<script>
import $ from "jquery";
import "jquery-ui-dist/jquery-ui";
import "jquery-ui-dist/jquery-ui.min.css";
import DragItDude from "vue-drag-it-dude";
import Snap from "imports-loader?this=>window,fix=>module.exports=0!snapsvg/dist/snap.svg.js";
import {
  newListDevices,
  getDevices,
  updateDevices,
} from "@/api/equipment/eqlist/api";
import {
  listConfiguration,
  getConfiguration,
  delConfiguration,
  addConfiguration,
  updateConfiguration,
  exportConfiguration,
} from "@/api/equipment/configuration/configuration";
import { listLipowerDevice } from "@/api/system/lipowerDevice";
import {
  listType,
  getType,
  delType,
  addType,
  updateType,
  loadPicture, hasListByBigType,
} from "@/api/equipment/type/api.js";

import {
  listTunnels,
  getTunnels,
  delTunnels,
  addTunnels,
  updateTunnels,
  getConfigData,
} from "@/api/equipment/tunnel/api.js";
import {
  listEqTypeState,
  getEqTypeState,
  delEqTypeState,
  addEqTypeState,
  updateEqTypeState,
} from "@/api/equipment/eqTypeState/api";
import { defaultIcon, laneImage } from "../../../utils/configData.js";

var selectedIconIndex = ""; //删除索引
var img = [];
export default {
  name: "TunnelConfig",
  dicts: ["environment","sd_sys_name"],
  components: {
    DragItDude,
  },
  data() {
    return {
      visible: true,
      windowHeight: document.documentElement.clientHeight, //实时屏幕高度
      displayNumb: true, //显示编号
      laneUrlList: laneImage, // 逆向车道
      activeTab: "direction0",
      //逆向预览列表
      previewLaneUrlList: [
        // require("@/assets/image/lane/2duan.png"),
        // require("@/assets/image/lane/2zhong.png"),
        // require("@/assets/image/lane/2chang.png"),
        // require("@/assets/image/lane/3duan.jpg"),
        // require("@/assets/image/lane/3zhong.jpg"),
        // require("@/assets/image/lane/3chang.jpg"),
        // require("@/assets/image/lane/4duan.png"),
        // require("@/assets/image/lane/4zhong.png"),
        // require("@/assets/image/lane/4chang.png"),
        // require("@/assets/image/lane/4chang+.png"),

        // require("@/assets/image/lane/2duanz.png"),
        // require("@/assets/image/lane/2zhongz.png"),
        // require("@/assets/image/lane/2changz.png"),
        // require("@/assets/image/lane/3duanz.jpg"),
        // require("@/assets/image/lane/3zhongz.jpg"),
        // require("@/assets/image/lane/3changz.jpg"),
        // require("@/assets/image/lane/4duanz.jpg"),
        // require("@/assets/image/lane/4zhongz.jpg"),
        // require("@/assets/image/lane/4changz.jpg"),
        require("@/assets/image/lane/3blue_duan.png"),
        require("@/assets/image/lane/3blue_chang.png"),
        require("@/assets/image/lane/fenghuangshan.png"),
        require("@/assets/image/lane/mjy.png"),
        require("@/assets/image/lane/fhs.png"),
      ],
      //当前隧道
      selectedTunnel: {
        id: "",
        name: "",
        lane: {},
      },
      upList: [],
      downList: [],
      leftDirection: "", //左侧出口方向
      rightDirection: "", //右侧出口方向
      //验证
      rules: {
        direction: [
          {
            required: true,
            message: "请选择设备方向",
            trigger: "change",
          },
        ],
      },
      paramFrom: {
        eqList: [],
        direction: "1",
      },
      eqTypeList: [],
      EnvironmentList: [],
      equipmentList: [],
      laneRadio: "", //标记选择的车道
      equipmentTypeflag: "",
      Environmentflag: 0,
      top: 0,
      left: 0,
      rowStyle: {
        //table样式
        "background-color": "#304156",
        color: "#C0CCDA",
      },
      // equipmentUrlList: defaultIcon, //设备类型默认图标（灵活性差，后期最好在类型管理中添加默认图标）
      selectedIconList: [], //全部拖动的图标
      svg: {},
      deleteObj: "",
      deleteObjs: "",
      direction: 0,
      deleteIndex: 0,
      loading: true,
      title: "",
      laneVisible: false,
      equipmentVisible: false,
      EnvironmentVisible: false,
      deleteVisible: false,
      paramVisible: false,
      saveLoading: false,
      //环境变量临时扩大数组
      Clist: [],
      pageXimage: 0,
      pageYimage: 0,
      scroll:'',
      svgScrollLeft:0,
      //设备类型集合
      deviceEqTypeList:"",
      deviceEqType:"0",
    };
  },
  created: function () {
    // console.log("我被穿件了");
    this.saveLoading = true;
    img = [];
    let lane = this.getLanUrl(this.$route.query.lane);
    this.selectedTunnel = {
      id: this.$route.query.id,
      name: this.$route.query.name,
      lane: lane,
    };
    // console.log(this.selectedTunnel, "this.selectedTunnel");
    this.selectEquipmentType();
    this.selectEnvironment();
    this.getTunnels(this.selectedTunnel.id);
    // debugger


  },
  watch: {
    deleteVisible(value) {
      // debugger
      if (value) {
        document.body.addEventListener("click", this.closeMenu);
      } else {
        document.body.removeEventListener("click", this.closeMenu);
      }
    },
    displayNumb(value) {
      // console.log(value, "value");
      this.displayControl(value);
    },
  },
  mounted: function () {
    var that = this;
    that.svg = Snap("#svg");
    //窗口重置
    window.onresize = () => {
      return (() => {
        this.windowHeight = document.documentElement.clientHeight;
      })();
    };
    // 横向滚动 获取滚动的长度 好定位删除提示的位置
    let params = document.getElementById("svgRow")
    params.addEventListener("scroll",this.dataScroll);
    //鼠标右键
    window.oncontextmenu = function (e) {
      // debugger
      e.preventDefault(); //取消默认右键
      if (e.target.localName == "image") {
        // debugger
        let letftWidth = 105;
        that.left = e.offsetX + 30;
        that.top = e.y - 90;

        that.deleteVisible = true;
        that.deleteObj = e.target.parentElement.snap;
      }
    };
    //鼠标拖动
    window.ondrag = function (e) {
      console.log(e)
      let oDiv = document.getElementById("imageId");
      this.pageXimage = e.pageX;
      this.pageYimage = e.pageY;
      if (
        e.pageY - oDiv.getBoundingClientRect().top < 5 ||
        e.pageX - oDiv.getBoundingClientRect().left < 5 ||
        oDiv.clientWidth + oDiv.getBoundingClientRect().left - e.pageX < 5 ||
        oDiv.clientHeight + oDiv.getBoundingClientRect().top - e.pageY < 5
      ) {
        let num = "";
        let selectedIconLists = JSON.parse(
          JSON.stringify(that.selectedIconList)
        );
        that.deleteObjs = e.target.snap;
        for (let i = 0; i < img.length; i++) {
          if (img[i].id == that.deleteObjs) {
            img[i].remove();
            img.splice(i, 1);
            that.selectedIconList.splice(i, 1);
            num = i;
          }
        }
        let selectedIcon = that.eqTypeList.find(
          (item) => item.typeId == selectedIconLists[num].eqType
        );
        that.getEquipment(selectedIconLists[num], selectedIcon);
      }
    };

  },

  methods: {
    selectEqType(e){
      console.log(this.deviceEqTypeList)
      console.log(this.deviceEqType)
      let value = e
      let lable = ""
      for (let i = 0; i < this.deviceEqTypeList.length; i++) {
        if(this.deviceEqTypeList[i].raw.dictValue==e){
          lable = this.deviceEqTypeList[i].label
        }
      }

      var val = value.toString();
      console.log( img)
      for (let i = 0; i <  img.length; i++) {
        img[i].attr({
          opacity: 0  // 设置透明度为0，使图像不可见
        });
      }
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
              img[i].attr({
                opacity: 0  // 设置透明度为0，使图像不可见
              });

              // 没有eqType属性的图片依旧显示 例如：隧道名称
              if (
                this.selectedIconList[i].eqType == null ||
                this.selectedIconList[i].eqType == undefined
              ) {
                this.selectedIconList[i].display = true;
                img[i].attr({
                  opacity: 1  // 设置透明度为0，使图像不可见
                });
              }
            }
          }
          for (let i = 0; i < typeIndex.length; i++) {
            this.selectedIconList[typeIndex[i]].display = true;
            img[typeIndex[i]].attr({
              opacity: 1  // 设置透明度为0，使图像不可见
            });
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            this.selectedIconList[i].display = false;
            img[i].attr({
              opacity: 0  // 设置透明度为0，使图像不可见
            });
          }
        }
      });
      // debugger
    },
    // 获取滚动条横向滚动的长度
    dataScroll(){
      this.svgScrollLeft = document.getElementById("svgRow").scrollLeft
    },
    /* 查询设备类型*/
    selectEquipmentType() {
      var that = this;
      listType("").then((response) => {
        that.planRoadmapUrl(response.rows);
        // console.log(response.rows, "response");
      });
    },
    /* 查询环境配置*/
    selectEnvironment() {
      var that = this;
      listConfiguration("").then((response) => {
        response.rows.forEach((e) => {
          e.iconWidth = e.width;
          e.iconHeight = e.height;
          e.eqId = e.id + e.sdName;
          e.pile = "";
        });
        // debugger
        var newDict = that.dict.type.sd_sys_name;
        if (that.selectedTunnel.name != "胡山隧道") {
          that.deviceEqTypeList = newDict.slice(0, 8);
        } else if (that.selectedTunnel.name == "胡山隧道") {
          that.deviceEqTypeList = this.dict.type.sd_sys_name;
        }
        // console.log(response.rows, "接口返回数据");
        // console.log(response.rows.length, "接口返回数据");
        // console.log(that.dict.type.environment, "字典数据");
        // console.log(that.dict.type.environment.length, "字典数据长度");
        if (that.dict.type.environment.length != response.rows.length) {
          for (
            let index = 0;
            index < that.dict.type.environment.length;
            index++
          ) {
            let obj = response.rows.filter(
              (e) =>
                e.environmentType == that.dict.type.environment[index].value
            );
            // console.log(obj, "符合条件的obj");
            if (obj == undefined) {
              // console.log(obj, "666666");
              let one = [
                {
                  url: [],
                },
              ];
              that.Clist.push(one);
            } else {
              that.Clist.push(obj);
            }
          }
        }
        // console.log(that.Clist, "that.Clist");
        that.planRoadmapEnvironmentUrl(that.Clist);
      });
    },
    async planRoadmapEnvironmentUrl(list) {
      var that = this;
      for (let index = 0; index < list.length; index++) {
        for (let i = 0; i < list[index].length; i++) {
          if (list[index][i].iFileList != null) {
            let imgUrl = [];
            for (let j = 0; j < list[index][i].iFileList.length; j++) {
              // let url = await that.picture(list[index][i].iFileList[j].url);
              let url = list[index][i].iFileList[j].url;
              imgUrl.push(url);
            }
            list[index][i].url = imgUrl;
          } else {
            list[index][i].url = [];
          }
        }
      }
      that.EnvironmentList = list;
      console.log(that.EnvironmentList, "that.EnvironmentList");
      this.saveLoading = false;
    },
    async planRoadmapUrl(list) {
      // debugger
      var that = this;
      // console.log(list, "接口返回数据");
      for (let i = 0; i < list.length; i++) {
        if (list[i].iFileList != null) {
          let imgUrl = [];
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let url = await that.picture(list[i].iFileList[j].url);
            let url = list[i].iFileList[j].url;
            imgUrl.push(url);
          }
          list[i].url = imgUrl;
        } else {
          list[i].url = [];
        }
      }
      that.eqTypeList = list;
      // console.log(list, "list");
      this.saveLoading = false;
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

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[0];
      //多车道配置 短中长
      // for (let i = 0; i < this.laneUrlList.length; i++) {
      //   if (this.laneUrlList[i].num == num) {
      //     lane = this.laneUrlList[i];
      //     break;
      //   }
      // }
      return lane;
    },
    /* 查询配置信息*/
    getTunnels(param) {
      getTunnels(param).then((response) => {
        this.loading = false;
        let res = response.data.storeConfigure;
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          // debugger
          this.selectedIconList = res.eqList;
          console.log(this.selectedIconList, "this.selectedIconList");
          listType("").then((response) => {
            this.drawSvg(response.rows);
          });

          if (res.upList != undefined) {
            // this.upList = res.upList
          }
          if (res.downList != undefined) {
            // this.downList = res.downList
          }
          if (res.leftDirection != undefined) {
            this.leftDirection = res.leftDirection;
          }
          if (res.rightDirection != undefined) {
            this.rightDirection = res.rightDirection;
          }
          let laneId = res.lane;
          for (let i = 0; i < this.laneUrlList.length; i++) {
            if (this.laneUrlList[i].id == laneId) {
              console.log(this.laneUrlList[i],"this.laneUrlList[i]")
              this.selectedTunnel.lane = this.laneUrlList[i];
              this.activeTab = "direction" + this.selectedTunnel.lane.direction;
              this.laneRadio = i;
            }
          }
        } else {
          this.selectedIconList = [];
        }
      });
    },

    /* 绘制svg图形*/
    drawSvg(eqTypeList) {
      var that = this;
      let list = that.selectedIconList;
      // console.log("that.selectedIconList", that.selectedIconList);
      // debugger;
      for (let i = 0; i < list.length; i++) {
        // console.log("list.length", list.length);
        var iconWidth = 0;
        var iconHeight = 0;
        if (list[i].width != "") {
          iconWidth = list[i].width;
          iconHeight = list[i].height;
        }
        for (var j = 0; j < eqTypeList.length; j++) {
          if (eqTypeList[j].typeId == list[i].eqType) {
            iconWidth = Number(eqTypeList[j].iconWidth);
            iconHeight = Number(eqTypeList[j].iconHeight);
            break;
          }
        }
        // console.log("list[i]", list[i]);
        //矩形框
        if (list[i].url.length > 1) {
          // console.log('矩形框')
          //设备图标
          let img1 = that.svg.paper.image(
            list[i].url[0],
            list[i].position.left,
            list[i].position.top,
            iconWidth,
            iconHeight
          );
          let img2 = that.svg.paper.image(
            list[i].url[1],
            list[i].position.left + 14,
            list[i].position.top,
            iconWidth,
            iconHeight
          );
          // console.log(list[i].pile, 'list[i].pile')
          if (list[i].pile != "" && list[i].pile) {
            // console.log("list[i].pile");
            let r = that.svg.paper
              .rect(
                list[i].position.left - 20,
                list[i].position.top + iconHeight,
                80,
                18,
                2
              )
              .attr({
                fill: "rgba(84, 85, 89, 0.4)",
              });

            //桩号
            // console.log(
            //   list[i],
            //   list[i].pile.length,
            //   list[i].position.left - (2 * list[i].pile.length - 20),
            //   "list[i].pile"
            // );
            let t = "";
            if (list[i].pile) {
              if (list[i].pile.length > 10) {
                t = that.svg.paper
                  .text(
                    list[i].position.left - 10,
                    list[i].position.top + iconHeight + 14,
                    list[i].pile
                  )
                  .attr({
                    fill: "#a2a2a3",
                    "font-size": 12,
                  });
              } else if (list[i].pile.length <= 10) {
                t = that.svg.paper
                  .text(
                    list[i].position.left - 10,
                    list[i].position.top + iconHeight + 14,
                    list[i].pile
                  )
                  .attr({
                    fill: "#a2a2a3",
                    "font-size": 12,
                  });
              }
            }

            img[i] = that.svg.paper.g(r, t, img2, img1).attr({
              class: "mydrags",
            });
          } else {
            img[i] = that.svg.paper.g(img2, img1).attr({
              class: "mydrags",
            });
          }
        } else {
          // console.log(iconWidth, iconWidth < 20, "iconWidth");
          // let num = iconWidth<30?(iconWidth<25?10:2):-8;
          // let num = 0;
          //添加图像
          var img3 = that.svg.paper.image(
            list[i].url[0],
            list[i].position.left, //此处增加+ iconWidth
            list[i].position.top,
            iconWidth,
            iconHeight
          );
          if (list[i].pile != "" && list[i].pile) {
            let num2 = 0;
            if (list[i].eqType == 21) {
              //紧急电话
              num2 = 12;
            } else if (list[i].eqType == 19) {
              //CO/VI检测器
              num2 = -8;
            } else if (list[i].eqType == 34) {
              //固定摄像机（枪机
              num2 = 20;
            }
            let r = that.svg.paper
              .rect(
                list[i].position.left - 20,
                list[i].position.top + iconHeight + 2,
                80,
                18,
                2
              )
              .attr({
                fill: "rgba(84, 85, 89, 0.4)",
              });
            //桩号
            let t = that.svg.paper
              .text(
                list[i].position.left - 10,
                list[i].position.top + iconHeight + 16,
                list[i].pile
              )
              .attr({
                fill: "#a2a2a3",
                "font-size": 12,
              });

            t.attr({
              x: list[i].position.left - list[i].pile.length - 4,
            });
            // console.log(list[i], r, "list[i]");
            img[i] = that.svg.paper.g(r, t, img3).attr({
              class: "mydrags",
            });
            // console.log("正常", img[i]);
            // if(list[i].eqType == 5){
            //   img[i] = that.svg.paper.g(r - 10, t, img3).attr({
            //     class: "mydrags",
            //   });
            // }
          } else {
            img[i] = that.svg.paper.g(img3).attr({
              class: "mydrags",
            });
            // console.log("错误", img[i]);
          }
        }
        // console.log("所有", img[i]);
        img[i].drag();
      }

      // 添加辅助线
      that.$nextTick(() => {
        that.auxiliaryLine();
      });
    },

    /*点击保存 */
    preserve() {
      // if (this.leftDirection == "" || this.rightDirection == "") {
      //   this.$alert("请配置完整的隧道方向！", "提示", {
      //     confirmButtonText: "确定",
      //     type: "warning",
      //     callback: (action) => {},
      //   });
      // } else {
      this.saveLoading = true;
      let eqList = [];
      //遍历设备，获取位置
      // console.log(this.selectedIconList, "this.selectedIconList", img);
      for (let i = 0; i < this.selectedIconList.length; i++) {
        if (this.selectedIconList[i].eqType == 12) {
          // console.log(this.selectedIconList[i], "111111111w");
          this.selectedIconList[i].pileNum = this.selectedIconList[
            i
          ].pile.replace(/[^\d.]/g, "");
          // console.log(this.selectedIconList[i].pileNum);
        }
        if (JSON.stringify(this.selectedIconList[i]) != "{}") {
          // console.log(2222222);
          if (img[i]) {
            // console.log(3333333333);
            this.selectedIconList[i].position = {
              left:
                img[i].attr("transform").localMatrix.e +
                this.selectedIconList[i].position.left,
              top:
                img[i].attr("transform").localMatrix.f +
                this.selectedIconList[i].position.top,
            };
            eqList.push(this.selectedIconList[i]);
          } else {
            eqList.push(this.selectedIconList[i]);
          }
        }
      }

      //遍历车道上方参数，获取位置
      for (let j = 0; j < this.upList.length; j++) {
        let upObj = document.getElementById("drag-up" + j);
        if (upObj != null) {
          let upTop = upObj.offsetTop;
          let upLeft = upObj.offsetLeft;
          this.upList[j].position = {
            top: upTop,
            left: upLeft,
          };
        }
      }
      //遍历车道下方参数，获取位置
      for (let k = 0; k < this.downList.length; k++) {
        let downObj = document.getElementById("drag-down" + k);
        if (downObj != null) {
          let downTop = downObj.offsetTop;
          let downLeft = downObj.offsetLeft;
          this.downList[k].position = {
            top: downTop,
            left: downLeft,
          };
        }
      }
      let configData = {
        eqList: eqList,
        upList: this.upList,
        downList: this.downList,
        lane: this.selectedTunnel.lane.id,
        direction: this.selectedTunnel.lane.direction,
        leftDirection: this.leftDirection,
        rightDirection: this.rightDirection,
      };
      let param = {
        tunnelId: this.selectedTunnel.id,
        storeConfigure: JSON.stringify(configData),
      };
      // console.log(param, configData, "configData");
      configData.eqList.forEach((v) => {
        if (v.eqType == 3) {
          // console.log(v, "交通信号灯");
        }
      });
      updateTunnels(param).then((response) => {
        this.saveLoading = false;
        if (response.code === 200) {
          this.$store
            .dispatch("tagsView/delView", this.$route)
            .then(({ visitedViews }) => {
              this.$router.back();
            });
        }
      });
      // }
    },
    /* 配置清空*/
    reset() {
      this.selectedIconList = [];
      this.upList = [];
      this.downList = [];
      for (let i = 0; i < img.length; i++) {
        img[i].remove();
      }
      img = [];
    },
    /* 控制桩号显示*/
    displayControl(value) {
      let list = this.selectedIconList;
      //不显示
      if (value == false) {
        for (let i = 0; i < list.length; i++) {
          if(img[i][0].node.nodeName != 'image'){
            img[i][0].attr({
              width: 0,
              height: 0,
            });
            if (img[i][1]) {
              img[i][1].attr({
                text: "",
              });
            }
          }
        }
      } else {
        //显示
        for (let i = 0; i < list.length; i++) {
          if(img[i][0].node.nodeName != 'image'){
            img[i][0].attr({
              width: 80,
              height: 18,
            });
            if (img[i][1]) {
              img[i][1].attr({
                text: list[i].pile,
              });
            }
          }
        }
      }
    },
    onActivated(key) {},
    onDragging(key) {},
    onDropped(key) {},
    /* 参数右键（删除）*/
    openMenu(direction, index, e) {
      const menuMinWidth = 105;
      const offsetLeft = this.$el.getBoundingClientRect().left;
      const offsetWidth = this.$el.offsetWidth;
      const maxLeft = offsetWidth - menuMinWidth;
      const left = e.clientX - offsetLeft + 20;
      if (left > maxLeft) {
        this.left = maxLeft;
      } else {
        this.left = left;
      }
      this.top = e.clientY;
      this.deleteVisible = true;
      this.direction = direction;
      this.deleteIndex = index;
    },
    /* 点击删除*/
    deleteImage() {
      // debugger
      // console.log("我右键删除了", this.direction, img);
      if (this.direction == 1) {
        this.upList.splice(this.deleteIndex, 1, {});
      } else if (this.direction == 2) {
        this.downList.splice(this.deleteIndex, 1, {});
      } else {
        for (let i = 0; i < img.length; i++) {
          if (img[i].id == this.deleteObj) {
            img[i].remove();
            img.splice(i, 1);
            this.selectedIconList.splice(i, 1);
          }
        }
        // this.equipmentList = this.addMask(response.rows)
      }
      // console.log("我右键删除了11", img, this.selectedIconList);
      this.deleteVisible = false;
      this.direction = 0;
    },

    /* 关闭右键菜单*/
    closeMenu() {
      this.deleteVisible = false;
    },

    /* 打开车道弹框*/
    openLaneDialog() {
      this.title = "更换车道";
      this.laneVisible = true;
    },
    submitLane() {
      this.laneVisible = false;
      this.selectedTunnel.lane = this.laneUrlList[this.laneRadio];
      // this.selectedIconList = []
    },
    /* 打开设备弹框*/
    openEquipmentDialog(id, index, type) {
      // console.log(id, index, type, "id, index, type");
      this.title = "选择设备";
      this.equipmentList = [];
      if (type == "type") {
        this.equipmentTypeflag = index;
        const param = {
          eqTunnelId: this.selectedTunnel.id,
          eqType: id,
        };
        this.selectEquipment(param);
      } else if (type == "configuration") {
        this.Environmentflag = index;
        const param = {
          eqTunnelId: this.selectedTunnel.id,
          environmentType: id,
        };
        this.selectConfiguration(param);
      }
    },
    /* 查询环境配置*/
    selectConfiguration(param) {
      var that = this;
      that.EnvironmentList.forEach((e) => {
        e = that.addMask(e);
      });
      // console.log(this.EnvironmentList, "处理后的数组");
      // console.log(this.EnvironmentLis.length,'判断的长度')
      if (this.EnvironmentList.length > 0) {
        this.EnvironmentVisible = true;
      }
    },
    /* 查询设备*/
    selectEquipment(param) {
      var that = this;
      newListDevices(param).then((response) => {
        that.equipmentList = that.addMask(response.rows);
        // console.log(that.equipmentList,"that.equipmentList")
        if (this.equipmentList.length > 0) {
          this.equipmentVisible = true;
        }
      });
      // 关联plc的设备
      /* if(!(param.eqType == 9 || param.eqType == 8 || param.eqType == 7 || param.eqType == 21)){
          // 根据隧道和设备类型区分
          listDevices(param).then(response => {
            that.equipmentList = that.addMask(response.rows)
            if (this.equipmentList.length > 0) {
              this.equipmentVisible = true
            }
          });
        }
		// 未关联plc的设备
        else{
          // 当前隧道的所有供电支路
          listLipowerDevice({"tunnelId":param.eqTunnelId,"eqType":param.eqType}).then(response => {
            debugger
            that.equipmentList = that.addMask(response.rows);
            if (this.equipmentList.length > 0) {
              this.equipmentVisible = true;
            }
          });
        } */
    },
    /* 选择设备*/
    getEquipment(item, eqType) {
      console.log(item, eqType, "选择设备");
      console.log(this.selectedIconList);
      // debugger
      var url = eqType.url;
      var iconWidth = Number(eqType.iconWidth);
      var iconHeight = Number(eqType.iconHeight);
      var that = this;
      let sign = this.eqExist(item);
      if (sign == false) {
        (item.display = true),
          (item.url = url),
          (item.position = {
            left: 0,
            top: 0,
          });
        this.selectedIconList.push(item);
        // console.log(this.selectedIconList, "this.selectedIconList");
        that.equipmentList = that.addMask(that.equipmentList);
        if (item.pile != "" && item.pile) {
          // 桩号框以及框内汉字的位置是由设备位置决定的
          // 绘制桩号框
          let r = that.svg.paper.rect(-20, iconHeight + 2, 80, 18, 2).attr({
            fill: "rgba(84, 85, 89, 0.4)",
          });
          // 绘制桩号
          let t = that.svg.paper.text(-18, iconHeight + 16, item.pile).attr({
            fill: "#a2a2a3",
            "font-size": 12,
          });
          if (url.length > 1) {
            let img1 = null;
            let img2 = null;
            if (item.eqType == 2 || item.eqType == 1) {
              img1 = this.svg.paper.image(url[0], 0, 0, iconWidth, iconHeight);
              img2 = this.svg.paper.image(url[1], 14, 0, iconWidth, iconHeight);
            } else {
              img1 = this.svg.paper.image(
                url[0],
                10 + iconWidth,
                0,
                iconWidth,
                iconHeight
              );
              img2 = this.svg.paper.image(
                url[1],
                10 + iconWidth,
                0,
                iconWidth,
                iconHeight
              );
            }
            img.push(
              this.svg.paper.g(r, t, img2, img1).drag().attr({
                id: item.eqId,
              })
            );
          } else {
            var img3;
            t.attr({
              x: -20,
            });

            // if (item.eqType == 7 || item.eqType == 117) {
            //   // let num = iconWidth>=29?-24:0;
            //   // 加强照明  电光标志
            //   img3 = this.svg.paper
            //     .image(url, 10, 0, iconWidth, iconHeight)
            //     .attr({
            //       id: item.eqId,
            //     });
            // } else if (item.eqType == 21) {
            //   console.log(1111111111111111);
            //   // "紧急电话"
            //   img3 = this.svg.paper
            //     .image(url, iconWidth + 18, 0, iconWidth, iconHeight)
            //     .attr({
            //       id: item.eqId,
            //     });
            // } else if (item.eqType == 25) {
            //   console.log(1111111111111111);
            //   // "抓拍摄像机"
            //   img3 = this.svg.paper
            //     .image(url, iconWidth + 12, 0, iconWidth, iconHeight)
            //     .attr({
            //       id: item.eqId,
            //     });
            // } else {
            //   console.log(222222222222);
            //   img3 = this.svg.paper
            //     .image(
            //       url,
            //       25 + iconWidth + iconWidth >= 29 ? 24 : 0,
            //       0,
            //       iconWidth,
            //       iconHeight
            //     )
            //     .attr({
            //       id: item.eqId,
            //     });
            // }

            img3 = this.svg.paper.image(url, 0, 0, iconWidth, iconHeight).attr({
              id: item.eqId,
            });

            img.push(
              this.svg.paper.g(r, t, img3).drag().attr({
                id: item.eqId,
              })
            );
          }
        } else {
          if (url.length > 1) {
            let img1 = this.svg.paper.image(
              url[0],
              0,
              0,
              iconWidth,
              iconHeight
            );
            let img2 = this.svg.paper.image(
              url[1],
              14,
              0,
              iconWidth,
              iconHeight
            );
            img.push(
              this.svg.paper.g(img2, img1).drag().attr({
                id: item.eqId,
              })
            );
          } else {
            var img3;

            img3 = this.svg.paper
              .image(url[0], 0, 0, iconWidth, iconHeight)
              .attr({
                id: item.eqId,
              });
            img.push(
              this.svg.paper.g(img3).drag().attr({
                id: item.eqId,
              })
            );
          }
        }
        // 车指等多个显示设备
        // if (url.length > 1) {
        //   let img1 = this.svg.paper.image(url[0], 10, 0, iconWidth, iconHeight);
        //   let img2 = this.svg.paper.image(
        //     url[1],
        //     10 + iconWidth,
        //     0,
        //     iconWidth,
        //     iconHeight
        //   );
        //   if (item.pile == "") {
        //     img.push(
        //       this.svg.paper.g(img2, img1).drag().attr({
        //         id: item.eqId,
        //       })
        //     );
        //   } else {
        //     img.push(
        //       this.svg.paper.g(r, t, img2, img1).drag().attr({
        //         id: item.eqId,
        //       })
        //     );
        //   }
        // }
        // // 风向等单个显示设备
        // else {
        //   var img3;
        //   t.attr({
        //     x: 15,
        //   });
        //   img3 = this.svg.paper.image(url, 25, 0, iconWidth, iconHeight).attr({
        //     id: item.eqId,
        //   });
        //   if (item.pile == "") {
        //          r = null;
        //     t = null;
        //     img.push(
        //       this.svg.paper.g(img3).drag().attr({
        //         id: item.eqId,
        //       })
        //     );
        //   } else {
        //     img.push(
        //       this.svg.paper.g(r, t, img3).drag().attr({
        //         id: item.eqId,
        //       })
        //     );
        //   }
        // }

        // 给图片添加辅助线
        that.$nextTick(() => {
          that.auxiliaryLine();
        });
      }
    },
    /* 提示*/
    tips() {
      this.$message({
        message: "请给设备类型添加默认图标 ！",
        type: "warning",
        customClass: "tunnelConfig-msg",
      });
    },
    equipmentSelectionChange(val) {
      this.paramFrom.eqList = [];
      for (let i = 0; i < val.length; i++) {
        this.paramFrom.eqList.push(val[i]);
      }
    },
    /* 设备是否被选择*/
    eqExist(equipment) {
      let exist = false;
      for (let i = 0; i < this.selectedIconList.length; i++) {
        if (this.selectedIconList[i].eqId == equipment.eqId) {
          exist = true;
          break;
        }
      }
      // console.log(exist, "exist");
      return exist;
    },
    /* 选过的设备加遮罩*/
    addMask(list) {
      for (let i = 0; i < list.length; i++) {
        let isExist = this.eqExist(list[i]);
        if (isExist == true) {
          list[i].exist = true;
        } else {
          list[i].exist = false;
        }
      }
      return list;
    },
    /* 选择风速等传感器设备后*/
    submitParam(formName) {
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          that.paramVisible = false;
          for (let i = 0; i < that.paramFrom.eqList.length; i++) {
            let typeName = that.paramFrom.eqList[i].typeName.typeName;
            let pile = that.paramFrom.eqList[i].pile;
            let obj = that.splicing(typeName, pile);
            if (that.paramFrom.direction == 1) {
              let size = that.listSize(that.upList);
              let exist = that.isExist(that.paramFrom.eqList[i].eqId);
              if (exist == false) {
                that.upList.push({
                  id: that.paramFrom.eqList[i].eqId,
                  name: obj.name,
                  unit: obj.unit,
                  value: "",
                  position: {
                    left: 150 * size,
                    top: 0,
                  },
                });
              }
            } else if (that.paramFrom.direction == 2) {
              let existOne = that.isExist(that.paramFrom.eqList[i].eqId);
              let sizeOne = that.listSize(that.downList);
              if (existOne == false) {
                that.downList.push({
                  id: that.paramFrom.eqList[i].eqId,
                  name: obj.name,
                  unit: obj.unit,
                  value: "",
                  position: {
                    left: 150 * sizeOne,
                    top: 0,
                  },
                });
              }
            }
          }
        } else {
          return false;
        }
      });
    },
    /* 计算非空长度*/
    listSize(list) {
      let size = 0;
      for (let i = 0; i < list.length; i++) {
        if (list.id != undefined) {
          size++;
        }
      }
      return size;
    },
    isExist(eqId) {
      let exist = false;
      let length = this.upList.length;
      for (let k = 0; k < length; k++) {
        if (this.upList[k] != {} && this.upList[k].id == eqId) {
          exist = true;
          return exist;
          break;
        }
      }
      let lengthOne = this.downList.length;
      for (let k = 0; k < lengthOne; k++) {
        if (this.downList[k] != {} && this.downList[k].id == eqId) {
          exist = true;
          return exist;
          break;
        }
      }
      return exist;
    },
    splicing(typeName, pile) {
      let name = "";
      let unit = "";
      if (pile == null || pile == undefined) {
        pile = "";
      }
      if (typeName.indexOf("风速") != -1) {
        name = "风速(" + pile + ")";
        unit = "m/s";
      } else if (typeName.indexOf("风向") != -1) {
        name = "风向(" + pile + ")";
        unit = "";
      } else if (typeName.indexOf("CO") != -1) {
        name = "CO(" + pile + ")";
        unit = "ppm";
      } else if (typeName.indexOf("能见度") != -1) {
        name = "能见度(" + pile + ")";
        unit = "m";
      } else if (typeName.indexOf("洞内亮度") != -1) {
        name = "洞内亮度(" + pile + ")";
        unit = "cd/㎡";
      } else if (typeName.indexOf("洞外亮度") != -1) {
        name = "洞外亮度(" + pile + ")";
        unit = "cd/㎡";
      } else if (typeName.indexOf("水池液位") != -1) {
        name = "水池液位(" + pile + ")";
        unit = "m³";
      }
      return {
        name: name,
        unit: unit,
      };
    },
    /* 跳转至策略页面*/
    strategyPage() {
      this.$router.push("/strategy/index");
    },
    /* 跳转至操作日志页面*/
    operationLogPage() {
      this.$router.push("/operationLog/index");
    },
    cancel() {
      this.equipmentVisible = false;
      this.EnvironmentVisible = false;
      this.laneVisible = false;
      this.paramVisible = false;
    },
    handleBack() {
      this.$router.push({
        path: "/dev/sd/tunnel",
      });
    },
    // 辅助线
    auxiliaryLine() {
      // debugger;
      // debugger
      let that = this;
      if (!$("#svg g")) return;
      var MIN_DISTANCE = 1; //捕获的最小距离

      var guides = []; // 没有可用的引导

      var innerOffsetX, innerOffsetY;

      $("#svg g").draggable({
        start: function (event, ui) {
          // debugger
          guides = $.map($("#svg g").not(this), computeGuidesForElement);
          //鼠标距离选中元素最左边和最上边的距离
          for (const k in event.target) {
            if (event.target[k].uiDraggable) {
              innerOffsetX = Math.round(
                event.target[k].uiDraggable.offset.click.left
              );
              innerOffsetY = Math.round(
                event.target[k].uiDraggable.offset.click.top
              );
              break;
            }
          }
        },

        /**

         * 参数说明

         * @param event

         * @param ui

         *

         *  event事件的

         * offsetX：

         * outerwidth： 属性是一个只读的整数，声明了整个窗口的宽度。

         *  outerheight： 属性是一个只读的整数，声明了整个窗口的高度。

         */

        drag: function (event, ui) {
          // console.log(event)
          // debugger
          //迭代所有的guids，记住最近的h和v guids

          var guideV,
            guideH,
            distV = MIN_DISTANCE + 1,
            distH = MIN_DISTANCE + 1,
            offsetV,
            offsetH;

          var chosenGuides = {
            top: {
              dist: MIN_DISTANCE + 1,
            },
            left: {
              dist: MIN_DISTANCE + 1,
            },
          };

          var $t = $(this);

          //pageX、pageY：文档坐标x、y ;
          var pos = {
            top: event.pageY,
            left: event.pageX,
          };

          //outerHeight、outerWidth：整个浏览器的高度、宽度

          var w = event.pageX - 1; //改

          var h = event.pageY - 1;

          var elemGuides = computeGuidesForElement(null, pos, w, h);

          // 旁边那个的
          $.each(guides, function (i, guide) {
            // 选择了那个的
            $.each(elemGuides, function (i, elemGuide) {
              // 高对高 航对航
              if (guide.type == elemGuide.type) {
                var prop = guide.type == "h" ? "top" : "left";

                var d = Math.abs(elemGuide[prop] - guide[prop]);

                if (d < chosenGuides[prop].dist) {
                  // debugger
                  chosenGuides[prop].dist = d;

                  chosenGuides[prop].offset = elemGuide[prop] - pos[prop];
                  let guide1 = {
                    left: event.pageX + 32,
                    top: event.pageY + 35,
                    type: "h",
                  };
                  event.toElement.parentNode;
                  // console.log(event.toElement.parentNode);

                  let style = window.getComputedStyle(
                    event.toElement.parentNode,
                    null
                  );
                  let paddingL = parseFloat(style.getPropertyValue("left")); //获取左侧内边距
                  console.log(paddingL)
                  let paddingtop = parseFloat(style.getPropertyValue("top")); //获取左侧内边距
                  console.log(paddingtop)
                  let svgs = document.getElementById("svgRow");
                  console.log(svgs)
                  let svgss = document.querySelector(".config-content");
                  console.log(svgss)
                  let svgeimage = document.querySelector(".el-image");
                  console.log(svgeimage)
                  let stylea = window.getComputedStyle(svgs, null);
                  console.log(stylea)
                  let styleas = window.getComputedStyle(svgss, null);
                  console.log(styleas)
                  let styleas1 = window.getComputedStyle(svgeimage, null);
                  console.log(styleas1)

                  let paddingLa = parseFloat(
                    stylea.getPropertyValue("padding-top")
                  ); //获取左侧内边距
                  console.log(paddingLa)
                  let paddingLa1 = parseFloat(
                    styleas.getPropertyValue("width")
                  ); //获取左侧内边距
                  console.log(paddingLa1)
                  let paddingLa2 = parseFloat(
                    styleas1.getPropertyValue("width")
                  ); //获取左侧内边距
                  console.log(paddingLa2)

                  guide1.left = paddingL + (paddingLa1 - paddingLa2) / 2 - 16;
                  guide1.top = paddingtop + paddingLa;
                  chosenGuides[prop].guide = guide1;
                }
              }
            });
          });

          // 画布与窗口的距离
          let left = event.pageX - event.offsetX;
          let top = event.pageY - event.offsetY + 3; // 上部辅助线稍微有偏差，所以多加了3(线往上偏移)，可以微调
          if (chosenGuides.top.dist <= MIN_DISTANCE) {
            $("#guide-h")
              .css("top", chosenGuides.top.guide.top - top)
              .show();
            // $("#guide-h1")
            //   .css("top", chosenGuides.top.guide.top - top +event.toElement.height.animVal.value+1)
            //   .show();
            // ui.position.top = chosenGuides.top.guide.top - 104 - chosenGuides.top.offset;
          } else {
            // debugger
            $("#guide-h").hide();
            $("#guide-h1").hide();
            // ui.position.top = pos.top - 104;
          }

          if (chosenGuides.left.dist <= MIN_DISTANCE) {
            $("#guide-v")
              .css("left", chosenGuides.left.guide.left - left)
              .show();
            // $("#guide-v1")
            //   .css("left", chosenGuides.left.guide.left - left +event.toElement.height.animVal.value+1)
            //   .show();
            /* ui.position.left =
				      chosenGuides.left.guide.left - chosenGuides.left.offset; */
          } else {
            $("#guide-v").hide();
            $("#guide-v1").hide();
            /* ui.position.left = pos.left; */
          }
        },

        stop: function (event, ui) {
          // console.log(event)
          // debugger
          //迭代所有的guids，记住最近的h和v guids

          var guideV,
            guideH,
            distV = MIN_DISTANCE + 1,
            distH = MIN_DISTANCE + 1,
            offsetV,
            offsetH;

          var chosenGuides = {
            top: {
              dist: MIN_DISTANCE + 1,
            },
            left: {
              dist: MIN_DISTANCE + 1,
            },
          };

          var $t = $(this);

          //pageX、pageY：文档坐标x、y ;
          var pos = {
            top: event.pageY,
            left: event.pageX,
          };

          //outerHeight、outerWidth：整个浏览器的高度、宽度

          var w = event.pageX - 1; //改

          var h = event.pageY - 1;

          var elemGuides = computeGuidesForElement(null, pos, w, h);

          // 旁边那个的
          $.each(guides, function (i, guide) {
            // 选择了那个的
            $.each(elemGuides, function (i, elemGuide) {
              // 高对高 航对航
              if (guide.type == elemGuide.type) {
                var prop = guide.type == "h" ? "top" : "left";

                var d = Math.abs(elemGuide[prop] - guide[prop]);

                if (d < chosenGuides[prop].dist) {
                  // debugger
                  chosenGuides[prop].dist = d;

                  chosenGuides[prop].offset = elemGuide[prop] - pos[prop];
                  let guide1 = {
                    left: event.pageX + 32,
                    top: event.pageY + 35,
                    type: "h",
                  };
                  event.toElement.parentNode;
                  // console.log(event.toElement.parentNode);

                  let style = window.getComputedStyle(
                    event.toElement.parentNode,
                    null
                  );
                  let paddingL = parseFloat(style.getPropertyValue("left")); //获取左侧内边距
                  let paddingtop = parseFloat(style.getPropertyValue("top")); //获取左侧内边距
                  // console.log(paddingL);
                  // console.log(paddingtop);
                  // console.log("ddddddddddddddddddddddddddddddd");
                  // let ds = getElementPosition(event.toElement.parentNode)
                  // console.log(ds)
                  let oDiv = document.getElementById("imageId");

                  let svgs = document.getElementById("svgRow");
                  let svgss = document.querySelector(".config-content");
                  let svgeimage = document.querySelector(".el-image");
                  // console.log(svgss);

                  let stylea = window.getComputedStyle(svgs, null);
                  let styleas = window.getComputedStyle(svgss, null);
                  let styleas1 = window.getComputedStyle(svgeimage, null);
                  // console.log(styleas);

                  let paddingLa = parseFloat(
                    stylea.getPropertyValue("padding-top")
                  ); //获取左侧内边距
                  let paddingLa1 = parseFloat(
                    styleas.getPropertyValue("width")
                  ); //获取左侧内边距
                  let paddingLa2 = parseFloat(
                    styleas1.getPropertyValue("width")
                  ); //获取左侧内边距
                  // console.log(paddingLa1 - paddingLa2);
                  // console.log(44444444444444444444444444444444444);
                  // console.log(event.toElement.width.animVal.value);
                  guide1.left = paddingL + (paddingLa1 - paddingLa2) / 2 - 16;
                  guide1.top = paddingtop + paddingLa;
                  chosenGuides[prop].guide = guide1;
                }
              }
            });
          });

          // 画布与窗口的距离
          let left = event.pageX - event.offsetX;
          let top = event.pageY - event.offsetY ; // 上部辅助线稍微有偏差，所以多加了3(线往上偏移)，可以微调
          if (chosenGuides.top.dist <= MIN_DISTANCE) {
            $("#guide-h")
              .css("top", chosenGuides.top.guide.top)
              .show();
            // $("#guide-h1")
            //   .css("top", chosenGuides.top.guide.top - top +event.toElement.height.animVal.value+1)
            //   .show();
            // ui.position.top = chosenGuides.top.guide.top - 104 - chosenGuides.top.offset;
          } else {
            // debugger
            $("#guide-h").hide();
            $("#guide-h1").hide();
            // ui.position.top = pos.top - 104;
          }

          if (chosenGuides.left.dist <= MIN_DISTANCE) {
            $("#guide-v")
              .css("left", chosenGuides.left.guide.left )
              .show();
            // $("#guide-v1")
            //   .css("left", chosenGuides.left.guide.left - left +event.toElement.height.animVal.value+1)
            //   .show();
            /* ui.position.left =
				      chosenGuides.left.guide.left - chosenGuides.left.offset; */
          } else {
            $("#guide-v").hide();
            $("#guide-v1").hide();
            /* ui.position.left = pos.left; */
          }
          $("#guide-v, #guide-h ,#guide-v1 ,#guide-h1").hide();
        },
      });

      function getElementPosition(element) {
        let top = element.offsetTop; //这是获取元素距父元素顶部的距离
        let left = element.offsetLeft;
        var current = element.offsetParent; //这是获取父元素
        while (current !== null) {
          //当它上面有元素时就继续执行
          top += current.offsetTop; //这是获取父元素距它的父元素顶部的距离累加起来
          left += current.offsetLeft;
          current = current.offsetParent; //继续找父元素
        }
        return {
          top,
          left,
        };
      }

      function computeGuidesForElement(elem, pos, w, h) {
        if (elem != null) {
          var $t = $(elem);

          if ($t.length > 0) {
            pos = $t.offset();
            w = $t[0].getBBox().width - 1;
            h = $t[0].getBBox().height - 1;
          }
        }
        return [
          {
            type: "h",
            left: pos.left,
            top: pos.top,
          }, //垂直方向左下对齐线

          {
            type: "h",
            left: pos.left,
            top: pos.top + h,
          },

          {
            type: "v",
            left: pos.left,
            top: pos.top,
          },

          {
            type: "v",
            left: pos.left + w,
            top: pos.top,
          },

          //您可以添加_any_其他指南在这里就好了（如指南10像素单元的左）

          {
            type: "h",
            left: pos.left,
            top: pos.top + h / 2,
          },

          {
            type: "v",
            left: pos.left + w / 2,
            top: pos.top,
          },
        ];
      }
    },
  },
};
</script>

<style lang="scss" scoped>
// .container {
//   width: 100%;
//   background-color: #181b2c;
//   display: flex;
//   flex-direction: column;
//   align-items: center;
// }
// .app-container {
//   background-color: #181b2c !important;
// }

.header {
  color: #ced8e4;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.config-header {
  background-color: #181b2c;
}

.eqType {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin-top: 10px;
}

.flex-row {
  display: flex;
  flex-direction: row;
}

/* 自动换行*/
.wrap {
  word-wrap: break-word;
  word-break: normal;
}

.menu {
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  position: relative;
}

.config-menu {
  // background-color: #25283b;
  padding: 20px 10px 0px 10px;
}

.title-wrap {
  padding-top: 12px;
  margin: 0px;
  white-space: nowrap;
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
  color: #fff;
  background: linear-gradient(177deg, #00aced, #0079db);
  opacity: 0.8;
  color: #fff !important;

  // background-color: #2e354f;
  border: none;
}

/* 鼠标在按钮上悬浮*/
.el-button:hover {
  background: linear-gradient(177deg, #00aced, #0079db);
  opacity: 0.8;
  // border: none;
  color: #fff !important;
  // border: 1px solid #7f8c98;
}

/*自定义primary类型按钮样式*/
.el-button--primary {
  color: #fff;
  // background-color: #2e354f;
  // border: 1px solid #2e354f;
}

/* 显示编号*/
.display-box {
  // background-color: #25283b;
  display: flex;
  // flex-direction: row;
  // justify-content: flex-end;
  align-items: center;
  // padding-right: 5px;
  position: absolute;
  right: 20px;
}

.menu-title {
  // color: rgb(191, 203, 217);
  font-size: 14px;
  color: #005e96;
}

.menu-button-group {
  width: 90%;

  .el-button {
    margin: 5px 1px;
  }
}

/* 图片区域*/
.content {
  clear: both;
  text-align: center;
  width: 100%;
  // height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* margin: 0 10px 0 10px; */
}

// .config-back {
//   background-color: #25283b;
// }

.config-content {
  width: 100%;
  // padding-bottom: 40px;
  // margin-top: 20px;
}

.direction-img {
  margin-bottom: 10px;

  img {
    height: 10px;
    width: 200px;
  }
}

.details-row {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  height: 45px;
  width: 1442px;
  /* border: 1px solid #000; */
  position: relative;
  z-index: 3;
  margin-left: 90px;
}

/* 详情信息*/
.details-box {
  width: 140px;
  height: 45px;
  border-radius: 4px;
  background-color: #2e354f;
  text-align: center;
  color: rgb(191, 203, 217);
  /*  padding: 10px 10px; */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  display: inline-block;
}

/* 风速等输入框*/
.details-input {
  width: 45px;
  height: 15px;
  color: #46a6ff;
  background-color: #f6f5ec;
  padding: 0;
  border-radius: 4px;
  margin: 0;
}

.small-title {
  margin: 4px 0 0 0;
  color: #46a6ff;
}

.el-input--small {
  margin-top: 2px;
  font-size: 14px;
}

.param-name {
  width: 140px;
  color: #afafb0;
  font-size: 12px;
  margin-right: 0;
  // white-space:nowrap;
  // overflow:hidden;
}

.input-color {
  margin: 0;
  color: #46a6ff;
}

/* 车道图片*/
.lane-img {
  margin: 10px 10px 0 10px;
  width: 200px;
  height: 90px;
  display: inherit;
}

.dialog-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.lane-img-box {
  /* display: flex;
    flex-direction: column; */
  align-items: center;
  /* margin: 10px 0 0 5px; */
  float: left;
  text-align: center;
}

.lane-radio {
  margin-top: 10px;
}

.el-tabs--border-card {
  background: none;
  border: 0px;
}

.el-tabs__header {
  background-color: inherit;
}

.equipment-img-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
  width: 100px;

  .el-row {
    margin-bottom: 10px;
  }
}

.equipment-img {
  height: 32px;
  width: 32px;
}

.eq-exist {
  content: "";
  background-color: #6a7b98;
  opacity: 0.4;
  z-index: 1;
  border-radius: 2px;
}

/* .eq-exist {
    position: absolute;
    top: 0;
    left: 0;
    content: "";
    background-color: #6a7b98;
    opacity: 0.4;
    z-index: 1;
    width: 100%;
    height: 100%;
    border-radius: 2px;
  } */

/* 画布区域*/
.config-img-box {
  // display: flex;
  // justify-content: center;
  // align-items: center;
  height: 680px;
  width: 1788px;
  background-size: cover;
  position: relative;
  // margin-left: 50px;
  // margin-top: 10px;
  margin: 10px auto;
  overflow-x: auto;
  padding: 38px 0px;
}

.config-img {
  height: 580px;
  width: 1620px;
  position: absolute;
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
    margin: 5px 0;
    // padding: 5px 5px;
    cursor: pointer;

    &:hover {
      background: #eee;
    }
  }
}

// 辅助线
.guide {
  display: none;
  position: absolute;
  left: 0;
  top: 0;
}

#guide-h {
  border-top: 1px solid red;
  width: 100%;
}
#guide-h1 {
  border-top: 1px solid red;
  width: 100%;
}

#guide-v {
  border-left: 1px solid red;
  height: 100%;
}
#guide-v1 {
  border-left: 1px solid red;
  height: 100%;
}
</style>
<style lang="scss">
/* 插槽样式*/
.reset-confirm {
  max-width: 280px;
  background-color: #4e5e89;
  border-color: #979aa5;
  color: #e5e8f1;

  ::after {
    border-bottom-color: #4e5e89 !important;
  }
}

.el-icon-info:before {
  content: "";
  color: #f9eb69;
}

.direction-input {
  .el-input {
    width: 150px;

    .el-input__inner {
      background-color: #25283b;
      border-color: #25283b;
      color: #bcc2c7;
      font-size: 16px;
      width: 150px;
      text-align: center;
    }
  }
}

/*状态弹框 */
.equipment-dialog {
  .el-form-item__label {
    // color: #c0ccda;
  }

  .el-dialog__header {
    // background-color: #455d79;
  }

  .el-dialog__title {
    // color: #fff;
    // background-color: #455d79;
  }

  .el-dialog__body {
    // color: #c0ccda;
    // background-color: #304156;
    padding-top: 20px;
    padding-bottom: 10px;
    height: 600px;
    overflow-y: auto;
  }

  .el-dialog__footer {
    // color: #c0ccda;
    // background-color: #304156;
    padding-top: 0;
  }

  .el-form-item__content {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .el-radio {
    margin: 8px 5px 10px 10px;
    // color: #c0ccda;
  }

  .form-item-img {
    width: 25px;
    height: 25px;
    margin-left: 10px;
    margin-right: 10px;
  }
}

/* 弹框中的table*/
.batch-table {
  /* table为空时*/
  .el-table__empty-block {
    background-color: #304156;
  }

  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  /* 鼠标停留在table*/
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: #304156;
  }
}

.tunnelConfig-msg {
  background-color: #1890ff;
  border-color: #1890ff;

  .el-message__content {
    color: #ffffff;
  }
}
</style>
<style>
.el-tabs--border-card .el-tabs__header {
  background-color: inherit;
  border-bottom: 1px solid #455d79;
}

.el-tabs--border-card .el-tabs__content {
  padding: 0px;
}

.tunnelNameSelect {
  margin-left: 10px;
}

/* .el-tabs--border-card .el-tabs__header .el-tabs__item.is-active {
  background-color: rgb(78, 94, 137);
  color: #ccd7e4;
  border: 1px solid #7f8c98;
} */
</style>
