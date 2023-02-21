<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <!-- 全局搜索 -->
    <el-row :gutter="20" style="margin: 10px 0 25px">
      <el-col :span="4">
        <el-button
          v-hasPermi="['system:material:add']"
          size="mini"
          type="primary"
          plain
          @click="handleAdd()"
          >新增物资
        </el-button>
<!--        <el-button type="primary" plain size="mini" @click="toggleExpandAll"
          >展开/折叠</el-button
        >-->
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >刷新</el-button
        >
      </el-col>
      <el-col :span="6" :offset="14">
        <div  ref = "main" class="grid-content bg-purple">
          <el-input
            placeholder="请输入物资名称、桩号，回车搜索"
            v-model="queryParams.materialName"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="el-icon-s-fold"
              @click="wz_boxShow = !wz_boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div class="wz_searchBox" v-show="wz_boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="75px"
      >
        <el-form-item label="物资类型" prop="materialType" style="width: 100%">
          <el-select
            v-model="queryParams.materialType"
            clearable
            placeholder="请选择物资类型"
            size="small"
          >
            <el-option
              v-for="dict in materialTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <!--        <el-form-item label="开始桩号:">
            <el-form-item prop="station">
              <el-input
                style="width:335px"
                class="dateClass"
                v-model.number="queryParams.station"
                placeholder="0~999"
                clearable
                size="small"
                oninput="value=value.replace(/[^\d]/g,'')"
              >
                <template slot="prepend">K</template>
              </el-input>
            </el-form-item>
            <span style="margin: 0 5px" class="formAddClass">+</span>
            <el-form-item prop="deviation">
              <el-input
                style="width:335px"
                class="dateClass"
                v-model.number="queryParams.deviation"
                placeholder="桩号偏差"
                clearable
                size="small"
              />
            </el-form-item>
          </el-form-item>
          <el-form-item label="结束桩号:">
            <el-form-item prop="endStation">
              <el-input
                style="width: 335px"
                class="dateClass"
                v-model.number="queryParams.endStation"
                placeholder="0~999"
                clearable
                size="small"
                oninput="value=value.replace(/[^\d]/g,'')"
              >
                <template slot="prepend">K</template>
              </el-input>
            </el-form-item>
            <span style="margin: 0 5px" class="formAddClass">+</span>
            <el-form-item prop="endDeviation">
              <el-input
                style="width: 335px"
                class="dateClass"
                v-model.number="queryParams.endDeviation"
                placeholder="桩号偏差"
                clearable
                size="small"
              />
            </el-form-item>
          </el-form-item>-->
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
    <!--    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      :rules="queryParamsRules"
    >
      <el-form-item label="物资类型" prop="materialType">
        <el-select
          v-model="queryParams.materialType"
          placeholder="请选择物资类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in materialTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开始桩号:">
        <el-form-item prop="station">
          <el-input
            style="width: 135px"
            class="dateClass"
            v-model.number="queryParams.station"
            placeholder="0~999"
            clearable
            size="small"
            oninput="value=value.replace(/[^\d]/g,'')"
          >
            <template slot="prepend">K</template>
          </el-input>
        </el-form-item>
        <span style="margin: 0 5px" class="formAddClass">+</span>
        <el-form-item prop="deviation">
          <el-input
            style="width: 145px"
            class="dateClass"
            v-model.number="queryParams.deviation"
            placeholder="桩号偏差"
            clearable
            size="small"
          />
        </el-form-item>
      </el-form-item>
      <el-form-item label="结束桩号:">
        <el-form-item prop="endStation">
          <el-input
            style="width: 135px"
            class="dateClass"
            v-model.number="queryParams.endStation"
            placeholder="0~999"
            clearable
            size="small"
            oninput="value=value.replace(/[^\d]/g,'')"
          >
            <template slot="prepend">K</template>
          </el-input>
        </el-form-item>
        <span style="margin: 0 5px" class="formAddClass">+</span>
        <el-form-item prop="endDeviation">
          <el-input
            style="width: 145px"
            class="dateClass"
            v-model.number="queryParams.endDeviation"
            placeholder="桩号偏差"
            clearable
            size="small"
          />
        </el-form-item>
      </el-form-item>
      &lt;!&ndash;      <el-form-item label="状态" prop="state">&ndash;&gt;
      &lt;!&ndash;        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable size="small">&ndash;&gt;
      &lt;!&ndash;          <el-option v-for="dict in stateOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />&ndash;&gt;
      &lt;!&ndash;        </el-select>&ndash;&gt;
      &lt;!&ndash;      </el-form-item>&ndash;&gt;
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQuery"
          >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:material:add']"
          >新增</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleUpdateMaterial"
          v-hasPermi="['system:material:edit']"
          >修改</el-button
        >
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:SdEmergencyPer:remove']"
          >删除</el-button
        >
      </el-form-item>
    </el-form>-->
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:material:add']">新增物资</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:SdEmergencyPer:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" @click="handleUpdate(1)" :disabled="single"
                   v-hasPermi="['system:material:crk']">物资入库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" @click="handleUpdate(2)" :disabled="single"
                   v-hasPermi="['system:material:crk']">物资出库</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row> -->

    <el-table
      v-loading="loading"
      :data="materialList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      max-height="640"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
