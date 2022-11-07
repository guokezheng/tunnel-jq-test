<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-03 16:00:36
 * @FilePath: \tunnel-ui\src\views\bigscreen\contingencyPlan\components\videoCarousel.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <div class="contentTitle">
      报警视频
      <i>Alarm video</i>
    </div>

    <v-carousel
      :slideData="slideData"
      :height="250"
      :begin="0"
      :interval="6000"
      :dot="true"
      :arrow="true"
    ></v-carousel>
  </div>
</template>

<script>
import carousel from "./carousel";
export default {
  components: {
    "v-carousel": carousel,
  },
  data() {
    return {
      video: require("../../../../assets/Example/v4.mp4"),
      slideData: [
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/v1.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/v2.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/v3.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/v4.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/d1.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/d2.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/d3.mp4"),
        },
        {
          title: "这是一个Vue轮播图组件",
          url: "#",
          video: require("../../../../assets/Example/d4.mp4"),
        },
      ],
    };
  },
  mounted() {
    // console.log(this.$refs.videoDom,"this.$refs.videoDom")
    // this.$refs.videoDom.src = this.video;
  },
  methods: {},
};
</script>

<style scoped="scoped">
.video-box {
  height: auto;
}
</style>
