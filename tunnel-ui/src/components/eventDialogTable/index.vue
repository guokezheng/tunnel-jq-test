<template>
  <div>
    <el-dialog
      class="eventBox"
      v-dialogDrag
      :visible.sync="eventTableDialog"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="title">
        事件预警

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
      <div class="contentBox">
        <div class="butBox">
          <div class="butLeftBox">
            <div :class="searchValue == 3 ? 'xz' : ''" @click="handleClick(3)">
              全部
            </div>
            <div>|</div>
            <div :class="searchValue == 1 ? 'xz' : ''" @click="handleClick(1)">
              安全预警
            </div>
            <div>|</div>
            <div :class="searchValue == 0 ? 'xz' : ''" @click="handleClick(0)">
              普通事件
            </div>
            <div>|</div>
            <div :class="searchValue == 2 ? 'xz' : ''" @click="handleClick(2)">
              设备故障
            </div>
          </div>
          <el-button
            class="butRightBox"
            @click="handleBatch"
            :disabled="searchValue == 2"
            v-if="!batchManageType"
          >
            批量执行
          </el-button>
          <div v-if="batchManageType" class="batchManageButton">
            <div @click="closeBatchManageDialog">取消</div>
            <div @click="implementBatchManage">执行</div>
          </div>
        </div>
        <ul
          class="listContent"
          v-infinite-scroll="load"
          infinite-scroll-disabled="disabled"
        >
          <li
            v-for="(item, index) of list"
            :key="index"
            @click="handleSee(item)"
            :style="{
              cursor: item.prevControlType != 2 ? 'pointer' : 'default',
              background: item.click ? 'rgba(0, 0, 0, 0.1)' : 'transparent',
            }"
            style="position: relative"
          >
            <el-row style="color: white">
              <el-col :span="1">
                <div class="iconBox">
                  <img :src="item.iconUrl" />
                </div>
              </el-col>
              <el-col :span="4" style="display: flex">
                <div
                  :style="{
                    color:
                      item.prevControlType == 0
                        ? 'red'
                        : item.prevControlType == 2
                        ? '#F6AC10'
                        : 'rgb(11,146,254)',
                  }"
                  style="width: 100%"
                >
                  {{ item.simplifyName }}
                </div>
                <span
                  class="icon-split"
                  style="padding: 0 12px; color: #3cd3fe"
                >
                  |</span
                >
              </el-col>
              <el-col
                :span="19"
                style="display: flex; justify-content: space-between"
              >
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="item.frameEventTitle"
                  placement="top"
                >
                  <div class="overflowText">{{ item.frameEventTitle }}</div>
                </el-tooltip>
                <div style="float: right; margin-right: 10px">
                  {{ parseTime(item.eventTime, "{yyyy}-{m}-{d} {h}:{i}:{s}") }}
                </div>
              </el-col>
            </el-row>
            <div class="lineBT">
              <div></div>
              <div></div>
              <div></div>
            </div>
          </li>
          <p
            v-if="noMore && total>0"
            style="
              margin-top: 10px;
              font-size: 13px;
              color: #ccc;
              text-align: center;
            "
          >
            没有更多了
          </p>
          <p
            v-if="total == 0"
            style="
              margin-top: 10px;
              font-size: 13px;
              color: #ccc;
              text-align: center;
            "
          >
            暂无事件
          </p>
        </ul>
        <p v-if="loading" class="loading">
          <span></span>
        </p>
      </div>
    </el-dialog>
    <batchDialog ref="batchRef" @clearClick="clearClick"></batchDialog>
  </div>
</template>

    <script>
import { mapState } from "vuex";
import moment from "moment";
import bus from "@/utils/bus";
import {
  updateEvent,
  eventList,
  eventPopFault,
  eventPopAll,
  eventPopData,
} from "@/api/event/event";
import evtdialog from "@/components/eventDialogTable/eventDialog"; //只有数据的弹窗
import batchDialog from "./batchDialog"; //只有数据的弹窗