<!--      <el-table-column label="序号" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>-->
      <!--      <el-table-column label="物资编号" align="center" prop="materialId" />-->
      <el-table-column label="物资名称" align="center" prop="materialName" />
      <el-table-column
        label="物资类型"
        align="center"
        prop="materialType"
        :formatter="materialTypeFormat"
      />
      <el-table-column label="隧道" align="center" prop="tunnelName" />
      <el-table-column label="桩号" align="center" prop="station" />
      <el-table-column
        label="方向"
        align="center"
        prop="direction"
        :formatter="directionFormat"
      />
      <el-table-column label="数量" align="center" prop="number" />
      <el-table-column
        label="保质期(月)"
        align="center"
        prop="qualityGuaranteePeriod"
      />
      <el-table-column
        label="生产日期"
        align="center"
        prop="dateOfManufacture"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdateMaterial(scope.row)"
            v-hasPermi="['system:material:edit']"
            >修改</el-button
          >
          <!-- <el-button size="mini" type="text" icon="el-icon-edit" @click="openCrkDrawer(scope.row)" v-hasPermi="['system:material:crkRecord']">出入库详情</el-button> -->
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:material:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
      class="zwsj"
      title="出入库详情"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
    >
      <el-form
        ref="crkFormDetail"
        :model="crkFormDetail"
        :rules="rules"
        label-width="80px"
        style="margin-left: 10px"
      >
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in list"
            :key="item.materialId"
            :timestamp="item.createTime"
            placement="top"
            style="padding-bottom: 60px"
          >
            <el-col :span="6">
              <el-form-item label="" style="color: green">
                <template>
                  <div v-if="item.type === '1'">
                    <span class="kcczMessage">入库:</span>{{ item.changeStock }}
                  </div>
                  <div v-else-if="item.type === '2'">
                    <span class="kcczMessage">出库:</span>{{ item.changeStock }}
                  </div>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="">
                <span class="kcMessage">当前库存:</span>{{ item.stock }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="">
                <span class="handlerClass">处理人:</span>
                {{ item.createBy }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="">
                <span class="remarkClass">备注:</span>
                {{ item.remark }}
              </el-form-item>
            </el-col>
          </el-timeline-item>
        </el-timeline>
        <el-form-item
          style="text-align: center; position: relative; bottom: 0; width: 100%"
        >
          <!-- <el-button style="width: 30%;" type="primary" @click="submitmaterialForm" :loading="dloading">{{ dloading ? '提交中 ...' : '保存' }}</el-button> -->
          <el-button
            style="width: 30%; position: absolute; margin-bottom: 20px"
            @click="materialFormClose"
            >取 消</el-button
          >
        </el-form-item>
      </el-form>
    </el-drawer>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加/修改应急资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!--        <el-form-item label="物资编号" prop="materialId">-->
        <!--          <el-input v-model="form.materialId" placeholder="请输入物资编号" />-->
        <!--        </el-form-item>-->
        <el-form-item label="物资名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="物资类型" prop="materialType">
          <el-select
            v-model="form.materialType"
            placeholder="请选择物资类型"
            style="width: 100%"
          >
            <el-option
              v-for="dict in materialTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>

        <el-form-item label="隧道名称" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="桩号" prop="station">
          <el-input
            v-model="form.station"
            placeholder="请输入桩号,例：Kxxx+xxx"
          />
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-select
            v-model="form.direction"
            placeholder="请选择方向"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in directionData"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input
            v-model.number="form.number"
            placeholder="请输入数量"
            style="display: table"
            :min="1"
          />
        </el-form-item>
        <el-form-item label="保质期" prop="qualityGuaranteePeriod">
          <el-input
            v-model.number="form.qualityGuaranteePeriod"
            placeholder="请输入保质期，例：3"
            style="display: table"
          >
            <template slot="append">月</template>
          </el-input>
        </el-form-item>
        <el-form-item label="生产日期" prop="dateOfManufacture">
          <el-date-picker
            clearable
            size="small"
            v-model="form.dateOfManufacture"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择生产时间"
            :picker-options="pickerOptions0"
            class="dateClass"
            @focus="focus"
          >
          </el-date-picker>
        </el-form-item>

        <!-- <el-form-item label="价格(元)" prop="price">
          <el-input v-model="form.price" placeholder="请输入保留两位小数的单价" oninput="value=value.replace(/[^0-9.]/g,'')" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          :disabled="disabled"
          :loading="submitBtnLoading"
          @click="submitForm"
          v-prevent-click
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 出入库应急资源对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="openCrkForm"
      width="650px"
      append-to-body
    >
      <el-form
        ref="formDetail"
        :model="formDetail"
        :rules="rules"
        label-width="80px"
      >
        <fieldset
          class="field"
          style="height: 220px; margin-top: 0.1px; position: relative"
        >
          <el-row style="margin-top: 20px">
            <el-col :span="12">
              <el-form-item
                label="物资名称"
                prop="materialName"
                style="width: 240px"
              >
                <el-input v-model="formDetail.materialName" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="物资类型"
                prop="materialType"
                style="width: 240px"
              >
                <el-select v-model="formDetail.materialType" disabled>
                  <el-option
                    v-for="dict in materialTypeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="12">
              <el-form-item
                label="库存数量"
                prop="position"
                style="width: 240px"
              >
                <el-input v-model="formDetail.inventoryQuantity" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位" prop="company" style="width: 240px">
                <el-input v-model="formDetail.company" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="仓库位置" prop="position" style="width: 240px">
            <el-input v-model="formDetail.position" disabled />
          </el-form-item>
        </fieldset>
      </el-form>
      <el-form
        ref="crkFormRecord"
        :model="crkFormRecord"
        :rules="rules"
        label-width="80px"
      >
        <fieldset
          class="field"
          style="height: 200px; margin-top: 10px; position: relative"
        >
          <el-row style="margin-top: 20px">
            <el-form-item
              v-if="flag == 1"
              label="入库数量"
              prop="stock"
              style="width: 240px"
            >
              <el-input
                v-model="crkFormRecord.changeStock"
                style="width: 300px"
                placeholder="请输入入库数量"
              />
            </el-form-item>
            <el-form-item
              v-if="flag == 2"
              label="出库数量"
              prop="stock"
              style="width: 240px"
            >
              <el-input
                v-model="crkFormRecord.changeStock"
                style="width: 300px"
                placeholder="请输入出库数量"
              />
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="crkFormRecord.remark"
                type="textarea"
                style="width: 300px"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-row>
        </fieldset>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCrkFormRecord">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMaterial,
  getMaterial,
  delMaterial,
  addMaterial,
  updateMaterial,
  getCrkDetailById,
  updateMaterialCrk,
} from "@/api/system/material";
import { listTunnels } from "@/api/equipment/tunnel/api";
import { number } from "echarts";
import { tunnelNames } from "@/api/event/reservePlan";
var type = "";
var varid = "";
export default {
  name: "Material",
  dicts: ["environment", "direction"],
  data() {
    /* const isNum = (rule, value, callback) => {
            const price = /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/
            if (!price.test(value)) {
             callback(new Error(rule.message || '请输入正确的价格，例：10, 10.00'))
            }else{
              callback()
            }
          } */
    const isNumber = (rule, value, callback) => {
      const age = /^[1-9]\d?$|^1[01]\d$|^120$/;
      if (!age.test(value)) {
        callback(new Error("保质期只能为1-120之间的整数"));
      } else {
        callback();
      }
    };
    const validatePrice = (rule, value, callback) => {
      let reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{2})$/;
      if (!value) {
        callback(new Error("单价不能为空"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入正确格式的单价"));
        this.$set(this.form, "price", "");
      } else if (value.length > 10) {
        callback(new Error("最多可输入10个字符"));
        this.$set(this.form, "price", "");
      } else {
        callback();
      }
    };

    readonly: true;
    return {
      //不能选择当前日期
      pickerOptions0: {
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6;
        },
      },
      paramsData: {
        tunnelId: "",
      },
      statusClass: {
        1: "yellowClass",
        2: "greenClass",
        3: "redClass",
      },
      disabled: false,
      // 遮罩层
      wz_boxShow: false,
      loading: true,
      dloading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      list: [],
      mark: false,
      stock: "",
      varid: "",
      a: "",
      changeStock: "",
      // 总条数
      total: 0,
      // 应急资源表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openCrkForm: false,
      open2: false,
      // 物品类型字典
      materialTypeOptions: [],
      //出入库字典
      typeOptions: [
        /* {
                 value: '1',
                 label: '入库'
               }, {
                 value: '2',
                 label: '出库'
               }, */
      ],
      // 状态字典
      stateOptions: [],
      flag: null,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialId: null,
        materialName: null,
        materialType: null,
        inventoryQuantity: null,
        company: null,
        position: null,
        warningValue: null,
        code: null,
        state: null,
        price: null,
        station: null,
        deviation: null,
        endStation: null,
        endDeviation: null,
      },
      // 查询参数桩号验证
      // queryParamsRules: {
      //   station: {
      //     type: "number",
      //     required: true,
      //     pattern: /^(?:[0-9]{1,3})$/,
      //     message: "桩号为0~999之间的数字",
      //     trigger: "blur",
      //   },
      //   deviation: {
      //     type: "number",
      //     required: true,
      //     pattern: /^(?:[0-9]{1,3})$/,
      //     message: "桩号偏差为0~999之间数字",
      //     trigger: "blur",
      //   },
      //   endStation: {
      //     type: "number",
      //     required: true,
      //     pattern: /^(?:[0-9]{1,3})$/,
      //     message: "桩号为0~999之间的数字",
      //     trigger: "blur",
      //   },
      //   endDeviation: {
      //     type: "number",
      //     required: true,
      //     pattern: /^(?:[0-9]{1,3})$/,
      //     message: "桩号偏差为0~999之间数字",
      //     trigger: "blur",
      //   },
      // },
      // 表单参数
      form: {
        /* type: null,
        id: null,
        inventoryQuantity: null */
      },
      formDetail: {},
      crkFormRecord: {},
      crkFormDetail: {},
      // 表单校验
      rules: {
        materialName: [
          { required: true, message: "物资名称不能为空", trigger: "blur" },
          {
            min: 1,
            max: 30,
            message: "长度在1 ~ 30 个字符之间",
            trigger: "blur",
          },
        ],
        materialType: {
          required: true,
          message: "物资类型不能为空",
          trigger: "change",
        },
        code: {
          required: false,
          min: 0,
          max: 30,
          message: "长度在0~30个字符之间",
          // pattern: /^[0-9]{1}([0-9]|[.])*$/,
          trigger: "blur",
        },
        tunnelId: {
          required: true,
          message: "隧道名称不能为空",
          trigger: "change",
        },
        /*station: [
          {
            required: true,
            message: "桩号格式为K、YK、ZKxxx+xxx组成",
            trigger: "blur",
          },
          {
            pattern: /^(K|YK|ZK)[0-9+]{7}$/,
            message: "桩号格式为K、YK、ZKxxx+xxx组成",
            trigger: "blur",
          },
        ],*/
        direction: {
          required: true,
          message: "隧道方向不能为空",
          trigger: "change",
        },
        number: [
          { required: true, message: "数量不能为空", trigger: "blur" },
          {
            pattern: /^[1-9]\d*$/,
            message: "大小在 1 到 10 0000 之间的数字",
            trigger: "blur",
          },
        ],
        qualityGuaranteePeriod: [
          { required: true, message: "保质期不能为空", trigger: "blur" },
          { validator: isNumber, type: "number", trigger: "blur" },
        ],
        dateOfManufacture: {
          required: true,
          message: "生产日期不能为空",
          trigger: "blur",
        },
        // price: [{
        //   required: true,
        //   trigger: 'blur',
        //   validator: validatePrice
        // }],
      },
      drawer: false,
      direction: "rtl",
      flag: 1,
      // 隧道名称字典
      tunnelData: [],
      // 隧道方向字典
      directionData: [],
      // 新增修改弹窗确认按钮loading
      submitBtnLoading: false,
    };
  },

  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_material_type").then((response) => {
      this.materialTypeOptions = response.data;
    });
    this.getDicts("sd_material").then((response) => {
      this.stateOptions = response.data;
    });

    this.getDicts("sd_material_warehousing").then((response) => {
      this.typeOptions = response.data;
    });
    this.getDicts("sd_direction").then((response) => {
      this.directionData = response.data;
    });
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (this.$refs.main && !this.$refs.main.contains(e.target)) {
        if (self.wz_boxShow == true) {
          self.wz_boxShow = false;
        }
      }
    },

    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    focus() {
      this.$nextTick(() => {
        document
          .getElementsByClassName("el-button--text")[1]
          .setAttribute("style", "display:none"); // 隐藏此刻按钮
      });
    },

    openCrkDrawer(row) {
      this.drawer = true;
      this.crkFormDetail.materialId = row.id; //通过物资id去查询出入库详情信息
      getCrkDetailById(this.crkFormDetail).then((response) => {
        if (response.code == 200) {
          this.list = response.data;
        }
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    /** 查询应急资源列表 */
    getList() {
      this.loading = true;
      var obj = this.queryParams;
      var params = {
        pageNum: obj.pageNum,
        pageSize: obj.pageSize,
        materialType: obj.materialType,
        materialName: obj.materialName,
      };
      // 有开始桩号
      /*if (obj.station) {
        if (!obj.endStation) {
          this.loading = false;
          return this.$modal.msgWarning(
            "桩号查询必须同时有'开始桩号'和'结束桩号'"
          );
        }
      }*/
      // 有结束桩号
      /*if (obj.endStation) {
        if (!obj.station) {
          this.loading = false;
          return this.$modal.msgWarning(
            "桩号查询必须同时有'开始桩号'和'结束桩号'"
          );
        }
      }*/
      // 开始桩号 和 结束桩号 都有
      /*if (obj.station && obj.endStation) {
        if (obj.endStation < obj.station) {
          return this.$modal.msgWarning("'结束桩号'要大于'开始桩号'");
        }
        obj.deviation == undefined ||
        obj.deviation == null ||
        obj.deviation == ""
          ? (obj.deviation = 0)
          : "";
        obj.endDeviation == undefined ||
        obj.endDeviation == null ||
        obj.endDeviation == ""
          ? (obj.endDeviation = 0)
          : "";
        if (obj.endStation == obj.station) {
          if (obj.endDeviation <= obj.deviation) {
            this.loading = false;
            return this.$modal.msgWarning("'结束桩号'要大于'开始桩号'");
          }
        }
        params.station = "K" + "." + obj.station + "." + obj.deviation;
        params.endStation = "K" + "." + obj.endStation + "." + obj.endDeviation;
      }*/
      if (this.$cache.local.get("manageStation") == "1") {
        params.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listMaterial(params).then((response) => {
        this.materialList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询隧道下拉框 */
    getTunnels() {
      if (this.$cache.local.get("manageStation") == "1") {
        this.paramsData.tunnelId = this.$cache.local.get("manageStationSelect");
      }
      listTunnels(this.paramsData).then((response) => {
        this.tunnelData = response.rows;
      });
    },

    // 物品类型字典翻译
    materialTypeFormat(row, column) {
      return this.selectDictLabel(this.materialTypeOptions, row.materialType);
    },
    // 隧道方向字典翻译
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionData, row.direction);
    },
    // 状态字典翻译
    stateFormat(row, column) {
      return this.selectDictLabel(this.stateOptions, row.state);
    },
    // 出入库字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.openCrkForm = false;
      this.open2 = false;
      this.reset();
    },
    resetMaterial() {
      this.$refs.crkFormDetail.resetFields();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        materialId: undefined,
        materialName: undefined,
        materialType: undefined,
        inventoryQuantity: undefined,
        company: undefined,
        position: undefined,
        warningValue: undefined,
        code: undefined,
        remark: undefined,
        state: undefined,
        createTime: undefined,
        updateTime: undefined,
        price: undefined,
      };
      this.resetForm("form");
      this.crkFormRecord = {
        stock: undefined,
        remark: undefined,
      };
      this.resetForm("crkFormRecord");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.materialList = [];

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.materialName = "";
      this.$refs.queryForm.resetFields();
      this.queryForm = {
        pageNum: 1,
        pageSize: 10,
        materialId: null,
        materialName: null,
        materialType: null,
        inventoryQuantity: null,
        company: null,
        position: null,
        warningValue: null,
        code: null,
        state: null,
        price: null,
        station: null,
        deviation: null,
        endStation: null,
        endDeviation: null,
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length; //非多个禁用
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加应急资源";
    },

    /** 修改按钮操作 */
    handleUpdateMaterial(row) {
      this.reset();
      const id = row.id || this.ids;
      getMaterial(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应急资源";
      });
    },

    /** 应急资源提交按钮 */
    submitForm() {
      this.disabled = true;
      // if(this.submitBtnLoading) return
      // this.submitBtnLoading = true
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            await updateMaterial(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            await addMaterial(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
        this.disabled = false;
        // this.submitBtnLoading = false
      });
    },

    /** 出入库按钮操作 */
    handleUpdate(index) {
      let rkid = this.ids;
      this.flag = index;
      this.rules.materialName = {};
      this.rules.company = {};
      this.rules.position = {};
      this.rules.price = {};
      this.reset();
      getMaterial(rkid).then((response) => {
        this.formDetail = response.data;
        this.openCrkForm = true;
        if (this.flag == 1) {
          this.title = "物资入库";
        } else if (this.flag == 2) {
          this.title = "物资出库";
        }
      });
    },
    /** 出入库提交按钮 */
    submitCrkFormRecord: function () {
      this.crkFormRecord.materialId = this.formDetail.id;
      this.crkFormRecord.type = this.flag;
      this.$refs["crkFormRecord"].validate((valid) => {
        if (valid) {
          if (this.flag == 1) {
            if (this.crkFormRecord.changeStock == 0) {
              this.$modal.msgError("请重新输入入库数量");
              return;
            }
            updateMaterialCrk(this.crkFormRecord).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("入库成功");
                this.openCrkForm = false;
                this.getList();
              }
            });
          }
          if (this.flag == 2) {
            if (
              this.crkFormRecord.changeStock > this.formDetail.inventoryQuantity
            ) {
              this.$modal.msgError(
                "当前库存不足，无法完成库存操作，请重新选择出库数量"
              );
              return;
            }
            updateMaterialCrk(this.crkFormRecord).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("出库成功");
                this.openCrkForm = false;
                this.getList();
              }
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delMaterial(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/material/export",
        {
          ...this.queryParams,
        },
        `system_material.xlsx`
      );
    },
    //关闭drawer
    materialFormClose() {
      this.resetMaterial();
      this.drawer = false;
    },
    // 表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    // // 查询参数-桩号-获取焦点
    // queryStationFocus(value, index) {
    //   this.queryStation.title = index
    //   if(!value) {
    //     this.clearQueryStation()
    //     this.queryStation.title = index
    //   } else {
    //     if(index == 1) {
    //       var arr = this.endStation.split('.')
    //       this.queryStation.endDirection = arr[0]
    //       this.queryStation.endLocation = arr[1]
    //       this.queryStation.endDeviation = arr[2]
    //     } else {
    //       var arr = this.station.split('.')
    //       this.queryStation.direction = arr[0]
    //       this.queryStation.location = arr[1]
    //       this.queryStation.deviation = arr[2]
    //     }
    //   }
    //   this.queryStationVisible = true
    // },
    // // 清空查询参数-桩号
    // clearQueryStation() {
    //   this.queryStation = {
    //     title: 0,
    //     direction: 'K',
    //     location: 0,
    //     deviation: 0,
    //     endDirection: 'K',
    //     endLocation: 0,
    //     endDeviation: 0,
    //   }
    // },
    // // 提交桩号
    // submitQueryStation() {
    //   this.$refs["queryStationForm"].validate(valid => {
    //     if(valid) {
    //       if(this.queryStation.title == 1) {
    //         this.queryParams.endStation = this.queryStation.endDirection + this.queryStation.endLocation + '+' + this.queryStation.endDeviation
    //         this.endStation = this.queryStation.endDirection + '.' + this.queryStation.endLocation + '.' + this.queryStation.endDeviation
    //       } else {
    //         this.queryParams.station = this.queryStation.direction + this.queryStation.location + '+' + this.queryStation.deviation
    //         this.station = this.queryStation.direction + '.' + this.queryStation.location + '.' + this.queryStation.deviation
    //       }
    //       this.cancelQueryStation()
    //     }
    //   })
    // },
    // // 取消桩号输入
    // cancelQueryStation() {
    //   this.queryStationVisible = false
    //   this.$refs.queryStationForm.resetFields()
    // }
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
      this.getTunnels();
    },
  },
};
</script>
<style scoped>
.btn {
  text-align: center;
  margin-bottom: 20px;
}

