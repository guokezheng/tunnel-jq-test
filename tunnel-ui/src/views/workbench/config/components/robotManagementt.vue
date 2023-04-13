<template>
  <div class="con">
    <!-- 头部 -->
    <div class="title">
      巡检机器人-K478+700
      <img src="./robot/topr.png" alt="" class="topr">
      <span class="close">×</span>
    </div>

    <!-- 状态信息 -->
    <div class="info">
      <ul class="infoUlOne">
        <li>当前状态：<span class="greenFont">在线</span></li>
        <li>当前电压：<span>48V</span></li>
        <li>当前电量：<span>88%</span></li>
        <li>02浓度：<span>22%</span></li>
        <li>投光灯亮度：<span>500cd/m2</span></li>
      </ul>

      <ul class="infoUlOne">
        <li>充电状态：<span class="greenFont">正在充电</span></li>
        <li>总里程：<span>8KM</span></li>
        <li>电池温度：<span>30℃</span></li>
        <li>CO浓度：<span>100PPM</span></li>
      </ul>

      <ul class="infoUlOne">
        <li>运行时长：<span>8天</span></li>
        <li>当前电流：<span>0.8A</span></li>
        <li>当前温度：<span>16℃</span></li>
        <li>当前湿度：<span>20%RH</span></li>
      </ul>
    </div>

    <div class="hr"></div>

    <div class="bottomCon">
      <div class="bLeft">
        <img src="./robot/img.png" alt="" class="robotImg">
        <div class="radioBox">
          <label><input type="radio" name="radio" checked="checked">云台</label>
          <label><input type="radio" name="radio">相机</label>
          <label><input type="radio" name="radio">移动</label>
        </div>
        <div class="yuntai">
          <img src="./robot/jian.png" alt="" class="btnImg jian">
          <div class="yuntaiCon">
            <div class="ytop">
              <div class="stop"></div>
            </div>
            <div class="ybottom">
              <div class="sbottom"></div>
            </div>
            <div class="yleft">
              <div class="sleft"></div>
            </div>
            <div class="yright">
              <div class="sleft"></div>
            </div>
          </div>
          <img src="./robot/jia.png" alt="" class="btnImg jia">
        </div>
        <button class="xiafa">下 发</button>
      </div>

      <div class="bRight">
        <div class="rtop">
          <progress min="0" max="100" value="33" class="progress"></progress>
          <span>50cd</span>
          <!-- <meter min="0" max="100" value="50" class="meter"></meter> -->
        </div>
        <img src="./robot/jqr.png" alt="" class="jqr">

        <div class="btnCon">
          <ul class="btnConUl">
            <li><label><input type="radio" name="radioa" checked="checked">充电桩A</label></li>
            <li><label><input type="radio" name="radioa">充电桩B</label></li>
            <li><button> <img src="./robot/chong.png" alt=""> 一键充电</button></li>
          </ul>

          <ul class="btnConUl">
            <li><label><input type="radio" name="radiob" checked="checked">预置点A</label></li>
            <li><label><input type="radio" name="radiob">预置点B</label></li>
            <li><button> <img src="./robot/hang.png" alt=""> 一键导航</button></li>
          </ul>
        </div>

        <div class="luyin">&nbsp;&nbsp;&nbsp;录入需播放的语言文字
          <img src="./robot/laba.png" alt="" class="laba">
        </div>

        <div class="switchBox">
          <div class="switchBoxOne">
            <div>
              <!-- 开关按钮 -->
              自动巡航
              <p class="btn-on" onclick="on_off(this)">
                <!-- 文字 -->
                <span class="btn-on-text">开</span>
                <!-- 圆点 -->
                <span class="btn-on-circle"></span>
              </p>
            </div>

            <div>
              语音对讲
              <p class="btn-on" onclick="on_offtwo(this)">
                <span class="btn-on-text">开</span>
                <span class="btn-on-circle"></span>
              </p>
            </div>
          </div>

          <div class="switchBoxTwo">
            <div>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;雨刷
              <p class="btn-on" onclick="on_offthree(this)">
                <span class="btn-on-text">开</span>
                <span class="btn-on-circle"></span>
              </p>
            </div>

            <div>
              &nbsp;&nbsp;&nbsp;投光灯
              <p class="btn-on" onclick="on_offfour(this)">
                <span class="btn-on-text">开</span>
                <span class="btn-on-circle"></span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  <style scoped>
  div{
    font-size: 20px;
  }
    /* html,
    body {
      width: 100%;
      height: 100%;
      margin: 0;
      background-color: #071727;
    } */
    .con {
      width: 100%;
      height: 100%;
      background:#071727;
    }

    .title {
      width: 100%;
      height: 10%;
      font-size: 2rem;
      color: #fff;
      display: flex;
      align-items: center;
      padding-left: 5%;
      background: url("./robot/top.png") no-repeat;
      background-size: 100% 100%;
      position: relative;
    }

    .topr,
    .close {
      position: absolute;
      height: 100%;
      right: 0;
    }

    .close {
      font-size: 3rem;
      height: 50%;
      cursor: pointer;
      right: 5%;
    }

    .info {
      width: 100%;
      height: 30%;
    }

    .infoUlOne {
      width: 28%;
      height: 100%;
      padding-left: 5%;
      float: left;
    }

    .infoUlOne li {
      height: 20%;
      font-size: 1.2rem;
      display: flex;
      align-items: center;
      color: #19a1da;
    }

    .infoUlOne li span {
      color: #fff;
    }

    .greenFont {
      color: #10a870 !important;
    }

    .hr {
      width: 90%;
      margin-left: 5%;
      margin-top: 1%;
      border: none;
      border: 1px dashed #186382;
    }

    .bottomCon {
      width: 100%;
      height: 50%;
      margin-top: 2%;
    }

    .bLeft {
      width: 33%;
      height: 100%;
      border-right: 1px dashed #186382;
      padding-left: 5%;
      float: left;
    }

    .robotImg {
      width: 90%;
      height: 50%;
    }

    .radioBox {
      width: 100%;
      height: 10%;
    }

    .radioBox label {
      width: 30%;
      height: 100%;
      /* display: inline-block; */
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      float: left;
      font-size: 1rem;
      cursor: pointer;
      color: #fff;
    }

    .radioBox label input {
      width: 20%;
    }

    .yuntai {
      width: 90%;
      height: 30%;
      position: relative;
    }

    .btnImg {
      width: 10%;
      top: 30%;
      position: absolute;
    }

    .jia {
      right: 0;
    }

    .yuntaiCon {
      width: 60%;
      height: 100%;
      position: absolute;
      left: 20%;
    }

    .ytop,
    .ybottom,
    .yleft,
    .yright {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .ytop {
      width: 50px;
      height: 50px;
      border-top-right-radius: 50px;
      background-color: #1193dd;
      transform: rotate(-45deg);
      /* margin: 0 45% auto; */
      position: absolute;
      left: 43%;
      top: 0;
    }

    .ybottom {
      width: 50px;
      height: 50px;
      border-top-right-radius: 50px;
      background-color: #1193dd;
      transform: rotate(135deg);
      position: absolute;
      left: 43%;
      bottom: 0;
    }

    .yleft {
      width: 50px;
      height: 50px;
      border-top-right-radius: 50px;
      background-color: #1193dd;
      transform: rotate(-135deg);
      position: absolute;
      top: 30%;
      /* left: 22%; */
      left: 0;
    }

    .yright {
      /* width: 50%;
      height: 50%;
      border-top-right-radius: 100%;
      background-color: orange; */
      width: 50px;
      height: 50px;
      border-top-right-radius: 50px;
      background-color: #1193dd;
      /* margin-top: 50px; */
      transform: rotate(45deg);
      position: absolute;
      top: 30%;
      right: 0;
      /* float: right;
      margin: 15% auto; */
    }

    .stop,
    .sbottom,
    .sleft,
    .srigth {
      width: 0;
      height: 0;
      border: 10px solid transparent;
      border-bottom: 10px solid #fff;
      border-left-width: 5px;
      border-right-width: 5px;
      transform: rotate(45deg);
    }

    .xiafa {
      width: 40%;
      height: 8%;
      border: none;
      border-radius: 30px;
      color: #fff;
      background-color: #f1b242;
      margin-left: 30%;
    }

    .bRight {
      width: 60%;
      height: 100%;
      float: right;
    }

    .rtop {
      width: 85%;
      height: 10%;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #fff;
    }

    .progress {
      width: 70%;
      margin-left: 10%;
      margin-right: 5%;
    }

    .jqr {
      margin-top: -3%;
      margin-left: 32%;
    }

    .btnCon {
      width: 60%;
      height: 30%;
      margin-left: 15%;
    }

    .btnConUl {
      width: 50%;
      height: 100%;
      float: left;
      padding: 0;
    }

    .btnConUl li {
      height: 33%;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .btnConUl li label {
      width: 50%;
      color: #fff;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .btnConUl li label input {
      width: 20%;
    }

    .btnConUl li button {
      width: 50%;
      height: 60%;
      color: #fff;
      background-color: #1193dd;
      border-radius: 5px;
      border: none;
      margin-left: 5%;
    }

    .luyin {
      width: 60%;
      height: 10%;
      color: #647481;
      background-color: #062337;
      border-radius: 10px;
      display: flex;
      align-items: center;
      margin-left: 15%;
      margin-top: 3%;
    }

    .laba {
      position: absolute;
      right: 17%;
    }

    .switchBox {
      width: 60%;
      height: 32%;
      margin-top: 3%;
      margin-left: 15%;
    }

    /* 设置整体开关大小 由于label是行内元素需要将其转换为块元素 */
    /* .switchBox label {
      width: 200px;
      height: 100px;
      border-radius: 50px;
      background-color: rgb(66, 63, 63);
      display: block;
    }

    .switchBox input:checked+label {
      background-color: rgb(49, 231, 33);
    } */

    /* 将input进行隐藏 */
    /* .switchBox input {
      display: none;
    } */

    /* 开关按钮设置 */
    /* .switchBox label div {
      width: 100px;
      height: 100px;
      border-radius: 50px;
      background-color: rgb(197, 188, 188);
    } */

    /* 点击后按钮左外间距变为100px */
    /* .switchBox input:checked+label div {
      margin-left: 100px;
    } */
    /* 开关按钮 */
    .btn-on {
      width: 60px;
      height: 25px;
      margin: 0 3px;
      border-radius: 25px;
      font-size: 14px;
    }

    .btn-on {
      cursor: pointer;
      position: relative;
      border: 1px solid white;
      background-color: #19a1da;
    }

    .btn-on-circle {
      position: absolute;
      width: 15px;
      height: 15px;
      top: 5px;
      left: 5px;
      background-color: rgb(255, 255, 255);
      border-radius: 50%;
      box-shadow: 0 0 10px white;
      transition: all .5s;
    }

    .btn-on-text {
      position: absolute;
      right: 10px;
      line-height: 25px;
      color: white;
      transition: all .5s;
    }

    .switchBoxOne,
    .switchBoxTwo {
      width: 100%;
      height: 50%;
      /* display: flex;
      justify-content: center;
      align-items: center; */
    }

    .switchBoxOne div,
    .switchBoxTwo div {
      width: 50%;
      height: 100%;
      float: left;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #fff;
      font-size: 1rem;
    }

    .switchBoxOne div p,
    .switchBoxTwo div p {
      margin-left: 10%;
    }
  </style>

<script>
  // 自动巡航开关
  var type = false;
  function on_off (e) {
    var btn = e;
    var circle = e.children[1];
    var text = e.children[0];

    if (!type) {
      btn.style = "background-color: #ccc;"
      circle.style = "left: 40px;background-color: #888;box-shadow: 0 0 10px #888;";
      text.style = "right: 30px;color: #888;";
      text.innerText = "关";
      type = true;
    } else {
      btn.style = ""
      circle.style = "";
      text.style = "";
      text.innerText = "开";
      type = false;
    }
  }

  // 语音对讲开关
  var typetwo = false;
  function on_offtwo (e) {
    var btn = e;
    var circle = e.children[1];
    var text = e.children[0];

    if (!typetwo) {
      btn.style = "background-color: #ccc;"
      circle.style = "left: 40px;background-color: #888;box-shadow: 0 0 10px #888;";
      text.style = "right: 30px;color: #888;";
      text.innerText = "关";
      typetwo = true;
    } else {
      btn.style = ""
      circle.style = "";
      text.style = "";
      text.innerText = "开";
      typetwo = false;
    }
  }

  // 雨刷开关
  var typethree = false;
  function on_offthree (e) {
    var btn = e;
    var circle = e.children[1];
    var text = e.children[0];

    if (!typethree) {
      btn.style = "background-color: #ccc;"
      circle.style = "left: 40px;background-color: #888;box-shadow: 0 0 10px #888;";
      text.style = "right: 30px;color: #888;";
      text.innerText = "关";
      typethree = true;
    } else {
      btn.style = ""
      circle.style = "";
      text.style = "";
      text.innerText = "开";
      typethree = false;
    }
  }

  // 投光灯开关
  var typefour = false;
  function on_offfour (e) {
    var btn = e;
    var circle = e.children[1];
    var text = e.children[0];

    if (!typefour) {
      btn.style = "background-color: #ccc;"
      circle.style = "left: 40px;background-color: #888;box-shadow: 0 0 10px #888;";
      text.style = "right: 30px;color: #888;";
      text.innerText = "关";
      typefour = true;
    } else {
      btn.style = ""
      circle.style = "";
      text.style = "";
      text.innerText = "开";
      typefour = false;
    }
  }
</script>

</html>