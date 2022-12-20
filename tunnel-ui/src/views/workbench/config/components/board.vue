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
        <div style="width: 93%; height: 200px; padding: 0 15px;border: solid 1px #fff;margin:0 30px 0 15px;overflow: auto;">
          <div v-for="item of 5" style="width:100%;height:50px;border:solid 1px red;margin: 5px 10px;">
          
          </div>
        </div>
        <div class="openMIniDialogStyle"
        :class="openDialog?'el-icon-d-arrow-left':'el-icon-d-arrow-right'"
        @click="openMesMode"
        >信息模板</div>
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
                    {{ getBrandName(stateForm.brandName) }}
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
                  <el-form-item label="设备状态:">
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
                    {{ getBrandName(stateForm.brandName) }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="型号:">
                    {{ stateForm.eqModel }}
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="分辨率:">
                    <!-- {{ '1280*720' }} -->
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
            @click="addInfo()"
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
      <div v-show="infoType == 'add'">
        <div
          style="
            width: calc(100% - 30px);
            height: 60px;
            border: solid 1px white;
            margin: 0 auto 10px;
          "
        ></div>
        <el-form
          ref="form"
          :model="addForm"
          label-width="72px"
          label-position="left"
          size="mini"
          style="padding: 0 15px 15px 15px"
        >
          <el-row>
            <el-col :span="8">
              <el-form-item prop="category" label="所属类别">
                <el-select
                  v-model="addForm.category"
                  placeholder="请选择所属类别"
                  clearable
                  size="small"
                  style="width: 90%"
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
            <el-col :span="8">
              <el-form-item label="分辨率">
                <el-select
                  v-model="addForm.screenSize"
                  placeholder="请选择分辨率"
                  style="width: 90%"
                >
                  <el-option
                    v-for="(item, index) in screenSizeOptions"
                    :key="index"
                    :label="item.type"
                    :value="item.type"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="内容位置">
                <el-radio-group v-model="addForm.COORDINATE" size="mini">
                  <el-radio-button label="左"></el-radio-button>
                  <el-radio-button label="中"></el-radio-button>
                  <el-radio-button label="右"></el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="详细内容">
                <el-input
                  type="textarea"
                  clearable
                  id="textContent"
                  placeholder="详细内容"
                  v-model="addForm.CONTENT"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="字体">
                <el-select
                  v-model="addForm.FONT"
                  filterable
                  placeholder="请选择"
                  style="width: 90%"
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
            <el-col :span="8">
              <el-form-item prop="FONT_SIZE" label="字体大小">
                <el-select v-model="addForm.FONT_SIZE" style="width: 90%">
                  <el-option
                    v-for="item in fontSizeOpt"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item prop="COLOR" label="字体颜色">
                <el-select
                  v-model="addForm.COLOR"
                  filterable
                  placeholder="请选择"
                  style="width: 90%"
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
            <el-col :span="8">
              <el-form-item prop="ACTION" label="入屏方式">
                <el-select
                  v-model="addForm.ACTION"
                  filterable
                  placeholder="请选择"
                  style="width: 90%"
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
            <el-col :span="8">
              <el-form-item prop="STAY" label="停留时间">
                <el-input-number
                  :min="0"
                  controls-position="right"
                  v-model="addForm.STAY"
                  style="width: 90%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog
      class="workbench-dialog mesModeDialog"
      title="信息模板"
      width="400px"
      append-to-body
      :visible="mesModeVisible"
      :before-close="closeMesMode">
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
                v-for="(itm, indx) in templateList"
                :key="indx"
                class="con"
                :style="{
                  'font-size': getFontSize(itm.tcontents[0].fontSize,itm.screenSize),
                  color: itm.tcontents[0].fontColor,
                  fontFamily: itm.tcontents[0].fontType,
                }"
              >
                <div class="templateTitle">
                  <span
                  :style="{
                    left:getCoordinate1(itm.tcontents[0].coordinate.substring(0, 3),itm.screenSize),
                    top:getCoordinate2(itm.tcontents[0].coordinate.substring(3, 6),itm.screenSize),
                  }"
                  style="position:absolute"
                  v-html="itm.tcontents[0].content.replace(/\n|\r\n/g, '<br>').replace(/ /g, ' &nbsp')"></span>
                </div>
                <div class="downIcon">
                  <img src="../../../../assets/cloudControl/download.png"></img>
                </div>
              </div>
            <!-- <div class="mesModeTitle">道路通阻</div>
          <div class="mesModeContent">
            <div class="mesModeLeft">
              <div style="width:260px;height: 50px;background: #000;color: yellow;text-align: center;line-height: 50px;">谨慎驾驶</div>

            </div>
            <div class="mesModeRight"></div> -->

          <!-- </div> -->
          </el-collapse-item>
          </el-collapse>
          

        </div>
      </div>
    </el-dialog>
  </div>
</template>
  
  <script>
import { displayH5sVideoAll } from "@/api/icyH5stream";
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getInfo } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态

