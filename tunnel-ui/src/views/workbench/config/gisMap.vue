<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-08 23:06:27
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-16 10:44:43
 * @FilePath: \tunnel-ui\src\views\workbench\config\gisMap.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <gisMap
    :themeName="themeName"
    :roadName="roadName"
    :checkBoxBottom="checkBoxBottom"
    :checkBoxLeft="checkBoxLeft"
    :checkBoxRight="checkBoxRight"
    :widgetBoxTop="widgetBoxTop"
    :widgetBoxRight="widgetBoxRight"
    :treetBoxTop="treetBoxTop"
    :treetBoxRight="treetBoxRight"
  ></gisMap>
  <!-- <gisMap :tunnelInfo="tunnelInfo"></gisMap> -->
</template>
    
<script>
import { defineComponent } from "@vue/composition-api";

export default {
  data() {
    return {
      themeName: "lightTheme",
      roadName: "济青中线",
      checkBoxBottom: "2%",
      checkBoxLeft: "20%",
      widgetBoxTop: "10%",
      widgetBoxRight: "2",
      treetBoxTop: "100",
      treetBoxRight: "4",
      tunnelInfo: [
        {
          tunnelId: "WLJD-JINAN-YANJIUYUAN-FHS",
          OPENuRL: "https://www.baidu.com",
        },
        {
          tunnelId: "WLJD-JINAN-YANJIUYUAN-LT",
          OPENuRL: "https://www.baidu.com",
        },
      ],
    };
  },
};
</script>
<style scoped>
.esri-view-surface--inset-outline:focus::after {
  outline: none !important;
}
.breadcrumb-container {
  display: none;
}
</style>