h3 {
  text-decoration: underline;
  color: red;
}

.el-input {
  position: relative;
  /* font-size: 14px; */
  /*display: inline;*/
  width: 100%;
}

.material-msg {
  background-color: #1890ff;
  border-color: #1890ff;

  .el-message__content {
    color: #ffffff;
  }
}

.el-drawer__header {
  background: #dcdfe6;
  padding: 0 10px;
  height: 58px;
}

.el-drawer__body {
  overflow: auto;
  /* overflow-x: auto; */
}

.greenClass {
  color: #00aa00;
}

.yellowClass {
  color: #0055ff;
}

.redClass {
  color: #ff0000;
}

.handlerClass {
  margin-left: -115px;
  font-weight: bold;
}

.kcczMessage {
  margin-left: -73px;
}

.kcMessage {
  margin-left: -134px;
}

.remarkClass {
  margin-left: -125px;
}

.el-timeline-item__timestamp.is-bottom {
  margin-top: 8px;
}
/* .el-form--inline .el-form-item {
    display: inline-block;
    margin-right: -7px;
    vertical-align: top;
} */

.dateClass {
  width: 100%;
  display: inline-table;
  vertical-align: middle;
}
</style>

<style>
.wz_searchBox {
  position: absolute;
  top: 8%;
  right: 1%;
  width: 24%;
  z-index: 1996;
  background-color: #00335a;
  padding: 20px;
  box-sizing: border-box;
}
</style>
<style lang="scss" scoped>
.wz_searchBox {
  ::v-deep .el-form-item__content {
    width: 80%;
    .el-select {
      width: 100%;
    }
  }
  .bottomBox {
    .el-form-item__content {
      display: flex;
      justify-content: center;
      align-items: flex-end;
    }
  }
}
.bottomBox {
  width: 100%;
  ::v-deep .el-form-item__content {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
  }
}
</style>
