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
      <div class="tunnelList">
        <div @click="moveTunnel('left')" class="button" 
          :style="{ visibility: leftIcon ? 'visible' : 'hidden' }"><</div>
        <el-scrollbar wrap-class="scrollbar-wrapper " ref="scroll" :wrap-class="tunnelList.length<7?'someTunnel':'content'">
          <el-radio-group v-model="radio1" @input="getConfigPage" id="group">
            <el-radio-button
              :label="item.tunnelId"
              v-for="(item, index) in tunnelList"
              :key="item.tunnelId"
              ref="reference"
          >{{item.tunnelName}}</el-radio-button>
          </el-radio-group>
        </el-scrollbar>
        <div @click="moveTunnel('right')" class="button"
          :style="{ visibility: rightIcon ? 'visible' : 'hidden' }">></div>
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
        radio1:'',
        leftIcon: false,
        rightIcon: false,
        wrapWith:0,
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
          code:'emphasisCars',
          tunnelId:this.radio1?this.radio1:JSON.parse(this.currentTunnel).tunnelId
        }
        configPage(params).then((res)=>{
          console.log(res,"emphasisCars")
          if(res.data.length == 0){
            this.$modal.msgWarning("当前隧道未配置孪生页面");
          }
          this.url = res.data[0].url
        })
      },
      /** 所属隧道 */
      getTunnel() {
        listTunnels().then((response) => {
          console.log(response.rows,"全部隧道")
          if(response.rows.length>7){
            this.rightIcon = true
          }else{
            this.rightIcon = false
          }
          this.tunnelList = response.rows;
          this.radio1 = JSON.parse(this.currentTunnel).tunnelId
          // let wrap = this.$refs.scroll.$refs.wrap;
          this.wrapWith  = this.tunnelList.length * 114 
          // let rollWidth = this.wrapWith  - Math.abs(wrap.scrollLeft);
          // this.rightIcon = rollWidth - 114 < wrap.offsetWidth ? false : true;
        });
      },
      moveTunnel(flag){
        let wrap = this.$refs.scroll.$refs.wrap;
        if(flag == 'left'){
          wrap.scrollLeft = wrap.scrollLeft - 114;
        }else if(flag == 'right'){
          wrap.scrollLeft = wrap.scrollLeft + 114;
        }
        let rollWidth = this.wrapWith  - Math.abs(wrap.scrollLeft);
        this.rightIcon = Math.abs(rollWidth - 114) < wrap.offsetWidth ? false : true;
        this.leftIcon = wrap.scrollLeft == 0 ? false : true;
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
  .tunnelList{
    width: 900px;
    position: fixed;
    top: 95%;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1000;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .el-scrollbar__bar.is-horizontal{
      display: none;
    }
    .content{
      width:798px;
      overflow: hidden;
      margin-bottom: 0 !important;
      
    }
    .someTunnel{
      overflow: hidden;
      .el-scrollbar__view{
        display: flex;
        justify-content: center;
      }
    }
    .button{
      width: 30px;
      height: 30px;
      line-height: 30px;
      text-align: center;
      font-size: 20px;
      border-radius: 4px;
      background: rgba(0,21,43,.68) !important;
      border: solid 1px rgba(0,21,43,.68);
      color: #fff;
      cursor: pointer;
    }
    .button:hover{
      background:#00b0ff linear-gradient(90deg,#2c3e91,#100a43)!important;
      border:1px solid #2c3e91;
    }
    .el-radio-button:first-child .el-radio-button__inner{
      border-left: none;
      border-radius: 4px;
    }
    .el-radio-group{
      white-space: nowrap;
    }
    .el-radio-button--medium .el-radio-button__inner{
      border-radius: 4px;
      background: rgba(0,21,43,.68) !important;
      border:solid 1px rgba(0,21,43,.68);
      color: #fff;
      width: 110px;
      padding: 10px;
      margin-right: 4px;
    }
    .el-radio-button__orig-radio:checked + .el-radio-button__inner{
      background:#00b0ff linear-gradient(90deg,#2c3e91,#100a43)!important;
      border:1px solid #2c3e91;
      box-shadow: none;
    }
    .el-radio-button{
      display: inline;
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
  