import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login','/auth-redirect', '/bind', '/register']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login'||to.path === '/loginjqtunnel') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if(to.path === '/login' && to.query.code !== "" && typeof to.query.code !== "undefined"){
      //正式环境的单点登录

        store.dispatch("Login1",{
          "code":to.query.code,
          "asideCollapse":to.query.asideCollapse
        }).then(() => {
          next({ path: '/' })
        }).catch(() => {
          next(`/login?redirect=${to.fullPath}`)// 否则全部重定向到登录页
      })
      NProgress.done()
    }else if(to.path.indexOf("loginjqtunnel")!=-1){

      //测试环境的单点登录
      store.dispatch("LoginTest").then(() => {
        next({ path: '/' })
      }).catch(() => {
        next(`/login?redirect=${to.fullPath}`)// 否则全部重定向到登录页
      })
    } else{
      if (whiteList.indexOf(to.path) !== -1) {
        // 在免登录白名单，直接进入
        next()
      }else{
        next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      }
    }

  }
})

router.afterEach(() => {
  NProgress.done()
})
