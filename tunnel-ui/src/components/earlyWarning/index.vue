<template>
  <div class="workbenchBox" >
     <!-- 车指 -->
     <div class="vehicleLaneBox" >
       <div class="vehicleLaneMiniBox " v-for="item in 3">
          <img :src="pic.vehicleLane.click == true?pic.vehicleLane.src:pic.vehicleLane.src1"
                  class="vehicleLane"  v-for="item in 3"/>
       </div>
     </div>
     <div class="vehicleLaneBox2">
       <div class="vehicleLaneMiniBox " v-for="item in 3">
          <img :src="pic.vehicleLane.click == true?pic.vehicleLane.src:pic.vehicleLane.src1"
                  class="vehicleLane"  v-for="item in 3"/>
       </div>
     </div>

     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan1"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan2"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan3"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan4"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan5"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan6"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan7"/>
     <img :src="pic.fan.click == true?pic.fan.src1:pic.fan.src" class="fan fan8"/>
     <img :src="pic.lighting.click == true?pic.lighting.src:pic.lighting.src1" class="lighting lighting1"/>
     <img :src="pic.lighting.click == true?pic.lighting.src:pic.lighting.src1" class="lighting lighting3"/>
     <img :src="pic.lighting.click == true?pic.lighting.src:pic.lighting.src1" class="lighting lighting2"/>
     <img :src="pic.lighting.click == true?pic.lighting.src:pic.lighting.src1" class="lighting lighting4"/>
     <img :src="pic.lighting3.click == true?pic.lighting3.src:pic.lighting3.src1" class="lightingMini lighting5"/>
     <img :src="pic.lighting3.click == true?pic.lighting3.src:pic.lighting3.src1" class="lightingMini lighting6"/>
     <img :src="pic.lighting3.click == true?pic.lighting3.src:pic.lighting3.src1" class="lightingMini lighting7"/>
     <img :src="pic.lighting3.click == true?pic.lighting3.src:pic.lighting3.src1" class="lightingMini lighting8"/>
     <img :src="pic.phone.click == true?pic.phone.src1:pic.phone.src" class="phone phone1"/>
     <img :src="pic.phone.click == true?pic.phone.src1:pic.phone.src" class="phone phone2"/>
     <img :src="pic.traffLights.click == true?pic.traffLights.src1:pic.traffLights.src" class="traffLights traffLights1"/>
     <img :src="pic.door.click == true?pic.door.src:pic.door.src1" class="door door1"/>
     <img :src="pic.door.click == true?pic.door.src:pic.door.src1" class="door door2"/>
     <img :src="pic.door.click == true?pic.door.src:pic.door.src1" class="door door3"/>
     <img :src="pic.door.click == true?pic.door.src:pic.door.src1" class="door door4"/>
     <div class="youdaoBox " >
        <img :src="pic.youdao.click == true?pic.youdao.src:pic.youdao.src1"
                class="youdao"  v-for="item in 20"/>
     </div>
     <div class="youdaoBox2 " >
        <img :src="pic.youdao2.click == true?pic.youdao2.src:pic.youdao2.src1"
                class="youdao"  v-for="item in 20"/>
     </div>
     <img :src="pic.shusan.click == true?pic.shusan.src1:pic.shusan.src" class="shusan shusan1"/>
     <img :src="pic.shusan.click == true?pic.shusan.src1:pic.shusan.src" class="shusan shusan2"/>
     <img :src="pic.board.click == true?pic.board.src:pic.board.src1" class="board board1"/>
  </div>
</template>

<script>
  export default{
    name:'earlyWarning',
    props:["earlyWarning"],
    data(){
      return{
        list:[
          {id:1},
          {id:1},
          {id:1},
          {id:1},
        ],
        pic:{
          fan:{
            src:require('@/assets/logo/equipment_log/风机-反转.gif'),
            src1:require('@/assets/logo/equipment_log/风机-停止.png'),
            click:true,
          },
          lighting:{
            src:require('@/assets/logo/equipment_log/加强照明-离线.png'),
            src1:require('@/assets/logo/equipment_log/加强照明-在线.png'),
            click:true,
          },
          lighting3:{
            src:require('@/assets/logo/equipment_log/基本照明-离线.png'),
            src1:require('@/assets/logo/equipment_log/基本照明-开启.png'),
            click:true,
          },
          phone:{
            src:require('@/assets/logo/equipment_log/紧急电话-在线.png'),
            src1:require('@/assets/logo/equipment_log/紧急电话-离线.png'),
            click:true,
          },
          traffLights:{
            src:require('@/assets/logo/equipment_log/交通信号灯-红.png'),
            src1:require('@/assets/logo/equipment_log/交通信号灯-黄.png'),
            click:true,
          },
          door:{
            src:require('@/assets/logo/equipment_log/卷帘门-全开.png'),
            src1:require('@/assets/logo/equipment_log/卷帘门-全关.png'),
            click:true,
          },
          youdao:{
            // src:require('@/assets/logo/equipment_log/light_yindao_buliang.png'),
            // src1:require('@/assets/logo/equipment_log/light_yindao_liang.png'),
            click:true,
          },
          shusan:{
            src:require('@/assets/logo/equipment_log/疏散标志-在线.png'),
            src1:require('@/assets/logo/equipment_log/疏散标志-离线.png'),
            click:true,
          },
          board:{
            // src:require('@/assets/logo/equipment_log/qingbaoban_kong.png'),
            // src1:require('@/assets/logo/equipment_log/qingbaoban.png'),
            click:true,
          }
        },
      }
    },
    methods:{
      changeEarlyWarn(type){
        switch (type) {
        	case 1:
          if(this.pic.vehicleLane.click == true){
            this.pic.vehicleLane.click = false
            this.pic.vehicleLane2.click = false
          }else{
            this.pic.vehicleLane.click = true
            this.pic.vehicleLane2.click = true
          }
        		break;
        	case 2:
        		if(this.pic.fan.click == true){
        		  this.pic.fan.click = false
        		}else{
        		  this.pic.fan.click = true
        		}
        		break;
          case 3:
          if(this.pic.lighting.click == true){
            this.pic.lighting.click = false
            this.pic.lighting3.click = false
          }else{
            this.pic.lighting.click = true
            this.pic.lighting3.click = true
          }
          	break;
          case 4:
            if(this.pic.shusan.click == true){
              this.pic.shusan.click = false
            }else{
              this.pic.shusan.click = true
            }
            break;
          case 5:
            if(this.pic.youdao.click == true){
              this.pic.youdao.click = false
              this.pic.youdao2.click = false
            }else{
              this.pic.youdao.click = true
              this.pic.youdao2.click = true
            }
            break;
          case 6:
            if(this.pic.phone.click == true){
              this.pic.phone.click = false
            }else{
              this.pic.phone.click = true
            }
            break;
          case 7:
            if(this.pic.board.click == true){
              this.pic.board.click = false
            }else{
              this.pic.board.click = true
            }
            break;
          case 8:
            if(this.pic.traffLights.click == true){
              this.pic.traffLights.click = false
            }else{
              this.pic.traffLights.click = true
            }
            break;
          case 9:
            if(this.pic.door.click == true){
              this.pic.door.click = false
            }else{
              this.pic.door.click = true
            }
            break;
        }
      },
    }
  }
