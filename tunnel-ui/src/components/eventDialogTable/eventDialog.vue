<template>
  <div>
    <div class="eventBox">
      <div class="title">
        事件详情
        <img
          src="../../assets/cloudControl/dialogHeader.png"
          style="height: 30px"
        />
      </div>
      <div class="blueLine"></div>
      <div style="display: flex; width: 100%; height: calc(100% - 40px)">
        <div class="eventLeft">
          <div class="video">
            <video
              :src="videoUrl"
              controls
              muted
              autoplay
              loop
              style="
                width: 100%;
                height: 390px;
                object-fit: cover;
                border-radius: 10px;
              "
            ></video>
            <el-image :src="require('@/assets/icons/outline.png')" v-show="!videoUrl"/>
          </div>
          <div class="pic">
            <el-carousel
              :interval="4000"
              type="card"
              indicator-position="none"
              height="200px"
            >
              <el-carousel-item
                v-for="(item, index) in urls"
                :key="index"
                :src="item.imgUrl"
              >
                <img :src="item.imgUrl" style="width: 100%" />
                <el-image :src="require('@/assets/icons/outline.png')" v-show="!item.imgUrl || urls.length==0"/>

              </el-carousel-item>
            </el-carousel>
          </div>
        </div>
        <div class="eventRight">
          <div class="eventRow">
            <div>隧道名称:</div>
            <div v-if="eventMes.tunnels">{{ eventMes.tunnels.tunnelName}}</div>
          </div>
          <div class="eventRow">
            <div>事件类型:</div>
            <div>{{ eventMes.eventTypeId }}</div>
          </div>
          <div class="eventRow">
            <div>车道号:</div>
            <div>{{ eventMes.laneNo }}</div>
          </div>
          <div class="eventRow">
            <div>事件位置经度:</div>
            <div>{{ eventMes.eventLongitude }}</div>
          </div>
          <div class="eventRow">
            <div>事件位置纬度:</div>
            <div>{{ eventMes.eventLatitude }}</div>
          </div>
          <div class="eventRow">
            <div>事件桩号:</div>
            <div>{{ eventMes.stakeNum }}</div>
          </div>
          <div class="eventRow">
            <div>事件开始时间:</div>
            <div>{{ eventMes.startTime }}</div>
          </div>
          <div class="eventRow">
            <div>事件结束时间:</div>
            <div>{{ eventMes.endTime }}</div>
          </div>

          <div style="width: 90%; display: flex; margin-top: 20px">
            <div class="handle button" @click="handleDispatch(eventMes)">
              处 理
            </div>
            <div class="ignore button" @click="handleIgnore(eventMes)">
              忽 略
            </div>
          </div>
        
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { mapState } from 'vuex';
import bus from "@/utils/bus";
import { loadPicture } from "@/api/equipment/type/api.js";
import { image, video } from "@/api/eventDialog/api.js";
import { updateEvent } from "@/api/event/event";
export default {
  name: "eventDialog",
  // props: ["eventMes"],
  data() {
    return {
      // eventList: [],
      urls: [],
      videoUrl: '',
      row11:null,
      // event: [{}],
      eventMes:[]
    };
  },
  created() {
    // console.log(this.eventMes, "eventMeseventMeseventMeseventMes");
    this.getDicts("sd_event_source").then((data) => {
      console.log(data, "事件来源");
      this.tabList = data.data;
    });
      // console.log(this.row11,"9999999999");

   
  },
  methods: {
    init(row){
      console.log(row,"initrow");
      this.eventMes = row
      this.getUrl()
    },
    getEventSource(num) {
      for (var item of this.tabList) {
        if (num == item.eventSource) {
          return item.dictLabel;
        }
      }
    },
    getUrl() {
      console.log(this.eventMes.id, "当前事件id");
      const param3 = {
        businessId: this.eventMes.id,
      };
      const param4 = {
        id: this.eventMes.id,
      };
      image(param3).then((response) => {
        console.log(response.data, "获取图片");
        this.urls = response.data;
      });
      video(param4).then((response) => {
        console.log(response.data, "获取视频");
        this.videoUrl = response.data.videoUrl;
      });
    },

    // 忽略事件
    handleIgnore() {
      // console.log(event,"三图一视忽略按钮")
      // const param = {
      //   id: event.id,
      //   eventState: "2",
      // };
      // updateEvent(param).then((response) => {
      //   console.log(response, "修改状态");
      //   this.$modal.msgSuccess("已成功忽略");
      // });
      // bus.$emit("closeDialog", false);
      // this.$parent("closeDialog")
      this.$emit('fMethod');
    },
    // 处理 跳转应急调度
    handleDispatch(event) {
      console.log(event,"三图一视处理 跳转应急调度")
      const param = {
        id: event.id,
        eventState: "0",
      };
      updateEvent(param).then((response) => {
        console.log(response, "修改状态");
        this.$modal.msgSuccess("开始处理事件");
      });
      bus.$emit("closeDialog", false);
      this.$router.push({
        path: "/emergency/administration/dispatch",
        query: { id: event.id },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.eventBox {
  width: 52%;
  height: 660px;
  border: solid 1px rgba($color: #0198ff, $alpha: 0.5);
  position: absolute;
  top: 10%;
  left: 25%;
  background-color: #071930;
  z-index: 100;
  > .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    background: linear-gradient(
      270deg,
      rgba(1, 149, 251, 0) 0%,
      rgba(1, 149, 251, 0.35) 100%
    );
    border-top: solid 2px white;
    display: flex;
    justify-content: space-between;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 1 10;
  }
  .blueLine {
    width: 20%;
    height: 1px;
    border-bottom: solid 1px white;
    margin-bottom: 20px;
    border-image: linear-gradient(to right, #0083ff, #3fd7fe, #0083ff) 30 30;
  }
}
.eventLeft {
  width: 70%;
  height: 590px;
  padding: 0px 20px;
  .video {
    width: 100%;
    height: 390px;
    margin-top: 0px !important;
  }
  .pic {
    width: 100%;
    height: 180px;
    margin-top: 10px;
  }
}
::v-deep .el-carousel__mask {
  background-color: transparent;
}

.eventRight {
  width: 30%;
  height: 590px;
  color: white;
  font-size: 16px;
  .eventRow {
    display: flex;
    height: 50px;
    > div:nth-of-type(1) {
      width: 140px;
      color: #0198ff;
    }
    > div {
      line-height: 22px;
    }
  }
  .button {
    width: 60%;
    height: 40px;
    margin-top: 15px;
    border-radius: 10px;
    border: solid 1px #00c8ff;
    text-align: center;
    line-height: 40px;
    margin-left: 10px;
    cursor: pointer;
  }
  .handle {
    color: #e1aa43;
  }
  .handle:hover {
    background-color: #e1aa43;
    color: white;
  }
  .ignore {
    color: #19b9ea;
  }
  .ignore:hover {
    background-color: #19b9ea;
    color: white;
  }
  .next {
    color: #fff;
  }
  .next:hover {
    background-color: #ddd;
    color: #005487;
  }
}
.videoDialog {
  width: 70%;
  height: 600px;
  border: solid 10px rgba($color: #5ac4e5, $alpha: 0.3);
  position: absolute;
  top: 15%;
  left: 15%;
  background-color: white;
  border-radius: 10px;
  padding: 10px;
  display: flex;
}
.closeButton {
  width: 40px;
  height: 40px;
  border: solid 5px rgba($color: #5ac4e5, $alpha: 0.3);
  position: absolute;
  top: -20px;
  left: calc(100% - 15px);
  border-radius: 20px;
  background-color: white;
  text-align: center;
  line-height: 35px;
  cursor: pointer;
}
::v-deep .el-icon-close {
  font-size: 24px !important;
  color: #005487;
  font-weight: bold;
}
// 滚动条
::-webkit-scrollbar-track-piece {
  background-color: rgba($color: #00c2ff, $alpha: 0.1);
  border-left: 1px solid rgba(0, 0, 0, 0);
}
::-webkit-scrollbar {
  width: 0px;
  height: 10px;
}
::-webkit-scrollbar-thumb {
  background-color: rgba($color: #00c2ff, $alpha: 0.6);
  background-clip: padding-box;
  border-radius: 10px;
  min-height: 28px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #00c2ff;
}
</style>
