<script src="../../../../vue.config.js"></script>
<template>
  <div class="app-container safeWarnStyle">
    <el-tabs v-model="activeName" @tab-click="handleClick" style="100%">
      <el-tab-pane
        :label="item.title"
        :name="item.name"
        v-for="item in tabList"
        :key="item.name"
      >
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          v-if="activeName == '1' || activeName == '0'"
          label-width="68px"
          class="formStyle"
        >
          <el-row>
            <el-col>
              <el-form-item label="事件类型" prop="eventTypeId">
                <div style="display: flex">
                  <el-button
                    v-for="(item, index) in eventTypeData"
                    class="eventTypeButton"
                    :key="index"
                    @click="handleEvtButton(item.id)"
                  >
                    {{ item.simplifyName }}
                  </el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="管理机构" prop="deptId">
                <treeselect
                  v-model="queryParams.deptId"
                  :options="deptOptions"
                  :show-count="true"
                  placeholder="请选择归属部门"
                  @select="changeMechanism"
                  style="width: 300px"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                label="所属隧道"
                prop="tunnelId"
                v-show="manageStation == '0'"
              >
                <el-select
                  v-model="queryParams.tunnelId"
                  placeholder="请选择所属隧道"
                  clearable
                  size="small"
                  style="width: 180px"
                >
                  <el-option
                    v-for="item in tunnelList"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="事件状态" prop="eventState">
                <el-select
                  v-model="queryParams.eventState"
                  placeholder="请选择事件状态"
                  clearable
                  size="small"
                  style="width: 180px"
                >
                  <el-option
                    v-for="dict in eventStateOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="选择日期" prop="eventTime">
                <el-date-picker
                  v-model="dateRange"
                  size="small"
                  style="width: 240px"
                  value-format="yyyy-MM-dd"
                  type="daterange"
                  range-separator="-"
                  unlink-panels
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" size="mini" @click="handleQuery"
                  >搜索
                </el-button>
                <el-button size="mini" @click="resetQuery" type="primary" plain
                  >重置
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
          v-if="activeName == '2'"
        >
          <el-form-item label="故障位置" prop="faultLocation">
            <el-input
              v-model="queryParams.faultLocation"
              placeholder="请输入故障位置"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="故障类型" prop="faultType">
            <el-select
              v-model="queryParams.faultType"
              placeholder="请选择故障类型"
              clearable
              size="small"
            >
              <el-option
                v-for="dict in dict.type.fault_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="故障描述" prop="faultDescription">
            <el-input
              v-model="queryParams.faultDescription"
              placeholder="请输入故障描述"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="handleQuery"
              >搜索
            </el-button>
            <el-button type="primary" plain size="mini" @click="resetQuery"
              >重置
            </el-button>
            <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:list:add']"
              >新增
            </el-button>
            <el-button
              type="primary"
              plain
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:list:edit']"
              :style="{ display: 'none' }"
              >修改
            </el-button>
            <el-button
              type="primary"
              plain
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:list:remove']"
              :style="{ display: 'none' }"
              >删除
            </el-button>
            <el-button
              type="primary"
              plain
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:list:export']"
              :style="{ display: 'none' }"
              >导出
            </el-button>
          </el-form-item>
        </el-form>
        <div
          class="contentListBox"
          v-if="activeName == '1' || activeName == '0'"
        >
          <div
            class="contentBox"
            v-for="(item, index) in eventList"
            :key="index"
          >
            <div class="video">
              <img :src="item.picUrl" v-show="item.picUrl" style="width:100%" @click="openPicDialog(item)"/>
              <img src="../../../assets/cloudControl/nullImg.png" v-show="!item.picUrl" />

              <div>{{ item.simplifyName }}</div>
            </div>
            <div class="contentText">
              <div>
                来源 <span>{{ getFrom(item.eventSource) }}</span>
              </div>
              <div>
                位置 <span>{{ item.position }}</span>
              </div>
              <div>
                时间 <span>{{ item.startTime }}</span>
              </div>
              <div class="contentButton">
                <div @click="detailsButton(item, 1)">详情</div>
                <div @click="detailsButton(item, 2)"
                :class="item.eventState == '1'|| item.eventState == '2'?'disabledButton':''">复核</div>
              </div>
              <div class="stateTab">
                <img :src="safeWarn0" v-show="item.eventState == '0'" />
                <img :src="safeWarn1" v-show="item.eventState == '1'" />
                <img :src="safeWarn2" v-show="item.eventState == '2'" />
                <img :src="safeWarn3" v-show="item.eventState == '3'" />
              </div>
            </div>
          </div>
        </div>

        <el-table
          v-loading="loading"
          :data="eventList"
          @selection-change="handleSelectionChange"
          :row-class-name="tableRowClassName"
          max-height="600"
          v-if="activeName == '2'"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="故障类型" align="center" prop="faultType">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.fault_type"
                :value="scope.row.faultType"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="发现时间"
            align="center"
            prop="faultFxtime"
            width="180"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.faultFxtime, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column label="持续时间" align="center" prop="faultCxtime" />
          <!--      <el-table-column label="设备id" align="center" prop="eqId"/>-->
          <el-table-column label="设备状态" align="center" prop="eqStatus">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.sd_monitor_state"
                :value="scope.row.eqStatus"
              />
            </template>
          </el-table-column>
          <el-table-column label="故障等级" align="center" prop="faultLevel">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.fault_level"
                :value="scope.row.faultLevel"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="消除状态"
            align="center"
            prop="falltRemoveStatue"
          >
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.fault_remove_statue"
                :value="scope.row.falltRemoveStatue"
              />
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="faultStatus">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.fault_status"
                :value="scope.row.faultStatus"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleCheckDetail(scope.row)"
                v-hasPermi="['system:list:edit']"
                :style="{ display: scope.row.faultStatus == 1 ? 'none' : '' }"
                >故障详情</el-button
              >
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="recordQuery(scope.row)"
              >
                检修记录
              </el-button>
              <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:list:edit']"
                :style="{ display: scope.row.faultStatus == 0 ? 'none' : '' }"
                >修改
              </el-button>
              <el-button
                size="mini"
                class="tableDelButtton"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:list:remove']"
                :style="{ display: scope.row.faultStatus == 0 ? 'none' : '' }"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-if="total > 0 && activeName == '2'"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
          :page-size="16"
        />
        <el-pagination
          v-if="total>0 && (activeName == '0' || activeName == '1')"
          class="specialPagination"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[16, 32, 48, 64]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </el-tab-pane>
    </el-tabs>

    <!-- 查看详情弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="details"
      :before-close="cancel"
      width="100%"
      text-align="left"
      :style="processType ? 'left: 13%' : ''"
      class="detailsDialog"
      :close-on-click-modal="false"
      ref="upload"
      :modal="false"
      append-to-body
    >
    <!-- append-to-body -->

    <!--  -->
      <div class="videoDialogBox">
        <div
          class="processButton"
          @click="openProcess()"
          :class="processType ? 'el-icon-s-fold' : 'el-icon-s-unfold'"
        >预警处置</div>
        <div class="dialogBg">
          <div>三图一视<span>(事前、事中、事后三张抓图+录像判断)</span></div>
          <video :src="eventForm.videoUrl" controls muted loop fluid></video>
          <div class="picBox">
            <div v-if="arrowLeft" class="turnPages"  @click="turnLeft()"><</div>
            <div class="picList">
              <div
              v-show="eventForm && eventForm.iconUrlList != []"
              v-for="item in imgUrlList" :key="item.imgId">
                <img  :src="item.imgUrl"   />
              </div>
              <div 
                v-show="eventForm && eventForm.iconUrlList == []" class="noPic"
                v-for="(item,index) of 4" :key="index">
                <img src="../../../assets/cloudControl/nullImg.png"/>
              </div>
            </div>
            <div v-if="arrowRight" class="turnPages" @click="turnRight()">></div>

          </div>
        </div>
        <div class="dialogBg">
          <div>实时视频<span>(事发位置最近的监控视频录像)</span></div>
          <videoPlayer
            v-if="videoForm.liveUrl "
            :rtsp="videoForm.liveUrl"
            :open="cameraPlayer"
          ></videoPlayer>
          <div class="picBox">
            <div v-if="arrowLeft2" class="turnPages"  @click="turnLeft2()"><</div>
            <div class="picList">
              <div 
              v-for="item in urlsList" :key="item.imgId">
                <img  :src="item.imgUrl"   />
              </div>
              <div 
                v-show="!urls" class="noPic"
                v-for="(item,index) of 4" :key="index">
                <img src="../../../assets/cloudControl/nullImg.png"/>
              </div>
            </div>
            <div v-if="arrowRight2" class="turnPages" @click="turnRight2()">></div>

          </div>
        </div>
      </div>
      <div class="dialogForm">
        <el-form :model="eventForm" label-width="80px" :rules="rules">
          <el-row style="display: flex; flex-wrap: wrap">
            <el-col :span="8">
              <el-form-item label="告警来源" prop="eventSource">
                <el-select
                  v-model="eventForm.eventSource"
                  :disabled="detailsDisabled"
                  placeholder="请选择告警来源"
                >
                  <el-option
                    v-for="item in fromList"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="告警时间" prop="startTime">
                <el-date-picker
                  clearable
                  size="small"
                  :disabled="detailsDisabled"
                  v-model="eventForm.startTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择告警时间"
                  style="width: 100%"
                  :picker-options="setDisabled"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="预计解除时间"
                prop="endTime"
                label-width="100px"
              >
                <el-date-picker
                  clearable
                  size="small"
                  :disabled="detailsDisabled"
                  v-model="eventForm.endTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择预计解除时间"
                  style="width: 100%"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属隧道" prop="tunnelName">
                <el-select
                  v-model="eventForm.tunnelName"
                  placeholder="请选择所属隧道"
                  clearable
                  size="small"
                  disabled
                >
                  <el-option
                    v-for="item in tunnelList"
                    :key="item.tunnelId"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件起点">
                <el-row >
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeNum1"
                      placeholder="Km"
                      :disabled="detailsDisabled"
                      width="100%"
                    />
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeNum2"
                      placeholder="m"
                      :disabled="detailsDisabled"
                      width="100%"
                    />
                  </el-col>
                </el-row>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="事件终点" label-width="100px">
                <el-row>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeEndNum1"
                      placeholder="Km"
                      :disabled="detailsDisabled"
                      width="100%"
                    />
                  </el-col>
                  <el-col :span="1">+</el-col>
                  <el-col :span="11">
                    <el-input
                      v-model="eventForm.stakeEndNum2"
                      placeholder="m"
                      :disabled="detailsDisabled"
                      width="100%"
                    />
                  </el-col>
                </el-row>
                
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="影响车道">
                <el-row>
                  <el-col :span="12">
                    <el-select
                      v-model="eventForm.direction"
                      placeholder="方向"
                      clearable
                      size="small"
                      :disabled="detailsDisabled"
                      style="width: 100%"
                    >
                      <el-option
                        v-for="item in directionList"
                        :key="item.dictValue"
                        :label="item.dictLabel"
                        :value="item.dictValue"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="12">
                    <el-select
                      v-model="eventForm.laneNo"
                      placeholder="车道"
                      clearable
                      size="small"
                      :disabled="detailsDisabled"
                      style="width: 100%; margin-left: 15px"
                    >
                      <el-option
                        v-for="item in chezhiLaneList"
                        :key="item.laneId"
                        :label="item.laneName"
                        :value="item.laneId"
                      />
                    </el-select>
                  </el-col>
                </el-row>
                
                
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="事件描述" prop="eventDescription">
                <el-input
                  v-model="eventForm.eventDescription"
                  placeholder="m"
                  :disabled="detailsDisabled"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="事件车辆" prop="plate">
                <div class="evtCarStyle">
                  <div
                    v-for="(item, index) in eventForm.confidenceList"
                    :key="index"
                  >
                    <div class="evtNum">{{ index + 1 }}</div>
                    <div>
                      车牌号
                      <el-input
                        v-model="item.plate"
                        :disabled="detailsDisabled"
                      ></el-input>
                    </div>
                    <div>
                      速度
                      <el-input
                      style="width: 110px;"
                        v-model="item.speed"
                        :disabled="detailsDisabled"
                      ></el-input>
                    </div>
                    <div>
                      目标置信度
                      <el-input
                        v-model="item.eventConfidence"
                        :disabled="detailsDisabled"
                      ></el-input>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="dialogFooterButton" >
        <div @click="submitDialog" v-show="detailsButtonType == 2">复核提交</div>
        <div v-show="detailsButtonType == 2 && activeName == '0'" @click="management(eventForm.id)">应急调度</div>
        <div v-show="detailsButtonType == 2 && activeName == '1'" @click="openProcess(1,eventForm.id)">处置</div>

      </div>
    </el-dialog>
    <!--    流程弹窗-->
    <el-dialog
      title="预警处置"
      :visible.sync="processDialog"
      width="480px"
      append-to-body
      class="animationDialog"
      :modal="false"
      :before-close="cancelProcessDialog"
      :close-on-click-modal="closeProcessDialog"
    >
      <div style="padding: 10px; background: #f7f7f7; height: 686px;overflow:auto">
        <div v-for="(item,index) of incHandList" :key="index" class="incHandContent" v-if="activeName == '1'">
          <div class="classification">
            <div class="type"
                  :style="{
            padding:item.flowContent?item.flowContent.toString().length>2?'8px':'15px 12px':'',
            marginTop:item.children?item.flowContent == '设备联控' ? (item.children.length * 40 + (4 * (item.children.length - 1)))/2 - 35 +'px':(item.children.length * 40 + (4 * (item.children.length - 1)))/2 - 25 +'px':''

            }"
            v-if="item.flowContent">{{item.flowContent}}
            </div>

            <div v-show="item.flowId == 7" class="yijian"  @click="getYiJian(item)"
            :class="!miniDialog?'disabledButton':''">一键</div>
            <div v-show="item.flowId == 1" class="hulue" @click="hulue()"
            :class="!miniDialog?'disabledButton':''">忽略</div>

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
              <img :src="incHand2"  style="float:right;" v-show="itm.eventState != '0'" >
              <img :src="incHand1"  style="float:right;cursor: pointer;" v-show="itm.eventState == '0'" @click="changeIncHand(itm)"
              :class="!miniDialog?'disabledButton':''">

            </div>
          </div>

        </div>
        <div v-if="activeName == '0'">
          <el-timeline style="height: calc(100% - 40px); overflow: auto">
            <el-timeline-item
              placement="top"
              v-for="(item, index) in eventWarnList"
              :key="index + item.flowTime"
            >
              <div>{{ item.flowDescription }}</div>
              <div>{{ item.flowTime }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
    <!-- 添加或修改故障清单对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1200px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <div class="topTxt">故障基本信息</div>
          </el-col>
          <el-col :span="8" :style="{ display: 'none' }">
            <el-form-item label="故障id" prop="id" :style="{ display: 'none' }">
              <el-input v-model="form.id" placeholder="请输入发现源" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所在路段隧道" prop="tunnelId">
              <el-select
                v-model="form.tunnelId"
                :disabled="disstate"
                placeholder="请选择所属隧道"
              >
                <el-option
                  v-for="item in tunnelList"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="故障类型" prop="faultType">
              <el-select
                v-model="form.faultType"
                :disabled="disstate"
                placeholder="请选择故障类型"
              >
                <el-option
                  v-for="item in faultTypeOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障发现源" prop="faultSource">
              <el-input
                :disabled="disstate"
                v-model="form.faultSource"
                placeholder="请输入发现源"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障发现时间" prop="faultFxtime">
              <el-date-picker
                clearable
                size="small"
                :disabled="disstate"
                v-model="form.faultFxtime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择故障发现时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障持续时间" prop="faultCxtime">
              <el-input
                :disabled="disstate"
                v-model="form.faultCxtime"
                style="width: 15.5em"
                placeholder="请按照天/小时/分格式填写"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="故障填报时间" prop="faultTbtime">
              <el-date-picker
                clearable
                size="small"
                :disabled="disstate"
                v-model="form.faultTbtime"
                style="width: 268px"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择故障填报时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <div class="topTxt">故障设备情况</div>
          </el-col>
          <el-col :span="8">
            <el-form-item label="设备名称" prop="eqId">
              <el-select
                v-model="form.eqId"
                :disabled="disstate"
                placeholder="请选择设备名称"
                @change="eqStatusGet"
              >
                <el-option
                  v-for="item in eqListData"
                  :key="item.eqId"
                  :label="item.eqName"
                  :value="item.eqId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="设备填报状态" prop="eqStatus">
              <el-select
                v-model="form.eqStatus"
                :disabled="disstate"
                placeholder="请选择设备填报状态"
              >
                <el-option
                  v-for="item in eqStatusList"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障位置" prop="faultLocation">
              <el-input
                ref="faultLocation"
                :disabled="disstate"
                v-model="form.faultLocation"
                placeholder="请输入故障位置"
              />
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="设备运行状态" prop="eqRunStatus">
              <el-input
                v-model="form.eqRunStatus"
                :disabled="disstate"
                placeholder=""
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <div class="topTxt">故障描述</div>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障代码" prop="faultCode">
              <el-input
                v-model="form.faultCode"
                :disabled="disstate"
                placeholder="请输入故障代码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障等级" prop="faultLevel">
              <el-select
                v-model="form.faultLevel"
                :disabled="disstate"
                placeholder="请选择故障等级"
              >
                <el-option
                  v-for="dict in dict.type.fault_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
                <!--                <el-option
                                  v-for="item in faultLevelOptions"
                                  :key="item.dictValue"
                                  :label="item.dictLabel"
                                  :value="item.dictValue"
                                />-->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="故障消除状态" prop="falltRemoveStatue">
              <el-select
                v-model="form.falltRemoveStatue"
                :disabled="disstate"
                placeholder="请选择消除状态"
              >
                <el-option
                  v-for="item in faultRemoveStateOptions"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="故障描述" prop="faultDescription">
              <el-input
                v-model="form.faultDescription"
                maxlength="250"
                :disabled="disstate"
                placeholder="请输入故障描述"
                style="width: 80%"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="现场图片" label-width="120px">
              <el-upload
                ref="upload"
                action="http://xxx.xxx.xxx/personality/uploadExcel"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :http-request="uploadFile"
                :file-list="fileList"
                :disabled="disstate"
                :on-exceed="handleExceed"
                :on-change="handleChange"
              >
                <i class="el-icon-plus"></i>
              </el-upload>
              <el-dialog
                :visible.sync="dialogVisible"
                class="modifyEqTypeDialog"
                :append-to-body="true"
                style="width: 600px !important; margin: 0 auto"
              >
                <img width="100%" :src="dialogImageUrl" alt="" />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="isWritable" type="primary" @click="submitForm"
          >仅保存</el-button
        >
        <el-button v-if="isWritable" type="primary" @click="publishForm"
          >保存并发布</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="record" width="70%">
      <div style="text-align: center; font-size: 18px">故障检修记录</div>
      <div class="card" v-if="news.length > 0" v-for="(item,index) in news" :key="index">
        <div class="card-col" style="font-size: 16px">
          <div>
            巡检时间:
            <span>{{ item.xcTime }}</span>
          </div>
          <div>
            检修班组:
            <span>{{ item.bzId }}</span>
          </div>
          <div>
            检修人:
            <span>{{ item.walkerId }}</span>
          </div>
        </div>
        <div class="card-col" style="font-size: 16px">
          <div>
            外观情况:
            <span>{{ item.impression }}</span>
          </div>
          <div>
            网络情况:
            <span>{{ item.network }}</span>
          </div>
          <div>
            配电情况:
            <span>{{ item.power }}</span>
          </div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          <div>
            设备运行状态:
            <span style="margin: 6%">设备状态:{{ item.eqStatus }}</span
            ><span> 设备运行状态:{{ item.runStatus }}</span>
          </div>
          <div class="col-test">(检修时检测情况)</div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          <div>
            现场故障情况:
            <span>{{ item.eqFaultDescription }}</span>
          </div>
          <div class="col-test">(检修时检测情况)</div>
        </div>
        <div class="card-cols" style="font-size: 16px">
          现场图片:
          <div v-for="(pic,index) in item.iFileList" :key="index">
            <img :src="pic.imgUrl" :title="pic.imgName" />
          </div>
        </div>
      </div>
      <div v-if="news.length == 0">
        <div style="text-align: center; margin-top: 50px; margin-bottom: 50px">
          暂无记录
        </div>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="picUrlDialog" width="70%" title="事件视频" class="videoDialog">
      <div class="videoDialogClass">
        <video :src="videoUrl" controls muted loop fluid autoplay></video>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEvent,
  getEvent,
  delEvent,
  addEvent,
  updateEvent,
  toll,
  getTunnelList,
  getTunnelLane,
  getHandle,
  implementProcess,
  implementPlan,
  updateHandle,
  eventFlowList,
  getSafetyHandle,
  implementDisposalStrategy,
  implementDisposalStrategyRl,
} from "@/api/event/event";
import {
  addList,
  delList,
  getEquipmentInfo,
  getList,
  getRepairRecordList,
  listList,
  updateList,
} from "@/api/electromechanicalPatrol/faultManage/fault";
import { listEventType, getTodayEventCount } from "@/api/event/eventType";
import { listPlan } from "@/api/event/reservePlan";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { image, video, getEventCamera } from "@/api/eventDialog/api.js";
import { listEventFlow, getListBySId } from "@/api/event/eventFlow";
import { treeselect, treeselectExcYG1 } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listType, loadPicture } from "@/api/equipment/type/api";
import { listBz } from "@/api/electromechanicalPatrol/taskManage/task";
import { listDevices, videoStreaming } from "@/api/equipment/eqlist/api";
import videoPlayer from "@/views/event/vedioRecord/myVideo.vue";

