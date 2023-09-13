package com.tunnel.deal.corniceTunnelRobot.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.tunnel.deal.corniceTunnelRobot.CorniceTunnelRobot;
import com.tunnel.deal.corniceTunnelRobot.domain.FindAlarmThresholdConfigDto;
import com.tunnel.deal.corniceTunnelRobot.domain.RobotsDto;
import com.tunnel.deal.corniceTunnelRobot.domain.StatusDto;
import com.tunnel.deal.corniceTunnelRobot.domain.VideoDto;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 北京卓视智通科技 - 飞檐隧道机器人平台系统
 */
@Component
public class ZhuoShiCorniceTunnelRobot implements CorniceTunnelRobot {

    @Override
    public String GetUsers(String userName, String userPassword, String baseUrl) {

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("userName", userName);
        reqMap.put("userPassword", userPassword);

        JSONObject jsonObject = toSend(baseUrl + "/api/adjustBrightness", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return jsonObject.getJSONObject("data").getString("userId");
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return null;
    }

    @Override
    public List<RobotsDto> GetUserRobots(Integer userId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("userId", userId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/GetUserRobots", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return (List<RobotsDto>) jsonObject.get("data");
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return null;
    }

    @Override
    public StatusDto GetStatus(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/GetStatus?deviceId="+deviceId, "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return JSONObject.parseObject(jsonObject.get("data").toString(),StatusDto.class);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public VideoDto GetVideoUrl(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/GetVideoUrl?deviceId="+deviceId, "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return JSONObject.parseObject(jsonObject.get("data").toString(),VideoDto.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return null;
    }

    @Override
    public int SetLEDLight(String deviceId, Integer control, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("control", control);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/SetLEDLight?deviceId=" + deviceId + "&control=" + control, "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int SetAlarmLight(String deviceId, Integer control, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("control", control);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/SetAlarmLight", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int Charge(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/Charge", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int GotoPreset(String deviceId, Integer presetId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("presetId", presetId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/GotoPreset", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int Move(String deviceId, Integer control, Integer speed, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("control", control);
        reqMap.put("speed", speed);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/Move", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int PTZ(String deviceId, Integer control, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("control", control);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/PTZ", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int Broadcast(String deviceId, String text, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("text", text);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/Broadcast", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int ChangeControl(String deviceId, Integer carmode, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);
        reqMap.put("carmode", carmode);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/ChangeControl", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int ControlWindscreen(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/ControlWindscreen", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public FindAlarmThresholdConfigDto FindAlarmThresholdConfig(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/FindAlarmThresholdConfig", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return (FindAlarmThresholdConfigDto) jsonObject.get("data");
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return null;
    }

    @Override
    public int SetAlarmThresholdConfig(FindAlarmThresholdConfigDto findAlarmThresholdConfigDto, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", findAlarmThresholdConfigDto.getDeviceId());
        reqMap.put("id", findAlarmThresholdConfigDto.getId());
        reqMap.put("groupName", findAlarmThresholdConfigDto.getGroupName());
        reqMap.put("beginTime", findAlarmThresholdConfigDto.getBeginTime());
        reqMap.put("endTime", findAlarmThresholdConfigDto.getEndTime());
        reqMap.put("powerEnable", findAlarmThresholdConfigDto.getSendStatus());
        reqMap.put("powerLowerLimit", findAlarmThresholdConfigDto.getPowerLowerLimit());
        reqMap.put("powerTopLimit", findAlarmThresholdConfigDto.getPowerTopLimit());

        JSONObject jsonObject = toSend(baseUrl + "/Robot/SetAlarmThresholdConfig", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int InitializeRobot(String deviceId, String baseUrl) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("deviceId", deviceId);

        JSONObject jsonObject = toSend(baseUrl + "/Robot/InitializeRobot", "GET", reqMap);

        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    @Override
    public int OneClickArrival(String deviceId, String posX, String posY, String angle, String baseUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("posX",posX);
        map.put("posY",posY);
        map.put("angle",angle);
        JSONObject jsonObject = toSend(baseUrl + "/Robot/OneclickArrival?deviceId=" + deviceId+"&posX="+posX+"&posY=" + posY + "&angle=" + angle, "GET", map);
        try {
            if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("飞檐隧道机器人平台数据解析异常");
        }
        return 0;
    }

    public JSONObject toSend(String url, String post, Map<String, Object> reqMap) {
       /* OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
       // okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(reqMap));
        Request request = new Request.Builder()
                .url(url)
                .method(post,null)
                .build();*/
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3,TimeUnit.SECONDS)
                .writeTimeout(3,TimeUnit.SECONDS).build();
        Request request = new Request
                .Builder() //利用建造者模式创建Request对象
                .url(url) //设置请求的URL
                .build(); //生成Request对象
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            if (StringUtils.isNotBlank(result) && JSONValidator.from(result).validate()) {
                return JSONObject.parseObject(result);
            }
        } catch (IOException e) {
            //throw new RuntimeException("飞檐隧道机器人平台服务异常");
            return null;
        }finally{
            if(response != null){
                response.close();
            }
        }
        return null;
    }


}
