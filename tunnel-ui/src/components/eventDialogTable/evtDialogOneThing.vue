<template>
  <div>
    <div class="eventBox" >
      <div class="title">
        事件详情
        <!-- <img
          src="../../assets/cloudControl/dialogHeader.png"
          style="height: 30px"
        /> -->
        <img
          src="../../assets/cloudControl/closeIcon.png"
          style="
            height: 14px;
            position: absolute;
            right: 10px;
            top: 10px;
            cursor: pointer;
          "
          @click="closeDialogTable()"
        />
      </div>
      <div class="blueLine"></div>
      <el-row style="color:white" class="eventContent">
        <el-col :span="2" >
          <img :src="formOneThing.iconUrl" style="width:20px;height:20px;transform: translateY(5px);" />
        </el-col>
        <el-col :span="2">
          <div>{{formOneThing.eventType.simplifyName}}</div>
        </el-col>
        <el-col :span="12">
          <div class="overflowText">{{formOneThing.eventTitle}}</div>
        </el-col>
        <el-col :span="4">
          <div>{{formOneThing.startTime}}</div>
        </el-col>
        <el-col :span="2">
          <el-button
            size="mini"
            type="text"
            @click="handleSee(formOneThing.id)"
            >查看
          </el-button>
        </el-col>
        <el-col :span="2">
          <el-button
            size="mini"
            type="text"
            @click="handleIgnore(formOneThing.id)"
            >忽略
          </el-button>
        </el-col>
      </el-row>
      <div class="lineBT">
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
    
  </div>
</template>
  
  <script>
import { mapState } from "vuex";
import bus from "@/utils/bus";
import { updateEvent } from "@/api/event/event";
import evtdialog from "@/components/eventDialogTable/eventDialog"; //只有数据的弹窗

export default {
  name: "eventDialogTable",
  components: {
    evtdialog,
  },
  data() {
    return {
      // showTable:false,
      carIcon: require("@/assets/icons/carIcon.png"),
      eventTableDialog: false,
      activeName: "0",
      formOneThing:{
          id:1,
          icon:require("@/assets/icons/carIcon.png"),
          title:"停车",
          content:'杭山东隧道K147+100处存在停车事件，请确认...',
          time:'10:45:33'
      },
      tabList: [],
      urls: [],
      videoUrl: require("@/assets/Example/v1.mp4"),
    };
  },
  computed: {
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
    }),
  },
  watch: {
    sdEventList(event) {
     this.formOneThing = event[0]
      // this.eventTableDialog = true;
      // this.showTable = true
    },
    deep: true,
  },
  created() {
    // this.getDicts("sd_event_source").then((data) => {
    //   console.log(data, "事件来源");
    //   this.tabList = data.data;
    //   this.tabList.forEach((item) => {
    //     item.list = [];
    //   });
    // });
  },
  mounted(){
    this.handleSee()
    // bus.$on('closeTableDialog', () => {
    //    this.eventTableDialog = false
    // })
    // bus.$on('openTableDialog', () => {
    //    this.eventTableDialog = true
    // })
  },
  methods: {
    handleSee(id) {
      setTimeout(() =>{
        bus.$emit("getPicId", id);
      },200)
      bus.$emit("openPicDialog");
    },

    // 忽略事件
    handleIgnore(id) {
      if (id) {
        const param = {
          id: id,
          eventState: "2",
        };
        updateEvent(param).then((response) => {
          this.$modal.msgSuccess("已成功忽略");
        });
        this.formOneThing = {}
        // this.tabList.forEach((item) => {
        //   item.list.forEach((its) =>{
        //     if(its.id == id){
        //       item.list.splice(its,1)
        //     }
        //   })
        // });
        // bus.$emit("getEvtList")
      } else {
        this.$modal.msgError("没有接收到事件id");
      }
    },

    // 处理 跳转应急调度
    // handleDispatch(event) {
    //   const param = {
    //     id: event.id,
    //     eventState: "0",
    //   };
    //   updateEvent(param).then((response) => {
    //     console.log(response, "修改状态");
    //     this.$modal.msgSuccess("开始处理事件");
    //   });
    //   this.$router.push({
    //     path: "/emergency/administration/dispatch",
    //     query: { id: event.id },
    //   });
    //   bus.$emit("closeDialog");
    //   this.eventTableDialog = false
    // },
    closeDialogTable() {
      bus.$emit("closeDialog");
      this.eventTableDialog = false
    },
   
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 表格的行样式
    // tableRowClassName({ row, rowIndex }) {
    //   if (rowIndex%2 == 0) {
    //   return 'tableEvenRow';
    //   } else {
    //   return "tableOddRow";
    //   }
    // },
  },
};
</script>
  
  <style lang="scss" scoped>

.eventClass {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
  // border: solid 10px rgba($color: #14B7EA, $alpha: 0.3);
  background-color: rgba($color: #000000, $alpha: 0.1);
  // border-radius: 10px;
}
.eventBox {
  width: 570px;
  height: 120px;
  border: solid 1px rgba($color: #0198ff, $alpha: 0.5);
  position: absolute;
  top: 0px;
  left: calc(100% - 600px);
  background: rgba($color: #00152B, $alpha: 0.9);
  z-index: 2000;

  .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    // background: linear-gradient(
    //   270deg,
    //   rgba(1, 149, 251, 0) 0%,
    //   rgba(1, 149, 251, 0.35) 100%
    // );
    // border-top: solid 2px white;
    display: flex;
    justify-content: space-between;
    // border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
    margin: 0 !important;
    background-image: url(../../assets/cloudControl/evtDialogTitle.png);
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
}
.eventContent{
  width:calc(100% - 40px);
  height: 50px;
  background: rgba($color: #6C8097, $alpha: 0.4);
  margin: 20px auto;
  font-size: 14px;
  padding:0 20px;
  line-height: 40px;
  .overflowText{
    white-space: nowrap; 
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
.lineBT{
  width: calc(100% - 80px);
  margin: 0 auto;
  // border-bottom: solid 1px white;
  transform: translateY(-30px);
  display: flex;
  >div:nth-of-type(1){
    width: 5%;
    border-bottom:#2DBAF5 solid 1px;
  }
  >div:nth-of-type(2){
    width:90%;
    border-bottom:1px solid rgba($color: #00B0FF, $alpha: 0.2);
  }
  >div:nth-of-type(3){
    width: 5%;
    border-bottom:#2DBAF5 solid 1px;
  }
}
 
  
  

</style>
  