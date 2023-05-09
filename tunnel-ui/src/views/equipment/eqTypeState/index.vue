<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button size="small" @click="resetQuery" type="primary" plain
          >刷新</el-button
        >
      </el-col>
      <el-col :span="6" :offset="12">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            v-model="queryParams.searchValue"
            placeholder="请输入设备类型名称,回车搜索"
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

    <div ref="cc" class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="80px"
      >
        <el-form-item label="设备类型" prop="stateTypeId">
          <el-select
            v-model="queryParams.stateTypeId"
            placeholder="请选择设备类型"
            clearable
            size="small"
          >
            <el-option
              v-for="item in typeStateData"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
            >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!-- <el-form :model="queryParams" align="right" ref="queryForm" :inline="true" v-show="showSearch" label-width="70px"> -->
    <!--      <el-form-item label="设备类型" prop="stateTypeId">-->

    <!--      </el-form-item>-->
    <!-- <el-form-item label="是否可控" prop="isControl">
        <el-select v-model="queryParams.isControl" placeholder="请选择是否可控" clearable size="small">
          <el-option v-for="dict in isControlOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item> -->
    <!--      <el-form-item>-->
    <!--&lt;!&ndash;        <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>&ndash;&gt;-->
    <!--&lt;!&ndash;        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>&ndash;&gt;-->
    <!--        <el-button type="primary" plain size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:eqTypeState:edit']">修改</el-button>-->
    <!--        <el-button type="primary" plain size="mini" :disabled="multiple" @click="handleDeleteAll"-->
    <!--                   v-hasPermi="['system:eqTypeState:remove']">删除</el-button>-->
    <!--      </el-form-item>-->
    <!-- <el-form-item >
        <el-input clearable
                  size="small"
                  @keyup.enter.native="handleQuery"  v-model="queryParams.searchValue" style="width:360px" placeholder="请输入设备类型名称,回车搜索" />
      </el-form-item>
    </el-form> -->
    <!--
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:eqTypeState:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:eqTypeState:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDeleteAll"
              v-hasPermi="['system:eqTypeState:remove']">删除</el-button>
          </el-col>
         <el-col :span="1.5">
           <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:eqTypeState:export']">导出</el-button>
         </el-col>
          <div class="top-right-btn">
            <el-tooltip class="item" effect="dark" content="刷新" placement="top">
              <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
              <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
            </el-tooltip>
          </div>
        </el-row> -->
    <div class="tableTopHr"></div>
    <el-table
      v-loading="loading"
      :data="eqTypeStateList"
      @selection-change="handleSelectionChange"
      class="allTable"
      height="59vh"
    >
      <!--      <el-table-column type="selection" width="55" align="center" />-->
      <!-- <el-table-column label="ID" align="center" prop="id" /> -->
      <!-- <el-table-column label="设备类型" align="center" prop="typeId" /> -->
      <el-table-column
        type="index"
        :index="indexMethod"
        label="序号"
        width="68"
        align="center"
      ></el-table-column>
      <el-table-column label="设备类型" align="center" prop="typeName" />
      <el-table-column label="状态类型" align="center" prop="stateType" />
      <!-- <el-table-column label="设备状态code" align="center" prop="deviceState" />
       <el-table-column label="状态名称" align="center" prop="stateName" /> -->
      <el-table-column
        label="状态名称"
        align="center"
        prop="stateNames"
        width="700"
      />
      <!-- <el-table-column label="是否可控" align="center" prop="isControl" :formatter="eqControlFormat" /> -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:eqTypeState:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            class="tableDelButtton"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:eqTypeState:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备类型状态关系对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1500px"
      append-to-body
      :before-close="cancel"
      class="tunnelRelationDialog"
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="设备类型" prop="stateTypeId">
              <el-select
                v-model="form.stateTypeId"
                placeholder="请选择设备类型"
                disabled
              >
                <el-option
                  v-for="item in typeStateData"
                  :key="item.typeId"
                  :label="item.typeName"
                  :value="item.typeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="1" v-show="title != '添加设备类型状态关系'">
            <el-button class="dialogButton addFormButton" @click="addFrom()"
              >添加</el-button
            >
          </el-col>
        </el-row>
        <template v-if="title === '添加设备类型状态关系'">
          <el-row v-for="(item, index) in form.equipmentList" :key="index">
            <el-col :span="6">
              <el-form-item label="设备状态code" prop="deviceState">
                <el-input
                  v-model="item.deviceState"
                  placeholder="请输入设备状态code"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态名称" prop="stateName">
                <el-input
                  v-model="item.stateName"
                  placeholder="请输入状态名称"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="是否可控" prop="isControl">
                <el-select
                  v-model="item.isControl"
                  placeholder="请选择是否可控"
                >
                  <el-option
                    v-for="dict in isControlOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="上传图标" label-width="120px">
                <el-upload
                  ref="upload"
                  action="http://xxx.xxx.xxx/personality/uploadExcel"
                  list-type="picture-card"
                  :data="{ index }"
                  :on-remove="handleRemove"
                  :http-request="uploadFile"
                  :on-success="handleSuccess"
                  :file-list="fileList"
                  :on-exceed="handleExceed"
                  :on-change="handleChange"
                  :limit="2"
                  :class="fileList.length >= 2 ? 'showUpload' : ''"
                >
                  <img v-if="imageUrl" :src="item.url" class="avatar" />
                </el-upload>
                <el-dialog
                  :visible.sync="dialogVisible"
                  :close-on-click-modal="false"
                >
                  <img width="100%" :src="dialogImageUrl" alt="" />
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
          <el-row v-for="(item, index) in equipmentStates" :key="index">
            <el-col :span="3">
              <el-form-item label="设备状态" prop="deviceState">
                <el-input
                  v-model="item.deviceState"
                  placeholder="请输入设备状态code"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="状态类型" prop="stateType" class="stateType">
                <el-select
                  v-model="item.stateType"
                  placeholder="请选择状态类型"
                >
                  <el-option
                    v-for="dict in isStateType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="状态名称" prop="stateName">
                <el-input
                  v-model="item.stateName"
                  placeholder="请输入状态名称"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="是否可控" prop="isControl">
                <el-select
                  v-model="item.isControl"
                  placeholder="请选择是否可控"
                >
                  <el-option
                    v-for="dict in isControlOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="parseInt(dict.dictValue)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="排序" prop="isControl">
                <el-input-number
                  v-model="item.sort"
                  controls-position="right"
                  size="small"
                  :min="1"
                  :max="10"
                ></el-input-number>
              </el-form-item>
            </el-col>

            <el-col :span="4">
              <el-form-item label="上传图标" label-width="120px">
                <el-upload
                  ref="upload"
                  action="http://xxx.xxx.xxx/personality/uploadExcel"
                  list-type="picture-card"
                  :data="{ index }"
                  :on-remove="handleRemove"
                  :http-request="uploadFile"
                  :on-success="handleSuccess"
                  :file-list="item.iFileList"
                  :on-exceed="handleExceed"
                  :on-change="handleChange"
                  :limit="2"
                  :on-progress="handleChange"
                  :class="item.iFileList.length >= 2 ? 'showUpload' : ''"
                >
                  <!-- <img :src="item.url" alt="">       -->
                </el-upload>

                <el-dialog
                  :visible.sync="dialogVisible"
                  :close-on-click-modal="false"
                >
                  <img width="100%" :src="dialogImageUrl" alt="" />
                </el-dialog>
              </el-form-item>
            </el-col>

            <!-- <el-col :span="1">
              <div class="dialogButton" @click="addFrom()">添加</div>
            </el-col> -->
            <el-col :span="1">
              <div class="dialogButton" @click="updataDeleteForm(index)">
                删除
              </div>
            </el-col>
          </el-row>
        </template>
        <!-- <el-form-item label="是否区分正反" >
          <el-radio v-model="direction" label="0">是</el-radio>
          <el-radio v-model="direction" label="1">否</el-radio>
        </el-form-item> -->
      </el-form>
      <div class="dialog-footer">
        <el-button class="submitButton" @click="submitForm">提 交</el-button>
        <el-button class="closeButton" @click="cancel">取 消</el-button>
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
  updatePic,
  deleteRow,
  deleteEquiment,
  batchDeletePic,
} from "@/api/equipment/eqTypeState/api";
import {
  addType,
  listType,
  loadPicture,
  updateType,
} from "@/api/equipment/type/api";
import moment from "moment";
// import { array } from "yargs";
import Template from "../../information/template";
export default {
  name: "EqTypeState",
  components: { Template },
  data() {
    return {
      boxShow: false,

      imageUrl: "",
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
        searchValue: null,
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        isControl: null,
      },
      // 表单参数
      form: {
        stateTypeId: "",
        equipmentList: [
          {
            stateTypeId: "",
            deviceState: "",
            stateName: "",
            isControl: "",
            iconFileId: "",
          },
        ],
      },
      equipmentStates: [],
      deleteEquipmentStates: [],
      // 表单校验
      rules: {
        stateTypeId: [
          {
            required: true,
            message: "请选择设备类型",
            trigger: "change",
          },
        ],
        equipmentList: [
          {
            deviceState: [
              {
                required: true,
                message: "请输入设备状态code",
                trigger: "blur",
              },
            ],
            stateName: [
              {
                required: true,
                message: "请输入状态名称",
                trigger: "blur",
              },
            ],
            isControl: [
              {
                required: true,
                message: "请选择是否可控",
                trigger: "change",
              },
            ],
          },
        ],
      },
      //是否区分方向
      direction: "",
      dialogImageUrl: "",
      dialogVisible: false,
      fileData: [], // 文件上传数据（多文件合一）
      fileList: [], // upload多文件数组
      newList: [],
      //需要移除的文件idsx`
      removeIds: [],
      index: "",
      fileqwe: null,
      //点击的下标
      idx: "",
      //所有返回的图片
      iconFileIdAll: "",
      //图标
      iconList: [],
      //多选的删除
      typeidArr: [],
      //状态类型枚举
      isStateType: [],
      //状态类型idx
      sid: "",
      //需要删除的图片
      currentDeleteFile: [],
      //删除的iconId
      iconId: "",
      //新添加的图片
      currentAddFile: [],
    };
  },
  created() {
    this.getList();
    this.getStateEqType();
    this.getDicts("sys_type_control").then((response) => {
      // console.log(response.data,'response.dataresponse.data')
      this.isControlOptions = response.data;
    });
    this.getDicts("sd_device_state_type").then((response) => {
      this.isStateType = response.data;
    });
  },
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    bodyCloseMenus(e) {
      let self = this;
      if (
        !this.$refs.main.contains(e.target) &&
        !this.$refs.cc.contains(e.target)
      ) {
        if (self.boxShow == true) {
          self.boxShow = false;
        }
      }
    },
    //翻页时不刷新序号
    indexMethod(index) {
      return (
        index + (this.queryParams.pageNum - 1) * this.queryParams.pageSize + 1
      );
    },
    addFrom() {
      let now = new Date();
      let item = {
        timeString: now,
        deviceState: "",
        stateName: "",
        isControl: "",
        iFileList: [],
        iconFileId: "",
        stateTypeId: this.form.stateTypeId,
        id: "",
      };
      // this.form.equipmentList.push(item);
      this.equipmentStates.push(item);
    },
    //添加的删除
    deleteForm(index) {
      //  console.log(this.form,'this.formthis.form');
      // let str=this.form.equipmentList[index].iconFileId
      //   if(str){
      //     deletePicture(str).then(res=>{
      //       console.log(res,'删除行')
      //   })
      //   }
      // this.equipmentStates.splice(index, 1)
    },
    //修改的删除
    updataDeleteForm(index) {
      // let id= this.equipmentStates[index].id
      // delEqTypeState(id).then(res=>{
      //   console.log(res,'修改的删除');
      // })
      // this.equipmentStates.splice(index, 1)
      console.log(
        this.equipmentStates[index].id,
        "this.equipmentStatesthis.equipmentStates"
      );
      let rowid = this.equipmentStates[index].id;
      console.log(
        this.equipmentStates[index].id,
        "this.equipmentStatesthis.equipmentStates"
      );
      if (!!this.equipmentStates[index].id) {
        //需要删除的集合
        debugger
        this.deleteEquipmentStates.push(rowid)
        // deleteRow(rowid).then((res) => {
        //   console.log(res, "shanchu");
        //   if ((res.code = 200)) {
        //     this.$modal.msgSuccess("删除成功");
        //   }
        // });
      }

      this.equipmentStates.splice(index, 1);
    },
    /** 查询设备类型状态关系列表 */
    getList() {
      this.loading = true;
      this.boxShow = false;
      listEqTypeStates(this.queryParams).then((response) => {
        console.log(response, "responseresponseresponse");
        response.rows.forEach((item) => {
          if (item.stateType == 1) {
            item.stateType = "设备运行状态";
          } else if (item.stateType == 2) {
            item.stateType = "设备数据状态";
          }
        });
        this.eqTypeStateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 设备类型 */
    getStateEqType() {
      listType().then((response) => {
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
      this.fileList.length = 0;
      this.equipmentStates = [];
      this.reset();
      console.log(this.iconFileIdAll, "this.iconFileIdAllthis.iconFileIdAll");
      this.currentDeleteFile = "";
      this.deleteEquipmentStates=[]
      // if(this.iconFileIdAllz1!==''){
      //      deletePicture(this.iconFileIdAll).then(res=>{
      //      console.log(res,'取消按钮')
      //       if(res.code==200){
      //         this.iconFileIdAll==''
      //       }
      //    })
      // }
    },
    // 表单重置
    reset() {
      this.form = {
        stateTypeId: "",
        equipmentList: [
          {
            deviceState: "",
            stateName: "",
            isControl: "",
            fileList: "",
          },
        ],
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
      this.queryParams.searchValue = "";
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      console.log(selection, "selectionselection");
      this.typeidArr = selection;
      if (selection.length == 0) {
        this.ids = "";
      } else {
        this.ids = selection[0].typeId;
        if (selection[0].stateType == "设备数据状态") {
          this.sid = 2;
        } else if (selection[0].stateType == "设备运行状态") {
          this.sid = 1;
        }
      }
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      console.log(this.ids, "this.idsthis.ids");
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
      this.currentDeleteFile = "";
      var that = this;
      // that.reset();

      let id = "";
      let staTyId = "";
      if (this.ids.length == 0) {
        id = row.typeId;
      } else {
        id = this.ids;
      }
      console.log(row.stateType, "stateTypestateTypev");
      if (
        (row.stateType === "设备数据状态" && row.stateType) ||
        this.sid == 2
      ) {
        staTyId = 2;
      } else if (
        (row.stateType === "设备运行状态" && row.stateType) ||
        this.sid == 1
      ) {
        staTyId = 1;
      } else if (row.stateType === undefined) {
        staTyId = 0;
      } else {
        staTyId = this.sid;
      }

      // console.log(staTyId,'11111111111111')
      getEqTypeStatesByType(id, staTyId).then((response) => {
        console.log(response, "resres");
        if (response.data.length == 0) {
          let item = {
            deviceState: "",
            stateName: "",
            isControl: "",
            iFileList: [],
            iconFileId: "",
            stateTypeId: id,
          };
          this.equipmentStates = [];
          this.equipmentStates.push(item);
          console.log(this.equipmentStates, " this.equipmentStatesv");
        } else {
          this.equipmentStates = response.data;
        }

        /*that.form.equipmentList = response.data;*/
        this.form.stateTypeId = id;
        const iFileList = [];
        this.equipmentStates.forEach(function (item, index, self) {
          if (item.iFileList) {
            item.iFileList.forEach(function (it, index, self) {
              // that.planRoadmapUrl(it.url);
              //    new Promise((resolve, reject) => {
              //   loadPicture({
              //     url: it.url
              //   }).then(response => {
              //     if (response.code == 200) {
              //       let base64 = response.msg
              //       it.url=base64
              //       resolve(base64) //不可缺少
              //     }
              //   })
              // })
            });
          }
        });

        this.open = true;
        this.title = "修改设备类型状态关系";
      });
    },
    //  async planRoadmapUrl(iFileList) {
    //   var that = this
    //   // that.fileList = []
    //   // console.log(iFileList,'iFileListiFileListiFileList')
    //     // let iconName = iFileList[i].stateIconName
    //     let iconUrl = await that.picture(iFileList)
    //     // console.log(iconUrl,'iconUrliconUrl')
    //       this.equipmentStates.forEach(item=>{
    //           item.iFileList.forEach(it=>{
    //              it.url=iconUrl
    //           })
    //       })
    //   console.log(that.equipmentStates,'gggggggggggggggg');
    // },
    // /* 请求图片base64地址*/
    // picture(fileUrl) {
    //   return new Promise((resolve, reject) => {
    //     loadPicture({
    //       url: fileUrl
    //     }).then(response => {
    //       if (response.code == 200) {
    //         let base64 = response.msg
    //         resolve(base64) //不可缺少
    //       }
    //     })
    //   })
    //   // return resolve(base64)
    // },
    // that.equipmentStates.push({
    //   name: iconName,
    //   url: iconUrl,
    //   fId: iFileList[i].id
    // })

    //  that.fileList.push(this.form.iFileList[i].url)
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
      var that = this;
      // this.fileData = new FormData(); // new formData对象
      // this.fileData.append("pictureName", this.form.pictureName);
      // this.fileData.append("imageType", this.form.imageType);
      // this.fileData.append("imageWidth", this.form.imageWidth);
      // this.fileData.append("imageHeight", this.form.imageHeight);
      // this.fileData.append("vmsSize", this.form.vmsSize);
      // this.fileData.append("imageRemark", this.form.imageRemark);
      // this.fileData.append("speed", this.form.speed);
      // this.fileData.append("deleteflag", this.form.deleteflag);
      console.log(this.form, "----====------");
      this.$refs["form"].validate((valid) => {
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
            if (this.currentDeleteFile != "") {
              if (this.currentDeleteFile.slice(0, 1) == ",") {
                this.currentDeleteFile = this.currentDeleteFile.slice(1);
              }
              let deleteFiles = this.currentDeleteFile.split(",");
              if (this.equipmentStates.length > 0) {
                for (let i = 0; i < this.equipmentStates.length; i++) {
                  let file = this.equipmentStates[i].iFileList;
                  if (file.length > 0) {
                    for (let j = 0; j < file.length; j++) {
                      for (let k = 0; k < deleteFiles.length; k++) {
                        if (file[j].id == deleteFiles[k]) {
                          file[j] = "";
                        }
                      }
                    }
                    if (file.length == 1 && file[0] == "") {
                      if (this.currentAddFile == "") {
                        this.iconId = this.equipmentStates[i].iconFileId;
                        this.equipmentStates[i].iconFileId = "";
                      } else if (
                        this.currentAddFile.indexOf(
                          this.equipmentStates[i].iconFileId
                        ) != -1
                      ) {
                        this.equipmentStates[i].iconFileId =
                          this.equipmentStates[i].iconFileId;
                      }
                    }
                    if (file.length == 2 && file[0] == "" && file[1] == "") {
                      if (this.currentAddFile == "") {
                        this.iconId = this.equipmentStates[i].iconFileId;
                        this.equipmentStates[i].iconFileId = "";
                      } else if (
                        this.currentAddFile.indexOf(
                          this.equipmentStates[i].iconFileId
                        ) != -1
                      ) {
                        this.equipmentStates[i].iconFileId =
                          this.equipmentStates[i].iconFileId;
                      }
                    }
                  }
                }
              }
            } else {
            }
            setTimeout(() => {
              let login = this.equipmentStates.every((item) => {
                return item.iconFileId !== "";
              });
              if (!login) {
                if (this.iconId != "" && this.iconId.substr(0, 1) == ",") {
                } else {
                  return this.$modal.msgWarning("请上传图片");
                }
              }

              if (this.equipmentStates.length > 0) {
                for (let i = 0; i < this.equipmentStates.length; i++) {
                  const flag =
                    this.equipmentStates[i].hasOwnProperty("stateType");
                  if (flag == false) {
                    return this.$modal.msgWarning("请选择状态类型");
                  }
                }
              }
              updatePic(this.equipmentStates).then((res) => {
                if (res.code == 200) {
                  if(this.deleteEquipmentStates.length>0){
                    for (let i = 0; i < this.deleteEquipmentStates.length; i++) {
                      deleteRow( this.deleteEquipmentStates[i]).then((res) => {
                        console.log(res, "shanchu");
                        if ((res.code = 200)) {
                          // this.$modal.msgSuccess("删除成功");
                        }
                      });
                    }
                  }

                  if (this.currentDeleteFile != "") {
                    batchDeletePic(this.currentDeleteFile).then((res) => {
                      this.open = false;
                      this.$modal.msgSuccess("修改成功");
                      this.getList();
                      this.currentDeleteFile = "";
                      this.currentAddFile = "";
                      this.iconId = "";
                    });
                  } else {
                    this.open = false;
                    this.$modal.msgSuccess("修改成功");
                    this.getList();
                  }
                }
              });
            }, 600);
          } else {
            addList2(this.form.equipmentList).then((res) => {
              if (res.code == 200) {
                that.open = false;
                that.reset();
                that.getList();
                that.$modal.msgSuccess("添加成功");
                this.currentDeleteFile = "";
              }
            });
          }
        }
      });
      //this.currentDeleteFile = "";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          console.log(row, "rowww");
          deleteEquiment(row.typeId).then((res) => {
            if (res.code == 200) {
              that.$modal.msgSuccess("删除成功");
              that.getList();
              console.log("删除成功");
            }
          });
        })
        .catch(function () {});
    },
    //  删除多条
    handleDeleteAll() {
      let that = this;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          const typeids = [];
          that.typeidArr.forEach((item) => {
            typeids.push(item.typeId);
          });
          // console.log(typeids,'typeidstypeids');
          deleteEquiment(typeids).then((res) => {
            if (res.code == 200) {
              that.$modal.msgSuccess("删除成功");
              that.getList();
              console.log("删除成功");
            }
          });
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/eqTypeState/export",
        {
          ...this.queryParams,
        },
        `system_eqTypeState.xlsx`
      );
    },
    changIndex(index) {
      this.index = index;
    },
    //=======================================文件上传start================================
    //删除文件
    handleRemove(file, fileList) {
      this.currentDeleteFile = this.currentDeleteFile + "," + file.id;
      if (file.hasOwnProperty("fId")) {
        this.removeIds.push(file.fId);
      }
      this.fileList = fileList;
    },
    // 上传文件
    uploadFile(param) {
      let obj = new FormData();
      obj.append("file", param.file);
      // console.log(obj.getAll('file'),'aaaaaaaaaaaaaaaa')
      // console.log(param)
      this.idx = param.data.index;
      if (this.title == "添加设备类型状态关系") {
        addEqTypeState1(obj).then((res) => {
          //  console.log(res,'resssssss')
          //  this.iconFileIdAll+=res+','
          //  this.form.equipmentList.forEach((item,index)=>{
          //   item.stateTypeId=this.form.stateTypeId
          //     if (index===param.data.index){
          //       this.form.equipmentList[index].iconFileId += res+','
          //     }
          //  })
          // console.log(res,'shanchuantupa');
        });
      } else {
        addEqTypeState1(obj).then((res) => {
          this.equipmentStates.forEach((item, index) => {
            if (index == param.data.index) {
              this.currentAddFile = item.iconFileId;
              item.iconFileId += "," + res;
            }
          });
        });
      }

      // this.fileData.push(); // append增加数据
      // this.fileList = fileList;
      // this.form.equipmentList[param.data].fileList.push(param.file)
    },
    getImageIndex(index) {
      console.log(index, "mmmmmmmmm");
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      // this.fileList = fileList;
      console.log(file, fileList, "uuuuuuuuuuuuuuu");
    },
    handleSuccess(response, file, fileList) {
      console.log(response, file, fileList, "文件上传成功");
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning("当前限制上传图标个数不超过2个");
    },

    //=======================================文件上传end===============================
  },
};
</script>
<style lang="less" scoped>
.getImageIndex {
  width: 100px;
  height: 100px;
  border: 1px solid red;
}
::v-deep .showUpload {
  .el-upload {
    display: none !important;
  }
}
::v-deep .el-upload--picture-card {
  width: 30px !important;
  height: 30px !important;
  // display: none;
}
::v-deep .el-col-4 {
  width: 15.666667%;
}
//  ::v-deep .el-form-item--medium .el-form-item__label{
//   width:96px!important;
//  }
//  ::v-deep .el-form-item--medium .el-form-item__content{
//   margin-left: 96px!important;
//  }
::v-deep .el-upload-list--picture-card .el-upload-list__item {
  width: 30px !important;
  height: 30px !important;
}
.dialogButton {
  width: 50px;
  height: 30px;
  padding: 0 10px;
  // border: solid 1px #ccc;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}
.addFormButton {
  height: 36px;
}
</style>
