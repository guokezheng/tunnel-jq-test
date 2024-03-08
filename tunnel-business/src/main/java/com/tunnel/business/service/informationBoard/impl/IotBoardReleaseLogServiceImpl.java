package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.informationBoard.IotBoardReleaseLog;
import com.tunnel.business.domain.informationBoard.IotDeviceAccess;
import com.tunnel.business.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.business.mapper.informationBoard.IotDeviceAccessMapper;
import com.tunnel.business.mapper.informationBoard.SdIotDeviceMapper;
import com.tunnel.business.service.informationBoard.IIotBoardReleaseLogService;
import com.tunnel.business.utils.util.DataUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 情报板内容发布日志Service业务层处理
 *
 * @author wangyaozong
 * @date 2020-06-01
 */
@Service
public class IotBoardReleaseLogServiceImpl implements IIotBoardReleaseLogService {


    private static final Logger logger = LoggerFactory.getLogger(IotBoardReleaseLogServiceImpl.class);

    @Autowired
    private IotBoardReleaseLogMapper iotBoardReleaseLogMapper;
    @Autowired
    private IotDeviceAccessMapper iotDeviceAccessMapper;

    @Autowired
    private SdIotDeviceMapper sdIotDeviceMapper;

    /**
     * 查询情报板内容发布日志
     *
     * @param id 情报板内容发布日志ID
     * @return 情报板内容发布日志
     */
    @Override
    public IotBoardReleaseLog selectIotBoardReleaseLogById(Long id) {
        return iotBoardReleaseLogMapper.selectIotBoardReleaseLogById(id);
    }

