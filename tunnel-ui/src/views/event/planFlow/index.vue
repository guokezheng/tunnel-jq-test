<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
            size="small"
            @click="handleAdd"
            v-hasPermi="['system:flow:add']"
          >新增</el-button>
          <el-button
            size="small"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:flow:remove']"
          >删除</el-button>
          <el-button
            size="small"
            :loading="exportLoading"
            @click="handleExport"
          >导出
          </el-button>
          <el-button size="small" @click="resetQuery">刷新</el-button>

      </el-col>
      <el-col :span="6" :offset="12" >
        <div class="grid-content bg-purple" ref="main">
          <el-input
            placeholder="请选择事件类型，回车搜索"
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
    <div class="searchBox" v-show="boxShow" ref="cc">

      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="事件类型" prop="eventTypeId" >
          <el-select
            v-model="queryParams.eventTypeId"
            clearable
            placeholder="请选择事件类型"
            size="small">
            <el-option
              v-for="item in optionsData"
              :key="item.id"
              :label="item.eventType"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
  <!--      <el-form-item label="环节名称" prop="flowName">
          <el-input
            v-model="queryParams.flowName"
            placeholder="请输入环节名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>-->
        <el-form-item class="bottomBox">
          <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="primary"  size="mini" @click="resetQuery">重置</el-button>

        </el-form-item>
      </el-form>
    </div>
    <div class="tableTopHr" ></div>
    <el-table v-loading="loading" :data="flowList" height="59vh"
    @selection-change="handleSelectionChange" class="allTable">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        type="index"
        width="70"
        align="center"
        label="序号">
      </el-table-column>
      <el-table-column label="事件分类" align="center" prop="dictLabel" />
      <el-table-column label="事件类型" align="center" prop="eventTypeId" >
        <template slot-scope="scope">
          <span>
            {{getOptions(scope.row.eventTypeId)}}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:flow:query']"
          >详情</el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:flow:remove']"
          >删除</el-button>
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

    <!-- 添加或修改事件类型预案流程关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="880px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件分类" prop="prevControlType">
          <el-radio v-model="form.radio" label="0" @change="radioFun">交通事件</el-radio>
          <el-radio v-model="form.radio" label="1" @change="radioFun">主动安全</el-radio>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventTypeId">
<!--          <el-input v-model="form.eventTypeId" placeholder="请输入事件类型" />-->
          <el-select
            v-model="form.eventTypeId"
            clearable
            placeholder="请选择事件类型"
            size="small">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.eventType"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <tree-transfer
          class="treeList"
          :from_data='fromData'
          :to_data='toData'
          :defaultProps="{label:'label'}"
          height='540px'
          @add-btn='add'
          @remove-btn='remove'
          :mode='mode'
          filter
          openAll>
        </tree-transfer>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改事件类型预案流程关联对话框 -->
    <el-dialog :title="title" :visible.sync="openDetail" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件类型" prop="eventTypeId">
          <!--          <el-input v-model="form.eventTypeId" placeholder="请输入事件类型" />-->
          <el-select
            v-model="form.eventTypeId"
            clearable
            placeholder="请选择事件类型"
            disabled="disabled"
            size="small">
            <el-option
              v-for="item in optionsData"
              :key="item.id"
              :label="item.eventType"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-tree
          :data="detailData"
          :props="defaultProps"
          :default-expand-all="true"
        ></el-tree>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFlow, getFlow, delFlow, addFlow, updateFlow, exportFlow, getTypeFlowList, checkData} from "@/api/event/planFlow";
import treeTransfer from "el-tree-transfer";
import { listEventType } from "@/api/event/eventType";
import Template from "@/views/information/template";

