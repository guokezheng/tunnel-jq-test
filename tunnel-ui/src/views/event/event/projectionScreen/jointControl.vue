<template>
  <el-dialog
    :title="titleHistory"
    :visible.sync="visibleSync"
    :before-close="close"
    width="73%"
    class="hitchHistoryDialog"
    :destroy-on-close ="true"
    :close-on-click-modal="false"
  >
    <el-form
    ref="queryForm"
    :inline="true"
    label-width="55px"
    >

      <el-button size="small" :loading="exportLoading" @click="deviceLogin"
      >设备登录
      </el-button>
      <el-button size="small" :loading="exportLoading" @click="wallSetting"
      >电视墙设置
      </el-button>
      <el-button size="small" :loading="exportLoading" @click="sendUpdate"
      >发送修改
      </el-button>

      <el-dropdown>
        <el-button size="small" type="primary" style="margin-left: 10px">
          窗口操作
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-button  size="small" type="primary" @click="handleButtonClick(1)">刷新</el-button>
          <el-button  size="small" type="primary" @click="handleButtonClick(2)">发送修改</el-button>
          <el-button   size="small" type="primary" @click="handleButtonClick(3)">快速开窗</el-button>
          <el-button  size="small" type="primary" @click="handleButtonClick(4)">删除所有窗</el-button>
        </el-dropdown-menu>
      </el-dropdown>

      <el-dropdown>
        <el-button size="small" type="primary" style="margin-left: 10px">
          解码
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-button  size="small" type="primary" @click="decodeButtonClick(1)">动解码态</el-button>
          <el-button  size="small" type="primary" @click="decodeButtonClick(2)">解码状态</el-button>
        </el-dropdown-menu>
      </el-dropdown>

    </el-form>
    <el-container style="height: 550px; border: 1px solid #eee">
      <div style="margin-right: 20px;margin-top: 20px"  >控制器树
<!--         <el-tree style="width:150px"-->
<!--                  :data="data"-->
<!--                  :props="defaultProps"-->
<!--                  :draggable="true"-->
<!--                  @dragstart="handleDragStart"-->
<!--                  @node-click="handleNodeClick"-->
<!--         ></el-tree>-->

      <treeModal :treeData="treeData"></treeModal>
      </div>
<!--      <treeModal :treeData="data"></treeModal>-->
    <el-container>


        <el-main>
<!--          <div class="container"   v-if="containerShow" style="border: 1px solid red;  background-color: #C0C0C0; width: 100%;height: 100%;"></div>-->
<!--          <div class="container" v-if="!containerShow">-->
<!--            <div class="top-block"></div>-->
<!--            <div class="top-block"></div>-->
<!--            <div class="top-block"></div>-->
<!--            <div class="top-block"></div>-->
<!--          </div>-->
          <div id="container">
            <div v-for="item in divItems" :key="item" class="box"  @dragover="dragOver" @drop="drop">
              <videoPlayer
                :rtsp="liveUrl"
                :open="cameraPlayer"
              ></videoPlayer>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
    <!--登录    -->
    <template>
      <el-dialog
        :title="loginVisibleTitle"
        :visible.sync="loginVisible"
        :before-close="closeLogin"
        width="40%"
        :append-to-body="true"
        class="hitchHistoryDialog"
        :close-on-click-modal="false"
      >
        <el-form
          ref="loginQueryForm"
          :model="loginModel"
          :inline="true"
          class="loginQueryFormClass"
          label-width="100px"
          height="300px"
        >
          <el-form-item label="ip" >
            <el-input v-model="loginModel.ip"></el-input>
          </el-form-item>
          <el-form-item label="port" >
            <el-input v-model="loginModel.port"></el-input>
          </el-form-item>
          <el-form-item label="userName" >
            <el-input v-model="loginModel.userName"></el-input>
          </el-form-item>
          <el-form-item label="passWord" >
            <el-input v-model="loginModel.passWord"></el-input>
          </el-form-item>
        </el-form>


        <template>
          <div class="containerebutton">
            <div class="centeredbutton">
              <el-button size="small" :loading="exportLoading" @click="deviceLogin"
              >登录
              </el-button>
              <el-button size="small" :loading="exportLoading" @click="jointControlClick"
              >关闭
              </el-button>
            </div>
          </div>
        </template>
      </el-dialog>
    </template>
    <wellSettingModal ref="wellSettingModal" :show="wellSettingModalShow"  @wellSettingModel="wellSettingModel"></wellSettingModal>
    <dynamicDecodeModal ref="dynamicDecodeModal" :show="dynamicDecodeModalShow" ></dynamicDecodeModal>
    <DecodingStateModal ref="DecodingStateModal" :show="DecodingStateModalShow" ></DecodingStateModal>
  </el-dialog>

