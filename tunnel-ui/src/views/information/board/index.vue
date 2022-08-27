<style lang="scss" scoped>
.el-form-item__label {
  width: auto !important;
}

.el-button--medium {
  padding: 5px 10px;
}
</style>
<style>

.show_content {
  position: relative;
  overflow: hidden;
  word-break: keep-all;
  white-space: nowrap;
  text-overflow: ellipsis;
  background: url(../../../assets/image/qbaoban.png) no-repeat center center;
}

.field {
  width: 98% !important;
}

.el-dialog__body {
  padding: 10px 20px 10px 20px;
}

.topBg {
  background-color: #BFCBD9;
}

fieldset {
  padding: 10px;
  margin: 10px;
  width: 960px;
  color: #333;
  border: #06c dashed 1px;
}

legend {
  color: #06c;
  font-weight: 800;
  background: #fff;
  border: #b6b6b6 solid 1px;
  padding: 3px 6px;
}

.marquee {
  width: 95%;
  overflow: hidden;
  white-space: nowrap;
  box-sizing: border-box;
  animation: marquee 30s linear infinite;
}

.marquee:hover {
  animation-play-state: paused
}

@keyframes marquee {
  0% {
    text-indent: 27.5em
  }

  100% {
    text-indent: -105em
  }
}

