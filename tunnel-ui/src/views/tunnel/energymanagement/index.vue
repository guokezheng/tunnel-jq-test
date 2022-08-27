<template>
  <div class="app-container">
    <el-button type="primary" icon="el-icon-plus" size="mini" @click="" v-hasPermi="['system:material:add']">能源管理</el-button>

    <el-row style="width: 80%;float:right;">
      <div class="block" style="margin-left: 800px;">
        <span style="margin: 0px;"> 选择日期:</span>
              <el-date-picker
                v-model="value1"
                style="width: 240px;height:29px;"

                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
        </div>
        
        <el-row :gutter="16" style="margin-top: 30px;">
            <el-col :xs="72" :sm="72" :lg="24">
              <div class="chart-wrapper">
                 <bar-chart />
              </div>
            </el-col>
        </el-row>
    </el-row>



    <el-row style="width: 20%;float:left;">
      <div class="custom-tree-container">
        <div class="block">
          <el-tree
            :data="data"
            show-checkbox
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            :render-content="renderContent">
          </el-tree>
        </div>
       </div>
    </el-row>
  </div>
</template>

<script>
import BarChart from '../../tunnel/energymanagement/BarChart'
  let id = 1000;
export default {
/* name: "energymanagement", */
  components: {
      BarChart
    },
     data() {
          const data = [{
            id: 1,
            label: '全部隧道',
            children: [{
              id: 4,
              label: '济南隧道',
              children: [{
                id: 9,
                label: '灯光'
              }, {
                id: 10,
                label: '车指灯'
              },{
                id: 11,
                label: '风机'
              },{
                id: 12,
                label: '洞内亮度'
              },{
                id: 13,
                label: '洞外亮度'
              }]
            },{
              id: 7,
              label: '乐疃隧道',
              children: [{
                id: 9,
                label: '灯光'
              }, {
                id: 10,
                label: '车指灯'
             },{
               id: 11,
               label: '风机'
             },{
               id: 12,
               label: '洞内亮度'
             },{
               id: 13,
               label: '洞外亮度'
             }]
            },{
              id: 7,
              label: '佛羊岭隧道', children: [{
                id: 9,
                label: '灯光'
              }, {
                id: 10,
                label: '车指灯'
              },{
                id: 11,
                label: '风机'
              },{
                id: 12,
                label: '洞内亮度'
              },{
                id: 13,
                label: '洞外亮度'
              }]
            }]
          }/* , {
            id: 2,
            label: '一级 2',
            children: [{
              id: 5,
              label: '二级 2-1'
            }, {
              id: 6,
              label: '二级 2-2'
            }]
          } , {
            id: 3,
            label: '一级 3',
            children: [{
              id: 7,
              label: '二级 3-1'
            }, {
              id: 8,
              label: '二级 3-2'
            }]
          } */];
          return {
             pickerOptions: {
                      shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                          const end = new Date();
                          const start = new Date();
                          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                          picker.$emit('pick', [start, end]);
                        }
                      }, {
                        text: '最近一个月',
                        onClick(picker) {
                          const end = new Date();
                          const start = new Date();
                          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                          picker.$emit('pick', [start, end]);
                        }
                      }, {
                        text: '最近三个月',
                        onClick(picker) {
                          const end = new Date();
                          const start = new Date();
                          start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                          picker.$emit('pick', [start, end]);
                        }
                      }]
                    },
                    value1: '',
                    value2: '',
            // 日期范围
            dateRange: [],
            data: JSON.parse(JSON.stringify(data)),
            data: JSON.parse(JSON.stringify(data))
          }
        },

        methods: {
          append(data) {
            const newChild = { id: id++, label: 'testtest', children: [] };
            if (!data.children) {
              this.$set(data, 'children', []);
            }
            data.children.push(newChild);
          },

          remove(node, data) {
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            children.splice(index, 1);
          },
        }
      };
</script>
<style lang="scss" scoped>
.pagination-container{
  width: 40%;
  float: right;
}
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
