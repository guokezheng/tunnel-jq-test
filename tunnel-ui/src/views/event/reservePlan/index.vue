<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-12-08 15:17:28
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-02-24 10:51:28
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
          >新增预案
        </el-button>
        <el-button size="small" @click="resetQuery" 
            >刷新</el-button
          >
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
              icon="icon-gym-Gsearch"
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
        label-width="75px"
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
        <el-form-item label="预案类型" prop="category" >
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
        </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId" >
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
    <div class="tableTopHr" ></div>
    <el-table
      ref="planTable"
      v-loading="loading"
      :data="planList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      max-height="640"
      :row-class-name="tableRowClassName"
    >
      <el-table-column
        type="index"
        width="70"
        align="center"
        label="序号">
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
      <el-table-column
        align="center"
        label="预案名称"
        prop="planName"
      >
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
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="相关策略"
        width="200"
      >
        <template slot-scope="scope">
          <!-- <p v-show="scope.row.strategyNames != null"
                  style="overflow: hidden;text-overflow: ellipsis;cursor: default;"
                  @click="showStrategyContent(scope.row)">
                 {{ scope.row.strategyNames }}
            </p> -->
          <el-tag
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
      </el-table-column>
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
          <!--          <el-button-->
          <!--            v-hasPermi="['business:plan:remove']"-->
          <!--            size="mini"-->
          <!--            class="tableBlueButtton"-->
          <!--            @click="openWorkbench(scope.row)"-->
          <!--            >预览-->
          <!--          </el-button>-->
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
    >
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
    >
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
    >
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
    <el-dialog :visible.sync="strategyDialog" title="相关策略" width="30%">
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
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form
        ref="addform1"
        :model="reservePlanDrawForm"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="reservePlanDrawForm.tunnelId"
            placeholder="请选择所属隧道"
            style="width: 80%"
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
            style="width: 80%"
          >
            <el-option
              v-for="(item, index) in directionData"
              :key="index"
              :label="item.dictLabel"
              :value="item.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId">
          <el-select
            v-model="reservePlanDrawForm.planTypeId"
            placeholder="请选择事件类型"
            style="width: 80%"
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
            v-model="reservePlanDrawForm.eventGrade"
            placeholder="请选择事件等级"
            style="width: 80%"
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
            style="width: 80%"
          />
        </el-form-item>
        <el-form-item label="预案描述" prop="planDescription">
          <el-input
            v-model="reservePlanDrawForm.planDescription"
            maxlength="250"
            placeholder="请输入预案描述"
            style="width: 80%"
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
            style="width: 80%"
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelsubmitUpload">取 消</el-button>
        <el-button type="primary" @click="submitUpload">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 配置策略 -->
    <el-dialog
      :before-close="handleClose"
      title="联控流程"
      :visible.sync="strategyVisible"
      append-to-body
      width="78%"
    >
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
                class="flex-row"
                type="primary"
                size="mini"
                icon="el-icon-delete"
                @click="deleteInfo(number)"
              >
                删除
              </el-button>
              <el-button
                class="flex-row"
                type="primary"
                size="mini"
                icon="el-icon-plus"
                @click="addInfo(number)"
              >
                插入
              </el-button>
            </el-col>
          </el-row>
          <el-row
            class="myTable"
            style="background-color: #51aced; color: white"
          >
            <el-col :span="2"> 排序 </el-col>
            <el-col :span="4"> 操作处置名称 </el-col>
            <el-col :span="4"> 设备资源类型 </el-col>
            <el-col :span="4"> 检索规则条件 </el-col>
            <el-col :span="5"> 选择设备（可多选） </el-col>
            <el-col :span="3"> 控制指令 </el-col>
            <el-col :span="2"> 操作 </el-col>
          </el-row>
          <el-row :gutter="20" class="planBox">
            <el-col
              :span="24"
              class="colflex"
              v-for="(itemed, index) in item.processesList"
              :key="index"
            >
              <el-col :span="2" style="text-align: center">
                <el-form-item prop="index">
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
              
                <!-- <el-select
                  v-model="itemed.eqTypeId"
                  placeholder="设备类型"
                  clearable
                  @change="changeEquipmentType(itemed.eqTypeId, number, index)"
                >
                  <el-option
                    v-for="items in equipmentTypeData"
                    :key="items.typeId"
                    :label="items.typeName"
                    :value="items.typeId"
                  />
                </el-select> -->
              </el-col>
              <el-col :span="4">
                
                <el-select
                  v-model="itemed.retrievalRule"
                  placeholder="规则条件"
                  clearable
                  @change="ruleChange(number, index, itemed.retrievalRule)"
                >
                  <el-option
                    v-for="itemz in retrievalRuleList"
                    :key="itemz.dictValue"
                    :label="itemz.dictLabel"
                    :value="itemz.dictValue"
                  />
                </el-select>
              
              </el-col>
              <el-col :span="5">
                <el-cascader
                  v-model="itemed.equipments"
                  :options="itemed.equipmentData"
                  :disabled="itemed.disabled"
                  :props="devicesProps"
                  :show-all-levels="false"
                  collapse-tags
                  @change="qbgChange(number,index, itemed.equipments)"
                  style="width: 100%"
                ></el-cascader>
                <!-- <el-select
                  v-model="itemed.equipments"
                  :disabled="itemed.disabled"
                  multiple
                  collapse-tags
                  placeholder="请选择设备"
                  style="width: 100%"
                  @change="qbgChange(index, itemed.equipments)"
                >
                  <el-option
                    v-for="single in itemed.equipmentData"
                    :key="single.eqId"
                    :label="single.eqName"
                    :value="single.eqId"
                  />
                </el-select> -->
              </el-col>

              <el-col
                :span="3"
                v-if="
                  itemed.eqTypeId != 16 &&
                  itemed.eqTypeId != 36 &&
                  itemed.eqTypeId != 22
                "
              >
                <el-select v-model="itemed.state" placeholder="设备操作">
                  <el-option
                    v-for="ite in itemed.eqStateList"
                    :key="ite.deviceState"
                    :label="ite.stateName"
                    :value="ite.deviceState"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <!-- 选择情报板模板 -->
              <el-col
                :span="3"
                v-if="itemed.eqTypeId == 16 || itemed.eqTypeId == 36"
              >
                  <el-cascader
                    placeholder="请选择模板"
                    :props="checkStrictly"
                    v-model="itemed.state"
                    :options="itemed.templatesList"
                    :show-all-levels="false"
                    :key="resetCascader"
                    clearable
                    collapse-tags
                  ></el-cascader>
              </el-col>

              <el-col :span="3" v-if="itemed.eqTypeId == 22">
                <el-form-item prop="itemed.state">
                  <el-select v-model="itemed.state" placeholder="播放文件">
                    <el-option
                      v-for="(itemv, figure) in fileList"
                      :key="figure"
                      :label="itemv.name"
                      :value="itemv.fileName"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col
                :span="2"
                style="
                  display: flex;
                  justify-content: space-around;
                  height: 35px;
                "
              >
                <el-button
                  type=""
                  icon="el-icon-delete"
                  circle
                  @click="removeItem(number, index)"
                ></el-button>
                <el-button
                  type=""
                  icon="el-icon-plus"
                  circle
                  @click="addItem(number, index)"
                ></el-button>
              </el-col>
            </el-col>
          </el-row>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          style="width: 10%"
          type="primary"
          v-hasPermi="['plan:process:add']"
          @click="submitstrategy"
          >保存</el-button
        >
        <el-button style="width: 10%" @click="closeStrategy">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import workBench from "./workBench";
