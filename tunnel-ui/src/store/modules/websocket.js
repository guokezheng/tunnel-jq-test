
const websocket = {
  state: {
    payment: undefined,
    carList:[],
    realTimeLaneTrajectory:{},
    sdEventList:{},
    radarDataList:{},
    deviceStatus:{}

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
    DEVICESTATUS: (state, data) => {
      state.deviceStatus = data
    },
},

  actions: {

  }
}

export default websocket
