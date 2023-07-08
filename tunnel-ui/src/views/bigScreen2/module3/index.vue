<template>
    <div class="bigScreen">
      <div class="leftEcharts">
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            总数据统计<span>Total data statistics</span>
          </div>
          <totalData />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            碳中和分析<span>Carbon neutralization analysis</span>
          </div>
          <carbonNeutralization />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            分项用能<span>Itemized energy consumption</span>
          </div>
          <itemizedEnergy />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            能耗与碳排放<span>Energy consumption</span>
          </div>
          <energyConsumption />
        </div>
      </div>
      <!-- <gode-map /> -->
      <div class="rightEcharts">
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            本月数据统计<span>Data statistics of this month</span>
          </div>
          <monthData />
        </div>
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            多能源协同<span>Multi-energy synergy</span>
          </div>
          <multiEnergySynergy />
        </div>
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            直流交流用能占比<span> DC Proportion</span>
          </div>
          <DCProportion />
        </div>
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            储能检测<span>Energy storage detection</span>
          </div>
          <brandFault />
        </div>
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            照明运行模式<span>Lighting operation mode</span>
          </div>
          <lightingMode />
        </div>
      </div>
    </div>
  </template>
  <script>
//   import GodeMap from "./components/GodeMap";
  import totalData from "./components/totalData";
  import carbonNeutralization from "./components/carbonNeutralization";
  import itemizedEnergy from "./components/itemizedEnergy";
  import monthData from "./components/monthData";
  import multiEnergySynergy from "./components/multiEnergySynergy";
  import brandFault from "./components/brandFault";
  import energyConsumption from "./components/energyConsumption";
  import DCProportion from "./components/DCProportion";
  import lightingMode from "./components/lightingMode";
  
  
  export default {
    name: "BigScreen",
    components: {
    //   GodeMap,
      totalData,
      carbonNeutralization,
      itemizedEnergy,
      monthData,
      multiEnergySynergy,
      brandFault,
      energyConsumption,
      DCProportion,
      lightingMode
    },
    created() {
    //   console.log(GodeMap);
    },
    methods: {
      init(){
        console.log(111111111111111)
      }
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
        height: 25%;
      }
      .echartsBox2 {
        height: 20%;
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
        >div{
          width:100%;
          height:50%;
          text-align: center;
          line-height: 40px;
          .title{
              font-size: 14px;
              font-weight: bold;
              padding-right:10px;
              color:#9ba0bc;
          }
          .num1{
              color:#FBD750;
              font-size:22px;
              font-weight:bold;
              padding-right:10px;
          }
          .num2{
              color:#FBD750;
              font-size:14px;
              font-weight:bold;
              padding-right:10px;
          }
          .num3{
              color:#D2434D;
              font-size:22px;
              font-weight:bold;
              padding-right:10px;
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
          #BB8228,
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
  