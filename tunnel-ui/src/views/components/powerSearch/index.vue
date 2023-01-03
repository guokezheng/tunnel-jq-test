<template>
<div>
  <el-input size="small" placeholder="请输入内容" v-model="current_row.powerName" :readonly="true" style="margin-bottom: 10px">
    <el-button slot="append" icon="el-icon-search" @click="open_search"></el-button>
  </el-input>
  <el-dialog :visible.sync="is_search" width="1200px" style="margin-top: 5px" append-to-body>
    <div class="dialog-container" v-loading.fullscreen.lock="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
      <div class="dialog-left">
        <el-tabs style="height: 100%;" v-model="tabType" type="border-card">
          <el-tab-pane class="tab-pan" name="organization" label="组织机构">
            <dept-tree @defaultCheck="defaultCheck" @nodeCheck="handleCheckChange" :show_checkbox="true" :default_check_first="true" ></dept-tree>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="dialog-right">
        <div style="margin: 10px 0 10px 0">
          <el-input v-model="queryParams.powerName" style="width: 300px" placeholder="请输入仓储名称" size="mini">
            <el-button slot="append" icon="el-icon-search" @click="pagePowerByDeptIds"></el-button>
          </el-input>
        </div>
        <div style="height: calc(100% - 56px);">
          <div style="height: calc(100% - 54px)">
            <el-table height="100%" border :data="list" :highlight-current-row="true"
                      @row-dblclick="row_dblclick" @row-click="row_click">
              <el-table-column min-width="200" label="编号" prop="powerCode" align="center" :show-overflow-tooltip="true">
              </el-table-column>
              <el-table-column min-width="200" label="变电站名称" prop="powerName" align="center" :show-overflow-tooltip="true">
              </el-table-column>
              <!--            <el-table-column min-width="200" label="组织机构" prop="fConame" align="center" :show-overflow-tooltip="true">-->
              <!--            </el-table-column>-->
              <el-table-column min-width="200" label="仓储类型" prop="powerType" align="center" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.power_type" :value="scope.row.powerType"/>
                </template>
              </el-table-column>
              <el-table-column min-width="200" label="桩号" prop="stakeNum" align="center" :show-overflow-tooltip="true">
              </el-table-column>
            </el-table>
          </div>
          <div>
            <pagination v-show="queryParams.total>0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" v-on:pagination="pagePowerByDeptIds"/>
          </div>
        </div>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="is_search = false">取 消</el-button>
      <el-button type="primary" @click="define">确定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
import {pagePowerByDeptIds, powerDropDown} from "@/api/configcenter/power";
/*import DeptTree from '@/views/components/deptTree'*/
export default {
  name: "powerSearch",
  /*components:{DeptTree},*/
  dicts:['power_type'],
  data(){
    return{
      loading:false,
      is_search: false, //弹窗是否打开
      tabType: 'organization', //tab类型
      list: [], //仓储集合
      current_row: {powerName:''}, //选中的仓储
      deptIds: [],
      queryParams: {
        deptIds: null, //组织ID
        powerName: null, //搜索名称
        total: 0, // 总条数
        pageNum: 1,
        pageSize: 10,
      }
    }
  },
  mounted() {
    this.getDefaultPower();
  },
  methods: {
    //组织树默认选中回调
    defaultCheck(keys){
      this.deptIds=keys;
      this.pagePowerByDeptIds();
    },
    //打开搜索弹框
    open_search() {
      //初始化弹窗
      this.queryParams.powerName = '';
      this.is_search = true;
    },
    //组织树节点选中变化回调
    handleCheckChange(data, checked){
      this.deptIds=checked.checkedKeys;
      this.pagePowerByDeptIds();
    },
    //表格行双击
    row_dblclick() {
      this.is_search = false;
      this.$emit('dbClick', this.current_row);
    },
    //表格行点击
    row_click(row, column, event) {
      this.current_row = row;
    },
    //确定按钮
    define(){
      if(this.current_row == null){
        this.$message({
          showClose: true,
          message: '请至少选择一行',
          type: 'error',
          duration: 1500
        });
        return;
      }
      this.row_dblclick();
    },
    /*根据deptId集合查询供配电单元*/
    pagePowerByDeptIds(){
      if(this.deptIds.length>0){
        this.loading=true;
        this.queryParams.deptIds = this.deptIds.join(',');
        pagePowerByDeptIds(this.queryParams).then(res => {
          this.list = res.rows;
          this.queryParams.total = res.total;
          this.loading=false;
        });
      }
    },
    /*查询 默认供配电单元*/
    getDefaultPower(){
      powerDropDown({}).then(res => {
        this.current_row=res.data[0];
        this.$emit('defaultCheck', res.data[0])
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-container{
  height: 70vh;
  .dialog-left{
    width: 300px;
    height: 100%;
    float: left;
    margin-right: 15px
  }
  .dialog-right{
    height: 100%;
    width: 840px;
    float: left;
    padding: 0 10px 0 10px;
    border: 1px solid #F2F2F2;
    box-shadow: 0 2px 4px 0 rgb(0 0 0 / 12%), 0 0 6px 0 rgb(0 0 0 / 4%);
  }
}

::v-deep.el-tabs--border-card > .el-tabs__content {
  padding: 10px;
  height: calc(100% - 42px);
}

.pagination-container {
  position: relative;
  height: 25px;
  margin-bottom: 10px;
  margin-top: 0px;
  padding: 10px 20px !important;
}

::v-deep .el-dialog__body {
  padding: 10px 20px;
}

::v-deep .el-table tr:focus {
  background-color: #99a9bf !important;
}

#pane-organization {
  height: 100%;
}
</style>
