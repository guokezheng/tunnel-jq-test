<template>
  <div class="index-container">
    <div class="header">
      <el-row class="header-box" :gutter="20" type="flex" justify="space-between">
        <el-col class="header-left">
          <span style="margin-right:1vw;">济南</span>
          <span>{{ temperature }}℃</span>
        </el-col>
        <el-col class="header-center">
          <span class="header-icon"></span>
          <h1 class="header-h1">智慧隧道云控平台</h1>
        </el-col>
        <el-col class="header-right">
          <span>{{ nowTime }}</span>
          <span style="margin-left:1vw;white-space: nowrap;">{{ nowDate }}</span>
        </el-col>
      </el-row>
    </div>

    <div class="body">
      <!-- 隧道管理 -->
      <company-rights />
    </div>
  </div>
</template>

<script>
import CompanyRights from "./CompanyRights";

export default {
  components: {
    CompanyRights
  },
  data() {
    return {
      nowTime: "",
      nowMonth: "",
      nowDate: "",
      temperature: "4" // 温度
      // tabs: [
      //   { id: 0, name: "全视图", type: "tunnel" },
      //   { id: 1, name: "报警研判", type: "warning" },
      //   { id: 2, name: "应急预案", type: "contingencyPlan" },
      //   { id: 3, name: "智能能耗", type: "smartEnergyConsumption" },
      // ],
      // isAcitve: "tunnel", // 当前页
    };
  },
  computed: {},
  mounted() {
    // html字体大小设置
    let htmlwidth =
      document.documentElement.clientWidth || document.body.clientWidth; //有些浏览器documentElement获取不到,那就使用后面的body
    // 2.htmlDom
    let htmlDom = document.getElementsByTagName("html")[0];
    // 3.设置根元素样式
    htmlDom.style.fontSize = htmlwidth / 20 + "px"; // 记住这个20是等份的意思,这样每一份是16px,即1rem=16px;

    this.initTime();
  },
  methods: {
    // 初始化时间
    initTime() {
      setInterval(() => {
        var date = new Date();
        var year = date.getFullYear();
        var month =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours =
          date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes =
          date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds =
          date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        this.nowTime = hours + ":" + minutes + ":" + seconds;
        this.nowDate = year + "-" + month + "-" + day;
        this.nowMonth = date.toDateString().split(" ")[1];
      }, 1000);
    },
    // 切换tabs标签页
    tabsClick(item) {
      if (item.type != this.isAcitve) {
        this.isAcitve = item.type;
      }
    }
  }
};
</script>

<style lang="less" scoped>
@import "~@/assets/font/font.css";
@import "~@/views/bigscreen/styles/index.css";
.index-container {
  width: 100%;
  padding: 0 1vw 1vw;
  background-color: #040f4e;
  color: #09bdef;
  height: 100%;
  .header {
    height: 8%;
    width: 100%;
    margin-bottom: 1vw;
    background: url(~@/assets/image/top.png) no-repeat;
    background-size: 100% 100%;

    .header-box {
      width: 100%;
      height: 100%;
      margin-left: 0px !important;
      margin-right: 0px !important;

      .header-left {
        display: flex;
        align-items: flex-end;
        font-size: 1.3vw;
      }

      .header-center {
        display: flex;
        align-items: center;
        justify-content: center;
        .header-icon {
          width: 2vw;
          height: 2vw;
          background-image: url(~@/assets/image/logo.png);
          background-size: 100%;
          background-repeat: no-repeat;
          background-position: center;
          margin-right: 0.5vw;
        }
        .header-h1 {
          padding: 0;
          margin: 0;
          white-space: nowrap;
          margin-right: 10px;
          font-size: 1.5vw;
        }
      }

      .header-right {
        display: flex;
        align-items: flex-end;
        justify-content: flex-end;
        font-family: Montserrat-Light;
        text-align: center;
        font-size: 1.5vw;
      }
    }
  }
  .body {
    height: 89.5%;
  }
}
</style>
