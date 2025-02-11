package com.zc.iot.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.common.core.foreignkeyservice.AssociatedService;
import com.zc.common.core.foreignkeyservice.ForeignKeyService;
import com.zc.common.core.foreignkeyservice.LinkModel;
import com.zc.iot.domain.IotGroup;
import com.zc.iot.mapper.IotGroupMapper;
import com.zc.iot.service.IIotGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 分组Service业务层处理
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-16
 */
@Service
public class IotGroupServiceImpl  extends AssociatedService implements IIotGroupService, ForeignKeyService
{
    @Autowired
    private IotGroupMapper iotGroupMapper;

    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /*外键注册*/
        registerForeignKey();
        /*
          添加数据到 redis 缓存
         */
        addIotGroupCache();
    }

    /**
     * 添加数据到 redis 缓存
     */
    public void addIotGroupCache()
    {
        // 获取全部品类列表数据
        List<IotGroup> iotCategories = selectIotGroupList(null);
        if (iotCategories == null || iotCategories.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_GROUP);

        // 添加 redis 缓存数据
        iotCategories.forEach(val -> redisCache.setCacheMapValue(RedisKeyConstants.IOT_GROUP, val.getId(), val));
    }

    /**
     * 查询分组
     * 
     * @param id 分组主键
     * @return 分组
     */
    @Override
    public IotGroup selectIotGroupById(Long id)
    {
        return iotGroupMapper.selectIotGroupById(id);
    }

    /**
     * 查询分组列表
     * 
     * @param iotGroup 分组
     * @return 分组
     */
    @Override
    public List<IotGroup> selectIotGroupList(IotGroup iotGroup)
    {
        return iotGroupMapper.selectIotGroupList(iotGroup);
    }

    /**
     * 新增分组
     * 
     * @param iotGroup 分组
     * @return 结果
     */
    @Override
    public int insertIotGroup(IotGroup iotGroup)
    {
        iotGroup.setCreateTime(DateUtils.getNowDate());

        int result = iotGroupMapper.insertIotGroup(iotGroup);
        // 添加到缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_GROUP, iotGroup.getId(), iotGroup);
        }
        return result;
    }

    /**
     * 修改分组
     * 
     * @param iotGroup 分组
     * @return 结果
     */
    @Override
    public int updateIotGroup(IotGroup iotGroup)
    {

        if (iotGroup == null)
        {
            return 0;
        }

        Long parentId = iotGroup.getParentId();
        Long id = iotGroup.getId();

        if (parentId == null || id == null || parentId.equals(id))
        {
            return 0;
        }

        if (selectIotGroupById(id)  == null)
        {
            return 0;
        }

        // 判断是否是个从属组织，自己不能是自己的从属组织
        if (isSubordinateGroup(id, parentId))
        {
            return 0;
        }

        iotGroup.setUpdateTime(DateUtils.getNowDate());

        int result = iotGroupMapper.updateIotGroup(iotGroup);
        // 更新缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_GROUP, iotGroup.getId(), iotGroup);
        }
        return result;
    }

    /**
     * 批量删除分组
     * 
     * @param ids 需要删除的分组主键
     * @return 结果
     */
    @Override
    public int deleteIotGroupByIds(Long[] ids)
    {
        if (!canHardDelete(ids)) return 0;

        int result = iotGroupMapper.deleteIotGroupByIds(ids);

        /*删除缓存数据*/
        if (result > 0)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_GROUP, ids);
        }

        return result;
    }

    /**
     * 删除分组信息
     * 
     * @param id 分组主键
     * @return 结果
     */
    @Override
    public int deleteIotGroupById(Long id)
    {
        if (!canHardDelete(id)) return 0;

        int result = iotGroupMapper.deleteIotGroupById(id);

        /*删除缓存*/
        if (result == 1)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_GROUP, id);
        }

        return result;
    }

    /**
     * 判断id2 是否是id1的从属组织
     * @param id1
     * @param id2
     * @return
     */
    public Boolean isSubordinateGroup(Long id1, Long id2)
    {
        if (null == id1 || null == id2)
        {
            return false;
        }

        if (id1.equals(id2))
        {
            return true;
        }

        IotGroup iotGroup = selectIotGroupById(id2);

        if (null == iotGroup)
        {
            return false;
        }

        Long parentId = iotGroup.getParentId();

        if (id1.equals(parentId))
        {
            return true;
        }

        return isSubordinateGroup(id1, parentId);
    }

    @Override
    public LinkModel findByForeignKey(String foreignKey, Long foreignKeyValue) {
        IotGroup iotGroup = new IotGroup();
        iotGroup.setParentId(foreignKeyValue);
        List<IotGroup> iotGroups = selectIotGroupList(iotGroup);
        if (iotGroups == null || iotGroups.isEmpty())
        {
            return new LinkModel(0);
        }
        return new LinkModel(iotGroups.size());
    }

    @Override
    public boolean deleteAllByForeignKey(String foreignKey, Long foreignKeyValue) {
        return false;
    }

    @Override
    public void registerForeignKey() {
       super.registerForeignKey(this, "parentId");
    }
}
