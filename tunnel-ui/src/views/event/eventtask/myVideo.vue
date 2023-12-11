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
            /*rtsp: {
                handler: function (val) {
                    if (val) {
                        if (this.player) {
                            this.player.unload()
                            this.player.destroy()
                            this.player = null
                            this.loading = true
                        }
                        this.$nextTick(() => {
                            this.playVideo()
                        })
                    }
                },
                immediate: true
            },*/
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
                            url: this.rtsp,//"http://172.16.12.143/live?app=live&stream=jtsj-112",//`ws://${this.hostIP}:8091/rtsp/${this.id}/?url=${this.rtsp}`,
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
    height: 100%;}
    video {
      width: 100%;
      height: 100%;
      object-fit: fill;
    }
    /*video::-webkit-media-controls-play-button {
      display: none;
    }
    video::-webkit-media-controls-current-time-display {
      display: none;
    }
    video::-webkit-media-controls-timeline {
      display: none;
    }*/
</style>
