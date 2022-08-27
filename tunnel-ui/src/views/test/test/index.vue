<template>
 <div>
   <el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>北京卓视智通</span>
        </div>
        <div class="test_background">
          <!-- <svg width="1900px" height="300px" id="svg"  viewBox="0 0 500 500">
            <g>
              <rect x="0" y="0" width="25" height="30" fill="#000000">
                <animate attributeName="x" from="-1800" to="1900" dur="10s" repeatCount="indefinite" />
               <animateMotion  path="M 0 0 L 100 85 M 0 85" dur="10s" fill="freeze"/>
              </rect>
              <rect x="0" y="45" width="25" height="30" fill="#000000">
                <animate  attributeName="x" from="0" to="1900"  dur="3s"  repeatCount="indefinite" />
              </rect>
            </g>
          </svg> -->
		  <svg width="1900px" height="300px" id="svg">
		    <rect v-for="(item,index) in test2.vehicles"  :x="item.flows[0].x" :y="item.flows[0].y" width="20" height="20" fill="#000000">
		    		      <!-- <animate attributeName="x" to="100" begin="0s" dur="2s" fill="freeze"/> -->
		    	<animateMotion  :path="item.carRunPath" dur="1s" fill="freeze"/>
		    </rect>
		  </svg>
        </div>
      </el-card>
     <el-card class="box-card" style="margin-top: 10px">
       <div slot="header" class="clearfix">
         <span>戴升智能</span>
       </div>
       <div class="test_background">
       </div>
       测试数据{{test2}}
     </el-card>

   </el-row>
 </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: "test",
  components: {
  },
  data() {
    return {
      st:{dur:"3s"},
      test:{},
      test2:{},
	  carRunPath:"",
	  fsa:false,
      carNum:[]
    }
  },
  computed: {
    ...mapState({
      payment_websocket: state => state.websocket.carList,
      trajectory_websocket: state => state.websocket.realTimeLaneTrajectory
    })
  },
  created: function () {
		
  },
  watch: {
   payment_websocket( carData ){
      this.test=carData
    },
    trajectory_websocket( realTimeLaneTrajectory ){
		// debugger
		if(this.fsa == false){
			this.test2=realTimeLaneTrajectory
			this.fsa = true
            this.createCar()
			
		}
      
    },
    deep: true,
  },
  mounted() {

  },
  methods: {
	createCar(){
        var flag = 0
		// 车数量
        console.log(this.test2,"test2")
        let firstData = JSON.parse(this.$cache.session.get('test2'));
        console.log(firstData);
        if(firstData != ''){//第二次推送数据时 对比本次推送和上次推送时缓存的数据
            for(let i = 0;i<firstData.vehiclesCount.length;i++){
                let arr = firstData.vehiclesCount[i];
                for(let z = 0;z<this.test2.vehiclesCount.length;z++){
                    let zrr = this.test2.vehiclesCount[z]
                    if(arr.carNum == zrr.carNum){
                        zrr.flows.unshift(arr.flows);
                    }
                }
            }
        }
		var vehiclesCount = this.test2.vehiclesCount
		for(var i=0;i<this.test2.vehicles.length;i++){
            
            var vehicles = this.test2.vehicles
            
            // var items = vehicles.filter(function (e) { return e.travelId == id; })
			// 创建一辆车
			// 车牌号
			this.carNum = this.test2.vehicles[i].carNum
            // console.log(this.carNum,"this.carNum")
			// 方向
			var direction = this.test2.vehicles[i].direction
			// 车辆类型
			var vehicleType = this.test2.vehicles[i].vehicleType
			var path = " "
            
            if(i == 0){
                var arr1 = this.test2.vehicles[0]
                for(let j=0;j<arr1.flows.length;j++){
                    
                    // if(j == 0){
                    //     j = 0
                    //     // console.log(arr1.flows[j])
                    //     var arrFlows = []
                    //     var timer = null
                    //     var oldX = ''
                    //     var oldY = ''
                    //     timer = setInterval(() =>{
                    //         let x = arr1.flows[j].x;
                    //         let y = arr1.flows[j].y;
                    //         let x1 = arr1.flows[j+1].x;
                    //         let y1 = arr1.flows[j+1].y;
                    //         this.test2.vehicles[0].carRunPath = "M "+ x +" "+ y + " L "+ x1 +" " + y1
                    
                           //  arrFlows.push(arr1.flows[j])
                            
                           
                           //  if(j >= 2){
                           //      arrFlows.shift()
                           //  }
                            
                           //  if(j == arr1.flows.length){
                           //      clearInterval(timer)
                           //  }
                           //  j += 1
                           //  var x = arrFlows[0].x
                           //  var y = arrFlows[0].y
                           //  if(j >= 1){
                           //      var newX = arrFlows[1].x
                           //      var newY = arrFlows[1].y
                           //  }
                           //  if(j == 0){
                           //      path = "M " + x + " " + y + " "
                           //  }else{
                           //      path = "M " + x + " " + y + " " + "L " + newX + " " + newY + " "
                            	
                               
                           //  }
                           //  console.log(path)
                           //  this.test2.vehicles[0].carRunPath = path
                           //  console.log(this.test2.vehicles[0])
                    //     },2000)
                    // }
                   
                   
                    
                    
                	var flows = this.test2.vehicles[i].flows[j]
                	// 所在车道
                	var lane = flows.lane
                	// 行车速度 单位m/s
                	var speed = flows.speed
                	// 行车速度 单位km/h
                	var speedKMpH = flows.speedKMpH
                	// x
                	var x = flows.x
                	var y = flows.y
                	if(j == 0){
                		path = path + x + " " + y + " "
                	}else{
                		path = path + "L " + x + " " + y + " "
                	}
                    
                }
                
                console.log(this.test2)
                if(this.test2 != ''){
                    this.$cache.session.set('test2',JSON.stringify(this.test2));
                }
            }else{
                return
            }
			
		}
	}	
  }
}
</script>

<style lang="scss" scoped>
.test_background {
  background-image: url("../../../assets/image/lane/道路.jpg");
  background-size: 100% 100%;
  width: 1900px;
  height: 300px;
  cursor: pointer;
  position: relative;
}
</style>
