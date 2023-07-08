<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
      <el-form-item label="设备编号" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属隧道" prop="tunnel">
        <el-select
          v-model="queryParams.tunnel"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in rescueTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="降水形态" prop="precipitationPattern">
        <el-input
          v-model="queryParams.precipitationPattern"
          placeholder="请输入降水形态"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->

      <el-form-item label="路面状态" prop="pavementCondition">
       <!-- <el-input
          v-model="queryParams.pavementCondition"
          placeholder="请输入路面状态"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select
          v-model="queryParams.pavementCondition"
          placeholder="请选择路面情况"
          size="small"
          style="width: 220px;float:left;"
        >
          <el-option
            v-for="dict in dict.type.sd_road_condition"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['intelligent:report:add']"-->
<!--          >新增</el-button-->
<!--        >-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['intelligent:report:edit']"-->
<!--          >修改</el-button-->
<!--        >-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['intelligent:report:remove']"-->
<!--          >删除</el-button-->
<!--        >-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['intelligent:report:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="reportList"
      @selection-change="handleSelectionChange"
    >
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="设备编号" align="center" prop="deviceId" />
      <el-table-column label="风的方向" align="center" prop="windDirection" />
      <el-table-column label="风的速度" align="center" prop="windSpeed" />
      <!-- <el-table-column label="空气温度" align="center" prop="airTemperature" />
      <el-table-column label="室内湿度" align="center" prop="indoorHumidity" />
      <el-table-column label="气压" align="center" prop="pressure" />
      <el-table-column label="降雨量" align="center" prop="rainfall" />
      <el-table-column
        label="降水形态"
        align="center"
        prop="precipitationPattern"
      />
      <el-table-column label="辐射" align="center" prop="radiation" />
      <el-table-column label="紫外" align="center" prop="ultraviolet" /> -->
      <el-table-column
        label="1分钟能见度(m)"
        align="center"
        prop="oneMinuteVisibility"
      />
      <el-table-column
        label="10分钟能见度(m)"
        align="center"
        prop="tenMinuteVisibility"
      />
      <!-- <el-table-column
        label="水膜厚度"
        align="center"
        prop="waterFilmThickness"
      />
      <el-table-column label="冰的厚度" align="center" prop="iceThickness" />
      <el-table-column label="雪的厚度" align="center" prop="snowThickness" />
      <el-table-column label="湿滑系数" align="center" prop="wetSliding" />
      <el-table-column
        label="路面温度"
        align="center"
        prop="pavementTemperature"
      /> -->
      <el-table-column
        label="路面状态"
        align="center"
        prop="pavementCondition"
      />
      <el-table-column
        label="遥感检测仪工作状态"
        align="center"
        prop="remoteSenseStatus"
      />
      <el-table-column
        label="能见度仪工作状态"
        align="center"
        prop="visibilityStatus"
      />
      <el-table-column
        label="采集时间"
        align="center"
        prop="createTime"
      />
<!--      <el-table-column-->
<!--        label="操作"-->
<!--        align="center"-->
<!--        class-name="small-padding fixed-width"-->
<!--      >-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['intelligent:report:edit']"-->
<!--            >修改</el-button-->
<!--          >-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['intelligent:report:remove']"-->
<!--            >删除</el-button-->
<!--          >-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改气象采集器信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="设备编号" prop="deviceId">
              <el-input v-model="form.deviceId" placeholder="请输入设备编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属隧道" prop="tunnel">
              <el-select
                v-model="form.tunnel"
                placeholder="请选择隧道"
                clearable
              >
                <el-option
                  v-for="item in rescueTunnelData"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="风的方向" prop="windDirection">
              <el-input
                v-model="form.windDirection"
                placeholder="请输入风的方向"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="风的速度" prop="windSpeed">
              <el-input
                v-model="form.windSpeed"
                placeholder="请输入风的速度"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="空气温度" prop="airTemperature">
              <el-input
                v-model="form.airTemperature"
                placeholder="请输入空气温度"
              /> </el-form-item
          ></el-col>
          <el-col :span="8">
            <el-form-item label="室内湿度" prop="indoorHumidity">
              <el-input
                v-model="form.indoorHumidity"
                placeholder="请输入室内湿度"
              /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"
            ><el-form-item label="气压" prop="pressure">
              <el-input
                v-model="form.pressure"
                placeholder="请输入气压"
              /> </el-form-item
          ></el-col>
          <el-col :span="8">
            <el-form-item label="降雨量" prop="rainfall">
              <el-input
                v-model="form.rainfall"
                placeholder="请输入降雨量"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="降水形态" prop="precipitationPattern">
              <el-input
                v-model="form.precipitationPattern"
                placeholder="请输入降水形态"
              /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="辐射" prop="radiation">
              <el-input
                v-model="form.radiation"
                placeholder="请输入辐射"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="紫外" prop="ultraviolet">
              <el-input
                v-model="form.ultraviolet"
                placeholder="请输入紫外"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="1分钟能见度" prop="oneMinuteVisibility">
              <el-input
                v-model="form.oneMinuteVisibility"
                placeholder="请输入1分钟能见度"
                style="width:80%;"
              /> m </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"
            ><el-form-item label="10分钟能见度" prop="tenMinuteVisibility">
              <el-input
                v-model="form.tenMinuteVisibility"
                placeholder="请输入10分钟能见度"
                style="width:80%;"
              /> m </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="水膜厚度" prop="waterFilmThickness">
              <el-input
                v-model="form.waterFilmThickness"
                placeholder="请输入水膜厚度"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="冰的厚度" prop="iceThickness">
              <el-input
                v-model="form.iceThickness"
                placeholder="请输入冰的厚度"
              /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"
            ><el-form-item label="雪的厚度" prop="snowThickness">
              <el-input
                v-model="form.snowThickness"
                placeholder="请输入雪的厚度"
              /> </el-form-item
          ></el-col>
          <el-col :span="8"
            ><el-form-item label="湿滑系数" prop="wetSliding">
              <el-input
                v-model="form.wetSliding"
                placeholder="请输入湿滑系数"
              /> </el-form-item
          ></el-col>
          <el-col :span="8">
            <el-form-item label="路面温度" prop="pavementTemperature">
              <el-input
                v-model="form.pavementTemperature"
                placeholder="请输入路面温度"
              /> </el-form-item
          ></el-col>
        </el-row>

        <el-form-item label="路面状态" prop="pavementCondition">
          <el-input
            v-model="form.pavementCondition"
            placeholder="请输入路面状态"
          />
        </el-form-item>
        <el-row>
          <el-col :span="8"
            ><el-form-item label="遥感检测仪" prop="remoteSenseStatus">
              <el-select
                v-model="form.remoteSenseStatus"
                placeholder="工作状态"
                size="small"
              >
                <el-option label="正常" value="0" />
                <el-option label="异常" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8"
            ><el-form-item label="能见度仪" prop="visibilityStatus">
              <el-select
                v-model="form.visibilityStatus"
                placeholder="工作状态"
                size="small"
              >
                <el-option label="正常" value="0" />
                <el-option label="异常" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="采集时间" prop="createTime">
          <el-date-picker
            v-model="form.createTime"
            type="datetime"
            placeholder="选择采集时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="addBtnLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listReport,
  getReport,
  delReport,
  addReport,
  updateReport,
  exportReport,
} from "@/api/intelligent/report";
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  name: "Report",
  dicts: ["sd_road_condition"],
  components: {},
  data() {
    return {
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
      // 气象采集器信息表格数据
      reportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        tunnel: null,
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        windDirection: null,
        windSpeed: null,
        airTemperature: null,
        indoorHumidity: null,
        pressure: null,
        rainfall: null,
        precipitationPattern: null,
        radiation: null,
        ultraviolet: null,
        oneMinuteVisibility: null,
        tenMinuteVisibility: null,
        waterFilmThickness: null,
        iceThickness: null,
        snowThickness: null,
        wetSliding: null,
        pavementTemperature: null,
        pavementCondition: null,
        remoteSenseStatus: null,
        visibilityStatus: null,
        isPush: null,
        rawData: null,
      },
      /* 隧道 */
      rescueTunnelData: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceId: [
          { required: true, message: '设备编号不能为空', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在1~30个字符', trigger: 'blur' },
        ],
        tunnel: [
          { required: true, message: '请选择隧道', trigger: 'change' },
        ],
        windDirection: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        windSpeed: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        airTemperature: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        indoorHumidity: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        pressure: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        rainfall: [
          { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ],
        precipitationPattern: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        radiation: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        ultraviolet: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        oneMinuteVisibility: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        tenMinuteVisibility: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        waterFilmThickness: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        iceThickness: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        snowThickness: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        wetSliding: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        pavementTemperature: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
        pavementCondition: { required: false, min: 1, max: 30, message: '长度在0~30个字符', trigger: 'blur' },
      },
      addBtnLoading: false,
    };
  },
  created() {
    this.getTunnel();
    this.getList();
  },
  methods: {
    /* 所选隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.rescueTunnelData = response.rows;
      });
    },
    /** 查询气象采集器信息列表 */
    getList() {
      this.loading = true;
      listReport(this.queryParams).then((response) => {
        this.reportList = response.rows;
        this.reportList.forEach((e) => {
          if (e.remoteSenseStatus == 0) {
            e.remoteSenseStatus = "正常";
          } else if (e.remoteSenseStatus == 1) {
            e.remoteSenseStatus = "异常";
          }
          if (e.visibilityStatus == 0) {
            e.visibilityStatus = "正常";
          } else if (e.visibilityStatus == 1) {
            e.visibilityStatus = "异常";
          }
          if (e.isPush == 0) {
            e.isPush = "未推送";
          } else if (e.isPush == 1) {
            e.isPush = "已推送";
          }
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        reportId: null,
        deviceId: null,
        windDirection: null,
        windSpeed: null,
        airTemperature: null,
        indoorHumidity: null,
        pressure: null,
        rainfall: null,
        precipitationPattern: null,
        radiation: null,
        ultraviolet: null,
        oneMinuteVisibility: null,
        tenMinuteVisibility: null,
        waterFilmThickness: null,
        iceThickness: null,
        snowThickness: null,
        wetSliding: null,
        pavementTemperature: null,
        pavementCondition: null,
        remoteSenseStatus: "0",
        visibilityStatus: "0",
        isPush: null,
        rawData: null,
        createBy: null,
        createTime: null,
        remark: null,
        acquisitionTime: null,
      };
      this.resetForm("form");
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
      this.ids = selection.map((item) => item.reportId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加气象采集器信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const reportId = row.reportId || this.ids;
      getReport(reportId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改气象采集器信息";
      });
    },
    /** 提交按钮 */
    async submitForm() {
      this.addBtnLoading = true
      await this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.reportId != null) {
            updateReport(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReport(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
      this.addBtnLoading = false
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const reportIds = row.reportId || this.ids;
      this.$confirm(
        '是否确认删除选中数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delReport(reportIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出气象采集器信息数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportReport(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
        });
    },
  },
};
</script>
