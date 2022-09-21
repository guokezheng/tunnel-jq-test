package com.tunnel.platform.controller.intelligentMonitoring;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagement;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementDTO;
import com.tunnel.business.service.intelligentMonitoring.ITrafficManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

/**
 * 特种车辆通行管理Controller
 *
 * @author ruoyi
 * @date 2021-12-01
 */
@RestController
@RequestMapping("/business/trafficManagement")
public class TrafficManagementController extends BaseController
{
    @Autowired
    private ITrafficManagementService trafficManagementService;

    /**
     * 查询特种车辆通行管理列表
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:list')")
    @GetMapping("/list")
    public TableDataInfo list(TrafficManagementDTO trafficManagementDTO)
    {
        startPage();
        List<TrafficManagement> list = trafficManagementService.selectTrafficManagementList(trafficManagementDTO);
        for (int i = 0;i < list.size();i++) {
            TrafficManagement trafficManagement = list.get(i);
            String photoIn64 = "";
            if (trafficManagement.getPhoto() == null || "".equals(trafficManagement.getPhoto())) {
                continue;
            }
            try {
                photoIn64 = ioToBase64(trafficManagement.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
                photoIn64 = new String("data:image/jpg;base64,");
            }
            trafficManagement.setPhoto(photoIn64);
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
     * 导出特种车辆通行管理列表
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:export')")
    @Log(title = "特种车辆通行管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TrafficManagementDTO trafficManagementDTO)
    {
        List<TrafficManagement> list = trafficManagementService.selectTrafficManagementList(trafficManagementDTO);
        ExcelUtil<TrafficManagement> util = new ExcelUtil<TrafficManagement>(TrafficManagement.class);
        return util.exportExcel(list, "特种车辆通行管理列表");
    }

    /**
     * 获取特种车辆通行管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(trafficManagementService.selectTrafficManagementById(id));
    }

    /**
     * 新增特种车辆通行管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:add')")
    @Log(title = "特种车辆通行管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TrafficManagement trafficManagement)
    {
        return toAjax(trafficManagementService.insertTrafficManagement(trafficManagement));
    }

    /**
     * 修改特种车辆通行管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:edit')")
    @Log(title = "特种车辆通行管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TrafficManagement trafficManagement)
    {
        return toAjax(trafficManagementService.updateTrafficManagement(trafficManagement));
    }

    /**
     * 删除特种车辆通行管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:remove')")
    @Log(title = "特种车辆通行管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(trafficManagementService.deleteTrafficManagementByIds(ids));
    }


    /**
     * 删除特种车辆通行管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagement:assign')")
    @Log(title = "特种车辆通行下发通知", businessType = BusinessType.INSERT)
    @PostMapping("/assign")
    public AjaxResult remove(@RequestBody TrafficManagementDTO trafficManagementDTO)
    {
        trafficManagementService.assignTrafficManagement(trafficManagementDTO.getId(), trafficManagementDTO.getUserIds());
        return AjaxResult.success();
    }
}
