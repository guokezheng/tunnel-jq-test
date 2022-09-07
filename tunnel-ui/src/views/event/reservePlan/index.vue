<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="事件类型" prop="planTypeId">
        <el-select v-model="queryParams.planTypeId" placeholder="请选择事件类型" clearable size="small">
          <el-option
                v-for="item in planTypeData"
                :key="item.id"
                :label="item.eventType"
                :value="item.id"/>
          </el-select>
      </el-form-item>

      <el-form-item label="预案名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入预案名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          @click="handleAdd()"
          v-hasPermi="['business:plan:add']"
        >新增</el-button>
      </el-col>
     <!-- <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:plan:edit']"
        >修改</el-button>
      </el-col> -->
     <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:plan:remove']"
        >删除</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:plan:export']"
        >导出</el-button>
      </el-col> -->
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table ref="planTable" v-loading="loading" :data="planList" @selection-change="handleSelectionChange" @row-click="handleRowClick">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!-- <el-table-column label="预案ID" align="center" prop="id" /> -->
      <el-table-column label="预案名称" align="center" prop="planName" />
      <el-table-column label="分区" align="center" prop="sdTunnelSubarea.sName" />
      <el-table-column label="事件类型" align="center" prop="eventType.eventType" />
      <el-table-column label="预案描述" align="left" prop="planDescription" width="200" >      
      <!-- <el-table-column label="查看工作台" align="left" prop="planDescription" width="200" /> -->
        <template slot-scope="scope">
          <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              :content="scope.row.planDescription">
              <div slot="reference" style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;cursor: default;">{{scope.row.planDescription}}</div>
            </el-popover>
        </template>
      </el-table-column>     

      <el-table-column label="相关文档" align="center" class-name="small-padding fixed-width">
         <template slot-scope="scope">
          <el-button  v-show="scope.row.planFileId !=null"
            size="mini"
            type="text"
            icon="el-icon-link"
            @click="openFileDrawer(scope.row)"
            style="cursor:pointer;"
          >点击查看</el-button>
          <div  v-show="scope.row.planFileId == null">
            无
          </div>
        </template>
      </el-table-column>
      <el-table-column label="相关策略" align="center"  class-name="small-padding fixed-width" width="200">
          <template slot-scope="scope">
            <span v-show="scope.row.strategyNames != null" 
                  style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;cursor: default;"
                  @click="showStrategyContent(scope.row)">
                 {{scope.row.strategyNames}}
            </span>
          <div  v-show="scope.row.strategyNames == null">
            无
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:plan:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:plan:remove']"
          >删除</el-button>
          <el-button 
            size="mini"
            type="text"
            icon="el-icon-guide"
            @click="chooseStrategyInfo(scope.row)"
            v-hasPermi="['business:plan:remove']"
          >执行策略</el-button>         
          <el-button 
            size="mini"
            type="text"
            icon="el-icon-guide"
            @click="openWorkbench(scope.row)"
            v-hasPermi="['business:plan:remove']"
          >预览</el-button>
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


     <el-drawer
       class="zwsj"
       :title="drawerFileTitle"
       :visible.sync="drawerFile"
       :direction="direction"
       :before-close="handleFileClose">
        <el-table v-loading="loading" :data="planFileList">
          <el-table-column label="序号" width="100px" align="center">
                 <template slot-scope="scope">
                     {{scope.$index+1}}
                 </template>
           </el-table-column>
          <el-table-column label="文件名称" align="center" prop="fileName" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                :loading="loadFileLoading"
                @click="loadFile(scope.row)"
                v-hasPermi="['business:plan:edit']"
              >下载</el-button>
            </template>
          </el-table-column>
        </el-table>
     </el-drawer>

    <!-- 执行策略选择窗口-->
    <el-dialog  :title="title" :visible.sync="handleStrategyVisible" width="60%" append-to-body>
     <div style="width: 100%;height: 31.25rem; overflow: auto;">
      <el-table ref="multipleTable" :data="handleStrategyList"  @selection-change="handleStrategySelectionChange" @row-click="multipleTableRowClick"
        empty-text="暂无策略" row-key="id">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="隧道名称" align="center" prop="tunnels.tunnelName" width="200" />
          <el-table-column label="策略名称" align="center" prop="strategyName" width="200"/>
         <el-table-column label="策略信息" align="center"  prop="slist" >
           <template slot-scope="scope">
               <div  v-for="(item,index) in scope.row.slist" :key="index" >
                 <span style="color: #005CBF;">
                   {{item}}
                 </span>
               </div>
           </template>
         </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="doStrategy">确定执行</el-button>
        <el-button  @click="doStrategyCancel"> 关  闭 </el-button>
      </div>
    </el-dialog>


    <!-- 相关策略策略，选择对话框-->
    <el-dialog  :title="addStrategyTitle" :visible.sync="addStrategyVisible" width="60%" append-to-body>
      <div style="width: 100%;height: 31.25rem; overflow: auto;">
        <el-table ref="addMultipleTable" :data="addStrategyList"  @selection-change="handleSelectionAddSChange" @row-click="addMultipleTableRowClick"
          empty-text="暂无策略" row-key="id">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="隧道名称" align="center" prop="tunnels.tunnelName" width="200" />
            <el-table-column label="策略名称" align="center" prop="strategyName" width="200"/>
            <el-table-column label="策略信息" align="center"  prop="slist" >
              <template slot-scope="scope">
                  <div  v-for="(item,index) in scope.row.slist" :key="index" >
                    <span style="color: #005CBF;">
                      {{item}}
                    </span>
                  </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      <div slot="footer">
        <el-button type="primary" @click="addStrategySubmit">确 认</el-button>
        <el-button  @click="addStrategyCancel">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="相关策略"
      :visible.sync="strategyDialog"
      width="30%"
      >
      <div v-for="(item,index) of str_arr" :key='index' style="font-size: 16px;line-height: 40px;padding-left: 20px;">
        {{item}}
      </div>
    </el-dialog>
     
      <!--  预览-->
    <el-dialog
        title="预览"
        :visible.sync="workbenchOpen"
         width="86.5%"
        :before-close="handleClose"              
         append-to-body
         >
         
      <img src="../../../assets/image/lane/3duan.png" alt="" class="chedaoImage"> 
        <!-- 设备图标-->
                  <div
                    class="icon-box mouseHover"
                    v-for="(item, index) in selectedIconList"
                    :key="index"
                    :style="{
                            left: item.position.left - 12 + 'px',
                            top: item.position.top + 52 +'px',
                            'z-index': item.eqType || item.eqType == 0 ? '' : '-1'
                          }"
                    :class="item.eqType == 7 || item.eqType == 8 || item.eqType == 9?'light-' + item.position.left:''"                
                  >
                     <div
                        v-show="(item.eqType != 7 &&

                                  item.eqType != 16 &&

                                  item.eqType != 15 &&
                                  item.eqType != 8 &&
                                  item.eqType != 9 &&
                                  item.eqType != 21 &&
                                  item.display == true) ||
                                ((item.eqType == 7 ||
                                  item.eqType == 8 ||
                                  item.eqType == 9 ||
                                  item.eqType == 21 ) &&
                                  item.display == true &&
                                  lightSwitch == 1)"
                        :class="{ focus: item.focus }"
                      >
                        <img
                          v-for="(url, indexs) in item.url"
                          style="position: relative;"
                          :style="item.eqType || item.eqType==0 ? 'cursor: pointer;' : ''"
                          :width="item.iconWidth"
                          :height="item.iconHeight"
                          :key="item.deptId + indexs"
                          :src="url"
                        />

                        <!-- 调光数值 -->
                        <label
                          style="
                                  color: yellow;
                                  position: absolute;
                                  left: 30px;
                                  bottom: 2px;
                                  pointer-events: none;
                                "
                          v-if="item.eqType == 21"
                        >{{ item.lightValue }}</label
                        >
                        <!-- CO/VI -->
                        <label
                          style="font-size:14px;position: absolute;color: #79e0a9; text-decoration:underline;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 19">
                          {{ item.value }}
                          <label v-if="item.eqType == 19" style="font-size:14px;">ppm</label> -->
                          <!-- <label v-if="item.eqType == 15" style="font-size:14px;">x10-3m<sup>-1</sup></label>-->
                        </label>
                        <!-- 风速风向 -->
                        <label
                          style="font-size:14px;position: absolute; text-decoration:underline;color:#79e0a9;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 17">
                          {{ item.value }}
                          <label v-if="item.eqType == 16" style="font-size:14px;">m/s</label>
                        </label>
                        <!-- 洞内洞外 -->
                        <label
                          style="font-size:14px;position: absolute;text-decoration:underline;color:#f2a520;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 5">
                          {{ item.value }}cd/m2
                        </label>  
                      </div>
                
                    <!-- 桩号 -->
                    <!-- <input
                      :class="[
                              item.eqType == 7 ||
                              item.eqType == 8 ||
                              item.eqType == 9 ||
                              item.eqType == 21
                                ? 's-config-img-input'
                                : 'config-img-input',
                            ]"
                        v-if="(item.display == true &&
                                displayNumb == true &&
                                item.eqType != 7 &&
                                item.eqType != 8 &&
                                item.eqType != 9 &&
                                item.eqType != 21) ||
                              ((item.eqType == 7 ||
                                item.eqType == 8 ||
                                item.eqType == 9 ||
                                item.eqType == 21) &&
                                item.display == true &&
                                lightSwitch == 1 &&
                                displayNumb == true)
                            "
                      v-show="item.eqType || item.eqType==0"
                      type="text"
                      v-model="item.pile"
                      disabled="true"
                      style="color: #055270;"
                    />
                    <div v-else style="width: 80px"></div> -->
                  </div>
          <el-steps :active="active" finish-status="success">
              <el-step title="步骤 1"></el-step>
              <el-step title="步骤 2"></el-step>
              <el-step title="步骤 3"></el-step>
            </el-steps>
        <span slot="footer" class="dialog-footer">
          <el-button @click="workbenchOpen = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
        
     </el-dialog>


    <!-- 新增弹窗 -->
     <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form ref="form1" :model="reservePlanDrawForm" :rules="rules" label-width="120px">
      <el-form-item label="所属隧道" prop="tunnelId">
              <el-select v-model="reservePlanDrawForm.tunnelId" placeholder="请选择所属隧道" style="width: 80%;" @change="changeSelection">
                <el-option v-for="item in eqTunnelData" :key="item.tunnelId" :label="item.tunnelName"
                  :value="item.tunnelId" ></el-option>
              </el-select>  
            </el-form-item>
            <el-form-item label="隧道分区" prop="sId">
              <el-select v-model="reservePlanDrawForm.sId" placeholder="请选择所属隧道" style="width: 80%;">
                <el-option v-for="item in eqTunnelDataList" :key="item.sName" :label="item.sName"
                  :value="item.sId"></el-option>
              </el-select>  
            </el-form-item>
        <el-form-item label="事件类型" prop="planTypeId">
          <el-select v-model="reservePlanDrawForm.planTypeId" placeholder="请选择事件类型" style="width: 80%;">
            <el-option
                v-for="item in planTypeData"
                :key="item.id"
                :label="item.eventType"
                :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="预案名称" prop="planName">
          <el-input style="width: 80%;" v-model="reservePlanDrawForm.planName" placeholder="请输入预案名称" />
        </el-form-item>
        <el-form-item label="预案描述" prop="planDescription">
          <el-input maxlength="250" style="width: 80%;" v-model="reservePlanDrawForm.planDescription" type="textarea" placeholder="请输入预案描述" />
        </el-form-item>
        <el-form-item label="相关文档" prop="eventLocation">
          <el-upload  style="width: 80%;"
            multiple
            class="upload-demo"
            ref="upload"
            :limit="5"
            action="http://xxx.xxx.xxx/personality/uploadExcel"
            :on-preview="handlePreview"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            :file-list="fileList"
            :http-request="uploadFile"
            :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 133px;" size="small" type="success" @click="submitUpload">上传到服务器
            </el-button>
          <span slot="tip" class="el-upload__tip" style="font-style: italic;color: red;padding-left:5%;">{{text}}</span>
          </el-upload>
        </el-form-item>
      </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelsubmitUpload">取 消</el-button>
          <el-button type="primary" @click="submitUpload">保 存</el-button>
        </div>
      </el-dialog>
      
 <!-- 配置策略 -->
 <el-dialog
        :title="addTitle"
        :visible.sync="strategyVisible"
         width="86.5%"
        :before-close="handleClose"              
         append-to-body
         >
        
         <el-form ref="form1" :model="reservePlanDrawForm" label-width="80px">
         <el-row  :gutter="20">
            <el-col :span="8" v-for="(item,index) in planTypeIdList" :key="index" class="colflex">
              <el-form-item label="相关策略" prop="planTypeId">
                <el-cascader 
                :value="item.a"        
                :options="options"
                @change="handleChangeStrategy()"></el-cascader>
              </el-form-item>           
                <div class="dialogButton" @click="addFrom()">添加</div>
             <div class="dialogButton" @click="updataDeleteForm(index)">删除</div>
              </el-col>      
            </el-row>   

         </el-form>
         <div>
          
       
         <img src="../../../assets/image/lane/3duan.png" alt="" class="chedaoImage"> 
        <!-- 设备图标-->
                  <div
                    class="icon-box mouseHover"
                    v-for="(item, index) in selectedIconList"
                    :key="index"
                    :style="{
                            left: item.position.left - 12 + 'px',
                            top: item.position.top + 104 +'px',
                            'z-index': item.eqType || item.eqType == 0 ? '' : '-1'
                          }"
                    :class="item.eqType == 7 || item.eqType == 8 || item.eqType == 9?'light-' + item.position.left:''"                
                  >
                     <div
                        v-show="(item.eqType != 7 &&

                                  item.eqType != 16 &&

                                  item.eqType != 15 &&
                                  item.eqType != 8 &&
                                  item.eqType != 9 &&
                                  item.eqType != 21 &&
                                  item.display == true) ||
                                ((item.eqType == 7 ||
                                  item.eqType == 8 ||
                                  item.eqType == 9 ||
                                  item.eqType == 21 ) &&
                                  item.display == true &&
                                  lightSwitch == 1)"
                        :class="{ focus: item.focus }"
                      >
                        <img
                          v-for="(url, indexs) in item.url"
                          style="position: relative;"
                          :style="item.eqType || item.eqType==0 ? 'cursor: pointer;' : ''"
                          :width="item.iconWidth"
                          :height="item.iconHeight"
                          :key="item.deptId + indexs"
                          :src="url"
                        />

                        <!-- 调光数值 -->
                        <label
                          style="
                                  color: yellow;
                                  position: absolute;
                                  left: 30px;
                                  bottom: 2px;
                                  pointer-events: none;
                                "
                          v-if="item.eqType == 21"
                        >{{ item.lightValue }}</label
                        >
                        <!-- CO/VI -->
                        <label
                          style="font-size:14px;position: absolute;color: #79e0a9; text-decoration:underline;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 19">
                          {{ item.value }}
                          <label v-if="item.eqType == 19" style="font-size:14px;">ppm</label> -->
                          <!-- <label v-if="item.eqType == 15" style="font-size:14px;">x10-3m<sup>-1</sup></label>-->
                        </label>
                        <!-- 风速风向 -->
                        <label
                          style="font-size:14px;position: absolute; text-decoration:underline;color:#79e0a9;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 17">
                          {{ item.value }}
                          <label v-if="item.eqType == 16" style="font-size:14px;">m/s</label>
                        </label>
                        <!-- 洞内洞外 -->
                        <label
                          style="font-size:14px;position: absolute;text-decoration:underline;color:#f2a520;padding-left: 5px;width: 100px;text-align: left;"
                          v-if="item.eqType == 5">
                          {{ item.value }}cd/m2
                        </label>  
                      </div>
                
                    <!-- 桩号 -->
                    <!-- <input
                      :class="[
                              item.eqType == 7 ||
                              item.eqType == 8 ||
                              item.eqType == 9 ||
                              item.eqType == 21
                                ? 's-config-img-input'
                                : 'config-img-input',
                            ]"
                        v-if="(item.display == true &&
                                displayNumb == true &&
                                item.eqType != 7 &&
                                item.eqType != 8 &&
                                item.eqType != 9 &&
                                item.eqType != 21) ||
                              ((item.eqType == 7 ||
                                item.eqType == 8 ||
                                item.eqType == 9 ||
                                item.eqType == 21) &&
                                item.display == true &&
                                lightSwitch == 1 &&
                                displayNumb == true)
                            "
                      v-show="item.eqType || item.eqType==0"
                      type="text"
                      v-model="item.pile"
                      disabled="true"
                      style="color: #055270;"
                    />
                    <div v-else style="width: 80px"></div> -->
                  </div>
                </div>
                  <el-form-item  style="text-align: right;width: 100%;">
          <el-button style="width: 10%;" type="primary" @click="submitstrategy">保存</el-button>
          <el-button style="width: 10%;"  @click="closeStrategy">取 消</el-button>
        </el-form-item>
     </el-dialog>







  </div>
