import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import websocket from './modules/websocket'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import wsData from './modules/wsData'
import getters from './getters'
import manage from './modules/manage'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    websocket,
    tagsView,
    permission,
    settings,
    wsData,
    manage
  },
  getters
})

export default store