<template>
  <div class="app-container">
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="box-card">
          <div class="headerTitle" slot="header"> 
            <div class="headerTriangle"></div>
            <div class="headerTitleCont">
              <div class="headerTitleCont2">情报快发</div></div>
          </div> 
          <el-row class="content_tunnel">
            <el-col :span="3" style="visibility:hidden">1</el-col>
            <el-col :span="5">
              <el-button size="mini" class="text_tunnel" style="background-color:#0C3783;">
                龙门隧道
              </el-button>
            </el-col>
            <el-col :span="5">
              <el-button size="mini" class="text_tunnel" style="background-color:#227764;">
                洪门隧道
              </el-button>
            </el-col>
            <el-col :span="5">
              <el-button size="mini" class="text_tunnel" style="background-color:#896321;">
                海望石隧道
              </el-button>
            </el-col>
            <el-col :span="5">
              <el-button size="mini" class="text_tunnel" style="background-color:#425283;">
                长山隧道
              </el-button>
            </el-col>
          </el-row>
          <el-row class="content_btn">
            <el-col :span="3">
              <span class="box_up">上行</span>
            </el-col>
            <el-col :span="5" v-for="(item,index) in tunnelList" :key="item.id" >
              <el-button size="mini" v-for="(itm,indx) in item.children" :key="indx" @click="upDirectionClick(item, indx+1,$event)"
              :class='addClassName(item,indx,1)'
                      >{{itm.name}}</el-button>
            </el-col>
          </el-row>
          <el-row class="content_btn">
            <el-col :span="3">
              <span class="box_up">下行</span>
            </el-col>
            <el-col :span="5" v-for="(item,index) in tunnelList" :key="index">
              <el-button size="mini" v-for="(itm,indx) in item.children" :key="indx" @click="downDirectionClick(item, indx+1,$event)"
              :class='addClassName(item,indx,2)'
                      >{{itm.name}}</el-button>
            </el-col>
          </el-row>
          <el-row class="radio-box" type="flex" :gutter="20" style="margin: 0 20px;">
            <el-col :span="6">信号灯：</el-col>
            <el-col :span="18">
              <el-radio-group v-model="signalLamp">
                <el-radio :label="0">
                  <div class="radio-content" style="background:#F1574C;">红</div>
                </el-radio>
                <el-radio :label="1">
                  <div class="radio-content" style="background:#FF9F03;">黄</div>
                </el-radio>
                <el-radio :label="2">
                  <div class="radio-content" style="background:#68C730;">绿</div>
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row class="radio-box" type="flex" :gutter="20" style="margin: 0 20px;">
            <el-col :span="6">车道指示器：</el-col>
            <el-col :span="18">
              <el-radio-group v-model="laneIndicator">
                <el-radio :label="0">
                  <div class="radio-content" >正红反绿</div>
                </el-radio>
                <el-radio :label="1">
                  <div class="radio-content" >正绿反红</div>
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row class="radio-box" type="flex" :gutter="20" style="margin: 0 20px;">
            <el-col :span="6">发布情报板：</el-col>
            <el-col :span="10">
              <el-select v-model="releaseInfoBoard" placeholder="请选择" size="mini" :popper-append-to-body="false" popper-class="selectCls">
                  <el-option
                    v-for="item in infoBoardData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-col>
            <el-col :span="8">
              <el-radio-group v-model="InfoBoardRadio">
                <el-radio :label="0">
                  <div class="radio-content" >是</div>
                </el-radio>
                <el-radio :label="1">
                  <div class="radio-content" >否</div>
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row class="radio-box" type="flex" :gutter="20" style="margin: 0 20px;">
            <el-col :span="6">发布广播内容：</el-col>
            <el-col :span="10">
              <el-select v-model="broadCont" placeholder="请选择" size="mini" :popper-append-to-body="false" popper-class="selectCls">
                  <el-option
                    v-for="item in broadContData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-col>
            <el-col :span="8">
              <el-radio-group v-model="broadContRadio">
                <el-radio :label="0">
                  <div class="radio-content" >是</div>
                </el-radio>
                <el-radio :label="1">
                  <div class="radio-content" >否</div>
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="display: flex;justify-content: center;margin: 40px;">
            <el-button style="background-color: #0C4698;color: white;border: solid 1px #aebaff;">一键下发</el-button>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="box-card send-history">
          <div class="headerTitle" slot="header">
            <div class="headerTriangle"></div>
            <div class="headerTitleCont">
              <div class="headerTitleCont2">快发历史</div></div>
          </div> 
          <template>
              <el-table :data="historyList"  style="width: 100%" class="releaseHistory" 
                        :header-cell-style="{'text-align':'center'}"  
                        :cell-style="{'text-align':'center'}">
                <el-table-column prop="id" label="序号" min-width="10%"></el-table-column>
                <el-table-column prop="content" label="内容" min-width="15%"></el-table-column>
                <el-table-column prop="state" label="状态" min-width="15%"></el-table-column>
                <el-table-column prop="time" label="时间" min-width="30%"></el-table-column>
                <el-table-column label="操作" min-width="10%">
                  <template slot-scope="scope">
                    <span style="color: #006fcf;">恢复</span>
                  </template>
                </el-table-column>
              </el-table>
            </template>

        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import $ from "jquery";
