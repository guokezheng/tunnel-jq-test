<template>
  <div class="app-container">
      <el-row
        :gutter="20"
        class="topFormRow"
      >
        <el-col :span="6">
          <el-button
            size="small"
            :loading="exportLoading"
            @click="handleExportTab"
          >导出
          </el-button>
          <el-button size="small" @click="resetQueryTab"
          >刷新</el-button
          >
        </el-col>
        <el-col :span="6" :offset="12">
          <div ref="main" class="grid-content bg-purple" >
            <el-input
              placeholder="请输入设备名称、桩号，回车搜索"
              v-model="querysParamsTab.pile"
              @keyup.enter.native="handleQueryTab"
            >
              <el-button
                slot="append"
                icon="icon-gym-Gsearch"
                @click="device_boxShow = !device_boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="searchBox" v-show="device_boxShow">
        <el-form
          ref="queryFormTab"
          :inline="true"
          :model="querysParamsTab"
          label-width="75px"
        >
          <el-form-item
            label="所属隧道"
            prop="tunnelId"
          >
            <el-select
              v-model="querysParamsTab.tunnelId"
              placeholder="请选择所属隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item
            label="设备类型"
            prop="searchValue"
          >
            <el-select
              v-model="querysParamsTab.searchValue"
              placeholder="请选择设备类型"
              clearable
              size="small"
            >
              <el-option label="CO/VI" value="1"> </el-option>
              <el-option label="风速风向" value="2"> </el-option>
              <el-option label="洞内光强" value="3"> </el-option>
              <el-option label="洞外光强" value="4"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item class="bottomBox">
            <el-button size="small" type="primary" @click="handleQueryTab"
            >搜索</el-button
            >
            <el-button size="small" @click="resetQueryTab" type="primary" plain
            >重置</el-button
            >
          </el-form-item>
        </el-form>
      </div>

    <div class="tableTopHr" ></div>

      <el-table
        ref="tables"
        v-loading="loading"
        :data="listTab"
        @selection-change="handleSelectionChangeTab"
        class="allTable"
        height="58vh"
      >
        <el-table-column type="selection" width="55" align="center" reserve-selection/>
        <el-table-column type="index" :index="indexMethodTab" label="序号" width="68" align="center"></el-table-column>
        <el-table-column label="设备名称" align="center" prop="eqName" />
        <el-table-column label="所属隧道" align="center" prop="tunnelName" />
        <el-table-column label="管理机构" align="center" prop="deptName" />
        <el-table-column label="方向" align="center" prop="direction" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              class="tableBlueButtton"
              @click="handleRecordy(scope.row)"
            >查看详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="totalTab > 0"
        :total="totalTab"
        :page.sync="querysParamsTab.pageNum"
        :limit.sync="querysParamsTab.pageSize"
        @pagination="getListTab"
      />


    <!--详情弹窗-->
    <el-dialog :visible.sync="record" width="70%">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div>
        <el-row
          :gutter="20"
          class="topFormRow"
        >
          <el-col :span="6">
            <el-button
              size="small"
              :loading="exportLoading"
              @click="handleExportRecord"
            >导出
            </el-button>
            <el-button size="small" @click="resetQuery"
            >刷新</el-button
            >
          </el-col>
          <el-col :span="1"  :offset="13" style="margin-left: 45.166667%;">
            <div @click="marketChang()">
              <i
                class="el-icon-s-marketing"
                style="font-size: 36px; color: #39adff; float: left"
              ></i>
            </div>
          </el-col>
          <el-col :span="6" style ="padding-left: 0px;">

            <div style="width: 100%; float: left;"  id="pldiv">
              <el-form
                ref="querysForm"
                :inline="true"
                :model="querysParams"
                label-width="75px"
              >
                <el-date-picker
                  v-model="dateRange"
                  size="small"
                  style="width: 100%"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetimerange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  @change="selectTime"
                  :default-time="['00:00:00', '23:59:59']"
                  append-to-body
                  popper-class="DataStyle"
                ></el-date-picker>
              </el-form>
            </div>

          </el-col>
        </el-row>

      </div>
      <div v-show="echartShow == false">
        <el-table
          ref="tables"
          v-loading="loading"
          :data="list"
          @selection-change="handleSelectionChange1"
          v-show="searchValue == '1'"
          class="allTable"
          height="58vh"
          :row-key="getRowKey"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
          <el-table-column label="设备名称" align="center" prop="eqName" />
          <el-table-column label="所属隧道" align="center" prop="tunnelName" />
          <el-table-column label="管理机构" align="center" prop="deptName" />
          <el-table-column label="方向" align="center" prop="direction" />
          <el-table-column label="桩号" align="center" prop="pile" />
          <el-table-column label="CO(ppm)" align="center" prop="CO" />
          <el-table-column label="VI(m)" align="center" prop="VI" />
          <el-table-column label="采集时间" align="center" prop="createTime" />
        </el-table>

        <el-table
          v-loading="loading"
          :data="list1"
          class="allTable"
          @selection-change="handleSelectionChange2"
          v-show="searchValue == '2'"
          height="58vh"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :index="indexMethod1" label="序号" width="68" align="center"></el-table-column>
          <el-table-column label="设备编码" align="center" prop="eqId" />
          <el-table-column label="设备名称" align="center" prop="eqName" />
          <el-table-column label="所属隧道" align="center" prop="tunnelName" />
          <el-table-column label="管理机构" align="center" prop="deptName" />
          <el-table-column label="方向" align="center" prop="direction" />
          <el-table-column label="桩号" align="center" prop="pile" />
          <el-table-column label="风速(m/s)" align="center" prop="FS" />
          <el-table-column label="风向" align="center" prop="FX" />
          <el-table-column
            label="采集时间"
            align="center"
            prop="createTime"
            width="180"
            sortable
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-table
          ref="tables"
          v-loading="loading"
          :data="list2"
          @selection-change="handleSelectionChange3"
          v-show="searchValue == '3'"
          class="allTable"
          height="58vh"
          :row-key="getRowKey"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :index="indexMethod2" label="序号" width="68" align="center"></el-table-column>
          <el-table-column label="设备编码" align="center" prop="eqId" />
          <el-table-column label="设备名称" align="center" prop="eqName" />
          <el-table-column label="所属隧道" align="center" prop="tunnelName" />
          <el-table-column label="管理机构" align="center" prop="deptName" />
          <el-table-column label="方向" align="center" prop="direction" />
          <el-table-column label="桩号" align="center" prop="pile" />
          <el-table-column label="洞内亮度(lux)" align="center" prop="data" />
          <el-table-column label="采集时间" align="center" prop="createTime" />
        </el-table>
        <el-table
          ref="tables"
          v-loading="loading"
          :data="list3"
          @selection-change="handleSelectionChange4"
          v-show="searchValue == '4'"
          class="allTable"
          height="58vh"
          :row-key="getRowKey"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :index="indexMethod3" label="序号" width="68" align="center"></el-table-column>
          <el-table-column label="设备编码" align="center" prop="eqId" />
          <el-table-column label="设备名称" align="center" prop="eqName" />
          <el-table-column label="所属隧道" align="center" prop="tunnelName" />
          <el-table-column label="管理机构" align="center" prop="deptName" />
          <el-table-column label="方向" align="center" prop="direction" />
          <el-table-column label="桩号" align="center" prop="pile" />
          <el-table-column label="洞外亮度(cd/㎡)" align="center" prop="data" />
          <el-table-column label="采集时间" align="center" prop="createTime" />
        </el-table>
        <pagination
          v-show="total > 0&&this.searchValue == '1'"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
        <pagination
          v-show="total > 0&&this.searchValue == '2'"
          :total="total"
          :page.sync="queryParams1.pageNum"
          :limit.sync="queryParams1.pageSize"
          @pagination="getList"
        />
        <pagination
          v-show="total > 0&&this.searchValue == '3'"
          :total="total"
          :page.sync="queryParams2.pageNum"
          :limit.sync="queryParams2.pageSize"
          @pagination="getList"
        />
        <pagination
          v-show="total > 0&&this.searchValue == '4'"
          :total="total"
          :page.sync="queryParams3.pageNum"
          :limit.sync="queryParams3.pageSize"
          @pagination="getList"
        />
      </div>
      <div v-show="echartShow">
        <div ref="echartsBox" id="echarts-Box" class="echarts-Box" style ="width: 1220px !important;"></div>
      </div>



    </el-dialog>

  </div>
