<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link class="nullchildren" style="height:100%;" v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item style="height:50px;line-height:50px;text-align:center;" :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item-blue v-if="onlyOneChild.meta" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>
    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body class="children">
      <template slot="title" style="height:50px;">
        <item-blue v-if="item.meta" :title="item.meta.title"/>
      </template>
      <sidebar-item-blue
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import ItemBlue from './ItemBlue'
import AppLink from './LinkBlue'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItemBlue',
  components: { ItemBlue, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath, routeQuery) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      if (routeQuery) {
        let query = JSON.parse(routeQuery);
        return { path: path.resolve(this.basePath, routePath), query: query }
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
<style>
  .el-submenu .el-menu{flex-flow: column;}
  .theme-blue .el-submenu .el-submenu__title .el-icon-arrow-down:before{content:unset;}
  .theme-blue .el-scrollbar__view .el-menu div .el-submenu .el-submenu__title span{color:#fff;}
  /* .theme-blue .el-scrollbar__view .el-menu div .el-submenu, .is-active .el-submenu__title span{color:#fff !important;} */

  .theme-blue .el-scrollbar__view .el-menu div .el-submenu .is-active span{color:white;}
  .theme-blue .el-scrollbar__view .el-menu div .el-submenu .is-active .el-submenu__title span{color: #004a77;}
  .theme-blue .el-scrollbar .el-scrollbar__view .el-menu .nullchildren .is-active{position: relative;}
  .theme-blue .el-scrollbar .el-scrollbar__view .el-menu .nullchildren .is-active::after{
      content:'';
      position:absolute;
      top:22px;
      left:20%;
      width:4px;
      height:4px;
      background-color: #3acbf4;
      border-radius: 25px;
  }
  .theme-blue .el-scrollbar .el-scrollbar__view .el-menu .nullchildren .is-active::before{
      content:'';
      position:absolute;
      top: 22px;
      right: 20%;
      width:4px;
      height:4px;
      background-color: #3acbf4;
      border-radius: 25px;
  }
  .theme-blue .el-menu.el-menu--horizontal{border:unset;}
  /* .theme-blue .el-menu--horizontal .el-menu .el-submenu__title{
    background-color:rgb(19, 103,150);
    text-align:center;
    color: white!important;
  } */
  /* .theme-blue .el-menu--horizontal .el-menu .el-submenu__title:hover{
    background-color:#0d6da7!important;
  } */
  .theme-blue .el-menu--horizontal .el-menu .nest-menu .children .el-submenu__title{height:52px;line-height:51px;}
  .theme-blue .el-scrollbar .el-scrollbar__view .el-menu .is-active{color: #f19f39!important;}
</style>