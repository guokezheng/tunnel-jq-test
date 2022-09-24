<template>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: "Websocket",
  data() {
    return {};
  },
  computed: {
    ...mapState({
      token: state => state.user.token,
      websocket: state => state.user.websocket
    })
  },
  watch: {
    websocket({password, path, port, interval}) {
      // 建立 websocket 连接
      this.socket.initialize({

        url: 'ws://10.3.16.40' + ':' + port + path,
        // url: 'ws://10.3.16.40'+ ':' + port + path,
        //  url: 'ws://10.168.64.171'+ ':' + port + path,
        //  url: 'ws://10.168.65.230'+ ':' + port + path,

        password: password,
        tokenSN: this.token,
        heartRate: interval
      });
      this.socket.onopen = () => {

      }
      this.socket.onmessage = (message) => {
        message = JSON.parse(message)

        const method = message.method;

        if (method !== 'event') {
          return;
        }

        const params = message.params;
        const subEvent = params.subEvent;
        const content = params.content;
        var contentList = JSON.parse(content);

        switch (subEvent) {
          case 'payment_webSocket_send':
            this.$store.commit('PAYMENT', content)
            break;
          case 'carList':
            this.$store.commit('CARLIST', content)
            break;
          case 'realTimeLaneTrajectory':
            this.$store.commit('REALTIMELANETRAJECTORY', content)
            break;
          case 'sdEventList':
            this.$store.commit('SDEVENTLIST', contentList.sdEventList)
            break;
          case 'radarDataList':
            this.$store.commit('RADARDATALIST', contentList.radarDataList)
            break;
          case 'deviceStatus':
            this.$store.commit('DEVICESTATUS', contentList.deviceStatus)
            break;

          default:
        }
      }
    }
  },
  created() {
  },
  methods: {}
};
</script>


