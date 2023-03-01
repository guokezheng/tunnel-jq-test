
// var tokens = ['token1','token2','token3','token4','token5','token6','token7'] //所有视频tokens
// var vedioBoxs = ['h5sVideo1',"h5sVideo2", "h5sVideo3","h5sVideo4", "h5sVideo5", "h5sVideo6","h5sVideo7"] //电脑播放使用

var vedioBox = "h5sVideo1"
 var tokens = ['token1','d7b9','0f33', 'a003','78c3','72ab','cb28'
 ,'f9a5','c65a','df09','c71b'] //所有视频tokens
 var vedioBoxs = ['h5sVideo1'] //入出卡口相机
var v1,
vs = []

var conf = {
    videoid: "h5sVideo1",
    protocol: "http:", //'http:' or 'https:'
    // host: "106.120.201.126:14525", // 广州
    host: "10.3.16.4:8080",//济青隧道
    // host: "10.102.1.20:8801/",//济青隧道
    rootpath: "/", // '/' or window.location.pathname
    token: "token1",
    hlsver: "v1", //v1 is for ts, v2 is for fmp4
    session: "7f8086e9-d2dc-409b-98c5-3fd312a7ed5c", //济青隧道
    // session: "ea48e26f-69aa-495c-9c69-2f6e7aa92054"
}

Date.prototype.format = function(format) {
    /*
     * eg:format="yyyy-MM-dd hh:mm:ss.SSS";
     */
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S+" : this.getMilliseconds()
        // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
        - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
        var formatStr="";
        for(var i=1;i<=RegExp.$1.length;i++){
            formatStr+="0";
        }

        var replaceStr="";
        if(RegExp.$1.length == 1){
            replaceStr=o[k];
        }else{
            formatStr=formatStr+o[k];
            var index=("" + o[k]).length;
            formatStr=formatStr.substr(index);
            replaceStr=formatStr;
        }
            format = format.replace(RegExp.$1, replaceStr);
        }
    }
    return format;
}
function displayH5sVideo(index) {
    if (H5siOS() === true || H5sSafariBrowser() === true) {
        $("#" + vedioBox).prop("controls", true)
    }

    if (v1 != null) {
        v1.disconnect()
        v1 = null
    }
    conf.videoid = vedioBox
    conf.token = tokens[index]
    v1 = H5sPlayerCreate(conf)
    v1.connect()
    setTimeout(() => {}, 3000)
}
var pbconf = {
    begintime: '',
    endtime:  '',
    serverpb: 'true', //'true' or 'false' playback from h5stream record, default false
    autoplay: 'true', // 'true' or 'false' for playback autoplay
    showposter: 'false', //'true' or 'false' show poster
};
export function displayH5sVideoAll(token,id,startTime,endTime) {
    console.log(token,'============================================================')
    $.ajax({
    type: "POST", //提交方式 
    url: "http://10.3.16.4:8080/api/v1/Login?user=admin&password=baa70aa2e4c48a30549cc3abb429179b",
    success: function (result) {
        conf.session=result.strSession;
        if (H5siOS() === true || H5sSafariBrowser() === true) {
            $("#" + id).prop("controls", true)
        }
    
        if (v1 != null) {
            v1.disconnect()
            v1 = null
        }
        conf.videoid = id
        conf.token = token
        v1 = H5sPlayerCreate(conf)
        v1.connect()
        setTimeout(() => {}, 3000)
    }
})
}
export function stopVideoAll() {
    for (var i = 0; i < tokens.length; i++) {
        // $("#" + vedioBoxs[i])[0].pause()
        if (vs[i] != null) {
            vs[i].disconnect()
            delete vs[i]
            vs[i] = null
        }
    }
}
// $('.h5video_').click(function () {

//     var loading;
//     loading = layer.load(0, {
//         shade: false,
//     });

//     var vedioId = $(this).attr("id");
//     var index = vedioId.substr(vedioId.length - 1, 1);
//     if (index != '7') {
//         displayH5sVideoPop('h5sVideo7', parseInt(index));
//         setTimeout(function () {
//             layer.open({
//                 type: 1,
//                 title: false,
//                 closeBtn: 0,
//                 // area:["54.1%","61.8%"],
//                 area: ["877.2px", "573.4px"],
//                 // area: ["1022px", "668px"],
//                 offset: ['11%', '27%'],
//                 skin: 'layui-layer-nobg', //没有背景色
//                 shadeClose: true,
//                 content: $('#tong'),
//                 end: function () {
//                     if (null != v7) {
//                         if (undefined != v7.S) {
//                             v7.disconnect();
//                             delete v7;
//                             v7 = null;
//                         }
//                     }
//                 }
//             });
//             //结束loading
//             layer.close(loading);
//         }, 2000);
//     }
// });
