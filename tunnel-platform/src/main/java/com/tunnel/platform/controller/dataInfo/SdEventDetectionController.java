package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.dataInfo.SdEventDetection;
import com.tunnel.platform.service.dataInfo.ISdEventDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件监测记录Controller
 * 
 * @author liubaokui
 * @date 2021-04-21
 */
@RestController
@RequestMapping("/system/detection")
public class SdEventDetectionController extends BaseController
{
    @Autowired
    private ISdEventDetectionService sdEventDetectionService;

    /**
     * 查询事件监测记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:detection:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEventDetection sdEventDetection)
    {
        startPage();
        List<SdEventDetection> list = sdEventDetectionService.selectSdEventDetectionList(sdEventDetection);
        return getDataTable(list);
    }

    /**
     * 导出事件监测记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:detection:export')")
    @Log(title = "事件监测记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEventDetection sdEventDetection)
    {
        List<SdEventDetection> list = sdEventDetectionService.selectSdEventDetectionList(sdEventDetection);
        ExcelUtil<SdEventDetection> util = new ExcelUtil<SdEventDetection>(SdEventDetection.class);
        return util.exportExcel(list, "事件检测记录");
    }

    /**
     * 获取事件监测记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventDetectionService.selectSdEventDetectionById(id));
    }

    /**
     * 新增事件监测记录
     */
    @PreAuthorize("@ss.hasPermi('system:detection:add')")
    @Log(title = "事件监测记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventDetection sdEventDetection)
    {
        return toAjax(sdEventDetectionService.insertSdEventDetection(sdEventDetection));
    }

    /**
     * 修改事件监测记录
     */
    @PreAuthorize("@ss.hasPermi('system:detection:edit')")
    @Log(title = "事件监测记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventDetection sdEventDetection)
    {
        return toAjax(sdEventDetectionService.updateSdEventDetection(sdEventDetection));
    }

    /**
     * 删除事件监测记录
     */
    @PreAuthorize("@ss.hasPermi('system:detection:remove')")
    @Log(title = "事件监测记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventDetectionService.deleteSdEventDetectionByIds(ids));
    }
    
   /* @PostMapping("/loadPicture")
    public static AjaxResult loadPicture(String url) {
		String src = "";
		AjaxResult ajaxResult = null;
		try {
			src = ioToBase64(url);
		} catch (IOException e) {
			e.printStackTrace();
			src = new String("data:image/jpg;base64,");
		}
		return AjaxResult.success(src);
	}*/
    
   
    
    public static void main(String[] args) {
    	String str = "ftp://user:123456@10.7.97.7/videopicture/20210306/5_64_0_0_20210306125639000.jpg";
    	//loadPicture(str);
	}
}
