/* 设备图标 */
/* url:图标地址
 * type:图标类型
 * explain：图标含义
 * */
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
    width: 2270,
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
    url: require('@/assets/image/lane/hsd.jpg'),
    width: 1620,
    num: 3,
    direction: 0
  },
  {
    id: "7",
    name: "金家楼隧道",
    url: require('@/assets/image/lane/jjl.jpg'),
    width: 1620,
    num: 3,
    direction: 0
  },
  {
    id: "8",
    name: "马鞍山隧道",
    url: require('@/assets/image/lane/mas.jpg'),
    width: 2270,
    num: 3,
    direction: 0
  },
  // 小于1300米的隧道 用1300px小车跑的长度加320光伏棚洞的px
  // 大于1300小于2600的隧道 用1300*1.5+320 等于2270
  // 大于2600的隧道 用1300*2+320 等于2920
]

export {
  laneImage
}
