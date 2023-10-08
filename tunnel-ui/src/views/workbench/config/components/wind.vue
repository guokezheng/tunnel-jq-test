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
          <el-col :span="12">
            <el-form-item label="设备类型:">
              {{ stateForm.typeName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属方向:">
              {{ getDirection(stateForm.eqDirection) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <!-- <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col> -->
          <el-col :span="12" v-show="ipShow">
            <el-form-item label="控制器IP:" >
              {{ stateForm.f_ip }}
            </el-form-item>
          </el-col>
          
          <el-col :span="12" v-show="!ipShow">
            <el-form-item label="控制器IP:" >
              {{ stateForm.ip }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          <el-col :span="12" v-show="!ipShow">
            <el-form-item label="plcIP:" >
              {{ stateForm.f_ip }}
            </el-form-item>
          </el-col>
          
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="12">
            <el-form-item label="风速:">
              {{ nowData }}
              <span style="padding-left: 5px" v-if="nowData">m/s</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风向:">
              {{ fengDirection }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
      </el-form>
      <el-radio-group v-model="tab" style="margin: 0 0 10px">
        <el-radio-button label="co">风速风向实时趋势</el-radio-button>
      </el-radio-group>
      <div id="feng" style="margin-bottom: 10px"></div>
      <div class="dialog-footer">
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

  <script>
import * as echarts from "echarts";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗数据信息
import { getTodayFSFXData } from "@/api/workbench/config.js"; //查询弹窗图表信息

export default {
  data() {
    return {
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      stateForm: {}, //弹窗表单
      title: "",
      visible: false,
      tab: "co",
      fengValue: "",
      fengDirection: "",
      nowData: "",
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
      ipShow:false,
    };
  },
  mounted() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getMessage();
      this.visible = true;
    },
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          if(this.stateForm.tunnelId == 'JQ-JiNan-WenZuBei-MJY' || this.stateForm.tunnelId == 'JQ-WeiFang-JiuLongYu-HSD'){
            this.ipShow = true
          }else{
            this.ipShow = false
          }
        });
        await getTodayFSFXData(this.eqInfo.equipmentId).then((response) => {
          console.log(response, "风速风向数据");
          if (response.data.nowData) {
            this.nowData = parseFloat(response.data.nowData).toFixed(2);
          }

          var xData = [];
          var yData = [];
          for (var item of response.data.todayFSData) {
            xData.push(item.order_hour);
            yData.push(parseFloat(item.count).toFixed(2));
          }
          // this.fengValue = yData[yData.length-1]
          this.fengDirection = response.data.windDirection;
          this.$nextTick(() => {
            this.initChart(xData, yData);
          });
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    // 获取图表数据信息
    initChart(xData, yData) {
      var mychart = echarts.init(document.getElementById("feng"));

      var option = {
        tooltip: {
          trigger: "axis",
          formatter: function (params) {
            var res = params[0].name;
            res +=
              "<br/>" + params[0].seriesName + ":" + params[0].value + "m/s";
            return res;
          },
        },
        grid: {
          left: "10%",
          right: "12%",
          bottom: "10%",
          top: "24%",
          containLabel: true,
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
              color: "#00AAF2",
            },
          },
        },
        yAxis: {
          name: "m/s",
          type: "value",
          minInterval: 1, //y轴的刻度只显示整数

          nameTextStyle: {
            color: "#FFB500",
            fontSize: 10,
            padding: [0, 20, 0, 0],
          },
          axisLabel: {
            textStyle: {
              color: "#00AAF2",
              fontSize: 10,
            },
          },
        },
        lineStyle: {
          normal: {
            color: "#ecc47e",
          },
        },
        itemStyle: {
          normal: {
            color: "#ecc47e",
          },
        },
        series: [
          {
            name: "风向风速",
            type: "line",
            color: "#FFBD49",
            // 方向箭头----------------
            // symbol: "arrow",
            // symbolOffset: [0, -3.5],
            // symbolSize: 8,
            // -----------------
            smooth: true, //让曲线变平滑
            // data: windSpeedList,
            data: yData,
            // 转折点为圆点 ------------
            symbol: "circle",
            symbolSize: [7, 7],
            // ------------------
            itemStyle: {
              normal: {
                borderColor: "white",
                areaStyle: {
                  type: "default",
                  //渐变色实现
                  color: new echarts.graphic.LinearGradient(
                    0,
                    0,
                    0,
                    1, //变化度
                    //三种由深及浅的颜色
                    [
                      {
                        offset: 0,
                        color: "#ecc47e",
                      },
                      {
                        offset: 0.5,
                        color: "#ecdbb0",
                      },
                      {
                        offset: 1,
                        color: "#eaecde",
                      },
                    ]
                  ),
                },
              },
            },
            areaStyle: {},
          },
        ],
      };
      mychart.setOption(option); //echarts

      window.addEventListener("resize", function () {
        mychart.resize();
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
  },
};
</script>

  <style  lang="scss" scoped>
::v-deep .el-radio-button--medium .el-radio-button__inner {
  padding: 5px 10px !important;
}
::v-deep .el-radio-button--medium .el-radio-button__inner {
  border-radius: 20px !important;
}
::v-deep .el-radio-button {
  margin: 0 15px;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
#feng {
  width: 100%;
  height: 200px;
  background: #fff;
  div {
    width: 100%;
    height: 200px !important;
  }
}
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background: #00aaf2 !important;
  border-radius: 20px !important;
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
