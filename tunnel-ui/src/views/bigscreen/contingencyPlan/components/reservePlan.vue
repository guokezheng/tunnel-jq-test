<template>
    <div class="planContentBox">
        <div class="planContent">
            <div class="planRow">
                <div class="textBox">火灾探测器</div>
                <div class="textBox">手动报警</div>
                <div class="textBox">电话报警</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">隧道管理所摄像机验证隧道管理所摄像机验证</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">通知路政、高速交警、养护所及分中心</div>
                <div class="textBox">向所有领导汇报事故具体情况通知现场专业指挥人员</div>
                <div class="textBox">迅速拨打消防、公安报警电话（119、110）报警</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">请求执行火灾预案</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">立即执行预案</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">通风</div>
                <div class="textBox">照明</div>
                <div class="textBox">交通系统</div>
                <div class="textBox">组织灭火</div>
                <div class="textBox">隧道广播通知</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">开启风机</div>
                <div class="textBox">开启照明</div>
                <div class="textBox">关闭隧道</div>
                <div class="textBox">准备好工具等候指挥员布置行动方案和发出指令</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">车道指示器、交通信号灯、可变现速率</div>
                <div class="textBox">发布火灾信息，修改洞外情报版</div>
                <div class="textBox">到达事故上游隧道口</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">隧道口50m~100m摆放标志牌</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">火势控制</div>
                <div class="textBox">火势无法控制</div>
            </div>
            <div style="width: 100%;justify-content: space-around;display: flex;">
                <div style="height: 1.6vw;line-height: 2vw;">
                    <img :src='srcArrow' style="width: 1vw;height: 1vw;"/>
                </div>
                <div style="height: 1.6vw;line-height: 2vw;">
                    <img :src='srcArrow' style="width: 1vw;height: 1vw;"/>
                </div>
            </div>
            <div class="planRow">
                <div class="textBox">火势解除</div>
                <div class="arrow1">
                    <img :src='srcArrow2' />
                </div>
                <div class="textBox">专业消防队灭火</div>
            </div>
            <div class="arrow">
                <img :src='srcArrow' />
            </div>
            <div class="planRow">
                <div class="textBox">警情结束</div>
            </div>
        </div>
    </div>
    
</template>

<script>
    export default{
        data(){
            return{
                srcArrow:require('../../../../assets/Example/箭头1-向下_fill.png'),
                srcArrow2:require('../../../../assets/Example/箭头1-向左_fill.png'),
            }
        },
        mounted(){
            
        },
        methods:{
            
        }
    }
</script>

<style lang="less" scoped>
    .planContentBox{
        width: 100%;
        height: 100%;
        // border: solid 1px red;
        overflow: hidden;
        border-radius: 1vw;
        border: solid 2px rgba(9,189,239,0.8);;
        background-color: rgba(4,15,78,0.6);
        .planContent{
            width: 103%;
            height: 100%;
            
            border-radius: 1vw;
            padding: 1vw;
            box-sizing: border-box;
            overflow: auto;
            font-size: 0.8vw;
            color: white;
            
            .planRow{
                width:  100%;
                // height: 13%;
                // border: solid 1px yellow;
                justify-content: space-around;
                display: flex;
            }
            .textBox{
                // width: 10vw;
                // height: 100%;
                padding: 0.5vw;
                // border: solid 1px white;
                background-color: rgba(9,189,239,0.6);
                border-radius: 0.6vw;
                margin: 0 0.5vw;
                text-align: center;
            }
            .arrow{
                width: 100%;
                text-align: center;
                height: 1.6vw;
                line-height: 2vw;
                img{
                    width: 1vw;
                    height: 1vw;
                }
            }
            .arrow1{
                justify-content: center;
                height: 1.6vw;
                line-height: 2vw;
                img{
                    width: 1vw;
                    height: 1vw;
                }
            }
        }
    }
    
</style>
