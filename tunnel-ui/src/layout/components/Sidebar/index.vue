<template>
  <div
    :class="{ 'has-logo': showLogo }"
    :style="{
      height: topNav ? '72px' : '100%',
    }"
  >
    <logo v-if="showLogo" :collapse="isCollapse" />
    <div
      :class="topNav ? 'workbenchNavbar' : 'workbenchSidbar'"
      :style="{
        color:
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor,
      }"
    >
      <router-link to="/index">
        <!-- <i class="el-icon-setting"></i> -->
        <span v-if="topNav || !isCollapse">工作台</span>
      </router-link>
    </div>
    <template v-if="topNav">
      <el-tooltip
        class="item"
        effect="dark"
        content="点击展示更多导航"
        placement="left"
      >
        <i class="el-icon-arrow-left" @click="prevScroll"></i>
      </el-tooltip>
    </template>

    <el-scrollbar
      :class="settings.sideTheme"
      wrap-class="scrollbar-wrapper"
      :style="topNav ? 'width:55%;height:100%;' : ''"
      ref="scroll"
    >
      <el-menu
        ref="currentNav"
        :default-active="activeMenu"
        :collapse="isCollapse"
        :text-color="
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor
        "
        :unique-opened="true"
        :active-text-color="settings.theme"
        :collapse-transition="true"
        :mode="topNav ? 'horizontal' : 'vertical'"
        :style="topNav ? '' : 'white-space:unset;'"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
          :class="topNav ? 'menu-item is_top' : 'menu-item is_left'"
          :style="topNav ? style : 'width:100%;'"
        />
      </el-menu>
    </el-scrollbar>
    <template v-if="topNav">
      <el-tooltip
        class="item"
        effect="dark"
        content="点击展示更多导航"
        placement="right"
      >
        <i class="el-icon-arrow-right" @click="nextScroll"></i>
      </el-tooltip>
    </template>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import TopNav from "@/components/TopNav";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
  components: { SidebarItem, Logo, TopNav },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
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
      style: null,
      path: null,
    };
  },
  mounted() {
    // 当前导航栏子元素数量
    const childrenLength = this.$refs.currentNav.$el["childElementCount"];
    // 导航栏菜单
    if (childrenLength > 6) {
      this.style = "min-width:20.7%;";
    } else {
      this.style = "width:20%;";
    }
    console.log(this.sidebarRouters, "sidebarRouters");
  },
  methods: {
    prevScroll() {
      let wrap = this.$refs.scroll.$refs.wrap;
      wrap.scrollLeft = wrap.scrollLeft - 150;
    },
    nextScroll() {
      let wrap = this.$refs.scroll.$refs.wrap;
      wrap.scrollLeft = wrap.scrollLeft + 150;
    },
    changeScroll(e) {
      let wrap = this.$refs.scroll.$refs.wrap;
      wrap.scrollLeft = wrap.scrollLeft - e.wheelDelta;
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
    width: 100%;
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
.theme-dark
  #app
  .topNav_head
  .sidebar-container
  .theme-dark
  .is-active
  > .el-submenu__title,
.theme-dark #app .topNav_head .sidebar-container .el-menu-item.is-active {
  color: #ffdb82 !important;
  // text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg2.png) !important;
  background-repeat: no-repeat;
  background-position: 70% 52%;
}
.theme-dark #app .topNav_head .workbenchNavbar .router-link-active {
  color: #ffdb82 !important;
  //    text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg2.png) !important;
  background-repeat: no-repeat;
  background-position: 100% 58%;
  height: 72px;
  line-height: 72px;
}
.theme-blue
  #app
  .topNav_head
  .sidebar-container
  .theme-blue
  .is-active
  > .el-submenu__title,
.theme-blue #app .topNav_head .sidebar-container .el-menu-item.is-active {
  color: #f19f39 !important;
  text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg.png) !important;
  background-repeat: no-repeat;
  background-position: 70% 52%;
}
.theme-blue #app .topNav_head .workbenchNavbar .router-link-active {
  color: #f19f39 !important;
  text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg.png) !important;
  background-repeat: no-repeat;
  background-position: 100% 58%;
  height: 72px;
  line-height: 72px;
}
.theme-light
  #app
  .topNav_head
  .el-menu--horizontal
  .is-active
  > .el-submenu__title,
.theme-light #app .topNav_head .sidebar-container .el-menu-item.is-active,
.theme-light
  #app
  .topNav_head
  .sidebar-container
  .theme-light
  .is-active
  > .el-submenu__title,
.theme-light #app .topNav_head .workbenchNavbar .router-link-active {
  color: #ffdb82 !important;
  background-image: url(../../../assets/cloudControl/navBg2.png) !important;
  background-repeat: no-repeat;
  background-position: 70% 52%;
  height: 72px;
  line-height: 72px;
}

// 工作台按钮样式
.workbenchNavbar,
.workbenchSidbar {
  // padding-left: 20px;
  a {
    height: 100%;
    font-size: 16px;
    line-height: 72px;
    padding-left: 26px;
  }
}
.workbenchNavbar {
  width: 10%;
  float: left;
  text-align: left;
  margin-left: 100px;

  span {
    margin-left: 10px;
    padding-right: 30px;
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
