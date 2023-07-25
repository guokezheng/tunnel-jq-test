<!-- 部门树-单选-可选择父节点 -->
<template>
  <div class="department">
    <treeselect
      style="width: 100%; font-size: 13px"
      @select="checkChange"
      v-model="deptId"
      :options="deptOptions"
      :show-count="true"
      placeholder="请选择归属部门"
      :beforeClearAll="clearable"
    />
  </div>
</template>
<script>
// import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { siteTree } from "@/api/energy/api";

export default {
  components: { Treeselect },
  props: {},
  name: "department",
  data() {
    return {
      deptOptions: [], //部门树

      defaultProps1: {
        children: "children",
        label: "label",
        id: "id",
      },
      selectData: [], //选中标签名
      optionData: [],
      selectIds: [], //选中ids

      default_check_first: true, //默认选中第一项
      firstSelectItem: null, //默认选中第一条
      deptId: null,
    };
  },
  watch: {
    selectData() {
      this.$refs.select.visible = false;
    },
  },
  created() {
    this.getTreeselect();
  },
  methods: {
    /** 查询部门下拉树结构 */
    getTreeselect() {
      siteTree().then((response) => {
        this.deptOptions = response.data;
        // console.log(this.deptOptions);
        this.firstSelectItem = null;
        this.deptId = this.deptOptions[0].id;
        this.$emit("getTreeFirst", this.deptOptions[0].id);
      });
    },
    // 单选回路
    checkChange(data) {
      // console.log(data);
      this.$emit("getTree", data.id);
    },
    /** 查询回路树结构 */
    //当复选框勾选中时，给下拉框赋值并且给查询条件赋值
    // checkChange(data, checked) {
    //   // console.log(checked);
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
      this.deptId = null;
      this.$emit("clearTree");
    },
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
  },
};
</script>
<style scoped>
/* @import url(); 引入css类 */
</style>
