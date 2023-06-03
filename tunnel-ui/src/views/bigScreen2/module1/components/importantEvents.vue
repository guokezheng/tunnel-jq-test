<template>
  <div class="box">
    <div class="row1Box">本日<span>{{ list.count }}</span>起重大事故</div>
    <div class="row2Box">
      <div class="yellowBox">
        <div><span>{{ list.handleWarnCount }}</span><span>/</span><span>{{ list.warnCount }}</span></div>
        <div>安全预警</div>
      </div>
      <div class="yellowBox">
        <div><span>{{ list.handleEventCount }}</span><span>/</span><span>{{ list.eventCount }}</span></div>
        <div>交通事件</div>
      </div>
    </div>
    <el-table :data="list.list" height="220" size="mini" class="bigScreenTable">
      <el-table-column prop="eventType" label="事件类型" width="80" align="center" />
      <el-table-column
        label="内容"
        width="138"
        show-overflow-tooltip
        align="center"
      >
      <template slot-scope="scope">
         <span style="margin-right:4px">{{ scope.row.tunnelName}}</span>
         <span>{{ getDirection(scope.row.direction)}}</span>
      </template></el-table-column>
      <el-table-column prop="eventState" label="状态" width="62" align="center" :formatter="eventStateFormat"/>
      <el-table-column prop="createTime" label="发生时间" width="75" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import {majorEvent} from "@/api/bigScreen/model1"
export default {
  data() {
    return {
      eventStateList:[],
      directionList:[],
      list:{}
    };
  },
  created() {
    this.getDicts("sd_event_state").then((data) => {
      this.eventStateList = data.data;
    });
    this.getDicts("sd_direction").then((data) => {
      this.directionList = data.data;
    });
    this.getList()
  },
  methods: {
    getList(){
      majorEvent().then((res)=>{
        this.list = res.data
      })
    },
    eventStateFormat(row){
      return this.selectDictLabel(this.eventStateList, row.eventState);
    },
    getDirection(num) {
      for (let item of this.directionList) {
        if (num == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
  },
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  .row1Box {
    width: 100%;
    height: 40px;
    border: dashed 1px rgba($color: #72d8b9, $alpha: 0.7);
    background: rgba($color: #72d8b9, $alpha: 0.1);
    text-align: center;
    line-height: 40px;
    span {
      color: red;
      font-size: 20px;
      font-weight: bold;
      padding: 0 2px;
    }
  }
  .row2Box {
    width: 100%;
    height: 58px;
    display: flex;
    justify-content: space-between;
    margin-top: 4px;
    .yellowBox {
      border: dashed 1px rgba($color: #ffb238, $alpha: 0.7);
      background: rgba($color: #ffb238, $alpha: 0.1);

      width: 49%;
      height: 100%;
      text-align: center;
      padding-top: 6px;
      > div {
        span:first-of-type {
          color: #fed37d;
          font-size: 20px;
          font-weight: bold;
        }
        span:last-of-type {
          color: white;
          font-size: 18px;
        }
      }
    }
  }
  // 大屏表格
  ::v-deep .bigScreenTable {
    margin-top: 4px;
    color: #9ba0bc;
    background: transparent !important;
    .el-table__header {
      width: 100% !important;
    }
    .el-table__header-wrapper .el-table__cell:nth-last-of-type(2) {
      border-right: 1px solid rgba(225, 228, 230, 0.16) !important;
    }
    .el-table__header-wrapper th,
    .el-table__fixed-header-wrapper th {
      background-color: #01457e !important;
      height: 2vh !important;
      color: #fff;
      border-top: 1px solid rgba(225, 228, 230, 0.16) !important;
    }
    td:last-of-type {
      border-right: none !important;
    }

    th.el-table__cell.is-leaf,
    td.el-table__cell {
      border-bottom: none !important;
    }
    tr {
      background-color: transparent !important;
    }
    tr:nth-of-type(2n){
      background: rgba($color: #01457e, $alpha: 0.3) !important;
    }
    th.el-table__cell.gutter {
      width: 0px !important;
      border-left: none !important;
    }
    .el-table__cell {
      line-height: 2vh !important;
      padding: 3px 0 !important;
      border-left: none !important;
    }
    ::-webkit-scrollbar {
      width: 0px !important;
    }
  }
}
</style>