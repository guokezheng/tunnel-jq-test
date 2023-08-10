<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="8">
        <el-button
          size="small"
          @click="handleAdd"
          v-if="edit == true"
          v-hasPermi="['system:devices:add']"
        >新增
        </el-button>

        <el-button
          size="small"
          :disabled="multiple"
          v-if="edit == true"
          @click="handleDelete"
          v-hasPermi="['system:devices:remove']"
        >删除
        </el-button>

        <el-button
          size="small"
          @click="handleImport"
          v-if="edit != true"
          v-hasPermi="['system:devices:import']"
        >导入
        </el-button>
        <el-button
          size="small"
          @click="handleExport"
          v-hasPermi="['system:devices:export']"
        >导出
        </el-button>


        <el-button size="small" v-if="edit == true"  @click="handleClose">关闭</el-button>
        <el-button size="small" @click="resetQuery">刷新</el-button>
        <!--          <el-button-->
        <!--            type="info"-->
        <!--            icon="el-icon-s-help"-->
        <!--            size="mini"-->
        <!--            @click="checkInstruction"-->
        <!--            >校验指令</el-button>-->
      </el-col>
      <el-col :span="6" :offset="10">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.searchValue"
            placeholder="请输入设备名称,回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              class="searchTable"
              @click="boxShow = !boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>


    <el-table
        v-loading="loading"
        :data="pointList"
        @selection-change="handleSelectionChange"
        class="allTable"
        height="62vh"
        ref="tableFile"
      >
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="设备id" align="center" prop="eqId" />
      <el-table-column label="设备类型" align="center" prop="eqType" />-->
      <el-table-column label="设备名称" width="250" align="center" prop="eqName" />
       <el-table-column label="功能" width="250" align="center" prop="functionName" />
      <el-table-column label="寄存器点位" align="center" prop="address" />
<!--      <el-table-column label="二进制点" align="center" prop="addressIndex" />-->

      <el-table-column label="数据类型" align="center" prop="dataType" />
      <el-table-column label="点位类型" align="center" prop="isReserved" :formatter="isReservedFormat" />

<!--        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sd_device_type_control"
            :value="scope.row.isReserved"
          />
        </template>-->
<!--      <el-table-column label="信号名称" align="center" prop="signalName" />-->
<!--      <el-table-column label="设备状态" align="center" prop="stateId" />
      <el-table-column label="数据状态" align="center" prop="dataStatus" />
      <el-table-column label="功能描述" align="center" prop="functionDescription" />-->
      <el-table-column label="功能码" align="center" prop="functionCode" />
      <el-table-column label="数据长度" align="center" prop="dataLength" />

      <el-table-column label="服务端JSON" align="center" prop="pointConfig" />
      <el-table-column label="客户端JSON" align="center" prop="functionJson" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="240"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:point:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:point:remove']"
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

    <!-- 添加或修改设备点位状态详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body class="hitchDialog">
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
<!--        <el-form-item label="设备id" prop="eqId">
          <el-input v-model="form.eqId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="设备类型" prop="eqType">
          <el-select v-model="form.eqType" placeholder="请选择设备类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>-->
        <el-form-item label="数据项" prop="itemId">
          <el-select ref="itemRef" v-model="form.itemId" placeholder="请选择数据项" style="width:100%">
            <el-option
              v-for="(item) in itemList"
              :key="item.id"
              :label="item.itemName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="是否可控" prop="isReserved" >
          <el-select
            v-model="form.isReserved"
            placeholder="请选择是否可控"
            @change="itemChange"
            style="width:100%"
          >
            <el-option
              v-for="dict in isControlOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType">
          <el-select v-model="form.dataType" placeholder="请选择数据类型" style="width:100%">
            <el-option
              v-for="(item) in dataTypeList"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue"
            />
          </el-select>
        </el-form-item>
<!--        <el-form-item label="功能" prop="functionName">
          <el-input v-model="form.functionName" placeholder="请输入功能" />
        </el-form-item>-->

        <el-form-item label="寄存器点位" prop="address">
          <el-input v-model="form.address" placeholder="请输入寄存器点位" />
        </el-form-item>
        <el-form-item label="功能码" prop="functionCode">
          <el-input v-model="form.functionCode" placeholder="请输入功能码" />
        </el-form-item>
        <el-form-item label="数据长度" prop="dataLength">
          <el-input  type="number" v-model="form.dataLength" placeholder="请输入数据长度:字节数" />
        </el-form-item>