export default {
  props: ["eqInfo", "brandList", "directionList", "eqTypeDialogList"],
  data() {
    return {
      openDialog:false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      title: "",
      cameraVisible: true, //摄像机弹窗
      videoActive: "information", // tab页
      stateForm: {}, //弹窗表单
      infoType: "info",
      mesModeVisible:false,
      addForm: {
        category: "",
        screenSize: "",
        COORDINATE: "中",
        CONTENT: "",
        FONT: "",
        FONT_SIZE: "",
        COLOR: "",
        ACTION: "",
        STAY: "",
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
      templateList: [
        {
          id: "1",
          name: "默认模板",
          screenSize:'400*40',
          tcontents: [
            { content: "日照服务区可以做核酸", fontColor: "red", fontSize: "20" ,fontType:'黑体',coordinate:'000000'},
            { content: "日照服务区可以做核酸", fontColor: "red", fontSize: "20" ,fontType:'黑体',coordinate:'000000'},
          ],
        },
        {
          id: "2",
          name: "日常通用",
          screenSize:'400*40',

          tcontents: [
            { content: "日照服务区可以做核酸", fontColor: "red", fontSize: "20" ,fontType:'黑体',coordinate:'000000'},
            { content: "日照服务区可以做核酸", fontColor: "red", fontSize: "20" ,fontType:'黑体',coordinate:'000000'},
          ],
        },
      ],
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    this.getmessage();

    this.getDicts("iot_template_category").then((res) => {
      this.iotTemplateCategoryList = res.data;
      console.log(this.iotTemplateCategoryList, "this.iotTemplateCategoryList");
    });
  },
  methods: {
    // 切换模板
    handleChange(val){
      console.log(val);
    },
    getCoordinate1(coordinate,screenSize){
      var screen = ''
      // if(!screenSize){
      //   screen = this.form.devicePixel.split("*")[0];
      // }else{
        screen = screenSize.split("*")[0]
      // }
      if (screen <= 630) {
        var i = 630 / screen;
        return coordinate * i + "px";
      }else{
        var i = screen/630;
        return coordinate / i + "px";
      }
    },
    getCoordinate2(coordinate,screenSize){
      var screen = ''
      // if(!screenSize){
      //   screen = this.form.devicePixel.split("*")[1];
      // }else{
        screen = screenSize.split("*")[1]
      // }
      if (screen <= 75) {
        var i = 75 / screen;
        return coordinate * i + "px";
      }else{
        var i = screen/75;
        return coordinate / i + "px";
      }
    },
    // 转字号
    getFontSize(font,screenSize) {
      if(!font){
        return
      }
      var screen = ''
      if(!screenSize){
        screen = this.form.devicePixel.split("*")[0];

      }else{
        screen = screenSize.split("*")[0]
      }
      if (screen <= 630) {
        var i = 630 / screen;
       
        if(font.toString().length == 2){
          return font * i + "px";
        }else {
          return font.substring(0, 2) * i + "px";
        }
      }else{
        var i = screen/630;
        if(font.toString().length == 2){
          return font / i + "px";
        }else {
          return font.substring(0, 2) / i + "px";
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
    openMesMode(){
      this.mesModeVisible = true
    },
    // 关闭信息模板
    closeMesMode(){
      this.mesModeVisible = false

    },
    // 根据设备id 获取弹窗内信息
    async getmessage() {
      if (this.eqInfo.equipmentId) {
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询摄像机弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          displayH5sVideoAll(res.data.secureKey);
        });
        // await getInfo(this.eqInfo.clickEqType).then((response) => {
        //     console.log(response, "查询设备当前状态");
        //     this.stateForm.state = response.data.state;
        //   });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    addInfo() {
      this.infoType = "add";
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
      this.$emit("dialogClose");
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
.openMIniDialogStyle{
  position:absolute;
  right:0;
  width:20px;
  height:85px;
  background: #D8D8D8 linear-gradient(180deg, #1EACE8 0%, #0074D4 100%);
  top:66px;
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

::v-deep .el-radio-button--mini .el-radio-button__inner{
    padding: 6px 13px !important;
}
::v-deep .el-radio-group .el-radio-button__inner{
    background: white;
}
.boardDialog{
  left: 20%;
  margin: unset;
  width: 620px;
  z-index: 2017;
}
.mesModeDialog{
  left: 53%;
  margin: unset;
  width: 400px;
  z-index: 2017;
  .mesModeBg{
    padding: 10px;
    background: #fff;
    width: calc(100% - 30px);
    height: 340px;
    margin: 10px auto;
    overflow: auto;
    
    .mesModeBox{
      width: 100%;
      height: 100px;
      border: solid 1px #E1E4E6;
      .con {
        width: 100%;
        height: 50px;
        margin-bottom: 10px;
        overflow: hidden;
        display: flex;
        border: 1px solid #f3f3f3;
        .templateTitle {
          height: 75px;
          border: 1px solid #f3f3f3;
          background: black;
          position: relative;
          width: 630px;
          float: left;
        }
        .downIcon{
          width: 50px;
          height: 50px;
          border-left: solid 1px #f3f3f3;
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
      
      .mesModeTitle{
        font-size: 14px;
        color: rgba(0,22,44,0.7);
        line-height: 30px;
        height: 30px;
        width: 100%;
        border: solid 1px #CFD5E0;
      }
      .mesModeContent{
        display: flex;
        width: 100%;
        height: 70px;
        .mesModeLeft{
          width: 280px;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }
        .mesModeRight{
          width: calc(100% - 280px);
          height: 100%;
          border-left: solid 1px #CFD5E0;
        }
      }
    }
  }
}
::v-deep .el-collapse-item__header{
        height: 30px;
      }
::v-deep .el-collapse-item__content{
        padding-bottom: 10px;
      }
::v-deep .el-collapse-item__wrap{
  padding: 0 10px;
}
::v-deep ::-webkit-scrollbar {
  width: 0px;
}
</style>
  