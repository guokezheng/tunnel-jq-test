<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-17 14:42:00
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-10-17 16:01:29
 * @FilePath: \tunnel-ui\src\views\event\reservePlan\workBench.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <el-dialog
      :before-close="handleClose"
      :visible.sync="workbenchOpen"
      append-to-body
      title="预览"
      width="1660px"
    >
      <img
        alt=""
        class="chedaoImage"
        src="../../../assets/image/lane/fenghuangshan.png"
      />
      <!-- 设备图标-->
      <div
        v-for="(item, index) in selectedIconList"
        :key="index"
        :class="
          item.eqType == 7 || item.eqType == 8 || item.eqType == 9
            ? 'light-' + item.position.left
            : ''
        "
        :style="{
          left: item.position.left - 12 + 'px',
          top: item.position.top + 52 + 'px',
          'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
        }"
        class="icon-box mouseHover"
      >
        <div
          v-show="
            (item.eqType != 7 &&
              item.eqType != 16 &&
              item.eqType != 15 &&
              item.eqType != 8 &&
              item.eqType != 9 &&
              item.eqType != 21 &&
              item.display == true) ||
            ((item.eqType == 7 ||
              item.eqType == 8 ||
              item.eqType == 9 ||
              item.eqType == 21) &&
              item.display == true &&
              lightSwitch == 1)
          "
          :class="{ focus: item.focus }"
        >
          <img
            v-for="(url, indexs) in item.url"
            :key="item.deptId + indexs"
            :height="item.iconHeight"
            :src="url"
            :style="item.eqType || item.eqType == 0 ? 'cursor: pointer;' : ''"
            :width="item.iconWidth"
            style="position: relative"
          />
        </div>
      </div>
      <el-steps :active="active" finish-status="success">
        <el-step
          v-for="item in previewList"
          :key="item.strategyId"
          :title="item.strategyName"
        ></el-step>
      </el-steps>
      <span slot="footer" class="dialog-footer">
        <el-button @click="workbenchOpenEvent">取 消</el-button>
        <el-button type="primary" @click="workbenchOpenEvent">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addProcess,
  getListByRId,
  previewDisplay,
} from "@/api/event/reserveProcess";
import { getTunnels } from "@/api/equipment/tunnel/api.js";
import { listType, getTypeAndStrategy } from "@/api/equipment/type/api.js";
export default {
  data() {
    return {
      tunnelId: "", //隧道id
      id: "", //事件id
      workbenchOpen: false,
      selectedIconList: null, //设备图标渲染数据
      previewList: null, //预览数据
    };
  },
  methods: {
    init() {
      console.log(this.tunnelId, "隧道id");
      this.getTunnelData();
    },
    // 获取隧道配置
    async getTunnelData() {
      let that = this;
      await getTunnels(this.tunnelId).then((response) => {
        let res = "";
        if (response.data) {
          res = response.data.storeConfigure;
          //存在配置内容
          if (res != null && res != "" && res != undefined) {
            res = JSON.parse(res);
            listType({ isControl: 1 })
              .then((response) => {
                var arr = [];
                for (let item1 of response.rows) {
                  for (let item of res.eqList) {
                    item.focus = false;
                    if (item1.typeId == item.eqType) {
                      item.iconWidth = Number(item1.iconWidth);
                      item.iconHeight = Number(item1.iconHeight);
                      arr.push(item);
                    }
                  }
                }
                this.selectedIconList = arr; //这是最终需要挂载到页面上的值
                console.log(this.selectedIconList, "this.selectedIconList");
              })
              .then(() => {});
          } else {
            //不存在
            that.selectedIconList = [];
            //工作台默认背景图
            // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
            that.upList = [];
            that.downList = [];
            that.leftDirection = "";
            that.rightDirection = "";
          }
          console.log(this.selectedIconList, "当前隧道设备");
        }
      });
      this.getPreview();
    },
    getPreview() {
      previewDisplay(this.id).then((res) => {
        this.previewList = res;
        console.log(this.previewList);
        var deviceList = [];
        for (let i = 0; i < this.previewList.length; i++) {
          var item = this.previewList[i].strategyRl;
          for (let z = 0; z < item.length; z++) {
            var arr = this.previewList[i].iFileList[z];
            if (item[z].equipments.indexOf(",")) {
              deviceList.push({
                list: item[z].equipments.split(","),
                state: item[z].state,
                eqId: this.previewList[i].deviceTypeId,
                file: arr,
              });
            } else {
              deviceList.push({
                list: item[z].equipments,
                state: item[z].state,
                eqId: this.previewList[i].deviceTypeId,
                file: arr,
              });
            }
          }
        }
        console.log(deviceList);
        this.deviceList = deviceList;
        this.ChangeDeviceState();
      });
      this.workbenchOpen = true;
    },
    // 操作设备，改变设备状态
    ChangeDeviceState() {
      // console.log(this.selectedIconList);
      for (let i = 0; i < this.selectedIconList.length; i++) {
        for (let x = 0; x < this.deviceList.length; x++) {
          var eqType = this.selectedIconList[i].eqType;
          if ((eqType ?? "") !== "") {
            if (eqType == this.deviceList[x].eqId) {
              var brr = this.deviceList[x].list;
              for (let p = 0; p < brr.length; p++) {
                if (this.selectedIconList[i].eqId == brr[p]) {
                  this.selectedIconList[i].url = [];
                  console.log(this.deviceList[x].file);
                  let url = this.deviceList[x].file;
                  url.forEach((item) => {
                    this.selectedIconList[i].url.push(item.url);
                  });
                }
              }
            }
          }
        }
      }
    },
    // 关闭弹窗
    workbenchOpenEvent() {
      //   this.getTunnelData(this.tunnelId);
      this.workbenchOpen = false;
    },
    //关闭drawer
    handleClose(done) {
      done();
    },
  },
};
</script>

<style lang="scss" scoped>
.chedaoImage {
  // position: relative;
  width: 1620px;
  height: 580px;
  margin-bottom: 20px;
}
.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  /* // align-items: center; */
  width: 40px !important;
}
</style>