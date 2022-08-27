<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    width="85%"
    append-to-body
  >
    <el-row :gutter="24">
      <el-col :span="16">
        <h3>播放列表</h3>
        <el-card style="background-color: #017cc4">
          <content-table
            :option="contentLists"
            @contentArr="saveContentItem"
            ref="contentTable"
            @dataList="waitConfirm"
            @contentList="contentPreview"
            @length0="updata"
            style="postion: absolute"
          ></content-table>
        </el-card>
        <h3>设备节目单</h3>
        <el-card style="padding: 0; background-color: #017cc4">
          <div
            style="
              background-color: #000000;
              color: yellow;
              margin: 0 auto;
              overflow: hidden;
              position: relative;
            "
          >
            <div
              v-for="(res, index) in templateContent"
              :key="index"
              v-model="templateContent"
              v-drag
              :id="index"
              @click="cliTest(res)"
              style="
                line-height: 1;
                position: absolute;
                white-space: nowrap;
                z-index: 1000;
              "
              :class="{ previewContentCSS: ispreviewContent == index }"
              :style="{
                color: res.fontColor,
                fontSize: res.fontSize + 'px',
                fontFamily: res.fontType,
                letterSpacing: res.fontSpacing + 'px',
                left: res.coordinate.substring(0, 3) + 'px',
                top: res.coordinate.substring(3, 6) + 'px',
              }"
              v-html="res.content"
            ></div>
            <div
              v-for="(item, index) in templateContent"
              :key="index"
              v-show="item.imageName != ''"
              :id="index"
              v-model="templateContent"
              @click="cliTest(item)"
              style="line-height: 1; position: absolute"
              v-drag
              :style="{
                left: item.coordinate.substring(0, 3) + 'px',
                top: item.coordinate.substring(3, 6) + 'px',
              }"
            >
              <img :src="item.imageName" alt="" @click="del(item)" />
            </div>
          </div>
        </el-card>
        <el-card
          class="box-card"
          style="margin-top: 2vh; background-color: #017cc4; overflow-y: scroll"
        >
          <el-form
            :model="dataForm"
            :rules="dataRule"
            label-width="90px"
            ref="dataForm"
            size="mini"
          >
            <!--            <el-scrollbar style="height:15vh;width: 1113px">-->
            <el-row
              :gutter="24"
              v-for="(res, index) in templateContent"
              :key="index"
              v-show="res.content != ''"
            >
              <el-col :span="22">
                <el-form-item label="内容">
                  <el-input
                    @change="test"
                    type="textarea"
                    clearable
                    placeholder="内容"
                    v-model="res.content"
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
                <el-form-item prop="fontColor" label="字体颜色">
                  <el-select
                    v-model="res.fontColor"
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
                <el-form-item prop="fontSize" label="字体大小">
                  <el-select v-model="res.fontSize" style="width: 100%">
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
                <el-form-item prop="fontType" label="字体类型">
                  <el-select
                    v-model="res.fontType"
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
              <el-col :span="2">
                <el-button type="primary" @click="addTemplateContent()"
                  >新增</el-button
                >
              </el-col>
              <el-col :span="2">
                <el-button type="primary" @click="chooseImageEvent"
                  >上传图片</el-button
                >
              </el-col>
            </el-row>
          </el-form>
        </el-card>
        <el-card style="margin-top: 20px; background-color: #017cc4">
          <el-button
            size="medium"
            @click="addRelease()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
            追加信息
          </el-button>
          <el-button
            size="medium"
            @click="saveClose()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
            保存设备发布内容edit
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-scrollbar style="height: 100vh; width: 100%">
          <h3>模板选择</h3>
          <div v-for="res in contentTemplate">
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
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                      z-index: 999;
                    "
                    :style="divStyles(res.screenSize, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="divStyles(res.screenSize, item)"
                  >
                    <img
                      :src="item.imageName"
                      alt=""
                      v-bind:style="
                        divStyleImg(
                          res.screenSize,
                          item.imageName,
                          item.width,
                          item.height
                        )
                      "
                    />
                  </div>
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
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="divStyles(res.screenSize, item)"
                  >
                    <img
                      :src="item.imageName"
                      alt=""
                      v-bind:style="
                        divStyleImg(
                          res.screenSize,
                          item.imageName,
                          item.width,
                          item.height
                        )
                      "
                    />
                  </div>
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
        </el-scrollbar>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script>
