<template>
  <div>
    <el-dialog class="eventBox" v-dialogDrag :visible.sync="eventTableDialog" :modal-append-to-body='false' >
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
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane
          :label="item.dictLabel"
          :name="item.dictValue"
          v-for="(item, index) in tabList"
          :key="index"
        >
        
          <!-- <el-table :data="item.list" class="eventTable" max-height="300" :row-class-name="tableRowClassName">
            <el-table-column
              label="隧道名称"
              align="center"
              prop="tunnels.tunnelName"
            />
            <el-table-column label="事件桩号" align="center" prop="stakeNum" />

            <el-table-column
              label="事件类型"
              align="center"
              prop="eventType.eventType"
            />
            <el-table-column label="车道号" align="center" prop="laneNo" width="70px"/>
            <el-table-column
              label="事件经度"
              align="center"
              prop="eventLongitude"
            />
            <el-table-column
              label="事件纬度"
              align="center"
              prop="eventLatitude"
            />
            <el-table-column label="开始时间" align="center" prop="startTime">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.startTime, '{h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="结束时间" align="center" prop="endTime" >
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.endTime, '{h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
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
                  @click="handleSee(scope.row.id)"
                  >查 看
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
          </el-table> -->
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- <evtdialog
      ref="evtdialog"
      class="eventClass"
      @fMethod="fatherMethod"
    ></evtdialog> -->
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
      eventTableDialog: false,
      activeName: "0",
      tabList: [
        // {
        //   dictLabel:'雷达',
        //   dictValue:'1',
        //   list:[
        //     {
        //       tunnels:{
        //         tunnelName:'666'
        //       },
        //       eventType:{
        //         eventType:'222'
        //       },
        //       stakeNum:1,
        //     }
        //   ]
        // }
      ],
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
      console.log(event, "websockt事件表格弹窗");
      for (let i = 0; i < event.length; i++) {
        for (let z = 0; z < this.tabList.length; z++) {
          if (event[i].eventSource == this.tabList[z].dictValue) {
            this.tabList[z].list.unshift(event[i]);
          }
        }
      }
      console.log(this.tabList);
      // this.eventTableDialog = true;
      // this.showTable = true
    },
    deep: true,
  },
  created() {
    this.getDicts("sd_event_source").then((data) => {
      console.log(data, "事件来源");
      this.tabList = data.data;
      this.tabList.forEach((item) => {
        item.list = [];
      });
    });
  },
  mounted(){
    bus.$on('closeTableDialog', () => {
       this.eventTableDialog = false
    })
    bus.$on('openTableDialog', () => {
       this.eventTableDialog = true
    })
  },
  methods: {
    handleSee(id) {
      bus.$emit("openPicDialog", id);
      bus.$emit("getPicId",id)
    },

    // 忽略事件
    handleIgnore(event) {
      if (event) {
        const param = {
          id: event.id,
          eventState: "2",
        };
        updateEvent(param).then((response) => {
          this.$modal.msgSuccess("已成功忽略");
        });
        this.tabList.forEach((item) => {
          item.list.forEach((its) =>{
            if(its.id == event.id){
              item.list.splice(its,1)
            }
          })
        });
        // bus.$emit("getEvtList")
      } else {
        this.$modal.msgError("没有接收到事件id");
      }
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
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: event.id },
      });
      bus.$emit("closeDialog", false);
      this.eventTableDialog = false
    },
    closeDialogTable() {
      bus.$emit("closeDialog", false);
      this.eventTableDialog = false
    },
   
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex%2 == 0) {
      return 'tableEvenRow';
      } else {
      return "tableOddRow";
      }
    },
  },
};
</script>
  
  <style lang="scss" scoped>
::v-deep .el-dialog {
  width: 100% !important;
  height: 100%;
  position: absolute !important;
  left: 0 !important;
  margin: 0;
  box-shadow: none;
  background: rgba($color: #00152B, $alpha: 0.6);
}
::v-deep .el-dialog:not(.is-fullscreen) {
  margin-top: 0vh !important;
}
::v-deep .el-dialog__header {
  display: none;
}
::v-deep .el-dialog__body {
  padding: 0;
  // background-color: rgba($color: #00152B, $alpha: 0.6);
}
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
::v-deep .eventBox {
  width: 570px;
  max-height: 400px;
  border: solid 1px rgba($color: #0198ff, $alpha: 0.5);
  position: absolute;
  top: 0px;
  left: calc(100% - 600px);
  // background-color: #071930;
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
    margin-bottom: 20px;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
  .el-tabs {
    width: 94%;
    margin: 0 auto;
  }
  .el-tabs__nav-scroll{
    background: rgba($color: #6C8097, $alpha: 0.4);
    border-radius: 4px;
  }
  //   表格内容文字
  .eventTable,
  .el-table {
    color: white;
    background: #071930 !important;
    // 表头下划边框
    th.el-table__cell.is-leaf,
    .el-table td.el-table__cell {
      border-bottom: 1px solid #00adff;
    }
    .el-table__header-wrapper{
      display: none;
    }
    // 表头背景
    .el-table__header-wrapper th,
    .el-table .el-table__fixed-header-wrapper th {
      background-color: #00adff;
      color: white;
    }
    // 表格内容背景色
    tr {
      background-color: #071930 !important;
    }
    tr:hover > td {
      background-color: #0e2c53 !important;
    }
    .el-table__empty-block {
      background-color: rgba($color: #6C8097, $alpha: 0.1);
      color: white;
    }
    .el-button--text {
      color: white;
    }
    .el-table__body-wrapper .el-table__cell {
      // border: 1px solid rgba($color: #00c8fe, $alpha: 0.4);
      border-bottom: 1px solid rgba($color: #00c8fe, $alpha: 0.4);
    }
    .el-table__body-wrapper{
      overflow-y:auto;
    }
  }
}
</style>
  