import {math} from "@/utils/math";
import {mapState} from "vuex";
import {getJlyTunnel, getTunnels} from "@/api/equipment/tunnel/api";
import {carSwitchType} from "@/api/workbench/config";


/**
 * 小车跑数据js
 */
export const singleCat = {

  methods: {
    //实时车辆开关
    carShowChange(val) {
      let tunnelItems = null;
      if(!!this.tunnelItem){
        tunnelItems = this.tunnelItem
      }else{
        tunnelItems = this.tunnelList[0]
      }
      carSwitchType(tunnelItems.tunnelId, this.carShow ? 0:1).then((res) =>{})
      this.$nextTick(() => {
        this.getEnergyConsumption(this.currentTunnel.id);
      });
      if(this.carShow){
        let that = this
        //小车展示秒表 定时清除不动的车辆
        this.catTime = setInterval(function(){
          console.log( that.catNumber)
          that.catNumber = that.catNumber+1
        },1000)
      }else{
        clearInterval(this.catTime)
      }
      this.carShow = val;
    },
    //通过实际距离计算 物体在页面位置   leftCount==true通过距离的方式计算左右边距
    carBackCount(eventIndex,leftCount){
      //车辆实际距离入口距离
      var carKm = eventIndex.distance;
      //计算
      let imgId =  document.getElementById("config-img-id")
      //上
      let topHigh = 73/580
      //上道
      let centreHighs = 60/580
      //中
      let centreHigh = 57/580
      //下道
      let nextHigh = 73/580
      if(eventIndex.roadDir == 1){//上行
        if(leftCount==true){ //通过距离计算桩号
          //计算最终经度 右边距
          eventIndex.right =
            math.add(math.multiply(+carKm * this.proportion) + 200) + "px";
          eventIndex.left =""
        }else{//通过桩号计算

        }
        //计算经度  上边距
        switch (eventIndex.laneNo) {
          case "3":
            eventIndex.top = imgId.offsetHeight*topHigh+(imgId.offsetHeight*topHigh/3)+ "px";
            break;
          case "2":
            eventIndex.top = imgId.offsetHeight*topHigh+imgId.offsetHeight*centreHighs+(imgId.offsetHeight*centreHighs/3)+ "px";
            break;
          case "1":
            eventIndex.top = imgId.offsetHeight*topHigh+imgId.offsetHeight*centreHighs*2+(imgId.offsetHeight*centreHighs/3)+ "px";
            break;
          default:
        }
      }else if(eventIndex.roadDir==2){//下行
        if(leftCount==true){
          //计算最终经度 左边距
          eventIndex.left =
            math.add(math.multiply(+carKm * this.proportion) + 190) + "px";
          eventIndex.right = ""
        }else{//通过桩号计算

        }

        //计算经度  上边距
        switch (eventIndex.laneNo) {
          case "1":
            eventIndex.top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*3+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
            break;
          case "2":
            eventIndex.top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*4+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
            break;
          case "3":
            eventIndex.top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*5+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
            break;
          default:
        }
      }
      return eventIndex
    },
    //自定义刷新频率
    carSetTimer() {
      if(this.timer == null) {
        this.timer = setInterval( () => {
          if( this.carShow == true){
            this.carShow = false
            this.carShow = true
          }
          // this.$forceUpdate();
        }, 300)
      }
    },
    //计算公里数
    carchange(tunnelId) {
      getTunnels(tunnelId).then((res) => {
        console.log(res.data, "当前隧道信息");
        const tunnel = res.data;
        // math.subtract(a-b)//减
        // math.multiply(a*b)//乘
        // math.divide(a/b)//除
        // 计算公里数
        let Mileage = math.divide(
          math.subtract(+tunnel.endPileNum - +tunnel.startPileNum) / 1000
        );
        this.tunnelKm = Mileage;
        if (Mileage <= 1.3) {
          var length = +1300;
        } else if (Mileage < +2.6) {
          var length = +1300 * 1.5;
        } else if (Mileage > +2.6) {
          var length = +1300 * 2;
        }
        this.lane = tunnel.lane;
        this.tunnelLength = length; //px长度
        this.proportion = math.divide(length / (Mileage * 1000)); //计算px和米的比例
      });
      // 首页获取隧道长度，根据隧道长度判断车辆行驶的全部距离
    },
    //点击爆炸图片打开视频
    CatImageClick(itme){
      this.accidentist =[]
      console.log(itme)
      itme.videoUrl = "https://v2.kwaicdn.com/u|pic/2023/04/24/22/BMjAyMzA0MjQyMjQxMDNfMTQ3MjMxMzI2NF8xMDE0MTQwNzM1ODRfMF8z_b_Bc9e02bc05cd920fb6441b70018da5326.mp4?pkey=AAUTZcZyp0Ig_v4NtpvmOQz31SQkSH5yFlE-p1tJ0Y6q2uRhXFlG-1v94LqA9Qa_lWErv1zEg1EuB35H13zU-reSoKlEGr37ckxsnovw_2mQUq3OgFjGyIJhIqnWLjYjeR0&tag=1-1682478898-unknown-0-dq59nfp47w-da8615b8ba03f216&clientCacheKey=3xgnyc9upjzvdcm_b.mp4&di=3cd05065&bp=10004&tt=b&ss=vp"
      this.accidentist.push(itme)
      //画事故效果视频
      this.accidentDialogVisible = true
      debugger
    }
  },
  watch: {
    //车祸图片显示
    sdSvgEventList(event){
      console.log(event)
      //判断隧道是否和传来的相同  相同显示不同则return
      let tunnelItems = null;
      if(!!this.tunnelItem){
        tunnelItems = this.tunnelItem
      }else{
        tunnelItems = this.tunnelList[0]
      }

      console.log(event, "websockt工作台接收事件弹窗");
      this.carActionExplodeList = []
      for ( let i = 0; i < event.length; i++){
        this.trafficList.unshift(event[i]);
        if(tunnelItems.tunnelId!=event[0].tunnelId){
          continue;
        }
        // switch(event[i].eventTypeId){
        //   case "12": //车祸
        //     // 存储数据
        //     localStorage.setItem('username', 'John Doe');
        //     //通过桩号  计算 事故在页面位置
        //     event[i] = this.carBackCount(event[i],false)
        //     //爆炸效果list
        //     this.carActionExplodeList.push( event[i])
        //     break
        //   case "20"://火灾
        //     break
        //   default:
        // }
        //通过桩号  计算 事故在页面位置
        event[i] = this.carBackCount(event[i],true)
        console.log(event[i] )
        this.carActionExplodeList.push( event[i])
        console.log(this.carActionExplodeList )
      }
    },
    //接收事件视频
    sdEventList(event) {
      console.log(event, "websockt工作台接收事件弹窗");
      this.accidentist = []
      for ( let i = 0; i < event.length; i++){
        this.trafficList.unshift(event[i]);
        //弹出 现场视频
        event[i].videoUrl = "https://v2.kwaicdn.com/u|pic/2023/04/24/22/BMjAyMzA0MjQyMjQxMDNfMTQ3MjMxMzI2NF8xMDE0MTQwNzM1ODRfMF8z_b_Bc9e02bc05cd920fb6441b70018da5326.mp4?pkey=AAUTZcZyp0Ig_v4NtpvmOQz31SQkSH5yFlE-p1tJ0Y6q2uRhXFlG-1v94LqA9Qa_lWErv1zEg1EuB35H13zU-reSoKlEGr37ckxsnovw_2mQUq3OgFjGyIJhIqnWLjYjeR0&tag=1-1682478898-unknown-0-dq59nfp47w-da8615b8ba03f216&clientCacheKey=3xgnyc9upjzvdcm_b.mp4&di=3cd05065&bp=10004&tt=b&ss=vp"
        this.accidentist.push(event[i])
      }
      //画爆炸效果视频
      // this.accidentDialogVisible = true
    },
    //小车运行方法
    radarDataList(event) {
      console.log(event)
      //判断隧道是否和传来的相同  相同显示不同则return
      let tunnelItems = null;
      if(!!this.tunnelItem){
        tunnelItems = this.tunnelItem
      }else{
        tunnelItems = this.tunnelList[0]
      }
      if(tunnelItems.tunnelId!=event[0].tunnelId){
        return;
      }

      //20秒清空一次不运行的小车
      if(this.catNumber>20){
        this.carList.clear()
        this.catNumber=1
      }
      for (let i = 0; i < event.length; i++) {

        //车辆实际距离入口距离
        var carKm = event[i].distance;

        // 根据车道数进行判断 上面空白区域
        if (this.lane == 2) {
          if (event[i].laneNo == 1) {
            event[i].top = 360 + "px";
          } else if (event[i].laneNo == 2) {
            event[i].top = 450 + "px";
          }
        }
        //通过实际距离  计算小车在页面位置
        event[i] = this.carBackCount(event[i],true)
        //判断边框  超出则删除数据
        if(math.add(math.multiply(+carKm * this.proportion) + 190)>1480 || math.add(math.multiply(+carKm * this.proportion) + 200)>1480){
          this.carList.delete(event[i].vehicleLicense);
          continue
        }
        //车辆类型 判定车颜色
        if(event[i].vehicleType == "40"){//危化车
          event[i].background = "red";
        }else if(event[i].vehicleType == "26"||event[i].vehicleType == "14"||event[i].vehicleType == "17"){//货车
          event[i].background = "yellow";
        }else if(event[i].vehicleType == "16"||event[i].vehicleType == "25"||event[i].vehicleType == "15"){//客车
          event[i].background = "blue";
        }
        this.carList.set(event[i].vehicleLicense, event[i]);
        console.log( this.carList)
      }
    },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true,
  },
}
