<template>
  <div :class="{ 'has-logo': showLogo }" :style="{
      height: topNav ? '7.6vh' : '100%',
    }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <div :class="topNav ? 'workbenchNavbar' : 'workbenchSidbar'" :style="{
        color:
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor,
      }" v-if="manageStation == '0'">
      <router-link to="/index">
        <span v-if="topNav || !isCollapse">工作台</span>
      </router-link>
    </div>
    <div :class="topNav ? 'selectNavbar' : 'selectSidbar'" :style="{
        color:
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor,
      }" v-if="manageStation == '1'">
      <el-select v-model="manageStationSelect" size="small" class="topNavSelect" :popper-append-to-body="false">
        <el-option v-for="item in manageStationList" :key="item.tunnelId" :label="item.tunnelName"
          :value="item.tunnelId" @click.native="changeNavSelect(item.tunnelId)" />
      </el-select>
    </div>
    <template v-if="topNav">
      <!-- <el-tooltip
        class="item"
        effect="dark"
       content="点击展示更多导航"
        placement="left"
      > -->
      <i class="el-icon-arrow-left" @click="moveMethod('left')"
        :style="{ visibility: leftIcon ? 'visible' : 'hidden' }"></i>
      <!-- </el-tooltip> -->
    </template>
    <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper"
      :style="topNav ? 'width:55%;height:100%;' : ''" ref="scroll">
      <el-menu ref="currentNav" :default-active="activeMenu" :collapse="isCollapse" :text-color="
            settings.sideTheme === 'theme-dark'
              ? variables.menuColor
              : variables.menuLightColor
          " :unique-opened="true" :active-text-color="settings.theme" :collapse-transition="true"
        :mode="topNav ? 'horizontal' : 'vertical'" :style="
            topNav ? 'background:transparent !important' : 'white-space:unset;'
          ">
        <sidebar-item v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route"
          :base-path="route.path" :class="topNav ? 'menu-item is_top' : 'menu-item is_left'"
          :style="topNav ? style : 'width:100%;'" style="caret-color: rgba(0,0,0,0);user-select: none;" />
      </el-menu>
    </el-scrollbar>

    <template v-if="topNav">
      <!-- <el-tooltip
        class="item"
        effect="dark"
       content="点击展示更多导航"
        placement="right"
      > -->
      <i class="el-icon-arrow-right" @click="moveMethod('right')"
        :style="{ visibility: rightIcon ? 'visible' : 'hidden' }"></i>
      <!-- </el-tooltip> -->
    </template>
  </div>
</template>

