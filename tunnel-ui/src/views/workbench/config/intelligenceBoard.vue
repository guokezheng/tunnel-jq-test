<template>
  <qz-dialog :title="title" :visible.sync="visible" top="6vh" width="1200px">
    <el-card
      class="box-card"
      style="margin-top: 2vh; background-color: #017cc4"
    >
      <span v-if="dataForm.applyType == ''" style="color: red"
        >请先选择屏幕尺寸，以方便查看具体预览</span
      >
      <span style="color: red; margin-left: 20px"
        >使用"#"作为占位符号,例如：前方#公里出现交通事故，请减速慢行</span
      >
      <div
        style="
          background-color: #000000;
          color: yellow;
          margin: 0 auto;
          overflow: hidden;
          position: relative;
        "
        v-on:ondragenter="ondragenter"
        v-on:drop="faceDrop"
        v-on:dragover="allowDrop"
        v-bind:style="divStyle"
      >
        <div
          v-for="(res, index) in templateContent"
          :key="index"
          v-drag
          @click="cliTest(res)"
          :id="index"
          v-model="templateContent"
          style="line-height: 1; position: absolute; white-space: nowrap"
          :class="{ previewContentCSS: ispreviewContent == index }"
          :style="{
            color: res.fontColor,
            fontSize: res.fontSize + 'px',
            fontFamily: res.fontType,
            letterSpacing: res.fontSpacing + 'px',
            left: res.coordinate.substring(0, 3) + 'px',
            top: res.coordinate.substring(3, 6) + 'px',
            zIndex: '1000',
          }"
          v-html="res.content"
        ></div>
        <div
          v-for="(item, i) in templateContent"
          :key="i"
          v-if="item.img != ''"
          :id="i"
          v-drag
          @click="cliTest(item)"
          :class="{ previewContentCSS: ispreviewContent == i }"
          v-model="templateContent"
          style="line-height: 1; position: absolute"
          :style="{
            left: item.coordinate.substring(0, 3) + 'px',
            top: item.coordinate.substring(3, 6) + 'px',
          }"
        >
          <img
            :src="isAdd ? item.img : item.imageName"
            alt=""
            @click="del(item)"
          />
        </div>
      </div>
    </el-card>
    <el-card>
      <el-form
        :model="dataForm"
        :rules="dataRule"
        label-width="90px"
        ref="dataForm"
        size="mini"
      >
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item prop="screenSize" label="屏幕尺寸">
              <el-select
                @change="resolvingPowerType"
                v-model="dataForm.screenSize"
                filterable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in screenSizeOptions"
                  :key="item.type"
                  :label="item.type"
                  :value="item.type"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="13">
            <el-form-item prop="contentPosition" label="内容位置">
              <el-button-group v-model="dataForm.contentPosition">
                <el-button @click="alignment('1')" size="small"
                  >左对齐</el-button
                >
                <el-button @click="alignment('2')" size="small"
                  >左右对齐</el-button
                >
                <el-button @click="alignment('3')" size="small"
                  >右对齐</el-button
                >
                <el-button @click="alignment('4')" size="small"
                  >上对齐</el-button
                >
                <el-button @click="alignment('5')" size="small"
                  >上下对齐</el-button
                >
                <el-button @click="alignment('6')" size="small"
                  >下对齐</el-button
                >
              </el-button-group>
            </el-form-item>
          </el-col> -->
          <el-col :span="2">
            <el-button type="primary" @click="addTemplateContent"
              >新增</el-button
            >
          </el-col>
