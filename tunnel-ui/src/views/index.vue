<template>
  <div class="app-container home" v-if="sideTheme == 'theme-dark' || sideTheme == 'theme-light'">
    <!--地图示例-->
    <div style="height: 510px;">
      <amap style="height: 510px;width: 100%"/>
      <div class="box_left box_public base-background">
        <div class="title flex-left">
          <i class="el-icon-s-marketing"></i>
          <p>这是左侧数据栏</p>
        </div>
        <div class="data_list" >
            <ul>
              <li v-for="(item,index) in dataList" :key="index">
                {{item.title}}
              </li>
            </ul>
        </div>
      </div>
      <!-- 右侧box -->
      <div class="box_right box_public base-background">
        <div class="title flex-left">
          <i class="el-icon-s-data"></i>
          <p>这是右侧数据栏{{sideTheme}}</p>
        </div>
        <div class="data_list">
            <ul>
              <li v-for="(item,index) in dataList" :key="index">
                {{item.title}}
              </li>
            </ul>
        </div>
      </div>
    </div>
    <el-row style="padding:25px 0;box-sizeing:border-box;">
      <el-col :span="8">
        <cross style="height: 300px;" />
      </el-col>
      <el-col :span="8">
        <pie style="height: 300px;" />
      </el-col>
      <el-col :span="8">
        <eline style="height: 300px;" />
      </el-col>
    </el-row>k

    <!--  dplayer 视频播放示例  -->
    <!-- <div id="dplayer" style="height: 400px; width: 600px"></div> -->
    <!--左侧Box-->
  </div>
  <div v-else>
    <blue></blue>
  </div>
</template>

<script>

// 百度地图组件
// import bmap from '@/components/Gis/bmap'
// 高德地图组件
import amap from '@/components/Gis/amap'// gis component
// 文档 http://dplayer.js.org/zh/guide.html
import DPlayer from 'dplayer'
// Echarts配置项,具体见 https://echarts.apache.org/examples/zh/index.html
import echarts from "echarts";
import cross from '@/views/indexfile/cross'
import pie from '@/views/indexfile/pie'
import eline from '@/views/indexfile/eline'
import blue from '@/views/blue'

export default {
  name: "Index",
  components: {
    amap, // 高德地图
    cross, //折柱混合
    pie, //饼状图
    eline, //折线图
    blue, //科技蓝首页
  },
  data() {
    return {
      center: {lng: 0, lat: 0},
      zoom: 3,
      // 测试数据
      count: 0,
      sideTheme: this.$store.state.settings.sideTheme,
      dataList:[
        {id:1,title:'集成redis实现集群会话'},
        {id:1,title:'集成jwt实现登录授权访问'},
        {id:1,title:'集成cas实现单点登录认证'},
        {id:1,title:'集成docker实现一键部署'},
        {id:1,title:'集成websocket实现实时通信'},
        {id:1,title:'集成atomikos实现分布式事务'},
        {id:1,title:'使用undertow来替代tomcat容器'}
      ],
      cache:[],
    };
  },
  mounted() {
    // console.log(this.sideTheme);
    // 视频
    // this.dplayerHandler();
  },
  methods: {
    /**
     * 百度地图准备好后调用
     * @param map 是地图API的核心类
     * @param gis 地图组件对象
     */
    // bmapHandler(map, gis) {
    //   const point = new BMapGL.Point(116.404, 39.915) // 创建点坐标
    //   map.centerAndZoom(point, 10);
    // },
    // dplayerHandler() {
    //   const dp = new DPlayer({
    //     container: document.getElementById('dplayer'),
    //     video: {
    //       //rtmp 测试流 rtmp://media3.scctv.net/live/scctv_800
    //       //rtsp 测试流 rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
    //       url: 'https://www.w3school.com.cn/i/movie.ogg',
    //     },
    //   });
    // },
  },
};
</script>

<style lang="scss" scoped>
  .app-container{
    padding:0px;
    position: relative;
    #dplayer{
      position: absolute;
      top: 100px;
      left: 100px;
    }
    .box_left{
      position: absolute;
      top: 100px;
      left: 50px;
      // border: 1px solid #ccc;
    }
    .box_right{
      position: absolute;
      top: 100px;
      right: 50px;
      // border: 1px solid #ccc;
    }
    .box_public{
      padding: 15px;
      box-sizing: border-box;
      background-color:rgb(29, 88, 169);
      .flex-left{display: flex;justify-content: left;}
      .title{padding:10px 20px;box-sizing: border-box;border-bottom:1px solid #ccc;color:white;
        i{font-size:25px;margin-right: 10px;}
        p{font-size:16px;margin:0px;}
      }
      .data_list{
        ul{padding: 0px;
          li{list-style: none;padding:10px 0;box-sizing: border-box;color: white;}
        }
      }
    }
  }
</style>

