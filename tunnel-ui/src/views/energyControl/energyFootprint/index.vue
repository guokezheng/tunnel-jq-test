<!-- 能耗足迹 -->
<template>
  <div
    class="app-container"
    v-loading.fullscreen.lock="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <el-row :gutter="20">
      <!-- 左侧下拉 -->
      <el-col :span="4">
        <el-card class="my-card-height left_tree" shadow="never">
          <site-tree
            @nodeCheck="handleCheckChange"
            @defaultCheck="defaultCheckLoop"
            :show_checkbox="true"
            :default_check_first="true"
            height="calc(100vh - 230px)"
          ></site-tree>
        </el-card>
      </el-col>
      <!-- 右侧内容 -->
      <el-col :span="20">
        <div class="my-card-height">
          <!-- tab选择器 -->
          <div class="right_tabs">
            <p>能耗足迹</p>
            <div style="display: flex; align-items: center">
              <el-select
                v-model="tabType"
                size="mini"
                style="width: 80px; margin-right: 10px"
                @change="changeTab"
              >
                <el-option label="日报" value="day"></el-option>
                <el-option label="月报" value="month"></el-option>
                <el-option label="年报" value="year"></el-option>
              </el-select>
              <el-date-picker
                v-if="tabType === 'day'"
                size="mini"
                class="date-picker"
                v-model="base_date"
                align="center"
                type="date"
                :clearable="false"
                :picker-options="setDateRange"
              ></el-date-picker>
              <el-date-picker
                v-if="tabType === 'month'"
                size="mini"
                class="date-picker"
                v-model="base_date"
                format="yyyy-MM"
                align="center"
                type="month"
                :clearable="false"
                :picker-options="setDateRange"
              ></el-date-picker>
              <el-date-picker
                v-if="tabType === 'year'"
                size="mini"
                class="date-picker"
                v-model="base_date"
                format="yyyy"
                align="center"
                type="year"
                :clearable="false"
                :picker-options="setDateRange"
              ></el-date-picker>
              <el-button size="mini" class="search" @click="getData"
                >搜索</el-button
              >
            </div>
          </div>
          <!-- 桑基图 -->
          <div class="echart">
            <div id="chart" style="width: 100%; height: 100%"></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
  
  <script>
import * as echarts from "echarts";
import SiteTree from "@/views/components/siteTree/index3.vue";
  import {getEnergyTrackList} from "@/api/energy/api";

