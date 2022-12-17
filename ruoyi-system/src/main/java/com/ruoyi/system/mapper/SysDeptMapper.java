package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.domain.vo.SysDeptUserTreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 */
public interface SysDeptMapper
{
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询部门管理数据(不包括dept=YG1及其子孙部门)
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> listDeptExcYG1(SysDept dept);

    /**
     * 查询部门管理数据(dept=YG1及其子孙部门)
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> listDeptYG1(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    public List<String> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);


    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门及自身
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    List<SysDept> selectChildrenIncludeSelfById(String deptId);


    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    public void updateDeptStatusNormal(String[] deptIds);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 获取部门用户树
     * lhd
     * @return
     */
    public List<SysDeptUserTreeVO> listDeptUser();

    /**
     * 获取该部门的下级部门和用户
     *
     * @param id
     * @return
     */
    public List<SysDeptUserTreeVO> childrenDeptUser(String id);

    public List<Object> tollById();

    /**
     * 新增巡检点--隧道树结构
     * @param deptId
     * @return
     */
    List<SysDept> selectTunnelDeptList(String deptId);

}
