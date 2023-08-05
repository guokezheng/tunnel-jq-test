package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyExport;
import com.tunnel.business.domain.event.SdStrategyModel;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.tunnel.business.utils.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public TableDataInfo<List<SdStrategy>> list(SdStrategy sdStrategy)
    {
        startPage();
        List<SdStrategy> list = sdStrategyService.selectSdStrategyList(sdStrategy);
        return getDataTable(list);
    }


    /**
     * 导出策略列表
     */
    @Log(title = "策略", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdStrategy sdStrategy)
    {
        if("1".equals(sdStrategy.getStrategyGroup())){//日常策略
            List<SdStrategy> list = sdStrategyService.selectSdStrategyList(sdStrategy);
            ExcelUtil<SdStrategy> util = new ExcelUtil<SdStrategy>(SdStrategy.class);
            ExcelUtil<SdStrategyExport> sdStrategyUtil = new ExcelUtil<SdStrategyExport>(SdStrategyExport.class);
            String SdStrategyName = "";
            if("0".equals(sdStrategy.getStrategyType())){
                SdStrategyName = "手动控制策略";
                List<SdStrategyExport> sdStrategyList = new ArrayList<>();
                list.forEach(SdStrategy ->{
                    SdStrategyExport sdStrategyExport = new SdStrategyExport();
                    sdStrategyExport.setStrategyName(SdStrategy.getStrategyName());
                    sdStrategyExport.setTunnels(SdStrategy.getTunnels());
                    sdStrategyExport.setFx(SdStrategy.getFx());
                    sdStrategyExport.setLx(SdStrategy.getLx());
                    sdStrategyExport.setStrategyInfo(SdStrategy.getStrategyInfo());
                    sdStrategyList.add(sdStrategyExport);
                });
                return sdStrategyUtil.exportExcel(sdStrategyList, SdStrategyName);
            }
            if("1".equals(sdStrategy.getStrategyType())){
                SdStrategyName = "定时控制策略";
            }
            if("2".equals(sdStrategy.getStrategyType())){
                SdStrategyName = "触发控制策略";
            }
            return util.exportExcel(list, SdStrategyName);
        }else{//控制策略
            List<SdStrategy> list = sdStrategyService.selectSdStrategyList(sdStrategy);
            ExcelUtil<SdStrategy> util = new ExcelUtil<SdStrategy>(SdStrategy.class);
            return util.exportExcel(list, "预警策略");
        }

    }

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

    @GetMapping(value = "/timeSharing/{tunnelId}")
    @ApiOperation("工作台分时控制抽屉")
    public Result<List<Map>> getTimeSharingInfo(@PathVariable("tunnelId") String tunnelId) {
        return Result.success(sdStrategyService.getTimeSharingInfo(tunnelId));
    }

    @GetMapping(value = "/timeSharing/updateControlTime")
    @ApiOperation("工作台分时控制抽屉修改控制时间")
    @Log(title = "控制策略分时控制", businessType = BusinessType.UPDATE)
    public Result updateControlTime(@RequestParam("strategyId") Long strategyId,@RequestParam("controlTime") String controlTime) {
        return Result.toResult(sdStrategyService.updateControlTime(strategyId,controlTime));
    }

    @GetMapping(value = "/workTriggerInfo/{tunnelId}")
    @ApiOperation("工作台阈值触发抽屉")
    public Result<List<Map>> workTriggerInfo(@PathVariable("tunnelId") String tunnelId) {
        return Result.success(sdStrategyService.workTriggerInfo(tunnelId));
    }

    @GetMapping(value = "/switch")
    @ApiOperation("控制策略开关")
    public Result strategySwitch(@RequestParam("strategyId") Long strategyId,@RequestParam("change") String change) {
        return Result.toResult(sdStrategyService.strategySwitch(strategyId,change));
    }

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

    @ApiOperation("预警处置一键执行")
    @GetMapping("/implementDisposalStrategy")
    public Result implementDisposalStrategy(@RequestParam("strategyId") Long strategyId,
                                            @RequestParam("eventId") Long eventId){
        return Result.success(sdStrategyService.implementDisposalStrategy(strategyId,eventId));
    }

    @ApiOperation("预警处置执行策略")
    @GetMapping("/implementDisposalStrategyRl")
    public Result implementDisposalStrategyRl(@RequestParam("rlId") Long rlId,
                                              @RequestParam("eventId") Long eventId){
        return Result.success(sdStrategyService.implementDisposalStrategyRl(rlId,eventId));
    }

    /**
     * 执行手动控制策略
     */
    @Log(title = "执行手动控制策略", businessType = BusinessType.OTHER)
    @GetMapping("/handleStrategy/{id}")
    public void handleStrategy(@PathVariable Long id) throws UnknownHostException {
    	sdStrategyService.handleStrategy(id);
    }

    /**
     * 查询预警事件触发策略
     * @param strategy
     * @return
     */
    @GetMapping("/getStrategyData")
    public AjaxResult getStrategyData(SdStrategy strategy){
        return sdStrategyService.getStrategyData(strategy);
    }

    /**
     * 查询所有定时策略详情信息
     */
    @GetMapping("/getSdStrategyAll")
    public TableDataInfo<List<SdStrategy>> getSdStrategyAll(SdStrategy sdStrategy)
    {
        startPage();
        List<SdStrategy> list = sdStrategyService.getSdStrategyAll(sdStrategy);
        return getDataTable(list);
    }
}
