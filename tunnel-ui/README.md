<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2023-02-24 08:36:42
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-02-24 08:41:34
 * @FilePath: \tunnel-ui\README.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<!--
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-12-16 15:10:19
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2023-02-24 08:30:24
 * @FilePath: \tunnel-ui\README.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
## 开发

```bash
# 克隆项目
git clone https://gitee.com/y_project/RuoYi-Vue

# 进入项目目录
cd ruoyi-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

#新增视频流播放和gis地图
npm install --save @vue/composition-api
npm install gis-map-tunnel@0.1.3
#新增swiper轮播图
npm install swiper@^5.4.5
#新增精确计算
npm install mathjs

# 新安装阿里云iconfont图标库，文件地址src\assets\icon\iconfont.css
#新增树形穿梭框
npm install --save el-tree-transfer

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod

# 新版gis地图
npm install gis-map-admin@0.9.2

# 新增 vue-resource 数据请求
npm install vue-resource --save

# 新增 视频 rtmp转码 
npm install vue-video-player -S
# 因为是播放rtmp格式流，需要安装 videojs-flash 插件，必须用npm安装！！！！！！
npm install video-flash --save
# 使用file-saver导出文件，下载Excel文件、下载图片、下载文本
npm install file-saver --save

```
