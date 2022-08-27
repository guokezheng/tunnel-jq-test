<!--
 * @Author: guo peng
 * @Date: 2022-03-17 17:32:02
 * @LastEditTime: 2022-03-29 19:45:02
 * @LastEditors: Please set LastEditors
 * @Description: 情报板批量编辑
 * @FilePath: \TunnelPlatform-V3\src\views\workbench\config\content-batch-edit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    top="6vh"
    @close="closeDialog"
    width="100%"
  >
    <el-row :gutter="24">
      <el-col :span="16">
        <h3>下发列表</h3>
        <el-scrollbar style="height: 40vh">
          <!-- 滚动条 -->
          <el-card>
            <div
              v-for="(items, index) in item"
              :key="index"
              @click="click(items)"
              style="display: flex; flex-flow: column; align-items: center"
            >
              <div
                style="
                  background-color: #000000;
                  text-align: center;
                  line-height: 40px;
                  color: yellow;
                  float: left;
                  margin-top: 10px;
                  position: relative;
                  width: 1000px;
                  height:50px;
                "
                v-bind:style="divStyle"
              >
                <!-- <div
                  v-show="res.imageName == ''"
                  v-for="(res, i) in items.templateContent"
                  :key="i"
                >
                  <span style="margin-top: 20px">{{ res.content }}</span>
                </div> -->
                <div
                  v-for="(res, index) in items.templateContent"
                  :key="index"
                  :id="index"
                  style="
                    line-height: 1;
                    position: absolute;
                    white-space: nowrap;
                    z-index: 1000;
                  "
                  :style="{
                    color: res.fontColor,
                    fontSize: res.fontSize + 'px',
                    fontFamily: res.fontType,
                    letterSpacing: res.fontSpacing + 'px',
                    left: res.coordinate.substring(0, 3) + 'px',
                    top: res.coordinate.substring(3, 6) + 'px',
                  }"
                  v-html="res.content"
                ></div>
                <div
                  v-for="(res, a) in items.templateContent"
                  :key="a"
                  v-show="res.imageName != ''"
                  style="position: absolute"
                  :style="{
                    left: res.coordinate.substring(0, 3) + 'px',
                    top: res.coordinate.substring(3, 6) + 'px',
                  }"
                >
                  <img :src="res.imageName" alt="" />
                </div>
              </div>
              <div class="menu_icon">
                <el-button
                  type="primary"
                  icon="el-icon-top"
                  style="padding: 5px"
                  @click="up(items, index)"
                  v-if="index != 0"
                >
                </el-button>
                <el-button
                  type="primary"
                  icon="el-icon-upload2"
                  style="padding: 5px"
                  @click="top(items, index)"
                  v-if="index != 0"
                >
                </el-button>
                <el-button
                  type="danger"
                  style="float: left; margin: 10px; padding: 5px"
                  icon="el-icon-delete"
                  @click="deleteHandle(items)"
                ></el-button>
                <el-button
                  type="primary"
                  icon="el-icon-bottom"
                  @click="down(items, index)"
                  class="i_bottom"
                  style="padding: 5px"
                  v-if="index < item.length - 1"
                >
                </el-button>
                <el-button
                  type="primary"
                  icon="el-icon-download"
                  @click="bottom(items, index)"
                  class="botbot"
                  style="padding: 5px"
                  v-if="index < item.length - 1"
                >
                </el-button>
              </div>
            </div>
          </el-card>
        </el-scrollbar>
        <h3>设备节目单</h3>
        <el-card style="padding: 0; background-color: #017cc4">
          <div
            style="
              background-color: #000000;
              color: yellow;
              margin: 0 auto;
              overflow: hidden;
              position: relative;
            "
            v-bind:style="divStyle"
          >
            <div
              v-for="(res, index) in templateContent"
              :key="index"
              v-show="res.imageName == ''"
              v-model="templateContent"
              v-drag
              :id="index"
              @click="cliTest(res)"
              style="
                line-height: 1;
                position: absolute;
                white-space: nowrap;
                z-index: 1000;
              "
              :class="{ previewContentCSS: ispreviewContent == index }"
              :style="{
                color: res.fontColor,
                fontSize: res.fontSize + 'px',
                fontFamily: res.fontType,
                letterSpacing: res.fontSpacing + 'px',
                left: res.coordinate.substring(0, 3) + 'px',
                top: res.coordinate.substring(3, 6) + 'px',
              }"
              v-html="res.content"
            ></div>
            <div
              v-for="(item, index) in templateContent"
              v-drag
              :key="index"
              :id="index"
              v-show="item.imageName != ''"
              v-model="templateContent"
              @click="cliTest(item)"
              style="line-height: 1; position: absolute"
              :style="{
                left: item.coordinate.substring(0, 3) + 'px',
                top: item.coordinate.substring(3, 6) + 'px',
              }"
            >
              <img :src="item.imageName" alt="" @click="delitem(item)" />
            </div>
          </div>
        </el-card>
        <el-card
          class="box-card"
          style="margin-top: 2vh;overflow-y: scroll"
        >
          <el-form
            :model="dataForm"
            :rules="dataRule"
            label-width="90px"
            ref="dataForm"
            size="mini"
          >
            <!--            <el-scrollbar style="height:15vh;width: 1113px">-->
            <el-row
              :gutter="24"
              v-for="(res, index) in templateContent"
              :key="index"
              v-show="res.content != ''"
            >
              <el-col :span="22">
                <el-form-item label="内容">
                  <el-input
                    @change="test"
                    type="textarea"
                    clearable
                    placeholder="内容"
                    v-model="res.content"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2" v-if="templateContent.length > 1">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="delTemplateContent(res)"
                ></el-button>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="fontColor" label="字体颜色">
                  <el-select
                    v-model="res.fontColor"
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
                <el-form-item prop="fontSize" label="字体大小">
                  <el-select v-model="res.fontSize" style="width: 100%">
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
                <el-form-item prop="fontType" label="字体类型">
                  <el-select
                    v-model="res.fontType"
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
                <el-form-item prop="fontSpacing" label="字间距(px)">
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="res.fontSpacing"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!--            </el-scrollbar>-->
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item prop="rollSpeed" label="滚动速度(s)">
                  <!--          毫秒    -->
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="dataForm.rollSpeed"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="stopTime" label="停留时间(s)">
                  <el-input-number
                    :min="0"
                    controls-position="right"
                    v-model="dataForm.stopTime"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="applyType" label="适用类型">
                  <!-- <el-input
                    clearable
                    placeholder="适用类型"
                    v-model="dataForm.applyType"
                  ></el-input> -->
                  <el-select
                    v-model="dataForm.applyType"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in applyTypeOptions"
                      :key="item.code"
                      :label="item.content"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="inScreenMode" label="入屏方式">
                  <el-select
                    v-model="dataForm.inScreenMode"
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
              <el-col :span="2">
                <el-button type="primary" @click="addTemplateContent()"
                  >新增</el-button
                >
              </el-col>
              <el-col :span="2">
                <el-button type="primary" @click="chooseImageEvent"
                  >上传图片</el-button
                >
              </el-col>
            </el-row>
            <div v-show="radio1 == 1">
              <el-row :gutter="24" style="display: flex; align-items: center">
                <el-col :span="2" :push="16">当前第{{ current }}条</el-col>
                <el-col :span="4" :push="16">
                  <el-input
                    v-model="targer"
                    style="font-size: 12px"
                    placeholder="请选择移动到目标位置"
                  ></el-input>
                </el-col>
                <el-col :span="2" :push="16">
                  <el-button type="primary" @click="move">移动</el-button>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
        <!-- 上传图片弹出框开始 -->
        <el-dialog
          title="选择图片"
          :visible.sync="dialogVisible"
          top="6vh"
          width="1100px"
          :modal="false"
          append-to-body
        >
          <!-- 选择图片内容区域开始 -->
          <div class="changeImage">
            <el-row style="padding-left: 60px">
              <el-checkbox-group v-model="checkList">
                <el-col
                  :span="8"
                  v-for="(item, index) in imgUrl"
                  :key="index"
                  style="margin-top: 12px"
                >
                  <el-checkbox :label="item.pictureUrl">
                    <div class="photo">
                      <img
                        :src="item.pictureUrl"
                        @dblclick="dblEvent(item.pictureUrl)"
                        draggable="true"
                      />
                    </div>
                  </el-checkbox>
                </el-col>
              </el-checkbox-group>
            </el-row>

            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button
                type="primary"
                @click="sendBtnEvent()"
                style="margin-left: 10px"
                >确 定</el-button
              >
            </span>
          </div>

          <!-- 选择图片内容区域结束 -->
        </el-dialog>
        <!-- 上传图片弹出框结束-->
        <el-card style="margin-top: 20px;">
          <!-- <el-button size="medium"
                     type="primary"
                     v-loading="loading"
                     style="margin-left: 2vw;">
            更换模板类型
          </el-button>
          <el-button size="medium"
                     @click="singleRelease()"
                     type="primary"
                     v-loading="loading"
                     style="margin-left: 15px;">单条发布
          </el-button> -->
          <el-button
            size="medium"
            @click="addRelease()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
            追加信息
          </el-button>
          <el-button
            size="medium"
            @click="saveClose()"
            type="primary"
            v-loading="loading"
            style="margin-left: 15px"
          >
            保存发布内容
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-scrollbar style="height: 100vh; width: 100%">
          <h3>模板选择</h3>
          <div v-for="(res, index) in contentTemplate" :key="index">
            <el-col
              :span="8"
              v-if="res.screenSize == '144*72'"
              @dblclick.native="templateUP(res)"
              style="margin-top: 20px"
            >
              <el-card v-for="(item, index) in res.item" :key="index">
                <div
                  style="
                    background-color: #000000;
                    height: 128px;
                    overflow: hidden;
                    position: relative;
                    width: 466px;
                  "
                >
                  <div
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                    "
                    :style="vmsSizeDiv(res, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="{
                      left: item.coordinate.substring(0, 3) + 'px',
                      top: item.coordinate.substring(3, 6) + 'px',
                    }"
                  >
                    <img :src="item.imageName" alt="" />
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col
              :span="24"
              v-if="res.screenSize == '768*72'"
              @dblclick.native="templateUP(res)"
              style="margin-top: 20px"
            >
              <el-card v-for="(item, index) in res.item" :key="index">
                <div
                  style="
                    background-color: #000000;
                    height: 128px;
                    overflow: hidden;
                    position: relative;
                    width: 466px;
                  "
                >
                  <div
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                    "
                    :style="vmsSizeDiv(res, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="{
                      left: item.coordinate.substring(0, 3) + 'px',
                      top: item.coordinate.substring(3, 6) + 'px',
                    }"
                  >
                    <img :src="item.imageName" alt="" />
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col
              :span="24"
              v-if="res.screenSize == '1024*128'"
              @dblclick.native="templateUP(res)"
              style="margin-top: 20px"
            >
              <el-card>
                <div
                  style="
                    background-color: #000000;
                    height: 128px;
                    overflow: hidden;
                    position: relative;
                    width: 100%;
                  "
                  v-bind:style="divStyle1(res.screenSize)"
                >
                  <div
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                      z-index: 999;
                    "
                    :style="divStyles(res.screenSize, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    v-bind:style="divStyles(res.screenSize, item)"
                  >
                    <img
                      :src="item.imageName"
                      alt=""
                      v-bind:style="
                        divStyleImg(
                          res.screenSize,
                          item.imageName,
                          item.width,
                          item.height
                        )
                      "
                    />
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col
              :span="24"
              v-if="res.screenSize == '480*48'"
              @dblclick.native="templateUP(res)"
              style="margin-top: 20px"
            >
              <el-card>
                <div
                  style="
                    background-color: #000000;
                    height: 128px;
                    overflow: hidden;
                    position: relative;
                    width: 100%;
                  "
                >
                  <div
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                      z-index: 999;
                    "
                    :style="vmsSizeDiv(res, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="{
                      left: item.coordinate.substring(0, 3) + 'px',
                      top: item.coordinate.substring(3, 6) + 'px',
                    }"
                  >
                    <img
                      :src="item.imageName"
                      alt=""
                      v-bind:style="
                        divStyleImg(
                          res.screenSize,
                          item.imageName,
                          item.width,
                          item.height
                        )
                      "
                    />
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col
              :span="12"
              v-if="res.screenSize == '320*32' || res.screenSize == '384*32'"
              @dblclick.native="templateUP(res)"
              style="margin-top: 20px"
            >
              <el-card v-for="(item, index) in res.item" :key="index">
                <div
                  style="
                    background-color: #000000;
                    height: 128px;
                    overflow: hidden;
                    position: relative;
                    width: 466px;
                  "
                >
                  <div
                    v-for="(item, index) in res.item"
                    :key="index"
                    style="
                      line-height: 1;
                      position: absolute;
                      white-space: nowrap;
                    "
                    :style="vmsSizeDiv(res, item)"
                    v-html="item.content"
                  ></div>
                  <div
                    v-for="(item, i) in res.item"
                    :key="i"
                    v-if="item.imageName != ''"
                    :id="i"
                    @click="cliTest(item)"
                    :class="{ previewContentCSS: ispreviewContent == i }"
                    v-model="templateContent"
                    style="line-height: 1; position: absolute"
                    :style="{
                      left: item.coordinate.substring(0, 3) + 'px',
                      top: item.coordinate.substring(3, 6) + 'px',
                    }"
                  >
                    <img :src="item.imageName" alt="" />
                  </div>
                </div>
              </el-card>
            </el-col>
          </div>
        </el-scrollbar>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script>
