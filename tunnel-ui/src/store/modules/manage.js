const state = {
  manageStation: '',
  manageStationSelect:''
  }
  
  const mutations = {
    SET_MANAGE: (state, manageStation) => {
      state.manageStation = manageStation
    },
    SET_TUNNELID: (state, manageStationSelect) => {
      state.manageStationSelect = manageStationSelect
    },

  }
  
  const actions = {
    // 选择隧道id
    changeTunnelId({ commit }, data) {
      commit('SET_TUNNELID', data)
    },
    // 选择系统模式
    changeManage({ commit }, data) {
      commit('SET_MANAGE', data)
    }
  }
  
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions
  }