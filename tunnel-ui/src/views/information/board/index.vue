<template>
  <div class="container infoBoardBox" style="height: 100%">
    <div
      style="
        height: 100%;
        margin-left: 0px;
        margin-right: 0px;
        display: flex;
        justify-content: space-between;
      "
    >
      <div style="width: 20%" class="verticalBox">
        <p class="bigTitle">情报板列表</p>
        <el-form ref="form" :model="form">
          <el-form-item prop="deptId">
            <treeselect
              v-model="form.deptId"
              :options="siteList"
              :props="siteProps"
              :show-count="true"
              noResultsText="暂无数据"
              placeholder="请选择归属部门"
              @select="changeSite"
              style="width: 100%"
              size="small"
              :beforeClearAll="beforeClearAll"
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
                <div>
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
                    <el-tooltip :content="itm.deviceIp" placement="top">
                      <div>{{ itm.deviceName }}</div>
                    </el-tooltip>
                      <el-tooltip content="回读当前信息" placement="top">
                        <el-button
                          class="el-icon-tickets huiduButton"
                          @click.stop.prevent="onSubmit(itm.deviceId)"
                          :disabled="submitButton"
                        ></el-button>
                      </el-tooltip>
                    </el-checkbox>
                  </el-checkbox-group>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>
        </el-form>
      </div>
      <div
        style="border-left: 1px solid #05afe3; width: 37.8%"
        class="verticalBox"
      >
        <div class="bigTitle">
          <div>
            <span>待下发信息</span>
            <span v-if="form.devicePixel">【 {{ form.devicePixel }}】</span>
          </div>

          <div class="controlBox">
            <el-button
              type="primary"
              @click.native="openDialogVisible(1, 2)"
              :disabled="form.devicePixel ? false : disabledButton"
              >添加信息</el-button
            >
            <el-button
              type="primary"
              @click="publishInfo"
              :disabled="
                !form.devicePixel ||
                contentList.length == 0 ||
                checkedCities.length == 0
              "
              >发布信息</el-button
            >
          </div>
        </div>
        <div class="contentBox">
          <el-table
            :data="contentList"
            row-key="ID"
            v-loading="loading"
            max-height="700"
            :key="toggleIndex"
          >
            <el-table-column width="545">
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
                        top: getCoordinate(
                          scope.row.COORDINATE.substring(3, 6),
                          'top'
                        ),
                        maxHeight:getScreenSize(form.devicePixel, 'height') + 'px',
                      }"
                      class="boardTextStyle"
                      v-html="
                        scope.row.CONTENT? scope.row.CONTENT.replace(/\n|\r\n/g,'<br>').replace(/ /g, '&nbsp'): ''"
                    ></span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column width="130">
              <template slot-scope="scope">
                <div class="menuBox">
                  <!-- <el-tooltip content="加入信息模板" placement="top">
                    <div @click="arrowRight(scope.row)"></div>
                  </el-tooltip> -->

                  <el-tooltip content="编辑" placement="top">
                    <div
                      @click="openQbbDrawer(scope.row, scope.$index, 1)"
                    ></div>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <div @click="delQbbDrawer(scope.$index)"></div>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div
        style="border-left: 1px solid #05afe3; width: 42.2%"
        class="verticalBox"
      >
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
          <el-collapse v-model="activeNames">
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
                  color: getColorStyle(itm.tcontents[0].fontColor),
                  fontFamily: itm.tcontents[0].fontType,
                }"
              >
                <div class="templateTitle">
                  <div
                    :style="{
                      width: getScreenSize(itm.screenSize, 'width') + 'px',
                      height: getScreenSize(itm.screenSize, 'height') + 'px',

                    }"
                    style="background: black; position: relative;overflow: hidden;"
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
                        maxHeight:getScreenSize(itm.screenSize, 'height') + 'px',
                      }"
                      class="boardTextStyle"
                      v-html="
                        itm.tcontents[0].content
                          .replace(/\n|\r\n/g, '<br>')
                          .replace(/ /g, '&nbsp')
                      "
                    ></span>
                  </div>
                </div>
                <div class="menuBox">
                  <el-tooltip content="加入待下发信息" placement="top">
                    <div
                      @click="arrowLeft(itm)"
                      :class="
                        disabledButton && !form.devicePixel
                          ? 'disabledClass'
                          : ''
                      "
                      style="cursor: pointer"
                    ></div>
                  </el-tooltip>
                  <el-tooltip content="编辑" placement="top">
                    <div @click="editOutline(itm, indx, 2)"></div>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <div @click="handleDelete(itm)"></div>
                  </el-tooltip>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="arrowRightVisible"
      width="20%"
      :before-close="dialogClose"
    >
      <el-row>
        <el-col :span="18">
          <el-select v-model="toRightCategory" placeholder="请选择所属类别">
            <el-option
              v-for="item in iotTemplateCategoryList"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            >
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" plain @click="arrowRightAllVmsTemplate">
            确定
          </el-button>
        </el-col>
      </el-row>
    </el-dialog>
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
  </div>
