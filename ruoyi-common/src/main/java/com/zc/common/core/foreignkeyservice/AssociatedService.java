package com.zc.common.core.foreignkeyservice;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 被关联服务抽象类
 * @author Athena-xiepufeng
 */
public abstract class AssociatedService
{
    /*
     * ForeignKeyService :对应的service
     */
    private final Map<ForeignKeyService, String> serviceForeignKeyMap = new HashMap<>();

    /**
     * 判断实体是否能够被硬删除。如果实体被其他实体关联，则不能被硬删除。
     *
     * @param id
     *            本实体ID
     * @return
     */
    public boolean canHardDelete(Long id)
    {

        if (id == null || serviceForeignKeyMap.isEmpty())
        {
            return true;
        }

        for (Map.Entry<ForeignKeyService, String> foreignKeyService : serviceForeignKeyMap.entrySet())
        {
            if (foreignKeyService.getKey().countByForeignKey(foreignKeyService.getValue(), id) > 0)
            {
                return false;
            }
        }

        return true;
    }
    /**
     * 判断实体是否能够被硬删除。如果实体被其他实体关联，则不能被硬删除。
     *
     * @param ids
     *            本实体ID
     * @return
     */
    public boolean canHardDelete(Long[] ids)
    {
        if (ids == null || ids.length <= 0 || serviceForeignKeyMap.isEmpty())
        {
            return true;
        }

        for (Long id : ids)
        {
            for (Map.Entry<ForeignKeyService, String> foreignKeyService : serviceForeignKeyMap.entrySet())
            {
                if (foreignKeyService.getKey().countByForeignKey(foreignKeyService.getValue(), id) > 0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 判断实体是否能够被硬删除。如果实体被其他实体关联，则不能被硬删除。
     *
     * @param id
     *            本实体ID
     * @return
     */
    public LinkModel getWhoReferenceMe(Long id)
    {

        if (id == null || serviceForeignKeyMap.isEmpty())
        {
            return null;
        }
        for (Map.Entry<ForeignKeyService, String> foreignKeyService : serviceForeignKeyMap.entrySet())
        {
            LinkModel model = foreignKeyService.getKey().findByForeignKey(foreignKeyService.getValue(), id);

            if (model != null)
            {
                return model;
            }
        }

        return null;
    }

    /**
     * 判断实体是否能够被硬删除。如果实体被其他实体关联，则不能被硬删除。
     *
     * @param ids
     *            本实体ID
     * @return
     */
    public LinkModel getWhoReferenceMe(Long[] ids)
    {

        if (ids == null || serviceForeignKeyMap.isEmpty() || ids.length <= 0)
        {
            return null;
        }

        for (Long id : ids)
        {
            for (Map.Entry<ForeignKeyService, String> foreignKeyService : serviceForeignKeyMap.entrySet())
            {

                LinkModel model = foreignKeyService.getKey().findByForeignKey(foreignKeyService.getValue(), id);

                if (model != null)
                {
                    return model;
                }
            }
        }

        return null;
    }

    /**
     * 删除所有关联数据
     * @param id
     *        本实体ID
     * @return
     */
    @Transactional
    public boolean deleteAssociatedDataById (Long id)
    {
        if (id == null)
        {
            return false;
        }

        if (serviceForeignKeyMap.isEmpty())
        {
            return true;
        }

        for (Map.Entry<ForeignKeyService, String> foreignKeyService : serviceForeignKeyMap.entrySet())
        {
            if (foreignKeyService.getKey().countByForeignKey(foreignKeyService.getValue(), id) > 0)
            {
                foreignKeyService.getKey().deleteAllByForeignKey(foreignKeyService.getValue(), id);
            }
        }

        return true;

    }

    /**
     * 注册外键服务
     *
     * @param subscriber
     *            外键服务
     * @param foreignKey
     *            外键名称
     */
    public void registerForeignKey(ForeignKeyService subscriber, String foreignKey)
    {
        serviceForeignKeyMap.put(subscriber, foreignKey);
    }
}
