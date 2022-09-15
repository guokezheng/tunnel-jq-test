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
            <el-form-item label="设备状态:">
              <!-- {{ stateForm.eqStatus }} -->
              {{ "在线" }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="CO值:">
              <!-- {{ stateForm.deptName }} -->
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="VI值:">
              <!-- {{ stateForm.brandName }} -->
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
      </el-form>
      <el-radio-group v-model="tab" style="margin-bottom: 10px" class="comCovi">
        <el-radio-button label="co">CO实时趋势</el-radio-button>
        <el-radio-button label="vi">VI实时趋势</el-radio-button>
      </el-radio-group>
      <div id="co" v-show="tab == 'co'" style="margin-bottom: 10px"></div>
      <div id="vi" v-show="tab == 'vi'" style="margin-bottom: 10px"></div>
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
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
export default {
  props: ["equipmentId", "brandList", "directionList"],
  watch: {
    tab: {
      handler(newValue, oldValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          // this.mychart.dispose()
          this.$nextTick(() => {
            this.initChart(newValue);
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
      tab: "co",
      mychart: null,
    };
  },
  created() {
    console.log(this.equipmentId, "equipmentIdequipmentId");
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
      if (this.equipmentId) {
        var obj = {};
        var state = "";
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.equipmentId).then((res) => {
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
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    // 获取图表数据信息
    initChart(val) {
      console.log(val);
      var lincolor = [];
      var yName = "";
      if (val) {
        if (val == "vi") {
          lincolor = ["#00AAF2", "#8DEDFF", "#E3FAFF"];
          yName = "VI/KM";
        } else {
          lincolor = ["#FC61AB", "#FFA9D1", "#FFE3F0"];
          yName = "CO/PPM";
        }
      } else {
        lincolor = ["#FC61AB", "#FFA9D1", "#FFE3F0"];
        yName = "CO/PPM";
      }

      console.log(yName, "yName");
      var XData = [0, 2, 4, 6, 8, 10, 12, 14, 16, 18];
      var YData = [1000, 1200, 1250, 1350, 1439, 1446, 1235, 1256, 1363, 1153];
      console.log(
        document.getElementById(this.tab),
        "document.getElementById(this.tab)"
      );
      // this.mychart.dispose()
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
    },
    getDirection(num) {
      // debugger
      // console.log(num,"num")
      // console.log(this.directionList,"this.directionList");
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
#co,
#vi {
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
