<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-05 09:35:35
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-14 11:37:43
 * @FilePath: \tunnel-ui\src\App.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div id="app">
    <router-view />
    <websocket />
  </div>
</template>

<script>
  import $ from "jquery";
import websocket from '@/views/websocket'
export default  {
  name:  'App',
  data(){
    return{
      path:this.$route.path,
    }
  },
  components: {
    websocket
  },
  watch: {
    $route(to,from){
      this.path = this.$route.path;
    }
  },
  metaInfo() {
      return {
          // ${process.env.VUE_APP_TITLE}
          title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
          titleTemplate: title => {
              return title ? `${title} - ${systemConfig.title(systemConfig.systemType)}` : process.env.VUE_APP_TITLE
          }
      }
  }
}
</script>
