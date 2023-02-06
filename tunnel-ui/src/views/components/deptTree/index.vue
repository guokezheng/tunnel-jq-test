<!--公用部门树-->
<template>
<div>
  <el-row v-if="filter">
    <el-input
      v-model="deptName"
      placeholder="请输入部门名称"
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
      node-key="id"
    />
  </el-row>
</div>
</template>

<script>
import {treeselect} from "@/api/system/dept";
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
      return data.label.indexOf(value) !== -1;
    },
    /** 查询部门下拉树结构 */
    async getTreeselect() {
      await treeselect().then(response => {
        this.deptOptions = response.data;
      });
      this.showCheckBox();
      this.selectFirstChild();
    },
    //节点单击事件
    handleNodeClick(data) {
      this.$emit('nodeClick',data);
    },
    //第一个子节点，高亮选中
    selectFirstChild(){
      if(this.default_select_first){
        let first = this.getFirstChildren(this.deptOptions);
        this.$refs.tree.setCurrentKey(first);
        this.$emit('defaultSelect',first);
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
        arr.push(item.id)
        let parentArr = []
        if(item.children) parentArr.push(...item.children)
        if(parentArr && parentArr.length) this.getAllKeys(parentArr,arr)
      }
      return arr
    },
    //获取第一个子节点
    getFirstChildren(node){
        if(node.length){
          return node[0].children ? this.getFirstChildren(node[0].children) : node[0].id
        }
        return null;
    }
  }
}
</script>

<style lang="scss" scoped>
  .leftTree{
    height: calc(100vh - 231px);
    overflow-y: auto;
  }

/*.tree{
::v-deep .el-checkbox__input.is-checked .el-checkbox__inner::after {
  transform: translate(-50%,-50%) scale(1);
}
::v-deep .el-checkbox__inner{
  border-radius: 100%;
}
::v-deep .el-checkbox__inner::after {
  width: 4px;
  height: 4px;
  border-radius: 100%;
  background-color: #fff;
  content: "";
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%) scale(0);
  transition: transform .15s ease-in;
}
}*/
</style>
