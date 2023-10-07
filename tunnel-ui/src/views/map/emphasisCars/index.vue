<template>
    <!-- 重点车辆 -->
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
  import $ from "jquery";
  import { configPage } from "@/api/map/config/api.js";
  import { getUserDeptId } from "@/api/system/user";
  export default {
    data() {
      return {
        userQueryParams: {
          userName: this.$store.state.user.name,
        },
        url:'',
        currentTunnel:''
      };
    },
    created() {
      this.currentTunnel = this.$cache.local.get("currentTunnel")
      this.getDeptId()
    },
    methods: {
      getDeptId(){
        getUserDeptId(this.userQueryParams).then((response) => {
          console.log(response, "管理站级联");
          this.userDeptId = response.rows[0].deptId;
          this.getConfigPage()
        });
      },
      getConfigPage(){
        const params = {
          deptId:this.userDeptId,
          code:'emphasisCars',
          tunnelId:JSON.parse(this.currentTunnel).tunnelId
        }
        configPage(params).then((res)=>{
          console.log(res,"emphasisCars")
          this.url = res.data[0].url
        })
      }
    },
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
  