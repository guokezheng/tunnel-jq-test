<!--
  电话广播管理平台websocket消息
-->
<template></template>

<script>
  import {mapState} from "vuex";
  import {onMessage} from "@/api/equipment/deviceBrand/brand";

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
      websocket({password, path, port, interval}) {
        // 建立 websocket 连接
        this.socket.initialize({
          // url: 'ws://' + location.hostname + ':' + port + path,
          url: 'ws://10.7.187.6:8981',
          password: password,
          tokenSN: this.token,
          heartRate: interval,
        });
        this.socket.onopen = () => {
        };
        this.socket.onmessage = (message) => {
          // console.log("电话广播websocket>>>>>>>>>>>>",message)
          // console.log("电话广播websocket>>>>>>>>>>>>",this.isJson(message))
          if (this.isJson(message)) {
            onMessage(message).then(response => {
              console.log(response)
            });
          }
        };
      },
    },
    created() {
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
    },
  };
</script>


