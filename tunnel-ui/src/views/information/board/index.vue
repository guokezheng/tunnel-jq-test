<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-08 10:04:52
 * @FilePath: \tunnel-ui\src\views\information\board\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="container infoBoardBox" style="height: 100%">
    <el-row
      :gutter="20"
      style="height: 100%; margin-left: 0px; margin-right: 0px"
    >
      <el-col :span="4">
        <p class="bigTitle">情报板列表</p>
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="所属单位">
            <el-select
              v-model="form.company"
              placeholder="请选择所属单位"
              clearable
              size="small"
            >
              <el-option
                v-for="item in deptList"
                :key="item.deptId"
                :label="item.deptName"
                :value="item.deptId"
                @click.native="changeCompany(item.deptId)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属机构">
            <el-select
              v-model="form.mechanism"
              placeholder="请选择所属机构"
              clearable
              size="small"
            >
              <el-option
                v-for="item in mechanismList"
                :key="item.deptId"
                :label="item.deptName"
                :value="item.deptId"
                @click.native="changeMechanism(item.deptId)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属隧道">
            <el-select
              v-model="form.tunnel"
              placeholder="请选择所属隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="位置信息">
            <el-select
              v-model="form.localInfo"
              placeholder="请选择位置信息"
              clearable
              size="small"
            >
              <el-option
                v-for="item in positionList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="分辨率">
            <el-select
              v-model="form.devicePixel"
              placeholder="请选择分辨率"
              clearable
              size="small"
            >
              <el-option
                v-for="item in devicessizeList"
                :key="item.device_pixel"
                :label="item.device_pixel"
                :value="item.device_pixel"
                @click.native="changeDevicessize(item.device_pixel)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label-width="0" v-show="checkbox">
            <el-checkbox
              style="width: 100%"
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
              >全选</el-checkbox
            >
            <div style="margin: 15px 0"></div>
            <el-checkbox-group
              class="checkbox"
              v-model="checkedCities"
              @change="handleCheckedCitiesChange"
            >
              <el-checkbox
                v-for="(item, index) in checkboxList"
                :label="item.deviceId"
                :key="index"
                >{{ item.deviceName }}</el-checkbox
              >
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">确认</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="10" style="border-left: 1px solid #f3f3f3">
        <p class="bigTitle">待下发信息</p>
        <div class="contentBox">
          <div class="controlBox">
            <el-button
              @click.native="openDialogVisible(1)"
              :disabled="disabledButton"
              >添加信息</el-button
            >
            <el-button type="primary" @click="publishInfo">发布信息</el-button>
          </div>
          <div
            v-for="(item, index) in contentList"
            :key="index"
            class="listBox"
          >
            <div class="indexBox">
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
            <div
              class="con"
              :style="{
                color: getColorStyle(item.COLOR),
                fontSize: getFontSize(item.FONT_SIZE),
                fontFamily: item.FONT,
              }"
            >
              <span
                :style="{
                  left: getCoordinate1(item.COORDINATE.substring(0, 3)),
                  top: getCoordinate2(item.COORDINATE.substring(3, 6)),
                }"
                style="position: absolute"
                v-html="
                  item.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                    / /g,
                    ' &nbsp'
                  )
                "
              ></span>
              <!-- {{ item.CONTENT }} -->
            </div>
            <div class="menuBox">
              <i class="el-icon-d-arrow-right" @click="arrowRight(item)"></i>
              <i
                class="el-icon-edit-outline"
                @click="openQbbDrawer(item, index, 1)"
              ></i>
              <i class="el-icon-close" @click="delQbbDrawer(index)"></i>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="10" style="border-left: 1px solid #f3f3f3">
        <p class="bigTitle">信息模板</p>
        <div class="templateBox">
          <div class="controlBox">
            <el-button type="primary" @click="openDialogVisible(2)"
              >添加模板</el-button
            >
          </div>
          <el-collapse v-model="activeNames" @change="handleChange" accordion>
            <el-collapse-item
              v-for="(item, index) in iotTemplateCategoryList"
              :key="index"
              :title="item.dictLabel"
              :name="item.dictValue"
            >
              <div
                v-for="(itm, indx) in templateList"
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
                  <span
                    :style="{
                      left: getCoordinate1(
                        itm.tcontents[0].coordinate.substring(0, 3),
                        itm.screenSize
                      ),
                      top: getCoordinate2(
                        itm.tcontents[0].coordinate.substring(3, 6),
                        itm.screenSize
                      ),
                    }"
                    style="position: absolute"
                    v-html="
                      itm.tcontents[0].content
                        .replace(/\n|\r\n/g, '<br>')
                        .replace(/ /g, ' &nbsp')
                    "
                  ></span>
                </div>
                <div class="menuBox">
                  <i
                    class="el-icon-d-arrow-left"
                    @click="arrowLeft(itm)"
                    :class="
                      disabledButton || itm.screenSize != form.devicePixel
                        ? 'disabledClass'
                        : ''
                    "
                    style="cursor: pointer"
                  ></i>

                  <i
                    class="el-icon-edit-outline"
                    @click="editOutline(itm, indx, 2)"
                  ></i>
                  <!-- <i class="el-icon-close"></i> -->
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-col>
    </el-row>
    <addinfo
      ref="addinfo"
      @addInfo="addInfo"
      @getActiveNames="getActiveNames"
    ></addinfo>
    <editInfo
      :boardEmitItem="this.boardEmitItem"
      @receiveForm="receiveForm"
      v-if="this.showEmit"
      @dialogClose="dialogClose"
    ></editInfo>
    <!-- <editInfo @boardEmitItem = 'boardEmitItem' v-if="boardEmitItem"></editInfo> -->
  </div>
