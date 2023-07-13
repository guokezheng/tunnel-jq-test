<!-- 部门树-单选 -->
<template>
  <!-- 用于带标题的下拉列表 -->
  <div class='department'>
    <treeselect style="width: 100%;font-size: 13px" size="small" @select="checkChange" v-model="deptCode" :options="deptOptions" :show-count="true" placeholder="请选择归属部门" :disable-branch-nodes="true" :beforeClearAll="clearable" />
  </div>
</template>
<script>
import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  components: { Treeselect },
  props: ['deptCode'],
  name: 'department',
  data() {
    return {
      deptOptions: [], //部门树
    }
  },
  watch: {
    deptCode: {
      // 自动触发,只能使用handler
      handler(newVal, oldVal) {
      }
    }
  },
  created() {
    this.getTreeselect()
  },
  methods: {
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data
      })
    },
    // 单选回路
    checkChange(data) {
      this.$emit('getTree', data.code)
    },
    //清除所有选项
    clearable() {
      this.deptCode = null
      this.$emit('clearTree')
    }
  }
}
</script>
