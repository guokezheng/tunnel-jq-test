/* 设备图标 */
/* url:图标地址
 * type:图标类型
 * explain：图标含义
 * */
let icon = [{
  url: require('@/assets/logo/equipment_log/chezhi_buliang.png'),
  type: "车道指示器",
  explain: "当前方向指示器关闭或离线"
}, //0车指
  {
    url: require('@/assets/logo/equipment_log/chezhi_jinxing.png'),
    type: "车道指示器",
    explain: "当前方向禁行"
  },
  {
    url: require('@/assets/logo/equipment_log/chezhi_tongxing.png'),
    type: "车道指示器",
    explain: "当前方向通行"
  },
  {
    url: require('@/assets/logo/equipment_log/chezhi_zuozhuan.png'),
    type: "车道指示器",
    explain: "当前方向允许左转"
  },
  {
    url: require('@/assets/logo/equipment_log/fengji_lixian.png'),
    type: "风机",
    explain: "离线"
  }, //4
  {
    url: require('@/assets/logo/equipment_log/fengji_fanzhuanqidong.gif'),
    type: "风机",
    explain: "反向转动"
  }, //5
  {
    url: require('@/assets/logo/equipment_log/fengji_zhengzhuanqidong.gif'),
    type: "风机",
    explain: "正向转动"
  }, //6
  {
    url: require('@/assets/logo/equipment_log/fengji_zidong.png'),
    type: "风机",
    explain: "自然转动（关闭）"
  }, //7
  {
    url: require('@/assets/logo/equipment_log/fengji_guzhang.png'),
    type: "风机",
    explain: "故障"
  }, //4
  {
    url: require('@/assets/logo/equipment_log/xinhaodeng_lixian.png'),
    type: "交通信号灯",
    explain: "离线"
  }, //8 红绿灯
  {
    url: require('@/assets/logo/equipment_log/xinhaodeng_hong.png'),
    type: "交通信号灯",
    explain: "红灯"
  }, //9
  {
    url: require('@/assets/logo/equipment_log/xinhaodeng_huang.png'),
    type: "交通信号灯",
    explain: "黄灯"
  }, //10
  {
    url: require('@/assets/logo/equipment_log/xinhaodeng_lv.png'),
    type: "交通信号灯",
    explain: "绿灯"
  }, //11
  {
    url: require('@/assets/logo/equipment_log/chezhi_zuozhuan.png'),
    type: "交通信号灯",
    explain: "左转"
  }, //12

  {
    url: require('@/assets/logo/equipment_log/juanlianmen_bankai.png'),
    type: "卷帘门",
    explain: "停止"
  }, //13 卷帘门
  {
    url: require('@/assets/logo/equipment_log/juanlianmen_guanbi.png'),
    type: "卷帘门",
    explain: "下降"
  }, //14
  {
    url: require('@/assets/logo/equipment_log/juanlianmen_quankai.png'),
    type: "卷帘门",
    explain: "上升"
  }, //15
  {
    url: require('@/assets/logo/equipment_log/juanlianmen_guzhang.png'),
    type: "卷帘门",
    explain: "卷帘门故障或离线"
  }, //16
 {
    url: require('@/assets/logo/equipment_log/juanlianmen_lixian.png'),
    type: "卷帘门",
    explain: "离线"
  }, //13 卷帘门
  {
    url: require('@/assets/logo/equipment_log/light_jiaqiang_buliang.png'),
    type: "加强照明",
    explain: "加强照明灯关闭或离线"
  }, //17
  {
    url: require('@/assets/logo/equipment_log/light_jiaqiang_liang.png'),
    type: "加强照明",
    explain: "加强照明灯开启"
  }, //18
  {
    url: require('@/assets/logo/equipment_log/light_jiaqiang_guzhang.png'),
    type: "加强照明",
    explain: "加强照明灯故障"
  }, //18
  {
    url: require('@/assets/logo/equipment_log/light_jiben_buliang.png'),
    type: "基本照明",
    explain: "基本照明灯关闭或离线"
  }, //19
  {
    url: require('@/assets/logo/equipment_log/light_jiben_liang.png'),
    type: "基本照明",
    explain: "基本照明灯开启"
  }, //20
  {
    url: require('@/assets/logo/equipment_log/light_jiben_guzhang.png'),
    type: "基本照明",
    explain: "基本照明灯故障"
  }, //20
  {
    url: require('@/assets/logo/equipment_log/light_yindao_buliang.png'),
    type: "引道照明",
    explain: "引道照明灯关闭或离线"
  }, //21
  {
    url: require('@/assets/logo/equipment_log/light_yindao_liang.png'),
    type: "引道照明",
    explain: "引道照明灯在线"
  }, //22
  {
    url: require('@/assets/logo/equipment_log/light_yindao_guzhang.png'),
    type: "引道照明",
    explain: "引道照明灯故障"
  }, //22
  {
    url: require('@/assets/logo/equipment_log/qiangji_zaixian.png'),
    type: "摄像机（枪机）",
    explain: "正常"
  }, //23 摄像机shexiangji_guzhang.png
  {
    url: require('@/assets/logo/equipment_log/qiangji_lixian.png'),
    type: "摄像机（枪机）",
    explain: "离线"
  }, //24
  {
    url: require('@/assets/logo/equipment_log/qiangji_guzhang.png'),
    type: "摄像机（枪机）",
    explain: "故障"
  }, //24
  {
    url: require('@/assets/logo/equipment_log/qiuji_zaixian.png'),
    type: "摄像机（球机）",
    explain: "正常"
  }, //24
  {
    url: require('@/assets/logo/equipment_log/qiuji_lixian.png'),
    type: "摄像机（球机）",
    explain: "离线"
  }, //24
  {
    url: require('@/assets/logo/equipment_log/qiuji_guzhang.png'),
    type: "摄像机（球机）",
    explain: "故障"
  }, //24
  {
    url: require('@/assets/logo/equipment_log/shuibeng_zaixian.png'),
    type: "水泵",
    explain: "正常"
  }, //26
  {
    url: require('@/assets/logo/equipment_log/shuibeng_lixian.png'),
    type: "水泵",
    explain: "离线"
  }, //25水泵
{
    url: require('@/assets/logo/equipment_log/shuibeng_guzhang.png'),
    type: "水泵",
    explain: "故障"
  }, //25水泵
  // {
  //   url: require('@/assets/logo/equipment_log/railing.png'),
  //   type: "升降杆",
  //   explain: "正常"
  // }, //27
  {
    url: require('@/assets/logo/equipment_log/yalibiao_zaixian.png'),
    type: "消防管道压力表",
    explain: "正常"
  }, //28
  {
    url: require('@/assets/logo/equipment_log/yalibiao_lixian.png'),
    type: "消防管道压力表",
    explain: "离线"
  }, //29
  {
    url: require('@/assets/logo/equipment_log/yalibiao_guzhang.png'),
    type: "消防管道压力表",
    explain: "故障"
  }, //29
  {
    url: require('@/assets/logo/equipment_log/robot_zaixian.png'),
    type: "巡检机器人",
    explain: "正常"
  }, //30
  {
    url: require('@/assets/logo/equipment_log/robot_lixian.png'),
    type: "巡检机器人",
    explain: "离线"
  }, //31
  {
    url: require('@/assets/logo/equipment_log/robot_guzhang.png'),
    type: "巡检机器人",
    explain: "故障"
  }, //31
  {
    url: require('@/assets/logo/equipment_log/qingbaoban.png'),
    type: "情报板",
    explain: "正常"
  },//32
  {
    url: require('@/assets/logo/equipment_log/weibochejian_zaixian.png'),
    type: "微波车检",
    explain: "正常"
  },//33
  {
    url: require('@/assets/logo/equipment_log/weibochejian_lixian.png'),
    type: "微波车检",
    explain: "离线"
  },//34
  {
    url: require('@/assets/logo/equipment_log/weibochejian_guzhang.png'),
    type: "微波车检",
    explain: "故障"
  },//34
  {
    url: require('@/assets/logo/equipment_log/daolujiebing_zaixian.png'),
    type: "路面温度检测器",
    explain: "正常"
  },//35
  {
    url: require('@/assets/logo/equipment_log/daolujiebing_lixian.png'),
    type: "路面温度检测器",
    explain: "离线"
  },//36
  {
    url: require('@/assets/logo/equipment_log/daolujiebing_guzhang.png'),
    type: "路面温度检测器",
    explain: "故障"
  },//36
  // {
  //   url: require('@/assets/logo/equipment_log/fengxiang_zuo.png'),
  //   type: "风向检测",
  //   explain: "正向"
  // },//37
  {
    url: require('@/assets/logo/equipment_log/fengsu_zaixian.png'),
    type: "风速风向检测",
    explain: "正常"
  },//38
  {
    url: require('@/assets/logo/equipment_log/fengsu_lixian.png'),
    type: "风速风向检测",
    explain: "离线"
  },//39
  {
    url: require('@/assets/logo/equipment_log/fengsu_guzhang.png'),
    type: "风速风向检测",
    explain: "故障"
  },//39
  {
    url: require('@/assets/logo/equipment_log/covi_zaixian.png'),
    type: "CO/VI检测",
    explain: "正常"
  },//40
  {
    url: require('@/assets/logo/equipment_log/covi_lixian.png'),
    type: "CO/VI检测",
    explain: "离线"
  },//40
  {
    url: require('@/assets/logo/equipment_log/covi_guzhang.png'),
    type: "CO/VI检测",
    explain: "故障"
  },//40
  // {
  //   url: require('@/assets/logo/equipment_log/VI_zaixian.png'),
  //   type: "能见度检测",
  //   explain: "正常"
  // },//41
  {
    url: require('@/assets/logo/equipment_log/liangdu_zaixian.png'),
    type: "亮度检测器",
    explain: "正常"
  },//42
  {
    url: require('@/assets/logo/equipment_log/liangdu_lixian.png'),
    type: "亮度检测器",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/liangdu_guzhang.png'),
    type: "亮度检测器",
    explain: "故障"
  },//43
  {
    url: require('@/assets/logo/equipment_log/biandiansuo_zaixian.png'),
    type: "变电所",
    explain: "正常"
  },//43
  {
    url: require('@/assets/logo/equipment_log/biandiansuo_lixian.png'),
    type: "变电所",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/biandiansuo_guzhang.png'),
    type: "变电所",
    explain: "故障"
  },//43
  {
    url: require('@/assets/logo/equipment_log/dianhua_zaixian.png'),
    type: "紧急电话",
    explain: "正常"
  },//43
  {
    url: require('@/assets/logo/equipment_log/dianhua_lixian.png'),
    type: "紧急电话",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/dianhua_guzhang.png'),
    type: "紧急电话",
    explain: "故障"
  },//43
  {
    url: require('@/assets/logo/equipment_log/PLC_zaixian.png'),
    type: "PLC",
    explain: "正常"
  },//43
  {
    url: require('@/assets/logo/equipment_log/PLC_lixian.png'),
    type: "PLC",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/PLC_guzhang.png'),
    type: "PLC",
    explain: "故障"
  },//43
  {
    url: require('@/assets/logo/equipment_log/shoubao_zaixian.png'),
    type: "手动报警",
    explain: "正常"
  },//43
  {
    url: require('@/assets/logo/equipment_log/shoubao_lixian.png'),
    type: "手动报警",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/shoubao_guzhang.png'),
    type: "手动报警",
    explain: "故障"
  },//43
  {
    url: require('@/assets/logo/equipment_log/yingjideng_zaixian.png'),
    type: "应急灯",
    explain: "正常"
  },//43
  {
    url: require('@/assets/logo/equipment_log/yingjideng_lixian.png'),
    type: "应急灯",
    explain: "离线"
  },//43
  {
    url: require('@/assets/logo/equipment_log/yingjideng_guzhang.png'),
    type: "应急灯",
    explain: "故障"
  },//43
]

/* 逆向车道 */
let laneImage = [
  {
    id: "1",
    name: "3车道-短",
    url: require('@/assets/image/lane/3duan.png'),
    width: 1620,
    num: 3,
    direction: 0
  },
  {
    id: "2",
    name: "3车道-长",
    url: require('@/assets/image/lane/chaochang.png'),
    width: 4220,
    num: 3,
    direction: 0
  },
  {
    id: "3",
    name: "2车道-短",
    url: require('@/assets/image/lane/fenghuangshan.png'),
    width: 1620,
    num: 2,
    direction: 0
  },
  {
    id: "4",
    name: "马家峪隧道",
    url: require('@/assets/image/lane/mjy.jpg'),
    width: 2200,
    num: 3,
    direction: 0
  },
]

export {
  icon,
  laneImage
}
