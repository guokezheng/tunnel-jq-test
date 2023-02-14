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
        <el-form ref="form" :model="form">
          <el-form-item prop="deptId">
            <treeselect
              v-model="form.deptId"
              :options="siteList"
              :props="siteProps"
              :show-count="true"
              placeholder="请选择归属部门"
              @select="changeSite"
              style="width: 100%"
              size="small"
            />
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="12" style="padding-right: 5px">
                <el-select
                  v-model="form.tunnel"
                  placeholder="请选择所属隧道"
                  clearable
                  size="small"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in tunnelData"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                    @click.native="changeTunnel(item.tunnelId)"
                  />
                </el-select>
              </el-col>
              <el-col :span="12" style="padding-left: 5px">
                <el-select
                  v-model="form.eqDirection"
                  size="small"
                  placeholder="请选择方向"
                >
                  <el-option
                    v-for="item in boardDirectionList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                    @click.native="changeDirection(item.dictValue)"
                  />
                </el-select>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-collapse
              v-model="iotBoardActive"
              accordion
              @change="handleChange"
            >
              <el-collapse-item
                v-for="(item, index) in iotBoardList"
                :key="index"
                :title="item.label"
                :name="item.devicePixel"
              >
                <el-checkbox
                  style="width: 100%"
                  :indeterminate="isIndeterminate"
                  v-model="checkAll"
                  @change="handleCheckAllChange"
                  >全选
                </el-checkbox>
                <div style="margin: 5px 0"></div>
                <el-checkbox-group
                  class="checkbox"
                  v-model="checkedCities"
                  @change="handleCheckedCitiesChange"
                >
                  <el-checkbox
                    v-for="(itm, index) in item.list"
                    :label="itm.deviceId"
                    :key="index"
                  >
                    <div>{{ itm.deviceName }}</div>
                    <el-tooltip content="回读当前信息" placement="top">
                      <el-button
                        class="el-icon-tickets huiduButton"
                        @click.stop.prevent="onSubmit(itm.deviceId)"
                        :disabled="submitButton"
                      ></el-button>
                    </el-tooltip>
                    
                  </el-checkbox>
                </el-checkbox-group>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="10" style="border-left: 1px solid #05afe3">
        <div class="bigTitle">
          <div>
            <span>待下发信息</span>
            <span v-if="form.devicePixel">【 {{ form.devicePixel }}】</span>
          </div>
          
          <div class="controlBox">
            <el-button
              @click.native="openDialogVisible(1, 2)"
              :disabled="disabledButton"
              >添加信息</el-button
            >
            <el-button type="primary" @click="publishInfo" :disabled="disabledCheckbox">发布信息</el-button>
          </div>
        </div>
        <div class="contentBox">
          
          <el-table :data="contentList" row-key="ID" v-loading="loading" max-height="700">
            <el-table-column align="center"  width="650">
              <template slot-scope="scope">
                <div class="con">
                  <div
                    style="background: black; position: relative"
                    :style="{
                      color: getColorStyle(scope.row.COLOR),
                      fontSize: getFontSize(scope.row.FONT_SIZE),
                      fontFamily: scope.row.FONT,
                      width: getScreenSize(form.devicePixel, 'width') + 'px',
                      height: getScreenSize(form.devicePixel, 'height') + 'px',
                    }"
                  >
                    <span
                      :style="{
                        left: getCoordinate(
                          scope.row.COORDINATE.substring(0, 3),
                          'left'
                        ),
                        top: getCoordinate(scope.row.COORDINATE.substring(3, 6), 'top'),
                      }"
                      class="boardTextStyle"
                      v-html="
                        scope.row.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                          / /g,
                          ' &nbsp'
                        )
                      "
                    ></span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column  align="center" style="width:20%;border:solid 1px red">
              <template slot-scope="scope">
                <div class="menuBox">
                  <el-tooltip content="加入信息模板" placement="top">
                    <i class="el-icon-d-arrow-right" @click="arrowRight(scope.row)"></i>
                  </el-tooltip>
                  
                  <el-tooltip content="编辑" placement="top">
                    <i
                      class="el-icon-edit-outline"
                      @click="openQbbDrawer(scope.row, scope.$index, 1)"
                    ></i>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <i class="el-icon-close" @click="delQbbDrawer(scope.$index)"></i>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="10" style="border-left: 1px solid #05afe3">
        <div class="bigTitle">
          <div>
            <span>信息模板</span>
            <span v-if="form.devicePixel">【 {{ form.devicePixel }}】</span>
          </div>
          <div class="controlBox">
            <el-button type="primary" @click="openDialogVisible(2, 2)"
              >添加模板</el-button
            >
          </div>
        </div>

        <div class="templateBox">
          
          <el-collapse v-model="activeNames"
          >
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
                  fontSize: getFontSize(
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
                      width: getScreenSize(itm.screenSize, 'width') + 'px',
                      height: getScreenSize(itm.screenSize, 'height') + 'px',
                    }"
                    style="background: black; position: relative"
                  >
                    <span
                      :style="{
                        left: getCoordinate(
                          itm.tcontents[0].coordinate.substring(0, 3),
                          'left',
                          itm.screenSize
                        ),
                        top: getCoordinate(
                          itm.tcontents[0].coordinate.substring(3, 6),
                          'top',
                          itm.screenSize
                        ),
                      }"
                      class="boardTextStyle"
                      v-html="
                        itm.tcontents[0].content
                          .replace(/\n|\r\n/g, '<br>')
                          .replace(/ /g, ' &nbsp')
                      "
                    ></span>
                  </div>
                </div>
                <div class="menuBox">
                  <el-tooltip content="加入待下发信息" placement="top">
                    <i
                      class="el-icon-d-arrow-left"
                      @click="arrowLeft(itm)"
                      :class="
                        disabledButton && !form.devicePixel
                          ? 'disabledClass'
                          : ''
                      "
                      style="cursor: pointer"
                    ></i>
                  </el-tooltip>
                  <el-tooltip content="编辑" placement="top">
                    <i
                      class="el-icon-edit-outline"
                      @click="editOutline(itm, indx, 2)"
                    ></i>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <i
                      class="el-icon-delete"
                      @click="handleDelete(itm)"
                    ></i>
                  </el-tooltip>
                  
                  
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
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import Sortable from 'sortablejs';
import addinfo from "./addinfo";
import editInfo from "./editInfo";
import boardData from "./boardData";
import { getUserDeptId } from "@/api/system/user";
import { listDept, getTreeByDeptId, treeselectExcYG1 } from "@/api/system/dept";
import {
  listTunnels,
  devicessize,
  information,
  getBoardInfo,
  getBoardEditInfo,
  getIotBoardList,
} from "@/api/information/api.js";
import {
  uploadBoardEditInfo,
  getAllVmsTemplate,
  addTemplate,
  addTemplateContent,
  getBoardContent,
  deleteTemplate
} from "@/api/board/template";
const cityOptions = ["上海", "北京", "广州", "深圳"];
export default {
  name: "Device",
  components: {
    addinfo,
    editInfo,
    Treeselect
  },
  dicts: ["iot_board_direction"],

  data() {
    return {
      disabledCheckbox:true,
      loading:false,
      submitButton:false,
      iotBoardActive: '',
      iotBoardList: [],
      boardDirectionList: [],
      siteList: [],
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      siteProps: {
        value: "id",
        label: "label",
        children: "children",
      },
      devicePixelMode: null,
      editType: 0,
      disabledButton: true,
      showEmit: false,
      index_: 0,
      index: 0,
      deviceId: "",
      boardEmitItem: null,
      userDeptId: "",
      deptList: [], //分中心下拉框
      mechanismList: [], //管理机构下拉框
      tunnelData: [], //所属隧道下拉框
      positionList: [], //位置信息下拉框
      devicessizeList: [], //分辨率下拉框
      // checkboxList: [], //多选框数组
      deviceList: [],
      checkbox: false,
      // checkboxValue: [],
      checkedCities: [],
      form: {
        company: null,
        mechanism: null,
        tunnel: null,
        localInfo: null,
        devicePixel: null,
        deptId: this.userDeptId,
      },
      boardStyle: {},
      itemStr: "",
      supplier: null,
      dialogVisible: false,
      activeNames: [],
      iotTemplateCategoryList: [],
      checkAll: false,
      isIndeterminate: false,
      contentList: [],
      templateList: [],
    };
  },
  created() {
    this.getUserDept();
    this.getInfoMode()
    
  },
  mounted() {
    this.rowDrop()
  },
 
  methods: {
    getInfoMode(){
      this.getDicts("iot_template_category").then((res) => {
        this.iotTemplateCategoryList = res.data;
        console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
      });
    },
    // 行拖拽
    rowDrop() {
      // 要侦听拖拽响应的DOM对象
      const tbody = document.querySelector('.el-table__body-wrapper tbody');
      console.log(tbody,"tbodytbodytbody")
      const _this = this;
      Sortable.create(tbody, {
        // 结束拖拽后的回调函数
        onEnd({newIndex, oldIndex}) {
          const currentRow = _this.contentList.splice(oldIndex, 1)[0];
          _this.contentList.splice(newIndex, 0, currentRow);
        }
      })
    },
    getUserDept() {
      let that = this;
      getUserDeptId(this.userQueryParams).then((response) => {
        console.log(response, "管理站级联222222");
        that.userDeptId = response.rows[0].deptId;
        that.getDeptList();
      });
    },
    getDeptList() {
      var userDeptId = this.userDeptId;
      const params = { status: 0 };
      getTreeByDeptId(params).then((response) => {
        console.log(response,"级联")
        const options = response.data;
        let childs = [];
        function a(list) {
          list.forEach((item) => {
            if (item.id == userDeptId) {
              childs = item.children || [];
            } else {
              item.children && a(item.children);
            }
          });
        }
        a(options);
        console.log(childs,"childschilds")
        if (childs.length == 0) {
          this.siteList = options[0].children;
        } else {
            this.siteList = childs;
        }
        this.checkData(this.siteList[0]);
      });
    },
    checkData(obj) {
      console.log(obj,"obj")
      if (obj.children && obj.children.length > 0) {
        this.checkData(obj.children[0]);
      } else {
        this.changeSite(obj);
        this.form.deptId = obj.id
      }
    },
    // 改变站点
    changeSite(index) {
      this.changeMechanism(index.id)
      this.form.tunnel = ''
      this.positionList = [];
      this.boardDirectionList = [];
    },
    // 通过所属机构查隧道
    changeMechanism(val) {
      listTunnels(val).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.form.tunnel = response.rows[0].tunnelId;
        this.tunnelData = response.rows;
        // this.getIotBoard();
        this.changeDirection();
      });
    },
    changeDirection(val) {
      // console.log(val,"val")
      this.getDicts("iot_board_direction").then((res) => {
        this.boardDirectionList = res.data;
        if (val) {
          this.form.eqDirection = val;
        } else {
          this.form.eqDirection = res.data[0].dictValue;
        }
        console.log(this.boardDirectionList, "板子方向");
        this.getIotBoard();
      });
    },
    // 改变隧道
    changeTunnel(value) {
      this.checkedCities = [];
      // this.checkboxList = [];
      this.contentList = [];
      this.form.tunnel = value;
      this.getIotBoard();
    },
    getIotBoard() {
      this.checkAll = false;
      let param = {
        eqDirection: this.form.eqDirection,
        tunnelId: this.form.tunnel,
        manageAgencyId: this.form.deptId,
      };
      getIotBoardList(param).then((res) => {
        console.log(res, "查询情报板设备列表");
        console.log(this.checkAll,"checkAllcheckAllcheckAllcheckAll")
        this.iotBoardList = res.data;

        if(res.data.length>0){
          this.iotBoardActive = res.data[0].devicePixel
          this.handleChange(res.data[0].devicePixel)
        }else{
          this.getInfoMode()
        }

        this.$forceUpdate();
      });
    },

    getScreenSize(num, type) {
      // console.log(num)
      let width = num.split("*")[0];
      let height = num.split("*")[1];
      // 实际分辨率比页面板子小
      if (width < 630 && height < 75) {
        if (type == "width") {
          return width;
        } else if (type == "height") {
          return height;
        }
      } else {
        // 实际分辨率比页面板子大
        if (width / 630 > height / 75) {
          if (type == "width") {
            return 630;
          } else if (type == "height") {
            return height / (width / 630);
          }
        } else {
          if (type == "width") {
            return width / (height / 75);
          } else if (type == "height") {
            return 75;
          }
        }
      }
    },

    getActiveNames() {
      // console.log(active);
      // this.activeNames = active;
      this.allVmsTemplate();
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
        ID:this.contentList.length,
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
        // this.getActiveNames(item.category);
        this.allVmsTemplate()
      });
    },
    // 修改弹窗
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

      console.log(this.showEmit, "this.showEmit");
      this.showEmit = true;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let content = '是否确认删除选中数据项?'
      if(ids == null || ids == undefined || ids == [] || ids == '') {
        content = '是否确认删除当前情报板模板?'
      }
      this.$confirm(
        content,
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return deleteTemplate(ids);
        })
        .then(() => {
          this.allVmsTemplate();
          this.$modal.msgSuccess("删除成功");
        });
    },
    // 情报板管理右侧查询接口
    allVmsTemplate() {
      // this.activeNames = []
      const param = {
        devicePixel: this.form.devicePixel,
      };
      getAllVmsTemplate(param).then((res) => {
        let data = res.data;
        console.log(res, "情报板管理右侧查询接口");
        for (let j = 0; j < this.iotTemplateCategoryList.length; j++) {
          let arr = this.iotTemplateCategoryList[j];
          let brr = data[j];
          arr.list = brr;
          this.activeNames.push(j.toString())
        }
        this.$forceUpdate();
        console.log(this.iotTemplateCategoryList,"新模板")
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
      let deviceld = this.checkedCities.toString();
      console.log(deviceld, "deviceld");
      uploadBoardEditInfo(deviceld, protocolType, content).then((response) => {
        console.log(response, "返回结果");
        this.$modal.msgSuccess("发布成功");
        // setTimeout(() => {
        //   getBoardContent(deviceld).then((res) => {
        //     console.log(res, "情报板内容查询");
        //   });
        // }, 1000);
      });
    },

    // 接收子组件新增待发模板
    addInfo(form) {
      console.log(form, "待发新增");
      form.ID = this.contentList.length
      this.contentList.push(form);
      console.log(this.contentList, "this.contentListthis.contentList");
      this.$forceUpdate();
    },

    // 接收子组件form表单 修改
    receiveForm(form) {
      console.log(form, "接收子组件form表单 修改");
      this.contentList.splice(this.index_,1,form);
      this.$forceUpdate();

      console.log(this.contentList, "99999999999");
      this.allVmsTemplate();
    },

    // 打开添加信息弹窗
    openDialogVisible(type, mode) {
      // this.devicePixel = this.form.devicePixel
      // if (type == 1) {
        this.$refs.addinfo.init(this.form.devicePixel, type, mode);
      // } else {
        // this.$refs.addinfo.init(this.devicePixelMode, type, mode);
      // }
      console.log(this.form.devicePixel, "this.devicePixelthis.devicePixel");
    },

    handleCheckAllChange(val) {
      if(val){
        this.disabledCheckbox = false
      }else{
        this.disabledCheckbox = true
      }
      this.checkedCities = val ? this.deviceList : [];
    },
    handleCheckedCitiesChange(value) {
      
      this.checkedCities = value;
      let val = JSON.parse(JSON.stringify(value));
      if(val.length>0){
        this.disabledCheckbox = false
      }else{
        this.disabledCheckbox = true
      }
      this.$forceUpdate()
      for (let itm of this.deviceList) {
        if (val.indexOf(itm) > -1) {
          this.checkAll = true;
        } else {
          this.checkAll = false;
        }
      }
    },
    handleChange(val) {
      console.log(val,"情报板列表手风琴")
      this.contentList = [];
      this.deviceList = [];
      // this.checkboxList = [];
      // this.checkboxValue = []
      this.disabledButton = true;
      this.disabledCheckbox = true
      this.checkedCities = []
      this.checkAll = false
      this.form.devicePixel = val;
      this.allVmsTemplate();
      for (let item of this.iotBoardList) {
        if (item.devicePixel == val) {
          // this.checkboxList = item.list;
          for (let itm of item.list) {
            this.deviceList.push(itm.deviceId);
          }
        }
      }
      console.log(this.deviceList,"this.deviceList")
      // let val2 = JSON.parse(JSON.stringify(this.checkboxValue));
      // if (val2.length>0) {
      //   for (let itm of this.deviceList) {
      //     if (val2.indexOf(itm) > -1) {
      //       this.checkAll = true;
      //     } else {
      //       this.checkAll = false
      //     }
      //   }
      // }
    },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
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
      if (color == "透明色" ) return "t";
      if (color == "红色" || color == 'red') return "255000000000";
      return "255255000000"; //黄色
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

    async onSubmit(deviceId) {
      this.submitButton = true
      this.loading = true
      this.contentList = [];
      // 获取情报板修改页面信息
      await getBoardEditInfo(deviceId).then((response) => {
        console.log(response, "response");
        if (response.code != 200) {
          this.$message.error(`设备网络连接异常，请稍后重试`);

          return;
        }
        if (response.data[0] == undefined) {
          this.$message(response.msg);
          return;
        }

        var parseObject = JSON.parse(response.data[0]);
        var protocolType = parseObject.support.PROTOCOL_TYPE;
        var contents = parseObject.content;
        console.log(parseObject, "parseObject");
        if (
          typeof contents == "undefined" ||
          typeof protocolType == "undefined"
        ) {
          this.$message(response.msg);
          this.$forceUpdate();
          this.loading = false
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
          var con = content[itemId][0];
          con.COLOR = this.getColorStyle(con.COLOR);
          con.FONT_SIZE = Number(con.FONT_SIZE.substring(0, 2)) + "px";
          con.ID = i
          // con.CONTENT = con.CONTENT

          this.contentList.push(con);
        }
        // this.allVmsTemplate();
        console.log(this.contentList, "this.contentList");
        // this.rowDrop()
        this.submitButton = false
        this.loading = false

        this.$forceUpdate();
      })
      getBoardInfo(deviceId).then((res) => {
        console.log(res, "getBoardInfo");
        this.deviceId = res.data.deviceId;
        this.form.devicePixel = res.data.pixel;
        if (res.data.deviceId) {
          this.disabledButton = false;
        } else {
          this.disabledButton = true;
        }
        
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
    },
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "GreenYellow";
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
      let width = "";
      let height = "";
      if (!screenSize) {
        width = this.form.devicePixel.split("*")[0];
        height = this.form.devicePixel.split("*")[1];
      } else {
        width = screenSize.split("*")[0];
        height = screenSize.split("*")[1];
      }
      if (width < 630 && height < 75) {
        if (font.toString().length == 2) {
          return font + "px";
        } else {
          return font.substring(0, 2) + "px";
        }
      } else {
        if (width / 630 > height / 75) {
          if (font.toString().length == 2) {
            return font / (width / 630) - 4 + "px";
          } else {
            return font.substring(0, 2) / (width / 630) - 4 + "px";
          }
        } else {
          if (font.toString().length == 2) {
            return font / (height / 75) - 4 + "px";
          } else {
            return font.substring(0, 2) / (height / 75) - 4 + "px";
          }
        }
      }
    },
    getCoordinate(coordinate, type, screenSize) {
      let width = "";
      let height = "";
      if (!screenSize) {
        width = this.form.devicePixel.split("*")[0];
        height = this.form.devicePixel.split("*")[1];
      } else {
        width = screenSize.split("*")[0];
        height = screenSize.split("*")[1];
      }
      if (width < 630 && height < 75) {
        return coordinate + "px";
      } else {
        if (width / 630 > height / 75) {
          if (type == "left") {
            return coordinate / (width / 630) + "px";
          } else if (type == "top") {
            return coordinate / (width / 630) + "px";
          }
        } else {
          // console.log(coordinate,"coordinate")
          if (type == "left") {
            // if (!screenSize) {
              return coordinate / (height / 75) + 10 + "px";
            // } else {
              // return coordinate / (height / 75) + "px";
            // }
          } else if (type == "top") {
            // if (!screenSize) {
              return coordinate / (height / 75) + 4 + "px";
            // } else {
              // return coordinate / (height / 75) + "px";
            // }
          }
        }
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
        this.allVmsTemplate();
      }, 500);
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
      border-bottom: 1px solid #05afe3;
      margin-bottom: 10px;
      display: flex;
      justify-content: space-between;
    }
    .contentBox {
      width: 100%;
      height: calc(100% - 67px);
      overflow: auto;
      // .listBox {
      //   height: 75px;
      //   display: flex;
      //   // grid-template-columns: 3% auto 10%;
      //   // align-content: center;
      //   // column-gap: 20px; //左右
      //   // row-gap: 40px; //上下
      //   margin-bottom: 25px;

        // .indexBox {
        //   display: flex;
        //   flex-flow: column;
        //   align-items: center;
        //   justify-content: space-evenly;
        //   cursor: pointer;
        //   i {
        //     font-size: 20px;
        //   }
        //   .disabledClass {
        //     pointer-events: none;
        //     cursor: auto !important;
        //     color: #ccc;
        //   }
        // }

        .con {
          border: 1px solid #05afe3;
          height: 75px;
          // line-height: 75px;
          // text-align: center;
          // background: black;
          position: relative;
          width: 630px;
          // margin-left: 10px;
          overflow: hidden;
          display: flex;
          justify-content: center;
          align-items: center;
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
            caret-color: rgba(0,0,0,0);
            user-select: none;
          }
          i:hover{
            color:#05afe3;
          }
        }
      // }
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
        margin-bottom: 10px;
        padding: 0 20px;
        overflow: hidden;
        display: flex;

        .templateTitle {
          height: 75px;
          // line-height: 75px;
          // text-align: center;
          border: 1px solid #05afe3;
          // background: black;
          position: relative;
          width: 630px;
          float: left;
          display: flex;
          justify-content: center;
          align-items: center;
          overflow: hidden;
        }
        .menuBox {
          display: flex;
          // align-items: center;
          margin: 28px 0;
          float: right;
          i {
            font-size: 24px;
            color: #666;
            padding-left: 4px;
            cursor: pointer;
            caret-color: rgba(0,0,0,0);
            user-select: none;
          }
          i:hover{
            color:#05afe3;
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
        max-height: 69vh !important;
        overflow: auto;
        border-bottom: none;
        border-top: none;
      }
    }
  }
  .checkbox {
    label {
      width: 100%;
      padding: 10px 0;
      box-sizing: border-box;
      display: flex;
    }
  }
}
::v-deep .el-collapse-item__content {
  line-height: normal;
  padding-bottom: 0px;
}
::v-deep .el-checkbox__label {
  display: flex !important;
  justify-content: space-between;
  width: 100%;
}
.el-checkbox{
  display: flex !important;
  padding-top: 10px;
}
::v-deep .el-table {
  thead{
    display: none;
  }
 
}
::v-deep .vue-treeselect__control {
  height: 3vh;
}
::v-deep .vue-treeselect__placeholder,
.vue-treeselect__single-value {
  line-height: 3vh;
}
.huiduButton{
  background: transparent;
  border: none;
  height: 19px;
  line-height: 20px;
  padding: 0;
  // color: #fff;
  font-size: 16px;
}
.huiduButton:hover{
  color:#05afe3 !important;
}
.boardTextStyle{
  position: absolute;
  line-height: 1;
  caret-color: rgba(0,0,0,0);
  user-select: none;
}
::v-deep .sortable-chosen:not(th) {

background-color: rgba(5,175,227,0.1) !important;
}

// .el-table:before {

// height: 0;

// }

// .el-table td {

// border: none;

// }

// .el-table__body tr:hover > td {

// background-color: transparent !important;

// }
</style>
