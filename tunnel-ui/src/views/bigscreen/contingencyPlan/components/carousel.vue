<template>
  <div id="carousel">
    <div
      class="carousel"
      ref="carousel"
      
    >
    <div class="playminus playButton" @click="playminus" v-show="leftArrow"> < </div>
    <div class="playAdd playButton" @click="playAdd" v-show="rightArrow"> > </div>
      <transition-group tag="ul" class="slide clearfix" :name="transitionName">
        <li
          v-for="(item, index) in slideData"
          :key="index"
          v-show="index == beginValue"
          v-bind:style="{ height: height + 'px' }"
          style="display:flex;"
        >
          <!-- 播放视频 -->
            <video
              ref="videoPlay"
              muted=""
              :src="item.video"
              controls="controls"
              :autoplay="autoplay"
              loop="loop"
              style="width:100%;height:100%"
            ></video>
        </li>
      </transition-group>
    </div>
  </div>
</template>

<script>
import "videojs-contrib-hls";
export default {
  name: "carousel",
  data() {
    return {
      rightArrow:true,
      leftArrow:false,
      autoplay:true,
      setInterval: "",
      beginValue: 0,
      transitionName: "slide",
      mytime_02: true
    };
  },
  beforeDestroy() {
    // 组件销毁前,清除监听器
    clearInterval(this.setInterval);
  },
  watch:{
    "beginValue"(val){
      if(val == 0){
        this.leftArrow = false
      }else{
        this.leftArrow = true
      }
      if(val == this.slideData.length-1){
        this.rightArrow = false
      }else{
        this.rightArrow = true
      }
    }
  },
  methods: {
    timeupdate(e) {
      if (!this.mytime_02) {
        return;
      } //首次进入能执行
      this.mytime_02 = false;
      setTimeout(() => {
        console.log(parseInt(e.target.currentTime), "event");
        this.mytime_02 = true; //上次执行成功，下一次才可执行。
      }, 3000);
    },
    change(key) {
      if (key > this.slideData.length - 1) {
        key = 0;
      }
      if (key < 0) {
        key = this.slideData.length - 1;
      }

      this.beginValue = key;
      if(this.beginValue == 0){
        this.leftArrow = false
      }else{
        this.leftArrow = true
      }
      if(this.beginValue == this.slideData.length){
        this.rightArrow = false
      }
    },

    autoPlay() {
      this.transitionName = "slide";
      this.beginValue++;
      if (this.beginValue >= this.slideData.length) {
        this.beginValue = 0;
        return;
      }
    },
    play() {
      this.setInterval = setInterval(this.autoPlay, this.interval);
    },
    mouseOver() {
      //鼠标进入
      //console.log('over')
      clearInterval(this.setInterval);
    },
    mouseOut() {
      //鼠标离开
      //console.log('out')
      this.play();
    },
    playminus() {
      //上一页
      --this.beginValue;
      this.transitionName = "slideBack";
      this.change(this.beginValue);
    },
    playAdd() {
      //下一页
      ++this.beginValue;
      this.transitionName = "slide";
      this.change(this.beginValue);
    }
  },
  mounted() {
    var box = this.$refs.carousel; //监听对象
    box.addEventListener("mouseover", () => {
      this.mouseOver();
    });
    box.addEventListener("mouseout", () => {
      this.mouseOut();
    });
    this.beginValue = this.begin;
    this.play();
  },
  props: {
    height: {
      type: Number,
      default: 600
    },
    dot: {
      type: Boolean,
      default: true
    },
    arrow: {
      type: Boolean,
      default: true
    },
    interval: {
      type: Number,
      default: 5000
    },
    begin: {
      type: Number,
      default: 0
    },
    slideData: {
      type: Array,
      default: function() {
        return [];
      }
    }
  }
};
</script>

<style scoped>
.slide {
  position: relative;
  margin: 0;
  padding: 0;
  overflow: hidden;
  width: 100%;
  height: 100%;
}
.slide li {
  list-style: none;
  position: absolute;
  width: 100%;
  height: 100%;
}
.slide li img {
  width: 100%;
  height: 1.4rem;
  cursor: pointer;
}
.slide li .title {
  position: absolute;
  left: 0;
  bottom: 0;
  padding: 10px 20px;
  width: 100%;
  /* background: rgba(0, 0, 0, 0.35); */
  color: #fff;
  font-size: larger;
  text-align: center;
}
.videos {
  width: 1.8rem;
  height: 100%;
  /* margin-top: 0.2rem; */
}
.videos:nth-child(1) {
  margin-right: 0.2rem;
}

