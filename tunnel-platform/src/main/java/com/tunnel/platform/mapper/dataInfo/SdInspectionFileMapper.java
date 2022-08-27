package com.tunnel.platform.mapper.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdInspectionFile;
import org.apache.ibatis.annotations.Param;

/**
 * 巡检任务文件Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-25
 */
public interface SdInspectionFileMapper
{
    /**
     * 查询巡检任务文件
     *
     * @param id 巡检任务文件主键
     * @return 巡检任务文件
     */
    public SdInspectionFile selectSdInspectionFileById(Long id);

    /**
     * 查询巡检任务文件列表
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 巡检任务文件集合
     */
    public List<SdInspectionFile> selectSdInspectionFileList(SdInspectionFile sdInspectionFile);

    /**
     * 新增巡检任务文件
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 结果
     */
    public int insertSdInspectionFile(SdInspectionFile sdInspectionFile);

    /**
     * 修改巡检任务文件
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 结果
     */
    public int updateSdInspectionFile(SdInspectionFile sdInspectionFile);

    /**
     * 删除巡检任务文件
     *
     * @param id 巡检任务文件主键
     * @return 结果
     */
    public int deleteSdInspectionFileById(Long id);

    /**
     * 批量删除巡检任务文件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdInspectionFileByIds(Long[] ids);

    public int brachInsertSdInspectionFile(List<SdInspectionFile> list);

    public int updateFileId(@Param("planfileid") String planfileid, @Param("oldid") String oldid);

    public int deleteFileByFileIds(@Param("pFileId") String pFileId);
}
