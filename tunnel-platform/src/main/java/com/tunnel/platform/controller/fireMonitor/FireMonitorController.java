package com.tunnel.platform.controller.fireMonitor;


import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.tunnel.platform.media.web.controller.media.MediaController;
import com.tunnel.platform.media.web.domain.MediaStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 消防炮操作Controller
 *
 * @author tjw
 * @date 2023-06-17
 */
@RestController
@RequestMapping("/fireMonitor")
@Api(tags = "消防炮操作Controller")
@ApiSupport(order = 16)
public class FireMonitorController {

    @Autowired
    MediaController mediaController;

    /**
     * 获取rtsp流地址
     */
    @GetMapping("/getRtspStreamAddr")
    @ApiOperation("获取rtsp流地址")
    public String getRtspStreamAddr(RedirectAttributes redirectAttributes,String ip)
    {

        //从数据库中查询视频流地址
        String rtspStream  ="";
        MediaStream mediaStream=new MediaStream();
        mediaStream.setRtspUrl(rtspStream);//rtsp视频流地址
        mediaStream.setStreamId("1");//视频流的唯一标识
        mediaController.add(mediaStream);
        //获取flv地址
        MediaStream mediaStream1=new MediaStream();
        mediaStream1.setStreamId("1");
        String flvStream = String.valueOf(mediaController.getStream(mediaStream1));
        return flvStream;
    }




}