</script>

<style scoped lang="scss">
  .workbenchBox{
    width: 95%;
    height: 100%;
    position: relative;
    top: 0;
    border:1px solid #4196C6;
    border-radius: 0.5rem;
    background-image: url(../../assets/image/lane/3zhong.jpg);
    background-position: center;
    background-repeat: no-repeat;
    background-size: 100% 54%;
    .fan,.traffLights{
      width: 20px;
      height: 20px;
      position: absolute;
    }
    .youdao{
      width: 10px;
      height: 10px;
      // position: absolute;
    }
    .youdaoBox{
      width: 568px;
      height: 50px;
      display: flex;
      justify-content: space-around;
      position: relative;
      top: -40px;
      left: 70px;
    }
    .youdaoBox2{
      width: 568px;
      height: 50px;
      display: flex;
      justify-content: space-around;
      position: relative;
      top: 138px;
      left: 70px;
    }
    .vehicleLane{
      width: 30px;
      height: 15px;
      // position: absolute;
    }
    .lighting{
      width: 56px;
      height: 8px;
      position: absolute;
    }
    .lightingMini{
      width: 30px;
      height: 8px;
      position: absolute;
    }
    .phone{
      width: 15px;
      height: 15px;
      position: absolute;
    }
    .door{
      width: 20px;
      height: 13px;
      position: absolute;
    }
    .shusan{
      width: 30px;
      height: 10px;
      position: absolute;
    }
    .board{
      width: 20px;
      height: 65px;
      position: absolute;
    }
    .board1{
      top: 184px;
      left: 590px;
    }
    .vehicleLaneBox{
      width: 588px;
      height: 100px;
      display: flex;
      justify-content: space-around;
      position: relative;
      top: 164px;
      left: 120px;
    }
    .vehicleLaneBox2{
      width: 588px;
      height: 100px;
      display: flex;
      justify-content: space-around;
      position: relative;
      top: 198px;
      left: 120px;
    }
    .vehicleLaneMiniBox{
      width: 145px;
      height: 100px;
      position: relative;
      display: flex;
      flex-direction:column;
      justify-content: space-around;
    }
    .vehicleLane1{
      top: 164px;
      left:153px;
    }
    .vehicleLane2{
      top: 64px;
      left:342px;
    }
    .vehicleLane3{
      top: -36px;
      left:530px;
    }
    .vehicleLane4{
      top: -4px;
      left:153px;
    }
    .fan1{
      top: 175px;
      left: 220px;
    }
    .fan2{
      top: 225px;
      left: 220px;
    }
    .fan3{
      top: 175px;
      left: 470px;
    }
    .fan4{
      top: 225px;
      left: 470px;
    }
    .fan5{
      top: 310px;
      left: 220px;
    }
    .fan6{
      top: 360px;
      left: 220px;
    }
    .fan7{
      top: 310px;
      left: 470px;
    }
    .fan8{
      top: 360px;
      left: 470px;
    }
    .lighting1{
      top: 130px;
      left: 120px;
    }
    .lighting2{
      top: 415px;
      left: 120px;
    }
    .lighting3{
      top: 130px;
      left: 520px;
    }
    .lighting4{
      top: 415px;
      left: 520px;
    }
    .lighting5{
      top: 130px;
      left: 260px;
    }
    .lighting6{
      top: 130px;
      left: 400px;
    }
    .lighting7{
      top: 415px;
      left: 260px;
    }
    .lighting8{
      top: 415px;
      left: 400px;
    }
    .phone1{
      top: 160px;
      left: 640px;
    }
    .phone2{
      top: 380px;
      left: 640px;
    }
    .traffLights1{
      top: 200px;
      left: 640px;
    }
    .door1{
      top: 260px;
      left: 250px;
    }
    .door2{
      top: 260px;
      left: 400px;
    }
    .door3{
      top: 280px;
      left: 250px;
    }
    .door4{
      top: 280px;
      left: 400px;
    }

    .shusan1{
      top: 252px;
      left: 300px;
    }
    .shusan2{
      top: 296px;
      left: 300px;
    }
  }
</style>
