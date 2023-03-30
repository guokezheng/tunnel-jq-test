package com.tunnel.business.service.electromechanicalPatrol.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.DeptTunnelTreeSelect;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;


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
        List<SdTaskList> taskLists = new ArrayList<>();
        if(sdTaskList.getTaskStatus()!=null&&!"".equals(sdTaskList.getTaskStatus())&&TaskStatus.YICHAOSHI.getCode().equals(sdTaskList.getTaskStatus())) {
            //已超时
            List<SdTaskList> taskListsAll = sdTaskListMapper.selectChaoshiSdTaskListList(sdTaskList);
            if(taskListsAll!=null&&taskListsAll.size() >0){
                taskLists = getChaoshiTaskList(taskListsAll);
            }

        }else{
            taskLists = sdTaskListMapper.selectSdTaskListList(sdTaskList);
            for(int i = 0;i<taskLists.size();i++){
                if(taskLists.get(i).getTaskStatus()!=null&&!"".equals(taskLists.get(i).getTaskStatus())){
                    taskListStatus(taskLists.get(i));
                }
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
        if(taskList.getTask().equals(TaskStatus.DAIXUNCHA.getName())&&taskList.getEndPlantime()!=null){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            long diff = System.currentTimeMillis() - time + 1000;
            if(diff>0){//当前时间与计划完成时间的差值
                taskList.setTask(TaskStatus.DAIXUNCHA.getName()+","+TaskStatus.YICHAOSHI.getName());
            }else{
                taskList.setTask(TaskStatus.DAIXUNCHA.getName());
            }

        }
        if(taskList.getTask().equals(TaskStatus.XUNCHAZHONG.getName())&&taskList.getEndPlantime()!=null){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            long diff = System.currentTimeMillis() - time;
            if(diff>0){//当前时间与计划完成时间的差值
                taskList.setTask(TaskStatus.XUNCHAZHONG.getName()+","+TaskStatus.YICHAOSHI.getName());
            }else{
                taskList.setTask(TaskStatus.XUNCHAZHONG.getName());
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
        String taskId = UUIDUtil.getRandom32BeginTimePK();//任务编号
        sdTaskList.setId(taskId);//id
        sdTaskList.setZzjgId(SecurityUtils.getLoginUser().getDeptId());
        sdTaskList.setDispatcher(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
        sdTaskList.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        //sdTaskList.setTaskStatus("0");//待巡查
        result = sdTaskListMapper.insertSdTaskList(sdTaskList);
        if(result>0){
            /*添加操作记录*/
            if("0".equals(sdTaskList.getTaskStatus())){//发布任务
                SdTaskOpt sdTaskOpt = new SdTaskOpt();
                sdTaskOpt.setId(UUIDUtil.getRandom32BeginTimePK());
                sdTaskOpt.setTaskId(taskId);
                sdTaskOpt.setOptType("0");
                sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
                result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
            }

        }
        if(result>0){
            result = insertPatrol(sdTaskList,taskId);
        }
        return result;
    }

    private int insertPatrol(SdTaskList sdTaskList,String taskId){
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
                            patrolList1.add(sdPatrolList);
                        }
                    }
                }
                result = sdPatrolListMapper.batchInsertPatrol(patrolList1);
            }
        }


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
        sdTaskList.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        sdTaskList.setUpdateTime(DateUtils.getNowDate());
        result = sdTaskListMapper.updateSdTaskList(sdTaskList);
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
                sdTaskOpt.setOptType("0");
                sdTaskOpt.setOptPersonId(String.valueOf(SecurityUtils.getLoginUser().getUserId()));
                result = sdTaskListMapper.insertTaskOpt(sdTaskOpt);
            }

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
            if(taskList.get(0).getTaskStatus()!=null&&!"".equals(taskList.get(0).getTaskStatus())){

                taskStatus(taskList.get(0));

                if(taskList.get(0).getTaskCxtime()==null||"".equals(taskList.get(0).getTaskCxtime())){//没有持续时间
                    //任务持续时间为 当前时间-发布时间
                    if(taskList.get(0).getEndPlantime()!=null&&!"".equals(taskList.get(0).getEndPlantime())){
                        taskList.get(0).setTaskCxtime(lengthTime(taskList.get(0).getDispatchTime()));
                    }
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
        if(taskList.getTaskStatus().equals(TaskStatus.DAIXUNCHA.getName()) &&(taskList.getEndPlantime()!=null)){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            long diff = System.currentTimeMillis() - time + 1000;
            if(diff>0){//当前时间与计划完成时间的差值
                taskList.setTaskStatus(TaskStatus.DAIXUNCHA.getName());
                taskList.setIfchaosgu(TaskStatus.YICHAOSHI.getName());
            }else{
                taskList.setTaskStatus(TaskStatus.DAIXUNCHA.getName());
                taskList.setIfchaosgu("");//未超时
            }

        }
        if(taskList.getTaskStatus().equals(TaskStatus.XUNCHAZHONG.getName())&&(taskList.getEndPlantime()!=null)){
            String  plantime = sdf.format(taskList.getEndPlantime());//计划完成时间
            long time = sdf.parse(plantime, new ParsePosition(0)).getTime();
            long diff = System.currentTimeMillis() - time;
            if(diff>0){//当前时间与计划完成时间的差值
                taskList.setTaskStatus(TaskStatus.XUNCHAZHONG.getName());
                taskList.setIfchaosgu(TaskStatus.YICHAOSHI.getName());
            }else{
                taskList.setTaskStatus(TaskStatus.XUNCHAZHONG.getName());
                taskList.setIfchaosgu("");//未超时
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
        System.out.println(day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
        return day + "天" + hour + "小时";

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
            for(int i =0;i<sdPatrolList.size();i++){
                String fileId = sdPatrolList.get(i).getImgFileId();
                if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                    SdTrafficImage sdTrafficImage = new SdTrafficImage();
                    sdTrafficImage.setBusinessId(fileId);
                    sdPatrolList.get(i).setiFileList(sdTrafficImageMapper.selectFaultImgFileList(sdTrafficImage));
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
    public List<SysDept> selectTableBzDataInfo(String deptId) {
        return sdTaskListMapper.selectTableBzDataInfo(deptId);
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
    public List<SdTaskList> getTaskList(String taskStatus,String taskName,String startTime,String endTime ) {

        return sdTaskListMapper.getTaskList(taskStatus,taskName,startTime,endTime);
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
    public int acceptSdTaskList(String id) {
        return sdTaskListMapper.acceptSdTaskList(id);
    }

    /**
     * app巡查点清单
     * @param taskId
     * @return
     */
    @Override
    public List<SdPatrolList> getPatrolInfo(String taskId) {
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
        //List<SdTaskList>taskList = sdTaskListMapper.getTaskSiteCondition(taskId);


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
        return sdTaskListMapper.saveLocal(sdTaskList);
    }

    /**
     * 巡查点检修情况保存
     * @param sdPatrolList
     * @return
     */
    @Override
    public int savePatrol(SdPatrolList sdPatrolList) {
        return sdPatrolListMapper.savePatrol(sdPatrolList);
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
    public List<SdTaskList> getTaskToDo(String deptId) {
        return sdTaskListMapper.getTaskToDo(deptId);
    }

    @Override
    public String selectBzByTunnel(String tunnelId) {
        return sdTaskListMapper.selectBzByTunnel(tunnelId);
    }

}
