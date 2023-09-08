<template>
  <div>
    <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" 
    @click.native="click" :name="isFullscreen?'exit-fullscreen':'fullscreen'"
    :style="{height: titleHeader == '1'?'1.8vh':'2vh'}"
    />
  </div>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
  data() {
    return {
      isFullscreen: false,
      titleHeader:this.$cache.local.get("navigationBar"), 
    }
  },
  mounted() {
    this.init()
    //调用监听事件
    this.screenFull()
    void ['fullscreenchange', 'webkitfullscreenchange', 'mozfullscreenchange'].forEach((item) => {
      window.addEventListener(item, () => {
        console.log('窗口变化', this.checkFull())
        this.isFullscreen = this.checkFull()
      })
    })
  },
  beforeDestroy() {
    this.destroy()
  },
  methods: {
    // 判断全屏
    checkFull(){
      //判断浏览器是否处于全屏状态 （需要考虑兼容问题）
      //火狐浏览器
      var isFull = document.mozFullScreen||
        document.fullScreen ||
        //谷歌浏览器及Webkit内核浏览器
        document.webkitIsFullScreen ||
        document.webkitRequestFullScreen ||
        document.mozRequestFullScreen ||
        document.msFullscreenEnabled
      if(isFull === undefined) {
        isFull = false
      }
      return isFull;

    },
     //监听Esc事件；
     screenFull() {
      //监听f11事件
      window.addEventListener("keydown", this.KeyDown, true);
     },
     KeyDown(event) {
      if (event.keyCode === 122) {
        event.returnValue = false;
        this.click(); //触发全屏的方法
      }
    },
    click() {
      // console.log(screenfull.isEnabled,"screenfull.isEnabled")
      if (!screenfull.isEnabled) {
        this.$message({ message: '你的浏览器不支持全屏', type: 'warning' })
        return false
      }
      screenfull.toggle()
      this.isFullscreen = !this.isFullscreen
      // console.log(this.isFullscreen,"isFullscreen")
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
      // console.log(this.isFullscreen,"this.isFullscreen")
    },
    init() {
      // console.log(screenfull.isEnabled,"screenfull.isEnabled")
      if (screenfull.isEnabled) {
        // screenfull.on('change', this.change)
        this.change()
      }
    },
    destroy() {
      if (screenfull.isEnabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;;
  width: 2vw;
  height: 2vh;
  vertical-align: 10px;
}
::v-deep .svg-icon{
  /* width: 2vw !important;
  height: 2vh !important;
  vertical-align: -2px !important; */
}
</style>
