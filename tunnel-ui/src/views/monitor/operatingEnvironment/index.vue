<template>
  <!-- 全景数据 运行环境 -->
  <div style="padding:0.5vh 0.5vw;font-size: 14px;">
    <el-row :gutter="10">
      <el-col :span="4">
        <!-- 左侧树形组件 -->
        <el-card class="box-card-left">
          <loop-tree height="calc(100vh - 220px)" :powerCode="powerCode" @defaultSelect="defaultSelectLoop" @nodeClick="handleClick" :default_select_first="true" :default_check_first="true"></loop-tree>
        </el-card>
      </el-col>
      <el-col :span="20">
        <!-- 状态 水浸 烟感 声光报警 气体检测 -->
        <div class="operatingEnvironment_top">
          <el-row :gutter="10">
            <el-col :span="6" v-for="item in topStateList" :key="item.id">
              <el-card>
                <div class="state_style">
                  <div class="state_style_header">
                    <span>{{item.tit}}</span>
                    <button class="state_style_header_btn" @click="dialogTableVisibleFn(item)">详情</button>
                  </div>
                  <div class="state_style_center">
                    <div class="state_style_center_t">
                      <img :src="item.img" alt="">
                    </div>
                    <div class="state_style_center_z">
                      <div class="state_style_center_z_1">
                        <div class="state_style_center_z_1_1" style="height:30%"></div>
                      </div>
                      <div class="state_style_center_z_2">
                        <div class="state_style_center_z_1_2" style="height:50%"></div>
                      </div>
                      <div class="state_style_center_z_3">
                        <div class="state_style_center_z_1_3" style="height:60%"></div>
                      </div>
                    </div>
                    <div class="state_style_center_s">
                      <div class="state_style_center_s_zc">
                        <span class="state_style_center_s_zc_dot"></span>
                        <span class="state_style_center_s_zc_t">正常</span>
                        <span class="state_style_center_s_zc_n">{{item.zc}}</span>
                      </div>
                      <div class="state_style_center_s_yc">
                        <span class="state_style_center_s_yc_dot"></span>
                        <span class="state_style_center_s_yc_t">异常</span>
                        <span class="state_style_center_s_yc_n">{{item.yc}}</span>
                      </div>
                      <div class="state_style_center_s_lx">
                        <span class="state_style_center_s_lx_dot"></span>
                        <span class="state_style_center_s_lx_t">离线</span>
                        <span class="state_style_center_s_lx_n">{{item.lx}}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <!-- 详情弹框 -->
          <el-dialog title="详情" :visible.sync="dialogTableVisible">
            <el-table :data="gridDataXqList">
              <el-table-column property="deviceName" label="设备名称" width="150"></el-table-column>
              <el-table-column property="deviceCode" label="设备编号" width="200"></el-table-column>
              <el-table-column property="deviceStatus" label="设备状态"></el-table-column>
            </el-table>
          </el-dialog>
        </div>
        <!-- 温湿度传感器 工业空调 -->
        <div class="operatingEnvironment_center">
          <el-row :gutter="10">
            <!-- 温湿度传感器 -->
            <el-col :span="12">
              <el-card>
                <div class="tit_wenDuHeRiQi">
                  <div class="tit_wenDuHeRiQi_tit">温湿度传感器</div>
                  <div class="block">
                    <el-date-picker size="mini" v-model="WenShiDuDateValue" type="date" placeholder="请选择日期" @change="WenShiDuDateFn">
                    </el-date-picker>
                  </div>
                  <div class="block">
                    <el-select v-model="WenShiDuValue" placeholder="请选择设备" size="mini" @change="WenShiDuValueFn">
                      <el-option v-for="item in wenShiDuOptions" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </div>
                </div>
                <el-row>
                  <el-col :span="8">
                    <div class="wenShiDu">
                      <div class="wenShiDu_style">
                        <div class="wenDu_press">
                          <el-progress :stroke-width="12" type="circle" :width="100" :percentage="wenDuV" :text-inside="true" status="warning"></el-progress>
                        </div>
                        <div class="wenDu_value">{{wenDuV + '℃'}}</div>
                        <div class="shiDu_press">
                          <el-progress type="circle" :width="160" :stroke-width="12" :percentage="shiDuV" :text-inside="true"></el-progress>
                        </div>
                        <div class="shiDu_value">{{shiDuV + '%'}}</div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="16">
                    <div class="grid-content bg-purple">
                      <echartsEnvironment :powerCode="powerCode" :WenShiDuDateValue="WenShiDuDateValue" :WenShiDuValue="WenShiDuValue" />
                    </div>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
            <!-- 工业空调 -->
            <el-col :span="12">
              <el-card>
                <div class="tit_wenDuHeRiQi">
                  <div class="tit_wenDuHeRiQi_tit">工业空调</div>
                  <div class="block">
                    <el-date-picker size="mini" v-model="GongYeDateValue" type="date" placeholder="请选择日期" @change="GongYeDateValueFn">
                    </el-date-picker>
                  </div>
                  <div class="block">
                    <el-select v-model="GongYeValue" placeholder="请选择设备" size="mini" @change="GongYeValueFn">
                      <el-option v-for="item in GongYeOptions" :key="item.id" :label="item.label" :value="item.id">
                      </el-option>
                    </el-select>
                  </div>
                </div>
                <el-row>
                  <el-col :span="8">
                    <div class="grid-content bg-purple">
                      <div class="industryModel">
                        <div class="industryModel_tit">工作模式</div>
                        <div class="industryModel_select">
                          <el-select v-model="workValue" placeholder="请选择" size="mini">
                            <el-option v-for="item in WorkOptions" :key="item.id" :label="item.label" :value="item.id">
                            </el-option>
                          </el-select>
                        </div>
                      </div>
                      <div class="industryWen">
                        <div class="industryModel_tit">设置温度</div>
                        <div class="industryModel_select">
                          <el-input-number size="mini" v-model="industryWen" @change="industryWenFn"></el-input-number>
                        </div>
                      </div>
                      <div class="industryWen">
                        <div class="industryModel_tit">设置湿度</div>
                        <div class="industryModel_select">
                          <el-input-number size="mini" v-model="industryShi" @click="industryShiFn"></el-input-number>
                        </div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="16">
                    <div class="grid-content bg-purple-light">
                      <echartsIndustry :powerCode="powerCode" :GongYeDateValue="GongYeDateValue" :GongYeValue="GongYeValue" />
                    </div>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
        </div>
        <!-- 摄像头 门禁 -->
        <div class="operatingEnvironment_bottom">
          <el-row :gutter="10">
            <!-- 摄像头 -->
            <el-col :span="16">
              <el-card>
                <div class="tit_wenDuHeRiQi">
                  <div class="tit_wenDuHeRiQi_tit">高压室摄像头</div>
                  <button class="state_style_header_btn">回放</button>
                </div>
                <div class="sxt_shiPing">
                  <div class="shiPing_list" v-for="(item ,index) in 2" :key="index" :style="(index > 0)? 'margin-left:1vw;' : ''">
                    <!-- <img style="width:100%;height: 100%;" src="../../../assets/images/stateImg/sxt.png" alt=""> -->
                    <video v-if="(cameraUrlList.length > 1)" :url="item.flvUrl" />
                    <el-empty v-else :image-size="100" description="暂无视频"></el-empty>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="menJin">
                <div class="menJin_top">
                  <div class="tit_wenDuHeRiQi menJin_top_men">
                    <div class="tit_wenDuHeRiQi_tit">门禁</div>
                    <button class="state_style_header_btn">详情</button>
                  </div>
                </div>
                <div class="menJin_switch" v-for="(item,index) in 4" :key="index">
                  <div class="menJin_switch_img">
                    <img src="../../../assets/images/stateImg/门禁.png" alt="">
                  </div>
                  <div class="menJin_switch_name">高低压室东侧门门禁</div>
                  <div class="menJin_switch_name_type_k">开启状态</div>
                  <!-- <div class="menJin_switch_name_type_g">关闭状态</div> -->
                  <div class="menJin_switch_btn">
                    <el-switch v-model="menJinValue">
                    </el-switch>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echartsEnvironment from './echartsEnvironment.vue'
