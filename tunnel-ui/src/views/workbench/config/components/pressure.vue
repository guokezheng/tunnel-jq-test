<template>
  <div style="width: 100%; height: 100%">
    <el-dialog v-dialogDrag class="workbench-dialog" :title="title" width="450px" append-to-body :visible="visible"
      :before-close="handleClosee" :close-on-click-modal="false" :modal="false">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="stateForm" label-width="80px" label-position="left" size="mini">
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
          <!-- <el-col :span="12">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col> -->
          <el-col :span="12" v-show="ipShow">
            <el-form-item label="控制器IP:">
              {{ stateForm.f_ip }}
            </el-form-item>
          </el-col>

          <el-col :span="12" v-show="!ipShow">
            <el-form-item label="控制器IP:">
              {{ stateForm.ip }}
            </el-form-item>
          </el-col>
          <el-col :span="12" v-show="!ipShow">
            <el-form-item label="plcIP:">
              {{ stateForm.f_ip }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备状态:" :style="{
                color:
                  stateForm.eqStatus == '1'
                    ? 'yellowgreen'
                    : stateForm.eqStatus == '2'
                    ? 'white'
                    : 'red',
              }">
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前压力:">
              {{ nowData }}
              <span v-show="nowData">Mpa</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="lineClass"></div>
      <el-radio-group v-model="tab" style="margin-bottom: 10px; margin-top: 10px">
        <el-radio-button label="yali">压力表实时趋势</el-radio-button>
      </el-radio-group>
      <div id="yaliCharts" style="margin-bottom: 10px"></div>

      <div class="dialog-footer">
        <el-button class="closeButton" @click="handleClosee()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getDeviceById,
    getTodayYcylData
  } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
  import * as echarts from "echarts";

  export default {
    data() {
      return {
        stateForm: {},
        title: "",
        visible: false,
        titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
        nowData: "",
        mychart: null,
        tab: "yali",
        brandList: [],
        eqInfo: {},
        eqTypeDialogList: [],
        directionList: [],
        ipShow: false,
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
      },
      // 查设备详情
      async getMessage() {
        var that = this;
        if (this.eqInfo.equipmentId) {
          // 查询单选框弹窗信息 -----------------------
          await getDeviceById(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "查询单选框弹窗信息");
            this.stateForm = res.data;
            this.title = res.data.eqName;
            if (this.stateForm.tunnelId == 'JQ-JiNan-WenZuBei-MJY' || this.stateForm.tunnelId ==
              'JQ-WeiFang-JiuLongYu-HSD') {
              this.ipShow = true
            } else {
              this.ipShow = false
            }
          });
          await getTodayYcylData(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "压力表折线图数据");
            this.nowData = res.data.nowData;
            let xData = [];
            let yData = [];
            for (var item of res.data.todayYcylData) {
              xData.push(item.order_hour);
              yData.push(parseFloat(item.count).toFixed(2));
            }
            this.initChart(xData, yData);
          });
        } else {
          this.$modal.msgWarning("没有设备Id");
        }
      },
      initChart(xData, yData) {
        console.log(xData, yData);
        this.mychart = echarts.init(document.getElementById("yaliCharts"));
        var option = {
          tooltip: {
            trigger: "axis",
            formatter: function (params) {
              var str = params[0].marker;
              str += params[0].value;
              return str;
            }
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
            name: "Mpa",
            nameTextStyle: {
              color: "#FFB500",
              fontSize: 10,
              padding: [0, 30, 0, 0],
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
          series: [{
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
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
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
          }, ],
        };

        this.mychart.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart.resize();
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
      handleOK() {
        this.$emit("dialogClose");
      },
      // 关闭弹窗
      handleClosee() {
        this.visible = false;
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
    margin-top: 15px;
    margin-bottom: 10px;
  }

  .el-row {
    margin-bottom: -10px;
    display: flex;
    flex-wrap: wrap;
  }

  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 5px 10px !important;
  }

  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    border-radius: 20px !important;
  }

  #yaliCharts {
    width: 100%;
    height: 200px;
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
