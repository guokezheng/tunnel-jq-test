<!-- 分类-多选 -->
<template>
  <div>
    <div style="width: 100%; padding: 10px 0;" class="box">
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
        <!-- 级联 全选 -->
        <div class="check" v-if="show_checkbox">
          <el-checkbox v-model="check_strictly">级联选择</el-checkbox>
          <el-checkbox
            v-model="default_check_all"
            @change="handleCheckedTreeNodeAll($event, 'menu')"
            >全选</el-checkbox
          >
        </div>
        <el-tree
          class="tree"
          :data="classificationOptions"
          :props="defaultProps"
          :expand-on-click-node="false"
          :check-on-click-node="true"
          :show-checkbox="show_checkbox"
          :check-strictly="!check_strictly"
          :filter-node-method="filterNode"
          ref="tree"
          default-expand-all
          @node-click="handleNodeClick"
          @check="handleCheckChange"
          node-key="id"
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
// import { getClassificationTree } from "@/api/configcenter/classification";
import departmentSelect from "@/views/components/department";
import { classificationTreeselect } from "@/api/energy/api";

export default {
  name: "classificationTree",
  components: { departmentSelect },
  props: {
    //开启过滤
    filter: {
      type: Boolean,
      default: true,
    },
    //节点是否可被选择
    show_checkbox: {
      type: Boolean,
      default: false,
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
        value: "id",
        label: "label",
        children: "children",
      },

      default_check_first: true, //默认选中第一项
      check_strictly: false, //级联选择
      default_check_all: false, //全选
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
    //节点选中状态发生变化时的回调
    // clickTree(ids) {
    //   this.$emit('deptId', ids)
    // },
    // // 清除选择站点
    // clearTree() {
    //   this.$emit('clear')
    // },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      let arr = [];
      if (value) {
        this.getAllKeys(this.classificationOptions, arr, "menuId");
      }
      this.$refs.tree.setCheckedKeys(arr);
      this.$emit("defaultCheck", arr);
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 查询回路树结构 */
    async getLoopTree() {
      const response = await classificationTreeselect();
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
      let n = this.getAllKeys(this.classificationOptions);
      if (checked.checkedKeys.length === n.length) {
        this.default_check_all = true;
      } else {
        this.default_check_all = false;
      }
      this.$emit("nodeCheck", data, checked);
      // console.log(data, checked);
    },
    //默认选中--复选框
    showCheckBox() {
      if (this.show_checkbox) {
        //默认全选
        if (this.default_check_all) {
          let all = this.getAllKeys(this.classificationOptions);
          this.$refs.tree.setCheckedKeys(all);
          this.$emit("defaultCheck", all);
        }
        //默认选中第一个子节点
        if (this.default_check_first) {
          let arr = [];
          let first = this.getFirstChildren(this.classificationOptions);
          if (first) arr.push(first);
          this.$refs.tree.setCheckedKeys(arr);
          this.$emit("defaultCheck", arr);
        }
      }
    },

    //获取所有节点
    getAllKeys(node, arr = []) {
      for (let item of node) {
        arr.push(item.id);
        let parentArr = [];
        if (item.children) parentArr.push(...item.children);
        if (parentArr && parentArr.length) this.getAllKeys(parentArr, arr);
      }
      return arr;
    },
    //获取第一个子节点
    getFirstChildren(node) {
      if (node.length) {
        // return node[0].children && node[0].children.length > 0
        //   ? this.getFirstChildren(node[0].children)
        //   : node[0].code;
        return node[0].id;
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
