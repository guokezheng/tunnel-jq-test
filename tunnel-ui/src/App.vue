<template>
  <div id="app">
    <router-view />
    <websocket />
  </div>
</template>

<script>
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
