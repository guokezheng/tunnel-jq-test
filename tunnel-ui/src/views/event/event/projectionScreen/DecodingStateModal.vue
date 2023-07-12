<template>
  <el-dialog
    :title="titleHistory"
    :visible.sync="visibleSync"
    :before-close="closeLogin"
    width="60%"
    height="580px"
    :append-to-body="true"
    class="hitchHistoryDialog"
    :close-on-click-modal="false"
  >
    <el-table
      v-loading="loading"
      :data="groupList"
      row-key="id"
      class="allTable"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="窗口号" align="left" prop="name" />
      <el-table-column label="解码状态" align="center" prop="description" />
      <el-table-column label="编码类型" align="center" prop="description" />
      <el-table-column label="打包格式" align="left" prop="name" />
      <el-table-column label="视频解码" align="center" prop="description" />
      <el-table-column label="音频解码" align="center" prop="description" />
      <el-table-column label="解码视频帧" align="left" prop="name" />
      <el-table-column label="解码音频帧" align="center" prop="description" />
      <el-table-column label="码流原图" align="center" prop="description" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['iot:group:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-plus"-->
<!--            @click="handleAdd(scope.row)"-->
<!--            v-hasPermi="['iot:group:add']"-->
<!--          >新增</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['iot:group:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
  </el-dialog>
</template>

<script>
export default {
  name: "DecodingState",
  data() {
    return {
      titleHistory:"窗口解码状态",
      visibleSync:false,
      loading:false,
      groupList:[],
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
