<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog workbench-boardDialog"
      :title="title"
      width="835px"
      append-to-body
      :visible="visible"
      :before-close="handleClosee"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div v-show="infoType == 'info'" style="display: flex">
        <div style="float: left">
          <div class="infoBox">
            <el-table
              :data="contentList"
              row-key="ID"
              max-height="550"
              v-loading="loading"
            >
              <el-table-column align="left" width="645">
                <template slot-scope="scope">
                  <div class="contentBox">
                    <div
                      class="content"
                      :style="{
                        color: getColorStyle(scope.row.COLOR),
                        fontSize: addForm.devicePixel
                          ? getFontSize(
                              scope.row.FONT_SIZE,
                              addForm.devicePixel
                            )
                          : '',
                        fontFamily: scope.row.FONT,
                        width: addForm.devicePixel
                          ? getDevicePixel(addForm.devicePixel, 0) + 'px'
                          : '',
                        height: addForm.devicePixel
                          ? getDevicePixel(addForm.devicePixel, 1) + 'px'
                          : '',
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
                        }"
                        class="boardTextStyle"
                        v-html="
                          scope.row.CONTENT
                            ? scope.row.CONTENT.replace(
                                /\n|\r\n/g,
                                '<br>'
                              ).replace(/ /g, '&nbsp')
                            : ''
                        "
                      ></span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" width="135">
                <template slot-scope="scope">
                  <div class="infoButton">
                    <div
                      @click="openQbbDrawer(scope.row, scope.$index, 1)"
                    ></div>
                    <!-- <img
                        src="../../../../assets/cloudControl/edit2.png"
                        @click="openQbbDrawer(scope.row, scope.$index, 1)"
                      /> -->
                    <div @click="delQbbDrawer(scope.$index)"></div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div
            class="openMIniDialogStyle"
            :class="
              msgModeShow ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'
            "
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
            style="width: 800px"
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
                    <el-form-item
                      label="设备状态:"
                      :style="{
                        color:
                          stateForm.eqStatus == '1'
                            ? 'yellowgreen'
                            : stateForm.eqStatus == '2'
                            ? 'white'
                            : 'red',
                      }"
                    >
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
            style="margin: 10px 0 20px; width: 800px"
            class="dialog-footer"
          >
            <el-button
              @click="openDialogVisible(1, 1)"
              style="width: 80px"
              class="submitButton"
              v-hasPermi="['workbench:dialog:save']"
              >添加信息</el-button
            >
            <el-button
              @click="releaseInfo()"
              v-hasPermi="['workbench:dialog:save']"
              :disabled="contentList.length == 0"
              :class="
                contentList.length == 0
                  ? 'zancunButtonDisabled'
                  : 'zancunButton'
              "
              >信息发布</el-button
            >
          </div>
        </div>
        <div class="boardRightBox" v-show="msgModeShow">
          <div class="mesModeBg">
            <div class="mesModeBox">
              <el-collapse v-model="activeNames" @change="handleChange">
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
                      color: getColorStyle(itm.tcontents[0].fontColor),
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
                              .replace(/ /g, '&nbsp')
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
import $ from "jquery";
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getInfo } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import { listDevice } from "@/api/board/informationBoard.js"; //查询弹窗信息
import {
  getAllVmsTemplate,
  uploadBoardEditInfo,
  getBoardContent,
  splicingBoard
} from "@/api/board/template";
import boardData from "@/views/information/board/boardData.json";
import editInfo from "@/views/information/board/editInfo";
import addinfo from "@/views/information/board//addinfo";
import Sortable from "sortablejs";
import { getBoardEditInfo } from "@/api/information/api.js";
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
  // props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  components: {
    editInfo,
    addinfo,
  },
  data() {
    return {
      loading: false,
      msgModeShow: false,
      associatedDeviceId: "",
      title: "",
      visible: false, //情报板弹窗
      videoActive: "information", // tab页
      stateForm: {}, //弹窗表单
      infoType: "info",
      boardEmitItem: {},
      showEmit: false,
      index_: 0,
      addForm: {},
      iotTemplateCategoryList: [],
      activeNames: [],
      templateList: [],
      contentList: [],
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
    };
  },
  watch: {
    contentList: function (newVal, oldVal) {
      this.$nextTick(() => {
        console.log("大弹窗contentList");
        this.rowDrop();
      });
    },
  },
  created() {
    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      // console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
  },
  mounted() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      console.log(eqInfo, "eqInfo");
      console.log("进来了");
      this.contentList = [];
      (this.videoActive = "information"), // tab页
        (this.msgModeShow = true);
      this.openMesMode();
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getmessage();
      this.visible = true;
    },
    // 行拖拽
    rowDrop() {
      if (JSON.parse(JSON.stringify(this.contentList)).length > 0) {
        // 要侦听拖拽响应的DOM对象
        const tbody = document.querySelector(
          ".infoBox .el-table__body-wrapper tbody"
        );
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
    // 信息发布
    releaseInfo() {
      this.$confirm("是否确定发布情报板?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
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
            this.$modal.msgSuccess("发布成功");
          }).catch(() => {
            this.$message({
              type: "info",
              message: res.msg,
            });
          });
          // let parameters = []
          // parameters.push(obj)

          // console.log(arr,"确定发布情报板arr");
          // console.log(parameters,"确定发布情报板parameters");

          // var content = "";
          // var playList = "[Playlist]<r><n>";
          // var Item_Start = "ITEM_NO=";
          // var Item_Content = "ITEM";
          // content += playList;
          // var length = parseInt(this.contentList.length);
          // var Item_No = Item_Start + length + "<r><n>";
          // var value = "";
          // content += Item_No;
          // console.log("确定发布情报板111111");
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
          //   console.log(content, "content");
          // }
          // console.log("确定发布情报板22222222");
          // let protocolType = "GUANGDIAN_V33";
          // let deviceld = this.associatedDeviceId.toString();
          // uploadBoardEditInfo(deviceld, protocolType, content).then(
          //   (response) => {
          //     this.$modal.msgSuccess("发布成功");
          //     console.log(response, "返回结果");
          //   }
          // );
          // .catch(() => {
          //   this.$message({
          //     type: "info",
          //     message: "发布失败，请联系管理员",
          //   });
          // });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消发布",
          });
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
      if (color == "蓝色" || color == "blue") return "000000255000";
      if (color == "绿色" || color == "#00FF00" || color == "GreenYellow")
        return "000255000000";
      if (color == "透明色" || color == "transparent") return "t";
      if (color == "红色" || color == "red") return "255000000000";
      return "255255000000"; //黄色
    },

    // 打开添加信息弹窗
    openDialogVisible(type, mode) {
      if (type == 1) {
        this.$refs.addinfo.init(this.addForm.devicePixel, type, mode);
      } else {
        this.$refs.addinfo.init(this.devicePixelMode, type, mode);
      }
    },
    // 接收子组件新增待发模板
    addInfo(form) {
      form.ID = this.contentList.length;
      console.log(form, "待发新增");
      this.contentList.push(deepClone(form));
      console.log(this.contentList, "this.contentList");
      this.$forceUpdate();
    },
    // 接收子组件form表单 修改
    receiveForm(form) {
      console.log(form, "接收子组件form表单 修改");
      this.contentList.splice(this.index_, 1, form);
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
      this.loading = true;
      getBoardEditInfo(this.associatedDeviceId)
        .then((response) => {
          var parseObject = JSON.parse(response.data[0]);
          var contents = parseObject.content;
          this.contentList = [];
          for (var i = 0; i < contents.length; i++) {
            var content = contents[i];
            var itemId = "ITEM" + this.formatNum(i, 3);
            for (var itm of content[itemId]) {
              itm.COLOR = this.getColorStyle(itm.COLOR);
              itm.FONT_SIZE = Number(itm.FONT_SIZE.substring(0, 2)) + "px";
              itm.ID = i;
              this.contentList.push(itm);
            }
          }
          console.log(this.contentList, "this.contentList11");
          this.loading = false;
          this.rowDrop();
        })
        .catch((e) => {
          this.loading = false;
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
        ID: this.contentList.length,
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
        for (let j = 0; j < this.iotTemplateCategoryList.length; j++) {
          let arr = this.iotTemplateCategoryList[j];
          let brr = data[j];
          arr.list = brr;
          this.activeNames.push(j.toString());
        }
        this.$forceUpdate();
        // console.log(this.iotTemplateCategoryList, "新模板");
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
      this.activeNames = active;
      const param = {
        devicePixel: this.addForm.devicePixel,
        category: active,
      };
      getAllVmsTemplate(param).then((res) => {
        console.log(res, "情报板管理右侧查询接口");
        this.templateList = res.data;
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
          if (type == "left") {
            return coordinate / (height / 75) + "px";
          } else if (type == "top") {
            return coordinate / (height / 75) + "px";
          }
        }
      }
    },
    // 转分辨率
    getDevicePixel(devicePixel, type) {
      let width = devicePixel.split("*")[0];
      let height = devicePixel.split("*")[1];
      // 实际分辨率比页面板子小
      if (width < 630 && height < 75) {
        if (type == 0) {
          return width;
        } else if (type == 1) {
          return height;
        }
      } else {
        // 实际分辨率比页面板子大
        if (width / 630 > height / 75) {
          if (type == 0) {
            return 630;
          } else if (type == 1) {
            return height / (width / 630);
          }
        } else {
          if (type == 0) {
            return width / (height / 75);
          } else if (type == 1) {
            return 75;
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

      if (width < 630 && height < 75) {
        if (font.toString().length == 2) {
          return font + "px";
        } else {
          return font.substring(0, 2) + "px";
        }
      } else {
        if (width / 630 > height / 75) {
          if (font.toString().length == 2) {
            return font / (width / 630) - 1 + "px";
          } else {
            return font.substring(0, 2) / (width / 630) - 1 + "px";
          }
        } else {
          if (font.toString().length == 2) {
            return font / (height / 75) - 1 + "px";
          } else {
            return font.substring(0, 2) / (height / 75) - 1 + "px";
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
      } else if (font == "绿色" || font == "GreenYellow") {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    // 打开信息模板
    openMesMode() {
      let dialog = $(".workbench-boardDialog .el-dialog")[0];
      if (this.msgModeShow == false) {
        this.msgModeShow = true;
        dialog.style.width = "1600px";
        dialog.style.left = "200px";
      } else {
        this.msgModeShow = false;
        dialog.style.width = "835px";
        dialog.style.left = "28%";
      }
    },
    // 根据设备id 获取弹窗内信息
    async getmessage() {
      if (this.eqInfo.equipmentId) {
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询情报板弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          this.associatedDeviceId = res.data.associatedDeviceId;
          if (this.associatedDeviceId) {
            this.onSubmit();
          } else {
            this.$modal.msgWarning("没有设备Id");
          }
          if (res.data.associatedDeviceId) {
            const param = {
              deviceId: res.data.associatedDeviceId,
            };
            listDevice(param).then((response) => {
              console.log(response, "查询设备信息");
              if (response.rows.length > 0) {
                this.addForm = response.rows[0];
              }
              this.allVmsTemplate();
            });
          }
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
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
      if (this.associatedDeviceId) {
        getBoardContent(this.associatedDeviceId)
          .then((res) => {
            console.log(res, "情报板内容查询");
          })
          .catch((e) => {
            console.log(e);
          });
      }
      this.visible = false;
    },
    dialogClose1() {
      this.showEmit = false;
      this.$forceUpdate();
    },
  },
};
</script>

  <style lang="scss" scoped>
::v-deep .el-table--medium .el-table__cell {
  padding: 10px 0 !important;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.openMIniDialogStyle {
  position: absolute;
  left: 813px;
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
/* ::v-deep .el-radio-group .el-radio-button__inner {
  background: white;
} */
.workbench-boardDialog {
  .el-dialog {
    left: 28%;
  }
}
.boardRightBox {
  float: right;
  width: 760px;
  margin-left: 20px;
  .el-collapse {
    border-top: transparent !important;
  }
}
.mesModeBg {
  height: 753px;
  overflow: auto;
  .mesModeBox {
    width: 100%;
    border: solid 1px #01aafd;
    .con {
      width: 100%;
      height: 80px;
      margin-bottom: 10px;
      overflow: hidden;
      display: flex;
      align-items: center;
      .templateTitle {
        height: 75px;
        border: 1px solid #01aafd;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 630px;
        float: left;
        margin-left: 5px;
        overflow: hidden;
      }
      .downIcon {
        width: 75px;
        height: 75px;
        border: solid 1px #01aafd;
        margin-left: 10px;
        justify-content: center;
        display: flex;
        align-items: center;
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
          cursor: pointer;
          background: #f2f8ff;
        }
        > div:hover {
          background: #39adff;
          color: #fff;
          border: solid 1px #39adff;
        }
      }
      .menuBox {
        display: flex;
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
// }
.infoBox {
  width: 783px;
  height: 550px;
  overflow: hidden;
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
  }
}
.contentBox {
  width: 630px;
  height: 75px;
  border: solid 1px #01aafd;
  margin-left: 4px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  .content {
    background: #000;
    overflow: hidden;
    position: relative;
  }
}
.infoButton {
  width: 112px;
  height: 75px;
  border: solid 1px #01aafd;
  display: flex;
  align-items: center;
  justify-content: space-around;
  > div,
  img {
    width: 40px;
    height: 40px;
    line-height: 40px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
    padding-left: 2px;
  }
  > div:nth-of-type(1) {
    background-image: url(../../../../assets/cloudControl/edit3.png);
  }
  > div:nth-of-type(2) {
    background-image: url(../../../../assets/cloudControl/edit4.png);
  }
  > div:nth-of-type(1):hover {
    background-image: url(../../../../assets/cloudControl/edit1.png);
  }
  > div:nth-of-type(2):hover {
    background-image: url(../../../../assets/cloudControl/closeIcon1.png);
  }
}
.disabledClass {
  pointer-events: none;
  cursor: auto !important;
  color: #00152b !important;
}
::v-deep .el-collapse-item__header {
  height: 30px;
  border-bottom: solid 1px #01aafd;
  padding-left: 10px;
}
::v-deep .el-collapse-item__header.is-active {
  border-bottom: transparent;
}
::v-deep .el-collapse-item__content {
  padding-bottom: 0px;
}
::v-deep .el-collapse-item__wrap {
  padding: 0 10px;
  border-bottom: solid 1px #01aafd;
}
::v-deep ::-webkit-scrollbar {
  width: 0px !important;
}
.boardTextStyle {
  position: absolute;
  line-height: 1;
  caret-color: rgba(0, 0, 0, 0);
  user-select: none;
}
::v-deep .el-table {
  border-bottom: none !important;
  thead {
    display: none;
  }
  th.el-table__cell.is-leaf,
  td.el-table__cell {
    border-bottom: none;
  }
}
::v-deep .el-table::before {
  height: 0px;
}
::v-deep .sortable-chosen:not(th) {
  background-color: rgba(5, 175, 227, 0.1) !important;
}
::v-deep .el-loading-mask {
  background: transparent !important;
}
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>
