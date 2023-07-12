<!-- 机构-单选(只选择最后层级节点) -->
<template>
  <div>
    <div style="
        width: 100%;
        padding: 10px 0;
        text-align: center;" class="box">
      <el-row v-if="filter">
        <el-input v-model="label" placeholder="请输入机构名称" clearable size="small" suffix-icon="el-icon-search" style="width: 90%" />
      </el-row>
    </div>
    <el-scrollbar>
      <el-row :style="{ height: height }">
        <el-tree class="tree" :data="siteTreeOptions" :props="defaultProps" :expand-on-click-node="false" :check-on-click-node="true" :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick" @check="handleCheckChange" node-key="code" highlight-current>
          <!-- 鼠标移入文本提示 -->
          <div class="showName" slot-scope="{ node }">
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
import { treeselect } from '@/api/system/dept'
export default {
  name: 'loopTree',
  components: {},
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true
    },
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
      default: 'calc(100vh - 280px)'
    }
  },
  data() {
    return {
      //名称
      label: null,
      // 部门树选项
      deptCode: null,
      //回路选项
      siteTreeOptions: [],
      defaultProps: {
        value: 'code',
        label: 'label',
        children: 'children'
      },

      default_check_first: true //默认选中第一项
    }
  },
  watch: {
    // 根据名称筛选部门树
    label(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getSiteTree()
  },
  methods: {
    // 筛选节点时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },

    // 获取树结构
    async getSiteTree() {
      await treeselect().then(response => {
        if (response.code === 200) {
          this.siteTreeOptions = response.data == null || response.data.length === 0 ? [] : response.data
        }
      })
      this.$nextTick(() => {
        this.showCheckBox()
        this.selectFirstChild()
      })
    },

    //节点单击事件
    handleNodeClick(data) {
      this.$emit('nodeClick', data)
    },

    //第一个子节点，高亮选中
    selectFirstChild() {
      if (this.default_select_first) {
        let first = this.getFirstChildren(this.siteTreeOptions)
        this.$refs.tree.setCurrentKey(first)
        this.$emit('defaultSelect', first, this.$refs.tree.getCurrentNode())
      }
    },
    //节点选中事件--复选框
    handleCheckChange(data, node) {
      // if (data.children && data.children.length > 0) {
      //   return
      // } else {
        this.$emit('nodeCheck', data)
      // }
    },
    //默认选中--复选框
    showCheckBox() {
      //默认选中第一个子节点
      if (this.default_check_first) {
        let arr = null
        let first = this.getFirstChildren(this.siteTreeOptions)
        if (first) arr = first
        this.$refs.tree.setCurrentKey(arr)
        this.$emit('defaultCheck', arr)
      }
    },

    //获取第一个子节点
    getFirstChildren(node) {
      if (node.length) {
        // return node[0].children && node[0].children.length > 0 ? this.getFirstChildren(node[0].children) : node[0].code
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
