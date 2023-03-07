<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb20">
      <!-- 分组数据 -->
      <el-col :span="4" :xs="24">
       <div class="head-container">
          <el-input
            v-model="grounpName"
            placeholder="请输入分组名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            class="filter-tree"
            :data="grounpOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!-- 设备数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="所属产品" prop="productKey" clearable>
            <el-input ref="procductName" size="small" v-model="queryParams.productName" @click.native="productDrawer()" placeholder="请选择所属产品" />
          </el-form-item>
          <el-form-item label="所属分组" prop="nodeType">
            <el-select v-model="queryParams.groupKey" placeholder="请选择所属分组" clearable size="small">
              <el-option
                v-for="item in grounpList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备名称" prop="deviceName">
            <el-input
              v-model="queryParams.deviceName"
              placeholder="请输入设备名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备编号" prop="deviceCode">
            <el-input
              v-model="queryParams.deviceCode"
              placeholder="请输入设备编号"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="在线状态" prop="onlineState">
            <el-select v-model="queryParams.onlineState" placeholder="请选择在线状态" clearable size="small">
              <el-option
                v-for="dict in onlineStateOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="启用状态" prop="enabled">
            <el-select v-model="queryParams.enabled" placeholder="请选择启用状态" clearable size="small">
              <el-option
                v-for="dict in enabledOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
                size="small"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="IP地址" prop="ipAddr">
            <el-input
              v-model="queryParams.ipAddr"
              placeholder="请输入IP地址"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
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
              v-hasPermi="['iot:device:add']"
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
              v-hasPermi="['iot:device:edit']"
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
              v-hasPermi="['iot:device:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              :loading="exportLoading"
              @click="handleExport"
              v-hasPermi="['iot:device:export']"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="序号" width="55" type="index" align="center" />
          <el-table-column label="所属产品" align="center" prop="productKey" >
            <template slot-scope="scope">
              {{ productMap.get(scope.row.productKey) }}
            </template>
          </el-table-column>
          <el-table-column label="所属分组" align="center" prop="groupName" />
          <el-table-column label="设备名称" align="center" prop="deviceName" />
          <el-table-column label="设备编号" align="center" prop="deviceCode" />
          <el-table-column label="启用状态" align="center" prop="enabled" width="80">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.enabled"
                active-color="#1D58A9"
                inactive-color="#ff4949"
                :active-value="1"
                :inactive-value="0"
                @change="swichChange($event,scope.row)">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="IP地址" align="center" prop="ipAddr" />
          <el-table-column label="位置" align="center" prop="location" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="handleLook(scope.row)"
                v-hasPermi="['iot:device:query']"
              >详细</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="handleDebug(scope.row)"
                v-hasPermi="['iot:device:edit']"
              >调试</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['iot:device:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['iot:device:remove']"
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

        <!-- 添加或修改设备对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="所属产品" prop="productName">
              <el-input v-model="form.productName" readonly @click.native="productDrawer()" placeholder="请选择所属产品" />
            </el-form-item>
            <el-form-item label="所属分组" prop="groupKey">
              <el-select v-model="form.groupKey" placeholder="请选择所属分组" clearable size="medium" style="width: 100%;">
                <el-option
                  v-for="item in grounpList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
            <el-form-item label="设备编号" prop="deviceCode">
              <el-input v-model="form.deviceCode" placeholder="请输入设备编号" />
            </el-form-item>
            <el-form-item label="启用状态" prop="enabled">
              <el-select v-model="form.enabled" placeholder="请选择启用状态" class="select-width">
                <el-option
                  v-for="dict in enabledOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="IP地址" prop="ipAddr">
              <el-input v-model="form.ipAddr" placeholder="请输入IP地址" />
            </el-form-item>
            <el-form-item label="位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入位置" />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
        <!-- 查看产品抽屉 -->
        <el-drawer
          title="选择产品"
          :visible.sync="product_drawer"
          direction="rtl"
          :before-close="productHandleClose"
          :modal="false">
          <div style="margin-left: 15px; margin-right: 15px">
            <div style="margin-bottom: 15px;">
              <!-- 选择下拉框和搜索 -->
              <el-row>
                <el-col>
                  <el-input placeholder="请输入产品名称" v-model="productName">
                    <el-button slot="append" icon="el-icon-search" @click="handleProductNameClick"></el-button>
                  </el-input>
                </el-col>
              </el-row>
            </div>
            <el-table
              :data="drawerProductList.rows"
              style="width: 100%"
              v-loading="loading">
              <el-table-column label="序号" width="55" align="center" type="index" />
              <el-table-column label="所属产品" align="center" prop="productName">
                <template slot-scope="scope">
                  {{scope.row.productName}}
                  <el-button style="color: #CCCCCCFF" type="text"
                  @click="handleCheckFunctionClick(scope.row.id)"></el-button>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button :disabled="form.productKey===scope.row.id" @click="productHandleClick(scope.row)"
                  type="text" size="small">选择</el-button>
                </template>
              </el-table-column>
            </el-table>
              <!-- 产品列表分页 -->
            <pagination
              v-show="drawerProductList.total>0"
              :total="drawerProductList.total"
              layout="prev, pager, next"
            />
          </div>
        </el-drawer>
        <!-- 调试抽屉 -->
        <el-drawer
          title="在线调试"
          :visible.sync="debug_drawer"
          direction="rtl"
          :before-close="handleClose">
          <div style="margin-left: 15px; margin-right: 15px;">
            <div style="margin-bottom: 15px;">
              <el-tabs v-model="activeName" @tab-click="debugTabs">
                <el-tab-pane label="属性调试" name="first">
                  <!-- <el-scrollbar wrap-class="scrollbar-wrapper" style="height:100%;"> -->
                    <p class="input-with-select_title">人脸比对成功阈值(OnMatchThreshOld)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                    <p class="input-with-select_title">人脸库MD5签名(FaceSetMD5Sign)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                    <p class="input-with-select_title">人脸库ID(FaceMD5Id)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                    <p class="input-with-select_title">当前人脸库大小(FaceMD5Id)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                    <p class="input-with-select_title">人脸库算法版本(FaceMD5Id)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                    <p class="input-with-select_title">人脸库硬件版本(FaceMD5Id)</p>
                    <el-input class="input-with-select" placeholder="调试">
                      <el-select slot="append" placeholder="请选择">
                        <el-option
                          v-for="item in productList.rows"
                          :key="item.id"
                          :label="item.productName"
                          :value="item.id"
                        />
                      </el-select>
                    </el-input>
                  <!-- </el-scrollbar> -->
                </el-tab-pane>
                <el-tab-pane label="服务调用" name="second">
                  <vue-json-editor
                    v-model="resultInfo"
                    :showBtns="true"
                    :mode="'code'"
                    lang="zh"
                    @json-change="onJsonChange"
                    @json-save="onJsonSave"
                  />
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </el-drawer>
        <!-- 查看设备抽屉 -->
        <el-drawer
          :title="drawer_title"
          :visible.sync="drawer"
          direction="rtl"
          :before-close="handleClose">
          <el-tabs type="border-card">
            <el-tab-pane>
              <span slot="label">设备信息</span>
              <el-descriptions class="margin-top" :column="1" border>
                <el-descriptions-item>
                  <template slot="label">
                    设备名称
                  </template>
                  {{formLook.deviceName}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    所属分组
                  </template>
                  {{formLook.grounpName}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    所属产品
                  </template>
                  {{ productMap.get(formLook.productKey) }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    设备编号
                  </template>
                  {{formLook.deviceCode}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    启用状态
                  </template>
                  <span :style="formLook.enabled == '1'?'color:green;':'color:red;'">
                    {{formLook.enabled == 1?'已启用':'已禁用'}}
                  </span>
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    创建时间
                  </template>
                  {{formLook.createTime}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    位置
                  </template>
                  {{formLook.location}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    IP地址
                  </template>
                  {{formLook.ipAddr}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    更新时间
                  </template>
                  {{formLook.updateTime}}
                </el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>
            <el-tab-pane label="物模型数据">
              <template>
                <el-descriptions class="margin-top" :column="1" border v-for="(item,index) in functionData" :key="index">
                  <el-descriptions-item label-class-name="descriptions-item">
                    <template slot="label">
                      功能名称
                    </template>
                    {{item.name}}
                  </el-descriptions-item>
                  <el-descriptions-item label-class-name="descriptions-item">
                    <template slot="label">
                      标识符
                    </template>
                    {{item.identifier}}
                  </el-descriptions-item>
                  <el-descriptions-item label-class-name="descriptions-item">
                    <template slot="label">
                      实际值
                    </template>
                    <div v-for="(items,idx) in item.children" :key="idx">
                      <div v-show="item.dataType == items.value">
                        {{items.name}}
                      </div>
                    </div>
                  </el-descriptions-item>
                  <el-descriptions-item label-class-name="descriptions-item">
                    <template slot="label">
                      单位
                    </template>
                    {{item.unit}}
                  </el-descriptions-item>
                </el-descriptions>
              </template>
            </el-tab-pane>
          </el-tabs>
        </el-drawer>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {listDevice, getDevice, delDevice, addDevice, updateDevice, exportDevice,getDeviceFunction} from "@/api/iot/device";
import { listProduct} from "@/api/iot/product";
import {listGroup} from "@/api/iot/group";
import {listValue} from "@/api/iot/value";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import vueJsonEditor from 'vue-json-editor';

export default {
  name: "Device",
  components: { Treeselect,vueJsonEditor},
  data() {
    return {
      resultInfo:{
        "firstName": "Brett", "lastName":"McLaughlin", "email": "brett@newInstance.com",
      },
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
      // 设备表格数据
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 在线状态字典
      onlineStateOptions: [],
      // 启用状态字典
      enabledOptions: [],
      // 分组参数
      groundQueryparams:{
        name: null,
        description: null,
      },
      // 分组树选项
      grounpOptions: undefined,
      // 分组名称
      grounpName: undefined,
      defaultProps: {
        children: "children",
        label: "name"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productKey: null,
        groupKey: null,
        deviceName: null,
        deviceCode: null,
        onlineState: null,
        enabled: null,
        version: null,
        ipAddr: null,
        productName:null,
      },
      drawerQueryParams: {
        pageNum: 1,
        pageSize: 10,
        productKey: null,
        groupKey: null,
        deviceName: null,
        deviceCode: null,
        onlineState: null,
        enabled: null,
        version: null,
        ipAddr: null,
        productName:null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        productKey: [
          { required: true, message: "所属产品不能为空", trigger: "blur" }
        ],
        groupKey: [
          { required: true, message: "所属分组不能为空", trigger: "blur" }
        ],
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        deviceCode: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
        enabled: [
          { required: true, message: "启用状态不能为空", trigger: "change" }
        ],
      },
      // 产品列表
      productList:[],
      // 抽屉产品列表
      drawerProductList:[],
      // 分组列表
      grounpList:[],
      // 设备抽屉开关
      drawer:false,
      // 所属产品抽屉开关
      product_drawer:false,
      // 调试抽屉开关
      debug_drawer:false,
      // 查看设备信息
      formLook:[],
      //设备名称
      drawer_title:'',
      // 物模型数据
      functionData:{},
      functionarray:{},
      activeName:'first',
      // 选择产品搜索框关键词
      productName:null,
      addProductName:'',
      // 产品数据
      productMap: new Map(),
    };
  },
  watch: {
    grounpName(val) {
      this.$refs.tree.filter(val);
    }
  },
  async created() {
    await this.getProductList();
    this.getDrawerProductList();
    this.getTreeselect();
    this.getDicts("iot_device_online_state").then(response => {
      this.onlineStateOptions = response.data;
    });
    this.getDicts("iot_device_enabled").then(response => {
      this.enabledOptions = response.data;
    });
    this.getList();
  },
  methods: {
    /** 获取产品列表 */
    getProductList() {
      return listProduct(this.queryParams).then(response => {
        const productList = response.rows;
        if (!productList) return;
        productList.forEach(item => {
          this.productMap.set(item.id, item.productName);
        })
      });
    },
    getDrawerProductList() {
      this.loading = true;
      listProduct(this.drawerQueryParams).then(response => {
        this.drawerProductList = response;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询分组 */
    getTreeselect() {
      this.loading = true;
      listGroup(this.groundQueryparams).then(response => {
        this.grounpList = response.data;
        this.grounpOptions = [];
        const data = { id: 0, name: '分组名称', children: [] };
        data.children = this.handleTree(response.data, "id", "parentId");
        this.grounpOptions.push(data);
        this.loading = false;
      });
    },
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows;
        for(let i = 0; i < this.deviceList.length; i++) {
          var groupName = this.grounpList.filter((item) => {
            return item.id == this.deviceList[i].groupKey;
          });
          this.deviceList[i].groupName = groupName[0].name;

          // let productList = this.productList.rows;
          // var productName = productList.filter((item) => {
          //   return item.id == this.deviceList[i].productKey;
          // });
          // this.deviceList[i].productName = productName[0].productName
        }
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 选择产品点击事件 **/
    productHandleClick(item){
      console.log(item);
      if(this.open){
        this.form.productKey = item.id;
        this.form.productName = item.productName;
      }else{
        this.queryParams.productKey = item.id;
        this.queryParams.productName = item.productName;
      }
      this.$forceUpdate();
    },
    // 新增时选择所属产品
    productDrawer(){
      this.loading = true;
      this.product_drawer = true;
      this.loading = false;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.groupKey = data.id;
      this.getList();
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        productKey: null,
        productName: null,
        groupKey: null,
        deviceName: null,
        deviceCode: null,
        ipAddr: null,
        location: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      console.log(this.queryParams);
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.reset();
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
      this.open = true;
      this.title = "添加设备";
      this.reset();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids
      // 获取到修改设备的id,然后查询
      getDevice(id).then(response => {
        this.open = true;
        this.title = "修改设备";
        this.form = response.data;
        this.form.productName = this.productMap.get(row.productKey)
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form);
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDevice(this.form).then(response => {
              console.log(response);
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
        this.reset();
      });
    },
    // 调试按钮
    handleDebug(){
      this.loading = true;
      this.debug_drawer = true;
      this.loading = false;
    },
    // 详细按钮
    async handleLook(row){
      this.loading = true;
      const id = row.id || this.ids
      // 获取到修改设备的id,然后查询
      getDevice(id).then(response => {
        this.formLook = response.data;
        // // 取所属产品数据
        // let proData = this.productList.rows.filter((item) => {
        //   return item.id == row.productKey;
        // });
        // 取所属分组数据
        let grounpData = this.grounpList.filter((item) => {
          return item.id == row.groupKey;
        });
        // this.formLook.productName = proData[0].productName;
        this.formLook.grounpName = grounpData[0].name;
        this.drawer = true;
        this.drawer_title = response.data.deviceName;
        this.loading = false;
      });
      // 获取物模型数据
      await getDeviceFunction(row.id).then((response) => {
        this.functionarray = response.data;
      })
      for(let i = 0;i < this.functionarray.length;i++){
          await this.getFvList(this.functionarray[i].id);
      }
      this.functionData = this.functionarray
    },
    // 获取详细功能定义
    async getFvList(id){
      let query = {
        pageNum: null,
        functionKey: id,
      };
      await listValue(query).then(response => {
        let list = response.rows;
        this.functionarray.forEach(function(item){
          if(item.id == id){
            item.children = list;
          }
        });
      });
    },
    // 关闭抽屉
    handleClose(done) {
      done();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDevice(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出设备数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportDevice(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    swichChange(status,item){
       updateDevice(item).then(response => {
         this.$modal.msgSuccess("状态修改成功");
         this.getList();
       })
    },
    debugTabs(tab, event) {
      console.log(tab, event);
    },
    // json改变时执行的方法
    onJsonChange(){

    },
    // json保存时的方法
    onJsonSave(){
      console.log('我点击保存了')
    },
    /** 领域选中值发生变化时触发 **/
    handleCategoryFieldChange() {
      this.ProductList.pageNum = 1;
      this.getProductList();
    },
    // 点击抽屉中产品
    handleCheckFunctionClick(){

    },
    // 产品选择搜索框
    handleProductNameClick(item){
      console.log(this.productName);
    },
    // 产品抽屉关闭事件
    productHandleClose(done){
      done();
      // this.reset();
    },
  }
};
</script>
<style scoped>
  #pane-1 .el-descriptions{margin-bottom: 25px;}
  .el-scrollbar__wrap{overflow-x: hidden;}
  .el-drawer__wrapper >>> .el-descriptions-item__cell{width: 50%;}
  .el-drawer__wrapper >>> .input-with-select .el-input__inner {
    width: 100%;
  }
  .el-drawer__wrapper >>> .input-with-select .el-input-group__append{
    width: 25%;
  }
  .input-with-select_title{margin:15px 0;color:#999;}
</style>>
