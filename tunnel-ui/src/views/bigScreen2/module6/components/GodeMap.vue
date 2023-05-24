<template>
    <div
      id="container"
      v-loading.lock="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="#000"
      element-loading-customClass="loadingClass"
    ></div>
  </template>
  <script src="//webapi.amap.com/ui/1.1/main.js?v=1.1.1"></script>
  <script>
//   window._AMapSecurityConfig = {
//   securityJsCode: 'b66f3507e271a2c351b28d49cbf1eb57'
// }
  export default {
    name: "AMap",
    
    data() {
      return {
        placeDate: {
        name: "山东",
        type: "province", // province代表省份, city代表城市
        centralPoint: [118.549381, 36.382265], // 中心点坐标
        incidentVal: 13, // 事件
        earlyWarningVal: 23, // 预警
        malfunctionVal: 10, // 故障
        markersList: [
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "姚家峪隧道",
            position: [117.822164, 36.493427],
            extData: {
              name: "姚家峪隧道",
              id: 0,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [240, 230, 250, 260, 255, 245],
                }, // co浓度
                trafficData: {
                  data: [22020, 25050, 23500, 25300, 26030, 24500],
                }, // 车流量
                windData: {
                  data: [6, 7, 8, 6, 8, 7],
                }, // 风速
                luminanceData: {
                  data: [5200, 4800, 5100, 5500, 4500, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "姚家峪隧道",
                  data: [544, 765, 834, 788, 734, 697],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "滨莱高速",
            position: [117.784441, 36.32409],
            extData: {
              name: "滨莱高速",
              id: 1,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [220, 210, 240, 250, 235, 265],
                }, // co浓度
                trafficData: {
                  data: [23020, 20050, 25500, 22300, 22030, 24500],
                }, // 车流量
                windData: {
                  data: [8, 6, 7, 7, 6, 7],
                }, // 风速
                luminanceData: {
                  data: [5500, 4500, 4500, 5500, 4500, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "滨莱高速",
                  data: [632, 765, 508, 722, 766, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 96, // 正常
                  malfunctionVal: 1, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "中庄隧道",
            position: [121.435835, 37.079518],
            extData: {
              name: "中庄隧道",
              id: 2,
              tunnelLength: null,
              affiliation: "荣潍高速",
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [12020, 15050, 13500, 15300, 16030, 14500],
                }, // 车流量
                windData: {
                  data: [7, 7, 8, 6, 7, 7],
                }, // 风速
                luminanceData: {
                  data: [4000, 4500, 4500, 5500, 4500, 5500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "中庄隧道",
                  data: [723, 786, 664, 698, 743, 631],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 4,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "马公祠隧道",
            position: [117.824197, 36.472199],
            extData: {
              name: "马公祠隧道",
              id: 3,
              tunnelLength: null,
              affiliation: null,
              echartsDate: {
                COData: {
                  data: [240, 240, 250, 250, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [12420, 15650, 13800, 15500, 15030, 14800],
                }, // 车流量
                windData: {
                  data: [7, 6, 8, 6, 6, 7],
                }, // 风速
                luminanceData: {
                  data: [4800, 5500, 4700, 5200, 4600, 5000],
                }, // 洞口亮度
                energyConsumption: {
                  name: "马公祠隧道",
                  data: [745, 813, 608, 824, 713, 698],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 94, // 正常
                  malfunctionVal: 3, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "乐疃隧道",
            position: [117.821815, 36.457601],
            extData: {
              name: "乐疃隧道",
              id: 4,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [230, 250, 240, 245, 265, 255],
                }, // co浓度
                trafficData: {
                  data: [20320, 25050, 23500, 25800, 25030, 24000],
                }, // 车流量
                windData: {
                  data: [7, 6, 6, 6, 7, 7],
                }, // 风速
                luminanceData: {
                  data: [4600, 5300, 5500, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "乐疃隧道",
                  data: [832, 743, 786, 686, 724, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 93, // 正常
                  malfunctionVal: 4, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "樵岭前隧道",
            position: [117.817382, 36.448935],
            extData: {
              name: "樵岭前隧道",
              id: 5,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [223, 223, 245, 236, 264, 273],
                }, // co浓度
                trafficData: {
                  data: [23340, 25250, 23340, 22300, 26240, 25200],
                }, // 车流量
                windData: {
                  data: [5, 6, 5, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [3200, 4200, 5500, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "樵岭前隧道",
                  data: [634, 786, 608, 624, 736, 618],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 92, // 正常
                  malfunctionVal: 5, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "佛羊岭隧道",
            position: [117.800823, 36.374613],
            extData: {
              name: "佛羊岭隧道",
              id: 6,
              tunnelLength: null,
              affiliation: "滨莱高速",
              echartsDate: {
                COData: {
                  data: [252, 235, 262, 246, 226, 273],
                }, // co浓度
                trafficData: {
                  data: [26230, 24220, 24630, 26530, 25240, 25200],
                }, // 车流量
                windData: {
                  data: [5, 6, 7, 6, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [5200, 5200, 5000, 4200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "佛羊岭隧道",
                  data: [642, 824, 646, 822, 796, 625],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 91, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "洪河隧道",
            position: [117.794317, 35.745686],
            extData: {
              name: "洪河隧道",
              id: 7,
              tunnelLength: "515米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [22330, 25220, 22530, 20100, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 6, 7, 7, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [4200, 4800, 5300, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "洪河隧道",
                  data: [756, 835, 676, 612, 798, 634],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 90, // 正常
                  malfunctionVal: 7, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "迎春坡隧道",
            position: [117.653719, 35.329564],
            extData: {
              name: "迎春坡隧道",
              id: 8,
              tunnelLength: "973米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [22430, 23420, 23530, 22300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 8, 7, 6, 6, 8],
                }, // 风速
                luminanceData: {
                  data: [3400, 4800, 5300, 5200, 4900, 5200],
                }, // 洞口亮度
                energyConsumption: {
                  name: "迎春坡隧道",
                  data: [724, 826, 755, 797, 726, 694],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 8, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "毓秀山隧道",
            position: [117.597113, 35.250696],
            extData: {
              name: "毓秀山隧道",
              id: 9,
              tunnelLength: "400米",
              affiliation: "滨台高速",
              echartsDate: {
                COData: {
                  data: [245, 255, 262, 256, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [24430, 25420, 23530, 23300, 25570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [4400, 4800, 5300, 5200, 4900, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "毓秀山隧道",
                  data: [735, 836, 635, 635, 735, 624],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 88, // 正常
                  malfunctionVal: 9, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "望海石隧道",
            position: [117.524916, 35.021412],
            extData: {
              name: "望海石隧道",
              id: 10,
              tunnelLength: "2145米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [245, 265, 252, 246, 236, 273],
                }, // co浓度
                trafficData: {
                  data: [23430, 25420, 24530, 24300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 7, 6, 7, 8],
                }, // 风速
                luminanceData: {
                  data: [4400, 5100, 5300, 4700, 4900, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "望海石隧道",
                  data: [735, 735, 545, 675, 724, 687],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 87, // 正常
                  malfunctionVal: 10, // 故障
                  offlineVal: 3,
                }, // 设备
              },
            },
          },
          {
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            title: "龙山寨隧道",
            position: [117.53021, 35.005675],
            extData: {
              name: "龙山寨隧道",
              id: 11,
              tunnelLength: "515米",
              affiliation: "新台高速",
              echartsDate: {
                COData: {
                  data: [265, 245, 252, 246, 256, 273],
                }, // co浓度
                trafficData: {
                  data: [25430, 25420, 23530, 22300, 23570, 25200],
                }, // 车流量
                windData: {
                  data: [6, 7, 6, 6, 7, 5],
                }, // 风速
                luminanceData: {
                  data: [5400, 5100, 5300, 4700, 5100, 4500],
                }, // 洞口亮度
                energyConsumption: {
                  name: "龙山寨隧道",
                  data: [724, 853, 524, 609, 825, 683],
                }, // 年度能耗
                equipmentData: {
                  normalVal: 89, // 正常
                  malfunctionVal: 6, // 故障
                  offlineVal: 5,
                }, // 设备
              },
            },
          },
        ],
      },
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
        redIcon: require("@/assets/image/poi-marker-red.png"),
        defaultIcon: require("@/assets/image/poi-marker-default.png"),
      };
    },
    created() {
      if (this.$cache.session.get("LOOPINDEX"))
        this.loopIndex = this.$cache.session.get("LOOPINDEX");
    },
    mounted() {
      //   页面加载完,开始异步引入高德地图
      //创建了一个回调函数,高德地图加载完毕会调用
      this.initNetTick();
    },
    methods: {
      async initNetTick() {
        this.clearTimeouts();
        this.loading = true;
        this.$modal.loading();
        await this.$nextTick(() => {
          this.initmap();
          this.init1(this.placeDate);
          this.initMarker();
          // 开启轮播
          var a = 0;
          if (this.loopIndex != null) a = this.loopIndex;
          this.markers[a].setIcon(this.redIcon);
          this.markers[a].setzIndex(101);
          this.openWindows(this.markers[a]);
        //   if (!this.userRight) {
        //     this.$emit("changeVideo", this.markers[a].w.extData);
        //   }
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
        // 创建地图
        this.map = new AMap.Map("container", {
          skyColor: "#000",
        //   mask: mask,
          center: this.placeDate.centralPoint ,
          resizeEnable: true, //是否监控地图容器尺寸变化
          features: ["bg", "road", "point"], //隐藏默认楼块
        //   mapStyle: "amap://styles/macaron", //设置地图的显示样式
        //   mapStyle: "amap://styles/8ffdb946a55f6ea221e481359037bee8",
        //   mapStyle: "amap://styles/b304d4538f623a3e78fffadc6733b4de",//王登顺地图

          mapStyle: 'amap://styles/darkblue',
          // layers: [new AMap.TileLayer.Satellite()],
          zoom: 9,
          pitch: 50, // 地图俯仰角度，有效范围 0 度- 83 度
          dragEnable: true,
          zoomEnable: true,
          doubleClickZoom: true,
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
            level: that.placeDate.type,
            // level: "city",
          }).search(placeDate.name, function (status, result) {
            var holes = result.districtList[0].boundaries;
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
                strokeColor: "#000",
                strokeWeight: 3,
                fillColor: "#000", // 遮罩背景色黑色
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
            this.saveLoopIndex(index);
          }
        });
        // if (this.userRight) {
        //   return this.$emit("updateUserRight", marker);
        // }
        // 点击坐标 更换坐标颜色为红色
        e.target.setIcon(this.redIcon);
        // 点击坐标 更换坐标层级
        e.target.setzIndex(101);
        this.openWindows(e.target);
  
        // 点击坐标 切换视频
        if (this.placeDate.markersList.length != 1) {
          this.$emit("changeVideo", marker.extData);
        }
  
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
          marker.setIcon(this.defaultIcon);
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
      saveLoopIndex(i) {
        this.loopIndex = i;
        this.$cache.session.set("LOOPINDEX", i);
      },
      // 删除保存的图标的序号
      delLoopIndex() {
        this.$cache.session.remove("LOOPINDEX");
        this.loopIndex = 0;
      },
      // 轮播事件
      LoopClick() {
        // 点击坐标 切换视频
        if (this.placeDate.markersList.length == 1) {
          this.clearTimeouts();
          return;
        } else {
          this.loopClick = setInterval(() => {
            this.resetMarkers();
            if (this.loopIndex >= this.markers.length - 1) {
              this.loopIndex = 0;
            } else {
              this.loopIndex++;
            }
            this.saveLoopIndex(this.loopIndex);
            let marker = this.markers[this.loopIndex];
            this.map.setCenter([marker.w.position.lng, marker.w.position.lat]);
            marker.setzIndex(101);
            marker.setIcon(this.redIcon);
            this.openWindows(marker);
            // 点击坐标 切换视频
            // if (!this.userRight) {
            //   this.$emit("changeVideo", marker.w.extData);
            // }
          }, 6000);
        }
      },
    },
    beforeDestroy() {
      clearTimeout(this.loopClick);
    },
  };
  </script>
  
  <style lang="less" scoped>
  #container {
    width: 100%;
    height: calc(100% - 72px);
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
  