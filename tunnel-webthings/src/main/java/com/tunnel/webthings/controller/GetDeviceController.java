package com.tunnel.webthings.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.webthings.domain.DeviceParameter;
import com.tunnel.webthings.service.GetDeviceService;
import com.tunnel.webthings.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author dzy
 * @date 2022/8/22 17:17
 */
@RequestMapping("/select")
@RestController
public class GetDeviceController {

    @Autowired
    private GetDeviceService getDeviceService;

    @PostMapping("/getDevList")
    public AjaxResult getDevList(@RequestBody DeviceParameter dev){
        List<ResponseVO> voList =getDeviceService.getDevList(dev);
        return AjaxResult.success(voList);
    }

    @PostMapping("/copyData")
    public void copyData(){
        getDeviceService.copyData();
    }
}
