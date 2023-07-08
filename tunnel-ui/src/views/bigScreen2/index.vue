<template>
  <div>
    <div style="height: 72px">
      <img
        :src="src"
        class="headerLinearBg"
      />
      <div class="bpp-topNav">
        <div class="leftNav">
          <div
            v-for="(item, index) in tabList.slice(0, 4)"
            :key="index"
            class="navButton"
            @click="changeNavButton(item.inx)"
          >
            {{ item.title }}
          </div>
        </div>
        <div class="leftNav rightNav">
          <div
            v-for="(item, index) in tabList.slice(4, 8)"
            :key="index"
            class="navButton"
            @click="changeNavButton(item.inx)"
          >
            {{ item.title }}
          </div>
        </div>
        <div class="diaodu">
          <i class="el-icon-bell"></i>
          集团调度
        </div>
      </div>
    </div>
    <gisMap
      id="gisMap"
      ref="gisMap"
      :typeName="typeName"
      :themeName="themeName"
      :roadName="roadName"
      :organizationId="organizationId"
      :mapZoom="mapZoom"
      :checkBoxBottom="checkBoxBottom"
      :checkBoxLeft="checkBoxLeft"
      @mapCreated="mapCreated"
      @openTunnelInfo="openTunnelInfo"
      style="height: calc(100% - 72px) !important; z-index: 2;position: absolute;top: 72px;"
    ></gisMap>
    <!-- <div style="width:100%;height:100%;background-color: #000;"></div> -->
    <bigScreen v-if="bigScreenList[0].type" class="charts" />
    <bigScreen2 v-if="bigScreenList[1].type" class="charts" />
    <bigScreen3 v-if="bigScreenList[2].type" class="charts" />
    <bigScreen4 v-if="bigScreenList[3].type" class="charts" />
    <!-- <bigScreen5  v-if="bigScreenList[0].type" class="charts"/> -->
    <bigScreen6 v-if="bigScreenList[4].type" class="charts" />
    <div class="bottomRoad">
      <div class="bottomRoad-main">
        <div class="box">
          <div class="imgBox">
            <img :src="logo"/>
          </div>
          <el-dropdown placement="top" @command="handleCommand"  :hide-on-click="false">
            <el-button type="primary">
              {{ roadName }}<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="1">全省</el-dropdown-item>
              <el-dropdown-item :command="2">济青中线</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
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
      typeName: "danao",
      themeName: "darkTheme",
      roadName: "全省",
      organizationId: "",
      gisMap: null,
      mapZoom: 10.5,
      checkBoxBottom: "2%",
      checkBoxLeft: "25%",
      logo:require("@/assets/logo/zclogo.png"),
      src:require("@/assets/Example/bigScreen/header.png"),
      tabList:[
        {title:'路网管理',inx:0},
        {title:'收费运营',inx:1},
        {title:'服务区',inx:2},
        {title:'智能养护',inx:3},
        {title:'工程建设',inx:4},
        {title:'智慧决策',inx:5},
        {title:'智慧隧道',inx:6},
        {title:'系统管理',inx:7},
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
    openTunnelInfo(){
      console.log(11111)
    },
    changeNavButton(index){
      // $(".navButton")
      //   .eq(index)
      //   .addClass("button_hover")
      //   .siblings()
      //   .removeClass("button_hover");
    },
    // 切换底部左侧 全省/济青中线
    handleCommand(command){
      console.log(command,"command")
      if(command == 2){
        this.roadName = "济青中线"
        this.typeName = "jiqing"
        this.logo = require("@/assets/logo/jiqing.png")
      }else{
        this.roadName = "全省"
        this.typeName = "danao"
        this.logo = require("@/assets/logo/zclogo.png")

      }
      this.$forceUpdate()
    },
    
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
                "<div>" +
                item.tunnelName +
                "<br>经度：" +
                item.longitude +
                "<br>纬度：" +
                item.latitude +
                "</div>", // 如果有html优先使用自定义html的内容
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
      if (!this.gisMap) {
        this.gisMap = this.$refs.gisMap || document.getElementByid("gisMap");
      }
      this.gisMap.addPointArr(opts, layerId);
    },
  },
};
</script>
<style scoped lang="scss">
.button_hover{
  color: rgb(255, 211, 113) !important;
  border-bottom:solid 1px rgb(255, 211, 113) !important;
  
}
.bottomRoad{
  display: inline-block;
  position: absolute;
  z-index: 999;
  left: 20%;
  width: 4.9%;
  overflow: hidden;
  bottom: 2%;
  .bottomRoad-main{
    width: 100%;
    color: #fff;
    .box{
      height: 95px;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      flex-direction: column;
      text-align: center;
      background: #00152b;
      border: 0.0052rem solid #1897e7;
      border-top: 0.0156rem solid #1897e7;
      box-sizing: border-box;
      .imgBox{
        height: 64px;
        flex:1;
        display: flex;
        align-items: center;
        img{
          width:30px;
        }
      }
      .el-dropdown{
        width:100%;
        .el-button{
          width:100%;
          height:.1458rem;
          padding:0;
          display: flex;
          justify-content: space-around;
          align-items: center;
          border-radius:0px;
        }
      }
    }
  }
}
.diaodu{
    position: absolute;
    top: 3.3vh;
    right: 0;
    width:120px;
    color: #fff;
    font-size: 14px;
    i{
      padding-right: 10px;
    }
  }
