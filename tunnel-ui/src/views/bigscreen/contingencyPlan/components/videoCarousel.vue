<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-28 18:15:00
 * @FilePath: \tunnel-ui\src\views\bigscreen\contingencyPlan\components\videoCarousel.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <div class="contentTitle">
      应急车辆列表
      <i>Emergency VEHICLE LIST</i>
    </div>
    <div class="alarmsStatisticsBox">
      <div class="listHeader">
        <div style="font-size: 0.8vw; display: flex; width: 100%">
          <div class="headerIndex" style="width: 4vw; text-align: center">
            序号
          </div>
          <div class="headerIndex" style="width: 5vw; text-align: center">
            车辆类型
          </div>
          <div style="width: 6vw; text-align: center">车牌号</div>
          <div style="width: 6vw; text-align: center">隧道名称</div>
        </div>
      </div>
      <vue-seamless-scroll
        :class-option="defaultOption"
        class="listContent"
        :data="carList"
      >
        <el-row
          v-for="(item, index) in carList"
          :key="index"
          :style="{
            backgroundColor:
              (index + 1) % 2 == 0
                ? 'rgba(255, 255, 255,0.1)'
                : 'rgba(255, 255, 255,0)',
          }"
        >
          <el-col style="width: 4vw; text-align: center">{{
            index + 1
          }}</el-col>
          <el-col style="width: 5vw; text-align: center">{{
            item.vType
          }}</el-col>
          <el-col style="width: 6vw; text-align: center">{{
            item.plateNumber
          }}</el-col>
          <el-col style="width: 6vw; text-align: center">{{
            item.tunnelName
          }}</el-col>
        </el-row>
      </vue-seamless-scroll>
    </div>
  </div>
</template>

<script>
import { getEmergencyVehicle } from "@/api/business/new";
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      carList: [],
    };
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 6, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      getEmergencyVehicle().then((res) => {
        this.carList = res.data;
      });
    },
  },
};
</script>

<style scoped="scoped">
.video-box {
  height: auto;
}
</style>
