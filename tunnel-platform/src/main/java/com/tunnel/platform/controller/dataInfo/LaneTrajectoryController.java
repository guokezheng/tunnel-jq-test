package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.core.controller.BaseController;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LaneTrajectoryController
 * 戴升平台推送的车道轨迹数据
 *
 * @author WYZ
 * @date 2021-11-26
 */
//@Api(tags = "戴升平台推送的车道轨迹数据")
@RestController
@RequestMapping("/LaneTrajectory")
public class LaneTrajectoryController extends BaseController {
    /*
     * 戴升平台推送的实时车道轨迹数据
     * */
  //  @ApiOperation("车道轨迹数据")
    @PostMapping("/realTimeLaneTrajectory")
    public void realTimeLaneTrajectory(@RequestBody Object roadData) {
        WebSocketService.broadcast("realTimeLaneTrajectory", roadData);
    }

}
