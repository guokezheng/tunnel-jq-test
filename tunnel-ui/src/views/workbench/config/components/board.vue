<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      class="workbench-dialog boardDialog"
      :title="title"
      width="620px"
      append-to-body
      :visible="cameraVisible"
      :before-close="handleClosee"
    >
      <div
        style="
          width: 100%;
          height: 30px;
          display: flex;
          justify-content: space-between;
        "
      >
        <div class="dialogLine"></div>
        <img
          :src="titleIcon"
          style="height: 30px; transform: translateY(-30px); cursor: pointer"
          @click="handleClosee"
        />
      </div>
      <div v-show="infoType == 'info'">
        <div class="infoBox">
          <div
            v-for="(item, index) of contentList"
            class="infoContent"
            :key="index"
          >
            <div class="upDown">
              <i
                :class="index == 0 ? 'disabledClass' : ''"
                class="el-icon-caret-top"
                size="18"
                style="cursor: pointer"
                @click="moveTop(index, item)"
              ></i>
              <i
                :class="contentList.length == index + 1 ? 'disabledClass' : ''"
                class="el-icon-caret-bottom"
                style="cursor: pointer"
                @click="moveBottom(index, item)"
              ></i>
            </div>
            <div class="contentBox">
              <div
                class="content"
                :style="{
                  color: getColorStyle(item.COLOR),
                  fontSize: getFontSize(item.FONT_SIZE, addForm.devicePixel),
                  fontFamily: item.FONT,
                  width: getDevicePixel(addForm.devicePixel, 0) + 'px',
                  height: getDevicePixel(addForm.devicePixel, 1) + 'px',
                }"
              >
                <span
                  :style="{
                    left: getCoordinate(item.COORDINATE.substring(0, 3),'left'),
                    top: getCoordinate(item.COORDINATE.substring(3, 6),'top'),
                  }"
                  style="position: absolute;line-height: 1;caret-color: rgba(0,0,0,0);"
                  v-html="
                    item.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                      / /g,
                      ' &nbsp'
                    )
                  "
                ></span>
              </div>
            </div>

            <div class="infoButton">
              <div  @click="openQbbDrawer(item, index, 1)"></div>
              <!-- <img
                src="../../../../assets/cloudControl/edit2.png"
                @click="openQbbDrawer(item, index, 1)"
              /> -->
              <div @click="delQbbDrawer(index)"></div>
            </div>
          </div>
        </div>
        <div
          class="openMIniDialogStyle"
          :class="openDialog ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'"
          @click="openMesMode"
        >
          信息模板
        </div>
        <el-form
          ref="form"
          :model="stateForm"
          label-width="75px"
          label-position="left"
          size="mini"
          style="padding: 0 15px 15px 15px"
        >
          <el-tabs class="videoTabs" v-model="videoActive">
            <el-tab-pane label="详细信息" name="information">
              <el-row>
                <el-col :span="9">
                  <el-form-item label="设备名称:">
                    {{ stateForm.typeName }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="隧道名称:">
                    {{ stateForm.tunnelName }}
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <el-form-item label="设备厂商:">
                    {{ stateForm.supplierName }}
                  </el-form-item>
                </el-col>
                <el-col :span="9">
                  <el-form-item label="所属机构:">
                    {{ stateForm.deptName }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="设备桩号:">
                    {{ stateForm.pile }}
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <el-form-item label="设备状态:" 
                  :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
                    {{ geteqType(stateForm.eqStatus) }}
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="设备参数" name="videoParams">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="IP:">
                    {{ stateForm.ip }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="端口:">
                    {{ stateForm.port }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="设备厂商:">
                    {{ stateForm.supplierName }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="型号:">
                    {{ stateForm.eqModel }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="分辨率:">
                    {{ addForm.devicePixel }}
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-form>
        <div
          slot="footer"
          style="
            display: flex;
            justify-content: right;
            margin-right: 20px;
            margin-bottom: 20px;
          "
        >
          <el-button
            type="primary"
            size="mini"
            @click="openDialogVisible(1,1)"
            style="width: 80px"
            class="submitButton"
            >添加信息</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="releaseInfo()"
            style="width: 80px"
            >信息发布</el-button
          >
        </div>
      </div>
      
    </el-dialog>
    <el-dialog
      class="workbench-dialog mesModeDialog"
      title="信息模板"
      width="520px"
      append-to-body
      :visible="mesModeVisible"
      :before-close="closeMesMode"
    >
      <div class="mesModeBg">
        <div class="mesModeBox">
          <el-collapse v-model="activeNames" @change="handleChange" accordion>
            <el-collapse-item
              v-for="(item, index) in iotTemplateCategoryList"
              :key="index"
              :title="item.dictLabel"
              :name="item.dictValue"
            >
              <div
                v-for="(itm, indx) in item.list"
                :key="indx"
                class="con"
                :style="{
                  'font-size': getFontSize(
                    itm.tcontents[0].fontSize,
                    itm.screenSize
                  ),
                  color: itm.tcontents[0].fontColor,
                  fontFamily: itm.tcontents[0].fontType,
                }"
              >
                <div class="templateTitle">
                  <div
                    :style="{
                      width: getDevicePixel(itm.screenSize, 0) + 'px',
                      height: getDevicePixel(itm.screenSize, 1) + 'px',
                    }"
                    style="background: black; position: relative"
                  >
                  <span
                    :style="{
                      left: getCoordinate(
                        itm.tcontents[0].coordinate.substring(0, 3),'left',
                        itm.screenSize
                      ),
                      top: getCoordinate(
                        itm.tcontents[0].coordinate.substring(3, 6),'top',
                        itm.screenSize
                      ),
                    }"
                    style="position: absolute;line-height: 1;caret-color: rgba(0,0,0,0);"
                    v-html="
                      itm.tcontents[0].content
                        .replace(/\n|\r\n/g, '<br>')
                        .replace(/ /g, ' &nbsp')
                    "
                  ></span>
                </div>
                  
                </div>
                <div class="downIcon">
                  <div
                    class="el-icon-d-arrow-left"
                    @click="arrowLeft(itm)"
                  ></div>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </el-dialog>
    <editInfo
      :boardEmitItem="this.boardEmitItem"
      @receiveForm="receiveForm"
      v-if="this.showEmit"
      @dialogClose="dialogClose1"
    ></editInfo>
    <addinfo ref="addinfo" @addInfo="addInfo"></addinfo>
  </div>
</template>

  <script>
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getInfo } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import { listDevice } from "@/api/board/informationBoard.js"; //查询弹窗信息
import {
  getAllVmsTemplate,
  uploadBoardEditInfo,
  getBoardContent,
} from "@/api/board/template";
import boardData from "@/views/information/board/boardData.json";
import editInfo from "@/views/information/board/editInfo";
import addinfo from "@/views/information/board//addinfo";

import { getBoardEditInfo } from "@/api/information/api.js";
export default {
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  components: {
    editInfo,
    addinfo,
  },
  data() {
    return {
      openDialog: false,
      associatedDeviceId: "",
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      title: "",
      cameraVisible: true, //摄像机弹窗
      videoActive: "information", // tab页
      stateForm: {}, //弹窗表单
      infoType: "info",
      mesModeVisible: false,
      boardEmitItem: {},
      showEmit: false,
      index_: 0,
      boardWidth: 400,
      boardHeight: 40,

      addForm: {
        category: "0",
        screenSize: "400*40",
        COORDINATE: "000000",
        CONTENT: "山东高速欢迎你",
        FONT: "黑体",
        FONT_SIZE: "32px",
        COLOR: "yellow",
        ACTION: 1,
        STAY: 500,
        position: 1,
      },
      iotTemplateCategoryList: [],
      screenSizeOptions: [
        {
          type: "400*40",
        },
        {
          type: "128*64",
        },
      ],
      fontTypeOptions: [
        // {
        //   code: "KaiTi",
        //   content: "楷体",
        // },
        // {
        //   code: "SimSun",
        //   content: "宋体",
        // },
        // {
        //   code: "SimHei",
        //   content: "黑体",
        // },
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
        //   code: "blue",
        //   content: "蓝色",
        // },
        // {
        //   code: "GreenYellow",
        //   content: "绿色",
        // },
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
      activeNames: "0",
      templateList: [],
      contentList: [
        // { CONTENT: "日照服务区可以做核酸", COLOR: "red", FONT_SIZE: "3232",COORDINATE:'000000',FONT:'黑体' },
        // { CONTENT: "日照服务区可以做核酸", COLOR: "red", FONT_SIZE: "3232",COORDINATE:'000000',FONT:'黑体' },
        // { CONTENT: "日照服务区可以做核酸", COLOR: "red", FONT_SIZE: "3232",COORDINATE:'000000',FONT:'黑体' },
      ],
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    this.getmessage();
    // this.getAllVmsTemplate();
    // this.onSubmit();
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
    this.getDicts("iot_devices_font_color").then((res) => {
      this.colorOptions = res.data;
      console.log(this.colorOptions, "字体颜色");
    });
    this.getDicts("iot_device_font_type").then((res) => {
      this.fontTypeOptions = res.data;
      console.log(this.fontTypeOptions, "字体类型");
    });


  },
  methods: {
    // 信息发布
    releaseInfo() {
      console.log(this.contentList, "this.contentListthis.contentList");
      var content = "";
      var playList = "[Playlist]<r><n>";
      var Item_Start = "ITEM_NO=";
      var Item_Content = "ITEM";
      content += playList;
      var length = parseInt(this.contentList.length);
      var Item_No = Item_Start + length + "<r><n>";
      var value = "";
      content += Item_No;
      for (var i = 0; i < this.contentList.length; i++) {
        // console.log(this.contentList[i].COLOR,"this.contentList[i].COORDINATE")
        value = ("000" + i).slice(-3);
        content += Item_Content + value + "=";
        content += this.contentList[i].STAY + ",";
        content += this.contentList[i].ACTION + ",";
        content += this.contentList[i].SPEED + "," + "\\";
        content += "C" + this.contentList[i].COORDINATE.replace("-",'0') + "\\";
        content += "S00\\";
        content += "c" + this.getColorValue(this.contentList[i].COLOR) + "\\";
        content += "f" + this.getFontValue(this.contentList[i].FONT);

        content +=
          this.contentList[i].FONT_SIZE.substring(0, 2) +
          this.contentList[i].FONT_SIZE.substring(0, 2);
        content += this.contentList[i].CONTENT.replace(/\n|\r\n/g, "<r><n>");

        if (i + 1 != this.contentList.length) {
          content += "<r><n>";
        }
        console.log(content, "content");
      }
      let protocolType = "GUANGDIAN_V33";
      let deviceld = this.associatedDeviceId.toString();
      uploadBoardEditInfo(deviceld, protocolType, content).then((response) => {
        this.$modal.msgSuccess("发布成功");
        console.log(response, "返回结果");
      });
    },
    getFontValue(font) {
      if (font == "黑体") return "h";
      if (font == "楷体") return "k";
      if (font == "仿宋") return "f";
      if (font == "隶书") return "l";
      return "s";
    },
    getColorValue(color) {
      if (color == "蓝色" || color == 'blue') return "000000255000";
      if (color == "绿色" || color == 'GreenYellow') return "000255000000";
      if (color == "透明色" || color == 'transparent') return "t";
      if (color == "红色" || color == 'red') return "255000000000";
      return "255255000000"; //黄色
    },
    
    // 打开添加信息弹窗
    openDialogVisible(type,mode) {
      // this.devicePixel = this.form.devicePixel
      if (type == 1) {
        this.$refs.addinfo.init(this.addForm.devicePixel, type,mode);
      } else {
        this.$refs.addinfo.init(this.devicePixelMode, type,mode);
      }
    },
    // 接收子组件新增待发模板
    addInfo(form) {
      console.log(form, "待发新增");
      this.contentList.push(form);
      this.$forceUpdate();
    },
    // 新增 修改分辨率
    changeScreenSize(size) {
      this.boardWidth = size.split("*")[0];
      this.boardHeight = size.split("*")[1];

      this.$forceUpdate();
    },
    // 接收子组件form表单 修改
    receiveForm(form) {
     console.log(form,"接收子组件form表单 修改")
      this.contentList[this.index_] = form;
      this.$forceUpdate();
      console.log(this.contentList, "this.contentList");

    },
    // 编辑
    openQbbDrawer(item, index, type) {
      this.index_ = index;
      console.log(item);
      this.boardEmitItem = item;
      this.boardEmitItem.screenSize = this.addForm.devicePixel;
      this.boardEmitItem.type = type;
      this.showEmit = true;
    
    },
   
    onSubmit() {
      getBoardEditInfo(this.associatedDeviceId).then((response) => {
        var parseObject = JSON.parse(response.data[0]);
        console.log(parseObject,"parseObject");
        var protocolType = parseObject.support.PROTOCOL_TYPE;
        var contents = parseObject.content;
        this.contentList = []
        for (var i = 0; i < contents.length; i++) {
          var content = contents[i];
          var itemId = "ITEM" + this.formatNum(i, 3);
          for(var itm of content[itemId]){
            itm.COLOR = this.getColorStyle(itm.COLOR);
            itm.FONT_SIZE = Number(itm.FONT_SIZE.substring(0, 2)) + "px";
            this.contentList.push(itm);
          }
        }
        console.log(this.contentList, "this.contentList11");

      });
    },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },
    // 删除中间模板
    delQbbDrawer(index) {
      console.log(index);
      if (index > -1) {
        this.contentList.splice(index, 1);
        this.$forceUpdate();
      }
    },
    // 信息模板转到待下发
    arrowLeft(item) {
      console.log(item, "item");
      var contentList = {
        FONT_SIZE: item.tcontents[0].fontSize + "px",
        COLOR: item.tcontents[0].fontColor,
        CONTENT: item.tcontents[0].content,
        COORDINATE: item.tcontents[0].coordinate,
        FONT: this.getFont(item.tcontents[0].fontType),
        SPEED: item.tcontents[0].fontSpacing, //字体间距
        ACTION: item.inScreenMode, //出屏方式
        STAY: item.stopTime, //停留时间
        category: item.category, //所属类别
      };
      this.contentList.push(contentList);
      console.log(this.contentList, "this.contentList");
    },
    // 转字体
    getFont(font) {
      if (font == "KaiTi") {
        return "楷体";
      } else if (font == "SimSun") {
        return "宋体";
      } else if (font == "SimHei") {
        return "黑体";
      } else {
        return font;
      }
    },
    // 情报板管理右侧查询接口
    allVmsTemplate() {
      const param = {
        devicePixel: this.addForm.devicePixel,
        category: 0,
      };
      getAllVmsTemplate(param).then((res) => {
        let data = res.data;
        console.log(res, "情报板管理右侧查询接口");
        for(let j = 0;j < this.iotTemplateCategoryList.length;j++){
            let arr = this.iotTemplateCategoryList[j];
            let brr = data[j];
            arr.list = brr
        }
        this.$forceUpdate()
        console.log(this.iotTemplateCategoryList,"新模板")
        // console.log(this.templateList,"this.templateList");
      });
    },
    //  上移
    moveTop(i, item) {
      if (item && i) {
        let obj = { ...this.contentList[i - 1] };
        this.contentList.splice(i - 1, 1, item);
        this.contentList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    // 下移
    moveBottom(i, item) {
      if (item && typeof i === "number") {
        let obj = { ...this.contentList[i + 1] };
        this.contentList.splice(i + 1, 1, item);
        this.contentList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    // 切换模板
    handleChange(active) {
      console.log(active);
      this.activeNames = active;
      const param = {
        devicePixel: this.addForm.devicePixel,
        category: active,
      };
      getAllVmsTemplate(param).then((res) => {
        console.log(res, "情报板管理右侧查询接口");
        this.templateList = res.data;
        // console.log(this.templateList,"this.templateList");
      });
    },

    getCoordinate(coordinate, type, screenSize) {
      let width = "";
      let height = "";
      if (!screenSize) {
        width = this.addForm.devicePixel.split("*")[0];
        height = this.addForm.devicePixel.split("*")[1];
      } else {
        width = screenSize.split("*")[0];
        height = screenSize.split("*")[1];
      }
      if (width < 394 && height < 65) {
        return coordinate + "px";
      } else {
        if (width / 394 > height / 65) {
          if (type == "left") {
            return coordinate / (width / 394) + "px";
          } else if (type == "top") {
            return coordinate / (width / 394) + "px";
          }
        } else {
          // console.log(coordinate,"coordinate")
          if (type == "left") {
            if (!screenSize) {
              return coordinate / (height / 65) + 10 + "px";
            } else {
              return coordinate / (height / 65) + "px";
            }
          } else if (type == "top") {
            if (!screenSize) {
              return coordinate / (height / 65) + 4 + "px";
            } else {
              return coordinate / (height / 65) + "px";
            }
          }
        }
      }
    },
    // 转分辨率
    getDevicePixel(devicePixel, type) {
      let width = devicePixel.split("*")[0];
      let height = devicePixel.split("*")[1];
      // 实际分辨率比页面板子小
      if (width < 394 && height < 65) {
        if (type == 0) {
          return width;
        } else if (type == 1) {
          return height;
        }
      } else {
        // 实际分辨率比页面板子大
        if (width / 394 > height / 65) {
          if (type == 0) {
            return 394;
          } else if (type == 1) {
            return height / (width / 394);
          }
        } else {
          if (type == 0) {
            return width / (height / 65);
          } else if (type == 1) {
            return 65;
          }
        }
      }
    },
    // 转字号
    getFontSize(font, screenSize) {
      if (!font) {
        return;
      }
      let width = screenSize.split("*")[0];
      let height = screenSize.split("*")[1];
      
      if (width < 394 && height < 65) {
        if (font.toString().length == 2) {
          return font + "px";
        } else {
          return font.substring(0, 2) + "px";
        }
      } else {
        if (width / 394 > height / 65) {
          if (font.toString().length == 2) {
            return font / (width / 394) - 4 + "px";
          } else {
            return font.substring(0, 2) / (width / 394) - 4 + "px";
          }
        } else {
          if (font.toString().length == 2) {
            return font / (height / 65) - 4 + "px";
          } else {
            return font.substring(0, 2) / (height / 65) - 4 + "px";
          }
        }
      }
    },
    // 转颜色
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "green";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    // 打开信息模板
    openMesMode() {
      if (this.openDialog == false) {
        this.mesModeVisible = true;
      } else {
        this.mesModeVisible = false;
      }
    },
    // 关闭信息模板
    closeMesMode() {
      this.mesModeVisible = false;
    },
    // 根据设备id 获取弹窗内信息
    async getmessage() {
      if (this.eqInfo.equipmentId) {
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询情报板弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          this.associatedDeviceId = res.data.associatedDeviceId;
          this.onSubmit();

          // displayH5sVideoAll(res.data.secureKey);
          if (res.data.associatedDeviceId) {
            const param = {
              deviceId: res.data.associatedDeviceId,
            };
            listDevice(param).then((response) => {
              console.log(response, "查询设备信息");
              this.addForm = response.rows[0];
              this.allVmsTemplate();
            });
          }
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    // addInfo() {
    //   this.infoType = "add";
    // },
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    getBrandName(num) {
      // 根据字典表查设备厂商--------------------------
      for (var item of this.brandList) {
        if (Number(item.dictValue) == num) {
          return item.dictLabel;
        }
      }
    },
    geteqType(num) {
      for (var item of this.eqTypeDialogList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 关闭弹窗
    handleClosee() {
      getBoardContent(this.associatedDeviceId).then((res) => {
        console.log(response, "情报板内容查询");
      }).catch(e => {
        console.log(e);
      })
      this.$emit("dialogClose");
    },
    dialogClose1() {
      this.showEmit = false;
    },
  },
};
</script>

  <style lang="scss" scoped>
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.openMIniDialogStyle {
  position: absolute;
  right: 0;
  width: 20px;
  height: 85px;
  background: #d8d8d8 linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  top: 66px;
  color: #fff;
  font-size: 12px;
  line-height: 16px;
  text-align: center;
  cursor: pointer;
}
::v-deep .el-tabs__nav-wrap::after {
  background-color: #dfe4ed;
  opacity: 0.4;
}
::v-deep .el-tabs__active-bar {
  background-color: #01aafd;
}

::v-deep .el-radio-button--mini .el-radio-button__inner {
  padding: 6px 13px !important;
}
::v-deep .el-radio-group .el-radio-button__inner {
  background: white;
}
.boardDialog {
  left: 20%;
  margin: unset;
  width: 620px;
  z-index: 2017;
}
.mesModeDialog {
  left: 53%;
  margin: unset;
  width: 530px;
  z-index: 2017;
  .mesModeBg {
    padding: 10px;
    background: #012e51;
    width: calc(100% - 30px);
    height: 403px;
    margin: 10px auto;
    overflow: auto;
    .el-collapse-item__header {
      background: #012e51;
      color: #fff;
    }
    .mesModeBox {
      width: 100%;
      // height: 100px;
      border: solid 1px #01aafd;
      .con {
        width: 100%;
        height: 80px;
        margin-bottom: 10px;
        overflow: hidden;
        display: flex;
        border: 1px solid #01aafd;
        align-items: center;
        .templateTitle {
          height: 75px;
          // border: 1px solid #01aafd;
          // background: black;
          // position: relative;
          display: flex;
          justify-content: center;
          align-items: center;
          width: 395px;
          float: left;
        }
        .downIcon {
          width: 50px;
          height: 50px;
          // border-left: solid 1px #f3f3f3;
          > div {
            width: 40px;
            height: 40px;
            border: solid 1px #cfd5e0;
            border-radius: 2px;
            text-align: center;
            line-height: 40px;
            font-size: 16px;
            color: #00162c;
            display: block;
            margin-top: 5px;
            margin-left: 5px;
            cursor: pointer;
            background: #f2f8ff;
          }
        }
        .menuBox {
          display: flex;
          // align-items: center;
          margin: 28px 0;
          float: right;
          i {
            font-size: 24px;
            color: #666;
          }
          .disabledClass {
            pointer-events: none;
            cursor: auto !important;
            color: #ccc;
          }
        }
      }

      .mesModeTitle {
        font-size: 14px;
        color: rgba(0, 22, 44, 0.7);
        line-height: 30px;
        height: 30px;
        width: 100%;
        border: solid 1px #cfd5e0;
      }
      .mesModeContent {
        display: flex;
        width: 100%;
        height: 70px;
        .mesModeLeft {
          width: 280px;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }
        .mesModeRight {
          width: calc(100% - 280px);
          height: 100%;
          border-left: solid 1px #cfd5e0;
        }
      }
    }
  }
}
.infoBox {
  width: 93%;
  height: 200px;
  // background: #fff;
  margin: 0 30px 0 15px;
  overflow-y: auto;
  overflow-x: hidden;
  border: solid 1px #01aafd;
  .infoContent {
    width: 97%;
    height: 75px;
    margin: 5px 10px;
    display: flex;
    .upDown {
      width: 30px;
      height: 100%;
      border: solid 1px #01aafd;
      text-align: center;
      writing-mode: tb-rl;
      white-space: nowrap;
      display: flex;
      align-items: center;
      justify-content: center;
      i {
        display: block;
        color: #f2f8ff;
      }
      i:nth-of-type(2) {
        padding-top: 10px;
      }
    }
    .contentBox {
      width: calc(100% - 160px);
      height: 100%;
      border: solid 1px #01aafd;
      margin-left: 4px;
      overflow: hidden;
      .content {
        // width: calc(100% - 10px);
        // height: calc(100% - 10px);
        background: #000;
        margin: 5px auto;
        overflow: hidden;
        position: relative;
      }
    }
    .infoButton {
      width: 112px;
      height: 100%;
      border: solid 1px #01aafd;
      margin-left: 4px;
      display: flex;
      align-items: center;
      justify-content: space-around;
      // img {
      //   width: 40px;
      //   height: 40px;
      //   cursor: pointer;
      // }
      > div {
        width: 40px;
        height: 40px;
        line-height: 40px;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        cursor: pointer;
      }
      >div:nth-of-type(1){
        background-image: url(../../../../assets/cloudControl/edit3.png);
      }
      >div:nth-of-type(2){
        background-image: url(../../../../assets/cloudControl/edit4.png);
      }
      >div:nth-of-type(1):hover{
        background-image: url(../../../../assets/cloudControl/edit1.png);
      }
    }
  }
}
.disabledClass {
  pointer-events: none;
  cursor: auto !important;
  color: #00152B  !important;
}
::v-deep .el-collapse-item__header {
  height: 30px;
  background: #012e51;
  color: #fff;
  border-bottom: solid 1px #01aafd;
}
::v-deep .el-collapse-item__content {
  padding-bottom: 10px;
}
::v-deep .el-collapse-item__wrap {
  padding: 0 10px;
  background: #012e51;
  border-bottom: solid 1px #01aafd;
}
::v-deep ::-webkit-scrollbar {
  width: 0px !important;
}
</style>
