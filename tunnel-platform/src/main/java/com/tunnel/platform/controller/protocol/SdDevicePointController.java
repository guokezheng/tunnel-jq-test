package com.tunnel.platform.controller.protocol;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 设备点位状态详情Controller
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
@RestController
@RequestMapping("/point")
public class SdDevicePointController extends BaseController
{
    @Autowired
    private ISdDevicePointService sdDevicePointService;

    /**
     * 查询设备点位状态详情列表
     */
    @PreAuthorize("@ss.hasPermi('point:list')")
    @GetMapping("/list")
    @ApiOperation("询设备点位状态详情列表")
    public TableDataInfo list(SdDevicePoint sdDevicePoint)
    {
        startPage();
        List<SdDevicePoint> list = sdDevicePointService.selectSdDevicePointList(sdDevicePoint);
        return getDataTable(list);
    }

    /**
     * 导出设备点位状态详情列表
     */
    @PreAuthorize("@ss.hasPermi('point:export')")
    @Log(title = "设备点位状态详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出设备点位状态详情列表")
    public AjaxResult export(SdDevicePoint sdDevicePoint)
    {
        List<SdDevicePoint> list = sdDevicePointService.selectSdDevicePointList(sdDevicePoint);
        ExcelUtil<SdDevicePoint> util = new ExcelUtil<SdDevicePoint>(SdDevicePoint.class);
        return util.exportExcel(list, "设备点位状态详情数据");
    }

    /**
     * 获取设备点位状态详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('point:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备点位状态详情详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDevicePointService.selectSdDevicePointById(id));
    }

    /**
     * 新增设备点位状态详情
     */
    @PreAuthorize("@ss.hasPermi('point:add')")
    @Log(title = "设备点位状态详情", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增设备点位状态详情")
    public AjaxResult add(@RequestBody SdDevicePoint sdDevicePoint)
    {
        return toAjax(sdDevicePointService.insertSdDevicePoint(sdDevicePoint));
    }

    /**
     * 修改设备点位状态详情
     */
    @PreAuthorize("@ss.hasPermi('point:edit')")
    @Log(title = "设备点位状态详情", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改设备点位状态详情")
    public AjaxResult edit(@RequestBody SdDevicePoint sdDevicePoint)
    {
        return toAjax(sdDevicePointService.updateSdDevicePoint(sdDevicePoint));
    }

    /**
     * 删除设备点位状态详情
     */
    @PreAuthorize("@ss.hasPermi('point:remove')")
    @Log(title = "设备点位状态详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除设备点位状态详情")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDevicePointService.deleteSdDevicePointByIds(ids));
    }


    @Log(title = "设备点位管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ApiOperation("设备点位管理")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        if(file!=null){
            String str1 = file.getOriginalFilename();
            String str = str1.substring(str1.indexOf(".") + 1);
            System.out.println("文件类型为============"+str);
            if(!"xls".equals(str)&&!"xlsx".equals(str)){
                String message = "抱歉，导入失败，仅允许导入“xls”或“xlsx”格式文件！";
                return AjaxResult.success(message);
            }
        }
        ExcelUtil<SdDevicePoint> util = new ExcelUtil<SdDevicePoint>(SdDevicePoint.class);
        List<SdDevicePoint> devicePointList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sdDevicePointService.importSdDevices(devicePointList, updateSupport, operName);

        return AjaxResult.success(message);
    }
}
