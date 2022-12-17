<template>
  <div class="app-container dispatchAss">
    <el-row>
      <el-col :span="7">
        <div class="dispatchLeft">
          <div class="video">
            <div class="title">实时视频</div>
            <div class="videoBox1">
              <div
                v-for="(item, index) of videoList"
                :key="index"
                class="videoContent"
              >
                <video :src="item.url" controls muted loop fluid></video>
                <div class="videoListTitle">{{ item.title }}</div>
              </div>
            </div>
          </div>
          <div class="evtMessage">
            <div class="title">事件信息</div>
            <div class="evtMessBox">
              <div class="evtMessLeft">
                <div>
                  <div>事件来源：</div>
                  <div>{{getFrom(eventForm.eventSource)}}</div>
                </div>
                <div>
                  <div>事件类型：</div>
                  <div>{{eventForm.simplifyName}}</div>
                </div>
                <div>
                  <div>所属隧道：</div>
                  <div>{{eventForm.tunnelName}}</div>
                </div>
                <div>
                  <div>方向：</div>
                  <div>{{getDirection(eventForm.direction)}}</div>
                </div>
                <div>
                  <div>影响车道：</div>
                  <div>{{eventForm.laneNo}} 车道</div>
                </div>
                <div>
                  <div>桩号：</div>
                  <div>{{eventForm.stakeNum}}</div>
                </div>
              </div>
              <div class="evtMessRight">
                <div class="evtMessVideo">
                  <video :src="eventForm.videoUrl" controls muted loop fluid></video>
                </div>
                <div class="evtMessImg"   v-if="eventForm.iconUrlList>0">
                  <img :src="item.imgUrl" v-for="(item,index) of eventForm.iconUrlList" :key="index" />
                </div>
                <img src="../../../assets/cloudControl/nullImg.png" v-else 
                  style="width: 46px; margin: 0 auto; display: flex;"/>
                <div class="evtMessTarget">
                  <div>事件目标：</div>
                  <div>
                    <div v-for="(item,index) of eventForm.confidenceList" :key="index">
                      {{item.plate}}/{{item.speed}}/{{item.eventConfidence}}
                    </div>

                  </div>
                </div>
              </div>

            </div>
          </div>
          <div class="plan">
            <div class="title">相关预案</div>
            <div class="planBox1">
              <div class="planLeft">
                <div class="oneWayTraffic">
                  <div>单向行车</div>
                  <div>
                    <el-radio v-model="reservePlan.oneWay" label="2" border>动态管控</el-radio>
                    <el-radio v-model="reservePlan.oneWay" label="1" border>全域管控</el-radio>
                  </div>
                  <div>
                    <div>预览</div>
                    <div @click="relation(1)" :disabled="relationDisabled">关联事件</div>

                  </div>
                </div>
                <div class="twoWayTraffic">
                  <div>双向行车</div>
                  <div>
                    <el-radio v-model="reservePlan.twoWay" label="1" border>全域管控</el-radio>
                  </div>
                  <div>
                    <div >预览</div>
                    <div @click="relation(2)" :disabled="relationDisabled">关联事件</div>

                  </div>
                </div>
              </div>
              <div class="planRight">
                <div>上游隧道</div>
                <div>
                  <div>隧道名称</div>
                  <div>金家楼隧道</div>
                  <div>隧道间距</div>
                  <div>300米</div>
                </div>
                <div>
                  <el-radio v-model="reservePlan.plan" label="2" border>动态管控</el-radio>
                  <el-radio v-model="reservePlan.plan" label="1" border :disabled="relationDisabled">全域管控</el-radio>
                </div>
                <div>
                  <div>预览</div>
                  <div>关联事件</div>

                </div>
              </div>

            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="17">
        <div class="dispatchRight">
          <div class="workBenchBox">
            <div class="tunnelBox1" v-show="activeMap == 1">
              <div class="tunnelMap">
                <el-image
                  class="back-img"
                  :src="backImg"
                  :style="{ width: laneUrlList[0].width + 'px' }"
                ></el-image>
                <div class="wrapper" id="eq-wrapper">
                  <div
                    class="icon-box active"
                    v-for="(item, index) in selectedIconList"
                    :key="index"
                    :style="{
                      left: item.position.left / 1.31 + 'px',
                      top: item.position.top / 1.23 + 'px',
                      'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                    }"
                    :class="
                      item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                        ? 'light-' + item.position.left
                        : ''
                    "
                  >
                    <div
                      :class="{ focus: item.focus }"
                    >
                      <img
                        v-show="item.eqType != ('31' || '16' || '36')"
                        v-for="(url, indexs) in item.url"
                        style="position: absolute"
                        :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                        :width="item.iconWidth / 1.23"
                        :height="item.iconHeight / 1.23"
                        :key="item.eqId + indexs"
                        :src='url'

                      />
                      <img
                        v-show="item.eqType == '31'"
                        style="position: absolute"
                        :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 0
                                ? 'scale(-1,1)'
                                : '',
                          }"
                        :width="item.iconWidth / 1.3"
                        :height="item.iconHeight"
                        :src= getTypePic(item)
                      />
                      <!-- 情报板洞内 -->
                      <div v-show="item.eqType == '16' || item.eqType == '36'"
                           style="position: absolute;overflow:hidden;writing-mode : tb-rl;
                            font-size:12px;color:#FFFF07;text-align: center;padding:2px"

                           :style="{
                          cursor:
                            item.eqType || item.eqType == 0 ? 'pointer' : '',
                          border:
                            item.click == true ? 'solid 2px #09C3FC' : '',
                            width:item.iconWidth / 1.3 + 'px',
                            height:item.iconHeight / 1.3 + 'px',
                          }"

                           :src= getTypePic(item)

                      >{{item.eqName}}
                      </div>
                      <!-- 情报板 门架 -->
                      <!-- <div v-show="item.eqType == '36'"
                      style="position: absolute;overflow:hidden;writing-mode : tb-rl;
                          font-size:15px;color:#FFFF07;text-align: center;padding:4px"

                      :style="{
                          cursor:
                            item.eqType || item.eqType == 0 ? 'pointer' : '',
                          border:
                            item.click == true ? 'solid 2px #09C3FC' : '',
                          width:item.iconWidth + 'px',
                          height:item.iconHeight + 'px',
                        }"

                        :src= getTypePic(item)
                        :class="
                          item.eqName == screenEqName
                            ? 'screenEqNameClass'
                            : ''
                        "
                        >{{item.eqName}}
                      </div> -->
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="tunnelBox3" v-show="activeMap == 2">
              <iframe
                name="tuniframe"
                id="miframe"
                class="map3D"
                frameborder="0"
                align="center"
                allowfullscreen="true"
                allow="autoplay"
                src="http://106.120.201.126:14712/dashboard"
              ></iframe>
            </div>
            <div class="tunnelBox2">
              <div >
                <img src="../../../assets/cloudControl/tunnelBox1.png"
                     style="transform: translate(-9px, -3px);"/>
              </div>
              <div>
                <img src="../../../assets/cloudControl/tunnelBox2.png" @click="changeActiveMap(1)"/>
                <img src="../../../assets/cloudControl/tunnelBox3.png" @click="changeActiveMap(2)"/>
                <!-- <img src="../../../assets/cloudControl/tunnelBox4.png"/> -->

              </div>
            </div>
          </div>
          <div class="rightBottom">
            <div class="evtManagement">
              <div class="IncHand">
                <div class="title">事件处置</div>
                <div class="incHandBox">
                  <div v-for="(item,index) of incHandList" :key="index" class="incHandContent">
                    <div class="classification">
                      <div class="type"
                           :style="{
                      padding:item.flowContent?item.flowContent.toString().length>2?'8px':'15px 12px':'',
                      marginTop:item.children?item.flowContent == '设备联控' ? (item.children.length * 40 + (4 * (item.children.length - 1)))/2 - 35 +'px':(item.children.length * 40 + (4 * (item.children.length - 1)))/2 - 25 +'px':''

                      }"
                      v-if="item.flowContent">{{item.flowContent}}
                      </div>
                    
                      <div v-show="item.flowId == 7" class="yijian">一键</div>
                    </div>

                    <div class="heng1"
                    v-if="item.children"
                         :style="{
                      marginTop:item.children?item.children.length==1?'20px':(item.children.length * 40 + (4 * (item.children.length - 1)))/2 +'px':''
                      }"
                      ></div>
                    <div class="shu"
                    v-if="item.children"
                         :style="{
                      height:item.children?item.children.length >1 ?item.children.length * 40 + (4 * item.children.length) - 40 +'px':'0px':'',
                      borderTop: item.children && item.children.length >1 ?'solid 1px #39adff':'',
                    }"
                    ></div>
                    <div>
                      <div v-for="(itm,inx) of item.children" :key="inx" class="contentList">
                        <div style="float:left">{{ itm.flowContent }}</div>
                        <!-- 绿对号 -->
                        <img :src="incHand2"  style="float:right;cursor: pointer;" v-show="itm.eventState != '0'" @click="changeIncHand(itm.id,1)">
                        <!-- 下发 -->
                        <img :src="incHand1"  style="float:right;cursor: pointer;" v-show="itm.eventState == '0'" @click="changeIncHand(itm.id,0)">

                      </div>
                    </div>

                  </div>

                </div>
              </div>
              <div class="DisRecords">
                <div class="title">处置记录</div>
                <el-timeline style="height: calc(100% - 40px); overflow: auto">
                  <el-timeline-item
                    placement="top"
                    v-for="(item, index) in eventList"
                    :key="index + item.flowTime"
                  >

                    <div>{{ item.flowDescription }}</div>
                    <div>{{item.flowTime}}</div>

                  </el-timeline-item>
                </el-timeline>
              </div>
            </div>
            <div class="implement1">
              <div class="phone">
                <div class="title">调度联络</div>
                <el-table :data="implementList" max-height="110" class="phoneTable">
                  <el-table-column
                    label="姓名"
                    align="center"
                    prop="userName"
                    width="150"
                  />
                  <el-table-column label="联系方式" align="center" prop="phone" >
                    <template slot-scope="scope">
                      <span>{{scope.row.phone}}</span>
                      <img :src="mesIcon"/>
                      <img :src="phoneIcon"/>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div class="eqRecord">
                <div class="title">设备执行记录</div>
                <div class="eqRecordBox">
                  <div
                    class="implementContent"
                    v-for="(item, index) in zxList"
                    :key="index"
                  >
                    <el-image
                      class="implementIcon"
                      :src="eqRecordIcon"
                    ></el-image>
                    <div class="contentBox">
                      <div class="row1">
                        <div>{{ item.eqName }}</div>
                        <div style="padding-left:20px;float: right;">{{ item.executeTime }}</div>
                      </div>
                      <div class="row2" >
                        <div >{{getDirection(item.eqDirection)}} {{ getEqType(item.state, item.eqType) }}</div>

                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { laneImage } from "../../../utils/configData.js";
