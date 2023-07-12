<template>
  <el-dialog
    :title="titleHistory"
    :visible.sync="visibleSync"
    :before-close="closeLogin"
    width="60%"
    :append-to-body="true"
    class="hitchHistoryDialog"
    :close-on-click-modal="false"
  >
    <el-form
      ref="loginQueryForm"
      :model="loginModel"
      :inline="true"
      class="loginQueryFormClass"
      label-width="100px"
      height="300px"
    >

      <el-row :gutter="24" class="topFormRow">
        <el-col :span="8">
          <el-form-item label="墙号" >
            <el-input v-model="loginModel.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="窗口号" >
            <el-input v-model="loginModel.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="子窗口号" >
            <el-input v-model="loginModel.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small"  :loading="exportLoading" @click="ProductionSetting"
      >采流设置
      </el-button>
      <el-button size="small" :loading="exportLoading" @click="startDecoding"
      >开始解码
      </el-button>
      <el-button size="small" :loading="exportLoading" @click="stopDecoding"
      >停止解码
      </el-button>
    </div>
    <takeStreamSetting ref="takeStreamSetting" :show="takeStreamSettingShow"></takeStreamSetting>
  </el-dialog>
</template>

<script>
import  takeStreamSetting  from "@/views/event/event/projectionScreen/takeStreamSetting.vue";
export default {
  name: "dynamicDecodeModal",
  components: { takeStreamSetting},
  data() {
    return {
      titleHistory:"动态解码",
      visibleSync:false,
      wellNumOptions: [],
      loginModel:{},
      takeStreamSettingShow:false,
      exportLoading:false,
    }
  },
  created(){

  },
  methods:{
    closeLogin(){
      this.visibleSync = !this.visibleSync
    },
    ProductionSetting(){
      this.takeStreamSettingShow = !this.takeStreamSettingShow
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