</template>
<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Sortable from "sortablejs";
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
  deleteTemplate,
  splicingBoard
} from "@/api/board/template";

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
  name: "Device",
  components: {
    addinfo,
    editInfo,
    Treeselect,
  },
  dicts: ["iot_board_direction"],

  data() {
    return {
      toggleIndex:0,
      toRightCategory: "", //向右类别绑定
      arrowRightVisible: false, //向右类别弹窗
      loading: false,
      submitButton: false, //回读禁用
      iotBoardActive: "", //情报板设备 绑定
      iotBoardList: [], //情报板设备列表
      boardDirectionList: [], //方向
      siteList: [], //站点
      userQueryParams: {
        userName: this.$store.state.user.name,
      }, //用户
      siteProps: {
        value: "id",
        label: "label",
        children: "children",
      }, //站点
      devicePixelMode: null, //分辨率
      editType: 0,
      disabledButton: true, //禁用按钮
      showEmit: false, //修改组件
      index_: 0, //待下发 编辑项index
      index: 0, //模板 编辑项 index
      deviceId: "", //情报板id
      boardEmitItem: null, //修改彈窗的數據
      userDeptId: "", //管理站id
      deptList: [], //分中心下拉框
      mechanismList: [], //管理机构下拉框
      tunnelData: [], //所属隧道下拉框
      devicessizeList: [], //分辨率下拉框
      deviceList: [], //分辨率
      checkedCities: [], //多选 选中项
      form: {
        company: null,
        mechanism: null,
        tunnel: null,
        localInfo: null,
        devicePixel: null,
        deptId: this.userDeptId,
      }, //情报板列表表单
      supplier: null, //报文类型
      activeNames: [], //模板 展开项
      iotTemplateCategoryList: [], //模板
      checkAll: false,
      isIndeterminate: false,
      contentList: [], //待下发数据
      toRightItem: {}, //向右添加到模板的数据
    };
  },
  watch: {
    contentList: function (newVal, oldVal) {
      this.$nextTick(()=>{
        this.rowDrop();
      })
    },
    // 清空部门
    'form.deptId':function(newVal,oldVal){
      this.form.tunnel = ''
      this.tunnelData = []
      this.boardDirectionList = [];
      // this.changeMechanism(newVal.id);
    },
    // 清空隧道
    'form.tunnel':function(newVal,oldVal){
      this.form.eqDirection = ''
      this.boardDirectionList = []
      this.iotBoardList = []
    },
    iotBoardList:function(newVal,oldVal){
      console.log(newVal,"iotBoardActive")
      if(newVal.length == 0){
        this.form.devicePixel = ''
        this.allVmsTemplate('no')
      }
    }
    // // 改变方向
    // 'form.eqDirection':function(newVal,oldVal){
    //   console.log(newVal,"newVal")
    //   this.iotBoardList = []
    // }
  },
  created() {
    this.getUserDept();
    this.getInfoMode();
  },
  mounted() {
    // this.rowDrop();
  },

  methods: {
    // 获取信息模板字典表
    getInfoMode() {
      this.getDicts("iot_template_category").then((res) => {
        this.iotTemplateCategoryList = res.data;
        console.log(
          this.iotTemplateCategoryList,
          "this.iotTemplateCategoryList"
        );
      });
    },

    // 行拖拽
    rowDrop() {
      if(JSON.parse(JSON.stringify(this.contentList)).length>0){
        // 要侦听拖拽响应的DOM对象
        const tbody = document.querySelector(".contentBox .el-table__body-wrapper tbody");
        const _this = this;
        Sortable.create(tbody, {
          // 结束拖拽后的回调函数
          onEnd({ newIndex, oldIndex }) {
            const currentRow = _this.contentList.splice(oldIndex, 1)[0];
            _this.contentList.splice(newIndex, 0, currentRow);
          },
        });
      }

    },

    // 级联 管理站
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
        console.log(response, "级联");
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
        console.log(childs, "childschilds");
        if (childs.length == 0) {
          this.siteList = options[0].children;
        } else {
          this.siteList = childs;
        }
        this.checkData(this.siteList[0]);
      });
    },
    checkData(obj) {
      if (obj.children && obj.children.length > 0) {
        this.checkData(obj.children[0]);
      } else {
        this.changeSite(obj);
        this.form.deptId = obj.id;
      }
    },
    // 清空站点
    beforeClearAll(){
      console.log(111111)
      this.form.deptId = null;
    },
    // 改变站点
    changeSite(index) {
      console.log(index,"changeSite")
      this.form.tunnel = "";
      this.boardDirectionList = [];
      if(index.children != undefined && index.children.length == 1){
        this.changeMechanism(index.children[0].id);
      }else {
        this.changeMechanism(index.id);
      }
    },

    // 通过所属机构查隧道
    changeMechanism(val) {
      listTunnels(val).then((response) => {
        this.form.tunnel = response.rows[0].tunnelId;
        this.tunnelData = response.rows;
        this.changeDirection();
      });
    },

    //改变方向
    changeDirection(val) {
      this.getDicts("iot_board_direction").then((res) => {
        this.boardDirectionList = res.data;
        if (val) {
          this.form.eqDirection = val;
        } else {
          this.form.eqDirection = res.data[0].dictValue;
        }
        if(this.form.tunnel){
          this.getIotBoard();
        }
      });
    },

    // 改变隧道
    changeTunnel(value) {
      this.getDicts("iot_board_direction").then((res) => {
        this.boardDirectionList = res.data;
      })
      this.checkedCities = [];
      this.contentList = [];
      this.form.tunnel = value;
      this.form.devicePixel = ''
      this.changeDirection()
    },
    // 情报板设备 折叠面板
    getIotBoard() {
      this.checkAll = false;
      let param = {
        eqDirection: this.form.eqDirection,
        tunnelId: this.form.tunnel,
        manageAgencyId: this.form.deptId,
      };
      getIotBoardList(param).then((res) => {
        console.log(res, "查询情报板设备列表");
        this.iotBoardList = res.data;
        if (res.data.length > 0) {
          this.iotBoardActive = res.data[0].devicePixel;
          this.handleChange(res.data[0].devicePixel);
        } else {
          this.getInfoMode();
        }
        this.$forceUpdate();
      });
    },

    // 板子分辨率换算
    getScreenSize(num, type) {
      // console.log(num)
      let width = num.split("*")[0];
      let height = num.split("*")[1];
      // 实际分辨率比页面板子小
      if (width <= 450 && height <= 75) {
        if (type == "width") {
          return width;
        } else if (type == "height") {
          return height;
        }
      } else {
        // 实际分辨率比页面板子大
        if (width / 450 > height / 75) {
          if (type == "width") {
            return 450;
          } else if (type == "height") {
            return height / (width / 450);
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

    // 接收子组件 新增模板
    getActiveNames() {
      this.allVmsTemplate();
    },

    // 向左新增待下发
    arrowLeft(item) {
      var list = {
        FONT_SIZE: item.tcontents[0].fontSize + "px",
        COLOR: item.tcontents[0].fontColor,
        CONTENT: item.tcontents[0].content,
        COORDINATE: item.tcontents[0].coordinate,
        FONT: this.getFont(item.tcontents[0].fontType),
        SPEED: item.tcontents[0].fontSpacing, //字体间距
        ACTION: item.inScreenMode, //出屏方式
        STAY: item.stopTime, //停留时间
        category: item.category, //所属类别
        ID: this.contentList.length,
      };
      this.contentList.push(list);
      this.toggleIndex ++
      console.log(this.contentList, "this.contentList");
      console.log(this.toggleIndex,"toggleIndex")
    },

    // 向右添加模板
    arrowRight(item) {
      this.toRightItem = item;
      if (!item.category) {
        this.arrowRightVisible = true;
      } else {
        this.arrowRightAllVmsTemplate();
      }
    },

    // 向右添加模板
    arrowRightAllVmsTemplate() {
      console.log(this.contentList, "不选所属类别向右contentList");
      console.log(this.toRightItem, this.toRightCategory);
      let item = this.toRightItem;
      let templateId = "";
      let method = "put";
      const params1 = {
        inScreenMode: item.ACTION,
        screenSize: this.form.devicePixel,
        applyType: "",
        category: item.category ? item.category : this.toRightCategory,
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
        console.log(data, "新增口返回data");
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
        addTemplateContent(params2)
          .catch((err) => {
            throw err;
          })
          .then((res) => {
            if (res.code == 200) {
              this.allVmsTemplate("no");
            }
          });
        // this.getActiveNames(item.category);
      });
      this.arrowRightVisible = false;
    },

    // 修改弹窗
    editOutline(item, index, type) {
      this.index = index;
      this.editType = type;
      console.log(item, "修改弹窗");
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
      this.showEmit = true;
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let content = "是否确认删除选中数据项?";
      if (ids == null || ids == undefined || ids == [] || ids == "") {
        content = "是否确认删除当前情报板模板?";
      }
      this.$confirm(content, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return deleteTemplate(ids);
        })
        .then(() => {
          this.allVmsTemplate("no");
          this.$modal.msgSuccess("删除成功");
        });
    },

    // 情报板管理右侧查询接口
    allVmsTemplate(type) {
      const param = {
        devicePixel: this.form.devicePixel,
      };
      getAllVmsTemplate(param).then((res) => {
        let data = res.data;
        console.log(res, "情报板管理右侧查询接口");
        let jArr = [];

        for (let j = 0; j < this.iotTemplateCategoryList.length; j++) {
          let arr = this.iotTemplateCategoryList[j];
          let brr = data[j];
          arr.list = brr;
          jArr.push(j.toString());
        }
        if (type != "no") {
          this.activeNames = jArr;
        }
        this.$forceUpdate();
        console.log(this.iotTemplateCategoryList, "新模板");
      });
    },

    // 删除中间模板
    delQbbDrawer(index) {
      if (index > -1) {
        this.contentList.splice(index, 1);
        this.$message.success("删除成功");
      }
    },

    // 发布信息
    publishInfo() {

      this.$confirm("是否确定发布情报板?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        const objAll = {};
          objAll.deviceIds = this.eqInfo.equipmentId;
          let that  = this
          let newArr = this.contentList.map(function(item){
            let obj = {}
            obj.STAY = item.STAY;
            obj.ACTION = item.ACTION;
            obj.SPEED = item.SPEED;
            obj.COORDINATE = item.COORDINATE.replace("-", "0");
            obj.COLOR = that.getColorValue(item.COLOR);
            obj.FONT = that.getFontValue(item.FONT);

            obj.FONT_SIZE = item.FONT_SIZE.substring(0, 2);
            obj.CONTENT = item.CONTENT.replace(/\n|\r\n/g, "<r><n>");
              return obj
          })
          objAll.parameters = newArr
          const param = {
            objectData:JSON.stringify(objAll)
          }
          console.log(param,"param")
          splicingBoard(param).then((res)=>{
            console.log(res)
          })
          // console.log(this.contentList, "发布信息");
          // var content = "";
          // var playList = "[Playlist]<r><n>";
          // var Item_Start = "ITEM_NO=";
          // var Item_Content = "ITEM";
          // content += playList;
          // var length = parseInt(this.contentList.length);
          // var Item_No = Item_Start + length + "<r><n>";
          // var value = "";
          // content += Item_No;
          // for (var i = 0; i < this.contentList.length; i++) {
          //   value = ("000" + i).slice(-3);
          //   content += Item_Content + value + "=";
          //   content += this.contentList[i].STAY + ",";
          //   content += this.contentList[i].ACTION + ",";
          //   content += this.contentList[i].SPEED + "," + "\\";
          //   content +=
          //     "C" + this.contentList[i].COORDINATE.replace("-", "0") + "\\";
          //   content += "S00\\";
          //   content +=
          //     "c" + this.getColorValue(this.contentList[i].COLOR) + "\\";
          //   content += "f" + this.getFontValue(this.contentList[i].FONT);
          //   content +=
          //     this.contentList[i].FONT_SIZE.substring(0, 2) +
          //     this.contentList[i].FONT_SIZE.substring(0, 2);
          //   content += this.contentList[i].CONTENT.replace(
          //     /\n|\r\n/g,
          //     "<r><n>"
          //   );

          //   if (i + 1 != this.contentList.length) {
          //     content += "<r><n>";
          //   }
          // }
          // console.log(content, "content");

          // let protocolType = "GUANGDIAN_V33";
          // let deviceld = this.checkedCities.toString();
          // uploadBoardEditInfo(deviceld, protocolType, content).then((response) => {
          //     console.log(response, "返回结果");
          //     loading.close();
          //     this.$modal.msgSuccess("发布成功");

          // }).catch(() => {
          //     loading.close();
          // })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消发布情报板",
          });
          // loading.close();
        });
    },

    // 接收子组件新增待发模板
    addInfo(form) {
      console.log(form, "待发新增");
      form.ID = this.contentList.length;
      this.contentList.push(deepClone(form));
      this.$message.success("添加成功");
      this.$forceUpdate();
    },

    // 接收子组件form表单 修改
    receiveForm(form) {
      console.log(form, "接收子组件form表单 修改");
      this.contentList.splice(this.index_, 1, form);
      this.$forceUpdate();
      this.allVmsTemplate("no");
    },

    // 打开添加信息弹窗
    openDialogVisible(type, mode) {
      this.$refs.addinfo.init(this.form.devicePixel, type, mode);
    },

    // 全选
    handleCheckAllChange(val) {
      this.checkedCities = val ? this.deviceList : [];
    },

    // 多选
    handleCheckedCitiesChange(value) {
      console.log(value,"value")
      this.checkedCities = value;
      let val = JSON.parse(JSON.stringify(value));
      for (let itm of this.deviceList) {
        if (val.indexOf(itm) > -1) {
          this.checkAll = true;
        } else {
          this.checkAll = false;
          return
        }
      }
      this.$forceUpdate();
    },
    // 情报板列表手风琴
    handleChange(val) {
      this.contentList = [];
      this.deviceList = [];
      this.disabledButton = true;
      this.checkedCities = [];
      this.checkAll = false;
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
    },

    // 转报文 ITEM001
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },

    // 转报文 字体
    getFontValue(font) {
      if (font == "黑体" || font == "SimHei") return "h";
      if (font == "楷体" || font == "KaiTi") return "k";
      if (font == "仿宋" || font == "FangSong") return "f";
      if (font == "隶书" || font == "LiSu") return "l";
      else return "s";
    },

    // 转报文 色号
    getColorValue(color) {
      if (color == "蓝色" || color == "blue") return "000000255000";
      if (color == "绿色" || color == "#00FF00" || color == "GreenYellow") return "000255000000";
      if (color == "透明色") return "t";
      if (color == "红色" || color == "red") return "255000000000";
      return "255255000000"; //黄色
    },

    // 向内容显示区域添加一条信息
    addContentDisplayInfos(addId, no, disContent) {
      var jsonArr = {};
      jsonArr.itemId = addId;
      jsonArr.disContent = disContent;
      this.disContentList.push(jsonArr);
    },

    // 回读
    async onSubmit(deviceId) {
      this.submitButton = true;
      this.loading = true;
      this.contentList = [];
      // 获取情报板修改页面信息
      await getBoardEditInfo(deviceId).then((response) => {
        console.log(response, "response");
        if (response.code != 200) {
          this.$message.error(`设备网络连接异常，请稍后重试`);
          this.loading = false;
          this.submitButton = false;
          return;
        }
        if (response.data[0] == undefined) {
          this.$message(response.msg);
          return;
        }

        var parseObject = JSON.parse(response.data[0]);
        console.log(parseObject,"parseObject")
        var protocolType = parseObject.support.PROTOCOL_TYPE;
        var contents = parseObject.content;
        if (
          typeof contents == "undefined" ||
          typeof protocolType == "undefined"
        ) {
          this.$message(response.msg);
          this.$forceUpdate();
          this.loading = false;
          return;
        }
        this.supplier = protocolType;
        console.log(contents, "contents");
        for (var i = 0; i < contents.length; i++) {
          var content = contents[i];
          var itemId = "ITEM" + this.formatNum(i, 3);
          var con = content[itemId][0];
          con.COLOR = this.getColorStyle(con.COLOR);
          con.FONT_SIZE = Number(con.FONT_SIZE.substring(0, 2)) + "px";
          con.ID = i;

          this.contentList.push(con);
        }
        console.log(this.contentList, "this.contentList");
        this.submitButton = false;
        this.loading = false;
        this.$forceUpdate();
      });

      // 根据情报板id获取分辨率
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

    // 待下发 编辑
    openQbbDrawer(item, index, type) {
      this.index_ = index;
      console.log(item);
      this.boardEmitItem = item;
      this.boardEmitItem.screenSize = this.form.devicePixel;
      this.boardEmitItem.deviceId = this.deviceId;
      this.boardEmitItem.type = type;

      this.showEmit = true;
    },

    // 转颜色
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色" || font == 'GreenYellow') {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },

    // 换算字号
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
      if (width <= 450 && height <= 75) {
        if (font.toString().length == 2) {
          return font + "px";
        } else {
          return font.substring(0, 2) + "px";
        }
      } else {
        if (width / 450 > height / 75) {
          if (font.toString().length == 2) {
            return font / (width / 450) + "px";
          } else {
            return font.substring(0, 2) / (width / 450) + "px";
          }
        } else {
          if (font.toString().length == 2) {
            return font / (height / 75) + "px";
          } else {
            return font.substring(0, 2) / (height / 75) + "px";
          }
        }
      }
    },

    // 换算文字在模板的位置
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
      if (width <= 450 && height <= 75) {
        return coordinate + "px";
      } else {
        if (type == "left") {
          if (width < 450 && height > 75) {
            return coordinate / (height / 75) + "px";
          } else {
            if (width / 450 >= height / 75) {
              return coordinate / (width / 450) + "px";
            } else {
              return coordinate / (height / 75) + "px";
            }
          }
        } else if (type == "top") {
          if (width < 450 && height > 75) {
            return coordinate / (height / 75) + "px";
          } else {
            if (width / 450 >= height / 75) {
              return coordinate / (width / 450) + "px";
            } else {
              return coordinate / (height / 75) + "px";
            }
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
      } else if (font == "Simsun") {
        return "宋体";
      } else if (font == "SimHei") {
        return "黑体";
      } else {
        return font;
      }
    },
    // //  上移
    // moveTop(i, item) {
    //   if (item && i) {
    //     let obj = { ...this.contentList[i - 1] };
    //     this.contentList.splice(i - 1, 1, item);
    //     this.contentList.splice(i, 1, obj);
    //     this.$forceUpdate();
    //   }
    // },
    // // 下移
    // moveBottom(i, item) {
    //   if (item && typeof i === "number") {
    //     let obj = { ...this.contentList[i + 1] };
    //     this.contentList.splice(i + 1, 1, item);
    //     this.contentList.splice(i, 1, obj);
    //     this.$forceUpdate();
    //   }
    // },
    dialogClose() {
      this.showEmit = false;
      this.arrowRightVisible = false;
      setTimeout(() => {
        this.allVmsTemplate("no");
      }, 500);
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  .verticalBox {
    height: 100%;
    padding: 10px;
    .bigTitle {
      padding: 15px 0;
      border-bottom: 1px solid #05afe3;
      margin-bottom: 10px;
      display: flex;
      justify-content: space-between;
      height: 60px;
    }
    .contentBox {
      width: 100%;
      height: calc(100% - 67px);
      // overflow: auto;

      .con {
        border: 1px solid #05afe3;
        height: 75px;
        position: relative;
        width: 540px;
        overflow: hidden;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .menuBox {
        display: flex;
        align-items: center;
        // margin-left: 10px;
        border: solid 1px #05afe3;
        height: 75px;
        justify-content: space-around;
        align-items: center;
        div {
          background-repeat: no-repeat;
          background-size: 100% 100%;
          width: 40px;
          height: 40px;
        }
        > div:first-of-type {
          background-image: url(../../../assets/cloudControl/edit2.png);
        }
        > div:nth-of-type(2) {
          background-image: url(../../../assets/cloudControl/edit4.png);
        }
        // >div:last-of-type{
        //   background-image: url(../../../assets/cloudControl/edit4.png);
        // }
        > div:first-of-type:hover {
          background-image: url(../../../assets/cloudControl/edit1.png);
        }
        > div:nth-of-type(2):hover {
          background-image: url(../../../assets/cloudControl/closeIcon1.png);
        }
        // >div:last-of-type:hover{
        //   background-image: url(../../../assets/cloudControl/closeIcon1.png);
        // }
        i {
          font-size: 24px;
          color: #666;
          cursor: pointer;
          caret-color: rgba(0, 0, 0, 0);
          user-select: none;
        }
        i:hover {
          color: #05afe3 !important;
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
        margin: 10px 0;
        padding: 0 20px;
        overflow: hidden;
        display: flex;

        .templateTitle {
          height: 75px;
          border: 1px solid #05afe3;
          position: relative;
          width: 540px;
          float: left;
          display: flex;
          justify-content: center;
          align-items: center;
          overflow: hidden;
        }
        .menuBox {
          display: flex;
          float: right;
          margin-left: 10px;
          border: solid 1px #05afe3;
          width: calc(100% - 550px);
          display: flex;
          justify-content: space-around;
          align-items: center;

          div {
            background-repeat: no-repeat;
            background-size: 100% 100%;
            width: 40px;
            height: 40px;
          }
          > div:first-of-type {
            background-image: url(../../../assets/cloudControl/toLeft2.png);
          }
          > div:nth-of-type(2) {
            background-image: url(../../../assets/cloudControl/edit2.png);
          }
          > div:last-of-type {
            background-image: url(../../../assets/cloudControl/edit4.png);
          }
          > div:first-of-type:hover {
            background-image: url(../../../assets/cloudControl/toLeft1.png);
          }
          > div:nth-of-type(2):hover {
            background-image: url(../../../assets/cloudControl/edit1.png);
          }
          > div:last-of-type:hover {
            background-image: url(../../../assets/cloudControl/closeIcon1.png);
          }
          i {
            font-size: 24px;
            color: #666;
            padding-left: 4px;
            cursor: pointer;
            caret-color: rgba(0, 0, 0, 0);
            user-select: none;
          }
          i:hover {
            color: #05afe3;
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
        max-height: 74vh !important;
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
.el-checkbox {
  display: flex !important;
  padding-top: 10px;
}
::v-deep .el-table {
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
  thead {
    display: none;
  }
}
::v-deep .vue-treeselect__control {
  height: 3vh;
}
::v-deep .vue-treeselect__placeholder,
.vue-treeselect__single-value {
  line-height: 4vh;
}
::v-deep .vue-treeselect--has-value .vue-treeselect__input{
  vertical-align: baseline;
}
.huiduButton {
  background: transparent;
  border: none;
  height: 19px;
  line-height: 20px;
  padding: 0;
  // color: #fff;
  font-size: 16px;
}
.huiduButton:hover {
  color: #05afe3 !important;
}
.boardTextStyle {
  position: absolute;
  line-height: 1;
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
  white-space: normal;
  word-break: break-all;
  overflow: hidden;
}
::v-deep .sortable-chosen:not(th) {
  background-color: rgba(5, 175, 227, 0.1) !important;
}

::v-deep .el-select .el-input .el-input__inner {
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
}
::v-deep .el-collapse-item__header,
.el-collapse-item__content {
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
}
::v-deep .el-collapse {
  border-bottom: transparent;
}
::v-deep .el-table .cell {
  padding-left: 5px;
}
::v-deep .el-checkbox+.el-checkbox{
  margin-left:0px !important;
}
</style>
