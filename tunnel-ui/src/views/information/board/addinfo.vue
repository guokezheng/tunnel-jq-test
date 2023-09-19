<template>
  <div class="container infoBoardDialog">
    <!-- 添加信息弹窗 -->
    <el-dialog
      title="新增"
      :visible.sync="dialogVisible"
      width="42%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
      <el-card class="box-card">
        <div
          v-on:ondragenter="ondragenter"
          v-on:drop="faceDrop"
          v-on:dragover="allowDrop"
          :style="{
            width: boardWidth + 'px',
            height: boardHeight + 'px',
          }"
          class="blackBoard2"
        >
          <span
            :style="{
              color: getColorStyle(dataForm.COLOR),
              fontSize: dataForm.FONT_SIZE,
              fontFamily: dataForm.FONT,
              zIndex: '1000',
              left: dataForm.COORDINATE
                ? dataForm.COORDINATE.substring(0, 3) + 'px'
                : '',
              top: dataForm.COORDINATE
                ? dataForm.COORDINATE.substring(3, 6) + 'px'
                : '',
              maxHeight:boardHeight + 'px',
            }"
            class="textBoard2 boardTextStyle"
            v-html="
              dataForm.CONTENT
                ? dataForm.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                    / /g,
                    '&nbsp'
                  )
                : ''
            "
          ></span>
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
          <el-row :gutter="24" style="height: 45px;">
            <el-col :span="8">
              <el-form-item
                prop="category"
                label="所属类别"
                :rules="[
                  {
                    required: categoryRules ? true : false,
                    message: '请选择所属类别',
                    trigger: 'blur',
                  },
                ]"
                v-show="infoType == 2"
              >
                <el-select
                  v-model="dataForm.category"
                  placeholder="请选择所属类别"
                  clearable
                  size="mini"
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
            <el-col class="infoBoardButton" :span="16">
              <el-radio-group v-model="radio1" @input="alignment(radio1)">
                <el-radio-button :label="1">左对齐</el-radio-button>
                <el-radio-button :label="2">左右居中</el-radio-button>
                <el-radio-button :label="3">右对齐</el-radio-button>
              </el-radio-group>
              <!-- <el-button type="primary" @click="alignment(1)" size="mini" 
                >左对齐</el-button
              >
              <el-button type="primary" @click="alignment(2)" size="mini" 
                >左右居中</el-button
              >
              <el-button type="primary" @click="alignment(3)" size="mini" 
                >右对齐</el-button
              > -->
            </el-col>
          </el-row>
        
          <!-- 选择图片弹出框结束 -->
          <el-row :gutter="24" style="display: flex;
    flex-wrap: wrap;width:100%">
            <el-col :span="24">
              <el-form-item label="详细内容" prop="CONTENT">
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
            <el-col :span="8">
              <el-form-item prop="COLOR" label="字体颜色">
                <el-select
                  v-model="dataForm.COLOR"
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
            <el-col :span="8">
              <el-form-item prop="FONT_SIZE" label="字体大小">
                <el-select
                  v-model="dataForm.FONT_SIZE"
                  style="width: 100%"
                  @change="changeFontSize"
                >
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
            <el-col :span="8">
              <el-form-item prop="FONT" label="字体类型">
                <el-select
                  v-model="dataForm.FONT"
                  filterable
                  placeholder="请选择"
                >
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
          
            <el-col :span="8">
              <el-form-item prop="STAY" label="停留时间(秒)">
                <el-input-number
                  :min="0"
                  controls-position="right"
                  v-model="dataForm.STAY"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item prop="ACTION" label="入屏方式">
                <el-select
                  v-model="dataForm.ACTION"
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in inScreenModeOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item prop="screenSize" label="屏幕尺寸">
                <el-select
                  v-model="dataForm.screenSize"
                  filterable
                  placeholder="请选择"
                  v-if="!devicePixelBoolean"
                >
                  <el-option
                    v-for="item in screenSizeOptions"
                    :key="item.device_pixel"
                    :label="item.device_pixel"
                    :value="item.device_pixel"
                    @click.native="changeScreenSize(item.device_pixel)"
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
         
          </el-row>
        </el-form>
      </el-card>

      <div slot="footer" class="dialog-footer">
        <el-button
          @click="dataFormSubmitHandle()"
          v-loading="loading"
          class="submitButton"
          >确认
        </el-button>
        <el-button class="closeButton" @click="handleClose"
          >取消</el-button
        >
      </div>
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
  getFontSizeByDevicePixel,
  uploadBoardEditInfo,
} from "@/api/board/template";
import { devicessize } from "@/api/information/api.js";
import { checkIotBoardContent } from "@/api/board/vocabulary";
export default {
  data() {
    return {
      radio1:'0',
      alignmentNum:2,
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
      dataForm: {},
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
        CONTENT: [
          {
            required: true,
            message: "请输入详细内容",
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
      fontTypeOptions: [],
      screenSizeOptions: [],
      colorOptions: [],
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
      inScreenModeOptions: [],
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
      fontSizeOpt: [],
      title: "选择图片",
      loading: false,
      isAdd: false,
      iotTemplateCategoryList: [],
      infoType: "",
      devicePixelBoolean: false,
      categoryRules: false,
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
    // "dataForm.CONTENT": {
    //   deep: true,
    //   handler: function (newValue, oldValue) {
    //     this.dataForm.content1 = newValue;
    //   },
    // },
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
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      // console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
  },
  created() {
    this.getDicts("iot_device_font_type").then((res) => {
      this.fontTypeOptions = res.data;
      // console.log(this.fontTypeOptions, "字体类型");
    });
    this.getDicts("iot_devices_font_color").then((res) => {
      this.colorOptions = res.data;
      // console.log(this.colorOptions, "字体颜色");
    });
    this.getDicts("iot_device_font_inScreen_mode").then((res) => {
      this.inScreenModeOptions = res.data;
      // console.log(this.inScreenModeOptions, "入屏方式");
    });
    this.getdevicessize();
  },
  methods: {
    init(devicePixel, type, mode) {
      if (devicePixel) {
        this.devicePixelBoolean = true;
        this.dataForm.screenSize = devicePixel;
        this.boardWidth = devicePixel.split("*")[0];
        this.boardHeight = devicePixel.split("*")[1];
      } else {
        this.devicePixelBoolean = false;
        (this.boardWidth = "400"), (this.boardHeight = "40");
      }
      // type 1:待下发信息  2:信息模板 
      // mode 1:工作台弹窗跳转 2：情报板管理跳转

      this.infoType = type;
      if (mode == 1 || type == 1) {
        this.categoryRules = false;
      } else {
        this.categoryRules = true;
      }
      // this.title = "新增";
      this.isAdd = !this.dataForm.id;
      this.dialogVisible = true;
      console.log(this.dataForm.id, "这是模板id");
      this.templateDelContent = [];
      this.$nextTick(() => {
        if (this.isAdd) {
          console.log("-----------------------");
          this.$refs["dataForm"] && this.$refs["dataForm"].resetFields();
          this.dataForm.id = "";
          this.dataForm = {};
          this.width = "400";
          this.height = "40";
          this.dataForm = {
            CONTENT: "请输入内容",
            COLOR: "yellow",
            FONT: "黑体",
            SPEED: "1",
            ACTION: "1",
            // COORDINATE: "",
            STATE: "true",
            STAY: "5",
            screenSize: devicePixel,
          };
          this.content = "请输入内容";
        } else {
          // this.getInfo();
          // this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
        }
      });
      if (this.dataForm.screenSize) {
        this.getFontSizeList(1);
      }
      this.$forceUpdate();
    },
    getFontSizeList(type) {
      getFontSizeByDevicePixel(this.dataForm.screenSize).then((res) => {
        console.log(res, "根据分辨率筛字体大小");
        this.fontSizeOpt = res.data.fontSizeList;
        this.dataForm.FONT_SIZE = res.data.defaultFont;
        if(type){
          this.alignment(2)
        } 
      });
    },
    // 查分辨率
    getdevicessize() {
      devicessize().then((res) => {
        console.log(res, "查分辨率");
        this.screenSizeOptions = res.data;
      });
    },
    changeScreenSize(size) {
      this.boardWidth = size.split("*")[0];
      this.boardHeight = size.split("*")[1];
      this.getFontSizeList();
      this.$forceUpdate();
    },
    changeFontSize() {
      console.log(this.dataForm.FONT_SIZE)
      this.alignment(this.alignmentNum)
      this.$forceUpdate()
    },
    keyDown(ev) {
      this.alignment(this.alignmentNum)
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
    // getInfo() {
    //   console.log("=================")
    //   getTemplateInfo(this.dataForm.id).then((data) => {
    //     this.dataForm = data.data;
    //     this.width = this.dataForm.screenSize.split("*")[0];
    //     this.height = this.dataForm.screenSize.split("*")[1];
    //   });
    //   getTemplateContent(this.dataForm.id).then((data) => {
    //     this.templateContent = data.rows;

    //     if (this.templateContent.length == 0) {
    //       this.templateContent.push({
    //         content: "",
    //         fontColor: "yellow",
    //         fontSize: "24",
    //         fontType: "KaiTi",
    //         fontSpacing: 0,
    //         coordinate: "000000",
    //         img: "",
    //       });
    //     }
    //   });
    // },
    // 表单提交
    async dataFormSubmitHandle() {
      this.alignment()
      // let valid = await this.$refs.dataForm.validate().catch(() => {
      //   return this.$modal.msgError("校验错误");
      // });
      // if (!valid) return;
      if(!this.dataForm.CONTENT.trim()){
        return this.$modal.msgError("当前输入内容为空");
      }else if(!this.dataForm.category && this.infoType == 2){
        return this.$modal.msgError("情报板所属类别不能为空");
      }
      //走接口检验内容是否包含敏感字段
      checkIotBoardContent(this.dataForm.CONTENT).then((response) => {
        if (response.data == 0) {
          return this.$modal.msgError("当前发布内容包含敏感字段，请修改");
        } else if(response.data == 2){
          return this.$modal.msgError("当前输入内容为空");
        }
        else {
          this.loading = true;
          // let templateId = "";
          let method = "put";
          if (this.isAdd) {
            if (this.infoType == 1) {
              // 不走接口 存到待下发信息里
              this.dataForm.STAY = Number(this.dataForm.STAY) * 100;
              this.$emit("addInfo", this.dataForm);
            } else {
              // 走接口 存到信息模板里
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
                stopTime: Number(this.dataForm.STAY) * 100,
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
                addTemplateContent(params2)
                  .then((res) => {
                    if (res.code == 200) {
                      this.$emit("getActiveNames");
                      this.$message.success("添加成功");

                    }
                  })
                  .catch((err) => {
                    throw err;
                  });
              });
            }
          } else {
            // // 修改
            // editTemplate(this.dataForm).then((data) => {});

            // this.templateContent.forEach((e) => {
            //   e.img = e.imageName;
            // });

            // var params = {
            //   templateContent: this.templateContent,
            //   templateId: this.dataForm.id,
            //   templateDelContent: this.templateDelContent,
            // };
            // editTemplateContent(params).then((response) => {
            //   // console.log(response, "返回结果");
            // });
          }
          this.loading = false;
          this.dialogVisible = false;
          this.isAdd = false;
          this.$emit("refreshDataList", this.dataForm);
          var textBoard2 = document.getElementsByClassName("textBoard2");
          textBoard2[0].style.position = "absolute";
        }
      });
    },
    /*********************************************业务代码***********************************************/
    getFontStyle(font) {
      if (font == "宋体") {
        return "Simsun";
      } else if (font == "黑体") {
        return "SimHei";
      } else if (font == "楷体") {
        return "KaiTi";
      } else if(font == "仿宋"){
        return "FangSong";
      } else if(font == "隶书"){
        return 'LiSu';
      }
    },
    getColorStyle(font) {
      if (font == "黄色" ) {
        return "yellow";
      } else if (font == "红色" ) {
        return "red";
      } else if (font == "绿色" || font == 'GreenYellow') {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    // 文字对齐方式
    alignment(alignmentNum) {
      // console.log(alignmentNum,"alignmentNum")
      this.alignmentNum = alignmentNum;
      var divContent2 = document.getElementsByClassName("blackBoard2");
      var textBoard2 = document.getElementsByClassName("textBoard2");
      // 获取文字长宽
      // let textWidth = textBoard2[0].offsetWidth;
      // let textHeight = textBoard2[0].offsetHeight;
      // // 获取黑盒子长宽
      // let divWidth = divContent2[0].offsetWidth;
      // let divHeight = divContent2[0].offsetHeight;
      // console.log(
      //   document.getElementsByClassName("textBoard2"),
      //   "document.getElementsByClassName('textBoard2')"
      // );
      // var args =  [...divContent2];
      // console.log(divContent2, "divContent2");
      // console.log(args, "divContent2");

      // console.log(textWidth, divWidth, "999999999999999");
      switch (alignmentNum) {
        // 左对齐
        case 1:
          divContent2[0].style.justifyContent = "left";
          divContent2[0].style.alignItems = "center";
          // textBoard2[0].style.textAlign = "left";
          textBoard2[0].style.position = "static";

          break;
        // 左右居中
        case 2:
          divContent2[0].style.justifyContent = "center";
          divContent2[0].style.alignItems = "center";
          // textBoard2[0].style.textAlign = "center";
          textBoard2[0].style.position = "static";

          break;
        // 右对齐
        case 3:
          divContent2[0].style.justifyContent = "right";
          divContent2[0].style.alignItems = "center";
          // textBoard2[0].style.textAlign = "right";
          textBoard2[0].style.position = "static";

          break;
        // 上对齐
        case 4:
          divContent2[0].style.alignItems = "flex-start";
          textBoard2[0].style.position = "static";

          break;
        // 上下对齐
        case 5:
          divContent2[0].style.alignItems = "center";
          textBoard2[0].style.position = "static";

          break;
        // 下对齐
        case 6:
          divContent2[0].style.alignItems = "flex-end";
          textBoard2[0].style.position = "static";

          break;
      }
      if(!alignmentNum){
        divContent2[0].style.alignItems = "center";
        textBoard2[0].style.position = "static";
      }
      var textLeft = this.addZero(textBoard2[0].offsetLeft);
      var textTop = this.addZero(textBoard2[0].offsetTop);
      this.dataForm.COORDINATE = textLeft + textTop;
      // console.log(this.dataForm.COORDINATE, "this.dataForm.COORDINATE");
    },
    addZero(num) {
      return ("000" + num).slice(-3);
    },
    /*增加新的内容*/

    /*删除内容*/
    // delTemplateContent(data) {
    //   for (let i = 0; i < this.templateContent.length; i++) {
    //     if (
    //       this.templateContent.indexOf(data) ==
    //       this.templateContent.indexOf(this.templateContent[i])
    //     ) {
    //       if (this.templateContent.length == 1) {
    //         this.$modal.msgError("至少保留一条数据");
    //       } else {
    //         if (data.id) {
    //           this.templateDelContent.push(data);
    //         }
    //         this.templateContent.splice(this.templateContent.indexOf(data), 1);
    //       }
    //     }
    //   }
    // },
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

    handleClose(done) {
      var textBoard2 = document.getElementsByClassName("textBoard2");
      textBoard2[0].style.position = "absolute";
      this.dialogVisible = false;
    },
  },
};
</script>
<style lang="less" scoped>
  .infoBoardButton{
    ::v-deep .el-radio-button--medium .el-radio-button__inner{
      height: 3vh;
      line-height: 3vh;
      padding: 0 0.6vw;
    }
  }
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
.infoBoardButton {
  display: flex;
  justify-content: left;
}
.boardTextStyle {
  line-height: 1;
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
  position: absolute;
  // max-height: 128px;
  overflow: hidden;
}
.blackBoard2 {
  background: #000000;
  display: flex;
  margin: 0 auto;
  overflow: hidden;
  position: relative;
  // justify-content: center;
  align-items: center;
}
::v-deep .el-card__body {
  padding: 10px 0;
}
</style>
