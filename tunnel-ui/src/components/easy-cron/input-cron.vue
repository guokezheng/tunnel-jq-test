<template>
  <div class="input-cron">
    <Input :placeholder="placeholder" style="width: auto" v-model="editCronValue" :disabled="disabled">
      <a slot="append" @click="showConfigDlg" class="config-btn" :disabled="disabled">
        <Icon type="ios-calendar-outline" style="margin-right: 5px;"></Icon>配置
      </a>
    </Input>
   <!-- <input v-model = "editName" /> -->
    <Modal v-model="show" title="定时配置" :closable="true" :width="`${width+50}`"
      :footer-hide="true">
        <div>
          <easy-cron v-model="editCronValue" :style="`width: ${width}px`" :exeStartTime="exeStartTime"
            :hideYear="hideYear" :remote="remote" :hideSecond="hideSecond"></easy-cron>
        </div>
    </Modal>
  </div>
</template>
<script src="../cronStrue/dist/cronstrue.min.js" type="text/javascript"></script>
<script src="../cronStrue/dist/cronstrue-i18n.min.js" type="text/javascript"></script>

<script>
import EasyCron from './index'

export default {
  name: 'input-cron',
  model: {
    prop: 'cronValue',
    event: 'change'
  },
  props: {
    cronValue: {
      type: String,
      default: ''
    },
    width: {
      type: Number,
      default: 700
    },
    placeholder: {
      type: String,
      default: '请输入cron表达式'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    exeStartTime: {
      type: [Number, String, Object],
      default: 0
    },
    hideSecond: {
      type: Boolean,
      default: false
    },
    hideYear: {
      type: Boolean,
      default: false
    },
    remote: {
      type: Function,
      default: null
    }
  },
  data () {
    return {
      editCronValue: this.cronValue,
      show: false,
    //  editName: ''
    }
  },
  watch: {
    cronValue (newVal, oldVal) {
      if (newVal === this.editCronValue) {
  //      this.editName = this.editCronValue+"==123"
        return
      }
      this.editCronValue = newVal
     // this.editName = cronstrue.toString(this.editCronValue, { locale: "zh_CN" })
    //  this.editName = this.editCronValue+"==hao"
    },
    editCronValue (newVal, oldVal) {
    //  alert(newVal)
      this.$emit('change', newVal)
    },
   /* 'editCronValue': {
        handler: function() {
          if (!this.editCronValue) {
            this.editCronValue = null
            this.editName = null
            return
          }
          this.editName = this.editCronValue+"==ni"
          //this.editName = cronstrue.toString(this.editCronValue, { locale: "zh_CN" })
        }
      } */

  },
  methods: {
    showConfigDlg () {
      if (!this.disabled) {
        this.show = true
      }
    }
  },
  components: {
    EasyCron
  }
}
</script>

<style scoped>
.input-cron, .input-ui {

}

.input-cron .ivu-input-wrapper {
  width: 100% !important;
}

.config-btn {
  cursor: pointer;
}

.config-btn:hover {
  color: #2D8CF0;
}
</style>