export default {
  name: "eventDialogTable",
  components: {
    evtdialog,
    batchDialog,
  },
  data() {
    return {
      searchValue: 3,
      loading: false,
      eventTableDialog: true,
      activeName: "0",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      list: [
        // {
        //   eventType: {
        //     iconUrl: "",
        //   },
        // },
      ],
      urls: [],
      videoUrl: require("@/assets/Example/v1.mp4"),
      eventTime: "",
      batchManageType: false,
      itemEvtIdList: [],
      itemEqType: "",
      simplifyName: "",
      eqDirection: "",
      batchDialog: false,
    };
  },
  computed: {
    noMore() {
            //当起始页数大于总页数时停止加载
      if (this.total % 10 == 0) {
        // console.log(2222222)
        return this.pageNum >= parseInt(this.total / 10);
      } else {
        // console.log(33333333)

        return this.pageNum >= parseInt(this.total / 10) + 1;
      }
    },
    disabled() {
      return this.loading || this.noMore;
    },
  },
  created() {
    this.eventTime = moment().format("YYYY-MM-DD");
    this.getList();
  },
  mounted() {
    bus.$on("forceUpdateTable", (id) => {
      let index = this.list.findIndex((item) => {
        if (item.id == id) {
          return true;
        }
      });
      this.list.splice(index, 1);
      if (this.list.length == 0) {
        bus.$emit("closeDialog");
      }
    });
  },
  methods: {
    clearClick(type) {
      for (let item of this.list) {
        item.click = false;
      }
      if (type) {
        this.batchManageType = false;
        this.getList();
      }
      this.simplifyName = ''
      this.$forceUpdate();
    },
    getList(num) {
      // console.log(num,"num")
      let prevControlType = "";
      if (this.searchValue != 3) {
        prevControlType = this.searchValue;
      }
      const params = {
        prevControlType: prevControlType,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      eventPopData(params).then((res) => {
                if (num) {
          this.list = this.list.concat(res.rows);
        } else {
          this.list = res.rows;
        }
        this.loading = false;
        this.total = res.total;
      });
    },
    // 执行批量 并弹窗
    implementBatchManage() {
      if(this.itemEvtIdList.length>0){
        this.$refs.batchRef.init(this.itemEvtIdList, this.handleItem);
      }else{
        this.$modal.msgWarning("至少选择一个预警事件");
      }
    },
    // 取消批量执行
    closeBatchManageDialog() {
      this.itemEvtIdList = [];
      this.batchManageType = false;
      this.clearClick();
    },
    // 批量执行
    handleBatch() {
      this.batchManageType = true;
      this.itemEvtIdList = []
      this.handleItem = {}
    },
    handleSee(item) {
      console.log(item, "item");
      // 不是批量时单独弹窗
      if (item.prevControlType != 2 && !this.batchManageType) {
        setTimeout(() => {
          bus.$emit("getPicId", item.id);
        }, 200);
        bus.$emit("openPicDialog");
        this.eventTableDialog = !this.eventTableDialog;
      } else {
        // 点击批量后 batchManageType == true 可多选弹窗
        if (this.simplifyName) {
          if (this.simplifyName == item.simplifyName && this.eqDirection == item.direction && this.tunnelId == item.tunnelId) {
            const result = this.itemEvtIdList.findIndex((a) => a == item.id);
            if (result === -1) {
              item.click = true;
              this.itemEvtIdList.push(item.id);
              this.$forceUpdate();
            } else {
              this.itemEvtIdList.splice(result, 1);
              item.click = false;
              this.$forceUpdate();
              if (this.itemEvtIdList.length == 0) {
                this.simplifyName = "";
                this.eqDirection = "";
                this.tunnelId = "";
                this.$forceUpdate();
              }
            }
          } else if(this.tunnelId != item.tunnelId){
            this.$modal.msgWarning("请选择同隧道事件");
          } else if (this.eqDirection != item.direction) {
            this.$modal.msgWarning("请选择同方向事件");
          } else if (this.simplifyName != item.simplifyName) {
            this.$modal.msgWarning("请选择同种事件类型");
          }
        } else {
          // 第一次点击时
          if (item.prevControlType != 2) {
            item.click = true;
            this.itemEvtIdList.push(item.id);
            this.simplifyName = item.simplifyName;
            this.handleItem = item;
            this.eqDirection = item.direction;
            this.tunnelId = item.tunnelId;
            this.$forceUpdate();
          }
        }
      }
    },
    getStartTime(time) {
      return moment(time).format("HH:mm:ss");
    },
    load() {
      console.log(1111)
      this.loading = true;
      setTimeout(() => {
        this.pageNum += 1;
        this.loadType = true;
        this.getList("load");
      }, 2000);
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
    closeDialogTable() {
      bus.$emit("closeDialog");
    },

    handleClick(searchValue) {
      this.searchValue = searchValue;
      let prevControlType = "";
      if (searchValue != 3) {
        prevControlType = searchValue;
      }
      const params = {
        prevControlType: prevControlType,
        pageNum: 1,
        pageSize: 10,
      };
      eventPopData(params).then((res) => {
        this.list = res.rows;
        this.loading = false;
        this.total = res.total;
      });
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
  border-top: none;
}
::v-deep .el-dialog:not(.is-fullscreen) {
  margin-top: 0vh !important;
}
::v-deep .el-dialog__header {
  display: none;
}
::v-deep .el-dialog__body {
  padding: 0;
}
.lineBT {
  width: 100%;
  margin: 5px 0px auto;
  display: flex;
  > div:nth-of-type(1) {
    width: 3%;
    border-bottom: #2dbaf5 solid 1px;
  }
  > div:nth-of-type(2) {
    width: 94%;
    border-bottom: 1px solid rgba($color: #00b0ff, $alpha: 0.2);
  }
  > div:nth-of-type(3) {
    width: 3%;
    border-bottom: #2dbaf5 solid 1px;
  }
}
.contentBox {
  width: 100%;
  padding: 0 15px;
  .butBox {
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: 0px 4px;
    background: #44576f;
    border-radius: 4px;
    margin-top: 20px;
    font-size: 14px;
    box-shadow: 0 0.125rem 0.25rem 0 #000;
    align-items: center;
    .butLeftBox {
      display: flex;
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
    .butRightBox {
      width: 80px;
      height: 28px;
      border: solid 1px #3cd3fe;
      padding: 0px 10px;
      border-radius: 3px;
      line-height: 27px;
      color: #3cd3fe;
      background: transparent;
    }
    .butRightBox:hover {
      background-color: #3cd3fe;
      color: #fff;
    }
    .batchManageButton {
      width: 120px;
      display: flex;
      justify-content: space-around;
      padding: 0 5px;
      color: #e1feff;
      background: #44576f !important;
      border: 1px solid #00c8ff;
      font-size: 12px;
      height: 32px;
      align-items: center;
      margin-right: 10px;
      border-radius: 3px;
      color: white;
      text-align: center;
      > div {
        width: 50px;
        height: 20px;
        border-radius: 13px;
        line-height: 20px;
        cursor: pointer;
      }
      > div:nth-of-type(1) {
        background: #c8c8c8;
      }
      > div:nth-of-type(2) {
        background: #00b0ff;
      }
    }
  }
  .listContent {
    max-height: 301px;
    overflow: auto;
    background: #44576f;
    box-shadow: 0 0.125rem 0.25rem 0 #000;
    padding: 4px 10px;
    > li {
      list-style: none;
      padding: 10px 4px;
      padding-bottom: 0px;
    }
    > li:hover {
      background-color: rgba(0, 0, 0, 0.2);
      background: rgba($color: #000000, $alpha: 0.1);
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
  background-color: rgba($color: #000000, $alpha: 0.1);
}
::v-deep .eventBox {
  width: 570px;
  max-height: 430px;
  position: absolute;
  top: 0px;
  left: calc(100% - 600px);
  .el-dialog__body {
    padding: 0 !important;
    width: 100% !important;
    margin: 0 !important;
    height: 100% !important;
  }
  .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    display: flex;
    justify-content: space-between;
    margin: 0 !important;
    background-image: url(../../assets/cloudControl/evtDialogTitle.png);
    background-repeat: no-repeat;
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
      border-bottom: 1px solid rgba($color: #00c8fe, $alpha: 0.4);
    }
    .el-table__body-wrapper {
      overflow-y: auto;
    }
  }
}
.overflowText {
  width: 248px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.iconBox {
  width: 16px;
  height: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    height: 100%;
  }
}
</style>