<script>
  import {
    mapGetters,
    mapState
  } from "vuex";
  import Logo from "./Logo";
  import TopNav from "@/components/TopNav";
  import SidebarItem from "./SidebarItem";
  import variables from "@/assets/styles/variables.scss";
  import {
    getJlyTunnel
  } from "@/api/equipment/tunnel/api.js";
  export default {
    components: {
      SidebarItem,
      Logo,
      TopNav
    },
    computed: {
      ...mapState(["settings"]),
      ...mapGetters(["sidebarRouters", "sidebar"]),
      activeMenu() {
        // debugger
        const route = this.$route;
        const {
          meta,
          path
        } = route;
        // if set path, the sidebar will highlight the path you set
        if (meta.activeMenu) {
          return meta.activeMenu;
        }
        return path;
      },
      showLogo() {
        return this.$store.state.settings.sidebarLogo;
      },
      variables() {
        return variables;
      },
      isCollapse() {
        return !this.sidebar.opened;
      },
      topNav() {
        return this.$store.state.settings.topNav;
      },
    },
    data() {
      return {
        wrapWith: 0,
        navWidth: 0,
        style: null,
        path: null,
        leftIcon: false,
        rightIcon: true,
        manageStationSelect: "",
        manageStationList: [],
        manageStation: this.$cache.local.get("manageStation"),
      };
    },
    watch: {
      "$store.state.manage.manageStation": function (newVal, oldVal) {
        this.$cache.local.set("manageStation", newVal);

        this.manageStation = newVal;
        this.$forceUpdate();
      },
      "$store.state.manage.manageStationSelect": function (newVal, oldVal) {

        this.$cache.local.set("manageStationSelect", newVal);

        if (newVal == "JQ-WeiFang-JiuLongYu-HSD") {
          window.location = "http://10.7.187.28:82/WorkBench?userId=8";
        }
      },
      $route() {
        // this.addTags()
        // debugger
      },
    },
    mounted() {
      // 当前导航栏子元素数量
      const childrenLength = this.$refs.currentNav.$el["childElementCount"];
      // 导航栏菜单
      if (childrenLength > 6) {
        this.style = "width:149.2px;";
      } else {
        this.style = "width:20%;";
      }

      // 初始化

      this.wrapWith = childrenLength * 149.2; //总长度
      let wrap = this.$refs.scroll.$refs.wrap;
      let rollWidth = this.wrapWith - Math.abs(wrap.scrollLeft);
      this.rightIcon = rollWidth - 149.2 < wrap.offsetWidth ? false : true;

    },
    async created() {
      await getJlyTunnel().then((res) => {
        console.log(res, "getJlyTunnel")
        this.manageStationList = res.data;
        var test = window.location.href;
        console.log(this.$cache.local.get(
          "manageStationSelect",
        ), "get manageStationSelect")
        if (test.substr(test.length - 1, 1) == "1") {
          this.$store.dispatch(
            "manage/changeTunnelId",
            "JQ-WeiFang-JiuLongYu-JJL"
          );
          this.$cache.local.set(
            "manageStationSelect",
            "JQ-WeiFang-JiuLongYu-JJL"
          );
          this.manageStationSelect = "JQ-WeiFang-JiuLongYu-JJL";
        } else if (test.substr(test.length - 1, 1) == "2") {
          this.changeNavSelect("JQ-WeiFang-JiuLongYu-MAS");
          this.manageStationSelect = "JQ-WeiFang-JiuLongYu-MAS";
        } else {
          if (this.$cache.local.get("manageStationSelect")) {

            let str = ""
            for (let i = 0; i < this.manageStationList.length; i++) {
              let item = this.manageStationList[i]

              if (item.tunnelId == this.$cache.local.get("manageStationSelect")) {
                let arr = []
                let itm = item.ancestors.split(",")
                arr.push(itm[itm.length - 1])
                arr.push(item.deptId)
                this.manageStationSelect = this.$cache.local.get("manageStationSelect")
                str = arr.toString()
                this.$cache.local.set("deptId", str);
                return
              } else {
                this.manageStationSelect = this.manageStationList[0].tunnelId
                let arr = []
                let itm = this.manageStationList[0].ancestors.split(",")
                arr.push(itm[itm.length - 1])
                arr.push(item.deptId)
                str = arr.toString()
                this.$cache.local.set("deptId", str);

              }
            }
            this.$cache.local.set("manageStationSelect", this.manageStationList[0].tunnelId);
          } else {
            // if (
            //   this.$cache.local.get("manageStationSelect") ==
            //   "JQ-WeiFang-JiuLongYu-HSD"
            // ) {
            //   window.location = "http://10.7.187.28:82/WorkBench?userId=8";
            // } else {
            this.manageStationSelect = res.data[0].tunnelId;
            this.$cache.local.set("manageStationSelect", res.data[0].tunnelId);
            // }
          }
        }
      });
    },
    methods: {
      changeNavSelect(val) {
        this.$store.dispatch("manage/changeTunnelId", val);
        this.$cache.local.set("manageStationSelect", val);
        let arr = []
        for (let item of this.manageStationList) {
          if (val == item.tunnelId) {
            let itm = item.ancestors.split(",")
            arr.push(itm[itm.length - 1])
            arr.push(item.deptId)
          }
        }
        this.$cache.local.set("deptId", arr.toString());
      },
      moveMethod(flag) {
        let wrap = this.$refs.scroll.$refs.wrap;
        if (flag == 'left') {
          wrap.scrollLeft = wrap.scrollLeft - 149.2;
        } else if (flag == 'right') {
          wrap.scrollLeft = wrap.scrollLeft + 149.2;
        }
        let rollWidth = this.wrapWith - Math.abs(wrap.scrollLeft);
        this.rightIcon = Math.abs(rollWidth - 149.2) < wrap.offsetWidth ? false : true;
        this.leftIcon = wrap.scrollLeft == 0 ? false : true;

      },
      changeScroll(e) {
        let wrap = this.$refs.scroll.$refs.wrap;
        wrap.scrollLeft = wrap.scrollLeft - e.wheelDelta;
      },
      addTags() {
        // debugger
        const {
          name
        } = this.$route
        if (name) {
          this.$store.dispatch('tagsView/addView', this.$route)
        }
        return false
      },
    },
  };

