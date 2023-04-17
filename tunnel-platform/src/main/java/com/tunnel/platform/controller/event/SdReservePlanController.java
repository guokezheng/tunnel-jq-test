package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.oss.OssUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdJoinTypeFlow;
import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdReservePlanFile;
import com.tunnel.business.service.dataInfo.ISdEquipmentFileService;
import com.tunnel.business.service.event.ISdReservePlanFileService;
import com.tunnel.business.service.event.ISdReservePlanService;
import com.tunnel.platform.service.event.ISdStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 预案信息Controller
 *
 * @author xuebi
 * @date 2020-09-10
 */
@RestController
@RequestMapping("/plan")
@Api(tags = "预案信息")
public class SdReservePlanController extends BaseController {
    @Autowired
    private ISdReservePlanService sdReservePlanService;
    @Autowired
    private ISdReservePlanFileService sdReservePlanFileService;
    @Autowired
    private ISdEquipmentFileService sdEquipmentFileService;
    @Autowired
    private ISdStrategyService sdStrategyService;


    /**
     * 查询预案信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<List<SysRole>> list(SdReservePlan sdReservePlan) {
        startPage();
        List<SdReservePlan> list = sdReservePlanService.selectSdReservePlanList(sdReservePlan);
        return getDataTable(list);
    }

    /**
     * 导出预案信息列表
     */
    /*@PreAuthorize(hasPermi = "business:plan:export")
    @Log(title = "预案信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdReservePlan sdReservePlan) throws IOException
    {
        List<SdReservePlan> list = sdReservePlanService.selectSdReservePlanList(sdReservePlan);
        ExcelUtil<SdReservePlan> util = new ExcelUtil<SdReservePlan>(SdReservePlan.class);
        util.exportExcel(response, list, "plan");
    }*/

    /**
     * 获取预案信息详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取预案信息详细信息")
    @ApiImplicitParam(name = "id", value = "预案信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdReservePlan> getInfo(@PathVariable("id") Long id) {
        return Result.success(sdReservePlanService.selectSdReservePlanById(id));
    }

    /**
     * 新增预案信息
     */
    /*@PreAuthorize(hasPermi = "business:plan:add")
    @Log(title = "预案信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdReservePlan sdReservePlan)
    {
     //   return toAjax(sdReservePlanService.insertSdReservePlan(sdReservePlan));
        return toAjax(sdReservePlanService.insertSdReservePlan(null,null));
    }*/
    @Log(title = "预案信息")
    @PostMapping(value = "/addReservePlan")
    @ApiOperation("新增预案信息")
    public Result addReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan) {
        return Result.toResult(sdReservePlanService.insertSdReservePlan(file, sdReservePlan));
    }

    /**
     * 下载预案信息文件
     *
     * @param response
     * @param id
     */
    @Log(title = "预案信息")
    @PostMapping(value = "/{id}")
    public void downloadFile(HttpServletResponse response, @PathVariable("id") Long id) {
        SdReservePlanFile planFile = sdReservePlanFileService.selectSdReservePlanFileById(id);
        String path = planFile.getUrl();
        OssUtil.download(path, response);
    }


    /**
     * 修改预案信息
     *
     * @param file
     * @return
     */
   /* @PreAuthorize(hasPermi = "business:plan:edit")
    @Log(title = "预案信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdReservePlan sdReservePlan)
    {
        return toAjax(sdReservePlanService.updateSdReservePlan(sdReservePlan));
    }*/
    @Log(title = "预案信息")
    @PostMapping(value = "/updateReservePlan")
    @ApiOperation("修改预案信息")
    public Result updateReservePlan(MultipartFile[] file,SdReservePlan sdReservePlan) {
        return Result.toResult(sdReservePlanService.updateSdReservePlan(file, sdReservePlan, sdReservePlan.getRemoveIds()));
    }


    /**
     * 删除预案信息
     */
    @Log(title = "预案信息")
    @DeleteMapping("/{id}")
    @ApiOperation("删除预案信息")
    @ApiImplicitParam(name = "id", value = "预案信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long id) {
        return Result.toResult(sdReservePlanService.deleteSdReservePlanById(id));
    }


    /**
     * 导出事件类型预案流程关联列表
     */
    @Log(title = "预案信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdReservePlan sdReservePlan)
    {
        List<SdReservePlan> list = sdReservePlanService.selectSdReservePlanList(sdReservePlan);
        ExcelUtil<SdReservePlan> util = new ExcelUtil<SdReservePlan>(SdReservePlan.class);
        return util.exportExcel(list, "应急预案");
    }

//    /**
//     * 删除预案信息
//     */
//    @PreAuthorize(hasPermi = "business:plan:remove")
//    @Log(title = "预案信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{rlIds}")
//    public AjaxResult remove(@PathVariable String[] rlIds)
//    {
//        return toAjax(sdReservePlanService.deleteSdReservePlanByIds(rlIds));
//    }

//    @Log(title = "预案信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{rlIds}")
//    public AjaxResult remove(@PathVariable String[] rlIds)
//    {
//      return toAjax(sdReservePlanService.deleteSdReservePlanByIds(rlIds));
//    }

    /**
     * 根据预案id，查询策略信息列表
     */
    @GetMapping(value = "/s/{id}")
    @ApiOperation("根据预案id，查询策略信息列表")
    @ApiImplicitParam(name = "id", value = "预案ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result selectStrategyListByPlanId(@PathVariable("id") Long id) {
        return Result.success(sdReservePlanService.selectStrategyListByPlanId(id));
    }

    /**
     * 查询预案类型
     *
     * @return
     */
    @GetMapping("/getPlanCateGory")
    @ApiOperation("查询预案类型")
    public Result selectPlanCategory() {
        return Result.success(sdReservePlanService.selectPlanCategory());
    }

    /**
     * 根据分区id,预案类别 查询预案
     *
     * @param sdReservePlan
     * @return
     */
    @GetMapping("/getListBySId")
    public Result selectPlanBySid(SdReservePlan sdReservePlan) {
        return Result.success(sdReservePlanService.selectSdReservePlanBySubareaId(sdReservePlan));
    }

    @GetMapping("/getListBytId")
    @ApiOperation("根据隧道id查询预案")
    public Result selectPlanByTid(String tunnelId) {
        return Result.success(sdReservePlanService.selectSdReservePlanByTunnelId(tunnelId));
    }

    @Log(title = "预案一键执行")
    @GetMapping(value = "/implementPlan")
    public AjaxResult implementPlan(@RequestParam("planId") String planId,
                                @RequestParam("eventId") Long eventId) {
        return sdStrategyService.implementPlan(planId,eventId);
    }

    @Log(title = "环节执行")
    @GetMapping(value = "/implementProcess")
    public Result implementProcess(@RequestParam("processId") Long processId,
                                   @RequestParam("eventId") Long eventId) {
        int result = sdStrategyService.implementProcess(processId,eventId);
        return Result.toResult(result);
    }

    /**
     * 查询事件相关预案
     *
     * @param sdReservePlan
     * @return
     */
    @GetMapping("/getReservePlanData")
    @ApiOperation("查询事件相关预案")
    public AjaxResult getReservePlanData(SdReservePlan sdReservePlan){
        return sdReservePlanService.getReservePlanData(sdReservePlan);
    }
}
