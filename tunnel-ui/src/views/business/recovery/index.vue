<!--
 * @Author: guopeng
 * @Date: 2022-02-22 09:52:40
 * @LastEditTime: 2022-07-11 17:22:49
 * @LastEditors: zbguopeng 18053314396@163.com
 * @Description: 应急管理系统--应急恢复
 * @FilePath: \TunnelPlatform-V3\src\views\business\recovery\index.vue
-->
<template>
  <div class="app-container">
    <el-dialog :title="title" :visible.sync="dialogTableVisible" custom-class="gridDataBox">
      <el-row >
        <p class="bigTitle">之前:</p>
        <el-col :span="24" class="publicBg">
          <p>所属隧道:{{gridData.tunnelName}}</p>
          <p>策略:{{gridData.afterContent}}</p>
          <p>时间:{{gridData.afterTime}}</p>
          <p>工作台记录:</p>
          <img :src="gridData.afterImage" style="width:100%">
        </el-col>
      </el-row>
      <el-row >
        <p class="bigTitle">之后:</p>
        <el-col :span="24" class="publicBg">
          <p>所属隧道:{{gridData.tunnelName}}</p>
          <p>策略:{{gridData.beforeContent}}</p>
          <p>时间:{{gridData.beforeTime}}</p>
          <p>工作台记录:</p>
          <img :src="gridData.beforeImage" style="width:100%">
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="recoveryEditLoading" @click="recoveryEdit(gridData.id)">控制复原</el-button>
        <el-button @click="dialogTableVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属隧道" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
              v-for="item in tunnelData"
              :key="item.tunnelName"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table ref="Table" v-loading="loading" :data="emeResourceList" @selection-change="handleSelectionChange" @row-click="handleRowClick">

      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="应急事件" align="center" prop="warningInfo.warningName" />

      <el-table-column label="预警时间" align="center" prop="warningInfo.warningTime" ></el-table-column>
      <el-table-column label="预警事件信息" align="center" prop="warningInfo.inforSources" ></el-table-column>
      <el-table-column label="事件位置" align="center" prop="warningInfo.position" ></el-table-column>
      <el-table-column label="执行策略之前图片" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.afterImage" style="width:50px;" @click="showImage('after',scope.row.afterImage)">
        </template>
      </el-table-column>
      <el-table-column label="执行策略之后图片" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.beforeImage" style="width:50px;" @click="showImage('before',scope.row.beforeImage)">
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div style="text-align: center;">
                <span :style="
                          scope.row.state == '1'
                            ? 'color: #ff0000'
                            : 'color: #00aa00'
                        ">
                  {{scope.row.state == 1?'未恢复':'已完成'}}
                </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="策略" align="center" prop="strategyContent" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" prop="state">
        <template slot-scope="scope">
          <template v-if="scope.row.state == 1">
            <el-button
              size="mini"
              type="text"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['business:emeResource:edit']"
            >控制复原</el-button>
            <el-button
              size="mini"
              type="text"
              @click="handleEnd(scope.row)"
              v-hasPermi="['business:emeResource:remove']"
            >完结备案</el-button>
          </template>
          <template v-else>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              v-hasPermi="['business:emeResource:remove']"
            >已完成备案</el-button>
        </template>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 执行策略之间之后图片展示 -->
    <el-dialog :visible.sync="imageDialog.imageVisible" custom-class="imageBox">
      <img :src="imageDialog.path">
    </el-dialog>
  </div>
</template>

