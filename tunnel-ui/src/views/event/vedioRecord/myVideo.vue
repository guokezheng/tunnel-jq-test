<template>
  <div class="video-box" v-loading="loading">
    <video class="demo-video" ref="player" muted @dblclick="fullScreen"></video>
  </div>
</template>
<script>
  import flvjs from 'flv.js'
  export default {
    props: {
      rtsp: {
        type: String,
        default: ''
      },
      id: {
        type: [String, Number],
        default: ''
      },
      hostIP: {
        type: String,
        default: ''
      },
      open: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        // id: '1',
        player: null,
        loading: true,
        timerId: null
      }
    },
    watch: {
      open: {
        handler: function (val) {
          if (val) {
            if (this.player) {
              this.player.stream.close();
              this.player.unload();
              this.player.destroy();
              this.player = null;
              this.loading = true
            }
            this.$nextTick(() => {
              this.playVideo()
            })
          } else {
            this.player.unload();
            this.player.destroy();
            this.player = null;
          }
        },
        immediate: true
      }
    },
    created() {
      if (this.timerId) {
        clearInterval(this.timerId)
        this.timerId = null
      }
      //   console.info("123");
      //   console.log(this.rtsp,"this.rtsp")
    },
    destroyed() {
      console.log("destorying video");
      if (this.timerId) {
        clearInterval(this.timerId)
        this.timerId = null
      }
      if (this.player) {
        this.player.unload();
        this.player.destroy();
        this.player = null
      }
    },
    methods: {

      fullScreen() {
        if (this.$refs.player.requestFullScreen) {
          this.$refs.player.requestFullScreen()
        } else if (this.$refs.player.mozRequestFullScreen) {
          this.$refs.player.mozRequestFullScreen()
        } else if (this.$refs.player.webkitRequestFullScreen) {
          this.$refs.player.webkitRequestFullScreen()
        }
      },
      playVideo() {
        if (this.timerId !== null) {
          clearInterval(this.timerId);
        }
        const video = document.querySelector('video');
        video.disablePictureInPicture = true;
        console.log(this.rtsp, "this.rtspthis.rtspthis.rtsp");
        this.loading = true
        // const time1 = new Date().getTime();
        if (flvjs.isSupported()) {
          let video = this.$refs.player;
          if (video) {
            //创建播放器实例"https://sf1-hscdn-tos.pstatp.com/obj/media-fe/xgplayer_doc_video/flv/xgplayer-demo-360p.flv",
            this.player = flvjs.createPlayer({
              type: 'flv',
              isLive: true,
              url: this.rtsp,
              // enableStashBuffer: false,
              cors: true, // 是否跨域
              enableWorker: true, // 是否多线程工作
              enableStashBuffer: false, // 是否启用缓存
              stashInitialSize: 384 * 1024, // 缓存大小(kb)  默认384kb
              autoCleanupSourceBuffer: true // 是否自动清理缓存
            }
              // ,{
              //     cors: true, // 是否跨域
              //     enableWorker: true, // 是否多线程工作
              //     enableStashBuffer: false, // 是否启用缓存
              //     stashInitialSize: 128, // 缓存大小(kb)  默认384kb
              //     autoCleanupSourceBuffer: true // 是否自动清理缓存
              // }
            );
            this.player.attachMediaElement(video);
            try {
              this.player.load();
              this.player.play().then(() => {
                // console.log(new Date().getTime() - time1);
                this.loading = false
              })
            } catch (error) {
              console.log(error)
            }
            let that = this
            this.player.on(flvjs.Events.ERROR, function (errorType, errorDetails) {
              // 处理错误
              // console.log("3333333333333333333333333333333333333")
              // console.error(`FLV.js Error - Type: ${errorType}, Details: ${errorDetails}`);
              // debugger
              // if (this.player) {
              // debugger
              that.destroyFlv();
              that.init();
            });
            this.timerId = setInterval(function () {
              if (that.$refs.player.buffered) {
                if (that.$refs.player.buffered.length > 0) {
                  const end = that.$refs.player.buffered.end(0);  // 视频结尾时间
                  const current = that.$refs.player.currentTime;  //  视频当前时间
                  const diff = end - current;// 相差时间
                  // console.log(diff);
                  const diffCritical = 4; // 这里设定了超过4秒以上就进行跳转
                  const diffSpeedUp = 1; // 这里设置了超过1秒以上则进行视频加速播放
                  const maxPlaybackRate = 4;// 自定义设置允许的最大播放速度
                  let playbackRate = 1.0; // 播放速度
                  if (diff > diffCritical) {
                    // console.log("相差超过4秒，进行跳转");
                    that.$refs.player.currentTime = end - 1.5;
                    playbackRate = Math.max(1, Math.min(diffCritical, 16));
                  } else if (diff > diffSpeedUp) {
                    // console.log("相差超过1秒，进行加速");
                    playbackRate = Math.max(1, Math.min(diff, maxPlaybackRate, 16))
                  }
                  that.$refs.player.playbackRate = playbackRate;
                  if (that.$refs.player.paused) {
                    that.player.load();
                  }
                }
              }
            }, 1000)
          }
        }
      },
      destroyFlv() {
        // debugger
        // console.log(this.player)
        if (this.player) {
          this.player.pause();
          this.player.unload();
          this.player.detachMediaElement();
          this.player.destroy();
          this.player = null;
          this.timerId = null
          clearInterval(this.timerId);
        }
      },
      init() {
        if (flvjs.isSupported()) {
          let video = this.$refs.player;
          if (video) {
            //创建播放器实例
            this.player = flvjs.createPlayer({
              type: 'flv',
              isLive: true,
              url: this.rtsp,
              // enableStashBuffer: false,
              cors: true, // 是否跨域
              enableWorker: true, // 是否多线程工作
              enableStashBuffer: false, // 是否启用缓存
              stashInitialSize: 384 * 1024, // 缓存大小(kb)  默认384kb
              autoCleanupSourceBuffer: true // 是否自动清理缓存
            }
              // ,{
              //     cors: true, // 是否跨域
              //     enableWorker: true, // 是否多线程工作
              //     enableStashBuffer: false, // 是否启用缓存
              //     stashInitialSize: 128, // 缓存大小(kb)  默认384kb
              //     autoCleanupSourceBuffer: true // 是否自动清理缓存
              // }
            );
            // debugger
            this.player.attachMediaElement(video);
            try {
              this.player.load();
              this.player.play().then(() => {
                // console.log(new Date().getTime() - time1);
                // this.loading = false
              })
            } catch (error) {
              console.log(error)
            }
            let that = this
            this.player.on(flvjs.Events.ERROR, function (errorType, errorDetails) {
              // 处理错误
              console.log("视频处理错误")
              // console.log("3333333333333333333333333333333333333")
              that.destroyFlv();
              that.init();
            });
          }
        }
      },
    },
    beforeDestroy() {
      if (this.player) {
        this.player.unload();
        this.player.destroy();
        this.player = null
      }
    }
  }
</script>
<style lang="scss">
  .video-box {
    width: 100%;
    height: 100%;

    video {
      width: 100%;
      height: 100%;
      object-fit: fill;
    }

    //播放按钮
    /*video::-webkit-media-controls-play-button {
      display: none;
    }
    video::-webkit-media-controls-current-time-display {
      display: none;
    }
    video::-webkit-media-controls-timeline {
      display: none;
    }*/
  }
</style>