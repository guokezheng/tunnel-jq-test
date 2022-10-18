package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyModel;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.tunnel.business.utils.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制策略Controller
 *
 * @author gongfanfei
 * @date 2020-08-27
 */
@RestController
@RequestMapping("/strategy")
@Api(tags = "控制策略")
public class SdStrategyController extends BaseController
{
    @Autowired
    private ISdStrategyService sdStrategyService;

    /**
     * 查询控制策略列表
     */
    @GetMapping("/list")
    @ApiOperation("查询控制策略列表")
    public TableDataInfo<List<SdStrategy>> list(SdStrategy sdStrategy)
    {
        startPage();
        List<SdStrategy> list = sdStrategyService.selectSdStrategyList(sdStrategy);
        return getDataTable(list);
    }

    /**
     * 导出控制策略列表
     */
   /* @PreAuthorize(hasPermi = "system:strategy:export")
    @Log(title = "控制策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdStrategy sdStrategy) throws IOException
    {
        List<SdStrategy> list = sdStrategyService.selectSdStrategyList(sdStrategy);
        ExcelUtil<SdStrategy> util = new ExcelUtil<SdStrategy>(SdStrategy.class);
        util.exportExcel(response, list, "strategy");
    }*/

    /**
     * 获取控制策略详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取控制策略详细信息")
    @ApiImplicitParam(name = "id", value = "控制策略ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdStrategy> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdStrategyService.selectSdStrategyById(id));
    }

//    @GetMapping(value = "/getTimeSharing/{id}")
//    @ApiOperation("分时控制数据回填接口")
//    @ApiImplicitParam(name = "id", value = "控制策略ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
//    public Result<Map> getTimeSharingInfo(@PathVariable("id") Long id)
//    {
//        return Result.success(sdStrategyService.selectSdStrategyById(id));
//    }

    /**
     * 根据策略id查询控制策略
     * @param id
     * @return
     */
    @GetMapping("/getStrategyById")
    @ApiOperation("通过id查询控制策略")
    public Result getStrategyById(String id) {
        SdStrategy sdStrategy = new SdStrategy();
        sdStrategy.setId(Long.parseLong(id));
        return Result.success(sdStrategyService.selectSdStrategyList(sdStrategy));
    }

    /**
     * 新增控制策略
     */
    @Log(title = "控制策略", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增控制策略")
    public Result add(@RequestBody SdStrategy sdStrategy)
    {
        return Result.toResult(sdStrategyService.insertSdStrategy(sdStrategy));
    }

    /**
     * 修改控制策略
     */
    @Log(title = "控制策略", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改控制策略")
    public Result edit(@RequestBody SdStrategy sdStrategy)
    {
        return Result.toResult(sdStrategyService.updateSdStrategy(sdStrategy));
    }

    /**
     * 删除控制策略
     */
    @Log(title = "控制策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    @ApiOperation("删除控制策略")
    @ApiImplicitParam(name = "rlIds", value = "需要删除的控制策略关系ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long id)
    {
        return Result.toResult(sdStrategyService.deleteSdStrategyById(id));
    }

    /**
     * 新增控制策略
     */
    @Log(title = "控制策略", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addStrategysInfo")
    @ApiOperation("新增控制策略")
    public Result addStrategysInfo(@RequestBody SdStrategyModel sdStrategymodel)
    {
        return Result.toResult(sdStrategyService.addStrategysInfo(sdStrategymodel));
    }

    /**
     *
     * 修改控制策略
     */
    @Log(title = "控制策略", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateStrategysInfo")
    @ApiOperation("修改控制策略")
    public Result updateStrategysInfo(@RequestBody SdStrategyModel sdStrategymodel)
    {
        return Result.toResult(sdStrategyService.updateSdStrategyInfo(sdStrategymodel));
    }
    /**
     * 获取guid
     * @return
     */
    @GetMapping(value = "/getGuid")
    @ApiOperation("获取guid")
    public String getGuid()
    {
    	String guid = UUIDUtil.getRandom32BeginTimePK();
    	return guid;
    }

    /**
     * 手动控制策略
     */
    @Log(title = "执行手动控制策略", businessType = BusinessType.OTHER)
    @GetMapping("/handleStrategy/{id}")
    public void handleStrategy(@PathVariable Long id)
    {
    	sdStrategyService.handleStrategy(id);
    }

}
