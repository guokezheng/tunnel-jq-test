<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row  :gutter="20" class="topFormRow">
      <el-col :span="6">
        <el-button
          type="primary"
          plain
          size="small"
          @click="handleExport"
          >导出</el-button>
          <el-button size="small" @click="resetQuery" type="primary" plain
          >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="12">
        <div  ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.searchValue"
            placeholder="请输入发布设备、发布内容，回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="icon-gym-Gsearch"
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
        label-width="75px"
      >
          <el-form-item label="发布时间" prop="releaseTime">
            <el-date-picker
              clearable
              size="small"
              v-model="queryParams.releaseTime"
              style="width: 100%"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择发布时间"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="发布状态" prop="releaseStatus">
            <el-select
              v-model="queryParams.releaseStatus"
              placeholder="请选择发布状态"
              clearable
              size="small"
            >
              <el-option label="成功" value="0" />
              <el-option label="失败" value="1" />
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
<!--    <el-form-->
<!--      :model="queryParams"-->
<!--      ref="queryForm"-->
<!--      :inline="true"-->
<!--      v-show="showSearch"-->
<!--      label-width="68px"-->
<!--    >-->
<!--      <el-form-item label="发布设备" prop="deviceId">-->
<!--        <el-input-->
<!--          v-model="queryParams.deviceId"-->
<!--          placeholder="请输入发布设备"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="发布时间" prop="releaseTime">-->
<!--        <el-date-picker-->
<!--          clearable-->
<!--          size="small"-->
<!--          v-model="queryParams.releaseTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="选择发布时间"-->
<!--        >-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="发布状态" prop="releaseStatus">-->
<!--        <el-select-->
<!--          v-model="queryParams.releaseStatus"-->
<!--          placeholder="请选择发布状态"-->
<!--          clearable-->
<!--          size="small"-->
<!--        >-->
<!--          <el-option label="成功" value="0" />-->
<!--          <el-option label="失败" value="1" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      &lt;!&ndash; <el-form-item label="发布机构" prop="releaseDeptName">-->
<!--        <el-input-->
<!--          v-model="queryParams.releaseDeptName"-->
<!--          placeholder="请输入发布机构"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item> &ndash;&gt;-->
<!--      <el-form-item label="发布用户" prop="releaseUserName">-->
<!--        <el-input-->
<!--          v-model="queryParams.releaseUserName"-->
<!--          placeholder="请输入发布用户"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          size="mini"-->
<!--          @click="handleQuery"-->
<!--          >搜索</el-button-->
<!--        >-->
<!--        <el-button size="mini" @click="resetQuery"  type="primary" plain-->
<!--          >重置</el-button-->
<!--        >-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          >导出</el-button-->
<!--        >-->
<!--      </el-form-item>-->
<!--    </el-form>-->
    <div class="tableTopHr" ></div>
    <el-table
      v-loading="loading"
      :data="recordList"
      @selection-change="handleSelectionChange"
      :default-sort = "{prop: 'releaseTime', order: 'descending'}"
      class="allTable"
      height="62vh"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" width="68" align="center"></el-table-column>
      <!--      <el-table-column label="发布用户" align="center" prop="id" />-->
      <!-- <el-table-column label="发布设备" align="center" prop="deviceId" /> -->
      <el-table-column label="屏幕尺寸" align="center" prop="devicePixel" />
      <el-table-column label="所属隧道" align="center" prop="tunnelName" />
      <el-table-column label="设备方向" align="center" prop="direction" />

      <el-table-column label="设备桩号" align="center" prop="pile" />
      <el-table-column label="发布内容" align="center" prop="releaseNewContent" >
        <template slot-scope="scope">
          <div v-for="(item,index) of scope.row.list" :key="index"
          :style="{
            width:item.WIDTH + 'px',
            height:item.HEIGHT + 'px',
            color:item.COLOR,
            fontSize:item.FONT_SIZE,
            }"
          style="background: #000;position: relative;margin: 2px auto;">
            <span
            :style="{
              top:item.TOP,left:item.LEFT
            }"
            style="position: absolute;line-height: 1;"
            v-html="
                    item.CONTENT.replace(/\n|\r\n/g, '<br>').replace(
                      / /g,
                      ' &nbsp'
                    )">
          </span>
          </div>

        </template>
      </el-table-column>
      <el-table-column
        label="发布时间"
        align="center"
        prop="releaseTime"

      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.releaseTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="releaseStatus" />

      <!-- <el-table-column label="发布机构" align="center" prop="releaseDeptName" /> -->

      <el-table-column label="发布用户" align="center" prop="releaseUserName" />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改发布记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件发布设备关联表ID" prop="vmsEventId">
          <el-input
            v-model="form.vmsEventId"
            placeholder="请输入事件发布设备关联表ID"
          />
        </el-form-item>
        <el-form-item label="发布设备" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入发布设备" />
        </el-form-item>
        <el-form-item label="发布时间" prop="releaseTime">
          <el-date-picker
            clearable
            size="small"
            v-model="form.releaseTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择发布时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发布状态" prop="releaseStatus">
          <el-select v-model="form.releaseStatus" placeholder="请选择发布状态">
            <el-option label="正常" value="0" />
            <el-option label="失败" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布机构" prop="releaseDeptName">
          <el-input
            v-model="form.releaseDeptName"
            placeholder="请输入发布机构"
          />
        </el-form-item>
        <el-form-item label="发布机构" prop="releaseDeptId">
          <el-input v-model="form.releaseDeptId" placeholder="请输入发布机构" />
        </el-form-item>
        <el-form-item label="发布用户" prop="releaseUserName">
          <el-input
            v-model="form.releaseUserName"
            placeholder="请输入发布用户"
          />
        </el-form-item>
        <el-form-item label="发布用户" prop="releaseUserId">
          <el-input v-model="form.releaseUserId" placeholder="请输入发布用户" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitFormLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRecord,
  exportRecord,
} from "@/api/board/record";

