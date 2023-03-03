package com.ruoyi.quartz.task;

import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.protocol.SdDevicePointMapper;
import com.tunnel.deal.plc.omron.OmronConnectProperties;
import com.tunnel.deal.plc.omron.exception.PlcException;
import com.tunnel.deal.plc.omron.finsTCP.OmronMessageHeader;
import com.tunnel.deal.plc.omron.finsTCP.OmronTcpClient;
import com.tunnel.deal.plc.omron.util.ByteUtil;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 欧姆龙定时任务调度
 */
@Component("omronTask")
public class OmronTask {


    private static final Logger log = LoggerFactory.getLogger(OmronTask.class);

    OmronTcpClient omronTcpClient = new OmronTcpClient(getOmronConnectProperties());

    @Autowired
    private SdDevicePointMapper sdDevicePointMapper;


    public void runTask(){
        try {
            //初始化链接     服务器地址
            omronTcpClient.init("127.0.0.1",60000);
        } catch (Exception e) {
            log.error("");
        }
        //获取当前所有设备以及点位信息。       根据条件筛选 当前 项目下 欧姆龙FINS协议的点位信息 下所有点位信息
        SdDevicePoint sdDevicePoint = new SdDevicePoint();
        //筛选条件。   (当前为测试  默认筛选所有。后期根据设备类型  以及  协议ID进行筛选)
        List<SdDevicePoint> sdDevicePointList = sdDevicePointMapper.selectSdDevicePointList(sdDevicePoint);

        for (SdDevicePoint sdd:sdDevicePointList) {
            byte[] data = new byte[0];
            try {
                //查看当前点位信息是否为   位信息。
                boolean isBit = sdd.getAddress().contains(".")?true:false;
                //读取数据
                byte[] dataInfo = omronTcpClient.buildReadRequestBody(sdd.getAddress(),isBit);

                OmronMessageHeader  omronMessageHeader = new OmronMessageHeader();
                byte[] dataBody = omronTcpClient.getMessageBody(omronMessageHeader,dataInfo.length);
                data = omronTcpClient.send(byteMerger(dataBody,dataInfo));
                //返回数据校验        true  成功   false  失败
                if(omronTcpClient.doBuildResponseMessage(data)){
                    //解析当前数据  根据设备点位信息解析
                    if(isBit){
                        //获取结尾一字节  结果集
                        byte code = data[data.length-1];
                    }else{
                        //获取结尾2字节  结果集

                    }
                    ThreadPool.executor.execute(() ->{

                        //数据持久化

                    });
                }
            } catch (PlcException e) {
                e.printStackTrace();
            }
            if(data!=null) {
                log.info("返回数据:{}",ByteUtil.bytesToHex(data));
            } else {
                log.info("未获取到返回数据");
            }
        }
    }

    /**
     * 初始化欧姆龙配置信息  (当前为测试数据  后期整理为线上链接)
     * @return
     */
    public OmronConnectProperties getOmronConnectProperties(){
        OmronConnectProperties conf = new OmronConnectProperties();
        //DA1地址： 服务器 ip
        conf.setHost("127.0.0.1");
        //SA1地址：电脑 ip
        conf.setLocalHost("127.0.0.1");
        //服务器port
        conf.setPort(6000);
        return conf;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

}
