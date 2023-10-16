<template>
  <div style="width: 100%; height: 100%">
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table"
      :title="title"
      width="450px"
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
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
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

          <el-col :span="13">
            <el-form-item label="所属机构:">
              {{ stateForm.deptName }}
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="设备IP:">
              {{ stateForm.ip }}
            </el-form-item>
          </el-col>
          <!-- <el-col :span="11">
            <el-form-item label="设备厂商:">
              {{ stateForm.supplierName }}
            </el-form-item>
          </el-col> -->

          <el-col :span="13">
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
        <div class="lineClass"></div>
        <el-form-item label="设备类型:" label-width="90px">
          <el-select
            v-model="eqStateForm.eqType"
            clearable
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="dict in eqStateList"
              :key="dict.eqType"
              :label="dict.eqName"
              :value="dict.eqType"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <el-table :data="dataList" max-height="200" empty-text="暂无操作日志" >
        <el-table-column
          type="index"
          :index="indexMethod"
          label="序号"
          width="68"
          align="center"
        ></el-table-column>
        <el-table-column label="设备名称" align="center" prop="eqName"/>
        <el-table-column
          label="设备状态"
          align="center"
          prop="eqState"
        />
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import { getDeviceById } from "@/api/equipment/eqlist/api.js"; //查询单选框弹窗信息

export default {
  data() {
    return {
      visible: false,
      title: "",
      stateForm: {},
      brandList: [],
      eqInfo: {},
      eqTypeDialogList: [],
      directionList: [],
      eqStateForm: {
        eqType: 1,
      },
      eqStateList: [
        {
          eqType: 1,
          eqName: "车道指示器",
        },
      ],
      dataList: [
        {
            eqName:"车道指示器",
            eqState:"正红反绿"
        },
        {
            eqName:"车道指示器",
            eqState:"正红反绿"
        },
        {
            eqName:"车道指示器",
            eqState:"正红反绿"
        },
        {
            eqName:"车道指示器",
            eqState:"正红反绿"
        },
        {
            eqName:"车道指示器",
            eqState:"正红反绿"
        },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
    };
  },
  created() {},
  methods: {
    init(eqInfo, brandList, directionList, eqTypeDialogList) {
      this.eqInfo = eqInfo;
      this.brandList = brandList;
      this.directionList = directionList;
      this.eqTypeDialogList = eqTypeDialogList;
      this.getMessage();
      this.visible = true;
    },
    async getMessage() {
      // 查询单选框弹窗信息 -----------------------
      await getDeviceById(this.eqInfo.equipmentId).then((res) => {
        console.log(res, "查询单选框弹窗信息");
        this.stateForm = res.data;
        this.title = this.stateForm.eqName;
        // this.stateForm2.lampType = res.data.eqStatus
        console.log(this.stateForm, "stateForm");
      });
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
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
      this.visible = false;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-dialog {
  pointer-events: auto !important;
}
</style>