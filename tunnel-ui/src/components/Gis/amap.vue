<!--
 * @Author: zbguopeng 18053314396@163.com
 * @Date: 2022-06-23 11:07:03
 * @LastEditors: zbguopeng 18053314396@163.com
 * @LastEditTime: 2022-07-13 10:15:49
 * @FilePath: \tunnel-ui\src\components\Gis\amap.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div :id="container"></div>
</template>

<script>
/**
 * 高德地图组件
 */

import AMapLoader from '@amap/amap-jsapi-loader';

export default {
  name: 'amap',
  data() {
    return {
      container: 'container',
      gisScript: 'gis-script',
      key: '3b5479d9ad9f01d138fef5e70daed7bd',
      v: '2.0',
      map: undefined,
      zoom: 10 // 默认缩放级别
    }
  },
  created() {
  },
  mounted() {
    this.amapInit()
  },
  methods: {
    amapInit() {
      AMapLoader.load({
        key: this.key,            // 申请好的Web端开发者Key，首次调用 load 时必填
        version: this.v,          // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: [],              // 需要使用的的插件列表，如比例尺'AMap.Scale'等
        AMapUI: {                 // 是否加载 AMapUI，缺省不加载
          version: '1.1',         // AMapUI 缺省 1.1
          plugins:[],             // 需要加载的 AMapUI ui插件
        },
        Loca:{                    // 是否加载 Loca， 缺省不加载
          version: '2.0'          // Loca 版本，缺省 1.3.2
        },
      }).then((AMap)=>{
        const map = new AMap.Map(this.container, {
          zoom: this.zoom,
          mapStyle: "amap://styles/darkblue"
        });
        this.$emit('ready', map, this)
      }).catch(e => {
        console.log(e);
      })
    }
  }
}
</script>

<style scoped>

</style>