.slideDot {
  position: absolute;
  z-index: 999;
  bottom: 0.2rem;
  right: 1.85rem;
}
.slideDot span {
  display: inline-block;
  width: 0.07rem;
  height: 0.07rem;
  background: rgba(255, 255, 255, 0.65);
  margin-left: 0.05rem;
}
.slideDot span.active {
  background: rgba(255, 255, 255, 1);
}
.up,
.next {
  position: absolute;
  left: 0;
  top: 85%;
  margin-top: 0;
  cursor: pointer;
  width: 0.2rem;
  height: 0.2rem;
  background-repeat: no-repeat;
  background-position: 50% 50%;
}
.up {
  width: 0.2rem;
  left: 0.25rem;
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAABSklEQVRoQ9Xa220CMRCF4XMqIB0kdJASSCfpIDWlAzqAElIC6SBUMNFIywsCtB7PjX336v/k3YexTBQ+IvIC4AvAN8mTJYWWRR5rROQdwAGAIk4kt5b3lgCu4rX7l+TbUwBuxJ8B7Ej+tAd4xys47ROKiE8DRMWnACLjwwHR8aGAjPgwQFZ8CCAz3h2QHe8KqIh3A1TFuwAq46cB1fFTgA7xZkCXeBOgU/wwoFv8EKBj/GpA1/gRgJ4e7JaZdWqGtcy9j9asGilFRM9sXpcX/QH4sA7hVQA9wzkC2HRDrNoBjV7+g3aI1YCuiCFAR8QwoBvCBOiEMAO6IKYAHRDTgGqEC6AS4QaoQrgCKhDugGxECCATEQbIQoQCMhDhgGhECiASkQaIQqQCIhDpgDuI57orcQNxJqmXPoafkh24VIqIXvD4BLC3HtP8A6pfGkB3vbyXAAAAAElFTkSuQmCC");
}
.next {
  left: auto;
  right: 0.25rem;
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAABOElEQVRoQ9Xa0W0CQQyEYU9JqShQAXSSEgIdUAIlQAd04milezgdOiHZ4/GGN5C8/N/u060Olvy4+8nMngDuyaVC4whNLUPufjazn+XrEcAls15klgkY/y9HpACj2N3Hrn+vdk+KSAO6ERRAJ4IG6EJQAR0IOkCNKAEoEWUAFaIUoECUA6oREkAlQgaoQkgBFQg5gI1oATARbQAWohXAQLQDsogpABnENIAoYirADuILwGPvxmJGwK+ZHVbB/wfg7tv4K4A15u0gpjmBSPzQTAGIxk8ByMS3A7LxrQBGfBuAFd8CYMbLAex4KaAiXgaoipcAKuPLAdXxpQBFfBlAFV8CUMbTAep4KqAjngboiqcAOuPTgM27EmO9j8+we7cL0d9Tj5QbgDw+fQLLPc54Y+UF4BbdxczcH9Le8DFn39OvAAAAAElFTkSuQmCC");
}
.up:hover {
  /* background-color: rgba(0, 0, 0, 0.3); */
}
.next:hover {
  /* background-color: rgba(0, 0, 0, 0.3); */
}

/*进入过渡生效时的状态*/
.slide-enter-active {
  transform: translateX(0);
  transition: all 1s ease;
}

/*进入开始状态*/
.slide-enter {
  transform: translateX(-100%);
}

/*离开过渡生效时的状态*/
.slide-leave-active {
  transform: translateX(100%);
  transition: all 1s ease;
}

/*离开过渡的开始状态*/
.slide-leave {
  transform: translateX(0);
}

/*进入过渡生效时的状态*/
.slideBack-enter-active {
  transform: translateX(0);
  transition: all 1s ease;
}

/*进入开始状态*/
.slideBack-enter {
  transform: translateX(100%);
}

/*离开过渡生效时的状态*/
.slideBack-leave-active {
  transform: translateX(-100%);
  transition: all 1s ease;
}

/*离开过渡的开始状态*/
.slideBack-leave {
  transform: translateX(0);
}
#carousel{
    height: calc( 100% - 1.6vw);
}
.carousel{
    height: 100%;
}
.playButton{
    color: white;
    width: 2vw;
    height: 2vw;
    border: solid 1px white;
    border-radius: 1vw;
    font-size: 1vw;
    text-align: center;
    line-height: 2vw;
    position: absolute;
    z-index: 1;
    top: 38%;
    cursor: pointer;
}
.playAdd{
    right: 0.5vw;
}
.playminus{
    left: 0.5vw;
}
.playAdd:hover{
    background-color: rgba(255,255,255,0.2);
}
.playminus:hover{
    background-color: rgba(255,255,255,0.2);
}
</style>
