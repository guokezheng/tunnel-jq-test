<template>
  <div>
   <div class="eventBox" >
     <div class="title">事件详情
     <img src="../../assets/cloudControl/dialogHeader.png" style="height: 30px;"/>
     </div>
     <div class="blueLine" ></div>
     <div style="display: flex;width: 100%;height: calc(100% - 40px);">
       <div class="eventLeft">
         <div class="video">
           <video :src='videoUrl' controls muted autoplay loop style="width: 100%;height: 390px;object-fit: cover;border-radius: 10px;"></video>
         </div>
         <div class="pic">
           <el-carousel :interval="4000" type="card" indicator-position="none"  height="200px">
               <el-carousel-item v-for="(item,index) in urls" :key="index" :src="item.imgUrl">
                 <img  :src="item.imgUrl" style="width: 100%;"/>
               </el-carousel-item>
             </el-carousel>
         </div>
       </div>
       <div class="eventRight">
         <div class="eventRow">
           <div>隧道名称:</div><div>{{event.tunnels}}</div>
         </div>
         <div class="eventRow">
           <div>事件类型:</div><div>{{event.eventTypeId}}</div>
         </div>
         <div class="eventRow">
           <div>车道号:</div><div>{{event.laneNo}}</div>
         </div>
         <div class="eventRow">
           <div>事件位置经度:</div><div>{{event.eventLongitude}}</div>
         </div>
         <div class="eventRow">
           <div>事件位置纬度:</div><div>{{event.eventLatitude}}</div>
         </div>
         <div class="eventRow">
           <div>事件桩号:</div><div>{{event.stakeNum}}</div>
         </div>
         <div class="eventRow">
           <div>事件开始时间:</div><div>{{event.startTime}}</div>
         </div>
         <div class="eventRow">
           <div>事件结束时间:</div><div>{{event.endTime}}</div>
         </div>
        
         <div style="width: 90%;display: flex;">
           <div class="handle button" @click="handleDispatch(event)">处 理</div>
           <div class="ignore button" @click="handleIgnore(event)">忽 略</div>
         </div>
         <div style="width: 90%;display: flex;">
           <div class="next button" v-show="eventList.length>1" @click="handleBefore(event.num)">上一条</div>
           <div class="next button" v-show="eventList.length>1" @click="handleNext(event.num)">下一条</div>
         </div>
         
       </div>
     </div>
    </div >
  </div>
</template>

<script>
  import { mapState } from 'vuex';
  import bus from "@/utils/bus";
  import { loadPicture } from "@/api/equipment/type/api.js";
  import { image,video } from "@/api/eventDialog/api.js";
  import { updateEvent } from "@/api/event/event";
  export default{
   name:"eventDialog",
   data() {
     return {
      eventList: [
        {
          tunnels:'11',
          id:1,
          laneNo:1,
          eventLongitude:35.632234,
          eventLatitude:117.36434,
          startTime:"17:34:23",
          endTime:'17:44:23',
          eventTypeId:'火灾报警',
          stakeNum:'K600+000'
          },
        {
          tunnels:'22',
          id:222},
        {
          tunnels:'33',
          id:333},
      ],
     
      urls: [
        {
          imgUrl:require('@/assets/images/warningPhoto.png'),
        },
        {
          imgUrl:require('@/assets/images/warningPhoto.png'),
        },
        {
          imgUrl:require('@/assets/images/warningPhoto.png'),
        },
        {
          imgUrl:require('@/assets/images/warningPhoto.png'),
        },
        ],
      videoUrl:require("@/assets/Example/v1.mp4"),
      // videoUrl:'',
      event:[
        {
          tunnelId:'',
          id:null,
          laneNo:null,
          eventLongitude:null,
          eventLatitude:null,
          startTime:"",
          tunnels:"",
          eventTypeId:'',
          eventType:''
        }
      ]
     }
   },
   computed: {
     ...mapState({
       WjEvent: state => state.websocket.WjEvent,
     })
   },
   watch: {
    WjEvent( event ){
      console.log(event,'websockt接收数据')
      if(event){
        for(var i=0;i<event.length;i++){
          event[i].num = i
        }
        console.log(event)
        this.eventList = event;
        this.event = this.eventList[0]
        this.getUrl()
      }
     },
     deep: true,
   },
   created() {
     this.getUrl()
     for(var i=0;i<this.eventList.length;i++){
       this.eventList[i].num = i
     }
     this.event = this.eventList[0]
     
   },
   methods:{
     
     getUrl(){
       console.log(this.event.id,'当前事件id')
       const param3 ={
        businessId:111,
        // businessId:this.event.id
       }
       const param4 ={
        id:1
        // id:this.event.id
       }
       image(param3).then(response => {
         console.log(response.data,'获取图片')
         this.urls = response.data;
       });
       video(param4).then(response => {
         console.log(response.data,'获取视频')
         this.videoUrl = response.data;
       });
     },
     
     // 忽略事件
     handleIgnore(event){
       const param ={
         id:1,
         eventState:'2'
       }
       updateEvent(param).then(response => {
         console.log(response,'修改状态')
         this.$modal.msgSuccess("已成功忽略");
       });
       bus.$emit("closeDialog",false)
     },
     // 处理 跳转应急调度
     handleDispatch(event){
       const param ={
         id:1,
         eventState:'0'
       }
       updateEvent(param).then(response => {
         console.log(response,'修改状态')
         this.$modal.msgSuccess("开始处理事件");
       });
       bus.$emit("closeDialog",false)
       this.$router.push({ path: "/emergency/administration/dispatch", query: { id: event.id } });
     },
     // 上一个事件
     handleBefore(num){
       this.event = this.eventList[num-1]
       console.log(this.event,"this.event")
       this.getUrl()
       this.$forceUpdate()
     },
     // 下一个事件
     handleNext(num){
       this.event = this.eventList[num+1]
       console.log(this.event,"this.event")
       this.getUrl()
       this.$forceUpdate()
     },
     /* 请求图片base64地址*/
     picture(fileUrl) {
       return new Promise((resolve, reject) => {
         loadPicture({
           url: fileUrl,
         }).then((response) => {
           if (response.code == 200) {
             let base64 = response.msg;
             resolve(base64); //不可缺少
           }
         });
       });
       return resolve(base64);
     },
   }
  }
