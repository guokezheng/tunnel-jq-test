<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
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
        <div style="width: 100%; height: 200px; padding: 0 15px"></div>
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
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      title: "",
      cameraVisible: true, //摄像机弹窗
      videoActive: "information", // tab页
      stateForm: {}, //弹窗表单
      infoType: "info",
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
// .robotTabs {
//   padding: 0 15px;
// }
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
// .theme-light .pagination-container {
//   background: #00152b;
// }

// ::v-deep .el-pagination__total {
//   color: #01aafd !important;
// }
// .el-pagination.is-background .btn-prev,
// .el-pagination.is-background .btn-next,
// .el-pagination.is-background .el-pager li {
//   background-color: #00152b !important;
// }
// .el-pagination.is-background .btn-prev:disabled,
// .el-pagination.is-background .btn-next:disabled {
//   color: "#01AAFD !important";
// }
::v-deep .el-tabs__nav-wrap::after {
  background-color: #dfe4ed;
  opacity: 0.4;
}
::v-deep .el-tabs__active-bar {
  background-color: #01aafd;
}

// ::v-deep .el-switch__label {
//   color: #0a6591 !important;
// }
// ::v-deep .el-switch__label.is-active {
//   color: #ff9900 !important;
// }
// ::v-deep .el-switch__core {
//   height: 14px;
// }
// ::v-deep .el-switch__core:after {
//   height: 12px;
//   width: 12px;
//   top: 0;
// }
// ::v-deep .el-switch.is-checked .el-switch__core::after {
//   margin-left: -12px;
// }

::v-deep .el-radio-button--mini .el-radio-button__inner{
    padding: 6px 13px !important;
}
::v-deep .el-radio-group .el-radio-button__inner{
    background: white;
}
</style>
  