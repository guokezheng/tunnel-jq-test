package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.electromechanicalPatrol.SdTeamsList;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 设备类型Controller
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@RestController
@RequestMapping("/type")
@Api(tags = "设备类型")
@ApiSupport(order = 16)
public class SdEquipmentTypeController extends BaseController
{
    @Autowired
    private ISdEquipmentTypeService sdEquipmentTypeService;

    /**
     * 查询设备类型列表
     */
    @GetMapping("/list")
	@ApiOperation("查询设备类型列表")
    public TableDataInfo<List<SdEquipmentType>> list(SdEquipmentType sdEquipmentType)
    {
        startPage();
        List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeList(sdEquipmentType);
        return getDataTable(list);
    }

    /**
     * 查询当前录入设备类型
     */
    @GetMapping("/hasList")
	@ApiOperation("查询当前录入设备类型")
	@ApiImplicitParam(name = "tunnelId", value = "隧道id", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public TableDataInfo<List<SdEquipmentType>> hasList(String tunnelId)
    {
    	// 获取全部设备类型
        List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeHasList(tunnelId);
        return getDataTable(list);
    }

	/**
	 * 查询当前录入设备类型
	 */
	@GetMapping("/hasListGroupByBigType")
	@ApiOperation("查询当前录入设备类型")
	public TableDataInfo<List<SdEquipmentType>> hasListGroupByBigType()
	{
		// 获取全部设备类型
		List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeGroupByBigType();
		return getDataTable(list);
	}

	/**
	 * 根据类型查询列表
	 * @param bigType
	 * @return
	 */
	@ApiOperation("根据类型查询列表")
	@GetMapping("/hasListByBigType")
	public TableDataInfo<List<SdEquipmentType>> hasListByBigType(String bigType)
	{
		// 获取全部设备类型
		List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeByBigType(bigType);
		return getDataTable(list);
	}

	/**
	 * 获得设备类型和控制策略
	 * @param sdEquipmentType
	 * @return
	 */
	@ApiOperation("获得设备类型和控制策略")
	@GetMapping("/getTypeAndStrategy")
	public Result getTypeAndStrategy(SdEquipmentType sdEquipmentType) {
		List<Map> sdEquipmentTypes = sdEquipmentTypeService.selectTypeAndStrategy(sdEquipmentType);
		return Result.success(sdEquipmentTypes);
	}

    /**
     * 查询设备类型列表（非分页）
     */
    @GetMapping("/eqTypeList")
	@ApiOperation("查询设备类型列表（非分页）")
    public Result eqTypeList(SdEquipmentType sdEquipmentType)
    {
        return Result.success( sdEquipmentTypeService.selectSdEquipmentTypeList(sdEquipmentType));
    }

	/**
	 * 查询数据项中设备类型列表
	 *
	 * @param sdEquipmentType
	 * @return
	 */
	@GetMapping("/getHasItemEqTypeList")
	@ApiOperation("查询数据项中设备类型列表")
	public TableDataInfo<List<SdEquipmentType>> getHasItemEqTypeList(SdEquipmentType sdEquipmentType)
	{
		startPage();
		List<SdEquipmentType> list = sdEquipmentTypeService.selectHasItemEqTypeList(sdEquipmentType);
		return getDataTable(list);
	}


	/**
     * 获取设备类型详细信息
     */
    @GetMapping(value = "/{typeId}")
	@ApiOperation("获取设备类型详细信息")
    public Result<SdEquipmentType> getInfo(@PathVariable("typeId") Long typeId)
    {
        return Result.success(sdEquipmentTypeService.selectSdEquipmentTypeById(typeId));
    }


	@Log(title = "设备类型", businessType = BusinessType.INSERT)
	@PostMapping
	@ApiOperation("新增设备类型")
	public Result add(@RequestParam(value = "file", required = false) MultipartFile[] file,SdEquipmentType sdEquipmentType)
	{
	  	Integer num = sdEquipmentTypeService.selectExistSameType(sdEquipmentType);
	  	if(num > 0){
	  	return Result.error("已存在相同的设备类型名称或代号");
	}
	  	return Result.toResult(sdEquipmentTypeService.insertSdEquipmentType( file,sdEquipmentType));
	}

    /**
     * 修改设备类型
     */
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
	@ApiOperation("修改设备类型")
    public Result edit( MultipartFile[] file,
    		@RequestParam("typeId") Long typeId,
    		@RequestParam("iconWidth") String iconWidth,
    		@RequestParam("iconHeight") String iconHeight,
    		@RequestParam("iconFileId") String iconFileId,
    		@RequestParam("typeName") String typeName,
			@RequestParam("typeAbbr") String typeAbbr,
			@RequestParam("bigType") String bigType,
			@RequestParam("eqCategory") Long eqCategory,
			@RequestParam("eqSystem") String eqSystem,
    		@RequestParam("removeIds") Long[] removeIds,
    		@RequestParam("isControl") String isControl
    		)
    {
    	SdEquipmentType sdEquipmentType = new SdEquipmentType();
    	sdEquipmentType.setTypeId(typeId);
    	sdEquipmentType.setIconFileId(iconFileId);
    	sdEquipmentType.setTypeName(typeName);
    	sdEquipmentType.setTypeAbbr(typeAbbr);
    	sdEquipmentType.setIconWidth(iconWidth);
    	sdEquipmentType.setIconHeight(iconHeight);
		sdEquipmentType.setBigType(bigType);
		sdEquipmentType.setIsControl(isControl);
		sdEquipmentType.setEqCategory(eqCategory);
		sdEquipmentType.setEqSystem(eqSystem);

		Integer num = sdEquipmentTypeService.selectExistSameType(sdEquipmentType);
		if(num > 0){
			return Result.error("已存在相同的设备类型名称或代号");
		}
        return Result.toResult(sdEquipmentTypeService.updateSdEquipmentType(file,sdEquipmentType,removeIds));
    }

    /**
     * 删除设备类型
     */
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
	@ApiOperation("删除设备类型")
	@ApiImplicitParam(name = "typeIds", value = "需要删除的设备类型ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] typeIds)
    {
        return Result.toResult(sdEquipmentTypeService.deleteSdEquipmentTypeByIds(typeIds));
    }

	/**
	 *加载图片
	 * @param url
	 * @return
	 */
	@ApiOperation("加载图片")
    @PostMapping("/loadPicture")
    public AjaxResult loadPicture(String url) {
		String src = "";
		//区分内存位置
		url=url.replaceAll("/opt/suidao/file/uploadPath/upload","D:/uploadPath/upload");
//		url=url.replaceAll("E:","D:/");
		try {
			src = ioToBase64(url);
		} catch (IOException e) {
			e.printStackTrace();
			src = new String("data:image/jpg;base64,");
		}
		return AjaxResult.success(src);
	}

    public String ioToBase64(String url) throws IOException {
		String strBase64 = null;
		if (url == null || "".equals(url)) {
			//log.info("url转base64参数为空" + url);
			strBase64 = new String("data:image/jpg;base64,");
		} else {
			String fileName = url; // 源文件
			try {
				InputStream in = new FileInputStream(fileName);
				// in.available()返回文件的字节长度
				byte[] bytes = new byte[in.available()];
				// 将文件中的内容读入到数组中
				in.read(bytes);
				strBase64 = new String("data:image/jpg;base64," + Base64.getEncoder().encodeToString(bytes)); // 将字节流数组转换为字符串
				in.close();
			} catch (FileNotFoundException fe) {
				fe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return strBase64;
	}

	/**
	 * 导出
	 * @param sdEquipmentType
	 * @return
	 */
	@ApiOperation("导出")
	@Log(title = "设备图标", businessType = BusinessType.EXPORT)
	@GetMapping("/export")
	public AjaxResult export(SdEquipmentType sdEquipmentType)
	{
		List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeList(sdEquipmentType);
		ExcelUtil<SdEquipmentType> util = new ExcelUtil<SdEquipmentType>(SdEquipmentType.class);
		return util.exportExcel(list, "设备图标数据");
	}


}
