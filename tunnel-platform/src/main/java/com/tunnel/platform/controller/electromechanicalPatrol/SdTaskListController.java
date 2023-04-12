package com.tunnel.platform.controller.electromechanicalPatrol;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.date.DateUtil;

import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.HackLoopTableRenderPolicy;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.business.datacenter.domain.dataReport.TaskStatus;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskOpt;
import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficImageService;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.business.utils.work.CustomXWPFDocument;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 巡查任务Controller
 *
 * @author tjw
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/task/list")
public class SdTaskListController extends BaseController
{
    @Autowired
    private ISdTaskListService sdTaskListService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdFaultListService sdFaultListService;


    /**
     * 查询巡查任务列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:list')")*/
    @GetMapping("/list")
    public TableDataInfo<List<SdTaskList>>list(SdTaskList sdTaskList)
    {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdTaskList.setDeptId(deptId);
        //String status = "0,1";
        /*if(sdTaskList.getTaskStatus()!=null&&!"".equals(sdTaskList.getTaskStatus())&& TaskStatus.YICHAOSHI.getCode().equals(sdTaskList.getTaskStatus())) {
            sdTaskList.setTaskStatus(status);
        }*/
        startPage();
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        return getDataTable(list);
    }






    /**
     * 导出巡查任务列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:export')")*/
    @Log(title = "巡查任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTaskList sdTaskList)
    {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdTaskList.setDeptId(deptId);
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        ExcelUtil<SdTaskList> util = new ExcelUtil<SdTaskList>(SdTaskList.class);
        return util.exportExcel(list, "巡查任务");
    }



    /**
     * 获取修改任务信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(id);
        Map patrolMap = sdTaskListService.getUpdatePatrolLists(id);
        //List<SdPatrolList> patrolLists = sdTaskListService.getUpdatePatrolLists(id);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.putAll(patrolMap);
        return Result.success(map);
    }


    /**
     *新增巡查任务
     * @param sdTaskList
     * @param
     * @return
     */

    @PostMapping("/addTask")
    public AjaxResult addTask(SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.insertSdTaskList(sdTaskList));
    }

    /**
     *修改巡查任务
     * @param sdTaskList
     * @param
     * @return
     */

