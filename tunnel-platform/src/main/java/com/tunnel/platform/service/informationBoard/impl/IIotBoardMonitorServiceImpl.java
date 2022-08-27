package com.tunnel.platform.service.informationBoard.impl;

import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.platform.domain.informationBoard.SdIotDevice;
import com.tunnel.platform.mapper.informationBoard.IotBoardMonitorMapper;
import com.tunnel.platform.service.informationBoard.IIotBoardMonitorService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.utils.core.domain.Ztree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控台情报板Service业务层处理
 * 
 * @author yangqichao
 * @date 2020-03-25
 */
@Service
public class IIotBoardMonitorServiceImpl implements IIotBoardMonitorService
{
    @Autowired
    private IotBoardMonitorMapper iotBoardMapper;
    @Autowired
    private ISysDeptService deptMapper;

    @Override
    public List<Ztree> selectBoardTree(SdIotDevice SdIotDevice) {
        List<SysDept> deptList = deptMapper.selectDeptList(new SysDept());
        List<HashMap<String, Object>> hashMaps = iotBoardMapper.selectDeviceInfo();
        for (Map<String,Object> map: hashMaps) {
            SysDept sysDept = new SysDept();
            String deptName;
           // String deptName=(String) map.get("route_number")+"("+(String) map.get("pile_number")+")"+"("+(String) map.get("device_status")+")";
            if (map.get("route_number")==null){
                 deptName=(String) ""+"("+(String) map.get("pile_number")+")";
            }else{
                 deptName=(String) map.get("route_number")+"("+(String) map.get("pile_number")+")";
            }
            Long ParentId = (Long) map.get("manage_agency_id");
            Long deptId = (Long) map.get("device_id");
            sysDept.setDeptId(deptId);
            sysDept.setDeptName(deptName);
            sysDept.setParentId(ParentId);
            sysDept.setStatus("0");
            deptList.add(sysDept);
        }
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    @Override
    public Map getDeviceInfo(Long deviceId) {
        return iotBoardMapper.getDeviceInfo(deviceId);
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList)
    {
        return initZtree(deptList, null);
    }
    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList)
    {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysDept dept : deptList)
        {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()))
            {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck)
                {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }
}
