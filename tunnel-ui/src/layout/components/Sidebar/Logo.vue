<template>
  <div class="sidebar-logo-container" :id="topNav?'index_logo':''" :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' ? '#004375' : '#004375' }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="topNav?logo:collapse?zclogo:logo" :class="topNav?'sidebar-logo':collapse?'zcSidebar-logo':'sidebar-logo'" />
        <!-- <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1> -->
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <!-- <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1> -->
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import zclogoImg from '@/assets/logo/sdgsLogo.png'

import variables from '@/assets/styles/variables.scss'
import TopNav from '@/components/TopNav'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      return variables;
    },
	  sideTheme() {
      return this.$store.state.settings.sideTheme
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  data() {
    return {
      title: null,
      zclogo: zclogoImg,
      logo: logoImg,

    }
  },
  created() {
    this.title = systemConfig.title(systemConfig.systemType);
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 72px;
  line-height: 72px;
  background: #2b2f3a;
  text-align: center;
  // overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 100%;
      // height: 23px;
      vertical-align: middle;
      margin-right: 12px;
    }
    & .zcSidebar-logo{
      width: 34px;
      height: 34px;
      vertical-align: middle;
    }
    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
#index_logo{
  width:31%;
  padding-left: 20px;
}
</style>
