package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.text.Convert;
import com.tunnel.business.domain.informationBoard.IotBoardReleaseLog;
import com.tunnel.business.domain.informationBoard.IotDeviceAccess;
import com.tunnel.business.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.business.mapper.informationBoard.IotDeviceAccessMapper;
import com.tunnel.business.service.informationBoard.IIotBoardReleaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 情报板内容发布日志Service业务层处理
 *
 * @author wangyaozong
 * @date 2020-06-01
 */
@Service
public class IotBoardReleaseLogServiceImpl implements IIotBoardReleaseLogService {
    @Autowired
    private IotBoardReleaseLogMapper iotBoardReleaseLogMapper;
    @Autowired
    private IotDeviceAccessMapper iotDeviceAccessMapper;

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
        List<IotBoardReleaseLog> iotBoardReleaseLogs = iotBoardReleaseLogMapper.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        List<IotDeviceAccess> iotDeviceAccesses = iotDeviceAccessMapper.selectIotDeviceAccessList(null);
        for (int i = 0;i < iotBoardReleaseLogs.size();i++) {
            IotBoardReleaseLog releaseLog = iotBoardReleaseLogs.get(i);
            String releaseNewContent = releaseLog.getReleaseNewContent();
            if (releaseNewContent.equals("") || releaseNewContent == null) {
                continue;
            }
            releaseNewContent = releaseNewContent.substring(releaseNewContent.indexOf("ITEM000"));
            if (!releaseNewContent.contains("ITEM")) {
                releaseLog.setReleaseNewContent("历史记录获取异常");
                continue;
            }
            String[] items = releaseNewContent.split("ITEM");
            if (items.length == 0) {
                continue;
            }
            Long deviceId = Long.valueOf(releaseLog.getDeviceId());
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            for (int j = 0;j < items.length;j++) {
                String item = items[j];
                if (item.equals("") || item == null) {
                    continue;
                }
                JSONObject itemObject = new JSONObject();
                JSONArray itemArray = new JSONArray();
                JSONObject object = new JSONObject();
                String num = item.substring(0,item.indexOf("=") == -1 ? 0 : item.indexOf("="));
                item = item.substring(item.indexOf("=") + 1 == -1 ? 0 : item.indexOf("=") + 1);
                String stay = item.substring(0, item.indexOf(",") == -1 ? 0 : item.indexOf(","));
                object.put("STAY", stay);
                item = item.substring(item.indexOf(",") + 1 == -1 ? 0 : item.indexOf(",") + 1);
                String action = item.substring(0, item.indexOf(",") == -1 ? 0 : item.indexOf(","));
                object.put("ACTION", action);
                item = item.substring(item.indexOf(",") + 1 == -1 ? 0 : item.indexOf(",") + 1);
                String speed = item.substring(0, item.indexOf(",") == -1 ? 0 : item.indexOf(","));
                object.put("SPEED", speed);
                item = item.substring(item.indexOf("C") + 1 == -1 ? 0 : item.indexOf("C"));
                String coordinate = item.substring(0, 6);
                object.put("COORDINATE", coordinate);
                item = item.substring(item.indexOf("c") + 1 == -1 ? 0 : item.indexOf("c") + 1);
                String color = item.substring(0, 12);
                color = handleColor(color);
                object.put("COLOR", color);
                item = item.substring(item.indexOf("f") == -1 ? 0 : item.indexOf("f"));
                String font = item.substring(1, 2);
                font = handleFont(font);
                object.put("FONT", font);
                item = item.substring(2);
                String fontSize = item.substring(0, 4);
                object.put("FONT_SIZE", fontSize);
                item = item.substring(4);
                item = item.replaceAll("<r><n>", "");
                object.put("CONTENT", item);
                object.put("STATE", "true");
                for (int m = 0;m < iotDeviceAccesses.size();m++) {
                    IotDeviceAccess iotDeviceAccess = iotDeviceAccesses.get(m);
                    if (iotDeviceAccess.getDeviceId().longValue() == deviceId.longValue()
                            && iotDeviceAccess.getDevicePixel() != null && !iotDeviceAccess.getDevicePixel().equals("")) {
                        object.put("DEVICEPIXEL", iotDeviceAccess.getDevicePixel());
                        releaseLog.setDevicePixel(iotDeviceAccess.getDevicePixel());
                    }
                }
                itemArray.add(object);
                itemObject.put("ITEM" + num, itemArray);
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
        List<IotBoardReleaseLog> iotBoardReleaseLogs = iotBoardReleaseLogMapper.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        List<IotDeviceAccess> iotDeviceAccesses = iotDeviceAccessMapper.selectIotDeviceAccessList(null);
        for (int i = 0;i < iotBoardReleaseLogs.size();i++) {
            IotBoardReleaseLog releaseLog = iotBoardReleaseLogs.get(i);
            String releaseNewContent = releaseLog.getReleaseNewContent();
            if (releaseNewContent.equals("") || releaseNewContent == null) {
                continue;
            }
            releaseNewContent = releaseNewContent.substring(releaseNewContent.indexOf("ITEM000"));
            if (!releaseNewContent.contains("ITEM")) {
                releaseLog.setReleaseNewContent("历史记录获取异常");
                continue;
            }
            String[] items = releaseNewContent.split("ITEM");
            if (items.length == 0) {
                continue;
            }
            Long deviceId = Long.valueOf(releaseLog.getDeviceId());
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            String releaseContent = "";
            for (int j = 0;j < items.length;j++) {
                String item = items[j];
                if (item.equals("") || item == null) {
                    continue;
                }
                JSONObject itemObject = new JSONObject();
                JSONArray itemArray = new JSONArray();
                JSONObject object = new JSONObject();
                String num = item.substring(0,item.indexOf("="));
                item = item.substring(item.indexOf("=") + 1);
                String stay = item.substring(0, item.indexOf(","));
                object.put("STAY", stay);
                item = item.substring(item.indexOf(",") + 1);
                String action = item.substring(0, item.indexOf(","));
                object.put("ACTION", action);
                item = item.substring(item.indexOf(",") + 1);
                String speed = item.substring(0, item.indexOf(","));
                object.put("SPEED", speed);
                item = item.substring(item.indexOf("C") + 1);
                String coordinate = item.substring(0, 6);
                object.put("COORDINATE", coordinate);
                item = item.substring(item.indexOf("c") + 1);
                String color = item.substring(0, 12);
                color = handleColor(color);
                object.put("COLOR", color);
                item = item.substring(item.indexOf("f"));
                String font = item.substring(1, 2);
                font = handleFont(font);
                object.put("FONT", font);
                item = item.substring(2);
                String fontSize = item.substring(0, 4);
                object.put("FONT_SIZE", fontSize);
                item = item.substring(4);
                item = item.replaceAll("<r><n>", "");
                object.put("CONTENT", item);
                releaseContent = releaseContent + item + ";";
                object.put("STATE", "true");
                for (int m = 0;m < iotDeviceAccesses.size();m++) {
                    IotDeviceAccess iotDeviceAccess = iotDeviceAccesses.get(m);
                    if (iotDeviceAccess.getDeviceId().longValue() == deviceId.longValue()
                            && iotDeviceAccess.getDevicePixel() != null && !iotDeviceAccess.getDevicePixel().equals("")) {
                        object.put("DEVICEPIXEL", iotDeviceAccess.getDevicePixel());
                        releaseLog.setDevicePixel(iotDeviceAccess.getDevicePixel());
                    }
                }
                itemArray.add(object);
                itemObject.put("ITEM" + num, itemArray);
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
            releaseLog.setReleaseNewContent(releaseContent);
        }
        return iotBoardReleaseLogs;
    }
}
