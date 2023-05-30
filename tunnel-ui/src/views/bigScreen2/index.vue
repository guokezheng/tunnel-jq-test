<template>
  <div>
    <gisMap
      ref="gisMap"
      :typeName="typeName"
      :themeName="themeName"
      :roadName="roadName"
      :organizationId="organizationId"
      :checkBoxBottom="checkBoxBottom"
      :checkBoxLeft="checkBoxLeft"
      :widgetBoxTop="widgetBoxTop"
      :widgetBoxRight="widgetBoxRight"
      :mapZoom="mapZoom"
      style="height: 100% !important; z-index: 2"
    ></gisMap>
    <!-- <div style="width:100%;height:100%;background-color: #000;"></div> -->
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
      typeName: "jiqing",
      themeName: "darkTheme",
      roadName: "济青中线",
      organizationId:"",
      checkBoxBottom: "2%",
      checkBoxLeft: "20%",
      widgetBoxTop: "14%",
      widgetBoxRight: "20%",
      treetBoxTop: "100",
      treetBoxRight: "4",
      mapZoom:12,
      
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
      // opts: {
      //   icon: "data:image/png;base64,xxx", //base64位图片
      //   width: 40, // 图片宽度
      //   height: 49, //图片高度
      //   noViewIn: true, //添加一个点时 是否禁止放大 不设置默认放大
      //   xyArr: [
      //     // 点信息
      //     {
      //       latitude: 36.801815, //纬度
      //       longitude: 118.539138, //经度
      //       icon: "data:image/png;base64,xxx", //base64位图片 当属性设置时使用当前图片 默认使用总图片
      //       iconWidth: 32, //图片宽度 当属性设置时使用当前图片宽度 默认使用总图片宽度
      //       iconHeight: 32, //图片高度 当属性设置时使用当前图片高度 默认使用总图片高度
      //       attributes: {
      //         //弹窗参数
      //         labelTextInfo: {
      //           //label设置
      //           show: true, //是否显示
      //           text: "文字文字文字文字", //文字内容
      //           color: "#ffffff", //文字颜色
      //           haloColor: "#000000", //文字描边颜色
      //           haloSize: "1px", //文字描边大小
      //           font: {
      //             //文字设置
      //             size: 10, //文字大小
      //             weight: "bold", //文字粗细 normal bold
      //           },
      //         },
      //         tooltip: {
      //           //鼠标经过点的弹窗
      //           showHtml: "<div>哈哈哈哈</div>", // 如果有html优先使用自定义html的内容
      //           show: true, //是否显示
      //           height: 100, //弹窗高度
      //           width: 200, //弹窗宽度
      //           style: {}, //弹窗样式
      //           title: {
      //             // 弹窗标题
      //             show: true, //是否显示标题
      //             text: "标题", //标题
      //             style: {
      //               //标题样式
      //               fontSize: "16px",
      //               color: "#ffffff",
      //             },
      //           },
      //           info: {
      //             //弹窗详情
      //             show: false, //是否显示
      //             text: "详情详情详情详情", //详情
      //             style: {
      //               //详情样式
      //               fontSize: "12px",
      //               color: "#ff0000",
      //             },
      //           },
      //         },
      //         noPopup: true, //判断点击这个点 是否弹窗 不设置默认弹窗
      //         title: "测试1", //标题
      //         url: "http://www.baidu.com", //iframe内嵌URL
      //         frameWidth: 500, //宽度
      //         frameHeight: 400, //高度
      //       },
      //     },
      //     {
      //       latitude: 36.86034316198399,
      //       longitude: 117.95579526047582,
      //       attributes: {
      //         title: "测试2",
      //         url: "http://10.168.77.155:8081/",
      //         frameWidth: 500,
      //         frameHeight: 400,
      //       },
      //     },
      //   ],
      // },
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
    // this.gisMapCreated();
  },
  mounted() {},
  methods: {
    // gisMapCreated() {
    //   // 打点
    //   const id = "003";
    //   console.log(this.$refs.gisMap, "this.$refs.gisMap");
    //   this.$refs.gisMap.addPointArr(this.opts, id);
    //   // 清除摄像机图层
    //   this.$refs.gisMap.layerOnOff(videoLayer, false);
    // },

    /**
     * 地图加载完成
     */
    // mapCreated() {
    //   // this.$refs.gisMap.layerOnOff("trafficJamGzLayer", true);
    //   this.$refs.gisMap.layerOnOff("videoLayer", false);
    //   // this.$refs.gisMap.layerOnOff("jqvideoLayer", false);
    // },
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