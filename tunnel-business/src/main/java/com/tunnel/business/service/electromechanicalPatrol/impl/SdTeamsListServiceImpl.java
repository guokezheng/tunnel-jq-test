package com.tunnel.business.service.electromechanicalPatrol.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.mapper.SysUserMapper;
import com.tunnel.business.domain.electromechanicalPatrol.SdTeamsList;
import com.tunnel.business.domain.electromechanicalPatrol.SdUserTeams;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTeamsListMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdTeamsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 班组Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class SdTeamsListServiceImpl implements ISdTeamsListService
{

    @Autowired
    private SdTeamsListMapper sdTeamsListMapper;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询班组列表
     * @param sdTeamsList
     * @return
     */
    @Override
    public List<SdTeamsList> selectSdTeamsList(SdTeamsList sdTeamsList) {
        return sdTeamsListMapper.selectSdTeamsList(sdTeamsList);
    }

    @Override
    public String checkTeamsNameUnique(SdTeamsList sdTeamsList) {
        String deptId = StringUtils.isNull(sdTeamsList.getDeptId()) ? "" : sdTeamsList.getDeptId();
        SdTeamsList info = sdTeamsListMapper.checkTeamsNameUnique(sdTeamsList.getDeptName(), sdTeamsList.getParentId());
        if (StringUtils.isNotNull(info) && !deptId.equals(info.getDeptId()) )
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertTeams(SdTeamsList sdTeamsList) {
        SdTeamsList info = sdTeamsListMapper.selectTeamsById(sdTeamsList.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new ServiceException("部门停用，不允许新增");
        }
        sdTeamsList.setAncestors(info.getAncestors() + "," + sdTeamsList.getParentId());
        String parentId = sdTeamsList.getParentId();
        String deptId = generateTree(parentId);//根据选中的父节点生成子节点
        sdTeamsList.setDeptId(deptId);
        return sdTeamsListMapper.insertTeams(sdTeamsList);
    }

    private  String generateTree(String parentId) {
        //判断该父节点有没有班组
        String cid = sdTeamsListMapper.selectTeamsChildByPid(parentId);
        if(cid != null&&!"".equals(cid)){
            String codeTmp = cid.substring(cid.length() - 2); // 获取字符串最后两个字符
            int num = StrToInt(codeTmp) + 1;
            if(num>=10) {
                parentId += num;
            }else
                parentId += ("0"+num);
        }else
            parentId += "01";

        return parentId;

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

    @Override
    public int updateTeams(SdTeamsList sdTeamsList) {
        return sdTeamsListMapper.updateTeams(sdTeamsList);
    }

    /**
     * 根据id查询班组详情
     * @param deptId
     * @return
     */
    @Override
    public SdTeamsList selectTeamsById(String deptId) {
        return sdTeamsListMapper.selectTeamsInfoById(deptId);
    }



    @Override
    public int deleteTeamsById(String deptId) {
        return sdTeamsListMapper.deleteTeamsById(deptId);
    }

    @Override
    public int teamsUserSelectAll(String deptId, Long[] userIds) {

        int rows = 1;

        // 新增人员与班组管理
        List<SdUserTeams> list = new ArrayList<SdUserTeams>();
        for (int i= 0;i<userIds.length;i++)
        {
            SdUserTeams rt = new SdUserTeams();
            rt.setTeamsId(deptId);
            rt.setUserId(userIds[i]);
            list.add(rt);
        }
        if (list.size() > 0)
        {
            rows = sdTeamsListMapper.batchUserTeams(list);
        }
        return rows;
    }

    @Override
    public boolean checkTeamsExistUser(String deptId) {
        int result = sdTeamsListMapper.checkTeamsExistUser(deptId);
        return result > 0;
    }
}
