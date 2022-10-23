package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdComponent;
import com.tunnel.business.domain.dataInfo.SdEquipmentFile;
import com.tunnel.business.service.dataInfo.ISdComponentService;
import com.tunnel.business.service.dataInfo.ISdEquipmentFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 设备档案管理Controller
 * 
 * @author yanghousheng
 * @date 2020-11-18
 */
@RestController
@RequestMapping("/component")
@Api(tags = "设备档案管理")
public class SdComponentController extends BaseController
{
    @Autowired
    private ISdComponentService sdComponentService;
    @Autowired
    private ISdEquipmentFileService sdEquipmentFileService;

    /**
     * 查询设备档案管理列表
     */
    @GetMapping("/list")
    @ApiOperation("查询设备档案管理列表")
    public TableDataInfo<List<SdComponent>> list(SdComponent sdComponent)
    {
        startPage();
        List<SdComponent> list = sdComponentService.selectSdComponentList(sdComponent);
        return getDataTable(list);
    }
    /**
     * 导出设备档案管理列表
     */
   /* @Log(title = "设备档案管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdComponent sdComponent) throws IOException
    {
        List<SdComponent> list = sdComponentService.selectSdComponentList(sdComponent);
        ExcelUtil<SdComponent> util = new ExcelUtil<SdComponent>(SdComponent.class);
        util.exportExcel(response, list, "component");
    }*/

    /**
     * 获取设备档案管理详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备档案管理详细信息")
    @ApiImplicitParam(name = "id", value = "设备档案管理ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result<SdComponent> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdComponentService.selectSdComponentById(id));
    }

    /**
     * 新增设备档案管理
     */
    @Log(title = "设备档案管理", businessType = BusinessType.INSERT)
    /*@PostMapping*/
    @PostMapping(value = "/addSdComponent")
    @ApiOperation("新增设备档案管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentName", value = "设备名称", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "brand", value = "品牌", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "model", value = "型号", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "manufacturer", value = "生产厂家", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),

    })
    public Result add(@RequestParam("file") MultipartFile[] file,
    		             @RequestParam("equipmentName") String equipmentName,
                         @RequestParam("tunnelId") String tunnelId,
    		             @RequestParam("remark") String remark,
                         @RequestParam("brand") String brand,
                         @RequestParam("model") String model,
                         @RequestParam("manufacturer") String manufacturer,
    		             HttpServletRequest request)
    {
    	SdComponent sdComponent = new SdComponent();
    	sdComponent.setEquipmentName(equipmentName);
        sdComponent.setTunnelId(tunnelId);
        sdComponent.setBrand(brand);
        sdComponent.setModel(model);
        sdComponent.setManufacturer(manufacturer);
    	sdComponent.setRemark(remark);
        return Result.toResult(sdComponentService.insertSdComponent(file,sdComponent));
    }

    /**
     * 下载文件
     * @param response
     * @param id
     */
    @Log(title = "设备文档")
    @PostMapping(value = "/{id}")
   // @ApiOperation("下载文件")
  //  @ApiImplicitParam(name = "id", value = "设备档案文件ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public void downloadFile(HttpServletResponse response,@PathVariable("id") Long id) {
    	try {
    		SdEquipmentFile sdEquipmentFile = sdEquipmentFileService.selectSdEquipmentFileById(id);
    		String path = sdEquipmentFile.getUrl();
            File file = new File(path);
            InputStream fis;
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            String fileName = URLEncoder.encode(sdEquipmentFile.getFileName(),"UTF-8");
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
		};
	}
    /**
     * 修改预案信息
     * @param file
     * @param id
     * @return
     */
    @Log(title = "预案信息")
    @ApiOperation("修改预案信息")
    @PostMapping(value = "/updateComponentPlan")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ID", value = "需要删除的设备档案管理ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "equipmentName", value = "设备名称", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "brand", value = "品牌", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "model", value = "型号", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "manufacturer", value = "生产厂家", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class),
            @ApiImplicitParam(name = "removeIds", value = "需要删除的ID合集", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class),

    })
    public Result updateComponentPlan(MultipartFile[] file,
    								@RequestParam("id") Long id,
    		    		            @RequestParam("equipmentName") String equipmentName,
    		    		            @RequestParam("remark") String remark,
                                    @RequestParam("tunnelId") String tunnelId,
                                    @RequestParam("brand") String brand,
                                    @RequestParam("model") String model,
                                    @RequestParam("manufacturer") String manufacturer,
    		    		            @RequestParam("fileId") String fileId,
    								@RequestParam("removeIds") Long[] removeIds,
    								HttpServletRequest request
    								)
    {
    	SdComponent sdComponent = new SdComponent();
    	sdComponent.setId(id);
    	sdComponent.setEquipmentName(equipmentName);
    	sdComponent.setRemark(remark);
        sdComponent.setFileId(fileId);
        sdComponent.setManufacturer(manufacturer);
        sdComponent.setBrand(brand);
        sdComponent.setModel(model);
        sdComponent.setTunnelId(tunnelId);
    	return Result.toResult(sdComponentService.updateSdComponent(file,sdComponent,removeIds));
    }
    

    /**
     * 修改设备档案管理
     */
   /* @PreAuthorize("@ss.hasPermi('system:component:edit')")
    @Log(title = "设备档案管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdComponent sdComponent)
    {
        return toAjax(sdComponentService.updateSdComponent(sdComponent));
    }*/

    /**
     * 删除设备档案管理
     */
    @Log(title = "设备档案管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rlIds}")
    @ApiOperation("删除设备档案管理")
    @ApiImplicitParam(name = "rlIds", value = "需要删除的设备档案管理ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result remove(@PathVariable String[] rlIds)
    {
        return Result.toResult(sdComponentService.deleteSdComponentByIds(rlIds));
    }


    @DeleteMapping("/delbyid/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdComponentService.deleteSdComponentById(ids));
    }
}
