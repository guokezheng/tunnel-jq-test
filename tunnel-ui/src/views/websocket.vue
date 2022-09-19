<template>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: "Websocket",
  data() {
    return {
      websocket: null,
    };
  },
  computed: {
    ...mapState({
      token: state => state.user.token,
      // websocket: state => state.user.websocket,
      wsData:state => state.wsData
    })
  },
  watch: {
//    websocket({ password, path, port, interval}) {
//       // 建立 websocket 连接
//       this.socket.initialize({
//
// 		     // url: 'ws://'+location.hostname + ':' + port + path,
// 		    //  url: 'ws://10.3.16.40'+ ':' + port + path,
// 		    //  url: 'ws://10.168.64.171'+ ':' + port + path,
// 		     url: 'ws://10.168.65.230'+ ':' + port + path,
//
// 		        password: password,
// 		        tokenSN: this.token,
// 		        heartRate: interval
//       });
//
//    
//       this.socket.onmessage = (message) => {
//         message = JSON.parse(message)
//         const method = message.method;
//
//         if (method !== 'event')
//         {
//           return;
//         }
//
//         const params = message.params;
//         const subEvent = params.subEvent;
//         const content = params.content;
//         switch (subEvent)
//         {
//           case 'payment_webSocket_send':
//             this.$store.commit('PAYMENT', content)
//             break;
//           case 'carList':
//             this.$store.commit('CARLIST', content)
//             break;
//           case 'realTimeLaneTrajectory':
//             this.$store.commit('REALTIMELANETRAJECTORY', content)
//             break;
//           case 'WjEvent':
//             debugger;
//             this.$store.commit('WJEVENT', content.list)
//             break;
//           default:
//         }
//       }
//     }
  },
  mounted() {
    if ('WebSocket' in window) {
      this.websocket = new WebSocket('ws://10.168.56.206:8000/websocket');
      this.initWebSocket();
    } else {
      alert('当前浏览器不支持WebSocket!!!')
    }
  },
  created() {

  },
  methods: {
    initWebSocket() {
      // 连接错误
      this.websocket.onerror = this.setErrorMessage
      // 连接成功
      this.websocket.onopen = this.setOnopenMessage
      // 收到消息的回调
      this.websocket.onmessage = this.setOnmessageMessage
      // 连接关闭的回调
      this.websocket.onclose = this.setOncloseMessage
      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = this.onbeforeunload
    },
    setErrorMessage() {
      window.console.log('WebSocket连接发生错误，状态码：' + this.websocket.readyState)
    },
    setOnopenMessage() {
      window.console.log('WebSocket连接成功，状态码：' + this.websocket.readyState)
    },
    setOnmessageMessage(event) {

      if(event.data){
        if(event.data === "success"){
          console.log("websocket连接成功");
        }else{
          var data = JSON.parse(event.data);
          if(data.sdEventList){
            this.$store.commit('sdEvent', data.sdEventList)
          }
          if(data.radarDataList){
            this.$store.commit('radarDataList', data.radarDataList)
          }

          

        }
      }
      // 根据服务器推送的消息做自己的业务处理
      // window.console.log(event.data);
      // console.log();

    },
    setOncloseMessage() {
      window.console.log('WebSocket连接关闭，状态码：' + this.websocket.readyState)
    },
    onbeforeunload() {
      this.closeWebSocket()
    },
    closeWebSocket() {
      this.websocket.close()
    }
  }
};
</script>