import { listType } from "@/api/equipment/type/api.js";
import { getDeviceData } from "@/api/workbench/config.js";
import { listEqTypeState, getStateByData } from "@/api/equipment/eqTypeState/api";
import { dispatchExecuted, listEvent, eventFlowList, getHandle, updateHandle, getRelation } from "@/api/event/event";
import { listSdEmergencyPer } from "@/api/event/SdEmergencyPer";

export default {
  data() {
    return {
      relationDisabled:false,
      activeMap:1,
      eqTypeList:[],
      directionList: [],
      eventForm: {},
      timer: null,
      laneUrlList: laneImage,
      eqTypeStateList: null,
      selectedIconList: [],
      backImg: "",
      eqRecordIcon: require("@/assets/cloudControl/eqRecord.png"),
      incHand1: require("@/assets/cloudControl/incHand1.png"),
      incHand2: require("@/assets/cloudControl/incHand2.png"),
      phoneIcon: require("@/assets/cloudControl/phone2.png"),
      mesIcon: require("@/assets/cloudControl/phone1.png"),
      videoUrl: require("@/assets/Example/v1.mp4"),
      imgUrl: require("@/assets/Example/pic1.jpg"),
      videoList: [
        {
          url: require("@/assets/Example/v1.mp4"),
          title: "入口",
        },
        {
          url: require("@/assets/Example/v1.mp4"),
          title: "出口",
        },
        {
          url: require("@/assets/Example/v1.mp4"),
          title: "现场1",
        },
        {
          url: require("@/assets/Example/v1.mp4"),
          title: "现场2",
        },
      ],
      fromList: [
        {
          value: "0",
          label: "雷达",
        },
        {
          value: "1",
          label: "火灾报警",
        },
        {
          value: "2",
          label: "紧急电话",
        },
        {
          value: "3",
          label: "其他",
        },
      ],
      reservePlan: {
        oneWay: "1",
        twoWay: "1",
        plan: "1",
      },
      eventList: [
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
        // {
        //   flowTime: "2022-12-13",
        //   flowDescription: "杭山东隧道上行K134+421发生事故事",
        // },
      ],
      implementList: [
        // {
        //   userName: "XXX",
        //   phone: "18888888888",
        // },
        // {
        //   userName: "XXX",
        //   phone: "18888888888",
        // },
        // {
        //   userName: "XXX",
        //   phone: "18888888888",
        // },
        // {
        //   userName: "XXX",
        //   phone: "18888888888",
        // },
        // {
        //   userName: "XXX",
        //   phone: "18888888888",
        // },
      ],
      zxList: [
        // {
        // eqName: "更新车道指示器",
        // executeResult: "K131+123车道指示器46-禁行",
        // executeTime: "2022/12/09 19:10:20",
        // },
        // {
        //   eqName: "更新车道指示器",
        //   executeResult: "K131+123车道指示器46-禁行",
        //   executeTime: "2022/12/09 19:10:20",
        // },
        // {
        //   eqName: "更新车道指示器",
        //   executeResult: "K131+123车道指示器46-禁行",
        //   executeTime: "2022/12/09 19:10:20",
        // },
        // {
        //   eqName: "更新车道指示器",
        //   executeResult: "K131+123车道指示器46-禁行",
        //   executeTime: "2022/12/09 19:10:20",
        // },
        // {
        //   eqName: "更新车道指示器",
        //   executeResult: "K131+123车道指示器46-禁行",
        //   executeTime: "2022/12/09 19:10:20",
        // },
        // {
        //   eqName: "更新车道指示器",
        //   executeResult: "K131+123车道指示器46-禁行",
        //   executeTime: "2022/12/09 19:10:20",
        // },
      ],
      incHandList: [
      // {
      //     flowContent: "预警",
      //     children: [
      //       {
      //         flowContent: "更改隧道入口情报板“隧道事故禁止通行”。",
      //       },
      //       {
      //         flowContent: "更改入口信号灯为红色。",
      //       },
      //     ],
      //   },
      //   {
      //     flowContent: "分析确认",
      //     children: [
      //       {
      //         flowContent: "现场确认并上报应急指挥领导小组",
      //       },
      //       {
      //         flowContent: "上报智慧高速云控中心。",
      //       },
      //       {
      //         flowContent: "上报智慧高速云控中心。",
      //       },
      //     ],
      //   },
      //   {
      //     flowContent: "设备联控",
      //     children: [
      //       {
      //         flowContent: "更改隧道入口情报板“隧道事故禁止通行”。",
      //       },
      //       {
      //         flowContent: "更改入口信号灯为红色。",
      //       },
      //     ],
      //   },
      //   {
      //     flowContent: "应急调度",
      //     children: [
      //       {
      //         flowContent: "雷视融合检测，2022/12/07 15:34:33",
      //       },
      //     ],
      //   },
      ],
    };
  },
  computed: {
    ...mapState({
      deviceStatus: (state) => state.websocket.deviceStatus,
      deviceStatusChangeLog: (state) => state.websocket.deviceStatusChangeLog,
      eventFlow: (state) => state.websocket.eventFlow,
    }),
  },
  watch: {
    deviceStatus(event) {
      console.log(event, "websockt工作台接收实时设备状态数据");
      this.deviceStatusList = event;
    },
    deviceStatusChangeLog(event) {
      // console.log(event, "websockt工作台接收感知事件数据");
      console.log(event, "已执行");
      console.log(this.$route.query.id, "this.$route.query.id");
      for (let item of event) {
        if (this.$route.query.id == item.eventId) {
          this.zxList.unshift(item);
        }
      }
    },
    // eventFlow(event) {
    //   // console.log(event, "websockt工作台接收感知事件数据");
    //   console.log(event, "已执行11");
    //   var zxc = event;
    // },
  },
  async created() {
    console.log(this.$route.query.id, "this.$route.query.id");

    await this.getEqTypeStateIcon();
    // await this.getTunnelData();
    await this.getDispatchExecuted();
    this.getListEvent()
    this.stateByData()
    this.getEventList()
    // this.evtHandle()
    // this.getpersonnelList()
    this.getDicts("sd_direction_list").then((response) => {
      console.log(response.data, "response.data车道方向");
      this.directionList = response.data;
    });
  },
  mounted() {
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 100);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);
  },
  methods: {
    // 关联事件
    relation(type){
      const params = {
        tunnelId:this.eventForm.tunnelId,
        category:this.reservePlan.oneWay,
        controlDirection:type,
        direction:this.eventForm.direction,
        eventId:this.eventForm.id,
      }
      getRelation(params).then((res) =>{
        console.log(res,"关联事件");
        this.getListEvent()
        this.relationDisabled = true
      })
    },
    changeIncHand(id,type){
      const params = {
        id:this.$route.query.id,
        ids:id,
      }
      updateHandle(params).then((res) =>{
        console.log(res);
        for(let item of this.incHandList){
          for(let itm of item.children){
            if(itm.id == id){
              if(type == 0){
                itm.eventState = '1'
                this.evtHandle()
                this.getEventList()
              }
            }
          }
        }
      })
    },
    // 事件处置
    async evtHandle(){
      await getHandle({id:this.$route.query.id,eventTypeId:this.eventForm.eventTypeId}).then((res) =>{
       let list =  this.handleTree(res.data, "flowId","flowPid");
       console.log(list,"999999999999999999");
      //  for(let item of list){
      //   console.log(item.flowContent.toString().length,"555555555555555")
      //  }
       this.incHandList = list
       this.$forceUpdate()
      })
    },
    // 查设备状态
    stateByData(){
      const params = {
        isControl: 1,
      };
      getStateByData(params).then((res) => {
        console.log(res.rows, "查设备状态 正红泛绿...");
        this.eqTypeList = res.rows;
      });
    },
    // 事件详情
    async getListEvent(){
      if (this.$route.query.id) {
        const param = {
          id: this.$route.query.id,
        };
        listEvent(param).then((response) => {
          console.log(response, "事件详情");
          this.eventForm = response.rows[0];
          this.getpersonnelList()
          this.evtHandle()
          this.getTunnelData()
        });
      }
    },
    /** 查询应急人员信息列表 */
    async getpersonnelList() {
      const params = {
        tunnelId: this.eventForm.tunnelId,
      };
      await listSdEmergencyPer(params).then((response) => {
        console.log(99999999999999999999999999);
        this.implementList = response.rows;
      });
    },
    // 切换工作台和3D隧道
    changeActiveMap(type){
      console.log(this.activeMap,"this.activeMap");
      if(type == 1){
        this.activeMap = 1
      }else{
        this.activeMap = 2
      }
    },
    // 处置记录
    getEventList(){
      eventFlowList({eventId:this.$route.query.id}).then((res) =>{
        // console.log(res);
        this.eventList = res.rows
      })
    },
    //设备执行记录
    getEqType(state, eqType) {
      // console.log(state, eqType);

      for (let i = 0; i < this.eqTypeList.length; i++) {
        let item = this.eqTypeList[i];
        if (eqType == item.stateTypeId && Number(item.deviceState) == state) {
          // console.log(item.stateName);
          return item.stateName;
        }
        //  else {
        //   continue;
        // }
      }
    },
    // 查询方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 转事件来源数据
    getFrom(num) {
      for (let item of this.fromList) {
        if (num == item.value) {
          return item.label;
        }
      }
    },
    // 一进页面获取已执行数据
    getDispatchExecuted() {
      dispatchExecuted(this.$route.query.id).then((res) => {
        console.log(res, "一进页面获取已执行数据");
        this.zxList = res.data;
      });
    },
    /* 获取隧道配置信息*/
    async getTunnelData() {
      let tunnelId = this.eventForm.tunnelId;
      // var tunnelId = this.eventMsg.tunnelId; //"WLJD-JiNan-YanJiuYuan-FHS";
      let that = this;

      await getTunnels(tunnelId).then((response) => {
        let res = response.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          let id = res.lane;
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.backImg = that.laneUrlList[i].url;
            }
          }
          listType().then((response) => {
            // var arr = [];
            for (let i = 0; i < res.eqList.length; i++) {
              res.eqList[i].focus = false;
              for (let j = 0; j < response.rows.length; j++) {
                if (response.rows[j].typeId == res.eqList[i].eqType) {
                  let iconWidth = Number(response.rows[j].iconWidth);
                  let iconHeight = Number(response.rows[j].iconHeight);
                  res.eqList[i].iconWidth = iconWidth;
                  res.eqList[i].iconHeight = iconHeight;
                  break;
                }
              }
            }
            this.selectedIconList = res.eqList; //这是最终需要挂载到页面上的值
            that.getRealTimeData();
          });
        } else {
          //不存在
          that.selectedIconList = [];
        }
      });
    },
    getRealTimeData() {
      getDeviceData({
        tunnelId: this.eventForm.tunnelId,
      }).then((response) => {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          if (deviceData) {
            // let type = deviceData.eqType;

            // 需要换光标的
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  // 5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 31, 32, 33, 35,
                  5,
                  14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35,
                ];
                if (arr.includes(deviceData.eqType)) {
                  if (
                    // 摄像机之类的只有在线 离线 故障图标
                    this.eqTypeStateList[k].stateType == "1" &&
                    this.eqTypeStateList[k].state == deviceData.eqStatus
                  ) {
                    //取设备监测状态图标
                    this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                    if (deviceData.eqType == 19) {
                      this.selectedIconList[j].num =
                        "CO:" +
                        parseFloat(deviceData.CO).toFixed(2) +
                        "/PPM  VI:" +
                        parseFloat(deviceData.VI).toFixed(2) +
                        "KM";
                    } else if (deviceData.eqType == 17) {
                      this.selectedIconList[j].num =
                        parseFloat(deviceData.FS).toFixed(2) +
                        "m/s " +
                        deviceData.FX;
                    } else if (deviceData.eqType == 5) {
                      if (deviceData.DWLD) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.DWLD).toFixed(2) + "lux";
                      }
                    } else if (deviceData.eqType == 18) {
                      if (deviceData.DNLD) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                      }
                    }
                  }
                } else {
                  //可以控制设备状态的设备类型，比如车指
                  if (deviceData.eqStatus == "1") {
                    // 在线
                    if (
                      // 车指之类的包括正红反绿之类的图标 == 2
                      this.eqTypeStateList[k].stateType == "2"
                    ) {
                      if (this.eqTypeStateList[k].state == deviceData.state) {
                        //取设备运行状态图标
                        let url = this.eqTypeStateList[k].url;
                        this.selectedIconList[j].eqDirection =
                          deviceData.eqDirection;
                        if (deviceData.eqDirection == "1") {
                          //上行车道
                          if (url.length > 1) {
                            this.selectedIconList[j].url = [url[1], url[0]];
                          } else {
                            this.selectedIconList[j].url = url;
                          }
                        } else {
                          this.selectedIconList[j].url =
                            this.eqTypeStateList[k].url;
                        }
                      }
                    }
                  } else {
                    //如果是离线、故障等状态
                    if (
                      this.eqTypeStateList[k].stateType == "1" &&
                      this.eqTypeStateList[k].state == deviceData.eqStatus
                    ) {
                      //取设备监测状态图标
                      this.selectedIconList[j].url =
                        this.eqTypeStateList[k].url;
                    }
                  }
                }
              }
            }
          }
        }
      });
    },

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[0];
      for (let i = 0; i < this.laneUrlList.length; i++) {
        if (this.laneUrlList[i].num == num) {
          lane = this.laneUrlList[i];
          break;
        }
      }
      return lane;
    },
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        // isControl: 1,
      };
      await listEqTypeState(queryParams).then((response) => {
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    getEqUrl(list) {
      // let that = this;
      // that.eqTypeStateList = [];
      this.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let img = await that.picture(list[i].iFileList[j].url);
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        this.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
      }
    },
  },
};
</script>
<style scoped lang="scss">
.dispatchAss {
  .title {
    width: 100%;
    height: 40px;
    border-bottom: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
    color: white;
    line-height: 40px;
    padding-left: 16px;
    font-size: 16px;
  }
  .el-row {
    height: 100%;
    .el-col {
      height: 100%;
    }
  }
}
.dispatchLeft,
.dispatchRight {
  height: 100%;
}
.dispatchLeft {
  > div {
    width: 100%;
    // background: #012e51;
  }
  .video {
    height: calc(36% - 20px);
    margin-top: 0px !important;
    border-radius: 0px !important;
    .videoBox1 {
      width: 100%;
      height: calc(100% - 40px);
      word-wrap: break-word;
      word-break: normal;
      .videoContent {
        width: 50%;
        height: 48%;
        text-align: center;
        display: inline-block;
        justify-content: center;
        margin-top: 4px;
        video {
          height: 90px;
          width: 70%;
          object-fit: fill;
        }
        .videoListTitle {
          width: 75px;
          height: 18px;
          border-radius: 4px;
          font-size: 13px;
          margin-left: 35%;
          color: #fff;
        }
      }
      .videoContent:nth-of-type(1) .videoListTitle {
        background: #00c8ff;
      }
      .videoContent:nth-of-type(2) .videoListTitle {
        background: #59b94e;
      }
      .videoContent:nth-of-type(3) .videoListTitle {
        background: #c4a23c;
      }
      .videoContent:nth-of-type(4) .videoListTitle {
        background: #c4a23c;
      }
    }
  }
  .evtMessage {
    height: 32%;
    margin-top: 10px;
    .evtMessBox {
      display: flex;
      width: 100%;
      height: calc(100% - 40px);
      .evtMessRight {
        border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        width: 50%;
        height: 100%;
        .evtMessVideo {
          height: 54%;
          width: auto;
          display: flex;
          justify-content: center;
          margin-top: 10px;
          > video {
            height: 100%;
            width: 90%;
            display: block;
            object-fit: fill;
          }
        }
        .evtMessImg {
          width: 90%;
          height: 38px;
          display: flex;
          justify-content: space-between;
          margin: 5px auto;
          > img {
            width: 24%;
          }
        }
        .evtMessTarget {
          font-size: 14px;
          display: flex;
          > div:nth-of-type(1) {
            color: #008aff;
            width: 80px;
            margin-left: 14px;
          }
          > div:nth-of-type(2) {
            color: #fff;
            height: 40px;
            overflow: auto;
          }
        }
      }
      .evtMessLeft {
        width: 50%;
        height: 100%;
        > div {
          display: flex;
          font-size: 14px;
          margin-top: 13px;

          > div:nth-of-type(1) {
            color: #008aff;
            width: 90px;
            margin-left: 20px;
          }
          > div:nth-of-type(2) {
            color: #fff;
          }
        }
      }
    }
  }
  .plan {
    height: 32%;
    margin-top: 10px;
    .planBox1 {
      width: 100%;
      height: calc(100% - 40px);
      display: flex;
      .planLeft {
        width: 50%;
        height: 100%;

        .oneWayTraffic,
        .twoWayTraffic {
          width: 100%;
          height: 50%;
          font-size: 14px;
          // color: #fff;
          padding-left: 20px;
          padding-top: 5px;
          > div:nth-of-type(3) {
            float: right;
            width: 160px;
            display: flex;
            justify-content: space-between;
            height: 22px;
            color: white;
            margin-top: 20px;
            margin-right: 20px;
            > div {
              text-align: center;
              padding: 0 15px;
              border-radius: 15px;
              line-height: 22px;
              cursor: pointer;
            }
            > div:nth-of-type(1) {
              background: #d8d8d8
              linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
            }
            > div:nth-of-type(2) {
              background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
            }
          }
        }
        .twoWayTraffic {
          border-top: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        }
      }
      .planRight {
        width: 50%;
        height: 100%;
        // border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        font-size: 14px;
        // color: #fff;
        padding: 5px 20px 0px;
        > div:nth-of-type(2) {
          display: flex;
          justify-content: space-between;
          width: 100%;
          margin: 6px 0;
          > div {
            color: #008aff;
          }
          > div:nth-of-type(2n) {
            color: #fff;
          }
        }
        > div:nth-of-type(4) {
          float: right;
          width: 160px;
          display: flex;
          justify-content: space-between;
          height: 22px;
          color: white;
          margin-top: 20px;
          > div {
            text-align: center;
            padding: 0 15px;
            border-radius: 15px;
            line-height: 22px;
          }
          > div:nth-of-type(1) {
            background: #d8d8d8
            linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
          }
          > div:nth-of-type(2) {
            background: linear-gradient(180deg, #ffc506 0%, #ff8300 100%);
          }
        }
      }
      .el-radio--medium.is-bordered {
        padding: 0px 10px;
        border-radius: 4px;
        height: 30px;
        width: 106px;
        margin-top: 4px;
        line-height: 27px;
        margin-right: 0px;
        // background: linear-gradient(180deg, #002847 0%, #00325e 100%);
        border-radius: 2px;
        // border: 1px solid #005d89;
      }
      // .el-radio {
      //   color: white;
      // }
    }
  }
}
.dispatchRight {
  margin-left: 10px;
  .workBenchBox {
    width: 100%;
    height: 59%;
    display: flex;
    .tunnelBox1 {
      width: 95%;
      height: 100%;
      .tunnelMap {
        height: 100%;
        width: 100%;
        position: relative;
        margin-bottom: 10px;
        padding-bottom: 10px;
        .back-img {
          width: 100% !important;
          height: 100% !important;
          position: absolute;
        }
        .wrapper {
          height: 100%;
          width: 100%;
          position: absolute;
          // z-index: 3;
          top: 0px;
          left: 0px;
          .icon-box {
            position: absolute;
            display: flex;
            flex-direction: column;
            // align-items: center;
            width: 30px !important;
          }
        }
      }
    }
    .tunnelBox2 {
      width: 5%;
      position: relative;
      > div:nth-of-type(1) {
        position: absolute;
        top: 0;
        left: 20px;
        width: 48px;
        height: 48px;
      }
      > div:nth-of-type(2) {
        position: absolute;
        bottom: 0;
        left: 20px;
        width: 48px;
        height: 150px;
      }
    }
    .tunnelBox3{
      width: 95%;
      height: 100%;
      .map3D{
        width: 100%;
        height: 100%;
      }
    }
  }
  .rightBottom {
    width: 100%;
    height: calc(41% - 10px);
    margin-top: 10px;
    display: flex;
    .evtManagement {
      width: calc(67% - 10px);
      height: 100%;
      display: flex;
      // background: #012e51;
      .IncHand {
        width: 60%;
        height: 100%;
        .incHandBox {
          height: calc(100% - 40px);
          overflow: auto;
          .incHandContent {
            display: flex;
            // color: white;
            font-size: 12px;
            padding: 10px;
            .classification {
              .type {
                width: 50px;
                height: 50px;
                // background: rgba($color: #084e84, $alpha: 0.6);
                // border: 1px solid rgba($color: #39adff, $alpha: 0.6);
                text-align: center;
              }
              .yijian {
                width: 50px;
                background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
                border: 1px solid #39adff;
                // padding: 10px;
                text-align: center;
              }
            }

            .heng1 {
              width: 20px;
              height: 1px;
              border-top: solid 1px #39adff;
            }
            .shu {
              width: 20px;
              border-left: solid 1px #39adff;
              border-bottom: solid 1px #39adff;
              margin-top: 20px;
            }
            .contentList {
              display: block;
              margin-top: 4px;
              line-height: 40px;
              padding: 0 20px;
              background: rgba($color: #084e84, $alpha: 0.6);
              border-radius: 3px;
              width: 400px;
              display: flex;
              justify-content: space-between;
              align-items: center;
              img {
                width: 18px;
                height: 18px;
              }
            }
            .contentList:nth-of-type(1) {
              margin-top: 0;
            }
          }
        }
      }
      .DisRecords {
        width: 40%;
        height: 100%;
        border-left: solid 1px rgba($color: #f0f1f2, $alpha: 0.2);
        overflow: hidden;

        // .el-timeline {
        //   padding: 10px;
        // }
        // .el-timeline-item {
        //   padding-bottom: 0px !important;
        // }
        // // .el-timeline-item__node--normal {
        // //   transform: translateY(10px);
        // // }
        // .el-timeline-item__tail {
        //   transform: translateY(10px);
        //   // border-left: 2px dashed #97A8AE;
        // }
        // .el-timeline-item__wrapper {
        //   margin-right: 8px;
        //   padding-left: 20px;
        // }
        // // 竖线
        // .el-timeline-item__tail {
        //   transform: translateY(30px);
        // }
        // .el-timeline-item__node--normal {
        //   top: 20px !important;
        // }
        // // 内容框
        // .el-timeline-item__content {
        //   padding: 10px;
        //   color: white;
        //   > div:nth-of-type(1) {
        //     text-align: left;
        //     width: 100%;
        //   }
        //   > div:nth-of-type(2) {
        //     text-align: right;

        //     width: 100%;
        //   }
        // }
      }
    }
    .implement1 {
      width: 33%;
      height: 100%;
      margin-left: 10px;
      .phone {
        width: 100%;
        height: calc(50% - 5px);
        // background: #012e51;
        .phoneTable {
          background: transparent !important;
          padding: 10px;

          img {
            margin-left: 10px;
          }
        }
      }
      .eqRecord {
        width: 100%;
        height: calc(50% - 5px);
        margin-top: 10px;
        // background: #012e51;
        .eqRecordBox {
          height: calc(100% - 40px);
          overflow: auto;
          .implementContent {
            width: 100%;
            height: 50px;
            padding-right: 10px;
            .implementIcon {
              width: 10px;
              height: 84%;
              text-align: center;
              padding-top: 11px;
              margin-left: 3%;
              margin-right: 3%;
            }
            .contentBox {
              display: inline-block;
              height: 100%;
              width: 90%;
              font-size: 12px;
              color:#fff;
              .row1 {
                display: flex;
                justify-content: space-between;
                align-items: center;
                height: 30px;
                > div:nth-of-type(1) {
                  // color: #00A0FF;
                }
                > div:nth-of-type(2) {
                  // color: #999999;
                }
              }
              .row2 {
                display: flex;
                align-items: center;
                height: 20px;
                // color: #333;
              }
            }
          }
        }
      }
    }
  }
}
// 修改封装样式
::v-deep .el-timeline {
  padding: 10px;
  .el-timeline-item {
    padding-bottom: 0px !important;
    // 竖线
    .el-timeline-item__tail {
      transform: translateY(40px);
      border-left: 2px dashed #97a8ae;
    }
    .el-timeline-item__node--normal {
      top: 32px !important;
      background: #3bd1fe;
      border: 1px solid #ffffff;
    }
    .el-timeline-item__wrapper {
      margin-right: 8px;
      padding-left: 20px;
      .el-timeline-item__content {
        padding: 10px;
        color: white;
        background: rgba($color: #084e84, $alpha: 0.6);
        font-size: 12px;
        > div:nth-of-type(1) {
          text-align: left;
          width: 100%;
        }
        > div:nth-of-type(2) {
          text-align: right;
          width: 100%;
        }
      }
    }
  }
}
::v-deep .el-table .el-table__cell {
  height: 30px !important;
}
::v-deep ::-webkit-scrollbar {
  width: 0px;
}
// ::v-deep .phoneTable tr {
//   background: #12578f !important;
// }
// ::v-deep .phoneTable tr:nth-of-type(2n) {
//   background: #165484 !important;
// }
::v-deep .el-radio__inner::after {
  width: 12px;
  height: 12px;
  background: #008aff;
  border: solid 2px #012e51;
}
</style>