export default {
  name: "Online",
  components: { SiteTree },
  data() {
    return {
      setDateRange: {
        disabledDate: (time) => {
          // 禁用今天之后的日期【当前天可选】
          return time.getTime() > Date.now();
        },
      },
      loopIds: [], //选中的站点列表id
      loading: false, // 遮罩层
      myChart: "",
      tabType: "day",
      base_date: new Date(), //选择的日期 默认当前时间
      queryParams: {
        // 机构查询参数
        deptCodeList: null,
        baseTime: null,
        type: null,
      },
      linksData: [], //links数据
      namesData: [], //name数据
    };
  },
  methods: {
    /******站点******/
    //默认选中回路回调
    defaultCheckLoop(keys) {
      this.loopIds = keys;
      this.getData();
    },
    //节点选中状态发生变化时的回调
    handleCheckChange(data, checked) {
      this.loopIds = checked.checkedKeys; //选中回路的id
      this.getData();
    },
    // tab切换
    changeTab() {
      this.getData();
    },
    //获取数据
    async getData() {
      // console.log(this.loopIds)
      this.clearData();
      //校验参数
      if (this.loopIds.length === 0) {
        this.$message({
          showClose: true,
          message: "至少选择一条站点回路",
          type: "warning",
          duration: 1500,
        });
        return;
      }
      // this.loading = true
      // 参数
      this.queryParams.deptCodeList = this.loopIds
        .filter((e) => e != null)
        .join(",");
      this.queryParams.baseTime = this.parseTime(this.base_date);
      this.queryParams.type = this.tabType;

      const res = await getEnergyTrackList(this.queryParams)
      if (res.code === 200) {
        console.log(res)
        this.linksData = res.data
        this.loading = false
      }

      // 获取namesData
      let names = [];
      this.linksData.forEach((item) => {
        names.push(item.source);
        names.push(item.target);
      });
      names = Array.from(new Set(names)); //去重
      for (let i in names) {
        this.namesData.push({
          name: names[i],
        });
      }
      // console.log(this.namesData);

      this.openChart();
    },
    // 查询之前清空数据
    clearData() {
      this.linksData = [];
      this.namesData = [];
      if (this.myChart) {
        this.myChart.clear();
      }
    },
    //桑基图
    openChart() {
      this.$nextTick(function () {
        if (
          this.myChart != null &&
          this.myChart != "" &&
          this.myChart != undefined
        ) {
          // 销毁
          this.myChart.dispose();
        }
        this.myChart = echarts.init(document.getElementById("chart"));

        var option = {
          tooltip: {
            trigger: "item",
            formatter: function (data) {
              return data.name + " : " + data.value.toFixed(2);
            },
          },
          series: {
            type: "sankey",
            layout: "none",
            top: 50,
            left: "3%",
            right: "12%",
            nodeGap: 14,
            layoutIterations: 0, // 自动优化列表，尽量减少线的交叉，为0就是按照数据排列
            data: this.namesData, // 节点
            links: this.linksData, // 节点之间的连线
            draggable: false,
            emphasi: "inEdges", // 鼠标划上时高亮的节点和连线，allEdges表示鼠标划到节点上点亮节点上的连线及连线对应的节点
            levels: [
              {
                depth: 0,
                itemStyle: {
                  color: "#F27E7E",
                },
                lineStyle: {
                  color: "source",
                  opacity: 0.2,
                },
              },
              {
                depth: 1,
                lineStyle: {
                  color: "source",
                  opacity: 0.2,
                },
              },
              {
                depth: 2,
                lineStyle: {
                  color: "source",
                  opacity: 0.2,
                },
              },
              {
                depth: 3,
                label: {
                  fontSize: 12,
                },
              },
            ],
            label: {
              fontSize: 14,
              color: "#666",
            },
            itemStyle: {
              borderWidth: 0,
            },
          },
        };
        this.myChart.setOption(option);
      });
    },
  },
};
</script>
  <style lang="scss" scoped>
.app-container {
  height: calc(100vh - 130px);
  padding: 5px 20px;
  background: none;
  margin: 0px;
  ::v-deep .el-card__body {
    // padding: 0 0 15px 0 !important;
    padding: 6px !important;
  }
  // ::v-deep .el-scrollbar {
  //   // padding-left: 10px;
  //   margin: 0px 10px;
  // }
  // 隐藏滚动条
  // ::v-deep .el-scrollbar__wrap {
  //   margin-right: -20px !important;
  // }
  ::v-deep .el-tabs--border-card > .el-tabs__content {
    padding: 12px;
  }
}
.date-picker {
  width: 9rem;
}
.echart {
  width: 100%;
  height: calc(100% - 56px);
}
.my-card-height {
  .right_tabs {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    p {
      color: #fff;
      font-size: 20px;
      font-weight: 600;
      height: 100%;
      line-height: 36px;
    }
    .date-picker {
      width: 7vw;
    }
    //   .view {
    //     border-color: #3070BA;
    //     margin-left: 10px;
    //     background: rgba(48, 112, 186, 0.15);
    //     color: rgba(48, 112, 186, 1);
    //   }
    .search {
      margin-left: 10px;
      background: linear-gradient(180deg, #4cbfff, #259bff) !important;
      border: 1px solid #65bfff !important;
      color: white;
    }
    ::v-deep .el-input__inner {
      border: 1px solid #ececec;
      color: #3070ba;
      background: #f3f8fe;
    }
    ::v-deep .el-input__prefix {
      color: #3070ba;
    }
  }
}
::v-deep .el-card {
  border: none !important;
  border-radius: 2px;
}
</style>
  