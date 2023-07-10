<template>
  <div class="container infoBoardDialog">
    <!-- 添加信息弹窗 -->
    <el-dialog
      title="修改"
      :visible.sync="dialogVisible"
      width="44%"
      :before-close="closeDialog"
      :close-on-click-modal="false"
    >
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
      <el-card class="box-card">
        <div
          class="blackBoard1"
          v-on:ondragenter="ondragenter"
          v-on:drop="faceDrop"
          v-on:dragover="allowDrop"
          :style="{
            width: getDevicePixel(boardWidth, 0),
            height: getDevicePixel(boardHeight, 1),
          }"
        >
          <span
            class="textBoard1 boardTextStyle"
            :style="{
              color: getColorStyle(dataForm.COLOR),
              fontSize: getFontSize(dataForm.FONT_SIZE),
              fontFamily: dataForm.FONT,
              zIndex: '1000',
              left: getCoordinate(dataForm.COORDINATE.substring(0, 3), 0),
              top: getCoordinate(dataForm.COORDINATE.substring(3, 6), 1),
              maxHeight:getDevicePixel(boardHeight, 1),
            }"
            v-html="
              dataForm.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                / /g,
                '&nbsp'
              )
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
          <el-row :gutter="24" style="height: 45px">
            <el-col :span="8" v-show="this.boardEmitItem.type == 2 || this.dataForm.category">
              <el-form-item prop="category" label="所属类别">
                <el-select
                  v-model="dataForm.category"
                  placeholder="请选择所属类别"
                  size="mini"
                  disabled
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
            <el-col :span="16" class="infoBoardButton">
              <!-- <el-button type="info" plain @click="alignment(6)" size="mini">下对齐</el-button>
            <el-button type="info" plain @click="alignment(5)" size="mini">上下居中</el-button>
            <el-button type="info" plain @click="alignment(4)" size="mini">上对齐</el-button> -->
              <el-button type="primary" @click="alignment(1)" size="mini"
                >左对齐</el-button
              >
              <el-button type="primary" @click="alignment(2)" size="mini"
                >左右居中</el-button
              >
              <el-button type="primary" @click="alignment(3)" size="mini"
                >右对齐</el-button
              >

              <!-- <el-button type="primary" plain @click="addCurrRow">添加</el-button> -->
            </el-col>
          </el-row>
          <el-row :gutter="24">
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
                    :key="item.dictLabel"
                    :label="item.dictLabel"
                    :value="item.dictLabel"
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
                    :key="item.cssClass"
                    :label="item.dictLabel"
                    :value="item.cssClass"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="6">
                <el-form-item prop="SPEED" label="字体间距">
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="dataForm.SPEED"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col> -->
            <!-- <el-col :span="24" v-show="templateContent.length > 1">
                <el-divider></el-divider>
              </el-col> -->

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
                <el-input disabled v-model="dataForm.screenSize"></el-input>
                <!-- <el-select
                    @change="resolvingPowerType"
                    v-model="dataForm.screenSize"
                    filterable
                    placeholder="请选择"
                    disabled
                  >
                    <el-option
                      v-for="item in screenSizeOptions"
                      :key="item.type"
                      :label="item.type"
                      :value="item.type"
                    >
                    </el-option>
                  </el-select> -->
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

      <div slot="footer" class="dialog-footer">
        <el-button
          @click="dataFormSubmitHandle()"
          v-loading="loading"
          class="submitButton"
          >确认
        </el-button>
        <el-button @click="closeDialog()" class="closeButton"
          >取消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
  <script>
