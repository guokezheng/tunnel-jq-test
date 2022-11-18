package com.tunnel.business.service.electromechanicalPatrol.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.DeptTunnelTreeSelect;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTaskListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.utils.util.UUIDUtil;
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
    public int insertSdTaskList(SdTaskList sdTaskList, List<SdPatrolList>sdPatrolList)
    {
        int flag = -1;
        sdTaskList.setId(UUIDUtil.getRandom32BeginTimePK());
        sdTaskList.setZzjgId(String.valueOf(SecurityUtils.getDeptId()));
        sdTaskList.setCreateTime(DateUtils.getNowDate());
        flag = sdTaskListMapper.insertSdTaskList(sdTaskList);//添加巡查任务
        if(flag==1){//添加巡查点
            flag = sdPatrolListMapper.batchInsertPatrolList(sdPatrolList,sdTaskList.getId());
        }
        return flag;
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
    public int deleteSdTaskListByIds(String[] ids)
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