</template>

<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { treeselectExcYG1 } from "@/api/system/dept";
import { getUserDeptId } from "@/api/system/user";
import {
  dataDevicesLogInfoList,
  dataLogInfoLineList,
  dataLogInfoList, exportDatainforTab, handleExportRecord,
} from "@/api/equipment/eqTypeItem/item";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {exportLogininfor1} from "@/api/system/log";

export default {
  name: "Logininfor",
  dicts: ["sys_common_status"],
  components: { Treeselect },

  data() {
    return {
      manageStatin: this.$cache.local.get("manageStation"),
      sj_boxShow: false,
      device_boxShow:false,
      searchValue: null,
      deviceId: null,
      record: false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 总条数
      totalTab: 0,
      // 表格数据  covi
      list: [],
      // 表格数据  风速风向
      list1: [],
      // 表格数据  洞内
      list2: [],
      // 表格数据  洞外
      list3: [],
      // 设备表格数据
      listTab: [],
      // 日期范围
      dateRange: [],
      //所属隧道
      eqTunnelData: {},
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      //控制方式
      controlTypeOptions: [],
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      //table查询参数
      querysParamsTab:{
        pageNum: 1,
        pageSize: 10,
        tunnelId:null,
        pile: null,
        searchValue:null,
        ids:"",
      },
      // 查询参数  covi
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        eqType:null,
        searchValue:null,
        ids:"",
      },

      // 查询参数  风速风向
      queryParams1: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        deptId: null,
        pile: null,
        deviceId: null,
        ids:"",
      },
      // 查询参数  洞内
      queryParams2: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        deptId: null,
        pile: null,
        deviceId: null,
        ids:"",
      },
      // 查询参数 洞外
      queryParams3: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        deptId: null,
        pile: null,
        deviceId: null,
        ids:"",
      },
      querysParams: {
        id: null,
        ids:"",
      },
      selectedValue: [],
      echartShow: false,
      CO: [],
      VI: [],
      VITime: [],
      fsData: [],
      fsTime: [],
      dnData: [],
      dnTime: [],
      dwData: [],
      dwTime: [],
      // 部门树选项
      deptOptions: undefined,
      // 部门树选项
      jgOptions: undefined,
      currentData: "",
    };
  },
  created() {
    this.dateRange = this.getPastTime(24);
    this.getListTab();
    this.getUserDept();
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    this.getDicts("sd_device_opt_state").then((response) => {
      this.operationStateOptions = response.data;
    });

    this.currentData = "日期";
  },

  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
    this.watchSize();

  },

  methods: {
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id
    },

    // 参数timer是过去的n个小时
    getPastTime(timer) {
      // 获取过去的时间
      const lastTime = new Date().getTime() - `${timer * 60 * 60 * 1000}`;
      const startTime = this.timeFormat(lastTime);
      // 当前时间时间
      let time = new Date().getTime();
      const endTime = this.timeFormat(time);
      return [startTime, endTime];
    },

    //时间生成并处理
    timeFormat(time) {
      // 对应的方法
      const timeType = [
        "getFullYear",
        "getMonth",
        "getDate",
        "getHours",
        "getMinutes",
        "getSeconds"
      ];
      // 分隔符
      const separator = {
        getFullYear: "-",
        getMonth: "-",
        getDate: " ",
        getHours: ":",
        getMinutes: ":",
        getSeconds: ""
      };
      let resStr = "";
      for (let i = 0; i < timeType.length; i++) {
        const element = timeType[i];
        let resTime = new Date(time)[element]();
        // 获取月份的要+1
        resTime = element == "getMonth" ? resTime + 1 : resTime;
        // 小于10，前面加0
        resTime = resTime > 9 ? resTime : "0" + resTime;
        resStr = resStr + resTime + separator[element];
      }
      return resStr;
    },

    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.device_boxShow == true) {
          self.device_boxShow = false;
        }
      }
    },
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },

    //翻页时不刷新序号
    indexMethodTab(index){
      return index+(this.querysParamsTab.pageNum-1)*this.querysParamsTab.pageSize+1
    },

    //翻页时不刷新序号
    indexMethod1(index){
      return index+(this.queryParams1.pageNum-1)*this.queryParams1.pageSize+1
    },
   //翻页时不刷新序号
    indexMethod2(index){
      return index+(this.queryParams2.pageNum-1)*this.queryParams2.pageSize+1
    },
    //翻页时不刷新序号
    indexMethod3(index){
      return index+(this.queryParams3.pageNum-1)*this.queryParams3.pageSize+1
    },

    /** 数据报表Tab导出按钮操作 */
    handleExportTab() {
      let confirmInfo ="是否确认导出所有的数据报表数据项？";
      if(this.ids.length>0){
        confirmInfo = "是否确认导出所选的数据报表数据项？";
      }
      this.querysParamsTab.ids = this.ids.join();
      const queryParams = this.querysParamsTab;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportDatainforTab(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tables.clearSelection();
          this.querysParamsTab.ids = ''
        })
        .catch(() => {});
    },



    /** 数据报表详情导出按钮 */
    handleExportRecord() {
      this.querysParams.searchValue = this.searchValue;
      this.querysParams.deviceId = this.deviceId;
      this.querysParams.ids = this.ids.join();
      const queryParams = this.querysParams;
      let confirmInfo;
      if(this.searchValue=="1"){//covi
        confirmInfo = "是否确认导出所有的COVI数据项？";
        if(this.ids.length>0){
          confirmInfo = "是否确认导出所选的COVI数据项？";
        }
      }else if(this.searchValue=="2"){//风速风向
        confirmInfo = "是否确认导出所有的风速风向数据项？";
        if(this.ids.length>0){
          confirmInfo = "是否确认导出所选的风速风向数据项？";
        }
      }else if(this.searchValue=="3"){//洞内光强
        confirmInfo = "是否确认导出所有的洞内光强数据项？";
        if(this.ids.length>0){
          confirmInfo = "是否确认导出所选的洞内光强数据项？";
        }
      }else{//洞外光强
        confirmInfo = "是否确认导出所有的洞外光强数据项？";
        if(this.ids.length>0){
          confirmInfo = "是否确认导出所选的洞外光强数据项？";
        }
      }

      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return handleExportRecord(this.addDateRange(queryParams, this.dateRange))
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tables.clearSelection();
          this.querysParams.ids = ''
        })
        .catch(() => {});
    },

    handleRecordy(row) {
      this.device_boxShow = false;
      this.echartShow = false;
      this.record = true;
      this.dateRange = this.getPastTime(24);
      //this.resetForm("queryForms");
      this.queryParams.pageNum = "1";
      this.queryParams.pageSize = "10";
      if(row.eqType=='19'){
        this.searchValue="1";
      }else if(row.eqType=='17'){
        this.searchValue="2";
      }else if(row.eqType=='18'){
        this.searchValue="3";
      }else{
        this.searchValue="4";
      }
      this.deviceId = row.eqId;
      this.getList(row.eqId,this.searchValue);

    },


    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    marketChang() {
      this.echartShow = !this.echartShow;
      if(this.echartShow)//echart
        this.getEchartsData();
      else
        this.getList();
      //this.initChart();
    },
    watchSize() {
      let that = this;
      let erd = elementResizeDetectorMaker();
      let Dom = that.$refs.echartsBox; //拿dom元素
      //监听盒子的变化
      erd.listenTo(Dom, function (element) {
        let myChart = echarts.init(Dom);
        myChart.resize(); //echarts自带的方法可以使图表重新加载
      });
    },
    initChart() {
      var chartDom = document.getElementById("echarts-Box");
      var myChart = echarts.init(chartDom);
      var option;
      let legends;
      if (this.searchValue == 1) {
        var series = [
          {
            name: "CO",
            type: "line",
            label: {
              show: false,
              position: "top",
            },
            data: this.CO,
          },
          {
            name: "VI",
            label: {
              show: false,
              position: "top",
            },
            data: this.VI,
            type: "line",
          },

        ];
        var xAxis = [
          {
            type: "category",
            name: this.currentData,
            data: this.VITime,
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
            },
            nameTextStyle: {
              fontFamily: "PingFang",
              fontsize: "29px",
            },
          },

      ];
        var yAxis = [
          {
            type: "value",
            name: "CO(ppm)/VI(km)",
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置y轴线条颜色为黑色
                width: 2, // 设置y轴线条宽度为2像素
              },
              show: true,
            },
          },
        ];

        legends = {
          textStyle: {
            fontSize: 13, //字体大小
            color: "#ffffff", //字体颜色
          },
          left: "center",
          data: ["CO", "VI"],
        };
      } else if (this.searchValue == 2) {
        //   fsData: [],
        // dnData: [],
        // dwData: [],
        var series = [
          {
            name: "风速风向",
            stack: "Total",
            label: {
              show: false,
              position: "top",
            },
            data: this.fsData,
            type: "line",

          },
        ];
        var xAxis = [
          {
            type: "category",
            name: this.currentData,
            data: this.fsTime,
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
            },
          },
        ];
        var yAxis = [
          {
            type: "value",
            name: "m/s",
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
              show: true,
            },
          },
        ];
      } else if (this.searchValue == 3) {
        var series = [
          {
            name: "洞内亮度",
            stack: "Total",
            label: {
              show: false,
              position: "top",
            },
            data: this.dnData,
            type: "line",
          },
        ];
        var xAxis = [
          {
            type: "category",
            name: this.currentData,
            data: this.dnTime,
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
            },
          },
        ];
        var yAxis = [
          {
            type: "value",
            name: "lux",
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
              show: true,
            },
          },
        ];
      } else if (this.searchValue == 4) {
        //   fsData: [],
        // dnData: [],
        // dwData: [],
        var series = [
          {
            name: "洞外亮度",
            stack: "Total",
            label: {
              show: false,
              position: "top",
            },
            data: this.dwData,
            type: "line",
          },
        ];
        var xAxis = [
          {
            type: "category",
            name: this.currentData,
            data: this.dwTime,
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
            },
          },
        ];
        var yAxis = [
          {
            type: "value",
            name: "cd/㎡",
            axisLine: {
              lineStyle: {
                color: "#ffffff", // 设置x轴额色为白色
                fontsize: "29px",
              },
              show: true,
            },
          },
        ];
      }
      option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: legends,
        xAxis,
        yAxis,
        series,
      };

      option && myChart.setOption(option, true);
    },

    /** 所属隧道 */
    getTunnel(userDeptId) {
      listTunnels(userDeptId).then((response) => {
        this.eqTunnelData = response.rows;
      });
    },

    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        // this.queryParams.deptId = response.rows[0].deptId;
        this.getTunnel(this.userDeptId);
      });
    },
    //操作是否成功 0：成功 1：失败
    stateFormat(row, column) {
      return this.selectDictLabel(this.operationStateOptions, row.state);
    },
    controlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    },
    /** 查询设备列表 **/
    getListTab(){
      this.loading = true;
      if (this.manageStatin == "1") {
        this.querysParamsTab.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      dataDevicesLogInfoList(
        this.addDateRange(this.querysParamsTab)
      ).then((response) => {
        this.listTab = response.rows;
        this.totalTab = response.total;
        this.loading = false;
      });


    },

    /** 查询列表 */
    getList(deviceId,searchValue) {
      this.loading = true;
      if(typeof searchValue === 'undefined'){//翻页
        deviceId = this.queryParams.deviceId;
        searchValue = this.queryParams.searchValue;
      }
      this.queryParams.deviceId = deviceId;
      this.queryParams.searchValue =searchValue;
      this.queryParams1 = this.queryParams;
      this.queryParams2 = this.queryParams;
      this.queryParams3 = this.queryParams;
      if (deviceId!=null&&deviceId!="") {//COVI
        dataLogInfoList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          if(searchValue=='1'){
            this.list = response.rows;
          }else if(searchValue=='2'){
            this.list1 = response.rows;
          }else if(searchValue=='3'){
            this.list2 = response.rows;
          }else{
            this.list3 = response.rows;
          }

          this.total = response.total;
          this.loading = false;
        });
      }
    },

    getEchartsData(){
      if (this.searchValue != null) {
        dataLogInfoLineList(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          let list1 = response.rows;
          if(this.searchValue=="1"){
            this.CO = list1.map((item) => item.CO);
            this.VI = list1.map((item) => item.VI);
            this.VITime = list1.map((item) => item.createTime);
          }else if(this.searchValue=="2"){
            this.fsData = list1.map((item) => item.FS);
            this.fsTime = list1.map((item) => item.createTime);
          }else if(this.searchValue=="3"){
            this.dnData = list1.map((item) => item.data);
            this.dnTime = list1.map((item) => item.createTime);
          }else{
            this.dwData = list1.map((item) => item.data);
            this.dwTime = list1.map((item) => item.createTime);
          }
          this.loading = false;
          this.initChart();
        });
      }

    },
    selectTime(){
      //先判断当前弹窗展示的列表还是图表
      if(this.echartShow)//echart
        this.getEchartsData();
      else
        this.getList();
    },
    /*table搜索*/
    handleQueryTab(){
      this.querysParamsTab.pageNum = 1;
      this.$refs.tables.clearSelection();
      this.getListTab();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForms");
      //待写
      if(this.echartShow)//echart
        this.getEchartsData();
      else
        this.getList();
    },
    //table重置按钮
    resetQueryTab(){
      this.resetForm("queryFormTab");
      this.querysParamsTab.pile = "";
      this.handleQueryTab();

    },
    /** 多选框选中数据 */
    handleSelectionChangeTab(selection) {
      this.ids = selection.map((item) => item.eqId);
      this.single = selection.length!==1
      this.multiple = !selection.length;
    },

    /** 多选框选中数据 */
    handleSelectionChange1(selection) {
      this.ids = selection.map((item) => item.createTime);
      this.single = selection.length!==1
      this.multiple = !selection.length;
    },

    /** 多选框选中数据 */
    handleSelectionChange2(selection) {
      this.ids = selection.map((item) => item.createTime);
      this.multiple = !selection.length;
    },

    /** 多选框选中数据 */
    handleSelectionChange3(selection) {
      this.ids = selection.map((item) => item.createTime);
      this.multiple = !selection.length;
    },
    /** 多选框选中数据 */
    handleSelectionChange4(selection) {
      this.ids = selection.map((item) => item.createTime);
      this.multiple = !selection.length;
    },
  },
};
</script>
<style scoped lang="scss">
.echarts-Box {
  width: 100vw;
  height: 60vh;
}

