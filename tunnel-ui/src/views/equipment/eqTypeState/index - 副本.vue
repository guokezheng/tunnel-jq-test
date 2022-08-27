<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="设备类型" prop="stateTypeId">
        <el-select v-model="queryParams.stateTypeId" placeholder="请选择设备类型" clearable size="small">
          <el-option v-for="item in typeStateData" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否可控" prop="isControl">
        <el-select v-model="queryParams.isControl" placeholder="请选择是否可控" clearable size="small">
          <el-option v-for="dict in isControlOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:eqTypeState:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:eqTypeState:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:eqTypeState:remove']">删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:eqTypeState:export']">导出</el-button>-->
<!--      </el-col>-->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="eqTypeStateList"
    max-height="610" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="ID" align="center" prop="id" /> -->
      <!-- <el-table-column label="设备类型" align="center" prop="typeId" /> -->
      <el-table-column label="设备类型" align="center" prop="typeName.typeName" />
     <!-- <el-table-column label="设备状态code" align="center" prop="deviceState" />
      <el-table-column label="状态名称" align="center" prop="stateName" /> -->
      <el-table-column label="状态名称" align="center" prop="stateNames" />
     <!-- <el-table-column label="是否可控" align="center" prop="isControl" :formatter="eqControlFormat" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:eqTypeState:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:eqTypeState:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改设备类型状态关系对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1500px" append-to-body :before-close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="设备类型" prop="stateTypeId">
                <el-select v-model="form.stateTypeId" placeholder="请选择设备类型">
                  <el-option v-for="item in typeStateData" :key="item.typeId" :label="item.typeName" :value="item.typeId"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <template v-if="title === '添加设备类型状态关系'">
            <el-row v-for="(item,index) in form.equipmentList" :key="index">
              <el-col :span="6">
                <el-form-item label="设备状态code" prop="deviceState">
                  <el-input v-model="item.deviceState" placeholder="请输入设备状态code" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="状态名称" prop="stateName">
                  <el-input v-model="item.stateName" placeholder="请输入状态名称" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="是否可控" prop="isControl">
                  <el-select v-model="item.isControl" placeholder="请选择是否可控">
                    <el-option v-for="dict in isControlOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="parseInt(dict.dictValue)"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">              
                <el-form-item label="上传图标" label-width="120px">                
                  <el-upload ref="upload" action="http://xxx.xxx.xxx/personality/uploadExcel" list-type="picture-card"
                             :data="{index}" :on-remove="handleRemove" :http-request="uploadFile" :on-success="handleSuccess" :file-list="fileList"
                             :on-exceed="handleExceed" :on-change="handleChange" :limit="2" :class="fileList.length >= 2 ? 'showUpload':''">
                  </el-upload>                  
                  <el-dialog :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                  </el-dialog>
                </el-form-item>
              
              </el-col>
              <el-col :span="1">
                <div class="dialogButton" @click="addFrom()">添加</div>
              </el-col>
              <el-col :span="1">
                <div class="dialogButton" @click="deleteForm(index)">删除</div>
              </el-col>
            </el-row>
          </template>
          <template v-else>
            <el-row v-for="(item,index) in equipmentStates" :key="index">
              <el-col :span="6">
                <el-form-item label="设备状态code" prop="deviceState">
                  <el-input v-model="item.deviceState" placeholder="请输入设备状态code" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="状态名称" prop="stateName">
                  <el-input v-model="item.stateName" placeholder="请输入状态名称" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="是否可控" prop="isControl">
                  <el-select v-model="item.isControl" placeholder="请选择是否可控">
                    <el-option v-for="dict in isControlOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="parseInt(dict.dictValue)"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :span="4">               
                <el-form-item label="上传图标" label-width="120px">        
                  
                  <el-upload ref="upload" action="http://xxx.xxx.xxx/personality/uploadExcel" list-type="picture-card"
                    :data="{index}" :on-remove="handleRemove" :http-request="uploadFile" :on-success="handleSuccess" :file-list="item.iFileList"
                    :on-exceed="handleExceed" :on-change="handleChange" :limit="2" :on-progress='handleChange' >
                  </el-upload>                 
                  
                  <el-dialog :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                  </el-dialog>
                </el-form-item>                 
              </el-col>
              
              <el-col :span="1">
                <div class="dialogButton" @click="addFrom()">添加</div>
              </el-col>
              <el-col :span="1">
                <div class="dialogButton" @click="updataDeleteForm(index)">删除</div>
              </el-col>
            </el-row>
          </template>
        <!-- <el-form-item label="是否区分正反" >
          <el-radio v-model="direction" label="0">是</el-radio>
          <el-radio v-model="direction" label="1">否</el-radio>
        </el-form-item> -->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listEqTypeState,
    getEqTypeState,
    delEqTypeState,
    addEqTypeState,
    updateEqTypeState,
    getEqTypeStateByType,
    addEqTypeStates,
    listEqTypeStates,
    getEqTypeStatesByType,
    addEqTypeState1,
    addList2,
    deletePicture,
    updatePic
  } from "@/api/equipment/eqTypeState/api";
  import {
    listType,
    loadPicture
  } from "@/api/equipment/type/api";
  import moment from "moment";
