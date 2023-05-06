<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2023-05-05 15:05:30
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-05-06 08:54:21
 * @FilePath: \tunnel-ui\src\views\event\reservePlan\board.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog boardDialog"
      title="情报板模板选择"
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
        <div class="boardRightBox">
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
                    <div class="templateTitle" @click="ondbclick(itm)">
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
} from "@/api/board/template";
import boardData from "@/views/information/board/boardData.json";
import editInfo from "@/views/information/board/editInfo";
import addinfo from "@/views/information/board//addinfo";
import Sortable from "sortablejs";
import { getBoardEditInfo,getVmsTemplateList } from "@/api/information/api.js";
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
      associatedDeviceId: "",
      title: "",
      visible: false, //情报板弹窗
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
      eqType:'',
      number:'',
      index:'',
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
    });
  },
  mounted() {},
  methods: {
    init(number,index,eqType) {
      console.log("进来了");
      this.number = number;
      this.index = index;
      this.eqType = eqType;
      // 1.没有设备id    获取所有的
      // 2.有设备id      获取对应的
      this.allVmsTemplate();
      this.visible = true;
    },
    // 情报板管理右侧查询接口
    allVmsTemplate() {
      const param = {
        eqType: this.eqType,
        category: 0,
      };
      getVmsTemplateList(param).then((res) => {
        let data = res.data;
        console.log(res, "情报板管理右侧查询接口");
        for (let j = 0; j < this.iotTemplateCategoryList.length; j++) {
          let arr = this.iotTemplateCategoryList[j];
          let brr = data[j];
          arr.list = brr;
          this.activeNames.push(j.toString());
        }
        this.$forceUpdate();
        console.log(this.iotTemplateCategoryList, "新模板");
      });
    },
    ondbclick(item){
      console.log(item,'12312312')
      let data = {
        id:item.id,
        content:item.tcontents[0].content,
        number:this.number,
        index:this.index,
      }
      this.$emit('getVmsData',data);
      this.visible = false;
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
      } else if (font == "绿色" || font == 'GreenYellow') {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
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
::v-deep .el-radio-group .el-radio-button__inner {
  background: white;
}
.boardDialog {
  .el-dialog {
    left: 28%;
  }
}
.boardRightBox {
  width: 800px;
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
        width: 768px;
        float: left;
        margin-left: 5px;
        overflow: hidden;
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
    background-image: url(../../../assets/cloudControl/edit3.png);
  }
  > div:nth-of-type(2) {
    background-image: url(../../../assets/cloudControl/edit4.png);
  }
  > div:nth-of-type(1):hover {
    background-image: url(../../../assets/cloudControl/edit1.png);
  }
  > div:nth-of-type(2):hover {
    background-image: url(../../../assets/cloudControl/closeIcon1.png);
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
