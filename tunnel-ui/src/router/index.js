import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/loginjqtunnel',
    component: (resolve) => require(['@/views/loginjqtunnel'], resolve),
    hidden: true
  },
  {
    path: '/cloudControl/index',
    name: 'index2',
    hidden: true,
    meta:
      {
        title: '云管控', icon: 'tab', noCache: true, affix: true,breadcrumb:false
      }
  },
  {
    path: '/tunnelConfig',
    component: (resolve) => require(['@/views/equipment/tunnel/tunnelConfig'], resolve),
    hidden: true
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/register',
    component: (resolve) => require(['@/views/register'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  // {
  //       path: "*",
  //       redirect: "/",
  //       hidden: true
  // },
  /** 支付路由 */
  {
    path: '/PaySuccess',
    component: (resolve) => require(['@/views/payment/example/PaySuccess'], resolve),
    hidden: true
  },
  /** 支付路由结束 */
  /* {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true,breadcrumb:false}
      }
    ]
  }, */
	{
	  path: '',
	  component: Layout,
	  redirect: 'index',
        hidden:true,
	  children: [
	    {
	      /* path: 'index',
	      component: (resolve) => require(['@/views/index'], resolve),
	      name: '首页',
	      meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true } */
	      path: 'index',
	      component: (resolve) => require(['@/views/workbench/config/index'], resolve),
	      name: '工作台',
	      meta: { title: '工作台', icon: 'tab', noCache: true, affix: true,breadcrumb:false}
	    },
      {
        /* path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true } */
        path: 'index2',
        component: (resolve) => require(['@/views/cloudControl/index'], resolve),
        name: '云管控',
        meta: { title: '云管控', icon: 'tab', noCache: true, affix: false,breadcrumb:false}
      }
	  ]
	},
	{
	  path: '/homeIndex',
	  component: Layout,
	  hidden: true,
	  redirect: 'noredirect',
	  children: [
	    {
	      path: 'homeIndex',
	      component: (resolve) => require(['@/views/index'], resolve),
	      name: 'HomeIndex',
	      meta: { title: '首页', icon: 'user' }
	    }
	  ]
	},
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user'}
      }
    ]
  },
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: (resolve) => require(['@/views/system/user/authRole'], resolve),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user'}
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: (resolve) => require(['@/views/system/role/authUser'], resolve),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role'}
      }
    ]
  },
  {
    path: '/electromechanicalPatrol/teamsManage',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'teamsUser/:deptId',
        component: (resolve) => require(['@/views/electromechanicalPatrol/teamsManage/authTeamsUser'], resolve),
        name: 'AuthUser',
        meta: { title: '包含用户', activeMenu: '/electromechanicalPatrol/teamsManage'}
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict'}
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job'}
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen'}
      }
    ]
  },
  // {
  //   path: '/bigscreen/index.html',
  //   name: '数据可视化大屏',
  //   component: (resolve) => require(['@/views/bigscreen/index2'], resolve),
  //   hidden: true
  // },
  {
    path: '/bigScreen2/index.html',
    name: '数据可视化大屏',
    component: (resolve) => require(['@/views/bigScreen2/index'], resolve),
    hidden: true
  },
  //诱导灯参数配置
  {
    path: '/inductionLamp',
    component: (resolve) => require(['@/views/equipment/param/index'], resolve),
    hidden: true
  }

]

export default new Router({
 // base:"/tunnel",
  mode: 'hash', // 去掉url中的#
  // mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
