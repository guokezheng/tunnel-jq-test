<template>
    <div class="weatherBox alarmsStatisticsBox">
        <div class="weatherAddress">
            <div>
                <span>济南 </span>
                <!-- <span class="changeCity"> [切换]</span> -->
            </div>
            <div>
                <span>风向：{{weather.wind}}&nbsp;{{weather.windsc}}</span>
            </div>
        </div>
        <div class="weatherPic">
            <img :src="weather.weatherImg" />
        </div>
        <div class="weatherNum">
            <div>今日({{weather.week}})</div>
            <div>{{weather.lowest}}~{{weather.highest}}</div>
            <div>{{weather.weather}}</div>
        </div>
    </div>
</template>

<script>

import axios from "axios";

export default {
  data(){
    return{
      weather:{
        day:'',
        lowest:'',
        qing:'',
        weatherImg:'',
        week:'',
        weather:'',
        highest:'',
        wind:'',
        windsc:'',
      },
    }
  },
  mounted() {
    this.getWeather();
  },
  methods: {
      getWeather(){
        let city = 'city=济南';
        let word = 'tianqi';
        const key='key=0c7ebab2461621aeb2c34b3a82e4c702';
        const header='http://api.tianapi.com/txapi';
        const url = `${header}/${word}/?${key}&${city}`;
        axios.get(url).then(res => {
          console.log(res);
          const data = res.data.newslist[0];
          this.weather.lowest = data.lowest;
          this.weather.highest = data.highest;
          this.weather.week = data.week;
          this.weather.weather = data.weather;
          this.weather.wind = data.wind;
          this.weather.windsc = data.windsc;
          this.weather.weatherImg = require('@/assets/weather/'+data.weatherimg+'');
          this.weather_weather = data.area +':'+data.weather + data.lowest + data.wind + data.windsc;
          this.weather_weatherimg = require('@/assets/weather/'+data.weatherimg+'');
        })
      }
    }
  }

</script>

<style lang="less" scoped>
    .weatherBox{
        display: flex;
        padding: 0.8vw 1vw;
        justify-content: center;
        color: white;
        font-size: 1vw;
        text-align: center;

        .weatherAddress{
            width: 39%;
            height: 100%;
            // border: solid 1px white;
            >div{
                padding-top: 1vw;
                text-align: left;
            }
            .changeCity{
                // border-bottom: 1px solid white;
                padding-bottom: 0.1vw;
                cursor: pointer;
                caret-color: rgba(0,0,0,0);
            }
            .weatherIcon{
                width: 1.5vw;
                height: 1.5vw;
                transform: translateY(0.4vw);
            }
        }
        .weatherPic{
            width: 25%;
            height: 100%;
            // border: solid 1px white;
            img{
                width: 5vw;
                height: 5vw;
            }
        }
        .weatherNum{
            width: 30%;
            height: 100%;
            // border: solid 1px white;
            >div{
                padding-top: 0.4vw;
                text-align: left;
            }
        }

    }
</style>
