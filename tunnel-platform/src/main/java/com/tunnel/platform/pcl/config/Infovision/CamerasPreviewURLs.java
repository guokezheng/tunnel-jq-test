package com.tunnel.platform.pcl.config.Infovision;

import com.alibaba.fastjson.JSON;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.tunnel.platform.pcl.config.dataVo.PreviewURLsRequest;

import java.util.HashMap;
import java.util.Map;

public class CamerasPreviewURLs {
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

    //获取监控点预览取流URL
    public static String previewURLs(PreviewURLsRequest previewURLsRequest ){
        String previewURLsDataApi = ARTEMIS_PATH +"/api/video/v1/cameras/previewURLs";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",previewURLsDataApi);
            }
        };
        String body= JSON.toJSONString(previewURLsRequest);
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }

    /***
     * 请求参数:
     * {
     *     "cameraIndexCode": "90ad77d8057c43dab140b77361606927",
     *     "streamType": 0,
     *     "protocol": "rtsp",
     *     "transmode": 0
     * }
     * @param args
     */
    public static void main(String[] args){
        PreviewURLsRequest request = new PreviewURLsRequest();
        request.setCameraIndexCode("90ad77d8057c43dab140b77361606927");
        request.setStreamType(0);
        request.setProtocol("rtsp");
        request.setTransmode(0);
        previewURLs(request);
    }
}

