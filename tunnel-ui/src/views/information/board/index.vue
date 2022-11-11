<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-08 10:04:52
 * @FilePath: \tunnel-ui\src\views\information\board\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="container" style="background-color: #004375; height: 100%">
    <el-row
      :gutter="20"
      style="height: 100%; margin-left: 0px; margin-right: 0px"
    >
      <el-col :span="4" style="background-color: white">
        <p class="bigTitle">情报板列表</p>
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="所属单位">
            <el-select
              v-model="form.company"
              placeholder="请选择所属单位"
              clearable
              size="small"
            >
              <el-option
                v-for="item in deptList"
                :key="item.deptId"
                :label="item.deptName"
                :value="item.deptId"
                @click.native="changeCompany(item.deptId)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属机构">
            <el-select
              v-model="form.mechanism"
              placeholder="请选择所属机构"
              clearable
              size="small"
            >
              <el-option
                v-for="item in mechanismList"
                :key="item.deptId"
                :label="item.deptName"
                :value="item.deptId"
                @click.native="changeMechanism(item.deptId)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属隧道">
            <el-select
              v-model="form.tunnel"
              placeholder="请选择所属隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="位置信息">
            <el-select
              v-model="form.position"
              placeholder="请选择所属隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in positionList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="分辨率">
            <el-select
              v-model="form.devicessize"
              placeholder="请选择分辨率"
              clearable
              size="small"
            >
              <el-option
                v-for="item in devicessizeList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
                @click.native="changeDevicessize(item.deptId)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label-width="0" v-show="checkbox">
            <el-checkbox
              style="width: 100%"
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
              >全选</el-checkbox
            >
            <div style="margin: 15px 0"></div>
            <el-checkbox-group
              class="checkbox"
              v-model="checkedCities"
              @change="handleCheckedCitiesChange"
            >
              <el-checkbox v-for="city in cities" :label="city" :key="city">{{
                city
              }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">确认</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col
        :span="10"
        style="background-color: white; border-left: 1px solid #f3f3f3"
      >
        <p class="bigTitle">待下发信息</p>
        <div class="contentBox">
          <div
            v-for="(item, index) in contentList"
            :key="index"
            class="listBox"
          >
            <div class="indexBox">
              <i
                class="el-icon-caret-top"
                size="18"
                @click="moveTop(index)"
              ></i>
              <i class="el-icon-caret-bottom" @click="moveBottom"></i>
            </div>
            <div
              class="con"
              :style="{ 'font-size': item.size, color: item.color }"
            >
              {{ item.content }}
            </div>
            <div class="menuBox">
              <i class="el-icon-d-arrow-right"></i>
              <i class="el-icon-edit-outline"></i>
              <i class="el-icon-close"></i>
            </div>
          </div>
          <div class="controlBox">
            <el-button @click.native="openDialogVisible">添加信息</el-button>
            <el-button type="primary">发布信息</el-button>
          </div>
        </div>
      </el-col>
      <el-col
        :span="10"
        style="background-color: white; border-left: 1px solid #f3f3f3"
      >
        <p class="bigTitle">信息模板</p>
        <div class="templateBox">
          <el-collapse v-model="activeNames" @change="handleChange" accordion>
            <el-collapse-item
              v-for="(item, index) in templateList"
              :key="index"
              :title="item.name"
              :name="item.id"
            >
              <div
                v-for="(itm, indx) in item.list"
                :key="indx"
                class="con"
                :style="{ 'font-size': itm.size, color: itm.color }"
              >
                <div class="templateTitle">{{ itm.content }}</div>
                <div class="menuBox">
                  <i class="el-icon-d-arrow-right"></i>
                  <i class="el-icon-edit-outline"></i>
                  <i class="el-icon-close"></i>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
          <div class="controlBox">
            <el-button type="primary">添加模板</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
    <addinfo ref="addinfo"></addinfo>
  </div>
</template>
<script>
import addinfo from "./addinfo";
import { getUserDeptId } from "@/api/system/user";
import { listDept } from "@/api/system/dept";
import { listTunnels, devicessize } from "@/api/information/api.js";
const cityOptions = ["上海", "北京", "广州", "深圳"];
export default {
  name: "Device",
  components: {
    addinfo,
  },
  data() {
    return {
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      userDeptId:'',
      tunnelQueryParams: {
        deptId: this.userDeptId,
      },
      deptList:[],//分中心下拉框
      mechanismList:[],//管理机构下拉框
      tunnelData:[],//所属隧道下拉框
      positionList:[],//位置信息下拉框
      devicessizeList:[],//分辨率下拉框
      checkbox:false,
      form:{
        company:null,
        mechanism:null,
        tunnel:null,
        position:null,
        devicessize:null,
      },
      dialogVisible: false,
      activeNames: "1",
      checkAll: false,
      checkedCities: [],
      cities: cityOptions,
      isIndeterminate: true,
      contentList: [
        { content: "日照服务区可以做核酸", color: "red", size: "20" },
        { content: "日照服务区可以做核酸", color: "red", size: "20" },
      ],
      templateList: [
        {
          id: "1",
          name: "默认模板",
          list: [
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
          ],
        },
        {
          id: "2",
          name: "日常通用",
          list: [
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
          ],
        },
      ],
    };
  },
  created() {
    this.getDeptList();
  },
  methods: {
    /** 查询部门列表 */
    getDeptList() {
      var that = this;
      var id = this.userDeptId;
      const params = {
        status: 0,
      };
      listDept(params).then((response) => {
        var list = that.handleTree(response.data, "deptId");
        this.deptList = list[0].children
      })
    },
    //通过分中心查机构 
    changeCompany(val){
      for(let item of this.deptList){
        if(val == item.deptId){
          console.log(item.children);
          this.mechanismList = item.children
          this.form.mechanism = null
          this.form.tunnel = null
        }
      }
    },
    // 通过所属机构查隧道
    changeMechanism(val){
      listTunnels(val).then((response) => {
        this.tunnelData = response.rows
        this.form.tunnel = null
        this.getPosition()
      });
    },
    // 位置信息
    getPosition(){
      this.getDicts("iot_devices_type").then((response) => {
          console.log(response,"位置信息")
          this.positionList = response.data
          this.getdevicessize()
      });
    },
    // 查分辨率
    getdevicessize(){
      devicessize().then((res) =>{
        console.log(res,"查分辨率");
        this.devicessizeList = res.data
      })
    },
    // 查设备多选框
    changeDevicessize(){
      this.checkbox = true
      const param = {
        
      }
    },
    // 打开添加信息弹窗
    openDialogVisible() {
      this.$refs.addinfo.init();
    },
    handleChange(val) {
      console.log(val);
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
    },
    // handleClose(done) {
    //   this.$confirm("确认关闭？")
    //     .then((_) => {
    //       done();
    //     })
    //     .catch((_) => {});
    // },
    onSubmit() {},
    moveBottom() {},
  },
};
</script>
<style lang="scss" scoped>
.container {
  .el-col {
    height: 100%;
    .bigTitle {
      padding: 15px 0;
      border-bottom: 1px solid #f3f3f3;
      margin-bottom: 20px;
    }
    .contentBox {
      width: 100%;
      .listBox {
        height: 75px;
        display: grid;
        grid-template-columns: 3% auto 10%;
        align-content: center;
        column-gap: 20px; //左右
        row-gap: 40px; //上下
        margin-bottom: 25px;
        .indexBox {
          display: flex;
          flex-flow: column;
          align-items: center;
          justify-content: space-evenly;
          i {
            font-size: 20px;
          }
        }
        .con {
          border: 1px solid #f3f3f3;
          height: 75px;
          line-height: 75px;
          text-align: center;
        }
        .menuBox {
          display: flex;
          align-items: center;
          i {
            font-size: 24px;
            color: #666;
          }
        }
      }
      .controlBox {
        display: flex;
        justify-content: center;
      }
    }
    .templateBox {
      width: 100%;
      .con {
        height: 75px;
        display: grid;
        grid-template-columns: auto 10%;
        align-content: center;
        column-gap: 20px; //左右
        row-gap: 40px; //上下
        margin-bottom: 25px;
        .templateTitle {
          height: 75px;
          line-height: 75px;
          text-align: center;
          border: 1px solid #f3f3f3;
        }
        .menuBox {
          display: flex;
          align-items: center;
          i {
            font-size: 24px;
            color: #666;
          }
        }
      }
      .controlBox {
        margin-top: 20px;
        display: flex;
        justify-content: center;
      }
    }
  }
  .checkbox {
    label {
      width: 100%;
      padding: 10px 0;
      box-sizing: border-box;
    }
  }
}
</style>