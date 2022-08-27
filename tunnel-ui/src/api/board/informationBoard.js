import request from '@/utils/request'

// 查询设备列列表
export function listDevice(query) {
  return request({
    url: '/information/list',
    method: 'get',
    params: query
  })
}

// 查询设备列详细
export function getDevice(deviceId) {
  return request({
    url: '/information/' + deviceId,
    method: 'get'
  })
}

// 新增设备列
export function addDevice(data) {
  return request({
    url: '/information',
    method: 'post',
    data: data
  })
}

// 修改设备列
export function updateDevice(data) {
  return request({
    url: '/information',
    method: 'put',
    data: data
  })
}

// 删除设备列
export function delDevice(deviceId) {
  return request({
    url: '/information/' + deviceId,
    method: 'delete'
  })
}

export function getBoardEditInfo(deviceId) {
  return request({
    url: '/parser/board/getBoardEditInfo',
    method: 'get',
    params: deviceId
  })
}

export function getBoardContent(deviceId) {
  return request({
    url: '/parser/board/getBoardContent',
    method: 'get',
    params: deviceId
  })
}

export function getBoardInfo(deviceId) {
  return request({
    url: '/parser/board/getBoardInfo',
    method: 'get',
    params: deviceId
  })
}

/* 获取情报板亮度 */
export function readBoardLightInfo(deviceId) {
  var data = {
    deviceId:deviceId
  }
  return request({
    url: '/parser/board/readBoardLightInfo',
    method: 'get',
    params: data
  })
}

export function uploadBoardEditInfo(deviceId,protocolType,parameters) {
  var data = {
    deviceId:deviceId,
    protocolType:protocolType,
    parameters:encodeURI(parameters)
  }
  return request({
    url: '/parser/board/uploadBoardEditInfo',
    method: 'get',
    params: data
  })
}

export function HashMap() {
  //定义长度
      var length = 0;
      //创建一个对象
      var obj = new Object();

      /**
      * 判断Map是否为空
      */
      this.isEmpty = function(){
          return length == 0;
      };

      /**
      * 判断对象中是否包含给定Key
      */
      this.containsKey=function(key){
          return (key in obj);
      };

      /**
      * 判断对象中是否包含给定的Value
      */
      this.containsValue=function(value){
          for(var key in obj){
              if(obj[key] == value){
                  return true;
              }
          }
          return false;
      };

      /**
      *向map中添加数据
      */
      this.put=function(key,value){
          if(!this.containsKey(key)){
              length++;
          }
          obj[key] = value;
      };

      /**
      * 根据给定的Key获得Value
      */
      this.get=function(key){
          return this.containsKey(key)?obj[key]:null;
      };

      /**
      * 根据给定的Key删除一个值
      */
      this.remove=function(key){
          if(this.containsKey(key)&&(delete obj[key])){
              length--;
          }
      };

      /**
      * 获得Map中的所有Value
      */
      this.values=function(){
          var _values= new Array();
          for(var key in obj){
              _values.push(obj[key]);
          }
          return _values;
      };

      /**
      * 获得Map中的所有Key
      */
      this.keySet=function(){
          var _keys = new Array();
          for(var key in obj){
              _keys.push(key);
          }
          return _keys;
      };

      /**
      * 获得Map的长度
      */
      this.size = function(){
          return length;
      };

      /**
      * 清空Map
      */
      this.clear = function(){
          length = 0;
          obj = new Object();
      };
}
