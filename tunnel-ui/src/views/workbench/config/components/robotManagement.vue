<template>
  <div>
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
      url:"",
      userName: this.$store.state.user.name,
    }
  },
  created() {
    if(this.userName == "admin_sso"){
      this.url = "http://10.7.179.33:8080/robot/robotManagementDisable.html"
    }else{
      this.url = "http://10.7.179.33:8080/robot/robotManagement.html"
    }
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