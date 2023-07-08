<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="开始时间" prop="startTime">
        <!-- <el-date-picker clearable size="small"
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择开始时间">
        </el-date-picker> -->
		<el-date-picker
		  v-model="statisticalTime"
		  type="daterange"
		  size="small"
		  style="width: 240px"
		  value-format="yyyy-MM-dd"
		  range-separator="至"
		  start-placeholder="开始日期"
		  end-placeholder="结束日期">
		</el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="informationList" @selection-change="handleSelectionChange">
	  <el-table-column label="所属隧道" align="center" prop="expressway" />
      <el-table-column label="分类" align="center" prop="classification" />
      <el-table-column label="原因" align="center" prop="reason" />
      <el-table-column label="位置" align="center" prop="place" />
      <el-table-column label="方向" align="center" prop="direction" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否有原因变化" align="center" prop="causeChange" />
      <el-table-column label="是否有地点变化" align="center" prop="locationChange" />
  <!--    <el-table-column label="最终分类" align="center" prop="finalClassification" /> -->
  <!--    <el-table-column label="第二次分类" align="center" prop="secondClassification" />
      <el-table-column label="第三次分类" align="center" prop="thirdClassification" /> -->
      <el-table-column label="最小桩号" align="center" prop="minimumStation" />
      <el-table-column label="最大桩号" align="center" prop="maximumStation" />
      <el-table-column label="最大缓行距离(单位:米)" align="center" prop="maximumSlowTravelDistance" />
      <el-table-column label="管制地点" align="center" prop="regulatoryPlace" />
      <el-table-column label="管制原因" align="center" prop="regulatoryReasons" />
      <el-table-column label="特殊地点描述" align="center" prop="specialLocationDescription" />
     <!-- <el-table-column label="备注" align="center" prop="remake" /> -->
      <el-table-column label="结束方式" align="center" prop="endMode" />
<!--      <el-table-column label="备注1" align="center" prop="remake1" />
      <el-table-column label="备注2" align="center" prop="remake2" /> -->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['trafficResistanceInformation:information:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['trafficResistanceInformation:information:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改交通通阻信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门" prop="company">
          <el-input v-model="form.company" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="分类" prop="classification">
          <el-input v-model="form.classification" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="原因" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入原因" />
        </el-form-item>
        <el-form-item label="高速" prop="expressway">
          <el-input v-model="form.expressway" placeholder="请输入高速" />
        </el-form-item>
        <el-form-item label="地点" prop="place">
          <el-input v-model="form.place" placeholder="请输入地点" />
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-input v-model="form.direction" placeholder="请输入方向" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable size="small"
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable size="small"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否有原因变化" prop="causeChange">
          <el-input v-model="form.causeChange" placeholder="请输入是否有原因变化" />
        </el-form-item>
        <el-form-item label="是否有地点变化" prop="locationChange">
          <el-input v-model="form.locationChange" placeholder="请输入是否有地点变化" />
        </el-form-item>
        <el-form-item label="最终分类" prop="finalClassification">
          <el-input v-model="form.finalClassification" placeholder="请输入最终分类" />
        </el-form-item>
        <el-form-item label="第二次分类" prop="secondClassification">
          <el-input v-model="form.secondClassification" placeholder="请输入第二次分类" />
        </el-form-item>
        <el-form-item label="第三次分类" prop="thirdClassification">
          <el-input v-model="form.thirdClassification" placeholder="请输入第三次分类" />
        </el-form-item>
        <el-form-item label="最小桩号" prop="minimumStation">
          <el-input v-model="form.minimumStation" placeholder="请输入最小桩号" />
        </el-form-item>
        <el-form-item label="最大桩号" prop="maximumStation">
          <el-input v-model="form.maximumStation" placeholder="请输入最大桩号" />
        </el-form-item>
        <el-form-item label="最大缓行距离(单位:米)" prop="maximumSlowTravelDistance">
          <el-input v-model="form.maximumSlowTravelDistance" placeholder="请输入最大缓行距离(单位:米)" />
        </el-form-item>
        <el-form-item label="管制地点" prop="regulatoryPlace">
          <el-input v-model="form.regulatoryPlace" placeholder="请输入管制地点" />
        </el-form-item>
        <el-form-item label="管制原因" prop="regulatoryReasons">
          <el-input v-model="form.regulatoryReasons" placeholder="请输入管制原因" />
        </el-form-item>
        <el-form-item label="特殊地点描述" prop="specialLocationDescription">
          <el-input v-model="form.specialLocationDescription" placeholder="请输入特殊地点描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remake">
          <el-input v-model="form.remake" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="结束方式" prop="endMode">
          <el-input v-model="form.endMode" placeholder="请输入结束方式" />
        </el-form-item>
        <el-form-item label="备注1" prop="remake1">
          <el-input v-model="form.remake1" placeholder="请输入备注1" />
        </el-form-item>
        <el-form-item label="备注2" prop="remake2">
          <el-input v-model="form.remake2" placeholder="请输入备注2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInformation, getInformation, delInformation, addInformation, updateInformation, exportInformation } from "@/api/trafficOperationControl/trafficOperationStatus/trafficResistanceInformation/information/information.js";

export default {
  name: "Information",
  data() {
    return {
      // 遮罩层
      loading: true,
	  statisticalTime:[],
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
      // 交通通阻信息表格数据
      informationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        company: null,
        startTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询交通通阻信息列表 */
    getList() {
      this.loading = true;
	  this.queryParams.params = {}
	  if (null != this.statisticalTime && "" != this.statisticalTime) {
	    this.queryParams.params.startTime = this.statisticalTime[0] + " 00:00:00";
	    this.queryParams.params.endTime = this.statisticalTime[1] + " 23:59:59";
	  }
      listInformation(this.queryParams).then(response => {
        this.informationList = response.rows;
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
        id: null,
        company: null,
        classification: null,
        reason: null,
        expressway: null,
        place: null,
        direction: null,
        startTime: null,
        endTime: null,
        causeChange: null,
        locationChange: null,
        finalClassification: null,
        secondClassification: null,
        thirdClassification: null,
        minimumStation: null,
        maximumStation: null,
        maximumSlowTravelDistance: null,
        regulatoryPlace: null,
        regulatoryReasons: null,
        specialLocationDescription: null,
        remake: null,
        endMode: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remake1: null,
        remake2: null
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
	  this.statisticalTime = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加交通通阻信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInformation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交通通阻信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInformation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInformation(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除交通通阻信息编号为"' + ids + '"的数据项？').then(function() {
        return delInformation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出交通通阻信息数据项？').then(() => {
        this.exportLoading = true;
        return exportInformation(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
