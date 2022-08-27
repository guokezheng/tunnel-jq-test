<template>
    <div class="textContent">
        <div class="textTitle">山东高速集团有限公司</div>
        
        <div class="textLine">
            <div class="textName">建筑物年代：</div>
            <div class="textNum">
                <span>2021</span><span>年</span>
            </div>
        </div>
        
        <div class="textLine">
            <div class="textName">建筑物面积：</div>
            <div class="textNum">
                <span>127000</span><span>平米</span>
            </div>
        </div>
        <div class="textLine">
            <div class="textName">系统已运行：</div>
            <div class="textNum">
                <span>108</span><span>天</span>
            </div>
        </div>
        <div class="textTitle">分项年度总耗能</div>
        <div class="textLine">
            <div class="textName">照明插座用电：</div>
            <div class="textNum">
                <span>69.99</span><span>kwh</span>
            </div>
        </div>
    </div>
</template>

<script>
</script>

<style lang="less" scoped>
    .textContent{
        width: 100%;
        height: 100%;
        // border: solid 3px rgba(255,255,255,0.2);
        color: white;
        .textTitle{
            text-align: center;
            font-size: 1vw;
            padding: 1vw 0;
            font-weight: bold;
        }
        .textLine{
            display: flex;
            font-size: 0.8vw;
            padding-bottom: 0.5vw;
            .textName{
                padding-left: 2.5vw;
                
            }
            .textNum{
                position: absolute;
                left: 50%;
                span:nth-of-type(1){
                    
                    color: #E8D76C; 
                }
                span:nth-of-type(2){
                    padding-left: 0.2vw;
                    color: #8FE3F6; 
                }
            }
            
        }
        
    }
</style>
