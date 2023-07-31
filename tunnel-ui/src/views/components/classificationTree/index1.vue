<!-- 分类-单选 -->
<template>
  <div>
    <div style="width: 100%; padding: 10px 0; text-align: center" class="box">
      <el-row>
        <!-- <department-select @getTree="clickTree" @clearTree="clearTree"></department-select> -->
      </el-row>
      <el-row v-if="filter">
        <el-input
          v-model="label"
          placeholder="请输入分类名称"
          clearable
          size="small"
          suffix-icon="el-icon-search"
          style="width: 100%"
        />
      </el-row>
    </div>
    <el-scrollbar>
      <el-row :style="{ height: height }">
        <el-tree
          class="tree"
          :data="classificationOptions"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :filter-node-method="filterNode"
          ref="tree"
          default-expand-all
          @node-click="handleNodeClick"
          @check="handleCheckChange"
          node-key="code"
        >
          <div class="showName" slot-scope="{ node, data }">
            <!-- <el-tooltip :content="node.label" placement="top" effect="light"> -->
              <span>{{ node.label }}</span>
            <!-- </el-tooltip> -->
          </div>
        </el-tree>
      </el-row>
    </el-scrollbar>
  </div>
</template>

<script>
// import { getClassificationTree } from '@/api/configcenter/classification'
import departmentSelect from "@/views/components/department";

export default {
  name: "classificationTree",
  components: { departmentSelect },
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true,
    },
    //默认第一个子节点高亮选中
    default_select_first: {
      type: Boolean,
      default: false,
    },
    powerCode: {
      type: String,
      default: "",
    },
    height: {
      type: String,
      default: "calc(100vh - 280px)",
    },
  },
  data() {
    return {
      //分类名称
      label: null,
      //分类选项
      classificationOptions: [],
      defaultProps: {
        value: "code",
        label: "label",
        children: "children",
      },

      default_check_first: true, //默认选中第一项
    };
  },
  watch: {
    // 根据名称筛选部门树
    label(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getLoopTree();
  },
  methods: {
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 查询回路树结构 */
    async getLoopTree() {
      const response = await getClassificationTree();
      this.classificationOptions =
        response.data == null || response.data.length === 0
          ? []
          : response.data;
      this.$nextTick(() => {
        this.showCheckBox();
        this.selectFirstChild();
      });
    },
    //节点单击事件
    handleNodeClick(data) {
      this.$emit("nodeClick", data);
    },

    //第一个子节点，高亮选中
    selectFirstChild() {
      if (this.default_select_first) {
        let first = this.getFirstChildren(this.classificationOptions);
        this.$refs.tree.setCurrentKey(first);
        this.$emit("defaultSelect", first, this.$refs.tree.getCurrentNode());
      }
    },
    //节点选中事件--复选框
    handleCheckChange(data, checked) {
      this.$emit("nodeCheck", data);
    },
    //默认选中--复选框
    showCheckBox() {
      //默认选中第一个子节点
      if (this.default_check_first) {
        let arr = null;
        let first = this.getFirstChildren(this.classificationOptions);
        if (first) arr = first;
        this.$refs.tree.setCurrentKey(arr);
        this.$emit("defaultCheck", arr);
      }
    },

    //获取第一个子节点
    getFirstChildren(node) {
      if (node.length) {
        return node[0].code;
      }
    },
  },
};
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
// ::v-deep .el-input--small .el-input__inner {
//   text-align: center;
// }
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