    @PostMapping("/updateTask")
    public AjaxResult updateTask(SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.updateSdTaskList(sdTaskList));
    }

    /**
     * 废止巡查任务
     *
     * @return
     */
    @PostMapping("/abolishSdTaskList")
    public AjaxResult abolishSdTaskList(@RequestBody String id)
    {
        return toAjax(sdTaskListService.abolishSdTaskList(id));
    }

    /**
     * 删除巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "巡查任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String ids)
    {
        return toAjax(sdTaskListService.deleteSdTaskListByIds(ids));
    }

    /**
     * 获取单位隧道所树形结构
     * @return
     */
    @PostMapping("/treeselect")
    @ApiOperation("获取隧道树形结构")
    public Result treeselect(@RequestBody String tunnelId)
    {
        String deptId = String.valueOf(SecurityUtils.getDeptId());
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        //List<SysDept> depts = deptService.selectTunnelDeptList(deptId);
        String ssdw = tunnelsService.selectTunnelDept(tunnelId);
        List<SysDeptTunnel>deptTunnels = new ArrayList<>();
        List<SysDept> depts = new ArrayList<>();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if(user.isAdmin())
            depts = deptService.selectTunnelDeptListBydw(ssdw);
        else{
            depts = deptService.selectTunnelDeptList(deptId);
        }

        List<SdTunnels> tunnels = tunnelsService.selectTunnelList(deptId,tunnelId);
        if(depts!=null&&depts.size()>0){
            for(int i = 0;i<depts.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId((depts.get(i).getDeptId()).toString());
                deptTunnel.setDeptName(depts.get(i).getDeptName());
                deptTunnel.setParentId((depts.get(i).getParentId()).toString());
                deptTunnel.setParentName(depts.get(i).getParentName());
                deptTunnels.add(deptTunnel);
            }
        }
        if(tunnels!=null&&tunnels.size()>0){
            for(int i = 0;i<tunnels.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(tunnels.get(i).getTunnelId());
                deptTunnel.setDeptName(tunnels.get(i).getTunnelName());
                deptTunnel.setParentId((tunnels.get(i).getDeptId()).toString());
                deptTunnel.setParentName(tunnels.get(i).getTunnelStationName());
                deptTunnels.add(deptTunnel);
            }
        }
        sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels);
        return Result.success(sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels));
    }


    /**
     * 获取隧道列表
     * @return
     */
    @PostMapping("/getTunnelList")
    public Result getTunnelList(@RequestBody String deptId){
        List<SdTunnels> tunnels = tunnelsService.selectTunnelsList(deptId);
        return Result.success(tunnels);
    }


    /**
     * 查询设备列表
     * @param tunnelId
     * @return
     */
    @GetMapping("/getDevicesList")
    public TableDataInfo getDevicesList(String tunnelId, String deviceType){
        startPage();
        List<SdDevices> devices = devicesService.getDevicesList(tunnelId,deviceType);
        return getDataTable(devices);
    }

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    @GetMapping("/getFaultList")
    public TableDataInfo getFaultList(String tunnelId,String faultLevel){
        startPage();
        List<SdFaultList> faultList = sdFaultListService.getFaultList(tunnelId,faultLevel);
        return getDataTable(faultList);
    }

    /**
     * 任务详情
     * @param taskId
     * @return
     */
    @PostMapping("/getTaskInfoList")
    public Result getTaskInfoList(@RequestBody String taskId){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(taskId);
        List<SdPatrolList> patrolLists = sdTaskListService.getPatrolListsInfo(taskId);
        List<SdTaskOpt> sdTaskOpts = sdTaskListService.getTaskOpt(taskId);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("patrol", patrolLists);
        map.put("opt", sdTaskOpts);
        return Result.success(map);
    }




    /**
     * 查询班组列表
     */
    @ApiOperation("查询班组列表")
    @GetMapping("/getListBz")
    public TableDataInfo<List<SysDept>> list()
    {
        startPage();
        String deptId = SecurityUtils.getDeptId();
        List<SysDept> list = sdTaskListService.selectTableBzDataInfo(deptId);
        return getDataTable(list);
    }

    /**
     * app端获取任务状态
     * @return
     */
    @PostMapping("/app/getTaskStatus")
    public Result getTaskStatus(){
        List<SdTaskList> taskList = sdTaskListService.getTaskStatus();
        return Result.success(taskList);
    }

    /**
     * app  巡查任务列表
     * @param
     * @param sdTaskList
     * @return
     */
    @PostMapping("/app/getTaskList")
    public Result getTaskList(SdTaskList sdTaskList){
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        String startTime = "";//开始时间
        String endTime = "";//结束时间
        if(sdTaskList.getTime()!=null&&!"".equals(sdTaskList.getTime())){//不为空
            //  0：最近1周；1：最近3周；2：最近1月；3：最近3月
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            endTime = format.format(date);//结束时间
            if("0".equals(sdTaskList.getTime())){//最近一周
                c.setTime(new Date());
                c.add(Calendar.DATE, - 7);
                Date d = c.getTime();
                startTime = format.format(d);
            }else if("1".equals(sdTaskList.getTime())){//最近3周
                c.setTime(new Date());
                c.add(Calendar.DATE, - 21);
                Date d = c.getTime();
                startTime = format.format(d);
            }else if("2".equals(sdTaskList.getTime())){//最近1月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                startTime = format.format(m);
            }else if("3".equals(sdTaskList.getTime())){//最近3月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -3);
                Date m3 = c.getTime();
                startTime = format.format(m3);
            }else{

                String [] s= sdTaskList.getTime().split(",",0);
                startTime = s[0];
                endTime = s[1];
            }
        }


        List<SdTaskList> taskList = sdTaskListService.getTaskList(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId);
        if(taskList!=null&&taskList.size()>0){
            for(int i=0;i<taskList.size();i++){
                if(taskList.get(i).getId()!=null){
                    SdTaskList task = sdTaskListService.countPatrolNum(taskList.get(i).getId());
                    taskList.get(i).setTotalNum(task.getTotalNum());
                }
            }

        }
        return Result.success(taskList);
    }

    /**
     *  app 巡检任务基本信息
     * @param taskId
     * @return
     */
    @PostMapping("/app/getTaskInfo")
    public Result getTaskInfo(String taskId){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(taskId);
        return Result.success(taskList);
    }

    /**
     * app 端接收任务  task_status变为1（巡检中）
     * @param id
     * @return
     */
    @GetMapping("/app/accept")
    public AjaxResult accept(String id)
    {
        Long userId  = SecurityUtils.getUserId();
        return toAjax(sdTaskListService.acceptSdTaskList(id,userId));
    }

    /**
     * app端  巡查点清单
     * @param taskId
     * @return
     */
    @PostMapping("/app/getPatrolInfo")
    public Result getPatrolInfo(String taskId){
        List<SdPatrolList> patrolList = sdTaskListService.getPatrolInfo(taskId);
        return Result.success(patrolList);
    }

    /**
     * app端  获取任务现场情况
     * @param taskId
     * @return
     */
    @PostMapping("/app/getTaskSiteCondition`")
    public Result getTaskSiteCondition(String taskId){
        String result = sdTaskListService.getTaskSiteCondition(taskId);
        return Result.success(result);
    }



    /**
     * app 端暂存本地  task_status 变为3 待回传：APP点击“暂存本地”；PC端不可见
     *     提交上报    task_status 变为2 已完结
     *     接收任务    task_status 从“待巡检”改为“巡检中”
     * @param  sdTaskList
     * @return
     */
    @GetMapping("/app/saveTask")
    public AjaxResult saveTask(SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.saveLocal(sdTaskList));
    }

    /**
     * 导出巡查任务执行报告
     * @param request
     * @param response
     * @param taskNo
     */
    @RequestMapping("/exportPatrolTaskReport")
    public void exportPatrolTaskReport(HttpServletRequest request, HttpServletResponse response, String taskNo){
        try {
            SdTaskList task = sdTaskListService.selectSdTaskListById(taskNo);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pdTime = "";
            String xcTime = "";
            String planEndTime = "";
            String taskEndTime = "";
            if (task.getDispatchTime() != null) {
                pdTime = format.format(DateUtil.parse(task.getDispatchTime().toString()));
            }
            if (task.getEndPlantime() != null) {
                planEndTime = format.format(DateUtil.parse(task.getEndPlantime().toString()));
            }
            if (task.getTaskEndtime() != null) {
                taskEndTime = format.format(DateUtil.parse(task.getTaskEndtime().toString()));
            }
            List<SdPatrolList> list = SpringUtils.getBean(SdPatrolListMapper.class).getPatrolListsInfo(taskNo);
//            AtomicInteger i = new AtomicInteger(1);
//            list.stream().peek(s->s.setRemark(String.valueOf(i.getAndIncrement()))).collect(Collectors.toList());
            List<Map<String,Object>> convertList = new ArrayList<>();
            try {
                //数据按巡查顺序排序
                list.sort(Comparator.comparing(SdPatrolList::getXcSort));
                for(SdPatrolList obj:list){
                    //处理图片需将原对象类型转为Map
                    Map<String,Object> map = BeanUtils.describe(obj);
                    if(map.get("eqFaultId") != null){
                        String eqId = null;
                        if(map.get("patrolType") != null){
                            if("1".equals(map.get("patrolType"))){//故障点
                               eqId = sdFaultListService.selectSdFaultEqById(map.get("eqFaultId").toString());
                            }else if("0".equals(map.get("patrolType"))){//设备
                               eqId = map.get("eqFaultId").toString();
                            }
                        }

                        SdDevices devices = SpringUtils.getBean(SdDevicesMapper.class).selectSdDevicesById(eqId);
                        map.put("tunnelName",SpringUtils.getBean(SdTunnelsMapper.class).selectSdTunnelsById(devices.getEqTunnelId()).getTunnelName());
                        map.put("typeName",SpringUtils.getBean(SdEquipmentTypeMapper.class).selectSdEquipmentTypeById(devices.getEqType()).getTypeName());
                    }
                    String imgFileId = obj.getImgFileId();
                    if(StrUtil.isNotEmpty(imgFileId)){
                        SdTrafficImage sdTrafficImage = new SdTrafficImage();
                        sdTrafficImage.setBusinessId(imgFileId);
                        List<SdTrafficImage> imageList = SpringUtils.getBean(SdTrafficImageMapper.class).selectFaultImgFileList(sdTrafficImage);
                        if(imageList.size()>0){
                            // 图片的处理
                            String imageBaseStr = imageList.get(0).getImgUrl();
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
                            .size(102, 126).create();
                            map.put("photo", pictureRenderData);
                        }
                    }
                    if (map.get("xcTime") != null) {
                        xcTime = format.format(DateUtil.parse(map.get("xcTime").toString()));
                    }
                    //添加巡查记录序号
                    map.put("xcTime",xcTime);
                    map.put("remark",obj.getXcSort()+1);
                    convertList.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*ClassPathResource classPathResource = new ClassPathResource("patrolTemplate/patrolReport.docx");
            String resource = classPathResource.getUrl().getPath();*/
            //渲染表格
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            //构建巡查点巡查记录数据
            Configure config = Configure.newBuilder().bind("detailList", policy).build();
            String finalPdTime = pdTime;
            String finalPlanEndTime = planEndTime;
            String finalTaskEndTime = taskEndTime;
            XWPFDocument document = new CustomXWPFDocument(new ClassPathResource("exporttemplate/patrolReport.docx").getInputStream());
            XWPFTemplate template = XWPFTemplate.compile(document, config).render(
                    new HashMap<String, Object>() {{
                        put("patrolBlock",convertList);
                        put("currentTime", DateUtils.getTime());
                        put("task",task);
                        put("pdTime", finalPdTime);
                        put("planEndTime", finalPlanEndTime);
                        put("endTime", finalTaskEndTime);
                        Class<?> cClass = (Class<?>) SdTaskList.class;
                        Field[] fields = cClass.getDeclaredFields();
                        //put("detailList",list);
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            if(fieldName.equals("serialVersionUID")){
                                continue;
                            }
                            String upperChar = fieldName.substring(0, 1).toUpperCase();
                            String anotherStr = fieldName.substring(1);
                            String methodName = "get" + upperChar + anotherStr;
                            Method newMethod = cClass.getDeclaredMethod(methodName);
                            Object Value = newMethod.invoke(task);
                            if(Value!=null){
                                put(fieldName,Value.toString());
                            }
                        }
                    }}
            );
            String temDir = RuoYiConfig.getProfile()+"/word/";
            //"D:/Electromechanical/"+ File.separator+"file/word/"; ;//生成临时文件存放地址
            File file = new File(temDir);
            if (!file.exists()){
                file.mkdirs();
            }
            //生成文件名
            Long time = new Date().getTime();
            // 生成的word格式
            String formatSuffix = ".docx";
            // 拼接后的文件名
            String fileName = time + formatSuffix;//文件名  带后缀
            FileOutputStream fos = new FileOutputStream(temDir+fileName);
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
     * 巡查点检修情况保存
     * @param sdPatrolList
     * @return
     */
    @GetMapping("/app/savePatrol")
    public AjaxResult savePatrol(@RequestParam(name = "file", required = false) MultipartFile[] file, SdPatrolList sdPatrolList)
    {
        return toAjax(sdTaskListService.savePatrol(file,sdPatrolList));
    }

    /**
     * app端首页待处理任务单
     * @return
     */
    @PostMapping("/app/taskToDo")
    public Result taskToDo(){
        String deptId = SecurityUtils.getDeptId();
        List<SdTaskList> taskList = sdTaskListService.getTaskToDo(deptId);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("num", taskList.size());
        return Result.success(map);
    }


    /**
     * 根据所选隧道查询班组
     * @param tunnelId
     * @return
     */
    @PostMapping("/selectBzByTunnel")
    public Result selectBzByTunnel(@RequestBody String tunnelId){
        String bz = sdTaskListService.selectBzByTunnel(tunnelId);
        return Result.success(bz);
    }


}
