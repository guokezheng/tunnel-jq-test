package com.tunnel.business.service.emeResource.impl;

import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.emeResource.SdMaterial;
import com.tunnel.business.domain.emeResource.SdMaterialRecord;
import com.tunnel.business.domain.event.SdEmergencyPer;
import com.tunnel.business.mapper.emeResource.SdMaterialMapper;
import com.tunnel.business.mapper.emeResource.SdMaterialRecordMapper;
import com.tunnel.business.service.emeResource.ISdMaterialService;
import com.tunnel.business.service.event.impl.SdEmergencyPerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 应急资源Service业务层处理
 *
 * @author xuebi
 * @date 2020-08-21
 */
@Service
public class SdMaterialServiceImpl implements ISdMaterialService {
    private static final Logger logger = LoggerFactory.getLogger(SdEmergencyPerServiceImpl.class);
    @Autowired
    private SdMaterialMapper sdMaterialMapper;

    @Autowired
    private SdMaterialRecordMapper sdMaterialRecordMapper;

    private int warehousing;

    private int warehouseOut;


    /**
     * 查询应急资源
     *
     * @param id 应急资源ID
     * @return 应急资源
     */
    @Override
    public SdMaterial selectSdMaterialById(Long id) {
        return sdMaterialMapper.selectSdMaterialById(id);
    }

    /**
     * 查询应急资源列表
     *
     * @param sdMaterial 应急资源
     * @return 应急资源
     */
    @Override
    public List<SdMaterial> selectSdMaterialList(SdMaterial sdMaterial) {
        String deptId = SecurityUtils.getDeptId();
        sdMaterial.setDeptId(deptId);
        List<SdMaterial> sdMaterialsList = sdMaterialMapper.selectSdMaterialList(sdMaterial);
        if (sdMaterialsList.size() > 0 && sdMaterial.getStation() != null && sdMaterial.getEndStation() != null
                && sdMaterial.getStation() != "" && sdMaterial.getEndStation() != "") {
            List<SdMaterial> lists = new ArrayList<>();
            //桩号范围开始节点转化
            String begin = sdMaterial.getStation();
            begin = begin.substring(begin.indexOf(".") + 1);
            BigDecimal beginStation = new BigDecimal(begin);
            //桩号范围结束节点转化
            String end = sdMaterial.getEndStation();
            end = end.substring(end.indexOf(".") + 1);
            BigDecimal endStation = new BigDecimal(end);
            //处理查询结果集
            for (int i = 0; i < sdMaterialsList.size(); i++) {
                SdMaterial material = sdMaterialsList.get(i);
                String station = material.getStation();
                String exactStation = "";
                String offset = "";
                if (station.contains("+")) {
                    exactStation = station.substring(station.indexOf("K") + 1, station.indexOf("+"));
                    offset = station.substring(station.indexOf("+") + 1);
                } else {
                    exactStation = station.substring(station.indexOf("K") + 1);
                    offset = "0";
                }
                station = exactStation.concat(".").concat(offset);
                BigDecimal currentStation = new BigDecimal(station);
                if (currentStation.compareTo(beginStation) >= 0 && currentStation.compareTo(endStation) <= 0) {
                    lists.add(material);
                }
            }
            return lists;
        } else {
            return sdMaterialsList;
        }
    }

    /**
     * 新增应急资源
     *
     * @param sdMaterial 应急资源
     * @return 结果
     */
    @Override
    public int insertSdMaterial(SdMaterial sdMaterial) {
        //添加前先检查当前隧道中新增应急物资是否已经存在
        List<SdMaterial> sdMaterialList = sdMaterialMapper.selectMaterialList(sdMaterial);
        if (sdMaterialList.size() > 0) {
            logger.error("当前物资已经存在！");
            //return 0;
            throw new RuntimeException("当前物资已经存在！");
        }
        sdMaterial.setCreateTime(DateUtils.getNowDate());
        return sdMaterialMapper.insertSdMaterial(sdMaterial);
    }

    @Override
    public int updateSdMaterial(SdMaterial sdMaterial) {
        sdMaterial.setUpdateTime(DateUtils.getNowDate());
        return sdMaterialMapper.updateSdMaterial(sdMaterial);
    }

    /**
     * 应急资源出入库
     *
     * @param sdmaterialRecord 应急资源
     * @return 结果
     */
    @Override
    public int insertSdMaterialRecord(SdMaterialRecord sdmaterialRecord) {
        SdMaterial SdMaterial = sdMaterialMapper.selectSdMaterialById(sdmaterialRecord.getMaterialId());
        int change = sdmaterialRecord.getChangeStock().intValue();//要出入库的数量
        int originalStock = SdMaterial.getInventoryQuantity().intValue();//原库存数量
        int warningValue = SdMaterial.getWarningValue().intValue();//警戒值
        String type = sdmaterialRecord.getType();
        int changeStock = originalStock;
        if ("1".equals(type)) {//入库
            changeStock = change + originalStock;//入库之后数量

        }
        if ("2".equals(type)) {//出库
            changeStock = originalStock - change;//originalStock指原库存数量//change指出库编辑的数量
        }
        if (changeStock >= warningValue) {
            SdMaterial.setState("2");//正常
        } else if (changeStock > 0) {
            SdMaterial.setState("1");//正常
        } else {
            SdMaterial.setState("3");//正常
        }
        sdmaterialRecord.setUserName(SdMaterial.getCreateBy());
        sdmaterialRecord.setCreateBy(SecurityUtils.getUsername());
        sdmaterialRecord.setType(type);
        sdmaterialRecord.setCreateTime(DateUtils.getNowDate());
        sdmaterialRecord.setStock(changeStock);
        sdMaterialRecordMapper.insertSdMaterialRecord(sdmaterialRecord);
        SdMaterial.setUpdateTime(DateUtils.getNowDate());
        SdMaterial.setInventoryQuantity(changeStock);
        return sdMaterialMapper.updateMaterialMessage(SdMaterial);
    }

    /**
     * 批量删除应急资源
     *
     * @param ids 需要删除的应急资源ID
     * @return 结果
     */
    @Override
    public int deleteSdMaterialByIds(Long[] ids) {
        return sdMaterialMapper.deleteSdMaterialByIds(ids);
    }

    /**
     * 删除应急资源信息
     *
     * @param id 应急资源ID
     * @return 结果
     */
    @Override
    public int deleteSdMaterialById(Long id) {
        return sdMaterialMapper.deleteSdMaterialById(id);
    }

    /**
     * 查询应急资源记录
     */
    @Override
    public List<SdMaterialRecord> selectSdMaterialRecordList(SdMaterialRecord sdmaterialrecord) {
        return sdMaterialRecordMapper.selectSdMaterialRecordList(sdmaterialrecord);
    }
}
