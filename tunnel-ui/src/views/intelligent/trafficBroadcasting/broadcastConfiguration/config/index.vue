<template>
  <div style="height: 100%;padding: 1.5rem 0;margin-right: 1.5rem">
    <el-row :gutter="32" style="height:100%">
      <el-col :xs="24" :sm="24" :lg="8" style="height: 100%">
        <el-card class="box-card" style="margin-left: 2rem; border:solid 2px #c0c0c0;height: 100%;overflow: auto;">
          <div slot="header" class="clearfix">
            <span>广播终端</span>
            <el-button
              @click="toggleSelection()"
              style="float: right; padding: 3px 20px 3px 0px"
              type="text"
            >全选</el-button
            >
          </div>
          <div style="display: flex;align-items: center">
            <span style="font-size: 16px;">隧道名称：</span>
            <el-select v-model="queryParams.tunnelId" placeholder="请选择所属隧道" clearable size="small" style="flex:60%;margin-right: 10px;">
              <el-option v-for="item in broadcastInformationListTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                         :value="item.tunnelId" />
            </el-select>&nbsp;&nbsp;&nbsp;
            <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
          </div>
          <div
            class="chart-wrapper"
            style="width: 100%; height: 32vh; overflow: auto"
          >
            <el-table
              ref="multipleTable"
              :data="tableData"
              tooltip-effect="dark"
              style="width: 100%"
              :show-header="false"
              @selection-change="handleSelectionChange"
              @row-click="handleRowClick"
            >
              <el-table-column type="selection" width="45"> </el-table-column>
              <el-table-column label="隧道名"  style="flex:70%;">
                <template slot-scope="scope">
                  {{scope.row.tunnelName}}+{{scope.row.broadcastName}}
                </template>
              </el-table-column>
              <!-- <el-table-column label="操作" width="50">
                <template slot-scope="scope">
                  <i
                    style="cursor: pointer; color: #76a3ec"
                    class="el-icon-delete"
                    @click="handleDelete(scope.$index, scope.row)"
                  ></i>
                </template>
              </el-table-column> -->
            </el-table>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="16" style="height: calc(50% - 1rem);margin-bottom: 2rem;">
        <el-card class="box-card" style="height: 100%;  overflow: auto; border:solid 2px #c0c0c0;">
          <div slot="header" class="clearfix">
            <span>广播模板</span>
          </div>
          <el-form
            :model="dataForm"
            label-width="90px"
            ref="dataForm"
            size="mini"
          >
            <el-table
              ref="templateTable"
              :data="templateData"
              tooltip-effect="dark"
              style="width: 100%"
              :show-header="false"

            >
              <el-table-column label="广播模板" >
                <template slot-scope="scope" style="width: 100%;">
                  <div class="broadcastTemplate">{{scope.row.broadcastContent}}</div>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <i
                    style="cursor: pointer; color: #76a3ec;"
                    @click="handleCheck(scope.row)"
                  >选择模板</i>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="16" style="height: calc(50% - 1rem)">
        <el-card class="box-card" style="margin-bottom: 2.875rem; height: 100%; overflow: auto; border:solid 2px #c0c0c0;">
          <div slot="header" class="clearfix">
            <span>广播发布</span>
          </div>
          <el-form
            :model="dataForm"
            label-width="90px"
            ref="dataForm"
            size="mini"
          >
            <el-form-item prop="eventid" label="" class="textareacls">
              <el-col :xs="24" :sm="24" :lg="24">
                <textarea
                  rows="3"
                  class="el-textarea__inner"
                  :maxlength="250"
                  v-model="dataForm.releaseContent"
                  style="border: solid 1px #c0c0c0;font-size: 16px "
                ></textarea>
              </el-col>
            </el-form-item>

            <el-row :gutter="32" style="margin-top: 2%">
              <el-col :xs="12" :sm="12" :lg="8">
                <el-form-item prop="eventid" label="发言人">
                  <el-select
                    v-model="dataForm.spokesman"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in fayanoptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="8">
                <el-form-item prop="eventid" label="语速">
                  <el-select
                    v-model="dataForm.speedSpeech"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in yusuoptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="8">
                <el-form-item prop="eventid" label="保存录音">
                  <el-select
                    v-model="dataForm.saveRecording"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in luyinoptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="32">
              <el-col :xs="12" :sm="12" :lg="8">
                <el-form-item prop="eventid" label="播放次数">
                  <el-input
                    type="number"
                    v-model.number="dataForm.playbackTimes"
                    :max="100"
                    :min="1"
                    placeholder="请输入播放次数"
                    @input="playbackTimesChange"
                    style="width:12.5rem;"
                  />
                </el-form-item>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="8">
                <el-form-item prop="eventid" label="音量 (dB)">
                  <el-input
                    type="number"
                    v-model.number="dataForm.volume"
                    :max="100"
                    :min="1"
                    placeholder="请输入播放次数"
                    @input="volumeChange"
                    style="width:12.5rem;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-row :gutter="32" style="margin: 3% -16px 0 70%">
            <template>
              <el-button size="small" @click="reset()">重置</el-button>
              <el-button
                size="small"
                @click="dataFormSubmitHandle()"
                type="primary"
                :disabled="abled"
              >发布</el-button
              >
            </template>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { addRecord } from "@/api/intelligent/trafficBroadcasting/broadcastRecord/record/record";
