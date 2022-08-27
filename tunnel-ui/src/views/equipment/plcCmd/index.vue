<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属隧道" prop="eqTunnelId">
        <el-select v-model="queryParams.eqTunnelId" placeholder="请选择所属隧道" clearable size="small">
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="cmdPlcId">
        <el-select v-model="queryParams.cmdPlcId" placeholder="请选择设备名称" clearable size="small">
          <el-option
            v-for="item in cmdPlcData"
            :key="item.eqId"
            :label="item.eqName"
            :value="item.eqId"
          />
        </el-select>
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:cmd:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:cmd:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:cmd:remove']"
        >删除
        </el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:cmd:export']"-->
<!--        >导出-->
<!--        </el-button>-->
<!--      </el-col>-->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="cmdList"
    max-height="610" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="plc主机" width="150" align="center" prop="cmdPlcId" :formatter="cmdPlcIdFormatter"/>
      <el-table-column label="设备类型" width="150" align="center" prop="cmdDevicesType"
                       :formatter="cmdDevicesTypeFormatter"/>
      <el-table-column label="设备名称" align="center" prop="cmdDevices">
        <template slot-scope="scope">
          <span v-html="cutout(scope.row.cmdDevices)"/>
        </template>
      </el-table-column>
      <el-table-column label="下发的报文" align="center" prop="command"/>
      <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:cmd:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:cmd:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改plc 报文对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="400px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属隧道" prop="eqTunnelId">
              <el-select v-model="form.eqTunnelId" placeholder="请选择所属隧道"
                    clearable size="small" @change="changeTunnel(form.eqTunnelId)">
                <el-option
                  v-for="item in eqTunnelData"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="PLC主机" prop="cmdPlcId">
              <el-select v-model="form.cmdPlcId" filterable placeholder="请选择PLC主机" clearable
                         size="small" @change="changePLC(form.cmdPlcId)">
                <el-option
                  v-for="item in cmdPlcData"
                  :key="item.eqId"
                  :label="item.eqName"
                  :value="item.eqId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="设备名称" prop="cmdDevices">
              <el-select v-model="form.cmdDevices" multiple placeholder="请选择设备名称" clearable
                         size="small" @change="changeCmdDevices(form.cmdDevices)">
                <el-option
                  v-for="item in cmdDevicesData"
                  :key="item.eqId"
                  :label="item.eqName"
                  :value="item.eqId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="报文类型" prop="cmdDevicesType" v-show='showCmdDevicesType'>
              <el-select v-model="form.cmdDevicesType" placeholder="请选择报文类型">
                <el-option
                  v-for="dict in cmdDevicesTypeOptions"
                  :key="dict.typeId"
                  :label="dict.typeName"
                  :value="dict.typeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="下发的报文" prop="command">
            <el-input v-model="form.command" placeholder="请输入下发的报文" type="textarea"/>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listCmd, getCmd, delCmd, addCmd, updateCmd} from "@/api/equipment/plcCmd/api.js";
import {listDevices} from "@/api/equipment/eqlist/api.js";
import { eqTypeList } from "@/api/equipment/type/api.js";
import { listTunnels } from "@/api/equipment/tunnel/api";

