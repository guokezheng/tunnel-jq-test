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
      v-if="manageStation == '0'"
    >
      <router-link to="/index">
        <span v-if="topNav || !isCollapse">工作台</span>
      </router-link>
    </div>
    <div
      :class="topNav ? 'selectNavbar' : 'selectSidbar'"
      :style="{
        color:
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor,
      }"
      v-if="manageStation == '1'"
    >
     
        <el-select
          v-model="manageStationSelect"
          size="small"
          class="topNavSelect"
          :popper-append-to-body="false"
        >
          <el-option
            v-for="item in manageStationList"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
           @click.native="changeNavSelect(item.tunnelId)"
          />
        </el-select>
     
    </div>
    <template v-if="topNav">
      <el-tooltip
        class="item"
        effect="dark"
        content="点击展示更多导航"
        placement="left"
      >
        <i
          class="el-icon-arrow-left"
          @click="prevScroll"
          :style="{ visibility: leftIcon ? 'visible' : 'hidden' }"
        ></i>
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
        :style="
          topNav ? 'background:transparent !important' : 'white-space:unset;'
        "
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
        <i
          class="el-icon-arrow-right"
          @click="nextScroll"
          :style="{ visibility: rightIcon ? 'visible' : 'hidden' }"
        ></i>
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
import { getJlyTunnel } from "@/api/equipment/tunnel/api.js";
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
      leftIcon: false,
      rightIcon: true,
      manageStationSelect: "",
      manageStationList: [],
      manageStation: this.$cache.local.get("manageStation"),
    };
  },
  watch: {
    "$store.state.manage.manageStation": function (newVal, oldVal) {
      console.log(newVal, "监听到模式啦监听到模式啦监听到模式啦监听到模式啦");
      this.$cache.local.set("manageStation",newVal);
      //this.manageStationSelect = "JQ-WeiFang-JiuLongYu-HSD";
      this.manageStation = newVal
      this.$forceUpdate();
    },
    // manageStationSelect(val){
    //   console.log(val,"watch")
    //   console.log(val,"0000000000");
    //   this.$store.dispatch("manage/changeTunnelId", val);
    //   this.$cache.local.set("manageStationSelect",val)
    //   if(val != 'JQ-WeiFang-JiuLongYu-HSD'){
    //     this.$cache.local.set("manageStation",'0')
    //     this.manageStation = '0'
    //   }
    //   this.$forceUpdate()
    // }
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
  created() {
    getJlyTunnel().then((res) => {
      this.manageStationList = res.data;
      this.manageStationSelect = res.data[0].tunnelId;
      this.$cache.local.set("manageStationSelect",res.data[0].tunnelId)
    });
  },
  methods: {
    changeNavSelect(val) {
      this.$store.dispatch("manage/changeTunnelId", val);
      this.$cache.local.set("manageStationSelect",val)
      // if(val != 'JQ-WeiFang-JiuLongYu-HSD'){
      //   this.$cache.local.set("manageStation",'0')
      //   // this.manageStation = '0'
      // }
    },
    prevScroll() {
      console.log(111);
      let wrap = this.$refs.scroll.$refs.wrap;
      wrap.scrollLeft = wrap.scrollLeft - 150;
      console.log(wrap.scrollLeft);
      if (wrap.scrollLeft == 0) {
        this.leftIcon = false;
        this.rightIcon = true;
      } else {
        this.leftIcon = true;
        this.rightIcon = true;
      }
    },
    nextScroll() {
      let wrap = this.$refs.scroll.$refs.wrap;
      wrap.scrollLeft = wrap.scrollLeft + 150;
      console.log(wrap.scrollLeft);
      if (wrap.scrollLeft == 320) {
        this.rightIcon = false;
        this.leftIcon = true;
      } else {
        this.rightIcon = true;
        this.leftIcon = true;
      }
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
  color: #ffdb82 !important;
  // text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg2.png) !important;
  background-repeat: no-repeat;
  background-position: 70% 52%;
}
.theme-blue #app .topNav_head .workbenchNavbar .router-link-active {
  color: #ffdb82 !important;
  text-shadow: 1px 1px white;
  background-image: url(../../../assets/cloudControl/navBg2.png) !important;
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
.selectNavbar,
.selectSidbar {
 margin-left: 20px;
 position: relative;
 width: 200px;
 height: 40px;
 line-height: 40px;
 .el-input__inner{
  background: transparent !important;
  border:solid 1px transparent !important;
  color: white !important;
  padding-left: 30px;
  width: 150px;
 }
}
.selectNavbar:after{
	position: absolute;
	content: '';
	z-index: -1;/*堆叠层推到宿主元素后面，避免遮住内容*/
	background: url(../../../assets/cloudControl/topNavSelect.png);
	left: 0;top: -2px;bottom: 0;right: 0;
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
<style lang="scss">
.topNavSelect{
  .el-select-dropdown{
    background: #165BA2 !important;
    border-radius:14px;
    border: solid 1px transparent;
    padding: 5px 10px;
    min-width:160px;
    .el-scrollbar__view{
      width:88% !important;
      color:white;
    }
    .el-select-dropdown__item.selected,.el-select-dropdown__item{
      color:white;
      height: 50px;
      line-height: 50px;
      padding:0 27px;
    }
    .el-select-dropdown__item.hover, .el-select-dropdown__item:hover{
      background-color: #2971CC;
      border-radius: 8px;
    }
  }
  .el-popper[x-placement^=bottom] .popper__arrow{
    display: none;
  }
}
</style>