</template>


<script>
import  wellSettingModal  from "@/views/event/event/projectionScreen/wellSettingModal.vue";
import  dynamicDecodeModal  from "@/views/event/event/projectionScreen/dynamicDecodeModal.vue";
import  DecodingStateModal  from "@/views/event/event/projectionScreen/DecodingStateModal.vue";
import  treeModal  from "@/views/event/event/projectionScreen/treeModal.vue";
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";
import {getDevices} from "@/api/equipment/eqlist/api";
const item = {
  date: '2016-05-02',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
};
export default {
  name: "jointControl",
  components: { wellSettingModal ,dynamicDecodeModal ,DecodingStateModal,videoPlayer,treeModal},
  data() {

    return {
      titleHistory:"视频墙配置",
      loginVisibleTitle:"设备登录",
      loginVisible:false,
      visibleSync: false,
      wallNumLoading:false,
      exportLoading:false,
      wellSettingModalShow:"",
      dynamicDecodeModalShow:"",
      DecodingStateModalShow:"",
      options: [{
        value: '1',
        label: '1'
      }, {
        value: '4',
        label: '4'
      }],
      loginModel:{
        ip :"",
        port :"",
        userName:"",
        passWord:"",
      },
      containerShow:true,
      value: '',
      wallValue: [],
      tableData: Array(20).fill(item),
      deviceData:[],
      divItems:1,
      //横向
      horizontalCount:0,
      //纵向
      verticalCount:0,
      liveUrl:"",
      cameraPlayer:false,
      data: [{
        label: '一级 1',
        children: [{
          label: '二级 1-1'
        }]
      }, {
        label: '一级 2',
        children: [{
          label: '二级 2-1'
        }, {
          label: '二级 2-2'
        }]
      }, {
        label: '一级 3',
        children: [{
          label: '二级 3-1'
        }, {
          label: '二级 3-2'
        }]
      }],
      treeData:[],
      // treeData:[
      //   {
      //     id: 1,
      //     name: '12.123.43.23',
      //     expanded: false,
      //     children: [
      //       {
      //         id: 2,
      //         name: '1_3_32_df',
      //         expanded: false,
      //       },
      //       {
      //         id: 3,
      //         name: '1_3_32_dfdsdf',
      //         expanded: false,
      //         children: []
      //       }
      //     ]
      //   },
      //   {
      //     id: 6,
      //     name: '12.123.43.22',
      //     expanded: false,
      //     children: []
      //   }
      // ],
      //拖拽组件
      draggedNode: null,
      dropTargetNode: null,
      // eqIdList:[],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    };
  },
  created(){

  },
  methods:{
    close(){
      // debugger
      this.cameraPlayer = false
      this.visibleSync = !this.visibleSync
    },
    selectChange(e){
      if(e==4){
        this.containerShow = false
      }else if(e==1){
        this.containerShow = true
      }
      // debugger
    },
    // calculateNumbers(num) {
    //   var factor1 = Math.floor(Math.sqrt(num)); // 取平方根向下取整
    //   var factor2 = Math.ceil(num / factor1); // 取商向上取整
    //
    //   while (factor1 * factor2 !== num) {
    //     // 当乘积不等于给定数时，递减一个因数，同时递增另一个因数
    //     factor1--;
    //     factor2 = Math.ceil(num / factor1);
    //   }
    //
    //   return [factor1, factor2];
    // },
    //div 数 horizontalCount横向数 纵向数
    setWillNum(divNum){
      // debugger
      this.divItems =4
      var container = document.getElementById("container");

      //设置div
      // for (var i = 0; i < divNum; i++) {
      //   var box = document.createElement("div"); // 创建新的div元素
      //   box.className = "box"; // 添加box类名
      //   container.appendChild(box); // 将div元素添加到container中
      // }
      //设置横纵表格
      var container = document.getElementById("container");
      container.style.display = "grid";
      container.style.width = "100%";
      container.style.height = "100%";
      container.style.gap = "10px";
      container.style.gridTemplateColumns = "repeat("+this.horizontalCount+", 1fr)";
      container.style.gridTemplateRows = "repeat("+this.verticalCount+", 1fr)";

      var boxes = document.getElementsByClassName("box");
      for (var i = 0; i < boxes.length; i++) {
        boxes[i].style.backgroundColor = "#ccc";
        boxes[i].style.padding = "20px";
      }
    },
    //登录拼控设备
    deviceLogin(){
      this.loginVisible = true
    },
    jointControlClick(){

    },
    closeLogin(){
      this.loginVisible = false
    },
    //电视墙设置
    wallSetting(){
      // debugger
      this.wellSettingModalShow = !this.wellSettingModalShow
    },
    //发送修改
    sendUpdate(){
      // debugger
      this.cameraPlayer = false
    },
    wallHandleChange(){

    },
    //窗口操作
    handleButtonClick(e){
      switch (e) {
        case 1:

          break;
        case 2:

          break;
        case 3:

          break;
        case 4:

          break;
        default:
      }
    },
    //解码操作
    decodeButtonClick(e){
      // debugger
      switch (e) {
        case 1:
          this.dynamicDecodeModalShow = !this.dynamicDecodeModalShow
          break;
        case 2:
          this.DecodingStateModalShow = !this.DecodingStateModalShow
          break;
        default:
      }
    },
    //设备树下拉出发
    handleNodeClick(e){
      // debugger
    },
    //电视墙设置回调
    wellSettingModel(e){
      // debugger
      this.liveUrl = "http://47.104.0.66:8082/live/52088.live.flv"
      this.cameraPlayer = true
      this.horizontalCount = e.breadth
      this.verticalCount = e.altitude
      this.setWillNum(12)
    },
    //一下是拖拽组件方法
    dragOver(event) {
      event.preventDefault();
    },
    drop(event) {
      event.preventDefault();
      const data = event.dataTransfer.getData('text/plain');
      event.target.innerText = data;
    }

  },
  props: {

    show: {
      type: Boolean,
    },
    eqIdList: {
      type: Array,
      default: () => []
    },

  },
  watch:{
    show: {
      async handler(newValue, oldValue) {


        if(this.eqIdList.length>0)
        {
          this.treeData = []
          let treeModer ={
            id: Math.random(),
            name:"147.104.0.66",
            expanded: false,
            children:[]
          }
          for (let i = 0; i <  this.eqIdList.length; i++) {
            await getDevices( this.eqIdList[i]).then((response) => {
              let childrenModer = {}
              childrenModer.id = Math.random()
              childrenModer.name = response.data.eqName
              childrenModer.expanded = false
              treeModer.children.push(childrenModer)
              // debugger
            });
          }
          this.treeData.push(treeModer)
        }
        // debugger
        this.visibleSync = !this.visibleSync
      },
    },
    eqIdList:{
      handler(newValue, oldValue) {
        // debugger
        this.eqIdList = newValue

      },
    }
  }
}


</script>

<style lang="scss" scoped>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}

#container {
  width: 100%;
  height: 100%;
}


.box {
  background-color: #ccc; /* 设置小div的背景色 */
  padding: 20px; /* 设置小div的内边距，可根据需要调整 */
  width: 100%;height: 100%;
}

.top-block {
  width: 48%;
  height: 48%;
  float:left;
  margin-left: 10px;
  margin-bottom: 10px;
  background-color: #C0C0C0;
}

.containerebutton {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 10vh;
}

.centeredbutton {
  text-align: center;
}


</style>
