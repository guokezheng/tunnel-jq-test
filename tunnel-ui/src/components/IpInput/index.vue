<template>
  <ul class="ipAdress">
    <li v-for="(item,index) in ipAddres">
      <input ref="ipInput" v-model="item.value" type="text" class="ipInputClass" @input="checkIpVal(item)"
        @keyup="turnIpPosition(item,index,$event)" />
      <div></div>
    </li>
  </ul>
</template>

<script>
  export default {
    name: 'IpInput',
    props: {
        value: {
            type: String,
            default: ''
        },
    },
    data() {
        return {
            ipAddres: this.getIpVal()
        }
    },
    watch: {
        ipAddres: { // 双向数据绑定的value
            handler: function (newVal, oldVal) {
                let str = ''
                for (const i in this.ipAddres) {
                    if(i==4){
                      break;
                    }
                    if (this.ipAddres[i].value === '') {
                      if(i==3){
                        str += '';
                      }else{
                        str += '.';
                      }
                    }else{
                      if(i==3){
                        str = str + this.ipAddres[i].value;
                      }else{
                        str = str + this.ipAddres[i].value + ".";
                      }
                    }
                }
                if(str == "..."){
                  str = "";
                }
                this.$emit('input', str);
            },
            deep: true,
        },
        value: {
          deep: true,
          handler(val) {
            this.ipAddres = this.getIpVal();
          }
        }
    },
    mounted() {
      this.$nextTick(() => {
        this.ipAddres = this.getIpVal();
      })
    },
    methods: {
        // 检查ip输入为0-255
        checkIpVal(item) {
            //确保每个值都处于0-255
            let val = item.value;
            // 处理非数字
            val = val.toString().replace(/[^0-9]/g,'');
            val = parseInt(val, 10);
            if(isNaN(val)) {
                val = ''
            } else {
                val = val < 0 ? 0 : val;
                val = val > 255 ? 255 : val;
            }
            item.value = val;
        },
        getIpVal(){
          if(this.value && this.value != "..."){
            var arr = this.value.split(".");
            var result = [];
            for(var i=0;i<arr.length;i++){
              var json = {}
              json.value = arr[i];
              result.push(json);
            }
            return result;
          }else{
            var result = [
                  {
                      value: ''
                  },{
                      value: ''
                  },{
                      value: ''
                  },{
                      value: ''
                  }
              ];
              return result;
          }
        },
        // 光标位置判断
        turnIpPosition(item, index, event) {
            let self = this;
            let e = event || window.event;
            if (e.keyCode === 37) { // 左箭头向左跳转，左一不做任何措施
                if(index !== 0 && e.currentTarget.selectionStart === 0) {
                    self.$refs.ipInput[index - 1].focus();
                }
            } else if (e.keyCode == 39) { // 右箭头向右跳转，右一不做任何措施
                if(index !== 3 && e.currentTarget.selectionStart === item.value.toString().length) {
                    self.$refs.ipInput[index + 1].focus();
                }
            } else if (e.keyCode === 8) { // 删除键把当前数据删除完毕后会跳转到前一个input，左一不做任何处理
                if(index !== 0 && item.value === '') {
                    self.$refs.ipInput[index - 1].focus();
                }
            } else if (e.keyCode === 13 || e.keyCode === 32 || e.keyCode === 190) {// 回车键、空格键、冒号均向右跳转，右一不做任何措施
                if(index !== 3) {
                    self.$refs.ipInput[index + 1].focus();
                }
            } else if (item.value.toString().length === 3) { // 满3位，光标自动向下一个文本框
                if(index !== 3) {
                    self.$refs.ipInput[index + 1].focus();
                }
            }
        }
    }
}
</script>
<style type="text/css" scoped>
  .ipAdress {
    display: flex;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    width: 196px;
    height: 36px;
    padding-inline-start: 0px;
  }

  .ipAdress li {
    position: relative;
    height: 36px;
    margin: 0;
    list-style-type: none;
  }

  .ipInputClass {
    border: none;
    width: 49px;
    height: 23px;
    text-align: center;
    background: transparent;
  }

  .ipAdress li div {
    position: absolute;
    bottom: 8px;
    right: 0;
    border-radius: 50%;
    background: #505050;
    width: 2px;
    height: 2px;
  }

  /*只需要3个div*/
  .ipAdress li:last-child div {
    display: none;
  }

  /*取消掉默认的input focus状态*/
  .ipAdress input:focus {
    outline: none;
  }
</style>
