package com.tunnel.business.service.electromechanicalPatrol.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.DeptTunnelTreeSelect;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.dataReport.FaultStatus;
import com.tunnel.business.datacenter.domain.dataReport.OptType;
import com.tunnel.business.datacenter.domain.dataReport.TaskStatus;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.*;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTaskListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 巡查任务Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class SdTaskListServiceImpl implements ISdTaskListService
{
    @Autowired
    private SdTaskListMapper sdTaskListMapper;

    @Autowired
    private SdPatrolListMapper sdPatrolListMapper;

    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdFaultListMapper sdFaultListMapper;

//    @Autowired
//    @Qualifier("kafkaThreeTemplate")
//    private KafkaTemplate<String, String> kafkaThreeTemplate;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTwoTemplate;
//
//    @Value("${tunnelTaskList}")
//    private String tunnelTaskList;
//
//    @Value("${tunnelTaskPatrol}")
//    private String tunnelTaskPatrol;
//
//    @Value("${tunnelTaskOperation}")
//    private String tunnelTaskOperation;

    /**
     * 查询巡查任务
     *
     * @param id 巡查任务主键
     * @return 巡查任务
     */
    @Override
    public SdTaskList selectSdTaskListById(String id)
    {
        SdTaskList sdTaskList = sdTaskListMapper.selectSdTaskListById(id);

        if(sdTaskList.getTaskCxtime()==null||"".equals(sdTaskList.getTaskCxtime())){//没有持续时间
            //任务持续时间为 当前时间-发布时间
            if(sdTaskList.getEndPlantime()!=null&&!"".equals(sdTaskList.getEndPlantime())){
                sdTaskList.setTaskCxtime(lengthTime(sdTaskList.getDispatchTime()));
            }
        }

        return sdTaskList;
    }

    /**
     * 查询巡查任务列表
     *
     * @param sdTaskList 巡查任务
     * @return 巡查任务
     */
    @Override
    public List<SdTaskList> selectSdTaskListList(SdTaskList sdTaskList)
    {
        List<SdTaskList> taskLists = sdTaskListMapper.selectSdTaskListList(sdTaskList);
        for(int i = 0;i<taskLists.size();i++){
                if(taskLists.get(i).getTaskStatus()!=null&&!"".equals(taskLists.get(i).getTaskStatus())){
                    taskListStatus(taskLists.get(i));
                }
            }
        return taskLists;

    }

    /**
     * 筛选超时的任务
     * @param taskListsAll
     * @return
     */
    private List<SdTaskList> getChaoshiTaskList(List<SdTaskList> taskListsAll) {
        List<SdTaskList> chaoshiTaskLists = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 0;i<taskListsAll.size();i++){
            if(taskListsAll.get(i).getEndPlantime()!=null){
                String  plantime = sdf.format(taskListsAll.get(i).getEndPlantime());//计划完成时间
                long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
                long diff = System.currentTimeMillis() - time + 1000;
                if(diff>0){//当前时间与计划完成时间的差值
                    taskListsAll.get(i).setTask(taskListsAll.get(i).getTask()+","+TaskStatus.YICHAOSHI.getName());
                    chaoshiTaskLists.add(taskListsAll.get(i));
                }
            }
        }
        return chaoshiTaskLists;
    }


    /**
     * 判断任务状态
     * @param taskList
     * @return
     */
    private SdTaskList taskListStatus(SdTaskList taskList){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        if(taskList.getTask()== null){//完结，已超时的情况
            taskList.setTask(TaskStatus.YIWANJIE.getName()+","+TaskStatus.YICHAOSHI.getName());
        }else if(taskList.getEndPlantime()!=null&&!"".equals(taskList.getEndPlantime())){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            diff = System.currentTimeMillis() - time;
            if(taskList.getTask().equals(TaskStatus.DAIXUNCHA.getName())||taskList.getTask().equals(TaskStatus.XUNCHAZHONG.getName())){
                if(diff>0){//当前时间与计划完成时间的差值
                    taskList.setTask(taskList.getTask()+","+TaskStatus.YICHAOSHI.getName());
                }
            }
        }
        return taskList;
    }

    /**
     * 新增巡查任务
     *
     *
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    @Override
    public int insertSdTaskList(SdTaskList sdTaskList)
    {
        int result = -1;
        //String taskId = UUIDUtil.getRandom32BeginTimePK();//任务编号
        String taskId = createTaskId(sdTaskList.getTunnelId());
        sdTaskList.setId(taskId);//id
        sdTaskList.setZzjgId(SecurityUtils.getLoginUser().getDeptId());
        sdTaskList.setDispatcher(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
        sdTaskList.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        //sdTaskList.setTaskStatus("0");//待巡查
        result = sdTaskListMapper.insertSdTaskList(sdTaskList);
        SdTaskOpt sdTaskOpt = new SdTaskOpt();
        if(result>0){
            /*添加操作记录*/
            if("0".equals(sdTaskList.getTaskStatus())){//发布任务
                sdTaskOpt.setId(UUIDUtil.getRandom32BeginTimePK());
                sdTaskOpt.setTaskId(taskId);
                sdTaskOpt.setOptType(OptType.PAIDAN.getCode());
                sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
                result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
            }

        }
        if(result>0){
            result = insertPatrol(sdTaskList,taskId);
        }
