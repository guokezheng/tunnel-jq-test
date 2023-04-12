<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog vehicle-dialog"
      :title="title"
      width="450px"
      append-to-body
      :visible="visible"
      :before-close="handleClosee"
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
            <el-form-item label="设备状态:"
            :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
              {{ geteqType(stateForm.eqStatus) }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
      </el-form>
      <el-radio-group v-model="tab" style="margin: 10px 0" class="comCovi">
        <el-radio-button label="data">车流量实时数据</el-radio-button>
        <el-radio-button label="trend">车流量实时趋势</el-radio-button>
      </el-radio-group>
      <div v-show="tab == 'data'" style="margin-bottom: 10px">
        <el-table :data="dataList" min-height="150" empty-text="暂无操作日志" >
          <el-table-column label="车道" align="center" prop="laneNo" width="76" >
            <template slot-scope="scope">
              <span>第{{scope.row.laneNo}}车道</span>
            </template>
          </el-table-column>
          <el-table-column
            label="车流量(辆/分钟)"
            align="center"
            prop="trafficFlowTotal"
            width="80"
          />
          <el-table-column
            label="平均车速"
            align="center"
            prop="avgSpeed"
            width="80"
          >
            <template slot-scope="scope">
              <span>{{scope.row.avgSpeed}}km/h</span>
            </template>
          </el-table-column>
          <el-table-column
            label="占有率"
            align="center"
            prop="avgOccupancy"
            width="64"
          >
            <template slot-scope="scope">
              <span>{{scope.row.avgOccupancy}}%</span>
            </template>
          </el-table-column>
          <el-table-column label="上传时间" align="center" prop="createTime" />
        </el-table>
      </div>
      <div id="trend" v-show="tab == 'trend'" style="margin-bottom: 10px"></div>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button
          type="primary"
          size="mini"
          @click="handleOK()"
          style="width: 80px"
          class="submitButton"
          >确 定</el-button
        > -->
        <el-button
          class="closeButton"
          @click="handleClosee()"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

  <script>
import * as echarts from "echarts";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getTodayCOVIData, getStatisticsNewList, getStatisticsRealList } from "@/api/workbench/config.js"; //查询弹窗信息
import { ConsoleWriter } from "istanbul-lib-report";

export default {
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  watch: {
    tab: {
      handler(newValue, oldValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          // this.mychart.dispose()
          this.$nextTick(() => {
            this.initChart();
          });
        }
      },
    },
  },
  data() {
    return {
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      stateForm: {}, //弹窗表单
      title: "",
      visible: true,
      tab: "data",
      mychart: null,
      dataList: [],
      XData:[],
      yData1:[],
      yData2:[],
      yData3:[]
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    this.getMessage();
  },
  mounted() {

  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.eqInfo.equipmentId) {
        var obj = {};
        var state = "";
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          obj = res.data;

          getTodayCOVIData(this.eqInfo.equipmentId).then((response) => {
            console.log(response, "covi数据");
          });
          this.title = obj.eqName;
          this.stateForm = obj
          console.log(this.stateForm, "stateForm");
          this.getMess()
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    getMess(){
      const params = {
        deviceId:this.eqInfo.equipmentId,
        eqDirection:this.stateForm.eqDirection
      }
      getStatisticsNewList(params).then((res)=>{
        console.log(res,"微波车检表格")
        this.dataList = res.data
      })
      getStatisticsRealList(params).then((res)=>{
        console.log(res,"微波车检 echarts")
        for(let item of res.data.laneNoOne){
          this.XData.push(item.order_hour)
          this.yData1.push(item.avgSpeed)
        }
        for(let item of res.data.laneNoTwo){
          this.yData2.push(item.avgSpeed)
        }
        for(let item of res.data.laneNoThree){
          this.yData3.push(item.avgSpeed)
        }
        setTimeout(()=>{
          this.$nextTick(() => {
            this.initChart();
          });
        },500)

      })
    },
    // 获取图表数据信息
    initChart() {
      this.mychart = echarts.init(document.getElementById(this.tab));
      var option = {
        tooltip: {
          trigger: "axis",
        },
        legend: {
          show: true,
          icon: "roundRect",
          itemWidth: 14,
          itemHeight: 8,
          x: "center",
          data: ["1车道", "2车道", "3车道"],
          textStyle: {
            //图例文字的样式
            color: "#00AAF2",
            fontSize: 12,
          },
          top: "20",
        },
        grid: {
          top: "30%",
          bottom: "18%",
          left: "14%",
          right: "12%",
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: this.XData,
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
          name: "辆",
          nameTextStyle: {
            color: "#FFB500",
            fontSize: 10,
            // padding: [0, 20, 0, 0],
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
            name: "1车道",
            type: "line",
            color: "#787FFE",
            symbol: "circle",
            symbolSize: [7, 7],
            itemStyle: {
              normal: {
                borderColor: "white",
              },
            },
            smooth: true,
            data:this.yData1,
          },
          {
            name: "2车道",
            type: "line",
            color: "#00DCA2",
            symbol: "circle",
            symbolSize: [7, 7],
            itemStyle: {
              normal: {
                borderColor: "white",
              },
            },
            smooth: true,
            data:this.yData2,
          },
          {
            name: "3车道",
            type: "line",
            color: "#FFB200",
            symbol: "circle",
            symbolSize: [7, 7],
            itemStyle: {
              normal: {
                borderColor: "white",
              },
            },
            smooth: true,
            data:this.yData3,
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
      this.$emit("dialogClose");
    },
    handleOK() {
      this.$emit("dialogClose");
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
// ::v-deep .el-radio-button--medium .el-radio-button__inner {

// }
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
#trend {
  width: 100%;
  height: 200px;
  background: #fff;
  div {
    width: 100%;
    height: 200px !important;
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
::v-deep .el-table {
  width: 100%;

  .el-table__header-wrapper {
    line-height: 28px;
    .cell {
      line-height: 14px;
    }
    // th {
    //   background: #00adff !important;
    // }
  }
  .el-table__body {
    width: 100% !important;
  }
  .el-table__body-wrapper {
    .cell {
      line-height: 14px;
    }
  }
  .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: #4395c1 !important;
    color: black;
    /* color: #f19944; */ /* 设置文字颜色，可以选择不设置 */
  }
}

/* 删除表格下横线 */
::v-deep .el-table::before {
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
}
// .el-table__body-wrapper::-webkit-scrollbar {
//       width: 10px;
//       height: 10px;
//       // background-color: #304156;
//     }
</style>
