<template>
  <div>
    <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" @click.native="click" :name="isFullscreen?'exit-fullscreen':'fullscreen'"/>
  </div>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
  data() {
    return {
      isFullscreen: false
    }
  },
  mounted() {
    this.init()
    //调用监听事件
    this.screenFull()
  },
  beforeDestroy() {
    this.destroy()
  },
  methods: {
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
      console.log(screenfull.isEnabled,"screenfull.isEnabled")
      if (!screenfull.isEnabled) {
        this.$message({ message: '你的浏览器不支持全屏', type: 'warning' })
        return false
      }
      screenfull.toggle()
      this.isFullscreen = !this.isFullscreen
      console.log(this.isFullscreen,"isFullscreen")
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
      console.log(this.isFullscreen,"this.isFullscreen")
    },
    init() {
      console.log(screenfull.isEnabled,"screenfull.isEnabled")
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
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
