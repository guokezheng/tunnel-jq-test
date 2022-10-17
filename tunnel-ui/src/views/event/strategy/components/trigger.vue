<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-10 13:59:55
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-10-10 14:24:29
 * @FilePath: \tunnel-ui\src\views\event\strategy\components\trigger.vue
 * @Description: 控制策略触发器
-->
<template>
  <div>
    <el-form
      ref="form1"
      :model="triggers"
      :rules="formDataValidator"
      label-width="100px"
    >
      <el-form-item label="触发器">
        <el-select
          v-model="strategyForm.triggers.deviceTypeId"
          placeholder="请选择设备类型"
          style="width: 18%"
          @change="slectEqName()"
        >
          <el-option
            v-for="item in eqTypeList"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          >
          </el-option>
        </el-select>
        <el-select
          v-model="strategyForm.triggers.deviceId"
          placeholder="请选择设备名称"
          style="width: 18%; margin-left: 2%"
          @change="selectDataItem()"
        >
          <el-option
            v-for="item in deviceName"
            :key="item.eqId"
            :label="item.eqName"
            :value="item.eqId"
          >
          </el-option>
        </el-select>
        <el-select
          v-model="strategyForm.triggers.elementId"
          placeholder="请选择数据项"
          style="width: 18%; margin-left: 2%"
        >
          <el-option
            v-for="item in dataItem"
            :key="item.id"
            :label="item.itemName"
            :value="item.id"
          >
          </el-option>
        </el-select>
        <el-select
          v-model="strategyForm.triggers.comparePattern"
          style="width: 12%; margin-left: 2%"
        >
          <el-option
            v-for="item in symbol"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          >
          </el-option>
        </el-select>
        <el-input
          style="width: 18%; margin-left: 2%"
          v-model="strategyForm.triggers.compareValue"
          placeholder="请输入阈值"
        />
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      triggers: {
        deviceTypeId: "", //设备类型
        deviceId: "", //设备名称
        elementId: null, //数据项
        comparePattern: null, //
        compareValue: "", //阈值
      },
      formDataValidator: {
        deviceTypeId: [
          { required: true, message: "请输入设备类型", trigger: "blur" },
        ],
        deviceId: [
          { required: true, message: "请选择设备名称", trigger: "change" },
        ],
        elementId: [
          {
            type: "date",
            required: true,
            message: "请选择数据项",
            trigger: "change",
          },
        ],
        comparePattern: [
          {
            type: "date",
            required: true,
            message: "请选择时间",
            trigger: "change",
          },
        ],
        compareValue: [
          { required: true, message: "请输入阈值", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    init() {},
  },
};
</script>

<style>
</style>