import contentTable from "./content-table";
import {getTemplates} from "@/api/board/template";
export default {
  components: {
    contentTable,
  },
  data() {
    return {
      current: 0,
      targer: "",
      fontSizeOpt: [],
	    fontTypeOptions: [],
      applyTypeOptions: [],
      dialogVisible: false, //上传图片弹出框
      obj: "",
      visible: false,
      deviceId: undefined,
      index: 0,
      vmsSize: "", //情报板分辨率
      radio1: 0,
      width: "1000",
      ispreviewContent: -1,
      startTxt_x: "000", //X轴
      startTxt_y: "000", //X轴
      dataForm: {
        inScreenMode: "0",
        rollSpeed: "0",
        stopTime: "0",
      },
      title: "",
      item: [], //最终返回数组
      loading: false,
      isAdd: false,
      templateContent: [
        {
          content: "请输入内容",
          fontColor: "yellow",
          fontSize: "24",
          fontType: "KaiTi",
          fontSpacing: 0,
          coordinate: "000000",
          img: "",
          imageName: "",
        },
      ],
      checkList: [], //多选
      imgUrl: [],
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
          code: "White",
          content: "白色",
        },
        {
          code: "Green",
          content: "绿色",
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
          code: "FangSong",
          content: "仿宋",
        },
        {
          code: "LiSu",
          content: "隶书",
        },
      ],
      list: [],
      contentPositionOptions: [
        {
          code: "1",
          content: "左对齐",
        },
        {
          code: "2",
          content: "左右对齐",
        },
        {
          code: "3",
          content: "右对齐",
        },
        {
          code: "4",
          content: "上对齐",
        },
        {
          code: "5",
          content: "上下对齐",
        },
        {
          code: "6",
          content: "下对齐",
        },
      ],
      screenSizeOptions: [
        {
          type: "144*72",
        },
        {
          type: "768*72",
        },
        {
          type: "320*32",
        },
        {
          type: "384*32",
        },
        {
          type: "1024*128",
        },
      ],
      contentTemplate: [], //模板内容
      checkStatus: false, //校验状态
      boardMultiple: "",
    };
  },
  directives: {
    drag: function (el, binding, vnode) {
      let that = vnode.context;
      let dragBox = el;
      dragBox.onmousedown = (e) => {
        let disX = e.clientX - dragBox.offsetLeft;
        let disY = e.clientY - dragBox.offsetTop;
        let clientHeight = dragBox.clientHeight;
        let clientWidth = dragBox.clientWidth;
        document.onmousemove = function (e) {
          //鼠标移动触发事件，元素移到对应为位置
          let left = e.pageX - disX;
          let top = e.pageY - disY;
          //限制区域
          if (left <= 0) {
            left = 0;
          } else if (that.width - left - clientWidth <= 0) {
            left = that.width - clientWidth;
          }
          if (top <= 0) {
            top = 0;
          } else if (that.height - top - clientHeight <= 0) {
            top = that.height - clientHeight;
          }
          dragBox.style.left = left + "px";
          dragBox.style.top = top + "px";
          let a = (Array(3).join("0") + parseInt(left)).slice(-3);
          let b = (Array(3).join("0") + parseInt(top)).slice(-3);
          that.templateContent[dragBox.id].coordinate = a + b;
        };
        document.onmouseup = function () {
          //鼠标抬起，清除绑定的事件，元素放置在对应的位置
          document.onmousemove = null;
          document.onmousedown = null;
        };
        e.preventDefault(); //阻止浏览器的默认事件
      };
    },
  },
  watch: {
    // contentLists: function () {
    //   this.$nextTick(function () {
    //     this.test(this.contentLists[this.contentLists.length - 1]);
    //   });
    // },
    templateContent: {
      deep: true,
      handler: function (newValue, oldValue) {
        var vm = this;
        for (var i = 0; i < vm.templateContent.length; i++) {
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("\r\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("\n", "gm"),
            "<br/>"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp(" ", "gm"),
            "&ensp;"
          );
          vm.templateContent[i].content = vm.templateContent[i].content.replace(
            new RegExp("<font&ensp;", "gm"),
            "<font "
          );
        }

        let inrex = [];
        for (let index = vm.templateContent.length - 1; index >= 0; index--) {
          if (
            vm.templateContent[index].content == "" &&
            vm.templateContent[index].imageName == ""
          ) {
            inrex.push(index);
          }
        }
        for (let index = 0; index < inrex.length; index++) {
          vm.templateContent.splice(inrex[index], 1);
        }
      },
    },
  },
  computed: {
    dataRule() {
      return {};
    },
    // contentLists: function () {
    //   return this.$props.option; // 直接监听props里的status状态
    // },
    previewDiv: function () {
      return {
        color: this.dataForm.fontColor,
        fontFamily: this.dataForm.fontType,
        fontSize: this.dataForm.fontSize + "px",
        width: this.width + "px",
        height: this.height + "px",
      };
    },
    divStyle: function () {
      return {
        width: this.width + "px",
        height: this.height + "px",
        width: this.width + "px",
        height: this.height + "px",
      };
    },
    divStyleHeight: function () {
      return {
        height: this.height + "px",
      };
    },
    templateDiv: function () {
      return {};
    },
    previewCss: function () {
      return {
        left: this.startTxt_x + "px",
        top: this.startTxt_y + "px",
      };
    },
  },
  mounted() {
    this.getDicts("vms_apply_type").then((data) => {
      this.applyTypeOptions = data;
    });
	this.getDicts("fontType").then((data) => {
	  this.fontTypeOptions = data;
	});
  },
  methods: {
    init() {
      this.getDicts("fontSize").then((data) => {
        this.fontSizeOpt = data;
      });
      this.title = "批量编辑发布内容";
      this.visible = true;
      this.$refs["dataForm"] && this.$refs["dataForm"].clearValidate();
      this.vmsSizeDivStyle(this.vmsSize);
      //item 加载templateContent
      this.item.push({
        isRecommend: 0,
        item: "00",
        tissVmsTemplate: this.dataForm,
        templateContent: this.templateContent,
      });

      this.$nextTick(() => {
        //通过分辨率查询模板内容
        let params = {
          screenSize: this.vmsSize,
        };
        getTemplates(params).then((data) => {
          this.contentTemplate = data.rows;
          this.contentTemplate.forEach((e) => {
            e.item.forEach((item) => {
              item.imageName = item.imageName == null ? "" : item.imageName;
            });
          });
        });
      });
    },
    // 上移
    up(data, index) {
      if (index == 0) {
        return this.$message.error("当前为第一行，不可上移！");
      }
      let templateContent = JSON.parse(
        JSON.stringify(this.item[index - 1].templateContent)
      );
      let tissVmsTemplate = JSON.parse(
        JSON.stringify(this.item[index - 1].tissVmsTemplate)
      );
      this.item[index - 1].templateContent = data.templateContent;
      this.item[index - 1].tissVmsTemplate = data.tissVmsTemplate;
      this.item[index].tissVmsTemplate = tissVmsTemplate;
      this.item[index].templateContent = templateContent;
    },
    // 下移
    down(data, index) {
      if (index == this.item.length - 1) {
        return this.$message.error("当前为最后行，不可下移！");
      }
      let templateContent = JSON.parse(
        JSON.stringify(this.item[index + 1].templateContent)
      );
      let tissVmsTemplate = JSON.parse(
        JSON.stringify(this.item[index + 1].tissVmsTemplate)
      );
      this.item[index + 1].templateContent = data.templateContent;
      this.item[index + 1].tissVmsTemplate = data.tissVmsTemplate;
      this.item[index].tissVmsTemplate = tissVmsTemplate;
      this.item[index].templateContent = templateContent;
    },
    //最上移
    top(data, index) {
      if (index == 0) {
        return this.$message.error("当前为第一行，不可上移！");
      }
      let obj = {
        tissVmsTemplate: data.tissVmsTemplate,
        templateContent: data.templateContent,
      };
      this.item.splice(index, 1);
      this.item.unshift(obj);
      for (let index = 0; index <= this.item.length - 1; index++) {
        this.item[index].item = index > 9 ? "0" + index : "00" + index;
        this.item[index].isRecommend = index > 9 ? "0" + index : "00" + index;
      }
    },
    //最下移
    bottom(data, index) {
      if (index == this.item.length - 1) {
        return this.$message.error("当前为最后行，不可下移！");
      }
      let obj = {
        tissVmsTemplate: data.tissVmsTemplate,
        templateContent: data.templateContent,
      };
      this.item.splice(index, 1);
      this.item.push(obj);
      for (let index = 0; index <= this.item.length - 1; index++) {
        this.item[index].item = index > 9 ? "0" + index : "00" + index;
        this.item[index].isRecommend = index > 9 ? "0" + index : "00" + index;
      }
    },
    delitem(item) {
      this.obj = item;
      var _this = this;
      document.onkeydown = function (e) {
        let key = window.event.keyCode;
        if (_this.obj != "") {
          if (key == 46 || key == 8) {
            let inx = "";
            for (let index = 0; index < _this.templateContent.length; index++) {
              if (_this.templateContent[index] == _this.obj) {
                inx = index;
              }
            }
            _this.templateContent.splice(inx, 1);
            _this.obj = "";
          } else {
            _this.obj = "";
          }
        }
      };
    },
    move() {
      let leng = this.item.length;
      if (this.targer < 1 || this.targer > leng) {
        this.targer = "";
        return this.$message.error("输入的目标位置不正确，请重新输入！");
      }
      let templateContent = JSON.parse(
        JSON.stringify(this.item[this.current - 1].templateContent)
      );
      let tissVmsTemplate = JSON.parse(
        JSON.stringify(this.item[this.current - 1].tissVmsTemplate)
      );
      let obj = {
        tissVmsTemplate: tissVmsTemplate,
        templateContent: templateContent,
      };
      this.item.splice(this.current - 1, 1);
      this.item.splice(this.targer - 1, 0, obj);
      this.current = this.targer;
      this.targer = "";
    },
    chooseImageEvent() {
      if (this.item.length == 0) {
        return this.$message.error("请选择模板或追加之后上传！");
      }
      this.imgUrl = [];
      this.getImageInfo();
      this.dialogVisible = true;
    },
    /**
     * 获取图片信息
     */ getImageInfo() {
      let params = {
        vmsSize: this.vmsSize,
      };
      api.queryImgInfo(params).then((data) => {
        if (!data) {
          return;
        }
        let list = data.sort((dataA, dataB) => {
          dataA.id - dataB.id;
        });
        this.imgUrl.push(...list);
      });
    },
    click(items) {
      this.dataForm = items.tissVmsTemplate;
      this.templateContent = items.templateContent;
      this.current = this.item.indexOf(items) + 1;
    },
    //  双击上传图片
    dblEvent(item) {
      this.templateContent.push({
        content: "",
        fontColor: "",
        fontSize: "",
        fontType: "",
        fontSpacing: 0,
        coordinate: "000000",
        img: item,
        imageName: item,
      });
      this.dialogVisible = false;
    },
    // 选择图片弹出框确定按钮
    sendBtnEvent() {
      if (this.templateContent.length + this.checkList.length > 9) {
        this.$message.error("最多可添加十条信息！");
        return;
      }
      if (this.checkList.length == 0) {
        this.$message.error("请选择您要上传的图片！");
        return;
      }
      let arr = [];
      let aindex = "";
      let size=this.vmsSize.split("*");
      this.checkList.forEach((item) => {
        let img = this.imgUrl.find((url) => url.pictureUrl == item);
        if (img.imageWidth == size[0] && img.imageHeight == size[1]) {
          arr.push(img.pictureUrl);
        }
      });

      if (arr.length > 1) {
        this.$message.error("一次只可上传一张底片！");
        return;
      }
      if (arr.length > 0) {
        aindex = this.checkList.indexOf(arr[0]);
      }
      if (aindex != "") {
        let ar = JSON.parse(JSON.stringify(this.checkList[aindex]));
        this.checkList.splice(aindex, 1);
        this.checkList.unshift(ar);
      }
      for (var i = 0; i < this.checkList.length; i++) {
        this.templateContent.push({
          content: "",
          fontColor: "",
          fontSize: "",
          fontType: "",
          fontSpacing: 0,
          coordinate: "000000",
          img: this.checkList[i],
          imageName: this.checkList[i],
        });
      }
      //   this.templateContent.push({
      //     content: "",
      //     fontColor: "",
      //     fontSize: "",
      //     fontType: "",
      //     fontSpacing: 0,
      //     coordinate: "000000",
      //     img: this.checkList[0],
      //     imageName: this.checkList[0],
      //   });
      this.checkList = [];
      this.dialogVisible = false;
    },
    saveClose() {
      //去除templateContent为默认内容的
      this.item.forEach((e) => {
        let a = [];
        for (let index = e.templateContent.length - 1; index >= 0; index--) {
          if (e.templateContent[index].content == "请输入内容") {
            a.push(index);
          }
          if (
            e.templateContent[index].content == "" &&
            e.templateContent[index].imageName == ""
          ) {
            a.push(index);
          }
        }
        a.forEach((ac) => {
          e.templateContent.splice(ac, 1);
        });
      });
      let a = [];
      for (let index = 0; index < this.item.length; index++) {
        if (this.item[index].templateContent.length == 0) {
          a.push(index);
        }
      }
      a.forEach((ac) => {
        this.item.splice(ac, 1);
      });
      //去除item中templateContent长度为0的
      a = [];
      for (let index = this.item.length - 1; index >= 0; index--) {
        if (this.item[index].templateContent.length == 0) {
          a.push(index);
        }
      }
      a.forEach((ac) => {
        this.item.splice(ac, 1);
      });
      if (this.item.length == 0) {
        return this.$message.error("请选择模板或追加之后保存！");
      }
      var that = this;
      if (that.radio1 == 0) {
        //滚屏---追加原本信息
        that.list.forEach((e) => {
          let index = e.item.length - 1;
          that.item.forEach((res) => {
            res.item = index;
            if (res.item.length < 3) {
              let i = 3 - res.item.length;
              for (let z = 0; z < i; z++) {
                res.item = "0" + res.item;
              }
            }
            res.isRecommend = res.item;
            e.item.push(res);
            index++;
          });
        });
      } else if (that.radio1 == 1) {
        //单屏---清空原本信息并追加
        that.list.forEach((e) => {
          e.item = [];
          let index = 0;
          that.item.forEach((res) => {
            res.item = index;
            if (res.item.length < 3) {
              let i = 3 - res.item.length;
              for (let z = 0; z < i; z++) {
                res.item = "0" + res.item;
              }
            }
            res.isRecommend = res.item;
            e.item.push(res);
            index++;
          });
        });
      }
      let params = {
        data: this.list,
      };
      api.getWifth(params).then((res) => {
        this.list = res.data;
        this.$emit("dataItem", this.list);
        this.visible = false;
        this.current = 0;
        this.targer = "";
      });
      this.current = 0;
      this.targer = "";
    },
    closeDialog() {
      this.item = [];
      this.templateContent = [];
      this.templateContent.push({
        id: null,
        templateId: null,
        content: "请输入内容",
        fontColor: "yellow",
        fontSize: "24",
        fontType: "KaiTi",
        fontSpacing: 0,
        coordinate: "000000",
        imageName: "",
        img: "",
      });
      this.current = 0;
      this.targer = "";
    },
    //判断是否包含非法字符和字符预览功能
    test(data) {
      let that = this;
      //   if (data.length == 0) {
      //     return util.$message.showInfo2("内容不能为空");
      //   }
      that.checkStatus = true;
    },
    cliTest(data) {
      this.ispreviewContent = this.templateContent.indexOf(data);
    },
    deleteHandle(data) {
      let a = {};
      this.item.forEach(function (item, index) {
        if (item == data) {
          a = item;
        }
      });
      let b = this.item.indexOf(a);
      this.item.splice(b, 1);
      if (this.item.length == 0) {
        this.templateContent[0].content =
          "请选择模板或追加内容！该内容不做保存！";
      }
    },
    //修改分辨率尺寸
    vmsSizeDivStyle(data) {
      let a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    //双击模板后，追加新的内容
    templateUP(data) {
      let vehicleChangeData = JSON.parse(JSON.stringify(data));
      this.templateContent = vehicleChangeData.item;
      this.dataForm = {
        inScreenMode: vehicleChangeData.inScreenMode,
        rollSpeed: vehicleChangeData.rollSpeed,
        stopTime: vehicleChangeData.stopTime,
      };
      let tager = {
        isRecommend: 0,
        item: "00",
        tissVmsTemplate: {
          id: null,
          screenSize: vehicleChangeData.screenSize,
          inScreenMode: vehicleChangeData.inScreenMode,
          rollSpeed: vehicleChangeData.rollSpeed,
          stopTime: vehicleChangeData.stopTime,
          applyType: null,
          isCurrency: null,
          templateType: null,
          vmsType: null,
          createby: null,
          createtime: null,
          updateby: null,
          updatetime: null,
          remark: null,
        },
        templateContent: vehicleChangeData.item,
      };
      this.item.push(tager);
      this.$forceUpdate();
      //   this.$refs.contentTable.addRelease(data)
    },
    //单条发布按钮
    singleRelease() {
      this.$refs.contentTable.deleteAllHandle();
    },
    //追加发布
    addRelease() {
      this.templateContent = [
        {
          content: "请输入内容",
          fontColor: "yellow",
          fontSize: "24",
          fontType: "KaiTi",
          fontSpacing: 0,
          coordinate: "000000",
          imageName: "",
          img: "",
        },
      ];
      this.item.push({
        isRecommend: 0,
        item: "00",
        tissVmsTemplate: this.dataForm,
        templateContent: this.templateContent,
      });
      this.item = [...new Set(this.item)];
    },
    /*增加新的内容*/
    addTemplateContent() {
      if (this.item.length == 0) {
        return this.$message.error("请选择模板或追加之后新增！");
      }
      if (this.templateContent.length > 9) {
        this.$message.error("最多可添加十条信息！");
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
        imageName: "",
      });
    },
    /*删除内容*/
    delTemplateContent(data) {
      for (let i = 0; i < this.templateContent.length; i++) {
        if (
          this.templateContent.indexOf(data) ==
            this.templateContent.indexOf(this.templateContent[i]) &&
          this.templateContent.length >= 1
        ) {
          this.templateContent.splice(this.templateContent.indexOf(data), 1);
        }
      }
    },
    divStyles: function (vmsSize, a) {
      let b = vmsSize.split("*");
      let obj = {
        color: a.fontColor,
        "font-family": a.fontType,
      };
      if (b[0] == "480") {
        this.$set(obj, "left", a.coordinate.substring(0, 3) + "px");
        this.$set(obj, "top", a.coordinate.substring(3, 6) + "px");
        this.$set(obj, "font-size", a.fontSize + "px");
        this.$set(obj, "letter-spacing", a.fontSpacing + "px");
        return obj;
      } else if (b[0] == "1024") {
        this.$set(obj, "left", a.coordinate.substring(0, 3) / 2 + "px");
        this.$set(obj, "top", a.coordinate.substring(3, 6) / 2 + "px");
        this.$set(obj, "font-size", a.fontSize / 2 + "px");
        this.$set(obj, "letter-spacing", a.fontSpacing / 2 + "px");
        return obj;
      }
    },
    divStyle1: function (vmsSize) {
      let a = vmsSize.split("*");
      if (a[0] == "480") {
        return {
          width: a[0] + "px",
          height: a[1] + "px",
        };
      } else if (a[0] == "1024") {
        return {
          width: a[0] / 2 + "px",
          height: a[1] / 2 + "px",
        };
      }
    },
    divStyleImg(vmsSize, data, width, height) {
      if (data != undefined && data != "") {
        let a = vmsSize.split("*");
        if (a[0] == "480") {
          return {
            width: width + "px",
            height: height + "px",
          };
        } else if (a[0] == "1024") {
          return {
            width: width / 2 + "px",
            height: height / 2 + "px",
          };
        }
      }
    },
    //获取情报板的分辨率后，通过分辨率大小预览显示内容的背景大小
    vmsSizeDiv(res, item) {
      if (res != null) {
        let a = [];
        let size = "";
        if (res.screenSize == "768*72") {
          size =
            "384*32" +
            "*" +
            item.fontSize / 2 +
            "*" +
            item.fontSpacing / 2 +
            "*" +
            item.coordinate.substring(0, 3) / 2 +
            "*" +
            item.coordinate.substring(3, 6) / 2;
          a = size.split("*");
        } else if (res.screenSize == "1024*128") {
          size =
            "512*64" +
            "*" +
            item.fontSize / 2 +
            "*" +
            item.fontSpacing / 2 +
            "*" +
            item.coordinate.substring(0, 3) / 2 +
            "*" +
            item.coordinate.substring(3, 6) / 2;
          a = size.split("*");
        }
        return {
          color: item.fontColor,
          fontFamily: item.fontType,
          width: a[0] + "px",
          height: a[1] + "px",
          fontSize: a[2] + "px",
          letterSpacing: a[3] + "px",
          left: a[4] + "px",
          top: a[5] + "px",
        };
      }
    },
  },
};
</script>
<style  scoped>
/* 页脚 */
.dialog-footer {
  padding-left: 450px;
}
.photoOther img,
.photo img {
  max-width: 300px;
  width: 100%;
  height: 80px;
}
.menu_icon {
  display: flex;
  justify-content: center;
  align-items: center;
}
.el-button--danger {
  margin-right: 0px !important;
}
</style>

