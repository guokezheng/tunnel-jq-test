<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>实时数据</div>
      <div>real-time data</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <div class="contentBox">
      <div class="row" style="height: 45%">
        <div class="content">
          <div>
            <div>1472</div>
            <div class="sanjiao"></div>
            <div class="percentage">12%</div>
          </div>
          <div>累计车流量</div>
        </div>
        <div class="content">
          <div>695</div>
          <div>当前在途车辆</div>
        </div>
        <div class="content">
          <div>247</div>
          <div>在途重点车辆</div>
        </div>
      </div>
      <div class="row" style="height: 45%; margin-top: 2%">
        <div class="content">
          <div :style="perceptionNum>0?'color:red':'color:#ffd17b'">{{perceptionNum}}</div>
          <div>感知事件</div>
        </div>
        <div class="content">
          <div>247</div>
          <div>气象预警</div>
        </div>
        <div class="content">
          <div>
            <div :style="accidentNum>0?'color:red':'color:#ffd17b'">{{accidentNum}}</div>
            <div style="color:white !important;font-size:10px;margin: 0 2px;">/</div>
            <div class="accident">12</div>
          </div>
          <div>交通事故</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      src: require("@/assets/cloudControl/dialogHeader.png"),
      iconSrc: require("@/assets/icons/energyIcon.png"),
      accidentNum:3,
      perceptionNum:3,
    };
  },
  methods: {},
};
</script>
<style scoped lang="scss">
.mapCharts {
  height: 25%;
  width: 100%;
  margin-left: 18px;
  .title {
    color: white;
    width: 100%;
    height: 30px;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
    align-items: center;

    > div:nth-of-type(1) {
      margin-left: 10px;
      font-size: 14px;
    }
    > div:nth-of-type(2) {
      text-transform: uppercase;
      margin-left: 10px;
      opacity: 0.35;
      font-size: 10px;
    }
    > img {
      height: 22px;
      margin-left: 8px;
    }
  }
  .titleIcon {
    position: absolute;
    top: 0;
    right: 0;
    height: 30px;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
    // margin-top:-10px;
  }
  .contentBox {
    width: 100%;
    height: calc(100% - 30px);
    padding: 30px;
    .row {
      width: 100%;
      //   height: 50%;
      display: flex;
      justify-content: space-between;
      .content {
        width: 30%;
        height: 100%;
        background: radial-gradient(
          circle farthest-side at bottom,
          #013a63 30%,
          transparent
        );
        > div:nth-of-type(1) {
          width: 100%;
          height: 60%;
          color: #ffd17b;
          font-size: 22px;
          display: flex;
          align-items: center;
          justify-content: center;
          > .sanjiao {
            width: 0;
            height: 0;
            border-top-width: 2px;
            border-right-width: 4px;
            border-bottom-width: 6px;
            border-left-width: 4px;
            border-color: transparent;
            border-bottom-color: #E33C64 !important;
            border-style: solid;
            margin-left: 2px;
          }
          >.percentage{
            color: #E33C64;
            font-size: 8px;
          }
          >.accident{
            color: white;
            font-size: 16px;
          }
        }
        > div:nth-of-type(2) {
          width: 100%;
          height: 40%;
          color: white;
          text-align: center;
          font-size: 14px;
          border-bottom: solid 2px white;
          border-image: linear-gradient(
              to right,
              rgba($color: #010d16, $alpha: 0.1) 0%,
              #27a8ff 50%,
              rgba($color: #010d16, $alpha: 0.1) 100%
            )
            1 10;
        }
      }
    }
  }
}
</style>
