package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdPlaceLocation;
import com.tunnel.business.service.dataInfo.ISdPlaceLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安装场所Controller
 * 
 * @author yanghousheng
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/location")
public class SdPlaceLocationController extends BaseController
{
    @Autowired
    private ISdPlaceLocationService sdPlaceLocationService;

    /**
     * 查询安装场所列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdPlaceLocation sdPlaceLocation)
    {
        startPage();
        List<SdPlaceLocation> list = sdPlaceLocationService.selectSdPlaceLocationList(sdPlaceLocation);
        return getDataTable(list);
    }

    /**
     * 导出安装场所列表
     */
    /*@Log(title = "安装场所", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdPlaceLocation sdPlaceLocation) throws IOException
    {
        List<SdPlaceLocation> list = sdPlaceLocationService.selectSdPlaceLocationList(sdPlaceLocation);
        ExcelUtil<SdPlaceLocation> util = new ExcelUtil<SdPlaceLocation>(SdPlaceLocation.class);
        util.exportExcel(response, list, "location");
    }*/

    /**
     * 获取安装场所详细信息
     */
    @GetMapping(value = "/{placeId}")
    public AjaxResult getInfo(@PathVariable("placeId") Long placeId)
    {
        return AjaxResult.success(sdPlaceLocationService.selectSdPlaceLocationById(placeId));
    }

    /**
     * 新增安装场所
     */
    @Log(title = "安装场所", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdPlaceLocation sdPlaceLocation)
    {
        return toAjax(sdPlaceLocationService.insertSdPlaceLocation(sdPlaceLocation));
    }

    /**
     * 修改安装场所
     */
    @Log(title = "安装场所", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdPlaceLocation sdPlaceLocation)
    {
        return toAjax(sdPlaceLocationService.updateSdPlaceLocation(sdPlaceLocation));
    }

    /**
     * 删除安装场所
     */
    @Log(title = "安装场所", businessType = BusinessType.DELETE)
	@DeleteMapping("/{placeIds}")
    public AjaxResult remove(@PathVariable Long[] placeIds)
    {
        return toAjax(sdPlaceLocationService.deleteSdPlaceLocationByIds(placeIds));
    }
}
