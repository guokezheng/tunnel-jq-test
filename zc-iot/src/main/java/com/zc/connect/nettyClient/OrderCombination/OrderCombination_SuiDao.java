package com.zc.connect.nettyClient.OrderCombination;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.zc.connect.config.NettyClientConfig;
import com.zc.connect.config.NettyClientConfigAttr;
import com.zc.connect.nettyClient.NettyClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author YangChao
 * @description 指令组装类
 *  当前类是各业务组拼装指令得类
 *  order传过来是下发指令得类型 通过这个类拼装指令(包括一系列算法)
 * @date 2021/11/23/023
 * @apiNote 定时任务无法使用 @Autowired 注解 使用工具类获取
 */
@Component
public class OrderCombination_SuiDao {


    /**
     * NettyClient 客户端
     */
    private NettyClient nettyClient = SpringUtils.getBean(NettyClient.class);
    /**
     * NettyClient 配置文件参数
     */
    private NettyClientConfig nettyClientConfig = SpringUtils.getBean(NettyClientConfig.class);

    /** 指令下发 */
    public void orderSend(){
        List<NettyClientConfigAttr> list = nettyClientConfig.getConfig();
        for(NettyClientConfigAttr configAttr: nettyClientConfig.getConfig()){
            // ip
            String ip = configAttr.getIp();
            // 端口
            int port = configAttr.getPort();
            // 回令处理类
            String handler_name = configAttr.getHandler_name();
            // 指令码类型
            String order_type = configAttr.getOrder();
            String order = "";
            switch (order_type)
            {
                case "chaxun_CIO":
                    order = "46494E530000001A0000000200000000800002";
                    break;
                case "chaxun_DM":
                    order = "46494E530000001A0000000200000000800002";
                    break;
                default:
                    break;
            }
            nettyClient.clientOrderSend(ip,port,order,handler_name);
        }
    }
}
