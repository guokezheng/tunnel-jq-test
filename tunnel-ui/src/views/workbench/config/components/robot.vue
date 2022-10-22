<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      width="450px"
      append-to-body
      :visible="visible"
      :before-close="handleClosee"
    >
      <div
        style="
          width: 100%;
          height: 30px;
          display: flex;
          justify-content: space-between;
        "
      >
        <div class="dialogLine"></div>
        <img
          :src="titleIcon"
          style="height: 30px; transform: translateY(-30px); cursor: pointer"
          @click="handleClosee"
        />
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="90px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备类型:">
              {{ stateForm.eqTypeName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="所属方向:">
              {{ stateForm.eqDirection }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.brandName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备电量:">
              <div
                style="
                  width: 100px;
                  height: 30px;
                  display: flex;
                  align-items: center;
                "
              >
                <div
                  style="
                    width: 30px;
                    height: 16px;
                    border: solid 2px #00c376;
                    display: flex;
                    align-items: center;
                  "
                >
                  <div
                    style="width: 80%; height: 10px; background: #00c376"
                  ></div>
                </div>
                <div
                  style="width: 2px; height: 10px; border: solid 1px #00c376"
                ></div>
                <span style="padding-left: 10px"> 80%</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="设备状态:">
              <!-- {{ stateForm.eqStatus }} -->
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="lineClass"></div>
      <el-radio-group
        v-model="tab"
        style="margin-bottom: 10px"
        class="tabRobot"
      >
        <el-radio-button label="trafficFlow">车流量情况</el-radio-button>
        <el-radio-button label="event">事件情况</el-radio-button>
        <el-radio-button label="road">地道路面情况</el-radio-button>
        <el-radio-button label="state">状态记录</el-radio-button>
      </el-radio-group>
      <div
        v-show="tab == 'trafficFlow'"
        style="margin-bottom: 10px"
        id="traffic"
      ></div>
      <div v-show="tab == 'event'" style="margin-bottom: 10px">事件情况</div>
      <div v-show="tab == 'road'" style="margin-bottom: 10px">地道路面情况</div>
      <div v-show="tab == 'state'" style="margin-bottom: 10px">状态记录</div>
      <div slot="footer">
          <el-button
            type="primary"
            size="mini"
            @click="handleOK()"
            style="width: 80px"
            class="submitButton"
            >确 定</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="handleClosee()"
            style="width: 80px"
            >取 消</el-button
          >
        </div>
    </el-dialog>
  </div>
</template>
  <script>
import * as echarts from "echarts";

import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
  data() {
    return {
      stateForm: {},
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      tab: "trafficFlow",
    };
  },
  created() {
    this.getMessage();
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      // if (this.eqInfo.equipmentId) {
        var obj = {};
        var state = "";
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          obj = res.data;

          this.title = obj.eqName;
          this.stateForm = {
            brandName: that.getBrandName(obj.brandId), //厂商
            eqDirection: that.getDirection(obj.eqDirection),

            pile: obj.pile, //桩号
            eqTypeName: obj.typeName, //设备类型名称
            tunnelName: obj.tunnelName, //隧道名称
            deptName: obj.deptName, //所属机构
            eqType: obj.eqType, //设备类型号
            state: obj.state,
          };
          console.log(this.stateForm, "stateForm");
        });
      // } else {
      //   this.$modal.msgWarning("没有设备Id");
      // }
    },
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    getBrandName(num) {
      // 根据字典表查设备厂商--------------------------
      for (var item of this.brandList) {
        if (Number(item.dictValue) == num) {
          return item.dictLabel;
        }
      }
    },
    geteqType(num) {
      for (var item of this.eqTypeDialogList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    initChart() {
        var traffic = echarts.init(document.getElementById('traffic'));

      var option = {
        // backgroundColor: '#00043A',
        tooltip: {
          trigger: "axis",
          show: true,
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: "{b}<br>能耗： {c} ",
        },
        legend: {
          show: false,
        },
        grid: {
          left: "10%",
          right: "10%",
          bottom: "8%",
          top: "30%",
          containLabel: true,
        },
        xAxis: [
          {
            // name:'日',
            type: "category",
            // boundaryGap : false,
            axisLabel: {
              textStyle: {
                color: '#00AAF2',
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#00152B',
              },
            },

            axisTick: {
              show: false,
            },
            // splitLine:{
            //   show:true,
            //   lineStyle:{
            //     color:'#195384'
            //   }
            // },
            data: [0,2,4,6,8,10,12,14,16],
          },
        ],
        yAxis: {
          name: "kw-h",
          nameTextStyle: {
            color: '#00AAF2',
            padding: [10, 20, 0, -40],
          },
          splitLine: {
            show: false,
          },
          axisLabel: {
            formatter: "{value}",
            textStyle: {
              color: '#00AAF2',
              fontSize: 10,
            },
          },
          axisLine: {
            show: false,
            // lineStyle:{
            //   color:'#0a88bd'
            // }
          },
          splitLine: {
            show: true,
            lineStyle: {
              //分割线的样式
              color: ["rgba(0,0,0,0.3)"],
              width: 1,
              type: "dashed",
            },
          },
          // offset:20,
        },
        series: [
          {
            type: "bar",
            barWidth: 12, //柱图宽度
            itemStyle: {
              normal: {
                barBorderRadius: [6, 6, 0, 0],
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#499eff", // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#838eff", // 100% 处的颜色
                    },
                  ],
                  globalCoord: true, // 缺省为 false
                },
              },
            },
            data: [180,150,180,240,230,260,220,280,270],
          },
        ],
      };
      traffic.setOption(option);
      window.addEventListener("resize", function () {
        traffic.resize();
      });
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
    handleOK(){
        this.$emit("dialogClose");
      },
  },
};
</script>
  <style lang="scss" scoped>
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.tabRobot {
  margin-top: 10px;
  >.el-radio-button:nth-of-type(1){
    margin-left: 15px;
  }
}
::v-deep .el-radio-button--medium .el-radio-button__inner {
  padding: 5px 10px !important;
  background: transparent;
  border: 1px solid transparent;
  // color: #fff;
}
::v-deep .el-radio-group > .is-active {
  background: #00aaf2 !important;
  border-radius: 20px !important;
}
::v-deep .el-radio-button {
  margin: 0 5px;
}
#traffic{
    width: 90%;
  height: 150px;
  background: #fff;
  margin-left: 5%;
  div {
    width: 100%;
    height: 150px !important;
  }
}
</style>