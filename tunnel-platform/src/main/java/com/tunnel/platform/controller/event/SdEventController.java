package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.service.event.ISdEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件管理Controller
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/event")
@Api(tags = "事件管理")
public class SdEventController extends BaseController
{
    @Autowired
    private ISdEventService sdEventService;

    /**
     * 查询事件管理列表
     */
    @GetMapping("/list")
    @ApiOperation("查询事件管理列表")
    public TableDataInfo<List<SysRole>> list(SdEvent sdEvent)
    {
        startPage();
        List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
        return getDataTable(list);
    }

    /**
     * 大屏查询事件报警列表
     */
    @GetMapping("/bigscreenEventList")
    @ApiOperation("大屏查询事件报警列表")
    public Result bigscreenEventList(SdEvent sdEvent)
    {
    	List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
    	return Result.success("成功", list);
    }

    /**
     * 导出事件管理列表
     */
    /*@Log(title = "事件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEvent sdEvent) throws IOException
    {
        List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
        ExcelUtil<SdEvent> util = new ExcelUtil<SdEvent>(SdEvent.class);
        util.exportExcel(response, list, "event");
    }*/

    /**
     * 获取事件管理详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取事件管理详细信息")
    @ApiImplicitParam(name = "id", value = "事件管理ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdEvent> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdEventService.selectSdEventById(id));
    }

    /**
     * 新增事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增事件管理")
    public Result add(@RequestBody SdEvent sdEvent)
    {
        return Result.toResult(sdEventService.insertSdEvent(sdEvent));
    }

    /**
     * 修改事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改事件管理")
    public Result edit(@RequestBody SdEvent sdEvent)
    {
        return Result.toResult(sdEventService.updateSdEvent(sdEvent));
    }

    /**
     * 删除事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除事件管理")
    @ApiImplicitParam(name = "ids", value = "需要删除的事件管理ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdEventService.deleteSdEventByIds(ids));
    }

    /**
     * 根据id查询事件 ---视频
     * @param id
     * @return
     */
    @GetMapping("/evntId")
    public AjaxResult getById(@RequestParam("id") Long id){
        return AjaxResult.success(sdEventService.getById(id));
    }

    /**
     * 预警事件查询全部
     * @return
     */
    @GetMapping("/getEvent")
    public AjaxResult getEvent(SdEvent sdEvent)
    {
        return AjaxResult.success(sdEventService.getEvent(sdEvent));
    }

    /**
     * 统计今日事件
     * @return
     */
    @GetMapping("/getTodayEventCount")
    @ApiOperation("统计今日事件")
    public Result getTodayEventCount() {
        return Result.success(sdEventService.getTodayEventCount());
    }

    /**
     * 根据事件属性获取所在分区ID
     * @param tunnelId
     * @param stakeNum
     * @param direction
     * @return
     */
    @GetMapping("/getSubareaByStakeNum")
    @ApiOperation("根据事件桩号获取所在的分区")
    public Result getSubareaByStakeNum(@RequestParam("tunnelId") String tunnelId,
                                       @RequestParam("stakeNum") String stakeNum,
                                       @RequestParam("direction")String direction){
        return Result.success(sdEventService.getSubareaByStakeNum(tunnelId,stakeNum,direction));
    }
}
