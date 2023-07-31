package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.core.domain.SdEquipmentCategoryDto;
import com.ruoyi.common.core.domain.TreeCategorySelect;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentCategory;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.dataInfo.SdEquipmentCategoryMapper;
import com.tunnel.business.service.dataInfo.ISdEquipmentCategoryService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备类型Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-06
 */
@Service
public class SdEquipmentCategoryServiceImpl implements ISdEquipmentCategoryService {
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private SdEquipmentCategoryMapper sdEquipmentCategoryMapper;

    /**
     * 查询设备类型
     *
     * @param id 设备类型主键
     * @return 设备类型
     */
    @Override
    public SdEquipmentCategory selectSdEquipmentCategoryById(Long id) {
        return sdEquipmentCategoryMapper.selectSdEquipmentCategoryById(id);
    }

    /**
     * 查询设备类型列表
     *
     * @param sdEquipmentCategory 设备类型
     * @return 设备类型
     */
    @Override
    public List<SdEquipmentCategory> selectSdEquipmentCategoryList(SdEquipmentCategory sdEquipmentCategory) {
        return sdEquipmentCategoryMapper.selectSdEquipmentCategoryList(sdEquipmentCategory);
    }

    /**
     * 新增设备类型
     *
     * @param sdEquipmentCategory 设备类型
     * @return 结果
     */
    @Override
    public int insertSdEquipmentCategory(SdEquipmentCategory sdEquipmentCategory) {
        sdEquipmentCategory.setCreateTime(DateUtils.getNowDate());
        return sdEquipmentCategoryMapper.insertSdEquipmentCategory(sdEquipmentCategory);
    }

    /**
     * 修改设备类型
     *
     * @param sdEquipmentCategory 设备类型
     * @return 结果
     */
    @Override
    public int updateSdEquipmentCategory(SdEquipmentCategory sdEquipmentCategory) {
        sdEquipmentCategory.setUpdateTime(DateUtils.getNowDate());
        return sdEquipmentCategoryMapper.updateSdEquipmentCategory(sdEquipmentCategory);
    }

    /**
     * 批量删除设备类型
     *
     * @param ids 需要删除的设备类型主键
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentCategoryByIds(Long[] ids) {
        return sdEquipmentCategoryMapper.deleteSdEquipmentCategoryByIds(ids);
    }

    /**
     * 删除设备类型信息
     *
     * @param id 设备类型主键
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentCategoryById(Long id) {
        return sdEquipmentCategoryMapper.deleteSdEquipmentCategoryById(id);
    }

    @Override
    public List<SdEquipmentCategoryDto> getCategoryDeviceList(String tunnelId) {
        return sdEquipmentCategoryMapper.getCategoryDeviceList(tunnelId);
    }

    @Override
    public List<SdEquipmentCategoryDto> getCategoryList() {
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> sdTunnels = sdTunnelsService.selectTunnelsList(deptId);
        List<String> tunnelIds = sdTunnels.stream().map(tunnel -> tunnel.getTunnelId()).collect(Collectors.toList());

        //Map参数，根据需要可添加其他参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("tunnelIds",tunnelIds);
        return sdEquipmentCategoryMapper.getCategoryList(paramMap);
    }

    @Override
    public List<SdEquipmentCategoryDto> getCategoryAllList() {
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> sdTunnels = sdTunnelsService.selectTunnelsList(deptId);
        List<String> tunnelIds = sdTunnels.stream().map(tunnel -> tunnel.getTunnelId()).collect(Collectors.toList());

        //Map参数，根据需要可添加其他参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("tunnelIds",tunnelIds);
        return sdEquipmentCategoryMapper.getCategoryAllList(paramMap);
    }

    @Override
    public List<SdEquipmentCategoryDto> getCategoryTypeDeviceList() {
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> sdTunnels = sdTunnelsService.selectTunnelsList(deptId);
        List<String> tunnelIds = sdTunnels.stream().map(tunnel -> tunnel.getTunnelId()).collect(Collectors.toList());

        //Map参数，根据需要可添加其他参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("tunnelIds",tunnelIds);
        return sdEquipmentCategoryMapper.getCategoryTypeDeviceList(paramMap);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param categoryList 设备类型列表
     * @return 树结构列表
     */
    @Override
    public List<SdEquipmentCategoryDto> buildCategoryTree(List<SdEquipmentCategoryDto> categoryList) {
        List<SdEquipmentCategoryDto> returnList = new ArrayList<>();

        List<String> tempList = new ArrayList<>();
        for (SdEquipmentCategoryDto categoryDto : categoryList) {
            tempList.add(categoryDto.getId());
        }

        for (Iterator<SdEquipmentCategoryDto> iterator = categoryList.iterator(); iterator.hasNext(); ) {
            SdEquipmentCategoryDto category = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId())) {
                recursionFn(categoryList, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty()) {
            returnList = categoryList;
        }

        return returnList;
    }


    @Override
    public List<TreeCategorySelect> buildCategoryTreeSelect(List<SdEquipmentCategoryDto> CategoryDtoList) {
        List<SdEquipmentCategoryDto> categoryList = buildCategoryTree(CategoryDtoList);
        return categoryList.stream().map(TreeCategorySelect::new).collect(Collectors.toList());
    }


    /**
     * 递归列表
     */
    private void recursionFn(List<SdEquipmentCategoryDto> list, SdEquipmentCategoryDto t) {
        // 得到子节点列表
        List<SdEquipmentCategoryDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SdEquipmentCategoryDto tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SdEquipmentCategoryDto> getChildList(List<SdEquipmentCategoryDto> list, SdEquipmentCategoryDto t) {
        List<SdEquipmentCategoryDto> tlist = new ArrayList<>();
        Iterator<SdEquipmentCategoryDto> it = list.iterator();
        while (it.hasNext()) {
            SdEquipmentCategoryDto n = (SdEquipmentCategoryDto) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }


    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SdEquipmentCategoryDto> list, SdEquipmentCategoryDto t) {
        return getChildList(list, t).size() > 0;
    }


}
