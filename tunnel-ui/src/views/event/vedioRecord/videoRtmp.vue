<template>
  <div v-loading="loading">
    <video-player
      class="video-player vjs-custom-skin"
      ref="videoPlayer"
      :playsinline="true"
      webkit-playsinline=""
      :options="playerOptions"
      @ready="onPlayerReady"
      @play="onPlayerPlay($event)"
      @pause="onPlayerPause($event)"
      @ended="onPlayerEnd($event)"
      @waiting="onPlayerWaiting($event)"
      controls
      x5-video-player-fullscreen="true"
      x5-playsinline
    >
    </video-player>
  </div>
</template>
<script>
import videojs from "video.js";
import "video.js/dist/video-js.css";
import "vue-video-player/src/custom-theme.css";
import { videoPlayer } from "vue-video-player";
import "videojs-flash";
import SWF_URL from "videojs-swf/dist/video-js.swf";
videojs.options.flash.swf = SWF_URL; // 设置flash路径，Video.js会在不支持html5的浏览中使用flash播放视频文件
export default {
  name: "videojs",
  components: {
    videoPlayer,
  },
  data() {
    return {
      loading: false,
      videoSrc: "",
      playerOptions: {
        live: true,
        autoplay: true, // 如果true，浏览器准备好时开始播放
        muted: false, // 默认情况下将会消除任何音频
        loop: false, // 是否视频一结束就重新开始
        preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        aspectRatio: "16:9", // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        controlBar: {
          timeDivider: false, //以后时光跟连续时光的分开符
          durationDisplay: false, // 表现连续时光
          remainingTimeDisplay: false, // 能否表现残余时光功效
          currentTimeDisplay: false, // 当前时间
          volumeControl: false, // 声音控制键
          playToggle: false, // 暂停和播放键
          progressControl: false, // 进度条
          fullscreenToggle: true, // 全屏按钮
        },
        techOrder: ["flash"], // 兼容顺序
        flash: {
          hls: {
            withCredentials: false,
          },
          swf: SWF_URL,
        },
        sources: [
          {
            // type: "rtmp/flv",
            src: "rtmp://192.168.50.123:1231/live/2", // 视频地址-改变它的值播放的视频会改变
          },
        ],
        notSupportedMessage: "此视频暂无法播放，请稍后再试", // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
      },
    };
  },
  computed: {
    playsinline() {
      // 播放之后关闭静音
      return this.isIos();
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs.videoPlayer.player;
    });
  },
  destroyed() {
    this.$refs.videoPlayer.dispose;
  },
  created() {},
  methods: {
    init(url) {
      console.log(url, "url");
      this.playerOptions.sources[0].src = 'rtmp://192.168.50.123:1231/live/2'
      console.log(this.playerOptions,"this.playerOptions")
    },
    // 准备好了
    onPlayerReady(v) {
      console.log(1);
      console.log(v);
    },
    // 播放了
    onPlayerPlay(v) {
      console.log(2);
      console.log(v);
    },
    // 暂停了
    onPlayerPause(v) {
      console.log(3);
      console.log(v);
    },
    // 播放结束了
    onPlayerEnd(v) {
      console.log(4);
      console.log(v);
    },
  },
};
</script>
<style scoped lang="less">
.video-js {
  width: 100%;
  height: 100%;
  .no-video {
    display: flex;
    height: 100%;
    font-size: 14px;
    text-align: center;
    justify-content: center;
    align-items: center;
  }
}
::v-deep .video-js {
  background: transparent !important;

  .vjs-modal-dialog {
    background: transparent !important;
  }
}
</style>