<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
	  <el-form-item label="所属隧道">
	    <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small" @change="getEquId">
	      <el-option
	        v-for="item in tunnelData"
	        :key="item.tunnelId"
	        :label="item.tunnelName"
	        :value="item.tunnelId"
	      />
	    </el-select>
	  </el-form-item>
      <el-form-item label="设备Id">
        <el-select v-model="queryParams.equipmentId" placeholder="请选择设备" clearable size="small">
          <el-option
            v-for="item in equipmentData"
            :key="item.eqId"
            :label="item.eqId"
            :value="item.eqId"
          />
        </el-select>
      </el-form-item>
     <el-form-item label="统计时间" prop="forecastTime">
       <el-date-picker
         v-model="forecastTime"
         size="small"
         style="width: 240px"
         value-format="yyyy-MM-dd"
         type="daterange"
         range-separator="-"
         unlink-panels
         start-placeholder="开始日期"
         end-placeholder="结束日期"
          :picker-options="pickerOptions"
       ></el-date-picker>
     </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="xfpipelineList" :default-sort = "{prop: 'createTime', order: 'descending'}" @selection-change="handleSelectionChange">
	    <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="设备Id" align="center" prop="equipmentId" />
      <el-table-column label="设备桩号" align="center" prop="position" />
      <el-table-column label="管道压力" align="center" prop="analogQuantity" >
          <template slot-scope="scope">
              <div>{{scope.row.analogQuantity}}Mp</div>
          </template>
      </el-table-column>
      <el-table-column label="压力上限" align="center" prop="highest" >
          <template slot-scope="scope">
              <div>{{scope.row.highest}}Mp</div>
          </template>
      </el-table-column>
      <el-table-column label="压力下限" align="center" prop="low" >
          <template slot-scope="scope">
              <div>{{scope.row.low}}Mp</div>
          </template>
      </el-table-column>
	  <el-table-column label="采集时间" align="center" prop="createTime" sortable />

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { record, listXfpipeline, getXfpipeline, delXfpipeline, addXfpipeline, updateXfpipeline, exportXfpipeline } from "@/api/xfpipeline/xfpipeline";
import { listTunnels } from "@/api/equipment/tunnel/api";
import {listDevices} from "@/api/equipment/eqlist/api";
import moment from "moment";
export default {
  name: "Xfpipeline",
  components: {
  },
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
      // 消防管道监测表格数据
      xfpipelineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        state: null,
        equipmentId:''
        },
      // 统计时间
      forecastTime:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 设备id 下拉框
      equipmentData:[],
      // 所属隧道
      tunnelData:{},
      // 状态列表
      stateData:[{
        "stateId":"online","stateName":"正常"
      },{
        "stateId":"offline","stateName":"离线"
      }],
      // 两天前的日期
      moment:'',
      pickerOptions:{
        onPick: (obj) => {
          this.pickerMinDate = new Date(obj.minDate).getTime();
        },
        disabledDate:(time)=> {
            if (this.pickerMinDate) {
                const day1 =  366 * 24 * 3600 * 1000
                let maxTime = this.pickerMinDate + day1
                let minTime = this.pickerMinDate - day1
                return time.getTime() > maxTime || time.getTime()<minTime || time.getTime() > Date.now()+0 * 24 * 3600 * 1000
            }else{
                return time.getTime() > Date.now()+0 * 24 * 3600 * 1000
            }

        },
      }
    };
  },
  created() {
	// 获取表格信息
    // this.getList()
    // 默认搜索前两天数据
    this.getTime()
	// 获取隧道列表
	this.getTunnel()
  },
  methods: {
      getTime(){
          this.forecastTime = []
          let times = moment(new Date()).format("YYYY-MM-DD")
          let yesTime = Date.parse(new Date())-172800000
          let yesTimes = moment(new Date(yesTime)).format("YYYY-MM-DD")

          this.queryParams.params = {};
          this.queryParams.params["beginChangeTime"] = yesTimes
          this.forecastTime.push(yesTimes)

          this.queryParams.params["endChangeTime"] = times
          this.forecastTime.push(times)

          this.getList()
      },
      // formatState(state){
      //   for(var i=0;i<this.stateData.length;i++){
      //     if(this.stateData[i].stateId == state){
      //       return this.stateData[i].stateName
      //     }
      //   }
      // },
      /** 所属隧道 */
      getTunnel() {
        listTunnels().then(response => {
          this.tunnelData = response.rows;
        });
      },
      /** 查询消防管道监测列表 */
      getList() {
        this.queryParams.pageNum = 1;
        this.loading = true;
        this.queryParams.params = {};
        if (null != this.forecastTime && "" != this.forecastTime) {
          if(this.forecastTime[0] == this.forecastTime[1]){
            this.queryParams.params["beginChangeTime"] = this.forecastTime[0] + " 00:00:00";
            this.queryParams.params["endChangeTime"] = this.forecastTime[1] + " 23:59:59";
          }else{
            this.queryParams.params["beginChangeTime"] = this.forecastTime[0] + " 00:00:00";
            this.queryParams.params["endChangeTime"] = this.forecastTime[1] + " 23:59:59";
          }

        }
        record(this.queryParams).then(response => {
          this.xfpipelineList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      // 选中隧道后 获取该隧道内的压力表id
      getEquId(data){
          this.equipmentData = []
          this.queryParams.equipmentId = ''
          const params = {
              eqType:111,
              eqDirection:0,
              eqTunnelId: data
          }
          listDevices(params).then((response) => {
            console.log(response,"responseId")
            this.equipmentData = response.rows
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
          equipmentId: null,
          tunnelId: null,
          pressure: null,
          state: null,
          pressureHeight: null,
          pressureLow: null,
          createTime: null,
          createBy: null,
          updateTime: null,
          updateBy: null
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
          this.queryParams= {
            pageNum: 1,
            pageSize: 10,
            tunnelId: null,
            state:null,
            equipmentId:null
          },
          this.equipmentData = []
          this.getTime()
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
        this.title = "添加消防管道监测";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getXfpipeline(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改消防管道监测";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateXfpipeline(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addXfpipeline(this.form).then(response => {
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
        this.$confirm('是否确认删除?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delXfpipeline(ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出消防管道监测数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return exportXfpipeline(queryParams);
          }).then(response => {
            this.download(response.msg);
          })
      }
  }
};
</script>
