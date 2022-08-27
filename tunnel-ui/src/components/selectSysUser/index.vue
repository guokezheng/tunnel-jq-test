<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="30%"
    :before-close="handleClose">
    <div style="height: 300px;overflow-y: auto">
      <e-tree
        :data="dataList"
        show-checkbox
        :props="treeProps"
        @check-change="check"
        ref="tree"
        node-key="id">
      </e-tree>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import eTree from '@/components/tree'
  import { treeDeptUser } from '@/api/system/dept'
  export default {
    name: "index",
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: '选择用户'
      }
    },
    components: {
      eTree
    },
    data() {
      return {
        dialogVisible: false,
        treeProps: {
            label: 'name',
            children: 'children'
        },
        dataList: []
      }
    },
    watch: {
      visible(val) {
        if(val) {
          this.init()
        }
      }
    },
    methods: {
      async init() {
        await this.getTree()
        this.dialogVisible = true
      },
      async getTree() {
        const res = await treeDeptUser()
        this.dataList = res.data
      },
      handleSubmit() {
        this.$emit('confirm', this.$refs.tree.getCheckedKeys(true))
        this.handleClose()
      },
      handleClose() {
        this.dialogVisible = false
        this.$emit('close')
      }
    }
  }
</script>

<style scoped>

</style>
