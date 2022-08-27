package com.tunnel.platform.pcl.config.Infovision;

import com.alibaba.fastjson.JSON;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.tunnel.platform.pcl.config.dataVo.CamerasRequest;

import java.util.HashMap;
import java.util.Map;

public class ResourceCameras {
    /**
     * 请根据自己的appKey和appSecret更换static静态块中的三个参数。
     */
    static {
        ArtemisConfig.host = "open8200.hikvision.com"; // 平台/nginx的IP和端口（必须使用https协议，https端口默认为443）
        ArtemisConfig.appKey = "请填入appKey"; // 秘钥appkey
        ArtemisConfig.appSecret = "请填入appSecret";// 秘钥appSecret
    }
    /**
     * 能力开放平台的网站路径
     * 路径不用修改，就是/artemis
     */
    private static final String ARTEMIS_PATH = "/artemis";

    //分页获取监控点资源
    public static String cameras(CamerasRequest camerasRequest ){
        String camerasDataApi = ARTEMIS_PATH +"/api/resource/v1/cameras";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",camerasDataApi);
            }
        };
        String body= JSON.toJSONString(camerasRequest);
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }


    public static void main(String[] args){
        CamerasRequest request=new CamerasRequest();
        request.setPageNo(1);
        request.setPageSize(20);
        request.setTreeCode("0");
        cameras(request);
    }
}