    /**
     * 查询情报板内容发布日志列表
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 情报板内容发布日志
     */
    @Override
    public List<IotBoardReleaseLog> selectIotBoardReleaseLogList(IotBoardReleaseLog iotBoardReleaseLog) {
        if (SecurityUtils.getDeptId() != null && !"".equals(SecurityUtils.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            iotBoardReleaseLog.getParams().put("deptId", deptId);
        }
        List<IotBoardReleaseLog> iotBoardReleaseLogs = iotBoardReleaseLogMapper.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        List<IotDeviceAccess> iotDeviceAccesses = iotDeviceAccessMapper.selectIotDeviceAccessList(null);
        for (int i = 0;i < iotBoardReleaseLogs.size();i++) {
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            IotBoardReleaseLog releaseLog = iotBoardReleaseLogs.get(i);
            String releaseNewContent = releaseLog.getReleaseNewContent();
            if (releaseNewContent == null || "".equals(releaseNewContent)) {
                continue;
            }
            releaseNewContent = releaseNewContent.substring(releaseNewContent.indexOf("ITEM000"));
            if (!releaseNewContent.contains("ITEM")) {
                releaseLog.setReleaseNewContent("历史记录获取异常");
                continue;
            }
            String protocolName = sdIotDeviceMapper.selectIotDeviceById(Long.valueOf(releaseLog.getDeviceId())).getProtocolName();
            String boardCon = DataUtils.itemContentToJson(releaseNewContent, protocolName);
            JSONArray objects = new JSONArray();
            if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                objects = JSONObject.parseArray(boardCon);
            }
            for(int e = 0; e < objects.size(); e++){
                JSONArray itemArray = new JSONArray();
                JSONObject itemObject = new JSONObject();
                JSONObject jsonObject1 = JSONObject.parseObject(objects.get(e).toString());
                String key = "ITEM" + String.format("%03d", e);
                JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get(key).toString());
                for(int a = 0; a < jsonArray.size(); a++){
                    JSONObject jsonObject2 = JSONObject.parseObject(jsonArray.get(a).toString());
                    if(protocolName.startsWith("DINGEN")){
                        String content1 = jsonObject2.getString("CONTENT");
                        if(content1.contains("W")){
                            content1 = content1.substring(1, jsonObject2.getString("CONTENT").length());
                        }
                        jsonObject2.put("CONTENT",content1);
                    }
                    IotDeviceAccess iotDeviceAccess = iotDeviceAccessMapper.selectIotDeviceAccessById(Long.valueOf(releaseLog.getDeviceId()));
                    jsonObject2.put("STATE", "true");
                    jsonObject2.put("DEVICEPIXEL",iotDeviceAccess.getDevicePixel());
                    itemArray.add(jsonObject2);
                }
                itemObject.put(key, itemArray);
                array.add(itemObject);
            }
            jsonObject.put("content", array);
            List<String> paramsList = new ArrayList<String>();
            paramsList.add(jsonObject.toString());
            releaseLog.setParamsList(paramsList);
        }
        return iotBoardReleaseLogs;
    }

    private String handleFont(String font) {
        if (font.equals("h")) {
            return "黑体";
        } else if (font.equals("k")) {
            return "楷体";
        } else if (font.equals("f")) {
            return "仿宋";
        } else if (font.equals("l")) {
            return "隶书";
        }
        return "宋体";
    }

    private String handleColor(String color) {
        if (color.equals("000255000000")) {
            return "绿色";
        } else if (color.equals("255000000000")) {
            return "红色";
        } else if (color.equals("000000255000")) {
            return "蓝色";
        }
        return "黄色";
    }

    /**
     * 新增情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    @Override
    public int insertIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog) {

        // 记录服务ip
        iotBoardReleaseLog.setReleaseIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        //推送到gsy
        try {
            String baseLogUrl = "http://10.166.157.192:31028/prod-api/login";
            String baseUrl = "http://10.166.157.192:31028/prod-api/system/log/addIotBoard";
            String username = "admin";
            String password = "Tunnel123!@#";
            //获取token
            String token = login( username, password,baseLogUrl);
            //同步数据
            syncBoardReleaseLog(token,baseUrl,iotBoardReleaseLog);
        }catch (Exception e){
            logger.error("");
        }
        return iotBoardReleaseLogMapper.insertIotBoardReleaseLog(iotBoardReleaseLog);
    }


    /**
     * 新增情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    @Override
    public int synIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog) {

        return iotBoardReleaseLogMapper.insertIotBoardReleaseLog(iotBoardReleaseLog);
    }

    /**
     * 修改情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    @Override
    public int updateIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog) {
        return iotBoardReleaseLogMapper.updateIotBoardReleaseLog(iotBoardReleaseLog);
    }

    /**
     * 删除情报板内容发布日志对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardReleaseLogByIds(String ids) {
        return iotBoardReleaseLogMapper.deleteIotBoardReleaseLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除情报板内容发布日志信息
     *
     * @param id 情报板内容发布日志ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardReleaseLogById(Long id) {
        return iotBoardReleaseLogMapper.deleteIotBoardReleaseLogById(id);
    }

    @Override
    public List<IotBoardReleaseLog> getLastReleaseLogsByDeviceId(String deviceId) {
        return iotBoardReleaseLogMapper.getLastReleaseLogsByDeviceId(deviceId);
    }

    @Override
    public List<IotBoardReleaseLog> selectIotBoardReleaseLogListToExport(IotBoardReleaseLog iotBoardReleaseLog) {
        if (SecurityUtils.getDeptId() != null && !"".equals(SecurityUtils.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            iotBoardReleaseLog.getParams().put("deptId", deptId);
        }
        List<IotBoardReleaseLog> iotBoardReleaseLogs = iotBoardReleaseLogMapper.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        List<IotDeviceAccess> iotDeviceAccesses = iotDeviceAccessMapper.selectIotDeviceAccessList(null);
        for (int i = 0;i < iotBoardReleaseLogs.size();i++) {
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            IotBoardReleaseLog releaseLog = iotBoardReleaseLogs.get(i);
            String releaseNewContent = releaseLog.getReleaseNewContent();
            if (releaseNewContent == null || "".equals(releaseNewContent)) {
                continue;
            }
            releaseNewContent = releaseNewContent.substring(releaseNewContent.indexOf("ITEM000"));
            if (!releaseNewContent.contains("ITEM")) {
                releaseLog.setReleaseNewContent("历史记录获取异常");
                continue;
            }
            String protocolName = sdIotDeviceMapper.selectIotDeviceById(Long.valueOf(releaseLog.getDeviceId())).getProtocolName();
            String boardCon = DataUtils.itemContentToJson(releaseNewContent, protocolName);
            JSONArray objects = new JSONArray();
            if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                objects = JSONObject.parseArray(boardCon);
            }
            String releaseContent = "";
            for(int e = 0; e < objects.size(); e++){
                JSONArray itemArray = new JSONArray();
                JSONObject itemObject = new JSONObject();
                JSONObject jsonObject1 = JSONObject.parseObject(objects.get(e).toString());
                String key = "ITEM" + String.format("%03d", e);
                JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get(key).toString());
                for(int a = 0; a < jsonArray.size(); a++){
                    JSONObject jsonObject2 = JSONObject.parseObject(jsonArray.get(a).toString());
                    if(protocolName.startsWith("DINGEN")){
                        String content1 = jsonObject2.getString("CONTENT");
                        if(content1.contains("W")){
                            content1 = content1.substring(1, jsonObject2.getString("CONTENT").length());
                        }
                        jsonObject2.put("CONTENT",content1);
                    }
                    IotDeviceAccess iotDeviceAccess = iotDeviceAccessMapper.selectIotDeviceAccessById(Long.valueOf(releaseLog.getDeviceId()));
                    jsonObject2.put("STATE", "true");
                    jsonObject2.put("DEVICEPIXEL",iotDeviceAccess.getDevicePixel());
                    itemArray.add(jsonObject2);
                    releaseContent = releaseContent + jsonObject2.getString("CONTENT") + ";";
                }
                itemObject.put(key, itemArray);
                array.add(itemObject);
            }
            jsonObject.put("content", array);
            List<String> paramsList = new ArrayList<String>();
            paramsList.add(jsonObject.toString());
            releaseLog.setParamsList(paramsList);
            releaseContent = releaseContent.replaceAll("\\\\n", "");
            releaseContent = releaseContent.replaceAll("\\\\r", "");
            releaseContent = releaseContent.replaceAll("<r><n>","");
            releaseContent = releaseContent.replaceAll("<r>","");
            releaseContent = releaseContent.replaceAll("<n>","");
            releaseContent = releaseContent.replaceAll("<br>","");
            releaseLog.setReleaseNewContent(releaseContent);
        }
        return iotBoardReleaseLogs;
    }
    public String login(String username, String password, String baseUrl) {
        Response response = null;
        String token = "";
        try {
            String data = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //强行返回true 即验证成功
                    return true;
                }
            }).build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl).post(body).build();
            response=client.newCall(request).execute();
            JSONObject jo = JSONObject.parseObject(  response.body().string());
            token = jo.getString("token");
        } catch (IOException e) {
            logger.error("同步数据登录异常提示",e.getMessage());
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        return token;
    }
    private void syncBoardReleaseLog(String token,String baseUrl,IotBoardReleaseLog iotBoardReleaseLog){
        if(!StringUtils.isEmpty(token)){
            Response response = null;
            try {
                String data = JSON.toJSONString(iotBoardReleaseLog);
                OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        //强行返回true 即验证成功
                        return true;
                    }
                }).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body=RequestBody.create(mediaType, data);
                Request request=new Request.Builder().url(baseUrl).put(body).addHeader("Authorization", token).build();
                response=client.newCall(request).execute();
                JSONObject jo = JSONObject.parseObject(  response.body().string());
                logger.info("同步情报板数据返回结果",jo);
            } catch (IOException e) {
                logger.error("同步情报板数据异常提示",e.getMessage());
            }finally{
                if(response != null){
                    response.close();
                }
            }
        }
    }
}
