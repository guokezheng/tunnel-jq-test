<template>
  <div class="controlRecord-container">
    <div class="title">近12h控制记录</div>
    <div class="scrollBox">
      <div class="listHeader">
        <el-row
          type="flex"
          style="font-size: 0.8vw; background-color: rgba(255, 255, 255, 0.2)"
        >
          <el-col style="width: 16vw; padding-left: 0.4vw">隧道名称</el-col>
          <el-col>操作内容</el-col>
          <el-col style="width: 20vw">发生时间</el-col>
        </el-row>
      </div>
      <vue-seamless-scroll
        :class-option="defaultOption"
        class="listContent"
        :data="listData"
      >
        <el-row
          type="flex"
          v-for="(item, index) in listData"
          :key="index"
          :style="{
            backgroundColor:
              (index + 1) % 2 == 0
                ? 'rgba(255, 255, 255,0.1)'
                : 'rgba(255, 255, 255,0)',
          }"
        >
          <el-col style="width: 16vw; padding-left: 0.4vw">{{
            item.name
          }}</el-col>
          <el-col>{{ item.operation }}</el-col>
          <el-col style="width: 20vw">{{ item.time }}</el-col>
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
          name: "姚家裕隧道",
          time: "2021/12/1 17:30:25",
          operation: "1号风机控制正转",
        },
        {
          id: 1,
          name: "毓秀山隧道",
          time: "2021/12/1 18:34:27",
          operation: "左洞车指1-1-YK247+881车道指示器控制正红反红",
        },
        {
          id: 2,
          name: "中庄隧道",
          time: "2021/12/2 08:30:25",
          operation: "2号机摄像头转正",
        },
        {
          id: 3,
          name: "海望石隧道",
          time: "2021/12/2 09:34:27",
          operation: "右洞车指3-2-YK247+892车道指示器控制正绿反红",
        },
      ],
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
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  methods: {},
};
</script>

<style lang="less" scoped>
.controlRecord-container {
  width: 100%;
  height: 100%;
  padding: 0.02px;
  overflow: hidden;
  border: 1px solid #01a4db;
  .title {
    color: #00c3f9;
  }
  .scrollBox {
    width: 100%;
    height: 86%;
    /* border: solid 1px white; */
    padding-bottom: 20px;
    padding: 1vw;
    overflow-y: auto;
    .listContent {
      .el-row {
        height: 100%;
        width: 100%;
        .el-col {
          display: flex;
          align-items: center;
        }
      }
    }
  }
}
</style>
