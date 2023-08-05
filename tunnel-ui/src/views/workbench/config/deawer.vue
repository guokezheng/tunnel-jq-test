<template>
  <div>
    <div class="topNavRightDeawer">
      <div class="indicatorLight" @click="isDrawerA()">
        <i :class="[drawerA ? 'el-icon-caret-right' : 'el-icon-caret-left']"></i
        >一键控制模块
      </div>
      <!-- 定时控制模块 -->
      <div class="brightnessControl" @click="isDrawerB()">
        <i :class="[drawerB ? 'el-icon-caret-right' : 'el-icon-caret-left']"></i
        >定时控制模块
      </div>
      <div class="triggerControl" @click="isDrawerC()">
        <i
          :class="[
            drawerCVisible ? 'el-icon-caret-right' : 'el-icon-caret-left',
          ]"
        ></i
        >触发控制模块
      </div>
    </div>
    <!-- 一键车道控制模块 -->
    <el-drawer
      title="一键控制"
      :visible.sync="drawerA"
      :modal="false"
      :append-to-body="true"
      class="drawerTop"
    >
      <div style="width: 100%; height: 100%; position: relative">
        <div class="chezhiDrawerDirection">
          {{ directionList[0].dictLabel }}-车道指示器
        </div>
        <div class="chezhiDrawerInfo">
          <div class="chezhiName">车道:</div>
          <el-select
            v-model="chezhiForm1.lane"
            size="small"
            multiple
            collapse-tags
            class="chezhiLaneSelect"
          >
            <el-option
              v-for="item in chezhiLaneList"
              :key="item.laneId"
              :label="item.laneName"
              :value="item.laneId"
            />
          </el-select>
          <div class="chezhiName">状态:</div>
          <el-select
            v-model="chezhiForm1.state"
            size="small"
            class="chezhiStateSelect"
          >
            <el-option
              v-for="item in chezhiStateList"
              :key="item.Id"
              :value="item.deviceState"
              :label="item.stateName"
            >
              <div style="display: flex; align-items: center">
                <el-image
                  :src="item.url[0]"
                  style="width: 20px; height: 20px"
                ></el-image>
                <el-image
                  :src="item.url[1]"
                  style="width: 20px; height: 20px"
                ></el-image>
                <div style="margin-left: 4px">{{ item.stateName }}</div>
              </div>
            </el-option>
          </el-select>
          <el-button
            class="chezhiControlButton"
            @click="chezhiControl(1)"
            :disabled="chezhiDisabled"
            v-hasPermi="['workbench:dialog:save']"
          >
            控制
          </el-button>
        </div>

        <div class="chezhiDrawerDirection">
          {{ directionList[1].dictLabel }}-车道指示器
        </div>
        <div class="chezhiDrawerInfo">
          <div class="chezhiName">车道:</div>
          <el-select
            v-model="chezhiForm2.lane"
            size="small"
            multiple
            collapse-tags
            class="chezhiLaneSelect"
          >
            <el-option
              v-for="item in chezhiLaneList"
              :key="item.laneId"
              :label="item.laneName"
              :value="item.laneId"
            />
          </el-select>
          <div class="chezhiName">状态:</div>
          <el-select
            v-model="chezhiForm2.state"
            size="small"
            class="chezhiStateSelect"
          >
            <el-option
              v-for="item in chezhiStateList"
              :key="item.Id"
              :value="item.deviceState"
              :label="item.stateName"
            >
              <div style="display: flex; align-items: center">
                <el-image
                  :src="item.url[1]"
                  style="width: 20px; height: 20px"
                ></el-image>
                <el-image
                  :src="item.url[0]"
                  style="width: 20px; height: 20px"
                ></el-image>
                <div style="margin-left: 4px">{{ item.stateName }}</div>
              </div>
            </el-option>
          </el-select>
          <el-button
            class="chezhiControlButton"
            @click="chezhiControl(2)"
            :disabled="chezhiDisabled"
            v-hasPermi="['workbench:dialog:save']"
          >
            控制
          </el-button>
        </div>
        <div class="chezhiDrawerDirection" style="margin: 10px 0">
          {{ directionList[0].dictLabel }} -广播
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">播放次数:</div>
          <el-input-number
            v-model.number="phoneForm1.loopCount"
            controls-position="right"
            @change="handleChangePhone(1)"
            :min="0"
            size="small"
          />
          <el-checkbox
            v-model="phoneForm1.loop"
            label="循环播放"
            border
            class="phoneCheckBox"
            >循环播放</el-checkbox
          >
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">音量:</div>

          <el-slider
            v-model="phoneForm1.volume"
            :max="100"
            class="sliderClass"
          ></el-slider>
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">播放文件:</div>
          <el-select
            v-model="phoneForm1.fileNames"
            placeholder="请选择播放文件"
            clearable
            size="small"
            @click.native="clickFileNames(directionList[1].dictValue)"
          >
            <el-option
              v-for="item in fileNamesList"
              :key="item.name"
              :label="item.name"
              :value="item.fileName"
            />
          </el-select>
          <el-button
            class="chezhiControlButton"
            size="mini"
            @click="phoneControl(directionList[0].dictValue)"
            v-hasPermi="['workbench:dialog:save']"
          >
            控制
          </el-button>
        </div>
        <div class="chezhiDrawerDirection" style="margin: 10px 0">
          {{ directionList[1].dictLabel }} -广播
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">播放次数:</div>
          <el-input-number
            v-model.number="phoneForm2.loopCount"
            controls-position="right"
            @change="handleChangePhone(2)"
            size="small"
            :min="0"
          />
          <el-checkbox
            v-model="phoneForm2.loop"
            label="循环播放"
            border
            class="phoneCheckBox"
            >循环播放</el-checkbox
          >
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">音量:</div>
          <el-slider
            v-model="phoneForm2.volume"
            :max="100"
            class="sliderClass"
          ></el-slider>
        </div>
        <div class="phoneBox1">
          <div class="chezhiName">播放文件:</div>
          <el-select
            v-model="phoneForm2.fileNames"
            placeholder="请选择播放文件"
            clearable
            size="small"
            @click.native="clickFileNames(directionList[1].dictValue)"
          >
            <el-option
              v-for="item in fileNamesList"
              :key="item.name"
              :label="item.name"
              :value="item.fileName"
            />
          </el-select>
          <el-button
            class="chezhiControlButton"
            size="mini"
            @click="phoneControl(directionList[1].dictValue)"
            v-hasPermi="['workbench:dialog:save']"
          >
            控制
          </el-button>
        </div>
      </div>
    </el-drawer>
    <el-drawer
      title="定时控制模块"
      :visible.sync="drawerB"
      :modal="false"
      :append-to-body="true"
      class="drawerCenter"
    >
      <div
        v-for="item in timStrategyList"
        :key="item.strategy_id"
        style="width: 100%"
      >
        <div class="ledLighting">
          <span
            style="max-width: 340px; margin-right: 10px"
            class="overflowText"
            @mouseenter="showToolTip2(item.strategy_name)"
            >
            <el-tooltip v-if="item.show" :content="item.strategy_name">
                <span>{{ item.strategy_name }}</span>
              </el-tooltip>
              <span v-else>
                {{ item.strategy_name }}
              </span>
          </span>
          <el-switch
            v-model="item.strategy_state"
            active-value="0"
            inactive-value="1"
            @change="timStrategySwitch(item)"
          >
          </el-switch>
        </div>
        <div class="Time">
          <div class="timeStart">
            <span class="setTime">执行日期：</span>
            <!--            <el-time-picker-->
            <!--              v-model="item.arr[0]"-->
            <!--              size="mini"-->
            <!--              :clearable="false"-->
            <!--              value-format="yyyy-MM-dd"-->
            <!--            >-->
            <!--            </el-time-picker>-->
            <el-date-picker
              v-model="item.arr[0]"
              type="date"
              size="mini"
              value-format="yyyy-MM-dd"
              placeholder="请选择日期"
              :picker-options="setDisabled"
              disabled
            >
            </el-date-picker>
          </div>
          <div class="timeEnd">
            <span class="setTime">执行时间：</span>
            <el-time-picker
              v-model="item.arr[1]"
              size="mini"
              :clearable="false"
              value-format="HH:mm:ss"
              disabled
            >
            </el-time-picker>
          </div>
          <!--          <el-button-->
          <!--            type="primary"-->
          <!--            size="mini"-->
          <!--            class="handleLightClass"-->
          <!--            @click="timingStrategy(item)"-->
          <!--            v-hasPermi="['workbench:dialog:save']"-->
          <!--            :disabled="timingStrategyDisabled"-->
          <!--            >确定-->
          <!--          </el-button>-->
        </div>
      </div>
    </el-drawer>
    <!-- 触发控制模块 -->
    <el-drawer
      title="触发控制"
      :visible.sync="drawerCVisible"
      :modal="false"
      :append-to-body="true"
      class="drawerTop"
    >
      <div
        style="height: 150px; overflowy: auto; padding: 5px; padding-left: 10px"
      >
        <el-row
          style="
            line-height: 40px;
            border-bottom: 1px solid rgba(224, 231, 237, 0.2);
            color: #00c2ff;
          "
        >
          <el-col :span="7" style="padding-left: 4px">
            <span>预警名称</span>
          </el-col>
          <el-col :span="7">
            <span>预警类型</span>
          </el-col>
          <el-col :span="5">
            <span>触发值</span>
          </el-col>
          <el-col :span="4">
            <span >状态</span>
          </el-col>
        </el-row>

        <el-row
          v-for="(item, index) in isDrawerCList"
          :key="index"
          style="
            display: flex;
            padding: 4px;
            line-height: 30px;
            border-bottom: 1px solid rgba(224, 231, 237, 0.2);
          "
        >
          <el-col :span="7">
            <div class="overflowText" @mouseenter="showToolTip(item)">
              <el-tooltip v-if="item.show" :content="item.strategy_name">
                <span>{{ item.strategy_name }}</span>
              </el-tooltip>
              <span v-else>
                {{ item.strategy_name }}
              </span>
            </div>
          </el-col>
          <el-col :span="7">
            <div>
              {{ item.name }}
            </div>
          </el-col>
          <el-col :span="5">
            <div>
              {{ item.str }}
            </div>
          </el-col>
          <el-col :span="4">
            <el-switch
              v-model="item.strategyState"
              active-value="0"
              inactive-value="1"
              @change="changeStrategyState(item.id,item.strategyState)"
            ></el-switch>
          </el-col>
        </el-row>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { getStateByData } from "@/api/equipment/eqTypeState/api";