export default {
  data() {
    return {
      // 情报快发
      tunnelList: [
        {id:1,tunnelName:'龙门隧道', upDirection: 1, downDirection: 3,children:[{name:'左侧'},{name:'右侧'},{name:'两侧'}]},
        {id:2,tunnelName:'洪门隧道', upDirection: 2, downDirection: 2,children:[{name:'左侧'},{name:'右侧'},{name:'双侧'}]},
        {id:3,tunnelName:'望海石隧道', upDirection: 3, downDirection: 1,children:[{name:'左侧'},{name:'右侧'},{name:'双侧'}]},
        {id:4,tunnelName:'长山隧道', upDirection: 1, downDirection: 0,children:[{name:'左侧'},{name:'右侧'},{name:'双侧'}]},
      ],
      signalLamp: 0,//信号灯单选
      laneIndicator:0,//车指器单选
      releaseInfoBoard:'value1',//发布情报板绑定
      InfoBoardRadio:0,//发布情报板单选
      //发布情报板下拉框
      infoBoardData:[{
        value:'value1',
        label:'***************'
      },{
        value:'value2',
        label:'***************'
      }],
      broadCont:'value1',//发布广播内容绑定
      broadContRadio:1,
      //发布广播内容下拉框
      broadContData:[{
        value:'value1',
        label:'***************'
      },{
        value:'value2',
        label:'***************'
      }],
      // 快发历史
      historyList: [{
        id: '01',
        state: '已下发',
        time: '2016-05-02 17:16:23',
        content: '快发1'
      },{
        id: '02',
        state: '已下发',
        time: '2016-05-02 17:16:23',
        content: '快发2'
      },{
        id: '03',
        state: '已下发',
        time: '2016-05-02 17:16:23',
        content: '快发3'
      },{
        id: '04',
        state: '已下发',
        time: '2016-05-02 17:16:23',
        content: '快发4'
      },{
        id: '05',
        state: '已下发',
        time: '2016-05-02 17:16:23',
        content: '快发5'
      }],
    }
  },
  created() {},
  mounted() {},
  methods: {
    addClassName(item,index,num){
      if(num == 1){
        if(item.upDirection == index+1){
          return 'activeInfoBoard'+ item.id
        }
      }else{
        if(item.downDirection == index+1){
          return 'activeInfoBoard'+ item.id
        }
      }
    },
    upDirectionClick(items,value,e) {
      console.log(items,'items')
      var res = ''
      if(e.target.tagName == 'SPAN'){
        res = e.currentTarget
      }else{
        res = e.target
      }
      var resBrather = e.currentTarget.parentElement.childNodes
      if(items.upDirection == value) {
        items.upDirection = 0
         res.style = 'null'
        return
      }else{
        console.log(resBrather,"resBrather")
        if(items.id == 1){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard1')==true){
              item.classList.remove("activeInfoBoard1")
            }
          })
          res.classList += ' activeInfoBoard1'
        }else if(items.id == 2){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard2')==true){
              item.classList.remove("activeInfoBoard2")
            }
          })
          res.classList += ' activeInfoBoard2'
        }else if(items.id == 3){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard3')==true){
              item.classList.remove("activeInfoBoard3")
            }
          })
          res.classList += ' activeInfoBoard3'
        }else{
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard4')==true){
              item.classList.remove("activeInfoBoard4")
            }
          })
          res.classList += ' activeInfoBoard4'
        }
      }
      items.upDirection = value
    },
    downDirectionClick(items, value,e) {
      var res = ''
      if(e.target.tagName == 'SPAN'){
        res = e.currentTarget
      }else{
        res = e.target
      }
      var resBrather = e.currentTarget.parentElement.childNodes
      if(items.downDirection == value) {
        items.downDirection = 0
         res.style = 'null'
        return
      }else{
        console.log(resBrather,"resBrather")
        if(items.id == 1){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard1')==true){
              item.classList.remove("activeInfoBoard1")
            }
          })
          res.classList += ' activeInfoBoard1'
        }else if(items.id == 2){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard2')==true){
              item.classList.remove("activeInfoBoard2")
            }
          })
          res.classList += ' activeInfoBoard2'
        }else if(items.id == 3){
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard3')==true){
              item.classList.remove("activeInfoBoard3")
            }
          })
          res.classList += ' activeInfoBoard3'
        }else{
          resBrather.forEach(item=>{
            if(item.classList.contains('activeInfoBoard4')==true){
              item.classList.remove("activeInfoBoard4")
            }
          })
          res.classList += ' activeInfoBoard4'
        }
      }
      items.downDirection = value
    },
  },
}
</script>

