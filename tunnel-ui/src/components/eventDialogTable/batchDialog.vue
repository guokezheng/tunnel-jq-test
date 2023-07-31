<template>
  <div>
    <el-dialog
      title="批量执行"
      :visible.sync="batchDialog"
      :before-close="cancel"
      width="40%"
      text-align="left"
      class="detailsDialog"
      :close-on-click-modal="false"
      :modal="false"
      append-to-body
    >
      <el-form :model="form" label-width="80px" class="dialogForm" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="影响方向">
              <el-select
                v-model="form.direction"
                placeholder="方向"
                clearable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="item in directionList"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响车道">
              <el-select
                v-model="form.laneNo"
                placeholder="车道"
                clearable
                size="small"
                multiple
                collapse-tags
                style="width: 100%"
              >
                <el-option
                  v-for="(item, index) in chezhiLaneList"
                  :key="index"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-form-item label="事件车辆" prop="confidenceList">
          <el-input v-model="form.confidenceList" placeholder="" />
        </el-form-item> -->
        <el-form-item label="影响描述" prop="eventDescription">
          <el-input v-model="form.eventDescription" placeholder="影响描述" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预估类型" prop="eventTypeId">
              <el-select
                v-model="form.eventTypeId"
                clearable
                size="small"
                style="width: 100%"
                @change="getReservePlanData"
              >
                <el-option
                  v-for="(item, index) in eventTypeData"
                  :key="index"
                  :label="item.simplifyName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预估等级" prop="eventGrade">
              <el-select
                v-model="form.eventGrade"
                clearable
                size="small"
                style="width: calc(100% - 10px)"
                @change="getReservePlanData"
              >
                <el-option
                  v-for="(item, index) in eventGradeList"
                  :key="index"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="复核结果" prop="eventState">
          <el-radio-group v-model="form.eventState" @input="eventStateChange">
            <el-radio :label="4"> 确认(已处理) </el-radio>
            <!-- <el-radio :label="2"> 挂起(稍后处理) </el-radio> -->
            <el-radio :label="5"> 误报 </el-radio>
            <!-- <el-radio :label="0"> 突发事件处置 </el-radio> -->
          </el-radio-group>
          <span style="color: #c59105">(请根据复核判定结果选择)</span>
        </el-form-item>
        <el-form-item prop="reviewRemark" v-show="form.eventState == 4">
          <el-checkbox-group v-model="form.reviewRemark" class="checkBox">
            <el-checkbox-button
              label="已线下处理"
              value="已线下处理"
            ></el-checkbox-button>
            <el-checkbox-button
              label="车辆已驶离"
              value="车辆已驶离"
            ></el-checkbox-button>
            <el-checkbox-button
              label="施工车辆"
              value="施工车辆"
            ></el-checkbox-button>
            <el-checkbox-button
              label="正常施工作业"
              value="正常施工作业"
            ></el-checkbox-button>
            <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item prop="reviewRemark" v-show="form.eventState == 5">
          <el-checkbox-group v-model="form.reviewRemark" class="checkBox">
            <el-checkbox-button
              label="系统误报"
              value="系统误报"
            ></el-checkbox-button>
            <el-checkbox-button
              label="误报或涉事车辆已驶离"
              value="误报或涉事车辆已驶离"
            ></el-checkbox-button>
            <el-checkbox-button
              label="无法复核事发情况"
              value="无法复核事发情况"
            ></el-checkbox-button>
            <el-checkbox-button label="其他" value="其他"></el-checkbox-button>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item v-show="eventIsShow(form.reviewRemark)" prop="otherContent">
          <el-input
            placeholder="请输入其他原因内容"
            v-model="form.otherContent"
          ></el-input>
        </el-form-item>
        <div class="dialogFooterButton">
          <div @click="submitDialog">复核提交</div>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { getTunnelLane, batchHandleEvent } from "@/api/event/event";
