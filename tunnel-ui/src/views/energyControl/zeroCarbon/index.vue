<template>
    <div style="height: calc(100% + 4vh)">
        <iframe
        name="tuniframe"
        id="miframe"
        class="mapHuaWei"
        frameborder="0"
        align="center"
        allowfullscreen="true"
        allow="autoplay"
        :src="url"
      ></iframe>
    </div>
</template>
<script>
export default {
    data() {
        return {
            url:'http://10.166.132.26/carbon-ui/#/login?code=410282511eb5a65e2a956a31c616c881&&asideCollapse=false&&route=tunnel/home?pointId=000000s001637000001'
        }
    },
    created() {
        console.log("零碳大屏")
    },
}
</script>
<style scoped>
.mapHuaWei {
  height:100%;
  position: relative;
  top: 0px;
  z-index: 3;
  width: 100%;
}
/* .mapBox {
  position: fixed;
  top: 0px;
  left: 0px;
  z-index: 960619;
  width: 100%;
} */
</style>