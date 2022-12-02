<template>
    <div class="container">
      <!-- 添加信息弹窗 -->
      <el-dialog
        title="修改"
        :visible.sync="dialogVisible"
        width="60%"
        :before-close="closeDialog"
      >
        <el-card class="box-card" >
          <div
            class="blackBoard"
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
                width:boardWidth + 'px',
                height:boardHeight + 'px',
            }"
          >
            <div
              class="textBoard"
              style="line-height: 1; position: absolute; white-space: pre-wrap"
             
              :style="{
                color: dataForm.COLOR,
                fontSize: dataForm.FONT_SIZE,
                fontFamily: dataForm.FONT,
                letterSpacing: dataForm.SPEED + 'px',
              
                zIndex: '1000',
                left:dataForm.COORDINATE.substring(0, 3) + 'px',
                top:dataForm.COORDINATE.substring(3, 6) + 'px',
              }"
            v-html="dataForm.CONTENT.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp')"
            ></div>
          </div>
        </el-card>
        <el-row >
            <!-- <el-button type="primary" plain @click="addCurrRow">添加</el-button> -->
            <el-button type="info" plain @click="alignment(6)" size="mini">下对齐</el-button>
            <el-button type="info" plain @click="alignment(5)" size="mini">上下居中</el-button>
            <el-button type="info" plain @click="alignment(4)" size="mini">上对齐</el-button>
            <el-button type="info" plain @click="alignment(3)" size="mini">右对齐</el-button>
            <el-button type="info" plain @click="alignment(2)" size="mini">左右居中</el-button>
            <el-button type="info" plain @click="alignment(1)" size="mini">左对齐</el-button>
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
                <el-form-item prop="screenSize" label="屏幕尺寸">
                  <el-input
                    disabled
                    v-model="dataForm.screenSize"
                  ></el-input>
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
              <el-col :span="6">
              <el-form-item prop="category" label="所属类别">
                <el-select
                  v-model="dataForm.category"
                  placeholder="请选择所属类别"
                  size="small"
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
            </el-row>
            <el-row
              :gutter="24"
            >
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
              <!-- <el-col :span="6">
                <el-form-item prop="remark" label="备注">
                  <el-input v-model="dataForm.remark" style="width: 100%" />
                </el-form-item>
              </el-col> -->
            </el-row>
          </el-form>
        </el-card>
  
        <template slot="footer">
          <el-button size="small" @click="closeDialog()">取消</el-button>
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

  } from "@/api/board/template";
  import {
   
    HashMap,
  } from "@/api/board/informationBoard";
  export default {
  props: {
    boardEmitItem:{
      required:true,
      type:Object,
    }
  },

    data() {
      return {
      iotTemplateCategoryList: [],
      content: "",

        boardWidth:'',
        boardHeight:'',
        // boardEmitItem:this.boardEmitItem,
        disabled:true,
        fontSizeOpt: [],
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
          id: "",
          screenSize: "400*40", //屏幕尺寸
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
        },
        templateContentModel:'',
        templateContent: [],
        templateDelContent: [],
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
            code: "32px",
            name: "32px",
          },
          {
            code: "24px",
            name: "24px",
          },
          {
            code: "16px",
            name: "16px",
          },
          
        ],
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
          itemPropertyMap:null,
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
      "dataForm.CONTENT": {
        deep: true,
        handler: function (newValue, oldValue) {
          this.dataForm.content1 = newValue;
        },
      },
    },
    created(){
      console.log(this.boardEmitItem,"this.boardEmitItem")
      if(this.boardEmitItem){
        // console.log(this.boardEmitItem.screenSize,"this.boardEmitItem.screenSize");
        // console.log(this.boardEmitItem.screenSize.split("*")[0],"this.boardEmitItem.screenSize.split("*")[0]");

        this.boardWidth = this.boardEmitItem.screenSize.split("*")[0];
        this.boardHeight = this.boardEmitItem.screenSize.split("*")[1];
       
        this.init()
      }
      this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
    },
    mounted() {
      console.log(this.boardEmitItem,"boardEmitItemboardEmitItem")
      // if(this.boardEmitItem){
      //   this.boardWidth = this.boardEmitItem.screenSize.split("*")[0];
      //   this.boardHeight = this.boardEmitItem.screenSize.split("*")[1];
   
      //   this.init()
      // }
      
    },
    methods: {
      init() {
        this.title =  "修改";
        
        this.dialogVisible = true;
        this.itemPropertyMap = new HashMap();

        this.dataForm = this.boardEmitItem
      },
      alignment(alignmentNum) {
        console.log(alignmentNum,"alignmentNum");
        var divContent = document.getElementsByClassName("blackBoard")
        var textBoard = document.getElementsByClassName("textBoard")
        console.log(textBoard,"textBoard");
        console.log(divContent,"divContent");

        // 获取文字长宽
        let textWidth = textBoard[0].offsetWidth;
        let textHeight = textBoard[0].offsetHeight;
        // 获取黑盒子长宽
        let divWidth = divContent[0].offsetWidth;
        let divHeight = divContent[0].offsetHeight;
        switch (alignmentNum) {
          // 左对齐
          case 1:
            textBoard[0].style.left = '0px';
            textBoard[0].style.removeProperty('right')
            break;
            // 左右居中
          case 2:
            textBoard[0].style.left = (divWidth - textWidth)/2 +'px';
            // divContent[0].style.display='flex';
            // divContent[0].style.justifyContent= 'center';

            break;
            // 右对齐
          case 3:
            textBoard[0].style.right = '0px';
            textBoard[0].style.removeProperty('left')
            break;
            // 上对齐
          case 4:
            textBoard[0].style.top = '0px';
            textBoard[0].style.removeProperty('bottom')
            break;
            // 上下对齐
          case 5:
            console.log(divHeight,textHeight,"00000");
            textBoard[0].style.top = (divHeight - textHeight)/2 +'px';
            break;
            // 下对齐
          case 6:
            textBoard[0].style.removeProperty('top')
            textBoard[0].style.bottom = '0px';
            break;
        }
        var textLeft = this.addZero(textBoard[0].offsetLeft)
        var textTop = this.addZero(textBoard[0].offsetTop)
        console.log(textBoard[0].offsetLeft,textBoard[0].offsetTop,"9999999999");
        this.dataForm.COORDINATE = textLeft+textTop
        console.log(this.dataForm.COORDINATE,"this.dataForm.COORDINATE");
      },
      addZero(num) {
        return ('000' + num).slice(-3);
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
      keyDown(ev){
        // console.log(ev.keyCode,"ev.keyCode");
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
      // console.log( this.dataForm.content1," this.content");
      },
      // 表单确认
      dataFormSubmitHandle() {
        let valid = this.$refs.dataForm.validate().catch(() => {
          return this.$modal.msgError("校验错误");
        });
        if (!valid) return;
        this.loading = true;
        console.log(this.dataForm,"点击修改 表单");
       
        this.loading = false;
        this.isAdd = false;
        if(this.dataForm.type == 1){
          this.$emit("receiveForm", this.dataForm);
        }else{
          const tcontent = {
          content: this.dataForm.CONTENT,
          coordinate: this.dataForm.COORDINATE,
          createBy: null,
          createTime: null,
          fontColor: this.dataForm.COLOR,
          fontSize: this.dataForm.FONT_SIZE.substring(0, 2),
          fontSpacing: this.dataForm.SPEED,
          fontType: this.getFontStyle(this.dataForm.FONT),
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
        }
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
          screenSize:  this.dataForm.screenSize,
          searchValue: null,
          stopTime: this.dataForm.STAY,
          tcontents: null,
          templateType: null,
          updateBy: null,
          updateTime: null,
          vmsType: "",
          tcontent:tcontent,
          templateId:this.dataForm.id

        }
          editTemplate(param).then((data) => {});
          let templateContent = []
          templateContent.push({
            content: this.dataForm.CONTENT,
            fontColor: this.dataForm.COLOR,
            fontSize: this.dataForm.FONT_SIZE.substring(0, 2),
            fontType: this.getFontStyle(this.dataForm.FONT),
            fontSpacing: this.dataForm.SPEED,
            coordinate: this.dataForm.COORDINATE,
            id:this.dataForm.tcontentsId,
            templateId:this.dataForm.id
          });
        

          var params = {
            templateContent: templateContent,
            templateId: this.dataForm.id,
            templateDelContent: [],
          }
          editTemplateContent(params).then(response => {
            console.log(response,'返回结果');
          });
        }
        this.closeDialog()

      },
      getFontStyle(font) {
      if (font == "宋体") {
        return "Simsun";
      } else if (font == "黑体") {
        return "SimHei";
      } else if (font == "楷体") {
        return "KaiTi";
      }
    },
      closeDialog(){
        this.dialogVisible = false;
        this.$emit("dialogClose");

      },
      //将item属性存入Map
      // addItemPropertyMap(itemId, addFlg, stay, action, speed, coordinate, font, font_size, color, content) {
      //   if (!action) {
      //     action = 0;
      //   }
      //   this.addPropertyMap(itemId + '_STAY', this.itemPropertyMap, addFlg, stay);
      //   this.addPropertyMap(itemId + '_ACTION', this.itemPropertyMap, addFlg, action);
      //   this.addPropertyMap(itemId + '_SPEED', this.itemPropertyMap, addFlg, speed);
      //   this.addPropertyMap(itemId + '_COORDINATE', this.itemPropertyMap, addFlg, coordinate);
      //   this.addPropertyMap(itemId + '_FONT', this.itemPropertyMap, addFlg, font);
      //   this.addPropertyMap(itemId + '_FONT_SIZE', this.itemPropertyMap, addFlg, font_size);
      //   this.addPropertyMap(itemId + '_COLOR', this.itemPropertyMap, addFlg, color);
      //   this.addPropertyMap(itemId + '_CONTENT', this.itemPropertyMap, addFlg, content);
      // },
      // addPropertyMap(itemIdStr, map, addFlg, propertyStr) {
      //   var array = map.get(itemIdStr);
      //   if (array == null || addFlg) array = new Array();
      //   array.push(propertyStr);
      //   map.put(itemIdStr, array);
      // },
      /*********************************************业务代码***********************************************/
      
      // /*增加新的内容*/
      // addTemplateContent() {
      //   if (this.templateContent.length >= 7) {
      //     this.$modal.msgError("最多只能添加7条信息！");
      //     return;
      //   }
      //   this.templateContent.push({
      //     content: "请输入内容",
      //     fontColor: "yellow",
      //     fontSize: "24",
      //     fontType: "KaiTi",
      //     fontSpacing: 0,
      //     coordinate: "000000",
      //     img: "",
      //   });
      // },
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
      // cliTest(data) {
      //   this.ispreviewContent = this.templateContent.indexOf(data);
      // },
      /********************图片上传*********************/
      // handleRemove(file, fileList) {},
      // handlePreview(file) {},
      // handleExceed(files, fileList) {
      //   this.$modal.msgError(
      //     `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
      //       files.length + fileList.length
      //     } 个文件`
      //   );
      // },
      // beforeRemove(file, fileList) {
      //   return this.$confirm(`确定移除 ${file.name}？`);
      // },
  
      /**
       * 获取图片信息
       */
      // getImageInfo() {
      //   let params = {
      //     vmsSize: this.dataForm.screenSize,
      //   };
      //   console.log(params, "params");
      //   getGalleryList(params).then((data) => {
      //     console.log(data, "data");
  
      //     if (!data) {
      //       return;
      //     }
      //     let list = data.rows.sort((dataA, dataB) => {
      //       dataA.id - dataB.id;
      //     });
      //     this.imgUrl.push(...list);
      //     console.log(this.imgUrl, "this.imgUrl");
      //   });
      // },
      // handleClose(done) {
      //   this.$confirm("确认关闭？")
      //     .then((_) => {
      //       done();
      //     })
      //     .catch((_) => {});
      // },
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