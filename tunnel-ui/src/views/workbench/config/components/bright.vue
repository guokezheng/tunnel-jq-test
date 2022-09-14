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
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  props: ["equipmentId", "clickEqType", "brandList", "directionList"],
  data() {
    return {
      stateForm: {},
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
    };
  },
  created() {
    this.getMessage();
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
    // 关闭弹窗
    handleClosee() {
      this.$emit("dialogClose");
    },
  },
};
</script>
