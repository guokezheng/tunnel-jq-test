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
    <div class="dialogStyleBox">
      <div class="dialogLine"></div>
      <div class="dialogCloseButton"></div>
    </div>
      <el-form
        ref="form"
        :model="stateForm"
        label-width="90px"
        label-position="left"
        size="mini"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备类型:">
              {{ stateForm.typeName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隧道名称:">
              {{ stateForm.tunnelName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属方向:">
              {{ getDirection(stateForm.eqDirection) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <!-- <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态:"
            :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
              {{ geteqType(stateForm.eqStatus)}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="消防泵状态:">
              {{ stateForm.xfsStatus}}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <!-- <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="电流Ia:">
              {{ stateForm.ia}} <span style="padding-left:5px">I</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="电流Ib:">
              {{ stateForm.ib}} <span style="padding-left:5px" >I</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="电流Ic:">
              {{ stateForm.ic}} <span style="padding-left:5px">I</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="电压Uab:">
              {{ stateForm.va }}<span style="padding-left:5px">V</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="电压Ubc:">
              {{ stateForm.vb}} <span style="padding-left:5px" >V</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="13">
            <el-form-item label="电压Uac:">
              {{ stateForm.vc}} <span style="padding-left:5px">V</span>
            </el-form-item>
          </el-col>
        </el-row> -->
        <div style="margin-top: 10px">
          <el-form-item label="配置状态:">
            <div class="wrap">
              <el-radio-group
                v-for="(item, index) in eqTypeStateList"
                :key="index"
                v-model="stateForm.state"
                style="display: flex; flex-direction: column"
                @change="$forceUpdate()"
              >
                <el-radio
                  class="el-radio flex-row"
                  :label="item.state"
                  style="align-items: center"
                  :class="[
                    String(stateForm.state) == String(item.state)
                      ? 'el-radio-selcted'
                      : '',
                  ]"
                >
                  <el-row
                    class="flex-row"
                  >
                    <img
                      :width="iconWidth"
                      :height="iconHeight"
                      :src="item.url[1]"
                      v-if="item.url.length > 1"
                    />
                    <img
                      :width="iconWidth"
                      :height="iconHeight"
                      :src="item.url[0]"
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
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="handleOK()"
          class="submitButton"
          >执 行</el-button
        >
        <el-button
          class="closeButton"
          @click="handleClosee()"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getType } from "@/api/equipment/type/api.js"; //查询设备图标宽高
import { getDevice } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态
import { getStateByData } from "@/api/equipment/eqTypeState/api"; //查询设备状态图标
import { setControlDeviceByParam } from "@/api/workbench/config.js"; //提交控制信息


export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],

  data() {
    return {
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),

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
    this.getMessage();
  },
  methods: {
    // 查设备详情
    async getMessage() {
      var that = this;
      if (this.eqInfo.equipmentId) {
        var obj = {};
        var state = "";
        // 查询单选框弹窗信息 -----------------------
        await getDeviceById(this.eqInfo.equipmentId).then((res) => {
          console.log(res, "查询单选框弹窗信息");
          this.stateForm = res.data;
          this.title = this.stateForm.eqName;
          // 查询设备当前状态 --------------------------------
          getDevice(this.eqInfo.equipmentId).then((response) => {
            console.log(response, "查询设备当前状态");
            this.stateForm.state = response.data.state;
            this.getEqTypeStateIcon();
          });
        });
      } else {
        this.$modal.msgWarning("没有设备Id");
      }
    },
    /* 查询设备状态图标*/
    async getEqTypeStateIcon() {
      let that = this;
      await getType(this.eqInfo.clickEqType).then((res) => {
        console.log(res, "查询设备图标宽高");
        this.iconWidth = res.data.iconWidth;
        this.iconHeight = res.data.iconHeight;
      });
      let list = [];
      const param = {
        stateTypeId: this.eqInfo.clickEqType,
        isControl: 1,
      };
      await getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
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
      console.log(that.eqTypeStateList, "that.eqTypeStateList");
      this.visible = true;
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
      if (num) {
        for (var item of this.brandList) {
          if (Number(item.dictValue) == num) {
            return item.dictLabel;
          }
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
    handleOK() {
      const param = {
        devId: this.stateForm.eqId, //设备id
        state: this.stateForm.state,
        // comType: "omron",
      };
      setControlDeviceByParam(param).then((res)=>{
        console.log("消防栓控制成功",res)
        // let msg = res.msg;
        if (res.data == 1) {
          this.$modal.msgSuccess("控制成功");
        } else {
          this.$modal.msgError("控制失败");
        }
      })
      this.$emit("dialogClose");
    },
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
  },
};
</script>

<style  lang="scss" scoped>
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
.el-radio {
  width: 240px;
  height: 40px;
  line-height: 40px;
}
.flex-row {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  height: 40px;
  align-items: center;
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
</style>
