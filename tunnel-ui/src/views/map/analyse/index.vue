<template>
    <!-- 研判分析 -->
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
      <div class="tunnelList">
        <el-radio-group v-model="radio1" @input="getConfigPage">
          <el-radio-button
            :label="item.tunnelId"
            v-for="(item, index) in tunnelList"
            :key="item.tunnelId"
        >{{item.tunnelName}}</el-radio-button>
        </el-radio-group>
      </div>
    </div>
  </template>
  <script>
  import $ from "jquery";
  import { configPage } from "@/api/map/config/api.js";
  import { getUserDeptId } from "@/api/system/user";
  import { listTunnels } from "@/api/equipment/tunnel/api";

  export default {
    data() {
      return {
        userQueryParams: {
          userName: this.$store.state.user.name,
        },
        url:'',
        currentTunnel:'',
        tunnelList:[],
        radio1:''
      };
    },
    created() {
      this.currentTunnel = this.$cache.local.get("currentTunnel")
      this.getDeptId()
      this.getTunnel()
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
          code:'analyse',
          tunnelId:this.radio1?this.radio1:JSON.parse(this.currentTunnel).tunnelId
        }
        configPage(params).then((res)=>{
          console.log(res,"analyse")
          if(res.data.length == 0){
            this.$modal.msgWarning("当前隧道未配置孪生页面");
          }
          this.url = res.data[0].url
        })
      },
      /** 所属隧道 */
      getTunnel() {
        listTunnels().then((response) => {
          
          this.tunnelList = response.rows;
          this.radio1 = JSON.parse(this.currentTunnel).tunnelId
        });
      },
    },
  };
  </script>
  <style scoped>
  .mapHuaWei {
    height:100%;
    position: relative;
    top: 0px;
    z-index: 3;
    width: 100%;
  }
  .tunnelList{
    position: absolute;
    top: 10%;
    left: 24%;
    z-index: 1000;
    .el-radio-button:first-child .el-radio-button__inner{
      border-left: none;
      border-radius: 4px;
    }
    .el-radio-button--medium .el-radio-button__inner{
      border-radius: 4px;
      background: rgba(0,21,43,.68) !important;
      border:solid 1px rgba(0,21,43,.68);
      color: #fff;
    }
    .el-radio-button__orig-radio:checked + .el-radio-button__inner{
      background:#00b0ff linear-gradient(90deg,#2c3e91,#100a43)!important;
      border:1px solid #2c3e91;
      box-shadow: none;
    }
    .el-radio-button{
      display: block;
      margin-top: 4px;
    }
  }
  /* .mapBox {
    position: fixed;
    top: 0px;
    left: 0px;
    z-index: 960619;
    width: 100%;
  } */
  </style>
  