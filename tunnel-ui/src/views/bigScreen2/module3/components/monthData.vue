<template>
  <div class="box">
    <div class="row">
      <div class="miniBox">
        <div class="top">
          <span class="yellow">1472</span><span>tCO</span><span style="vertical-align: sub;font-size:9px;">2</span>
        </div>
        <div class="bottom">
          <div class="inBottom">本月碳排放</div>
        </div>
      </div>
      <div class="miniBox">
        <div class="top">
          <span class="yellow">695</span><span>tCO</span><span style="vertical-align: sub;font-size:9px;">2</span>
        </div>
        <div class="bottom">
          <div class="inBottom">本月碳减排</div>
        </div>
      </div>
      <div class="miniBox">
        <div class="top">
          <span class="yellow">247</span><span>MWh</span>
        </div>
        <div class="bottom">
          <div class="inBottom">能耗</div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="miniBox">
        <div class="top">
          <span class="yellow">3345</span><span>万元</span>
        </div>
        <div class="bottom">
          <div class="inBottom">本月电费</div>
        </div>
      </div>
      <div class="miniBox">
        <div class="top">
          <span class="yellow">695</span><span>MWh</span>
        </div>
        <div class="bottom">
          <div class="inBottom">本月光伏发电</div>
        </div>
      </div>
      <div class="miniBox">
        <div class="top">
          <span class="yellow">695</span><span>MWh</span>
        </div>
        <div class="bottom">
          <div class="inBottom">本月储能放电</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

export default {
  data () {
    return {
      
    }
  },
  mounted () {
    
  },
  methods: {
    
  }

}
</script>

<style lang="less" scoped>
.box{
  width:100%;
  height:calc(100% - 30px);
  .row{
    width:100%;
    height:50%;
    display: flex;
    justify-content: space-between;
    .miniBox{
      width:33%;
      height:100%;
      font-family: fantasy;
      text-align: center;
      .top{
        width: 100%;
        height: 50%;
        padding-top:20px;
        color:#9ba0bc;
        font-size:10px;
      }
      .bottom{
        width: 100%;
				height: 40%;
				line-height: 50px;
        overflow:hidden;
        color:#9ba0bc;
        font-size:10px;
        border-bottom: solid 2px white;
        border-image: linear-gradient(to right, rgba(0,0,0,0) 0%,rgba(0,0,0,0) 10%, #113C64,rgba(0,0,0,0) 90%,rgba(0,0,0,0)) 30 30;
        .inBottom{
          width:100%;
          height: 100%;
          border-radius: 50px 50px 0 0;
          background-image: radial-gradient(#113C64 0%,rgba(0,0,0,0) 90%);
          line-height: 40px;
        }
      }
      .yellow{
        color:#F1CA56;
        font-size:20px;
        // font-weight:bold;
        padding-right:4px;
      }
    }
  }
}
</style>