.microsoft {
  padding-left: 1.5em;
  position: relative;
  font: 16px 'Segoe UI', Tahoma, Helvetica, Sans-Serif;
}
</style>
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="桩号" prop="pileNumber">
        <el-input v-model="queryParams.pileNumber" placeholder="请输入桩号" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="deviceList" max-height="630" @selection-change="handleSelectionChange">
      <el-table-column label="设备名称" align="center" prop="deviceName"/>
      <el-table-column label="桩号" align="center" prop="pileNumber"/>
      <el-table-column label="路段方向" align="center" prop="routeDirection"/>
      <el-table-column label="设备状态" align="center" prop="deviceStatus">
        <template slot-scope="scope">
          <el-tag :type="scope.row.deviceStatus == 0?'success':'info'">
            {{ deviceStatusFormat(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openQbbDrawer(scope.row)">实时</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openCrkDrawer(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 修改情报板内容 -->
    <el-dialog title="修改情报板" :visible.sync="open" width="1050px" append-to-body>
      <el-form ref="form1" :model="crkForm" :rules="rules" label-width="80px">
        <fieldset class="field" style="height:20vh;position:relative;">
          <legend>显示内容</legend>
          <div id='top_contents' style="overflow-y: auto; height: 73%;border: solid 1px #5e96a3;">
            <el-row v-for="(item,index) in disContentList" :class="{topBg:item.itemId==isactive}" :id="item.itemId"
                    style="cursor:pointer;margin-bottom: 5px;">
              <el-col :span="1" style="padding-left: 5px;" @click.native="infosRowClick(item)">{{ index + 1 }}</el-col>
              <el-col :span="21" @click.native="infosRowClick(item)">{{ item.disContent }}</el-col>
              <el-col :span="2">
                <el-button type="danger" plain @click="delCurrRow(item,index)">删除</el-button>
              </el-col>
            </el-row>
          </div>
          <el-row style="position:absolute;bottom:8px;left:200px;">
            <el-button type="primary" plain @click="addCurrRow">添加</el-button>
            <el-button type="info" plain @click="alignment(6)">下对齐</el-button>
            <el-button type="info" plain @click="alignment(5)">上下居中</el-button>
            <el-button type="info" plain @click="alignment(4)">上对齐</el-button>
            <el-button type="info" plain @click="alignment(3)">右对齐</el-button>
            <el-button type="info" plain @click="alignment(2)">左右居中</el-button>
            <el-button type="info" plain @click="alignment(1)">左对齐</el-button>
          </el-row>
        </fieldset>
        <fieldset class="field" style="height:22vh;">
          <legend>内容预览</legend>
          <div id='middle_contents' v-loading="loadingDialog"></div>
        </fieldset>
        <fieldset class="field" style="height:9vh;">
          <legend>亮度设置</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="调节方式:">
                <el-select id="selectFontFamily" name="selectFontFamily" style="width:6vw;" v-model="lightValue.mode">
                  <el-option value="" label="请选择"></el-option>
                  <el-option value="0" label="自动调节"></el-option>
                  <el-option value="1" label="手动调节"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="亮度值:">
                <el-select style="width:5.6vw;" v-model="lightValue.brightness">
                  <el-option value="" label="请选择"></el-option>
                  <el-option v-for="item in selectLightNumList" :value="item.value" :label="item.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" style="margin-top: 5px;" plain :loading="loadingLight" @click="readLight">读取亮度
              </el-button>
              <!-- <el-button type="primary" style="margin-top: 5px;" disabled plain @click="setLight">设置亮度</el-button> -->
            </el-col>
          </el-row>
        </fieldset>
        <fieldset class="field" style="height:22vh;margin-top: 0.1px;">
          <legend>内容编辑</legend>
          <el-row type="flex">
            <el-col :span="3">
              <span style="margin-right:5px;">字体:</span>
              <el-select v-model="selectFontFamily" @change="boardAttrInfoChange" style="width:80px;">
                <el-option value="s" selected="selected" label="宋体"></el-option>
                <el-option value="k" label="楷体"></el-option>
                <el-option value="h" label="黑体"></el-option>
                <el-option value="f" label="仿宋"></el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <span style="margin-right:5px;">大小:</span>
              <el-select v-model="selectFontSize" @change="boardAttrInfoChange" style="width:80px;">
                <el-option value="4848" selected="selected" label="48px"></el-option>
                <el-option value="4040" label="40px"></el-option>
                <el-option value="3232" label="32px"></el-option>
                <el-option value="2424" label="24px"></el-option>
                <el-option value="1616" label="16px"></el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <span style="margin-right:5px;">颜色:</span>
              <el-select v-model="selectFontColor" @change="boardAttrInfoChange" style="width:80px;">
                <el-option value="255255000000" selected="selected" label="黄色"></el-option>
                <el-option value="255255255000" label="白色"></el-option>
                <el-option value="255000000000" label="红色"></el-option>
                <el-option value="000255000000" label="绿色"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <span style="margin-right:5px;">速度:</span>
              <el-input disabled="disabled" @change="boardAttrInfoChange" v-model="selectInOutScreenSpeed" value="1"
                        style="width: 80px;"/>
            </el-col>
            <el-col :span="4">
              <span style="margin-right:5px;">停留时间:</span>
              <el-input v-model="selectResTime" @change="boardAttrInfoChange" maxlength="4" value="500"
                        style="width:80px;"/>
            </el-col>
            <el-col :span="7">
              <span style="margin-right:5px;">出屏方式:</span>
              <el-select v-model="selectOutScreenMode" @change="boardAttrInfoChange" class="boardAttrInfoChange"
                         style="width: 200px;">
                <el-option value="0" selected="selected" label="清屏(全黑)"></el-option>
                <el-option value="1" label="立即显示"></el-option>
                <el-option value="2" label="上移"></el-option>
                <el-option value="3" label="下移"></el-option>
                <el-option value="4" label="左移"></el-option>
                <el-option value="5" label="右移"></el-option>
                <el-option value="6" label="横百叶窗"></el-option>
                <el-option value="7" label="竖百叶窗"></el-option>
                <el-option value="8" label="上下合拢"></el-option>
                <el-option value="9" label="上下展开"></el-option>
                <el-option value="10" label="左右合拢"></el-option>
                <el-option value="11" label="左右展开"></el-option>
                <el-option value="12" label="中心合拢"></el-option>
                <el-option value="13" label="中心展开"></el-option>
                <el-option value="14" label="向下马赛克"></el-option>
                <el-option value="15" label="向右马赛克"></el-option>
                <el-option value="16" label="淡入"></el-option>
                <el-option value="17" label="淡出"></el-option>
                <el-option value="18" label="字符闪烁(闪后消失)"></el-option>
                <el-option value="19" label="字符闪烁(闪后停留)"></el-option>
                <el-option value="20" label="区域闪烁(闪后复原)"></el-option>
                <el-option value="21" label="区域闪烁(闪后区域为黑)"></el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col>
              <span style="margin-right:5px;">起始坐标:</span>
              x:
              <el-input v-model="startCoordTxt_x" maxlength="3" @change="boardAttrInfoChange" style="width: 60px;"/>
              y:
              <el-input v-model="startCoordTxt_y" maxlength="3" @change="boardAttrInfoChange" style="width: 60px;"/>
              <span style="margin:0px 5px 0px 15px;">内容:</span>
              <el-input type="textarea" v-model="showContentArea" @change="boardAttrInfoChange" value="欢迎拨打齐鲁交通热线"
                        style="width: 670px;"/>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="20">
              <el-input v-model="boardEidtContentArea" disabled="disabled"
                        style="width:800px;overflow-y: auto;resize: none;"></el-input>
            </el-col>
            <el-col :span="4">
              <el-button style="margin-top: 5px;" type="primary" plain v-bind:disabled="saveBtnDisabled"
                         @click="saveBtnClick()">保存
              </el-button>
            </el-col>
          </el-row>
        </fieldset>
        <el-form-item style="width: 100%;margin-bottom:0px;">
          <el-button id="boardPublishBtn" v-bind:disabled="boardPublishBtnDisabled"
                     style="width: 30%;margin-left: 100px;" type="primary" @click="submitmaterialForm"
                     :loading="dloading">发布
          </el-button>
          <el-button style="width: 30%;" @click="boardFormClose">关闭</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="情报板实时信息" :visible.sync="open1" width="1050px" append-to-body>
      <el-form ref="form2" :model="qbbForm" :rules="rules" label-width="80px">
        <fieldset class="field" style="height:150px;margin-top: 0.1px;position:relative;">
          <legend>监控信息</legend>
          <el-row style="padding-top: 1vh;padding-left: 2vw;">
            <el-col :span="12">
              <el-form-item label="设备桩号:">
                <label>{{ qbbForm.pileNumber }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分辨 率 :">
                <label>{{ qbbForm.pixel }}</label>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="padding-left: 2vw;">
            <el-col :span="12">
              <el-form-item label="I P地址:">
                <label>{{ qbbForm.ip }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端  口:">
                <label>{{ qbbForm.port }}</label>
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
        <fieldset class="field" style="height:200px;margin-top: 0.1px;position:relative;">
          <legend>展示内容</legend>
          <div style='width: 100%;height:100%;display:flex;justify-content:center;align-items:center;'>
            <div class='show_content' :style='realInfoStyle'>
              <div :style="realContentStyle" v-html='realContent' v-loading="loadingDialog"></div>
            </div>
          </div>
        </fieldset>
        <fieldset class="field" style="height:300px;margin-top: 0.1px;position:relative;">
          <legend>全部内容</legend>
          <div style="height: 100%;">
            <el-row style="padding-top: 2vh;padding-left: 2vw;font-weight: bold;"
                    v-for="(item,index) in allContentList">
              <el-col :span="1">{{ index + 1 + ":" }}</el-col>
              <el-col :span="23">{{ item.disContent }}</el-col>
            </el-row>
          </div>
        </fieldset>
      </el-form>
    </el-dialog>

  </div>
</template>
<script>
import {
  listDevice,
  getBoardInfo,
  getBoardContent,
  getBoardEditInfo,
  getDevice,
  delDevice,
  addDevice,
  updateDevice,
  HashMap,
  uploadBoardEditInfo,
  readBoardLightInfo
} from "@/api/board/informationBoard";

var timer;
var supplier = "";
var that = "";
export default {
  name: "Device",
  data() {
    return {
      loadingDialog: true,
      dloading: false,
      // 遮罩层
      loading: true,
      // 亮度值
      lightValue: {
        mode: "",
        brightness: "",
        state: ""
      },
      loadingLight: false,
      // 选中数组
      ids: [],
      deviceId: "",
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      itemStr: "",
      boardMultiple: "",
      saveBtnDisabled: "",
      // 总条数
      total: 0,
      selectFontFamily: null,
      selectFontSize: null,
      selectFontColor: null,
      selectResTime: null,
      selectOutScreenMode: null,
      selectInOutScreenSpeed: null,
      startCoordTxt_x: null,
      startCoordTxt_y: null,
      showContentArea: null,
      selectLightNumList: [],
      // 设备列表格数据
      deviceList: [],
      boardEidtContentArea: "",
      itemIndex: 0,
      // 弹出层标题
      title: "",
      boardPublishBtnDisabled: false,
      // 是否显示弹出层
      open: false,
      open1: false,
      drawer: false,
      drawer1: false,
      direction: 'rtl',
      supplier: null,
      itemEditCoordinate: null,
      itemEditFont: null,
      itemEditFontSize: null,
      itemEditColor: null,
      itemEditContents: null,
      itemEditContent: null,
      boardContentS: null,
      // 运行状态字典
      //设备类型
      eqTypeData: {},
      runStatusOptions: [],
      disContentList: [],
      allContentList: [],
      isactive: 0,
      // 设备状态(0:正常  1:离线 )字典
      deviceStatusOptions: [],
      // 是否监控 0不监控 1监控字典
      isMonitorOptions: [],
      itemPropertyMap: null,
      transSign: [
        ["C", "f", "c", "", "\\\\n"],
        ["C", "F", "T", "U", "\\\\N"],
        ["C", "F", "T", "W", "\\\\A"],
        ["C", "f", "c", "", "\\\\n"]
      ],
      itemMap: null,
      itemMapIndex: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        deviceName: null,
        brandId: null,
        routeId: null,
        routeDirection: null,
        deviceMarkingName: null,
        deviceTypeNumber: null,
        deviceTypeId: null,
        deviceModelId: null,
        factoryLibrary: null,
        userUnitId: null,
        operatorId: null,
        longitude: null,
        latitude: null,
        firmId: null,
        purchaseDate: null,
        warrantyYears: null,
        unitPrice: null,
        runStatus: null,
        deviceStatus: null,
        installDate: null,
        maintainId: null,
        collarAgencyId: null,
        pileNumber: null,
        manageAgencyId: null,
        producteDate: null,
        repairDate: null,
        operateDate: null,
        serviceLife: null,
        storageDate: null,
        isMonitor: null,
        isConfig: null,
        localInfo: null,
      },
      realInfoStyle: {
        width: '960px',
        height: '96px',
        backgroundSize: '960px 96px',
        fontSize: '16px',
        lineHeight: '1.0'
      },
      realContentStyle: {
        position: 'static',
      },
      realContent: "",
      // 表单参数
      form: {},
      form1: {},
      form2: {},
      //修改情报板表单参数
      crkForm: {
        /* materialId:null, */
      },
      qbbForm: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    let eqId = this.$route.query.id
    if (eqId != undefined) {
      this.queryParams.deviceId = eqId
    } else {
      this.queryParams.deviceId = ""
    }
    this.getList();
    /* this.getEqType(); */
    this.getDicts("iot_running_state").then(response => {
      this.runStatusOptions = response.data;
    });
    this.getDicts("iot_equipment_state").then(response => {
      this.deviceStatusOptions = response.data;
    });
    this.getDicts("iot_monitor_state").then(response => {
      this.isMonitorOptions = response.data;
    });
  },
  methods: {
    //打开修改情报板框
    openCrkDrawer(row) {
      this.open = true;
      this.deviceId = row.deviceId;
      this.showEditModal(row.deviceId);
    },
    // 获取情报板基础信息
    getboardInfoFun(deviceId) {
      this.qbbForm = {};
      getBoardInfo(deviceId).then(response => {
        this.qbbForm = response.data;
        // 加载情报板的宽高
        var boardSize = this.qbbForm.pixel;
        var boardWidth = 0;
        var boardHeight = 0;
        var boardMultipleModify = 1;
        if ((!boardSize) || (boardSize == "")) {
          boardWidth = 960;
          boardHeight = 96;
        } else {
          // 情报板宽度和高度
          boardWidth = boardSize.split("*")[0];
          boardHeight = boardSize.split("*")[1];
        }
        boardWidth = boardWidth * boardMultipleModify;
        boardHeight = boardHeight * boardMultipleModify;
        // 切换情报板宽高
        this.realInfoStyle.width = boardWidth + 'px'
        this.realInfoStyle.height = boardHeight + 'px'
        this.realInfoStyle.backgroundSize = boardWidth + 'px ' + boardHeight + 'px'
      });
    },
    alignment(alignmentNum) {
      // 1、获取情报板的大小。
      var boardSize = this.qbbForm.pixel;
      if ((!boardSize) || (boardSize == "")) {
        boardSize = "960*96";
      }
      // 情报板宽度和高度
      var width = boardSize.split("*")[0];
      var height = boardSize.split("*")[1];
      // 根据情报板的高度确定显示倍数boardMultiple
      var boardMultiple = this.boardMultiple;
      width = width * boardMultiple;
      height = height * boardMultiple;
      // 2、获取字体大小。
      var fontSize = Number(this.selectFontSize.substring(0, 2)) * boardMultiple;
      // 3、获取内容。
      var content = this.showContentArea;
      // 4、获取行数
      var childrenDivObj = document.getElementById("showinfo_eidt_preview").children;
      switch (alignmentNum) {
        // 左对齐
        case 1:
          this.startCoordTxt_x = "000";
          this.boardAttrInfoChange();
          break;
        // 左右居中
        case 2:
          // 获取文字长度
          var divWidth = childrenDivObj[0].offsetWidth / boardMultiple;
          var coorX = this.formatNum((width / boardMultiple - divWidth) / 2 < 0 ? 0 : (width / boardMultiple -
              divWidth) /
            2, 3);
          this.startCoordTxt_x = coorX;
          this.boardAttrInfoChange();
          break;
        // 右对齐
        case 3:
          // 获取文字长度
          var divWidth = childrenDivObj[0].offsetWidth / boardMultiple;
          var coorX = this.formatNum((width / boardMultiple - divWidth) < 0 ? 0 : (width / boardMultiple - divWidth),
            3);
          this.startCoordTxt_x = coorX;
          this.boardAttrInfoChange();
          break;
        // 上对齐
        case 4:
          this.startCoordTxt_y = "000";
          this.boardAttrInfoChange();
          break;
        // 上下对齐
        case 5:
          // 获取文字长度
          var coord = 0;
          var divHeight = 0;
          var coorY = 0
          divHeight = childrenDivObj[0].offsetHeight / boardMultiple;
          coorY = this.formatNum((height / boardMultiple - divHeight) / 2 < 0 ? 0 : (height / boardMultiple -
              divHeight) /
            2, 3);
          this.startCoordTxt_y = coorY;
          this.boardAttrInfoChange();
          break;
        // 下对齐
        case 6:
          // 获取文字长度
          var coord = 0;
          var divHeight = 0;
          var coorY = 0
          divHeight = childrenDivObj[0].offsetHeight / boardMultiple;
          coorY = this.formatNum((height / boardMultiple - divHeight) < 0 ? 0 : (height / boardMultiple - divHeight),
            3);
          this.startCoordTxt_y = coorY;
          this.boardAttrInfoChange();
          break;
      }
    },
    getContentInfo() {
      var content = this.boardEidtContentArea;
      content = content.replace(/\\C.{6}/, "").trim();
      content = content.replace(/C.{6}/, "").trim();
      content = content.replace(/\\B.{3}/, "").trim();
      content = content.replace(/\\y.{1}/, "").trim();
      content = content.replace(/c.{12}/, "").trim();
      content = content.replace(/\\b.{12}/, "").trim();
      content = content.replace(/\\s.{12}/, "").trim();
      content = content.replace(/\\S.{2}/, "").trim();
      content = content.replace(/S.{2}/, "").trim();
      content = content.replace(/f.{5}/, "").trim();
      content = content.replace(/\\r.{12}/, "").trim();
      content = content.replace(/\\K.{12}/, "").trim();
      content = content.replace(/b.{12}/, "").trim();
      content = content.replace(/\\F.{6}/, "").trim();
      content = content.replace(/T.{12}/, "").trim();
      content = content.replace(/\\M.{2}/, "").trim();
      content = content.replace(/\\W/, "").trim();
      content = content.replace(/\\\\n/, "").trim();
      content = content.replace(/n/, "").trim();
      content = content.replace(/N/, "").trim();
      content = content.replace(/\\A/, "").trim();
      content = content.replace(/A/, "").trim();
      content = content.replace(/\\/, "").trim();
      content = content.replace(/\\N.{2}/, "").trim();
      if (content.indexOf('\\') != -1 && content.indexOf(',') != -1 &&
        content.indexOf(',') != content.lastIndexOf(',')) {
        return content.substring(content.indexOf('\\') + 1, content.length);
      }
      return content;
    },
    boardAttrInfoChange() {
      if (this.showContentArea == "" || this.showContentArea == undefined || this.showContentArea == null) {
        this.$message("发布内容不能为空");
        return;
      }
      var boardAttrInfoUpdate = true;
      if (this.checkCoordinates(this.startCoordTxt_x) == false) {
        boardAttrInfoUpdate = false;
      }
      if (this.checkCoordinates(this.startCoordTxt_y) == false) {
        boardAttrInfoUpdate = false;
      }
      if (boardAttrInfoUpdate == true) {
        this.changeApply(); //属性应用
        this.changePreview(); //属性预览
      }
    },
    changePreview() {
      this.setBoardPreviewContents(this.showContentArea, this.fontValueToDisplay(this.selectFontFamily),
        this.selectFontSize, this.colorValueToDisplay(this.selectFontColor), '',
        this.startCoordTxt_x, this.startCoordTxt_y);
    },
    changeApply() {
      var transSigns = '';
      var selectOutScreenMode = 1;
      var itemEditInfo = this.boardEidtContentArea;
      itemEditInfo = itemEditInfo.substring(itemEditInfo.indexOf('\\'), itemEditInfo.length);
      if (this.supplier.startsWith('SANSI') || this.supplier.startsWith('GUANGDIAN')) {
        transSigns = this.transSign[0];
        itemEditInfo = this.selectResTime + ',' + this.selectOutScreenMode + ',' + this.selectInOutScreenSpeed + ',' +
          itemEditInfo;
      }
      itemEditInfo = itemEditInfo.replace(transSigns[1] + this.itemEditFont, transSigns[1] + this.selectFontFamily);
      this.itemEditFont = this.selectFontFamily;
      if (this.supplier.startsWith('XIANKE')) {

      } else {
        itemEditInfo = itemEditInfo.replace(this.itemEditFontSize, this.selectFontSize);
        this.itemEditFontSize = this.selectFontSize;
      }
      itemEditInfo = itemEditInfo.replace(transSigns[2] + this.itemEditColor, transSigns[2] + this.selectFontColor);
      this.itemEditColor = this.selectFontColor;
      /* var coordObj = $("#coordXY1").next(); */
      var contentArr = new Array();
      var coordinateArr = new Array();
      for (var i = 0; i < this.itemEditContent.length; i++) {
        if (i == 0) {
          itemEditInfo = itemEditInfo.replace(this.itemEditContent[i].replace(/\n/g, "\\n"), this.showContentArea
            .replace(
              /\n/g, "\\n"));
          contentArr.push(this.showContentArea.replace(/\n/g, "\\n"));
          itemEditInfo = itemEditInfo.replace(transSigns[0] + this.itemEditCoordinate[i], transSigns[0] +
            this.formatNum(this.startCoordTxt_x, 3) +
            this.formatNum(this.startCoordTxt_y, 3));
          coordinateArr.push(this.formatNum(this.startCoordTxt_x, 3) + this.formatNum(this.startCoordTxt_y, 3));
        } else {
          /* if (!coordObj.attr('id')) {
            itemEditInfo = itemEditInfo.replace("\\" + transSigns[0] + this.itemEditCoordinate[i], "");
            itemEditInfo = itemEditInfo.replace(itemEditContent[i], "");
          } */
        }
      }
      this.itemEditContent = contentArr;
      this.itemEditCoordinate = coordinateArr;
      this.boardEidtContentArea = itemEditInfo;
    },
    checkCoordinates(val) {
      if (val == '')
        return true;
      if (!/^[0-9]\d*$/.test(val)) {
        this.$message("坐标为0~3位的整数");
        return false;
      }
      return true;
    },
    // 情报板修改页面展示
    showEditModal(ids) {
      that = this
      this.lightValue = {
        mode: "",
        brightness: "",
        state: ""
      };
      this.loadingDialog = true;
      // 发布按钮设置disabled
      this.boardPublishBtnDisabled = true;
      // 清空情报板列表内容
      this.disContentList = [];
      // 清空情报板内容
      var middleContents = document.getElementById("middle_contents");
      if (middleContents) {
        document.getElementById("middle_contents").innerHTML = "<div id = 'detail_eidt_preview' ></div>";
      }
      // 编辑区域属性初始化
      this.contentPropertyEditInit();
      // 清空情报板命令内容
      this.boardEidtContentArea = "";
      this.itemPropertyMap = new HashMap();
      var deviceId = {};
      deviceId.deviceId = ids;
      // 获取情报板基础信息
      this.getboardInfoFun(deviceId);
      // 获取情报板修改页面信息
      getBoardEditInfo(deviceId).then(response => {
        this.loadingDialog = false;
        if (response.code != 200) {
          this.$message(response.msg);
          return;
        }
        if (response.data[0] == undefined) {
          this.$message(response.msg);
          return;
        }
        var parseObject = JSON.parse(response.data[0]);
        var protocolType = parseObject.support.PROTOCOL_TYPE;
        var contents = parseObject.content;
        if (typeof (contents) == 'undefined' || typeof (protocolType) == 'undefined') {
          this.$message(response.msg);
          return;
        }
        this.supplier = protocolType;
        var currRowId = "";
        var reg = /,/g;
        for (var i = 0; i < contents.length; i++) {
          var content = contents[i];
          var itemId = 'ITEM' + this.formatNum(i, 3);
          if (i == 0) {
            currRowId = itemId;
          }
          var con = content[itemId];
          this.itemStr = '';

          for (var j = 0; j < con.length; j++) {
            this.itemStr = this.combineItemContent(protocolType, con[j].STAY, con[j].ACTION, con[j].SPEED, con[j]
                .COORDINATE,
              this.getFontValue(con[j].FONT), con[j].FONT_SIZE, this.getColorValue(con[j].COLOR), con[j].CONTENT
                .replace(reg, '，').replace(/<br>/g, "\\n"));
            this.addItemPropertyMap(itemId, false, con[j].STAY, con[j].ACTION, con[j].SPEED, con[j].COORDINATE,
              con[j].FONT, con[j].FONT_SIZE, con[j].COLOR, con[j].CONTENT.replace(reg, '，').replace(/<br>/g,
                "\n"));
          }
          this.addContentDisplayInfos('ITEM' + this.formatNum(i, 3), i + 1, this.itemStr);
        }
        this.isactive = currRowId;
        this.infosRowClick(this.disContentList[0]);
      }).catch(function (error) {
        that.disContentList = [];
        that.loadingDialog = false;
        // this.$message(error);
      })
    },
    formatNum(num, length) {
      return (Array(length).join('0') + parseInt(num)).slice(-length);
    },
    getFontValue(font) {
      if (font == '黑体')
        return 'h';
      if (font == '楷体')
        return 'k';
      if (font == '仿宋')
        return 'f';
      if (font == '隶书')
        return 'l';
      return 's';
    },
    fontValueToDisplay(font) {
      if (font == 'h')
        return '黑体';
      if (font == 'k')
        return '楷体';
      if (font == 'f')
        return '仿宋';
      if (font == 'l')
        return '隶书';
      return '宋体';
    },
    getColorValue(color) {
      if (color == '蓝色')
        return '000000255000';
      if (color == '绿色')
        return '000255000000';
      if (color == '透明色')
        return 't';
      if (color == '红色')
        return '255000000000';
      return '255255000000'; //黄色
    },
    colorValueToFont(color) {
      if (color.toString() == '000000255000')
        return '蓝色';
      if (color.toString() == '000255000000')
        return '绿色';
      if (color.toString().toLowerCase() == 't')
        return '透明色';
      if (color.toString() == '255000000000')
        return '红色';
      return '黄色'; //黄色
    },
    colorValueToDisplay(colorType) {
      if (colorType.toString() == '255255255000') {
        return 'white';
      } else if (colorType.toString() == '255000000000') {
        return 'red';
      } else if (colorType.toString() == '000000255000') {
        return 'blue';
      } else if (colorType.toString() == '255000000' || colorType.toString() == '000255000000') {
        return '#bbffd7';
      }
      return 'yellow';
    },
    combineItemContent(protocolType, stay, action, speed, coordinate, font, font_size, color, content) {
      if (protocolType.startsWith('SANSI') || protocolType.startsWith('GUANGDIAN')) {
        if (this.itemStr.indexOf(stay) == -1 || this.itemStr.indexOf(action) == -1 || this.itemStr.indexOf(speed) ==
          -1) {
          if (protocolType.startsWith('GUANGDIAN'))
            this.itemStr = stay + ',' + action + ',' + speed + ',';
          else
            this.itemStr = stay + ',' + action + ',' + speed;
        }
        if (this.itemStr.indexOf(coordinate) == -1 && typeof (coordinate) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[0][0] + coordinate;
        if (this.itemStr.indexOf(font + font_size) == -1 && typeof (font) != 'undefined' && typeof (font_size) !=
          'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[0][1] + font + font_size;
        if (this.itemStr.indexOf(color) == -1 && typeof (color) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[0][2] + color;
        this.itemStr = this.itemStr + this.transSign[0][3] + content;
      } else if (protocolType.startsWith('XIANKE')) {
        if (this.itemStr.indexOf(stay) == -1 || this.itemStr.indexOf(action) == -1 || this.itemStr.indexOf(speed) ==
          -1)
          this.itemStr = stay + ',' + action + ',' + '0,' + '1,' + speed + ',';
        if (this.itemStr.indexOf(coordinate) == -1 && typeof (coordinate) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[1][0] + coordinate;
        if (this.itemStr.indexOf(font + font_size) == -1 && typeof (font) != 'undefined' && typeof (font_size) !=
          'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[1][1] + font + font_size;
        if (this.itemStr.indexOf(color) == -1 && typeof (color) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[1][2] + color;
        this.itemStr = itemStr + '\\' + this.transSign[1][3] + content;
      } else if (protocolType.startsWith('DIANMING') || protocolType.startsWith('TONGZHOU')) {
        if (this.itemStr.indexOf(stay) == -1 || this.itemStr.indexOf(action) == -1 || this.itemStr.indexOf(speed) ==
          -1)
          this.itemStr = stay + ',' + action + ',' + '0,' + '0,' + speed + ',';
        if (this.itemStr.indexOf(coordinate) == -1 && typeof (coordinate) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[2][0] + coordinate;
        if (this.itemStr.indexOf(font + font_size) == -1 && typeof (font) != 'undefined' && typeof (font_size) !=
          'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[2][1] + font + font_size;
        if (this.itemStr.indexOf(color) == -1 && typeof (color) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[2][2] + color;
        this.itemStr = this.itemStr + '\\' + this.transSign[2][3] + content;
      } else if (protocolType.startsWith('DINGEN')) {
        if (this.itemStr.indexOf(stay) == -1 || this.itemStr.indexOf(action) == -1 || this.itemStr.indexOf(speed) ==
          -1)
          this.itemStr = stay + ',' + action + ',' + '0,' + '1,' + speed;
        if (this.itemStr.indexOf(coordinate) == -1 && typeof (coordinate) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[3][0] + coordinate;
        if (this.itemStr.indexOf(font + font_size) == -1 && typeof (font) != 'undefined' && typeof (font_size) !=
          'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[3][1] + font + font_size;
        if (this.itemStr.indexOf(color) == -1 && typeof (color) != 'undefined')
          this.itemStr = this.itemStr + '\\' + this.transSign[3][2] + color;
        this.itemStr = this.itemStr + '\\' + this.transSign[3][3] + content;
      }
      return this.itemStr;
    },
    //情报板添加内容
    addCurrRow() {
      this.boardPublishBtnDisabled = true;
      var newId = 'ITEM000';
      var no = 1;
      if (this.disContentList.length > 0) {
        // 获取最后一个
        var lastElemId = this.disContentList[this.disContentList.length - 1].itemId;
        var lastId = lastElemId.substring(5, 7);
        newId = 'ITEM' + this.formatNum(parseInt(lastId) + 1, 3);
      }
      var defalutContent = '';
      defalutContent = '300,0,0,\\C000000\\fh3232\\c255255000000请修改显示内容';
      this.addItemPropertyMap(newId, true, '300', '0', '0', '000000', '黑体', '3232', '黄色', '请修改显示内容');
      this.addContentDisplayInfos(newId, "", defalutContent); //添加一条默认信息
      /* var scrollHeight = this.jquery('#top_contents').prop("scrollHeight");
      this.jquery('#top_contents').scrollTop(scrollHeight, 200); // 滚动条移至最底部 */
      var item = {};
      item.itemId = newId;
      item.disContent = defalutContent;
      this.infosRowClick(item);
    },
    // 向内容显示区域添加一条信息
    addContentDisplayInfos(addId, no, disContent) {
      var jsonArr = {};
      jsonArr.itemId = addId;
      jsonArr.disContent = disContent;
      this.disContentList.push(jsonArr);
    },
    infosRowClick(item) {

      var itemId = item.itemId;
      var disContent = item.disContent;
      this.isactive = itemId;
      this.boardEidtContentArea = disContent;
      this.itemEditCoordinate = this.itemPropertyMap.get(itemId + '_COORDINATE');
      this.itemEditFont = this.getFontValue(this.itemPropertyMap.get(itemId + '_FONT')[0]);
      this.itemEditFontSize = this.itemPropertyMap.get(itemId + '_FONT_SIZE')[0];
      this.itemEditColor = this.getColorValue(this.itemPropertyMap.get(itemId + '_COLOR')[0]);
      var itemEditContents = this.itemPropertyMap.get(itemId + '_CONTENT');
      this.boardContentS = itemEditContents;
      this.itemEditContent = [];
      for (var i = 0; i < itemEditContents.length; i++) {
        this.itemEditContent.push(itemEditContents[i]);
      }
      var content = '';
      var contentArr = this.itemPropertyMap.get(itemId + '_CONTENT');
      for (var i = 0; i < contentArr.length; i++) {
        content = content + contentArr[i];
      }
      var font = this.itemPropertyMap.get(itemId + '_FONT')[0];
      var fontSize = this.itemPropertyMap.get(itemId + '_FONT_SIZE')[0];
      var color = this.itemPropertyMap.get(itemId + '_COLOR')[0];
      this.setContentEditInfos(this.itemPropertyMap.get(itemId + '_STAY')[0],
        this.itemPropertyMap.get(itemId + '_ACTION')[0],
        this.itemPropertyMap.get(itemId + '_SPEED')[0],
        this.itemPropertyMap.get(itemId + '_COORDINATE')[0],
        this.getFontValue(font), fontSize, this.getColorValue(color), content);
      this.setBoardPreviewContents(content, font, fontSize,
        this.colorValueToDisplay(this.getColorValue(color)), '',
        this.startCoordTxt_x, this.startCoordTxt_y);
    },
    setBoardPreviewContents(content, fontStyle, fontSize, color, fontSpace, marginX, marginY) {
      fontSize = fontSize.length > 2 ? parseInt(fontSize.toString().substring(0, 2)) : parseInt(fontSize);
      // 情报板坐标
      if (!marginX) {
        marginX = 0;
      } else {
        marginX = Number(marginX);
      }
      if (!marginY) {
        marginY = 0;
      } else {
        marginY = Number(marginY);
      }
      // 获取情报板的大小
      var boardSize = this.qbbForm.pixel;
      var boardWidth = 0;
      var boardHeight = 0;
      var boardMultiple = 1;
      if ((!boardSize) || (boardSize == "")) {
        boardWidth = 960;
        boardHeight = 96;
      } else {
        // 情报板宽度和高度
        boardWidth = boardSize.split("*")[0];
        boardHeight = boardSize.split("*")[1];
        // 根据情报板的高度确定显示倍数boardMultiple
        boardMultiple = 1;
      }
      this.boardMultiple = boardMultiple;
      boardWidth = boardWidth * boardMultiple;
      boardHeight = boardHeight * boardMultiple;
      // 清空情报板
      document.getElementById("middle_contents").innerHTML = "<div id = 'detail_eidt_preview' ></div>";
      content = content.replace(/\n/g, "<br>");
      var appdenDivContent = "<div id='showinfo_eidt_preview' class='show_content' style='width:" + boardWidth +
        "px;line-height:1.0;background-size:" + boardWidth + "px " + boardHeight + "px;height:" + boardHeight +
        "px;font-size:" + fontSize * boardMultiple + "px;font-family:" + fontStyle + ";color:" + color + "'>";
      appdenDivContent += "<div style='position:absolute;left:" + marginX * boardMultiple + "px;top:" + marginY *
        boardMultiple + "px;'>" + content.replace("n", "<br>") + "</div></div>";
      document.getElementById("detail_eidt_preview").innerHTML = appdenDivContent;
      document.getElementById("detail_eidt_preview").style.width = "100%";
      document.getElementById("detail_eidt_preview").style.height = "100%";
      document.getElementById("detail_eidt_preview").style.display = "flex";
      document.getElementById("detail_eidt_preview").style.justifyContent = "center";
      document.getElementById("detail_eidt_preview").style.alignItems = "center";
      document.getElementById("detail_eidt_preview").style.marginTop = "calc((18vh - " + boardHeight + "px) / 2)";
      this.saveBtnDisabled = false;
    },
    // 清空情报板下方内容
    contentPropertyEditInit() {
      this.selectResTime = "";
      this.selectInScreenMode = "",
        this.selectInOutScreenSpeed = "";
      this.selectOutScreenMode = "";
      this.selectLightNumList = [];
      this.selectFontFamily = "";
      this.selectFontSize = "";
      this.selectFontColor = "";
      this.startCoordTxt_x = "000";
      this.startCoordTxt_y = "000";
      this.showContentArea = "";
      for (var i = 1; i <= 31; i++) {
        if (i < 10) {
          var json = {};
          json.value = "0" + i;
          json.name = i;
          this.selectLightNumList.push(json);
        } else {
          var json = {};
          json.value = i;
          json.name = i;
          this.selectLightNumList.push(json);
        }
      }
    },
    setContentEditInfos(stay, action, speed, coordinate, font, font_size, color, content) {
      this.contentPropertyEditInit(); // 编辑区域属性初始化
      this.selectFontFamily = font; //字体
      this.selectFontSize = font_size.length == 4 ? font_size : font_size + font_size; //字体大小
      this.selectFontColor = color; //字体颜色
      if (coordinate == undefined) {
        this.startCoordTxt_x = "000";
        this.startCoordTxt_y = "000";
      } else {
        if (coordinate.length == 1) {
          this.startCoordTxt_x = coordinate[0].substring(0, 3);
          this.startCoordTxt_y = coordinate[0].substring(3, 6);
        } else if (coordinate.length == 6) {
          this.startCoordTxt_x = coordinate.substring(0, 3);
          this.startCoordTxt_y = coordinate.substring(3, 6);
        }
      }
      this.selectResTime = stay; //停留时间
      this.selectOutScreenMode = action; //出屏方式
      this.selectInOutScreenSpeed = speed; //出入屏速度
      this.showContentArea = content;
    },
    delCurrRow(item, index) {
      this.disContentList.splice(index, 1);
      //this.setBoardPreviewContents('', '', '', '','','','');//清空预览区域
      this.boardEidtContentArea = "";
      // 选中第一条
      if (this.disContentList.length > 0) {
        this.infosRowClick(this.disContentList[0]);
      } else {
        currRowId == '';
      }
    },
    //将item属性存入Map
    addItemPropertyMap(itemId, addFlg, stay, action, speed, coordinate, font, font_size, color, content) {
      if (!action) {
        action = 0;
      }
      this.addPropertyMap(itemId + '_STAY', this.itemPropertyMap, addFlg, stay);
      this.addPropertyMap(itemId + '_ACTION', this.itemPropertyMap, addFlg, action);
      this.addPropertyMap(itemId + '_SPEED', this.itemPropertyMap, addFlg, speed);
      this.addPropertyMap(itemId + '_COORDINATE', this.itemPropertyMap, addFlg, coordinate);
      this.addPropertyMap(itemId + '_FONT', this.itemPropertyMap, addFlg, font);
      this.addPropertyMap(itemId + '_FONT_SIZE', this.itemPropertyMap, addFlg, font_size);
      this.addPropertyMap(itemId + '_COLOR', this.itemPropertyMap, addFlg, color);
      this.addPropertyMap(itemId + '_CONTENT', this.itemPropertyMap, addFlg, content);
    },
    addPropertyMap(itemIdStr, map, addFlg, propertyStr) {
      var array = map.get(itemIdStr);
      if (array == null || addFlg) array = new Array();
      array.push(propertyStr);
      map.put(itemIdStr, array);
    },
    //打开查看情报板设备框
    openQbbDrawer(row) {
      that = this;
      this.loadingDialog = true
      clearInterval(timer); // 关闭定时
      // 打开页面
      this.open1 = true;
      // 清空全部内容列表
      this.allContentList = "";
      this.realContent = "";
      this.realContentStyle.position = 'static';
      // 加载基础信息
      this.getboardInfoFun({
        deviceId: row.deviceId
      });
      // 获取情报板内容
      getBoardContent({
        deviceId: row.deviceId
      }).then(response => {
        this.loadingDialog = false
        if (response.code == 200) {
          var parseObject = "";
          if (response.data[1] != "") {
            parseObject = JSON.parse(response.data[0]);
          }
          if (typeof (parseObject) == 'undefined' || parseObject == null) {
            return;
          }
          var contents = parseObject.content;
          if (typeof (contents) == 'undefined') {
            return;
          }
          var fontStyle = "宋体";
          var color = "red";
          this.allContentList = [];
          this.itemMapIndex = contents.length;
          if (this.itemIndex > this.itemMapIndex) {
            this.itemIndex = 0;
          }
          for (var i = 0; i < contents.length; i++) {
            var itemArrays = new Array();
            var itemStr = '';
            var content = contents[i];
            var itemId = 'ITEM' + this.formatNum(i, 3);
            var con = content[itemId];
            for (var j = 0; j < con.length; j++) {
              itemStr = itemStr + con[j].CONTENT;
              itemArrays.push(con[j].COLOR);
              switch (con[j].FONT_SIZE) {
                case "1616":
                  itemArrays.push("16");
                  break;
                case "2424":
                  itemArrays.push("24");
                  break;
                case "3232":
                  itemArrays.push("32");
                  break;
                case "4040":
                  itemArrays.push("40");
                  break;
                case "4848":
                  itemArrays.push("48");
                  break;
                default:
                  itemArrays.push(con[j].FONT_SIZE); //大小
                  break;
              }
              switch (con[j].FONT) {
                case "h":
                  itemArrays.push("黑体");
                  break;
                case "k":
                  itemArrays.push("楷体");
                  break;
                case "f":
                  itemArrays.push("仿宋");
                  break;
                case "s":
                  itemArrays.push("宋体");
                  break;
                case "l":
                  itemArrays.push("隶书");
                  break;
                default:
                  itemArrays.push("楷体");
                  break;
              }

              itemArrays.push(con[j].CONTENT); //文本
              itemArrays.push(con[j].COORDINATE); //坐标
            }
            itemStr = itemStr.replace("<br>", "");
            var json = {};
            json.disContent = itemStr;
            this.allContentList.push(json);
            var type = true;
            if (contents.length > 2) {
              type = false;
            }
          }
          // 板子放上内容
          this.showDisplayInfo(contents);
          // 定时刷新板子内容
          timer = setInterval(() => {
            that.showDisplayInfo(contents);
          }, 6000);
        } else {
          //this.$message.error(response.msg)
        }
      }).catch(function (error) {
        that.loadingDialog = false
      })
    },
    showDisplayInfo(contents) {
      var color = ""
      var size = ""
      var family = ""
      var content = ""
      var contentXY = ""
      var con = contents[this.itemIndex]['ITEM' + this.formatNum(this.itemIndex, 3)];
      for (var j = 0; j < con.length; j++) {
        var itemArrays = new Array();
        color = con[j].COLOR
        switch (con[j].FONT_SIZE) {
          case "1616":
            size = "16";
            break;
          case "2424":
            size = "24";
            break;
          case "3232":
            size = "32";
            break;
          case "4040":
            size = "40";
            break;
          case "4848":
            size = "48";
            break;
          case "2020":
            size = "20";
            break;
          default:
            size = con[j].FONT_SIZE;
            break;
        }
        switch (con[j].FONT) {
          case "h":
            family = "黑体";
            break;
          case "k":
            family = "楷体";
            break;
          case "f":
            family = "仿宋";
            break;
          case "s":
            family = "宋体";
            break;
          case "l":
            family = "隶书";
            break;
          default:
            family = "楷体";
            break;
        }
        contentXY = con[j].COORDINATE //坐标
        content += con[j].CONTENT
      }
      if (contentXY == undefined) {
        var marginX = "000";
        var marginY = "000";
      } else {
        var marginX = contentXY.substring(0, 3);
        var marginY = contentXY.substring(3, 6);
      }
      marginX = parseInt(marginX);
      marginY = parseInt(marginY);
      this.realContent = content.replace("n", "<br>");
      this.realContentStyle.position = 'absolute'
      this.realContentStyle.top = marginY + 'px'
      this.realContentStyle.left = marginX + 'px'
      this.realContentStyle.fontSize = size + 'px'
      this.realContentStyle.fontFamily = family
      this.realContentStyle.color = this.colorValueToDisplay(this.getColorValue(color))

      this.itemIndex = this.itemIndex + 1;
      //如果超过数组长度,itemIndex初始化
      if (this.itemIndex >= this.itemMapIndex) {
        this.itemIndex = 0;
      }
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {
        });
    },
    //关闭情报板设备
    handleCloseBoard(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {
        });
    },
    /** 查询设备列列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 运行状态字典翻译
    runStatusFormat(row, column) {
      return this.selectDictLabel(this.runStatusOptions, row.runStatus);
    },
    // 设备状态(0:正常  1:离线 )字典翻译
    deviceStatusFormat(row, column) {
      return this.selectDictLabel(this.deviceStatusOptions, row.deviceStatus);
    },
    // 是否监控 0不监控 1监控字典翻译
    isMonitorFormat(row, column) {
      return this.selectDictLabel(this.isMonitorOptions, row.isMonitor);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    //重置修改情报板内容
    resetBoard() {
      this.$refs.form1.resetFields()
    },
    /* 读取亮度 */
    readLight() {
      this.loadingLight = true;
      readBoardLightInfo(this.deviceId).then(response => {
        this.loadingLight = false;
        if (response.code == 200) {
          this.msgSuccess("亮度获取成功");
          this.lightValue = response.data;
        } else {
          this.msgError("亮度获取失败");
        }
      });
    },
    /* 设定亮度 */
    setLight() {

    },
    //重置查看情报板设备
    resetDetailBoard() {
      this.$refs.form2.resetFields()
    },
    // 表单重置
    reset() {
      this.form = {
        deviceId: null,
        deviceName: null,
        brandId: null,
        routeId: null,
        routeDirection: null,
        deviceMarkingName: null,
        deviceTypeNumber: null,
        deviceTypeId: null,
        deviceModelId: null,
        factoryLibrary: null,
        userUnitId: null,
        operatorId: null,
        longitude: null,
        latitude: null,
        firmId: null,
        purchaseDate: null,
        warrantyYears: null,
        unitPrice: null,
        runStatus: null,
        deviceStatus: null,
        installDate: null,
        maintainId: null,
        collarAgencyId: null,
        pileNumber: null,
        manageAgencyId: null,
        producteDate: null,
        repairDate: null,
        operateDate: null,
        serviceLife: null,
        storageDate: null,
        isMonitor: null,
        isConfig: null,
        localInfo: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deviceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备列";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const deviceId = row.deviceId || this.ids
      getDevice(deviceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备列";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateDevice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDevice(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /* 保存 */
    saveBtnClick() {
      var editContent = this.getContentInfo();
      this.boardPublishBtnDisabled = false;
      // 更改情报板列表的值
      for (var i = 0; i < this.disContentList.length; i++) {
        var dis = this.disContentList[i];
        if (dis.itemId == this.isactive) {
          this.disContentList[i].disContent = this.boardEidtContentArea;
        }
      }
      this.addItemPropertyMap(this.currRowId, true, this.selectResTime, this.selectOutScreenMode, this
          .selectInOutScreenSpeed,
        this.formatNum(this.startCoordTxt_x, 3) + this.formatNum(this.startCoordTxt_y, 3),
        this.fontValueToDisplay(this.selectFontFamily),
        this.selectFontSize, this.colorValueToFont(this.selectFontColor), this.showContentArea);
    },
    /** 发布 */
    submitmaterialForm() {
      // 询问弹框
      this.$confirm('情报板信息确定发布吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        if(this.dloading) return
        this.dloading = true
        this.boardPublishBtnDisabled = true;
        var d = this.disContentList;
        var content = "";
        var playList = "";
        var Item_Start = "";
        var Item_Content = "";
        playList = "[Playlist]\r\n";
        Item_Start = "ITEM_NO=";
        Item_Content = "ITEM";
        content += playList;
        var length = parseInt(d.length);
        var Item_No = Item_Start + length + "\r\n";
        content += Item_No;
        var b = /\\/gi;
        for (var i = 0; i < d.length - 1; i++) {
          var keyContent = d[i].itemId.replace(/[^0-9]/ig, "");
          var value = Item_Content + keyContent + "=" + d[i].disContent + "\r\n";
          content += value;
        }
        var keyContent = d[d.length - 1].itemId.replace(/[^0-9]/ig, "");
        var value = Item_Content + keyContent + "=" + d[d.length - 1].disContent;
        content += value;
        var contentArr = new Array();
        content = this.delWithItem(content, supplier);
        await uploadBoardEditInfo(this.deviceId, "GUANGDIAN_V33", content).then(response => {
          if (response.code == 200) {
            this.msgSuccess("发布成功");
          } else {
            this.msgError("发布失败");
          }
        });
        this.dloading = false
      });
    },
    delWithItem(content, protocolType) {
      /**var scontentArr = content.split(/[(\r\n)\r\n]+/);
       content = scontentArr[0]+ "\r\n"+ scontentArr[1]+ "\r\n" +scontentArr[2]+ "\r\n"+scontentArr[3];
       for(var i=4;i<scontentArr.length;i++){
        					var item = scontentArr[i];
        					var itemArrs = item.split('=');
        					var items = itemArrs[1].split('\\');
        					var it = '';
        					for(var j=0;j<items.length;j++){
        						var everyItem = items[j];
        						if(isNumber(everyItem.substring(0,1))&&everyItem.indexOf(',')){
        							var propertys = everyItem.split(',');
        							var prefix = '';
        							prefix = propertys[0]+' ,'+propertys[1]+' ,'+propertys[2]+' ,';
        							it = j==0 ? it + prefix:it + '\\'+ prefix;
        						}else{
        							it = it + '\\'+ everyItem;
        						}
        					}
        					content = content + "\r\n" + itemArrs[0]+'='+it;
        				}**/
      return content;
    },
    //关闭drawer
    boardFormClose() {
      this.resetBoard();
      this.open = false;
    },
    //关闭查看的情报板设备
    boardDetailFormClose() {
      this.resetDetailBoard();
      this.open1 = false;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids;
      this.$confirm('是否确认删除选中数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delDevice(deviceIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/information/export', {
        ...this.queryParams
      }, `system_device.xlsx`)
    }
  }
};
</script>
