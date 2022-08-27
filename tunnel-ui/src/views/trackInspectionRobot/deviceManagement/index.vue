<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="隧道名称" prop="inspectionTunnel">
        <el-select
          v-model="queryParams.inspectionTunnel"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="机器人名称" prop="eqId">
        <el-select
          v-model="queryParams.eqId"
          placeholder="请选择机器人"
          clearable
          size="small"
          >
            <el-option
              v-for="item in robotList"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
            />
          </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select
          v-model="queryParams.state"
          placeholder="请选择状态"
          clearable
          size="small"
          >
            <el-option
              v-for="item in inspectionStateDate"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
      </el-form-item>
      <el-form-item label="巡检时间">
        <el-date-picker
          v-model="queryTime"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="handleQuery"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="inspectionTasksList" :default-sort = "{prop: 'inspectionTime', order: 'descending'}" @selection-change="handleSelectionChange" style="width:100%;">
      <!-- <el-table-column type="selection" width="55" align="center" fixed="left" /> -->
      <el-table-column label="机器人名称" align="center" prop="eqId" min-width="200" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" min-width="200" />
      <el-table-column label="开始时间" align="center" prop="inspectionTime" min-width="200" sortable />
      <el-table-column label="结束时间" align="center" prop="inspectionEndTime" min-width="200" />
      <el-table-column label="巡检内容" align="center" prop="inspectionContent" min-width="200" :formatter="contentFormat" />
      <el-table-column label="状态" align="center" prop="state">
        <template v-slot="{row}" >
          <el-tag :type="row.state == 1 ? 'warning':row.state == 2 ? '':'success'" v-if="row.state">
            {{ stateFormat(row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="inspectionRemark" min-width="200" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="150" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.state == 2" size="mini" type="text" icon="el-icon-edit" @click="handleSearch(scope.row)" v-hasPermi="['system:inspectionTasks:search']">查看</el-button>
          <el-button v-if="scope.row.state == 1 " size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:inspectionTasks:edit']">修改</el-button>
          <el-button v-if="scope.row.state == 3" size="mini" type="text" icon="el-icon-edit" @click="handleSearchResult(scope.row)" v-hasPermi="['system:inspectionTasks:searchResult']">查看结果</el-button>
          <el-button v-if="scope.row.state == 1 || scope.row.state == 2" size="mini" type="text" icon="el-icon-delete" @click="handleOver(scope.row)" v-hasPermi="['system:inspectionTasks:over']">结束</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="handleQuery" />

    <!-- 新增修改弹窗 -->
    <el-dialog :title="DialogTitle" :visible.sync="open" @close="close" width="600px">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="所属隧道" prop="inspectionTunnel">
          <el-select
          v-model="formData.inspectionTunnel"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
        </el-form-item>
        <el-form-item label="机器人名称" prop="eqId">
          <el-select
          v-model="formData.eqId"
          placeholder="请选择机器人"
          clearable
          size="small"
          >
            <el-option
              v-for="item in robotList"
              :key="item.eqId"
              :label="item.eqName"
              :value="item.eqId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检时间" prop="formDataTime">
          <el-date-picker
            v-model="formDataTime"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="巡检内容" prop="inspectionContent">
          <el-radio-group v-model="formData.inspectionContent">
            <el-radio v-for="item in inspectionContentDate" :key="item.dictValue" :label="item.dictValue">{{item.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否开始" prop="state">
          <el-radio-group v-model="formData.state">
            <el-radio v-show="item.dictValue != 3" v-for="item in inspectionStateDate" :key="item.dictValue" :label="item.dictValue">{{item.dictValue == 1? '否' : '是'}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="inspectionRemark">
          <el-input v-model="formData.inspectionRemark" type="textarea" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="handelConfirm">发布</el-button>
      </div>
    </el-dialog>

    <!-- 查看、查看结果 -->
    <el-dialog class="search" :title="searchDialogTitle" :visible.sync="openSearchDialog" width="600px">
      <el-timeline>
        <el-timeline-item
          v-for="item in searchList"
          :key="item.id"
          :icon="item.icon"
          :type="item.type"
          :color="item.color"
          :size="item.size"
          :timestamp="item.timestamp"
          placement="top">
          <el-card>
            <h4>{{item.content}}</h4>
            <el-row type="flex">
              <el-col :span="8">巡检位置: {{item.position}}</el-col>
            </el-row>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <div slot="footer">
        <el-button @click="searchClose">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listTunnels } from "@/api/equipment/tunnel/api";
import { getInspectionTasksList, addInspectionTasks, updateInspectionTasks } from "@/api/patrolRobot/inspectionTasks.js"
import { getRobotList } from "@/api/patrolRobot/patrolRobot.js"

export default {
  inheritAttrs: false,
  name: "inspectionTasks",
  components: {},
  props: [],
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectionTunnel: '',
        eqId: '',
        state: null,
      },
      queryTime: [],
      // 新增修改弹窗-------------start
      DialogTitle: '',
      type: '',
      open: false,
      formDataTime: [],
      formData: {
        eqId: null,
        inspectionTunnel: null,
        inspectionContent: null,
        inspectionRemark: null,
        state: 1,
        inspectionTime: '',
        inspectionEndTime: '',
      },
      rules: {
        inspectionTunnel: [{
          required: true,
          message: '请选择所属隧道',
          trigger: 'change'
        }],
        eqId: [{
          required: true,
          message: '请选择机器人名称',
          trigger: 'change'
        }],
        inspectionContent: [{
          required: true,
          message: '请选择巡检内容',
          trigger: 'change'
        }],
        state: [{
          required: false,
          message: '请选择是否开始',
          trigger: 'change'
        }],
        inspectionRemark: [
          { required: false, min: 0, max: 100, message: '长度在0 到 100 个字符之间', trigger: 'blur' }
        ],
      }, // 新增修改弹窗-------------end
      // 巡检任务列表
      inspectionTasksList: [],
      // 隧道列表
      tunnelData: [],
      // 轨道机器人列表
      robotList: [],
      // 巡检内容字典
      inspectionContentDate: [],
      // 巡检状态字典
      inspectionStateDate: [],
      // 查看、查看结果弹窗------------------------------------------start
      searchDialogTitle: '',
      openSearchDialog: false,
      searchList: null,
      // 查看、查看结果弹窗------------------------------------------end
    }
  },
  computed: {},
  watch: {},
  created() {
    this.handleQuery()
    this.handleQueryRobts()
    this.getTunnels()
    this.getDicts("inspectionTasks_content").then(response => {
      this.inspectionContentDate = response.data;
    });
    this.getDicts("inspection_state").then(response => {
      this.inspectionStateDate = response.data;
    });
  },
  mounted() {},
  methods: {
    /** 查询巡检任务列表 */
    handleQuery() {
      this.loading = true;
      var params = {
        ...this.queryParams,
        inspectionTime: this.queryTime ? this.parseTime(this.queryTime[0]) : '',
        inspectionEndTime: this.queryTime ? this.parseTime(this.queryTime[1]) : '',
      }
      getInspectionTasksList(params).then(response => {
        this.inspectionTasksList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询机器人列表 */
    handleQueryRobts() {
      getRobotList().then(response => {
        this.robotList = response.rows;
      });
    },
    /** 查询隧道名称列表 */
    getTunnels() {
      listTunnels().then((response) => {
        this.tunnelData = response.rows;
      });
    },
    // 表单重置
    reset() {
      this.formDataTime = []
      this.formData = {
        eqId: null,
        inspectionTunnel: null,
        inspectionContent: null,
        inspectionRemark: null,
        state: null,
        inspectionTime: '',
        inspectionEndTime: '',
      };
      this.resetForm("elForm");
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryTime = []
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 巡检内容字典翻译
    contentFormat(row, column) {
      return this.selectDictLabel(this.inspectionContentDate, row.inspectionContent);
    },
    // 巡检状态字典翻译
    stateFormat(row, column) {
      return this.selectDictLabel(this.inspectionStateDate, row.state);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.DialogTitle = '添加巡检任务'
      this.type = 'add'
      this.open = true
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 查看
    handleSearch(row) {
      this.searchList = [
        {
          content: '巡检结束',
          position: 'k300-100',
          timestamp: '2018-04-12 20:46',
          size: 'large',
          color: '#0bbd87'
        }, {
          content: '事件巡检',
          position: 'k200-100',
          timestamp: '2018-04-03 20:46',
          color: '#0bbd87'
        }, {
          content: '设备巡检',
          position: 'k100-100',
          timestamp: '2018-04-03 20:46',
          color: '#0bbd87'
        }, {
          content: '巡检开始',
          position: 'k0-100',
          timestamp: '2018-04-03 20:46',
          size: 'large',
          type: 'primary',
          icon: 'el-icon-more',
        }
      ]
      this.openSearchDialog = true
    },
    // 查看结果
    handleSearchResult(row) {
      this.openSearchDialog = true
    },
    // 关闭
    searchClose() {
      this.openSearchDialog = false
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      row.inspectionEndTime || (row.inspectionEndTime = '')
      this.DialogTitle = '修改巡检任务'
      this.type = 'edit'
      this.formDataTime = [row.inspectionTime, row.inspectionEndTime]
      this.formData = {
          ...row,
          state: row.state || 1,
        }
      this.open = true
    },
    // 结束
    handleOver(row) {
      var that = this
      this.$confirm('是否确认结束?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        updateInspectionTasks({...row, state: 3}).then((response) => {
          if(response.code == 200) {
            that.$modal.msgSuccess('巡检结束')
            that.handleQuery()
          }
        })
      })
    },
    // 删除
    handleDelete(row) {
      const id = row.id || this.ids;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delMaterial(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(function() {
        this.$modal.msgError("删除失败，请稍后重试");
      });
    },
    // 新增修改弹窗-取消
    close() {
      this.open = false
      this.reset()
      this.$refs['elForm'].resetFields()
    },
    // 新增修改弹窗-确认
    handelConfirm() {
      var that = this
      if(that.formDataTime == [] || !that.formDataTime) {
        return this.$modal.msgWarning('请选择巡检时间哦！')
      }
      this.formData.inspectionTime = this.parseTime(this.formDataTime[0])
      this.formData.inspectionEndTime = this.parseTime(this.formDataTime[1])
      this.$refs.elForm.validate(valid => {
        if (!valid) return
        if(that.type == 'add') {
          addInspectionTasks(that.formData).then((response) => {
            if(response.code == 200) {
              that.$modal.msgSuccess('新增成功')
              that.close()
              that.handleQuery()
            } else {
              that.$modal.msgWarning('新增失败，请稍后重试！')
            }
          }).catch((error) => {
            that.$modal.msgError('新增失败，请稍后重试！')
          })
        } else if(that.type == 'edit') {
          updateInspectionTasks(that.formData).then((response) => {
            if(response.code == 200) {
              that.$modal.msgSuccess('修改成功')
              that.close()
              that.handleQuery()
            } else {
              that.$modal.msgWarning('修改失败，请稍后重试！')
            }
          }).catch((error) => {
            that.$modal.msgError('修改失败，请稍后重试！')
          })
        }
      })
    },
  }
}

</script>
<style lang="less" scoped>
.search {
  .el-card {
    h4 {
      margin-top: 0;
      font-weight: 700;
    }
  }
}
</style>
