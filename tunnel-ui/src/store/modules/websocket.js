
const websocket = {
  state: {
    payment: undefined,
    carList:[],
    realTimeLaneTrajectory:{}
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
  },

  actions: {

  }
}

export default websocket
