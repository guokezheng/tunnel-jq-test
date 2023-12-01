<!-- 部门树-单选 -->
<template>
  <div class='department'>
    <treeselect class="tree_sty" @select="checkChange" v-model="deptId" :options="deptOptions" :show-count="true" placeholder="请选择归属部门" :disable-branch-nodes="true" :beforeClearAll="clearable" :normalizer="normalizer" />
  </div>
</template>
<script>
import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  components: { Treeselect },
  props: {},
  name: 'department',
  data() {
    return {
      deptOptions: [], //部门树

      defaultProps1: {
        children: 'children',
        label: 'label',
        id: 'id'
      },
      selectData: [], //选中标签名
      optionData: [],
      selectIds: [], //选中ids
      firstSelectItem:[],
      default_check_first: true, //默认选中第一项

      deptId: null
    }
  },
  watch: {
    selectData() {
      this.$refs.select.visible = false
    }
  },
  created() {
    this.getTreeselect()
  },
  methods: {
    // 去掉括号中默认选中的unknown
     normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.code,
        label: node.label,
        children: node.children
      }
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data
        this.firstSelectItem = []
        this.firstSelectItemFn(this.deptOptions)
        // 首次加载默认选中第一个
        this.deptId = this.firstSelectItem[0].code
        this.$emit('getTree',this.firstSelectItem[0].code )
      })
    },
    // 单选回路
    checkChange(data) {
      this.$emit('getTree', data.code)
    },
    // 默认选中第一个
    firstSelectItemFn(item) {
      item.forEach(i => {
        if (!i.children) {
          this.firstSelectItem.push(i)
          return
        } else {
          this.firstSelectItemFn(i.children)
        }
      })
    },
    /** 查询回路树结构 */
    //当复选框勾选中时，给下拉框赋值并且给查询条件赋值
    // checkChange(data, checked) {
    //   let arrLabel = [];
    //   let arr = [];
    //   this.selectIds=[]
    //   checked.checkedNodes.forEach(item => {
    //     arrLabel.push(item.label);
    //     arr.push(item);
    //   });
    //   this.selectData = arrLabel;
    //   this.optionData = arr;
    //   for (let i in arr) {
    //     this.selectIds.push(arr[i].id);
    //   }
    //   this.$emit('getTree',this.selectIds)
    // },
    //清除所有选项
    clearable() {
      // debugger
      // this.$refs.selectTree.setCheckedKeys([]);
      this.deptId = null
      this.$emit('clearTree')
    }
    //当多选时候，删除小标签时，将删除的子节点状态改为未勾选，重新查询
    // remove(val) {
    //   this.selectIds = [];
    //   this.$refs.selectTree.setChecked(val, false);
    //   let arr = this.$refs.selectTree.getCheckedNodes();
    //   for (let i in arr) {
    //     this.selectIds.push(arr[i].id);
    //   }
    //   this.$emit('getTree',this.selectIds)
    // },
  }
}
</script>
<style lang="scss" scoped>
/* @import url(); 引入css类 */
.tree_sty {
  width: 90%;
  margin-bottom: 10px;
  margin-left: 14px;
  font-size: 13px;
}
</style>