<style lang="less" scoped>
.app-container {
  background: url(~@/assets/image/workbench/background.jpg) fixed no-repeat;
  background-position: center;
  background-size: cover;
  -webkit-background-size: cover;
  height: 100%;
  ::v-deep .box-card {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    border: 1px solid rgb(20,43,76);
    background: rgba(8,23,54,1);
    // rbga兼容IE8
    filter: progid:DXImageTransform.Microsoft.gradient(startcolorstr=#7F000000,endcolorstr=#7F000000);
    
    .el-card__header {
      border-bottom: none;
    }
    .headerTitle{
        padding: 5px 20px;
        line-height: 24px;
        font-size: 14px;
        display: flex;
        align-items: center;
        .headerTriangle{
          width: 0;
          height: 0;
          border-top: 5px solid transparent;
          border-left: 10px solid #1590ff;
          border-bottom: 5px solid transparent;
        }
        .headerTitleCont{
          width: 100%;
          height: 30px;
          background-image: linear-gradient(to right, rgba(21,35,57,0.8) ,rgba(47,80,111,0.8)20%, rgba(21,35,57,0.8));
          display: flex;
          align-items: center;
        }
        .headerTitleCont2{
          width: 100%;
          height: 22px;
          background-image: linear-gradient(to right, rgba(21,35,57,1) , rgba(47,80,111,1)20%, rgba(21,35,57,1));
          padding-left: 10px;
        }
    }
    .el-card__body {
      border: 1px solid rgb(33, 59, 104);
      margin: 10px 15px 15px 15px;
    }

    .content_tunnel {
      margin-bottom: 20px;

      .el-col {
        display: flex;
        justify-content: center;
        align-items: center;
      }

      .text_tunnel {
        color:#fff;
        border: none;
        cursor: default;  
      }
    }
    .content_btn {
      height: 120px;
      text-align: center;
      margin-bottom: 30px;

      .el-col {
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-evenly;
        border-right: 1px solid #2F5373;
      }
      .el-col:last-child {
        border-right: 0;
      }
      .el-col:first-child {
        border-right: 0;
      }

      .el-button + .el-button {
        margin-left: 0;
      }

      .box_up {
        height: 100px;
        line-height: 30px;
        width: fit-content;
        background-color: #0F1F41;
        writing-mode: tb-rl;
        -webkit-writing-mode: tb-rl;
        -ms-writing-mode: tb-rl;
        letter-spacing: 7px;
      }

      .el-button {
        color: #fff;
        background: transparent;
        border: 1px solid #2F5373;
      }
      .active {
        border: 1px solid #304A8D;
        background: #232834;
      }
    }

    .radio-box {
      display: flex;
      align-items: center;
      border-bottom: 1px solid #1d3056;
      padding: 10px 0;
      
      
      .radio-content {
        display: inline-block;
        width: 30px;
        height: 20px;
        line-height: 20px;
        font-size: 12px;
        color: #fff;
        border-radius: 5px;
        text-align: center;
      }
    }
  }
  .box-card .content_btn .el-button {
      color: #fff;
      background: transparent;
      border: 1px solid transparent;
  }
  .activeInfoBoard1{
    background-image: url(../../../assets/image/boardButon1.png) !important;
    background-size: 100% 100% !important;
  }
  .activeInfoBoard2{
    background-image: url(../../../assets/image/boardButon2.png) !important;
    background-size: 100% 100% !important;
  }
  .activeInfoBoard3{
    background-image: url(../../../assets/image/boardButon3.png) !important;
    background-size: 100% 100% !important;
  }
  .activeInfoBoard4{
    background-image: url(../../../assets/image/boardButon4.png) !important;
    background-size: 100% 100% !important;
  }
  ::v-deep .selectCls {
    background-color: #081736 !important;
    border: solid 1px #213b68;
  }
  .el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
      background-color: rgba(127, 155, 234, 0.5);
  }
  ::v-deep .el-select-dropdown__item{
    color: #fff;
  }
  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #fff;
    background-color: #0C4698 ;
  }
  ::v-deep .el-radio__inner:hover {
    border-color: #0C4698 ;
  }
  ::v-deep .el-radio__inner{
    background-color: #081736;
  }
  ::v-deep .el-input__inner{
    background-color: #071635;
    border: solid 1px #0e2148;
    color: #dadada;
  }
  ::v-deep .el-icon-arrow-up:before {
    content: "\e78f";
  }
  .releaseHistory{
    background-color: #081736;
    color: white;
    font-size: 12px;
  }
  ::v-deep .el-table{
    .el-table__header-wrapper th,{
      background-color: transparent;
      color: white;
      font-size: 12px;
    }
    tr{
      background-color: transparent;
    }
    th.el-table__cell.is-leaf{
      border-bottom: solid 1px #213b68;
    }
    td.el-table__cell{
      border-bottom: solid 1px #213b68;
    }
  } 
  ::v-deep .el-table tr:hover>td{
    background-color: #0B2345;
  } 
}
</style>