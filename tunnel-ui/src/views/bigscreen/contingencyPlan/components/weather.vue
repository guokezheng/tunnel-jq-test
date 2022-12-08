<template>
  <div>
    <div class="contentTitle">
      今日天气预告
      <i>Real time alarm</i>
    </div>
    <div class="weatherBox alarmsStatisticsBox">
      <div class="weatherAddress">
        <div class="topBox">
          <img :src="weather.weatherImg" />
          <div>
            <p>济南市</p>
            <span>晴转多云</span>
          </div>
          <div class="Temperature">25°C</div>
        </div>
      </div>
      <!-- <div class="weatherNum">
        <div>
          <p>温度</p>
          <span>25°C~34°C</span>
        </div>
        <div>
          <p>湿度</p>
          <span>60%</span>
        </div>
        <div>
          <p>空气质量</p>
          <span>良</span>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      weather: {
        day: "",
        lowest: "",
        qing: "",
        weatherImg: "",
        week: "",
        weather: "",
        highest: "",
        wind: "",
        windsc: "",
      },
    };
  },
  mounted() {
    this.getWeather();
  },
  methods: {
    getWeather() {
      let city = "city=济南";
      let word = "tianqi";
      const key = "key=0c7ebab2461621aeb2c34b3a82e4c702";
      const header = "http://api.tianapi.com/txapi";
      const url = `${header}/${word}/?${key}&${city}`;
      axios.get(url).then((res) => {
        console.log(res);
        const data = res.data.newslist[0];
        this.weather.lowest = data.lowest;
        this.weather.highest = data.highest;
        this.weather.week = data.week;
        this.weather.weather = data.weather;
        this.weather.wind = data.wind;
        this.weather.windsc = data.windsc;
        this.weather.weatherImg = require("@/assets/weather/" +
          data.weatherimg +
          "");
        this.weather_weather =
          data.area +
          ":" +
          data.weather +
          data.lowest +
          data.wind +
          data.windsc;
        this.weather_weatherimg = require("@/assets/weather/" +
          data.weatherimg +
          "");
      });
    },
  },
};
</script>

<style lang="less" scoped>
.weatherBox {
  padding: 0.8vw 1vw;
  color: white;
  font-size: 1vw;
  text-align: center;

  .weatherAddress {
    width: 100%;
    height: 60%;
    // border: solid 1px white;
    .topBox {
      display: grid;
      grid-template-columns: repeat(3, 33.3%);
      grid-template-rows: 70px 70px;
      img {
        width: 1.5vw;
        height: 1.5vw;
        transform: translateY(0.4vw);
      }
      div {
        p {
          width: 100px;
          height: 28px;
          background-color: #4391f1;
          font-size: 16px;
          margin: 10px auto;
        }
        span {
          display: block;
          font-size: 16px;
          color: white;
        }
      }
      .Temperature {
        border-left: 1px solid white;
        font-size: 36px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
  .weatherPic {
    width: 25%;
    height: 100%;
    // border: solid 1px white;
    img {
      width: 5vw;
      height: 5vw;
    }
  }
  .weatherNum {
    width: 100%;
    height: 40%;
    // border: solid 1px white;
    > div {
      padding-top: 0.4vw;
      text-align: left;
      p {
        width: 100px;
        height: 28px;
        color: white;
        text-align: center;
      }
      span {
        color: #00c8ff;
      }
    }
  }
}
</style>
