<template>
  <div>
    <template
      v-if="
        sideTheme == 'theme-dark' ||
        sideTheme == 'theme-light' ||
        sideTheme == 'theme-blue'
      "
    >
      <div :class="classObj" class="app-wrapper">
        <div
          v-if="device === 'mobile' && sidebar.opened"
          class="drawer-bg mapBox"
          @click="handleClickOutside"
        />
        <!-- 左右结构 -->
        <template v-if="topNav == false">
          <sidebar class="sidebar-container" />
          <div
            :class="{ hasTagsView: needTagsView }"
            class="main-container"
            :style="fixedHeaderClass"
          >
            <div
              :class="{ 'fixed-header': fixedHeader }"
              :style="fixedHeader ? 'background-color:white;' : ''"
            >
              <navbar style="display: block; height: 7.6vh" />
              <!-- <tags-view v-if="needTagsView" /> -->
            </div>
            <div
              :class="getRoute2($route.path) ? '' : 'breadcrumbAppmainBox'"
              style="height: 100%"
            >
              <breadcrumb
                :style="'display:' + getRoute($route.path) + ';'"
                ref="Breadcrumb"
                id="breadcrumb-container"
                class="breadcrumb-container"
              />
              <app-main />
            </div>

            <right-panel>
              <settings />
            </right-panel>
          </div>
        </template>
        <!-- 上下结构 -->
        <template v-else>
          <div
            :class="{ 'fixed-header': fixedHeader }"
            :style="fixedHeader ? 'width:100%;' : ''"
            v-if="titleHeader == '0'"
          >
            <div
              :class="
                $route.path == '/map/map3d/index'
                  ? 'topNav_head mapBox'
                  : $route.path == '/energy'
                  ? 'topNav_head mapBox'
                  : 'topNav_head'
              "
            >
              <sidebar
                class="sidebar-container index_menu blue_index_menu"
                style="
                  width: 75% !important;
                  position: relative;
                  box-shadow: unset;
                  float: left;
                  display: flex;
                  align-items: center;
                "
              />
              <template v-if="weatherView">
                <div class="weather">
                  <img :src="weather_weatherimg" />
                  <p>{{ weather_weather }}</p>
                </div>
              </template>
              <navbar
                :class="
                  sideTheme == 'theme-light'
                    ? 'theme-light-navbar'
                    : 'theme-dark-navbar'
                "
                :style="
                  weatherView
                    ? 'width: 15%!important;justify-content:right;'
                    : 'width: 25%;justify-content:right;display:flex'
                "
              />
            </div>
          </div>
          <!-- 标题放中间 -->
          <div
            :class="{ 'fixed-header': fixedHeader }"
            :style="fixedHeader ? 'width:100%;' : ''"
            v-if="titleHeader == '1'"
          >
            <div
              :class="
                $route.path == ('/map/map3d/index' || '/energy')
                  ? 'topNav_head_center mapBox'
                  : 'topNav_head_center'
              "
              :style="{
                backgroundColor:sideTheme == 'theme-dark'?($route.path == '/index' ? '#010913' : '#004375'):'#100A43'
              }"

            >
              <centerbar
                style="
                  width: 100% !important;
                  height: 7.6vh;
                  position: relative;
                  box-shadow: unset;
                  display: flex;
                "
              ></centerbar>
              <!-- <sidebar
                class="sidebar-container index_menu blue_index_menu"
                style="
                  width: 75% !important;
                  position: relative;
                  box-shadow: unset;
                  float: left;
                  display: flex;
                  align-items: center;
                "
              /> -->
            </div>
          </div>
          <div
            :class="{ hasTagsView: needTagsView }"
            class="main-container"
            :style="
              fixedHeader
                ? 'padding-top:50px;margin-left:0px;'
                : 'margin-left:0px;'
            "
          >
            <!-- <div :class=""> -->
            <div :class="getRoute2($route.path) ? 'noSeparate' : 'separate'">
              <breadcrumb
                :style="'display:' + getRoute($route.path) + ';'"
                ref="Breadcrumb"
                id="breadcrumb-container"
                class="breadcrumb-container"
              />
              <app-main />
              <right-panel>
                <settings />
              </right-panel>
              <!-- <el-badge
                :value="eventValue"
                class="eventIcon"
                :hidden="badgeHidden"
              >
                <div
                  class="el-icon-s-order"
                  @click="openEventTitleDialog"
                ></div>
              </el-badge> -->
              <evtDialogOneThing v-if="evtDialogOneThing"></evtDialogOneThing>
              <event-dialog
                v-if="eventDialogPic"
                ref="picDialog"
              ></event-dialog>
              <event-dialogTable v-if="eventDialogTable"></event-dialogTable>
              <evtDialogVideo v-if="evtDialogVideo"></evtDialogVideo>
            </div>
          </div>
        </template>
      </div>
    </template>
    <!-- add -->
    <template v-else>
      <div :class="classObj" class="app-wrapper">
        <div :class="{ 'fixed-header': fixedHeader }">
          <div class="topNavHeadBlue">
            <sidebarblue
              class="sidebar-container index_menu blue_index_menu"
              style="width: 73% !important; float: left"
            />
            <template v-if="weatherView == true">
              <div class="weather">
                <img :src="weather_weatherimg" />
                <p>{{ weather_weather }}</p>
              </div>
            </template>
            <navbar
              class="theme-blue-navbar"
              :style="
                weatherView
                  ? 'width: 12%!important;height:50px;float:left;'
                  : 'margin-left:10%;width: 12%!important;height:50px;float:left;'
              "
            />
            <div class="clear"></div>
          </div>
        </div>
        <div
          :class="{ hasTagsView: needTagsView }"
          class="main-container"
          :style="fixedHeaderClass"
        >
          <div :class="{ 'fixed-header': fixedHeader }"></div>
          <template v-if="is_breadcrumb == 'true' && $route.path != '/index'">
            <breadcrumb
              ref="Breadcrumb"
              id="breadcrumb-container"
              class="breadcrumb-container"
              style="margin-top: 20px; margin-left: 20px"
            />
          </template>
          <app-main />
          <right-panel>
            <settings />
          </right-panel>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import Breadcrumb from "@/components/Breadcrumb";
