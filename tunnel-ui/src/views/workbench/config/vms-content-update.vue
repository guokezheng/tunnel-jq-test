<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    width="80%"
    append-to-body
  >
    <el-row :gutter="24">
      <el-col :span="16">
        <h3>播放列表</h3>
        <el-card>
          <content-table
            :option="contentLists"
            @contentArr="saveContentItem"
            ref="contentTable"
            @dataList="waitConfirm"
            @contentList="contentPreview"
            @length0="updata"
          ></content-table>
        </el-card>
        <h3>设备节目单</h3>
        <el-card style="padding: 0;">
          <div
            style="
              background-color: #000000;
              color: yellow;
              margin: 0 auto;
              overflow: hidden;
              position: relative;
            "
            v-bind:style="divStyle"
          >
            <div
              v-for="(res, index) in templateContent"
              v-model="templateContent"
              v-drag
              :id="gpIndex+'---'+index"
              @click="cliTest(res,index)"
              style="
                line-height: 1;
                position: absolute;
                white-space: nowrap;
                z-index: 1000;
              "
              v-show="gpIndex == index"
              :style="{
                color: res.COLOR,
                fontFamily: res.FONT,
                fontSize: res.FONT_SIZE,
                left: res.COORDINATE && res.COORDINATE.substring(0,3) + 'px',
                top: res.COORDINATE && res.COORDINATE.substring(0,3) + 'px',
              }"
              v-html="res.CONTENT"
            ></div>
          </div>
        </el-card>
        <el-card
          class="box-card"
          style="margin-top: 2vh;height:auto;"
        >
          <el-form
            :model="dataForm"
            :rules="dataRule"
            label-width="90px"
            ref="dataForm"
            size="mini"
          >
            <el-row
              :gutter="24"
              v-for="(res, index) in templateContent"
              v-show="res.CONTENT != ''"
            >
              <el-col :span="22">
                <el-form-item label="内容">
                  <el-input
                    @change="test"
                    type="textarea"
                    clearable
                    placeholder="内容"
                    v-model="res.CONTENT"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="delTemplateContent(res)"
                ></el-button>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="COLOR" label="字体颜色">
                  <el-select
                    v-model="res.COLOR"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in colorOptions"
                      :key="item.code"
                      :label="item.content"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="FONT_SIZE" label="字体大小">
                  <el-select v-model="res.FONT_SIZE" style="width: 100%">
                    <el-option
                      v-for="item in fontSizeOpt"
                      :key="item.code"
                      :label="item.content"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="FONT" label="字体类型">
                  <el-select
                    v-model="res.FONT"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in fontTypeOptions"
                      :key="item.code"
                      :label="item.content"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="fontSpacing" label="字间距(px)">
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="res.fontSpacing"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!--            </el-scrollbar>-->
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item prop="rollSpeed" label="滚动速度(s)">
                  <!--          毫秒    -->
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="dataForm.rollSpeed"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="stopTime" label="停留时间(s)">
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="dataForm.stopTime"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="applyType" label="适用类型">
                  <!-- <el-input
                    clearable
                    placeholder="适用类型"
                    v-model="dataForm.applyType"
                  ></el-input> -->
                  <el-select
                    v-model="dataForm.applyType"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in applyTypeOptions"
                      :key="item.code"
                      :label="item.content"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="inScreenMode" label="入屏方式">
                  <el-select
                    v-model="dataForm.inScreenMode"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in inScreenModeOptions"
                      :key="item.code"
                      :label="item.name"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
        <el-card style="margin-top: 20px;">
          <el-button
            size="medium"
            @click="addRelease()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
          新增
          </el-button>
          <el-button
            size="medium"
            @click="saveClose()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
          发布
          </el-button>
        </el-card>
      </el-col>
      <!-- 模板选择 -->
      <el-col :span="8">
        <el-scrollbar style="height: 100vh; width: 100%">
          <h3>模板选择</h3>
          <el-card style="height:100vh;">
            <template v-if="contentTemplate.length != 0">
            <div v-for="res in contentTemplate" :key="res.id">
              <el-col
                :span="8"
                v-if="res.screenSize == '144*72'"
                @dblclick.native="templateUP(res)"
                style="margin-top: 20px"
              >
                <el-card>
                  <div
                    v-bind:style="{
                      color: res.fontColor,
                      fontFamily: res.fontType,
                      fontSize: res.fontSize + 'px',
                      letterSpacing: res.fontSpacing + 'px',
                    }"
                    style="
                      background-color: #000000;
                      height: 72px;
                      overflow: hidden;
                    "
                  >
                    {{ res.content }}
                  </div>
                </el-card>
              </el-col>
              <el-col
                :span="24"
                v-if="res.screenSize == '768*72'"
                @dblclick.native="templateUP(res)"
                style="margin-top: 2px"
              >
                <el-card>
                  <div
                    v-bind:style="{
                      color: res.fontColor,
                      fontFamily: res.fontType,
                      fontSize: res.fontSize + 'px',
                      letterSpacing: res.fontSpacing + 'px',
                    }"
                    style="
                      background-color: #000000;
                      height: 72px;
                      overflow: hidden;
                    "
                  >
                    {{ res.content }}
                  </div>
                </el-card>
              </el-col>
              <el-col
                :span="24"
                v-if="res.screenSize == '1024*128'"
                @dblclick.native="templateUP(res)"
                style="margin-top: 20px; padding-left: 0px; padding-right: 0px"
              >
                <el-card>
                  <div
                    v-bind:style="divStyle1(res.screenSize)"
                    style="
                      background-color: #000000;
                      height: 128px;
                      overflow: hidden;
                      position: relative;
                      width: 100%;
                    "
                    class="first"
                  >
                    <div
                      style="
                        line-height: 1;
                        position: absolute;
                        white-space: nowrap;
                        z-index: 999;
                      "
                      :style="divStyles(res.screenSize, res)"
                      v-html="res.tcontent.content"
                    ></div>
                  </div>
                </el-card>
              </el-col>
              <el-col
                :span="24"
                v-if="res.screenSize == '480*72'"
                @dblclick.native="templateUP(res)"
                style="margin-top: 20px"
              >
                <el-card>
                  <div
                    style="
                      background-color: #000000;
                      height: 128px;
                      overflow: hidden;
                      position: relative;
                      width: 100%;
                    "
                  >
                    <div
                      v-for="(item, index) in res.item"
                      :key="index"
                      style="
                        line-height: 1;
                        position: absolute;
                        white-space: nowrap;
                        z-index: 999;
                      "
                      :style="vmsSizeDiv(res, item)"
                      v-html="item.content"
                    ></div>
                  </div>
                </el-card>
              </el-col>
              <el-col
                :span="12"
                v-if="res.screenSize == '320*32' || res.screenSize == '384*32'"
                @dblclick.native="templateUP(res)"
                style="margin-top: 20px"
              >
                <el-card>
                  <div
                    v-bind:style="{
                      color: res.fontColor,
                      fontFamily: res.fontType,
                      fontSize: res.fontSize + 'px',
                      letterSpacing: res.fontSpacing + 'px',
                      width: res.applyType == '384*32' ? '384px' : '320px',
                    }"
                    style="
                      background-color: #000000;
                      height: 32px;
                      overflow: hidden;
                    "
                  >
                    {{ res.content }}
                  </div>
                </el-card>
              </el-col>
            </div>
            </template>
            <template v-else>
              <el-col
                :span="8"
                style="margin-top: 20px"
              >
              暂无模板数据
              </el-col>
            </template>
          </el-card>
        </el-scrollbar>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script>
