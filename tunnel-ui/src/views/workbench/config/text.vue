<template>
    <div class="app-content">
        <el-dialog
        class="workbench-dialog"
        title="情报板编辑"
        :visible.sync="dialogVisible"
        width="85%"
        style="left:0px;"
        append-to-body>
            <el-row :gutter="40">
                <el-col :span="8" v-for="(item,index) in eqIdList" :key="index" style="padding:20px 40px;">
                    <el-row class="grid-content">
                        <el-col :span="24" class="item-title">{{item.eqName}}  {{item.stakeMark}}分辨率:{{item.resolution}}</el-col>
                        
                        <el-col :span="8" :offset="16" class="iconList" style="margin-top:25px;">
                            <el-row>
                                <!-- 附近摄像机 -->
                                <el-col :span="4">
                                    <el-button type="text" @click="openVideo">
                                        <i class="el-icon el-icon-video-play" @click="video(item.id)"></i>
                                    </el-button>
                                </el-col>
                                <!-- 编辑 -->
                                <el-col :span="5">
                                    <el-button type="text" @click="edit(item.id)">
                                        <i class="el-icon el-icon-edit-outline"></i>
                                    </el-button>
                                </el-col>
                                <!-- 历史版本 -->
                                <el-col :span="5"><i class="el-icon el-icon-time"></i></el-col>
                                <!-- 离线状态 -->
                                <template v-if="item.online == 0">
                                    <el-col :span="5" class="offline"> 
                                        <el-tooltip class="item" effect="dark" content="离线" placement="top">
                                            <el-button><i class="el-icon el-icon-warning-outline" ></i></el-button>
                                        </el-tooltip>
                                    </el-col>
                                </template>
                                <template v-if="item.online == 1">
                                    <el-col :span="5" class="online">    
                                        <el-tooltip class="item" effect="dark" content="在线" placement="top">
                                            <el-button><i class="el-icon el-icon-warning-outline"></i></el-button>
                                        </el-tooltip>
                                    </el-col>
                                </template>
                                <!-- 删除 -->
                                <el-col :span="5"><i class="el-icon el-icon-delete" @click="delect(item.id)"></i></el-col>
                            </el-row>
                        </el-col>
                        <div class="clear"></div>
                    </el-row>
                    <div class="grid-content" style="padding:20px 0px;">
                        <el-col :span="24" style="padding:10px 20px;" class="fromTitle"><span>当前内容:</span></el-col>
                        <el-col :span="24" style="margin-bottom:20px;">
                            <el-input class="oldContent" v-model="item.oldContent"/>
                        </el-col>
                        <el-col :span="24" style="padding:10px 20px;" class="fromTitle"><span>下发内容:</span></el-col>
                        <el-col :span="24" style="margin-bottom:20px;">
                            <el-input class="newContent" v-model="item.content"/>
                        </el-col>
                        <div class="clear"></div>
                    </div>
                </el-col>
            </el-row>
            <div class="grid-content">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-col :span="6">
                        <el-form-item label="内容:">
                            <el-input v-model="form.content"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="字体大小:">
                            <el-select v-model="form.size" placeholder="16">
                                <el-option label="16" value="16"></el-option>
                                <el-option label="15" value="15"></el-option>
                                <el-option label="14" value="14"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="字体类型:">
                            <el-select v-model="form.type" placeholder="楷体">
                                <el-option label="楷体" value="kai"></el-option>
                                <el-option label="颜体" value="yan"></el-option>
                                <el-option label="正楷" value="zhengkai"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="字体颜色:">
                            <el-select v-model="form.color" placeholder="白色">
                                <el-option label="白色" value="white"></el-option>
                                <el-option label="黑色" value="blank"></el-option>
                                <el-option label="红色" value="red"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <div class="clear"></div>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" size="mini" @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="dialogVisible = false">确 定</el-button>
            </span>
            <el-dialog
            title="情报板编辑"
            :append-to-body="true"
            :visible.sync="editVisible">
                <el-form :model="ruleForm" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="情报板名称" prop="title">
                        <el-input v-model="ruleForm.title"></el-input>
                    </el-form-item>
                    <el-form-item label="当前内容" prop="content">
                        <el-input v-model="ruleForm.oldContent"></el-input>
                    </el-form-item>
                    <el-form-item label="下发内容" prop="content">
                        <el-input v-model="ruleForm.content" style="font-size:16px;"></el-input>
                    </el-form-item>
                    <el-form-item label="在线" prop="content">
                        <el-input v-model="ruleForm.online"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="editVisible = false">确 定</el-button>
                </span>
            </el-dialog>
        </el-dialog>
    </div>
</template>
<script>
export default{
    data(){
        return{
            dialogVisible: false,
            eqIdList:[
                {id:'1',title:'S29-LinYiCompany-BaiYanStation-001-CZ-010',content:'',oldContent:'山东高速欢迎你',online:'0',resolution:'1024*128'},
                {id:'2',title:'S29-LinYiCompany-BaiYanStation-001-CZ-009',content:'',oldContent:'山东高速欢迎你',online:'1',resolution:'1024*128'},
                {id:'3',title:'S29-LinYiCompany-BaiYanStation-001-CZ-008',content:'',oldContent:'山东高速欢迎你',online:'0',resolution:'1024*128'}
            ],
            form:{content:'',size:'16',type:'楷体',color:'白色'},
            editVisible:false,
            editItem:{},
            ruleForm: {
                id:'',
                title: '',
                online:'',
                content:'',
            },
        }
    },
    props:[
        'IntelligenceBoardShow',
    ],
    watch:{
        "form.content"(val){
            this.eqIdList.forEach(item=>{
                item.content = this.form.content;
            })
        }
    },
    methods:{
        onSubmit(){
            this.eqIdList.forEach(item=>{
                item.content = this.form.content;
            })
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
            .then(_ => {
                done();
            })
            .catch(_ => {});
        },
        childerfunction(dialogVisible,item){
            let list = [];
            list.push(item);
            this.eqIdList = list
            console.log()
            this.dialogVisible = dialogVisible;
        },
        delect(id){
            this.$confirm('确认删除？')
            .then(_ => {
                this.eqIdList.forEach((item,index)=>{
                    if(item.id == id){
                        this.eqIdList.splice(index,1);
                    }
                })
            })
            .catch(_ => {});
        },
        video(id){
            this.videovideoDialogVisibleShow = !this.videoDialogVisible;
        },
        videoHandleClose(done){
            done();
        },
        openVideo(){
            this.$alert('<iframe width="100%" height="300px" :src="require("@/assets/Example/d1.mp4")" autoplay></iframe>', '摄像头', {
                dangerouslyUseHTMLString: true
            });
        },
        edit(id){
            this.editVisible = !this.editVisible;
            this.eqIdList.forEach(item=>{
                if(id == item.id){
                    this.ruleForm = item;
                }
            })
        },
        // editHandleClose(done){
        //     done();
        // }
    }
}
</script>
<style scoped lang="scss">
    .intelligenceDialog {
        .el-dialog{left: unset!important;}
        .el-dialog__header {
            background-color: #455d79;
            color: #fff;
        }
        .iconList{
            .el-icon{font-size:16px}
        }
        .grid-content{margin-bottom:25px;
        }

        .el-select{width:100%}
        .item-title{font-size:16px;}
        .center{margin-top:25px;
            span{margin-top:25px}
        }
        .bottom{margin-top:40px;}
    }
    ul{padding-left:0px;
        li{list-style:none;}
    }
    .oldContent .el-input__inner, .newContent .el-input__inner{color:white;}
</style>
