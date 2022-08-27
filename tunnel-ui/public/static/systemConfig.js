/*
 * @Author: zbguopeng 18053314396@163.com
 * @Date: 2022-06-23 11:07:06
 * @LastEditors: zbguopeng 18053314396@163.com
 * @LastEditTime: 2022-06-27 17:20:16
 * @FilePath: \tunnel-ui\public\static\systemConfig.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
/**
 * @author YangChao
 * 0 若依管理系统
 * 1 雅典娜

名称用到得文件
 Logo.vue
 login.vue
 register.vue
 index.html

 * 每次更换 1.系统名称 2.系统页面 3.navBarShow显隐  只需要更改 systemType即可
 * navBarShow0==>bigScreen-大屏(暂时没有)---frontPage-前台(暂时没有)---doc-文档
 */
const systemConfig = {

  systemType: 1,

  title0: '管理系统',
  url0: 'ry',
  navBarShow0: {bigScreen: 'false', frontPage: 'true', doc: 'true',weather:'true',breadcrumb:'true'},
  title1: '智慧隧道综合管控平台',
  url1: "athena",
  navBarShow1: {bigScreen: 'false', frontPage: 'false', doc: 'false',weather:'false',breadcrumb:'true'},


  title: function (type) {
    if (type === 0) {
      return this.title0
    } else if (type === 1) {
      return this.title1
    }
  },
  url: function (type) {
    if (type === 0) {
      return this.url0
    } else if (type === 1) {
      return this.url1
    }
  },

  navBarShow: function (type) {
    if (type === 0) {
      return this.navBarShow0
    } else if (type === 1) {
      return this.navBarShow1
    }
  }
}

