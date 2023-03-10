package com.tunnel.webthings.controller;

import com.tunnel.webthings.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调用示例
 */
@RequestMapping("/send")
@RestController
public class SendCotroller {

    @Autowired
    private SendService sendService;

//    @PostMapping("/status")
//    public String status() {
//        sendService.sendStatus();
//        return "OK";
//    }
//
//    @PostMapping("/event")
//    public String event() {
//        sendService.sendEvent();
//        return "OK";
//    }
//
//    @PostMapping("/direct")
//    public String direct() {
//        sendService.sendDirect();
//        return "OK";
//    }

}
