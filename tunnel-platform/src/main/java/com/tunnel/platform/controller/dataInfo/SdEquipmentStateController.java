package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 设备类型状态关系Controller
 *
 * @author gongfanfei
 * @date 2020-08-28
 */
@RestController
@RequestMapping("/eqTypeState")
@Api(tags = "设备状态")
public class SdEquipmentStateController extends BaseController
{
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;

    /**
     * 查询设备类型状态关系列表
     */
    @GetMapping("/list")
    @ApiOperation("查询设备类型状态关系列表")
    public TableDataInfo<List<SdEquipmentState>> list(SdEquipmentState sdEquipmentState)
    {
        startPage();
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        return getDataTable(list);
    }

    /**
     * 查询设备类型状态关系列表设备数据状态
     * @param sdEquipmentState
     * @return
     */
    @GetMapping("/getDataTypeList")
    @ApiOperation("查询设备类型状态关系列表设备数据状态 ")
    public TableDataInfo<List<SdEquipmentState>> getStatesByData(SdEquipmentState sdEquipmentState) {
        startPage();
        sdEquipmentState.setStateType("2");
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateListGroupByStateType(sdEquipmentState);
        return getDataTable(list);
    }

    /**
     * 查询设备类型状态关系列表设备运行状态
     * @param sdEquipmentState
     * @return
     */
    @GetMapping("/getRunTypeList")
    @ApiOperation("查询设备类型状态关系列表设备运行状态")
    public TableDataInfo<List<SdEquipmentState>> getStatesByRun(SdEquipmentState sdEquipmentState) {
        startPage();
        sdEquipmentState.setStateType("1");
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateListGroupByStateType(sdEquipmentState);
        return getDataTable(list);
    }


     /**
     * 查询设备类型状态关系列表（按设备类型分组查询）
     */
    @GetMapping("/getList")
    public TableDataInfo getGroupList(SdEquipmentState sdEquipmentState)
    {
        startPage();
        List<Map> list = sdEquipmentStateService.getGroupList(sdEquipmentState);
        return getDataTable(list);
    }

    /**
     * 查询设备类型状态关系列表
     */
    @GetMapping("/isControlList")
    @ApiOperation("查询设备类型状态关系列表")
    public TableDataInfo<List<SdEquipmentState>> isControlList(SdEquipmentState sdEquipmentState)
    {
        startPage();
        List<SdEquipmentState> list = sdEquipmentStateService.selectIsControlList(sdEquipmentState);
        return getDataTable(list);
    }


    /**
     * 导出设备类型状态关系列表
     */
  /*  @Log(title = "设备类型状态关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEquipmentState sdEquipmentState) throws IOException
    {
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        ExcelUtil<SdEquipmentState> util = new ExcelUtil<SdEquipmentState>(SdEquipmentState.class);
        util.exportExcel(response, list, "eqTypeState");
    }*/

    /**
     * 获取设备类型状态关系详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备类型状态关系详细信息")
    @ApiImplicitParam(name = "id", value = "设备类型状态关系ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result<SdEquipmentState> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdEquipmentStateService.selectSdEquipmentStateById(id));
    }

    /**
     * 根据设备类型获取设备类型可控制状态
     */
    @GetMapping(value = "getState/{typeId}")
    @ApiOperation("根据设备类型获取设备类型可控制状态")
    @ApiImplicitParam(name = "typeId", value = "类型id", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result< List<SdEquipmentState>> getState(@PathVariable("typeId") Long typeId)
    {
        SdEquipmentState se = new SdEquipmentState();
        se.setStateTypeId(typeId);
        se.setIsControl(1);
        return Result.success(sdEquipmentStateService.selectSdEquipmentStateList(se));
    }

    /**
     * 根据设备类型获取设备类型状态信息
     */
    @GetMapping(value = "getStates/{stateTypeId}/{stateType}")
    public AjaxResult getStates(@PathVariable("stateTypeId") Long typeId,@PathVariable("stateType") String stateType)
    {
        SdEquipmentState se = new SdEquipmentState();
        se.setStateTypeId(typeId);
        if (stateType.equals("1") || stateType.equals("2")) {
            se.setStateType(stateType);
        }
        return AjaxResult.success(sdEquipmentStateService.selectDropSdEquipmentStateListByTypeId(se));
    }

    /**
     * 新增设备类型状态关系
     */
//    @PreAuthorize(hasPermi = "system:eqTypeState:add")
//    @Log(title = "设备类型状态关系", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SdEquipmentState sdEquipmentState)
//    {
//        return toAjax(sdEquipmentStateService.insertSdEquipmentState(sdEquipmentState));
//    }
//    @Log(title = "设备类型状态关系", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestParam("file") MultipartFile[] file,
//    		SdEquipmentState sdEquipmentState)
//    {
//
//    	return toAjax(sdEquipmentStateService.insertSdEquipmentState(file,sdEquipmentState));
//    }

    /**
     * 添加设备类型状态(单个)
     * @param file
     * @param sdEquipmentState
     * @return
     */
    @ApiOperation("新增设备类型状态关系")
    @Log(title = "设备类型状态关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file") MultipartFile[] file, SdEquipmentState sdEquipmentState) {
      return toAjax(sdEquipmentStateService.insertSdEquipmentState(file,sdEquipmentState));
    }

