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
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
      >
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备类型:">
              {{ stateForm.typeName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="所属方向:">
              {{ getDirection(stateForm.eqDirection) }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item
              label="设备状态:"
              :style="{
                color:
                  stateForm.eqStatus == '1'
                    ? 'yellowgreen'
                    : stateForm.eqStatus == '2'
                    ? 'white'
                    : 'red',
              }"
            >
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
          <el-col :span="11" v-if="this.eqInfo.clickEqType == 5">
            <el-form-item label="洞外亮度:">
              {{ nowData }}
              <span style="padding-left: 10px" v-if="nowData">lux</span>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-if="this.eqInfo.clickEqType == 18">
            <el-form-item label="洞内亮度:">
              {{ nowData }}
              <span style="padding-left: 10px" v-if="nowData">lux</span>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="lineClass"></div>
      </el-form>
      <el-radio-group v-model="tab" style="margin: 10px 0" class="comCovi">
        <el-radio-button label="Inside" v-if="this.eqInfo.clickEqType == 18"
          >洞内亮度</el-radio-button
        >
        <el-radio-button label="Inside" v-if="this.eqInfo.clickEqType == 5"
          >洞外亮度</el-radio-button
        >
      </el-radio-group>
      <div id="Inside" style="margin-bottom: 10px"></div>
      <div slot="footer" class="dialog-footer">
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import * as echarts from "echarts";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗数据信息
import { getTodayLDData } from "@/api/workbench/config.js"; //查询弹窗图表信息

export default {
  watch: {
    tab: {
      handler(newValue, oldValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          this.getChartMes(newValue);
        }
      },
    },
  },
  data() {
    return {
      stateForm: {},
      title: "",
      visible: false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      tab: "Inside",
      nowData: "",
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
    };
  },
  created() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getMessage();
      this.visible = true;
      this.getChartMes();
    },
    // 查设备详情
    async getMessage() {
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    getChartMes() {
      getTodayLDData(this.eqInfo.equipmentId).then((response) => {
        console.log(response, "亮度检测器数据");
        if (response.data.nowData) {
          this.nowData = parseFloat(response.data.nowData).toFixed(2);
        }
        var xData = [];
        var yData1 = [];
        var yData2 = [];

        if (this.eqInfo.clickEqType == 5) {
          for (var item of response.data.todayLDOutsideData) {
            xData.push(item.order_hour);
            yData1.push(item.count);
            yData2.push(item.ctCount);
          }
        } else if (this.eqInfo.clickEqType == 18) {
          for (var item of response.data.todayLDInsideData) {
            xData.push(item.order_hour);
            yData1.push(item.count);
          }
        }
        this.brightValue = yData1[yData1.length - 1];
        console.log(xData, "xData");
        console.log(yData1, "yData1");
        console.log(yData2, "yData2");

        this.$nextTick(() => {
          this.initChart(xData, yData1, yData2);
        });
      });
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
    // 关闭弹窗
    handleClosee() {
      this.visible = false;
    },
    initChart(xData, yData1, yData2) {
      this.mychart = echarts.init(document.getElementById("Inside"));
      var option = {
        tooltip: {
          trigger: "axis",
        },
        toolbox: {
          show: true,
          feature: {
            // magicType: { show: true, type: ['stack', 'tiled'] },
            // saveAsImage: { show: true }
          },
        },
        legend: {
          show: true,
          data: this.eqInfo.clickEqType == 18?[]:["当前隧道", "传统隧道"],
          textStyle: {
            color: "#AFAFAF",
            fontSize: 10,
          },
          itemWidth: 10,
          itemHeight: 10,
          itemStyle: {},
          top: "top",
          left: "center",
          padding: [6, 15, 0, 15],
          icon: "circle",
          orient: "horizontal",
        },
        grid: {
          top: "24%",
          bottom: "18%",
          left: "14%",
          right: "12%",
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: xData,
          axisLabel: {
            textStyle: {
              color: "#00AAF2",
              fontSize: 10,
            },
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: "#386D88",
            },
          },
        },
        yAxis: {
          type: "value",
          name: "lux",
          nameTextStyle: {
            color: "#FFB500",
            fontSize: 10,
            padding: [0, 30, 0, 0],
          },
          // minInterval: 1, //y轴的刻度只显示整数
          axisLabel: {
            textStyle: {
              color: "#00AAF2",
              fontSize: 10,
            },
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
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
        },
        series: [
          {
            name: "当前隧道",
            type: "line",
            color: "#00AAF2",
            symbol: "none",
            smooth: true,
            stack: "Total",
            areaStyle: {},
            symbol: "circle",
            symbolSize: [7, 7],
            itemStyle: {
              normal: {
                borderColor: "white",
              },
            },
            emphasis: {
              focus: "series",
            },
            //渐变色
            areaStyle: {
              normal: {
                //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: "#8DEDFF",
                  },
                  {
                    offset: 1,
                    color: "#E3FAFF",
                  },
                ]),
              },
            },
            data: yData1,
          },
          {
              type: "line",
              smooth: true, // 平滑曲线显示
              color: "#FAC858",
              lineStyle: {
                width: 1,
              },
              // stack: "Total",
              // areaStyle: {},
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              // emphasis: {
              //   focus: "series",
              // },
              name: "传统隧道",
              data: yData2,
            },
        ],
      };

      this.mychart.setOption(option);
      window.addEventListener("resize", function () {
        this.mychart.resize();
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.tunnelDialogButton {
  width: 100px;
  height: 26px;
  border-radius: 13px;
  background-color: #00aaf2;
  color: #fff;
  text-align: center;
  line-height: 26px;
  margin: 15px 0 10px 15px;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
#Outside,
#Inside {
  width: 100%;
  height: 150px;
  background: #fff;
  div {
    width: 100%;
    height: 150px !important;
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
  margin: 0 10px;
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
