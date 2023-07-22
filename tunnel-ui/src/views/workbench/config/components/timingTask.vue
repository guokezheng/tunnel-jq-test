<template>
  <!-- 定时任务-->
  <el-dialog
    class="explain-table operationDiglog"
    title="定时任务策略"
    :close-on-click-modal="false"
    :visible.sync="visibleSync"
    width="90%"
    :destroy-on-close="true"
    append-to-body
    :before-close="closeLogin"
  >
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
    <div>
      <div style="margin-left: 30%;margin-top: 15px ;font-size: 19px">
        <div style="float: left;">主策略</div>
        <div style="margin-left:30px ;float: left;">当前策略名称：{{titleHistory}}</div>
        <div style="margin-left:30px ;float: left;">时间：{{titleHistory1}}</div>
        <div style="margin-left:30px ;float: left;">下修比例：{{titleHistory2}}</div>
      </div>
      <div>
        <el-table
          :data="tableData"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column
            prop="date"
            label="执行时间"
            sortable
            width="180">
          </el-table-column>

          <el-table-column prop="name" label="策略名称">
            <template slot-scope="scope">
              <span v-if="editingRow === scope.row.id">
                <el-input v-model="scope.row.strategyName"></el-input>
              </span>
              <span v-else>{{ scope.row.strategyName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="隧道名称">
            <template slot-scope="scope">
              <span v-if="editingRow === scope.row.id">
                <el-select
                  style="width: 100%"
                  v-model="scope.row.tunnelId"
                  placeholder="请选择隧道"
                  clearable
                  @change="changeEvent(scope.row,'1')"
                >
                  <el-option
                    v-for="item in tunnelData"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </span>
              <span v-else>{{ scope.row.tunnelName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="direction" label="隧道方向">
            <template slot-scope="scope">
              <span v-if="editingRow === scope.row.id">
               <el-select
                 clearable
                 v-model="scope.row.direction"
                 placeholder="请选择隧道方向"
                 @change="changeEvent(scope.row,'2')"
                 style="width: 100%"
               >
              <el-option
                v-for="dict in directionOptions"
                :key="dict.value"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
              </span>
              <span v-else>{{ scope.row.dictLabel }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="direction" label="设备类型">
            <template slot-scope="scope">
              <span v-if="editingRow === scope.row.id">
                 <el-select
                   clearable
                   v-model="scope.row.equipmentTypeId"
                   placeholder="请选择设备类型"
                   @change="equipmenEvent(scope.row)"
                   style="width: 100%"
                 >
                  <el-option
                    v-for="dict in equipmentTypeData"
                    :key="dict.value"
                    :label="dict.keyName"
                    :value="dict.value"
                  />
                </el-select>
              </span>
              <span v-else>{{ scope.row.equipmentName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="address"
            label="设备资源类型">
          </el-table-column>
          <el-table-column
            prop="eqId"
            label="指定设备">
          </el-table-column>
          <el-table-column
            prop="controlInstruction"
            label="控制指令">
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleRowEdit(scope)"
              >编辑
              </el-button
              >
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleRowSave(scope)"
              >保存
              </el-button
              >
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="richanghandleUpdate(scope.row)"
              >执行</el-button
              >
              <el-button
                size="mini"
                class="tableDelButtton"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:strategy:remove']"
              >删除
              </el-button
              >
            </template>
          </el-table-column>
        </el-table>

      </div>

      <el-radio-group v-model="tabRadio" style="margin: 10px 0"  size="small">
        <el-radio-button size="small" label="data">历史洞内亮度</el-radio-button>
        <el-radio-button size="small" label="trend">历史车辆数</el-radio-button>
      </el-radio-group>
      <div v-show="tabRadio == 'data'" style="margin-bottom: 10px">
          <div class="charts-container">
            <div  class="chart" ref="chart" style="width: 20%; height: 400px; float: left"></div>
            <div class="chartFunction">
              <el-form
                ref="loginQueryForm"
                :model="loginModel"
                :inline="true"
                class="loginQueryFormClass"

                height="300px"
              >
                <el-form-item label="状态" align="center" prop="schedulerTime">
                  <template slot-scope="scope">
                    <el-switch
                      v-model="strategyState"
                      active-color="#13ce66"
                      inactive-color="#ff4949"
                      active-value="0"
                      inactive-value="1"

                    >
                    </el-switch>
                  </template>
                </el-form-item>
                <el-form-item label="下修比例"  >
                  <el-input size="small" v-model="titleHistory2"></el-input>
                </el-form-item>
              </el-form>
            </div>

        </div>
      </div>

      <div style="margin-bottom: 10px"  v-show="tabRadio == 'trend'">
        <div class="charts-container">
          <div   id='trend' class="chartTow" ref="chart1" style="width: 30%; height: 400px; float: left"></div>
          <div class="chartTowFunction" >
            <el-form
              ref="loginQueryForm"
              :model="loginModel"
              :inline="true"
              class="loginQueryFormClass"

              height="300px"
            >
              <el-form-item label="状态" align="center" prop="schedulerTime">
                <template slot-scope="scope">
                  <el-switch
                    v-model="strategyState"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"

                  >
                  </el-switch>
                </template>
              </el-form-item>
              <el-form-item label="下修比例"  >
                <el-input size="small" v-model="titleHistory2"></el-input>
              </el-form-item>
              <el-form-item label="时间段"  v-for="(item, index) in formItems" :key="index" >
                <el-time-picker
                  style="width: 35%"
                  v-model="item.startValue"
                  size="small"
                  :picker-options="pickerOptions"
                  placeholder="选择开始时间">
                </el-time-picker>

                <el-time-picker
                  style="width: 35%"
                  v-model="item.endValue"
                  size="small"
                  :picker-options="pickerOptions"
                  placeholder="选择结束时间">
                </el-time-picker>
                <el-button
                  style="width: 10%"
                  size="small"
                  class="tableBlueButtton"
                  @click="addHandleUpdate(index)"
                >+
                </el-button
                ><el-button
                style="width: 10%"
                size="small"
                class="tableBlueButtton"
                @click="deleteHandleUpdate(index)"
              >-
              </el-button
              >
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>

  </el-dialog>
</template>

<script>
import * as echarts from "echarts";
import {getCategoryTree} from "@/api/event/strategy";
import {listTunnels} from "@/api/equipment/tunnel/api";
import {dataLogInfoLineList} from "@/api/equipment/eqTypeItem/item";
export default {
  name: "timingTask",
  data() {
    return {
      titleHistory:"杭山东策略",
      titleHistory1:"2023-07-20",
      titleHistory2:"30%",
      visibleSync:false,
      operationLogDialog:false,
      tableData: [{
        id: 1,
        date: '2016-05-02',
        name: '测试定时控制00001',
        address: '加强照明',
        eqId:'出口段',
        controlInstruction:"开启",
        editableDisabled:false
      }, {
        id: 2,
        date: '2016-05-04',
        name: '测试定时控制00002',
        address: '加强照明',
        eqId:'入口段',
        controlInstruction:"开启",
        editableDisabled:false
      }, {
        id: 3,
        date: '2016-05-01',
        name: '测试定时控制00003',
        address: '加强照明',
        controlInstruction:"开启",
        editableDisabled:false,
        children: [{
          id: 31,
          date: '2016-05-01',
          name: '测试定时控制00003',
          address: '加强照明',
          eqId:'出口段',
          controlInstruction:"开启",
          editableDisabled:false
        }, {
          id: 32,
          date: '2016-05-01',
          name: '测试定时控制00003',
          address: '加强照明',
          eqId:'入口段',
          controlInstruction:"开启",
          editableDisabled:false
        }]
      }, {
        id: 4,
        date: '2016-05-03',
        name: '测试定时控制00004',
        address: '加强照明',
        eqId:'入口段',
        controlInstruction:"开启",
        editableDisabled:false
      }],
      chart: null,
      chart1: null,
      startTime: '', // 存储开始时间
      endTime: '', // 存储结束时间
      pickerOptions: {
        selectableRange: '00:00:00 - 23:59:59' // 设置可选的时间范围
      },
      formItems: [
        {
          label: '',
          startValue: '',
          endValue: '',
        },
        {
          label: '',
          startValue: '',
          endValue: '',
        },
        // 更多表单项...
      ],
      strategyState:false,
      tabRadio:"data",
      mychart1:null,
      editingRow:"",
      tunnelData: [], //隧道列表
      //隧道列表查询
      paramsData: {
        tunnelId: "",
      },
      directionOptions: [], //方向列表
      //设备类型
      equipmentTypeData: [
        {keyName:"加强照明",value:'1'},
        {keyName:"基础照明",value:'2'}
      ],
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
    }
  },
  mounted() {
    this.initChart();
  },
  created(){
    //查询隧道列表
    this.getTunnels()
    //查询方向
    this.getDirection()
  },
  methods:{
    //获取光亮的
    getEchartsData() {
      this.queryParams.deviceId = deviceId;
      this.queryParams.searchValue = searchValue;
        dataLogInfoLineList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          let list1 = response.rows;
          if (this.searchValue == "1") {
            this.CO = list1.map((item) => item.CO);
            this.VI = list1.map((item) => item.VI);
            this.VITime = list1.map((item) => item.createTime);
          } else if (this.searchValue == "2") {
            this.fsData = list1.map((item) => item.FS);
            this.fsTime = list1.map((item) => item.createTime);
          } else if (this.searchValue == "3") {
            this.dnData = list1.map((item) => item.data);
            this.dnTime = list1.map((item) => item.createTime);
          } else {
            this.dwData = list1.map((item) => item.data);
            this.dwTime = list1.map((item) => item.createTime);
          }
          this.loading = false;
          this.initChart();
        });
    },
    /** 查询隧道列表 */
    getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
        console.log(this.tunnelData, "隧道列表");
      });
    },
    //查询方向
    getDirection() {
      this.getDicts("sd_strategy_direction").then((response) => {
        this.directionOptions = response.data;
      });
    },
    handleRowEdit(item) {
      debugger
      this.editingRow = item.row.id;
      this.$forceUpdate()
    },
    handleRowSave(item){
      debugger
      this.editingRow =""
        this.$forceUpdate()
    },
    // 改变设备类型或者方向
    changeEvent(value,index) {
      if(index=="1"){
        let  tunnelData = this.tunnelData.find(item => item.tunnelId ==value.tunnelId);
        value.tunnelName = tunnelData.tunnelName
      }else if(index=="2"){
        console.log(this.directionOptions)
        debugger
        let  tunnelData = this.directionOptions.find(item => item.dictValue ==value.direction);
        value.dictLabel = tunnelData.dictLabel
      }

      this.equipmentTypeData = [];
      // 重置设备列表
      if(value.tunnelId.length !=0 && value.direction.length !=0){
        //this.listEqTypeStateIsControl();
        this.getEquipmentType();
      }
    },
    equipmenEvent(value){
      debugger
      let equipment = this.equipmentTypeData.find(item => item.value ==value.equipmentTypeId);
      value.equipmentName = equipment.keyName
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      let autoControl = this.strategyForm.autoControl;
      for (let i = 0; i < autoControl.length; i++) {
        getCategoryTree().then((data) => {
          this.$set(autoControl[i], "equipmentTypeData", data.data);
          this.equipmentName = data.data;
        });
      }
    },
    // 获取图表数据信息
    initChart1() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart1 = echarts.init(document.getElementById(this.tabRadio));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '历史车辆数',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天车辆数", "昨天车辆数", "今天车辆数"],
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
            name: "车辆数",
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
              name: "前天车辆数",
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
              data: this.yData1,
            },
            {
              name: "昨天车辆数",
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
              data: this.yData2,
            },
            {
              name: "今天车辆数",
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
              data: this.yData3,
            },
          ],
        };

        this.mychart1.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart1.resize();
        });
      });
    },
    // 获取图表数据信息
    initChart() {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        this.mychart = echarts.init(this.$refs.chart);
        var option = {
          tooltip: {
            trigger: "axis",
          },
          title: {
            text: '历史洞内亮度',
            textStyle: {
              color: '#00396B', // 设置标题颜色
            },
          },
          legend: {
            show: true,
            icon: "roundRect",
            itemWidth: 14,
            itemHeight: 8,
            x: "center",
            data: ["前天光强", "昨天光强", "今天光强"],
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
            name: "光强",
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
              name: "前天光强",
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
              data: this.yData1,
            },
            {
              name: "昨天光强",
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
              data: this.yData2,
            },
            {
              name: "今天光强",
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
              data: this.yData3,
            },
          ],
        };

        this.mychart.setOption(option);
        window.addEventListener("resize", function () {
          this.mychart.resize();
        });
      });
    },
    renderChart() {
      debugger
      this.chart = echarts.init(this.$refs.chart);
      this.chart.setOption({
        xAxis: {
          type: 'category',
          data: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        },
        yAxis: {
          type: 'value',
        },
        series: [{
          data: [10, 20, 30, 40, 50, 60, 70],
          type: 'line',
        }],
      });
    },
    closeLogin(){
      this.visibleSync = !this.visibleSync
      this.renderChart();
    },
    ProductionSetting(){

    },
    startDecoding(){

    },
    stopDecoding(){

    },

    resetQuery(){
      debugger
      this.visibleSync = !this.visibleSync
    },
    deleteHandleUpdate(index){
      this.formItems.splice(index,1)
      debugger
    },
    addHandleUpdate(index){
      debugger
      let form={
        label: '',
        startValue: '',
        endValue: ''
      }
      this.formItems.push(form)
    }
  },
  props:{
    show:Boolean,
  },
  watch:{
    show:{
      handler(newValue, oldValue){
        debugger
        this.visibleSync = !this.visibleSync
        this.initChart();
        this.initChart1();
      }
    },
    tabRadio: {
      handler(newValue, oldValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          this.$nextTick(() => {
            this.initChart1();
          });
        }
      },
    },
  }
}
</script>

