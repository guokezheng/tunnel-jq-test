<template>
  <div class="container">
    <!-- 添加信息弹窗 -->
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="60%"
      :before-close="handleClose"
    >
      <el-card class="box-card" style="margin-top: 2vh">
        <div
          style="
            backgroundColor: #000000;
            color: yellow;
            margin: 0 auto;
            overflow: hidden;
            position: relative;
          "
          v-on:ondragenter="ondragenter"
          v-on:drop="faceDrop"
          v-on:dragover="allowDrop"
          :style="{
            width: boardWidth + 'px',
            height: boardHeight + 'px',
          }"
          class="blackBoard"
        >
          <span
            style="line-height: 1; position: absolute; white-space: nowrap"
            :style="{
              color: dataForm.COLOR,
              fontSize: dataForm.FONT_SIZE,
              fontFamily: dataForm.FONT,
              letterSpacing: dataForm.SPEED + 'px',
              zIndex: '1000',
              left: dataForm.COORDINATE.substring(0, 3) + 'px',
              top: dataForm.COORDINATE.substring(3, 6) + 'px',
            }"
            class="textBoard"
            v-html="dataForm.CONTENT.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp')"
          ></span>
         
        </div>
      </el-card>
      <el-row>
        <!-- <el-button type="primary" plain @click="addCurrRow">添加</el-button> -->
        <el-button type="info" plain @click="alignment(6)" size="mini"
          >下对齐</el-button
        >
        <el-button type="info" plain @click="alignment(5)" size="mini"
          >上下居中</el-button
        >
        <el-button type="info" plain @click="alignment(4)" size="mini"
          >上对齐</el-button
        >
        <el-button type="info" plain @click="alignment(3)" size="mini"
          >右对齐</el-button
        >
        <el-button type="info" plain @click="alignment(2)" size="mini"
          >左右居中</el-button
        >
        <el-button type="info" plain @click="alignment(1)" size="mini"
          >左对齐</el-button
        >
      </el-row>
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
              <el-form-item prop="category" label="所属类别">
                <el-select
                  v-model="dataForm.category"
                  placeholder="请选择所属类别"
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="item in iotTemplateCategoryList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="2">
              <el-button type="primary" @click="addTemplateContent">
                新增
              </el-button>
            </el-col> -->
            <!-- <el-col :span="2">
              <el-button type="primary" @click="chooseImageEvent()"
                >选择图片</el-button
              >
            </el-col> -->
          </el-row>
          <!-- 选择图片弹出框开始 -->
          <!-- <el-dialog
            title="选择图片"
            :visible.sync="dialogVisible1"
            :before-close="close"
            top="6vh"
            width="1100px"
            :modal="false"
            append-to-body
          >
            <div class="changeImage">
              <el-row style="padding-left: 60px">
                <el-checkbox-group v-model="checkList">
                  <el-col
                    :span="8"
                    v-for="(item, index) in imgUrl"
                    :key="index"
                    style="margin-top: 12px"
                    v-if="item.pictureUrl"
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
                <el-button size="small" @click="close">取消</el-button>
                <el-button
                  size="small"
                  @click="sendBtnEvent()"
                  type="primary"
                  v-loading="loading"
                  >确认</el-button
                >
              </span>
            </div>
          </el-dialog> -->
          <!-- 选择图片弹出框结束 -->
          <el-row :gutter="24">
            <el-col :span="22">
              <el-form-item label="详细内容">
                <el-input
                  type="textarea"
                  clearable
                  id="textContent"
                  placeholder="详细内容"
                  v-model="dataForm.CONTENT"
                  @keyup.native="keyDown($event)"
                ></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="2">
              <el-button
                type="danger"
                icon="el-icon-delete"
                @click="delTemplateContent(res)"
              ></el-button>
            </el-col> -->
            <el-col :span="6">
              <el-form-item prop="COLOR" label="字体颜色">
                <el-select
                  v-model="dataForm.COLOR"
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
                <el-select v-model="dataForm.FONT_SIZE" style="width: 100%">
                  <el-option
                    v-for="item in fontSizeOpt"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="FONT" label="字体类型">
                <el-select
                  v-model="dataForm.FONT"
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
              <el-form-item prop="SPEED" label="字体间距">
                <el-input-number
                  :min="0"
                  controls-position="right"
                  v-model="dataForm.SPEED"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" v-show="templateContent.length > 1">
              <el-divider></el-divider>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <!-- <el-col :span="6">
              <el-form-item prop="rollSpeed" label="滚动速度">
                <el-input-number
                  :min="0"
                  controls-position="right"
                  v-model="dataForm.rollSpeed"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col> -->
            <el-col :span="6">
              <el-form-item prop="STAY" label="停留时间">
                <el-input-number
                  :min="0"
                  controls-position="right"
                  v-model="dataForm.STAY"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="ACTION" label="入屏方式">
                <el-select
                  v-model="dataForm.ACTION"
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
            <el-col :span="6">
              <el-form-item prop="screenSize" label="屏幕尺寸">
                <el-select
                  v-model="dataForm.screenSize"
                  filterable
                  placeholder="请选择"
                  v-if="!devicePixelBoolean"
                >
                  <el-option
                    v-for="item in screenSizeOptions"
                    :key="item.type"
                    :label="item.type"
                    :value="item.type"
                    @click.native="changeScreenSize(item.type)"
                  >
                  </el-option>
                </el-select>
                <el-input
                  disabled
                  v-model="dataForm.screenSize"
                  v-if="devicePixelBoolean"
                ></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="6">
              <el-form-item prop="remark" label="备注">
                <el-input v-model="dataForm.remark" style="width: 100%" />
              </el-form-item>
            </el-col> -->
          </el-row>
        </el-form>
      </el-card>

      <template slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button
          size="small"
          @click="dataFormSubmitHandle()"
          type="primary"
          v-loading="loading"
          >确认
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import {
  getTemplateInfo,
  addTemplate,
  addTemplateContent,
  editTemplateContent,
  editTemplate,
  deleteTemplate,
  getTemplateContent,
  getGalleryList,
} from "@/api/board/template";
export default {
  data() {
    return {
      content: "",
      boardWidth: "",
      boardHeight: "",
      checkList: [], //复选框一组
      obj: "",
      imgUrl: [],
      imgUrlOther: [],
      dialogVisible: false,
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
      width: "400",
      height: "40",
      // content: "",
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
        category: "",
        inScreenMode: "1", //入屏方式
        rollSpeed: "1000",
        stopTime: "500",
        applyType: "", //适用类型
        vmsType: "", //情报板类型
        remark: "", //备注
        imgSizeFrom: "", //尺寸大小
        imageUrl: "",
        height: "",
        width: "",
        coordinate: "", //起始点位置;前3位代表x点的位值，后3位代表y点的位置
        screenSize: "",
        COORDINATE: "",
        FONT_SIZE: "",
      },
      templateContent: [],
      templateDelContent: [],
      dataRule: {
        screenSize: [
          {
            required: true,
            message: "请选择分辨率",
            trigger: "blur",
          },
        ],
        category: [
          {
            required: true,
            message: "请选择所属类别",
            trigger: "blur",
          },
        ],
        fontColor: [
          {
            required: true,
            message: "请填写字体颜色",
            trigger: "blur",
          },
        ],
        fontSize: [
          {
            required: true,
            message: "请填写字体大小",
            trigger: "blur",
          },
        ],
        fontType: [
          {
            required: true,
            message: "请选择字体类型",
            trigger: "change",
          },
        ],
        fontSpacing: [
          {
            required: true,
            message: "请选择字体间距",
            trigger: "change",
          },
        ],
        rollSpeed: [
          {
            required: true,
            message: "请填写滚动速度",
            trigger: "blur",
          },
        ],
        stopTime: [
          {
            required: true,
            message: "请填写停留时间",
            trigger: "blur",
          },
        ],
        inScreenMode: [
          {
            required: true,
            message: "请选择入屏方式",
            trigger: "blur",
          },
        ],
      },
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
          code: "SimHei",
          content: "黑体",
        },
      ],
      screenSizeOptions: [
        {
          type: "400*40",
        },
        {
          type: "128*64",
        },
      ],
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
          code: "blue",
          content: "蓝色",
        },
        {
          code: "GreenYellow",
          content: "绿色",
        },
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
      fontSizeOpt: [
        {
          value: "32px",
          label: "32px",
        },
        {
          value: "24px",
          label: "24px",
        },
        {
          value: "16px",
          label: "16px",
        },
      ],
      title: "选择图片",
      loading: false,
      isAdd: false,
      iotTemplateCategoryList: [],
      infoType: "",
      devicePixelBoolean: false,
    };
  },
  //   directives: {
  //     drag: function (el, binding, vnode) {
  //       let that = vnode.context;
  //       let dragBox = el;
  //       dragBox.onmousedown = (e) => {
  //         // that.startTxt_x = dragBox.style.left.substring (0, dragBox.style.left.length - 2)
  //         // that.startTxt_y = dragBox.style.left.substring (0, dragBox.style.top.length - 2)
  //         let disX = e.clientX - dragBox.offsetLeft;
  //         let disY = e.clientY - dragBox.offsetTop;
  //         let clientHeight = dragBox.clientHeight;
  //         let clientWidth = dragBox.clientWidth;
  //         document.onmousemove = function (e) {
  //           //鼠标移动触发事件，元素移到对应为位置
  //           let left = e.pageX - disX;
  //           let top = e.pageY - disY;
  //           //限制区域
  //           if (left <= 0) {
  //             left = 0;
  //           } else if (that.width - left - clientWidth <= 0) {
  //             left = that.width - clientWidth;
  //           }
  //           if (top <= 0) {
  //             top = 0;
  //           } else if (that.height - top - clientHeight <= 0) {
  //             top = that.height - clientHeight;
  //           }
  //           dragBox.style.left = left + "px";
  //           dragBox.style.top = top + "px";
  //           let a = (Array(3).join("0") + parseInt(left)).slice(-3);
  //           let b = (Array(3).join("0") + parseInt(top)).slice(-3);
  //           that.templateContent[dragBox.id].coordinate = a + b;
  //         };
  //         document.onmouseup = function () {
  //           //鼠标抬起，清除绑定的事件，元素放置在对应的位置
  //           document.onmousemove = null;
  //           document.onmousedown = null;
  //         };
  //         e.preventDefault(); //阻止浏览器的默认事件
  //       };
  //     },
  //   },
  computed: {
    divStyle: function () {
      return {
        width: this.width + "px",
        height: this.height + "px",
      };
    },
  },
  watch: {
    "dataForm.CONTENT": {
      deep: true,
      handler: function (newValue, oldValue) {
        this.dataForm.content1 = newValue;
      },
    },
    //   templateContent: {
    //     deep: true,
    //     handler: function (newValue, oldValue) {
    //       // this.templateContent=newValue
    //       var vm = this;
    //       let inrex = [];
    //       for (let index = vm.templateContent.length - 1; index >= 0; index--) {
    //         if (
    //           vm.templateContent[index].content == "" &&
    //           (vm.templateContent[index].img == "" ||
    //             vm.templateContent[index].imageName == "")
    //         ) {
    //           inrex.push(index);
    //         }
    //       }
    //       for (let index = 0; index < inrex.length; index++) {
    //         vm.templateContent.splice(inrex[index], 1);
    //       }
    //     },
    //   },
  },
  mounted() {
    // 屏幕尺寸字典数据
    // this.getDicts("screenSize").then((res) => {
    // this.screenSizeOptions = res.data;
    // console.log(this.screenSizeOptions,'this.screenSizeOptions')
    // });
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
  },
  methods: {
    init(devicePixel, type) {
      if (devicePixel) {
        this.devicePixelBoolean = true;

        console.log(devicePixel, "00000");
        this.dataForm.screenSize = devicePixel;

        this.boardWidth = devicePixel.split("*")[0];
        this.boardHeight = devicePixel.split("*")[1];
      } else {
        this.devicePixelBoolean = false;
        (this.boardWidth = "400"), (this.boardHeight = "40");
      }
      this.infoType = type;

      this.title = "新增";
      this.isAdd = !this.dataForm.id;
      this.dialogVisible = true;
      console.log(this.dataForm.id, "这是模板id");
      this.templateDelContent = [];
      this.$nextTick(() => {
        if (this.isAdd) {
          this.$refs["dataForm"] && this.$refs["dataForm"].resetFields();
          this.dataForm.id = "";
          this.dataForm = {};
          this.width = "400";
          this.height = "40";
          this.dataForm = {
            CONTENT: "请输入内容",
            COLOR: "黄色",
            FONT_SIZE: "24px",
            FONT: "黑体",
            SPEED: "1",
            ACTION: "1",
            COORDINATE: "063004",
            STATE: "true",
            STAY: "500",
            screenSize: devicePixel,
          };
          this.content = "请输入内容";
        } else {
          this.getInfo();
          this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
        }
      });
      this.$forceUpdate();
    },
    changeScreenSize(size){
      console.log(size,"00000000000000000000");
      this.boardWidth = size.split("*")[0];
      this.boardHeight = size.split("*")[1];

      this.$forceUpdate()
    },
    keyDown(ev) {
      console.log(ev.keyCode, "ev.keyCode");
      
      let arr = [];
      let content = "";
      const input = document.getElementById("textContent");
      // console.log(input.selectionStart);
      arr = this.dataForm.CONTENT.split("");
      // console.log(arr, "arr");
      content += "<div>";
      for (var i = 0; i < arr.length; i++) {
        content += arr[i];
        if (i == input.selectionStart - 1) {
          if(ev.keyCode == 13){
            content += "<br>";
          }else if(ev.keyCode == 32){
            content += "&nbsp";
          }
        }
      }
      content += "</div>";
      this.dataForm.content1 = content;
    },

    //选择图片弹框关闭事件
    close() {
      this.checkList = [];
      this.dialogVisible = false;
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
      getTemplateInfo(this.dataForm.id).then((data) => {
        this.dataForm = data.data;
        this.width = this.dataForm.screenSize.split("*")[0];
        this.height = this.dataForm.screenSize.split("*")[1];
      });
      getTemplateContent(this.dataForm.id).then((data) => {
        this.templateContent = data.rows;

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
        return this.$modal.msgError("校验错误");
      });
      if (!valid) return;
      this.loading = true;
      // let templateId = "";
      let method = "put";
      if (this.isAdd) {
        console.log(this.dataForm, "this.dataForm新增组件");
        console.log(this.devicePixelBoolean, "this.devicePixelBoolean");
        if (this.devicePixelBoolean) {
          this.$emit("addInfo", this.dataForm);
        } else {
          const params1 = {
            applyType: "",
            category: this.dataForm.category,
            coordinate: "",
            height: "",
            id: "",
            imageUrl: "",
            imgSizeFrom: "",
            inScreenMode: this.dataForm.ACTION,
            remark: "",
            screenSize: this.dataForm.screenSize,
            stopTime: this.dataForm.STAY,
            vmsType: "",
            width: "",
          };
          const templateContent = [];
          templateContent.push({
            content: this.dataForm.CONTENT,
            coordinate: this.dataForm.COORDINATE,
            fontColor: this.getColorStyle(this.dataForm.COLOR),
            fontSize: this.dataForm.FONT_SIZE.substring(0, 2),
            fontSpacing: this.dataForm.SPEED,
            fontType: this.getFontStyle(this.dataForm.FONT),
          });
          // this.$emit("addInfoMode", this.dataForm);
          addTemplate(params1, method).then((data) => {
            console.log(data, "新增口");
            let params2 = {
              templateContent: templateContent,
              templateId: data,
            };
            addTemplateContent(params2).catch((err) => {
              throw err;
            });
            this.$emit("getActiveNames", this.dataForm.category);
          });
        }
      } else {
        console.log(this.dataForm);
        console.log(params);
        // 修改
        await editTemplate(this.dataForm).then((data) => {});

        this.templateContent.forEach((e) => {
          e.img = e.imageName;
        });

        var params = {
          templateContent: this.templateContent,
          templateId: this.dataForm.id,
          templateDelContent: this.templateDelContent,
        };
        editTemplateContent(params).then((response) => {
          console.log(response, "返回结果");
        });
      }
      this.loading = false;
      this.dialogVisible = false;
      this.isAdd = false;
      this.$emit("refreshDataList", this.dataForm);
    },
    /*********************************************业务代码***********************************************/
    getFontStyle(font) {
      if (font == "宋体") {
        return "Simsun";
      } else if (font == "黑体") {
        return "SimHei";
      } else if (font == "楷体") {
        return "KaiTi";
      }
    },
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "green";
      } else if (font == "蓝色") {
        return "blue";
      }
    },
    // 文字对齐方式
    alignment(alignmentNum) {
      var divContent = document.getElementsByClassName("blackBoard");
      var textBoard = document.getElementsByClassName("textBoard");
      // 获取文字长宽
      let textWidth = textBoard[0].offsetWidth;
      let textHeight = textBoard[0].offsetHeight;
      // 获取黑盒子长宽
      let divWidth = divContent[0].offsetWidth;
      let divHeight = divContent[0].offsetHeight;
      console.log(document.getElementsByClassName("textBoard"),"document.getElementsByClassName('textBoard')");
      console.log(textBoard[0].style,"textBoard[0].style");
      console.log(textWidth,divWidth,"999999999999999");
      switch (alignmentNum) {
        // 左对齐
        case 1:
          textBoard[0].style.left = "0px";
          textBoard[0].style.removeProperty("right");
          break;
        // 左右居中
        case 2:
          textBoard[0].style.left = (divWidth - textWidth) / 2 + "px";
          textBoard[0].style.right = "unset";

          break;
        // 右对齐
        case 3:
          textBoard[0].style.right = "0px";
          textBoard[0].style.removeProperty("left");
          break;
        // 上对齐
        case 4:
          textBoard[0].style.top = "0px";
          textBoard[0].style.removeProperty("bottom");
          break;
        // 上下对齐
        case 5:
          console.log(divHeight, textHeight, "00000");
          textBoard[0].style.top = (divHeight - textHeight) / 2 + "px";
          textBoard[0].style.bottom = "unset";
          break;
        // 下对齐
        case 6:
          textBoard[0].style.removeProperty("top");
          textBoard[0].style.bottom = "0px";
          break;
      }
      var textLeft = this.addZero(textBoard[0].offsetLeft);
      var textTop = this.addZero(textBoard[0].offsetTop);
      this.dataForm.COORDINATE = textLeft + textTop;
      console.log(this.dataForm.COORDINATE, "this.dataForm.COORDINATE");
    },
    addZero(num) {
      return ("000" + num).slice(-3);
    },
    /*增加新的内容*/
    addTemplateContent() {
      if (this.templateContent.length >= 7) {
        this.$modal.msgError("最多只能添加7条信息！");
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
      for (let i = 0; i < this.templateContent.length; i++) {
        if (
          this.templateContent.indexOf(data) ==
          this.templateContent.indexOf(this.templateContent[i])
        ) {
          if (this.templateContent.length == 1) {
            this.$modal.msgError("至少保留一条数据");
          } else {
            if (data.id) {
              this.templateDelContent.push(data);
            }
            this.templateContent.splice(this.templateContent.indexOf(data), 1);
          }
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
      this.$modal.msgError(
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
      console.log(params, "params");
      getGalleryList(params).then((data) => {
        console.log(data, "data");

        if (!data) {
          return;
        }
        let list = data.rows.sort((dataA, dataB) => {
          dataA.id - dataB.id;
        });
        this.imgUrl.push(...list);
        console.log(this.imgUrl, "this.imgUrl");
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
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
