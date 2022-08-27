<template>
  <div class="left-chart-3">
    <div class="lc1-header">事件报警</div>
    <div id="scroll-board">
      <dv-scroll-board :config="config" style="height: 20vh;" />
    </div>
  </div>

</template>

<script>
  import request from '@/utils/request'
  var datav;

  function getContent(eqTunnelId) {
    return request({
      url: '/event/bigscreenEventList?tunnelId='+ eqTunnelId,
      method: 'get'
    })
  }
  export default {
    name: 'ScrollBoard',
    props: ['test'],
    watch: {
      test: {
        deep: true,
        handler(val) {
          this.getEventList();
        }
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.getEventList()
      })
    },
    data() {
      return {
        config: {
          header: ['事件名', '发生时间', '发生地点'],
          data: [
            ['路面危害-松散', '07-01 19:25', 'K19+120'],
            ['路面危害-路面油污清理', '07-02 17:25', 'K19+130'],
            ['路面危害-路面油污清理', '07-02 17:25', 'K19+140'],
            ['路面危害-路面油污清理', '07-02 17:25', 'K19+150'],
            ['路面危害-路面油污清理', '07-02 17:25', 'K19+160'],
          ],
          eventTitle: [],
          eventTime: [],
          columnWidth: [120],
          eventLocation: [],
          index: false,
          align: ['center'],
          rowNum: 4,
          headerBGC: '#1981f6',
          headerHeight: 35,
          oddRowBGC: 'rgba(0, 44, 81, 0.8)',
          evenRowBGC: 'rgba(10, 29, 50, 0.8)'
        }
      }
    },
    created() {
      //setInterval(this.updateHandler,1000);
      setInterval(this.getEventList,10000);
    },
    methods: {
      getEventList() {
        getContent(this.test).then(response => {
          /* datav = response.data; */
          var arr = [];
          for(var i=0;i<response.data.length;i++){
              var arr1 = [];
              arr1.push(response.data[i].eventTitle);
              arr1.push(response.data[i].eventTime.substring(5,response.data[i].eventTime.length - 3));
              arr1.push(response.data[i].eventLocation);
              arr.push(arr1);
          }
          datav = arr;
          this.updateHandler();
        });
      },
      // 更新数据的示例方法
      updateHandler() {
        const {
          config
        } = this;
        this.config.data = datav;
        this.config = { ...this.config
        }
      }
    }
  }
</script>

<style scoped>
  .left-chart-3 {
    width: 100%;
    height: 100%;
    display: flex;
    flex-grow: 0;
    flex-direction: column;
  }

  .lc1-header {
    text-align: center;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 25px;
    margin-top: 10px;
    margin-bottom: 5px;
    color: white;
  }

  #scroll-board {
    width: 90%;
    margin-left: 5%;
    height: 70%;
    overflow: hidden;
  }
</style>
