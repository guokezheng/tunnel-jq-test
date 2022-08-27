package com.tunnel.platform.business.iotcom;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.service.xfyjNbRecevice.XfyjWaterReceviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName:  XFYJNBReceviceController   
 * @Description:   NB设备信号接收统一口径
 * 注意：本内容仅限于山东正晨技术股份有限公司内部传阅，禁止外泄以及用于其他的商业目
 */


@Controller
@RequestMapping(value ="/com/nbrecevice")
public class XfyjNbReceviceController {
	 private static final Logger log = LoggerFactory.getLogger(XfyjNbReceviceController.class);
	 	@Autowired
	 	private XfyjWaterReceviceService xfyjWaterReceviceService;

		/**
		 * 接收消防水压力表设备事件数据
		 */
		@CrossOrigin
		@ResponseBody
		@RequestMapping(value = "/handleWaterAlarmInfo", method = { RequestMethod.GET, RequestMethod.POST })
		public void handleWaterAlarmInfo(@RequestBody(required = false) JSONObject jsonObject)
		{
			log.info("开始接收消防水压力表信息...");
			xfyjWaterReceviceService.analysisWater(jsonObject);
		}
}
