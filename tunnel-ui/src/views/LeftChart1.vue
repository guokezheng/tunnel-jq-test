<template>
  <div class="left-chart-1">
    <div class="lc1-header">ËíµÀ¼ò½é<div class="youdong-icon" v-on:click="changeSd()"></div>
    </div>
    <span id="tunContent" class="lc1-cont">{{test.remake}}</span>
  </div>
</template>

<script>
  import request from '@/utils/request'
  export default {
    data() {
      return {

      };
    },
    props: ['test'],
    methods: {
      changeSd() {

      }
    },
  }
</script>

<style>
  .left-chart-1 {
    width: 100%;
    height: 37%;
    display: flex;
    flex-grow: 0;
    flex-direction: column;
  }

  .youdong-icon {
    background-image: url(../assets/icons/right.png);
    background-size: 100% 100%;
    width: 10px;
    height: 10px;
    cursor: pointer;
  }

  .lc1-header {
    text-align: center;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 25px;
    margin-top: 10px;
    margin-bottom: 5px;
    color: white;
  }

  .lc1-cont {
    width: 80%;
    text-align: center;
    height: 70%;
    justify-content: center;
    align-items: center;
    font-size: 18px;
    margin-left: 10%;
    color: #48e7ff;
  }

  .lc1-details {
    height: 50px;
    font-size: 16px;
    display: flex;
    align-items: center;
    text-indent: 20px;
  }


  .lc1-chart {
    flex: 1;
  }
</style>
