<template>
  <div class="listBox">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="miniList1">
          <div class="contentTitle">
            预案列表
            <i>Plan list</i>
          </div>
          <div class="alarmsStatisticsBox">
            <div class="listHeader">
              <div style="font-size: 0.8vw; display: flex; width: 100%">
                <div class="headerIndex" style="width: 4vw">序号</div>
                <div>预案列表</div>
              </div>
            </div>
            <vue-seamless-scroll
              :class-option="defaultOption"
              class="listContent"
              :data="faultList"
            >
              <el-row
                v-for="(item, index) in faultList"
                :key="index"
                :style="{
                  backgroundColor:
                    (index + 1) % 2 == 0
                      ? 'rgba(255, 255, 255,0.1)'
                      : 'rgba(255, 255, 255,0)',
                }"
              >
                <el-col style="width: 3vw; text-align: center">{{
                  index + 1
                }}</el-col>
                <el-col style="width: 9vw">{{ item.content }}</el-col>
              </el-row>
            </vue-seamless-scroll>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="miniList2">
          <div class="contentTitle">
            周边物资
            <i>Surrounding resources</i>
          </div>
          <div class="alarmsStatisticsBox">
            <div class="listHeader">
              <!-- <ul> -->
              <div style="font-size: 0.8vw; display: flex; width: 100%">
                <div class="headerIndex" style="width: 4vw">序号</div>
                <div style="width: 5vw">物资名称</div>
                <div style="width: 4vw">物资数量</div>
                <div style="width: 5vw">物资位置</div>
              </div>
              <!-- </ul> -->
            </div>
            <vue-seamless-scroll
              :class-option="defaultOption"
              class="listContent"
              :data="resourcesList"
            >
              <el-row
                v-for="(item, index) in resourcesList"
                :key="index"
                :style="{
                  backgroundColor:
                    (index + 1) % 2 == 0
                      ? 'rgba(255, 255, 255,0.1)'
                      : 'rgba(255, 255, 255,0)',
                }"
              >
                <el-col style="width: 3vw; text-align: center">{{
                  index + 1
                }}</el-col>
                <el-col style="width: 7vw">{{ item.resourcesName }}</el-col>
                <el-col style="width: 3vw">{{ item.resourcesNum }}</el-col>
                <el-col style="width: 5vw">{{ item.resourcesAddress }}</el-col>
              </el-row>
            </vue-seamless-scroll>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="miniList3">
          <div class="contentTitle">
            值班人员
            <i>Personnel on duty</i>
          </div>
          <div class="alarmsStatisticsBox">
            <div class="listHeader">
              <div style="font-size: 0.8vw; display: flex; width: 100%">
                <div class="headerIndex" style="width: 4vw">序号</div>
                <div style="width: 6vw">人员名称</div>
                <div style="width: 8vw">电话号码</div>
              </div>
            </div>
            <vue-seamless-scroll
              :class-option="defaultOption"
              class="listContent"
              :data="PersonnelList"
            >
              <el-row
                v-for="(item, index) in PersonnelList"
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
                <el-col style="width: 5vw">{{ item.PersonnelName }}</el-col>
                <el-col style="width: 6vw">{{ item.PersonnelTel }}</el-col>
              </el-row>
            </vue-seamless-scroll>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- <div class="miniList4">
            <div class="contentTitle">周边资源
              <i>Surrounding resources</i>
            </div>
            <div class="alarmsStatisticsBox">
                 <div class="listHeader">
                     	<div style="font-size: 0.8vw;display: flex;width: 100%;">
                     		<div class="headerIndex" style="width: 4vw">序号</div>
                     		<div style="width: 7vw">资源名称</div>
                     		<div style="width: 6vw">资源位置</div>
                            <div style="width: 6vw">资源距离</div>
                     	</div>
                 </div>
                <vue-seamless-scroll
                  :class-option="defaultOption"
                  class="listContent"
                  :data="materialList"
                >
                    <el-row v-for="(item, index) in materialList" :key="index" 
                    :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(255, 255, 255,0.1)' : 'rgba(255, 255, 255,0)'}">
                        <el-col style="width: 4vw;text-align: center;">{{index+1}}</el-col>
                        <el-col style="width: 6vw;">{{item.materialName}}</el-col>
                        <el-col style="width: 6vw;">{{item.materialAddress}}</el-col>
                        <el-col style="width: 4vw;">{{item.materialDistance}}</el-col>
                    </el-row>
                  </ul>
                </vue-seamless-scroll>
             
            </div>
        </div> -->
  </div>
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  data() {
    return {
      faultList: [
        { content: "隧道临时停车预案" },
        { content: "隧道内行人预案" },
        { content: "隧道内车辆自燃预案" },
        { content: "隧道内火灾报警预案" },
        { content: "隧道内抛洒物预案" },
      ],
      resourcesList: [
        {
          resourcesName: "手持干粉灭火器",
          resourcesNum: "1",
          resourcesAddress: " K350+127",
        },
        {
          resourcesName: "消防沙箱",
          resourcesNum: "1",
          resourcesAddress: " K350+127",
        },
        {
          resourcesName: "消火栓",
          resourcesNum: "1",
          resourcesAddress: " K350+127",
        },
        {
          resourcesName: "手持干粉灭火器",
          resourcesNum: "1",
          resourcesAddress: " K351+127",
        },
        {
          resourcesName: "消防沙箱",
          resourcesNum: "1",
          resourcesAddress: " K351+127",
        },
        {
          resourcesName: "消火栓",
          resourcesNum: "1",
          resourcesAddress: " K351+127",
        },
      ],
      PersonnelList: [
        {
          PersonnelName: "张丁杰",
          PersonnelTel: "17866969781",
        },
        {
          PersonnelName: "王召雨",
          PersonnelTel: "13969250965",
        },
        {
          PersonnelName: "刘兆坤",
          PersonnelTel: "15910077101",
        },
        {
          PersonnelName: "沈晓东",
          PersonnelTel: "18615660586",
        },
      ],
      materialList: [
        {
          materialName: "消防站",
          materialAddress: "白彦镇",
          materialDistance: "2.3km",
        },
        {
          materialName: "中心医院",
          materialAddress: "白彦镇",
          materialDistance: "1.2km",
        },
      ],
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
  methods: {},
};
</script>

<style lang="less" scoped="scoped">
.listBox {
  //   display: flex;
  //   justify-content: space-between;
  background-color: #00598f;
  overflow: hidden;
  //   .miniList1 {
  //     width: 20%;
  //     height: 100%;
  //     // border: solid 1px red;
  //   }
  //   .miniList2 {
  //     width: 28%;
  //     height: 100%;
  //     // border: solid 1px red;
  //   }
  //   .miniList3 {
  //     width: 23%;
  //     height: 100%;
  //     // border: solid 1px red;
  //   }
  //   .miniList4 {
  //     width: 30%;
  //     height: 100%;
  //     // border: solid 1px red;
  //   }
  .headerIndex {
    width: 2vw;
    // border: solid 1px white;
    text-align: center;
  }
}
// ul{
//     list-style-type: none;
// }
</style>