import echartsIndustry from './echartsIndustry.vue'
import LoopTree from '@/views/components/powerTree/eTree.vue'
import { powerTreeselect } from '@/api/configcenter/power'
import { getTypeListApi, getTypeListXqApi, getSelectListEqApi, getSelectListDataEqApi, fixIndustryModelApi, getCameraListApi } from '@/api/panoramicData/index.js'
import {getToken} from "@/api/panoramicData/indexToken";
/*import video from '@/components/videoPlayer/myVideo.vue'*/
export default {
  name: 'operatingEnvironment',
  data() {
    return {
      // 摄像头数组
      cameraUrlList: [],
      deviceCode: '',
      // 详情数组
      gridDataXqList: [],
      //当前供配电单元编码
      powerCode: '',
      menJinValue: '', // 门禁开关
      industryWen: '31', // 工业空调 温度
      industryShi: '40', // 工业空调湿度
      // 内环温度值
      wenDuV: 10,
      // 外环湿度值
      shiDuV: 10,
      WenShiDuDateValue: new Date(), // 温湿度传感器日期选中
      GongYeDateValue: new Date(), // 工业空调日期选中
      // 温湿度 选项框
      wenShiDuOptions: [],
      WenShiDuValue: '', // 温湿度设备
      // 工业空调 选项框
      GongYeOptions: [],
      GongYeValue: '', // 工业设备 选中
      // 工业空调 工作模式 选项框
      WorkOptions: [],
      workValue: '', // 工作模式 选中
      electricityValue: '', // 右上角下拉框 配电室选择选中的值
      // 水浸 烟感 声光报警器 气体检测
      topStateList: [
        {
          id: 1,
          tit: '水浸',
          img: require('../../../assets/images/stateImg/水浸.png'),
          zc: '',
          yc: '',
          lx: '',
          code: 'water-invasion-detection'
        },
        {
          id: 2,
          tit: '烟感',
          img: require('../../../assets/images/stateImg/烟感.png'),
          zc: '',
          yc: '',
          lx: '',
          code: 'smoke-detection'
        },
        {
          id: 3,
          tit: '声光报警',
          img: require('../../../assets/images/stateImg/报警器.png'),
          zc: '',
          yc: '',
          lx: '',
          code: 'audible-and-visual-alarm'
        },
        {
          id: 4,
          tit: '红外测温',
          img: require('../../../assets/images/stateImg/气体.png'),
          zc: '',
          yc: '',
          lx: '',
          code: 'red-tem'
        }
      ],
      // 详情 弹框展示与隐藏
      dialogTableVisible: false,
      // 树形组件默认数据
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },

  props: {},
  // 注册组件
  components: { echartsEnvironment, LoopTree, echartsIndustry/*, video*/ },
  // 计算属性
  computed: {},
  // 生周期函数
  created() {
    this.initFn()
  },
  // 侦听器
  watch: {
    powerCode: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
        this.getSelectListEqFn()
        this.getCameraDataListFn()
      }
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    },
    wenDuV: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {}
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    },
    shiDuV: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {}
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      // deep: true
    },
    topStateList: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {},
      // true表示侦听器立即触发(immediate默认为false)
      // immediate: true,
      // true表示深度侦听，侦听对象属性的变化
      deep: true
    }
  },
  // 事件函数
  methods: {
    async initFn() {
      const res = await powerTreeselect()
      this.powerCode = res.data[0].code
      if (res.data[0].code) {
        this.getTypeFn()
      }

      let item = localStorage.getItem("cmts");
      if(item == 'undefined'){
        getToken().then((res) => {
          localStorage.setItem("cmts", res.data);
        })
      }
    },
    // 获取
    getTypeFn() {
      getTypeListApi({ powerCode: this.powerCode, categoryCode: 'water-invasion-detection' }).then(res => {
        this.configurationStatus(res, 0)
      })
      getTypeListApi({ powerCode: this.powerCode, categoryCode: 'smoke-detection' }).then(res => {
        this.configurationStatus(res, 1)
      })
      getTypeListApi({ powerCode: this.powerCode, categoryCode: 'audible-and-visual-alarm' }).then(res => {
        this.configurationStatus(res, 2)
      })
      getTypeListApi({ powerCode: this.powerCode, categoryCode: 'red-tem' }).then(res => {
        this.configurationStatus(res, 3)
      })
    },
    // 详情事件
    async dialogTableVisibleFn(item) {
      this.dialogTableVisible = true
      const res = await getTypeListXqApi({ powerCode: this.powerCode, categoryCode: item.code })
      this.gridDataXqList = res.data.map(item => {
        if (item.deviceStatus === 0) {
          return {
            ...item,
            deviceStatus: '离线'
          }
        } else if (item.deviceStatus === 1) {
          return {
            ...item,
            deviceStatus: '正常'
          }
        } else if (item.deviceStatus === 2) {
          return {
            ...item,
            deviceStatus: '报警'
          }
        }
      })
    },
    // 左侧树形组件点击事件
    handleClick(data) {
      console.log(data)
      this.powerCode = data.code
      this.getTypeFn()
    },
    //默认高亮选中回路回调
    defaultSelectLoop(key, node) {
      // console.log(key, node)
      // this.loopId = key
      // this.dataName = node.loopName
      // this.queryParams.code = node.deviceCode
      // this.queryParams.onlyId = this.loopId
      // this.queryParams.type = '1'
      // this.queryParamsDay.dataItemCode = node.deviceCode //设备编码
      // this.queryParamsDay.type = '1'
      // this.getData()
      // this.getDataDay()
    },
    // 获取温湿度 圆形条
    async getProgressBarFn(obj) {
      const a = await getSelectListDataEqApi(obj)
      for (const k in a.data) {
        if (a.data[k].name === '温度') {
          if (a.data[k].value === '-') {
            this.wenDuV = 0
          } else {
            this.wenDuV = a.data[k].value
          }
        } else if (a.data[k].name === '湿度') {
          if (a.data[k].value === '-') {
            this.shiDuV = 0
          } else {
            this.shiDuV = a.data[k].value
          }
        }
      }
    },
    // 获取摄像头数据
    async getCameraDataListFn() {
      // 获取摄像头编号
      const f = await getSelectListEqApi({ powerCode: this.powerCode, categoryCode: 'hik-camera' })
      // console.log(f)
      // 获取摄像头url
      // if (f.data) {
      //   this.cameraUrlList = []
      //   f.data.forEach(async item => {
      //     const g = await getCameraListApi({ powerCode: this.powerCode, deviceCode: item.id })
      //     console.log(g);
      //     // this.cameraUrlList.push(g.data)
      //     // console.log(this.cameraUrlList);
      //   })
      // }
    },
    // 获取温湿度下拉框设备
    async getSelectListEqFn() {
      if (this.powerCode) {
        const res = await getSelectListEqApi({ powerCode: this.powerCode, categoryCode: 'tem-hum' })
        this.wenShiDuOptions = res.data
        this.WenShiDuValue = res.data[0].id
        let obb = {}
        obb.powerCode = this.powerCode
        obb.deviceCode = res.data[0].id
        this.getProgressBarFn(obb)
        const b = await getSelectListEqApi({ powerCode: this.powerCode, categoryCode: 'GYKT' })
        this.GongYeOptions = b.data
        this.GongYeValue = b.data[0].id
        // 首次获取工业空调 温度 湿度
        let bjo = {}
        bjo.powerCode = this.powerCode
        bjo.deviceCode = this.GongYeValue
        const d = await getSelectListDataEqApi(bjo)
        // console.log(d)
        // // 获取摄像头编号
        // const f = await getSelectListEqApi({ powerCode: this.powerCode, categoryCode: 'hik-camera' })
        // // console.log(f)
        // // 获取摄像头url
        // this.cameraUrlList = []
        // if (f.data) {
        //   f.data.forEach(async item => {
        //     const g = await getCameraListApi({ powerCode: this.powerCode, deviceCode: item.id })
        //     // console.log(g);
        //     this.cameraUrlList.push(g.data)
        //     // console.log(this.cameraUrlList);
        //   })
        // }
      }
      // 获取门禁 getSelectListEqApi
      // const n = await getSelectListEqApi({ powerCode: this.powerCode, categoryCode: 'entrance-guard-detection' })
      // if (n.data) {
      //   n.data.forEach(async item => {
      //     const u = await getSelectListDataEqApi({ powerCode: this.powerCode, deviceCode: item.id })
      //     console.log(u)
      //   })
      // }
    },
    // 工业空调按钮的温度 设置
    async industryWenFn() {
      let obj = {}
      obj.powerCode = this.powerCode
      obj.deviceCode = this.GongYeValue
      obj.dataItemCode = 'tem'
      obj.value = this.industryWen
      const a = await fixIndustryModelApi(obj)
      this.$modal.msgSuccess('修改成功')
    },
    // 工业空调按钮的湿度 设置
    async industryShiFn() {
      let obj = {}
      obj.powerCode = this.powerCode
      obj.deviceCode = this.GongYeValue
      obj.dataItemCode = 'hum'
      obj.value = this.industryWen
      const a = await fixIndustryModelApi(obj)
      this.$modal.msgSuccess('修改成功')
    },
    // 温湿度 日期 下拉框
    async WenShiDuDateFn() {},
    // 温湿度 设备 下拉框
    async WenShiDuValueFn() {
      let obb = {}
      obb.powerCode = this.powerCode
      obb.deviceCode = this.WenShiDuValue
      this.getProgressBarFn(obb)
    },
    // 工业空调 日期
    GongYeDateValueFn() {},
    // 工业空调 下拉框
    async GongYeValueFn() {
      let bjo = {}
      bjo.powerCode = this.powerCode
      bjo.deviceCode = this.GongYeValue
      console.log(bjo)
      const d = await getSelectListDataEqApi(bjo)
      console.log(d)
    },

    // 方法
    configurationStatus(data, num) {
      data.data.forEach(item => {
        if (item.deviceStatus === '0') {
          this.topStateList[num].lx = item.countNum
        } else if (item.deviceStatus === '1') {
          this.topStateList[num].zc = item.countNum
        } else if (item.deviceStatus === '2') {
          this.topStateList[num].yc = item.countNum
        }
      })
    }
  }
}
</script>
<style lang='scss' scoped>
.top_peiDianShi_select {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-bottom: 1vh;
}
.state_style_header {
  padding: 1vh 0;
  display: flex;
  justify-content: space-between;
  color: #478acb;
}
.state_style_header_btn {
  border: 1px solid #82bae4;
  background-color: #fff;
  color: #2c91df;
  border-radius: 3px;
  cursor: pointer;
}
.state_style_center {
  margin-top: 1vh;
  display: flex;
  justify-content: space-between;
  .state_style_center_t {
    width: 5.8vw;
    height: 11vh;
    background-color: #ecf5ff;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
  }
  .state_style_center_z {
    width: 5.8vw;
    height: 11vh;
    padding: 1vh 0.5vw;
    display: flex;
    justify-content: space-around;
    .state_style_center_z_1 {
      width: 0.6vw;
      height: 100%;
      background-color: #478acb;
      .state_style_center_z_1_1 {
        display: block;
        width: 100%;
        background-color: #f2f2f2;
      }
    }
    .state_style_center_z_2 {
      width: 0.6vw;
      height: 100%;
      background-color: #f7747c;
      .state_style_center_z_1_2 {
        display: block;
        width: 100%;
        background-color: #f2f2f2;
      }
    }
    .state_style_center_z_3 {
      width: 0.6vw;
      height: 100%;
      background-color: #c8c8c8;
      .state_style_center_z_1_3 {
        display: block;
        width: 100%;
        background-color: #f2f2f2;
      }
    }
  }
  .state_style_center_s {
    width: 5.8vw;
    height: 11vh;
    padding: 1vh 0.5vw;
    .state_style_center_s_zc {
      width: 100%;
      height: 25%;
      .state_style_center_s_zc_dot {
        display: inline-block;
        width: 0.5vw;
        height: 1vh;
        background-color: #478acb;
        border-radius: 50%;
        margin-right: 5px;
      }
      .state_style_center_s_zc_t {
        display: inline-block;
        margin-right: 10px;
      }
      .state_style_center_s_zc_n {
        color: #478acb;
        font-weight: 700;
      }
    }
    .state_style_center_s_yc {
      width: 100%;
      height: 25%;
      margin: 10px 0;
      .state_style_center_s_yc_dot {
        display: inline-block;
        width: 0.5vw;
        height: 1vh;
        background-color: #f7747c;
        border-radius: 50%;
        margin-right: 5px;
      }
      .state_style_center_s_yc_t {
        display: inline-block;
        margin-right: 10px;
      }
      .state_style_center_s_yc_n {
        color: #f7747c;
        font-weight: 700;
      }
    }
    .state_style_center_s_lx {
      width: 100%;
      height: 25%;
      .state_style_center_s_lx_dot {
        display: inline-block;
        width: 0.5vw;
        height: 1vh;
        background-color: #c8c8c8;
        border-radius: 50%;
        margin-right: 5px;
      }
      .state_style_center_s_lx_t {
        display: inline-block;
        margin-right: 10px;
      }
      .state_style_center_s_lx_n {
        color: #c8c8c8;
        font-weight: 700;
      }
    }
  }
}

