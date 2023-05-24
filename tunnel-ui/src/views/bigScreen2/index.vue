<template>
  <div>
    <!-- <gisMap
      ref="gisMap"
      :typeName="typeName"
      :themeName="themeName"
      :roadName="roadName"
      :checkBoxBottom="checkBoxBottom"
      :checkBoxLeft="checkBoxLeft"
      :widgetBoxTop="widgetBoxTop"
      :widgetBoxRight="widgetBoxRight"
      :treetBoxTop="treetBoxTop"
      :treetBoxRight="treetBoxRight"
      @mapCreated="mapCreated"
      style="height: 100% !important; z-index: 2"
    ></gisMap> -->
    <gisMap style="height: 100% !important;z-index: 2;"></gisMap>
    <!-- <div style="background: #000;width: 100%;height: 100%;"></div> -->
    <bigScreen v-if="bigScreenList[0].type" class="charts" />
    <bigScreen2 v-if="bigScreenList[1].type" class="charts" />
    <bigScreen3 v-if="bigScreenList[2].type" class="charts" />
    <bigScreen4 v-if="bigScreenList[3].type" class="charts" />
    <!-- <bigScreen5  v-if="bigScreenList[0].type" class="charts"/> -->
    <bigScreen6 v-if="bigScreenList[4].type" class="charts" />
    <div class="bottomBox">
      <el-radio-group v-model="radio1">
        <el-radio-button
          :label="item.title"
          v-for="(item, index) in radioList"
          :key="index"
        ></el-radio-button>
      </el-radio-group>
    </div>
  </div>
</template>
<script>
import bigScreen from "./module1/index";
import bigScreen2 from "./module2/index";
import bigScreen3 from "./module3/index";
import bigScreen4 from "./module4/index";
import bigScreen5 from "./module5/index";
import bigScreen6 from "./module6/index";
export default {
  data() {
    return {
      typeName: 'jiqing',
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
      bigScreenList: [
        {
          label: "综合态势",
          type: false,
        },
        {
          label: "设备运维",
          type: false,
        },
        {
          label: "能耗分析",
          type: false,
        },
        {
          label: "智能养护",
          type: false,
        },
        // {
        //   label:"5",
        //   type:false,
        // },
        {
          label: "运营服务",
          type: true,
        },
      ],
      // bigScreen1: false,
      // bigScreen2: false,
      // bigScreen3: false,
      // bigScreen4: false,
      // bigScreen5: false,
      // bigScreen6: true,
      radio1: "运营服务",
      radioList: [
        {
          title: "综合态势",
        },
        {
          title: "设备运维",
        },
        {
          title: "能耗分析",
        },
        {
          title: "智能养护",
        },
        // {
        //   title:'5',
        // },
        {
          title: "运营服务",
        },
        // {
        //   title:'返回平台',
        // },
      ],
    };
  },
  components: {
    bigScreen,
    bigScreen2,
    bigScreen3,
    bigScreen4,
    bigScreen5,
    bigScreen6,
  },
  watch: {
    radio1: function (newValue, oldValue) {
      for (let item of this.bigScreenList) {
        if (newValue == item.label) {
          item.type = true;
          this.$cache.local.set("bigScreenLabel", item.label);
        } else {
          item.type = false;
        }
      }
    },
  },
  created() {
    let bigScreenLabel = this.$cache.local.get("bigScreenLabel");
    if (bigScreenLabel) {
      this.radio1 = bigScreenLabel;
    }
  },
  mounted() {},
  methods: {
    /**
     * 地图加载完成
     */
    mapCreated() {
      console.log(111);
      console.log(this.$refs.gisMap, "this.$refs.gisMap");
      // this.$refs.gisMap.layerOnOff("trafficJamGzLayer", true);
      this.$refs.gisMap.layerOnOff("videoLayer", false);
      // this.$refs.gisMap.layerOnOff("jqvideoLayer", false);
    },
  },
};
</script>
<style scoped lang="scss">
::v-deep .esri-view-surface--inset-outline:focus::after {
  outline: none !important;
}

.charts {
  position: absolute;
  left: 0;
  top: 20px;
  width: 100%;
  height: calc(100% - 20px);
}
.bottomBox {
  height: 40px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  bottom: 00px;
  margin: 0px auto 30px auto;
  display: flex;
  justify-content: space-between;
  z-index: 2;
  ::v-deep .el-radio-group {
    display: flex;
    .el-radio-button {
      margin-left: 20px;
    }
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 0 !important;
    width: 130px;
    height: 40px;
    line-height: 40px;
    border: none;
    background: url(../../assets/cloudControl/cardTitle.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
    color: #a7c9fb;
    opacity: 0.6;
  }
  ::v-deep .is-active .el-radio-button__inner {
    color: #fff;
    opacity: 1;
  }
}
</style>