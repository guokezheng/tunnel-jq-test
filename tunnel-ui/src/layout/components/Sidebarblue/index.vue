<template>
    <div :class="{'has-logo':showLogo}" style="height:50px;background: #004375;">
        <logo-blue :class="weatherView == 'true'?'index_logo':'blue_index_logo'" :collapse="isCollapse"/>
        <div class="workbenchNavbar">
            <router-link to="/index">
            <i class="el-icon-setting"></i>
            <span v-if="topNav||!isCollapse">工作台</span>
            </router-link>
        </div>
        <i class="el-icon-arrow-left" @click="prevScroll" :style="weatherView == 'true'?'':'left:31%;'"></i>
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper"
            :style="weatherView == 'true'?'width:60%;height:100%;background-color:unset;margin:0 3%;':'width:71%;height:100%;background-color:unset;margin:0 3%;'" ref="scroll">
            <!-- @wheel.native.prevent="changeScroll" -->
            <el-menu
                ref="currentNav"
                :default-active="activeMenu"
                :collapse="isCollapse"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="true"
                mode="horizontal"
            >
                <sidebar-item-blue
                    :style="style"
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                    class="end_item"
                />
            </el-menu>
        </el-scrollbar>
        <i class="el-icon-arrow-right" @click="nextScroll"></i>
    </div>
</template>

<script>
import logoBg from '@/assets/logo/logo_bg.png'
import { mapGetters, mapState } from "vuex";
import LogoBlue from "./LogoBlue";
import TopNav from '@/components/TopNav'
import SidebarItemBlue from "./SidebarItemBlue";
import variables from "@/assets/styles/variables.scss";
import { number } from 'echarts';

export default {
    components: { SidebarItemBlue, LogoBlue, TopNav},
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar"]),
        activeMenu() {
            const route = this.$route;
            const { meta, path } = route;
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu;
            }
            return path;
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo;
        },
        variables() {
            return variables;
        },
        isCollapse() {
            return !this.sidebar.opened;
        },
        topNav(){
            return this.$store.state.settings.topNav
        },
        weatherView(){
            return this.$store.state.settings.topNav
        }
    },
    data(){
        return{
            logoBg:logoBg,
            initial:0,
            style:null,
        }
    },
    mounted(){
        // 当前导航栏子元素数量
        const childrenLength = this.$refs.currentNav.$el['childElementCount'];
        console.log(childrenLength)
        if(childrenLength > 6){
            this.style = "width:16.7%;";
        }else{
            this.style = "width:20%;";
        }
    },
    methods: {
        prevScroll(){
            let wrap = this.$refs.scroll.$refs.wrap
            wrap.scrollLeft = wrap.scrollLeft - 150;
        },
        nextScroll(){
            let wrap = this.$refs.scroll.$refs.wrap
            wrap.scrollLeft = wrap.scrollLeft + 150;
        },
        changeScroll(e) {
            let wrap = this.$refs.scroll.$refs.wrap
            wrap.scrollLeft = wrap.scrollLeft - e.wheelDelta
        }
    }
};
</script>
<style scope lang="scss">
    #el-scrollbar-top{
        width:100%;
        height: 50px !important;
        left:unset;
        top:unset;
        bottom:unset;
        position: unset;
    }
    .theme-dark,.theme-blue  .el-scrollbar__wrap{
        margin-right: 0px !important;
        margin-bottom: 0px !important;
    }
    .theme-blue{
        .blue_index_logo{width:22%;}
        .index_logo{width:29%;}
        #index_logo{height:7.6vh;background-size: 100% 100%;z-index: 999;border-bottom-right-radius:121px;line-height: 7.6vh;}
        .el-men-flex{display:flex;justify-content:left;align-items:center;}
        .index_menu{
            el-scrollbar.theme-blue{
                .el-scrollbar__bar.is-vertical{display:none;}
            }
            .el-scrollbar__wrap{height: 100%;overflow: hidden;margin-right: 0px !important;margin-bottom: 0px !important;}
            .el-scrollbar__view{width:100%;height:100%;
                .end_item{white-space:nowrap;display:inline-block;vertical-align:initial;}
                .el-submenu{
                    div{width:100%;
                        span{width:100%;display:block;text-align: center;}
                    }
                    .el-submenu__title{padding:0px;}
                    .el-submenu__title:hover{background-color:unset;}
                }
                span{display:block;}
            }
            .el-icon-arrow-right,.el-icon-arrow-left{position:absolute;top: 29px;font-size:16px;color:#fff;cursor: pointer;}
            .el-icon-arrow-right{right:-4%;}
            .el-icon-arrow-left{left:47%;z-index: 999;}
            .is-horizontal{display:none;}
        }
        // .el-menu{background-color:unset;}
        // .el-menu-item:hover, .el-menu-item:focus{background-color:unset!important;}
        // .el-menu-item:hover, .el-menu-item:focus{background-color:#00588f!important;}
        .sidebar-logo-container{background-color:unset!important;background:unset;}
        .el-scrollbar{
            .el-scrollbar__view{
                .el-menu{
                    white-space: nowrap;width:100%;
                    // .children{position: relative;}
                    .children.is-active::after{
                        content:'';
                        position:absolute;
                        top: 22px;
                        left:20%;
                        width:4px;
                        height:4px;
                        background-color: #3acbf4;
                        border-radius: 25px;
                    }
                    .children.is-active::before{
                        content:'';
                        position:absolute;
                        top: 22px;
                        right: 20%;
                        width:4px;
                        height:4px;
                        background-color: #3acbf4;
                        border-radius: 25px;
                    }
                    div a{display:block;}
                    .submenu-title-noDropdown{color: white;}
                    .is-active span{color: #fff;}
                } 
            } 
        }
        .el-menu.el-menu--horizontal{border:unset;}
        .el-menu{
            .nest-menu{
                a{
                    .is-active{
                        // span{color: #fff!important;}
                    }
                }
            }
        }
    }

</style>
