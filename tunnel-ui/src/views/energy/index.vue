<template>
    <div style="height: calc(100% + 124px)">
      <iframe
        name="tuniframe"
        id="miframe"
        class="mapHuaWei"
        frameborder="0"
        align="center"
        allowfullscreen="true"
        allow="autoplay"
        src="http://10.3.16.40:2222/dist/index"
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
    height: calc(100% + 10px);
    position: relative;
    top: -10px;
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
  