<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-24 14:35:26
 * @FilePath: \tunnel-ui\src\views\bigscreen\warning\components\eventList.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <div class="contentTitle">
      事件信息列表
      <i>event information list</i>
    </div>
    <div class="alarmsStatisticsBox">
      <div class="listHeader">
        <ul style="padding-left: 0px">
          <li style="font-size: 0.8vw; display: flex; width: 100%">
            <div class="headerIndex" style="width: 4vw; text-align: center">
              序号
            </div>
            <div class="headerName" style="width: 28vw; text-align: center">
              事件信息
            </div>
          </li>
        </ul>
      </div>
      <vue-seamless-scroll
        :class-option="defaultOption"
        class="listContent"
        :data="faultList"
      >
        <!-- <ul>
                <li v-for="(item, index) in faultList" :key="index" 
                    :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(4, 19, 86,0.1)' : '#041356'}">
                    <span style="position: absolute;left: 1.5vw;">{{index+1}}</span>
                    <span style="position: absolute;left: 3.5vw;">{{item.content}}</span>
                </li>
              </ul> -->
        <el-row
          v-for="(item, index) in faultList"
          :key="index"
          type="flex"
          :style="{
            backgroundColor: (index + 1) % 2 == 0 ? '#0a5e97' : '#11629d',
          }"
        >
          <el-col style="width: 4vw; text-align: center">{{
            index + 1
          }}</el-col>
          <el-col style="width: 28vw">{{ item.eventTitle }}</el-col>
        </el-row>
      </vue-seamless-scroll>
    </div>
  </div>
</template>

<script>
import { getTrafficIncident } from "@/api/business/new";
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      faultList: [],
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
      // let tunnelId = { tunnelId: "WLJD-JiNan-YanJiuYuan-FHS" };
      getTrafficIncident().then((res) => {
        this.faultList = res.data;
      });
    },
  },
};
</script>

<style>
</style>
