<template>
  <div class="app-container">
    <!-- <div
      style="display: flex; font-size: 14px; width: 100%; align-items: center"
    >
      <div class="warningStatistics">事件预警统计:</div>
      <div class="EquipStatistics">
        今日累计预警事件: <span>{{ eventMsg.allnum }}</span>
      </div>
      <div class="EquipStatistics">
        今日执行预警事件: <span>{{ eventMsg.process }}</span>
      </div>
      <div class="EquipStatistics">
        今日预警事件执行率: <span>{{ eventMsg.bl }}</span>
      </div>
    </div> -->

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:event:add']"
        >新增事件</el-button>
      </el-col>

      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button
            size="mini"
            circle
            icon="el-icon-refresh"
            @click="handleQuery"
          />
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          :content="showSearch ? '隐藏搜索' : '显示搜索'"
          placement="top"
        >
          <el-button
            size="mini"
            circle
            icon="el-icon-search"
            @click="showSearch = !showSearch"
          />
        </el-tooltip>
      </div>
    </el-row> -->

    <div class="butBox">
      <div :class="searchValue == '1' ? 'xz' : ''" @click="qiehuan('1')">
        主动安全
      </div>
      <div :class="searchValue == '0' ? 'xz' : ''" @click="qiehuan('0')">
        交通事件
      </div>
      <div :class="searchValue == '2' ? 'xz' : ''" @click="qiehuan('2')">
        设备故障
      </div>
    </div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="searchValue == '1' || searchValue == '0'"
      label-width="68px"
      style="margin-top: 10px"
    >
      <el-form-item label="事件类型" prop="eventTypeId">
        <el-select
          v-model="queryParams.eventTypeId"
          placeholder="请选择事件类型"
          clearable
          size="small"
          style="width: 180px"
        >
          <el-option
            v-for="item in eventTypeData"
            :key="item.id"
            :label="item.eventType"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="管理机构" prop="deptId">
        <treeselect
          v-model="queryParams.deptId"
          :options="deptOptions"
          :show-count="true"
          placeholder="请选择归属部门"
          @select="changeMechanism"
          style="width: 300px"

        />


        <!--<el-select
          v-model="queryParams.deptId"
          placeholder="请选择管理机构"
          clearable
          size="small"
          style="width: 180px"
        >
          <el-option
            v-for="item in mechanism"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
            @click.native="changeMechanism(item.deptId)"
          />
        </el-select>-->
      </el-form-item>
      <el-form-item label="所属隧道" prop="tunnelId">
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
        </el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
      v-show="searchValue == '2'"
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
        </el-button
        >
        <el-button type="primary" plain size="mini" @click="resetQuery"
        >重置
        </el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:list:add']"
        >新增
        </el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:list:edit']"
        >修改
        </el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:list:remove']"
        >删除
        </el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:list:export']"
        >导出
        </el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="eventList"
      :default-sort="{ prop: 'eventTime', order: 'descending' }"
      max-height="600"
      ref="tableRef"
      :row-class-name="tableRowClassName"
      v-show="searchValue == '1' || searchValue == '0'"
    >
      <el-table-column
        label="隧道名称"
        align="center"
        prop="tunnels.tunnelName"
      />
      <el-table-column
        label="所属机构"
        align="center"
        prop="tunnelStationName"
      />
      <el-table-column label="方向" align="center" prop="direction" width="120">
        <template slot-scope="scope">
          <span>{{ getDirection(scope.row.direction) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="桩号" align="center" prop="stakeNum"/>
      <el-table-column
        label="车道号"
        align="center"
        prop="laneNo"
        width="100"
      />
      <!-- <el-table-column label="车型" align="center" prop="tunnels.tunnelName" /> -->
      <el-table-column
        label="事件类型"
        align="center"
        prop="eventType.eventType"
      />
      <!-- <el-table-column label="级别" align="center" prop="eventGrade" /> -->
      <el-table-column label="事件标题" align="center" prop="eventTitle"/>
      <!-- <el-table-column
        label="事件描述"
        align="center"
        prop="eventDescription"
        :show-overflow-tooltip="true"
      /> -->
      <!-- <el-table-column label="影响程度" align="center" prop="eventType.eventType" /> -->
      <el-table-column
        label="开始时间"
        align="center"
        prop="startTime"
        sortable
      >
        <template slot-scope="scope">
          <span>{{
            parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{m}")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="endTime" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{m}") }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="状态" align="center" prop="eventState" :formatter="eventStateFormat" /> -->
      <el-table-column label="事件状态" align="center" prop="eventState">
        <template slot-scope="scope">
          <span v-show="scope.row.eventState == 0" style="color: #00aa00"
          ><i class="el-icon-info" style="color: #00aa00;!important"></i
          >&nbsp;处理中</span
          >
          <span v-show="scope.row.eventState == 1" style="color: #00aa00"
          ><i class="el-icon-success" style="color: #00aa00;!important"></i
          >&nbsp;已处理</span
          >
          <span v-show="scope.row.eventState == 2" style="color: #00aa00"
          ><i class="el-icon-success" style="color: #00aa00;!important"></i
          >&nbsp;已忽略</span
          >
          <span v-show="scope.row.eventState == 3" style="color: #ff0000"
          ><i class="el-icon-info" style="color: #ff0000;!important"></i
          >&nbsp;未处理</span
          >
        </template>
      </el-table-column>
      <!-- <el-table-column label="处置时长" align="center" prop="eventType.eventType" /> -->
      <!-- <el-table-column label="发布时间" align="center" prop="eventTime"sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.eventTime, '{y}-{m}-{d} {h}:{m}') }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="级别 " align="center" prop="eventGrade" :formatter="eventGradeFormat" />
      <el-table-column label="位置" align="center" prop="eventLocation" />
      <el-table-column prop="specs,quantityUnit" label="伤亡" align="left">
            <template slot-scope="scope"> 
                  死亡：{{scope.row.eventDeath}}<br />
                  受伤：{{scope.row.eventInjured}} 
             </template>
       </el-table-column>
 -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="240"
      >
        <template slot-scope="scope">
          <el-button
            v-show="scope.row.eventState == 3"
            size="mini"
            class="tableBlueButtton"
            @click="changeState(scope.row, 2)"
            v-hasPermi="['system:event:edit']"
          >忽略
          </el-button
          >
          <!-- <el-button
            v-show="scope.row.eventState == 0"
            size="mini"
            class="tableBlueButtton"
            @click="handleOk(scope.row)"
            v-hasPermi="['system:event:edit']"
            >已解决</el-button
          > -->
          <!-- <router-link :to="'/business/event/' + scope.row.id" class="link-type"> -->
          <el-button
            size="mini"
            class="tableBlueButtton"
            v-hasPermi="['system:event:remove']"
            @click="handleDetails(scope.row)"
          >查看详情
          </el-button>
          <!-- </router-link> -->
          <el-button
            v-show="
              (scope.row.eventState == 0 && searchValue != '1') ||
              (scope.row.eventState == 3 && searchValue != '1')
            "
            size="mini"
            class="tableBlueButtton"
            v-hasPermi="['system:event:remove']"
            @click="handleDispatch(scope.row)"
          >应急调度
          </el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="loadBigScreen(scope.row)"
            v-hasPermi="['system:event:edit']"
          >进入应急调度大屏</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <el-table
      v-loading="loading"
      :data="eventList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      max-height="600"
      v-show="searchValue == '2'"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="故障类型" align="center" prop="faultType"/>
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
      <el-table-column label="持续时间" align="center" prop="faultCxtime"/>
      <el-table-column label="设备id" align="center" prop="eqId"/>
      <el-table-column label="设备状态" align="center" prop="eqStatus"/>
      <el-table-column label="故障等级" align="center" prop="faultLevel"/>
      <el-table-column
        label="消除状态"
        align="center"
        prop="falltRemoveStatue"
      />
      <el-table-column label="状态" align="center" prop="faultStatus"/>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:list:edit']"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
          >删除
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :before-close="eventFormClose" class="addClass">
      <el-form ref="form1" :model="eventForm" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="管理机构" prop="deptId">
              <el-select v-model="eventForm.deptId" placeholder="请选择管理机构" clearable size="small">
                <el-option v-for="item in mechanism" :key="item.deptId" :label="item.deptName"
                  :value="item.deptId" @click.native="changeMechanism(item.deptId)"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属隧道" prop="tunnelId">
              <el-select v-model="eventForm.tunnelId" placeholder="请选择所属隧道" clearable size="small">
                <el-option v-for="item in tunnelList" :key="item.tunnelId" :label="item.tunnelName"
                  :value="item.tunnelId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="方向" prop="direction">
              <el-select v-model="eventForm.direction" placeholder="请选择方向">
                <el-option
                    v-for="item in dict.type.sd_direction"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"/>
              </el-select>
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
                /> -->
    <!--<el-option-->
    <!--v-for="item in directionList"-->
    <!--:key="item.directionName"-->
    <!--:label="item.directionName"-->
    <!--:value="item.directionId"-->
    <!--/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="桩号" prop="stakeNum">
              <el-input  v-model="eventForm.stakeNum" placeholder="请输入桩号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>

          <el-col :span="12">
            <el-form-item label="事件类型" prop="eventTypeId">
              <el-select v-model="eventForm.eventTypeId" placeholder="请选择事件类型">
                <el-option
                    v-for="item in eventTypeData"
                    :key="item.id"
                    :label="item.eventType"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件标题" prop="eventTitle">
              <el-input  v-model="eventForm.eventTitle" placeholder="请输入事件标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker clearable size="small" style="width: 200px"
                v-model="eventForm.startTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完成时间" prop="endTime">
              <el-date-picker clearable size="small" style="width: 200px"
                v-model="eventForm.endTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择完成时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件描述" prop="eventDescription">
              <el-input  v-model="eventForm.eventDescription" placeholder="请输入事件描述" style="width: 630px !important;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="级别 " prop="eventGrade">
              <el-radio-group v-model="eventForm.eventGrade">
                <el-radio
                  v-for="dict in eventGradeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="伤亡" prop="eventDeath">
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;" v-model="eventForm.eventDeath" placeholder="请填写死亡人数"  />
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;margin-left: 3%;" v-model="eventForm.eventInjured" placeholder="请填写重伤人数" />
              <el-input  oninput="value=value.replace(/[^0-9.]/g,'')"
                         onKeypress="return(/[\d]/.test(String.fromCharCode(event.keyCode)))"
                         style="width: 30% !important;margin-left: 3%;" v-model="eventForm.slightInjured" placeholder="请填写轻伤人数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item  style="text-align: center;">
          <el-button  size="small"  type="primary" :loading="submitEventFormLoading" @click="submitEventForm">保存</el-button>
          <el-button  size="small"   @click="eventFormClose">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog> -->

    <!-- 查看详情弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="details"
      width="800px"
      append-to-body
      :before-close="cancel"
      class="detailsClass"
    >
      <el-form
        ref="form1"
        :model="eventForm"
        :rules="rules"
        label-width="100px"
      >
        <el-row>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="circle" style="background-color: #ff5823"></div>
            </div>
            <div class="detailsText">事件发生时间</div>
            <div style="color: #82b3c2; line-height: 40px">
              {{ eventForm.startTime }}
            </div>
          </el-col>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="circle" style="background-color: #0065f8"></div>
            </div>
            <div class="detailsText">预计解除时间</div>
            <div style="color: #82b3c2; line-height: 40px">
              {{ eventForm.endTime }}
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="el-icon-discover" style="width: 10px"></div>
            </div>
            <div class="detailsText">行驶方向</div>
            <div
              style="color: #82b3c2; line-height: 40px"
              v-if="eventForm.direction"
            >
              {{ getDirection(eventForm.direction) }}
            </div>
          </el-col>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="el-icon-sunny" style="width: 10px"></div>
            </div>
            <div class="detailsText">所属隧道</div>
            <div
              style="color: #82b3c2; line-height: 40px"
              v-if="eventForm.tunnels"
            >
              {{ eventForm.tunnels.tunnelName }}
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div
                class="el-icon-location-information"
                style="width: 10px"
              ></div>
            </div>
            <div class="detailsText">桩号位置</div>
            <div style="color: #82b3c2; line-height: 40px">
              {{ eventForm.stakeNum }}
            </div>
          </el-col>
          <el-col :span="12" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="el-icon-notebook-2" style="width: 10px"></div>
            </div>
            <div class="detailsText">事件分类</div>
            <div
              style="color: #82b3c2; line-height: 40px; width: 195px"
              v-if="eventForm.eventType.eventType"
            >
              {{ eventForm.eventType.eventType }}
            </div>
          </el-col>
        </el-row>
        <!-- <hr /> -->
        <el-row>
          <el-col :span="24" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="el-icon-discount" style="width: 10px"></div>
            </div>
            <div class="detailsText">事件标题</div>
            <div
              style="
                color: #82b3c2;
                line-height: 40px;
                width: calc(100% - 140px);
              "
            >
              {{ eventForm.eventTitle }}
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" style="display: flex; height: 40px">
            <div
              style="
                width: 40px;
                height: 40px;
                text-align: center;
                line-height: 40px;
              "
            >
              <div class="el-icon-discount" style="width: 10px"></div>
            </div>
            <div class="detailsText">事件描述</div>
            <div
              style="
                color: #82b3c2;
                line-height: 40px;
                width: calc(100% - 140px);
              "
            >
              {{ eventForm.eventDescription }}
            </div>
          </el-col>
        </el-row>
        <!-- <el-row class="rowClass">
          <el-col :span="12">
            <div class="eventClass">
              事件分类：<span style="font-style: oblique">{{
                eventForm.eventType.eventType
              }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="eventClass">
              事件级别：<span style="font-style: oblique">{{
                eventForm.eventGrade
              }}</span>
            </div>
          </el-col>
        </el-row> -->
        <!-- <el-row class="rowClass">
          <el-col :span="6">
            <div class="eventTitleClass">伤亡情况</div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              死亡人数：<span style="font-style: oblique">{{
                eventForm.eventDeath
              }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              重伤人数：<span style="font-style: oblique">{{
                eventForm.eventInjured
              }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              轻伤人数：<span style="font-style: oblique">{{
                eventForm.slightInjured
              }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row class="rowClass">
          <el-col :span="4">
            <div class="eventTitleClass">车辆损失</div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">
              小车：<span style="font-style: oblique">{{
                eventForm.smallCarNum
              }}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">
              货车：<span style="font-style: oblique">{{
                eventForm.truckNum
              }}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">
              客车：<span style="font-style: oblique">{{
                eventForm.passengerCarNum
              }}</span>
            </div>
          </el-col>
          <el-col :span="5">
            <div class="eventClass">
              罐车：<span style="font-style: oblique">{{
                eventForm.tankerNum
              }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row class="rowClass">
          <el-col :span="6">
            <div class="eventTitleClass">联系方式</div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              交警电话<span style="font-style: oblique">{{
                eventForm.policePhone
              }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              车主电话：<span style="font-style: oblique">{{
                eventForm.carOwnerPhone
              }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="eventClass">
              清障电话：<span style="font-style: oblique">{{
                eventForm.wreckerPhone
              }}</span>
            </div>
          </el-col>
        </el-row>
        <hr /> -->

        <el-row style="height: 300px">
          <el-col :span="16" style="height: 300px">
            <video
              :src="videoUrl"
              controls
              muted
              autoplay
              loop
              class="video"
            ></video>
          </el-col>
          <el-col :span="8" style="height: 300px">
            <img
              v-for="(item, index) in urls"
              :key="index"
              :src="item.imgUrl"
              class="image3"
            />
          </el-col>
        </el-row>
        <el-row>
          <p class="eventTitle">处置记录</p>
          <el-timeline
            style="
              height: calc(100% - 150px);
              overflow: auto;
              padding: 20px 0px;
              box-sizing: border-box;
            "
          >
            <el-timeline-item
              placement="top"
              v-for="(item, index) in dialogEventList"
              :key="index + item.flowTime"
              color="#00A0FF"
              :timestamp="item.flowTime"
            >
              <!-- <div>{{ item.flowTime }}</div> -->
              <el-card>
                <p>{{ item.flowDescription }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-row>
      </el-form>
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
  } from "@/api/event/event";
  import {getList, listList} from "@/api/electromechanicalPatrol/faultManage/fault";
  import {listEventType, getTodayEventCount} from "@/api/event/eventType";
  import {listPlan} from "@/api/event/reservePlan";
  import {listTunnels} from "@/api/equipment/tunnel/api";
  import {image, video} from "@/api/eventDialog/api.js";
  import {listEventFlow, getListBySId} from "@/api/event/eventFlow";
  import {treeselect, treeselectExcYG1} from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {loadPicture} from "@/api/equipment/type/api";


  export default {
    name: "Event",
    dicts: ["sd_direction"],
    components: {Treeselect},
    data() {
      return {
        dialogEventList: [],
        eventMsg: {
          allnum: 0,
          process: 0,
          bl: 0,
        },
        urls: [
          {imgUrl: require("@/assets/image/nodata.png")},
          {imgUrl: require("@/assets/image/nodata.png")},
        ],

        videoUrl: "",
        // 管理机构
        mechanism: [],
        // 所属隧道
        tunnelList: [],
        fileList:[],
        removeIds:[],
        // 遮罩层
        loading: true,
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        //事件类型
        eventTypeData: {},
        // 总条数
        total: 0,
        // 事件管理表格数据
        eventList: [
          {
            tunnels: {
              tunnelName: "",
            },
          },
        ],
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
          pageSize: 10,
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
          searchValue: null,
          searchValue: 1,
        },
        allmsg: "",
        process: "",
        proportion: "",
        // 表单参数
        form: {},
        eqTunnelData: {},
        // 表单校验
        rules: {
          /*  tunnelId: [{required: true, message: '请选择隧道名称', trigger: 'blur'}], */
          eventTitle: [
            {required: true, message: "请输入事件标题", trigger: "blur"},
          ],
          eventTypeId: [
            {required: true, message: "请选择事件类型", trigger: "change"},
          ],
          eventGrade: [
            {required: true, message: "请选择事件级别", trigger: "change"},
          ],
          eventLocation: [
            {required: true, message: "请输入位置", trigger: "blur"},
          ],
          eventDescription: [
            {required: true, message: "请输入内容", trigger: "blur"},
          ],
        },
        // 日期范围
        dateRange: [],
        open: false,
        details: false,
        submitEventFormLoading: false,
        direction: "rtl",
        eventForm: {
          eventType: {
            eventType: "",
          },
        },
        // 遮罩层
        dloading: false,
        // 部门树选项
        deptOptions: undefined,

      };
    },
    async created() {
      this.getTreeselect();

      await this.getDicts("sd_direction").then((data) => {
        console.log(data, "方向");
        this.directionList = data.data;
      });
      await this.getList();
      await this.getEventMsg();

      this.getEventType();
      this.getTunnel();
      this.getDicts("sd_event_state").then((response) => {
        this.eventStateOptions = response.data;
      });
      this.getDicts("sd_incident_level").then((response) => {
        console.log(response.data, "response.data事件级别");
        this.eventGradeOptions = response.data;
      });
      // 管理机构
      toll().then((res) => {
        console.log(res);
        this.mechanism = res.data;
      });
    },
    methods: {
      getTreeselect() {
        treeselectExcYG1().then((response) => {
          this.deptOptions = response.data;
          console.log(this.deptOptions);
        });
      },
      // 切换按钮
      qiehuan(inx) {
        this.queryParams.eventTypeId = "";
        this.queryParams.searchValue = inx;
        this.searchValue = inx;
        this.getEventType();
        this.getList();
      },
      accidentInit(eventId) {
        var eventId = {eventId: eventId};
        listEventFlow(eventId).then((result) => {
          this.dialogEventList = result.rows;
          console.log(this.dialogEventList);
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
      getUrl(id) {
        const param3 = {
          businessId: id,
        };
        const param4 = {
          id: id,
        };
        image(param3).then((response) => {
          console.log(response.data.length);
          if (response.data.length >= 1) {
            this.urls = response.data;
          }
        });
        video(param4).then((response) => {
          console.log(response.data, "视频信息");
          this.videoUrl = response.data.videoUrl;
        });
      },
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
        this.loading = true;
        if (this.searchValue == "2") {
          listList(this.queryParams).then((response) => {
            this.eventList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        } else {
          if (!this.dateRange) {
            this.dateRange = [];
          }
          this.queryParams.startTime = this.dateRange[0];
          this.queryParams.endTime = this.dateRange[1];
          this.queryParams.searchValue = this.searchValue;
          console.log(this.queryParams, this.addDateRange(this.queryParams));
          listEvent(this.addDateRange(this.queryParams)).then((response) => {
            console.log(response.rows, "查询事件管理列表");
            this.eventList = response.rows;
            this.$nextTick(() => {
              this.$refs.tableRef.doLayout();
            });
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
        let prevControlType = {prevControlType: this.searchValue};
        listEventType(prevControlType).then((response) => {
          console.log(response, "responseresponse");
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
          .catch(function () {
          });
      },
      /** 进入应急指挥大屏 */
      loadBigScreen(row) {
        alert("正在研发中...");
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        let that = this;
       // this.isWritable = true;
        this.searchValue = '2'
        that.reset();
        const id = row.id|| that.ids;
        getList(id).then((response) => {
          this.form = response.data;
          /*if(this.form.faultSource=="null"){
            this.form.faultSource=""
          }
          if(this.form.eqRunStatus=="undefined"){
            this.form.eqRunStatus=""
          }
          if(this.form.faultCode=="null"){
            this.form.faultCode=""
          }
          if(this.form.faultDescription=="null"){
            this.form.faultDescription=""
          }*/
          console.log("that.form.iFileList"+that.form.iFileList.length);
          that.planRoadmapUrl(that.form.iFileList);
         /* this.disstate = false;
          this.open = true;*/
          this.title = "修改故障清单";
        });
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
          eqTunnelId:null,
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
      /* handleExceed(files, fileList) {
         // let num = this.direction == 0 ? 2 : 1;
         this.$message.warning("限制上传图标个数不超过2个");
       },*/
      //监控上传文件列表
      handleChange(file, fileList) {
        this.fileList = fileList;
      },
      async planRoadmapUrl(iFileList) {
        var that = this;
        that.fileList = [];
        for (let i = 0; i < iFileList.length; i++) {
          let iconName = iFileList[i].imgName;
          // let iconUrl = await that.picture(iFileList[i].url);
          let iconUrl = iFileList[i].imgUrl
          that.fileList.push({
            name: iconName,
            url: iconUrl,
            fId: iFileList[i].id,
          });
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
        this.queryParams = {pageNum: 1, pageSize: 10};
        this.dateRange = [];
        this.tunnelList = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.open = true;
        this.title = "新增事件";
      },
      //关闭drawer
      handleClose(done) {
        this.$confirm("确认关闭？")
          .then((_) => {
            done();
          })
          .catch((_) => {
          });
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
        this.reset();
      },
      // 表格的行样式
      tableRowClassName({row, rowIndex}) {
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
</style>
