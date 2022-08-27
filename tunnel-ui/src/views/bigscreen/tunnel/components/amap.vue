<template>
  <div
    id="container"
    v-loading.lock="loading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="#040f4e"
    element-loading-customClass="loadingClass"
  ></div>
</template>

<script>
export default {
  name: "AMap",
  props: {
    placeDate: {
      type: Object,
      required: true,
    },
    userRight: {
      type: Boolean,
      required: true,
    },
    viewMode: {
      type: String,
      default: "3D",
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
      loopIndex: null,
      infoWindow: null,
      infoWindowPosition: {}, // 信息窗坐标
      loading: false,
      redIcon: require('@/assets/image/poi-marker-red.png'),
      defaultIcon: require('@/assets/image/poi-marker-default.png'),
    };
  },
  created() {
    if(this.$cache.session.get('LOOPINDEX')) this.loopIndex = this.$cache.session.get('LOOPINDEX')
  },
  mounted() {
    //   页面加载完,开始异步引入高德地图
    //创建了一个回调函数,高德地图加载完毕会调用
    this.initNetTick();
  },
  methods: {
    async initNetTick() {
      this.clearTimeouts()
      this.loading = true;
      this.$modal.loading();
      await this.$nextTick(() => {
        this.initmap();
        this.init1(this.placeDate);
        this.initMarker();
        // 开启轮播
        var a = 0;
        if (this.loopIndex != null) a = this.loopIndex
        this.markers[a].setIcon(
          this.redIcon
        );
        this.markers[a].setzIndex(101);
        this.openWindows(this.markers[a]);
        if(!this.userRight) {
          this.$emit("changeVideo", this.markers[a].w.extData);
        }
        this.LoopClick();
      });
      this.$modal.closeLoading();
      setTimeout(() => {
        this.loading = false;
      }, 2000);
    },
    initmap() {
      var that = this;
      // 所有关于地图的逻辑全部都要写在这个回调里面;
      // 保证高德地图加载完毕;
      var zoom = 7;
      if (this.userRight) {
        if (this.placeDate.name == "山东") zoom = 8;
        if (this.placeDate.name == "四川") zoom = 7;
      } else {
        this.placeDate.name == "四川" ? (zoom = 6) : zoom;
      }
      if (this.viewMode == "3D") {
        var mask = [];
        var opts = {
          subdistrict: 1,
          extensions: "all",
          // level: 'province'
          level: this.placeDate.type,
        };
        AMap.plugin("AMap.DistrictSearch", () => {
          var district = new AMap.DistrictSearch(opts);
          district.search(this.placeDate.name, function (status, result) {
            var bounds = result.districtList[0].boundaries;
            for (var i = 0; i < bounds.length; i += 1) {
              mask.push([bounds[i]]);
            }
          });
        });
      }
      this.map = new AMap.Map("container", {
        skyColor: "#040f4e",
        mask: mask,
        center: this.placeDate.centralPoint || [118.549381, 36.382265],
        resizeEnable: true, //是否监控地图容器尺寸变化
        features: ["bg", "road", "point"], //隐藏默认楼块
        // mapStyle: "amap://styles/macaron", //设置地图的显示样式
        mapStyle: "amap://styles/b304d4538f623a3e78fffadc6733b4de",
        // mapStyle: 'amap://styles/e917957d149773350451cec75db378d1',
        // layers: [new AMap.TileLayer.Satellite()],
        zoom: zoom,
        pitch: 50, // 地图俯仰角度，有效范围 0 度- 83 度
        viewMode: that.viewMode, // 地图模式
        dragEnable: true,
        zoomEnable: true,
        doubleClickZoom: true,
      });
      if(this.viewMode == '3D') {
      AMap.service(
          ["AMap.PlaceSearch", "AMap.Autocomplete", "AMap.Driving"],
          function () {
            AMapUI.loadUI(
              ["geo/DistrictExplorer"],
              function (DistrictExplorer) {
                //创建一个实例
                var districtExplorer = new DistrictExplorer({
                  eventSupport: true,
                  map: that.map,
                });

                function renderAreaNode(areaNode) {
                  if ([370000].indexOf(areaNode.getAdcode()) >= 0) {
                    //绘制子区域
                    districtExplorer.renderSubFeatures(
                      areaNode,
                      function (feature, i) {
                        return {
                          cursor: "default",
                          bubble: true,
                          strokeColor: "#00ffff", //线颜色 '#00ffff'
                          strokeOpacity: 1, //线透明度
                          strokeWeight: 1, //线宽
                          fillColor: "#00baff", //填充色
                          fillOpacity: 0, //填充透明度
                        };
                      }
                    );
                  }

                  //绘制父区域
                  districtExplorer.renderParentFeature(areaNode, {
                    cursor: "default",
                    bubble: true,
                    strokeColor: "#00ffff", //线颜色
                    strokeOpacity: 1, //线透明度
                    strokeWeight: 3, //线宽
                    fillColor: "#0090ff", //填充色
                    fillOpacity: 0.4, //填充透明度
                  });
                }

                var adcodes = [
                  370000, //山东
                ];

                districtExplorer.loadMultiAreaNodes(
                  adcodes,
                  function (error, areaNodes) {
                    //设置定位节点，支持鼠标位置识别
                    //注意节点的顺序，前面的高优先级
                    districtExplorer.setAreaNodesForLocating(areaNodes);

                    //清除已有的绘制内容
                    districtExplorer.clearFeaturePolygons();
                    if (areaNodes.length) {
                      for (var i = 0, len = areaNodes.length; i < len; i++) {
                        renderAreaNode(areaNodes[i]);
                      }
                    }
                    //更新地图视野
                    // that.map.setFitView(districtExplorer.getAllFeaturePolygons());
                  }
                );
              }
            );
          }
        );
      }
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
          level: that.placeDate.type,
          // level: "city",
        }).search(placeDate.name, function (status, result) {
          var holes = result.districtList[0].boundaries;
          if (that.viewMode == "2D") {
            // 外多边形坐标数组和内多边形坐标数组
            var outer = [
              new AMap.LngLat(-360, 90, true),
              new AMap.LngLat(-360, -90, true),
              new AMap.LngLat(360, -90, true),
              new AMap.LngLat(360, 90, true),
            ];
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
          } else {
            //添加描边
            for (let i = 0; i < holes.length; i += 1) {
              // eslint-disable-next-line no-undef
              new AMap.Polyline({
                path: holes[i],
                strokeColor: "#99FFFF",
                strokeWeight: 5,
                strokeOpacity: 0.9,
                map: that.map,
              });
            }
            var object3Dlayer = new AMap.Object3DLayer({ zIndex: 1 });
            that.map.add(object3Dlayer);
            var height = -108000;
            var color = "#0088ffcc"; //rgba
            var wall = new AMap.Object3D.Wall({
              path: holes,
              height: height,
              color: color,
            });
            wall.transparent = true;
            object3Dlayer.add(wall);
          }
        });
      });
    },
    // 点击地图
    showInfoClick(e) {
      this.resetMarkers();
      this.infoWindow.close(this.map, [
        this.infoWindowPosition.lng,
        this.infoWindowPosition.lat,
      ]);
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
          icon: this.defaultIcon,
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
      var marker = e.target.w;
      // 每次点击坐标 重置所有图标颜色
      this.resetMarkers();
      // 清空定时器
      clearInterval(this.loopClick);
      this.markers.forEach((item, index) => {
        if (
          item.w.position.lng == marker.position.lng &&
          item.w.position.lat == marker.position.lat
        ) {
          this.saveLoopIndex(index)
        }
      });
      if (this.userRight) {
        return this.$emit("updateUserRight", marker);
      }
      // 点击坐标 更换坐标颜色为红色
      e.target.setIcon(
        this.redIcon
      );
      // 点击坐标 更换坐标层级
      e.target.setzIndex(101);
      this.openWindows(e.target);

      // 点击坐标 切换视频
      if (this.placeDate.markersList.length != 1) {
        this.$emit("changeVideo", marker.extData);
      };

      // 开启定时器
      this.LoopClick();
    },
    // 图标的自定义弹窗
    openWindows(e) {
      var marker = e.w;
      var header =
        "<div style='background:rgba(2,19,88,0.8);padding:10px;border:solid 1px #04B4E2;" +
        "border-radius:10px'>";
      var footer = "</div>";
      var contert;
      var title = "<div>" + marker.title + "</div>";
      var coordinates =
        "<div>经纬度：" +
        marker.position.lng +
        "/" +
        marker.position.lat +
        "</div>";
      var tunnelLength =
        marker.extData.tunnelLength == null
          ? ""
          : "<div>隧道长度：" + marker.extData.tunnelLength + "</div>";
      var affiliation =
        marker.extData.affiliation == null
          ? ""
          : "<div>隧道所属：" + marker.extData.affiliation + "</div>";
      // 内容
      contert =
        header + title + coordinates + tunnelLength + affiliation + footer;
      // 点击弹窗
      this.infoWindow = new AMap.InfoWindow({
        isCustom: true,
        anchor: "top-left",
        content: contert,
      });
      this.infoWindowPosition = {
        lng: marker.position.lng,
        lat: marker.position.lat,
      };
      this.infoWindow.open(this.map, [
        marker.position.lng,
        marker.position.lat,
      ]);
    },
    // 重置图标颜色
    resetMarkers() {
      this.markers.forEach((marker) => {
        marker.setzIndex(100);
        marker.setIcon(
          this.defaultIcon
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
    // 清除定时器
    clearTimeouts() {
      if (this.loopClick) {
        clearInterval(this.loopClick);
      }
    },
    // 保存当前点击图标的序号
    saveLoopIndex(i){
      this.loopIndex = i
      this.$cache.session.set('LOOPINDEX',i)
    },
    // 删除保存的图标的序号
    delLoopIndex() {
      this.$cache.session.remove('LOOPINDEX')
      this.loopIndex = 0
    },
    // 轮播事件
    LoopClick() {
      // 点击坐标 切换视频
      if (this.placeDate.markersList.length == 1) {
        this.clearTimeouts()
        return;
      } else {
        this.loopClick = setInterval(() => {
          this.resetMarkers();
          if (this.loopIndex >= this.markers.length - 1) {
            this.loopIndex = 0;
          } else {
            this.loopIndex++;
          }
          this.saveLoopIndex(this.loopIndex)
          let marker = this.markers[this.loopIndex];
          this.map.setCenter([marker.w.position.lng, marker.w.position.lat]);
          marker.setzIndex(101);
          marker.setIcon(
            this.redIcon
          );
          this.openWindows(marker);
          // 点击坐标 切换视频
          if (!this.userRight) {
            this.$emit("changeVideo", marker.w.extData);
          }
        }, 6000);
      }
    },
  },
  beforeDestroy() {
    clearTimeout(this.loopClick)
  }
};
</script>

<style lang="less" scoped>
#container {
  width: 100%;
  height: 100%;
  color: #09bdef;
  font-size: 0.8vw;
  // background: url("~@/assets/Example/earth.png");
  background: #040f4e !important;

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
    .amap-markers {
      .amap-icon {
        cursor: pointer;
      }
    }
  }
}
</style>
