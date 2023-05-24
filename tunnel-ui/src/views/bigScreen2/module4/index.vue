<template>
    <div class="bigScreen">
      <div class="leftEcharts">
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            养护决策<span>Maintenance decisions</span>
          </div>
          <maintenanceDecisions />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            性能评价<span>performance evaluation</span>
          </div>
          <performanceEvaluation />
        </div>
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            养护计划完成率<span>maintenance Completion rate</span>
          </div>
          <completionRate />
        </div>
      </div>
      <!-- <gode-map /> -->
      <div class="rightEcharts">
        <div class="echartsBox2">
          <div class="title">
            <div class="icon"></div>
            维养作业<span>Maintenance operation</span>
          </div>
          <maintenanceOperation />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            当日养护<span>Daily maintenance</span>
          </div>
          <dailyMaintenance />
        </div>
        <div class="echartsBox1">
          <div class="title">
            <div class="icon"></div>
            缺陷维修<span>Defect Repair</span>
          </div>
          <defectRepair />
        </div>
      </div>
      <div class="topBox">
        <div class="topMiniBox">
          <div>
              <span class="title">今日巡查</span><span class="num1">6</span>
          </div>
          <div>
              <span class="title">巡检公里数</span><span class="num1">0</span><span class="title">KM</span><span class="title">今日病害</span><span class="num1">0</span>
          </div>
        </div>
        <div class="topMiniBox">
          <div>
              <span class="title">养护总里程</span><span class="num1">189,493</span><span class="title">KM</span>
          </div>
          <div>
              <span class="title">起始</span><span class="num1">471.166</span>
              <span class="title">结束</span><span class="num1">659.659</span>
          </div>
        </div>
      </div>
      <div class="topBox2 topBox">
        <div v-for="(item,index) in styleList" :key="index" class="rowMiniBox">
          <img :src="item.src" />
          <div>
            <div>{{item.title}}</div>
            <div><span class="num">{{item.num}}</span>{{ item.unit }}</div>
          </div>
        </div>
      </div>
    </div>
  </template>
  <script>
//   import GodeMap from "./components/GodeMap";
  import maintenanceDecisions from "./components/maintenanceDecisions";
  import performanceEvaluation from "./components/performanceEvaluation";
  import completionRate from "./components/completionRate";
  import maintenanceOperation from "./components/maintenanceOperation";
  import dailyMaintenance from "./components/dailyMaintenance";
  import defectRepair from "./components/defectRepair";
  
  export default {
    name: "BigScreen",
    components: {
    //   GodeMap,
      maintenanceDecisions,
      performanceEvaluation,
      completionRate,
      maintenanceOperation,
      dailyMaintenance,
      defectRepair,
    },
    data() {
      return {
        styleList:[
          {
            src:require("@/assets/Example/bigScreen/1.png"),
            title:'维修量',
            num:61,
            unit:'条',
          },
          {
            src:require("@/assets/Example/bigScreen/2.png"),
            title:'平均维修时长',
            num:3,
            unit:'天',
          },
          {
            src:require("@/assets/Example/bigScreen/3.png"),
            title:'养护计划完成率',
            num:97,
            unit:'%',
          },
          {
            src:require("@/assets/Example/bigScreen/4.png"),
            title:'封道次数',
            num:54,
            unit:'次',
          },
          {
            src:require("@/assets/Example/bigScreen/5.png"),
            title:'保洁里程',
            num:4214,
            unit:'KM',
          },
          {
            src:require("@/assets/Example/bigScreen/6.png"),
            title:'巡视里程',
            num:8288,
            unit:'KM',
          },
          {
            src:require("@/assets/Example/bigScreen/7.png"),
            title:'人工',
            num:1186,
            unit:'工日',
          },
          {
            src:require("@/assets/Example/bigScreen/8.png"),
            title:'台班',
            num:536,
            unit:'辆天',
          },
        ]
      }
    },
    created() {
    //   console.log(GodeMap);
    },
    methods: {},
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
        height: 33%;
      }
      .echartsBox2 {
        height: 34%;
      }
    }
    .rightEcharts {
      right: 10px !important;
      left: auto;
      background: linear-gradient(to left, rgba(0, 0, 0), rgb(0 0 0 / 25%));
    }
    .topBox2{
      width: calc(100% - 40vw + 1px) !important;
      top: 0px !important;
      border: solid 1px #455B77;
      background: #000001;
      border-radius: 2px;
      .rowMiniBox{
        width: 13%;
        height: 100%;
        display:flex;
        align-items:center;
        >img{
          width: 50px;
          height: 50px;
        }
        >div{
          font-size: 12px;
          color: #9ba0bc;
          .num{
            color: #30C3FE;
            font-size:22px;
            font-weight: bold;
            padding-right: 4px;
          }
        }
      }
    }
   
    .topBox {
      width: calc(100% - 40vw);
      height: 80px !important;
      position: absolute;
      top: 82px;
      left: 20vw;
      z-index: 4;
      display: flex;
      justify-content: space-between;
    }
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
              font-size:16px;
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
          #30C3FE,
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
  </style>
  