</script>
<style lang="scss" scope>
  .hideSidebar .el-menu .menu-item .el-submenu__title .el-icon-arrow-right {
    display: none;
  }

  .el-menu {
    background-color: unset;
  }

  #el-scrollbar-top {
    width: 100%;
    height: 65px !important;
    left: unset;
    top: unset;
    bottom: unset;
    position: unset;
  }

  .el-men-flex {
    display: flex;
    justify-content: left;
    align-items: center;
  }

  .el-scrollbar__wrap {
    // width: 100%;
  }

  // .theme-light .el-scrollbar__wrap{background-color:white;}
  // .theme-light #app .sidebar-container{background-color: white;}
  .theme-light,
  .theme-dark,
  .theme-blue {
    .el-scrollbar {
      .is-vertical {
        display: none;
      }
    }

    .index_menu {
      position: relative;

      .el-icon-arrow-left,
      .el-icon-arrow-right {
        font-size: 16px;
        cursor: pointer;
      }

      .el-icon-arrow-left {
        color: white;
        margin-left: 20px;
      }

      .el-icon-arrow-right {
        color: white;
        margin-left: 40px;
      }

      a {
        display: unset !important;
      }
    }

    .el-scrollbar__view {
      // width: 100%;
      height: 100%;
    }

    .el-scrollbar {
      .el-scrollbar__view {
        .el-menu {
          white-space: nowrap;

          .menu-item {
            display: inline-block;
            vertical-align: bottom;
          }
        }
      }
    }

    .right-menu {
      .el-icon-caret-bottom {
        display: none;
      }
    }
  }

  .theme-light {

    .index_menu .el-icon-arrow-left,
    .index_menu .el-icon-arrow-right {
      font-size: 16px;
      cursor: pointer;
    }

    .index_menu .el-icon-arrow-left {
      color: #fff !important;
    }

    .index_menu .el-icon-arrow-right {
      color: #fff !important;
    }
  }

  // .theme-dark #app .sidebar-container .el-menu-item, .theme-dark #app .sidebar-container .el-submenu__title {
  //     color: white !important;
  // }
  .theme-dark #app .topNav_head .sidebar-container .theme-dark .is-active>.el-submenu__title,
  .theme-dark #app .topNav_head .sidebar-container .el-menu-item.is-active {
    color: #ffdb82 !important;
    // text-shadow: 1px 1px white;
    background-image: url(../../../assets/cloudControl/navBg2.png) !important;
    background-repeat: no-repeat !important;
    background-position: 70% 52% !important;
  }

  .theme-dark #app .topNav_head .workbenchNavbar .router-link-active {
    color: #ffdb82 !important;
    //    text-shadow: 1px 1px white;
    background-image: url(../../../assets/cloudControl/navBg2.png) !important;
    background-repeat: no-repeat;
    background-position: 100% 58%;
    height: 7.6vh;
    line-height: 7.6vh;
  }

  .theme-blue #app .topNav_head .sidebar-container .theme-blue .is-active>.el-submenu__title,
  .theme-blue #app .topNav_head .sidebar-container .el-menu-item.is-active {
    color: #ffdb82 !important;
    // text-shadow: 1px 1px white;
    background-image: url(../../../assets/cloudControl/navBg2.png) !important;
    background-repeat: no-repeat;
    background-position: 70% 52%;
  }

  .theme-blue #app .topNav_head .workbenchNavbar .router-link-active {
    color: #ffdb82 !important;
    // text-shadow: 1px 1px white;
    background-image: url(../../../assets/cloudControl/navBg2.png) !important;
    background-repeat: no-repeat;
    background-position: 100% 58%;
    height: 7.6vh;
    line-height: 7.6vh;
  }

  .theme-light #app .topNav_head .el-menu--horizontal .is-active>.el-submenu__title,
  .theme-light #app .topNav_head .sidebar-container .el-menu-item.is-active,
  .theme-light #app .topNav_head .sidebar-container .theme-light .is-active>.el-submenu__title,
  .theme-light #app .topNav_head .workbenchNavbar .router-link-active {
    color: #ffdb82 !important;
    background-image: url(../../../assets/cloudControl/navBg2.png) !important;
    background-repeat: no-repeat;
    background-position: 70% 52%;
    height: 7.6vh;
    line-height: 7.6vh;
  }

  // 工作台按钮样式
  .workbenchNavbar,
  .workbenchSidbar {

    // padding-left: 20px;
    a {
      height: 100%;
      font-size: 0.8vw;
      line-height: 7.6vh;
      padding-left: 2vw;
    }
  }

  .selectNavbar,
  .selectSidbar {
    margin-left: 20px;
    position: relative;
    width: 200px;
    height: 40px;
    line-height: 40px;

    .el-input__inner {
      background: transparent !important;
      border: solid 1px transparent !important;
      color: white !important;
      padding-left: 30px;
      width: 150px;
    }
  }

  .selectNavbar:after {
    position: absolute;
    content: "";
    z-index: -1;
    /*堆叠层推到宿主元素后面，避免遮住内容*/
    background: url(../../../assets/cloudControl/topNavSelect.png);
    left: 0;
    top: -2px;
    bottom: 0;
    right: 0;
    transform: skew(-17deg);
    width: 170px;
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    height: 40px;
  }

  .workbenchNavbar {
    width: 10%;
    float: left;
    text-align: left;
    margin-left: 5vw;

    span {
      margin-left: 1vw;
      // padding-right: 30px;
    }
  }

  .workbenchSidbar {
    width: 100%;

    span {
      margin-left: 15px;
    }
  }

  .is_top {
    height: 100%;

    .el-submenu {
      height: 100%;
    }
  }

</style>
<style lang="scss">
  .topNavSelect {
    .el-select-dropdown {
      background: #165ba2 !important;
      border-radius: 14px;
      border: solid 1px transparent;
      padding: 5px 10px;
      min-width: 160px;

      .el-select-dropdown__list {
        padding: 0;
      }

      .el-scrollbar__view {
        // width: 100% !important;
        color: white;
      }

      .el-select-dropdown__item.selected,
      .el-select-dropdown__item {
        color: white;
        height: 50px;
        line-height: 50px;
        // padding: 0 27px;
        text-align: center;
      }

      .el-select-dropdown__item.hover,
      .el-select-dropdown__item:hover {
        background-color: #2971cc;
        border-radius: 8px;
      }
    }

    .el-popper[x-placement^="bottom"] .popper__arrow {
      display: none;
    }
  }

</style>