</template>
<script>
import addinfo from "./addinfo";
import editInfo from "./editInfo";
import boardData from "./boardData";
import { getUserDeptId } from "@/api/system/user";
import { listDept } from "@/api/system/dept";
import {
  listTunnels,
  devicessize,
  information,
  getBoardInfo,
  getBoardEditInfo,
} from "@/api/information/api.js";
import {
  uploadBoardEditInfo,
  getAllVmsTemplate,
  addTemplate,
  addTemplateContent,
  getBoardContent
} from "@/api/board/template";
const cityOptions = ["上海", "北京", "广州", "深圳"];
export default {
  name: "Device",
  components: {
    addinfo,
    editInfo,
  },
  data() {
    return {
      devicePixelMode: null,
      editType: 0,
      disabledButton: true,
      // devicePixel:'',
      showEmit: false,
      index_: 0,
      index: 0,
      deviceId: "",
      boardEmitItem: null,
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      userDeptId: "",
      tunnelQueryParams: {
        deptId: this.userDeptId,
      },
      deptList: [], //分中心下拉框
      mechanismList: [], //管理机构下拉框
      tunnelData: [], //所属隧道下拉框
      positionList: [], //位置信息下拉框
      devicessizeList: [], //分辨率下拉框
      checkboxList: [], //多选框数组
      checkbox: false,
      checkboxValue: [],
      checkAll: false,
      checkedCities: [],
      form: {
        company: null,
        mechanism: null,
        tunnel: null,
        localInfo: null,
        devicePixel: null,
      },
      boardStyle: {},
      itemStr: "",
      supplier: null,
      disContentList: [],
      transSign: [
        ["C", "f", "c", "", "\\\\n"],
        ["C", "F", "T", "U", "\\\\N"],
        ["C", "F", "T", "W", "\\\\A"],
        ["C", "f", "c", "", "\\\\n"],
      ],
      isactive: 0,
      boardEidtContentArea: "",
      itemEditCoordinate: null,
      itemEditFont: null,
      itemEditFontSize: null,
      itemEditColor: null,
      itemEditContents: null,
      itemEditContent: null,
      boardContentS: null,
      itemPropertyMap: null,
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

      dialogVisible: false,
      activeNames: "0",
      iotTemplateCategoryList: [],
      isIndeterminate: true,
      contentList: [
        // { content: "日照服务区可以做核酸", color: "red", size: "20" },
        // { content: "日照服务区可以做核酸", color: "red", size: "20" },
      ],
      templateList: [
        // {
        //   id: "1",
        //   name: "默认模板",
        //   list: [
        //     { content: "日照服务区可以做核酸", color: "red", size: "20" },
        //     { content: "日照服务区可以做核酸", color: "red", size: "20" },
        //   ],
        // },
        // {
        //   id: "2",
        //   name: "日常通用",
        //   list: [
        //     { content: "日照服务区可以做核酸", color: "red", size: "20" },
        //     { content: "日照服务区可以做核酸", color: "red", size: "20" },
        //   ],
        // },
      ],
    };
  },
  created() {
    this.getDeptList();
    this.getAllVmsTemplate();
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
      this.$forceUpdate();
    });
  },
  methods: {
    getActiveNames(active) {
      console.log(active);
      this.activeNames = active;
      const param = {
        devicePixel: this.form.devicePixel,
        category: active,
      };
      getAllVmsTemplate(param).then((res) => {
        console.log(res, "情报板管理右侧查询接口");
        this.templateList = res.data;
        // console.log(this.templateList,"this.templateList");
      });
    },
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
    arrowRight(item) {
      console.log(item, "item");
      let templateId = "";
      let method = "put";
      const params1 = {
        inScreenMode: item.ACTION,
        screenSize: this.form.devicePixel,
        applyType: "",
        category: item.category,
        coordinate: "",
        height: "",
        id: "",
        imageUrl: "",
        imgSizeFrom: "",
        remark: "",
        stopTime: item.STAY,
        vmsType: "",
        width: "",
      };
      // 新增
      addTemplate(params1, method).then((data) => {
        console.log(data, "新增口");
        const templateContent = [];
        templateContent.push({
          content: item.CONTENT,
          coordinate: item.COORDINATE,
          fontColor: this.getColorStyle(item.COLOR),
          fontSize: item.FONT_SIZE.substring(0, 2),
          fontSpacing: item.SPEED,
          fontType: this.getFontStyle(item.FONT),
        });
        const params2 = {
          templateContent: templateContent,
          templateId: data,
        };
        addTemplateContent(params2).catch((err) => {
          throw err;
        });
        this.getActiveNames(item.category);
      });
    },
    editOutline(item, index, type) {
      console.log(item, index, "item,index");
      this.index = index;
      this.editType = type;
      console.log(item);
      this.boardEmitItem = {
        FONT_SIZE: item.tcontents[0].fontSize + "px",
        COLOR: item.tcontents[0].fontColor,
        CONTENT: item.tcontents[0].content,
        COORDINATE: item.tcontents[0].coordinate,
        FONT: this.getFont(item.tcontents[0].fontType),
        SPEED: item.tcontents[0].fontSpacing,
        ACTION: item.inScreenMode, //出屏方式
        STAY: item.stopTime, //停留时间
        type: type,
        screenSize: item.screenSize,
        category: item.category,
        id: item.id,
        tcontentsId: item.tcontents[0].id,
      };
      // console.log(this.form.devicePixel,"this.form.devicePixel");
      // this.boardEmitItem.screenSize = item.screenSize;
      // this.boardEmitItem.category = item.category;
      // this.boardEmitItem.deviceId = this.deviceId;
      console.log(this.showEmit, "this.showEmit");
      this.showEmit = true;
    },
    // 获取折叠面板字典值
    getListTitle(index) {
      for (var item of this.iotTemplateCategoryList) {
        if (index == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    // 情报板管理右侧查询接口
    getAllVmsTemplate() {
      const param = {
        devicePixel: this.form.devicePixel,
        category: 0,
      };
      getAllVmsTemplate(param).then((res) => {
        console.log(res, "情报板管理右侧查询接口");
        this.templateList = res.data;
        // console.log(this.templateList,"this.templateList");
      });
    },
    // 删除中间模板
    delQbbDrawer(index) {
      if (index > -1) {
        this.contentList.splice(index, 1);
      }
    },
    // 发布信息
    publishInfo() {
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
        value = ("000" + i).slice(-3);
        content += Item_Content + value + "=";
        content += this.contentList[i].STAY + ",";
        content += this.contentList[i].ACTION + ",";
        content += this.contentList[i].SPEED + "," + "\\";
        content += "C" + this.contentList[i].COORDINATE + "\\";
        content += "f" + this.getFontValue(this.contentList[i].FONT);
        content +=
          this.contentList[i].FONT_SIZE.substring(0, 2) +
          this.contentList[i].FONT_SIZE.substring(0, 2) +
          "\\";
        content += "c" + this.getColorValue(this.contentList[i].COLOR);
        content += this.contentList[i].CONTENT.replace(/\n|\r\n/g, "<r><n>");

        if (i + 1 != this.contentList.length) {
          content += "<r><n>";
        }
        console.log(content, "content");
      }
      let protocolType = "GUANGDIAN_V33";
      let deviceld = this.checkedCities.toString();
      uploadBoardEditInfo(deviceld, protocolType, content).then((response) => {
        console.log(response, "返回结果");
        setTimeout(() => {
          getBoardContent(this.checkboxList[0]).then((res) => {
            console.log(res, "情报板内容查询");
          });
        }, 1000);
      });
    },
    getContentInfo(content) {
      // var content = this.boardEidtContentArea;
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
      if (
        content.indexOf("\\") != -1 &&
        content.indexOf(",") != -1 &&
        content.indexOf(",") != content.lastIndexOf(",")
      ) {
        return content.substring(content.indexOf("\\") + 1, content.length);
      }
      return content;
    },
    // 接收子组件新增待发模板
    addInfo(form) {
      console.log(form, "待发新增");
      this.contentList.push(form);
      console.log(this.contentList, "this.contentListthis.contentList");
      this.$forceUpdate();
    },
    // // 接收子组件新增信息模板
    // addInfoMode(form) {
    //   console.log(form, "模板新增");
    //   let params = {};
    //   let tcontents = {
    //     content: form.CONTENT,
    //     coordinate: form.COORDINATE,
    //     fontColor: this.getColorStyle(form.COLOR),
    //     fontSize: form.FONT_SIZE,
    //     fontSpacing: form.SPEED,
    //     fontType: this.getFontStyle(form.FONT),
    //   };
    //   console.log(tcontents, "tcontents");
    //   params.tcontents = [];
    //   params.tcontents.push(tcontents);
    //   params.stopTime = form.STAY;
    //   params.inScreenMode = form.ACTION;
    //   params.screenSize = form.screenSize;
    //   params.category = form.category;
    //   this.activeNames = form.category;

    //   getAllVmsTemplate(Number(form.category)).then((res) => {
    //     console.log(res, "情报板管理右侧查询接口888");
    //     this.templateList = res.data;
    //     this.templateList.push(params);
    //     console.log(this.templateList, "this.templateList");
    //   });
    // },
    // 接收子组件form表单 修改
    receiveForm(form) {
      // if (this.editType == 2) {
      //   console.log(this.index, "this.index_");
      //   console.log(form, "2222222222222");
      //   this.templateList[this.index].tcontents[0] = {
      //     fontColor: form.COLOR,
      //     content: form.CONTENT,
      //     coordinate: form.COORDINATE,
      //     fontType: form.FONT,
      //     fontSize: form.FONT_SIZE,
      //     fontSpacing: form.SPEED,
      //     stopTime: form.STAY,
      //     inScreenMode: form.ACTION,
      //   };
      //   // this.templateList[this.index_] = form;
      //   console.log(
      //     this.templateList[this.index].tcontents[0],
      //     "this.templateList"
      //   );
      //   console.log(this.templateList, "this.templateList");
      //   this.$forceUpdate();
      // } else {

      console.log(form, "1111111111111");
      this.contentList[this.index_] = form;
      this.$forceUpdate();
      console.log(this.contentList, "99999999999");

      // }
    },
    /** 查询部门列表 */
    getDeptList() {
      var that = this;
      var id = this.userDeptId;
      const params = {
        status: 0,
      };
      listDept(params).then((response) => {
        var list = that.handleTree(response.data, "deptId");
        this.deptList = list[0].children;
      });
    },
    //通过分中心查机构
    changeCompany(val) {
      for (let item of this.deptList) {
        if (val == item.deptId) {
          console.log(item.children);
          this.mechanismList = item.children;
          this.form.mechanism = null;
          this.form.tunnel = null;
        }
      }
    },
    // 通过所属机构查隧道
    changeMechanism(val) {
      listTunnels(val).then((response) => {
        this.tunnelData = response.rows;
        this.form.tunnel = null;
        this.getPosition();
      });
    },
    // 位置信息
    getPosition() {
      this.getDicts("iot_devices_type").then((response) => {
        console.log(response, "位置信息");
        this.positionList = response.data;
        this.getdevicessize();
      });
    },
    // 查分辨率
    getdevicessize() {
      devicessize().then((res) => {
        console.log(res, "查分辨率");
        this.devicessizeList = res.data;
      });
    },
    // 查设备多选框
    changeDevicessize() {
      this.checkbox = true;
      const param = {
        tunnelId: this.form.tunnel,
        devicePixel: this.form.devicePixel, //设备分辨率
        localInfo: this.form.localInfo, //洞口位置
      };
      information(param).then((res) => {
        console.log(res, "查设备多选框");
        this.checkboxList = res.rows;
      });
    },
    // 打开添加信息弹窗
    openDialogVisible(type) {
      // this.devicePixel = this.form.devicePixel
      if (type == 1) {
        this.$refs.addinfo.init(this.form.devicePixel, type);
      } else {
        this.$refs.addinfo.init(this.devicePixelMode, type);
      }
      console.log(this.form.devicePixel, "this.devicePixelthis.devicePixel");
    },
    handleChange(val) {
      console.log(val);
      if (val) {
        const param = {
          devicePixel: this.form.devicePixel,
          category: val,
        };
        // 情报板管理右侧查询接口
        getAllVmsTemplate(param).then((res) => {
          console.log(res.data, "情报板管理右侧查询接口");
          this.templateList = res.data;
          // console.log(this.templateList,"this.templateList");
        });
      }
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? this.checkboxList : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      console.log(value);
      this.checkboxValue = value;
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.checkboxList.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.checkboxList.length;
    },
    // combineItemContent(
    //   protocolType,
    //   stay,
    //   action,
    //   speed,
    //   coordinate,
    //   font,
    //   font_size,
    //   color,
    //   content
    // ) {
    //   if (
    //     protocolType.startsWith("SANSI") ||
    //     protocolType.startsWith("GUANGDIAN")
    //   ) {
    //     if (
    //       this.itemStr.indexOf(stay) == -1 ||
    //       this.itemStr.indexOf(action) == -1 ||
    //       this.itemStr.indexOf(speed) == -1
    //     ) {
    //       if (protocolType.startsWith("GUANGDIAN"))
    //         this.itemStr = stay + "," + action + "," + speed + ",";
    //       else this.itemStr = stay + "," + action + "," + speed;
    //     }
    //     if (
    //       this.itemStr.indexOf(coordinate) == -1 &&
    //       typeof coordinate != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[0][0] + coordinate;
    //     if (
    //       this.itemStr.indexOf(font + font_size) == -1 &&
    //       typeof font != "undefined" &&
    //       typeof font_size != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[0][1] + font + font_size;
    //     if (this.itemStr.indexOf(color) == -1 && typeof color != "undefined")
    //       this.itemStr = this.itemStr + "\\" + this.transSign[0][2] + color;
    //     this.itemStr = this.itemStr + this.transSign[0][3] + content;
    //   } else if (protocolType.startsWith("XIANKE")) {
    //     if (
    //       this.itemStr.indexOf(stay) == -1 ||
    //       this.itemStr.indexOf(action) == -1 ||
    //       this.itemStr.indexOf(speed) == -1
    //     )
    //       this.itemStr = stay + "," + action + "," + "0," + "1," + speed + ",";
    //     if (
    //       this.itemStr.indexOf(coordinate) == -1 &&
    //       typeof coordinate != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[1][0] + coordinate;
    //     if (
    //       this.itemStr.indexOf(font + font_size) == -1 &&
    //       typeof font != "undefined" &&
    //       typeof font_size != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[1][1] + font + font_size;
    //     if (this.itemStr.indexOf(color) == -1 && typeof color != "undefined")
    //       this.itemStr = this.itemStr + "\\" + this.transSign[1][2] + color;
    //     itemStr = itemStr + "\\" + this.transSign[1][3] + content;
    //   } else if (
    //     protocolType.startsWith("DIANMING") ||
    //     protocolType.startsWith("TONGZHOU")
    //   ) {
    //     if (
    //       this.itemStr.indexOf(stay) == -1 ||
    //       this.itemStr.indexOf(action) == -1 ||
    //       this.itemStr.indexOf(speed) == -1
    //     )
    //       this.itemStr = stay + "," + action + "," + "0," + "0," + speed + ",";
    //     if (
    //       this.itemStr.indexOf(coordinate) == -1 &&
    //       typeof coordinate != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[2][0] + coordinate;
    //     if (
    //       this.itemStr.indexOf(font + font_size) == -1 &&
    //       typeof font != "undefined" &&
    //       typeof font_size != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[2][1] + font + font_size;
    //     if (this.itemStr.indexOf(color) == -1 && typeof color != "undefined")
    //       this.itemStr = this.itemStr + "\\" + this.transSign[2][2] + color;
    //     this.itemStr = this.itemStr + "\\" + this.transSign[2][3] + content;
    //   } else if (protocolType.startsWith("DINGEN")) {
    //     if (
    //       this.itemStr.indexOf(stay) == -1 ||
    //       this.itemStr.indexOf(action) == -1 ||
    //       this.itemStr.indexOf(speed) == -1
    //     )
    //       this.itemStr = stay + "," + action + "," + "0," + "1," + speed;
    //     if (
    //       this.itemStr.indexOf(coordinate) == -1 &&
    //       typeof coordinate != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[3][0] + coordinate;
    //     if (
    //       this.itemStr.indexOf(font + font_size) == -1 &&
    //       typeof font != "undefined" &&
    //       typeof font_size != "undefined"
    //     )
    //       this.itemStr =
    //         this.itemStr + "\\" + this.transSign[3][1] + font + font_size;
    //     if (this.itemStr.indexOf(color) == -1 && typeof color != "undefined")
    //       this.itemStr = this.itemStr + "\\" + this.transSign[3][2] + color;
    //     this.itemStr = this.itemStr + "\\" + this.transSign[3][3] + content;
    //   }
    //   return this.itemStr;
    // },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },
    // infosRowClick(item) {
    //   var itemId = item.itemId;
    //   var disContent = item.disContent;
    //   this.isactive = itemId;
    //   this.boardEidtContentArea = disContent;
    //   this.itemEditCoordinate = this.itemPropertyMap.get(
    //     itemId + "_COORDINATE"
    //   );
    //   this.itemEditFont = this.getFontValue(
    //     this.itemPropertyMap.get(itemId + "_FONT")[0]
    //   );
    //   this.itemEditFontSize = this.itemPropertyMap.get(
    //     itemId + "_FONT_SIZE"
    //   )[0];
    //   this.itemEditColor = this.getColorValue(
    //     this.itemPropertyMap.get(itemId + "_COLOR")[0]
    //   );
    //   var itemEditContents = this.itemPropertyMap.get(itemId + "_CONTENT");
    //   this.boardContentS = itemEditContents;
    //   this.itemEditContent = [];
    //   for (var i = 0; i < itemEditContents.length; i++) {
    //     this.itemEditContent.push(itemEditContents[i]);
    //   }
    //   var content = "";
    //   var contentArr = this.itemPropertyMap.get(itemId + "_CONTENT");
    //   for (var i = 0; i < contentArr.length; i++) {
    //     content = content + contentArr[i];
    //   }
    //   var font = this.itemPropertyMap.get(itemId + "_FONT")[0];
    //   var fontSize = this.itemPropertyMap.get(itemId + "_FONT_SIZE")[0];
    //   var color = this.itemPropertyMap.get(itemId + "_COLOR")[0];
    //   this.setContentEditInfos(
    //     this.itemPropertyMap.get(itemId + "_STAY")[0],
    //     this.itemPropertyMap.get(itemId + "_ACTION")[0],
    //     this.itemPropertyMap.get(itemId + "_SPEED")[0],
    //     this.itemPropertyMap.get(itemId + "_COORDINATE")[0],
    //     this.getFontValue(font),
    //     fontSize,
    //     this.getColorValue(color),
    //     content
    //   );
    //   this.setBoardPreviewContents(
    //     content,
    //     font,
    //     fontSize,
    //     this.colorValueToDisplay(this.getColorValue(color)),
    //     "",
    //     this.startCoordTxt_x,
    //     this.startCoordTxt_y
    //   );
    // },
    // setContentEditInfos(stay, action, speed, coordinate, font, font_size, color, content) {
    //     this.contentPropertyEditInit(); // 编辑区域属性初始化
    //     this.selectFontFamily = font; //字体
    //     this.selectFontSize = font_size.length == 4 ? font_size : font_size + font_size; //字体大小
    //     this.selectFontColor = color; //字体颜色
    //     if (coordinate == undefined) {
    //       this.startCoordTxt_x = "000";
    //       this.startCoordTxt_y = "000";
    //     } else {
    //       if (coordinate.length == 1) {
    //         this.startCoordTxt_x = coordinate[0].substring(0, 3);
    //         this.startCoordTxt_y = coordinate[0].substring(3, 6);
    //       } else if (coordinate.length == 6) {
    //         this.startCoordTxt_x = coordinate.substring(0, 3);
    //         this.startCoordTxt_y = coordinate.substring(3, 6);
    //       }
    //     }
    //     this.selectResTime = stay; //停留时间
    //     this.selectOutScreenMode = action; //出屏方式
    //     this.selectInOutScreenSpeed = speed; //出入屏速度
    //     this.showContentArea = content;
    //   },
    // 清空情报板下方内容
    // contentPropertyEditInit() {
    //     this.selectResTime = "";
    //     this.selectInScreenMode = "",
    //       this.selectInOutScreenSpeed = "";
    //     this.selectOutScreenMode = "";
    //     this.selectLightNumList = [];
    //     this.selectFontFamily = "";
    //     this.selectFontSize = "";
    //     this.selectFontColor = "";
    //     this.startCoordTxt_x = "000";
    //     this.startCoordTxt_y = "000";
    //     this.showContentArea = "";
    //     for (var i = 1; i <= 31; i++) {
    //       if (i < 10) {
    //         var json = {};
    //         json.value = "0" + i;
    //         json.name = i;
    //         this.selectLightNumList.push(json);
    //       } else {
    //         var json = {};
    //         json.value = i;
    //         json.name = i;
    //         this.selectLightNumList.push(json);
    //       }
    //     }
    //   },
    getFontValue(font) {
      if (font == "黑体") return "h";
      if (font == "楷体") return "k";
      if (font == "仿宋") return "f";
      if (font == "隶书") return "l";
      return "s";
    },
    getColorValue(color) {
      if (color == "蓝色") return "000000255000";
      if (color == "绿色") return "000255000000";
      if (color == "透明色") return "t";
      if (color == "红色") return "255000000000";
      return "255255000000"; //黄色
    },
    //将item属性存入Map
    addItemPropertyMap(
      itemId,
      addFlg,
      stay,
      action,
      speed,
      coordinate,
      font,
      font_size,
      color,
      content
    ) {
      if (!action) {
        action = 0;
      }
      this.addPropertyMap(itemId + "_STAY", this.itemPropertyMap, addFlg, stay);
      this.addPropertyMap(
        itemId + "_ACTION",
        this.itemPropertyMap,
        addFlg,
        action
      );
      this.addPropertyMap(
        itemId + "_SPEED",
        this.itemPropertyMap,
        addFlg,
        speed
      );
      this.addPropertyMap(
        itemId + "_COORDINATE",
        this.itemPropertyMap,
        addFlg,
        coordinate
      );
      this.addPropertyMap(itemId + "_FONT", this.itemPropertyMap, addFlg, font);
      this.addPropertyMap(
        itemId + "_FONT_SIZE",
        this.itemPropertyMap,
        addFlg,
        font_size
      );
      this.addPropertyMap(
        itemId + "_COLOR",
        this.itemPropertyMap,
        addFlg,
        color
      );
      this.addPropertyMap(
        itemId + "_CONTENT",
        this.itemPropertyMap,
        addFlg,
        content
      );
    },
    addPropertyMap(itemIdStr, map, addFlg, propertyStr) {
      var array = map.get(itemIdStr);
      if (array == null || addFlg) array = new Array();
      array.push(propertyStr);
      map.put(itemIdStr, array);
    },
    // 向内容显示区域添加一条信息
    addContentDisplayInfos(addId, no, disContent) {
      var jsonArr = {};
      jsonArr.itemId = addId;
      jsonArr.disContent = disContent;
      this.disContentList.push(jsonArr);
    },
    // handleClose(done) {
    //   this.$confirm("确认关闭？")
    //     .then((_) => {
    //       done();
    //     })
    //     .catch((_) => {});
    // },
    onSubmit() {
      // var that = this;
      if (
        this.checkboxValue[0] == "undefined" ||
        this.checkboxValue[0] == null
      ) {
        return;
      }
      getBoardInfo(this.checkboxValue[0]).then((res) => {
        console.log(res, "getBoardInfo");
        this.deviceId = res.data.deviceId;
        if (res.data.deviceId) {
          this.disabledButton = false;
        } else {
          this.disabledButton = true;
        }
      });
      this.checkboxValue[0];
      // 获取情报板修改页面信息
      getBoardEditInfo(this.checkboxValue[0]).then((response) => {
        console.log(response, "response");
        if (response.code != 200) {
          this.$message(response.msg);
          return;
        }
        if (response.data[0] == undefined) {
          this.$message(response.msg);
          return;
        }
        // var response = {};
        // response = boardData;
        var parseObject = JSON.parse(response.data[0]);
        var protocolType = parseObject.support.PROTOCOL_TYPE;
        var contents = parseObject.content;
        // console.log(parseObject,"parseObject")
        // console.log(protocolType,"protocolType")
        // console.log(contents,"contents")

        // this.checkboxList = contents
        if (
          typeof contents == "undefined" ||
          typeof protocolType == "undefined"
        ) {
          this.$message(response.msg);
          return;
        }
        this.supplier = protocolType;
        var currRowId = "";
        var reg = /,/g;
        console.log(contents, "contents");
        for (var i = 0; i < contents.length; i++) {
          var content = contents[i];
          var itemId = "ITEM" + this.formatNum(i, 3);
          if (i == 0) {
            currRowId = itemId;
          }
          var con = content[itemId];

          this.itemStr = "";
          for (let item of con) {
            item.COLOR = this.getColorStyle(item.COLOR);
            item.FONT_SIZE = Number(item.FONT_SIZE.substring(0, 2)) + "px";
            // item.font = this.getFontStyle(item.FONT)
          }
          console.log(con, "con");
          this.contentList = con;
          this.getAllVmsTemplate();
          // for (var j = 0; j < con.length; j++) {
          //   this.itemStr = this.combineItemContent(
          //     protocolType,
          //     con[j].STAY,
          //     con[j].ACTION,
          //     con[j].SPEED,
          //     con[j].COORDINATE,
          //     this.getFontValue(con[j].FONT),
          //     con[j].FONT_SIZE,
          //     this.getColorValue(con[j].COLOR),
          //     con[j].CONTENT.replace(reg, "，").replace(/<br>/g, "\\n")
          //   );
          //   console.log(this.itemStr,"this.itemStr");
          //   this.addItemPropertyMap(
          //     itemId,
          //     false,
          //     con[j].STAY,
          //     con[j].ACTION,
          //     con[j].SPEED,
          //     con[j].COORDINATE,
          //     con[j].FONT,
          //     con[j].FONT_SIZE,
          //     con[j].COLOR,
          //     con[j].CONTENT.replace(reg, "，").replace(/<br>/g, "\n")
          //   );

          // }
          // this.addContentDisplayInfos(
          //   "ITEM" + this.formatNum(i, 3),
          //   i + 1,
          //   this.itemStr
          // );
        }
        // this.isactive = currRowId;
        // this.infosRowClick(this.disContentList[0]);
        // })
        // .catch(function (error) {
        //   that.disContentList = [];
        //   that.loadingDialog = false;
        //   // this.$message(error);
      });
    },
    openQbbDrawer(item, index, type) {
      this.index_ = index;
      console.log(item);
      this.boardEmitItem = item;
      // console.log(this.form.devicePixel,"this.form.devicePixel");
      this.boardEmitItem.screenSize = this.form.devicePixel;
      this.boardEmitItem.deviceId = this.deviceId;
      this.boardEmitItem.type = type;

      this.showEmit = true;
      // this.$refs.editInfo.init();
      // this.$emit("boardEmitItem",item)
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
      } else {
        return font;
      }
    },
    getFontSize(font, screenSize) {
      if (!font) {
        return;
      }
      var screen = "";
      if (!screenSize) {
        screen = this.form.devicePixel.split("*")[0];
      } else {
        screen = screenSize.split("*")[0];
      }
      if (screen <= 630) {
        var i = 630 / screen;

        if (font.toString().length == 2) {
          return font * i + "px";
        } else {
          return font.substring(0, 2) * i + "px";
        }
      } else {
        var i = screen / 630;
        if (font.toString().length == 2) {
          return font / i + "px";
        } else {
          return font.substring(0, 2) / i + "px";
        }
      }
    },
    getCoordinate1(coordinate, screenSize) {
      var screen = "";
      if (!screenSize) {
        screen = this.form.devicePixel.split("*")[0];
      } else {
        screen = screenSize.split("*")[0];
      }
      if (screen <= 630) {
        var i = 630 / screen;
        return coordinate * i + "px";
      } else {
        var i = screen / 630;
        return coordinate / i + "px";
      }
    },
    getCoordinate2(coordinate, screenSize) {
      var screen = "";
      if (!screenSize) {
        screen = this.form.devicePixel.split("*")[1];
      } else {
        screen = screenSize.split("*")[1];
      }
      if (screen <= 75) {
        var i = 75 / screen;
        return coordinate * i + "px";
      } else {
        var i = screen / 75;
        return coordinate / i + "px";
      }
    },
    getFontStyle(font) {
      if (font == "宋体") {
        return "Simsun";
      } else if (font == "黑体") {
        return "SimHei";
      } else if (font == "楷体") {
        return "KaiTi";
      } else {
        return font;
      }
    },
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
    dialogClose() {
      this.showEmit = false;
      setTimeout(() => {
        const param = {
          devicePixel: this.form.devicePixel,
          category: 0,
        };
        getAllVmsTemplate(param).then((res) => {
          console.log(res, "情报板管理右侧查询接口");
          this.templateList = res.data;
          // console.log(this.templateList,"this.templateList");
        });
      }, 2000);
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  .el-col {
    height: 100%;
    .bigTitle {
      padding: 15px 0;
      border-bottom: 1px solid #f3f3f3;
      margin-bottom: 10px;
    }
    .contentBox {
      width: 100%;
      height: 730px;
      overflow: auto;
      .listBox {
        height: 75px;
        display: flex;
        // grid-template-columns: 3% auto 10%;
        // align-content: center;
        // column-gap: 20px; //左右
        // row-gap: 40px; //上下
        margin-bottom: 25px;

        .indexBox {
          display: flex;
          flex-flow: column;
          align-items: center;
          justify-content: space-evenly;
          cursor: pointer;
          i {
            font-size: 20px;
          }
          .disabledClass {
            pointer-events: none;
            cursor: auto !important;
            color: #ccc;
          }
        }

        .con {
          border: 1px solid #f3f3f3;
          // height: 75px;
          // line-height: 75px;
          // text-align: center;
          background: black;
          position: relative;
          width: 630px;
          margin-left: 10px;
          overflow: hidden;
          // position: absolute;
        }
        .menuBox {
          display: flex;
          align-items: center;
          margin-left: 10px;
          i {
            font-size: 24px;
            color: #666;
            cursor: pointer;
          }
        }
      }
      .controlBox {
        display: flex;
        justify-content: center;
        margin-bottom: 10px;
      }
    }
    .templateBox {
      width: 100%;
      .con {
        height: 75px;
        // display: grid;
        // grid-template-columns: auto 10%;
        // align-content: center;
        // column-gap: 20px; //左右
        // row-gap: 40px; //上下
        margin-bottom: 25px;
        padding: 0 20px;
        overflow: hidden;

        .templateTitle {
          height: 75px;
          // line-height: 75px;
          // text-align: center;
          border: 1px solid #f3f3f3;
          background: black;
          position: relative;
          width: 630px;
          float: left;
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
      .controlBox {
        margin-top: 10px;
        margin-bottom: 10px;
        display: flex;
        justify-content: center;
      }
      .el-collapse {
        max-height: 680px !important;
        overflow: auto;
      }
    }
  }
  .checkbox {
    label {
      width: 100%;
      padding: 10px 0;
      box-sizing: border-box;
    }
  }
}
::v-deep .el-collapse-item__content {
  line-height: normal;
}
</style>