.butBox {
  width: 315px;
  display: flex;
  padding: 4px 4px;
  background: #9ecced;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  // justify-content: space-between;
  div {
    padding: 6px 10px;
    color: #fff;
    letter-spacing: 1px;
    cursor: pointer;
  }
  .xz {
    background: #285b8d;
    border-radius: 10px;
  }
}
.addClass {
  .el-select {
    width: 250px;
  }
  .el-input {
    width: 250px !important;
  }
  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 250px !important;
  }
}
.circle {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  display: inline-block;
}
.detailsText {
  display: inline-block;
  margin-left: 20px;
  line-height: 40px;
  width: 100px;
}
hr {
  border: solid 1px #ddd;
}
.rowClass {
  border-top: solid 1px #ddd;
  border-bottom: solid 1px #ddd;
  height: 40px;
  margin-top: 10px;
}
.eventClass {
  height: 30px;
  border-right: solid 1px #ddd;
  width: 100%;
  text-align: center;
  margin-top: 5px;
  line-height: 30px;
}
.eventTitleClass {
  height: 40px;
  background-color: #eeeeee;
  line-height: 40px;
  text-align: center;
}
.video {
  height: 300px;
  border-radius: 0;
  padding: 5px;
  margin-top: 0;
}
.image3 {
  padding: 5px;
  height: 49%;
  // border: solid 1px green;
  width: 100%;
}
.card-box {
  width: 30%;
  text-align: center;
  font-weight: bold;
}

.EquipStatistics {
  width: 200px;
  height: 40px;
  background-image: url(../../../assets/cloudControl/shebeiWarning.png);
  color: white;
  text-align: center;
  line-height: 40px;
  font-weight: 400;
  font-size: 16px;
  margin-left: 14px;
  > span {
    font-size: 24px;
    font-weight: 600;
    vertical-align: middle;
  }
}
.warningStatistics {
  line-height: 60px;
  font-size: 14px;
  // color: #606266;
  font-weight: 700;
}
.eventTitle {
  padding: 15px 0;
  font-size: 18px;
  font-weight: 400;
  color: #303133;
}
.tableHeight {
  max-height: 59vh !important;
  overflow: auto;
}
</style>

