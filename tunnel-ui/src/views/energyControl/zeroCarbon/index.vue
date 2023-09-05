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
import {getZeroUrl} from "@/api/energy/api";
export default {
    data() {
        return {
            url:'',
            currentTunnel: this.$cache.local.get("currentTunnel"),
        }
    },
    created() {
        console.log("零碳大屏");
        console.log(this.currentTunnel,"manageStationSelectmanageStationSelectmanageStationSelect");
        
        let param = {
          tunnelId: JSON.parse(this.currentTunnel).tunnelId
        }
        console.log(param,"paramparamparam")
        getZeroUrl(param).then((res) => {
          if (res.code == "200") {
            this.url = res.data;
          }
        })
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