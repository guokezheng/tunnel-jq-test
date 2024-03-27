package com.tunnel.platform.controller.bigScreenApi;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdControlRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 近12小时控制记录信息Controller
 *
 * @author wuhaoyang
 * @date 2021-12-13
 */
@RestController
@RequestMapping("/controlRecord")
@Api(tags = "近12小时控制记录信息")
@ApiSupport(order = 16)
public class SdControlRecordController extends BaseController {


    @Autowired
    private ISdControlRecordService iSdControlRecordService;

    /**
     * 查询近12小时控制记录
     * @param tunnelId
     * @return
     */
    @ApiOperation("查询近12小时控制记录")
    @PostMapping("/recordlist")
    public AjaxResult getRecentControlRecordMsg(String tunnelId){
        List<Map<String, Object>> recentControlRecordMsg = iSdControlRecordService.getRecentControlRecordMsg(tunnelId);
        return AjaxResult.success(recentControlRecordMsg);
    }

}
