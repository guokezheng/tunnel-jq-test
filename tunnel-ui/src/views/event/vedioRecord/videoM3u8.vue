<template>
  <div class="myVideo">
    <video
      id="videoPlayer"
      class="video-js vjs-default-skin"
      controls
      playsinline
      autoplay="autoplay"
      width="500px"
    >
      <source :src="attachmentLink" type="application/x-mpegURL" />
    </video>
  </div>
</template>
  
  <script>
import videojs from "video.js";
import "video.js/dist/video-js.css";
export default {
  data() {
    return {
      dp: null,
      options: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
        aspectRatio: "16:9",
        notSupportedMessage: "此视频暂无法播放，请稍后再试", //允许覆盖Video.js无法播放媒体源时显示的默认信息。
        autoplay: false, // 设置自动播放
        muted: true, // 设置了它为true，才可实现自动播放,同时视频也被静音（Chrome66及以上版本，禁止音视频的自动播放）
        preload: "auto", // 预加载
        controls: true, // 显示播放的控件
      },
      attachmentLink: "http://10.7.182.85:40080/hls2FAF2DE3/9_10.7.182.85.m3u8",
    };
  },
  mounted() {
    // 使用 $nextTick 解决vedio报错  The element or ID supplied is not valid. (videojs)
    this.$nextTick(() => {
      this.loadVideo();
    });
  },
  methods: {
    loadVideo() {
      this.dp = videojs("videoPlayer", this.options);
      // 也可以使用以下方式给vedio设置 src
      // this.db.src([
      //   {
      //     src: "https://e-sign.dms.t.cn-np.com/files/m3u8_file/c4b94118-3c8d-4410-9987-985c2b44c278/c4b94118-3c8d-4410-9987-985c2b44c278.m3u8", // 地址
      //     type: "application/x-mpegURL", // 告诉videojs,这是一个hls流
      //   },
      // ]);
    },
    // 销毁
    beforeDestroy() {
      if (this.dp) {
        this.dp.dispose(); // dispose()会直接删除Dom元素
      }
    },
  },
};
</script>
  
  <style lang="less" scoped>
.video-box {
  width: 100%;
  max-width: 500px;
  max-height: 500px;
}
// 暂停播放按钮居中
::v-deep .video-js .vjs-big-play-button {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
  