export default {
  name: "Flow",
  components: {Template, treeTransfer },
  data() {
    const validateLongitude = (rule, value, callback) => {
      if (value == "" || value == null) {
        callback(new Error("请选择事件类型！"));
      } else {
        callback();
      }
    };
    return {
      boxShow:false,
      radio: '0',
      // 遮罩层
      loading: true,
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
      // 事件类型预案流程关联表格数据
      flowList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openDetail: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eventTypeId: null,
        flowId: null,
        flowPid: null,
        flowName: null,
        flowSort: null,
      },
      // 表单参数
      form: {
        eventTypeId: null,
        radio: "0",
      },
      // 表单校验
      rules: {
        eventTypeId: [
          { validator: validateLongitude,required: false, trigger: "change" },
        ],
      },
      defaultProps: {
        children: 'children',
        label: 'flowName'
      },
      fromData:[],
      toData:[],
      mode: "transfer",
      options: [],
      detailData:[],
      optionsData:[]
    };
  },
  created() {
    this.getList();
    this.selectTypeFlowList();
    this.getEventType();
    this.getTableEventType();
  },
  mounted(){
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.main.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
    /** 查询事件类型预案流程关联列表 */
    getList() {
      this.loading = true;
      listFlow(this.queryParams).then(response => {
        this.flowList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查询预案流程树
    selectTypeFlowList() {
      getTypeFlowList().then(res => {
        console.log(res,"1111111111111111111")
        const dataList = this.handleTree(res,"id","pid");
        for(var i = 0; i < dataList.length; i++){
          for(var j = i; j < dataList.length - 1; j++){
            if(Number(dataList[i].sort) > Number(dataList[j + 1].sort)){
              const temp = dataList[i];
              dataList[i] = dataList[j + 1];
              dataList[j + 1] = temp;
            }
          }
        }
        for(var i = 0; i < dataList.length; i++){
          if(dataList[i].children){
            for(var a = 0; a < dataList[i].children.length; a++){
              for(var b = a; b < dataList[i].children.length - 1; b++){
                if(Number(dataList[i].children[a].sort) > Number(dataList[i].children[b + 1].sort)){
                  const temp = dataList[i].children[a];
                  console.log(temp,"temptemptemptemptemptemp")
                  dataList[i].children[a] = dataList[i].children[b + 1];
                  dataList[i].children[b + 1] = temp;
                }
              }
            }
          }
        }
        this.fromData = dataList;
        console.log(this.fromData,"2222222222")
      })
    },

    radioFun(item){
      this.getEventType(item);
    },

    getEventType(item){
      const eventTypeParams = {prevControlType : ""};
      if(item != undefined){
        eventTypeParams.prevControlType = item;
      }else {
        eventTypeParams.prevControlType = this.radio;
      }
      this.form.eventTypeId = null;
      listEventType(eventTypeParams).then(res => {
        console.log(res,"resresresres")
        this.options = res.rows;
      })
    },

    getTableEventType(){
      listEventType().then(res => {
        console.log(res,"optionsDataoptionsData")
        this.optionsData = res.rows;
      })
    },

    //列表事件类型
    getOptions(eventTypeId){
      for(var item of this.optionsData){
        if(item.id == eventTypeId){
          return item.eventType;
        }
      }
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.openDetail = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        eventTypeId: null,
        flowId: null,
        flowPid: null,
        flowName: null,
        flowSort: null,
        createTime: null,
        updateTime: null,
        radio: "0",
      };
      this.toData = [];
      this.selectTypeFlowList();
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
      this.ids = selection.map(item => item.eventTypeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加事件预案流程";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.eventTypeId
      console.log(row,"rowrow")
      getFlow(id).then(response => {
        this.form = response.data;
        const dataList = this.handleTree(response.data,"flowId","flowPid")
        this.form.eventTypeId = dataList[0].eventTypeId;
        this.detailData = dataList;
        this.openDetail = true;
        this.title = "事件预案流程详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFlow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            if(this.toData.length == 0){
              this.$modal.msgError("请选择预案流程");
              return;
            }
            checkData(this.form.eventTypeId).then(res => {
              if(res.code == 200){
                this.form.planFlowList = this.toData
                addFlow(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }else {
                this.$modal.msgError(res.msg)
              }
            })
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.eventTypeId || this.ids;
      this.$modal.confirm('是否确认删除事件类型预案流程').then(function() {
        return delFlow(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出预案流程数据项？').then(() => {
        this.exportLoading = true;
        return exportFlow(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    // 监听穿梭框组件添加
    add(fromData,toData,obj){
      const data = [];
      for(var i = 0; i < toData.length; i++){
        data.push(toData[i])
      }
      for(var i = 0; i < data.length; i++){
        for(var j = i; j < data.length - 1; j++){
          if(Number(data[i].sort) > Number(data[j + 1].sort)){
            const temp = data[i];
            data[i] = data[j + 1];
            data[j + 1] = temp;
          }
        }
      }
      for(var i = 0; i < data.length; i++){
        if(data[i].children){
          for(var a = 0; a < data[i].children.length; a++){
            for(var b = a; b < data[i].children.length - 1; b++){
              if(Number(data[i].children[a].sort) > Number(data[i].children[b + 1].sort)){
                const temp = data[i].children[a];
                data[i].children[a] = data[i].children[b + 1];
                data[i].children[b + 1] = temp;
              }
            }
          }
        }
      }
      this.toData = [];
      this.toData = data;
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的        {keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      console.log("fromData:", fromData);

      console.log("toData:", toData);

      console.log("obj:", obj);

    },

    // 监听穿梭框组件移除
    remove(fromData,toData,obj){
      const data = [];
      for(var i = 0; i < fromData.length; i++){
        data.push(fromData[i])
      }
      for(var i = 0; i < data.length; i++){
        for(var j = i; j < data.length - 1; j++){
          if(Number(data[i].sort) > Number(data[j + 1].sort)){
            const temp = data[i];
            data[i] = data[j + 1];
            data[j + 1] = temp;
          }
        }
      }
      for(var i = 0; i < data.length; i++){
        if(data[i].children){
          for(var a = 0; a < data[i].children.length; a++){
            for(var b = a; b < data[i].children.length - 1; b++){
              if(Number(data[i].children[a].sort) > Number(data[i].children[b + 1].sort)){
                const temp = data[i].children[a];
                data[i].children[a] = data[i].children[b + 1];
                data[i].children[b + 1] = temp;
              }
            }
          }
        }
      }
      this.fromData = [];
      this.fromData = data;
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象

      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表

      console.log("fromData:", fromData);

      console.log("this.fromData:", this.fromData);

      console.log("toData:", toData);

      console.log("obj:", obj);

    }
  }
};
</script>
<style  lang="scss" scoped>

::v-deep ::-webkit-scrollbar {
  width: 0px;
}

</style>
<style  lang="scss" scoped>
.treeList .transfer-main{
  height: calc(100% - 84px) !important;;
}
</style>