    /**
     * 添加设备类型图片(返回ID)
     * @param file
     * @return
     */
    @ApiOperation("添加设备类型图片")
    @PostMapping("/addFiles")
    public String addFile(@RequestParam("file") MultipartFile[] file) {
        return sdEquipmentStateService.insertSdEquipmentStateIconFile(file);
    }

    /**
     * 删除设备类型图片(根据图标文件ID)
     * @param id
     * @returnR
     */
    @ApiOperation("删除设备类型图片")
    @DeleteMapping("/delFiles/{id}")
    public AjaxResult delFile(@PathVariable Long id) {
        return toAjax(sdEquipmentStateService.delSdEquipmentStateIconFileById(id));
    }

    /**
     * 批量新增设备类型状态关系
     * @param sdEquipmentStates
     * @return
     */
    @ApiOperation("批量新增设备类型状态关系")
    @PostMapping("/addList")
    public AjaxResult addList(@RequestBody List<SdEquipmentState> sdEquipmentStates) {
        return toAjax(sdEquipmentStateService.insertSdEquipmentStateList(sdEquipmentStates));
    }


    /**
     * 修改设备类型状态关系
     */
//    @PreAuthorize(hasPermi = "system:eqTypeState:edit")
//    @Log(title = "设备类型状态关系", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody SdEquipmentState sdEquipmentState)
//    {
//        return toAjax(sdEquipmentStateService.updateSdEquipmentState(sdEquipmentState));
//    }
    @Log(title = "设备类型状态关系", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改设备类型状态关系")
    @ApiImplicitParam(name = "removeIds", value = "ids 为要删除的sd_equipment_state_icon_file id数组", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result edit(MultipartFile[] file,
    		SdEquipmentState sdEquipmentState,
    		@RequestParam("removeIds") Long[] removeIds)
    {
        return Result.toResult(sdEquipmentStateService.updateSdEquipmentState(file,sdEquipmentState,removeIds));
    }

    /**
     * 批量修改设备类型关系
     * @param sdEquipmentStates
     * @return
     */
    @ApiOperation("批量修改设备类型关系")
    @PutMapping("/updateStates")
    public Result updateStates(@RequestBody List<SdEquipmentState> sdEquipmentStates) {
        return Result.toResult(sdEquipmentStateService.updateSdEquipmentStates( sdEquipmentStates ));
    }

    /**
     * 批量删除设备类型状态关系
     */
    @Log(title = "设备类型状态关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除设备类型状态关系")
    @ApiImplicitParam(name = "ids", value = "需要删除的设备类型状态关系ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdEquipmentStateService.deleteSdEquipmentStateByIds(ids));
    }

    /**
     * 根据设备id删除设备状态的接口
     * @param id
     * @return
     */
    @ApiOperation("根据设备状态id删除设备状态")
    @DeleteMapping("/delState/{id}")
    public Result delStateById(@PathVariable Long id) {
        return Result.toResult(sdEquipmentStateService.deleteSdEquipmentStateById(id));
    }

    /**
     * 根据设备类型删除设备状态的接口
     * @param eqTypeId
     * @return
     */
    @ApiOperation("根据设备类型删除设备状态的接口")
    @DeleteMapping("/delStatesByTypeId/{eqTypeId}")
    public Result delStatesByTypeId(@PathVariable Long[] eqTypeId ) {
        return Result.toResult(sdEquipmentStateService.deleteSdEquipmentStateByTypeId(eqTypeId));
    }
}
