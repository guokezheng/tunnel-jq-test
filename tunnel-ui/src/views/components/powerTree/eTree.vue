<!--公用供配电单元树-->
<template>
  <div>
    <el-row v-if="filter" class="eTreeInput">
      <el-input
        v-model="deptName"
        placeholder="请输入变电站名称"
        clearable
        size="small"
        prefix-icon="el-icon-search"
        style="margin-bottom: 20px"
      />
    </el-row>
    <el-row class="leftTree">
      <el-tree
        class="tree"
        :data="deptOptions"
        :props="defaultProps"
        :expand-on-click-node="false"
        :check-on-click-node="true"
        :show-checkbox="show_checkbox"
        :check-strictly="check_strictly"
        :filter-node-method="filterNode"
        ref="tree"
        default-expand-all
        @node-click="handleNodeClick"
        @check="handleCheckChange"
        node-key="code"
      />
    </el-row>
  </div>
</template>

<script>
import {powerTreeselect} from '@/api/configcenter/power';
export default {
  name: "deptTree",
  props:{
    //开启过滤
    filter:{
      type:Boolean,
      default:true,
    },
    //节点是否可被选择
    show_checkbox:{
      type:Boolean,
      default:false,
    },
    //是否级联
    check_strictly:{
      type:Boolean,
      default:false,
    },
    //开启默认全选
    default_check_all:{
      type:Boolean,
      default:false,
    },
    //开启默认选中第一个子节点
    default_check_first:{
      type:Boolean,
      default:false,
    },
    //默认第一个节点高亮选中
    default_select_first_child:{
      type:Boolean,
      default:false,
    },
    //默认第一个子节点高亮选中
    default_select_first:{
      type:Boolean,
      default:false,
    }
  },
  data(){
    return{
      //部门名称
      deptName:null,
      //部门选项
      deptOptions:[],
      defaultProps: {
        value: 'code',
        children: "children",
        label: "label"
      },
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  mounted() {
    this.getTreeselect();
  },
  methods:{
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.powerName.indexOf(value) !== -1;
    },
    /** 查询部门下拉树结构 */
    async getTreeselect() {
      await powerTreeselect().then(response => {
        this.deptOptions = response.data;
      });
      this.showCheckBox();
      this.selectFirstChild();
      this.selectFirstNode();
    },
    //节点单击事件
    handleNodeClick(data) {
      this.$emit('nodeClick',data);
    },
    //第一个子节点，高亮选中
    selectFirstChild(){
      if(this.default_select_first_child){
        let first = this.getFirstChildren(this.deptOptions);
        this.$refs.tree.setCurrentKey(first);
        this.$emit('defaultSelectChild',first);
      }
    },
    //第一个节点，高亮选中
    selectFirstNode(){
      if(this.default_select_first){
        if(this.deptOptions.length>0){
          let first = this.deptOptions[0].code;
          this.$refs.tree.setCurrentKey(first);
          this.$emit('defaultSelect',first);
        }

      }
    },

    //节点选中事件--复选框
    handleCheckChange(data, checked){
      this.$emit('nodeCheck',data,checked);
    },
    //默认选中--复选框
    showCheckBox(){
      if(this.show_checkbox){
        //默认全选
        if(this.default_check_all){
          let all=this.getAllKeys(this.deptOptions);
          this.$refs.tree.setCheckedKeys(all);
          this.$emit('defaultCheck',all);
        }
        //默认选中第一个子节点
        if(this.default_check_first){
          let arr = [];
          let first = this.getFirstChildren(this.deptOptions);
          if(first) arr.push(first);
          this.$refs.tree.setCheckedKeys(arr);
          this.$emit('defaultCheck',arr);
        }
      }
    },

    //获取所有节点
    getAllKeys(node,arr=[]) {
      for(let item of node) {
        arr.push(item.code)
        let parentArr = []
        if(item.children) parentArr.push(...item.children)
        if(parentArr && parentArr.length) this.getAllKeys(parentArr,arr)
      }
      return arr
    },
    //获取第一个子节点
    getFirstChildren(node){
      if(node.length){
        return node[0].children ? this.getFirstChildren(node[0].children) : node[0].code
      }
      return null;
    }
  }
}
</script>

<style lang="scss" scoped>
.leftTree{
  height: calc(100vh - 215px);
  overflow-y: auto;
}
</style>