import RightPanel from "@/components/RightPanel";
import TopNav from "@/components/TopNav";
import centerbar from "@/components/centerbar";

import {
  AppMain,
  Navbar,
  Settings,
  Sidebar,
  TagsView,
  Sidebarblue,
} from "./components";
// import {reproductionImage } from '@/components/reproductionImage'
import ResizeMixin from "./mixin/ResizeHandler";
import { mapState } from "vuex";
import variables from "@/assets/styles/variables.scss";
import axios from "axios";
import navBg from "@/assets/logo/nav_bg.png";
import bus from "@/utils/bus";
export default {
  name: "Layout",
  components: {
    Sidebarblue,
    Breadcrumb,
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView,
    TopNav,
    centerbar,
    // reproductionImage
  },
  data() {
    return {
      // this.$cache.local.get("navigationBar")
      titleHeader: this.$cache.local.get("navigationBar"),
      evtDialogOneThing: false,
      badgeHidden: true,
      eventValue: 0,
      mapStyle: "",
      // 天气
      weather_weather: "",
      // 天气图标
      weather_weatherimg: "",
      navBg: navBg,
      is_weather: null,
      is_breadcrumb: null,
      tunnelStyle: null,
      eventDialogPic: true,
      eventDialogTable: false,
      evtDialogVideo: false,
      routePath: [
        "/index",
        "/map/map/index",
        "/emergency/administration/dispatch",
        "/map/map3d/index",
        "/energy",
      ],
    };
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
      theme: (state) => state.settings.theme,
      sideTheme: (state) => state.settings.sideTheme,
      sidebar: (state) => state.app.sidebar,
      device: (state) => state.app.device,
      needTagsView: (state) => state.settings.tagsView,
      weatherView: (state) => state.settings.weatherView,
      fixedHeader: (state) => state.settings.fixedHeader,
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === "mobile",
      };
    },
    variables() {
      return variables;
    },
    topNav() {
      return this.$store.state.settings.topNav;
    },
    weatherView() {
      return this.$store.state.settings.weatherView;
    },
    fixedHeaderClass() {
      let h = 0;
      if (this.$route.path == "/index") {
        if (this.topNav) {
          if (this.fixedHeader) {
            h = 50;
          }
        } else {
          if (this.fixedHeader) {
            this.needTagsView ? (h = 84) : (h = 50);
          }
        }
      } else {
        if (this.topNav) {
          if (this.fixedHeader) {
            h = 104;
          }
        } else {
          if (this.fixedHeader) {
            this.needTagsView ? (h = 84) : (h = 50);
          }
        }
      }
      return "padding-top:" + h + "px;";
    },
  },
  created() {
    console.log(this.titleHeader,"this.titleHeader");
    console.log(this.$route.path, "路由");
    if (this.$route.path == "/tunnel") {
      if (
        this.sideTheme == "theme-blue" ||
        (this.sideTheme == "theme-dark" && this.topNav) ||
        (this.sideTheme == "theme-light" && this.topNav)
      ) {
        this.tunnelStyle = "margin-left:0px;background-color:#181b2c;";
      } else {
        this.tunnelStyle = "margin-left:0px;";
      }
    } else {
      this.tunnelStyle = "margin-left:0px;";
    }
  },
  methods: {
    // openEventTitleDialog() {
    //   if (this.eventDialogTable == false) {
    //     bus.$emit("openTableDialog");
    //     this.eventDialogTable = true;
    //   }
    // },
    getRoute(path) {
      // console.log(path,"path");
      var arr = [
        "/index",
        "/map/map/index",
        "/emergency/administration/dispatch",
        "/map/map3d/index",
        "/energy",
        "/map/trafficChart",
        "/map/emphasisCars",
        "/map/deviceMonitor",
        "/map/trackPlayback",
        "/map/analyse",
        "/zeroCarbon",
      ];
      if (arr.includes(path)) {
        return "none";
      } else {
        return "block";
      }
    },
    getRoute2(path) {
      var arr = [
        "/index",
        "/map/map/index",
        "/emergency/administration/dispatch",
        "/map/map3d/index",
        "/energy",
        "/map/trafficChart",
        "/map/emphasisCars",
        "/map/deviceMonitor",
        "/map/trackPlayback",
        "/map/analyse",
      ];
      if (arr.includes(path)) {
        return true;
      } else {
        return false;
      }
    },
    // style="$route.path == '/index'?'display:none;':$route.path == '/map/map/index' ?
    //           'display:none;':$route.path == '/emergency/administration/dispatch' ?
    //           'display:none;':$route.path == '/map/map3d/index'?'display:none;':''"
    getWeather() {
      let city = "city=济南";
      let word = "tianqi";
      const key = "key=0c7ebab2461621aeb2c34b3a82e4c702";
      const header = "http://api.tianapi.com/txapi";
      const url = `${header}/${word}/?${key}&${city}`;
      axios.get(url).then((res) => {
        if (res.data.newslist) {
          const data = res.data.newslist[0];
          this.weather_weather =
            data.area +
            ":" +
            data.weather +
            data.lowest +
            data.wind +
            data.windsc;
          this.weather_weatherimg = require("@/assets/weather/" +
            data.weatherimg +
            "");
        }
      });
    },
    handleClickOutside() {
      this.$store.dispatch("app/closeSideBar", { withoutAnimation: false });
    },
  },
  watch: {
    sideTheme(val) {
      document.getElementsByTagName("body")[0].className = val;
    },
    sdEventList(event) {
      if (event.length > 0) {
        this.evtDialogOneThing = true;
      }
      // this.eventValue += event.length;
      // if (this.eventValue > 0) {
      //   this.$forceUpdate();
      //   // this.badgeHidden = false;
      //   this.eventDialogPic = true;

      // }
    },
  },
  mounted() {
    if (this.weatherView == undefined) {
      this.weatherView = false;
    }
    this.getWeather();
    document.getElementsByTagName("body")[0].className = this.sideTheme;
    this.is_breadcrumb = systemConfig.navBarShow(systemConfig.systemType)[
      "breadcrumb"
    ];
    // 打开列表弹窗
    bus.$on("openTableDialog", () => {
      this.eventDialogTable = true;
    });
    // 关闭列表弹窗
    bus.$on("closeDialog", () => {
      console.log("关闭一件事弹窗");
      this.eventDialogTable = false;
      this.evtDialogOneThing = false;
    });
    // 打开三图一视弹窗
    bus.$on("openPicDialog", () => {
      this.eventDialogPic = true;
    });
    // 关闭三图一视弹窗
    bus.$on("closePicDialog", () => {
      this.eventDialogPic = false;
    });
    // 打开视频弹窗
    bus.$on("openVideoDialog", () => {
      this.evtDialogVideo = true;
    });
    // 关闭视频弹窗
    bus.$on("closeVideoDialog", () => {
      this.evtDialogVideo = false;
    });
    // 事件表格忽略后 右上角数字跟着改
    // bus.$on("getEvtList", () => {
    //   this.eventValue = this.eventValue - 1;
    //   if (this.eventValue == 0) {
    //     this.badgeHidden = false;
    //   }
    // });
  },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/mixin.scss";
