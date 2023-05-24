<template>
  <div class="bigScreen">
    <div class="leftEcharts">
      <div class="echartsBox1">
        <div class="title">
          <div class="icon"></div>
          隧道重要事件情况<span>Tunnel important events</span>
        </div>
        <importantEvents />
      </div>
      <div class="echartsBox2">
        <div class="title">
          <div class="icon"></div>
          分隧道事件统计<span>Tunnel event statistics</span>
        </div>
        <eventStatistics />
      </div>
      <div class="echartsBox2">
        <div class="title">
          <div class="icon"></div>
          感知事件类型统计<span>Perceived event type</span>
        </div>
        <perceivedEvent />
      </div>
    </div>
    <!-- <gode-map /> -->
    <div class="rightEcharts">
      <div class="echartsBox4">
        <div class="title">
          <div class="icon"></div>
          隧道车流量情况<span>Tunnel traffic flow</span>
        </div>
        <trafficFlow />
      </div>
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          各隧道实时拥挤度等级<span>Real-time congestion</span>
        </div>
        <realTimeCongestion />
      </div>
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          各隧道实时车流量<span>Real-time traffic flow</span>
        </div>
        <realTimeTrafficFlow />
      </div>
    </div>
    <div class="topBox">
      <div class="topMiniBox">
        <div>
          <span class="title">感知事件</span>
          <span class="num1">{{obj.twentyFourHoursEventCount}}</span>
          <span class="num2">/</span>
          <span class="num2">24小时</span>
          <span class="num1">{{obj.eventCount}}</span>
        </div>
        <div>
          <span class="title">视频AI</span><span class="num1">6</span
          ><span class="title">雷达监测</span><span class="num1">0</span>
        </div>
      </div>
      <div class="topMiniBox">
        <div>
          <span class="title">在途车流量</span
          ><span class="num1">{{ obj.travelVehicleFlowCount }}</span>
        </div>
        <div>
          <span class="title">客车</span><span class="num1">2,236</span
          ><span class="num3">71.0%</span> <span class="title">货车</span
          ><span class="num1">914</span><span class="num3">29.0%</span>
        </div>
      </div>
      <div class="topMiniBox">
        <div>
          <span class="title">在途重点车辆</span
          ><span class="num1">{{ obj.importantVehicleCount }}</span>
        </div>
        <div>
          <span class="title">客车</span><span class="num1">171</span
          ><span class="title">货车</span><span class="num1">718</span>
        </div>
      </div>
    </div>
  </div>
</template>
  <script>
import importantEvents from "./components/importantEvents";
import eventStatistics from "./components/eventStatistics";
import perceivedEvent from "./components/perceivedEvent";
import trafficFlow from "./components/trafficFlow";
import realTimeCongestion from "./components/realTimeCongestion";
import realTimeTrafficFlow from "./components/realTimeTrafficFlow";
import { allIndex } from "@/api/bigScreen/model1";
export default {
  name: "BigScreen",
  components: {
    importantEvents,
    eventStatistics,
    perceivedEvent,
    trafficFlow,
    realTimeCongestion,
    realTimeTrafficFlow,
  },
  data() {
    return {
      obj: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      allIndex().then((res) => {
        this.obj = res.data;
      });
    },
  },
};
</script>
  
  <style scoped lang="scss">
.bigScreen {
  height: calc(100% - 87px);
  position: absolute;
  top: 87px;
  width: 100%;
  font-size: 0.7rem;
  .leftEcharts,
  .rightEcharts {
    width: 18.5vw;
    height: 100%;
    position: absolute;
    top: 0;
    left: 10px;
    z-index: 4;
    background: linear-gradient(to right, rgba(0, 0, 0), rgb(0 0 0 / 25%));
    color: #fff;
    .title {
      background: url(../../../assets/Example/bigScreen/title.png);
      background-repeat: no-repeat;
      background-position: center;
      background-size: 100%;
      height: 30px;
      padding-left: 10px;
      display: flex;
      align-items: center;
      .icon {
        background: url(../../../assets/Example/bigScreen/Icon.png);
        background-repeat: no-repeat;
        background-position: center;
        background-size: 100%;
        width: 18px;
        height: 18px;
        margin-right: 8px;
      }
      span {
        text-transform: uppercase;
        padding-left: 8px;
        color: rgba(255, 255, 255, 0.35);
        font-size: 0.6rem;
      }
    }
    .echartsBox1 {
      height: 40%;
    }
    .echartsBox2 {
      height: 30%;
    }
    .echartsBox3 {
      height: 32%;
    }
    .echartsBox4 {
      height: 36%;
    }
  }
  .rightEcharts {
    right: 10px !important;
    left: auto;
    background: linear-gradient(to left, rgba(0, 0, 0), rgb(0 0 0 / 25%));
  }
  .topBox {
    width: calc(100% - 40vw);
    height: 80px;
    position: absolute;
    top: 0;
    left: 20vw;
    z-index: 4;
    display: flex;
    justify-content: space-between;
    color: #fff;
    .topMiniBox {
      width: 33%;
      height: 100%;
      --borderWidth: 1px;
      background: #000001;
      position: relative;
      border-radius: var(--borderWidth);
      > div {
        width: 100%;
        height: 50%;
        text-align: center;
        line-height: 40px;
        .title {
          font-size: 14px;
          font-weight: bold;
          padding-right: 10px;
          color: #9ba0bc;
        }
        .num1 {
          color: #fbd750;
          font-size: 22px;
          font-weight: bold;
          padding-right: 10px;
        }
        .num2 {
          color: #fbd750;
          font-size: 14px;
          font-weight: bold;
          padding-right: 10px;
        }
        .num3 {
          color: #d2434d;
          font-size: 16px;
          font-weight: bold;
          padding-right: 10px;
        }
      }
    }
    .topMiniBox:after {
      content: "";
      position: absolute;
      top: calc(-1 * var(--borderWidth));
      left: calc(-1 * var(--borderWidth));
      height: calc(100% + var(--borderWidth) * 2);
      width: calc(100% + var(--borderWidth) * 2);
      background: linear-gradient(
        60deg,
        #a166ab,
        #5073b8,
        #1098ad,
        #30c3fe,
        #6fba82
      );
      border-radius: calc(2 * var(--borderWidth));
      z-index: -1;
      animation: animatedgradient 3s ease alternate infinite;
      background-size: 300% 300%;
    }

    @keyframes animatedgradient {
      0% {
        background-position: 0% 50%;
      }
      50% {
        background-position: 100% 50%;
      }
      100% {
        background-position: 0% 50%;
      }
    }
  }
}
</style>
  