export default {
  name: "Cmd",
  data() {
    return {
      showCmdDevicesType:true,
      //所属隧道
      eqTunnelData: {},
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
      //plc主机
      cmdPlcData: {},
      // 设备名称
      cmdDevicesData:[],
      //plc下属设备
      // cmdDeviceData: {},
      //设备类型字典
      cmdDevicesTypeOptions: [],
      // plc 报文表格数据
      cmdList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cmdPlcId: null,
        cmdDevices: null,
        eqTunnelId:null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        eqTunnelId:[{required: true, message: '请选择所属隧道', trigger: ['blur', 'change']}],
        cmdPlcId: [{required: true, message: '请选择PLC主机', trigger: ['blur', 'change']}],
        // cmdDevicesType: [{required: true, message: '请选择设备类型', trigger: ['blur', 'change']}],
        cmdDevices: [
          {required: true, message: '请选择设备',  trigger:'blur'},
           {required: false, message: '请选择设备',  trigger:'change'},
          ],
        command: [
          {required: true, message: '请输入报文', trigger: ['blur', 'change']},
          {min: 1, max: 300, message: '长度为1~300个字符', trigger: ['blur', 'cahnge']},
        ]
      },
    };
  },
  watch: {
    'form.cmdPlcId': {
      handler(newVal, oldVal) {
        if(newVal) {
          this.form.cmdDevices = "";
          this.changePLC(newVal)
        }
      },
    },
    'form.eqTunnelId': {
      handler(newVal, oldVal) {
        if(oldVal) {
          this.form.cmdPlcId = "";
        }
      },
    },
    // 'form.cmdDevicesType':{
    //   handler(newVal, oldVal) {
    //     if(newVal) {
    //       this.getDevices()
    //     } else {
    //       this.cmdDeviceData = [];
    //       this.form.cmdDevices = null;
    //     }
    //   },
    // }
  },
  created() {
    this.getTunnel();
    this.getList();
    // this.getPlc();
    this.getDeviceTypelist();
  },
  methods: {
    changeCmdDevices(data){
      if(data == null){
        this.$refs.form.validateField('cmdDevices[1]') //单独触发校验
      }
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    // 选择所属隧道 获取PLC主机下拉菜单
    changeTunnel(data){
      listDevices({
        eqTunnelId: data,
        eqType:'0',
      }).then((response) => {
        console.log(response.rows,"选择所属隧道 获取PLC主机下拉菜单")
        this.cmdPlcData = response.rows;
      });
    },
    // 选择PLC主机 获取类型为DM还是CIO 若为CIO显示设备类型 若为DM 隐藏设备类型
    changePLC(data){
      console.log(data,"选择PLC主机 获取类型为DM还是CIO")
      for(var item of this.cmdPlcData){
        if(data == item.eqId){
          if(item.protocol == 'DM'){
            listDevices({
              fEqId: item.eqId,
            }).then((res) =>{
              this.cmdDevicesData = res.rows
            })
            this.showCmdDevicesType = false
          }
          if(item.protocol == 'CIO'){
            this.cmdDevicesData = []
             var param = {
              eqId : item.eqId,
              eqName : item.eqName
            }
            this.cmdDevicesData.push(param)
            this.showCmdDevicesType = true
          }
        }
      }
    },
    // 获取设备报文类型
    getDeviceTypelist() {
      eqTypeList().then(response => {
        this.cmdDevicesTypeOptions = response.data;
      })
    },
    /** 查询plc 报文列表 */
    getList() {
      this.loading = true;
      listCmd(this.queryParams).then(response => {
        this.cmdList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** plc主机 */
    // getPlc() {
    //   //查询CIO类型的PLC
    //   listDevices({protocol: "CIO",eqType:"0"}).then(response => {
    //     this.cmdPlcData = response.rows;
    //   });
    // },
    /** plc下属设备 */
    // getDevices() {
    //   this.form.cmdDevices = '';
    //   listDevices({fEqId: this.form.cmdPlcId,eqType:this.form.cmdDevicesType}).then(response => {
    //     this.cmdDeviceData = response.rows;
    //   });
    // },
    /** plc下属设备换行显示 */
    cutout(data) {
      if(data == '' || data == null) return data
      return data.replace(/\,/g, '、');
    },
    /** PLC名称format */
    cmdPlcIdFormatter(row, column) {
      let actions = [];
      for (let i = 0; i < this.cmdPlcData.length; i++) {
        if (row.cmdPlcId == this.cmdPlcData[i].eqId) {
          actions.push(this.cmdPlcData[i].eqName);
          break
        }
      }
      return actions.join('')
    },
    /** 设备类型名称format */
    cmdDevicesTypeFormatter(row, column) {
      var a = ''
      this.cmdDevicesTypeOptions.filter(item => {
        if(item.typeId == row.cmdDevicesType) {
          return a = item.typeName
        }
      })
      return a
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.showCmdDevicesType = true
    },
    // 表单重置
    reset() {
      this.form = {
        cmdId: null,
        cmdPlcId: null,
        cmdDevices: null,
        command: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.ids = selection.map(item => item.cmdId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form = {}
      this.open = true;
      this.title = "添加设备查询报文";
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.reset();
      const cmdId = row.cmdId || this.ids
      let data = {}
      await getCmd(cmdId).then(res => {
        data = res.data
        if (data.cmdDevices != null) {
          data.cmdDevices= data.cmdDevices.split(',');
        }
        data.cmdDevicesType -= 0
        this.form = {...data}
      });
      this.title = "修改PLC报文";
      this.form.cmdDevices = data.cmdDevices || []
      this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cmdDevices != null) {
            this.form.cmdDevices = this.form.cmdDevices.join(",")
          }
          if (this.form.cmdId != null) {
            updateCmd(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addCmd(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
                this.showCmdDevicesType = true
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const cmdIds = row.cmdId || this.ids;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delCmd(cmdIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/cmd/export', {
        ...this.queryParams
      }, `system_cmd.xlsx`)
    }
  }
};
</script>
