package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.SdPictureUpload;
import com.tunnel.business.service.informationBoard.ISdPictureUploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

/**
 * 情报板使用图片Controller
 *
 * @author ruoyi
 * @date 2022-03-22
 */
@RestController
@RequestMapping("/system/upload")
public class SdPictureUploadController extends BaseController
{
    @Autowired
    private ISdPictureUploadService sdPictureUploadService;

    /**
     * 查询情报板使用图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:list')")
    @GetMapping("/list")
    @ApiOperation("查询情报板使用图片列表")
    public TableDataInfo list(SdPictureUpload sdPictureUpload)
    {
        startPage();
        List<SdPictureUpload> list = sdPictureUploadService.selectSdPictureUploadList(sdPictureUpload);
        for (int i = 0;i < list.size();i++) {
            SdPictureUpload pictureUpload = list.get(i);
            String pictureUrl = "";
            if (pictureUpload.getPictureUrl() == null) {
                continue;
            }
            try {
                if (pictureUpload.getPictureUrl() != null) {
                    String picPath = FileUploadUtils.getDefaultBaseDir() + pictureUpload.getPictureUrl();

                    //pictureUrl = ioToBase64(pictureUpload.getPictureUrl());
                    pictureUrl = ioToBase64(picPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                pictureUrl = new String("data:image/jpg;base64,");
            }
            pictureUpload.setPictureUrl(pictureUrl);
        }
        return getDataTable(list);
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
     * 导出情报板使用图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:export')")
    @Log(title = "情报板使用图片", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出情报板使用图片列表")
    public AjaxResult export(SdPictureUpload sdPictureUpload)
    {
        List<SdPictureUpload> list = sdPictureUploadService.selectSdPictureUploadList(sdPictureUpload);
        ExcelUtil<SdPictureUpload> util = new ExcelUtil<SdPictureUpload>(SdPictureUpload.class);
        return util.exportExcel(list, "情报板使用图片数据");
    }

    /**
     * 获取情报板使用图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取情报板使用图片详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        SdPictureUpload sdPictureUpload = sdPictureUploadService.selectSdPictureUploadById(id);
        String pictureUrl = "";
        if (sdPictureUpload.getPictureUrl() != null) {
            try {
                if (sdPictureUpload.getPictureUrl() != null) {
                    pictureUrl = ioToBase64(FileUploadUtils.getDefaultBaseDir() + sdPictureUpload.getPictureUrl());
                }
            } catch (IOException e) {
                e.printStackTrace();
                pictureUrl = new String("data:image/jpg;base64,");
            }
            sdPictureUpload.setPictureUrl(pictureUrl);
        }
        return AjaxResult.success(sdPictureUpload);
    }

    /**
     * 新增情报板使用图片
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:add')")
    @Log(title = "情报板使用图片", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增情报板使用图片")
    public AjaxResult add(MultipartFile[] file,
                          @RequestParam("pictureName") String pictureName,
                          @RequestParam("imageRemark") String imageRemark,
                          @RequestParam("imageWidth") String imageWidth,
                          @RequestParam("imageHeight") String imageHeight,
                          @RequestParam("imageType") String imageType,
                          @RequestParam("vmsSize") String vmsSize,
                          @RequestParam("speed") String speed,
                          @RequestParam("deleteflag") String deleteflag) throws IOException {
        if (file == null) {
            throw new RuntimeException("图片不能为空");
        }
        if (vmsSize.length() > 60) {
            throw new RuntimeException("图片分辨率输入过长，请重新输入");
        }
        if (imageRemark.length() > 60) {
            throw new RuntimeException("图片备注输入过长，请重新输入");
        }
        if (speed.length() > 60) {
            throw new RuntimeException("速度输入过长，请重新输入");
        }
        SdPictureUpload sdPictureUpload = new SdPictureUpload();
        sdPictureUpload.setPictureName(pictureName.equals("null")?null:pictureName);
        sdPictureUpload.setImageRemark(imageRemark.equals("null")?null:imageRemark);
        sdPictureUpload.setImageWidth(imageWidth.equals("null")?null:imageWidth);
        sdPictureUpload.setImageHeight(imageHeight.equals("null")?null:imageHeight);
        sdPictureUpload.setImageType(imageType.equals("null")?null:imageType);
        sdPictureUpload.setVmsSize(vmsSize.equals("null")?null:vmsSize);
        sdPictureUpload.setSpeed(speed.equals("null")?null:speed);
        sdPictureUpload.setDeleteflag(deleteflag.equals("null")?null:deleteflag);
        return toAjax(sdPictureUploadService.insertSdPictureUpload(file,sdPictureUpload));
    }

    /**
     * 修改情报板使用图片
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:edit')")
    @Log(title = "情报板使用图片", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改情报板使用图片")
    public AjaxResult edit(MultipartFile[] file,
                           @RequestParam("id") Long id,
                           @RequestParam("pictureName") String pictureName,
                           @RequestParam("imageRemark") String imageRemark,
                           @RequestParam("imageWidth") String imageWidth,
                           @RequestParam("imageHeight") String imageHeight,
                           @RequestParam("imageType") String imageType,
                           @RequestParam("vmsSize") String vmsSize,
                           @RequestParam("speed") String speed,
                           @RequestParam("deleteflag") String deleteflag) throws IOException {
        if (vmsSize.length() > 60) {
            throw new RuntimeException("图片分辨率输入过长，请重新输入");
        }
        if (imageRemark.length() > 60) {
            throw new RuntimeException("图片备注输入过长，请重新输入");
        }
        if (speed.length() > 60) {
            throw new RuntimeException("速度输入过长，请重新输入");
        }
        SdPictureUpload sdPictureUpload = new SdPictureUpload();
        sdPictureUpload.setId(id);
        sdPictureUpload.setPictureName(pictureName.equals("null")?null:pictureName);
        sdPictureUpload.setImageRemark(imageRemark.equals("null")?null:imageRemark);
        sdPictureUpload.setImageWidth(imageWidth.equals("null")?null:imageWidth);
        sdPictureUpload.setImageHeight(imageHeight.equals("null")?null:imageHeight);
        sdPictureUpload.setImageType(imageType.equals("null")?null:imageType);
        sdPictureUpload.setVmsSize(vmsSize.equals("null")?null:vmsSize);
        sdPictureUpload.setSpeed(speed.equals("null")?null:speed);
        sdPictureUpload.setDeleteflag(deleteflag.equals("null")?null:deleteflag);
        return toAjax(sdPictureUploadService.updateSdPictureUpload(file,sdPictureUpload));
    }

    /**
     * 删除情报板使用图片
     */
    @PreAuthorize("@ss.hasPermi('system:templateImage:remove')")
    @Log(title = "情报板使用图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除情报板使用图片")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdPictureUploadService.deleteSdPictureUploadByIds(ids));
    }
}
