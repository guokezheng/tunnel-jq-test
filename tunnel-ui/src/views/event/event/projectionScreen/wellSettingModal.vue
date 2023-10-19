<template>
  <el-dialog
    :title="titleHistory"
    :visible.sync="visibleSync"
    :before-close="closeLogin"
    width="40%"
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
      <div style="border: 1px solid black;">
        <el-row :gutter="20" class="topFormRow">
          <el-col :span="12">
            <el-form-item label="电视墙号" >
              <el-select v-model="loginModel.wellNum" placeholder="请选择"  @change="selectChange">
                <el-option
                  v-for="item in wellNumOptions"
                  :key="item.value"
                  size="small"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用" >
              <el-radio-group v-model="loginModel.use">
                <el-radio class="el-radio" label="1"> 是</el-radio>
                <el-radio class="el-radio" label="2"> 否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="topFormRow">
          <el-col :span="24">
            <el-form-item label="电视墙名称" >
              <el-input v-model="loginModel.name" style="width: 200%"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="topFormRow">
          <el-col :span="12">
            <el-form-item label="宽度(格)" >
              <el-input v-model="loginModel.breadth"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高度(格)" >
              <el-input v-model="loginModel.altitude"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="topFormRow">
          <el-button size="small" style="float: right;margin-left: 10px;margin-right: 15px" :loading="exportLoading" @click="deviceLogin"
          >获取
          </el-button>
          <el-button size="small" style="float: right"  :loading="exportLoading" @click="jointControlClick"
          >设置
          </el-button>
        </el-row>
      </div>
      <div style="border: 1px solid black;margin-top:5px">
          <div style="margin-left: 10px">墙关联</div>
        <el-row :gutter="20" class="topFormRow">
          <el-col :span="12">
            <el-form-item label="设备墙" >
              <el-input v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物理墙" >
              <el-input v-model="loginModel.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="topFormRow">
          <el-button size="small" style="float: right;margin-left: 10px;margin-right: 15px" :loading="exportLoading" @click="deviceLogin"
          >获取
          </el-button>
          <el-button size="small" style="float: right"  :loading="exportLoading" @click="jointControlClick"
          >设置
          </el-button>
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
  name: "wellSetting",
  data() {
    return {
      titleHistory:"视频墙设置",
      visibleSync: false,
      exportLoading:false,
      wellNumOptions: [],
      loginModel:{
        use:"0"
      },
    }
  },
  created(){
    // debugger
    this.wellNumOptions = []
    let valueNum = 0
    let labelNum = 0
    for (let i = 0; i < 17; i++) {
      let wellNum ={value:valueNum,label:labelNum}
      valueNum = valueNum+1
      labelNum = labelNum+1
      this.wellNumOptions.push(wellNum)
    }
  },
  methods:{
    closeLogin(){
      // debugger
      this.visibleSync = !this.visibleSync
    },
    selectChange(){

    },
    //usa 单选事件
    radioChange(e){
      // debugger
      console.log(e)
      this.loginModel.use = e
    },
    deviceLogin(){
      // debugger
      this.visibleSync = !this.visibleSync
      this.$emit('wellSettingModel', this.loginModel)

    },
    jointControlClick(){

    }
  },
  model: {
    prop: 'value',
    event: 'change',
  },
  props:{
    show:Boolean,
  },
  watch:{
    show:{
      handler(newValue, oldValue){
        // debugger
        this.visibleSync = !this.visibleSync
      }
    }
  }
}
</script>

<style scoped>

</style>