import { listList } from "@/api/intelligent/trafficBroadcasting/broadcastInformation/list/list";
import Template from "../../../../information/template/index";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { listTemplate } from "@/api/intelligent/trafficBroadcasting/broadcastTemplate/template/template";

export default {
  components: {Template},
  data() {
    return {
      // 广播信息列表格数据
      listList: [],
      tableData: [],
      templateData: [],
      abled: false,
      multipleSelection: [],
      fayanoptions: [
        {
          value: "汤姆",
          label: "汤姆",
        },
        {
          value: "大卫",
          label: "大卫",
        },
      ],

      yusuoptions: [
        {
          value: "10",
          label: "10快音速",
        },
        {
          value: "9",
          label: "9快音速",
        },
        {
          value: "8",
          label: "8快音速",
        },
        {
          value: "7",
          label: "7快音速",
        },
        {
          value: "6",
          label: "6快音速",
        },
        {
          value: "5",
          label: "5正常音速",
        },
        {
          value: "4",
          label: "4慢音速",
        },
        {
          value: "3",
          label: "3慢音速",
        },
        {
          value: "2",
          label: "2慢音速",
        },
        {
          value: "1",
          label: "1慢音速",
        },
      ],

      luyinoptions: [
        {
          value: "1",
          label: "不保存",
        },
        {
          value: "0",
          label: "保存",
        },
      ],

      bofangoptions: [
        {
          value: "5",
          label: "5",
        },
        {
          value: "4",
          label: "4",
        },
      ],

      yinliangoptions: [
        {
          value: "0",
          label: "0默认",
        },
        {
          value: "1",
          label: "音量1",
        },
      ],
      /* 隧道 */
      broadcastInformationListTunnelData: {},
      // 查询参数
      queryParams: {
        tunnelId: null,
      },
      dataForm: {
        eqIds: "",
        eqNames: "",
        spokesman: "",
        speedSpeech: "",
        saveRecording: "",
        playbackTimes: "",
        volume: "",
        releaseContent: "",
      },
      segmentNo: "",
      segmentName: "",
      formLabel: {},
      sIndex: 0,
      showSink: false,
    };
  },
  created() { },
  mounted() {
    this.getList();
    this.getTunnel();
    this.getTemplateList();
    if (
      this.$route.fullPath.indexOf("?") != -1 &&
      this.$route.fullPath.indexOf("appId") != -1
    ) {
      let routePath = this.$route.fullPath;
      let indexIte = this.$route.fullPath.lastIndexOf("?");
      let routenRes = routePath.substring(indexIte + 1, routePath.length);
      let routenResDecode = decodeURIComponent(routenRes);
      let resultJson = JSON.parse(routenResDecode);
      //判断是否是外部接口调用
      let isExternal = resultJson.hasOwnProperty("appId");
      if (isExternal) {
        this.showSink = true;
        this.externalFunction(resultJson);
      } else {
        this.showSink = false;
      }
    }
  },
  filters: { },
  methods: {
    /* 所选隧道 */
    getTunnel() {
      listTunnels().then(response => {
        this.broadcastInformationListTunnelData = response.rows;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    getTemplateList() {
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows;
        this.templateData = response.rows;
        this.total = response.total;
      });
    },
    /*查询广播信息列表*/
    getList() {
      listList(this.queryParams).then(response => {
        this.listList = response.rows;
        this.tableData = response.rows;
        this.total = response.total;
      });
    },
    /*外部接口调用返回方法*/
    externalFunction(params) {
      params.interfaceType = "2";
      api.getExternalResult(params).then((result) => {
        if (result.code == 0) {
          this.formLabel = {
            evtType: result.data.evtType, //事件类型
            occurTime: result.data.occurTime, //发生时间
            source: result.data.source, //事件来源
            content: result.data.content, //内容
          };
        }
      });
    },
    getEqAllInfo() {
      api
        .getJTongEqInfo()
        .then((data) => {
          //var data = data
        })
        .catch((err) => {
          alert(err);
        });
    },
    //表格全选操作
    toggleSelection() {
      this.$refs.multipleTable.toggleAllSelection();
      this.dealInfo();
    },
    handleSelectionChange(val) {
      console.log("val:" + val)
      this.multipleSelection = val;
      this.dealInfo();
    },
    // 点击行，切换选中状态
    handleRowClick(row) {
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    dealInfo() {
      this.dataForm.eqIds = "";
      console.log(this.multipleSelection);
      for (var i = 0; i < this.multipleSelection.length; i++) {
        this.dataForm.eqIds =
          this.dataForm.eqIds + this.multipleSelection[i].id + ",";
      }
      if (this.multipleSelection.length > 0) {
        this.dataForm.eqIds = this.dataForm.eqIds.substring(
          0,
          this.dataForm.eqIds.lastIndexOf(",")
        );
      }
      console.log(this.dataForm);
    },
    //获取相应的交通广播设备列表
    querybctList() {
      var parm = {
        pile_start: this.chooseRoad.segmentStartPile,
        pile_end: this.chooseRoad.segmentEndPile,
        sectionId: "3",
        devType: "019",
      };
      api
        .eqList(parm)
        .then((data) => {
          this.tableData = data;
        })
        .catch((err) => {
          alert(err);
        });
    },
    // 交通广播发布-表单提交
    async dataFormSubmitHandle() {
      if (this.dataForm.eqIds == "") {
        this.$message({
          message: "请先选择需要发布的设备！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.releaseContent == "") {
        this.$message({
          message: "请输入需要发布的内容！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.spokesman == "") {
        this.$message({
          message: "请选择发言人！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.speedSpeech == "") {
        this.$message({
          message: "请选择语速！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.saveRecording == "") {
        this.$message({
          message: "请选择是否保存录音！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.playbackTimes == "") {
        this.$message({
          message: "请选择播放次数！",
          type: "warning",
        });
        return;
      }
      if (this.dataForm.volume == "") {
        this.$message({
          message: "请选择音量！",
          type: "warning",
        });
        return;
      }
      //发布广播
      this.release();
    },
    handleCheck(row){
      console.log("同步数据到模板发布");
      this.dataForm.releaseContent = row.broadcastContent;
      this.dataForm.spokesman = row.broadcastSpokesman;
      this.dataForm.speedSpeech = row.broadcastSpeed;
      this.dataForm.saveRecording = row.isSaveRecording;
      this.dataForm.playbackTimes = row.numberOfBroadcasts;
      this.dataForm.volume = row.volume;
    },
    // handleDelete(index, row) {
    //   for (var i = 0; i < this.tableData.length; i++) {
    //     if (this.tableData[i].broadcastName == row.broadcastName) {
    //       this.tableData.splice(this.tableData.indexOf(this.tableData[i]), 1);
    //     }
    //   }
    // },
    reset() {
      this.dataForm.eqIds = "";
      this.dataForm.eqNames = "";
      this.dataForm.releaseContent = "";
      this.dataForm.spokesman = "";
      this.dataForm.speedSpeech = "";
      this.dataForm.saveRecording = "";
      this.dataForm.playbackTimes = "";
      this.dataForm.volume = "";
      this.$refs.multipleTable.clearSelection();
    },
    //发布
    release() {
      var param = {
        broadcastContent: this.dataForm.releaseContent,
        broadcastSpokesman: this.dataForm.spokesman,
        broadcastSpeed: this.dataForm.speedSpeech,
        isSaveRecording: this.dataForm.saveRecording,
        numberOfBroadcasts: this.dataForm.playbackTimes,
        volume: this.dataForm.volume,
        broadcastEqids: this.dataForm.eqIds,
      };

      this.abled = true;
      //调用发送方法
      this.saveRelaseRecord(param);
      // api
      //   .publishBroadcast(param)
      //   .then((data) => {
      //     if (data == "0") {
      //       this.saveRelaseRecord();
      //     } else {
      //       this.$message({
      //         message: "广播发布失败！",
      //         type: "error",
      //       });
      //     }
      //   })
      //   .catch((err) => {
      //     alert(err);
      //   });
    },
    //保存发布记录
    saveRelaseRecord(param) {
      // let method = "post";
      // //await api.save(this.dataForm, method).then((data) => {
      // api
      //   .save(this.dataForm, method)
      //   .then((data) => {
      //     this.$message({
      //       message: "广播发布成功",
      //       type: "success",
      //     });
      //     this.reset();
      //     this.abled = false;
      //   })
      //   .catch((err) => {
      //     throw err;
      //   });

      console.log(param);
      addRecord(param).then(response => {
        console.log("添加广播记录成功");
      });
      this.$message({
        message: "广播发布成功",
        type: "success",
      });
      this.reset();
      this.abled = false;
    },
    //选择方向
    changeDirection() {
      return;
      api
        .apiTest()
        .then((data) => {})
        .catch((err) => {
          alert(err);
        });
      this.segmentNo = "";
      this.segmentName = "";
    },
    playbackTimesChange(value) {
      if(!value) {
        this.dataForm.playbackTimes = 1
      }
      if(value > 100) {
        this.dataForm.playbackTimes = 100
      }
    },
    volumeChange(value) {
      if(!value) {
        this.dataForm.volume = 1
      }
      if(value > 100) {
        this.dataForm.volume = 100
      }
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-table__empty-block{
  width: auto !important;
}

</style>
<style>

.zhandian {
  color: #f7f7f8;
  font-size: 16px;
  position: absolute;
  width: 90px;
  -webkit-transform: translateX(-20px) rotate(25deg);
  -moz-transform: rotate(90deg);
  filter: progid:DXImageTransform.Microsoft.BasicImage(Rotation=0.45);
  top: 40px;
}

.lastStation {
  position: absolute;
  top: 40px;
  left: 100%;
}

.xianlu {
  width: 100%;
  height: 8px;
  background-color: #888888;
  position: relative;
  margin-top: 10px;
  cursor: pointer;
}

.xianlu:hover {
  background-color: #00aa00;
}

.form-style-class {
  font-size: 14px;
  font-weight: bold;
  float: left;
  padding-top: 5px;
  width: 80px;
  color: rgb(255, 255, 255) !important;
}

/* 路段名称 --end */
.textareacls > .el-form-item__content {
  margin-left: 0px !important;
}
.broadcastTemplate{overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
</style>
