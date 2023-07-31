<template>
  <div
    class="navbar"
    :style="
      sideTheme == 'theme-light' || sideTheme == 'theme-dark'
        ? 'display:flex;'
        : ''
    "
  >
    <!-- 缩放按钮操作  -->
    <template v-if="!topNav">
      <hamburger
        id="hamburger-container"
        :is-active="sidebar.opened"
        class="hamburger-container"
        @toggleClick="toggleSideBar"
        v-if="!topNav"
      />
    </template>
    <!-- 面包屑 -->
    <!-- <breadcrumb :style="getRoute($route.path) ?'display:none;':''" id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav && sideTheme != 'theme-blue'"/> -->
    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <template v-if="sideTheme != 'theme-blue'">
          <screenfull
            :id="
              topNav && sideTheme == 'theme-dark' ? 'top_icon' : 'screenfull'
            "
            class="right-menu-item hover-effect white_icon"
          />
        </template>
      </template>
      <!-- 预警 -->
      <!-- <el-tooltip
        :content="alarmTitle"
        effect="dark"
        placement="bottom"
        class="right-menu-item bell"
        style="display: inline-block"
      >
        <el-popover
          placement="bottom"
          trigger="click"
          @click="disabled = !disabled"
        >
          <div style="height: 30px">
            <span
              class="popover_hint"
              style="float: left; color: rgb(254, 0, 0)"
              >预警({{ nodealNum }}个待解决)</span
            >
            <span
              class="popover_hint"
              style="float: right; font-size: 14px; cursor: pointer"
              @click="moreInfo"
              >更多>></span
            >
            <span
              class="popover_hint"
              style="
                float: right;
                color: rgb(0, 170, 255);
                cursor: pointer;
                margin-right: 30px;
              "
              @click="ignores"
              >全部忽略</span
            >
          </div>
          <div style="border: 0.4px solid rgb(204, 204, 204)"></div>
          <el-table :data="warningInfoList">
            <el-table-column
              width="150"
              property="warningName"
              label="预警名称"
            ></el-table-column>
            <el-table-column
              width="180"
              property="warningTime"
              label="预警时间"
            ></el-table-column>
            <el-table-column
              width="150"
              property="tunnelName"
              label="隧道名称"
            ></el-table-column>
            <el-table-column
              width="300"
              property="inforSources"
              label="预警内容"
            ></el-table-column>
            <el-table-column
              width="100"
              property="processState"
              label="处理状态"
            >
              <template slot-scope="scope">
                <el-tag v-if="scope.row.processState == 0" type="danger"
                  >未处理</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-finished"
                  @click="handleView(scope.row)"
                  >查看</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getAlarmInfo"
            @mousemove="block"
          />
          <div
            :class="
              topNav && sideTheme == 'theme-dark'
                ? 'avatar-wrapper white_icon'
                : 'avatar-wrapper'
            "
            slot="reference"
          >
            <el-badge
              :value="nodealNum"
              :hidden="nodealNum > 0 ? false : true"
              class="item bell_icon"
              style="cursor: pointer"
            >
              <i class="el-icon-bell white_icon" @click="bell()"></i>
            </el-badge>
          </div>
        </el-popover>
      </el-tooltip> -->
      <!-- 隧道运营APP下载二维码 -->
      <!-- <el-tooltip content="隧道营运APP下载" effect="dark" placement="bottom"  class="right-menu-item bell" style="display: inline-block;">
        <el-popover
              placement="bottom"
              trigger="hover">
            <img :src="qrCodePath" style="width:200px;height:260px;">
            <div :class="topNav && sideTheme == 'theme-dark'?'avatar-wrapper white_icon':'avatar-wrapper'" slot="reference">
              <el-badge :value="nodealNum" :hidden="true" class="item bell_icon"
                  style="cursor:pointer;">
                  <i class="el-icon-mobile-phone white_icon" @hover="bell()"></i>
              </el-badge>
            </div>
        </el-popover>
      </el-tooltip> -->

      <!-- qrCodeShow -->
      <!--  大屏    -->
      <!-- <div :class="topNav && sideTheme == 'theme-dark'?'avatar-wrapper white_icon':'avatar-wrapper'" slot="reference"> -->
      <el-badge
        :value="nodealNum"
        :hidden="nodealNum > 0 ? false : true"
        class="item bell_icon"
        style="cursor: pointer;padding: 0 16px; vertical-align: text-bottom;
              color: white !important;caret-color: rgba(0,0,0,0);user-select: none;"
      >
        <img src="../../assets/image/evtNum.png" style="height:3vh;transform: translateY(0.7vh);" @click="bell()"/>
      </el-badge>
      <!-- </div> -->
      <el-dropdown class="right-menu-item bell">
        <el-dropdown-menu style="display: none"></el-dropdown-menu>
        <el-tooltip content="监控一体化大屏" effect="dark" placement="bottom">
          <i
            class="
              el-icon-s-platform
              homefun
              right-menu-item
              hover-effect
              white_icon
            "
            @click="bigScreenfun2"
            style="height: 7.6vh;line-height:7.6vh; font-size: 2.2vh;"
            title="监控一体化大屏"
          ></i>
        </el-tooltip>
      </el-dropdown>
      <!--  个人中心  -->
      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
      >
        <div
          :class="
            topNav && sideTheme == 'theme-dark'
              ? 'avatar-wrapper white_icon'
              : 'avatar-wrapper'
          "
        >
          <img src="@/assets/images/avatar.png" class="user-avatar" />
          <span>{{ roleGroup }}</span>
        </div>

        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <el-dialog
        class="eventDiglog"
        v-dialogDrag
        :visible.sync="eventopen"
        width="95%"
        append-to-body
      >
        <!-- <div class="app-container"  style="height: 650px;"> -->
        <div class="title">
          {{ eventTitle }}
          <img
            src="../../assets/cloudControl/dialogHeader.png"
            style="height: 30px"
          />
          <img
            src="../../assets/cloudControl/closeIcon.png"
            style="
              height: 14px;
              position: absolute;
              right: 10px;
              top: 10px;
              cursor: pointer;
            "
            @click="closeDialog()"
          />
        </div>
        <div class="blueLine"></div>
        <div class="warning">
          <div style="color: #0a88bd; font-size: 24px">
            {{ warning.inforSources }}
          </div>
        </div>
        <!-- <div class="videoDiv" style="float: left;">
            <div class="img">
                <el-popover placement="top-end" title="" trigger="click">
                  <img :src="picPath" style="width: 600px;height: 600px">
                  <img slot="reference" :src="picPath" style="width: 100%;height: 100%;cursor: pointer;border-radius:10px;">
                </el-popover>
            </div>
            <div class="video">
              <div v-for="(item,index) in linkedCameras"  :key="index" >
                <div style="width: 100%;height: 230px;float: left;border:1px solid #4196C6;" v-if="linkedCameras.length==1 && linkedCameras[0].url != null">
                  <videoPlayer style="100%;height: 228px;" :id="item.id" :rtsp="item.url" :hostIP="hostIP" :open="openV"></videoPlayer>
                </div>
                <div style="width: 75%;height: 230px;float: left;border:1px solid #4196C6;" v-else-if="index==0">
                  <videoPlayer style="100%;height: 228px;" :id="item.id" :rtsp="item.url" :hostIP="hostIP" :open="openV"></videoPlayer>
                </div>
                <div style="width: 23%;height: 70px;float: left; margin: 0px 0px 10px 5px;border:1px solid #4196C6;" v-else >
                   <videoPlayer  style="width: 100%;height: 68px;" :id="item.id" :rtsp="item.url" :hostIP="hostIP" :open="openV"></videoPlayer>
                </div>
              </div>
            </div>
        </div>-->

        <div class="warningTop">
          <div class="photoBox">
            <img src="../../assets/images/warningPhoto.png" />
            <div>
              <img
                v-for="(item, index) of 4"
                src="../../assets/images/warningPhoto.png"
                :key="index"
              />
            </div>
          </div>
          <div class="processBox">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                color="#0bbd87"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
          <div class="strategyBox">
            <el-table
              :data="strategyList"
              header-align="center"
              height="230"
              :row-class-name="tableRowClassName"
            >
              <el-table-column
                property="name"
                align="center"
                label="策略名称"
              ></el-table-column>
              <el-table-column
                property="type"
                align="center"
                label="策略类型"
              ></el-table-column>
              <el-table-column
                property="message"
                align="center"
                label="策略信息"
              ></el-table-column>
            </el-table>
            <div slot="footer" class="strategyFooter" style="text-align: left">
              <el-button @click="startStrategy" style="margin-left: 22%"
                >转为事件</el-button
              >
              <el-button @click="startStrategy" style="margin-left: 2%"
                >执 行</el-button
              >
              <el-button @click="ignoreEvent" style="margin-left: 2%"
                >忽 略</el-button
              >
              <el-button @click="cancel" style="margin-left: 2%"
                >取 消</el-button
              >
            </div>
          </div>
        </div>
        <div class="warningBottom">
          <div class="warningTable">
            <h3>消防物资</h3>
            <el-table
              :data="emdeviceList"
              header-align="center"
              height="200"
              :row-class-name="tableRowClassName"
            >
              <el-table-column
                property="mileage"
                align="center"
                label="桩号"
                width="130"
              ></el-table-column>
              <el-table-column
                property="eqFire"
                align="center"
                label="灭火器"
                width="80"
              ></el-table-column>
              <el-table-column
                property="eqFireHydrant"
                align="center"
                label="消火栓"
                width="70"
              ></el-table-column>
              <el-table-column
                property="eqFoam"
                align="center"
                label="水成膜"
              ></el-table-column>
            </el-table>
          </div>
          <div class="humanTable">
            <h3>应急人员</h3>
            <el-table
              :data="emergencyPerList"
              header-align="center"
              height="200"
              :row-class-name="tableRowClassName"
            >
              <el-table-column
                property="userName"
                align="center"
                label="应急人员"
              ></el-table-column>
              <el-table-column
                property="groupName"
                align="center"
                label="所属组"
              ></el-table-column>
              <el-table-column
                property="phone"
                align="center"
                label="联系电话"
              ></el-table-column>
            </el-table>
          </div>
          <div class="trafficTable">
            <h3>交通流量</h3>
            <el-table
              :data="trafficList"
              header-align="center"
              height="200"
              :row-class-name="tableRowClassName"
            >
              <el-table-column
                property="byLane"
                align="center"
                label="车道号"
              ></el-table-column>
              <el-table-column
                property="byVehicelNum"
                align="center"
                label="车流量"
              ></el-table-column>
              <el-table-column
                property="bySpeed"
                align="center"
                label="平均速度"
              ></el-table-column>
              <el-table-column
                property="fSpaceOccupyRation"
                align="center"
                label="占有率"
              ></el-table-column>
            </el-table>
          </div>
          <div class="CoTable">
            <h3>CO/VI监测</h3>
            <el-table
              :data="devicesList"
              header-align="center"
              height="200"
              :row-class-name="tableRowClassName"
            >
              <el-table-column
                prop="eqTypeName"
                align="center"
                label="监测内容"
                width="150"
              ></el-table-column>
              <el-table-column
                prop="remark"
                align="center"
                label="监测设备"
              ></el-table-column>
              <el-table-column
                prop="sensorValue"
                align="center"
                label="监测数据"
              >
                <template slot-scope="scope">
                  <span>{{ scope.row.sensorValue }}</span>
                  <span v-if="scope.row.eqTypeId == '14'"> ppm </span>
                  <span v-else-if="scope.row.eqTypeId == '5'"> lux </span>
                  <span v-else-if="scope.row.eqTypeId == '6'"> cd/m² </span>
                  <span v-else-if="scope.row.eqTypeId == '16'"> m/s </span>
                  <span v-else-if="scope.row.eqTypeId == '15'"> 1/km </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <!-- </div> -->
      </el-dialog>
    </div>
  </div>
