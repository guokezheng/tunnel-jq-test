<template>
  <div>
    <el-scrollbar style="height: 40vh; width: 100%">
      <!-- 滚动条 -->
      <div v-for="(items, index) in option" @click="test(items,index)" style="display: flex;flex-flow: column;justify-content: center;align-items: center;">
        <div 
          style="
            margin-top: 10px;
            background-color: #000000;
            text-align: center;
            line-height: 40px;
            color: yellow;
            float: left;
            position: relative;
            overflow: hidden;
          "
          v-bind:style="divStyle"
        >
          <div
            style="
              line-height: 1;
              position: absolute;
              white-space: nowrap;
              z-index: 1000;
            "
            :style="{
              color: items.COLOR,
              fontSize: items.FONT_SIZE && items.FONT_SIZE + 'px',
              fontFamily: items.FONT,
              left: items.COORDINATE && items.COORDINATE.substring(0, 3) + 'px',
              top: items.COORDINATE && items.COORDINATE.substring(3, 6) + 'px',
            }"
            v-html="items.CONTENT"
          ></div>
        </div>
        <div class="menu_icon">
          <el-button
            type="primary"
            icon="el-icon-top"
            @click="up(items,index)"
            v-if="index != 0"
          ></el-button>

          <el-button
            type="primary"
            icon="el-icon-upload2"
            @click="top(items,index)"
            v-if="index != 0"
          ></el-button>
        
          <el-button
            type="danger"
            icon="el-icon-delete"
            @click="deleteHandle(items)"
          ></el-button>
        
          <el-button
            type="primary"
            icon="el-icon-bottom"
            @click="down(items,index)"
            v-if="index < option.length - 1"
          ></el-button>
        
          <el-button
            type="primary"
            icon="el-icon-download"
            @click="bottom(items,index)"
            v-if="index < option.length - 1"
          ></el-button>
          
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>
<script>
import Sortable from "sortablejs"; // 引入拖拽js 这个是实现拖拽功能的核心，可以通過npm 安裝，

