<template>
  <div style="width: 100%;height: 100%;">
    <el-dialog v-dialogDrag class="workbench-dialog" title="固定摄像机"  width="450px" append-to-body :visible="cameraVisible"
                :before-close="handleClosee">
      <div style="width: 100%;height: 30px;padding: 0 15px;">
        
      </div>
      <div style="width: 100%;height: 200px;padding: 0 15px;">
         <video id="videoBox" controls muted autoplay loop style="width: 100%;height:200px;object-fit: cover;z-index: -100;" :src="src"></video>
      </div>
      <el-form ref="form" :model="stateForm" label-width="90px" label-position="left" size="mini"
              style="padding-top: 0px;">
        <el-tabs class="robotTabs" v-model="videoActive">
          <el-tab-pane label="详细信息" name="information">
            <el-row>
              <el-col :span="13">
                <el-form-item label="设备类型:" >
                  {{ stateForm.eqName}}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="隧道名称:" >
                  {{ stateForm.tunnelName }}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="所属机构:" >
                  {{ stateForm.deptName }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="设备桩号:" >
                  {{ stateForm.pile }}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="设备状态:" >
                  {{ stateForm.eqStatus }}
                </el-form-item>
              </el-col>
              
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="上行摄像机:" >
                  <!-- {{ '1000米' }} -->
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="下行摄像机:" >
                  <!-- {{ '1000米' }} -->
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="摄像机参数" name="videoParams">
            <el-row>
              <el-col :span="13">
                <el-form-item label="IP:" >
                  {{ stateForm.ip }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="端口:" >
                  {{ stateForm.port }}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="协议类型:" >
                  {{ stateForm.protocol }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="分辨率:" >
                  <!-- {{ '1280*720' }} -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="格式:" >
                  <!-- {{ 'Video/H.262' }} -->
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="码流:" >
                  <!-- {{ '8M' }} -->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="13">
                <el-form-item label="厂商:" >
                  {{ stateForm.brandName }}
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="型号:" >
                  {{ stateForm.eqModel }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="videoViewing()" style="width: 80px;" class="submitButton">录像查看</el-button>
        <el-button type="primary" size="mini" @click="handleClosee()" style="width: 80px;" >取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog v-dialogDrag class="workbench-dialog" title="摄像机K20+060"  width="1000px" append-to-body :visible="historyVisible"
                :before-close="handleClosee" >
        <el-form ref="historyForm" :model="queryParams" label-width="100px" :inline="true" size="mini" style="margin-top: 10px;">
          <el-form-item label="查询事件" prop="eventTypeId">
            <el-select v-model="queryParams.eventTypeId" placeholder="请选择事件类型" clearable size="small" style="width:180px">
              <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
          </el-form-item>
          <el-form-item label="选择时间" prop="eventTime" >
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              unlink-panels
              start-placeholder="开始时间"
              end-placeholder="结束时间"
            ></el-date-picker>
          </el-form-item>
          <el-button type="primary" size="mini" @click="handleQuery()" style="width: 60px;">查 询</el-button>
          <el-button type="primary" size="mini" @click="resetQuery()" style="width: 60px;">重 置</el-button>
        </el-form>
        <div style="width: 100%;height: 400px;overflow-y: auto;">
          <div style="width: 19%;display: inline-block;height: 200px;margin-left: 9px;" v-for="item in 20">
            <div style="width: calc(100% - 20px);height: 50%;margin: 5px auto;">
              <el-image :src="pic" style="width: 100%;height: 100%;"></el-image>
            </div>
            <div style="padding-left: 10px;line-height: 20px;">2022-03-11 14:47:13</div>
            <div style="padding-left: 10px;line-height: 20px;">事件: 道路拥堵</div>
            <div style="padding-left: 10px;line-height: 20px;">状态: <span style="color: #FF9900;">正在进行</span></div>
          </div>
        </div>
       
          <pagination
            v-show="total==0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="handleQuery"
            style="height: 40px;margin-right: 30px;"
          />
       
    </el-dialog>
  </div>
</template>

<script>
  import flvjs from "flv.js";
  import { getDevices } from "@/api/equipment/eqlist/api";
  export default{
     props: ['equipmentId'],
    data(){
      return{
        cameraVisible:true,//摄像机弹窗
        historyVisible:false,//历史记录弹窗
        videoActive: 'information',// tab页
        stateForm:{},//弹窗表单
        eventTypeData:[],//事件类型
        queryParams:{
          pageNum: 1,
          pageSize: 10,
        },//历史记录表单
        dateRange:[],//选择时间数组
        total: 0,// 总条数
        src:require('@/assets/Example/v1.mp4'),
        pic:require('@/assets/images/warningPhoto.png'),
      }
    },
    created() {
      console.log(this.equipmentId,"equipmentIdequipmentId")
      // 根据设备id 获取弹窗内信息
      if(this.equipmentId){
        const eqId = this.equipmentId
        getDevices(eqId).then(res =>{
          console.log(res,"查询摄像机弹窗信息")
          this.stateForm = {
            pile:res.data.pile,//桩号
            eqName:res.data.eqName,//设备类型
            tunnelName:res.data.tunnelName.tunnelName,//隧道名称
            ip:res.data.ip,//设备ip
            brandName:'',//厂商
            eqModel:res.data.eqModel,//型号
            protocol:res.data.protocol,//协议类型
            port:res.data.port,//端口
            deptName:res.data.deptName,//所属机构
          }
        })
      }else{
        this.$modal.msgWarning("没有设备Id");
      }
      this.loadFlv()
    },
    methods:{
      loadFlv() {
        // if (flvjs.isSupported()) {
        //   var videoElement = document.getElementById("videoBox");
        //   var flvPlayer = flvjs.createPlayer({
        //     type: 'flv',
        //     url: 'http://10.166.139.12:8081/live/22456.flv' //你的url地址
        //   });
        //   flvPlayer.attachMediaElement(videoElement);
        //   flvPlayer.load();
        //   flvPlayer.play();
        // }
      },
      // 关闭弹窗
      handleClosee() {
        this.$emit('dialogClose')
      },
      // 录像查看
      videoViewing(){
        this.cameraVisible = false
        this.historyVisible = true
      },
      // 历史记录表单查询
      handleQuery(){
        
      },
      // 历史记录表单重置
      resetQuery(){
        this.resetForm("queryForm");
      }
    }
  }
</script>

<style lang="scss" scoped>
  .robotTabs {
    padding: 0 15px;
  }
  .el-row{
    margin-bottom: -10px;
    display: flex;
    flex-wrap: wrap
  }
  
</style>
