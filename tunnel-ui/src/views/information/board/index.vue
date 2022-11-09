<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-27 09:52:13
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-08 10:04:52
 * @FilePath: \tunnel-ui\src\views\information\board\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="container" style="background-color: #004375; height: 100%">
    <el-row
      :gutter="20"
      style="height: 100%; margin-left: 0px; margin-right: 0px"
    >
      <el-col :span="4" style="background-color: white">
        <p class="bigTitle">情报板列表</p>
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="所属单位">
            <el-select v-model="form.region" placeholder="请选择所属单位">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属机构">
            <el-select v-model="form.region" placeholder="请选择所属机构">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属路段">
            <el-select v-model="form.region" placeholder="请选择所属路段">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设备类型">
            <el-select v-model="form.region" placeholder="请选择设备类型">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="分辨率">
            <el-select v-model="form.region" placeholder="请选择分辨率">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label-width="0">
            <el-checkbox
              style="width: 100%"
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
              >全选</el-checkbox
            >
            <div style="margin: 15px 0"></div>
            <el-checkbox-group
              class="checkbox"
              v-model="checkedCities"
              @change="handleCheckedCitiesChange"
            >
              <el-checkbox v-for="city in cities" :label="city" :key="city">{{
                city
              }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">确认</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col
        :span="10"
        style="background-color: white; border-left: 1px solid #f3f3f3"
      >
        <p class="bigTitle">待下发信息</p>
        <div class="contentBox">
          <div
            v-for="(item, index) in contentList"
            :key="index"
            class="listBox"
          >
            <div class="indexBox">
              <i
                class="el-icon-caret-top"
                size="18"
                @click="moveTop(index)"
              ></i>
              <i class="el-icon-caret-bottom" @click="moveBottom"></i>
            </div>
            <div
              class="con"
              :style="{ 'font-size': item.size, color: item.color }"
            >
              {{ item.content }}
            </div>
            <div class="menuBox">
              <i class="el-icon-d-arrow-right"></i>
              <i class="el-icon-edit-outline"></i>
              <i class="el-icon-close"></i>
            </div>
          </div>
          <div class="controlBox">
            <el-button @click.native="openDialogVisible">添加信息</el-button>
            <el-button type="primary">发布信息</el-button>
          </div>
        </div>
      </el-col>
      <el-col
        :span="10"
        style="background-color: white; border-left: 1px solid #f3f3f3"
      >
        <p class="bigTitle">信息模板</p>
        <div class="templateBox">
          <el-collapse v-model="activeNames" @change="handleChange" accordion>
            <el-collapse-item
              v-for="(item, index) in templateList"
              :key="index"
              :title="item.name"
              :name="item.id"
            >
              <div
                v-for="(itm, indx) in item.list"
                :key="indx"
                class="con"
                :style="{ 'font-size': itm.size, color: itm.color }"
              >
                <div class="templateTitle">{{ itm.content }}</div>
                <div class="menuBox">
                  <i class="el-icon-d-arrow-right"></i>
                  <i class="el-icon-edit-outline"></i>
                  <i class="el-icon-close"></i>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
          <div class="controlBox">
            <el-button type="primary">添加模板</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
    <addinfo ref="addinfo"></addinfo>
  </div>
</template>
<script>
import addinfo from "./addinfo";
const cityOptions = ["上海", "北京", "广州", "深圳"];
export default {
  name: "Device",
  components: {
    addinfo,
  },
  data() {
    return {
      dialogVisible: false,
      tunnelList: [],
      activeNames: "1",
      form: { region: "" },
      checkAll: false,
      checkedCities: [],
      cities: cityOptions,
      isIndeterminate: true,
      contentList: [
        { content: "日照服务区可以做核酸", color: "red", size: "20" },
        { content: "日照服务区可以做核酸", color: "red", size: "20" },
      ],
      templateList: [
        {
          id: "1",
          name: "默认模板",
          list: [
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
          ],
        },
        {
          id: "2",
          name: "日常通用",
          list: [
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
            { content: "日照服务区可以做核酸", color: "red", size: "20" },
          ],
        },
      ],
    };
  },
  created() {},
  methods: {
    // 打开添加信息弹窗
    openDialogVisible() {
      this.$refs.addinfo.init();
    },
    handleChange(val) {
      console.log(val);
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? cityOptions : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  .el-col {
    height: 100%;
    .bigTitle {
      padding: 15px 0;
      border-bottom: 1px solid #f3f3f3;
      margin-bottom: 20px;
    }
    .contentBox {
      width: 100%;
      .listBox {
        height: 75px;
        display: grid;
        grid-template-columns: 3% auto 10%;
        align-content: center;
        column-gap: 20px; //左右
        row-gap: 40px; //上下
        margin-bottom: 25px;
        .indexBox {
          display: flex;
          flex-flow: column;
          align-items: center;
          justify-content: space-evenly;
          i {
            font-size: 20px;
          }
        }
        .con {
          border: 1px solid #f3f3f3;
          height: 75px;
          line-height: 75px;
          text-align: center;
        }
        .menuBox {
          display: flex;
          align-items: center;
          i {
            font-size: 24px;
            color: #666;
          }
        }
      }
      .controlBox {
        display: flex;
        justify-content: center;
      }
    }
    .templateBox {
      width: 100%;
      .con {
        height: 75px;
        display: grid;
        grid-template-columns: auto 10%;
        align-content: center;
        column-gap: 20px; //左右
        row-gap: 40px; //上下
        margin-bottom: 25px;
        .templateTitle {
          height: 75px;
          line-height: 75px;
          text-align: center;
          border: 1px solid #f3f3f3;
        }
        .menuBox {
          display: flex;
          align-items: center;
          i {
            font-size: 24px;
            color: #666;
          }
        }
      }
      .controlBox {
        margin-top: 20px;
        display: flex;
        justify-content: center;
      }
    }
  }
  .checkbox {
    label {
      width: 100%;
      padding: 10px 0;
      box-sizing: border-box;
    }
  }
}
</style>