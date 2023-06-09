<template>
  <div style="height: calc(100% + 38px)">
    <iframe
      name="tuniframe"
      id="miframe"
      class="robot"
      frameborder="0"
      align="center"
      allowfullscreen="true"
      allow="autoplay"
      :src="url"
    ></iframe>
  </div>
</template>
<script>
export default {
  data() {
    return {
      url:"http://42.193.36.70:38081/robot/robotManagement.html"
    }
  },
  created() {
    
  },
  methods: {
    
  },
}
</script>
<style>
.robot{
  width:100%;
  height:100%;
}
</style>