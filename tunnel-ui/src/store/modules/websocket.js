
const websocket = {
  state: {
    payment: undefined,
    carList:[],
    realTimeLaneTrajectory:{},
    WjEvent:{}
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
    WJEVENT: (state, data) => {
      state.WjEvent = data
    },
  },

  actions: {

  }
}

export default websocket
