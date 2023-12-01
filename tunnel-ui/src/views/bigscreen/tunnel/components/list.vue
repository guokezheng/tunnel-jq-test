<template>
  <div style="height: 100%;">
    <el-tabs
      v-model="activeName"
      style="border-bottom: transparent"
      class="deleteBorder"
    >
      <el-tab-pane label="事件事故" name="first" lazy>
        <div class="tabBox">
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="seamless-warp"
            :data="listData"
          >
            <!-- <ul> -->
              <div v-for="(item, index) in listData" :key="index">
                <div class="tabContent">
                  <div class="tabLeft">
                    <div class="tabLeftPic"><img :src="item.src" /></div>
                    <div class="tabLeftNum"><img :src="item.num" /></div>
                  </div>
                  <div class="tabRight">
                    <div class="tabRightLine">
                      <!-- <div><span>位置：</span><span>{{item.position}}</span></div> -->
                      <div>
                        <div>时期:</div><div>{{ item.date }}</div>
                      </div>
                      <div>
                        <div>时间:</div><div>{{ item.time }}</div>
                      </div>
                      <div>
                        <div>类型:</div><div>{{ item.type }}</div>
                      </div>
                      <div
                        :style="
                          item.state == '未处置'
                            ? 'color: #EC963D'
                            : 'color: #5CADE4'
                        "
                      >
                        <div>状态:</div><div>{{ item.state }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            <!-- </ul> -->
          </vue-seamless-scroll>
        </div>
      </el-tab-pane>
      <el-tab-pane label="故障信息" name="second" lazy>
        <div class="tabBox">
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="seamless-warp"
            :data="malfunctionList"
          >
            <!-- <ul> -->
              <div v-for="(item, index) in malfunctionList" :key="index">
                <div class="tabContent">
                  <div class="tabLeft">
                    <div class="tabLeftPic"><img :src="item.src" /></div>
                    <div class="tabLeftNum">{{item.num}}</div>
                  </div>
                  <div class="tabRight">
                    <div class="tabRightLine">
                      <div>
                        <div>时期:</div><div>{{ item.date }}</div>
                      </div>
                      <div>
                        <div>时间:</div><div>{{ item.time }}</div>
                      </div>
                      <div>
                        <div>类型:</div><div>{{ item.type }}</div>
                      </div>
                      <div
                        :style="
                          item.state == '未处置'
                            ? 'color: #EC963D'
                            : 'color: #5CADE4'
                        "
                      >
                        <div>状态:</div><div>{{ item.state }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            <!-- </ul> -->
          </vue-seamless-scroll>
        </div>
      </el-tab-pane>
      <el-tab-pane label="预警信息" name="third" lazy>
        <div class="tabBox">
          <vue-seamless-scroll
            :class-option="defaultOption"
            class="seamless-warp"
            :data="warningList"
          >
            <!-- <ul> -->
              <div v-for="(item, index) in warningList" :key="index">
                <div class="tabContent">
                  <div class="tabLeft">
                    <div class="tabLeftPic"><img :src="item.src" /></div>
                    <div class="tabLeftNum">{{item.num}}</div>
                  </div>
                  <div class="tabRight">
                    <div class="tabRightLine">
                      <!-- <div><span>位置：</span><span>{{item.position}}</span></div> -->
                      <div>
                        <div>时期:</div><div>{{ item.date }}</div>
                      </div>
                      <div>
                        <div>时间:</div><div>{{ item.time }}</div>
                      </div>
                      <div>
                        <div>类型:</div><div>{{ item.type }}</div>
                      </div>
                      <div
                        :style="
                          item.state == '未处置'
                            ? 'color: #EC963D'
                            : 'color: #5CADE4'
                        "
                      >
                        <div>状态:</div><div>{{ item.state }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            <!-- </ul> -->
          </vue-seamless-scroll>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      activeName: "first",
      listData: [
        {
          date: "2021/4/16",
          time: "08:30:25",
          type: "违章变道",
          state: "未处置",
          src: require("../../../../assets/Example/pic1.jpg"),
          num: require("../../../../assets/Example/plateNum1.jpg"),
        },
        {
          date: "2021/4/16",
          time: "08:30:25",
          type: "违章变道",
          state: "处置中",
          src: require("../../../../assets/Example/pic2.jpg"),
          num: require("../../../../assets/Example/plateNum2.jpg"),
        },
      ],
      malfunctionList: [
        {
          date: "2021/5/10",
          time: "12:30:25",
          type: "风机故障",
          state: "未处置",
          src: require("@/assets/Example/draught_fan.jpeg"),
          num: '姚家峪隧道',
        },
        {
          date: "2021/6/5",
          time: "14:30:25",
          type: "COVI仪器故障",
          state: "处置中",
          src: require("@/assets/Example/COVI.jpeg"),
          num: '海望石隧道',
        },
      ],
      warningList: [
        {
          date: "2021/5/5",
          time: "15:30:25",
          type: "CO浓度超标",
          state: "未处置",
          src: require("@/assets/Example/COoverproof.png"),
          num: '毓秀山隧道',
        },
        {
          date: "2021/6/12",
          time: "12:30:25",
          type: "用电能耗超标",
          state: "处置中",
          src: require("@/assets/Example/electric_energy_loss.png"),
          num: '洪河隧道',
        },
      ],
    };
  },
  components: {
    vueSeamlessScroll,
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
    defaultOptionTwo() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  mounted() {},
  metheds: {
    handleClick(tab, event) {

    },
  },
};
</script>

<style scoped lang="less">
.deleteBorder {
    height: 100%;
}
/*去掉el-tab-pane底部灰色线条*/
.deleteBorder .el-tabs__nav-wrap::after {
  height: 0;
}
/*去掉el-tab-pane切换时的蓝色下划线*/
::v-deep .deleteBorder .el-tabs__active-bar {
  background: transparent;
}

::v-deep .el-tabs__content{
    height: 90% !important;
}
::v-deep .el-tabs__header {
  height: 10%;
  margin: 0px !important;
  .el-tabs__nav-wrap{
      height: 100%;
      .el-tabs__nav-scroll{
          height: 100%;
          .el-tabs__nav{
              height: 100%;
              width: 100%;
              padding: 10px 15px 0px 15px;
              .el-tabs__item{
                  transform: translateY(-3vw);
                  color: #025d9d;
                  font-size: 0.8vw;
              }
          }
      }
  }
}
::v-deep .el-tabs__nav-wrap::after {
      position: static !important;
  }
::v-deep .is-active{
    color: white !important;
}
// .el-tab-pane{
//     height: 100%;
//     /* font-size: 1vw; */
// }
.tabBox {
  width: 100%;
  height: 100%;
  /* border: solid 1px white; */
  padding-bottom: 20px;
  overflow-y: auto;
}
.tabContent {
  width: 96%;
  height: 6.5vw;
  background-color: #143673;
  border-radius: 10px;
  margin: 0px auto 8px;
  display: flex;
  justify-content: space-between;
  .tabLeft {
    width: 48%;
    height: 100%;
    /* border: solid 1px white; */
    .tabLeftPic {
      width: 90%;
      height: 55%;
      border: solid 1px #4079d5;
      margin: 8% 10% 0% 10%;
    }
    .tabLeftNum {
      width: 54%;
      height: 18%;
      /* border: solid 1px white; */
      margin: 6% 30%;
      font-size: 0.7vw;
      text-align: center;
    }
    img {
      width: 100%;
      height: 100%;
      display: table-cell;
      vertical-align: middle;
      text-align: center;
    }
  }
  .tabRight {
    width: 50%;
    height: 100%;
    /* border: solid 1px white; */
    .tabRightLine {
      width: 95%;
      height: 80%;
      border: solid 1px #2d5ba5;
      margin-top: 8%;
      > div {
        width: 100%;
        height: 1.3vw;
        color: white;
        line-height: 1.3vw;
        border-bottom: solid 1px #2d5ba5;
        display: flex;
        overflow: hidden;
        white-space: nowrap;
        >div{
            font-size: 1vw;
            transform: scale(0.7);
        }
        >div:first-of-type{
            width: 23%;
        }
        >div:last-of-type{
            width: 77%;
        }
      }
      > div:last-of-type {
        border-bottom: transparent;
      }
    }
  }
}

.seamless-warp {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
