/* 平台地图 */
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
    url: require('@/assets/image/lane/mjy.png'),
    width: 2378,
    num: 3,
    direction: 0
  },
  {
    id: "5",
    name: "凤凰山隧道",
    url: require('@/assets/image/lane/fhs.png'),
    width: 1620,
    num: 2,
    direction: 0
  },
  {
    id: "6",
    name: "杭山东隧道",
    url: require('@/assets/image/lane/hsd.png'),
    width: 1728,
    num: 3,
    direction: 0
  },
  {
    id: "7",
    name: "金家楼隧道",
    url: require('@/assets/image/lane/jjl.png'),
    width: 1728,
    num: 3,
    direction: 0
  },
  {
    id: "8",
    name: "马鞍山隧道",
    url: require('@/assets/image/lane/mas.png'),
    width: 2378,
    num: 3,
    direction: 0
  },
  {
    id: "9",
    name: "北甲峪隧道",
    url: require('@/assets/image/lane/bjy.png'),
    width: 2378,
    num: 3,
    direction: 0
  },
  {
    id: "10",
    name: "盘顶山隧道",
    url: require('@/assets/image/lane/pds.png'),
    width: 3678,
    num: 3,
    direction: 0
  },
  {
    id: "11",
    name: "青风岭隧道",
    url: require('@/assets/image/lane/qfl.png'),
    width: 2378,
    num: 3,
    direction: 0
  },
  {
    id: "12",
    name: "双子山隧道",
    url: require('@/assets/image/lane/szs.png'),
    width: 2378,
    num: 3,
    direction: 0
  },
  {
    id: "13",
    name: "万昌溜隧道",
    url: require('@/assets/image/lane/wcl.png'),
    width: 3028,
    num: 3,
    direction: 0
  },
  {
    id: "14",
    name: "仰天山隧道",
    url: require('@/assets/image/lane/yts.png'),
    width: 1728,
    num: 3,
    direction: 0
  },
  
  // 小于1300米的隧道 用1300px小车跑的长度加214*2光伏棚洞的px 等于1728
  // 大于1300小于2600的隧道 用1300*1.5+214*2 等于2378
  // 大于2600小于2900的隧道 用1300*2+214*2 等于3028
  // 大于3900的隧道 用1300*2.5+214*2 等于3678
]

export {
  laneImage
}
