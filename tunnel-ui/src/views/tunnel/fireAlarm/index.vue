<!-- <template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true"  label-width="68px">
      
      <el-form-item label="隧道名称" prop="tunnelId">
        <el-select v-model="queryParams.tunnelId" placeholder="请选择隧道名称" clearable size="small">
          <el-option
            v-for="item in tunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData">
      <el-table-column label="隧道名称" align="center" prop="tunnelName" />
      <el-table-column label="火灾报警器编号" align="left" prop="equipmentNumber" />
      <el-table-column label="当前温度(°C)" align="center" prop="currentTemperature" >
          <template slot-scope="scope">
              <div style="text-align: center">
                <span
                  :style="
                    scope.row.currentTemperature >= '50'
                      ? 'color: #ff0000'
                      : 'color: #000'
                  "
                >
                  {{ scope.row.currentTemperature }}
                </span>
              </div>
          </template>
      </el-table-column>
      <el-table-column label="报警系统状态" align="center" prop="systemState" />
      <el-table-column label="采集时间" align="center" prop="createTime" />
    </el-table>
    <pagination
      
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <div class="fields__item">
          <input type="checkbox" class="uiswitch custom">
          <h6>Custom</h6>
        </div>
  </div>
</template>

<script>
    export default{
        data(){
            return{
                tableData:[
                    {
                        tunnelName:'迎春坡隧道',
                        equipmentNumber:'SH7382-hsk662-uww32',
                        currentTemperature:'28',
                        systemState:'-',
                        createTime:'2022.01.17 15:13:00'
                    },
                    {
                        tunnelName:'迎春坡隧道',
                        equipmentNumber:'SH7382-hsk662-uew77',
                        currentTemperature:'55',
                        systemState:'异常',
                        createTime:'2022.01.17 15:18:00'
                    }
                ],
                queryParams: {
                  pageNum: 1,
                  pageSize: 10,
                  eqId: null,
                  tunnelId: null
                },
                tunnelData:{},
                // 遮罩层
                loading: true,
                // 总条数
                total: 0,
            }
        },
        mounted(){
            this.getList();
        },
        methods:{
            getList() {
              this.loading = true;
              setTimeout(() => {
                  this.loading = false;
              },1000)
              // listPower_record(this.queryParams).then(response => {
              //   this.power_recordList = response.rows;
              //   this.total = response.total;
              //   this.loading = false;
              // });
              // 当设备异常时
              // this.$refs.audio.currentTime = 0; //从头开始播放提示音
              // this.$refs.audio.play(); //播放
              
            },
            handleQuery() {
              this.queryParams.pageNum = 1;
              this.getList();
            },
            resetQuery() {
              this.resetForm("queryForm");
              this.handleQuery();
            },
            // 语音播放
            aplayAudio () {
              const audio = document.getElementById('audio')
              audio.play()
            }
        }
    }
</script>

