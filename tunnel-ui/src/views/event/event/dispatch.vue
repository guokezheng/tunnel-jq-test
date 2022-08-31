<template>
  <div class="app-container">
    <div style="height: 100%;display: flex;justify-content: space-between;">
      <div  style="height: 100%;width: 66%;">
        <el-card class="bottomEventForm">
        <div class="tunnelMap">
          <el-image
            class="back-img"
            :src="laneUrlList[0].url"
            :style="{ width: laneUrlList[0].width + 'px' }"
          ></el-image>
          <div  class="maskClass" >
              <div v-for="(item,index) in planList1" class="mousemoveBox"
              @contextmenu.prevent="rightClick(index)"
              @mouseleave='mouseleave(index)'
              :style="{marginTop:index>=planList1.length/2?'4%':'',width:100/(planList1.length/2)+'%'}"
              >
                <div class="partitionBox"></div>
                <div class="rightClickClass">
                  <div class="row1" >{{item.text}}</div>
                  <div class="row2" >
                    <div>执行</div>
                    <div>预览</div>
                  </div>
                </div>
              </div>
          </div>
          <div
            class="wrapper"
            id="eq-wrapper"
          >
            <div
              class="icon-box"
              v-for="(item, index) in selectedIconList"
              :key="index"
              :style="{
                      left: item.position.left / 1.34 + 'px',
                      top: item.position.top / 1.3  + 'px',
                      'z-index': item.eqType || item.eqType == 0 ? '' : '-1'
                    }"
              :class="item.eqType == 7 || item.eqType == 8 || item.eqType == 9?'light-' + item.position.left:''"
            >
              <div
             v-show="(item.eqType != 7 &&
                       item.eqType != 16 &&
                       item.eqType != 15 &&
                       item.eqType != 9 &&
                       item.display == true) ||
                     ((item.eqType == 7 ||
                       item.eqType == 8 ||
                       item.eqType == 9 ||
                       item.eqType == 21 ) &&
                       item.display == true &&
                       lightSwitch == 1)"
             :class="{ focus: item.focus }"
              >
                <img
                  v-for="(url, indexs) in item.url"
                  style="position: relative;"
                  :style="item.eqType || item.eqType==0 ? 'cursor: pointer;' : ''"
                  :width="item.iconWidth /1.26"
                  :height="item.iconHeight / 1.26"
                  :key="item.deptId + indexs"
                  :src="url"
                />
              </div>
            </div>
          </div>
        </div>
        <div style="height: 39%;" v-show="changeVideo == 0">
            <div class="formTitle" >
              <div style="float: left;">事件详情</div>
              <div class="formButton formButton1" @click="submitEventForm">
                <el-image :src="require('@/assets/icons/relieve.png')"/>
                <div>事件解除</div>
              </div>
              <div class="formButton formButton2" @click="eventFormClose">
                <el-image :src="require('@/assets/icons/update.png')"/>
                <div>更新信息</div>
              </div>
            </div>
            <el-form ref="form" :model="eventForm" label-width="80px" style="height: calc(100% - 35px);overflow-y: auto;">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="事件类型" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle" placeholder="请输入事件类型" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事故类型" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" placeholder="请选择事故类型" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="所属路段" prop="tunnelId">
                    <el-select v-model="eventForm.tunnelId" placeholder="请选择所属路段" clearable size="small" >
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                 <el-col :span="8">
                   <el-form-item label="桩号位置:" prop="eventTitle">
                     <span>K </span>
                     <el-input  v-model="eventForm.eventTitle" placeholder="请输入桩号位置" style="width: 250px;"/>
                   </el-form-item>
                 </el-col>
                 <el-col :span="8">
                   <el-form-item label="开始时间" prop="eventTime">
                     <el-date-picker clearable size="small"
                       v-model="eventForm.eventTime"
                       type="datetime"
                       value-format="yyyy-MM-dd HH:mm:ss"
                       placeholder="选择开始时间">
                     </el-date-picker>
                   </el-form-item>
                 </el-col>
                 <el-col :span="8">
                   <el-form-item label="预撤时间" prop="eventTime">
                     <el-date-picker clearable size="small"
                       v-model="eventForm.eventTime"
                       type="datetime"
                       value-format="yyyy-MM-dd HH:mm:ss"
                       placeholder="选择预撤时间">
                     </el-date-picker>
                   </el-form-item>
                 </el-col>
               </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="影响程度" prop="tunnelId">
                    <el-select v-model="eventForm.tunnelId" placeholder="请选择影响程度" clearable size="small" >
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="压车长度:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  style="width: 100px" />
                    <span> KM</span>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="事件等级 " prop="eventGrade">
                    <el-radio-group v-model="eventForm.eventGrade">
                      <el-radio
                        v-for="dict in eventGradeOptions"
                        :key="dict.dictValue"
                        :label="dict.dictValue"
                      >{{dict.dictLabel}}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="事件标题:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
                <el-col :span="16">
                  <el-form-item label="详细内容:" prop="eventTitle">
                    <el-input v-model="eventForm.eventTitle"  style="width: 660px;"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="人员伤亡:" prop="eventTitle">
               <el-row>
                 <el-col :span="5">
                  <div class="formLable">轻伤：</div>
                  <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                  <span> 人</span>
                </el-col>
                <el-col :span="5">
                  <div class="formLable">重伤：</div>
                  <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                  <span> 人</span>
                </el-col>
                 <el-col :span="5">
                  <div class="formLable">死亡：</div>
                  <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                  <span> 人</span>
                </el-col>
               </el-row>
              </el-form-item>
              <el-form-item label="车辆:" prop="eventTitle">
                <el-row>
                  <el-col :span="5">
                    <div class="formLable">小型车：</div>
                    <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">货车：</div>
                    <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">客车：</div>
                    <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                    <span> 辆</span>
                  </el-col>
                  <el-col :span="5">
                    <div class="formLable">罐车：</div>
                    <el-input v-model="eventForm.eventTitle" style="width: 100px;"/>
                    <span> 辆</span>
                  </el-col>
                </el-row>
              </el-form-item>
              <el-row>
                <el-col :span="8">
                    <el-form-item label="特情处置" prop="tunnelId">
                      <el-select v-model="eventForm.tunnelId" placeholder="请选择特情处置" clearable size="small" >
                        <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                          :value="item.tunnelId" />
                      </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="洒落物:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="超时原因:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small" >
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="倒货:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="天气:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="拥堵路段:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="施工路段:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="处在弯道:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="在分叉口:" prop="eventTitle">
                    <el-select v-model="eventForm.tunnelId" clearable size="small">
                      <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                        :value="item.tunnelId" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="车主电话:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="清障电话:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="交警电话:" prop="eventTitle">
                    <el-input  v-model="eventForm.eventTitle"  />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="经验总结:" prop="eventTitle">
                <el-input type="textarea" v-model="eventForm.eventTitle"  style="width: 1056px;"/>
              </el-form-item>
            </el-form>
          </div>
          <div style="height: 39%;" v-show="changeVideo == 1">
            <div class="formTitle" >
              <div style="float: left;">摄像视频</div>
              <div class="formButton formButton2" @click="returnDetails">
                <el-image :src="require('@/assets/icons/change.png')"/>
                <div>切换事件详情</div>
              </div>
            </div>
            <div class="cameraBox">
              <video controls muted autoplay loop :src=item.src  v-for="item in videoList"></video>
              <el-image :src="require('@/assets/icons/outline.png')" v-show="videoList.length<2"/>
            </div>
          </div>
        </el-card>
      </div>


      <div style="height: 100%;margin-left: 10px;width: 34%;">
        <el-row style="height: 100%;">
          <el-col :span="11" style="height: 100%;">
            <!-- 视频 -->
            <div class="rightBox videoBox">
                <video controls muted autoplay loop :src="require('@/assets/Example/d1.mp4')" class="video1" ></video>
                <div class="video">
                  <img :src="require('@/assets/images/warningPhoto.png')" style="width: 32%;border-radius: 6px;" v-for="item in 3"/>
                </div>
            </div>
            <!-- 预案 -->
            <div class="rightBox planBox">
              <div class="title">周边物资</div>
              <el-table :data="materialList" max-height="230" >
                <el-table-column label="物资名称" align="center" prop="name" />
                <el-table-column label="桩号" align="center" prop="phone" />
                <el-table-column label="数量" align="center" prop="phone1" width="60"/>
              </el-table>

            </div>
            <!-- 电话 -->
            <div class="rightBox phoneBox">
              <div class="title">调度电话</div>
              <el-table :data="personnelList" max-height="210" >
                <el-table-column label="姓名" align="center" prop="name" width="60"/>
                <el-table-column label="联系方式" align="center" prop="phone" />
                <el-table-column label="操作" align="center" class-name="small-padding" width="60">
                  <template slot-scope="scope">
                    <div class="phoneButton">
                      <el-image :src="require('@/assets/icons/phone.png')" />
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
          <el-col :span="12" style="height: 100%;">
            <!-- 事件流程 -->
            <div class="rightBox processBox">
              <el-steps :active="1">
                <el-step title="事件预警" icon="el-icon-edit"></el-step>
                <el-step title="事故确认" icon="el-icon-upload"></el-step>
                <el-step title="处置中" icon="el-icon-picture"></el-step>
                <el-step title="处置结束" icon="el-icon-s-tools"></el-step>
              </el-steps>
              <el-timeline style="height: calc(100% - 150px);overflow: auto;">
                  <el-timeline-item placement="top" v-for="(item,index) in eventList" color="#00A0FF">
                    <div>{{item.time}}</div>
                    <el-card>
                      <p>{{item.content}}</p>
                    </el-card>
                  </el-timeline-item>
                </el-timeline>
                <div class="endButton">
                  <div class="ButtonBox">
                    <div class="recovery">左洞恢复</div>
                    <div class="button">
                      <div>预览</div>
                      <div>执行</div>
                    </div>
                  </div>
                  <div class="ButtonBox">
                    <div class="recovery">右洞恢复</div>
                    <div class="button">
                      <div>预览</div>
                      <div>执行</div>
                    </div>
                  </div>
                 <!-- <el-image :src="require('@/assets/icons/hand.png')"/>
                  <div>一键结束</div> -->
                </div>
            </div>
            <div class="rightBox implement">
              <div class="title">已执行</div>
              <div style="height: calc(100% - 26px);overflow: auto;">
                <div class="implementContent"  v-for="item in 6">
                  <el-image class="implementIcon" :src="require('@/assets/icons/implementIcon.png')"></el-image>
                  <div class="contentBox" >
                    <div class="row1">
                      <div>更新车道指示器</div>
                      <div>13:25:45</div>
                    </div>
                    <div class="row2">zk546+11车道指示器46 - 禁行</div>
                  </div>
                </div>
              </div>

            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from "jquery";
  import {
    icon,
    laneImage,
  } from "../../../utils/configData.js";
  import {
    getTunnels,
  } from "@/api/equipment/tunnel/api.js";
  import {
    listType,
  } from "@/api/equipment/type/api.js";
  export default {
    name: "dispatch",
    data(){
      return{
        planList1:[
          {
            text:'火灾报警1区'
          },
          {
            text:'火灾报警2区'
          },
          {
            text:'火灾报警3区'
          },
          {
            text:'火灾报警4区'
          },
          {
            text:'火灾报警5区'
          },
          {
            text:'火灾报警6区'
          },
          {
            text:'火灾报警7区'
          },
          {
            text:'火灾报警8区'
          },
          {
            text:'火灾报警9区'
          },
          {
            text:'火灾报警10区'
          },
        ],
        lightSwitch: 0,
        changeVideo:1,
        eqTunnelData:[],
        videoList:[
          {
            src:require('@/assets/Example/v1.mp4')
          },
          {
            src:require('@/assets/Example/d1.mp4')
          },
        ],
        // 策略按钮
        strategyList:[
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
          {
            name:'火灾报警1区',
          },
        ],
        // 人员列表
        personnelList:[
          {
            name:'郭鹏',
            phone:'13666666666'
          },
          {
            name:'郭鹏',
            phone:'13666666666'
          },
          {
            name:'郭鹏',
            phone:'13666666666'
          },
          {
            name:'郭鹏',
            phone:'13666666666'
          },
          {
            name:'郭鹏',
            phone:'13666666666'
          },
        ],
        materialList:[
          {
            name:'泡沫灭火器',
            phone:'K666+000',
            phone1:'1',

          },
          {
            name:'干粉灭火器',
            phone:'K666+000',
            phone1:'1',

          },
          {
            name:'消火栓',
            phone:'K666+000',
            phone1:'1',
          },
          {
            name:'消防水带',
            phone:'K666+000',
            phone1:'1',
          },
          {
            name:'消防水带',
            phone:'K666+000',
            phone1:'1',
          },
          {
            name:'消防水带',
            phone:'K666+000',
            phone1:'1',
          },
        ],
        coviList:[
          {
            name:'CO',
            phone:'XXX',
            phone2:'0.00',
          }
        ],
        //车道列表
        laneUrlList: laneImage,
        eventForm:{
          eventGrade:'0'
        },
        eventGradeOptions:[
          {
            dictValue:"0",
            dictLabel:"一般"
          },
          {
            dictValue:"1",
            dictLabel:"较大及以上"
          },
        ],
        // 时间轴循环数据
        eventList:[
          {
            time:'2022-08-08 14:04:00',
            title:'泰安监控高静静',
            content:'根据匿名先生(13396235108)来电，录入事件：S31泰新高速公路K079+000新泰方向，1辆货车发生故障，占用应急道，超车道、行车道正常通行，请过往车辆减速慢行。'

          },
          {
            time:'2022-08-08 14:10:00',
            title:'泰安监控高静静',
            content:'已通知路管、交警、路政。'
          },
          {
            time:'2022-08-08 14:20:00',
            title:'泰安监控高静静',
            content:'故障车车牌：鲁V637RR'
          },
          {
            time:'2022-08-08 14:22:00',
            title:'泰安监控高静静',
            content:'路管人员到达现场已安全布控'
          },
          {
            time:'2022-08-08 14:25:00',
            title:'泰安监控高静静',
            content:'策略选择：火灾报警1区'
          },
        ],
      moveTop: 0.11,
      py: "", //开始y轴的位置
      px: "", //开始x轴的位置
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: ""
      },
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
      selectedIconList: [], //配置图标
      }
    },
    created() {
      this.getTunnelData()
    },
    methods:{
      rightClick(index){
         $('.mousemoveBox').eq(index).children(1)[1].style.display = 'block'
         console.log($('.mousemoveBox').eq(index).children(1)[1],"index")
      },
      // mouseenter(index){
      //   console.log(index)
      //   $('.mousemoveBox').eq(index).children(1)[1].style.display = 'block'
      // },
      mouseleave(index){
        $('.mousemoveBox').eq(index).children(1)[1].style.display = 'none'
      },

      returnDetails(){
        this.changeVideo = 0
      },
      submitEventForm(){

      },
      eventFormClose(){

      },
      /* 鼠标点击*/
      dian(event) {
        if (event.button != 0) {
          return;
        }
        let parentObj = document.getElementById("eq-wrapper");
        wrapperClientX = parentObj.getBoundingClientRect().left;
        wrapperClientY = parentObj.getBoundingClientRect().top;
        this.px = event.clientX + event.clientX * this.moveTop - wrapperClientX;
        this.py = event.clientY + event.clientY * this.moveTop - wrapperClientY;
        boxEqList = [];
        this.batchForm = {
          eqType: "",
          eqList: [],
          state: "",
        };
      },
      /* 获取隧道配置信息*/
      getTunnelData(tunnelId) {
        var tunnelId = "JQ-JiNan-WenZuBei-MJY"
        let that = this;
        that.upList = [];
        that.downList = [];
        const params = {
          tunnelId: tunnelId
        }
        getTunnels(tunnelId).then((response) => {
          let res = response.data.storeConfigure;
          //存在配置内容
          if (res != null && res != "" && res != undefined) {
            res = JSON.parse(res);
            console.log(res,"res")
            listType("").then((response) => {
              console.log(response,'response')
              for (let i = 0; i < res.eqList.length; i++) {
                res.eqList[i].focus = false;
                console.log(res.eqList[i].focus,'item.focus')
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
              that.selectedIconList = res.eqList //设备
              console.log(that.selectedIconList, '当前设备所属隧道')
            }).then(() => {
            });
          } else {
            //不存在
            that.selectedIconList = [];
            //工作台默认背景图
            // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
            that.upList = [];
            that.downList = [];
            that.leftDirection = "";
            that.rightDirection = "";
          }
        });
      },
    }
    }
