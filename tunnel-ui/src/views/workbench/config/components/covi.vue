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
        label-width="90px"
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
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="CO值:">
              {{ COnowData
              }}<span style="padding-left: 5px" v-if="COnowData">PPM</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="VI值:">
              {{ VInowData
              }}<span style="padding-left: 5px" v-if="VInowData">M</span>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
      </el-form>
      <el-radio-group v-model="tab" style="margin: 10px 0" class="comCovi">
        <el-radio-button label="co">CO实时趋势</el-radio-button>
        <el-radio-button label="vi">VI实时趋势</el-radio-button>
      </el-radio-group>
      <div id="co" v-show="tab == 'co'" style="margin-bottom: 10px"></div>
      <div id="vi" v-show="tab == 'vi'" style="margin-bottom: 10px"></div>
      <div slot="footer" class="dialog-footer">
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getTodayCOVIData } from "@/api/workbench/config.js"; //查询弹窗信息

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
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      stateForm: {}, //弹窗表单
      title: "",
      visible: false,
      tab: "co",
      mychart: null,
      COnowData: "",
      VInowData: "",
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
    };
  },
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.tab = "co";
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
      getTodayCOVIData(this.eqInfo.equipmentId).then((response) => {
        console.log(response, "covi数据");
        if (response.data.COnowData) {
          this.COnowData = parseFloat(response.data.COnowData).toFixed(2);
        }
        if (response.data.VInowData) {
          this.VInowData = parseFloat(response.data.VInowData).toFixed(2);
        }
        var coXdata = [];
        var coYdata = [];
        var viXdata = [];
        var viYdata = [];

        for (var item of response.data.todayCOData) {
          coXdata.push(item.order_hour);
          coYdata.push(item.count);
        }
        for (var item of response.data.todayVIData) {
          viXdata.push(item.order_hour);
          viYdata.push(item.count);
        }
        this.$nextTick(() => {
          this.initChart(coXdata, coYdata, viXdata, viYdata);
        });
      });
    },
    // 获取图表数据信息
    initChart(coXdata, coYdata, viXdata, viYdata) {
      var lincolor = [];
      var yName = "";
      var XData = [];
      var YData = [];

      if (this.tab == "vi") {
        XData = viXdata;
        YData = viYdata;
        lincolor = ["#00AAF2", "#8DEDFF", "#E3FAFF"];
        yName = "VI/M";
      } else {
        XData = coXdata;
        YData = coYdata;
        lincolor = ["#FC61AB", "#FFA9D1", "#FFE3F0"];
        yName = "CO/PPM";
      }

      // 新建一个promise对象
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        //	此dom为echarts图标展示dom
        this.mychart = echarts.init(document.getElementById(this.tab));
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
            data: XData,
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
            name: yName,
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
            },
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
              color: lincolor[0],
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
                      color: lincolor[1],
                    },
                    {
                      offset: 1,
                      color: lincolor[2],
                    },
                  ]),
                },
              },
              data: YData,
            },
          ],
        };
        this.mychart.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart.resize();
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
      if (num) {
        for (var item of this.brandList) {
          if (Number(item.dictValue) == num) {
            return item.dictLabel;
          }
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
    // 提交修改
    handleOK() {
      this.visible = false;
    },
  },
};
</script>

<style  lang="scss" scoped>
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
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
#co,
#vi {
  width: 100%;
  height: 150px;
  background: #fff;
  div {
    width: 100%;
    height: 150px !important;
  }
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
