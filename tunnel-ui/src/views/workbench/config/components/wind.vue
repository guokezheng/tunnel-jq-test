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
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="13" >
            <el-form-item label="风速:" >
             
                {{ nowData }}
                <span style="padding-left:5px"  v-if="nowData">m/s</span>
             
              
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="风向:">
              {{ fengDirection }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-radio-group
        v-model="tab"
        style="margin-bottom: 10px; margin-top: -10px"
      >
        <el-radio-button label="co">风速风向实时趋势</el-radio-button>
      </el-radio-group>
      <div id="feng" style="margin-bottom: 10px"></div>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          @click="handleClosee()"
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
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗数据信息
import { getTodayFSFXData } from "@/api/workbench/config.js"; //查询弹窗图表信息

export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
  data() {
    return {
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      stateForm: {}, //弹窗表单
      title: "",
      visible: true,
      tab: "co",
      fengValue:'',
      fengDirection:'',
      nowData:''
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    this.getMessage();
  },
  mounted() {
    // this.$nextTick(() => {
    //   this.initChart();
    // });
  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.eqInfo.equipmentId) {
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
        });
        await getTodayFSFXData(this.eqInfo.equipmentId).then((response) => {
          console.log(response, "风速风向数据");
          if(response.data.nowData){
            this.nowData = parseFloat(response.data.nowData).toFixed(2)

          }
          
          var xData = [];
          var yData = [];
          for (var item of response.data.todayFSData) {
            xData.push(item.order_hour);
            yData.push(item.count);
          }
          // this.fengValue = yData[yData.length-1]
          this.fengDirection = response.data.windDirection
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
      // var data = [
      //   [0, 1, 0],
      //   [280, 2, 2],
      //   [260, 1, 4],
      //   [290, 3, 6],
      //   [240, 2, 8],
      //   [270, 3, 10],
      //   [0, 1, 12],
      //   [280, 2, 14],
      //   [260, 1, 16],
      //   [290, 3, 18],
      //   [240, 2, 20],
      //   [270, 3, 22],
      // ];
      // var dateTime = [];
      // var windSpeedList = [];
      // var obj;
      // console.log(data);
      //   for (var i = 0; i < data.length; i++) {
      //     var item = data[i];
      //     obj = {
      //       value: item[1],
      //       symbolRotate: 180 - item[0],
      //     };
      //     windSpeedList.push(obj);
      //     dateTime.push(item[2]);
      //   }
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
        // dataZoom: [
        //   {
        //     type: "inside", //鼠标滑动缩放
        //     realtime: false,
        //     start: 30,
        //     end: 70,
        //   },
        // ],
        grid: {
          left: "10%",
          right: "12%",
          bottom: "10%",
          top:"24%",
          containLabel: true,
        },

        xAxis: {
          // name: "时间",
          type: "category",
          boundaryGap: true,
          // data: dateTime,
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
            smooth: true, //这句就是让曲线变平滑的
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
      this.$emit("dialogClose");
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
#feng {
  width: calc(100% - 30px);
  height: 150px;
  background: #fff;
  margin-left: 15px;
  div {
    width: 100%;
    height: 150px !important;
  }
}
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background: #00aaf2 !important;
  border-radius: 20px !important;
}
</style>
  