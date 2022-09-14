<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog"
      :title="title"
      width="450px"
      append-to-body
      :visible="visible"
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
          style="height: 30px; transform: translateY(-30px)"
        />
      </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="90px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备类型:">
              {{ stateForm.eqTypeName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="所属方向:">
              {{ stateForm.eqDirection }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.brandName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备状态:">
              <!-- {{ stateForm.eqStatus }} -->
              {{ "在线" }}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <div style="margin-top: 10px">
          <el-form-item label="配置状态:">
            <div class="wrap">
              <el-radio-group
                v-for="(item, index) in eqTypeStateList"
                :key="index"
                v-model="stateForm.state"
                style="display: flex; flex-direction: column"
              >
                <el-radio
                  v-if="stateForm.eqType == item.type && item.control == 1"
                  class="el-radio flex-row"
                  :label="item.state"
                  style="align-items: center"
                  :class="[
                    stateForm.state == item.state ? 'el-radio-selcted' : '',
                  ]"
                >
                  <el-row class="flex-row">
                    <img
                      :width="iconWidth"
                      :height="iconHeight"
                      :src="item.url[0]"
                    />
                    <img
                      :width="iconWidth"
                      :height="iconHeight"
                      :src="item.url[1]"
                      v-if="item.url.length > 1"
                    />
                    <div style="margin: 0 0 0 10px; display: inline-block">
                      {{ item.name }}
                    </div>
                  </el-row>
                </el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
  
  <script>
// import flvjs from "flv.js";
// import { getDevices } from "@/api/equipment/eqlist/api";
// import { getStateByData } from "@/api/equipment/eqTypeState/api";
// import { getEqTypeStateByType } from "@/api/equipment/eqTypeState/api"; //查询设备状态图标
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getType } from "@/api/equipment/type/api.js"; //查询设备图标宽高
import { getInfo } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import { getStateByData } from "@/api/equipment/eqTypeState/api"; //查询设备状态图标
export default {
  props: ["equipmentId", "clickEqType", "brandList", "directionList"],
  data() {
    return {
      title: "",
      stateForm: {}, //弹窗表单
      eqTypeStateList: [],
      visible: false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      iconWidth: "",
      iconHeight: "",
    };
  },
  created() {
    console.log(this.equipmentId, "equipmentIdequipmentId");
    console.log(this.clickEqType, "clickEqTypeclickEqTypeclickEqType");

    this.getEqTypeStateIcon();
    this.getMessage();
    this.loadFlv();
  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.equipmentId) {
        var obj = {};
        var state = "";
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          obj = res.data;

          // 查询设备当前状态 --------------------------------
          getInfo(this.clickEqType).then((response) => {
            console.log(response, "查询设备当前状态");
            // debugger
            obj.state = response.data.state;

            this.title = obj.typeName + " " + obj.pile;
            this.stateForm = {
              brandName: that.getBrandName(obj.brandId), //厂商
              eqDirection: that.getDirection(obj.eqDirection),
              pile: obj.pile, //桩号
              eqTypeName: obj.typeName, //设备类型名称
              tunnelName: obj.tunnelName, //隧道名称
              deptName: obj.deptName, //所属机构
              eqType: obj.eqType, //设备类型号
              state: obj.state,
            };
            console.log(this.stateForm, "stateForm");

            this.visible = true;
          });
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    /* 查询设备状态图标*/
    async getEqTypeStateIcon() {
      let that = this;
      await getType(this.clickEqType).then((res) => {
        console.log(res, "查询设备图标宽高");
        this.iconWidth = res.data.iconWidth;
        this.iconHeight = res.data.iconHeight;
      });
      let list = [];
      const param = {
        stateTypeId: this.clickEqType,
        isControl: 1,
      };
      await getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
        // debugger
        list = response.rows;
      });

      that.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        that.eqTypeStateList.push({
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
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
    loadFlv() {
      // if (flvjs.isSupported()) {
      //   var videoElement = document.getElementById("videoBox");
      //   var flvPlayer = flvjs.createPlayer({
      //     type: 'flv',
      //     url: 'http://10.166.139.12:8081/live/22456.flv' //你的url地址
      //   });
      //   flvPlayer.attachMediaElement(videoElement);
      //   flvPlayer.load();
      //   flvPlayer.play();
      // }
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
  },
};
</script>
  
  <style lang="scss" scoped>
.videoTabs {
  padding: 0 15px;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.el-radio-selcted {
  padding: 5px 300px 5px 20px;
  margin: 2px 0px;
  color: #c0ccda;
  border-radius: 4px;
  background-color: #455d79;
}

.el-radio-button--medium .el-radio-button__inner {
  padding: 5px 10px;
}
::v-deep .el-radio__label {
  height: 40px !important;
}
.flex-row {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  height: 40px;
  align-items: center;
}
.el-radio {
  width: 240px;
  height: 40px;
  line-height: 40px;
}
</style>
  