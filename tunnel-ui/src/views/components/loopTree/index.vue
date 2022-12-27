<template>
  <div>
    <div
      style="
        width: 100%;
        background: red;
        padding: 10px 0;
        background: #f5f7fa;
        text-align: center;
      "
    >
      <el-row v-if="filter">
        <el-input
          v-model="loopName"
          placeholder="搜索"
          clearable
          size="small"
          prefix-icon="el-icon-search"
          style="width: 90%"
        />
      </el-row>
    </div>
    <el-scrollbar>
      <el-row :style="{ height: height }">
        <el-tree
          class="tree"
          :data="loopOptions"
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
        >
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
import { getLoopTree } from "@/api/configcenter/loop";
export default {
  name: "loopTree",
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
    //是否级联
    check_strictly: {
      type: Boolean,
      default: false,
    },
    //开启默认全选
    default_check_all: {
      type: Boolean,
      default: false,
    },
    //开启默认选中第一个子节点
    default_check_first: {
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
      default: "calc(100vh - 220px)",
    },
  },
  data() {
    return {
      //回路名称
      loopName: null,
      //回路选项
      loopOptions: [
        {
          id: 1,
          loopName: "穆陵关预制舱",
          children: [
            {
              id: 2,
              loopName: "高压部分",
              children: [
                {
                  id: 6,
                  loopName: "高压进线1",
                },
                {
                  id: 7,
                  loopName: "高压进线2",
                },
                {
                  id: 8,
                  loopName: "计量柜",
                },
                {
                  id: 9,
                  loopName: "高压出线1",
                },
                {
                  id: 10,
                  loopName: "高压出线2",
                },
              ],
            },
            {
              id: 3,
              loopName: "低压部分",
              children: [
                {
                  id: 11,
                  loopName: "低压进线D01",
                },
                {
                  id: 12,
                  loopName: "无功补偿柜D02",
                },
                {
                  id: 13,
                  loopName: "低压出线D03",
                  children: [
                    {
                      id: 21,
                      loopName: "左洞阴天照明D03-1",
                    },
                    {
                      id: 22,
                      loopName: "路测D03-2",
                    },
                  ],
                },
              ],
            },
            {
              id: 4,
              loopName: "双舱柜K1+100",
              children: [
                {
                  id: 14,
                  loopName: "远端机（总电量）",
                },
                {
                  id: 15,
                  loopName: "摄像头（1路）",
                },
                {
                  id: 16,
                  loopName: "雷达（2路）",
                },
                {
                  id: 17,
                  loopName: "气象（3路）",
                },
              ],
            },
            {
              id: 5,
              loopName: "一体化智慧杆K1+200",
              children: [
                {
                  id: 18,
                  loopName: "远端机（总电量）",
                },
                {
                  id: 19,
                  loopName: "摄像头（1路）",
                },
                {
                  id: 20,
                  loopName: "雷达（2路）",
                },
              ],
            },
          ],
        },
      ],
      defaultProps: {
        value: "id",
        label: "loopName",
        children: "children",
      },
    };
  },
  watch: {
    // 根据名称筛选部门树
    loopName(val) {
      this.$refs.tree.filter(val);
    },
    powerCode(val) {
      if (val) {
        this.powerCode = val;
        this.getLoopTree();
      }
    },
  },
  methods: {
    // 筛选节点时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    filterNode(value, data) {
      // console.log(value,data);
      if (!value) return true;
      return data.loopName.indexOf(value) !== -1;
    },
    /** 查询回路树结构 */
    // 获取树结构
    async getLoopTree() {
      // await getLoopTree(this.powerCode).then((response) => {
      //   console.log(response);
      //   this.loopOptions =
      //     response.data == null || response.data.length == 0
      //       ? []
      //       : [{ id: 0, loopName: "全选", children: response.data }];
      this.loopOptions = [
        {
          id: 1,
          loopName: "穆陵关预制舱",
          children: [
            {
              id: 2,
              loopName: "高压部分",
              children: [
                {
                  id: 6,
                  loopName: "高压进线1",
                },
                {
                  id: 7,
                  loopName: "高压进线2",
                },
                {
                  id: 8,
                  loopName: "计量柜",
                },
                {
                  id: 9,
                  loopName: "高压出线1",
                },
                {
                  id: 10,
                  loopName: "高压出线2",
                },
              ],
            },
            {
              id: 3,
              loopName: "低压部分",
              children: [
                {
                  id: 11,
                  loopName: "低压进线D01",
                },
                {
                  id: 12,
                  loopName: "无功补偿柜D02",
                },
                {
                  id: 13,
                  loopName: "低压出线D03",
                  children: [
                    {
                      id: 21,
                      loopName: "左洞阴天照明D03-1",
                    },
                    {
                      id: 22,
                      loopName: "路测D03-2",
                    },
                  ],
                },
              ],
            },
            {
              id: 4,
              loopName: "双舱柜K1+100",
              children: [
                {
                  id: 14,
                  loopName: "远端机（总电量）",
                },
                {
                  id: 15,
                  loopName: "摄像头（1路）",
                },
                {
                  id: 16,
                  loopName: "雷达（2路）",
                },
                {
                  id: 17,
                  loopName: "气象（3路）",
                },
              ],
            },
            {
              id: 5,
              loopName: "一体化智慧杆K1+200",
              children: [
                {
                  id: 18,
                  loopName: "远端机（总电量）",
                },
                {
                  id: 19,
                  loopName: "摄像头（1路）",
                },
                {
                  id: 20,
                  loopName: "雷达（2路）",
                },
              ],
            },
          ],
        },
      ];
      // });
      this.showCheckBox();
      this.selectFirstChild();
    },
    //节点单击事件
    handleNodeClick(data) {
      this.$emit("nodeClick", data);
    },

    //第一个子节点，高亮选中
    selectFirstChild() {
      if (this.default_select_first) {
        let first = this.getFirstChildren(this.loopOptions);
        this.$refs.tree.setCurrentKey(first);
        this.$emit("defaultSelect", first, this.$refs.tree.getCurrentNode());
      }
    },
    //节点选中事件--复选框
    handleCheckChange(data, checked) {
      this.$emit("nodeCheck", data, checked);
      // console.log(data, checked);
    },
    //默认选中--复选框
    showCheckBox() {
      if (this.show_checkbox) {
        //默认全选
        if (this.default_check_all) {
          let all = this.getAllKeys(this.loopOptions);
          this.$refs.tree.setCheckedKeys(all);
          this.$emit("defaultCheck", all);
        }
        //默认选中第一个子节点
        if (this.default_check_first) {
          let arr = [];
          let first = this.getFirstChildren(this.loopOptions);
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
        return node[0].children && node[0].children.length > 0
          ? this.getFirstChildren(node[0].children)
          : node[0].id;
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
</style>
