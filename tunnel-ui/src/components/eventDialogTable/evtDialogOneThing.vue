<template>
  <div>
    <div class="eventBox"  v-show="evtShow">
      <div class="title">
        事件详情

        <img
          src="../../assets/cloudControl/closeIcon.png"
          style="
            height: 14px;
            position: absolute;
            right: 10px;
            top: 10px;
            cursor: pointer;
          "
          @click="closeDialogTable()"
        />
      </div>
      <div class="listContent">
        <div
          v-for="(item, index) of list"
          :key="index"
          @click="handleSee(item.ids)"
        >
          <el-row>
            <el-col :span="2">
              <img
                :src="item.eventType.iconUrl"
                style="width: 20px; height: 20px; transform: translateY(5px)"
              />
            </el-col>
            <el-col :span="3">
              <div>{{ item.eventType.eventType }}</div>
            </el-col>
            <el-col
              :span="19"
              style="display: flex; justify-content: space-between"
            >
              <el-tooltip
                class="item"
                effect="dark"
                :content="item.eventTitle"
                placement="top"
              >
                <div
                  class="overflowText"
                  style="
                    width: 15vw;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                  "
                >
                  {{ item.eventTitle }}
                </div>
              </el-tooltip>
              <div style="float: right; margin-right: 16px">
                {{ item.startTime }}
              </div>
            </el-col>
          </el-row>
          <div class="lineBT">
            <div></div>
            <div></div>
            <div></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import bus from "@/utils/bus";
import { updateEvent ,getdeptIdTunnelList} from "@/api/event/event";
import evtdialog from "@/components/eventDialogTable/eventDialog"; //只有数据的弹窗
import {getUserDeptId} from "@/api/system/user";

import {getTreeByDeptId} from "@/api/system/dept"; //只有数据的弹窗

export default {
  name: "eventDialogTable",
  components: {
    evtdialog,
  },
  data() {
    return {
      carIcon: require("@/assets/icons/carIcon.png"),
      eventTableDialog: false,
      activeName: "0",
      // formOneThing: {
      //   id: 1,
      //   icon: require("@/assets/icons/carIcon.png"),
      //   title: "停车",
      //   content: "杭山东隧道K147+100处存在停车事件，请确认...",
      //   time: "10:45:33",
      // },
      list: [],
      urls: [],
      videoUrl: require("@/assets/Example/v1.mp4"),
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      userDeptId:"",
      tunnelList:[],
      evtShow:false
    };
  },
  computed: {
    ...mapState({
      sdEventList: (state) => state.websocket.sdEventList,
    }),
  },
  watch: {
    sdEventList: {
      immediate: true,
      handler: function (event) {
        console.log(event, "事件弹窗websockt推送");
        // debugger
        // 存储集合
        let  key = this.$store.state.user.name+"suidao"

        // this.tunnelList = JSON.parse(sessionStorage.getItem(key));
        this.tunnelList = JSON.parse(this.$cache.local.get(key));
        console.log(this.tunnelList,"this.tunnelList11111");

        if(!!this.tunnelList  &&this.tunnelList.length>0){
          this.tunnelDataFind(event)
          console.log(1)
        }else{
          this.getUserDept(event)
          console.log(2)

        }

        // console.log(event, "事件弹窗websockt推送");
        console.log(this.tunnelList,"this.tunnelList22222")
        // this.list = event;
        this.list = event;
      },
    },
    deep: true,
  },
  created() {},
  mounted() {
    // bus.$on("forceUpdateTable", (id) => {
    //   let index = this.list.findIndex((item) => {
    //     if (item.id == id) {
    //       return true;
    //     }
    //   });
    //   this.list.splice(index, 1);
    //   if (this.list.length == 0) {
    //     bus.$emit("closeDialog");
    //   }
    // });
  },
  methods: {
    tunnelDataFind(event){
      let tunnelDataList = []
      // console.log(event)
      for (let i = 0; i < event.length; i++) {
        // event[i].tunnelId
        let tunnelData = this.tunnelList.find(item => item ==event[i].tunnelId)
        if(!!tunnelData){
          tunnelDataList.push(event[i])
        }
      }
      if(tunnelDataList.length>0){
        this.evtShow = true
        this.list = tunnelDataList;
      }
    },
    getUserDept(event) {
      getUserDeptId(this.userQueryParams).then((response) => {
        console.log(response, "管理站级联");
        this.userDeptId = response.rows[0].deptId;
        this.getDeptList(event);
      });
    },
    getDeptList(event) {
      var userDeptId = this.userDeptId;
      const params = { status: 0 };
      getTreeByDeptId(params)
        .then((response) => {
          // debugger
          console.log(response.data, "级联");
          const options = response.data;
          let childs = [];
          function a(list) {
            list.forEach((item) => {
              if (item.id == userDeptId) {
                childs = item.children || [];
              } else {
                item.children && a(item.children);
              }
            });
          }
          a(options);
          if (childs.length == 0) {
            this.siteList = options[0].children;
          } else {
            this.siteList = childs;
          }
          console.log(this.siteList)

          let ids =[]
          for (let i = 0; i < this.siteList.length; i++) {
            if(this.siteList[i].children.length>0){
              for (let j = 0; j < this.siteList[i].children.length; j++) {
                ids.push(this.siteList[i].children[j].id)
              }
            }
          }

          let query = {
            ids:ids.join(",")
          }
          getdeptIdTunnelList(query).then((res) => {
            // debugger
            console.log(res, "根据管理机构筛选所属隧道");
            this.tunnelList =[]
            for (let i = 0; i <  res.data.length; i++) {
              this.tunnelList.push(res.data[i].tunnelId)
            }

            let  key = this.$store.state.user.name+"suidao"
            // sessionStorage.setItem(key, JSON.stringify(this.tunnelList));
            this.$cache.local.set(key, JSON.stringify(this.tunnelList));
            //判断筛选
            this.tunnelDataFind(event)

          });
        })
        .then(() => {
        });
    },
    checkData(obj, arr) {
      if (obj.children && obj.children.length > 0) {
        arr.push(obj.id);
        this.checkData(obj.children[0], arr);
      } else {
        arr.push(obj.id);
        arr.shift();
        this.changeSite(arr);

        this.$forceUpdate();
      }
    },
    handleSee(ids) {
      bus.$emit("getPicId", ids);
    },

    // 忽略事件
    // handleIgnore(id) {
    //   if (id) {
    //     const param = {
    //       id: id,
    //       eventState: "2",
    //     };
    //     updateEvent(param).then((response) => {
    //       this.$modal.msgSuccess("已成功忽略");
    //     });
    //     let index = this.list.findIndex((item) => {
    //       if (item.id == id) {
    //         return true;
    //       }
    //     });
    //     this.list.splice(index, 1);
    //     this.$forceUpdate();
    //     bus.$emit("getEvtList");
    //   } else {
    //     this.$modal.msgError("没有接收到事件id");
    //   }
    // },
    closeDialogTable() {
      bus.$emit("closeDialog");
      this.eventTableDialog = false;
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },
  },
};
</script>

