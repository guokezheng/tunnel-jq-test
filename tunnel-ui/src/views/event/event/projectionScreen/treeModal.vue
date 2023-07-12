<template>
  <div class="treeOneClass">
    <div class="tree">
      <div v-for="node in treeData" :key="node.id">
        <div @click="toggleNode(node)" class="toggleNodeClass"  >
          <div :class="{'node': true, 'expanded': node.expanded}" class="treeClass" draggable="true" @dragstart="dragStart">{{ node.name }}</div>
        </div>
        <div v-if="node.expanded">
          <tree :treeData="node.children"></tree>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: 'Tree',
  props: {
    treeData: {
      type: Array,
      default: () => []
    },
    indent: {
      type: Number,
      default: 20
    }
  },
  methods: {
    toggleNode(node) {
      node.expanded = !node.expanded;
    },
    dragStart(event) {
      event.dataTransfer.setData('text/plain', event.target.innerText);
    },
    dragOver(event) {
      event.preventDefault();
    },
    drop(event) {
      event.preventDefault();
      const data = event.dataTransfer.getData('text/plain');
      event.target.innerText = data;
    }
  }
};
</script>

<style scoped>
.treeOneClass{
  width: 170px;
  background-color:#05213E;
}
.tree {
  margin-left: 15px;
  background-color:#05213E;
}

.node {
  cursor: pointer;
}

.expanded::before {
  content: '-';
  margin-right: 2px;
}




.drag-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.box {
  width: 100px;
  height: 100px;
  background-color: #f0f0f0;
  text-align: center;
  line-height: 100px;
  cursor: move;
}

.target {
  width: 200px;
  height: 200px;
  background-color: #eee;
  text-align: center;
  line-height: 200px;
}
.treeClass{
  text-align: left;
  line-height: 30px;
  height: 26px;
  transition: background-color 0.3s ease; /* 添加过渡效果 */
}
.treeClass:hover {
  background-color: #004375; /* 悬停时的背景颜色 */
}
.toggleNodeClass{
  text-align: center;
}
</style>
