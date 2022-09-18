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
                {{geteqType(stateForm.eqStatus) }}
              </el-form-item>
            </el-col>
          </el-row>
          <div class="lineClass"></div>
        </el-form>
        <el-radio-group v-model="tab" style="margin-bottom: 10px" class="comCovi">
          <el-radio-button label="data">车流量实时数据</el-radio-button>
          <el-radio-button label="trend">车流量实时趋势</el-radio-button>
        </el-radio-group>
        <div v-show="tab == 'data'" style="margin-bottom: 10px">
            <el-table :data="dataList" min-height="150"  empty-text="暂无操作日志" >
                <el-table-column label="车道" align="center" prop="lane" width="76"/>
                <el-table-column label="车流量(辆/分钟)" align="center" prop="trafficFlow" width="80"/>
                <el-table-column label="平均车速" align="center" prop="velocity" width="80"/>
                <el-table-column label="占有率" align="center" prop="occupancy" width="64"/>
                <el-table-column label="上传时间" align="center" prop="createTime" />
            </el-table>
        </div>
        <div id="trend" v-show="tab == 'trend'" style="margin-bottom: 10px"></div>
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
  import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
  import { getTodayCOVIData } from "@/api/workbench/config.js"; //查询弹窗信息
  
  
  export default {
    props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
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
        tab: "data",
        mychart: null,
        dataList:[
            {
                lane:'第一车道',
                trafficFlow:'47分钟',
                velocity:'74km/h',
                occupancy:'47%',
                createTime:'2022-09-07 14:17:00'
            },
            {
                lane:'第二车道',
                trafficFlow:'47分钟',
                velocity:'74km/h',
                occupancy:'47%',
                createTime:'2022-09-07 14:17:00'
            },
            {
                lane:'第三车道',
                trafficFlow:'47分钟',
                velocity:'74km/h',
                occupancy:'47%',
                createTime:'2022-09-07 14:17:00'
            },
            
        ]
      };
    },
    created() {
      console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
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
        if (this.eqInfo.equipmentId) {
          var obj = {};
          var state = "";
          // 查询单选框弹窗信息 -----------------------
          await getDeviceById(this.eqInfo.equipmentId).then((res) => {
            console.log(res, "查询单选框弹窗信息");
            obj = res.data;
            
            getTodayCOVIData(this.eqInfo.equipmentId).then((response) =>{
              console.log(response,"covi数据");
            })
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
            lincolor = ["#00AAF2", "#8DEDFF", "#E3FAFF"];
            yName = "VI/KM";
        } 
        
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
           legend: {
              show: true,
              icon: "roundRect",
              itemWidth: 14,
              itemHeight: 8,
              x: 'center',
              data: ['1车道', '2车道', '3车道'],
              textStyle: { //图例文字的样式
                color:'#00AAF2',
                fontSize: 12,
              },
              top:"20",
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
            name: 'KM',
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
          series: [{
              name: '1车道',
              type: 'line',
              color: '#787FFE',
              symbol: 'circle',
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white"
                }
              },
              smooth: true,
             
              data: [ 30, 80, 90, 70,80, 90, 80, 90, 70, 50,60, 70]
            },
            {
              name: '2车道',
              type: 'line',
              color: '#00DCA2',
              symbol: 'circle',
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white"
                }
              },
              smooth: true,
            //   stack: 'Total',
            //   areaStyle: {},
            //   emphasis: {
            //     focus: 'series'
            //   },
              
              data: [90, 70, 50, 60, 80, 90, 30, 60, 70, 80, 90, 80]
            }, {
              name: '3车道',
              type: 'line',
              color: '#FFB200',
              symbol: 'circle',
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white"
                }
              },
              smooth: true,
              data: [60, 50, 40, 50, 70, 80, 90, 60, 50, 40, 50, 60]
            },
             ]
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
      handleOK(){
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
  #trend{
    width: 90%;
    height: 200px;
    background: #fff;
    margin-left: 5%;
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
::v-deep .el-table{
    width:90%;
    margin-left: 5%;
    
    .el-table__header-wrapper{
        line-height: 28px;
        .cell{
            line-height:14px;
        }
        th{
            background: #00ADFF !important;
        }
    }
    .el-table__body{
        width: 100% !important;
    }
    .el-table__body-wrapper{
        .cell{
            line-height:14px;
        }
    }
    .el-table--enable-row-hover .el-table__body tr:hover>td {
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
  