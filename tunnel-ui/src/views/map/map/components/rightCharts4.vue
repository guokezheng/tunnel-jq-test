<template>
  <div class="mapCharts">
    <div class="title">
      <img :src="iconSrc" />
      <div>实时监控</div>
      <div>real time monitoring</div>
    </div>
    <img :src="src" class="titleIcon" />
    <div class="blueLine"></div>
    <div >
      <el-carousel :interval="5000" arrow="always" >
        <el-carousel-item v-for="(item,index) in videoList" :key="index">
          <video
            :src="item.src"
            controls
            muted
            autoplay
            loop
            style="width: 100%; object-fit: cover;height:100%"
          ></video>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>
  <script>
export default {
  data() {
    return {
      src: require("@/assets/cloudControl/dialogHeader.png"),
      iconSrc: require("@/assets/icons/energyIcon.png"),
      videoList: [
        {
          src: require("@/assets/Example/v1.mp4"),
        },
        {
          src: require("@/assets/Example/v2.mp4"),
        },
        {
          src: require("@/assets/Example/v3.mp4"),
        },
      ],
    };
  },
  methods: {},
};
</script>
  <style scoped lang="scss">
.mapCharts {
  height: 25%;
  width: 100%;
  transform: translateX(-18px);
  position: relative;
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
    right: 18px;
    height: 30px;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
 ::v-deep .el-carousel {
    height: 184px;
    .el-carousel__container {
      height: 184px !important;
      .el-carousel__item {
        height: 184px;
      }
    }
  }
  ::v-deep .el-carousel__indicators{
    display: none;
  }
}
</style>
  