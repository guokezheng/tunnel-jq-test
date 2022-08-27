package com.tunnel.platform.controller.videoevents;

import com.tunnel.platform.domain.videoevents.SdCreateDocker;
import com.tunnel.platform.service.videoevents.ISdCreateDockerService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.utils.util.Docker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tunnel.platform.utils.util.Docker.createDocker;
import static com.tunnel.platform.utils.util.Docker.delete;


/**
 * 容器表Controller
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
@RestController
@RequestMapping("/docker")
public class SdCreateDockerController extends BaseController
{
    @Autowired
    private ISdCreateDockerService sdCreateDockerService;

    /**
     * 查询容器表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdCreateDocker sdCreateDocker)
    {
        startPage();
        List<SdCreateDocker> list = sdCreateDockerService.selectSdCreateDockerList(sdCreateDocker);
        return getDataTable(list);
    }

    /**
     * 导出容器表列表
     */
    @Log(title = "容器表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdCreateDocker sdCreateDocker)
    {
        List<SdCreateDocker> list = sdCreateDockerService.selectSdCreateDockerList(sdCreateDocker);
        ExcelUtil<SdCreateDocker> util = new ExcelUtil<SdCreateDocker>(SdCreateDocker.class);
        return util.exportExcel(list, "detection");
    }

    /**
     * 获取容器表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdCreateDockerService.selectSdCreateDockerById(id));
    }

    /**
     * 新增容器表
     */
    @Log(title = "容器表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdCreateDocker sdCreateDocker)
    {
        Boolean docker = createDocker(sdCreateDocker);
        if (docker){
            return toAjax(sdCreateDockerService.insertSdCreateDocker(sdCreateDocker));
        }else {
            return toAjax(-1);
        }

    }

    /**
     * 修改容器表
     */
    @Log(title = "容器表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdCreateDocker sdCreateDocker)
    {
        if (delete(sdCreateDocker)){
            if (createDocker(sdCreateDocker)){
                return toAjax(sdCreateDockerService.updateSdCreateDocker(sdCreateDocker));
            }
        }
        return toAjax(-1);
    }

    /**
     * 删除容器表
     */
    @Log(title = "容器表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        for (Integer id: ids){
            SdCreateDocker sdCreateDocker = sdCreateDockerService.selectSdCreateDockerById(id);
            Boolean docker = delete(sdCreateDocker);
        }
        return toAjax(sdCreateDockerService.deleteSdCreateDockerByIds(ids));
    }
    /**
     * 初始化容器表
     */
    @Log(title = "容器表")
	@GetMapping("/dockerInit/{id}")
    public AjaxResult dockerInit(@PathVariable("id") Integer id)
    {
            SdCreateDocker sdCreateDocker = sdCreateDockerService.selectSdCreateDockerById(id);
            Boolean docker = Docker.dockerInit(sdCreateDocker);
            if (docker){
                return toAjax(1);
            }else {
                return toAjax(-1);
            }
    }
}
