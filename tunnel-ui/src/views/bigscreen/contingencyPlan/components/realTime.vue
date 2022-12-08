<template>
  <div>
    <div class="contentTitle">
      报警信息
      <i>Real time alarm</i>
    </div>
    <div class="alarmsStatisticsBox">
      <vue-seamless-scroll
        :class-option="defaultOption"
        class="realTimeList"
        :data="policeList"
      >
        <ul>
          <li
            v-for="(item, index) in policeList"
            :key="index"
            :style="{ color: (index + 1) % 2 == 0 ? '#ECAF4C' : '#09BDEF' }"
          >
            <div class="realTimeContent">
              <div class="realLeft">
                <div style="color: #fff; font-size: 16px">
                  {{ item.eventTitle }}
                </div>
                <div class="msgBox">
                  <span class="realTitle">报警信息 </span>
                  <span style="color: #09bdef">{{ item.startTime }}</span>
                </div>
              </div>
              <div class="realRight">
                <img v-if="item.videoUrl != ''" :src="item.videoUrl" />
                <h6 v-if="item.videoUrl == ''">暂无视频</h6>
              </div>
            </div>
          </li>
        </ul>
      </vue-seamless-scroll>
    </div>
  </div>
</template>

<script>
import { getAlarmInformation } from "@/api/business/new";
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      policeList: [],
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 6, // 开始无缝滚动的数据量 this.dataList.length
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
    this.getList();
  },
  methods: {
    getList() {
      // let tunnelId = { tunnelId: "WLJD-JiNan-YanJiuYuan-FHS" };
      getAlarmInformation().then((res) => {
        this.policeList = res.data;
      });
    },
  },
};
</script>

<style lang="less" scoped>
// 实时报警vue-seamless-scroll大框
.realTimeContent {
  display: flex;
  width: 100%;
  height: 7vw;
  margin-bottom: 0.5vw;
  background-color: #015384;
  // border: solid 1px white;
}
// 实时报警轮播内容
.realTimeList {
  width: 100%;
  height: 100%;
  // border: solid 1px red;
  overflow: hidden;
  font-size: 0.8vw;
  padding: 1vw 0.5vw;
}
.realLeft {
  width: 60%;
  height: 100%;
  //   border: solid 1px rgba(255, 255, 255, 0.4);
  margin-right: 0.2vw;
  overflow: hidden;
  > div {
    width: 100%;
    height: 60%;
    // border: solid 1px red;
    padding: 0px 15px 0 15px;
    overflow: auto;
    display: flex;
    align-items: center;
  }
}
.realRight {
  width: 40%;
  height: 100%;
  //   border: solid 1px rgba(255, 255, 255, 0.4);
  img {
    width: auto;
    height: 100%;
    margin: 0 auto;
  }
  h6 {
    color: white;
    margin: 0px;
    line-break: unset;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
.realTitle {
  padding-left: 0.4vw;
  padding-bottom: 0.1vw;
  padding: 5px 10px;
  box-sizing: border-box;
  background-color: #ec6600;
  color: #fff;
  font-size: 14px;
  height: 28px;
}
.realTitle:hover {
  color: white;
}
::v-deep ul {
  list-style-type: none !important;
  padding-left: 0vw !important;
}
.msgBox {
  display: flex;
  justify-content: space-between;
  align-content: center;
}
</style>
