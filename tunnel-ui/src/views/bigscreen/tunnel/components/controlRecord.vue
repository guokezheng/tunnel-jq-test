<template>
  <div class="controlRecord-container">
    <div class="title">近12小时控制记录</div>
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
import { getRecordlist } from "@/api/business/new";
export default {
  data() {
    return {
      listData: [],
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
  created() {
    this.getRecordlistData();
  },
  methods: {
    getRecordlistData() {
      // let tunnelId = "WLJD-JiNan-YanJiuYuan-FHS";
      getRecordlist().then((res) => {
        this.listData = res.data;
      });
    },
  },
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
