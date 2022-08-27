package com.tunnel.platform.controller.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdInspection;
import com.tunnel.platform.domain.dataInfo.SdInspectionFile;
import com.tunnel.platform.service.dataInfo.ISdInspectionFileService;
import com.tunnel.platform.service.dataInfo.ISdInspectionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 巡视记录Controller
 *
 * @author liubaokui
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/system/inspection")
public class SdInspectionController extends BaseController
{
    @Autowired
    private ISdInspectionService sdInspectionService;
    @Autowired
    private ISdInspectionFileService iSdInspectionFileService;

    /**
     * 查询巡视记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdInspection sdInspection)
    {
        startPage();
        List<SdInspection> list = sdInspectionService.selectSdInspectionList(sdInspection);
        return getDataTable(list);
    }

    /**
     * 导出巡视记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:export')")
    @Log(title = "巡视记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdInspection sdInspection)
    {
        List<SdInspection> list = sdInspectionService.selectSdInspectionList(sdInspection);
        ExcelUtil<SdInspection> util = new ExcelUtil<SdInspection>(SdInspection.class);
        return util.exportExcel(list, "巡视记录");
    }

    /**
     * 获取巡视记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:query')")
    @GetMapping(value = "/{inspectionId}")
    public AjaxResult getInfo(@PathVariable("inspectionId") Long inspectionId) throws IOException {
        return AjaxResult.success(sdInspectionService.selectSdInspectionById(inspectionId));
    }

    /**
     * 新增巡视记录
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:add')")
    @Log(title = "巡视记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody SdInspection sdInspection)
    {
        return toAjax(sdInspectionService.insertSdInspection(sdInspection));
    }

    /**
     * 带文件上传功能新增接口
     * @param file
     * @param sdInspection
     */
    @PostMapping("/addInspection")
    public AjaxResult addInspection(@RequestParam("file") MultipartFile[] file, SdInspection sdInspection){
        return toAjax(sdInspectionService.addInspection(file, sdInspection));
    }

    @PostMapping(value = "/uploadPicture")
    public AjaxResult uploadPicture(@RequestParam("file") MultipartFile[] file) throws IOException {
        return AjaxResult.success(sdInspectionService.uploadPicture(file));
    }

    /**
     * 修改巡视记录
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:edit')")
    @Log(title = "巡视记录", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/update")
    public AjaxResult edit(@RequestBody SdInspection sdInspection)
    {
        return toAjax(sdInspectionService.updateSdInspection(sdInspection));
    }

    /**
     * 带修改文件功能修改接口
     * @param file
     * @param sdInspection
     * @param removeIds
     */
    @PostMapping(value = "/updateInspection")
    public AjaxResult updateInspection( MultipartFile[] file,
                                         SdInspection sdInspection,
                                         @RequestParam("removeIds") Long[] removeIds) {
        return toAjax(sdInspectionService.updateInspection(file,sdInspection,removeIds));
    }

    /**
     * 删除巡视记录
     */
    @PreAuthorize("@ss.hasPermi('system:inspection:remove')")
    @Log(title = "巡视记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inspectionIds}")
    public AjaxResult remove(@PathVariable Long[] inspectionIds)
    {
        return toAjax(sdInspectionService.deleteSdInspectionByIds(inspectionIds));
    }

    /**
     * 下载文件
     * @param response
     * @param id
     */
    @PostMapping(value = "/{id}")
    public void downloadFile(HttpServletResponse response, @PathVariable("id") Long id) {
        try {
            SdInspectionFile sdInspectionFile = iSdInspectionFileService.selectSdInspectionFileById(id);
            String path = sdInspectionFile.getUrl();
            File file = new File(path);
            InputStream fis;
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            String fileName = URLEncoder.encode(sdInspectionFile.getFileName(),"UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            response.addHeader("Access-Control-Allow-Origin", "*");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
