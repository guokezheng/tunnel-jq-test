<template lang="html">
  <div class="cron" :val="value_">
    <el-tabs v-model="activeName">
      <el-tab-pane label="秒" name="s">
        <second-and-minute v-model="sVal" lable="秒" ref="clearS"></second-and-minute >
      </el-tab-pane>
      <el-tab-pane label="分" name="m">
        <second-and-minute v-model="mVal" lable="分" ref="clearM"></second-and-minute >
      </el-tab-pane>
      <el-tab-pane label="时" name="h">
        <hour v-model="hVal" lable="时" ref="clearH"></hour>
      </el-tab-pane>
      <el-tab-pane label="日" name="d">
        <day v-model="dVal" lable="日" ref="clearD"></day>
      </el-tab-pane>
      <el-tab-pane label="月" name="month">
        <month v-model="monthVal" lable="月" ref="clearMonth"></month>
      </el-tab-pane>
      <el-tab-pane label="周" name="week">
        <week v-model="weekVal" lable="周" ref="clearW"></week>
      </el-tab-pane>
      <el-tab-pane label="年" name="year">
        <year v-model="yearVal" lable="年" ref="clearY"></year>
      </el-tab-pane>
    </el-tabs>
    <!-- table -->
    <el-table
       :data="tableData"
       size="mini"
       border
       style="width: 100%;">
       <el-table-column
         prop="sVal"
         label="秒"
         width="70">
       </el-table-column>
       <el-table-column
         prop="mVal"
         label="分"
         width="70">
       </el-table-column>
       <el-table-column
         prop="hVal"
         label="时"
         width="70">
       </el-table-column>
       <el-table-column
         prop="dVal"
         label="日"
         width="70">
       </el-table-column>
       <el-table-column
         prop="monthVal"
         label="月"
         width="70">
       </el-table-column>
       <el-table-column
         prop="weekVal"
         label="周"
         width="70">
       </el-table-column>
       <el-table-column
         prop="yearVal"
         label="年">
       </el-table-column>
     </el-table>
  </div>
</template>

<script>
import SecondAndMinute from "./secondAndMinute";
import hour from "./hour";
import day from "./day";
import month from "./month";
import week from "./week";
import year from "./year";
export default {
  props: {
    value: {
      type: String,
    },
  },
  data() {
    return {
      //
      activeName: "s",
      sVal: "",
      mVal: "",
      hVal: "",
      dVal: "",
      monthVal: "",
      weekVal: "",
      yearVal: "",
    };
  },
  watch: {
    value(a, b) {
      this.updateVal();
      this.$emit("changeValue", true);
    },
  },
  computed: {
    tableData() {
      return [
        {
          sVal: this.sVal,
          mVal: this.mVal,
          hVal: this.hVal,
          dVal: this.dVal,
          monthVal: this.monthVal,
          weekVal: this.weekVal,
          yearVal: this.yearVal,
        },
      ];
    },
    value_() {
      console.log(this.dVal, this.weekVal);
      if (!this.dVal && !this.weekVal) {
        return "";
      }
      if (this.dVal !== "?" && this.weekVal !== "?") {
        this.dVal = "*";
        this.weekVal = "*";
        return this.$message.warning("日期与星期不可以同时为“指定”");
      }
      if (this.yearVal == "undefined") {
        return this.$message.warning("请重新选择“年”");
      }
      let v = `${this.sVal} ${this.mVal} ${this.hVal} ${this.dVal} ${this.monthVal} ${this.weekVal} ${this.yearVal}`;
      if (v !== this.value) {
        this.$emit("input", v);
      }
      return v;
    },
  },
  methods: {
    updateVal() {
      if (!this.value) {
        return;
      }
      let arrays = this.value.split(" ");
      this.sVal = arrays[0];
      this.mVal = arrays[1];
      this.hVal = arrays[2];
      this.dVal = arrays[3];
      this.monthVal = arrays[4];
      this.weekVal = arrays[5];
      this.yearVal = arrays[6];
    },
    checkClear() {
      this.$refs.clearS.checkboxClear();
      this.$refs.clearM.checkboxClear();
      this.$refs.clearH.checkboxClear();
      this.$refs.clearD.checkboxClear();
      this.$refs.clearMonth.checkboxClear();
      this.$refs.clearW.checkboxClear();
      this.$refs.clearY.checkboxClear();
      this.activeName = "s";
    },
  },
  created() {
    this.updateVal();
    this.activeName = "s";
  },
  components: {
    SecondAndMinute,
    hour,
    day,
    month,
    week,
    year,
  },
};
</script>

<style lang="css">
.cron {
  text-align: left;
  padding: 10px;
  background: #fff;
  border: 1px solid #dcdfe6;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
}
</style>
