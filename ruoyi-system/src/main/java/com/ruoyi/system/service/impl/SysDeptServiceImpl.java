package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeDeptSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.vo.SysDeptUserTreeVO;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 *
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }


    /**
     * 查询部门管理数据(不包括dept=YG1及其子孙部门)
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> listDeptExcYG1(SysDept dept)
    {
        return deptMapper.listDeptExcYG1(dept);
    }

//    /**
//     * 查询部门管理数据(dept=YG1及其子孙部门)
//     *
//     * @param dept 部门信息
//     * @return 部门信息集合
//     */
//    @Override
//    @DataScope(deptAlias = "d")
//    public List<SysDept> treeselectYG1(SysDept dept)
//    {
//        return deptMapper.listDeptYG1(dept);
//    }



    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts)
    {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<String> tempList = new ArrayList<String>();
        for (SysDept dept : depts)
        {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext();)
        {
            SysDept dept = (SysDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeDeptSelect> buildDeptTreeSelect(List<SysDept> depts)
    {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeDeptSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<String> selectDeptListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    @Override
    public List<SysDept> selectChildrenDeptById(String deptId) {
        return deptMapper.selectChildrenDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门及自身
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Override
    public List<SysDept> selectChildrenIncludeSelfById(String deptId) {
        return deptMapper.selectChildrenIncludeSelfById(deptId);
    }

    /**
     * 根据ID查询所有子部门及自身
     *状态：正常
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Override
    public List<SysDept> selectChildrenIncludeSelfByIdNormal(String deptId) {
        return deptMapper.selectChildrenIncludeSelfByIdNormal(deptId);
    }

    @Override
    public List<SysDept> listSiteDeptYG1(SysDept dept) {
        return deptMapper.listSiteDeptYG1(dept);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept)
    {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "" : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && !deptId.equals(info.getDeptId()) )
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(String deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept)
    {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        String parentId = dept.getParentId();
        String deptId = generateTree(parentId);//根据选中的父节点生成子节点
        dept.setDeptId(deptId);
        return deptMapper.insertDept(dept);
    }
    private  String generateTree(String parentId) {
        //判断该父节点有没有子节点
        String cid = deptMapper.selectChildByPid(parentId);
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


    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept)
    {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept)
    {
        String ancestors = dept.getAncestors();
        String[] deptIds = Convert.toStrArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t)
    {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList)
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
    private List<SysDept> getChildList(List<SysDept> list, SysDept t)
    {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext())
        {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t)
    {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public List<SysDeptUserTreeVO> treeDeptUser() {
        List<SysDeptUserTreeVO> sysDeptUserTreeVOS = deptMapper.listDeptUser();
        for (SysDeptUserTreeVO sysDeptUserTreeVO : sysDeptUserTreeVOS) {
            if(sysDeptUserTreeVO.getIsUser() != 1) {
                List<SysDeptUserTreeVO> children = deptMapper.childrenDeptUser(sysDeptUserTreeVO.getId());
                childrenDeptUser(children);
                sysDeptUserTreeVO.setChildren(children);
                sysDeptUserTreeVO.setShowCheckbox(false);
            } else {
                sysDeptUserTreeVO.setShowCheckbox(true);
            }
        }
        return sysDeptUserTreeVOS;
    }

    @Override
    public List<Object> tollById() {
        return  deptMapper.tollById();
    }

    @Override
    public List<SysDept> selectTunnelDeptList(String deptId) {
        return deptMapper.selectTunnelDeptList(deptId);
    }


    @Override
    public List<SysDept> selectTunnelDeptListBydw(String ssdw) {
        return deptMapper.selectTunnelDeptListBydw(ssdw);
    }

    @Override
    public List<SysDept> listDeptYG1(SysDept dept) {
        return deptMapper.listDeptYG1(dept);
    }

    @Override
    public String getParentDept(String deptId) {
        return deptMapper.getParentDept(deptId);
    }

    private void childrenDeptUser(List<SysDeptUserTreeVO> list) {
        for (SysDeptUserTreeVO sysDeptUserTreeVO : list) {
            if(sysDeptUserTreeVO.getIsUser() != 1) {
                sysDeptUserTreeVO.setShowCheckbox(false);
            } else {
                sysDeptUserTreeVO.setShowCheckbox(true);
            }
            List<SysDeptUserTreeVO> children = deptMapper.childrenDeptUser(sysDeptUserTreeVO.getId());
            childrenDeptUser(children);
            sysDeptUserTreeVO.setChildren(children);
        }
    }


}
