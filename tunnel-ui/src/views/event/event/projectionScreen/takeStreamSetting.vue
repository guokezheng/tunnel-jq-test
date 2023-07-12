<template>
  <el-dialog
    :title="titleHistory"
    :visible.sync="visibleSync"
    :before-close="closeLogin"
    width="80%"
    :append-to-body="true"
    class="hitchHistoryDialog"
    :close-on-click-modal="false"
  >
    <el-form
      ref="loginQueryForm"
      :model="loginModel"
      :inline="true"
      class="loginQueryFormClass"
      label-width="120px"
      height="300px"

    >

      <el-row :gutter="24" class="topFormRow">
        <el-col :span="24">
          <el-form-item label="采流模式" >
            <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small"  @change="selectChange">
              <el-option
                v-for="item in takeStreamOptions"
                :key="item.value"
                size="small"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <div style=" border: 1px solid #eee">
        <div>IP或者域名取流</div>
        <el-row :gutter="24" class="topFormRow">
          <el-col :span="6">
            <el-form-item label="主机ip或者域名"  >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="通道号" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="远程主机端口" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="制造商类型" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="topFormRow">
          <el-col :span="6">
            <el-form-item label="远程主机用户名"  >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="远程主机密码" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="传输流协议" >
              <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small" style="width: 100px"  @change="selectChange">
                <el-option
                  v-for="item in takeStreamOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="传输流模式主流" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24" class="topFormRow">
          <el-col :span="6">
            <el-form-item label="使用流媒体服务器"  >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="ip" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="端口" >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="协议 | 通道类型" >
              <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small" style="width: 100px" @change="selectChange">
                <el-option
                  v-for="item in takeStreamOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small" style="width: 100px"  @change="selectChange">
                <el-option
                  v-for="item in takeStreamOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24" class="topFormRow">
          <el-col :span="6">
            <el-form-item label="流ID"  >
              <el-input size="small" v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="设备类型" >
              <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small"  @change="selectChange">
                <el-option
                  v-for="item in takeStreamOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="解决" >
              <el-select v-model="loginModel.wellNum" placeholder="请选择" size="small"  @change="selectChange">
                <el-option
                  v-for="item in takeStreamOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item  >
              <el-checkbox v-model="loginModel.wellNum">是否显示通道</el-checkbox>
              <el-checkbox v-model="loginModel.wellNum">是否显示子通道</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small"  :loading="exportLoading" @click="deviceLogin"
      >确认
      </el-button>
      <el-button size="small" :loading="exportLoading" @click="jointControlClick"
      >关闭
      </el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  name: "takeStreamSetting",
  data() {
    return {
      titleHistory:"采流设置",
      visibleSync:false,
      exportLoading:false,
      wellNumOptions: [],
      loginModel:{},
      takeStreamOptions:[
        {label:"IP或者域名解析",value:"1"}
      ],
    }
  },
  created(){

  },
  methods:{
    closeLogin(){
      this.visibleSync = !this.visibleSync
    },
    ProductionSetting(){

    },
    startDecoding(){

    },
    stopDecoding(){

    }
  },
  props:{
    show:Boolean,
  },
  watch:{
    show:{
      handler(newValue, oldValue){
        debugger
        this.visibleSync = !this.visibleSync
      }
    }
  }
}
</script>

<style scoped>

</style>
