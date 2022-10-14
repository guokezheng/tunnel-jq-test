<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-08 23:06:27
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-25 08:41:00
 * @FilePath: \tunnel-ui\src\views\workbench\config\gisMap.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <!-- <gisMap 
      :themeName="themeName"
      :roadName="roadName"
      :checkBoxBottom="checkBoxBottom"
      :checkBoxLeft="checkBoxLeft"
      :widgetBoxTop="widgetBoxTop"
      :widgetBoxRight="widgetBoxRight"
      :treetBoxTop="treetBoxTop"
      :treetBoxRight="treetBoxRight"
  ></gisMap> -->
  <div class="mapBox" style="height: calc(100% + 60px)">
    <gisMap :tunnelInfo="tunnelInfo" style="height: 100% !important"></gisMap>
    <div class="leftCharts">
      <leftChart1 />
      <leftChart2 />
      <leftChart3 />
      <leftChart4 />
    </div>
    <div class="rightCharts">
      <rightCharts1 />
      <rightCharts2 />
      <rightCharts3 />
      <rightCharts4 />
    </div>
  </div>
</template>
  
<script>
import { defineComponent } from "@vue/composition-api";
import leftChart1 from "./components/leftChart1.vue";
import leftChart2 from "./components/leftChart2.vue";
import leftChart3 from "./components/leftChart3.vue";
import leftChart4 from "./components/leftChart4.vue";
import rightCharts1 from "./components/rightCharts1.vue";
import rightCharts2 from "./components/rightCharts2.vue";
import rightCharts3 from "./components/rightCharts3.vue";
import rightCharts4 from "./components/rightCharts4.vue";

export default {
  data() {
    return {
      themeName: "darkTheme",
      roadName: "济青中线",
      checkBoxBottom: "2%",
      checkBoxLeft: "20%",
      widgetBoxTop: "10%",
      widgetBoxRight: "2",
      treetBoxTop: "100",
      treetBoxRight: "4",
      tunnelInfo: [
        {
          tunnelId: "WLJD-JINAN-YANJIUYUAN-FHSA",
          OPENuRL: "https://www.baidu.com",
        },
        {
          tunnelId: "WLJD-JINAN-YANJIUYUAN-LT",
          OPENuRL: "https://www.baidu.com",
        },
      ],
    };
  },
  components: {
    leftChart1,
    leftChart2,
    leftChart3,
    leftChart4,
    rightCharts1,
    rightCharts2,
    rightCharts3,
    rightCharts4,
  },
};
</script>
<style scoped lang="scss">
.esri-view-surface--inset-outline:focus::after {
  outline: none !important;
}
.leftCharts {
  width: 20%;
  height: calc(100% + 60px);
  position: absolute;
  top: 0;
  left: 0px;
  background: rgba($color: #001021, $alpha: 0.9);
  box-shadow: 20px 0px 30px 10px rgba($color: #001021, $alpha: 0.9);
  padding-top: 25px;
}
.rightCharts {
  width: 20%;
  height: calc(100% + 60px);
  position: absolute;
  top: 0;
  right: 0px;
  background: rgba($color: #001021, $alpha: 0.9);
  box-shadow: -20px 0px 30px 10px rgba($color: #001021, $alpha: 0.9);
  padding-top: 25px;

}
</style>