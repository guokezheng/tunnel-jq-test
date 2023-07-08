<template>
  <div class="box">
    <div style="height: 30px">
      <el-select v-model="selectModel" size="small" class="bigScreenSelect">
        <el-option
          v-for="item in selectList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
    <el-row class="alarmBox" style="margin-top: 8px">
      <el-row class="tableHead">
        <el-col :span="6">
          <span>设施</span>
        </el-col>
        <el-col :span="6">
          <span>作业类型</span>
        </el-col>

        <el-col :span="4">
          <span>计划时间</span>
        </el-col>
        <el-col :span="4">
          <span>开工时间</span>
        </el-col>
        <el-col :span="4">
          <span>状态</span>
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
          <el-col class="tableCell" :span="5">
            <span>{{ item.eqName }}</span>
          </el-col>
          <el-col class="tableCell" :span="6">
            <span>{{ item.type }}</span>
          </el-col>
          <el-col class="tableCell" :span="4">
            <span>{{ item.planTime }}</span>
          </el-col>
          <el-col class="tableCell" :span="4">
            <span>{{ item.startTime }}</span>
          </el-col>
          <el-col class="tableCell" :span="5">
            <button
              style="background: linear-gradient(#ffcd48, 50%, #fe861e)"
              class="btn"
              v-if="item.dealStatus === 2"
            >
              一级验收
            </button>
            <button
              class="btn"
              v-if="item.dealStatus === 0"
            >
              确认
            </button>
            <!-- style="background: linear-gradient(#1eace8, 50%, #0074d4)" -->
            <button
              class="btn"
              v-if="item.dealStatus === 1"
            >
              执行
            </button>
          </el-col>
        </el-row>
      </scroll>
    </el-row>
  </div>
</template>
<script>
import scroll from "vue-seamless-scroll";

export default {
  data() {
    return {
      selectModel: 1,
      selectList: [
        {
          id: 1,
          name: "全部隧道",
        },
      ],
      dataList: [
        {
          id: "7",
          eqName: "马家峪隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 2,
        },
        {
          id: "6",
          eqName: "青风岭隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 1,
        },
        {
          id: "5",
          eqName: "盘山顶隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 1,
        },
        {
          id: "4",
          eqName: "万昌溜隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 1,
        },
        {
          id: "3",
          eqName: "北甲峪隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 0,
        },
        {
          id: "2",
          eqName: "仰长天隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 2,
        },
        {
          id: "1",
          eqName: "杭山东隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 2,
        },
        {
          id: "8",
          eqName: "金家楼隧道",
          type: "养护作业单",
          planTime: "05:00",
          startTime:'05:00',
          dealStatus: 2,
        },
      ],
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
  mounted() {},
  methods: {},
};
</script>
<style scoped lang="scss">
.box {
  height: calc(100% - 30px);
  .bigScreenSelect {
    float: right;
    padding-top: 4px;
    ::v-deep .el-input--small .el-input__inner {
      height: 28px !important;
      line-height: 28px !important;
      background-color: rgba(0,0,0,0.5) !important;
      border: solid 1px #0c4b72 !important;
      color: #9ba0bc !important;
    }
  }
  .alarmBox {
    width: 100%;
    height: calc(100% - 40px);
    color: #d5d5d5;
    font-size: 0.7vw;
    .scrollStyle {
      height: 190px;
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
      color: #d5d5d5;
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
}
</style>