export default {
  name: "Record",
  components: {},
  data() {
    return {
      boxShow: false,
      listtt:[{
        COLOR:'yellow',
        FONTSIZE:'24px',
        WIDTH:'300px',
        HEIGHT:'40px',
        CONTENT:'山东高速欢迎您',
        TOP:'5px',
        LEFT:'20px',
      }],
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
      // 发布记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitFormLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        releaseTime: null,
        releaseStatus: null,
        releaseDeptName: null,
        searchValue: null,
        releaseUserName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  //点击空白区域关闭全局搜索弹窗
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
  },
  methods: {
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
    /** 查询发布记录列表 */
    getList() {
      this.loading = true;
      let contents = [];

      listRecord(this.queryParams).then((response) => {
        this.recordList = response.rows;
        console.log(this.recordList,"发送记录表格")
        for (var item of this.recordList) {
          if (item.releaseStatus == "0") {
            item.releaseStatus = "成功";
          } else {
            item.releaseStatus = "失败";
          }
          item.releaseNewContent = item.releaseNewContent.substring(item.releaseNewContent.indexOf("\\f")+7).replaceAll("\\n","");
          var arr = []
          contents = JSON.parse(item.paramsList[0])['content']
          for (var i = 0; i < contents.length; i++) {
            var content = contents[i];
            var itemId = "ITEM" + this.formatNum(i, 3);
            for(var itm of content[itemId]){
              itm.COLOR = this.getColorStyle(itm.COLOR);
              itm.FONT_SIZE = this.getFontSize(itm.FONT_SIZE.substring(0, 2),itm.DEVICEPIXEL) + 'px';
              itm.WIDTH = this.getDevicePixel(itm.DEVICEPIXEL,'width');
              itm.HEIGHT = this.getDevicePixel(itm.DEVICEPIXEL,'height')
              itm.TOP = this.getCoordinate(itm.COORDINATE.substring(3, 6),'top',itm.DEVICEPIXEL) + 'px';
              itm.LEFT = this.getCoordinate(itm.COORDINATE.substring(0, 3),'left',itm.DEVICEPIXEL) + 'px';
              itm.CONTENT = itm.CONTENT.replace('\\n', '<br>').replace(/ /g,' &nbsp')
              arr.push(itm);
            }
          }
          item.list = arr
        }
        console.log(this.recordList,"发送记录表格1111111")

        this.total = response.total;
        this.loading = false;
      });
    },
    getFontSize (font,devicePixel){
      let width = devicePixel.split("*")[0];
      let height = devicePixel.split("*")[1];
      if (width < 250 && height < 38) {
        return font;
      } else {
        if (width / 250 > height / 38) {
          return font / (width / 250) - 1;
        } else {
          return font / (height / 38) - 1;
        }
      }
    },
    getCoordinate(coordinate, type, screenSize){
      let width = screenSize.split("*")[0];
      let height = screenSize.split("*")[1];

      if (width < 250 && height < 38) {
        return coordinate;
      } else {
        if (width / 250 > height / 38) {
          if (type == "left") {
            return coordinate / (width / 250);
          } else if (type == "top") {
            return coordinate / (width / 250);
          }
        } else {
          if (type == "left") {
            return coordinate / (height / 38) + 3;
          } else if (type == "top") {
            return coordinate / (height / 38) + 1;
          }
        }
      }
    },
    // 转分辨率
    getDevicePixel(devicePixel, type) {
      let width = devicePixel.split("*")[0]
      let height = devicePixel.split("*")[1]
      if(width < 250 && height < 38){
        if(type == 'width'){
          return width
        }else if (type == 'height'){
          return height
        }
      }else{
        if (width / 250 > height / 38) {
          if (type == "width") {
            return 250;
          } else if (type == "height") {
            return height / (width / 250);
          }
        } else {
          if (type == "width") {
            return width / (height / 38);
          } else if (type == "height") {
            return 38;
          }
        }
      }
      // if (devicePixel) {
      //   if(width > 250){

      //     if(type == 'width'){
      //       return 394 + 'px'
      //     }else if(type == 'height'){
      //       return 75 + 'px'
      //     }
      //   }
      //   return devicePixel.split("*")[num] + "px";
      // }
    },
    // 转颜色
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },
    getReleaseNewContent(content){
      console.log(content,"content")


    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        deviceId: null,
        releaseTime: null,
        releaseStatus: null,
        releaseDeptName: null,
        releaseDeptId: null,
        releaseUserName: null,
        releaseUserId: null,
        releaseNewContent: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.searchValue='';
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
    },
    /** 提交按钮 */
    submitForm() {
    },
    /** 删除按钮操作 */
    handleDelete(row) {
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      //查看当前ids是否存在,如果存在。则按照当前ids进行导出。
      queryParams.ids = this.ids;
      this.$confirm("是否确认导出发布记录数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportRecord(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          queryParams.ids = null;
        });
    },
  },
};
</script>
<style scoped>
 /* ::v-deep .el-table .cell{
    display: flex;
    justify-content: center;
 } */
</style>