</script>

<style scoped lang="scss">
  .leftContent{
    width: 30%;
    height: 100%;
  }
  .rightContent{
    width: 70%;
    height: 100%;
    margin-left: 10px;
  }
  ::v-deep .el-card__body{
    height: 100%;
    overflow-y: auto;
  }
  ::v-deep .timelineClass{
    height: calc(100% - 50px);
    // .el-card__body{
    //   height: 100%;
    //   overflow-y: auto;
    // }
  }
  .formLable{
    width: 60px;
    display: inline-block;
  }
  .steps{
    height: 26%;
  }
  ::v-deep .el-icon-sunrise{
    width: 10px;
    height: 10px;
    border-radius: 5px;
    background-color: #1D58A9;
  }
  ::v-deep .el-icon-sunrise:before {
      content: "11";
      visibility: hidden;
  }
  /* 进行中状态：圈线 */
  ::v-deep .steps .el-step__line {
    color: #1D58A9;
    border-color: #1D58A9;
  }
  ::v-deep .el-icon-eleme{
    width: 10px;
    height: 10px;
    border-radius: 5px;
    background-color: #00ff7f;
  }
  ::v-deep .el-icon-eleme:before {
      content: "11";
      visibility: hidden;
  }
  .back-img{
    width: 100% !important;
    height: 98% !important;
    position: absolute;
  }
  .changeMap{
    width: 30px;height: 30px;position: absolute;left: 0px;top: 0px;cursor: pointer;z-index: 10;
  }
  ::v-deep .bottomEventForm{
    width: 100%;
    height: 100%;
    .el-card__body{
      height: 100%;
      overflow-y: auto;
      overflow-x: hidden;
      .tunnelMap{
        height: 59%;width:100%;position: relative;margin-bottom: 10px;border-bottom: solid 1px #ccc;padding-bottom: 10px;
        .maskClass{
          height: 100%;width: 100%;position: relative;z-index: 3;
            .mousemoveBox{
              height: 44%;position: relative;display: inline-block;
              .partitionBox{
                width: 100%;height: 100%;color: white;position: relative;z-index: 100;
              }
              .partitionBox:hover{
                background-color: rgba($color: #fff, $alpha: 0.3);z-index: 10;
              }
              .rightClickClass{
                width: 70%;height: 70%;border: solid 1px white;position: absolute;top: 15%;left:15%;display: none;
                color: white;background-color: rgba($color: #005E96, $alpha: 0.5);z-index: 100;border-radius: 10px;box-shadow: 0 0 5px white;
                .row1{
                  width: 100%;height: 50%;border-bottom: dashed 1px #ccc;display: flex;justify-content: center;align-items: center;
                }
                .row2{
                  width: 100%;height: 50%;display: flex;justify-content: space-around;padding: 10px;
                  div{
                    width: 35%;height: 30px;border: solid 1px white;border-radius: 10px;text-align: center;font-size: 14px;line-height: 29px;cursor: pointer;
                  }
                  div:nth-of-type(1):hover{
                    background-color: #E1AA43;
                  }
                  div:nth-of-type(2):hover{
                    background-color: #19B9EA;
                  }
                }
              }
            }

        }
      }
    }
  }
  ::v-deep .el-input{
    width: 263px;
  }
  ::v-deep ul{
    padding-inline-start: 8px !important;
  }
  .strategyButton{
    margin-top: 10px;
    width: 138px;
  }
  ::v-deep .el-button{
    margin-left: 10px;
  }
  // 工作台
  .wrapper {
    height: 100%;
    width: 100%;
    position: absolute;
    // z-index: 3;
    top: 0px;
    left: 0px;
  }
  .icon-box {
    position: absolute;
    display: flex;
    flex-direction: column;
    // align-items: center;
    width: 30px !important;
    z-index: 2;
  }
  .app-container{
      background-color: #D6EDFB;
      padding: 0px 16px;
  }
  .formTitle{
    width: 100%;height: 35px;line-height: 35px;border-left: 5px solid #385CD6;padding-left: 10px;
    background-color: #E6EAFD;font-size: 16px;font-weight: bold;margin-bottom: 6px;
  }
  .formButton{
    width: 130px;height: 35px;float: right;color: white;border-radius: 2px;text-align: center;font-size: 14px;font-weight: 500;
    display: flex;justify-content: center;align-items: center;cursor: pointer;
    >.el-image{
        width: 15%;
        height: 50%;
        margin-right: 4px;
    }
  }
  .formButton2{
    background-image: linear-gradient(2deg,#4B6AD4,#07A1FB);

  }
  .formButton1{
    background: linear-gradient(2deg, #37D8AC, #1CCF7B);
    margin-left: 10px;
  }
  .cameraBox{
    width: 100%;height: calc(100% - 35px);display: flex;justify-content: space-between;
    .el-image{
      width: 49%;
      height: 100%;
      border: solid 1px #E0E7ED;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      ::v-deep .el-image__inner{
        width: 60%;height: 65%;
      }
    }
    video{
      width: 49%;
      height: 100%;
      border: solid 1px #E0E7ED;
      border-radius: 10px;
      object-fit: cover;
      border-radius: 10px;
    }
  }
  .video1{
    width: 100%;height: 150px;object-fit: cover;z-index: -100;border-radius: 10px;
  }
  .rightBox{
    width: 100%;border-radius: 10px;padding: 10px;background-color: #F2F9FF;
    >.video{
      width: 100%;height: 26%;display: flex;justify-content: space-between;
    }
  }

  .videoBox{
    border-top: solid 10px #1287B2;height: 30%;
  }
  .planBox{
    border-top: solid 10px #0F9CF5;margin-top: 10px;height: 35%;
    >.title{
      font-size: 14px;font-weight: bold;height: 26px;line-height: 20px;border-bottom: solid 1px #E0E7ED;margin: 0px auto 0px auto;
    }
    .planMiniBox{
      width: 43%;height: 68px;border-radius: 10px;border: solid 1px #00A0FF;display: inline-block;margin: 8px;
      >.miniTitle{
        width: 100%;text-align: center;font-size: 12px;line-height: 24px;font-weight: bold;border-bottom: dashed 1px #00A0FF;
        height: 30px;line-height: 30px;
      }
      >.planButtonS{
        height: 26px;width: 100%;display: flex;justify-content: space-around;padding-top: 4px;color: white;font-size: 12px;
        text-align: center;line-height: 23px;
        >div{
          width: 45%;height: 100%;background: linear-gradient(2deg, #4B6AD4, #07A1FB);border-radius: 2px;cursor: pointer;
        }
      }
    }
  }
  .phoneBox{
    border-top: solid 10px #E1AA43;margin-top: 10px;height: calc(35% - 20px);
    >.title{
      font-size: 14px;font-weight: bold;height: 26px;line-height: 20px;border-bottom: solid 1px #E0E7ED;margin: 0px auto 0px auto;
    }


    .phoneButton{
      width: 40px;height: 23px;background: linear-gradient(2deg,#4B6AD4,#07A1FB);border-radius: 12px;
      .el-image{
        width: 14px;height: 17px;padding-top: 4px;
      }
    }
  }
  .processBox{
    border-top: solid 10px #52B7C9;height: 65%;
    margin-left: 10px;
    ::v-deep .el-step__icon.is-icon{
      background: linear-gradient(114deg, #96DAFA, #5FA6F5);
      width: 40px;
      height: 40px;
      border-radius: 20px;
    }
    ::v-deep .el-icon-edit {
        background: url(../../../assets/icons/earlyWarning1.png);
        background-position: top;
        background-repeat: no-repeat;
        background-size: 80% 80%;
    }

    ::v-deep .el-icon-edit:before {
        content: "11";
        visibility: hidden;
    }
    ::v-deep .el-icon-upload {
        background: url(../../../assets/icons/confirm1.png);
        background-position: center;
        background-repeat: no-repeat;
        background-size: 80% 80%;
    }
    ::v-deep .el-icon-upload:before {
        content: "11";
        visibility: hidden;
    }
    ::v-deep .el-icon-picture{
      background: url(../../../assets/icons/management1.png);
      background-position: center;
      background-repeat: no-repeat;
      background-size: 80% 80%;
    }
    ::v-deep .el-icon-picture:before {
        content: "11";
        visibility: hidden;
    }
    ::v-deep .el-icon-s-tools{
      background: url(../../../assets/icons/end1.png);
      background-position: center;
      background-repeat: no-repeat;
      background-size: 80% 80%;
    }
    ::v-deep .el-icon-s-tools:before {
        content: "11";
        visibility: hidden;
    }
    ::v-deep .el-step__title{
      font-size: 12px;
    }
    ::v-deep .el-step__title.is-finish,.el-step__title.is-process{
      color: #333;
    }
    ::v-deep .el-step__title.is-process{
      font-weight: 400;
      color: #333;
    }
    ::v-deep .el-step.is-horizontal .el-step__line{
      top: 20px;
      border-top: dashed 2px #8BCFF9;
      background-color: transparent;
    }

    ::v-deep .el-card{
      color: #3D6CA2;
      border: 1px solid #C4DAE7;
      border-radius: 10px;
      background-color: #E8F3FC;
      margin-top: 10px;
    }
    ::v-deep .el-card__body{
      padding: 15px !important;
    }
    ::v-deep .el-timeline-item{
      padding-bottom: 0px !important;
    }
    ::v-deep .el-timeline-item__node--normal{
      transform: translateY(10px);
    }
    ::v-deep .el-timeline-item__tail{
      transform: translateY(10px);
      border-left: 2px dashed #97A8AE;
    }
    ::v-deep .el-timeline-item__wrapper{
      margin-right: 8px;
    }
    .endButton{
      width: 90%;height: 65px;margin: 10px auto;display: flex;justify-content: space-between;
      // background: linear-gradient(2deg, #07A1FB, #4B6AD4);border-radius: 2px;
      color: #4B6AD4;
      font-size: 14px;
      .ButtonBox{
        width: 45%;height: 100%;border: solid 1px #07A1FB;border-radius: 10px;background-color: #E8F3FC;box-shadow: 0 0 5px #07A1FB;
        .recovery{
          width: 100%;height: 45%;border-bottom: dashed 1px #07A1FB;text-align: center;line-height: 2;
        }
        .button{
          height: 55%;display: flex;justify-content: space-around;color: white;padding-top: 5%;
          div{
            width: 40%;height: 80%;text-align: center;font-size: 12px;line-height: 2;
            background: linear-gradient(2deg, #4B6AD4, #07A1FB);border-radius: 4px;cursor: pointer;
          }
        }
      }
      // .el-image{
      //   width: 16px;height: 30px;padding-top: 6px;transform: translateX(-5px);
      // }
      // >div{
      //   height: 35px;display: inline-block;transform: translate(6px,-7px);
      // }
    }
  }
  .implement{
    border-top: solid 10px #52B7C9;height: calc(35% - 10px);
    margin-left: 10px;
    margin-top: 10px;
    >.title{
      font-size: 14px;font-weight: bold;height: 26px;line-height: 20px;border-bottom: solid 1px #E0E7ED;margin: 0px auto 0px auto;
    }
    .implementContent{
      width: 100%;height: 50px;border-bottom: dashed 1px #E0E7ED;padding-right: 10px;
      .implementIcon{
        width: 2%;height: 100%;text-align: center;padding-top: 11px;margin-left: 1%;margin-right: 6%;
      }
      .contentBox{
        display: inline-block;height: 100%;width: 90%;font-size: 12px;transform: translateY(-20%);
        .row1{
          display: flex;justify-content: space-between;align-items: flex-end;height: 30px;
          >div:nth-of-type(1){
            color: #00A0FF;
          }
          >div:nth-of-type(2){
            color: #999999;
          }
        }
        .row2{
          display: flex;align-items: flex-end;height: 20px;color: #333;
        }
      }
    }

  }
  ::-webkit-scrollbar-track-piece {
      background-color: rgba($color: #00c2ff, $alpha: 0.1);
      border-left: 1px solid rgba(0, 0, 0, 0);
  }
  ::-webkit-scrollbar {
      width: 8px;
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
  ::v-deep .el-icon-picture-outline{
    background: url(../../../assets/icons/outline.png);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100% 100%;
  }
  ::v-deep .el-icon-picture-outline:before{
    content: "11";
    visibility: hidden;
  }
  ::v-deep .el-table .el-table__cell.is-center{
    background-color: #F2F9FF !important;
  }
  ::v-deep .el-table .el-table__header-wrapper th, .el-table .el-table__fixed-header-wrapper th{
    background-color: #F2F9FF !important;
  }
  // ::v-deep .el-table thead{
  //   color: #1287B2 !important;
  // }
  ::v-deep .el-table .el-table__header-wrapper th, .el-table .el-table__fixed-header-wrapper th{
    color: #1287B2 !important;
  }
  /*table滚动条背景色 */
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    background-color: rgba($color: #00c2ff, $alpha: 0.1);
    width: 8px;
    height: 10px;
  }

  /* table滚动条的滑块*/
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: rgba($color: #00c2ff, $alpha: 0.6);
    border-radius: 10px;
  }
  /* 鼠标悬浮*/
  ::v-deep .el-table--enable-row-hover .el-table__body tr:hover > td {
    background-color: #c4e8f6;
  }
  ::v-deep .el-table__body{
    width: 264px !important;
  }
</style>
