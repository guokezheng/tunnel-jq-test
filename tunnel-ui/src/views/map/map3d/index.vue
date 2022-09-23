<template>
  <div>
    <iframe
      name="tuniframe"
      id="miframe"
      width="100%"
      class="mapHuaWei"
      frameborder="0"
      align="center"
      allowfullscreen="true"
      allow="autoplay"
      src="http://10.3.16.4/home"
    ></iframe>
  </div>
</template>
<script>
  import $ from "jquery"; 
  export default {
    data(){
      return{

      }
    },
    created(){
      console.log($('.mapBox'));
      // $('.mapBox').css('position:fixed;top:0px;left:0px;z-index:960619;width:100%;');
    },
  }
</script>
<style scoped>
  .mapHuaWei{
    height: 1000px; 
    /* position: relative; 
    top: -100px; 
    z-index: 3 */
  }
/* .mapBox {
  position: fixed;
  top: 0px;
  left: 0px;
  z-index: 960619;
  width: 100%;
} */
</style>