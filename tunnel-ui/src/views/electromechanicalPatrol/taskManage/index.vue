<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
        <el-button
          v-hasPermi="['system:list:add']"
          size="small"
          type="primary"
          plain
          @click="handleAdd"
        >新增
        </el-button>
        <el-button size="small" :loading="exportLoading" @click="handleExport"
        >导出
        </el-button>
        <el-button size="small" @click="resetQuery" type="primary" plain
        >刷新</el-button
        >
      </el-col>
      <el-col :span="6" :offset="14">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            placeholder="请输入巡查班组，任务名称，回车搜索"
            v-model="queryParams.zzjgId"
            @keyup.enter.native="handleQuery"
            size="small"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="task_boxShow = !task_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="searchBox" v-show="task_boxShow" ref="cc">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="queryParams.tunnelId"
            placeholder="请选择所属隧道"
            @change="changeGroup($event)"
            clearable
            size="small"

          >
            <el-option
              v-for="item in eqTunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"

            />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态" prop="publishStatus">
          <el-select
            v-model="queryParams.publishStatus"
            placeholder="请选择发布状态"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in dict.type.publish_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="任务状态" prop="taskStatus">
          <el-select
            v-model="queryParams.taskStatus"
            placeholder="请选择任务状态"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in dict.type.task_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="预完成时">
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 100%"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
          >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
          >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="listList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      class="allTable"
      height="70vh"
      :row-key="getRowKey"
      ref="tableFile"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>

      <el-table-column
        label="所属隧道"
        align="center"
        prop="tunnelName.tunnelName"
      />
      <el-table-column
        label="任务名称"
        align="center"
        prop="taskName"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="派单人员" align="center" prop="dispatcher" />
      <el-table-column
        label="派单时间"
        align="center"
        prop="dispatchTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.dispatchTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡查班组" align="center" prop="bzName">
      </el-table-column>
      <!--      <el-table-column label="任务描述" align="center" prop="taskDescription" />-->
      <el-table-column
        label="预完成时"
        align="center"
        prop="endPlantime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.endPlantime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="publishStatus">
        <template slot-scope="scope">
          <!-- <dict-tag
            :options="dict.type.publish_status"
            :value="scope.row.publishStatus"
          /> -->
          <span
            :style="{
              color:
                scope.row.publish == '已废止'
                  ? 'red'
                  : scope.row.publish == '未发布'
                  ? 'yellow'
                  : '#00FF00',
            }"
          >{{ scope.row.publish }}</span
          >
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="task">
        <template slot-scope="scope">
          <!-- <dict-tag
            :options="dict.type.task_status"
            :value="scope.row.taskStatus"
          /> -->
          <span
            :style="{
              color:
                scope.row.task1 == '待巡检'
                  ? 'yellow'
                  : scope.row.task1 == '巡检中'
                  ? '#00FF00'
                  : '#60BCFD',
            }"
          >{{ scope.row.task1 }}</span
          >
          <span v-show="scope.row.task2" class="chaoshi">{{
              scope.row.task2
            }}</span>
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
            @click="handleRecordy(scope.row)"
          >任务详情</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleAbolish(scope.row)"
            :style="{ display: scope.row.task1 == '已完结'? 'none': scope.row.publishStatus == 2 ? '' :'none' }"
          >废止任务</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="exportTaskReport(scope.row)"
            :style="{ display: (scope.row.task).indexOf( '已完结')>=0 ? '' : 'none' }"
          >巡查报告</el-button
          >
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:list:edit']"
            :style="{ display: scope.row.publishStatus != 0 ? 'none' : scope.row.task1 == '已完结'? 'none':'' }"
          >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:list:remove']"
            :style="{ display: scope.row.publishStatus != 0 ? 'none' : scope.row.task1 == '已完结'? 'none':'' }"
          >删除</el-button
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

    <el-dialog
      :title="title"
      :visible.sync="open"
      width="70%"
      class="xjDialog"
      :before-close="cancel"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <!--      <h1>新增巡检任务</h1>-->
      <el-card>
        <div class="task">
          <div class="topTxt" style="margin-bottom: 20px">巡查任务基本信息</div>
          <div class="tableTopHr" style="display: none"></div>
          <el-form
            :inline="true"
            ref="form"
            :model="form"
            :rules="rules"
            label-width="110px"
          >
            <el-row>

              <el-col :span="8">
                <el-form-item label="任务名称" prop="taskName">
                  <el-input
                    type="text"
                    placeholder="请输入内容"
                    v-model="form.taskName"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="所属隧道" prop="tunnelId">
                  <el-select
                    v-model="form.tunnelId"
                    placeholder="请选择所属隧道"
                    @change="changeGroup($event)"
                  >
                    <el-option
                      v-for="item in eqTunnelData"
                      :key="item.tunnelId"
                      :label="item.tunnelName"
                      :value="item.tunnelId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="巡查班组" prop="bzId">
                  <el-select
                    v-model="form.bzId"
                    placeholder="请选择巡查班组"
                    id="bzSel"
                  >
                    <el-option
                      v-for="item in bzData"
                      :key="item.deptId"
                      :label="item.deptName"
                      :value="item.deptId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="派单人员" prop="dispatcher">
                  <el-select
                    v-model="form.dispatcher"
                    disabled="disabled"
                    placeholder="默认当前登录人"
                  >
                    <el-option
                      v-for="item in userListData"
                      :key="item.userId"
                      :label="item.nickName"
                      :value="item.userId"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <!--                <el-form-item label="派单人员" prop="dispatcher">
                                          <el-input
                                            ref="dispatcher"
                                            disabled="disabled"
                                            v-model="form.dispatcher"
                                            placeholder="（默认当前登录人）"
                                          ></el-input>
                                        </el-form-item>&ndash;&gt;-->
              </el-col>
              <el-col :span="8">
                <el-form-item label="派单时间" prop="dispatchTime">
                  <el-date-picker
                    clearable
                    size="small"
                    disabled="disabled"
                    v-model="form.dispatchTime"
                    type="datetime"
                    value-format="yyyy-MM-dd hh:mm:ss"
                    placeholder="选择派单时间"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="预完成时" prop="endPlantime">
                  <el-date-picker
                    clearable
                    :picker-options="forbiddenTime"
                    size="small"
                    v-model="form.endPlantime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    @change="handleEndTime"
                    placeholder="选择预完成时"
                    @focus="focus"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="任务描述" prop="taskDescription">
                  <el-input
                    type="textarea"
                    :rows="5"
                    maxlength="255"
                    show-word-limit
                    placeholder="请输入内容"
                    v-model="form.taskDescription"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-card>
      <el-card>
        <div class="patrol">
          <div class="topTxt">巡查点信息</div>
          <div class="tableTopHr" style="display: none"></div>
          <div class="button-father">
            <el-button type="primary" style="height: 15%" @click="show1"
            >选择巡检点</el-button
            >
            <el-button type="primary" style="height: 15%" @click="show2"
            >选择故障点</el-button
            >
            <!--          <el-button type="primary" style="height: 15%" disabled
            >导入巡查计划</el-button
          >-->
          </div>
          <div
            class="box-father"
            style="display: block; max-height: 16vh; overflow: auto"
          >
            <div style="height: 4vh">
              <div class="titleRow" style="width: 8%">序号</div>
              <div class="titleRow" style="width: 12%">所属隧道</div>
              <div class="titleRow" style="width: 15%">设备/故障类型</div>
              <div class="titleRow" style="width: 20%">设备名称</div>
              <div class="titleRow" style="width: 30%">设备位置</div>
              <div class="titleRow" style="width: 15%">操作</div>
            </div>
            <div class="box" :key="index" v-for="(item, index) in boxList">
              <div class="contentTextRow" style="float: left">
                <div class="number" style="width: 10%">{{ index + 1 }}</div>
                <div class="text" style="padding-left: 0px; margin-left: 0px">
                  <div style="width: 12%">{{ item.tunnel_name }}</div>
                  <div style="width: 15%; margin-left: 30px">
                    {{ item.type_name }}
                  </div>
                  <div style="width: 20%; margin-left: 40px">
                    {{ item.eq_name }}
                  </div>
                  <div style="width: 30%; margin-left: 62px">
                    {{ item.pile }}
                  </div>
                </div>
              </div>

              <template @slot="scop">
                <div
                  :class="index == 0 ? 'disabledClass' : 'top'"
                  @click="clickUP(index, item)"
                  style="float: left; margin-left: -40px"
                >
                  <i class="el-icon-top"></i>
                </div>
              </template>
              <div
                :class="
                  boxList.length == index + 1 ? 'disabledClass' : 'bottom'
                "
                @click="clickDown(index, item)"
                style="float: left"
              >
                <i class="el-icon-bottom"></i>
              </div>
              <div
                class="delete"
                style="float: left"
                @click="clickDelete(index, item)"
              >
                <i class="el-icon-delete-solid"></i>
              </div>
            </div>
          </div>
          <div class="dialog-footer">
            <el-button class="zancunButton" @click="save">暂存</el-button>
            <el-button
              style="display: none"
              class="closeButton"
              @click="abolish"
            >废止</el-button
            >
            <el-button class="submitButton" @click="release">发布</el-button>
          </div>
        </div>
      </el-card>
    </el-dialog>

    <el-dialog
      :visible.sync="isShow1"
      width="50%"
      class="show"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="show-left">
        <div class="show-title">设备位置</div>
        <el-tree
          class="tree"
          :data="treeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :show-checkbox="show_checkbox"
          :check-strictly="check_strictly"
          :filter-node-method="filterNode"
          ref="tree"
          accordion
          default-expand-all
          @node-click="handleNodeClick1"
          node-key="id"
          highlight-current
        />
      </div>
      <div class="show-right">
        <div class="show-title">设备清单</div>
        <div class="right-button">
          <el-select v-model="options1value" @change="changeDevList">
            <el-option
              v-for="dict in dict.type.eq_system"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <div >

            <el-input placeholder="请输入设备类型、名称" clearable  @keyup.enter.native="getTable()"   v-model="searchValue" class="input-with-select">
              <el-button slot="append" icon="el-icon-search"  @click.native.prevent="getTable()"></el-button>
            </el-input>
          </div>
          <div class="cancel-determine">
            <el-button @click="cancelDetermine1">取消</el-button>
            <el-button type="primary" @click="determine1">确定</el-button>
          </div>
        </div>
        <div class="table-father">
          <el-table
            ref="multipleTable1"
            :data="tableData1"
            tooltip-effect="dark"
            :header-cell-style="{ 'text-align': 'center', padding: '0px' }"
            :cell-style="{ 'text-align': 'center', padding: '0px' }"
            :header-row-style="{
              height: '30px',
            }"
            :row-style="{
              height: '30px',
            }"
            style="width: 100%; border: solid 1px #284159 !important"
            border
            height="412px"
            class="dialogTable"
            :row-key="getRowKey1"
            @selection-change="onSiteInspectionSelection"
          >
            <el-table-column
              type="selection"
              width="39"
              reserve-selection
            ></el-table-column>
            <el-table-column
              type="index"
              :index="indexMethod1"
              label="序号"
              width="68"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="type_name"
              label="设备类型"
              width="160"
            ></el-table-column>
            <el-table-column prop="eq_name" label="设备名称" width="200">
            </el-table-column>
            <el-table-column prop="pile" label="设备位置"> </el-table-column>
            <el-table-column prop="dict_label" label="方向"> </el-table-column>
          </el-table>
          <pagination
            v-show="dialogTotal > 0"
            :total="dialogTotal"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            @pagination="getDialogList"
            small
          />
        </div>
      </div>
    </el-dialog>

    <!-- -->

    <el-dialog
      :visible.sync="isShow2"
      width="50%"
      class="show"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="show-left">
        <div class="show-title">设备位置</div>
        <el-tree
          class="tree"
          :data="treeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :show-checkbox="show_checkbox"
          :check-strictly="check_strictly"
          :filter-node-method="filterNode"
          ref="tree"
          accordion
          default-expand-all
          @node-click="handleNodeClick2"
          node-key="id"
          highlight-current
        />
      </div>
      <div class="show-right">
        <div class="show-title">故障清单</div>
        <div class="right-button">
          <el-select v-model="options2value" @change="getGzTable">
            <el-option
              v-for="dict in dict.type.fault_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
          <div class="cancel-determine">
            <el-button @click="cancelDetermine2">取消</el-button>
            <el-button type="primary" @click="determine2">确定</el-button>
          </div>
        </div>
        <div class="table-father">
          <el-table
            ref="multipleTable2"
            :data="tableData2"
            tooltip-effect="dark"
            :header-cell-style="{ 'text-align': 'center', padding: '0px' }"
            :cell-style="{ 'text-align': 'center', padding: '0px' }"
            :header-row-style="{
              height: '30px',
            }"
            :row-style="{
              height: '30px',
            }"
            style="width: 100%; border: solid 1px #284159 !important"
            border
            height="412px"
            class="dialogTable"
            :row-key="getRowKey1"
            @selection-change="onSiteInspectionSelection"
          >
            <el-table-column
              type="selection"
              width="39"
              reserve-selection
            ></el-table-column>
            <el-table-column
              type="index"
              :index="indexMethod1"
              label="序号"
              width="68"
              align="center"
            ></el-table-column>
            <el-table-column prop="type_name" label="故障类型">
            </el-table-column>
            <el-table-column prop="eq_name" label="故障设备名称">
            </el-table-column>
            <el-table-column prop="pile" label="故障位置"> </el-table-column>
            <el-table-column prop="fault_fxtime" label="发现时间">
              <template slot-scope="scope">
                <span>{{
                    !!scope.row.fault_fxtime
                      ? scope.row.fault_fxtime
                        .replace(/T/g, " ")
                        .replace(/.[\d]{3}Z/, " ")
                      : ""
                  }}</span>
              </template></el-table-column
            >
            <el-table-column prop="dict_label" label="故障描述">
            </el-table-column>
          </el-table>
          <pagination
            v-show="dialogTotal > 0"
            :total="dialogTotal"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            @pagination="getDialogGzList"
            small
          />
        </div>
      </div>
    </el-dialog>

    <!--巡查任务及执行记录单-->
    <el-dialog
      :visible.sync="record"
      :before-close="closeRecord"
      width="70%"
      title="巡检任务及执行记录单"
      class="xjDialog"
      :close-on-click-modal="false"
      :destroy-on-close ="true"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div class="col-1" v-for="(ite, index) in taskNews2" :key="index">
        发布状态/执行状态：
        <div class="col-card" v-show="ite.publishStatus">
          {{ ite.publishStatus }}
        </div>
        <div class="col-card" v-show="ite.taskStatus">{{ ite.taskStatus }}</div>
        <div v-show="!ite.publishStatus && !ite.taskStatus">暂无状态</div>
      </div>
      <div class="card" v-for="(item, index) in taskNews1" :key="index">
        <el-row style="margin-left: 2em">
          <el-col :span="8">
            <div>任务编号：</div>
            <span>{{ item.id }}</span>
          </el-col>
          <el-col :span="8" style="display:inline-block">
            <div style="display:inline-block">任务名称：</div>
            <span style="width: calc(100% - 110px);display:inline-flex">{{ item.taskName }}</span>
          </el-col>
          <!--          <el-col :span="8">
            <div>所属单位：</div>
            <span>{{ item.zzjgId }}</span>
          </el-col>-->
          <el-col :span="8">
            <div>巡查班组：</div>
            <span>{{ item.bzName }}</span>
          </el-col>
          <el-col :span="8">
            <div>派单人员：</div>
            <span>{{ item.dispatcher }}</span>
          </el-col>
          <el-col :span="8">
            <div>派单时间：</div>
            <span>{{ item.dispatchTime }}</span>
          </el-col>
          <el-col :span="8">
            <div>预完成时：</div>
            <span>{{ item.endPlantime }}</span>
          </el-col>
          <el-col :span="24" style="display:inline-block">
            <div style="display:inline-block">任务描述：</div>
            <span style="width: calc(100% - 110px);display:inline-flex">{{
                item.taskDescription == "null" ? "" : item.taskDescription
              }}</span>
          </el-col>
        </el-row>
      </div>
      <div class="card" v-for="(pat, index) in patrolNews" :key="index">
        <el-row>
          <!--            <el-col :span="2" class="topTxt">
              {{index+1}}设备巡检点:
            </el-col>-->
          <div class="topTxt">
            {{ index + 1 }}&nbsp;&nbsp;&nbsp;&nbsp;、设备巡检点
          </div>
          <el-col
            :span="2"
            style="margin-top: -34px; margin-left: 37%; width: 50%"
          >
            <span style="width: 15%">{{ pat.tunnelName }}</span
            ><span>{{ pat.eqName }}</span>
          </el-col>
          <!--            <el-col :span="2" style="width: auto;margin-top: -34px;margin-left: 37%;" >
              {{ pat.eqName }}
            </el-col>-->
          <!--            <el-col :span="2" >
            {{ pat.xcTime }}
            </el-col>-->
        </el-row>
        <el-row style="margin-left: 2em; margin-top: 10px">

          <el-col :span="8">
            <div>外观情况：</div>
            <span>{{ pat.impression }}</span>
          </el-col>
          <el-col :span="8">
            <div style="width: auto;">通信和网络情况：</div>
            <span>{{ pat.network }}</span>
          </el-col>
          <el-col :span="8">
            <div>供配电情况：</div>
            <span>{{ pat.power }}</span>
          </el-col>
