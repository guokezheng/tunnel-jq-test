package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeDeptSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.domain.vo.SysDeptUserTreeVO;

import java.util.List;

/**
 * 部门管理 服务层
 *
 *
 */
public interface ISysDeptService
{
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询部门管理数据(不包括dept_id=YG1及其子孙部门)
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> listDeptExcYG1(SysDept dept);

//    /**
//     * 查询部门管理数据(只查询 dept_id=YG1及其子孙部门)
//     *
//     * @param dept 部门信息
//     * @return 部门信息集合
//     */
//    public List<SysDept> treeselectYG1(SysDept dept);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeDeptSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<String> selectDeptListByRoleId(Long roleId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门及自身
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenIncludeSelfById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    public void checkDeptDataScope(String deptId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 获取组织用户树
     * lhd
     * @return
     */
    public List<SysDeptUserTreeVO> treeDeptUser();

    public List<Object> tollById();

    /**
     * 新增巡检点--隧道树结构
     * @param deptId
     * @return
     */
    List<SysDept> selectTunnelDeptList(String deptId);

    /**
     * 查询单位
     * @param
     * @param ssdw
     * @return
     */
    List<SysDept> selectTunnelDeptListBydw(String ssdw);

    /**
     * 管养部门树
     * @param dept
     * @return
     */
    List<SysDept> listDeptYG1(SysDept dept);

    String getParentDept(String deptId);

    /**
     * 根据ID查询所有子部门及自身(状态：正常)
     * @param deptId
     * @return
     */
    List<SysDept> selectChildrenIncludeSelfByIdNormal(String deptId);

    List<SysDept> listSiteDeptYG1(SysDept dept);
}
