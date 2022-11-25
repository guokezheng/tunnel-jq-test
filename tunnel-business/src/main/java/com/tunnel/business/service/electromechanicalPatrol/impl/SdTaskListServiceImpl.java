package com.tunnel.business.service.electromechanicalPatrol.impl;

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
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTaskListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        return sdTaskListMapper.selectSdTaskListById(id);
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
        return sdTaskListMapper.selectSdTaskListList(sdTaskList);
    }

    /**
     * 新增巡查任务
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
        sdTaskList.setDispatcher(SecurityUtils.getLoginUser().getUsername());
        sdTaskList.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        sdTaskList.setTaskStatus("0");//待巡查
        result = sdTaskListMapper.insertSdTaskList(sdTaskList);
        if(result>0){
            List<String> list = sdTaskList.getDevicesList();
            list.remove(list.size() - 1);
            List<String> devicesList = new ArrayList<>();
            List<String> faultList = new ArrayList<>();
            if(list!=null&&list.size()>0){
                for(int k=0;k<list.size();k++){
                    String str = list.get(k);
                    String flag = str.substring(str.length() -2,str.length());
                    String strm = str.substring(0,str.length()-2);
                    if("_1".equals(flag)){//巡查点
                        devicesList.add(strm);
                    }else if("_2".equals(flag)){
                        faultList.add(strm);
                    }
                }

            }
            /*新增巡查点*/
            List<SdPatrolList>patrolList = new ArrayList<>();
            if(devicesList!=null&&devicesList.size()>0){
                String[] eqIds = devicesList.toArray(new String[devicesList.size()]);
                List<SdDevices> devices = sdDevicesMapper.batchGetDevicesList(eqIds);
                if(devices!=null&&devices.size()>0){
                    for(int i = 0;i<devicesList.size();i++){
                        SdPatrolList sdPatrolList = new SdPatrolList();
                        sdPatrolList.setId(UUIDUtil.getRandom32BeginTimePK());//id
                        sdPatrolList.setTaskId(taskId);//task_id
                        for(int j=0;j<devices.size();j++){
                            if(devices.get(j).getEqId().equals(devicesList.get(i))){
                                sdPatrolList.setEqFaultId(devicesList.get(i));//eq_fault_id
                                sdPatrolList.setPatrolType("0");//巡检点类型
                                sdPatrolList.setEqName(devices.get(j).getEqName());//eq_name
                                sdPatrolList.setPosition(devices.get(j).getEqDirection()+devices.get(j).getPile());//position
                                sdPatrolList.setXcSort(i);
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
                    for(int i = 0;i<faultList.size();i++){
                        SdPatrolList sdPatrolList = new SdPatrolList();
                        sdPatrolList.setId(UUIDUtil.getRandom32BeginTimePK());//id
                        sdPatrolList.setTaskId(taskId);//task_id
                        for(int j=0;j<fault.size();j++){
                            if(fault.get(j).getId().equals(faultList.get(i))){
                                sdPatrolList.setEqFaultId(faultList.get(i));//eq_fault_id
                                sdPatrolList.setPatrolType("1");//巡检点类型
                                sdPatrolList.setEqName(fault.get(j).getEqName());//eq_name
                                sdPatrolList.setPosition(fault.get(j).getFaultLocation());//position
                                sdPatrolList.setXcSort(i);
                                sdPatrolList.setXcStatus("0");//未巡查
                                patrolList1.add(sdPatrolList);
                            }
                        }
                    }
                    result = sdPatrolListMapper.batchInsertPatrol(patrolList1);
                }
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
    public int updateSdTaskList(SdTaskList sdTaskList,List<SdPatrolList>sdPatrolList)
    {
        int result = 0;
        sdTaskList.setUpdateTime(DateUtils.getNowDate());
        result = sdTaskListMapper.updateSdTaskList(sdTaskList);
        if(result>0){
            result = sdPatrolListMapper.batchDeletePatrolListByTaskId(sdTaskList.getId());
        }
        if(result>0){
            result =  sdPatrolListMapper.batchInsertPatrolList(sdPatrolList,sdTaskList.getId());
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
        return sdTaskListMapper.getTaskInfoList(task_id);
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
    public List<SysDept> selectTableBzDataInfo(Long deptId) {
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
    public int abolishSdTaskList(String id) {
        return sdTaskListMapper.abolishSdTaskList(id);
    }


    /**
     * 查询巡查列表
     * @param tunnelName
     * @param sdTaskList
     * @return
     */
    @Override
    public List<SdTaskList> getTaskList(String tunnelName, SdTaskList sdTaskList) {
        return sdTaskListMapper.getTaskList(tunnelName,sdTaskList);
    }

    /**
     * 查询任务关联的巡查点数量
     * @param id
     * @return
     */
    @Override
    public int countPatrolNum(String id) {
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
    public List<SdTaskList> getTaskSiteCondition(String taskId) {
        List<SdPatrolList> SdPatrolList = sdPatrolListMapper.isFaultEnd(taskId);//所有巡查点的巡查状态
        if(SdPatrolList!=null&&SdPatrolList.size()>0){

        }
        //List<SdTaskList>taskList = sdTaskListMapper.getTaskSiteCondition(taskId);


        return null;
    }

    /**
     * 获取任务关联巡检点信息
     * @param id
     * @return
     */
    @Override
    public List<SdPatrolList> getUpdatePatrolLists(String id) {
        List<SdPatrolList> devicesPatrolList = sdPatrolListMapper.getDevicesPatrolLists(id);
        List<SdPatrolList> faultPatrolList = sdPatrolListMapper.getFaultPatrolLists(id);
        List<SdPatrolList>list = new ArrayList<>();
        list.addAll(devicesPatrolList);
        list.addAll(faultPatrolList);
        List<SdPatrolList> pointList = new ArrayList();
        if(list!=null&&list.size()>0){
            for(int i = 0;i<list.size();i++){
                SdPatrolList sp = new SdPatrolList();
                sp.setTunnel_id(list.get(i).getTunnelId());
                sp.setTunnel_name(list.get(i).getTunnelName());
                sp.setEq_id(list.get(i).getEqFaultId());
                sp.setPile(list.get(i).getPosition());
                sp.setType_name(list.get(i).getTypeName());
                pointList.add(sp);
            }
        }

        return pointList;
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


}
