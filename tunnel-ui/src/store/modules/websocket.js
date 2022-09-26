/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-25 08:41:42
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-26 14:36:30
 * @FilePath: \tunnel-ui\src\store\modules\websocket.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

const websocket = {
  state: {
    payment: undefined,
    carList: [],
    realTimeLaneTrajectory: {},
    sdEventList: {},
    radarDataList: {},
    deviceStatus: {},
    deviceStatusChangeLog: {},
  },

  mutations: {
    DEVICESTATUSCHANGELOG: (state, data) => {
      state.deviceStatusChangeLog = data
    },
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
