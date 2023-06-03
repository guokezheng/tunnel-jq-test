<template>
    <div class="box">
        <div class="row">
            <div class="miniBox">
                <div>
                    <img src="../../../../assets/Example/bigScreen/Group1.png"/>
                </div>
                <div class="textBox">
                    <div>总能耗</div>
                    <div ><span class="num" style="color:#fff">237</span><span class="text">tCO</span><span  style="vertical-align: sub;font-size:9px;">2</span></div>
                </div>
            </div>
            <div class="miniBox">
                <div>
                    <img src="../../../../assets/Example/bigScreen/Group2.png"/>
                </div>
                <div class="textBox">
                    <div>碳排放</div>
                    <div ><span class="num" style="color:#F1CA56">78</span><span>tCO</span><span style="vertical-align: sub;font-size:9px">2</span></div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="miniBox">
                <div>
                    <img src="../../../../assets/Example/bigScreen/Group3.png"/>
                </div>
                <div class="textBox">
                    <div>总电费</div>
                    <div ><span class="num" style="color:#33ADE6">1235</span><span>万元</span></div>
                </div>
            </div>
            <div class="miniBox">
                <div>
                    <img src="../../../../assets/Example/bigScreen/Group1.png"/>
                </div>
                <div class="textBox">
                    <div>储能充放电次数</div>
                    <div ><span class="num" style="color:#33ADE6">1234</span><span>次</span></div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            
        }
    }
}
</script>
<style scoped lang="scss">
    .box{
        height: calc(100% - 30px);
        .row{
            width: 100%;
            height:50%;
            display: flex;
            .miniBox{
                width:50%;
                height:100%;
                display:flex;
                img{
                    width:50px;
                    height:53px;
                    margin-right:10px;
                }
                >div{
                    width:60%;
                    height:100%;
                }
                >div:first-of-type{
                    width:40%;
                    color: #9ba0bc;
                    display: flex;
                    justify-content: right;
                    align-items: center;

                }
                .textBox{
                    
                    line-height: 25px;
                    >div:first-of-type{
                        padding-top: 20px;
                        font-size: 12px;
                        color: #9ba0bc;
                    }
                    .num{
                        font-size: 22px;
                        padding-right: 10px;
                    }
                    span{
                        font-size: 12px;
                        color: #9ba0bc;
                    }
                    .text{
                        font-family: revert;
                    }
                }
            }
        }
    }
</style>