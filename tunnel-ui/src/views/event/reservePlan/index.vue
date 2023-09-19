<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-12-08 15:17:28
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-06-03 11:47:24
 * @FilePath: \tunnel-ui\src\views\event\reservePlan\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="4">
        <el-button
          v-hasPermi="['business:plan:add']"
          size="small"
          @click="handleAdd()"
          >新增
        </el-button>
        <el-button size="small" :loading="exportLoading" @click="handleExport"
          >导出
        </el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
      </el-col>
      <el-col :span="6" :offset="14">
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请输入预案名称，回车搜索"
            v-model="queryParams.planName"
            @keyup.enter.native="handleQuery"
            size="small"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="boxShow = !boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px">

      >
        <el-form-item
          label="所属隧道"
          prop="tunnelId"
          v-show="manageStatin == '0'"
        >
          <el-select
            v-model="queryParams.tunnelId"
            placeholder="请选择所属隧道"
            @change="changeSelection"
            clearable
            size="small"
          >
            <el-option
              v-for="(item, index) in eqTunnelData"
              :key="index"
              :label="item.tunnelName"
              :value="item.tunnelId"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="方向" prop="direction">
          <el-select
            v-model="queryParams.direction"
            placeholder="请选择方向"
            clearable
            style="width: 100%"
            @change="$forceUpdate()"
          >
            <el-option
              v-for="(item, index) in directionData"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>

        <!-- <el-form-item label="预案类型" prop="category" >
          <el-select
            v-model="queryParams.category"
            placeholder="请选择预案类型"
            clearable
            size="small"
          >
            <el-option
              v-for="(item, index) in planCategory"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="防控类型" prop="prevControlType">
          <el-select
            v-model="queryParams.prevControlType"
            placeholder="请选择防控类型"
            clearable
            size="small"
            @change="handleChangeControl(1)"
          >
            <el-option
              v-for="item in controlTypeOptions"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId">
          <el-select
            v-model="queryParams.planTypeId"
            clearable
            placeholder="请选择事件类型"
            size="small"
          >
            <el-option
              v-for="(item, index) in planTypeData"
              :key="index"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="事件等级" prop="eventGrade">
          <el-select
            v-model="queryParams.eventGrade"
            clearable
            placeholder="请选择事件等级"
            size="small"
          >
            <el-option
              v-for="(item, index) in eventGradeList"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
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
      ref="planTable"
      v-loading="loading"
      :data="planList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      height="62vh"
      class="allTable"
      :row-key="getRowKey"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        reserve-selection
      />
      <el-table-column type="index" width="70" align="center" label="序号">
      </el-table-column>
      <el-table-column
        align="center"
        label="隧道名称"
        prop="sdTunnels.tunnelName"
        width="130"
      />
      <el-table-column
        align="center"
        label="方向"
        prop="direction"
        width="130"
        :formatter="directionFormat"
      />
      <el-table-column
        align="center"
        label="防控类型"
        prop="prevControlType"
        :formatter="prevControlTypeFormat"
      />
      <el-table-column align="center" label="事件类型" prop="planTypeId">
        <template slot-scope="scope">
          <span>{{ getPlanTypeData(scope.row.planTypeId) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="事件等级"
        prop="eventType.eventGrade"
        :formatter="eventGradeFormat"
      >
      </el-table-column>
      <el-table-column align="center" label="预案名称" prop="planName">
      </el-table-column>
      <el-table-column
        align="center"
        label="预案描述"
        prop="planDescription"
        width="200"
        :show-overflow-tooltip="true"
      >
      </el-table-column>

      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="相关文档"
        width="130"
      >
        <template slot-scope="scope">
          <el-button
            v-show="scope.row.planFileId && scope.row.planFileId != 'null'"
            icon="el-icon-link"
            size="mini"
            style="cursor: pointer; color: #39adff"
            type="text"
            @click="openFileDrawer(scope.row)"
            >点击查看
          </el-button>
          <div v-show="!scope.row.planFileId || scope.row.planFileId == 'null'">
            无
          </div>
        </template>
      </el-table-column>
      <!-- <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="相关策略"
        width="200"
      >
        <template slot-scope="scope"> -->
      <!-- <p v-show="scope.row.strategyNames != null"
                  style="overflow: hidden;text-overflow: ellipsis;cursor: default;"
                  @click="showStrategyContent(scope.row)">
                 {{ scope.row.strategyNames }}
            </p> -->
      <!-- <el-tag
            v-show="scope.row.strategyNames != null"
            :key="tag"
            v-for="tag in scope.row.strategyNames"
            :disable-transitions="false"
            @close="handleClose(tag)"
            style="display: block"
          >
            {{ tag }}
          </el-tag>
          <div v-show="scope.row.strategyNames == ''">无</div>
        </template>
      </el-table-column> -->
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['business:plan:edit']"
            class="tableBlueButtton"
            size="mini"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['business:plan:remove']"
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
          <el-button
            v-hasPermi="['business:plan:add']"
            size="mini"
            class="tableBlueButtton"
            @click="chooseStrategyInfo(scope.row)"
            >配置策略
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
    <el-dialog
      :title="drawerFileTitle"
      :visible.sync="drawerFile"
      width="500px"
      append-to-body
      :before-close="handleFileClose"
      :close-on-click-modal="false"
      class="explain-table"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <!-- <el-drawer
      :before-close="handleFileClose"
      :direction="direction"
      :title="drawerFileTitle"
      :visible.sync="drawerFile"
      class="zwsj"
    > -->
      <el-table v-loading="loading" :data="planFileList">
        <el-table-column align="center" label="序号" width="100px">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="文件名称" prop="fileName" />
        <el-table-column
          align="center"
          class-name="small-padding fixed-width"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button
              v-hasPermi="['business:plan:edit']"
              :loading="loadFileLoading"
              size="mini"
              type="text"
              @click="loadFile(scope.row)"
              >下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- </el-drawer> -->
    </el-dialog>
    <!-- 配置策略选择窗口-->
    <el-dialog
      :title="title"
      :visible.sync="handleStrategyVisible"
      append-to-body
      width="60%"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div style="width: 100%; height: 31.25rem; overflow: auto">
        <el-table
          ref="multipleTable"
          :data="handleStrategyList"
          empty-text="暂无策略"
          row-key="id"
          @selection-change="handleStrategySelectionChange"
          @row-click="multipleTableRowClick"
        >
          <el-table-column align="center" type="selection" width="55" />
          <el-table-column
            align="center"
            label="隧道名称"
            prop="tunnels.tunnelName"
            width="200"
          />
          <el-table-column
            align="center"
            label="策略名称"
            prop="strategyName"
            width="200"
          />
          <el-table-column align="center" label="策略信息" prop="slist">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.slist" :key="index">
                <p style="color: #005cbf">
                  {{ item }}
                </p>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="doStrategy">确定执行</el-button>
        <el-button @click="doStrategyCancel"> 关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 相关策略策略，选择对话框-->
    <el-dialog
      :title="addStrategyTitle"
      :visible.sync="addStrategyVisible"
      append-to-body
      width="60%"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div style="width: 100%; height: 31.25rem; overflow: auto">
        <el-table
          ref="addMultipleTable"
          :data="addStrategyList"
          empty-text="暂无策略"
          row-key="id"
          @selection-change="handleSelectionAddSChange"
          @row-click="addMultipleTableRowClick"
        >
          <el-table-column align="center" type="selection" width="55" />
          <el-table-column
            align="center"
            label="隧道名称"
            prop="tunnels.tunnelName"
            width="200"
          />
          <el-table-column
            align="center"
            label="策略名称"
            prop="strategyName"
            width="200"
          />
          <el-table-column align="center" label="策略信息" prop="slist">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.slist" :key="index">
                <span style="color: #005cbf">
                  {{ item }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="addStrategySubmit">确 认</el-button>
        <el-button @click="addStrategyCancel">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="strategyDialog"
      title="相关策略"
      width="30%"
      :close-on-click-modal="false"
    >
      <div
        v-for="(item, index) of str_arr"
        :key="index"
        style="font-size: 16px; line-height: 40px; padding-left: 20px"
      >
        {{ item }}
      </div>
    </el-dialog>

    <!--  预览-->
    <!--    <work-bench ref="workBench"></work-bench>-->
    <!-- 新增弹窗 -->
    <el-dialog
      :title="title"
      :destroy-on-close="true"
      :visible.sync="dialogFormVisible"
      width="500px"
      :before-close="cancelsubmitUpload"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form
        ref="addform1"
        :model="reservePlanDrawForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="reservePlanDrawForm.tunnelId"
            placeholder="请选择所属隧道"
            style="width: 100%"
            @change="changeSelection"
          >
            <el-option
              v-for="(item, index) in eqTunnelData"
              :key="index"
              :label="item.tunnelName"
              :value="item.tunnelId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="隧道方向" prop="direction">
          <el-select
            v-model="reservePlanDrawForm.direction"
            placeholder="请选择隧道方向"
            style="width: 100%"
          >
            <el-option
              v-for="(item, index) in directionData"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="防控类型" prop="prevControlType">
          <el-select
            v-model="reservePlanDrawForm.prevControlType"
            placeholder="请选择防控类型"
            clearable
            size="small"
            style="width: 100%"
            @change="handleChangeControl(2)"
          >
            <el-option
              v-for="item in controlTypeOptions"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId" key="planTypeId">
          <el-select
            v-model="reservePlanDrawForm.planTypeId"
            placeholder="请选择事件类型"
            style="width: 100%"
            v-if="reservePlanDrawForm.prevControlType"
          >
            <el-option
              v-for="(item, index) in planTypeData"
              :key="index"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
          <el-select
            v-model="reservePlanDrawForm.planTypeId"
            placeholder="请选择事件类型"
            style="width: 100%"
            v-else
          >
            <el-option
              v-for="(item, index) in planTypeDataAll"
              :key="index"
              :label="item.eventType"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="事件等级" prop="eventGrade">
          <el-select
            v-model="reservePlanDrawForm.eventGrade"
            placeholder="请选择事件等级"
            style="width: 100%"
          >
            <el-option
              v-for="(item, index) in eventGradeList"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="管控方向" prop="controlDirection">
          <el-select
            v-model="reservePlanDrawForm.controlDirection"
            placeholder="请选择管控方向"
            style="width: 80%"
          >
            <el-option
              v-for="(item, index) in controlDirectionList"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预案类型" prop="category">
          <el-select
            v-model="reservePlanDrawForm.category"
            placeholder="请选择预案类型"
            style="width: 80%"
          >
            <el-option
              v-for="(item, index) in planCategory"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="预案名称" prop="planName">
          <el-input
            v-model="reservePlanDrawForm.planName"
            placeholder="请输入预案名称"
            style="width: 100%"
            @blur="getCheckPlanName"
          />
        </el-form-item>
        <el-form-item label="预案描述" prop="planDescription">
          <el-input
            v-model="reservePlanDrawForm.planDescription"
            maxlength="250"
            placeholder="请输入预案描述"
            style="width: 100%"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="相关文档" prop="eventLocation">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :file-list="fileList"
            :http-request="uploadFile"
            :limit="5"
            :on-change="handleChange"
            :on-exceed="handleExceed"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            action="http://xxx.xxx.xxx/personality/uploadExcel"
            class="upload-demo"
            multiple
            style="width: 100%"
          >
            <el-button slot="trigger" size="small" type="primary"
              >选取文件</el-button
            >
            <!-- <el-button size="small" style="margin-left: 133px;" type="success" @click="submitUpload">上传到服务器
            </el-button> -->
            <span
              slot="tip"
              class="el-upload__tip"
              style="font-style: italic; color: red; padding-left: 5%"
              >{{ text }}</span
            >
          </el-upload>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitUpload" class="submitButton">保 存</el-button>
        <el-button @click="cancelsubmitUpload" class="closeButton"
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!-- 配置策略 -->
    <el-dialog
      :before-close="closeStrategy"
      title="联控流程"
      :visible.sync="strategyVisible"
      append-to-body
      width="78%"
      class="strategy-dialog"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form1" :inline="true" :model="reserveProcessDrawForm">
        <div
          v-for="(item, number) in planTypeIdList"
          :key="number"
          class="dialongBox"
        >
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item
                prop="processStageName"
                label="阶段名称:"
                label-width="80px"
              >
                <el-input
                  v-model="item.processStageName"
                  placeholder="阶段名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col
              :span="4"
              :offset="12"
              style="display: flex; justify-content: flex-end"
            >
              <el-button
                class="flex-row dialogButton"
                icon="el-icon-delete"
                @click="deleteInfo(number)"
              >
                删除
              </el-button>
              <el-button
                class="flex-row dialogButton"
                type="primary"
                size="mini"
                icon="el-icon-plus"
                @click="addInfo(number)"
              >
                插入
              </el-button>
            </el-col>
          </el-row>
          <el-row class="myTable" style="color: white">
            <el-col :span="1"> 排序 </el-col>
            <el-col :span="4"> 操作处置名称 </el-col>
            <el-col :span="4"> 设备资源类型 </el-col>
            <el-col :span="4"> 检索规则条件 </el-col>
            <el-col :span="5"> 选择设备（可多选） </el-col>
            <el-col :span="4"> 控制指令 </el-col>
            <el-col :span="2"> 操作 </el-col>
          </el-row>
          <el-row class="planBox">
            <el-col
              :span="24"
              class="colflex"
              v-for="(itemed, index) in item.processesList"
              :key="index"
            >
              <el-col
                :span="1"
                style="text-align: center; padding-left: 0px !important"
              >
                <el-form-item prop="index" class="sort">
                  {{ index + 1 }}
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item prop="itemed.processName">
                  <el-input
                    v-model="itemed.processName"
                    placeholder="操作处置名称"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-cascader
                  v-model="itemed.eqTypeId"
                  :options="equipmentTypeData"
                  :props="equipmentTypeProps"
                  :show-all-levels="false"
                  @change="changeEquipmentType(itemed.eqTypeId, number, index)"
                  style="width: 100%"
                ></el-cascader>
              </el-col>
              <el-col :span="4">
                <el-select
                  v-model="itemed.retrievalRule"
                  placeholder="规则条件"
                  clearable
                  @change="ruleChange(number, index, itemed.retrievalRule)"
                  @visible-change="ruleVisible"
                >
                  <el-option
                    v-for="(tag, inx) in itemed.retrievalRuleList"
                    :key="inx"
                    :label="tag.dictLabel"
                    :value="tag.dictValue"
                    :disabled="tag.disabled"
                  />
                </el-select>
              </el-col>
              <el-col :span="5">
                <!-- @change="qbgChange(number, index, itemed.equipments)" -->
                <el-cascader
                  v-model="itemed.equipments"
                  :options="itemed.equipmentData"
                  :disabled="itemed.disabled"
                  :props="devicesProps"
                  :show-all-levels="false"
                  collapse-tags
                  style="width: 100%"
                  node-key="label"
                  @check-change="treeChange"
                  ref="tree"
                ></el-cascader>
              </el-col>

              <el-col
                :span="4"
                v-if="
                  itemed.eqTypeId != 16 &&
                  itemed.eqTypeId != 36 &&
                  itemed.eqTypeId != 22 &&
                  itemed.eqTypeId != 7 &&
                  itemed.eqTypeId != 9
                "
              >
                <el-select v-model="itemed.state" placeholder="设备操作">
                  <el-option
                    v-for="(ite, idx) in itemed.eqStateList"
                    :key="idx"
                    :label="ite.stateName"
                    :value="ite.deviceState"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <!-- 照明设备 -->
              <el-col
                :span="itemed.lightCol"
                v-if="itemed.eqTypeId == 7 || itemed.eqTypeId == 9"
              >
                <el-select
                  v-model="itemed.state"
                  placeholder="设备操作"
                  @change="lightStateChange(number, index, itemed.state)"
                >
                  <el-option
                    v-for="(ite, idx) in itemed.eqStateList"
                    :key="idx"
                    :label="ite.stateName"
                    :value="ite.deviceState"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <el-col
                :span="2"
                v-if="
                  (itemed.eqTypeId == 7 || itemed.eqTypeId == 9) &&
                  itemed.state == '1'
                "
              >
                <el-input-number
                  style="width: 100%"
                  v-model="itemed.brightness"
                  :min="itemed.minLight"
                  :max="100"
                  label="亮度"
                ></el-input-number>
              </el-col>
              <!-- 照明设备end -->
              <!-- 选择情报板模板 -->
              <el-col
                :span="4"
                v-if="itemed.eqTypeId == '16' || itemed.eqTypeId == '36'"
              >
                <!-- <el-cascader
                  placeholder="请选择模板"
                  :props="checkStrictly"
                  v-model="itemed.state"
                  :options="itemed.templatesList"
                  :show-all-levels="false"
                  :key="resetCascader"
                  clearable
                  collapse-tags
                  style="width: 100%"
                ></el-cascader> -->
                <el-input
                  v-model="itemed.content"
                  placeholder="请选择模板"
                  readonly
                  @click.native="openTemDialog(itemed)"
                >
                  <el-button
                    slot="append"
                    icon="el-icon-search"
                    @click.stop="templateClick(number, index, itemed)"
                  ></el-button>
                </el-input>
              </el-col>

              <el-col :span="4" v-if="itemed.eqTypeId == '22'">
                <el-form-item prop="itemed.state">
                  <el-select v-model="itemed.state" placeholder="播放文件">
                    <el-option
                      v-for="(itemPl, figure) in itemed.templatesList"
                      :key="figure"
                      :label="itemPl.name"
                      :value="itemPl.fileName"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="2" class="dialogTableButtonBox">
                <el-button
                  class="delete"
                  @click="removeItem(number, index)"
                ></el-button>
                <el-button
                  class="add"
                  @click="addItem(number, index)"
                ></el-button>
              </el-col>
            </el-col>
          </el-row>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          class="submitButton"
          v-hasPermi="['plan:process:add']"
          @click="submitStrategy"
          >保存</el-button
        >
        <el-button @click="closeStrategy" class="closeButton">取 消</el-button>
      </div>
    </el-dialog>
    <com-board
      class="comClass"
      ref="boardRef"
      @getVmsData="getMsgFormSon"
    ></com-board>
    <el-dialog
      :title="templateData.processName"
      :visible.sync="dialogVisibleTem"
      width="45%"
      :before-close="handleClose"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <div style="display: flex; justify-content: center; align-items: center">
        <!-- 'letter-spacing':templateData['font_spacing'] + 'px', -->
        <div
          :style="{
            width: templateData['width'] + 'px',
            height: templateData['height'] + 'px',
            color: templateData['font_color'],
            'font-size': templateData['font_size'] + 'px',
            'font-family': templateData['font_type'],
            'background-color': '#000',
            position: 'relative',
          }"
        >
          <span
            :style="{
              position: 'absolute',
              top: templateData['top'] + 'px',
              left: templateData['left'] + 'px',
            }"
            style="line-height: 1"
            v-html="templateData['content']"
          >
          </span>
        </div>
      </div>
      <!-- <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleTem = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span> -->
    </el-dialog>
  </div>
</template>

<script>
import comBoard from "@/views/event/reservePlan/board";
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
  getAudioFileList,
} from "@/api/equipment/eqTypeState/api";
import { listDevices } from "@/api/equipment/eqlist/api";
import { selectVmsContent } from "@/api/information/api.js";
import {
  listPlan,
  getPlan,
  delPlan,
  addPlan,
  updatePlan,
  addPlanFile,
  loadPlanFile,
  updatePlanFile,
  listStrategyByPlanId,
  partitionTunnel,
  tunnelNames,
  getPlanType,
  getTreeDeviceList,
  exportPlan,
  getVmsDataList,
  checkPlanName,
} from "@/api/event/reservePlan";
import { listEventType } from "@/api/event/eventType";
import {
  listReservePlanFile,
  getReservePlanProcess,
} from "@/api/event/reservePlanFile";
import { download } from "@/utils/request";
import {
  listStrategy,
  getStrategy,
  handleStrategy,
  getRl,
  getCategoryTree,
} from "@/api/event/strategy";
import { listType, getTypeAndStrategy } from "@/api/equipment/type/api.js";
import {
  getJlyTunnel,
  getTunnels,
  listTunnels,
} from "@/api/equipment/tunnel/api.js";
import { fastLerp } from "zrender/lib/tool/color";
import {
  addProcess,
  getListByRId,
  previewDisplay,
} from "@/api/event/reserveProcess";
import { exportFlow } from "@/api/event/planFlow";
import { Loading } from "element-ui";

