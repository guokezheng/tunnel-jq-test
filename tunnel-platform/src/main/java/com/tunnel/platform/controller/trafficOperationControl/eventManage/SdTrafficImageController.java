package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.config.ServerConfig;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficImageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片信息Controller
 *
 * @author ruoyi
 * @date 2022-02-22
 */
@RestController
@RequestMapping("/traffic/image")
public class SdTrafficImageController extends BaseController
{
    @Autowired
    private ISdTrafficImageService sdTrafficImageService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:list')")
    @GetMapping("/list")
    @ApiOperation("查询图片信息列表")
    public TableDataInfo list(SdTrafficImage sdTrafficImage)
    {
        startPage();
        List<SdTrafficImage> list = sdTrafficImageService.selectSdTrafficImageList(sdTrafficImage);
        return getDataTable(list);
    }

    /**
     * 导出图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:export')")
    @Log(title = "图片信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出图片信息列表")
    public AjaxResult export(SdTrafficImage sdTrafficImage)
    {
        List<SdTrafficImage> list = sdTrafficImageService.selectSdTrafficImageList(sdTrafficImage);
        ExcelUtil<SdTrafficImage> util = new ExcelUtil<SdTrafficImage>(SdTrafficImage.class);
        return util.exportExcel(list, "图片信息数据");
    }

    /**
     * 获取图片信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:query')")
    @GetMapping(value = "/{imgId}")
    @ApiOperation("获取图片信息详细信息")
    public AjaxResult getInfo(@PathVariable("imgId") Long imgId)
    {
        return AjaxResult.success(sdTrafficImageService.selectSdTrafficImageByImgId(imgId));
    }

    /**
     * 新增图片信息
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:add')")
    @Log(title = "图片信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增图片信息")
    public AjaxResult add(@RequestBody SdTrafficImage sdTrafficImage)
    {
        return toAjax(sdTrafficImageService.insertSdTrafficImage(sdTrafficImage));
    }

    /**
     * 修改图片信息
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:edit')")
    @Log(title = "图片信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改图片信息")
    public AjaxResult edit(@RequestBody SdTrafficImage sdTrafficImage)
    {
        return toAjax(sdTrafficImageService.updateSdTrafficImage(sdTrafficImage));
    }

    /**
     * 删除图片信息
     */
    @PreAuthorize("@ss.hasPermi('traffic:image:remove')")
    @Log(title = "图片信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{imgIds}")
    @ApiOperation("删除图片信息")
    public AjaxResult remove(@PathVariable Long[] imgIds)
    {
        return toAjax(sdTrafficImageService.deleteSdTrafficImageByImgIds(imgIds));
    }

//    /**
//     * 上传图片
//     * @param file
//     * @return
//     * @throws IOException
//     */
//    @PostMapping("/uploadImg")
//    public AjaxResult uploadImg(@RequestParam("file") MultipartFile file) throws IOException
//    {
//        SdTrafficImage trafficImage = new SdTrafficImage();
//        // 上传文件路径
//        String imgPath = RuoYiConfig.getUploadPath();
//        // 上传并返回新文件名称
//        String imgName = FileUploadUtils.upload(imgPath, file);
//        //获取的url是否可以在前台直接展示
//        String url = serverConfig.getUrl() + imgName;
//
//        String fileName = file.getOriginalFilename();
//        trafficImage.setImgName(fileName);
//        trafficImage.setImgUrl(url);
//
//        sdTrafficImageService.insertSdTrafficImage(trafficImage);
//        return AjaxResult.success(trafficImage);
//    }


    /**
     * 上传图片
     * @param file
     * @param trafficImage
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImg")
    @ApiOperation("上传图片")
    public AjaxResult uploadImg(@RequestParam("file") MultipartFile file, SdTrafficImage trafficImage) throws IOException
    {
        // 上传文件路径
        String imgPath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String imgName = FileUploadUtils.upload(imgPath, file);
        //获取的url是否可以在前台直接展示
        String url = serverConfig.getUrl() + imgName;

        String fileName = file.getOriginalFilename();
        trafficImage.setImgName(fileName);
        trafficImage.setImgUrl(url);

        sdTrafficImageService.insertSdTrafficImage(trafficImage);
        return AjaxResult.success(trafficImage);
    }

    /**
     * 多图片上传
     * @return 返回json
     * @throws Exception
     */
    @ApiOperation("多图片上传")
    @RequestMapping(method = RequestMethod.POST, path = "/uploadMultiImage")
    public Map<String, Object> uploadMultiImage(MultipartHttpServletRequest multipartRequest) throws Exception {
        multipartRequest.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();
//        // 业务id
//        String fkId = multipartRequest.getParameter("fkId");

        // 多文件上传实现
        MultipartFile multipartFile = null;
        Map<String, MultipartFile> map = multipartRequest.getFileMap();
        for (MultipartFile value : map.values()) {
            multipartFile = value;
        }

        if (null != multipartFile) {
            String fileName = multipartFile.getOriginalFilename();
//            // 获取文件后缀
//            String suffix = fileName.substring(fileName.lastIndexOf("."));
//            // 随机生成上传目录中的文件名称
//            String id = UUID.randomUUID().toString();
            SdTrafficImage trafficImage = new SdTrafficImage();
            // 上传文件路径
            String imgPath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String imgName = FileUploadUtils.upload(imgPath, multipartFile);
            //获取的url是否可以在前台直接展示
            String url = serverConfig.getUrl() + imgName;
            String path = RuoYiConfig.getProfile() + imgName.replace("/profile","");
            trafficImage.setImgName(fileName);
//            trafficImage.setImgUrl(url);
            trafficImage.setImgUrl(path);

//            sdTrafficImageService.insertSdTrafficImage(trafficImage);
            result.put("image",trafficImage);
        }

        return result;
    }

    /**
     * 查询单个事件图片
     * @param businessId
     * @return
     */
    @GetMapping("/Image")
    @ApiOperation("查询单个事件图片")
    public AjaxResult getImage(@RequestParam("businessId") Long  businessId)
    {
        return AjaxResult.success(sdTrafficImageService.selectImageByBusinessId(businessId));
    }
}
