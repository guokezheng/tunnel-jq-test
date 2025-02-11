package com.zc.connect.nettyClient.ClientHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author YangChao
 * @description 客户端 回令处理类
 * @date 2021/11/23/023
 * @apiNote
 */
@Component
public class ClientHandlers_SuiDao {
    private static final Logger log = LoggerFactory.getLogger(ClientHandlers_SuiDao.class);

    /**
     * 回令业务处理
     * 解析
     */
    public void response_analysis(String msg){
        String data = String.valueOf(msg);
        log.info("解析1协议接收:" + data);
    }


}
