<template>
  <div>
    <el-dialog
      class="eventBox"
      v-dialogDrag
      :visible.sync="eventTableDialog"
      :modal-append-to-body="false"
    >
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
      <div class="contentBox">
        <div class="butBox">
          <div :class="searchValue == 3 ? 'xz' : ''" @click="handleClick(3)">
            全部
          </div>
          <div :class="searchValue == 1 ? 'xz' : ''" @click="handleClick(1)">
            主动安全
          </div>
          <div :class="searchValue == 0 ? 'xz' : ''" @click="handleClick(0)">
            交通事件
          </div>
          <div :class="searchValue == 2 ? 'xz' : ''" @click="handleClick(2)">
            设备故障
          </div>
        </div>
        <ul class="listContent" 
            v-infinite-scroll="load"
            infinite-scroll-disabled="disabled">
          <li v-for="(item, index) of list" :key="index">
            <el-row style="color: white">
              <el-col :span="2">
                <img
                  :src="item.iconUrl"
                  style="width: 20px; height: 20px; transform: translateY(5px)"
                />
              </el-col>
              <el-col :span="2">
                <div>
                  {{ item.eventType.simplifyName }}
                </div>
              </el-col>
              <el-col :span="16">
                <div class="overflowText">{{ item.eventTitle }}</div>
                <div style="float: right; margin-right: 16px">
                  {{ item.startTime }}
                </div>
              </el-col>
              <el-col :span="2">
                <el-button size="mini" type="text" @click="handleSee(item.id)"
                  >查看
                </el-button>
              </el-col>
              <el-col :span="2">
                <el-button
                  size="mini"
                  type="text"
                  @click="handleIgnore(item.id)"
                  >忽略
                </el-button>
              </el-col>
            </el-row>
            <div class="lineBT">
              <div></div>
              <div></div>
              <div></div>
            </div>
          </li>
          <p v-if="noMore" style="margin-top: 10px; font-size: 13px; color: #ccc; text-align: center;">
          没有更多了
        </p>
        </ul>
        <p v-if="loading"  class="loading">
          <span></span>
        </p>
        
        <!-- <div class="listContent">
          <div v-for="(item, index) of list" :key="index" >
            <el-row style="color: white">
              <el-col :span="2">
                <img
                  :src="item.iconUrl"
                  style="width: 20px; height: 20px; transform: translateY(5px)"
                />
              </el-col>
              <el-col :span="2">
                <div v-if="searchValue != 3">
                  {{ item.eventType.simplifyName }}
                </div>
                <div v-else-if="searchValue == 3">{{ item.simplifyName }}</div>
              </el-col>
              <el-col :span="16">
                <div class="overflowText">{{ item.eventTitle }}</div>
                <div style="float: right; margin-right: 16px">
                  {{ item.startTime }}
                </div>
              </el-col>
              <el-col :span="2">
                <el-button size="mini" type="text" @click="handleSee(item.id)"
                  >查看
                </el-button>
              </el-col>
              <el-col :span="2">
                <el-button
                  size="mini"
                  type="text"
                  @click="handleIgnore(item.id)"
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
        </div> -->
      </div>

      <!-- <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane
          :label="item.dictLabel"
          :name="item.dictValue"
          v-for="(item, index) in tabList"
          :key="index"
        > -->

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
      <!-- </el-tab-pane> -->
      <!-- </el-tabs> -->
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
import moment from 'moment'
import bus from "@/utils/bus";
import { updateEvent, eventList, eventPopAll } from "@/api/event/event";
import evtdialog from "@/components/eventDialogTable/eventDialog"; //只有数据的弹窗

