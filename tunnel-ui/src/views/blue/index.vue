<template>
    <div class="blue-container">
        <el-row :gutter="30">
            <el-col :span="6">
                <div class="left-content publicStyle">
                    <div class="firstModule publicTitle">
                        <p>统计模块一</p>
                        <span>主题详情 <i class="el-icon-arrow-right"></i></span>
                    </div>
                    <div class="publicDes">
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <ul class="list">
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <div class="clear"></div>
                                </ul>
                            </div>
                        </div>
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <category style="height: 200px;width:100%;"></category>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="left-content publicStyle margin_15">
                    <div class="firstModule publicTitle">
                        <p>统计模块二</p>
                        <span>主题详情 <i class="el-icon-arrow-right"></i></span>
                    </div>
                    <div class="publicDes">
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <ring style="height: 200px;width:100%;"></ring>
                            </div>
                        </div>
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <piev3 style="height: 200px;width:100%;"></piev3>
                            </div>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="12">
                <amap style="height: 560px;width: 100%"/>
                <ul class="centerList">
                    <li>
                        <p>1432(t)</p>
                        <span>城市供电</span>
                    </li>
                    <li>
                        <p>1432(t)</p>
                        <span>城市供电</span>
                    </li>
                                        <li>
                        <p>1432(t)</p>
                        <span>城市供电</span>
                    </li>
                                        <li>
                        <p>1432(t)</p>
                        <span>城市供电</span>
                    </li>
                     <li>
                        <p>1432(t)</p>
                        <span>城市供电</span>
                    </li>
                </ul>
            </el-col>
            <el-col :span="6">
                <div class="left-content publicStyle">
                    <div class="firstModule publicTitle">
                        <p>统计模块三</p>
                        <span>主题详情 <i class="el-icon-arrow-right"></i></span>
                    </div>
                    <div class="publicDes">
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <ul class="list">
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <div class="clear"></div>
                                </ul>
                            </div>
                        </div>
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <ul class="list">
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <div class="clear"></div>
                                </ul>
                                </div>
                        </div>
                    </div>
                </div>
                <div class="left-content publicStyle margin_15">
                    <div class="firstModule publicTitle">
                        <p>统计模块四</p>
                        <span>主题详情 <i class="el-icon-arrow-right"></i></span>
                    </div>
                    <div class="publicDes">
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <histogram style="height: 200px;width: 100%" />
                            </div> 
                        </div>
                        <div class="publicBox">
                            <p class="moudleTitle">自定义图标</p>
                            <div class="moudleBox">
                                <ul class="list">
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <li>
                                        <p>143</p>
                                        <span>数据</span>
                                    </li>
                                    <div class="clear"></div>
                                </ul>
                            </div> 
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import amap from '@/components/Gis/amap'
import category from '@/views/v3/category'
import piev3 from '@/views/v3/piev3'
import ring from '@/views/v3/ring'
import histogram from '@/views/v3/histogram'
export default {
    name: 'blue',
    components: {
    amap, // 高德地图
    category, // 折线
    piev3, //圆
    ring, // 圆环
    histogram,// 柱状图
  },
}
</script>
<style lang="scss" scoped>
    @import "~@/assets/styles/variables.scss";
    // 公共样式
    .publicStyle{
        .publicTitle{display:flex;
            padding:10px 0;
            box-sizing:border-box;
            background:linear-gradient(to right,#166a99,#075482);
            border-left: 7px solid #eab923;
            justify-content: space-between;
            color:white;
            font-size:16px;
            p{
                text-indent: 15px;
            }
            span{
                display:block;
                padding-right:10px;
                font-size:12px;
                i{
                    font-size:12px;
                }
            }
        }
        .publicDes{
            margin: 10px 0;
            .publicBox{
                width:48%;
                display: inline-block;
                margin-right:4%;
                .moudleTitle{
                    width:100%;
                    background:linear-gradient(to right,#166a99,#024d7a);
                    padding:10px 0;
                    color:white;
                    text-align:center;
                }
                .moudleBox{
                    width:100%;
                    background-color:#075380;
                    margin: 10px 0;
                    .list{
                        padding:10px;
                        box-sizing:border-box;
                        li{
                            background-color:#0e5b88;
                            list-style: none;
                            width: 48%;
                            float: left;
                            margin-right:4%;
                            padding:22.5px 20px;
                            box-sizing:border-box;
                            p{color:#f4cd54;font-weight:bold;font-size:21px;text-align: center;}
                            span{color:white;text-align: center;display: block;font-size:12px;}
                            margin-bottom: 7px;
                        }
                        li:nth-child(2n){margin-right:0px;}
                        li:nth-child(3){margin-bottom:0px;}
                        li:nth-child(4){margin-bottom:0px;}
                    }
                }
            }
            .publicBox:nth-child(2){margin-right:0px;}
        }
        .clear{clear: both;}   
    }
    .blue-container{
        background-color:$theme-blue-bg-color;
        padding: 85px 25px;
        ul{padding-left:0px;margin:0px;}
        .margin_15{margin-top: 15%;}
        .centerList{width:100%;margin:4.5% 0;
            li{list-style: none;width:18%;margin-right:2.5%;display: inline-block;padding:15px 0;box-sizing: border-box;
                background: linear-gradient(to bottom, #1b72a3,#065585,#1a71a2);
                p{color:#f4cd54;font-size:21px;text-align: center;font-weight:bold;}
                span{color:white;font-size:12px;text-align: center;display:block;margin-top: 5px;}
            }
            li:nth-child(5){margin-right:0px;}
        }
    }
    #app{background-color:$theme-blue-bg-color;}
    .bell .el-badge .el-badge__content{top:12px;}
</style>