</template>

<script>
import {
  listPlan,
  getPlan,
  delPlan,
  addPlan,
  updatePlan,
  addPlanFile,
  loadPlanFile,
  updatePlanFile,
  listStrategyByPlanId,
  partitionTunnel,
  tunnelNames
  } from "@/api/event/reservePlan";
import { listEventType } from "@/api/event/eventType";
import { listReservePlanFile } from "@/api/event/reservePlanFile";
import { download } from "@/utils/request";
import { listStrategy, getStrategy, handleStrategy } from "@/api/event/strategy";
import {
  listType,
  getTypeAndStrategy
} from "@/api/equipment/type/api.js"
import {
  getTunnels,
} from "@/api/equipment/tunnel/api.js";
import {
    listTunnels
  } from "@/api/equipment/tunnel/api";
import { fastLerp } from "zrender/lib/tool/color";
export default {
  name: "Plan",
  data() {
    return {
      //新增弹窗
      dialogFormVisible:false,
      visibleAdd:false,
      //配置策略
      strategyVisible:false,
      //策略数组
      planTypeIdList:[{
        a:[]
      }],
      title:"",
      addTitle:"",
      options: [
         {
          value: '',
          label: '',
          children:[
          {
            value: '',
            label: '',
          }
          ]
        }
      ],
      active: 0,
      //工作台
      workbenchOpen:false,
      str_arr:[],
      strategyDialog:false,//相关策略弹窗
      text:'*注*：上传文件不可超过1m',
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中关系ID数组
      rlIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预案信息表格数据
      planList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planTypeId: null,
        planName: null,
        planFileId: null,
        strategyId: null,
      },
      // 表单校验
      rules: {
        planName: [
          { required: true, message: '请输入预案名称', trigger: 'blur'},
          { min: 1, max: 30, message: '长度在1~30个字符之间', trigger: 'blur'},
        ],
        planTypeId: { required: true, message: '请选择事件类型', trigger: 'change'},
        tunnelId: { required: true, message: '请选择隧道类型', trigger: 'change'},
        sId: { required: true, message: '请选择分区隧道', trigger: 'change'},
        planDescription: { required: true, message: '请输入预案描述', trigger: 'blur'},
        eventLocation: {required: true, trigger: 'blur'},
        /* strategyNames: [{ required: true, trigger: 'blur'}],
        eventLocation: [{required: true, trigger: 'blur'}], */
      },
      //draw开关
      drawer:false,
      // 侧边层标题
      drawerTitle: "",
      //搜索-事件类型
      planTypeData:[],
      //from表单参数
      reservePlanDrawForm:{
        planTypeId:null,//事件类型
        planName:null,//预案名称
        planDescription:null,//预案描述
        strategyId:null,//多个策略ID
        strategyNames:null,//多个策略的名称，以：分割
        planFileId:null,
        tunnelId:null ,//隧道
        sId:null,//分区隧道
      },
      // 遮罩层
      dloading: false,
      //drawer方向
      direction: 'rtl',
      //策略数据
      strategyData:[],//策略下来数据信息
      fileData: '',  // 文件上传数据（多文件合一）
      fileList: [],   // upload多文件数组
      fileDrawForm:{},
      //相关文件drawer标题
      drawerFileTitle: "",
      //相关文件draw开关
      drawerFile:false,
      loadFileLoading: false,
      // 预案文件信息表格数据
      planFileList: [],
      // 遮罩层
      fileLoading: true,

      //需要移除的文件ids
      removeIds:[],
      //添加or编辑标志；add/edit
      planChangeSink: null,
      // 选择--需要执行的控制策略
      handleStrategyVisible:false,
      handleStrategyList:[],
      title:'',
      handleIds:[],


      //选择---新增控制策略
      addStrategyVisible:false,
      addStrategyList:[],
      addStrategyTitle:'',
      adsIds: [],
      multipleSelectionIds:[],//存放选择的id数组
      adsStrategyNames:[],
      // 非单个禁用
      adsSingle: true,
      // 非多个禁用
      adsMultiple: true,
      tunnelId:'JQ-JiNan-WenZuBei-MJY',
      selectedIconList:[],
      lightSwitch: 0,
      eqTunnelData:[] ,//隧道下拉
      eqTunnelDataList:[], //分区隧道下拉
      timer:null,//步骤条定时器
    };
  },
  created() {
    this.getList();
    this.getPlanType();//事件类型下拉
    // this.getStrategyInfo();//策略下拉
    this.getTunnelData(this.tunnelId)
    this.lightSwitchFunc()
    this.ceshiTime()
    tunnelNames().then(res=>{
           this.eqTunnelData=res.rows
           console.log(res,'this.eqTunnelDatathis.eqTunnelData')
          this.eqTunnelData.forEach(item=>{
          item.sdTunnelSubareas.forEach((item,index)=>{
            this.eqTunnelDataList.push(item)
           })          
         })
         console.log(this.eqTunnelDataList,'this.eqTunnelDataListthis.eqTunnelDataList')
        })
  },
  methods: {
   //点击了取消  
      cancelsubmitUpload(){
        this.dialogFormVisible = false
          this.resetReservePlanDrawForm()
      },

   
   
   
   
   
   
   
    //删除一行
    updataDeleteForm(index){
      if( this.planTypeIdList.length<=1){
        this.$modal.msgError('请保留一行')
        return
      }
      this.planTypeIdList.splice(index,1)
    },

    //添加一行
    addFrom(){
      this.planTypeIdList.push([{
        a:[]
      }])
    },
    ceshiTime(){     
     this.timer=setInterval(()=>{            
            if(this.active++ >2) this.active = 0            
        },1000)
    },
    showStrategyContent(row){
      this.strategyDialog = true
      this.str_arr = row.strategyNames.split("；");
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },
   
    getTunnelData(tunnelId) {     
      console.log(tunnelId,'参数餐宿参数擦擦是')

      let that = this;
      // that.upList = [];
      // that.downList = [];
      const params = {
        tunnelId: tunnelId
      }
      getTunnels(tunnelId).then((response) => {
        let res = response.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          console.log(res,"res")
          listType({isControl:1}).then((response) => {
            console.log(response,'response')
            var arr = []
            for(let item1 of response.rows) {
              for(let item of res.eqList){
                item.focus = false;
                if (item1.typeId ==item.eqType) {
                  item.iconWidth = Number(item1.iconWidth);
                  item.iconHeight = Number(item1.iconHeight);
                  arr.push(item)
                }
              }
             
            }
            this.selectedIconList = arr //这是最终需要挂载到页面上的值
            console.log(this.selectedIconList,"this.selectedIconList")
          }).then(() => {
          });
        } else {
          //不存在
          that.selectedIconList = [];
          //工作台默认背景图
          // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
    },
      //关闭策略弹窗
    closeStrategy(){
        this.strategyVisible=false
    },
    submitstrategy(){
      console.log(this.planTypeIdList,'cecceeecceeee')
    },
    handleChangeStrategy(e,index){
        console.log(e,index,'cecceeecceeee')
        this.planTypeIdList[index].a=e
        // for(let i=0; i<this.planTypeIdList.length; i++ ){
        //   this.planTypeIdList[i].a=e
        //   // console.log( this.planTypeIdList[i],' this.planTypeIdList[i] this.planTypeIdList[i] ')
        // }
        console.log( this.planTypeIdList,' this.planTypeIdList this.planTypeIdList')
    },

  //查看工作台
  openWorkbench(){
    this.workbenchOpen=true
  },

//=========================执行相关预案开始=====================
    //执行策略
   /* executionStrategy(row){
      this.$modal.msgSuccess("执行策略中.......");
      handleStrategy(row.strategyId);
    }, */
    // 选择将要执行的策略
    chooseStrategyInfo(row){
      // this.handleStrategyVisible = true;
      // this.title = "执行相关策略";
      // listStrategyByPlanId(row.id).then(response => {
      //   this.handleStrategyList = response.data;
      // });
      console.log(row,'ppppppppppppppp')
      this.strategyVisible=true
      // getTypeAndStrategy({isControl:1}).then(res=>{
      //     console.log(res.data,'之心测策略')
      //     this.options=res.data
      //     res.data.forEach((item,index)=>{
            
      //     })
      // })
    },
    //执行策略id勾选事件
    handleStrategySelectionChange(selection){
      this.handleIds = selection.map(item => item.id)
    },
    // 关闭 相关策略对话框
    doStrategyCancel(){
      this.handleStrategyVisible = false;
    },
    // 执行 选择的策略
    doStrategy(){
      if(this.handleIds.length>0){
        this.$modal.msgSuccess("执行策略中.......");
        for(let i=0;i < this.handleIds.length;i++){
          handleStrategy(this.handleIds[i]).then(response => {
            this.$modal.msgSuccess('执行成功')
            this.doStrategyCancel()
          }).catch(() => {
            this.$modal.msgError('执行失败')
          });
        }
      }else{
        this.$modal.msgError("请先选择需要执行的策略！");
      }
    },
//=========================执行相关预案结束=====================


//=========================选择相关预案开始=====================
      /* 跳至选择相关策略页面*/
      openAddStragegyDialog() {
        this.addStrategyVisible = true
        this.addStrategyTitle = "相关策略"
        listStrategy({strategyType:0}).then(response => {
          this.addStrategyList = response.rows;
          this.$refs.addMultipleTable.clearSelection();
          if(this.multipleSelectionIds !=null && this.multipleSelectionIds.length>0){
            let array = [];
            for ( let i = 0 ; i < this.addStrategyList.length ; i++ ){
                for ( let j = 0 ; j < this.multipleSelectionIds.length ; j++ ){
                  console.log("==="+j)
                  if(this.multipleSelectionIds[j] == this.addStrategyList[i].id){
                    array.push(this.addStrategyList[i]);
                  }
                }
            }
            //执行回显勾选
            this.$nextTick(() => {
              for ( let k = 0 ; k < array.length ; k++ ){
                this.$refs.addMultipleTable.toggleRowSelection(array[k],true);//这是默认选中上的
              }
            })
          }
        });
      },
      // 关闭控制策略对话框
      addStrategyCancel(){
        this.addStrategyVisible = false
      },
      // 选择,添加控制策略ids
      handleSelectionAddSChange(selection){
        this.adsIds = selection.map(item => item.id)
        this.adsStrategyNames = selection.map(item => item.strategyName)
        this.adsSingle = selection.length!==1
        this.adsMultiple = !selection.length
      },
      // 提交策略，暂存关闭对话框
      addStrategySubmit(){
        this.multipleSelectionIds = this.adsIds;//选中的策略id
        this.reservePlanDrawForm.strategyNames = ""
        this.reservePlanDrawForm.strategyId = ""
        for(let i=0;i<this.adsStrategyNames.length;i++){
          this.reservePlanDrawForm.strategyNames = this.reservePlanDrawForm.strategyNames + this.adsStrategyNames[i]+"；"
          this.reservePlanDrawForm.strategyId = this.reservePlanDrawForm.strategyId + this.adsIds[i]+"；"
        }
        this.addStrategyVisible = false
      },
 //===========================选择相关预案结束=========================
    //点击查看文件
    openFileDrawer(row) {
      console.log(row,'row');
      this.fileLoading = true;
      listReservePlanFile({planFileId : row.planFileId}).then(response => {
        this.drawerFileTitle = "相关文档";
        this.planFileList = response.rows;
        this.fileLoading = false;
        this.drawerFile = true;
      });
    },
    //下载文件
    async loadFile(row) {
      this.loadFileLoading = true
      await loadPlanFile(row.id, row.fileName)
      this.loadFileLoading = false
    },
    downfiles ( datas, filename ) {
      var data  = new Blob( [ datas ] )
      console.log( data )
      var downloadUrl = window.URL.createObjectURL(data)
      var anchor = document.createElement( 'a' )
      anchor.href = downloadUrl
      // 这里的filename 带有后缀，能决定文件的类型
      anchor.downolad =  filename
      anchor.click()
      window.URL.revokeObjectURL( data )
    },
    //点击文件触发
    handlePreview(file, fileList) {
      ///debugger
    },
    // 上传文件
    uploadFile(file) {
      console.log(file,"上传文件")
      this.fileData.append('file', file.file);  // append增加数据
    },
    //form表单置空
    resetReservePlanDrawForm() {
        this.reservePlanDrawForm.planTypeId = null;
        this.reservePlanDrawForm.planName = null;
        this.reservePlanDrawForm.strategyId = '';
        this.reservePlanDrawForm.strategyNames = null;
        this.reservePlanDrawForm.planDescription = null;
        this.reservePlanDrawForm.tunnelId = null;
        this.reservePlanDrawForm.sId = null;
        this.fileList = [];
        this.removeIds = [];
        this.planChangeSink = null;
        this.multipleSelectionIds = [];
        this.eqTunnelDataList=[]
    },
    // 上传到服务器
    async submitUpload() {
      console.log(this.reservePlanDrawForm.sId,'this.reservePlanDrawForm')
     if(!this.reservePlanDrawForm.planTypeId){
        this.$modal.msgError("请选择事件类型！");
        return;
     }
     if(!this.reservePlanDrawForm.planName){
        this.$modal.msgError("请输入预案名称！");
        return;
     }
     if (!this.reservePlanDrawForm.planDescription) {
       this.$modal.msgError("请输入预案描述！");
       return;
     }
    if (this.fileList.length === 0) {
        this.$message({
            message: '请先选择文件',
            type: 'warning'
        })
    } else {
      if(this.loading) return
      this.dloading = true
      let currentFileList = [];
      for ( var i = 0; i < this.fileList.length; i++){
        if(!this.fileList[i].hasOwnProperty("fId")){
          if(!this.fileList[i].size) this.fileList[i].size = 0 // 如果没有size，则给个0
          currentFileList.push(this.fileList[i]);
        }
      }
      const isLt100M = currentFileList.every(file => file.size / 1024 / 1024 < 1);
      if (!isLt100M) {
        this.$message.error('请检查，上传文件大小不能超过1MB!');
      } else {
        this.fileData = new FormData();  // new formData对象
        this.$refs.upload.submit();  // 提交调用uploadFile函数      
        this.fileData.append('planName', this.reservePlanDrawForm.planName);  // 预案名称
        this.fileData.append('planTypeId', this.reservePlanDrawForm.planTypeId);  // 事件类型
        this.fileData.append('planDescription', this.reservePlanDrawForm.planDescription==null ? "#^#" : this.reservePlanDrawForm.planDescription);  // 预案描述
        // this.fileData.append('strategyId', this.reservePlanDrawForm.strategyId==null? "-1" : this.reservePlanDrawForm.strategyId);  // 策略id
        this.fileData.append('tunnelId', this.reservePlanDrawForm.tunnelId);
        this.fileData.append('subareaId', Number(this.reservePlanDrawForm.sId));
        console.log(this.fileData,'this.fileDatathis.fileDatathis.fileData')
        if(this.planChangeSink == "add"){
          await addPlanFile(this.fileData).then((response) => {
                  if (response.code === 200) {
                    this.$modal.msgSuccess("保存成功");
                    // this.drawer = false;//关闭drawer窗体
                      this.dialogFormVisible=false
                    this.resetReservePlanDrawForm();//重置表单
                    //this.open = false;
                    this.getList();
                  }else{
                    this.$modal.msgError("保存失败");
                  }
                });
        }else if( this.planChangeSink == "edit"){
            this.fileData.append('id', this.reservePlanDrawForm.id);
            this.fileData.append('removeIds', this.removeIds);
            this.fileData.append('planFileId', this.reservePlanDrawForm.planFileId);

            await updatePlanFile(this.fileData).then((response) => {
                    if (response.code === 200) {
                      this.$modal.msgSuccess("修改成功");
                      // this.drawer = false;//关闭drawer窗体
                      this.dialogFormVisible=false
                      this.resetReservePlanDrawForm();//重置表单
                      //this.open = false;
                      this.getList();
                    }else{
                      this.$modal.msgError("修改失败");
                    }
                  });
        }
        this.multipleSelectionIds = [];
      }
      this.dloading = false
    }
  },
      /** drawer-form表单，取消操作 **/
      drawerClose() {
        this.$refs['form1'].clearValidate();
        this.resetReservePlanDrawForm();
        this.drawer = false;
        this.dloading=false
        this.addForm=false
      },
      /** 新增按钮操作 **/
      handleAdd() {
     console.log(this.$refs['form1'],'this.$refs["form1"]')
            
          //  this.$nextTick(() => {
          //       this.$refs["form1"].clearValidate();
          //   });
        // this.resetReservePlanDrawForm();  
        this.title = "新增预案";
        this.planChangeSink = "add";
        this.dialogFormVisible=true
       
        this.visibleAdd=true
        // this.addForm=true
          this.$refs["form1"].clearValidate();
      },
      changeSelection(e){
        console.log(e,'indexindex')
         this.eqTunnelData.forEach(item=>{
           if(item.tunnelId==e){
           this.eqTunnelDataList=item.sdTunnelSubareas
           }
         })
         console.log( this.eqTunnelDataList,' this.eqTunnelDataList this.eqTunnelDataList')
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.resetReservePlanDrawForm();
        this.planChangeSink = "edit";
        const id = row.id || this.ids
        tunnelNames().then(res=>{
           this.eqTunnelData=res.rows
           console.log(res,'this.eqTunnelDatathis.eqTunnelData')
          this.eqTunnelData.forEach(item=>{
        item.sdTunnelSubareas.forEach((item,index)=>{
            this.eqTunnelDataList.push(item)
           })          
         })
         console.log(this.eqTunnelDataList,'this.eqTunnelDataListthis.eqTunnelDataList')
        })
        getPlan(id).then(response => {
          console.log(response,"response回显")
          this.fileList = [];
          this.reservePlanDrawForm = response.data;
          this.reservePlanDrawForm.tunnelId=response.data.sdTunnels.tunnelId
          this.reservePlanDrawForm.sId=response.data.sdTunnelSubarea.sId
 
          if(this.reservePlanDrawForm.strategyId != -1 && this.reservePlanDrawForm.strategyId != "-1" && this.reservePlanDrawForm.strategyId != null){
            this.multipleSelectionIds = this.reservePlanDrawForm.strategyId.split("；");
          }

          let fileInfo = response.data.pFileList;
          for ( var i = 0; i < fileInfo.length; i++){
              let fileModel = {};
              fileModel.name = fileInfo[i].fileName;
              fileModel.url = fileInfo[i].url;
              fileModel.fId = fileInfo[i].id;
              this.fileList.push(fileModel);
          }
          //文件回显
         
        });
         // this.drawer = true;
         this.$nextTick(()=>{
            this.$refs['form1'].resetFields();
          })
          this.$nextTick(() => {
            this.$refs["form1"].clearValidate();
        });
          this.dialogFormVisible=true
          this.title = "修改预案信息";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        /* debugger */
        const ids = row.id || this.ids;
        //  const rlIds = row.id || this.rlIds;
        this.$confirm('是否确认删除?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delPlan(ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(function() {});
      },
        //移除文件
        handleRemove(file, fileList) {
            if(file.hasOwnProperty("fId")){
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
            let number = [];
            fileList.forEach((item,index)=>{
              if(item.name.length>20){
                number.push({
                  zxc:index
                })
                fileList.splice(index,1);
              }
            })
            if(number.length>0){
              this.text = '* 文件名不得超过 20 个字符  !'
            }  
            this.fileList = fileList;
        },
        /** 查询相关策略下拉列表 */
        getStrategyInfo() {
          listStrategy({strategyType:'0'}).then(response => {
          this.strategyData = response.rows;
          });
        },
        /** 查询事件类型下拉列表 */
        getPlanType() {
          listEventType().then(response => {
            console.log(response,'事件类型下拉')
          this.planTypeData = response.rows;
          });
        },
        //关闭drawer
        handleClose(done) {
            // this.$confirm('确认关闭？')
            //   .then(_ => {
            //     done();
            //   })
            //   .catch(_ => {});
            // console.log(done )
            done();
            this.$refs['form1'].resetFields()
         },
         //关闭drawer
         handleFileClose(done) {
          // console.log(done,'donedone')
              done();
          },
        /** 查询预案信息列表 */
        getList() {
          this.loading = true;
          listPlan(this.queryParams).then(response => {
            console.log(response,'responseresponse')
            this.planList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
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
          this.rlIds = selection.map(item => item.planFileId)
          this.single = selection.length!==1
          this.multiple = !selection.length
        },
        // 点击某一行，将其选中(表格)
        handleRowClick(row,i,a) {
          this.$refs.planTable.toggleRowSelection(row)
        },
        // 点击某一行，将其选中(执行策略弹窗)
        multipleTableRowClick(row) {
          this.$refs.multipleTable.toggleRowSelection(row)
        },
        // 点击某一行，将其选中(相关策略弹窗)
        addMultipleTableRowClick(row) {
          this.$refs.addMultipleTable.toggleRowSelection(row)
        }
    /** 提交按钮 */
    /* submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlan(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addPlan(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    }, */
  }
};
</script>
<style scoped lang="scss">
  .colflex{
    display: flex;
  }
 .el-drawer__header{
       background: #dcdfe6;
       padding: 0 10px;
       height: 58px;
       font-size: 1.125rem;
  }
  .workbench{
    background-image: url('../../../assets/image/lane/3duan.png');
  }
  .chedaoImage{
      // position: relative;
      width: 1620px;
      height: 580px;
      margin-bottom: 20px;
  }
.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  /* // align-items: center; */
  width: 40px !important;
}
 .el-dialog-div{
    height: 80vh;
     overflow: auto;
    }
// .mouseHover {
//   &:hover {
//     z-index: 10;

//     input {
//       cursor: pointer;
//       background-color: #eee;
//       color: #000;
//     }
//   }
// }
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
  margin-left: 20px;
  // margin-right: 10px;
}
.flexWrap{
flex-wrap: nowrap;
}
</style>
