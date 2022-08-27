<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车辆类型" prop="vehicleType">
        <el-select v-model="queryParams.vehicleType" placeholder="请选择车辆类型" clearable size="small">
          <el-option v-for="dict in specialVehicleTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="状态" prop="status">-->
<!--        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">-->
<!--          <el-option-->
<!--            v-for="item in statusOptions"-->
<!--            :key="item.value"-->
<!--            :label="item.label"-->
<!--            :value="item.value"/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="通行时间" prop="status">
        <el-date-picker
          v-model="times"
          type="datetimerange"
          range-separator="至"
          value-format="yyyy-MM-dd HH:mm:ss"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
<!--          v-hasPermi="['business:trafficManagement:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['business:trafficManagement:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['business:trafficManagement:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['business:trafficManagement:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="trafficTable" v-loading="loading" :data="trafficManagementList" :default-sort = "{prop: 'startTime', order: 'descending'}" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
      <el-table-column label="车辆类型" align="center" prop="vehicleType" :formatter="vehicleTypeFormat" />
<!--      <el-table-column label="检测时间" align="center" prop="startTime" width="180" sortable>-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="检测时间" align="center" prop="createTime" />
      <el-table-column label="检测位置" align="center" prop="position" />
<!--      <el-table-column label="照片" align="center" prop="photo" />-->

      <el-table-column label="照片" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.photo" style="width:50px;" @click="showImage(scope.row.photo)">
        </template>
      </el-table-column>

      <el-table-column label="检测源" align="center" prop="deviceSource" />
<!--      <el-table-column label="查看记录" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="testPic(scope.row)"-->
<!--            v-hasPermi="['business:trafficManagement:assign']"-->
<!--          >点击查看</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <!-- <el-table-column label="结束时间" align="center" prop="endTime" width="180" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column> -->
<!--      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat"/>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="assign(scope.row)"-->
<!--            v-hasPermi="['business:trafficManagement:assign']"-->
<!--            v-if="scope.row.status === 2"-->
<!--          >下发</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['business:trafficManagement:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :visible.sync="imageDialog.imageVisible" custom-class="imageBox" width="35%">
      <img :src="imageDialog.path">
    </el-dialog>

    <!-- 添加或修改特种车辆通行管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车辆类型" prop="vehicleType">
          <el-select v-model="form.vehicleType" placeholder="请选择车辆类型">
            <el-option v-for="dict in specialVehicleTypeList" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
<!--        <el-form-item label="检测时间" prop="startTime">-->
<!--          <el-date-picker clearable size="small"-->
<!--                          v-model="form.startTime"-->
<!--                          type="date"-->
<!--                          value-format="yyyy-MM-dd"-->
<!--                          placeholder="选择开始时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="结束时间" prop="endTime">-->
<!--          <el-date-picker clearable size="small"-->
<!--                          v-model="form.endTime"-->
<!--                          type="date"-->
<!--                          value-format="yyyy-MM-dd"-->
<!--                          placeholder="选择结束时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 下发 -->
    <select-sys-user :visible="selectSysUserVisible" @close="selectSysUserClose" @confirm="selectSysUserConfirm" :title="'下发通知'"></select-sys-user>
  </div>
</template>

<script>
  import { listTrafficManagement, getTrafficManagement, delTrafficManagement, addTrafficManagement, updateTrafficManagement, exportTrafficManagement, assignTrafficManagement } from "@/api/business/trafficManagement";
  import selectSysUser from '@/components/selectSysUser'
  export default {
    name: "TrafficManagement",
    components: {
      selectSysUser
    },
    data() {
      return {
        imageDialog:{
          imageTitle:'',
          imageVisible:false,
          path:'',//展示图片地址
        },
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
        // 特种车辆通行管理表格数据
        trafficManagementList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 选择用户
        selectSysUserVisible: false,
        // 当前选中数据
        currentRow: '',
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          vehicleType: null,
          startTime: null,
          endTime: null,
        },
        times: '',
        // 状态选项
        statusOptions: [
          {label: '未下发', value: 1},
          {label: '已下发', value: 2}
        ],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          vehicleType: [
            { required: true, message: "车辆类型不能为空", trigger: "change" }
          ],
          startTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" }
          ],
          endTime: [
            { required: true, message: "结束时间不能为空", trigger: "blur" }
          ],
          createTime: [
            { required: true, message: "创建时间不能为空", trigger: "blur" }
          ]
        },
        // 特种车辆类型字典
        specialVehicleTypeList: [],
        title: '',
      };
    },
    created() {
      this.getList();
      this.getDicts("sd_specialVehicle_type").then((response) => {
        console.log(response.data,"response.data")
        this.specialVehicleTypeList = response.data;
      });
    },
    methods: {
      selectSysUserConfirm(val) {
        assignTrafficManagement({
          id: this.currentRow.id,
          userIds: val
        }).then(res=>{
          this.$message.success('下发成功')
          this.getList()
        })
      },
      showImage(path){
        console.log(path);
        this.imageDialog.imageTitle = '照片'
        this.imageDialog.path = path
        this.imageDialog.imageVisible = true
      },
      // 点击查看检测图片
      testPic(row){

      },
      /** 关闭用户选择 **/
      selectSysUserClose() {
        this.selectSysUserVisible = false
      },
      /** 下达 **/
      assign(row) {
        this.currentRow = row
        this.selectSysUserVisible = true
      },
      /** 状态字段格式化 **/
      statusFormat(row, column) {
        if(row.status === 2) {
          return '未下发'
        } else {
          return '已下发'
        }
      },
      /** 查询特种车辆通行管理列表 */
      getList() {
        this.loading = true;
        if(this.times && this.times.length > 0) {
          this.queryParams.startTime = this.times[0]
          this.queryParams.endTime = this.times[1]
        } else {
          this.queryParams.startTime = ''
          this.queryParams.endTime = ''
        }
        listTrafficManagement(this.queryParams).then(response => {
          this.trafficManagementList = response.rows;
          console.log(response.rows,"response.rows")
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
        this.times = ''
        this.form = {
          id: null,
          vehicleType: null,
          startTime: null,
          endTime: null,
          status: null,
          createTime: null
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
        this.times = ''
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      // 点击行切换选中状态
      handleRowClick(row) {
        this.$refs.trafficTable.toggleRowSelection(row)
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加特种车辆通行管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getTrafficManagement(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改特种车辆通行管理";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateTrafficManagement(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addTrafficManagement(this.form).then(response => {
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
        this.$confirm('是否确认删除数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTrafficManagement(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTrafficManagement(queryParams);
        }).then(response => {
          this.$download.name(response.msg);
        })
      },
      // 特种车辆类型字典翻译
      vehicleTypeFormat(row) {
        return this.selectDictLabel(this.specialVehicleTypeList, row.vehicleType);
      }
    }
  };
</script>
