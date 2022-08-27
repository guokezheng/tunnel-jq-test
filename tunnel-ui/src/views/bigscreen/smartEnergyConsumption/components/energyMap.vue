<template lang="html">
  <div id="container"></div>
</template>

<script>
export default {
  name: "AMap",
  props: {
    placeDate: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      map: null,
      polygon: null,
      marker: null,
      lng: "",
      lat: "",
      markers: [], // marker实例
      loopClick: null, // 定时器
      loopIndex: 0,
      infoWindow: null,
      infoWindowPosition: {}, // 信息窗坐标
    };
  },
  mounted() {
      console.log(this.placeDate,"地图")
    //   页面加载完,开始异步引入高德地图
    //创建了一个回调函数,高德地图加载完毕会调用
    this.initNetTick();
  },
  methods: {
    initNetTick() {
      this.$nextTick(() => {
        this.initmap();
        this.init1(this.placeDate);
        this.initMarker();
        // 开启轮播
          this.markers[0].setIcon(
          "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png"
          );
          this.markers[0].setzIndex(101);
          this.openWindows(this.markers[0]);
          this.LoopClick();
      });
    },
    initmap() {
      // 所有关于地图的逻辑全部都要写在这个回调里面;
      // 保证高德地图加载完毕;
      var zoom = 8;
      this.placeDate.name == "四川" ? (zoom = 6) : zoom;
      this.map = new AMap.Map("container", {
        center: this.placeDate.centralPoint || [118.549381, 36.382265],
        resizeEnable: true, //是否监控地图容器尺寸变化
        features: ["bg", "road", "point"], //隐藏默认楼块
        // mapStyle: "amap://styles/macaron", //设置地图的显示样式
        mapStyle: "amap://styles/b304d4538f623a3e78fffadc6733b4de",
        // layers: [new AMap.TileLayer.Satellite()],
        zoom: zoom,
      });
      this.map.on("click", this.showInfoClick);
    },
    init1(placeDate) {
      //区域遮盖
      var that = this;
      if (that.polygon) {
        that.map.remove(that.polygon);
      }
      AMap.plugin("AMap.DistrictSearch", function () {
        new AMap.DistrictSearch({
          extensions: "all",
          subdistrict: 1,
          level: placeDate.type,
          // level: "city",
        }).search(placeDate.name, function (status, result) {
          // 外多边形坐标数组和内多边形坐标数组
          var outer = [
            new AMap.LngLat(-360, 90, true),
            new AMap.LngLat(-360, -90, true),
            new AMap.LngLat(360, -90, true),
            new AMap.LngLat(360, 90, true),
          ];
          var holes = result.districtList[0].boundaries;
          var pathArray = [outer];
          pathArray.push.apply(pathArray, holes);
          that.polygon = new AMap.Polygon({
            pathL: pathArray,
            // strokeColor: "#3FB8ED", //城市边界颜色
            strokeColor: "#09BDEF",
            strokeWeight: 3,
            fillColor: "#040F4E", // 遮罩背景色黑色
            fillOpacity: 1,
          });
          that.polygon.setPath(pathArray);
          that.map.add(that.polygon);
        });
      });
    },
    // 点击地图
    showInfoClick(e) {
      this.resetMarkers();
      this.infoWindow.close(this.map, [this.infoWindowPosition.lng, this.infoWindowPosition.lat])
      var text =
        "您在 [ " +
        e.lnglat.getLng() +
        "," +
        e.lnglat.getLat() +
        " ] 的位置单击了地图！";
      this.lng = e.lnglat.getLng();
      this.lat = e.lnglat.getLat();
      console.log(text);
    },
    // 初始化地图上的图标
    initMarker() {
      this.markers = [];
      this.placeDate.markersList.forEach((marker) => {
        var mark = new AMap.Marker({
          map: this.map,
          icon: marker.icon,
          title: marker.title,
          raiseOnDrag: true,
          extData: marker.extData,
          position: [marker.position[0], marker.position[1]],
          offset: new AMap.Pixel(-13, -30),
          // content:'<div>'+marker.title+'</div><div>'+[marker.position[0], marker.position[1]]+'</div>'
        });
        mark.on("click", this.showInfoM);
        this.markers.push(mark);
      });
    },
    infoOpen(e) {
      // this.marker.setContent(e.target.content);
      // this.marker.open(this.map, e.target.getPosition());
    },
    // 点击图标
    showInfoM(e) {
      // 每次点击坐标 重置所有图标颜色
      this.resetMarkers();
      // 清空定时器
      clearInterval(this.loopClick);
      this.markers.forEach((item, index) => {
        if (
          item.w.position.lng == e.target.w.position.lng &&
          item.w.position.lat == e.target.w.position.lat
        ) {
          this.loopIndex = index;
        }
      });
      
      // 点击坐标 更换坐标颜色为红色
      e.target.setIcon(
        "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png"
      );
      // 点击坐标 更换坐标层级
      e.target.setzIndex(101);
      this.openWindows(e.target);

      // 点击坐标 切换视频
      this.$emit("changeVideo", e.target.w.extData);

      // 开启定时器
      this.LoopClick();
    },
    openWindows(e) {
      var header = "<div style='background:rgba(2,19,88,0.8);padding:10px;border:solid 1px #04B4E2;" +
          "border-radius:10px;font-size:0.8vw;color:#04B4E2'>" 
      var footer = "</div>"
      var contert;
      var title = "<div>"+ e.w.title + "</div>"
      var coordinates = "<div>经纬度：" + e.w.position.lng + "/" + e.w.position.lat + "</div>"
      var tunnelLength = e.w.extData.tunnelLength==null ? '' : "<div>隧道长度：" + e.w.extData.tunnelLength + "</div>"
      var affiliation = e.w.extData.affiliation==null ? '' : "<div>隧道所属：" + e.w.extData.affiliation + "</div>"
      // 内容
      contert = header + title + coordinates + tunnelLength + affiliation + footer
      // 点击弹窗
      this.infoWindow = new AMap.InfoWindow({
        isCustom: true,
        anchor: "top-left",
        content: contert
      });
      this.infoWindowPosition = {
        lng: e.w.position.lng,
        lat: e.w.position.lat,
      }
      this.infoWindow.open(this.map, [e.w.position.lng, e.w.position.lat]);
    },
    // 重置图标颜色
    resetMarkers() {
      this.markers.forEach((marker) => {
        marker.setzIndex(100);
        marker.setIcon(
          "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png"
        );
      });
    },
    // 设置打点坐标
    setMarkersList(val) {
      if (val) {
        this.placeDate.markersList = val;
      }
    },
    // 清除 marker
    clearMarker() {
      if (this.markers) {
        this.markers.forEach((marker) => {
          marker.setMap(null);
          marker = null;
        });
      }
    },
    // 轮播事件
    LoopClick() {
      // 点击坐标 切换视频
      if (this.placeDate.markersList.length == 1) {
        clearInterval(this.loopClick);
        return;
      } else {
        this.loopClick = setInterval(() => {
          this.resetMarkers();
          if (this.loopIndex >= this.markers.length - 1) {
            this.loopIndex = 0;
          } else {
            this.loopIndex++;
          }
          let marker = this.markers[this.loopIndex];
          this.map.setCenter([marker.w.position.lng, marker.w.position.lat]);
          marker.setzIndex(101);
          marker.setIcon(
            "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png"
          );
          this.openWindows(marker);
          // 点击坐标 切换视频
          this.$emit("changeVideo", marker.w.extData);
        }, 6000);
      }
    },
  },
};
</script>

<style lang="less" scoped>
#container {
  width: 100%;
  height: 100%;
  /deep/ .amap-logo {
    display: none;
    opacity: 0 !important;
  }
  /deep/ .amap-copyright {
    opacity: 0;
  }
  /deep/ .amap-icon {
    img {
      width: 34px;
    }
  }
  /deep/ .amap-maps {
    .amap-layers {
      canvas {
        height: auto !important;
      }
    }
  }
}
</style>
