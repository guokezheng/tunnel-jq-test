<template>
  <div class="tunnelRanking-container">
    <div class="contentTitle">
      隧道能耗
      <i>energy consumption</i>
    </div>
    <div class="scroll-Box">
      <!-- <el-scrollbar>
        <div class="box-title">
          <div>排名</div>
          <div>隧道</div>
          <div>
            能耗
            <span style="font-size:0.6vw;">(Kwh/年)</span>
          </div>
        </div>
        <div class="box-title" v-for="item in listData" :key="item.id">
          <div>{{item.id+1}}</div>
          <div>{{item.name}}</div>
          <div>{{item.energyConsumption}}</div>
        </div>
      </el-scrollbar> -->
      <div class="listHeader">
        <el-row type="flex" style="font-size:0.8vw;">
          <el-col style="width:13vw;padding-left:1vw;">排名</el-col>
          <el-col>隧道</el-col>
          <el-col>能耗<span style="font-size:0.6vw;">(Kwh/年)</span></el-col>
        </el-row>
      </div>
      <vue-seamless-scroll :class-option="defaultOption" class="listContent" :data="listData">
        <el-row
          type="flex"
          v-for="(item, index) in listData"
          :key="index"
          :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}"
        >
          <el-col style="width:12vw;padding-left:1.3vw;">{{item.id+1}}</el-col>
          <el-col>{{item.name}}</el-col>
          <el-col>{{item.energyConsumption}}</el-col>
        </el-row>
      </vue-seamless-scroll>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      listData: [
        {
          id: 0,
          name: "姚家峪隧道",
          energyConsumption: 19431.48
        },
        {
          id: 1,
          name: "毓秀山隧道",
          energyConsumption: 18483.24
        },
        {
          id: 2,
          name: "洪河隧道",
          energyConsumption: 18374.28
        },
        {
          id: 3,
          name: "滨莱高速",
          energyConsumption: 17492.42
        },
        {
          id: 4,
          name: "望海石隧道",
          energyConsumption: 16232.12
        },
        {
          id: 5,
          name: "中庄隧道",
          energyConsumption: 15837.83
        },
        {
          id: 6,
          name: "马公祠隧道",
          energyConsumption: 14827.32
        },
        {
          id: 7,
          name: "乐疃隧道",
          energyConsumption: 14758.23
        },
        {
          id: 8,
          name: "樵岭前隧道",
          energyConsumption: 14539.75
        },
        {
          id: 9,
          name: "佛羊岭隧道",
          energyConsumption: 14348.75
        },
        {
          id: 10,
          name: "迎春坡隧道",
          energyConsumption: 14102.32
        },
        {
          id: 11,
          name: "龙山寨隧道",
          energyConsumption: 13975.74
        }
      ]
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: this.listData.length, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      };
    }
  }
};
</script>

<style lang="less" scoped>
.tunnelRanking-container {
  height: 100%;
  padding: 0.1px;
  font-size: 0.8vw;
  .scroll-Box {
    // padding: 0 1vw 1vw;
    overflow: hidden;
    width: 100%;
    height: calc(100% - 2vw);

    .box-title {
      display: flex;
      justify-content: start;
      padding-left: 1vw;
      > div {
        margin-bottom: 0.6vw;
      }
      > div:first-child {
        width: 24%;
      }
      >div:nth-child(2) {
        width: 40%;
      }
      >div:last-child {
        width: 36%;
      }
    }

    /deep/ .el-scrollbar {
      height: 100%;
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
      .el-scrollbar__thumb {
        background-color: #027dec;
      }
    }
  }
}
</style>
