<template>
  <div class="video-box" v-loading="loading">
    <video class="demo-video" ref="player" muted autoplay   @dblclick="fullScreen"></video>
  </div>
</template>
<script>
    import flvjs from 'flv.js'
    export default {
      props: {
          url: {
              type: String,
              default: ''
          }
      },
      data () {
          return {
              // id: '1',
              player: null,
              loading: false
          }
      },
      watch: {
        url: {
          handler() {
            if (this.player) {
              this.player.unload();
              this.player.destroy();
              this.player = null;
            }
            this.playVideo()
          },
          deep: true
        }
      },
      mounted() {
        this.loading = true;
        if (this.player) {
          //this.player.stream.close();
          this.player.unload();
          this.player.destroy();
          this.player = null;
        }
        this.$nextTick(() => {
          this.playVideo()
        })
      },
      beforeDestroy () {
        if (this.player) {
          this.player.unload();
          this.player.destroy();
          this.player = null
        }
      },
      methods: {
            fullScreen () {
                if (this.$refs.player.requestFullScreen) {
                    this.$refs.player.requestFullScreen()
                } else if (this.$refs.player.mozRequestFullScreen) {
                    this.$refs.player.mozRequestFullScreen()
                } else if (this.$refs.player.webkitRequestFullScreen) {
                    this.$refs.player.webkitRequestFullScreen()
                }
            },
            playVideo () {
                const time1 = new Date().getTime();
                if (flvjs.isSupported()) {
                    let video = this.$refs.player;
                    if (video) {
                        //创建播放器实例
                        this.player = flvjs.createPlayer({
                            type: 'flv',
                            isLive: true,
                            url: this.url,
                            enableStashBuffer: false
                        });
                        this.player.attachMediaElement(video);
                        try {
                            this.player.load();
                            this.player.play().then(() => {
                                console.log(new Date().getTime() - time1);
                                this.loading = false
                            })
                        } catch (error) {
                            console.log(error)
                        }
                    }
                }
            }
        },

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
