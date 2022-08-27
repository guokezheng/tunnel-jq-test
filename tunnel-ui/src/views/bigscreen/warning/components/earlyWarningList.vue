<template>
    <div>
        <div class="contentTitle">预警信息列表
          <i>Alert information list</i>
        </div>
        <div class="alarmsStatisticsBox">
             <div class="listHeader">
                 <ul>
                 	<li style="font-size: 0.8vw;display: flex;width: 100%;">
                 		<div class="headerIndex" >序号</div>
                 		<div class="headerName" >预警信息</div>
                 	</li>
                 </ul>
             </div>
            <vue-seamless-scroll
              :class-option="defaultOption"
              class="listContent"
              :data="faultList"
            >
              <el-row v-for="(item, index) in faultList" :key="index" type="flex"
              :style="{backgroundColor:((index+1)%2 == 0) ? 'rgba(4, 19, 86,0.1)' : '#041356'}">
                  <el-col style="width: 6vw;text-align: center;">{{index+1}}</el-col>
                  <el-col style="width: 26vw;">{{item.content}}</el-col>
              </el-row>
            </vue-seamless-scroll>
         
        </div>
    </div>
</template>

<script>
    import vueSeamlessScroll from "vue-seamless-scroll";
    export default{
        data(){
            return{
                faultList:[
                    {content:'10:26隧道下行K350+137发生CO浓度超标预警'},
                    
                    ]
            }
        },
        computed: {
        	defaultOption() {
        		return {
        			step: 0.2, // 数值越大速度滚动越快
        			limitMoveNum: 6, // 开始无缝滚动的数据量 this.dataList.length
        			hoverStop: true, // 是否开启鼠标悬停stop
        			direction: 1, // 0向下 1向上 2向左 3向右
        			openWatch: true, // 开启数据实时监控刷新dom
        			singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        			singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        			waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
        		}
        	}
        },
        methods:{
            
        }
    }
</script>

<style>
</style>