import { listEventType } from "@/api/event/eventType";
export default {
  data() {
    return {
      batchDialog: false,
      form: {
        direction: "",
        laneNo: [],
        // confidenceList: "",
        eventDescription: "",
        eventTypeId: "",
        eventGrade: "",
        eventState: 4,
        reviewRemark: [],
      },
      directionList: [],
      chezhiLaneList: [],
      chezhiLaneList2: [],
      chezhiLaneList3: [],
      eventTypeData: [],
      eventGradeList: [],
      tunnelId: "",
      rules:{
        otherContent:[
          { max: 100, message: '最长输入100个字符', trigger: 'blur' }
        ]
      }
    };
  },
  watch: {
    "form.eventState": function (newVal, oldVal) {
      console.log(newVal, "newVal");
    },
  },
  created() {
    this.getDicts("sd_direction").then((response) => {
      this.directionList = response.data;
    });
    this.getDicts("sd_lane_one").then((data) => {
      this.chezhiLaneList2 = data.data;
    });
    // 三车道
    this.getDicts("sd_lane_two").then((data) => {
      this.chezhiLaneList3 = data.data;
    });
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeList = response.data;
    });
  },
  methods: {
    init(list, item) {
      this.getEventTypeList(item);
      this.list = list;
      this.form.direction = item.direction;
      this.tunnelId = item.tunnelId;
      this.batchDialog = true;
      this.tunnelLane();
    },
    // 获取车道数
    tunnelLane() {
      getTunnelLane(this.tunnelId).then((res) => {
        this.chezhiLaneList = [];
        if (res.data.lane == 1) {
          this.chezhiLaneList = this.chezhiLaneList1;
        } else if (res.data.lane == 2) {
          this.chezhiLaneList = this.chezhiLaneList2;
        } else if (res.data.lane == 3) {
          this.chezhiLaneList = this.chezhiLaneList3;
        }
      });
    },
    /** 查询事件类型列表 */
    getEventTypeList(item) {
      let prevControlType = {
        isUsable: "1",
        prevControlType: item.prevControlType,
      };
      listEventType(prevControlType).then((response) => {
        this.eventTypeData = response.rows;
        this.form.eventTypeId = Number(item.eventTypeId);
        if(this.form.eventTypeId == 20){
          this.form.laneNo = ['1','2','3']
        }
      });
    },
    cancel() {
      this.batchDialog = false;
      this.eventState = 4;
      this.form.laneNo = [];
      this.form.eventDescription = '';
      this.$emit("clearClick", 1);
    },
    // 预估等级 预估类型
    getReservePlanData() {
      if(this.form.eventTypeId == 20){
        this.form.laneNo = ['1','2','3']
      }
    },
    // 复核结果
    eventStateChange() {
      console.log(this.form.eventState, "this.form.eventState");
    },
    eventIsShow(value) {
      if (value != null) {
        if (value.includes("其他")) {
          return true;
        }
      } else {
        return false;
      }
    },
    
    // 复核弹窗内单选改变事件
    eventStateChange() {
      this.form.reviewRemark = [];
    },
    submitDialog() {
      this.form.ids = this.list.toString();
      this.form.laneNo = this.form.laneNo.toString();
      this.form.reviewRemark = this.form.reviewRemark.toString();

      batchHandleEvent(this.form).then((res) => {
        this.$modal.msgSuccess("批量执行成功");
        this.cancel();
      });
    },
  },
};
</script>
<style scoped lang="scss">
.dialogForm {
  padding: 10px;
}
.dialogFooterButton {
  width: 100%;
  height: 30px;
  display: flex;
  justify-content: right;
  margin-bottom: 15px;
  div {
    margin-right: 20px;
    width: 80px;
    height: 28px;
    border-radius: 14px;
    text-align: center;
    line-height: 28px;
    color: white;
    cursor: pointer;
  }
  div:nth-of-type(1) {
    background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
  }
  div:nth-of-type(2) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
  div:nth-of-type(3) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
}
</style>