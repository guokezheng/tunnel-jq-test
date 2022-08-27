<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="queryParams.equipmentName" placeholder="请输入设备名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:component:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:component:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:component:remove']">删除</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>



    <el-table ref="table" v-loading="loading" :data="componentList" @selection-change="handleSelectionChange" @row-click="handlePlanRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="设备品牌" align="center" prop="brand" />
      <el-table-column label="设备型号" align="center" prop="model" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName.tunnelName" />
      <el-table-column label="生产厂家" align="center" prop="manufacturer" />
      <el-table-column label="相关文件" align="center" prop="fileId">
        <template slot-scope="scope">
          <el-button v-show="scope.row.fileId !=null" size="mini" type="text" icon="el-icon-link" @click="openFileDrawer(scope.row)"
            style="cursor:pointer;">点击查看</el-button>
          <div v-show="scope.row.fileId == null">
            无
          </div>
        </template>
      </el-table-column>
     <el-table-column label="备注" align="center" prop="remark"  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:component:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:component:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-drawer class="zwsj" :title="drawerFileTitle" :visible.sync="drawerFile" :direction="direction" :before-close="handleFileClose">
      <el-table v-loading="loading" :data="planFileList">
        <el-table-column label="序号" width="100px" align="center">
          <template slot-scope="scope">
            {{scope.$index+1}}
          </template>
        </el-table-column>
        <el-table-column label="文件名称" align="center" prop="fileName" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="loadFile(scope.row)" v-hasPermi="['business:plan:edit']">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>

    <el-drawer class="zwsj" :title="drawerTitle" :visible.sync="open" :direction="direction" :before-close="handleClose" @close="handleDraClose">
      <el-form ref="form1" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="设备名称" prop="equipmentName" style="width: 80%;">
          <el-input v-model="form.equipmentName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备品牌" prop="brand" style="width: 80%;">
          <el-input v-model="form.brand" placeholder="请输入设备品牌" />
        </el-form-item>
        <el-form-item label="设备型号" prop="model" style="width: 80%;">
          <el-input v-model="form.model" placeholder="请输入设备型号" />
        </el-form-item>
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select
            v-model="form.tunnelId"
            placeholder="请选择隧道"
            clearable
            style="width: 76%;"
          >
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer" style="width: 80%;">
          <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
        </el-form-item>

        <el-form-item label="相关文档" prop="equipmentLocation">
          <el-upload style="width: 80%;" multiple class="upload-demo" ref="upload" :limit="5" action="http://xxx.xxx.xxx/personality/uploadExcel"
            :on-preview="handlePreview" :on-change="handleChange" :on-remove="handleRemove" :on-exceed="handleExceed"
            :file-list="fileList" :http-request="uploadFile" :auto-upload="false" >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <span slot="tip" class="el-upload__tip" style="font-style: italic;color: #acacac;padding-left:5%;">*注*：上传文件不可超过1m</span>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="remark" style="width: 80%;">
          <textarea class="el-textarea__inner" v-model="form.remark"  :rows="5" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item style="text-align: center;text-align: center;width: 100%;">
          <el-button style="width: 30%;"  type="primary" @click="submitUpload" v-prevent-click>保 存</el-button>
          <el-button style="width: 30%;" @click="drawerClose">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
  import {
    listComponent,
    getComponent,
    delComponent,
    delComponentById,
    addComponent,
    updateComponent,
    addComponentFile,
    updateComponentFile,
    loadPlanFile
  } from "@/api/equipment/devicePlan/api.js";
  import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
  import {
    listType
  } from "@/api/equipment/type/api";
  import {
    listLocation
  } from "@/api/equipment/locationPlace/api";
  import {
    listFile
  } from "@/api/equipment/equipmentFile/api";
  import {
    download
  } from "@/utils/request";
  export default {
    name: "Component",
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
        // 设备档案管理表格数据
        componentList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 设备状态(0:正常 1:故障 2:离线)字典
        equipmentStateOptions: [],
        typeStateData: {},
        tunnelData: [],
        fileData: '', // 文件上传数据（多文件合一）
        locationPlaceData: {},
        //定义一个文件标题
        drawerFileTitle: "",
        //设备档案查看draw开关
        drawerFile: false,
        // 遮罩层
        dloading: false,
        //设备档案添加/修改drawer开关
        /* drawer: false, */
        //添加/修改定义的文件标题
        drawerTitle: "",
        // 预案文件信息表格数据
        planFileList: [],
        //添加或修改的标志
        changeAddOrEdit: null,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          equipmentId: null,
          /* equipmentCode: null, */
          equipmentName: null,
          brandId: null,
          equipmentTypeId: null,
          tunnelId: null,
          producerId: null,
          equipmentState: null,
          installationPlaceId: null,
          installationPosition: null,
          installationTime: null,
          productionTime: null,
          equipmentInventory: null,
          fileId: null,
          fileName: null,
          imgUrl: null,
          fileUrl: null,
          equipmentPicture: null,
          equipmentPrice: null,
          buyingTime: null,
          equipmentCount: null,
          brand: null,
          model: null,
          manufacturer: null,
        },
        //drawer方向
        direction: 'rtl',
        fileList: [], // upload多文件数组
        //需要移除的文件ids
        removeIds: [],
        //遮罩层
        fileLoading: true,
        // 表单参数
        form: {
          /* equipmentCode: null, */
          equipmentId: null,
          equipmentTypeId: null,
          equipmentName: null,
          tunnelId: null,
          equipmentState: null,
          fileId: null,
          installationPlaceId: null,
          brand: null,
          model: null,
          manufacturer: null,
        },
        // 表单校验
        rules: {
          equipmentName: [
            { required: true, message: "请输入设备名称", trigger: "blur" },
            { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" }
          ],
          brand: [{
            required: true,
            message: "请输入设备品牌",
            trigger: "blur"
          },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" }],
          model: [{
            required: true,
            message: "请输入设备型号",
            trigger: "blur"
          },{
            message: "1 到 20个字符，由字母数字_-组成",
            trigger: 'blur',
            pattern: /^[0-9a-zA-Z_-]{1,20}$/
          }],
          tunnelId: [{
            required: true,
            message: "请选择隧道",
            trigger: "change"
          },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" }],
          manufacturer: [{
            required: true,
            message: "请输入生产厂家",
            trigger: "blur"
          },
          { min: 1, max: 30, message: "长度在1~30个字符之间", trigger: "blur" }],
          equipmentLocation: [{
              required: true,
              message: "请上传相关文件",
              trigger: "blur"
          }],
         /* equipmentType: [{
            required: true,
            message: "请选择设备类型",
            trigger: "blur"
          }], */

        }
      };
    },
    created() {
      this.getList();
      this.getDicts("device_state").then(response => {
        this.equipmentStateOptions = response.data;
      });
      this.getTunnel(); //查询隧道信息
      this.getStateEqType(); //查询设备类型
      this.getLocation(); //查询安装场所

    },
    methods: {
      /** 查询设备档案管理列表 */
      getList() {
        this.loading = true;
        listComponent(this.queryParams).then(response => {
          this.componentList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      //点击查看设备档案文件
      openFileDrawer(row) {
        this.fileLoading = true;
        listFile({
          fileId: row.fileId
        }).then(response => {
          this.drawerFileTitle = "相关文档";
          this.planFileList = response.rows;
          this.fileLoading = false;
          this.drawerFile = true;
        });
      },
      /** drawer-form表单，取消操作 **/
      drawerClose() {
        this.resetDevicePlanDrawForm();
        this.open = false;
      },
      //关闭drawer
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      handleDraClose() {
        this.$refs.form1.resetFields()
      },
      //关闭drawer
      handleFileClose(done) {
        done();
      },
      /** 查询隧道列表 */
      getTunnel() {
        /* .then((data)=>{ })里的data是指接口成功返回的数据,包含请求头,请求体,等信息; */
        listTunnels().then(response => {
          this.tunnelData = response.rows;
        });
      },

      getLocation() {
        listLocation().then(response => {
          /* this.locationPlaceData = response.rows; */
          this.locationPlaceData = [];
          for (var i = 0; i < response.rows.length; i++) {
            response.rows[i].placeId = response.rows[i].placeId.toString();
            this.locationPlaceData.push(response.rows[i]);
          }
        });
      },

      /** 设备类型 */
      getStateEqType() {
        listType().then(response => {
          this.typeStateData = response.rows;
        });
      },

      // 设备状态(0:正常 1:故障 2:离线)字典翻译
      equipmentStateFormat(row, column) {
        return this.selectDictLabel(this.equipmentStateOptions, row.equipmentState);
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
          /* equipmentCode: null, */
          equipmentName: null,
          brandId: null,
          equipmentTypeId: null,
          tunnelId: null,
          producerId: null,
          equipmentState: null,
          installationPlaceId: null,
          installationPosition: null,
          installationTime: null,
          productionTime: null,
          equipmentInventory: null,
          fileId: null,
          fileName: null,
          imgUrl: null,
          fileUrl: null,
          equipmentPicture: null,
          equipmentPrice: null,
          buyingTime: null,
          equipmentCount: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          brand: null,
          model: null,
          manufacturer: null,
        };
        this.resetForm("form");
      },
      //form表单置空
      resetDevicePlanDrawForm() {
        /* this.form.equipmentCode = null; */
        this.form.equipmentId = null;
        this.form.equipmentTypeId = null;
        this.form.equipmentName = null;
        this.form.tunnelId = null;
        this.form.equipmentState = null;
        this.form.installationPlaceId = null;
        this.form.remark = null;
        this.fileList = [];
        this.removeIds = [];
        this.changeAddOrEdit = null;
        this.form.manufacturer = null;
        this.form.brand = null;
        this.form.model = null;
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
        this.rlIds = selection.map(item => item.fileId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      // 选中当前行
      handlePlanRowClick(row) {
        this.$refs.table.toggleRowSelection(row)
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.resetDevicePlanDrawForm();
        this.drawerTitle = "添加设备文件";
        this.changeAddOrEdit = "add";
        this.open = true;
      },
      //点击文件触发
      handlePreview(file, fileList) {
        ///debugger
      },
      // 上传文件
      uploadFile(file) {
        this.fileData.append('file', file.file); // append增加数据
      },
      //下载文件
      async loadFile(row) {
        loadPlanFile(row.id, row.fileName);
      },
      //移除文件
      handleRemove(file, fileList) {
        if (file.hasOwnProperty("fId")) {
          this.removeIds.push(file.fId);
        }
        this.fileList = fileList;
        // return this.$confirm(`确定移除 ${ file.name }？`);
      },
      // 选取文件超过数量提示
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 5 个文件`);
      },
      //监控上传文件列表
      handleChange(file, fileList) {
        let existFile = fileList.slice(0, fileList.length - 1).find(f => f.name === file.name);
        if (existFile) {
          this.$message.error('当前文件已经存在!');
          fileList.pop();
        }
        this.fileList = fileList;
      },
      /** 修改按钮操作 */
      /* handleUpdate(row) {
         this.resetDevicePlanDrawForm();
         const id = row.id || this.ids
         getComponent(id).then(response => {
           this.form = response.data;
           this.open = true;
           this.drawerTitle = "修改设备档案管理";
         });
       }, */
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.resetDevicePlanDrawForm();
        this.changeAddOrEdit = "edit";
        const id = row.id || this.ids
        //查看设备档案详细信息
        getComponent(id).then(response => {
          this.fileList = [];
          this.form = response.data;
          let fileInfo = response.data.pFileList;
          for (var i = 0; i < fileInfo.length; i++) {
            let fileModel = {};
            fileModel.name = fileInfo[i].fileName;
            fileModel.url = fileInfo[i].url;
            fileModel.fId = fileInfo[i].id;
            this.fileList.push(fileModel);
          }
          //文件回显
          this.open = true;
          this.drawerTitle = "修改设备档案管理信息";
        });
      },
      // 上传到服务器
      submitUpload() {
        /* if(!this.form.equipmentCode){
          this.$modal.msgError("请输入设备编号！");
          return;
       } */
        if (!this.form.equipmentName) {
          this.$modal.msgError("请输入设备名称！");
          return;
        }
        if (!this.form.brand) {
          this.$modal.msgError("请输入设备品牌！");
          return;
        }
        if (!this.form.model) {
          this.$modal.msgError("请输入设备型号！");
          return;
        }
        if (!this.form.manufacturer) {
          this.$modal.msgError("请输入生产厂家！");
          return;
        }
        if (!this.form.tunnelId) {
          this.$modal.msgError("请选择所属隧道！");
          return;
        }


        if (this.fileList.length === 0) {
          this.$message({
              message: '请先选择文件',
              type: 'warning'
          })
          return
        }
        /* else { */
        //如果文件存在，定义一个数组去追加预案
        let currentFileList = [];
        for (var i = 0; i < this.fileList.length; i++) {
          if (!this.fileList[i].hasOwnProperty("fId")) {
            currentFileList.push(this.fileList[i]);
          }
        }
        console.log(currentFileList,"currentFileList")
        //isLt100M为true
        const isLt100M = currentFileList.every(file => file.size / 1024 / 1024 < 1);
        if (!isLt100M) {
          this.$message.error('请检查，上传文件大小不能超过1MB!');
        } else {

          this.fileData = new FormData(); // new formData对象
          this.$refs.upload.submit(); // 提交调用uploadFile函数
          this.fileData.append('equipmentName', this.form.equipmentName); // 设备名称
          this.fileData.append('tunnelId', this.form.tunnelId); // 隧道名称
          this.fileData.append('brand', this.form.brand); // 设备品牌
          this.fileData.append('model', this.form.model); // 设备型号
          this.fileData.append('manufacturer', this.form.manufacturer); // 设备型号
          this.fileData.append('remark', this.form.remark==null?"":this.form.remark);
          if (this.changeAddOrEdit == "add") {
            addComponentFile(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("保存成功");
                this.open = false; //关闭drawer窗体
                this.resetDevicePlanDrawForm(); //重置表单
                /* this.open = false; */
                this.getList();
              } else {
                this.$modal.msgError("保存失败");
              }
            });
          } else if (this.changeAddOrEdit == "edit") {
            this.fileData.append('id', this.form.id);
            this.fileData.append('removeIds', this.removeIds);
            this.fileData.append('fileId', this.form.fileId); //文件id
            updateComponentFile(this.fileData).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false; //关闭drawer窗体
                this.resetDevicePlanDrawForm(); //重置表单
                //this.open = false;
                this.getList();
              } else {
                this.$modal.msgError("修改失败");
              }
            });
          }
        }
        /* } */
      },
      /** 删除按钮操作 */
      /* handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除设备档案管理编号为"' + ids + '"的数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delComponent(ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(function() {});
      }, */
      handleDelete(row) {
        const ids = row.id || this.ids;
        const rlIds = row.fileId || this.rlIds;
        console.log(row,rlIds)
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          console.log(2);
          console.log(Array.isArray(rlIds),rlIds.length)
          if (Array.isArray(rlIds) && rlIds.length) {
            return delComponentById(ids);
          }
          return delComponent(rlIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('business/component/export', {
          ...this.queryParams
        }, `system_component.xlsx`)
      }
    }
  };
</script>
<style>
  .el-drawer__header {
    background: #dcdfe6;
    padding: 0 10px;
    height: 58px;
    font-size: 1.125rem;
  }

  .el-drawer__open .el-drawer.rtl {
    overflow: auto;
  }

  .el-drawer__header> :first-child {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    padding-top: 30px;
  }
</style>
