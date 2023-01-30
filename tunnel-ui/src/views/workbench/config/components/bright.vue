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
        label-width="80px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
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
        </el-row>
        <el-row>
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
        </el-row>
        <el-row>
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
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备状态:">
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
      <el-radio-group
        v-model="tab"
        style="margin-bottom: 10px; margin-left: 10px"
        class="comCovi"
      >
        <el-radio-button label="Inside" v-if="this.eqInfo.clickEqType == 18"
          >洞内亮度</el-radio-button
        >
        <el-radio-button label="Inside" v-if="this.eqInfo.clickEqType == 5"
          >洞外亮度</el-radio-button
        >
      </el-radio-group>
      <div id="Inside" style="margin-bottom: 10px"></div>
      <div slot="footer">
        <!-- <el-button
          type="primary"
          size="mini"
          @click="handleClosee()"
          style="width: 80px"
          class="submitButton"
          >确 定</el-button
        > -->
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
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗数据信息
import { getTodayLDData } from "@/api/workbench/config.js"; //查询弹窗图表信息

export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
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
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      tab: "Inside",
      nowData: "",
    };
  },
  created() {
    this.getMessage();
    this.getChartMes();
    console.log(this.eqInfo,"eqInfo")
  },
  methods: {
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
        if(response.data.nowData){
          this.nowData = parseFloat(response.data.nowData).toFixed(2)
        }
        var xData = [];
        var yData = [];

        if (this.eqInfo.clickEqType == 5) {
          for (var item of response.data.todayLDOutsideData) {
            xData.push(item.order_hour);
            yData.push(item.count);
          }
        } else if (this.eqInfo.clickEqType == 18) {
          for (var item of response.data.todayLDInsideData) {
            xData.push(item.order_hour);
            yData.push(item.count);

          }
        }
        this.brightValue = yData[yData.length - 1];
       console.log(xData,"xData")
       console.log(yData,"yData")

        this.$nextTick(() => {
          this.initChart(xData, yData);
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
      this.$emit("dialogClose");
    },
    initChart(xData, yData) {
      // var lincolor = [];
      // var XData = [];
      // var YData = [];

      // if (this.tab == "Inside") {
      // XData = inXdata;
      // YData = inYdata;
      // lincolor = ["#00AAF2", "#8DEDFF", "#E3FAFF"];
      // } else {
      //   XData = outXdata;
      //   YData = outYdata;
      //   lincolor = ["#FC61AB", "#FFA9D1", "#FFE3F0"];
      // }

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
            data: yData,
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
  width: 90%;
  height: 150px;
  background: #fff;
  margin-left: 5%;
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
</style>