<!--        <el-form-item label="信号名称" prop="signalName">
          <el-input v-model="form.signalName" placeholder="请输入信号名称" />
        </el-form-item>-->


<!--        <el-form-item label="设备状态" prop="stateId">
          <el-input v-model="form.stateId" placeholder="请输入设备状态" />
        </el-form-item>-->
<!--        <el-form-item label="数据状态">
          <el-radio-group v-model="form.dataStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>-->

<!--        <el-form-item label="是否可控" prop="isReserved">
          <el-select
            v-model="form.isReserved"
            placeholder="请选择是否可控"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dict in dict.type.sd_is_monitor"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            />
          </el-select>
        </el-form-item>-->


        <el-form-item label="服务端JSON" prop="serverJSON">
          <vue-json-editor v-model="serverJSON" mode="code" :showBtns="false" @json-change="onJsonChange"></vue-json-editor>
        </el-form-item>

        <el-form-item label="客户端JSON" prop="clientJSON">
          <vue-json-editor v-model="clientJSON" mode="code"  :showBtns="false" @json-change="onJsonChange"></vue-json-editor>
        </el-form-item>

        <el-form-item label="功能描述" prop="functionDescription">
          <el-input v-model="form.functionDescription" placeholder="请输入功能描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">确 定</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      v-if="upload.open"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
      class="zxc"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :on-error="handleFileError"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
<!--          <el-checkbox v-model="upload.updateSupport" />-->
<!--          更新已经存在的设备点位数据-->
          <el-link
            type="info"
            style="font-size: 12px; color: #39adff"
            @click="importTemplate"
          >下载模板</el-link
          >
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          提示：仅允许导入“xls”或“xlsx”格式文件！
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitFileForm" class="submitButton"
        >确 定</el-button
        >
        <el-button @click="upload.open = false" class="closeButton"
        >取 消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPoint, getPoint, delPoint, addPoint, updatePoint, exportPoint } from "@/api/equipment/protocol/api.js";