export default {
  props: {
    option: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      //   contentList: this.$props.option,
      dataList: [],
      sortable: null,
      width: "",
      height: "",
    };
  },
  // watch: {
  //   contentLists: function () {
  //     this.$nextTick(function () {
  //       this.test(this.contentLists[this.contentLists.length - 1]);
  //     });
  //   },
  // },
  mounted() {
    // this.setSort()
    this.vmsSizeDivStyle(this.option[0].tissVmsTemplate.screenSize);
    this.test(this.contentLists[this.contentLists.length - 1]);
  },
  computed: {
    contentLists: function () {
      return this.$props.option; // 直接监听props里的status状态
    },
    divStyle: function () {
      return {
        width: this.width + "px",
        height: this.height + "px",
      };
    },
    divStyleHeight: function () {
      return {
        height: this.height + "px",
      };
    },
  },
  methods: {
    //修改分辨率尺寸
    vmsSizeDivStyle(data) {
      let a = data.split("*");
      this.width = a[0];
      this.height = a[1];
    },
    setSort() {
      const el = this.$refs.dragTable.$el.querySelectorAll(
        ".el-table__body-wrapper > table > tbody"
      )[0];
      this.sortable = new Sortable(el, {
        ghostClass: "blue-background-class", 
        // Class name for the drop placeholder,// 抓取的元素的类名，简单来说就是抓取元素的时候给他加一个类名blue-background-class
        setData: function (dataTransfer) {
          dataTransfer.setData("Text", "");
        },
        onEnd: (evt) => {
          // console 一下 evt ，你就明白下面为什么这样写
          const targetRow = this.contentLists.splice(evt.oldIndex, 1)[0];
          this.contentLists.splice(evt.newIndex, 0, targetRow);
          this.$emit("dataList", this.contentLists); //最新排序数据
        },
      });
    },
    test(data,index) {
      // 触发父组件contentList事件
      this.$emit("contentList", index);
    },
    up(data, index) {
      if (index == 0) {
        return this.$message.error("当前为第一行，不可上移！");
      }
      let templateContent = JSON.parse(
        JSON.stringify(this.option[index - 1].templateContent)
      );
      let tissVmsTemplate = JSON.parse(
        JSON.stringify(this.option[index - 1].tissVmsTemplate)
      );
      this.option[index - 1].templateContent = data.templateContent;
      this.option[index - 1].tissVmsTemplate = data.tissVmsTemplate;
      this.option[index].tissVmsTemplate = tissVmsTemplate;
      this.option[index].templateContent = templateContent;
    },
    down(data, index) {
      if (index == this.option.length - 1) {
        return this.$message.error("当前为最后行，不可下移！");
      }
      let templateContent = JSON.parse(
        JSON.stringify(this.option[index + 1].templateContent)
      );
      let tissVmsTemplate = JSON.parse(
        JSON.stringify(this.option[index + 1].tissVmsTemplate)
      );
      this.option[index + 1].templateContent = data.templateContent;
      this.option[index + 1].tissVmsTemplate = data.tissVmsTemplate;
      this.option[index].tissVmsTemplate = tissVmsTemplate;
      this.option[index].templateContent = templateContent;
    },
    //最上移
    top(data, index) {
      if (index == 0) {
        return this.$message.error("当前为第一行，不可上移！");
      }
      // let obj = {
      //   tissVmsTemplate: data.tissVmsTemplate,
      //   templateContent: data.templateContent,
      // };
      let obj = data;
      this.contentLists.splice(index, 1);
      this.option.unshift(obj);
      for (let index = 0; index <= this.option.length - 1; index++) {
        this.option[index].item = index > 9 ? "0" + index : "00" + index;
        this.option[index].isRecommend = index > 9 ? "0" + index : "00" + index;
      }
    },
    //最下移
    bottom(data, index) {
      if (index == this.option.length - 1) {
        return this.$message.error("当前为最后行，不可下移！");
      }
      // let obj = {
      //   tissVmsTemplate: data.tissVmsTemplate,
      //   templateContent: data.templateContent,
      // };
      let obj = data;
      this.contentLists.splice(index, 1);
      this.option.push(obj);
      for (let index = 0; index <= this.option.length - 1; index++) {
        this.option[index].item = index > 9 ? "0" + index : "00" + index;
        this.option[index].isRecommend = index > 9 ? "0" + index : "00" + index;
      }
    },
    deleteHandle(data) {
      let a = {};
      this.contentLists.forEach(function (item, index) {
        if (item == data) {
          a = item;
        }
      });
      let b = this.contentLists.indexOf(a);
      this.contentLists.splice(b, 1);
      if (this.contentLists.length == 0) {
        this.$emit("length0", 0);
      }
    },
    /* 单条发布按钮 */
    deleteAllHandle() {
      let that = this;
      that.contentLists.forEach(function (item, index) {
        that.contentLists.splice(0, that.contentLists.length - 1);
      });
    },
    /* 追加发布按钮 */
    addRelease(addRelease) {
      let addParams = {};
      if (addRelease != null) {
        addParams = addRelease
        // addParams = {
        //   isRecommend: this.contentLists.length,
        //   item:
        //     this.contentLists.length < 10
        //       ? "00" + this.contentLists.length
        //       : "0" + this.contentLists.length,
        //   tissVmsTemplate: {
        //     applyType: addRelease.applyType,
        //     screenSize: addRelease.screenSize,
        //     rollSpeed: addRelease.rollSpeed,
        //     stopTime: addRelease.stopTime,
        //     id: null,
        //     inScreenMode: addRelease.inScreenMode,
        //   },
        //   templateContent: addRelease.item,
        // };
      } else {
        addParams = {
          isRecommend: this.contentLists.length,
          item:
            this.contentLists.length < 10
              ? "00" + this.contentLists.length
              : "0" + this.contentLists.length,
          tissVmsTemplate: {
            applyType: null,
            screenSize: null,
            rollSpeed: "1",
            stopTime: "10",
            id: null,
            inScreenMode: "1",
          },
          templateContent: [
            {
              fontType: "KaiTi",
              coordinate: "000000",
              fontSpacing: 0,
              fontSize: "24",
              id: null,
              templateId: null,
              content: "请输入内容",
              fontColor: "yellow",
            },
          ],
        };
      }
      this.option.push(addParams);
      console.log(this.option,'this.optionthis.optionthis.option')

    },
    //保存本次按钮后返回主页数据格式
    saveContentS() {
      this.$emit("contentArr", this.contentList);
    },
  },
};
</script>
<style scoped>
  .menu_icon{
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top:15px;
  }
  .menu_icon .el-col{margin-right: 10px;}
  .menu_icon .el-col:nth-last-child(1){margin-right: 0px;}
  .menu_icon button{
    padding: 5px;
    margin: 0 10px;
  }
</style>
