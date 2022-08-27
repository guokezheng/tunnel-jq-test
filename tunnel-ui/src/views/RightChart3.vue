<template>
  <div class="left-chart-3">
    <div class="lc1-header">情报板信息</div>
    <div id="scroll-board">
      <dv-scroll-board :config="config" style="height: 20vh;"/>
    </div>
  </div>

</template>

<script>
import request from '@/utils/request'
var datav;
function getBoardContent() {
        return request({
            url: '/information/bigscreenBoardList',
            method: 'get'
        })
    }
    export default {
        name: 'ScrollBoard',
        data () {
            return {
                config: {
                    header: ['桩号', '信息'],
                    data: [
                        ['K19+120', '隧道限速80m/s'],
                        ['K19+140', '前方隧道，请注意保持车距意保持车距'],
                        ['K19+160', '道路千万条，安全第一条'],
                        ['K19+180', '隧道限速80m/s'],
                        ['K20+120', '隧道限速80m/s']
                    ],
                    index: false,
                    align: ['center'],
                    rowNum: 4,
                    columnWidth: [100, ],
                    headerBGC: '#1981f6',
                    headerHeight: 35,
                    oddRowBGC: 'rgba(0, 44, 81, 0.8)',
                    evenRowBGC: 'rgba(10, 29, 50, 0.8)'
                }
            }
        },
        created() {
           /* setInterval(this.updateHandler,1000);
            setInterval(this.getBoardList,1000); */
          //  this.getBoardList();
          //  this.updateHandler();
        },
        methods: {
        getBoardList(){
          getBoardContent().then(response => {
             // datav = response;
          });
        },
        // 更新数据的示例方法
        updateHandler() {
        const { config } = this;
            this.config.data=datav;
            this.config = { ...this.config }
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
    flex-direction: column;}

  .lc1-header {
    text-align: center;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 25px;
    margin-top: 10px;
    margin-bottom: 5px;
    color:white;
  }
  #scroll-board {
    width: 90%;
    margin-left: 5%;
    height: 70%;
    overflow: hidden;
  }
</style>