.box-card-state {
  width: 18vw;
}
.box-card-left {
  width: 100%;
  height: 85vh;
}
.operatingEnvironment_center {
  margin: 1vh 0;
}
.tit_wenDuHeRiQi {
  padding: 0.74vh 0;
  display: flex;
  justify-content: space-between;
  .tit_wenDuHeRiQi_tit {
    color: #4685e1;
    font-weight: 700;
  }
}
.wenShiDu_style {
  position: relative;
  .wenDu_press {
    margin: 7vh 0 5vh 3.5vw;
  }
  .shiDu_press {
    position: absolute;
    left: 1.8vw;
    top: -3vh;
  }
}
.wenDu_value {
  position: absolute;
  top: 35%;
  left: 41%;
}
.shiDu_value {
  position: absolute;
  top: 135%;
  left: 41%;
}
.industryModel {
  display: flex;
  margin: 5vh 0 0vh 1vw;
  .industryModel_tit {
    margin-right: 8px;
  }
  .industryModel_select {
    width: 6.8vw;
  }
}
.industryWen {
  display: flex;
  margin: 2vh 0 0vh 1vw;
  .industryModel_tit {
    margin-right: 8px;
  }
  .industryModel_select {
    width: 5vw;
  }
}
.sheXTou_ {
  display: flex;
  justify-content: flex-end;
}
.sxt_shiPing {
  margin-top: 1vh;
  display: flex;
  // justify-content: space-between;
  .shiPing_list {
    width: 17vw;
    height: 19vh;
  }
}
.menJin_top_men {
  padding-bottom: 3vh;
  border-bottom: 1px solid #eee;
}
.menJin {
  height: 28.4vh;
  overflow: auto;
}
.menJin_switch {
  width: 100%;
  height: 6vh;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;
  .menJin_switch_img {
    width: 1.5vw;
    height: 3vh;
    margin-left: 0.8vw;
    background-color: #ecf5ff;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
  }
  .menJin_switch_name {
    margin-left: 10px;
  }
  .menJin_switch_name_type_k {
    padding: 1px 2px;
    font-size: 12px;
    background-color: #ecf5fc;
    color: #4686ee;
    margin-left: 8px;
  }
  .menJin_switch_name_type_g {
    padding: 1px 2px;
    font-size: 12px;
    background-color: #e1e2e5;
    color: #fff;
    margin-left: 8px;
  }
  .menJin_switch_btn {
    margin-left: 15%;
  }
}
</style>