// import { array } from "yargs";
  import Template from "../../information/template";
  export default {
    name: "EqTypeState",
    components: {Template},
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
        //设备类型
        typeStateData: {},
        // 总条数
        total: 0,
        // 设备类型状态关系表格数据
        eqTypeStateList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否可控字典
        isControlOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          stateTypeId: null,
          deviceState: null,
          stateName: null,
          isControl: null
        },
        // 表单参数
        form: {
          stateTypeId:'',
          equipmentList:[
            {
              stateTypeId:"",
              deviceState:'',
              stateName:'',
              isControl:'',
              iconFileId:''
            }  
          ]
        },
        equipmentStates: [
        ],
        // 表单校验
        rules: {
          stateTypeId: [{
            required: true,
            message: '请选择设备类型',
            trigger: 'change'
          }],
          equipmentList:[
            {
              deviceState: [{
                required: true,
                message: '请输入设备状态code',
                trigger: 'blur'
              }],
              stateName: [{
                required: true,
                message: '请输入状态名称',
                trigger: 'blur'
              }],
              isControl: [{
                required: true,
                message: '请选择是否可控',
                trigger: 'change'
              }],
            }
          ]
        },
        //是否区分方向
        direction: "",
        dialogImageUrl: '',
        dialogVisible: false,
        fileData: [], // 文件上传数据（多文件合一）
        fileList: [], // upload多文件数组
        newList: [],
        //需要移除的文件idsx`
        removeIds: [],
        index:'',
        fileqwe:null,
        //点击的下标
        idx:"",
        //所有返回的图片
        iconFileIdAll:''
      };
    },
    created() {
      this.getList();
      this.getStateEqType();
      this.getDicts("sys_type_control").then(response => {
        this.isControlOptions = response.data;
      });
    },
    methods: {
      addFrom(){
        let now = new Date()
        let item = {
           timeString:now,
           deviceState:'',
           stateName:'',
           isControl:'',
           fileList:[]
          }
        this.form.equipmentList.push(item);
      },
      //添加的删除
      deleteForm(index){
         console.log(this.form,'this.formthis.form');     
      let str=this.form.equipmentList[index].iconFileId
        if(str){          
          deletePicture(str).then(res=>{
            console.log(res,'删除行')
        })
        }
        this.form.equipmentList.splice(index, 1)
      },
      //修改的删除
      updataDeleteForm(index){
        let id= this.equipmentStates[index].id
        delEqTypeState(id).then(res=>{
          console.log(res,'修改的删除');
        })
        this.equipmentStates.splice(index, 1)
      },
      /** 查询设备类型状态关系列表 */
      getList() {
        this.loading = true;
        listEqTypeStates(this.queryParams).then(response => {
          this.eqTypeStateList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 设备类型 */
      getStateEqType() {
        listType().then(response => {
          this.typeStateData = response.rows;
        });
      },
      // 是否可控字典翻译
      eqControlFormat(row, column) {
        return this.selectDictLabel(this.isControlOptions, row.isControl);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.fileList.length = 0
        this.equipmentStates = []
        this.reset();
        
        if(this.iconFileIdAllz1!==''){
             deletePicture(this.iconFileIdAll).then(res=>{
             console.log(res,'取消按钮')
              if(res.code==200){
                this.iconFileIdAll==''
              }            
           })
            console.log(this.iconFileIdAll,'iconFileIdAlliconFileIdAll')
           console.log('点击了新增的取消');
        }
       
      },
      // 表单重置
      reset() {
        this.form = {
          stateTypeId:'',
          equipmentList:[
            {
              deviceState:'',
              stateName:'',
              isControl:'',
              fileList:''
            }
          ]
          // id: null,
          // stateTypeId: null,
          // deviceState: null,
          // stateName: null,
          // isControl: null
        };
        this.fileList = [];
        this.removeIds = [];
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
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        //this.reset();
        this.fileList = [];
        this.open = true;
        this.title = "添加设备类型状态关系";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        //debugger
        var that = this
        // that.reset();
        const id = row.stateTypeId
        getEqTypeStatesByType(id).then(response => {
         console.log(response,'resres')
          this.equipmentStates = response.data;
          /*that.form.equipmentList = response.data;*/
          this.form.stateTypeId = id;
          const iFileList = [];
          this.equipmentStates.forEach(function (item,index,self) {
            item.iFileList.forEach(function (item,index,self) {
              iFileList.push(item)
            });
          });
          that.planRoadmapUrl(iFileList);
          this.open = true;
          this.title = "修改设备类型状态关系";
         
        });
      },
      /** 提交按钮 */
      // submitForm() {
      //   this.$refs["form"].validate(valid => {
      //     if (valid) {
      //       if (this.form.id != null) {
      //         updateEqTypeState(this.form).then(response => {
      //           if (response.code === 200) {
      //             this.$modal.msgSuccess("修改成功");
      //             this.open = false;
      //             this.getList();
      //           }
      //         });
      //       } else {
      //         addEqTypeState(this.form).then(response => {
      //           if (response.code === 200) {
      //             this.$modal.msgSuccess("新增成功");
      //             this.open = false;
      //             this.getList();
      //           }
      //         });
      //       }
      //     }
      //   });
      // },
      /** 提交按钮 */
      submitForm() {
       
        var that = this
        // this.fileData = new FormData(); // new formData对象
        // this.fileData.append("pictureName", this.form.pictureName);
        // this.fileData.append("imageType", this.form.imageType);
        // this.fileData.append("imageWidth", this.form.imageWidth);
        // this.fileData.append("imageHeight", this.form.imageHeight);
        // this.fileData.append("vmsSize", this.form.vmsSize);
        // this.fileData.append("imageRemark", this.form.imageRemark);
        // this.fileData.append("speed", this.form.speed);
        // this.fileData.append("deleteflag", this.form.deleteflag);
        console.log(this.form,'----====------')        
        this.$refs["form"].validate(valid => {
          // this.title = "添加设备类型状态关系";
          if (valid) {
            // that.fileData.append('stateTypeId', that.form.stateTypeId); //类型id
            // that.fileData.append('deviceState', that.form.equipmentList.deviceState); //状态code
            // that.fileData.append('stateName', that.form.equipmentList.stateName); //状态名称
            // that.fileData.append('isControl', that.form.equipmentList.isControl); //是否可控
           
            if (this.title == "修改设备类型状态关系") {
              // if (this.fileList.length === 0) {
              //   this.$message({
              //     message: '请先选择文件',
              //     type: 'warning'
              //   })
              //   return
              // }
              // that.fileData.append('id', that.form.id); //类型id
              // that.fileData.append('iconFileId', that.form.iconFileId); //关联文件id
              // that.fileData.append('removeIds', that.removeIds);
              // updateEqTypeState(that.fileData).then(response => {
              //   if (response.code === 200) {
              //     that.$modal.msgSuccess("修改成功");
              //     that.open = false;
              //     that.reset();
              //     that.getList();下·
              //   }
              // });
              console.log(this.equipmentStates,'this.form.equipmentList');
                updatePic(this.equipmentStates).then(res=>{

                })
            
            } else {
              // console.log(this.form)
              // addEqTypeState(that.fileData).then(response => {
              //   if (response.code === 200) {
              //     that.$modal.msgSuccess("新增成功");
              //     that.reset();
              //     that.open = false;
              //     that.getList();
              //   }
              // });              
                  addList2(this.form.equipmentList).then(res=>{
                      if (res.code==200){
                         that.open = false;
                         that.reset();
                         that.getList();
                          that.$modal.msgSuccess("添加成功");
                      }
                  })
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
          return delEqTypeState(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/eqTypeState/export', {
          ...this.queryParams
        }, `system_eqTypeState.xlsx`)
      },
      changIndex(index){
        this.index = index;
      },
      //=======================================文件上传start================================
      //删除文件
      handleRemove(file, fileList) {
        // console.log(this.idx,file,fileList,'sahnchushahusc')
        // if (file.hasOwnProperty("fId")) {
        //   this.removeIds.push(file.fId);
        // }
        // let data = this.form.equipmentList;
        // for(let i = 0; i < data.length; i++) {
        //   let fileList = data[i].fileList
        //   for(let z = 0; z < fileList.length; z++) {
        //     if(file.uid == fileList[z].uid){
        //       console.log(this.form.equipmentList[i])
        //       this.form.equipmentList[i].fileList.splice(z,1)
        //       // this.form.equipmentList[i].fileList[z].splice(z,1)
        //     }
        //   }
        // }
        // console.log(this.form)
        // this.fileList = fileList;
        if(this.fileList.length===0){
         let fileId= this.form.equipmentList[this.idx].iconFileId      
         this.form.equipmentList[this.idx].iconFileId =''  
            deletePicture(fileId).then(res=>{
              console.log(res,'图片上的删除')
            })
        }
       
      },
      // 上传文件
      uploadFile(param) {      
        let obj=new FormData()
          obj.append('file',param.file)
          // console.log(obj.getAll('file'),'aaaaaaaaaaaaaaaa')  
          // console.log(param)   
        this.idx=param.data.index  
        if(this.title == "添加设备类型状态关系"){
           addEqTypeState1(obj).then(res=>{
         console.log(res,'resssssss')  
         this.iconFileIdAll+=res+','
         this.form.equipmentList.forEach((item,index)=>{
          item.stateTypeId=this.form.stateTypeId
            if (index===param.data.index){
              this.form.equipmentList[index].iconFileId += res+','
           
            }
         })
         console.log(this.form,'ttttttttttttttttttttt');
        })
          
        }else{
          
             addEqTypeState1(obj).then(res=>{
              this.equipmentStates.forEach(item=>{
                item.iconFileId +=res
              })
         console.log(this.form,'xiugaigaigaig');
          })
        }
       
        // this.fileData.push(); // append增加数据
        // this.fileList = fileList;
        // this.form.equipmentList[param.data].fileList.push(param.file)
      },
        getImageIndex(index){
            console.log(index,'mmmmmmmmm');
        },
      //监控上传文件列表
      handleChange(file, fileList,) {
        // this.fileList = fileList;
        
        console.log(file,fileList,'uuuuuuuuuuuuuuu')
      },
      handleSuccess(response, file, fileList){
        console.log(response, file, fileList,'response') 
      },
      // 选取文件超过数量提示
      handleExceed(files, fileList) {
        this.$message.warning("当前限制上传图标个数不超过2个");
      },
      async planRoadmapUrl(iFileList) {
        var that = this
        that.fileList = []
        for (let i = 0; i < iFileList.length; i++) {
          let iconName = iFileList[i].stateIconName
          let iconUrl = await that.picture(iFileList[i].url)
          that.fileList.push({
            name: iconName,
            url: iconUrl,
            fId: iFileList[i].id
          })
          // fileList.push(this.form.iFileList[i].url)
        }
      },
      /* 请求图片base64地址*/
      picture(fileUrl) {
        return new Promise((resolve, reject) => {
          loadPicture({
            url: fileUrl
          }).then(response => {
            if (response.code == 200) {
              let base64 = response.msg
              resolve(base64) //不可缺少
            }
          })
        })
        return resolve(base64)
      },
      //=======================================文件上传end===============================
    }
  };
</script>
<style lang="less" scoped>
.getImageIndex {
  width: 100px;
  height:100px;
  border: 1px solid red;
}
::v-deep .showUpload {
  .el-upload {
    display: none;
  }
}
::v-deep .el-upload--picture-card{
  width: 30px !important;
  height: 30px !important;
}
::v-deep .el-upload-list--picture-card .el-upload-list__item{
   width: 30px !important;
   height: 30px !important;
}
.dialogButton{
  width: 50px;
  height: 24px;
  border: solid 1px #ccc;
  border-radius: 4px;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  margin-top: 5px;
  cursor: pointer;
}
</style>
