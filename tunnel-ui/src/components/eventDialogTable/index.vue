<template>
  <div>
    <div class="eventBox">
      <div class="title">
        事件详情
        <img
          src="../../assets/cloudControl/dialogHeader.png"
          style="height: 30px"
        />
      </div>
      <div class="blueLine"></div>
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane
          :label="item.dictLabel"
          :name="item.dictValue"
          v-for="(item, index) in tabList"
          :key="index"
        >
          <el-table :data="item.list"  class="eventTable" min-height="200" >
            <el-table-column label="隧道名称" align="center" prop="tunnels.tunnelName" />
            <el-table-column label="隧道名称" align="center" prop="tunnelStationName" />
            <el-table-column label="事件来源" align="center" prop="eventSource" />
            <el-table-column label="事件桩号" align="center" prop="stakeNum" />

            <el-table-column
              label="事件类型"
              align="center"
              prop="eventType.eventType"
            />
            <el-table-column label="车道号" align="center" prop="laneNo" />
            <el-table-column
              label="事件位置经度"
              align="center"
              prop="eventLongitude"
            />
            <el-table-column
              label="事件位置纬度"
              align="center"
              prop="eventLatitude"
            />
            <el-table-column
              label="事件开始时间"
              align="center"
              prop="startTime"
            />
            <el-table-column
              label="事件结束时间"
              align="center"
              prop="endTime"
            />
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-thumb"
                  @click="handleSee(scope.row)"
                  >查 看
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-thumb"
                  @click="handleDispatch(scope.row)"
                  >处理
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleIgnore(scope.row)"
                  >忽略
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <evtdialog ref="evtdialog" v-show="eventDialog" class="eventClass"></evtdialog>

  </div>
</template>
  
  <script>
import { mapState } from "vuex";
import bus from "@/utils/bus";
import { loadPicture } from "@/api/equipment/type/api.js";
import { updateEvent } from "@/api/event/event";
import evtdialog from "@/components/eventDialogTable/eventDialog"; //只有数据的弹窗

export default {
  name: "eventDialogTable",
  components: {
    evtdialog
  },
  data() {
    return {
      activeName: "0",
      tabList: [],
      eventDialog:false,
      // eventMes:[],
      eventList: [
    
      ],
      urls: [],
      videoUrl: require("@/assets/Example/v1.mp4"),
     evtList:[]
    };
  },
  computed: {
    ...mapState({
      WjEvent: (state) => state.websocket.WjEvent,
    }),
  },
  watch: {
    WjEvent(event) {
      console.log(event, "websockt接收数据");
      for(let i=0;i<event.length;i++){
        for(let z=0;z<this.tabList.length;z++){
          if(event[i].eventSource == this.tabList[z].dictValue){
            this.tabList[z].list.push(event[i])
          }
        }
      }
      console.log(this.tabList)
    },
    deep: true,
  },
  created() {
    this.getDicts("sd_event_source").then((data) => {
      console.log(data, "事件来源");
      this.tabList = data.data;
      this.tabList.forEach(item=>{
        item.list = [];
      })
    });
   
  },
  methods: {
    getEventList(event){
      console.log(event,'this.eventList')
    
      for(var item of event){
        if(item.eventSource == this.activeName){
          this.evtList.push(item)
        }
      }
      console.log(this.evtList,"evtList");
      return this.evtList
    },
    
    handleSee(row){
      console.log(row)
      // this.eventMes = row;
      this.$refs.evtdialog.row11 = row   
      this.eventDialog = true
    },

    // 忽略事件
    handleIgnore(event) {
      const param = {
        id: event.id,
        eventState: "2",
      };
      updateEvent(param).then((response) => {
        console.log(response, "修改状态");
        this.$modal.msgSuccess("已成功忽略");
      });
      bus.$emit("closeDialog", false);
      this.eventDialog = false

    },
    // 处理 跳转应急调度
    handleDispatch(event) {
      const param = {
        id: event.id,
        eventState: "0",
      };
      updateEvent(param).then((response) => {
        console.log(response, "修改状态");
        this.$modal.msgSuccess("开始处理事件");
      });
      bus.$emit("closeDialog", false);
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: event.id },
      });
    },
    /* 请求图片base64地址*/
    picture(fileUrl) {
      return new Promise((resolve, reject) => {
        loadPicture({
          url: fileUrl,
        }).then((response) => {
          if (response.code == 200) {
            let base64 = response.msg;
            resolve(base64); //不可缺少
          }
        });
      });
      return resolve(base64);
    },
    handleClick(tab, event) {
      console.log(tab, event);
     
    },
  },
};
</script>
  
  <style lang="scss" scoped>
    .eventClass{
   position: absolute;top: 0;left: 0;width: 100%;height: 100%;z-index: 100;
   // border: solid 10px rgba($color: #14B7EA, $alpha: 0.3);
   background-color: rgba($color: #000000, $alpha: 0.1);
   // border-radius: 10px;
 }
::v-deep .eventBox {
  width: 52%;
  height: 660px;
  border: solid 1px rgba($color: #0198ff, $alpha: 0.5);
  position: absolute;
  top: 10%;
  left: 25%;
  background-color: #071930;
  > .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    justify-content: space-between;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    margin-bottom: 20px;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
  .el-tabs {
    width: 94%;
    margin: 0 auto;
  }


//   表格内容文字
  .eventTable,.el-table{
    color:white;
    // 表头下划边框
    th.el-table__cell.is-leaf, .el-table td.el-table__cell{
        border-bottom: 1px solid #00ADFF
    }
    // 表头背景
    .el-table__header-wrapper th, .el-table .el-table__fixed-header-wrapper th{
        background-color: #00ADFF;
    color: white;
    }
    // 表格内容背景色
    tr{
        background-color: #071930;
    }
    tr:hover > td {
    background-color: #0E2C53 !important;
}
.el-table__empty-block{
  background-color: #071930;
  color:white;
}
.el-button--text{
  color: white;
}
.el-table__body-wrapper .el-table__cell{
    border: 1px solid rgba($color: #00c8fe, $alpha: 0.4);

  }
  }
}
</style>
  