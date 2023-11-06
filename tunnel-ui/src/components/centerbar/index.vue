<template>
  <div class="topNavBox">
    <div style="width: 125px; height: 37px; display: flex">
      <screenfull
        :id="sideTheme == 'theme-dark' ? 'top_icon' : 'screenfull'"
        class="right-menu-item hover-effect white_icon"
      />
      <el-dropdown class="right-menu-item bell">
        <el-dropdown-menu style="display: none"></el-dropdown-menu>
        <el-tooltip content="监控一体化大屏" effect="dark" placement="bottom">
          <i
            class="el-icon-s-platform homefun right-menu-item hover-effect white_icon"
            @click="bigScreenfun2"
            style="height: 7.6vh; line-height: 7.6vh; font-size: 2.2vh"
            title="监控一体化大屏"
          ></i>
        </el-tooltip>
      </el-dropdown>
      <!-- <el-badge
        :value="nodealNum"
        :hidden="nodealNum > 0 ? false : true"
        class="item bell_icon"
      >
        <img
          src="../../assets/image/evtNum.png"
          style="height: 2.6vh"
          @click="bell()"
        />
      </el-badge> -->
    </div>
    <el-scrollbar
      :class="sideTheme"
      wrap-class="scrollbar-wrapper"
      style="width: 100%; height: 100%"
      ref="scroll"
    >
      <el-menu
        ref="currentNav"
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        active-text-color="#fff"
        :collapse-transition="true"
        mode="horizontal"
        style="background: transparent !important"
      >
        <div class="leftNavBox">
          <div
            class="workBenchNav"
            :style="{ width: userName == 'admin' ? '135px' : '160px' }"
          >
            <router-link to="/index" tag="div">
              <span>工作台</span>
            </router-link>
          </div>
          <sidebar-item
            v-for="(route, index) in sidebarRouters1"
            :key="route.path + index"
            :item="route"
            :base-path="route.path"
            class="menu-item is_top centerNav"
            :style="{ width: userName == 'admin' ? '135px' : '160px' }"
          />
        </div>
        <div class="rightNavBox">
          <sidebar-item
            v-for="(route, index) in sidebarRouters2"
            :key="route.path + index"
            :item="route"
            :base-path="route.path"
            class="menu-item is_top centerNav"
            :style="{ width: userName == 'admin' ? '135px' : '160px' }"
          />
        </div>
      </el-menu>
    </el-scrollbar>
    <div style="width: 135px; height: 37px; display: flex; margin-left: -10px">
      <!-- <el-dropdown class="right-menu-item bell">
        <el-tooltip content="监控一体化大屏" effect="dark" placement="bottom">
          <i
            class="el-icon-s-platform homefun right-menu-item hover-effect white_icon"
            @click="bigScreenfun2"
            style="height: 7.6vh; line-height: 7.6vh; font-size: 2.2vh"
            title="监控一体化大屏"
          ></i>
        </el-tooltip>
      </el-dropdown> -->
      <el-badge
        :value="nodealNum"
        :hidden="nodealNum > 0 ? false : true"
        :max="99"
        class="item bell_icon"
      >
        <img
          src="../../assets/image/evtNum.png"
          style="height: 2.6vh"
          @click="bell()"
        />
      </el-badge>
      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
      >

        <div
          :class="
            sideTheme == 'theme-dark'
              ? 'avatar-wrapper white_icon'
              : 'avatar-wrapper'
          "
          style="display: flex; align-items: center;transform: translateX(10px);"
        >
          <img src="@/assets/images/avatar.png" class="user-avatar" />
          <span style="font-size: 0.7vw">{{ roleGroup }}</span>
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
    </div>
  </div>