import {allListItem} from "@/api/equipment/eqTypeItem/item";
import vueJsonEditor from 'vue-json-editor'
import {getType} from "@/api/equipment/type/api";
import {listEqTypeState} from "@/api/equipment/eqTypeState/api";
import {getToken} from "@/utils/auth";
export default {
  components: { vueJsonEditor },
  name: "Point",
  data() {
    return {
      // 遮罩层
      loading: true,
      serverJSON:"",
      clientJSON:"",
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
      // 是否可控
      isControl:0,
      // 总条数
      total: 0,
      // 设备点位状态详情表格数据
      pointList: [],
      // 是否可控字典
      isControlOptions: [
        {
          "dictLabel":"否",
          "dictValue":"1"
        },
        {
          "dictLabel":"是",
          "dictValue":"2"
        }
      ],
      //设备编号
      eqId:0,
      //设备类型编号
      typeId:0,
      // 设备数据项列表
      itemList:[],
      // 数据类型
      dataTypeList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqId: null,
        eqType: null,
        address: null,
        addressIndex: null,
        functionName: null,
        functionCode: null,
        dataType: null,
        signalName: null,
        stateId: null,
        dataStatus: null,
        functionDescription: null,
        isReserved: null,
        dataLength: null,
        edit:false
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken(),
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/point/importData",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {

    //数据类型
    this.getDicts("sd_device_point_data_type").then(response => {
      this.dataTypeList = response.data;
    });

    this.typeId = this.$route.query.typeId;
    this.eqId = this.$route.query.eqId;
    this.protocolId = this.$route.query.protocolId;


    if(this.typeId && this.eqId && this.protocolId){
      this.edit = true;
    }


    if (this.typeId !== undefined &&  this.typeId != 0) {
      //获取设备数据项
      this.getItemList( this.typeId);
    }
    this.getList();


  },
  methods: {

    isReservedFormat(row, column){

      if(row.isReserved == "2"){
        return "控制";
      }
      return "只读";
      // console.log(row)
      //return this.selectDictLabel(this.isControlOptions, row.isReserved);
    },

    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },

    /** 下载模板操作 */
    importTemplate() {
      /* exportDevicesTemplate()*/
      /*.then((response) => {*/
      this.$download.nameXlsx("设备点位数据.xlsx", false);
      /*});*/
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      if (response.msg != null && response.msg != "") {
        this.$alert(response.msg, "导入结果", {
          customClass: "el-message-box_style",
          dangerouslyUseHTMLString: true,
        });
      } else {
        const msg = "恭喜您，数据已全部导入成功！";
        this.$alert(msg, "导入结果", {
          customClass: "el-message-box_style",
          dangerouslyUseHTMLString: true,
        });
      }

      this.$forceUpdate();
      this.getList();
    },
    // 文件上传失败处理
    handleFileError(err, file, fileList) {
      this.$modal.msgError(JSON.parse(err.message).message);
      this.upload.open = false;
    },
    // 数据项change
    itemChange(){
      console.log("this.form",this.form);
      // 修改操作不显示
      if(this.form.id && this.form.id != 0){
        return;
      }

      /*if(!this.form.isControl || !this.form.itemId){
        this.$modal.msgSuccess("请选择数据项和是否可控，进行初始化模版");
        return;
      }*/
      getType(this.typeId).then(response => {
          let serverJSON = [];
          let clientJSON = [];
          // 可控设备
          if(this.form.isReserved == '2' ){
            let param = {
              stateTypeId :this.typeId,
              isControl : 1
            };
            listEqTypeState(param).then(response => {
                response.rows.forEach(function (item){
                   let clientObj = {
                     "text": item.stateName,
                     "state": item.deviceState,
                     "stateConfig": {
                       "RO1": true,
                     },
                     "resConfig": ["SI1"]
                   };
                  // 客户端 默认点位JSON
                  clientJSON.push(clientObj);

                  let  serverObj = {
                    state: item.deviceState,
                    text: item.stateName,
                    address: '',
                    bit: '4',
                    pointValue: ''
                  };
                  // 服务器默认点位JSON
                  serverJSON.push(serverObj);

                });
                this.serverJSON = serverJSON;
                this.clientJSON = clientJSON;
            });

          }else{
            this.serverJSON = {"address":'0001',"byte":'0',"rangeMin":'0',"rangeMax":'0',"realRangeMin":'0',"realRangeMax":'0'};

            if(this.protocolId == 6){
              this.clientJSON = {"deviceNo":"1","deviceType":"0"};
            }else{
              this.clientJSON = {"key":"AI1","ma_min":"0","ma_max":"0","lc_max":"0","lc_min":"0"};
            }
          }
      });

    },
    /** 打开导入表弹窗 */
    handleImport() {
      this.upload.title = "设备点位导入";
      this.upload.open = true;
    },
    onJsonChange (value) {
      console.log('=====================value:', value)
    },
    /** 查询设备点位状态详情列表 */
    getList() {
      this.loading = true;
      if (this.eqId !== undefined && this.eqId != 0) {
        this.queryParams.eqId = this.eqId
      }
      listPoint(this.queryParams).then(response => {
        console.log(response);
        this.pointList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getItemList(typeId){
       let data = {
          "deviceTypeId" : typeId
        }
        allListItem(data).then(response => {
         this.itemList = response.data;
        });
    },
    // 返回按钮
    handleClose() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/dev/device/eqlist" });
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
        eqId: null,
        eqType: null,
        address: null,
        addressIndex: null,
        functionName: null,
        dataType: null,
        signalName: null,
        stateId: null,
        dataStatus: "0",
        functionDescription: null,
        isReserved: null
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.serverJSON = {};
      this.clientJSON = {};
      this.title = "添加设备点位状态详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPoint(id).then(response => {
        this.form = response.data;
        //this.form.pointConfig = JSON.parse(this.form.pointConfig);
        this.serverJSON = eval('('+ this.form.pointConfig + ')')
        this.clientJSON = JSON.parse(this.form.functionJson);
        this.open = true;
        this.title = "修改设备点位状态详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          this.form.eqId = this.eqId;
          this.form.eqType = this.typeId;
          this.form.functionName =  this.$refs.itemRef.selected.label;

          this.form.pointConfig = JSON.stringify(this.serverJSON);
          this.form.functionJson = JSON.stringify(this.clientJSON);

          if (this.form.id != null) {
            updatePoint(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPoint(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备点位状态详情编号为"' + ids + '"的数据项？').then(function() {
        return delPoint(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出设备点位状态详情数据项？').then(() => {
        this.exportLoading = true;
        return exportPoint(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
<style scoped lang="scss">
::v-deep .el-dialog__body{
    max-height: 70vh!important;
    overflow-y: auto!important;
  }

</style>