<!--          <el-col :span="2">-->
<!--            <el-button type="primary" @click="chooseImageEvent()"-->
<!--              >选择图片</el-button-->
<!--            >-->
<!--          </el-col>-->
        </el-row>
        <!-- 选择图片弹出框开始 -->
        <qz-dialog
          title="选择图片"
          :visible.sync="dialogVisible"
          :before-close="close"
          top="6vh"
          width="1100px"
          :modal="false"
          append-to-body
        >
          <!-- 选择图片内容区域开始 -->
          <div class="changeImage">
            <el-row style="padding-left: 60px">
              <el-checkbox-group v-model="checkList">
                <el-col
                  :span="8"
                  v-for="(item, index) in imgUrl"
                  :key="index"
                  style="margin-top: 12px"
                >
                  <el-checkbox :label="item.pictureUrl">
                    <div class="photo">
                      <img
                        :src="item.pictureUrl"
                        @dblclick="dblEvent(item.pictureUrl)"
                        draggable="true"
                        v-on:dragstart="faceImagedragg($event, item)"
                      />
                    </div>
                  </el-checkbox>
                </el-col>
              </el-checkbox-group>
            </el-row>

            <span slot="footer" class="dialog-footer">
              <el-button size="small" @click="close">{{
                $t("cancel")
              }}</el-button>
              <el-button
                size="small"
                @click="sendBtnEvent()"
                type="primary"
                v-loading="loading"
                >{{ $t("confirm") }}</el-button
              >
            </span>
          </div>

          <!-- 选择图片内容区域结束 -->
        </qz-dialog>
        <!-- 选择图片弹出框结束 -->
        <!--        <el-scrollbar style="height:15vh;width: 1130px">-->
        <el-row
          :gutter="24"
          v-for="(res, index) in templateContent"
          :key="index"
          v-show="res.content != ''"
        >
          <el-col :span="22">
            <el-form-item label="内容">
              <el-input
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
                  :key="item.cssClass"
                  :label="item.dictLabel"
                  :value="item.cssClass"
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
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="fontType" label="字体类型">
              <el-select v-model="res.fontType" filterable placeholder="请选择">
                <el-option
                  v-for="item in fontTypeOptions"
                  :key="item.dictLabel"
                  :label="item.dictLabel"
                  :value="item.dictLabel"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="fontSpacing" label="字体间距">
              <el-input-number
                :min="0"
                controls-position="right"
                v-model="res.fontSpacing"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-show="templateContent.length > 1">
            <el-divider></el-divider>
          </el-col>
        </el-row>
        <!--        </el-scrollbar>-->
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item prop="rollSpeed" label="滚动速度(毫秒)">
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
            <el-form-item prop="stopTime" label="停留时间(秒)">
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
              <!-- <el-input clearable
                        placeholder="适用类型"
                        v-model="dataForm.applyType"></el-input> -->
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
                  :key="item.cssClass"
                  :label="item.dictLabel"
                  :value="item.cssClass"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item prop="templateType" label="模板类型">
              <el-select
                v-model="dataForm.templateType"
                filterable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in isCurrencyOptions"
                  :key="item.code"
                  :label="item.content"
                  :value="item.code"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="vmsType" label="情报板类型">
              <el-input
                clearable
                placeholder="情报板类型"
                v-model="dataForm.vmsType"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="isCurrency" label="模板模式">
              <el-select
                v-model="dataForm.isCurrency"
                filterable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in isCurrencyOptions"
                  :key="item.code"
                  :label="item.content"
                  :value="item.code"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="remark" label="备注">
              <el-input
                clearable
                placeholder="备注"
                v-model="dataForm.remark"
              ></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item prop="picSize"
                          label="图片大小">
              <el-select v-model="dataForm.imgSizeFrom"
                         size='mini'
                         filterable
                         placeholder="请选择"
                         style="width: 173px;">
                <el-option v-for="item,index in imgSize"
                           :key="index"
                           :label="item.name"
                           :value="item.type">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>
    </el-card>

    <template slot="footer">
      <el-button size="small" @click="visible = false">{{
        $t("cancel")
      }}</el-button>
      <el-button
        size="small"
        @click="dataFormSubmitHandle()"
        type="primary"
        v-loading="loading"
        >{{ $t("confirm") }}
      </el-button>
    </template>
  </qz-dialog>
</template>
<script>
// import * as api from "./api";
// import util from "@/libs/util";

// import { saveContentsList } from "./api";