// import * as api from "./api";
// import util from "@/libs/util";
import contentTable from "./content-table";
import {
  getDeviceBase,
  getNewBoardEditInfo,
  templateList,
  vocabularyList,
  uploadBoardEditInfo,
} from "@/api/workbench/config";
export default {
  components: {
    contentTable,
  },
  props:[
    'IntelligenceBoardShow',
  ],
  data() {
    return {
      saveData:'',
      divStyle:{},
      fontSizeOpt:[
        {
          code: "24",
          name: "24",
        },
        {
          code: "23",
          name: "23",
        },
        {
          code: "22",
          name: "22",
        },
        {
          code: "21",
          name: "21",
        },
        {
          code: "20",
          name: "20",
        },
        {
          code: "19",
          name: "19",
        },
        {
          code: "18",
          name: "18",
        }
      ],
	    fontTypeOptions: [],
      applyTypeOptions: [],
      dialogVisible: false, //上传图片弹出框
      visible: false,
      obj: "",
      contentLists: [],
      confirmData: {}, //确认数据存储
      vmsSize: "", //情报板分辨率
      deviceId:0,
      width: "",
      height:"",
      gpIndex: 0,
      startTxt_x: "000", //X轴
      startTxt_y: "000", //X轴
      dataForm: {
        inScreenMode: "",
        rollSpeed: "",
        stopTime: "",
      },
      title: "",
      loading: false,
      isAdd: false,
      auditWord: [], //审核词汇表
      templateContent: [],
      checkList: [], //多选
      colorOptions: [
        {
          code: "red",
          content: "红色",
        },
        {
          code: "yellow",
          content: "黄色",
        },
        {
          code: "White",
          content: "白色",
        },
        {
          code: "Green",
          content: "绿色",
        },
      ],
      inScreenModeOptions: [
        {
          code: "0",
          name: "清屏（全黑)",
        },
        {
          code: "1",
          name: "立即显示",
        },
        {
          code: "2",
          name: "上移",
        },
        {
          code: "3",
          name: "下移",
        },
        {
          code: "4",
          name: "左移",
        },
        {
          code: "5",
          name: "右移",
        },
        {
          code: "6",
          name: "横百叶窗",
        },
        {
          code: "7",
          name: "竖百叶窗",
        },
        {
          code: "8",
          name: "上下合拢",
        },
        {
          code: "9",
          name: "上下展开",
        },
        {
          code: "10",
          name: "左右合拢",
        },
        {
          code: "11",
          name: "左右展开",
        },
        {
          code: "12",
          name: "中心合拢",
        },
        {
          code: "13",
          name: "中心展开",
        },
        {
          code: "14",
          name: "向下马赛克",
        },
        {
          code: "15",
          name: "向右马赛克",
        },
        {
          code: "16",
          name: "淡入",
        },
        {
          code: "17",
          name: "淡出",
        },
        {
          code: "18",
          name: "字符闪烁（闪后消失）",
        },
        {
          code: "19",
          name: "字符闪烁（闪后停留）",
        },
        {
          code: "20",
          name: "区域闪烁（闪后复原）",
        },
        {
          code: "21",
          name: "区域闪烁（闪后区域为黑）",
        },
      ],
      fontTypeOptions: [
        {
          code: "KaiTi",
          content: "楷体",
        },
        {
          code: "SimSun",
          content: "宋体",
        },
        {
          code: "FangSong",
          content: "仿宋",
        },
        {
          code: "LiSu",
          content: "隶书",
        },
      ],
      contentPositionOptions: [
        {
          code: "1",
          content: "左对齐",
        },
        {
          code: "2",
          content: "左右对齐",
        },
        {
          code: "3",
          content: "右对齐",
        },
        {
          code: "4",
          content: "上对齐",
        },
        {
          code: "5",
          content: "上下对齐",
        },
        {
          code: "6",
          content: "下对齐",
        },
      ],
      screenSizeOptions: [
        {
          type: "144*72",
        },
        {
          type: "768*72",
        },
        {
          type: "320*32",
        },
        {
          type: "384*32",
        },
        {
          type: "1024*128",
        },
      ],
      contentTemplate: [], //模板内容
      checkStatus: false, //校验状态
      boardMultiple: "",
    };
  },
  directives: {
    drag: function (el, binding, vnode) {
      let that = vnode.context;
      let dragBox = el;
      dragBox.onmousedown = (e) => {
        let disX = e.clientX - dragBox.offsetLeft;
        let disY = e.clientY - dragBox.offsetTop;
        let clientHeight = dragBox.clientHeight;
        let clientWidth = dragBox.clientWidth;
        document.onmousemove = function (e) {
          //鼠标移动触发事件，元素移到对应为位置
          let left = e.pageX - disX;
          let top = e.pageY - disY;
          //限制区域
          if (left <= 0) {
            left = 0;
          } else if (that.width - left - clientWidth <= 0) {
            left = that.width - clientWidth;
          }
          if (top <= 0) {
            top = 0;
          } else if (that.height - top - clientHeight <= 0) {
            top = that.height - clientHeight;
          }
          dragBox.style.left = left + "px";
          dragBox.style.top = top + "px";
          let a = (Array(3).join("0") + parseInt(left)).slice(-3);
          let b = (Array(3).join("0") + parseInt(top)).slice(-3);
          that.templateContent[dragBox.id].coordinate = a + b;
        };
        document.onmouseup = function () {
          //鼠标抬起，清除绑定的事件，元素放置在对应的位置
          document.onmousemove = null;
          document.onmousedown = null;
        };
        e.preventDefault(); //阻止浏览器的默认事件
      };
    },
  },
  watch: {
    templateContent: {
      deep: true,
      handler: function (newValue, oldValue) {
        var vm = this;
        for (var i = 0; i < vm.templateContent.length; i++) {
          vm.templateContent[i].CONTENT = vm.templateContent[i].CONTENT.replace(
            new RegExp("\r\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].CONTENT = vm.templateContent[i].CONTENT && vm.templateContent[i].CONTENT.replace(
            new RegExp("\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].CONTENT = vm.templateContent[i].CONTENT.replace(
            new RegExp(" ", "gm"),
            "&ensp;"
          );
        }
        let inrex = [];
        for (let index = vm.templateContent.length - 1; index >= 0; index--) {
          if (
            vm.templateContent[index].content == "" &&
            vm.templateContent[index].imageName == ""
          ) {
            inrex.push(index);
          }
        }
        for (let index = 0; index < inrex.length; index++) {
          vm.templateContent.splice(inrex[index], 1);
        }
      },
    },
  },
  computed: {
    dataRule() {
      return {};
    },
    previewDiv: function () {
      return {
        color: this.dataForm.fontColor,
        fontFamily: this.dataForm.fontType,
        fontSize: this.dataForm.fontSize + "px",
        width: this.width + "px",
        height: this.height + "px",
      };
    },
    // divStyle: function () {
    //   return {
    //     width: this.width + "px",
    //     height: this.height + "px",
    //   };
    // },
    templateDiv: function () {
      return {};
    },
    previewCss: function () {
      return {
        left: this.startTxt_x + "px",
        top: this.startTxt_y + "px",
      };
    },
  },
  methods: {
    init() {
      if(this.isAdd){
        this.title = "修改可变信息标志发布内容";
        this.visible = true;
        this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
        this.vmsSizeDivStyle(this.vmsSize);      
        this.contentLists = [
          {
            isRecommend: 0,
            item: "00",
            tissVmsTemplate: {
              id: null,
              screenSize: this.vmsSize,
              inScreenMode: "1",
              rollSpeed: 0,
              stopTime: 500,
              applyType: null,
              isCurrency: null,
              templateType: null,
              vmsType: null,
              createby: null,
              createtime: null,
              updateby: null,
              updatetime: null,
              remark: null,
            },
            templateContent: [
              {
                id: null,
                templateId: null,
                content: "",
                fontColor: "yellow",
                fontSize: 32,
                fontType: "SimSun",
                fontSpacing: 0,
                coordinate: "080007",
              },
            ],
            status: 0,
          },
        ];
        this.contentLists[0].tissVmsTemplate.screenSize = this.vmsSize;
        console.log(this.deviceId,'this.deviceId')
        this.$nextTick(() => {
          let that = this;
          getDeviceBase(this.deviceId).then((data) => {
            that.divStyle = {'width':data.data.devicePixel.split('*')[0] + 'px','height':data.data.devicePixel.split('*')[1] + 'px'} 
            that.$refs.contentTable.width = data.data.devicePixel.split('*')[0]
            that.$refs.contentTable.height = data.data.devicePixel.split('*')[1]
            that.width = data.data.devicePixel.split('*')[0]
            that.height = data.data.devicePixel.split('*')[1]
          });
          // 内容查询
          // getNewBoardEditInfo(this.deviceId).then((data) => {
            // if(data.code == 200){
              let data = {
                  "msg": "返回成功",
                  "code": 200,
                  "data": [
                      "{\"support\":{\"PROTOCOL_TYPE\":\"GUANGDIAN_V33\",\"FONT_SIZE\":\"3232,2424,1616\",\"DEVICEID\":\"S29-ZaoZhuangCompany-ShanTingStation-003-QBB-001\",\"COLOR\":\"红色,绿色,蓝色,黄色\",\"FONT\":\"宋体,黑体,楷体\"},\"content\":[{\"ITEM000\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"1616\",\"ACTION\":\"1\",\"COLOR\":\"绿色\",\"STATE\":true,\"CONTENT\":\"欢迎行驶山东高速，咨询求援请拨打9665\",\"COORDINATE\":\"012008\",\"FONT\":\"黑体\",\"STAY\":\"500\"}]},{\"ITEM001\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"1616\",\"ACTION\":\"1\",\"COLOR\":\"红色\",\"STATE\":true,\"CONTENT\":\"       2022年2月10日至11月30日<br>京台高速殷家林立交至泰山立交西幅封闭施工\",\"COORDINATE\":\"000000\",\"FONT\":\"宋体\",\"STAY\":\"300\"}]},{\"ITEM002\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"1616\",\"ACTION\":\"1\",\"COLOR\":\"红色\",\"STATE\":true,\"CONTENT\":\"    殷家林立交主线及各匝道台北方向车辆<br>       改至东幅单向通行，限速80km/h\",\"COORDINATE\":\"000000\",\"FONT\":\"宋体\",\"STAY\":\"300\"}]},{\"ITEM003\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"1616\",\"ACTION\":\"1\",\"COLOR\":\"绿色\",\"STATE\":true,\"CONTENT\":\"道运通手机APP、国家政务服务平台网上办理\",\"COORDINATE\":\"004005\",\"FONT\":\"宋体\",\"STAY\":\"500\"}]},{\"ITEM004\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"2424\",\"ACTION\":\"1\",\"COLOR\":\"黄色\",\"STATE\":true,\"CONTENT\":\" 通行施工路段 请谨慎驾驶\",\"COORDINATE\":\"004003\",\"FONT\":\"宋体\",\"STAY\":\"500\"}]},{\"ITEM005\":[{\"SPEED\":\"1\",\"FONT_SIZE\":\"1616\",\"ACTION\":\"1\",\"COLOR\":\"红色\",\"STATE\":true,\"CONTENT\":\"    G3京台高速 泰安西、崮山站、万德站<br>            双向出入口封闭\",\"COORDINATE\":\"000000\",\"FONT\":\"宋体\",\"STAY\":\"300\"}]}]}"
                  ]
              }
              let list = JSON.parse(data.data);
              this.saveData = list;
              let content = list.content
              let templateContent = [];
              for(let i = 0;i<content.length;i++){
                templateContent.push(content[i]['ITEM00'+i][0])
              }
              templateContent.forEach((item,index)=>{
                item.FONT_SIZE = item.FONT_SIZE.substr(0,2);
              })
              this.templateContent = templateContent
              this.contentLists = this.templateContent;
          //   }
          // });
          
          templateList(this.vmsSize).then(response => {
            this.contentTemplate = response.rows;
            console.log(this.contentTemplate,'模板列表')
          })
        });
      }else{
        //批量修改
        this.title = "批量修改可变信息标志发布内容";
        this.visible = true;
        this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
        this.vmsSizeDivStyle(this.vmsSize);
        this.contentLists = [
          {
            isRecommend: 0,
            item: "00",
            tissVmsTemplate: {
              id: null,
              screenSize: this.vmsSize,
              inScreenMode: "1",
              rollSpeed: 0,
              stopTime: 500,
              applyType: null,
              isCurrency: null,
              templateType: null,
              vmsType: null,
              createby: null,
              createtime: null,
              updateby: null,
              updatetime: null,
              remark: null,
            },
            templateContent: [
              {
                id: null,
                templateId: null,
                content: "",
                fontColor: "yellow",
                fontSize: 32,
                fontType: "SimSun",
                fontSpacing: 0,
                coordinate: "080007",
                img: "",
                imageName: "",
              },
            ],
            status: 0,
          },
        ];
        this.contentLists[0].tissVmsTemplate.screenSize = this.vmsSize;
        this.$nextTick(() => {
          console.log(this.deviceId);
          getDeviceBase(this.deviceId).then((data) => {
            console.log(data,'基础信息')
            that.divStyle = {'width':data.data.devicePixel.split('*')[0] + 'px','height':data.data.devicePixel.split('*')[1] + 'px'} 
            that.$refs.contentTable.width = data.data.devicePixel.split('*')[0]
            that.$refs.contentTable.height = data.data.devicePixel.split('*')[1]
            that.width = data.data.devicePixel.split('*')[0]
            that.height = data.data.devicePixel.split('*')[1]
          });
          this.templateContent = [{
            CONTENT:'请输入内容',
            COLOR:'red',
            FONT_SIZE:'16',
            FONT:'宋体',
            COORDINATE:'000000',
          }]
          this.contentLists = this.templateContent;
          templateList(this.vmsSize).then(response => {
            this.contentTemplate = response.rows;
            console.log(this.contentTemplate,'模板列表')
          })
        })
      }
    },
    del(index) {
      this.obj = index;
      var _this = this;
      document.onkeydown = function (e) {
        let key = window.event.keyCode;
        if (_this.obj != "") {
          if (key == 46 || key == 8) {
            let inx = "";
            for (let index = 0; index < _this.templateContent.length; index++) {
              if (_this.templateContent[index] == _this.obj) {
                inx = index;
              }
            }
            _this.templateContent.splice(inx, 1);
            _this.obj = "";
          } else {
            _this.obj = "";
          }
        }
      };
    },
    saveContent() {
      if (!this.checkStatus) {
        return util.$message.showInfo2("校验文本未通过，禁止提交发布");
      }
      this.$refs.contentTable.saveContentS();
    },
    saveContentItem(data) {
      this.$message({
        showClose: true,
        message: "保存成功",
        type: "success",
      });
    },
    async saveClose() {
      var d = this.contentLists;
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
      for (var i = 0; i < d.length; i++) {
        var value = Item_Content + i + "=" + this.dataForm.stopTime + "," + this.dataForm.inScreenMode + "," + "1" + "\\"+"C"+ d[i].COORDINATE
        + "\\"+"fh" + d[i].FONT_SIZE + d[i].FONT_SIZE + "\\"+"c" + this.operationColor(d[i].COLOR) + d[i].CONTENT + "\r\n";
        content += value;
      }
      let data = {
        'protocolType':this.saveData['support'].PROTOCOL_TYPE,
        'deviceId':this.saveData['support'].DEVICEID,
        content
      }
      await uploadBoardEditInfo(data).then(response => {
        if (response.code == 200) {
          this.$message.success("发布成功");
        } else {
          this.$message.error("发布失败"+response.msg);
        }
      });
    },
    operationColor(color){
      switch(color){
        case "红色":
          return "\c255000000"
        break;
        case "绿色":
          return "\c000255000"
        break;
        case "黄色":
          return "\c255255000"
        break;
        case "白色":
          return "\c225225225"
        break;
      }
    },
    //处理table传递过来数据
    contentPreview(index=0) {
      // if(index==''||index==undefined) {
      //   index = 0
      // }
      this.gpIndex = index;
    },
    updata(data) {
      if (data == 0) {
        this.templateContent[0].content = "请选择模板！该内容不做保存！";
      }
    },
    //将最新内容赋值给当前组件内存储，等待调整确认
    waitConfirm(data) {
      this.confirmData = data;
    },
    //判断是否包含非法字符和字符预览功能
    test(data) {
      let that = this;
      if (data.length == 0) {
        return util.$message.showInfo2("内容不能为空:" + item.word);
      }
      console.log(data,'正义！');
      // for(let i = 0;i<data.length;i++){
        
      // }
      this.checkStatus = true;
    },
    divStyles: function (vmsSize, a) {
      let b = vmsSize.split("*");
      let obj = {
        color: a.fontColor,
        "font-family": a.fontType,
      };
      if (b[0] == "480") {
        this.$set(obj, "color", a.tcontent.fontColor);
        this.$set(obj, "left", a.tcontent.coordinate.substring(0, 3) + "px");
        this.$set(obj, "top", a.tcontent.coordinate.substring(3, 6) + "px");
        this.$set(obj, "font-size", a.tcontent.fontSize + "px");
        if(a.tcontent.fontSpacing != 0){
        this.$set(obj, "letter-spacing", a.tcontent.fontSpacing + "px");
        }
        return obj;
      } else if (b[0] == "1024") {
        this.$set(obj, "color", a.tcontent.fontColor);
        this.$set(obj, "left", a.tcontent.coordinate.substring(0, 3) / 2.15 + "px");
        this.$set(obj, "top", a.tcontent.coordinate.substring(3, 6) / 2 + "px");
        this.$set(obj, "font-size", a.tcontent.fontSize / 2 + "px");
        if(a.tcontent.fontSpacing != 0){
          this.$set(obj, "letter-spacing", a.tcontent.fontSpacing / 2 + "px");
        }
        return obj;
      }
    },
    divStyle1: function (vmsSize) {
      let a = vmsSize.split("*");
      if (a[0] == "480") {
        return {
          width: a[0] + "px",
          height: a[1] + "px",
        };
      } else if (a[0] == "1024") {
        return {
          width: "100%",
          height: a[1] / 2 + "px",
        };
      }
    },
    divStyleImg(vmsSize, data, width, height) {
      if (data != undefined && data != "") {
        let a = vmsSize.split("*");
        if (a[0] == "480") {
          return {
            width: width + "px",
            height: height + "px",
          };
        } else if (a[0] == "1024") {
          return {
            width: "100%",
            height: height / 2 + "px",
          };
        }
      }
    },
    cliTest(data,index) {
      this.gpIndex = index;
    },
    //文字对齐方式
    alignment(alignmentNum) {
      let that = this;
      let boardSize = this.vmsSize;
      if (!boardSize || boardSize === "") {
        boardSize = "768*72";
      }
      let width = boardSize.split("*")[0];
      let height = boardSize.split("*")[1];
      //获取内容
      let contentWidth = document.getElementById("previewDivText").offsetWidth;
      let contentHeight =
        document.getElementById("previewDivText").offsetHeight;
      switch (alignmentNum) {
        case "1":
          this.startTxt_x = "000";
          break;
        case "2":
          this.startTxt_x = this.formatNum(
            (width - contentWidth) / 2 < 0 ? 0 : (width - contentWidth) / 2,
            3
          );
          break;
        case "3":
          this.startTxt_x = this.formatNum(
            width - contentWidth < 0 ? 0 : width - contentWidth,
            3
          );
          break;
        case "4":
          this.startTxt_y = "000";
          break;
        case "5":
          this.startTxt_y = this.formatNum(
            (height - contentHeight) / 2 < 0 ? 0 : (height - contentHeight) / 2,
            3
          );
          break;
        case "6":
          this.startTxt_y = this.formatNum(
            height - contentHeight < 0 ? 0 : height - contentHeight,
            3
          );
          break;
      }
    },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },
    //修改分辨率尺寸
    vmsSizeDivStyle(data) {
      console.log(data)
      let a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    //双击模板后，追加新的内容
    templateUP(data) {
      console.log(data)
      let item = {};
      item.COLOR = data.tcontent.fontColor;
      item.FONT_SIZE = data.tcontent.fontSize;
      item.CONTENT = data.tcontent.content;
      item.FONT = data.tcontent.fontType;
      this.$refs.contentTable.addRelease(item);
    },
    //单条发布按钮
    singleRelease() {
      this.$refs.contentTable.deleteAllHandle();
    },
    //追加发布
    addRelease() {
      this.$refs.contentTable.addRelease();
    },
    /*增加新的内容*/
    addTemplateContent() {
      if (this.contentLists.length == 0) {
        return this.$message.error("请选择模板之后新增！");
      }
      if (this.templateContent.length > 10) {
        this.$message.error("最多可添加十条信息！");
        return;
      }
      this.templateContent.push({
        content: "请输入内容",
        fontColor: "yellow",
        fontSize: "24",
        fontType: "KaiTi",
        fontSpacing: 0,
        coordinate: "000000",
        img: "",
        imageName: "",
      });
    },
    /*删除内容*/
    delTemplateContent(data) {
      for (let i = 0; i < this.templateContent.length; i++) {
        if (
          this.templateContent.indexOf(data) ==
            this.templateContent.indexOf(this.templateContent[i]) &&
          this.templateContent.length >= 1
        ) {
          this.templateContent.splice(this.templateContent.indexOf(data), 1);
        }
      }
    },
    //获取情报板的分辨率后，通过分辨率大小预览显示内容的背景大小
    vmsSizeDiv(res, item) {
      if (res != null) {
        let a = [];
        let size = "";
        if (res.screenSize == "768*72") {
          size =
            "384*32" +
            "*" +
            item.fontSize / 2 +
            "*" +
            item.fontSpacing / 2 +
            "*" +
            item.coordinate.substring(0, 3) / 2 +
            "*" +
            item.coordinate.substring(3, 6) / 2;
          a = size.split("*");
        } else if (res.screenSize == "1024*128") {
          size =
            "512*64" +
            "*" +
            item.fontSize / 2 +
            "*" +
            item.fontSpacing / 2 +
            "*" +
            item.coordinate.substring(0, 3) / 2 +
            "*" +
            item.coordinate.substring(3, 6) / 2;
          a = size.split("*");
        }
        return {
          color: item.fontColor,
          fontFamily: item.fontType,
          width: a[0] + "px",
          height: a[1] + "px",
          fontSize: a[2] + "px",
          letterSpacing: a[3] + "px",
          left: a[4] + "px",
          top: a[5] + "px",
        };
      }
    },
  },
};
</script>
<style  scoped>
/* 页脚 */
.dialog-footer {
  padding-left: 450px;
}
.photoOther img,
.photo img {
  max-width: 300px;
  width: 100%;
  height: 80px;
}
.none{display:none;}
.block{display:block;}
</style>

