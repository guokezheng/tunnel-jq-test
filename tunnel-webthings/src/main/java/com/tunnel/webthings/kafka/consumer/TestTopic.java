package com.tunnel.webthings.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * describe: 测试华为kafka推送
 *
 * @author zs
 * @date 2023/1/7
 */
@Controller
@RequestMapping("/testTopic")
public class TestTopic {

    @Autowired
    @Qualifier("kafkaThreeTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/event")
    public String event(@RequestBody JSONObject eventObject) {

        //推送给华为
        kafkaTemplate.send("rhy_tunnel_merge_event", eventObject.toJSONString());
        return "OK";
    }



}
