<template>
  <div class="bigScreen">
    <div class="leftEcharts">
      <div class="echartsBox2">
        <div class="title">
          <div class="icon"></div>
          昨日运营日流量<span>Yesterday traffic</span>
        </div>
        <yesterdayTraffic />
      </div>
      <div class="echartsBox1">
        <div class="title">
          <div class="icon"></div>
          服务性能<span>Service performance</span>
        </div>
        <capitalInvestment />
      </div>
      <div class="echartsBox1">
        <div class="title">
          <div class="icon"></div>
          隧道流量预测<span>Tunnel flow prediction</span>
        </div>
        <completionRate />
      </div>
    </div>
    <!-- <gode-map /> -->
    <div class="rightEcharts">
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          路况信息<span>Traffic information</span>
        </div>
        <maintenanceOperation />
      </div>
      <div class="echartsBox3">
        <div class="title">
          <div class="icon"></div>
          公众行驶速度异常路段排行<span>Ranking road</span>
        </div>
        <dailyMaintenance />
      </div>
      <div class="echartsBox4">
        <div class="title">
          <div class="icon"></div>
          气象预警信息<span>Meteorological early warning</span>
        </div>
        <defectRepair />
      </div>
    </div>
    <div class="topBox">
      <div class="topMiniBox">
         <div>
          <span class="title">今日通行量</span>
        </div>
         <div><span class="num">{{obj.todayFlowCount}}</span><span class="title">辆</span><span class="num2">{{obj.todayFolwPercent}}%</span></div>
      </div>
      <div class="topMiniBox">
         <div>
          <span class="title">今日累计服务用户</span><span class="num">8</span><span class="num3">3.0%</span><i class="el-icon-top"></i>
         </div>
         <div><span class="title">情报板总数</span><span class="num">{{obj.boardCount}}</span></div>
      </div>
      <div class="topMiniBox">
         <div>
          <span class="title">近7日情报板发布</span><span class="num">{{obj.sevenDaysBoardPushCount}}</span>
         </div>
         <div><span class="title">情报板发布总数</span><span class="num">{{ obj.boardPushCount }}</span></div>
      </div>
    </div>
    
  </div>
</template>
<script>
// import GodeMap from "./components/GodeMap";
import yesterdayTraffic from "./components/yesterdayTraffic";
import capitalInvestment from "./components/capitalInvestment";
import completionRate from "./components/completionRate";
import maintenanceOperation from "./components/maintenanceOperation";
import dailyMaintenance from "./components/dailyMaintenance";
import defectRepair from "./components/defectRepair";
import {allIndex} from "@/api/bigScreen/model6"

export default {
  name: "BigScreen",
  components: {
    // GodeMap,
    yesterdayTraffic,
    capitalInvestment,
    completionRate,
    maintenanceOperation,
    dailyMaintenance,
    defectRepair,
  },
  data() {
    return {
      obj:{}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList(){
      allIndex().then((res)=>{
        this.obj = res.data
      })
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
      height: 33%;
    }
    .echartsBox2 {
      height: 34%;
    }
    .echartsBox3{
      height: 25%;
    }
    .echartsBox4{
      height: 50%;
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
        display:flex;
        justify-content: center;
        .title{
            font-size: 14px;
            font-weight: bold;
            padding-right:10px;
            color:#9ba0bc;
        }
        .num{
            color:#FBD750;
            font-size:22px;
            font-weight:bold;
            padding-right:10px;
        }
        .num2{
            color:#33C18C;
            font-size:20px;
            font-weight:bold;
            padding-right:10px;
        }
        .num3{
            color:red;
            font-size:18px;
            font-weight:bold;
            padding-right:10px;
        }
        [class^=el-icon-], [class*=" el-icon-"]{
          line-height: 40px;
          color: red;;
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
}
</style>
