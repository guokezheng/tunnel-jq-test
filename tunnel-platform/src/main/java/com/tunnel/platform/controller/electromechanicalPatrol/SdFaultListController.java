package com.tunnel.platform.controller.electromechanicalPatrol;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.HackLoopTableRenderPolicy;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.business.utils.work.CustomXWPFDocument;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 故障清单Controller
 * 
 * @author tjw
 * @date 2022-11-02
 */
@RestController
@RequestMapping("/fault/list")
public class SdFaultListController extends BaseController {
    @Autowired
    private ISdFaultListService sdFaultListService;

    @Autowired
    private ISdDevicesService isdDevicesService;

    @Autowired
    private SdFaultListMapper sdFaultListMapper;



    /**
     * 查询故障清单列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(SdFaultList sdFaultList) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdFaultList.setDeptId(deptId);
        startPage();
        List<SdFaultList> list = sdFaultListService.selectSdFaultListList(sdFaultList);
        return getDataTable(list);
    }

    /**
     * 导出故障清单列表
     */
    /* @PreAuthorize("@ss.hasPermi('system:list:export')")*/
    @Log(title = "故障清单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdFaultList sdFaultList) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdFaultList.setDeptId(deptId);
        List<SdFaultList> list = sdFaultListService.selectSdFaultListList(sdFaultList);
        ExcelUtil<SdFaultList> util = new ExcelUtil<SdFaultList>(SdFaultList.class);
        return util.exportExcel(list, "故障管理");
    }

    /**
     * 获取故障清单详细信息
     */
    /*   @PreAuthorize("@ss.hasPermi('system:list:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sdFaultListService.selectSdFaultListById(id));
    }

    /**
     * 新增故障清单
     */
    /* @PreAuthorize("@ss.hasPermi('system:list:add')")*/
    @Log(title = "故障清单", businessType = BusinessType.INSERT)
    @PostMapping
    /*public AjaxResult add(@RequestBody SdFaultList sdFaultList)*/
    public AjaxResult add(@RequestParam(name = "file", required = false) MultipartFile[] file, SdFaultList sdFaultList) {
        sdFaultList.setId(UUIDUtil.getRandom32BeginTimePK());// 文件关联ID
        return toAjax(sdFaultListService.insertSdFaultList(file, sdFaultList));
    }

    /**
     * 修改故障清单
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:edit')")*/
    @Log(title = "故障清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MultipartFile[] file, SdFaultList sdFaultList, @RequestParam("removeIds") String[] removeIds) {
        return toAjax(sdFaultListService.updateSdFaultList(file, sdFaultList, removeIds));

    }

    /**
     * 删除故障清单
     */
    /*  @PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "故障清单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sdFaultListService.deleteSdFaultListByIds(ids));
    }


    /**
     * 查询故障单检修记录信息
     */
    @PostMapping(value = "/getFaultRepairInfo")
    @ApiOperation("故障单检修记录信息")
    public Result getFaultRepairInfo(@RequestBody String faultId) {
        return Result.success(sdFaultListService.getFaultRepairInfo(faultId));
    }


    /**
     * 根据设备名称查询设备详情
     */
    @PostMapping(value = "/getEquipmentInfo")
    public Result getEquipmentInfo(@RequestBody String eqId) {
        //eqId = "JQ-JiNan-WenZuBei-CAM-RSU-012";//模拟数据
        return Result.success(isdDevicesService.getEquipmentInfo(eqId));
    }