<style lang="scss" scoped>
.eventClass {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
  background-color: rgba($color: #000000, $alpha: 0.1);
}
.eventBox {
  width: 30%;
  max-height: 180px;
  position: absolute;
  top: 0px;
  right: 25px;
  background-color: #00152b;
  z-index: 2000;
  border-bottom: solid 2px white;
  border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
  border-left: solid 1px rgba($color: #0198ff, $alpha: 0.8);
  border-right: solid 1px rgba($color: #0198ff, $alpha: 0.8);
  .el-dialog__body {
    padding: 0 !important;
    width: 100% !important;
  }
  .title {
    padding-left: 1vw;
    height: 3.2vh;
    line-height: 3vh;
    color: white;
    font-size: 0.75vw;
    font-weight: bold;
    background-image: url(../../assets/cloudControl/evtDialogTitle.png);
    background-repeat: no-repeat;
    display: flex;
    justify-content: space-between;
    margin: 0 !important;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
}
.listContent {
  height: 4vh;
  background: #44576f;
  margin: 10px;
  > div {
    height: 100%;
  }
  .el-row {
    color: white;
    font-size: 0.7vw;
    display: flex;
    align-items: center;
    height: 3vh;
    .el-col {
      height: 100%;
      line-height: 4vh;
    }
  }
}
/*table滚动条背景色 */
::-webkit-scrollbar {
  width: 4px;
  background-color: #c4e8f6;
}

/* table滚动条的滑块*/
::-webkit-scrollbar-thumb {
  background-color: #00c2ff;
}
.eventContent {
  width: calc(100% - 40px);
  height: 50px;
  background: rgba($color: #6c8097, $alpha: 0.4);
  margin: 20px auto;
  font-size: 14px;
  padding: 0 20px;
  line-height: 40px;
}
.overflowText {
  width: 248px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.lineBT {
  width: 100%;
  margin: 5px 0px auto;
  display: flex;
  > div:nth-of-type(1) {
    width: 5%;
    border-bottom: #2dbaf5 solid 1px;
  }
  > div:nth-of-type(2) {
    width: 90%;
    border-bottom: 1px solid rgba($color: #00b0ff, $alpha: 0.2);
  }
  > div:nth-of-type(3) {
    width: 5%;
    border-bottom: #2dbaf5 solid 1px;
  }
}
</style>