<style scoped="scoped" >
    .uiswitch {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        -webkit-appearance: none;
        -moz-appearance: none;
        -ms-appearance: none;
        -o-appearance: none;
        appearance: none;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        height: 31px;
        width: 51px;
        position: relative;
        border-radius: 16px;
        cursor: pointer;
        outline: 0;
        z-index: 0;
        margin: 0;
        padding: 0;
        border: none;
        background-color: #e5e5e5;
        -webkit-transition-duration: 600ms;
        -moz-transition-duration: 600ms;
        transition-duration: 600ms;
        -webkit-transition-timing-function: ease-in-out;
        -moz-transition-timing-function: ease-in-out;
        transition-timing-function: ease-in-out;
        -webkit-touch-callout: none;
        -webkit-text-size-adjust: none;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-user-select: none;
    }
     
    .uiswitch::before {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: 27px;
        width: 47px;
        content: ' ';
        position: absolute;
        left: 2px;
        top: 2px;
        background-color: #ffffff;
        border-radius: 16px;
        z-index: 1;
        -webkit-transition-duration: 300ms;
        -moz-transition-duration: 300ms;
        transition-duration: 300ms;
        -webkit-transform: scale(1);
        -moz-transform: scale(1);
        -ms-transform: scale(1);
        -o-transform: scale(1);
        transform: scale(1);
    }
     
    .uiswitch::after {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: 27px;
        width: 27px;
        content: ' ';
        position: absolute;
        border-radius: 27px;
        background: #ffffff;
        z-index: 2;
        top: 2px;
        left: 2px;
        box-shadow: 0px 0px 1px 0px rgba(0, 0, 0, 0.25), 0px 4px 11px 0px rgba(0, 0, 0, 0.08), -1px 3px 3px 0px rgba(0, 0, 0, 0.14);
        -webkit-transition: -webkit-transform 300ms, width 280ms;
        -moz-transition: -moz-transform 300ms, width 280ms;
        transition: transform 300ms, width 280ms;
        -webkit-transform: translate3d(0, 0, 0);
        -moz-transform: translate3d(0, 0, 0);
        -ms-transform: translate3d(0, 0, 0);
        -o-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0);
        -webkit-transition-timing-function: cubic-bezier(0.42, 0.8, 0.58, 1.2);
        -moz-transition-timing-function: cubic-bezier(0.42, 0.8, 0.58, 1.2);
        transition-timing-function: cubic-bezier(0.42, 0.8, 0.58, 1.2);
    }
     
    .uiswitch:checked {
        background-color: #4CD964;
        background-image: -webkit-linear-gradient(-90deg, #4CD964 0%, #4dd865 100%);
        background-image: linear-gradient(-180deg,#4CD964 0%, #4dd865 100%);
    }
     
    .uiswitch:checked::after {
        -webkit-transform: translate3d(16px, 0, 0);
        -moz-transform: translate3d(16px, 0, 0);
        -ms-transform: translate3d(16px, 0, 0);
        -o-transform: translate3d(16px, 0, 0);
        transform: translate3d(16px, 0, 0);
        right: 18px;
        left: inherit;
    }
     
    .uiswitch:active::after {
        width: 35px;
    }
     
    .uiswitch:checked::before, .uiswitch:active::before {
        -webkit-transform: scale(0);
        -moz-transform: scale(0);
        -ms-transform: scale(0);
        -o-transform: scale(0);
        transform: scale(0);
    }
     
    .uiswitch:disabled {
        opacity: 0.5;
        cursor: default;
        -webkit-transition: none;
        -moz-transition: none;
        transition: none;
    }
     
    .uiswitch:disabled:active::before, .uiswitch:disabled:active::after, .uiswitch:disabled:checked:active::before, .uiswitch:disabled:checked::before {
        width: 27px;
        -webkit-transition: none;
        -moz-transition: none;
        transition: none;
    }
     
    .uiswitch:disabled:active::before {
        height: 27px;
        width: 41px;
        -webkit-transform: translate3d(6px, 0, 0);
        -moz-transform: translate3d(6px, 0, 0);
        -ms-transform: translate3d(6px, 0, 0);
        -o-transform: translate3d(6px, 0, 0);
        transform: translate3d(6px, 0, 0);
    }
     
    .uiswitch:disabled:checked:active::before {
        height: 27px;
        width: 27px;
        -webkit-transform: scale(0);
        -moz-transform: scale(0);
        -ms-transform: scale(0);
        -o-transform: scale(0);
        transform: scale(0);
    }
     
    .uiswitch {
        background-color: #e5e5e5;
    }
     
    .uiswitch::before {
        background-color: #ffffff;
    }
     
    .uiswitch::after {
        background: #ffffff;
    }
     
    .uiswitch:checked {
        background-color: #4CD964;
        background-image: -webkit-linear-gradient(-90deg, #4CD964 0%, #4dd865 100%);
        background-image: linear-gradient(-180deg,#4CD964 0%, #4dd865 100%);
    }
    .custom {
        background-color: #eadcbc;
    }
     
    .custom::before {
        background-color: #f7f2e5;
    }
     
    .custom::after {
        background: #fff3a6;
    }
     
    .custom:checked {
        background-color: #ffca3f;
        background-image: -webkit-linear-gradient(-90deg, #ffca3f 0%, #feca40 100%);
        background-image: linear-gradient(-180deg, #ffca3f 0%, #feca40 100%);
    }
</style>
 -->