    /**
     * 导出机电设备故障检修报告
     *
     * @param request
     * @param response
     * @param faultId
     */
    @RequestMapping("/exportFaultReport")
    public void exportFaultReport(HttpServletRequest request, HttpServletResponse response, String faultId) {
        SdFaultList fault = SpringUtils.getBean(SdFaultListMapper.class).exportFaultReport(faultId);
        //sdFaultListService.selectSdFaultListById(faultId);
        SdDevices devices = SpringUtils.getBean(ISdDevicesService.class).selectSdDevicesById(fault.getEqId());
        String eqName = devices.getEqName();
        String typeName = SpringUtils.getBean(ISdEquipmentTypeService.class).selectSdEquipmentTypeById(Long.valueOf(devices.getEqType())).getTypeName();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String faultFxtime = "";
        String faultRemovetime = "";
        String faultTbtime = "";
        try {
            Map<String, Object> faultMap = BeanUtils.describe(fault);
            if (fault.getFaultFxtime() != null) {
                faultFxtime = format.format(DateUtil.parse(fault.getFaultFxtime().toString()));
            }
            if (fault.getFaultRemoveTime() != null) {
                faultRemovetime = format.format(DateUtil.parse(fault.getFaultRemoveTime().toString()));
            }
            if (fault.getFaultTbtime() != null) {
                faultTbtime = format.format(DateUtil.parse(fault.getFaultTbtime().toString()));
            }
            AtomicInteger i = new AtomicInteger(1);
            if (faultMap.get("imgFileId") != null && !"".equals(faultMap.get("imgFileId"))) {
                String faultImgId = faultMap.get("imgFileId").toString();
                /*SdTrafficImage sdTrafficImage = new SdTrafficImage();
                sdTrafficImage.setBusinessId(faultImgId);*/
                String[] businessId = faultImgId.split(",");
                List<SdTrafficImage> imageList = SpringUtils.getBean(SdTrafficImageMapper.class).selectFaultImgFileLists(businessId);
                //List<SdTrafficImage> imageList = SpringUtils.getBean(SdTrafficImageMapper.class).selectFaultImgFileList(sdTrafficImage);
                if (imageList.size() > 0) {
                    List<PictureRenderData> photoList = new ArrayList<>();
                    for (int y = 0; y < imageList.size(); y++) {
                        String faultPhoto = "faultPhoto" + y;
                        // 图片的处理
                        String imageBaseStr = imageList.get(y).getImgUrl();
                        Base64.Decoder decoder = Base64.getDecoder();
                        // 去掉base64前缀
                        imageBaseStr = imageBaseStr.substring(imageBaseStr.indexOf(",", 1) + 1, imageBaseStr.length());
                        byte[] b = decoder.decode(imageBaseStr);
                        // 处理数据
                        for (int a = 0; a < b.length; ++a) {
                            if (b[a] < 0) {
                                b[a] += 256;
                            }
                        }
                        byte[] bytes = decoder.decode(imageBaseStr);
                        PictureRenderData pictureRenderData = Pictures.ofStream(new ByteArrayInputStream(bytes), PictureType.PNG)
                                .size(250, 160).create();
                        photoList.add(pictureRenderData);
                        faultMap.put(faultPhoto, pictureRenderData);
                    }
                }
            }
            List<SdPatrolList> list = SpringUtils.getBean(SdFaultListMapper.class).getFaultRepairReportInfo(faultId);
            List<Map<String, Object>> convertList = new ArrayList<>();
            //数据按巡查顺序排序
            //list.sort(Comparator.comparing(SdPatrolList::getXcSort));
            for (SdPatrolList obj : list) {
                //处理图片需将原对象类型转为Map
                Map<String, Object> map = BeanUtils.describe(obj);
                Object createTime = map.get("createTime");
                if (createTime != null) {
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("createTime", format.format(DateUtil.parse(createTime.toString())));
                }
                String imgFileId = obj.getImgFileId();
                if (StrUtil.isNotEmpty(imgFileId)) {
                    List<SdTrafficImage> imageLists = new ArrayList<>();
                    /*SdTrafficImage trafficImage = new SdTrafficImage();
                    trafficImage.setBusinessId(imgFileId);*/
                    if (imgFileId != null && !"".equals(imgFileId) && !"null".equals(imgFileId)) {
                        String[] businessId = imgFileId.split(",");
                        imageLists = SpringUtils.getBean(SdTrafficImageMapper.class).selectPatrolFaultImgFileList(businessId);
                    }

                    if (imageLists.size() > 0) {
                        for (int x = 0; x < imageLists.size(); x++) {
                            String photo = "photo" + x;
                            String imageBaseStr = imageLists.get(x).getImgUrl();
                            Base64.Decoder decoder = Base64.getDecoder();
                            imageBaseStr = imageBaseStr.substring(imageBaseStr.indexOf(",", 1) + 1, imageBaseStr.length());
                            byte[] b = decoder.decode(imageBaseStr);
                            for (int a = 0; a < b.length; ++a) {
                                if (b[a] < 0) {
                                    b[a] += 256;
                                }
                            }
                            byte[] bytes = decoder.decode(imageBaseStr);
                            PictureRenderData renderData = Pictures.ofStream(new ByteArrayInputStream(bytes), PictureType.PNG)
                                    .size(250, 126).create();
                            map.put(photo, renderData);
                        }


                    }
                }
                //序号
                map.put("remark", i.getAndIncrement());
                convertList.add(map);
            }
           /* ClassPathResource classPathResource = new ClassPathResource("patrolTemplate/faultReport.docx");
            String resource = classPathResource.getUrl().getPath();*/
            //渲染表格
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            //绑定数据
            Configure config = Configure.newBuilder().bind("detailList", policy).build();
            String finalFaultFxtime = faultFxtime;
            String finalFaultRemovetime = faultRemovetime;
            String finalFaultTbtime = faultTbtime;
            XWPFDocument document = new CustomXWPFDocument(new ClassPathResource("exporttemplate/faultReport.docx").getInputStream());
            XWPFTemplate template = XWPFTemplate.compile(document, config).render(
                    new HashMap<String, Object>() {{
                        put("faultBlock", convertList);
                        put("currentTime", DateUtils.getTime());
                        put("task", fault);
                        put("Fxtime", finalFaultFxtime);
                        put("RemoveTime", finalFaultRemovetime);
                        put("Tbtime", finalFaultTbtime);
                        put("eqName", eqName);
                        put("typeName", typeName);
                        List<String> photolist = new ArrayList<>();
                        for (String key : faultMap.keySet()) {
                            if (key.contains("faultPhoto")) {
                                photolist.add(key);
                            }
                        }
                        if (photolist != null && photolist.size() > 0) {
                            for (int q = 0; q < photolist.size(); q++) {
                                put(photolist.get(q), faultMap.get(photolist.get(q)));
                            }
                        }
                    /*if (faultMap.get("faultPhoto") != null) {

                        put("faultPhoto", faultMap.get("faultPhoto"));
                    }*/
                        Class<?> cClass = (Class<?>) SdFaultList.class;
                        Field[] fields = cClass.getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            if (fieldName.equals("serialVersionUID") || fieldName.equals("iFileList")) {
                                continue;
                            }
                            String upperChar = fieldName.substring(0, 1).toUpperCase();
                            String anotherStr = fieldName.substring(1);
                            String methodName = "get" + upperChar + anotherStr;
                            Method newMethod = cClass.getDeclaredMethod(methodName);
                            Object Value = newMethod.invoke(fault);
                            if (Value != null) {
                                put(fieldName, Value.toString());
                            }
                        }
                    }}
            );
//            String temDir = "D:/Electromechanical/" + File.separator + "file/word/";
            String temDir = RuoYiConfig.getProfile() + "/word/";

            //生成临时文件存放地址
            File file = new File(temDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            //生成文件名
            Long time = new Date().getTime();
            // 生成的word格式
            String formatSuffix = ".docx";
            // 拼接后的文件名
            String fileName = time + formatSuffix;//文件名  带后缀
            FileOutputStream fos = new FileOutputStream(temDir + fileName);
            template.write(fos);
            //=================生成word到设置浏览默认下载地址=================
            // 设置强制下载不打开
            response.setContentType("application/force-download");
//            response.setContentType("application/msword");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream out = response.getOutputStream();
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消防炮模拟故障接口
     */
    @GetMapping("/faultDemonFireMonitor")
    public void faultDemonFireMonitor() {
        String eqStatus = "3";
        //log.info("设备状态为故障");
        //故障位置处理
        //存 故障清单表sd_fault_list
        SdFaultList sdFaultList = new SdFaultList();
        sdFaultList.setId(UUIDUtil.getRandom32BeginTimePK());
        sdFaultList.setFaultTbtime(DateUtils.getNowDate());//故障填报时间
        sdFaultList.setCreateTime(DateUtils.getNowDate());// 创建时间
        sdFaultList.setTunnelId("JQ-JiNan-WenZuBei-MJY");//隧道
        sdFaultList.setFaultLocation("潍坊方向YK16+718");//设备位置   方向+桩号拼接
        sdFaultList.setFaultType("6");//故障类型  其他
        sdFaultList.setFaultEscalationType("1");//故障来源  系统上报
        sdFaultList.setFaultFxtime(DateUtils.getNowDate());//故障发现时间
        sdFaultList.setEqId("JQ-JiNan-WenZuBei-MJY-IFM-002");//设备id
        sdFaultList.setEqStatus("3");//设备状态  故障
        sdFaultList.setFaultLevel("0");//故障等级  一般
        sdFaultList.setFalltRemoveStatue("1");//故障消除状态  未消除
        sdFaultList.setFaultStatus("0");//故障状态  已发布
        sdFaultListMapper.insertSdFaultList(sdFaultList);
    }

}