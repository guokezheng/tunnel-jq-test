<!--
 * @Author: zbguopeng 18053314396@163.com
 * @Date: 2022-06-23 11:07:04
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-27 20:23:48
 * @FilePath: \tunnel-ui\src\layout\components\AppMain.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <section :class="path == '/index' ? '' : 'app-main'">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
import TopNav from "@/components/TopNav";
export default {
  name: "AppMain",
  components: {
    TopNav,
  },
  data() {
    return {
      path: "",
    };
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews;
    },
    key() {
      this.path = this.$route.path;
      return this.$route.path;
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
  },
};
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  // height: calc(100vh - 107px) !important;
  width: 100%;
  position: relative;
  // 顶部
  // overflow: hidden;
}

.fixed-header + .app-main {
  // padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    // height: 100%;
    // overflow: hidden;
  }

  .fixed-header + .app-main {
    // padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 17px;
  }
}
</style>