// import * as api from "./api";
// import util from "@/libs/util";
import contentTable from "./content-table";

export default {
  components: {
    contentTable,
  },
  props:[
    'IntelligenceBoardShow',
  ],
  data() {
    return {
      fontSizeOpt: [],
	  fontTypeOptions: [],
      applyTypeOptions: [],
      dialogVisible: false, //上传图片弹出框
      visible: false,
      obj: "",
      contentLists: [],
      confirmData: {}, //确认数据存储
      vmsSize: "", //情报板分辨率
      width: "",
      ispreviewContent: -1,
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
      templateContent: [
        {
          id: null,
          templateId: null,
          content: "请输入内容",
          fontColor: "yellow",
          fontSize: 32,
          fontType: "SimSun",
          fontSpacing: 0,
          coordinate: "080007",
          img: "",
          imageName: "",
        },
        {
          id: null,
          templateId: null,
          content: "请输入内容",
          fontColor: "yellow",
          fontSize: 32,
          fontType: "SimSun",
          fontSpacing: 0,
          coordinate: "080007",
          img: "",
          imageName: "",
        },
      ],
      checkList: [], //多选
      imgUrl: [],
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
      // fontTypeOptions: [
      //   {
      //     code: "MicrosoftYaHei",
      //     content: "微软雅黑",
      //   },
      //   {
      //     code: "KaiTi",
      //     content: "楷体",
      //   },
      //   {
      //     code: "SimSun",
      //     content: "宋体",
      //   },
      //   {
      //     code: "FangSong",
      //     content: "仿宋",
      //   },
      //   {
      //     code: "LiSu",
      //     content: "隶书",
      //   },
      //],
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
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("\r\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp(" ", "gm"),
            "&ensp;"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("<font&ensp;", "gm"),
            "<font "
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
    divStyle: function () {
      return {
        width: this.width + "px",
        height: this.height + "px",
      };
    },
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
  mounted() {
    this.getDicts("vms_apply_type").then((data) => {
      this.applyTypeOptions = data;
    });
	this.getDicts("fontType").then((data) => {
	  this.fontTypeOptions = data;
	});
  },
  methods: {
    init() {
      this.getDicts("fontSize").then((data) => {
        this.fontSizeOpt = data;
      });
      this.title = "修改可变信息标志发布内容";
      this.visible = true;
      this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
      this.vmsSizeDivStyle(this.vmsSize);
      if (this.radio1 == 0) {
        //滚屏方式处理数据
        this.contentLists = JSON.parse(JSON.stringify(this.list.item));
      } else {
        //单屏方式处理数据contentLists默认
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
                content: "请输入内容",
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
      }
      // this.contentLists[0].tissVmsTemplate.screenSize = this.vmsSize;
      this.$nextTick(() => {
        api.getWordList().then((data) => {
          this.auditWord = data;
        });
        //通过分辨率查询模板内容
        let params = {
          screenSize: this.vmsSize,
        };
        // 模板列表查询接口
        api.vmsTemplateList(params).then((data) => {
          this.contentTemplate = data;
        });
      });
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
    chooseImageEvent() {
      if (this.contentLists.length == 0) {
        return this.$message.error("请选择模板之后上传图片！");
      }
      this.imgUrl = [];
      this.getImageInfo();
      this.dialogVisible = true;
    },
    /**
     * 获取图片信息
     */ getImageInfo() {
      let params = {
        vmsSize: this.vmsSize,
      };
      api.queryImgInfo(params).then((data) => {
        if (!data) {
          return;
        }
        let list = data.sort((dataA, dataB) => {
          dataA.id - dataB.id;
        });
        this.imgUrl.push(...list);
      });
    },

    //  双击上传图片
    dblEvent(item) {
      this.templateContent.push({
        content: "",
        fontColor: "",
        fontSize: "",
        fontType: "",
        fontSpacing: 0,
        coordinate: "000000",
        imageName: item,
        img: item,
      });
      this.dialogVisible = false;
    },
    // 选择图片弹出框确定按钮
    sendBtnEvent() {
      if (this.templateContent.length + this.checkList.length > 10) {
        this.$message.error("最多可添加十条信息！");
        return;
      }
      if (this.checkList.length == 0) {
        this.$message.error("请选择您要上传的图片！");
        return;
      }
      let arr = [];
      let aindex = "";
      let size = this.vmsSize.split("*");
      this.checkList.forEach((item) => {
        let img = this.imgUrl.find((url) => url.pictureUrl == item);
        if (img.imageWidth == size[0] && img.imageHeight == size[1]) {
          arr.push(img.pictureUrl);
        }
      });

      if (arr.length > 1) {
        this.$message.error("一次只可上传一张底片！");
        return;
      }
      if (arr.length > 0) {
        aindex = this.checkList.indexOf(arr[0]);
      }
      if (aindex != "") {
        let ar = JSON.parse(JSON.stringify(this.checkList[aindex]));
        this.checkList.splice(aindex, 1);
        this.checkList.unshift(ar);
      }
      for (var i = 0; i < this.checkList.length; i++) {
        this.templateContent.push({
          content: "",
          fontColor: "",
          fontSize: "",
          fontType: "",
          fontSpacing: 0,
          coordinate: "000000",
          img: this.checkList[i],
          imageName: this.checkList[i],
        });
      }
      this.checkList = [];
      this.dialogVisible = false;
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
    saveClose() {
      let params = {
        id: this.deviceId,
        item: this.contentLists,
      };
      params.item.forEach((e) => {
        let a = [];
        for (let index = e.templateContent.length - 1; index >= 0; index--) {
          if (e.templateContent[index].content == "请输入内容") {
            a.push(index);
          }
          if (e.templateContent[index].imageName == undefined) {
            e.templateContent[index].imageName = "";
          }
          if (
            e.templateContent[index].content == "" &&
            e.templateContent[index].imageName == ""
          ) {
            a.push(index);
          }
        }
        a.forEach((ac) => {
          e.templateContent.splice(ac, 1);
        });
      });
      let a = [];
      for (let index = 0; index < params.item.length; index++) {
        params.item[index].isRecommend = index > 9 ? "0" + index : "00" + index;
        params.item[index].item = index > 9 ? "0" + index : "00" + index;
        if (params.item[index].templateContent.length == 0) {
          a.push(index);
        }
      }
      a.forEach((ac) => {
        params.item.splice(ac, 1);
      });
      if (this.contentLists.length == 0) {
        return this.$message.error("请选择模板之后保存！");
      }
      let list = [];
      params.item.forEach((e) => {
        list.push(e);
      });
      let listz = [
        {
          item: list,
        },
      ];
      let paramss = {
        data: listz,
      };
      api.getWifth(paramss).then((res) => {
        params.item = res.data[0].item;
        this.$emit("dataItem", params);
        this.visible = false;
      });
    },
    //处理table传递过来数据
    contentPreview(data) {
      this.templateContent = [];
      this.templateContent = data.templateContent;
      // this.dataForm = data.tissVmsTemplate;
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
      this.checkStatus = true;
    },
    divStyles: function (vmsSize, a) {
      let b = vmsSize.split("*");
      let obj = {
        color: a.fontColor,
        "font-family": a.fontType,
      };
      if (b[0] == "480") {
        this.$set(obj, "left", a.coordinate.substring(0, 3) + "px");
        this.$set(obj, "top", a.coordinate.substring(3, 6) + "px");
        this.$set(obj, "font-size", a.fontSize + "px");
        this.$set(obj, "letter-spacing", a.fontSpacing + "px");
        return obj;
      } else if (b[0] == "1024") {
        this.$set(obj, "left", a.coordinate.substring(0, 3) / 2.15 + "px");
        this.$set(obj, "top", a.coordinate.substring(3, 6) / 2 + "px");
        this.$set(obj, "font-size", a.fontSize / 2 + "px");
        this.$set(obj, "letter-spacing", a.fontSpacing / 2 + "px");
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
    cliTest(data) {
      this.ispreviewContent = this.templateContent.indexOf(data);
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
      let a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    //双击模板后，追加新的内容
    templateUP(data) {
      this.$refs.contentTable.addRelease(JSON.parse(JSON.stringify(data)));
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
</style>
