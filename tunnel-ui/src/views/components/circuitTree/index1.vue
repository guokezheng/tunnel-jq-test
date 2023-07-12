<template>
  <div>
    <div style="
        width: 100%;
        padding: 10px 0;
        text-align: center;" class="box">
      <el-row>
        <!-- <deptTree style="width: 90%;margin-bottom: 10px;margin-left:14px;font-size: 13px;"
                  @select="getCircuitTree" v-model="deptCode" :options="deptOptions"
                  :show-count="true" placeholder="请选择归属部门" /> -->

        <el-select v-model="selectData" style="width: 90%;margin-bottom: 10px;" placeholder="请选择归属部门" multiple clearable collapse-tags @clear="clearable" @remove-tag="remove" ref="select">
          <el-option v-model="optionData" style="height: auto">
            <el-tree :data="deptOptions" show-checkbox node-key="label" ref="selectTree" highlight-current :props="defaultProps1" @check="checkChange"></el-tree>
          </el-option>
        </el-select>
      </el-row>
      <el-row v-if="filter">
        <el-input v-model="label" placeholder="请输入线路名称" clearable size="small" suffix-icon="el-icon-search" style="width: 90%" />
      </el-row>
    </div>
    <el-scrollbar>
      <el-row :style="{ height: height }">
        <!-- 级联 全选 -->
        <div class="check" v-if="show_checkbox">
          <el-checkbox v-model="check_strictly">级联选择</el-checkbox>
          <el-checkbox v-model="default_check_all" @change="handleCheckedTreeNodeAll($event, 'menu')">全选</el-checkbox>
        </div>
        <el-tree class="tree" :data="treeOptions" :props="defaultProps" :expand-on-click-node="false" :check-on-click-node="true" :show-checkbox="show_checkbox" :check-strictly="!check_strictly" :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick" @check="handleCheckChange" node-key="code">
          <!-- 鼠标移入文本提示 -->
          <div class="showName" slot-scope="{ node, data }">
            <el-tooltip :content="node.label" placement="top" effect="light">
              <span>{{ node.label }}</span>
            </el-tooltip>
          </div>
        </el-tree>
      </el-row>
    </el-scrollbar>
  </div>
</template>

<script>
import { getCircuitTree } from '@/api/configcenter/circuit'
import deptTree from '@riophae/vue-treeselect'
import { treeselect } from '@/api/system/dept'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import node from 'vue-resource/src/http/client/node'

