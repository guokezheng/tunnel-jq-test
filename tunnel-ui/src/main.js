import Vue from 'vue'
import CollapseTransition from 'element-ui/lib/transitions/collapse-transition';
import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'
import './views/iot/css/iot.css'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' //directive
import plugins from './plugins' // plugins
import './assets/icon/iconfont.css' // 阿里巴巴icon
import './assets/icons' // icon
import './permission' // permission control
import { Socket } from './utils/socket'
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree, numberMul } from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'
// 无缝滚动组件
import VueSeamlessScroll from 'vue-seamless-scroll'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/css/swiper.css'
//el-table无线滚动

// 事件弹窗组件
import eventDialog from '@/components/eventDialogTable/eventDialog.vue'
import eventDialogTable from '@/components/eventDialogTable'
import evtDialogOneThing from '@/components/eventDialogTable/evtDialogOneThing.vue'
import evtDialogVideo from '@/components/eventDialogTable/video.vue'


// 字典标签组件
// import reproductionImage from '@/components/reproductionImage'
// jQuery
import $ from 'jquery'
import preventClick from './api/clickOnceAtime'
// gisMap
// import gisMap from 'gis-map-tunnel'
import gisMap from 'gis-map-admin'
import config from '../public/config'


import request from '../public/config'
import moment from 'moment'
import dayjs from "dayjs"

//定义全局过滤器
Vue.filter('dateformat', function (dataStr, pattern = 'YYY-MM-DD HH:mm:ss') {
  if (dataStr === null || dataStr === "") {
    return "";
  }
  return moment(dataStr).format(pattern)
  //filter两个参数  第一个是函数名，第二个是时间格式化处理的函数
  //(函数里面的参数 第一个是传递的数据，第二个是需要转换的时间格式)
})


// 全局方法挂载
Vue.prototype.dayjs = dayjs;
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.handleTree = handleTree
Vue.prototype.socket = Socket
Vue.prototype.numberMul = numberMul


// gis 
Vue.prototype.$GlobalConfig = config.GlobalConfig

Vue.use(preventClick)
Vue.use(VueAwesomeSwiper)
// 全局组件挂载

Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('VueSeamlessScroll', VueSeamlessScroll)
Vue.component('eventDialog', eventDialog)
Vue.component('eventDialogTable', eventDialogTable)
Vue.component('evtDialogOneThing', evtDialogOneThing)
Vue.component('evtDialogVideo', evtDialogVideo)



Vue.component(CollapseTransition.name, CollapseTransition)
// Vue.component('reproductionImage', reproductionImage)

Vue.use(directive)
Vue.use(plugins)
Vue.use(gisMap)
Vue.use(VueMeta)
DictData.install()
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})