export default {
  name: "Plan",
  components: {
    comBoard,
  },
  data() {
    return {
      controlTypeOptions: [], //防控类型
      dataLoading: false,
      resetCascader: 0,
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      devicesProps: {
        multiple: true,
        checkStrictly: false,
        emitPath: false,
      },
      eventGradeList: "", //事件等级
      boxShow: false,
      fileList: null,
      checkStrictly: {
        multiple: false,
        checkStrictly: true,
        emitPath: false,
      },
      equipmentTypeData: [],
      directionData: [], //方向
      controlDirectionList: [], //管控方向
      manageStatin: this.$cache.local.get("manageStation"),
      paramsData: {
        tunnelId: "",
      },
      deviceList: [], //需要操作的设备以及状态数据
      previewList: [], //预览数据
      reserveId: "",
      //新增弹窗
      dialogFormVisible: false,
      visibleAdd: false,
      //配置策略
      strategyVisible: false,
      //策略数组
      planTypeIdList: [
        {
          processStageName: "",
          processesList: [
            {
              processName: "",
              eqTypeId: "",
              equipments: [],
              state: "",
              qbbState: "",
              retrievalRule: "", //规则条件
              equipmentTypeData: [],
              equipmentData: [],
              eqStateList: [],
              templatesList: [],
              brightness: 100,
              minLight: 1,
            },
          ],
        },
      ],
      title: "",
      addTitle: "",
      options: [],
      props: {
        label: "name",
        value: "id",
        children: "children",
        multiple: true,
        emitPath: false,
      },
      active: 0,
      //工作台
      workbenchOpen: false,
      str_arr: [],
      strategyDialog: false, //相关策略弹窗
      text: "*注*：上传文件不可超过1M且不可超过五个文件",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中关系ID数组
      rlIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预案信息表格数据
      planList: [],
      // 导出遮罩层
      exportLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planTypeId: null,
        planName: null,
        planFileId: null,
        strategyId: null,
        tunnelId: null,
        category: null,
        direction: null,
        eventGrade: null,
      },
      // 表单校验
      rules: {
        planName: [
          { required: true, message: "请输入预案名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" },
        ],
        planTypeId: {
          required: true,
          message: "请选择事件类型",
          trigger: "change",
        },
        eventGrade: {
          required: true,
          message: "请选择事件等级",
          trigger: "change",
        },
        tunnelId: {
          required: true,
          message: "请选择隧道类型",
          trigger: "change",
        },
        direction: {
          required: true,
          message: "请选择方向",
          trigger: "change",
        },
        prevControlType: {
          required: true,
          message: "请选择防控类型",
          trigger: "change",
        },
        // controlDirection: {
        //   required: true,
        //   message: "请选择管控方向",
        //   trigger: "change",
        // },
        sId: { required: true, message: "请选择分区隧道", trigger: "change" },
        planDescription: {
          required: true,
          message: "请输入预案描述",
          trigger: "blur",
        },
        // category: {
        //   required: true,
        //   trigger: "change",
        //   message: "请选择预案类型",
        // },
      },
      //draw开关
      drawer: false,
      // 侧边层标题
      drawerTitle: "",
      //搜索-事件类型
      planTypeData: [],
      // 表格内回显事件类型
      planTypeDataAll: [],
      //from表单参数
      reservePlanDrawForm: {
        planTypeId: null, //事件类型
        eventGrade: null, //事件等级
        planName: null, //预案名称
        category: null, //预案类别
        planDescription: null, //预案描述
        strategyId: null, //多个策略ID
        strategyNames: null, //多个策略的名称，以：分割
        planFileId: null,
        tunnelId: null, //隧道
        controlDirection: null,
        direction: null,
      },
      reserveProcessDrawForm: {
        reserveId: null, //预案id
        deviceTypeId: null, //设备类型id
        strategyId: null, //策略id
        processName: null, //流程节点名称
        processSort: null, //流程节点顺序
        strategyIds: null, //多个策略id
      },
      strategyId: [{}],
      // 遮罩层
      dloading: false,
      //drawer方向
      direction: "rtl",
      //策略数据
      strategyData: [], //策略下来数据信息
      fileData: "", // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      fileDrawForm: {},
      //相关文件drawer标题
      drawerFileTitle: "",
      //相关文件draw开关
      drawerFile: false,
      loadFileLoading: false,
      // 预案文件信息表格数据
      planFileList: [],
      // 遮罩层
      fileLoading: true,
      //需要移除的文件ids
      removeIds: [],
      //添加or编辑标志；add/edit
      planChangeSink: null,
      // 选择--需要执行的控制策略
      handleStrategyVisible: false,
      handleStrategyList: [],
      // title:'',
      handleIds: [],
      //选择---新增控制策略
      addStrategyVisible: false,
      addStrategyList: [],
      addStrategyTitle: "",
      adsIds: [],
      multipleSelectionIds: [], //存放选择的id数组
      adsStrategyNames: [],
      // 非单个禁用
      adsSingle: true,
      // 非多个禁用
      adsMultiple: true,
      tunnelId: "WLJD-JiNan-YanJiuYuan-FHS",
      selectedIconList: [],
      lightSwitch: 0,
      eqTunnelData: [], //隧道下拉
      eqTunnelDataList: [], //分区隧道下拉
      timer: null, //步骤条定时器
      planCategory: [], //预案类别下拉
      strategyRlData: [], //返回策略设备和状态
      currentClickData: {},
      //设备类型查询参数
      queryEqTypeParams: {
        isControl: 1,
      },
      retrievalRuleList: [],
      maskOptions: {
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: ".strategy-dialog",
      },
      templateData: {},
      dialogVisibleTem: false,
    };
  },
  created() {
    this.getList();
    this.getPlanType(); //事件类型下拉
    // this.getStrategyInfo();//策略下拉
    // this.getTunnelData(this.tunnelId);
    this.lightSwitchFunc();
    this.ceshiTime();
    if (this.$cache.local.get("manageStation") == "1") {
      this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
    }
    listTunnels().then((res) => {
      this.eqTunnelData = res.rows;
    });
    this.getDicts("sd_emergency_plan_type").then((response) => {
      this.planCategory = response.data;
    });
    //规则条件
    // this.getRules();
    // 管控方向
    this.getDicts("sd_control_direction").then((response) => {
      this.controlDirectionList = response.data;
    });
    // 方向
    this.getDicts("sd_direction").then((response) => {
      this.directionData = response.data;
    });
    // 事件等级
    this.getDicts("sd_event_grade").then((response) => {
      this.eventGradeList = response.data;
    });
    //防控类型
    this.getDicts("prev_control_type").then((response) => {
      if (response.data.length > 0) {
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].dictLabel == "设备故障") {
            response.data.splice(i, 1);
          }
        }
      }
      this.controlTypeOptions = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    // 判断预案名称是否重复
    getCheckPlanName() {
      if (
        this.reservePlanDrawForm.planName &&
        this.reservePlanDrawForm.tunnelId
      ) {
        const param = {
          planName: this.reservePlanDrawForm.planName,
          tunnelId: this.reservePlanDrawForm.tunnelId,
        };
        checkPlanName(param).then((res) => {
          console.log(res, "res");
        }).catch(()=>{
          // this.reservePlanDrawForm.planName = ''
        });
      }
    },
    handleChangeControl(type, num) {
      console.log(type, num, "type,num");
      this.planTypeData = [];
      this.$forceUpdate();
      let prevControlType = "";
      if (type == 1) {
        prevControlType = this.queryParams.prevControlType;
        this.queryParams.planTypeId = "";
      } else if (type == 2) {
        prevControlType = this.reservePlanDrawForm.prevControlType;
        this.reservePlanDrawForm.planTypeId = null;
        this.$nextTick(() => {
            this.$refs["addform1"].clearValidate('planTypeId')
        })
      } else if (type == 3) {
        prevControlType = num;
      }
      //查询事件类型
      let params = {
        isUsable: "1",
        prevControlType: prevControlType,
      };
      listEventType(params).then((response) => {
        this.planTypeData = [...response.rows];
      });
    },
    treeChange(data, checked, node) {
      console.log(data, checked, node);
      this.$refs.tree.setCheckedKeys([data]);
    },
    //查看情报板信息
    openTemDialog(item) {
      console.log(item);
      if (item.state == "") {
        return this.$modal.msgWarning("请选择模板");
      }
      let params = { id: item.id, state: item.state };
      console.log(item);
      selectVmsContent(params).then((res) => {
        this.templateData = Object.assign(res.data, item);
        console.log(this.templateData);
        let zxc = this.templateData["screen_size"].split("*");
        this.templateData["width"] = zxc[0];
        this.templateData["height"] = zxc[1];
        let align = this.templateData["coordinate"];
        this.templateData["left"] = align.substr(0, 3);
        this.templateData["top"] = align.substr(3, 6);
        let content = this.templateData["content"];
        if (content.indexOf("/n") == "-1") {
          this.templateData["content"] = content
            .replace(/\n|\r\n/g, "<br>")
            .replace(/ /g, " &nbsp");
        }
        this.dialogVisibleTem = true;
      });
    },
    getMsgFormSon(data) {
      console.log(data);
      this.$set(
        this.planTypeIdList[data.number].processesList[data.index],
        "content",
        data.content
      );
      this.$set(
        this.planTypeIdList[data.number].processesList[data.index],
        "state",
        data.id
      );
    },
    // 情报板选择模板点击事件
    templateClick(number, index, item) {
      this.$refs.boardRef.init(number, index, item.eqTypeId);
    },
    lightStateChange(number, index, state) {
      console.log(state, "当前状态");
      if (state == "1") {
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "lightCol",
          2
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "brightness",
          100
        );
      } else {
        // 关闭状态下亮度值赋0
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "brightness",
          0
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "lightCol",
          4
        );
      }
      this.$forceUpdate();
    },
    // 规则下拉显示或者隐藏触发
    ruleVisible(e) {
      console.log(e);
    },
    openFullScreen2() {
      const Loading = this.$loading(this.maskOptions);
      setTimeout(() => {
        Loading.close();
      }, 1000);
    },
    getRules() {
      this.getDicts("sd_device_retrieval_rule").then((response) => {
        this.retrievalRuleList = response.data;
        for (let item of this.planTypeIdList) {
          for (let itemed of item.processesList) {
            itemed.retrievalRuleList = response.data;
          }
        }
      });
    },
    // 保存选中的数据id,row-key就是要指定一个key标识这一行的数据
    getRowKey(row) {
      return row.id;
    },
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.boxShow == true) {
          self.boxShow = false;
        }
      }
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
    },
    deleteInfo(index) {
      if (this.planTypeIdList.length == 1) {
        return this.$modal.msgWarning("至少保留一行");
      }
      this.planTypeIdList.splice(index, 1);
    },

    /** 导出按钮操作 */
    handleExport() {
      let confirmInfo = "是否确认导出所有的应急预案数据项？";
      if (this.ids.length > 0) {
        confirmInfo = "是否确认导出所选的应急预案数据项？";
      }
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm(confirmInfo)
        .then(() => {
          this.exportLoading = true;
          return exportPlan(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
          this.$refs.planTable.clearSelection();
          this.queryParams.ids = "";
        })
        .catch(() => {
          this.queryParams.ids = "";
        });
    },
    addInfo(index) {
      let data = {
        processStageName: "",
        processesList: [
          {
            processName: "",
            eqTypeId: "",
            equipments: [],
            state: "",
            qbbState: "",
            retrievalRule: null, //规则条件
            retrievalRuleList: [],
            equipmentTypeData: [],
            equipmentData: [],
            eqStateList: [],
            disabled: false,
            templatesList: [],
            brightness: 100,
            minLight: "",
          },
        ],
      };
      this.planTypeIdList.splice(index + 1, 0, data);
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      for (let i = 0; i < this.planTypeIdList.length; i++) {
        getCategoryTree().then((data) => {
          this.$set(this.planTypeIdList[i], "equipmentTypeData", data.data);
          this.equipmentTypeData = data.data;
        });
      }
    },
    getPlanTypeData(id) {
      for (var item of this.planTypeDataAll) {
        if (item.id == id) {
          return item.eventType;
        }
      }
    },
    controlDirectionFormat(row, column) {
      return this.selectDictLabel(
        this.controlDirectionList,
        row.controlDirection
      );
    },
    prevControlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.prevControlType);
    },
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionData, row.direction);
    },
    // categoryFormat(row, column) {
    //   return this.selectDictLabel(this.planCategory, row.category);
    // },
    eventGradeFormat(row, column) {
      return this.selectDictLabel(this.eventGradeList, row.eventGrade);
    },
    tunnelIdFormat(row, column) {
      return this.selectDictLabel(this.eqTunnelData, row.tunnelId);
    },
    workbenchOpenEvent() {
      // this.getTunnelData(this.tunnelId);
      this.workbenchOpen = false;
    },
    closeDialogVisible() {
      // this.getTunnelData(this.tunnelId);
      this.dialogVisible = false;
    },
    // 规则条件修改事件
    ruleChange(number, index, value) {
      // 如果是否指定
      // 如果是指定   根据类型查设备
      // 如果不是指定  禁用选择设备 同时判断 设备类型是否为  16 和 36 如果是  则 请求情表板模板
      if (value != 1) {
        //赋空
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "equipments",
          ""
        );
        // 禁用选择设备
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "disabled",
          true
        );
        let eqType = this.planTypeIdList[number].processesList[index].eqTypeId;
        // 同时如果为情报板则查询情报板模板
        if (eqType == "16" || eqType == "36") {
          let params = {
            eqTunnelId: this.currentClickData.tunnelId,
            eqType: this.planTypeIdList[number].processesList[index].eqTypeId,
          };
          getVmsDataList(params).then((res) => {
            this.$set(
              this.planTypeIdList[number].processesList[index],
              "templatesList",
              res.data
            );
          });
        }
      } else {
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "disabled",
          false
        );
      }
      this.$forceUpdate();
    },
    addStrategy(index) {
      let obj = {
        processName: "",
        processSort: "",
        handleStrategyList: "",
      };
      this.planTypeIdList.splice(index + 1, 0, obj);
    },
    removeItem(number, index) {
      if (this.planTypeIdList[number].processesList.length == 1) {
        return this.$modal.msgWarning("至少保留一行");
      }
      this.planTypeIdList[number].processesList.splice(index, 1);
    },
    // 添加执行操作
    addItem(number, index) {
      // this.getRules();
      let data = {
        processStageName: "",
        processesList: [
          {
            processName: "",
            eqTypeId: "",
            equipments: [],
            state: "",
            retrievalRule: null, //规则条件
            equipmentTypeData: [],
            equipmentData: [],
            retrievalRuleList: [],
            eqStateList: [],
            disabled: false,
            templatesList: [],
            brightness: 100,
            minLight: 1,
          },
        ],
      };
      this.planTypeIdList[number].processesList.splice(index + 1, 0, data);
      this.getEquipmentType();
    },
    //获得预案类别
    // selectPlanType() {
    //   getPlanType().then((res) => {
    //     this.planCategory = res.data;
    //   });
    // },
    //点击了取消
    cancelsubmitUpload() {
      this.dialogFormVisible = false;
      this.fileList = [];
      this.$nextTick(() => {
        this.$refs["addform1"].resetFields();
      });
      this.$refs.planTable.clearSelection();
      //this.handleQuery();
      this.resetReservePlanDrawForm();
    },
    //form表单置空
    resetReservePlanDrawForm() {
      (this.reservePlanDrawForm = {
        planTypeId: null, //事件类型
        planName: null, //预案名称
        category: null, //预案类别
        planDescription: null, //预案描述
        strategyId: null, //多个策略ID
        strategyNames: null, //多个策略的名称，以：分割
        planFileId: null,
        tunnelId: null, //隧道
        sId: null, //分区隧道
      }),
        (this.fileList = []);
      this.removeIds = [];
      this.planChangeSink = null;
      this.multipleSelectionIds = [];
      this.eqTunnelDataList = [];
      // this.planCategory = [];
    },
    ceshiTime() {
      this.timer = setInterval(() => {
        if (this.active++ > 2) this.active = 0;
      }, 1000);
    },
    showStrategyContent(row) {
      this.strategyDialog = true;
      this.str_arr = row.strategyNames.split("；");
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },
    // 改变设备类型
    changeEquipmentType(eqTypeId, number, index) {
      console.log(
        "设备类型:" + eqTypeId,
        "第" + number + "阶段",
        "第" + index + "个",
        "设备类型改变"
      );
      if (eqTypeId) {
        let retrievalRuleList = JSON.parse(
          JSON.stringify(this.retrievalRuleList)
        );
        // 加强照明开启百分比选择
        if (eqTypeId == "7" || eqTypeId == "9") {
          let light = this.planTypeIdList[number].processesList[index];
          light.lightCol = 4;
          light.state = "";
          light.minLight = 1;
          //开启加强照明百分比
          this.$set(light, "lightShow", true);
          // 改变布局占比
          if (light.state == "1") {
            light.lightCol = 2;
            light.brightness = 100;
          } else {
            light.lightCol = 4;
          }
        }
        // 疏散标志只能选择最近1个，同时无法选择具体设备
        if (eqTypeId == 30) {
          retrievalRuleList.map((item) => {
            if (item.dictValue != "6") {
              item.disabled = true;
            }
          });
          this.$set(
            this.planTypeIdList[number].processesList[index],
            "retrievalRuleList",
            retrievalRuleList
          );
        }
        // 基本照明
        if (eqTypeId == "9") {
          let light = this.planTypeIdList[number].processesList[index];
          light.brightness = 30;
          light.minLight = 30;
        }
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "retrievalRuleList",
          retrievalRuleList
        );
        // 更改设备类型后状态和设备重置
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "equipments",
          ""
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "eqStateList",
          ""
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "retrievalRule",
          ""
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "state",
          ""
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "content",
          ""
        );
        let params = {
          eqType: eqTypeId, //设备类型
          eqTunnelId: this.currentClickData.tunnelId, //隧道
        };
        getTreeDeviceList(params).then((res) => {
          if (res.data.length == 0) {
            this.$set(
              this.planTypeIdList[number].processesList[index],
              "equipmentData",
              ""
            );
            return this.$modal.msgWarning("暂无设备");
          }
          this.$set(
            this.planTypeIdList[number].processesList[index],
            "equipments",
            ""
          );
          this.$set(
            this.planTypeIdList[number].processesList[index],
            "equipmentData",
            res.data
          );
        });
        if (eqTypeId != 22) {
          this.listEqTypeStateIsControl(eqTypeId, number, index);
        } else {
          //广播
          this.getAudioFileListData("", number, index);
        }
      }
    },
    getAudioFileListData(value, number, index) {
      let params = {
        tunnelId: this.currentClickData.tunnelId,
        direction: this.currentClickData.direction,
      };
      getAudioFileList(params).then((res) => {
        this.fileList = res.data;
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "templatesList",
          res.data
        );
      });
    },
    // 查询设备可控状态
    listEqTypeStateIsControl(eqTypeId, number, index) {
      let params = {
        stateTypeId: eqTypeId,
        direction: this.currentClickData.direction,
        isControl: 1,
      };
      listEqTypeStateIsControl(params).then((response) => {
        // this.planTypeIdList[index].eqStateList = response.rows;
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "eqStateList",
          response.rows
        );
      });
    },
    // qbgChange(number, index, value) {
    //   let data = value;
    //   if (
    //     this.planTypeIdList[number].processesList[index].eqTypeId == 16 ||
    //     this.planTypeIdList[number].processesList[index].eqTypeId == 36
    //   ) {
    //     getVMSTemplatesByDevIdAndCategory(data).then((res) => {
    //       // this.templatesList = res.data;
    //       this.$set(
    //         this.planTypeIdList[number].processesList[index],
    //         "templatesList",
    //         res.data
    //       );
    //     });
    //   }
    // },
    //关闭策略弹窗
    closeStrategy() {
      // this.getTunnelData(this.tunnelId);
      this.strategyVisible = false;
      this.planTypeIdList = [];
      // this.handleQuery();
      this.$refs.boardRef.handleClosee();

      this.$refs.planTable.clearSelection();
      //this.handleQuery();
      this.resetReservePlanDrawForm();
    },
    everyForeach(value) {
      return value != "";
    },
    // 编辑策略保存方法
    submitStrategy() {
      for (let i = 0; i < this.planTypeIdList.length; i++) {
        if (this.planTypeIdList[i].processStageName == "") {
          return this.$modal.msgWarning("请填写阶段名称");
        }

        let item = this.planTypeIdList[i].processesList;
        //加强照明开启默认1不做判断
        let result = item.every((items) => {
          if (items.retrievalRule == 1) {
            return (
              items.processName &&
              items.state &&
              items.eqTypeId &&
              items.equipments.length >= 1 &&
              items.equipments[0] != ""
            );
          } else {
            //非指定由后端判断具体设备
            return (
              items.processName &&
              items.state &&
              items.retrievalRule &&
              items.eqTypeId &&
              items.state
            );
          }
        });
        if (!result) {
          return this.$modal.msgError("请填写完整");
        }
      }

      this.planTypeIdList.forEach((item, index) => {
        item.processSort = index;
      });
      let data = {
        reserveId: this.reserveId,
        sdReserveProcesses: this.planTypeIdList,
      };

      addProcess(data).then((res) => {
        if (res.code === 200) {
          this.strategyVisible = false;
          this.planTypeIdList = [];
          this.$modal.msgSuccess(res.msg);
          this.$refs.planTable.clearSelection();
          this.getList();
        }
      });
    },
    // 配置策略
    async chooseStrategyInfo(row) {
      this.getRules();
      this.getEquipmentType();
      this.reserveId = row.id;
      this.currentClickData = row;
      await getTypeAndStrategy({ isControl: 1 }).then((res) => {
        this.options = res.data;
      });
      getReservePlanProcess(this.reserveId).then((res) => {
        this.planTypeIdList = res.data;
        // this.getRules();
        this.openFullScreen2();
        if (this.planTypeIdList.length == 0) {
          //this.getRules();
          this.planTypeIdList = [
            {
              stageName: "",
              processesList: [
                {
                  state: "",
                  eqTypeId: "",
                  equipmentTypeData: [],
                  equipmentData: [],
                  retrievalRule: "",
                  retrievalRuleList: [],
                  equipments: [],
                  disabled: false,
                  templatesList: [],
                  brightness: "100",
                  minLight: "",
                },
              ],
            },
          ];
        } else {
          // this.getRules();
          let data = res.data;
          for (let i = 0; i < data.length; i++) {
            let arr = data[i];
            //阶段名称
            this.$set(
              this.planTypeIdList[i],
              "processStageName",
              data[i].processStageName
            );
            for (let j = 0; j < arr.processesList.length; j++) {
              let brr = arr.processesList[j];
              brr.retrievalRule = brr.retrievalRule; //规则条件
              // 选择指定设备
              if (brr.retrievalRule != 1) {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "disabled",
                  true
                );
              }
              brr.eqTypeId = String(brr.deviceTypeId); //设备类型
              brr.equipments = brr.equipmentList; //设备列表
              // brr.brightness =
              // 渲染设备列表
              let params = {
                eqType: brr.eqTypeId, //设备类型
                eqTunnelId: this.currentClickData.tunnelId, //隧道
              };
              getTreeDeviceList(params).then((res) => {
                console.log(res.data);
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "equipmentData",
                  res.data
                );
              });
              // 渲染设备可控状态
              this.listEqTypeStateIsControl(brr.deviceTypeId, i, j);
              // 疏散标志
              //加强和基本设置不同最小值
              if (brr.eqTypeId == 9) {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "minLight",
                  30
                );
              } else if (brr.eqTypeId == 7) {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "minLight",
                  1
                );
              }
              // 设备类型为加强照明且状态为开启
              if (
                (brr.eqTypeId == 7 || brr.eqTypeId == 9) &&
                brr.state == "1"
              ) {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "lightCol",
                  2
                );
              } else {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "lightCol",
                  4
                );
              }
              // 请求情报板数据
              if (brr.eqTypeId == 16 || brr.eqTypeId == 36) {
                // 指定设备
                if (brr.retrievalRule == 1) {
                  brr.state = +brr.state;
                  // this.qbgChange(i, j, brr.equipments);
                } else {
                  //不指定
                  brr.state = +brr.state;
                  let params = {
                    eqTunnelId: this.currentClickData.tunnelId,
                    eqType: brr.eqTypeId,
                  };
                  getVmsDataList(params).then((res) => {
                    this.$set(
                      this.planTypeIdList[i].processesList[j],
                      "templatesList",
                      res.data
                    );
                  });
                }
              }
              //请求广播音频列表数据
              if (brr.eqTypeId == 22) {
                brr.state = brr.state;
                this.getAudioFileListData(brr.equipments, i, j);
              }
              this.getDicts("sd_device_retrieval_rule").then((response) => {
                console.log(response.data);
                var retrievalRuleList = response.data;
                if (brr.eqTypeId == 30) {
                  console.log(retrievalRuleList);
                  for (let items of retrievalRuleList) {
                    console.log(items);
                    if (items.dictValue != "6") {
                      items.disabled = true;
                    }
                  }
                  this.$set(
                    this.planTypeIdList[i].processesList[j],
                    "retrievalRuleList",
                    retrievalRuleList
                  );
                } else {
                  for (let item of this.planTypeIdList) {
                    for (let itemed of item.processesList) {
                      itemed.retrievalRuleList = retrievalRuleList;
                    }
                  }
                }
              });
            }
          }
        }
      });
      this.handleStrategyList = [];
      this.reserveProcessDrawForm.reserveId = row.id;
      this.strategyVisible = true;
    },
    //执行策略id勾选事件
    handleStrategySelectionChange(selection) {
      this.handleIds = selection.map((item) => item.id);
    },
    // 关闭 相关策略对话框
    doStrategyCancel() {
      this.handleStrategyVisible = false;
    },
    // 执行 选择的策略
    doStrategy() {
      if (this.handleIds.length > 0) {
        this.$modal.msgSuccess("执行策略中.......");
        for (let i = 0; i < this.handleIds.length; i++) {
          handleStrategy(this.handleIds[i])
            .then((response) => {
              this.$modal.msgSuccess("执行成功");
              this.doStrategyCancel();
            })
            .catch(() => {
              this.$modal.msgError("执行失败");
            });
        }
      } else {
        this.$modal.msgError("请先选择需要执行的策略！");
      }
    },
    //=========================执行相关预案结束=====================

    //=========================选择相关预案开始=====================
    /* 跳至选择相关策略页面*/
    openAddStragegyDialog() {
      this.addStrategyVisible = true;
      this.addStrategyTitle = "相关策略";
      listStrategy({ strategyType: 0 }).then((response) => {
        this.addStrategyList = response.rows;
        this.$refs.addMultipleTable.clearSelection();
        if (
          this.multipleSelectionIds != null &&
          this.multipleSelectionIds.length > 0
        ) {
          let array = [];
          for (let i = 0; i < this.addStrategyList.length; i++) {
            for (let j = 0; j < this.multipleSelectionIds.length; j++) {
              if (this.multipleSelectionIds[j] == this.addStrategyList[i].id) {
                array.push(this.addStrategyList[i]);
              }
            }
          }
          //执行回显勾选
          this.$nextTick(() => {
            for (let k = 0; k < array.length; k++) {
              this.$refs.addMultipleTable.toggleRowSelection(array[k], true); //这是默认选中上的
            }
          });
        }
      });
    },
    // 关闭控制策略对话框
    addStrategyCancel() {
      this.addStrategyVisible = false;
    },
    // 选择,添加控制策略ids
    handleSelectionAddSChange(selection) {
      this.adsIds = selection.map((item) => item.id);
      this.adsStrategyNames = selection.map((item) => item.strategyName);
      this.adsSingle = selection.length !== 1;
      this.adsMultiple = !selection.length;
    },
    // 提交策略，暂存关闭对话框
    addStrategySubmit() {
      this.multipleSelectionIds = this.adsIds; //选中的策略id
      this.reservePlanDrawForm.strategyNames = "";
      this.reservePlanDrawForm.strategyId = "";
      for (let i = 0; i < this.adsStrategyNames.length; i++) {
        this.reservePlanDrawForm.strategyNames =
          this.reservePlanDrawForm.strategyNames +
          this.adsStrategyNames[i] +
          "；";
        this.reservePlanDrawForm.strategyId =
          this.reservePlanDrawForm.strategyId + this.adsIds[i] + "；";
      }
      this.addStrategyVisible = false;
    },
    //===========================选择相关预案结束=========================
    //点击查看文件
    openFileDrawer(row) {
      this.fileLoading = true;
      listReservePlanFile({ planFileId: row.planFileId }).then((response) => {
        this.drawerFileTitle = "相关文档";
        this.planFileList = response.rows;
        this.fileLoading = false;
        this.drawerFile = true;
      });
    },
    //下载文件
    async loadFile(row) {
      this.loadFileLoading = true;
      await loadPlanFile(row.id, row.fileName);
      this.loadFileLoading = false;
    },
    downfiles(datas, filename) {
      var data = new Blob([datas]);
      var downloadUrl = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.href = downloadUrl;
      // 这里的filename 带有后缀，能决定文件的类型
      anchor.downolad = filename;
      anchor.click();
      window.URL.revokeObjectURL(data);
    },
    //点击文件触发
    handlePreview(file, fileList) {
      ///debugger
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append("file", file.file); // append增加数据
    },

    // 上传到服务器
    async submitUpload() {
      this.$refs["addform1"].validate((valid) => {
        if (valid) {
          if (this.loading) return;
          this.dloading = true;
          let currentFileList = [];
          for (var i = 0; i < this.fileList.length; i++) {
            if (!this.fileList[i].hasOwnProperty("fId")) {
              if (!this.fileList[i].size) this.fileList[i].size = 0; // 如果没有size，则给个0
              currentFileList.push(this.fileList[i]);
            }
          }
          const isLt100M = currentFileList.every(
            (file) => file.size / 1024 / 1024 < 1
          );
          if (!isLt100M) {
            this.$message.error("请检查，上传文件大小不能超过1MB!");
          } else {
            this.fileData = new FormData(); // new formData对象
            this.$refs.upload.submit(); // 提交调用uploadFile函数
            this.fileData.append("planName", this.reservePlanDrawForm.planName); // 预案名称
            this.fileData.append(
              "planTypeId",
              this.reservePlanDrawForm.planTypeId
            ); // 事件类型
            // this.fileData.append("category", this.reservePlanDrawForm.category); // 预案类型
            this.fileData.append(
              "planDescription",
              this.reservePlanDrawForm.planDescription == null
                ? "#^#"
                : this.reservePlanDrawForm.planDescription
            ); // 预案描述
            this.fileData.append("tunnelId", this.reservePlanDrawForm.tunnelId);
            this.fileData.append(
              "direction",
              this.reservePlanDrawForm.direction
            );
            // this.fileData.append(
            //   "controlDirection",
            //   this.reservePlanDrawForm.controlDirection
            // );
            this.fileData.append(
              "subareaId",
              Number(this.reservePlanDrawForm.sId)
            );
            this.fileData.append(
              "eventGrade",
              Number(this.reservePlanDrawForm.eventGrade)
            );
            this.fileData.append(
              "prevControlType",
              Number(this.reservePlanDrawForm.prevControlType)
            );
            if (this.planChangeSink == "add") {
              let filess = Object.assign({}, this.fileData);
              addPlanFile(this.fileData)
                .then((response) => {
                  if (response.code === 200) {
                    this.$modal.msgSuccess("保存成功");
                    // this.drawer = false;//关闭drawer窗体
                    this.dialogFormVisible = false;
                    this.resetReservePlanDrawForm(); //重置表单
                    //this.open = false;
                    this.getList();
                  } else {
                    console.log(filess, "filess");
                    this.fileData = filess;
                    this.$modal.msgError("保存失败");
                  }
                })
                .catch(() => {
                  console.log(this.fileData, "this.fileData");
                  console.log("保存失败");
                });
            } else if (this.planChangeSink == "edit") {
              this.fileData.append("id", this.reservePlanDrawForm.id);
              this.fileData.append("removeIds", this.removeIds);
              this.fileData.append(
                "planFileId",
                this.reservePlanDrawForm.planFileId
              );

              updatePlanFile(this.fileData).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.$refs.planTable.clearSelection();
                  this.dialogFormVisible = false;
                  this.resetReservePlanDrawForm(); //重置表单
                  //this.open = false;
                  //this.handleQuery();
                  this.getList();
                } else {
                  this.$modal.msgError("修改失败");
                }
              });
            }
            this.fileList = [];
            this.multipleSelectionIds = [];
          }
          this.dloading = false;
          this.$nextTick(() => {
            this.$refs["addform1"].resetFields();
          });
        }
      });
    },
    /** drawer-form表单，取消操作 **/
    drawerClose() {
      this.$nextTick(() => {
        this.$refs["form1"].clearValidate();
      });
      // this.$refs['form1'].clearValidate();
      this.resetReservePlanDrawForm();
      this.drawer = false;
      this.dloading = false;
      this.addForm = false;
    },
    /** 新增按钮操作 **/
    handleAdd() {
      this.resetReservePlanDrawForm();
      this.title = "新增预案";
      this.planChangeSink = "add";
      this.dialogFormVisible = true;
      this.visibleAdd = true;
    },
    changePartitionSelection(e) {
      this.$forceUpdate();
    },
    changeSelection(e) {
      var that = this;
      this.eqTunnelData.forEach((item) => {
        if (item.tunnelId == e) {
          this.reservePlanDrawForm.sId = null;
          that.eqTunnelDataList = item.sdTunnelSubareas;
          this.getCheckPlanName()
          that.$forceUpdate();
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });

      this.resetForm("addForm1");
      // this.resetReservePlanDrawForm();
      this.planChangeSink = "edit";

      const id = row.id || this.ids;
      listTunnels().then((res) => {
        console.log(res, "resresres");
        this.eqTunnelData = res.rows;
      });
      this.getDicts("sd_emergency_plan_type").then((response) => {
        this.planCategory = response.data;
      });
      getPlan(id).then((response) => {
        console.log(response.data, "response修改弹窗");
        this.reservePlanDrawForm = response.data;
        this.reservePlanDrawForm.tunnelId = response.data.sdTunnels.tunnelId;
        this.reservePlanDrawForm.sId = response.data.sdTunnelSubarea.sId;
        this.reservePlanDrawForm.category = response.data.category;

        this.handleChangeControl(3, response.data.prevControlType);
        if (
          this.reservePlanDrawForm.strategyId != -1 &&
          this.reservePlanDrawForm.strategyId != "-1" &&
          this.reservePlanDrawForm.strategyId != null
        ) {
          this.multipleSelectionIds =
            this.reservePlanDrawForm.strategyId.split(";");
        }
        let fileInfo = response.data.pFileList;
        this.$nextTick(() => {
          if (fileInfo) {
            for (var i = 0; i < fileInfo.length; i++) {
              let fileModel = {};
              fileModel.name = fileInfo[i].fileName;
              fileModel.url = fileInfo[i].url;
              fileModel.fId = fileInfo[i].id;
              this.fileList.push(fileModel);
            }
          }
        });
        loading.close();
        this.dialogFormVisible = true;
        this.title = "修改预案信息";
        //文件回显
      });
      // this.drawer = true;
      // this.$nextTick(() => {
      //   this.$refs["form1"].resetFields();
      // });
      // this.$nextTick(() => {
      //   this.$refs["form1"].clearValidate();
      // });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      /* debugger */
      let that = this;
      const ids = row.id || this.ids;
      //  const rlIds = row.id || this.rlIds;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delPlan(ids);
        })
        .then(() => {
          /*this.handleQuery();*/
          that.$refs.planTable.clearSelection();
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {
          that.$refs.planTable.clearSelection();
          this.getList();
        });
    },
    //移除文件
    handleRemove(file, fileList) {
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
      // return this.$confirm(`确定移除 ${ file.name }？`);
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择5个文件`);
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      let existFile = fileList
        .slice(0, fileList.length - 1)
        .find((f) => f.name === file.name);
      if (existFile) {
        this.$message.error("当前文件已经存在!");
        fileList.pop();
      }
      // let number = [];
      // fileList.forEach((item, index) => {
      //   if (item.name.length > 20) {
      //     number.push({
      //       zxc: index
      //     })
      //     fileList.splice(index, 1);
      //   }
      // })
      // if (number.length > 0) {
      //   this.text = '* 文件名不得超过 20 个字符  !'
      // }
      this.fileList = fileList;
    },
    /** 查询相关策略下拉列表 */
    getStrategyInfo() {
      listStrategy({ strategyType: "0" }).then((response) => {
        this.strategyData = response.rows;
      });
    },
    /** 查询事件类型下拉列表 */
    getPlanType() {
      let data = {};
      listEventType(data).then((response) => {
        this.planTypeDataAll = response.rows;
      });
    },
    //关闭drawer
    handleClose(done) {
      done();
      this.$refs["form1"].resetFields();
    },
    //关闭drawer
    handleFileClose(done) {
      done();
      this.$refs.planTable.clearSelection();
    },
    /** 查询预案信息列表 */
    getList() {
      this.loading = true;
      if (this.manageStatin == "1") {
        this.queryParams.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      listPlan(this.queryParams).then((response) => {
        this.planList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.$refs.planTable.clearSelection();
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 10;
      this.$refs.planTable.bodyWrapper.scrollTop = 0;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.planName = "";
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.rlIds = selection.map((item) => item.planFileId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 点击某一行，将其选中(表格)
    handleRowClick(row, i, a) {
      this.$refs.planTable.toggleRowSelection(row);
    },
    // 点击某一行，将其选中(执行策略弹窗)
    multipleTableRowClick(row) {
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    // 点击某一行，将其选中(相关策略弹窗)
    addMultipleTableRowClick(row) {
      this.$refs.addMultipleTable.toggleRowSelection(row);
    },
  },
  watch: {
    config: {
      handler() {
        this.resetCascader++;
      },
      deep: true,
    },

    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      this.getList();
      this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      listTunnels(this.paramsData).then((res) => {
        this.eqTunnelData = res.rows;
      });
    },
  },
};
</script>
<style>
.planBox .el-input-number__decrease {
  border-right: 1px solid #00152b !important;
}
#cascader-menu-45-0 .el-radio {
  display: none !important;
}
</style>
<style lang="scss" scoped>
::v-deep .el-scrollbar__wrap {
  overflow: hidden !important;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
// ::v-deep .el-dialog .el-dialog__header{
//     // background-image: url(../../../assets/cloudControl/dialogHeader.png);
//     // background-repeat: no-repeat;
//     // background-position-x: right;
//     background: linear-gradient(270deg, rgba(1,149,251,0) 0%, rgba(1,149,251,0.35) 100%);
// }
// ::v-deep .el-form-item--medium .el-form-item__label {
//   line-height: 3vh;
// }
.strategy-dialog {
  ::v-deep .el-dialog__body {
    height: 72vh;
    overflow: auto;
  }
}
::v-deep .in-checked-path .el-radio {
  display: none;
}

::v-deep .in-checked-path .el-radio {
  display: none;
}
.colflex {
  display: flex;
}

.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
  font-size: 1.125rem;
}

.workbench {
  background-image: url("../../../assets/image/lane/3duan.png");
}

.chedaoImage {
  // position: relative;
  width: 1620px;
  height: 580px;
  margin-bottom: 20px;
}

.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  /* // align-items: center; */
  width: 40px !important;
}

.el-dialog-div {
  height: 80vh;
  overflow: auto;
}

// .mouseHover {
//   &:hover {
//     z-index: 10;

//     input {
//       cursor: pointer;
//       background-color: #eee;
//       color: #000;
//     }
//   }
// }
.dialogButton {
  width: 50px;
  height: 24px;
  border: solid 1px #ccc;
  border-radius: 4px;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  margin-top: 10px;
  cursor: pointer;
  margin-left: 20px;
  // margin-right: 10px;
}

.flexWrap {
  flex-wrap: nowrap;
}
.myTable {
  padding: 10px 0;
  margin-bottom: 10px;
  .el-form--inline .el-form-item {
    margin-right: 0px;
  }
  .el-col {
    text-align: center;
    padding: 0px !important;
  }
}
.indexBox {
  display: flex;
  flex-flow: column;
  align-items: center;
  justify-content: space-evenly;
  cursor: pointer;
  i {
    font-size: 15px;
  }
  .disabledClass {
    pointer-events: none;
    cursor: auto !important;
    color: #ccc;
  }
}
.planBox {
  .el-col {
    text-align: center;
  }
}
.dialongBox {
  // border: 1px solid #05afe3;
  padding: 10px;
  box-sizing: border-box;
}
.dialongBox + .dialongBox {
  margin-top: 10px;
}

.dialogTableButtonBox {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 35px;
  padding-right: 0 !important;
  margin-left: 2px;
  .delete,
  .add {
    width: 16px;
    height: 16px;
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100%;
    border: none;
    background-color: transparent;
  }
  .delete {
    background-image: url(../../../assets/icons/delete.png);
  }
  .add {
    background-image: url(../../../assets/icons/add.png);
  }
}
.operationDiglog {
  .el-dialog__body {
    padding: 20px !important;
  }
}
</style>