<script>
import { listRecovery,updateRecover,controlRecovery} from "@/api/business/recovery";
import { listTunnels } from "@/api/equipment/tunnel/api";
export default {
  name: "EmeResource",
  components: {
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
      // 周边资源表格数据
      emeResourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resourceId: null,
        resourceName: null,
        resourceType: null,
        tunnelId: null,
        distance: null,
        person: null,
        phone: null,
        state: null,
        detail: null
      },
      tunnelData:[],
      // 演练类型字典
      resourceTypeOptions: [],

      dialogTableVisible:false,
      recoveryEditLoading: false,
      gridData:{},
      title:'',
    };
  },
  created() {
    this.getList();
    this.getTunnels();
    this.getDicts("sd_resource_type").then(response => {
      this.resourceTypeOptions = response.data;
    });
  },
  methods: {
    // 控制复原按钮
    async recoveryEdit(id){
      if(this.recoveryEditLoading) return
      this.recoveryEditLoading = true
      let data = {
        id:id
      }
      await controlRecovery(data).then(response => {
        console.log(response)
        if(response.code == 200){
          this.$modal.msgSuccess("控制复原成功");
          this.getList();
          this.dialogTableVisible = false
        }
      })
      this.recoveryEditLoading = false
    },
    showImage(type,path){
      console.log(path);
      switch (type) {
        case 'after':
          this.imageDialog.imageTitle = '执行策略之前图片'
          break;
        case 'before':
          this.imageDialog.imageTitle = '执行策略之后图片'
          break;
      }
      this.imageDialog.path = path
      this.imageDialog.imageVisible = true
    },
    // 获取所有隧道名称 下拉框
    getTunnels() {
      listTunnels().then(response => {
        this.tunnelData = response.rows;
      });
    },
    /** 查询应急恢复列表 */
    getList() {
      this.loading = true;
      listRecovery(this.queryParams).then(response => {
        let data = response.rows
        // data.forEach(item=>{
        //   this.tunnelData.forEach(res=>{
        //     if(item.tunnelId == res.tunnelId){
        //       item.tunnelName = res.tunnelName;
        //       console.log(item.tunnelName)
        //     }
        //   })
        // })
        this.emeResourceList = data;
        console.log(this.emeResourceList)
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
        resourceId: null,
        resourceName: null,
        resourceType: null,
        tunnelId: null,
        distance: null,
        person: null,
        phone: null,
        state: null,
        createTime: null,
        updateTime: null,
        detail: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 点击某一行，将其选中
    handleRowClick(row,i,a) {
      this.$refs.Table.toggleRowSelection(row)
    },
    /** 控制复原操作 */
    handleUpdate(row) {
      const id = row.id || this.ids

      this.gridData = row;

      this.title = '控制复原';
      this.dialogTableVisible = true;

      // this.$confirm('是否确认解除策略?', "警告", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      //   type: "warning"
      // }).then(function() {
      //   let data = {
      //     id:id,
      //   }
      //   console.log(id)

      // })
    },
    // 解除策略按钮
    handleRemove(row){
      const ids = row.id || this.ids;
      this.$confirm('是否确认解除策略?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        let data = {
          id:ids,
          state:row.state,
        }
        updateRecover(data).then(response => {
          console.log(response)
          if(response.code == 200){
            this.$modal.msgSuccess("该策略已解除");
            this.getList();
          }
        })
      })
    },
    // 完结备案接口
    updateRecover(data){
        updateRecover(data).then(response => {
          console.log(response)
          if(response.code == 200){
            this.$modal.msgSuccess("该备案已完结");
            this.getList();
          }
        })
    },
    // 完结备案
    handleEnd(row){
      const ids = row.id || this.ids;
      this.$confirm('是否确认完结备案?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.getList();
        let data = {
          id:ids,
          state:row.state = 2,
        }
        this.updateRecover(data);
      })
    },
  }
};
</script>
<style lang="scss">
  .gridDataBox{
    width: 30%;
    p{color:white;font-size:16px;padding:10px 0;}
    .bigTitle{font-size:20px;}
    .publicBg{
      padding:20px;box-sizing: border-box;
      background-color:rgb(26, 115, 163);
    }
  }
  .imageBox{width:30%;background-color: unset!important;box-shadow: unset!important;}
  .imageBox img{width:100%;}
</style>

