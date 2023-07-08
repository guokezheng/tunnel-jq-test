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
          style="height: 30px; transform: translateY(-30px); cursor: pointer"
          @click="handleClosee"
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
              {{ stateForm.typeName }}
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
              {{ getDirection(stateForm.eqDirection) }}
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
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <el-form-item label="设备状态:"
            :style="{color:stateForm.eqStatus=='1'?'yellowgreen':stateForm.eqStatus=='2'?'white':'red'}">
              {{ geteqType(stateForm.eqStatus)}}
            </el-form-item>
          </el-col>
        </el-row>
        <div class="lineClass"></div>
        <el-row style="margin-top: 10px">
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
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          @click="handleClosee()"
          style="width: 80px"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询弹窗信息
import { getDevice } from "@/api/equipment/tunnel/api.js"; //查询设备当前状态

export default {
  props: ["eqInfo", "brandList", "directionList","eqTypeDialogList"],
  watch: {
    tab: {
      handler(newValue) {
        if (newValue) {
          console.log(newValue, "newValue");
          this.getChartMes(newValue);
        }
      },
    },
  },
  data() {
    return {
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),

      title: "",
      stateForm: {}, //弹窗表单
      eqTypeStateList: [],
      visible: false,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
    };
  },
  created() {
    console.log(this.eqInfo.equipmentId, "equipmentIdequipmentId");
    this.getMessage();
    this.getChartMes();
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
          });
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
</style>
