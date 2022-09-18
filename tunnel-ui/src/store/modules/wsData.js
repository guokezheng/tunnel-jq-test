
const wsData = {
  state: {
    sdEvent:{},
    radarDataList:{}
  },

  mutations: {
    sdEvent: (state, data) => {
      state.sdEvent = data
    },
    radarDataList: (state, data) => {
      state.radarDataList = data
    },
  },

  actions: {

  }
}

export default wsData
