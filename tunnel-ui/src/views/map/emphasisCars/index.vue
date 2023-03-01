<template>
    <!-- 设备检测 -->
    <div style="height: calc(100% + 38px)">
      <iframe
        name="tuniframe"
        id="miframe"
        class="mapHuaWei"
        frameborder="0"
        align="center"
        allowfullscreen="true"
        allow="autoplay"
        src="http://10.3.16.4:81/emphasisCars"
      ></iframe>
    </div>
  </template>
  <script>
  import $ from "jquery";
  export default {
    data() {
      return {};
    },
    created() {},
  };
  </script>
  <style scoped>
  .mapHuaWei {
    height: 100%;
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
  