<!--          <el-col :span="8">
            <div>现场故障情况：</div>
            <span>{{ pat.eqFaultCode }}</span>
          </el-col>-->

          <el-col :span="8">
            <div>设备状态：</div>
            <span>{{ pat.eqStatus }}</span>
          </el-col>
          <el-col v-if="pat.runStatus" :span="8">
            <div>设备运行状态：</div>
            <span>{{ pat.runStatus }}</span>
          </el-col>
          <el-col :span="8">
            <div>设备描述：</div>
            <span>{{ pat.eqFaultDescription }}</span>
          </el-col>
          <el-col>
            <div style="width:12%">现场情况照片：</div>
            <div v-for="(pic, index) in pat.iFileList" :key="index" style = "padding-right: 30px;">
              <img :src="pic.imgUrl"  @click="openPic(pic.imgUrl)"/>
            </div>
          </el-col>
        </el-row>
        <!-- </div> -->
      </div>
      <div class="card" v-for="(tas, index) in taskNews" :key="index">
        <el-row style="width: 100%; margin-left: 1em">
          <el-col :span="8">
            <div>任务执行状态：</div>
            <span>{{ tas.taskStatus }}</span>
          </el-col>
          <el-col :span="8">
            <div>巡查班组：</div>
            <span>{{ tas.bzName }}</span>
          </el-col>
          <el-col :span="8">
            <div>执行巡查人：</div>
            <span>{{ tas.walkerId }}</span>
          </el-col>
          <el-col :span="8">
            <div>任务完成时间：</div>
            <span>{{ tas.taskEndtime }}</span>
          </el-col>
          <el-col :span="8">
            <div>任务持续时长：</div>
            <span>{{ tas.taskCxtime }}</span>
            <span :class="{ active: isActive }">{{ tas.ifchaosgu }}</span>
          </el-col>
          <el-col :span="8">
            <div>现场情况描述：</div>
            <span>{{
                tas.siteDescription == "null" ? "" : tas.siteDescription
              }}</span>
          </el-col>
        </el-row>
      </div>
      <div class="card">
        <el-row style="margin-left: 2em">
          <el-col :span="4">
            <span style="color: #05aafd">序号</span>
          </el-col>
          <el-col :span="4">
            <span style="color: #05aafd">操作类型</span>
          </el-col>
          <el-col :span="8">
            <span style="color: #05aafd"> 操作记录</span>
          </el-col>
          <el-col :span="8">
            <span style="color: #05aafd"> 操作时间</span>
          </el-col>
        </el-row>
        <el-row
          v-show="taskOpt.length > 0"
          v-for="(item, index) in taskOpt"
          :key="index"
          style="margin-left: 2em"
        >
          <el-col :span="4">
            <span>{{ index + 1 > 9 ? index + 1 : index + 1 }}</span>
          </el-col>
          <el-col :span="4">
            <span>{{ item.optType }}</span>
          </el-col>
          <el-col :span="8" style="text-align: center !important">
            <span> {{ item.tunnelName }} / {{ item.optPersonId }}</span>
          </el-col>
          <el-col :span="8">
            <span> {{ item.optTime }}</span>
          </el-col>
        </el-row>
        <div v-show="taskOpt.length == 0">
          <div
            style="text-align: center; margin-top: 20px; margin-bottom: 20px"
          >
            暂无执行记录
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="picVisible"
      width="60%"
      :before-close="handleCloseImg"
      :close-on-click-modal="false"
      class="picDialog"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img :src="picUrl" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<script>