.bpp-topNav{
    height: 72px;
    background: url(../../assets/Example/bigScreen/topNav.png);
    background-position: center;
    background-repeat: no-repeat;
    position: absolute;
    z-index: 10;
    width: 100%;
    top: 0;
    background-size: 100%;
    .leftNav{
      width:32vw;
      height: 72px;
      display: flex;
      justify-content: space-around;
      .navButton{
        margin-top:3vh;
        color: #fff;
        font-size: 18px;
        cursor: pointer;
        caret-color: rgba(0,0,0,0);
        user-select: none; 
        height: 35px;
      }
      .navButton:first-of-type{
        margin-left: 8vw;
      }
    }
    .rightNav{
      position: absolute;
      right: 0;
      top: 0;
      .navButton:last-of-type{
        margin-right: 8vw;
      }
      .navButton:first-of-type{
        margin-left: 0vw;
      }
    }
    
  }
.headerLinearBg{
  height:18vh;
  width:100%;
  position:absolute;
  z-index: 3;
}
::v-deep .esri-view-surface--inset-outline:focus::after {
  outline: none !important;
}

.charts {
  position: absolute;
  left: 0;
  top: 72px;
  width: 100%;
  height: calc(100% - 72px);
}
.bottomBox {
  height: .12rem;
  position: absolute;
  left: 20%;
  // transform: translateX(-50%);
  top: 72px;
  // margin: 0px auto 30px auto;
  display: flex;
  justify-content: space-between;
  z-index: 3;
  ::v-deep .el-radio-group {
    display: flex;
    .el-radio-button {
      margin-left: .052rem;
    }
    .el-radio-button:first-of-type{
      margin-left: 0px;
    }
    .el-radio-button:last-of-type,.el-radio-button:first-of-type{
      .el-radio-button__inner{
        border-radius:0px !important;
      }
    }
  }
  ::v-deep .el-radio-button--medium .el-radio-button__inner {
    padding: 0 !important;
    width: 130px;
    height: .12rem;
    line-height: .12rem;
    border: none;
    background: linear-gradient(180deg,#1eace8,#0074d4);
    // background: url(../../assets/cloudControl/cardTitle.png);
    // background-repeat: no-repeat;
    // background-size: 100% 100%;
    color: #fff;
    // opacity: 0.6;
  }
  ::v-deep .is-active .el-radio-button__inner {
    background: linear-gradient(180deg,#ffcd48,#fe861e);
  }
}
</style>