export default {
  name: "eventDialogTable",
  components: {
    evtdialog,
  },
  data() {
    return {
      searchValue: 3,
      loading: false,
      // showTable:false,
      eventTableDialog: true,
      activeName: "0",
      pageNum: 1,
      total: 0,
      list: [],
      urls: [],
      videoUrl: require("@/assets/Example/v1.mp4"),
      startTime:''
    };
  },
  computed: {
    noMore() {
      //当起始页数大于总页数时停止加载
      console.log(this.pageNum, parseInt(this.total/10));
      if(this.total%10==0){
        return this.pageNum >= parseInt(this.total/10);
      }else{
        return this.pageNum >= parseInt(this.total/10)+1;

      }
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  created() {
    this.startTime = moment().format('YYYY-MM-DD')
    console.log(this.startTime)
    eventList(this.searchValue, this.pageNum,this.startTime).then((res) => {
      console.log(res, "事件弹窗分类数组");
      this.list = res.rows;
      this.total = res.total;
      this.loading = false;
    });
    
  },
  mounted() {
    bus.$on('forceUpdateTable', (id) => {
      let index = this.list.findIndex((item) => {
          if (item.id == id) {
            return true;
          }
        });
        this.list.splice(index, 1);
        if(this.list.length == 0){
          bus.$emit("closeDialog");
        }
    })
    // bus.$on('closeTableDialog', () => {
    //  this.eventTableDialog = false
    // })
    // bus.$on('openTableDialog', () => {
    //  this.eventTableDialog = true
    // })
  },
  methods: {
    load() {
      this.loading = true;
      setTimeout(() => {
        this.pageNum += 1;

        eventList(this.searchValue, this.pageNum,this.startTime).then((res) => {
          console.log(res, "事件弹窗分类数组");
          // this.list.push(res.rows);
          this.list = this.list.concat(res.rows);
          this.$forceUpdate();
          this.loading = false
        });
      }, 2000);
    },
    handleSee(id) {
      setTimeout(() => {
        bus.$emit("getPicId", id);
      }, 200);
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
        let index = this.list.findIndex((item) => {
          if (item.id == id) {
            return true;
          }
        });
        this.list.splice(index, 1);
        bus.$emit("getEvtList");

        this.$forceUpdate();
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
    //   // this.eventTableDialog = false
    // },
    closeDialogTable() {
      // this.eventTableDialog = false
      bus.$emit("closeDialog");
    },

    handleClick(searchValue) {
      this.searchValue = searchValue;

      // if (searchValue == 3) {
      //   eventPopAll().then((res) => {
      //     console.log(res, "全部事件");
      //     this.list = res.data;
      //   });
      // } else {
      const pageNum = 1;
      eventList(searchValue, pageNum,this.startTime).then((res) => {
        console.log(res, "事件弹窗分类数组");
        this.list = res.rows;
        this.total = res.total;
        this.loading = false;
      });
      // }
    },
    // 表格的行样式
    // tableRowClassName({ row, rowIndex }) {
    //   if (rowIndex % 2 == 0) {
    //     return "tableEvenRow";
    //   } else {
    //     return "tableOddRow";
    //   }
    // },
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
  background: rgba($color: #00152b, $alpha: 0.6);
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
.lineBT {
  width: 100%;
  margin: 5px 0px auto;
  // border-bottom: solid 1px white;
  // transform: translateY(-30px);
  display: flex;
  > div:nth-of-type(1) {
    width: 5%;
    border-bottom: #2dbaf5 solid 1px;
  }
  > div:nth-of-type(2) {
    width: 90%;
    border-bottom: 1px solid rgba($color: #00b0ff, $alpha: 0.2);
  }
  > div:nth-of-type(3) {
    width: 5%;
    border-bottom: #2dbaf5 solid 1px;
  }
}
.contentBox {
  width: 100%;
  // height: 100%;
  padding: 0 15px;
  .butBox {
    width: 100%;
    display: flex;
    padding: 4px 4px;
    background: #6c8097;
    border-radius: 4px;
    // margin-bottom: 10px;
    margin-top: 20px;
    font-size: 14px;
    // justify-content: space-between;
    div {
      padding: 6px 10px;
      color: #3cd3fe;
      letter-spacing: 1px;
      cursor: pointer;
    }
    .xz {
      color: #ffffff !important;
    }
  }
  .listContent {
    max-height: 290px;
    overflow: auto;
    background: rgba($color: #6c8097, $alpha: 0.3);
    padding-left: 0;
    > li {
      // margin-bottom: 6px;
      list-style: none;
      padding: 10px;
      padding-bottom: 0px;
    }
  }
  /*table滚动条背景色 */
  ::-webkit-scrollbar {
    width: 4px;
    background-color: #c4e8f6;
  }

  /* table滚动条的滑块*/
  ::-webkit-scrollbar-thumb {
    background-color: #00c2ff;
  }
}
.loading {
  position: absolute;
  top: 230px;
  left: 284px;
  span {
    display: inline-block;
    width: 20px;
    height: 20px;
    border: 2px solid #409eff;
    border-left: transparent;
    animation: zhuan 0.5s linear infinite;
    border-radius: 50%;
  }
}
@keyframes zhuan {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
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
  max-height: 430px;
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
  .el-tabs__nav-scroll {
    background: rgba($color: #6c8097, $alpha: 0.4);
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
    // .el-table__header-wrapper{
    //   display: none;
    // }
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
      background-color: rgba($color: #6c8097, $alpha: 0.1);
      color: white;
    }
    .el-button--text {
      color: white;
    }
    .el-table__body-wrapper .el-table__cell {
      // border: 1px solid rgba($color: #00c8fe, $alpha: 0.4);
      border-bottom: 1px solid rgba($color: #00c8fe, $alpha: 0.4);
    }
    .el-table__body-wrapper {
      overflow-y: auto;
    }
  }
}
</style>
    