
const websocket = {
  state: {
    payment: undefined,
    carList:[],
    realTimeLaneTrajectory:{},
    sdEventList:{},
    radarDataList:{}
  },

  mutations: {
    PAYMENT: (state, data) => {
      state.payment = data
    },
    CARLIST: (state, data) => {
      state.carList = data
    },
    REALTIMELANETRAJECTORY: (state, data) => {
      state.realTimeLaneTrajectory = data
    },
    SDEVENTLIST: (state, data) => {
      state.sdEventList = data
    },
    RADARDATALIST: (state, data) => {
      state.radarDataList = data
    },
},

  actions: {

  }
}

export default websocket
