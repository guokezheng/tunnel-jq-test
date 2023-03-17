package com.tunnel.business.service.event;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdEventType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 事件类型Service接口
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface ISdEventTypeService {
    /**
     * 查询事件类型
     *
     * @param id 事件类型ID
     * @return 事件类型
     */
    SdEventType selectSdEventTypeById(Long id);

    /**
     * 查询事件类型列表
     *
     * @param sdEventType 事件类型
     * @return 事件类型集合
     */
    List<SdEventType> selectSdEventTypeList(SdEventType sdEventType);

    /**
     * 新增事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    AjaxResult insertSdEventType(MultipartFile[] file, SdEventType sdEventType);

    /**
     * 修改事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    AjaxResult updateSdEventType(MultipartFile[] file,SdEventType sdEventType);

    /**
     * 批量删除事件类型
     *
     * @param ids 需要删除的事件类型ID
     * @return 结果
     */
    int deleteSdEventTypeByIds(Long[] ids);

    /**
     * 删除事件类型信息
     *
     * @param id 事件类型ID
     * @return 结果
     */
    int deleteSdEventTypeById(Long id);


    /**
     * 获取所有事件类型Map格式
     * key: id,value: 事件类型名称
     * @return
     */
    Map<Long,String> getEventTypeMap();
}
