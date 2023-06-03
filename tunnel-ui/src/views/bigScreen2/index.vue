<template>
  <div>
    <gisMap
      id="gisMap"
      ref="gisMap"
      :typeName="typeName"
      :themeName="themeName"
      :roadName="roadName"
      :organizationId="organizationId"
      :mapZoom="mapZoom"
      @mapCreated="mapCreated"
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
      organizationId: "",
      gisMap: null,
      mapZoom: 10.5,

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
      xyList: [
        {
          latitude: 36.62769254, //纬度
          longitude: 117.551764, //经度
          tunnelName: "马家峪隧道",
          tunnelId: "JQ-JiNan-WenZuBei-MJY",
        },
        {
          latitude: 36.516925, //纬度
          longitude: 118.776104, //经度
          tunnelName: "杭山东隧道",
          tunnelId: "JQ-WeiFang-JiuLongYu-HSD",
        },
        {
          latitude: 36.57898729, //纬度
          longitude: 118.4532739, //经度
          tunnelName: "金家楼隧道",
          tunnelId: "JQ-WeiFang-JiuLongYu-JJL",
        },
        {
          latitude: 36.58407113, //纬度
          longitude: 118.395356, //经度
          tunnelName: "马鞍山隧道",
          tunnelId: "JQ-WeiFang-JiuLongYu-MAS",
        },
        {
          latitude: 36.59284668, //纬度
          longitude: 118.2914069, //经度
          tunnelName: "北甲峪隧道",
          tunnelId: "JQ-WeiFang-MiaoZi-BJY",
        },
        {
          latitude: 36.59183014, //纬度
          longitude: 118.2285395, //经度
          tunnelName: "万昌溜隧道",
          tunnelId: "JQ-WeiFang-MiaoZi-WCL",
        },
        {
          latitude: 36.59184438, //纬度
          longitude: 118.3687687, //经度
          tunnelName: "双子山隧道",
          tunnelId: "JQ-WeiFang-YangTianShan-SZS",
        },
        {
          latitude: 36.60063471, //纬度
          longitude: 118.0830504, //经度
          tunnelName: "盘顶山隧道",
          tunnelId: "JQ-ZiBo-TaiHe-PDS",
        },
        {
          latitude: 36.60977357, //纬度
          longitude: 118.0441253, //经度
          tunnelName: "青风岭隧道",
          tunnelId: "JQ-ZiBo-TaiHe-QFL",
        },
        {
          latitude: 36.58971091, //纬度
          longitude: 118.3524619, //经度
          tunnelName: "仰天山隧道",
          tunnelId: "JQ-WeiFang-YangTianShan-YTS",
        },
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
      console.log("地图准备好了");
      // 清除自带的收费站图层
      // this.$refs.gisMap.layerOnOff("stationLayerJQ", false);
      // this.$refs.gisMap.addReductionPointArr(this.opts, this.fields);
      this.addPointByTunnel();
    },
    addPointByTunnel() {
      const layerId = "threme-tunnel";
      this.$refs.gisMap.closeAllLayer();
      //处理数据
      const xyArr = [];
      this.xyList.forEach((item) => {
        let title = item.tunnelName;
        let url = window.origin + "/#/tunnelDialog";
        let icon = require("@/assets/image/地标.png");
        url += "?tunnelId=" + item.tunnelId;
        xyArr.push({
          icon,
          iconwidth: 30,
          iconHeight: 39,
          // 点信息
          latitude: item.latitude, // 纬度
          longitude: item.longitude, // 经度
          attributes: {
            noPopup: 0,
            url,
            title,
            framewidth: 400, // 宽度
            frameHeight: 400, // 高度
            labelTextInfo: {
              //label设置
              show: false, //是否显示
              text: "文字文字文字文字", //文字内容
              color: "#ffffff", //文字颜色
              haloColor: "#000000", //文字描边颜色
              haloSize: "1px", //文字描边大小
              font: {
                //文字设置
                size: 10, //文字大小
                weight: "bold", //文字粗细 normal bold
              },
            },

            // 鼠标经过点的弹窗
            tooltip: {
              show: true,
              showHtml:
                "<div>"+item.tunnelName+"<br>经度："+ item.longitude +"<br>纬度："+item.latitude+"</div>", // 如果有html优先使用自定义html的内容
              height: 80, //弹窗高度
              width: 160, //弹窗宽度
              title: {
                // 弹窗标题
                show: true, //是否显示标题
                text: "标题", //标题
                style: {
                  //标题样式
                  fontSize: "16px",
                  color: "#ffffff",
                },
              },
              info: {
                //弹窗详情
                show: false, //是否显示
                text: "详情详情详情详情", //详情
                style: {
                  //详情样式
                  fontSize: "12px",
                  color: "#ff0000",
                },
              },
            },
          },
        });
      });
      const opts = {
        icon: "", // base64位图片
        width: 30, // 图片宽度
        height: 39, // 图片高度
        noviewIn: true,
        xyArr,
      };
      if (!this.gisMap){
        this.gisMap = this.$refs.gisMap || document.getElementByid("gisMap");
      }
      this.gisMap.addPointArr(opts, layerId);
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