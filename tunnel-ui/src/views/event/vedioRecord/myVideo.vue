<template>
  <div class="video-box" v-loading="loading">
    <video class="demo-video" ref="player" muted  @dblclick="fullScreen"></video>
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
            open:{
                type:Boolean,
                default:false
            }
        },
        data () {
            return {
                // id: '1',
                player: null,
                loading: true
            }
        },
        watch: {
            open:{
                handler:function (val) {
                    if(val){
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
                    }else {
                        this.player.unload();
                        this.player.destroy();
                        this.player = null;
                    }
                },
                immediate: true
            }
        },
        created(){
        //   console.info("123");
        //   console.log(this.rtsp,"this.rtsp")
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
                console.log(this.rtsp,"this.rtspthis.rtspthis.rtsp");
              this.loading = true
                // const time1 = new Date().getTime();
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
                            stashInitialSize: 128, // 缓存大小(kb)  默认384kb
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
                    }
                }
            }
        },
        beforeDestroy () {
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
