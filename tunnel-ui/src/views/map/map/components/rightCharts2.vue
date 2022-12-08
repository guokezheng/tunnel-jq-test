<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>实时感知事件</div>
      <div>real time event awareness</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <el-table
      :data="dataList"
      min-height="150"
      empty-text="暂无感知事件"
      height="180"
    >
      <el-table-column
        label="发生时间"
        align="center"
        prop="time"
        width="100"
      />
      <el-table-column label="道路桩号" align="center" prop="stakeNum" />
      <el-table-column label="事件类型" align="center" prop="eqType" />
      <el-table-column label="状态" align="center" prop="state" />
    </el-table>
  </div>
</template>
  <script>
export default {
  data() {
    return {
      src: require("@/assets/cloudControl/dialogHeader.png"),
      iconSrc: require("@/assets/icons/energyIcon.png"),
      dataList: [
        {
          time: "14:17:00",
          stakeNum: "K600+000",
          eqType: "交通事故",
          state: "处置中",
        },
        {
          time: "14:17:00",
          stakeNum: "K600+000",
          eqType: "交通事故",
          state: "处置中",
        },
        {
          time: "14:17:00",
          stakeNum: "K600+000",
          eqType: "交通事故",
          state: "处置中",
        },
        {
          time: "14:17:00",
          stakeNum: "K600+000",
          eqType: "交通事故",
          state: "处置中",
        },
        {
          time: "14:17:00",
          stakeNum: "K600+000",
          eqType: "交通事故",
          state: "处置中",
        },
      ],
    };
  },
  methods: {
  
  },
};
</script>
  <style scoped lang="scss">
.mapCharts {
  height: 25%;
  width: 100%;
  transform: translateX(-18px);
  position: relative;
  .title {
    color: white;
    width: 100%;
    height: 30px;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
    align-items: center;

    > div:nth-of-type(1) {
      margin-left: 10px;
      font-size: 14px;
    }
    > div:nth-of-type(2) {
      text-transform: uppercase;
      margin-left: 10px;
      opacity: 0.35;
      font-size: 10px;
    }
    > img {
      height: 22px;
      margin-left: 8px;
    }
  }
  .titleIcon {
    position: absolute;
    top: 0;
    right: 0;
    height: 30px;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
  ::v-deep .el-table {
    width: 97%;
    margin: 8px;
    color:white;
    tr{
      background-color: #010F23 !important;
    }
    // .el-table__header-wrapper .el-table__cell
    .el-table__header-wrapper {
      line-height: 20px;
      .cell {
        line-height: 14px;
      }
      .el-table__cell{
        border: none !important;
      }
      th {
        background: rgba(1, 121, 255, 0.18) !important;
        color:#01AAFD;
      }
    }
    th.el-table__cell.is-leaf, td.el-table__cell{
      border-bottom:none !important;
    }
    .el-table__body {
      width: 100% !important;
    }
    .el-table__body-wrapper {
      .cell {
        line-height: 14px;
      }
      .el-table__cell{
        border: none !important;

      }
    }
    .el-table--enable-row-hover .el-table__body tr:hover > td {
      background-color: #4395c1 !important;
      color: black;
      /* color: #f19944; */ /* 设置文字颜色，可以选择不设置 */
    }

    /*table滚动条背景色 */
    .el-table__body-wrapper::-webkit-scrollbar {
      background-color: #c4e8f6;
      width: 1px;
      height: 10px;
    }
  
    /* table滚动条的滑块*/
    .el-table__body-wrapper::-webkit-scrollbar-thumb {
      background-color: #00c2ff;
    }
   
  }
  .el-table{
    border: none !important;
    background: transparent !important;
  }

  // ::v-deep .el-table th.el-table__cell.is-leaf, .el-table td.el-table__cell{
  //     border-bottom:none !important;
  //   }
  ::v-deep .el-table::before {
    left: 0;
    bottom: 0;
    width: 100%;
    height: 0px;
  }
  ::v-deep .el-table--enable-row-hover .el-table__body tr:hover > td {
    background: rgba(1, 121, 255, 0.18) !important;

      color: white;
      cursor: pointer;
    }
}
</style>
  