import {
  // getTemplateInfo,
  // addTemplate,
  // addTemplateContent,
  // editTemplateContent,
  // editTemplate,
  // deleteTemplate,
  // getTemplateContent,
  editTemplate,
  editTemplateContent,
  getGalleryList,
  uploadBoardEditInfo,
  getFontSizeByDevicePixel,
} from "@/api/board/template";
import { checkIotBoardContent } from "@/api/board/vocabulary";
import { HashMap } from "@/api/board/informationBoard";
// 对象深拷贝
export const deepClone = (data) => {
  // 封装的判断数据类型的方法
  var type = typeof data;
  var obj;
  if (type === "array") {
    obj = [];
  } else if (type === "object") {
    obj = {};
  } else {
    // 不再具有下一层次
    return data;
  }
  if (type === "array") {
    for (var i = 0, len = data.length; i < len; i++) {
      obj.push(deepClone(data[i]));
    }
  } else if (type === "object") {
    for (var key in data) {
      obj[key] = deepClone(data[key]);
    }
  }
  return obj;
};
export default {
  props: {
    boardEmitItem: {
      required: true,
      type: Object,
    },
  },

  data() {
    return {
      alignmentNum:2,
      iotTemplateCategoryList: [],
      content: "",

      boardWidth: "",
      boardHeight: "",
      // boardEmitItem:this.boardEmitItem,
      disabled: true,
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
        CONTENT:'',
        STAY:'',
        FONT:'',
        COORDINATE:'',

        // id: "",
        // screenSize: "400*40", //屏幕尺寸
        // inScreenMode: "1", //入屏方式
        // rollSpeed: "1000",
        // stopTime: "500",
        // applyType: "", //适用类型
        // vmsType: "", //情报板类型
        // remark: "", //备注
        // imgSizeFrom: "", //尺寸大小
        // imageUrl: "",
        // height: "",
        // width: "",
        // coordinate: "", //起始点位置;前3位代表x点的位值，后3位代表y点的位置
      },
      templateContentModel: "",
      templateContent: [],
      templateDelContent: [],
      fontTypeOptions: [],
      screenSizeOptions: [
        {
          type: "400*40",
        },
        {
          type: "128*64",
        },
      ],
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
    dataRule() {
      return {
        itemPropertyMap: null,
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
            trigger: "blur",
          },
        ],
        fontSpacing: [
          {
            required: true,
            message: "请选择字体间距",
            trigger: "blur",
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
    //   boardEmitItem(newval){
    //       console.log(newval,"newval")
    //       this.boardEmitItem = newval
    //   }
    // "dataForm.CONTENT": {
    //   deep: true,
    //   handler: function (newValue, oldValue) {
    //     this.dataForm.content1 = newValue;
    //   },
    // },
  },
  created() {
    console.log(this.boardEmitItem, "this.boardEmitItem");
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
    this.getDicts("iot_device_font_type").then((res) => {
        this.fontTypeOptions = res.data;
        console.log(this.fontTypeOptions, "字体类型");
      });
    this.getDicts("iot_devices_font_color").then((res) => {
      this.colorOptions = res.data;
      console.log(this.colorOptions, "字体颜色");
    });
    // this.getDicts("iot_device_font_size").then((res) => {
    //   this.fontSizeOpt = res.data;
    //   console.log(this.fontSizeOpt, "字体大小");
    // });
    this.getDicts("iot_device_font_inScreen_mode").then((res) => {
      this.inScreenModeOptions = res.data;
      console.log(this.inScreenModeOptions, "入屏方式");
    });
    if (this.boardEmitItem) {
      this.boardWidth = this.boardEmitItem.screenSize.split("*")[0];
      this.boardHeight = this.boardEmitItem.screenSize.split("*")[1];
      this.init();
    }
  },
  methods: {
    init() {
      this.title = "修改";

      this.dialogVisible = true;
      this.itemPropertyMap = new HashMap();
      this.alignmentNum = 2
      this.dataForm = JSON.parse(JSON.stringify(this.boardEmitItem));
      console.log(this.dataForm,"this.dataForm")
      // this.dataForm.FONT = this.getFont(this.boardEmitItem.FONT)
      this.dataForm.CONTENT = JSON.parse(
        JSON.stringify(
          this.boardEmitItem.CONTENT.replace("<br>", "\n").replace(/ /g, " ")
        )
      );
      this.dataForm.STAY = JSON.parse(
        JSON.stringify(Number(this.boardEmitItem.STAY) / 100)
      );
      this.getFontSizeList();
    },
    getFontSizeList() {
      getFontSizeByDevicePixel(this.dataForm.screenSize).then((res) => {
        console.log(res, "根据分辨率筛字体大小");
        this.fontSizeOpt = res.data.fontSizeList;
      });
    },
    changeFontSize() {
      setTimeout(() => {
        this.alignment(this.alignmentNum)
      },100);
    },
    alignment(alignmentNum) {
      var divContent1 = document.getElementsByClassName("blackBoard1");
      var textBoard1 = document.getElementsByClassName("textBoard1");

      switch (alignmentNum) {
        // 左对齐
        case 1:
          textBoard1[0].style.position = "static";
          divContent1[0].style.justifyContent = "left";
          divContent1[0].style.alignItems = "center";
          break;
        // 左右居中
        case 2:
          textBoard1[0].style.position = "static";
          divContent1[0].style.justifyContent = "center";
          divContent1[0].style.alignItems = "center";
          // console.log(textBoard1,"111111alignmentNum")
           break;
        // 右对齐
        case 3:
          divContent1[0].style.justifyContent = "right";
          divContent1[0].style.alignItems = "center";
          textBoard1[0].style.position = "static";
          break;
        // 上对齐
        case 4:
          divContent1[0].style.alignItems = "flex-start";
          textBoard1[0].style.position = "static";

          break;
        // 上下对齐
        case 5:
          divContent1[0].style.alignItems = "center";
          textBoard1[0].style.position = "static";

          break;
        // 下对齐
        case 6:
          divContent1[0].style.alignItems = "flex-end";
          textBoard1[0].style.position = "static";

          break;
      }

      var textLeft = this.addZero(JSON.parse(JSON.stringify(textBoard1[0].offsetLeft)));
      var textTop = this.addZero(textBoard1[0].offsetTop);

      this.dataForm.COORDINATE = textLeft + textTop;
    },
    addZero(num) {
      if(num<0){
        return '000'
      }else{
        return ("000" + num).slice(-3);
      }
    },
    faceDrop(e) {
      e.preventDefault(); //阻止默认行为
      this.listquery.push(this.curDragImgItem);
    },
    /* 拆分分辨率大小 */
    // resolvingPowerType(data) {
    //   let a = [];
    //   a = data.split("*");
    //   this.width = a[0];
    //   this.height = a[1];
    // },
    // 全选
    allowDrop(e) {
      e.preventDefault(); //阻止默认行为
    },
    ondragenter(e) {
      e.preventDefault(); //阻止默认行为
    },
    keyDown(ev) {
      this.alignment(this.alignmentNum)
    },
    // 表单确认
    dataFormSubmitHandle() {
      console.log(this.dataForm.type, "this.dataForm.type");
      if(!this.dataForm.CONTENT.trim()){
        return this.$modal.msgError("当前输入内容为空");
      }
      checkIotBoardContent(this.dataForm.CONTENT).then((response) => {
        if (response.data == 0) {
          return this.$modal.msgError("当前发布内容包含敏感字段，请修改");
        } else if(response.data == 2){
          return this.$modal.msgError("当前输入内容为空");
        }
        else {
          this.loading = true;
          console.log(this.dataForm, "点击修改 表单");

          this.loading = false;
          this.isAdd = false;
          if (this.dataForm.type == 1) {
            this.dataForm.STAY = Number(this.dataForm.STAY) * 100;
            this.$emit("receiveForm", this.dataForm);
            console.log(this.dataForm, "this.dataForm修改后给父组件传表单内容");
          } else {
            const tcontent = {
              content: this.dataForm.CONTENT,
              text: this.dataForm.CONTENT,
              coordinate: this.dataForm.COORDINATE,
              createBy: null,
              createTime: null,
              fontColor: this.dataForm.COLOR,
              fontSize: this.dataForm.FONT_SIZE.substring(0, 2),
              fontSpacing: this.dataForm.SPEED,
              fontType: this.dataForm.FONT,
              height: null,
              id: this.dataForm.id,
              imageUrl: null,
              params: {},
              remark: null,
              searchValue: null,
              templateId: this.dataForm.id,
              updateBy: null,
              updateTime: null,
              width: null,
            };
            const param = {
              applyType: "",
              category: this.dataForm.category,
              createBy: null,
              createTime: null,
              dictLable: null,
              id: this.dataForm.id,
              inScreenMode: this.dataForm.ACTION,
              isCurrency: null,
              params: {},
              remark: "",
              screenSize: this.dataForm.screenSize,
              searchValue: null,
              stopTime: Number(this.dataForm.STAY) * 100,
              tcontents: null,
              templateType: null,
              updateBy: null,
              updateTime: null,
              vmsType: "",
              tcontent: tcontent,
              templateId: this.dataForm.id,
            };
            editTemplate(param).then((data) => {});
            let templateContent = [];
            templateContent.push({
              content: this.dataForm.CONTENT,
              fontColor: this.dataForm.COLOR,
              fontSize: this.dataForm.FONT_SIZE.substring(0, 2),
              fontType: this.dataForm.FONT,
              fontSpacing: this.dataForm.SPEED,
              coordinate: this.dataForm.COORDINATE,
              id: this.dataForm.tcontentsId,
              templateId: this.dataForm.id,
            });

            var params = {
              templateContent: templateContent,
              templateId: this.dataForm.id,
              templateDelContent: [],
            };
            console.log(params,"params")
            editTemplateContent(params).then((response) => {
              console.log(response, "返回结果");
              this.$modal.msgSuccess("修改成功");
            });
            this.$forceUpdate();
          }
          this.closeDialog();
        }
      });
    },
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
    closeDialog() {
      this.dialogVisible = false;
      this.$emit("dialogClose");
    },
    getDevicePixel(devicePixel, num) {
      if (num == 0) {
        if (devicePixel > 768) {
          return 768 + "px";
        } else {
          return devicePixel + "px";
        }
      } else if (num == 1) {
        if (devicePixel > 128) {
          return 128 + "px";
        } else {
          return devicePixel + "px";
        }
      }
    },
    getCoordinate(coordinate, num) {
      if (num == 0) {
        if (this.boardWidth > 768) {
          let i = this.boardWidth / 768;
          console.log(coordinate / i,"width")
          return coordinate / i + "px";
        } else {
          return coordinate + "px";
        }
      } else if (num == 1) {
        if (this.boardHeight > 128) {
          let i = this.boardHeight / 128;
          console.log(coordinate / i,"height")
          return coordinate / i + "px";
        } else {
          return coordinate + "px";
        }
      }
    },
    getFontSize(size) {
      if (this.boardWidth > 768) {
        let i = this.boardWidth / 768;
        return size.substring(0, 2) / i - 2 + "px";
      } else {
        return size;
      }
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
.infoBoardButton {
  display: flex;
  justify-content: left;
}
::v-deep .el-card__body {
  padding: 10px 0;
}
.boardTextStyle {
  line-height: 1;
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
  position: absolute;
  // max-height: 128px;
  overflow: hidden;
}
.blackBoard1 {
  background: #000000;
  display: flex;
  margin: 0 auto;
  overflow: hidden;
  position: relative;
  // text-align: center;
  align-items: center;
}
</style>
