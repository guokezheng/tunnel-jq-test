<template>
  <div class="box">
    <!-- <el-table
              :data="tableList" height="240" size="mini" class="bigScreenTable">
              <el-table-column prop="eqName" label="设备名称" width="76" align="center"/>
              <el-table-column prop="position" label="位置" width="60" align="center"/>
              <el-table-column prop="describe" label="异常描述" width="95" show-overflow-tooltip align="center"/>
              <el-table-column prop="time" label="最后预警时间" width="73" align="center"/>
              <el-table-column prop="rank" label="预警级别" width="50" align="center"/>
  
          </el-table> -->
    <el-row class="alarmBox" style="margin-top: 4px">
      <el-row class="tableHead">
        <el-col :span="5">
          <span>设备名称</span>
        </el-col>
        <el-col :span="5">
          <span>位置</span>
        </el-col>

        <el-col :span="5">
          <span>异常描述</span>
        </el-col>
        <el-col :span="6">
          <span>最后预警时间</span>
        </el-col>
        <el-col :span="3">
          <span>级别</span>
        </el-col>
      </el-row>
      <scroll
        class="scrollStyle"
        :data="tableList"
        :class-option="defaultOption"
      >
        <el-row
          :class="index % 2 === 0 ? 'tableLine1' : 'tableLine2'"
          class="tableLine"
          v-for="(item, index) in tableList"
          :key="item.id"
        >
          <el-col class="tableCell" :span="5">
            <el-tooltip effect="dark" :content="item.eqName" placement="top">
              <span>{{ item.eqName }}</span>
            </el-tooltip>
          </el-col>
          <el-col class="tableCell" :span="5">
            <span>{{ item.faultLocation }}</span>
          </el-col>
          <el-col class="tableCell" :span="5">
            <el-tooltip
              effect="dark"
              :content="item.faultDescription"
              placement="top"
            >
              <span>{{ item.faultDescription }}</span>
            </el-tooltip>
          </el-col>
          <el-col class="tableCell" :span="6">
            <span>{{ parseTime(item.faultFxtime, "{y}-{m}-{d}") }}</span>
          </el-col>
          <el-col class="tableCell" :span="3">
            <button
              :style="{
                background:
                  item.faultLevel === '0'
                    ? 'linear-gradient(#ffcd48, 50%, #fe861e)'
                    : 'linear-gradient(#1eace8, 50%, #0074d4)',
              }"
              class="btn"
            >
              {{ getFaultLevel(item.faultLevel) }}
            </button>
          </el-col>
        </el-row>
      </scroll>
    </el-row>
  </div>
</template>
  <script>
import scroll from "vue-seamless-scroll";
import { faultWarn } from "@/api/bigScreen/model2";
export default {
  data() {
    return {
      tableList: [],
      faultLevelList: [],
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
    this.getDicts("fault_level").then((data) => {
      this.faultLevelList = data.data;
    });
    this.getList();
  },
  methods: {
    getList() {
      faultWarn().then((res) => {
        this.tableList = res.data.list;
      });
    },
    getFaultLevel(num) {
      for (let item of this.faultLevelList) {
        if (num == item.dictValue) {
          return item.dictLabel.slice(0, 2);
        }
      }
    },
  },
};
</script>
  <style scoped lang="scss">
.tableLine {
  height: 3.5vh;
  font-size: 0.7em;
  overflow: hidden;
  text-align: center;
  cursor: default;
}
.box {
  height: calc(100% - 30px);
}
.alarmBox {
  width: 100%;
  height: 100%;
  color: #d5d5d5;
  font-size: 0.7vw;
  .scrollStyle {
    width: 100%;
    height: calc(100% - 30px);
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