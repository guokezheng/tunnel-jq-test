<template>
    <div>
      <el-row class="alarmBox" style="margin-top: 4px;">
        <el-row class="tableHead">
          <el-col :span="8">
            <span>模式</span>
          </el-col>
          <el-col :span="8">
            <span>时间</span>
          </el-col>
         
          <el-col :span="8">
            <span>运行状态</span>
          </el-col>
        </el-row>
        <scroll class="scrollStyle" :data="dataList" :class-option="defaultOption" >
          <el-row :class="index%2===0 ? 'tableLine1': 'tableLine2'" style="height: 3.5vh;font-size: 0.7em;overflow: hidden;text-align: center;" v-for="(item,index) in dataList" :key="item.id">
            <el-col class="tableCell" :span="8">
              <span>{{ item.deptName }}</span>
            </el-col>
            <el-col class="tableCell" :span="8">
              <span>{{ item.happenTime }}</span>
            </el-col>
            <el-col class="tableCell" :span="8">
              <button style="background: linear-gradient(#1EACE8, 50%, #0074D4)" class="btn" v-if="item.dealStatus === 2">关闭</button>
              <button style="background: linear-gradient(#ffcd48, 50%, #fe861e)" class="btn" v-if="item.dealStatus === 0">进行中
              </button>
            </el-col>
          </el-row>
        </scroll>
      </el-row>
  
    </div>
  </template>
  
  <script>
  import scroll from 'vue-seamless-scroll'
  
  export default {
    components: {
      scroll
    },
    data () {
      return {
        // 实时告警数据
        dataList: [],
        // 告警统计数据
        alarmData: {
          dates: [],
          values: [],
          backValues: []
        }
      }
    },
    computed: {
      defaultOption () {
        return {
          step: 0.2, // 数值越大速度滚动越快
          limitMoveNum: 3, // 开始无缝滚动的数据量 this.dataList.length
          hoverStop: true, // 是否开启鼠标悬停stop
          direction: 1, // 0向下 1向上 2向左 3向右
          openWatch: true, // 开启数据实时监控刷新dom
          singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
          singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
          waitTime: 3000 // 单步运动停止的时间(默认值1000ms)
        }
      }
    },
    created () {
      this.dataList = [
        {
          id: '7',
          deptName: '全自动模式',
          content: '能耗异常-办公用电',
          happenTime: '2023-02-14',
          dealStatus: 2
        },
        {
          id: '8',
          deptName: '全功率模式',
          content: '污水处理异常',
          happenTime: '2023-02-16',
          dealStatus: 0
        },
        {
          id: '9',
          deptName: '车路协同模式',
          content: '能耗异常-照明用电',
          happenTime: '2023-02-15',
          dealStatus: 2
        },
        {
          id: '10',
          deptName: '全自动模式',
          content: '光伏阵列故障',
          happenTime: '2023-02-16',
          dealStatus: 0
        },
        {
          id: '11',
          deptName: '全功率模式',
          content: '能耗异常-桩号K10+80',
          happenTime: '2023-02-14',
          dealStatus: 0
        },
        {
          id: '1',
          deptName: '车路协同模式',
          content: '储能损耗过高',
          happenTime: '2023-02-16',
          dealStatus: 0
        },
        
      ]
    },
    methods: {}
  }
  </script>
  
  <style lang="less" scoped>
  .alarmBox {
    width: 100%;
    height: 100%;
    color: #d5d5d5;
    font-size: 0.7vw;
    .scrollStyle {
      height: 100px;
      overflow: hidden;
    }
  
    .tableHead {
      background-color: #01457e;
      font-size: 0.7vw;
      color: #ffffff;
      text-align: center;
      height: 2.5vh;
  
      span {
        line-height: 2.5vh;
      }
    }
  
    .tableLine2 {
      background: url('../../../../assets/Example/bigScreen/scroll.png');
    }
  
    .tableLine1 {
      background: transparent;
    }
  
    .tableLine1:hover {
      background-image: linear-gradient(to right, rgba(69, 146, 210, 1), rgba(1, 71, 129, 0)) !important;
      color: #ffff00 !important;
    }
  
    .tableLine2:hover {
      background-image: linear-gradient(to right, rgba(69, 146, 210, 1), rgba(1, 71, 129, 0)) !important;
      color: #ffff00 !important;
    }
  
    .tableCell {
      text-align: center;
      justify-content: center;
      align-items: center;
      color: #d5d5d5;
      font-size: 0.7vw;
      .btn {
        width: 60%;
        height: 80%;
        font-size: 12px;
        background: #0e90df;
        color: white;
        border: none;
        border-radius: 1px;
        margin-top: 8%;
      }
      span {
        line-height: 3.5vh;
      }
    }
  }
  
  .statisticsBox {
    height: 100%;
    position: relative;
  
    .statisticsTitle {
      position: absolute;
      top: 5%;
      left: 5%;
      font-size: 0.9em;
      color: #ffffff;
      font-weight: 200;
    }
  }
  </style>
  