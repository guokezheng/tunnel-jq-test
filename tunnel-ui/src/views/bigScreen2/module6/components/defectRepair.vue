<template>
  <div class="box">
    <div class="row1">
      <div class="miniBox">
        <div class="round" style="background: #d57e06">2</div>
        <div class="text">全部</div>
      </div>
      <div class="miniBox">
        <div class="round" style="background: #379df8">2</div>
        <div class="text">蓝色</div>
      </div>
      <div class="miniBox">
        <div class="round" style="background: #ebc106">0</div>
        <div class="text">黄色</div>
      </div>
      <div class="miniBox">
        <div class="round" style="background: #f73f41">0</div>
        <div class="text">红色</div>
      </div>
    </div>
    <div class="row2">
      <div class="content" v-for="(item, index) in list" :key="index">
        <div>{{ item.time }}</div>
        <div>{{ item.grade }}</div>
        <div>{{ item.describe }}</div>
        <div>{{ item.influence }}</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      list: [
        {
          time: "开始时间：2023-03-03 13：00:00",
          grade: "等级：弱",
          describe: "描述：强风",
          influence: "影响范围：K470+000 - K542+000",
        },
        {
          time: "开始时间：2023-03-03 13：00:00",
          grade: "等级：弱",
          describe: "描述：强风",
          influence: "影响范围：K470+000 - K542+000",
        },
        {
          time: "开始时间：2023-03-03 13：00:00",
          grade: "等级：弱",
          describe: "描述：强风",
          influence: "影响范围：K470+000 - K542+000",
        },
      ],
    };
  },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style scoped lang="scss">
.box {
  width: 100%;
  height: calc(100% - 30px);
  color: #d5d5d5;
  font-size: 0.7vw;
  .row1 {
    width: 100%;
    height: 100px;
    padding-top: 4px;
    display: flex;
    background: rgba($color: #1c568a, $alpha: 0.3);
    justify-content: space-around;
    margin-bottom:4px;
    .miniBox {
      width: 22%;
      height: 100%;
      text-align: center;
      padding-top: 10px;
      cursor: default;

      .round {
        width: 50px;
        height: 50px;
        text-align: center;
        line-height: 50px;
        font-size: 20px;
        border-radius: 30px;
        margin: 0 auto;
        color:#FFF;
      }
      .text {
        margin-top: 10px;
        font-size: 12px;
      }
    }
  }
  .row2{
    height: calc(100% - 110px);
    overflow:auto;
    .content{
      background: rgba($color: #1c568a, $alpha: 0.3);
      margin-bottom:4px;
      padding: 20px;
      cursor: default;
    }
  }
}
::-webkit-scrollbar-track {
      background-color: #022A54;
    }
  
    ::-webkit-scrollbar {
      width: 1px !important;
      background-color: #022A54;
    }
  
    ::-webkit-scrollbar-thumb {
      background-color: #0090D8;
    }
</style>
