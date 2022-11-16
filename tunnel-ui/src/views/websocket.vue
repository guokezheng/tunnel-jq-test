<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-25 08:41:42
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-10 09:31:05
 * @FilePath: \tunnel-ui\src\views\websocket.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template></template>

<script>
import { mapState } from "vuex";

export default {
  name: "Websocket",
  data() {
    return {};
  },
  computed: {
    ...mapState({
      token: (state) => state.user.token,
      websocket: (state) => state.user.websocket,
    }),
  },
  watch: {
    websocket({ password, path, port, interval }) {
      // 建立 websocket 连接
      this.socket.initialize({
        // url: "ws://10.168.65.230" + ":" + port + path,
        // url: "ws://10.168.64.171" + ":" + port + path,
         url: 'ws://10.168.78.127'+ ':' + port + path,

        password: password,
        tokenSN: this.token,
        heartRate: interval,
      });
      this.socket.onopen = () => {};
      this.socket.onmessage = (message) => {
        message = JSON.parse(message);
        const method = message.method;

        if (method !== "event") {
          return;
        }

        const params = message.params;
        const subEvent = params.subEvent;
        const content = params.content;
        var contentList = JSON.parse(content);
        console.log(subEvent,"subEvent")

        console.log(contentList,"contentList")
        switch (subEvent) {
          case "payment_webSocket_send":
            this.$store.commit("PAYMENT", content);
            break;
          case "carList":
            this.$store.commit("CARLIST", content);
            break;
          case "realTimeLaneTrajectory":
            this.$store.commit("REALTIMELANETRAJECTORY", content);
            break;
          case "sdEventList":
            this.$store.commit("SDEVENTLIST", contentList.sdEventList);
            break;
          case "radarDataList":
            this.$store.commit("RADARDATALIST", contentList.radarDataList);
            break;
          case "deviceStatus":
            this.$store.commit("DEVICESTATUS", contentList.deviceStatus);
            break;
          case "deviceStatusChangeLog":
            this.$store.commit(
              "DEVICESTATUSCHANGELOG",
              contentList.deviceStatusChangeLog
            );
            break;
          case "eventFlow":
            this.$store.commit("EVENTFLOW", contentList.eventFlow);
            break;
          default:
        }
      };
    },
  },
  created() {},
  methods: {},
};
</script>


