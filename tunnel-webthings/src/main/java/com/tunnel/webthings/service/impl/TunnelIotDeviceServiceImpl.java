package com.tunnel.webthings.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.dao.TunnelIotDeviceMapper;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import com.tunnel.webthings.vo.SdActiveLuminousSignsVO;
import com.tunnel.webthings.vo.SdConfluenceDevFaultWarnVO;
import com.tunnel.webthings.vo.SdRadarMsgTopicVO;
import com.tunnel.webthings.vo.SdStateStorageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZHC
 * {@code @date} 2022/7/18 10:07
 * 隧道物联设备Service接口实现类
 */
@Service
public class TunnelIotDeviceServiceImpl implements TunnelIotDeviceService {
    @Autowired
    private TunnelIotDeviceMapper iotDeviceMapper;

    @Override
    public String selectIotDeviceType(String devType) {
        return iotDeviceMapper.selectIotDeviceType(devType);
    }

    /**
     * 添加主动发光标志数据控制信息
     * @param vo
     * @return
     */
    @Override
    public int addActiveLuminousSigns(SdActiveLuminousSignsVO vo) {
        vo.setEx(vo.getExpands());
        int addNumber = 0;
        for (String devN : vo.getDevNo()) {
            vo.setDevN(devN);
            int i = iotDeviceMapper.addActiveLuminousSigns(vo);
            addNumber = addNumber + i;
        }
        return addNumber;
    }

    /**
     * 添加合流区预警设备故障告警
     * @param vo
     * @return
     */
    @Override
    public int addConfluenceDevFaultWarn(SdConfluenceDevFaultWarnVO vo) {
        JSON parse = JSONUtil.parse(vo.getExpands());
        vo.setEx(parse.toString());
        int addNumber = 0;
        for (String devN : vo.getDevNo()) {
            vo.setDevN(devN);
            int i = iotDeviceMapper.addConfluenceDevFaultWarn(vo);
            addNumber = addNumber + i;
        }
        return addNumber;
    }

    /**
     * 添加雷达信息数据
     * @param vo
     * @return
     */
    @Override
    public int addRadarMag(SdRadarMsgTopicVO vo) {
        JSON parse = JSONUtil.parse(vo.getExpands());
        vo.setEx(parse.toString());
        int addNumber = 0;
        for (String devN : vo.getDevNo()) {
            vo.setDevN(devN);
            int i = iotDeviceMapper.addRadarMsgTopic(vo);
            addNumber = addNumber + i;
        }
        return addNumber;
    }

    /**
     * 添加设备状态更改信息
     * @param vo
     * @return
     */
    @Override
    public int addStateStorage(SdStateStorageVO vo) {
        JSON parse = JSONUtil.parse(vo.getExpands());
        vo.setEx(parse.toString());
        int addNumber = 0;
        for (String devN : vo.getDevNo()) {
            vo.setDevN(devN);
            addNumber = iotDeviceMapper.addStateStorage(vo);
        }
        return addNumber;
    }

    @Override
    public void method() {
        //获取url
        List<String> list=iotDeviceMapper.selectFile();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("E:/")){
                sb.append(list.get(i));
                String s = list.get(i).replace("E:/", "E:/equipmentIcon");
                String s1 = sb.toString();
                iotDeviceMapper.update(s1,s);
                sb.delete(0,sb.length());
            }
        }

    }
}