</script>

<style lang="scss" scoped>
 .eventBox{
   width: 52%;height: 660px;border: solid 1px rgba($color: #0198FF, $alpha: 0.5);
   position: absolute;top: 10%;left: 25%;background-color: #071930;
   >.title{
     padding-left: 20px ;height: 30px;line-height: 30px;color: white;font-size: 14px;
     font-weight: bold;background: linear-gradient(270deg, rgba(1,149,251,0) 0%, rgba(1,149,251,0.35) 100%);
     border-top: solid 2px white;display: flex;justify-content: space-between;
     border-image: linear-gradient(to right,#0083FF,#3FD7FE,#0083FF)1 10;
   }
   .blueLine{
     width: 20%;height: 1px;border-bottom: solid 1px white;margin-bottom: 20px;
     border-image: linear-gradient(to right,#0083FF,#3FD7FE,#0083FF)30 30;
   }
 }
 .eventLeft{
   width: 70%;height: 590px;padding: 0px 20px;
   .video{
     width: 100%;height: 390px;margin-top: 0px !important;
   }
   .pic{
     width: 100%;height: 180px;margin-top: 10px;
   }
 }
 ::v-deep .el-carousel__mask{
   background-color: transparent;
 }
 
 .eventRight{
   width: 30%;height: 590px;color: white;font-size: 16px;
   .eventRow{
     display: flex;height: 50px;
     >div:nth-of-type(1){
       width: 140px;
       color: #0198FF;
     }
     >div{
       line-height: 50px;
     }
   }
   .button{
     width: 60%;
     height: 40px;
     margin-top: 15px;
     border-radius: 10px;
     border: solid 1px #00c8ff;
     text-align: center;
     line-height: 40px;
     margin-left: 10px;
     cursor: pointer;
   }
   .handle{
    color: #E1AA43;
   }
   .handle:hover{
     background-color: #E1AA43;
     color: white;
   }
   .ignore{
    color: #19B9EA;
   }
   .ignore:hover{
     background-color: #19B9EA;
     color: white;
   }
   .next{
     color: #fff;
   }
   .next:hover{
     background-color: #ddd;
     color: #005487;
   }
 }
 .videoDialog{
   width: 70%;height: 600px;border: solid 10px rgba($color: #5AC4E5, $alpha: 0.3);
   position: absolute;top: 15%;left: 15%;background-color: white;border-radius: 10px;
   padding: 10px;display: flex;
 }
 .closeButton{
   width: 40px;height: 40px;border: solid 5px rgba($color: #5AC4E5, $alpha: 0.3);
   position: absolute;top: -20px;left: calc(100% - 15px);border-radius: 20px;
   background-color: white;text-align: center;line-height: 35px;cursor: pointer;
 }
 ::v-deep .el-icon-close{
   font-size: 24px !important;color: #005487;font-weight: bold;
 }
 // 滚动条
 ::-webkit-scrollbar-track-piece {
     background-color: rgba($color: #00c2ff, $alpha: 0.1);
     border-left: 1px solid rgba(0, 0, 0, 0);
 }
 ::-webkit-scrollbar {
     width: 0px;
     height: 10px;
 }
 ::-webkit-scrollbar-thumb {
     background-color: rgba($color: #00c2ff, $alpha: 0.6);
     background-clip: padding-box;
     border-radius: 10px;
     min-height: 28px;
 }
 ::-webkit-scrollbar-thumb:hover {
     background-color: #00c2ff;
 }
</style>
