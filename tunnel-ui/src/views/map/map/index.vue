<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-08 23:06:27
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-19 09:41:17
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
  <div class="mapBox" style="height:calc(100% + 60px)">
    <gisMap :tunnelInfo="tunnelInfo" style="height: 100% !important;"></gisMap>
    <home style="width:100%;height:calc(100% + 60px);position:absolute;top:0;left:0"></home>

  </div>

</template>
  
<script>
import { defineComponent } from "@vue/composition-api";
import home from "./home"; //诱导灯弹窗

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
  components:{
    home
  }
};
</script>
<style scoped lang="scss">
.esri-view-surface--inset-outline:focus::after {
  outline: none !important;
}
</style>