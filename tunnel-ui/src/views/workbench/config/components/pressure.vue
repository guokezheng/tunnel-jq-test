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
                {{ getBrandName(stateForm.brandName) }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="13">
              <el-form-item label="设备状态:">
                <!-- {{ stateForm.eqStatus }} -->
                {{ geteqType(stateForm.eqStatus) }}
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="当前压力值:">
                {{ nowData }}
              </el-form-item>
            </el-col>
          </el-row>
        <div class="lineClass"></div>
        <el-radio-group
          v-model="tab"
          style="margin-bottom: 10px; margin-top: 10px"
        >
          <el-radio-button label="yali">压力表实时趋势</el-radio-button>
        </el-radio-group>
        <div id="yaliCharts" style="margin: 0px auto"></div>
        </el-form>
        <div slot="footer">
        <!-- <el-button
          type="primary"
          size="mini"
          @click="handleOK()"
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
  import { getDeviceById, getTodayYcylData } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息
  import * as echarts from "echarts";
  
  export default {
    props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
    data() {
      return {
        stateForm: {},
        title: "",
        visible: true,
        titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
        nowData:'',
        mychart:null,
        tab:'yali',
      };
    },
    created() {
      this.getMessage();
    },
    methods: {
      // 查设备详情
      async getMessage() {
        var that = this;
        if (this.eqInfo.equipmentId) {
          var obj = {};
          // 查询单选框弹窗信息 -----------------------
          await getDeviceById(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "查询单选框弹窗信息");
            this.stateForm = res.data;
            this.title = res.data.eqName;
            console.log(this.stateForm, "stateForm");
          });
          await getTodayYcylData(this.eqInfo.equipmentId).then((res) => {
            console.log(res,"压力表折线图数据")
            this.nowData = res.data.nowData
            let xData = []
            let yData = []
            for(var item of res.data.todayYcylData){
              xData.push(item.order_hour)
              yData.push(item.count)

            }
            // console.log(xData,yData,"压力表echarts数据");
            this.initChart(xData,yData)
          })
        } else {
          this.$modal.msgWarning("没有设备Id");
        }
      },
      initChart(xData, yData) {
     console.log(xData, yData)
      this.mychart = echarts.init(document.getElementById("yaliCharts"));
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
          name: "Mpa",
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
      handleOK(){
        this.$emit("dialogClose");
      },
      // 关闭弹窗
      handleClosee() {
        this.$emit("dialogClose");
      },
    },
  };
  </script>
  <style lang="scss" scoped>
    .tunnelDialogButton{
        width: 100px;
        height: 26px;
        border-radius: 13px;
        background-color: #00AAF2;
        color:#fff;
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
// ::v-deep .el-radio-button {
//   margin: 0 15px;
// }
#yaliCharts {
  width: 100%;
  height: 150px;
  background: #fff;
  margin-left: 5%;
  div {
    width: 100%;
    height: 150px !important;
  }
}
  </style>
  