export default {
  name: 'loopTree',
  components: { deptTree },
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true
    },
    //节点是否可被选择
    show_checkbox: {
      type: Boolean,
      default: false
    },
    //是否级联
    // check_strictly: {
    //   type: Boolean,
    //   default: false,
    // },
    // //开启默认全选
    // default_check_all: {
    //   type: Boolean,
    //   default: false,
    // },
    // //开启默认选中第一个子节点
    // default_check_first: {
    //   type: Boolean,
    //   default: false,
    // },
    //默认第一个子节点高亮选中
    default_select_first: {
      type: Boolean,
      default: false
    },
    height: {
      type: String,
      default: 'calc(100vh - 220px)'
    }
  },
  data() {
    return {
      //名称
      label: null,
      // 部门树选项
      deptOptions: null,
      deptCode: null,
      //回路选项
      treeOptions: [],
      defaultProps: {
        value: 'code',
        label: 'label',
        children: 'children'
      },
      defaultProps1: {
        children: 'children',
        label: 'label',
        id: 'id'
      },

      default_check_first: true, //默认选中第一项
      check_strictly: false, //级联选择
      default_check_all: false, //全选

      selectData: [], //选中标签名
      optionData: [],
      selectIds: [] //选中节点id集合
    }
  },
  watch: {
    // 根据名称筛选部门树
    label(val) {
      this.$refs.tree.filter(val)
    },
    selectData() {
      this.$refs.select.visible = false
    }
  },
  created() {
    this.getTreeselect()
  },
  methods: {
    node,
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data
        console.log(this.deptOptions)
      })
    },
    // 树权限（父子联动）
    // handleCheckedTreeConnect (value, type) {
    //   this.check_strictly = value ? true : false
    // },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      let arr = []
      if (value) {
        this.getAllKeys(this.treeOptions, arr, 'menuId')
      }
      this.$refs.tree.setCheckedKeys(arr)
      this.$emit('defaultCheck', arr)
    },
    // 筛选节点时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /** 查询回路树结构 */
    //当复选框勾选中时，给下拉框赋值并且给查询条件赋值
    checkChange(data, checked) {
      // console.log(checked);
      let arrLabel = []
      let arr = []
      this.selectIds = []
      checked.checkedNodes.forEach(item => {
        arrLabel.push(item.label)
        arr.push(item)
      })
      this.selectData = arrLabel
      this.optionData = arr
      for (let i in arr) {
        this.selectIds.push(arr[i].id)
      }
      this.check_strictly = false
      this.default_check_all = false
      this.getCircuitTree()
      this.$emit('choseDepts', this.selectIds, '1')
    },
    // 获取树结构
    async getCircuitTree() {
      this.treeOptions = []
      let ids = this.selectIds.toString()
      if (!ids) {
        this.$message.warning('请先选择归属部门')
        this.treeOptions = []
        return
      }
      const response = await getCircuitTree({ deptCode: id })
      this.treeOptions = response.data == null || response.data.length === 0 ? [] : response.data
      this.$nextTick(() => {
        this.showCheckBox()
        this.selectFirstChild()
      })
    },
    //清除所有选项
    clearable() {
      this.selectIds = []
      this.treeOptions = []
      this.$refs.selectTree.setCheckedKeys([])
      this.check_strictly = false
      this.default_check_all = false
    },
    //当多选时候，删除小标签时，将删除的子节点状态改为未勾选，重新查询
    remove(val) {
      this.selectIds = []
      this.$refs.selectTree.setChecked(val, false)
      let arr = this.$refs.selectTree.getCheckedNodes()
      for (let i in arr) {
        this.selectIds.push(arr[i].id)
      }
      this.check_strictly = false
      this.default_check_all = false
      this.getCircuitTree(this.selectIds)
    },

    //节点单击事件
    handleNodeClick(data) {
      this.$emit('nodeClick', data)
    },

    //第一个子节点，高亮选中
    selectFirstChild() {
      if (this.default_select_first) {
        let first = this.getFirstChildren(this.treeOptions)
        this.$refs.tree.setCurrentKey(first)
        this.$emit('defaultSelect', first, this.$refs.tree.getCurrentNode())
      }
    },
    //节点选中事件--复选框
    handleCheckChange(data, checked) {
      // console.log(data, checked);
      let n = this.getAllKeys(this.treeOptions)
      // 反选
      if (checked.checkedKeys.length === n.length) {
        this.default_check_all = true
      } else {
        this.default_check_all = false
      }
      this.$emit('nodeCheck', data, checked)
    },
    //默认选中--复选框
    showCheckBox() {
      if (this.show_checkbox) {
        //默认全选
        if (this.default_check_all) {
          let all = this.getAllKeys(this.treeOptions)
          this.$refs.tree.setCheckedKeys(all)
          this.$emit('defaultCheck', all)
        }
        //默认选中第一个子节点
        if (this.default_check_first) {
          let arr = []
          let first = this.getFirstChildren(this.treeOptions)
          if (first) arr.push(first)
          this.$refs.tree.setCheckedKeys(arr)
          this.$emit('defaultCheck', arr)
        }
      }
    },

    //获取所有节点
    getAllKeys(node, arr = []) {
      for (let item of node) {
        arr.push(item.code)
        let parentArr = []
        if (item.children) parentArr.push(...item.children)
        if (parentArr && parentArr.length) this.getAllKeys(parentArr, arr)
      }
      return arr
    },
    //获取第一个子节点
    getFirstChildren(node) {
      if (node.length) {
        // return node[0].children && node[0].children.length > 0
        //   ? this.getFirstChildren(node[0].children)
        //   : node[0].code;
        return node[0].code
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.leftTree {
  height: calc(100vh - 250px);
}
.showName {
  width: 170px; //外部容器的宽度
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: block;
}
::v-deep .el-tree-node__content {
  margin: 3px 0 !important;
}
::v-deep .el-input--small .el-input__inner {
  text-align: center;
}
.check {
  padding: 10px 10px;
  ::v-deep .el-checkbox {
    width: 50%;
    height: 20px;
    margin: 0;
    line-height: 20px;
  }
  ::v-deep .el-checkbox__label {
    font-size: 16px;
    padding-left: 8px;
  }
  ::v-deep .el-checkbox__inner {
    margin-bottom: 2px;
  }
}
.theme-blue .box {
  background: none !important;
}
</style>