import $ from "jquery";

import {
  listList,
  getList,
  delList,
  addList,
  updateList,
  exportList,
  getTaskInfoList,
  listBz,
  treeselect,
  getDevicesList,
  abolishList,
  addTask,
  getFaultList,
  updateTask,
  //test11,
  selectBzByTunnel, getUserInfo,
} from "@/api/electromechanicalPatrol/taskManage/task";
import {
  getEquipmentInfo,
  getRepairRecordList,
} from "@/api/electromechanicalPatrol/faultManage/fault";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { color } from "echarts";
import { download } from "@/utils/request";
import {getUser} from "@/api/system/user";

export default {
  name: "List",
  //字典值：任务发布状态,任务状态
  dicts: [
    "publish_status",
    "task_status",
    "network",
    "power",
    "eq_system",
    "fault_level",
    "opt_type",
  ],
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true,
    },
    //节点是否可被选择
    show_checkbox: {
      type: Boolean,
      default: false,
    },
    //是否级联
    check_strictly: {
      type: Boolean,
      default: false,
    },
    //开启默认全选
    default_check_all: {
      type: Boolean,
      default: false,
    },
    //开启默认选中第一个子节点
    default_check_first: {
      type: Boolean,
      default: false,
    },
    //默认第一个子节点高亮选中
    default_select_first: {
      type: Boolean,
      default: false,
    },
    // powerCode:{
    //   type:String,
    //   default:''
    // },
  },
  data() {
    return {
      picUrl:'',
      picVisible:false,
      isActive: false,
      task_boxShow: false,
      isClick: true,
      userName: "",
      currentTime: "",
      deviceType: "",
      searchValue:"",
      faultLevel: "",
      // 获取巡检点 表格选中项
      dialogSelection: [],
      // 日期范围
      dateRange: [],
      dialogTotal: 0,
      pageNum: 1,
      pageSize: 10,
      taskName: "",
      tunnelId: "",
      defaultProps: {
        value: "id",
        label: "label",
        children: "children",
      },
      treeData: [],
      tableData1: [],
      tableData2: [],
      //所属隧道
      eqTunnelData: {},
      userListData: {},
      options1value: "", //设备清单绑定
      options2value: "", //故障清单绑定
      boxList: [], //巡检点list
      thatboxList: [], //that巡检点list
      boxTolList: [], //巡检点故障点总list
      boxIds: "", //巡检点ids
      faultList: [], //故障点list
      options1: [], //设备清单
      record: false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 巡查任务表格数据
      listList: [],
      //巡查班组
      bzData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 弹框内容是否重置
      openCz: false,
      openGz: false,
      //新增巡查点弹窗
      isShow1: false,
      //新增故障点弹窗
      isShow2: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        zzjgId: null,
        endPlantime: "",
        dispatcher: null,
        dispatchTime: "",
        bzId: null,
        taskDescription: "",
        publishStatus: null,
        taskStatus: null,
        tunnelId: null,
        walkerId: null,
        taskEndtime: "",
        taskCxtime: "",
        siteDescription: "",
        ids: "",
      },
      // 任务详情参数
      taskNews: {
        bzId: "",
        dispatcher: "",
        taskDescription: "",
        taskStatus: "",
        ifchaosgu: "",
        walkerId: "",
      },
      // 任务详情参数
      taskNews2: {
        taskStatus: "",
        publishStatus: "",
      },
      // 任务详情参数
      taskNews1: {
        id: "",
        zzjgId: "",
        bzName: "",
        endPlantime: "",
        dispatcher: "",
        dispatchTime: "",
        taskDescription: "",
      },
      //操作记录
      taskOpt: {
        optType: "",
        optPersonId: "",
        optTime: "",
        optDescription: "",
        tunnelName: "",
      },
      //巡查点参数
      patrolNews: {
        tunnelName: "",
        xcTime: "",
        bzId: "",
        walkerId: "",
        impression: 0,
        network: 0,
        power: 0,
        eqStatus: "",
        runStatus: "",
        eqFaultDescription: "",
      },
      //禁用当前日期之前的日期
      forbiddenTime: {
        disabledDate(time) {
          //Date.now()是javascript中的内置函数，它返回自1970年1月1日00:00:00 UTC以来经过的毫秒数。
          return time.getTime() < (Date.now()+24*60*60*1000 - 8.64e7);
        },
      },
      // 表单参数
      form: {},
      // 表单校验指派巡查班组
      rules: {
        bzId: [{ required: true, message: "请选择巡查班组", trigger: "blur" }],
        taskName: [
          {
            required: true,
            message: "请填写任务名称",
            trigger: "blur",
          },
          { max: 50, message: '最长输入50个字符', trigger: 'change' }
        ],
        tunnelId: [
          {
            required: true,
            message: "请选择所属隧道",
            trigger: "blur",
          },
        ],
      },
      impressionOptions: [], //外观情况
      networkOptions: [], //网络情况  opt_type
      powerOptions: [], //配电情况
      optTypeOptions: [], //操作类型
    };
  },
  created() {
    this.getList();
    this.getTunnel();
    this.getTreeSelect();

    //this.test11()
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
    //操作类型
    this.getDicts("opt_type").then((response) => {
      this.optTypeOptions = response.data;
    });
    //获取当前登录人
    //this.userName = this.$store.state.user.name;
    this.currentTime = this.getCurrentTime();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    handleCloseImg(){
      this.picVisible = false
    },
    openPic(src){
      this.picUrl = src
      this.picVisible = true
    },
    changeDevList(){
      this.searchValue = '';
      this.getTable(this.options1value);
    },

    //更换隧道，更新隧道下关联班组列表
    changeGroup(e){
      this.getBz(e)
      this.form.bzId = '';
      this.boxList = [];
    },
    closeRecord() {
      // 关闭弹出层
      this.$refs.tableFile.clearSelection();
      this.record = false;
    },
    handleRowClick(row) {
      this.$refs.tableFile.toggleRowSelection(row);
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      // console.log(row,"row")
      return row.id;
    },
    getRowKey1(row) {
      // console.log(row,"row")
      return row.eq_id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (self.task_boxShow == true) {
        if (
          !this.$refs.main.contains(e.target) &&
          !this.$refs.cc.contains(e.target)
        ) {
          if (self.task_boxShow == true) {
            self.task_boxShow = false;
          }
        }
      }
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    //翻页时不刷新序号
    indexMethod1(index) {
      return index + (this.pageNum - 1) * this.pageSize + 1;
    },
    //班组点击时间
    /*selChange() {
        if (typeof this.form.tunnelId == "undefined") {
          this.$modal.msgWarning("请先选择隧道");
          return;
        } else {
          $("#bzSel").attr("pointer-events", "none");
        }
      },*/
    /*获取当前时间*/
    getCurrentTime() {
      //获取当前时间并打印
      var _this = this;
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth() + 1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf =
        new Date().getMinutes() < 10
          ? "0" + new Date().getMinutes()
          : new Date().getMinutes();
      let ss =
        new Date().getSeconds() < 10
          ? "0" + new Date().getSeconds()
          : new Date().getSeconds();
      _this.gettime = yy + "-" + mm + "-" + dd + " " + hh + ":" + mf + ":" + ss;
      return _this.gettime;
    },

    /*tunnelSelectGet(e) {
        this.tunnelId = e;
        selectBzByTunnel(this.tunnelId).then((response) => {
          this.form.bzId = response.data;
          console.log(response.data, "隧道部门树");
        });
        treeselect(this.tunnelId).then((response) => {
          this.treeData = response.data;
          console.log(response.data, "隧道部门树");
        });
      },*/

    //  上移
    clickUP(i, item) {
      if (item && i) {
        let obj = { ...this.boxList[i - 1] };
        this.boxList.splice(i - 1, 1, item);
        this.boxList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    // 下移
    clickDown(i, item) {
      if (item && typeof i === "number") {
        let obj = { ...this.boxList[i + 1] };
        this.boxList.splice(i + 1, 1, item);
        this.boxList.splice(i, 1, obj);
        this.$forceUpdate();
      }
    },
    unique(arr) {
      const res = new Map();
      return arr.filter((arr) => !res.has(arr.eq_id) && res.set(arr.eq_id, 1));
    },
    arraySort(property) {
      return function (a, b) {
        var value1 = a[property];
        var value2 = b[property];
        return value1 - value2;
      };
    },
    clickDelete(i, item) {
      if (item && typeof i === "number") {
        //splice 操作数组的方法
        if (i > -1) {
          this.boxList.splice(i, 1);
        }
        console.log("clickDelete====================" + this.boxList);
      }
    },
    // 弹窗表格翻页
    getDialogList() {
      this.getTable();
    },
    // 弹窗表格翻页
    getDialogGzList() {
      this.getGzTable();
    },
    // 获取巡检点弹窗表格选中项
    onSiteInspectionSelection(selection) {
      this.dialogSelection = selection;
      console.log(this.dialogSelection, "this.dialogSelection");
    },
    /** 所属隧道 */
    getTunnel() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      listTunnels(this.queryParams).then((response) => {
        console.log(response.rows, "所属隧道列表");
        this.eqTunnelData = response.rows;
      });
    },
    /*获取当前登录人*/
    getLoginUser(){
      getUserInfo().then((response) => {
        this.userListData = response.rows;
        this.form.dispatcher = this.userListData[0].userId;
      });
    },


    // 获取设备table
    getTable(deviceType) {
      if (deviceType) {
        this.deviceType = deviceType;
      }
      getDevicesList(
        this.searchValue,
        this.tunnelId,
        this.deviceType,
        this.pageNum,
        this.pageSize
      ).then((res) => {
        this.tableData1 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          //console.log(this.boxList[0].eq_type, deviceType, "0000000000");
          // if (this.boxList[0].eq_type == deviceType) {
          this.tableData1.forEach((item) => {
            this.boxList.forEach((row) => {
              const eq_id = row.eq_id.slice(0, -2);
              if (item.eq_id == eq_id) {
                this.$nextTick(() => {
                  this.$refs.multipleTable1.toggleRowSelection(item, true);
                });
              }
            });
          });
          // }
        } else {
          this.$refs.multipleTable1.clearSelection();
        }
      });
    },
    // 获取设备table
    getGzTable(deviceType) {
      if (deviceType) {
        this.faultLevel = deviceType;
      }
      getFaultList(
        this.tunnelId,
        this.faultLevel,
        this.pageNum,
        this.pageSize
      ).then((res) => {
        console.log(res, "获取故障table");
        console.log(
          "==================getFaultListthis.boxList==" + this.boxList,
          "boxList"
        );
        this.tableData2 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          // console.log(this.boxList[0].eq_type, deviceType, "0000000000");
          // if (this.boxList[0].eq_type == deviceType) {
          this.tableData2.forEach((item) => {
            this.boxList.forEach((row) => {
              const eq_id = row.eq_id.slice(0, -2);
              if (item.eq_id == eq_id) {
                this.$nextTick(() => {
                  this.$refs.multipleTable2.toggleRowSelection(item, true);
                });
              }
            });
          });
          // }
        } else {
          this.$refs.multipleTable2.clearSelection();
        }
      });
    },
    //设备节点单击事件
    handleNodeClick1(data) {
      this.tunnelId = data.id;

      getDevicesList(
        this.searchValue,
        this.tunnelId,
        this.deviceType,
        this.pageNum,
        this.pageSize
      ).then((res) => {
        console.log(res, "获取设备table");
        console.log(this.boxList, "boxList");

        this.tableData1 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          // if (this.boxList[0].eq_type == deviceType) {
          this.tableData1.forEach((item) => {
            this.boxList.forEach((row) => {
              const eq_id = row.eq_id.slice(0, -2);
              if (item.eq_id == eq_id) {
                this.$nextTick(() => {
                  this.$refs.multipleTable1.toggleRowSelection(item, true);
                });
              }
            });
          });
          // }
        } else {
          this.$refs.multipleTable1.clearSelection();
        }
      });
    },
    //故障节点单击事件
    handleNodeClick2(data) {
      this.tunnelId = data.id;
      getFaultList(
        this.tunnelId,
        this.faultLevel,
        this.pageNum,
        this.pageSize
      ).then((res) => {
        console.log(res, "获取故障table");
        console.log(
          "==================getFaultListthis.boxList==" + this.boxList,
          "boxList"
        );
        this.tableData2 = res.rows;
        this.dialogTotal = res.total;
        if (this.boxList != []) {
          // if (this.boxList[0].eq_type == deviceType) {
          this.tableData2.forEach((item) => {
            this.boxList.forEach((row) => {
              const eq_id = row.eq_id.slice(0, -2);
              if (item.eq_id == eq_id) {
                this.$nextTick(() => {
                  this.$refs.multipleTable2.toggleRowSelection(item, true);
                });
              }
            });
          });
          // }
        } else {
          this.$refs.multipleTable2.clearSelection();
        }
      });
    },
    // 筛选节点
    filterNode(value, data) {
      console.log(value, data, "筛选节点");
      if (!value) return true;
      return data.loopName.indexOf(value) !== -1;
    },

    handleRecordy(row) {
      this.record = true;
      this.taskId = row.id;
      getTaskInfoList(this.taskId).then((response) => {
        this.taskNews = response.data.task;
        if (response.data.task[0].ifchaosgu == "超时") {
          this.isActive = true;
        } else {
          this.isActive = false;
        }
        this.taskNews1 = response.data.task;
        this.taskNews2 = response.data.task;
        this.patrolNews = response.data.patrol;
        this.taskOpt = response.data.opt;
        this.impressionOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.impression == opt.dictValue) {
              taskitem.impression = opt.dictLabel;
            }
          });
        });

        this.networkOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.network == opt.dictValue) {
              taskitem.network = opt.dictLabel;
            }
          });
        });

        this.powerOptions.forEach((opt) => {
          this.patrolNews.forEach((taskitem) => {
            if (taskitem.power == opt.dictValue) {
              taskitem.power = opt.dictLabel;
            }
          });
        });

        this.optTypeOptions.forEach((opt) => {
          this.taskOpt.forEach((taskitem) => {
            if (taskitem.optType == opt.dictValue) {
              taskitem.optType = opt.dictLabel;
            }
          });
        });
      });
    },
    //handle实现插件能选取当前时间的时、分、秒，但是选择完毕之后，只要选择的时、分、秒小于当前时间，会自动填充为当前的时、分、秒
    handleEndTime: function () {
      var startAt = (new Date(this.form.endPlantime) * 1000) / 1000;
      if (startAt < Date.now()) {
        this.form.endPlantime = new Date();
        //时间格式转换
        var d = new Date(this.form.endPlantime);
        this.form.endPlantime =
          d.getFullYear() +
          "-" +
          (d.getMonth() + 1) +
          "-" +
          d.getDate() +
          " " +
          d.getHours() +
          ":" +
          d.getMinutes() +
          ":" +
          d.getSeconds();
      }
    },

    /** 查询巡查任务列表 */
    getList() {
      this.loading = true;
      listList(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          console.log(response.rows, "发布状态列表");
          for (let item of response.rows) {
            if (item.task.indexOf(",") > -1) {
              console.log(item, "itemitemitemitem");
              item.task1 = item.task.split(",")[0];
              item.task2 = item.task.split(",")[1];
            } else {
              item.task1 = item.task;
            }
          }
          this.listList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.tableFile.clearSelection();
      this.reset();
    },

    /** 巡查班组 */
    getBz(tunnelId) {

      let param = {
        "tunnelId":tunnelId
      }

      listBz(param).then((response) => {
        this.bzData = response.rows;
      });
    },
    /** 隧道部门树 */
    getTreeSelect() {
      if (typeof this.form.tunnelId == "undefined") {
        return;
      }
      treeselect().then((response) => {
        this.treeData = response.data;
        console.log(response.data, "隧道部门树");
      });
    },


    /** 测试 */
    // test11() {
    //
    //   test11().then((response) => {
    //
    //   });
    // },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        zzjgId: null,
        endPlantime: "",
        dispatcher: "",
        dispatchTime: "",
        bzId: null,
        taskDescription: "",
        publishStatus: 0,
        taskStatus: 0,
        walkerId: null,
        taskEndtime: "",
        taskCxtime: "",
        taskName: "",
        siteDescription: "",
        createBy: "",
        createTime: "",
        updateBy: "",
        updateTime: "",
        devicesList: "",
      };
      this.boxList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;

      this.task_boxShow = false;
      this.$refs.tableFile.clearSelection();
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.ids = "";
      this.queryParams.zzjgId = "";
      this.dateRange = [];
      this.handleQuery();
    },
    show1() {
      //this.tableData1 = null
      if (typeof this.form.tunnelId == "undefined") {
        return this.$modal.msgWarning("请选择所属隧道");
      }
      this.dialogSelection = [];
      //  this.$refs.multipleTable1.toggleRowSelection(item, true);
      this.isShow1 = true;
      this.searchValue = '';
      this.tunnelId = this.form.tunnelId;
      treeselect(this.tunnelId).then((response) => {
        this.treeData = response.data;
        console.log(response.data, "隧道部门树");
      });

      this.options1value = "0";
      this.getTable(this.options1value);
      //点击确定，数据还原
      if (this.openCz) {
        this.options1value = "0";
        this.tableData1 = null;
        this.dialogTotal = null;
        console.log("=========" + this.options1value);
        this.getTable(this.options1value);
      }
      this.openCz = false;
    },
    show2() {
      //this.tableData1 = null
      if (typeof this.form.tunnelId == "undefined") {
        return this.$modal.msgWarning("请选择所属隧道");
      }
      this.isShow2 = true;
      this.options2value = "0";
      this.tunnelId = this.form.tunnelId;

      treeselect(this.tunnelId).then((response) => {
        this.treeData = response.data;
        console.log(response.data, "隧道部门树");
      });

      this.getGzTable(this.options2value);
      if (this.openGz) {
        this.options2value = "0";
        this.tableData2 = null;
        this.dialogTotal = null;
        console.log("=========" + this.options2value);
        this.getGzTable(this.options2value);
      }
      this.openGz = false;
    },
    cancelDetermine1() {
      this.dialogSelection = [];
      this.isShow1 = false;
    },
    cancelDetermine2() {
      this.dialogSelection = [];
      this.isShow2 = false;
    },
    determine1() {
      this.isShow1 = false;
      this.dialogSelection.forEach((item) => {
        item.eq_id = item.eq_id + "_1";
      });
      this.boxList = this.unique(this.boxList.concat(this.dialogSelection));
      this.dialogSelection = [];
    },
    determine2() {
      this.isShow2 = false;
      this.dialogSelection.forEach((item) => {
        item.eq_id = item.eq_id + "_2";
      });
      this.boxList = this.unique(this.boxList.concat(this.dialogSelection));
      this.dialogSelection = [];
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.boxList = "";
      this.boxIds = "";
      this.bzData = [];
      this.reset();
      this.open = true;
      this.openGz = true;
      this.openCz = true;
      this.title = "新增巡查任务";
      this.tableData1 = null;
      this.tableData2 = null;
      this.form.dispatcher = this.userName;
      this.form.dispatchTime = this.currentTime;
      this.getLoginUser();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      let that = this;
      getList(id).then((response) => {
        that.form = response.data.task[0];
        this.boxList = response.data.list;
        console.log("handleUpdate=============" + this.boxList);
        // this.tableData1 = response.data.devicesPatrolList;//巡检点
        // this.tableData2 = response.data.faultPatrolList;//故障点
        this.boxList.forEach((item) => {
          this.$nextTick(() => {
            this.$refs.multipleTable2.toggleRowSelection(item, true);
          });
        });
        this.boxList.forEach((item) => {
          if (item.patrol_type == 0) {
            item.eq_id = item.eq_id + "_1";
          } else {
            item.eq_id = item.eq_id + "_2";
          }
        });
        if (this.form.taskDescription == "null") {
          this.form.taskDescription = "";
        }

        /*if(this.form.bzId!=null&&this.form.bzId!=""&&this.form.bzId!="null"){
              this.form.bzId = parseInt(this.form.bzId)
          }else{
            this.form.bzId=""
          }*/
        this.boxList.sort(this.arraySort("xc_sort"));
        this.open = true;
        this.openGz = true;
        this.openCz = true;
        this.title = "修改巡查任务";
      });
    },
    focus() {
      this.$nextTick(() => {
        document
          .getElementsByClassName("el-button--text")[1]
          .setAttribute("style", "display:none"); // 隐藏此刻按钮
      });
    },
    exportTaskReport(row) {
      let time = parseInt(new Date().getTime() / 1000) + "";
      let fileName = "巡查报告" + time;
      download(
        "/task/list/exportPatrolTaskReport",
        { taskNo: row.id },
        fileName + ".docx"
      );
    },
    /** 废止按钮操作 */
    handleAbolish(row) {
      const id = row.id || this.ids;
      if (id != null) {
        abolishList(id).then((response) => {
          this.$modal.msgSuccess("废止成功");
          this.getList();
        });
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateList(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addList(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除？")
        .then(function () {
          return delList(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          this.$refs.tableFile.clearSelection();
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的巡查任务数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的巡查任务数据项？";
      }
      let ids = this.ids.join();
      this.queryParams.ids = ids;
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportList(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.tableFile.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {});
    },
    //发布
    release() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription", this.form.taskDescription);
      this.fileData.append("publishStatus", "2");
      this.fileData.append("taskStatus", "0");
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("taskName", this.form.taskName);
      if (this.boxList == [] || this.boxList == "") {
        this.$modal.msgWarning("请选择巡检点或故障点");
        return;
      }

      this.boxIds = "";
      this.boxList.forEach((item) => {
        this.boxIds = this.boxIds + (item.eq_id + ",");
      });
      this.fileData.append("devicesList", this.boxIds);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (this.isClick) {
              updateTask(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("发布成功");
                this.open = false;
                this.getList();
              });
            }
          } else {
            if (this.isClick) {
              addTask(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("发布成功");
                this.open = false;
                this.getList();
              });
            }
          }
          this.$refs.tableFile.clearSelection();
        }
      });

      setTimeout(() => {
        this.isClick = true;
      }, 500);
    },
    //废止
    abolish() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription", this.form.taskDescription);
      this.fileData.append("publishStatus", "1");
      this.fileData.append("taskStatus", "");
      //判断是否选择点
      if (this.boxList == [] || this.boxList == "") {
        this.$modal.msgWarning("请选择巡检点或故障点");
        return;
      }
      this.boxIds = "";
      this.boxList.forEach((item) => {
        this.boxIds = this.boxIds + (item.eq_id + ",");
      });
      this.fileData.append("devicesList", this.boxIds);

      if (this.form.id != null) {
        updateTask(this.fileData).then((response) => {
          this.$modal.msgSuccess("废止成功");
          this.open = false;
          this.getList();
        });
      } else {
        addTask(this.fileData).then((response) => {
          this.$modal.msgSuccess("废止成功");
          this.open = false;
          this.getList();
        });
      }
    },
    //暂存
    save() {
      this.fileData = new FormData(); // new formData对象
      this.fileData.append("id", this.form.id);
      this.fileData.append("endPlantime", this.form.endPlantime);
      this.fileData.append("bzId", this.form.bzId);
      this.fileData.append("taskDescription", this.form.taskDescription);
      this.fileData.append("publishStatus", "0");
      this.fileData.append("taskStatus", "0");
      this.fileData.append("tunnelId", this.form.tunnelId);
      this.fileData.append("taskName", this.form.taskName);
      //判断是否选择点
      if (this.boxList == [] || this.boxList == "") {
        this.$modal.msgWarning("请选择巡检点或故障点");
        return;
      }

      this.boxIds = "";
      this.boxList.forEach((item) => {
        this.boxIds = this.boxIds + (item.eq_id + ",");
      });
      this.fileData.append("devicesList", this.boxIds);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (this.isClick) {
              updateTask(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("暂存成功");
                this.open = false;
                this.$refs.tableFile.clearSelection();
                this.getList();
              });
            }
          } else {
            if (this.isClick) {
              addTask(this.fileData).then((response) => {
                this.isClick = false;
                this.$modal.msgSuccess("暂存成功");
                this.open = false;
                this.$refs.tableFile.clearSelection();
                this.getList();
              });
            }
          }
        }
      });

      setTimeout(() => {
        this.isClick = true;
      }, 500);
    },
  },
  watch: {
    isShow1: {
      handler(val) {
        this.$refs.multipleTable1.clearSelection();
        this.dialogSelection = [];
      },
    },
    isShow2: {
      handler(val) {
        this.$refs.multipleTable2.clearSelection();
        this.dialogSelection = [];
      },
    },
  },
};
</script>
<style lang="scss">
.el-table tr {
  background-color: transparent;
}
</style>
<style lang="scss" scoped>
.card {
  font-size: 15px;
  position: relative;
  width: 100%;
  padding: 20px;
  margin-top: 20px;
  border-radius: 10px;
  background-color: #f0f0f0;
  .el-col {
    line-height: 40px;
    display: flex;
    align-items: center;
    > div {
      width: 110px;
    }
  }
  //.card-col {
  //margin-top: 10px;
  //display: flex;
  .active {
    height: 30px;
    padding: 0px 5px;
    color: #ffd69a;
    display: inline;
    margin-left: 10px;
    border: 1px solid #ffd69a;
    line-height: 30px;
    // }
    // div {
    //   width: 33%;
    //   span {
    //     color: black;
    //     margin-left: 10px;
    //   }
    // }
  }
  .card-cols {
    margin-top: 10px;
    display: flex;
    .col-test {
      text-align: right;
      color: #79949c;
    }
    img {
      width: 100px;
      margin-left: 20px;
    }
    span {
      color: #ffffff;
    }
  }
  .card-col1 {
    font-size: 14px;
    .row {
      margin-top: 5px;
      display: flex;
      background-color: white;
      padding: 10px;
      // justify-content: center;
      align-items: center;
      .row-card1 {
        padding: 5px;
        background-color: #f0f0f0;
        margin-left: 5px;
      }
      .row-card2 {
        width: 29%;
      }
    }
  }
}
.col-1 {
  font-size: 14px;
  display: flex;
  justify-content: right;
  align-items: center;
  text-align: right;
  .col-card {
    width: 80px;
    height: 28px;
    text-align: center;
    line-height: 28px;
    font-size: 14px;
    margin-left: 5px;
    color: #fff;
    border-radius: 3px;
  }
  .col-card:first-of-type {
    background: linear-gradient(180deg, #e5a535 0%, #ffbd49 100%);
  }
  .col-card:nth-of-type(2) {
    background: linear-gradient(180deg, #1eace8 0%, #0074d4 100%);
  }
}
.card-cols {
  margin-top: 10px;
  display: flex;
  .col-test {
    width: 20%;
    text-align: right;
    color: #79949c;
  }
  img {
    width: 100px;
    margin-left: 20px;
  }
}
.table-row {
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  div {
    text-align: center;
    margin-left: 15px;
    border-bottom: 1px solid black;
  }
}
.test {
  color: #79949c;
  span {
    color: black;
    margin-left: 5px;
  }
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}
::v-deep .el-dialog {
  margin-top: 7vh !important;
  // .el-dialog__body {
  //   padding: 15px 20px;
  // }
  .el-table--group::after, .el-table--border::after{
    width:0px;
  }
}
img {
  width: 100px;
  margin-top: 20px;
  // margin-left: -20px;
}
::v-deep .el-card {
  margin-bottom: 10px !important;
  background: #0d203c !important;
}
.task {
  //margin-bottom: 30px;
  .el-row {
    display: flex;
    flex-wrap: wrap;
  }
  ::v-deep .el-form-item {
    width: 100%;
    .el-form-item__content {
      width: calc(100% - 110px);
      .el-select,
      .el-date-editor {
        width: 100%;
      }
    }
  }
  .form-one,
  .form-two {
    display: flex;
    justify-content: space-between;
    margin-top: 12px;
    div {
      display: flex;
      align-items: center;
      &:nth-child(3) {
        ::v-deep .el-input {
          width: 100%;
        }
      }
      span {
        display: inline-block;
        width: 30%;
      }
      div {
        width: 70%;
        ::v-deep .el-input {
          width: 90%;
        }
        ::v-deep .el-select {
          width: 100%;
        }
      }
    }
  }
  .describe {
    width: 100%;
    display: flex;
    align-items: center;
    margin-top: 12px;
    span {
      display: inline-block;
      width: 11%;
    }
  }
}
.patrol {
  .button-father {
    display: flex;
    justify-content: end;
    .el-button {
      height: 14px;
      display: flex;
      align-items: center;
    }
  }
  .box-father {
    padding: 0 5px;
    margin-top: 20px;
    margin-bottom: 20px;
    .titleRow {
      float: left;
      font-size: 16px;
      color: #05aafd;
      height: 40px;
      text-align: center;
    }

    .box {
      display: flex;
      margin-top: 5px;
      align-items: center;

      .contentTextRow {
        display: flex;
        width: calc(100% - 100px);
        //height:50px;
      }
      .number {
        width: 30px;
        height: 32px;
        /*border: 1px solid rgba(215, 215, 215, 1);*/
        border-radius: 3px;
        font-weight: 400;
        color: #333;
        /*vertical-align: none;*/
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 10px;
      }
      .text {
        width: calc(100% - 50px);
        height: 32px;
        padding-left: 10px;
        background: inherit;
        /*border: 1px solid rgba(215, 215, 215, 1);*/
        border-radius: 3px;
        font-weight: 400;
        font-size: 14px;
        margin-right: 20px;
        display: flex;
        align-items: center;
        > div:nth-of-type(1) {
          width: 180px;
        }
        > div:nth-of-type(2) {
          width: 200px;
          margin-left: 20px;
        }
        > div:nth-of-type(3) {
          margin-left: 20px;
          width: 200px;
        }
        > div:nth-of-type(4) {
          margin-left: 20px;
          width: 200px;
        }
        > div:nth-of-type(5) {
          margin-left: 20px;
          width: 200px;
        }
      }
      .top,
      .bottom,
      .delete {
        width: 24px;
        height: 24px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: skyblue;
        border: 1px solid skyblue;
        border-radius: 50%;
        cursor: pointer;
      }
      .disabledClass {
        width: 24px;
        height: 24px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #ccc;
        border: 1px solid #ccc;
        border-radius: 50%;
        margin-right: 10px;
        pointer-events: none;
      }
      .top,
      .bottom {
        margin-right: 10px;
      }
    }
  }
  .release-father {
    display: flex;
    justify-content: center;
    .el-button {
      width: 100px;
      height: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
.topTxt {
  margin-left: 7px;
  margin-top: -5px;
  font-size: 16px;
  // background-image: url(../../../assets/cloudControl/cardTitle.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  text-align: center;
  width: 139px;
  height: 30px;
  line-height: 30px;
}

.show {
  ::v-deep .el-dialog__body {
    display: flex;
    .show-left,
    .show-right {
      height: 530px;
      // border: 1px solid black;
      border-radius: 3px;
      .show-title {
        font-weight: 400;
        font-style: normal;
        color: #5e7f89;
        border-radius: 3px;
        line-height: 30px;
        // border-bottom: 1px solid rgb(204, 204, 204);
        padding-left: 10px;
      }
    }
    .show-left {
      width: 25%;
      margin-right: 10px;
    }
    .show-right {
      width: 75%;
      .right-button {
        background-color: rgba(248, 248, 248, 1);
        padding: 5px 10px;
        display: flex;
        justify-content: space-between;
        .el-input__inner {
          height: 26px;
        }
        .el-input__suffix {
          top: 5px;
        }
        .el-input__icon {
          line-height: inherit;
        }
        .el-input__suffix-inner {
          display: inline-block;
        }
        .cancel-determine {
          display: flex;
          .el-button {
            height: 14px;
            display: flex;
            align-items: center;
          }
        }
      }
      .table-father {
        padding: 10px 10px;
        .el-table {
          font-size: 12px;
        }
        // .el-table--enable-row-hover .el-table__body tr:hover > td {
        //   background: #fff !important;
        // }
        // .pagination-container {
        //   position: static !important;
        //   margin-top: 0px !important;
        //   .el-pagination__total,
        //   .el-pagination__jump {
        //     color: #606266 !important;
        //   }
        //   .el-input__inner {
        //     background: #fff !important;
        //     color: #606266 !important;
        //   }
        //   .el-pagination.is-background .btn-prev,
        //   .el-pagination.is-background .btn-next,
        //   .el-pagination.is-background .el-pager li {
        //     background: #fff !important;
        //     color: #606266 !important;
        //   }
        // }
      }
    }
  }
}
::v-deep .xjDialog {
  .el-dialog__body {
    max-height: 70vh;
    overflow: auto;
  }
}
::v-deep .tree {
  height: 445px;
  overflow: auto;
  .el-tree-node:focus > .el-tree-node__content {
    background-color: #89c2f7;
    color: #fff;
  }
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
    background-color: #89c2f7;
    color: #fff;
  }
}
.chaoshi {
  background: rgba(255, 44, 44, 0.28);
  color: #ff2c2c;
  padding: 5px;
  font-size: 12px;
  margin-left: 4px;
}
::v-deep .el-textarea .el-input__count{
  background: transparent !important;
}
.picDialog{
  ::v-deep .el-dialog__body{
    max-height: 78vh;
    overflow: auto;
  }
}
</style>
