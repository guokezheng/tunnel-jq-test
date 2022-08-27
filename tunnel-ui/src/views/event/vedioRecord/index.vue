<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="隧道名称">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道" clearable size="small">
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="视频名称" prop="vedioName">
        <el-input
          v-model="queryParams.vedioName"
          placeholder="请输入视频名称"
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
          @click="handleAdd"
          v-hasPermi="['business:vediorecord:add']"
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
          v-hasPermi="['business:vediorecord:edit']"
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
          v-hasPermi="['business:vediorecord:remove']"
        >删除
        </el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
        </el-tooltip>
      </div>
    </el-row>

    <el-table ref="videoTable" v-loading="loading" :data="vediorecordList" max-height="610" 
                @selection-change="handleSelectionChange" @row-click="videoTableRowClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="隧道" align="center" prop="tunnels.tunnelName"/>
      <el-table-column label="相机名称" align="center" prop="vedioName" width="300"/>
      <el-table-column label="相机IP" align="center" prop="videoIp"/>
      <!-- <el-table-column label="流地址" align="center" prop="url" width="500"/> -->
      <!-- <el-table-column label="桩号" align="center" prop="stakeMark"/> -->
      <!-- <el-table-column label="回放地址" align="center" prop="storageAddress" width="280"/> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="openPlayer(scope.row)"
            v-hasPermi="['business:vediorecord:edit']"
          >监控直播
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

    <!-- 添加或修改历史视频信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属隧道" prop="tunnelId">
          <el-select v-model="form.tunnelId" placeholder="请选择隧道" clearable size="small">
            <el-option
              v-for="item in tunnelData"
              :key="item.tunnelId"
              :label="item.tunnelName"
              :value="item.tunnelId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="相机名称" prop="vedioName">
          <el-input v-model="form.vedioName" placeholder="请输入视频名称"/>
        </el-form-item>
        <el-form-item label="相机ip" prop="videoIp">
          <el-input v-model="form.videoIp" placeholder="请输入视频ip"/>
        </el-form-item>
        <el-form-item label="流地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入流地址"/>
        </el-form-item>
        <el-form-item label="桩号" prop="stakeMark">
          <el-input v-model="form.stakeMark" placeholder="请输入所在桩号"/>
        </el-form-item>
        <el-form-item label="回放地址" prop="storageAddress">
          <el-input v-model="form.storageAddress" placeholder="请输入存储地址"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 视频测试窗口 -->
    <el-dialog :title="titleV" :visible.sync="openV" width="960px" append-to-body>
      <videoPlayer :id="id" :rtsp="rtsp" :hostIP="hostIP" :open="openV"></videoPlayer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelV">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
    import {
        listVediorecord,
        getVediorecord,
        delVediorecord,
        addVediorecord,
        updateVediorecord,
        getLocalIP
    } from "@/api/event/vedioRecord";
    import {listTunnels} from "@/api/equipment/tunnel/api";
    import videoPlayer from "@/views/event/vedioRecord/myVideo";

    export default {
        name: "Vediorecord",
        components: {videoPlayer},
        data() {
            return {
                hostIP:"",
                id: '',
                rtsp: "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov",
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
                // 历史视频信息表格数据
                vediorecordList: [],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                submitFormLoading: false,
                // 弹出层标题
                titleV: "",
                // 是否显示弹出层
                openV: false,
                // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                    tunnelId: null,
                    vedioName: null,
                    videoIp: null,
                    stakeMark: null,
                    startTime: null,
                    overTime: null,
                    vedioFormat: null,
                },
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                    tunnelId:[{required: true, message: '请选择隧道', trigger: 'blur'}],
                    vedioName:[{required: true, message: '请输入相机名称', trigger: 'blur'}],
                    videoIp:[{required: true, message: '请输入ip', trigger: 'blur'},
                        { pattern: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
, message: '请输入正确的IP' }],
                },
                tunnelData: {},
            };
        },
        created() {
            this.getList();
            this.getTunnels();
            getLocalIP().then(response => {
                this.hostIP = response;
            });
        },
        methods: {
            /** 查询隧道列表 */
            getTunnels() {
                listTunnels().then(response => {
                    this.tunnelData = response.rows;
                });
            },
            /** 查询历史视频信息列表 */
            getList() {
                this.loading = true;
                listVediorecord(this.queryParams).then(response => {
                    this.vediorecordList = response.rows;
                    this.total = response.total;
                    this.loading = false;
                });
            },
            // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 取消按钮
            cancelV() {
                this.openV = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                    id: null,
                    tunnelId: null,
                    vedioName: null,
                    videoIp: null,
                    stakeMark: null,
                    startTime: null,
                    overTime: null,
                    vedioSize: null,
                    vedioFormat: null,
                    storageAddress: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null
                };
                this.resetForm("form");
            },

            /** 播放操作 */
            openPlayer(row) {
                debugger
                this.id = row.id;
                this.rtsp = row.url;
                this.openV = true;
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.resetForm("queryForm");
                this.queryParams.tunnelId = null
                this.queryParams.vedioName = null
                this.handleQuery();
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id)
                this.single = selection.length !== 1
                this.multiple = !selection.length
            },
            // 单击行，切换行的选中状态（表格）
            videoTableRowClick(row) {
              this.$refs.videoTable.toggleRowSelection(row)
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加历史视频信息";
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                const id = row.id || this.ids
                getVediorecord(id).then(response => {
                    this.form = response.data;
                    this.open = true;
                    this.title = "修改历史视频信息";
                });
            },
            /** 提交按钮 */
            submitForm() {
              if(this.submitFormLoading) return
              this.submitFormLoading = true
              this.$refs["form"].validate(async valid => {
                    if (valid) {
                        if (this.form.id != null) {
                            await updateVediorecord(this.form).then(response => {
                                if (response.code === 200) {
                                    this.$modal.msgSuccess("修改成功");
                                    this.open = false;
                                    this.getList();
                                }
                            });
                        } else {
                            await addVediorecord(this.form).then(response => {
                                if (response.code === 200) {
                                    this.$modal.msgSuccess("新增成功");
                                    this.open = false;
                                    this.getList();
                                }
                            });
                        }
                    }
                    this.submitFormLoading = false
                });
            },
            /** 删除按钮操作 */
            handleDelete(row) {
                const ids = row.id || this.ids;
                this.$confirm('是否确认删除选中的历史视频信息吗?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function () {
                    return delVediorecord(ids);
                }).then(() => {
                    this.getList();
                    this.$modal.msgSuccess("删除成功");
                }).catch(function () {
                });
            },
            /** 导出按钮操作 */
            handleExport() {
                this.download('business/vediorecord/export', {
                    ...this.queryParams
                }, `business_vediorecord.xlsx`)
            }
        }
    };
</script>
