package com.tunnel.platform.controller.vehicle;

/**
 * @author zhai
 * @date 2023/10/16
 */
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdTrafficVolume;
import com.tunnel.business.service.vehicle.SdTrafficVolumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author zhai
 * @date 2022/11/22
 */
@RestController
@RequestMapping("/trafficVolume")
@Api(tags = "车流量Controller")
public class SdTrafficVolumeController extends BaseController {

    @Autowired
    private SdTrafficVolumeService trafficVolumeService;

    @GetMapping("/list")
    @ApiOperation("查询车流量列表")
    public TableDataInfo list(SdTrafficVolume sdTrafficVolume){
        startPage();
        List<SdTrafficVolume> list = trafficVolumeService.selectTrafficVolumeList(sdTrafficVolume);
        return getDataTable(list);
    }

    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出车流量列表")
    public AjaxResult export(SdTrafficVolume sdTrafficVolume){
        List<SdTrafficVolume> list = trafficVolumeService.exportTrafficVolumeList(sdTrafficVolume);
        ExcelUtil<SdTrafficVolume> util = new ExcelUtil<SdTrafficVolume>(SdTrafficVolume.class);
        return util.exportExcel(list, "车流量数据");
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list){
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
}
