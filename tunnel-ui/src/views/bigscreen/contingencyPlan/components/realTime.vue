<template>
  <div>
    <div class="contentTitle">
      实时报警信息
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
                  {{ item.content }}
                </div>
                <div class="msgBox">
                  <span class="realTitle">{{ item.title }} </span>
                  <span>{{ item.time }}</span>
                </div>
              </div>
              <div class="realRight">
                <img :src="item.src" />
              </div>
            </div>
          </li>
        </ul>
      </vue-seamless-scroll>
    </div>
  </div>
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      policeList: [
        {
          title: "报警信息",
          time: "10：13：12",
          content:
            "凤凰山隧道，博山方向，150+016疑似发生火警，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic1.jpg"),
        },
        {
          title: "预警信息",
          time: "10：30：39",
          content:
            "凤凰山隧道，济南方向，152+826疑似发生超速，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic2.jpg"),
        },
        {
          title: "报警信息",
          time: "11：07：59",
          content:
            "凤凰山隧道，济南方向，152+996疑似发生异常变道，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic1.jpg"),
        },
        {
          title: "报警信息",
          time: "15：03：02",
          content:
            "凤凰山隧道，济南方向，159+806疑似发生超速，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic1.jpg"),
        },
        {
          title: "报警信息",
          time: "19：57：52",
          content:
            "凤凰山隧道，济南方向，153+538疑似发生超速，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic2.jpg"),
        },
        {
          title: "报警信息",
          time: "22：45：22",
          content:
            "凤凰山隧道，济南方向，154+0112疑似发生超速，请计时处理以免造成更大损失。",
          src: require("../../../../assets/Example/pic1.jpg"),
        },
      ],
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
  methods: {},
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
    padding: 15px 15px 0 15px;
    overflow: auto;
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