@import "~@/assets/styles/variables.scss";
// 区分不同主题下导航栏颜色
.mapBox {
  position: fixed;
  top: 0px;
  left: 0px;
  width: 100%;
  z-index: 999;
}
// .theme-light-navbar {
//   background-color: white;
// }
.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  // overflow: hidden;
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}
.topNav_head .has-logo {
  background: transparent !important;
}
.has-logo {
  background: rgb(0, 67, 117);
}
.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  // transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.mobile .fixed-header {
  width: 100%;
}
// .theme-light .topNav_head{background-color: #ffffff}
.topNavHeadBlue {
  box-shadow: 2px 0 6px rgb(0 21 41 / 35%);
  background-size: 100% 100%;
  width: 100%;
}
.topNavHeadBlue .index_menu .theme-blue {
  background-color: unset !important;
}
.theme-blue-navbar {
  box-shadow: unset;
}
.theme-blue {
  .theme-blue-navbar {
    background-color: unset;
  }
}
.weather p {
  color: white;
  height: 50px;
  line-height: 50px;
  text-indent: 10px;
  white-space: nowrap;
  font-size: 14px;
}
.weather img {
  width: 25px;
  height: 25px;
}
.weather {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 11%;
  float: left;
}
.theme-blue-box {
  height: 100%;
}
.theme-light {
  .weather {
    p {
      color: #000000;
    }
  }
}
.eventIcon {
  position: absolute;
  top: calc(100% - 50px);
  left: calc(100% - 60px);
  width: 40px;
  height: 40px;
  border-radius: 20px;
  z-index: 100;
  background: linear-gradient(177deg, #3b87bb, #2b65ae);
  text-align: center;
  line-height: 40px;
  color: white;
}
.el-icon-s-order:before {
  font-size: 24px;
}
.main-container > div {
  background: rgb(0, 67, 117);
}
</style>
