<!--
  电话广播管理平台websocket消息
-->
<template></template>

<script>
  import {mapState} from "vuex";
  import {onMessage} from "@/api/equipment/phone/phone";

  export default {
    name: "PhoneWebsocket",
    data() {
      return {
        //修改变量名，避免跟原websocket冲突
        phoneWebsocket: null
      };
    },
    // computed: {
    //   ...mapState({
    //     token: (state) => state.user.token,
    //     websocket: (state) => state.user.websocket,
    //   }),
    // },
    // watch: {
    //   websocket({password, path, port, interval}) {
    //     建立 websocket 连接
    //     this.socket.initialize({
    //       // url: 'ws://' + location.hostname + ':' + port + path,
    //       url: 'ws://10.7.187.6:8981',
    //       password: password,
    //       tokenSN: this.token,
    //       heartRate: interval,
    //     });
    //     this.socket.onopen = () => {
    //     };
    //     this.socket.onmessage = (message) => {
    //       // console.log("电话广播websocket>>>>>>>>>>>>",message)
    //       // console.log("电话广播websocket>>>>>>>>>>>>",this.isJson(message))
    //       if (this.isJson(message)) {
    //         onMessage(message).then(response => {
    //           console.log(response)
    //         });
    //       }
    //     };
    //   },
    // },
  created() {
    this.initWebSocket();
  },
  destroyed() {
    this.phoneWebsocket.close() //离开路由之后断开websocket连接
  },
    methods: {
      isJson(str){
        if (typeof str == 'string') {
          try {
            let obj=JSON.parse(str);
            if(typeof obj == 'object' && obj ){
              return true;
            }else{
              return false;
            }

          } catch(e) {
            return false;
          }
        }
      },
      initWebSocket(){ //初始化weosocket
        // var url = "ws://localhost:8000/websocket";
        var url = "ws://10.7.187.6:8981";
        this.phoneWebsocket = new WebSocket(url);
        this.phoneWebsocket.onmessage = this.websocketonmessage;
        this.phoneWebsocket.onopen = this.websocketonopen;
        this.phoneWebsocket.onerror = this.websocketonerror;
        this.phoneWebsocket.onclose = this.websocketclose;
      },
      websocketonopen(){
        console.log("紧急电话广播系统建立连接");
      },
      websocketonerror(){//连接建立失败重连
        console.log("紧急电话广播系统连接建立失败重连");
        setTimeout(5000,this.initWebSocket());
        // this.initWebSocket();
      },
      websocketonmessage(message){ //数据接收
        if (this.isJson(message)) {
          onMessage(message).then(response => {
            console.log(response)
        });
        }
      },
      websocketclose(e){  //关闭
        console.log('紧急电话广播系统断开连接',e);
      },
    },
  };
</script>