import {
  batchControlCarFinger,
  timeSharing,
  updateControlTime,
  timeStrategySwitch,
} from "@/api/workbench/config.js";
import {
  updateState,
  workTriggerInfo,
} from "@/api/event/strategy";
import {
  getAudioFileList,
  playVoiceGroup,
} from "@/api/equipment/eqlist/api";
import {changeJobStatus} from "@/api/monitor/job";
export default {
  data() {
    return {
      drawerA: false,
      drawerB: false,
      drawerCVisible: false,
      directionList: [{}, {}],
      phoneForm1: {
        loopCount: "1",
        loop: false,
        volume: 0,
        fileNames: [],
      },
      phoneForm2: {
        loopCount: 1,
        loop: false,
        volume: 0,
        fileNames: [],
      },
      // 一键车道指示器表单
      chezhiForm1: {
        lane: [],
        state: "",
      },
      chezhiForm2: {
        lane: [],
        state: "",
      },
      // 一键车指状态下拉框
      chezhiStateList: [],
      timingStrategyDisabled: false,
      timStrategyList: [], //定时控制
      // 一键车道指示器 车道下拉框
      chezhiLaneList: [],
      chezhiDisabled: false, //车指按钮 返回接口结果前禁用
      fileNamesList: [],
      isDrawerCList: [],
      setDisabled: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 86400000; // 可选历史天、可选当前天、不可选未来天
        },
      },
      chezhiLaneOptionList: [
        {
          laneId: 1,
          laneName: "1车道",
        },
        {
          laneId: 2,
          laneName: "2车道",
        },
        {
          laneId: 3,
          laneName: "3车道",
        },
        {
          laneId: 4,
          laneName: "4车道",
        },
        {
          laneId: 5,
          laneName: "5车道",
        },
        {
          laneId: 6,
          laneName: "6车道",
        },
        {
          laneId: 7,
          laneName: "7车道",
        },
        ,
        {
          laneId: 8,
          laneName: "8车道",
        },
        {
          laneId: 9,
          laneName: "9车道",
        },
      ],
      tunnelId: "",
      tunnelLane: "",
      tunnelIdNew: "",
    };
  },
  created() {
    this.getDicts("sd_strategy_direction").then((data) => {
      this.directionList = data.data;
    });
  },
  methods: {
    init(tunnelId, lane) {
      // console.log(tunnelId,lane,"tunnelId,lane")
      this.tunnelId = tunnelId;
      this.tunnelLane = lane;
      this.getTunnelState();
    },
    showToolTip(row) {
      // 改变列表数据的show字段，show为true时展示tooltip，false为隐藏tooltip
      this.isDrawerCList.forEach((item) => {
        if (
          item.strategy_name == row.strategy_name &&
          row.strategy_name.length > 10
        ) {
          item.show = true;
        } else {
          item.show = false;
        }
      });
      this.$forceUpdate();
    },
    showToolTip2(title){
      this.timStrategyList.forEach((item) => {
        if (
          item.strategy_name == title &&
          title.length > 23
        ) {
          item.show = true;
        } else {
          item.show = false;
        }
      });
      this.$forceUpdate();
    },
    // 抽屉车指批量控制 状态下拉框
    getTunnelState() {
      const param = {
        stateTypeId: 1,
        isControl: 1,
      };
      getStateByData(param).then((response) => {
        // console.log(response, "查询设备状态图标");
        // this.chezhiStateList = response.rows;
        this.chezhiStateList = [];
        for (let i = 0; i < response.rows.length; i++) {
          let iconUrl = [];
          if (response.rows[i].iFileList != null) {
            for (let j = 0; j < response.rows[i].iFileList.length; j++) {
              let img = response.rows[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          this.chezhiStateList.push({
            deviceState: response.rows[i].deviceState,
            stateName: response.rows[i].stateName,
            url: iconUrl,
          });
        }
      });
    },
    // 控制按钮
    chezhiControl(num) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      this.chezhiDisabled = true;
      const param = {
        tunnelId: this.tunnelId,
        direction: num,
        state: this["chezhiForm" + num].state,
        lane: this["chezhiForm" + num].lane,
      };
      batchControlCarFinger(param)
        .then((res) => {
          // console.log(res);
          loading.close();

          if (res.data == 0) {
            this.$modal.msgWarning("控制失败");
          } else if (res.data == 1) {
            this.$modal.msgSuccess("控制成功");
          }
        })
        .catch(() => {
          loading.close();
        });
      this.chezhiDisabled = false;
    },
    // 抽屉车指批量控制 车道下拉框
    getTunnelLane() {
      let laneArray = JSON.parse(JSON.stringify(this.chezhiLaneOptionList));
      this.chezhiLaneList = laneArray.slice(0, this.tunnelLane);
      // console.log(this.chezhiLaneList,"this.chezhiLaneList")
    },
    timingStrategy(item) {
      var time = item.arr.join("-");
      updateControlTime(item.strategy_id, time).then((res) => {
        this.$modal.msgSuccess("修改时间成功");
      });
    },
    changeEndTime(start, end, index) {
      // console.log(start, end, "start,end");
      let date = new Date();
      let a = start.split(":");
      let b = end.split(":");
      let bool = date.setHours(a[0], a[1]) > date.setHours(b[0], b[1]);
      if (bool) {
        this.$modal.msgWarning("结束时间必须大于开始时间");
        // console.log(this.timStrategyList, "this.timStrategyList");
        for (let i = 0; i < this.timStrategyList.length; i++) {
          this.timStrategyList[index].arr[1] = "";
          this.timingStrategyDisabled = true;
        }
      } else {
        this.timingStrategyDisabled = false;
      }
    },
    timStrategySwitch(item) {
      timeStrategySwitch(item.strategy_id, item.strategy_state).then((res) => {
        if (item.strategy_state == 0) {
          this.$modal.msgSuccess("开启成功");
        } else if (item.strategy_state == 1) {
          this.$modal.msgSuccess("关闭成功");
        }
      });
    },
    //抽屉
    isDrawerA() {
      this.drawerA = !this.drawerA;
      this.drawerB = false;
      this.drawerCVisible = false;
      // Object.assign(this.$data.phoneForm1, this.$options.data().phoneForm1)
      this.phoneForm1 = {
        loopCount: "1",
      };
      this.phoneForm2 = {
        loopCount: "1",
      };
      (this.chezhiForm1 = {
        lane: [],
        state: "",
      }),
        (this.chezhiForm2 = {
          lane: [],
          state: "",
        }),
        this.getTunnelLane();
      this.$forceUpdate();
    },
    isDrawerB() {
      this.drawerB = !this.drawerB;
      this.drawerA = false;
      this.drawerCVisible = false;
      this.timingStrategyDisabled = false;
      // debugger
      if (this.tunnelId) {
        timeSharing(this.tunnelId).then((res) => {
          for (var item of res.data) {
            let itemData = [];
            itemData.push(item.sky);
            itemData.push(item.minuteStr);
            item.arr = itemData;
          }
          this.timStrategyList = res.data;
        });
      }
    },
    isDrawerC() {
      this.drawerCVisible = !this.drawerCVisible;
      this.drawerA = false;
      this.drawerB = false;

      workTriggerInfo(this.tunnelId).then((response) => {
        // console.log(response, "自动触发抽屉");
        this.isDrawerCList = response.data;
      });
    },
    phoneControl(direction) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      if (direction == 1) {
        const param = {
          lib: "YeastarHost",
          loop: this.phoneForm1.loop,
          loopCount: this.phoneForm1.loopCount,
          volume: this.phoneForm1.volume,
          fileNames: Array(this.phoneForm1.fileNames),
          direction: direction,
          tunnelId: this.tunnelId,
          controlType: "0",
        };
        // console.log(param, "param");
        playVoiceGroup(param)
          .then((res) => {
            loading.close();
            this.$modal.msgSuccess("控制成功");
          })
          .catch(() => {
            loading.close();
          });
      } else {
        const param = {
          lib: "YeastarHost",
          loop: this.phoneForm2.loop,
          loopCount: this.phoneForm2.loopCount,
          volume: this.phoneForm2.volume,
          fileNames: Array(this.phoneForm2.fileNames),
          direction: direction,
          tunnelId: this.tunnelId,
          controlType: "0",
        };
        // console.log(param, "param");
        playVoiceGroup(param)
          .then((res) => {
            loading.close();
            this.$modal.msgSuccess("控制成功");
          })
          .catch(() => {
            loading.close();
          });
      }
      // console.log(direction,"广播一键控制方向");
    },
    // 广播播放控制次数
    handleChangePhone(num) {},
    // 点击侧边栏文件列表下拉框
    clickFileNames(direction) {
      const params = {
        tunnelId: this.tunnelId,
        direction: direction,
      };
      getAudioFileList(params).then((res) => {
        // console.log(res, "广播一键文件列表");
        this.fileNamesList = res.data;
      });
    },
    // 任务状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm('确认要"' + text + '""' + row.jobName + '"任务吗？')
        .then(function () {
          return changeJobStatus(row.jobId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    changeStrategyState(row,index) {
      let data = {strategyId: row, change: index};
      updateState(data).then((result) => {

        if(result.code == 200){
          if(index == 0){
            this.$modal.msgSuccess("开启成功");
          }else{
            this.$modal.msgSuccess("关闭成功");
          }
        }else{
          this.$modal.msgSuccess(result.msg);
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.overflowText {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.topNavRightDeawer {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  height: 100%;
}

.indicatorLight {
  width: 100%;
  height: 33%;
  background: linear-gradient(
    90deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  );
  color: white;
  writing-mode: vertical-lr;
  letter-spacing: 5px;
  font-size: 0.75vw;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

//照明控制
.brightnessControl {
  width: 100%;
  height: 33%;
  background: linear-gradient(
    90deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  );
  color: white;
  //垂直向下
  writing-mode: vertical-lr;
  text-align: center;
  //文字间隔
  letter-spacing: 5px;
  font-size: 0.75vw;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.el-icon-close:before {
  content: "\e6d9" !important;
}
// 触发控制模块
.triggerControl {
  width: 100%;
  height: 33%;
  background: linear-gradient(
    90deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  );
  color: white;
  //垂直向下
  writing-mode: vertical-lr;
  text-align: center;
  //文字间隔
  letter-spacing: 5px;
  font-size: 0.75vw;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}
.drawerTop {
  height: 62%;
  top: 120px;
  right: 38px;
}
.drawerCenter {
  height: 62%;
  top: 120px;
  right: 38px;

  // top: 33%;
}
.chezhiDrawerDirection {
  width: 100%;
  height: 30px;
  padding-left: 10px;
  line-height: 30px;
}
//抽屉的高度
::v-deep .el-drawer.rtl {
  width: 23% !important;
  font-size: 0.7vw;

  ::-webkit-scrollbar-track-piece {
    background-color: rgba($color: #00c2ff, $alpha: 0.1);
    border-left: 1px solid rgba(0, 0, 0, 0);
    width: 1000px;
    border-width: 50px;
  }

  ::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba($color: #00c2ff, $alpha: 0.6);
    background-clip: padding-box;

    min-height: 28px;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #00c2ff;
  }
}
::v-deep .el-drawer__header {
  font-size: 14px;
  margin-bottom: 0px !important;
  height: 36px;
  line-height: 40px;
  color: #fff;
}
//开关

::v-deep .el-drawer__body {
  .bingZhou {
    display: flex;
    height: 50%;
    padding: 20px;

    border: solid 1px red;
    font-size: 12px;
    display: flex;
    > .number {
      width: 28px;
      height: 28px;
      color: white;
      text-align: center;
      line-height: 28px;
      vertical-align: middle;
    }
    > .checkbox {
      width: 28px;
      height: 28px;
    }
  }

  .ledLighting {
    height: 36px;
    line-height: 40px;
    padding-left: 14px;
    font-size: 14px;
    display: flex;
    align-items: center;
  }

  .Time {
    display: flex;
    align-items: flex-start;
    height: 50px;
    padding-top: 10px;

    .setTime {
      white-space: nowrap;
      line-height: 32px;
    }

    .timeStart {
      display: flex;
      margin-right: 10px;
      margin-left: 14px;

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .timeEnd {
      display: flex;

      .el-button--mini {
        padding: 0px 0px !important;
      }

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .handleLightClass {
      height: 28px;
      margin: 0 5px;
      width: 4vw;
      text-align: center;
      // background-color: #07C2FF !important;
      border: none;
    }
  }
}
::v-deep .el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 100%;
}
.el-input-number {
  width: 5.4vw;
  line-height: 28px;
}
</style>