import {
  listEqTypeStateIsControl,
  getVMSTemplatesByDevIdAndCategory,
  getAudioFileList,
} from "@/api/equipment/eqTypeState/api";
import { listDevices } from "@/api/equipment/eqlist/api";
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
import { getJlyTunnel, getTunnels } from "@/api/equipment/tunnel/api.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { fastLerp } from "zrender/lib/tool/color";
import {
  addProcess,
  getListByRId,
  previewDisplay,
} from "@/api/event/reserveProcess";

export default {
  name: "Plan",
  // components: {
  //   workBench,
  // },
  data() {
    return {
      resetCascader:0,
      equipmentTypeProps: {
        value: "id",
        label: "label",
        // checkStrictly: true,
        emitPath: false,
      },
      devicesProps: {
        multiple: true,
        checkStrictly: true,
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
              qbbState:'',
              retrievalRule: "", //规则条件
              equipmentTypeData: [],
              equipmentData: [],
              eqStateList: [],
              templatesList:[],
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
    tunnelNames(this.paramsData).then((res) => {
      this.eqTunnelData = res.rows;
      this.eqTunnelData.forEach((item) => {
        item.sdTunnelSubareas.forEach((item, index) => {
          this.eqTunnelDataList.push(item);
        });
      });
    });
    this.getDicts("sd_emergency_plan_type").then((response) => {
      console.log(response.data, "预案类型");
      this.planCategory = response.data;
    });
    //规则条件
    this.getDicts("sd_device_retrieval_rule").then((response) => {
      this.retrievalRuleList = response.data;
    });
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
    // listEventType().then((response) => {
    //   console.log(response, "事件类型下拉");
    //   this.planTypeData = response.rows;
    // });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
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
    addInfo(index) {
      let data = {
        processStageName: "",
        processesList: [
          {
            processName: "",
            eqTypeId: "",
            equipments: [],
            state: "",
            qbbState:'',
            retrievalRule: null, //规则条件
            equipmentTypeData: [],
            equipmentData: [],
            eqStateList: [],
            disabled: false,
            templatesList:[],
          },
        ],
      };
      this.planTypeIdList.splice(index + 1, 0, data);
    },
    /** 查询设备类型列表 */
    getEquipmentType() {
      for (let i = 0; i < this.planTypeIdList.length; i++) {
        getCategoryTree().then((data) => {
          console.log(data, "设备类型");
          this.$set(this.planTypeIdList[i], "equipmentTypeData", data.data);
          this.equipmentTypeData = data.data;
        });
      }
    },
    getPlanTypeData(id) {
      for (var item of this.planTypeData) {
        if (item.id == id) {
          return item.eventType;
        }
      }
    },
    //  上移
    // moveTop(i, item) {
    //   if (item && i) {
    //     let obj = { ...this.planTypeIdList[i - 1] };
    //     this.planTypeIdList.splice(i - 1, 1, item);
    //     this.planTypeIdList.splice(i, 1, obj);
    //     this.$forceUpdate();
    //   }
    // },
    // 下移
    // moveBottom(i, item) {
    //   if (item && typeof i === "number") {
    //     let obj = { ...this.planTypeIdList[i + 1] };
    //     this.planTypeIdList.splice(i + 1, 1, item);
    //     this.planTypeIdList.splice(i, 1, obj);
    //     this.$forceUpdate();
    //   }
    // },

    controlDirectionFormat(row, column) {
      return this.selectDictLabel(
        this.controlDirectionList,
        row.controlDirection
      );
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
      if (value != 1) {
        // 如果不指定设备则设备赋空
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "equipments",
          ""
        );
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "disabled",
          true
        );
      } else {
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "disabled",
          false
        );
      }
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
      console.log(index);
      if(this.planTypeIdList[number].processesList.length == 1){
        return this.$modal.msgWarning("至少保留一行");
      }
      this.planTypeIdList[number].processesList.splice(index, 1);
    },
    // 添加执行操作
    addItem(number, index) {
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
            eqStateList: [],
            disabled: false,
            templatesList:[],
          },
        ],
      };
      this.planTypeIdList[number].processesList.splice(index + 1, 0, data);
      // this.planTypeIdList.splice(index + 1, 0, data);
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
    //删除一行
    updataDeleteForm(index) {
      if (this.planTypeIdList.length <= 1) {
        this.$modal.msgError("请保留一行");
        return;
      }
      this.planTypeIdList.splice(index, 1);
    },
    //添加一行
    addFrom() {
      this.planTypeIdList.push({
        a: [],
      });
      /*this.reserveProcessDrawForm.strategyId.push([{
        id:[]
      }])*/
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
      let params = {
        eqType: eqTypeId, //设备类型
        eqTunnelId: this.currentClickData.tunnelId, //隧道
      };
      getTreeDeviceList(params).then((res) => {
        if (res.data.length == 0) {
          return this.$modal.msgWarning("暂无设备");
        }
        this.$set(
          this.planTypeIdList[number].processesList[index],
          "equipmentData",
          res.data
        );
        console.log(res, "设备列表");
      });
      // listDevices(params).then((res) => {
      //   if (res.rows.length == 0) {
      //     return this.$modal.msgWarning("暂无设备");
      //   }
      //   this.$set(
      //     this.planTypeIdList[number].processesList[index],
      //     "equipmentData",
      //     res.rows
      //   );
      //   console.log(res, "设备列表");
      // });
      if (eqTypeId != 22) {
        this.listEqTypeStateIsControl(eqTypeId, number, index);
      } else {
        //广播
        this.getAudioFileListData();
      }
    },
    getAudioFileListData() {
      let params = {
        tunnelId: this.currentClickData.tunnelId,
        direction: this.currentClickData.direction,
      };
      getAudioFileList(params).then((res) => {
        console.log(res.data, "广播");
        this.fileList = res.data;
        this.$set(this.planTypeIdList[index], "eqStateList", response.rows);
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
    qbgChange(number,index, value) {
      console.log(value);
      let data = value;
      if (
        this.planTypeIdList[number].processesList[index].eqTypeId == 16 ||
        this.planTypeIdList[number].processesList[index].eqTypeId == 36
      ) {
        getVMSTemplatesByDevIdAndCategory(data).then((res) => {
          console.log(res.data, "模板信息");
          // this.templatesList = res.data;
          this.$set(this.planTypeIdList[number].processesList[index], "templatesList", res.data);
        });
      }
    },
    //关闭策略弹窗
    closeStrategy() {
      // this.getTunnelData(this.tunnelId);
      this.strategyVisible = false;
    },
    everyForeach(value){
      return value != '';
    },
    // 编辑策略保存方法
    submitstrategy() {
      console.log(this.planTypeIdList, "000000000000000000");
      for (let i = 0; i < this.planTypeIdList.length; i++) {
        let item = this.planTypeIdList[i].processesList;
        if(this.planTypeIdList[i].processStageName == ''){
          return this.$modal.msgWarning("请填写完整");
        }
        for (let j = 0; j < item.length; j++) {
          console.log(item[j]);
          // 如果指定设备则判断是否填写完整
          if (item[j].retrievalRule == 1) {
            if (
              item[j].equipments == "" ||
              item[j].processName == "" ||
              item[j].state == "" ||
              item[j].retrievalRule == ""
            ) {
              return this.$modal.msgWarning("请填写完整");
            }
          }else if(item[j].retrievalRule == ''){
            return this.$modal.msgWarning("请填写完整");
          }
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
          this.$modal.msgSuccess(res.msg);
          this.getList();
        }
      });
    },
    //预览按钮
    // openWorkbench(row) {
    //   this.tunnelId = row.tunnelId;
    //   this.$nextTick(() => {
    //     this.$refs.workBench.currentClass = "red";
    //     this.$refs.workBench.id = row.id;
    //     this.$refs.workBench.tunnelId = this.tunnelId;
    //     this.$refs.workBench.init();
    //   });
    // },
    //=========================执行相关预案开始=====================
    //执行策略
    /* executionStrategy(row){
       this.$modal.msgSuccess("执行策略中.......");
       handleStrategy(row.strategyId);
     }, */
    // 配置策略
    async chooseStrategyInfo(row) {
      this.getEquipmentType();
      this.reserveId = row.id;
      this.currentClickData = row;
      await getTypeAndStrategy({ isControl: 1 }).then((res) => {
        this.options = res.data;
      });
      getReservePlanProcess(this.reserveId).then((res) => {
        this.planTypeIdList = res.data;
        console.log(this.planTypeIdList, "编辑数据");
        if (this.planTypeIdList.length == 0) {
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
                  equipments: [],
                  disabled: false,
                  templatesList:[],
                },
              ],
            },
          ];
        } else {
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
              console.log(brr);
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

              // 渲染设备列表
              let params = {
                eqType: brr.eqTypeId, //设备类型
                eqTunnelId: this.currentClickData.tunnelId, //隧道
              };
              getTreeDeviceList(params).then((res) => {
                this.$set(
                  this.planTypeIdList[i].processesList[j],
                  "equipmentData",
                  res.data
                );
                console.log(res.data, "设备列表");
              });
              // 渲染设备可控状态
              this.listEqTypeStateIsControl(brr.deviceTypeId, i, j);
              // 请求情报板数据
              if (brr.eqTypeId == 16 || brr.eqTypeId == 36) {
                brr.state = +brr.state;
                this.qbgChange(i,j, brr.equipments);
              }
              //请求广播音频列表数据
              if (data[i].eq_type_id == 22) {
                this.getAudioFileListData();
              }
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
      console.log(row, "row");
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
      console.log(data);
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
      console.log(file, "上传文件");
      this.fileData.append("file", file.file); // append增加数据
    },

    // 上传到服务器
    async submitUpload() {
      console.log(this.reservePlanDrawForm.sId, "this.reservePlanDrawForm");
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
            console.log(
              this.fileData,
              "this.fileDatathis.fileDatathis.fileData"
            );
            if (this.planChangeSink == "add") {
              addPlanFile(this.fileData).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("保存成功");
                  // this.drawer = false;//关闭drawer窗体
                  this.dialogFormVisible = false;
                  this.resetReservePlanDrawForm(); //重置表单
                  //this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError("保存失败");
                }
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
                  this.dialogFormVisible = false;
                  this.resetReservePlanDrawForm(); //重置表单
                  //this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError("修改失败");
                }
              });
            }
            this.multipleSelectionIds = [];
          }
          this.dloading = false;
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
      this.$nextTick(() => {
        this.$refs["addform1"].resetFields();
      });
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
          that.$forceUpdate();
        }
      });
      // this.getDicts("sd_reserve_plan_category").then((response) => {
      //   this.planCategory = response.data;
      // });
      console.log(
        this.eqTunnelDataList,
        " this.eqTunnelDataList this.eqTunnelDataList"
      );
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row);

      // this.$nextTick(() => {
      //   this.$refs["addForm1"].clearValidate();
      // });
      this.resetForm("addForm1");

      // this.resetReservePlanDrawForm();
      this.planChangeSink = "edit";
      const id = row.id || this.ids;
      tunnelNames().then((res) => {
        this.eqTunnelData = res.rows;
        this.eqTunnelData.forEach((item) => {
          item.sdTunnelSubareas.forEach((item, index) => {
            this.eqTunnelDataList.push(item);
          });
        });
      });
      this.getDicts("sd_emergency_plan_type").then((response) => {
        this.planCategory = response.data;
      });
      getPlan(id).then((response) => {
        this.fileList = [];
        this.reservePlanDrawForm = response.data;
        this.reservePlanDrawForm.tunnelId = response.data.sdTunnels.tunnelId;
        this.reservePlanDrawForm.sId = response.data.sdTunnelSubarea.sId;
        this.reservePlanDrawForm.category = response.data.category;
        if (
          this.reservePlanDrawForm.strategyId != -1 &&
          this.reservePlanDrawForm.strategyId != "-1" &&
          this.reservePlanDrawForm.strategyId != null
        ) {
          this.multipleSelectionIds =
            this.reservePlanDrawForm.strategyId.split(";");
        }

        let fileInfo = response.data.pFileList;
        for (var i = 0; i < fileInfo.length; i++) {
          let fileModel = {};
          fileModel.name = fileInfo[i].fileName;
          fileModel.url = fileInfo[i].url;
          fileModel.fId = fileInfo[i].id;
          this.fileList.push(fileModel);
        }
        //文件回显
      });
      // this.drawer = true;
      // this.$nextTick(() => {
      //   this.$refs["form1"].resetFields();
      // });
      // this.$nextTick(() => {
      //   this.$refs["form1"].clearValidate();
      // });
      this.dialogFormVisible = true;
      this.title = "修改预案信息";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      /* debugger */
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
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {});
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
      console.log(file, "filefile");
      console.log(fileList, "fileListfileList");
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
      let data = { prevControlType: 0 };
      listEventType(data).then((response) => {
        console.log(response, "事件类型下拉");
        this.planTypeData = response.rows;
      });
    },
    //关闭drawer
    handleClose(done) {
      done();
      this.$refs["form1"].resetFields();
    },
    //关闭drawer
    handleFileClose(done) {
      // console.log(done,'donedone')
      done();
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
        console.log(this.planList, "========");
        this.planList.forEach((item) => {
          console.log(item.strategyName);
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
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
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
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
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      tunnelNames(this.paramsData).then((res) => {
        this.eqTunnelData = res.rows;
        console.log(this.eqTunnelData, "111");
        this.eqTunnelData.forEach((item) => {
          item.sdTunnelSubareas.forEach((item, index) => {
            this.eqTunnelDataList.push(item);
          });
        });
      });
    },
  },
};
</script>
<style>
#cascader-menu-45-0 .el-radio {
  display: none !important;
}
</style>
<style lang="scss" scoped>
::v-deep .el-form-item--medium .el-form-item__label {
  line-height: 3vh;
}
::v-deep .in-checked-path .el-radio {
  display: none;
}

::v-deep .in-checked-path .el-radio {
  display: none;
}

// .in-checked-path{
//   ::v-deep .el-radio__original{
//     display: none;
//   }
// }
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
  margin-top: 5px;
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
  border: 1px solid #05afe3;
  padding: 10px;
  box-sizing: border-box;
}
</style>
