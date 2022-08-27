<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="隧道名称" prop="tunnelId">
            <el-select
              v-model="queryParams.tunnelId"
              placeholder="请选择隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelName"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="发生时间" prop="occurTime">
            <el-date-picker
              v-model="queryParams.params.occurTime"
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" style="margin-left: 30px;">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['trafficFlowInformation:information:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
           <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['trafficFlowInformation:information:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['trafficFlowInformation:information:remove']"
            >删除</el-button>
          </el-col>
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          plain-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          :loading="exportLoading"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['trafficFlowInformation:information:export']"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="controlList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="隧道名称" align="center" prop="tunnelName" />
          <el-table-column label="发生时间" align="center" prop="occurTime" />
          <!--<el-table-column label="统计时间" align="center" prop="reportTime" />-->
          <el-table-column label="桩号范围" align="center" >
              <template slot-scope="scope">
                  {{scope.row.stakeNumber}}+{{scope.row.stakeDistance}}
              </template>
          </el-table-column>
          <!--<el-table-column label="管控原因" align="center" prop="reasonType" >-->
              <!--<template slot-scope="scope">-->
                 <!--<dict-tag :options="dict.type.sd_road_condition" :value="scope.row.reasonType"/>-->
              <!--</template>-->
          <!--</el-table-column>-->
          <!--<el-table-column label="道路交通状况" align="center" prop="trafficCondition" />-->

          <!--<el-table-column label="原因分类" align="center" prop="reasonClass" >-->
            <!--<template slot-scope="scope">-->
              <!--<dict-tag :options="dict.type.sd_reason_class" :value="scope.row.reasonClass"/>-->
            <!--</template>-->
          <!--</el-table-column>-->
          <el-table-column label="管控类别" align="center" prop="controlType" >
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sd_control_type" :value="scope.row.controlType"/>
            </template>
          </el-table-column>
          <el-table-column label="管控等级" align="center" prop="controlLevel" >
             <template slot-scope="scope">
                 <dict-tag :options="dict.type.sd_control_level" :value="scope.row.controlLevel"/>
             </template>
          </el-table-column>
          <el-table-column label="管控措施" align="center" prop="measureDetail" >
             <!--<template slot-scope="scope">-->
                 <!--<dict-tag :options="dict.type.sd_control_measure" :value="scope.row.takeMeasure"/>-->
             <!--</template>-->
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['trafficEmergencies:emergencies:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['trafficEmergencies:emergencies:remove']"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleRelease(scope.row)"
                v-hasPermi="['trafficEmergencies:emergencies:edit']"
              >发布</el-button>
            </template>
          </el-table-column>
        </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />


        <el-dialog title="事件发布" :visible.sync="openRelease" width="500px" append-to-body class="releaseDialog">
            <el-form :model="releaseForm" :inline="true" ref='releaseForm'>
                <el-form-item label="事件信息" prop="evtInformation">
                    <el-input type="textarea" rows="3" v-model="releaseForm.evtInformation" style="width:300px;"></el-input>
                </el-form-item>
                <el-form-item label="发布渠道" prop="checkList">
                     <el-checkbox-group v-model="releaseForm.checkList">
                        <el-checkbox label="1">
                            <img src="../../../../../assets/icons/wechat.png"/>
                        </el-checkbox>
                        <el-checkbox label="2">
                            <img src="../../../../../assets/icons/weibo.png"/>
                        </el-checkbox>
                       <!--地图暂时不做-->
                        <!--<el-checkbox label="3">-->
                            <!--<img src="../../../../../assets/icons/map.png"/>-->
                        <!--</el-checkbox>-->
                      </el-checkbox-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm('release')">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body class="controlDialog">
            <el-form :model="form" ref="form" :inline="true" label-width="120px" :rules="rules">
              <el-form-item label="隧道名称" prop="incidentInfo.tunnelId">
                <el-select
                  v-model="form.incidentInfo.tunnelId"
                  placeholder="请选择隧道名称"
                  clearable
                  size="small" @change="tunnelChange"
                >
                  <el-option
                    v-for="item in tunnelData"
                    :key="item.tunnelName"
                    :label="item.tunnelName"
                    :value="item.tunnelId"
                  />
                </el-select>
              </el-form-item>
               <el-form-item label="事件发生时间" prop="incidentInfo.occurTime">
                    <el-date-picker
                          v-model="form.incidentInfo.occurTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择日期时间"
                          default-time="12:00:00">
                        </el-date-picker>
                </el-form-item>
                <el-form-item label="信息来源" prop="msgSource">
                  <el-select
                    v-model="form.incidentInfo.msgSource"
                    placeholder="请选择信息来源"
                    clearable
                    size="small"
                  >
                    <el-option
                     v-for="dict in dict.type.information_sources"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="通知人" prop="reportPerson">
                  <el-input v-model="form.incidentInfo.reportPerson" placeholder="请输入通知人" />
                </el-form-item>
                <el-form-item label="统计时间" prop="incidentInfo.reportTime">
                    <el-date-picker
                          v-model="form.incidentInfo.reportTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择日期时间"
                          default-time="12:00:00">
                        </el-date-picker>
                </el-form-item>
                <el-form-item label="事件级别" prop="incidentInfo.incidentGrade">
                  <el-select
                    v-model="form.incidentInfo.incidentGrade"
                    placeholder="请选择级别"
                    clearable
                    size="small"
                  >
                    <el-option
                    v-for="dict in dict.type.sd_incident_level"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              <el-form-item label="事件状态" prop="incidentStatus">
                <el-select
                  v-model="form.incidentInfo.incidentStatus"
                  placeholder="请选择事件状态"
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="dict in dict.type.sd_incident_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                  <!--<el-option-->
                  <!--v-for="item in incidentTypeList"-->
                  <!--:key="item.incidentTypeName"-->
                  <!--:label="item.incidentTypeName"-->
                  <!--:value="item.incidentTypeId"-->
                  <!--/>-->
                </el-select>
              </el-form-item>
                <!-- <el-form-item label="站点选择" prop="site">
                  <el-select
                    v-model="form.site"
                    placeholder="请选择站点"
                    clearable
                    size="small"
                  >
                    <el-option
                      v-for="item in siteList"
                      :key="item.siteName"
                       :label="item.siteName"
                      :value="item.siteId"
                    />
                  </el-select>
                </el-form-item> -->

                <el-form-item label="方向" prop="direction">
                  <el-select
                    v-model="form.controlInfo.direction"
                    placeholder="请选择方向"
                    clearable
                    size="small"
                  >
                    <el-option
                      v-for="dict in dict.type.sd_direction_list"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                    <!--<el-option-->
                      <!--v-for="item in directionList"-->
                      <!--:key="item.directionName"-->
                       <!--:label="item.directionName"-->
                      <!--:value="item.directionId"-->
                    <!--/>-->
                  </el-select>
                </el-form-item>
                <el-form-item label="桩号" prop="controlInfo.stakeNumber">
                  <el-input v-model="form.controlInfo.stakeNumber" placeholder="请输入桩号" />
                </el-form-item>
                <el-form-item label="(+m)" prop="controlInfo.stakeDistance">
                  <el-input v-model="form.controlInfo.stakeDistance" placeholder="请输入桩号" />
                </el-form-item>

                <!-- <el-form-item label="交通状况" prop="incidentStatus">
                  <el-select
                    v-model="form.controlInfo.trafficCondition"
                    placeholder="请选择事件状况"
                    clearable
                    size="small"
                  >
                    <el-option
                      v-for="item in incidentStatusList"
                      :key="item.incidentStatusName"
                       :label="item.incidentStatusName"
                      :value="item.incidentStatusId"
                    />
                  </el-select>
                </el-form-item> -->
               <!-- <el-form-item label="(+m)" prop="addM">
                  <el-input v-model="form.addM" placeholder="请输入桩号" />
                </el-form-item> -->
                <el-form-item label="特殊地点描述" prop="describe">
                  <el-input v-model="form.controlInfo.specialSiteDesc"  />
                </el-form-item>
                <!--<el-form-item label="原因分类" prop="controlInfo.reasonClass">-->
                  <!--<el-select-->
                    <!--v-model="form.controlInfo.reasonClass"-->
                    <!--placeholder="请选择原因分类"-->
                    <!--clearable-->
                    <!--size="small"-->
                  <!--&gt;-->
                    <!--<el-option-->
                      <!--v-for="dict in dict.type.sd_reason_class"-->
                      <!--:key="dict.value"-->
                      <!--:label="dict.label"-->
                      <!--:value="dict.value"-->
                    <!--/>-->
                  <!--</el-select>-->
                <!--</el-form-item>-->
                <!--<el-form-item label="管控原因" prop="controlInfo.reasonType" v-if="form.controlInfo.reasonClass == 'weather_pavement_visibility'">-->
                  <!--<el-select-->
                    <!--v-model="form.controlInfo.reasonType"-->
                    <!--placeholder="请选择原因"-->
                    <!--clearable-->
                    <!--size="small"-->
                  <!--&gt;-->
                    <!--<el-option-->
                      <!--v-for="dict in dict.type.sd_road_condition"-->
                      <!--:key="dict.value"-->
                      <!--:label="dict.label"-->
                      <!--:value="dict.value"-->
                    <!--/>-->
                  <!--</el-select>-->
                <!--</el-form-item>-->

                <!--<el-form-item label="是否接交警通知" prop="policeInform">-->
                  <!--<el-select-->
                    <!--v-model="form.incidentInfo.policeInform"-->

                    <!--clearable-->
                    <!--size="small"-->
                  <!--&gt;-->
                    <!--<el-option-->
                     <!--v-for="dict in dict.type.sd_control_measure"-->
                     <!--:key="dict.value"-->
                     <!--:label="dict.label"-->
                     <!--:value="dict.value"-->
                    <!--/>-->
                  <!--</el-select>-->
                <!--</el-form-item>-->
                <!--<el-form-item label="发布对象" prop="publishObject" >-->
                    <!--<div style="display: flex;border: solid 1px #D9DCE3;border-radius: 4px;" @click='publishObj'>-->
                        <!--<input type="text" name="year" id="year"-->
                                <!--style="width:188px;height: 34px;border: none;margin-left: 2px;outline: none;font-size: 14px;text-indent: 10px;"-->
                                <!--v-model="form.incidentInfo.publishObject" readonly/>-->
                        <!--<img src="../../../../../assets/icons/search.png" width="20px" height="20px" @click='publishObj'-->
                               <!--style="margin-top: 7px;" />-->
                    <!--</div>-->
                <!--</el-form-item>-->
                <el-form-item label="开启管制"  prop="isFinish" >
                    <el-switch
                      v-model="form.controlInfo.isFinish"
                      active-value='1'
                      inactive-value='0'
                      active-color="#13ce66">
                    </el-switch>
                </el-form-item>
                <el-form-item label="备注" prop="remark" class="remarksTextarea" style="width: 100%;">
                    <el-input type="textarea" v-model="form.incidentInfo.remark" ></el-input>
                </el-form-item>

              <el-divider></el-divider>
              <div class="titleFontStyle">
                隧道的实时数据
              </div>
                <el-tabs v-model="activeName" @tab-click="handleClick" class="tabStyle" >
                  <el-tab-pane label="能见度" name="first">
                    <div>
                      <el-table :data="realData.weatherList">
                        <!--<el-table-column type="selection" width="55" align="center" />-->
                        <el-table-column label="所属隧道" align="center" prop="tunnelName" />
                        <el-table-column label="设备编号" align="center" prop="deviceId" />
                        <!--<el-table-column label="风的房向" align="center" prop="windDirection" />-->
                        <!--<el-table-column label="风的速度" align="center" prop="windSpeed" />-->
                        <el-table-column
                          label="1分钟能见度"
                          align="center"
                          prop="oneMinuteVisibility"
                        />
                        <el-table-column
                          label="10分钟能见度"
                          align="center"
                          prop="tenMinuteVisibility"
                        />
                        <el-table-column
                          label="创建时间"
                          align="center"
                          prop="createTime"
                        />
                        <!--<el-table-column-->
                          <!--label="路面状态"-->
                          <!--align="center"-->
                          <!--prop="pavementCondition"-->
                        <!--/>-->
                        <!--<el-table-column-->
                          <!--label="遥感检测仪工作状态"-->
                          <!--align="center"-->
                          <!--prop="remoteSenseStatus"-->
                        <!--/>-->
                        <!--<el-table-column-->
                          <!--label="能见度仪工作状态"-->
                          <!--align="center"-->
                          <!--prop="visibilityStatus"-->
                        <!--/>-->
                      </el-table>
                    </div>

                  </el-tab-pane>
                  <el-tab-pane label="路面情况" name="second">
                    <el-table :data="realData.icyRoadList">
                      <!--<el-table-column type="selection" width="55" align="center" />-->
                      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
                      <el-table-column label="设备名称" align="center" prop="eqName" />
                      <el-table-column label="设备状态" align="center" prop="status" />
                      <!--<el-table-column label="表面温度" align="center" prop="surfaceTemperature" />-->
                      <!--<el-table-column label="结冰温度" align="center" prop="freezeTemperature" />-->
                      <!--<el-table-column label="冰厚度" align="center" prop="iceThickness" />-->
                      <!--<el-table-column label="盐度值" align="center" prop="salinityValue" />-->
                      <!--<el-table-column label="湿滑系数" align="center" prop="wetslidingCoefficient" />-->
                      <el-table-column label="路面状况" align="center" prop="roadCondition" />
                      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                        <template slot-scope="scope">
                          <span>{{ scope.row.createTime }}</span>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                  <el-tab-pane label="微波车检数据" name="third">
                    <el-table  :data="realData.flowList">
                      <el-table-column label="设备名称" align="center" prop="deviceName" ></el-table-column>
                      <el-table-column label="车道号 " align="center" prop="byLane" />
                      <el-table-column label="过车平均速度 " align="center" prop="bySpeed" />
                      <el-table-column label="占有率(%)" align="center" prop="fSpaceOccupyRation" />
                      <el-table-column label="车流量(辆/分钟)"align="center" prop="byVehicelNum" />
                      <el-table-column label="上传时间"align="center" prop="createTime" />
                    </el-table>
                  </el-tab-pane>
                </el-tabs>

              <el-divider></el-divider>
              <div class="titleFontStyle titleStyle">
                <!--主动交通流推送的-->
                管控措施
              </div>
              <el-form-item label="管控类别" prop="levelConfig.controlType">
                <el-select v-model="form.levelConfig.controlType" clearable size="small" @change="controlTypeSelectChange">
                  <el-option
                    v-for="(dict,index) in dict.type.sd_control_type"
                    :key="index"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="管控级别" prop="levelConfig.controlLevel">
              <el-select v-model="form.levelConfig.controlLevel" clearable size="small" @change="controlLevelSelectChange">
                <el-option
                  v-for="(dict,index) in dict.type.sd_control_level"
                  :key="index"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
              <el-form-item label="管控原因" prop="levelConfig.controlReason" class="remarksTextarea" style="width: 100%;">
                <el-input type="textarea" v-model="form.levelConfig.controlReason" readonly></el-input>
              </el-form-item>
              <el-form-item label="措施详情" prop="levelConfig.measureDetail" class="remarksTextarea" style="width: 100%;">
                <el-input type="textarea" v-model="form.levelConfig.measureDetail" readonly></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm('form')">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>


        <el-dialog title="用户选择" :visible.sync="openTransfer" width="800px" append-to-body>
            <el-transfer
                ref='transfer'
                filterable
                :filter-method="filterMethod"
                filter-placeholder="请输入用户名称"
                 :titles="['请选择用户', '已选择用户']"
                v-model="transferVal"
                :data="data">
              </el-transfer>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('transfer')">确 定</el-button>
                <el-button @click="cancel('transfer')">取 消</el-button>
              </div>
        </el-dialog>
    </div>
</template>

<script>
import { getList,getTrafficControlInfo, addControlInfo,updateControlInfo,delControlInfo,
  getPublishContent}
  from "@/api/trafficOperationControl/trafficOperationStatus/trafficControlLevelConfiguration/controlLevelConfiguration/controlLevelConfiguration";

import {getActiveTrafficMeasure,getActiveTrafficMeasureByTunnelId,getControlMeasureByTypeLevel} from
    "@/api/trafficOperationControl/activeTrafficFlow/activeTrafficFlow";
import { listTunnels } from "@/api/equipment/tunnel/api";
import axios from "axios";
export default {
  name: "Emergencies",
  dicts: ['sd_control_level','sd_control_measure','sd_incident_level','sd_reason_class','sd_control_reason','sd_road_condition','information_sources',"sd_control_type","sd_control_level","sd_incident_status","sd_direction_list"],
  data() {
      // 发布对象 穿梭框
      const generateData = _ => {
          const data = [];
          const cities = ['郭鹏', '王登顺', '吴昊阳', '李浩东', '刘方堃', '宋航', '车东旭'];
          const pinyin = ['guopeng', 'wangdengshun', 'wuhaoyang', 'lihaodong', 'liufangkun', 'songhang', 'chedongxu'];
          cities.forEach((city, index) => {
            data.push({
              label: city,
              key: city,
              pinyin: pinyin[index]
            });
          });
          return data;
        };
    return {
        title:'',
        // 是否结束 否
        // isFinish:false,
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        rules: {
          incidentInfo:{
            tunnelId: [
              { required: true, message: "请选择隧道", trigger: "change", },
            ],
            occurTime: [
              { required: true, message: "请选择时间", trigger: "blur"},
            ],
            reportTime: [
              //加上 type:'date'保存报错
              { required: true, message: "请选择时间", trigger: "blur"},
            ],
            incidentGrade: [
              { required: true, message: "请选择级别", trigger: "change", },
            ],
            // takeMeasure: [
            //   { required: true, message: "请选择措施", trigger: "change", },
            // ]
          },
          controlInfo:{
            stakeNumber:[
              { required: true, message: "请输入桩号", trigger: "blur", },
              { pattern: /^(K|YK|ZK)[0-9]{3}$/, message: '桩号格式为K、YK、ZKxxx+xxx组成', trigger: 'blur'}
            ],
            stakeDistance:[
              { required: true, message: "请输入桩号", trigger: "blur", },
              { pattern: /^[0-9]{3}$/, message: '+m 为1-3个字符，由数字组成', trigger: 'blur'}
            ],
            // reasonClass:[
            //   { required: true, message: "请选择原因", trigger: "change", },
            // ],
            // reasonType:[
            //   { required: true, message: "请选择原因", trigger: "change", },
            // ],

          },
          levelConfig:{
            controlType:[
              { required: true, message: "请选择管控类别", trigger: "change", },
            ],
            controlLevel:[
              { required: true, message: "请选择管控等级", trigger: "change", },
            ]
          }
        },
        // 事件发布弹窗
        openRelease:false,
        releaseForm:{
            evtInformation:'',
            checkList:[],
        },
        // 新增管控事件 弹窗
        open:false,
        // 发布对象 穿梭框 弹窗
        openTransfer:false,
        form:{
          incidentInfo:{
            id:"",
            tunnelId:'',
            incidentType:'',
            incidentStatus:'',
            occurTime:'',
            msgSource:'',
            reportPerson:'',
            reportTime:'',
            incidentGrade:'',
            // policeInform:'',
            // takeMeasure:'',
            // publishObject:[]
          },
          controlInfo:{
            id:"",
            incidentId:"",
            // reasonClass:"",
            // reasonType:"",
            direction:"",
            stakeNumber:"",
            // trafficCondition:"",
            specialSiteDesc:"",
            isFinish:""
          },
          levelConfig:{
            controlType:"",
            controlLevel:"",
            measureList:[],
            measureDetail:"",
            controlReason:""
          }

        },
        // // 事件状况
        // incidentTypeList:[
        //     {
        //        incidentTypeName:'已处理',
        //        incidentTypeId:'1'
        //     },
        //     {
        //        incidentTypeName:'未处理',
        //        incidentTypeId:'2'
        //     },
        // ],


        // // 站点名称 下拉框
        // siteList:[
        //     {
        //         siteName:'新泰北隧道所',
        //         siteId:'1',
        //     },
        // ],
        // // 方向 下拉框
        // directionList:[
        //    {
        //        directionName:'上行',
        //        directionId:'1',
        //    },
        //    {
        //        directionName:'下行',
        //        directionId:'2',
        //    },
        // ],
        // incidentStatusList:[
        //     {
        //         incidentStatusName:'拥堵',
        //         incidentStatusId:'1',
        //     },
        // ],


        // // 是否接交警通知 下拉框
        // policeInformList:[
        //     {
        //         policeInformName:'是',
        //         policeInformId:'1'
        //     },
        //     {
        //         policeInformName:'否',
        //         policeInformId:'2'
        //     },
        // ],

        // 发布对象 穿梭框
        data: generateData(),
        transferVal: [],
        filterMethod(query, item) {
          return item.pinyin.indexOf(query) > -1;
        },
        // 查询参数
        queryParams: {
          tunnelId:"",
          pageNum: 1,
          pageSize: 10,
          params:{
            reportTime:[]
          }
        },
        tunnelData:[],
        handleType:'',
        controlList:[],
      activeName: 'first',
      realData:{
        icyRoadList:[],
        flowList:[],
        weatherList:[]
      },
      //避免重复多次点击新增按钮
      isClick:true
    }
  },
  created() {
    this.getTunnels();
    this.getList();

  },
  methods: {
      getTunnels() {
        listTunnels().then((response) => {
           this.tunnelData = response.rows
        });
       },
    /** 查询交通突发事件信息管理列表 */
    getList() {
      this.loading = true;
      getList(this.queryParams).then(response => {
        this.controlList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    publishObj(){
        this.openTransfer = true
    },
    /** 新增按钮操作 */
    handleAdd() {
        this.form = {
          incidentInfo:{
            id:"",
            tunnelId:'',
            incidentType:'',
            incidentStatus:'',
            occurTime:'',
            msgSource:'',
            reportPerson:'',
            reportTime:'',
            incidentGrade:'',
            // policeInform:'',
            // takeMeasure:'',
            // publishObject:[]
          },
          controlInfo:{
            id:"",
            incidentId:"",
            // reasonClass:"",
            // reasonType:"",
            direction:"",
            stakeNumber:"",
            // trafficCondition:"",
            specialSiteDesc:"",
            isFinish:""
          },
          levelConfig:{
            controlType:"",
            controlLevel:"",
            measureList:[],
            measureDetail:"",
            controlReason:""
          }
        },
      this.open = true;
      this.title = "新增交通管制事件";
      this.handleType = 'add';
      this.realData = [];

    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.resetQuery();
      const ids = row.id || this.ids;
      getTrafficControlInfo(ids).then(response => {

        this.form.controlInfo = response.controlInfo;
        this.form.incidentInfo = response.incidentInfo;
        this.open = true;
        this.title = "修改交通管制事件";
        this.handleType = 'update';

        if(response.hasOwnProperty("measureList")){
          //回显措施数据
          this.form.levelConfig.controlLevel = response.levelConfig.controlLevel;
          this.form.levelConfig.controlType = response.levelConfig.controlType;
          this.form.levelConfig.controlReason = response.controlReason;
          this.form.levelConfig.measureList = response.measureList;
          this.form.levelConfig.measureDetail = response.measureDetail;

          this.getRealTimeData(ids);
        }else{
          //没有措施数据，获取主动交通流推送的措施
          this.getMeasureData(ids);
        }
      });
    },
    //隧道下拉框改变事件
    tunnelChange(){
      var tunnelId = this.form.incidentInfo.tunnelId;
      var params = {tunnelId:tunnelId};
      //改变时重新获取实时数据和推送措施
      this.getMeasureDataByTunnelId(params);
    },
    //获取实时数据
    getRealTimeData(ids){
      getActiveTrafficMeasure(ids).then(response => {
        if(!response.data){
          return;
        }
        this.realData = response.data.realData;
      });
    },
    //根据事件id获取措施数据
    getMeasureData(ids){
      getActiveTrafficMeasure(ids).then(response => {
        this.measureDataCallback(response);
      });
    },
    //根据隧道id获取措施数据
    getMeasureDataByTunnelId(params){
      getActiveTrafficMeasureByTunnelId(params).then(response => {
        this.measureDataCallback(response);
      });
    },
    //获取到措施数据后的回调
    measureDataCallback(response){
      if(!response.data){
        return;
      }
      this.realData = response.data.realData;

      var measureData = response.data.measureData;

      if(measureData.hasOwnProperty("measureList")){
        // this.getMeasureDetail(measureData.measureList);
        this.form.levelConfig.controlReason = measureData.controlReason;
          this.form.levelConfig.measureList = measureData.measureList;
          this.form.levelConfig.measureDetail = measureData.measureDetail;
      }else{
        this.form.levelConfig.controlReason = "";
        this.form.levelConfig.measureDetail = "";
        this.form.levelConfig.measureList = [];
      }
      if(measureData.hasOwnProperty("levelConfig")){
        this.form.levelConfig.controlType = measureData.levelConfig.controlType;
        this.form.levelConfig.controlLevel = measureData.levelConfig.controlLevel;
      }else{
        this.form.levelConfig.controlType = "";
        this.form.levelConfig.controlLevel = "";
      }

    },

    handleClick(tab, event) {
      // console.log(tab, event);
    },
    //管控类别下拉框改变事件
    controlTypeSelectChange(){
      this.controlLevelSelectChange();
    },
    //管控级别下拉框改变事件
    controlLevelSelectChange(){
      //获取措施详情
      var controlType = this.form.levelConfig.controlType;
      var controlLevel = this.form.levelConfig.controlLevel;

      if(!this.form.incidentInfo.tunnelId){
        this.$modal.msgWarning("请选择隧道");
        return;
      }

      if(!controlType || !controlLevel){
        this.form.levelConfig.controlReason = "";
        this.form.levelConfig.measureDetail = "";
        return;
      }
      var param = {
        controlType:controlType,
        controlLevel:controlLevel
      };

      getControlMeasureByTypeLevel(param).then(response => {
        var controlReason = response.data.controlReason;
        var measureList = response.data.measureList;
        var measureDetail = response.data.measureDetail;
        if(measureList && measureList.length > 0){
          this.form.levelConfig.controlReason = controlReason;
            this.form.levelConfig.measureList = measureList;
            this.form.levelConfig.measureDetail = measureDetail;
        }else{
          this.form.levelConfig.controlReason = "";
          this.form.levelConfig.measureDetail = "";
        }
      });

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除这条交通管制事件？').then(function() {
        return delControlInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
          this.$modal.msgWarning("已取消删除");
      });
    },
    // 发布按钮操作
    handleRelease(row) {
      const ids = row.id || this.ids;
      this.openRelease = true;
      this.releaseForm.evtInformation = "";
      this.releaseForm.checkList = [];

      //获取事件信息-发布内容
      getPublishContent(ids).then(response => {
        var content = response.data.content;
        this.releaseForm.evtInformation = content;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.resetForm("form");
      this.queryParams.params.reportTime = [];
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
        console.log(selection,"selection")
      this.ids = selection.map(item => item.id)
      console.log(this.ids,"this.ids")
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 取消按钮
    cancel(type) {
      if(type == 'transfer'){
        this.openTransfer = false;
      }else{
        this.open = false
        this.openRelease = false;
        this.resetQuery();
      }

    },
    /** 提交按钮 */
    submitForm(type) {
        if(type == 'release'){
            console.log(this.releaseForm,"releaseForm")
            if(this.releaseForm.checkList.length == 0){
              this.$modal.msgWarning("请选择发布渠道");
                // this.$modal.confirm('请选择发布渠道').then(function() {
                // }).then(() => { }).catch(() => {});
                return
            }else if(this.releaseForm.evtInformation == ''){
              this.$modal.msgWarning("请输入发布内容");
                // this.$modal.confirm('请输入发布内容').then(function() {
                // }).then(() => { }).catch(() => {});
                return
            }
            this.$refs["releaseForm"].validate(valid => {
            // this.handlePublish();
               this.$modal.msgWarning("开发中");
              // if (valid) {
              //     updateInformation(this.releaseForm).then(response => {
              //       this.$modal.msgSuccess("发布成功");
              //       this.openRelease = false;
              //       this.getList();
              //     });
              // }
            });
        }else if(type == 'transfer'){
            console.log(this.transferVal,"transferVal")
            this.form.incidentInfo.publishObject = this.transferVal
            this.openTransfer = false

        }else if(type == 'form'){
            console.log(this.form,"this.form")
           this.$refs["form"].validate(valid => {
             if (valid) {
               if (this.form.incidentInfo.id) {
                 updateControlInfo(this.form).then(response => {
                   this.$modal.msgSuccess("修改成功");
                   this.open = false;
                   this.getList();
                 });
               } else {
                 //避免重复多次点击新增按钮
                 if(this.isClick){
                   addControlInfo(this.form).then(response => {
                     this.$modal.msgSuccess("新增成功");
                     this.open = false;
                     this.getList();
                   });

                   this.isClick = false;
                   setTimeout(function(that){
                     that.isClick = true;
                   },2000,this);
                 }

               }
             }
           });
        }

    },
    //发布
    handlePublish(){
      // 判断选中的发布渠道
      var array = this.releaseForm.checkList;
      if(array.includes("1")){
        //微信
        this.getToken();
      }
    },
    //获取微信订阅号token，暂时使用测试账号
    getToken(){
      var appid = "wxd9732ad6c9a1a5c0";
      var appsecret = "7fb1d3e3cbe5d8d9d6aa9d1fb9c3318f";

      var url = "/wechat/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appsecret;
      axios.get(url).then(res => {
        var accessToken = res.data.access_token;
        this.sendMessage(accessToken);
      });
    },
    //群发文本
    sendMessage(accessToken){
      var url = "/wechat/cgi-bin/message/mass/sendall?access_token="+accessToken;
      var content = this.releaseForm.evtInformation;
      var json = {
        "filter":{
          "is_to_all":true,
          "tag_id":1
        },
        "text":{
          "content":content
        },
        "msgtype":"text"
      };

      axios({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'post',
        url: url,
        data: json,
        // params: {
        //   access_token: accessToken
        // },
      }).then(res => {
        if(res.data.errcode === "0"){
          console.log("微信发布成功");
        }
      });
    },

    // wechatTest(){
    //   wx.config({
    //     debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    //     appId: 'wxd9732ad6c9a1a5c0', // 必填，公众号的唯一标识
    //     timestamp:'' , // 必填，生成签名的时间戳
    //     nonceStr: '', // 必填，生成签名的随机串
    //     signature: '',// 必填，签名
    //     jsApiList: [] // 必填，需要使用的JS接口列表
    //   });
    //   wx.ready(function(){
    //     // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    //   });
    //   wx.error(function(res){
    //     // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    //   });
    // },
  }
}
</script>

<style scoped lang="scss">
    ::v-deep .el-dialog{


    }
    .releaseDialog{
        img{
            width: 20px;

        }
        .el-form--inline .el-form-item{
            width: 100%;
        }
        .el-form-item--medium .el-form-item__content{
            width: 75%;
        }
        textarea{
            min-height: 100px !important;
        }
    }
    ::v-deep .inline{
        .el-form-item__content{
            display: flex;
        }
        .el-input{
            width: 10% !important;
        }
    }
    ::v-deep .el-transfer-panel{
        width: 38%;
    }
    ::v-deep .remarksTextarea{
      .el-form-item__content{
          width: 75% !important;
      }
    }
    ::v-deep .controlDialog{
        // .el-select > .el-input{
        //     width: 220px !important;
        // }
        .el-form-item__content{
            width: 220px !important;
        }
    }

    /*注释掉，避免影响分页样式*/
    /*::v-deep .el-input{*/
        /*width: 220px;*/
    /*}*/
    ::v-deep .el-form-item--medium .el-form-item__content{
        width: 220px;
    }

  /*推送tab标签样式*/
  .tabStyle{
    padding: 30px 40px;
  }
  .titleFontStyle{
    font-size: 15px;
    font-weight: bold;
  }
  .titleStyle{
    margin: 30px 0;
  }
</style>
