<template>
  <div class="bigScreen">
    <div class="leftEcharts">
      <div class="echartsBox1">
        <div class="title">
          <div class="icon"></div>
          设备故障预警<span>Equipment fault warning</span>
        </div>
        <eqFaultWarn />
      </div>
      <div class="echartsBox1">
        <div class="title">
          <div class="icon"></div>
          设备实时状态统计<span>real-time statistics </span>
        </div>
        <realTimeStatistics />
      </div>
      <div class="echartsBox2">
        <div class="title">
          <div class="icon"></div>
          故障持续时间TOP10<span>top 10 fault duration</span>
        </div>
        <faultDuration />
      </div>
    </div>
    <div class="rightEcharts">
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          设备占比<span>Proportion of equipment</span>
        </div>
        <eqProportion />
      </div>
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          故障分类统计<span>Fault classification statistics</span>
        </div>
        <faultClassification />
      </div>
      <div class="echartsBox4">
        <div class="title">
          <div class="icon"></div>
          品牌故障率TOP10<span>Top 10 brand failure rate</span>
        </div>
        <brandFault />
      </div>
    </div>
    <div class="topBox">
      <div class="topMiniBox">
        <div>
          <span class="title">设备总数</span
          ><span class="num1">{{ obj.count }}</span
          ><span class="num2">套</span>
        </div>
        <div>
          <span class="title">预警</span
          ><span class="num3">{{ obj.warnCount }}</span
          ><span class="title">在修</span><span class="num1">{{obj.repairCount}}</span>
        </div>
      </div>
      <div class="topMiniBox">
        <div>
          <span class="title">机电设备总览</span><span class="num1">8523</span
          ><span class="num2">套</span>
        </div>
        <div>
          <span class="title">预警</span><span class="num3">21</span
          ><span class="title">在修</span><span class="num1">0</span>
        </div>
      </div>
    </div>
  </div>
</template>
  <script>
import eqFaultWarn from "./components/eqFaultWarn";
import realTimeStatistics from "./components/realTimeStatistics";
import faultDuration from "./components/faultDuration";
import eqProportion from "./components/eqProportion";
import faultClassification from "./components/faultClassification";
import brandFault from "./components/brandFault";
import { allIndex } from "@/api/bigScreen/model2";
export default {
  name: "BigScreen",
  components: {
    eqFaultWarn,
    realTimeStatistics,
    faultDuration,
    eqProportion,
    faultClassification,
    brandFault,
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
  font-size: 0.7vw;
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
        font-size: 0.6vw;
      }
    }
    .echartsBox1 {
      height: 33%;
    }
    .echartsBox2 {
      height: 34%;
    }
    .echartsBox3 {
      height: 30%;
    }
    .echartsBox4 {
      height: 40%;
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
    top: 0.12rem;
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
          font-size: 22px;
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
  