<style lang="scss">
/* 设备含义中的table*/
.explain-table {
  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  .el-table::before {
    height: 0;
  }

  .el-table--mini td {
    padding: 2px 0;
  }

  .el-table .cell.el-tooltip {
    padding-top: 5px;
  }

  .el-table__body-wrapper {
    background-color: #304156;
  }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: #455d79;
    border-radius: 3px;
  }
  .el-radio {
    width: 240px;
    height: 40px;
    line-height: 40px;
  }
}
.operationDiglog {
  ::v-deep .el-input-group__append {
    padding: 0;
    width: 60px;
    border-left: none !important;
    .el-button {
      height: 32px;
      border-top-right-radius: 3px !important;
      border-bottom-right-radius: 3px !important;
      border-top-left-radius: 0px !important;
      border-bottom-left-radius: 0px !important;
      // transform: translateX(20px);
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  .el-dialog .el-form {
    // padding: 15px !important;
    .el-form-item__content .el-button {
      width: 88px;
      height: 22px;
      border: none;
    }
    .el-form-item__content .el-button--mini {
      padding: 2px 15px !important;
    }
  }
  .el-dialog__body {
    padding: 0 15px !important;
    .el-col {
      padding: 0 !important;
    }
  }
  .el-table {
    margin-bottom: 20px;
  }
}
.charts-container {
  display: flex;
}

.chart {
  flex: 0 0 40%;
  height: 400px;
  margin-right: 10px;
}

.chartTow {
  flex: 0 0 40%;
  height: 400px;
}
.chartFunction {
  flex: 0 0 30%;
  height: 400px;
  margin-right: 10px;
}
.chartTowFunction {
  flex: 0 0 30%;
  height: 400px;
  margin-right: 10px;
}
::v-deep .el-radio-button--small .el-radio-button__inner {
  padding: 5px 10px !important;
  background: transparent;
  border: 1px solid transparent;
  background-color: red;
}
</style>