</template>
<script>
import { mapGetters, mapState } from "vuex";
import SidebarItem from "@/layout/components/Sidebar/SidebarItem";
import Navbar from "@/layout/components/Navbar.vue";
import Screenfull from "@/components/Screenfull";
import { checkPermi } from "@/utils/permission.js";
import { getUserProfile } from "@/api/system/user";
import bus from "@/utils/bus";
import { getEventUntreatedNum } from "@/api/event/event";
export default {
  computed: {
    ...mapGetters(["sidebarRouters", "sidebar"]),
    ...mapState({
      eventUntreatedNum: (state) => state.websocket.eventUntreatedNum,
      sideTheme: (state) => state.settings.sideTheme,
    }),
    activeMenu() {
      // debugger
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
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
  },
  components: { SidebarItem, Navbar, Screenfull },
  watch: {
    eventUntreatedNum(event) {
      this.nodealNum = event;
    },
  },
  data() {
    return {
      userName: this.$store.state.user.name,
      sidebarRouters1: [],
      sidebarRouters2: [],
      nodealNum: 0,
      roleGroup: "", //角色
      clickSure: false,
    };
  },

  created() {
    this.nodealNum = 0;
    this.getUser();
  },
  mounted() {
    this.getSidebarRouters();

    setInterval(() => {
      this.getNodealNum();
    }, 5000);
  },
  methods: {
    getSidebarRouters() {
      let arr = [];

      // console.log(this.sidebarRouters, "this.sidebarRouters");
      for (let item of this.sidebarRouters) {
        if (!item.hidden && item.path != "/energy" && item.path != "/zeroCarbon" && item.path != "/") {
          arr.push(item);
        }
      }
      console.log(arr, "路由");
      let num = 2;
      if (this.userName == "admin") {
        num = 2;
      } else {
        num = 1;
      }
      for (let i = 0; i < arr.length; i++) {
        if (i <= num) {
          this.sidebarRouters1.push(arr[i]);
        } else {
          this.sidebarRouters2.push(arr[i]);
        }
      }
    },
    getNodealNum() {
      getEventUntreatedNum().then((res) => {
        // console.log(res, "事件总数");
        this.nodealNum = res.data;
      });
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
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
    // 获取当前角色信息
    getUser() {
      getUserProfile().then((response) => {
        this.roleGroup = response.data.nickName;
      });
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
            this.$cache.local.remove("deptId");
            this.$cache.local.remove("currentTunnel", "");
            // location.href = '/#/login';
          });
        })
        .catch(() => {});
    },
    bell() {
      if (!this.clickSure) {
        this.clickSure = true;
        bus.$emit("openTableDialog");
      } else {
        this.clickSure = false;
        bus.$emit("closeDialog");
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.topNavBox {
  height: 37px;
  color: #fff;
  display: flex;
  justify-content: space-between;
  padding: 0 1vw;
  .el-menu.el-menu--horizontal {
    border: none !important;
  }
  .el-menu {
    .leftNavBox {
      display: flex;
      justify-content: space-between;
      width: 25%;
      height: 100%;
      float: left;
      margin-left: 10px;
      .workBenchNav {
        // width: 135px;
        background: url(../../assets/cloudControl/topNavLing.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        transform: scale(-1, 1);
        margin-right: -15px;
        transform: skew(54deg);
        height: 37px;
        line-height: 39px;
        cursor: pointer;
        > div {
          transform: skew(126deg);
          font-size: 0.75vw;
          text-align: center;
          font-family: '光良酒-干杯体';
          // font-weight: bold;
          // text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
          background-image: -webkit-linear-gradient(
            -90deg,
            #e8f7ff 0%,
            #92d6ff 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
        .router-link-exact-active,
        .router-link-active {
          background: url(../../assets/cloudControl/topNavLing_hover.png);
          background-position: center;
          background-repeat: no-repeat;
          background-size: 100% 100%;
          transform: skew(0deg) !important;
          height: 37px;
          line-height: 39px;
          > span {
            display: block;
            transform: skew(-54deg) !important;
            font-family: '光良酒-干杯体';
            // font-weight: bold;
            font-size: 0.75vw;
            background-image: none !important;
            -webkit-text-fill-color: snow;
            text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
          }
        }
      }
      .workBenchNav:hover {
        background: url(../../assets/cloudControl/topNavLing_hover.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        > div {
          background-image: none !important;
          -webkit-text-fill-color: snow;
          text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
        }
      }
      .centerNav {
        caret-color: rgba(0, 0, 0, 0);
        user-select: none;
        background: url(../../assets/cloudControl/topNavLing.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        transform: scale(-1, 1);
        margin-right: -15px;
        transform: skew(54deg);
        ::v-deep .el-submenu__title {
          color: #fff !important;
          height: 37px;
          line-height: 37px;
          text-align: center;
          padding: 0 !important;
          transform: skew(126deg);
          .svg-icon {
            display: none;
          }
          span {
            font-size: 0.75vw;
            font-family: '光良酒-干杯体';
            // font-weight: bold;
            background-image: -webkit-linear-gradient(
              -90deg,
              #e8f7ff 0%,
              #92d6ff 100%
            );
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
        ::v-deep .is-active {
          background: url(../../assets/cloudControl/topNavLing_hover.png) !important;
          background-position: center;
          background-repeat: no-repeat !important;
          background-size: 100% 100% !important;
        }
        ::v-deep .is-active span {
          color: #fff;
          background-image: none;
          -webkit-text-fill-color: snow;
          text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
        }
        ::v-deep .el-submenu__title:hover span {
          color: #fff;
          background-image: none;
          -webkit-text-fill-color: snow;
          text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
        }
      }
      .centerNav:hover {
        background: url(../../assets/cloudControl/topNavLing_hover.png) !important;
        background-position: center;
        background-repeat: no-repeat !important;
        background-size: 100% 100% !important;
      }
    }
    .rightNavBox {
      display: flex;
      justify-content: space-between;
      width: 25%;
      height: 100%;
      margin-right: 34px;
      float: right;
      .centerNav {
        caret-color: rgba(0, 0, 0, 0);
        user-select: none;
        // width: 135px;
        background: url(../../assets/cloudControl/topNavLing.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        margin-right: -15px;
        transform: skew(-20deg);
        ::v-deep .el-submenu__title {
          color: #fff !important;
          height: 37px;
          line-height: 37px;
          text-align: center;
          padding: 0 !important;
          transform: skew(-160deg);
          .svg-icon {
            display: none;
          }
          span {
            font-family: '光良酒-干杯体';
            // font-weight: bold;
            font-size: 0.75vw;

            // text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
            background-image: -webkit-linear-gradient(
              -90deg,
              #e8f7ff 0%,
              #92d6ff 100%
            );
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
        ::v-deep .is-active {
          background: url(../../assets/cloudControl/topNavLing_hover.png) !important;
          background-position: center;
          background-repeat: no-repeat !important;
          background-size: 100% 100% !important;
        }
        ::v-deep .is-active span {
          color: #fff;
          background-image: none;
          -webkit-text-fill-color: snow;
          text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
        }
        ::v-deep .el-submenu__title:hover span {
          color: #fff;
          background-image: none;
          -webkit-text-fill-color: snow;
          text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.5);
        }
      }
      .centerNav:hover {
        background: url(../../assets/cloudControl/topNavLing_hover.png) !important;
        background-position: center;
        background-repeat: no-repeat !important;
        background-size: 100% 100% !important;
      }
    }
  }
  ::v-deep .el-scrollbar {
    .el-scrollbar__wrap {
      overflow: hidden;
    }
  }
}
.right-menu-item {
  display: flex;
  align-items: center;
  padding: 0 4px;
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
  .user-avatar {
    cursor: pointer;
    // width: 30px;
    height: 2.6vh;
    border-radius: 10px;
    margin-right: 8px;
  }
}
.bell_icon {
  cursor: pointer;
  padding: 0 10px 0 0;
  vertical-align: text-bottom;
  color: white !important;
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
  display: flex;
  align-items: center;
}
</style>