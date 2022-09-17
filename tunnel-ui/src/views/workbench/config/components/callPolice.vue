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
        <el-row style="margin-top: 10px">
          <el-col :span="24">
            <el-form-item label="IP:"> </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="控制模式:">
              <el-radio-group v-model="radioManual">
                <el-radio :label="1">闪烁</el-radio>
                <el-radio :label="2">常亮</el-radio>
                <el-radio :label="3">常灭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="车道:">
              <el-checkbox-group v-model="checkboxLane" class="checkboxLane">
                <el-checkbox label="1">左车道</el-checkbox>
                <el-checkbox label="2">右车道</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="白灯亮度:">
                <el-slider v-model="whiteLight" :show-tooltip="false" class="sliderClass"></el-slider>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="黄灯亮度:">
                <el-slider v-model="yellowLight" :show-tooltip="false" class="sliderClass"></el-slider>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
          <el-button
            type="primary"
            size="mini"
            @click="handleOK()"
            style="width: 80px"
            class="submitButton"
            >确 定</el-button
          >
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
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  props: ["eqInfo", "brandList", "directionList"],
  data() {
    return {
      stateForm: {},
      title: "",
      visible: true,
      titleIcon: require("@/assets/cloudControl/dialogHeader.png"),
      radioManual: 1,
      checkboxLane:['1'],
      whiteLight:50,
      yellowLight:50,
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
          obj = res.data;

          this.title = obj.eqName;
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
    handleOK(){
        this.$emit("dialogClose");
      },
  },
};
</script>
  <style lang="scss" scoped>
::v-deep .el-radio-group {
  display: flex;
}
::v-deep .el-radio {
  padding: 5px 10px 5px 0px !important;
}
.el-row {
  margin-bottom: -10px;
  display: flex;
  flex-wrap: wrap;
}
::v-deep.sliderClass{
    .el-slider__runway{
        width: 50%;
        background-color: #006784;
        margin:12px 0;
    }
    .el-slider__bar{
        background: linear-gradient(90deg, #00ADED 0%, #007CDD 100%);
    }
    .el-slider__button{
        width:10px;
        height:10px;
        border: solid 1px #fff;
    background-color: #FF9300;
    }
}

</style>
  