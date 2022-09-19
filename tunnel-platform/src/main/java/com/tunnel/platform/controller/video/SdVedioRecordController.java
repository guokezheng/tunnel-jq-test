package com.tunnel.platform.controller.video;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.video.SdVedioRecord;
import com.tunnel.business.service.video.ISdVedioRecordService;
import com.tunnel.platform.controller.informationBoard.AjaxResultb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 历史视频信息Controller
 * 
 * @author xuebi
 * @date 2020-11-20
 */
@RestController
@RequestMapping("/vediorecord")
public class SdVedioRecordController extends BaseController
{
    @Autowired
    private ISdVedioRecordService sdVedioRecordService;

    /**
     * 查询历史视频信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdVedioRecord sdVedioRecord)
    {
        startPage();
        List<SdVedioRecord> list = sdVedioRecordService.selectSdVedioRecordList(sdVedioRecord);
        return getDataTable(list);
    }

    /**
     * 导出历史视频信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:export')")
    @Log(title = "历史视频信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdVedioRecord sdVedioRecord) throws IOException
    {
        List<SdVedioRecord> list = sdVedioRecordService.selectSdVedioRecordList(sdVedioRecord);
        ExcelUtil<SdVedioRecord> util = new ExcelUtil<SdVedioRecord>(SdVedioRecord.class);
        util.exportExcel(list, "历史视频信息");
    }

    /**
     * 获取历史视频信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResultb getInfo(@PathVariable("id") Long id)
    {
        return AjaxResultb.success(sdVedioRecordService.selectSdVedioRecordById(id));
    }
    @GetMapping(value = "/getVideos/{ids}")
    public AjaxResultb getVideos(@PathVariable("ids") String ids)
    {
        List<SdVedioRecord> list =new ArrayList<>();
        if (!"null".equals(ids) && ids!=null){
            if (ids.indexOf("，")==1){
                for (String id:ids.split("，")){
                    list.add(sdVedioRecordService.selectSdVedioRecordById(Long.parseLong(id)));
                }
            }else {
                for (String id:ids.split(",")){
                    list.add(sdVedioRecordService.selectSdVedioRecordById(Long.parseLong(id)));
                }
            }
        }
        return AjaxResultb.success(list);
    }
    /**
     * 新增历史视频信息
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:add')")
    @Log(title = "历史视频信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdVedioRecord sdVedioRecord)
    {
        return toAjax(sdVedioRecordService.insertSdVedioRecord(sdVedioRecord));
    }

    /**
     * 修改历史视频信息
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:edit')")
    @Log(title = "历史视频信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdVedioRecord sdVedioRecord)
    {
        return toAjax(sdVedioRecordService.updateSdVedioRecord(sdVedioRecord));
    }

    /**
     * 删除历史视频信息
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:remove')")
    @Log(title = "历史视频信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdVedioRecordService.deleteSdVedioRecordByIds(ids));
    }
    /**
     * 获取本地ip
     * @return
     * @throws UnknownHostException
     */
    @PreAuthorize("@ss.hasPermi('business:vediorecord:list')")
    @GetMapping("/getLocalIP")
    public String getLocalIP() throws UnknownHostException {
        String localIpByNetcard = InetAddress.getLocalHost().getHostAddress();
        return localIpByNetcard;
    }

}