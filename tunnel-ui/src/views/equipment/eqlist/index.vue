<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
      <el-input v-model="queryParams.eqDirection" v-show="false"/>
      <el-form-item label="所属隧道" prop="eqTunnelId" v-show="manageStatin == '0'">
        <el-select v-model="queryParams.eqTunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                     :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="eqName">
        <el-input v-model="queryParams.eqName" placeholder="请输入设备名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>

      <el-form-item label="设备类型" prop="eqType">
        <el-select v-model="queryParams.eqType" placeholder="请选择设备类型" clearable size="small">
          <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
        <el-button type="primary" plain size="mini" @click="handleAdd"
                   v-hasPermi="['system:devices:add']">新增
        </el-button>
        <el-button type="primary" plain size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPermi="['system:devices:edit']">修改
        </el-button>
        <el-button type="primary" plain size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPermi="['system:devices:remove']">删除
        </el-button>
        <el-button type="primary" plain size="mini" @click="handleExport"
                   v-hasPermi="['system:devices:export']">导出
        </el-button>
        <el-button type="primary" plain size="mini" @click="handleImport"
                   v-hasPermi="['system:devices:import']">导入
        </el-button>
        <!-- <el-button
          type="info"
          icon="el-icon-s-help"
          size="mini"
          @click="checkInstruction"
          >校验指令</el-button
        > -->
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:devices:add']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:devices:edit']">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:devices:remove']">删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:devices:export']">导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain plain icon="el-icon-upload" size="mini" @click="handleImport"
          v-hasPermi="['system:devices:import']">导入</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch = !showSearch" />
        </el-tooltip>
      </div>
    </el-row> -->
    <el-table v-loading="loading" :data="devicesList" @selection-change="handleSelectionChange" max-height="640"
              :row-class-name="tableRowClassName"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备ID" min-width="220" align="center" prop="eqId" show-overflow-tooltip/>
      <el-table-column label="所属隧道" align="center" prop="tunnelName.tunnelName" min-width="100" show-overflow-tooltip/>
      <el-table-column label="设备名称" align="center" prop="eqName" min-width="200" show-overflow-tooltip/>
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" min-width="150" show-overflow-tooltip/>
      <el-table-column label="设备品牌" align="center" prop="brandId" min-width="100" show-overflow-tooltip>
        <template slot-scope="scope">
          <!--<dict-tag :options="dict.type.brand" :value="scope.row.brandId"/>-->

          <span>{{ getBrand(scope.row.brandId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备型号" align="center" prop="eqModel" min-width="100" show-overflow-tooltip/>
      <el-table-column label="设备方向" align="center" prop="eqDirection" min-width="100" show-overflow-tooltip>
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_direction" :value="scope.row.eqDirection"/>
        </template>
      </el-table-column>
      <el-table-column label="设备IP" align="center" prop="ip"/>
      <el-table-column label="设备端口号" align="center" prop="port"/>
      <el-table-column label="桩号" align="center" prop="pile" min-width="150" show-overflow-tooltip/>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" class="tableBlueButtton" @click="updateCmd(scope.row)"
                     v-hasPermi="['system:devices:edit']">控制修改
          </el-button>
          <el-button size="mini" class="tableDelButtton" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:devices:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 添加或修改设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="740px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属隧道" prop="eqTunnelId">
              <el-select v-model="form.eqTunnelId" @change="getPlcs()" placeholder="请选择所属隧道" style="width:100%">
                <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                           :value="item.tunnelId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="eqType">
              <el-select v-model="form.eqType" placeholder="请选择设备类型" @change="changeEqType(form.eqType)" style="width:100%">
                <el-option v-for="item in eqTypeData" :key="item.typeId" :label="item.typeName" :value="item.typeId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="21">
            <el-form-item label="设备ID" prop="eqId" v-if="submitMode == 1">
              <el-input v-model="form.eqId" placeholder="请输入设备ID"/>
            </el-form-item>
          </el-col>
          <el-col :span="1" style="margin: 5px" v-if="submitMode == 1">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="automaticGenerationID(form)">生成
            </el-button>
          </el-col>
          <el-col :span="12">
            <el-form-item label="plc主机" prop="fEqId" v-if="showPlc">
              <el-select v-model="form.fEqId" placeholder="请选择plc主机"
                         @click.native="onChangePlc(form.eqTunnelId)"
                         @change="changePlc(form.fEqId)"
                         style="width:100%">

                <el-option v-for="item in eqHostData" :key="item.eqId" :label="item.eqName" :value="item.eqId"/>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="设备大类" prop="fEqType">
              <el-select v-model="form.fEqType" placeholder="请选择设备大类" clearable style="width:100%">
                <el-option
                  v-for="dict in dict.type.eq_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item label="设备名称" prop="eqName">
              <el-input v-model="form.eqName" placeholder="请输入设备名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备方向" prop="eqDirection">
              <el-select v-model="form.eqDirection" placeholder="请选择设备方向" clearable style="width:100%">
                <el-option
                  v-for="dict in dict.type.sd_direction"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备品牌" prop="brandId">
              <el-select v-model="form.brandId" placeholder="请选择设备品牌" style="width:100%">
                <el-option
                  v-for="item in brandList"
                  :key="item.supplierId"
                  :label="item.shortName"
                  :value="item.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="外部系统" prop="externalSystemId">
              <el-select v-model="form.externalSystemId" placeholder="请选择外部系统" style="width:100%">
                <el-option
                  v-for="item in externalSystemList"
                  :key="item.id"
                  :label="item.systemName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item label="设备型号" prop="eqModel">
              <el-input v-model="form.eqModel" placeholder="设备型号"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="所属车道" prop="lane">
              <el-select v-model="form.lane" placeholder="请选择所属车道" style="width:100%">
                <el-option
                  v-for="dict in dict.type.sd_lane"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备桩号" prop="pile">
              <el-input v-model="form.pile" placeholder="请输入桩号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备整形桩号" prop="pileNum">
              <el-input v-model="form.pileNum" placeholder="请输入设备整形桩号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="lat">
              <el-input v-model="form.lat" placeholder="请输入纬度"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="lng">
              <el-input v-model="form.lng" placeholder="请输入经度"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备IP" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入设备IP"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备端口号" prop="port">
              <el-input v-model="form.port" placeholder="请输入设备端口号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备密钥" prop="secureKey">
              <el-input v-model="form.secureKey" placeholder="请输入设备密钥"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备用户名" prop="eqUser">
              <el-input v-model="form.eqUser" placeholder="请输入设备用户名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备密码" prop="eqPwd">
              <el-input v-model="form.eqPwd" placeholder="请输入设备密码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协议类型" prop="protocol">
              <el-input v-model="form.protocol" placeholder="请输入协议类型"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出场时间" prop="deliveryTime">
              <el-date-picker
                v-model="form.deliveryTime"
                type="date"
                placeholder="请选择出场时间"
                :picker-options="optionsDisable"
                value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维保截至时间" prop="warrantyEndTime">
              <el-date-picker
                v-model="form.warrantyEndTime"
                type="date"
                placeholder="请选择维保截至时间"
                :picker-options="optionsDisable"
                value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备安装时间" prop="installTime">
              <el-date-picker
                v-model="form.installTime"
                type="date"
                placeholder="请选择设备安装时间"
                :picker-options="optionsDisable"
                value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="预期寿命" prop="useLife">
              <el-input v-model="form.useLife" placeholder="请输入预期寿命"/>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <div style="line-height: 40px;margin-left: 10px;">年</div>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用状态" prop="useStatus">
              <el-select v-model="form.useStatus" placeholder="请选择使用状态" clearable style="width:100%">
                <el-option
                  v-for="dict in dict.type.sd_use_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否监控" prop="isMonitor">
              <el-select v-model="form.isMonitor" placeholder="请选择是否监控" clearable style="width:100%">
                <el-option
                  v-for="dict in dict.type.sd_is_monitor"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="21" v-if="showOrhide">
            <el-form-item label="查询指令" prop="controlPointAddress">
              <el-input v-model="form.controlPointAddress" placeholder="请输入查询指令"/>
            </el-form-item>
          </el-col>
          <el-col :span="1" style="margin: 5px" v-if="showOrhide">
            <el-button type="success" icon="el-icon-setting" size="mini" @click="createControlPointAddress">生成
            </el-button>
          </el-col>
          <el-col :span="12" v-if="showOrhide">
            <el-form-item label="查询对应点" prop="qNumber">
              <el-input v-model="form.qNumber" placeholder="请输入对应点"/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="showOrhide">
            <el-form-item label="点位地址" prop="queryPointAddress">
              <el-input v-model="form.queryPointAddress" placeholder="请输入点位地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="安装时间" prop="installTime">
              <el-date-picker clearable v-model="form.installTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请输入安装时间">
              </el-date-picker>
            </el-form-item>
          </el-col> -->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改设备控制指令对话框 -->
    <el-dialog :title="title2" :visible.sync="ctrlcmd" width="700px" append-to-body>
      <el-row>
        <el-col :span="23" v-for="(item, index) in ctrlCommandList" :key="index" :index="index" style="margin: 10px">
          <el-card :body-style="{ padding: '10px' }">
            <div style="padding: 14px">
              <span>{{ item.stateName }}</span>
              <div style="margin-top: 15px; font-size: 18px">
                <span>{{ item.command }}</span>
              </div>
              <!--              <input v-if="item.zhanshi == true" type="text" id="item.eqId">-->
              <el-input v-if="item.zhanshi == true" v-model="input" placeholder="请输入指令"></el-input>
              <div class="bottom clearfix">
                <el-button type="text" class="button" @click="OpenCtrlCmd(item, index)">修改指令</el-button>
                <el-button v-if="item.zhanshi" type="text" class="button" @click="updateCtrlCmd(item, index)">确认修改
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 修改设备控制指令对话框 -->
    <el-dialog :title="instructionTitle" :visible.sync="instructionDialog" width="400px" append-to-body>
      <el-form ref="instructionForm" :model="instructionForm" :rules="instructionFormRules" label-width="100px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="指令模式" prop="instruction">
              <el-select v-model="instructionForm.instruction" placeholder="请选择指令模式" clearable size="small">
                <el-option v-for="dict in instructionTypeOptions" :key="dict.dictValue" :label="dict.dictLabel"
                           :value="dict.dictValue"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="机位" prop="seat">
              <el-input v-model="instructionForm.seat" placeholder="请输入机位"/>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="对应点" prop="qNumber">
              <el-input v-model="instructionForm.qNumber" placeholder="请输入对应点"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="insertEqControlPointAddress"
                   v-hasPermi="['system:devices:createDmcontrolSeat']">确
          定
        </el-button>
        <el-button @click="cancelInstruction">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-if="upload.open" :visible.sync="upload.open" width="400px" append-to-body
               class="zxc">
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :on-error="handleFileError"
                 :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/>
          是否更新已经存在的设备数据
          <el-link type="info" style="font-size: 12px;color:#39ADFF" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          提示：仅允许导入“xls”或“xlsx”格式文件！
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getDevBrandList,
    listDevices,
    getDevices,
    delDevices,
    addDevices,
    createDmcontrolSeat,
    createInstruction,
    updateDevices,
    checkInstruction,
    exportDevices,
    exportDevicesTemplate,
    autoId
  } from "@/api/equipment/eqlist/api";
  import {
    listHosts
  } from "@/api/equipment/plc/api";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    listType
  } from "@/api/equipment/type/api";
  import {
    listDevcmd,
    getDevcmd,
    delDevcmd,
    addDevcmd,
    updateDevcmd,
  } from "@/api/equipment/deviceCmd/api";
  import {getEqTypeStateByType,listEqTypeState,} from "@/api/equipment/eqTypeState/api";
  import { getToken} from "@/utils/auth";
  import { listAllSystem } from "@/api/equipment/externalsystem/system";

  export default {
    name: "Devices",
    //字典值：设备方向，设备品牌，所属车道,使用状态，是否监控，诱导灯控制状态
    dicts: ['sd_direction', 'brand', 'sd_lane', 'sd_use_status', 'sd_is_monitor', 'inductionlamp_control_type','eq_category'],
    data() {
      const validatePass = (rule, value, callback) => {
        console.log(rule, value, callback, "rule, value, callback")
        if (this.option) {
          callback(new Error("请选择列表中已有的选项"));
        } else {
          callback();
        }
      }
      return {
        //不能选择当前日期
        optionsDisable: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
        //巡检状态
        showOrhide: false,
        // plc主机
        showPlc: true,
        //设备品牌
        brandList: [],
        //提交模式 0是修改（隐藏）  1是新增（显示）（控制设备ID字段是否展示）
        submitMode: 1,
        currentDate: new Date(),
        // 遮罩层
        loading: true,
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
        // 设备表格数据
        devicesList: [],
        // 控制指令数据
        ctrlCommandList: [],
        // 设备状态数据
        ctrlStateList: [],
        // 弹出层标题
        title: "",
        title2: "修改控制指令",
        instructionTitle: "生成指令",
        // 是否显示弹出层
        open: false,
        submitFormLoading: false,
        instructionDialog: false,
        manageStatin:this.$cache.local.get("manageStation"),
        ctrlcmd: false,
        //plc主机
        eqHostData: {},
        //所属隧道
        eqTunnelData: {},
        // 设备类型字典
        /* eqTypeOptions: [], */
        //设备类型
        eqTypeData: {},
        // 照明灯类型字典
        eqLampTypeOptions: [],
        instructionTypeOptions: [],
        // 设备方向字典
        eqDirections: [],

        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          fEqId: null,
          eqTunnelId: null,
          eqName: null,
          eqType: null,
          deviceState: null,
        },
        queryCmdParams: {
          codeDeviceId: null,
          fEqId: null,
          codePlcId: null,
          deviceTypeId: null,
          codeDeviceState: null,
          command: null,
        },
        queryStateParams: {
          stateTypeId: null,
          deviceState: null,
          stateName: null,
          isControl: 1,
        },
        // 表单参数
        form: {},
        instructionForm: {},
        // 表单校验
        rules: {
          eqTunnelId: [{
            required: true,
            message: "请选择所属隧道",
            trigger: "change",
          },],
          eqType: [{
            required: true,
            message: "请选择设备类型",
            trigger: "blur",
          }],
          eqName: [{
            required: true,
            message: "请选择设备名称",
            trigger: "blur",
          },],
          eqId: [{
            required: true,
            message: "请填写设备ID",
            trigger: "blur",
          },
            {
              pattern: /^[0-9a-zA-Z_-]{1,}$/,
              message: "请输入数字字母或横线",
            },
          ],
          pileNum: [{
            pattern: /^[1-9]\d*$/,
            message: "只能输入整数",
          }],
          // fEqId: [{
          //   required: true,
          //   message: "请选择plc主机",
          //   trigger: "change",
          // }, ],
          eqDirection: [{
            required: true,
            message: "请选择设备方向",
            trigger: "change",
          },], //{ min: 1, max:1,message: '只允许输入1或0，1为左线0为右线' },{ pattern: /^[0-1]{1,}$/, message: '只允许输入1或0，1为左线0为右线' }
          qNumber: [{
            pattern: /^[0-9]*$/,
            message: "查询对应点需为数字",
            trigger: "blur",
          },],
        },
        instructionFormRules: {
          instruction: [{
            required: true,
            message: "请选择指令模式",
            trigger: "blur",
          },],
          seat: [{
            required: true,
            message: "机位不能为空",
            trigger: "blur"
          },
            {
              pattern: /^[0-9]*$/,
              message: "机位需为数字",
              trigger: "blur"
            },
          ],
          qNumber: [{
            required: true,
            message: "对应点不能为空",
            trigger: "blur"
          },
            {
              pattern: /^[0-9]*$/,
              message: "对应点需为数字",
              trigger: "blur"
            },
          ],
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: {
            Authorization: "Bearer " + getToken()
          },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/devices/importData",
        },
        // zhanshi:false,
        input: "",
        externalSystemList: []
      };
    },

    created() {
      this.getList();
      this.getTunnel();
      this.getEqType();
      this.getStateList();
      this.getDicts("sys_eq_light").then((response) => {
        this.eqLampTypeOptions = response.data;
      });
      this.getDicts("sys_instruction_type").then((response) => {
        this.instructionTypeOptions = response.data;
      });
      /* this.getDicts("sys_eq_state").then(response => {
          this.deviceStateOptions = response.data;
        }); */

      this.getDevBrandList()
      this.getExternalSystemList()
    },
    methods: {
      getDevBrandList() {
        getDevBrandList().then(result => {
          console.log("brandList:>>>",result.data)
          this.brandList = result.data
        })
      },
      getExternalSystemList() {
        listAllSystem().then(result => {
          console.log("externalSystemList:>>>",result.data)
          this.externalSystemList = result.data
        })
      },
      getBrand(num) {
        for (var item of this.brandList) {
          if (item.supplierId == num) {
            return item.shortName;
          }
        }
      },

      // 弹窗校验 当没选择隧道时 点击plc 提示先选隧道
      onChangePlc(eqTunnelId) {
        if (eqTunnelId == null) {
          this.$refs.form.validateField('eqTunnelId') //单独触发校验
        }
      },
      // 选中plc主机时
      changePlc(fEqId) {
        if (fEqId == 0) {
          this.showOrhide = true
        }
      },
      // 点击弹窗设备类型
      changeEqType(data) {
        console.log(data, 'data')
        this.form.fEqId = null
        if (data == 0) {
          this.showPlc = false
          this.showOrhide = false
        } else {
          this.showPlc = true
        }
      },
      // 新增弹窗 自动生成id
      automaticGenerationID(form) {
        console.log(form, 'form')
        if (!form.eqTunnelId) {
          this.$modal.msgError("请选择所属隧道");
          return;
        }
        if (!form.eqType) {
          this.$modal.msgError("请选择设备类型");
          return;
        }
        // if(!form.eqType){
        //   this.$modal.msgError("请选择设备类型");
        //   return;
        // }
        const params = {
          tunnelId: form.eqTunnelId,
          typeId: form.eqType
        }
        autoId(params).then((response) => {
          if (response.data) {
            this.form.eqId = response.data
          } else {
            this.$modal.msgError("未生成设备ID，请检查配置");
          }
        });
      },
      //查询设备状态
      getStateList() {
        var that = this;
        listEqTypeState(this.queryStateParams).then((response) => {
          that.ctrlStateList = response.rows;
        });
      },
      /** 查询设备列表 */
      getList() {
        if(this.manageStatin == '1'){
          this.queryParams.eqTunnelId = this.$cache.local.get("manageStationSelect")
        }
        this.loading = true;
        listDevices(this.queryParams).then((response) => {
          this.devicesList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** plc主机 */
      getPlcs() {
        listDevices({
          eqTunnelId: this.form.eqTunnelId,
          eqType: '0',
        }).then((response) => {
          console.log(response, 'response')
          this.eqHostData = response.rows;
          // this.eqHostData.unshift({
          //   eqId: 0,
          //   eqName: "未关联PLC设备",
          // });
        });
      },
      /** 所属隧道 */
      getTunnel() {
        if(this.$cache.local.get("manageStation") == '1'){
          this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
        }
        listTunnels(this.queryParams).then((response) => {
          console.log(response.rows, "所属隧道列表")
          this.eqTunnelData = response.rows;
        });
      },
      /** 设备类型 */
      getEqType() {
        listType().then((response) => {
          this.eqTypeData = response.rows;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.submitFormLoading = false;
        this.reset()
      },
      cancelInstruction() {
        this.instructionDialog = false;
      },
      // 表单重置
      reset() {
        this.form = {
          brandId: null,
          externalSystemId: null,
          eqId: null,
          fEqId: null,
          eqTunnelId: null,
          eqName: null,
          eqDirection: null,
          stakeMark: null,
          eqType: null,
          fEqType: null,
          queryPointAddress: null,
          controlPointAddress: null,
          deviceState: null,
          remark: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          leishiDeviceIp: null,
          lane: null,
          pile: null,
          pileName: null,
          lat: null,
          lng: null,
          ip: null,
          port: null,
          secureKey: null,
          eqUser: null,
          eqPwd: null,
          protocol: null,
          deliveryTime: null,
          warrantyEndTime: null,
          installTime: null,
          useLife: null,
          useStatus: null,
          isMonitor: null,
        };
        this.resetForm("form");
        this.queryCmdParams = {
          codeDeviceId: null,
          fEqId: null,
          codePlcId: null,
          deviceTypeId: null,
          codeDeviceState: null,
          command: null,
        };
        this.eqHostData = []
      },
      instructionFormReset() {
        this.instructionForm = {
          instruction: null,
          fEqId: null,
          seat: null,
          qNumber: null,
        };
        this.resetForm("instructionForm");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        /* this.queryParams.eqDirection = 0; */
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map((item) => item.eqId);
        this.single = selection.length !== 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.submitMode = 1;
        this.title = "添加设备";
        this.form.fEqId = ''
      },
      /** 打开修改控制指令框 */
      OpenCtrlCmd(item, index) {
        this.input = "";
        var myItem = this.ctrlCommandList[index].zhanshi;
        for (let i = 0; i < this.ctrlCommandList.length; i++) {
          this.ctrlCommandList[i].zhanshi = false;
        }
        if (myItem) {
          this.ctrlCommandList[index].zhanshi = false;
        } else {
          this.ctrlCommandList[index].zhanshi = true;
          this.instructionForm.instruction =
            this.instructionTypeOptions[1].dictValue;
          this.instructionForm.fEqId = this.queryCmdParams.fEqId;
          this.instructionForm.seat = this.ctrlCommandList[index].dmcontrolSeat;
          this.instructionDialog = true;
        }
      },
      /** 确认修改 */
      updateCtrlCmd(item, index) {
        this.queryCmdParams.codeDeviceId = item.eqId;
        this.queryCmdParams.codeDeviceState = item.stateId;
        this.queryCmdParams.codePlcId = item.fEqId;
        this.queryCmdParams.deviceTypeId = item.eqType;
        this.queryCmdParams.command = this.input;
        updateDevcmd(this.queryCmdParams).then((response) => {
          this.$modal.msgSuccess("修改成功");
          listDevcmd(this.queryCmdParams).then((response) => {
            this.ctrlCommandList[index].command = response.rows[0].command;
            this.ctrlCommandList[index].zhanshi = false;
          });
        });
      },
      /** 修改控制指令操作 */
      updateCmd(row) {
        this.input = "";
        this.reset();
        this.instructionFormReset();
        var that = this;
        that.ctrlCommandList = [];
        that.queryCmdParams.codeDeviceId = row.eqId;
        that.queryCmdParams.fEqId = row.fEqId;
        //筛选出选中设备的设备类型的所有设备状态
        for (var cs in that.ctrlStateList) {
          if (that.ctrlStateList[cs].stateTypeId == row.eqType) {
            var ctrl = {};
            ctrl.stateId = that.ctrlStateList[cs].deviceState;
            ctrl.stateName = that.ctrlStateList[cs].stateName;
            ctrl.command = "";
            ctrl.eqId = row.eqId;
            ctrl.fEqId = row.fEqId;
            ctrl.eqType = row.eqType;
            ctrl.dmcontrolSeat = row.dmcontrolSeat;
            ctrl.zhanshi = false;
            that.ctrlCommandList.push(ctrl);
          }
        }
        if (that.ctrlCommandList.length > 0) {
          this.ctrlcmd = true;
          //查询选中设备的设备指令数据
          listDevcmd(this.queryCmdParams).then((response) => {
            for (var cc in that.ctrlCommandList) {
              for (let i = 0; i < response.rows.length; i++) {
                //设备状态相同
                if (that.ctrlCommandList[cc].stateId == response.rows[i].codeDeviceState) {
                  that.ctrlCommandList[cc].command = response.rows[i].command;
                }
              }
            }
          });
        } else {
          this.$modal.msgSuccess("该设备没有控制状态");
        }
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const eqId = row.eqId || this.ids;
        getDevices(eqId).then((response) => {
          this.form = response.data;
          this.open = true;
          this.submitMode = 0;
          this.title = "修改设备";
        });
      },
      /** 提交按钮 */
      submitForm() {
        if (this.submitFormLoading) return
        /*this.submitFormLoading = true*/
        this.$refs["form"].validate(async (valid) => {
          if (valid) {
            if (this.submitMode == 0) {
              await updateDevices(this.form).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              await addDevices(this.form).then((response) => {
                if (response.code === 200) {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                }
              });
            }
          }
          /*this.submitFormLoading = false*/
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const eqIds = row.eqId || this.ids;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            return delDevices(eqIds);
          })
          .then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          })
          .catch(function () {
          });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有设备管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDevices(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
      },

      /** 打开导入表弹窗 */
      handleImport() {
        this.upload.title = "设备导入";
        this.upload.open = true;
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        if(response.msg!=null&&response.msg!=""){
          this.$alert(response.msg, "导入结果", {
            customClass: 'el-message-box_style',
            dangerouslyUseHTMLString: true

          });
        }else{
          const msg = "恭喜您，数据已全部导入成功！";
          this.$alert(msg, "导入结果", {
            customClass: 'el-message-box_style',
            dangerouslyUseHTMLString: true

          });
        }

        this.$forceUpdate()
        this.getList();
      },
      // 文件上传失败处理
      handleFileError(err, file, fileList) {
        this.$modal.msgError(JSON.parse(err.message).message)
        this.upload.open = false
      },
      /** 下载模板操作 */
      importTemplate() {
        /* exportDevicesTemplate()*/
        /*.then((response) => {*/
        this.$download.name('sbsj.xlsx', false);
        /*});*/
      },
      insertEqControlPointAddress() {
        this.$refs["instructionForm"].validate((valid) => {
          if (valid) {
            createDmcontrolSeat(this.instructionForm).then((response) => {
              this.instructionDialog = false;
              this.form.controlPointAddress = response.instruction;
              this.input = response.instruction;
            });
          }
        });
      },
      createControlPointAddress() {
        if (
          this.form.fEqId != null &&
          this.form.qNumber != null
        ) {
          createInstruction(this.form).then((response) => {
            this.form.controlPointAddress = response.instructionSeat;
          });
        } else {
          this.$modal.msgError(
            "plc主机,查询+机位(格式：DM_*/CIO_*),查询对应点不能为空!!"
          );
        }
      },
      checkInstruction() {
        checkInstruction(this.queryParams).then((response) => {
          if (response.errorDate.length > 0) {
            alert(
              "共查询" +
              response.sumDate +
              "条数据," +
              response.errorDate.length +
              "数据指令出错。"
            );
            this.queryParams.pageNum = 1;
            this.queryParams.eqDirection = 1;
            this.getList();
          } else {
            this.$modal.msgSuccess("没有数据指令出错");
          }
        });
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
      // 表格行样式
      tableRowClassName({row, rowIndex}) {
        if (rowIndex % 2 == 0) {
          return 'tableEvenRow';
        } else {
          return "tableOddRow";
        }
      },
    },
    watch: {
      "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
        console.log(newVal, "0000000000000000000000");
        this.getList();
        this.getTunnel();
      }
    }
  };
</script>
<style scoped lang="scss">
  .el-select-dropdown__item.selected {
    color: #606266;
    font-weight: 400;
  }

  ::v-deep .el-dialog__body {
    max-height: 690px;
    overflow: auto;
  }

</style>
<style lang="scss">
  .el-message-box_style {
    .el-message-box__content {
      max-height: 500px !important;
      overflow: auto !important;
    }
  }
</style>