export default {
  name: "Event",
  dicts: [
    "sd_direction",
    "fault_type",
    "fault_level",
    "fault_remove_statue",
    "sd_monitor_state",
    "fault_status",
    "impression",
    "network",
    "power",
  ],
  //字典值：故障类型、故障等级，故障消除状态
  components: { Treeselect, videoPlayer},
  data() {
    return {
      picUrlDialog:false,
      eventWarnList: [],
      miniDialog: true,
      eventTypeId: "",
      evtId: "",
      incHand1: require("@/assets/cloudControl/incHand1.png"),
      incHand2: require("@/assets/cloudControl/incHand2.png"),
      incHandList: [],
      setDisabled: {
        disabledDate(time) {
          return time.getTime() > Date.now(); // 可选历史天、可选当前天、不可选未来天
          // return time.getTime() > Date.now() - 8.64e7;  // 可选历史天、不可选当前天、不可选未来天
          // return time.getTime() < Date.now() - 8.64e7;  // 不可选历史天、可选当前天、可选未来天
          // return time.getTime() < Date.now(); // 不可选历史天、不可选当前天、可选未来天
        },
      },
      detailsButtonType: 1,
      eqStatusList: [],
      directionList: [],
      direction: "",
      fromList: [],
      closeProcessDialog: false,
      processDialog: false,
      processType: false,
      evtCar: [
        {
          carNum: "鲁A88888",
          speed: "100km/h",
          percentage: "80%",
        },
        {
          carNum: "鲁A88888",
          speed: "100km/h",
          percentage: "80%",
        },
      ],
      detailsDisabled: false,
      laiyuanList: [
        {
          label: "雷视融合",
          value: 1,
        },
      ],
      videoUrl: '',
      safeWarn0: require("@/assets/cloudControl/safeWarn0.png"),
      safeWarn1: require("@/assets/cloudControl/safeWarn1.png"),
      safeWarn2: require("@/assets/cloudControl/safeWarn2.png"),
      safeWarn3: require("@/assets/cloudControl/safeWarn3.png"),
      // 一键车道指示器 车道下拉框
      chezhiLaneList: [],
      chezhiLaneList1: [
        {
          laneId: "1",
          laneName: "一车道",
        },
      ],
      chezhiLaneList2: [
        {
          laneId: "1",
          laneName: "一车道",
        },
        {
          laneId: "2",
          laneName: "二车道",
        },
      ],
      chezhiLaneList3: [
        {
          laneId: "1",
          laneName: "一车道",
        },
        {
          laneId: "2",
          laneName: "二车道",
        },
        {
          laneId: "3",
          laneName: "三车道",
        },
      ],

      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      activeName: "1",
      tabList: [
        {
          title: "主动安全",
          name: "1",
        },
        {
          title: "交通事件",
          name: "0",
        },
        {
          title: "设备故障",
          name: "2",
        },
      ],
      sClick: true,
      manageStation: this.$cache.local.get("manageStation"),
      manageStationSelect: this.$cache.local.get("manageStationSelect"),
      //检修记录弹出窗
      record: false,
      dialogEventList: [],
      eventMsg: {
        allnum: 0,
        process: 0,
        bl: 0,
      },

      // 管理机构
      mechanism: [],
      // 所属隧道
      tunnelList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      fileList: [],
      removeIds: [],
      // 遮罩层
      loading: true,
      //巡查班组
      fileData: "", // 文件上传数据（多文件合一）
      bzData: {},
      // 弹出层是否可写
      isWritable: true,
      // 是否不可点击
      disstate: false,
      // 显示搜索条件
      showSearch: true,
      //事件类型
      eventTypeData: {},
      // 总条数
      total: 0,
      // 事件管理表格数据
      eventList: [],
      //
      searchValue: "1",
      // 弹出层标题
      title: "",
      // 状态字典
      eventStateOptions: [],
      // 级别 字典
      eventGradeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 16,
        tunnelId: null,
        eventTypeId: null,
        eventTitle: null,
        eventTime: null,
        eventState: null,
        eventGrade: null,
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
        startTime: null,
        endTime: null,
        deptId: null,
      },
      allmsg: "",
      process: "",
      proportion: "",
      // 表单参数
      form: {},
      //故障类型
      faultTypeData: {},
      //所属隧道
      eqTunnelData: {},
      //设备类型
      eqTypeData: {},
      //设备
      eqListData: {},
      // 查询参数
      news: {
        xcTime: "",
        bzId: "",
        walkerId: "",
        impression: "",
        network: "",
        power: "",
        eqStatus: "",
        runStatus: "",
        eqFaultDescription: "",
      },
      pics: {
        imgUrl: "",
        imgName: "",
      },
      // 表单校验
      rules: {
        /*  tunnelId: [{required: true, message: '请选择隧道名称', trigger: 'blur'}], */
        eventTitle: [
          { required: true, message: "请输入事件标题", trigger: "blur" },
        ],
        eventTypeId: [
          { required: true, message: "请选择事件类型", trigger: "change" },
        ],
        eventGrade: [
          { required: true, message: "请选择事件级别", trigger: "change" },
        ],
        eventLocation: [
          { required: true, message: "请输入位置", trigger: "blur" },
        ],
        eventDescription: [
          { required: true, message: "请输入内容", trigger: "blur" },
        ],
        faultLevel: [
          { required: true, message: "请选择故障等级", trigger: "faultLevel" },
        ],
        faultLocation: [
          {
            required: true,
            message: "请填写故障位置",
            trigger: "faultLocation",
          },
        ],
        faultType: [
          {
            required: true,
            message: "请选中故障类型",
            trigger: "faultLocation",
          },
        ],
        faultFxtime: [
          { required: true, message: "请填写发现时间", trigger: "faultFxtime" },
        ],
        faultCxtime: [
          { required: true, message: "请填写持续时间", trigger: "faultCxtime" },
        ],
        eqId: [{ required: true, message: "请填写设备名称", trigger: "eqId" }],
        eqStatus: [
          {
            required: true,
            message: "请选中设备填报状态",
            trigger: "eqStatus",
          },
        ],
        falltRemoveStatue: [
          {
            required: true,
            message: "请选中消除状态",
            trigger: "falltRemoveStatue",
          },
        ],
        falltRemoveStatu: [
          {
            required: true,
            message: "请选中消除状态",
            trigger: "falltRemoveStatue",
          },
        ],
        tunnelId: [
          {
            required: true,
            message: "请选中所在路段隧道",
            trigger: "tunnelId",
          },
        ],
      },
      // 日期范围
      dateRange: [],
      open: false,
      details: false,
      submitEventFormLoading: false,
      direction: "rtl",
      eventForm: {
        stakeNum1: "",
        stakeNum2: "",
        stakeEndNum1: "",
        stakeEndNum2: "",
        iconUrlList: [],
      },
      iconUrlListAll:[],
      imgUrlList:[],
      urls: [],
      urlsList:[],
      urlsAll:[],
      // 翻页
      arrowRight:false,
      arrowLeft:false,
      arrowRight2:false,
      arrowLeft2:false,
      imgPage:1,
      imgPage2:1,

      // 遮罩层
      dloading: false,
      // 部门树选项
      deptOptions: [],
      //检修记录故障id
      faultId: "",
      //设备填报状态
      faultTypeOptions: [], //故障类型
      faultLevelOptions: [], //故障等级
      faultRemoveStateOptions: [], //故障消除状态
      impressionOptions: [], //外观情况
      networkOptions: [], //网络情况
      powerOptions: [], //配电情况
      

      // 实时视频
      videoForm:{
        liveUrl:'',
      },
      cameraVisible:true,
    };
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "监听到隧道啦监听到隧道啦监听到隧道啦监听到隧道啦");
      this.manageStationSelect = newVal;
      this.queryParams.tunnelId = newVal;
      this.queryParams.eventTypeId = "";
      this.getList();
      this.getTunnelLane();
    },
  },
  async created() {
    // this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect");

    this.eventList = [];
    this.getTreeselect();
    this.getBz();

    await this.getList();
    await this.getEventMsg();
    this.getEventType();
    this.getTunnel();
    this.getEqType();
    this.getDevices();
    this.getTunnelLane();
    // 事件来源
    this.getDicts("sd_event_source").then((data) => {
      console.log(data,"事件来源")
      this.fromList = data.data;
    });
    this.fileData = new FormData(); // new formData对象
    //设备填报状态
    this.getDicts("sd_monitor_state").then((data) => {
      this.eqStatusList = data.data;
    });
    //故障类型
    this.getDicts("fault_type").then((response) => {
      this.faultTypeOptions = response.data;
    });
    //故障等级
    this.getDicts("fault_level").then((response) => {
      this.faultLevelOptions = response.data;
    });
    //故障消除状态
    this.getDicts("fault_remove_statue").then((response) => {
      this.faultRemoveStateOptions = response.data;
    });
    //外观情况
    this.getDicts("impression").then((response) => {
      this.impressionOptions = response.data;
    });
    //网络情况
    this.getDicts("network").then((response) => {
      this.networkOptions = response.data;
    });
    //外观情况
    this.getDicts("power").then((response) => {
      this.powerOptions = response.data;
    });
    this.getDicts("sd_event_state").then((response) => {
      this.eventStateOptions = response.data;
    });
    this.getDicts("sd_incident_level").then((response) => {
      console.log(response.data, "response.data事件级别");
      this.eventGradeOptions = response.data;
    });
    this.getDicts("sd_direction").then((response) => {
      console.log(response.data, "response.data车道方向");
      this.directionList = response.data;
    });
    // 管理机构
    toll().then((res) => {
      console.log(res);
      this.mechanism = res.data;
    });
  },
  methods: {
    // 打开图片变视频弹窗
    openPicDialog(item){
      this.videoUrl = item.videoUrl
      this.picUrlDialog = true
    },
    // 忽略
    hulue() {
      const param = {
        id: this.eventForm.id,
        eventState: 2,
      };
      updateEvent(param).then((res) => {
        console.log(res, "忽略");
        that.$modal.msgSuccess("忽略成功");
      });
    },
    // 处置记录
    getEventList() {
      eventFlowList({ eventId: this.eventForm.id }).then((res) => {
        console.log(res, "处置记录");
        this.eventWarnList = res.rows;
      });
    },
    // 单点 下发
    changeIncHand(item) {
      console.log(item, "下发");
      var that = this;
      this.$confirm(
        item.flowId == 5
          ? "是否完成?"
          : item.flowId == 6
          ? "是否上报?"
          : item.flowPid == 8
          ? "是否通知？"
          : "是否确认执行?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(function () {
        if (item.flowPid == "7") {
          let rlId = item.processId;
          let eventId = that.eventForm.id;
          implementDisposalStrategyRl(eventId, rlId).then((response) => {
            console.log(response, "单点下发");
            that.$modal.msgSuccess("状态修改成功");
            that.evtHandle();
          });
        } else {
          const params = {
            id: that.eventForm.id,
            ids: item.id,
          };
          updateHandle(params).then((res) => {
            console.log(res, "单点改状态");
            console.log(that.incHandList, "this.incHandList");
            for (let itt of that.incHandList) {
              if (itt.children) {
                for (let itm of itt.children) {
                  if (itm.id == item.id) {
                    itm.eventState = "1";
                    that.$modal.msgSuccess("状态修改成功");
                  }
                }
              }
            }
            that.evtHandle();
          });
        }
      });
    },
    // 事件处置 一键
    getYiJian(item) {
      console.log(item, "一键");
      var that = this;
      // let str = ''
      let arr = [];
      for (let itm of item.children) {
        arr.push(itm.id);
      }
      // str = arr.join(',')

      this.$confirm("是否确认执行?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        let strategyId = item.reserveId;
        let eventId = that.eventForm.id;

        implementDisposalStrategy(eventId, strategyId).then((response) => {
          console.log(response, "一键下发成功");
          for (let item of that.incHandList) {
            for (let itm of item.children) {
              for (let it_m of arr) {
                if (itm.id == it_m) {
                  itm.eventState = "1";
                  that.$modal.msgSuccess("一键下发成功");
                }
              }
            }
          }
        });
      });
    },
    // 处置
    management(id) {
      const param = {
        id: id,
        eventState: "0",
      };
      updateEvent(param).then(() => {
        this.$modal.msgSuccess("开始处理事件");
      });
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: id },
      });
      //
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.queryParams.pageSize = val;

      this.getList();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.queryParams.pageNum = val;
      this.getList();
    },
    // 复核提交
    submitDialog() {
      for (let item of this.eventForm.confidenceList) {
        if (
          !/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/.test(
            item.plate
          )
        ) {
          this.$modal.msgWarning("请输入正确车牌号");
          return;
        }
      }
      if (this.eventForm.stakeNum1 && this.eventForm.stakeNum2) {
        this.eventForm.stakeNum =
          "K" + this.eventForm.stakeNum1 + "+" + this.eventForm.stakeNum2;
      }
      if (this.eventForm.stakeEndNum1 && this.eventForm.stakeEndNum2) {
        this.eventForm.stakeEndNum =
          "K" + this.eventForm.stakeEndNum1 + "+" + this.eventForm.stakeEndNum2;
      }
      // delete this.eventForm['confidenceList'];
      console.log(this.eventForm, "888888888888888888");
      this.eventForm.searchValue = this.activeName;
      updateEvent(this.eventForm).then((response) => {
        this.processDialog = false;
        this.closeProcessDialog = false;
        this.processType = false;
        this.details = false;
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
    },
    // 获取车道数
    getTunnelLane() {
      getTunnelLane(this.manageStationSelect).then((res) => {
        console.log(res.data.lane, "车道数");
        this.chezhiLaneList = [];
        if (res.data.lane == 1) {
          this.chezhiLaneList = this.chezhiLaneList1;
        } else if (res.data.lane == 2) {
          this.chezhiLaneList = this.chezhiLaneList2;
        } else if (res.data.lane == 3) {
          this.chezhiLaneList = this.chezhiLaneList3;
        }
      });
    },
    getFrom(from) {
      for (let item of this.fromList) {
        if (from == item.dictValue) {
          return item.dictLabel;
        }
      }
    },
    // 关闭流程弹窗
    cancelProcessDialog() {
      this.processDialog = false;
      this.processType = false;
    },
    openProcess(type,id) {
      console.log(type, "00000000000000");
      if (type && id) {
        this.processType = true;
        this.processDialog = true;
        const param = {
          id: id,
          eventState: 0,
        };
        updateEvent(param).then((res) => {
          console.log(res, "处理中");
          this.$modal.msgSuccess("正在处理");
          this.evtHandle();
        });
      } else {
        if (this.processType == true) {
          this.processDialog = false;
          // this.details = true;
          // this.closeProcessDialog = false;
          this.processType = false;
        } else {
          this.processType = true;
          this.processDialog = true;
          // this.details = true;
          // this.closeProcessDialog = true;
        }
      }
    },
    // 事件处置
    evtHandle() {
      const param = {
        tunnelId: this.tunnelId,
        id: this.evtId,
        eventTypeId: this.eventTypeId,
        direction: this.direction,
      };
      getSafetyHandle(param).then((res) => {
        let list = this.handleTree(res.data, "flowId", "flowPid");
        console.log(list, "999999999999999999");
        //  for(let item of list){
        //   console.log(item.flowContent.toString().length,"555555555555555")
        //  }
        this.incHandList = list;
        this.$forceUpdate();
      });
    },
    //详情弹窗
    detailsButton(item, type) {
      if (type == 1) {
        this.miniDialog = false;
        this.detailsDisabled = true;
        this.detailsButtonType = 1;
      } else {
        this.miniDialog = true;
        this.detailsDisabled = false;
        this.detailsButtonType = 2;
      }
      console.log(item, "点击弹窗");
      this.eventTypeId = item.eventTypeId;
      this.evtId = item.id;
      this.tunnelId = item.tunnelId;
      this.direction = item.direction;

      this.details = true;
      this.eventForm = item;

      // 图片分组翻页
      if(this.eventForm.iconUrlList.length>4){
        this.arrowRight = true
        for(let i=0;i<this.eventForm.iconUrlList.length;){
          this.iconUrlListAll.push(this.eventForm.iconUrlList.splice(0,4))
        }
        this.imgUrlList = this.iconUrlListAll[0]
        this.imgPage = 0
      }else{
        this.arrowRight = false
        this.imgUrlList = this.eventForm.iconUrlList
      }

      this.getEventList();
      if (item.stakeNum) {
        this.$set(
          this.eventForm,
          "stakeNum1",
          item.stakeNum.split("+")[0].substr(1)
        );
        this.$set(this.eventForm, "stakeNum2", item.stakeNum.split("+")[1]);
      }
      if (item.stakeEndNum) {
        this.$set(
          this.eventForm,
          "stakeEndNum1",
          item.stakeEndNum.split("+")[0].substr(1)
        );
        this.$set(
          this.eventForm,
          "stakeEndNum2",
          item.stakeEndNum.split("+")[1]
        );
      }
      this.title = item.eventTitle;
      // 获取实时视频
      this.getVideoUrl(item)
      // 获取实时视频截图
      this.getImgUrl(item)
      
    },
    getImgUrl(item){
      const param = {
        businessId: item.id,
      };
      image(param).then((response) => {
        console.log(response.data, "获取图片");
        this.urls = response.data;
        if(response.data.length > 4){
          this.arrowRight2 = true
          for(let i=0;i<this.urls.length;){
            this.urlsAll.push(this.urls.splice(0,4))
          }
          this.urlsList = this.urlsAll[0]
          this.imgPage2 = 0
        }else{
          this.arrowRight2 = false
          this.urlsList = this.urls
        }
      });
    },
    getVideoUrl(item){
      console.log(item,"item")
      console.log(item.stakeNum,"item.stakeNum")

      this.cameraPlayer = false
      getEventCamera(
        item.tunnelId,
        item.stakeNum,
        item.direction
      ).then((res) => {
        console.log(res,"获取实时视频上游相机")
        if(res.data){
          let videoId = res.data[0].eqId
          videoStreaming(videoId).then((response) =>{
            console.log(response,"视频流");
            if(response.code == 200){
              this.videoForm = response.data
              this.cameraPlayer = true
            }
          }).catch((e)=>{
            this.$modal.msgWarning("获取视频失败");
          })
        }
      });
    },
    turnLeft(){
      this.arrowRight = true
      this.imgPage --
      this.imgUrlList = this.iconUrlListAll[this.imgPage]
      if(this.imgPage == 0){
        this.arrowLeft = false
      }
    },
    turnRight(){
      this.imgPage ++
      this.imgUrlList = this.iconUrlListAll[this.imgPage]
      this.arrowLeft = true
      if(this.imgPage == this.iconUrlListAll.length-1){
        this.arrowRight = false
      }
    },
    turnLeft2(){
      this.arrowRight2 = true
      this.imgPage2 --
      this.urlsList = this.urlsAll[this.imgPage2]
      if(this.imgPage2 == 0){
        this.arrowLeft2 = false
      }
    },
    turnRight2(){
      this.imgPage2 ++
      this.urlsList = this.urlsAll[this.imgPage2]
      this.arrowLeft2 = true
      if(this.imgPage2 == this.urlsAll.length-1){
        this.arrowRight2 = false
      }
    },
    //选事件类型
    handleEvtButton(item) {
      console.log(item);
      this.queryParams.eventTypeId = item;
      this.getList();
    },
    //导出
    handleExport() {},
    //切换tab页
    handleClick(e) {
      console.log(e);
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 16

      this.getList();
      this.getEventType();
    },
    handleSelectionChange(val) {
      this.ids = val.map((item) => item.id);
      this.single = val.length !== 1;
      this.multiple = !val.length;
    },
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions,"00000000000000");
      });
    },
    eqStatusGet(e) {
      getEquipmentInfo({ eqId: e }).then((response) => {
        this.form.faultLocation = "";
        this.form.eqRunStatus = "";
        this.form.eqStatus = "";
        debugger;
        if (response.data.length != 0) {
          this.form.faultLocation = response.data[0].pile;
          this.form.eqRunStatus = response.data[0].runStatus;
          this.form.eqStatus = response.data[0].eqStatus;
          //this.$refs(this.form, "eqStatus", 1);
        }
        // this.$modal.msgSuccess("修改成功");
        // this.open = false;
        // this.getList();
      });
    },
    // // 切换按钮
    // qiehuan(inx) {
    //   this.queryParams.eventTypeId = "";
    //   this.queryParams.searchValue = inx;
    //   this.searchValue = inx;
    //   this.getEventType();
    //   this.getList();
    // },
    accidentInit(eventId) {
      var eventId = { eventId: eventId };
      listEventFlow(eventId).then((result) => {
        this.dialogEventList = result.rows;
        console.log(this.dialogEventList);
      });
    },
    /** 巡查班组 */
    getBz() {
      listBz().then((response) => {
        this.bzData = response.rows;
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 设备 */
    getDevices() {
      listDevices({
        fEqId: this.form.codePlcId,
        eqType: this.form.deviceTypeId,
      }).then((response) => {
        this.eqListData = response.rows;
      });
    },
    changeState(row, state) {
      this.$confirm("是否确认忽略此事件！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        row.eventState = state;
        return updateEvent(row);
      });
    },
    getEventMsg() {
      // 获取事件预警信息
      getTodayEventCount().then((result) => {
        console.log(result, "11111111111111");
        this.eventMsg = result.data;
        this.$forceUpdate();
      });
    },
    //获取图片视频
    // getUrl(id) {
    //   const param3 = {
    //     businessId: id,
    //   };
    //   const param4 = {
    //     id: id,
    //   };
    //   image(param3).then((response) => {
    //     console.log(response.data.length);
    //     if (response.data.length >= 1) {
    //       this.urls = response.data;
    //     }
    //   });
    //   video(param4).then((response) => {
    //     console.log(response.data, "视频信息");
    //     this.videoUrl = response.data.videoUrl;
    //   });
    // },
    // 查询方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },

    // 根据管理机构筛选所属隧道
    changeMechanism(item) {
      this.queryParams.tunnelId = null;
      const param = {
        deptId: item.id,
      };
      getTunnelList(param).then((res) => {
        console.log(res, "根据管理机构筛选所属隧道");
        this.tunnelList = res.data;
      });
    },
    /** 查询事件管理列表 */
    getList() {
      // console.log(this.activeName, "9999999");
      this.loading = true;
      this.eventList = []

      // console.log(this.manageStation, "this.manageStation");
      if (this.manageStation == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      console.log(this.queryParams, "666666666666666666666");

      if (this.activeName == "2") {
        this.queryParams.pageSize = 10
        listList(this.queryParams).then((response) => {
          console.log(response,"列表内容")
          this.eventList = response.rows;
          this.eventList.forEach((item) => {
            if (item.faultLocation == "null") {
              item.faultLocation = "";
            }
            if (item.faultCxtime == "null") {
              item.faultCxtime = "";
            }
            if (item.faultCode == "null") {
              item.faultCode = "";
            }
          });
          this.total = response.total;
          this.loading = false;
        });
      } else {
        if (!this.dateRange) {
          this.dateRange = [];
        }
        this.queryParams.pageSize = 16
        this.queryParams.startTime = this.dateRange[0];
        this.queryParams.endTime = this.dateRange[1];
        this.queryParams.searchValue = this.activeName;
        listEvent(this.queryParams).then((response) => {
          console.log(response, "查询事件管理列表");
          for(let item of response.rows){
            if(item.iconUrlList){
              for(let i=0;i<item.iconUrlList.length;i++){
                item.picUrl = item.iconUrlList[0].imgUrl
              }
            }
          }
          this.eventList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      }
    },
    // getList() {
    //   this.loading = true;
    //   if(!this.dateRange){
    //     this.dateRange = []
    //   }
    //   this.queryParams.startTime = this.dateRange[0];
    //   this.queryParams.endTime = this.dateRange[1];
    //   listEvent(this.addDateRange(this.queryParams)).then((response) => {
    //     console.log(response.rows, "查询事件管理列表");
    //     this.eventList = response.rows;
    //     this.$nextTick(() => {
    //       this.$refs.tableRef.doLayout();
    //     });
    //     this.total = response.total;
    //     this.loading = false;
    //   });
    // },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除故障清单编号为"' + ids + '"的数据项？')
        .then(function () {
          return delList(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 所属隧道 */
    getTunnel() {
      if (!this.queryParams.deptId) {
        listTunnels().then((response) => {
          console.log(response.rows, "所属隧道");
          this.tunnelList = response.rows;
        });
      }
    },
    /** 查询事件类型列表 */
    getEventType() {
      let prevControlType = { prevControlType: this.activeName };
      listEventType(prevControlType).then((response) => {
        console.log(response, "responseresponse1111");
        this.eventTypeData = response.rows;
      });
    },
    // 状态字典翻译
    eventStateFormat(row, column) {
      return this.selectDictLabel(this.eventStateOptions, row.eventState);
    },
    // 级别 字典翻译
    eventGradeFormat(row, column) {
      return this.selectDictLabel(this.eventGradeOptions, row.eventGrade);
    },
    /** 已解决按钮操作 */
    handleOk(row) {
      // const ids = row.id || this.ids;
      this.$confirm("是否确认此事件已解决！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          row.eventState = "1";
          return updateEvent(row);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("解决成功");
        })
        .catch(function () {});
    },
    /** 进入应急指挥大屏 */
    loadBigScreen(row) {
      alert("正在研发中...");
    },
    /** 新增按钮操作 */
    handleGzAdd() {
      this.reset();
      this.isWritable = true;
      this.disstate = false;
      this.open = true;
      this.title = "添加故障清单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      let that = this;
      this.isWritable = true;
      this.activeName = "2";
      that.reset();
      const id = row.id || that.ids;
      getList(id).then((response) => {
        this.form = response.data;
        if (this.form.faultSource == "null") {
          this.form.faultSource = "";
        }
        if (this.form.eqRunStatus == "undefined") {
          this.form.eqRunStatus = "";
        }
        if (this.form.faultCode == "null") {
          this.form.faultCode = "";
        }
        if (this.form.faultDescription == "null") {
          this.form.faultDescription = "";
        }
        /*console.log(
          "that.form.iFileList====================" + that.form.iFileList.length
        );*/
        that.planRoadmapUrl(that.form.iFileList);
        this.disstate = false;

        this.open = true;
        this.title = "修改故障清单";
      });
    },
    handleCheckDetail(row) {
      let that = this;
      this.isWritable = false;
      that.reset();
      const id = row.id || that.ids;
      getList(id).then((response) => {
        this.form = response.data;
        if (this.form.faultSource == "null") {
          this.form.faultSource = "";
        }
        if (this.form.eqRunStatus == "undefined") {
          this.form.eqRunStatus = "";
        }
        if (this.form.faultCode == "null") {
          this.form.faultCode = "";
        }
        if (this.form.faultDescription == "null") {
          this.form.faultDescription = "";
        }
        that.planRoadmapUrl(that.form.iFileList);
        this.disstate = true;
        this.open = true;
        this.title = "故障详情";
      });
    },
    // 检修记录按钮操作
    recordQuery(row) {
      this.record = true;
      this.faultId = row.id;
      let that = this;
      getRepairRecordList(this.faultId).then((response) => {
        that.news = response.data;
        that.impressionOptions.forEach((opt) => {
          if (opt.dictValue == "0") {
            that.news[0].impression = opt.dictLabel;
          }
          if (opt.dictValue == "1") {
            that.news[0].impression = opt.dictLabel;
          }
        });
        that.networkOptions.forEach((opt) => {
          if (opt.dictValue == "0") {
            that.news[0].network = opt.dictLabel;
          }
          if (opt.dictValue == "1") {
            that.news[0].network = opt.dictLabel;
          }
        });
        that.powerOptions.forEach((opt) => {
          if (opt.dictValue == "0") {
            that.news[0].power = opt.dictLabel;
          }
          if (opt.dictValue == "1") {
            that.news[0].power = opt.dictLabel;
          }
        });

        this.news.forEach((taskitem) => {
          this.bzData.forEach((opt) => {
            if (taskitem.bzId == opt.deptId) {
              taskitem.bzId = opt.deptName;
            } else {
              taskitem.bzId = "";
            }
            if (taskitem.bzId == null || taskitem.bzId == "null") {
              taskitem.bzId = "";
            }
          });
        });
      });
    },
    // 关闭弹窗
    close() {
      // 关闭弹出层
      this.record = false;
    },
    handleDetails(row) {
      this.details = true;
      this.title = "事件详情";
      this.eventForm = row;
      this.accidentInit(row.id);
      this.getUrl(row.id);
      console.log(row, "事件详情row");
    },
    handleDispatch(row) {
      console.log(row);
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: {
          id: row.id,
          tunnelId: row.tunnelId,
          stakeNum: row.stakeNum,
          direction: row.direction,
        },
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        tunnelId: null,
        eventTypeId: null,
        eventTitle: null,
        eventTime: null,
        eventState: null,
        eventGrade: "0",
        eventLocation: null,
        eventDeath: null,
        eventInjured: null,
        eventDescription: null,
        reservePlanId: null,
        flowId: null,
        warningId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        faultLocation: null,
        faultType: null,
        faultSource: null,
        faultFxtime: null,
        faultCxtime: null,
        eqTunnelId: null,
        faultTbr: null,
        faultTbtime: null,
        eqId: null,
        eqStatus: null,
        faultCode: null,
        faultLevel: null,
        falltRemoveStatue: null,
        faultDescription: null,
        faultStatus: 0,
      };
      this.fileList = [];
      this.removeIds = [];
      this.resetForm("form");
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //删除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      // let num = this.direction == 0 ? 2 : 1;
      this.$message.warning("限制上传图标个数不超过2个");
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    async planRoadmapUrl(iFileList) {
      let that = this;
      that.fileList = [];
      if (iFileList != null) {
        for (let i = 0; i < iFileList.length; i++) {
          let iconName = iFileList[i].imgName;
          // let iconUrl = await that.picture(iFileList[i].url);
          let iconUrl = iFileList[i].imgUrl;
          that.fileList.push({
            name: iconName,
            url: iconUrl,
            fId: iFileList[i].businessId,
          });
        }
      }
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
    openImg(url) {
      this.img = url;
      this.yn = !this.yn;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = { pageNum: 1, pageSize: 16 };
      this.dateRange = [];
      this.tunnelList = [];
      this.queryParams.eventTypeId = "";
      // this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isWritable = true;
      this.disstate = false;
      this.open = true;
      this.title = "添加故障清单";
    },
    //关闭drawer
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    /** 提交按钮 */
    submitEventForm() {
      this.dloading = true;
      if (this.submitEventFormLoading) return;
      this.submitEventFormLoading = true;
      this.$refs["form1"].validate(async (valid) => {
        if (valid) {
          await addEvent(this.eventForm).then((response) => {
            if (response.code === 200) {
              this.$modal.msgSuccess("新增成功");
              setTimeout(() => {
                this.resetEvent();
                this.dloading = false;
                this.open = false;
              }, 400);
              this.getList();
            }
          });
        }
        this.submitEventFormLoading = false;
      });
    },
    //关闭弹窗
    eventFormClose() {
      this.resetEvent();
      this.open = false;
    },

    /** 提交按钮 */
    submitForm() {
      debugger;
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("id", this.form.id);
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("faultType", this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("faultTbtime", this.form.faultTbtime);
      this.fileData.append("eqId", this.form.eqId);
      this.fileData.append("eqStatus", this.form.eqStatus);
      this.fileData.append("faultLocation", this.form.faultLocation);
      this.fileData.append("eqRunStatus", this.form.eqRunStatus);
      this.fileData.append("faultCode", this.form.faultCode);
      this.fileData.append("faultLevel", this.form.faultLevel);
      this.fileData.append("falltRemoveStatue", this.form.falltRemoveStatue);
      this.fileData.append("faultDescription", this.form.faultDescription);
      this.fileData.append("faultStatus", 1);
      /*      if(this.fileList.length <= 0) {
                this.fileData.append("file", -1);
              }else{
                console.log("================"+this.fileList)
                return
              }*/
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            this.fileData.append("removeIds", this.removeIds);
            if (this.isClick) {
              updateList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            }
          } else {
            if (this.isClick) {
              addList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        }
      });
      setTimeout(() => {
        this.isClick = true;
      }, 500)
    },
    publishForm() {
      this.fileData = new FormData(); // new formData对象
      this.$refs.upload.submit(); // 提交调用uploadFile函数
      this.fileData.append("id", this.form.id);
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("faultType", this.form.faultType);
      this.fileData.append("faultSource", this.form.faultSource);
      this.fileData.append("faultFxtime", this.form.faultFxtime);
      this.fileData.append("faultCxtime", this.form.faultCxtime);
      this.fileData.append("faultTbtime", this.form.faultTbtime);
      this.fileData.append("eqId", this.form.eqId);
      this.fileData.append("eqStatus", this.form.eqStatus);
      this.fileData.append("faultLocation", this.form.faultLocation);
      this.fileData.append("eqRunStatus", this.form.eqRunStatus);
      this.fileData.append("faultCode", this.form.faultCode);
      this.fileData.append("faultLevel", this.form.faultLevel);
      this.fileData.append("falltRemoveStatue", this.form.falltRemoveStatue);
      this.fileData.append("faultDescription", this.form.faultDescription);
      this.fileData.append("faultStatus", 0);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            this.fileData.append("removeIds", this.removeIds);
            if (this.isClick) {
              updateList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            }
          } else {
            if (this.isClick) {
              addList(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        }
      });
      setTimeout(() => {
        this.isClick = true;
      }, 500)
    },
    // 表单重置
    resetEvent() {

      this.$refs.form1.resetFields();
      this.eventForm.eventTypeId = null;
      this.eventForm.eventInjured = null;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.details = false;
      this.processDialog = false;
      this.processType = false;
      this.reset();
      if(this.detailsButtonType == 2){
        this.getList();
      }
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
  },
};
</script>
<style scoped lang="scss">
.formStyle {
  .el-form-item {
    margin-bottom: 1vh;
    
  }
}
::v-deep .el-form-item--medium .el-form-item__label{
  line-height: 3vh;
  font-size: 0.7vw;
}
::v-deep .el-form-item--medium .el-form-item__content{
  line-height: 3vh;
  font-size: 0.7vw;
}
::v-deep .el-input--medium .el-input__icon{
  line-height: 3vh;
}
::v-deep .el-input--small .el-input__icon{
  line-height: 3vh;
}
.el-tabs__header {
  margin: 0 0 12px !important;
}
.contentListBox {
  width: 100%;
  // height: 570px;
  word-wrap: break-word;
  word-break: normal;
  //display: flex;
  .contentBox {
    width: 24%;
    // height: 135px;
    border: solid 1px #2aa6ff;
    display: inline-flex;
    margin-right: 1vw;
    margin-bottom: 5px;
    position: relative;
    .video {
      width: 200px;
      height: 100%;
      float: left;
      text-align: center;
      font-size: 0.7vw;
      // color: #2aa6ff;
      video {
        width: 100%;
        height: 100px;
        margin-top: 2px;
        float: left;
      }
      img {
        // width: 100%;
        height: 10vh;
        margin-top: 2px;
      }
    }

    .contentText {
      margin-top: 10px;
      font-size: 0.7vw;
      // color: #0087e7;
      margin-right: 20px;
      width: 213px;
      float: right;
      .stateTab {
        position: absolute;
        top: -34px;
        right: -21px;
      }
      div {
        padding: 0.4vh 0;
        span {
          padding-left: 6px;
        }
      }
      .contentButton {
        display: flex;
        justify-content: space-between;
        width: 150px;
        div {
          width: 65px;
          height: 2vh;
          border-radius: 14px;
          color: white;
          display: flex;
          justify-content: center;
          align-items: center;
          cursor: pointer;
        }
        div:nth-of-type(1) {
          background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
        }
        div:nth-of-type(2) {
          background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
        }
      }
    }
  }
  .contentBox:nth-of-type(4n) {
    margin-right: 0px;
  }
}

.videoDialogBox {
  width: 100%;
  height: 50%;
  display: flex;
  justify-content: space-between;
  position: relative;
  .processButton {
    position: absolute;
    top: 20px;
    right: -20px;
    width: 25px;
    height: 100px;
    cursor: pointer;
    background: #39adff;
    text-align: center;
    line-height: 18px;
    color: #fff;
  }
  .processButton::before {
    font-size: 14px;
    color: #fff;
  }
  .dialogBg {
    background: #f7f7f7;
    height: 100%;
    width: 49%;
    color: #0087e7;
    padding: 20px;
    span {
      color: #767676 !important;
      padding-left: 10px;
    }
    video {
      width: 100%;
      height: 70%;
    }
    .picBox {
      width: 100%;
      height: calc(30% - 20px);
      margin-top: 10px;
      // border: solid 1px red;
      display: flex;
      justify-content: center;
      align-items: center;
      .picList{
        width: 100%;
        height: 100%;
        // display: flex;
        // justify-content: left;
        >div{
          overflow: hidden;
          margin-left: 10px;
          width: 21%;
          height: 100%;
          display: inline-block;
          > img {
            width: auto;
            height: 100%;
            overflow: hidden;
            // border: solid 1px blue;
            margin: 0 auto;
          }
        }
        
     
      }
      .turnPages{
        width:20px !important;
        height:20px !important;
        border:solid 1px #0087e7;
        border-radius:10px;
        text-align: center;
        cursor: pointer;
        caret-color: rgba(0,0,0,0);
      }
      .turnPages:hover{
        background: #0087e7;
        color:#fff
      }
      .noPic{
        border:solid 1px #0087e7;
        display:flex;
        justify-content: center;
        align-items: center;
        img{
          width:50%;
        }
      }
      
      
    }
  }
}
.dialogForm {
  width: 100%;
  height: 43%;
  background: #f7f7f7;
  padding: 10px;
  margin-top: 10px;
  overflow-y: auto;
  overflow-x: hidden;
  .el-input {
    width: 100%;
    .el-input--medium .el-input__inner {
      width: 93px;
    }
  }
  .el-form-item {
    margin-bottom: 10px !important;
  }
  .evtCarStyle {
    width: 100%;
    height: 100px;
    // border: solid 1px #cfd5e0;
    padding: 10px;
    overflow-y: auto;
    padding-bottom: 0;
    > div {
      display: flex;
      margin-bottom: 5px;
      .evtNum {
        width: 35px;
        height: 35px;
        // border: solid 1px #ccc;
        text-align: center;
        line-height: 35px;
      }
      div {
        margin-left: 5px;
      }
    }
  }
}
.dialogFooterButton {
  width: 100%;
  height: 30px;
  margin-top: 10px;
  display: flex;
  justify-content: right;
  div {
    margin-right: 20px;
    width: 80px;
    height: 28px;
    border-radius: 14px;
    text-align: center;
    line-height: 28px;
    color: white;
    cursor: pointer;
  }
  div:nth-of-type(1) {
    background: linear-gradient(180deg, #ba8400 0%, #fed11b 100%);
  }
  div:nth-of-type(2) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
  div:nth-of-type(3) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
}
.detailsDialog {
  height: 92%;
  // z-index: 2008 !important;
  width: 53%;
  position: absolute;
  left: 20%;
  
}
::v-deep .el-dialog{
    height:calc(100% - 8vh) !important;
    .el-dialog__body{
      height: calc(100% - 4vh - 30px);
    }
  }
.animationDialog {
  z-index: 2008 !important;
  height: 92%;
  width: 480px;
  // transform: translateX(1330px);
  animation: mymove 0.3s linear;
  position: absolute;
  left: 66%;
}
@keyframes mymove {
  0% {
    left: 60%;
  }
  100% {
    left: 69%;
  }
}
.el-select-dropdown {
  z-index: 2010 !important;
}
.eventTypeButton {
  height: 2.6vh;
  line-height: 2.6vh;
  border-radius: 2px;
  cursor: pointer;
  padding: 0px 10px;
  font-size: 0.7vw;
}
::v-deep .vue-treeselect__control{
  height: 3vh;
}
::v-deep .vue-treeselect__placeholder, .vue-treeselect__single-value{
  line-height: 3vh;
}
::v-deep .el-input--small .el-input__inner{
  line-height: 3vh;
  height: 3vh;
  font-size: 0.7vw;
}
::v-deep .el-input--medium .el-input__inner{
  line-height: 3vh;
  height: 3vh;
  font-size: 0.7vw;
}
.butBox {
  width: 280px;
  display: flex;
  padding: 4px 4px;
  background: #9ecced;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  // justify-content: space-between;
  div {
    padding: 6px 10px;
    color: #fff;
    letter-spacing: 1px;
    cursor: pointer;
  }

  .xz {
    background: #285b8d;
    border-radius: 10px;
  }
}
.incHandContent {
  display: flex;
  color: #333333;
  font-size: 12px;
  padding: 10px;
  .classification {
    .type {
      width: 50px;
      height: 50px;
      background: #f2f8ff;
      border: 1px solid #39adff;
      text-align: center;
    }
    .yijian {
      width: 50px;
      background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
      border: 1px solid #39adff;
      color: #fff;
      text-align: center;
      transform: translateY(-2px);
      cursor: pointer;
    }
    .hulue {
      width: 50px;
      background: linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
      border: 1px solid #ebab3a;
      color: #fff;
      text-align: center;
      transform: translateY(-2px);
      cursor: pointer;
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
    background: #f2f8ff;
    border: solid 1px #39adff;
    border-radius: 3px;
    width: 300px;
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
.addClass {
  .el-select {
    width: 250px;
  }

  .el-input {
    width: 250px !important;
  }

  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 250px !important;
  }
}

.circle {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  display: inline-block;
}

.detailsText {
  display: inline-block;
  margin-left: 20px;
  line-height: 40px;
  width: 100px;
}

hr {
  border: solid 1px #ddd;
}

.rowClass {
  border-top: solid 1px #ddd;
  border-bottom: solid 1px #ddd;
  height: 40px;
  margin-top: 10px;
}

.eventClass {
  height: 30px;
  border-right: solid 1px #ddd;
  width: 100%;
  text-align: center;
  margin-top: 5px;
  line-height: 30px;
}

.eventTitleClass {
  height: 40px;
  background-color: #eeeeee;
  line-height: 40px;
  text-align: center;
}

.video {
  height: 300px;
  border-radius: 0;
  padding: 5px;
  margin-top: 0;
}

.image3 {
  padding: 5px;
  height: 49%;
  // border: solid 1px green;
  width: 100%;
}

.card-box {
  width: 30%;
  text-align: center;
  font-weight: bold;
}

.EquipStatistics {
  width: 200px;
  height: 40px;
  background-image: url(../../../assets/cloudControl/shebeiWarning.png);
  color: white;
  text-align: center;
  line-height: 40px;
  font-weight: 400;
  font-size: 16px;
  margin-left: 14px;

  > span {
    font-size: 24px;
    font-weight: 600;
    vertical-align: middle;
  }
}

.warningStatistics {
  line-height: 60px;
  font-size: 14px;
  // color: #606266;
  font-weight: 700;
}

.eventTitle {
  padding: 15px 0;
  font-size: 18px;
  font-weight: 400;
  color: #303133;
}

.card {
  position: relative;
  width: 100%;
  padding: 20px;
  margin-top: 20px;
  border-radius: 10px;
  background-color: #f0f0f0;
  .card-col {
    margin-top: 10px;
    display: flex;
    color: #79949c;
    div {
      width: 33%;
      span {
        color: black;
        margin-left: 10px;
      }
    }
  }
  .card-cols {
    margin-top: 10px;
    display: flex;
    div {
      width: 50%;
    }
    .col-test {
      text-align: right;
      color: #79949c;
    }
    img {
      width: 100px;
      margin-left: 20px;
    }
  }

  .icon {
    position: absolute;
    top: 0;
    right: 30px;
    background-image: url(../../../assets/icons/svg/u954.svg);
    background-size: 100%;
  }
}


::v-deep .el-tabs{
  height: 100%;
  .el-tabs__item {
    height: 4vh;
    font-size: 0.7vw;
  }
  .el-tabs__content {
    height: calc(100% - 5vh);
    .el-tab-pane {
      height: 100%;
      .contentListBox {
        height: 60vh;
        overflow-x: hidden;
        overflow-y: auto;
        .contentBox {
          height: 14vh;
        }
      }
    }
  }
}

.disabledButton {
  cursor: no-drop;
  pointer-events: none;
}
.video-box{
  height: 70%;
}
::-webkit-scrollbar{
  width:6px;
}
.videoDialog{
  height: 92%;
}
.videoDialogClass{
  width:100%;
  height:100%;

  video{
    width:100%;
    height:auto;
  }
}
</style>

