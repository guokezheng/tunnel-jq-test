<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-25 08:41:42
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-01-30 09:10:22
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
      // debugger
      // console.log(path)
      // console.log(port)
      // console.log(location.hostname )
      // 建立 websocket 连接
      this.socket.initialize({
        // url: 'ws://' + location.hostname + ':' + port + path,
        url: "ws://10.7.179.15" + ":" + port + path,
        // url: "ws://10.168.64.171" + ":" + port + path,
        //  url: 'ws://10.168.78.127'+ ':' + port + path,

        password: password,
        tokenSN: this.token,
        heartRate: interval,
      });
      this.socket.onopen = () => {};
      this.socket.onmessage = (message) => {
        // debugger
        message = JSON.parse(message);
        const method = message.method;

        if (method !== "event") {
          return;
        }

        const params = message.params;
        const subEvent = params.subEvent;
        const content = params.content;
        var contentList = JSON.parse(content);

        switch (subEvent) {
          case "payment_webSocket_send":
            this.$store.commit("PAYMENT", content);
            break;
          case "playvideo":
            //let audio = new Audio("http://10.7.200.14:7080/video/ding.WAV");
            let audio = new Audio("/static/ding.WAV");
            audio.play();
            break;
          case "carList":
            this.$store.commit("CARLIST", content);
            break;
          case "realTimeLaneTrajectory":
            this.$store.commit("REALTIMELANETRAJECTORY", content);
            break;
          case "sdEventList":
            //弹窗
            this.$store.commit("SDEVENTLIST", contentList.sdEventList);
            break;
          case "sdSvgEventList":
            //弹窗
            this.$store.commit("SDSVGEVENTLIST", contentList.sdSvgEventList);
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
          case "eventUntreatedNum":
            this.$nextTick(() => {
              this.$store.commit("EVENTUNTREATEDNUM", contentList);
            });

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


