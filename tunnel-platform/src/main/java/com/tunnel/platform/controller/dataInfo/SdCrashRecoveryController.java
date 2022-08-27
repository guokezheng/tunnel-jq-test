package com.tunnel.platform.controller.dataInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.dataInfo.SdCrashRecovery;
import com.tunnel.platform.service.dataInfo.ISdCrashRecoveryService;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

/**
 * 应急恢复Controller
 *
 * @author why
 * @date 2022-02-22
 */
@RestController
@RequestMapping("/dataInfo/recovery")
public class SdCrashRecoveryController extends BaseController
{
    @Autowired
    private ISdCrashRecoveryService sdCrashRecoveryService;

    /**
     * 查询应急恢复列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdCrashRecovery sdCrashRecovery)
    {
        startPage();
        List<SdCrashRecovery> list = sdCrashRecoveryService.selectSdCrashRecoveryList(sdCrashRecovery);
        for (int i = 0;i < list.size();i++) {
            SdCrashRecovery sdCrashRecovery1 = list.get(i);
            String srcBefore = "";
            String srcAfter = "";
            if (sdCrashRecovery1.getBeforeImage() == null) {
                continue;
            }
            try {
                if (sdCrashRecovery1.getBeforeImage() != null) {
                    srcBefore = ioToBase64(sdCrashRecovery1.getBeforeImage());
                }
                if (sdCrashRecovery1.getAfterImage() != null) {
                    srcAfter = ioToBase64(sdCrashRecovery1.getAfterImage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                srcBefore = new String("data:image/jpg;base64,");
                srcAfter = new String("data:image/jpg;base64,");
            }

            sdCrashRecovery1.setAfterImage(srcAfter);
            sdCrashRecovery1.setBeforeImage(srcBefore);
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
     * 导出应急恢复列表
     */
    @Log(title = "应急恢复", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdCrashRecovery sdCrashRecovery)
    {
        List<SdCrashRecovery> list = sdCrashRecoveryService.selectSdCrashRecoveryList(sdCrashRecovery);
        ExcelUtil<SdCrashRecovery> util = new ExcelUtil<SdCrashRecovery>(SdCrashRecovery.class);
        return util.exportExcel(list, "应急恢复数据");
    }

    /**
     * 获取应急恢复详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
    	SdCrashRecovery sdCrashRecovery = sdCrashRecoveryService.selectSdCrashRecoveryById(id);

    	String srcBefore = "";
    	String srcAfter = "";
		try {
			srcBefore = ioToBase64(sdCrashRecovery.getBeforeImage());
			srcAfter = ioToBase64(sdCrashRecovery.getAfterImage());
		} catch (IOException e) {
			e.printStackTrace();
			srcBefore = new String("data:image/jpg;base64,");
			srcAfter = new String("data:image/jpg;base64,");
		}

		sdCrashRecovery.setAfterImage(srcAfter);
		sdCrashRecovery.setBeforeImage(srcBefore);
        return AjaxResult.success(sdCrashRecovery);
    }

    /**
     * 新增应急恢复
     */
    @Log(title = "应急恢复", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file") MultipartFile[] file, SdCrashRecovery sdCrashRecovery) throws IOException {
        return toAjax(sdCrashRecoveryService.insertSdCrashRecovery(file,sdCrashRecovery));
    }

    /**
     * 修改应急恢复
     */
    @Log(title = "应急恢复", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdCrashRecovery sdCrashRecovery) throws IOException {
        return toAjax(sdCrashRecoveryService.updateSdCrashRecovery(null,sdCrashRecovery));
    }

    /**
     * 删除应急恢复
     */
    @Log(title = "应急恢复", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdCrashRecoveryService.deleteSdCrashRecoveryByIds(ids));
    }

    @GetMapping("/controlRecovery")
    public AjaxResult controlRecovery(SdCrashRecovery sdCrashRecovery) {
        return AjaxResult.success(sdCrashRecoveryService.controlRecovery(sdCrashRecovery));
    }
}
