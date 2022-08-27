<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true"  label-width="68px">
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select
          v-model="queryParams.tunnelId"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelName"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="发生时间" prop="occurTime">
        <el-date-picker
          v-model="queryParams.params.occurTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          unlink-panels
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="事件类型" prop="incidentType">
        <el-select
          v-model="queryParams.incidentType"
          placeholder="请选择事件类型"
          clearable
          size="small"
        >
          <el-option
           v-for="dict in dict.type.incident_type"
           :key="dict.value"
           :label="dict.label"
           :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['trafficFlowInformation:information:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['trafficFlowInformation:information:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['trafficFlowInformation:information:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          :loading="exportLoading"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['trafficFlowInformation:information:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="事件类型" align="center" prop="incidentType" >
          <template slot-scope="scope">
              <dict-tag :options="dict.type.incident_type" :value="scope.row.incidentType"/>
          </template>
      </el-table-column>
      <el-table-column label="事件级别" align="center" prop="incidentGrade" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_incident_level" :value="scope.row.incidentGrade"/>
        </template>
      </el-table-column>
      <el-table-column label="发生时间" align="center" prop="occurTime" width="180" />
      <el-table-column label="方向" align="center" prop="direction" >
          <template slot-scope="scope">
              <dict-tag :options="dict.type.sd_direction_list" :value="scope.row.direction"/>
          </template>
      </el-table-column>
      <!--<el-table-column label="事件内容" align="center" prop="incidentContent" />-->
      <el-table-column label="管控类别" align="center" prop="controlType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_control_type" :value="scope.row.controlType"/>
        </template>
      </el-table-column>
      <el-table-column label="管控等级" align="center" prop="controlLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sd_control_level" :value="scope.row.controlLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="管控措施" align="center" prop="measureDetail" >
      </el-table-column>

      <el-table-column label="详情展示" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-data-analysis"
            @click="viewDetails(scope.row)"
            v-hasPermi="['trafficFlowInformation:information:edit']"
          >查看详情</el-button>

        </template>
      </el-table-column>
      <el-table-column label="信息展示" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-files"
            @click="InformationBoard(scope.row)"
            v-hasPermi="['trafficFlowInformation:information:edit']"
          >提交情报板</el-button>

        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-add"
            @click="handleMeasures(scope.row)"
            v-hasPermi="['system:config:edit']"
          >添加详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['trafficEmergencies:emergencies:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['trafficEmergencies:emergencies:remove']"
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
    <!-- 添加或修改交通流量信息对话框 -->
    <el-dialog title="信息管理" :visible.sync="openInformation" width="500px" append-to-body class="inforDialog">
      <div v-if="formMeasures.eveList.length > 0">
        <div v-for="(evtItem,index) in formMeasures.eveList">
          <div>{{evtItem.processTime}}</div>
          <div>{{evtItem.processDesc}}</div>
          <div v-for="(imgItem,imgIndex) in evtItem.imgList">
            <img :src="imgItem.imgBase64" />
          </div>
        </div>
      </div>
      <div v-else>
        无事件详情
      </div>
    </el-dialog>
    <el-dialog title="" :visible.sync="openPic" width="1000px" append-to-body class="picDialog">
       <img src="../../../../../assets/fender_bender.jpg" @click="bigPic"/>
    </el-dialog>
    <el-dialog title="情报板信息" :visible.sync="openBoard" width="500px" append-to-body class="boardDialog">
        <el-form :model="boardForm" :inline="true" ref='boardForm'>
            <el-form-item label="事件信息">
                <el-input type="textarea" v-model="boardForm.evtInformation" ></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm('board')">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>
    <!-- 添加或修改交通流量信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道名称" prop="tunnelId">
              <el-select
                v-model="form.tunnelId"
                placeholder="请选择隧道名称"
                clearable
                size="small"
              >
                <el-option
                  v-for="item in tunnelData"
                  :key="item.tunnelName"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发生时间" prop="occurTime">
              <el-date-picker clearable size="small"
                              v-model="form.occurTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="选择发生时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="事件类型" prop="incidentType" >
              <el-select
                v-model="form.incidentType"
                placeholder="请选择事件类型"
                clearable
                size="small"
                @change="incidentTypeChange"
              >
                <el-option
                  v-for="dict in dict.type.incident_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件级别" prop="incidentGrade" >
              <el-select
                v-model="form.incidentGrade"
                placeholder="请选择事件级别"
                clearable
                size="small"
                @change="incidentGradeChange"
              >
                <el-option
                  v-for="dict in dict.type.sd_incident_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="统计时间" prop="reportTime">
              <el-date-picker clearable size="small"
                              v-model="form.reportTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="选择统计时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方向" prop="direction">
              <el-select
                v-model="form.direction"
                placeholder="请选择方向"
                clearable
                size="small"
              >
                <el-option
                  v-for="dict in dict.type.sd_direction_list"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
       <el-row>
         <el-form-item label="事件内容" prop="incidentContent">
           <el-input  type="textarea" v-model="form.incidentContent" maxlength="255" placeholder="请输入事件内容" />
           <!--<div style="float: right;color: #ccc;font-size: 12px;">最多输入255个字</div>-->
         </el-form-item>
       </el-row>

        <el-divider></el-divider>
        <div class="titleFontStyle titleStyle">
        <!--主动交通流推送的-->
          管控措施
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="管控类别" prop="controlType">
              <el-select v-model="form.controlType" clearable size="small" @change="controlTypeSelectChange">
                <el-option
                  v-for="(dict,index) in dict.type.sd_control_type"
                  :key="index"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管控级别" prop="controlLevel">
              <el-select v-model="form.controlLevel" clearable size="small" @change="controlLevelSelectChange">
                <el-option
                  v-for="(dict,index) in dict.type.sd_control_level"
                  :key="index"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="措施详情" prop="measureDetail" class="remarksTextarea" style="width: 100%;">
          <el-input type="textarea" v-model="form.measureDetail" readonly></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('add')">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 配置措施 弹窗 -->
    <el-dialog :title="title" :visible.sync="openMeasures" width="500px" append-to-body class="boardDialog">
        <div style="float: right;margin-top: -0.6vh;">
          <el-button type="primary" round size="mini" @click="addMeasuresHandle()">+事件详情</el-button>
          <!-- <el-button type="primary" round size="mini" @click="addMeasuresHandle(2)">+图片</el-button> -->
        </div>

        <el-form :model="formMeasures" v-for="(evtItem,index) in formMeasures.eveList">
            <el-form-item :label="label" prop="eventContent">
              <div>{{evtItem.processTime}}
                <i style="float: right;margin-right: 20px;margin-top: 10px;cursor: pointer;"
                    class="el-icon-circle-close" @click="removeEvtItem(evtItem)"></i>
              </div>
              <el-input v-model="evtItem.processDesc" placeholder="请输入事件内容" @input='textMax'/>
              <span  style="font-style: italic;color: #acacac;float: right;">{{evtItem.processDesc.length}}/255</span>
              <el-upload
                style="width: 80%;"
                multiple
                class="upload-demo"
                ref="upload"
                :limit="50"
                :headers="headers"
                :action="uploadUrl"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :on-exceed="handleExceed"
                :on-change="handleChange"
                :on-success="handleFileSuccess"
                :file-list="evtItem.imgList==null?[]:evtItem.imgList"
                :auto-upload="true"
                accept=".jpg,.jpeg,.png"
                :before-upload="beforeUpload"
              >
                <el-button slot="trigger" size="small" type="primary" @click="zxc(index)">选取文件</el-button>
                <span slot="tip" class="el-upload__tip" style="font-style: italic;color: #acacac;padding-left:5%;">*注*：上传文件不可超过100m</span>
              </el-upload>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm('measures')" :disabled="disabled">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>
  </div>
</template>
<script>
import { getList, addAccident, getAccident, updateAccident, delInformation, saveProcessList, getProcessList,getPublishContent}
  from "@/api/trafficOperationControl/trafficOperationStatus/trafficOperationStatePrediction/prediction/prediction";
import {getActiveTrafficMeasure,getActiveTrafficMeasureByTunnelId,getControlMeasureByTypeLevel,getActiveMeasureWithEmergencyIncident} from
    "@/api/trafficOperationControl/activeTrafficFlow/activeTrafficFlow";
import { listTunnels } from "@/api/equipment/tunnel/api";
import * as echarts from "echarts";
import ImageUpload from '@/components/ImageUpload';
import { getToken } from "@/utils/auth";
import moment from "moment";
export default {
    dicts: ['incident_type', 'sd_direction_list',"sd_control_type","sd_control_level","sd_incident_level"],
    components: {
      ImageUpload,
    },
    data(){
      return{
        disabled:false,
        uploadUrl: process.env.VUE_APP_BASE_API + "/traffic/image/uploadMultiImage", // 上传的图片服务器地址
        headers: {
          Authorization: "Bearer " + getToken()
        },
        index:0,
        // headers:{ Authorization: "Bearer " + getToken() },
        // action:process.env.VUE_APP_BASE_API + "/system/devices/importData",
        // 文件上传数据（多文件合一）
        fileData:'',
        // fileList: [],
        //需要移除的文件ids
        removeIds:[],
        openMeasures:false,
        // 遮罩层
        loading: false,
        // 弹窗 信息管理
        openInformation:false,
        // 弹窗 提交情报版
        openBoard:false,
        // 弹窗 大图片
        openPic:false,
        // 总条数
        total: 0,
        // 选中数组
        ids: [],
        // 隧道名称
        tunnelData:{},
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 情报版弹窗 form表单
        boardForm:{
          evtInformation:""
            // evtInformation:'一级管控，2022.02.10 17:08:33 K470-K550(龙门隧道附近)，检测有积雪，请货车于该路段限速30km/h',
            // checkList:[],
        },
        // 配置弹窗内 新增事件内容表单
        formMeasures:{
           // eventContent:"",
           measuresId:'',
           eveList:[],
        },
        // 新增 修改 弹窗表单
        form:{
            id:null,
            tunnelId:'',
            reportTime:'',
            incidentType:'',
            incidentContent:'',
            occurTime:"",
            direction:'',
            //管控措施配置字段
            controlType:"",
            controlLevel:"",
            measureList:[],
            measureDetail:"",
        },
        informationList:[],
        // 新增 修改 弹窗
        open:false,
        // 新增 修改的弹窗名
        title:'',
        // 自动显示新增事件的当前时间
        label:'',
        // 表单校验
        rules: {
          tunnelId:[
            { required: true, message: "请选择隧道", trigger: "change", },
          ],
          reportTime: [
            //加上 type:'date'保存报错
            { required: true, message: "请选择统计时间", trigger: "blur"},
          ],
          incidentType: [
            { required: true, message: "请选择事件类型", trigger: "change", },
          ],
          incidentGrade: [
            { required: true, message: "请选择事件级别", trigger: "change", },
          ],
          occurTime: [
            { required: true, message: "请选择发生时间", trigger: "blur"},
          ],
          direction:[
            { required: true, message: "请选择方向", trigger: "change", },
          ],
          controlType:[
            { required: true, message: "请选择管控类别", trigger: "change", },
          ],
          controlLevel:[
            { required: true, message: "请选择管控等级", trigger: "change", },
          ]
        },
        queryParams: {
          tunnelId:"",
          incidentType:'',
          pageNum: 1,
          pageSize: 10,
          params:{
            reportTime:[]
          }
        },
        isClick:true
      }
    },
    created() {
      // this.getList();
      this.getTunnels();
    },
    methods: {
      // 上传成功之后的回调
      handleFileSuccess(response,file, fileList){
        // console.log(file,fileList,"file")
        if(fileList.length>0){
          let existFile = fileList.slice(0, fileList.length - 1).find(f => f.name === file.name);
          if (existFile) {
              this.$modal.msgError("当前文件已经存在");
              fileList.pop();
          }
        }
        // this.fileList = fileList;
        // file.image.name = file.image.imgName

        // for(var item of this.formMeasures.eveList){
        //   if(item.imgList == null){
        //     item.imgList = []
        //     this.formMeasures.eveList[this.index].imgList.push(file.image);
        // }
        // }

        for(var item of fileList){
          if(item.response){
            item.url = item.response.image.imgUrl;
            item.imgName = item.response.image.imgName;
            item.imgUrl = item.response.image.imgUrl;
          }
        }
        this.formMeasures.eveList[this.index].imgList = fileList;
      },
      //上传图片前校验格式、大小
      beforeUpload(file) {
        const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);

        const whiteList = ["jpg","jpeg","png"];

        if (whiteList.indexOf(fileSuffix) === -1) {
          this.$message.error('上传图片只能是jpg、jpeg、png格式');
          return false;
        }

        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isLt2M) {
          this.$message.error('上传图片大小不能超过2MB');
          return false;
        }
      },
      getTunnels() {
        listTunnels().then((response) => {
           this.tunnelData = response.rows
           this.getList()
        });
      },
      /** 查询交通流量信息列表 */
      getList() {
        this.loading = true;
        getList(this.queryParams).then(response => {
          this.informationList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 信息管理 查看详情按钮 弹窗
      viewDetails(row){
          console.log(row,"row")
          this.formMeasures.eveList = []
          this.openInformation = true
          const params = {
            incidentId : row.id
          }
          getProcessList(params).then(response => {
            console.log(response,"response")
            for(var item of response.data){
              console.log(item,"item")
              if(item.imgList != null){
                for(var imgItem of item.imgList){
                  imgItem.name = imgItem.imgName
                  console.log(imgItem,"imgItem")
                }
              }

            }
            this.formMeasures.eveList = response.data;
            // this.openMeasures = true;

          })
      },
      textMax(text){
        console.log(text,"text")
        if(text.length>255){
          this.$modal.msgWarning('最多输入255个字')
          this.disabled = true
        }else{
          this.disabled = false
        }
      },
      // 信息展示 提交情报版按钮 弹窗
      InformationBoard(row){
        const ids = row.id || this.ids;
        //获取事件信息-发布内容
        getPublishContent(ids).then(response => {
          var content = response.data.content;
          this.boardForm.evtInformation = content;
        });

        this.openBoard = true;
      },
      // 配置详情 按钮
      handleMeasures(row){
          this.openMeasures = true;
          this.title = "配置详情";
          this.formMeasures.measuresId = row.id
          const params = {
            incidentId : row.id
          }
          getProcessList(params).then(response => {
            console.log(response,"response添加详情")
            for(var evtItem of response.data){
              console.log(evtItem,"evtItem")
              if(evtItem.imgList != null){
                for(var imgItem of evtItem.imgList){
                  imgItem.name = imgItem.imgName
                  console.log(imgItem,"imgItem")
                }
              }

            }
            this.formMeasures.eveList = response.data;
            // this.openMeasures = true;

          })
      },
      // 配置详情内 添加记录按钮
      addMeasuresHandle(){
          let now = new Date()
          console.log(now,"now")
          let date = moment(now).format("YYYY-MM-DD HH:mm:ss")
          // let date = moment(now).utc().format('YYYY-MM-DD HH:MM:SS')
          console.log(date,"date")
          let item = {
              processDesc:'',
              imgList:[],
              processTime:date
            }
          this.formMeasures.eveList.push(item);
          console.log(this.formMeasures.eveList,'this.formMeasures.eveList')
          this.openMeasures = true;
      },

      bigPic(){
          this.openPic = true
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 提交按钮 */
      submitForm(type) {
          if(type == 'board'){
              this.$modal.msgWarning("开发中");
              return;
              // this.$refs["boardForm"].validate(valid => {
              //   if (valid) {
              //       console.log(this.boardForm,"valid")
              //       if(this.boardForm.evtInformation != ''){
              //           updateInformation(this.boardForm).then(response => {
              //             this.$modal.msgSuccess("发布成功");
              //             this.openBoard = false;
              //             this.getList();
              //           });
              //       }else{
              //       this.$modal.msgError("发布失败");
              //       }
              //   }
              // });
          }
          else if(type == 'measures'){
              var verifyFlag = true;
              if(this.formMeasures.eveList.length != 0){
                this.formMeasures.eveList.forEach(item => {
                    if(item.processDesc == '' && item.imgList.length == 0){
                        this.$modal.msgWarning('请输入事件内容或上传图片');
                        verifyFlag = false;
                    }
                });
              }
              if(verifyFlag){
                saveProcessList(this.formMeasures).then((res) =>{
                  if(this.formMeasures.eveList.length == 0){
                    this.$modal.msgWarning('无内容，返回上一级');
                  }else{
                    this.$modal.msgSuccess('发布成功');
                  }
                  this.openMeasures = false;
                }).catch(() =>{
                  this.$modal.msgWarning('发布失败');
                });
              }
          }
          else if(type == 'add'){
              this.$refs["form"].validate(valid => {
                if (valid) {
                    if(this.form.id == null){
                      //避免重复多次点击新增按钮
                      if(this.isClick){
                        addAccident(this.form).then(response => {
                          this.$modal.msgSuccess("新增成功");
                          this.open = false;
                          this.getList();
                          this.loading = false
                        });

                        this.isClick = false;
                        setTimeout(function(that){
                          that.isClick = true;
                        },2000,this);
                      }

                    }else{
                        console.log(this.form,'修改form表单')
                        updateAccident(this.form).then(response => {
                          this.$modal.msgSuccess("修改成功");
                          this.open = false;
                          this.getList();
                          this.loading = false
                        });
                    }
                }
              });
          }
        // }
      },
      zxc(index){
        this.index = index
      },
      removeEvtItem(evtItem){
        this.formMeasures.eveList.splice(this.formMeasures.eveList.indexOf(evtItem),1)//this.arr.indexOf(item) 可以找到它的下标
      },
      // 取消按钮
      cancel() {
        this.openMeasures = false
        this.openBoard = false;
        this.open = false;
        this.resetQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.resetQuery();
        this.open = true;
        this.title = "添加交通突发事件信息";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        console.log(row,"row")
        this.resetQuery();
        const id = row.id || this.ids
        getAccident(id).then(response => {
          console.log(response,"response")
          this.form = response.data;
          this.open = true;
          this.title = "修改交通突发事件信息";


          if(response.data.hasOwnProperty("measureList")){
            //回显措施数据
            this.form.controlType = response.data.levelConfig.controlType;
            this.form.controlLevel = response.data.levelConfig.controlLevel;
            this.form.measureList = response.data.measureList;
            this.form.measureDetail = response.data.measureDetail;

          }else{
            //没有措施数据，获取主动交通流推送的措施
            this.getMeasureData(id);
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除该条突发事件信息？').then(function() {
          return delInformation(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 重置按钮操作 */
      resetQuery() {
          this.form = {
              tunnelId:'',
              reportTime:'',
              incidentType:'',
              incidentContent:'',
              direction:'',
            controlType:"",
            controlLevel:"",
            measureList:[],
            measureDetail:""
          }
          this.queryParams = {
                tunnelId:"",
                eventId:'',
                pageNum: 1,
                pageSize: 10,
                dateRange:[],
                params:{
                  reportTime:[]
                }
          }

        this.resetForm("queryForm");
        this.resetForm("form");
        this.handleQuery();
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
    //点击文件触发
    handlePreview(file, fileList) {
      console.log(file, fileList,"点击文件触发")
      ///debugger
    },
    //监控上传文件列表
    handleChange(file, fileList) {
      console.log(file, fileList,"监控上传文件列表")


    },
    //移除文件
    handleRemove(file, fileList) {
      // console.log(file, fileList,"移除文件")
      //   if(file.hasOwnProperty("fId")){
      //     this.removeIds.push(file.fId);
      //   }
      // this.fileList = fileList;
      // return this.$confirm(`确定移除 ${ file.name }？`);

      for(var item of this.formMeasures.eveList){
        if(item.imgList && item.imgList.length > 0){
          for(var imgFile of item.imgList){
            if(imgFile.uid === file.uid){
              item.imgList.splice(item.imgList.indexOf(imgFile),1);
             return;
            }
          }
        }
      }
    },
    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      console.log(file, fileList,"选取文件超过数量提示")
        this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
      //事件类型改变事件
      incidentTypeChange(){
        //重新获取推送措施
        var incidentType = this.form.incidentType;
        var incidentGrade = this.form.incidentGrade;
        if(!incidentType || !incidentGrade){
          this.form.measureDetail = "";
          this.form.controlLevel = "";
          this.form.controlType = "";
          return;
        }

        this.getActiveMeasure();

      },
      //事件级别改变事件
      incidentGradeChange(){
        //重新获取推送措施
        this.incidentTypeChange();
      },
      //获取主动交通流推送措施
      getActiveMeasure(){
        var incidentType = this.form.incidentType;
        var incidentGrade = this.form.incidentGrade;
        var param = {incidentType:incidentType,incidentGrade:incidentGrade};
        getActiveMeasureWithEmergencyIncident(param).then(response => {
          this.measureDataCallback(response);
        });
      },
      //获取到措施数据后的回调
      measureDataCallback(response){
        if(!response.data || Object.keys(response.data).length === 0){
          this.form.controlLevel = "";
          this.form.controlType = "";
          this.form.measureDetail = "";
          return;
        }

        var measureData = response.data;

        if(measureData.hasOwnProperty("measureList")){
          // this.getMeasureDetail(measureData.measureList);
          this.form.measureList = measureData.measureList;
          this.form.measureDetail = measureData.measureDetail;
        }else{
          this.form.measureDetail = "";
        }
        if(measureData.hasOwnProperty("levelConfig")){
          this.form.controlLevel = measureData.levelConfig.controlLevel;
          this.form.controlType = measureData.levelConfig.controlType;
        }else{
          this.form.controlLevel = "";
          this.form.controlType = "";
        }

      },

      //管控类别下拉框改变事件
      controlTypeSelectChange(){
        this.controlLevelSelectChange();
      },
      //管控级别下拉框改变事件
      controlLevelSelectChange(){
        //获取措施详情
        var controlType = this.form.controlType;
        var controlLevel = this.form.controlLevel;

        if(!controlType || !controlLevel){
          this.form.measureDetail = "";
          return;
        }
        var param = {
          controlType:controlType,
          controlLevel:controlLevel
        };

        getControlMeasureByTypeLevel(param).then(response => {
          var measureList = response.data.measureList;
          var measureDetail = response.data.measureDetail;
          if(measureList && measureList.length > 0){
            this.form.measureList = measureList;
            this.form.measureDetail = measureDetail;
          }else{
            this.form.measureDetail = "";
          }
        });

      },
   }
}
</script>
<style scoped lang="scss">
    ::v-deep .el-dialog{
        .el-dialog__body{
            padding: 10px 40px 30px 40px;
            >div{
                line-height: 25px;
                font-size: 18px;
                margin-bottom: 10px;
                >div{
                  >div:first-of-type{
                    color: #b4b4b4;
                    font-size: 16px;
                  }
                }
            }
            // >div:nth-of-type(even){
            //     text-indent: 20px;
            // }
        }

        .el-form--inline .el-form-item{
            width: 100%;
        }
        .el-form-item--medium .el-form-item__content{
            width: 80%;
        }
        textarea{
            min-height: 100px !important;
        }
    }
    .inforDialog{
        img{
            width: 80%;
            margin: 0 auto;
        }
    }
    .boardDialog{
        img{
            width: 20px;

        }
    }
    .picDialog{
        img{
            width: 100%;
            height: auto;
        }
    }

    /*推送tab标签样式*/
    .tabStyle{
      padding: 30px 40px;
    }
    .titleFontStyle{
      font-size: 15px;
      font-weight: bold;
    }
    .titleStyle{
      margin: 30px 0;
    }
</style>
