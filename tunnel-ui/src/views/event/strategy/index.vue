<template>
  <div class="app-container">
    <el-tabs v-model="strategyActive" @tab-click="handleClick">
      <el-tab-pane label="手动控制" name="shoudong">
        <indexTable :tableType="strategyActive"></indexTable>
      </el-tab-pane>
      <el-tab-pane label="定时控制" name="dingshi">
        <indexTable :tableType="strategyActive"></indexTable>
      </el-tab-pane>
      <el-tab-pane label="自动触发" name="zidong">
        <indexTable :tableType="strategyActive"></indexTable>
      </el-tab-pane>
      <!--                <el-tab-pane label="预警策略" name="yujing"></el-tab-pane>-->
    </el-tabs>
  </div>
</template>

<script>

import indexTable from "./event/indexTable.vue";

export default {
  components: {
    indexTable
  },
  data() {
    return{
      strategyActive:"shoudong"
    }
  },
  created() {

  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
    document.addEventListener("click", this.bodyCloseMenus1);
  },
  methods: {
    handleClick(e){
      console.log(e.name)
      this.strategyActive = e.name
     debugger
    }
  },

  watch:{

  }
};
</script>
<style>
.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}

.el-table .cell {
  white-space: pre-line;
}
</style>
<style scoped lang="scss">
::v-deep .el-dialog .el-dialog__header {
  background-image: url(../../../assets/cloudControl/dialogHeader.png);
  background-repeat: no-repeat;
  background-position-x: right;
}

::v-deep .el-tabs {
  height: 100%;

  .el-tabs__item {
    height: 4vh;
    font-size: 0.7vw;
  }

  .el-tabs__content {
    height: calc(100% - 5vh);

    .el-tab-pane {
      height: 100%;

      .contentListBox {
        height: 60vh;
        overflow-x: hidden;
        overflow-y: auto;

        .contentBox {
          height: 14vh;
        }
      }
    }
  }
}

::v-deep .el-tabs__header {
  margin: 0 0 8px !important;
}
</style>

