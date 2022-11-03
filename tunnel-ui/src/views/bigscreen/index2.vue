<template>
  <div class="index-container">
    <div class="header">
      <el-row
        class="header-bgimg"
        :gutter="20"
        type="flex"
        align="middle"
        justify="space-between"
      >
        <el-col :span="5">
          <span class="header-icon"></span>
          <el-row type="flex" align="middle">
            <el-col class="header-icon"></el-col>
            <el-col class="header-left">
              <h1>智慧隧道云控平台</h1>
            </el-col>
            <el-col class="header-logo">
              <img src="@/assets/image/0-.png" alt="" />
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12" class="header-center">
          <el-row type="flex" class="header-center-btn">
            <button
              v-for="item in tabs"
              :key="item.id"
              :class="item.type == isAcitve ? 'isActive' : ''"
              @click="tabsClick(item)"
            >
              <a>{{ item.name }}</a>
            </button>
          </el-row>
        </el-col>
        <el-col :span="6" class="header-right">
          <el-row type="flex">
            <el-col style="font-size: 1.3vw">
              {{ weather_weather }}
            </el-col>
            <!-- <span style="margin: 0 10px 1px; font-size: 1vw">|</span> -->
            <el-col style="font-size: 1.5vw">{{ nowTime }}</el-col>
            <el-col style="font-size: 1.5vw; white-space: nowrap">{{
              nowDate
            }}</el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>

    <div class="body">
      <!-- 隧道管理 -->
      <tunnel v-if="isAcitve == 'tunnel'" />
      <!-- 报警研判 -->
      <warning v-if="isAcitve == 'warning'" />
      <!-- 应急预案 -->
      <contingencyPlan v-if="isAcitve == 'contingencyPlan'"></contingencyPlan>
      <!-- 智能能耗 -->
      <smartEnergyConsumption
        v-if="isAcitve == 'smartEnergyConsumption'"
      ></smartEnergyConsumption>
    </div>
  </div>
</template>

<script>
import tunnel from "./tunnel";
import warning from "./warning";
import contingencyPlan from "./contingencyPlan";
import smartEnergyConsumption from "./smartEnergyConsumption";
import axios from "axios";

export default {
  components: {
    tunnel,
    warning,
    contingencyPlan,
    smartEnergyConsumption,
  },
  data() {
    return {
      nowTime: "",
      nowMonth: "",
      nowDate: "",
      tabs: [
        { id: 0, name: "全视图", type: "tunnel" },
        { id: 1, name: "报警研判", type: "warning" },
        { id: 2, name: "应急处置", type: "contingencyPlan" },
        { id: 3, name: "智能能耗", type: "smartEnergyConsumption" },
      ],
      isAcitve: this.$cache.session.get("TABSSELECT") || "tunnel", // 当前页
      weather_weather: "", // 温度
    };
  },
  computed: {},
  mounted() {
    // html字体大小设置
    let htmlwidth =
      document.documentElement.clientWidth || document.body.clientWidth; //有些浏览器documentElement获取不到,那就使用后面的body
    console.log(htmlwidth);
    // 2.htmlDom
    let htmlDom = document.getElementsByTagName("html")[0];
    // 3.设置根元素样式
    htmlDom.style.fontSize = htmlwidth / 20 + "px"; // 记住这个20是等份的意思,这样每一份是16px,即1rem=16px;

    this.getWeather();
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
        this.$cache.session.set("TABSSELECT", item.type);
      }
    },
    // 获取天气
    getWeather() {
      let city = "city=济南";
      let word = "tianqi";
      const key = "key=0c7ebab2461621aeb2c34b3a82e4c702";
      const header = "http://api.tianapi.com/txapi";
      const url = `${header}/${word}/?${key}&${city}`;
      axios.get(url).then((res) => {
        const data = res.data.newslist[0];
        this.weather_weather = data.area + ": " + data.lowest;
      });
    },
  },
};
</script>

<style lang="less" scoped>
@import "~@/assets/font/font.css";
.index-container {
  width: 100%;
  padding: 1vw;
  background-color: #004375;
  height: 100%;
  .header {
    height: 6%;
    width: 100%;
    // border: solid 1px red;
    margin-bottom: 1vw;
    color: #09bdef;
    .header-bgimg {
      background: url(~@/assets/image/wire.png) no-repeat;
      background-size: 100% 100%;
      padding: 10px 0;
      width: 100%;
      height: 100%;
      margin-left: 0px !important;
      margin-right: 0px !important;

      .header-icon {
        width: 3vw;
        height: 2vw;
        background-image: url(~@/assets/image/logo.png);
        background-size: 100%;
        background-repeat: no-repeat;
        background-position: center;
      }
      .header-logo {
        width: 5vw;
        height: 2vw;
        padding: 0 !important;
        margin-right: 1vw;
        font-size: 0;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .header-left {
        white-space: nowrap;
        margin-right: 10px;
        h1 {
          font-size: 1.5vw;
          padding: 0;
          margin: 0;
        }
      }
      .header-right {
        font-family: Montserrat-Light;
        text-align: center;
        .el-row {
          align-items: center;
        }
      }
      .header-center {
        .header-center-btn {
          button {
            background-color: #143675;
            width: 20%;
            height: 1.5vw;
            text-align: center;
            transform: skewX(-45deg);
            margin-right: 10px;
            border-color: #09bdef;
            font-size: 0.8vw;
            > a {
              transform: scale(0.8);
              display: block;
              color: white;
              transform: skewX(45deg);
            }
            &:last-child {
              margin-right: 0;
            }
          }

          .isActive {
            background-color: #09bdef;
          }
        }
      }
    }
  }
  .body {
    height: 91.5%;
    color: white;
  }
}
</style>