export default {
  data() {
    return {
      applyTypeOptions: [],
      fontSizeOpt: [],
	    fontTypeOptions: [],
      checkList: [], //复选框一组
      obj: "",
      imgUrl: [],
      imgUrlOther: [],
      dialogVisible: false, //选择图片
      fileList: [
        {
          name: "food.jpeg",
          url: "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100",
        },
        {
          name: "food2.jpeg",
          url: "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100",
        },
      ],
      listquery: [], //拖拽图片
      curDragImgItem: "",
      visible: false,
      startTxt_x: "000",
      startTxt_y: "000",
      width: "1024",
      height: "128",
      content: "",
      fontColor: "yellow",
      fontSize: "24",
      fontType: "KaiTi",
      fontSpacing: 0,
      coordinate: "000000",
      url: "",
      previewContent: "", //预览内容
      ispreviewContent: -1,
      dataForm: {
        id: "",
        screenSize: "1024*128",
        inScreenMode: "",
        rollSpeed: undefined,
        stopTime: undefined,
        applyType: "",
        isCurrency: "",
        templateType: "",
        vmsType: "",
        createby: "",
        createtime: "",
        updateby: "",
        updatetime: "",
        remark: "",
        imgSizeFrom: "", //尺寸大小
      },
      templateContent: [],
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
        {
          type: "480*48",
        },
        {
          type: "480*72",
        },
        {
          type: "880*80",
        },
      ],
      colorOptions: [
        // {
        //   code: "red",
        //   content: "红色",
        // },
        // {
        //   code: "yellow",
        //   content: "黄色",
        // },
        // {
        //   code: "White",
        //   content: "白色",
        // },
        // {
        //   code: "GreenYellow",
        //   content: "绿色",
        // },
      ],
      isCurrencyOptions: [
        {
          code: "0",
          content: "通用",
        },
        {
          code: "1",
          content: "仅为智能推荐模板",
        },
      ],
      inScreenModeOptions: [
        // {
        //   code: "0",
        //   name: "清屏（全黑)",
        // },
        // {
        //   code: "1",
        //   name: "立即显示",
        // },
        // {
        //   code: "2",
        //   name: "上移",
        // },
        // {
        //   code: "3",
        //   name: "下移",
        // },
        // {
        //   code: "4",
        //   name: "左移",
        // },
        // {
        //   code: "5",
        //   name: "右移",
        // },
        // {
        //   code: "6",
        //   name: "横百叶窗",
        // },
        // {
        //   code: "7",
        //   name: "竖百叶窗",
        // },
        // {
        //   code: "8",
        //   name: "上下合拢",
        // },
        // {
        //   code: "9",
        //   name: "上下展开",
        // },
        // {
        //   code: "10",
        //   name: "左右合拢",
        // },
        // {
        //   code: "11",
        //   name: "左右展开",
        // },
        // {
        //   code: "12",
        //   name: "中心合拢",
        // },
        // {
        //   code: "13",
        //   name: "中心展开",
        // },
        // {
        //   code: "14",
        //   name: "向下马赛克",
        // },
        // {
        //   code: "15",
        //   name: "向右马赛克",
        // },
        // {
        //   code: "16",
        //   name: "淡入",
        // },
        // {
        //   code: "17",
        //   name: "淡出",
        // },
        // {
        //   code: "18",
        //   name: "字符闪烁（闪后消失）",
        // },
        // {
        //   code: "19",
        //   name: "字符闪烁（闪后停留）",
        // },
        // {
        //   code: "20",
        //   name: "区域闪烁（闪后复原）",
        // },
        // {
        //   code: "21",
        //   name: "区域闪烁（闪后区域为黑）",
        // },
      ],
      imgSize: [
        {
          type: "1024*128",
          name: "全屏",
        },
        {
          type: "",
          name: "正常",
        },
      ],
      title: "选择图片",
      loading: false,
      isAdd: false,
    };
  },
  directives: {
    drag: function (el, binding, vnode) {
      let that = vnode.context;
      let dragBox = el;
      dragBox.onmousedown = (e) => {
        // that.startTxt_x = dragBox.style.left.substring (0, dragBox.style.left.length - 2)
        // that.startTxt_y = dragBox.style.left.substring (0, dragBox.style.top.length - 2)
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
  computed: {
    dataRule() {
      return {
        isCurrency: [
          {//模板模式
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        rollSpeed: [
          //滚动速度
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        stopTime: [
          //停留时间
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        inScreenMode: [
          //入屏方式
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
      };
    },
    divStyle: function () {
      return {
        width: this.width + "px",
        height: this.height + "px",
      };
    },
  },
  watch: {
    templateContent: {
      deep: true,
      handler: function (newValue, oldValue) {
        // this.templateContent=newValue
        var vm = this;
        let inrex = [];
        for (let index = vm.templateContent.length - 1; index >= 0; index--) {
          if (
            vm.templateContent[index].content == "" &&
            (vm.templateContent[index].img == "" ||
              vm.templateContent[index].imageName == "")
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
  created() {
    this.getDicts("vms_apply_type").then((data) => {
      this.applyTypeOptions = data;
    });
    // this.getDicts("fontType").then((data) => {
    //   this.fontTypeOptions = data;
    // });
    this.getDicts("iot_device_font_type").then((res) => {
      this.fontTypeOptions = res.data;
      console.log(this.fontTypeOptions, "字体类型");
    });
    this.getDicts("iot_devices_font_color").then((res) => {
      this.colorOptions = res.data;
      console.log(this.colorOptions, "字体颜色");
    });
    this.getDicts("iot_device_font_size").then((res) => {
      this.fontSizeOpt = res.data;
      console.log(this.fontSizeOpt, "字体大小");
    });
    this.getDicts("iot_device_font_inScreen_mode").then((res) => {
      this.inScreenModeOptions = res.data;
      console.log(this.inScreenModeOptions, "入屏方式");
    });
  },
  methods: {
    init() {
      // this.getDicts("fontSize").then((data) => {
      //   this.fontSizeOpt = data;
      // });
      this.title = !this.dataForm.id ? this.$t("add") : this.$t("update");
      this.isAdd = !this.dataForm.id;
      this.visible = true;
      this.$nextTick(() => {
        if (this.isAdd) {
          this.$refs["dataForm"] && this.$refs["dataForm"].resetFields();
          this.dataForm.id = "";
          this.templateContent = [];
          this.width = "1024";
          this.height = "128";
          this.templateContent.push({
            content: "请输入内容",
            fontColor: "yellow",
            fontSize: "24",
            fontType: "KaiTi",
            fontSpacing: 0,
            coordinate: "000000",
            img: "",
          });
        } else {
          this.getInfo();
          this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
        }
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
    // 选择图片按钮
    chooseImageEvent() {
      this.imgUrl = [];
      this.getImageInfo();
      this.dialogVisible = true;
    },
    //选择图片弹框关闭事件
    close() {
      this.checkList = [];
      this.dialogVisible = false;
    },
    // 图片双击事件
    dblEvent(item) {
      if (this.templateContent.length > 7) {
        this.$message.error("最多只能添加七条信息！");
        return;
      }
      this.templateContent.push({
        content: "",
        fontColor: null,
        fontSize: null,
        fontType: null,
        fontSpacing: 0,
        coordinate: "000000",
        img: item,
        imageName: item,
      });
      this.checkList = [];
      this.dialogVisible = false;
    },
    // 选择图片点击确定按钮
    sendBtnEvent() {
      if (this.templateContent.length > 7) {
        this.$message.error("最多只能添加七条信息！");
        return;
      }
      if (this.checkList.length > 1) {
        this.$message.error("每次只能选择一张图片！");
        return;
      }
      if (this.checkList.length > 0) {
        for (var i = 0; i < this.checkList.length; i++)
          this.templateContent.push({
            content: "",
            fontColor: null,
            fontSize: null,
            fontType: null,
            fontSpacing: 0,
            coordinate: "000000",
            img: this.checkList[i],
          });

        this.checkList = [];
      } else {
        this.$message.error("请选择您要添加的图片！");
        return;
      }
      this.dialogVisible = false;
    },
    // 选择图片弹框拖动图片
    faceImagedragg(event, item) {
      this.curDragImgItem = item; //拖动存储该图片信息
    },
    faceDrop(e) {
      e.preventDefault(); //阻止默认行为
      this.listquery.push(this.curDragImgItem);
    },
    // 全选
    allowDrop(e) {
      e.preventDefault(); //阻止默认行为
    },
    ondragenter(e) {
      e.preventDefault(); //阻止默认行为
    },
    // 获取信息
    getInfo() {
      api.info(this.dataForm.id).then((data) => {
        this.dataForm = data;
        this.dataForm.isCurrency = this.dataForm.isCurrency + "";
        if (this.dataForm.templateType != null) {
          this.dataForm.templateType = this.dataForm.templateType + "";
        }
        this.width = this.dataForm.screenSize.split("*")[0];
        this.height = this.dataForm.screenSize.split("*")[1];
      });
      api.getTemplateContentById(this.dataForm.id).then((data) => {
        this.templateContent = data;
        console.log(this.templateContent,"this.templateContent");
        if (this.templateContent.length == 0) {
          this.templateContent.push({
            content: "",
            fontColor: "yellow",
            fontSize: "24",
            fontType: "KaiTi",
            fontSpacing: 0,
            coordinate: "000000",
            img: "",
          });
        }
      });
    },
    // 表单提交
    async dataFormSubmitHandle() {
      let valid = await this.$refs.dataForm.validate().catch(() => {
        return util.$message.showInfo2("校验错误");
      });
      if (!valid) return;
      this.loading = true;
      let templateId = "";
      let method = !this.isAdd ? "put" : "post";
      if (this.isAdd) {
        await api.save(this.dataForm, method).then((data) => {
          templateId = data;
        });
        let params = {
          templateContent: this.templateContent,
          templateId: templateId,
        };
        api.saveContentsList(params).catch((err) => {
          throw err;
        });
      } else {
        await api.save(this.dataForm, method).then((data) => {});
        this.templateContent.forEach((e) => {
          e.img = e.imageName;
        });
        let params = {
          templateContent: this.templateContent,
          templateId: this.dataForm.id,
        };
        await api.delContent(this.dataForm.id).catch((err) => {
          throw err;
        });
        await api.saveContentsList(params).catch((err) => {
          throw err;
        });
      }

      this.loading = false;
      this.visible = false;
      this.isAdd = false;
      this.$emit("refreshDataList", this.dataForm);
    },
    /*********************************************业务代码***********************************************/
    //文字对齐方式
    alignment(alignmentNum) {
      let that = this;
      let boardSize = this.dataForm.screenSize;
      if (!boardSize || boardSize === "") {
        boardSize = "768*72";
      }
      let width = boardSize.split("*")[0];
      let height = boardSize.split("*")[1];
      //获取内容
      let contentWidth = document.getElementById("templateDivText").offsetWidth;
      let contentHeight =
        document.getElementById("templateDivText").offsetHeight;
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
            width - contentWidth <= 0 ? 0 : width - contentWidth,
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
            height - contentHeight <= 0 ? 0 : height - contentHeight,
            3
          );
          break;
      }
    },
    /* 拆分分辨率大小 */
    resolvingPowerType(data) {
      let a = [];
      a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    /*增加新的内容*/
    addTemplateContent() {
      if (this.templateContent.length >= 7) {
        this.$message.error("最多只能添加7条信息！");
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
      });
    },
    /*删除内容*/
    delTemplateContent(data) {
      console.log(data,"data");
      for (let i = 0; i < this.templateContent.length; i++) {
        if (
          this.templateContent.indexOf(data) ==
          this.templateContent.indexOf(this.templateContent[i])
        ) {
          this.templateContent.splice(this.templateContent.indexOf(data), 1);
        }
      }
    },
    cliTest(data) {
      this.ispreviewContent = this.templateContent.indexOf(data);
    },
    /********************图片上传*********************/
    handleRemove(file, fileList) {},
    handlePreview(file) {},
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + fileList.length
        } 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    /**
     * 获取图片信息
     */
    getImageInfo() {
      let params = {
        vmsSize: this.dataForm.screenSize,
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
  },
};
</script>
<style lang="less" scoped>
.previewContentCSS {
  border: yellow 1px dashed;
}
/* 页脚 */
.dialog-footer {
  padding-left: 450px;
}
.photoOther img,
.photo img {
  max-width: 300px;
  width: 100%;
  // height: 80px;
}
</style>
