<template>
  <div>
    <div style="padding: 20px 0px 0px 20px;">事件详情</div>
    <div style="padding: 20px;">
      <el-table v-loading="loading" :data="eventList" :default-sort = "{prop: 'eventTime', order: 'descending'}">
        <el-table-column label="车道号" align="left" prop="laneNo"  />
         <el-table-column label="事件位置经度" align="left" prop="eventLongitude"  />
         <el-table-column label="事件位置纬度" align="center" prop="eventLatitude" />
         <el-table-column label="事件开始时间" align="center" prop="startTime" />
         <el-table-column label="隧道ID" align="center" prop="tunnelId" />
         <el-table-column label="隧道对象" align="center" prop="tunnels" />
         <el-table-column label="事件类型" align="center" prop="eventTypeId" />
         <el-table-column label="事件类型对象" align="center" prop="eventType" />
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
           <template slot-scope="scope">
             <el-button 
               size="mini"
               type="text"
               icon="el-icon-finished"
               @click="handleVideo(scope.row)"
               v-hasPermi="['system:event:edit']"
             >查看</el-button>
             
               <el-button
                 size="mini"
                 type="text"
                 icon="el-icon-chat-line-square"
                 v-hasPermi="['system:event:remove']"
                 @click="handleIgnore(scope.row)"
               >忽略
               </el-button>
               <el-button
                 size="mini"
                 type="text"
                 icon="el-icon-chat-line-square"
                 v-hasPermi="['system:event:remove']"
                 @click="handleDispatch(scope.row)"
               >处理
               </el-button>
           </template>
         </el-table-column>
       </el-table>
    </div>
    
  </div>
</template>

<script>
  import { mapState } from 'vuex';
  import bus from "@/utils/bus";
  export default{
   name:"eventDialog",
   data() {
     return {
      eventList: [
        {tunnelId:'11'}
      ],
      loading:false,
     }
   },
   computed: {
     ...mapState({
       WjEvent: state => state.websocket.WjEvent,
     })
   },
   watch: {
    WjEvent( event ){
      console.log(event,'eventeventeventeventeventeventevent')
      if(event){
        this.eventList = event
      }
     },
     deep: true,
   },
   methods:{
     // 查看事件
     handleVideo(row) {
       
     },
     // 忽略事件
     handleIgnore(row){
       
     },
     // 处理 跳转应急调度
     handleDispatch(row){
       bus.$emit("closeDialog",false)
       this.$router.push({ path: "/emergency/administration/dispatch", query: { row: row } });
     }
   }
  }
</script>

<style lang="scss" scoped>
 
</style>