//        JSONObject  jsonObject = new JSONObject();
//        //任务数据推送
//        jsonObject.put("taskRecord",sdTaskList);
//        jsonObject.put("optType",OptType.PAIDAN.getCode());//用于区分新增，修改，删除，接收，提交
//        kafkaTwoTemplate.send(tunnelTaskList, jsonObject.toString());
//        //操作记录数据推送
//        jsonObject.put("taskOptRecord",sdTaskOpt);
//        kafkaTwoTemplate.send(tunnelTaskOperation, jsonObject.toString());
        return result;
    }
   /**
    *
    * 生成任务id  格式:隧道id的后三位+当前年月日+001  例HSD20230331001
    *
    * */
    private String createTaskId(String tunnelId) {
        tunnelId.substring(tunnelId.length()-3);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String pid = tunnelId.substring(tunnelId.length()-3)+formatter.format(date);

        String cid = sdTaskListMapper.selectCurrentDayTask(pid);
        String countNum = "";
        if(cid != null&&!"".equals(cid)){
            String codeTmp = cid.substring(cid.length() - 3); // 获取字符串最后三个字符
            int num = StrToInt(codeTmp) + 1;
            if(num>=100) {
                countNum += num;
            }else if(10<=num&&num<100) {
                countNum += ("0"+num);
            }else if(num<10) {
                countNum += ("00"+num);
            }
        }else
            countNum += "001";
        pid = pid+countNum;
        return pid;

    }

    public static int StrToInt(String str) {
        int rs = 0;
        DecimalFormat df = new DecimalFormat("#");
        if (str != null) {
            try {
                rs = df.parse(str).intValue();
            } catch (Exception e) {
                rs = 0;
            }
        }
        return rs;
    }



    public int insertPatrol(SdTaskList sdTaskList,String taskId){
        int result = -1;
        List<String> list = sdTaskList.getDevicesList();
        list.remove(list.size() - 1);
        Map<String,Integer>devicesMap = new HashMap();//定义devicemap用于对应巡查点的顺序
        Map<String,Integer>faultMap = new HashMap();//定义faultmap用于对应巡查点的顺序
        List<String> devicesList = new ArrayList<>();
        List<String> faultList = new ArrayList<>();
        if(list!=null&&list.size()>0){
            for(int k=0;k<list.size();k++){
                String str = list.get(k);
                String flag = str.substring(str.length() -2,str.length());
                String strm = str.substring(0,str.length()-2);
                if("_1".equals(flag)){//巡查点
                    devicesList.add(strm);
                    devicesMap.put(strm,k);
                }else if("_2".equals(flag)){
                    faultList.add(strm);
                    faultMap.put(strm,k);
                }

            }

        }
        /*新增巡查点*/
        List<SdPatrolList>patrolList = new ArrayList<>();
        if(devicesList!=null&&devicesList.size()>0){
            String[] eqIds = devicesList.toArray(new String[devicesList.size()]);
            List<SdDevices> devices = sdDevicesMapper.batchGetDevicesList(eqIds);
            if(devices!=null&&devices.size()>0){
                for(Map.Entry<String, Integer> entry:devicesMap.entrySet()){
                    SdPatrolList sdPatrolList = new SdPatrolList();
                    //区分  pc端新增或者高速云监听到的数据
                    sdPatrolList.setId(UUIDUtil.getRandom32BeginTimePK());//id
                    sdPatrolList.setTaskId(taskId);//task_id
                    for(int j=0;j<devices.size();j++){
                        if(devices.get(j).getEqId().equals(entry.getKey())){
                            sdPatrolList.setEqFaultId(entry.getKey());//eq_fault_id
                            sdPatrolList.setPatrolType("0");//巡检点类型
                            sdPatrolList.setEqName(devices.get(j).getEqName());//eq_name
                            sdPatrolList.setPosition(devices.get(j).getEqDirection()+devices.get(j).getPile());//position
                            sdPatrolList.setXcSort(entry.getValue());
                            sdPatrolList.setXcStatus("0");//未巡查
                            sdPatrolList.setFaultClstatus("2");//未消除
                            patrolList.add(sdPatrolList);
                        }
                    }
                }
                result = sdPatrolListMapper.batchInsertPatrol(patrolList);
            }
        }
        /*新增故障点*/
        List<SdPatrolList>patrolList1 = new ArrayList<>();
        if(faultList!=null&&faultList.size()>0){
            String[] faultIds = faultList.toArray(new String[faultList.size()]);
            List<SdFaultList> fault = sdFaultListMapper.batchGetFaultList(faultIds);
            if(fault!=null&&fault.size()>0){
                for(Map.Entry<String, Integer> entry:faultMap.entrySet()){
                    SdPatrolList sdPatrolList = new SdPatrolList();
                    sdPatrolList.setId(UUIDUtil.getRandom32BeginTimePK());//id
                    sdPatrolList.setTaskId(taskId);//task_id
                    for(int j=0;j<fault.size();j++){
                        if(fault.get(j).getId().equals(entry.getKey())){
                            sdPatrolList.setEqFaultId(entry.getKey());//eq_fault_id
                            sdPatrolList.setPatrolType("1");//巡检点类型
                            sdPatrolList.setEqName(fault.get(j).getEqName());//eq_name
                            sdPatrolList.setPosition(fault.get(j).getFaultLocation());//position
                            sdPatrolList.setXcSort(entry.getValue());
                            sdPatrolList.setXcStatus("0");//未巡查
                            sdPatrolList.setFaultClstatus("2");//未消除
                            patrolList1.add(sdPatrolList);
                        }
                    }
                }
                result = sdPatrolListMapper.batchInsertPatrol(patrolList1);
            }
        }
//        JSONObject  jsonObject = new JSONObject();
//        //巡检点数据推送
//        jsonObject.put("taskPatrolDeviceRecord",patrolList);
//        jsonObject.put("taskPatrolFaultRecord",patrolList1);
//        jsonObject.put("optType",OptType.PAIDAN.getCode());
//        kafkaTwoTemplate.send(tunnelTaskPatrol, jsonObject.toString());
        return result;

    }

    /**
     * 修改巡查任务
     *
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    @Override
    public int updateSdTaskList(SdTaskList sdTaskList)
    {
        int result = -1;
        if(sdTaskList.getUpdateBy()==null||"".equals(sdTaskList.getUpdateBy())){
            sdTaskList.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        }
        sdTaskList.setUpdateTime(DateUtils.getNowDate());
        result = sdTaskListMapper.updateSdTaskList(sdTaskList);
        if(sdTaskList.getUpdateBy()==null||"".equals(sdTaskList.getUpdateBy())){
            if(result>0){
                result = sdPatrolListMapper.batchDeletePatrolListByTaskId(sdTaskList.getId());
            }
            if(result>0){
                String taskId = sdTaskList.getId();
                result = insertPatrol(sdTaskList,taskId);
            }

            if(result>0){
                /*添加操作记录*/
                if("0".equals(sdTaskList.getTaskStatus())){//发布任务
                    SdTaskOpt sdTaskOpt = new SdTaskOpt();
                    sdTaskOpt.setId(UUIDUtil.getRandom32BeginTimePK());
                    sdTaskOpt.setTaskId(sdTaskList.getId());
                    sdTaskOpt.setOptType(OptType.PAIDAN.getCode());
                    sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
                    result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
                }
            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("taskRecord",sdTaskList);
