<template>
  <div style="width: 100%; height: 100%">
    <el-row class="alarmBox" style="margin-top: 4px">
      <el-row class="tableHead">
        <el-col :span="7">
          <span>隧道名称</span>
        </el-col>
        <el-col :span="7">
          <span>事件类型</span>
        </el-col>

        <el-col :span="10">
          <span>发生时间</span>
        </el-col>
      </el-row>
      <scroll
        class="scrollStyle"
        :data="dataList"
        :class-option="defaultOption"
      >
        <el-row
          :class="index % 2 === 0 ? 'tableLine1' : 'tableLine2'"
          style="
            height: 3.5vh;
            font-size: 0.7em;
            overflow: hidden;
            text-align: center;
          "
          v-for="(item, index) in dataList"
          :key="item.id"
        >
          <el-col class="tableCell" :span="7">
            <span>{{ item.tunnelName }}</span>
          </el-col>
          <el-col class="tableCell" :span="7">
            <span>{{ item.eventType }}</span>
          </el-col>
          <el-col class="tableCell" :span="10">
            <span>{{ parseTime(item.createTime, "{y}-{m}-{d} {h}:{m}:{s}") }}</span>
          </el-col>
        </el-row>
      </scroll>
    </el-row>
  </div>
</template>

<script>
import scroll from "vue-seamless-scroll";
import {event} from "@/api/bigScreen/model6"
export default {
  data() {
    return {
      dataList: [],
    };
  },
  components: {
    scroll,
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 3, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 3000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  created() {
    this.getList()
  },
  methods: {
    getList(){
      event().then((res)=>{
        this.dataList = res.data.list
      })
    }
  },
};
</script>

<style lang="less" scoped>
.alarmBox {
  width: 100%;
  height: calc(100% - 40px);
  color: #d5d5d5;
  font-size: 0.7vw;
  .scrollStyle {
    height: calc(100% - 23px);
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
    background: url("../../../../assets/Example/bigScreen/scroll.png");
  }

  .tableLine1 {
    background: transparent;
  }

  .tableLine1:hover {
    background-image: linear-gradient(
      to right,
      rgba(69, 146, 210, 1),
      rgba(1, 71, 129, 0)
    ) !important;
    color: #ffff00 !important;
  }

  .tableLine2:hover {
    background-image: linear-gradient(
      to right,
      rgba(69, 146, 210, 1),
      rgba(1, 71, 129, 0)
    ) !important;
    color: #ffff00 !important;
  }

  .tableCell {
    text-align: center;
    justify-content: center;
    align-items: center;
    color:#d5d5d5;
    font-size: 0.7vw;
    .btn {
      width: 100%;
      height: 80%;
      font-size: 12px;
      background: transparent;
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
</style>
 