</template>
<script>
import bus from "@/utils/bus";
import { mapState } from "vuex";

import { mapGetters } from "vuex";
import qrCodeImg from "@/assets/images/qbqlogo.png";
import Breadcrumb from "@/components/Breadcrumb";
import TopNav from "@/components/TopNav";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import RuoYiGit from "@/components/RuoYi/Git";
import RuoYiDoc from "@/components/RuoYi/Doc";
import {
  listWarningInfo,
  getStrategyList,
  getDeviceInfo,
  handleStrategy,
  handleStrategyRollBack,
  emdeviceList,
  emergencyPerList,
  getTrafficInfo,
  getIgnoreEvent,
  oneKeyIgnore,
  ioToBase64,
} from "@/api/event/warningInfo";
import { getVideoRecord, getLocalIP } from "@/api/event/vedioRecord.js";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import earlyWarning from "@/components/earlyWarning"; // 路侧设备
import { checkPermi } from "@/utils/permission.js";
import { getUserProfile } from "@/api/system/user";

export default {
  data() {
    return {
      /*      checked:{
        switch:false,
      }, */
      qrCodePath: qrCodeImg,
      qrCodeShow: false, // 二维码
      activeName: "first",
      dataList: [
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 0,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 1,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 1,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 0,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 0,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 1,
        },
        {
          time: "2021-11-25",
          description: "发生行人横穿(工作人员)事件",
          dir: "北京方向",
          PileNo: "K500+120",
          status: 1,
        },
      ],
      strategyList: [
        {
          name: "风机正转策略",
          type: "手动控制",
          message: "主风机控制执行正转",
        },
        {
          name: "车道指示器禁行策略",
          type: "手动控制",
          message: "普通车道指示器控制执行...",
        },
        {
          name: "车道指示器禁行策略",
          type: "手动控制",
          message: "普通车道指示器控制执行...",
        },
        {
          name: "车道指示器禁行策略",
          type: "手动控制",
          message: "普通车道指示器控制执行...",
        },
        {
          name: "车道指示器禁行策略",
          type: "手动控制",
          message: "普通车道指示器控制执行...",
        },
      ],
      activities: [
        {
          content: "根据周边视频确认警情",
        },
        {
          content: "拨打119",
        },
        {
          content: "请工作人员到达现场",
        },
        {
          content: "选择控制策略",
        },
        {
          content: "配合119灭火",
        },
        {
          content: "清理现场",
        },
      ],
      // 小弹层的数据 start
      alarmTitle: "您有0条未读消息",
      nodealNum: '',
      warningInfoList: [],
      total: 0, // 总条数
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        processState: 0,
      },
      // 小弹层的数据 end
      // 大弹框的数据 start
      id: "",
      eventTitle: "",
      hostIP: "",
      picPath: "",
      rtsp: "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov",
      positionTimer: null,
      eventopen: false,
      openV: false,
      warning: {},
      linkedCameras: [],
      ids: [],
      warningList: [], // 预警处理
      emdeviceList: [], // 消防洞室
      emergencyPerList: [], // 应急人员
      devicesList: [], // 环境参数
      trafficList: [], // 交通参数
      // 大弹框的数据 end
      roleGroup: "", //角色
      clickSure:false,
    };
  },
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    videoPlayer,
    earlyWarning,
  },
  computed: {
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
      eventUntreatedNum: (state) => state.websocket.eventUntreatedNum,
    }),
    ...mapGetters(["sidebar", "device"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
      set(val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val,
        });
      },
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
  },
  watch: {
    // sdEventList(event) {
    //   console.log(event,"事件弹窗");
    // this.nodealNum += event.length;
    // this.$forceUpdate()
    // },
    eventUntreatedNum(event){
      // debugger
      console.log(event, "事件总数");
      this.nodealNum = event;
    },
  },

  created() {
    this.nodealNum =0
      this.getUser();
    // setInterval(() => {
    //   setTimeout(this.getAlarmInfo, 0);
    // }, 5000 * 1);
  },
  mounted() {
    // setInterval(()=>{
    //   this.getNodealNum()
    //
    // },5000)

    // 关闭列表弹窗
    bus.$on("closeDialog", () => {
      this.clickSure = false
    });
    //  bus.$on("getEvtList", () => {
    //   this.nodealNum = this.nodealNum - 1;
    // });
  },
  methods: {
    // getNodealNum() {
    //   getEventUntreatedNum().then((res) => {
    //     // console.log(res, "事件总数");
    //     this.nodealNum = res.data;
    //   });
    // },
    getRoute(path) {
      var arr = [
        "/index",
        "/map/map/index",
        "/emergency/administration/dispatch",
        "/map/map3d/index",
        "/energy",
      ];
      if (arr.includes(path)) {
        return true;
      } else {
        return false;
      }
    },
    // 获取当前角色信息
    getUser() {
      getUserProfile().then((response) => {
        console.log(response,"获取当前角色信息")
        this.roleGroup = response.data.nickName;
      });
    },
    block() {},
    changeEarlyWarn(type) {
      this.$refs.earlyWarning.changeEarlyWarn(type);
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    bigScreenfun() {
      if (checkPermi(["fullViewShow"])) {
        let routeUrl = this.$router.resolve({
          path: "/bigscreen/index.html",
          query: {},
        });
        window.open(routeUrl.href, "_blank");
      } else {
        this.$modal.msgWarning("您的账号没有查看此功能的权限");
      }
    },
    bigScreenfun2() {
      if (checkPermi(["fullViewShow"])) {
        let routeUrl = this.$router.resolve({
          path: "/bigScreen2/index.html",
          query: {}, 
        });
        window.open(routeUrl.href, "_blank");
      } else {
        this.$modal.msgWarning("您的账号没有查看此功能的权限");
      }
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "";
            this.$cache.local.remove("deptId")
            // location.href = '/#/login';
          });
        })
        .catch(() => {});
    },
    // handleClick(tab, event) {
    //   console.log(tab, event);
    // },

    // 鼠标经过铃铛提示信息
    // getAlarmInfo() {
    //   listWarning(this.queryParams).then((response) => {
    //     this.warningInfoList = response.rows;
    //     this.total = response.total;
    //     this.alarmTitle = "您有" + response.total + "个未处理预警";
    //     // this.nodealNum = response.total;
    //   });
    // },
    // 更多
    moreInfo() {
      this.$router.push({
        name: "WarningInfo",
      });
    },
    // 全部忽略
    ignores() {
      oneKeyIgnore().then((response) => {
        if (response.code == 200) {
          this.getAlarmInfo();
          this.$modal.msgSuccess("已成功忽略");
        } else {
          this.$modal.msgError("忽略失败！！！");
        }
      });
    },
    // 查看
    handleView(row) {
      /* if(row.holeDirection=="Z"){
        row.holeDirection="左洞";
      }else{
        row.holeDirection="右洞";
      } */
      this.checkedReset();
      this.id = row.id;
      this.warning = row;
      this.eventopen = true;
      this.openV = true;
      this.eventTitle = row.warningName;
      this.getIoToBase64(row.picture);
      this.getStrategyList(row.id);
      this.positionTimer = setInterval(() => {
        this.getDeviceInfo(row);
        this.getTrafficInfo(row);
      }, 1000);
      this.getEmdeviceList(row.id);
      this.getEmergencyPerList(row.id);
      this.openPlayer(row.linkedCamera);
      getLocalIP().then((response) => {
        this.hostIP = response;
      });
    },
    checkedReset() {},
    bell() {
      if(!this.clickSure){
        this.clickSure = true
        bus.$emit("openTableDialog");
      }else{
        this.clickSure = false
        bus.$emit("closeDialog");

      }

    },
    // 监听弹层关闭
    closeDialog() {
      clearInterval(this.positionTimer);
      this.openV = false;
      this.positionTimer = null;
      this.cancel();
    },
    getIoToBase64(url) {
      ioToBase64(url).then((response) => {
        this.picPath = response.msg;
      });
    },
    openPlayer(linkedCamera) {
      getVideoRecord(linkedCamera).then((response) => {
        console.log(response.data, "response.data");
        this.linkedCameras = response.data;
      });
    },
    on_select(selection) {
      //点击行选中复选框
      this.$refs.warningTable.toggleRowSelection(selection);
    },
    getStrategyList(id) {
      getStrategyList(id).then((response) => {
        this.warningList = response.data;
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 消防洞室列表获取
    getEmdeviceList(id) {
      emdeviceList(id).then((response) => {
        this.emdeviceList = response.data;
      });
    },
    // 应急人员列表获取
    getEmergencyPerList(id) {
      emergencyPerList(id).then((response) => {
        this.emergencyPerList = response.data;
      });
    },
    getDeviceInfo(row) {
      //环境参数
      getDeviceInfo(row.tunnelId, row.holeDirection).then((response) => {
        this.devicesList = response.data;
      });
    },
    getTrafficInfo(row) {
      //交通参数
      getTrafficInfo(row.tunnelId, row.holeDirection).then((response) => {
        this.trafficList = response.data;
      });
    },
    // 生效
    startStrategy() {
      if (this.ids.length > 0) {
        this.$confirm("是否确认执行！", "警告", {
          confirmButtonText: "执行",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.$modal.msgSuccess("手动控制中.......");
            return handleStrategy(this.ids, this.warning.id);
          })
          .catch((_) => {});
      } else {
        this.$modal.msgSuccess("请选择策略");
      }
    },
    // 一键恢复
    startStrategyRollBack() {
      this.$confirm("是否确认执行！", "警告", {
        confirmButtonText: "执行",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$modal.msgSuccess("手动控制中.......");
          return handleStrategyRollBack(this.warning.id);
        })
        .catch((_) => {});
    },
    // 忽略
    ignoreEvent() {
      getIgnoreEvent(this.id).then((response) => {
        if (response.code == 200) {
          this.$modal.msgSuccess("已成功忽略");
          this.getAlarmInfo();
          this.cancel();
        } else {
          this.$modal.msgError("忽略失败！！！");
        }
      });
    },
    // 取消
    cancel() {
      this.eventopen = false;
      /*  this.checked.switch = false
      this.checked.value1 = false
      this.checked.value2 = false
      this.checked.value3 = false
      this.checked.value4 = false
      this.checked.value5 = false
      this.checked.value6 = false
      this.checked.value7 = false
      this.checked.value8 = false
      this.checked.value9 = false */
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0px !important;
}
// .theme-dark .navbar{background-color:unset;}
.navbar {
  background: transparent;
  position: absolute;
  top: 0;
  right: 0;
}
.right-menu{
  position: absolute;
  right: 0;
}
.theme-dark .navbar {
  background-color: unset;
}
.theme-dark .right-menu #screenfull {
  color: white;
}
.el-dropdown-menu_theme-blue {
  background-color: rgba(0, 74, 119, 0.9);
}
.el-dropdown-menu_theme-dark {
  background-color: rgba(29, 88, 169, 0.9);
}
.el-dropdown-menu_theme-light {
  background-color: rgba(255, 255, 255, 0.9);
  .el-tabs__content {
    .theme-dark .el-scrollbar__wrap {
      background-color: rgba(255, 255, 255, 0.8);
    }
    .el-scrollbar__view {
      .warnItem {
        .description {
          p {
            color: #000000;
          }
          span,
          h5 {
            color: #999999;
          }
        }
      }
    }
  }
}
.homefun {
  font-size: 22px;
  color: #8697ad;
  cursor: pointer;
}
.bell-item {
  margin-right: 20px;
  margin-bottom: 15px;
}
.navbar {
  height: 100%;
  // overflow: hidden;
  position: relative;
  // background: #fff;
  // box-shadow: 0 1px 4px rgba(0,21,41,.08);
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 7.6vh;
    &:focus {
      outline: none;
    }
    .bell {
      // margin-right: 15px;
    }
    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #fff;
      vertical-align: text-bottom;
      >>> .el-dropdown-menu {
        padding: 0 25px;
        background-color: rgba(0, 61, 96, 0.8);
      }
      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;
        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }
    .avatar-container {
      margin-right: 15px;
      .avatar-wrapper {
        // margin-top: 5px;
        position: relative;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 7.6vh;
        span {
          font-size: 0.8vw;
          margin-left: 10px;
        }
        .user-avatar {
          cursor: pointer;
          // width: 30px;
          height: 3vh;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
    #top_icon {
      color: white;
    }
  }
}
.theme-linght .white_icon {
  color: #999 !important;
}
// .theme-dark .white_icon{color: white!important;}
.theme-dark .main-container .white_icon {
  color: #fff !important;
}
.theme-dark-navbar .white_icon {
  color: white !important;
}
.theme-blue .white_icon {
  color: white !important;
}
/* .theme-blue .el-icon-bell{color:white;font-size:21px;} */
/* .theme-dark .el-icon-bell{color:white;font-size:21px;} */
.theme-dark
  #app
  .sidebar-container
  .theme-dark
  .el-submenu
  .el-menu-item:hover {
  background-color: unset;
}
.theme-light .navbar .right-menu .right-menu-item {
  color: white !important;
}
::v-deep .el-dialog__body {
  padding: 0px 0px 10px 0px !important;
}
.eventDiglog {
  .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    // color: white;
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
    margin: 0 !important;
  }

  // .el-dialog__header .el-dialog__title{
  //  font-size: 24px;
  //  font-weight: bolder;
  // }
  // .el-dialog__headerbtn i {
  //     font-size: 25px;
  // }
  .el-dialog__header {
    display: none !important;
  }
  h3 {
    margin: 5px 5px 5px 0px;
    text-align: left;
    font-weight: normal;
  }
  .el-row {
    padding-left: 10px;
    margin-left: 10px;
    width: 92%;
    border-radius: 0.5rem;
  }
}
.warning {
  width: 100%;
  text-align: center;
  line-height: 60px;
}
.warningTop {
  width: calc(100% - 40px);
  height: 45%;
  display: flex;
  margin: 0 20px;
  .photoBox {
    width: 24.2%;
    height: 100%;
    > img {
      width: 100%;
      height: 74%;
      background-image: url(../../assets/images/warningPhoto.png);
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
    > div {
      display: flex;
      justify-content: space-between;
      width: 100%;
      height: 24%;
      margin-top: 2px;
      > img {
        width: 24%;
        height: 100%;
      }
    }
  }
  .processBox {
    width: 20%;
    height: 100%;
    margin-left: 1%;
    padding: 30px 10px;
    .el-timeline-item__content {
      color: #003a5d !important;
    }
    .el-timeline-item {
      padding-bottom: 15px;
    }
  }
  .strategyBox {
    width: 53.8%;
    height: 100%;
    margin-left: 1%;
    padding: 20px 0;

    .el-table {
      margin-bottom: 10px;
      .el-table__header-wrapper th {
        // background-color: #d6edf9 !important;
      }
    }
    .strategyFooter {
      margin: 0 auto;
      justify-content: space-between;
      > .el-button {
        border-radius: 20px !important;
        width: 130px;
        height: 35px;
        line-height: 15px;
        background-color: #1ca7ec;
        color: white;
      }
    }
  }
}
.warningBottom {
  width: calc(100% - 40px);
  height: 36%;
  display: flex;
  margin: 0px 20px 10px 20px;
  padding: 10px 0px;
  > div {
    width: 25%;
    height: 100%;
    margin-left: 1%;
  }
  > div:nth-of-type(1) {
    margin-left: 0%;
  }
  .warningTable {
    overflow-y: auto;
    // padding: 0 20px;
    .el-table::before,
    .el-table--group::after,
    .el-table--border::after {
      background-color: transparent !important;
    }
  }
}
</style>
<style>
.bell_icon .el-badge__content {
  top: 22px !important;
  right:18px !important;
}
</style>

<style lang="scss">
.videoDiv {
  width: 35%;
  float: left;
  height: 100%;
  border-radius: 0.5rem;
}
.img {
  width: 100%;
  height: 45%;
  border: 1px solid #4196c6;
  border-radius: 0.5rem;
}
.video {
  width: 100%;
  height: 50%;
  border-radius: 0.5rem;
  margin-top: 2%;
}
.el-icon-my-people {
  background: url("../../assets/image/logo.png") center no-repeat;
  font-size: 16px;
  background-size: cover;
}
</style>