//            jsonObject.put("optType",OptType.PAIDAN.getCode());//用于区分新增，修改，删除，接收，提交
//            kafkaTwoTemplate.send(tunnelTaskList, jsonObject.toString());
        }
        return result;
    }

    /**
     * 批量删除巡查任务
     *
     * @param ids 需要删除的巡查任务主键
     * @return 结果
     */
    @Override
    public int deleteSdTaskListByIds(String ids)
    {
        int result = 0;
        result = sdTaskListMapper.deleteSdTaskListByIds(ids);
        if(result>0){
            result = sdPatrolListMapper.batchDeletePatrolListByTaskIds(ids);
        }
//        JSONObject  jsonObject = new JSONObject();
//        //任务数据推送
//        jsonObject.put("taskRecord",ids);
//        jsonObject.put("optType",OptType.DELETE.getCode());//用于区分新增，修改，删除，接收，提交
//        kafkaTwoTemplate.send(tunnelTaskList, jsonObject.toString());
//        kafkaTwoTemplate.send(tunnelTaskPatrol, jsonObject.toString());
        return result;
    }

    /**
     * 删除巡查任务信息
     *
     * @param id 巡查任务主键
     * @return 结果
     */
    @Override
    public int deleteSdTaskListById(String id)
    {
        return sdTaskListMapper.deleteSdTaskListById(id);
    }

    /**
     * 查询任务详情
     * @param task_id
     * @return
     */
    @Override
    public List<SdTaskList> getTaskInfoList(String task_id) {
        List<SdTaskList> taskList = new ArrayList<>();
        taskList = sdTaskListMapper.getTaskInfoList(task_id);
        //任务持续时间逻辑判断   task_status=2  已完结
        if(taskList!=null){
            //判断任务状态，已完结时保存任务持续时间
            if(taskList.get(0).getTaskStatus()==null){//已完结，超时
                taskList.get(0).setTaskStatus(TaskStatus.YIWANJIE.getName());
                taskList.get(0).setIfchaosgu(TaskStatus.YICHAOSHI.getName());//超时
            }
            else{
                taskStatus(taskList.get(0));
                if(taskList.get(0).getTaskCxtime()==null||"".equals(taskList.get(0).getTaskCxtime())){//没有持续时间
                    //任务持续时间为 当前时间-发布时间
                    //if(taskList.get(0).getEndPlantime()!=null&&!"".equals(taskList.get(0).getEndPlantime())){
                        taskList.get(0).setTaskCxtime(lengthTime(taskList.get(0).getDispatchTime()));
                    //}
                }

            }
        }

        return taskList;
    }

    /**
     * 判断任务状态
     * @param taskList
     * @return
     */
    private SdTaskList taskStatus(SdTaskList taskList){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        if(taskList.getEndPlantime()!=null&&!"".equals(taskList.getEndPlantime())){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            diff = System.currentTimeMillis() - time;
            if((taskList.getTaskStatus().equals(TaskStatus.DAIXUNCHA.getName()))||(taskList.getTaskStatus().equals(TaskStatus.XUNCHAZHONG.getName()))){
                if(diff>0){
                    taskList.setTaskStatus(taskList.getTaskStatus());
                    taskList.setIfchaosgu(TaskStatus.YICHAOSHI.getName());
                }else{
                    taskList.setTaskStatus(taskList.getTaskStatus());
                    taskList.setIfchaosgu("");//未超时
                }
            }


        }
        return taskList;
    }


    /**
     * 计算持续时间
     * @param dispatchTime
     * @return
     */
    private String lengthTime(Date dispatchTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  fbtime = sdf.format(dispatchTime);
        long time = sdf.parse(fbtime, new ParsePosition(0)).getTime();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = System.currentTimeMillis() - time + 1000;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
      //  System.out.println(day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
        return day + "天" + hour + "小时" + min + "分钟";

    }

    /**
     * 根据任务id查询关联巡检点详细信息
     * @param task_id
     * @return
     */
    @Override
    public List<SdPatrolList> getPatrolListsInfo(String task_id) {

        List<SdPatrolList> sdPatrolList = sdPatrolListMapper.getPatrolListsInfo(task_id);
        if(sdPatrolList!=null && sdPatrolList.size()>=0){
            for (SdPatrolList patrolList : sdPatrolList) {
                String fileId = patrolList.getImgFileId();
                if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                    String[] businessId = fileId.split(",");
                    patrolList.setiFileList(sdTrafficImageMapper.selectPatrolFaultImgFileList(businessId));
                }
            }
        }
        return sdPatrolList;
    }


    /**
     * 查询班组信息
     * @param deptId
     * @return
     */
    @Override
    public List<SysDept> selectTableBzDataInfo(String deptId,String tunnelId) {
        return sdTaskListMapper.selectTableBzDataInfo(deptId,tunnelId);
    }


    /**
     * 构建前端所需要树结构
     *
     * @param deptTunnels 隧道部门列表
     * @return 树结构列表
     */
    public List<SysDeptTunnel> buildDeptTree(List<SysDeptTunnel> deptTunnels)
    {
        List<SysDeptTunnel> returnList = new ArrayList<SysDeptTunnel>();
        List<String> tempList = new ArrayList();
        for (SysDeptTunnel dept : deptTunnels)
        {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDeptTunnel> iterator = deptTunnels.iterator(); iterator.hasNext();)
        {
            SysDeptTunnel dept = (SysDeptTunnel) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点

            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(deptTunnels, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = deptTunnels;
        }
        return returnList;
    }

    /**
     * 构建前端所需隧道部门树
     * @param deptTunnels 部门列表
     * @return
     */
    @Override
    public List<DeptTunnelTreeSelect> buildDeptTunnelTreeSelect(List<SysDeptTunnel> deptTunnels) {
        List<SysDeptTunnel> deptTunnelTrees = buildDeptTree(deptTunnels);
        return deptTunnelTrees.stream().map(DeptTunnelTreeSelect::new).collect(Collectors.toList());
    }


    /**
     * 废止巡查任务
     * @return
     */
    @Override
    public int abolishSdTaskList(@Param("id") String id) {
        return sdTaskListMapper.abolishSdTaskList(id);
    }


    /**
     * 查询巡查列表
     * @param
     * @param
     * @return
     */
    @Override
    public List<SdTaskList> getTaskList(String taskStatus,String taskName,String startTime,String endTime,String deptId,Integer pageSize,Integer pageNum,Long userId ) {
        SdTaskList sdTaskList  = new SdTaskList();
        if(pageNum!=null&&pageSize!=null){
            pageNum = (pageNum-1)*pageSize ;
        }
        sdTaskList.getParams().put("taskStatus",taskStatus);
        sdTaskList.getParams().put("taskName",taskName);
        sdTaskList.getParams().put("startTime",startTime);
        sdTaskList.getParams().put("endTime",endTime);
        sdTaskList.getParams().put("deptId",deptId);
        sdTaskList.getParams().put("pageSize",pageSize);
        sdTaskList.getParams().put("pageNum",pageNum);
        sdTaskList.getParams().put("userId",userId);
        //return sdTaskListMapper.getTaskList(sdTaskList);
        List<SdTaskList>list = sdTaskListMapper.getTaskList(sdTaskList);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if(list.get(i).getTaskStatus()!=null&&!"".equals(list.get(i).getTaskStatus())){
                    taskListAppStatus(list.get(i));
                }
            }
        }


        return list;
    }

    /**
     * 查询任务关联的巡查点数量
     * @param id
     * @return
     */
    @Override
    public SdTaskList countPatrolNum(String id) {
        return sdPatrolListMapper.countPatrolNum(id);
    }

    /**
     * app端 接收任务
     * @param id
     * @return
     */
    @Override
    public int acceptSdTaskList(String id,Long userId) {
        int result = -1;
        result = sdTaskListMapper.acceptSdTaskList(id,userId);
        SdTaskOpt sdTaskOpt = new SdTaskOpt();
        if(result>0){//添加操作记录
            sdTaskOpt.setId(UUIDUtil.getRandom32BeginTimePK());
            sdTaskOpt.setTaskId(id);
            sdTaskOpt.setOptType(OptType.JEISHOU.getCode());
            sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
            result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
        }
//        JSONObject  jsonObject = new JSONObject();
//        jsonObject.put("taskRecord",id);
//        jsonObject.put("acceptUser",userId);
//        jsonObject.put("optType",OptType.JEISHOU.getCode());//用于区分新增，修改，删除，接收，提交
//        //接收任务数据推送
//        kafkaTwoTemplate.send(tunnelTaskList, jsonObject.toString());
//        //操作记录数据推送
//        jsonObject.put("taskOptRecord",sdTaskOpt);
//        kafkaTwoTemplate.send(tunnelTaskOperation, jsonObject.toString());
        return result;
    }

    /**
     * app巡查点清单
     * @param taskId
     * @return
     */
    @Override
    public List<SdPatrolList> getPatrolInfo(String taskId) {
        //要判断是故障点还是设备点
        return sdPatrolListMapper.getPatrolInfo(taskId);
    }

    /**
     * app端  获取任务现场情况
     * @param taskId
     * @return
     */
    @Override
    public  String getTaskSiteCondition(String taskId) {
        String result = "";
        List<SdPatrolList> SdPatrolList = sdPatrolListMapper.isFaultEnd(taskId);//所有巡查点的巡查状态
        if(SdPatrolList!=null&&SdPatrolList.size()>0){
            for(int i=0;i<SdPatrolList.size();i++){
                result = SdPatrolList.get(i).getXcStatus();
                if(result!=null&&!"".equals(result)&&"0".equals(result)){//巡查点存在未巡查为0，即返回任务执行状态为未完结
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 获取任务关联巡检点信息
     * @param id
     * @return
     */
    @Override
    public Map getUpdatePatrolLists(String id) {
        Map map = new HashMap();
        List<SdPointList> devicesPatrolList = sdPatrolListMapper.getDevicesPatrolLists(id);
        List<SdPointList> faultPatrolList = sdPatrolListMapper.getFaultPatrolLists(id);
        List<SdPointList>list = new ArrayList<>();
        list.addAll(devicesPatrolList);
        list.addAll(faultPatrolList);
        map.put("devicesPatrolList",devicesPatrolList);
        map.put("faultPatrolList",faultPatrolList);
        map.put("list",list);
        return map;
    }

    /**
     * 操作记录
     * @param taskId
     * @return
     */
    @Override
    public List<SdTaskOpt> getTaskOpt(String taskId) {
        return sdTaskListMapper.getTaskOpt(taskId);
    }



    /**
     * 递归列表
     */
    private void recursionFn(List<SysDeptTunnel> list, SysDeptTunnel t)
    {
        // 得到子节点列表
        List<SysDeptTunnel> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDeptTunnel tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private List<SysDeptTunnel> getChildList(List<SysDeptTunnel> list, SysDeptTunnel t)
    {
        List<SysDeptTunnel> tlist = new ArrayList<SysDeptTunnel>();
        Iterator<SysDeptTunnel> it = list.iterator();
        while (it.hasNext())
        {
            SysDeptTunnel n = (SysDeptTunnel) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId()) )
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDeptTunnel> list, SysDeptTunnel t)
    {
        return getChildList(list, t).size() > 0;
    }


    /**
     * app端暂存本地
     * @param sdTaskList
     * @return
     */
    @Override
    public int saveLocal(SdTaskList sdTaskList) {
        if(sdTaskList.getTaskStatus()!=null&&!"".equals(sdTaskList.getTaskStatus())&&"4".equals(sdTaskList.getTaskStatus())){
            sdTaskList.setTaskStatus(TaskStatus.YIWANJIE.getCode()+","+TaskStatus.YICHAOSHI.getCode());
        }else{
            sdTaskList.setTaskStatus(TaskStatus.YIWANJIE.getCode());
        }
        int result = -1;
        result =  sdTaskListMapper.saveLocal(sdTaskList);
        SdTaskOpt sdTaskOpt = new SdTaskOpt();
        if(result>0){//添加操作记录
            sdTaskOpt.setId(UUIDUtil.getRandom32BeginTimePK());
            sdTaskOpt.setTaskId(sdTaskList.getId());
            sdTaskOpt.setOptType(OptType.TIJIAO.getCode());
            sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
            result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
        }
//        JSONObject  jsonObject = new JSONObject();
//        jsonObject.put("taskRecord",sdTaskList);
//        jsonObject.put("optType",OptType.TIJIAO.getCode());//用于区分新增，修改，删除，接收，提交
//        //接收任务数据推送
//        kafkaTwoTemplate.send(tunnelTaskList, jsonObject.toString());
//        //操作记录数据推送
//        jsonObject.put("taskOptRecord",sdTaskOpt);
//        kafkaTwoTemplate.send(tunnelTaskOperation, jsonObject.toString());

        return result ;
    }

    /**
     * 巡查点检修情况保存
     * @param sdPatrolList
     * @return
     */
    @Override
    public int savePatrol(SdPatrolList sdPatrolList) {
        List<SdTrafficImage> list = new ArrayList<SdTrafficImage>();
        int result = -1;
        String falltRemoveStatue = null;
        String  faultId  = null;
        if(sdPatrolList.getImgFileId()!=null&&!"".equals(sdPatrolList.getImgFileId())){
            sdPatrolList.setImgFileId(sdPatrolList.getImgFileId().substring(0,sdPatrolList.getImgFileId().length()-1));
        }
        //先判断是故障点还是设备
        SdPatrolList patrolInfo = sdPatrolListMapper.getPatroltype(sdPatrolList.getId());
        sdPatrolList.setXcStatus("1");//巡查状态为已巡查
        sdPatrolList.setUpdateTime(new Date());//更新时间
        sdPatrolList.setUpdateBy(SecurityUtils.getUsername());
        if(patrolInfo.getPatrolType().equals("1")){//故障点
            faultId = patrolInfo.getEqFaultId();
        }
        /*巡检点故障处理情况与故障消除状态情况对应*/
        if(faultId!=null){
            if(sdPatrolList.getFaultClstatus()!=null&&!"".equals(sdPatrolList)){
                if(sdPatrolList.getFaultClstatus().equals(FaultStatus.PATROLNULL.getCode())){//无故障
                    falltRemoveStatue = FaultStatus.FAULTNULL.getCode();
                }else  if(sdPatrolList.getFaultClstatus().equals(FaultStatus.PATROLYIXIAOCHU.getCode())){//已消除
                    falltRemoveStatue  = FaultStatus.FAULTYIXIAOCHU.getCode();
                }else{
                    falltRemoveStatue = FaultStatus.FAULTWEIXIAOCHU.getCode();
                }
            }
        }
        result = sdPatrolListMapper.savePatrol(sdPatrolList);
        if(result > -1&&falltRemoveStatue!=null){//是故障点则更新故障点的消除状态
            if(falltRemoveStatue.equals(FaultStatus.FAULTYIXIAOCHU.getCode())||falltRemoveStatue.equals(FaultStatus.FAULTNULL.getCode())){//已消除或者无故障
                result = sdFaultListMapper.updateFaultRemoveState(faultId,falltRemoveStatue);
            }else{
                result = sdFaultListMapper.updateFaultUnRemoveState(faultId,falltRemoveStatue);
            }

//            JSONObject  jsonObjectFault = new JSONObject();
//            //故障状态数据推送
//            jsonObjectFault.put("faultRecord",faultId);
//            jsonObjectFault.put("faultStatue",falltRemoveStatue);
//            jsonObjectFault.put("optType","2");//更新故障状态
//            kafkaTwoTemplate.send("test", jsonObjectFault.toString());
        }
//
//        JSONObject  jsonObject = new JSONObject();
//        //任务数据推送
//        jsonObject.put("taskPatrol",sdPatrolList);
//        jsonObject.put("taskPatrolImg",list);
//        jsonObject.put("optType",OptType.TIJIAO.getCode());//用于区分新增，修改，删除，接收，提交
//        kafkaTwoTemplate.send(tunnelTaskPatrol, jsonObject.toString());
        return result;
    }

    /***
     * 获取任务状态
     * @return
     */
    @Override
    public List<SdTaskList> getTaskStatus() {
        return sdTaskListMapper.getTaskStatus();
    }

    /**
     * app端首页待处理任务单
     * @param deptId
     * @return
     */
    @Override
    public List<Map> getTaskToDo(String deptId,Long userId) {
        return sdTaskListMapper.getTaskToDo(deptId,userId);
    }

    @Override
    public String selectBzByTunnel(String tunnelId) {
        return sdTaskListMapper.selectBzByTunnel(tunnelId);
    }

    @Override
    public List<Map> getTaskToDoTeams(String deptId,Long userId) {
        return sdTaskListMapper.getTaskToDoTeams(deptId,userId);
    }

    @Override
    public int getTaskCountList(String taskStatus, String taskName, String startTime, String endTime, String deptId,Long userId) {
        return sdTaskListMapper.getTaskCountList(taskStatus,taskName,startTime,endTime,deptId,userId);
    }

    @Override
    public int getTaskCountListTeams(String taskStatus, String taskName, String startTime, String endTime, String deptId,Long userId) {
        return sdTaskListMapper.getTaskCountListTeams(taskStatus,taskName,startTime,endTime,deptId,userId);
    }

    @Override
    public List<SdTaskList> getTaskListTeams(String taskStatus, String taskName, String startTime, String endTime, String deptId, Integer pageSize, Integer pageNum,Long userId) {
        SdTaskList sdTaskList  = new SdTaskList();
        if(pageNum!=null&&pageSize!=null){
            pageNum = (pageNum-1)*pageSize ;
        }
        sdTaskList.getParams().put("taskStatus",taskStatus);
        sdTaskList.getParams().put("taskName",taskName);
        sdTaskList.getParams().put("startTime",startTime);
        sdTaskList.getParams().put("endTime",endTime);
        sdTaskList.getParams().put("deptId",deptId);
        sdTaskList.getParams().put("pageSize",pageSize);
        sdTaskList.getParams().put("pageNum",pageNum);
        sdTaskList.getParams().put("userId",userId);
        List<SdTaskList>list = sdTaskListMapper.getTaskListTeams(sdTaskList);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if(list.get(i).getTaskStatus()!=null&&!"".equals(list.get(i).getTaskStatus())){
                    taskListAppStatus(list.get(i));
                }
            }
        }


        return list;
    }

    private SdTaskList taskListAppStatus(SdTaskList taskList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        if(taskList.getEndPlantime()!=null&&!"".equals(taskList.getEndPlantime())){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            diff = System.currentTimeMillis() - time;
        }
        if(taskList.getTaskStatus().equals(TaskStatus.DAIXUNCHA.getCode())||taskList.getTaskStatus().equals(TaskStatus.XUNCHAZHONG.getCode())){
            if(diff>0){//当前时间与计划完成时间的差值
                taskList.setTaskStatus(taskList.getTaskStatus()+","+TaskStatus.YICHAOSHI.getCode());
            }
        }
        return taskList;
    }

    @Override
    public List<SdTaskList> getSiteInfo(String taskId) {
        return sdTaskListMapper.getSiteInfo(taskId);
    }

    @Override
    public SdTaskList selectSdTaskById(String id) {
        return sdTaskListMapper.selectSdTaskById(id);
    }

    @Override
    public int updateGsySdTaskList(SdTaskList sdTaskList) {
        int result = -1;
        sdTaskList.setUpdateTime(DateUtils.getNowDate());
        result = sdTaskListMapper.updateSdTaskList(sdTaskList);
        return result;
    }

    @Override
    public String uploadPicture(MultipartFile[] file) {
        String guid ="";
        List<SdTrafficImage> list = new ArrayList<SdTrafficImage>();
        int result = -1;
        if(file!=null){
            guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
            //sdPatrolList.setImgFileId(guid);// 文件关联ID
            for (int i = 0; i < file.length; i++) {
                // 图片Base64
                String imageBaseStr = null;
                try {
                    String contentType = file[i].getContentType();
                    if (!contentType.contains("image")) {
                        throw new RuntimeException("文件类型不正确!");
                    }
                    byte[] imageBytes = file[i].getBytes();
                    BASE64Encoder base64Encoder = new BASE64Encoder();
                    imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                    imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                } catch (IOException e) {
                    throw new RuntimeException("图片转换base64异常");
                }
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/faultIcon/" + fileName);
                File filepath = new File(fileServerPath + "/faultIcon");

                SdTrafficImage iconFile = new SdTrafficImage();
                iconFile.setBusinessId(guid);
                iconFile.setImgUrl(imageBaseStr);
                iconFile.setImgName(fileName);
                iconFile.setCreateBy(SecurityUtils.getUsername());
                iconFile.setCreateTime(DateUtils.getNowDate());
                list.add(iconFile);

                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                try {
                    file[i].transferTo(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            result = sdTrafficImageMapper.brachInsertFaultIconFile(list);
            /*if (result > -1) {
                result = sdPatrolListMapper.savePatrol(sdPatrolList);
            }*/
        }
        return guid;
    }

    /**
     * 检验任务名称是否存在
     * @param sdTaskList
     * @return
     */
    @Override
    public int checkTaskList(SdTaskList sdTaskList) {
        return sdTaskListMapper.checkTaskList(sdTaskList);
    }

    /**
     * 删除现场图片
     * @param id
     * @return
     */
    @Override
    public int deleteSitePhoto(String id) {
        return sdTrafficImageMapper.deleteSitePhoto(id);
    }


    @Override
    public List getTaskAllList() {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }

        List<Map> taskList = sdTaskListMapper.getTaskAllList(deptId);


        for (Map map: taskList) {

            String taskId = map.get("id").toString();

            Map taskInfoMap = sdTaskListMapper.getTaskInfoByTaskId(taskId);

            if(taskInfoMap != null){
                String taskStatus = taskInfoMap.get("taskStatus") != null ? taskInfoMap.get("taskStatus").toString():null;
                String taskCxtime = taskInfoMap.get("taskCxtime") != null ? taskInfoMap.get("taskCxtime").toString():null;
                String dispatchTime = taskInfoMap.get("dispatchTime") != null ? taskInfoMap.get("dispatchTime").toString():null;
                String endPlantime = taskInfoMap.get("endPlantime") != null ? taskInfoMap.get("endPlantime").toString():null;
                if(taskStatus == null){
                    taskInfoMap.put("taskStatus",TaskStatus.YIWANJIE.getName());
                    taskInfoMap.put("ifchaosgu",TaskStatus.YICHAOSHI.getName());
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long diff = 0;
                    if(endPlantime!=null&&!"".equals(endPlantime)){
                        long time = sdf.parse(endPlantime, new ParsePosition(0)).getTime();
                        diff = System.currentTimeMillis() - time;
                        if((taskStatus.equals(TaskStatus.DAIXUNCHA.getName()))||(taskStatus.equals(TaskStatus.XUNCHAZHONG.getName()))) {
                            if (diff > 0) {
                                taskInfoMap.put("ifchaosgu", TaskStatus.YICHAOSHI.getName());
                            } else {
                                taskInfoMap.put("ifchaosgu", "");
                            }
                        }
                    }
                    if(taskCxtime==null||"".equals(taskCxtime)){//没有持续时间
                        //任务持续时间为 当前时间-发布时间
                        //if(taskList.get(0).getEndPlantime()!=null&&!"".equals(taskList.get(0).getEndPlantime())){
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dispatchTimeDate;
                        try{
                            dispatchTimeDate = sdf1.parse(dispatchTime);
                        }catch (Exception e){
                            dispatchTimeDate = null;
                        }
                        taskInfoMap.put("taskCxtime",lengthTime(dispatchTimeDate));
                        //}
                    }
                }
            }

            map.put("taskInfo",taskInfoMap);
            map.put("patrolInfo",sdTaskListMapper.getpatrolInfoByTaskId(taskId));

        }

        return taskList;
    }

    @Override
    public Map modifyTaskData(String reqJson) {
        JSONArray jsonArray = JSONArray.parseArray(reqJson);

        Map res = new HashMap();
        List<String>  success = new ArrayList<>();
        List<String>  error = new ArrayList<>();
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            Boolean  isEdit = jsonObject.getBoolean("isEdit");

            if(isEdit){
                SdTaskList sdTaskList = new SdTaskList();

                //更新状态
                String taskStatus = jsonObject.getString("taskStatus");
                //巡检编号
                String id = jsonObject.getString("id");

                sdTaskList.setId(id);
                if(taskStatus != null){
                    // 状态为 待回传
                    if(taskStatus == TaskStatus.DAIHUICHUAN.getCode()){
                        taskStatus = TaskStatus.YIWANJIE.getCode();
                    }
                    sdTaskList.setTaskStatus(taskStatus);
                }
                // 巡检基本信息
                JSONObject zc = jsonObject.getJSONObject("zc");
                if(zc != null){
                    String siteDescription = zc.getString("siteDescription"); //巡检描述
                    Date taskEndtime = zc.getDate("taskEndtime"); // 选件完成时间
                    sdTaskList.setSiteDescription(siteDescription);
                    sdTaskList.setTaskEndtime(taskEndtime);
                }
                // 更新基本信息
                if(updateSdTaskList(sdTaskList) != 0){
                    success.add(id);

                    // 巡检设备信息
                    JSONArray patrolInfoFormArray = jsonObject.getJSONArray("patrolInfoForm");

                    for (Object plObj : patrolInfoFormArray){
                        JSONObject patrolJSON = (JSONObject) plObj;
                        SdPatrolList sdPatrolList = new SdPatrolList();
                        sdPatrolList.setId(patrolJSON.getString("id"));
                        sdPatrolList.setImpression(patrolJSON.getString("impression"));
                        sdPatrolList.setNetwork(patrolJSON.getString("network"));
                        sdPatrolList.setPower(patrolJSON.getString("power"));

               /*     @Excel(name = "设备状态")
                    private String eqStatus;

                    @Excel(name = "运行状态")
                    private String runStatus;*/


                        // ? 缺个巡查时间 xcTime
                        sdPatrolList.setEqStatus(patrolJSON.getString("eqStatus"));
                        sdPatrolList.setRunStatus(patrolJSON.getString("runStatus"));
                        sdPatrolList.setXcTime(patrolJSON.getDate("xcTime"));

                        sdPatrolList.setEqFaultCode(patrolJSON.getString("eqFaultCode"));
                        sdPatrolList.setEqFaultDescription(patrolJSON.getString("eqFaultDescription"));
                        sdPatrolList.setFaultClstatus(patrolJSON.getString("faultClstatus"));
                        sdPatrolList.setXcStatus("1");
                        sdPatrolList.setImgFileId(patrolJSON.getString("imgFileId"));

                        sdPatrolListMapper.updateSdPatrolList(sdPatrolList);
                    }
                }else{
                    error.add(id);
                }
            }

        }
        res.put("success",success);
        res.put("error",error);

        return res;
    }
}
